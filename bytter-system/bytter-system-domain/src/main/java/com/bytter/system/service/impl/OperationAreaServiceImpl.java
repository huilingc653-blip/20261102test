package com.bytter.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IOperationAreaService;
import com.bytter.system.api.dto.OperationAreaDTO;
import com.bytter.system.dao.OperationAreaDao;
import com.bytter.system.entity.OperationArea;
import com.bytter.system.service.OperationAreaService;
import com.bytter.system.vo.OperationAreaVo;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@DubboService
@Service("operationAreaServiceImpl")
public class OperationAreaServiceImpl implements IOperationAreaService {
    @Autowired
    private OperationAreaDao operationAreaDao;

    @Autowired
    private OperationAreaService operationAreaService;
    @Override
    public OperationAreaDTO queryById(String id) {
        OperationAreaDTO dto = new OperationAreaDTO();
        if (StringUtils.isNotBlank(id)){
            List<String> codes = Lists.newArrayList();

            codes.add(id);
            List<OperationAreaDTO> operationAreaDTOS = this.queryByIds(codes);
            if (CollectionUtil.isNotEmpty(operationAreaDTOS)){
                dto= operationAreaDTOS.get(0);
            }
//            OperationArea operationArea = operationAreaDao.get(id, true);
//            BeanUtils.copyNotNullProperties(dto, operationArea);
        }
        return dto;
    }

    @Override
    public List<OperationAreaDTO> queryByIds(List<String> ids) {
        List<OperationAreaDTO> dtoList = Lists.newArrayList();
        List<OperationArea> operationAreas = operationAreaService.queryByIds(ids);
        if (!EmptyUtils.isEmpty(operationAreas)) {
            dtoList = BeanUtils.copyListProperties(operationAreas, OperationAreaDTO.class);
        }
//        List<OperationAreaDTO> operationAreaDTOS = operationAreaService.queryByCods(ids);


        return dtoList;
    }

    @Override
    public List<OperationAreaDTO> queryByCode(String code) {
        List<OperationAreaDTO> dtoList = Lists.newArrayList();
        List<OperationArea> operationAreas = operationAreaService.queryByCode(code);
        if (!EmptyUtils.isEmpty(operationAreas)) {
            dtoList = BeanUtils.copyListProperties(operationAreas, OperationAreaDTO.class);
        }
        return dtoList;
    }

    public List<OperationAreaDTO> queryByCodes(List<String> codes) {
        List<OperationAreaDTO> dtoList = Lists.newArrayList();
        List<OperationArea> operationAreas = operationAreaService.queryByCodes(codes);
        if (!EmptyUtils.isEmpty(operationAreas)) {
            dtoList = BeanUtils.copyListProperties(operationAreas, OperationAreaDTO.class);
        }
        return dtoList;
    }

    @Override
    public List<OperationAreaDTO> getOperationArea() {
        List<OperationArea> operationArea = operationAreaDao.getOperationArea();
        List<OperationAreaDTO> result = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(operationArea)){
            result = BeanUtils.copyListProperties(operationArea, OperationAreaDTO.class);
        }
        return result;
    }
}
