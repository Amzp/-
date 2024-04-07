package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * ClassName: AutoFill
 * Package: com.sky.aspect
 * Description: 自定义切面，实现公共字段自动填充处理逻辑
 *
 * @Author Rainbow
 * @Create 2024/4/3 17:30
 * @Version 1.0
 */
/*@Aspect 是 Spring Framework 中用于定义切面的注解，它允许您声明一个类为切面，
并在其中定义通知（advice）、切点（pointcut）和引入（introduction），从而实现对应用程序中横切关注点的模块化和重用。*/
@Aspect // 声明为切面
@Component // 声明为 Spring Bean
@Slf4j // 引入日志组件
public class AutoFillAspect {


    /**表达式的含义：
        execution: 声明这是一个方法执行的切点表达式，表示匹配方法的执行。
            *: 匹配任意的返回类型。
            com.sky.mapper.*: 前面这一部分表示匹配位于com.sky.mapper包（以及其子包）下的类。
            *: 匹配任意方法名。
            (..): 表示匹配任意参数列表的方法。
        @ annotation(com.sky.annotation.AutoFill): 这部分是一个注解切点表达式，表示匹配使用了@AutoFill注解的方法。
      这个复合切点表达式将匹配位于com.sky.mapper包及其子包下的类中，同时被标注了@AutoFill注解的所有方法。*/
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut() {}

    /**
     *  @Before注解 用于定义前置通知，在目标方法执行之前执行，在通知中进行公共字段的自动填充处理。
     * 这里的切点表达式是autoFillPointcut()，它将匹配位于com.sky.mapper包及其子包下的类中，同时被标注了@AutoFill注解的所有方法。
     */
    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始自动填充公共字段...");

        /* 1. 获取当前被拦截的方法上的数据库操作类型
            joinPoint.getSignature() 是在 Spring AOP 中用于获取连接点（join point）的签名信息的方法。
                在切面编程中，连接点表示程序执行的特定点，如方法执行、异常抛出等，并通过切点表达式进行匹配。
                通过joinPoint.getSignature()可以获得用于描述连接点的 Signature 对象，它包含了连接点的各种信息，如方法名、返回类型、参数类型等。
            joinPoint.getSignature()方法返回一个Signature对象，但是通常我们实际知道连接点是一个方法，我们需要将其转换为MethodSignature以获取方法的额外信息。
                这当我们调用 (MethodSignature) joinPoint.getSignature() 时，我们将 Signature 对象强制转换为 MethodSignature 对象，
                以便能够访问与方法相关的信息，如方法名称，返回类型和参数等。*/
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 获取方法签名对象
        AutoFill autoFill = signature.getMethod() // 获取连接点的方法
                                     .getAnnotation(AutoFill.class); // 获取方法上的@AutoFill注解
        OperationType operationType = autoFill.value(); // 获取@AutoFill注解的值

        // 2. 获取当前被拦截的方法的参数-实体对象，注意，实体对象必须是第一个参数
        Object[] joinPointArgs = joinPoint.getArgs(); // 获取连接点所在方法的参数列表
        if (joinPointArgs == null || joinPointArgs.length == 0){
            log.error("自动填充失败，实体对象不能为空！");
            return;
        }
        Object entity = joinPointArgs[0]; // 获取第一个参数，即实体对象

        // 3. 准备赋值的数据
        LocalDateTime now = LocalDateTime.now();// 获取当前时间
        Long currentId = BaseContext.getCurrentId(); // 获取当前用户线程的ID

        // 4. 根据当前不同的操作类型，为对应的属性赋值，通过反射调用实体对象的set方法
        if (operationType == OperationType.INSERT){
            // 为四个公共字段赋值
            try {
                log.info("正在为实体对象{}自动填充createTime、createUser、updateTime、updateUser属性...", entity);
                Method setCreateTime = entity.getClass().  // 获取了 entity 对象的运行时类对象（Runtime Class Object）
                        getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class); // 获取一个名为 "setCreateTime"，参数类型为 LocalDateTime 的方法对象
                // 通过反射，获取名为"setCreateUser"的方法对象
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                // 通过反射，获取名为"setUpdateTime"的方法对象
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                // 通过反射，获取名为"setUpdateUser"的方法对象
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射为实体对象设置四个公共字段的值
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);

            } catch (NoSuchMethodException e) {
                log.error("自动填充失败，实体对象中缺少必要的属性！");
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                log.error("自动填充失败，实体对象中set方法调用失败！");
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                log.error("自动填充失败，实体对象中set方法访问权限受限！");
                throw new RuntimeException(e);
            }
        }else if (operationType == OperationType.UPDATE){
            // 为2个公共字段赋值
            try {
                log.info("正在为实体对象{}自动填充updateTime、updateUser属性...", entity);
                // 通过反射，获取名为"setUpdateTime"的方法对象
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                // 通过反射，获取名为"setUpdateUser"的方法对象
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射为实体对象设置2个公共字段的值
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);

            } catch (NoSuchMethodException e) {
                log.error("自动填充失败，实体对象中缺少必要的属性！");
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                log.error("自动填充失败，实体对象中set方法调用失败！");
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                log.error("自动填充失败，实体对象中set方法访问权限受限！");
                throw new RuntimeException(e);
            }
        }
    }
}
