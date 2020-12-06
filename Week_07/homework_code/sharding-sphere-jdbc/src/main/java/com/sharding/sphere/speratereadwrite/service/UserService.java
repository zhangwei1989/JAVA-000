package com.sharding.sphere.speratereadwrite.service;

import com.sharding.sphere.speratereadwrite.entity.UserDO;
import com.sharding.sphere.speratereadwrite.vo.UserVO;

import java.util.List;

public interface UserService {

    List<UserDO> findAll();

    void save(UserVO userVO);
}
