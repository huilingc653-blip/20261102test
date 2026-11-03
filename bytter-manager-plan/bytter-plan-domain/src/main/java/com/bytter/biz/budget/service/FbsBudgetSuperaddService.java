package com.bytter.biz.budget.service;
import com.bytter.biz.budget.dao.FbsBudgetSuperaddDao;
import com.bytter.biz.budget.entity.FbsBudgetSuperadd;
import com.bytter.biz.budget.vo.FbsBudgetSuperaddDetailVO;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.ICorpParentChangVerifyService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
* @Title: Service
* @Description: FBS_BUDGET_SUPERADD
* @author onlineGenerator
* @date 2017-07-28 11:36:40
* @version 1.0
*
*/
@Service("fbsBudgetSuperaddService")
@Transactional
public class FbsBudgetSuperaddService extends BaseService<FbsBudgetSuperadd,String> implements ICorpDisableVerifyService, ICorpParentChangVerifyService {
	@Autowired
	private FbsBudgetSuperaddDao fbsBudgetSuperaddDao;
	@Autowired
	private FbsItemService fbsItemService;
	@Resource
    private IMultiLangService multiLangServiceI;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(FbsBudgetSuperadd entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	@Override
	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(FbsBudgetSuperadd entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FbsBudgetSuperadd entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(FbsBudgetSuperadd entity){
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<FbsBudgetSuperadd, String> getEntityDao() {
		return fbsBudgetSuperaddDao;
	}
	
	/**
	 * 根据计划ID获取追加信息
	 * @param budgetId
	 * @return
	 */
	public List<FbsBudgetSuperadd> listByBudgetId(String budgetId){
		return fbsBudgetSuperaddDao.listByBudgetId(budgetId);
	}
	
	/**
	 * 根据计划ID获取追加信息
	 * @param budgetId
	 * @return
	 */
	public FbsBudgetSuperadd getLastSuperaddByBudgetId(String budgetId){
		return fbsBudgetSuperaddDao.getLastSuperaddByBudgetId(budgetId);
	}
	
	/**
	 * 计算预算科目(detail)的上报值
	 * @param dtl
	 * @param dtlMap
	 * @return
	 */
	public void caluteRelation(FbsBudgetSuperaddDetailVO dtl,HashMap<String, FbsBudgetSuperaddDetailVO> dtlMap, FbsItemVO item) {
		//获取科目计算公式
		String itemRelation =  item.getItemRelation();
		if(StringUtils.isNotEmpty(itemRelation)){
			//当存在自定义科目关系时，计算科目科目金额
			String itemRelation1 = itemRelation.replace("+", ";");
			itemRelation1 = itemRelation1.replace("-", ";");
			itemRelation1 = itemRelation1.replace("*", ";");
			itemRelation1 = itemRelation1.replace("/", ";");
			String[] itemCodes = itemRelation1.split(";");
			
			String itemRelation2 = item.getItemRelation();
			for(int i=0; i<itemCodes.length; i++){
				itemRelation2 = itemRelation2.replace(itemCodes[i], ";");
			}
			String[] operators = itemRelation2.split(";");
			
			//金额计算
			BigDecimal bMoneyTotal = BigDecimal.ZERO;
			for(int i=0; i<itemCodes.length-1; i++){
				String itemCode = itemCodes[i];//获取科目
				String itemCodeNext = itemCodes[i+1];
				String operator = operators[i+1];//获取运算符
				BigDecimal bMoneySuper = BigDecimal.ZERO;
				BigDecimal bMoneySuperNext = BigDecimal.ZERO;
				
				if(i != 0){
					bMoneySuper = bMoneyTotal;
				}else{
					if(itemCode.indexOf("k")!=-1){
						itemCode = itemCode.replace("k", "");
						if(!dtlMap.containsKey(itemCode)){
							bMoneySuper = BigDecimal.ZERO;
						}else{
							bMoneySuper = dtlMap.get(itemCode).getBsuperMoney().multiply(new BigDecimal(fbsItemService.getFbsItemByCode(itemCode).getSign()));
						}
					}else{
						bMoneySuper = new BigDecimal(itemCode);
					}
				}
				if(itemCodeNext.indexOf("k")!=-1){
					itemCodeNext = itemCodeNext.replace("k", "");
					if(!dtlMap.containsKey(itemCodeNext)){
						bMoneySuperNext = BigDecimal.ZERO;
					}else{
						bMoneySuperNext = dtlMap.get(itemCodeNext).getBsuperMoney().multiply(new BigDecimal(fbsItemService.getFbsItemByCode(itemCodeNext).getSign()));
					}
				}else{
					bMoneySuperNext = new BigDecimal(itemCodeNext);
				}
				BigDecimal resultTemp = BigDecimal.ZERO;
				//根据运算符计算
				if("+".equals(operator)){
					resultTemp = bMoneySuper.add(bMoneySuperNext);
				}else if("-".equals(operator)){
					resultTemp = bMoneySuper.subtract(bMoneySuperNext);
				}
				bMoneyTotal = resultTemp;
			}
			dtl.setBsuperMoney(bMoneyTotal);
		}
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有资金计划追加的在途数据，不允许禁用！
			int num = this.fbsBudgetSuperaddDao.getSuperaddCountByCorpId(corpId,"disable");
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.FBS,
						SysConfigConstant.BusType.FBS_SUPERADD,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.FBS_VALID_DISABLE_SUPERADD_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpParentChang(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有资金计划追加的在途数据，不允许变更！
			int num = this.fbsBudgetSuperaddDao.getSuperaddCountByCorpId(corpId,"change");
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.FBS,
						SysConfigConstant.BusType.FBS_SUPERADD,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.FBS_VALID_CHANGE_SUPERADD_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
}
