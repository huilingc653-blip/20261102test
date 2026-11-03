package com.bytter.guarantee.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author luxy
 * @Title: GuaPledgeOccupy
 * @ProjectName T3(3.2)
 * @Description: 抵质押物占用实体类
 * @date 2020/7/7 9:44
 */
@Entity
@Table(name = "GUA_PLEDGE_OCCUP")
public class GuaPledgeOccupy extends IdEntity implements java.io.Serializable {
    /** 系统代码 */
    private String sysCode;
    /** 业务id */
    private String businessId;
    /** 业务编号 */
    private String itemNo;
    /** 抵质押物id */
    private String pledgeId;
    /** 业务币种 */
    private String curId;
    /** 占用额度 */
    private BigDecimal occupyMoney;
    /** 占用开始日期 */
    private Date startDate;
    /** 占用结束日期 */
    private Date endDate;
    /** 状态 */
    private Integer status;
    @Column(name = "SYSCODE")
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    @Column(name = "BUSINESS_ID")
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    @Column(name = "ITEM_NO")
    public String getItemNo() {
        return itemNo;
    }
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
    @Column(name = "PLEDGE_ID")
    public String getPledgeId() {
        return pledgeId;
    }
    public void setPledgeId(String pledgeId) {
        this.pledgeId = pledgeId;
    }
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "OCCUP_MONEY")
    public BigDecimal getOccupyMoney() {
        return occupyMoney;
    }
    public void setOccupyMoney(BigDecimal occupyMoney) {
        this.occupyMoney = occupyMoney;
    }
    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
