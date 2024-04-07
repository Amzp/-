package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request,    // 请求对象，包含请求信息、请求头、请求参数等
                             HttpServletResponse response,  // 响应对象，用于向客户端返回响应数据
                             Object handler // 处理器对象，包含处理请求的方法和相关信息
    ) throws Exception {
        System.out.println("当前线程的id：" + Thread.currentThread().getId());

        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            log.info("当前请求不是Controller的方法，直接放行");
            return true;
        }
        log.info("当前请求是Controller的方法，开始执行jwt校验");

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        //2、校验令牌
        try {
            log.info("开始执行管理端的jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("当前员工id：{}", empId);
            /*将从 JWT 声明中获得的员工ID（empId）设置到 BaseContext 的线程局部变量中。
            这样就将员工ID与当前线程相关联，以便后续的代码可以方便地访问和使用这个员工ID。
            这种模式在 Web 应用程序中常常用于跟踪当前登录用户的会话信息或权限信息等*/
            BaseContext.setCurrentId(empId);

            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应 401 状态码
            log.error("管理端的jwt校验失败：{}", ex.getMessage());
            response.setStatus(401);
            return false;
        }
    }
}
