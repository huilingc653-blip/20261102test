package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPoolLimitAllot;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotQueryVO;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @version V1.0
 * @Title: 额度分配业务Dao
 * @Description: 额度分配业务数据操作类
 * @author: zhenghj
 * @date: 2020-1-13
 */
@Repository
public class AbPoolLimitAllotDao extends BaseDao<AbPoolLimitAllot, String> {
    /**
     * 获取查询的sql语句
     *
     * @return
     */
    private StringBuilder getQuerySql() {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" APLA.ID AS id,APLA.Corp_Id as corpId,APLA.Bp_Id as bpId,APLA.Bustype_Id as bustypeId,")
                .append("APLA.Limt_Type as limtType,APLA.Pool_Money as poolMoney,APLA.Valid_Money as validMoney,")
                .append("APLA.Lock_Money as lockMoney,APLA.Apply_Money as applyMoney,APLA.End_Date as endDate,APLA.CREATE_TIME as createTime,")
                .append("APLA.Opt_Status as optStatus,APLA.Status as status,APLA.Rmk as rmk,AP.BP_NAME as bpName,")
                .append("APC.PARENT_ID as parentId,APC.Bustype_Level as bustypeLevel,APC.corp_code as corpCode,APC.corp_name as corpName,")
                .append("APB.BUSTYPE_NAME  as busTypeName,APC2.corp_name as parentName,apbe.re_msg as returnMsg ")
                .append(" FROM AB_POOL_LIMIT_ALLOT APLA ")
                .append(" LEFT JOIN AB_POOL AP ON AP.ID=APLA.BP_ID ")
                .append(" LEFT JOIN AB_POOL_CORP APC ON APC.CORP_ID=APLA.CORP_ID  and apc.valid_sign=1")
                // .append(" LEFT JOIN sys_corp s on s.id=APLA.Corp_Id ")
                .append(" LEFT JOIN AB_POOL_CORP APC2 ON APC2.id = APC.Parent_Id  ")
                // .append(" LEFT JOIN sys_corp s2 on s2.id = APC2.Corp_Id  ")
                .append(" LEFT JOIN AB_POOL_BUSTYPE APB ON APB.ID=APLA.BUSTYPE_ID ")
                .append(" LEFT JOIN ab_pool_bank_exc apbe ON APLA.ID=apbe.id ");
        return sql;
    }
    /**
     * 额度分配列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE APLA.status<>-2 ");
        sql.append(" and APLA.create_id=:userId ");//操作限制：只能由变更人处理
        Map<String, Object> params = new HashMap<>();
        params.put("userId", input.getCurrentUser().getId());
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "APLA.Corp_Id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        Map<String, Object> formdata = input.getFormdata();
        String createTime = (String) formdata.get("led_createTime");
        //手动拼接时间条件，时间戳类型
        if (StringUtils.isNotEmpty(createTime)) {
            createTime += " 23:59:59";
            formdata.put("led_createTime", createTime);
            input.setFormdata(formdata);
        }
        return getDynamicBySQL(input, sql.toString(), AbPoolLimitAllotVO.class, "", false, params);
    }
    /**
     * 额度调剂列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbPoolLimitAllotQueryVO> getQueryDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
        //查询同步信息标识
        String bankSynInfo = String.valueOf(input.getFormdata().get("bankSynInfo"));
        StringBuilder sql = new StringBuilder("select ALIAS.* from (");
        sql.append(" select APLA.ID AS id,APLA.Corp_Id as corpId,APLA.Bp_Id as bpId,APLA.Bustype_Id as bustypeId,APLA.Limt_Type as limtType,APLA.Apply_Money as applyMoney,");
        if ("1".equals(bankSynInfo)) {
            sql.append("APLA.OLD_Money as poolMoney,null as validMoney,null as lockMoney,")
                    .append("(case when APLA.Apply_Money > 0 then APLA.Apply_Money else 0 end) as downAmt,(case when APLA.Apply_Money < 0 then -1*APLA.Apply_Money else 0 end) as overAmt,(case when APLA.Apply_Money <0 then '1' else '2' end) as adjustType,APLA.Pool_Money as finalMoney,");
        } else {
            sql.append("APLA.Pool_Money as poolMoney,APLA.Valid_Money as validMoney,APLA.Opt_Status as optStatus,APLA.Status as status,APLA.Lock_Money as lockMoney,")
                    .append("(case when APLA.Apply_Money > 0 then APLA.Apply_Money else 0 end) as downAmt,(case when APLA.Apply_Money < 0 then -1*APLA.Apply_Money else 0 end) as overAmt,(case when APLA.Apply_Money <0 then '1' else '2' end) as adjustType,APLA.Pool_Money+APLA.Apply_Money as finalMoney,");
//                .append("(case when APLA.Apply_Money > APLA.Pool_Money then APLA.Apply_Money - APLA.Pool_Money else 0 end) as downAmt,(case when APLA.Apply_Money < APLA.Pool_Money then APLA.Pool_Money-APLA.Apply_Money else 0 end) as overAmt,(case when APLA.Apply_Money < APLA.Pool_Money then '1' else '2' end) as adjustType,")
        }
        sql.append("APLA.Rmk as rmk,AP.BP_NAME as bpName,APLA.End_Date as endDate,APLA.CREATE_TIME as createTime,")
                //.append("APC.PARENT_ID as parentId,APC.Bustype_Level as bustypeLevel,s.code as corpCode,s.name as corpName,")
                .append("APC.PARENT_ID as parentId,APC.Bustype_Level as bustypeLevel,APC.corp_code as corpCode,APC.corp_name as corpName,")
                //.append("APB.BUSTYPE_NAME  as busTypeName,s2.name as parentName,apbe.re_msg as returnMsg ");
                .append("APB.BUSTYPE_NAME  as busTypeName,APC2.corp_name as parentName,apbe.re_msg as returnMsg ");
        if ("1".equals(bankSynInfo)) {
            sql.append(" FROM AB_POOL_LIMIT_DTL APLA ");
        } else {
            sql.append(" FROM AB_POOL_LIMIT_ALLOT APLA ");
        }
        sql.append(" LEFT JOIN AB_POOL AP ON AP.ID=APLA.BP_ID ")
                .append(" LEFT JOIN AB_POOL_CORP APC ON APC.CORP_ID=APLA.CORP_ID  and apc.valid_sign=1")
                //.append(" LEFT JOIN sys_corp s on s.id=APLA.Corp_Id ")
                .append(" LEFT JOIN AB_POOL_CORP APC2 ON APC2.id = APC.Parent_Id  ")
                //.append(" LEFT JOIN sys_corp s2 on s2.id = APC2.Corp_Id  ")
                .append(" LEFT JOIN AB_POOL_BUSTYPE APB ON APB.ID=APLA.BUSTYPE_ID ")
                .append(" LEFT JOIN ab_pool_bank_exc apbe ON APLA.ID=apbe.id ");
        Map<String, Object> params = new HashMap<>();
        if (!input.getFormdata().containsKey("isQuery")) {
            sql.append(" WHERE APLA.status<>-2 ");
            sql.append(" and APLA.create_id=:userId ");//操作限制：只能由变更人处理
            params.put("userId", input.getCurrentUser().getId());
        } else if (!"1".equals(bankSynInfo)) {
            sql.append(" WHERE APLA.status=95 and APLA.opt_status='19' ");
        }
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "APLA.Corp_Id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        sql.append(") ALIAS where 1=1 ");
        Map<String, Object> formdata = input.getFormdata();
        return getDynamicBySQL(input, sql.toString(), AbPoolLimitAllotQueryVO.class, "", false, params);
    }
    /**
     * 审批页面查询当前操作员可审批的额度分配数据
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();
        String businessSql = SqlUtil.getInSqlByParams(businessList, "APLA.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return getDynamicBySQL(input, sql.toString(), AbPoolLimitAllotVO.class, "", false, params);
    }
    /**
     * 根据分配Id查询额度分配信息
     *
     * @param aid 额度分配id
     * @return
     */
    public List<AbPoolLimitAllotVO> getLimitAllotVoById(String aid) {
        StringBuilder sql = new StringBuilder(getQuerySql());
        sql.append(" WHERE APLA.id = :aid  ");
        Map<String, Object> params = new HashMap<>();
        params.put("aid", aid);
        return this.listBySQLAliasToBean(sql.toString(), params, AbPoolLimitAllotVO.class);
    }
    /**
     * 根据单位id获取此单位下有没有处理中的数据
     *
     * @param corpId
     * @return 有效额度分配
     */
    public int getPoolLimitAllotByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder("select count(*) from AB_POOL_LIMIT_ALLOT where corp_id=:corpId ")
                .append(" and status>:status1 and (status<:status2 or opt_status in (:optStatus))");
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        params.put("status1", AbPoolConstant.AB_POOL_STATUS_2);
        params.put("status2", AbPoolConstant.AB_POOL_STATUS95);
        params.put("optStatus", new String[]{
                AbPoolConstant.AB_POOL_OPT_STATUS16,
                AbPoolConstant.AB_POOL_OPT_STATUS18});
        return ((Number) this.createSQLQuery(sql.toString(), params).uniqueResult()).intValue();
    }
    /**
     * 根据单位id获取此单位中是否存在额度
     *
     * @param corpId
     * @return 有效用户笔数
     */
    public int getValidLimitAllotNumByCorpId(String corpId) {
        String sql = "select count(*) from AB_POOL_LIMIT_ALLOT where 1=1  and corp_id=:corpId";
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }
}
