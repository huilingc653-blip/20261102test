package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.ListUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/**
 * 账号授权票据公共service
 * @author zhenghj
 *
 */
@Service("abAccountGrantService")
@Transactional
public class AbAccountGrantService extends BaseService<String, AbAccrecords> {
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@Autowired
	private AbConfigService abConfigService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 获取有权限的账号信息
	 * @param userId 用户ID
	 * @param corpId 单位ID
	 * @param resource 需要过滤的资源单位
	 * @param accountType 账号类型
	 * @param permission 对应的授权权限
	 * @return
	 */
	public List<String> listByMultiVal(String userId, String corpId, String resource, String accountType, String permission){
		List<String> bankAccGrantList=null;
		List accountGrantList= accountGrantService.listDTOByMultiVal(userId,corpId,resource,null,accountType,permission, true);
		if(ListUtils.isNotNullOrEmpty(accountGrantList)){
			bankAccGrantList=new ArrayList<>();
			for(int i=0;i<accountGrantList.size();i++){
				BtBankAccDTO accDTO=(BtBankAccDTO)accountGrantList.get(i);
				bankAccGrantList.add(accDTO.getBankAcc());
			}
		}
		return bankAccGrantList;
//		return accountGrantService.listByMultiVal(userId,corpId,resource,null,accountType,permission);
	}
	/**
	 * 权限控制类型为账号级  及 票据参数是否按票据账户控制为是
	 * 按当前登录用户账户权限过滤是票据账号，账号权限为可转账
	 * @param input 查询输入
	 * @param resource 资源权限
	 * @param corpIds 查询单位
	 */
	public void setElectricBillAccount(DataTablesInput input,String resource,String corpIds) {
		String controlType = abConfigService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
		//权限控制类型为账号级  及 票据参数是否按票据账户控制为是
		if (CommonConstant.NO.equals(controlType) && abConfigService.isElectricBill()) {
			//设置账号授权控制
			List<String> accountGrantList = this.listByMultiVal(input.getCurrentUserId(), corpIds, resource,
					AccountGrantConstant.ONLINE_BANK_TYPE + "," + AccountGrantConstant.OFFLINE_BANK_TYPE +"," + AccountGrantConstant.ELECTRIC_BILL_BANK_TYPE,
					AccountGrantConstant.TRANSFER_ACCOUNT);
			//如果有多个资源，判断账号是否有值，有值则追加
			setAccountGrantList(input, accountGrantList);
		}
	}
	/**
	 * 权限控制类型为账号级  及 票据参数是否按票据账户控制为是
	 * 按当前登录用户账户权限过滤是票据账号，账号权限为查询明细
	 * @param input 查询输入
	 * @param resource 资源权限
	 * @param corpIds 查询单位
	 */
	public void setElectricBillAccountIsDetail(DataTablesInput input,String resource,String corpIds) {
		String controlType = abConfigService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
		//权限控制类型为账号级  及 票据参数是否按票据账户控制为是
		if (CommonConstant.NO.equals(controlType) && abConfigService.isElectricBill()) {
			//设置账号授权控制
			List<String> accountGrantList = this.listByMultiVal(input.getCurrentUserId(), corpIds, resource,
					AccountGrantConstant.ONLINE_BANK_TYPE + "," + AccountGrantConstant.OFFLINE_BANK_TYPE +"," + AccountGrantConstant.ELECTRIC_BILL_BANK_TYPE,
					AccountGrantConstant.QUERY_DETAIL);
			//如果有多个资源，判断账号是否有值，有值则追加
			setAccountGrantList(input, accountGrantList);
		}
	}
	/**
	 * 如果有多个资源，判断账号是否有值，有值则追加
	 * @param input
	 * @param accountGrantList
	 */
	private void setAccountGrantList(DataTablesInput input, List<String> accountGrantList) {
		if(input.getFormdata().containsKey("accountGrantList")) {
			List<String> accountGrantListOld = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			accountGrantListOld.addAll(accountGrantList);
			//去重
			HashSet h = new HashSet(accountGrantListOld);
			accountGrantListOld.clear();
			accountGrantListOld.addAll(h);
			input.getFormdata().put("accountGrantList", accountGrantListOld);
		}else{
			//去重
			if(!CollectionUtils.isEmpty(accountGrantList)){
				HashSet h = new HashSet(accountGrantList);
				accountGrantList.clear();
				accountGrantList.addAll(h);
				input.getFormdata().put("accountGrantList", accountGrantList);
			}
		}
	}
}
