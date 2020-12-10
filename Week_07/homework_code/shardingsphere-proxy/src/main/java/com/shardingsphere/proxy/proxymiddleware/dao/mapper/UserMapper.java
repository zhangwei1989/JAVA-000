package com.shardingsphere.proxy.proxymiddleware.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shardingsphere.proxy.proxymiddleware.dao.dto.UserDO;
import com.shardingsphere.proxy.proxymiddleware.dao.vo.UserVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    @Insert("INSERT INTO user_info(name, mobile) VALUES (#{name}, #{mobile})")
    void save(UserVO userVO);

    @Select("SELECT * FROM user_info")
    List<UserDO> findAll();
}
