# Springboot整合Rabbitmq

### 整合步骤
1.  整合SpringBoot rabbitmq依赖包
~~~shell script
  <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
~~~

2. 在properties配置文件中添加相应的rabbitMq的配置
3. 定义相关配置文件来绑定RabbitMq配置信息或者直接在消费者使用注解进行绑定相关配置。
> 如fanout模式和direct模式 为使用配置文件进行配置rabbitMq信息
> topic模式使用的以注解方式进行绑定具体可参考代码