package com.swe681.checkers.service;

import com.swe681.checkers.dao.TokenDao;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenDao tokenDao;
    @Override
    public String generateNewToken(String username) {
        String generatedString = RandomStringUtils.randomAlphanumeric(30);
        int status = tokenDao.insertToken(username, generatedString);
        return generatedString;
    }
}
