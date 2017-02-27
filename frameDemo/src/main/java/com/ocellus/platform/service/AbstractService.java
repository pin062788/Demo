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

    public void insert(T model)throws Exception {
        model.setDBId(StringUtil.getGUID());
        Date now = new Date();
        String userLogin = WebUtil.getLoginUserName();
        model.setAddDate(now);
        model.setAddUser(userLogin);
        model.setEditDate(now);
        model.setEditUser(userLogin);
        if(beforeInsert(model)){
            dao.insert(model);
            afterInsert(model);
        }
    }
    public boolean beforeInsert(T vo) throws Exception {return true;}
    public void afterInsert(T model) throws Exception {}

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

    public T save(T vo) throws Exception {
        boolean canNext = beforeSave(vo);
        if(canNext){
            if (!"0".equals(vo.getDBId())&&!StringUtil.isEmpty(vo.getDBId())) {//mysql 主键 int 初始化就是0
                update(vo);
            } else {
                insert(vo);
            }
            afterSave(vo);
        }
        return vo;
    }
    public boolean beforeSave(T vo) throws Exception {return true;}
    public void afterSave(T vo) throws Exception {}

    public List<T> selectAll() {
        return dao.search(null);
    }
}
