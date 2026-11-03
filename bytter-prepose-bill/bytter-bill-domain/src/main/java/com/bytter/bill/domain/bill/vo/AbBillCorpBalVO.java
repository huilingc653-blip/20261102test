package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: 票据单位余额VO
 * @author zhengg
 * @date 2017-09-28 19:20:01
 * @version 1.0
 */
public class AbBillCorpBalVO implements java.io.Serializable {
    private static final long serialVersionUID = 268419642335938295L;
    private String id;
    /**银行账户ID*/
    private String bankId;
    /**银行账号*/
    private String bankAcc;
    /**单位ID*/
    @BtCompositeSearch(fieldName="d.corp_id")
    private String corpId;
    /**单位名称*/
    @BtCompositeSearch(fieldName="C.code")
    private String corpName;
    /**单位代码*/
    private String corpCode;
    /**币别ID*/
	@Currency(inject="curName")
    private String curId;
    /**
     * 币别
     */
    private String curName;
    /**余额日期*/
    @BtCompositeSearch(fieldName="d.bal_Date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date balDate;
    /**余额*/
    @BtCompositeSearch(fieldName="d.bal")
    private BigDecimal bal;
    /**可用余额*/
    @BtCompositeSearch(fieldName="d.AVAIL_Bal")
    private BigDecimal availBal;
    /**占用金额*/
    @BtCompositeSearch(fieldName="d.TIEDUP_BAL")
    private BigDecimal tiedupBal;
    
	/**商票余额*/
	@BtCompositeSearch(fieldName="d.spBal")
	private BigDecimal spBal;
	/**商票可用余额*/
	@BtCompositeSearch(fieldName="d.spAvailBal")
	private BigDecimal spAvailBal;
	/**商票占用金额*/
	@BtCompositeSearch(fieldName="d.spTiedupBal")
	private BigDecimal spTiedupBal;
	
	/**银票余额*/
	@BtCompositeSearch(fieldName="d.ypBal")
	private BigDecimal ypBal;
	/**银票可用余额*/
	@BtCompositeSearch(fieldName="d.ypAvailBal")
	private BigDecimal ypAvailBal;
	/**银票占用金额*/
	@BtCompositeSearch(fieldName="d.ypTiedupBal")
	private BigDecimal ypTiedupBal;
    
    
    /**票面金额*/
    private BigDecimal billMoney;
    /**金额*/
    private BigDecimal money;
    /**可用金额*/
    private BigDecimal availMoney;
    /**占用金额*/
    private BigDecimal tiedupMoney;
    /**开始日期*/
    private Date beginDate;
    /**结束日期*/
    private Date endDate;
    /**银行账户信息**/
    private BtBankAccDTO bankAccDTO;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public String getBankId(){
        return this.bankId;
    }
    public BtBankAccDTO getBankAccDTO() {
        return bankAccDTO;
    }
    public void setBankAccDTO(BtBankAccDTO bankAccDTO) {
        this.bankAccDTO = bankAccDTO;
    }
    public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	/**
     *方法: 设置java.lang.String
     *@param: String  银行账户ID
     */
    public void setBankId(String bankId){
        this.bankId = bankId;
    }
    /**
     *方法: 取得java.lang.String
     *@return: String  银行账号
     */
    public String getBankAcc(){
        return this.bankAcc;
    }
    /**
     *方法: 设置java.lang.String
     *@param: String  银行账号
     */
    public void setBankAcc(String bankAcc){
        this.bankAcc = bankAcc;
    }
    /**
     *方法: 取得java.lang.String
     *@return: String  单位ID
     */
    public String getCorpId(){
        return this.corpId;
    }
    /**
     *方法: 设置java.lang.String
     *@param: String  单位ID
     */
    public void setCorpId(String corpId){
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    /**
     *方法: 取得java.lang.String
     *@return: String  币别ID
     */
    public String getCurId(){
        return this.curId;
    }
    /**
     *方法: 设置java.lang.String
     *@param: String  币别ID
     */
    public void setCurId(String curId){
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    /**
     *方法: 取得Date
     *@return: Date  余额日期
     */
    public Date getBalDate(){
        return this.balDate;
    }
    /**
     *方法: 设置Date
     *@param: Date  余额日期
     */
    public void setBalDate(Date balDate){
        this.balDate = balDate;
    }
    /**
     *方法: 取得BigDecimal
     *@return: BigDecimal  余额
     */
    public BigDecimal getBal(){
        return this.bal;
    }
    /**
     *方法: 设置BigDecimal
     *@param: BigDecimal  余额
     */
    public void setBal(BigDecimal bal){
        this.bal = bal;
    }
    /**
     *方法: 取得BigDecimal
     *@return: BigDecimal  可用余额
     */
    public BigDecimal getAvailBal(){
        return this.availBal;
    }
    /**
     *方法: 设置BigDecimal
     *@param: BigDecimal  可用余额
     */
    public void setAvailBal(BigDecimal availBal){
        this.availBal = availBal;
    }
    /**
     *方法: 取得BigDecimal
     *@return: BigDecimal  占用金额
     */
    public BigDecimal getTiedupBal(){
        return this.tiedupBal;
    }
    /**
     *方法: 设置BigDecimal
     *@param: BigDecimal  占用金额
     */
    public void setTiedupBal(BigDecimal tiedupBal){
        this.tiedupBal = tiedupBal;
    }
    /**
     *方法: 取得java.lang.String
     *@return: String  创建人登录名称
     */
    public String getCreateBy(){
        return this.createBy;
    }
    /**
     *方法: 设置java.lang.String
     *@param: String  创建人登录名称
     */
    public void setCreateBy(String createBy){
        this.createBy = createBy;
    }
    /**
     *方法: 取得Date
     *@return: Date  创建日期
     */
    public Date getCreateDate(){
        return this.createDate;
    }
    /**
     *方法: 设置Date
     *@param: Date  创建日期
     */
    public void setCreateDate(Date createDate){
        this.createDate = createDate;
    }
    /**
     *方法: 取得java.lang.String
     *@return: String  更新人登录名称
     */
    public String getUpdateBy(){
        return this.updateBy;
    }
    /**
     *方法: 设置java.lang.String
     *@param: String  更新人登录名称
     */
    public void setUpdateBy(String updateBy){
        this.updateBy = updateBy;
    }
    /**
     *方法: 取得Date
     *@return: Date  更新日期
     */
    public Date getUpdateDate(){
        return this.updateDate;
    }
    /**
     *方法: 设置Date
     *@param: Date  更新日期
     */
    public void setUpdateDate(Date updateDate){
        this.updateDate = updateDate;
    }
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public BigDecimal getAvailMoney() {
        return availMoney;
    }
    public void setAvailMoney(BigDecimal availMoney) {
        this.availMoney = availMoney;
    }
    public BigDecimal getTiedupMoney() {
        return tiedupMoney;
    }
    public void setTiedupMoney(BigDecimal tiedupMoney) {
        this.tiedupMoney = tiedupMoney;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public BigDecimal getSpBal() {
		return spBal;
	}
	public void setSpBal(BigDecimal spBal) {
		this.spBal = spBal;
	}
	public BigDecimal getSpAvailBal() {
		return spAvailBal;
	}
	public void setSpAvailBal(BigDecimal spAvailBal) {
		this.spAvailBal = spAvailBal;
	}
	public BigDecimal getSpTiedupBal() {
		return spTiedupBal;
	}
	public void setSpTiedupBal(BigDecimal spTiedupBal) {
		this.spTiedupBal = spTiedupBal;
	}
	public BigDecimal getYpBal() {
		return ypBal;
	}
	public void setYpBal(BigDecimal ypBal) {
		this.ypBal = ypBal;
	}
	public BigDecimal getYpAvailBal() {
		return ypAvailBal;
	}
	public void setYpAvailBal(BigDecimal ypAvailBal) {
		this.ypAvailBal = ypAvailBal;
	}
	public BigDecimal getYpTiedupBal() {
		return ypTiedupBal;
	}
	public void setYpTiedupBal(BigDecimal ypTiedupBal) {
		this.ypTiedupBal = ypTiedupBal;
	}
    @Override
    public String toString() {
        return "AbBillCorpBalVO{" +
                "id='" + id + '\'' +
                ", bankId='" + bankId + '\'' +
                ", bankAcc='" + bankAcc + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpName='" + corpName + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", curId='" + curId + '\'' +
                ", curName='" + curName + '\'' +
                ", balDate=" + balDate +
                ", bal=" + bal +
                ", availBal=" + availBal +
                ", tiedupBal=" + tiedupBal +
                ", spBal=" + spBal +
                ", spAvailBal=" + spAvailBal +
                ", spTiedupBal=" + spTiedupBal +
                ", ypBal=" + ypBal +
                ", ypAvailBal=" + ypAvailBal +
                ", ypTiedupBal=" + ypTiedupBal +
                ", billMoney=" + billMoney +
                ", money=" + money +
                ", availMoney=" + availMoney +
                ", tiedupMoney=" + tiedupMoney +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", bankAccDTO=" + bankAccDTO +
                ", createBy='" + createBy + '\'' +
                ", createDate=" + createDate +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate=" + updateDate +
                '}';
    }
}
