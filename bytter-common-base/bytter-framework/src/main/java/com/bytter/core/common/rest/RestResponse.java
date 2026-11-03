package com.bytter.core.common.rest;
import com.bytter.core.common.page.IPageAdapter;
import com.bytter.core.common.page.IPagePara;
import com.bytter.core.common.page.IPageResult;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 * rest json返回POJO by 周亮
 */
public class RestResponse<T> implements IPageAdapter, Serializable {
    private Integer pageSize;
    /**
     * Current page
     */
    private Integer pageNumber;
    /**
     * Total pages
     */
    private Integer totalPage;
    /**
     * Contains the actual data
     */
    private List<T> data;
    /**
     * 用于表示是否显示自定义排列的审批字段：1要自定义显示
     */
    private Integer version;

    private BigDecimal sumIncome;//收入总金额
    private BigDecimal sumPay;//支出总金额

    public BigDecimal getSumIncome() {
        return sumIncome;
    }

    public void setSumIncome(BigDecimal sumIncome) {
        this.sumIncome = sumIncome;
    }

    public BigDecimal getSumPay() {
        return sumPay;
    }

    public void setSumPay(BigDecimal sumPay) {
        this.sumPay = sumPay;
    }

    public RestResponse() {
    }
    public RestResponse(List<T> data) {
        this.data = data;
    }
    public Integer getPageNumber() {
        return pageNumber;
    }
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
    public Integer getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
    @Override
    public void convert(IPagePara pagePara, IPageResult pageResult) {
        // int totalPage = pageResult.getTotalRow() / pagePara.getPageSize();
        int totalPage;
        if (pageResult.getTotalRow() % pagePara.getPageSize() != 0) {
            totalPage = pageResult.getTotalRow() / pagePara.getPageSize() + 1;
        } else {
            totalPage = pageResult.getTotalRow() / pagePara.getPageSize();
        }
        this.pageSize = pagePara.getPageSize();
        this.pageNumber = pageResult.getPageNumber();
        this.totalPage = totalPage;
        this.data = pageResult.getList();
        this.version = pageResult.getVersion();
    }
}
