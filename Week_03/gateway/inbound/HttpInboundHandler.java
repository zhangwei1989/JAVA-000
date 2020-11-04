package gateway.inbound;

import gateway.filter.ProxyRequestFilter;
import gateway.outbound.netty4.NettyHttpClientAsyncGet;
import gateway.router.RandomRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private NettyHttpClientAsyncGet nettyClient;
    List<ProxyRequestFilter> filters = new LinkedList<>();

    public HttpInboundHandler() {
        nettyClient = new NettyHttpClientAsyncGet();
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

            System.out.println("============url=============" + url);
            URI uri = new URI(url);
            System.out.println("url: " + url);
            nettyClient.doGetRequest(uri.getHost(), uri.getPort(), url, fullRequest, ctx);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
