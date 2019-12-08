package com.swe681.checkers.dao;

import com.swe681.checkers.model.Token;

public interface TokenDao {
    public int insertToken(String username, String token);
    public Token fetchlastToken(String username);
}
