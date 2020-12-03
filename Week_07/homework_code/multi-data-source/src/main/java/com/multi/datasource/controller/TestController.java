package com.multi.datasource.controller;

import com.multi.datasource.dao.dto.UserDO;
import com.multi.datasource.dao.vo.UserVO;
import com.multi.datasource.service.UserService;
import com.zctj.web.controller.base.BaseController;
import com.zctj.web.controller.base.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName TestController
 * @Description Test
 * @Author zhangwei
 * @Date 2020-12-03 13:05
 * @Version 1.0
 */
@RestController
@Slf4j
public class TestController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("/testdb1")
    public RestResponse testdb1() {
        List<UserDO> userDOList = userService.findAll();
        System.out.println(userDOList);

        return successResponse(userDOList);
    }

    @GetMapping("/testdb0")
    public RestResponse testdb0(@RequestParam String name, @RequestParam String mobile) {
        UserVO userVO = new UserVO();
        userVO.setName(name);
        userVO.setMobile(mobile);
        userService.save(userVO);

        return successResponse();
    }
}
