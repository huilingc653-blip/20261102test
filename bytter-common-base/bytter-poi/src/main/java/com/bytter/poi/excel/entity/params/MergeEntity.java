package com.bytter.poi.excel.entity.params;
import java.util.List;
/**
 * 合并
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class MergeEntity {
    /**
     * 起始行
     */
    private int startRow;
    /**
     * 结束行
     */
    private int endRow;
    /**
     * 内容
     */
    private String text;
    private List<String> relyList;
    public MergeEntity() {
    }
    public MergeEntity(String text, int startRow, int endRow) {
        this.text = text;
        this.endRow = endRow;
        this.startRow = startRow;
    }
    public int getEndRow() {
        return this.endRow;
    }
    public List<String> getRelyList() {
        return this.relyList;
    }
    public int getStartRow() {
        return this.startRow;
    }
    public String getText() {
        return this.text;
    }
    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }
    public void setRelyList(List<String> relyList) {
        this.relyList = relyList;
    }
    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }
    public void setText(String text) {
        this.text = text;
    }
}
