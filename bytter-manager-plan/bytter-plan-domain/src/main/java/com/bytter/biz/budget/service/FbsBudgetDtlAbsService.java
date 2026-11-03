package com.bytter.biz.budget.service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICorpService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.budget.dao.FbsBudgetDtlAbsDao;
import com.bytter.biz.budget.entity.FbsBudgetDtlAbs;
import com.bytter.biz.budget.vo.FbsBudgetDtlAbsVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.util.StopWatch;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FBS_BUDGET_DTL_ABS
 * @date 2017-07-18 11:25:46
 */
@Service("fbsBudgetDtlAbsService")
@Transactional
public class FbsBudgetDtlAbsService extends BaseService<FbsBudgetDtlAbs, String> {
	@Autowired
	private FbsBudgetDtlAbsDao fbsBudgetDtlAbsDao;
	@DubboReference
	private ICorpService corpService;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(FbsBudgetDtlAbs entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(FbsBudgetDtlAbs entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FbsBudgetDtlAbs entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FbsBudgetDtlAbs entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<FbsBudgetDtlAbs, String> getEntityDao() {
		return fbsBudgetDtlAbsDao;
	}
	/**
	 * 根据计划主表ID获取计划明细
	 *
	 * @param budgetId
	 * @param timeType
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> listByBudgetId(String budgetId, String timeType) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		return fbsBudgetDtlAbsDao.listByBudgetId(tableName, budgetId);
	}
	/**
	 * 根据计划主表ID获取计划明细
	 *
	 * @param budgetIds
	 * @param timeType
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> listSumByBudgetIds(List<String> budgetIds, String timeType) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		return fbsBudgetDtlAbsDao.listSumByBudgetIds(tableName, budgetIds);
	}
	/**
	 * 根据计划主表ID获取计划明细
	 *
	 * @param budgetIds
	 * @param timeType
	 * @return
	 */
	public Map<String,List<FbsBudgetDtlAbsVO>> listByBudgetIds(List<String> budgetIds, String timeType) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		StopWatch stopWatch = new StopWatch("listByBudgetIds");
		stopWatch.start("fbsBudgetDtlAbsDao.listByBudgetIds");
		List<FbsBudgetDtlAbsVO> dtls = fbsBudgetDtlAbsDao.listByBudgetIds(tableName, budgetIds);
		Map<String,List<FbsBudgetDtlAbsVO>> result = Maps.newHashMap();
		if(EmptyUtils.isEmpty(dtls)){
			return result;
		}
		for(FbsBudgetDtlAbsVO dtl:dtls){
			List<FbsBudgetDtlAbsVO> dtlsForOneBudget = result.get(dtl.getBudgetId());
			if(EmptyUtils.isEmpty(dtlsForOneBudget)){
				dtlsForOneBudget = Lists.newArrayList();
				result.put(dtl.getBudgetId(),dtlsForOneBudget);
			}
			dtlsForOneBudget.add(dtl);
		}
		stopWatch.stop();
		logger.debug(" listByBudgetIds all spends :"+stopWatch.prettyPrint()+", data size :"+dtls.size());
		return result;
	}
	/**
	 * 根据计划Id获取明细列表（折算本位币）
	 *
	 * @param budgetId
	 * @param timeType
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> listByBudgetIdConvertCur(String budgetId, String timeType) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		return fbsBudgetDtlAbsDao.listByBudgetIdConvertCur(tableName, budgetId);
	}
	/**
	 * 根据明细Id获取明细
	 *
	 * @param id
	 * @param timeType
	 * @return
	 */
	public FbsBudgetDtlAbs getById(String id, String timeType) {
		String entityName = FbsBudgetDtlAbs.getEntityNameByTimeType(timeType);
		return fbsBudgetDtlAbsDao.getById(entityName, id);
	}
	/**
	 * 根据计划主表ID删除明细
	 *
	 * @param budgetId
	 * @param timeType
	 */
	public void deleteByBudgetId(String budgetId, String timeType) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		fbsBudgetDtlAbsDao.deleteByBudgetId(tableName, budgetId);
	}
	/**
	 * 根据明细ID更新明细上报金额
	 * @param dtlId
	 * @param timeType
	 * @param money
	 */
	public void updateByDtlId(String dtlId, String timeType,BigDecimal money) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		fbsBudgetDtlAbsDao.updateByDtlId(tableName, dtlId,money);
	}
	/**
	 * 清除上报计划的上报金额
	 *
	 * @param budgetId
	 * @param timeType
	 */
	public void clearInputMoney(String budgetId, String timeType) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		fbsBudgetDtlAbsDao.clearInputMoney(tableName, budgetId);
	}
	/**
	 * 清除本次计划追加金额
	 *
	 * @param budgetId
	 * @param timeType
	 */
	public void clearSuperaddTempMoney(String budgetId, String timeType) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		fbsBudgetDtlAbsDao.clearSuperaddTempMoney(tableName, budgetId);
	}
	/**
	 * 设置追加金额
	 *
	 * @param budgetId
	 * @param timeType
	 */
	public void setSuperaddMoney(String budgetId, String timeType) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		fbsBudgetDtlAbsDao.setSuperaddMoney(tableName, budgetId);
	}
	/**
	 * 汇总计划上报明细
	 *
	 * @param timeType
	 * @param joinSql
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> collectByBudgetIds(String timeType, String joinSql) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		return fbsBudgetDtlAbsDao.collectByBudgetIds(tableName, joinSql);
	}
	/**
	 * 汇总计划上报明细
	 *
	 * @param timeType
	 * @param joinSql
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> collectByBudgetIdsNormal(String timeType, String joinSql) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		return fbsBudgetDtlAbsDao.collectByBudgetIdsNormal(tableName, joinSql);
	}
	/**
	 * 根据条件获取明细
	 *
	 * @param budgetId 预算ID
	 * @param itemCode 科目编码
	 * @param timeType 科目名称
	 * @return
	 */
	public FbsBudgetDtlAbs getByBudgetId(String budgetId, String itemCode, String timeType) {
		String entityName = FbsBudgetDtlAbs.getEntityNameByTimeType(timeType);
		return fbsBudgetDtlAbsDao.getByBudgetId(entityName, budgetId, itemCode);
	}
	/**
	 * 汇总下级单位某科目上报金额
	 *
	 * @param itemCode
	 * @param parentCorpId
	 * @return
	 */
	public BigDecimal collectMoneySameLevelCorp(String itemCode, String parentCorpId, String timeType, Date beginDate, Date endDate) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		//调用sys基础域接口，获取数据
		List<String> corpIds = corpService.getCorpIdByParentId(parentCorpId);
		return fbsBudgetDtlAbsDao.collectMoneySameLevelCorp2(itemCode, corpIds, tableName, beginDate, endDate);
