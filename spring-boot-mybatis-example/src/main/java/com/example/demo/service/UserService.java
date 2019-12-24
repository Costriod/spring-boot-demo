package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public Page<User> findPage(String name, int pageIndex, int pageSize) {
        //PageHelper.startPage(pageIndex, pageSize, true)必须紧挨着下一句的mapper查询才会自动分页查询以及自动count
        PageHelper.startPage(pageIndex, pageSize, true);
        //紧挨着PageHelper.startPage(pageIndex, pageSize, true)的查询返回的就是Page对象，否则就是普通的ArrayList
        return (Page<User>) userMapper.selectByName(name);
    }
}
