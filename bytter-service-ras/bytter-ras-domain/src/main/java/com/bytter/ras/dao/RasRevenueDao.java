package com.bytter.ras.dao;

import com.bytter.ras.entity.RasRevenueDtl;
import com.bytter.ras.vo.RasRevenueDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核营收数据dao
 * @date 2019/5/15 13:39
 */
@Repository
public class RasRevenueDao extends BaseDao<RasRevenueDtl,String> {

	/**
	 * 通用查询营收数据sql
	 * @return
	 */
	public StringBuilder getCommonSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("select nue.id,nue.serial_no as serialNo,nue.ORDER_NO as orderNo,nue.shop_id as shopId,");
		sql.append("rs.name as shopName,rs.corp_id as corpId,sc.code as corpCode,sc.name as corpName,");
		sql.append("nue.TRANS_DATE as transDate,nue.MONEY,nue.pay_type as payType,");
		sql.append("nue.CUST_NAME as custName,nue.cust_acc as custAcc,nue.rmk,nue.create_date as createDate,");
		sql.append("nue.status,nue.Audit_Id from ras_revenue_dtl nue left join ras_shop rs on nue.shop_id=rs.id ");
		sql.append("left join sys_corp sc on rs.corp_id=sc.id where 1=1");
		return sql;
	}
	/**
	 * 数据sql
	 * @return
	 */
	public StringBuilder getSql(String str){
		StringBuilder sql = new StringBuilder();
		sql.append("select "+str+"id,"+str+"serial_no,"+str+"ORDER_NO,"+str+"shop_id,");
		sql.append(str+"TRANS_DATE,"+str+"MONEY,"+str+"pay_type,");
		sql.append(str+"CUST_NAME,"+str+"cust_acc,"+str+"rmk,"+str+"create_date,"+str+"status,"+str+"Audit_Id ");
		return sql;
	}
	/**
	 * 营收数据列表加载
	 * @param dataTablesInput
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,
											   List<String> corpAuthList,List<String> shopAuthList) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> formdata = dataTablesInput.getFormdata();
		//根据是否历史数据查询数据
		sql.append("select nue.id,nue.serial_no as serialNo,nue.ORDER_NO as orderNo,nue.shop_id as shopId,");
		sql.append("nue.TRANS_DATE as transDate,nue.MONEY,nue.pay_type as payType,nue.CUST_NAME as custName,");
		sql.append("nue.cust_acc as custAcc,nue.rmk,nue.create_date as createDate,nue.status,nue.Audit_Id");
		sql.append(",rs.name as shopName,rs.corp_id as corpId,sc.code as corpCode,sc.name as corpName from ");
		if("5".equals(formdata.get("eqi_status")) && RasConstant.RAS_STATUS_NO.equals(formdata.get("isHistory"))){
			sql.append("("+this.getSql("nueDtl.")+" from ras_revenue_dtl nueDtl union all "+this.getSql("suDtl.")+" from ras_revenue_su_dtl suDtl) nue ");
		}else{
			if(RasConstant.RAS_STATUS_YES.equals(formdata.get("isHistory"))){
				sql.append("ras_revenue_his_dtl nue ");
			}else{
				sql.append("ras_revenue_dtl nue ");
			}
		}
		sql.append(" left join ras_shop rs on nue.shop_id=rs.id left join sys_corp sc on rs.corp_id=sc.id where 1=1");
		sql.append(" and nue.status not in(:status)");
		String transDate = (String)formdata.get("led_transDate");
		//手动拼接时间条件，时间戳类型
		if(StringUtils.isNotEmpty(transDate)){
			transDate +=" 23:59:59";
			formdata.put("led_transDate",transDate);
			dataTablesInput.setFormdata(formdata);
		}
		String revenueTransDate = (String)formdata.get("led_revenueTransDate");
		//手动拼接时间条件，时间戳类型
		if(StringUtils.isNotEmpty(revenueTransDate)){
			revenueTransDate +=" 23:59:59";
			formdata.put("led_revenueTransDate",revenueTransDate);
		}
		Map<String,Object> params=new HashMap<>();
		//排除掉已作废的数据
		params.put("status",new Integer[]{RasConstant.RAS_AUDIT_STATUS_RETURN_2});
		if("1".equals(formdata.get("auditManual"))){
			sql.append(" and (nue.pay_type not in (:paytypeMoneyOrOther) or (nue.pay_type in (:paytypeMoneyOrOther) and nue.IS_SUM=:isSum)) ");
			params.put("paytypeMoneyOrOther",new String[]{RasConstant.RAS_PAYTYPE_001,RasConstant.RAS_PAYTYPE_006});
			params.put("isSum",Integer.parseInt(RasConstant.RAS_STATUS_YES));
			formdata.remove("auditManual");
		}
		String shopAuthSql= SqlUtil.getInSqlByParams(shopAuthList, "nue.shop_id","shopId", params);
		sql.append(" and ").append(shopAuthSql).append(" ");
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "rs.corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		return getDynamicBySQL(dataTablesInput,sql.toString() , RasRevenueDtlVO.class, "nue", false,params);
	}

	/**
	 * 根据条件获取营收表数据
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RasRevenueDtlVO> getAllRasRevenueDtlVOInfo(Map<String,Object> params){
		StringBuilder sql=new StringBuilder("select ID,SHOP_ID as shopId,SERIAL_NO as SERIALNO, ");
		sql.append(" ORDER_NO as ORDERNO,TRANS_DATE as TRANSDATE,str_TRANS_DATE as strTRANSDATE,MONEY,PAY_TYPE PAYTYPE,IS_SUM as isSum from RAS_REVENUE_DTL d ");
		sql.append(" where 1=1 ");
		if(params.containsKey("wIdList")){
			List<String> wIdList=(List<String>)params.get("wIdList");
			if(wIdList!=null&&wIdList.size()>900){
				sql.append(" and ").append(SqlUtil.getInSqlByParams(wIdList, "d.id", params));
			}else{
				sql.append(" and d.id in(:wIdList) ");
			}
		}
		if(params.containsKey("wId")){
			sql.append(" and d.id =:wId ");
		}
		if(params.containsKey("ids")){
			sql.append(" and d.id in (:ids) ");
		}
		if(params.containsKey("aids")){
			String[] idsStr = (String[])params.get("aids");
			String ids =SqlUtil.getInSql(idsStr, "d.audit_Id", true);
			sql.append(" and ").append(ids);
		}
		if(params.containsKey("statusOld")){
			sql.append(" and d.status in (:statusOld) ");
		}
		if(params.containsKey("wUnid")){
			sql.append(" and d.un_Id=:wUnid ");
		}
		if(params.containsKey("unId")){
			sql.append(" and d.un_Id is null ");
		}
		if(params.containsKey("wShopId")){
			sql.append(" and d.shop_Id=:wShopId ");
		}
		if(params.containsKey("shopIdArray")){
			sql.append(" and d.shop_Id in(:shopIdArray) ");
		}
		if(params.containsKey("statusNew")){
			sql.append(" and d.status!=:statusNew ");
		}
		if(params.containsKey("wAuditStatus")){
			sql.append(" and d.status=:wAuditStatus ");
		}
		if(params.containsKey("startRevenueDate")){
			sql.append(" and d.trans_Date>=:startRevenueDate ");
		}
		if(params.containsKey("wPayType")){
			sql.append(" and d.pay_Type=:wPayType ");
		}
		if(params.containsKey("payTypeArray")){
			sql.append(" and d.pay_Type in (:payTypeArray) ");
		}
		if(params.containsKey("startDate")){
			sql.append(" and d.trans_Date >= :startDate ");
		}
		if(params.containsKey("endDate")){
			sql.append(" and d.trans_Date < :endDate ");
		}
		if(params.containsKey("revenueTransDateStart")){
			sql.append(" and d.trans_Date >= :revenueTransDateStart ");
		}
		if(params.containsKey("revenueTransDateEnd")){
			sql.append(" and d.trans_Date < :revenueTransDateEnd ");
		}
		if(params.containsKey("notIsSum")){
			sql.append(" and (d.is_Sum !=:notIsSum or d.isSum is null ) ");
		}
		if (params.containsKey("moneyStart")) {
			sql.append(" and d.money >= :moneyStart ");
		}
		if (params.containsKey("moneyEnd")) {
			sql.append(" and d.money <= :moneyEnd ");
		}

		if(params.containsKey("maxResult")){
			int maxResult=Integer.parseInt(String.valueOf(params.get("maxResult")));
			int first=0;
			if(params.containsKey("first")){
				first=Integer.parseInt(String.valueOf(params.get("first")));
			}
			sql.append(" order by shop_id,pay_type,TRANS_DATE,id ");
			return this.listBySQLAliasToBean(sql.toString(), first, maxResult, params, RasRevenueDtlVO.class, RasConstant.RAS_AUTOT_MAXCACHE_FETCHSIZE);
		}else{
			sql.append(" order by shop_id,pay_type,TRANS_DATE ");
			return this.listBySQLAliasToBean(sql.toString(), params, RasRevenueDtlVO.class, RasConstant.RAS_AUTOT_MAXCACHE_FETCHSIZE);
		}
//        return this.listBySQLAliasToBean(sql.toString(), first, maxResult, params, RasRevenueDtlVO.class);
//        sql.append(" order by shop_id,pay_type,TRANS_DATE ");
//    	return this.listBySQLAliasToBean(sql.toString(), params, RasRevenueDtlVO.class);
	}

	/**
	 * 根据ids批量修改营收数据明细表数据状态
	 * @param params
	 * @return
	 */
	public int batchUpdate(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update RasRevenueDtl d "
				+ " set "
				+ "d.updateDate = :updateDate ");
		if(params.containsKey("updateBy")){
			hql.append(",d.updateBy = :updateBy ");
		}
		if(params.containsKey("updateName")){
			hql.append(",d.updateName = :updateName ");
		}
		if(params.containsKey("statusNew")){
			hql.append(",d.status = :statusNew ");
		}
		if(params.containsKey("auditStatus")){
			hql.append(",d.status = :auditStatus ");
		}
		if(params.containsKey("auditId")){
			hql.append(",d.auditId = :auditId ");
		}
		if (params.containsKey("isAuditIdInter")) {
			hql.append(",d.auditId = (select i.id from RasAuditInter i where d.id=i.revenueId  ) ");
		}
		if(params.containsKey("unId")){
			hql.append(",d.unId = :unId ");
		}
		if(params.containsKey("wUnid")){
			hql.append(",d.unId = null ");
		}

		hql.append(" where 1=1 ");
		if(params.containsKey("wIdList")){
			List<String> wIdList=(List<String>)params.get("wIdList");
			if(wIdList!=null&&wIdList.size()>900){
				hql.append(" and ").append(SqlUtil.getInSqlByParams(wIdList, "d.id", params));
			}else{
				hql.append(" and d.id in(:wIdList) ");
			}
		}else if(params.containsKey("isAuditIdInter")){
//        	hql.append(" and d.id = (select i.id from RasAuditInter i where d.id=i.revenueId  ) ");
			hql.append(" and exists ( from RasAuditInter i where d.id=i.revenueId) ");
		}
		if(params.containsKey("wId")){
			hql.append(" and d.id =:wId ");
		}
		if(params.containsKey("ids")){
			hql.append(" and d.id in (:ids) ");
		}
		if(params.containsKey("aids")){
			String[] idsStr = (String[])params.get("aids");
			String ids =SqlUtil.getInSql(idsStr, "d.auditId", true);
			hql.append(" and ").append(ids);
		}
		if(params.containsKey("statusOld")){
			hql.append(" and d.status in (:statusOld) ");
		}
		if(params.containsKey("wUnid")){
			hql.append(" and d.unId=:wUnid ");
		}
		if(params.containsKey("unId")){
			hql.append(" and d.unId is null ");
		}
		if(params.containsKey("wShopId")){
			hql.append(" and d.shopId=:wShopId ");
		}
		if(params.containsKey("shopIdArray")){
			hql.append(" and d.shopId in(:shopIdArray) ");
		}
		if(params.containsKey("statusNew")){
			hql.append(" and d.status!=:statusNew ");
		}
		if(params.containsKey("wAuditStatus")){
			hql.append(" and d.status=:wAuditStatus ");
		}
		if(params.containsKey("startRevenueDate")){
			hql.append(" and d.transDate>=:startRevenueDate ");
		}
		if(params.containsKey("wPayType")){
			hql.append(" and d.payType=:wPayType ");
		}
		if(params.containsKey("payTypeArray")){
			hql.append(" and d.payType in (:payTypeArray) ");
		}
		if(params.containsKey("startDate")){
			hql.append(" and d.transDate >= :startDate ");
		}
		if(params.containsKey("endDate")){
			hql.append(" and d.transDate < :endDate ");
		}
		if(params.containsKey("revenueTransDateStart")){
			hql.append(" and d.transDate >= :revenueTransDateStart ");
		}
		if(params.containsKey("revenueTransDateEnd")){
			hql.append(" and d.transDate < :revenueTransDateEnd ");
		}
		if(params.containsKey("notIsSum")){
			hql.append(" and (d.isSum !=:notIsSum or d.isSum is null ) ");
		}
		if (params.containsKey("moneyStart")) {
			hql.append(" and d.money >= :moneyStart ");
		}
		if (params.containsKey("moneyEnd")) {
			hql.append(" and d.money <= :moneyEnd ");
		}
