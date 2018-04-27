package com.tool.sql.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SqlParserTest {
    @Test
    public void testParse() {
        Map<String, String> mapIO = new HashMap<String, String>() {{
            put(null, null);
            put("", null);

            // xcd
            put("/*", null);
            put("Navicat MySQL Data Transfer", null);
            put("Source Server         : aliyundb2.0", null);

            put("Source Database       : oms-v2-st", "DB{name='oms-v2-st', comment='null'}");
            put("Date: 2017-09-22 17:13:25", null);
            put("*/", null);
            put("SET FOREIGN_KEY_CHECKS=0;", null);
            put("-- ----------------------------", null);
            put("-- Table structure for agif_agent", null);
            put("DROP TABLE IF EXISTS `agif_agent`;", null);

            put("CREATE TABLE `agif_agent` (", "Table{name='agif_agent', comment='null'}");
            put("`AGENT_ID` varchar(36) COLLATE utf8_unicode_ci NOT NULL COMMENT '买手ID',", "Field{name='AGENT_ID', comment='买手ID'}");

            put("PRIMARY KEY (`TSP_STD_ID`),", null);
            put(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;", null);

            // ody: prod
            put("-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)", null);
            put("--", null);

            put("-- Host: 192.168.1.140    Database: ad", "DB{name='ad', comment='null'}");
            put("-- ------------------------------------------------------", null);
            put("-- Server version\t5.6.29", null);
            put("/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;", null);
            put("-- Table structure for table `ad_code`", null);
            put("DROP TABLE IF EXISTS `ad_code`;", null);

            put("CREATE TABLE `ad_code` (", "Table{name='ad_code', comment='null'}");
            put("`id` bigint(20) NOT NULL AUTO_INCREMENT,", "Field{name='id', comment='null'}");
            put("`page_type` bigint(20) NOT NULL COMMENT '广告,页面',", "Field{name='page_type', comment='广告,页面'}");
            put("`company_id` bigint(20) DEFAULT NULL COMMENT '公司id',", "Field{name='company_id', comment='公司id'}");

            put("PRIMARY KEY (`id`),", null);
            put("KEY `index_adcode_page` (`page_type`,`code`,`is_deleted`),", null);
            put(") ENGINE=InnoDB AUTO_INCREMENT=1194020000000001 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;", null);

            // art
            put("CREATE TABLE `ART_DATABASES` (", "Table{name='ART_DATABASES', comment='null'}");
        }};

        for (Map.Entry<String, String> io : mapIO.entrySet()) {
            DBItem item = SqlParser.parse(io.getKey());
            String ret = item == null ? null : item.toString();
            Assert.assertEquals(io.getValue(), ret);
        }
    }

    @Test
    public void testParseName() {
        Map<Object[], String> mapIO = new HashMap<Object[], String>() {{
            put(new Object[] {null, null, 0, null}, "");
            put(new Object[] {"name", null, 0, null}, "name");
            put(new Object[] {"create table ea", " ", 2, null}, "ea");
            put(new Object[] {"create table `ea`", " ", 2, new String[] {"`"}}, "ea");

            put(new Object[] {"Database: ad", SqlConfig.DBSplitter, SqlConfig.DBIndex, SqlConfig.DBTrimArr}, "ad");
            put(new Object[] {"Database       : oms-v2-st", SqlConfig.DBSplitter, SqlConfig.DBIndex, SqlConfig.DBTrimArr}, "oms-v2-st");

            put(new Object[] {"CREATE TABLE `agif_agent` (", SqlConfig.TableSplitter, SqlConfig.TableIndex, SqlConfig.TableTrimArr}, "agif_agent");
            put(new Object[] {"CREATE TABLE `ad_code` (", SqlConfig.TableSplitter, SqlConfig.TableIndex, SqlConfig.TableTrimArr}, "ad_code");

            put(new Object[] {"`id` bigint(20) NOT NULL AUTO_INCREMENT,", SqlConfig.FieldSplitter, SqlConfig.FieldIndex, SqlConfig.FieldTrimArr}, "id");
            put(new Object[] {"`AGENT_ID` varchar(36) COLLATE utf8_unicode_ci NOT NULL COMMENT '买手ID',", SqlConfig.FieldSplitter, SqlConfig.FieldIndex, SqlConfig.FieldTrimArr}, "AGENT_ID");

            put(new Object[]{"COMMENT '广告 页面',", SqlConfig.FieldCommentSplitter, SqlConfig.CommentIndex, SqlConfig.CommentTrimArr}, "广告 页面");
            put(new Object[]{"COMMENT '买手ID',", SqlConfig.FieldCommentSplitter, SqlConfig.CommentIndex, SqlConfig.CommentTrimArr}, "买手ID");
            put(new Object[]{"`create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间 - 应用操作时间',", SqlConfig.FieldCommentSplitter, SqlConfig.CommentIndex, SqlConfig.CommentTrimArr}, "创建时间 - 应用操作时间");
            put(new Object[]{" COMMENT='客户= 表';", SqlConfig.TableCommentSplitter, SqlConfig.CommentIndex, SqlConfig.CommentTrimArr}, "客户= 表");
            put(new Object[]{") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='页面关联品牌';", SqlConfig.TableCommentSplitter, SqlConfig.CommentIndex, SqlConfig.CommentTrimArr}, "页面关联品牌");
        }};

        for (Map.Entry<Object[], String> io : mapIO.entrySet()) {
            Object[] params = io.getKey();
            String ret = SqlParser.parseName((String) params[0], (String) params[1], (Integer) params[2], (String[]) params[3]);
            Assert.assertEquals(io.getValue(), ret);
        }
    }
}
