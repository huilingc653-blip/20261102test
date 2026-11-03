package com.bytter.pricing.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author luxy
 * @Title: DepositRate
 * @ProjectName T2_IDEA
 * @Description: 存款利率维护
 * @date 2019/5/23 9:03
 */
@Entity
@Table(name = "TB_LOAN_RATE_MAINTENCE", schema = "")
@SuppressWarnings("serial")
public class DepositRate extends IdEntity implements java.io.Serializable{
        /** 存款利率类型*/
        private String depositRateType;
        /**利率*/
        private BigDecimal rate;
        /**生效时间*/
        private Date timeOfTakeEffect;
        /**最近修改时间*/
        private Date timeOfLastModifier;
        /**状态*/
        private Integer status;
        /**打回说明*/
        private String returndeScribe;
        /**币别*/
        private String moneyType;
        /**创建人**/
        private String createBy;
        /**备注*/
        private String remark;
        /**复核人*/
        private String checkOp;
        /**利率种类 0-基准利率，1-基础利率*/
        private String referenceRateType;
        /**存款账户标识  1-内部账户存款 2-资金池账户存款 3-银行账户存款 */
        private String depositAccSign;
        /**利率上限*/
        private BigDecimal rateUpLimit;
        /**利率下限*/
        private BigDecimal rateDownLimit;
        /**
         *方法: 取得java.lang.String
         *@return: java.lang.String  存款利率类型
         */
        @Column(name ="CREDIT_PERIOD_ID")
        public String getDepositRateType() {
                return depositRateType;
        }
        /**
         *方法: 设置java.lang.String
         *@param: java.lang.String  存款利率类型
         */
        public void setDepositRateType(String depositRateType) {
                this.depositRateType = depositRateType;
        }
        /**
         *方法: 取得java.math.BigDecimal
         *@return: java.math.BigDecimal  利率
         */
        @Column(name ="RATE")
        public BigDecimal getRate(){
            return this.rate;
        }
        /**
         *方法: 设置java.math.BigDecimal
         *@param: java.math.BigDecimal  利率
         */
        public void setRate(BigDecimal rate){
            this.rate = rate;
        }
        /**
         *方法: 取得java.util.Date
         *@return:java.util.Date  生效时间
         */
        @Column(name ="TIME_OF_TAKE_EFFECT")
        public Date getTimeOfTakeEffect(){
            return this.timeOfTakeEffect;
        }
        /**
         *方法: 设置java.util.Date
         *@param: java.util.Date  生效时间
         */
        public void setTimeOfTakeEffect(Date timeOfTakeEffect){
            this.timeOfTakeEffect = timeOfTakeEffect;
        }
        /**
         *方法: 取得java.util.Date
         *@return: java.util.Date 最近修改时间
         */
        @Column(name ="TIME_OF_LAST_MODIFIER")
        public Date getTimeOfLastModifier(){
            return this.timeOfLastModifier;
        }
        /**
         *方法: 设置java.util.Date
         *@param: java.util.Date  最近修改时间
         */
        public void setTimeOfLastModifier(Date timeOfLastModifier){
            this.timeOfLastModifier = timeOfLastModifier;
        }
        /**
         *方法: 取得java.lang.Integer
         *@return: java.lang.Integer  状态
         */
        @Column(name ="STATUS")
        public Integer getStatus(){
            return this.status;
        }
        /**
         *方法: 设置java.lang.Integer
         *@param: java.lang.Integer  状态
         */
        public void setStatus(Integer status){
            this.status = status;
        }
        /**
         *方法: 取得java.lang.String
         *@return: java.lang.String  打回说明
         */
        @Column(name ="RETURNDE_SCRIBE")
        public String getReturndeScribe(){
            return this.returndeScribe;
        }
        /**
         *方法: 设置java.lang.String
         *@param: java.lang.String  打回说明
         */
        public void setReturndeScribe(String returndeScribe){
            this.returndeScribe = returndeScribe;
        }
        /**
         *方法: 取得java.lang.String
         *@return: java.lang.String  币别
         */
        @Column(name ="MONEY_TYPE")
        public String getMoneyType(){
            return this.moneyType;
        }
        /**
         *方法: 设置java.lang.String
         *@param: java.lang.String  币别
         */
        public void setMoneyType(String moneyType){
            this.moneyType = moneyType;
        }
        /**
         *方法: 取得java.lang.String
         *@return: java.lang.String  创建人
         */
        @Column(name ="CREATE_BY")
        public String getCreateBy() {
            return createBy;
        }
        /**
         *方法: 设置java.lang.String
         *@return: java.lang.String  创建人
         */
        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }
        /**
         *方法: 取得java.lang.String
         *@return: java.lang.String  备注
         */
        @Column(name ="REMARK")
        public String getRemark() {
                return remark;
        }
        /**
         *方法: 设置java.lang.String
         *@return: java.lang.String  备注
         */
        public void setRemark(String remark) {
                this.remark = remark;
        }
        /**
         *方法: 取得java.lang.String
         *@return: java.lang.String  复核人
         */
        @Column(name ="CHECK_OP")
        public String getCheckOp() {
                return checkOp;
        }
        /**
         *方法: 设置java.lang.String
         *@return: java.lang.String 复核人
         */
        public void setCheckOp(String checkOp) {
                this.checkOp = checkOp;
        }
        /**
         *方法: 取得java.lang.String
         *@return: java.lang.String  利率类型
         */
        @Column(name ="REFERENCE_RATE_TYPE")
        public String getReferenceRateType() {
                return referenceRateType;
        }
        /**
         *方法: 设置java.lang.String
         *@return: java.lang.String  利率类型
         */
        public void setReferenceRateType(String referenceRateType) {
                this.referenceRateType = referenceRateType;
        }
        /**
         *方法: 取得java.lang.String
         *@return: java.lang.String  存款账户标识
         */
        @Column(name ="DEPOSIT_ACC_SIGN")
        public String getDepositAccSign() {
                return depositAccSign;
        }
        /**
         *方法: 设置java.lang.String
         *@return: java.lang.String  存款账户标识
         */
        public void setDepositAccSign(String depositAccSign) {
                this.depositAccSign = depositAccSign;
        }
        /**
         *方法: 取得Integer
         *@return: Integer  利率上限
         */
        @Column(name ="RATE_UP_LIMIT")
        public BigDecimal getRateUpLimit(){
                return this.rateUpLimit;
        }
        /**
         *方法: 设置Integer
         *@param: Integer  利率上限
         */
        public void setRateUpLimit(BigDecimal rateUpLimit){
                this.rateUpLimit = rateUpLimit;
        }
        /**
         *方法: 取得Integer
         *@return: Integer  利率下限
         */
        @Column(name ="RATE_DOWN_LIMIT")
        public BigDecimal getRateDownLimit(){
                return this.rateDownLimit;
        }
        /**
         *方法: 设置Integer
         *@param: Integer  利率下限
         */
        public void setRateDownLimit(BigDecimal rateDownLimit){
                this.rateDownLimit = rateDownLimit;
        }
}
