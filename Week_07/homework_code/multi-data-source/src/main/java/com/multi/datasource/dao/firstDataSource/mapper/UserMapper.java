package com.multi.datasource.dao.firstDataSource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.multi.datasource.dao.dto.UserDO;
import com.multi.datasource.dao.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    @Select("INSERT INTO user_info(name, mobile) VALUES (#{name}, #{mobile})")
    void save(UserVO userVO);
}
