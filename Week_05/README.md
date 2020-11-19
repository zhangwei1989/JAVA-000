# 第五周作业

## 周四

### 作业一

作业题：（选做）使 Java 里的动态代理，实现一个简单的 AOP。

---

### 作业二

作业题：（必做）写代码实现 Spring Bean 的装配，方式越多越好（XML、Annotation 都可以）, 提交到 Github。

#### 第一种方式：XML

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                        http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
                        http://www.springframework.org/schema/context
                        http://www.springframework.org/schema/context/spring-context-3.2.xsd">
    
    <bean id="student123"
          class="framework.spring.spring01.Student">
        <property name="id" value="123" />
        <property name="name" value="KK123" />
    </bean>
    
    <bean id="student456"
          class="framework.spring.spring01.Student">
        <property name="id" value="456" />
        <property name="name" value="KK456" />
    </bean>
    
    <bean id="klass" class="framework.spring.spring01.Klass">
        <property name="students">
            <list>
                <ref bean="student456" />
                <ref bean="student123" />
            </list>
        </property>
    </bean>

    <context:component-scan base-package="framework.spring.spring01" />

    <bean id="school" class="framework.spring.spring01.School"></bean>
</beans>
```

#### 第二种方式：@Component

```java
@Data
@Component
public class School implements ISchool {
    
    @Autowired
    Klass klass;
    
    @Resource(name = "student123")
    Student student123;
    
    @Override
    public void ding(){
        System.out.println("Class1 have " + this.klass.getStudents().size() + " students and one is " + this.student123);
    }
}
```

#### 第三种方式：@Configuration

```java
@Configuration
public class KlassConfig {

    @Bean
    public Klass klass() {
        Klass klass = new Klass();
        klass.students = new ArrayList<>();
        klass.students.add(klass.getStudent123());
        klass.students.add(klass.getStudent456());

        return klass;
    }
}
```

#### 第四种方式：SpringXbean

```java

```


---

### 作业三

作业题：（选做）实现一个 Spring XML 自定义配置，配置一组 Bean，例如：Student/Klass/School。


---

### 作业四
作业题：（选做，会添加到高手附加题）
    4.1 （挑战）讲网关的 frontend/backend/filter/router 线程池都改造成 Spring 配置方式；
    4.2 （挑战）基于 AOP 改造 Netty 网关，filter 和 router 使用 AOP 方式实现；
    4.3 （中级挑战）基于前述改造，将网关请求前后端分离，中级使用 JMS 传递消息；
    4.4 （中级挑战）尝试使用 ByteBuddy 实现一个简单的基于类的 AOP；
    4.5 （超级挑战）尝试使用 ByteBuddy 与 Instrument 实现一个简单 JavaAgent 实现无侵入下的 AOP。

---

## 周六

### 作业一

作业题：（选做）总结一下，单例的各种写法，比较它们的优劣。

#### 第一种方式：懒汉式，线程不安全

- 是否 Lazy 初始化
    - 是
- 是否线程安全
    - 否
- 特点
    - 因为没有加锁 synchronized，所以严格意义上它并不算单例模式。这种方式 lazy loading 很明显，不要求线程安全，在多线程不能正常工作

```java
public class Singleton {  
    private static Singleton instance;  
    private Singleton (){}  
  
    public static Singleton getInstance() {  
        if (instance == null) {  
            instance = new Singleton();  
        }  
    return instance;  
    }  
}

```

#### 第二种方式：懒汉式，线程安全

- 是否 Lazy 初始化
    - 是
- 是否线程安全
    - 是
- 优点
    - 第一次调用才初始化，避免内存浪费
- 缺点
    - 加锁 synchronized 能保证单例，但加锁会影响效率


```java
public class Singleton {  
    private static Singleton instance;  
    private Singleton (){}  
    
    public static synchronized Singleton getInstance() {  
        if (instance == null) {  
            instance = new Singleton();  
        }  
    return instance;  
    }  
}

```

#### 第三种方式：饿汉式

- 是否 Lazy 初始化
    - 否
- 是否线程安全
    - 是
- 优点
    - 没有加锁，执行效率会提高
- 缺点
    - 类加载时就初始化，浪费内存
- 特点
    - 基于 classloader 机制避免了多线程的同步问题，instance 在类装载时就实例化


```java

public class Singleton {  
    private static Singleton instance = new Singleton();  
    private Singleton (){}  
    
    public static Singleton getInstance() {  
        return instance;  
    }  
}

```

#### 第四种方式：双检锁/双重校验锁（DCL，即 double-checked locking）

- 是否 Lazy 初始化
    - 是
- 是否线程安全
    - 是
- 特点
    - 双锁能保证实例为单例

```java

public class Singleton {  
    private volatile static Singleton singleton;  
    private Singleton (){}  
    
    public static Singleton getSingleton() {  
        if (singleton == null) {  
            synchronized (Singleton.class) {  
                if (singleton == null) {  
                    singleton = new Singleton();  
                }  
            }  
        }  
        
        return singleton;  
    }  
}

```

#### 第五种方式：登记式/静态内部类

- 是否 Lazy 初始化
    - 是
- 是否线程安全
    - 是
- 特点
    - 只有通过显式调用 getInstance 方法时，才会显式装载 SingletonHolder 类，从而实例化 instance

```java

public class Singleton {  
    private static class SingletonHolder {  
        private static final Singleton INSTANCE = new Singleton();  
    }  
    
    private Singleton (){}  
    
    public static final Singleton getInstance() {  
        return SingletonHolder.INSTANCE;  
    }  
}

```

#### 第六种方式：枚举

- 是否 Lazy 初始化
    - 否
- 是否线程安全
    - 是
- 优点
    - 避免多线程同步问题
    - 自动支持序列化机制，防止反序列化重新创建新的对象，绝对防止多次实例化

```java

public enum Singleton {  
    INSTANCE;
}

```

---

### 作业二

作业题：（选做）maven/spring 的 profile 机制，都有什么用法？


---

### 作业三

作业题：（选做）总结 Hibernate 与 MyBatis 的各方面异同点。




---

### 作业四

作业题：（必做）给前面课程提供的 Student/Klass/School 实现自动配置和 Starter。




---

### 作业五

作业题：（选做）学习 MyBatis-generator 的用法和原理，学会自定义 TypeHandler 处理复杂类型。


---

### 作业六

作业题：（必做）研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法：
    1）使用 JDBC 原生接口，实现数据库的增删改查操作。
    2）使用事务，PrepareStatement 方式，批处理方式，改进上述操作。
    3）配置 Hikari 连接池，改进上述操作。提交代码到 Github。

---

### 作业七

作业题：附加题（可以后面上完数据库的课再考虑做）：
    (挑战) 基于 AOP 和自定义注解，实现 @MyCache(60) 对于指定方法返回值缓存 60 秒。
    (挑战) 自定义实现一个数据库连接池，并整合 Hibernate/Mybatis/Spring/SpringBoot。
    (挑战) 基于 MyBatis 实现一个简单的分库分表 + 读写分离 + 分布式 ID 生成方案。


---