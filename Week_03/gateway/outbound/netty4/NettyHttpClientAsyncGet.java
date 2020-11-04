package gateway.outbound.netty4;

import gateway.outbound.common.ProxyClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

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
