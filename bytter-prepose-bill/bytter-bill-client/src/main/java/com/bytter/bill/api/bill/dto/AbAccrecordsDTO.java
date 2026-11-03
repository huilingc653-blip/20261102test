package com.bytter.bill.api.bill.dto;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.constant.ConstantBean;
import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
public class AbAccrecordsDTO extends IdEntity implements java.io.Serializable{
    /**票据主表ID*/
    private String vid;
    /**票据从表id*/
    private String vvid;
    /**票据签收表id*/
    private String recid;
    /**票据划拨表ID*/
    private String trid;
    /**上一流水ID*/
    private String lastid;
    /**上一票据状态代码*/
    private String lastbillcode;
    /**处理标志：0、未完成；1可用；2已结束*/
    private String validSign;
    /**是否隐藏1是0否*/
    private String isHide;
    /**审批处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成*/
    private Integer status;
    /**操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签*/
    private String optStatus;
    /**是否发送指令标志：0否； 1是 ；*/
    private String sendSign;
    /**返回代码*/
    private String returnCode;
    /**处理结果*/
    private String returnMsg;
    /**业务类型*/
    private String briefId;
    /**内部业务标志：0  否；1 是*/
    private Integer insideSing;
    /**所属库存:10单位库存11中心代管12银行托管*/
    private String enterStockNew;
    /**所属库存:10单位库存11中心代管12银行托管*/
    private String enterStockOld;
    /**保证类型:01开票保证，02承兑保证；03背书保证*/
    private String bailType;
    /**撤回类型*/
    private String recallType;
    /**不得转让标记：EM00 可再转让；EM01 不得转让*/
    private String bankendrsmtmk;
    /**申请日期*/
    private Date applyDate;
    /**业务归属日期*/
    private Date businessDate;
    /**追索同意清偿金额*/
    private java.math.BigDecimal rcrsokmoney;
    /**前手*/
    private String cnAccrecords;
    /**SU00-同意  SU01-拒绝*/
    private String signUpFlag;
    /**摘要  */
    private String abs;
    /**备注*/
    private String rmk;
    /**授信代码*/
    private String creditCode;
    /**预算科目*/
    private String budgetCode;
    /**保证金账户*/
    private String bailAcc;
    /**保证金*/
    private java.math.BigDecimal bailmoney;
    /**保证金比例*/
    private java.math.BigDecimal bailpercent;
    /**保证金利率*/
    private java.math.BigDecimal bailrate;
    /**手续费*/
    private java.math.BigDecimal feemoney;
    /**追索理由代码:RC00承兑人被依法宣告破产RC01承兑人因违法被责令终止活动（非拒付追索时填写）*/
    private String rcrsrsncd;
    /**追索种类：RT00 拒付追索；RT01 非拒付追索*/
    private String rcrsTp;
    /**收款或付款确认人*/
    private String signerok;
    /**银行接口代码*/
    private String bifCode;
    /**业务申请人类别RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司*/
    private String reqRole;
    /**业务申请人名称*/
    private String reqNm;
    /**业务申请人组织机构代码*/
    private String reqCmonid;
    /**业务申请人账号*/
    private String reqAcctId;
    /**业务申请人开户行号*/
    private String reqAcctsvcr;
    /**业务申请人承接行号*/
    private String reqAcctsvcrAg;
    /**业务接收人类别：RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司*/
    private String recRole;
    /**业务接收人名称*/
    private String recNm;
    /**业务接收人组织机构代码*/
    private String recCmonid;
    /**业务接收人账号*/
    private String recAcctId;
    /**业务接收人开户行号*/
    private String recAcctsvcr;
    /**业务接收人承接行号*/
    private String recAcctsvcrAg;
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
    /**保证人地址 背书时候保存对内背书的单位*/
    private String adr;
    /**开票信用等级*/
    private String cdtRatgs;
    /**开票评级机构*/
    private String cdtRatgGgcy;
    /**开票评级到期日*/
    private Date cdtRatgDuedt;
    private Integer billFlag;
    /**清算方式 SM00线上清算、SMO1线下清算*/
    private String sttlmmk;
    /**全局业务ID*/
    private String busGlobalId;
    /** 票据来源：10收票登记11背书登记12质押登记14贴现登记15归集收入16上划收入17下拨收入 */
    private String billSource;
    private AbVoucherinfoDTO vouInfo;
//    private RatUrationDTO ratUrationDTO;
    /**
     * 创建人是否为空 1为是，0和空位否
     */
    private String isCreateByNull;
    /**
     * 兑付方式:0或空线下兑付,1线上兑付'
     */
    private String emitSttlmmk;
    private String corpId;
    private String netId;
    private String otherCorpId;
    private CorpDTO corpInfo;
    private String sysRaceCode;
    private ConstantBean<Integer> busOriType;
    private String bankAcc;
    private String bankCode;
    private String bankName;
    private String origNo;
    private Date endDate;
    private BigDecimal billMoney;
    private String excId;

