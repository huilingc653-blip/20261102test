package com.bytter.guarantee.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * 
 * @Title: 合同主信息-合同字段明细表
 * @Package com.bytter.biz.contract.entity
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-4 下午3:22:16
 * @version V1.0
 */
@Entity
@Table(name = "CON_INFO_BUSDTL", schema = "")
@SuppressWarnings("serial")
public class ConInfoBusDtl extends IdEntity implements java.io.Serializable {
    /** 关联ConInfo主信息ID */
    private String mid;
    /** 要素名称 */
    private String name;
    /** 要素值 */
    private String val;
    /** 字段类型 */
    private String fieldType;
    /** 排序序号 */
    private Integer sortNum;
    /**
     * 是否必输：0否1是
     */
    private String requiredSign;
    @Column(name = "MID", nullable = true, length = 50)
    public String getMid() {
        return mid;
    }
    public void setMid(String mid) {
        this.mid = mid;
    }
    @Column(name = "NAME", nullable = true, length = 100)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "VAL", nullable = true, length = 500)
    public String getVal() {
        return val;
    }
    public void setVal(String val) {
        this.val = val;
    }
    @Column(name = "FIELD_TYPE", nullable = true, length = 10)
    public String getFieldType() {
        return fieldType;
    }
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
    @Column(name = "SORT_NUM", nullable = true, length = 6)
    public Integer getSortNum() {
        return sortNum;
    }
    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }
    /**
     * 方法: 取得是否必输：0否1是
     *
     * @return:是否必输：0否1是
     */
    @Column(name = "REQUIRED_SIGN", nullable = true, length = 1)
    public String getRequiredSign() {
        return requiredSign;
    }
    public void setRequiredSign(String requiredSign) {
        this.requiredSign = requiredSign;
    }
}
