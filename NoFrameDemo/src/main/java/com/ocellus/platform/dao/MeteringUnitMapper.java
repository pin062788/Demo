package com.ocellus.platform.dao;

import com.ocellus.platform.model.MeteringUnit;


public interface MeteringUnitMapper extends BaseDAO<MeteringUnit, String> {

    MeteringUnit getByNcPK(String ncPk);
}
