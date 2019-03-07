CREATE TABLE `metric` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(64) NOT NULL COMMENT '域账号',
  `main_metric` varchar(64) NOT NULL COMMENT '主度量',
  `sub_metric` varchar(64) NOT NULL COMMENT '度量项',
  `metric_item` json DEFAULT NULL COMMENT '度量项内容',
  `creator` varchar(64) NOT NULL COMMENT '创建人',
  `modifier` varchar(64) NOT NULL COMMENT '修改人',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'n' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='度量表';

CREATE TABLE `user_profile` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(64) NOT NULL COMMENT '工号',
  `user_name` varchar(64) NOT NULL COMMENT '名字',
  `dep` varchar(128) NOT NULL COMMENT '部门',
  `role` varchar(6) NOT NULL COMMENT '角色',
  `total_score` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '综合得分',
  `app_quality_score` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '代码质量分',
  `tech_influence_score` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '技术影响力分',
  `tech_contribution_score` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '技术贡献分',
  `dev_quality_score` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '开发质量分',
  `checkin_code_quantity` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT 'checkin代码量',
  `is_manager` char(1) DEFAULT NULL COMMENT '是否主管',
  `creator` varchar(64) NOT NULL COMMENT '创建人',
  `modifier` varchar(64) NOT NULL COMMENT '修改人',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `is_deleted` char(1) NOT NULL DEFAULT 'n' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='用户Profile表';
