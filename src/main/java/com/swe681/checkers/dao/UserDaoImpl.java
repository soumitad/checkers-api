package com.swe681.checkers.dao;

import com.swe681.checkers.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getPassword(String username) {
        String sql = "select password from sdas22.user where username=?";
        String storedPass="";
        try{
            storedPass =  jdbcTemplate.queryForObject(
                    sql, new Object[]{username}, String.class);
        } catch (EmptyResultDataAccessException emptyException) {
            System.out.println("User doesnt exist");
            storedPass = null;
        }
        return storedPass;
    }

    @Override
    public int registerUser(User user) {
        int status = jdbcTemplate.update(
                "INSERT INTO sdas22.user(username, firstname, lastname, password) " +
                        "VALUES (?, ?, ?, ?)",
                user.getUserName(), user.getFirstName(), user.getLastName(), user.getPassword());
        return status;
    }
}
