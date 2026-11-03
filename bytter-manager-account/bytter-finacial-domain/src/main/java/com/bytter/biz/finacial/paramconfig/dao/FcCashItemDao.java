package com.bytter.biz.finacial.paramconfig.dao;
import com.bytter.biz.finacial.paramconfig.entity.FcCashItem;
import com.bytter.biz.finacial.paramconfig.vo.FcCashItemVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author
 * @version V1.0
 * @Title: dao
 * @Description: 现金流量项目数据访问
 * @date 2020-09-10 09:30:39
 */
@Repository
public class FcCashItemDao extends BaseDao<FcCashItem, String> {
    public DataTablesOutput listCashItem(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id as id ,a.item_code as itemCode,a.item_name as itemName ,");
        sql.append(" a.rmk as rmk, a.valid_sign as validSign ");
        sql.append(" from fc_cash_item a ");
        sql.append(" where 1 = 1  ");
        return getDynamicBySQL(input, sql.toString(), FcCashItemVo.class, null, false);
    }
    /**
    * <br>
    * <b>功能：</b> 查询是否存在录入的数据 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/9/11 0011 <br>
    */
    public List<FcCashItemVo> queryCashItem(FcCashItem cashItem) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id as id ,a.item_code as itemCode ");
        sql.append(" from fc_cash_item a ");
        sql.append(" where ( a.item_code = :itemCode or a.item_name = :itemName ) ");
        if (EmptyUtils.isNotEmpty(cashItem.getId())) {
            sql.append(" and a.id <> :id ");
            params.put("id", cashItem.getId());
        }
        params.put("itemCode", cashItem.getItemCode());
        params.put("itemName", cashItem.getItemName());
        return listBySQLAliasToBean(sql.toString(), params, FcCashItemVo.class);
    }
    /**
     * 查询所有有效现金流项目
     * @param params
     * @return
     */
    public List<FcCashItemVo> getAllCashItem(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("select * from fc_cash_item where VALID_SIGN = '1'");
        return listBySQLAliasToBean(sql.toString(), params, FcCashItemVo.class);
    }
}
