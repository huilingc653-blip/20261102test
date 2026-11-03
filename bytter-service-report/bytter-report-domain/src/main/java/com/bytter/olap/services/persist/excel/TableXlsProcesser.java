package com.bytter.olap.services.persist.excel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyuan on 2017/2/15.
 */
public class TableXlsProcesser extends XlsProcesser {

//    @Override
//    protected ClientAnchor drawContent(XlsProcesserContext context) {
//        JSONArray tData = context.getData().getJSONArray("data");
//        final ClientAnchor tAnchor = new HSSFClientAnchor();
//        tAnchor.setCol1(context.getC1());
//        tAnchor.setRow1(context.getR1());
//        int colSpan = (context.getC2() - context.getC1()) / tData.getJSONArray(0).size();
//        int delta = (context.getC2() - context.getC1()) % tData.getJSONArray(0).size();
//        int rowHeader = 0;
//        List<Integer> columnHeaderCellIdx = new ArrayList<>();
//        List<Integer> columnDataCellIdx = new ArrayList<>();
//        List<CellRangeAddress> mergeRegion = new ArrayList<>();
//        for (int r = 0; r < tData.size(); r++) {
//            Row row = context.getBoardSheet().getRow(context.getR1() + r);
//            if (row == null) {
//                row = context.getBoardSheet().createRow(context.getR1() + r);
//            }
//            int colStart = context.getC1();
//            for (int c = 0; c < tData.getJSONArray(r).size(); c++) {
//                JSONObject cData = tData.getJSONArray(r).getJSONObject(c);
//                int deltaSpan = colSpan;
//                if (c <= delta) {
//                    deltaSpan = colSpan + 1;
//                }
//                String property = cData.getString("property");
//
//                if (c == tData.getJSONArray(r).size() - 1) {
//                    if (!"data".equals(property)) {
//                        rowHeader = r;
//                    }
//                }
//                if (r == tData.size() - 1) {
//                    if (!"data".equals(property)) {
//                        columnHeaderCellIdx.add(colStart);
//                    } else {
//                        columnDataCellIdx.add(colStart);
//                    }
//                }
//
//                for (int j = colStart; j < colStart + deltaSpan; j++) {
//                    Cell cell = row.createCell(j);
//                    if ("header_key".equals(property) || "header_empty".equals(property)) {
//                        cell.setCellStyle(context.getTableStyle());
//                    } else if ("data".equals(property) || "column_key".equals(property)) {
//                        cell.setCellStyle(context.gettStyle());
//                    }
//                    if (j == colStart) {
//                        if ("data".equals(property)) {
//                            if (cData.getString("data") != null && cData.getString("data").contains("%")) {
//                                cell.setCellValue(cData.getDoubleValue("raw"));
//                                cell.setCellStyle(context.getPercentStyle());
//                            } else {
//                                cell.setCellValue(cData.getDoubleValue("raw"));
//                            }
//                        } else {
//                            cell.setCellValue(cData.getString("data"));
//                        }
//                    }
//                }
//                mergeRegion.add(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colStart, colStart + deltaSpan - 1));
//                colStart = colStart + deltaSpan;
//
//                tAnchor.setCol2(context.getC2());
//                tAnchor.setRow2(row.getRowNum());
//            }
//        }
//
//        String mergeKey = null;
//        List<CellHelper> mergedList = new ArrayList<>();
//        for (int col = columnHeaderCellIdx.size() - 1; col >= 0; col--) {
//            for (int r = context.getR1(); r <= tAnchor.getRow2(); r++) {
//                HCell hCell = new HCell(context, mergeRegion, context.getBoardSheet().getRow(r).getCell(columnHeaderCellIdx.get(col)));
//                if (mergeKey != null) {
//                    if (mergeKey.equals(hCell.getMergeKey())) {
//                        mergedList.add(hCell);
//                    } else {
//                        mergeCellHelper(mergeRegion, mergedList);
//                        mergedList = new ArrayList<>();
//                        mergeKey = hCell.getMergeKey();
//                        mergedList.add(hCell);
//                    }
//                } else {
//                    mergeKey = hCell.getMergeKey();
//                }
//            }
//        }
//        mergeCellHelper(mergeRegion, mergedList);
//        mergeKey = null;
//        mergedList = new ArrayList<>();
//        for (int row = rowHeader; row >= 0; row--) {
//            for (int c = 0; c < columnDataCellIdx.size(); c++) {
//                VCell vCell = new VCell(context, mergeRegion, context.getBoardSheet().getRow(context.getR1() + row).getCell(columnDataCellIdx.get(c)));
//                if (mergeKey != null) {
//                    if (mergeKey.equals(vCell.getMergeKey())) {
//                        mergedList.add(vCell);
//                    } else {
//                        mergeCellHelper(mergeRegion, mergedList);
//                        mergedList = new ArrayList<>();
//                        mergeKey = vCell.getMergeKey();
//                        mergedList.add(vCell);
//                    }
//                } else {
//                    mergeKey = vCell.getMergeKey();
//                }
//            }
//        }
//        mergeCellHelper(mergeRegion, mergedList);
//        for (CellRangeAddress e : mergeRegion) {
//            if (e.getFirstColumn() != e.getLastColumn() || e.getFirstRow() != e.getLastRow()) {
//                context.getBoardSheet().addMergedRegion(e);
//            }
//        }
//        return tAnchor;
//    }


