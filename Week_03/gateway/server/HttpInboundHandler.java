package gateway.server;

import gateway.client.common.ProxyClient;
import gateway.filter.ProxyRequestFilter;
import gateway.router.RandomRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;

import java.util.LinkedList;
import java.util.List;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    List<ProxyRequestFilter> filters = new LinkedList<>();
    HttpOutboundHandler outboundHandler;

    public HttpInboundHandler() {
        outboundHandler = new HttpOutboundHandler(ProxyClient.ProxyClientType.NETTY);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;

            /** 调用所有 Filter 处理过滤逻辑 */
            for (ProxyRequestFilter filter : this.filters) {
                filter.filter(fullRequest, ctx);
            }

            System.out.println(filters);

            /** 调用 Router 获取后端服务地址 */
            String proxyServer = new RandomRouter().getProxyServer();

            final String url = proxyServer + fullRequest.uri();

            outboundHandler.handle(fullRequest, ctx, url);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
