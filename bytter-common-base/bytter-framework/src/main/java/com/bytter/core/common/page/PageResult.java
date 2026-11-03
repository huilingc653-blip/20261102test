package com.bytter.core.common.page;
import org.springframework.util.Assert;
import java.io.Serializable;
import java.util.List;
/**
 * 查询结果集 Created by zhoul on 2017/9/18.
 */
public class PageResult<T> implements IPageResult<T>, Serializable {
    /**
     * 结果集
     */
    private List<T> list;
    /**
     * 当前页码
     */
    private Integer pageNumber;
    /**
     * 总共有多少行
     */
    private Integer totalRow;
    private IPagePara pagePara;
    /**
     * 用于表示是否显示自定义排列的审批字段：1要自定义显示
     */
    private Integer version;
    /**
     * 分页组件自定义显示内容
     */
    private String insertInformation;
    public PageResult() {
    }
    public PageResult(IPagePara pagePara, Integer totalRow, List<T> list) {
        setPageNumber(pagePara.getPageNumber());
        setPagePara(pagePara);
        setTotalRow(totalRow);
        setList(list);
    }
    public PageResult(IPagePara pagePara, Integer totalRow, List<T> list, Integer version) {
        setPageNumber(pagePara.getPageNumber());
        setPagePara(pagePara);
        setTotalRow(totalRow);
        setList(list);
        setVersion(version);
    }
    @Override
    public List<T> getList() {
        return list;
    }
    @Override
    public void setList(List<T> list) {
        this.list = list;
    }
    @Override
    public Integer getPageNumber() {
        return pageNumber;
    }
    @Override
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
    @Override
    public Integer getTotalRow() {
        return totalRow;
    }
    @Override
    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
    }
    @Override
    public IPagePara getPagePara() {
        return pagePara;
    }
    @Override
    public void setPagePara(IPagePara pagePara) {
        this.pagePara = pagePara;
    }
    @Override
    public Integer getVersion() {
        return version;
    }
    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }
    @Override
    public <C extends IPageAdapter> C convert(Class<? extends IPageAdapter> adapter) {
        Assert.notNull(adapter, "adapter must not be null");
        try {
            IPageAdapter adapterInstance = adapter.newInstance();
            adapterInstance.convert(pagePara, this);
            return (C) adapterInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public String getInsertInformation() {
        return insertInformation;
    }
    @Override
    public void setInsertInformation(String insertInformation) {
        this.insertInformation = insertInformation;
    }
    public static PageResult emptyPage(int pageNumber){
        PageResult pageResult = new PageResult();
        pageResult.setPageNumber(pageNumber);
        return pageResult;
    }
    public static <T> PageResult<T> of(int pageNumber,int totalRow,List<T> datas){
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setPageNumber(pageNumber);
        pageResult.setTotalRow(totalRow);
        pageResult.setList(datas);
        return pageResult;
    }
}
