package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/findPage")
    public Page<User> findPage(String name, int pageIndex, int pageSize) {
        return userService.findPage(name, pageIndex, pageSize);
    }

    @RequestMapping("/findRolePage")
    public Page<User> findRolePage(String name, int pageIndex, int pageSize) {
        return userService.findRolePage(name, pageIndex, pageSize);
    }
}
