package com.bytter.biz.expense.vo;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
/**
 * @Title: Entity
 * @Description: 操作类型表
 * @author onlineGenerator
 * @date 2017-06-01 14:42:30
 * @version 1.0
 *
 */
public class NisOperationTypeVO implements Serializable {
    /**操作类型ID*/
    @Dict(tableName="NIS_OPERATION_TYPE",valueFieldName="id",showFiledName="OP_TYPE_NAME",inject="opTypeName")
    private String id;
    /**操作类型编码*/
    private String opType;
    /**操作类型名称*/
    private String opTypeName;
    /**业务类型编码*/
    private String busType;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getOpType() {
        return opType;
    }
    public void setOpType(String opType) {
        this.opType = opType;
    }
    public String getOpTypeName() {
        return opTypeName;
    }
    public void setOpTypeName(String opTypeName) {
        this.opTypeName = opTypeName;
    }
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
}
