package com.bytter.biz.bdlinks.service.impl;

import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.erp.dto.ErpPayInfoDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qijh
 */
@Service("iErpPayInfoServiceImpl")
@DubboService
@Transactional
public class ErpPayInfoServiceImpl implements com.bytter.biz.payment.api.service.IErpPayInfoService {

	@Autowired
    private com.bytter.erp.service.IErpPayInfoErpService iErpPayInfoErpService;
    /**
     * 更新ERP中间表（付款删除退单反写接口）
     * @param id
     * @param busType
     */
    @Override
    public void updateErpPayInfo(String id, String busType){
    	ErpPayInfoDTO erpPayInfo = iErpPayInfoErpService.getById(id);
        if(erpPayInfo != null && StringUtils.isNotBlank(erpPayInfo.getId())){
            erpPayInfo.setBusType(busType);
            erpPayInfo.setBackDate(DateUtils.str2Date(DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL), DateUtils.datetimeFormat()));
            iErpPayInfoErpService.updateEntity(erpPayInfo);
        }
    }
    /**
     * erp标准接口保存
     * @param info
     * @return
     */
    @Override
    public String saveErpPayInfo(com.bytter.biz.payment.api.dto.ErpPayInfoDTO info) {
    	com.bytter.erp.dto.ErpPayInfoDTO dto = new com.bytter.erp.dto.ErpPayInfoDTO();
        BeanUtils.copyNotNullProperties(info, dto);
        return iErpPayInfoErpService.saveEntity(dto);
    }
}
