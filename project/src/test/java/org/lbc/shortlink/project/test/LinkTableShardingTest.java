package org.lbc.shortlink.project.test;

public class LinkTableShardingTest {
    public static final String SQL = "CREATE TABLE `t_link_%d` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `gid` varchar(32) DEFAULT 'default' COMMENT '分组 id',\n" +
            "  `domain` varchar(128) DEFAULT NULL COMMENT '域名',\n" +
            "  `short_uri` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '短链接',\n" +
            "  `full_short_url` varchar(256) DEFAULT NULL COMMENT '完整短链接',\n" +
            "  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',\n" +
            "  `click_num` int(11) DEFAULT 0 COMMENT '点击量',\n" +
            "  `favicon` varchar(256) DEFAULT NULL COMMENT '原始网站图标',\n" +
            "  `status_enable` tinyint(1) DEFAULT 0 COMMENT '启用标识： 0 未启用，1 启用',\n" +
            "  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型：0 接口创建，1 控制台创建',\n" +
            "  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型：0 永久有效 1 自定义',\n" +
            "  `valid_date` bigint(20) DEFAULT NULL COMMENT '有效期时间戳(秒)',\n" +
            "  `remark` varchar(1024) DEFAULT NULL COMMENT '描述',\n" +
            "  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间戳(秒)',\n" +
            "  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间戳(秒)',\n" +
            "  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0:未删除 1：已删除',\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=2055270514833760259 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";
    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf((SQL) + "%n", i);
        }
    }
}
