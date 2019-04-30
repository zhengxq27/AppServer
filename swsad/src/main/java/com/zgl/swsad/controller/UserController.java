package com.zgl.swsad.controller;

import com.zgl.swsad.model.Person;
import com.zgl.swsad.service.UserService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
//@Controller
//@ResponseBody
//restController返回的不是页面,Controller返回的是页面  restController=Controller + ResponseBody
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/Users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> queryUser (@PathVariable int id){
        Person p = userService.selectUser(id);
        if(p == null)
        {
            //这里我们可以自己单独写一个异常类来代替这个p
            return new ResponseEntity(p , HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(p, HttpStatus.OK);
    }

    @RequestMapping(value = "/Users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object selectAllUser(){
        return userService.selectAllUser();
    }

    //https://blog.csdn.net/justry_deng/article/details/80972817，注意requestParm注解的使用方法不同
    @RequestMapping(value = "/Users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object createUser (@RequestBody Person person){
        return userService.insertUser(person);
    }

    @RequestMapping(value = "/Users/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object modifyUser (@RequestBody Person person){
        return userService.updateUser(person);
    }


    @RequestMapping(value = "/Users/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object deleteUser (@PathVariable int id){
        return userService.deleteUser(id);
    }

}
