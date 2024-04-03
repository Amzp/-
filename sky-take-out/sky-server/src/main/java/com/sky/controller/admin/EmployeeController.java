package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登陆方法")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO
                                         // @RequestBody 注解标注在 EmployeeLoginDTO employeeLoginDTO 参数上，表示这个参数会从HTTP请求的body中获取数据。
    ) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();  //声明一个Map来存放自定义的声明
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),  //  JwtProperties 对象中获取管理员（admin）JWT的秘钥
                jwtProperties.getAdminTtl(),  //  JwtProperties 对象中获取管理员（admin）JWT的过期时间
                claims  );

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出方法")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO) { // @RequestBody注解标注接收了一个 EmployeeDTO 对象，这使得控制器能够接收并处理来自客户端的 JSON 格式数据。
        System.out.println("当前线程的id：" + Thread.currentThread().getId());

        log.info("新增员工：{}", employeeDTO);
        employeeService.save(employeeDTO);

        return Result.success();
    }

    /**
     * 分页查询员工
     *
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询员工")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("分页查询员工：{}", employeePageQueryDTO);

        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 启用或禁用员工账号
     *
     * @param status 状态 0：禁用 1：启用
     * @param id     员工id
     * @return
     */
    @PostMapping("/status/{status}")
    /*为什么用Post请求？
    * 通常，处理像“启用或禁用员工账号”这样的操作时，应该使用POST或者PUT请求，因为这类操作涉及到对资源的修改。
    * 从语义上讲，GET请求应该用于获取资源的信息，而POST或PUT请求应该用于修改资源的状态。*/
    @ApiOperation(value = "启用或禁用员工账号")
    public Result startOrStop(@PathVariable("status") Integer status,
                              @RequestParam("id") Long id){
        log.info("启用或禁用员工账号：status={}, id={}", status, id);
        // 调用service层方法，修改员工状态
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询员工信息")
    public Result<Employee> queryById(@PathVariable("id") Long id) {
        log.info("根据id查询员工信息：id={}", id);
        Employee employee = employeeService.queryById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "编辑员工信息")
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息：{}", employeeDTO);
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }


}
