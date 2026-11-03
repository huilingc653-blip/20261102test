package com.bytter.core.common.jqgrid;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.page.IPageAdapter;
import com.bytter.core.common.page.IPagePara;
import com.bytter.core.common.page.IPageResult;
import java.util.List;
/**
 * 表格json返回POJO by 周亮
 */
public class TableGridResponse<T> implements IPageAdapter {
    /**
     * Current page
     */
    private Integer page;
    /**
     * Total pages
     */
    private Integer total;
    /**
     * Total number of records
     */
    private Integer records;
    /**
     * Contains the actual data
     */
    private List<T> rows;
    public TableGridResponse() {
    }
    public TableGridResponse(List<T> rows) {
        this.rows = rows;
    }
    public TableGridResponse(TableGridInput tableGridInput, Long records, List<T> rows) {
        this(tableGridInput, Integer.parseInt(records.toString()), rows);
    }
    public TableGridResponse(TableGridInput tableGridInput, Integer records, List<T> rows) {
        this(tableGridInput.getPage(), tableGridInput.getRows(), records, rows);
    }
    /**
     * @param page
     *            当前页码
     * @param row
     *            显示条数
     * @param records
     *            总记录数
     * @param rows
     *            记录数据
     */
    public TableGridResponse(Integer page, Integer row, Integer records, List<T> rows) {
        this(rows);
        int tempTotal = records / row;
        int moreOnePage = records % row;
        this.page = page;
        this.total = moreOnePage == 0 ? tempTotal : tempTotal + 1;
        this.records = records;
    }
    public TableGridResponse(TableGridInput input, DataTablesOutput output) {
        this(input, Integer.parseInt(output.getRecordsTotal() + ""), output.getData());
    }
    public static <T> TableGridResponse<T> convert(IPage<T> iPage) {
        TableGridResponse<T> tableGridResponse = new TableGridResponse<>();
        tableGridResponse.setPage(Math.toIntExact(iPage.getPages()));
        tableGridResponse.setTotal(Math.toIntExact(iPage.getTotal()));
        tableGridResponse.setRows(iPage.getRecords());
        return tableGridResponse;
    }
    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }
    public Integer getRecords() {
        return records;
    }
    public void setRecords(Integer records) {
        this.records = records;
    }
    public List<T> getRows() {
        return rows;
    }
    public void setRows(List<T> rows) {
        this.rows = rows;
    }
    @Override
    public String toString() {
        return "TableGridResponse{" + "page=" + page + ", total=" + total + ", records=" + records + ", rows=" + rows
                + '}';
    }
    @Override
    public void convert(IPagePara pagePara, IPageResult pageResult) {
        int tempTotal = pageResult.getTotalRow() / pagePara.getPageSize();
        int moreOnePage = pageResult.getTotalRow() % pagePara.getPageSize();
        this.page = pageResult.getPageNumber();
        this.total = moreOnePage == 0 ? tempTotal : tempTotal + 1;
        this.records = pageResult.getTotalRow();
        this.rows = pageResult.getList();
    }
}
