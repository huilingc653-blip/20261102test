package com.bytter.bill.domain.bill.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.FinanceLoanDTO;
import com.bytter.bill.api.bill.dto.FinanceTypeLoanDTO;
import com.bytter.bill.domain.bill.entity.AbBriefStatus;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 票据查询Dao
 * @date 2017-06-21 17:35:25
 */
@Repository
public class AbBillQueryDao extends BaseDao<AbAccrecordsVO, String> {
    @DubboReference
    private IBisExcService iBisExcService;
    /**
     * 加载票据明细信息数据
     *
     * @param dataTablesInput 查询入参
     * @param corpAuthList    单位权限
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbBillQueryDtl(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select * from ( ");
        sql.append("select v.id as vid,v.payer_bank_name as payerBankName,vv.id as vvid,a.ID as id,a.recid as recid,vv.type,vv.TYPE_NAME as bankTypeName,");
        sql.append(" v.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,v.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,v.SBBLL_RNG_STRT_SN as sbbllRngStrtSn,v.SBBLL_RNG_END_SN as sbbllRngEndSn,");
        sql.append("vv.corp_id as corpId,BT_CONCAT4('(',vv.CORP_CODE,')',vv.CORP_NAME) as corpCodeAndName,vv.bank_acc as bankAcc,vv.BANK_TYPE_ID as bankTypeId,");
        sql.append("case when bs.bus_type ='6001' and a.bill_source='12' then '9018' ");
        sql.append("when bs.bus_type ='6001' and a.bill_source='14' then '9011' ");
        sql.append("else bs.bus_type end as busType,");
        sql.append("sd.TYPE_CODE as source,v.orig_no as origNo,");
        sql.append("v.bill_flag as billFlag,v.bill_type as billType,");
//		sql.append("v.bill_money as billMoney,BT_CONCAT4('(',a.create_by,')',a.create_name) as applyName,");
        sql.append("v.bill_money as billMoney,");
        sql.append("(case ");
        sql.append("when a.create_by is not null then BT_CONCAT4('(',a.create_by,')',a.create_name) ");
        sql.append("else null end) as applyName,");
        sql.append("a.apply_date as applyDate,a.business_date as businessDate,");
        sql.append("v.start_date as startDate,v.end_date as endDate,d.interest,");
        sql.append("d.interest_rates as interestRates,v.payer,v.payee,");
        //--所属库存判断（如果是库存转移和取票暂存和打回审批中取老的enter_Stock_old字段审批通过取enter_Stock_new的字段）
        sql.append("case ");
        sql.append("when a.brief_id in (:enterStockByBriefId) and a.status=:enterStockByStatus then a.enter_Stock_new ");
        sql.append("else a.enter_Stock_old end as enterStock,");
        params.put("enterStockByBriefId", new String[]{AccebillConstant.BRIEF_STATUS_6005, AccebillConstant.BRIEF_STATUS_6006});
        params.put("enterStockByStatus", AccebillConstant.AB_BILL_BUS_STATUS95);
//		--前手
        sql.append("case ");
        sql.append("when a.brief_id='6001' then a.cn_accrecords ");
        sql.append("when bs.bus_type in (:bus_type_0) then a.rec_nm ");
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        sql.append("else '' end as cnAccrecords, ");
//    --后手
        sql.append("case ");
        sql.append("when bs.bus_type in (:bus_type_1) then a.rec_nm ");
        params.put("bus_type_1", new String[]{"001", "017", "002", "003", "010", "018", "019", "020", "011", "012", "022", "6201", "6203", "6004", "023"});
        sql.append("else '' end as rearAccrecords, ");
        sql.append("a.status as busStatus,a.opt_status as inoptStatus,a.send_sign as sendSign,vv.abs as abs,BT_NVL_CHAR(A.RMK,VV.RMK) as rmk, ");
        sql.append("a.return_Code as returnCode,a.return_Msg as returnMsg, ");
        //交票人或领票人
        sql.append("a.trader as trader,a.is_Autosign as isAutosign, ");
        sql.append(" vv.APPLY_CORP_ID as applyCorpId,vv.APPLY_CORP_NAME as applyCorpName,vv.APPLY_CORP_CODE as applyCorpCode,vv.BUS_CORP_ID as busCorpId,vv.BUS_PERSONNEL as busPersonnel ");
        sql.append("from ab_voucherinfo_vice vv ");
        sql.append(" join ab_voucherinfo v on vv.vid=v.id  and (v.OLD_DRAFT_ID is null or v.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" join ab_accrecords a on a.vid=v.id and a.vvid=vv.id and a.copy_flag in ('0','2') ");
        sql.append("left join ab_discount d on d.record_id=a.id ");
        sql.append("left join ab_brief_status bs on bs.status=a.brief_id ");
        sql.append("left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID=a.brief_id "
                + "and (vv.source=sd.source "
                + "or (vv.source is null and a.bill_source=sd.source) "
                + "or (vv.source is null and a.bill_source is null and sd.source is null )"
                + ") ");
        sql.append("where 1=1 ");
        sql.append("and (a.is_hide<>:is_hide_1 or a.is_hide is null) ");
        params.put("is_hide_1", AccebillConstant.AB_IS_HIDE_Y);
        sql.append("and a.status not in(:status) ");
        params.put("status", new Integer[]{AccebillConstant.AB_BILL_BUS_STATUS_2, AccebillConstant.AB_BILL_BUS_STATUS_3});
        sql.append("and a.opt_status not in(:opt_status) ");
        params.put("opt_status", new String[]{AccebillConstant.AB_BILL_OPT_STATUS_3, AccebillConstant.AB_BILL_OPT_STATUS_4});
        sql.append("and bs.bus_type is not null ");
        sql.append("and a.id is not null ");
        //票据业务排除票据池流水,但是票据池签收流水要显示
        sql.append(" and (a.pd_id is null or a.recid is not null) ");
        //用户满足对应查询权限的单位数据
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "vv.APPLY_CORP_ID", params);
        sql.append("and ").append(corpAuthSql).append(" ");
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (vv.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "vv.bank_acc", "bankAccs", params)).append(")");
        }
        sql.append(" ) dtl ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbAccrecordsVO.class, "dtl", true, params);
    }

    /**
     * 加载票据业务信息数据
     *
     * @param dataTablesInput 查询入参
     * @param corpAuthList    单位权限
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbBillQueryBus(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select * from ( ");
        sql.append("select v.id as vid,vv.id as vvid,a.ID as id,v.payer_bank_name as payerBankName,a.recid as recid,vv.type,banktype.type_name as bankTypeName,banktype.id as bankTypeId,");
        sql.append("vv.CORP_ID as corpId,BT_CONCAT4('(',c.code,')',c.name) as corpCodeAndName,vv.bank_acc as bankAcc,");
        sql.append("a.brief_id as briefId,sd.TYPE_CODE as source,v.orig_no as origNo,");
        sql.append("v.bill_flag as billFlag,v.bill_type as billType,");
        //--所属库存判断（如果是库存转移和取票暂存和打回审批中取老的enter_Stock_old字段审批通过取enter_Stock_new的字段）
        sql.append("case ");
        sql.append("when a.brief_id in (:enterStockByBriefId) and a.status=:enterStockByStatus then a.enter_Stock_new ");
        sql.append("else a.enter_Stock_old end as enterStock,");
        sql.append("v.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,v.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,v.SBBLL_RNG_STRT_SN as sbbllRngStrtSn,")
                .append("v.SBBLL_RNG_END_SN as sbbllRngEndSn,v.BL_STD_AMT as blStdAmt,v.EBILL_BL_CRCL_INDC as ebillBlCrclIndc,")
                .append("v.BL_BSN_TPCD as blBsnTpcd,vv.batch_no batchNo,vv.SOC_CODE socCode,vv.valid_Sign validSign, ");
        params.put("enterStockByBriefId", new String[]{AccebillConstant.BRIEF_STATUS_6005, AccebillConstant.BRIEF_STATUS_6006});
        params.put("enterStockByStatus", AccebillConstant.AB_BILL_BUS_STATUS95);
        sql.append("v.bill_money as billMoney,");
        sql.append("a.create_name as applyName,");
        sql.append("a.apply_date as applyDate,a.business_date as businessDate,");
        sql.append("v.start_date as startDate,v.end_date as endDate,d.interest,");
        sql.append("d.interest_rates as interestRates,v.payer,v.payee,");
//		--前手
        sql.append("case ");
        sql.append("when a.brief_id='6001' then a.cn_accrecords ");
        sql.append("when bs.bus_type in (:bus_type_0) then a.rec_nm ");
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        sql.append("else '' end as cnAccrecords, ");
//    --后手
        sql.append("case ");
        sql.append("when bs.bus_type in (:bus_type_1) then a.rec_nm ");
        params.put("bus_type_1", new String[]{"001", "017", "002", "003", "010", "018", "019", "020", "011", "012", "022", "6201", "6203", "6004", "023"});
        sql.append("else '' end as rearAccrecords, ");
        sql.append("a.status as busStatus,a.opt_status as inoptStatus,a.send_sign as sendSign, vv.abs as abs,BT_NVL_CHAR(A.RMK,VV.RMK) as rmk,a.rsn as rsn, ");
        sql.append("a.return_Code as returnCode,a.return_Msg as returnMsg, v.acceptor as acceptor, ");
        //交票人或领票人
        sql.append(" a.trader as trader,a.is_Autosign as isAutosign, ");
        sql.append(" vv.APPLY_CORP_ID as applyCorpId,s2.name as applyCorpName,s2.code as applyCorpCode,vv.BUS_CORP_ID as busCorpId,s3.name as busCorpName,vv.BUS_PERSONNEL as busPersonnel ");
        sql.append(" ,BT_NVL_CHAR(nb001.id,nb010.id) as payId,BT_NVL_CHAR(nb001.bursar_bill_no,nb010.bursar_bill_no) as bursarBillNo,BT_NVL_CHAR(nb001.bill_no,nb010.bill_no) as payNo,a.final_approver as finalApprover ");
        sql.append("from ab_voucherinfo_vice vv ");
        sql.append("left join ab_voucherinfo v on vv.vid=v.id ");
        sql.append("left join ab_accrecords a on a.vid=v.id and a.vvid=vv.id ");
        sql.append("left join ab_discount d on d.record_id=a.id ");
        sql.append("left join bt_bank_acc acc on acc.bank_acc = vv.bank_acc ");
        sql.append("left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append("left join ab_brief_status bs on bs.status=a.brief_id ");
        sql.append("left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID=a.brief_id "
                + "and (vv.source=sd.source "
                + "or (vv.source is null and a.bill_source=sd.source) "
                + "or (vv.source is null and a.bill_source is null and sd.source is null )"
                + ") ");
        sql.append(" left join nis_bill nbm on a.id = nbm.bill_id and a.brief_id='010' ");
//        sql.append(" left join nis_billhead nb on (nb.id = nbm.pay_id and a.brief_id='010') or (nb.id = vv.voucher_id and a.brief_id='001') ");
        sql.append(" left join nis_billhead nb010 on nb010.id = nbm.pay_id and a.brief_id='010' ");
        sql.append(" left join nis_billhead nb001 on nb001.id = vv.voucher_id and a.brief_id='001' ");
        sql.append(" left join sys_corp c on c.id=vv.corp_id ");
        sql.append(" left join sys_corp s2 on vv.apply_corp_id = s2.id ");
        sql.append(" left join sys_corp s3 on vv.BUS_CORP_ID = s3.id ");
        sql.append("where  a.COPY_FLAG in ('0','2') ");
        //票据业务排除票据池流水,但是票据池签收流水要显示
        sql.append(" and (a.pd_id is null or a.recid is not null) ");
        sql.append("and a.id is not null ");
        //用户满足对应查询权限的单位数据
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "vv.APPLY_CORP_ID", params);
        sql.append("and ").append(corpAuthSql).append(" ");
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (vv.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "vv.bank_acc", "bankAccs", params)).append(")");
        }
        String insideSing = String.valueOf(dataTablesInput.getFormdata().get("eqs_insideSing"));
        // 是否对内条件查询
        if (StringUtils.isNotEmpty(insideSing)) {
            if (AccebillConstant.AB_INSIDE_SING_Y.equals(insideSing)) {
                sql.append(" and ((vv.inside_sing=:insideSing and vv.type=0) or (vv.inside_sing=:insideSing and vv.type=1 and a.brief_id in (:briefId1)) or a.inside_sing=:insideSing ) ");
                params.put("briefId1", new String[]{AccebillConstant.BRIEF_STATUS_9003,
                        AccebillConstant.BRIEF_STATUS_9010,
                        AccebillConstant.BRIEF_STATUS_9012,
                        AccebillConstant.BRIEF_STATUS_9019,
                        AccebillConstant.BRIEF_STATUS_6019,
                        AccebillConstant.BRIEF_STATUS_6001,
                        AccebillConstant.BRIEF_STATUS_6212});
            } else if (AccebillConstant.AB_INSIDE_SING_N.equals(insideSing)) {
                sql.append(" and ((vv.inside_sing=:insideSing and vv.type=0) or (vv.type=1 and (vv.inside_sing=:insideSing or vv.inside_sing is null) and (a.inside_sing=:insideSing or a.inside_sing is null))) ");
            }
            params.put("insideSing", insideSing);
            dataTablesInput.getFormdata().remove("eqs_insideSing");
        }
        if (dataTablesInput.getFormdata().containsKey("bursarBillNoList")) {
            sql.append(" and ").append(SqlUtil.getInSqlByParams((List<String>) dataTablesInput.getFormdata().get("bursarBillNoList"), "nb.bursar_bill_no", "bursarBillNoList", params));
        }
        sql.append(" ) bus ");
        List<Order> orderList = dataTablesInput.getOrder();
        if (!CollectionUtils.isEmpty(orderList)) {
            for (Order order : orderList) {
                if (StringUtils.isEmpty(order.getDir())) {
                    order.setDir("desc");
                }
            }
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbAccrecordsVO.class, "bus", true, params);
    }

    /**
     * 加载票据贴现信息数据
     *
     * @param dataTablesInput 查询入参
     * @param corpAuthList    单位权限
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbBillQueryDiscountInfo(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select * from ( ");
        sql.append("select v.id as vid,vv.id as vvid,a.ID as id,a.recid as recid,vv.type,");
        sql.append("vv.CORP_ID as corpId,BT_CONCAT4('(',vv.CORP_CODE,')',vv.CORP_NAME) as corpCodeAndName,vv.bank_acc as bankAcc,");
        sql.append("a.brief_id as briefId,sd.TYPE_CODE as source,v.orig_no as origNo,");
        sql.append("v.bill_flag as billFlag,v.bill_type as billType,a.enter_Stock_old as enterStock,");
        sql.append("v.bill_money as billMoney,");
        sql.append("(case ");
        sql.append("when a.create_by is not null then BT_CONCAT4('(',a.create_by,')',a.create_name) ");
        sql.append("else null end) as applyName,");
        sql.append("a.apply_date as applyDate,a.business_date as businessDate,");
        sql.append("v.start_date as startDate,v.end_date as endDate,d.interest,");
        sql.append("d.interest_rates as interestRates,v.payer,v.payee,a.inside_Sing as insideSing,d.real_money as realMoney,");
        sql.append("d.rpdmk as rpdmk,d.sttlmmk as sttlmmk,d.aoaccninf_Id as aoaccninfId,d.discount_date as discountDate,");
        sql.append("v.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,v.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,v.SBBLL_RNG_STRT_SN as sbbllRngStrtSn,")
                .append("v.SBBLL_RNG_END_SN as sbbllRngEndSn,v.BL_STD_AMT as blStdAmt,v.EBILL_BL_CRCL_INDC as ebillBlCrclIndc,")
                .append("v.BL_BSN_TPCD as blBsnTpcd,vv.batch_no batchNo,vv.SOC_CODE socCode,vv.valid_Sign validSign, ");
//		--前手
        sql.append("case ");
        sql.append("when a.brief_id='6001' then a.cn_accrecords ");
        sql.append("when bs.bus_type in (:bus_type_0) then a.rec_nm ");
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        sql.append("else '' end as cnAccrecords, ");
//    --后手
        sql.append("case ");
        sql.append("when bs.bus_type in (:bus_type_1) then a.rec_nm ");
        params.put("bus_type_1", new String[]{"001", "017", "002", "003", "010", "018", "019", "020", "011", "012", "022", "6201", "6203", "6004", "023"});
        sql.append("else '' end as rearAccrecords, ");
        sql.append("a.status as busStatus,a.opt_status as inoptStatus,a.send_sign as sendSign, ");
        sql.append(" vv.APPLY_CORP_ID as applyCorpId,vv.APPLY_CORP_NAME as applyCorpName,vv.APPLY_CORP_CODE as applyCorpCode,vv.BUS_CORP_ID as busCorpId,vv.BUS_PERSONNEL as busPersonnel ");
        sql.append("from ab_voucherinfo_vice vv ");
        sql.append(" join ab_voucherinfo v on vv.vid=v.id  and (v.OLD_DRAFT_ID is null or v.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" join ab_accrecords a on a.vid=v.id and a.vvid=vv.id and a.COPY_FLAG in ('0','2') ");
        sql.append("left join ab_discount d on d.record_id=a.id ");
        sql.append("left join ab_brief_status bs on bs.status=a.brief_id ");
        sql.append("left join sys_corp c on c.id=vv.corp_id ");
        sql.append("left join sys_corp s2 on vv.apply_corp_id = s2.id ");
        sql.append("left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID=a.brief_id "
                + "and (vv.source=sd.source "
                + "or (vv.source is null and a.bill_source=sd.source) "
                + "or (vv.source is null and a.bill_source is null and sd.source is null )"
                + ") ");
//				+ "and BT_NVL_CHAR(BT_NVL_CHAR(vv.source,a.bill_source),:source_1)=BT_NVL_CHAR(sd.source,:source_2) ");
        sql.append("where 1=1 ");
        sql.append("and a.id is not null ");
        sql.append("and vv.valid_sign=:vv_validSign ");
        params.put("vv_validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
        sql.append("and a.status =:status ");
        params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
        sql.append("and a.opt_status=:opt_status ");
        params.put("opt_status", AccebillConstant.AB_BILL_OPT_STATUS19);
        sql.append("and a.brief_id=:brief_id ");
        params.put("brief_id", AccebillConstant.BRIEF_STATUS_011);
        //票据业务排除票据池流水
        sql.append(" and a.pd_id is null ");
        //用户满足对应查询权限的单位数据
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.id", params);
        sql.append("and ").append(corpAuthSql).append(" ")
                .append(" ) bus ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbAccrecordsVO.class, "bus", true, params);
    }

    /**
     * 查询所有业务类型
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AbBriefStatus> listBriefStatusInfo(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("from AbBriefStatus where 1=1 ");
        if (params != null) {
            if (params.containsKey("notStatus")) {
                sql.append(" and status !=:notStatus");
            }
        }
        return this.listByHQL(sql.toString(), params);
    }

    /**
     * 获取单位票据统计表数据
     *
     * @param vo               查询条件
     * @param busTypes         业务类型
     * @param billTypeDictList 票据类型字典信息
     * @return 返回列表数据
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBillUnitLstatisticsInfo(AbAccrecordsVO vo, String[] busTypes, String[] allBusTypes, List<DictDTO> billTypeDictList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("paramsOptStatusOK", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("paramsStatusOK", new Integer[]{AccebillConstant.AB_BILL_BUS_STATUS95, AccebillConstant.AB_BILL_BUS_STATUS100});
        params.put("paramsBillFlagZP", AccebillConstant.AB_BILL_FLAG_PAPER);
        params.put("paramsBillFlagDP", AccebillConstant.AB_BILL_FLAG_ELECT);
        params.put("poolStatus0And3", new String[]{
                AccebillConstant.POOL_STATUS_0,
                AccebillConstant.POOL_STATUS_3
        });
        params.put("poolStatus1And2", new String[]{
                AccebillConstant.POOL_STATUS_1,
                AccebillConstant.POOL_STATUS_2
        });
        sql.append("select ");
        sql.append("info.busType, ");
        //总计数据
        sql.append("count(*) zCount, ");
        sql.append("sum(case when (info.optStatus<>:paramsOptStatusOK or info.status not in(:paramsStatusOK) ) then 1 else 0 end) zztCount, ");
        sql.append("sum(info.billMoney) zMoney, ");
        //在池总笔数
        sql.append("SUM(CASE WHEN INFO.POOLSTATUS in (:poolStatus1And2) THEN 1 ELSE 0 END) ZCCOUNT, ");
        //非在池总笔数
        sql.append("SUM(CASE WHEN (INFO.POOLSTATUS in (:poolStatus0And3) or INFO.POOLSTATUS is null ) THEN 1 ELSE 0 END) FZCCOUNT, ");
        //在池金额
        sql.append("SUM(CASE WHEN INFO.POOLSTATUS in (:poolStatus1And2) THEN INFO.BILLMONEY ELSE 0 END) ZCMONEY,");
        //非在池金额
        sql.append("SUM(CASE WHEN (INFO.POOLSTATUS in (:poolStatus0And3) or INFO.POOLSTATUS is null ) THEN INFO.BILLMONEY ELSE 0 END) FZCMONEY,");
        //纸票合计
        sql.append("SUM(CASE WHEN info.billFlag=:paramsBillFlagZP and INFO.POOLSTATUS in (:poolStatus1And2) THEN 1 ELSE 0 END) ZPZCCOUNT, ");
        sql.append("SUM(CASE WHEN info.billFlag=:paramsBillFlagZP and (INFO.POOLSTATUS in (:poolStatus0And3) or INFO.POOLSTATUS is null ) THEN 1 ELSE 0 END) ZPFZCCOUNT, ");
        sql.append("SUM(CASE WHEN info.billFlag=:paramsBillFlagZP and INFO.POOLSTATUS in (:poolStatus1And2) THEN INFO.BILLMONEY ELSE 0 END) ZPZCMONEY,");
        sql.append("SUM(CASE WHEN info.billFlag=:paramsBillFlagZP and (INFO.POOLSTATUS in (:poolStatus0And3) or INFO.POOLSTATUS is null ) THEN INFO.BILLMONEY ELSE 0 END) ZPFZCMONEY,");
        sql.append("sum(case when info.billFlag=:paramsBillFlagZP then 1 else 0 end) zpCount, ");
        sql.append("sum(case when info.billFlag=:paramsBillFlagZP and (info.optStatus<>:paramsOptStatusOK or info.status not in(:paramsStatusOK) ) then 1 else 0 end) zpztCount, ");
        sql.append("sum(case when info.billFlag=:paramsBillFlagZP then info.billMoney else 0 end) zpMoney, ");
        //电票合计
        sql.append("SUM(CASE WHEN info.billFlag=:paramsBillFlagDP and INFO.POOLSTATUS in (:poolStatus1And2) THEN 1 ELSE 0 END) DPZCCOUNT, ");
        sql.append("SUM(CASE WHEN info.billFlag=:paramsBillFlagDP and (INFO.POOLSTATUS in (:poolStatus0And3) or INFO.POOLSTATUS is null ) THEN 1 ELSE 0 END) DPFZCCOUNT, ");
        sql.append("SUM(CASE WHEN info.billFlag=:paramsBillFlagDP and INFO.POOLSTATUS in (:poolStatus1And2) THEN INFO.BILLMONEY ELSE 0 END) DPZCMONEY,");
        sql.append("SUM(CASE WHEN info.billFlag=:paramsBillFlagDP and (INFO.POOLSTATUS in (:poolStatus0And3) or INFO.POOLSTATUS is null ) THEN INFO.BILLMONEY ELSE 0 END) DPFZCMONEY,");
        sql.append("sum(case when info.billFlag=:paramsBillFlagDP then 1 else 0 end) dpCount, ");
        sql.append("sum(case when info.billFlag=:paramsBillFlagDP and (info.optStatus<>:paramsOptStatusOK or info.status not in(:paramsStatusOK) ) then 1 else 0 end) dpztCount, ");
        sql.append("sum(case when info.billFlag=:paramsBillFlagDP then info.billMoney else 0 end) dpMoney ");
        for (DictDTO billType : billTypeDictList) {
            String paramsKey = "paramsBillType" + billType.getCode();
            //纸票银票合计
            sql.append(",sum(case when info.billFlag=:paramsBillFlagZP and info.billType=:").append(paramsKey).append(" and INFO.POOLSTATUS in (:poolStatus1And2) THEN 1 else 0 end) zp").append(billType.getCode()).append("zcCount ");
            sql.append(",sum(case when info.billFlag=:paramsBillFlagZP and info.billType=:").append(paramsKey).append(" and (INFO.POOLSTATUS in (:poolStatus0And3) or INFO.POOLSTATUS is null ) then 1 else 0 end) zp").append(billType.getCode()).append("fzcCount ");
            sql.append(",sum(case when info.billFlag=:paramsBillFlagZP and info.billType=:").append(paramsKey).append(" and INFO.POOLSTATUS in (:poolStatus1And2) THEN info.billMoney else 0 end) zp").append(billType.getCode()).append("zcMoney ");
            sql.append(",sum(case when info.billFlag=:paramsBillFlagZP and info.billType=:").append(paramsKey).append(" and (INFO.POOLSTATUS in (:poolStatus0And3) or INFO.POOLSTATUS is null ) then info.billMoney else 0 end) zp").append(billType.getCode()).append("fzcmoney ");
            sql.append(",sum(case when info.billFlag=:paramsBillFlagZP and info.billType=:").append(paramsKey).append(" then 1 else 0 end) zp").append(billType.getCode()).append("Count ");
            sql.append(",sum(case when info.billFlag=:paramsBillFlagZP and info.billType=:").append(paramsKey).append(" and (info.optStatus<>:paramsOptStatusOK or info.status not in(:paramsStatusOK) ) then 1 else 0 end) zp").append(billType.getCode()).append("ztCount ");
            sql.append(",sum(case when info.billFlag=:paramsBillFlagZP and info.billType=:").append(paramsKey).append(" then info.billMoney else 0 end) zp").append(billType.getCode()).append("Money ");
            //电票银票合计
            sql.append(",sum(case when info.billFlag=:paramsBillFlagDP and info.billType=:").append(paramsKey).append(" and INFO.POOLSTATUS in (:poolStatus1And2) THEN 1 else 0 end) dp").append(billType.getCode()).append("zcCount ");
            sql.append(",sum(case when info.billFlag=:paramsBillFlagDP and info.billType=:").append(paramsKey).append(" and (INFO.POOLSTATUS in (:poolStatus0And3) or INFO.POOLSTATUS is null ) then 1 else 0 end) dp").append(billType.getCode()).append("fzcCount ");
            sql.append(",sum(case when info.billFlag=:paramsBillFlagDP and info.billType=:").append(paramsKey).append(" and INFO.POOLSTATUS in (:poolStatus1And2) THEN info.billMoney else 0 end) dp").append(billType.getCode()).append("zcMoney ");
            sql.append(",sum(case when info.billFlag=:paramsBillFlagDP and info.billType=:").append(paramsKey).append(" and (INFO.POOLSTATUS in (:poolStatus0And3) or INFO.POOLSTATUS is null ) then info.billMoney else 0 end) dp").append(billType.getCode()).append("fzcmoney ");
            sql.append(",sum(case when info.billFlag=:paramsBillFlagDP and info.billType=:").append(paramsKey).append(" then 1 else 0 end) dp").append(billType.getCode()).append("Count ");
            sql.append(",sum(case when info.billFlag=:paramsBillFlagDP and info.billType=:").append(paramsKey).append(" and (info.optStatus<>:paramsOptStatusOK or info.status not in(:paramsStatusOK) ) then 1 else 0 end) dp").append(billType.getCode()).append("ztCount ");
            sql.append(",sum(case when info.billFlag=:paramsBillFlagDP and info.billType=:").append(paramsKey).append(" then info.billMoney else 0 end) dp").append(billType.getCode()).append("Money ");
            params.put(paramsKey, billType.getCode());
        }
        sql.append("from ( ");
        sql.append(getBillUnitLstatisticsSql(params, busTypes, allBusTypes, vo.getEndDate(), vo.getCorpId(), false, vo.getBeginDate()));
        sql.append(")info group by info.busType  ");
        return this.listMapBySQL(sql.toString(), params);
    }

    /**
     * 获取单位票据统计表核心查询sql
     *
     * @param params      查询参数
     * @param busTypes    对应查询的业务类型
     * @param allBusTypes 所有业务类型
     * @param endDate     结束时间
     * @param corpIds     查询单位id
     * @param isDtl       是否查询明细
     * @return
     */
    public String getBillUnitLstatisticsSql(Map<String, Object> params, String[] busTypes, String[] allBusTypes, Date endDate, String corpIds, boolean isDtl, Date beginDate) {
        params.put("paramsAllBusTypes", allBusTypes);
        params.put("paramsBusTypes", busTypes);
        params.put("paramsEndDate", DateUtils.addDay(endDate, 1));
        if (beginDate != null) {
            params.put("paramsBeginDate", beginDate);
        }
        params.put("paramsCorpIds", StringUtils.split(corpIds, ","));
        params.put("poolBriefId", new String[]{
                AccebillConstant.AB_POOL_BRIEF_8018,
                AccebillConstant.AB_POOL_BRIEF_8218,
                AccebillConstant.AB_POOL_BRIEF_8118
        });
        StringBuilder sql = new StringBuilder();
        if (isDtl) {
            sql.append("select vv.BANK_TYPE_ID as banktypeId,vv.TYPE_NAME as bankTypeName,v.id as vid,vv.id as vvid,v.bill_status as billStatus,v.acceptor as acceptor,a.ID as id,a.recid as recid,vv.type,");
            sql.append("vv.CORP_ID as corpId,BT_CONCAT4('(',vv.CORP_CODE,')',vv.CORP_NAME) as corpCodeAndName,vv.bank_acc as bankAcc,");
            sql.append("sd.TYPE_CODE as source,v.orig_no as origNo,");
            sql.append("v.bill_flag as billFlag,v.bill_type as billType,");
//			sql.append("v.bill_money as billMoney,BT_CONCAT4('(',a.create_by,')',a.create_name) as applyName,");
            sql.append("v.bill_money as billMoney,");
            sql.append("(case ");
            sql.append("when a.create_by is not null then BT_CONCAT4('(',a.create_by,')',a.create_name) ");
            sql.append("else null end) as applyName,");
            sql.append("a.apply_date as applyDate,a.business_date as businessDate,");
            sql.append("v.start_date as startDate,v.end_date as endDate,d.interest,");
            sql.append("d.interest_rates as interestRates,v.payer,v.payee,");
//			--前手
            sql.append("case ");
            sql.append("when a.brief_id='6001' then a.cn_accrecords ");
            sql.append("when bs.bus_type in (:bus_type_0) then a.rec_nm ");
            params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
            sql.append("else '' end as cnAccrecords, ");
//	    	--后手
            sql.append("case ");
            sql.append("when bs.bus_type in (:bus_type_1) then a.rec_nm ");
            params.put("bus_type_1", new String[]{"001", "017", "002", "003", "010", "018", "019", "020", "011", "012", "022", "6201", "6203", "6004", "023"});
            sql.append("else '' end as rearAccrecords, ");
            sql.append("a.status as busStatus,a.opt_status as inoptStatus,a.send_sign as sendSign, ");
            //单位票据统计池状态
            sql.append("case ");
            sql.append("when vv.POOL_STATUS in (1,2) then 1  ");
            sql.append("else 0 end as countPoolStatus,");
            sql.append("ap.id as bpId, ap.bp_name as bpName,apd.brief_id as poolBriefId,");
        } else {
            sql.append("select a.id,vv.vid,VV.POOL_STATUS AS POOLSTATUS,apd.brief_id as poolBriefId,a.create_date as createDate,v.bill_money as billMoney,v.bill_type as billType,v.bill_flag as billFlag,a.status,a.opt_status as optStatus, ");
        }
        sql.append("case  ");
        sql.append("when bs.bus_type ='6001' and a.bill_source='12' then '9018' ");//判断线下录入为质押收入数据
        sql.append("when bs.bus_type ='6001' and a.bill_source='14' then '9011' ");//判断线下录入为贴现收入数据
        sql.append("else bs.bus_type ");
        sql.append("end as busType, ");
        sql.append(" v.new_stm_issuofppr_ind as newStmIssuofpprInd, v.blgrdbagcrcl_prmt_ind as blgrdbagcrclPrmtInd,");
        sql.append(" v.sbbll_rng_strt_sn as sbbllRngStrtSn, v.sbbll_rng_end_sn as sbbllRngEndSn, v.bl_std_amt as blStdAmt ");
        sql.append("from ab_voucherinfo_vice vv ");
        sql.append("inner join ab_voucherinfo v on v.id = vv.vid and (v.OLD_DRAFT_ID is null or v.OLD_DRAFT_ID not like '%-del') ");
        sql.append("inner join ab_accrecords a on a.vvid = vv.id  and a.COPY_FLAG in ('0','2') ");
        sql.append("left join ab_brief_status bs on bs.status = a.brief_id  ");
        sql.append("left join ab_pool_dtl apd on apd.vv_id = vv.id ");
        sql.append("left join ab_pool ap on ap.id=apd.bp_id  ");
        if (isDtl) {
            sql.append("left join ab_discount d on d.record_id=a.id ");
            sql.append("left join AB_BILL_SOURCE_DTL sd on sd.BRIEF_ID=a.brief_id "
                    + "and (vv.source=sd.source "
                    + "or (vv.source is null and a.bill_source=sd.source) "
                    + "or (vv.source is null and a.bill_source is null and sd.source is null )"
                    + ") ");
        }
        sql.append("inner join ( ");//找出票据最后一个业务的流水时间
        sql.append("select vv.vid,vv.corp_id as corp_id,max(a.create_date) createDate ");
        sql.append("from ab_voucherinfo_vice vv ");
        sql.append("left join ab_accrecords a on a.vvid = vv.id  ");
        sql.append("left join ab_brief_status bs on bs.status = a.brief_id  ");
        sql.append("where 1 = 1 ");
        sql.append("and a.status not in (:status) ");
        params.put("status", new Integer[]{AccebillConstant.AB_BILL_BUS_STATUS_2, AccebillConstant.AB_BILL_BUS_STATUS_3, 0, 96});
        sql.append("and a.opt_status not in ('-3', '-4') ");
        sql.append("and bs.bus_type is not null ");
        sql.append("and a.id is not null ");
        //票据业务排除票据池流水,但是票据池签收流水要显示
        sql.append(" and (a.pd_id is null or a.recid is not null) ");
        sql.append("and ( ");
        sql.append("case  ");
        sql.append("when bs.bus_type ='6001' and a.bill_source='12' then '9018' ");//判断线下录入为质押收入数据
        sql.append("when bs.bus_type ='6001' and a.bill_source='14' then '9011' ");//判断线下录入为贴现收入数据
        sql.append("else bs.bus_type ");
        sql.append("end ");
        sql.append(") in (:paramsAllBusTypes) ");
//		sql.append(") in ('001','004','6120','6001','9011','010','018','9018','019','011','6012','012','020','6020','6004') ");
        sql.append("and vv.corp_id in (:paramsCorpIds) and a.business_date<:paramsEndDate ");
        if (beginDate != null) {
            sql.append("and (a.business_date>=:paramsBeginDate or (bs.bus_type = '6001' and a.VALID_SIGN='1')) ");
        }
        sql.append("group by vv.vid,vv.corp_id ");
        sql.append(")info  on info.vid=vv.vid and a.create_date=info.createDate and vv.corp_id=info.corp_id ");
        sql.append("where 1 = 1 ");
//   -- and (a.is_hide <> '1' or a.is_hide is null)
        sql.append(" AND (  ");
        sql.append(" (apd.valid_sign in (0,1) AND apd.create_time = (SELECT MAX(create_time) FROM AB_POOL_DTL apd2 WHERE apd2.vv_id = vv.id and apd2.valid_sign in (0,1)))  ");
        sql.append(" or (apd.brief_id is null and apd.valid_sign IS NULL)  ) ");
        sql.append("and a.status not in (:status) ");
//        params.put("status", new Integer[]{AccebillConstant.AB_BILL_BUS_STATUS_2, AccebillConstant.AB_BILL_BUS_STATUS_3});
        sql.append("and a.opt_status not in ('-3', '-4') ");
        sql.append("and bs.bus_type is not null ");
        sql.append("and a.id is not null ");
        //票据业务排除票据池流水,但是票据池签收流水要显示
        sql.append(" and (a.pd_id is null or a.recid is not null) ");
        sql.append("and ( ");
        sql.append("case  ");
        sql.append("when bs.bus_type ='6001' and a.bill_source='12' then '9018' ");//判断线下录入为质押收入数据
        sql.append("when bs.bus_type ='6001' and a.bill_source='14' then '9011' ");//判断线下录入为贴现收入数据
        sql.append("else bs.bus_type ");
        sql.append("end ");
        sql.append(") in (:paramsBusTypes) ");
//		sql.append(") in ('001','004','6120','6001','9011','010','018','9018','019','011','6012','012','020','6020','6004') ");
        sql.append("and vv.corp_id in (:paramsCorpIds) and a.business_date<:paramsEndDate ");
        if (beginDate != null) {
            sql.append("and (a.business_date>=:paramsBeginDate or (bs.bus_type = '6001' and a.VALID_SIGN='1')) ");
        }
        return sql.toString();
    }

