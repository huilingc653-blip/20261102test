package com.bytter.biz.wage.vo.v1;

import com.bytter.biz.wage.vo.NisWageVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel("代发工资数据页")
@Data
public class NisWagePageVO implements Serializable {

    private String isPay;

    private NisWageVO nisWagePage;

    private String busType;

    private String user;

    private String budgetSel;

    private String payItem;

    private String businessName;

    private String processSysname;

    private String isOpenLimit;
}
