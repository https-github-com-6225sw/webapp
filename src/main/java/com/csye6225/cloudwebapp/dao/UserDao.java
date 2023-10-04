package com.csye6225.cloudwebapp.dao;

import com.csye6225.cloudwebapp.entity.User;


import java.util.List;

public interface UserDao {
    void save(User theUser);

    List<User> findByEmail(String email);

}