//        //自动任务条件处理
//        if("1".equals(params.get(RasConstant.RAS_AUTOTASK_OPKEY))){
//
//        }
		return this.batchExecute(hql.toString(), params);
	}

	/**
	 * 营收明细列表加载（报表统计）差异稽核、已稽核
	 * @param dataTablesInput
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getRevenueDifferentDataTablesUnion(DataTablesInput dataTablesInput, String id) {
		StringBuilder sql = this.getCommonSql();
		sql.append(" and nue.Audit_Id =:auditId");
		Map<String,Object> params=new HashMap<>();
		params.put("auditId",id);
		DataTablesOutput dataTablesOutput = getDynamicBySQL(dataTablesInput, sql.toString(), RasRevenueDtlVO.class, "nue", false, params);
		if(dataTablesOutput.getData().size() == 0) {
			StringBuilder sq = new StringBuilder();
			sq.append("select nue.id,nue.serial_no as serialNo,nue.ORDER_NO as orderNo,nue.shop_id as shopId,");
			sq.append("rs.name as shopName,rs.corp_id as corpId,sc.code as corpCode,sc.name as corpName,");
			sq.append("nue.TRANS_DATE as transDate,nue.MONEY,nue.pay_type as payType,");
			sq.append("nue.CUST_NAME as custName,nue.cust_acc as custAcc,nue.rmk,nue.create_date as createDate,");
			sq.append("nue.status,nue.Audit_Id from ras_revenue_his_dtl nue left join ras_shop rs on nue.shop_id=rs.id ");
			sq.append("left join sys_corp sc on rs.corp_id=sc.id where 1=1 and nue.Audit_Id =:auditId");
			return getDynamicBySQL(dataTablesInput,sq.toString() , RasRevenueDtlVO.class, "nue", false,params);
		}
		return dataTablesOutput;
	}
	/**
	 * 营收明细列表加载（报表统计）待稽核
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getRevenueNotDataTablesUnion(DataTablesInput dataTablesInput, String[] split) {
		StringBuilder sql = this.getCommonSql();
		sql.append(" and ((nue.shop_id=:shopId)or(nue.shop_id is null and :shopId is null)) and ((nue.pay_Type=:payType)or(nue.pay_Type is null and :payType is null))");
		sql.append(" and BT_TO_CHAR(nue.TRANS_DATE,'yyyy-MM-dd') =:transDate and nue.status =:status");
		Map<String,Object> params=new HashMap<>();
		params.put("shopId",split[0]);
		params.put("payType",split[1]);
		params.put("transDate",split[2]);
		params.put("status",split[3]);
		return getDynamicBySQL(dataTablesInput,sql.toString() , RasRevenueDtlVO.class, "nue", false,params);
	}

	/**
	 * 根据参数获取稽核数据
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RasRevenueDtl> getRasRevenueDtlByParams(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("from RasRevenueDtl where 1=1 ");
		if(params.containsKey("id")){
			hql.append(" and id=:id ");
		}
		if(params.containsKey("wPayType")){
			hql.append(" and payType=:wPayType ");
		}
		if(params.containsKey("wUnid")){
			hql.append(" and unId=:wUnid ");
		}
		if(params.containsKey("wShopId")){
			hql.append(" and shopId=:wShopId ");
		}
		if(params.containsKey("wAuditStatus")){
			hql.append(" and status =:wAuditStatus ");
		}
		if(params.containsKey("isSum")){
			hql.append(" and isSum =:isSum ");
		}
		if(params.containsKey("startDate")){
			hql.append(" and transDate >= :startDate ");
		}
		if(params.containsKey("endDate")){
			hql.append(" and transDate < :endDate ");
		}
		return this.listByHQL(hql.toString(), params);
	}

	/**
	 * 根据参数获取稽核数据
	 * @param params
	 * @return
	 */
	public List<RasRevenueDtl> getRasRevenueDtlSumByParams(Map<String,Object> params){
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" pay_type as paytype,shop_Id as shopId,max("+RasConstant.RAS_AUTOT_REVENUE_FIELD_TRANSDATE+") transDate, ");
		sql.append(" sum("+RasConstant.RAS_AUTOT_REVENUE_FIELD_MONEY+") as money,"+RasConstant.RAS_AUDIT_STATUS_1+" status, ");
		sql.append(" "+RasConstant.RAS_STATUS_YES+" as isSum from RAS_REVENUE_DTL where 1=1 ");
		if(params.containsKey("id")){
			sql.append(" and id=:id ");
		}
		if(params.containsKey("wPayType")){
			sql.append(" and pay_Type=:wPayType ");
		}
		if(params.containsKey("wUnid")){
			sql.append(" and un_Id=:wUnid ");
		}
		if(params.containsKey("wShopId")){
			sql.append(" and shop_Id=:wShopId ");
		}
		if(params.containsKey("wAuditStatus")){
			sql.append(" and status =:wAuditStatus ");
		}
		if(params.containsKey("notIsSum")){
			sql.append(" and (is_Sum !=:notIsSum or is_Sum is null ) ");
		}
		if(params.containsKey("startDate")){
			sql.append(" and trans_Date >= :startDate ");
		}
		if(params.containsKey("endDate")){
			sql.append(" and trans_Date < :endDate ");
		}
		sql.append(" group by pay_Type,shop_Id ");
		return this.listBySQLAliasToBean(sql.toString(), params, RasRevenueDtl.class);
	}

	/**
	 * @return
	 *
	 * @Title: tovoid
	 * @Description: TODO(营收数据作废)
	 * @param @param params    参数
	 * @return void    返回类型
	 * @throws
	 */
	public int tovoid(Map<String,Object> params){
		StringBuilder hql=new StringBuilder();
		hql.append(" update RasRevenueDtl set status=-2 where id =:ids and status=1");
		return this.batchExecute(hql.toString(), params);
	}


	/**
	 *
	 * @Title: reduction
	 * @Description: TODO(还原同天汇总数据)
	 * @param @param params
	 * @param @return    参数
	 * @return int    返回类型
	 * @throws
	 */
	public int reduction(Map<String,Object> params){
		StringBuilder hql=new StringBuilder();
		hql.append(" update RasRevenueDtl set status=1 where 1=1");
		hql.append(" and bt_to_char(trans_date,'yyyy/mm/dd')=bt_to_char(:sameDate,'yyyy/mm/dd')");
		hql.append(" and status=6");
		hql.append(" and pay_Type=:payType");
		hql.append(" and shop_Id=:shopId");
		return this.batchExecute(hql.toString(), params);
	}



	/**
	 *
	 * @Title: getIsRepeatByRasRevenueDtlVO
	 * @Description: TODO(查询是否有重复的数据返回)
	 * @param @param rasRevenueDtlVO
	 * @param @return    参数
	 * @return boolean    返回类型
	 * @throws
	 */
	public boolean getIsRepeatByRasRevenueDtlVO(RasRevenueDtlVO rasRevenueDtlVO){
		Map<String,Object> parms =new HashMap<String,Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select id");
		sql.append(" from ras_revenue_dtl rrl");
		sql.append(" where 1=1");
		//门店名称
		if(StringUtils.isBlank(rasRevenueDtlVO.getShopId())){
			sql.append(" and rrl.shop_Id=:shopId");
			parms.put("shopId", rasRevenueDtlVO.getShopId());
		}
		//交易日期
		if(null!=rasRevenueDtlVO.getTransDate()){
			sql.append(" and rrl.trans_Date=:transDate");
			parms.put("transDate", rasRevenueDtlVO.getTransDate());
		}
		//交易金额
		if(null!=rasRevenueDtlVO.getMoney()){
			sql.append(" and rrl.Money=:money");
			parms.put("money",rasRevenueDtlVO.getMoney());
		}
		//交易缴款方式
		if(null!=rasRevenueDtlVO.getPayType()){
			sql.append(" and rrl.Pay_Type=:payType");
			parms.put("payType", rasRevenueDtlVO.getPayType());
		}
		//交易流水
		if(null!=rasRevenueDtlVO.getSerialNo()){
			sql.append(" and rrl.serial_no=:serialNo");
			parms.put("serialNo",rasRevenueDtlVO.getSerialNo());
		}
		//营收订单号
		if(null!=rasRevenueDtlVO.getOrderNo()){
			sql.append(" and rrl.order_no=:orderNo");
			parms.put("orderNo", rasRevenueDtlVO.getOrderNo());
		}
		sql.append(" and rrl.status=1");
		List<RasRevenueDtlVO> list = this.listBySQLAliasToBean(sql.toString(), parms, RasRevenueDtlVO.class);
		if(null!=list && list.size()>0){
			return true;
		}
		return false;
	}
	/**
	 * 定时任务将营收稽核完成的数据迁移
	 *
	 */
	public void revenueDataMove() {
		StringBuilder sql = new StringBuilder(" insert into ras_revenue_su_dtl(ID,SHOP_ID,SERIAL_NO, ");
		sql.append(" ORDER_NO,TRANS_DATE,MONEY,PAY_TYPE,CUST_NAME,CUST_ACC,RMK,STATUS, ");
		sql.append(" AUDIT_ID,CREATE_NAME,CREATE_BY,CREATE_DATE,UPDATE_NAME,UPDATE_BY,UPDATE_DATE,IS_SUM) ");
		sql.append(" select nue.ID,nue.SHOP_ID,nue.SERIAL_NO,nue.ORDER_NO,nue.TRANS_DATE,nue.MONEY, ");
		sql.append(" nue.PAY_TYPE,nue.CUST_NAME,nue.CUST_ACC,nue.RMK, ");
		sql.append(" nue.STATUS,nue.AUDIT_ID,nue.CREATE_NAME,nue.CREATE_BY,nue.CREATE_DATE,nue.UPDATE_NAME,");
		sql.append(" nue.UPDATE_BY,nue.UPDATE_DATE,nue.IS_SUM from ras_revenue_dtl nue where nue.status=5 ");
		this.createSQLQuery(sql.toString()).executeUpdate();
	}
	/**
	 * 营收历史数据迁移公共sql
	 *
	 */
	public StringBuilder moveRevenueSql(){
		StringBuilder sql = new StringBuilder(" insert into ras_revenue_his_dtl(ID,SHOP_ID,SERIAL_NO, ");
		sql.append(" ORDER_NO,TRANS_DATE,MONEY,PAY_TYPE,CUST_NAME,CUST_ACC,RMK,STATUS, ");
		sql.append(" AUDIT_ID,CREATE_NAME,CREATE_BY,CREATE_DATE,UPDATE_NAME,UPDATE_BY,UPDATE_DATE,IS_SUM) ");
		sql.append(" select nue.ID,nue.SHOP_ID,nue.SERIAL_NO,nue.ORDER_NO,nue.TRANS_DATE,nue.MONEY, ");
		sql.append(" nue.PAY_TYPE,nue.CUST_NAME,nue.CUST_ACC,nue.RMK,nue.STATUS,nue.AUDIT_ID,nue.CREATE_NAME, ");
		sql.append(" nue.CREATE_BY,nue.CREATE_DATE,nue.UPDATE_NAME,nue.UPDATE_BY,nue.UPDATE_DATE,nue.IS_SUM from ");
		return sql;
	}
	/**
	 * 定时任务将营收稽核完成的历史数据迁移
	 *
	 */
	public void revenueHisDataMove() {
		StringBuilder sql = this.moveRevenueSql();
		sql.append("  ras_revenue_su_dtl nue left join RAS_AUDIT_INFO rai on nue.AUDIT_ID=rai.ID where rai.ID is null");
		this.createSQLQuery(sql.toString()).executeUpdate();
	}
	/**
	 * 定时任务将营收中现金和其它的历史数据迁移
	 *
	 */
	public void revenueMoneyHisDataMove(int hisDay) {
		StringBuilder sql = this.moveRevenueSql();
		sql.append(" ras_revenue_dtl nue left join RAS_AUDIT_INFO rai on rai.SHOP_ID=nue.SHOP_ID and ");
		sql.append(" rai.PAY_TYPE=nue.PAY_TYPE and BT_TO_CHAR(rai.CREATE_DATE,'yyyy/mm/dd')=BT_TO_CHAR(nue.TRANS_DATE,'yyyy/mm/dd') ");
		sql.append(" where nue.STATUS in (3,4,6) and nue.TRANS_DATE<:hisDate and nue.PAY_TYPE in ('001','006') and rai.STATUS in (3,4)");
		Map<String, Object> parms = new HashMap<String, Object>();
		Date hisDate = DateUtils.addDay(new Date(),hisDay*-1);
		parms.put("hisDate",hisDate);
		this.createSQLQuery(sql.toString(),parms).executeUpdate();
	}
	/**
	 * 定时任务将营收作废数据迁移
	 *
	 */
	public void revenueCancelMove() {
		StringBuilder sql = this.moveRevenueSql();
		sql.append("  ras_revenue_dtl nue where 1=1 and nue.STATUS=-2");
		this.createSQLQuery(sql.toString()).executeUpdate();
	}
	/**
	 * 在营收表作废、稽核完成表中迁移后的数据进行删除
	 *
	 */
	public void delRevenueData(String revenue){
		StringBuilder sql = new StringBuilder();
		if("revenue".equals(revenue)){
			//营收表中稽核完成数据迁移后删除
			sql.append("delete from ras_revenue_dtl where status = 5");
		}else if("revenueHis".equals(revenue)){
			//营收稽核完成表中历史数据的删除
			sql.append("delete from ras_revenue_su_dtl where not exists(select rai.ID from ");
			sql.append(" RAS_AUDIT_INFO rai where ras_revenue_su_dtl.AUDIT_ID=rai.ID)");
		}else {
			//营收表中作废数据的删除
			sql.append(" delete from ras_revenue_dtl where status = -2 ");
		}
		this.createSQLQuery(sql.toString()).executeUpdate();
	}
	/**
	 * 在营收表中现金和其它迁移后的数据进行删除
	 *
	 */
	public void delMoneyRevenueData(int hisDay){
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from ras_revenue_dtl where exists(select 1 from RAS_AUDIT_INFO rai ");
		sql.append(" where rai.SHOP_ID=ras_revenue_dtl.SHOP_ID and rai.PAY_TYPE=ras_revenue_dtl.PAY_TYPE");
		sql.append(" and BT_TO_CHAR(rai.CREATE_DATE,'yyyy/mm/dd')=BT_TO_CHAR(ras_revenue_dtl.TRANS_DATE,'yyyy/mm/dd') and ras_revenue_dtl.STATUS in (3,4,6)");
		sql.append(" and ras_revenue_dtl.TRANS_DATE<:hisDate and ras_revenue_dtl.PAY_TYPE in ('001','006') and rai.STATUS in (3,4))");
		Map<String, Object> parms = new HashMap<String, Object>();
		Date hisDate = DateUtils.addDay(new Date(),hisDay*-1);
		parms.put("hisDate",hisDate);
		this.createSQLQuery(sql.toString(),parms).executeUpdate();
	}
	
	/**
     * 根据单位id获取此单位中是否存在营收数据
     * @param corpId
     * @return 有效用户笔数
     */
    public int getValidRevenueDtlByCorpId(String corpId){
    	StringBuilder sql =new StringBuilder()
    		.append(" select count(*) from ras_revenue_dtl nue ")
	    	.append(" left join ras_shop rs on nue.shop_id=rs.id left join sys_corp sc on rs.corp_id=sc.id")
	    	.append(" where 1=1 and sc.id=:corpId");
        Map<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        return ((Number)this.createSQLQuery(sql.toString(),params).uniqueResult()).intValue();
    }
    
}
