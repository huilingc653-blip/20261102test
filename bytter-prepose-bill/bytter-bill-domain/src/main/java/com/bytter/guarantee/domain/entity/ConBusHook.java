package com.bytter.guarantee.domain.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * 
 * @Title: 合同业务挂接表
 * @Package com.bytter.biz.contract.entity
 * @author: zhengg
 * @date: 2019-11-6 下午2:53:20
 * @version V1.0
 */
@Entity
@Table(name = "CON_BUS_HOOK", schema = "")
@SuppressWarnings("serial")
public class ConBusHook extends IdEntity implements java.io.Serializable {
    public ConBusHook() {
    }
    public ConBusHook(String conId, String busId) {
        this.conId = conId;
        this.busId = busId;
    }
    /** 合同id */
    private String conId;
    /** 业务挂接ID */
    private String busId;
    /**
     * 创建人id
     */
    private String createId;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新人id
     */
    private String updateId;
    /**
     * 更新日期
     */
    private Date updateTime;
    /**
     * 有效标志 ：1已挂接0取消挂接
     */
    private String validSign;
    @Column(name = "CON_ID")
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    @Column(name = "BUS_ID")
    public String getBusId() {
        return busId;
    }
    public void setBusId(String busId) {
        this.busId = busId;
    }
    /**
     * 方法: 取得 创建人名称
     *
     * @return: 创建人名称
     */
    @Column(name = "CREATE_ID")
    public String getCreateId() {
        return createId;
    }
    /**
     * 设置 创建人名称
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    /**
     * 方法: 取得 创建日期
     *
     * @return: 创建日期
     */
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * 设置 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     * 方法: 取得 更新人名称
     *
     * @return: 更新人名称
     */
    @Column(name = "UPDATE_ID")
    public String getUpdateId() {
        return updateId;
    }
    /**
     * 设置 更新人名称
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    /**
     * 方法: 取得 更新日期
     *
     * @return:更新日期
     */
    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }
    /**
     * 设置更新日期
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * 方法: 取得有效标志
     *
     * @return: java.lang.String 有效标志：1已挂接0取消挂接
     */
    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    /**
     * 设置 有效标志：0业务中1业务完成2已作废
     */
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
}
