package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillRecallVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 通用撤回处理
 * @date 2017-06-21 17:35:25
 */
@Repository
public class AbBillRecallDao extends BaseDao<AbBillRecallVO, String> {
    /**
     * 加载AbBillRecall表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbBillRecall(DataTablesInput dataTablesInput, String corpAuthSql) {
        StringBuilder sql = new StringBuilder("select * from ( ");
        sql.append("select c.batch_no batchNo,z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,")
                .append("z.bill_type as billType,z.bill_flag as billFlag,c.type,")
                .append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,")
                .append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
                .append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
                .append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
                .append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,")
                //设置业务处理状态
                .append("(case c.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_032 + "' then -1 else l.status end) as status,")
                .append("c.corp_id as corpId,c.CORP_CODE as corpCode,c.CORP_NAME as corpName,l.opt_status as optStatus,BT_NVL_CHAR(l.RECALL_TYPE,BT_CONCAT2(l.brief_id,BT_NVL_CHAR(l.BAIL_TYPE,''))) as recallType, ")
                //		--前手
                .append("case ")
                .append("when l.brief_id='6001' then l.cn_accrecords ")
                .append("when bs.bus_type in (:bus_type_0) then l.rec_nm ")
                .append("else c.cn_accrecords end as cnAccrecords, ")
                .append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel ")
                //新一代票据新增出票标志、是否允许分包、子票区间开始序号、子票区间结束序号、标准金额、出票人统一信用代码
                .append(", z.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,z.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,z.SBBLL_RNG_STRT_SN as sbBllRngStrtSN")
                .append(", z.SBBLL_RNG_END_SN as sbBllRngEndSN,z.BL_STD_AMT as blStdAmt,c.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo,z.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo ")
                .append(" , c.BUS_CORP_NAME as busCorpName,c.bank_type_id as bankTypeId ")
                .append(" from AB_VOUCHERINFO_VICE c ")
                .append(" left join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ")
                .append(" left join AB_ACCRECORDS l on c.id=l.vvid ")
                .append(" left join ab_brief_status bs on bs.status=l.brief_id ")
                .append(" where c.valid_sign='" + AccebillConstant.AB_BILL_VALID_SIGN_1 + "'  ")
                .append(" and l.SEND_SIGN='" + AccebillConstant.AB_BILL_SEND_SIGN_Y + "' ")
                .append(" and z.bill_flag ='" + AccebillConstant.AB_BILL_FLAG_ELECT + "' ")
                .append(" and ( ")
                //正常可以做通用撤回业务处理
                .append(" ( ")
                .append(" l.STATUS='" + AccebillConstant.AB_BILL_BUS_STATUS95 + "' ")
                .append(" and l.valid_sign ='" + AccebillConstant.AB_BILL_VALID_SIGN_1 + "' ")
                .append(" and l.OPT_STATUS in('"
//				+AccebillConstant.AB_BILL_OPT_STATUS16+"','"
//				+AccebillConstant.AB_BILL_OPT_STATUS17+"','"
                        + AccebillConstant.AB_BILL_OPT_STATUS18 + "') ")
                //撤回类型:01701开票保证，01702收票保证；01703背书保证 002提示承兑003提示收票 010背书申请 011贴现申请 018质押申请 019质押解除 020提示付款 021逾期付款 022追索申请 023追索同意清楚
                .append(" and l.brief_id in('"
                        + AccebillConstant.BRIEF_STATUS_017 + "','"
                        + AccebillConstant.BRIEF_STATUS_002 + "','"
                        + AccebillConstant.BRIEF_STATUS_003 + "','"
                        + AccebillConstant.BRIEF_STATUS_010 + "','"
                        + AccebillConstant.BRIEF_STATUS_011 + "','"
                        + AccebillConstant.BRIEF_STATUS_018 + "','"
                        + AccebillConstant.BRIEF_STATUS_019 + "','"
                        + AccebillConstant.BRIEF_STATUS_020 + "','"
                        + AccebillConstant.BRIEF_STATUS_021 + "','"
                        //+AccebillConstant.BRIEF_STATUS_022+"','"
                        + AccebillConstant.BRIEF_STATUS_024+"','"
                        + AccebillConstant.BRIEF_STATUS_023 + "') ")
                .append(" and c.box_status='" + AccebillConstant.AB_BOX_STATUS_Y + "' ")
                .append(" ) ")
                //通用撤回业务审批中可以查看
                .append(" or (l.brief_id ='" + AccebillConstant.BRIEF_STATUS_032 + "' "
                        + "and l.OPT_STATUS!=" + AccebillConstant.AB_BILL_OPT_STATUS_2 + " "
                        + "and l.status=" + AccebillConstant.AB_BILL_BUS_STATUS11 + " ) ")
                .append(" ) ")
                //用户满足对应查询权限的单位数据
                .append(" and ").append(corpAuthSql).append(" ");
        Map<String, Object> params = new HashMap<>();
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
        }
        sql.append(") bail ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillRecallVO.class, "bail", true, params);
    }
    /**
     * 加载审批列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder("select * from ( ");
        sql.append("select z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,")
                .append("z.bill_type as billType,z.bill_flag as billFlag,c.type,")
                .append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,")
                .append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
                .append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
                .append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
                .append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,'0' as checkboxHide,")
                .append("c.corp_id as corpId,c.CORP_CODE as corpCode,c.CORP_NAME as corpName,l.opt_status as optStatus,l.RECALL_TYPE as recallType,")
                .append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_CORP_NAME as busCorpName,c.BUS_PERSONNEL as busPersonnel ")
                .append(" from AB_VOUCHERINFO_VICE c ")
                .append(" left join AB_VOUCHERINFO z on z.id=c.vid ")
                .append(" left join AB_ACCRECORDS l on c.id=l.vvid ")
//                .append(" left join sys_corp s on c.corp_id = s.id ")
//                .append(" left join sys_corp s2 on c.apply_corp_id = s2.id ")
                .append(" where 1=1 ")
                //用户满足对应查询权限的单位数据
                .append(" and ").append(businessSql).append(" ")
                .append(") bail ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillRecallVO.class, "bail", true);
    }
}
