package com.bytter.poi.excel.imports.sax.parse;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.excel.entity.ImportParams;
import com.bytter.poi.excel.entity.params.ExcelCollectionParams;
import com.bytter.poi.excel.entity.params.ExcelImportEntity;
import com.bytter.poi.excel.entity.sax.SaxReadCellEntity;
import com.bytter.poi.excel.imports.CellValueServer;
import com.bytter.poi.excel.imports.base.ImportBaseService;
import com.bytter.poi.exception.excel.ExcelImportException;
import com.bytter.poi.handler.inter.IExcelReadRowHanlder;
import com.bytter.poi.util.PoiPublicUtil;
import com.bytter.poi.util.PoiReflectorUtil;
import com.bytter.poi.util.StringPoiUtils;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Field;
import java.util.*;
/**
 * Created by zhouj on 2017/6/2.
 */
@Slf4j
public class SaxRowRead extends ImportBaseService implements ISaxRowRead {
    private List list = new ArrayList();
    private Class<?> pojoClass;
    private ImportParams params;
    private Map<Integer, String> titlemap = new HashMap();
    private Object object = null;
    private Map<String, ExcelImportEntity> excelParams = new HashMap();
    private List<ExcelCollectionParams> excelCollection = new ArrayList();
    private String targetId;
    private CellValueServer cellValueServer;
    private IExcelReadRowHanlder hanlder;
    public SaxRowRead(Class<?> pojoClass, ImportParams params, IExcelReadRowHanlder hanlder) {
        this.params = params;
        this.pojoClass = pojoClass;
        this.cellValueServer = new CellValueServer();
        this.hanlder = hanlder;
        this.initParams(pojoClass, params);
    }
    private void initParams(Class<?> pojoClass, ImportParams params) {
        try {
            Field[] e = PoiPublicUtil.getClassFields(pojoClass);
            ExcelTarget etarget = pojoClass.getAnnotation(ExcelTarget.class);
            if (etarget != null) {
                this.targetId = etarget.value();
            }
            this.getAllExcelField(this.targetId, e, this.excelParams, this.excelCollection, pojoClass, null);
        } catch (Exception var5) {
            log.error(var5.getMessage(), var5);
            throw new ExcelImportException(var5.getMessage());
        }
    }
    @Override
    public <T> List<T> getList() {
        return this.list;
    }
    @Override
    public void parse(int index, List<SaxReadCellEntity> datas) {
        try {
            if (datas != null && datas.size() != 0) {
                if (index >= this.params.getTitleRows()) {
                    if (index < this.params.getTitleRows() + this.params.getHeadRows()) {
                        this.addHeadData(datas);
                    } else {
                        this.addListData(datas);
                    }
                }
            }
        } catch (Exception var4) {
            log.error(var4.getMessage(), var4);
            throw new ExcelImportException(var4.getMessage());
        }
    }
    private void addListData(List<SaxReadCellEntity> datas) throws Exception {
        String value = String.valueOf(((SaxReadCellEntity) datas.get(this.params.getKeyIndex())).getValue());
        if ((datas.get(params.getKeyIndex()) == null
                || StringPoiUtils.isEmpty(String.valueOf(datas.get(params.getKeyIndex()).getValue())))
                && object != null) {
            for (ExcelCollectionParams param : excelCollection) {
                addListContinue(object, param, datas, titlemap, targetId, params);
            }
        } else {
            if (this.object != null && this.hanlder != null) {
                this.hanlder.hanlder(this.object);
            }
            this.object = PoiPublicUtil.createObject(this.pojoClass, this.targetId);
            int i$ = 0;
            SaxReadCellEntity entity;
            for (int param = datas.size(); i$ < param; ++i$) {
                entity = datas.get(i$);
                String titleString = (String) this.titlemap.get(Integer.valueOf(i$));
                if (this.excelParams.containsKey(titleString)) {
                    this.saveFieldValue(this.params, this.object, entity, this.excelParams, titleString);
                }
            }
            for (ExcelCollectionParams param : excelCollection) {
                addListContinue(object, param, datas, titlemap, targetId, params);
            }
            if (this.hanlder == null) {
                list.add(object);
            }
        }
    }
    private void addListContinue(Object object, ExcelCollectionParams param, List<SaxReadCellEntity> datas,
            Map<Integer, String> titlemap, String targetId, ImportParams params) throws Exception {
        Collection collection = (Collection) PoiReflectorUtil.fromCache(pojoClass).getValue(object, param.getName());
        // Collection collection = (Collection)PoiPublicUtil.getMethod(param.getName(),
        // object.getClass()).invoke(object, new Object[0]);
        Object entity = PoiPublicUtil.createObject(param.getType(), targetId);
        boolean isUsed = false;
        for (int i = 0; i < datas.size(); ++i) {
            String titleString = (String) titlemap.get(Integer.valueOf(i));
            if (param.getExcelParams().containsKey(titleString)) {
                this.saveFieldValue(params, entity, datas.get(i), param.getExcelParams(), titleString);
                isUsed = true;
            }
        }
        if (isUsed) {
            collection.add(entity);
        }
    }
    private void saveFieldValue(ImportParams params, Object object, SaxReadCellEntity entity,
            Map<String, ExcelImportEntity> excelParams, String titleString) throws Exception {
        Object value = this.cellValueServer.getValue(params.getDataHanlder(), object, entity, excelParams, titleString);
        this.setValues(excelParams.get(titleString), object, value);
    }
    private void addHeadData(List<SaxReadCellEntity> datas) {
        for (int i = 0; i < datas.size(); ++i) {
            String value = String.valueOf((datas.get(i)).getValue());
            if (value == null || value.length() == 0) {
                this.titlemap.put(i, String.valueOf((datas.get(i)).getValue()));
            }
        }
    }
}
