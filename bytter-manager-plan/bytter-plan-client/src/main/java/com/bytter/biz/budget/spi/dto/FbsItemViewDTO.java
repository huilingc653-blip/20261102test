package com.bytter.biz.budget.spi.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FbsItemViewDTO implements Serializable {
    private String id;
    private String parentId;
}
