package com.ocellus.platform.service;

import com.ocellus.platform.dao.BaseDAO;
import com.ocellus.platform.model.AbstractModel;
import com.ocellus.platform.utils.StringUtil;
import com.ocellus.platform.utils.WebUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AbstractService<T extends AbstractModel, PK extends Serializable> {
    private BaseDAO<T, PK> dao;

    public void setDao(BaseDAO<T, PK> dao) {
        this.dao = dao;
    }

    public int insert(T model) {
        model.setDBId(StringUtil.getGUID());
        Date now = new Date();
        String userLogin = WebUtil.getLoginUserName();
        model.setAddDate(now);
        model.setAddUser(userLogin);
        model.setEditDate(now);
        model.setEditUser(userLogin);
        return dao.insert(model);
    }
    public void afterInsert(T model){}

    public void delete(PK id) {
        dao.delete(id);
    }

    public void deleteByIds(List<PK> idList) {
        dao.deleteByIds(idList);
    }

    public void update(T model) {
        Date now = new Date();
        String userLogin = WebUtil.getLoginUserName();
        model.setAddDate(now);
        model.setAddUser(userLogin);
        dao.update(model);
    }

    public List<T> search(Map params) {
        return dao.search(params);
    }

    public T getById(PK id) {
        return dao.getById(id);
    }

    public T save(T vo) {
        beforeSave(vo);
        if (!StringUtil.isEmpty(vo.getDBId())) {
            update(vo);
        } else {
            insert(vo);
        }
        afterSave(vo);
        return vo;
    }
    public void afterSave(T vo) {}
    public void beforeSave(T vo) {}
}
