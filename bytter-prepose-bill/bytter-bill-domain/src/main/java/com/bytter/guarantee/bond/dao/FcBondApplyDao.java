package com.bytter.guarantee.bond.dao;


import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.bond.entity.FcBondApply;
import  com.bytter.guarantee.vo.FcBondAccVO;
import com.bytter.guarantee.vo.FcBondApplyVO;
import  com.bytter.guarantee.vo.FcBondPreserveVO;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author luxy
 * @version 1.0
 * @Title:FcBondApplyDao
 * @Description:
 * @date 2018年9月18日 上午10:17:44
 */
@Repository
public class FcBondApplyDao extends BaseDao<FcBondApply, String> {
	/**
	 * 获取保证金维护列表
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<FcBondPreserveVO> getPreserveTables(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select M.ID AS ID,M.BILL_NO AS billNo,M.CORP_ID AS corpId,M.ITEM_NO AS itemNo, M.START_DATE AS startDate,");
		sql.append(" M.END_DATE AS endDate,");
		sql.append(" M.ITEM_MONEY AS itemMoney,M.APPLY_MONEY AS applyMoney,M.CREATE_DATE AS inputDate,M.STATUS AS status,");
		sql.append(" M.BOND_ID AS bondId, P.Cur_Id as curId,P.BANK_ACC_ID AS bankAccId,(Q.BOND_MONEY+Q.ADD_MONEY-Q.BACK_MONEY) AS accBalance ");
		sql.append(" from FC_BOND_APPLY M,FC_BOND P ,FC_BOND_BAL Q WHERE M.BOND_ID=P.ID AND P.ID=Q.BOND_ID ");//只取占用类型为手工占用的数据
		sql.append(" AND M.APPLY_TYPE=").append(FcBondConstant.APPLY_TYPE_HANDWORK  );//手工占用
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
		return getDynamicBySQL(dataTablesInput, sql.toString(), FcBondPreserveVO.class, "n", false);
	}
	/**
	 * 获取保证金项目审批列表
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getItemApproveDataTables(DataTablesInput dataTablesInput) {
		StringBuilder sql = new StringBuilder();
		sql.append("select M.ID AS ID,M.CORP_ID AS corpId,M.ITEM_NO AS itemNo, M.START_DATE AS startDate,");
		sql.append(" M.END_DATE AS endDate,P.BANK_ACC_ID AS bankAccId,");
		sql.append(" M.ITEM_MONEY AS itemMoney,M.APPLY_MONEY AS applyMoney,M.CREATE_DATE AS inputDate,M.STATUS AS status,");
		sql.append(" M.BOND_ID AS bondId, P.CUR_ID AS curId,(Q.BOND_MONEY+Q.ADD_MONEY-Q.BACK_MONEY) AS accBalance ");
		sql.append(" from FC_BOND_APPLY M,FC_BOND P ,FC_BOND_BAL Q WHERE M.BOND_ID=P.ID AND P.ID=Q.BOND_ID");
		String flowStatus = dataTablesInput.getParameter().get("flowStatus");
		//已审批不过滤状态
		if(!IWorkFlowService.PASS.equals(flowStatus)) {
			sql.append(" AND M.STATUS='11'");
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), FcBondPreserveVO.class, "n", false);
	}
	/**
	 * 根据id获取保证金项目相关信息
	 */
	public FcBondPreserveVO getFcBondItemByID(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select M.ID AS id,M.apply_type as applyType,M.Item_Type as itemType,M.BILL_NO AS billNo,M.CORP_ID AS corpId,M.ITEM_NO AS itemNo,M.START_DATE AS startDate,");
		sql.append(" M.BANK_ID AS bankId,M.END_DATE AS endDate,M.ITEM_MONEY AS itemMoney,M.APPLY_MONEY AS applyMoney,");
		sql.append(" M.ITEM_UNIT AS itemUnit,M.CREATE_DATE AS inputDate,M.STATUS AS status,");
		sql.append(" M.CREATE_BY AS createBy,M.CREATE_DATE AS createDate,M.UPDATE_BY AS updateBy,M.UPDATE_DATE AS updateDate,");
		sql.append(" M.REMARK AS remark,M.BOND_CONTRACT AS bondContract,M.BOND_PERCENT AS bondPercent,");
		sql.append(" P.ID AS bondId,P.BANK_ACC_ID as bankAccId,P.BANK_ACC as bankAcc,P.ACC_NAME as accName,P.BANK_NAME as bankName,P.START_DATE AS bondDate, P.Cur_Id as curId,");
		sql.append(" (Q.BOND_MONEY+Q.ADD_MONEY-Q.BACK_MONEY) AS accBalance,");
		sql.append(" (Q.BOND_MONEY+Q.ADD_MONEY-Q.BACK_MONEY-Q.apply_money-Q.freeze_money) as availMoney ");
		sql.append(" from FC_BOND_APPLY M,FC_BOND P,FC_BOND_BAL Q ");
		sql.append(" WHERE M.BOND_ID = P.ID AND Q.BOND_ID=P.ID AND M.ID=:id");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, FcBondPreserveVO.class);
	}
	/**
	 * 根据网点和银行查询存出保证金的账户列表
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<FcBondAccVO> queryFcBondVOByTerms(DataTablesInput dataTablesInput, String joinSql) {
		Map<String, Object> searchDate = dataTablesInput.getSearchValues();
		Map<String,Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		//TODO 名称调整
		sql.append("select a.id as bondId,a.bank_acc as bankAcc,a.corp_id as corpId,a.acc_name as accName,a.bank_code as bankCode,");
		sql.append(" a.bank_name as bankName,a.bond_money as bondMoney, a.cur_id as curId,(c.apply_money+c.freeze_money) as applyMoney,");
		sql.append(" (c.bond_money+c.add_money-c.back_money) as accBalance,");
		sql.append(" (c.bond_money+c.add_money-c.back_money-c.apply_money-c.freeze_money) as availMoney,");
		sql.append(" a.start_date as bondDate ,a.interest_rate as bondRate,t.bond_type_name as bondTypeName, A.is_Regular AS bailType ");
//		sql.append(" from fc_bond a left join fc_bond_type t on a.bond_type_id = t.id ,bt_bank_acc b,fc_bond_bal c ");
//		sql.append(" where a.bank_acc_id=b.id and a.id=c.bond_id ");
		sql.append(" from fc_bond a left join fc_bond_type t on a.bond_type_id = t.id  left join fc_bond_bal c on a.id = c.bond_id ");
		sql.append(" inner join BT_INPUT_BANK_INFO ibi on ibi.SYS_BANK_CODE = a.BANK_CODE ");
		sql.append(" where 1=1 ");
		joinDateSql(dataTablesInput, params, sql,false);
		sql.append("  and a.is_regular = 0 ");
		sql.append("  and a.status = 95 ");
//        sql.append("  and not exists ");
//        sql.append("  (select 1 ");
//        sql.append("  from nis_allocation_req n ");
////        sql.append("  left join bis_exc e ");
////        sql.append("  on n.id = e.cbs_bill_id ");
//        sql.append("  where a.id = n.erp_id ");
//        sql.append("  and n.status <> -2 ");
////        sql.append("  and ((n.status in (95, 100) and e.voucher_stat <> '0') or ");
//		sql.append("  and ((n.status in (95, 100) and n.voucher_stat <> '0') or ");
//        sql.append("   (n.status not in (95, 100))) ");
//        sql.append("  and n.bill_from = '3') ");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		if(StringUtils.isNotEmpty(searchDate.get("likebankAcc"))){
			sql.append(" and a.bank_acc like '"+searchDate.get("likebankAcc")+"' ");
		}
		if(StringUtils.isNotEmpty(searchDate.get("likeaccName"))){
			sql.append(" and a.acc_name like '"+searchDate.get("likeaccName")+"' ");
		}
		sql.append(" union ");
		sql.append("select a.id as bondId,a.bank_acc as bankAcc,a.corp_id as corpId,a.acc_name as accName,a.bank_code as bankCode,");
		sql.append(" a.bank_name as bankName,a.bond_money as bondMoney, a.cur_id as curId,(c.apply_money+c.freeze_money) as applyMoney,");
		sql.append(" (c.bond_money+c.add_money-c.back_money) as accBalance,");
		sql.append(" (c.bond_money+c.add_money-c.back_money-c.apply_money-c.freeze_money) as availMoney,");
		sql.append(" a.start_date as bondDate ,a.interest_rate as bondRate,t.bond_type_name as bondTypeName, A.is_Regular AS bailType ");
//		sql.append(" from fc_bond a left join fc_bond_type t on a.bond_type_id = t.id ,bt_bank_acc b,fc_bond_bal c ");
//		sql.append(" where a.bank_acc_id=b.id and a.id=c.bond_id ");
		sql.append(" from fc_bond a left join fc_bond_type t on a.bond_type_id = t.id  left join fc_bond_bal c on a.id = c.bond_id ");
		sql.append(" inner join BT_INPUT_BANK_INFO ibi on ibi.SYS_BANK_CODE = a.BANK_CODE ");
		sql.append(" where 1=1 ");
		joinDateSql(dataTablesInput, params, sql,true);
		sql.append("  and a.is_regular = 1 ");
		sql.append("  and a.status = 95 ");
//        sql.append("  and not exists ");
//        sql.append("  (select 1 ");
//        sql.append("  from nis_allocation_req n ");
////        sql.append("  left join bis_exc e ");
////        sql.append("  on n.id = e.cbs_bill_id ");
//        sql.append("  where a.id = n.erp_id ");
//        sql.append("  and n.status <> -2 ");
////        sql.append("  and ((n.status in (95, 100) and e.voucher_stat <> '0') or ");
//		sql.append("  and ((n.status in (95, 100) and n.voucher_stat <> '0') or ");
//        sql.append("   (n.status not in (95, 100))) ");
//        sql.append("  and n.bill_from = '3') ");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), FcBondAccVO.class, "n", false ,params);
	}

	private static void joinDateSql(DataTablesInput dataTablesInput, Map<String, Object> params, StringBuilder sql,boolean isRegular) {
		if(dataTablesInput.getFormdata() != null && dataTablesInput.getFormdata().get("led_startDate") != null && StrUtil.isNotBlank(dataTablesInput.getFormdata().get("led_startDate").toString())){
			sql.append(" and a.start_date<=:led_startDate ");
			params.put("led_startDate", parseDateParam(dataTablesInput.getFormdata().get("led_startDate").toString()));
		}
		if(dataTablesInput.getFormdata() != null && dataTablesInput.getFormdata().get("ged_startDate") != null && StrUtil.isNotBlank(dataTablesInput.getFormdata().get("ged_startDate").toString())){
			sql.append(" and a.start_date>=:ged_startDate ");
			params.put("ged_startDate", parseDateParam(dataTablesInput.getFormdata().get("ged_startDate").toString()));
		}
		//如果是定期才会有到期日期
		if(isRegular){
			if(dataTablesInput.getFormdata() != null && dataTablesInput.getFormdata().get("ged_endDate") != null && StrUtil.isNotBlank(dataTablesInput.getFormdata().get("ged_endDate").toString())){
				sql.append(" and a.end_date>=:ged_endDate ");
				params.put("ged_endDate", parseDateParam(dataTablesInput.getFormdata().get("ged_endDate").toString()));
			}
			if(dataTablesInput.getFormdata() != null && dataTablesInput.getFormdata().get("led_endDate") != null && StrUtil.isNotBlank(dataTablesInput.getFormdata().get("led_endDate").toString())){
				sql.append(" and a.end_date<=:led_endDate ");
				params.put("led_endDate", parseDateParam(dataTablesInput.getFormdata().get("led_endDate").toString()));
			}
		}
	}

	/**
	 * 将前端传入的日期字符串解析为 java.util.Date，避免以字符串字面量绑定到 Oracle DATE 列时
	 * 因 NLS_DATE_FORMAT 不匹配而抛出 ORA-01861: literal does not match format string
	 */
	private static Date parseDateParam(String value) {
		if (StrUtil.isBlank(value)) {
			return null;
		}
		// 兼容 yyyy-MM-dd 与 yyyy-MM-dd HH:mm:ss 两种常见格式
		Date date = DateUtils.str2Date(value, DateUtils.date_sdf());
		if (date == null) {
			date = DateUtils.str2Date(value, DateUtils.datetimeFormat());
		}
		return date;
	}

	/**
	 * 根据项目单号得到保证金占用表中相应的占用信息
	 */
	public FcBondApplyDTO getFcBondApplyByItemId(String itemId){
		StringBuilder sql = new StringBuilder();
		  sql.append("SELECT  B.ID AS id,A.BANK_ACC_ID AS bankAccId,");
		  sql.append("B.BILL_NO AS billNo,B.APPLY_TYPE AS applyType,B.ITEM_TYPE AS itemType,B.ITEM_NO AS itemNo,B.ITEM_ID AS itemId, ");
		  sql.append("B.CORP_ID AS corpId,B.BANK_ID AS bankId,B.ITEM_MONEY AS itemMoney,B.BOND_PERCENT AS bondPercent,");
		  sql.append("B.START_DATE AS startDate,B.END_DATE AS endDate,B.ITEM_UNIT AS itemUnit,B.BOND_CONTRACT AS bondContract,");
		  sql.append("B.BOND_ID AS bondId,B.APPLY_MONEY as applyMoney,B.REMARK AS remark,B.STATUS AS status,B.CREATE_BY AS createBy,");
		  sql.append("B.CREATE_DATE AS createDate,B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updatDate,  ");
		  sql.append("(D.BOND_MONEY+D.ADD_MONEY-D.BACK_MONEY-D.APPLY_MONEY-D.FREEZE_MONEY) AS availMoney");//可用余额
		  sql.append(" FROM FC_BOND_APPLY B LEFT JOIN FC_BOND A ON B.BOND_ID=A.ID ");
		  sql.append(" LEFT JOIN FC_BOND_BAL D ON B.BOND_ID= D.BOND_ID  where  B.item_id=:itemId ");
		  sql.append(" AND B.STATUS IN ('0','11','95','-1')");
		  Map<String,String> params = new HashMap<String,String>();
		  params.put("itemId", itemId);
		  FcBondApplyDTO fc= getBySQLAliasToBean(sql.toString(), params, FcBondApplyDTO.class);
		  return fc;
	}

	public List<FcBondApplyDTO>  getFcBondApplyListByItemId(String itemId){
		StringBuilder sql = new StringBuilder();
		//TODO 保证金调整
//		sql.append("SELECT  B.ID AS id,C.BANK_NAME AS bankName,C.BANK_ACC AS bankAcc,C.ACC_NAME AS accName,");
		sql.append("SELECT  B.ID AS id, A.BANK_ACC_ID AS bankAccId,");
		sql.append("B.BILL_NO AS billNo,B.APPLY_TYPE AS applyType,B.ITEM_TYPE AS itemType,B.ITEM_NO AS itemNo,B.ITEM_ID AS itemId, ");
		sql.append("B.CORP_ID AS corpId,B.BANK_ID AS bankId,B.ITEM_MONEY AS itemMoney,B.BOND_PERCENT AS bondPercent,");
		sql.append("B.START_DATE AS startDate,B.END_DATE AS endDate,B.ITEM_UNIT AS itemUnit,B.BOND_CONTRACT AS bondContract,");
		sql.append("B.BOND_ID AS bondId,B.APPLY_MONEY as applyMoney,B.REMARK AS remark,B.STATUS AS status,B.CREATE_BY AS createBy,");
		sql.append("B.CREATE_DATE AS createDate,B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updatDate,  ");
		sql.append("(D.BOND_MONEY+D.ADD_MONEY-D.BACK_MONEY-D.APPLY_MONEY-D.FREEZE_MONEY) AS availMoney");//可用余额
//		sql.append(" FROM FC_BOND_APPLY B 	LEFT JOIN FC_BOND A ON B.BOND_ID=A.ID LEFT JOIN BT_BANK_ACC  C  ");
//		sql.append(" ON C.ID=A.BANK_ACC_ID LEFT JOIN FC_BOND_BAL D ON B.BOND_ID= D.BOND_ID  where  B.item_id=:itemId ");
		sql.append(" FROM FC_BOND_APPLY B 	LEFT JOIN FC_BOND A ON B.BOND_ID=A.ID  ");
		sql.append(" LEFT JOIN FC_BOND_BAL D ON B.BOND_ID= D.BOND_ID  where  B.item_id=:itemId ");
		sql.append(" AND B.STATUS="+FcBondConstant.STATUE_APPLY  );
		Map<String,String> params = new HashMap<String,String>();
		params.put("itemId", itemId);
		return listBySQLAliasToBean(sql.toString(), params, FcBondApplyDTO.class);
	}

	public FcBondApplyDTO getFcBondApplyByItemIds(String itemId, String itemNo){
		StringBuilder sql = new StringBuilder();
		//TODO 保证金调整
//		  sql.append("SELECT  B.ID AS id,C.BANK_NAME AS bankName,C.BANK_ACC AS bankAcc,C.ACC_NAME AS accName,");
		//临时存bank_id
		  sql.append("SELECT  B.ID AS id, A.BANK_ACC_ID AS bankAccId,");
		  sql.append("B.BILL_NO AS billNo,B.APPLY_TYPE AS applyType,B.ITEM_TYPE AS itemType,B.ITEM_NO AS itemNo,B.ITEM_ID AS itemId, ");
		  sql.append("B.CORP_ID AS corpId,B.BANK_ID AS bankId,B.ITEM_MONEY AS itemMoney,B.BOND_PERCENT AS bondPercent,");
		  sql.append("B.START_DATE AS startDate,B.END_DATE AS endDate,B.ITEM_UNIT AS itemUnit,B.BOND_CONTRACT AS bondContract,");
		  sql.append("B.BOND_ID AS bondId,B.APPLY_MONEY as applyMoney,B.REMARK AS remark,B.STATUS AS status,B.CREATE_BY AS createBy,");
//		  sql.append("B.CREATE_DATE AS createDate,B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updatDate, C.RATES as bondRate, ");
		  sql.append("B.CREATE_DATE AS createDate,B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updatDate, ");
		  sql.append("(D.BOND_MONEY+D.ADD_MONEY-D.BACK_MONEY-D.APPLY_MONEY-D.FREEZE_MONEY) AS availMoney, A.CUR_ID as curId, A.IS_REGULAR as bailType ");//可用余额
//		  sql.append(" FROM FC_BOND_APPLY B 	LEFT JOIN FC_BOND A ON B.BOND_ID=A.ID LEFT JOIN BT_BANK_ACC  C  ");
//		  sql.append(" ON C.ID=A.BANK_ACC_ID LEFT JOIN FC_BOND_BAL D ON B.BOND_ID= D.BOND_ID  where  B.item_id=:itemId ");
		  sql.append(" FROM FC_BOND_APPLY B 	LEFT JOIN FC_BOND A ON B.BOND_ID=A.ID  ");
		  sql.append(" LEFT JOIN FC_BOND_BAL D ON B.BOND_ID= D.BOND_ID  where  B.item_id=:itemId ");
		  sql.append("and B.item_no=:itemNo ");
		  sql.append(" AND B.STATUS="+FcBondConstant.STATUE_APPLY  );
		  Map<String,String> params = new HashMap<String,String>();
		  params.put("itemId", itemId);
		  params.put("itemNo", itemNo);
		  FcBondApplyDTO fc= getBySQLAliasToBean(sql.toString(), params, FcBondApplyDTO.class);
		  return fc;
	}

	/**
	 * 该笔业务占用保证金总额
	 * @param itemId
	 * @return
	 */
	public BigDecimal getFcBondApplyMoneyByItemId(String itemId){
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(B.APPLY_MONEY) as applyMoney ");
//		sql.append(" FROM FC_BOND_APPLY B 	LEFT JOIN FC_BOND A ON B.BOND_ID=A.ID LEFT JOIN BT_BANK_ACC  C  ");
//		sql.append(" ON C.ID=A.BANK_ACC_ID LEFT JOIN FC_BOND_BAL D ON B.BOND_ID= D.BOND_ID  where  B.item_id=:itemId ");
		sql.append(" FROM FC_BOND_APPLY B 	LEFT JOIN FC_BOND A ON B.BOND_ID=A.ID ");
		sql.append(" LEFT JOIN FC_BOND_BAL D ON B.BOND_ID= D.BOND_ID  where  B.item_id=:itemId ");
		sql.append(" AND B.STATUS ='95' ");
		Map<String,String> params = new HashMap<String,String>();
		params.put("itemId", itemId);
		Map map=getBySQL(sql.toString(), params);
		if(!map.isEmpty()){
			return (BigDecimal)map.get("APPLYMONEY");
		}
		return new BigDecimal("0");
	}
	public FcBondApplyDTO getFcBondApply(String itemId,String itemNo,String bondId){
		StringBuilder sql = new StringBuilder();
		//TODO
//		sql.append("SELECT  B.ID AS id,C.BANK_NAME AS bankName,C.BANK_ACC AS bankAcc,C.ACC_NAME AS accName,");
		sql.append("SELECT  B.ID AS id, A.BANK_ACC_ID AS bankAccId,");
		sql.append("B.BILL_NO AS billNo,B.APPLY_TYPE AS applyType,B.ITEM_TYPE AS itemType,B.ITEM_NO AS itemNo,B.ITEM_ID AS itemId, ");
		sql.append("B.CORP_ID AS corpId,B.BANK_ID AS bankId,B.ITEM_MONEY AS itemMoney,B.BOND_PERCENT AS bondPercent,");
		sql.append("B.START_DATE AS startDate,B.END_DATE AS endDate,B.ITEM_UNIT AS itemUnit,B.BOND_CONTRACT AS bondContract,");
		sql.append("B.BOND_ID AS bondId,B.APPLY_MONEY as applyMoney,B.REMARK AS remark,B.STATUS AS status,B.CREATE_BY AS createBy,");
		sql.append("B.CREATE_DATE AS createDate,B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updatDate,  ");
		sql.append("(D.BOND_MONEY+D.ADD_MONEY-D.BACK_MONEY-D.APPLY_MONEY-D.FREEZE_MONEY) AS availMoney, A.CUR_ID as curId ");//可用余额
//		sql.append(" FROM FC_BOND_APPLY B 	LEFT JOIN FC_BOND A ON B.BOND_ID=A.ID LEFT JOIN BT_BANK_ACC  C  ");
//		sql.append(" ON C.ID=A.BANK_ACC_ID LEFT JOIN FC_BOND_BAL D ON B.BOND_ID= D.BOND_ID  where  B.item_id=:itemId ");
		sql.append(" FROM FC_BOND_APPLY B 	LEFT JOIN FC_BOND A ON B.BOND_ID=A.ID ");
		sql.append(" LEFT JOIN FC_BOND_BAL D ON B.BOND_ID= D.BOND_ID  where  B.item_id=:itemId ");
		sql.append("and B.item_no=:itemNo and A.id=:bondId ");
		//sql.append(" AND B.STATUS="+FcBondConstant.STATUE_APPLY  );
		Map<String,String> params = new HashMap<String,String>();
		params.put("itemId", itemId);
		params.put("itemNo", itemNo);
		params.put("bondId", bondId);
		FcBondApplyDTO fc= getBySQLAliasToBean(sql.toString(), params, FcBondApplyDTO.class);
		return fc;
	}
	public FcBondApplyDTO getFcBondApplyByparams(FcBondApplyDTO fcBondApplyDTO){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  B.ID AS id,A.BANK_ACC_ID AS bankAccId,");
		sql.append("B.BILL_NO AS billNo,B.APPLY_TYPE AS applyType,B.ITEM_TYPE AS itemType,B.ITEM_NO AS itemNo,B.ITEM_ID AS itemId, ");
		sql.append("B.CORP_ID AS corpId,B.BANK_ID AS bankId,B.ITEM_MONEY AS itemMoney,B.BOND_PERCENT AS bondPercent,");
		sql.append("B.START_DATE AS startDate,B.END_DATE AS endDate,B.ITEM_UNIT AS itemUnit,B.BOND_CONTRACT AS bondContract,");
		sql.append("B.BOND_ID AS bondId,B.APPLY_MONEY as applyMoney,B.REMARK AS remark,B.STATUS AS status,B.CREATE_BY AS createBy,");
		sql.append("B.CREATE_DATE AS createDate,B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updatDate, ");
		sql.append("(D.BOND_MONEY+D.ADD_MONEY-D.BACK_MONEY-D.APPLY_MONEY-D.FREEZE_MONEY) AS availMoney, A.CUR_ID as curId ");//可用余额
		sql.append(" FROM FC_BOND_APPLY B 	LEFT JOIN FC_BOND A ON B.BOND_ID=A.ID ");
		sql.append(" LEFT JOIN FC_BOND_BAL D ON B.BOND_ID= D.BOND_ID  where  B.item_id=:itemId ");
		sql.append("and B.item_no=:itemNo ");
		Map<String,String> params = new HashMap<String,String>();
		params.put("itemId", fcBondApplyDTO.getItemId());
		params.put("itemNo", fcBondApplyDTO.getItemNo());
		FcBondApplyDTO fc= getBySQLAliasToBean(sql.toString(), params, FcBondApplyDTO.class);
		return fc;
	}
	public List<String> getItemIds() {
		return createSQLQuery("select item_id from fc_bond_apply where (status > 2 or status < 2) and apply_Type = 0 ").list();
	}

	/**
	 * 定时任务释放保证金自动转回
	 * @return
	 */
	public List<FcBondApplyVO> getFcBondApplyListForAutoFcBondRotationJob() {
		StringBuilder sql = new StringBuilder();
		Map<String,String> params = new HashMap<String,String>();
		sql.append(" select fba.id as id , fba.BOND_ID as bondId,fba.apply_money as applyMoney ");
		sql.append(" from FC_BOND_APPLY fba ");
		sql.append(" left join fc_bond fb on fb.id = fba.BOND_ID ");
		sql.append(" left join bt_bank_acc acc on fb.BANK_ACC_ID = acc.id ");
		sql.append(" left join bt_bank_type bbt on bbt.id = acc.BANK_TYPE_ID ");
		sql.append(" where bbt.bank_type <> '9004' and fba.status = 2 and fba.ITEM_TYPE  = '2' ");
		sql.append(" and fba.RELEASE_DATE <= :todate and (fba.auto_sign <> '1' or fba.auto_sign is null) ");
		params.put("todate", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
		return listBySQLAliasToBean(sql.toString(), params, FcBondApplyVO.class);
	}

	/**
	 * 释放保证金自动转回 更新自动转回状态
	 * @param id
	 * @param autoSign
	 * @return
	 */
	public int updateAutoSign(String id, String autoSign) {
		String sql = "update FC_BOND_APPLY set auto_sign = :autoSign where id = :id";
		Map<String, String> params = new HashMap<>();
		params.put("autoSign", autoSign);
		params.put("id", id);
		return this.createSQLQuery(sql, params).executeUpdate();
	}

	public List<FcBondApplyDTO>  getFcBondApplyByItemIdsType(List<String> itemIds,String itemType){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  B.ID AS id, B.BOND_PERCENT AS bondPercent");
		sql.append(" ,B.BILL_NO AS billNo,B.APPLY_TYPE AS applyType,B.ITEM_TYPE AS itemType,B.ITEM_NO AS itemNo,B.ITEM_ID AS itemId ");
		sql.append(", B.CORP_ID AS corpId,B.BANK_ID AS bankId,B.ITEM_MONEY AS itemMoney ");
		sql.append(", B.START_DATE AS startDate,B.END_DATE AS endDate,B.ITEM_UNIT AS itemUnit,B.BOND_CONTRACT AS bondContract ");
		sql.append(", B.BOND_ID AS bondId,B.APPLY_MONEY as applyMoney,B.REMARK AS remark,B.STATUS AS status,B.CREATE_BY AS createBy");
		sql.append(", B.CREATE_DATE AS createDate,B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updatDate  ");
		sql.append(" FROM FC_BOND_APPLY B 	");
		sql.append("  where  B.ITEM_TYPE=:itemType  and B.STATUS=:status  ");

		Map<String,Object> params = new HashMap<>();
		params.put("itemType", itemType);
		params.put("status", FcBondConstant.STATUE_APPLY);
		if(null != itemIds && itemIds.size() > 0){
			sql.append(" and ").append(SqlUtil.getInSql(itemIds, "B.ITEM_ID", true));
		}
		return listBySQLAliasToBean(sql.toString(), params, FcBondApplyDTO.class);
	}
}
