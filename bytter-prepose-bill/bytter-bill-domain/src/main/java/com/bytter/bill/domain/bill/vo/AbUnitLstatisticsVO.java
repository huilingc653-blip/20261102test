package com.bytter.bill.domain.bill.vo;
import java.math.BigDecimal;
/**   
 * @Description: 单位票据统计表VO
 * @author onlineGenerator
 * @date 2017-06-21 15:36:23
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class AbUnitLstatisticsVO implements java.io.Serializable {
	
	/**业务类型代码*/
	private String busType;
	/**业务类型名称*/
	private String busTypeName;
	/**总计数据 所有总数*/
	private Integer zCount=0;
	/**总计数据 在途总数*/
	private Integer zztCount=0;
	/**总计数据 总金额*/
	private BigDecimal zMoney=new BigDecimal(0); 
	
	/**纸票合计 所有总数*/
	private Integer zpCount=0;
	/**纸票合计 在途总数*/
	private Integer zpztCount=0;
	/**纸票合计 总金额*/
	private BigDecimal zpMoney=new BigDecimal(0); 
	
	/**电票合计 所有总数*/
	private Integer dpCount=0;
	/**电票合计 在途总数*/
	private Integer dpztCount=0;
	/**电票合计 总金额*/
	private BigDecimal dpMoney=new BigDecimal(0); 
	
	/**纸票银票合计 所有总数*/
	private Integer zpypCount=0;
	/**纸票银票合计 在途总数*/
	private Integer zpypztCount=0;
	/**纸票银票合计 总金额*/
	private BigDecimal zpypMoney=new BigDecimal(0); 
	
	/**纸票商票合计 所有总数*/
	private Integer zpspCount=0;
	/**纸票商票合计 在途总数*/
	private Integer zpspztCount=0;
	/**纸票商票合计 总金额*/
	private BigDecimal zpspMoney=new BigDecimal(0); 
	
	/**电票银票合计 所有总数*/
	private Integer dpypCount=0;
	/**电票银票合计 在途总数*/
	private Integer dpypztCount=0;
	/**电票银票合计 总金额*/
	private BigDecimal dpypMoney=new BigDecimal(0); 
	
	/**电票商票合计 所有总数*/
	private Integer dpspCount=0;
	/**电票商票合计 在途总数*/
	private Integer dpspztCount=0;
	/**电票商票合计 总金额*/
	private BigDecimal dpspMoney=new BigDecimal(0); 
	
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getBusTypeName() {
		return busTypeName;
	}
	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
	}
	public Integer getzCount() {
		return zCount;
	}
	public void setzCount(Integer zCount) {
		this.zCount = zCount;
	}
	public Integer getZztCount() {
		return zztCount;
	}
	public void setZztCount(Integer zztCount) {
		this.zztCount = zztCount;
	}
	public BigDecimal getzMoney() {
		return zMoney;
	}
	public void setzMoney(BigDecimal zMoney) {
		this.zMoney = zMoney;
	}
	public Integer getZpCount() {
		return zpCount;
	}
	public void setZpCount(Integer zpCount) {
		this.zpCount = zpCount;
	}
	public Integer getZpztCount() {
		return zpztCount;
	}
	public void setZpztCount(Integer zpztCount) {
		this.zpztCount = zpztCount;
	}
	public BigDecimal getZpMoney() {
		return zpMoney;
	}
	public void setZpMoney(BigDecimal zpMoney) {
		this.zpMoney = zpMoney;
	}
	public Integer getDpCount() {
		return dpCount;
	}
	public void setDpCount(Integer dpCount) {
		this.dpCount = dpCount;
	}
	public Integer getDpztCount() {
		return dpztCount;
	}
	public void setDpztCount(Integer dpztCount) {
		this.dpztCount = dpztCount;
	}
	public BigDecimal getDpMoney() {
		return dpMoney;
	}
	public void setDpMoney(BigDecimal dpMoney) {
		this.dpMoney = dpMoney;
	}
	public Integer getZpypCount() {
		return zpypCount;
	}
	public void setZpypCount(Integer zpypCount) {
		this.zpypCount = zpypCount;
	}
	public Integer getZpypztCount() {
		return zpypztCount;
	}
	public void setZpypztCount(Integer zpypztCount) {
		this.zpypztCount = zpypztCount;
	}
	public BigDecimal getZpypMoney() {
		return zpypMoney;
	}
	public void setZpypMoney(BigDecimal zpypMoney) {
		this.zpypMoney = zpypMoney;
	}
	public Integer getZpspCount() {
		return zpspCount;
	}
	public void setZpspCount(Integer zpspCount) {
		this.zpspCount = zpspCount;
	}
	public Integer getZpspztCount() {
		return zpspztCount;
	}
	public void setZpspztCount(Integer zpspztCount) {
		this.zpspztCount = zpspztCount;
	}
	public BigDecimal getZpspMoney() {
		return zpspMoney;
	}
	public void setZpspMoney(BigDecimal zpspMoney) {
		this.zpspMoney = zpspMoney;
	}
	public Integer getDpypCount() {
		return dpypCount;
	}
	public void setDpypCount(Integer dpypCount) {
		this.dpypCount = dpypCount;
	}
	public Integer getDpypztCount() {
		return dpypztCount;
	}
	public void setDpypztCount(Integer dpypztCount) {
		this.dpypztCount = dpypztCount;
	}
	public BigDecimal getDpypMoney() {
		return dpypMoney;
	}
	public void setDpypMoney(BigDecimal dpypMoney) {
		this.dpypMoney = dpypMoney;
	}
	public Integer getDpspCount() {
		return dpspCount;
	}
	public void setDpspCount(Integer dpspCount) {
		this.dpspCount = dpspCount;
	}
	public Integer getDpspztCount() {
		return dpspztCount;
	}
	public void setDpspztCount(Integer dpspztCount) {
		this.dpspztCount = dpspztCount;
	}
	public BigDecimal getDpspMoney() {
		return dpspMoney;
	}
	public void setDpspMoney(BigDecimal dpspMoney) {
		this.dpspMoney = dpspMoney;
	}
	@Override
	public String toString() {
		return "AbUnitLstatisticsVO [busType=" + busType + ", busTypeName=" + busTypeName + ", zCount=" + zCount
				+ ", zztCount=" + zztCount + ", zMoney=" + zMoney + ", zpCount=" + zpCount + ", zpztCount=" + zpztCount
				+ ", zpMoney=" + zpMoney + ", dpCount=" + dpCount + ", dpztCount=" + dpztCount + ", dpMoney=" + dpMoney
				+ ", zpypCount=" + zpypCount + ", zpypztCount=" + zpypztCount + ", zpypMoney=" + zpypMoney
				+ ", zpspCount=" + zpspCount + ", zpspztCount=" + zpspztCount + ", zpspMoney=" + zpspMoney
				+ ", dpypCount=" + dpypCount + ", dpypztCount=" + dpypztCount + ", dpypMoney=" + dpypMoney
				+ ", dpspCount=" + dpspCount + ", dpspztCount=" + dpspztCount + ", dpspMoney=" + dpspMoney + "]";
	} 
	
}
