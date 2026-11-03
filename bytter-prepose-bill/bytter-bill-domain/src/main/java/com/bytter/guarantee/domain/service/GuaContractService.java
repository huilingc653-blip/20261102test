package com.bytter.guarantee.domain.service;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.guarantee.api.IGuaContractServie;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("guaContractService")
@DubboService
public class GuaContractService implements IGuaContractServie {
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    /**
     * 检查是否存在挂接的担保合同
     * @param input
     * @param corpId
     * @return
     */
    @Override
    public boolean checkContractApprovedList(String corpId) {
        return guaContractInfoService.checkContractList(corpId);
    }
}
