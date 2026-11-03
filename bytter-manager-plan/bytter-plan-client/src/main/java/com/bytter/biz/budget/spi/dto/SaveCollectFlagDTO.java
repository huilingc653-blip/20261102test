package com.bytter.biz.budget.spi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SaveCollectFlagDTO implements Serializable {
    private  List<String> corpIds;
}
