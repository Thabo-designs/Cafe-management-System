package com.jaws.cafe.cafe.management.system.JWT;

import com.jaws.cafe.cafe.management.system.dao.UserDao;
import com.jaws.cafe.cafe.management.system.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserDao userDao;

    private User user;

    public CustomUserDetailsService(
            UserDao repository) {
        this.userDao = repository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String username) {

        user = userDao.findByEmailId(username);

        return new CustomUserDetails(user);
    }

    public User getUser(){
        return user;
    }
}
