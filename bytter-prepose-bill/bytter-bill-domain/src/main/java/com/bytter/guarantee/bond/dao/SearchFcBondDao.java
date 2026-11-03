package com.bytter.guarantee.bond.dao;


import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.bond.entity.FcBondApply;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.vo.*;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author fusb
 *	@version 1.0
 *  @Title FcBondBackDao 
 *  @Description  保证金转回dao
 *  @date 2018年9月17日
 */
@Repository
public class SearchFcBondDao extends BaseDao<FcBondApply, String>{
	
	/**
	 * 查询保证项目信息
	 * @param dataTablesInput
	 * @param joinSql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<FcBondApplyVO> getDataTablesUnion(DataTablesInput dataTablesInput,String joinSql){
		StringBuilder sql = new StringBuilder();		
		sql.append("SELECT A.START_DATE AS bondDate, A.Cur_Id AS curId,C.ID as id,A.bank_acc_id AS bankAccId,C.BILL_NO AS billNo,C.APPLY_TYPE AS applyType,C.ITEM_TYPE AS itemType,");
		sql.append("C.ITEM_NO AS itemNo,C.BANK_ID AS bankId,C.ITEM_MONEY AS itemMoney,C.BOND_PERCENT AS bondPercent,");
		sql.append("C.START_DATE AS startDate,C.END_DATE as endDate,C.ITEM_UNIT AS itemUnit,C.BOND_ID AS bondId,C.REMARK as remark,");
		sql.append("C.BOND_CONTRACT AS bondContract,C.APPLY_MONEY AS applyMoney,C.STATUS AS status,C.CREATE_BY AS createBy,");
		sql.append("C.CREATE_DATE AS createDate,C.UPDATE_BY AS updateBy,C.UPDATE_DATE AS updateDate,");
		sql.append("(D.BOND_MONEY+D.ADD_MONEY-D.BACK_MONEY) AS balMoney  ");
		sql.append(" FROM FC_BOND_APPLY C LEFT JOIN FC_BOND_BAL D  ON  C.BOND_ID=D.BOND_ID  LEFT  JOIN FC_BOND A  ON C.BOND_ID=A.ID ");
		sql.append(" left join BT_BANK_ACC co on A.bank_acc_id   = co.id ");
		sql.append("where C.STATUS='95'");
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput,sql.toString(), FcBondApplyVO.class,"",false);
	}
	
	 /**
	   * 根据保证金id,得到保证金账户信息
	   * @param id
	   * @return
	   */
	  public FcBondBackVO getByIdFcBondBackVO(String id){
		  StringBuilder sql = new StringBuilder();
		  sql.append("SELECT d.bondId,d.corpId,d.curId,d.bankAccId,(CASE WHEN d.backMoney IS NULL THEN 0 ELSE d.backMoney END) backMoney, ");
		  sql.append("(CASE WHEN d.backInterest IS NULL THEN 0 ELSE d.backInterest END) backInterest,d.balMoney,d.applyMoney,d.availMoney,d.status ,");
		  sql.append(" d.isRegular, d.bondTypeName,d.endDate,d.dayLimit,d.interestRate, d.outBankAcc,d.isPayed,d.purpose,d.remark ");
		  sql.append("FROM (SELECT B.ID AS bondId,B.STATUS AS status,B.CORP_ID AS corpId,B.CUR_ID AS curId,B.bank_acc_id AS bankAccId,A.backMoney,A.backInterest,");
		  sql.append("(C.BOND_MONEY+C.ADD_MONEY-C.BACK_MONEY) AS balMoney,(C.APPLY_MONEY+C.FREEZE_MONEY) AS applyMoney,");
		  sql.append("(C.BOND_MONEY+C.ADD_MONEY-C.BACK_MONEY-C.APPLY_MONEY-C.FREEZE_MONEY) AS availMoney, b.out_bank_acc as outBankAcc,b.is_payed as isPayed,");
		  sql.append(" b.is_regular as isRegular,t.bond_type_name as bondTypeName,b.end_date as endDate,b.day_limit as dayLimit,b.interest_rate as interestRate, ");
		  sql.append(" b.remark,b.purpose ");
		  sql.append(" FROM FC_BOND B LEFT JOIN (select BOND_ID AS bondId,SUM(BACK_MONEY) as backMoney,SUM(BACK_INTEREST) AS backInterest ");
		  sql.append(" FROM  FC_BOND_BACK GROUP BY  FC_BOND_BACK.BOND_ID) A ON B.ID=A.bondId ");
		  sql.append(" LEFT JOIN FC_BOND_BAL C on B.ID = C.BOND_ID ");
		  sql.append(" left join fc_bond_type t on b.bond_type_id = t.id ");
		  sql.append(" ) d ");
		  sql.append("  where bondId=:id");
		  Map<String,String> params = new HashMap<String,String>();
		  params.put("id", id);
		  return getBySQLAliasToBean(sql.toString(), params, FcBondBackVO.class);
	  }
	  
