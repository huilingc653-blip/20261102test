package com.bytter.bill.domain.billPool.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * 
* @Title:  票据池质权人 
* @Package com.bytter.biz.billPool.entity   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-12-30 下午5:30:29   
* @version V1.0
 */
@Entity
@Table(name = "AB_POOL_PLEDGEE", schema = "")
@SuppressWarnings("serial")
public class AbPoolPledgee extends IdEntity implements java.io.Serializable{
	/**
     * 票据池ID
     */
    private String bpId;
    
    /**
     * 质权人账号
     */
    private String bankAcc;
    
    /**
     * 质权人名称
     */
    private String accName;   
    /**
     * 质权人联行号
     */
    private String bankNo;
    /**
     * 质权人银行名称
     */
    private String bankName;
    
    /**
     * 状态：1启用、0禁用 默认禁用
     */
	private String validSign;
	/**
     * 备注
     */
	private String rmk;
	/**
     * 创建人id
     */
    private String createId;
    /**
     * 创建日期
     */
    private Date createTime;
	 /**
     * 更新人id
     */
    private String updateId;
    /**
     * 更新日期
     */
    private Date updateTime;
    
    @Column(name = "BP_ID", nullable = true, length = 32)
	public String getBpId() {
		return bpId;
	}
	public void setBpId(String bpId) {
		this.bpId = bpId;
	}
    @Column(name = "BANK_ACC", nullable = true, length = 50)
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
    @Column(name = "ACC_NAME", nullable = true, length = 200)
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
    @Column(name = "BANK_NO", nullable = true, length = 50)
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
    @Column(name = "BANK_NAME", nullable = true, length = 200)
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
    @Column(name = "VALID_SIGN", nullable = true, length = 2)
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
    @Column(name = "RMK", nullable = true, length = 200)
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	/**
     * 方法: 取得 创建人名称
     *
     * @return: 创建人名称
     */
    @Column(name = "CREATE_ID", nullable = true, length = 32)
    public String getCreateId() {
        return createId;
    }
    /**
     * 设置 创建人名称
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    /**
     * 方法: 取得 创建日期
     *
     * @return: 创建日期
     */
    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * 设置 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     * 方法: 取得 更新人名称
     *
     * @return: 更新人名称
     */
    @Column(name = "UPDATE_ID", nullable = true, length = 32)
    public String getUpdateId() {
        return updateId;
    }
    /**
     * 设置 更新人名称
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    /**
     * 方法: 取得 更新日期
     *
     * @return:更新日期
     */
    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return updateTime;
    }
    /**
     * 设置更新日期
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
