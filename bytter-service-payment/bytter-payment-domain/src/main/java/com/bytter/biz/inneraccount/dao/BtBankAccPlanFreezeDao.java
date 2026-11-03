package com.bytter.biz.inneraccount.dao;

import com.bytter.biz.inneraccount.entity.BtBankAccPlanFreeze;
import com.bytter.biz.inneraccount.vo.BtBankAccPlanFreezeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.vo.BtBankAccVO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 银行账户计划冻结申请DAO层
 *
 * @author fangcf
 * @date 2025-02-11
 */
@Repository
public class BtBankAccPlanFreezeDao extends BaseDao<BtBankAccPlanFreeze, String> {

    /**
     * 银行账户计划冻结列表
     *
     * @return 银行账户计划冻结数据
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id, acc.bank_acc as bankAcc, acc.acc_name as accName, acc.bank_name as bankName, acc.corp_id as corpId, corp.name as corpName, ");
        sql.append(" acccur.cur_id as curId, c.cur_name curName, acc.acc_type as accType, att.attribute_name as attributeName, ");
        sql.append(" nat.nature_name as natureName, banktype.type_name as bankTypeName, acc.reg_date, acc.electric_bill as electricBill ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join sys_corp corp on acc.corp_id = corp.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id ");
        sql.append(" left join bt_currency c on c.id = acccur.cur_id ");
        sql.append(" where acc.status = ").append(BtBankAccConstant.STATUE_CHECK).append(" and acc.VALID_SIGN = ").append(BtBankAccConstant.VALIDSIGN_1);
        sql.append(" and acc.bank_acc not in (select bank_acc from bt_bank_acc_app where status <> ").append(BtBankAccConstant.STATUE_CANCELLATION).append(" and bank_acc is not null ) ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if (EmptyUtils.isEmptyList(dataTablesInput.getOrder())) {
            dataTablesInput.setOrderString(new OrderString("acc.reg_date", "desc"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "", false);
    }

    /**
     * 查询银行账户计划冻结申请记录
     *
     * @param bankAccId 银行账号id
     * @param curId     币别id
     * @param status    单据状态：0-暂存，95：完成
     * @return 计划冻结申请集合
     */
    public List<BtBankAccPlanFreezeVO> listBtBankAccPlanFreeze(String bankAccId, String curId, Integer status) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder hql = new StringBuilder();
        hql.append(" from BtBankAccPlanFreeze bf where bf.bankAccId = :bankAccId and bf.curId = :curId ");
        if (null != status) {
            hql.append(" and bf.status = :status ");
            params.put("status", status);
        } else {
            hql.append(" and bf.status <> :status ");
            params.put("status", BtBankAccConstant.STATUE_DEL);
        }
        params.put("bankAccId", bankAccId);
        params.put("curId", curId);
        return this.listByHQLAliasToBean(hql.toString(), params, BtBankAccPlanFreezeVO.class);
    }

    /**
     * 查询符合自动冻结条件的记录
     *
     * @param now 当前时间
     * @return 计划冻结申请集合
     */
    public List<BtBankAccPlanFreezeVO> listAutoFreezeRecord(Date now) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from BT_BANK_ACC_PLAN_FREEZE bf ");
        sql.append(" where bf.STATUS = :status and bf.FREEZE_STATUS in (:freezeStatus) ");
        sql.append(" and bf.FREEZE_START_DATE <= :now and bf.FREEZE_END_DATE > :now ");
        sql.append(" and bf.is_unfreeze = :isUnfreeze ");
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        params.put("freezeStatus", new Integer[]{BtBankAccConstant.FREEZE_STATUS_0, BtBankAccConstant.FREEZE_STATUS_1});
        // 使用时间戳参数，避免 MySQL/Oracle 对字符串日期的隐式转换。
        params.put("now", new Timestamp(now.getTime()));
        params.put("isUnfreeze", CommonConstant.NO);
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccPlanFreezeVO.class);
    }

    /**
     * 查询符合自动解冻条件的记录
     *
     * @param now 当前时间
     * @return 计划解冻申请集合
     */
    public List<BtBankAccPlanFreezeVO> listAutoUnfreezeRecord(Date now) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from BT_BANK_ACC_PLAN_FREEZE bf ");
        sql.append(" where bf.STATUS = :status and bf.IS_UNFREEZE = :isUnfreeze and bf.FREEZE_STATUS in (:freezeStatus) and bf.FREEZE_END_DATE <= :now ");
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        params.put("isUnfreeze", CommonConstant.NO);
        params.put("freezeStatus", new Integer[]{BtBankAccConstant.FREEZE_STATUS_1, BtBankAccConstant.FREEZE_STATUS_2});
        // 使用时间戳参数，避免 MySQL/Oracle 对字符串日期的隐式转换。
        params.put("now", new Timestamp(now.getTime()));
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccPlanFreezeVO.class);
    }

    /**
     * 查询符合全部冻结成功的记录
     *
     * @return 全部冻结成功记录集合
     */
    public List<BtBankAccPlanFreezeVO> listAllFreezeSuccessRecord() {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" select bf.ID as id, bf.PLAN_FREEZE_NO as planFreezeNo, bf.BANK_ACC as bankAcc, acc.BANK_NAME as bankName, bf.CORP_ID as corpId, sc.NAME as corpName, ");
        sql.append(" bt_nvl_num(bf.APPLY_FREEZE_MONEY, 0) as applyFreezeMoney, bt_nvl_num(bf.REAL_FREEZE_MONEY, 0) as realFreezeMoney, bf.CREATE_BY as createBy ");
        sql.append(" from BT_BANK_ACC_PLAN_FREEZE bf ");
        sql.append(" left join BT_BANK_ACC acc on bf.BANK_ACC_ID = acc.ID ");
        sql.append(" left join SYS_CORP sc on bf.CORP_ID = sc.ID ");
        sql.append(" where bf.STATUS = :status and bf.FREEZE_STATUS = :freezeStatus and bf.APPLY_FREEZE_MONEY = bf.REAL_FREEZE_MONEY ");
        sql.append(" and bf.IS_FREEZE_COMPLETE = :isFreezeComplete and bf.IS_UNFREEZE = :isUnfreeze ");
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        params.put("freezeStatus", BtBankAccConstant.FREEZE_STATUS_2);
        params.put("isFreezeComplete", CommonConstant.NO);
        params.put("isUnfreeze", CommonConstant.NO);
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccPlanFreezeVO.class);
    }

    /**
     * 查询符合部分冻结成功的记录
     *
     * @return 全部冻结成功记录集合
     */
    public List<BtBankAccPlanFreezeVO> listPartFreezeSuccessRecord() {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" select bf.ID as id, bf.PLAN_FREEZE_NO as planFreezeNo, bf.BANK_ACC as bankAcc, acc.BANK_NAME as bankName, bf.CORP_ID as corpId, sc.NAME as corpName, ");
        sql.append(" bt_nvl_num(bf.APPLY_FREEZE_MONEY, 0) as applyFreezeMoney, bt_nvl_num(bf.REAL_FREEZE_MONEY, 0) as realFreezeMoney, bf.CREATE_BY as createBy ");
        sql.append(" from BT_BANK_ACC_PLAN_FREEZE bf ");
        sql.append(" left join BT_BANK_ACC acc on bf.BANK_ACC_ID = acc.ID ");
        sql.append(" left join SYS_CORP sc on bf.CORP_ID = sc.ID ");
        sql.append(" where bf.STATUS = :status and bf.FREEZE_STATUS = :freezeStatus and bf.APPLY_FREEZE_MONEY > bf.REAL_FREEZE_MONEY and bf.IS_UNFREEZE = :isUnfreeze ");
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        params.put("freezeStatus", BtBankAccConstant.FREEZE_STATUS_1);
        params.put("isUnfreeze", CommonConstant.NO);
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccPlanFreezeVO.class);
    }

    /**
     * 查询符合无冻结金额成功的记录
     *
     * @return 全部冻结成功记录集合
     */
    public List<BtBankAccPlanFreezeVO> listNoFreezeSuccessRecord(Date now) {
        Map<String, Object> params = Maps.newHashMap();

        StringBuilder sql = new StringBuilder();
        sql.append(" select bf.ID as id, bf.PLAN_FREEZE_NO as planFreezeNo, bf.BANK_ACC as bankAcc, acc.BANK_NAME as bankName, bf.CORP_ID as corpId, sc.NAME as corpName, ");
        sql.append(" bt_nvl_num(bf.APPLY_FREEZE_MONEY, 0) as applyFreezeMoney, bt_nvl_num(bf.REAL_FREEZE_MONEY, 0) as realFreezeMoney, bf.CREATE_BY as createBy ");
        sql.append(" from BT_BANK_ACC_PLAN_FREEZE bf ");
        sql.append(" left join BT_BANK_ACC acc on bf.BANK_ACC_ID = acc.ID ");
        sql.append(" left join SYS_CORP sc on bf.CORP_ID = sc.ID ");
        sql.append(" where bf.STATUS = :status and bf.FREEZE_STATUS = :freezeStatus and bf.REAL_FREEZE_MONEY = 0 and bf.IS_UNFREEZE = :isUnfreeze ");
        sql.append(" and bf.FREEZE_START_DATE <= :now and bf.FREEZE_END_DATE >= :now ");
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        params.put("freezeStatus", BtBankAccConstant.FREEZE_STATUS_0);
        params.put("isUnfreeze", CommonConstant.NO);
        // 使用时间戳参数，避免 MySQL/Oracle 对字符串日期的隐式转换。
        params.put("now", new Timestamp(now.getTime()));
        return this.listBySQLAliasToBean(sql.toString(), params, BtBankAccPlanFreezeVO.class);
    }

    /**
     * 银行账户计划冻结申请查询列表
     *
     * @return 银行账户计划冻结数据
     */
    public DataTablesOutput getQueryDataTablesUnion(DataTablesInput dataTablesInput, String authCorpSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select bf.ID as id, bf.PLAN_FREEZE_NO as planFreezeNo, bf.BANK_ACC as bankAcc, acc.ACC_NAME as bankAccName, acc.BANK_NAME as bankName, acc.IS_ONLINE as isOnline, ");
        sql.append(" bf.CORP_ID as corpId, sc.NAME as corpName, bf.FREEZE_START_DATE as freezeStartDate, bf.FREEZE_END_DATE as freezeEndDate, ");
        sql.append(" bt_nvl_num(bf.APPLY_FREEZE_MONEY, 0) as applyFreezeMoney, bt_nvl_num(bf.REAL_FREEZE_MONEY, 0) as realFreezeMoney, ");
        sql.append(" bf.STATUS as status, bf.FREEZE_STATUS as freezeStatus, bf.IS_FREEZE_COMPLETE as isFreezeComplete, bf.IS_UNFREEZE as isUnfreeze, ");
        sql.append(" bf.FREEZE_RESSION as freezeRession, bf.CREATE_BY as createBy, bf.CREATE_DATE as createDate, su.LOGIN_NAME as loginName ");
        sql.append(" from BT_BANK_ACC_PLAN_FREEZE bf ");
        sql.append(" left join BT_BANK_ACC acc on bf.BANK_ACC_ID = acc.ID ");
        sql.append(" left join SYS_CORP sc on bf.CORP_ID = sc.ID ");
        sql.append(" left join SYS_USER su on bf.CREATE_BY = su.ID ");
        sql.append(" where acc.status = ").append(BtBankAccConstant.STATUE_CHECK).append(" and acc.VALID_SIGN = ").append(BtBankAccConstant.VALIDSIGN_1);
        if (StringUtils.isNotEmpty(authCorpSql)) {
            sql.append(" and ").append(authCorpSql);
        }
        if (EmptyUtils.isEmptyList(dataTablesInput.getOrder())) {
            dataTablesInput.setOrderString(new OrderString("bf.CREATE_DATE", "desc"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccPlanFreezeVO.class, "", false);
    }
}
