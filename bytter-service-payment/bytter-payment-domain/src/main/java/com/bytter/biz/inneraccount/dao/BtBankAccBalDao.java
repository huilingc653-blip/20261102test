package com.bytter.biz.inneraccount.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.inneraccount.entity.BtBankAccBal;
import com.bytter.biz.inneraccount.vo.BtBankAccBalVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Maps;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: BT_BANK_ACC_BAL
 * @date 2022-05-25 17:50:16
 */
@Repository
public class BtBankAccBalDao extends BaseDao<BtBankAccBal, String> {
    /**
     * 全量获取银行账号的最新余额
     * @return
     */
    public List<BtBankAccBal> getBankAccBalInit(String joinSql){
        StringBuilder sql = new StringBuilder();
        /*sql.append("select b.bank_acc as bankAcc, bc.cur_id as curId,bal.bal_date as balDate,bal.get_date as getDate,");
        sql.append(" bt_nvl_num(bal.bal, 0) as bal,bt_nvl_num(bal.avail_bal, 0) as availBal,bt_nvl_num(bal.frz_bal, 0) as frzBal");
        sql.append(" from bt_bank_acc b left join bt_bank_acc_cur bc on b.id = bc.bank_acc_id");
        sql.append(" left join bis_acc_bal bal on bal.bank_acc = b.bank_acc and bal.cur_id=bc.cur_id");
        sql.append(" where (bal.status = 95 or bal.status is null or bal.status = '')");
        sql.append(" and bal.bal_date = (select max(bal_date) from bis_acc_bal bal1 where bal.bank_acc = bal1.bank_acc");
        sql.append(" and bal1.cur_id = bal.cur_id and (bal1.status = 95 or bal1.status is null or bal1.status = '') and bal1.bal is not null)");
        if(StringUtils.isNotBlank(joinSql)){
            sql.append(" and ").append(joinSql);
        }
        sql.append(" order by b.bank_Acc,bal.bal_Date");*/

        // sql.append("SELECT b.bank_acc AS bankAcc,bc.cur_id AS curId,bal.bal_date AS balDate,bal.get_date AS getDate, ");
        // sql.append("bt_nvl_num(bal.bal, 0) AS bal,bt_nvl_num(bal.avail_bal, 0) AS availBal,bt_nvl_num(bal.frz_bal, 0) AS frzBal ");
        // sql.append("FROM bt_bank_acc b ");
        // sql.append("LEFT JOIN bt_bank_acc_cur bc ON b.id = bc.bank_acc_id ");
        // sql.append("JOIN ( SELECT *,ROW_NUMBER() OVER(PARTITION BY bank_acc, cur_id ORDER BY bal_date DESC) AS rn ");
        // sql.append("        FROM bis_acc_bal ");
        // sql.append("        WHERE (status = 95 OR status IS NULL OR status = '') AND bal IS NOT NULL ");
        // sql.append(") bal ON bal.bank_acc = b.bank_acc AND bal.cur_id = bc.cur_id AND bal.rn = 1 ");
        // sql.append("WHERE 1=1 ");

        sql.append("SELECT b.bank_acc AS bankAcc,bc.cur_id AS curId,bal.bal_date AS balDate,bal.get_date AS getDate, ");
        sql.append("bt_nvl_num(bal.bal, 0) AS bal,bt_nvl_num(bal.avail_bal, 0) AS availBal,bt_nvl_num(bal.frz_bal, 0) AS frzBal ");
        sql.append("FROM bt_bank_acc b ");
        sql.append("LEFT JOIN bt_bank_acc_cur bc ON b.id = bc.bank_acc_id ");
        sql.append("JOIN bis_acc_bal bal ON bal.bank_acc = b.bank_acc AND bal.cur_id = bc.cur_id ");
        sql.append("WHERE (bal.status = 95 OR bal.status IS NULL OR bal.status = '') ");
        sql.append("AND bal.bal IS NOT NULL ");
        sql.append("AND NOT EXISTS ( " +
                "      SELECT 1 FROM bis_acc_bal bal2 WHERE bal2.bank_acc = bal.bank_acc " +
                "        AND bal2.cur_id = bal.cur_id " +
                "        AND (bal2.status = 95 OR bal2.status IS NULL OR bal2.status = '') " +
                "        AND bal2.bal IS NOT NULL " +
                "        AND ( bal2.bal_date > bal.bal_date OR (bal2.bal_date = bal.bal_date AND bal2.get_date  > bal.get_date )) " +
                "  ) ");
        if(StringUtils.isNotBlank(joinSql)){
            sql.append(" and ").append(joinSql);
        }
        sql.append(" ORDER BY b.bank_Acc, bal.bal_Date ");

        return this.listBySQLAliasToBean(sql.toString(), null, BtBankAccBal.class);
    }
    /**
     * 获取未初始化的账号
     * @return
     */
    public List<String> getBankAccInit(){
        StringBuilder sql = new StringBuilder();
        sql.append("select b.bank_acc from bt_bank_acc b where b.bank_acc not in (select bank_acc from bt_bank_acc_bal)");
        Query query = createSQLQuery(sql.toString());
        query.setFetchSize(5000);
        List<String> list = query.list();
        return list;
    }
    /**
     * 全量获取银行账号的最新余额
     * @return
     */
    public List<BtBankAccBalVO> getBankAccBalMax(){
        StringBuilder sql = new StringBuilder();
        /*sql.append("select b.id,b.bank_acc as bankAcc, b.cur_id as curId,bal.bal_date as balDate,bal.get_date as getDate,");
        sql.append(" bt_nvl_num(bal.bal, 0) as bal,bt_nvl_num(bal.avail_bal, 0) as availBal,bt_nvl_num(bal.frz_bal, 0) as frzBal");
        sql.append(" from bt_bank_acc_bal b left join bis_acc_bal bal on bal.bank_acc = b.bank_acc and bal.cur_id=b.cur_id");
        sql.append(" where (bal.status = 95 or bal.status is null or bal.status = '')");
        sql.append(" and bt_to_char(bal.get_date,'YYYY-MM-DD HH24:MI:SS') > to_char(b.get_date,'YYYY-MM-DD HH24:MI:SS')");
        sql.append(" and bal.bal_date = (select max(bal_date) from bis_acc_bal bal1 where bal.bank_acc = bal1.bank_acc");
        sql.append(" and bal1.cur_id = bal.cur_id and (bal1.status = 95 or bal1.status is null or bal1.status = '') and bal1.bal is not null");
        sql.append(" and bt_to_char(bal.get_date,'YYYY-MM-DD HH24:MI:SS') > to_char(b.get_date,'YYYY-MM-DD HH24:MI:SS'))");
        sql.append(" order by b.bank_Acc,bal.bal_Date");*/


        sql.append(" SELECT b.id,b.bank_acc AS bankAcc,b.cur_id AS curId,lb.bal_date AS balDate,lb.get_date AS getDate,");
        sql.append(" bt_nvl_num(lb.bal, 0) AS bal,bt_nvl_num(lb.avail_bal, 0) AS availBal,bt_nvl_num(lb.frz_bal, 0) AS frzBal ");
        sql.append(" FROM bt_bank_acc_bal b  ");
        sql.append(" LEFT JOIN (  SELECT bal1.*,");
        sql.append("                ROW_NUMBER() OVER (PARTITION BY bal1.bank_acc, bal1.cur_id ORDER BY bal1.bal_date DESC) AS rn ");
        sql.append("              FROM bis_acc_bal bal1 ");
        sql.append("              WHERE (bal1.status = 95 OR bal1.status IS NULL OR bal1.status = '') AND bal1.bal IS NOT NULL ");
        sql.append(" ) lb ON lb.bank_acc = b.bank_acc AND lb.cur_id = b.cur_id AND lb.rn = 1 ");
        sql.append(" WHERE lb.get_date > b.get_date ");
        sql.append(" ORDER BY b.bank_acc, lb.bal_date ");

        return this.listBySQLAliasToBean(sql.toString(), null, BtBankAccBalVO.class);
    }

