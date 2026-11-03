package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillInstructionManageVO;
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
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 指令处理
 * @date 2017-06-21 17:35:25
 */
@Repository
public class AbBillInstructionManageDao extends BaseDao<AbBillInstructionManageVO, String> {
    /**
     * 加载AbBillInstructionManage表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbBillInstructionManageVO> listAbBillInstructionManage(DataTablesInput dataTablesInput, String corpAuthSql) {
        // 设置业务结束时间
        String etime = (String) dataTablesInput.getFormdata().get("led_applyDate");
        if (StringUtils.isNotBlank(etime)) {
            etime = etime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_applyDate", etime);
        }
        StringBuilder sql = new StringBuilder("select ");
        sql.append("z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,z.bill_type as billType,z.bill_flag as billFlag,")
                .append("z.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,z.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,z.SBBLL_RNG_STRT_SN as sbbllRngStrtSn,")
                .append("z.SBBLL_RNG_END_SN as sbbllRngEndSn,z.BL_STD_AMT as blStdAmt,z.EBILL_BL_CRCL_INDC as ebillBlCrclIndc,")
                .append("z.BL_BSN_TPCD as blBsnTpcd,c.batch_no batchNo,")
                .append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,c.type,")
                .append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
                .append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
                .append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
                .append("l.rec_acct_id as recAcctId,l.return_code as returnCode,l.return_msg as returnMsg,")
                .append("c.corp_id as corpId,c.SOC_CODE as socCode,c.CORP_CODE as corpCode,c.CORP_NAME as corpName,l.opt_status as optStatus,l.brief_id as briefId, ")
                //		--前手
                .append("case ")
                .append("when l.brief_id='6001' then l.cn_accrecords ")
                .append("when bs.bus_type in (:bus_type_0) then l.rec_nm ")
                .append("else '' end as cnAccrecords ,")
                .append(" C.CN_ACCRECORDS_NAME as cnAccrecordsName, ")
                .append(" case  ")
                .append(" when ll.brief_id = '6201' then '上划' ")
                .append(" when ll.brief_id = '6202' then '归集' ")
                .append(" else '' end as billTransferType, ")
                .append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel ")
        //		--前手
                .append(" , c.BUS_CORP_NAME as busCorpName,c.bank_acc as bankAcc ")
                .append(" from AB_VOUCHERINFO_VICE c ")
                .append(" left join AB_VOUCHERINFO z on z.id=c.vid ")
                .append(" left join AB_ACCRECORDS l on c.id = l.vvid ")
                .append(" left join ab_brief_status bs on bs.status=l.brief_id ")
                .append(" left join AB_ACCRECORDS ll on l.lastid = ll.id ")
                .append(" where c.valid_sign='" + AccebillConstant.AB_BILL_VALID_SIGN_1 + "'  ")
                .append(" and z.bill_flag ='" + AccebillConstant.AB_BILL_FLAG_ELECT + "' ")
                //（撤票时前业务valid_sign为2）
                .append(" and l.valid_sign in ('" + AccebillConstant.AB_BILL_VALID_SIGN_2 + "','" + AccebillConstant.AB_BILL_VALID_SIGN_1 + "') ")
                .append(" and l.SEND_SIGN='" + AccebillConstant.AB_BILL_SEND_SIGN_Y + "' ")
                .append(" and l.STATUS='" + AccebillConstant.AB_BILL_BUS_STATUS95 + "' ")
                .append(" and l.OPT_STATUS in('" +
                        AccebillConstant.AB_BILL_OPT_STATUS_1 + "','" +
                        AccebillConstant.AB_BILL_OPT_STATUS15 + "','" +
                        AccebillConstant.AB_BILL_OPT_STATUS16 + "','" +
                        AccebillConstant.AB_BILL_OPT_STATUS17 + "','" +
                        AccebillConstant.AB_BILL_OPT_STATUS18
                        + "') ")
                //指令处理排除掉票据池的票据明细指令
                .append(" and l.PD_ID is null ")
                .append(" and ").append(corpAuthSql).append(" ");
        Map<String, Object> params = new HashMap<>();
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillInstructionManageVO.class, null, false, params);
    }
    /**
     * 加载测试AbBillInstructionManage表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbBillInstructionManageTest(DataTablesInput dataTablesInput, String corpAuthSql) {
        // 设置业务结束时间
        String etime = (String) dataTablesInput.getFormdata().get("led_applyDate");
        if (StringUtils.isNotBlank(etime)) {
            etime = etime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_applyDate", etime);
        }
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,z.bill_type as billType,z.bill_flag as billFlag,");
        sql.append(" z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,c.type,");
        sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,");
        sql.append(" z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,");
        sql.append(" z.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,z.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,z.SBBLL_RNG_STRT_SN as sbbllRngStrtSn,");
        sql.append(" z.SBBLL_RNG_END_SN as sbbllRngEndSn,z.BL_STD_AMT as blStdAmt,z.EBILL_BL_CRCL_INDC as ebillBlCrclIndc,");
        sql.append(" z.BL_BSN_TPCD as blBsnTpcd, l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,");
        sql.append(" l.rec_acct_id as recAcctId,'0' checkboxHide,l.return_code as returnCode,l.return_msg as returnMsg,");
        sql.append(" c.corp_id as corpId,c.CORP_CODE as corpCode,c.CORP_NAME as corpName,l.opt_status as optStatus,l.brief_id as briefId,");
        sql.append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel ");
        sql.append(" , z.bl_std_amt as transMoney, z.bl_rmtr_unn_id_no as blRmtrUnnIdNo, c.BUS_CORP_NAME as busCorpName");
        sql.append(" from AB_VOUCHERINFO_VICE c ");
        sql.append("  join AB_VOUCHERINFO z on z.id=c.vid  and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" left join AB_ACCRECORDS l on c.id = l.vvid ");
        sql.append(" left join ab_pool_dtl pd on l.PD_ID = pd.id ");
        sql.append(" where c.valid_sign='" + AccebillConstant.AB_BILL_VALID_SIGN_1 + "'  ");
        sql.append(" and ((z.bill_flag ='" + AccebillConstant.AB_BILL_FLAG_ELECT + "' ");
        sql.append(" and l.SEND_SIGN='" + AccebillConstant.AB_BILL_SEND_SIGN_Y + "' ");
        sql.append(" and l.valid_sign in ('" + AccebillConstant.AB_BILL_VALID_SIGN_2 + "','" + AccebillConstant.AB_BILL_VALID_SIGN_1 + "') ");
        sql.append(" and l.STATUS='" + AccebillConstant.AB_BILL_BUS_STATUS95 + "') ");
                //指令测试兼容票据池的票据明细指令
        sql.append(" or (l.PD_ID is not null and pd.status=95 ) ) ");
        sql.append(" and l.OPT_STATUS in('" +
                        AccebillConstant.AB_BILL_OPT_STATUS_1 + "','" +
                        AccebillConstant.AB_BILL_OPT_STATUS16 + "','" +
                        AccebillConstant.AB_BILL_OPT_STATUS17 + "','" +
                        AccebillConstant.AB_BILL_OPT_STATUS18
                        + "') ");
        sql.append(" and ").append(corpAuthSql).append(" ");
        Map<String, Object> params = new HashMap<>();
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillInstructionManageVO.class, null, false, params);
    }
}
