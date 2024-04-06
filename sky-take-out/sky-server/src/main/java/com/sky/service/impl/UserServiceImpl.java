package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * ClassName: UserServiceImpl
 * Package: com.sky.service.impl
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/6 下午8:26
 * @Version 1.0
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    // 微信服务接口地址
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * 微信登陆
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 调用微信接口服务，获得当前微信用户的openId
        String openid = getOpenid(userLoginDTO.getCode());

        // 判断openId是否为空，如果为空，则登录失败，抛出业务异常
        if (openid == null) {
            log.error("微信登录失败，openId为空");
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 判断openId是否存在，如果不存在，则新增用户，并返回用户信息
        User user = userMapper.getByUserId(openid);
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            // 新增用户
            userMapper.insert(user);
        }
        // 如果openId存在，则直接返回用户信息

        // 返回用户对象
        return user;
    }

    private String getOpenid(String code) {
        // 调用微信接口服务，获得当前微信用户的openId
        HashMap<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");

        String json = HttpClientUtil.doGet(WX_LOGIN_URL, map);

        // 解析json数据，获得openId
        JSONObject jsonObject = JSON.parseObject(json);

        return jsonObject.getString("openid");
    }


}
