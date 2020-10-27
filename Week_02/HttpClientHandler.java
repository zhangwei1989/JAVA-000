package io.github.kimmking.netty.server;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @ClassName HttpClientHandler
 * @Description TODO
 * @Author zhangwei
 * @Date 2020-10-27 09:14
 * @Version 1.0
 */
public class HttpClientHandler {

    public static void get(String uri) throws IOException {
        HttpGet request = new HttpGet(uri);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            /** Get HttpResponse Status */
            System.out.println(response.getProtocolVersion());              // HTTP/1.1
            System.out.println(response.getStatusLine().getStatusCode());   // 200
            System.out.println(response.getStatusLine().getReasonPhrase()); // OK
            System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                /** return it as a String */
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }
        }
    }
}
