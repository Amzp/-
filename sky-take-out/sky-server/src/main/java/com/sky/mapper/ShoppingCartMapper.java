package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * ClassName: ShoppingCartMapper
 * Package: com.sky.mapper
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/7 下午3:53
 * @Version 1.0
 */
@Mapper
public interface ShoppingCartMapper {

    /**
     * 根据动态条件查询购物车列表
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据id，更新购物车商品数量
     * @param cart
     */
    @Update("update sky_take_out.shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart cart);

    /**
     * 插入购物车商品
     * @param shoppingCart
     */
    @Insert("insert into sky_take_out.shopping_cart " +
            "(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "values (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户id删除购物车商品--清空该用户的购物车
     * @param userId
     */
    @Delete("delete from sky_take_out.shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);


    /**
     * 根据条件删除购物车商品
     * @param id
     */
    @Delete("delete from sky_take_out.shopping_cart where id = #{id}")
    void deleteById(Long id);

    /**
     * 将购物车对象批量添加到数据库
     * @param shoppingCartList
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
