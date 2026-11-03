package com.bytter.system.api.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class ExternalCorpSearchDTO implements Serializable {

   //所属单位ID
    private String corpId;

   //审核状态：0:暂存，11:待审核，95:审核通过，-1:打回
    private Integer auditStatus;

  //是否有效（0:无效, 1:有效
    private Integer status;

    //客商属性：00-一般客商，01-黑名单，02-白名单
    private String bwType;

   //客户编号
    private String code;


    //客户名称
    private String name;
}
