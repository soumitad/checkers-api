package com.swe681.checkers.service;

import com.swe681.checkers.model.User;


public interface UserService {

    User getUserInfo(String userName);
    User login(User user);
    User register(User user);
}
