package com.ocellus.platform.dao;


import com.ocellus.platform.model.GroupUserMapping;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupUserMappingDAO extends BaseDAO<GroupUserMapping, String> {
    int deleteByGroupId(String groupId);

    int deleteByUserId(String userId);
}
