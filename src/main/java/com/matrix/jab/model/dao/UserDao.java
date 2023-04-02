package com.matrix.jab.model.dao;

import com.matrix.jab.model.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long> {
    User findFirstByUserName(String userName);
}
