package com.bytter.poi.cache;
import com.bytter.poi.cache.manager.POICacheManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
/**
 * Excel文件缓存
 *
 * @author zhouj
 * @since 2017/6/2.
 */
@Slf4j
public final class ExcelCache {
    public ExcelCache() {
    }
    /**
     * 获取Workbook
     *
     * @param url
     *            路径
     * @param sheetNums
     *            工作表
     * @param needAll
     *            是否加载全部
     * @return Workbook
     */
    public static Workbook getWorkbook(String url, Integer[] sheetNums, boolean needAll) {
        InputStream is = null;
        Workbook workbook = null;
        List sheetList = Arrays.asList(sheetNums);
        try {
            log.error("excel url==>【{}】", url);
            is = POICacheManager.getFile(url);
            if (url.toLowerCase().contains(".xls")) {
                workbook = new HSSFWorkbook(is);
            } else {
                workbook = WorkbookFactory.create(is);
            }
            if (!needAll) {
                for (int i = workbook.getNumberOfSheets() - 1; i >= 0; --i) {
                    if (!sheetList.contains(Integer.valueOf(i))) {
                        workbook.removeSheetAt(i);
                    }
                }
            }
            Workbook var21 = workbook;
            return var21;
        } catch (InvalidFormatException var18) {
            log.error(var18.getMessage(), var18);
        } catch (Exception var19) {
            log.error(var19.getMessage(), var19);
        } finally {
            try {
                is.close();
            } catch (Exception var17) {
                log.error(var17.getMessage(), var17);
            }
        }
        return null;
    }
}
