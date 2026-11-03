package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 柜台账务对外接口 服务类
 * @author zhenghj
 *
 */
@Service("abFcBusRaceService")
@Transactional
public class AbFcBusRaceService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private IFcBusRaceService fcBusRaceService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	 /**
	  * 功能介绍 : 自动写入网点的业务种类。<br>
	  * <b>日期：</b>2017年9月29日<br>
	  * @version 1.0
	  * @author liyh
	  */
	 @Transactional(readOnly = true)
	 public String getNetBusRace(String sysRaceCode, String netId, String userId){
	 	return fcBusRaceService.getNetBusRace(sysRaceCode, netId, userId);
	 }
}
