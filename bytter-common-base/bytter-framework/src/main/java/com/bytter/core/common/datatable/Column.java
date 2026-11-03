package com.bytter.core.common.datatable;
import java.io.Serializable;
public class Column implements Serializable {
    private String data;
    private String name;
    private Boolean searchable;
    private Boolean orderable;
    private Search search;
    private String title;
    private String align;
    private boolean isPagesum;
    public Column() {
    }
    public Column(String columnName, String data, boolean searchable, boolean orderable, Search search) {
        this.name = columnName;
        this.data = data;
        this.searchable = searchable;
        this.orderable = orderable;
        this.search = search;
    }
    public void setSearchValue(String searchValue) {
        this.search.setValue(searchValue);
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Boolean getSearchable() {
        return searchable;
    }
    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }
    public Boolean getOrderable() {
        return orderable;
    }
    public void setOrderable(Boolean orderable) {
        this.orderable = orderable;
    }
    public Search getSearch() {
        return search;
    }
    public void setSearch(Search search) {
        this.search = search;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public boolean getIsPagesum() {
        return isPagesum;
    }
    public void setIsPagesum(boolean isPagesum) {
        this.isPagesum = isPagesum;
    }
    public String getAlign() {
        return align;
    }
    public void setAlign(String align) {
        this.align = align;
    }
    @Override
    public String toString() {
        return "Column{" + "data='" + data + '\'' + ", name='" + name + '\'' + ", searchable=" + searchable
                + ", orderable=" + orderable + ", search=" + search + ", title='" + title + '\'' + ", isPagesum='"
                + isPagesum + '\'' + '}';
    }
}
