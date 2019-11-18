package com.swe681.checkers.dao;

import com.swe681.checkers.model.game.checkers.GameInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class GameDaoImpl implements GameDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public GameInfo createGame() {
        return null;
    }
}
