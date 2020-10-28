## 第二周作业

### 作业一
作业题：使用 GCLogAnalysis.java 自己演练一遍串行 / 并行 /CMS/G1 的案例。

测试 GCLogAnalysis.java 生成对象结果统计：

|GC方式|128M|256M|512M|1G|2G|4G|
|:----    |:---|:----- |-----   |:-----   |:-----   |:-----   |
|Serial GC | OOM |4603 |8538|10477|10540|8410|
|Parallel GC| OOM |OOM |8580|10623|11725|11038|
|CMS GC| OOM |4377 |10551|12074|11521|11238|
|G1 GC| OOM |OOM |10759|11675|10235|8991|

---

### 作业二
作业题：使用压测工具（wrk 或 sb），演练 gateway-server-0.0.1-SNAPSHOT.jar 示例。

测试gateway-server-0.0.1-SNAPSHOT.jar 压测 RQS 结果如下表：

压测指令为
```
wrk -t100  -c1000 -d30s http://localhost:8088/api/hello
```

|GC方式|128M|256M|512M|1G|2G|4G|
|:----    |:---|:----- |-----   |:-----   |:-----   |:-----   |
|Serial GC | 19785 |7314 |4731|7629|4397|4925|
|Parallel GC| 4576 |7242 |5751|5891|5369|5050|
|CMS GC| 4764 |4925 |5115|4312|4044|5682|
|G1 GC| 6750 |5012 |4189|5245|4220|4467|

---

### 作业三（先跳过）
作业题：如果自己本地有可以运行的项目，可以按照 2 的方式进行演练。

---

### 作业四
作业题：根据上述自己对于 1 和 2 的演示，写一段对于不同 GC 的总结，提交到 Github。

在堆内存分配最小的情况下，Serial GC 反而是最有效率的。在压测四种 Web 请求时，四种 GC 方式 RQS 差别不大。

---

### 作业五
作业题：运行课上的例子，以及 Netty 的例子，分析相关现象。

压测指令为
```
wrk -t100  -c1000 -d30s http://localhost:8888/test
```

|NIO方式|吞吐量RQS|
|:----    |:---|
|简约也很简单的单线程BIO | 24.5 |
|多线程版本| 299 |
|线程池版本| 132 |
|Netty| 20536 |

前两种方式的结果正常，线程池版本比多线程版本 RQS 低，以及 Netty 高那么多，还需要继续研究原因。

---

### 作业六
作业题：（必做）写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801 ，代码提交到 Github。
 
此次作业使用 HttpClient，需要添加两个 Maven 依赖。

```
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5.10</version>
        </dependency>

        <dependency>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
            <version>1.2</version>
        </dependency>
```

代码如下：

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
![HTTPClient结果](http://zhangwei1989.oss-cn-beijing.aliyuncs.com/2020-10-27-httpclient%E7%BB%93%E6%9E%9C%E5%B1%95%E7%A4%BA.png)