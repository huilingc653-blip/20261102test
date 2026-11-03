package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexExposureItem;
import com.bytter.biz.foreign.vo.FexExposureItemVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 敞口科目表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 */
@Repository
public class FexExposureItemDao extends BaseDao<FexExposureItem, String> {

    /**
     * 根据name搜索查询
     *
     * @param fexExposureItem
     * @param validSign
     * @return
     */
    public List<FexExposureItemVO> listFexExposureItem(FexExposureItemVO fexExposureItem, String validSign) {
        StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName,c.super_Item_Code as superItemCode,c.super_Item_Codes as superItemCodes,");
        sql.append(" c.sign as sign,c.is_Natural_Hedge as isNaturalHedge,c.valid_Sign as validSign,c.is_System as isSystem,c.rmk as rmk,");
        sql.append(" c.create_By as createBy,c.create_Date as createDate,c.update_By as updateBy,c.update_Date as updateDate");
        sql.append(" from Fex_Exposure_Item c where 1=1 ");

        if (StringUtils.isNotEmpty(validSign)) {
            sql.append(" and c.valid_Sign='");
            sql.append(validSign);
            sql.append("'");
        }
        if (!EmptyUtils.isEmpty(fexExposureItem)) {
            if (StringUtils.isNotEmpty(fexExposureItem.getItemName())) {
                sql.append(" and c.item_Name like '%");
                sql.append(fexExposureItem.getItemName());
                sql.append("%'");
            }
            if (StringUtils.isNotEmpty(fexExposureItem.getItemCode())) {
                sql.append(" and c.item_Code like '%");
                sql.append(fexExposureItem.getItemCode());
                sql.append("%'");
            }
            if (fexExposureItem.getSign() != null && 0 != fexExposureItem.getSign()) {
                sql.append(" and c.sign =");
                sql.append(fexExposureItem.getSign());
            }
            if (!EmptyUtils.isEmpty(fexExposureItem.getShowItem())) {
                if(fexExposureItem.getShowItem() == -1){
                    //查询末级科目，即不存在子科目的科目
                    sql.append(" and c.item_code not in (select super_item_code from Fex_Exposure_Item where super_item_code is not null)");
                }else if(fexExposureItem.getShowItem()>0){
                    sql.append(" and (bt_length(item_code)/3)="+fexExposureItem.getShowItem());
                }
            }
            sql.append(" order by c.item_Code");
        }
        return listBySQLAliasToBean(sql.toString(), null, FexExposureItemVO.class);
    }

    /**
     * 根据code搜索查询
     *
     * @param codes
     * @return
     */
    public List<FexExposureItemVO> listFexExposureItemByCodes(List<String> codes, FexExposureItemVO fexExposureItem) {
        StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName,c.super_Item_Code as superItemCode,c.super_Item_Codes as superItemCodes,");
        sql.append(" c.sign as sign,c.is_Natural_Hedge as isNaturalHedge,c.valid_Sign as validSign,c.is_System as isSystem,c.rmk as rmk,");
        sql.append(" c.create_By as createBy,c.create_Date as createDate,c.update_By as updateBy,c.update_Date as updateDate");
        sql.append(" from fex_Exposure_Item c where 1=1 ");

        String where = getSQLIn(codes, 900, "c.item_Code");
        if (!EmptyUtils.isEmpty(fexExposureItem)) {
            if (!EmptyUtils.isEmpty(fexExposureItem.getShowItem())) {
                if (fexExposureItem.getShowItem() == -1) {
                    //查询末级科目，即不存在子科目的科目
                    sql.append(" and c.item_code not in (select super_item_code from fbs_item where super_item_code is not null)");
                } else if (fexExposureItem.getShowItem() > 0) {
                    sql.append(" and (bt_length(item_code)/3)=" + fexExposureItem.getShowItem());
                }
            }
        }
        sql.append(" and ");
        return listBySQLAliasToBean(sql.append(where).toString(), null, FexExposureItemVO.class);
    }
    
    /**
     * 获取科目最高层级
     * @return
     */
    public Integer getItemMaxLevel(){
        String sql="select max(bt_length(item_code))/3 as maxLevel from fex_exposure_item";
        Object result = this.getSession().createSQLQuery(sql).uniqueResult();
        if(EmptyUtils.isEmpty(result)){
            return 0;
        }else {
            BigDecimal maxLevel = (BigDecimal) result;
            return maxLevel.intValue();
        }
    }

