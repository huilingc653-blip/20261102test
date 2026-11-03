package com.bytter.guarantee.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 产品类型映射主表Entity
 * @Description: CON_PD_MAP合产品类型映射主表
 * @date 2019-11-04
 */
@Entity
@Table(name = "CON_PD_MAP", schema = "")
@SuppressWarnings("serial")
public class ConPdMap extends IdEntity implements java.io.Serializable {
    /**
     * 合同产品类型代码: CON_PD_INNERLOAN内部贷款、 CON_PD_ENTRUSTLOAN委托贷款、 CON_PD_BANKBORROW银行借款、 CON_PD_RAT授信管理
     */
    private String pdCode;
    /**
     * 合同类型ID
     */
    private String typeId;
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
    /**
     * 方法: 取得 合同产品类型代码
     *
     * @return: 合同产品类型代码
     */
    @Column(name = "PD_CODE", nullable = true, length = 32)
    public String getPdCode() {
        return pdCode;
    }
    /**
     * 设置 合同产品类型代码
     */
    public void setPdCode(String pdCode) {
        this.pdCode = pdCode;
    }
    /**
     * 方法: 取得 合同类型ID
     *
     * @return: 合同类型ID
     */
    @Column(name = "TYPE_ID", nullable = true, length = 32)
    public String getTypeId() {
        return typeId;
    }
    /**
     * 合同类型ID
     */
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    /**
     * 方法: 取得 创建人id
     *
     * @return: 创建人名称
     */
    @Column(name = "CREATE_ID", nullable = true, length = 32)
    public String getCreateId() {
        return createId;
    }
    /**
     * 设置 创建人id
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
     * 方法: 取得 更新人id
     *
     * @return: 更新人id
     */
    @Column(name = "UPDATE_ID", nullable = true, length = 32)
    public String getUpdateId() {
        return updateId;
    }
    /**
     * 设置 更新人id
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
    @Override
    public String toString() {
        return "ConPdMap{" + "pdCode='" + pdCode + '\'' + ", typeId='" + typeId + '\'' + ", createId='" + createId
                + '\'' + ", createTime=" + createTime + ", updateId='" + updateId + '\'' + ", updateTime=" + updateTime
                + '}';
    }
}
