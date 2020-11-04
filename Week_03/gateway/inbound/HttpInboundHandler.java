package gateway.inbound;

import gateway.filter.AddHeaderFilter;
import gateway.outbound.netty4.NettyHttpClientAsyncGet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private final String proxyServer;
    private NettyHttpClientAsyncGet nettyClient;

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
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
}
