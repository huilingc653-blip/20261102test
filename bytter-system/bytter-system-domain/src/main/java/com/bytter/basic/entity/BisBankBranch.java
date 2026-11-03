package com.bytter.basic.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: 办理机构表
 * @date 2022-12-12 19:06:25
 */
@Entity
@Table(name = "BIS_BANK_BRANCH")
public class BisBankBranch implements java.io.Serializable {
    /**
     * 办理机构代码
     */
    @Id
    @Column(name = "BRANCH_CODE", unique = true, length = 32)
    private String branchCode;
    /**
     * 办理机构名称
     */
    @Column(name = "BRANCH_NAME", nullable = true)
    private String branchName;
    /**
     * 状态 1-启用 0-禁用
     */
    @Column(name = "STATUS", nullable = true)
    private String status;
    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME", nullable = true)
    private String createTime;
    public String getBranchCode() {
        return branchCode;
    }
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
    public String getBranchName() {
        return branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
