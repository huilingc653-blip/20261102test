package com.bytter.bill.domain.bill.service;

import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 内部账号服务服务类
 * @author zhenghj
 *
 */
@Service("abInnerAccountService")
@Transactional
public class AbInnerAccountService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private IInnerAccountService innerAccountService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 *  根据参数筛选 内部账号
	 * @param curId
	 * @param netId
	 * @param corpId
	 * @param subjectId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<FcAccDTO> listAccountByParams(String curId, String netId, String corpId, String subjectId){
		return innerAccountService.listAccountByParams(curId, netId, corpId, subjectId);
	}
	/**
	 * 自动开户内部账号[新增内部账号]
	 * @param fcAccDTO
	 * @return 内部账号
	 */
	public String saveOrUpdate(FcAccDTO fcAccDTO, ICurrentUser user){
		return innerAccountService.saveOrUpdate(fcAccDTO, user);
	}
}
