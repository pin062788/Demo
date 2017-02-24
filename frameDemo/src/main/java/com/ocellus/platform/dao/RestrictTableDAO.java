package com.ocellus.platform.dao;

import com.ocellus.platform.model.RestrictTable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestrictTableDAO extends BaseDAO<RestrictTable, String> {
    List<String> selectAllUserTable();

    List<RestrictTable> searchDuplicated(RestrictTable restrictTable);
}
