package com.bytter.system.service.impl;

import com.bytter.system.api.IEnterpriseLoanService;
import com.bytter.system.api.dto.FcAccPlusDTO;
import com.bytter.system.service.EnterpriseLoanService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author luoyj
 * @date 2024/1/24.
 */
@DubboService
public class EnterpriseLoanServiceImpl implements IEnterpriseLoanService {
    @Autowired
    private EnterpriseLoanService enterpriseLoanService;

    @Override
    public List<FcAccPlusDTO> getTallyAccTypeAccs(String netId, String corpId, String curId, List<String> accs) {
        return enterpriseLoanService.getTallyAccTypeAccs(netId,corpId,curId,accs);
    }

    @Override
    public List<FcAccPlusDTO> getTallyAccByCorp(String netId, String corpId, String curId) {
        return enterpriseLoanService.getTallyAccByCorp(netId,corpId,curId);
    }
}
