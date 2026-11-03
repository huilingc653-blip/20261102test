package com.bytter.ras.dao;

import com.bytter.ras.entity.RasReceivedDtl;
import com.bytter.ras.vo.RasReceivedClaimVO;
import com.bytter.ras.vo.RasReceivedDtlVO;
import com.bytter.ras.vo.RasRevenueDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核实收数据dao
 * @date 2019/5/15 13:39
 */
@Repository
public class RasReceivedDao extends BaseDao<RasReceivedDtl, String> {


    /**
     * 通用查询营收数据sql
     *
     * @return
     */
    public StringBuilder getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select ved.id,ved.serial_no as serialNo,ved.ORDER_NO as orderNo,ved.shop_id as shopId,rs.name as shopName,ved.TRANS_MONEY as transMoney,");
        sql.append("rs.corp_id as corpId,sc.code as corpCode,sc.name as corpName,ved.payee_acc as payeeAcc,ved.payee_acc_name as payeeAccName,ved.payee_bank_name as payeeBankName,");
        sql.append("ved.pay_type as payType,ved.TRANS_DATE as transDate,ved.MONEY,ved.CUST_NAME as custName,ved.cust_acc as custAcc,ved.rmk,");
        sql.append("ved.date_source as dateSource,ved.create_date as createDate,ved.status,ved.Audit_Id from RAS_RECEIVED_DTL ved left join ");
        sql.append("ras_shop rs on ved.shop_id=rs.id left join sys_corp sc on rs.corp_id=sc.id where 1=1");
        return sql;
    }
    /**
     * 数据sql
     * @return
     */
    public StringBuilder getSql(String str){
        StringBuilder sql = new StringBuilder();
        sql.append("select "+str+"id,"+str+"serial_no,"+str+"ORDER_NO,"+str+"shop_id,"+str+"payee_acc,");
        sql.append(str+"TRANS_DATE,"+str+"MONEY,"+str+"pay_type,"+str+"payee_acc_name,"+str+"payee_bank_name,");
        sql.append(str+"CUST_NAME,"+str+"cust_acc,"+str+"rmk,"+str+"create_date,"+str+"date_source,");
        sql.append(str+"status,"+str+"Audit_Id ");
        return sql;
    }
    /**
     * 实收数据列表加载
     *
     * @param dataTablesInput
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,
                                               List<String> corpAuthList, List<String> shopAuthList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        //根据是否历史数据查询数据
        sql.append("select ved.id,ved.serial_no as serialNo,ved.ORDER_NO as orderNo,ved.shop_id as shopId,rs.name as shopName,");
        sql.append("rs.corp_id as corpId,sc.code as corpCode,sc.name as corpName,ved.payee_acc as payeeAcc,ved.payee_acc_name as payeeAccName,ved.payee_bank_name as payeeBankName,");
        sql.append("ved.pay_type as payType,ved.TRANS_DATE as transDate,ved.MONEY,ved.CUST_NAME as custName,ved.cust_acc as custAcc,ved.rmk,");
        sql.append("ved.date_source as dateSource,ved.create_date as createDate,ved.status,ved.Audit_Id,ved.trans_money as transMoney from ");
        if("5".equals(formdata.get("eqi_status")) && RasConstant.RAS_STATUS_NO.equals(formdata.get("isHistory"))){
            sql.append("("+this.getSql("vedDtl.")+" from ras_received_dtl vedDtl union all "+this.getSql("suDtl.")+" from ras_received_su_dtl suDtl) ved ");
        }else{
            if(RasConstant.RAS_STATUS_YES.equals(formdata.get("isHistory"))){
                sql.append("ras_received_his_dtl ved ");
            }else{
                sql.append("ras_received_dtl ved ");
            }
        }
        sql.append(" left join ras_shop rs on ved.shop_id=rs.id left join sys_corp sc on rs.corp_id=sc.id where 1=1");
        sql.append(" and ved.status not in(:status)");
        String transDate = (String) formdata.get("led_transDate");
        //手动拼接时间条件，时间戳类型
        if (StringUtils.isNotEmpty(transDate)) {
            transDate += " 23:59:59";
            formdata.put("led_transDate", transDate);
            dataTablesInput.setFormdata(formdata);
        }
        String receivedTransDate = (String) formdata.get("led_receivedTransDate");
        //手动拼接时间条件，时间戳类型
        if (StringUtils.isNotEmpty(receivedTransDate)) {
            receivedTransDate += " 23:59:59";
            formdata.put("led_receivedTransDate", receivedTransDate);
        }
        Map<String, Object> params = new HashMap<>();
        //排除已作废的数据
        params.put("status", new Integer[]{RasConstant.RAS_AUDIT_STATUS_RETURN_2});
        if ("1".equals(formdata.get("auditManual"))) {
            sql.append(" and (ved.pay_type not in (:paytypeMoneyOrOther) or (ved.pay_type in (:paytypeMoneyOrOther) and ved.IS_SUM=:isSum)) ");
            params.put("paytypeMoneyOrOther", new String[]{RasConstant.RAS_PAYTYPE_001, RasConstant.RAS_PAYTYPE_006});
            params.put("isSum", Integer.parseInt(RasConstant.RAS_STATUS_YES));
            formdata.remove("auditManual");
        }
        String shopAuthSql = SqlUtil.getInSqlByParams(shopAuthList, "ved.shop_id", "shopId", params);
        sql.append(" and (").append(shopAuthSql).append("or ved.shop_id is null) ");
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "rs.corp_id", params);
        sql.append(" and (").append(corpAuthSql).append("or rs.corp_id is null) ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), RasReceivedDtlVO.class, "ved", false, params);
    }


    /**
     * 根据条件获取营收表数据
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RasReceivedDtlVO> getAllRasReceivedDtlVOInfo(Map<String,Object> params){
        StringBuilder sql=new StringBuilder("select ID,SHOP_ID as shopId,SERIAL_NO as SERIALNO, ");
        sql.append(" ORDER_NO as ORDERNO,TRANS_DATE as TRANSDATE,str_TRANS_DATE as strTRANSDATE,TRANS_MONEY as TRANSMONEY,MONEY,PAY_TYPE PAYTYPE,IS_SUM as isSum from RAS_RECEIVED_DTL d ");
        sql.append(" where 1=1 ");
        if(params.containsKey("wIdList")){
            List<String> wIdList=(List<String>)params.get("wIdList");
            if(wIdList!=null&&wIdList.size()>900){
                sql.append(" and ").append(SqlUtil.getInSqlByParams(wIdList, "d.id", params));
            }else{
                sql.append(" and d.id in(:wIdList) ");
            }
        }
        if (params.containsKey("wId")) {
            sql.append(" and d.id =:wId ");
        }
        if (params.containsKey("ids")) {
            sql.append(" and d.id in (:ids) ");
        }
        if (params.containsKey("aids")) {
            String[] idsStr = (String[])params.get("aids");
            String ids =SqlUtil.getInSql(idsStr, "d.audit_Id", true);
            sql.append(" and ").append(ids);
        }
        if (params.containsKey("statusOld")) {
            sql.append(" and d.status in (:statusOld) ");
        }
        if (params.containsKey("statusNew")) {
            sql.append(" and d.status!=:statusNew ");
        }
        if(params.containsKey("unId")){
            sql.append(" and d.un_Id is null ");
        }
        if(params.containsKey("wUnid")){
            sql.append(" and d.un_Id=:wUnid ");
        }
        if (params.containsKey("wShopId")) {
            sql.append(" and d.shop_Id=:wShopId ");
        }
        if (params.containsKey("shopIdArray")) {
            sql.append(" and d.shop_Id in(:shopIdArray) ");
        }
        if (params.containsKey("wAuditStatus")) {
            sql.append(" and d.status=:wAuditStatus ");
        }
        if (params.containsKey("startReceivedDate")) {
            sql.append(" and d.trans_Date>=:startReceivedDate ");
        }
        if (params.containsKey("wPayType")) {
            sql.append(" and d.pay_Type=:wPayType ");
        }
        if (params.containsKey("payTypeArray")) {
            sql.append(" and d.pay_Type in (:payTypeArray) ");
        }
        if (params.containsKey("dataSourceArray")) {
            sql.append(" and d.date_Source in (:dataSourceArray) ");
        }
        if (params.containsKey("startDate")) {
            sql.append(" and d.trans_Date >= :startDate ");
        }
        if (params.containsKey("endDate")) {
            sql.append(" and d.trans_Date < :endDate ");
        }
        if (params.containsKey("receivedTransDateStart")) {
            sql.append(" and d.trans_Date >= :receivedTransDateStart ");
        }
        if (params.containsKey("receivedTransDateEnd")) {
            sql.append(" and d.trans_Date < :receivedTransDateEnd ");
        }
        if (params.containsKey("moneyStart")) {
            sql.append(" and d.money >= :moneyStart ");
        }
        if (params.containsKey("moneyEnd")) {
            sql.append(" and d.money <= :moneyEnd ");
        }

        if(params.containsKey("notIsSum")){
            sql.append(" and (d.is_Sum !=:notIsSum or d.is_Sum is null ) ");
        }

        if(params.containsKey("maxResult")){
            int maxResult=Integer.parseInt(String.valueOf(params.get("maxResult")));
            int first=0;
            if(params.containsKey("first")){
                first=Integer.parseInt(String.valueOf(params.get("first")));
            }
            sql.append(" order by shop_id,pay_type,TRANS_DATE,id ");
            return this.listBySQLAliasToBean(sql.toString(), first, maxResult, params, RasReceivedDtlVO.class);
        }else{
            sql.append(" order by shop_id,pay_type,TRANS_DATE ");
            return this.listBySQLAliasToBean(sql.toString(), params, RasReceivedDtlVO.class);
        }
//    	sql.append(" order by shop_id,pay_type,TRANS_DATE,id ");
//    	return this.listBySQLAliasToBean(sql.toString(), first, maxResult, params, RasReceivedDtlVO.class);
//        sql.append(" order by shop_id,pay_type,TRANS_DATE ");
//        return this.listBySQLAliasToBean(sql.toString(), params, RasReceivedDtlVO.class);
    }


    /**
     * 根据ids批量修改实收数据明细表数据状态
     *
     * @param params
     * @return
     */
    public int batchUpdate(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update RasReceivedDtl d "
                + " set "
                + "d.updateDate = :updateDate ");
        if (params.containsKey("updateBy")) {
            hql.append(",d.updateBy = :updateBy ");
        }
        if (params.containsKey("updateName")) {
            hql.append(",d.updateName = :updateName ");
        }
        if (params.containsKey("statusNew")) {
            hql.append(",d.status = :statusNew ");
        }
        if (params.containsKey("auditStatus")) {
            hql.append(",d.status = :auditStatus ");
        }
        if (params.containsKey("shopId")) {
            hql.append(",d.shopId = :shopId ");
        }
        if (params.containsKey("payType")) {
            hql.append(",d.payType = :payType ");
        }
        if (params.containsKey("claimType")) {
            hql.append(",d.claimType = :claimType ");
        }
        if (params.containsKey("auditId")) {
            hql.append(",d.auditId = :auditId ");
        }
        if (params.containsKey("isAuditIdInter")) {
            hql.append(",d.auditId = (select i.id from RasAuditInter i where d.id=i.receivedId  ) ");
        }
        if(params.containsKey("unId")){
            hql.append(",d.unId = :unId ");
        }
        if(params.containsKey("wUnid")){
            hql.append(",d.unId = null ");
        }
        hql.append(" where 1=1 ");

        if(params.containsKey("wIdList")){
            @SuppressWarnings("unchecked")
            List<String> wIdList=(List<String>)params.get("wIdList");
            if(wIdList!=null&&wIdList.size()>900){
                hql.append(" and ").append(SqlUtil.getInSqlByParams(wIdList, "d.id", params));
            }else{
                hql.append(" and d.id in(:wIdList) ");
            }
        }else if(params.containsKey("isAuditIdInter")){
//        	hql.append(" and d.id = (select i.receivedId from RasAuditInter i where d.id=i.receivedId  ) ");
            hql.append(" and exists ( from RasAuditInter i where d.id=i.receivedId) ");
        }
        if (params.containsKey("wId")) {
            hql.append(" and d.id =:wId ");
        }
        if (params.containsKey("ids")) {
            hql.append(" and d.id in (:ids) ");
        }
        if (params.containsKey("aids")) {
            String[] idsStr = (String[])params.get("aids");
            String ids =SqlUtil.getInSql(idsStr, "d.auditId", true);
            hql.append(" and ").append(ids);
        }
        if (params.containsKey("statusOld")) {
            hql.append(" and d.status in (:statusOld) ");
        }
        if (params.containsKey("statusNew")) {
            hql.append(" and d.status!=:statusNew ");
        }
        if(params.containsKey("unId")){
            hql.append(" and d.unId is null ");
        }
        if(params.containsKey("wUnid")){
            hql.append(" and d.unId=:wUnid ");
        }
        if (params.containsKey("wShopId")) {
            hql.append(" and d.shopId=:wShopId ");
        }
        if (params.containsKey("shopIdArray")) {
            hql.append(" and d.shopId in(:shopIdArray) ");
        }
        if (params.containsKey("wAuditStatus")) {
            hql.append(" and d.status=:wAuditStatus ");
        }
        if (params.containsKey("startReceivedDate")) {
            hql.append(" and d.transDate>=:startReceivedDate ");
        }
        if (params.containsKey("wPayType")) {
            hql.append(" and d.payType=:wPayType ");
        }
        if (params.containsKey("payTypeArray")) {
            hql.append(" and d.payType in (:payTypeArray) ");
        }
        if (params.containsKey("dataSourceArray")) {
            hql.append(" and d.dateSource in (:dataSourceArray) ");
        }
        if (params.containsKey("startDate")) {
            hql.append(" and d.transDate >= :startDate ");
        }
        if (params.containsKey("endDate")) {
            hql.append(" and d.transDate < :endDate ");
        }
        if (params.containsKey("receivedTransDateStart")) {
            hql.append(" and d.transDate >= :receivedTransDateStart ");
        }
        if (params.containsKey("receivedTransDateEnd")) {
            hql.append(" and d.transDate < :receivedTransDateEnd ");
        }
        if (params.containsKey("moneyStart")) {
            hql.append(" and d.money >= :moneyStart ");
        }
        if (params.containsKey("moneyEnd")) {
            hql.append(" and d.money <= :moneyEnd ");
        }

        if(params.containsKey("notIsSum")){
            hql.append(" and (d.isSum !=:notIsSum or d.isSum is null ) ");
        }

