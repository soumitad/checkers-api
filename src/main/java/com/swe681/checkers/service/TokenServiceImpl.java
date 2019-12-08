package com.swe681.checkers.service;

import com.swe681.checkers.dao.TokenDao;
import com.swe681.checkers.model.Token;
import com.swe681.checkers.util.CheckersUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private CheckersUtil checkersUtil;
    @Override
    public String generateNewToken(String username) {
        Token token = tokenDao.fetchlastToken(username);
        if(checkersUtil.isTokenValid(token.getTtl())) {
            return token.getToken();
        }
        String generatedString = RandomStringUtils.randomAlphanumeric(30);
        int status = tokenDao.insertToken(username, generatedString);
        return generatedString;
    }
}