    public Map<String, BigDecimal> getBalByAccAndCur(String bankAcc, String cur){
        Map<String, BigDecimal> result = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append("select BANK_ACC as bankAcc, CUR_ID as curId, BAL as bal, AVAIL_BAL as availBal , FRZ_BAL as frzBal ");
        sql.append(" from  BT_BANK_ACC_BAL  ");
        sql.append(" where  1 = 1 ");
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(bankAcc)){
            sql.append(" and BANK_ACC = :bankAcc");
            params.put("bankAcc", bankAcc);
        }
        if (StringUtils.isNotBlank(cur)){
            sql.append(" and CUR_ID = :cur");
            params.put("cur", cur);
        }
        List<BtBankAccBalVO> btBankAccBalVOS = this.listBySQLAliasToBean(sql.toString(), params, BtBankAccBalVO.class);
        if (CollectionUtil.isNotEmpty(btBankAccBalVOS)){
            BtBankAccBalVO btBankAccBalVO = btBankAccBalVOS.get(0);
            result.put(btBankAccBalVO.getBankAcc(), btBankAccBalVO.getBal())  ;
        }

        return result;
    }

    public Integer selectNotInitBalCount(String bankAcc, String accCurId) {
        String countSql = "select count(*) from bis_acc_bal where bank_acc = :bankAcc and cur_id = :accCurId and init_sign != '1' ";
        Map<String, Object> params = new HashMap<>();
        params.put("bankAcc", bankAcc);
        params.put("accCurId", accCurId);
        Object result = createSQLQuery(countSql, params).uniqueResult();
        if (result == null) {
            return 0;
        }
        return ((Number) result).intValue();
    }
}
