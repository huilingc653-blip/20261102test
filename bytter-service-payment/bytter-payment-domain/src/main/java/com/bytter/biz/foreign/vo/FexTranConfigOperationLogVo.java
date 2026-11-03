package com.bytter.biz.foreign.vo;


import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.check.vo.IBaseDataInject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;

@Data
public class FexTranConfigOperationLogVo implements Serializable, IBaseDataInject {

    private static final long serialVersionUID = 5590113252138221587L;
    /**
     * ID
     */
    private String id;
    /**
     * 操作页面
     */
    @Dict(dictGroup = "fextranTemplateConfig.operationPage",inject ="operationPageName" )
    @BtCompositeSearch(fieldName = "p.operation_page")
    private String operationPage;
    private String operationPageName;
    /**
     * 操作内容
     */
    private String operationContent;
    /**
     * 修改前数据
     */
    private String beforeData;
    /**
     * 修改后数据
     */
    private String afterData;
    /**
     * 创建人ID
     */
    private String createBy;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建时间
     */
    @BtCompositeSearch(fieldName = "p.create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    private String createDateStr;
    /**
     * 是否删除 0否 1是
     */
    private String isDelete;


    // ========== 添加这两个方法 ==========

    /**
     * 处理 before_data 字段（兼容 Oracle CLOB）
     */
    public void setBeforeData(Object beforeData) {
        this.beforeData = convertClobToString(beforeData);
    }

    /**
     * 处理 after_data 字段（兼容 Oracle CLOB）
     */
    public void setAfterData(Object afterData) {
        this.afterData = convertClobToString(afterData);
    }

    /**
     * 将 CLOB 对象转换为 String
     */
    private String convertClobToString(Object value) {
        if (value == null) {
            return null;
        }
        // 已经是 String，直接返回
        if (value instanceof String) {
            return (String) value;
        }
        // 处理 Oracle CLOB
        if (value instanceof Clob) {
            try {
                Clob clob = (Clob) value;
                // 获取 CLOB 长度
                long length = clob.length();
                // 读取 CLOB 内容
                return clob.getSubString(1, (int) length);
            } catch (SQLException e) {
                e.printStackTrace();
                return value.toString();
            }
        }
        // 其他类型，转成字符串
        return value.toString();
    }
}
