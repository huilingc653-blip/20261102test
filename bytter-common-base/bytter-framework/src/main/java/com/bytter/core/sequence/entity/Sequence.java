package com.bytter.core.sequence.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 流水号实体
 *
 * @author zhoul
 * @date 2017/5/27
 */
@Entity
@Table(name = "SYS_ID")
public class Sequence extends IdEntity implements Serializable {
    private String tableName;
    private String fieldName;
    private String specialCode;
    private String currentSequence;
    @Column(name = "TABLE_NAME")
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    @Column(name = "FIELD_NAME")
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    @Column(name = "CURRENT_SEQUENCE")
    public String getCurrentSequence() {
        return currentSequence;
    }
    public void setCurrentSequence(String currentSequence) {
        this.currentSequence = currentSequence;
    }
    @Column(name = "SPECIAL_CODE")
    public String getSpecialCode() {
        return specialCode;
    }
    public void setSpecialCode(String specialCode) {
        this.specialCode = specialCode;
    }
}
