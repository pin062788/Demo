package com.ocellus.platform.service;

import com.ocellus.platform.dao.RestrictTableDAO;
import com.ocellus.platform.model.RestrictTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestrictTableService extends AbstractService<RestrictTable, String> {

    @Autowired
    private RestrictService restrictService;

    private RestrictTableDAO restrictTableDAO;

    @Autowired
    public void setRestrictTableDAO(RestrictTableDAO restrictTableDAO) {
        super.setDao(restrictTableDAO);
        this.restrictTableDAO = restrictTableDAO;
    }

    public List<String> selectAllUserTable() {
        return restrictTableDAO.selectAllUserTable();
    }

    public RestrictTable save(RestrictTable restrictTable) throws Exception {
        if ("".equals(restrictTable.getTableId())) {
            insert(restrictTable);
        } else {
            update(restrictTable);
        }
        return restrictTable;
    }

    public boolean isDuplicated(RestrictTable restrictTable) {
        List<RestrictTable> tables = restrictTableDAO.searchDuplicated(restrictTable);
        return !tables.isEmpty();
    }

    public void batchDelete(String tableIds) {
        String[] tableIdArray = tableIds.split(",");
        Map<String, String> params = new HashMap<String, String>();
        for (String tableId : tableIdArray) {
            RestrictTable table = restrictTableDAO.getById(tableId);
            params.clear();
            params.put("tableName", table.getTableName());
            restrictService.deleteBatch(params);
            restrictTableDAO.delete(tableId);
        }
    }
}
