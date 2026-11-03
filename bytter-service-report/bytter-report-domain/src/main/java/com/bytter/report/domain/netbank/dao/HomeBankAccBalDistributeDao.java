package com.bytter.report.domain.netbank.dao;
import com.bytter.report.api.vo.HomeBankAccBalDistributeSearchVO;
import com.bytter.report.domain.netbank.vo.HomeBankAccBalDistributeVO;
import com.bytter.report.domain.netbank.vo.HomeBankAccBalVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * 银行账户余额分布Dao
 * @author huangx 2022-03-28
 */
@Repository
public class HomeBankAccBalDistributeDao extends BaseDao<HomeBankAccBalDistributeVO, String> {
    /**
     * 获取账户余额（账户最新余额）
     * @param joinSql
     * @return
     */
    public List<HomeBankAccBalVO> getBankAccBal(String joinSql){
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.bank_acc as bankAcc, cur.cur_id as curId, bal.bal_date as balDate, acc.corp_id as corpId, acc.bank_type_id as bankTypeId,");
        sql.append(" bt_nvl_num(bal.bal,0) as bal, bt_nvl_num(bal.avail_bal,0) as availBal, bt_nvl_num(bal.frz_bal,0) as frzBal");
        sql.append(" from bt_bank_acc acc");
        sql.append(" left join bt_bank_acc_cur cur on acc.id=cur.bank_acc_id");
        sql.append(" left join bt_bank_acc_bal bal on bal.bank_acc = acc.bank_acc and cur.cur_id=bal.cur_id");
        sql.append(" where acc.valid_sign= :validSign and acc.status<> :status");
        sql.append(" and bal.bal_date is not null");
        sql.append(joinSql);
        sql.append(" order by bal.bal desc");
        HashMap<String, Object> params = new HashMap<>();
        params.put("validSign", CommonConstant.YES);
        params.put("status", CommonConstant.STATUE_DELETE);
        return this.listBySQLAliasToBean(sql.toString(), params, HomeBankAccBalVO.class);
    }
    /**
     * 获取账户余额
     * @param joinSql
     * @param searchVO
     * @return
     */
    public List<HomeBankAccBalVO> getBankAccBalByBalDate(String joinSql, HomeBankAccBalDistributeSearchVO searchVO){
        StringBuilder sql = new StringBuilder();
        sql.append(" select bal.cur_id as curId, bal.bal_date as balDate, bt_nvl_num(bal.bal,0) as bal");
        sql.append(" from bt_bank_acc acc left join bis_acc_bal bal on bal.bank_acc = acc.bank_acc");
        sql.append(" where (bal.status = 95 or bal.status is null or bal.status = '')");
        sql.append(" and acc.valid_sign= :validSign and acc.status<> :status");
        sql.append(" and (bal.bal_date is null or bal.bal_date>=:searchBeginDate and bal.bal_date<=:searchEndDate)");
        sql.append(joinSql);
        HashMap<String, Object> params = new HashMap<>();
        params.put("validSign", CommonConstant.YES);
        params.put("status", CommonConstant.STATUE_DELETE);
        params.put("searchBeginDate", DateUtils.parse(DateUtils.format(searchVO.getSearchBeginDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        params.put("searchEndDate", DateUtils.parse(DateUtils.format(searchVO.getSearchEndDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        sql.append(" order by bal.bal_date desc");
        return this.listBySQLAliasToBean(sql.toString(), params, HomeBankAccBalVO.class);
    }
    /**
     * 获取账户余额（按银行类别分组）
     * @param joinSql
     * @return
     */
    public List<HomeBankAccBalVO> getBankAccBalGroupByBankType(String joinSql){
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.bankTypeId,t.bal from (select acc.bank_type_id as bankTypeId, sum(bt_nvl_num(bal.bal,0)) as bal");
        sql.append(" from bt_bank_acc acc left join bt_bank_acc_bal bal on bal.bank_acc = acc.bank_acc");
        sql.append(" where acc.valid_sign= :validSign and acc.status<> :status");
        sql.append(joinSql);
        sql.append(" group by acc.bank_type_id) t");
        sql.append(" order by t.bal desc");
        HashMap<String, Object> params = new HashMap<>();
        params.put("validSign", CommonConstant.YES);
        params.put("status", CommonConstant.STATUE_DELETE);
        return this.listBySQLAliasToBean(sql.toString(), params, HomeBankAccBalVO.class);
    }
    /**
     * 获取账户余额（按账户属性分组）
     * @param joinSql
     * @return
     */
    public List<HomeBankAccBalVO> getBankAccBalGroupByAttribute(String joinSql){
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.attributeId,t.bal from (select acc.attribute_id as attributeId, sum(bt_nvl_num(bal.bal,0)) as bal");
        sql.append(" from bt_bank_acc acc left join bt_bank_acc_bal bal on bal.bank_acc = acc.bank_acc");
        sql.append(" where acc.valid_sign= :validSign and acc.status<> :status");
        sql.append(joinSql);
        sql.append(" group by acc.attribute_id) t");
        sql.append(" order by t.bal desc");
        HashMap<String, Object> params = new HashMap<>();
        params.put("validSign", CommonConstant.YES);
        params.put("status", CommonConstant.STATUE_DELETE);
        return this.listBySQLAliasToBean(sql.toString(), params, HomeBankAccBalVO.class);
    }
    /**
     * 获取账户余额（按账户性质分组）
     * @param joinSql
     * @return
     */
    public List<HomeBankAccBalVO> getBankAccBalGroupByNature(String joinSql){
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.natureId,t.bal from (select acc.nature_id as natureId, sum(bt_nvl_num(bal.bal,0)) as bal");
        sql.append(" from bt_bank_acc acc left join bt_bank_acc_bal bal on bal.bank_acc = acc.bank_acc");
        sql.append(" where acc.valid_sign= :validSign and acc.status<> :status");
        sql.append(joinSql);
        sql.append(" group by acc.nature_id) t");
        sql.append(" order by t.bal desc");
        HashMap<String, Object> params = new HashMap<>();
        params.put("validSign", CommonConstant.YES);
        params.put("status", CommonConstant.STATUE_DELETE);
        return this.listBySQLAliasToBean(sql.toString(), params, HomeBankAccBalVO.class);
    }
}
