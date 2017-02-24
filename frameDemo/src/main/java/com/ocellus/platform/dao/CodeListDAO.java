package com.ocellus.platform.dao;

import com.ocellus.platform.model.CodeList;
import com.ocellus.platform.model.NativeSQL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeListDAO {
    public List<CodeList> getCodeList(NativeSQL sql);

}
