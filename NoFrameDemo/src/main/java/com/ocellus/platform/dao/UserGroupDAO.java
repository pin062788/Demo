package com.ocellus.platform.dao;

import com.ocellus.platform.model.UserGroup;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupDAO extends BaseDAO<UserGroup, String> {
    int deleteById(String id);

    List<UserGroup> searchDuplicated(UserGroup userGroup);

    List<UserGroup> getUserGroups(String userId);
}