    @Override
    protected ClientAnchor drawContent(XlsProcesserContext context) {
        JsonNode data = context.getData();
        ArrayNode tData = (ArrayNode) data.get("data");
        ArrayNode firstDataNode = (ArrayNode) tData.get(0);

        final ClientAnchor tAnchor = new HSSFClientAnchor();
        tAnchor.setCol1(context.getC1());
        tAnchor.setRow1(context.getR1());
        int colSpan = (context.getC2() - context.getC1()) / firstDataNode.size();
        int delta = (context.getC2() - context.getC1()) % firstDataNode.size();
        int rowHeader = 0;
        List<Integer> columnHeaderCellIdx = new ArrayList<>();
        List<Integer> columnDataCellIdx = new ArrayList<>();
        List<CellRangeAddress> mergeRegion = new ArrayList<>();
        for (int r = 0; r < tData.size(); r++) {
            Row row = context.getBoardSheet().getRow(context.getR1() + r);
            if (row == null) {
                row = context.getBoardSheet().createRow(context.getR1() + r);
            }
            int colStart = context.getC1();
            for (int c = 0; c < tData.get(r).size(); c++) {
                JsonNode cData = tData.get(r).get(c);
                int deltaSpan = colSpan;
                if (c <= delta) {
                    deltaSpan = colSpan + 1;
                }
                JsonNode propertyNode = cData.get("property");

                if (c == tData.get(r).size() - 1) {
                    if (propertyNode != null && !"data".equals(propertyNode.textValue())) {
                        rowHeader = r;
                    }
                }
                if (r == tData.size() - 1) {
                    if (propertyNode != null && !"data".equals(propertyNode.textValue())) {
                        columnHeaderCellIdx.add(colStart);
                    } else {
                        columnDataCellIdx.add(colStart);
                    }
                }

                for (int j = colStart; j < colStart + deltaSpan; j++) {
                    Cell cell = row.createCell(j);
                    if (propertyNode != null
                            && ("header_key".equals(propertyNode.textValue()) || "header_empty".equals(propertyNode.textValue()))) {
                        cell.setCellStyle(context.getTableStyle());
                    } else if (propertyNode != null
                            && ("data".equals(propertyNode.textValue()) || "column_key".equals(propertyNode.textValue()))) {
                        cell.setCellStyle(context.gettStyle());
                    }
                    if (j == colStart) {
                        if (propertyNode != null
                                && "data".equals(propertyNode.textValue())) {

                            JsonNode rawNode = cData.get("raw");

                            if (cData.get("data") != null && cData.get("data").textValue().contains("%")) {
                                if (rawNode != null) {
                                    cell.setCellValue(rawNode.doubleValue());
                                }
                                cell.setCellStyle(context.getPercentStyle());
                            } else {
                                if (rawNode != null) {
                                    cell.setCellValue(rawNode.doubleValue());
                                }
                            }
                        } else {
                            cell.setCellValue(cData.get("data").textValue());
                        }
                    }
                }
                mergeRegion.add(new CellRangeAddress(row.getRowNum(), row.getRowNum(), colStart, colStart + deltaSpan - 1));
                colStart = colStart + deltaSpan;

                tAnchor.setCol2(context.getC2());
                tAnchor.setRow2(row.getRowNum());
            }
        }

        String mergeKey = null;
        List<CellHelper> mergedList = new ArrayList<>();
        for (int col = columnHeaderCellIdx.size() - 1; col >= 0; col--) {
            for (int r = context.getR1(); r <= tAnchor.getRow2(); r++) {
                HCell hCell = new HCell(context, mergeRegion, context.getBoardSheet().getRow(r).getCell(columnHeaderCellIdx.get(col)));
                if (mergeKey != null) {
                    if (mergeKey.equals(hCell.getMergeKey())) {
                        mergedList.add(hCell);
                    } else {
                        mergeCellHelper(mergeRegion, mergedList);
                        mergedList = new ArrayList<>();
                        mergeKey = hCell.getMergeKey();
                        mergedList.add(hCell);
                    }
                } else {
                    mergeKey = hCell.getMergeKey();
                }
            }
        }
        mergeCellHelper(mergeRegion, mergedList);
        mergeKey = null;
        mergedList = new ArrayList<>();
        for (int row = rowHeader; row >= 0; row--) {
            for (int c = 0; c < columnDataCellIdx.size(); c++) {
                VCell vCell = new VCell(context, mergeRegion, context.getBoardSheet().getRow(context.getR1() + row).getCell(columnDataCellIdx.get(c)));
                if (mergeKey != null) {
                    if (mergeKey.equals(vCell.getMergeKey())) {
                        mergedList.add(vCell);
                    } else {
                        mergeCellHelper(mergeRegion, mergedList);
                        mergedList = new ArrayList<>();
                        mergeKey = vCell.getMergeKey();
                        mergedList.add(vCell);
                    }
                } else {
                    mergeKey = vCell.getMergeKey();
                }
            }
        }
        mergeCellHelper(mergeRegion, mergedList);
        for (CellRangeAddress e : mergeRegion) {
            if (e.getFirstColumn() != e.getLastColumn() || e.getFirstRow() != e.getLastRow()) {
                context.getBoardSheet().addMergedRegion(e);
            }
        }
        return tAnchor;
    }

