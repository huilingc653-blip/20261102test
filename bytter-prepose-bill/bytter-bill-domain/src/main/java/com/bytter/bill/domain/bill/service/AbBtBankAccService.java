package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 银行账号接口服务类
 * @author zhenghj
 *
 */
@Service("abBtBankAccService")
@Transactional
public class AbBtBankAccService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 根据银行账号各个条件查询银行账号列表
	 *
	 * @param dto
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BtBankAccDTO> getBankAccList(BtBankAccDTO dto){
		return btBankAccService.getBankAccList(dto);
	}
	/**
	 * 根据银行账户查找账户信息
	 * @param bankAcc
	 * @return
	 */
	@Transactional(readOnly = true)
	public BtBankAccDTO getBankAccInfo(String bankAcc){
		return btBankAccService.getBankAccInfo(bankAcc);
	}
	/**
	 * 根据银行账号列表查询线上票据专户。
	 *
	 * @param bankAccList 银行账号。
	 * @return 票据专户和流通方式。
	 */
	public List<Map<String, Object>> getBankModelByBankAcc(List<String> bankAccList) {
		if(CollUtil.isNotEmpty(bankAccList)){
			bankAccList=bankAccList.stream().filter(s-> StrUtil.isNotEmpty(s)).distinct().collect(Collectors.toList());
		}
		if(CollUtil.isEmpty(bankAccList)){
			return new ArrayList<>();
		}
		return btBankAccService.getBankModelByBankAcc(bankAccList);
	}

	/**
	 * 根据银行账号列表查询票据专户。
	 *
	 * @param bankAccList 银行账号。
	 * @return 票据专户。
	 */
	public Map<String, BtBankAccDTO> getBillBankAccByBankAccList(List<String> bankAccList) {
		if(CollUtil.isNotEmpty(bankAccList)){
			bankAccList=bankAccList.stream().filter(s-> StrUtil.isNotEmpty(s)).distinct().collect(Collectors.toList());
		}
		if(CollUtil.isEmpty(bankAccList)){
			return new HashMap<>();
		}
		return btBankAccService.getBillBankAccByBankAccList(bankAccList);
	}
}
