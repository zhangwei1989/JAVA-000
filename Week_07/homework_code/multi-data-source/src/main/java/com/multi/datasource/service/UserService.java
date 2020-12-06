package com.multi.datasource.service;

import com.multi.datasource.dao.dto.UserDO;
import com.multi.datasource.dao.vo.UserVO;

import java.util.List;

public interface UserService {

    List<UserDO> findAll();

    List<UserDO> findAllFromSlave();

    void save(UserVO userVO);
}
