package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolCollectionApplyVO;
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
 * @Title: AbPoolCollectionApplyDao
 * @Description: 提示付款DAO
 * @author wangzy
 * @date 2020/3/11 10:10
 *
 */
@Repository
public class AbPoolCollectionApplyDao extends BaseDao<AbPoolDtl, String> {
    /**
     * 通用查询数据sql
     *
     * @return
     */
    public StringBuilder getCommonSql(StringBuilder sql) {
        //sql.append(" BT_CONCAT4('(',s.code,')',s.name) as corpCodeAndName,dtl.id as dtlId,v.bill_Type as billType, v.bill_Flag as billFlag,v.orig_No as origNo, ");
        sql.append(" BT_CONCAT4('(',s.corp_code,')',s.corp_name) as corpCodeAndName,dtl.id as dtlId,v.bill_Type as billType, v.bill_Flag as billFlag,v.orig_No as origNo, ");
        sql.append(" v.bill_Money as billMoney,v.banendrsmtmk as banendrsmtmk,v.start_Date as startDate,v.end_Date as endDate,v.payer as payer, ");
        sql.append(" v.PAYERACC as payeracc, v.acceptor as acceptor,BT_CONCAT4('(',p.BP_CODE,')',p.BP_NAME) as pbCodeAndName,v.payee as payee, ");
        sql.append(" (case when vv.BACK_BRIEF_ID='"+AbPoolConstant.AB_POOL_BRIEF_020+"' then -1 when a.brief_Id='"+AbPoolConstant.AB_POOL_BRIEF_020+"' ");
        sql.append(" then dtl.status else 0 end) as status,dtl.APP_NO as appNo,a.id as aid,v.PAYER_BANK_NAME as payerBankName, ");
        sql.append(" (case dtl.brief_Id when '"+AbPoolConstant.AB_POOL_BRIEF_020+"' then dtl.OPT_STATUS else null end) as optStatus,vv.corp_Id as corpId, ");
        sql.append(" vv.abs as abs,v.payeeacc as payeeacc,v.ACCEPTOR_BANK_NAME as acceptorBankName,dtl.bp_Id as bpId,dtl.INOUT_TYPE as inoutType,a.rsn as rsn, ");
        sql.append(" v.PAYEE_BANK_NAME as payeeBankName,v.PAYEEBANKNO as payeebankno,v.ACCEPTORACC as acceptoracc,v.ACCEPTORBANKNO as acceptorbankno, ");
        sql.append(" a.STTLMMK as sttlmmk,(case dtl.brief_Id when '"+AbPoolConstant.AB_POOL_BRIEF_020+"' then a.BUSINESS_DATE else null end) as businessDate ");
        sql.append(" from ab_pool_dtl dtl left join AB_VOUCHERINFO_VICE vv on vv.id=dtl.vv_id left join AB_VOUCHERINFO v on v.id=vv.vid ");
        sql.append(" left join ab_accrecords a on a.PD_ID=dtl.id and a.brief_Id=:briefId2 left join ab_pool p on p.id=dtl.bp_id left join ab_poolif lif on lif.id=p.pif_id ");
        sql.append(" left join ab_pool_corp apc on vv.corp_id = apc.corp_id  and APC.valid_sign=1");
        //sql.append(" left join sys_corp s on s.id=apc.corp_id  ");
        sql.append(" left join ab_pool_corp s on s.corp_id=apc.corp_id  ");
        sql.append(" where dtl.valid_Sign=:validSign");
        return sql;
    }
    /**
     * 提示付款列表查询
     * @param input
     * @return 列表信息
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        String stime = (String)input.getFormdata().get("led_startDate");
        if(StringUtils.isNotBlank(stime)){
            stime = stime + " 23:59:59";
            input.getFormdata().put("led_startDate", stime);
        }
        String etime = (String)input.getFormdata().get("led_endDate");
        if(StringUtils.isNotBlank(etime)){
            etime = etime + " 23:59:59";
            input.getFormdata().put("led_endDate", etime);
        }
        StringBuilder sql = new StringBuilder(" select * from ( select");
        this.getCommonSql(sql);
        sql.append(" and ((dtl.brief_Id in (:briefId) and dtl.opt_Status=:optStatus  and vv.pool_status = :poolStatus ) or ");
        sql.append(" (dtl.brief_id=:briefId2 ))  and (lif.bif_code is null or lif.bif_code not in(:bifCode)) ");
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "s.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ) coll ");
        params.put("briefId", new String[]{AbPoolConstant.AB_POOL_BRIEF_8018,AbPoolConstant.AB_POOL_BRIEF_8118,AbPoolConstant.AB_POOL_BRIEF_8218});
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        params.put("poolStatus", AbPoolConstant.AB_BILL_POOL_STATUS_1);
        params.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS19);
        params.put("briefId2", AccebillConstant.BRIEF_STATUS_020);
        params.put("bifCode", new String[]{AbPoolConstant.AB_POOL_INTO_APPLE_9015,AbPoolConstant.AB_POOL_INTO_APPLE_CZB});
        return getDynamicBySQL(input, sql.toString(), AbPoolCollectionApplyVO.class, "", true, params);
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
        sql.append(" and ").append(businessSql).append(" ) coll");
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        params.put("briefId2", AccebillConstant.BRIEF_STATUS_020);
        DataTablesOutput dataOutInfo=this.getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolCollectionApplyVO.class, "", false,params);
        return dataOutInfo;
    }
    /**
     * 信息查询
     * @param dtlId 明细表ID
     * @return
     *
     */
    @SuppressWarnings("rawtypes")
    public AbPoolCollectionApplyVO getAbPoolCollectionApplyVOInfo(String dtlId) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(" select * from (select ");
        this.getCommonSql(sql);
        sql.append(" and dtl.id=:dtlId ) coll");
        params.put("dtlId", dtlId);
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        params.put("briefId2", AccebillConstant.BRIEF_STATUS_020);
        return getBySQLAliasToBean(sql.toString(), params, AbPoolCollectionApplyVO.class);
    }
    /**
     * 信息查询
     * @param dtlId 明细表ID
     * @return
     *
     */
    @SuppressWarnings("rawtypes")
    public List<String> listAbAccrecordIds(String dtlId) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(" select a.id ");
        sql.append(" from ab_accrecords a where a.PD_ID=:pdId ");
        params.put("pdId", dtlId);
        return createSQLQuery(sql.toString(), params).list();
    }
}
