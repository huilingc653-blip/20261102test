package com.bytter.bill.domain.bill.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Title: Entity
 * @Description: 开票登记VO
 * @author luop
 * @date 2017-06-21 18:00:00
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Data
public class CmbCrlconVo implements java.io.Serializable {
	private String id;//varchar(32)  comment '主键id',
	private String acrcon;//varchar(100) comment '风险合同编号',
	private String connum;//varchar(100) comment '信贷合同编号',
	private String connam;//varchar(200) comment '合同文本号',
	private String grttyp;//varchar(60)  comment '担保物类型',
	private String conccy;//varchar(30)  comment '合同币种',
	private String conbrn;//varchar(100) comment '合同机构号',
	private String bustyp;//varchar(60)  comment '业务品种',
	private String grtccy;//varchar(30)  comment '担保物币种',
	private String zbrdnam;//varchar(600) comment '合同机构行名',
	private String zconbrd;//varchar(600) comment '合同机构行号',
	private BigDecimal grtave;//decimal(17, 2) comment '担保物可用金额',
	private BigDecimal conamt;//decimal(17, 2) comment '合同金额',
	private BigDecimal conbal;//decimal(17, 2) comment '合同业务余额',
	private BigDecimal conave;//decimal(17, 2) comment '合同可用额度',
	private BigDecimal grtamt;//decimal(17, 2) comment '担保物登记金额',
	private BigDecimal grtper;//decimal(17, 2) comment '合同保证金比例',
	@BtCompositeSearch(fieldName = "creditlineid")
	private String creditlineid;//cvm编号
}
