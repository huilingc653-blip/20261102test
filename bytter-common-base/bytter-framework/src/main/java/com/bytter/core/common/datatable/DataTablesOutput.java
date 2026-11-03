package com.bytter.core.common.datatable;
import com.bytter.core.common.page.IPageAdapter;
import com.bytter.core.common.page.IPagePara;
import com.bytter.core.common.page.IPageResult;
import java.io.Serializable;
import java.util.List;
public class DataTablesOutput<T> implements IPageAdapter, Serializable {
    private int draw;
    private long recordsTotal = 0L;
    private long recordsFiltered = 0L;
    private String insertInformation;
    private List<T> data;
    private String error;
    public DataTablesOutput() {
    }
    public DataTablesOutput(DataTablesOutput dataTablesOutput, List<T> data) {
        this.draw = dataTablesOutput.getDraw();
        this.data = data;
        this.recordsTotal = dataTablesOutput.getRecordsTotal();
        this.recordsFiltered = dataTablesOutput.getRecordsFiltered();
    }
    public DataTablesOutput(int draw, List<T> data, long recordsTotal, long recordsFiltered) {
        this.draw = draw;
        this.data = data;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
    }
    public int getDraw() {
        return draw;
    }
    public void setDraw(int draw) {
        this.draw = draw;
    }
    public long getRecordsTotal() {
        return recordsTotal;
    }
    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }
    public long getRecordsFiltered() {
        return recordsFiltered;
    }
    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }
    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public String getInsertInformation() {
        return insertInformation;
    }
    public void setInsertInformation(String insertInformation) {
        this.insertInformation = insertInformation;
    }
    @Override
    public void convert(IPagePara pagePara, IPageResult pageResult) {
        DataTablesInput dataTablesInput = (DataTablesInput) pagePara;
        this.draw = dataTablesInput.getDraw();
        this.data = pageResult.getList();
        this.recordsTotal = pageResult.getTotalRow();
        this.recordsFiltered = pageResult.getTotalRow();
        this.insertInformation = pageResult.getInsertInformation();
    }
}
