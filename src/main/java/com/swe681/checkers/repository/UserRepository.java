package com.swe681.checkers.repository;

import com.swe681.checkers.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long > {

     @Query("select username, firstname, lastname, address, city, gender from user where username=?")
     User findByUserName(String userName);
}
