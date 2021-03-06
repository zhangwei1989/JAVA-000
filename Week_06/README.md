# 第六周作业

## 周四

### 作业一

作业题：（选做）尝试使用 Lambda/Stream/Guava 优化之前作业的代码。

---

### 作业二

作业题：尝试使用 Lambda/Stream/Guava 优化工作中编码的代码。


---

### 作业三

作业题：（选做）根据课上提供的材料，系统性学习一遍设计模式，并在工作学习中思考如何用设计模式解决问题。

---

### 作业四

作业题：（选做）根据课上提供的材料，深入了解 Google 和 Alibaba 编码规范，并根据这些规范，检查自己写代码是否符合规范，有什么可以改进的。

---

## 周六

### 作业一

作业题：（选做）基于课程中的设计原则和最佳实践，分析是否可以将自己负责的业务系统进行数据库设计或是数据库服务器方面的优化

---

### 作业二

作业题：（必做）基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交 DDL 的 SQL 文件到 Github（后面 2 周的作业依然要是用到这个表结构）。

#### 用户表

```sql
CREATE TABLE `mall`.`user` (
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '用户主键',
  `password` varchar(64) NOT NULL COMMENT '用户登录密码',
  `mobile_phone` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '用户创建时间',
  `update_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '用户更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_index` (`user_name`) USING BTREE COMMENT '用户名唯一性索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8

```
#### 商品

```sql
CREATE TABLE `mall`.`product` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '商品主键',
  `name` varchar(12) NOT NULL COMMENT '商品名称',
  `description` varchar(255) DEFAULT NULL COMMENT '商品描述',
  `price` decimal(7,2) NOT NULL COMMENT '商品价格',
  `quality` int(5) NOT NULL COMMENT '商品数量',
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '商品创建时间',
  `update_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '商品更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

#### 订单

```sql
CREATE TABLE `mall`.`order` (
  `order_id` bigint(32) NOT NULL COMMENT '订单主键',
  `user_id` bigint(32) NOT NULL COMMENT '用户 ID',
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '订单创建时间',
  `update_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '订单更新时间',
  `pay_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '订单支付状态：0 -未支付；1-已支付',
  `status` tinyint(2) NOT NULL COMMENT '订单状态：0-已提交；1-已付款；2-已发货；3-已收货；4-已取消；5-已退款',
  `total_price` decimal(10,2) DEFAULT NULL COMMENT '订单总价',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

#### 订单商品快照

```sql
CREATE TABLE `mall`.`order_product` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '商品快照主键',
  `order_id` bigint(32) NOT NULL COMMENT '订单 ID',
  `product_id` bigint(32) NOT NULL COMMENT '商品 ID',
  `product_name` varchar(12) NOT NULL COMMENT '商品快照名称',
  `product_description` varchar(255) DEFAULT NULL COMMENT '商品快照描述',
  `product_price` decimal(7,2) DEFAULT NULL COMMENT '商品快照价格',
  `quantity` int(5) NOT NULL COMMENT '商品快照数量',
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '商品快照创建时间',
  `updated_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '商品快照更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

---

### 作业三

作业题：（选做）尽可能多的从“常见关系数据库”中列的清单，安装运行，并使用上一题的 SQL 测试简单的增删改查。




---

### 作业四

作业题：（选做）基于上一题，尝试对各个数据库测试 100 万订单数据的增删改查性能。




---

### 作业五

作业题：（选做）尝试对 MySQL 不同引擎下测试 100 万订单数据的增删改查性能。


---

### 作业六

作业题：（选做）模拟 1000 万订单数据，测试不同方式下导入导出（数据备份还原）MySQL 的速度，包括 jdbc 程序处理和命令行处理。思考和实践，如何提升处理效率。

---

### 作业七

作业题：（选做）对 MySQL 配置不同的数据库连接池（DBCP、C3P0、Druid、Hikari），测试增删改查 100 万次，对比性能，生成报告。


---