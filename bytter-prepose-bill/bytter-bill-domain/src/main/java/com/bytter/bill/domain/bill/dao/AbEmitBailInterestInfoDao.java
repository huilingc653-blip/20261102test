package com.bytter.bill.domain.bill.dao;

import cn.hutool.core.date.DateUtil;
import com.bytter.bill.domain.bill.entity.AbEmitBailInterestInfo;
import com.bytter.bill.domain.bill.vo.AbEmitBailInterestInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Description 票据保证金利息维护dao
* @author zhous
* @date 2025/3/5 15:57
*/
@Repository
public class AbEmitBailInterestInfoDao extends BaseDao<AbEmitBailInterestInfo, String> {

    /**
    * @Description 表格获取数据
    * @author zhous
    * @date 2025/3/6 17:32
    */
    public DataTablesOutput<AbEmitBailInterestInfoVO> dataTable(DataTablesInput input,List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        Map<String, Object> params = new HashMap<>();
        sql.append(" ae.id,ae.corp_id as corpId,ae.corp_name as corpName,ae.bank_acc as bankAcc,ae.bank_type_id as bankTypeId,ae.bank_type_name as bankTypeName, ae.current_month as currentMonth,");
        sql.append(" ae.bill_money as billMoney,ae.bail_money as bailMoney,ae.due_settlement_amount as dueSettlementAmount,ae.actual_settlement_amount as actualSettlementAmount, ");
        sql.append(" ae.interest_margin as interestMargin,ae.status,ae.push_status as pushStatus,ae.record_source as recordSource, ");
        sql.append(" case when ae.update_by is not null then ae.update_by else ae.create_by end as lastOperator ");
        sql.append(" from ab_emit_bail_interest_info ae ");
        sql.append(" where 1=1 ");
        sql.append(" and ").append(SqlUtil.getInSql(corpAuthList, "ae.corp_id", true));
        DataTablesOutput dataOutInfo = this.getDynamicBySQL(input, sql.toString(), AbEmitBailInterestInfoVO.class, "ae", false, params);
        return dataOutInfo;
    }

    /**
    * @Description 获取开票的数据
    * @author zhous
    * @date 2025/3/6 17:32
    */
    public List<AbEmitBailInterestInfo> getOpenBillInfo(String currentMonth) {
        StringBuilder sql = new StringBuilder(" select ");
        sql.append(" a.bankAcc,a.corpId,a.corpName,a.bankTypeId,a.bankTypeName,sum(a.billMoney) as billMoney,sum(a.bailMoney) as bailMoney,sum(bt_nvl_num(b.amt,0)) as actualSettlementAmount from( ");
        sql.append(" select avv.BANK_ACC as bankAcc,avv.corp_id as corpId,sc.name as corpName,bbt.id as bankTypeId,bbt.type_name as bankTypeName ,bt_nvl_num(sum(av.bill_money),0) as billMoney,");
        sql.append(" bt_nvl_num(sum(avv.bail_money),0) as bailMoney,avv.claim_id ");
        sql.append(" from ab_voucherinfo_vice avv ");
        sql.append(" left join ab_voucherinfo av on av.id =avv.vid  and (av.OLD_DRAFT_ID is null or av.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" left join ab_accrecords aa on aa.vvid = avv.id ");
        sql.append(" left join bt_bank_acc bba on bba.bank_acc = avv.bank_acc ");
        sql.append(" left join bt_bank_type bbt on bba.bank_type_id = bbt.id ");
        sql.append(" left join sys_corp sc on avv.corp_id = sc.id ");
        sql.append(" where avv.type =:type0 and avv.valid_sign=:validSign1 and av.orig_no is not null and av.orig_no <>'' ");
        sql.append(" and aa.brief_id =:briefId001 and aa.OPT_STATUS =:optStatus19 and aa.STATUS =:status95 ");
        sql.append(" and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.brief_id =:briefId004 and ba.opt_status=:optStatus19 and ba.status=:status95 ) ");
        sql.append(" and bba.valid_sign=:validSign1 and bba.status=:status95 and bba.ELECTRIC_BILL ='1'  ");
        sql.append(" and av.END_DATE >= bt_to_date(:startMonthDate,'yyyy-MM-dd hh24:mi:ss') and  av.END_DATE <= bt_to_date(:endMonthDate,'yyyy-MM-dd hh24:mi:ss')  ");
        sql.append(" and not exists (select 1 from ab_emit_bail_interest_info ae where  ae.bank_acc = avv.BANK_ACC and ae.current_month=:currentMonth and ae.status =:bailStatus and ae.RECORD_SOURCE =:source )  ");
        sql.append(" group by avv.BANK_ACC,avv.corp_id,sc.name,bbt.id,bbt.type_name,avv.claim_id ) a  ");
        sql.append(" left join bis_acc_dtl b on a.claim_id =b.claim_id  ");
        sql.append(" left join ab_claim_middle acm on a.claim_id =acm.id ");
        sql.append(" where (a.claim_id is null or (a.claim_id is not null && acm.claim_status=:claimStatus95)) group by a.bankAcc,a.corpId,a.corpName,a.bankTypeId,a.bankTypeName ");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("claimStatus95", AccebillConstant.BILL_CLAIM_STATUS95);
        params.put("type0", AccebillConstant.AB_VOUCHERINFO_OP_TYPE0);
        params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("briefId001", AccebillConstant.BRIEF_STATUS_001);
        params.put("briefId004", AccebillConstant.BRIEF_STATUS_004);
        params.put("optStatus19", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("status95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("currentMonth", currentMonth);
        params.put("bailStatus", com.bytter.bill.api.bill.constant.AccebillConstant.BAIL_INTEREST_INFO_STATUS_1);
        params.put("source", com.bytter.bill.api.bill.constant.AccebillConstant.AUTO_GENERATION);
        if (StringUtils.isNotBlank(currentMonth)) {
            params.put("startMonthDate",DateUtil.format(DateUtil.beginOfMonth(DateUtil.parse(currentMonth, "yyyy-MM")),"yyyy-MM-dd HH:mm:ss"));
            params.put("endMonthDate",DateUtil.format(DateUtil.endOfMonth(DateUtil.parse(currentMonth, "yyyy-MM")),"yyyy-MM-dd HH:mm:ss"));
        }
        List<AbEmitBailInterestInfo> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbEmitBailInterestInfo.class);
        return listInfo;
    }

    /**
    * @Description 根据账号和当前月份删除
    * @author zhous
    * @date 2025/3/6 17:32
    */
    public void deleteOldBailInterestInfo(String bankAcc, String currentMonth) {
        String hql = " delete from AbEmitBailInterestInfo where bankAcc = :bankAcc and currentMonth=:currentMonth and status =:status0 and recordSource=:source ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bankAcc", bankAcc);
        params.put("currentMonth", currentMonth);
        params.put("status0", com.bytter.bill.api.bill.constant.AccebillConstant.BAIL_INTEREST_INFO_STATUS_0);
        params.put("source", com.bytter.bill.api.bill.constant.AccebillConstant.AUTO_GENERATION);
        this.batchExecute(hql, params);
    }

    /**
    * @Description 根据ids更新
    * @author zhous
    * @date 2025/3/6 17:31
    */
    public void updateByIds(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update ab_emit_bail_interest_info set  update_by=:updateBy , update_date=:updateDate ");
        if(params.containsKey("status")){
            sql.append(" , status = :status ");
        }
        if(params.containsKey("pushStatus")){
            sql.append(" , push_status = :pushStatus ");
        }
        sql.append(" where 1=1 and id in (:ids) ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
