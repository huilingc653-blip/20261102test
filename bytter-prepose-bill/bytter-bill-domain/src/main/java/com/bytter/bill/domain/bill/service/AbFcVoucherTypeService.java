package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 结算接口 服务类
 * @author zhenghj
 *
 */
@Service("abFcVoucherTypeService")
@Transactional
public class AbFcVoucherTypeService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private IFcVoucherTypeService fcVoucherTypeService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 获取系统凭证种类(付款方式)
	 * @param sysVoucherCode 为空默认取内转 不为空默认取第一个参数对应的值
	 * @return
	 */
	@Transactional(readOnly = true)
	public FcVoucherTypeDTO getSysFcVoucherType(String... sysVoucherCode){
		return fcVoucherTypeService.getSysFcVoucherType(sysVoucherCode);
	}
}
