package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.AbVoucherinfoDTO;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolEndorseVO;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 　 * @Title: 背书申请数据操作类
 * 　 * @author zhenghj
 *
 * @Description:背书申请数据操作类 　 * @date 2020年3月6日
 * 　 * @version 1.0
 */
@Repository
public class AbPoolEndorseDao extends BaseDao<AbPoolDtl, String> {
    /**
     * 获取查询的sql语句
     *
     * @return
     */
    private StringBuilder getQuerySql(boolean isApprove) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" apd.id as id,apd.brief_id as briefId,apd.orig_no as origNo,apd.inout_type as inoutType,");
        sql.append(" ap.bp_code as bpCode,ap.bp_name as bpName,apd.LASTID as lastId,");
        //sql.append(" sc.code as corpCode,sc.name as corpName,vv.corp_id as corpId,vv.id AS vvId,ap.id as bpId,");
        sql.append(" apc.corp_code as corpCode,apc.corp_name as corpName,vv.corp_id as corpId,vv.id AS vvId,ap.id as bpId,");
        sql.append(" v.id AS vouId,v.bill_type AS billType,v.bill_flag as billFlag,v.payer as payer,v.payee as payee,");
        sql.append(" v.acceptor as acceptor,v.bill_money as billMoney,v.start_date as startDate,v.end_date as endDate,");
        //sql.append(" nbh.bill_no as payNo,nbh.id as payId,");
        sql.append(" apd.id as billId,");
        sql.append("(case vv.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_010 + "' then -1 else (case apd.brief_id when '" + AccebillConstant.BRIEF_STATUS_010 + "' then apd.status else 0  end) end) as status,");
        sql.append("(case apd.brief_id when '" + AccebillConstant.BRIEF_STATUS_010 + "' then apd.opt_Status else null end) as optStatus,api.bif_code as bifCode");
        if (isApprove) {
            sql.append(",apd.id as businessKey ");
        }
        sql.append(" from ab_pool_dtl apd  ");
        sql.append(" left join AB_VOUCHERINFO_VICE vv on apd.vv_id=vv.id  ");
        sql.append(" left join AB_VOUCHERINFO v on vv.vid =v.id  ");
        //sql.append(" left join nis_bill b on b.bill_id=apd.id ");
        //sql.append(" left join nis_billhead nbh on b.pay_id=nbh.id  ");
        sql.append(" left join ab_pool_corp apc on vv.corp_id = apc.corp_id  and APC.valid_sign=1");
        //sql.append(" left join sys_corp sc on apc.corp_id = sc.id    ");
        sql.append(" left join ab_pool ap on ap.id=apd.bp_id  ");
        sql.append(" left join AB_POOLIF api on api.id=ap.pif_id  ");
        return sql;
    }
    /**
     * 查询背书申请数据
     *
     * @param input
     * @param corpAuthList
     * @return
     */
    public DataTablesOutput listAbPoolEndorseTab(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(getQuerySql(false));
        sql.append(" where 1=1 ");
        //过滤掉锁定的数据
        sql.append(" and vv.VALID_SIGN=:validSign ");
        //待处理
        StringBuilder sqlWhere = new StringBuilder(" (apd.brief_id in (:briefId)" +
                "     and apd.status in (:status)" +
                "     and apd.opt_status in (:optStatus)" +
                "     and apd.valid_sign = :validSign" +
                "     and vv.pool_status = :poolStatus" +
                "     and vv.box_status = :boxStatus) ");
        //票据池明细数据入池完成和质押托管转换成功有效的数据
        sql.append(" and ("+sqlWhere);
        sql.append(" or ");
        //审批中
        sql.append(" (apd.brief_id in (:briefId2)" +
                "     and apd.status in (:status2)" +
                "     and vv.box_status = :boxStatus2)");
        //审批通过
        sql.append(" or (apd.brief_id in (:briefId2) and apd.status in (:status) " +
                "        and apd.valid_sign = :validSign))");
        //能背书转让的业务 票据入池、质押托管转换
        params.put("briefId", new String[]{AbPoolConstant.AB_POOL_BRIEF_8018, AbPoolConstant.AB_POOL_BRIEF_8118, AbPoolConstant.AB_POOL_BRIEF_8218});
        params.put("briefId2", new String[]{AbPoolConstant.AB_POOL_BRIEF_010});
        params.put("status", new Integer[]{AbPoolConstant.AB_POOL_STATUS95});
        params.put("status2", new Integer[]{AbPoolConstant.AB_POOL_STATUS11});
        params.put("optStatus", new String[]{AbPoolConstant.AB_POOL_OPT_STATUS19});
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        params.put("poolStatus", AbPoolConstant.AB_BILL_POOL_STATUS_1);
        params.put("boxStatus", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("boxStatus2", AccebillConstant.AB_BOX_STATUS_N);
        //处理审批状态多选查询
        StringBuilder sqlStatus = new StringBuilder(" ");
        if (StringUtils.isNotEmpty(input.getFormdata().get("ins_statusName"))){
            List<String> statusList = Arrays.asList(input.getFormdata().get("ins_statusName").toString().split(","));
            sqlStatus.append(" and ( ");
            for(int i=0; i<statusList.size();i++){
                if(i!=0){sqlStatus.append(" or ");}
                //查询待处理数据
                if(statusList.get(i).equals(String.valueOf(AbPoolConstant.AB_POOL_STATUS0))){
                    sqlStatus.append(" ( "+sqlWhere+" ) ");
                }else
                // 条件查询审批打回数据
                if (String.valueOf(AbPoolConstant.AB_POOL_STATUS_1).equals(statusList.get(i))) {
                    sqlStatus.append("  (vv.back_brief_id =:backBriefId) ");
                    params.put("backBriefId",AbPoolConstant.AB_POOL_BRIEF_010);
                }else
                //审批通过数据排除打回数据
                if (String.valueOf(AbPoolConstant.AB_POOL_STATUS95).equals(statusList.get(i))) {
                    sqlStatus.append(" (vv.back_brief_id is null  and apd.brief_id=:briefId2 and apd.status="+statusList.get(i)+") ");
                }else{
                    sqlStatus.append(" apd.status="+statusList.get(i));
                }
            }
            sqlStatus.append(" ) ");
            sql.append(sqlStatus);
            input.getFormdata().remove("ins_statusName");
        }
        //处理指令状态多选查询
        StringBuilder sqlOptStatus = new StringBuilder(" ");
        if (StringUtils.isNotEmpty(input.getFormdata().get("ins_optStatusName"))){
            List<String> optStatusList = Arrays.asList(input.getFormdata().get("ins_optStatusName").toString().split(","));
            sqlOptStatus.append(" and ( ");
            for(int i=0; i<optStatusList.size();i++){
                if(i!=0){sqlOptStatus.append(" or ");}
                //查询指令状态银行交易完成
                if (String.valueOf(AbPoolConstant.AB_POOL_OPT_STATUS19).equals(optStatusList.get(i))) {
                    sqlOptStatus.append(" ( apd.opt_status=:optStatus and apd.brief_id=:briefId2 ) ");
                }else{
                    sqlOptStatus.append(" apd.opt_status="+optStatusList.get(i));
                }
            }
            sqlOptStatus.append(" ) ");
            sql.append(sqlOptStatus);
            input.getFormdata().remove("ins_optStatusName");
        }
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "apc.CORP_ID", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        input.addOrder("origNo", true);
        return this.getDynamicBySQL(input, sql.toString(), AbPoolEndorseVO.class, "", false, params);
    }
    /**
     * 审批页面查询当前操作员可审批的贴现申请数据
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
        StringBuilder sql = new StringBuilder(getQuerySql(true));
        sql.append(" WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();
        String businessSql = SqlUtil.getInSqlByParams(businessList, "apd.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return getDynamicBySQL(input, sql.toString(), AbPoolEndorseVO.class, "", false, params);
    }
    /**
     * 根据票据池交易明细Id查询背书申请信息
     *
     * @param pdId 票据池交易明细id
     * @return
     */
    public List<AbPoolEndorseVO> getAbPoolEndorseVOByPdId(String pdId) {
        StringBuilder sql = new StringBuilder(getQuerySql(true));
        sql.append(" WHERE apd.id = :pdId  ");
        Map<String, Object> params = new HashMap<>();
        params.put("pdId", pdId);
        List<AbPoolEndorseVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbPoolEndorseVO.class);
        return listInfo;
    }
    /**
     * 获取付款背书失败信息返回给结算系统
     * @param apdId 票据池明细ID
     * @return  背书失败票据信息
     */
    public List<AbVoucherinfoDTO> queryBillPoolEndorsePayErrorInfo(String apdId) {
        Map<String,Object> params=new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select b.pay_id as payId,b.bill_id as id,z.bill_money as billMoney,a.opt_status as optStatus ");
        sql.append(" from nis_bill b  ");
        sql.append(" inner join ab_pool_dtl a on a.id = b.bill_id ");
        sql.append(" inner join AB_VOUCHERINFO_VICE c on a.vv_id = c.id ");
        sql.append(" inner join AB_VOUCHERINFO z on c.vid = z.id ");
        sql.append(" inner join nis_billhead n on b.pay_id = n.id ");
        sql.append(" inner join fc_voucher_type t on t.id = n.voucher_type_id ");
        sql.append(" where t.sys_voucher_code =:sysEndorsePayType and n.status =:status  ");
        sql.append(" and a.brief_id =:briefIdEndorse and (a.opt_status in (:errorOptStatus) or (a.status=:status1 and a.opt_status is null))  ");
        sql.append("  and a.id=:apdId ");
        sql.append(" order by b.pay_id, a.opt_status  ");
        params.put("sysEndorsePayType", NetbankConstant.PAY_TYPE_50);
        params.put("status", NetbankConstant.STATUE_CHECK);
        params.put("status1", AccebillConstant.AB_BILL_BUS_STATUS_1);
        params.put("apdId", apdId);
        params.put("briefIdEndorse", AccebillConstant.BRIEF_STATUS_010);
        params.put("errorOptStatus", new String[]{AccebillConstant.AB_BILL_OPT_STATUS_1,AccebillConstant.AB_BILL_OPT_STATUS_2});
        return this.listBySQLAliasToBean(sql.toString(),params, AbVoucherinfoDTO.class);
    }
    /**
     * 获取付款背书失败信息返回给结算系统
     * @return  背书失败票据信息
     */
    public List<AbVoucherinfoDTO> queryBillPoolEndorsePayErrorInfo() {
        Map<String,Object> params=new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id as vvid,b.pay_id as payId,b.bill_id as id,z.bill_money as billMoney,a.opt_status as optStatus ");
        sql.append(" from nis_bill b  ");
        sql.append(" inner join ab_pool_dtl a on a.id = b.bill_id ");
        sql.append(" inner join AB_VOUCHERINFO_VICE c on a.vv_id = c.id ");
        sql.append(" inner join AB_VOUCHERINFO z on c.vid = z.id ");
        sql.append(" inner join nis_billhead n on b.pay_id = n.id ");
        sql.append(" inner join fc_voucher_type t on t.id = n.voucher_type_id ");
        sql.append(" where t.sys_voucher_code =:sysEndorsePayType and n.status =:status  and b.pay_id in ( ");
        sql.append("select b.pay_id from nis_bill b  ");
        sql.append("inner join ab_pool_dtl a on a.id=b.bill_id  ");
        sql.append("left join AB_BILL_MIDDLE m on b.bill_id=m.bu_id  ");
        sql.append("where a.brief_id=:briefIdEndorse and m.id is null and a.opt_status in (:errorOptStatus)) ");
        sql.append(" order by b.pay_id, a.opt_status  ");
        params.put("sysEndorsePayType", NetbankConstant.PAY_TYPE_50);
        params.put("status", NetbankConstant.STATUE_CHECK);
        params.put("briefIdEndorse", AccebillConstant.BRIEF_STATUS_010);
        params.put("errorOptStatus", new String[]{AccebillConstant.AB_BILL_OPT_STATUS_1,AccebillConstant.AB_BILL_OPT_STATUS_2});
        return this.listBySQLAliasToBean(sql.toString(),params, AbVoucherinfoDTO.class);
    }
    /**
     * 获取付款背书成功信息返回给结算系统
     */
    public List<AbVoucherinfoDTO> queryBillPoolEndorsePaySuccessInfo() {
        Map<String,Object> params=new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id as vvid,b.pay_id as payId,b.bill_id as id,z.bill_money as billMoney,a.opt_status as optStatus ");
        sql.append(" from nis_bill b  ");
        sql.append(" inner join ab_pool_dtl a on a.id = b.bill_id ");
        sql.append(" inner join AB_VOUCHERINFO_VICE c on a.vv_id = c.id ");
        sql.append(" inner join AB_VOUCHERINFO z on c.vid = z.id ");
        sql.append(" inner join nis_billhead n on b.pay_id = n.id ");
        sql.append(" inner join fc_voucher_type t on t.id = n.voucher_type_id ");
        sql.append(" where t.sys_voucher_code =:sysEndorsePayType and n.status =:status  and b.pay_id in ( ");
        sql.append("select b.pay_id from nis_bill b  ");
        sql.append("inner join ab_pool_dtl a on a.id=b.bill_id  ");
        sql.append("left join AB_BILL_MIDDLE m on b.bill_id=m.bu_id  ");
        sql.append("where a.brief_id=:briefIdEndorse and m.id is null and a.opt_status in (:successOptStatus)) ");
        sql.append(" order by b.pay_id, a.opt_status  ");
        params.put("sysEndorsePayType", NetbankConstant.PAY_TYPE_50);
        params.put("status", NetbankConstant.STATUE_CHECK);
        params.put("briefIdEndorse", AccebillConstant.BRIEF_STATUS_010);
        params.put("successOptStatus", new String[]{AccebillConstant.AB_BILL_OPT_STATUS19});
        return this.listBySQLAliasToBean(sql.toString(),params, AbVoucherinfoDTO.class);
    }
}
