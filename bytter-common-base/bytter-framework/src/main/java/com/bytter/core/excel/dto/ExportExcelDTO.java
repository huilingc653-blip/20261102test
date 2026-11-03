package com.bytter.core.excel.dto;
import com.bytter.core.common.datatable.DataTablesInput;
import java.io.Serializable;
import java.util.List;
/**
 * 导出所有Excel信息
 *
 * @author zuoy
 */
public class ExportExcelDTO implements Serializable {
    private DataTablesInput dataTablesInput;
    private List<ExportExcelHeadDTO> heads;
    private Long total;
    private String title;
    private String fileName;
    public DataTablesInput getDataTablesInput() {
        return dataTablesInput;
    }
    public void setDataTablesInput(DataTablesInput dataTablesInput) {
        this.dataTablesInput = dataTablesInput;
    }
    public List<ExportExcelHeadDTO> getHeads() {
        return heads;
    }
    public void setHeads(List<ExportExcelHeadDTO> heads) {
        this.heads = heads;
    }
    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
