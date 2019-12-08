package com.swe681.checkers.dao;

import com.swe681.checkers.model.Token;
import com.swe681.checkers.model.game.checkers.Piece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TokenDaoImpl implements TokenDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int insertToken(String username, String token) {
        Instant instant = Instant.now();
        long seconds = TimeUnit.MINUTES.toSeconds( 30 );
        Instant ttl = instant.plusSeconds( seconds );
        int status = jdbcTemplate.update(
                "INSERT INTO sdas22.tokenStore(username, token, ttl) VALUES (?, ?, ?)",
                username, token, ttl.toEpochMilli());
        return status;
    }

    @Override
    public Token fetchlastToken(String username) {
        String sql = "select username, token, ttl " +
                "from sdas22.tokenStore where username=? " +
                "order by username desc limit 1";
        List<Token> token;
        try{
            token =  jdbcTemplate.query(
                    sql, new TokenRowMapper(), username);
        } catch (EmptyResultDataAccessException emptyException) {
            System.out.println("No token exists for the user yet");
            token = null;
        }
        return token == null? null : token.get(0);
    }

    class TokenRowMapper implements RowMapper<Token> {

        public Token mapRow(ResultSet resultSet, int i) throws SQLException {
            Token token = new Token();
            token.setUsername(resultSet.getString("username"));
            token.setToken(resultSet.getString("token"));
            token.setTtl(resultSet.getLong("ttl"));
            return token;
        }
    }
}