	  /**
	   * 根据保证金id,得到账户项目信息
	   * @param id
	   * @return
	   */
	  public  List<FcBondApplyVO> getByIdFcBondApplys(String id){
		  StringBuilder sql = new StringBuilder();				 
		  sql.append("SELECT  B.ID AS id,B.BILL_NO AS billNo,B.APPLY_TYPE AS applyType,B.ITEM_TYPE AS itemType,");
		  sql.append("B.ITEM_NO AS itemNo,B.CORP_ID AS corpId,B.BANK_ID AS bankId,B.ITEM_MONEY AS itemMoney,");
		  sql.append("B.BOND_PERCENT AS bondPercent,B.START_DATE AS startDate,B.END_DATE AS endDate,");
		  sql.append("B.ITEM_UNIT AS itemUnit,B.BOND_CONTRACT AS bondContract,B.BOND_ID AS bondId,");
		  sql.append("B.APPLY_MONEY as applyMoney,B.REMARK AS remark,B.STATUS AS status,B.CREATE_BY AS createBy,");
		  sql.append("B.CREATE_DATE AS createDate,B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updatDate  ");
		  sql.append(" FROM FC_BOND_APPLY B  WHERE  (B.STATUS="+ FcBondConstant.STATUE_APPLY+"  OR  ");
		  sql.append(" B.STATUS="+FcBondConstant.STATUE_RELEASE   );
		  sql.append(" ) AND bond_id=:id ");
		  sql.append(" order by  B.START_DATE  desc ");
		  Map<String,String> params = new HashMap<String,String>();
		  params.put("id", id);
		  return listBySQLAliasToBean(sql.toString(), params, FcBondApplyVO.class);
	  }

