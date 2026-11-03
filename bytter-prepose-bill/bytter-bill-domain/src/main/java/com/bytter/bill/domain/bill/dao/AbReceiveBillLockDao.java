package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbReceiveBillLockVo;
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
 * @Description: 票据锁定/解锁
 * @date 2017-06-21 17:35:25
 */
@Repository
public class AbReceiveBillLockDao extends BaseDao<AbVoucherinfo, String> {
    /**
     * 加载票据锁定/解锁数据
     *
     * @param dataTablesInput 查询条件
     * @param corpAuthList     特殊管理权限单位
     * @return 返回列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbReceiveBillLock(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,z.bill_flag as billFlag,"
                + "z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,lo.rmk as reason,");
        sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,");
        sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.ENTER_STOCK AS enterStock,"
                + "c.CORP_ID as corpId,c.CORP_CODE as corpCode,c.CORP_NAME as corpIdName,c.valid_sign as lockSign,l.brief_id as briefId,l.send_sign as sendSign, ");
        sql.append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,");
        sql.append(" c.BUS_CORP_ID as busCorpId,c.BUS_CORP_NAME as busCorpName,c.BUS_PERSONNEL as busPersonnel, ");
        //		--前手
        sql.append("case ");
        sql.append("when l.brief_id='6001' then l.cn_accrecords ");
        sql.append("when bs.bus_type in (:bus_type_0) then l.rec_nm ");
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        sql.append("else c.cn_accrecords end as cnAccrecords ");
        //		--前手
        // 新一代票据 start
        sql.append(", z.new_stm_issuofppr_ind as newStmIssuofpprInd, z.blgrdbagcrcl_prmt_ind as blgrdbagcrclPrmtInd, z.sbbll_rng_strt_sn as sbbllRngStrtSn")
                .append(" , z.sbbll_rng_end_sn as sbbllRngEndSn, z.bl_std_amt as blStdAmt, z.bl_rmtr_unn_id_no as blRmtrUnnIdNo ,z.bill_money as transMoney")
                .append(" ,c.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo,c.BATCH_NO AS batchNo ");
        //新一代票据 end
        sql.append(" from AB_VOUCHERINFO_VICE c ");
        sql.append(" left join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ");
        sql.append(" left join AB_ACCRECORDS l on c.id=l.vvid ");
        sql.append(" left join AB_ACCRECORDS lo on l.id=lo.lastid "
                + "and lo.brief_id = :LO_BRIEF_ID "
                + "and lo.valid_sign=:LO_VALID_SIGN ");
        params.put("LO_BRIEF_ID", AccebillConstant.BRIEF_STATUS_6007);
        params.put("LO_VALID_SIGN", AccebillConstant.AB_BILL_VALID_SIGN_1);
        sql.append(" left join ab_brief_status bs on bs.status=l.brief_id ");
        sql.append(" where ");
        sql.append(" c.box_status = :C_BOX_STATUS ");
        sql.append(" and l.valid_sign = :L_VALID_SIGN ");
        sql.append(" and l.STATUS = :L_STATUS ");
        sql.append(" and l.opt_status = :L_OPT_STATUS ");
        sql.append(" and ((c.valid_sign in (:C_VALID_SIGN) ");
        params.put("C_BOX_STATUS", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("L_VALID_SIGN", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("L_STATUS", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("L_OPT_STATUS", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("C_VALID_SIGN", new String[]{AccebillConstant.AB_BILL_VALID_SIGN_0,
                AccebillConstant.AB_BILL_VALID_SIGN_1
        });
        //线上电票业务处理
        sql.append(" and (( ");
        sql.append(" l.brief_id in (:L_BRIEF_ID)  ");
        sql.append(" and z.bill_flag = :Z_BILL_FLAG "
                + " and l.send_sign=:L_SEND_SIGN ");
        sql.append(" ) ");
        params.put("L_BRIEF_ID", new String[]{
                AccebillConstant.BRIEF_STATUS_9003,
                AccebillConstant.BRIEF_STATUS_9010,
                AccebillConstant.BRIEF_STATUS_9012,
                AccebillConstant.BRIEF_STATUS_9018,
                AccebillConstant.BRIEF_STATUS_9019
        });
        params.put("Z_BILL_FLAG", AccebillConstant.AB_BILL_FLAG_ELECT);
        params.put("L_SEND_SIGN", AccebillConstant.AB_BILL_SEND_SIGN_Y);
        //纸票和线下票据业务
        sql.append(" or ");
        sql.append(" l.brief_id in (:L_BRIEF_ID2) ");
        sql.append(" ) )");
        params.put("L_BRIEF_ID2", new String[]{
                AccebillConstant.BRIEF_STATUS_6001,
                AccebillConstant.BRIEF_STATUS_6011,
                AccebillConstant.BRIEF_STATUS_6019,
                AccebillConstant.BRIEF_STATUS_6212,
                AccebillConstant.BRIEF_STATUS_020,
                AccebillConstant.BRIEF_STATUS_021
        });
        //应付票据补录审批通过锁定的票据
        sql.append(" or ");
        sql.append(" (c.valid_sign = :C_VALID_SIGN2  and  (l.brief_id = :L_BRIEF_ID3 and z.bill_flag = :Z_BILL_FLAG2 ) ))");
        params.put("C_VALID_SIGN2", AccebillConstant.AB_BILL_VALID_SIGN_0);
        params.put("L_BRIEF_ID3", AccebillConstant.BRIEF_STATUS_6101);
        params.put("Z_BILL_FLAG2", AccebillConstant.AB_BILL_FLAG_PAPER);
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.APPLY_CORP_ID", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbReceiveBillLockVo.class, "", false, params);
    }
}
