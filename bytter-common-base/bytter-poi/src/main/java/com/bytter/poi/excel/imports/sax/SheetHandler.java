package com.bytter.poi.excel.imports.sax;
import com.bytter.poi.excel.entity.enmus.CellValueType;
import com.bytter.poi.excel.entity.sax.SaxReadCellEntity;
import com.bytter.poi.excel.imports.sax.parse.ISaxRowRead;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by zhouj on 2017/6/2.
 */
public class SheetHandler extends DefaultHandler {
    private SharedStringsTable sst;
    private String lastContents;
    private int curRow = 0;
    private int curCol = 0;
    private CellValueType type;
    private ISaxRowRead read;
    private List<SaxReadCellEntity> rowlist = new ArrayList<SaxReadCellEntity>();
    public SheetHandler(SharedStringsTable sst, ISaxRowRead rowRead) {
        this.sst = sst;
        this.read = rowRead;
    }
    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        this.lastContents = "";
        if ("c".equals(name)) {
            String cellType = attributes.getValue("t");
            if ("s".equals(cellType)) {
                this.type = CellValueType.String;
                return;
            }
            cellType = attributes.getValue("s");
            if ("1".equals(cellType)) {
                this.type = CellValueType.Date;
            } else if ("2".equals(cellType)) {
                this.type = CellValueType.Number;
            }
        } else if ("t".equals(name)) {
            this.type = CellValueType.TElement;
        }
    }
    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (CellValueType.String.equals(this.type)) {
            try {
                int value = Integer.parseInt(this.lastContents);
                this.lastContents = (new XSSFRichTextString(this.sst.getEntryAt(value))).toString();
            } catch (Exception var6) {
                ;
            }
        }
        String var7;
        if (CellValueType.TElement.equals(this.type)) {
            var7 = this.lastContents.trim();
            this.rowlist.add(this.curCol, new SaxReadCellEntity(CellValueType.String, var7));
            ++this.curCol;
            this.type = CellValueType.None;
        } else if ("v".equals(name)) {
            var7 = this.lastContents.trim();
            var7 = "".equals(var7) ? " " : var7;
            if (CellValueType.Date.equals(this.type)) {
                Date bd = HSSFDateUtil.getJavaDate(Double.valueOf(var7).doubleValue());
                this.rowlist.add(this.curCol, new SaxReadCellEntity(CellValueType.Date, bd));
            } else if (CellValueType.Number.equals(this.type)) {
                BigDecimal var8 = new BigDecimal(var7);
                this.rowlist.add(this.curCol, new SaxReadCellEntity(CellValueType.Number, var8));
            } else if (CellValueType.String.equals(this.type)) {
                this.rowlist.add(this.curCol, new SaxReadCellEntity(CellValueType.String, var7));
            }
            ++this.curCol;
        } else if ("row".equals(name)) {
            this.read.parse(this.curRow, this.rowlist);
            this.rowlist.clear();
            ++this.curRow;
            this.curCol = 0;
        }
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.lastContents = this.lastContents + new String(ch, start, length);
    }
}
