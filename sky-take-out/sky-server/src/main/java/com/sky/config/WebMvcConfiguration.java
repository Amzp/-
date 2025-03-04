package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.interceptor.JwtTokenUserInterceptor;
import com.sky.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
/** @Slf4j 是 Lombok 提供的一个注解，用于在类中自动添加对SLF4J (Simple Logging Facade for Java) 日志对象的支持。
 * 使用 @Slf4j 注解会自动在类中生成一个以类名为参数的log静态常量。这个常量可以直接在类中使用，而无需在类中显式地为日志对象编写实例化语句。
 */
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor; // 管理端拦截器

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor; // 用户端拦截器

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        log.info("管理端拦截器注册...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
        log.info("用户端拦截器注册...");
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/shop/status"); // 获取店铺状态不需要用户登录，因此排除
        log.info("自定义拦截器注册完毕...");
    }

    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public Docket docketControllerAdmin() {
        log.info("开始通过knife4j生成管理端接口文档...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("苍穹外卖项目接口文档")
                .version("2.0")
                .description("苍穹外卖项目接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("管理端接口文档")
                .apiInfo(apiInfo)
                .select()
                // 扫描com.sky.controller.admin包下的所有类，生成接口文档
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.admin"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public Docket docketControllerUser() {
        log.info("开始通过knife4j生成用户端接口文档...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("苍穹外卖项目接口文档")
                .version("2.0")
                .description("苍穹外卖项目接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户端接口文档")
                .apiInfo(apiInfo)
                .select()
                // 扫描com.sky.controller.user包下的所有类，生成接口文档
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.user"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始设置静态资源映射...");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 扩展SpringMVC的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("开始扩展SpringMVC的消息转换器...");

        // 创建一个消息转换器对象，将 Java 对象序列化为 JSON 格式的数据，并在接收到 JSON 数据时将其反序列化为 Java 对象
        // 这里使用的是 Jackson 库，SpringMVC 默认使用的也是 Jackson 库
        MappingJackson2HttpMessageConverter converter
                = new MappingJackson2HttpMessageConverter();

        // 需要为消息转换器设置一个对象转换器，对象转换器可以将 Java 对象序列化为 JSON 格式的数据，
        converter.setObjectMapper(new JacksonObjectMapper());

        // 将自定义的消息转换器添加到 SpringMVC 的消息转换器列表中
        converters.add(0, converter);  // 注意：新的消息转换器将被插入到列表的开头，而现有的转换器将依次向后移动一个位置
    }
}