//		return fbsBudgetDtlAbsDao.collectMoneySameLevelCorp(itemCode, parentCorpId, tableName, beginDate, endDate);
	}
	/**
	 * 汇总下级单位明细
	 *
	 * @param itemCode
	 * @param parentCorpId
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> collectListSameLevelCorp(String itemCode, String parentCorpId, String timeType, Date beginDate, Date endDate) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		List<String> corpIds = corpService.getCorpIdByParentId(parentCorpId);
		return fbsBudgetDtlAbsDao.collectListSameLevelCorp2(itemCode, corpIds, tableName, beginDate, endDate);
//		return fbsBudgetDtlAbsDao.collectListSameLevelCorp(itemCode, parentCorpId, tableName, beginDate, endDate);
	}
	/**
	 * 查询上报累计净额
	 *
	 * @param budgetId
	 * @return
	 */
	public BigDecimal getNetAmount(String budgetId, String timeType) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		return fbsBudgetDtlAbsDao.getNetAmount(budgetId, tableName);
	}
	public List<FbsBudgetDtlAbsVO> listByBudgetIdConvertCur(Map<String, Set<String>> timeType2BudgetIds) {
		List<FbsBudgetDtlAbsVO> result= Lists.newArrayList();
		if(EmptyUtils.isEmpty(timeType2BudgetIds)){
			return result;
		}
		for(String timeType:timeType2BudgetIds.keySet()) {
			String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
			List<FbsBudgetDtlAbsVO> fbsBudgetDtlAbsVOList = fbsBudgetDtlAbsDao.listByBudgetIdConvertCur(tableName, timeType2BudgetIds.get(timeType));
			if(EmptyUtils.isNotEmpty(fbsBudgetDtlAbsVOList)){
				result.addAll(fbsBudgetDtlAbsVOList);
			}
		}
		return result;
	}
}
