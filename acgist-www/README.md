# 网站模块

#### 网站

* 提供页面服务

#### 网关

* 提供接口服务

## 目录结构

|目录|结构|
|:--|:--|
|acgist-www[-*]|网站模块|
|acgist-www-gateway[-*]|网关模块|

## 目录说明

|目录|说明|
|:--|:--|
|acgist-www|前台网站|
|acgist-www-admin|后台网站|
|acgist-www-gateway|前台网关|
|acgist-www-gateway-admin|后台网关|
|acgist-www-resources|静态资源|

## Session共享

Redis实现Session共享

## 静态资源

* 配置缓存：浏览器
* 静态资源：`cookie-free`

## Tomcat配置

* 修改Session名称
* 调整线程