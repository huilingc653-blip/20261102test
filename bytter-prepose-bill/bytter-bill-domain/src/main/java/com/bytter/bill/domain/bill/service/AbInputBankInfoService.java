package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.dto.InputBankInfoDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * 银行联行号接口服务类
 * @author zhenghj
 *
 */
@Service("abInputBankInfoService")
@Transactional
public class AbInputBankInfoService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private IInputBankInfoService inputBankInfoService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 通过银行代码查找联行号
	 * @param bankCode
	 */
	@Transactional(readOnly = true)
	public InputBankInfoDTO getByBankCode(String bankCode){
		return inputBankInfoService.getByBankCodeAndImport(bankCode);
	}
	/**
	 * 通过银行名称查找联行号
	 * @param bankName
	 * Name
	 */
	@Transactional(readOnly = true)
	public InputBankInfoDTO getByBankName(String bankName){
		return inputBankInfoService.getByBankNameAndImport(bankName);
	}
	/**
	 * 通过银行代码查找联行号（导入专用，不关联银行类型表）
	 * @param bankCode
	 * @author zuoy 2018-07-13
	 */
	@Transactional(readOnly = true)
	public InputBankInfoDTO getByBankCodeAndImport(String bankCode){
		return inputBankInfoService.getByBankCodeAndImport(bankCode);
	}
	/**
	 * 通过银行代码查找联行号（导入专用，不关联银行类型表）
	 * @param bankName
	 * @author zuoy 2018-07-13
	 */
	@Transactional(readOnly = true)
	public InputBankInfoDTO getByBankNameAndImport(String bankName){
		return inputBankInfoService.getByBankNameAndImport(bankName);
	}
	public InputBankInfoDTO getBtInputBankInfo(String bankCode) {
		return inputBankInfoService.getByBankCode(bankCode);
	}
}
