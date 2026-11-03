package com.bytter.system.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.constants.BaseDataConstant;
import com.bytter.system.entity.TbBaseRate;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 基准利率表
 * @author onlineGenerator
 * @date 2017-07-26 17:33:59
 * @version 1.0
 *
 */
@Repository
public class TbBaseRateDao extends BaseDao<TbBaseRate, String> {
    /**
     * 查询央行基准利率是否存在
     */
    public List<TbBaseRate> isBankBaseRateExist(String type) {
        return listBy("isBankBaseRate", type);
    }
    /**
     * 查询基准利率
     */
    public List<TbBaseRate> getTbBaseRateList(String id) {
        return listBy("id", id);
    }
    /**
     * 查询全部基准利率
     */
    public List<TbBaseRate> getListTbBaseRate() {
        return listAll();
    }
    /** 查询本位币利率类型 */
    public List<TbBaseRate> getBaseRateByType(String type) {
        String sql = null;
        if (StringUtils.equals(type,BaseDataConstant.IS_BANKBASERATE)) {
            sql = " select * from TB_BASE_RATE t where (t.is_bank_base_rate ='1') ";
        } else {
            sql = " select * from TB_BASE_RATE t where  code <> 'lpr' ";
        }
        return listBySQLAliasToBean(sql, null, TbBaseRate.class);
    }
    public int findAllBy(String code, String name, String id, boolean isAdd) {
        Map<String, Object> param = new HashMap<>();
        String sql = " select * from TB_BASE_RATE t where (t.code = :code or t.base_type_name = :name) ";
        if (!isAdd) {
            sql += " and t.id <> :id";
            param.put("id", id);
        }
        param.put("code", code);
        param.put("name", name);
        List<TbBaseRate> rates = listBySQLAliasToBean(sql, param, TbBaseRate.class);
        if (rates.size() > 0) {
            if (rates.get(0).getCode().equals(code)) {
                return 1;
            }
            if (rates.get(0).getBaseTypeName().equals(name)) {
                return 2;
            }
        }
        return 0;
    }
}
