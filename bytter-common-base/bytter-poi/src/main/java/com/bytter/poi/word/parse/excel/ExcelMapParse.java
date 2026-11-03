package com.bytter.poi.word.parse.excel;
import com.bytter.poi.util.PoiPublicUtil;
import com.google.common.collect.Maps;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static com.bytter.poi.util.PoiElUtil.*;
/**
 * Created by zhouj on 2017/6/2.
 */
public final class ExcelMapParse {
    public ExcelMapParse() {
    }
    /**
     * 解析参数行,获取参数列表
     * 
     * @param currentRow
     * @return
     */
    private static String[] parseCurrentRowGetParams(XWPFTableRow currentRow) {
        List<XWPFTableCell> cells = currentRow.getTableCells();
        String[] params = new String[cells.size()];
        String text;
        for (int i = 0; i < cells.size(); i++) {
            text = cells.get(i).getText();
            params[i] = text == null ? "" : text.trim().replace(START_STR, EMPTY).replace(END_STR, EMPTY);
        }
        return params;
    }
    /**
     * 解析下一行,并且生成更多的行
     * 
     * @param table
     * @param index
     * @param list
     */
    public static void parseNextRowAndAddRow(XWPFTable table, int index, List<Object> list) throws Exception {
        XWPFTableRow currentRow = table.getRow(index);
        String[] params = parseCurrentRowGetParams(currentRow);
        String listname = params[0];
        boolean isCreate = !listname.contains(FOREACH_NOT_CREATE);
        listname = listname.replace(FOREACH_NOT_CREATE, EMPTY).replace(FOREACH_AND_SHIFT, EMPTY).replace(FOREACH, EMPTY)
                .replace(START_STR, EMPTY);
        String[] keys = listname.replaceAll("\\s{1,}", " ").trim().split(" ");
        params[0] = keys[1];
        table.removeRow(index);// 移除这一行
        int cellIndex = 0;// 创建完成对象一行好像多了一个cell
        Map<String, Object> tempMap = Maps.newHashMap();
        for (Object obj : list) {
            currentRow = isCreate ? table.insertNewTableRow(index++) : table.getRow(index++);
            tempMap.put("t", obj);
            for (cellIndex = 0; cellIndex < currentRow.getTableCells().size(); cellIndex++) {
                String val = eval(params[cellIndex], tempMap).toString();
                currentRow.getTableCells().get(cellIndex).setText("");
                PoiPublicUtil.setWordText(currentRow.getTableCells().get(cellIndex).addParagraph().createRun(), val);
            }
            for (; cellIndex < params.length; cellIndex++) {
                String val = eval(params[cellIndex], tempMap).toString();
                PoiPublicUtil.setWordText(currentRow.createCell().addParagraph().createRun(), val);
            }
        }
    }
}
