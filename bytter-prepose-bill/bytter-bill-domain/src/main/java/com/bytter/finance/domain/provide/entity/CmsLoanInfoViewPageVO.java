package com.bytter.finance.domain.provide.entity;

import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.domain.basedata.entity.RepayType;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.finance.domain.provide.vo.ProvideSyndicatedSendVo;
import com.bytter.finance.domain.provide.vo.ProvideSyndicatedVo;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.system.api.dto.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CmsLoanInfoViewPageVO {


    private Object loanInfo;

    private List<ProvideSyndicatedVo> syndicated;

    private List<DfIntermediateCostVO> costVOS;

    private Date processDate;

    private String sendItem;

    private Object budgetSel;

    private String isBond;

    private LoanSendVO loanSend;

    private String sendBillCode;

    private BigDecimal repayPassage;

    private BigDecimal repayFinish;

    private String show;

    private String relateRat;

    private String ratBankConfig;

    private String isOpenRat;

    private BtCurrencyDTO cur;

    private String ratBankCode;

    private RatUrationDTO urationDTO;

    private String pledgeConfig;

    private String isRelatePledge;

    private String itemNo;

    private String businessId;

    private List<GuaPledgeOccupyDTO> pledgeDTOS;

    private String businessName;

    private String processSysname;


    private String contractIds;

    private List<String> idList;

    private String guaranteeRmk;

    private String guaWayNames;

    private String guaWay;

    private String contractCode;

    private String viewFlag;

    private String isGuarantee;

    private String isHangGuarantee;

    private List<ProvideSyndicatedSendVo> syndicatedSend;

    private String enableUpload;

    private ConfigDTO relateCon;
    private ConfigDTO relateConType;
    private String planId;
    private TbRateMaintenceDTO tbLoanRateMaintence;
    private TbBaseRateDTO tbBaseRate;
    private RepayType repayType;
    private CorpDTO corp;

    private String baseCurId;
    private String showBankName;
    private String showBankName1;


    private String configIsHookBond;
    // 存在挂接关系
    FcBondApplyDTO dto;
    private String isHookBond;
    private String bondBankAcc;
    private String bondCurId;
    private BigDecimal bailmoneyRate;
    private BigDecimal bailmoney;
    private String bailmoneyContract;
    private BigDecimal bondRate;
    private ConfigDTO budgetConfig;
    private String natualCurId;
    private ConfigDTO conSegment;
    private String taskId;
    private String plMoneyUpper;


}
