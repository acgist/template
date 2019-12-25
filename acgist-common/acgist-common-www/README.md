# 网站模块

提供网站（页面）、网关（接口）访问

## 权限鉴定

网站：Session

网关：签名

## 注意事项

#### 页面静态化

###### 参考

`StaticService`、`FreeMarkerService`

###### 配置

```
# 首页
#acgist.index=index.html
# 首页模板
#acgist.index.template=index.ftl
# 静态页面路径
acgist.html.path=file:E:/acgist-www/html/
# 静态资源路径
acgist.static.path=file:E:/acgist-www/resources/
```

#### 静态资源

静态资源建议配置独立服务器

###### 配置

```
# 静态资源地址
acgist.static.url=http://192.168.1.100:28888
```