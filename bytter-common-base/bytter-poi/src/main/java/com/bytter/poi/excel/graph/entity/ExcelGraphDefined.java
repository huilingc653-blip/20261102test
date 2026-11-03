package com.bytter.poi.excel.graph.entity;
import java.util.List;
import com.bytter.poi.excel.graph.constant.ExcelGraphType;
import com.google.common.collect.Lists;
public class ExcelGraphDefined implements ExcelGraph {
    private ExcelGraphElement category;
    public List<ExcelGraphElement> valueList = Lists.newArrayList();
    public List<ExcelTitleCell> titleCell = Lists.newArrayList();
    private Integer graphType = ExcelGraphType.LINE_CHART;
    public List<String> title = Lists.newArrayList();
    @Override
    public ExcelGraphElement getCategory() {
        return category;
    }
    public void setCategory(ExcelGraphElement category) {
        this.category = category;
    }
    @Override
    public List<ExcelGraphElement> getValueList() {
        return valueList;
    }
    public void setValueList(List<ExcelGraphElement> valueList) {
        this.valueList = valueList;
    }
    @Override
    public Integer getGraphType() {
        return graphType;
    }
    public void setGraphType(Integer graphType) {
        this.graphType = graphType;
    }
    @Override
    public List<ExcelTitleCell> getTitleCell() {
        return titleCell;
    }
    public void setTitleCell(List<ExcelTitleCell> titleCell) {
        this.titleCell = titleCell;
    }
    @Override
    public List<String> getTitle() {
        return title;
    }
    public void setTitle(List<String> title) {
        this.title = title;
    }
}
