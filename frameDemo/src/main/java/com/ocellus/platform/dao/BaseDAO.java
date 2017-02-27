package com.ocellus.platform.dao;

import com.ocellus.platform.model.AbstractModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDAO<T extends AbstractModel, PK extends Serializable> {

    int insert(T model);

    void delete(PK id);

    void update(T model);

    void deleteByIds(List<PK> idList);

    T getById(PK id);

    List<T> search(Map params);
}
