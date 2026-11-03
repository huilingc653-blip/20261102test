package com.bytter.basic.dao;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.basic.entity.BtCurrency;
import com.bytter.basic.vo.BtCurrencyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.system.api.constant.BtCurrencyConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.google.common.collect.Maps;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 货币资料维护
 * @date 2017-05-23 11:14:22
 */
@Repository
public class BtCurrencyDao extends BaseDao<BtCurrency, String> {
    /**
     * 查询货币信息列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BtCurrencyVO> getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.cur_code,t.cur_name,t.english_code,t.cur_exrate,t.cur_sign,t.natural_sign,t.nonwork_day_rule as nonworkDayRule,t.nonwork_day as nonworkDay,");
        sql.append(" t.pb_scale_code,t.rmk,t.valid_sign,t.create_by,t.create_date,t.update_by,t.update_date,t.sys_cur_id as sysCurId,i.cur_name as sysCurName from bt_currency t ");
        sql.append(" left join bt_currency_init i on t.sys_cur_id=i.id where 1 = 1 ");
        return this.getDynamicBySQL(input, sql.toString(), BtCurrencyVO.class, "t", false);
    }
    /**
     * 根据id查找货币信息
     *
     * @param id
     * @return
     */
    public BtCurrencyVO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.cur_code,t.cur_name,t.english_code,t.cur_exrate,t.cur_sign,t.natural_sign,t.nonwork_day_rule as nonworkDayRule,t.nonwork_day as nonworkDay, ");
        sql.append(" t.pb_scale_code,t.rmk,t.valid_sign,t.create_by,t.create_date,t.update_by,t.update_date,t.is_system,t.sys_cur_id as sysCurId,i.cur_name as sysCurName from bt_currency t ");
        sql.append(" left join bt_currency_init i on t.sys_cur_id=i.id");
        sql.append(" where t.id = :id ");
        HashMap<String, String> params = Maps.newHashMap();
        params.put("id", id);
        return this.getBySQLAliasToBean(sql.toString(), params, BtCurrencyVO.class);
    }
    /**
     * 获取有效的货币资料信息
     *
     * @return
     */
    public List<BtCurrency> getBtCurrency() {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from BtCurrency where validSign = :validSign order by curCode ";
        params.put("validSign", BtCurrencyConstant.VALID_SIGN_YES);
        return listByHQL(hql, params, false);
    }
    /**
     * 获取有效的货币资料信息
     *
     * @return
     */
    public List<BtCurrencyVO> getBtCurrencyVO() {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from BtCurrency where validSign = :validSign order by curCode ";
        params.put("validSign", BtCurrencyConstant.VALID_SIGN_YES);
        return listByHQLAliasToBean(hql, params, BtCurrencyVO.class, false);
    }
    /**
     * 查询本位币
     *
     * @return
     */
    public BtCurrency getNatualCurrency() {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from BtCurrency where englishCode = :englishCode and validSign = :validSign ";
        params.put("englishCode", BtCurrencyConstant.CNY_ENGLISHCODE);
        params.put("validSign", BtCurrencyConstant.VALID_SIGN_YES);
        return this.getUnique(hql, params);
    }
    public BtCurrency getByIdCache(String currencyId) {
        String hql = "from BtCurrency where id = ?0";
        Query query = createQuery(hql, currencyId);
        query.setCacheable(true);
        return (BtCurrency) query.uniqueResult();
    }
    /**
     * 查询非人民币的货币信息
     *
     * @return
     */
    public List<BtCurrency> listByNotNatualCurrency() {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from BtCurrency where englishCode <> :englishCode and validSign = :validSign order by curCode ";
        params.put("englishCode", BtCurrencyConstant.CNY_ENGLISHCODE);
        params.put("validSign", BtCurrencyConstant.VALID_SIGN_YES);
        return this.listByHQL(hql, params);
    }
    /**
     * 获取除人民币以外的所有有效货币信息(无效的当前所属的币别)
     *
     * @param curId 币别id
     * @return
     */
    public List<BtCurrency> listByNotNatualCurrencyAndSelf(String curId) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from BtCurrency where englishCode <> :englishCode and (validSign = :validSign or id = :curId) order by curCode";
        params.put("englishCode", BtCurrencyConstant.CNY_ENGLISHCODE);
        params.put("validSign", BtCurrencyConstant.VALID_SIGN_YES);
        params.put("curId", curId);
        return this.listByHQL(hql, params);
    }
    /**
     * 根据币别名称或者英文简码获取ID
     *
     * @param eqMatchName 匹配字
     * @return id
     */
    public String getIdByNameOrEnglishCode(String eqMatchName) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from BtCurrency where englishCode = :eqMatchName or curName = :eqMatchName ";
        params.put("eqMatchName", eqMatchName);
        List<BtCurrency> list = this.listByHQL(hql, params);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0).getId();
    }
    /**
     * 获取币别跟其他业务的关联数量
     *
     * @param curId 币别ID
     * @return
     */
    public long checkAssociated(String curId) {
        long check = 0;
        Map<String, String> map = new HashMap<>();
        map.put("cur_Id", curId);
        map.put("delivery_cur_id", curId);
        map.put("buy_cur_id", curId);
        map.put("sell_cur_id", curId);
        map.put("bond_cur_id", curId);
        map.put("tran_cur_id1", curId);
        map.put("tran_cur_id2", curId);
        check = check + addSum("fex_tran_req", map);
        check = check + addSum("fex_tran_app_dtl", map);
        return check;
    }
    /**
     * 获取关联数量
     *
     * @param table 表名
     * @param map   列名/值
     * @return
     */
    private long addSum(String table, Map<String, String> map) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(id) from ");
        sql.append(table);
        sql.append(" WHERE ");
        for (String key : map.keySet()) {
            sql.append(key).append("=").append("'").append(map.get(key)).append("'").append(" OR ");
        }
        String substring = sql.toString().substring(0, sql.toString().length() - 3);
        SQLQuery query = getSession().createSQLQuery(substring);
        List list = query.list();
        //数据库兼容
        if (ListUtils.isNotNullOrEmpty(list)) {
            Object value = list.get(0);
            if (value instanceof BigDecimal) {
                return ((BigDecimal) value).intValue();
            }
            if (value instanceof BigInteger) {
                return ((BigInteger) value).intValue();
            }
        }
        return 0L;
    }
    /**
     * 根据币别名称或英文见吗获取币种
     *
     * @param search 查询条件
     * @return id
     */
    public List<BtCurrencyVO> getByCurNameOrEnglishCode(String search) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.cur_code,t.cur_name,t.english_code,t.cur_exrate,t.cur_sign,t.natural_sign,t.nonwork_day_rule as nonworkDayRule,t.nonwork_day as nonworkDay, ");
        sql.append(" t.pb_scale_code,t.rmk,t.valid_sign,t.create_by,t.create_date,t.update_by,t.update_date,t.is_system,t.sys_cur_id as sysCurId,i.cur_name as sysCurName from bt_currency t ");
        sql.append(" left join bt_currency_init i on t.sys_cur_id=i.id");
        sql.append(" where t.cur_name like :search or t.english_code like :search");
        HashMap<String, String> params = Maps.newHashMap();
        params.put("search", "%" + search + "%");
        return this.listBySQLAliasToBean(sql.toString(), params, BtCurrencyVO.class);
    }
    public List<BtCurrencyVO> listByIds(List<String> curIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.cur_code,t.cur_name,t.english_code,t.cur_exrate,t.cur_sign,t.natural_sign,t.nonwork_day_rule as nonworkDayRule,t.nonwork_day as nonworkDay, ");
        sql.append(" t.pb_scale_code,t.rmk,t.valid_sign,t.create_by,t.create_date,t.update_by,t.update_date,t.is_system,t.sys_cur_id as sysCurId,i.cur_name as sysCurName from bt_currency t ");
        sql.append(" left join bt_currency_init i on t.sys_cur_id=i.id");
        sql.append(" where 1=1 ");
        HashMap<String, Object> params = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(curIds)){
            curIds = curIds.stream().filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
            if (curIds.size() > 0){
                sql.append(" and t.id in (:curIds) ");
                params.put("curIds", curIds);
            }
        }
        return this.listBySQLAliasToBean(sql.toString(), params, BtCurrencyVO.class);
    }
    public List<BtCurrencyDTO> queryValidCur() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select bc.id as id, bc.cur_code as curCode from bt_currency bc ");
        sql.append(" where bc.valid_sign = '1' ");
        sql.append(" and exists (select 1 from fc_acc fa where fa.cur_id = bc.id) ");
        return listBySQLAliasToBean(sql.toString(), null, BtCurrencyDTO.class);
    }

    public List<BtCurrencyVO> listByNames(List<String> curNames) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.cur_code,t.cur_name,t.english_code,t.cur_exrate,t.cur_sign,t.natural_sign,t.nonwork_day_rule as nonworkDayRule,t.nonwork_day as nonworkDay, ");
        sql.append(" t.pb_scale_code,t.rmk,t.valid_sign,t.create_by,t.create_date,t.update_by,t.update_date,t.is_system,t.sys_cur_id as sysCurId,i.cur_name as sysCurName from bt_currency t ");
        sql.append(" left join bt_currency_init i on t.sys_cur_id=i.id");
        sql.append(" where 1=1 ");
        HashMap<String, Object> params = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(curNames)){
            curNames.removeIf(Objects::isNull);
            if (curNames.size() > 0){
                sql.append(" and t.CUR_NAME in (:curNames) ");
                params.put("curNames", curNames);
            }
        }
        return this.listBySQLAliasToBean(sql.toString(), params, BtCurrencyVO.class);
    }
}
