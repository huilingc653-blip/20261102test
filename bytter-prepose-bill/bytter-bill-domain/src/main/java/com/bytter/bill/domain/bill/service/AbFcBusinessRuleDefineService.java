package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.biz.finacial.api.dto.FcBusinessRuleDefineDTO;
import com.bytter.biz.finacial.api.dto.FcBusinessRuleDefinePoolDTO;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
/**
 * 柜台账务对外接口 服务类
 * @author zhenghj
 *
 */
@Service("abFcBusinessRuleDefineService")
@Transactional
public class AbFcBusinessRuleDefineService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	@Transactional(readOnly = true)
	public FcBusinessRuleDefineDTO getBusinessRuleExp(Map<String, Object> map){
		return fcBusinessRuleDefineService.getBusinessRuleExp(map);
	}
	/**
	 * <br>
	 * <b>功能：</b> 查询资金池记账规则 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2019/5/22 <br>
	 */
	@Transactional(readOnly = true)
	public FcBusinessRuleDefinePoolDTO getBusinessRulePoolExp(String poolId, String curId, String sysRaceCode){
		return fcBusinessRuleDefineService.getBusinessRulePoolExp(poolId, curId, sysRaceCode);
	}
}
