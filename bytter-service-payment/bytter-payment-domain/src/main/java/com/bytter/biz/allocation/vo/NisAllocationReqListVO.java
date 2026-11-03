package com.bytter.biz.allocation.vo;


import com.bytter.biz.payment.api.dto.NisAllocationReqListDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.vo.FcAccBalanceQueryVO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class NisAllocationReqListVO implements Serializable {
    //是否启用限额管理  1-是 0-否
    private String isOpenLimit;
    //银行账户余额不足
    private String bankaccOver;
    //银行账户余额不足
    private String accountOver;
    //企业ID
    private String erpId;
    //5-资金需求清单
    private String billFrom;
    //5-头寸调拨
    private String queryType;


    private String isAccount;
    private String isAutoAccount;
    //头寸调拨是否记账
    private String isAccountPos;
    // 请款和下拨挂接资金计划
    private String budgetSel;
    private String downItem;
    private String askItem;
    private String businessName;
    private String processSysname;

    private String createBy;
    private Date createDate;
    private String curName;
    private BtBankAccDTO payeeCorpDTO;
    private CorpDTO corp;

    //收款方内部账户信息
    private  NisAllocationReqListDTO nisAllocationReqPage ;

    private BigDecimal bal;

    private FcAccBalanceQueryVO payeeCounterAcc;

    private String  busType;

    private BigDecimal     availbal;
    private BigDecimal     payeeBal;
    private BigDecimal    payeeAvailbal;
    private String  taskId;
 private String   askAllowEditMoney;
}
