package com.bytter.ras.dao;

import com.bytter.ras.entity.RasAuditInfo;
import com.bytter.ras.vo.RasAuditBackVO;
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
 * @author zhenghj
 * @version 1.0
 * @Title: dao
 * @Description: 稽核数据库操作类
 * @date 2019-05-16
 */
@Repository
public class RasAuditDao extends BaseDao<RasAuditInfo, String> {

    /**
     * 营收反稽核数据列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList, List<String> shopAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("a.id,a.pay_type as payType,a.shop_id as shopId,s.name as shopName,s.code as shopCode,");
        sql.append("a.audit_no as auditNo,a.type,a.revenue_size as revenueSize,a.revenue_money as revenueMoney,");
        sql.append("a.received_money as receivedMoney,a.less_money as lessMoney,a.status,a.rmk,a.create_name as createName,");
        sql.append("a.create_by as createBy,a.create_date as createDate,c.id as corpId,c.name as corpName,c.code as corpCode");
        sql.append(" from ras_audit_info a   ");
        sql.append(" left join ras_shop s on a.shop_id = s.id ");
        sql.append(" left join sys_corp c on s.corp_id = c.id ");
        sql.append(" WHERE a.status in (:status)  ");
        Map<String, Object> params = new HashMap<>();
        params.put("status", new Integer[]{RasConstant.RAS_AUDIT_STATUS_3, RasConstant.RAS_AUDIT_STATUS_4});
        String shopAuthSql = SqlUtil.getInSqlByParams(shopAuthList, "s.id", "shopId", params);
        sql.append(" and ").append(shopAuthSql).append(" ");
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        //稽核人支持用户代码和用户名称like查询
        if (StringUtils.isNotEmpty(input.getFormdata().get("likes_createName"))) {
            sql.append(" and (a.create_name like :name or a.create_by like :name) ");
            params.put("name", "%" + input.getFormdata().get("likes_createName") + "%");
            input.getFormdata().remove("likes_createName");
        }
        Map<String, Object> formdata = input.getFormdata();
        String createDate = (String) formdata.get("led_createDate");
        //手动拼接时间条件，时间戳类型
        if (StringUtils.isNotEmpty(createDate)) {
            createDate += " 23:59:59";
            formdata.put("led_createDate", createDate);
            input.setFormdata(formdata);
        }
        return getDynamicBySQL(input, sql.toString(), RasAuditBackVO.class, "", false, params);
    }

    /**
     * 根据参数条件查询营收反稽核数据
     *
     * @param rasAuditBackVO
     * @return
     */
    public List<RasAuditBackVO> getBackAuditList(RasAuditBackVO rasAuditBackVO, List<String> corpAuthList, List<String> shopAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("a.id,a.pay_type as payType,a.shop_id as shopId,s.name as shopName,s.code as shopCode,");
        sql.append("a.audit_no as auditNo,a.type,a.revenue_size as revenueSize,a.revenue_money as revenueMoney,");
        sql.append("a.received_money as receivedMoney,a.less_money as lessMoney,a.status,a.rmk,a.create_name as createName,");
        sql.append("a.create_by as createBy,a.create_date as createDate,c.id as corpId,c.name as corpName,c.code as corpCode");
        sql.append(" from ras_audit_info a   ");
        sql.append(" left join ras_shop s on a.shop_id = s.id ");
        sql.append(" left join sys_corp c on s.corp_id = c.id ");
        sql.append(" WHERE a.status in (:status)  ");
        Map<String, Object> params = new HashMap<>();
        params.put("status", new Integer[]{RasConstant.RAS_AUDIT_STATUS_3, RasConstant.RAS_AUDIT_STATUS_4});
        if (StringUtils.isNotEmpty(rasAuditBackVO.getCorpId())) {
            sql.append(" and c.id = :corpId ");
            params.put("corpId",rasAuditBackVO.getCorpId());
        }else{
            String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.id", params);
            sql.append(" and ").append(corpAuthSql).append(" ");
        }
        if (StringUtils.isNotEmpty(rasAuditBackVO.getShopId())) {
            sql.append(" AND s.id = :shopId ");
            params.put("shopId",rasAuditBackVO.getShopId());
        }else{
            String shopAuthSql = SqlUtil.getInSqlByParams(shopAuthList, "s.id", "shopId", params);
            sql.append(" and ").append(shopAuthSql).append(" ");
        }
        //缴款方式
        if (StringUtils.isNotEmpty(rasAuditBackVO.getPayType())) {
            sql.append(" and a.pay_type=:payType ");
            params.put("payType",rasAuditBackVO.getPayType());
        }
        //稽核码
        if (StringUtils.isNotEmpty(rasAuditBackVO.getAuditNo())) {
            sql.append(" and a.audit_No like :auditNo ");
            params.put("auditNo","%"+rasAuditBackVO.getAuditNo()+"%");
        }

        //稽核人支持用户代码和用户名称like查询
        if (StringUtils.isNotEmpty(rasAuditBackVO.getCreateName())) {
            sql.append(" and (a.create_name like :name or a.create_by like :name) ");
            params.put("name", "%" + rasAuditBackVO.getCreateName() + "%");
        }

        //差异金额
        if (StringUtils.isNotEmpty(rasAuditBackVO.getLessMoney())) {
            sql.append(" and a.LESS_MONEY >= :lessMoney ");
            params.put("lessMoney",rasAuditBackVO.getLessMoney());
        }
        if (StringUtils.isNotEmpty(rasAuditBackVO.getLessMoneyEnd())) {
            sql.append(" and a.LESS_MONEY <= :lessMoneyEnd ");
            params.put("lessMoneyEnd",rasAuditBackVO.getLessMoneyEnd());
        }

        //稽核日期
        if (StringUtils.isNotEmpty(rasAuditBackVO.getCreateDateStart())) {
            sql.append(" and a.CREATE_DATE >= :createDateStart ");
            params.put("createDateStart",rasAuditBackVO.getCreateDateStart());
        }
        if (StringUtils.isNotEmpty(rasAuditBackVO.getCreateDateEnd())) {
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd" );
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS" );
            String dateEnd = dateFormat1.format(rasAuditBackVO.getCreateDateEnd())+" 23:59:59 999";
            Date createDateEnd = null;
            try {
                createDateEnd = dateFormat2.parse(dateEnd);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            sql.append(" and a.CREATE_DATE <= :createDateEnd ");
            params.put("createDateEnd",createDateEnd);
        }
        return this.listBySQLAliasToBean(sql.toString(), 0, RasConstant.RAS_AUTOT_MAXRESULT, params, RasAuditBackVO.class);
//        return  this.listBySQLAliasToBean(sql.toString(), params, RasAuditBackVO.class);
    }

    /**
     * 根据ids批量修改稽核信息表数据状态
     *
     * @param params
     * @return
     */
    public int batchUpdate(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update RasAuditInfo r"
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

        hql.append(" where  ");
        String[] idsStr = (String[])params.get("ids");
        String ids =SqlUtil.getInSql(idsStr, "r.id", true);
        hql.append(ids);
        if (params.containsKey("statusOld")) {
            hql.append(" and status in (:statusOld) ");
        }
        if (params.containsKey("statusNew")) {
            hql.append(" and status!=:statusNew ");
        }
        return this.batchExecute(hql.toString(), params);
    }

    
    /**
     * 根据参数获取稽核第一条数据
     *
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
	public RasAuditInfo getRasAuditInfoOneByParams(Map<String, Object> params) {
    	StringBuilder hql = new StringBuilder("from RasAuditInfo where 1=1 ");
    	if (params.containsKey("id")) {
    		hql.append(" and id=:id ");
    	}
    	if (params.containsKey("wShopId")) {
    		hql.append(" and shopId=:wShopId ");
    	}
    	if (params.containsKey("wPayType")) {
    		hql.append(" and payType=:wPayType ");
    	}
    	if (params.containsKey("statusArray")) {
    		hql.append(" and status in(:statusArray) ");
    	}
    	if (params.containsKey("startDate")) {
    		hql.append(" and createDate >= :startDate ");
    	}
    	if ("1".equals(params.get(RasConstant.RAS_AUTOTASK_OPKEY))) {
    		hql.append(" order by createDate desc ");
    	}
    	RasAuditInfo info=null; 
    	List<RasAuditInfo> list=this.listByHQL(hql.toString(), 0, 1, params);
    	if(list!=null&&list.size()>0){
    		info=list.get(0);
    	}
    	return info;
    }

    /**
     * 根据参数获取稽核数据
     *
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RasAuditInfo> getRasAuditInfoByParams(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("from RasAuditInfo where 1=1 ");
        if (params.containsKey("id")) {
            hql.append(" and id=:id ");
        }
        if (params.containsKey("wShopId")) {
            hql.append(" and shopId=:wShopId ");
        }
        if (params.containsKey("wPayType")) {
            hql.append(" and payType=:wPayType ");
        }
        if (params.containsKey("statusArray")) {
            hql.append(" and status in(:statusArray) ");
        }
        if (params.containsKey("startDate")) {
            hql.append(" and createDate >= :startDate ");
        }
        if ("1".equals(params.get(RasConstant.RAS_AUTOTASK_OPKEY))) {
            hql.append(" order by createDate desc ");
        }
        return this.listByHQL(hql.toString(), params);
    }


    /**
     * 根据规则进行营收和实收数据匹配
     *
     * @param params    传递查询参数
     * @param rulesWsql 稽核关联条件
     * @return
     */
    public List<RasAuditInfo> getRasAuditInfo(Map<String, Object> params, String rulesWsql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ")
                .append(RasConstant.RAS_AUTOT_REVENUE_ALIAS)
                .append(".id as revenueId,")
//                .append(RasConstant.RAS_AUTOT_REVENUE_ALIAS)
//                .append(".pay_type as paytype,")
//                .append(RasConstant.RAS_AUTOT_REVENUE_ALIAS)
//                .append(".shop_id as shopid,")
                .append(RasConstant.RAS_AUTOT_REVENUE_ALIAS)
                .append(".money as revenueMoney,")
                .append(RasConstant.RAS_AUTOT_RECEIVED_ALIAS)
                .append(".id as receivedId from ");
        sql.append(" RAS_REVENUE_DTL ").append(RasConstant.RAS_AUTOT_REVENUE_ALIAS);
        sql.append(" inner join RAS_RECEIVED_DTL ").append(RasConstant.RAS_AUTOT_RECEIVED_ALIAS)
        .append(" on ")
        .append(RasConstant.RAS_AUTOT_REVENUE_ALIAS)
        .append(".shop_id=")
        .append(RasConstant.RAS_AUTOT_RECEIVED_ALIAS)
        .append(".shop_id ")
        .append(" and ")
    	.append(RasConstant.RAS_AUTOT_REVENUE_ALIAS)
    	.append(".pay_type=")
    	.append(RasConstant.RAS_AUTOT_RECEIVED_ALIAS)
    	.append(".pay_type ")
    	.append(" and ")
    	.append(rulesWsql);
        sql.append("where 1=1 ");
        if (params.containsKey("wPayType")) {
        	sql.append(" and ")
	        	.append(RasConstant.RAS_AUTOT_RECEIVED_ALIAS)
	        	.append(".pay_type=:wPayType ");
        }
        if (params.containsKey("wPayTypes")) {
            sql.append(" and ")
                .append(RasConstant.RAS_AUTOT_RECEIVED_ALIAS)
                .append(".pay_type in (:wPayTypes) ");
        }
        if (params.containsKey("wShopId")) {
        	sql.append(" and ")
	        	.append(RasConstant.RAS_AUTOT_RECEIVED_ALIAS)
	        	.append(".shop_id=:wShopId ");
        }
        if (params.containsKey("wShopIds")) {
        	sql.append(" and ")
	        	.append(RasConstant.RAS_AUTOT_RECEIVED_ALIAS)
	        	.append(".shop_id in (:wShopIds) ");
        }
        if (params.containsKey("wUnid")) {
            sql.append(" and ")
                    .append(RasConstant.RAS_AUTOT_REVENUE_ALIAS)
                    .append(".un_id='"+String.valueOf(params.get("wUnid"))).append("' ")
                    .append(" and ")
                    .append(RasConstant.RAS_AUTOT_RECEIVED_ALIAS)
                    .append(".un_id='"+String.valueOf(params.get("wUnid"))).append("' ");
        }
        if (params.containsKey("wAuditStatus")) {
            sql.append(" and ")
                    .append(RasConstant.RAS_AUTOT_REVENUE_ALIAS)
                    .append(".status=:wAuditStatus ")
                    .append(" and ")
                    .append(RasConstant.RAS_AUTOT_RECEIVED_ALIAS)
                    .append(".status=:wAuditStatus ");
        }
        sql.append(" order by ")
                .append(RasConstant.RAS_AUTOT_REVENUE_ALIAS)
                .append(".")
                .append(RasConstant.RAS_AUTOT_REVENUE_FIELD_TRANSDATE)
                .append(",")
                .append(RasConstant.RAS_AUTOT_REVENUE_ALIAS)
                .append(".id,")
                .append(RasConstant.RAS_AUTOT_RECEIVED_ALIAS)
                .append(".id ");
        this.logger.info("============sql=================:"+sql.toString());
        return this.listBySQLAliasToBean(sql.toString(), 0, RasConstant.RAS_AUTOT_MAXRESULT, params, RasAuditInfo.class);
    }
    /**
     * 定时任务将稽核中间表作废、历史的数据迁移
     *
     */
    public void auditHisDataMove(int hisDay) {
        StringBuilder sql = new StringBuilder(" insert into RAS_AUDIT_INFO_HIS(ID,PAY_TYPE,SHOP_ID, ");
        sql.append(" AUDIT_NO,TYPE,REVENUE_SIZE,RECEIVED_SIZE,REVENUE_MONEY,RECEIVED_MONEY,LESS_MONEY, ");
        sql.append(" END_CHECK_MONEY,IS_MSG,STATUS,RULES,RMK,CREATE_NAME,CREATE_BY,CREATE_DATE, ");
        sql.append(" UPDATE_NAME,UPDATE_BY,UPDATE_DATE) ");
        sql.append(" select aud.ID,aud.PAY_TYPE,aud.SHOP_ID,aud.AUDIT_NO,aud.TYPE,aud.REVENUE_SIZE,aud.RECEIVED_SIZE,aud.REVENUE_MONEY, ");
        sql.append(" aud.RECEIVED_MONEY,aud.LESS_MONEY,aud.END_CHECK_MONEY,aud.IS_MSG,aud.STATUS,aud.RULES,aud.RMK,aud.CREATE_NAME, ");
        sql.append(" aud.CREATE_BY,aud.CREATE_DATE,aud.UPDATE_NAME,aud.UPDATE_BY,aud.UPDATE_DATE from RAS_AUDIT_INFO aud");
        sql.append("  where aud.CREATE_DATE < :hisDate and aud.status = 5 union all ");
        sql.append(" select aud.ID,aud.PAY_TYPE,aud.SHOP_ID,aud.AUDIT_NO,aud.TYPE,aud.REVENUE_SIZE,aud.RECEIVED_SIZE,aud.REVENUE_MONEY, ");
        sql.append(" aud.RECEIVED_MONEY,aud.LESS_MONEY,aud.END_CHECK_MONEY,aud.IS_MSG,aud.STATUS,aud.RULES,aud.RMK,aud.CREATE_NAME, ");
        sql.append(" aud.CREATE_BY,aud.CREATE_DATE,aud.UPDATE_NAME,aud.UPDATE_BY,aud.UPDATE_DATE from RAS_AUDIT_INFO aud");
        sql.append("  where aud.CREATE_DATE < :hisDate and aud.status in (3,4) and aud.PAY_TYPE in ('001','006')");
        Map<String, Object> parms = new HashMap<String, Object>();
        Date hisDate = DateUtils.addDay(new Date(),hisDay*-1);
        parms.put("hisDate",hisDate);
        this.createSQLQuery(sql.toString(),parms).executeUpdate();
    }
    /**
     * 将迁移后的稽核数据删除
     *
     */
    public void delAuditData(int hisDay){
        String sql="delete from RAS_AUDIT_INFO where CREATE_DATE < :hisDate and (status = 5 or (status in (3,4) and PAY_TYPE in ('001','006')))";
        Map<String, Object> parms = new HashMap<String, Object>();
        Date hisDate = DateUtils.addDay(new Date(),hisDay*-1);
        parms.put("hisDate",hisDate);
        this.createSQLQuery(sql,parms).executeUpdate();
    }
    
    /**
     * 通过稽核中间表写入稽核表
     */
    public void saveAuditInfoByAuditInter(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder(" insert into RAS_AUDIT_INFO( ");
        sql.append(" ID,PAY_TYPE,SHOP_ID, ");
        sql.append(" AUDIT_NO,TYPE,REVENUE_SIZE,RECEIVED_SIZE,REVENUE_MONEY,RECEIVED_MONEY,LESS_MONEY, ");
        sql.append(" STATUS,RULES,CREATE_NAME,CREATE_BY,CREATE_DATE) ");
        sql.append(" select aud.ID,aud.PAY_TYPE,aud.SHOP_ID,");
        sql.append(" aud.AUDIT_NO,:type,:size,:size,aud.MONEY,aud.MONEY,:lessMoney,");
        sql.append(" :status,aud.RULES,:createName,:createBy,aud.CREATE_DATE ");
        sql.append("  from RAS_AUDIT_INTER aud ");
        sql.append("  where aud.UN_ID=:wUnid" );
        this.createSQLQuery(sql.toString(),params).executeUpdate();
    }
}




