	public  List<FcBondApplyVO> getByIdFcBondApplys(String id,String param){
		StringBuilder sql = new StringBuilder();
		Map<String,String> params = new HashMap<String,String>();
		sql.append("SELECT  B.ID AS id,B.BILL_NO AS billNo,B.APPLY_TYPE AS applyType,B.ITEM_TYPE AS itemType,");
		sql.append("B.ITEM_NO AS itemNo,B.CORP_ID AS corpId,B.BANK_ID AS bankId,B.ITEM_MONEY AS itemMoney,");
		sql.append("B.BOND_PERCENT AS bondPercent,B.START_DATE AS startDate,B.END_DATE AS endDate,");
		sql.append("B.ITEM_UNIT AS itemUnit,B.BOND_CONTRACT AS bondContract,B.BOND_ID AS bondId,");
		sql.append("B.APPLY_MONEY as applyMoney,B.REMARK AS remark,B.STATUS AS status,B.CREATE_BY AS createBy,");
		sql.append("B.CREATE_DATE AS createDate,B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updatDate,  ");
		sql.append(" sc2.name as parentName, sc.operationArea_ID as operationAreaCode,sem.equity_name as equityName,avv.type_name as bankName, ");
		sql.append(" av.orig_no as origNo,nb.bill_no as payBillNo,nb.bursar_Bill_No as bursarBillNo,B.RELEASE_DATE as releaseDate ");
		sql.append(" FROM FC_BOND_APPLY B    ");
		sql.append(" left join ab_voucherinfo_vice avv on B.ITEM_ID = avv.ID ");
		sql.append(" left join ab_voucherinfo av on av.id = avv.vid ");
		sql.append(" left join nis_billhead nb on nb.id = avv.VOUCHER_ID ");
		sql.append(" left join sys_corp sc on sc.id = B.corp_id ");
		// Oracle 不允许在 left join 的 on 条件里把列外连接到一个子查询（ORA-01799）。
		// 这里子查询与外层表无关联且 cell_code 唯一，用 in 代替 = 即可绕过；
		// 若以后子查询需要引用外层的表，就得改成派生表或 not exists 写法。
		sql.append(" left join sys_corp sc2 on sc2.id = sc.parent_id AND sc2.UNIT_CM in (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') ");
		sql.append(" left join sys_equity_maintenance sem on sem.equity_code = sc.equityMaintenance_ID ");
		sql.append(" WHERE  (B.STATUS="+ FcBondConstant.STATUE_APPLY+"  OR B.STATUS="+FcBondConstant.STATUE_RELEASE );
		sql.append(" ) AND bond_id=:id ");
		if(EmptyUtils.isNotEmpty(param) && !"null".equals(param)){
			String status = "";
			String payBillNo = "";
			String bursarBillNo = "";
			String origNo = "";
			String startDate = "";
			String endDate = "";
			if(param.split(",").length >=1) {
				status = param.split(",")[0];
			}
			if(param.split(",").length >=2) {
				payBillNo = param.split(",")[1];
			}
			if(param.split(",").length >=3) {
				bursarBillNo = param.split(",")[2];
			}
			if(param.split(",").length >=4) {
				origNo = param.split(",")[3];
			}
			if(param.split(",").length >=5) {
				startDate = param.split(",")[4];
			}
			if(param.split(",").length >=6) {
				endDate = param.split(",")[5];
			}
			if(EmptyUtils.isNotEmpty(status.trim())) {
				sql.append(" AND B.STATUS=:status ");
				params.put("status", status);
			}
			if(EmptyUtils.isNotEmpty(payBillNo)) {
				sql.append(" AND nb.bill_no like :payBillNo ");
				params.put("payBillNo", "%" + payBillNo + "%");
			}
			if(EmptyUtils.isNotEmpty(bursarBillNo)) {
				sql.append(" AND nb.bursar_Bill_No like :bursarBillNo ");
				params.put("bursarBillNo", "%" + bursarBillNo + "%");
			}
			if(EmptyUtils.isNotEmpty(origNo)) {
				sql.append(" AND av.orig_no like :origNo ");
				params.put("origNo", "%" + origNo + "%");
			}
			if(EmptyUtils.isNotEmpty(startDate)) {
				sql.append(" AND B.START_DATE >= :startDate ");
				params.put("startDate", startDate);
			}
			if(EmptyUtils.isNotEmpty(endDate)) {
				sql.append(" AND B.END_DATE <= :endDate ");
				params.put("endDate", endDate);
			}
		}
		sql.append(" order by  B.START_DATE  desc ");
		params.put("id", id);
		return listBySQLAliasToBean(sql.toString(), params, FcBondApplyVO.class);
	}
	  
	  /**
	   * 根据保证金id,得到转回历史记录
	   * @param id
	   * @return
	   */
	  public  List<FcBondBackVO> getByIdFcBondBacks(String id){
		  StringBuilder sql = new StringBuilder();				 
		  sql.append("SELECT B.ID AS id,B.BILL_NO AS billNo,B.BOND_ID AS bondId,B.BACK_DATE AS backDate,");
		  sql.append("B.BACK_MONEY AS backMoney,B.BACK_INTEREST AS backInterest,B.STATUS AS status,");
		  sql.append("B.CREATE_BY AS createBy,B.CREATE_DATE AS createDate,B.UPDATE_BY AS updateBy,");
		  sql.append("B.UPDATE_DATE AS updateDate ");
		  sql.append(" from FC_BOND_BACK B ");
		  sql.append(" where bond_id=:id");
		  Map<String,String> params = new HashMap<String,String>();
		  params.put("id", id);
		  return listBySQLAliasToBean(sql.toString(), params, FcBondBackVO.class);
	  }
	  
