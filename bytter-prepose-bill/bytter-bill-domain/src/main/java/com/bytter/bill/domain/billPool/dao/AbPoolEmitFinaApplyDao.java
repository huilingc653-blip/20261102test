package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPoolContract;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitFinaApplyVO;
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
 * @Title: AbPoolEmitFinaApplyDao
 * @Description: 融资申请DAO
 * @author wangzy
 * @date 2020/2/10 16:10
 *
 */
@Repository
public class AbPoolEmitFinaApplyDao extends BaseDao<AbPoolContract, String> {
    /**
     * 通用查询数据sql
     *
     * @return
     */
    public StringBuilder getCommonSql(StringBuilder sql) {
        //sql.append(" BT_CONCAT4('(',s.code,')',s.name) as corpCodeAndName,dtl.id as dtlId,v.bill_Type as billType, v.bill_Flag as billFlag, ");
        sql.append(" BT_CONCAT4('(',s.corp_code,')',s.corp_name) as corpCodeAndName,dtl.id as dtlId,v.bill_Type as billType, v.bill_Flag as billFlag, ");
        sql.append(" v.bill_Money as billMoney,v.banendrsmtmk as banendrsmtmk,v.start_Date as startDate,v.end_Date as endDate,v.payer as payer, ");
        sql.append(" v.PAYERACC as payeracc, v.acceptor as acceptor,BT_CONCAT4('(',p.BP_CODE,')',p.BP_NAME) as pbCodeAndName,v.payee as payee, ");
        sql.append(" dtl.STATUS as status,dtl.OPT_STATUS as optStatus,vv.corp_Id as corpId,dtl.APP_NO as appNo,abCon.status as conStatus,a.id as aid, ");
        sql.append(" vv.abs as abs,v.payeeacc as payeeacc,v.ACCEPTOR_BANK_NAME as acceptorBankName,abCon.id as conId,dtl.bp_Id as bpId,abCon.CON_NAME as conName, ");
        sql.append(" v.PAYER_BANK_NAME as payerBankName,v.PAYERBANKNO as payerbankno,v.orig_No as origNo,abCon.OPT_STATUS as conOptStatus,dtl.CREATE_TIME as businessDate,");
        sql.append(" v.PAYEE_BANK_NAME as payeeBankName,v.PAYEEBANKNO as payeebankno,v.ACCEPTORACC as acceptoracc,v.ACCEPTORBANKNO as acceptorbankno, ");
        //sql.append(" vv.source as source,nbh.bill_no as payNo,nbh.id as payId,vv.voucher_id as voucherId ");
        sql.append(" vv.source as source,vv.voucher_id as payId,vv.voucher_id as voucherId ");
        sql.append(" from ab_pool_dtl dtl left join AB_VOUCHERINFO_VICE vv on vv.id=dtl.vv_id left join AB_VOUCHERINFO v on v.id=vv.vid ");
        sql.append(" left join ab_pool_corp apc on vv.corp_id = apc.corp_id  and APC.valid_sign=1");
        //sql.append(" left join sys_corp s on s.id=apc.corp_id  ");
        sql.append(" left join ab_pool_corp s on s.corp_id=apc.corp_id  ");
        sql.append(" left join ab_accrecords a on a.PD_ID=dtl.id left join ab_pool p on p.id=dtl.bp_id  ");
        //sql.append(" left join Nis_Billhead nbh on nbh.id=vv.voucher_id  ");
        sql.append(" left join AB_POOL_CONTRACT abCon on dtl.id=abCon.dtl_Id and (abCon.status in (:status) or abCon.OPT_STATUS=:conOptStatus) where 1=1 ");
        return sql;
    }
    /**
     * 融资申请列表查询
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
        StringBuilder sql = new StringBuilder(" select ");
        this.getCommonSql(sql);
        sql.append(" and vv.VALID_SIGN=:validSign and dtl.valid_Sign=:validSign and dtl.brief_Id=:briefId ");
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "s.corp_Id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        params.put("briefId", AbPoolConstant.AB_POOL_BRIEF_8001);
        params.put("status", new String[]{AbPoolConstant.AB_POOL_CONTRACT_STATUS0,AbPoolConstant.AB_POOL_CONTRACT_STATUS1});
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        params.put("conOptStatus", AbPoolConstant.AB_POOL_OPT_STATUS_1);
        return getDynamicBySQL(input, sql.toString(), AbPoolEmitFinaApplyVO.class, "", false, params);
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
        StringBuilder sql =  new StringBuilder(" select a.id as businessKey, ");
        this.getCommonSql(sql);
        String businessSql = SqlUtil.getInSqlByParams(businessList, "a.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        params.put("status", new String[]{AbPoolConstant.AB_POOL_CONTRACT_STATUS0,AbPoolConstant.AB_POOL_CONTRACT_STATUS1});
        params.put("conOptStatus", AbPoolConstant.AB_POOL_OPT_STATUS_1);
        DataTablesOutput dataOutInfo=this.getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolEmitFinaApplyVO.class, "", false,params);
        return dataOutInfo;
    }
    /**
     * 出票融资申请信息查询
     * @param dtlId 明细表ID
     * @return
     *
     */
    @SuppressWarnings("rawtypes")
    public AbPoolEmitFinaApplyVO getAbPoolEmitFinaApplyVOInfo(String dtlId) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql =  new StringBuilder(" select ");
        this.getCommonSql(sql);
        sql.append(" and dtl.id=:dtlId ");
        params.put("dtlId", dtlId);
        params.put("status", new String[]{AbPoolConstant.AB_POOL_CONTRACT_STATUS0,AbPoolConstant.AB_POOL_CONTRACT_STATUS1});
        params.put("conOptStatus", AbPoolConstant.AB_POOL_OPT_STATUS_1);
        return getBySQLAliasToBean(sql.toString(), params, AbPoolEmitFinaApplyVO.class);
    }
    /**
     * 票据池质权人信息查询
     * @param bpId 票据池ID
     * @return
     *
     */
    @SuppressWarnings("rawtypes")
    public AbPoolEmitFinaApplyVO selectPledgee(String bpId) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql =  new StringBuilder(" select ");
        sql.append(" bp.BANK_ACC as acceptoracc,bp.ACC_NAME as acceptor,bp.BANK_NO as acceptorbankno,bp.BANK_NAME as acceptorBankName ");
        sql.append(" from AB_POOL_PLEDGEE bp where bp.BP_ID=:bpId and bp.VALID_SIGN=:validSign");
        params.put("bpId", bpId);
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        return getBySQLAliasToBean(sql.toString(), params, AbPoolEmitFinaApplyVO.class);
    }
    /**
     * 合同信息查询
     * @param conId 合同ID
     * @return
     *
     */
    @SuppressWarnings("rawtypes")
    public AbPoolEmitFinaApplyVO viewContract(String conId) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql =  new StringBuilder(" select ");
        sql.append(" CON_FTYPE as conFtype,CON_NAME as conName,con_info as conInfo ");
        sql.append(" from Ab_Pool_Contract where ID=:conId");
        params.put("conId", conId);
        return getBySQLAliasToBean(sql.toString(), params, AbPoolEmitFinaApplyVO.class);
    }
    /**
     * 出票融资申请信息查询
     * @param dtlId 明细表ID
     * @return
     *
     */
    @SuppressWarnings("rawtypes")
    public List<AbPoolEmitFinaApplyVO> listConInfo(String dtlId) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql =  new StringBuilder(" select ");
        sql.append(" con.OPT_STATUS as conOptStatus,con.CON_NAME as conName,con.STATUS as conStatus ");
        sql.append(" from Ab_Pool_Contract con where con.DTL_ID=:dtlId and con.STATUS in (:status) order by create_time");
        params.put("dtlId", dtlId);
        params.put("status", new String[]{AbPoolConstant.AB_POOL_CONTRACT_STATUS2,AbPoolConstant.AB_POOL_CONTRACT_STATUS1});
        return listBySQLAliasToBean(sql.toString(),params,AbPoolEmitFinaApplyVO.class);
    }
}
