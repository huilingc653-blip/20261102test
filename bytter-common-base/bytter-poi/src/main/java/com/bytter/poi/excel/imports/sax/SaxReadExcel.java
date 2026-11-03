package com.bytter.poi.excel.imports.sax;
import com.bytter.poi.excel.entity.ImportParams;
import com.bytter.poi.excel.imports.sax.parse.ISaxRowRead;
import com.bytter.poi.excel.imports.sax.parse.SaxRowRead;
import com.bytter.poi.exception.excel.ExcelImportException;
import com.bytter.poi.handler.inter.IExcelReadRowHanlder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
/**
 * Created by zhouj on 2017/6/2.
 */
@Slf4j
public class SaxReadExcel {
    public SaxReadExcel() {
    }
    public <T> List<T> readExcel(InputStream inputstream, Class<?> pojoClass, ImportParams params, ISaxRowRead rowRead,
            IExcelReadRowHanlder hanlder) {
        try {
            OPCPackage e = OPCPackage.open(inputstream);
            return this.readExcel(e, pojoClass, params, rowRead, hanlder);
        } catch (Exception var7) {
            log.error(var7.getMessage(), var7);
            throw new ExcelImportException(var7.getMessage());
        }
    }
    private <T> List<T> readExcel(OPCPackage opcPackage, Class<?> pojoClass, ImportParams params, ISaxRowRead rowRead,
            IExcelReadRowHanlder hanlder) {
        try {
            XSSFReader e = new XSSFReader(opcPackage);
            SharedStringsTable sst = e.getSharedStringsTable();
            if (rowRead == null) {
                rowRead = new SaxRowRead(pojoClass, params, hanlder);
            }
            XMLReader parser = this.fetchSheetParser(sst, (ISaxRowRead) rowRead);
            Iterator sheets = e.getSheetsData();
            int sheetIndex = 0;
            while (sheets.hasNext() && sheetIndex < params.getSheetNum()) {
                ++sheetIndex;
                InputStream sheet = (InputStream) sheets.next();
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
                sheet.close();
            }
            return ((ISaxRowRead) rowRead).getList();
        } catch (Exception var13) {
            log.error(var13.getMessage(), var13);
            throw new ExcelImportException("SAX导入数据失败");
        }
    }
    private XMLReader fetchSheetParser(SharedStringsTable sst, ISaxRowRead rowRead) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        SheetHandler handler = new SheetHandler(sst, rowRead);
        parser.setContentHandler(handler);
        return parser;
    }
}
