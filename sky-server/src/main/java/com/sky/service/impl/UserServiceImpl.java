package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    // 微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    public User wxlogin(UserLoginDTO userLoginDTO) {
        // 调用微信服务器接口 获取openid
        String openid = getOpenId(userLoginDTO.getCode());
        log.info("openid:{}", openid);
        // 判断openid是否为空 如果为空抛出业务异常
        if (openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 判断用户是否是新用户(当前用户的openid有没有在user表中) 如果是新用户 则新增用户表(自动完成注册)
        User user = userMapper.selectByOpenId(openid);
        if(user==null){
            user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
            userMapper.insertUser(user);
        }
        // 返回User对象
        return user;
    }

    /**
     * 调用微信接口服务 获取openid
     * @param code
     * @return
     */
    private String getOpenId(String code){
        HashMap<String,String> params = new HashMap<>();
        params.put("appid",weChatProperties.getAppid());
        params.put("secret",weChatProperties.getSecret());
        params.put("js_code",code);
        params.put("grant_type","authorization_code");
        String message = HttpClientUtil.doGet(WX_LOGIN, params);
        JSONObject jsonObject = JSON.parseObject(message);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
