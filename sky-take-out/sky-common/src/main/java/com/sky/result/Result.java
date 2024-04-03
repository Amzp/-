package com.sky.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用的后端统一返回结果类 Result<T>，用于在后端接口中标准化地返回操作结果和数据
 *
 * @param <T>
 */
@Data
/*定义了一个泛型类 Result<T>，其中 <T> 是一个类型参数。Result 类可以在实例化时接受不同类型的数据，并将这些数据存储在 T 类型的字段中。
通过实现 Serializable 接口，这个泛型类可以被序列化，这在很多情况下是非常有用的，比如在网络传输、持久化存储等方面*/
public class Result<T> implements Serializable {

    /*code（编码）、msg（错误信息）和 data（数据）三个字段，用于描述操作结果和携带返回的数据*/
    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; // data 的类型可以是在实例化 Result 对象时指定的任何类型。这使得 Result 类可以在不同上下文中存储不同类型的数据

    /*success() 方法用于返回一个表示成功的结果对象，通常用于无返回数据的成功情况。
    * 这个方法使用了泛型 <T>，允许在创建 Result 对象时指定其内部的数据类型。
    关键点解释：
        static 表示这是一个静态方法，不需要创建 Result 对象，直接调用即可。
               静态方法属于类本身，它与特定实例的状态无关。静态方法可以在不创建类的实例的情况下被调用，它通常用于表示与类相关联的一些通用操作或功能。
        <T> 表示这是一个泛型方法，允许在调用时指定具体的数据类型。
             如果一个静态方法需要使用泛型，那么这个静态方法也必须定义成泛型方法，因为静态方法不可以访问定义在类上的泛型类型参数。
             在Java中，静态方法是与类相关联的，而不是与特定的实例相关联的。由于泛型类型参数是与实例相关联的，在静态方法中是无法访问到类上定义的泛型类型参数的。
             在静态方法中是无法访问到类上定义的泛型类型参数的，除非在静态方法自身的定义中也包含同样的泛型类型参数。 */
    public static <T> Result<T> success() {
        /* Result<T> result = new Result<T>(); 创建了一个泛型的 Result 对象。这种方式允许这个方法可以适用于不同类型的返回值。*/
        Result<T> result = new Result<T>();
        /* result.code = 1; 设置了结果对象的编码为 1，表示成功的状态。*/
        result.code = 1;
        /* return result; 返回表示成功的结果对象。*/
        return result;
    }

    /*success(T object) 方法用于返回表示成功并携带特定数据的结果对象。*/
    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    /*error(String msg) 方法用于返回表示失败并携带错误信息的结果对象。*/
    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
