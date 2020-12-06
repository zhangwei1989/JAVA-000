# 第七周作业

## 周四

### 作业一

作业题：（选做）用今天课上学习的知识，分析自己系统的 SQL 和表结构

---

### 作业二

作业题：（必做）按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率


---

### 作业三

作业题：（选做）按自己设计的表结构，插入 1000 万订单模拟数据，测试不同方式的插入效

---

### 作业四

作业题：（选做）使用不同的索引或组合，测试不同方式查询效率

---

### 作业五

作业题：（选做）调整测试数据，使得数据尽量均匀，模拟 1 年时间内的交易，计算一年的销售报表：销售总额，订单数，客单价，每月销售量，前十的商品等等（可以自己设计更多指标）

---

### 作业六

作业题：（选做）尝试自己做一个 ID 生成器（可以模拟 Seq 或 Snowflake）

---

### 作业七

作业题：（选做）尝试实现或改造一个非精确分页的程序

---

## 周六

### 作业一

作业题：（选做）配置一遍异步复制，半同步复制、组复制

#### 配置异步复制（基于 Mac Docker）

1.编写docker-compose文件
  
```yaml
 version: '2' 
 networks:
   byfn:                                       #配置byfn网络
 services:
   master:                                     #配置master服务
     image: 'mysql:5.7'                        #使用刚才pull下来的镜像
     restart: always
     container_name: mysql-master              #容器起名 mysql-master
     environment:
       MYSQL_USER: test
       MYSQL_PASSWORD: admin123
       MYSQL_ROOT_PASSWORD: admin123           #配置root的密码
     ports:
       - '3316:3306'                           #配置端口映射
     volumes:
       - "./master/my.cnf:/etc/mysql/my.cnf"   #配置my.cnf文件挂载，
     networks:
       - byfn                                  #配置当前servie挂载的网络
   slave:																			#配置slave服务
     image: 'mysql:5.7'
     restart: always
     container_name: mysql-slave
     environment:
       MYSQL_USER: test
       MYSQL_PASSWORD: admin123
       MYSQL_ROOT_PASSWORD: admin123
     ports:
       - '3326:3306'
     volumes:
       - "./slave/my.cnf:/etc/mysql/my.cnf"
     networks:
       - byfn
```

2. 在docker-compose.yaml当前目录下，建立两个文件夹，master和slave，里面分别写入文件my.cnf

```yaml
mater/my.cnf

[mysqld]
basedir = ./
datadir = ./data
server-id=1

sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
log_bin=/var/lib/mysql/mysql-bin
binlog-format=Row


slave/my.cnf

[mysqld]
basedir = ./
datadir = ./data
server-id=2

sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
log_bin=/var/lib/mysql/mysql-bin
binlog-format=Row
```

分别保存后退出

3. 然后在当前docker-compose.yaml 文件目录下执行

```shell
docker-compose -f docker-compse.yaml up -d
```

4. 启动之后进入master容器

```shell
docker exec -it mysql-master /bin/bash
mysql -uroot -padmin123
进入 mysql 终端之后
mysql> create user 'repl'@'%' identified by 'admin123';
mysql> GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'repl'@'%'; 
mysql> flush privileges;
mysql> show master status;

mysql> show master status;
   +------------------+----------+--------------+------------------+-------------------+
   | File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
   +------------------+----------+--------------+------------------+-------------------+
   | mysql-bin.000003 |      767 |              |                  |                   |
   +------------------+----------+--------------+------------------+-------------------+
   1 row in set (0.00 sec)
```

需要记住File名字，和Position偏移位置

5. 另起一个终端进入slave容器

```shell
docker exec -it mysql-slave /bin/bash
mysql -uroot -padmin123
进入 mysql 终端之后
mysql> CHANGE MASTER TO MASTER_HOST='mysql-master', MASTER_PORT=3306,  MASTER_USER='repl', MASTER_PASSWORD='admin123', MASTER_LOG_FILE='mysql-bin.000003', MASTER_LOG_POS=767;
mysql> start slave;
```

这里两个参数 MASTER_LOG_FILE 和 MASTER_LOG_POS 就是前面master上最后查询出来的；

```shell
mysql> show slave status\G
*************************** 1. row ***************************
              Slave_IO_State: Waiting for master to send event
                 Master_Host: mysql-master
                 Master_User: repl
                 Master_Port: 3306
               Connect_Retry: 60
             Master_Log_File: mysql-bin.000003
         Read_Master_Log_Pos: 1116
              Relay_Log_File: eefecaed2964-relay-bin.000002
               Relay_Log_Pos: 320
       Relay_Master_Log_File: mysql-bin.000003
            Slave_IO_Running: Yes
           Slave_SQL_Running: Yes
             Replicate_Do_DB:
         Replicate_Ignore_DB:
          Replicate_Do_Table:
```

查询slave的状态，看到 Slave_IO_Running 和 Slave_SQL_Running 都是yes即为同步成功。
之后在 mysql 控制台创建数据库，表，然后在 slave 查看数据已经同步代表已经配置成功。


---

### 作业二

作业题：（必做）读写分离 - 动态切换数据源版本 1.0

见作业代码项目 [multi-data-source](https://github.com/zhangwei1989/JAVA-000/tree/main/Week_07/homework_code/multi-data-source)

实现了以下功能：

- 动态切换数据源 1.0 版本：根据具体的 Service 方法是否会操作数据，注入不同的数据源
- 动态切换数据源 1.2 版本：配置一主两从的数据源

---

### 作业三

作业题：（必做）读写分离 - 数据库框架版本 2.0

见作业代码项目 [shardingsphere-jdbc](https://github.com/zhangwei1989/JAVA-000/tree/main/Week_07/homework_code/sharding-sphere-jdbc)

实现了以下功能：

- 动态切换数据源 2.0 版本：配置 shardingsphere 5.0alpha 版本配置读写分离，实现一主一从数据库的配置

---

### 作业四

作业题：（选做）读写分离 - 数据库中间件版本 3.0




---

### 作业五

作业题：（选做）配置 MHA，模拟 master 宕机



---

### 作业六

作业题：（选做）配置 MGR，模拟 master 宕机

---

### 作业七

作业题：（选做）配置 Orchestrator，模拟 master 宕机，演练 UI 调整拓扑结构


---