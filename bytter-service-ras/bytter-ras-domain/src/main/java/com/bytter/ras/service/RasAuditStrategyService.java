package com.bytter.ras.service;

import com.bytter.ras.dao.RasAuditStrategyDao;
import com.bytter.ras.entity.RasAuditStrategy;
import com.bytter.ras.utils.RasUtils;
import com.bytter.ras.vo.RasAuditStrategyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.ras.api.enums.RasMessageEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
* @Title:  营收稽核策略维护业务操作类
* @author: zhengg
* @date:   2019-5-15 下午5:55:04
 */
@Service("rasAuditStrategyService")
@Transactional
public class RasAuditStrategyService extends BaseService<RasAuditStrategy, String> {

	@Autowired
	private RasAuditStrategyDao rasAuditStrategyDao;

	@Autowired
	private RasLogService rasLogService;


	@Override
	public BaseDao<RasAuditStrategy, String> getEntityDao() {
		return rasAuditStrategyDao;
	}

	/**
	 *
	* @Title: 查询营收稽核策略数据
	* @param input
	* @return 列表数据
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
		DataTablesOutput data=rasAuditStrategyDao.getDataTablesUnion(input);
		@SuppressWarnings("unchecked")
		List<RasAuditStrategyVO> infoList=data.getData();
		for(RasAuditStrategyVO info:infoList){
			RasUtils.setRulesList(info.getRules(), info.getRulesList());
			info.setLocale(input.getLocale());
			//this.inject(info.getRulesList());
		}
		return data;
	}

	/**
	 *
	* @Title: 通过ID设置策略启用和禁用
	* @param id 需要设置的ID
	* @param validSign 设置的类型 1启用0禁用
	* @param user 用户
	* @return 处理结果
	* @throws
	 */
	public AjaxJson batchValidSign(String id,String validSign,ICurrentUser user){
		AjaxJson reJson = new AjaxJson();
		if(StringUtils.isEmpty(id)){
			throw new BusinessException(RasMessageEnum.RAS_MESSAGE_AUDITSTRATEGY_NULL);
		}else{
			RasAuditStrategy info=rasAuditStrategyDao.get(id, true);
			if(null==info){
				throw new BusinessException(RasMessageEnum.RAS_MESSAGE_AUDITSTRATEGY_NULL);
			}
			info.setValidSign(validSign);
			info.setUpdateBy(user.getLoginName());
			info.setUpdateName(user.getUsername());
			info.setUpdateDate(new Date());
			rasAuditStrategyDao.update(info);
			String logType=RasConstant.RAS_LOGTYPE_STOP;
			String msg="业务进行禁用操作！";
			if(RasConstant.RAS_VALID_SIGN_1.equals(validSign)){
				logType=RasConstant.RAS_LOGTYPE_START;
				msg="业务进行启用操作！";
			}
			rasLogService.saveRasLog(info.getId(), logType, msg, user);
		}
		reJson.setSuccess(true);
		return reJson;
	}


	/**
	 *
	* @Title: 营收稽核策略数据新增或者修改操作
	* @param info 核策略数据信息
	* @param user 用户
	* @return 处理结果
	* @throws
	 */
	public AjaxJson saveOrUpdate(RasAuditStrategyVO infoVO,ICurrentUser user){
		AjaxJson reJson = new AjaxJson();
		if(infoVO==null||StringUtils.isEmpty(infoVO.getPayType())||StringUtils.isEmpty(infoVO.getPayType())){
			throw new BusinessException(RasMessageEnum.RAS_MESSAGE_AUDITSTRATEGY_NULL);
		}else{
			RasAuditStrategy info=new RasAuditStrategy();
			BeanUtils.copyNotNullProperties(infoVO, info);
			if(!RasConstant.RAS_PAYTYPE_001.equals(info.getPayType())
					&&!RasConstant.RAS_PAYTYPE_006.equals(info.getPayType())){
				info.setRules(RasConstant.RAS_AUDIT_RULES_MONEY+","+info.getRules());
			}
			if(!checkPayTypeAndRules(info.getPayType(), info.getRules(), info.getId())){
				throw new BusinessException(RasMessageEnum.RAS_MESSAGE_AUDITSTRATEGY_CHECK);
			}
			if(StringUtils.isEmpty(info.getId())){
				info.setCreateBy(user.getLoginName());
				info.setCreateName(user.getUsername());
				info.setCreateDate(new Date());
				rasAuditStrategyDao.save(info);
				rasLogService.saveRasLog(info.getId(), RasConstant.RAS_LOGTYPE_ADD, "进行新增操作！", user);
			}else{
				RasAuditStrategy oldInfo=rasAuditStrategyDao.get(info.getId(), true);
				BeanUtils.copyNotNullProperties(info, oldInfo);
				oldInfo.setUpdateBy(user.getLoginName());
				oldInfo.setUpdateName(user.getUsername());
				oldInfo.setUpdateDate(new Date());
				rasAuditStrategyDao.update(oldInfo);
				rasLogService.saveRasLog(info.getId(), RasConstant.RAS_LOGTYPE_UPDATE, "进行修改操作！", user);
			}
		}
		reJson.setSuccess(true);
		return reJson;
	}
	
	/**
	 * 检查是否有重复的缴款方式和策略 
	 * @param payType 缴款方式
	 * @param rules 策略 
	 * @param id 需要排除的ID（判断修改的时候需要排除自身）
	 * @return true不存在重复的信息 false存在重复的信息
	 */
	@Transactional(readOnly = true)
	public boolean checkPayTypeAndRules(String payType,String rules,String id){
		List<RasAuditStrategy> list=rasAuditStrategyDao.getRasAuditStrategyListByPayTypeAndRules(payType, rules, id);
		if(list==null||list.size()<1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 通过查询条件获取对应维护好的且有效策略信息
	 * @param params 查询参数
	 * @return 符合条件的策略列表
	 */
	@Transactional(readOnly = true)
	public List<RasAuditStrategy> getRasAuditStrategyValidList(Map<String,Object> params){
		params.put("validSign", RasConstant.RAS_VALID_SIGN_1);
		return getRasAuditStrategyList(params);
	}
	
	/**
	 * 通过查询条件获取对应维护好的策略信息
	 * @param params 查询参数
	 * @return 符合条件的策略列表
	 */
	@Transactional(readOnly = true)
	public List<RasAuditStrategy> getRasAuditStrategyList(Map<String,Object> params){
		return rasAuditStrategyDao.getRasAuditStrategyList(params);
	}
}
