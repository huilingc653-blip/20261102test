package com.bytter.cloud.common.core.persistence.vo;
import com.bytter.cloud.common.core.constant.CommonConstant;
import com.bytter.cloud.common.core.exception.CommonOperationMessage;
import com.bytter.cloud.common.core.exception.ErrorType;
import com.bytter.cloud.common.core.exception.IMessage;
import com.bytter.core.enums.IMessageEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
/**
 * rest请求结果返回实体
 *
 * @author zhoul 2021-01-14
 * @since 1.0.0
 */
@Getter
public class ResultAuth<T> implements Serializable {
    private boolean success;
    @Deprecated
    private String code;
    
    private String authstatus;
    private String msg;
    private Object msgParams;
    private final Date time;
    
    private String status;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    public ResultAuth() {
        this.time = new Date();
    }
    /**
     * 内部使用，用于构造成功的结果
     *
     * @param success   是否成功
     * @param msg       消息信息
     * @param msgParams 消息国际化参数
     * @param data      携带数据
     * @param msgMaps   map类型的国际化参数
     */
    public ResultAuth(boolean success, T data, IMessage msg, Map<String, Object> msgMaps, Object... msgParams) {
        this.success = success;
        if (msg != null) {
            if (msg instanceof ErrorType) {
                this.code = ((ErrorType) msg).getCode();
            }
            String name = msg.getName();
            if (StringUtils.hasText(name)) {
                this.msg = name;
            } else {
                // 增加判断，分布式事务中返回值可能出现大小写，直接转换为小写后，页面显示错误
                if (msg.name().contains(CommonConstant.UNDERLINE_SPLIT)) {
                    this.msg = msg.name().toLowerCase().replace(CommonConstant.UNDERLINE_SPLIT, ".");
                } else {
                    this.msg = msg.name();
                }
            }
        }
        //如果map参数不为空，则赋值
        if (!CollectionUtils.isEmpty(msgMaps)) {
            this.msgParams = msgMaps;
        }
        //如果map参数不为空，则赋值
        if (!Objects.isNull(msgParams) && msgParams.length > 0) {
            this.msgParams = msgParams;
        }
        this.data = data;
        this.time = new Date();
    }
    
