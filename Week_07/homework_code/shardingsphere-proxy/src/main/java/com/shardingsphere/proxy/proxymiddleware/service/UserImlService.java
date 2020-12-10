package com.shardingsphere.proxy.proxymiddleware.service;

import com.shardingsphere.proxy.proxymiddleware.dao.dto.UserDO;
import com.shardingsphere.proxy.proxymiddleware.dao.mapper.UserMapper;
import com.shardingsphere.proxy.proxymiddleware.dao.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserImlService
 * @Description user service
 * @Author zhangwei
 * @Date 2020-12-03 13:02
 * @Version 1.0
 */
@Service
@Slf4j
public class UserImlService implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserDO> findAll() {
        List<UserDO> userInfos = userMapper.findAll();

        return userInfos;
    }

    @Override
    public void save(UserVO userVO) {
        userMapper.save(userVO);
    }
}
