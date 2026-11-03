package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.vo.AbBillEndorseVo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
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
 * 背书请求应答dao
 *
 * @author ouyj
 */
@Repository
public class AbBillEndorseResponseDao extends BaseDao<AbVoucherinfo, String> {
    /**
     * 查询sql
     *
     * @param params 参数
     * @return 查询sql
     */
    private String getQuerySQL(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("select c.bank_Acc as bankAcc,c.TYPE_NAME as bankTypeName,c.bank_Name as bankName,c.BANK_TYPE_ID as bankTypeId,");
        sql.append(" z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,z.bill_flag as billFlag,z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,");
        sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,");
        sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.ENTER_STOCK AS enterStock,c.corp_id as corpId,c.corp_code as corpCode,c.corp_name as corpIdName,");
        sql.append(" (case c.back_brief_id when :briefId014 then -1 else l.status end) as status, l.brief_id as briefId, ");
        sql.append(" (case l.status when " + AccebillConstant.AB_BILL_BUS_STATUS11
                + " then 1 when " + AccebillConstant.AB_BILL_BUS_STATUS94
                + " then 1 else 0 end) as checkboxHide,l.send_sign as sendSign,");
        sql.append("ba.num as attachmentNum,ba.attachment_id as attachmentId,");
        sql.append(" c.APPLY_CORP_ID as applyCorpId,c.apply_corp_name as applyCorpName,c.apply_corp_code as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_CORP_NAME as busCorpName,c.BUS_PERSONNEL as busPersonnel, ");
        sql.append(" c.CN_ACCRECORDS as cnAccrecords, l.serial_no as serialNo");
        sql.append(" from ab_voucherinfo_vice c");
        sql.append(" left join ab_voucherinfo z on z.id=c.vid");
        sql.append(" left join ab_accrecords l on c.id=l.vvid");
//        sql.append(" left join bt_bank_acc acc on acc.bank_acc =c.bank_acc");
//        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        // 同一笔流水可能挂多条背书申请附件，只取其中 id 最大的一条，避免一条流水被查成多行。
        // 这里用 not exists 过滤，不能写成 on ba.id = (select max(...))：
        // Oracle 不允许在 left join 的 on 条件里外连接子查询（ORA-01799）
        sql.append(" left join ab_bill_attachment ba on ba.acid in (l.id,l.lastid) and ba.brief_id='" + AccebillConstant.BRIEF_STATUS_010 + "'");
        sql.append(" and not exists (select 1 from ab_bill_attachment b2 where b2.acid in (l.id,l.lastid) and b2.brief_id='" + AccebillConstant.BRIEF_STATUS_010 + "' and b2.id > ba.id) ");
        sql.append(" where ");
        sql.append(" ((l.brief_id = :briefId013 and l.status = :status95 and l.valid_sign = :validSign1) ");
        sql.append(" or (l.brief_id = :briefId014 and l.status in (:statusArr))) ");
        params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("briefId013", AccebillConstant.BRIEF_STATUS_013);
        params.put("briefId014", AccebillConstant.BRIEF_STATUS_014);
        params.put("status95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("statusArr", new Integer[]{AccebillConstant.AB_BILL_BUS_STATUS_1, AccebillConstant.AB_BILL_BUS_STATUS11});
        return sql.toString();
    }
    /**
     * 查询背书请求应答列表数据
     *
     * @param dataTablesInput 前端传参
     * @param corpAuthList    当前用户单位
     * @return 前端列表数据
     */
    public DataTablesOutput<AbBillEndorseVo> listAbBillEndorseResponse(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>(8);
        StringBuilder sql = new StringBuilder(getQuerySQL(params));
        // 条件查询打回数据
        if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS_1)
                .equals(dataTablesInput.getFormdata().get("eqi_status"))) {
            sql.append(" and c.back_brief_id =:backBriefId ");
            dataTablesInput.getFormdata().remove("eqi_status");
            params.put("backBriefId", AccebillConstant.BRIEF_STATUS_013);
        }
        if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS95)
                .equals(dataTablesInput.getFormdata().get("eqi_status"))) {
            sql.append(" and (c.back_brief_id is null or c.back_brief_id = '') ");
        }
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.apply_corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillEndorseVo.class, "", false, params);
    }
    /**
     * 查询背书请求应答审批列表数据
     *
     * @param dataTablesInput 前端传参
     * @param businessList    id集合
     * @return 前端列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, List<String> businessList) {
        Map<String, Object> param = new HashMap<>();
        StringBuilder sql = new StringBuilder("select c.TYPE_NAME as bankTypeName,c.BANK_TYPE_ID as bankTypeId,");
        sql.append(" z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,"
                + "z.bill_flag as billFlag,z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,");
        sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,");
        sql.append(" z.bill_status as billStatus,l.inside_sing as insideSing,c.ENTER_STOCK AS enterStock,l.bankendrsmtmk,"
                + "c.corp_id as corpId,c.corp_code as corpCode,c.corp_name as corpIdName,l.brief_id as briefId,"
                + "l.rec_nm as recNm,l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,l.rmk as rmk, l.is_endorse as isEndorse,");
        sql.append("(case  when l.create_by is not null then BT_CONCAT4('(',l.create_by,')',l.create_name) else null end) as applyName,");
        sql.append(" c.APPLY_CORP_ID as applyCorpId,c.apply_corp_name as applyCorpName,c.apply_corp_code as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_CORP_NAME as busCorpName,c.BUS_PERSONNEL as busPersonnel ");
        sql.append(" from ab_voucherinfo_vice c");
        sql.append(" left join ab_voucherinfo z on z.id=c.vid");
        sql.append(" left join ab_accrecords l on c.id=l.vvid");
//        sql.append(" left join bt_bank_acc acc on acc.bank_acc =c.bank_acc");
//        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" where 1=1 ");
        String applyName = dataTablesInput.getFormdata().get("applyName").toString();
        if (StringUtils.isNotEmpty(applyName)) {
            sql.append(" and BT_CONCAT4('(',l.create_by,')',l.create_name) like BT_CONCAT3('%',:applyName,'%')");
            param.put("applyName", applyName);
        }
        dataTablesInput.getFormdata().remove("applyName");
        String businessSql = SqlUtil.getInSqlByParams(businessList, "l.id", param);
        sql.append(" and ").append(businessSql).append(" ");
        return this.getDynamicBySQL(dataTablesInput, sql.toString(), AbBillEndorseVo.class, "", false, param);
    }
}
