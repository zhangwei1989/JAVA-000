package gateway.server;

import gateway.client.common.ProxyClient;
import gateway.client.httpclient.NamedThreadFactory;
import gateway.client.netty4.NettyHttpClientAsyncGet;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.*;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @ClassName HttpOutboundHandler
 * @Description TODO
 * @Author zhangwei
 * @Date 2020-11-05 00:10
 * @Version 1.0
 */
public class HttpOutboundHandler {

    private ProxyClient httpClient;
    private ExecutorService threadPool;
    private final HttpOutboundHandler consumer;

    public HttpOutboundHandler(ProxyClient.ProxyClientType clientType) {
        if (clientType == ProxyClient.ProxyClientType.HTTPCLIENTHTTP) {
            httpClient = new NettyHttpClientAsyncGet();
        } else if (clientType == ProxyClient.ProxyClientType.NETTY) {
            httpClient = new NettyHttpClientAsyncGet();
        }

        this.consumer = this;

        int cores = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 1000;
        int queueSize = 2048;

        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();//.DiscardPolicy();
        threadPool = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);
    }

    public void handle(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx, String url) {
        try {
            URI uri = new URI(url);
            threadPool.execute(
                    () -> httpClient.doGetRequest(uri.getHost(), uri.getPort(), url, fullHttpRequest, ctx, consumer));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void proxyResponse (final byte[] rsp, final FullHttpRequest origReq, final ChannelHandlerContext ctx) {
        FullHttpResponse response;

        if (rsp != null) {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(rsp));
        } else {
            response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
        }

        response.headers().set("Content-Type", "application/json");
        response.headers().setInt("Content-Length", response.content().readableBytes());

        System.out.println(response);

        ctx.writeAndFlush(response);

        if (!HttpUtil.isKeepAlive(origReq)) {
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.writeAndFlush(response);
        }
    }
}
