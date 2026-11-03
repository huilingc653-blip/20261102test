package com.bytter.biz.netbank.vo;

import com.bytter.biz.expense.vo.NisExpenseDetailVO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NisBillheadUpdateVO implements Serializable {
    private String editVoucherTypeId;
    private String editPayerAcc;
    private String editPayMoney;
    private String editPayeeAcc;
    private String editPayeeAccName;
    private String editPayeeBank;
    private String editIsForindividual;
    private String editWishPayday;
    private String editPurpose;
    private String editBillType;
    private String editBillFlag;
    private NisBillheadVO nisBillheadPage;
    private String wishPayday;
    private String requiredBusiness;
    private String isAccebillIntellect;
    private String budgetSel;
    private String payItem;
    private String expFbsin;
    private String accountOver;
    private String bankaccOver;
    private String showIsPayed;
    private String externalCorp;
    private String allowDifferentBillMoney;
    private String isOpenLimit;
    private String balControlType;
    private List<NisExpenseDetailVO> listNisExpenseDetail;
    private String isSplitOrMerge;
    private List<NisBillheadVO> listMerge;
    //资金计划
    private String businessName;
    private String processSysname;
    private String isBudget;
    private String busType;
    private String billBalance;
    private BtBankAccDTO payerBankAcc;
    private String demandControl;
    private CorpDTO corp;
    private String openBillInfo;
    private String taskId;
    private String reportBudget;
    private String settlType;
    private String allowEditDownacc;
    private BtBankAccDTO prentAcc;
    private String bankAcc;
    private String accName;
    private String isMerge;
    private List<NisBillheadVO> list;
    private String ids;

    private BtCurrencyDTO rmbCurrency;

}
