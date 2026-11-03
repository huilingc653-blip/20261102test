package com.bytter.ras.dao;

import com.bytter.ras.entity.RasAuditInfo;
import com.bytter.ras.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核查询统计dao
 * @date 2019/5/16 13:39
 */
@Repository
public class RasQueryDao extends BaseDao<RasAuditInfo,String> {

    @Autowired
    public RasRevenueDao rasRevenueDao;
    @Autowired
    public RasReceivedDao rasReceivedDao;

    /**
     * 已处理差异明细查询列表加载
     * @param dataTablesInput
     */
    public DataTablesOutput getDifferentDataTablesUnion(DataTablesInput dataTablesInput,
              List<String> corpAuthList,List<String> shopAuthList) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rai.id,rai.audit_no as auditNo,rai.shop_id as shopId,rs.name as shopName,sc.code as corpCode,sc.name as corpName,");
        sql.append("rs.corp_id as corpId,rai.PAY_TYPE as payType,rai.revenue_money as revenueMoney,");
        sql.append("rai.revenue_size as revenueSize,rai.received_money as receivedMoney,rai.received_size as receivedSize,");
        sql.append("rai.less_money as lessMoney,rai.create_date as createDate,rai.create_name as createName,rai.create_by as createBy,");
        sql.append("rai.rmk from ");
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        String payType = (String)formdata.get("eqs_payType");
        if(RasConstant.RAS_PAYTYPE_001.equals(payType) || RasConstant.RAS_PAYTYPE_006.equals(payType) || StringUtils.isBlank(payType)){
            sql.append("( select aud.id,aud.audit_no,aud.shop_id,aud.PAY_TYPE,aud.revenue_money,aud.status,");
            sql.append(" aud.revenue_size,aud.received_money,aud.received_size,aud.less_money,aud.create_date,aud.create_name,aud.create_by,aud.rmk ");
            sql.append(" from RAS_AUDIT_INFO aud union all ");
            sql.append(" select ras.id,ras.audit_no,ras.shop_id,ras.PAY_TYPE,ras.revenue_money,ras.status,");
            sql.append(" ras.revenue_size,ras.received_money,ras.received_size,ras.less_money,ras.create_date,ras.create_name,ras.create_by,");
            sql.append(" ras.rmk from RAS_AUDIT_INFO_HIS ras where ras.pay_Type in ('001','006')) rai");
        }else {
            sql.append(" RAS_AUDIT_INFO rai");
        }
        sql.append(" left join ras_shop rs on rs.id=rai.shop_id left join sys_corp sc on rs.corp_Id=sc.id where rai.status=4");
        String transDate = (String)formdata.get("led_createDate");
        //手动拼接时间条件，时间戳类型
        if(StringUtils.isNotEmpty(transDate)){
            transDate +=" 23:59:59";
            formdata.put("led_createDate",transDate);
            dataTablesInput.setFormdata(formdata);
        }
        String createBy = (String)formdata.get("createBy");
        //手动拼接稽核人名称、登陆名称模糊查询
        if(StringUtils.isNotEmpty(createBy)){
            sql.append(" and (rai.create_By like '%"+createBy+"%' or rai.create_Name like '%"+createBy+"%')");
        }
        Map<String,Object> params=new HashMap<>();
        String shopAuthSql= SqlUtil.getInSqlByParams(shopAuthList, "rai.shop_Id","shopId", params);
        sql.append(" and ").append(shopAuthSql).append(" ");
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "rs.corp_Id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        return getDynamicBySQL(dataTablesInput,sql.toString() , RasDifferentDtlQueryVO.class, "", false,params);
    }

    /**
     * （营收）明细列表加载
     * @param dataTablesInput
     * @param id
     * @return
     */
    public DataTablesOutput getRevenueDifferentDataTablesUnion(DataTablesInput dataTablesInput, String id,String queryType) {
        if("notAudit".equals(queryType)){
            return rasRevenueDao.getRevenueNotDataTablesUnion(dataTablesInput, id.split(","));
        }else{
            return rasRevenueDao.getRevenueDifferentDataTablesUnion(dataTablesInput, id);
        }
    }

    /**
     * 跳转（营收）（实收）明细页面
     * @param id 稽核表id
     * @return
     */
    public RasAuditCommonInfoVO getMessage(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select BT_CONCAT4('(',sc.code,')',sc.name) as corpCodeAndName,rs.name as shopName,rai.pay_type as payType,");
        sql.append(" rai.create_date as lessDate from RAS_AUDIT_INFO rai");
        sql.append(" left join ras_shop rs on rs.id=rai.shop_id left join ");
        sql.append(" sys_corp sc on sc.id=rs.corp_id where rai.id=:id ");
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<RasAuditCommonInfoVO> list = this.listBySQLAliasToBean(sql.toString(), params, RasAuditCommonInfoVO.class);
        RasAuditCommonInfoVO rasAuditCommonInfoVO = null;
        if (list != null && !list.isEmpty()){
            rasAuditCommonInfoVO = list.get(0);
        }else{
            StringBuilder sb = new StringBuilder();
            sb.append(" select BT_CONCAT4('(',sc.code,')',sc.name) as corpCodeAndName,rs.name as shopName,rai.pay_type as payType,");
            sb.append(" rai.create_date as lessDate from RAS_AUDIT_INFO_HIS rai");
            sb.append(" left join ras_shop rs on rs.id=rai.shop_id left join ");
            sb.append(" sys_corp sc on sc.id=rs.corp_id where rai.id=:id ");
            rasAuditCommonInfoVO = getBySQLAliasToBean(sb.toString(), params, RasAuditCommonInfoVO.class);
        }
        return rasAuditCommonInfoVO;
    }

    /**
     * （实收）明细列表加载
     * @param dataTablesInput
     * @param id
     * @return
     */
    public DataTablesOutput getReceivedDifferentDataTablesUnion(DataTablesInput dataTablesInput, String id,String queryType) {
        if("notAudit".equals(queryType)){
            return rasReceivedDao.getReceivedNotDataTablesUnion(dataTablesInput, id.split(","));
        }else{
            return rasReceivedDao.getReceivedDifferentDataTablesUnion(dataTablesInput, id);
        }
    }

    /**
     * 已稽核统计页面列表加载
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getYesAuditDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuthList, List<String> shopAuthList) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rai.id,rai.audit_no as auditNo,rai.shop_id as shopId,sc.code as corpCode,sc.name as corpName,");
        sql.append("rs.name as shopName,rs.corp_id as corpId,rai.PAY_TYPE as payType,");
        sql.append("rai.revenue_money as nueOrVedMoney,rai.create_date " );
        sql.append("as auditDate,rai.create_name as createName,rai.create_by as createBy,rai.type as auditType,");
        sql.append("rai.revenue_size as revenueSize,rai.received_size as receivedSize from ");
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        if(RasConstant.RAS_STATUS_YES.equals(formdata.get("isHistory"))){
            sql.append("RAS_AUDIT_INFO_HIS rai ");
        }else{
            sql.append("RAS_AUDIT_INFO rai ");
        }
        sql.append(" left join ras_shop rs on rs.id = rai.shop_id left join sys_corp sc on rs.corp_id=sc.id where rai.status=3");
        String transDate = (String)formdata.get("led_auditDate");
        //手动拼接时间条件，时间戳类型
        if(StringUtils.isNotEmpty(transDate)){
            transDate +=" 23:59:59";
            formdata.put("led_auditDate",transDate);
            dataTablesInput.setFormdata(formdata);
        }
        String createBy = (String)formdata.get("createBy");
        //手动拼接稽核人名称、登陆名称模糊查询
        if(StringUtils.isNotEmpty(createBy)){
            sql.append(" and (rai.create_By like '%"+createBy+"%' or rai.create_Name like '%"+createBy+"%')");
        }
        Map<String,Object> params=new HashMap<>();
        String shopAuthSql= SqlUtil.getInSqlByParams(shopAuthList, "rai.shop_Id","shopId", params);
        sql.append(" and ").append(shopAuthSql).append(" ");
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "rs.corp_Id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), RasYesAuditQueryVO.class, "", false, params);
    }

    /**
     * 待稽核统计页面列表加载
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getNotAuditDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuthList, List<String> shopAuthList) {
        Map<String,Object> params=new HashMap<>();
        String shopAuthSql= SqlUtil.getInSqlByParams(shopAuthList, "r1.shop_id","shopId", params);
        StringBuilder sql = new StringBuilder();
        sql.append("select sc.id as corpId,rr2.shopId,sc.code as corpCode,sc.name as corpName,rs.name as shopName,rr2.payType,rr2.transDate,rr2.transDateStr,");
        sql.append("rr2.status,rr2.vedMoney,rr2.nueMoney from(select r1.shop_id as shopId,BT_TO_DATE(r1.str_trans_date, 'YYYY-MM-DD HH24:MI:SS') as");
        sql.append(" transDate,r1.str_trans_date as transDateStr,r1.pay_type as payType,r1.status,");
        sql.append("sum(r1.money1) vedMoney ,sum(r1.money2) nueMoney from(select r1.shop_id,r1.str_trans_date,r1.pay_type,r1.status,sum(r1.trans_money) money1,");
        sql.append("0 money2 from ras_received_dtl r1 where r1.status in (0,1) and ("+shopAuthSql+" or r1.shop_id is null) group by r1.shop_id,r1.str_trans_date,r1.pay_type,r1.status ");
        sql.append("union all select r1.shop_id,r1.str_trans_date,r1.pay_type,1 status,0 money1 ,sum(r1.money) money2 from ras_revenue_dtl r1 where r1.status in (1) ");
        sql.append("and "+shopAuthSql+" group by r1.shop_id,r1.str_trans_date,r1.pay_type) r1 group by r1.shop_id,r1.str_trans_date,r1.pay_type,r1.status)rr2 left join ras_shop rs ");
        sql.append("on rs.id=rr2.shopId left join sys_corp sc on rs.corp_id=sc.id where 1=1");
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "sc.id", params);
        sql.append(" and (").append(corpAuthSql).append("or sc.id is null)");

        Map<String, Object> formdata = dataTablesInput.getFormdata();
        String transDateStart = (String)formdata.get("transDateStart");
        String transDateEnd = (String)formdata.get("transDateEnd");
        //这里跟字符串字段比较速度跟快，跟data类型的字段比较会很慢
        sql.append(" and transDateStr>=:transDateStart and transDateStr<=:transDateEnd");
        params.put("transDateStart",transDateStart.replace("-",""));
        params.put("transDateEnd",transDateEnd.replace("-",""));

        return getDynamicBySQL(dataTablesInput, sql.toString(), RasShopNotAuditQueryVO.class, "rr2", false, params);
    }

    /**
     * 跳转（营收）（实收）明细页面
     * @param id 门店id
     * @return 稽核信息
     */
    public RasAuditCommonInfoVO getNotMessage(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select BT_CONCAT4('(',sc.code,')',sc.name) as corpCodeAndName,rs.name as shopName");
        sql.append(" from ras_shop rs left join sys_corp sc on sc.id=rs.corp_id where rs.id=:id");
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, RasAuditCommonInfoVO.class);
    }

    /**
     * 跳转数据明细页面营收数据
     * @param reveTab 营收表名，params 条件参数
     * @return 营收数据
     */
    public List<RasRevenueDtlVO> listRevenue(String reveTab,HashMap<String, Object> params){
        StringBuilder sql = new StringBuilder();
        //营收
        sql.append("select sc.name as corpName,rs.name as shopName,nue.pay_type as payType,nue.create_date as createDate,");
        sql.append("nue.SERIAL_NO as serialNo,nue.ORDER_NO as orderNo,nue.TRANS_DATE as transDate,nue.MONEY as money,");
        sql.append(" nue.CUST_NAME as custName,nue.CUST_ACC as custAcc,nue.RMK as rmk from "+reveTab+" nue ");
        sql.append(" left join ras_shop rs on rs.id=nue.shop_id left join ");
        sql.append("sys_corp sc on sc.id=rs.corp_id where nue.audit_id=:id");
        List<RasRevenueDtlVO> listRevenue = this.listBySQLAliasToBean(sql.toString(), params, RasRevenueDtlVO.class);
        return listRevenue;
    }
    /**
     * 跳转数据明细页面实收数据
     * receTab 实收表名，params 条件参数
     * @return 实收数据
     */
    public List<RasReceivedDtlVO> listReceived(String receTab,HashMap<String, Object> params){
        //实收
        StringBuilder sql = new StringBuilder();
        sql.append("select sc.name as corpName,rs.name as shopName,ved.pay_type as payType,ved.create_date as createDate,");
        sql.append("ved.SERIAL_NO as serialNo,ved.ORDER_NO as orderNo,ved.TRANS_DATE as transDate,ved.MONEY as money,ved.YH_MONEY as yhMoney,ved.TRANS_MONEY as transMoney,");
        sql.append("ved.DATE_SOURCE as dataSource,ved.CUST_NAME as custName,ved.CUST_ACC as custAcc,ved.PAYEE_ACC as payeeAcc,ved.PAYEE_ACC_NAME as payeeAccName,");
        sql.append("ved.PAYEE_BANK_NAME as payeeBankName,ved.RMK as rmk,ved.CLAIM_TYPE as clainType from "+receTab+" ved ");
        sql.append(" left join ras_shop rs on rs.id=ved.shop_id left join ");
        sql.append("sys_corp sc on sc.id=rs.corp_id where ved.audit_id=:id");
        List<RasReceivedDtlVO> listReceived = this.listBySQLAliasToBean(sql.toString(), params, RasReceivedDtlVO.class);
        return listReceived;
    }
    /**
     * 跳转数据明细页面稽核信息
     * receTab 实收表名，params 条件参数
     * @return 稽核信息
     */
    public RasAuditCommonInfoVO getAuditInfo(String auditTable,HashMap<String, Object> params){
        //实收
        StringBuilder sql = new StringBuilder();
        sql.append("select BT_CONCAT4('(',sc.code,')',sc.name) as corpCodeAndName,rs.name as shopName,rai.pay_type as payType,rai.create_date as createDate,");
        sql.append("rai.audit_No as auditNo,rai.revenue_Size as revenueSize,rai.received_Size as receivedSize,rai.type as type,");
        sql.append("rai.revenue_MONEY as revenueMoney,rai.received_Money as receivedMoney,rai.less_Money as lessMoney,");
        sql.append("rai.create_Name as createName,rai.RMK as rmk from "+auditTable+" rai ");
        sql.append(" left join ras_shop rs on rs.id=rai.shop_id left join ");
        sql.append(" sys_corp sc on sc.id=rs.corp_id where rai.id=:id");
        return getBySQLAliasToBean(sql.toString(), params, RasAuditCommonInfoVO.class);
    }

}