    public ResultAuth(boolean success, T data, IMessage msg, String status) {
        this.success = success;
        if (msg != null) {
            if (msg instanceof ErrorType) {
                this.code = ((ErrorType) msg).getCode();
            }
            String name = msg.getName();
            if (msg instanceof ErrorType) {
                this.msg = ((ErrorType) msg).getMsg();
            }else if (StringUtils.hasText(name)) {
                this.msg = name;
            } else {
                // 增加判断，分布式事务中返回值可能出现大小写，直接转换为小写后，页面显示错误
                if (msg.name().contains(CommonConstant.UNDERLINE_SPLIT)) {
                    this.msg = msg.name().toLowerCase().replace(CommonConstant.UNDERLINE_SPLIT, ".");
                } else {
                    this.msg = msg.name();
                }
            }
        }
        this.status = status;
        this.data = data;
        this.time = new Date();
    }
    
    
    public ResultAuth(boolean success, T data, IMessageEnum msg, Map<String, Object> msgMaps, Object... msgParams) {
        this.success = success;
        if (msg != null) {
            String name = msg.getEnum().name();
            // 增加判断，分布式事务中返回值可能出现大小写，直接转换为小写后，页面显示错误
            if (name.contains(CommonConstant.UNDERLINE_SPLIT)) {
                this.msg = name.toLowerCase().replace(CommonConstant.UNDERLINE_SPLIT, ".");
            } else {
                this.msg = name;
            }
        }
        //如果map参数不为空，则赋值
        if (!CollectionUtils.isEmpty(msgMaps)) {
            this.msgParams = msgMaps;
        }
        //如果map参数不为空，则赋值
        if (!Objects.isNull(msgParams) && msgParams.length > 0) {
            this.msgParams = msgParams;
        }
        this.data = data;
        this.time = new Date();
    }
    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code 代码
     * @param msg  消息信息
     * @param data 携带数据
     */
    public ResultAuth(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.time = new Date();
    }
    /**
     * 返回成功
     *
     * @param <T> 返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> success() {
        return success(CommonOperationMessage.COMMON_TIPS_SUCCESS);
    }
    /**
     * 返回成功
     *
     * @param data 返回数据
     * @param <T>  返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> success(T data) {
        return new ResultAuth<>(Boolean.TRUE, data, CommonOperationMessage.COMMON_TIPS_SUCCESS, null);
    }
    /**
     * 返回成功
     *
     * @param msg 国际化枚举
     * @param <T> 返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> success(IMessage msg) {
        return new ResultAuth<>(Boolean.TRUE, null, msg, null);
    }
    /**
     * 返回成功
     *
     * @param msg     国际化枚举
     * @param msgMaps 国际化枚举参数补全
     * @param <T>     返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> success(IMessage msg, Map<String, Object> msgMaps) {
        return new ResultAuth<>(Boolean.TRUE, null, msg, msgMaps);
    }
    /**
     * 返回成功
     *
     * @param msg       国际化枚举
     * @param msgParams 国际化枚举参数补全
     * @param <T>       返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> success(IMessage msg, Object... msgParams) {
        return new ResultAuth<>(Boolean.TRUE, null, msg, null, msgParams);
    }
    /**
     * 返回成功
     *
     * @param data 返回数据
     * @param msg  国际化枚举
     * @param <T>  返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> success(T data, IMessage msg) {
        return new ResultAuth<>(Boolean.TRUE, data, msg, null);
    }
    /**
     * 返回成功
     *
     * @param data    返回数据
     * @param msg     国际化枚举
     * @param msgMaps 国际化枚举参数补全
     * @param <T>     返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> success(T data, IMessage msg, Map<String, Object> msgMaps) {
        return new ResultAuth<>(Boolean.TRUE, data, msg, msgMaps);
    }
    /**
     * 返回成功
     *
     * @param data      返回数据
     * @param msg       国际化枚举
     * @param msgParams 国际化枚举参数补全
     * @param <T>       返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> success(T data, IMessage msg, Object... msgParams) {
        return new ResultAuth<>(Boolean.TRUE, data, msg, null, msgParams);
    }
    /**
     * 返回失败
     *
     * @param <T> 返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> fail() {
        return new ResultAuth<>(Boolean.FALSE, null, CommonOperationMessage.COMMON_TIPS_FAIL, null);
    }
    /**
     * 返回失败
     *
     * @param data 返回数据
     * @param <T>  返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> fail(T data) {
        return new ResultAuth<>(Boolean.FALSE, data, CommonOperationMessage.COMMON_TIPS_FAIL, null);
    }
    /**
     * 返回失败
     *
     * @param msg 国际化枚举
     * @param <T> 返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> fail(IMessage msg) {
        return new ResultAuth<>(Boolean.FALSE, null, msg, null);
    }
    
    public static <T> ResultAuth<T> failAuth(IMessage msg,String status) {
        return new ResultAuth<>(Boolean.FALSE, null, msg,status);
    }
    /**
     * 返回失败
     *
     * @param msg 国际化枚举
     * @param <T> 返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> fail(IMessageEnum msg) {
        return new ResultAuth<>(Boolean.FALSE, null, msg, null);
    }
    /**
     * 返回失败
     *
     * @param msg     国际化枚举
     * @param msgMaps 国际化枚举参数补全
     * @param <T>     返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> fail(IMessage msg, Map<String, Object> msgMaps) {
        return new ResultAuth<>(Boolean.FALSE, null, msg, msgMaps);
    }
    /**
     * 返回失败
     *
     * @param msg       国际化枚举
     * @param msgParams 国际化枚举参数补全
     * @param <T>       返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> fail(IMessage msg, Object... msgParams) {
        return new ResultAuth<>(Boolean.FALSE, null, msg, null, msgParams);
    }
    
    public static <T> ResultAuth<T> fail(IMessageEnum msg, Object... msgParams) {
        return new ResultAuth<>(Boolean.FALSE, null, msg, null, msgParams);
    }
    /**
     * 返回失败
     *
     * @param data 返回数据
     * @param msg  国际化枚举
     * @param <T>  返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> fail(T data, IMessage msg) {
        return new ResultAuth<>(Boolean.FALSE, data, msg, null);
    }
    /**
     * 返回失败
     *
     * @param data    返回数据
     * @param msg     国际化枚举
     * @param msgMaps 国际化枚举参数补全
     * @param <T>     返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> fail(T data, IMessage msg, Map<String, Object> msgMaps) {
        return new ResultAuth<>(Boolean.FALSE, data, msg, msgMaps);
    }
    /**
     * 返回失败
     *
     * @param data      返回数据
     * @param msg       国际化枚举
     * @param msgParams 国际化枚举参数补全
     * @param <T>       返回结果的类型
     * @return Result
     */
    public static <T> ResultAuth<T> fail(T data, IMessage msg, Object... msgParams) {
        return new ResultAuth<>(Boolean.FALSE, data, msg, null, msgParams);
    }
    /**
     * 成功 true
     *
     * @return true/false
     */
    public boolean isSuccess() {
        return success;
    }
    /**
     * 失败
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isFail() {
        return !success;
    }
    
    public static <T> ResultAuth<T> fail(String code, String msg, T data) {
        return new ResultAuth<>(code, msg, data);
    }
}
