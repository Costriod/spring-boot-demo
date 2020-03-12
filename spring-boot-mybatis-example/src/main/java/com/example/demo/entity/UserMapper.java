package com.example.demo.entity;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectByName(String name);

    List<User> selectByRole(String role);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}