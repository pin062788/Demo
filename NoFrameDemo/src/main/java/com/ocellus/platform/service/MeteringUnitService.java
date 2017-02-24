package com.ocellus.platform.service;

import com.ocellus.platform.dao.MeteringUnitMapper;
import com.ocellus.platform.model.MeteringUnit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("meteringUnitService")
public class MeteringUnitService extends AbstractService<MeteringUnit, String> {
    private static Logger log = Logger.getLogger(MeteringUnitService.class);

    @Autowired
    private MeteringUnitMapper meteringUnitMapper;


    public void update(MeteringUnit meteringUnit) {

        meteringUnitMapper.update(meteringUnit);
    }

    public MeteringUnit getById(String id) {
        return meteringUnitMapper.getById(id);
    }

    public MeteringUnit getByNcPk(String ncPk) {
        return meteringUnitMapper.getByNcPK(ncPk);
    }
}
