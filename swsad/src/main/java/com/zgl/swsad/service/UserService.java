package com.zgl.swsad.service;


import com.zgl.swsad.mapper.UserMapper;
import com.zgl.swsad.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    // 获取单个用户信息
    public Person selectUser(Long id) {
        return userMapper.selectUser(id);
    }

    public Person selectUserByname(String name) {
        return userMapper.selectUserByName(name);
    }

    //获取所有的用户信息
    public ArrayList<Person> selectAllUser() {
        return userMapper.selectAllUser();
    }

    //创建用户
    public int insertUser(Person person){
        return userMapper.insertUser(person);
    }

    //修改用户
    public int updateUser(Person person){
        return userMapper.updateUser(person);
    }

    //删除用户
    public int deleteUser(int id){
        return userMapper.deleteUser(id);
    }
}
