package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
/*通过这个注解，AliOssProperties 类会自动绑定以 sky.alioss 为前缀的配置属性到类的对应字段中。
在使用这个注解时，Spring Boot 会尝试自动注入以 sky.alioss 为前缀的配置属性，
例如 sky.alioss.endpoint, sky.alioss.accessKeyId等，并将它们与 AliOssProperties 对象进行映射。*/
@ConfigurationProperties(prefix = "sky.alioss")
@Data
public class AliOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}
