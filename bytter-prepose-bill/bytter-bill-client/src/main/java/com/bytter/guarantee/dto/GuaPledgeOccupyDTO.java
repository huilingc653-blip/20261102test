package com.bytter.guarantee.dto;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
    /*
 * @author luxy
 * @Title: GuaPledgeOccupyDTO
 * @ProjectName T3(3.2)
 * @Description: 抵质押物占用公共数据传输对象
 * @date 2020/7/7 13:39
 */
public class GuaPledgeOccupyDTO implements Serializable {
    private String id;
    /*
     * 系统代码(必填)
     */
    private String sysCode;
    /*
     * 业务id(必填)
     */
    private String businessId;
    /*
     * 业务编号(必填)
     */
    private String itemNo;
    /*
     * 抵质押物id(必填)
     */
    private String pledgeId;
    /*
     * 占用额度(必填)
     */
    private BigDecimal occupyMoney;
    /*
     * 占用开始日期(必填)
     */
    private Date startDate;
    /*
     * 占用结束日期(必填)
     */
    private Date endDate;
    /*
     * 币别(必填)
     */
//    @Currency(inject = "curName")
    private String curId;
    /*
     * 币别名称
     */
    private String curName;
    /*
     * 状态
     */
    private Integer status;
    /*
     * 抵质押物编号
     */
    private String pledgeNo;
    /*
     * 抵质押物性质
     */
    private String pledgeAttr;
    /*
     * 抵质押物类型
     */
//    @Dict(tableName = "GUA_PLEDGE_TYPE", valueFieldName = "id", showFiledName = "TYPE_NAME", inject = "typeName")
    private String pledgeType;
    private String typeName;
    /*
     * 抵质押物名称
     */
    private String pledgeName;
    /*
     * 评估价值
     */
    private BigDecimal pledgeMoney;
    /*
     * 可用价值
     */
    private BigDecimal availMoney;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSysCode() {
        return sysCode;
    }
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    public String getItemNo() {
        return itemNo;
    }
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
    public String getPledgeId() {
        return pledgeId;
    }
    public void setPledgeId(String pledgeId) {
        this.pledgeId = pledgeId;
    }
    public BigDecimal getOccupyMoney() {
        return occupyMoney;
    }
    public void setOccupyMoney(BigDecimal occupyMoney) {
        this.occupyMoney = occupyMoney;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getPledgeNo() {
        return pledgeNo;
    }
    public void setPledgeNo(String pledgeNo) {
        this.pledgeNo = pledgeNo;
    }
    public String getPledgeAttr() {
        return pledgeAttr;
    }
    public void setPledgeAttr(String pledgeAttr) {
        this.pledgeAttr = pledgeAttr;
    }
    public String getPledgeType() {
        return pledgeType;
    }
    public void setPledgeType(String pledgeType) {
        this.pledgeType = pledgeType;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getPledgeName() {
        return pledgeName;
    }
    public void setPledgeName(String pledgeName) {
        this.pledgeName = pledgeName;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getPledgeMoney() {
        return pledgeMoney;
    }
    public void setPledgeMoney(BigDecimal pledgeMoney) {
        this.pledgeMoney = pledgeMoney;
    }
    public BigDecimal getAvailMoney() {
        return availMoney;
    }
    public void setAvailMoney(BigDecimal availMoney) {
        this.availMoney = availMoney;
    }
}
