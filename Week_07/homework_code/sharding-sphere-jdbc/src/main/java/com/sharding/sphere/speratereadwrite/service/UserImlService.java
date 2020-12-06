package com.sharding.sphere.speratereadwrite.service;

import com.sharding.sphere.speratereadwrite.entity.UserDO;
import com.sharding.sphere.speratereadwrite.mapper.UserMapper;
import com.sharding.sphere.speratereadwrite.vo.UserVO;
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
