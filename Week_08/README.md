# 第八周作业

## 周四

### 作业一

作业题：（选做）分析前面作业设计的表，是否可以做垂直拆分。

---

### 作业二

作业题：（必做）设计对前面的订单表数据进行水平分库分表，拆分 2 个库，每个库 16 张表。并在新结构在演示常见的增删改查操作。代码、sql 和配置文件，上传到 Github。

使用 sharding-proxy 实现水平分库分表的配置，拆分成2个库，各16张表。

#### config-sharding.yaml 配置

```sql
schemaName: sharding_db

dataSourceCommon:
  username: root
  password: admin123
  connectionTimeoutMilliseconds: 30000
  idleTimeoutMilliseconds: 60000
  maxLifetimeMilliseconds: 1800000
  maxPoolSize: 50
  minPoolSize: 1
  maintenanceIntervalMilliseconds: 30000

dataSources:
  db_0:
    url: jdbc:mysql://192.168.99.100:3316/db_0?serverTimezone=UTC&useSSL=false
  db_1:
    url: jdbc:mysql://192.168.99.100:3326/db_1?serverTimezone=UTC&useSSL=false

rules:
  - !SHARDING
    tables:
      t_order:
        actualDataNodes: db_${0..1}.t_order_${0..15}
        tableStrategy:
          standard:
            shardingColumn: order_id
            shardingAlgorithmName: t_order_inline
        keyGenerateStrategy:
          column: order_id
          keyGeneratorName: snowflake
    defaultDatabaseStrategy:
      standard:
        shardingColumn: order_id
        shardingAlgorithmName: database_inline
    defaultTableStrategy:
      none:

    shardingAlgorithms:
      database_inline:
        type: INLINE
        props:
          algorithm-expression: db_${order_id % 2}
          allow-range-query-with-inline-sharding: true
      t_order_inline:
        type: INLINE
        props:
          algorithm-expression: t_order_${order_id % 16}
          allow-range-query-with-inline-sharding: true

    keyGenerators:
      snowflake:
        type: SNOWFLAKE
        props:
          worker-id: 123

```

#### 增删改查

1. `insert into t_order (order_id, user_id, pay_status, status, total_price) VALUES (34, 10, 0, 1, 1000);`

按照 `order_id % 2` 计算，新增的记录会增加到 db_0 号库，按照 `order_id % 16` 计算，新增的记录会增加到 t_order_2 号表。

从结果可以看出符合预期

![新增结果](http://zhangwei1989.oss-cn-beijing.aliyuncs.com/2020-12-11-%E5%B1%8F%E5%B9%95%E5%BF%AB%E7%85%A7%202020-12-11%20%E4%B8%8B%E5%8D%887.27.39.png)

2. `select * from t_order where user_id = 10;`

使用非分片键进行查询，会导致广播扫描查询，势必效率会很低

![user_id 查询结果](http://zhangwei1989.oss-cn-beijing.aliyuncs.com/2020-12-11-%E5%B1%8F%E5%B9%95%E5%BF%AB%E7%85%A7%202020-12-11%20%E4%B8%8B%E5%8D%887.33.28.png)

---

### 作业三

作业题：（选做）模拟 1000 万的订单单表数据，迁移到上面作业 2 的分库分表中。

---

### 作业四

作业题：（选做）重新搭建一套 4 个库各 64 个表的分库分表，将作业 2 中的数据迁移到新分库。

---

## 周六

### 作业一

作业题：（选做）列举常见的分布式事务，简单分析其使用场景和优缺点。

---

### 作业二

作业题：（必做）基于 hmily TCC 或 ShardingSphere 的 Atomikos XA 实现一个简单的分布式事务应用 demo（二选一），提交到 Github。

见作业代码项目 [XATransactionExample](https://github.com/zhangwei1989/JAVA-000/tree/main/Week_08/homework_code/XATransactionExample)

---

### 作业三

作业题：（选做）基于 ShardingSphere narayana XA 实现一个简单的分布式事务 demo。

---

### 作业四

作业题：（选做）基于 seata 框架实现 TCC 或 AT 模式的分布式事务 demo。

---

### 作业五

作业题：（选做☆）设计实现一个简单的 XA 分布式事务框架 demo，只需要能管理和调用 2 个 MySQL 的本地事务即可，不需要考虑全局事务的持久化和恢复、高可用等。

---

### 作业六

作业题：（选做☆）设计实现一个 TCC 分布式事务框架的简单 Demo，需要实现事务管理器，不需要实现全局事务的持久化和恢复、高可用等。

---

### 作业七

作业题：（选做☆）设计实现一个 AT 分布式事务框架的简单 Demo，仅需要支持根据主键 id 进行的单个删改操作的 SQL 或插入操作的事务。


---