//      //自动任务条件处理
//      if("1".equals(params.get(RasConstant.RAS_AUTOTASK_OPKEY))){
//
//      }
        return this.batchExecute(hql.toString(), params);
    }

    /**
     * 实收明细列表加载（报表统计）差异稽核
     *
     * @param dataTablesInput
     * @param id
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getReceivedDifferentDataTablesUnion(DataTablesInput dataTablesInput, String id) {
        StringBuilder sql = this.getCommonSql();
        sql.append(" and ved.Audit_Id =:auditId");
        Map<String, Object> params = new HashMap<>();
        params.put("auditId", id);
        DataTablesOutput dataTablesOutput = getDynamicBySQL(dataTablesInput, sql.toString(), RasReceivedDtlVO.class, "ved", false, params);
        if(dataTablesOutput.getData().size() == 0){
            StringBuilder sq = new StringBuilder();
            sq.append("select ved.id,ved.serial_no as serialNo,ved.ORDER_NO as orderNo,ved.shop_id as shopId,rs.name as shopName,ved.TRANS_MONEY as transMoney,");
            sq.append("rs.corp_id as corpId,sc.code as corpCode,sc.name as corpName,ved.payee_acc as payeeAcc,ved.payee_acc_name as payeeAccName,ved.payee_bank_name as payeeBankName,");
            sq.append("ved.pay_type as payType,ved.TRANS_DATE as transDate,ved.MONEY,ved.CUST_NAME as custName,ved.cust_acc as custAcc,ved.rmk,");
            sq.append("ved.date_source as dateSource,ved.create_date as createDate,ved.status,ved.Audit_Id from RAS_RECEIVED_HIS_DTL ved left join ");
            sq.append("ras_shop rs on ved.shop_id=rs.id left join sys_corp sc on rs.corp_id=sc.id where 1=1 and ved.Audit_Id =:auditId");
            return getDynamicBySQL(dataTablesInput, sq.toString(), RasReceivedDtlVO.class, "ved", false, params);
        }
        return dataTablesOutput;
    }

    /**
     * @param @param params    参数
     * @return void    返回类型
     * @throws
     * @Title: tovoid
     * @Description: TODO(营收数据作废)
     */
    public int tovoid(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder();
        hql.append(" update RasReceivedDtl set status=-2 where id =:ids and (status=1 or status=0)");
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
        hql.append(" update RasReceivedDtl set status=1 where 1=1");
        hql.append(" and bt_to_char(trans_date,'yyyy/mm/dd')=bt_to_char(:sameDate,'yyyy/mm/dd')");
        hql.append(" and status=6");
        hql.append(" and pay_Type=:payType");
        hql.append(" and shop_Id=:shopId");
        return this.batchExecute(hql.toString(), params);
    }


    public boolean getIsRepeatByrasReceivedDtlVO(RasReceivedDtlVO rasReceivedDtlVO) {
        Map<String, Object> parms = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select id");
        sql.append(" from RAS_RECEIVED_DTL rrl");
        sql.append(" where 1=1");
        //交易日期
        if (null != rasReceivedDtlVO.getTransDate()) {
            sql.append(" and rrl.trans_Date=:transDate");
            parms.put("transDate", rasReceivedDtlVO.getTransDate());
        }
        //交易金额
        if (null != rasReceivedDtlVO.getMoney()) {
            sql.append(" and rrl.Money=:money");
            parms.put("money", rasReceivedDtlVO.getMoney());
        }
        sql.append(" and rrl.status=1");
        List<RasRevenueDtlVO> list = this.listBySQLAliasToBean(sql.toString(), parms, RasRevenueDtlVO.class);
        if (null != list && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 实收明细列表加载（报表统计）待稽核
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getReceivedNotDataTablesUnion(DataTablesInput dataTablesInput, String[] split) {
        StringBuilder sql = this.getCommonSql();
        sql.append(" and ((ved.shop_id=:shopId)or(ved.shop_id is null and :shopId is null)) and ((ved.pay_Type=:payType)or(ved.pay_Type is null and :payType is null))");
        sql.append(" and BT_TO_CHAR(ved.TRANS_DATE,'yyyy-MM-dd') =:transDate and ved.status =:status");
        Map<String, Object> params = new HashMap<>();
        params.put("shopId", split[0]);
        params.put("payType", split[1]);
        params.put("transDate", split[2]);
        params.put("status", split[3]);
        return getDynamicBySQL(dataTablesInput, sql.toString(), RasReceivedDtlVO.class, "ved", false, params);
    }

    /**
     * 根据ids批量修改实收数据明细表数据状态
     *
     * @param params
     * @return
     */
    public int batchUpdateNoCheckStatus(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update RasReceivedDtl "
                + " set "
                + "updateDate = :updateDate ");
        if (params.containsKey("updateBy")) {
            hql.append(",updateBy = :updateBy ");
        }
        if (params.containsKey("updateName")) {
            hql.append(",updateName = :updateName ");
        }
        if (params.containsKey("statusNew")) {
            hql.append(",status = :statusNew ");
        }
        if (params.containsKey("shopId")) {
            hql.append(",shopId = :shopId ");
        }
        if (params.containsKey("payType")) {
            hql.append(",payType = :payType ");
        }
        if (params.containsKey("claimType")) {
            hql.append(",claimType = :claimType ");
        }
        hql.append(" where id in (:ids) ");
        return this.batchExecute(hql.toString(), params);
    }

    /**
     * 根据ID查询实收数据明细历史数据
     *
     * @param id 实收数据明细历史id
     * @return
     */
    public List<RasReceivedClaimVO> getRasReceivedClaimHISById(String id) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" rc.id,rc.serial_no as serialNo,rc.shop_id as shopId,rc.order_no as orderNo,rc.trans_date as transDate, ");
        sql.append(" rc.money,rc.yh_money as yhMoney,rc.trans_money as transMoney,rc.pay_type as payType,rc.date_Source as dateSource, ");
        sql.append(" rc.cust_name as custname,rc.cust_acc as custAcc,rc.payee_acc as payeeAcc,rc.payee_acc_name as payeeAccName, ");
        sql.append(" rc.payee_bank_name as payeeBankName,rc.rmk,rc.claim_type as claimType,rc.status,rc.audit_id as auditId, ");
        sql.append(" rc.create_name as createName,rc.create_by as createBy,rc.create_date as createDate ");
        sql.append(" from RAS_RECEIVED_HIS_DTL rc ");
        sql.append(" WHERE rc.id=:id ");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<RasReceivedClaimVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, RasReceivedClaimVO.class);
        return listInfo;
    }


    /**
     * 根据参数获取稽核数据
     *
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RasReceivedDtl> getRasReceivedDtlByParams(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("from RasReceivedDtl where 1=1 ");
        if (params.containsKey("id")) {
            hql.append(" and id=:id ");
        }
        if (params.containsKey("wPayType")) {
            hql.append(" and payType=:wPayType ");
        }
        if(params.containsKey("wUnid")){
            hql.append(" and unId=:wUnid ");
        }
        if (params.containsKey("wShopId")) {
            hql.append(" and shopId=:wShopId ");
        }
        if (params.containsKey("wClaimType")) {
            hql.append(" and claimType =:wClaimType ");
        }
        if (params.containsKey("wAuditStatus")) {
            hql.append(" and status =:wAuditStatus ");
        }
        if (params.containsKey("isSum")) {
            hql.append(" and isSum =:isSum ");
        }
        if (params.containsKey("startDate")) {
            hql.append(" and transDate >= :startDate ");
        }
        if (params.containsKey("endDate")) {
            hql.append(" and transDate < :endDate ");
        }
        if ("1".equals(params.get("isShopIdAndCreateUser"))) {
            if (params.containsKey("wManualShopId") && params.containsKey("wManualUserCode")) {
                hql.append(" and (shopId=:wManualShopId or createBy=:wManualUserCode )");
            }
        }
        return this.listByHQL(hql.toString(), params);
    }


    /**
     * 根据参数获取稽核数据
     *
     * @param params
     * @return
     */
    public List<RasReceivedDtl> getRasReceivedDtlSumByParams(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" pay_type as paytype,shop_Id as shopId,max(" + RasConstant.RAS_AUTOT_RECEIVED_FIELD_TRANSDATE + ") transDate, ");
        sql.append(" sum(" + RasConstant.RAS_AUTOT_RECEIVED_FIELD_MONEY + ") as " + RasConstant.RAS_AUTOT_RECEIVED_FIELD_MONEY_ALIAS + ",");
        sql.append(" sum(" + RasConstant.RAS_AUTOT_RECEIVED_FIELD_TRANSMONEY + ") as " + RasConstant.RAS_AUTOT_RECEIVED_FIELD_TRANSMONEY_ALIAS + "," + RasConstant.RAS_CLAIM_TYPE_1 + " as claimType," + RasConstant.RAS_AUDIT_STATUS_1 + " status, ");
        sql.append(" " + RasConstant.RAS_STATUS_YES + " as isSum from RAS_RECEIVED_DTL where 1=1 ");
        if (params.containsKey("id")) {
            sql.append(" and id=:id ");
        }
        if (params.containsKey("wPayType")) {
            sql.append(" and pay_Type=:wPayType ");
        }
        if(params.containsKey("wUnid")){
            sql.append(" and un_Id=:wUnid ");
        }
        if (params.containsKey("wShopId")) {
            sql.append(" and shop_Id=:wShopId ");
        }
        if (params.containsKey("wClaimType")) {
            sql.append(" and claim_Type =:wClaimType ");
        }
        if (params.containsKey("wAuditStatus")) {
            sql.append(" and status =:wAuditStatus ");
        }
        if (params.containsKey("startDate")) {
            sql.append(" and trans_Date >= :startDate ");
        }
        if(params.containsKey("notIsSum")){
            sql.append(" and (is_Sum !=:notIsSum or is_Sum is null ) ");
        }
        if (params.containsKey("endDate")) {
            sql.append(" and trans_Date < :endDate ");
        }
        sql.append(" group by pay_Type,shop_Id ");
        return this.listBySQLAliasToBean(sql.toString(), params, RasReceivedDtl.class);
    }

    /**
     * 实收数据认领列表加载
     *
     * @param dataTablesInput
     */
    public DataTablesOutput getClaimListDataTablesUnion(DataTablesInput dataTablesInput,
                                                        List<String> corpAuthList, List<String> shopAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" rc.id,rc.serial_no as serialNo,rc.shop_id as shopId,s.name as shopName,rc.order_no as orderNo,rc.trans_date as transDate, ");
        sql.append(" rc.money,rc.yh_money as yhMoney,rc.trans_money as transMoney,rc.pay_type as payType,rc.date_Source as dateSource, ");
        sql.append(" rc.cust_name as custname,rc.cust_acc as custAcc,rc.payee_acc as payeeAcc,rc.payee_acc_name as payeeAccName, ");
        sql.append(" rc.payee_bank_name as payeeBankName,rc.rmk,rc.claim_type as claimType,rc.status,rc.audit_id as auditId, ");
        sql.append(" rc.create_name as createName,rc.create_by as createBy,rc.create_date as createDate,c.name as corpName,c.id as corpId,c.code as corpCode ");
        sql.append(" from RAS_RECEIVED_DTL rc ");
        sql.append(" left join ras_shop s on rc.shop_id = s.id ");
        sql.append(" left join sys_corp c  on s.corp_id = c.id ");
        sql.append(" where (rc.status=" + RasConstant.RAS_AUDIT_STATUS_0 + " or rc.status=" + RasConstant.RAS_AUDIT_STATUS_1 + ") ");
        Map<String, Object> params = new HashMap<>();
        params.put("userLogingName", dataTablesInput.getCurrentUser().getLoginName());
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.id", params);
        String shopAuthSql = SqlUtil.getInSqlByParams(shopAuthList, "s.id", "shopIdstr", params);
        sql.append("   and (( ");
        sql.append(shopAuthSql + " and ");
        sql.append(corpAuthSql).append(" ) or ((rc.shop_id is null or rc.shop_id=' ') and rc.create_by=:userLogingName))");

        //判断认领方式是否为空,是则默认查待认领状态
        if (dataTablesInput.getFormdata().get("eqi_claimType") == null) {
            sql.append(" and rc.claim_type=:claimType ");
            params.put("claimType", RasConstant.RAS_CLAIM_TYPE_0);
        }
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        String transDate = (String) formdata.get("led_transDate");
        //手动拼接时间条件，时间戳类型
        if (StringUtils.isNotEmpty(transDate)) {
            transDate += " 23:59:59";
            formdata.put("led_transDate", transDate);
            dataTablesInput.setFormdata(formdata);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), RasReceivedClaimVO.class, "", false, params);
    }

    /**
     * 根据参数获取实收数据认领数据
     *
     * @param rasReceivedClaimVO
     */
    public List<RasReceivedClaimVO> getClaimListData(RasReceivedClaimVO rasReceivedClaimVO,
                                                     List<String> corpAuthList, List<String> shopAuthList){
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(" rc.id,rc.serial_no as serialNo,rc.shop_id as shopId,s.name as shopName,rc.order_no as orderNo,rc.trans_date as transDate, ");
        sql.append(" rc.money,rc.yh_money as yhMoney,rc.trans_money as transMoney,rc.pay_type as payType,rc.date_Source as dateSource, ");
        sql.append(" rc.cust_name as custname,rc.cust_acc as custAcc,rc.payee_acc as payeeAcc,rc.payee_acc_name as payeeAccName, ");
        sql.append(" rc.payee_bank_name as payeeBankName,rc.rmk,rc.claim_type as claimType,rc.status,rc.audit_id as auditId, ");
        sql.append(" rc.create_name as createName,rc.create_by as createBy,rc.create_date as createDate,c.name as corpName,c.id as corpId,c.code as corpCode ");
        sql.append(" FROM RAS_RECEIVED_DTL rc ");
        sql.append(" left join ras_shop s on rc.shop_id = s.id ");
        sql.append(" left join sys_corp c  on s.corp_id = c.id ");
        sql.append(" WHERE rc.status=" + RasConstant.RAS_AUDIT_STATUS_0 );
        Map<String, Object> params = new HashMap<>();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.id", params);
        String shopAuthSql = SqlUtil.getInSqlByParams(shopAuthList, "s.id", "shopIdstr", params);
        sql.append("   AND (( ");
        if (StringUtils.isNotEmpty(rasReceivedClaimVO.getCorpId())) {
            sql.append(" c.id = :corpId ");
            params.put("corpId",rasReceivedClaimVO.getCorpId());
        }else{
            sql.append(corpAuthSql);
        }
        if (StringUtils.isNotEmpty(rasReceivedClaimVO.getShopId())) {
            sql.append(" AND s.id = :shopId ");
            params.put("shopId",rasReceivedClaimVO.getShopId());
        }else{
            sql.append(" AND "+shopAuthSql);
        }
        sql.append(" ) ");
        if (StringUtils.isEmpty(rasReceivedClaimVO.getCorpId()) && StringUtils.isEmpty(rasReceivedClaimVO.getShopId())) {
            sql.append(" or ((rc.shop_id is null or rc.shop_id=' ') AND rc.create_by=:userLogingName))");
            params.put("userLogingName", rasReceivedClaimVO.getUser().getLoginName());
        }else{
            sql.append(" ) ");
        }

        //缴款方式
        if (StringUtils.isNotEmpty(rasReceivedClaimVO.getPayType())) {
            sql.append(" and rc.pay_type=:payType ");
            params.put("payType",rasReceivedClaimVO.getPayType());
        }
        //收款账号
        if (StringUtils.isNotEmpty(rasReceivedClaimVO.getPayeeAcc())) {
            sql.append(" and rc.PAYEE_ACC like :payeeAcc ");
            params.put("payeeAcc","%"+rasReceivedClaimVO.getPayeeAcc()+"%");
        }
        //交易日期
        if (StringUtils.isNotEmpty(rasReceivedClaimVO.getTransDateStart())) {
            sql.append(" and rc.TRANS_DATE >= :transDateStart ");
            params.put("transDateStart",rasReceivedClaimVO.getTransDateStart());
        }
        if (StringUtils.isNotEmpty(rasReceivedClaimVO.getTransDateEnd())) {
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd" );
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS" );
            String dateEnd = dateFormat1.format(rasReceivedClaimVO.getTransDateEnd())+" 23:59:59 999";
            Date transDateEnd = null;
            try {
                transDateEnd = dateFormat2.parse(dateEnd);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            sql.append(" and rc.TRANS_DATE <= :transDateEnd ");
            params.put("transDateEnd",transDateEnd);
        }

        //交易金额
        if (StringUtils.isNotEmpty(rasReceivedClaimVO.getTransMoney())) {
            sql.append(" and rc.TRANS_MONEY >= :transMoney ");
            params.put("transMoney",rasReceivedClaimVO.getTransMoney());
        }
        if (StringUtils.isNotEmpty(rasReceivedClaimVO.getEndTransMoney())) {
            sql.append(" and rc.TRANS_MONEY <= :transMoneyEnd ");
            params.put("transMoneyEnd",rasReceivedClaimVO.getEndTransMoney());
        }

        //认领方式为待认领状态
        sql.append(" and rc.claim_type=:claimType ");
        params.put("claimType", RasConstant.RAS_CLAIM_TYPE_0);

        return  this.listBySQLAliasToBean(sql.toString(), params, RasReceivedClaimVO.class);
    }

    /**
     * @param @param  info
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: getIsRasReceivedDtlVO
     * @Description: TODO(营收导入判断是否有重复数据)
     */
    public boolean getIsRasReceivedDtlVO(RasReceivedDtlVO info) {

        Map<String, Object> parms = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select id");
        sql.append(" from ras_received_dtl rrl");
        sql.append(" where 1=1");
        //门店编码
        if (null != info.getShopId()) {
            sql.append(" and rrl.shop_id=:shopId");
            parms.put("shopId", info.getShopId());
        }
        //交易金额
        if (null != info.getTransMoney()) {
            sql.append(" and rrl.trans_money=:money");
            parms.put("money", info.getTransMoney());
        }
        //营收订单号
        if (null != info.getOrderNo()) {
            sql.append(" and rrl.ORDER_NO=:orderNo");
            parms.put("orderNo", info.getOrderNo());
        }
        //交易流水号
        if (null != info.getSerialNo()) {
            sql.append(" and rrl.serial_no=:serialNo");
            parms.put("serialNo", info.getSerialNo());
        }
        sql.append(" and (rrl.status=1 or rrl.status=0)");
        List<RasRevenueDtlVO> list = this.listBySQLAliasToBean(sql.toString(), parms, RasRevenueDtlVO.class);
        if (null != list && list.size() > 0) {
            return true;
        }
        return false;
    }
    /**
     * 实收历史数据公共sql
     *
     */
    public StringBuilder moveReceivedSql(){
        StringBuilder sql = new StringBuilder(" insert into ras_received_his_dtl(ID,SHOP_ID,SERIAL_NO, ");
        sql.append(" ORDER_NO,TRANS_DATE,MONEY,YH_MONEY,TRANS_MONEY,DATE_SOURCE,PAY_TYPE, ");
        sql.append(" CUST_NAME,CUST_ACC,PAYEE_ACC,PAYEE_ACC_NAME,PAYEE_BANK_NAME,RMK,CLAIM_TYPE,STATUS, ");
        sql.append(" AUDIT_ID,CREATE_NAME,CREATE_BY,CREATE_DATE,UPDATE_NAME,UPDATE_BY,UPDATE_DATE,IS_SUM) select vedDtl.ID,vedDtl.SHOP_ID,");
        sql.append(" vedDtl.SERIAL_NO,vedDtl.ORDER_NO,vedDtl.TRANS_DATE,vedDtl.MONEY,vedDtl.YH_MONEY,vedDtl.TRANS_MONEY,vedDtl.DATE_SOURCE, ");
        sql.append(" vedDtl.PAY_TYPE,vedDtl.CUST_NAME,vedDtl.CUST_ACC,vedDtl.PAYEE_ACC,vedDtl.PAYEE_ACC_NAME,vedDtl.PAYEE_BANK_NAME,vedDtl.RMK, ");
        sql.append(" vedDtl.CLAIM_TYPE,vedDtl.STATUS,vedDtl.AUDIT_ID,vedDtl.CREATE_NAME,vedDtl.CREATE_BY,vedDtl.CREATE_DATE,vedDtl.UPDATE_NAME,");
        sql.append(" vedDtl.UPDATE_BY,vedDtl.UPDATE_DATE,vedDtl.IS_SUM from ");
        return sql;
    }
    /**
     * 定时任务将实收稽核完成的数据迁移
     *
     */
    public void receivedDataMove() {
        StringBuilder sql = new StringBuilder(" insert into ras_received_su_dtl(ID,SHOP_ID, ");
        sql.append(" SERIAL_NO,ORDER_NO,TRANS_DATE,MONEY,YH_MONEY,TRANS_MONEY,DATE_SOURCE,PAY_TYPE, ");
        sql.append(" CUST_NAME,CUST_ACC,PAYEE_ACC,PAYEE_ACC_NAME,PAYEE_BANK_NAME,RMK,CLAIM_TYPE,STATUS, ");
        sql.append(" AUDIT_ID,CREATE_NAME,CREATE_BY,CREATE_DATE,UPDATE_NAME,UPDATE_BY,UPDATE_DATE,IS_SUM) select vedDtl.ID,vedDtl.SHOP_ID,");
        sql.append(" vedDtl.SERIAL_NO,vedDtl.ORDER_NO,vedDtl.TRANS_DATE,vedDtl.MONEY,vedDtl.YH_MONEY,vedDtl.TRANS_MONEY,vedDtl.DATE_SOURCE, ");
        sql.append(" vedDtl.PAY_TYPE,vedDtl.CUST_NAME,vedDtl.CUST_ACC,vedDtl.PAYEE_ACC,vedDtl.PAYEE_ACC_NAME,vedDtl.PAYEE_BANK_NAME,vedDtl.RMK, ");
        sql.append(" vedDtl.CLAIM_TYPE,vedDtl.STATUS,vedDtl.AUDIT_ID,vedDtl.CREATE_NAME,vedDtl.CREATE_BY,vedDtl.CREATE_DATE,vedDtl.UPDATE_NAME,");
        sql.append(" vedDtl.UPDATE_BY,vedDtl.UPDATE_DATE,vedDtl.IS_SUM from ras_received_dtl vedDtl where vedDtl.status = 5");
        this.createSQLQuery(sql.toString()).executeUpdate();
    }
    /**
     * 定时任务将实收作废数据迁移
     *
     */
    public void receivedCancelMove() {
        StringBuilder sql = this.moveReceivedSql();
        sql.append("  ras_received_dtl vedDtl where vedDtl.status = -2");
        this.createSQLQuery(sql.toString()).executeUpdate();
    }
    /**
     * 定时任务将实收历史的数据迁移
     *
     */
    public void receivedHisDataMove() {
        StringBuilder sql = this.moveReceivedSql();
        sql.append(" ras_received_su_dtl vedDtl left join RAS_AUDIT_INFO rai on vedDtl.AUDIT_ID=rai.ID where rai.ID is null");
        this.createSQLQuery(sql.toString()).executeUpdate();
    }
    /**
     * 定时任务将实收中现金和其它的历史数据迁移
     *
     */
    public void receivedMoneyHisDataMove(int hisDay) {
        StringBuilder sql = this.moveReceivedSql();
        sql.append(" ras_received_dtl vedDtl left join RAS_AUDIT_INFO rai on rai.SHOP_ID=vedDtl.SHOP_ID and  ");
        sql.append(" rai.PAY_TYPE=vedDtl.PAY_TYPE and BT_TO_CHAR(rai.CREATE_DATE,'yyyy/mm/dd')=BT_TO_CHAR(vedDtl.TRANS_DATE,'yyyy/mm/dd')  ");
        sql.append(" where vedDtl.STATUS in (3,4,6) and vedDtl.TRANS_DATE < :hisDate and vedDtl.PAY_TYPE in ('001','006') and rai.STATUS in (3,4)");
        Map<String, Object> parms = new HashMap<String, Object>();
        Date hisDate = DateUtils.addDay(new Date(),hisDay*-1);
        parms.put("hisDate",hisDate);
        this.createSQLQuery(sql.toString(),parms).executeUpdate();
    }
    /**
     * 在实收表和稽核完成表中迁移后的数据进行删除
     *
     */
    public void delReceivedData(String received){
        StringBuilder sql = new StringBuilder();
        if("received".equals(received)){
            //实收表中稽核完成数据迁移后删除
            sql.append("delete from ras_received_dtl where status = 5");
        }else if("receivedHis".equals(received)){
            //实收稽核完成表中历史数据的删除
            sql.append("delete from ras_received_su_dtl where not exists(select rai.ID from ");
            sql.append(" RAS_AUDIT_INFO rai where ras_received_su_dtl.AUDIT_ID=rai.ID)");
        }else {
            //实收表中作废数据的删除
            sql.append(" delete from ras_received_dtl where status = -2 ");
        }
        this.createSQLQuery(sql.toString()).executeUpdate();
    }
    /**
     * 在实收表中现金和其它迁移后的数据进行删除
     *
     */
    public void delMoneyReceivedData(int hisDay){
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ras_received_dtl where exists (select 1 from RAS_AUDIT_INFO rai where rai.SHOP_ID=ras_received_dtl.SHOP_ID and ");
        sql.append(" rai.PAY_TYPE=ras_received_dtl.PAY_TYPE and BT_TO_CHAR(rai.CREATE_DATE,'yyyy/mm/dd')=BT_TO_CHAR(ras_received_dtl.TRANS_DATE,'yyyy/mm/dd') ");
        sql.append(" and rai.STATUS in (3,4) and ras_received_dtl.STATUS in (3,4,6)  and ras_received_dtl.PAY_TYPE in ('001','006'))");
        Map<String, Object> parms = new HashMap<String, Object>();
        Date hisDate = DateUtils.addDay(new Date(),hisDay*-1);
        parms.put("hisDate",hisDate);
        this.createSQLQuery(sql.toString(),parms).executeUpdate();
    }
    
	/**
     * 根据单位id获取此单位中是否存在实收数据
     * @param corpId
     * @return 有效用户笔数
     */
    public int getValidReceivedByCorpId(String corpId){
    	StringBuilder sql =new StringBuilder()
    		.append(" select count(*) from ras_received_dtl nue ")
	    	.append(" left join ras_shop rs on nue.shop_id=rs.id left join sys_corp sc on rs.corp_id=sc.id")
	    	.append(" where 1=1 and sc.id=:corpId");
        Map<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        return ((Number)this.createSQLQuery(sql.toString(),params).uniqueResult()).intValue();
    }
}
