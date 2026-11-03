package com.bytter.credit.dto;

import java.io.Serializable;

/**
 * 通用保存/提交请求 DTO,组合业务数据与保存标志(saveType)
 *
 * @param <T> 业务数据类型
 */
public class RatSaveDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T data;
    private String saveType;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }
}
