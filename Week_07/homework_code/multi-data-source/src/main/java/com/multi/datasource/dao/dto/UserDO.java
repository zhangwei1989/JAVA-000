package com.multi.datasource.dao.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName UserDO
 * @Description UserDO
 * @Author zhangwei
 * @Date 2020-12-03 12:56
 * @Version 1.0
 */
@Data
@TableName("user_info")
public class UserDO {

    private Integer id;

    private String name;

    private String mobile;
}
