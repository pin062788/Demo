package com.ocellus.platform.dao;

import com.ocellus.platform.model.NativeSQL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface NativeSQLDAO {
    public List<Map> execute(NativeSQL sql);
}
