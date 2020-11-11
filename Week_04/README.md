## 第四周作业

### 作业一

作业题：（选做）把示例代码，运行一遍，思考课上相关的问题。也可以做一些比较。

---

### 作业二

作业题：（必做）思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？ 写出你的方法，越多越好，提交到 Github。

在这里将 httpclient 和 netty 作为客户端进行了整合，因为做的事情是一样的：

```java
public class HomeWork {

    private final static AtomicInteger result = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        method7();
        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    /**
     * 第一种：使用 线程join确保任务线程执行完成后执行主线程
     */
    private static void method1() throws InterruptedException {
        Runnable task = () -> result.set(sum());

        Thread thread = new Thread(task,"获取斐波那契值");

        thread.start();
        // 使用join确保线程执行完成
        thread.join();
    }

    /**
     * 第二种：使用 countDownLatch 阻塞等到线程 countDown后执行
     */
    private static void method2() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Runnable task = () -> {
            result.set(sum());
            countDownLatch.countDown();
        };

        Thread thread = new Thread(task,"获取斐波那契值");
        thread.start();

        // 使用 countDownLatch 阻塞等到线程 countDown后执行
        countDownLatch.await();
    }

    /**
     * 第三种：使用 cyclicBarrier 阻塞等到计算线程执行完后，回调执行，同时线程池需要 shutdown
     */
    private static void method3() throws InterruptedException {
        long start = System.currentTimeMillis();

        CyclicBarrier cyclicBarrier = new CyclicBarrier(1,new Runnable() {
            @Override
            public void run() {
                System.out.println("异步计算结果为：" + result);
                System.out.println("使用时间："+ (System.currentTimeMillis() - start) + " ms");
            }
        });
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                4,
                8,
                5,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                new DefaultThreadFactory("获取斐波那契值线程池"));

        Runnable task = () -> {
            result.set(sum());
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        threadPoolExecutor.submit(task);
        threadPoolExecutor.shutdown();
    }


    /**
     * 第四种：使用 callable 返回计算的值，将值赋予result，通过 Future 的 get 方法阻塞主线程
     */
    private static void method4() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Callable<Integer> task = () -> sum();

        Future<Integer> future = executorService.submit(task);
        result.set(future.get());

        executorService.shutdown();
    }

    /**
     * 第五种：使用 CompletableFuture 返回计算的值，通过回调将值赋予 result
     */
    private static void method5() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CompletableFuture.supplyAsync(() -> sum(), executorService)
                .thenAcceptAsync(res -> result.set((Integer) res)).join();
        executorService.shutdown();
    }

    /**
     * 第六种：无锁，主线程中 自旋不断监测 result是否已被赋值
     */
    private static void method6() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Runnable task = () -> result.set(sum());

        executorService.submit(task);
        while (result.get() == 0);
        executorService.shutdown();
    }

    /**
     * 第七种：使用 condition 如主线程拿到的值未被赋值则阻塞，等待异步计算线程释放信号
     */
    private static void method7() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Lock lock = new ReentrantLock();
        Condition calComplete = lock.newCondition();
        Runnable task = () -> {
            try {
                lock.lock();
                result.set(sum());
                calComplete.signal();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        };

        executorService.submit(task);

        lock.lock();
        try {
            while (result.get() == 0) {
                calComplete.await();
            }
        } finally {
            lock.unlock();
        }

        executorService.shutdown();
    }


    private static int sum() {
        return fibo(60);
    }

    /**
     * @param a [1 - ...]
     * @return 第 a 个斐波那契值
     */
    private static int fibo(int a) {
        if ( a <= 2) return 1;
        int first = 1;
        int secoed = 1;
        int res = 0;
        for (int i = 3; i <= a; i++) {
            res = first + secoed;
            first = secoed;
            secoed = res;
        }
        return res;
    }

}
```

---

### 作业三

作业题：（选做）列举常用的并发操作 API 和工具类，简单分析其使用场景和优缺点。

-  CountDownLatch 实现线程等待

    - 主线程需要等待多个线程执行完成之后做一些操作的时候使用
    - 优点: 使用场景比较丰富
    - 缺点: 不可以重用,再次使用需要初始化计数器的值
  
-  cyclicBarrier 多个线程之间相互等待
   
    - 多个线程互相等待，通过回调执行后续流程
    - cyclibarrier可以指定一个回调函数,调用回调函数的是这些线程之间的最后一个运行完成的线程
    
- ReadWriteLock
  
    - 读写锁: 可以分为读锁和写锁
    - 读锁不能升级为写锁.但是写锁可以降级为读锁
    - 优点是读写分离.提高了性能
      
- Lock和Condition
  
    - Lock是一个显式的锁.相比于synchronized更加灵活
    - condition的方法和obj锁对象的wait和notify有着类似的效果
    - 一把锁可以有多个condition
    
- CopyOnWriteArrayList

    - 是一个读和写复制的线程安全的ArrayList
    - 写的时候直接复制一份副本去写,不会影响到旧引用
    - 然后替换旧引用

---

### 作业四
作业题：（选做）请思考：什么是并发？什么是高并发？实现高并发高可用系统需要考虑哪些因素，对于这些你是怎么理解的？

多个请求同时对临界区访问，即为并发，高并发即是大量的请求同时对临界区访问
需要考虑临界区的数据读写是否发生数据覆盖，以及拿不到最新的数据问题，数据访问读写顺序，也就是数据安全问题
一般使用加锁来解决问题，或者乐观锁（注意当非常高的并发时，乐观锁并不一定最好），使用锁须注意防止死锁的发生

---

### 作业五
作业题：（选做）请思考：还有哪些跟并发类似 / 有关的场景和问题，有哪些可以借鉴的解决办法。

---

### 作业六
作业题：（必做）把多线程和并发相关知识带你梳理一遍，画一个脑图，截图上传到 Github 上。

此图为借鉴其他同学的，之后补上自己的

![HTTPClient结果](http://zhangwei1989.oss-cn-beijing.aliyuncs.com/2020-11-11-%E5%A4%9A%E7%BA%BF%E7%A8%8B%E4%BD%9C%E4%B8%9A%E5%9B%BE.png)

---