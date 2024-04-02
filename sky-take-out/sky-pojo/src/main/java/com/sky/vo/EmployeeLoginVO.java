package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder    // Lombok会为这个类生成一个建造者模式的构造器。使用建造者模式，可以使用链式调用的方式来设置对象的属性，从而简化对象的创建过程。
@NoArgsConstructor  // @NoArgsConstructor 注解可以直接放在类上，它会为该类生成一个无参构造方法，Lombok 会在编译时自动生成这个方法。
@AllArgsConstructor  // @AllArgsConstructor 注解可以直接放在类上，它会为该类生成一个包含所有属性的构造方法，Lombok 会在编译时自动生成这个方法。
@ApiModel(description = "员工登录返回的数据格式") // @ApiModel 注解用于生成 Swagger 文档，描述返回的数据格式。
// EmployeeLoginVO 类实现了 Serializable 接口。通过实现 Serializable 接口，该类表明它的对象可以被序列化为字节流，从而可以在网络上传输或保存在持久存储设备上。
public class EmployeeLoginVO implements Serializable {

    @ApiModelProperty("主键值")  // 这个注解表示 id 属性的含义是 "主键值"。这个描述信息可以在生成的 API 文档中使用
    private Long id;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("jwt令牌")
    private String token;

}
