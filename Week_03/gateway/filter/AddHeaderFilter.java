package gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @ClassName AddHeaderFilter
 * @Description TODO
 * @Author zhangwei
 * @Date 2020-11-04 14:25
 * @Version 1.0
 */
public class AddHeaderFilter implements ProxyRequestFilter {

    @Override
    public void filter (FullHttpRequest req, ChannelHandlerContext ctx) {
        req.headers().add("NIO", "Zhang Wei");
    }
}