    /**
     * 加载单位票据统计表明细信息数据
     *
     * @param dataTablesInput 查询入参
     * @param allBusTypes     所有的业务类型
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput queryBillUnitLstatisticsDtlData(DataTablesInput dataTablesInput, String[] allBusTypes) {
        Map<String, Object> params = new HashMap<>();
        String[] busTypes = {String.valueOf(dataTablesInput.getFormdata().get("busType"))};
        dataTablesInput.getFormdata().remove("busType");
        Date endDate = DateUtils.parse(String.valueOf(dataTablesInput.getFormdata().get("endDate")), "yyyy-MM-dd");
        dataTablesInput.getFormdata().remove("endDate");
        Date beginDate = DateUtils.parse(String.valueOf(dataTablesInput.getFormdata().get("beginDate")), "yyyy-MM-dd");
        dataTablesInput.getFormdata().remove("beginDate");
        String corpIds = String.valueOf(dataTablesInput.getFormdata().get("corpId"));
        dataTablesInput.getFormdata().remove("corpId");
        StringBuilder sql = new StringBuilder("select * from ( ");
        sql.append(getBillUnitLstatisticsSql(params, busTypes, allBusTypes, endDate, corpIds, true, beginDate));
        sql.append(" ) dtl ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbAccrecordsVO.class, "dtl", true, params);
    }

    /**
     * 加载应收票据台账数据
     *
     * @param dataTablesInput 查询入参
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput billReceDataTable(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        Date businessDate = null;
        String ged_receDate = (String) dataTablesInput.getFormdata().get("ged_receDate");
        if (StringUtils.isNotEmpty(ged_receDate) && ged_receDate.length() == 10) {
            businessDate = DateUtils.parse(ged_receDate, DateUtils.PATTERN_CLASSICAL_SIMPLE);
            params.put("vvBusinessDate", businessDate);
        }
        if (dataTablesInput.isMoneySum()) {
            sql.append("select BT_NVL_NUM(sum(bill.billMoney),0) as billMoney from (");
            sql.append(this.getReceSql("1",businessDate));
        } else {
            sql.append("select * from (");
            sql.append(this.getReceSql(null,businessDate));
        }
        //用户满足对应查询权限的单位数据
        String corpAuthSql = SqlUtil.getInSql(corpAuthList, "v.apply_corp_id", true);
        sql.append("and ").append(corpAuthSql).append(" ");
        String transferWay = String.valueOf(dataTablesInput.getFormdata().get("ins_transferWay"));
        //添加持有方式条件
        if (StringUtils.isNotEmpty(transferWay)) {
            List<String> way = Arrays.asList(transferWay.split(","));
            sql.append(" and (bs.BUS_TYPE in (:transferWay) ");
            params.put("transferWay", way);
            if (way.contains("0")) {
                sql.append(" or bs.BUS_TYPE is null ");
            }
            dataTablesInput.getFormdata().remove("ins_transferWay");
            sql.append(") ");
        }
        // 是否对内条件查询
        if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("eqs_insideSing"))) {
            String insideSing = dataTablesInput.getFormdata().get("eqs_insideSing").toString();
            if (AccebillConstant.AB_INSIDE_SING_Y.equals(insideSing)) {
                sql.append(" and ((v.inside_sing=:insideSing and a.brief_id in (:briefId1) and ba.id is null) or ba.inside_sing=:insideSing or bd.inside_sing=:insideSing) ");
                params.put("briefId1", new String[]{AccebillConstant.BRIEF_STATUS_9003,
                        AccebillConstant.BRIEF_STATUS_9010,
                        AccebillConstant.BRIEF_STATUS_9012,
                        AccebillConstant.BRIEF_STATUS_9019,
                        AccebillConstant.BRIEF_STATUS_6019,
                        AccebillConstant.BRIEF_STATUS_6001,
                        AccebillConstant.BRIEF_STATUS_6212});
            } else if (AccebillConstant.AB_INSIDE_SING_N.equals(insideSing)) {
                sql.append(" and ((v.inside_sing=:insideSing and ba.id is null)  or ba.inside_sing=:insideSing or (ba.id is not null and ba.inside_sing is null) or bd.inside_sing=:insideSing) ");
            }
            params.put("insideSing", insideSing);
        }
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (v.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "v.bank_acc", "bankAccs", params)).append(")");
        }

        if (dataTablesInput.getFormdata().containsKey("bursarBillNoList")) {
            sql.append(" and ").append(SqlUtil.getInSqlByParams((List<String>) dataTablesInput.getFormdata().get("bursarBillNoList"), "nb.bursar_bill_no", "bursarBillNoList", params));
        }
        sql.append(") bill ");
        // 无前端排序时默认按录入日期排序；order by 不能写在子查询内，否则 prepareCountSQL
        // 会从最后一个 order by 处截断，导致 count 语句括号不平衡、SQL 解析异常
        if (CollectionUtils.isEmpty(dataTablesInput.getOrder())) {
            dataTablesInput.setOrderString(new OrderString("bill.createDate", "asc"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillPayAndReceVO.class, "bill", true, params);
    }
//    /**
//     * 应收票据台账数据总金额
//     *
//     * @return 金额 //todo 黑的没人调用 注释掉
//     */
//    public BigDecimal getBillReceiveMoneySum(DataTablesInput dataTablesInput, List<String> corpAuthList) {
//        StringBuilder sql = new StringBuilder("select BT_NVL_NUM(sum(i.BILL_MONEY),0) ");
//        sql.append(" from ab_voucherinfo_vice v inner join ab_voucherinfo i on v.vid=i.id  and (i.OLD_DRAFT_ID is null or i.OLD_DRAFT_ID not like '%-del') inner join ab_accrecords a on v.id=a.vvid");
//        sql.append(" left join ab_accrecords ba on a.id=ba.lastid and ba.status=95 and ba.opt_status='19' and ba.brief_id in ('6004','010','011','6011','018','8018','020','021')");
//        sql.append(" left join ab_accrecords bd on ba.id=bd.lastid and bd.status=95 and bd.opt_status='19' and bd.brief_id = '011'");
//        sql.append(" left join ab_brief_status bs on bs.status = bt_nvl_char(bd.brief_id,ba.brief_id) and bs.status!='6011'");
//        sql.append(" left join SYS_CORP sc on v.CORP_ID=sc.ID");
//        sql.append(" left join bt_bank_acc acc on acc.bank_acc =v.bank_acc");
//        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
//        sql.append(" where a.opt_status='19' and a.status=95 and v.TYPE='1' and (");
//        sql.append(" (a.brief_id in ('6001','6019','6212') and ((ba.brief_id !='6004') or ba.brief_id is null))");
//        sql.append(" or (a.brief_id in ('9003','9010','9012','9019'))) ");
//        this.setDataTablesValue(dataTablesInput, corpAuthList, sql);
//        return (BigDecimal) this.createSQLQuery(sql.toString(), dataTablesInput.getFormdata()).uniqueResult();
//    }

