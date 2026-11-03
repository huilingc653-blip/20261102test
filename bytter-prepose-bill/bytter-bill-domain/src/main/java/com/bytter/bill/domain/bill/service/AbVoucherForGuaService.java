package com.bytter.bill.domain.bill.service;
import com.bytter.bill.api.bill.dto.GuaContractBillDTO;
import com.bytter.bill.api.bill.service.IAbVoucherForGuaService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service("abVoucherForGuaService")
@DubboService
public class AbVoucherForGuaService implements IAbVoucherForGuaService {
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Override
    public List<GuaContractBillDTO> getGuaForbill() {
        return abVoucherinfoViceService.getGuaForbill();
    }
}
