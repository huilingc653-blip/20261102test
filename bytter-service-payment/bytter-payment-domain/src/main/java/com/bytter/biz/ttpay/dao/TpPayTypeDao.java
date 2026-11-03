package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.entity.TpPayType;
import com.bytter.biz.ttpay.vo.TpPayTypeVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class TpPayTypeDao extends BaseDao<TpPayType, String> {
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getQueryDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.ID id,t.CODE code,t.RMK rmk,t.PAYTYPE payType,t.STATUS status,t.IS_FLAG isFlag");
        sql.append(" from TP_PAY_TYPE t where STATUS<>-2");
        Map<String, Object> params = new HashMap<String, Object>();
        return getDynamicBySQL(input, sql.toString(), TpPayTypeVo.class, "t", false, params);
    }
    @SuppressWarnings("rawtypes")
    public boolean checkTpShopType(TpPayType tpShopType, String flag) {
        StringBuilder sql = new StringBuilder();
        sql.append("select COUNT(*) as count");
        sql.append(" from  TP_PAY_TYPE t where STATUS<>-2 ");
        Map<String,String> params = new HashMap<String,String>();
        if(flag.equals("code")){
            sql.append(" and t.CODE=:code");
            params.put("code", tpShopType.getCode());
        }
        if(flag.equals("type")){
            sql.append(" and t.PAYTYPE=:payType");
            params.put("payType", tpShopType.getPayType());
        }
        if(!EmptyUtils.isEmpty(tpShopType.getId())){ //修改时应该排除自身
            sql.append(" and t.id<>:id");
            params.put("id", tpShopType.getId());
        }
        Map map=getBySQL(sql.toString(), params);
        if(!map.isEmpty()){
            BigDecimal count = (BigDecimal)map.get("COUNT");
            if(EmptyUtils.isEmpty(count)){
                count = (BigDecimal)map.get("count");
            }
            if(count!=null){
                int i=count.compareTo(BigDecimal.ZERO);
                if(i==0){
                    return true;
                }
            }
        }
        return false;
    }
    //获取类型名称
    public List<TpPayTypeVo> getTypeName(){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select t.ID id,t.PAYTYPE payType,t.code ");
        sql.append(" from TP_PAY_TYPE t where STATUS<>-2 and t.IS_FLAG='1' order by t.code ");
        return listBySQLAliasToBean(sql.toString(), params, TpPayTypeVo.class);
    }
}