	  /**
	   * 跟据表FC_BOND_APPLY的ID得到FcBondApplyVO
	   * @param id
	   * @return
	   */
	  public FcBondApplyVO getFcBondApplyVOById(String id){
		  StringBuilder sql = new StringBuilder();
		  sql.append("SELECT A.START_DATE AS bondDate, A.CUR_ID AS curId,A.BANK_ACC_ID as bankAccId,C.ID as id,C.BILL_NO AS billNo,");
		  sql.append("C.APPLY_TYPE AS applyType,C.ITEM_TYPE AS itemType,C.ITEM_NO AS itemNo,C.BANK_ID AS bankId,");
		  sql.append("C.ITEM_MONEY AS itemMoney,C.BOND_PERCENT AS bondPercent,C.START_DATE AS startDate,");
		  sql.append("C.END_DATE as endDate,C.ITEM_UNIT AS itemUnit,C.BOND_ID AS bondId,C.BOND_CONTRACT AS bondContract,");
		  sql.append("C.APPLY_MONEY AS applyMoney,C.REMARK as remark,C.STATUS AS status,C.CREATE_BY AS createBy,");
		  sql.append("C.CREATE_DATE AS createDate,C.UPDATE_BY AS updateBy,C.UPDATE_DATE AS updateDate,");
		  sql.append("(D.BOND_MONEY+D.ADD_MONEY-D.BACK_MONEY) AS balMoney,");
		  sql.append("(D.BOND_MONEY+D.ADD_MONEY-D.BACK_MONEY-D.APPLY_MONEY-D.FREEZE_MONEY) AS availMoney ");
		  sql.append(" FROM FC_BOND_APPLY C LEFT JOIN FC_BOND_BAL D  ON  C.BOND_ID=D.BOND_ID  LEFT  JOIN ");
		  sql.append(" FC_BOND A  ON C.BOND_ID=A.ID ");
		  sql.append(" where C.id=:id");
		  Map<String,String> params = new HashMap<String,String>();
		  params.put("id", id);
		  return getBySQLAliasToBean(sql.toString(), params, FcBondApplyVO.class);
	  }
	  
	  /**
		 * 保证金账户查询
		 * @param dataTablesInput
		 * @param joinSql
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		public DataTablesOutput<FcBondBackSearchVO> getDataTablesUnion1(DataTablesInput dataTablesInput,String joinSql){
			StringBuilder sql = new StringBuilder();
			try {
				String erpIds=dataTablesInput.getFormdata().get("erpIds")+"";
				String count =" 1!=1";//不会成立的条件
				if(!"null".equals(erpIds)){
					String[] ids = erpIds.split(",");
					List<String> idList = Arrays.asList(ids);
					count = getSQLIn(idList, 900, "c.bond_id");
				}
				sql.append(" select b.bondId,b.bond_type_id as bondTypeId,b.CORP_ID as corpId,b.CUR_ID as curId,B.BANK_ACC_ID AS BankAccId,b.backMoney,b.backInterest, ");
				sql.append(" (b.BOND_MONEY + b.ADD_MONEY - b.BACK_MONEY) AS balMoney, ");
				sql.append(" (b.APPLY_MONEY + b.FREEZE_MONEY) AS applyMoney, ");
				sql.append(" (b.BOND_MONEY + b.ADD_MONEY - b.BACK_MONEY - b.APPLY_MONEY - b.FREEZE_MONEY) AS availMoney ");
				sql.append(" from( ");
				sql.append(" SELECT B.ID AS bondId,B.bond_type_id,B.CORP_ID,B.CUR_ID,B.BANK_ACC_ID,A.backMoney,A.backInterest,");
				sql.append(" case when "+count+" then 0 else nvl(c.bond_money,0) end as BOND_MONEY, ");
				sql.append(" case when "+count+" then 0 else nvl(c.add_money,0) end as ADD_MONEY,");
				sql.append(" c.back_money,c.apply_money,c.freeze_money ");
				sql.append(" FROM (SELECT fc.ID, fc.BANK_ACC_ID, fc.CORP_ID, fc.CUR_ID, fc.BOND_TYPE_ID FROM FC_BOND fc ");
				sql.append(" LEFT JOIN bt_bank_acc bba on fc.BANK_ACC_ID = bba.ID WHERE fc.STATUS IN ('95') and bba.status = '95' and bba.valid_Sign = '1' ) B ");
				sql.append("  LEFT JOIN (select BOND_ID AS bondId,SUM(BACK_MONEY) as backMoney,SUM(BACK_INTEREST) AS backInterest FROM FC_BOND_BACK GROUP BY FC_BOND_BACK.BOND_ID) A ");
				sql.append("  ON B.ID = A.bondId ");
				sql.append("  LEFT JOIN FC_BOND_BAL C  on B.ID = C.BOND_ID");
//				sql.append(" left join nis_allocation_req t on c.bond_id = t.erp_id ");
				sql.append(" ) b ");
				sql.append("  where 1 = 1 ");
				if(StringUtils.isNotEmpty(joinSql)){
					sql.append(joinSql);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return getDynamicBySQL(dataTablesInput,sql.toString(),FcBondBackSearchVO.class,null,false);
		}
	  
		/**
		 * 通过保证金ID得到保证金账户追加明细
		 * @param bondId
		 * @return
		 */
		public List<FcBondAddDTO> getFcBondAddsByBondId(String bondId){
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT b.ID AS id,b.BILL_NO AS billNo,b.BOND_ID AS bondId,b.ADD_DATE AS addDate,");
			sql.append("b.IS_PAYED AS isPayed,b.ADD_MONEY AS addMoney,b.REMARK AS remark,b.STATUS AS status,");
			sql.append("b.CREATE_BY AS createBy,b.CREATE_DATE AS createDate,b.UPDATE_BY AS updateBy,b.UPDATE_DATE AS updateDate ");
			sql.append(" FROM FC_BOND_ADD B  WHERE b.STATUS='95'");
			sql.append(" AND b.BOND_ID=:bondId");
			Map<String,String> params = new HashMap<String,String>();
			params.put("bondId", bondId);
			
			return listBySQLAliasToBean(sql.toString(), params, FcBondAddDTO.class);
		}
		
