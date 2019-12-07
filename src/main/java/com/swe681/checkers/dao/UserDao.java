package com.swe681.checkers.dao;

import com.swe681.checkers.model.User;

public interface UserDao {

    public String getPassword(String hashPassword);
    public int registerUser(User user);
}
