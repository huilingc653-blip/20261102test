package com.bytter.biz.erp.controller;


import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/erp/user")
public class UserErpController {


    @DubboReference(check = false)
    private IUserService userService;

    @PostMapping("/save")
    public void save(@RequestBody List<UserDTO> userList) {
        userService.save(userList);
    }

    @PostMapping("/update")
    public void update(@RequestBody List<UserDTO> userList) {
        userService.update(userList);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody List<UserDTO> userList) {
        userService.delete(userList);
    }

    @PostMapping("/todoInfo")
    public List<Map<String,Object>> todoInfo(@RequestBody List<UserDTO> userList) {
        return userService.todoInfo(userList);
    }
}
