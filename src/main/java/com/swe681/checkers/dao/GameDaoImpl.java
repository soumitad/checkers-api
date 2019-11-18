package com.swe681.checkers.dao;

import com.swe681.checkers.model.game.checkers.GameInfo;
import com.swe681.checkers.model.request.GameRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import static java.time.LocalTime.now;

@Service
public class GameDaoImpl implements GameDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int createGame(GameRequest gameRequest) {
        //Get the last id inserted into game table
        // "select "
        Integer lastGameplayId = fetchLastGameId();
        int status = jdbcTemplate.update(
                "INSERT INTO sdas22.game VALUES (?, ?, ?, ?, ?, ?)",
                lastGameplayId + 1, gameRequest.getPlayer1(), gameRequest.getPlayer2(),
                "InProgress",new java.sql.Timestamp(new java.util.Date().getTime()) , null);
        if (status == 1) {
            return lastGameplayId + 1;
        } else {
            return 0;
        }

    }

    private Integer fetchLastGameId() {
        Integer id;
        String sql = "select gameId from sdas22.game order by gameId desc limit 1";
        try{
            id =  jdbcTemplate.queryForObject(
                    sql, new Object[]{}, Integer.class);
        } catch (EmptyResultDataAccessException emptyException) {
            System.out.println("No games exist, lets default to one!!");
            id = new Integer(100);
        }
        return id;
    }
}
