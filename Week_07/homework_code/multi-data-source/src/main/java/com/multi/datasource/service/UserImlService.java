package com.multi.datasource.service;

import com.multi.datasource.dao.dto.UserDO;
import com.multi.datasource.dao.firstDataSource.mapper.UserMapper;
import com.multi.datasource.dao.secondDataSource.mapper.SecondUserMapper;
import com.multi.datasource.dao.vo.UserVO;
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

    @Autowired
    private SecondUserMapper secondUserMapper;

    @Override
    public List<UserDO> findAll() {
        List<UserDO> userInfos = secondUserMapper.findAll();

        return userInfos;
    }

    @Override
    public void save(UserVO userVO) {
        userMapper.save(userVO);
    }
}
