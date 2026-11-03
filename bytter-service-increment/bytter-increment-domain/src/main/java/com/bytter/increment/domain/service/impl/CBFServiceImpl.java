package com.bytter.increment.domain.service.impl;

import java.util.Date;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.core.utils.BeanUtils;
import com.bytter.increment.api.dto.ChoBankFinanceDTO;
import com.bytter.increment.api.service.ICBFService;
import com.bytter.increment.domain.entity.ChoBankFinance;
import com.bytter.increment.domain.service.ChoBankFinanceService;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: CHO_BANK_FINANCE
 * @date 2017-08-18 14:03:30
 */
@Service("cbfService")
@Transactional
@DubboService
public class CBFServiceImpl implements ICBFService {
    @Autowired
    private ChoBankFinanceService choBankFinanceService;
   
	@Override
	public ChoBankFinanceDTO getBankFinanceByVoucherNo(String voucherNo) {
		ChoBankFinance finance = choBankFinanceService.getBankFinanceByVoucherNo(voucherNo);
		ChoBankFinanceDTO dto = new ChoBankFinanceDTO();
		BeanUtils.copyNotNullProperties(finance, dto);
		return dto;
	}
	@Override
	public int updateBankFinanceByVoucherNo(ChoBankFinanceDTO choBankFinanceDTO) {
		try {
			ChoBankFinance entity = new ChoBankFinance();
			BeanUtils.copyNotNullProperties(choBankFinanceDTO, entity);
			entity.setUpdateDate(new Date());
			choBankFinanceService.update(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	@Override
	public ChoBankFinanceDTO getChoBankFinanceById(String cbsBillId) {
		ChoBankFinance entity = choBankFinanceService.get(cbsBillId, true);
		ChoBankFinanceDTO dto = new ChoBankFinanceDTO();
		if(ObjectUtils.isEmpty(entity)) {
			return dto; 
		}
		BeanUtils.copyNotNullProperties(entity, dto);
		return dto;
	}
}
