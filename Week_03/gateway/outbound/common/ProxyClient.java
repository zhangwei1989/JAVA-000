package gateway.outbound.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @ClassName ProxyClient
 * @Description TODO
 * @Author zhangwei
 * @Date 2020-11-04 13:02
 * @Version 1.0
 */
enum ProxyClientType
{
    HTTPCLIENTHTTP,     // 客户端使用OkHttp实现
    NETTY       // 客户端是用Netty实现
}

/** 代理HTTP客户端接口 */
public interface ProxyClient {

    public static enum ProxyClientType
    {
        HTTPCLIENTHTTP,     // 客户端使用OkHttp实现
        NETTY       // 客户端是用Netty实现
    }

    public void doGetRequest(String ip, int port, String url, FullHttpRequest origReq, ChannelHandlerContext ctx);
}
