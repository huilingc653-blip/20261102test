package com.bytter.bill.domain.bill.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
/**
 * 票据配置服务类
 * @author zhenghj
 *
 */
@Service("abConfigService")
@Transactional
public class AbConfigService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private IConfigService configService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	@Transactional(readOnly = true)
	public Map<String,Object> listByType(String type){
		return configService.listByType(type);
	}
	/**
	 * 保存或更新配置
	 * @param parasMap
	 */
	public void saveOrUpdate(Map<String, String[]> parasMap){
		configService.saveOrUpdate(parasMap);
	}
	/**
	 * 根据模块和配置名获取配置的值
	 * @param type
	 * @param name
	 * @return
	 */
	@Transactional(readOnly = true)
	public ConfigDTO getConfig(String type, String name){
		return configService.getConfig(type,name);
	}
	/**
	 * 根据票据模块和配置名获取配置的值
	 * @param name
	 * @return
	 */
	@Transactional(readOnly = true)
	public ConfigDTO getBillConfig(String name){
		return configService.getConfig(AccebillConstant.AB_CONFIG_TYPE,name);
	}
	/**
	 * 是否按票据账户控制
	 * @return true 是
	 */
	@Transactional(readOnly = true)
	public boolean isElectricBill(){
		return this.getBillConfig(AccebillConstant.AB_CONFIG_ISELECTRICBILL).toBoolean(true);
	}
}
