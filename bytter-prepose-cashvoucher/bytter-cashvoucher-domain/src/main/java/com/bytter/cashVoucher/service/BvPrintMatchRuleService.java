package com.bytter.cashVoucher.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.cashVoucher.dao.BvPrintMatchRuleDao;
import com.bytter.cashVoucher.entity.BvPrintMatchRule;
import com.bytter.cashVoucher.vo.BvPrintMatchRuleVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
* @Title: Service
* @Description: 凭证打印格式匹配规则
* @author onlineGenerator
* @date 2017-05-31 15:25:46
* @version 1.0
*
*/
@Service("bvPrintMatchRuleService")
@Transactional
public class BvPrintMatchRuleService extends BaseService<BvPrintMatchRule,String> {
	@Autowired
	private BvPrintMatchRuleDao bvPrintMatchRuleDao;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(BvPrintMatchRule entity){
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
	* 更新
	* @param entity
	*/
	public void update(BvPrintMatchRule entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(BvPrintMatchRule entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<BvPrintMatchRule, String> getEntityDao() {
		return bvPrintMatchRuleDao;
	}
	
	/**
	 * 根据打印格式主信息ID
	 * 获取凭证打印格式匹配规则信息
	 *
	 * @return list集合
	 */
	@Transactional(readOnly = true)
	public List<BvPrintMatchRule> listPrintMatchRuleInfoByPrintId(String printId) {
		if(StringUtils.isBlank(printId)||"undefined".equals(printId)){
			return new ArrayList<BvPrintMatchRule>();
		}
		// 远程获取字典表数据
		List<BvPrintMatchRule> matchRuleList = bvPrintMatchRuleDao.listPrintMatchRuleInfoByPrintId(printId);
		if (ObjectUtil.isNotEmpty(matchRuleList)){
			Map<String, DictDTO> listMap = dictService.listMap("bv_print_match_rule");
			if (ObjectUtil.isNotEmpty(listMap)){
				matchRuleList.forEach(rule->{
					if (ObjectUtil.isNotEmpty(rule.getMatchRule())){
						Optional.ofNullable(listMap.get(rule.getMatchRule())).ifPresent(dict->rule.setMatchRuleName(dict.getName()));
					}
				});
			}
		}
		return matchRuleList;
	}
	
	/**
	 * 批量新增凭证打印格式匹配规则
	 */
	public void batchBvPrintMatchRule(List<BvPrintMatchRule> listprintMR,String printId) {
		if(StringUtils.isNotBlank(printId)&&!"undefined".equals(printId)){
			for(BvPrintMatchRule entity:listprintMR){
				entity.setPrintId(printId);
			}
		}
		batchBvPrintMatchRule(listprintMR);
	}
	
	/**
	 * 批量新增凭证打印格式匹配规则
	 */
	public void batchBvPrintMatchRule(List<BvPrintMatchRule> listprintMR) {
		for(BvPrintMatchRule entity:listprintMR){
			bvPrintMatchRuleDao.save(entity);
		}
	}
	
	/**
	 * 批量删除数据
	 * @return
	 */
	public boolean batchDeleteByPrintIds(String[] pinrtIds){
		if(pinrtIds!=null&&pinrtIds.length>0){
			Map<String, Object> params=new HashMap<>();
			params.put("pinrtIds", pinrtIds);
			int num=bvPrintMatchRuleDao.batchDeleteByPrintIds(params);
			if(num>0){
				return true;
			}
		}
		return true;
	}
	
	/**
	 * 批量逻辑删除数据
	 * @return
	 */
	public boolean batchLogicDeleteByPrintIds(String[] pinrtIds){
		if(pinrtIds!=null&&pinrtIds.length>0){
			Map<String, Object> params=new HashMap<>();
			params.put("ids", pinrtIds);
			int num=bvPrintMatchRuleDao.batchLogicDeleteByPrintIds(params);
			if(num>0){
				return true;
			}
		}
		return true;
	}
	
	/**
	 * 根据套打格式ID获取可以匹配的匹配规则数据
	 * @param printId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvPrintMatchRuleVo> getCanMatchInfoByPrintId(String printId){
		return bvPrintMatchRuleDao.getCanMatchInfoByPrintId(printId);
	}
}
