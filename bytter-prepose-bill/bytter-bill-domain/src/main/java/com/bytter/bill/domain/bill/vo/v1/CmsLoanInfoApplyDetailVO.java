package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.domain.loan.entity.CmsLoanInfo;
import lombok.Data;

import java.util.List;

@Data
public class CmsLoanInfoApplyDetailVO {
    private CmsLoanInfo cmsLoanInfoPage;
    // 贷款种类Id loanGenreId
    private String loanGenreId;
    private String createDate;
    private String netName;
    private RatUrationDTO cpURationDTO;
    private String relateRat;
    private Boolean isCharging;
    private String planId;
    private String isGuarantee;
    private String isHangGuarantee;

    private List<String> idList; // 担保合同id List
    private String contractIds;// 担保合同id+担保合同号，存入业务表的字段
    private String guaWayNames;// 担保方式名称组合
    private String guaWay;// 担保方式code组合
    private String guaranteeRmk;
    private String contractCode;// 担保合同编号组合，逗号隔开
}
