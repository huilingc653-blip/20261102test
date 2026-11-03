package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.dto.BtBankAccCurDTO;
import com.bytter.system.api.dto.FcCheckInitDTO;
import com.bytter.system.entity.BtBankAccCur;
import com.bytter.system.vo.FcCheckInitVo;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: BT_BANK_ACC_CUR
 * @date 2017-06-02 18:03:22
 */
@Repository
public class BtBankAccCurDao extends BaseDao<BtBankAccCur, String> {
    /**
     * 查询中心对账余额初始化
     *
     * @param dataTablesInput
     * @param corpAuth
     * @param bankAcc
     * @return
     */
    public DataTablesOutput getFcCheckInit(DataTablesInput dataTablesInput, List<String> corpAuth, List<String> bankAcc) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select c.bank_acc as bankAcc,c.acc_name as bankAccName, c.bank_name as bankName,d.blnctr_acc as account,d.acc_name as accName, ");
        sb.append(" a.cur_id as curId ");
        sb.append(" from bt_bank_acc_cur a ");
        sb.append(" inner join bt_bank_acc c on c.ID = a.bank_acc_id ");
        sb.append(" inner join fc_acc d on a.counter_acc = d.blnctr_acc and d.acc_type in ('01','02') ");
        sb.append(" and ").append(SqlUtil.getNotinSql(bankAcc, "c.bank_acc", true));////已对账则不能再调整初始化
        sb.append(" and ").append(getSQLIn(corpAuth, 900, "d.net_id"));
        DataTablesOutput out = getDynamicBySQL(dataTablesInput, sb.toString(), FcCheckInitVo.class, null, false);
        return out;
    }
    /**
     * 查询ERP对账余额初始化
     *
     * @param dataTablesInput
     * @param corpAuth
     * @param bankAcc
     * @return
     */
    public DataTablesOutput getFcCheckInitErp(DataTablesInput dataTablesInput, List<String> corpAuth, List<String> bankAcc) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select c.bank_acc as bankAcc,c.acc_name as accName, c.bank_name as bankName,bc.cur_name as curName, ");
        sb.append(" a.cur_id as curId ");
        sb.append(" from bt_bank_acc c  ");
        sb.append(" inner join bt_bank_acc_cur a on c.ID = a.bank_acc_id ");
        sb.append(" left join bt_currency bc on a.cur_id = bc.id ");
        sb.append(" where c.valid_sign = '1' ");
        sb.append(" and ").append(SqlUtil.getInSql(bankAcc, "c.bank_acc", true));
        if (EmptyUtils.isNotEmpty(corpAuth)) {
            sb.append(" and ").append(getSQLIn(corpAuth, 900, "c.corp_id"));
        }
        DataTablesOutput out = getDynamicBySQL(dataTablesInput, sb.toString(), FcCheckInitVo.class, null, false);
        return out;
    }
    /***
     * 查询中心对账余额初始化信息
     */
    public FcCheckInitDTO FcCheckInitList(String bankAcc, String account) {
        Map<String, Object> val = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer();
        sb.append(" select c.bank_acc as bankacc,c.acc_name as bankaccname, c.bank_name as bankname,d.blnctr_acc as account,d.acc_name as accname, ");
        sb.append(" a.cur_id as curid,b.check_date as checkdate ,b.bln_balance as blnbalance,b.bank_balance as bankbalance ,b.id as id ,b.rmk as rmk ");
        sb.append(" from bt_bank_acc_cur a ");
        sb.append(" inner join bt_bank_acc c on c.id = a.bank_acc_id ");
        sb.append(" left join fc_check_init b on b.bank_acc = c.bank_acc ");
        sb.append(" inner join fc_acc d on a.counter_acc = d.blnctr_acc ");
        sb.append(" where 1=1 and b.check_type = '1' ");
        sb.append(" and c.bank_acc = :bankacc ");
        val.put("bankacc", bankAcc);
        sb.append(" and d.blnctr_acc = :account ");
        val.put("account", account);
        return getBySQLAliasToBean(sb.toString(), val, FcCheckInitDTO.class);
    }
    /**
     * 根据币种ID获取银行账号ID集合
     *
     * @param curId
     * @return
     */
    public List<String> getBankAccIdsByCurId(String curId) {
        StringBuffer sql = new StringBuffer("select bank_acc_id from bt_bank_acc_cur where cur_id = :curId");
        Map<String, Object> map = new HashMap<>();
        map.put("curId", curId);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), map);
        return sqlQuery.list();
    }

    /**
     * 根据Id得到实体账号
     */
    public BtBankAccCurDTO getBankAccCurByBankAccId(String bankAccId, String curId) {
        BtBankAccCurDTO  dto = new BtBankAccCurDTO();
        String sql = "select * from BT_BANK_ACC_CUR where BANK_ACC_ID = :bankAccId and CUR_ID = :curId";
        Map<String, String> param = new HashMap<String, String>();
        param.put("bankAccId", bankAccId);
        param.put("curId", curId);
        BtBankAccCur accCur = getBySQLAliasToBean(sql, param, BtBankAccCur.class);
        if (accCur != null){
            BeanUtils.copyNotNullProperties(accCur, dto);
        }
        return dto;
    }

    public List<BtBankAccCurDTO> getBankAccCurByBankAccId(String bankAccId) {
        List<BtBankAccCurDTO> list = new ArrayList<>();
        String sql = "select * from BT_BANK_ACC_CUR where BANK_ACC_ID = :bankAccId";
        Map<String, String> param = new HashMap<String, String>();
        param.put("bankAccId", bankAccId);
        List<BtBankAccCur> accCurList = listBySQLAliasToBean(sql,param,BtBankAccCur.class);
        if (accCurList != null){
            for (BtBankAccCur cur : accCurList){
                BtBankAccCurDTO dto = new BtBankAccCurDTO();
                BeanUtils.copyNotNullProperties(cur, dto);
                list.add(dto);
            }
        }
        return list;
    }
}
