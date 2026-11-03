package com.bytter.biz.netbank.vo;

import com.bytter.biz.netbank.vo.NisBillheadPoolVO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import lombok.Data;

@Data
public class NisBillheadPoolUpdateResp extends NisBillheadPoolInitConfigVO {
    private NisBillheadPoolVO nisBillheadPage;
    private BtBankAccDTO payerBankAcc;
    private String wishPayday;
    private String businessName;
    private String processSysname;
    private CorpDTO corp;
    private String requiredBusiness;
    // initEditConfig 字段
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
}
