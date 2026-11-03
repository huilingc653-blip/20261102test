package com.bytter.credit.domain.service.impl;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.credit.api.IRatBkrationService;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.system.api.dto.BtBankDTO;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luxy
 * @Title: RatBkrationServiceImpl
 * @ProjectName T3(3.2)
 * @Description: TODO
 * @date 2020/5/7 15:54
 */
@Service("ratBkrationServiceImpl")
@Transactional
@DubboService
public class RatBkrationServiceImpl implements IRatBkrationService {
    @Autowired
    private RatBkrationService ratBkrationService;
    @Override
    public void occupyGuaContract(String id) {
        ratBkrationService.updateGuaranteeConfig(id, CommonConstant.TRUE);
    }
    @Override
    public void cancelGuaContract(String id) {
        ratBkrationService.updateGuaranteeConfig(id, CommonConstant.FALSE);
    }
    
    @Override
    public List<com.bytter.credit.dto.BtBankDTO> queryRationBank() {
    	List<BtBankDTO> list = ratBkrationService.queryRationBank();
    	if(CollectionUtils.isEmpty(list)) {
    		return null;
    	}
    	List<com.bytter.credit.dto.BtBankDTO> listVO = BeanUtils.copyListProperties(list, com.bytter.credit.dto.BtBankDTO.class);
        return listVO;
    }
    
    
}
