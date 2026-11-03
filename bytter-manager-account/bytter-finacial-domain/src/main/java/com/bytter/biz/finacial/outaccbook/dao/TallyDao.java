package com.bytter.biz.finacial.outaccbook.dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbook;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbookDtl;
import com.bytter.biz.finacial.outaccbook.vo.TallyOutAccbookVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant.OuterBusOriType;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant.OuterStatus;
/**
* @Title: Dao
* @Description: 表外流水账主档表
* @author onlineGenerator
* @date 2017-06-30 11:04:08
* @version 1.0
*
*/
@Repository
public class TallyDao extends BaseDao<FcOutAccbook,String>{
	/**
	 * 功能介绍 : 获取列表查询sql 公用方法<br>
	 */
	private void getQueryListSql(StringBuilder sql,Map<String, Object> params){
		sql.append("select ");
		sql.append(" book.id as id,book.tally_date as tallyDate,book.order_no as orderNo,bookDTL.account as acc,bookDTL.acc_name as accName, ");
		sql.append(" bookDTL.debit_money as debitMoney,bookDTL.credit_money as creditMoney,bookDTL.summary_content as summaryContent, ");
		sql.append(" bookDTL.subject_name as subjectName,bookDTL.measure_unit as unit,book.status as status ");
		sql.append(" from ");
		sql.append(" FC_OUT_ACCBOOK book inner join FC_OUT_ACCBOOK_DTL bookDTL on book.id = bookDTL.out_accbook_id ");
		sql.append(" where 1=1 ");
		sql.append(" and book.bus_ori_type =:busOriType ");
		params.put("busOriType", OuterBusOriType.TALLY_HAND.get());
	}
	/**
	 * 功能介绍 : 方向与余额处理 公用方法
	 */
	private void directionMoney(DataTablesInput dataTablesInput,StringBuilder sql,Map<String, Object> params){
		Map<String, Object> map = dataTablesInput.getFormdata();
		String direction = (String) map.get("eqs_direction");
		String moneyCond = "";
		String startMoney = (String) map.get("eqs_startMoney");
		String endMoney = (String) map.get("eqs_endMoney");
		if(direction != null && !"".equals(direction.trim())){
			//借方
			if(FinacialConstant.BALANCE_WAY_DEBIT.equals(direction)){
				moneyCond += " and bookDTL.debit_money != 0 ";
	            if (EmptyUtils.isNotEmpty(startMoney)) {
	                moneyCond += " and bookDTL.debit_money >=:startMoney ";
	                params.put("startMoney", startMoney);
	            }
	            if (EmptyUtils.isNotEmpty(endMoney)) {
	                moneyCond += " and bookDTL.debit_money <=:endMoney ";
	                params.put("endMoney", endMoney);
	            }
			}
			//贷方
			if(FinacialConstant.BALANCE_WAY_CREDIT.equals(direction)){
				moneyCond += " and bookDTL.credit_money != 0 ";
	            if (EmptyUtils.isNotEmpty(startMoney)) {
	                moneyCond += " and bookDTL.credit_money >=:startMoney ";
	                params.put("startMoney", startMoney);
	            }
	            if (EmptyUtils.isNotEmpty(endMoney)) {
	                moneyCond += " and bookDTL.credit_money <=:endMoney ";
	                params.put("endMoney", endMoney);
	            }
			}
		}else{
			String debitCondExp = "";
            String creditCondExp = "";
            if (EmptyUtils.isNotEmpty(startMoney)) {
                debitCondExp += " bookDTL.credit_money >=:startMoney ";
                creditCondExp += " bookDTL.credit_money >=:startMoney ";
                params.put("startMoney", startMoney);
            }
            if (EmptyUtils.isNotEmpty(endMoney)) {
                if (EmptyUtils.isNotEmpty(debitCondExp)) {
                    debitCondExp += " and ";
                    creditCondExp += " and ";
                }
                debitCondExp += " bookDTL.credit_money <=:endMoney ";
                creditCondExp += " bookDTL.credit_money <=:endMoney ";
                params.put("endMoney", endMoney);
            }
            if (EmptyUtils.isNotEmpty(debitCondExp)) {
                moneyCond += "(" + debitCondExp + " and bookDTL.credit_money != 0)"; //updateby peij 冲账查询金额区间
            }
            if (EmptyUtils.isNotEmpty(creditCondExp)) {
                moneyCond += " or (" + creditCondExp + " and bookDTL.credit_money != 0)"; //updateby peij 冲账查询金额区间
            }
            if (EmptyUtils.isNotEmpty(moneyCond)) {
                moneyCond = " and (" + moneyCond + ")";
            }
		}
		sql.append(moneyCond);
	}
	/**
	 * 功能介绍 : 查询公共部分
	 */
	public DataTablesOutput queryData(StringBuilder where,Map<String, Object> whereParams,DataTablesInput dataTablesInput){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		this.getQueryListSql(sql,params);//获取查询sql
		sql.append(where);
		this.directionMoney(dataTablesInput,sql,params);//处理方向与金额
		sql.append(getWhereCondition(dataTablesInput, new TallyOutAccbookVO(), null, false));//自动生成其他查询条件
		sql.append(getWhereOrderBy(dataTablesInput, new TallyOutAccbookVO(), null));//排序
		params.putAll(whereParams);//设置where查询参数
		params.putAll(dataTablesInput.getSearchValues());//设置自动生成条件查询值
		return getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, TallyOutAccbookVO.class);
	}
	/**
	 * 查询表外记账数据-分页
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput queryOutAccbookData(DataTablesInput dataTablesInput, ICurrentUser user,List<String> corpAuth){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" and book.rel_accbook_id is null ");
		sql.append(" and book.redblue_sign =:redblueSign ");
		sql.append(" and book.status in (:status) ");
		sql.append(" and book.operator_code =:userId ");
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "acc.corp_Id"));
		//TODO 记账状态为空设置默认查询状态，网点为空查询用户默认权限网点
		params.put("redblueSign", FinacialConstant.REDBLUE_SIGN_BLUE);
		List<Integer> statues = new ArrayList<Integer>();
		statues.add(OuterStatus.STATUE_TALLY_TEMP.get());
		statues.add(OuterStatus.STATUE_TALLY_BACK.get());
		params.put("status", statues);
		params.put("userId", user.getId());
		return queryData(sql,params,dataTablesInput);
	}
	/**
	 * 记账复核-分页
	 */
	public DataTablesOutput queryTableTallyCheckData(
			DataTablesInput dataTablesInput, ICurrentUser user) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" and book.status in (:status) ");
		sql.append(" and book.OPERATOR_CODE <> :userId ");
		//TODO 网点为空查询用户默认权限网点
		List<Integer> statues = new ArrayList<Integer>();
		statues.add(OuterStatus.STATUE_TALLY_WAIT_CHECK.get());
		params.put("status", statues);
		params.put("userId", user.getId());
		return queryData(sql,params,dataTablesInput);
	}
	/**
	 * 记账作废-分页
	 */
	public DataTablesOutput queryTableTallyCancelData(DataTablesInput dataTablesInput, ICurrentUser user, List<String> corpAuth) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" and book.status in (:status) ");
		sql.append(" and book.operator_code =:userId ");
		sql.append(" and book.over_sign = :overSign ");
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "acc.corp_Id"));
		//TODO 网点为空查询用户默认权限网点
		List<Integer> statues = new ArrayList<Integer>();
		statues.add(OuterStatus.STATUE_TALLY_CHECK.get());
		params.put("status", statues);
		params.put("userId", user.getId());
		params.put("overSign", OuterFinacialConstant.OVER_SIGN_N);
		return queryData(sql,params,dataTablesInput);
	}
	/**
	 * 记账作废修改-分页
	 */
	public DataTablesOutput queryTableTallyCancelUpdateData(DataTablesInput dataTablesInput, ICurrentUser user, List<String> corpAuth) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" and book.status in (:status) ");
		sql.append(" and book.operator_code =:userId ");
		sql.append(" and book.over_sign = :overSign ");
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "acc.corp_Id"));
		//TODO 网点为空查询用户默认权限网点
		List<Integer> statues = new ArrayList<Integer>();
		statues.add(OuterStatus.STATUE_INVALID_TEMP.get());
		statues.add(OuterStatus.STATUE_INVALID_BACK.get());
		params.put("status", statues);
		params.put("userId", user.getId());
		params.put("overSign", OuterFinacialConstant.OVER_SIGN_N);
		return queryData(sql,params,dataTablesInput);
	}
	/**
	 * 记账作废复核-分页
	 */
	public DataTablesOutput queryTableTallyCancelCheckData(
			DataTablesInput dataTablesInput, ICurrentUser user) {
		Map<String, Object> params = dataTablesInput.getSearchValues();
		StringBuilder sql = new StringBuilder();
		sql.append(" and book.status in (:status) ");
		sql.append(" and book.operator_code <> :userId ");
		//TODO 网点为空查询用户默认权限网点
		List<Integer> statues = new ArrayList<Integer>();
		statues.add(OuterStatus.STATUE_INVALID_WAIT_CHECK.get());
		params.put("status", statues);
		params.put("userId", user.getId());
		return queryData(sql,params,dataTablesInput);
	}
	/**
	 * 记账冲销-分页
	 */
	public DataTablesOutput queryTableTallyChargeData(DataTablesInput dataTablesInput, ICurrentUser user, List<String> corpAuth) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" and book.status in (:status) ");
		sql.append(" and (book.rush_sign = :rushSign or book.rush_sign is null) ");
		sql.append(" and book.over_sign = :overSign ");
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "acc.corp_Id"));
		//  TODO 网点为空查询用户默认权限网点
		//TODO 已日结数据
		List<Integer> statues = new ArrayList<Integer>();
		statues.add(OuterStatus.STATUE_TALLY_CHECK.get());
		params.put("status", statues);
		params.put("rushSign", FinacialConstant.BOOLEAN_FALSE);
		params.put("overSign", OuterFinacialConstant.OVER_SIGN_Y);
		return queryData(sql,params,dataTablesInput);
	}
	/**
	 * 记账冲销修改-分页
	 */
	public DataTablesOutput queryTableTallyChargeUpdateData(DataTablesInput dataTablesInput, ICurrentUser user, List<String> corpAuth) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" and book.status in (:status) ");
		sql.append(" and book.operator_code = :userId ");
		sql.append(" and book.over_sign = :overSign ");
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "acc.corp_Id"));
		//TODO 网点为空查询用户默认权限网点
		List<Integer> statues = new ArrayList<Integer>();
		statues.add(OuterStatus.STATUE_RUSH_TEMP.get());
		statues.add(OuterStatus.STATUE_RUSH_BACK.get());
		params.put("userId", user.getId());
		params.put("status", statues);
		params.put("overSign", OuterFinacialConstant.OVER_SIGN_Y);
		return queryData(sql,params,dataTablesInput);
	}
	/**
	 * 记账冲销复核-分页
	 */
	public DataTablesOutput queryTableTallyChargeCheckData(
			DataTablesInput dataTablesInput, ICurrentUser user) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" and book.status in (:status) ");
		sql.append(" and book.operator_code <> :userId ");
		sql.append(" and book.over_sign = :overSign ");
		//TODO 网点为空查询用户默认权限网点
		List<Integer> statues = new ArrayList<Integer>();
		statues.add(OuterStatus.STATUE_RUSH_WAIT_CHECK.get());
		params.put("userId", user.getId());
		params.put("status", statues);
		params.put("overSign", OuterFinacialConstant.OVER_SIGN_Y);
		return queryData(sql,params,dataTablesInput);
	}
	/**
	 *
	 * 功能介绍 : 根绝表外ID查询明细<br>
	 * @param id
	 * @return
	 * <b>日期：</b>2017-7-6<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	public List<TallyOutAccbookVO> queryOutDtlById(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		/*sql.append("select dtl.id,dtl.account as acc,acc.acc_name as accName,dtl.debit_money as debitMoney,dtl.credit_money as creditMoney, ");
		sql.append(" dtl.summary_content as summaryContent,sub.subject_name as subjectName,sub.measure_unit as unit,acc.cur_id as curId ");
		sql.append(" from  FC_OUT_ACCBOOK_DTL dtl ");
		sql.append(" left join FC_ACC acc on acc.blnctr_acc = dtl.account ");
		sql.append(" left join FC_SUBJECT sub on sub.id = acc.subject_id ");
		sql.append(" where 1=1 ");
		sql.append(" and dtl.out_accbook_id =:outAccbookId ");
		sql.append(" order by line_no ");
		params.put("outAccbookId", id);*/
		String sql = "select dtl.id,dtl.account as acc,dtl.debit_money as debitMoney,dtl.credit_money as creditMoney, " +
				" dtl.summary_content as summaryContent, " +
				" from  FC_OUT_ACCBOOK_DTL dtl " +
				" where 1=1 " +
				" and dtl.out_accbook_id =:outAccbookId " +
				" order by line_no ";
		params.put("outAccbookId", id);
		return listBySQLAliasToBean(sql, params, TallyOutAccbookVO.class);
	}
	/**
	 * 功能介绍 : 根据Id查询明细数据<br>
	 * @param id
	 * @return
	 * <b>日期：</b>2017-7-6<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	public FcOutAccbookDtl queryDtlById(String id){
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select * from FC_OUT_ACCBOOK_DTL where id=:id";
		params.put("id", id);
		return getBySQLAliasToBean(sql, params, FcOutAccbookDtl.class);
	}
	/**
	 * 功能介绍 : 根据outAccId查询明细数据<br>
	 * @param id
	 * @return
	 * <b>日期：</b>2017-7-6<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	public List<FcOutAccbookDtl> queryDtlByOutId(String id){
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select * from FC_OUT_ACCBOOK_DTL where out_accbook_id=:outAccbookId";
		params.put("outAccbookId", id);
		return listBySQLAliasToBean(sql, params, FcOutAccbookDtl.class);
	}
	/**
	 * 功能介绍 : 根据OutId删除明细数据<br>
	 * @param id
	 * @return
	 * <b>日期：</b>2017-7-6<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	public void delDtlByOutId(String id){
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "delete from FC_OUT_ACCBOOK_DTL where OUT_ACCBOOK_ID=:id";
		params.put("id", id);
		createSQLQuery(sql,params).executeUpdate();
	}
}
