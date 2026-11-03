package com.bytter.olap.services.persist.excel;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.*;

/**
 * Created by yfyuan on 2017/2/15.
 */
public class JpgXlsProcesser extends XlsProcesser {


    /**
     * 在看板导出图表的时候会执行。
     * Created by chenlw on 2019/5/14.（去除JAVA8特性）
     *
     * @param context
     * @return
     */
    @Override
    protected ClientAnchor drawContent(XlsProcesserContext context) {
        JsonNode data = context.getData();
        String pngData = data.get("data").textValue();
        // data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAABI4AAAEsCAYAAAClh/jbAAA ...
        String[] arr = pngData.split("base64,");

        byte[] bytes = Base64.decodeBase64(arr[1]);
        //byte[] bytes = Base64.getDecoder().decode(pngData.substring(23));
        int pictureIdx = context.getWb().addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
        Drawing drawing = context.getBoardSheet().createDrawingPatriarch();
        CreationHelper helper = context.getWb().getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();
        //HSSFClientAnchor anchor = new HSSFClientAnchor();
        anchor.setCol1(context.getC1());
        anchor.setRow1(context.getR1());
        anchor.setCol2(context.getC2());
        int r2 = context.getR2();
        JsonNode widgetTypeNode = data.get("widgetType");
        if (widgetTypeNode != null && "kpi".equals(widgetTypeNode.textValue())) {
            r2 = context.getR1() + 8;
        }
        anchor.setRow2(r2);
        Picture picture = drawing.createPicture(anchor, pictureIdx);
        return picture.getClientAnchor();
    }


}
