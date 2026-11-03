package com.bytter.core.sequence.interfaces;
import com.bytter.core.sequence.entity.MultiTable;
/**
 * Created by zhoul on 2017/5/31.
 */
public interface ISequenceService {
    String getSequence(MultiTable[] multiTables, String businessPrefix);
    String getSequence(String tableName, String fieldName, String businessPrefix);
    String getSequenceByTableNameAndFieldName(String tableName, String fieldName, String specialCode);
    String getSequenceFromTable(String tableName, String fieldName, String prefix, String specialCode);
    void updateCurrentSequence(String table, String fieldName, String currentSequence, String specialCode, Boolean isNew);
}
