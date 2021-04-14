SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cnbm_intf
-- ----------------------------
DROP TABLE IF EXISTS `cnbm_intf`;
CREATE TABLE `cnbm_intf`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口编码',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口名称',
  `input_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产者系统名称',
  `input_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产者系统地址',
  `input_param_format` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产者系统传入参数格式',
  `output_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消费者系统名称',
  `output_format` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消费者系统传入参数格式',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口描述',
  `input_class_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产者系统类名',
  `input_method_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产者系统方法名',
  `output_class_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消费者系统类名',
  `output_method_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产者系统方法名',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态0启用，1禁用',
  `created_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_retry` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否重试',
  `max_retry_count` int(10) NULL DEFAULT 3 COMMENT '重试次数',
  `timeout` int(10) NULL DEFAULT NULL COMMENT '超时时间',
  `ip_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP限制',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cnbm_intf_log
-- ----------------------------
DROP TABLE IF EXISTS `cnbm_intf_log`;
CREATE TABLE `cnbm_intf_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `intf_id` int(11) NULL DEFAULT NULL COMMENT '接口管理表',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态0成功1失败',
  `input_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产者调用状态0成功1失败',
  `input_params` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '生产者调用参数',
  `input_return_val` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '生产者响应结果',
  `output_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消费者调用抓状态0成功1失败',
  `output_params` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '消费者调用参数',
  `failed_reason` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '失败原因',
  `created_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `ip_address` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '访问地址',
  `method_ext` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调用方法',
  `response_time_ext` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '响应时间',
  `retry_count` int(10) NULL DEFAULT 0 COMMENT '记录重试次数',
  `intf_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `intf_id`(`intf_id`) USING BTREE,
  CONSTRAINT `cnbm_intf_log_ibfk_1` FOREIGN KEY (`intf_id`) REFERENCES `cnbm_intf` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3102 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
