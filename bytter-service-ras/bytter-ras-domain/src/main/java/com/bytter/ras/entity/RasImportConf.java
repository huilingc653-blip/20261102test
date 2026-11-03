package com.bytter.ras.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bytter.core.base.entity.IdEntity;
/**
 * 
* @Title:  营收稽核 导入配置  
* @Package com.bytter.ras.entity   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-15 下午2:50:08   
* @version V1.0
 */
@Entity
@Table(name = "RAS_IMPORT_CONF", schema = "")
@SuppressWarnings("serial")
public class RasImportConf extends IdEntity implements java.io.Serializable{
	
	/**类型：0营收1实收*/
	private String  type;
	/**数据来源:002支付宝003微信004银联POS机005收钱吧006其它格式*/
	private String  dateSource;
	/**缴款方式:002支付宝003微信004银联POS机005收钱吧006其它 排除001现金*/
	private String  payType;
	/**文件类型xls xlsx csv txt 营收不存在txt*/
	private String  fileType;  
	/**门店ID*/
	private Integer startLine;
	/**是否有分隔符:0否1是*/
	private String  isFormat;
	/**txt分隔符*/
	private String  txtFormat;
	
	/**创建人名称*/
    private String  createName;  
    /**创建人登录名称*/
    private String  createBy;   
    /**创建日期*/
    private Date  createDate;   
    /**更新人名称*/
    private String  updateName;   
    /**更新人登录名称*/
    private String  updateBy;     
    /**更新日期*/
    private Date  updateDate;
    
	@Column(name ="TYPE",nullable=true,length=10)
    public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name ="PAY_TYPE",nullable=true,length=10)
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	@Column(name ="FILE_TYPE",nullable=true,length=10)
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	@Column(name ="START_LINE",nullable=true,length=6)
	public Integer getStartLine() {
		return startLine;
	}
	public void setStartLine(Integer startLine) {
		this.startLine = startLine;
	}
	@Column(name ="IS_FORMAT",nullable=true,length=10)
	public String getIsFormat() {
		return isFormat;
	}
	public void setIsFormat(String isFormat) {
		this.isFormat = isFormat;
	}
	@Column(name ="TXT_FORMAT",nullable=true,length=10)
	public String getTxtFormat() {
		return txtFormat;
	}
	public void setTxtFormat(String txtFormat) {
		this.txtFormat = txtFormat;
	}
	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Column(name ="CREATE_DATE",nullable=true)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	@Column(name ="UPDATE_DATE",nullable=true)
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Column(name ="DATE_SOURCE",nullable=true,length=10)
	public String getDateSource() {
		return dateSource;
	}
	public void setDateSource(String dateSource) {
		this.dateSource = dateSource;
	}   
}
