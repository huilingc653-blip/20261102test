package com.bytter.biz.creditRating.vo;

import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CrContactAddressBookVo extends ExcelModel implements Serializable {

    private Integer rowNum;

    private String id;

    /**
     * 姓名
     */
    @Excel(name = "姓名")
    private String name;
    /**
     * 性别
     */
    @Excel(name = "性别")
    private String sex;
    /**
     * 职务
     */
    @Excel(name = "职务")
    private String job;
    /**
     * 电话号码
     */
    @Excel(name = "电话号码")
    private String  telephoneNum;
    /**
     * 邮箱
     */
    @Excel(name = "邮箱")
    private String email;
    /**
     * 微信号
     */
    @Excel(name = "微信号")
    private String  weChatNum;
    /**
     * qq号
     */
    @Excel(name = "qq号")
    private String qqNum;
    /**
     * 外键（结构原始数据）id
     */
    private String institutionalRawDataId;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String rmk;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建日期时间:yyyy-mm-dd hh24:mm:ss
     */
    private Date createDate;

    @Override
    public String getBeanName() {
        return "crContactAddressBookService";
    }
}
