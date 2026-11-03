package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 报表格式定义临时数据Bean
 * 
 * @author liyh
 *
 */
public class FcReportFormatBean implements Serializable {
	/**
	 * 行号（计算顺序）
	 */
	private Integer lineNo;
	/**
	 * 左右
	 */
	private String direction;
	/**
	 * 项目名
	 */
	private String item;
	/**
	 * 行次（报表显示顺序）
	 */
	private Integer orderNo;
	/**
	 * 正负
	 */
	private Integer rect;
	/**
	 * 科目
	 */
	private String subject;
	/**
	 * 公式
	 */
	private String formula;
	/**
	 * 网点
	 */
	private String netCode;
	/**
	 * 是否年初
	 */
	private String initYear;
	/**
	 * 年余额
	 */
	private BigDecimal yearBalance;
	/**
	 * 余额
	 */
	private BigDecimal balance;
	/**
     * 计算标识
	 */
	private boolean calcSign = false;
	public Integer getLineNo() {
		return lineNo;
	}
	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getRect() {
		return rect;
	}
	public void setRect(Integer rect) {
		this.rect = rect;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	public String getInitYear() {
		return initYear;
	}
	public void setInitYear(String initYear) {
		this.initYear = initYear;
	}
	public BigDecimal getYearBalance() {
		if (null == yearBalance) {
			yearBalance = BigDecimal.ZERO;
		}
		return yearBalance;
	}
	public void setYearBalance(BigDecimal yearBalance) {
		this.yearBalance = yearBalance;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
    public boolean isCalcSign() {
        return calcSign;
    }
    public void setCalcSign(boolean calcSign) {
        this.calcSign = calcSign;
    }
    //比较两个bean是否相同的orderNo
	@Override
	public boolean equals(Object obj){
		if(orderNo.equals(((FcReportFormatBean)obj).getOrderNo())){
			return true;
		}else{
			return false;
		}
		
	}
}
