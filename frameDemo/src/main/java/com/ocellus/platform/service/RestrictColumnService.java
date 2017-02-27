package com.ocellus.platform.service;

import com.ocellus.platform.dao.RestrictColumnDAO;
import com.ocellus.platform.model.RestrictColumn;
import com.ocellus.platform.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestrictColumnService extends AbstractService<RestrictColumn, String> {
    private RestrictColumnDAO restrictColumnDAO;
    @Autowired
    private RestrictService restrictService;

    @Autowired
    public void setRestrictColumnDAO(RestrictColumnDAO restrictColumnDAO) {
        super.setDao(restrictColumnDAO);
        this.restrictColumnDAO = restrictColumnDAO;
    }

    public RestrictColumn save(RestrictColumn restrictColumn) throws Exception {
        if (StringUtil.isEmpty(restrictColumn.getColumnId())) {
            insert(restrictColumn);
        } else {
            update(restrictColumn);
        }
        return restrictColumn;
    }

    public List<String> selectTableColumns(String tableId) {
        return restrictColumnDAO.selectTableColumns(tableId);
    }

    public boolean isDuplicated(RestrictColumn restrictColumn) {
        List<RestrictColumn> column = restrictColumnDAO.searchDuplicated(restrictColumn);
        return !column.isEmpty();
    }

    public void batchDelete(String columnIds) {
        String[] columnIdArray = columnIds.split(",");
        Map<String, String> params = new HashMap<String, String>();
        for (String columnId : columnIdArray) {
            RestrictColumn column = restrictColumnDAO.getById(columnId);
            params.clear();
            params.put("columnName", column.getColumnName());
            params.put("tableName", column.getTableName());
            restrictService.deleteBatch(params);
            restrictColumnDAO.delete(columnId);
        }
    }
}
