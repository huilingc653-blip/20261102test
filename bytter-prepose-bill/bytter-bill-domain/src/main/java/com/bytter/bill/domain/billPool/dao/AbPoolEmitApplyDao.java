package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitApplyVO;
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
 * @Title: AbPoolEmitApplyDao
 * @Description: 出票申请DAO
 * @author wangzy
 * @date 2020/2/26 13:45
 *
 */
@Repository
public class AbPoolEmitApplyDao  extends BaseDao<AbPoolDtl, String> {
    /**
     * 通用查询数据sql
     *
     * @return
     */
    public StringBuilder getCommonSql(StringBuilder sql) {
        sql.append(" BT_CONCAT4('(',apc.corp_code,')',apc.corp_name) as corpCodeAndName,dtl.id as dtlId,v.bill_Type as billType, v.bill_Flag as billFlag, ");
        sql.append(" v.bill_Money as billMoney,v.banendrsmtmk as banendrsmtmk,v.start_Date as startDate,v.end_Date as endDate,v.payer as payer, ");
        sql.append(" v.PAYERACC as payeracc, v.acceptor as acceptor,BT_CONCAT4('(',p.BP_CODE,')',p.BP_NAME) as pbCodeAndName,v.payee as payee, ");
        sql.append(" vv.corp_Id as corpId,dtl.APP_NO as appNo,a.id as aid,v.PAYER_BANK_NAME as payerBankName,v.PAYERBANKNO as payerbankno, ");
        sql.append(" (case dtl.brief_Id when '"+AbPoolConstant.AB_POOL_BRIEF_001+"' then dtl.STATUS else 0 end) as status, ");
        sql.append(" (case dtl.brief_Id when '"+AbPoolConstant.AB_POOL_BRIEF_001+"' then dtl.OPT_STATUS else null end) as optStatus, ");
        sql.append(" vv.abs as abs,v.payeeacc as payeeacc,v.ACCEPTOR_BANK_NAME as acceptorBankName,dtl.bp_Id as bpId,v.orig_No as origNo, ");
        sql.append(" v.PAYEE_BANK_NAME as payeeBankName,v.PAYEEBANKNO as payeebankno,v.ACCEPTORACC as acceptoracc,v.ACCEPTORBANKNO as acceptorbankno ");
        sql.append(" from ab_pool_dtl dtl left join AB_VOUCHERINFO_VICE vv on vv.id=dtl.vv_id left join AB_VOUCHERINFO v on v.id=vv.vid ");
        sql.append(" left join ab_accrecords a on a.PD_ID=dtl.id left join ab_pool p on p.id=dtl.bp_id  ");
        sql.append(" left join ab_pool_corp apc on vv.corp_id = apc.corp_id  and APC.valid_sign=1");
        //sql.append(" left join sys_corp s on s.id=apc.corp_id  where 1=1 ");
        sql.append(" where 1=1 ");
        return sql;
    }
    /**
     * 出票申请列表查询
     * @param input
     * @return 列表信息
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        String etime = (String)input.getFormdata().get("led_startDate");
        if(StringUtils.isNotBlank(etime)){
            etime = etime + " 23:59:59";
            input.getFormdata().put("led_startDate", etime);
        }
        StringBuilder sql = new StringBuilder(" select * from ( select ");
        this.getCommonSql(sql);
        sql.append(" and vv.VALID_SIGN=:validSign and dtl.valid_Sign=:validSign");
        sql.append(" and (dtl.brief_Id=:briefId or (dtl.brief_Id=:briefId2 and dtl.opt_status=:optStatus))");
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "apc.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ) emit ");
        params.put("briefId", AbPoolConstant.AB_POOL_BRIEF_001);
        params.put("briefId2", AbPoolConstant.AB_POOL_BRIEF_8001);
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        params.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS19);
        return getDynamicBySQL(input, sql.toString(), AbPoolEmitApplyVO.class, "", true, params);
    }
    /**
     * 加载审批列表数据
     *
     * @param dataTablesInput 查询条件
     * @param businessList 审批中流水
     * @return 返回列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listApproveDatatable(DataTablesInput dataTablesInput, List<String> businessList) {
        // 设置结束时间
        String etime = (String)dataTablesInput.getFormdata().get("led_startDate");
        if(StringUtils.isNotBlank(etime)){
            etime = etime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_startDate", etime);
        }
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder(" select * from (select a.id as businessKey, ");
        this.getCommonSql(sql);
        String businessSql = SqlUtil.getInSqlByParams(businessList, "a.id", params);
        sql.append(" and ").append(businessSql).append(" ) emit");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolEmitApplyVO.class, "", true,params);
    }
    /**
     * 出票申请信息查询
     * @param dtlId 明细表ID
     * @return
     *
     */
    @SuppressWarnings("rawtypes")
    public AbPoolEmitApplyVO getAbPoolEmitApplyVOInfo(String dtlId) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(" select * from (select ");
        this.getCommonSql(sql);
        sql.append(" and vv.VALID_SIGN=:validSign and dtl.valid_Sign=:validSign");
        sql.append(" and dtl.id=:dtlId ) emit");
        params.put("dtlId", dtlId);
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        return getBySQLAliasToBean(sql.toString(), params, AbPoolEmitApplyVO.class);
    }
    /**
     * 根据单位id获取此单位中是否存在入票据池业务流程流水中
     * @param corpId
     * @return 存在笔数
     */
    public int getValidPoolDtlNumByCorpId(String corpId,Object briefId){
        Map<String,Object> params=new HashMap<String,Object>();
        StringBuilder sql  = new StringBuilder()
                .append(" select count(*) from ab_pool_dtl apd")
                .append(" left join ab_voucherinfo_vice vv on vv.id=apd.vv_id ")
                .append(" where vv.corp_id=:corpId and apd.VALID_SIGN<:validSign and ");
        if (AbPoolConstant.AB_POOL_BRIEF_8019.equals(briefId)){
            // 只有出池成功的单位才能禁用
            sql.append(" ((apd.brief_id in (:briefId) and vv.POOL_STATUS is not null and vv.POOL_STATUS!=:poolStatus ) ")
                    .append(" or (apd.BRIEF_ID in (:briefId2) and apd.status=:status2 and apd.opt_status=:optStatus2 and vv.POOL_STATUS=:poolStatus2)) ");
            params.put("briefId2", new String[]{
                    AccebillConstant.AB_POOL_BRIEF_8018,
                    AccebillConstant.AB_POOL_BRIEF_8218,
                    AccebillConstant.AB_POOL_BRIEF_8118
            });
            params.put("optStatus2", AbPoolConstant.AB_POOL_OPT_STATUS19);
            params.put("poolStatus", AbPoolConstant.AB_BILL_POOL_STATUS_0);
            params.put("poolStatus2", AbPoolConstant.AB_BILL_POOL_STATUS_1);
        }else {
            sql.append(" apd.brief_id in (:briefId) and apd.status>:status1 and (apd.status<:status2 or apd.opt_status in (:optStatus)) ");
        }
        params.put("corpId",corpId);
        params.put("briefId",briefId);
        params.put("validSign",AbPoolConstant.AB_POLL_VALID_SIGN_2);
        Integer status = AbPoolConstant.AB_POOL_STATUS_1;
        if(AbPoolConstant.AB_POOL_BRIEF_8001.equals(briefId) || AbPoolConstant.AB_POOL_BRIEF_001.equals(briefId)){
            status = AbPoolConstant.AB_POOL_STATUS_2;
        }
        params.put("status1",status);
        params.put("status2",AbPoolConstant.AB_POOL_STATUS95);
        params.put("optStatus",new String[]{
                AbPoolConstant.AB_POOL_OPT_STATUS16,
                AbPoolConstant.AB_POOL_OPT_STATUS18,
                AbPoolConstant.AB_POOL_OPT_STATUS_1});
        return ((Number)this.createSQLQuery(sql.toString(),params).uniqueResult()).intValue();
    }
}
