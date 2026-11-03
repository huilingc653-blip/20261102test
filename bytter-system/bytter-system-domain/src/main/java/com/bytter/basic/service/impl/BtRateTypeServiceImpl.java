package com.bytter.basic.service.impl;
import com.bytter.basic.entity.BtRateType;
import com.bytter.basic.service.BtRateTypeService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtRateTypeService;
import com.bytter.system.api.dto.BtRateTypeDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service("btRateTypeServiceI")
@DubboService
@Transactional
public class BtRateTypeServiceImpl implements IBtRateTypeService {
    @Autowired
    private BtRateTypeService btRateTypeService;
    @Override
    public Integer getDecimalPlacesById(String rateTypeId) {
        BtRateType rateType = btRateTypeService.get(rateTypeId, true);
        if (rateType != null && rateType.getDecimalPlaces() != null) {
            return rateType.getDecimalPlaces();
        } else {
            return 4;
        }
    }
    @Override
    public List<BtRateTypeDTO> getAllRateType() {
        List<BtRateType> btRateTypes = btRateTypeService.listByValidSign();
        List<BtRateTypeDTO> rateTypeDTOS = new ArrayList<>();
        if (null != btRateTypes) {
            rateTypeDTOS = BeanUtils.copyListProperties(btRateTypes, BtRateTypeDTO.class);
        }
        return rateTypeDTOS;
    }
    @Override
    public BtRateTypeDTO getObjectById(String id) {
        if (EmptyUtils.isBlank(id)) {
            return null;
        }
        BtRateType rateType = btRateTypeService.get(id, true);
        if (EmptyUtils.isEmpty(rateType)) {
            return null;
        }
        BtRateTypeDTO btRateTypeDTO = new BtRateTypeDTO();
        BeanUtils.copyProperties(rateType, btRateTypeDTO);
        return btRateTypeDTO;
    }
}
