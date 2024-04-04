package com.sky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/*@SpringBootApplication 是一个注解，用于标识一个主程序类（Main class）是Spring Boot应用程序的入口点和配置类。
    这个注解整合了多个其他注解，包括 @Configuration、@EnableAutoConfiguration 和 @ComponentScan。这意味着使用 @SpringBootApplication 注解等同于使用这三个注解。
    以下是各个组成部分的含义：
            @Configuration：标识该类作为应用程序的Bean定义的源，可以使用@Bean注解来注册bean。
            @EnableAutoConfiguration：启用Spring Boot自动配置，自动根据classpath设置和其他bean来设置bean。
            @ComponentScan：启用组件扫描，以确保Spring会自动发现和注册应用程序上下文中的bean。
    通常情况下，@SpringBootApplication 注解会放在主程序类（Main class）上。
    当应用程序被启动时，Spring Boot会扫描注解所在的包及其子包中的组件，并根据需要自动配置Spring应用程序上下文。
    这个注解简化了Spring应用程序的配置，并且是使用Spring Boot创建新应用程序的推荐方式。*/
@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
public class SkyApplication {
    public static void main(String[] args) {
        /*SpringApplication.run(SkyApplication.class, args); 是 Spring Boot 应用程序的入口点，作用是启动 Spring Boot 应用程序
            1.根据传入的参数和启动类（这里是SkyApplication.class）创建一个新的应用程序上下文（Application Context）。
            2.注册加载配置（Configuration）类、服务（Service）类、控制器（Controller）类等组件。
            3.启动Spring应用程序。
            4.执行Spring Boot的自动配置（Auto-configuration）过程，例如加载默认的配置、自动扫描注册bean等。
            5.在服务器上启动Spring Web容器（比如Tomcat）。*/
        SpringApplication.run(SkyApplication.class, args);
        log.info("server started");
    }
}
