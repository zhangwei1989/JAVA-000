package gateway.client.netty4;

import gateway.client.common.ProxyClient;
import gateway.server.HttpOutboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.util.Map;

public class NettyHttpClientAsyncGet implements ProxyClient {

    static class  NettyHttpClientHandler  extends ChannelInboundHandlerAdapter {

        private ChannelHandlerContext consumerCtx;
        private FullHttpRequest originHttpRequest;
        private String targetUrl;
        private HttpOutboundHandler consumer;

        public NettyHttpClientHandler(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest, String url, HttpOutboundHandler consumer) {
            this.consumerCtx = ctx;
            this.originHttpRequest = fullHttpRequest;
            this.targetUrl = url;
            this.consumer = consumer;
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

                this.consumer.proxyResponse(responseBuf, this.originHttpRequest, this.consumerCtx);
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
    public void doGetRequest(final String ip, final int port, final String url, final FullHttpRequest origReq, final ChannelHandlerContext ctx, HttpOutboundHandler consumer) {
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
                            ch.pipeline().addLast(new NettyHttpClientHandler(ctx, origReq, url, consumer));
                        }
                    });

            // Start the client.
            b.connect(ip, port).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
