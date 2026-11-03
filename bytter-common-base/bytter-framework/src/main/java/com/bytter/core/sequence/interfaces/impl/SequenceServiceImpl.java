package com.bytter.core.sequence.interfaces.impl;
import com.bytter.core.sequence.dao.SequenceDao;
import com.bytter.core.sequence.entity.MultiTable;
import com.bytter.core.sequence.interfaces.ISequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 操作数据库获取流水号
 *
 * @author zhoul
 * @date 2017/5/31
 */
@Service("sequenceServiceImpl")
public class SequenceServiceImpl implements ISequenceService {
    @Autowired
    private SequenceDao sequenceDao;
    @Transactional(readOnly = true)
    @Override
    public String getSequence(MultiTable[] multiTables, String businessPrefix) {
        return sequenceDao.getSequence(multiTables, businessPrefix);
    }
    @Transactional(readOnly = true)
    @Override
    public String getSequence(String tableName, String fieldName, String businessPrefix) {
        return sequenceDao.getSequence(tableName, fieldName, businessPrefix);
    }
    @Override
    public String getSequenceByTableNameAndFieldName(String tableName, String fieldName, String specialCode) {
        return sequenceDao.getSequenceByTableNameAndFieldName(tableName, fieldName, specialCode);
    }
    @Override
    public String getSequenceFromTable(String tableName, String fieldName, String prefix, String specialCode) {
        return sequenceDao.getSequenceFromTable(tableName, fieldName, prefix, specialCode);
    }
    @Override
    public void updateCurrentSequence(String table, String fieldName, String currentSequence, String specialCode, Boolean isNew) {
        sequenceDao.updateCurrentSequence(table, fieldName, currentSequence, specialCode, isNew);
    }
}
