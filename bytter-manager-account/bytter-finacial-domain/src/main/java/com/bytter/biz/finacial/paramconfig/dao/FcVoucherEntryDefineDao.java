package com.bytter.biz.finacial.paramconfig.dao;

import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherEntryDefine;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherEntryDefineVo;
import com.bytter.core.base.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: FC_VOUCHER_ENTRY_DEFINE
 * @date 2017-05-22 16:44:06
 */
@Repository
public class FcVoucherEntryDefineDao extends BaseDao<FcVoucherEntryDefine, String> {

    /**
     * 功能介绍 ： 根据记账规则获取规则记账分录。<br>
     * <b>日期：</b>2017年5月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcVoucherEntryDefine> getEntryDefinesByRuleId(String ruleId) {
        String hql = " from FcVoucherEntryDefine where businessRuleDefineId = :ruleId order by groupNum ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ruleId", ruleId);
        return this.createQuery(hql, params).list();
    }

    /**
     * 功能介绍 : 获取手续费记账规则明细。<br>
     * <b>日期：</b>2017年7月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcVoucherEntryDefine> getChargeDefinesByRuleId(String ruleId) {
        String hql = " from FcVoucherEntryDefine where businessRuleDefineId = :ruleId and belongType = :belongType order by balanceWay desc ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ruleId", ruleId);
        params.put("belongType", FinacialConstant.BELONG_TYPE_CHARGE);
        return this.createQuery(hql, params).list();
    }

    /**
     * 功能介绍 ： 根据记账规则ID删除规则记账分录。<br>
     * <b>日期：</b>2017年5月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteEntryDefinesByRuleId(String ruleId) {
        String hql = " delete from FcVoucherEntryDefine where businessRuleDefineId = :ruleId ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ruleId", ruleId);
        this.batchExecute(hql, params);
    }

    /**
     * 功能介绍 ： 根据记账规id则获取规则记账分录。<br>
     * <b>日期：</b>2017年9月21日<br>
     *
     * @version 1.0
     * @author yangl
     */
    public List<FcVoucherEntryDefineVo> getVoucherEntryDefinesByRuleId(String ruleId) {
        StringBuilder sql = new StringBuilder();
        /*sql.append("select fc.id as id,fc.business_rule_define_id as businessRuleDefineId, ");
        sql.append("fc.belong_type as belongType,fc.balance_way as balanceWay,fc.subject_code as subjectCode, ");
        sql.append("fs.id as subjectId,fs.subject_name as subjectName,fc.summary as summary, ");
        sql.append("fc.entry_type as entryType,fc.group_num as groupNum ");
        sql.append(" from FC_VOUCHER_ENTRY_DEFINE fc ");
        sql.append(" left join fc_subject fs on fc.subject_code=fs.id");
        sql.append(" where fc.business_rule_define_id = :ruleId ");
        sql.append(" order by fc.group_num,fc.balance_way desc ");*/
        sql.append("select fc.id as id,fc.business_rule_define_id as businessRuleDefineId, ");
        sql.append("fc.belong_type as belongType,fc.balance_way as balanceWay,fc.subject_code as subjectCode, ");
        sql.append("fc.subject_code as subjectId,fc.summary as summary, ");
        sql.append("fc.entry_type as entryType,fc.group_num as groupNum, fc.blnctr_acc as blnctrAcc ");
        sql.append(" from FC_VOUCHER_ENTRY_DEFINE fc ");
        sql.append(" where fc.business_rule_define_id = :ruleId ");
        sql.append(" order by fc.group_num,fc.balance_way desc ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ruleId", ruleId);
        return listBySQLAliasToBean(sql.toString(), params, FcVoucherEntryDefineVo.class);
    }

    /**
     * 功能介绍 ： 根据记账规id则获取规则记账分录。<br>
     * <b>日期：</b>2017年9月21日<br>
     *
     * @version 1.0
     * @author yangl
     */
    public List<FcVoucherEntryDefineVo> getInitVoucherEntryDefinesByRuleId(String ruleId) {
        StringBuilder sql = new StringBuilder();
        /*sql.append("select fc.id as id,fc.business_rule_define_id as businessRuleDefineId, ");
        sql.append("fc.belong_type as belongType,fc.balance_way as balanceWay,fc.subject_code as subjectCode, ");
        sql.append("fs.subject_name as subjectName,fc.summary as summary, ");
        sql.append("fc.entry_type as entryType,fc.group_num as groupNum ");
        sql.append(" from FC_VOUCHER_ENTRY_INIT fc ");
        sql.append(" left join fc_subject fs on fc.subject_code=fs.id");
        sql.append(" where fc.business_rule_define_id = :ruleId ");
        sql.append(" order by fc.group_num,fc.balance_way desc ");*/
        sql.append("select fc.id as id,fc.business_rule_define_id as businessRuleDefineId, ");
        sql.append("fc.belong_type as belongType,fc.balance_way as balanceWay,fc.subject_code as subjectCode, ");
        sql.append("fc.summary as summary, ");
        sql.append("fc.entry_type as entryType,fc.group_num as groupNum ");
        sql.append(" from FC_VOUCHER_ENTRY_INIT fc ");
        sql.append(" where fc.business_rule_define_id = :ruleId ");
        sql.append(" order by fc.group_num,fc.balance_way desc ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ruleId", ruleId);
        return listBySQLAliasToBean(sql.toString(), params, FcVoucherEntryDefineVo.class);
    }

    /***
     * 删除已初始化的凭证分录
     */
    public void deleteInitVoucherEntry() {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from FC_VOUCHER_ENTRY_DEFINE fb where fb.IS_INIT = '1' ");
        createSQLQuery(sql.toString()).executeUpdate();
    }

    /***
     * 根据记账规则ID获取凭证分录
     *
     * @param businessRuleDefineId 记账规则ID
     * @return 凭证分录（记账规则明细）
     */
    public List<FcVoucherEntryDefine> listInitVoucherEntry(String businessRuleDefineId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select fb.BUSINESS_RULE_DEFINE_ID, fb.BELONG_TYPE, fb.BALANCE_WAY, fb.SUBJECT_CODE, fb.ACCOUNT, ");
        sql.append(" fb.CUR_CODE, fb.SUMMARY, fb.ENTRY_TYPE, fb.GROUP_NUM, fb.VOUCHER_TYPE ");
        sql.append(" from FC_VOUCHER_ENTRY_INIT fb ");
        sql.append(" WHERE fb.BUSINESS_RULE_DEFINE_ID = :businessRuleDefineId ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("businessRuleDefineId", businessRuleDefineId);
        return listBySQLAliasToBean(sql.toString(), params, FcVoucherEntryDefine.class);
    }
}
