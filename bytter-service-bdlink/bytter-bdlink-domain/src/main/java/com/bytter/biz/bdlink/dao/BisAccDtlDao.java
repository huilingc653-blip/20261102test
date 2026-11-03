package com.bytter.biz.bdlink.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.dto.NisFlowRulePurposeDTO;
import com.bytter.biz.bdlink.entity.BisAccDtl;
import com.bytter.biz.bdlink.entity.BtBankAccDtl;
import com.bytter.biz.bdlink.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.DataTableHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.page.IPagePara;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.persistence.PropertyFilter;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.constant.BisAccBalConstant;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.constant.BtCurrencyConstant;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 银行账号明细表
 * @date 2017-07-05 11:20:30
 */
@Repository
public class BisAccDtlDao extends BaseDao<BisAccDtl, String> {

    /**
     * 根据id查找银行账户流水信息
     * zuoy 2017-07-03
     *
     * @param gedTransTime
     * @param ledTransTime
     * @return BtBankAccDtlVO
     */
    public List<BisAccDtlVO> getByTransTime(String gedTransTime,String ledTransTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.acc_no,acc.corp_id,t.bank_acc,t.acc_name,t.bank_name,t.bif_code, bis.name bifName,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,t.voucher_no,t.bank_serial_no,t.return_time, t.claim_id,t.bill_id,");
        sql.append(" t.import_sign,t.import_date,t.create_by,t.create_date,t.update_by,t.update_date,t.fbs_status,t.status,t.project_remark,t.REFUND_BLEND_TYPE,t.tally_Date from bis_acc_dtl t ");
        sql.append(" left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join bis_bif_init bis on bis.id = t.bif_code  ");
        sql.append(" where 1=1 ");
        sql.append(" AND t.trans_time >= BT_TO_DATE( :gedTransTime, 'yyyy-MM-dd HH24:MI:SS') ");
        sql.append(" AND t.trans_time <= BT_TO_DATE( :ledTransTime, 'yyyy-MM-dd HH24:MI:SS') ");
        sql.append(" AND (t.receipt_blend_sign = '0' or t.receipt_blend_sign is null) ");
        Map<String, String> params = new HashMap<>();
        params.put("gedTransTime", gedTransTime+" 00:00:00");
        params.put("ledTransTime", ledTransTime+" 23:59:59");
        return listBySQLAliasToBean(sql.toString(), params, BisAccDtlVO.class);
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id as id,t.acc_no as accNo,acc.corp_id as corpId,corp.name as corpName,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,cur.cur_name as curName,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime,t.bank_serial_no as bankSerialNo, ");
        sql.append(" acc.attribute_Id as attributeId, att.attribute_name as attributeName, t.project_remark as projectRemark, ");
        sql.append(" acc.nature_id as natureId, n.nature_name  as natureName, acc.acc_type as accType ");
        sql.append(" from bis_acc_dtl t  force index(idx_bis_acc_dtl_transtime_accname_oppaccname)  left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  ");
//        sql.append(" from bis_acc_dtl t left join (select * from bt_bank_acc acc where 1=1 " + joinSql + ") acc on t.bank_acc = acc.bank_acc  ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature n on acc.nature_id = n.id");
        sql.append(" left join sys_corp corp on acc.corp_id = corp.id ");
        sql.append(" left join bt_currency cur on t.cur_id = cur.id ");
        sql.append(" where 1 = 1 and (t.status <> -2 or t.status is null) ");
		/*sql.append(" select t.id as id,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
		sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
		sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
		sql.append(" acc.attribute_Id as attributeId, att.attribute_name as attributeName, t.project_remark as projectRemark,t.bank_serial_no as bank_serial_no, ");
		sql.append(" acc.nature_id as natureId, n.nature_name  as natureName, acc.acc_type as accType ");
		sql.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  ");
		sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
		sql.append(" left join bt_acc_nature n on acc.nature_id = n.id");
		sql.append(" where  1=1 and (t.status <> -2 or t.status is null) ");*/
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        //交易时间
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlVO.class, "t", false);
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BisAccDetailVO> getDataTablesUnionV2(DataTablesInput dataTablesInput, String joinSql) {
        StopWatch stopWatch = new StopWatch("银行流水查询效率");
        stopWatch.start("银行流水查询");
        //交易时间
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        if (StringUtils.isNotEmpty(gedTransTime) && !gedTransTime.contains("00:00:00")) {
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime) && !ledTransTime.contains("23:59:59")) {
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
        }

