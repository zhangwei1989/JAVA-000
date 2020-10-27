## 第二周作业
4. （必做）写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801 ，代码提交到 Github。

```java
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

public class HttpClientExample {

    public static void main(String[] args) throws IOException {
        /** 发起 GET 请求 */
        HttpClientHandler.get("http://localhost:8808/test");
    }
}
```

请求打印信息如下：
![Image text](http://zhangwei1989.oss-cn-beijing.aliyuncs.com/2020-10-27-httpclient%E7%BB%93%E6%9E%9C%E5%B1%95%E7%A4%BA.png)