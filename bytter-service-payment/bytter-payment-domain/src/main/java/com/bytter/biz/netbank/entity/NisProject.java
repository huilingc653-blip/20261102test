package com.bytter.biz.netbank.entity;
import com.bytter.core.base.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author shenc
 * @version 1.0
 * @Title
 * @Description: 项目维护管理中的项目列表
 * @date 2018/5/14 16:06
 */
@ApiModel("项目维护管理中的项目")
@Entity
@Table(name = "NIS_PROJECT", schema = "")
public class NisProject extends IdEntity implements java.io.Serializable {
    /**
     * 项目代码
     */
    @ApiModelProperty("项目代码")
    private String projectCode;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String projectName;

    /**
     * 收支标志
     */
    @ApiModelProperty("收支标志")
    private String projectCdSign;

    /**
     * 匹配属性
     */
    @ApiModelProperty("匹配属性")
    private String matchAttr;

    /**
     * 匹配内容
     */
    @ApiModelProperty("匹配内容")
    private String matchContent;

    /**
     * 匹配关系
     */
    @ApiModelProperty("匹配关系")
    private String matchRelation;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", example = "0")
    private Integer projectSort;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String rmk;

    /**
     * 创建人登录名称
     */
    @ApiModelProperty("创建人登录名称")
    private String createBy;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private java.util.Date createDate;

    /**
     * 更新人登录名称
     */
    @ApiModelProperty("更新人登录名称")
    private String updateBy;

    /**
     * updateDate
     */
    @ApiModelProperty("更新日期")
    private java.util.Date updateDate;
    @Column(name = "PROJECT_CODE", nullable = false, length = 10)
    public String getProjectCode() {
        return projectCode;
    }
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
    @Column(name = "PROJECT_NAME", nullable = false, length = 60)
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    @Column(name = "PROJECT_CDSIGN", nullable = false, length = 1)
    public String getProjectCdSign() {
        return projectCdSign;
    }
    public void setProjectCdSign(String projectCdSign) {
        this.projectCdSign = projectCdSign;
    }
    @Column(name = "MATCH_ATTR", nullable = false, length = 50)
    public String getMatchAttr() {
        return matchAttr;
    }
    public void setMatchAttr(String matchAttr) {
        this.matchAttr = matchAttr;
    }
    @Column(name = "MATCH_CONTENT", nullable = true, length = 4000)
    public String getMatchContent() {
        return matchContent;
    }
    public void setMatchContent(String matchContent) {
        this.matchContent = matchContent;
    }
    @Column(name = "MATCH_RELATION", nullable = false, length = 1)
    public String getMatchRelation() {
        return matchRelation;
    }
    public void setMatchRelation(String matchRelation) {
        this.matchRelation = matchRelation;
    }
    @Column(name = "PROJECT_SORT", nullable = true, length = 10)
    public Integer getProjectSort() {
        return projectSort;
    }
    public void setProjectSort(Integer projectSort) {
        this.projectSort = projectSort;
    }
    @Column(name = "RMK", nullable = true, length = 400)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return this.createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true)
    public java.util.Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return this.updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true)
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
}
