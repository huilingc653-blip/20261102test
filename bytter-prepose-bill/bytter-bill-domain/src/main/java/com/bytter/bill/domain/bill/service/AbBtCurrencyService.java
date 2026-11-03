package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 货币维护接口服务类
 * @author zhenghj
 *
 */
@Service("abBtCurrencyService")
@Transactional
public class AbBtCurrencyService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 根据币别名称获取ID
	 *
	 * @param curName 币别名称
	 * @return id
	 */
	@Transactional(readOnly = true)
	public String getCurrencyByName(String curName){
		return btCurrencyService.getCurrencyByName(curName);
	}
	/**
	 * 查询本位币
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public BtCurrencyDTO getNatualCurrency(){
		return btCurrencyService.getNatualCurrency();
	}
	/**
	 * 查询人民币
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public BtCurrencyDTO getRMBCurrency(){
		return btCurrencyService.getRMBCurrency();
	}
}