    /**
     * 根据id或者 code 查找 对象
     *
     * @param id
     * @param itemCode
     * @return
     */
    public FexExposureItemVO getFexExposureItemVO(String id, String itemCode) {
        StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName,c.super_Item_Code as superItemCode,c.super_Item_Codes as superItemCodes,");
        sql.append(" c.sign as sign,c.is_Natural_Hedge as isNaturalHedge,c.valid_Sign as validSign,c.is_System as isSystem,c.rmk as rmk,");
        sql.append(" c.create_By as createBy,c.create_Date as createDate,c.update_By as updateBy,c.update_Date as updateDate");
        sql.append(" from fex_exposure_item c where 1=1 ");

        if (StringUtils.isNotEmpty(id)) {
            sql.append(" and c.id='");
            sql.append(id);
            sql.append("'");
        }
        if (StringUtils.isNotEmpty(itemCode)) {
            sql.append(" and c.item_Code ='");
            sql.append(itemCode);
            sql.append("'");
        }
        return getBySQLAliasToBean(sql.toString(), null, FexExposureItemVO.class);
    }

    /**
     * 获取最大流水号
     *
     * @param prefix 业务前缀
     * @return
     */
    public String getSequence(String prefix, int sequenceLength) {
        StringBuilder sql = new StringBuilder();
        sql.append("select max(item_code) as currentSequence from fex_Exposure_Item ");
        Map<String, String> sqlMap = new HashMap<>();
        if (StringUtils.isNotBlank(prefix)) {
            sql.append(" where seq_code = :prefix");
            sqlMap.put("prefix", prefix);
        }
        Object sqlValue = createSQLQuery(sql.toString(), sqlMap).uniqueResult();
        if (!EmptyUtils.isEmpty(sqlValue)) {
            if (StringUtils.isBlank(prefix)) {
                String seq = sqlValue.toString().substring(0,1);
                Integer maxSeq = Integer.parseInt(seq) + 1;
                if(maxSeq > 9){
                    return "9";
                } else {
                    return maxSeq + "00";
                }
            } else {
                String seq = sqlValue.toString().replace(prefix, "");
                Integer maxSeq = Integer.parseInt(seq) + 1;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < (sequenceLength - String.valueOf(maxSeq).length()); i++) {
                    sb.append(0);
                }
                sb.append(maxSeq);
                return prefix + sb.toString();
            }
        }
        return "";
    }

    public BigDecimal isEdit(String itemCode) {
        String sql = "select count(1) as cnt from fex_Exposure_Item f where f.super_item_code=:itemCode and f.valid_sign ='1'";
        Map<String, String> params = new HashMap<>();
        params.put("itemCode", itemCode);
        BigDecimal count = new BigDecimal(createSQLQuery(sql, params).uniqueResult().toString());
        return count;
    }

    /**
     * 根据上级科目获取所有子科目
     * @param superItemCodes
     * @return
     */
    public List<FexExposureItemVO> listBySuperItemCodes(List<String> superItemCodes) {
        StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName,c.super_Item_Code as superItemCode,c.super_Item_Codes as superItemCodes,");
        sql.append(" c.sign as sign,c.is_Natural_Hedge as isNaturalHedge,c.valid_Sign as validSign,c.is_System as isSystem,c.rmk as rmk,");
        sql.append(" c.create_By as createBy,c.create_Date as createDate,c.update_By as updateBy,c.update_Date as updateDate");
        sql.append(" from fex_exposure_item c where 1=1 ");
        sql.append(" and ").append(getSQLIn(superItemCodes, 900, "c.super_item_code"));
        return listBySQLAliasToBean(sql.toString(), null, FexExposureItemVO.class);
    }

    /**
     * 根据科目代码删除科目信息
     * @param itemCodes
     */
    public void deleteByItemCodes(List<String> itemCodes) {
        StringBuilder sql = new StringBuilder(" delete from fex_exposure_item where  1=1 ");
        sql.append(" and ").append(getSQLIn(itemCodes, 900, "item_code"));
        this.createSQLQuery(sql.toString()).executeUpdate();
    }
}
