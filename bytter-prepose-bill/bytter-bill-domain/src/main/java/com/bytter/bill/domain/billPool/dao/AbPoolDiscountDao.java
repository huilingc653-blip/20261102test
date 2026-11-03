package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolDiscountVO;
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
 * @author zhenghj
 * @Title: AbPoolDiscountDao
 * @Description:贴现申请数据操作类
 * @date 2020年3月10日
 * 　 * @version 1.0
 */
@Repository
public class AbPoolDiscountDao extends BaseDao<AbPoolDtl, String> {
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
        sql.append("(case vv.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_011 + "' then -1 else (case apd.brief_id when '" + AccebillConstant.BRIEF_STATUS_011 + "' then apd.status else 0  end) end) as status,");
        sql.append("(case apd.brief_id when '" + AccebillConstant.BRIEF_STATUS_011 + "' then apd.opt_Status else null end) as optStatus,api.bif_code as bifCode");
        if (isApprove) {
            sql.append(",apd.id as businessKey ");
        }
        sql.append(" from ab_pool_dtl apd  ");
        sql.append(" left join AB_VOUCHERINFO_VICE vv on apd.vv_id=vv.id  ");
        sql.append(" left join AB_VOUCHERINFO v on vv.vid =v.id  ");
        sql.append(" left join ab_pool_corp apc on vv.corp_id = apc.corp_id and APC.valid_sign=1 ");
        //sql.append(" left join sys_corp sc on apc.corp_id = sc.id    ");
        sql.append(" left join ab_pool ap on ap.id=apd.bp_id  ");
        sql.append(" left join AB_POOLIF api on api.id=ap.pif_id  ");
        return sql;
    }
    /**
     * 查询贴现申请数据
     *
     * @param input
     * @param corpAuthList
     * @return
     */
    public DataTablesOutput listAbPoolDiscountTab(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(getQuerySql(false));
        sql.append(" where 1=1 ");
        //过滤掉锁定的数据
        sql.append(" and vv.VALID_SIGN=:validSign ");
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
        params.put("briefId2", new String[]{AccebillConstant.BRIEF_STATUS_011});
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
                    params.put("backBriefId",AbPoolConstant.AB_POOL_BRIEF_011);
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
        return this.getDynamicBySQL(input, sql.toString(), AbPoolDiscountVO.class, "", false, params);
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
        return getDynamicBySQL(input, sql.toString(), AbPoolDiscountVO.class, "", false, params);
    }
    /**
     * 根据票据池交易明细Id查询贴现申请信息
     *
     * @param pdId 票据池交易明细id
     * @return
     */
    public List<AbPoolDiscountVO> getAbPoolDiscountVOByPdId(String pdId) {
        StringBuilder sql = new StringBuilder(getQuerySql(true));
        sql.append(" WHERE apd.id = :pdId  ");
        Map<String, Object> params = new HashMap<>();
        params.put("pdId", pdId);
        List<AbPoolDiscountVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbPoolDiscountVO.class);
        return listInfo;
    }
    /**
     * 根据参数获取票据池交易明细数据
     *
     * @param params 票据池交易明细id
     * @return
     */
    public AbPoolDtl getAbPoolDtlByParams(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("select d.vv_id as vvid,d.id as id,d.bp_id as bpId,d.BRIEF_ID as briefId,d.ORIG_NO as origNo,");
        sql.append("d.INOUT_TYPE as inoutType,d.STATUS as status,d.OPT_STATUS as optStatus,d.VALID_SIGN as validSign,d.LASTID as lastid ");
        sql.append("  FROM ab_pool_dtl d where 1=1 ");
        if (params.containsKey("origNo")) {
            sql.append("and ORIG_NO=:origNo ");
        }
        if (params.containsKey("briefId")) {
            sql.append("and BRIEF_ID=:briefId ");
        }
        if (params.containsKey("lastId")) {
            sql.append("and lastId=:lastId ");
        }
        if (params.containsKey("id")) {
            sql.append(" and id=:id ");
        }
        sql.append(" order by CREATE_TIME desc ");
        List<AbPoolDtl> list = this.listBySQLAliasToBean(sql.toString(), params, AbPoolDtl.class);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
