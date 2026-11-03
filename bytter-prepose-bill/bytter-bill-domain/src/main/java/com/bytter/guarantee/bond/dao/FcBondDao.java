package com.bytter.guarantee.bond.dao;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.bond.entity.FcBond;
import com.bytter.guarantee.vo.FcBondDetailVO;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author fusb
 *	@version 1.0
 *  @Title FcBondDao 
 *  @Description
 *  @date 2018年9月6日
 */  
@Repository
public class FcBondDao extends BaseDao<FcBond, String>{
	/**
	 * 查询保证金
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT B.ID AS id,B.BANK_ACC_ID bankAccId,B.BILL_NO AS billNo,B.CORP_ID AS corpId, t.bond_type_name as bondTypeName,");
		sql.append("B.BOND_MONEY AS bondMoney,B.cur_Id as curId,");
		sql.append("B.START_DATE AS startDate,B.CREATE_DATE AS createDate,B.IS_PAYED AS isPayed,B.STATUS AS status");
		sql.append(" FROM FC_BOND B ");
		sql.append(" left join fc_bond_type t on b.bond_type_id = t.id ");
		sql.append(" WHERE B.STATUS IN (0,-1,11)");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), FcBondDetailVO.class, "", false);
	}
	
	
	/**
	 * 需审核列表数据
	 *
	 * @param dataTablesInput
	 * @param businessSql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataTablesOutput<FcBondDetailVO> dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT B.ID AS id , B.BILL_NO AS billNo , B.CORP_ID AS corpId ,B.BANK_ACC_ID AS bankAccId,b.bank_acc AS bankAcc, B.BOND_MONEY AS bondMoney, B.cur_Id as curId,");
		sql.append("B.START_DATE AS startDate , B.CREATE_DATE AS createDate , B.IS_PAYED AS isPayed , B.STATUS AS status, ");
		sql.append("t.bond_Type_Name as bondTypeName ");
		sql.append("FROM FC_BOND B ");
		sql.append(" left join fc_bond_type t on B.bond_type_id = t.id ");
		sql.append("WHERE 1=1 ");
		sql.append(" AND ").append(businessSql);
		return getDynamicBySQL(dataTablesInput, sql.toString(), FcBondDetailVO.class, "", false);
	}
	
	  
	  /**
	   * fusb,2018-9-12添加
	   * 根据保证金id,得到保证金信息
	   * @param id
	   * @return
	   */
	  public  FcBondDetailVO getByIdUnion(String id){
		 return getBondDetail(id,null);
	  }

	  /**
	   * 根据保证金账号,得到保证金信息
	   * @param bondBankAcc
	   * @return
	   */
	  public  FcBondDetailVO getByBondBankAccUnion(String bondBankAcc){
		 return getBondDetail(null,bondBankAcc);
	  }
	  public  FcBondDetailVO getBondDetail(String id,String bondBankAcc){
		  if(StrUtil.isEmpty(id) && StrUtil.isEmpty(bondBankAcc) ){
			  return null;
		  }
		 StringBuilder sql = new StringBuilder();
		 sql.append("SELECT B.ID AS id,B.BILL_NO AS billNo,B.CORP_ID AS corpId, ");
		 sql.append("B.BOND_MONEY AS bondMoney,B.BANK_ACC_ID AS bankAccId, ");
		 sql.append("B.START_DATE AS startDate,B.CREATE_DATE AS createDate,B.IS_PAYED AS isPayed,B.CREATE_BY AS createBy,B.UPDATE_BY AS updateBy, ");
		 sql.append("B.UPDATE_DATE AS updateDate,B.STATUS AS status,B.REMARK AS remark,B.ATTACHMENT_ID AS attachmentId,B.CUR_ID AS curId, ");
		 sql.append(" (c.bond_money + c.add_money - c.back_money) as balMoney, ");
		 sql.append(" (c.bond_money + c.add_money - c.back_money - c.apply_money -c.freeze_money) as availMoney, ");
		 sql.append(" b. OUT_BANK_ACC as outBankAcc,b.BOND_TYPE_ID as bondTypeId, b.INTEREST_RATE as interestRate,");
		 sql.append(" b.END_DATE as endDate,b.IS_REGULAR as isRegular, b.DAY_LIMIT as dayLimit,  b.PURPOSE as purpose, ");
		 sql.append(" type.bond_type_name as bondTypeName ");
		 sql.append("FROM FC_BOND B ");
		 sql.append(" left join fc_bond_bal c  on b.id=c.bond_id ");
		 sql.append(" left join fc_bond_type type on b.bond_type_id = type.id ");
		 sql.append("WHERE 1=1 and B.status != -2 ");
		 Map<String,String> params = new HashMap<String,String>();
		 if(StrUtil.isNotEmpty(id)){
			 sql.append(" and B.ID=:id ");
			 params.put("id", id);
		 }
		 if(StrUtil.isNotEmpty(bondBankAcc)){
			 sql.append(" and B.BANK_ACC=:bondBankAcc ");
			 params.put("bondBankAcc", bondBankAcc);
		 }
		 List<FcBondDetailVO> list = listBySQLAliasToBean(sql.toString(), params, FcBondDetailVO.class);
		 if(CollectionUtil.isEmpty(list)){
			 return null;
		 }
		  return list.get(0);
	  }
	  
