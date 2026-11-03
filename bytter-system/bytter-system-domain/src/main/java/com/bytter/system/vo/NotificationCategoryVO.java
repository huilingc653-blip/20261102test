package com.bytter.system.vo;
import com.bytter.core.annotation.Menu;
import com.bytter.core.base.persistence.tree.interfaces.TreeSortStructure;
import java.util.List;
/**
 * Created by zhoul on 2017/11/1.
 */
@SuppressWarnings("serial")
public class NotificationCategoryVO implements TreeSortStructure<NotificationCategoryVO> {
    private String id;
    private String tag;
    @Menu(inject = "menuName")
    private String menuId;
    private String menuName;
    private String resourceContent;
    private Integer roleFilter;
    private Integer sort;
    private String content;
    private String parentId;
    private String name;
    private String listUrl;
    private String detailUrl;
    private List<NotificationCategoryVO> children;
    private Boolean expanded = true;
    private Boolean isLeaf;
    private Integer level;
    /**
     * 重点关注
     */
    private String starTarget;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public String getResourceContent() {
        return resourceContent;
    }
    public void setResourceContent(String resourceContent) {
        this.resourceContent = resourceContent;
    }
    public Integer getRoleFilter() {
        return roleFilter;
    }
    public void setRoleFilter(Integer roleFilter) {
        this.roleFilter = roleFilter;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
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
    public String getListUrl() {
        return listUrl;
    }
    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }
    public String getDetailUrl() {
        return detailUrl;
    }
    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
    public List<NotificationCategoryVO> getChildren() {
        return children;
    }
    public void setChildren(List<NotificationCategoryVO> children) {
        this.children = children;
    }
    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }
    public Boolean getIsLeaf() {
        return isLeaf;
    }
    public void setIsLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
    public Boolean getExpanded() {
        return expanded;
    }
    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
    public String getMenuName() {
        return menuName;
    }
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    public String getStarTarget() {
        return starTarget;
    }
    public void setStarTarget(String starTarget) {
        this.starTarget = starTarget;
    }
}
