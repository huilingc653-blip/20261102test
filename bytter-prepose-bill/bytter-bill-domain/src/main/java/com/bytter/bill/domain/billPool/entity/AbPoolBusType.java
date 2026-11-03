package com.bytter.bill.domain.billPool.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "AB_POOL_BUSTYPE", schema = "")
@SuppressWarnings("serial")
public class AbPoolBusType extends IdEntity implements java.io.Serializable{
	/**
     * 业务类型代码
     */
	private String bustypeCode;
	
	 /**
     * 业务类型名称
     */
	private String bustypeName;
	
	 /**
     * 上级业务类型
     */
	private String parentId;
	
	  /**
     * 状态：1启用、0禁用 默认禁用
     */
	private String validSign;
	
	 /**
     * 层级
     */
	private Integer bustypeLevel;
	
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
    
    
    @Column(name = "VALID_SIGN", nullable = true, length = 2)
 	public String getValidSign() {
 		return validSign;
 	}
 	public void setValidSign(String validSign) {
 		this.validSign = validSign;
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
    @Column(name = "BUSTYPE_LEVEL", nullable = true)
	public Integer getBustypeLevel() {
		return bustypeLevel;
	}
	public void setBustypeLevel(Integer bustypeLevel) {
		this.bustypeLevel = bustypeLevel;
	}
	@Column(name = "BUSTYPE_CODE", nullable = true, length = 12)
	public String getBustypeCode() {
		return bustypeCode;
	}
	public void setBustypeCode(String bustypeCode) {
		this.bustypeCode = bustypeCode;
	}
	@Column(name = "BUSTYPE_NAME", nullable = true, length = 100)
	public String getBustypeName() {
		return bustypeName;
	}
	public void setBustypeName(String bustypeName) {
		this.bustypeName = bustypeName;
	}
	@Column(name = "PARENT_ID", nullable = true, length = 32)
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
}
