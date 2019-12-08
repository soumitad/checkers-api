package com.swe681.checkers.service;


import com.swe681.checkers.dao.UserDao;
import com.swe681.checkers.model.User;
import com.swe681.checkers.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//this implements the interface
@Service
public class UserServiceImpl implements UserService {
    String SALT = "hash-password";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenService tokenService;

    @Override
    public User getUserInfo(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User login(User user) {
        String password = user.getPassword();
        String userName = user.getUserName();
        String saltedPassword = SALT + password;
        String hashedPassword = generateHash(saltedPassword);

        String storedPass = userDao.getPassword(userName);
        String token = null;
        if(storedPass.equals(hashedPassword)) {
            //User is a valid user, generate a new token
            token = tokenService.generateNewToken(userName);
            user.setToken(token);
            user.setPassword("");
        } else {
            user = null;
        }
        return user;
    }

    @Override
    public User register(User user) {
        String password = user.getPassword();
        String userName = user.getUserName();
        String saltedPassword = SALT + password;
        String hashedPassword = generateHash(saltedPassword);
        user.setPassword(hashedPassword);
        int status = userDao.registerUser(user);
        if (status == 1) {
            user.setPassword("");
        } else {
            user = null;
        }
        return user;
    }

    private static String generateHash(String input) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length; ++idx) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            // handle error here.
        }

        return hash.toString();
    }
}
