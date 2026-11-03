package com.bytter.biz.finacial.paramconfig.service.impl;
import com.alibaba.excel.util.ObjectUtils;
import com.bytter.biz.finacial.api.dto.FcVoucherCashDTO;
import com.bytter.biz.finacial.api.service.IFcVoucherCashService;
import com.bytter.biz.finacial.paramconfig.service.FcVoucherCashService;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherCashVo;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author YonJar
 * @date 2023/4/23.
 * @description
 *
 */
@Service("fcVoucherCashServiceImpl")
@Transactional
@DubboService
public class FcVoucherCashServiceImpl implements IFcVoucherCashService {
    @Autowired
    private FcVoucherCashService fcVoucherCashService;
    @Override
    public FcVoucherCashDTO getItemBySubject(String debitItem, String creditItem) {
        FcVoucherCashVo itemBySubject = fcVoucherCashService.getItemBySubject(debitItem, creditItem);
        FcVoucherCashDTO fcVoucherCashDTO = new FcVoucherCashDTO();
        if(!ObjectUtils.isEmpty(itemBySubject)) {
        	 BeanUtils.copyProperties(itemBySubject,fcVoucherCashDTO);
        }
        return fcVoucherCashDTO;
    }
}
