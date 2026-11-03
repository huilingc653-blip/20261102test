package com.bytter.system.service;

import com.bytter.system.api.dto.FcAccPlusDTO;
import com.bytter.system.dao.EnterpriseLoanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luoyj
 * @date 2024/1/24.
 */
@Service("enterpriseLoanService")
public class EnterpriseLoanService {

    @Autowired
    private EnterpriseLoanDao enterpriseLoanDao;

    public List<FcAccPlusDTO> getTallyAccTypeAccs(String netId, String corpId, String curId, List<String> accs){
        return enterpriseLoanDao.getTallyAccTypeAccs(netId,corpId,curId,accs);
    }

    public List<FcAccPlusDTO> getTallyAccByCorp(String netId, String corpId, String curId) {
        return enterpriseLoanDao.getTallyAccByCorp(netId,corpId,curId);
    }
}
