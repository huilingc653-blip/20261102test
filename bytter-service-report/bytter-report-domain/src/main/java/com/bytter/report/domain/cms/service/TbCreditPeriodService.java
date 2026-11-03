package com.bytter.report.domain.cms.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.report.domain.cms.dao.TbCreditPeriodDao;
import com.bytter.report.domain.cms.entity.TbCreditPeriod;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.enums.BaseDataMessageEnum;

/**
* @Title: Service
* @Description: 贷款期限表
* @author onlineGenerator
* @date 2017-07-26 17:50:25
* @version 1.0
*
*/
@Service("tbCreditPeriodService")
@Transactional
public class TbCreditPeriodService extends BaseService<TbCreditPeriod,String> {
	@Autowired
	private TbCreditPeriodDao tbCreditPeriodDao;

	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(TbCreditPeriod entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(TbCreditPeriod entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(TbCreditPeriod entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(TbCreditPeriod entity){
 		super.saveOrUpdate(entity);
 	}


	public BaseDao<TbCreditPeriod, String> getEntityDao() {
		return tbCreditPeriodDao;
	}
	/**
	 * <b>功能介绍：</b>保存期限<br>
	 * <b>日期：</b>2017年7月31日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public void savePeriod(TbCreditPeriod entity){
		if(findCreditPeriodByCode(entity.getCode())!=null){//期限是否存在
			throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_CREDITPERIOD_EXIST);
		}
		entity.setIsSysvalue("0");
		setCreditDays(entity);//设置贷款总天数
		super.saveEntity(entity);
	}
	
	/**
	 * <b>功能介绍：</b>设置贷款总天数<br>
	 * <b>日期：</b>2017年9月9日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param entity
	 */
	public void setCreditDays(TbCreditPeriod entity){
		if(!EmptyUtils.isEmpty(entity)){
			int period = entity.getPeriod();
			int cycle = entity.getPeriodCycle();
			int creditDays = 0;
			if(cycle == 1){//年
				creditDays = period*360;
			}else if(cycle == 2){//半年
				creditDays = period*180;
			}else if(cycle == 3){//季
				creditDays = period*90;
			}else if(cycle == 4){//月
				creditDays = period*30;
			}else if(cycle == 5){//周
				creditDays = period*7;
			}else{//日
				creditDays = period*1;
			}
			entity.setCreditDays(creditDays);
		}
	}
	
	/**
	 * <b>功能介绍：</b>是否允许操作<br>
	 * <b>日期：</b>2017年7月27日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public void isAllowOp(TbCreditPeriod entity){
		/*if("1".equals(entity.getIsSysvalue())){//系统预设不能操作
			throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_SYSTEM_SET);
		}*/
		
		if(tbCreditPeriodDao.isReferedRate(entity)){//被引用
			throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_BASERATE_IS_USED);
		}
	}
	
	/**
	 * <b>功能介绍：</b>获取人民币期限<br>
	 * <b>日期：</b>2017年7月27日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public List<TbCreditPeriod> getrmbTbCreditPeriod() {
		return tbCreditPeriodDao.getrmbTbCreditPeriod();
	}
	/**
	 * <b>功能介绍：</b>获取外币期限<br>
	 * <b>日期：</b>2017年7月27日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public List<TbCreditPeriod> getforeignTbCreditPeriod(){
		return tbCreditPeriodDao.getforeignTbCreditPeriod();
	}
	/**
	 * <b>功能介绍：</b>根据编号查找期限<br>
	 * <b>日期：</b>2017年7月31日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public TbCreditPeriod findCreditPeriodByCode(String code){
		return tbCreditPeriodDao.findCreditPeriodByCode(code);
	}

	/**
	 * <b>功能介绍：</b>获取所有期限<br>
	 * <b>日期：</b>2017年7月27日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public List<TbCreditPeriod> getAll() {
		return tbCreditPeriodDao.getrmbTbCreditPeriod();
	}
}