    /**
     * 加载应付票据台账数据
     *
     * @param dataTablesInput 查询入参
     * @param isEmitMake      是否补录
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput billPayDataTable(DataTablesInput dataTablesInput, boolean isEmitMake, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        if (dataTablesInput.isMoneySum()) {
            sql.append("select BT_NVL_NUM(sum(bill.billMoney),0) as billMoney,BT_NVL_NUM(sum(bill.payMoney),0) payMoney from (");
        } else {
            sql.append("select * from (");
        }
        sql.append(this.getPaySql());
        //用户满足对应查询权限的单位数据
        String corpAuthSql = SqlUtil.getInSql(corpAuthList, "v.APPLY_CORP_ID", true);
        sql.append("and ").append(corpAuthSql).append(" ");
        // 根据票据异常情况条件查询
        if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("ins_billAnomalies"))) {
            String billAnomalies = dataTablesInput.getFormdata().get("ins_billAnomalies").toString();
            Date sureTime = new Date();
            String time = DateUtils.date_sdf().format(sureTime);
            if (AccebillConstant.BILL_ANOMALIES_YES.equals(billAnomalies)) {
                BisExcDTO dto = new BisExcDTO();
                dto.setTransTimeNull(true);
                dto.setVoucherStat("0");
                List<BisExcDTO> bisExcDTOS = iBisExcService.listBisExc(dto);
                if(CollectionUtil.isNotEmpty(bisExcDTOS)){
                    List<String> collect = bisExcDTOS.stream().map(BisExcDTO::getCbsBillNo).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(collect)){
                        sql.append(" and ").append(getSQLIn(collect,900,"p.bill_no"));
                    }
                }
                sql.append(" and bd.BUSINESS_DATE is null ");
                sql.append(" and i.END_DATE < BT_TO_DATE( :payDateTime ,'YYYY-MM-DD') ");
                params.put("payDateTime", time);
            }
        }
        // 是否对内条件查询
        if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("eqs_insideSing"))) {
            String insideSing = dataTablesInput.getFormdata().get("eqs_insideSing").toString();
            sql.append(" and v.inside_sing=:insideSing ");
            params.put("insideSing", insideSing);
        }
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (v.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "v.bank_acc", "bankAccs", params)).append(")");
        }

        if (dataTablesInput.getFormdata().containsKey("bursarBillNoList")) {
            sql.append(" and ").append(SqlUtil.getInSqlByParams((List<String>) dataTablesInput.getFormdata().get("bursarBillNoList"), "nb.bursar_bill_no", "bursarBillNoList", params));
        }
        sql.append(") bill");
        // 无前端排序时默认按录入日期排序；order by 不能写在子查询内，否则 prepareCountSQL
        // 会从最后一个 order by 处截断，导致 count 语句括号不平衡、SQL 解析异常
        if (CollectionUtils.isEmpty(dataTablesInput.getOrder())) {
            dataTablesInput.setOrderString(new OrderString("bill.createDate", "asc"));
        }
        List<Order> orderList = dataTablesInput.getOrder();
        //到期提醒排序需根据付款日期和到期日期一起排序判断
        if (!CollectionUtils.isEmpty(orderList)) {
            Order order = orderList.get(0);
            String name = dataTablesInput.getColumns().get(order.getColumn()).getName();
            if ("dueRemindStr".equals(name)) {
                orderList.clear();
                Order payDateOrder = new Order(25, "asc");
                orderList.add(payDateOrder);
                Order endDateOrder = new Order(16, order.getDir());
                orderList.add(endDateOrder);
            }
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillPayAndReceVO.class, "bill", true, params);
    }

    /**
     * 应付票据台账数据总金额
     *
     * @return 金额
     */
    public BigDecimal getBillPayMoneySum(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder("select BT_NVL_NUM(sum(i.BILL_MONEY),0) ");
        sql.append(" from ab_voucherinfo_vice v inner join ab_voucherinfo i on v.vid=i.id and (i.OLD_DRAFT_ID is null or i.OLD_DRAFT_ID not like '%-del')  inner join ab_accrecords a on v.id = a.vvid ");
        sql.append(" left join ab_accrecords ba on v.id = ba.vvid and ba.opt_status = '19'and ba.brief_id='004'and ba.status = 95 ");
        sql.append(" left join ab_accrecords bd on  v.id = bd.vvid and bd.opt_status = '19' and bd.brief_id = '6120' and bd.status = 95 ");
        sql.append(" left join ab_emitbill_pay p on v.id=p.aid ");
        sql.append(" left join bis_exc e on e.cbs_bill_no=p.bill_no and e.voucher_stat='0'");
        sql.append(" left join SYS_CORP sc on v.CORP_ID=sc.ID");
        sql.append(" left join bt_bank_acc acc on acc.bank_acc =v.bank_acc");
        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" where a.opt_status = '19'  and a.status = 95 and v.type = '0' and a.BRIEF_ID = '001' and ba.brief_id is null ");
        this.setDataTablesValue(dataTablesInput, corpAuthList, sql);
        return (BigDecimal) this.createSQLQuery(sql.toString(), dataTablesInput.getFormdata()).uniqueResult();
    }

    /**
     * 应付票据统计公共sql
     *
     * @param isEmitMake 是否补录
     * @param curId      币别id
     * @return
     */
    public StringBuilder getStatisticsSql(boolean isEmitMake, String curId) {
        StringBuilder sql = new StringBuilder("select i.BILL_FLAG as billFlag,i.BILL_TYPE as billType,i.ORIG_NO as origNo,");
        sql.append(" i.PAYER_BANK_NAME as payerBankName,i.START_DATE as startDate,i.END_DATE as endDate,i.BILL_MONEY as billMoney,i.payee as payee,");
        sql.append(" v.BAIL_MONEY as bailMoney,BT_NVL_CHAR(ba.BRIEF_ID,a.BRIEF_ID) as briefId,v.emit_type as emitType,v.emit_guandis_sign as emitGuandisSign,");
        sql.append(" i.PAYER as payer,v.corp_Id as corpId,v.BAIL_PERCENT as bailpercent,BT_NVL_CHAR(A.RMK,V.RMK) as rmk,sc.name as corpName,");
        sql.append(" bt.TYPE_NAME as typeName, '" + curId + "' as curId,gw.NAME as guaTypeName,r.rat_code as ratCode,(case when i.END_DATE>:calcDate then i.BILL_MONEY else 0 end) as balMoney ");
        sql.append(" from ab_voucherinfo_vice v ");
        sql.append(" inner join ab_voucherinfo i on v.vid=i.id and (i.OLD_DRAFT_ID is null or i.OLD_DRAFT_ID not like '%-del')  ");
        sql.append(" inner join ab_accrecords a on v.id=a.vvid ");
        sql.append(" left join ab_accrecords ba on a.id=ba.lastid and ba.opt_status='19' and ba.brief_id in ( '004','6120') and ba.status=95 ");
        sql.append(" left join SYS_CORP sc on v.CORP_ID=sc.ID ");
        sql.append(" left join RAT_URATION r on  r.bill_id = v.id ");
        sql.append(" left join (select bill_id,max(create_date) create_date from RAT_URATION r where r.syscode = 'accebill'  and r.rat_type = 0 group by bill_id) r1 ");
        sql.append("      on r.bill_id = r1.bill_id and r.create_date=r1.create_date ");
        sql.append(" left join bt_input_bank_info bi on i.acceptorbankno=bi.sys_bank_code ");
        sql.append(" left join bt_bank_type bt on bt.bank_prefix=bi.bank_prefix and (bt.bank_name_prefix is null or bi.bank_name like bt_concat2(bt.bank_name_prefix,'%')) ");
        sql.append(" left join (select gco.business_id,cast(wmsys.wm_concat(distinct gw.name) as varchar2(2000) ) as name from gua_contract_occup gco ");
        sql.append("     left join GUA_CONTRACT_WAY gcw on gco.contract_id=gcw.gua_id ");
        sql.append("     left join GUA_WAY gw on gw.id= gcw.way_codes ");
        sql.append("     where gco.syscode = 'accebill' ");
        sql.append("     group by gco.business_id) gw on gw.business_id=v.id ");
        sql.append(" where (r.id is null or r1.bill_id is not null ) and a.opt_status='19' and a.status=95 and v.type='0' and ( ");
        if (isEmitMake) {
            sql.append(" (a.BRIEF_ID='6101' and ((ba.brief_id !='004') or ba.brief_id is null)))");
        } else {
            sql.append(" (a.SEND_SIGN=0 and a.BRIEF_ID='001' and ((ba.brief_id !='004') or ba.brief_id is null))");
            sql.append(" or (a.SEND_SIGN=1 and a.BRIEF_ID='003'))");
        }
        return sql;
    }

    /**
     * 根据查询条件查询所有需要导出的应付票据
     *
     * @param dataTablesInput
     * @return
     */
    public List<AbBillPayAndReceVO> listPayData(DataTablesInput dataTablesInput, boolean isEmitMake, List<String> corpAuthList) {
        DataTablesOutput data = billPayDataTable(dataTablesInput, isEmitMake, corpAuthList);
        return data.getData();
    }

    /**
     * 根据查询条件查询所有需要导出的应收票据
     *
     * @param dataTablesInput
     * @param corpAuthList
     * @return
     */
    public List<AbBillPayAndReceVO> listReceData(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        DataTablesOutput<AbBillPayAndReceVO> data = this.billReceDataTable(dataTablesInput, corpAuthList);
        return data.getData();
//        StringBuilder sql = this.getReceSql(null);
//        this.setDataTablesValue(dataTablesInput, corpAuthList, sql);
//        Map<String, Object> params = new HashMap<>();
//        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
//            //添加账户授权控制
//            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
//            sql.append(" and (v.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "v.bank_acc", "bankAccs", params)).append(")");
//            Map<String, Object> formdata = dataTablesInput.getFormdata();
//            formdata.put("bankAccs", params.get("bankAccs"));
//        }
//        // 是否对内条件查询
//        if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("eqs_insideSing"))) {
//            String insideSing = dataTablesInput.getFormdata().get("eqs_insideSing").toString();
//            if(AccebillConstant.AB_INSIDE_SING_Y.equals(insideSing)){
//                sql.append(" and ((v.inside_sing=1 and a.brief_id in ('9003', '9010', '9012', '9019','6001', '6019', '6212') and ba.id is null) or ba.inside_sing=1 or bd.inside_sing=1) ");
//                params.put("briefId1", new String[]{AccebillConstant.BRIEF_STATUS_9003,
//                        AccebillConstant.BRIEF_STATUS_9010,
//                        AccebillConstant.BRIEF_STATUS_9012,
//                        AccebillConstant.BRIEF_STATUS_9019,
//                        AccebillConstant.BRIEF_STATUS_6019,
//                        AccebillConstant.BRIEF_STATUS_6001,
//                        AccebillConstant.BRIEF_STATUS_6212});
//            }else if(AccebillConstant.AB_INSIDE_SING_N.equals(insideSing)){
//                sql.append(" and ((v.inside_sing=0  and ba.id is null)  or ba.inside_sing=0 or (ba.id is not null and ba.inside_sing is null) or bd.inside_sing=0) ");
//            }
//        }
//        sql.append(" order by a.business_date desc ");
//        return listBySQLAliasToBean(sql.toString(), dataTablesInput.getFormdata(), AbBillPayAndReceVO.class);
    }

    /**
     * 应收票据公共sql
     *
     * @return
     */
    public StringBuilder getReceSql(String isSumSql,Date businessDate) {
        StringBuilder sql = new StringBuilder("select a.id, i.bill_status billStatus,i.ebill_Bl_Crcl_Indc ebillBlCrclIndc,ba.PD_ID pDID, i.BILL_FLAG as billFlag,i.BILL_TYPE as billType,i.ORIG_NO as origNo,");
        sql.append(" i.PAYER as payer, i.payer_bank_name as payerBankName,i.ACCEPTOR as acceptor,i.START_DATE as startDate,i.END_DATE as endDate,v.bank_Name as bankName,v.corp_Id as corpId, ");
        if (StringUtils.isBlank(isSumSql)) {
            sql.append(" (case when a.brief_id='9010' then a.rec_acct_id else null end) as cnAccrecordsAcc, ");
            sql.append(" (case when a.brief_id='9010' then a.rec_Acctsvcr else null end) as cnAccrecordsBankCode, ");
            sql.append(" bibi.bank_name as cnAccrecordsBankName,");
            sql.append(" (case when bd.rec_acct_id is null then ba.rec_acct_id else bd.rec_acct_id end) recAcctId, ");
            sql.append(" (case when bd.rec_Acctsvcr is null then ba.rec_Acctsvcr else bd.rec_Acctsvcr end) recAcctBankCode,");
            sql.append(" bdba.bank_name as  recAcctsvcr,");
        }
        sql.append(" BT_NVL_CHAR(v.cn_accrecords,BT_NVL_CHAR(a.cn_accrecords,a.rec_nm)) as cnAccrecords, ");
        sql.append(" (case when bd.rec_nm = '' or bd.rec_nm is null then ba.rec_nm else bd.rec_nm end) as rearAccrecords,");
        sql.append("  ba.rmk,i.PAYERACC as payerAcc,case when BT_DATEDIFF(i.END_DATE) > 0 then BT_DATEDIFF(i.END_DATE) else null end as expireDays,");
        sql.append("  case " +
                "when acc.trans_time is not null then acc.trans_time " +
                "when bd.business_date is null and ba.brief_id='010' and ba.opt_status !='19' then null " +
                "when bd.business_date is null then ba.business_date " +
                "else bd.business_date end as transferDate,");
        sql.append("  case when bd.apply_date is not null then bd.apply_date else ba.apply_date end as applyDate,");
        sql.append("  CASE WHEN bn.status is not null THEN bn.status ELSE a.status end AS status,");
        sql.append("  CASE WHEN bn.opt_status is not null THEN bn.opt_status ELSE a.opt_status end AS optStatus,");
        sql.append("  BT_CONCAT4('(',v.CORP_CODE,')',v.CORP_NAME)");
        sql.append("  as corpCodeAndName,i.BILL_MONEY as billMoney,a.business_date as receDate,");
        sql.append("  BT_NVL_CHAR(bd.BRIEF_ID,BT_NVL_CHAR(ba.BRIEF_ID,a.BRIEF_ID)) as briefId,BT_NVL_CHAR(bd.send_sign,BT_NVL_CHAR(ba.send_sign, a.send_sign)) as sendSign,");
        sql.append(" BT_NVL_NUM(ad.INTEREST_RATES,ad2.INTEREST_RATES) as interestRates,BT_NVL_NUM(ad.INTEREST,ad2.INTEREST) as interest,BT_NVL_NUM(ad.REAL_MONEY,ad2.REAL_MONEY) as realMoney,");
        sql.append(" bt_nvl_char(bs.BUS_TYPE,'0') as transferWay,v.BANK_TYPE_ID as banktypeid, i.endorsement_Count as endorsementCount, v.bank_acc as bankAcc, ");
        sql.append(" v.APPLY_CORP_ID as applyCorpId,v.APPLY_CORP_NAME as applyCorpName,v.APPLY_CORP_CODE as applyCorpCode,v.BUS_CORP_ID as busCorpId,v.BUS_CORP_NAME as busCorpName,v.BUS_PERSONNEL as busPersonnel ");
        sql.append(" , i.new_stm_issuofppr_ind as newStmIssuofpprInd, i.blgrdbagcrcl_prmt_ind as blgrdbagcrclPrmtInd,  ");
        sql.append(" i.sbbll_rng_strt_sn as sbbllRngStrtSn, i.sbbll_rng_end_sn as sbbllRngEndSn, i.bl_std_amt as blStdAmt, i.bl_rmtr_unn_id_no as blRmtrUnnIdNo ");
        sql.append(" ,nb.id as payId,nb.bursar_bill_no as bursarBillNo,nb.bill_no as payNo,ba.final_approver as finalApprover, v.create_date as createDate ");
        sql.append(" from ab_voucherinfo_vice v inner join ab_voucherinfo i on v.vid=i.id " +
                " inner join ab_accrecords a on v.id=a.vvid");
        sql.append(" left join ab_accrecords ba on a.id=ba.lastid and ba.status=95 and ((ba.brief_id in ('6004','011','6011','8018','018','020','021') and ba.opt_status='19') or (ba.brief_id='010' and ba.opt_status in ('18','19'))  )");
        sql.append(" left join ab_accrecords bd on ba.id=bd.lastid and bd.status=95 and bd.opt_status='19' and bd.brief_id = '011' and bd.status<>-2 ");
        sql.append(" left join (SELECT vvid,max(create_date) AS maxDate FROM ab_accrecords WHERE brief_id in ('6004','010','011','6011','6012','8018','018','020','021','6020') ");
        if (businessDate != null) {
            sql.append(" and business_date>=:vvBusinessDate ");
        }
        sql.append(" GROUP BY vvid ) m ON m.vvid = v.id ");
        sql.append(" left JOIN ab_accrecords bn ON bn.vvid = m.vvid and bn.brief_id in ('6004','010','011','6011','6012','8018','018','020','021','6020') AND bn.create_date = m.maxDate ");
        sql.append(" left join ab_brief_status bs on ((bd.brief_id is not null and bs.status =bd.brief_id) or (bd.brief_id is null and bs.status=ba.brief_id)) and bs.status!='6011'");
        sql.append(" left join ab_discount ad on bd.id=ad.record_id ");
        sql.append(" left join ab_discount ad2 on ba.id=ad2.record_id ");
        sql.append(" LEFT JOIN AB_CLAIM_MIDDLE m on m.id = v.CLAIM_ID and m.claim_status ='95' ");
        sql.append(" left join BIS_ACC_DTL ACC on m.id = acc.claim_id  ");
        sql.append(" left join nis_bill nbm on ba.id = nbm.bill_id and ba.brief_id='010' ");
        if (StringUtils.isBlank(isSumSql)) {
            sql.append(" left join bt_input_bank_info bibi on a.rec_acctsvcr = bibi.sys_bank_code and a.brief_id='9010' ");
            sql.append(" left join bt_input_bank_info bdba on (case when bd.rec_Acctsvcr is null then ba.rec_Acctsvcr else bd.rec_Acctsvcr end) = bdba.sys_bank_code ");
        }
        sql.append(" left join nis_billhead nb on nb.id = nbm.pay_id and ba.brief_id='010' ");
        sql.append(" where 1=1 and a.opt_status='19' and a.status=95 and v.TYPE='1' and (");
        sql.append(" (a.brief_id in ('6001','6019','6212') and ((ba.brief_id !='6004') or ba.brief_id is null))");
        sql.append(" or (a.brief_id in ('9003','9010','9012','9019'))) ");
        return sql;
    }

    /**
     * 应付票据公共sql
     *
     * @return
     */
    public StringBuilder getPaySql() {
        StringBuilder sql = new StringBuilder("select a.id,i.BILL_FLAG as billFlag,i.BILL_TYPE as billType,i.ORIG_NO as origNo,v.bank_Name as bankName,v.BANK_TYPE_ID as bankTypeId,");
        sql.append(" i.PAYER_BANK_NAME as payerBankName,i.START_DATE as startDate,i.END_DATE as endDate,i.BILL_MONEY as billMoney,i.payee as payee,");
        sql.append(" i.payeeacc,i.payeebankno,i.payee_Bank_Name as payeeBankName,");
        sql.append(" i.bill_money-BT_NVL_NUM(v.BAIL_MONEY,0) as payMoney,");
        sql.append(" BT_NVL_NUM(v.BAIL_MONEY,0) as bailMoney,BT_NVL_NUM(v.bail_percent,0) as bailPercent,BT_NVL_CHAR(bd.BRIEF_ID,a.BRIEF_ID) as briefId,a.send_sign as sendSign,v.emit_type as emitType,v.emit_guandis_sign as emitGuandisSign,");
        sql.append(" (case when p.EXC_TRANS_TIME is not null then p.EXC_TRANS_TIME else bd.BUSINESS_DATE end) as payDate,i.PAYER as payer,v.corp_Id as corpId,BT_CONCAT4('(',v.CORP_CODE,')',v.CORP_NAME) as corpCodeAndName");
        sql.append(" ,(case " +
                "when bd.id is not null and i.new_stm_issuofppr_ind='0' then '000000' " +
                "when bd.id is not null and i.new_stm_issuofppr_ind='1' then 'CS06' " +
                "when bt_sysdate()>=i.END_DATE then 'CS04' " +
                "when i.bill_status is not null then i.bill_status " +
                "when i.new_stm_issuofppr_ind='0' then '030006' " +
                "when i.new_stm_issuofppr_ind='1' then 'CS03' " +
                "else null end ");
        sql.append(" ) as billStatus ");
        sql.append(" ,(case when p.EXC_TRANS_TIME is null and bd.BUSINESS_DATE is null then '0' else '1' end) as isPay ");
        sql.append(" ,(case when (i.bill_status is null or i.bill_status not in ('170001','170006','010004','020001','020006','030001','000002'))  then '1' else '0' end) as emitOpenOk ");
        sql.append(" ,(case when br.id is not null then br.BUSINESS_DATE when a.SEND_SIGN ='0' and bbr.BUSINESS_DATE is null then a.BUSINESS_DATE when a.SEND_SIGN ='0' then bbr.BUSINESS_DATE else null end) as emitOpenDate ");
        sql.append(" ,v.APPLY_CORP_ID as applyCorpId,v.APPLY_CORP_NAME as applyCorpName,v.APPLY_CORP_CODE as applyCorpCode,v.BUS_CORP_ID as busCorpId,v.BUS_CORP_NAME as busCorpName,v.BUS_PERSONNEL as busPersonnel, v.bank_acc as bankAcc ");
        sql.append(" , i.new_stm_issuofppr_ind as newStmIssuofpprInd, i.blgrdbagcrcl_prmt_ind as blgrdbagcrclPrmtInd,i.ebill_bl_crcl_indc ebillBlCrclIndc,  ");
        sql.append(" i.sbbll_rng_strt_sn as sbbllRngStrtSn, i.sbbll_rng_end_sn as sbbllRngEndSn, i.bl_std_amt as blStdAmt, i.bl_rmtr_unn_id_no as blRmtrUnnIdNo,v.rmk,bd.rmk as rmkDescr ");
        sql.append(" ,v.voucher_id as payId,nb.bursar_bill_no as bursarBillNo,nb.bill_no as payNo ");
        sql.append(" ,a.create_name as applyName, v.create_date as createDate ");
        sql.append(" from ab_voucherinfo_vice v inner join ab_voucherinfo i on v.vid=i.id inner join ab_accrecords a on v.id = a.vvid ");
        sql.append(" left join ab_accrecords ba on v.id = ba.vvid and ba.opt_status = '19'and ba.brief_id='004'and ba.status = 95 ");
        sql.append(" left join ab_accrecords br on  v.id = br.vvid and br.opt_status = '19' and br.brief_id = '003' and br.status = 95 ");
        sql.append(" left join ab_accrecords bd on  v.id = bd.vvid and bd.opt_status = '19' and bd.brief_id = '6120' and bd.status in (95,100) ");
        sql.append(" left join ab_accrecords bbr on  v.id = bbr.vvid and bbr.opt_status = '19' and bbr.brief_id = '6101' and bbr.status = 95 ");
        sql.append(" left join ab_emitbill_pay p on v.id=p.aid ");
        sql.append(" left join nis_billhead nb on nb.id = v.voucher_id ");
        sql.append(" where a.opt_status = '19'  and a.status = 95 and v.type = '0' and a.BRIEF_ID = '001' and ba.brief_id is null ");
        return sql;
    }

    /**
     * 台账查询条件
     */
    public void setDataTablesValue(DataTablesInput dataTablesInput, List<String> corpAuthList, StringBuilder sql) {
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        //用户满足对应查询权限的单位数据
        String corpAuthSql = SqlUtil.getInSql(corpAuthList, "v.apply_corp_id", true);
        sql.append("and ").append(corpAuthSql).append(" ");
        //判断是否已付款条件
        if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("eqs_isPay"))) {
            String isPay = dataTablesInput.getFormdata().get("eqs_isPay").toString();
            if (AccebillConstant.AB_IS_PAY_Y.equals(isPay)) {
                sql.append(" and (e.TRANS_TIME is not null or bd.BUSINESS_DATE is not null) ");
            } else if (AccebillConstant.AB_IS_PAY_N.equals(isPay)) {
                sql.append(" and (e.TRANS_TIME is null and bd.BUSINESS_DATE is null) ");
            }
        }//开票完成条件
        if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("eqs_emitOpenOk"))) {
            String emitOpenOk = dataTablesInput.getFormdata().get("eqs_emitOpenOk").toString();
            if ("1".equals(emitOpenOk)) {
                sql.append(" and (i.bill_status is null or i.bill_status not in ('170001','170006','010004','020001','020006','030001','000002')) ");
            } else if ("0".equals(emitOpenOk)) {
                sql.append(" and i.bill_status in ('010004','020001','020006','030001','000002') ");
            }
        }
        if (StringUtils.isNotBlank((String) formdata.get("ins_corpId"))) {
            List list = Arrays.asList(((String) formdata.get("ins_corpId")).split(","));
            sql.append(" and (" + getSQLIn(list, 900, "v.corp_Id") + ")");
        }
        if (StringUtils.isNotBlank((String) formdata.get("ins_applyCorpId"))) {
            List list = Arrays.asList(((String) formdata.get("ins_applyCorpId")).split(","));
            sql.append(" and (" + getSQLIn(list, 900, "v.APPLY_CORP_ID") + ")");
        }
        if (StringUtils.isNotBlank((String) formdata.get("ins_busCorpId"))) {
            List list = Arrays.asList(((String) formdata.get("ins_busCorpId")).split(","));
            sql.append(" and (" + getSQLIn(list, 900, "v.BUS_CORP_ID") + ")");
        }
        if (StringUtils.isNotBlank((String) formdata.get("ini_billFlag"))) {
            List list = Arrays.asList(((String) formdata.get("ini_billFlag")).split(","));
            formdata.put("ini_billFlag", list);
            sql.append(" and i.bill_Flag in :ini_billFlag ");
        }
        if (StringUtils.isNotBlank((String) formdata.get("ini_billType"))) {
            List list = Arrays.asList(((String) formdata.get("ini_billType")).split(","));
            formdata.put("ini_billType", list);
            sql.append(" and i.bill_Type in :ini_billType ");
        }
        if (StringUtils.isNotBlank((String) formdata.get("ins_transferWay"))) {
            List list = Arrays.asList(((String) formdata.get("ins_transferWay")).split(","));
            formdata.put("ins_transferWay", list);
            sql.append(" and (bt_nvl_char(bs.BUS_TYPE,'0') in :ins_transferWay ");
            if (list.contains("0")) {
                sql.append(" or bs.BUS_TYPE is null ");
            }
            sql.append(") ");
        }
        if (StringUtils.isNotBlank((String) formdata.get("ins_bankTypeId"))) {
            List list = Arrays.asList(((String) formdata.get("ins_bankTypeId")).split(","));
            formdata.put("ins_bankTypeId", list);
            sql.append(" and v.BANK_TYPE_ID in :ins_bankTypeId ");
        }
        //票据开票方式
        if (StringUtils.isNotBlank((String) formdata.get("ins_emitTypeName"))) {
            List list = Arrays.asList(((String) formdata.get("ins_emitTypeName")).split(","));
            formdata.put("ins_emitTypeName", list);
            sql.append(" and v.emit_type in :ins_emitTypeName ");
        }
        String gedStartDate = (String) formdata.get("ged_startDate");
        if (StringUtils.isNotBlank(gedStartDate)) {
            sql.append(" and i.START_DATE >= BT_TO_DATE(:ged_startDate,'YYYY-MM-DD') ");
        }
        String gedLedStartDate = (String) formdata.get("led_startDate");
        if (StringUtils.isNotBlank(gedLedStartDate)) {
            sql.append(" and i.START_DATE <= BT_TO_DATE(:led_startDate,'YYYY-MM-DD') ");
        }
        //票据金额区间
        String geg_billMoney = (String) formdata.get("geg_billMoney");
        if (StringUtils.isNotBlank(geg_billMoney)) {
            sql.append(" and i.bill_money >= :geg_billMoney ");
            dataTablesInput.getFormdata().put("geg_billMoney", new BigDecimal(geg_billMoney));
        }
        String leg_billMoney = (String) formdata.get("leg_billMoney");
        if (StringUtils.isNotBlank(leg_billMoney)) {
            sql.append(" and i.bill_money <= :leg_billMoney ");
            dataTablesInput.getFormdata().put("leg_billMoney", new BigDecimal(leg_billMoney));
        }
        String gedGedEndDate = (String) formdata.get("ged_endDate");
        if (StringUtils.isNotBlank(gedGedEndDate)) {
            sql.append(" and i.END_DATE >= BT_TO_DATE(:ged_endDate,'YYYY-MM-DD') ");
        }
        String gedLedEndDate = (String) formdata.get("led_endDate");
        if (StringUtils.isNotBlank(gedLedEndDate)) {
            sql.append(" and i.END_DATE <= BT_TO_DATE(:led_endDate,'YYYY-MM-DD') ");
        }
        String gedGedReceDate = (String) formdata.get("ged_receDate");
        if (StringUtils.isNotBlank(gedGedReceDate)) {
            sql.append(" and a.business_date >= BT_TO_DATE(:ged_receDate,'YYYY-MM-DD') ");
        }
        String gedLedReceDate = (String) formdata.get("led_receDate");
        if (StringUtils.isNotBlank(gedLedReceDate)) {
            gedLedReceDate = gedLedReceDate + " 23:59:59";
            dataTablesInput.getFormdata().put("led_receDate", gedLedReceDate);
            sql.append(" and a.business_date <= BT_TO_DATE(:led_receDate,'yyyy-MM-dd hh24:mi:ss') ");
        }
        String gedGedPayDate = (String) formdata.get("ged_payDate");
        if (StringUtils.isNotBlank(gedGedPayDate)) {
            sql.append(" and (ba.business_date >= BT_TO_DATE(:ged_payDate,'YYYY-MM-DD') or e.TRANS_TIME >= BT_TO_DATE(:ged_payDate,'YYYY-MM-DD')) ");
        }
        String gedLedPayDate = (String) formdata.get("led_payDate");
        if (StringUtils.isNotBlank(gedLedPayDate)) {
            sql.append(" and (ba.business_date <= BT_TO_DATE(:led_payDate,'YYYY-MM-DD') or e.TRANS_TIME <= BT_TO_DATE(:led_payDate,'YYYY-MM-DD'))");
        }

