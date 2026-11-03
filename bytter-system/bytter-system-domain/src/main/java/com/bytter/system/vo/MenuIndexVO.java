package com.bytter.system.vo;
import com.bytter.core.base.persistence.tree.interfaces.TreeStructureAttribute;
import com.bytter.core.utils.StringUtils;
import java.io.Serializable;
import java.util.List;
/**
 * Created by zhoul on 2017/11/17.
 */
public class MenuIndexVO implements TreeStructureAttribute<MenuIndexVO>, Serializable {
    private String id;
    private String parentId;
    private String name;
    private String url;
    private Boolean expanded = true;
    private Boolean isLeaf;
    private Integer level;
    private String icon;
    private String oldIcon;
    private Integer isExtend;
    //全路径菜单名称
    private String absoluteName;
    //菜单IDs
    private String parentIds;
    //收藏标志
    private String collectFlag;
    //顶级菜单ID
    private String topParentId;
    //顶级菜单名称
    private String topParentName;
    private List<MenuIndexVO> children;
    public String getUrl() {
        if (StringUtils.isEmpty(url)) {//避免脚本菜单有空字符串的url页面跳转问题
            return null;
        }
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public List<MenuIndexVO> getChildren() {
        return children;
    }
    @Override
    public void setChildren(List<MenuIndexVO> children) {
        this.children = children;
    }
    @Override
    public Boolean getExpanded() {
        return expanded;
    }
    @Override
    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
    @Override
    public Boolean getIsLeaf() {
        return isLeaf;
    }
    @Override
    public void setIsLeaf(Boolean leaf) {
        this.isLeaf = leaf;
    }
    @Override
    public Integer getLevel() {
        return level;
    }
    @Override
    public void setLevel(Integer level) {
        this.level = level;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getOldIcon() {
        return oldIcon;
    }
    public void setOldIcon(String oldIcon) {
        this.oldIcon = oldIcon;
    }
    public Integer getIsExtend() {
        return isExtend;
    }
    public void setIsExtend(Integer isExtend) {
        this.isExtend = isExtend;
    }
    public String getAbsoluteName() {
        return absoluteName;
    }
    public void setAbsoluteName(String absoluteName) {
        this.absoluteName = absoluteName;
    }
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
    public String getCollectFlag() {
        return collectFlag;
    }
    public void setCollectFlag(String collectFlag) {
        this.collectFlag = collectFlag;
    }
    public String getTopParentId() {
        return topParentId;
    }
    public void setTopParentId(String topParentId) {
        this.topParentId = topParentId;
    }
    public String getTopParentName() {
        return topParentName;
    }
    public void setTopParentName(String topParentName) {
        this.topParentName = topParentName;
    }
}
