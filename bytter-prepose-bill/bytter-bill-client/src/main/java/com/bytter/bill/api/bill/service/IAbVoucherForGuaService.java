package com.bytter.bill.api.bill.service;
import com.bytter.bill.api.bill.dto.GuaContractBillDTO;
import java.util.List;
public interface IAbVoucherForGuaService {
    /**
     * 获取所有挂接担保的票据数据
     */
    public List<GuaContractBillDTO> getGuaForbill();
}
