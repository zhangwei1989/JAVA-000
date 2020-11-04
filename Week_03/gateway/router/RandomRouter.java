package gateway.router;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName RandomRouter
 * @Description TODO
 * @Author zhangwei
 * @Date 2020-11-04 20:25
 * @Version 1.0
 */
public class RandomRouter implements ProxyRouter {

    @Override
    public String getProxyServer() {
        /** 获取 0-3 之间的随机数 */
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return PROXY_SERVER_LIST.get(random.nextInt(3));
    }
}
