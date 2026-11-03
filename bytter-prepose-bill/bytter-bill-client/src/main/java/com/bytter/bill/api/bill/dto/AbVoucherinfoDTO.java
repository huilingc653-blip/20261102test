package com.bytter.bill.api.bill.dto;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import java.util.Date;
public class AbVoucherinfoDTO extends IdEntity implements java.io.Serializable{
    /**票据号码*/
    private String origNo;
    /**票据类型：10 银票、11 商票*/
    private Integer billType;
    /**不得转让标记EM00可再转让EM01不得转让*/
    private String banendrsmtmk;
    /**处理标记 10正在处理11处理完成*/
    private Integer boxStatus;
    /**票据状态*/
    private String billStatus;
    /**流通介质：10 纸票、11 电票*/
    private Integer billFlag;
    /**票据金额*/
    private java.math.BigDecimal billMoney;
    /**出票日期*/
    private Date startDate;
    /**到期日期*/
    private Date endDate;
    /**出票人*/
    private String payer;
    /**出票人账号*/
    private String payeracc;
    /**出票人开户银行名称*/
    private String payerBankName;
    /**出票人开户银行行号*/
    private String payerbankno;
    /**收票人全称*/
    private String payee;
    /**收票人账号*/
    private String payeeacc;
    /**收票人开户银行名称*/
    private String payeeBankName;
    /**收票人开户银行行号*/
    private String payeebankno;
    /**承兑人*/
    private String acceptor;
    /**承兑人账号*/
    private String acceptoracc;
    /**承兑人开户行行号*/
    private String acceptorbankno;
    /**承兑人开户行名称*/
    private String acceptorBankName;
    /**承兑人开户行地址*/
    private String acceptorbankaddress;
    /**瑕疵类型 通过数据字典维护*/
    private String arXclx;
    /**承兑合同号*/
    private String acontractNo;
    /**发票号*/
    private String ainvcnb;
    /**承兑协议编号*/
    private String accptncagrmtnb;
    /** 来源类型 0应付票据，1应收票据*/
    private String sourceType;
    /**创建人名称*/
    private String createName;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人名称*/
    private String updateName;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**开票信用等级*/
    private String payerCdtRatgs;
    /**开票评级机构*/
    private String payerCdtRatgGgcy;
    /**开票评级到期日*/
    private Date payerCdtRatgDuedt;
    /** 从表id*/
    private String vvid;
    /**付款单号ID*/
    private String payId;
    /**操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签*/
    private String optStatus;
    @Column(name ="PAYER_CDT_RATGS")
    public String getPayerCdtRatgs() {
        return payerCdtRatgs;
    }
    public void setPayerCdtRatgs(String payerCdtRatgs) {
        this.payerCdtRatgs = payerCdtRatgs;
    }
    @Column(name ="PAYER_CDT_RATG_AGCY")
    public String getPayerCdtRatgGgcy() {
        return payerCdtRatgGgcy;
    }
    public void setPayerCdtRatgGgcy(String payerCdtRatgGgcy) {
        this.payerCdtRatgGgcy = payerCdtRatgGgcy;
    }
    @Column(name ="PAYER_CDT_RATG_DUEDT")
    public Date getPayerCdtRatgDuedt() {
        return payerCdtRatgDuedt;
    }
    public void setPayerCdtRatgDuedt(Date payerCdtRatgDuedt) {
        this.payerCdtRatgDuedt = payerCdtRatgDuedt;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  票据号码
     */
    @Column(name ="ORIG_NO",nullable=true,length=40)
    public String getOrigNo(){
        return this.origNo;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  票据号码
     */
    public void setOrigNo(String origNo){
        this.origNo = origNo;
    }
    /**
     *方法: 取得java.lang.Integer
     *@return: java.lang.Integer  票据类型：10 银票、11 商票
     */
    @Column(name ="BILL_TYPE",nullable=true,length=10)
    public Integer getBillType(){
        return this.billType;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  票据类型：10 银票、11 商票
     */
    public void setBillType(Integer billType){
        this.billType = billType;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  不得转让标记EM00可再转让EM01不得转让
     */
    @Column(name ="BANENDRSMTMK",nullable=true,length=8)
    public String getBanendrsmtmk(){
        return this.banendrsmtmk;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  不得转让标记EM00可再转让EM01不得转让
     */
    public void setBanendrsmtmk(String banendrsmtmk){
        this.banendrsmtmk = banendrsmtmk;
    }
    /**
     *方法: 取得java.lang.Integer
     *@return: java.lang.Integer  处理标记 10正在处理11处理完成
     */
    @Column(name ="BOX_STATUS",nullable=true,length=10)
    public Integer getBoxStatus(){
        return this.boxStatus;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  处理标记 10正在处理11处理完成
     */
    public void setBoxStatus(Integer boxStatus){
        this.boxStatus = boxStatus;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  票据状态
     */
    @Column(name ="BILL_STATUS",nullable=true,length=10)
    public String getBillStatus(){
        return this.billStatus;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  票据状态
     */
    public void setBillStatus(String billStatus){
        this.billStatus = billStatus;
    }
    /**
     *方法: 取得java.lang.Integer
     *@return: java.lang.Integer  流通介质：10 纸票、11 电票
     */
    @Column(name ="BILL_FLAG",nullable=true,length=10)
    public Integer getBillFlag(){
        return this.billFlag;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  流通介质：10 纸票、11 电票
     */
    public void setBillFlag(Integer billFlag){
        this.billFlag = billFlag;
    }
    /**
     *方法: 取得java.math.BigDecimal
     *@return: java.math.BigDecimal  票据金额
     */
    @Column(name ="BILL_MONEY",nullable=true,scale=2,length=15)
    public java.math.BigDecimal getBillMoney(){
        return this.billMoney;
    }
    /**
     *方法: 设置java.math.BigDecimal
     *@param: java.math.BigDecimal  票据金额
     */
    public void setBillMoney(java.math.BigDecimal billMoney){
        this.billMoney = billMoney;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  出票日期
     */
    @Column(name ="START_DATE",nullable=true)
    public Date getStartDate(){
        return this.startDate;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  出票日期
     */
    public void setStartDate(Date startDate){
        this.startDate = startDate;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  到期日期
     */
    @Column(name ="END_DATE",nullable=true)
    public Date getEndDate(){
        return this.endDate;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  到期日期
     */
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  出票人
     */
    @Column(name ="PAYER",nullable=true,length=240)
    public String getPayer(){
        return this.payer;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  出票人
     */
    public void setPayer(String payer){
        this.payer = payer;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  出票人账号
     */
    @Column(name ="PAYERACC",nullable=true,length=30)
    public String getPayeracc(){
        return this.payeracc;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  出票人账号
     */
    public void setPayeracc(String payeracc){
        this.payeracc = payeracc;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  出票人开户银行名称
     */
    @Column(name ="PAYER_BANK_NAME",nullable=true,length=240)
    public String getPayerBankName(){
        return this.payerBankName;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  出票人开户银行名称
     */
    public void setPayerBankName(String payerBankName){
        this.payerBankName = payerBankName;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  出票人开户银行行号
     */
    @Column(name ="PAYERBANKNO",nullable=true,length=30)
    public String getPayerbankno(){
        return this.payerbankno;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  出票人开户银行行号
     */
    public void setPayerbankno(String payerbankno){
        this.payerbankno = payerbankno;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  收票人全称
     */
    @Column(name ="PAYEE",nullable=true,length=240)
    public String getPayee(){
        return this.payee;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  收票人全称
     */
    public void setPayee(String payee){
        this.payee = payee;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  收票人账号
     */
    @Column(name ="PAYEEACC",nullable=true,length=30)
    public String getPayeeacc(){
        return this.payeeacc;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  收票人账号
     */
    public void setPayeeacc(String payeeacc){
        this.payeeacc = payeeacc;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  收票人开户银行名称
     */
    @Column(name ="PAYEE_BANK_NAME",nullable=true,length=240)
    public String getPayeeBankName(){
        return this.payeeBankName;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  收票人开户银行名称
     */
    public void setPayeeBankName(String payeeBankName){
        this.payeeBankName = payeeBankName;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  收票人开户银行行号
     */
    @Column(name ="PAYEEBANKNO",nullable=true,length=30)
    public String getPayeebankno(){
        return this.payeebankno;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  收票人开户银行行号
     */
    public void setPayeebankno(String payeebankno){
        this.payeebankno = payeebankno;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  承兑人
     */
    @Column(name ="ACCEPTOR",nullable=true,length=240)
    public String getAcceptor(){
        return this.acceptor;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  承兑人
     */
    public void setAcceptor(String acceptor){
        this.acceptor = acceptor;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  承兑人账号
     */
    @Column(name ="ACCEPTORACC",nullable=true,length=30)
    public String getAcceptoracc(){
        return this.acceptoracc;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  承兑人账号
     */
    public void setAcceptoracc(String acceptoracc){
        this.acceptoracc = acceptoracc;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  承兑人开户行行号
     */
    @Column(name ="ACCEPTORBANKNO",nullable=true,length=30)
    public String getAcceptorbankno(){
        return this.acceptorbankno;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  承兑人开户行行号
     */
    public void setAcceptorbankno(String acceptorbankno){
        this.acceptorbankno = acceptorbankno;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  承兑人开户行名称
     */
    @Column(name ="ACCEPTOR_BANK_NAME",nullable=true,length=240)
    public String getAcceptorBankName(){
        return this.acceptorBankName;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  承兑人开户行名称
     */
    public void setAcceptorBankName(String acceptorBankName){
        this.acceptorBankName = acceptorBankName;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  承兑人开户行地址
     */
    @Column(name ="ACCEPTORBANKADDRESS",nullable=true,length=500)
    public String getAcceptorbankaddress(){
        return this.acceptorbankaddress;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  承兑人开户行地址
     */
    public void setAcceptorbankaddress(String acceptorbankaddress){
        this.acceptorbankaddress = acceptorbankaddress;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  瑕疵类型 通过数据字典维护
     */
    @Column(name ="AR_XCLX",nullable=true,length=30)
    public String getArXclx(){
        return this.arXclx;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  瑕疵类型 通过数据字典维护
     */
    public void setArXclx(String arXclx){
        this.arXclx = arXclx;
    }
    /**
     * 方法: 取得java.lang.String
     * @return: java.lang.String 承兑合同号
     */
    @Column(name = "A_CONTRACT_NO", nullable = true, length = 250)
    public String getAcontractNo() {
        return this.acontractNo;
    }
    /**
     * 方法: 设置java.lang.String
     * @param: java.lang.String  承兑合同号
     *
     */
    public void setAcontractNo(String acontractNo) {
        this.acontractNo = acontractNo;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  发票号
     */
    @Column(name ="A_INVCNB",nullable=true,length=250)
    public String getAinvcnb(){
        return this.ainvcnb;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  发票号
     */
    public void setAinvcnb(String ainvcnb){
        this.ainvcnb = ainvcnb;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  承兑协议编号
     */
    @Column(name ="ACCPTNCAGRMTNB",nullable=true,length=250)
    public String getAccptncagrmtnb(){
        return this.accptncagrmtnb;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  承兑协议编号
     */
    public void setAccptncagrmtnb(String accptncagrmtnb){
        this.accptncagrmtnb = accptncagrmtnb;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  创建人名称
     */
    @Column(name ="CREATE_NAME",nullable=true,length=50)
    public String getCreateName(){
        return this.createName;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  创建人名称
     */
    public void setCreateName(String createName){
        this.createName = createName;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  创建人登录名称
     */
    @Column(name ="CREATE_BY",nullable=true,length=50)
    public String getCreateBy(){
        return this.createBy;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  创建人登录名称
     */
    public void setCreateBy(String createBy){
        this.createBy = createBy;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  创建日期
     */
    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate(){
        return this.createDate;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  创建日期
     */
    public void setCreateDate(Date createDate){
        this.createDate = createDate;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  更新人名称
     */
    @Column(name ="UPDATE_NAME",nullable=true,length=50)
    public String getUpdateName(){
        return this.updateName;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  更新人名称
     */
    public void setUpdateName(String updateName){
        this.updateName = updateName;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  更新人登录名称
     */
    @Column(name ="UPDATE_BY",nullable=true,length=50)
    public String getUpdateBy(){
        return this.updateBy;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  更新人登录名称
     */
    public void setUpdateBy(String updateBy){
        this.updateBy = updateBy;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  更新日期
     */
    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate(){
        return this.updateDate;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  更新日期
     */
    public void setUpdateDate(Date updateDate){
        this.updateDate = updateDate;
    }
    @Column(name ="SOURCE_TYPE")
    public String getSourceType() {
        return sourceType;
    }
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
    public String getPayId() {
        return payId;
    }
    public void setPayId(String payId) {
        this.payId = payId;
    }
    public String getOptStatus() {
        return optStatus;
    }
    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }
    public String getVvid() {
        return vvid;
    }
    public void setVvid(String vvid) {
        this.vvid = vvid;
    }
    @Override
    public String toString() {
        return "AbVoucherinfoDTO{" +
                "origNo='" + origNo + '\'' +
                ", billType=" + billType +
                ", banendrsmtmk='" + banendrsmtmk + '\'' +
                ", boxStatus=" + boxStatus +
                ", billStatus='" + billStatus + '\'' +
                ", billFlag=" + billFlag +
                ", billMoney=" + billMoney +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", payer='" + payer + '\'' +
                ", payeracc='" + payeracc + '\'' +
                ", payerBankName='" + payerBankName + '\'' +
                ", payerbankno='" + payerbankno + '\'' +
                ", payee='" + payee + '\'' +
                ", payeeacc='" + payeeacc + '\'' +
                ", payeeBankName='" + payeeBankName + '\'' +
                ", payeebankno='" + payeebankno + '\'' +
                ", acceptor='" + acceptor + '\'' +
                ", acceptoracc='" + acceptoracc + '\'' +
                ", acceptorbankno='" + acceptorbankno + '\'' +
                ", acceptorBankName='" + acceptorBankName + '\'' +
                ", acceptorbankaddress='" + acceptorbankaddress + '\'' +
                ", arXclx='" + arXclx + '\'' +
                ", acontractNo='" + acontractNo + '\'' +
                ", ainvcnb='" + ainvcnb + '\'' +
                ", accptncagrmtnb='" + accptncagrmtnb + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", createName='" + createName + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDate=" + createDate +
                ", updateName='" + updateName + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateDate=" + updateDate +
                ", payerCdtRatgs='" + payerCdtRatgs + '\'' +
                ", payerCdtRatgGgcy='" + payerCdtRatgGgcy + '\'' +
                ", payerCdtRatgDuedt=" + payerCdtRatgDuedt +
                ", vvid='" + vvid + '\'' +
                ", payId='" + payId + '\'' +
                ", optStatus='" + optStatus + '\'' +
                '}';
    }
}
