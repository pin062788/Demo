package com.ocellus.platform.dao;

import com.ocellus.platform.model.RestrictColumn;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestrictColumnDAO extends BaseDAO<RestrictColumn, String> {
    List<String> selectTableColumns(String tableId);

    List<RestrictColumn> searchDuplicated(RestrictColumn restrictColumn);

}