	   /**
		* 查询追加保证金列表
		*/
		@SuppressWarnings("rawtypes")
		public DataTablesOutput getBondAdditionDataTables(DataTablesInput dataTablesInput) {
			StringBuilder sql = new StringBuilder();
			sql.append("select b.id as id,b.bill_no as billNo,b.corp_id as corpId,n.bank_Name as bankName,");
			sql.append("n.bank_Acc as bankAcc,n.acc_Name as accName,b.bond_money as bondMoney,");
			sql.append("b.start_date as startDate,b.create_date as createDate,b.is_payed as isPayed,b.status as status");
			sql.append("  from FC_BOND b left join BT_BANK_ACC n  on b.bank_acc_id=n.id ");
			sql.append("  where b.status='95'");
			return getDynamicBySQL(dataTablesInput, sql.toString(), FcBondDetailVO.class, "n", true,false);
		}
	
	    /**
		 * 查询存出保证金的账户信息 
		 */
		@SuppressWarnings("rawtypes")
		public DataTablesOutput queryFcBondVO(DataTablesInput dataTablesInput){
			    StringBuilder sql=new StringBuilder();
			    sql.append("select b.id as id,b.bill_no as billNo,b.corp_id as corpId,n.bank_Name as bankName,");
			    sql.append("n.bank_Acc as bankAcc,n.acc_Name as accName,b.bond_money as bondMoney,");
			    sql.append("b.start_date as startDate,b.create_date as createDate,b.is_payed as isPayed,b.status as status");
			    sql.append(" from FC_BOND b left join BT_BANK_ACC n on b.bank_acc_id = n.id");
			    sql.append(" where b.status='95'");
				return getDynamicBySQL(dataTablesInput, sql.toString(), FcBondDetailVO.class, "n", true,false);
	    }
		/**
		 * 查询保证金账户是否已存出
		 */
		public boolean isBondAccExist(String bankAccId){
			  StringBuilder sql=new StringBuilder();
			  sql.append("select * from fc_bond where fc_bond.bank_acc_id=:bankAccId and status in (95,11,0,-1)");
			  Map<String,String> params = new HashMap<String,String>();
		      params.put("bankAccId", bankAccId);
		      List<FcBondDetailVO> fcBondDetailVOS = listBySQLAliasToBean(sql.toString(), params, FcBondDetailVO.class);
	          if(EmptyUtils.isEmpty(fcBondDetailVOS)){
	          	return true;
			  }else{
	          	return false;
			  }
		}
	/**
	 * 查询单位是否存在保证金申请单中
	 */
	public int queryBusBillByCorpId(String corpId){
		StringBuilder sql=new StringBuilder();
		sql.append("select count(*) from fc_bond where status<>-2 and corp_id=:corpId");
		Map<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
	}
	public List<FcBondDetailVO> getBondAccBal(List<String> corps, Date searchDate){
		StringBuilder sql=new StringBuilder();
		Map<String,Object> params=new HashMap<String,Object>();
		sql.append(" SELECT bond.corp_id, bond.cur_id, sum(bond.money) as balMoney ")
				.append(" from (SELECT B.ID, ")
				.append(" B.CORP_ID, ")
				.append(" B.CUR_ID, ")
				.append(" B.START_DATE, ")
				.append(" (C.BOND_MONEY + C.ADD_MONEY - C.BACK_MONEY) AS money ")
				.append(" FROM FC_BOND B ")
				.append(" LEFT JOIN FC_BOND_BAL C ")
				.append(" on B.ID = C.BOND_ID ")
				.append(" WHERE B.STATUS = 95 ")
				.append(" and B.START_DATE<=:searchDate ");
		if(!CollectionUtils.isEmpty(corps)){
			sql.append(" and ").append(getSQLIn(corps,900,"b.CORP_ID"));
		}
		sql.append(" ) bond  ");
		sql.append(" group by bond.CORP_ID, bond.CUR_ID ");
		params.put("searchDate",searchDate);
		return listBySQLAliasToBean(sql.toString(), params, FcBondDetailVO.class);
	}
	public Boolean getBondByBondTypeId(String bondTypeId){
		StringBuilder sql = new StringBuilder();
		sql.append(" select id from fc_bond where bond_type_id = :bondTypeId ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("bondTypeId",bondTypeId);
		List<FcBondDetailVO> bonds = listBySQLAliasToBean(sql.toString(), params, FcBondDetailVO.class);
		if(!EmptyUtils.isEmptyList(bonds) && bonds.size() > 0){
			return false;
		}
		return true;
	}
	public List<FcBondDetailVO> getBondByDate(Date startDate,Date endDate){
		StringBuilder sql = new StringBuilder();
		sql.append(" select b.id,b.corp_id as corpId,n.bank_acc as bankAcc,b.end_date as endDate from fc_bond b left join bt_bank_acc n on b.bank_acc_id = n.id  ");
		sql.append(" where b.status = 95  and b.is_regular = '1' ");
		sql.append(" and b.end_date >= to_date(:startDate,'yyyy-MM-dd') and b.end_date <= to_date(:endDate,'yyyy-MM-dd') ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startDate", DateUtils.format(startDate,"yyyy-MM-dd"));
		params.put("endDate",DateUtils.format(endDate,"yyyy-MM-dd"));
		return listBySQLAliasToBean(sql.toString(), params, FcBondDetailVO.class);
	}
}
	  
	
