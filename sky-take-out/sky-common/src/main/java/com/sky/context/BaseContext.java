package com.sky.context;

public class BaseContext {

    /**ThreadLocal 是 Java 中用于创建线程局部变量的类。每个线程都可以独立地访问其线程局部变量，而无需担心线程的干扰。
     * 在这里，threadLocal 变量被声明为存储 Long 类型的值。通过使用 ThreadLocal，可以在每个线程中存储一个独立的 Long 值，而不会影响其他线程的值。
     * 通常情况下，将这样的 ThreadLocal 变量声明为 private static final，并提供相应的静态方法来访问和操作它，保证线程安全。*/
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    // 将当前线程的ID设置为指定的值
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    // 获取当前线程的ID
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    // 清除当前线程的ID
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