    private void mergeCellHelper(List<CellRangeAddress> mergeRegion, List<CellHelper> mergeList) {
        if (mergeList.size() < 2) {
            return;
        }
        for (CellHelper cellHelper : mergeList) {
            mergeRegion.remove(cellHelper.getMergedCell());
        }
        List<CellRangeAddress> cellRangeAddressList = new ArrayList<>();
        for (CellHelper cellHelper : mergeList) {
            cellRangeAddressList.add(cellHelper.getMergedCell());
        }
        CellRangeAddress[] toMerged = cellRangeAddressList.toArray(new CellRangeAddress[0]);
        CellRangeAddress[] merged = CellRangeUtil.mergeCellRanges(toMerged);

        for (CellRangeAddress cellRangeAddress : merged) {
            mergeRegion.add(cellRangeAddress);
        }

    }


    private abstract class CellHelper {
        private Cell cell;
        private CellRangeAddress mergedCell;
        private String mergeKey;


        public CellHelper(XlsProcesserContext context, List<CellRangeAddress> mergeRegion, Cell cell) {
            List<CellRangeAddress> mergeRegionFilterList = new ArrayList<>();
            for (CellRangeAddress cellRangeAddress : mergeRegion) {
                if (cellRangeAddress.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
                    mergeRegionFilterList.add(cellRangeAddress);
                }
            }
            CellRangeAddress _c = null;
            if (mergeRegionFilterList.size() > 0) {
                _c = mergeRegionFilterList.get(0);
            }
            this.cell = cell;
            this.mergedCell = _c;

            parseMergeKey(context, cell);
        }


        protected abstract void parseMergeKey(XlsProcesserContext context, Cell cell);

        public String getMergeKey() {
            return mergeKey;
        }

        public Cell getCell() {
            return cell;
        }

        public void setCell(Cell cell) {
            this.cell = cell;
        }

        public CellRangeAddress getMergedCell() {
            return mergedCell;
        }
    }

    private class HCell extends CellHelper {

        public HCell(XlsProcesserContext context, List<CellRangeAddress> mergeRegion, Cell cell) {
            super(context, mergeRegion, cell);
        }

        @Override
        protected void parseMergeKey(XlsProcesserContext context, Cell cell) {
            StringBuilder sb = new StringBuilder();
            for (int c = cell.getColumnIndex(); c >= context.getC1(); c--) {
                Cell _cell = context.getBoardSheet().getRow(cell.getRowIndex()).getCell(c);
                if (_cell != null && _cell.getStringCellValue() != null) {
                    sb.append(_cell.getStringCellValue());
                }
            }
            super.mergeKey = sb.toString();
        }

    }

    private class VCell extends CellHelper {

        public VCell(XlsProcesserContext context, List<CellRangeAddress> mergeRegion, Cell cell) {
            super(context, mergeRegion, cell);
        }

        @Override
        protected void parseMergeKey(XlsProcesserContext context, Cell cell) {
            StringBuilder sb = new StringBuilder();
            for (int r = cell.getRowIndex(); r >= context.getR1(); r--) {
                Cell _cell = context.getBoardSheet().getRow(r).getCell(cell.getColumnIndex());
                if (_cell != null && _cell.getStringCellValue() != null) {
                    sb.append(_cell.getStringCellValue());
                }
            }
            super.mergeKey = sb.toString();
        }

    }

}
