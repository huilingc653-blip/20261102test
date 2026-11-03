package com.bytter.poi.handler.impl;
import com.bytter.poi.handler.inter.IExcelDataHandler;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import java.util.Map;
/**
 * 导出处理接口实现类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public abstract class ExcelDataHandlerDefaultImpl implements IExcelDataHandler {
    /**
     * 需要处理的字段
     */
    private String[] needHandlerFields;
    public ExcelDataHandlerDefaultImpl() {
    }
    @Override
    public Object exportHandler(Object obj, String name, Object value) {
        return value;
    }
    @Override
    public String[] getNeedHandlerFields() {
        return this.needHandlerFields;
    }
    @Override
    public Object importHandler(Object obj, String name, Object value) {
        return value;
    }
    @Override
    public void setNeedHandlerFields(String[] needHandlerFields) {
        this.needHandlerFields = needHandlerFields;
    }
    @Override
    public void setMapValue(Map<String, Object> map, String originKey, Object value) {
        map.put(originKey, value);
    }
    @Override
    public Hyperlink getHyperlink(CreationHelper creationHelper, Object obj, String name, Object value) {
        return null;
    }
}
