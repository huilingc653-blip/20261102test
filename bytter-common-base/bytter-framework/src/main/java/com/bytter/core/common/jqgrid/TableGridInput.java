package com.bytter.core.common.jqgrid;
import com.bytter.core.base.persistence.BasePersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.common.page.ParamImpl;
/**
 * Created by zhoul on 2017/7/25.
 */
public class TableGridInput extends BasePersistence implements IParameterInput {
    public Boolean getSearch() {
        return getParameter().getParaToBoolean("_search");
    }
    public String getFilters() {
        return getParameter().getPara("filters");
    }
    public Integer getPage() {
        return getParameter().getParaToInt("page", 1);
    }
    /**
     * 从第几条记录开始
     *
     * @return 从第几条记录开始
     */
    public Integer getStart() {
        Integer page = getPage() - 1;
        return page * getRows();
    }
    public Integer getRows() {
        return getParameter().getParaToInt("rows", 15);
    }
    public String getSidx() {
        return getParameter().getPara("sidx");
    }
    public String getSord() {
        return getParameter().getPara("sord");
    }
    public Long getNd() {
        return getParameter().getPara("nd");
    }
    /**
     * tableGrid与datatable互转
     * 
     * @return {@link DataTablesInput}
     */
    public DataTablesInput getDataTableInput() {
        DataTablesInput dataTablesInput = new DataTablesInput();
        dataTablesInput.setFormdata(this);
        dataTablesInput.setStart(getStart());
        dataTablesInput.setLength(this.getRows());
        dataTablesInput.setOrderString(new OrderString(getSidx(), getSord()));
        return dataTablesInput;
    }
    public String getOrder() {
        return getSidx();
    }
    public String getSort() {
        return getSord();
    }
    @Override
    public Integer getPageNumber() {
        return getPage();
    }
    @Override
    public Integer getPageSize() {
        return getRows();
    }
    /**
     * 2019-01-07 chenlw 设置每页的大小
     *
     * @param pageSize
     *            分页大小
     */
    @Override
    public void setPageSize(Integer pageSize) {
    }
    // @Override
    // public String getOrderCondition() {
    // if(StringUtils.isNotBlank(getOrder())){
    //
    // }
    // return null;
    // }
    @Override
    public IParameter getParameter() {
        return new ParamImpl(this);
    }
}
