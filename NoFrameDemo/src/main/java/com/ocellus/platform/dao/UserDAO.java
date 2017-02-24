package com.ocellus.platform.dao;

import com.ocellus.platform.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO extends BaseDAO<User, String> {
    public User getByUserName(String userName);

    List<User> getGroupUsers(String groupId);

    List<User> searchDuplicated(User user);

    public void edit(User user);
}
