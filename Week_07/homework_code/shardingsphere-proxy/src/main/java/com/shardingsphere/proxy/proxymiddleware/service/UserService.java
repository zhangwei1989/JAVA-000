package com.shardingsphere.proxy.proxymiddleware.service;

import com.shardingsphere.proxy.proxymiddleware.dao.dto.UserDO;
import com.shardingsphere.proxy.proxymiddleware.dao.vo.UserVO;

import java.util.List;

public interface UserService {

    List<UserDO> findAll();

    void save(UserVO userVO);
}
