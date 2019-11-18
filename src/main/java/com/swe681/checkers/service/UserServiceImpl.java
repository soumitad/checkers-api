package com.swe681.checkers.service;


import com.swe681.checkers.model.User;
import com.swe681.checkers.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//this implements the interface
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserInfo(String userName) {
        return userRepository.findByUserName(userName);
    }
}
