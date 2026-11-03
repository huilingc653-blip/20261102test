package com.bytter.biz.creditRating.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CrdDataSourceCategoryVo implements Serializable {

    private String id;
    /**
     *数据来源类别名称
     */
    private String name;
    /**
     *数据来源类别代码:D001-供应商;D002-子企业;D003-机构
     */
    private String code;
    /**
     *创建人
     */
    private String creator;
    /**
     *创建日期时间:yyyy-MM-dd hh24:mm:ss
     */
    private Date createDate;
}
