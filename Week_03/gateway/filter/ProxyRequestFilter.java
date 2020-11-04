package gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @ClassName ProxyRouter
 * @Description 过滤器的接口
 * @Author zhangwei
 * @Date 2020-11-04 14:21
 * @Version 1.0
 */
public interface ProxyRequestFilter {

    public void filter(FullHttpRequest req, ChannelHandlerContext ctx);
}
