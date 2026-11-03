package com.bytter.poi.excel.entity.params;
import java.lang.reflect.Method;
import java.util.List;
/**
 * Excel基础
 * 
 * @author zhouj
 * @since 2017/6/2.
 */
public class ExcelBaseEntity {
    protected String name;
    /**
     * 对应type
     */
    private int type = 1;
    /**
     * 数据库格式
     */
    private String databaseFormat;
    /**
     * 导出日期格式
     */
    private String format;
    private String[] replace;
    /**
     * set/get方法
     */
    private Method method;
    /**
     * 这个是不是超链接,如果是需要实现接口返回对象
     */
    private boolean hyperlink;
    private List<Method> methods;
    public ExcelBaseEntity() {
    }
    public String getDatabaseFormat() {
        return this.databaseFormat;
    }
    public String getFormat() {
        return this.format;
    }
    public Method getMethod() {
        return this.method;
    }
    public List<Method> getMethods() {
        return this.methods;
    }
    public String getName() {
        return this.name;
    }
    public String[] getReplace() {
        return this.replace;
    }
    public int getType() {
        return this.type;
    }
    public void setDatabaseFormat(String databaseFormat) {
        this.databaseFormat = databaseFormat;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public void setMethod(Method method) {
        this.method = method;
    }
    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setReplace(String[] replace) {
        this.replace = replace;
    }
    public void setType(int type) {
        this.type = type;
    }
    public boolean isHyperlink() {
        return hyperlink;
    }
    public void setHyperlink(boolean hyperlink) {
        this.hyperlink = hyperlink;
    }
}
