package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component // 声明为Spring Bean
/**
 * @ConfigurationProperties 注解将属性文件或 YAML 配置文件中的属性值绑定到一个特定的 Java 对象上。
 * 这允许你轻松地访问和使用配置文件中的属性，并且提供了类型安全和自动完成的便利性。
 * prefix = "sky.jwt" 意味着这个注解会绑定以 sky.jwt 开头的配置属性到目标 Java 对象上
 * \sky-server\src\main\resources\application.yml
 *    sky:
 *   jwt:
 *     # 设置jwt签名加密时使用的秘钥
 *     admin-secret-key: itcast
 *     # 设置jwt过期时间
 *     admin-ttl: 7200000
 *     # 设置前端传递过来的令牌名称
 *     admin-token-name: token
 */
@ConfigurationProperties(prefix = "sky.jwt")
@Data
public class JwtProperties {

    /**
     * 管理端员工生成jwt令牌相关配置
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
