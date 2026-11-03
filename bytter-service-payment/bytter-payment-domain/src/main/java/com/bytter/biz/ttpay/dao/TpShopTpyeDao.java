package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.entity.TpShopType;
import com.bytter.biz.ttpay.vo.TpShopTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class TpShopTpyeDao extends BaseDao<TpShopType, String> {
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getQueryDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.ID id,t.CODE code,t.RMK rmk,t.SHOPTYPE shopType,t.STATUS status,t.IS_FLAG isFlag");
        sql.append(" from TP_SHOPTYPE t where STATUS<>-2");
        Map<String, Object> params = new HashMap<String, Object>();
        return getDynamicBySQL(input, sql.toString(), TpShopTypeVO.class, "t", false, params);
    }
    //获取类型名称
    public List<TpShopTypeVO> getTypeName(){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select t.ID id,t.SHOPTYPE shopType,t.code ");
        sql.append(" from TP_SHOPTYPE t where STATUS<>-2 and t.IS_FLAG='1' order by t.code ");
        return listBySQLAliasToBean(sql.toString(), params, TpShopTypeVO.class);
    }
    /**
     * 根据商户类型id、code、名称查找商户类别（查询支付宝、微信对象）
     * @param payTypeId 商户类型id
     * @param payTypeCode 商户类型Code
     * @param payTypeName 商户类型名称：支付宝、微信
     * @return 商户对象
     */
    public TpShopType getShopType(String payTypeId,String payTypeCode,String payTypeName){
        if(StringUtils.isNotEmpty(payTypeId)){
            return this.getUniqueBy("id", payTypeId);
        }else if(StringUtils.isNotEmpty(payTypeCode)){
            return this.getUniqueBy("code", payTypeCode);
        }else if(StringUtils.isNotEmpty(payTypeName)){
            return this.getUniqueBy("shopType", payTypeName);
        }
        return null;
    }
    @SuppressWarnings("rawtypes")
    public boolean checkTpShopType(TpShopType tpShopType,String flag) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.*");
        sql.append(" from  TP_SHOPTYPE t where STATUS<>-2 ");
        Map<String,String> params = new HashMap<String,String>();
        if(flag.equals("code")){
            sql.append(" and t.CODE=:code");
            params.put("code", tpShopType.getCode());
        }
        if(flag.equals("type")){
            sql.append(" and t.SHOPTYPE=:shopType");
            params.put("shopType", tpShopType.getShopType());
        }
        if(!EmptyUtils.isEmpty(tpShopType.getId())){ //修改时应该排除自身
            sql.append(" and t.id<>:id");
            params.put("id", tpShopType.getId());
        }
        List<TpShopType> tpShopTypes=listBySQLAliasToBean(sql.toString(), params, TpShopType.class);
        if (tpShopTypes.size() < 1) {
            return true;
        }else {
            return false;
        }
    }
}
