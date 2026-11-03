package com.bytter.guarantee.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @author fusb
 * @version 1.0
 * @Title:GuaWay
 * @Description:担保方式
 * @date 2018年12月17日
 */
@Entity
@Table(name = "GUA_WAY", schema = "")
@SuppressWarnings("serial")
public class GuaWay extends IdEntity implements java.io.Serializable {
    /** 类型编码 */
    private String code;
    /** 担保方式名称 */
    private String name;
    /** 是否抵质押 **/
    private String isPledge;
    /** 抵质押性质 **/
    private String pledgeProperty;
    /** 备注 **/
    private String rmk;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 类型编码
     */
    @Column(name = "CODE")
    public String getCode() {
        return code;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 类型编码
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 抵质押类型名称
     */
    @Column(name = "NAME")
    public String getName() {
        return name;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 抵质押类型名称
     */
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "IS_PLEDGE")
    public String getIsPledge() {
        return isPledge;
    }
    public void setIsPledge(String isPledge) {
        this.isPledge = isPledge;
    }
    @Column(name = "PLEDGE_PROPERTY")
    public String getPledgeProperty() {
        return pledgeProperty;
    }
    public void setPledgeProperty(String pledgeProperty) {
        this.pledgeProperty = pledgeProperty;
    }
    @Column(name = "rmk")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
}
