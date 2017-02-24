package com.ocellus.platform.service;

import com.ocellus.platform.dao.CodeListDAO;
import com.ocellus.platform.model.CodeList;
import com.ocellus.platform.model.NativeSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeListService {
    @Autowired
    private CodeListDAO codeListDAO;

    public List<CodeList> getCodeList(String sql) {
        return codeListDAO.getCodeList(new NativeSQL(sql));
    }

    public List<CodeList> getCodeList(NativeSQL sql) {
        return codeListDAO.getCodeList(sql);
    }

}
