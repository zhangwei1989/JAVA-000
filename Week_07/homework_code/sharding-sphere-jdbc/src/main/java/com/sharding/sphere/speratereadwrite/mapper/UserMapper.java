package com.sharding.sphere.speratereadwrite.mapper;

import com.sharding.sphere.speratereadwrite.entity.UserDO;
import com.sharding.sphere.speratereadwrite.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    void save(UserVO userVO);

    List<UserDO> findAll();
}
