package com.bytter.basic.dao;
import com.bytter.basic.entity.BtCurrencyPair;
import com.bytter.basic.vo.BtCurrencyPairVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 货币对管理
 * @date 2020-1-19 11:14:22
 */
@Repository
public class BtCurrencyPairDao extends BaseDao<BtCurrencyPair, String> {
    /**
     * 查询货币对列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id as id,t.cur_id_1 as curId1,t.cur_id_2 as curId2,t.base_cur_id as baseCurId,t.exchange_rate as exchangeRate,");
        sql.append(" c1.english_code as englishCode1,c2.english_code as englishCode2,c.english_code as baseEnglishCode,");
        sql.append(" c1.cur_name as curName1,c2.cur_name as curName2,c.cur_name as baseCurName,");
        sql.append(" t.valid_sign as validSign,t.create_by as createBy,t.create_date as createDate,t.update_by as updateBy,t.update_date as updateDate ");
        sql.append(" from bt_currency_pair t left join bt_currency c1 on t.cur_id_1=c1.id");
        sql.append(" left join bt_currency c2 on t.cur_id_2=c2.id");
        sql.append(" left join bt_currency c on t.base_cur_id=c.id where 1 = 1");
        return this.getDynamicBySQL(input, sql.toString(), BtCurrencyPairVO.class, "t", false);
    }
    /**
     * 根据ID查找货币对
     *
     * @param id
     * @return
     */
    public BtCurrencyPairVO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id as id,t.cur_id_1 as curId1,t.cur_id_2 as curId2,t.base_cur_id as baseCurId,t.exchange_rate as exchangeRate,");
        sql.append(" c1.english_code as englishCode1,c2.english_code as englishCode2,c.english_code as baseEnglishCode,");
        sql.append(" c1.cur_name as curName1,c2.cur_name as curName2,c.cur_name as baseCurName,");
        sql.append(" t.valid_sign as validSign,t.create_by as createBy,t.create_date as createDate,t.update_by as updateBy,t.update_date as updateDate ");
        sql.append(" from bt_currency_pair t left join bt_currency c1 on t.cur_id_1=c1.id");
        sql.append(" left join bt_currency c2 on t.cur_id_2=c2.id");
        sql.append(" left join bt_currency c on t.base_cur_id=c.id");
        sql.append(" where t.id = :id ");
        HashMap<String, String> params = Maps.newHashMap();
        params.put("id", id);
        return this.getBySQLAliasToBean(sql.toString(), params, BtCurrencyPairVO.class);
    }
    /**
     * 货币对配对
     *
     * @param params
     * @return
     */
    public BtCurrencyPairVO getByCurId(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select  c.id, c.cur_id_1 as curId1, c.cur_id_2 as curId2, c.base_cur_id, c.exchange_rate, c.valid_sign ");
        sql.append(" from bt_currency_pair c ");
        sql.append(" where ((CUR_ID_1 = :curId1 and CUR_ID_2 = :curId2) or (CUR_ID_1 = :curId2 and CUR_ID_2 = :curId1))");
        return getBySQLAliasToBean(sql.toString(), params, BtCurrencyPairVO.class);
    }

    /**
     *
     * 查询所有有效货币对
     *
     * @return
     */
    public List<BtCurrencyPairVO> listCurrencyPair() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id as id,t.cur_id_1 as curId1,t.cur_id_2 as curId2,t.base_cur_id as baseCurId,t.exchange_rate as exchangeRate,");
        sql.append(" c1.english_code as englishCode1,c2.english_code as englishCode2,c.english_code as baseEnglishCode,");
        sql.append(" c1.cur_name as curName1,c2.cur_name as curName2,c.cur_name as baseCurName,t.valid_sign as validSign");
        sql.append(" from bt_currency_pair t left join bt_currency c1 on t.cur_id_1=c1.id");
        sql.append(" left join bt_currency c2 on t.cur_id_2=c2.id");
        sql.append(" left join bt_currency c on t.base_cur_id=c.id");
        sql.append(" where t.valid_sign = :validSign ");
        HashMap<String, String> params = Maps.newHashMap();
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        return this.listBySQLAliasToBean(sql.toString(), params, BtCurrencyPairVO.class);
    }



    /**
     * 获取有效的货币对
     * @return
     */
    public List<BtCurrencyPairVO> getValidCurrencyPair() {
        Map<String,Object> params = new HashMap<>();
        params.put("validSign", "1");
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id as id,t.cur_id_1 as curId1,t.cur_id_2 as curId2,t.base_cur_id as baseCurId,t.exchange_rate as exchangeRate,");
        sql.append(" c1.english_code as englishCode1,c2.english_code as englishCode2,c.english_code as baseEnglishCode,");
        sql.append(" c1.cur_name as curName1,c2.cur_name as curName2,c.cur_name as baseCurName,");
        sql.append(" t.valid_sign as validSign,t.create_by as createBy,t.create_date as createDate,t.update_by as updateBy,t.update_date as updateDate ");
        sql.append(" from bt_currency_pair t left join bt_currency c1 on t.cur_id_1=c1.id");
        sql.append(" left join bt_currency c2 on t.cur_id_2=c2.id");
        sql.append(" left join bt_currency c on t.base_cur_id=c.id");
        sql.append(" where t.valid_sign =:validSign");
        return listBySQLAliasToBean(sql.toString(), params, BtCurrencyPairVO.class);
    }
}
