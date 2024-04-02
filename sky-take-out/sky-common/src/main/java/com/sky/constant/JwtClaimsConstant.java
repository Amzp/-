package com.sky.constant;

/**
 * 这段代码展示了一个名为 JwtClaimsConstant 的类，该类包含了用于JWT声明的常量字符串。
 * 这些常量字符串用于定义JWT中的声明名称，以便在代码中使用这些常量而不是直接使用字符串。
 *
 * 例如，常量 EMP_ID 的值为 "empId"，而不是在代码中直接使用字符串 "empId"。这种做法的好处包括：
 *      可维护性：通过将声明名称定义为常量，可以在整个代码库中统一使用，从而提高了代码的可维护性。
 *      避免错误：使用常量可以避免因为拼写错误或者遗漏而导致的问题。
 *     易于修改：如果将来需要修改声明名称，可以简单地在一个位置更新常量的值，而不必在整个代码库中手动寻找和更新。
 */
public class JwtClaimsConstant {

    public static final String EMP_ID = "empId";
    public static final String USER_ID = "userId";
    public static final String PHONE = "phone";
    public static final String USERNAME = "username";
    public static final String NAME = "name";

}
