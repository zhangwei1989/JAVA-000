## 第三周作业

### 作业一
作业题：整合你上次作业的 httpclient/okhttp。

上次使用的是 httpclient，整合到老师的示范网关里，这里整合的是 httpclient 最简单的版本：

```java
public HttpClientOutBoundHandler(String backendUrl){
        this.backendUrl = backendUrl.endsWith("/")?backendUrl.substring(0,backendUrl.length()-1):backendUrl;
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();//.DiscardPolicy();
        proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);

        httpclient = HttpClients.createDefault();
    }
```

```java
private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
        final HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);

        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            handleResponse(inbound, ctx, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final HttpResponse endpointResponse) {
        FullHttpResponse response = null;
        try {
            byte[] body = EntityUtils.toByteArray(endpointResponse.getEntity());

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", Integer.parseInt(endpointResponse.getFirstHeader("Content-Length").getValue()));

        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(response);
                }
            }
            ctx.flush();
        }
    }
```

---

### 作业二
作业题：（选做）使用 netty 实现后端 http 访问（代替上一步骤）

在这里将 httpclient 和 netty 作为客户端进行了整合，因为做的事情是一样的：

```java
public class NettyHttpClientAsyncGet implements ProxyClient {

    static class  NettyHttpClientHandler  extends ChannelInboundHandlerAdapter {

        private ChannelHandlerContext consumerCtx;
        private FullHttpRequest originHttpRequest;
        private String targetUrl;

        public NettyHttpClientHandler(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest, String url) {
            this.consumerCtx = ctx;
            this.originHttpRequest = fullHttpRequest;
            this.targetUrl = url;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            try {
                FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, targetUrl);
                for (Map.Entry<String, String> entry : originHttpRequest.headers()) {
                    request.headers().add(entry.getKey(), entry.getValue());
                }

                ctx.writeAndFlush(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            try {
                FullHttpResponse rsp = (FullHttpResponse) msg;
                ByteBuf buf = rsp.content();
                byte[] responseBuf = new byte[buf.readableBytes()];
                buf.readBytes(responseBuf);

                FullHttpResponse response;

                try {
                    if (rsp != null) {
                        response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(responseBuf));
                    } else {
                        response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
                    }

                    response.headers().set("Content-Type", "application/json");
                    response.headers().setInt("Content-Length", response.content().readableBytes());

                    System.out.println(response);

                    consumerCtx.writeAndFlush(response);

                    if (!HttpUtil.isKeepAlive(originHttpRequest)) {
                        consumerCtx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                    } else {
                        response.headers().set(CONNECTION, KEEP_ALIVE);
                        consumerCtx.writeAndFlush(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            System.out.println("come in exceptionCaught exception=============");
            cause.printStackTrace();
            ctx.close();
        }
    }

    @Override
    public void doGetRequest(final String ip, final int port, final String url, final FullHttpRequest origReq, final ChannelHandlerContext ctx) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        System.out.println(url);

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpClientCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(1024 * 10 * 1024));
                            ch.pipeline().addLast(new HttpContentDecompressor());
                            ch.pipeline().addLast(new NettyHttpClientHandler(ctx, origReq, url));
                        }
                    });

            // Start the client.
            b.connect(ip, port).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

### 作业三
作业题：（必做）实现过滤器。

实现了一个接口 ProxyRequestFilter 以及对应的一个实现类。在实现类中添加新的 header：

```java
public class AddHeaderFilter implements ProxyRequestFilter {

    @Override
    public void filter (FullHttpRequest req, ChannelHandlerContext ctx) {
        req.headers().add("NIO", "Zhang Wei");
    }
}
```

然后在 `HttpInboundInitializer` 中新增 handler 时把过滤器集成进去。

```java
@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
		p.addLast(new HttpServerCodec());
		p.addLast(new HttpObjectAggregator(1024 * 1024));
		HttpInboundHandler inboundHandler = new HttpInboundHandler(this.proxyServer);
		inboundHandler.filters.add(new AddHeaderFilter());
		p.addLast(inboundHandler);
	}
```

最后在拿到请求的时候，去遍历执行所有的过滤器，实现对请求的再加工。

```java
@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;

            /** 调用所有 Filter 处理过滤逻辑 */
            for (ProxyRequestFilter filter : this.filters) {
                filter.filter(fullRequest, ctx);
            }

            System.out.println(filters);

            final String url = this.proxyServer + fullRequest.uri();
            URI uri = new URI(url);
            System.out.println("url: " + url);
            nettyClient.doGetRequest(uri.getHost(), uri.getPort(), url, fullRequest, ctx);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
```

---
