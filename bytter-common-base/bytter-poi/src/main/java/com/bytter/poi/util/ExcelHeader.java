package com.bytter.poi.util;

import java.util.List;

/**
 * excel复杂表头信息类
 */
public class ExcelHeader {
    /**
     * 表头数据
     */
    private List<List<String>> cellInfos;

    /**
     * 表头合并的单元格  n行 m列，xyz 合并
     */
    private List<Integer[]> mergedRegion;


    public List<List<String>> getCellInfos() {
        return cellInfos;
    }

    public void setCellInfos(List<List<String>> cellInfos) {
        this.cellInfos = cellInfos;
    }

    public List<Integer[]> getMergedRegion() {
        return mergedRegion;
    }

    public void setMergedRegion(List<Integer[]> mergedRegion) {
        this.mergedRegion = mergedRegion;
    }
}
