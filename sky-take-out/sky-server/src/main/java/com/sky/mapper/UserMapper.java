package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * ClassName: UserMapper
 * Package: com.sky.mapper
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/6 下午8:43
 * @Version 1.0
 */
@Mapper
public interface UserMapper {
    /**
     * 根据openId查询用户信息
     * @param openid
     * @return
     */
    @Select("select * from sky_take_out.user where openid = #{openId}")
    User getByUserId(String openid);

    /**
     * 新增用户信息（需要主键回显）
     * @param user
     */
    void insert(User user);
}
