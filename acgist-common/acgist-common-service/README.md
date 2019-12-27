# 服务模块

使用`Dubbo`实现服务调用

## Dubbo

## 注解

```java
@DubboComponentScan({ "com.acgist.core.**.service.impl" })
```

## RabbitMQ

#### 依赖

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

#### 注解

```java
@EnableRabbit
```

#### 配置

添加`Virtual host`：`acgist`

## 注意事项

#### ZooKeeper

需要安装`ZooKeeper`