# acgist

## 介绍

简单的项目，提供一套完整建站模板。

使用`Dubbo`进行服务调用

后台网站和网关使用`Vue` + `Node.js`实现前后端分离

## 软件技术

|技术|版本|功能|主页|
|:--|:--|:--|:--|
|Java|11|开发语言||
|-|-|-|-|
|Nginx||网关||
|-|-|-|-|
|Redis||缓存||
|MySQL||数据库||
|RabbitMQ||消息||
|-|-|-|-|
|Vue||前后端分离||
|Node.js||前后端分离||
|iview-admin||后台网站||
|Bootstrap||前台网站样式||
|-|-|-|-|
|Dubbo||服务治理|https://github.com/apache/dubbo|
|ZooKeeper||注册中心||
|DubboAdmin||服务管理|https://github.com/apache/dubbo-admin|
|-|-|-|-|
|SprintBoot||框架||

## 项目结构

|目录|描述|
|:--|:--|
|acgist-common|通用模块|
|acgist-service|服务模块|
|acgist-www|网站模块|

## 包名结构

|包名|描述|
|:--|:--|
|com.acgist.main|启动方法|
|com.acgist.utils|工具|
|com.acgist.core.服务模块.*|核心模块|
|com.acgist.data.服务模块.*|数据模块|
|com.acgist.*.服务模块.aop|AOP|
|com.acgist.*.服务模块.pojo|POJO|
|com.acgist.*.服务模块.config|配置|
|com.acgist.*.服务模块.exception|异常|
|com.acgist.*.服务模块.gateway|网关|
|com.acgist.*.服务模块.controller|控制器|
|com.acgist.*.服务模块.interceptor|拦截器|
|com.acgist.*.服务模块.service|本地服务或者服务接口|
|com.acgist.*.服务模块.service.impl|服务接口实现|
|com.acgist.*.服务模块.repository|DAO|

## 配置

不使用数据库

```
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
```

## 疑问

#### 为什么不用SpringCloud？

首先承认SpringCloud是非常出色的，使用`Dubbo`主要出于以下几点：

1. SpringCloud依赖太多功能过于复杂
2. SpringCloud提供服务全部依赖Web模块

#### 为什么前台网站不使用前后端分离？

前端网站主要提供用户访问页面，前后端分离不利于SEO。
