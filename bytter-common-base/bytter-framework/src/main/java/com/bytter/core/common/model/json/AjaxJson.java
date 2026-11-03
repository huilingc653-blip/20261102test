package com.bytter.core.common.model.json;

import com.bytter.core.enums.IMessageEnum;
import lombok.Data;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

/**
 * $.ajax后需要接受的JSON
 *
 * @author zhoul
 */
@Data
public class AjaxJson<T> implements Serializable{

    private static final long serialVersionUID = 1L;
    private boolean success;// 是否成功
    private String msg;// 提示信息
    private T obj;// 其他信息
    private Map<String, Object> attributes;// 其他参数
    private IMessageEnum messageEnum;
    private String[] params;

    public AjaxJson() {
    }

    public AjaxJson(boolean success) {
        this.success = success;
    }

    public AjaxJson(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    /**
     * 成功通用返回
     *
     * @return AjaxJson{success:true,msg:common.message.success}
     */
    public static <T> AjaxJson<T> success() {
        return new AjaxJson<>(true);
    }

    /**
     * 返回成功 带data参数
     *
     * @param data 成功请求体
     * @return {success:true,msg:common.message.success,obj:object}
     */
    public static <T> AjaxJson<T> success(T data) {
        AjaxJson<T> ajaxJson = new AjaxJson<>(true);
        ajaxJson.setObj(data);
        return ajaxJson;
    }

    public static <T> AjaxJson<T> success(T data, String msg) {
        AjaxJson<T> ajaxJson = new AjaxJson<>(true, msg);
        ajaxJson.setObj(data);
        return ajaxJson;
    }

    public static <T> AjaxJson<T> failed(String msg) {
        return new AjaxJson<>(false, msg);
    }

    public static <T> AjaxJson<T> failed(String msg, T data) {
        AjaxJson<T> ajaxJson = new AjaxJson<>(false, msg);
        ajaxJson.setObj(data);
        return ajaxJson;
    }

    public AjaxJson<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public AjaxJson<T> obj(T obj) {
        this.obj = obj;
        return this;
    }

    public AjaxJson<T> success(boolean success) {
        this.success = success;
        return this;
    }

    public AjaxJson<T> messageEnum(IMessageEnum messageEnum) {
        this.messageEnum = messageEnum;
        return this;
    }

    public AjaxJson<T> params(String[] params) {
        this.params = params;
        return this;
    }


    @Override
    public String toString() {
        return "AjaxJson{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", obj=" + obj +
                ", attributes=" + attributes +
                ", messageEnum=" + messageEnum +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}

