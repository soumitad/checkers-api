package com.swe681.checkers.controller;

import com.swe681.checkers.model.User;
import com.swe681.checkers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/user")
public class UserController { //has a dependcy on userService to get UserOjbects

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserInfo(@PathVariable("userName") String userName) {
        User user = userService.getUserInfo(userName);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
