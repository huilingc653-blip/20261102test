package com.bytter.biz.erp.entity;

import lombok.Data;

@Data
public class ErpBankReceiptImgView implements java.io.Serializable {
    /**
     * 主键
     */
    private String serialId;
    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 文件类型: pdf,png,jpg键
     */
    private String fileType;
    /**
     * 保存方式: 1-数据库 2-FTP 6-nas盘
     */
    private String saveType;
    private String filePath;

    /**
     * 图片内容
     */
    private byte[] ebillimg;
    /**
     * PDF转图片
     */
    private byte[] pdfToImg;


}
