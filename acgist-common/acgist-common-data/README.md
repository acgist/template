# 数据模块

配置环境：`data-[dev|test|prod]`

## 缓存

#### 依赖

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

#### 注解

```java
@ComponentScan({"com.acgist.data"})
```

## 数据库

#### 依赖

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

#### 注解

```java
@EntityScan("com.acgist.data.**.entity")
@ComponentScan({"com.acgist.data"})
@EnableJpaRepositories(basePackages = "com.acgist.data.**.repository", repositoryBaseClass = BaseExtendRepositoryImpl.class)
@EnableTransactionManagement
```

## Redis

#### 依赖

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

#### 注解

```java
@ComponentScan({"com.acgist.data"})
```

## 注意事项

#### Redis

缓存需要安装`Redis`