		public FcBondApplyInfoVO getFcBondApplyInfoVO(String itemId, String itemNo){
			StringBuilder sql = new StringBuilder();		
			 sql.append("SELECT  B.ID AS id,C.BANK_NAME AS bankName,C.BANK_ACC AS bankAcc,C.ACC_NAME AS accName,");
			  sql.append("B.BILL_NO AS billNo,B.APPLY_TYPE AS applyType,B.ITEM_TYPE AS itemType,B.ITEM_NO AS itemNo,B.ITEM_ID AS itemId, ");
			  sql.append("B.CORP_ID AS corpId,B.BANK_ID AS bankId,B.ITEM_MONEY AS itemMoney,B.BOND_PERCENT AS bondPercent,");
			  sql.append("B.START_DATE AS startDate,B.END_DATE AS endDate,B.ITEM_UNIT AS itemUnit,B.BOND_CONTRACT AS bondContract,");
			  sql.append("B.BOND_ID AS bondId,B.APPLY_MONEY as applyMoney,B.REMARK AS remark,B.STATUS AS status,B.CREATE_BY AS createBy,");
			  sql.append("B.CREATE_DATE AS createDate,B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updatDate,A.CUR_ID AS curId,");
			  sql.append("(D.BOND_MONEY+D.ADD_MONEY-D.BACK_MONEY-D.APPLY_MONEY-D.FREEZE_MONEY) AS availMoney,");//可用余额
			  sql.append("(D.BOND_MONEY+D.ADD_MONEY-D.BACK_MONEY) AS balMoney ,C.RATES as bondRate  ");
			  sql.append(" FROM FC_BOND_APPLY B 	LEFT JOIN FC_BOND A ON B.BOND_ID=A.ID LEFT JOIN BT_BANK_ACC  C  ");
			  sql.append(" ON C.ID=A.BANK_ACC_ID LEFT JOIN FC_BOND_BAL D ON B.BOND_ID= D.BOND_ID  where  B.item_id=:itemId ");
			  sql.append("and B.item_no=:itemNo and   B.status<>2 ");
			Map<String,String> params = new HashMap<String,String>();
			params.put("itemId", itemId);  
			params.put("itemNo", itemNo);
			return getBySQLAliasToBean(sql.toString(),params,FcBondApplyInfoVO.class);
		}
	/**
	 * 查询保证金占用相关银行
	 */
	public List<FcBondVO> queryBondBank() {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct fc.bank_acc_id as bankAccId ")
				.append(" from fc_bond_apply bond, fc_bond fc")
				.append(" where bond.bond_id = fc.id and bond.status = :status ");
		sql.append(" order by fc.bank_acc_id ");
		params.put("status", FcBondConstant.STATUE_CHECK);
		return listBySQLAliasToBean(sql.toString(), params, FcBondVO.class);
	}
	/**
	 * 查询保证金占用相关单位
	 */
	public List<FcBondApplyDTO> queryBondCorp(String joinSql) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct  bond.corp_id as corpId ")
				.append(" from fc_bond_apply bond ")
				.append(" where bond.status = :status ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(" and ").append(joinSql);
        }
		sql.append(" order by bond.corp_id ");
		params.put("status", FcBondConstant.STATUE_CHECK);
		return listBySQLAliasToBean(sql.toString(), params, FcBondApplyDTO.class);
	}
	/**
	 * 查询保证金占用情况汇总
	 * @param joinSql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<FcBondUseDtlVO> getFcBondApplySum(FcBondApplyVO applyVO, String joinSql){
		Map<String,String> params=new HashMap<String,String>();
		StringBuilder sql = new StringBuilder();

		sql.append("select dtl.corpId,dtl.bankAccId, h.bondSumMoney,dtl.curId,dtl.itemType,dtl.applyMoney ")
				.append(" from (SELECT A.CORP_ID AS corpId, a.bank_acc_id AS bankAccId,a.cur_id as curId,b.item_Type as itemType,sum(b.apply_money) as applyMoney ")
				.append(" FROM FC_BOND_APPLY B")
				.append(" inner join fc_bond a ")
				.append("  on b.bond_id = a.id where B.STATUS = 95 ")
				.append(" group by b.item_Type, a.corp_id, a.bank_acc_id , a.cur_id) dtl,")
				.append(" (SELECT B.CORP_ID AS corpId,b.bank_acc_id AS bankAccId, B.CUR_ID AS curId, sum(b.balMoney) AS bondSumMoney ")
				.append("FROM (select t.id, t.bank_acc_id, t.corp_id, t.cur_id, (c.bond_money+c.add_money-c.back_money) balMoney ")
				.append(" from fc_bond t, FC_BOND_BAL C ")
				.append("where t.id = c.bond_id) B ")
				.append("where 1 = 1 ")
				.append("group by B.CORP_ID,b.bank_acc_id, B.CUR_ID ) h ")
				.append("where dtl.corpId = h.corpId ")
				.append("and dtl.bankAccId = h.bankAccId  ")
				.append("and dtl.curId = h.curId ");

		if(StringUtils.isNotEmpty(applyVO.getCorpId())){
			sql.append(" and dtl.corpId=:corpId ");
			params.put("corpId",applyVO.getCorpId());
		}
		if(StringUtils.isNotEmpty(applyVO.getItemType())){
			sql.append(" and dtl.itemType=:itemType ");
			params.put("itemType",applyVO.getItemType());
		}
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append(" and " + joinSql);
		}
		sql.append(" order by dtl.corpId ");
		return listBySQLAliasToBean(sql.toString(), params, FcBondUseDtlVO.class);
	}
	/**
	 * 查询同单位同银行同币别的保证金占用明细
	 */
	public List<FcBondUseDtlVO> queryBondUseDtl(FcBondApplyVO applyVO,String joinSql){
		Map<String,String> params=new HashMap<>();
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT a.CORP_ID     AS corpId, ")
				.append(" B.BANK_ID     AS bankId, ")
				.append(" B.ITEM_NO     AS itemNo, ")
				.append(" B.Start_Date  as startDate, ")
				.append(" b.end_date    as endDate, ")
				.append(" b.item_unit   as itemUnit, ")
				.append(" a.bank_acc_id as bankAccId, ")
				.append(" a.cur_id      as curId, ")
				.append(" b.item_Type   as itemType, ")
				.append(" (c.bond_money+c.add_money-c.back_money) bondMoney,")//加上追加额度
				.append(" b.apply_money as applyMoney ")
				.append(" FROM FC_BOND_APPLY B ")
				.append(" left join fc_bond a on b.bond_id = a.id ")
				.append(" left join FC_BOND_BAL C on a.id=c.bond_id ")
				.append(" WHERE (B.STATUS = 95)");
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append(joinSql);
		}
		if (StringUtils.isNotBlank(applyVO.getCorpId())) {
			sql.append(" and ").append(SqlUtil.getInSql(applyVO.getCorpId().split(","), "a.corp_Id", true));
		}
		if (StringUtils.isNotEmpty(applyVO.getCurId())) {
			sql.append(" and a.cur_Id = :curId ");
			params.put("curId", applyVO.getCurId());
		}
		if (StringUtils.isNotBlank(applyVO.getItemType())) {
			sql.append(" and ").append(SqlUtil.getInSql(applyVO.getItemType().split(","), "b.item_Type", false));
		}
		if (!EmptyUtils.isEmpty(applyVO.getBeginApplyMoney())) {
			sql.append(" and b.apply_money >= :beginApplyMoney ");
			params.put("beginApplyMoney", applyVO.getBeginApplyMoney().toString());
		}
		if (!EmptyUtils.isEmpty(applyVO.getEndApplyMoney())) {
			sql.append(" and b.apply_money <= :endApplyMoney ");
			params.put("endApplyMoney", applyVO.getEndApplyMoney().toString());
		}
		if (!EmptyUtils.isEmpty(applyVO.getBeginBondMoney())) {
			sql.append(" and (c.bond_money+c.add_money-c.back_money) >= :beginBondMoney ");
			params.put("beginBondMoney", applyVO.getBeginBondMoney().toString());
		}
		if (!EmptyUtils.isEmpty(applyVO.getEndBondMoney())) {
			sql.append(" and (c.bond_money+c.add_money-c.back_money) <= :endBondMoney ");
			params.put("endBondMoney", applyVO.getEndBondMoney().toString());
		}
		sql.append(" order by B.Start_Date desc ");
		return listBySQLAliasToBean(sql.toString(), params, FcBondUseDtlVO.class);
	}
	public List<FcBondUseDtlVO> getBondTransDtl(String id) {
		StringBuilder sql=new StringBuilder();
		sql.append(" select z.id, z.createBy,z.createDate,z.busSign,z.startDate,z.outBankAcc,");
		sql.append(" z.money,z.status from ( ");
		sql.append(getBond(id));
		sql.append(" union all ");
		sql.append(getBondAdd(id));
		sql.append(" union all ");
		sql.append(getBondBack(id));
		sql.append(" )z order by z.createDate");
		return listBySQLAliasToBean(sql.toString(), null, FcBondUseDtlVO.class);
	}
	public String getBond(String id){
		StringBuilder sql = new StringBuilder();
		sql.append(" select b.id, b.create_by as createBy,b.create_date as createDate,'b' as busSign,b.start_date as startDate, ");
		sql.append(" b.out_bank_acc as outBankAcc,b.bond_money as money,b.status as status");
		sql.append(" from fc_bond b ");
		sql.append(" where b.id = '"+id+"'");
		return sql.toString();
	}
	public String getBondAdd(String id){
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.id, a.create_by as createBy,a.create_date as createDate, 'a' as busSign,a.create_date as startDate,");
		sql.append(" a.out_bank_acc as outBankAcc,a.add_money as money,a.status as status");
		sql.append(" from fc_bond_add a ");
		sql.append(" inner join fc_bond b on a.bond_id = b.id ");
		sql.append(" where a.bond_id = '"+id+"'");
		return sql.toString();
	}
	public String getBondBack(String id){
		StringBuilder sql = new StringBuilder();
		sql.append(" select c.id, c.create_by as createBy, c.create_date as createDate,'c' as busSign, c.create_date as startDate,");
		sql.append("  c.out_bank_acc as outBankAcc,c.back_money as money, c.status as status");
		sql.append(" from fc_bond_back c ");
		sql.append(" inner join fc_bond b  on c.bond_id = b.id ");
		sql.append(" where c.bond_id = '"+id+"'");
		return sql.toString();
	}
}