//        票据号
        String origNo = (String) formdata.get("likes_origNo");
        if (StringUtils.isNotBlank(origNo)) {
            sql.append(" and i.orig_no like :origNo ");
            dataTablesInput.getFormdata().put("origNo", StrUtil.builder("%", origNo, "%").toString());
        }
//        备注
        String rmk = (String) formdata.get("likes_rmk");
        if (StringUtils.isNotBlank(rmk)) {
            sql.append(" and ba.rmk like :rmk ");
            dataTablesInput.getFormdata().put("rmk", StrUtil.builder("%", rmk, "%").toString());
        }
//        付款单号
        String bursarBillNo = (String) formdata.get("likes_bursarBillNo");
        if (StringUtils.isNotBlank(bursarBillNo)) {
            sql.append(" and nb.bursar_bill_no like :bursarBillNo ");
            dataTablesInput.getFormdata().put("bursarBillNo", StrUtil.builder("%", bursarBillNo, "%").toString());
        }
//        共享单据号
        String payNo = (String) formdata.get("likes_payNo");
        if (StringUtils.isNotBlank(payNo)) {
            sql.append(" and nb.bill_no like :payNo ");
            dataTablesInput.getFormdata().put("payNo", StrUtil.builder("%", payNo, "%").toString());
        }
        if (dataTablesInput.getFormdata().containsKey("bursarBillNoList")) {
            sql.append(" and ").append(SqlUtil.getInSqlByParams((List<String>) dataTablesInput.getFormdata().get("bursarBillNoList"), "nb.bursar_bill_no", "bursarBillNoListParams", dataTablesInput.getFormdata()));
        }
    }

    /**
     * <br>
     * <b>功能：</b> 查询票据融资余额情况 直接融资票据余额、保证金金额、敞口金额（票据金额-保证金） <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/10 <br>
     */
    public List<FinanceLoanDTO> queryProvideFinance(List<String> corpAuth, String curId, Date queryDate, boolean isEmitMake) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("queryDate", queryDate);
        params.put("curId", curId);
        sql.append(" select '02' as busType,info.corpId,s.name as corpName,info.money,info.bondMoney as bondMoney,(info.money-info.bondMoney) as openMoney  ")
                .append(" from (");
        sql.append(" select d.corpId,sum(d.money) as money, sum(d.bondMoney) as bondMoney ")
                .append(" from (");
        //内关联查询开始
        //应付票据根据业务办理日期查询未兑付未退票数据
        sql.append(" select vv.corp_id as corpId,sum(BT_NVL_NUM(v.bill_money, 0)) as money,sum(BT_NVL_NUM(vv.bail_money, 0)) as bondMoney  ")
                .append(" from ab_voucherinfo_vice vv ")
                .append(" inner join ab_voucherinfo v on vv.vid=v.id and (v.OLD_DRAFT_ID is null or v.OLD_DRAFT_ID not like '%-del')  ")
                .append(" inner join ab_accrecords a on vv.id=a.vvid ")
                .append(" left join ab_accrecords ba on a.id=ba.lastid and ba.opt_status='19' and ba.status in (95,100)  ")
                .append(" where  a.opt_status='19' and a.status=95 and vv.type='0' and ");
        if (isEmitMake) {
            sql.append(" (a.BRIEF_ID='6101' and a.business_date <=:queryDate and (ba.id is null or (ba.id is not null and ba.brief_id in ( '004','6120') and ba.valid_sign=1 and ba.business_date>:queryDate)")
                    .append(" or (ba.id is not null and ba.brief_id in ('6120') and ba.valid_sign=2 and a.valid_sign=1))) ");
        } else {
            sql.append(" (((a.SEND_SIGN=0 and a.BRIEF_ID in ('001','002')) or (a.SEND_SIGN=1 and a.BRIEF_ID='003')) and a.business_date <=:queryDate ")
                    .append(" and (ba.id is null or (ba.id is not null and ba.brief_id in ('004','6120') and ba.valid_sign=1 and ba.business_date>:queryDate)))  ");
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "vv.corp_id"))
                .append(" group by vv.corp_id ");
        sql.append(" union all ");
        //应收票据根据业务办理日期查询已贴现到银行、未收回赎回的数据
        sql.append(" select b.corp_Id as corpId,sum(BT_NVL_NUM(a.bill_money, 0)) as money,sum(BT_NVL_NUM(b.bail_money, 0)) as bondMoney ")
                .append(" from ab_voucherinfo_vice b ")
                .append(" inner join ab_voucherinfo a on a.id = b.vid and (a.OLD_DRAFT_ID is null or a.OLD_DRAFT_ID not like '%-del')  ")
                .append(" inner join ab_accrecords l on l.vvid = b.id and l.status = 95 and l.opt_status = 19 and l.brief_id in (011) ")
                .append(" left join ab_accrecords l2 on l2.lastid = l.id and l2.status = 95 and l2.opt_status = 19 and l2.brief_id in (6012) ")
                .append(" left join ab_accrecords l3 on l3.lastid = l2.id and l3.status = 95 and l3.opt_status = 19 and l3.brief_id in (6212) ")
                .append(" where b.type = 1 and l.inside_sing=0  ")
                .append(" and (l3.id is null or l3.business_date > :queryDate)")
                .append(" and l.business_date <= :queryDate ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "b.corp_id"));
        sql.append(" group by b.corp_id ");
        //内关联查询结束
        sql.append(" ) d ");
        sql.append(" group by d.corpId ) info ");
        sql.append(" left join sys_corp s on s.id = info.corpId ");
        sql.append("  order by s.id ");
        return listBySQLAliasToBean(sql.toString(), params, FinanceLoanDTO.class);
    }

    /**
     * <br>
     * <b>功能：</b> 查询票据利息支出情况 <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/10 <br>
     */
    public List<FinanceLoanDTO> queryInterestFinance(List<String> corpAuth, String curId, Date queryDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("queryDate", queryDate);
        params.put("curId", curId);
        sql.append(" select '02' as busType,info.corpId,s.name as corpName,info.money  ")
                .append(" from (");
        //应收票据根据业务办理日期查询已贴现到银行、未赎回收回的数据
        sql.append("  select b.corp_Id as corpId,sum(BT_NVL_NUM(ad.interest,0)) as money ")
                .append(" from ab_voucherinfo_vice b ")
                .append(" inner join ab_voucherinfo a on a.id = b.vid and (a.OLD_DRAFT_ID is null or a.OLD_DRAFT_ID not like '%-del')  ")
                .append(" inner join ab_accrecords l on l.vvid = b.id and l.status = 95 and l.opt_status = 19 and l.brief_id in (011) ")
                .append(" left join ab_accrecords l2 on l2.lastid = l.id and l2.status = 95 and l2.opt_status = 19 and l2.brief_id in (6012) ")
                .append(" left join ab_accrecords l3 on l3.lastid = l2.id and l3.status = 95 and l3.opt_status = 19 and l3.brief_id in (6212) ")
                .append(" left join ab_discount ad on ad.record_id=l.id ")
                .append(" where b.type = 1 and l.inside_sing=0  ")
                .append(" and (l3.id is null or l3.business_date > :queryDate)")
                .append(" and l.business_date <= :queryDate ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "b.corp_id"));
        sql.append(" group by b.corp_id ");
        sql.append(" ) info ");
        sql.append(" left join sys_corp s on s.id = info.corpId ");
        sql.append("  order by s.id ");
        return listBySQLAliasToBean(sql.toString(), params, FinanceLoanDTO.class);
    }

    /**
     * <br>
     * <b>功能：</b> 查询票据融资品种融资余额（第三方票据品种）,融资品种融资余额、保证金金额、敞口金额（票据金额-保证金） <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/11 <br>
     */
    public List<FinanceTypeLoanDTO> queryProvideTypeFinance(List<String> corpAuth, String curId, Date queryDate, boolean isEmitMake) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("queryDate", queryDate);
        params.put("curId", curId);
        sql.append(" select '02' as sysCode,info.billType as typeCode,sd.type_name as typeName,info.money,info.bondMoney as bondMoney,(info.money-info.bondMoney) as openMoney  ")
                .append(" from (");
        sql.append(" select d.billType,sum(d.money) as money, sum(d.bondMoney) as bondMoney ")
                .append(" from (");
        //内关联查询开始
        //应付票据根据业务办理日期查询未兑付未退票数据
        sql.append(" select v.bill_type as billType,sum(BT_NVL_NUM(v.bill_money, 0)) as money,sum(BT_NVL_NUM(vv.bail_money, 0)) as bondMoney  ")
                .append(" from ab_voucherinfo_vice vv ")
                .append(" inner join ab_voucherinfo v on vv.vid=v.id and (v.OLD_DRAFT_ID is null or v.OLD_DRAFT_ID not like '%-del')  ")
                .append(" inner join ab_accrecords a on vv.id=a.vvid ")
                .append(" left join ab_accrecords ba on a.id=ba.lastid and ba.opt_status='19' and ba.status in (95,100)  ")
                .append(" where  a.opt_status='19' and a.status=95 and vv.type='0' and ");
        if (isEmitMake) {
            sql.append(" (a.BRIEF_ID='6101' and a.business_date <=:queryDate and (ba.id is null or (ba.id is not null and ba.brief_id in ( '004','6120') and ba.valid_sign=1 and ba.business_date>:queryDate)")
                    .append(" or (ba.id is not null and ba.brief_id in ('6120') and ba.valid_sign=2 and a.valid_sign=1))) ");
        } else {
            sql.append(" (((a.SEND_SIGN=0 and a.BRIEF_ID in ('001','002')) or (a.SEND_SIGN=1 and a.BRIEF_ID='003'))  and a.business_date <=:queryDate ")
                    .append(" and (ba.id is null or (ba.id is not null and ba.brief_id in ('004','6120') and ba.valid_sign=1 and ba.business_date>:queryDate)))  ");
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "vv.corp_id"))
                .append(" group by v.bill_type ");
        sql.append(" union all ");
        //应收票据根据业务办理日期查询已贴现到银行、未赎回收回的数据
        sql.append(" select a.bill_Type as billType,sum(BT_NVL_NUM(a.bill_money, 0)) as money,sum(BT_NVL_NUM(b.bail_money, 0)) as bondMoney ")
                .append(" from ab_voucherinfo_vice b ")
                .append(" inner join ab_voucherinfo a on a.id = b.vid and (a.OLD_DRAFT_ID is null or a.OLD_DRAFT_ID not like '%-del')  ")
                .append(" inner join ab_accrecords l on l.vvid = b.id and l.status = 95 and l.opt_status = 19 and l.brief_id in (011) ")
                .append(" left join ab_accrecords l2 on l2.lastid = l.id and l2.status = 95 and l2.opt_status = 19 and l2.brief_id in (6012) ")
                .append(" left join ab_accrecords l3 on l3.lastid = l2.id and l3.status = 95 and l3.opt_status = 19 and l3.brief_id in (6212) ")
                .append(" where b.type = 1 and l.inside_sing=0  ")
                .append(" and (l3.id is null or l3.business_date > :queryDate)")
                .append(" and l.business_date <= :queryDate ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "b.corp_id"));
        sql.append(" group by a.bill_type ");
        //内关联查询结束
        sql.append(" ) d ");
        sql.append(" group by d.billType ) info ");
        sql.append(" inner join sys_dict sd on sd.type_code=info.billType inner join sys_dict_group sdg on  sd.type_group_id=sdg.id and sdg.type_group_code='AB_BILL_TYPE' ");
        sql.append("  order by sd.id ");
        return listBySQLAliasToBean(sql.toString(), params, FinanceTypeLoanDTO.class);
    }

    /**
     * 获取票据池信息
     **/
    public void getBillPoolInfo(AbVoucherinfoVO abVoucherinfoVO) {
        StringBuilder sql = new StringBuilder(" select ");
        sql.append(" ap.id as bpId, ap.bp_name as bpName, case when p.pool_status = 1 and apd.brief_id in (8018, 8118, 8218) then 1 else 0 end as countPoolStatus ");
        sql.append(" from AB_POOL_DTL apd LEFT JOIN AB_VOUCHERINFO_VICE P ON apd.vv_id = p.id ");
        sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID = P.VID LEFT JOIN AB_POOL ap on apd.bp_id = ap.id ");
        sql.append(" where 1 = 1 and v.orig_no =:origNo");
        sql.append(" and apd.create_time = (SELECT MAX(create_time) FROM AB_POOL_DTL apd2 WHERE apd2.vv_id = p.id and apd2.valid_sign in (0,1)) ");
        sql.append(" and apd.valid_sign in (0, 1) ");
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("origNo", abVoucherinfoVO.getOrigNo());
        List<AbAccrecordsVO> list = this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
        if (!CollectionUtils.isEmpty(list)) {
            AbAccrecordsVO abAccrecordsVO = list.get(0);
            abVoucherinfoVO.setBpName(abAccrecordsVO.getBpName());
            abVoucherinfoVO.setCountPoolStatus(abAccrecordsVO.getCountPoolStatus());
            abVoucherinfoVO.setCountPoolStatusName(abAccrecordsVO.getCountPoolStatusName());
        } else {
            abVoucherinfoVO.setCountPoolStatusName("非在池");
        }
    }

    /**
     * 查询背书转让详情数据
     *
     * @param dataTablesInput 参数
     * @return 列表数据
     */
    public DataTablesOutput getEndorsementCountData(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder(" select ");
        sql.append(" back.bussdt AS bussdt, back.req_nm AS reqNm, back.rec_nm AS recNm, back.bankendrsmtmk AS bankendrsmtmk ");
        sql.append(" FROM AB_BACKBILLINFO back ");
        sql.append(" WHERE back.BUSTYPE in ('010','ET05') and back.orig_no = :origNo and (back.sbbll_rng_strt_sn is null or back.sbbll_rng_strt_sn=:sbbllRngStrtSn) and (back.sbbll_rng_end_sn is null or back.sbbll_rng_end_sn=:sbbllRngEndSn) ");
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("origNo", dataTablesInput.getFormdata().get("origNo"));
        params.put("sbbllRngStrtSn", dataTablesInput.getFormdata().get("sbbllRngStrtSn"));
        params.put("sbbllRngEndSn", dataTablesInput.getFormdata().get("sbbllRngEndSn"));
        return this.getDynamicBySQLForBill(dataTablesInput, sql.toString(), AbBackbillinfoVO.class, "", false, params);
    }

    /**
     * @Description
     * @author zhous
     * @date 2024/9/18 17:26
     */
    public DataTablesOutput<AbBillBabVo> dataQueryBillBabByMonthTable(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        List<Order> orderList = new ArrayList<>();
        Order order1 = new Order();
        order1.setColumn(1);
        order1.setDir("asc");
        orderList.add(order1);
        orderList.addAll(dataTablesInput.getOrder());
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(" select a.* from ( ");
        sql.append(" select sem.Equity_code as equityCode,sem.Equity_name as equityName,m.operationAreaCode as operationAreaCode,");
        sql.append(" m.corpName,m.corpId,m.bankTypeId,m.typeName, m.bankName, m.bankAcc,m.billMoney,m.bailMoney,m.endDate,m.bailPer as bailPercent,m.parentName,m.parentId,m.parentSort,");
        sql.append(" case when m.bailPer <> 1  or m.bailPer is null then m.billMoney else 0 end  as applyRepayMoney,case when m.bailMoney is not null then m.billMoney-m.bailMoney else m.billMoney end  as settleMoney ");
        sql.append(" from ( ");
        sql.append(" select BT_NVL_NUM(sum(av.bill_money),0) as billMoney,BT_NVL_NUM(sum(avv.bail_money),0) as bailMoney,avv.corp_id as corpId,sc.name as corpName, ");
        sql.append(" avv.BANK_TYPE_ID as bankTypeId,avv.type_name as typeName, avv.bank_name AS bankName, avv.bank_acc as bankAcc,av.end_date as endDate, ");
        sql.append(" case when  a.bailPercent = 0  or a.bailPercent is null then 0 else  a.bailPercent end as bailPer,");
        sql.append(" sc.equityMaintenance_ID as equityMaintenanceId,sc.operationArea_ID as operationAreaCode,  ");
        sql.append(" case when sc.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc.id when sc2.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc2.id else sc3.id end as parentId, ");
        sql.append(" case when sc.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc.name  when sc2.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc2.name else sc3.name end as parentName, ");
        sql.append(" case when sc.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc.sort  when sc2.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc2.sort else sc3.sort end as parentSort ");
        sql.append(" from ");
        sql.append(" ab_voucherinfo_vice avv ");
        sql.append(" left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append(" left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001'  ");
        sql.append(" left join sys_corp sc on sc.id = avv.corp_id  ");
        sql.append(" left join sys_corp sc2 on sc2.id = sc.parent_id  ");
        sql.append(" left join sys_corp sc3 on sc3.id = sc2.parent_id  ");
        sql.append(" where avv.type='0' and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95  ");
        sql.append(" and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.brief_id ='004' and ba.opt_status='19' and ba.status=95 ) group by sc.name, avv.BANK_TYPE_ID, avv.type_name, avv.bank_name, avv.bank_acc, av.end_date, bailPer, sc.equityMaintenance_ID,\n" +
                "         sc.operationArea_ID, sc2.id, sc2.name, avv.corp_id,sc3.id, sc3.name, avv.corp_id,sc.sort,sc2.sort,sc3.sort ");
        sql.append(" ) m  ");
        sql.append(" left join sys_equity_maintenance sem on sem.equity_code = m.equityMaintenanceId ");
        sql.append(" where 1=1  ");
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "m.corpId", params);
        sql.append("and ").append(corpAuthSql).append(" ");
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (m.bankAcc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "m.bankAcc", "bankAccs", params)).append(")");
        }
        String operationAreaCode = (String) dataTablesInput.getFormdata().get("operationAreaCode");
        if (StringUtils.isNotEmpty(operationAreaCode)) {
            List<String> codes = Arrays.asList(operationAreaCode.split(","));
            sql.append(" and (");
            for (int i = 0; i < codes.size(); i++) {
                sql.append(" FIND_IN_SET('").append(codes.get(i)).append("',m.operationAreaCode)");
                if (i != codes.size() - 1) {
                    sql.append(" or ");
                }
            }
            sql.append(" ) ");
            dataTablesInput.getFormdata().remove("operationAreaCode");
        }
        sql.append(" ) a ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillBabVo.class, "a", true, params);
    }

    /**
     * @Description 加载票据解付与解付账户前一天余额对比表数据
     * @author zhous
     * @date 2024/10/10 15:13
     */
    public DataTablesOutput<AbBillBankAccBalCompareVo> dataQueryBankAccBalCompareData(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        String endDateStr = (String) dataTablesInput.getFormdata().get("ged_endDate");
        String ledEndDateStr = (String) dataTablesInput.getFormdata().get("led_endDate");
        Date endDate = DateUtil.parse(endDateStr, "yyyy-MM-dd");
        params.put("gedRepayDate", endDateStr);
        params.put("ledRepayDate", ledEndDateStr);
        Date balDate = null;
        //过期时间大于当前时间,取当前时间实时余额
        if (DateUtil.date().compareTo(endDate) < 0) {
            balDate = DateUtil.date();
        } else {
            balDate = DateUtil.offsetDay(endDate, -1);
        }
        Date balEndDate = DateUtil.offsetDay(balDate, +1);
        params.put("balDate", DateUtil.format(balDate, "yyyy-MM-dd"));
        params.put("balEndDate", DateUtil.format(balEndDate, "yyyy-MM-dd"));
        StringBuilder sql = new StringBuilder(" select a.*,(a.bal - BT_NVL_NUM(a.applyRepayMoney, 0)) as discrepancy from ( ");
        sql.append("  select m.parentId,m.parentName,m.operationAreaCode,m.corpId,m.corpName,m.equityCode,m.bankAcc,m.billMoney,m.bailPercent,");
        sql.append("  m.bailMoney,case when m.BAILPERCENT <> 1 or m.BAILPERCENT is null then BT_NVL_NUM(m.billMoney,0) + BT_NVL_NUM(l.repayTotalMoney,0) else BT_NVL_NUM(l.repayTotalMoney,0) end  as applyRepayMoney,m.applicant,sem.Equity_NAME as equityName,bba.BANK_TYPE_ID as bankTypeId,bbt.type_name as bankName,BT_NVL_NUM(l.loanMoney,0) as loanMoney,");
        sql.append("  BT_NVL_NUM(b.AVAIL_BAL, 0) as bal,BT_NVL_NUM(l.repayTotalMoney, 0) as repayTotalMoney,m.rmk from");
        sql.append("  (select sc.operationArea_ID as operationAreaCode,avv.corp_id as corpId,sc.name as corpName,sc.equityMaintenance_ID as equityCode,avv.bank_acc as bankAcc,");
        sql.append("  sum(av.bill_money) as billMoney,a.BAILPERCENT as bailPercent,sum(a.bailmoney) as bailMoney,");
        sql.append("  group_concat(distinct BT_NVL_CHAR(avv.CREATE_NAME,a.CREATE_NAME) separator ',') as applicant,case when group_concat(distinct av.bill_type separator ',') like '%11%' then '商票' end as rmk, ");
        sql.append("  case when sc.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc.id when sc2.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc2.id else sc3.id end as parentId, ");
        sql.append("  case when sc.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc.name  when sc2.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc2.name else sc3.name end as parentName ");
        sql.append("  from ab_voucherinfo_vice avv ");
        sql.append("  left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append("  left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001' ");
        sql.append("  left join sys_corp sc on sc.id = avv.corp_id ");
        sql.append("  left join sys_corp sc2 on sc2.id = sc.parent_id ");
        sql.append("  left join sys_corp sc3 on sc3.id = sc2.parent_id ");
        sql.append("  where  avv.type='0' and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95 ");
        sql.append("  and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.opt_status='19' and ((ba.brief_id = '004' and ba.status = 95) or ( ba.brief_id = '6120' and ba.status = 100)) ) ");
        sql.append("  and av.end_date >= bt_to_date(:gedRepayDate,'yyyy-MM-dd')  and av.end_date <= bt_to_date(:ledRepayDate,'yyyy-MM-dd') ");
        sql.append("  group by  avv.bank_acc,a.BAILPERCENT,sc2.id,sc2.name,sc.operationArea_ID,avv.corp_id,sc.name,sc.equityMaintenance_ID,sc3.name,sc3.id ) m  ");
        sql.append("  left join sys_equity_maintenance sem on sem.equity_code = m.equityCode ");
        sql.append("  left join bt_bank_acc bba on bba.bank_acc = m.bankAcc ");
        sql.append("  left join bt_bank_type bbt on bbt.id = bba.BANK_TYPE_ID ");
        sql.append("  left join ( select BT_NVL_NUM(sum(cpi.PL_MONEY),0) as loanMoney,cpi.ONLINE_PAYMENT_ACC as bankAcc ,ROUND(BT_NVL_NUM(sum(cpp.repayMoney), 0) + BT_NVL_NUM(sum(cpp.interest), 0),2) as repayTotalMoney ");
        sql.append("  from cms_provide_loan_info cpi ");
        sql.append("  left join (select cps.loan_code as loanCode,BT_NVL_NUM(sum(case when cp.status ='0' then cp.REPAY_MONEY else 0 end),0) as repayMoney, ");
        sql.append("  BT_NVL_NUM(sum(case when cp.status ='0' then (case when cp.REPAY_MONEY > 0 then cp.INTEREST else cp.PAY_INTEREST end   ) else 0 end) ,0) as interest ");
        sql.append("  from cms_provide_loan_send cps left join cms_provide_interest cp on cps.bill_code = cp.bill_code where cp.REPAY_DATE >= bt_to_date(:gedRepayDate,'yyyy-MM-dd') and cp.REPAY_DATE <= bt_to_date(:ledRepayDate,'yyyy-MM-dd') group by  cps.loan_code ) cpp ");
        sql.append("  on cpp.loanCode = cpi.bill_code ");
        sql.append("  where cpi.status >= 95 and cpi.REPAY_DATE >= bt_to_date(:gedRepayDate,'yyyy-MM-dd') group by cpi.ONLINE_PAYMENT_ACC  ) l on l.bankAcc = m.bankAcc ");
        sql.append("  left join bis_acc_bal b on b.bank_acc = m.bankAcc and b.bal_date >=bt_to_date(:balDate,'yyyy-MM-dd') and b.bal_date < bt_to_date(:balEndDate,'yyyy-MM-dd') ");
        sql.append(" where 1=1  ");
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "m.corpId", params);
        sql.append("and ").append(corpAuthSql).append(" ");
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (m.bankAcc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "m.bankAcc", "bankAccs", params)).append(")");
        }
        sql.append("  ) a  ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillBankAccBalCompareVo.class, "", true, params);
    }

    /**
     * 银承开票金额统计表
     *
     * @param form         表单数据
     * @param authCorpList 有权限集合的单位集合
     * @return 返回列表数据信息
     */
    public List<AbBillBankOpenBillAmtStatisticsVo> AbBillBankOpenBillAmtStatisticsVo(AbBillBankOpenBillAmtStatisticsVo form, List<String> authCorpList) {
        Map<String, Object> params = Maps.newHashMap();
        String startQueryDate = form.getStartQueryDate();
        String endQueryDate = form.getEndQueryDate();
        params.put("startQueryDate", startQueryDate);
        params.put("endQueryDate", endQueryDate);
        params.put("startQueryDate1", startQueryDate + " 00:00:00");
        params.put("endQueryDate1", endQueryDate + " 23:59:59");
        StringBuilder sql = new StringBuilder(" select m.* from ( ");
        sql.append(" select b.corpId, b.corpName, b.corpCode, b.parendIds, b.bankCode, b.bankName, b.bankTypeId, ");
        // 授信总额
        sql.append(" BT_NVL_NUM(b.ratMoneySum, 0) as ratMoneySum,");
        // 其中：敞口
        sql.append(" BT_NVL_NUM(b.openMoneySum, 0) as openMoneySum,");
        // 其中：低风险
        sql.append(" BT_NVL_NUM(b.lowRiskMoneySum, 0) as lowRiskMoneySum,");
        // 合同到期日
        sql.append(" b.contractEndDate, ");
        // 流贷金额
        sql.append(" BT_NVL_NUM(b.loanMoney, 0) as loanMoney, ");
        // 本月止未到期开票金额
        sql.append(" BT_NVL_NUM(a.unexpiredBillMoney, 0) as unexpiredBillMoney, ");
        // 本月止保证金余额
        sql.append(" BT_NVL_NUM(a.unexpiredBailMoney, 0) as unexpiredBailMoney, ");
        // 本月末授信剩余额度
        sql.append(" BT_NVL_NUM(b.leftQuota, 0) as leftQuota, ");
        // 其中：电汇总额
        sql.append(" BT_NVL_NUM(c.ebankMoney, 0) as ebankMoney, ");
        // 其中：开票总额
        sql.append(" BT_NVL_NUM(d.openBillTotalMoney,0) as openBillTotalMoney, ");
        // 支付总额
        sql.append(" BT_NVL_NUM(c.ebankMoney,0) + BT_NVL_NUM(d.openBillTotalMoney,0) as payTotalMoney, ");
        // 敞口：三个月，敞口：六个月
        sql.append(" BT_NVL_NUM(e.openBillThreeMonth,0) as openBillThreeMonth, BT_NVL_NUM(f.openBillSixMonth,0) as openBillSixMonth, ");
        // 低风险：三个月，低风险：六个月
        sql.append(" BT_NVL_NUM(g.lowRiskThreeMonth,0) as lowRiskThreeMonth, BT_NVL_NUM(h.lowRiskSixMonth,0) as lowRiskSixMonth, ");
        // 商承：三个月，商承：六个月
        sql.append(" BT_NVL_NUM(i.businessBillThreeMonth,0) as businessBillThreeMonth, BT_NVL_NUM(j.businessBillSixMonth,0) as businessBillSixMonth, ");
        // 开票占比
        sql.append(" case when (BT_NVL_NUM(c.ebankMoney, 0) + BT_NVL_NUM(d.openBillTotalMoney, 0)) = 0 then 0 ");
        sql.append(" else BT_NVL_NUM(d.openBillTotalMoney, 0)/(BT_NVL_NUM(c.ebankMoney, 0)+BT_NVL_NUM(d.openBillTotalMoney, 0)) end as openBillPercent, ");
        // 6个月银承占比
        sql.append(" case when BT_NVL_NUM(d.openBillTotalMoney, 0) = 0 then 0 ");
        sql.append(" else (BT_NVL_NUM(f.openBillSixMonth, 0) + BT_NVL_NUM(h.lowRiskSixMonth, 0) + BT_NVL_NUM(j.businessBillSixMonth, 0))/BT_NVL_NUM(d.openBillTotalMoney, 0) end as sixMonthBankBillPercent, ");
        // 本月未到期（其中：低风险），本月未到期（其中：敞口）
        sql.append(" BT_NVL_NUM(k.lowRiskUnexpiredBillMoney,0) as lowRiskUnexpiredBillMoney, BT_NVL_NUM(l.openUnexpiredBillMoney,0) as openUnexpiredBillMoney,");
        // 敞口额度已使用总额（票面口径）
        sql.append(" BT_NVL_NUM(l.openUnexpiredBillMoney,0) + BT_NVL_NUM(b.loanMoney,0) as useBillMoneyByBill, ");
        // 敞口额度已使用总额（银行口径）
        sql.append(" BT_NVL_NUM(b.useBillMoneyByBank,0) as useBillMoneyByBank ");
        sql.append(" from ");
        //统计汇总授信信息：（以授信合同为主关联信息）
        getRatStatisticsSql(sql);
        //根据单位和银行类型（大行）维度分组
        getOpenBillInfoSql(sql);
        //统计汇总电汇付款和应付票据信息
        getPayMoneyStatisticsSql(sql);
        //统计 敞口 低风险 商承 汇总数据
        getBillStatisticsSql(sql);
        //统计 本月未到期数据
        getBillCurrentMonthsql(sql);
        sql.append(" ) m where 1 = 1 ");
        String corpAuthSql = SqlUtil.getInSql(authCorpList, "m.corpId", true);
        sql.append(" and ").append(corpAuthSql).append(" ");
        // 单位不为空
        if (StringUtils.isNotEmpty(form.getCorpId())) {
            List<String> corpIdList = Arrays.asList(form.getCorpId().split(CommonConstant.COMMA_SPLIT));
            sql.append(" and m.corpId in (:corpIdList) ");
            params.put("corpIdList", corpIdList);
        }
        // 银行类型不为空
        if (StringUtils.isNotEmpty(form.getBankTypeId()) && StringUtils.isNotEmpty(form.getBankTypeId().trim())) {
            sql.append(" and m.bankTypeId = :bankTypeId ");
            params.put("bankTypeId", form.getBankTypeId());
        }
        return this.listBySQLAliasToBean(sql.toString(), params, AbBillBankOpenBillAmtStatisticsVo.class);
    }

    //统计 本月未到期数据
    private void getBillCurrentMonthsql(StringBuilder sql) {
        sql.append("  left join ( ");
        sql.append("  select avv.corp_id as corpId, avv.bank_type_id as bankTypeId, avv.bank_code as bankCode, avv.bank_name as bankName, BT_NVL_NUM(sum(av.bill_money),0) as lowRiskUnexpiredBillMoney ");
        sql.append("  from ab_voucherinfo_vice avv ");
        sql.append("  left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append("  left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001'  ");
        sql.append("  where avv.type='0' and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95 ");
        sql.append("  and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.brief_id ='004' and ba.opt_status='19' and ba.status=95 )  and a.bailpercent = 1 ");
        sql.append("  and av.start_date <= bt_to_date(:endQueryDate,'yyyy-MM-dd') and av.end_date > bt_to_date(:endQueryDate,'yyyy-MM-dd') ");
        sql.append("  group by avv.corp_id,avv.bank_type_id,avv.bank_code,avv.bank_name ");
        sql.append("  ) k on b.corpId = k.corpId and b.bankTypeId = k.bankTypeId ");
        sql.append("  left join ( ");
        sql.append("  select avv.corp_id as corpId, avv.bank_type_id as bankTypeId, avv.bank_code as bankCode, avv.bank_name as bankName, BT_NVL_NUM(sum(av.bill_money),0) as openUnexpiredBillMoney ");
        sql.append("  from ab_voucherinfo_vice avv ");
        sql.append("  left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append("  left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001'  ");
        sql.append("  where avv.type='0' and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95 ");
        sql.append("  and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.brief_id ='004' and ba.opt_status='19' and ba.status=95 )  and a.bailpercent != 1 ");
        sql.append("  and av.start_date <= bt_to_date(:endQueryDate,'yyyy-MM-dd') and av.end_date > bt_to_date(:endQueryDate,'yyyy-MM-dd') ");
        sql.append("  group by avv.corp_id,avv.bank_type_id,avv.bank_code,avv.bank_name ");
        sql.append("  ) l on b.corpId = l.corpId and b.bankTypeId = l.bankTypeId ");
    }

    //统计 票据信息
    private void getOpenBillInfoSql(StringBuilder sql) {
        sql.append("  left join ( ");
        sql.append("  select avv.corp_id as corpId, avv.bank_type_id as bankTypeId, avv.bank_code as bankCode, avv.bank_name as bankName, BT_NVL_NUM(sum(av.bill_money), 0) as unexpiredBillMoney, BT_NVL_NUM(sum(a.bailmoney), 0) as unexpiredBailMoney ");
        sql.append("  from ab_voucherinfo_vice avv ");
        sql.append("  left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append("  left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001' ");
        // 只取银票bill_type=10
        sql.append("  where avv.type='0' and av.bill_type = 10 and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95  ");
        sql.append("  and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.brief_id ='004' and ba.opt_status='19' and ba.status=95 )  ");
        sql.append("  and av.end_date > bt_to_date(:endQueryDate,'yyyy-MM-dd')  and av.start_date <= bt_to_date(:endQueryDate,'yyyy-MM-dd') ");
        sql.append("  group by avv.bank_type_id,avv.corp_id,avv.bank_code,avv.bank_name ");
        sql.append("  ) a on a.corpId = b.corpId and a.bankTypeId = b.bankTypeId ");
    }

    //统计 敞口 低风险 商承 汇总数据
    private void getBillStatisticsSql(StringBuilder sql) {
        sql.append(" left join ( ");
        sql.append(" select avv.corp_id as corpId, avv.bank_type_id as bankTypeId, avv.bank_code as bankCode, avv.bank_name as bankName, BT_NVL_NUM(sum(av.bill_money),0) as openBillThreeMonth ");
        sql.append(" from ab_voucherinfo_vice avv ");
        sql.append(" left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append(" left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001' ");
        sql.append(" where avv.bail_percent != 1 and avv.type='0' and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95 ");
        sql.append(" and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.brief_id ='004' and ba.opt_status='19' and ba.status=95 ) ");
        sql.append(" and av.BILL_TYPE=10 and av.start_date >= bt_to_date(:startQueryDate,'yyyy-MM-dd') and av.start_date <= bt_to_date(:endQueryDate,'yyyy-MM-dd') and BT_DATEDIFF2(av.end_date,av.start_date) <=93 ");
        sql.append(" group by avv.corp_id,avv.bank_type_id,avv.bank_code,avv.bank_name ) e on b.corpId = e.corpId and b.bankTypeId = e.bankTypeId ");
        sql.append(" left join ( ");
        sql.append(" select avv.corp_id as corpId, avv.bank_type_id as bankTypeId, avv.bank_code as bankCode, avv.bank_name as bankName, BT_NVL_NUM(sum(av.bill_money),0) as openBillSixMonth ");
        sql.append(" from ab_voucherinfo_vice avv ");
        sql.append(" left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append(" left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001' ");
        sql.append(" where avv.bail_percent != 1 and avv.type='0' and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95 ");
        sql.append(" and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.brief_id ='004' and ba.opt_status='19' and ba.status=95 ) ");
        sql.append(" and av.BILL_TYPE=10 and av.start_date >= bt_to_date(:startQueryDate,'yyyy-MM-dd') and av.start_date <= bt_to_date(:endQueryDate,'yyyy-MM-dd') and BT_DATEDIFF2(av.end_date,av.start_date) >93 ");
        sql.append(" group by avv.corp_id,avv.bank_type_id,avv.bank_code,avv.bank_name ) f on b.corpId = f.corpId and b.bankTypeId = f.bankTypeId ");
        sql.append(" left join ( ");
        sql.append(" select avv.corp_id as corpId, avv.bank_type_id as bankTypeId, avv.bank_code as bankCode, avv.bank_name as bankName, BT_NVL_NUM(sum(av.bill_money),0) as lowRiskThreeMonth ");
        sql.append(" from ab_voucherinfo_vice avv ");
        sql.append(" left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append(" left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001' ");
        sql.append(" where avv.type='0' and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95 ");
        sql.append(" and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.brief_id ='004' and ba.opt_status='19' and ba.status=95 ) ");
        sql.append(" and a.bailpercent = 1 and av.BILL_TYPE=10 and av.start_date >= bt_to_date(:startQueryDate,'yyyy-MM-dd') and av.start_date <= bt_to_date(:endQueryDate,'yyyy-MM-dd') and BT_DATEDIFF2(av.end_date,av.start_date) <=93 ");
        sql.append(" group by avv.corp_id,avv.bank_type_id,avv.bank_code,avv.bank_name ) g on b.corpId = g.corpId and b.bankTypeId = g.bankTypeId ");
        sql.append(" left join ( ");
        sql.append(" select avv.corp_id as corpId, avv.bank_type_id as bankTypeId, avv.bank_code as bankCode, avv.bank_name as bankName, BT_NVL_NUM(sum(av.bill_money),0) as lowRiskSixMonth ");
        sql.append(" from ab_voucherinfo_vice avv ");
        sql.append(" left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append(" left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001' ");
        sql.append(" where avv.type='0' and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95 ");
        sql.append(" and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.brief_id ='004' and ba.opt_status='19' and ba.status=95 ) ");
        sql.append(" and a.bailpercent = 1 and av.BILL_TYPE=10 and av.start_date >= bt_to_date(:startQueryDate,'yyyy-MM-dd') and av.start_date <= bt_to_date(:endQueryDate,'yyyy-MM-dd') and BT_DATEDIFF2(av.end_date,av.start_date) >93 ");
        sql.append(" group by avv.corp_id,avv.bank_type_id,avv.bank_code,avv.bank_name ) h on b.corpId = h.corpId and b.bankTypeId = h.bankTypeId ");
        sql.append(" left join ( ");
        sql.append(" select avv.corp_id as corpId, avv.bank_type_id as bankTypeId, avv.bank_code as bankCode, avv.bank_name as bankName, BT_NVL_NUM(sum(av.bill_money),0) as businessBillThreeMonth ");
        sql.append(" from ab_voucherinfo_vice avv ");
        sql.append(" left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append(" left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001' ");
        sql.append(" where avv.type='0' and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95 ");
        sql.append(" and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.brief_id ='004' and ba.opt_status='19' and ba.status=95 ) ");
        sql.append(" and av.BILL_TYPE=11 and av.start_date >= bt_to_date(:startQueryDate,'yyyy-MM-dd') and av.start_date <= bt_to_date(:endQueryDate,'yyyy-MM-dd') and BT_DATEDIFF2(av.end_date,av.start_date) <=93 ");
        sql.append(" group by avv.corp_id,avv.bank_type_id,avv.bank_code,avv.bank_name ) i on b.corpId = e.corpId and b.bankTypeId = i.bankTypeId ");
        sql.append(" left join ( ");
        sql.append(" select avv.corp_id as corpId, avv.bank_type_id as bankTypeId, avv.bank_code as bankCode, avv.bank_name as bankName, BT_NVL_NUM(sum(av.bill_money),0) as businessBillSixMonth ");
        sql.append(" from ab_voucherinfo_vice avv ");
        sql.append(" left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append(" left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001' ");
        sql.append(" where avv.type='0' and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95 ");
        sql.append(" and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.brief_id ='004' and ba.opt_status='19' and ba.status=95 ) ");
        sql.append(" and av.BILL_TYPE=11 and av.start_date >= bt_to_date(:startQueryDate,'yyyy-MM-dd') and av.start_date <= bt_to_date(:endQueryDate,'yyyy-MM-dd') and BT_DATEDIFF2(av.end_date,av.start_date) >93 ");
        sql.append(" group by avv.corp_id,avv.bank_type_id,avv.bank_code,avv.bank_name ) j on b.corpId = f.corpId and b.bankTypeId = j.bankTypeId ");
    }

    //统计汇总电汇付款和应付票据信息
    private void getPayMoneyStatisticsSql(StringBuilder sql) {
        sql.append(" left join ( ");
        sql.append(" select n.corp_id as corpId, bba.bank_type_id as bankTypeId, BT_NVL_NUM(sum(n.pay_money),0) as ebankMoney ");
        sql.append(" from nis_billhead n ");
        sql.append(" left join bt_bank_acc bba on bba.bank_acc = n.payer_acc ");
        sql.append(" left join fc_voucher_type fvt on n.VOUCHER_TYPE_ID = fvt.ID ");
        sql.append(" left join nis_bill_bank_serial_no nbbs on nbbs.bill_id = n.id ");
        sql.append(" left join bis_acc_dtl bad on bad.id = nbbs.dtl_id  ");
        sql.append(" left join nis_bus_type nbs on nbs.id = n.BUS_TYPE ");
        sql.append(" where fvt.SYS_VOUCHER_CODE = '34' and n.status >= 95 and nbbs.bill_id is not null and nbbs.bill_id != '' ");
        sql.append(" and nbs.CD_SIGN = '1' and nbs.VALID_SIGN = '1' and (nbs.BUS_TYPE_NAME = '共享结算-预付单' or nbs.BUS_TYPE_NAME = '共享结算-付款明细单') ");
        sql.append(" and bad.trans_time >= bt_to_date(:startQueryDate1,'yyyy-MM-dd HH24:mi:ss') ");
        sql.append(" and bad.trans_time <= bt_to_date(:endQueryDate1,'yyyy-MM-dd HH24:mi:ss') ");
        sql.append(" group by n.corp_id, bba.bank_type_id) c on b.corpId = c.corpId and b.bankTypeId = c.bankTypeId ");
        sql.append(" left join ( ");
        sql.append(" select avv.corp_id as corpId, avv.bank_type_id as bankTypeId, avv.bank_code as bankCode, avv.bank_name as bankName, BT_NVL_NUM(sum(bill_money),0) as openBillTotalMoney  ");
        sql.append(" from ab_voucherinfo_vice avv ");
        sql.append(" left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append(" left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001' ");
        sql.append(" where av.start_date >= bt_to_date(:startQueryDate,'yyyy-MM-dd') and av.start_date <= bt_to_date(:endQueryDate,'yyyy-MM-dd') and  avv.type='0' and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95 ");
        sql.append(" and not exists (select 1 from ab_accrecords ba where avv.id = ba.vvid and ba.brief_id ='004' and ba.opt_status='19' and ba.status=95 ) ");
        sql.append(" group by avv.corp_id, avv.bank_type_id, avv.bank_code, avv.bank_name ) d on b.corpId = d.corpId and b.bankTypeId = d.bankTypeId ");
    }

    //统计汇总授信信息
    private void getRatStatisticsSql(StringBuilder sql) {
        sql.append(" ( ");
        sql.append(" select b1.corpId, sc.name as corpName, sc.code as corpCode, sc.PARENT_IDS as parendIds, ");
        sql.append(" b1.bank_code as bankCode, bank.bank_name as bankName, b1.bankTypeId, b1.contractEndDate, ");
        sql.append(" b1.ratMoneySum, b1.openMoneySum, b1.lowRiskMoneySum, b1.loanMoney, b1.leftQuota, b1.useBillMoneyByBank ");
        sql.append(" from ");
        sql.append(" (select rb.corp_id as corpId, rb.bank_type as bankTypeId, rb.bank_code, max(rb.ENDDATE) as contractEndDate, ");
        // 授信总额
        sql.append(" BT_NVL_NUM(sum(rb.money), 0) as ratMoneySum, ");
        // 其中：敞口
        sql.append(" BT_NVL_NUM(sum(ri1.openMoney), 0) as openMoneySum, ");
        // 其中：低风险
        sql.append(" BT_NVL_NUM(sum(ri2.openMoney), 0) as lowRiskMoneySum, ");
        // 流贷金额
        sql.append(" BT_NVL_NUM(sum(rr.loanMoney), 0) as loanMoney, ");
        // 本月末授信剩余额度
        sql.append(" BT_NVL_NUM(sum(rb.money), 0) - BT_NVL_NUM(sum(t2.useRatTotalMoney), 0) as leftQuota, ");
        sql.append(" BT_NVL_NUM(sum(t2.useRatTotalMoney), 0) - BT_NVL_NUM(sum(t1.useRatBillMoney), 0) as useBillMoneyByBank ");
        sql.append(" from ");
        // 授信主表
        sql.append(" (SELECT a.id, a.corp_id, (BT_NVL_NUM(a.money, 0) + BT_NVL_NUM(rbadd.addMoney, 0)) as money, a.BEGINDATE, a.ENDDATE, a.status, a.bank_type, a.bank_code ");
        sql.append(" FROM RAT_BKRATION a ");
        // 授信追加
        sql.append(" LEFT JOIN ( select rba.rat_code, sum(rba.MONEY) as addMoney from rat_bkration_add rba where rba.status >= 95 group by rba.rat_code ) rbadd on rbadd.rat_code = a.ID ");
        // 先过滤再取同单位同银行同支行取授信结束日期最大的那条 --开始
        sql.append(" INNER JOIN ( select maxRb.corp_id, maxRb.bank_type, maxRb.bank_code, max(maxRb.enddate)as maxEndDate   ");
        sql.append(" from RAT_BKRATION maxRb where maxRb.status >= 95 ");
        // 过期且未占用，也需要剔除合同，此处sql写的意思是：授信合同到期日期 >= 查询日期 或者 该合同存在授信占用
        sql.append(" and (maxRb.ENDDATE >= bt_to_date(:startQueryDate, 'yyyy-MM-dd') or exists (select 1 from rat_uration ru where ru.rat_code = maxRb.id and ru.status in (1, 2)))");
        // 如果一个合同中只有一个品种，且为“并购贷款”或者、“项目贷款”或者“固定资产贷款”时，将这个合同剔除掉
        sql.append(" and not exists ( select 1 from rat_bkration_detail rbd left join rat_item_custom ric on rbd.item_code = ric.id ");
        sql.append(" where rbd.rat_code = maxRb.id group by rbd.rat_code having count(*) = 1 and (max(ric.name) = '并购贷款' or max(ric.name) = '项目贷款' or max(ric.name) = '固定资产贷款')) ");
        sql.append(" group by corp_id, bank_type, bank_code ");
        // 先过滤再取同单位同银行同支行取授信结束日期最大的那条 -- 结束

        sql.append("  ) a1 ON a.corp_id = a1.corp_id AND a.enddate = a1.maxEndDate and a.bank_type = a1.bank_type and a.bank_code = a1.bank_code ");
        sql.append(" ) rb ");
        sql.append(" left join (select ru.rat_code as ratCode,BT_NVL_NUM(sum(ru.money),0) as loanMoney from rat_uration ru left join rat_item_custom ric on ric.id = ru.item_code left join rat_item_type rit on rit.id = ric.rit_code where rit.rit_code ='05' and ru.status = 2 group by ru.rat_code ) rr on rb.id = rr.ratCode ");
        sql.append(" left join (select rbd.rat_code as ratCode, case when rbd.item_flag = '1' then rbd.max_money else rbd.open_money end as openMoney from rat_bkration_detail rbd left join rat_item_custom ric on rbd.item_code = ric.id where ric.name = '银行承兑汇票（敞口）' and ric.status = 0) ri1 on ri1.ratCode = rb.id ");
        sql.append(" left join (select rbd.rat_code as ratCode, case when rbd.item_flag = '1' then rbd.max_money else rbd.open_money end as openMoney from rat_bkration_detail rbd left join rat_item_custom ric on rbd.item_code = ric.id where ric.name = '银行承兑汇票（低风险）' and ric.status = 0) ri2 on ri2.ratCode = rb.id ");
        sql.append(" left join (select ru.rat_code as ratCode, BT_NVL_NUM(sum(ru.pre_money),0) as useRatBillMoney from rat_uration ru where ru.syscode = 'accebill' and ru.status = 2 and ru.ASSUREPERCENT = 1 group by ru.rat_code ) t1 on rb.id = t1.ratCode ");
        sql.append(" left join (select ru.rat_code as ratCode, BT_NVL_NUM(sum(ru.pre_money),0) as useRatTotalMoney from rat_uration ru where ru.status in (1, 2) and ru.BEGIN_DATE <= bt_to_date(:endQueryDate1, 'yyyy-MM-dd HH24:mi:ss') group by ru.rat_code ) t2 on rb.id = t2.ratCode ");
        sql.append(" where rb.status>=95 and ( ((rb.BEGINDATE <= bt_to_date(:startQueryDate,'yyyy-MM-dd') and rb.ENDDATE >= bt_to_date(:startQueryDate,'yyyy-MM-dd')) or (rb.BEGINDATE <= bt_to_date(:endQueryDate,'yyyy-MM-dd') and rb.ENDDATE >= bt_to_date(:endQueryDate,'yyyy-MM-dd')) ) ");
        sql.append(" or ( (rb.BEGINDATE <= bt_to_date(:startQueryDate,'yyyy-MM-dd') and DATE_ADD(rb.ENDDATE, INTERVAL 180 DAY) >= bt_to_date(:startQueryDate,'yyyy-MM-dd')) or (rb.BEGINDATE <= bt_to_date(:endQueryDate,'yyyy-MM-dd') and DATE_ADD(rb.ENDDATE, INTERVAL 180 DAY) >= bt_to_date(:endQueryDate,'yyyy-MM-dd')) ) )");
        sql.append(" group by rb.corp_id, rb.bank_type, rb.bank_code ");
        sql.append(" ) b1 ");
        sql.append(" left join bt_input_bank_info bank on bank.sys_bank_code = b1.bank_code ");
        sql.append(" left join sys_corp sc on sc.id = b1.corpId ");
        // 单位有效且只取值100网点下单位
        sql.append(" where sc.STATUS = 1 and sc.NET_ID = (SELECT id FROM sys_corp WHERE CODE = '100') ");
        // 排除网点自己
        sql.append(" and sc.code != '100' ");
        // 排除管理单位为“（6000）大区”的单位
        sql.append(" and (sc.UNIT_CM is null or sc.UNIT_CM != (SELECT id FROM sys_cell_manage WHERE cell_code = '6000')) ");
        sql.append(" ) b ");
    }

    /**
     * 银承占比及额度使用情况统计表数据
     *
     * @param ym           查询年月
     * @param authCorpList 有权限单位集合
     * @author fangcf
     * @date 2025-09-12
     */
    public List<AbBillTargetProportionAndQuotaUsageVo> listAcUsageRatioStatistics(String ym, List<String> authCorpList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select m.corpId, m.corpName, m.parendIds, m.unitStoreMaintenanceId, m.targetProportion, m.actual, m.preActual, m.difference, m.increaseProportion, ");
        sql.append(" m.bankCredit, m.leftQuota, m.unexpiredBillMoney, m.totalPay, m.eBankPay, m.openBillPay, m.preEBankPay, m.preOpenBillPay ");
        sql.append(" from ( ");
        sql.append(" select aeb.corp_id as corpId, sc.name as corpName, sc.PARENT_IDS as parendIds, sc.unitStoreMaintenance_ID as unitStoreMaintenanceId, ");
        // 目标
        sql.append(" BT_NVL_NUM(aeb.TARGET_PROPORTION,0) as targetProportion, ");
        // 实际 = 本月总开票金额 / （本月货款电汇金额 + 本月总开票金额）
        sql.append(" BT_NVL_NUM(ROUND(BT_NVL_NUM(b.openBillPay,0)/(BT_NVL_NUM(a.eBankPay,0)+BT_NVL_NUM(b.openBillPay,0)),4),0) as actual, ");
        // 上月实际
        sql.append(" BT_NVL_NUM(ROUND(BT_NVL_NUM(b1.preOpenBillPay,0)/(BT_NVL_NUM(a1.preEBankPay,0)+BT_NVL_NUM(b1.preOpenBillPay,0)),4),0) as preActual, ");
        // 差异 = 实际 - 目标
        sql.append(" BT_NVL_NUM(ROUND(BT_NVL_NUM(b.openBillPay,0)/(BT_NVL_NUM(a.eBankPay,0)+BT_NVL_NUM(b.openBillPay,0)),4),0) - aeb.target_proportion as difference, ");
        // 提升比例 = 本月实际 - 上月实际
        sql.append(" BT_NVL_NUM(ROUND(BT_NVL_NUM(b.openBillPay,0)/(BT_NVL_NUM(a.eBankPay,0)+BT_NVL_NUM(b.openBillPay,0)),4),0) - BT_NVL_NUM(ROUND(BT_NVL_NUM(b1.preOpenBillPay,0)/(BT_NVL_NUM(a1.preEBankPay,0)+BT_NVL_NUM(b1.preOpenBillPay,0)),4),0) as increaseProportion, ");
        // 本月上月货款电汇金额
        sql.append(" BT_NVL_NUM(a.eBankPay,0) as eBankPay, BT_NVL_NUM(a1.preEBankPay,0) as preEBankPay, ");
        // 本月上月总开票金额
        sql.append(" BT_NVL_NUM(b.openBillPay,0) as openBillPay, BT_NVL_NUM(b1.preOpenBillPay,0) as preOpenBillPay, ");
        // 本月止未到期开票金额
        sql.append(" BT_NVL_NUM(c.unexpiredBillMoney,0) as unexpiredBillMoney, ");
        // 银行额度（bankCredit）
        sql.append(" BT_NVL_NUM(d.bankCredit,0) as bankCredit, ");
        // 本月末授信剩余额度 = 银行额度 - 已占用授信（包括在途）
        sql.append(" BT_NVL_NUM(d.bankCredit,0) - BT_NVL_NUM(d.useCreditMoney,0) as leftQuota, ");
        // 本月货款支付总额（totalPay） = 本月货款电汇金额（eBankPay） + 本月总开票金额（openBillPay）
        sql.append(" BT_NVL_NUM(a.eBankPay,0) + BT_NVL_NUM(b.openBillPay,0) as totalPay ");
        sql.append(" from ab_emit_bill_target_ratio_info aeb ");
        sql.append(" left join ( ");
        //获取当前时间系统付款方式为“外付网银”的付款单关联的流水金额之和
        getEBankPaySql(sql, "0");
        sql.append(" ) a on a.corpId = aeb.corp_id ");
        sql.append(" left join ( ");
        //获取上月系统付款方式为“外付网银”的付款单关联的流水金额之和
        getEBankPaySql(sql, "1");
        sql.append(" ) a1 on a1.corpId = aeb.corp_id ");
        sql.append(" left join ( ");
        //获取本月总开票金额
        getOpenBillPaySql(sql, "0");
        sql.append(" ) b on b.corpId = aeb.corp_id ");
        sql.append(" left join ( ");
        //获取上月总开票金额
        getOpenBillPaySql(sql, "1");
        sql.append(" ) b1 on b1.corpId = aeb.corp_id ");
        sql.append(" left join ( ");
        //获取本月总开票未到期金额
        getUnexpiredBillMoneySql(sql);
        sql.append(" ) c on c.corpName = aeb.corp_name ");
        //获取授信额度
        getBankCreditSql(sql);
        sql.append(" left join sys_corp sc on sc.id = aeb.corp_id ");
        sql.append(" where aeb.is_delete is null and aeb.current_year = :queryYear ");
        // 单位有效且只取值100网点下单位
        sql.append(" and sc.STATUS = 1 and sc.NET_ID = (SELECT id FROM sys_corp WHERE CODE = '100') ");
        // 排除网点自己
        sql.append(" and sc.code != '100' ");
        // 排除管理单位为“（6000）大区”的单位
        sql.append(" and (sc.UNIT_CM is null or sc.UNIT_CM != (SELECT id FROM sys_cell_manage WHERE cell_code = '6000')) ");
        String corpAuthSql = SqlUtil.getInSql(authCorpList, "aeb.corp_id", true);
        sql.append("and ").append(corpAuthSql).append(" ");
        sql.append(" ) m ");

        Map<String, Object> params = Maps.newHashMap();
        this.getAcUsageRatioStatisticsParams(ym, params);

        return this.listBySQLAliasToBean(sql.toString(), params, AbBillTargetProportionAndQuotaUsageVo.class);
    }

    /**
     * 获取日期参数
     *
     * @author zhous
     * @date 2025/4/24 14:37
     */
    private void getAcUsageRatioStatisticsParams(String ym, Map<String, Object> params) {
        String year = ym.substring(0, 4);
        DateTime queryDate = DateUtil.parse(ym + "-01");
        DateTime preQueryDate = DateUtil.offsetMonth(queryDate, -1);
        String startQueryDate = DateUtil.format(DateUtil.beginOfMonth(queryDate), "yyyy-MM-dd");
        String endQueryDate = DateUtil.format(DateUtil.endOfMonth(queryDate), "yyyy-MM-dd");
        String preStartQueryDate = DateUtil.format(DateUtil.beginOfMonth(preQueryDate), "yyyy-MM-dd");
        String preEndQueryDate = DateUtil.format(DateUtil.endOfMonth(preQueryDate), "yyyy-MM-dd");
        //查询时间参数
        params.put("queryYear", year);
        params.put("startQueryDate", startQueryDate);
        params.put("endQueryDate", endQueryDate);
        params.put("preStartQueryDate", preStartQueryDate);
        params.put("preEndQueryDate", preEndQueryDate);
        params.put("startQueryDateTime", startQueryDate + " 00:00:00");
        params.put("endQueryDateTime", endQueryDate + " 23:59:59");
        params.put("preStartQueryDateTime", preStartQueryDate + " 00:00:00");
        params.put("preEndQueryDateTime", preEndQueryDate + " 23:59:59");
    }

    /**
     * @Description 获取授信额度
     * @author zhous
     * @date 2025/4/24 14:28
     */
    private void getBankCreditSql(StringBuilder sql) {
        sql.append(" left join ( ");
        sql.append(" select tem.corpId as corpId, BT_NVL_NUM(sum(tem.bankCredit), 0) as bankCredit, BT_NVL_NUM(sum(tem.useCreditMoney), 0) as useCreditMoney ");
        sql.append(" from ( ");
        sql.append(" select rb.corp_id as corpId, rb.money as bankCredit, ");
        sql.append(" ( select BT_NVL_NUM(sum(ru.money), 0) from rat_uration ru ");
        sql.append(" where rb.id = ru.rat_code and rb.corp_id = ru.corp_id and ru.status in (1,2) and ru.end_date > bt_to_date(:endQueryDateTime,'yyyy-MM-dd HH24:mi:ss') ) as useCreditMoney ");
        sql.append(" from RAT_BKRATION rb ");
        sql.append(" where rb.status>=95 and rb.BEGINDATE <= bt_to_date(:endQueryDate,'yyyy-MM-dd') and rb.ENDDATE >= bt_to_date(:startQueryDate,'yyyy-MM-dd') ");
        sql.append(" ) tem group by tem.corpId ");
        sql.append(" ) d on d.corpId = aeb.corp_id  ");
    }

    /**
     * @Description 查询时间范围内（“出票日”）的开票总金额 获取本月总开票未到期金额
     * @author zhous
     * @date 2025/4/24 14:27
     */
    private void getUnexpiredBillMoneySql(StringBuilder sql) {
        sql.append(" select t.corpName, BT_NVL_NUM(sum(t.bill_money), 0) as unexpiredBillMoney ");
        sql.append(" from ( ");
        sql.append(" select avv.corp_id,sc.name,av.bill_money,av.rmk,case when  av.rmk like '%代%' or  av.rmk like '%支付货款%'  then  SUBSTRING(av.rmk, 2, CHARACTER_LENGTH(av.rmk)  -5 ) else sc.name end as corpName ");
        sql.append(" from  ab_voucherinfo_vice avv ");
        sql.append(" left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append(" left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001' ");
        sql.append(" left join sys_corp sc on sc.id = avv.corp_id ");
        sql.append(" where avv.type='0' and av.bill_type = 10 and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95  ");
        sql.append(" and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.brief_id ='004' and ba.opt_status='19' and ba.status=95 ) ");
        sql.append(" and av.end_date > bt_to_date(:endQueryDate,'yyyy-MM-dd')  and av.start_date <= bt_to_date(:endQueryDate,'yyyy-MM-dd')  ");
        sql.append(" ) t group by t.corpName");
    }

    /**
     * @Description 获取系统付款方式为“外付网银”的付款单关联的流水金额之和 type 0-当前查询时间 1-前一个月查询时间
     * @author zhous
     * @date 2025/4/24 14:26
     */
    private void getEBankPaySql(StringBuilder sql, String type) {
        sql.append(" select n.corp_id as corpId , ");
        if ("0".equals(type)) {
            sql.append(" BT_NVL_NUM(sum(bad.amt),0) as eBankPay  ");
        } else {
            sql.append(" BT_NVL_NUM(sum(bad.amt),0) as preEBankPay  ");
        }
        sql.append(" from nis_billhead n ");
        sql.append(" left join fc_voucher_type fvt on n.VOUCHER_TYPE_ID = fvt.ID ");
        sql.append(" left join nis_bill_bank_serial_no nbbs on nbbs.bill_id = n.id ");
        sql.append(" left join bis_acc_dtl bad on bad.id = nbbs.dtl_id  ");
        sql.append(" left join nis_bus_type nbs on nbs.id = n.BUS_TYPE ");
        sql.append(" where fvt.SYS_VOUCHER_CODE = '34' and n.status >= 95 and nbbs.bill_id is not null and nbbs.bill_id != '' ");
        sql.append(" and nbs.CD_SIGN = '1' and nbs.VALID_SIGN = '1' and (nbs.BUS_TYPE_NAME = '共享结算-预付单' or nbs.BUS_TYPE_NAME = '共享结算-付款明细单') ");
        if ("0".equals(type)) {
            sql.append(" and bad.trans_time >= bt_to_date(:startQueryDateTime,'yyyy-MM-dd HH24:mi:ss')  and bad.trans_time <= bt_to_date(:endQueryDateTime,'yyyy-MM-dd HH24:mi:ss') ");
        } else {
            sql.append(" and bad.trans_time >= bt_to_date(:preStartQueryDateTime,'yyyy-MM-dd HH24:mi:ss')  and bad.trans_time <= bt_to_date(:preEndQueryDateTime,'yyyy-MM-dd HH24:mi:ss') ");
        }
        sql.append(" group by n.corp_id ");
    }

    /**
     * 系统付款方式为“应付票据”和“应收票据”在查询时间范围内：
     * 付款方式名称为：三个月银行承兑汇票、六个月银行承兑汇票、三个月商业承兑汇票、六个月商业承兑汇票、三个月银行承兑背书、六个月银行承兑背书”的付款单金额之和
     * type 0-当前查询时间 1-前一个月查询时间
     *
     * @author zhous
     * @date 2025/4/24 14:26
     */
    private void getOpenBillPaySql(StringBuilder sql, String type) {
        sql.append(" select n.corp_id as corpId , ");
        if ("0".equals(type)) {
            sql.append(" BT_NVL_NUM(sum(n.PAY_MONEY),0) as openBillPay  ");
        } else {
            sql.append(" BT_NVL_NUM(sum(n.PAY_MONEY),0) as preOpenBillPay  ");
        }
        sql.append(" from nis_billhead n ");
        sql.append(" left join fc_voucher_type fvt on n.VOUCHER_TYPE_ID = fvt.ID ");
        sql.append(" where n.status >= 95 ");
        sql.append(" and ( ");
        sql.append(" (fvt.SYS_VOUCHER_CODE = '49' and fvt.VOUCHER_NAME = '三个月银行承兑汇票') ");
        sql.append(" or (fvt.SYS_VOUCHER_CODE = '49' and fvt.VOUCHER_NAME = '六个月银行承兑汇票') ");
        sql.append(" or (fvt.SYS_VOUCHER_CODE = '49' and fvt.VOUCHER_NAME = '三个月商业承兑汇票') ");
        sql.append(" or (fvt.SYS_VOUCHER_CODE = '49' and fvt.VOUCHER_NAME = '六个月商业承兑汇票') ");
        sql.append(" or (fvt.SYS_VOUCHER_CODE = '50' and fvt.VOUCHER_NAME = '三个月银行承兑背书') ");
        sql.append(" or (fvt.SYS_VOUCHER_CODE = '50' and fvt.VOUCHER_NAME = '六个月银行承兑背书') ");
        sql.append(" ) ");
        if ("0".equals(type)) {
            sql.append(" and n.WISH_PAYDAY >= bt_to_date(:startQueryDateTime,'yyyy-MM-dd HH24:mi:ss') and n.WISH_PAYDAY <= bt_to_date(:endQueryDateTime,'yyyy-MM-dd HH24:mi:ss') ");
        } else {
            sql.append(" and n.WISH_PAYDAY >= bt_to_date(:preStartQueryDateTime,'yyyy-MM-dd HH24:mi:ss') and n.WISH_PAYDAY <= bt_to_date(:preEndQueryDateTime,'yyyy-MM-dd HH24:mi:ss') ");
        }
        sql.append(" group by n.corp_id ");
    }

    /**
     * 获取区域（二级单位）：单位代码为（100）下级单位，且管理单元为（6000）大区的二级单位
     *
     * @return 二级单位
     */
    public List<CorpDTO> listSecondLevCorp() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select sc.id as id, sc.name as name, sc.sort as sort ");
        sql.append(" from sys_corp sc  ");
        sql.append(" inner join sys_cell_manage scm on scm.id = sc.UNIT_CM ");
        // 单位有效且只取值100网点下单位，管理单元有效且管理单元为“（6000）大区”
        sql.append(" where sc.STATUS = 1 and scm.valid_sign = '1' and scm.cell_code = '6000' and sc.NET_ID = (SELECT id FROM sys_corp WHERE CODE = '100') ");
        return this.listBySQLAliasToBean(sql.toString(), null, CorpDTO.class);
    }

    /**
     * 根据区域（二级单位）集合和当前年份查询银承开票占比目标维护维护表
     *
     * @param currentYear 当前年
     * @param corpIdList  单位集合
     * @return 银承开票占比目标维护维护数据
     */
    public List<AbEmitBillTargetProportionInfoVO> listByCorpIdAndCurrentYear(String currentYear, List<String> corpIdList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select aeb.CORP_ID as corpId, aeb.CURRENT_YEAR as currentYear, aeb.TARGET_PROPORTION as targetProportion, sc.unitStoreMaintenance_ID as unitStoreMaintenanceId ");
        sql.append(" from ab_emit_bill_target_ratio_info aeb ");
        sql.append(" left join sys_corp sc on sc.id = aeb.CORP_ID ");
        sql.append(" where aeb.corp_id in (:corpIdList) and aeb.current_year = :currentYear and aeb.is_delete is null ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("corpIdList", corpIdList);
        params.put("currentYear", currentYear);
        return this.listBySQLAliasToBean(sql.toString(), params, AbEmitBillTargetProportionInfoVO.class);
    }
}
