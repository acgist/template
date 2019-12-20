# acgist

## 介绍

简单的项目，提供一套完整建站模板。

使用`Dubbo`进行服务调用

后台模块使用`Vue` + `Node.js`实现前后端分离

## 软件技术

|技术|版本|功能|主页|
|:--|:--|:--|:--|
|Vue||前后端分离||
|Java|11|开发语言||
|Redis||缓存||
|MySQL||数据||
|Nginx||网关||
|Dubbo||服务治理|https://github.com/apache/dubbo|
|Node.js||前后端分离||
|RabbitMQ||消息||
|ZooKeeper||注册中心||
|DubboAdmin||服务管理|https://github.com/apache/dubbo-admin|
|SprintBoot||框架||

## 项目结构

|目录|描述|
|:--|:--|
|acgist-www|网站模块|
|acgist-common|通用模块|
|acgist-service|服务模块|

## 疑问

#### 为什么不用SpringCloud？

首先承认SpringCloud在微服务方面是非常出色的，但是SpringCloud依赖太多，而且使用没有Dubbo直接。

#### 为什么前台网站不使用前后端分离？

前端网站主要提供用户访问页面，前后端分离不利于SEO。