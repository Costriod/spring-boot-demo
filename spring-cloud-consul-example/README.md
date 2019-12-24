# spring-cloud-consul做配置中心的基本配置
#### 1.可以在consul中配置 logging.level.root=debug，那么java应用会自动刷新，然后java应用的log默认级别变成了debug
#### 2.可以在创建@Bean方法上面指定@ConfigurationProperties，那么在创建完bean对象之后，spring能自动执行@Bean创建的bean内部的set方法；而且只要是consul配置刷新了，那么spring还能够将新配置刷新到刚刚的这个@Bean创建的bean里面（前提是这个bean指定了@ConfigurationProperties）
#### 3.使用consul做配置中心，则需要在resources目录下面创建一个bootstrap.yml或bootstrap.properties文件，文件内容如下：
```
spring.application.name=consul-demo
spring.profiles.active=prod,cus
#consul基本配置
spring.cloud.consul.host=127.0.0.1
spring.cloud.consul.port=8500
#启用consul配置中心
spring.cloud.consul.config.enabled=true
#启用consul自动刷新配置
spring.cloud.consul.config.watch.enabled=true
#watch线程读取consul api等待的秒数（官方文档这句话没看明白）
spring.cloud.consul.config.watch.wait-time=1
#前后两次查询consul的间隔时间
spring.cloud.consul.config.watch.delay=10000

#consul有三种配置格式：properties、yml、files
#第一种properties格式配置如下：（yml和properties配置方法基本上一样，只不过format不一样）
#基础文件夹，默认值config
spring.cloud.consul.config.prefix=custom
#如果active多个profile，那么在consul的配置里面用","作为profile分隔符
spring.cloud.consul.config.profile-separator=,
#默认context，默认是application
spring.cloud.consul.config.default-context=mycontext
spring.cloud.consul.config.format=properties
spring.cloud.consul.config.data-key=mykey
#对于上述配置，首先在consul的k-v配置里面创建几个路径如下：
#/custom/consul-demo,prod/mykey   （如果已经active的profile有prod,cus,dev三个，那么需要配置consul-demo,prod/mykey  consul-demo,cus/mykey consul-demo,dev/mykey三个路径）
#/custom/consul-demo/mykey
#/custom/mycontext,prod/mykey
#/custom/mycontext/mykey
#上面的custom代表默认prefix，也就是上面配置的custom，
#上面的consul-demo表示项目名，consul-demo目录仅提供给consul-demo项目使用，consul-demo,prod代表consul-demo项目已经active的profile，也就是上面spring.profiles.active=prod
#上面的mycontext代表默认项目配置路径，该目录是所有项目公用的配置目录
#上面的mykey代表配置路径最底层（类似于我们的文件），需要在mykey里面配置我们的property
#上述配置的优先级是从上往下，consul-demo,prod/mykey优先级最高


#剩下一种是files格式：
#基础文件夹，默认值config
spring.cloud.consul.config.prefix=custom
#files格式的情况下profile的默认分隔符变成了"-"
spring.cloud.consul.config.profile-separator=-
#默认context，默认是application
spring.cloud.consul.config.default-context=mycontext
spring.cloud.consul.config.format=files
#files格式情况下，spring.cloud.consul.config.data-key就不起作用了
#对于files格式配置，首先在consul的k-v配置里面创建几个路径如下：
#/custom/consul-demo-prod.properties
#/custom/consul-demo-prod.yml
#/custom/consul-demo.properties
#/custom/consul-demo.properties
#/custom/mycontext-prod.properties
#/custom/mycontext-prod.yml
#/custom/mycontext.properties
#/custom/mycontext.yml
#对于files格式配置，properties优先级高于yml，所以properties会覆盖yml
#同样的对于上述配置，优先级是从上往下，consul-demo-prod.properties优先级最高
```