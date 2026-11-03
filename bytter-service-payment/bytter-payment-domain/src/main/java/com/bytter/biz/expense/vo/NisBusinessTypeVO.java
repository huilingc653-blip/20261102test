package com.bytter.biz.expense.vo;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
/**
 * @Title: Entity
 * @Description: 业务类型表
 * @author onlineGenerator
 * @date 2017-06-01 14:42:30
 * @version 1.0
 *
 */
public class NisBusinessTypeVO implements Serializable {
    /**业务类型ID*/
    @Dict(tableName="NIS_BUSINESS_TYPE",valueFieldName="id",showFiledName="BUS_TYPE_NAME",inject="busTypeName")
    private String id;
    /**业务类型编码*/
    private String busType;
    /**业务类型名称*/
    private String busTypeName;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    public String getBusTypeName() {
        return busTypeName;
    }
    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
    }
}
