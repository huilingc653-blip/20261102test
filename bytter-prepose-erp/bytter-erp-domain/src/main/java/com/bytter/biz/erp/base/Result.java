package com.bytter.biz.erp.base;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 接口传输结果集类
 *
 * @author zhoul
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result implements Serializable {
    private Integer status;
    private String message;
    private Object data;

    public static Result failure() {
        return failure(null);
    }
    public static Result failure(String message) {
        Result result = new Result();
        result.setStatus(0);
        result.setMessage(message);
        return result;
    }
    public static Result success() {
        return success(null);
    }
    public static Result success(String message) {
        Result result = new Result();
        result.setStatus(1);
        result.setMessage(message);
        return result;
    }

    public static Result success(String message, Object data) {
        Result result = new Result();
        result.setStatus(1);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result failure(String message, Object data) {
        Result result = new Result();
        result.setStatus(0);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
