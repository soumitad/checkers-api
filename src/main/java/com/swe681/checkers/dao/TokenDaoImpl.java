package com.swe681.checkers.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
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
                "INSERT INTO sdas22.tokenStore VALUES (?, ?, ?)",
                username, token, ttl.toEpochMilli());
        return status;
    }
}
