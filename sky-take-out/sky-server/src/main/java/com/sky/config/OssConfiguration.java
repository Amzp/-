package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: OssConfiguration
 * Package: com.sky.config
 * Description: 配置类，用于创建AliOssUtil工具类对象
 *
 * @Author Rainbow
 * @Create 2024/4/3 22:10
 * @Version 1.0
 */
@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    /*Q:@Bean不是单例模式吗？为什么还需要@ConditionalOnMissingBean？
    * A1:Spring Framework 中默认情况下 @Bean 注解创建的 Bean 是单例的，即在整个应用程序的生命周期中只存在一个实例。
            因此，在使用 @Bean 注解创建一个 Bean 时，通常情况下不会重复创建相同类型的 Bean 实例。
            那么为什么还需要 @ConditionalOnMissingBean 呢？实际上，@ConditionalOnMissingBean 注解不是用来避免重复创建单例 Bean 的，
            而是用来根据条件实例化 Bean 的。它允许您根据容器是否已经存在某个特定类型的 Bean 来决定是否要注册另一个 Bean。
            举个例子，假设你的应用程序中可能会引入多个配置类，并且每个配置类中都定义了相同类型的 Bean，
            那么使用@ConditionalOnMissingBean 注解可以确保只有在容器中不存在指定类型的 Bean 实例时才会注册另一个 Bean。这样就可以避免这个类型的 Bean 被重复注册或重复实例化。
            因此，@ConditionalOnMissingBean 注解实际上是用于配置自动装配的条件，以确保根据需要条件化地注册 Bean。
        *
      A2：@ConditionalOnMissingBean，它是修饰bean的一个注解，主要实现的是，当你的bean被注册之后，如果而注册相同类型的bean，就不会成功，它会保证你的bean只有一个，即你的实例只有一个。
            如果不加@ConditionalOnMissingBean，当你注册多个相同的bean时，会出现异常*/
    @ConditionalOnMissingBean // 检查容器中是否已存在指定类型的 Bean 实例。如果不存在，则会注册相应的 Bean；若容器中已存在AliOssUtil对象，则不再创建
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建AliOss文件上传的工具类对象...");
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
