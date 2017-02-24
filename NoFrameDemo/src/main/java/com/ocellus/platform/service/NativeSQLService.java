package com.ocellus.platform.service;

import com.ocellus.platform.dao.NativeSQLDAO;
import com.ocellus.platform.model.NativeSQL;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class NativeSQLService {
    private static final Logger logger = Logger.getLogger(NativeSQLService.class);
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    private NativeSQLDAO dao;

    public List<Map> execute(NativeSQL sql) {
        return dao.execute(sql);
    }

    public Integer getSequence(String seqName) {
        Integer currVal = null;
        lock.lock();
        try {
            String sql = "select * from TB_SEQUENCE where SEQ_NAME = '" + seqName + "'";
            NativeSQL nsql = new NativeSQL();
            nsql.setSql(sql);

            List<Map> seqs = dao.execute(nsql);
            if (seqs == null || seqs.size() == 0) {
                sql = "insert into TB_SEQUENCE (SEQ_NAME,NEXT_VALUE) values('" + seqName + "',2)";
                currVal = 1;
            } else {
                Map seq = seqs.iterator().next();
                currVal = seq.get("NEXT_VALUE") == null ? 1 : (Integer) seq.get("NEXT_VALUE");
                Integer incVal = seq.get("INCREMENT_VALUE") == null ? 1 : (Integer) seq.get("INCREMENT_VALUE");
                Integer nextVal = currVal + incVal;
                sql = "update TB_SEQUENCE set NEXT_VALUE = " + nextVal + " where SEQ_NAME = '" + seqName + "'";
                ;
            }
            nsql.setSql(sql);
            dao.execute(nsql);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            lock.unlock();
        }
        return currVal;
    }
}