    private String batchNo;
    /**
     * 批次开票顺序号
     */
    private Integer batchDtlNo ;

    public Integer getBatchDtlNo() {
        return batchDtlNo;
    }

    public void setBatchDtlNo(Integer batchDtlNo) {
        this.batchDtlNo = batchDtlNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getExcId() {
        return excId;
    }

    public void setExcId(String excId) {
        this.excId = excId;
    }

    @Transient
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    @Transient
    public String getOtherCorpId() {
        return otherCorpId;
    }
    public void setOtherCorpId(String otherCorpId) {
        this.otherCorpId = otherCorpId;
    }
    @Transient
    public CorpDTO getCorpInfo() {
        return corpInfo;
    }
    public void setCorpInfo(CorpDTO corpInfo) {
        this.corpInfo = corpInfo;
    }
    @Transient
    public String getSysRaceCode() {
        return sysRaceCode;
    }
    public void setSysRaceCode(String sysRaceCode) {
        this.sysRaceCode = sysRaceCode;
    }
    @Transient
    public ConstantBean<Integer> getBusOriType() {
        return busOriType;
    }
    public void setBusOriType(ConstantBean<Integer> busOriType) {
        this.busOriType = busOriType;
    }
    @Transient
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Transient
    public String getIsCreateByNull() {
        return isCreateByNull;
    }
    public void setIsCreateByNull(String isCreateByNull) {
        this.isCreateByNull = isCreateByNull;
    }
    @Column(name ="EMIT_STTLMMK",nullable=true,length=1)
    public String getEmitSttlmmk() {
        return emitSttlmmk;
    }
    public void setEmitSttlmmk(String emitSttlmmk) {
        this.emitSttlmmk = emitSttlmmk;
    }
//    @Transient
//    public RatUrationDTO getRatUrationDTO() {
//        return ratUrationDTO;
//    }
//    public void setRatUrationDTO(RatUrationDTO ratUrationDTO) {
//        this.ratUrationDTO = ratUrationDTO;
//    }
    @Column(name ="BUS_GLOBAL_ID")
    public String getBusGlobalId() {
        return busGlobalId;
    }
    public void setBusGlobalId(String busGlobalId) {
        this.busGlobalId = busGlobalId;
    }
    @Column(name ="CDT_RATGS")
    public String getCdtRatgs() {
        return cdtRatgs;
    }
    public void setCdtRatgs(String cdtRatgs) {
        this.cdtRatgs = cdtRatgs;
    }
    @Column(name ="CDT_RATG_AGCY")
    public String getCdtRatgGgcy() {
        return cdtRatgGgcy;
    }
    public void setCdtRatgGgcy(String cdtRatgGgcy) {
        this.cdtRatgGgcy = cdtRatgGgcy;
    }
    @Column(name ="CDT_RATG_DUEDT")
    public Date getCdtRatgDuedt() {
        return cdtRatgDuedt;
    }
    public void setCdtRatgDuedt(Date cdtRatgDuedt) {
        this.cdtRatgDuedt = cdtRatgDuedt;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  票据主表ID
     */
    @Column(name ="VID",nullable=true,length=32)
    public String getVid(){
        return this.vid;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  票据主表ID
     */
    public void setVid(String vid){
        this.vid = vid;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  票据从表id
     */
    @Column(name ="VVID",nullable=true,length=32)
    public String getVvid(){
        return this.vvid;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  票据从表id
     */
    public void setVvid(String vvid){
        this.vvid = vvid;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  票据签收表id
     */
    @Column(name ="RECID",nullable=true,length=32)
    public String getRecid(){
        return this.recid;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  票据签收表id
     */
    public void setRecid(String recid){
        this.recid = recid;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  票据划拨表ID
     */
    @Column(name ="TRID",nullable=true,length=32)
    public String getTrid(){
        return this.trid;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  票据划拨表ID
     */
    public void setTrid(String trid){
        this.trid = trid;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  上一流水ID
     */
    @Column(name ="LASTID",nullable=true,length=32)
    public String getLastid(){
        return this.lastid;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  上一流水ID
     */
    public void setLastid(String lastid){
        this.lastid = lastid;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  上一票据状态代码
     */
    @Column(name ="LASTBILLCODE",nullable=true,length=10)
    public String getLastbillcode(){
        return this.lastbillcode;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  上一票据状态代码
     */
    public void setLastbillcode(String lastbillcode){
        this.lastbillcode = lastbillcode;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String 处理标志：0、未完成；1可用；2已结束
     */
    @Column(name ="VALID_SIGN",nullable=true,length=1)
    public String getValidSign() {
        return this.validSign;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String 处理标志：0、未完成；1可用；2已结束
     */
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    /**
     *方法: 取得java.lang.Integer
     *@return: java.lang.Integer  审批处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
     */
    @Column(name ="STATUS",nullable=true,length=10)
    public Integer getStatus(){
        return this.status;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  审批处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
     */
    public void setStatus(Integer status){
        this.status = status;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签
     */
    @Column(name ="OPT_STATUS",nullable=true,length=10)
    public String getOptStatus(){
        return this.optStatus;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签
     */
    public void setOptStatus(String optStatus){
        this.optStatus = optStatus;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  是否发送指令标志：0否； 1是 ；
     */
    @Column(name ="SEND_SIGN",nullable=true,length=1)
    public String getSendSign(){
        return this.sendSign;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  是否发送指令标志：0否； 1是 ；
     */
    public void setSendSign(String sendSign){
        this.sendSign = sendSign;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  返回代码
     */
    @Column(name ="RETURN_CODE",nullable=true,length=10)
    public String getReturnCode(){
        return this.returnCode;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  返回代码
     */
    public void setReturnCode(String returnCode){
        this.returnCode = returnCode;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  处理结果
     */
    @Column(name ="RETURN_MSG",nullable=true,length=200)
    public String getReturnMsg(){
        return this.returnMsg;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  处理结果
     */
    public void setReturnMsg(String returnMsg){
        this.returnMsg = returnMsg;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务类型
     */
    @Column(name ="BRIEF_ID",nullable=true,length=10)
    public String getBriefId(){
        return this.briefId;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务类型
     */
    public void setBriefId(String briefId){
        this.briefId = briefId;
    }
    /**
     *方法: 取得java.lang.Integer
     *@return: java.lang.Integer  内部业务标志：0  否；1 是
     */
    @Column(name ="INSIDE_SING",nullable=true,length=10)
    public Integer getInsideSing(){
        return this.insideSing;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  内部业务标志：0  否；1 是
     */
    public void setInsideSing(Integer insideSing){
        this.insideSing = insideSing;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  所属库存:10单位库存11中心代管12银行托管
     */
    @Column(name ="ENTER_STOCK_NEW",nullable=true,length=10)
    public String getEnterStockNew(){
        return this.enterStockNew;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  所属库存:10单位库存11中心代管12银行托管
     */
    public void setEnterStockNew(String enterStockNew){
        this.enterStockNew = enterStockNew;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  所属库存:10单位库存11中心代管12银行托管
     */
    @Column(name ="ENTER_STOCK_OLD",nullable=true,length=10)
    public String getEnterStockOld(){
        return this.enterStockOld;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  所属库存:10单位库存11中心代管12银行托管
     */
    public void setEnterStockOld(String enterStockOld){
        this.enterStockOld = enterStockOld;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  保证类型:01开票保证，02承兑保证；03背书保证
     */
    @Column(name ="BAIL_TYPE",nullable=true,length=10)
    public String getBailType(){
        return this.bailType;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  保证类型:01开票保证，02承兑保证；03背书保证
     */
    public void setBailType(String bailType){
        this.bailType = bailType;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  撤回类型
     */
    @Column(name ="RECALL_TYPE",nullable=true,length=10)
    public String getRecallType(){
        return this.recallType;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  撤回类型
     */
    public void setRecallType(String recallType){
        this.recallType = recallType;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  不得转让标记：EM00 可再转让；EM01 不得转让
     */
    @Column(name ="BANKENDRSMTMK",nullable=true,length=4)
    public String getBankendrsmtmk(){
        return this.bankendrsmtmk;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  不得转让标记：EM00 可再转让；EM01 不得转让
     */
    public void setBankendrsmtmk(String bankendrsmtmk){
        this.bankendrsmtmk = bankendrsmtmk;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  申请日期
     */
    @Column(name ="APPLY_DATE",nullable=true)
    public Date getApplyDate(){
        return this.applyDate;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  申请日期
     */
    public void setApplyDate(Date applyDate){
        this.applyDate = applyDate;
    }
    /**
     *方法: 取得java.util.Date
     *@return: java.util.Date  业务归属日期
     */
    @Column(name ="BUSINESS_DATE",nullable=true)
    public Date getBusinessDate(){
        return this.businessDate;
    }
    /**
     *方法: 设置java.util.Date
     *@param: java.util.Date  业务归属日期
     */
    public void setBusinessDate(Date businessDate){
        this.businessDate = businessDate;
    }
    /**
     *方法: 取得java.math.BigDecimal
     *@return: java.math.BigDecimal  追索同意清偿金额
     */
    @Column(name ="RCRSOKMONEY",nullable=true,scale=2,length=15)
    public java.math.BigDecimal getRcrsokmoney(){
        return this.rcrsokmoney;
    }
    /**
     *方法: 设置java.math.BigDecimal
     *@param: java.math.BigDecimal  追索同意清偿金额
     */
    public void setRcrsokmoney(java.math.BigDecimal rcrsokmoney){
        this.rcrsokmoney = rcrsokmoney;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  前手
     */
    @Column(name ="CN_ACCRECORDS",nullable=true,length=240)
    public String getCnAccrecords(){
        return this.cnAccrecords;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  前手
     */
    public void setCnAccrecords(String cnAccrecords){
        this.cnAccrecords = cnAccrecords;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  SU00-同意  SU01-拒绝
     */
    @Column(name ="SIGN_UP_FLAG",nullable=true,length=50)
    public String getSignUpFlag(){
        return this.signUpFlag;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  SU00-同意  SU01-拒绝
     */
    public void setSignUpFlag(String signUpFlag){
        this.signUpFlag = signUpFlag;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  摘要
     */
    @Column(name ="ABS",nullable=true,length=100)
    public String getAbs(){
        return this.abs;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  摘要
     */
    public void setAbs(String abs){
        this.abs = abs;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  备注
     */
    @Column(name ="RMK",nullable=true,length=100)
    public String getRmk(){
        return this.rmk;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  备注
     */
    public void setRmk(String rmk){
        this.rmk = rmk;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  授信代码
     */
    @Column(name ="CREDIT_CODE",nullable=true,length=30)
    public String getCreditCode(){
        return this.creditCode;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  授信代码
     */
    public void setCreditCode(String creditCode){
        this.creditCode = creditCode;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  预算科目
     */
    @Column(name ="BUDGET_CODE",nullable=true,length=30)
    public String getBudgetCode(){
        return this.budgetCode;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  预算科目
     */
    public void setBudgetCode(String budgetCode){
        this.budgetCode = budgetCode;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  保证金账户
     */
    @Column(name ="BAIL_ACC",nullable=true,length=30)
    public String getBailAcc(){
        return this.bailAcc;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  保证金账户
     */
    public void setBailAcc(String bailAcc){
        this.bailAcc = bailAcc;
    }
    /**
     *方法: 取得java.math.BigDecimal
     *@return: java.math.BigDecimal  保证金
     */
    @Column(name ="BAILMONEY",nullable=true,scale=2,length=15)
    public java.math.BigDecimal getBailmoney(){
        return this.bailmoney;
    }
    /**
     *方法: 设置java.math.BigDecimal
     *@param: java.math.BigDecimal  保证金
     */
    public void setBailmoney(java.math.BigDecimal bailmoney){
        this.bailmoney = bailmoney;
    }
    /**
     *方法: 取得java.math.BigDecimal
     *@return: java.math.BigDecimal  保证金比例
     */
    @Column(name ="BAILPERCENT",nullable=true,scale=6,length=8)
    public java.math.BigDecimal getBailpercent(){
        return this.bailpercent;
    }
    /**
     *方法: 设置java.math.BigDecimal
     *@param: java.math.BigDecimal  保证金比例
     */
    public void setBailpercent(java.math.BigDecimal bailpercent){
        this.bailpercent = bailpercent;
    }
    /**
     *方法: 取得java.math.BigDecimal
     *@return: java.math.BigDecimal  保证金利率
     */
    @Column(name ="BAILRATE",nullable=true,scale=6,length=8)
    public java.math.BigDecimal getBailrate(){
        return this.bailrate;
    }
    /**
     *方法: 设置java.math.BigDecimal
     *@param: java.math.BigDecimal  保证金利率
     */
    public void setBailrate(java.math.BigDecimal bailrate){
        this.bailrate = bailrate;
    }
    /**
     *方法: 取得java.math.BigDecimal
     *@return: java.math.BigDecimal  手续费
     */
    @Column(name ="FEEMONEY",nullable=true,scale=2,length=15)
    public java.math.BigDecimal getFeemoney(){
        return this.feemoney;
    }
    /**
     *方法: 设置java.math.BigDecimal
     *@param: java.math.BigDecimal  手续费
     */
    public void setFeemoney(java.math.BigDecimal feemoney){
        this.feemoney = feemoney;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  追索理由代码:RC00承兑人被依法宣告破产RC01承兑人因违法被责令终止活动（非拒付追索时填写）
     */
    @Column(name ="RCRSRSNCD",nullable=true,length=10)
    public String getRcrsrsncd(){
        return this.rcrsrsncd;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  追索理由代码:RC00承兑人被依法宣告破产RC01承兑人因违法被责令终止活动（非拒付追索时填写）
     */
    public void setRcrsrsncd(String rcrsrsncd){
        this.rcrsrsncd = rcrsrsncd;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  追索种类：RT00 拒付追索；RT01 非拒付追索
     */
    @Column(name ="RCRS_TP",nullable=true,length=4)
    public String getRcrsTp(){
        return this.rcrsTp;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  追索种类：RT00 拒付追索；RT01 非拒付追索
     */
    public void setRcrsTp(String rcrsTp){
        this.rcrsTp = rcrsTp;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  收款或付款确认人
     */
    @Column(name ="SIGNEROK",nullable=true,length=30)
    public String getSignerok(){
        return this.signerok;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  收款或付款确认人
     */
    public void setSignerok(String signerok){
        this.signerok = signerok;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  银行接口代码
     */
    @Column(name ="BIF_CODE",nullable=true,length=10)
    public String getBifCode(){
        return this.bifCode;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  银行接口代码
     */
    public void setBifCode(String bifCode){
        this.bifCode = bifCode;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务申请人类别RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司
     */
    @Column(name ="REQ_ROLE",nullable=true,length=12)
    public String getReqRole(){
        return this.reqRole;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务申请人类别RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司
     */
    public void setReqRole(String reqRole){
        this.reqRole = reqRole;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务申请人名称
     */
    @Column(name ="REQ_NM",nullable=true,length=60)
    public String getReqNm(){
        return this.reqNm;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务申请人名称
     */
    public void setReqNm(String reqNm){
        this.reqNm = reqNm;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务申请人组织机构代码
     */
    @Column(name ="REQ_CMONID",nullable=true,length=32)
    public String getReqCmonid(){
        return this.reqCmonid;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务申请人组织机构代码
     */
    public void setReqCmonid(String reqCmonid){
        this.reqCmonid = reqCmonid;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务申请人账号
     */
    @Column(name ="REQ_ACCT_ID",nullable=true,length=32)
    public String getReqAcctId(){
        return this.reqAcctId;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务申请人账号
     */
    public void setReqAcctId(String reqAcctId){
        this.reqAcctId = reqAcctId;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务申请人开户行号
     */
    @Column(name ="REQ_ACCTSVCR",nullable=true,length=12)
    public String getReqAcctsvcr(){
        return this.reqAcctsvcr;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务申请人开户行号
     */
    public void setReqAcctsvcr(String reqAcctsvcr){
        this.reqAcctsvcr = reqAcctsvcr;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务申请人承接行号
     */
    @Column(name ="REQ_ACCTSVCR_AG",nullable=true,length=12)
    public String getReqAcctsvcrAg(){
        return this.reqAcctsvcrAg;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务申请人承接行号
     */
    public void setReqAcctsvcrAg(String reqAcctsvcrAg){
        this.reqAcctsvcrAg = reqAcctsvcrAg;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务接收人类别：RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司
     */
    @Column(name ="REC_ROLE",nullable=true,length=12)
    public String getRecRole(){
        return this.recRole;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务接收人类别：RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司
     */
    public void setRecRole(String recRole){
        this.recRole = recRole;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务接收人名称
     */
    @Column(name ="REC_NM",nullable=true,length=60)
    public String getRecNm(){
        return this.recNm;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务接收人名称
     */
    public void setRecNm(String recNm){
        this.recNm = recNm;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务接收人组织机构代码
     */
    @Column(name ="REC_CMONID",nullable=true,length=32)
    public String getRecCmonid(){
        return this.recCmonid;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务接收人组织机构代码
     */
    public void setRecCmonid(String recCmonid){
        this.recCmonid = recCmonid;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务接收人账号
     */
    @Column(name ="REC_ACCT_ID",nullable=true,length=32)
    public String getRecAcctId(){
        return this.recAcctId;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务接收人账号
     */
    public void setRecAcctId(String recAcctId){
        this.recAcctId = recAcctId;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务接收人开户行号
     */
    @Column(name ="REC_ACCTSVCR",nullable=true,length=12)
    public String getRecAcctsvcr(){
        return this.recAcctsvcr;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务接收人开户行号
     */
    public void setRecAcctsvcr(String recAcctsvcr){
        this.recAcctsvcr = recAcctsvcr;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务接收人承接行号
     */
    @Column(name ="REC_ACCTSVCR_AG",nullable=true,length=12)
    public String getRecAcctsvcrAg(){
        return this.recAcctsvcrAg;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  业务接收人承接行号
     */
    public void setRecAcctsvcrAg(String recAcctsvcrAg){
        this.recAcctsvcrAg = recAcctsvcrAg;
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
    @Column(name ="ADR")
    public String getAdr() {
        return adr;
    }
    public void setAdr(String adr) {
        this.adr = adr;
    }
    @Transient
    public Integer getBillFlag() {
        return billFlag;
    }
    public void setBillFlag(Integer billFlag) {
        this.billFlag = billFlag;
    }
    @Column(name ="STTLMMK")
    public String getSttlmmk() {
        return sttlmmk;
    }
    public void setSttlmmk(String sttlmmk) {
        this.sttlmmk = sttlmmk;
    }
    /**
     * 是否隐藏
     * @return 1是0否
     */
    @Column(name ="IS_HIDE")
    public String getIsHide() {
        return isHide;
    }
    /**
     * 是否隐藏
     * @param isHide 1是0否
     */
    public void setIsHide(String isHide) {
        this.isHide = isHide;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer 10收票登记11背书登记12质押登记14贴现登记15归集收入16上划收入17下拨收入
     */
    @Column(name = "BILL_SOURCE", nullable = true, length = 10)
    public String getBillSource() {
        return this.billSource;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer
     *             10收票登记11背书登记12质押登记14贴现登记15归集收入16上划收入17下拨收入
     */
    public void setBillSource(String billSource) {
        this.billSource = billSource;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOrigNo() {
        return origNo;
    }

    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getBillMoney() {
        return billMoney;
    }

    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }

    @Override
    public String toString() {
        return "AbAccrecords [vid=" + vid + ", vvid=" + vvid + ", recid=" + recid + ", trid=" + trid + ", lastid="
                + lastid + ", lastbillcode=" + lastbillcode + ", validSign=" + validSign + ", isHide=" + isHide
                + ", status=" + status + ", optStatus=" + optStatus + ", sendSign=" + sendSign + ", returnCode="
                + returnCode + ", returnMsg=" + returnMsg + ", briefId=" + briefId + ", insideSing=" + insideSing
                + ", enterStockNew=" + enterStockNew + ", enterStockOld=" + enterStockOld + ", bailType=" + bailType
                + ", recallType=" + recallType + ", bankendrsmtmk=" + bankendrsmtmk + ", applyDate=" + applyDate
                + ", businessDate=" + businessDate + ", rcrsokmoney=" + rcrsokmoney + ", cnAccrecords=" + cnAccrecords
                + ", signUpFlag=" + signUpFlag + ", abs=" + abs + ", rmk=" + rmk + ", creditCode=" + creditCode
                + ", budgetCode=" + budgetCode + ", bailAcc=" + bailAcc + ", bailmoney=" + bailmoney + ", bailpercent="
                + bailpercent + ", bailrate=" + bailrate + ", feemoney=" + feemoney + ", rcrsrsncd=" + rcrsrsncd
                + ", rcrsTp=" + rcrsTp + ", signerok=" + signerok + ", bifCode=" + bifCode + ", reqRole=" + reqRole
                + ", reqNm=" + reqNm + ", reqCmonid=" + reqCmonid + ", reqAcctId=" + reqAcctId + ", reqAcctsvcr="
                + reqAcctsvcr + ", reqAcctsvcrAg=" + reqAcctsvcrAg + ", recRole=" + recRole + ", recNm=" + recNm
                + ", recCmonid=" + recCmonid + ", recAcctId=" + recAcctId + ", recAcctsvcr=" + recAcctsvcr
                + ", recAcctsvcrAg=" + recAcctsvcrAg + ", createName=" + createName + ", createBy=" + createBy
                + ", createDate=" + createDate + ", updateName=" + updateName + ", updateBy=" + updateBy
                + ", updateDate=" + updateDate + ", adr=" + adr + ", cdtRatgs=" + cdtRatgs + ", cdtRatgGgcy="
                + cdtRatgGgcy + ", cdtRatgDuedt=" + cdtRatgDuedt + ", billFlag=" + billFlag + ", sttlmmk=" + sttlmmk
                + ", busGlobalId=" + busGlobalId + ", billSource=" + billSource + ", vouInfo=" + vouInfo
                + "]";
    }
}
