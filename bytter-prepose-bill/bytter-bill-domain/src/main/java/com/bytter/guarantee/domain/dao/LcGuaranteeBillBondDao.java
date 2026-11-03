package com.bytter.guarantee.domain.dao;

import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.dto.CrBillBondRateDTO;
import com.bytter.guarantee.dto.LcEnrolDTO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class LcGuaranteeBillBondDao extends BaseDao {

    public DataTablesOutput findAbVoucherInfoId(String corpId, String scoringStartDate, String scoringEndDate, String bankType, String bankId) {

        StringBuilder sql = new StringBuilder();
        sql.append("select * from ( ");
        sql.append("select vv.id as vvid, banktype.id as bankTypeId,vv.batch_no as batchNo, vv.BANK_CODE  as bankCode ,v.start_date as startDate ");
        sql.append(", a.status as busStatus,a.brief_id as briefId ");

        sql.append(" from ab_voucherinfo_vice vv  left join ab_voucherinfo v on vv.vid = v.id and(v.OLD_DRAFT_ID is null or v.OLD_DRAFT_ID not like '%-del')");
        sql.append(" left join ab_accrecords a on a.vid = v.id and a.vvid = vv.id ");
        sql.append(" left join ab_discount d on d.record_id = a.id ");
        sql.append(" left join bt_bank_acc acc on acc.bank_acc = vv.bank_acc ");
        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" left join ab_brief_status bs on bs.status = a.brief_id ");
        sql.append(" left join sys_corp c on c.id = vv.corp_id");
        sql.append(" left join sys_corp s2 on vv.apply_corp_id = s2.id ");
        sql.append(" left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID = a.brief_id ");
        sql.append(" and(vv.source = sd.source or(vv.source is null and a.bill_source = sd.source) ");
        sql.append("or(vv.source is null and a.bill_source is null and sd.source is null)) ");
        sql.append(" where a.COPY_FLAG in ('0', '2') and(a.pd_id is null or a.recid is not null) ");
        sql.append(" and a.id is not null ");
        if (StringUtils.isNotBlank(corpId)){
            sql.append(" and vv.APPLY_CORP_ID = '" + corpId + "' ");
        }

        if (StringUtils.isNotBlank(scoringStartDate)){
            sql.append(" and v.start_date >= to_date(:scoringStartDate, 'yyyy-MM-dd') ");
        }

        if (StringUtils.isNotBlank(scoringEndDate)){
            sql.append(" and v.start_date <= to_date(:scoringEndDate, 'yyyy-MM-dd') ");
        }
        sql.append(" ) bus  where  bus.briefId ='001' and bus.busStatus = '95' ");
        if (CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE.equals(bankType)){
            sql.append(" and bus.banktypeId = '" + bankId + "' ");

        }
        if (CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO.equals(bankType)){
            sql.append(" and bus.bankCode = '" + bankId + "' ");

        }

        sql.append(" order by bus.batchNo desc ");
        Map<String, Object> param = Maps.newHashMap();
        param.put("scoringStartDate",scoringStartDate);
        param.put("scoringEndDate",scoringEndDate);
        return  getDynamicBySQLForBill(new DataTablesInput(), sql.toString(), CrBillBondRateDTO.class, "bus", true, param);
    }
//lc_enrol
    public List<LcEnrolDTO> findLcEnrol(String corpId, String scoringStartDate, String scoringEndDate, String bankType, String bankId){

        StringBuilder sql = new StringBuilder();
        sql.append("select lc.id, lc.status, lc.corp_id as corpId, lc.is_spill_over as isSpillOver, lc.spill_over_rate as spillOverRate ");
        sql.append(" , userud_.USERNAME as updateName, bb.bank_name as issuingBank, lc.re_bank as reBank, lc.lc_inter as lcInter, sc.name as applicant");
        sql.append(" , lc.beneficiary as beneficiary, lc.credit_no as creditNo, lc.re_credit_no as reCreditNo, lc.contract_no as contractNo");
        sql.append(" , lc.issuing_date as issuingDate , lc.expiry_date as expiryDate, lc.issuing_cur_id as issuingCurId");
        sql.append(", bt_nvl_num(lc.credit_money, 0) as creditMoney,lc.delay_dates as delayDates");
        sql.append("  from lc_enrol lc ");
        sql.append(" left join bt_bank_acc bb on lc.applicant_acc = bb.bank_acc");
        sql.append(" left join sys_corp sc on lc.applicant = sc.id");
        sql.append(" left join BT_CURRENCY bc on lc.issuing_cur_id = bc.id ");
        sql.append(" left join sys_user userud_ on lc.update_by = userud_.LOGIN_NAME ");
        sql.append(" where lc.status >0 and lc.lc_type = '0'");
        if (StringUtils.isNotBlank(scoringStartDate)){
            sql.append(" and lc.issuing_date >= to_date(:scoringStartDate, 'yyyy-MM-dd') ");
        }

        if (StringUtils.isNotBlank(scoringEndDate)){
            sql.append(" and lc.issuing_date <= to_date(:scoringEndDate, 'yyyy-MM-dd') ");
        }

        if (CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE.equals(bankType)){
            sql.append(" and bb.bank_type_id = '" + bankId + "' ");

        }
        if (CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO.equals(bankType)){
            sql.append(" and lc.ISSUING_BANK = '" + bankId + "' ");
        }
        sql.append(" and lc.corp_id = '" + corpId + "' ");
        Map<String, Object> param = Maps.newHashMap();
        param.put("scoringStartDate",scoringStartDate);
        param.put("scoringEndDate",scoringEndDate);
        return listBySQLAliasToBean(sql.toString(), param, LcEnrolDTO.class);
    }


}
