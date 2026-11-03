package com.bytter.biz.erp.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 电子回单文件下载请求参数
 *
 * @author fangcf
 * @date 2026-05-12
 */
@Data
public class BankReceiptDownReqVo implements Serializable {
    private String serialId;
}
