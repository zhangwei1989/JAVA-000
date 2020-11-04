package gateway.router;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ProxyRouter
 * @Description TODO
 * @Author zhangwei
 * @Date 2020-11-04 20:25
 * @Version 1.0
 */
public interface ProxyRouter {

    public final static List<String> PROXY_SERVER_LIST = Arrays.asList("http://localhost:8088", "http://localhost:8089", "http://localhost:8090");

    public String getProxyServer();
}
