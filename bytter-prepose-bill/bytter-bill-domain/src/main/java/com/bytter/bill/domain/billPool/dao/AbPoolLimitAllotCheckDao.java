package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPoolLimitAllotCheck;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotCheckVO;
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
 * @Title: 额度分配核准Dao
 * @Description: 额度分配核准业务数据操作类
 * @author: zhenghj
 * @date: 2020-2-24
 */
@Repository
public class AbPoolLimitAllotCheckDao extends BaseDao<AbPoolLimitAllotCheck, String> {
    /**
     * 获取查询的sql语句
     *
     * @return
     */
    private StringBuilder getQuerySql(boolean isApprove) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" APLAC.ID AS id,APLAC.Corp_Id as corpId,APLAC.Bp_Id as bpId,APLAC.Bustype_Id as bustypeId,")
                .append("APLAC.Limt_Type as limtType,APLAC.Pool_Money as poolMoney,APLAC.Valid_Money as validMoney,")
                .append("APLAC.Lock_Money as lockMoney,APLAC.Apply_Money as applyMoney,APLAC.End_Date as endDate,APLAC.CREATE_TIME as createTime,")
                .append("APLAC.Opt_Status as optStatus,APLAC.Status as status,APLAC.Rmk as rmk,AP.BP_NAME as bpName,")
                .append("APC.PARENT_ID as parentId,APC.Bustype_Level as bustypeLevel,APC.corp_code as corpCode,APC.corp_name as corpName,")
                .append("APB.BUSTYPE_NAME  as busTypeName,APLAC.CHECK_TYPE as checkType,APLAC.CHECK_MSG as checkMsg, ")
                .append("APLAC.RETURN_CODE as returnCode,APC2.corp_name as parentName,apbe.re_msg as returnMsg  ");
        if (isApprove) {
            sql.append(",APLAC.ID as businessKey ");
        }
        sql.append(" FROM AB_POOL_LIMIT_ALLOTCHECK APLAC ")
                .append(" LEFT JOIN AB_POOL AP ON AP.ID=APLAC.BP_ID ")
                .append(" LEFT JOIN AB_POOL_CORP APC ON APC.CORP_ID=APLAC.CORP_ID  and APC.valid_sign=1 ")
                // .append(" LEFT JOIN sys_corp s on s.id=APLAC.Corp_Id ")
                .append(" LEFT JOIN AB_POOL_CORP APC2 ON APC2.id = APC.Parent_Id  ")
                // .append(" LEFT JOIN sys_corp s2 on s2.id = APC2.Corp_Id  ")
                .append(" LEFT JOIN AB_POOL_BUSTYPE APB ON APB.ID=APLAC.BUSTYPE_ID ")
                .append(" LEFT JOIN ab_pool_bank_exc apbe ON APLAC.ID=apbe.id ");
        return sql;
    }
    /**
     * 额度分配核准列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbPoolLimitAllotCheckVO> getDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder(getQuerySql(false));
        sql.append(" WHERE (APLAC.status<>-2 or APLAC.status is null)  ");
        Map<String, Object> params = new HashMap<>();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "APLAC.Corp_Id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        Map<String, Object> formdata = input.getFormdata();
        String createTime = (String) formdata.get("led_createTime");
        //手动拼接分配日期条件，时间戳类型
        if (StringUtils.isNotEmpty(createTime)) {
            createTime += " 23:59:59";
            formdata.put("led_createTime", createTime);
            input.setFormdata(formdata);
        }
        return getDynamicBySQL(input, sql.toString(), AbPoolLimitAllotCheckVO.class, "", false, params);
    }
    /**
     * 审批页面查询当前操作员可审批的额度分配核准数据
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
        StringBuilder sql = new StringBuilder(getQuerySql(true));
        sql.append(" WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();
        String businessSql = SqlUtil.getInSqlByParams(businessList, "APLAC.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return getDynamicBySQL(input, sql.toString(), AbPoolLimitAllotCheckVO.class, "", false, params);
    }
    /**
     * 根据分配核准Id查询额度分配核准信息
     *
     * @param aid 额度分分配核准id
     * @return
     */
    public List<AbPoolLimitAllotCheckVO> getLimitAllotCheckVoByAid(String aid) {
        StringBuilder sql = new StringBuilder(getQuerySql(false));
        sql.append(" WHERE APLAC.id = :aid  ");
        Map<String, Object> params = new HashMap<>();
        params.put("aid", aid);
        return this.listBySQLAliasToBean(sql.toString(), params, AbPoolLimitAllotCheckVO.class);
    }
    /**
     * 根据单位id获取此单位下有没有处理中的数据
     * @param corpId
     * @return 有效额度分配核准
     */
    public int getPoolLimitAllotCheckByCorpId(String corpId){
        StringBuilder sql  = new StringBuilder("select count(*) from AB_POOL_LIMIT_ALLOTCHECK where corp_id=:corpId ")
                .append(" and status>:status1 and (status<:status2 or opt_status in (:optStatus))");
        Map<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        params.put("status1",AbPoolConstant.AB_POOL_STATUS_2);
        params.put("status2",AbPoolConstant.AB_POOL_STATUS95);
        params.put("optStatus",new String[]{
                AbPoolConstant.AB_POOL_OPT_STATUS16,
                AbPoolConstant.AB_POOL_OPT_STATUS18});
        return ((Number)this.createSQLQuery(sql.toString(),params).uniqueResult()).intValue();
    }
}
