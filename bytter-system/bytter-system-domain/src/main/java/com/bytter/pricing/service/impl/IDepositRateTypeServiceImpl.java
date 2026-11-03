package com.bytter.pricing.service.impl;
import com.bytter.pricing.entity.DepositRateType;
import com.bytter.pricing.service.DepositRateTypeService;
import com.bytter.system.pricing.dto.DepositRateTypeDTO;
import com.bytter.system.pricing.service.IDepositRateTypeService;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author luxy
 * @Title: IDepositRateTypeServiceImpl
 * @ProjectName T3(3.2)
 * @Description: TODO
 * @date 2019/11/8 14:19
 */
@Service("depositIRateTypeServiceI")
@Transactional
@DubboService
public class IDepositRateTypeServiceImpl implements IDepositRateTypeService {
    @Autowired
    private DepositRateTypeService depositIRateTypeService;
    @Override
    public List<DepositRateTypeDTO> getDepositRateType() {
        List<DepositRateType> rateTypes=depositIRateTypeService.getTypeList();
        List<DepositRateTypeDTO> rateTypeDTOS= null;
        if(null!=rateTypes&&rateTypes.size()>0){
            rateTypeDTOS=BeanUtils.copyListProperties(rateTypes, DepositRateTypeDTO.class);
        }
        return rateTypeDTOS;
    }

    @Override
    public DepositRateTypeDTO getDepositRateTypeById(String id) {
        DepositRateTypeDTO dto = new DepositRateTypeDTO();
        DepositRateType depositRateType = depositIRateTypeService.get(id);
        BeanUtils.copyNotNullProperties(depositRateType, dto);
        return dto;
    }

}
