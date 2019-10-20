package com.soumita.checkersapi.service;


import com.soumita.checkersapi.model.User;
import com.soumita.checkersapi.repository.UserRepository;
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
