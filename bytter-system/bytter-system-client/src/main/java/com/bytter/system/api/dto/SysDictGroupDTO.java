package com.bytter.system.api.dto;
import java.io.Serializable;
/**
 * @author YonJar
 * @date 2023/3/30.
 * @description 根据 TypeGroupCode, TypeCode 查询字典表，返回国际化的名称 langContext
 */
public class SysDictGroupDTO implements Serializable {
    private static final long serialVersionUID = -8373251479552088854L;
    private String TypeGroupCode;
    private String TypeCode;
    public SysDictGroupDTO() {
    }
    public SysDictGroupDTO(String typeGroupCode, String typeCode) {
        TypeGroupCode = typeGroupCode;
        TypeCode = typeCode;
    }
    public String getTypeGroupCode() {
        return TypeGroupCode;
    }
    public void setTypeGroupCode(String typeGroupCode) {
        TypeGroupCode = typeGroupCode;
    }
    public String getTypeCode() {
        return TypeCode;
    }
    public void setTypeCode(String typeCode) {
        TypeCode = typeCode;
    }
}
