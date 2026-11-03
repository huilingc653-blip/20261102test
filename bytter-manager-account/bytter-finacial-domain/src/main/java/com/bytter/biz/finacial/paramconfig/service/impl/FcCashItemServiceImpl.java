package com.bytter.biz.finacial.paramconfig.service.impl;
import com.bytter.biz.finacial.api.dto.FcCashItemDTO;
import com.bytter.biz.finacial.api.service.IFcCashItemService;
import com.bytter.biz.finacial.paramconfig.entity.FcCashItem;
import com.bytter.biz.finacial.paramconfig.service.FcCashItemService;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author YonJar
 * @date 2023/4/23.
 * @description
 */
@Service("fcCashItemServiceImpl")
@DubboService
public class FcCashItemServiceImpl implements IFcCashItemService {
    @Autowired
    private FcCashItemService fcCashItemServiceI;
    @Override
    public FcCashItemDTO get(String cashFlux) {
        FcCashItem fcCashItem = fcCashItemServiceI.get(cashFlux,true);
        FcCashItemDTO fcCashItemDTO = new FcCashItemDTO();
        BeanUtils.copyProperties(fcCashItem,fcCashItemDTO);
        return fcCashItemDTO;
    }
}
