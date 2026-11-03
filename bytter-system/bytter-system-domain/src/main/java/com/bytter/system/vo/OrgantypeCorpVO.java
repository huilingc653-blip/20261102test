package com.bytter.system.vo;
import com.bytter.core.annotation.CellManage;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.persistence.tree.interfaces.TreeNode;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.constant.CorpConstant;
import java.io.Serializable;
import java.util.List;
/**
 * @author zhengg
 * @version V1.0
 * @Title: VO
 * @Description: 组织架构单位层级关联表VO
 * @date 2020-05-14 16:01:41
 */
@SuppressWarnings("serial")
public class OrgantypeCorpVO implements Serializable, TreeNode<OrgantypeCorpVO> {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 单位代码
     */
    private String corpCode;
    /**
     * 上级单位ID
     */
    private String parentId;
    /**
     * 组织架构id
     */
    private String orgtId;
    /**
     * 创建人ID
     */
    private String createId;
    /**
     * 创建日期
     */
    private java.util.Date createTime;
    /**
     * 更新人名称
     */
    private String updateId;
    /**
     * 更新日期
     */
    private java.util.Date updateTime;
    /**
     * 机构代码
     */
    private String code;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否有效，0停用，1启用（默认）
     */
    //@Dict(dictGroup = "SYS_ORGANTYPE_STATUS", inject = "statusName")
    private Integer status;
    private String statusName;
    //树使用字段
    private List<OrgantypeCorpVO> children;
    private Boolean expanded = true;
    private Boolean isLeaf;
    private Integer level;
    /**
     * 所有上级id
     */
    private String parentIds;
    private String parentName;
    /**
     * 子单位
     */
    private List<OrgantypeCorpVO> sonOrgtCorpVOCorpList;
    /**
     * 组织类型
     */
    //@Dict(dictGroup = "SYS_ORGANTYPE_CORP_TYPE", inject = "typeName")
    private Integer type;
    private String typeName;
    /**
     * *单位属性
     */
    @Dict(dictGroup = CorpConstant.CORP_ATTRIBUTE_TYPE_DICT, inject = "unitAttributeName")
    private String unitAttribute;
    /**
     * 单位属性名称
     */
    private String unitAttributeName;
    /**
     * 管理单元
     */
    //@CellManage(inject = "unitCmName")
    private String unitCm;
    /**
     * 管理单元名称
     */
    private String unitCmName;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  主键ID
     */
    public String getId() {
        return this.id;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  主键ID
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  单位ID
     */
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  单位ID
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  上级单位ID
     */
    public String getParentId() {
        return this.parentId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  上级单位ID
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  组织架构id
     */
    public String getOrgtId() {
        return this.orgtId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  组织架构id
     */
    public void setOrgtId(String orgtId) {
        this.orgtId = orgtId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建人ID
     */
    public String getCreateId() {
        return this.createId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建人ID
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */
    public java.util.Date getCreateTime() {
        return this.createTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建日期
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  更新人名称
     */
    public String getUpdateId() {
        return this.updateId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  更新人名称
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新日期
     */
    public java.util.Date getUpdateTime() {
        return this.updateTime;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新日期
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Boolean getExpanded() {
        return expanded;
    }
    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
    @Override
    public Integer getLevel() {
        return level;
    }
    @Override
    public void setLevel(Integer level) {
        this.level = level;
    }
    public Boolean getIsLeaf() {
        return isLeaf;
    }
    public void setIsLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
    @Override
    public void setChildren(List<OrgantypeCorpVO> object) {
        this.children = object;
    }
    @Override
    public List getChildren() {
        return children;
    }
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
    @Override
    public String getPath() {
        return getParentIds();
    }
    @Override
    public String getSplit() {
        return CommonConstant.COMMA_SPLIT;
    }
    public List<OrgantypeCorpVO> getSonOrgtCorpVOCorpList() {
        return sonOrgtCorpVOCorpList;
    }
    public void setSonOrgtCorpVOCorpList(List<OrgantypeCorpVO> sonOrgtCorpVOCorpList) {
        this.sonOrgtCorpVOCorpList = sonOrgtCorpVOCorpList;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public String getUnitAttribute() {
        return unitAttribute;
    }
    public void setUnitAttribute(String unitAttribute) {
        this.unitAttribute = unitAttribute;
    }
    public String getUnitCm() {
        return unitCm;
    }
    public void setUnitCm(String unitCm) {
        this.unitCm = unitCm;
    }
    public String getUnitAttributeName() {
        return unitAttributeName;
    }
    public void setUnitAttributeName(String unitAttributeName) {
        this.unitAttributeName = unitAttributeName;
    }
    public String getUnitCmName() {
        return unitCmName;
    }
    public void setUnitCmName(String unitCmName) {
        this.unitCmName = unitCmName;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    @Override
    public String toString() {
        return "OrgantypeCorpVO{" +
                "id='" + id + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpName='" + corpName + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", parentId='" + parentId + '\'' +
                ", orgtId='" + orgtId + '\'' +
                ", createId='" + createId + '\'' +
                ", createTime=" + createTime +
                ", updateId='" + updateId + '\'' +
                ", updateTime=" + updateTime +
                ", code='" + code + '\'' +
                ", sort=" + sort +
                ", status=" + status +
                ", statusName='" + statusName + '\'' +
                ", children=" + children +
                ", expanded=" + expanded +
                ", isLeaf=" + isLeaf +
                ", level=" + level +
                ", parentIds='" + parentIds + '\'' +
                ", parentName='" + parentName + '\'' +
                ", sonOrgtCorpVOCorpList=" + sonOrgtCorpVOCorpList +
                ", type=" + type +
                ", typeName='" + typeName + '\'' +
                ", unitAttribute='" + unitAttribute + '\'' +
                ", unitAttributeName='" + unitAttributeName + '\'' +
                ", unitCm='" + unitCm + '\'' +
                ", unitCmName='" + unitCmName + '\'' +
                '}';
    }
}
