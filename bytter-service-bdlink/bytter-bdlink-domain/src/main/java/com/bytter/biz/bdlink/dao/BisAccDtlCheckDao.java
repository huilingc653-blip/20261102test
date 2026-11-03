package com.bytter.biz.bdlink.dao;

import com.bytter.biz.bdlink.entity.BisAccDtlCheck;
import com.bytter.biz.bdlink.vo.BisAccDtlCheckVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.vo.BtBankAccVO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 余额流水异常核查表
 * @date 2023-02-15 11:20:30
 */
@Repository
public class BisAccDtlCheckDao extends BaseDao<BisAccDtlCheck, String> {

    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BisAccDtlCheckVO> getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.corp_Id as corpId,t.bank_acc as bankAcc,t.cur_id as curId, acc.acc_name as accName, acc.bank_name as bankName,");
        sql.append(" t.check_Date as checkDate,t.bal,t.check_Bal as checkBal,t.status,t.create_date as createDate");
        sql.append(" from bis_acc_dtl_check t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" where acc.status <> -2 and acc.valid_sign = 1 ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlCheckVO.class, "t", false);
    }

    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BtBankAccVO> queryBankAccDatatable(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId, ");
        sql.append(" acc.reg_date as regDate,acc.valid_sign as validSign, acc.is_check as isCheck,");
        sql.append(" acc.acc_type as accType,acc.is_online as isOnline, acccur.cur_id as curId, ");
        sql.append(" banktype.type_name as bankTypeName,acc.bank_name as bankName, acc.status as status ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acccur.bank_acc_id = acc.id ");
        sql.append(" where acc.valid_sign = '1' and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "", false);
    }

    /**
     * 获取核查账户
     * @return
     */
    public List<BtBankAccVO> getCheckBankAcc() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.corp_id as corpId,acc.bank_acc as bankAcc,cur.cur_id as curId from bt_bank_acc acc");
        sql.append(" left join bt_bank_acc_cur cur on cur.bank_acc_id=acc.id");
        sql.append(" where acc.is_check='1' and acc.status <> -2 and acc.valid_sign = '1'");
        return this.listBySQLAliasToBean(sql.toString(), null,  BtBankAccVO.class);
    }

    /**
     * 查询核查信息
     * @param bankAcc
     * @param curId
     * @param checkDate
     * @return
     */
    public BisAccDtlCheck getBisAccDtlCheck(String bankAcc, String curId, Date checkDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.corp_Id as corpId,t.bank_acc as bankAcc,t.cur_id as curId, ");
        sql.append(" t.check_Date as checkDate,t.bal,t.check_Bal as checkBal,t.status,t.create_date as createDate");
        sql.append(" from bis_acc_dtl_check t where t.bank_acc=:bankAcc and t.cur_Id=:curId");
        sql.append(" and t.check_Date =:checkDate");
        Map<String,Object> params = new HashMap<>();
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);
        params.put("checkDate", checkDate);
        return this.getBySQLAliasToBean(sql.toString(), params,  BisAccDtlCheck.class);
    }
}
