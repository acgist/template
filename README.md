<h1 align="center">acgist</h1> 

<p align="center">
	<a target="_blank" href="https://openjdk.java.net/">
		<img alt="Java" src="https://img.shields.io/badge/Java-11-yellow.svg?style=flat-square" />
	</a>
	<img alt="GitHub Workflow Status" src="https://img.shields.io/github/workflow/status/acgist/template/build?style=flat-square">
	<img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/acgist/acgist?style=flat-square" />
	<img alt="GitHub code size in bytes" src="https://img.shields.io/github/languages/code-size/acgist/template?color=crimson&style=flat-square" />
	<img alt="GitHub" src="https://img.shields.io/github/license/acgist/acgist?style=flat-square" />
</p>

## 介绍

简单模板项目

## 软件技术

|名称|版本|
|:--|:--|
|[Java](http://openjdk.java.net/)|11|
|[Nginx](http://nginx.org/)|1.17.6|
|[Redis](https://redis.io/)|5.0.7|
|[Dubbo](https://github.com/apache/dubbo)|2.7.4.1|
|[MySQL](https://dev.mysql.com/downloads/mysql/5.7.html)|5.7.28|
|[Node.js](https://nodejs.org/en/)|12.14.0|
|[RabbitMQ](https://www.rabbitmq.com/)|3.7.10|
|[ZooKeeper](https://zookeeper.apache.org/)|3.5.6|
|[SprintBoot](https://start.spring.io/)|2.2.2.RELEASE|
|[DubboAdmin](https://github.com/apache/dubbo-admin)|0.1|
|[vue-admin-template](https://github.com/PanJiaChen/vue-admin-template)||

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
|com.acgist.*.服务模块.www|网站|
|com.acgist.*.服务模块.gateway|网关|
|com.acgist.*.服务模块.service|本地服务或者服务接口|
|com.acgist.*.服务模块.service.impl|服务接口实现|
|com.acgist.*.服务模块.listener|监听|
|com.acgist.*.服务模块.exception|异常|
|com.acgist.*.服务模块.repository|数据库|
|com.acgist.*.服务模块.controller|控制器|
|com.acgist.*.服务模块.interceptor|拦截器|

## 表名

* ts_系统表
* tb_业务表

## 安全

[安全](./acgist-guide/安全.md)

## 疑问

#### 为什么不用SpringCloud？

首先承认SpringCloud是非常出色的，使用`Dubbo`主要出于以下几点：

1. SpringCloud依赖太多功能过于复杂
2. SpringCloud提供服务全部依赖Web模块

#### 为什么前台网站不使用前后端分离？

前端网站主要提供用户访问页面，前后端分离不利于SEO。

## 注意事项

#### 级联查询

级联查询一般使用延迟加载和瞬时状态，使用`Dubbo`查询时可能会出现信息等于`null`，这是需要传输时手动创建对象。

## 配置说明

#### 可以覆盖

|配置|默认|说明|
|:--|:--|:--|
|`acgist.version`|-|软件版本|
|`acgist.index`|`index.html`|首页|
|`acgist.index.template`|`index.ftl`|首页模板|
|`acgist.error.template`|`/error`|错误模板|
|`acgist.html.path`|-|静态页面路径|
|`acgist.static.path`|-|静态资源路径|
|`acgist.permission`|`false`|加载权限|
|`acgist.dubbo.port`|随机|`Dubbo`端口|
|`acgist.session.name`|`ACGISTID`|`SessionCookieID`|
|`acgist.redis.database`|`0`|`Redis`索引|
|`acgist.zookeeper.port`|`2181`|`ZooKeeper`端口|
|`acgist.zookeeper.host`|`127.0.0.1`|`ZooKeeper`地址|
|`acgist.static.url`|`acgist.static.base.url`|静态资源域名|

#### 固定配置

|配置|默认|说明|
|:--|:--|:--|
|`acgist.service.version`|`1.0.0`|服务版本|
|`acgist.gateway.duration`|`10`|请求有效时间（分钟）|
|`acgist.static.base.url`|`http://192.168.1.100:28888`|静态资源基础域名|
