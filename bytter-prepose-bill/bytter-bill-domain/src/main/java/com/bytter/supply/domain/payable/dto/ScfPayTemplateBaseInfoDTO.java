package com.bytter.supply.domain.payable.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScfPayTemplateBaseInfoDTO {
    private String id;
    private String templateCode;
    private String templateName;
    private String templateDesc;
    private String remark;
    private String validSign;
    private List<ScfPayProductDTO> productList;
}