        StringBuilder sqlForOtherFileds = new StringBuilder();
        sqlForOtherFileds.append(" select a.* from ( ");
        sqlForOtherFileds.append(" select t.id as id,t.id as dtlId,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
        sqlForOtherFileds.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sqlForOtherFileds.append(" case t.CD_SIGN when '0' then t.amt when '1' then 0.00 end as amt_in, case t.CD_SIGN when '1' then t.amt when '0' then 0.00 end as amt_out, ");
        sqlForOtherFileds.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
        sqlForOtherFileds.append(" acc.attribute_Id as attributeId, att.attribute_name as attributeName, t.project_remark as projectRemark,t.bank_serial_no as bank_serial_no, ");
        sqlForOtherFileds.append(" acc.nature_id as natureId, n.nature_name  as natureName, acc.acc_type as accType,acc.fc_acc_group_code as fcAccGroupCode, ");
        sqlForOtherFileds.append(" t.gx_dtl_sync_status syncStatus,t.bus_type as busType,acc.is_online as isOnline ");
        sqlForOtherFileds.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sqlForOtherFileds.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sqlForOtherFileds.append(" left join bt_acc_nature n on acc.nature_id = n.id");
        sqlForOtherFileds.append(" where  1 = 1 and (t.status <> -2 or t.status is null) ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sqlForOtherFileds.append(joinSql);
        }

        if (dataTablesInput.getFormdata().containsKey("ins_bifCode")) {
            String bifCodeStr = dataTablesInput.getFormdata().get("ins_bifCode").toString();
            if (CharSequenceUtil.isNotBlank(bifCodeStr)) {
                List<String> bifCodeList = Arrays.stream(bifCodeStr.split(",")).collect(Collectors.toList());
                sqlForOtherFileds.append(" and ").append(SqlUtil.getInSql(bifCodeList, "acc.bif_code", true));
            }
        }

        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("accStatus"))) {
            String accStatus = dataTablesInput.getFormdata().get("accStatus").toString();
            if (StringUtils.isNotBlank(accStatus)) {
                if (accStatus.equals(BtBankAccConstant.ACCSTATUS_1)) {
                    sqlForOtherFileds.append(" and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
                } else if (accStatus.equals(BtBankAccConstant.ACCSTATUS_2)) {
                    sqlForOtherFileds.append(" and acc.status = " + BtBankAccConstant.STATUE_DEL + " ");
                }
            }
        }
        String syncStatus = (String) dataTablesInput.getFormdata().get("eqs_syncStatus");
        if (!EmptyUtils.isEmpty(syncStatus)){
            if ("1".equals(syncStatus)){
                sqlForOtherFileds.append(" and t.gx_dtl_sync_status = 'S' ");
            }else{
                sqlForOtherFileds.append(" and ( t.gx_dtl_sync_status is null or t.gx_dtl_sync_status = 'F' ) ");
            }
            dataTablesInput.getFormdata().remove("eqs_syncStatus");
        }
        // if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("dtlId"))) {
        //     String id = dataTablesInput.getFormdata().get("dtlId").toString();
        //     sqlForOtherFileds.append(" and acc.id = " + id + " ");
        //     params.put("id", id);
        // }
        sqlForOtherFileds.append(" ) a where 1=1 ");
        logger.error("银行账号流水SQL========== " + sqlForOtherFileds.toString());
        DataTablesOutput<BisAccDetailVO> outputForBisAccDtls = getDynamicBySQL(dataTablesInput, sqlForOtherFileds.toString(), BisAccDetailVO.class, "a", false);
        stopWatch.stop();
        logger.error("stopWatch == " + stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        return outputForBisAccDtls;
    }
    /**
     * <br>
     * <b>功能：</b> 根据权限和查询条件顾虑满足条件的银行账户 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/7/25 0025 <br>
     */
    public List<String> getAcclistByCondition(DataTablesInput dataTablesInput, List<String> corpList, List<String> accList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.bank_acc from bt_bank_acc acc ");
        sql.append(" where 1 = 1 ");
        //根据查询条件及账户数据进行一次过滤
        String corpId = (String) dataTablesInput.getFormdata().get("ins_corpId");
        if (StringUtils.isNotBlank(corpId)) {
            String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
            corpList = Arrays.asList(corpIds);
        }
        if (EmptyUtils.isNotEmpty(corpList)) {
            if (corpList.size() >= 1000) {
                String corpAuthSql = SqlUtil.getInSql(corpList, "acc.corp_id", true);
                sql.append(" and ").append(corpAuthSql);
            } else {
                sql.append(" and acc.corp_id in (:corpList) ");
                params.put("corpList", corpList);
            }
        }
        if (EmptyUtils.isNotEmpty(accList)) {
            if (accList.size() >= 1000) {
                String accSql = SqlUtil.getInSql(accList, "acc.bank_acc", true);
                sql.append(" and ").append(accSql);
            } else {
                sql.append(" and acc.bank_acc in (:accLists) ");
                params.put("accLists", accList);
            }
        }
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("ins_attributeId"))) {//账户属性
            String attributeId = dataTablesInput.getFormdata().get("ins_attributeId").toString();
            if (StringUtils.isNotBlank(attributeId)) {
                sql.append(" and acc.attribute_Id in (:attributeId) ");
                params.put("attributeId", attributeId.split(CommonConstant.COMMA_SPLIT));
            }
        }
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("ins_natureId"))) {//账户性质
            String natureId = dataTablesInput.getFormdata().get("ins_natureId").toString();
            if (StringUtils.isNotBlank(natureId)) {
                sql.append(" and acc.nature_Id in (:natureId) ");
                params.put("natureId", natureId.split(CommonConstant.COMMA_SPLIT));
            }
        }
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("ins_accType"))) {//账户属性
            String accType = dataTablesInput.getFormdata().get("ins_accType").toString();
            if (StringUtils.isNotBlank(accType)) {
                sql.append(" and acc.acc_Type in (:accType) ");
                params.put("accType", accType.split(CommonConstant.COMMA_SPLIT));
            }
        }
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("accStatus"))) {//账户状态
            String accStatus = dataTablesInput.getFormdata().get("accStatus").toString();
            if (StringUtils.isNotBlank(accStatus)) {
                if (accStatus.equals(BtBankAccConstant.ACCSTATUS_1)) {
                    sql.append(" and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
                } else if (accStatus.equals(BtBankAccConstant.ACCSTATUS_2)) {
                    sql.append(" and acc.status = " + BtBankAccConstant.STATUE_DEL + " ");
                }
            }
        }
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("ins_bifCode"))) {//接口代码
            String bifCode = dataTablesInput.getFormdata().get("ins_bifCode").toString();
            if (StringUtils.isNotBlank(bifCode)) {
                sql.append(" and acc.bif_code in (:bifCode) ");
                params.put("bifCode", bifCode.split(CommonConstant.COMMA_SPLIT));
            }
        }
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("likes_bankAcc"))) {//账号
            String bankAcc = dataTablesInput.getFormdata().get("likes_bankAcc").toString();
            if (StringUtils.isNotBlank(bankAcc)) {
                sql.append(" and acc.bank_Acc = :bankAcc ");
                params.put("bankAcc", bankAcc);
            }
        }
        logger.info("getAcclistByCondition SQL= {}", sql.toString());
        return this.createSQLQuery(sql.toString(), params).list();
    }
    /**
     * 根据id查找银行账户流水信息
     * zuoy 2017-07-03
     *
     * @param id
     * @return BtBankAccDtlVO
     */
    public BisAccDtlVO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.acc_no,acc.corp_id,c.name as corpName,t.bank_acc,t.acc_name,t.bank_name,t.bif_code, bis.name bifName,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" t.amt,t.bal,t.cur_id,cur.cur_name as curName,t.uses,t.postscript,t.remark,t.trans_time,t.voucher_no,t.bank_serial_no,t.return_time, t.claim_id,t.bill_id,");
        sql.append(" t.import_sign,t.import_date,t.create_by,t.create_date,t.update_by,t.update_date,t.fbs_status,t.status,t.project_remark,t.REFUND_BLEND_TYPE,t.tally_Date ");
        sql.append(" from bis_acc_dtl t ");
        sql.append(" left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join bis_bif_init bis on bis.id = t.bif_code  ");
        sql.append(" left join bt_currency cur ON t.cur_id = cur.id   ");
        sql.append(" left join sys_corp c on c.id = acc.corp_id");
        sql.append(" where t.id = :id ");
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, BisAccDtlVO.class);
    }
    /**
     * 根据账号币别获取流水
     *
     * @param bankAcc
     * @param curId
     * @return
     */
    public List<BisAccDtlVO> getListByBankAcc(String bankAcc, String curId, String remark) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.acc_no,acc.corp_id,t.bank_acc,t.acc_name,t.bank_name,t.bif_code, bis.name bifName,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,t.voucher_no,t.bank_serial_no,t.return_time, ");
        sql.append(" t.import_sign,t.import_date,t.create_by,t.create_date,t.update_by,t.update_date,t.fbs_status, t.project_remark,t.REFUND_BLEND_TYPE from bis_acc_dtl t ");
        sql.append(" left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join bis_bif_init bis on bis.id = t.bif_code  ");
        sql.append(" where t.CUR_ID = '").append(curId).append("'");
        sql.append(" and t.BANK_ACC = '").append(bankAcc).append("'");
        sql.append(" and (t.status <> -2 or t.status is null) ");
        if (StringUtils.isNotBlank(remark)) {
            sql.append(" and t.remark like '%").append(remark).append("%'");
        }
        sql.append(" order by t.trans_time desc");
        return listBySQLAliasToBean(sql.toString(), null, BisAccDtlVO.class);
    }

    /**
     * 根据账号币别获取流水
     * @param bankAcc
     * @param curId
     * @return
     */
    public List<BisAccDtlVO> getListByBankAcc(String bankAcc,String curId,String remark, String beginDate, String endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.acc_no,acc.corp_id,t.bank_acc,t.acc_name,t.bank_name,t.bif_code, bis.name bifName,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,t.voucher_no,t.bank_serial_no,t.return_time, ");
        sql.append(" t.import_sign,t.import_date,t.create_by,t.create_date,t.update_by,t.update_date,t.fbs_status, t.project_remark,t.refund_blend_type from bis_acc_dtl t ");
        sql.append(" left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join bis_bif_init bis on bis.id = t.bif_code  ");
        sql.append(" where t.CUR_ID = '").append(curId).append("'");
        sql.append(" and t.BANK_ACC = '").append(bankAcc).append("'");
        sql.append(" and t.status = 95 ");
        if(StringUtils.isNotBlank(remark)){
            sql.append(" and t.remark like '%").append(remark).append("%'");
        }
        if (StringUtils.isNotBlank(beginDate)) {
            sql.append(" and bt_to_char(t.trans_time,'yyyy-MM-dd') >= '" + beginDate + "'");
        }
        if (StringUtils.isNotBlank(endDate)) {
            sql.append(" and bt_to_char(t.trans_time,'yyyy-MM-dd') <= '" + endDate + "'");
        }
        sql.append(" order by t.trans_time desc");
        return listBySQLAliasToBean(sql.toString(),null,BisAccDtlVO.class);
    }

    /**
     * 根据账号币别获取流水 - 分页
     *
     * @param pagePara
     * @param bankAcc
     * @param curId
     * @return
     */
    public IPageResult<BisAccDtlVO> getPageListByBankAcc(IPagePara pagePara,
                                                         String bankAcc,
                                                         String curId,
                                                         String remark) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.acc_no,acc.corp_id,t.bank_acc,t.acc_name,t.bank_name,t.bif_code, bis.name bifName,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,t.voucher_no,t.bank_serial_no,t.return_time, ");
        sql.append(" t.import_sign,t.import_date,t.create_by,t.create_date,t.update_by,t.update_date,t.fbs_status, t.project_remark,t.REFUND_BLEND_TYPE from bis_acc_dtl t ");
        sql.append(" left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join bis_bif_init bis on bis.id = t.bif_code  ");
        sql.append(" where t.CUR_ID = '").append(curId).append("'");
        sql.append(" and t.BANK_ACC = '").append(bankAcc).append("'");
        sql.append(" and (t.status <> -2 or t.status is null) ");
        if (StringUtils.isNotBlank(remark)) {
            sql.append(" and t.remark like '%").append(remark).append("%'");
        }
        sql.append(" order by t.trans_time desc");
        return this.getPageResultBySQL(pagePara, sql.toString(), null, BisAccDtlVO.class);
    }

    /**
     * 根据账号币别获取流水 - 分页
     *
     * @param pagePara
     * @param bankAcc
     * @param curId
     * @return
     */
    public IPageResult<BisAccDtlVO> getPageListByBankAcc(IPagePara pagePara,
                                                         String bankAcc,
                                                         String curId,
                                                         String remark,
                                                         String beginDate,
                                                         String endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.acc_no,acc.corp_id,t.bank_acc,t.acc_name,t.bank_name,t.bif_code, bis.name bifName,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,t.voucher_no,t.bank_serial_no,t.return_time, ");
//        sql.append(" t.import_sign,t.import_date,t.create_by,t.create_date,t.update_by,t.update_date,t.fbs_status, t.project_remark,t.REFUND_BLEND_TYPE,t.tally_Date from bis_acc_dtl t ");
        sql.append(" t.import_sign,t.import_date,t.create_by,t.create_date,t.update_by,t.update_date,t.fbs_status, t.project_remark,t.refund_blend_type from bis_acc_dtl t ");
        sql.append(" left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join bis_bif_init bis on bis.id = t.bif_code  ");
        sql.append(" where t.CUR_ID = '").append(curId).append("'");
        sql.append(" and t.BANK_ACC = '").append(bankAcc).append("'");
        sql.append(" and t.status = 95 "); //不清楚app为什么要限制 95，现和测试沟通，建议放开
        if (StringUtils.isNotBlank(remark)) {
            sql.append(" and t.remark like '%").append(remark).append("%'");
        }
        if (StringUtils.isNotBlank(beginDate)) {
            sql.append(" and bt_to_char(t.trans_time,'yyyy-MM-dd') >= '" + beginDate + "'");
        }
        if (StringUtils.isNotBlank(endDate)) {
            sql.append(" and bt_to_char(t.trans_time,'yyyy-MM-dd') <= '" + endDate + "'");
        }
        sql.append(" order by t.trans_time desc");
        return this.getPageResultBySQL(pagePara, sql.toString(), null, BisAccDtlVO.class);
    }


    public BisAccDtlVO getListByBankAccAmt(String bankAcc,String curId,String remark,String cdSign) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(t.amt) amt from bis_acc_dtl t ");
        sql.append(" left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join bis_bif_init bis on bis.id = t.bif_code  ");
        sql.append(" where t.CUR_ID = '").append(curId).append("'");
        sql.append(" and t.BANK_ACC = '").append(bankAcc).append("'");
        sql.append(" and t.status = 95 ");
        sql.append(" AND t.CD_SIGN = ").append(cdSign).append(" ");
        if(StringUtils.isNotBlank(remark)){
            sql.append(" and t.remark like '%").append(remark).append("%'");
        }
        sql.append(" GROUP BY t.cd_sign ");
        return getBySQLAliasToBean(sql.toString(),null,BisAccDtlVO.class);
    }

    public BisAccDtlVO getListWithTransTime(String bankAcc, String curId, String remark, String cdSign, String beginDate, String endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(t.amt) amt from bis_acc_dtl t ");
        sql.append(" left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join bis_bif_init bis on bis.id = t.bif_code  ");
        sql.append(" where t.CUR_ID = '").append(curId).append("'");
        sql.append(" and t.BANK_ACC = '").append(bankAcc).append("'");
        sql.append(" and (t.status = 95 or t.status is null) ");
        sql.append(" AND t.CD_SIGN = ").append(cdSign).append(" ");
        if(StringUtils.isNotBlank(remark)){
            sql.append(" and t.remark like '%").append(remark).append("%'");
        }
        if (StringUtils.isNotBlank(beginDate)) {
            sql.append(" and bt_to_char(t.trans_time,'yyyy-MM-dd') >= '" + beginDate + "'");
        }
        if (StringUtils.isNotBlank(endDate)) {
            sql.append(" and bt_to_char(t.trans_time,'yyyy-MM-dd') <= '" + endDate + "'");
        }
        sql.append(" GROUP BY t.cd_sign ");
        return getBySQLAliasToBean(sql.toString(),null,BisAccDtlVO.class);
    }


    /**
     * 根据时间范围查询流水记录金额
     *
     * @param bankAcc        账户
     * @param beginTransTime 开始时间
     * @param endTransTime   结束时间
     * @param cdSign         借贷标志 0-收入 1-支出
     * @param rbSign         红蓝标志 1-红 2-蓝
     * @return
     */
    public BigDecimal getSumAmtBy(String bankAcc, String curId, String beginTransTime, String endTransTime, String cdSign, String rbSign) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select sum(amt) as AMT from bis_acc_dtl ");
        sql.append(" where bank_acc = :bankAcc and cur_id = :curId and trans_time >= bt_to_date(:beginTransTime, 'yyyy-mm-dd') and trans_time < bt_to_date(:endTransTime, 'yyyy-mm-dd') and cd_sign = :cdSign");
        sql.append(" and (status <> -2 or status is null) ");
        // 红蓝标志，考虑到银行返回的流水红蓝标志为空，根据流水计算余额时取不到导致余额计算有误
        if (BisAccBalConstant.RBSIGN_BLUE.equals(rbSign)) {
            sql.append(" and (rb_sign = :rbSign or rb_sign is null or rb_sign = '')");
        } else {
            sql.append(" and rb_sign = :rbSign");
        }
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        params.put("beginTransTime", beginTransTime);
        params.put("endTransTime", endTransTime);
        params.put("cdSign", cdSign);
        params.put("rbSign", rbSign);
        Map<String, BigDecimal> amp = this.getBySQL(sql.toString(), params);
        if (EmptyUtils.isEmpty(amp)) {
            return BigDecimal.ZERO;
        }
        return EmptyUtils.isEmpty(amp.get("AMT")) ? BigDecimal.ZERO : new BigDecimal(String.valueOf(amp.get("AMT")));
    }
    /**
     * 查询银行流水可补录数据
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesMakeup(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.acc_no,t.bank_acc,t.acc_name,t.bank_name,t.bif_code,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time, t.project_remark,t.bank_serial_no ");
        sql.append(" from bis_acc_dtl t where 1 = 1 and (t.status <> -2 or t.status is null) ");
        sql.append(" and (t.opp_acc_no is null or t.opp_acc_name is null or t.opp_acc_bank is null or t.opp_acc_no= '' or t.opp_acc_name= '' or t.opp_acc_bank= '') ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        //交易时间
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlVO.class, "t", false);
    }
    /**
     * 查询当前交易时间之后的流水数据
     *
     * @param bankAcc
     * @param curId
     * @param transTime
     * @param id
     * @return
     */
    public List<BisAccDtl> listAfterBtBankAccDtl(String bankAcc, String curId, Date transTime, String id, Integer orderNo) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<String, Object>();
        sql.append(" select t.id,t.acc_no,t.bank_acc,t.acc_name,t.bank_name,t.bif_code,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,t.voucher_no,t.bank_serial_no,t.return_time, ");
        sql.append(" t.import_sign,t.import_date,t.create_by,t.create_date,t.update_by,t.update_date,t.fbs_status from bis_acc_dtl t ");
        sql.append(" join bt_bank_acc_dtl dtl on t.acc_no = dtl.acc_no and t.bank_acc = dtl.bank_acc and t.cur_id = dtl.cur_id and t.trans_time = dtl.trans_time ");
        sql.append(" where t.bank_acc = :bankAcc and t.cur_id = :curId and (t.status <> -2 or t.status is null) ");
        sql.append(" and ((t.trans_time = :transTime and dtl.order_no > :orderNo ) or t.trans_time > :transTime) ");
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        params.put("transTime", transTime);
        params.put("orderNo", orderNo);
        if (!EmptyUtils.isBlank(id)) {
            sql.append(" and t.id <> :id");
            params.put("id", id);
        }
        sql.append(" order by t.trans_time,dtl.order_no ");
        return this.listBySQLAliasToBean(sql.toString(), params, BisAccDtl.class);
    }
    /**
     * 根据账号和币别id查找账号明细最小时间
     *
     * @param bankAcc
     * @param curId
     * @return
     */
    public Date getMinTransTime(String bankAcc, String curId) {
        Map<String, Object> params = new HashMap<>();
        String sql = "select min(trans_time) as transTime from bis_acc_dtl " +
                " where bank_acc = :bankAcc and cur_id = :curId and (status <> -2 or status is null) ";
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        List<Map<String, Date>> dateTemp = this.listBySQL(sql, 0, 1, params);
        if (EmptyUtils.isEmptyList(dateTemp) || EmptyUtils.isEmpty(dateTemp.get(0).get("TRANSTIME"))) {
            return null;
        }
        return dateTemp.get(0).get("TRANSTIME");
    }
    /**
     * 根据账号币别交易日期查找明细数据
     *
     * @param bankAcc
     * @param curId
     * @param transTime
     * @return
     */
    public BisAccDtlVO getBisAccDtlVOByAccCurTime(String bankAcc, String curId, Date transTime) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        String sql = " select t.id as id,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, " +
                " t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, " +
                " t.amt as amt,t.bal as bal,t.cur_id as curId,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, " +
                " acc.attribute_Id as attributeId, att.attribute_name as attributeName, t.project_remark as projectRemark " +
                " from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  " +
                " left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id " +
                " where 1 = 1 and t.bank_acc =:bankAcc and t.cur_id = :curId and (t.status <> -2 or t.status is null) " +
                " and t.trans_time = (select max(d.trans_time) from bis_acc_dtl d where d.bank_acc =:bankAcc and d.cur_id =:curId" +
                " and bt_to_char(d.trans_time,'yyyy-MM-dd') <= :transTime )" +
                " order by t.trans_time,t.acc_no,t.bank_serial_no desc";
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        params.put("transTime", DateUtils.formatDate(transTime));
        List<BisAccDtlVO> list = this.listBySQLAliasToBean(sql, 0, 1, params, BisAccDtlVO.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }
    public List<BisAccDtlExportVO> listExport(DataTablesInput dataTablesInput, String joinSql) {
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        if (StringUtils.isNotEmpty(gedTransTime) && !gedTransTime.contains(" 00:00:00")) {
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime) && !ledTransTime.contains(" 23:59:59")) {
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
        }

        StringBuilder sqlForOtherFileds = new StringBuilder();
        sqlForOtherFileds.append(" select t.id as id,t.id as dtlId,t.acc_no as accNo,acc.corp_id as corpId,corp.name as corpName,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
        sqlForOtherFileds.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sqlForOtherFileds.append(" case t.CD_SIGN when '0' then t.amt when '1' then 0.00 end as amtIn, case t.CD_SIGN when '1' then t.amt when '0' then 0.00 end as amtOut, ");
        sqlForOtherFileds.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,cur.cur_name as curName,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime,t.bank_serial_no as bankSerialNo, ");
        sqlForOtherFileds.append(" acc.attribute_Id as attributeId, att.attribute_name as attributeName, t.project_remark as projectRemark, ");
        sqlForOtherFileds.append(" acc.nature_id as natureId, n.nature_name  as natureName, acc.acc_type as accType,acc.fc_acc_group_code as fcAccGroupCode, ");
        sqlForOtherFileds.append(" f.sync_status syncStatus,acc.is_online as isOnline ");
        sqlForOtherFileds.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  ");
        sqlForOtherFileds.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sqlForOtherFileds.append(" left join bt_acc_nature n on acc.nature_id = n.id");
        sqlForOtherFileds.append(" left join sys_corp corp on acc.corp_id = corp.id ");
        sqlForOtherFileds.append(" left join bt_currency cur on t.cur_id = cur.id ");
        sqlForOtherFileds.append(" left join dsl_inteface_flag f on t.id=f.source_id ");
        sqlForOtherFileds.append(" where  1 = 1 and (t.status <> -2 or t.status is null) ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sqlForOtherFileds.append(joinSql);
        }

        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("accStatus"))) {
            String accStatus = dataTablesInput.getFormdata().get("accStatus").toString();
            if (StringUtils.isNotBlank(accStatus)) {
                if (accStatus.equals(BtBankAccConstant.ACCSTATUS_1)) {
                    sqlForOtherFileds.append(" and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
                } else if (accStatus.equals(BtBankAccConstant.ACCSTATUS_2)) {
                    sqlForOtherFileds.append(" and acc.status = " + BtBankAccConstant.STATUE_DEL + " ");
                }
            }
        }
        return exportBySQL(dataTablesInput, sqlForOtherFileds.toString(), BisAccDtlExportVO.class, "t", false);
    }
    /**
     * 处理 自动拼接条件和排序
     * <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param input    dataTable请求对象
     * @param sql      SQL
     * @param voClazz  转换为VO
     * @param alias    别名
     * @param hasWhere 是否存在where
     * @return dataTable响应对象
     */
    protected List exportBySQL(DataTablesInput input, String sql, Class<?> voClazz, String alias, Boolean hasWhere) {
        Object bean = null;
        try {
            if (null != voClazz) {
                bean = voClazz.newInstance();
            }
            //组合附带传值问题
            List<Object> tempList = new ArrayList<Object>();
            Object[] needList = null;
            Map<String, Object> paramMap = new HashMap<String, Object>();
            if (tempList.size() > 0) {
                needList = tempList.toArray(new Object[tempList.size()]);
            }
            String allCondition = getWhereAllCondition(input, bean, alias, hasWhere, needList);
            Map<String, Object> values = getSearchValues(input, needList);
            //赋值
            if (values == null) {
                values = new HashMap<>();
            }
            values.putAll(paramMap);
            //修改: zuoy 优化查询方法
            Integer fetchSize = 100000;
            List list = listBySQL(sql + allCondition, input.getStart(), input.getLength(), values, fetchSize);
            if (null != voClazz) {
                list = BeanUtils.copyMapListProperties(list, voClazz);
            }
            return list;
        } catch (Exception e) {
            logger.error(e.toString());
            throw new ParameterException(e);
        }
    }
    /**
     * map to bean
     *
     * @return 搜索到的结果集
     */
    public Map<String, Object> getSearchValues(DataTablesInput input, Object... needParams) {
        Map<String, Object> beanMap = Maps.newHashMap();
        //表单搜索元素
        List<PropertyFilter> propertyFilters = DataTableHelper.searchWithIn(input);
        if (!propertyFilters.isEmpty()) {
            for (PropertyFilter filter : propertyFilters) {
                if (null != needParams && needParams.length > 0) {
                    List<Object> objects = Arrays.asList(needParams);
                    if (!objects.contains(filter.getPropertyName())) {
                        continue;
                    }
                }
                Object value = filter.getMatchValue();
                MatchTypeEnum matchTypeEnum = filter.getMatchType();
                if (MatchTypeEnum.LIKE.getValue().equalsIgnoreCase(filter.getMatchType().getValue())) {
                    //zhoujian 2018年6月14日15:52:08 B20180600221新增支持中间带空格模糊搜索
                    String val = String.valueOf(filter.getMatchValue());
                    if (StringUtils.isNotBlank(val)) {
                        val = val.trim().replaceAll("\\s+", "%");
                    }
                    value = "%" + val + "%";
                    //value = "%" + String.valueOf(filter.getMatchValue()) + "%";
                } else if (MatchTypeEnum.LLIKE.getValue().equalsIgnoreCase(filter.getMatchType().getValue())) {
                    matchTypeEnum = MatchTypeEnum.LIKE;
                    value = "%" + filter.getMatchValue();
                } else if (MatchTypeEnum.RLIKE.getValue().equalsIgnoreCase(filter.getMatchType().getValue())) {
                    matchTypeEnum = MatchTypeEnum.LIKE;
                    value = filter.getMatchValue() + "%";
                }
                if (!filter.hasMultiProperties()) { //只有一个属性需要比较的情况.
                    beanMap.put(matchTypeEnum.name().toLowerCase() + filter.getPropertyName(), value);
                } else {//包含多个属性需要比较的情况,进行or处理.
                    for (String propertyName : filter.getPropertyNames()) {
                        beanMap.put("or" + matchTypeEnum.name().toLowerCase() + propertyName, value);
                    }
                }
            }
        }
        return beanMap;
    }
    public List<BisAccDtl> getListForFlush() {
        String sql = "  select acc.bank_acc AS bankAcc, cur.cur_id AS curId " +
                " from bt_bank_acc acc " +
                " LEFT JOIN bt_bank_acc_cur cur ON acc.id = cur.bank_acc_id " +
                " WHERE acc.IS_ONLINE = '0'";
        return this.listBySQLAliasToBean(sql, null, BisAccDtl.class);
    }
    public List<BisAccDtl> listAfterBtBankAccDtl(String bankAcc, String curId) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<String, Object>();
        sql.append(" select t.id,t.acc_no,t.bank_acc,t.acc_name,t.bank_name,t.bif_code,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,t.voucher_no,t.bank_serial_no,t.return_time, ");
        sql.append(" t.import_sign,t.import_date,t.create_by,t.create_date,t.update_by,t.update_date,t.fbs_status from bis_acc_dtl t ");
        //无需关联离线流水申请表，考虑账号存在在线离线切换
        //sql.append(" join bt_bank_acc_dtl dtl on t.acc_no = dtl.acc_no and t.bank_acc = dtl.bank_acc and t.cur_id = dtl.cur_id and t.trans_time = dtl.trans_time ");
        sql.append(" where t.bank_acc = :bankAcc and t.cur_id = :curId  and (t.status <> -2 or t.status is null) ");
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        sql.append(" order by t.trans_time");
        return this.listBySQLAliasToBean(sql.toString(), params, BisAccDtl.class);
    }
    /**
     * 获取一个账号期间发生额
     *
     * @param curId     币种
     * @param bankAcc   账号
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 期间发生额
     */
    public BigDecimal getDuringMoney(String curId, String bankAcc, Date beginDate, Date endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("curId", curId);
        params.put("bankAcc", bankAcc);
        params.put("beginDate", DateUtils.addDay(beginDate, 1));
        params.put("endDate", DateUtils.addDay(endDate, 1));
        String sql = " select sum(case when t.CD_SIGN = '1' then t.AMT * -1 else t.AMT end) result " +
                " from bis_acc_dtl t " +
                " where t.CUR_ID = :curId and (t.status <> -2 or t.status is null) " +
                "   and BANK_ACC = :bankAcc " +
                "   and t.TRANS_TIME >= :beginDate " +
                "   and t.TRANS_TIME < :endDate ";
        Object result = createSQLQuery(sql, params).uniqueResult();
        return null == result ? BigDecimal.ZERO : BigDecimal.valueOf(Double.valueOf(result.toString()));
    }
    /**
     * 根据id更新流水余额
     *
     * @param id
     * @param bal
     * @return
     */
    public int updateBalById(String id, BigDecimal bal) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("bal", bal);
        return this.batchSQLExecute(" update bis_acc_dtl set bal = :bal where id = :id ", params);
    }
    /**
     * 电子回单勾兑查询银行流水
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<BisAccDtlVO> getDataTablesUnionForReceiptBlending(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        String eqs_id = (String) dataTablesInput.getFormdata().get("eqs_id");
        dataTablesInput.getFormdata().remove("eqs_id");
        sql.append(" select t.id as id,t.acc_no as accNo,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
        sql.append(" t.project_remark as projectRemark ");
        sql.append(" from bis_acc_dtl t ");
        sql.append(" where 1 = 1 and (t.status <> -2 or t.status is null) ");
        if (EmptyUtils.isNotEmpty(eqs_id)) {
            if ("#".equals(eqs_id)) {
                sql.append(" and t.id =:eqsId ");
                params.put("eqsId", eqs_id);
            } else {
                sql.append(" and  " + SqlUtil.getInSql(eqs_id.split(","), "t.id", true));
            }
        } else {
            //查询未勾兑流水记录
            sql.append(" and t.id not in ( select DTL_ID from BIS_RECEIPT_DTL )");
        }
        //交易时间
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            if (gedTransTime.length() > 10) {
                gedTransTime = gedTransTime.substring(0, 10);
            }
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            if (ledTransTime.length() > 10) {
                ledTransTime = ledTransTime.substring(0, 10);
            }
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
        }
        //考虑流水账号为空的情况
        String bankAcc = (String) dataTablesInput.getFormdata().get("eqs_bankAcc");
        if ("null".equals(bankAcc)) {
            sql.append(" and (t.bank_acc is null or t.bank_acc='')");
            dataTablesInput.getFormdata().put("eqs_bankAcc", "");
        }
        String oppAccNo = (String) dataTablesInput.getFormdata().get("eqs_oppAccNo");
        if ("null".equals(oppAccNo)) {
            sql.append(" and (t.opp_acc_no is null or t.opp_acc_no='')");
            dataTablesInput.getFormdata().put("eqs_oppAccNo", "");
        }
        DataTablesOutput output = getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlVO.class, "t", false, params);
        List<BisAccDtlVO> bisAccDtlVOS = output.getData();
        List<BisAccDtlDTO> listDto;
        listDto = BeanUtils.copyListProperties(bisAccDtlVOS, BisAccDtlDTO.class);
        output.setData(listDto);
        return output;
    }
    /**
     * 被关联的流水的勾兑规则
     *
     * @param id
     * @return
     */
    public List<BisAccDtlRulesVO> getRules(String id) {
        String sql = "select dtl_id as dtlId,receipt_id as receiptId,rules_name as rulesName from BIS_RECEIPT_DTL where dtl_id=:id";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return this.listBySQLAliasToBean(sql, params, BisAccDtlRulesVO.class);
    }
    /**
     * 获取指定账号某天的流水集合
     *
     * @param bankAcc
     * @param curId
     * @param transTime
     * @return
     */
    public List<BisAccDtlVO> listBisAccDtlVOByAccCurTime(String bankAcc, String curId, Date transTime) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        String sql = " select t.id as id,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, " +
                " t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, " +
                " t.amt as amt,t.bal as bal,t.cur_id as curId,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, " +
                " acc.attribute_Id as attributeId, att.attribute_name as attributeName, t.project_remark as projectRemark " +
                " from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  " +
                " left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id " +
                " where 1 = 1 and t.bank_acc =:bankAcc and t.cur_id = :curId and (t.status <> -2 or t.status is null)" +
                //sql.append(" and t.trans_time = (select max(d.trans_time) from bis_acc_dtl d where d.bank_acc =:bankAcc and d.cur_id =:curId");
                " and bt_to_char(t.trans_time,'yyyy-MM-dd') = :transTime" +
                " order by t.trans_time,t.acc_no,t.bank_serial_no desc";
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        params.put("transTime", DateUtils.formatDate(transTime));
        return this.listBySQLAliasToBean(sql, params, BisAccDtlVO.class);
    }
    /**
     * 根据票据认领中间表id数组修改
     *
     * @param claimIdArr 票据认领id数组
     */
    public void updateByClaimArr(String[] claimIdArr) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("claimIdArr", claimIdArr);
        this.batchSQLExecute(" update bis_acc_dtl set claim_id = null where claim_id in (:claimIdArr) ", params);
    }
    /**
     * 根据id数组修改票据认领中间表id
     *
     * @param ids     流水明细id
     * @param claimId 票据认领中间表id
     */
    public void updateClaimIdByIds(String[] ids, String claimId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("ids", ids);
        params.put("claimId", claimId);
        this.batchSQLExecute(" update bis_acc_dtl set claim_id = :claimId where id in (:ids) ", params);
    }
    /**
     * 根据流水余额
     *
     * @param dtl 流水
     */
    public void updateBal(BtBankAccDtl dtl) {
        Map<String, Object> params = new HashMap<>(4);
        params.put("bal", dtl.getBal());
        params.put("accNo", dtl.getAccNo());
        params.put("bankAcc", dtl.getBankAcc());
        params.put("curId", dtl.getCurId());
        this.batchSQLExecute(" update bis_acc_dtl set bal = :bal where acc_no=:accNo and bank_acc=:bankAcc and cur_id=:curId ", params);
    }
    /**
     * 根据ids查询流水
     *
     * @param ids
     * @return BtBankAccDtlVO
     */
    public List<BisAccDtlVO> getByUnionIds(List<String> ids) {
        String sql = " select t.id,t.acc_no,t.bank_acc,t.acc_name,t.bank_name,t.bif_code,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign," +
                " t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,t.voucher_no,t.bank_serial_no,t.return_time, t.claim_id," +
                " t.import_sign,t.import_date,t.create_by,t.create_date,t.update_by,t.update_date,t.fbs_status, t.project_remark,t.REFUND_BLEND_TYPE from bis_acc_dtl t " +
                " where 1=1 and  " +
                SqlUtil.getInSql(ids, "t.id", true);
        List<BisAccDtlVO> list = listBySQLAliasToBean(sql, null, BisAccDtlVO.class);
        return list;
    }

    /**
             * 根据ids查询流水
     *
     * @param ids
     * @return BtBankAccDtlVO
     */
    public List<BisAccDtl> getByIds(List<String> ids) {
    	if(CollectionUtils.isEmpty(ids)){
    		return new ArrayList<BisAccDtl>();
    	}
        String sql = " SELECT ID, BANK_ACC, ACC_NAME, BANK_NAME, BIF_CODE, OPP_ACC_NO, OPP_ACC_NAME, OPP_ACC_BANK, CD_SIGN, AMT, BAL, CUR_ID, USES, POSTSCRIPT, REMARK, "
        		+ "TRANS_TIME, VOUCHER_NO, BANK_SERIAL_NO, RETURN_TIME, RB_SIGN, ACC_NO, IMPORT_SIGN, IMPORT_DATE, CREATE_DATE, CREATE_BY, UPDATE_DATE, UPDATE_BY, "
        		+ "FBS_STATUS, PROJECT_REMARK, SETTL_TYPE, BLEND_SIGN, REFUND_BLEND_TYPE, RMK, BILL_ID, MERGE_ID, SPLIT_ID, STATUS, BUS_TYPE, CLAIM_ID, BIS_DTL_NO, "
        		+ "BIS_EBILL_NO, AUTO_SIGN, corp_id, corp_name, bank_type_id, bank_type_name, CUR_NAME, PAYMENTBLENDING_DATE, AUTO_SIGN_DATE, mactibkn, mactacn, "
        		+ "mactname, mactbank, RETURN_MSG, REFUND_BLEND_BY, REFUND_BLEND_TIME, RECEIPT_BLEND_SIGN, AGAIN_SIGN, TALLY_DATE, TRANSACTION_TYPE_CODE, "
        		+ "TRANSACTION_TYPE_NAME, DTL_SYNC_STATUS, OP_TIME, SOURCE_NUM, BFS_REQUEST_NO, BUS_GLOBAL_ID, ESTATE, OPERATOR_CODE, BOOK_ID, TALLY_VOUCHER_NO, "
        		+ "ORDER_NO, TALLY_SIGN FROM bis_acc_dtl where 1=1 and  " +
                SqlUtil.getInSql(ids, "ID", true);
        List<BisAccDtl> list = listBySQLAliasToBean(sql, null, BisAccDtl.class);
        return list;
    }

    private void accCommonSql(String corpAuthSql, Map<String, Object> params, StringBuilder sql) {
        sql.append(" select acc.id as id, acc.trans_time AS transTime, acc.corp_id AS corpId, acc.corp_name AS corpName, acc.bank_acc AS bankAcc, acc.acc_name AS accName,acc.BANK_NAME AS bankName,");
        sql.append(" acc.bank_type_id AS bankTypeId, acc.bank_type_name AS bankTypeName, acc.cd_sign AS cdSign, acc.cur_id AS curId, '人民币' as curName, ");
        sql.append(" acc.remark AS remark,acc.uses AS uses, acc.postscript AS postscript, acc.bank_serial_no AS bankSerialNo, acc.claim_id as claimId,");
        sql.append(" acc.OPP_ACC_NO AS oppAccNo, acc.OPP_ACC_NAME AS oppAccName, acc.OPP_ACC_BANK AS oppAccBank, acc.amt AS amt, acc.bal AS bal  ");
        sql.append(" from BIS_ACC_DTL ACC ");
        sql.append(" WHERE (ACC.STATUS <> :STATUS_2 OR ACC.STATUS IS NULL) AND acc.CUR_ID = :RMB ");
        sql.append(" AND (ACC.BLEND_SIGN = :BLEND_SIGN0 or ACC.BLEND_SIGN is NULL ) AND ACC.ID NOT IN ");
        sql.append(" (SELECT ACC2.ID FROM BIS_ACC_DTL ACC2 INNER JOIN BIS_EXC E ON ACC2.BANK_SERIAL_NO = E.BANK_SERIAL_NO) ");
        if (corpAuthSql != null) {
            sql.append(" and ").append(corpAuthSql).append(" ");
        }
        params.put("RMB", BtCurrencyConstant.RMBID);
        params.put("STATUS_2", CommonConstant.STATUE_DELETE);
        params.put("BLEND_SIGN0", CommonConstant.NO);
    }
    /**
     * 根据查询参数，获取票据认领处理的银行流水数据。
     *
     * @param params      查询参数。
     * @param corpAuthSql 单位权限。
     * @return 银行流水数据。
     */
    public List<BisAccDtlDTO> getClaimBillAccInfo(Map<String, Object> params, String corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        // 组装公共SQL
        accCommonSql(corpAuthSql, params, sql);
        if (params.get("cdSign") != null) {
            sql.append(" and acc.cd_sign = :cdSign ");
        }
        if (params.get("bankTypeId") != null) {
            sql.append(" and b.bank_type_id = :bankTypeId ");
        }
        if (params.get("field") != null && params.get("origNo") != null) {
            sql.append(" and acc.").append(params.get("field")).append(" like :origNo ");
        }
        if (params.get("bankAcc") != null) {
            sql.append(" and acc.bank_acc in (:bankAcc) ");
        }
        if (params.get("billMoney") != null) {
            sql.append(" and acc.amt = :billMoney ");
        }
        if (params.get("claimDate") != null) {
            sql.append(" and to_char(acc.trans_time,'yyyy-MM-dd') = :claimDate ");
        }
        if (params.get("billMoneyLeft") != null) {
            sql.append(" and acc.amt >= :billMoneyLeft ");
            sql.append(" and acc.amt <= :billMoneyRight ");
        }
        if (params.get("claimDateLeft") != null) {
            sql.append(" and to_char(acc.trans_time,'yyyy-MM-dd') >= :claimDateLeft ");
            sql.append(" and to_char(acc.trans_time,'yyyy-MM-dd') <= :claimDateRight ");
        }
        if (params.get("accIdList") != null && ((String[]) params.get("accIdList")).length > 0) {
            sql.append(" and acc.id not in (:accIdList) ");
        }
        return listBySQLAliasToBean(sql.toString(), params, BisAccDtlDTO.class);
    }
    /**
     * 根据查询参数，获取票据认领处理的银行流水数据。
     *
     * @param dataTablesInput 查询参数。
     * @param corpAuthSql     单位权限。
     * @return 银行流水数据。
     */
    public DataTablesOutput listAbClaimInfoAcc(DataTablesInput dataTablesInput, String corpAuthSql) {
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql = new StringBuilder();
        // 组装公共SQL
        accCommonSql(corpAuthSql, params, sql);
        return this.getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlDTO.class, "", false, params);
    }
    public DataTablesOutput<BisAccDtlDTO> queryFcBankAccDtl(DataTablesInput input, String menuType, List<String> dtlIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.id as accDtlId,a.trans_time as transTime,a.bank_name as bankName,a.bank_acc as bankAcc,a.acc_name as accName,a.amt, ");
        sql.append("a.remark,a.opp_acc_no as oppAccNo,a.opp_acc_bank as oppAccBank,a.acc_no as accNo ");
        sql.append("from bis_acc_dtl a ");
        if ("up".equals(menuType)) {
            sql.append(" where a.cd_sign = '0' ");
        } else {
            sql.append(" where a.cd_sign = '1' ");
        }
        String leTime = (String) input.getFormdata().get("led_transTime");
        String getTime = (String) input.getFormdata().get("ged_transTime");
        if (StringUtils.isNotEmpty(getTime)) {
            getTime = getTime + " 00:00:00";
            input.getFormdata().put("ged_transTime", getTime);
        }
        if (StringUtils.isNotEmpty(leTime)) {
            leTime = leTime + " 23:59:59";
            input.getFormdata().put("led_transTime", leTime);
        }
        return getDynamicBySQL(input, sql.toString(), BisAccDtlDTO.class, null, false);
    }
    /**
     * 通过sql语句查询明细列表
     * @param params
     * @param sql
     * @return
     */
    public List<BisAccDtlVO> getBisAccDtlListBySql(Map<String, Object> params, String sql){
        return listBySQLAliasToBean(sql, params, BisAccDtlVO.class);
    }
    public DataTablesOutput getInterfaceFcBankBill(DataTablesInput dataTablesInput, String jionSql) {
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
        }
        return getDynamicBySQL(dataTablesInput, jionSql, FcBankBillVo.class, null, false);
    }

    /**
     * 查询页面数据-记账
     */
    public List<BisAccDtlDTO> queryBisAccDtlDTO(DataTablesInput dataTablesInput, String menuType, List<String> accList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.*,'' as accDtlId,'0' as tallySign ");
        sql.append("from bis_acc_dtl a ");
        sql.append(" where 1=1 ");
        if ("up".equals(menuType)) {
            sql.append(" and a.cd_sign = '0' ");
        } else {
            sql.append(" and a.cd_sign = '1' ");
        }
        Map<String, Object> search = dataTablesInput.getFormdata();
        if (StringUtils.isNotEmpty(search.get("ged_transTime")) ) {
            sql.append(" and a.trans_time >= bt_to_date('" + search.get("ged_transTime") + " 00:00:00','yyyy-MM-dd  HH24:mi:ss') ");
        }
        if (StringUtils.isNotEmpty(search.get("led_transTime") )) {
            sql.append(" and a.trans_time <= bt_to_date('" + search.get("led_transTime") + " 23:59:59','yyyy-MM-dd  HH24:mi:ss') ");
        }

        if (StringUtils.isNotEmpty(search.get("likes_bankAcc") )) {
            sql.append(" and a.bank_acc like '%" + search.get("likes_bankAcc") + "%'");
        }
        if (StringUtils.isNotEmpty(search.get("eqs_curId") )) {
            sql.append(" and a.cur_id = '" + search.get("eqs_curId") + "'");
        }
        if (StringUtils.isNotEmpty(search.get("ins_remark"))) {
            String str = search.get("ins_remark").toString();
            String[] rmk = str.split(",");
            List<String> rmkList = Arrays.asList(rmk);
            sql.append(" and ").append(getSQLIn(rmkList, 900, "a.remark"));
        }
        if (StringUtils.isNotEmpty(search.get("geg_amt"))) {
            sql.append(" and a.amt >= " + search.get("geg_amt"));
        }
        if (StringUtils.isNotEmpty(search.get("leg_amt") )) {
            sql.append(" and a.amt <= " + search.get("leg_amt"));
        }
        if(CollectionUtil.isNotEmpty(accList)) {
            sql.append(" and ").append(getSQLIn(accList, 900, "a.bank_acc"));
        }
        Map<String, String> params = new HashMap<>();
        return listBySQLAliasToBean(sql.toString(),params, BisAccDtlDTO.class);
    }

    /**
     * 通过bankids的还原银行明细导出标志
     */
    public void recoverBankBillImportSign(String bankIds) {
        String sql = "update bis_acc_dtl set import_sign = '0',import_date = null " +
                " where id = :bankId and import_sign = '1' ";
        Map<String, Object> params = new HashMap<>();
        params.put("bankId", bankIds);
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 通过bankids的修改银行明细导出标志
     */
    public int updateBankBillImportSign(String bankIds) {
        int result = 0;
        if(EmptyUtils.isNotEmpty(bankIds)) {
            String[] ids = bankIds.split(",");
            Date now = DateUtils.getDate();
            String sql = "update bis_acc_dtl set import_sign = '1',import_date = :now " +
                    " where id in (:bankId) and (import_sign <> '1' or import_sign is null) ";
            Map<String, Object> params = new HashMap<>();
            params.put("bankId", ids);
            params.put("now", now);
            result = createSQLQuery(sql, params).executeUpdate();
        }
        return result;
    }

    /**
     * 获取银行明细列表
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public DataTablesOutput getDataTablesUnionPaymentBlending(DataTablesInput dataTablesInput, String joinSql,String allocationAllowBlending) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        //交易时间
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        String eqs_blendSign = (String) dataTablesInput.getFormdata().get("eqs_blendSign");
        String eqs_id = (String) dataTablesInput.getFormdata().get("eqs_id");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
        }
//        String sqlnoIn = "select dtl.id from bis_acc_dtl dtl left join bt_bank_acc acc on dtl.bank_acc = acc.bank_acc\n " +
//                " left join bt_bank_acc acc1 on dtl.opp_acc_no = acc1.bank_acc,bt_bank_acc_cur cur\n " +
//                " where dtl.cd_sign = '1' and ((acc.id = cur.bank_acc_id and acc1.id = cur.prent_acc_id) or (acc.id=cur.prent_acc_id and acc1.id=cur.bank_acc_id)) ";
        sql.append(" select t.id as id,t.acc_no as accNo,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode,t.corp_name AS corpName, ");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,t.cur_name as curName,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
        sql.append(" t.project_remark as projectRemark,case when t.BLEND_SIGN is null then '0' else t.BLEND_SIGN end as blendSign,t.bank_serial_no as bankSerialNo,t.paymentblending_date as paymentBlendingDate ");
        sql.append(" from bis_acc_dtl t  ");
        sql.append(" left join bis_exc b on t.bank_serial_no = b.bank_serial_no and b.bank_serial_no <> '' ");
        if (EmptyUtils.isNotEmpty(eqs_id)) {
            sql.append(" where t.cd_sign='1' and (b.voucher_from = '" + BdlinkConstant.FROM_NIS_PAY + "' or b.voucher_from is null) ");
        } else {
            if("0".equals(allocationAllowBlending)) {
//                sql.append(" where t.cd_sign='1' and (b.voucher_from = '" + BdlinkConstant.FROM_NIS_PAY + "' or b.voucher_from is null) and t.id not in (" + sqlnoIn + ") ");
                sql.append(" where t.cd_sign='1' and (b.voucher_from = '" + BdlinkConstant.FROM_NIS_PAY + "' or b.voucher_from is null) ");
            }else{
                sql.append(" where t.cd_sign='1' ");
            }
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if (BtBankAccConstant.BLENDSIGN_0.equalsIgnoreCase(eqs_blendSign)) {
            sql.append(" and (t.BLEND_SIGN =:blendSign or t.BLEND_SIGN is null) ");
        } else {
            if (StringUtils.isNotEmpty(eqs_blendSign)) {
                sql.append(" and t.BLEND_SIGN =:blendSign ");
            }
        }
        params.put("blendSign",eqs_blendSign);
        if (EmptyUtils.isNotEmpty(eqs_id)) {
            sql.append(" and    " + SqlUtil.getInSql(eqs_id.split("/"), "t.id", true));
        }
        dataTablesInput.getFormdata().remove("eqs_blendSign");
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlVO.class, "t", false, params);
    }

    /**
     * 获取银行明细列表
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public DataTablesOutput getDataTablesUnionRefundBlending(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        //交易时间
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        String eqs_blendSign = (String) dataTablesInput.getFormdata().get("eqs_blendSign");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
        }
//        String sqlnoIn = "select dtl.id from bis_acc_dtl dtl left join bt_bank_acc acc on dtl.bank_acc = acc.bank_acc\n " +
//                " left join bt_bank_acc acc1 on dtl.opp_acc_no = acc1.bank_acc,bt_bank_acc_cur cur\n " +
//                " where 1=1  and ((acc.id = cur.bank_acc_id and acc1.id = cur.prent_acc_id) or (acc.id=cur.prent_acc_id and acc1.id=cur.bank_acc_id)) ";
        sql.append(" select  t.id as id,t.acc_no as accNo,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode,t.corp_name AS corpName, ");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
        sql.append(" t.project_remark as projectRemark,case when t.BLEND_SIGN is null then '0' else t.BLEND_SIGN end as blendSign,t.bank_serial_no as bankSerialNo,");
        sql.append(" t.refund_Blend_Type as refundBlendType,REFUND_BLEND_TIME as paymentBlendingDate ");
        sql.append(" from bis_acc_dtl t ");
        sql.append(" left join bis_exc b on t.bank_serial_no = b.bank_serial_no ");
        sql.append(" left join bt_bank_acc acc on t.BANK_ACC  = acc.BANK_ACC ");
        sql.append(" left join bt_bank_acc_cur cur  on t.CUR_ID = cur.CUR_ID  ");
//        sql.append(" where 1 = 1 and t.cd_sign='0' and (b.voucher_from = '"+ BdlinkConstant.FROM_NIS_PAY+"' or b.voucher_from is null) and t.id not in ("+sqlnoIn+") "  );
        sql.append(" where 1 = 1 and t.cd_sign='0' and (b.voucher_from = '"+ BdlinkConstant.FROM_NIS_PAY+"' or b.voucher_from is null) "  );
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if(!EmptyUtils.isEmpty(eqs_blendSign)) {
            if (BtBankAccConstant.BLENDSIGN_0.equalsIgnoreCase(eqs_blendSign)) {
                sql.append(" and (t.BLEND_SIGN = '" + eqs_blendSign + "' or t.BLEND_SIGN is null) ");
            } else {
                sql.append(" and t.BLEND_SIGN = '" + eqs_blendSign + "' ");
            }
        }
        dataTablesInput.getFormdata().remove("eqs_blendSign");
        //根据查询条件及账户数据进行一次过滤
        List<String> corpList = null;
        String corpId = (String) dataTablesInput.getFormdata().get("ins_corpId");
        if (StringUtils.isNotBlank(corpId)) {
            String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
            corpList = Arrays.asList(corpIds);
        }
        if (EmptyUtils.isNotEmpty(corpList)) {
            String corpAuthSql = SqlUtil.getInSql(corpList, "acc.corp_id",true);
            sql.append(" and ").append(corpAuthSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlVO.class, "t", false);
    }

    /**
     * 更新流水勾兑状态
     *
     * @param id
     * @param blendSign
     * @return
     */
    public int toUpdateBlendSign(String id, String blendSign, String userName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update bis_acc_dtl set blend_sign = :blendSign, paymentblending_date = :paymentblendingDate , ");
        sql.append(" update_date = :updateDate , update_by = :updateBy  where id = :id ");
        //避免重复勾兑
        Date paymentblendingDate = null;
        if(BtBankAccConstant.BLENDSIGN_1.equals(blendSign)){
            paymentblendingDate = new Date();
//            sql += " and (blend_sign<>:blendSign or blend_sign is null or blend_sign = '' ) ";
            sql.append("and (blend_sign<>:blendSign or blend_sign is null or blend_sign = '' ) ");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("blendSign", blendSign);
        map.put("paymentblendingDate", paymentblendingDate);
        map.put("updateDate",new Date());
        map.put("updateBy", userName);
        return createSQLQuery(sql.toString(), map).executeUpdate();
    }

    /**
     * 更新流水银行流水号
     *
     * @param id
     * @param bankSerialNo
     * @return
     */
    public int toUpdateBankSerialNo(String id, String bankSerialNo) {
        String sql = " update bis_acc_dtl set bank_serial_no = :bankSerialNo where id = :id";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("bankSerialNo", bankSerialNo);
        return createSQLQuery(sql, map).executeUpdate();
    }

    /**
     * 分批查询匹配的银行流水。
     *
     * @param bankTypeId 银行类型编号。
     * @param remark     用途/摘要。
     * @param transTime  交易日期。
     * @param busType    业务类型ID。
     * @param startIndex 开始行数。
     * @param maxIndex   结束行数。
     * @return 银行流水。
     */
    public List<BisAccDtlVO> listDtlByInterestRule(String bankTypeId, String remark, Date transTime, String busType, Integer startIndex, Integer maxIndex) {
        String sql = "select bad.id,bad.bank_acc as bankAcc,bad.bus_type as busType,bad.bank_name as bankName,bad.uses,bad.remark,bad.trans_time as transTime " +
                " from bis_acc_dtl bad left join bt_bank_acc bba on bad.bank_acc = bba.bank_acc " +
                " where bba.bank_type_id = :bankTypeId and trans_time between :startTime and :endTime " +
                " and ( bad.uses like :remark or bad.remark like :remark ) " +
                " and ( bad.bus_type is null or bad.bus_type != :busType ) ";
        Map<String, Object> params = new HashMap<>();
        params.put("bankTypeId", bankTypeId);
        params.put("startTime", DateUtil.beginOfDay(transTime).toJdkDate());
        params.put("endTime", DateUtil.endOfDay(transTime).toJdkDate());
        params.put("remark", "%" + remark + "%");
        params.put("busType", busType);
        return listBySQLAliasToBean(sql, startIndex, maxIndex, params, BisAccDtlVO.class);
    }

    /**
     * 分批查询匹配的银行流水。
     *
     * @param bankTypeId 银行类型编号。
     * @param remark     用途/摘要。
     * @param transTime  交易日期。
     * @param busType    业务类型ID。
     * @param startIndex 开始行数。
     * @param maxIndex   结束行数。
     * @return 银行流水。
     */
    public List<BisAccDtlVO> listDtlByFlowRule(String bankTypeId, String remark, Date transTime, String busType, Integer startIndex, Integer maxIndex) {
        String sql = "select bad.id,bad.bank_acc as bankAcc,bad.bus_type as busType,bad.bank_name as bankName,bad.uses,bad.remark,bad.trans_time as transTime " +
                " from bis_acc_dtl bad left join bt_bank_acc bba on bad.bank_acc = bba.bank_acc " +
                " where bba.bank_type_id = :bankTypeId and trans_time between :startTime and :endTime " +
                " and ( bad.uses like :remark or bad.remark like :remark ) " +
                " and ( bad.bus_type is null or bad.bus_type != :busType ) ";
        Map<String, Object> params = new HashMap<>();
        params.put("bankTypeId", bankTypeId);
        params.put("startTime", DateUtil.beginOfDay(transTime).toJdkDate());
        params.put("endTime", DateUtil.endOfDay(transTime).toJdkDate());
        params.put("remark", "%" + remark + "%");
        params.put("busType", busType);
        return listBySQLAliasToBean(sql, startIndex, maxIndex, params, BisAccDtlVO.class);
    }

    /**
     * 查询匹配的银行流水总行数。
     *
     * @param list 匹配规则集合。
     * @param transferMode     调拨方式。
     * @return 银行流水行数。
     */
    public int countDtlByFlowRule(List<NisFlowRulePurposeDTO> list,String transferMode) {
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select count(*) from bis_acc_dtl bad left join bt_bank_acc bba on bad.OPP_ACC_NO = bba.BANK_ACC left join sys_corp sc on bba.CORP_ID = sc.ID where 1 = 1 ");
        if(!"2".equals(transferMode)){
            sql.append(" and ( bad.uses like :remark or bad.remark like :remark or bad.postscript like :remark ) ");
            params.put("remark", "%" + list.get(0).getPurpose() + "%");
            if("1".equals(transferMode)){
                //“资金系统上划”和“资金系统下拨”增加校验规则：对方账号必须为资金中心或集团本部账号（单位编码为100和100000）
                sql.append(" and ( sc.CODE = '100' or sc.CODE = '100000' ) ");
            }
        }else{
            sql.append(" and (");
            for(int i = 0; i < list.size(); i++){
                NisFlowRulePurposeDTO nisFlowRulePurposeDTO = list.get(i);
                if(i > 0){
                    sql.append(" or ");
                }
                sql.append(" (bad.bif_code = :bif_code"+ i +" ");
                sql.append(" and (bad.uses like :remark"+ i +" or bad.remark like :remark"+ i +" or bad.postscript like :remark"+ i +" )) ");
                params.put("bif_code" + i , nisFlowRulePurposeDTO.getBifCode());
                params.put("remark" + i, nisFlowRulePurposeDTO.getPurpose());
            }
            sql.append(")");
        }

        sql.append(" and (bad.bus_type is null or bad.bus_type = '')");
        return Convert.toInt(createSQLQuery(sql.toString(), params).uniqueResult(), 0);
    }

    /**
     * 查询匹配的银行流水总行数。
     *
     * @param bankTypeId 银行类型编号。
     * @param remark     用途/摘要。
     * @param transTime  交易日期。
     * @param busType    业务类型ID。
     * @return 银行流水行数。
     */
    public int countDtlByInterestRule(String bankTypeId, String remark, Date transTime, String busType) {
        String sql = "select count(*) " +
                " from bis_acc_dtl bad left join bt_bank_acc bba on bad.bank_acc = bba.bank_acc " +
                " where bba.bank_type_id = :bankTypeId and trans_time between :startTime and :endTime " +
                " and ( bad.uses like :remark or bad.remark like :remark ) " +
                " and ( bad.bus_type is null or bad.bus_type != :busType ) ";
        Map<String, Object> params = new HashMap<>();
        params.put("bankTypeId", bankTypeId);
        params.put("startTime", DateUtil.beginOfDay(transTime).toJdkDate());
        params.put("endTime", DateUtil.endOfDay(transTime).toJdkDate());
        params.put("remark", "%" + remark + "%");
        params.put("busType", busType);
        return Convert.toInt(createSQLQuery(sql, params).uniqueResult(), 0);
    }

    /**
     * 自动认领银行流水。
     *
     * @param dtlIdList 银行流水编号列表。
     * @param busType   业务类型ID。
     * @return 响应条数。
     */
    public int updateBusTypeByIdList(List<String> dtlIdList, String busType) {
        String sql = " update bis_acc_dtl set bus_type = :busType,fbs_status =:status where id in (:dtlIdList)";
        Map<String, Object> params = new HashMap<>();
        params.put("busType", busType);
        params.put("dtlIdList", dtlIdList);
        params.put("status", BdlinkConstant.FBS_STATUE_CONFIRM);
        return createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 分批查询匹配的银行流水。
     * @param startIndex 开始行数。
     * @param maxIndex   结束行数。
     * @param list    规则集合。
     * @param transferMode    调拨方式。
     * @return 银行流水。
     */
    public List<BisAccDtlVO> listDtlByFlowRule(Integer startIndex, Integer maxIndex, List<NisFlowRulePurposeDTO> list,String transferMode) {
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select bad.id from bis_acc_dtl bad left join bt_bank_acc bba on bad.OPP_ACC_NO = bba.BANK_ACC left join sys_corp sc on bba.CORP_ID = sc.ID where 1 = 1 ");
        if(!"2".equals(transferMode)){
            sql.append(" and ( bad.uses like :remark or bad.remark like :remark or bad.postscript like :remark ) ");
            params.put("remark", "%" + list.get(0).getPurpose() + "%");
            /*if("1".equals(transferMode)){
                //“资金系统上划”和“资金系统下拨”增加校验规则：对方账号必须为资金中心或集团本部账号（单位编码为100和100000）
                sql.append(" and ( sc.CODE = '100' or sc.CODE = '100000' ) ");
                // 此条件放到最后，后面需求又加了其他四种业务类型，所以放到最后。
            }*/
        }else{
            sql.append(" and (");
            for(int i = 0; i < list.size(); i++){
                NisFlowRulePurposeDTO nisFlowRulePurposeDTO = list.get(i);
                if(i > 0){
                    sql.append(" or ");
                }
                sql.append(" (bad.bif_code = :bif_code"+ i +" ");
                sql.append(" and (bad.uses like :remark"+ i +" or bad.remark like :remark"+ i +" or bad.postscript like :remark"+ i +")) ");
                params.put("bif_code" + i , nisFlowRulePurposeDTO.getBifCode());
                params.put("remark" + i, nisFlowRulePurposeDTO.getPurpose());
            }
            sql.append(")");
        }
        // 下面业务类型，增加对方账号必须为资金中心或集团本部账号（单位编码为100和100000）
        // “银行上划”对应业务种类编码为：bs006，“自主上划”对应业务种类编码为：bs007
        // "银行下拨"对应业务种类编码为：bs009，“自主下拨”对应业务种类编码为：bs010
        /*if (NisBusTypeConstant.SYS_BUS_TYPE_BS009.equals(busType) || NisBusTypeConstant.SYS_BUS_TYPE_BS010.equals(busType)
            || NisBusTypeConstant.SYS_BUS_TYPE_BS006.equals(busType) || NisBusTypeConstant.SYS_BUS_TYPE_BS007.equals(busType)){
                sql.append(" and ( sc.CODE = '100' or sc.CODE = '100000' ) ");
        }*/
        sql.append(" and ( sc.CODE = '100' or sc.CODE = '100000' ) ");
        sql.append(" and (bad.bus_type is null or bad.bus_type = '')");
        return listBySQLAliasToBean(sql.toString(), startIndex, maxIndex, params, BisAccDtlVO.class);
    }

    public List<BisAccDtlDTO> listBisAccDtlByBusType(Date queryDate, List<String> busTypes){
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        params.put("queryDate", queryDate);
        params.put("busTypes", busTypes);
        sql.append(" select t.id AS id,t.acc_no AS accNo,acc.corp_id AS corpId,t.bank_acc AS bankAcc,t.acc_name AS accName,t.bank_name AS bankName, ");
        sql.append(" t.bif_code AS bifCode,t.opp_acc_no AS oppAccNo,t.opp_acc_name AS oppAccName,t.opp_acc_bank AS oppAccBank,");
        sql.append(" t.cd_sign AS cdSign,t.amt AS amt,t.bal AS bal,t.cur_id AS curId,t.uses AS uses,t.postscript AS postscript,");
        sql.append(" t.remark AS remark,t.trans_time AS transTime,t.bank_serial_no AS bankSerialNo,t.project_remark AS projectRemark");
        sql.append(" FROM bis_acc_dtl t LEFT JOIN bt_bank_acc acc ON t.bank_acc = acc.bank_acc");
        sql.append(" where DATE_FORMAT(t.trans_time, '%Y-%m') =:queryDate ");
        sql.append(" and t.bus_type in (:busTypes) ");

        return listBySQLAliasToBean(sql.toString(), params, BisAccDtlDTO.class);
    }

    /**
     * 根据银行账号、查询日期、业务类型
     * @param bankAccs
     * @param queryDate
     * @param busTypes
     * @return
     */
    public List<BisAccDtlDTO> listBisAccDtlByBankAccQueryDateBusTypes(List<String> bankAccs,Date queryDate, List<String> busTypes){
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        params.put("queryDate", DateUtils.format(queryDate, "yyyy-MM-dd"));

        sql.append(" select t.id AS id,t.acc_no AS accNo,acc.corp_id AS corpId,t.bank_acc AS bankAcc,t.acc_name AS accName,t.bank_name AS bankName, ");
        sql.append(" t.bif_code AS bifCode,t.opp_acc_no AS oppAccNo,t.opp_acc_name AS oppAccName,t.opp_acc_bank AS oppAccBank,");
        sql.append(" t.cd_sign AS cdSign,t.amt AS amt,t.bal AS bal,t.cur_id AS curId,t.uses AS uses,t.postscript AS postscript,");
        sql.append(" t.remark AS remark,t.trans_time AS transTime,t.bank_serial_no AS bankSerialNo,t.project_remark AS projectRemark");
        sql.append(" FROM bis_acc_dtl t LEFT JOIN bt_bank_acc acc ON t.bank_acc = acc.bank_acc");
        sql.append(" where BT_TO_CHAR(t.trans_time, 'yyyy-MM-dd') =:queryDate ");
        sql.append(" and ").append(SqlUtil.getInSql(bankAccs,"t.bank_acc",true));
        sql.append(" and ").append(SqlUtil.getInSql(busTypes,"t.bus_type",true));

        return listBySQLAliasToBean(sql.toString(), params, BisAccDtlDTO.class);
    }

    /**
     * 根据单位ID、查询日期、业务类型
     * @param corpIds
     * @param queryDate
     * @param busTypes
     * @return
     */
    public List<BisAccDtlDTO> listBisAccDtlByCorpIdsQueryDateBusTypes(List<String> corpIds,Date queryDate, List<String> busTypes){
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        params.put("queryDate", queryDate);

        sql.append(" select t.id AS id,t.acc_no AS accNo,acc.corp_id AS corpId,t.bank_acc AS bankAcc,t.acc_name AS accName,t.bank_name AS bankName,t.bus_type AS busType, ");
        sql.append(" t.bif_code AS bifCode,t.opp_acc_no AS oppAccNo,t.opp_acc_name AS oppAccName,t.opp_acc_bank AS oppAccBank,");
        sql.append(" t.cd_sign AS cdSign,t.amt AS amt,t.bal AS bal,t.cur_id AS curId,t.uses AS uses,t.postscript AS postscript,");
        sql.append(" t.remark AS remark,t.trans_time AS transTime,t.bank_serial_no AS bankSerialNo,t.project_remark AS projectRemark");
        sql.append(" FROM bis_acc_dtl t LEFT JOIN bt_bank_acc acc ON t.bank_acc = acc.bank_acc");
        sql.append(" where DATE_FORMAT(t.trans_time, '%Y-%m') = DATE_FORMAT(:queryDate, '%Y-%m') ");
        sql.append(" and ").append(SqlUtil.getInSql(corpIds,"acc.corp_id",true));
        sql.append(" and ").append(SqlUtil.getInSql(busTypes,"t.bus_type",true));

        return listBySQLAliasToBean(sql.toString(), params, BisAccDtlDTO.class);
    }

    /**
     * 根据单位ID、业务类型、开始日期，结束日期
     * @param corpIds
     * @param busTypes
     * @param queryDateBegin
     * @param queryDateEnd
     * @return
     */
    public List<BisAccDtlDTO> listBisAccDtlByCorpIdsQueryDateBusTypes(List<String> corpIds,List<String> busTypes,Date queryDateBegin,Date queryDateEnd,String accountGroup){
        StringBuffer sql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        params.put("queryDateBegin", queryDateBegin);
        params.put("queryDateEnd", queryDateEnd);

        sql.append(" select t.id AS id,t.acc_no AS accNo,acc.corp_id AS corpId,t.bank_acc AS bankAcc,t.acc_name AS accName,t.bank_name AS bankName,t.bus_type AS busType, ");
        sql.append(" t.bif_code AS bifCode,t.opp_acc_no AS oppAccNo,t.opp_acc_name AS oppAccName,t.opp_acc_bank AS oppAccBank,");
        sql.append(" t.cd_sign AS cdSign,t.amt AS amt,t.bal AS bal,t.cur_id AS curId,t.uses AS uses,t.postscript AS postscript,");
        sql.append(" t.remark AS remark,t.trans_time AS transTime,t.bank_serial_no AS bankSerialNo,t.project_remark AS projectRemark");
        sql.append(" FROM bis_acc_dtl t LEFT JOIN bt_bank_acc acc ON t.bank_acc = acc.bank_acc");
        sql.append(" where BT_TO_CHAR(t.trans_time, 'yyyy-MM-dd') >= BT_TO_CHAR(:queryDateBegin, 'yyyy-MM-dd') ");
        sql.append(" and BT_TO_CHAR(t.trans_time, 'yyyy-MM-dd') <= BT_TO_CHAR(:queryDateEnd, 'yyyy-MM-dd') ");
        sql.append(" and ").append(SqlUtil.getInSql(corpIds,"acc.corp_id",true));
        sql.append(" and ").append(SqlUtil.getInSql(busTypes,"t.bus_type",true));
        if (ObjectUtil.isNotEmpty(accountGroup)){
            sql.append(" and INSTR(acc.FC_ACC_GROUP_CODE, :accountGroup) > 0 ");
            params.put("accountGroup", accountGroup);
        }

        return listBySQLAliasToBean(sql.toString(), params, BisAccDtlDTO.class);
    }

    public BigDecimal getBnakAccDtlForReportDate(List<String> corpIds, Date startDate, Date endDate,String bankTypeId, String bankCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(dtl.amt) money");
        sql.append(" from bis_acc_dtl dtl ");
        sql.append(" left join bt_bank_acc acc on dtl.bank_acc = acc.bank_acc");
        sql.append(" left join bt_currency btcur on dtl.cur_id = btcur.id ");
        sql.append(" where dtl.status = 95 ");
        sql.append(" and dtl.trans_time >= :startDate and dtl.trans_time <= :endDate ");
        sql.append(" and btcur.CUR_NAME='人民币' ");
        sql.append(" and "+SqlUtil.getInSql(corpIds, "acc.corp_id", true));
        if(StringUtils.isNotEmpty(bankTypeId)){
            sql.append(" and acc.BANK_TYPE_ID= :bankTypeId ");
            params.put("bankTypeId", bankTypeId);
        }
        if(StringUtils.isNotEmpty(bankCode)){
            sql.append(" and acc.BANK_CODE= :bankCode");
            params.put("bankCode", bankCode);
        }
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        Object result = this.createSQLQuery(sql.toString(), params).uniqueResult();
        BigDecimal balance = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(result)) {
            balance = (BigDecimal) result;
        }
        return balance;
    }

    public BisAccDtlDTO getLastDtlBal(String bankAcc, String curId, Date transTime) {
        if (EmptyUtils.isEmpty(transTime)) {
            return null;
        }
        Date startTime = DateUtils.getIntegralStartTime(transTime);
        Date endTime = DateUtils.getIntegralEndTime(transTime);

        StringBuilder sql = new StringBuilder();
        sql.append(" select * from bis_acc_dtl ");
        sql.append(" where bank_acc = :bankAcc and cur_id = :curId ");
        sql.append(" and trans_time >= :startTime and trans_time <= :endTime ");
        sql.append(" order by trans_time desc, acc_no desc ");

        Map<String, Object> params = new HashMap<>();
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        List<BisAccDtlDTO> list = listBySQLAliasToBean(sql.toString(), params, BisAccDtlDTO.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }
}
