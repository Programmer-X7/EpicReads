package com.ardent.epicreads.dao;

import com.ardent.epicreads.entity.User;

public interface UserDao {

    User findByUserName(String userName);

    void save(User theUser);
}
