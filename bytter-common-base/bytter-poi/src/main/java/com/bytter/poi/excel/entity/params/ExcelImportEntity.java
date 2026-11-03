package com.bytter.poi.excel.entity.params;
import java.util.List;
/**
 * Excel导入
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class ExcelImportEntity extends ExcelBaseEntity {
    /**
     * 对应 Collection NAME
     */
    private String collectionName;
    /**
     * 保存图片的地址
     */
    private String saveUrl;
    /**
     * 保存图片的类型,1是文件,2是数据库
     */
    private int saveType;
    /**
     * 对应exportType
     */
    private String classType;
    /**
     * 后缀
     */
    private String suffix;
    /**
     * 导入校验字段
     */
    private boolean importField;
    /**
     * 导入数字格式化
     */
    private String numFormat;
    /**
     * 导入集合
     */
    private List<ExcelImportEntity> list;
    /**
     * 日期预先转换格式
     */
    private boolean isTryTransferDate;
    public ExcelImportEntity() {
    }
    public String getClassType() {
        return this.classType;
    }
    public String getCollectionName() {
        return this.collectionName;
    }
    public List<ExcelImportEntity> getList() {
        return this.list;
    }
    public int getSaveType() {
        return this.saveType;
    }
    public String getSaveUrl() {
        return this.saveUrl;
    }
    public void setClassType(String classType) {
        this.classType = classType;
    }
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
    public void setList(List<ExcelImportEntity> list) {
        this.list = list;
    }
    public void setSaveType(int saveType) {
        this.saveType = saveType;
    }
    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }
    public String getSuffix() {
        return this.suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public boolean isImportField() {
        return importField;
    }
    public void setImportField(boolean importField) {
        this.importField = importField;
    }
    public String getNumFormat() {
        return numFormat;
    }
    public void setNumFormat(String numFormat) {
        this.numFormat = numFormat;
    }
    public boolean isTryTransferDate() {
        return isTryTransferDate;
    }
    public void setTryTransferDate(boolean tryTransferDate) {
        isTryTransferDate = tryTransferDate;
    }
}
