# acgist

## 介绍

简单的项目，提供一套完整建站模板。

## 软件技术

|名称|版本|
|:--|:--|
|[Java](http://openjdk.java.net/)|11|
|[Nginx](http://nginx.org/)|1.17.6|
|[Redis](https://redis.io/)|5.0.7|
|[MySQL](https://dev.mysql.com/downloads/mysql/5.7.html)|5.7.28|
|[RabbitMQ](https://www.rabbitmq.com/)|
|[ZooKeeper](https://zookeeper.apache.org/)|3.5.6|
|[Vue.js](https://cn.vuejs.org/)|2.X|
|[Node.js](https://nodejs.org/en/)|12.14.0|
|Bootstrap|-|
|[iview-admin](https://github.com/iview/iview-admin)|-|
|[Dubbo](https://github.com/apache/dubbo)|2.7.4.1|
|[DubboAdmin](https://github.com/apache/dubbo-admin)|0.1|
|[SprintBoot](https://start.spring.io/)|2.2.2.RELEASE|

## 目录说明

|目录|说明|
|:--|:--|
|acgist-www|网站模块|
|acgist-common|通用模块|
|acgist-service|服务模块|

## 包名结构

|包名|结构|
|:--|:--|
|com.acgist.main|启动方法|
|com.acgist.core|核心模块|
|com.acgist.data|数据模块|
|com.acgist.utils|工具模块|
|com.acgist.*.服务模块.aop|AOP|
|com.acgist.*.服务模块.pojo|POJO|
|com.acgist.*.服务模块.config|配置|
|com.acgist.*.服务模块.gateway|网关|
|com.acgist.*.服务模块.service|本地服务或者服务接口|
|com.acgist.*.服务模块.service.impl|服务接口实现|
|com.acgist.*.服务模块.exception|异常|
|com.acgist.*.服务模块.repository|数据库|
|com.acgist.*.服务模块.controller|控制器|
|com.acgist.*.服务模块.interceptor|拦截器|

## 表名

* ts_系统表
* tb_业务表

## 疑问

#### 为什么不用SpringCloud？

首先承认SpringCloud是非常出色的，使用`Dubbo`主要出于以下几点：

1. SpringCloud依赖太多功能过于复杂
2. SpringCloud提供服务全部依赖Web模块

#### 为什么前台网站不使用前后端分离？

前端网站主要提供用户访问页面，前后端分离不利于SEO。
