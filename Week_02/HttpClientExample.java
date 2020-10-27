package io.github.kimmking.netty.server;

import java.io.IOException;

/**
 * @ClassName HttpClientExample
 * @Description TODO
 * @Author zhangwei
 * @Date 2020-10-27 09:13
 * @Version 1.0
 */
public class HttpClientExample {

    public static void main(String[] args) throws IOException {
        /** 发起 GET 请求 */
        HttpClientHandler.get("http://localhost:8808/test");
    }
}
