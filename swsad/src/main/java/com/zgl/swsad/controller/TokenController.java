package com.zgl.swsad.controller;

import com.zgl.swsad.authorization.annotation.Authorization;
import com.zgl.swsad.authorization.annotation.CurrentUser;
import com.zgl.swsad.authorization.manager.TokenManager;
import com.zgl.swsad.authorization.model.TokenModel;
import com.zgl.swsad.model.Person;
import com.zgl.swsad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    @Autowired
    private UserService userRepository;

    @Autowired
    private TokenManager tokenManager;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> login(@RequestParam String username, @RequestParam String password) {
        Assert.notNull(username, "username can not be empty");
        Assert.notNull(password, "password can not be empty");

        Person user = userRepository.selectUserByname(username);
        //生成一个token，保存用户登录状态
        TokenModel model = tokenManager.createToken(user.getId());
        return new ResponseEntity(model, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @Authorization
    public ResponseEntity<Object> logout(@CurrentUser Person user) {
        tokenManager.deleteToken(user.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

}

