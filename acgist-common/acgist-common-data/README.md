# 数据库模块

Maven添加：

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

添加扫描：

```
@EntityScan("com.acgist.data.**.entity")
@ComponentScan({"com.acgist.data"})
@EnableJpaRepositories(basePackages = "com.acgist.data.**.repository", repositoryBaseClass = BaseExtendRepositoryImpl.class)
@EnableTransactionManagement
```
