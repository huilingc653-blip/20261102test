package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillSearchforApplyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 追索通知处理
 * @date 2017-06-21 17:35:25
 */
@Repository
public class AbBillSearchforApplyDao extends BaseDao<AbBillSearchforApplyVO, String> {
    /**
     * 获取查询列表界面的sql
     *
     * @param corpAuthSql 操作用户授权的单位权限
     * @return
     */
    private String getQuerySql(String corpAuthSql,DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder("select * from ( ");
        sql.append("select z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,")
                .append("z.bill_type as billType,z.bill_flag as billFlag,c.type,")
                .append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,")
                .append("z.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,z.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,z.SBBLL_RNG_STRT_SN as sbbllRngStrtSn,z.SBBLL_RNG_END_SN as sbbllRngEndSn,")
                .append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
                .append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
                .append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
                .append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,BT_NVL_NUM(l.rcrsokmoney,z.bill_money) as searchForMoney,")
                //设置追索类型状态
                .append("(case "
                        + "when l.rcrs_Tp is not null then l.rcrs_Tp "
                        + "when (l.id in (select LS.LASTID from AB_ACCRECORDS LS where LS.brief_id in ('"
                        + AccebillConstant.BRIEF_STATUS_020 + "','"
                        + AccebillConstant.BRIEF_STATUS_021 + "') and LS.Opt_Status='-4')) "
                        + "then '" + AccebillConstant.AB_RCRS_TP_PROTESTY + "' "
                        + "else '" + AccebillConstant.AB_RCRS_TP_PROTESTN + "' end) as rcrsTp,")
                //设置业务处理状态
                .append("(case c.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_022 + "' then -1 else l.status end) as status,")
                .append("c.corp_id as corpId,c.CORP_CODE as corpCode,c.CORP_NAME as corpName,l.opt_status as optStatus,l.brief_id as briefId, ")
                //		--前手
                .append("case ")
                .append("when l.brief_id='6001' then l.cn_accrecords ")
                .append("when bs.bus_type in ('6001', '9002', '9011', '9017', '9018', '9020', '9023') then l.rec_nm ")
                .append("else c.cn_accrecords end as cnAccrecords, ")
                .append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,")
                .append(" c.BUS_PERSONNEL as busPersonnel, c.BUS_CORP_NAME as busCorpName ");
        if(dataTablesInput != null && !CollectionUtils.isEmpty(dataTablesInput.getFormdata()) && dataTablesInput.getFormdata().get("ins_bankTypeId") != null && !"".equals(dataTablesInput.getFormdata().get("ins_bankTypeId"))){
            sql.append(" ,acc.bank_Type_Id bankTypeId ");
        }
                sql.append(" from AB_VOUCHERINFO_VICE c ")
                .append(" join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ")
                .append(" left join AB_ACCRECORDS l on c.id=l.vvid ")
                .append(" left join ab_brief_status bs on bs.status=l.brief_id ");
        if(dataTablesInput != null && !CollectionUtils.isEmpty(dataTablesInput.getFormdata()) && dataTablesInput.getFormdata().get("ins_bankTypeId") != null && !"".equals(dataTablesInput.getFormdata().get("ins_bankTypeId"))){
            sql.append(" left join bt_bank_acc acc on c.bank_acc=acc.BANK_ACC ");
        }
                sql.append(" where c.valid_sign='" + AccebillConstant.AB_BILL_VALID_SIGN_1 + "'  ")
                .append(" and l.SEND_SIGN='" + AccebillConstant.AB_BILL_SEND_SIGN_Y + "' ")
                .append(" and c.type ='" + AccebillConstant.AB_VOUCHERINFO_OP_TYPE1 + "' ")
                .append(" and z.bill_flag ='" + AccebillConstant.AB_BILL_FLAG_ELECT + "' ")
                .append(" and ( ")
                //正常可以做追索通知业务处理
                .append(" ( ")
                .append(" l.STATUS='" + AccebillConstant.AB_BILL_BUS_STATUS95 + "' ")
                .append(" and l.valid_sign ='" + AccebillConstant.AB_BILL_VALID_SIGN_1 + "' ")
                .append(" and l.OPT_STATUS ='" + AccebillConstant.AB_BILL_OPT_STATUS19 + "' ")
                //背书追索通知
                .append(" and l.brief_id in ('"
                        + AccebillConstant.BRIEF_STATUS_023 + "','"
                        + AccebillConstant.BRIEF_STATUS_9003 + "','"
                        + AccebillConstant.BRIEF_STATUS_9010 + "','"
                        + AccebillConstant.BRIEF_STATUS_9012 + "','"
                        + AccebillConstant.BRIEF_STATUS_9019 + "')"
                        + "and c.box_status='" + AccebillConstant.AB_BOX_STATUS_Y + "' ")
                .append(" ) ")
                //追索通知业务审批中可以查看
                .append(" or (l.brief_id ='" + AccebillConstant.BRIEF_STATUS_022 + "' "
                        + "and l.OPT_STATUS!=" + AccebillConstant.AB_BILL_OPT_STATUS_2 + " "
                        + "and l.status=" + AccebillConstant.AB_BILL_BUS_STATUS11 + " ) ")
                .append(" ) ")
                //用户满足对应查询权限的单位数据
                .append(" and ").append(corpAuthSql).append(" ")
                .append(") bail where 1=1 ");
        return sql.toString();
    }
    /**
     * 加载AbBillSearchforApply表数据
     *
     * @param dataTablesInput
     * @param corpAuthSql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbBillSearchforApplyVO> listAbBillSearchforApply(DataTablesInput dataTablesInput, String corpAuthSql) {
        StringBuilder sql = new StringBuilder(this.getQuerySql(corpAuthSql,dataTablesInput));
        Map<String, Object> params = new HashMap<>();
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (bail.bankAcc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "bail.bankAcc", "bankAccs", params)).append(")");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillSearchforApplyVO.class, "bail", false, params);
    }
    /**
     * 根据参数获取票据可追索数据
     *
     * @param params      查询参数
     * @param corpAuthSql 授权单位
     * @return 返回数据对象
     */
    public AbBillSearchforApplyVO getAbBillSearchforApplyVOByParams(Map<String, Object> params, String corpAuthSql) {
        if (params == null) {
            return null;
        }
        StringBuilder sql = new StringBuilder(getQuerySql(corpAuthSql,null));
        if (params.containsKey("origNo")) {
            sql.append(" and bail.origNo =:origNo ");
        }
        if (params.containsKey("origNo")) {
            sql.append(" and bail.origNo =:origNo ");
        }
        if (params.containsKey("sbbllRngStrtSn")) {
            sql.append(" and bail.sbbllRngStrtSn =:sbbllRngStrtSn ");
        }
        if (params.containsKey("sbbllRngEndSn")) {
            sql.append(" and bail.sbbllRngEndSn =:sbbllRngEndSn ");
        }
        AbBillSearchforApplyVO info = this.getBySQLAliasToBean(sql.toString(), params, AbBillSearchforApplyVO.class);
        return info;
    }
    /**
     * 加载审批列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbBillSearchforApplyVO> dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder("select * from ( ");
        sql.append("select z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,")
                .append("z.bill_type as billType,z.bill_flag as billFlag,l.rcrsokmoney as rcrsokmoney,")
                .append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,c.type,")
                .append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
                .append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
                .append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
                .append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,")
                //设置追索类型状态
                .append("l.rcrs_Tp as rcrsTp,'0' as checkboxHide,l.rcrsrsncd as rcrsrsncd,")
                //设置业务处理状态
                .append("(case c.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_022 + "' then -1 else l.status end) as status,")
                .append("c.corp_id as corpId,c.CORP_CODE as corpCode,c.CORP_NAME as corpName,l.opt_status as optStatus,l.brief_id as briefId, ")
                .append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel ")
                .append(" from AB_VOUCHERINFO_VICE c ")
                .append(" left join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ")
                .append(" left join AB_ACCRECORDS l on c.id=l.vvid ")
//                .append(" left join sys_corp s on c.corp_id = s.id ")
//                .append(" left join sys_corp s2 on c.apply_corp_id = s2.id ")
                .append(" where 1=1 ")
                .append(" and ").append(businessSql).append(" ")
                .append(") bail ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillSearchforApplyVO.class, "bail", true);
    }
}
