CREATE DATABASE mall;

CREATE TABLE `mall`.`user` (
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '用户主键',
  `password` varchar(64) NOT NULL COMMENT '用户登录密码',
  `mobile_phone` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '用户创建时间',
  `update_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '用户更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_index` (`user_name`) USING BTREE COMMENT '用户名唯一性索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `mall`.`product` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '商品主键',
  `name` varchar(12) NOT NULL COMMENT '商品名称',
  `description` varchar(255) DEFAULT NULL COMMENT '商品描述',
  `price` decimal(7,2) NOT NULL COMMENT '商品价格',
  `quality` int(5) NOT NULL COMMENT '商品数量',
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '商品创建时间',
  `update_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '商品更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `mall`.`order` (
  `order_id` int(32) NOT NULL COMMENT '订单主键',
  `user_id` int(32) NOT NULL COMMENT '用户 ID',
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '订单创建时间',
  `update_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '订单更新时间',
  `pay_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '订单支付状态：0 -未支付；1-已支付',
  `status` tinyint(2) NOT NULL COMMENT '订单状态：0-已提交；1-已付款；2-已发货；3-已收货；4-已取消；5-已退款',
  `total_price` decimal(10,2) DEFAULT NULL COMMENT '订单总价',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `mall`.`order_product` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '商品快照主键',
  `order_id` int(32) NOT NULL COMMENT '订单 ID',
  `product_id` int(32) NOT NULL COMMENT '商品 ID',
  `product_name` varchar(12) NOT NULL COMMENT '商品快照名称',
  `product_description` varchar(255) DEFAULT NULL COMMENT '商品快照描述',
  `product_price` decimal(7,2) DEFAULT NULL COMMENT '商品快照价格',
  `quantity` int(5) NOT NULL COMMENT '商品快照数量',
  `created_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '商品快照创建时间',
  `updated_at` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '商品快照更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8