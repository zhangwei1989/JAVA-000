package com.multi.datasource.dao.thirdDataSource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.multi.datasource.dao.dto.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ThirdUserMapper extends BaseMapper<UserDO> {

    @Select("SELECT * FROM user_info")
    List<UserDO> findAll();
}
