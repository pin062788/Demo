package com.ocellus.platform.service;

import com.ocellus.platform.dao.UserDAO;
import com.ocellus.platform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexConfigureService extends AbstractService<User, String> {
    @Autowired
    private UserDAO userDAO;

    public void edit(User user) {
        userDAO.edit(user);
    }

    public User getById(String userId) {
        return userDAO.getById(userId);
    }
}
