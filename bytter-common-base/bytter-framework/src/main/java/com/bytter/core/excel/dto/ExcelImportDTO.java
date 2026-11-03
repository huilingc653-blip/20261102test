package com.bytter.core.excel.dto;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2017/9/11.
 */
public class ExcelImportDTO implements Serializable {
    private String key;// 缓存key
    private Long allTotal;//
    private Long total;//
    private List<Object> list;// 结果集
    private List<Object> errorList;// 结果集
    private List<Object> successList;// 结果集
    private Map<String, String> map = new LinkedHashMap<String, String>();// 表头对应关系
    private LinkedHashMap<String, LinkedHashMap<String, String>> header = new LinkedHashMap<String, LinkedHashMap<String, String>>();
    private Map<String, Object> alignment = new HashMap<String, Object>();// 对齐方式 字段对齐方式
    private Map<String, Object> split = new HashMap<String, Object>();// 货币转换 字段是否进行格式化显示
    private String code; // 缓存code
    public ExcelImportDTO() {
    }
    public ExcelImportDTO(List<Object> list) {
        this.list = list;
    }
    public ExcelImportDTO(List<Object> list, LinkedHashMap<String, String> map) {
        this.list = list;
        this.map = map;
    }
    public ExcelImportDTO(List<Object> list, LinkedHashMap<String, String> map, Map<String, Object> alignment) {
        this.list = list;
        this.map = map;
        this.alignment = alignment;
    }
    public ExcelImportDTO(List<Object> list, LinkedHashMap<String, String> map, Map<String, Object> alignment,
            Map<String, Object> split) {
        this.list = list;
        this.map = map;
        this.alignment = alignment;
        this.split = split;
    }
    public ExcelImportDTO(List<Object> list, LinkedHashMap<String, String> map,
            LinkedHashMap<String, LinkedHashMap<String, String>> header) {
        this.list = list;
        this.map = map;
        this.header = header;
    }
    public ExcelImportDTO(List<Object> list, List<Object> errorList, LinkedHashMap<String, String> map,
            LinkedHashMap<String, LinkedHashMap<String, String>> header) {
        this.list = list;
        this.errorList = errorList;
        this.map = map;
        this.header = header;
    }
    public ExcelImportDTO(List<Object> list, List<Object> errorList, LinkedHashMap<String, String> map,
            Map<String, Object> alignment, Map<String, Object> split) {
        this.list = list;
        this.errorList = errorList;
        this.map = map;
        this.alignment = alignment;
        this.split = split;
    }
    public ExcelImportDTO(List<Object> list, List<Object> successList,List<Object> errorList, LinkedHashMap<String, String> map,
            Map<String, Object> alignment, Map<String, Object> split) {
        this.list = list;
        this.errorList = errorList;
        this.map = map;
        this.alignment = alignment;
        this.split = split;
        this.successList = successList;
    }
    public ExcelImportDTO(List<Object> list, List<Object> errorList, LinkedHashMap<String, String> map,
                          Map<String, Object> alignment, Map<String, Object> split,
                          LinkedHashMap<String, LinkedHashMap<String, String>> header) {
        this.list = list;
        this.errorList = errorList;
        this.map = map;
        this.alignment = alignment;
        this.split = split;
        this.header = header;
    }
    public ExcelImportDTO(List<Object> list, List<Object> successList,List<Object> errorList, LinkedHashMap<String, String> map,
                          Map<String, Object> alignment, Map<String, Object> split,
                          LinkedHashMap<String, LinkedHashMap<String, String>> header) {
        this.successList = successList;
        this.list = list;
        this.errorList = errorList;
        this.map = map;
        this.alignment = alignment;
        this.split = split;
        this.header = header;
    }
    public Long getAllTotal() {
        return allTotal;
    }
    public void setAllTotal(Long allTotal) {
        this.allTotal = allTotal;
    }
    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public List<Object> getList() {
        return list;
    }
    public void setList(List<Object> list) {
        this.list = list;
    }
    public List<Object> getErrorList() {
        return errorList;
    }
    public void setErrorList(List<Object> errorList) {
        this.errorList = errorList;
    }
    public Map<String, String> getMap() {
        return map;
    }
    public void setMap(Map<String, String> map) {
        this.map = map;
    }
    public LinkedHashMap<String, LinkedHashMap<String, String>> getHeader() {
        return header;
    }
    public void setHeader(LinkedHashMap<String, LinkedHashMap<String, String>> header) {
        this.header = header;
    }
    public Map<String, Object> getAlignment() {
        return alignment;
    }
    public void setAlignment(Map<String, Object> alignment) {
        this.alignment = alignment;
    }
    public Map<String, Object> getSplit() {
        return split;
    }
    public void setSplit(Map<String, Object> split) {
        this.split = split;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public List<Object> getSuccessList() {
        return successList;
    }

    public void setSuccessList(List<Object> successList) {
        this.successList = successList;
    }
}
