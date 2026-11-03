package com.bytter.poi.excel.html.helper;
import org.apache.poi.ss.usermodel.Sheet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * Created by zhouj on 2017/6/2.
 */
public class MergedRegionHelper {
    private Map<String, Integer[]> mergedCache = new HashMap();
    private Set<String> notNeedCread = new HashSet();
    public MergedRegionHelper(Sheet sheet) {
        this.getAllMergedRegion(sheet);
    }
    private void getAllMergedRegion(Sheet sheet) {
        int nums = sheet.getNumMergedRegions();
        for (int i = 0; i < nums; ++i) {
            this.handerMergedString(sheet.getMergedRegion(i).formatAsString());
        }
    }
    private void handerMergedString(String formatAsString) {
        String[] strArr = formatAsString.split(":");
        if (strArr.length == 2) {
            int startCol = strArr[0].charAt(0) - 65;
            if (strArr[0].charAt(1) >= 65) {
                startCol = (startCol + 1) * 26 + (strArr[0].charAt(1) - 65);
            }
            int startRol = Integer.valueOf(strArr[0].substring(strArr[0].charAt(1) >= 65 ? 2 : 1)).intValue();
            int endCol = strArr[1].charAt(0) - 65;
            if (strArr[1].charAt(1) >= 65) {
                endCol = (endCol + 1) * 26 + (strArr[1].charAt(1) - 65);
            }
            int endRol = Integer.valueOf(strArr[1].substring(strArr[1].charAt(1) >= 65 ? 2 : 1)).intValue();
            this.mergedCache.put(startRol + "_" + startCol,
                    new Integer[] { Integer.valueOf(endRol - startRol + 1), Integer.valueOf(endCol - startCol + 1) });
            for (int i = startRol; i <= endRol; ++i) {
                for (int j = startCol; j <= endCol; ++j) {
                    this.notNeedCread.add(i + "_" + j);
                }
            }
            this.notNeedCread.remove(startRol + "_" + startCol);
        }
    }
    public boolean isNeedCreate(int row, int col) {
        return !this.notNeedCread.contains(row + "_" + col);
    }
    public boolean isMergedRegion(int row, int col) {
        return this.mergedCache.containsKey(row + "_" + col);
    }
    public Integer[] getRowAndColSpan(int row, int col) {
        return (Integer[]) this.mergedCache.get(row + "_" + col);
    }
}
