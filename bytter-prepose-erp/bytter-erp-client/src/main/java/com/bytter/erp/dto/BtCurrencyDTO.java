package com.bytter.erp.dto;
import com.bytter.core.annotation.Currency;
import com.bytter.core.utils.Base64Util;
import com.bytter.core.utils.EmptyUtils;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 货币资料维护DTO
 * @date 2017-06-26
 */
public class BtCurrencyDTO implements Serializable {
	@Currency(inject = "curName")
	private String id;
	/**
	 * 币别编码
	 */
	private String curCode;
	/**
	 * 币别名称
	 */
	private String curName;
	/**
	 * 汇率
	 */
	private BigDecimal curExrate;
	/**
	 * 是否本位币
	 */
	private String naturalSign;
	/**
	 * 币别符号
	 */
	private String curSign;
	private String ceshiclj;
	private Double ceshiclj2;
	private Integer ceshiclj3;
	private Long ceshiclj4;
	/**
	 * 英文简码
	 */
	private String englishCode;
	private BtCurrencyDTO btCurrencyDTO;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public BigDecimal getCurExrate() {
		return curExrate;
	}
	public void setCurExrate(BigDecimal curExrate) {
		this.curExrate = curExrate;
	}
	public String getNaturalSign() {
		return naturalSign;
	}
	public void setNaturalSign(String naturalSign) {
		this.naturalSign = naturalSign;
	}
	public String getCurSign() {
		if (!EmptyUtils.isBlank(this.curSign)) {
			//todo
			return new String(java.util.Base64.getEncoder().encode(this.curSign.getBytes()));
		} else {
			return this.curSign;
		}
	}
	public void setCurSign(String curSign) {
		//todo
		if (!EmptyUtils.isBlank(curSign)) {
			this.curSign = java.util.Base64.getEncoder().encodeToString(curSign.getBytes());
		} else {
			this.curSign = curSign;
		}
	}
	public String getCeshiclj() {
		return ceshiclj;
	}
	public void setCeshiclj(String ceshiclj) {
		this.ceshiclj = ceshiclj;
	}
	public Double getCeshiclj2() {
		return ceshiclj2;
	}
	public void setCeshiclj2(Double ceshiclj2) {
		this.ceshiclj2 = ceshiclj2;
	}
	public Integer getCeshiclj3() {
		return ceshiclj3;
	}
	public void setCeshiclj3(Integer ceshiclj3) {
		this.ceshiclj3 = ceshiclj3;
	}
	public Long getCeshiclj4() {
		return ceshiclj4;
	}
	public void setCeshiclj4(Long ceshiclj4) {
		this.ceshiclj4 = ceshiclj4;
	}
	public String getEnglishCode() {
		return englishCode;
	}
	public void setEnglishCode(String englishCode) {
		this.englishCode = englishCode;
	}
	public BtCurrencyDTO getBtCurrencyDTO() {
		return btCurrencyDTO;
	}
	public void setBtCurrencyDTO(BtCurrencyDTO btCurrencyDTO) {
		this.btCurrencyDTO = btCurrencyDTO;
	}
	@Override
	public String toString() {
		return "BtCurrencyDTO{" +
				"id='" + id + '\'' +
				", curCode='" + curCode + '\'' +
				", curName='" + curName + '\'' +
				", curExrate=" + curExrate +
				", naturalSign='" + naturalSign + '\'' +
				", curSign='" + curSign + '\'' +
				'}';
	}
}
