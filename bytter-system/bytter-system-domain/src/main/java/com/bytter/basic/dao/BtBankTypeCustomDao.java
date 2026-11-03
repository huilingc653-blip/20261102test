package com.bytter.basic.dao;


import cn.hutool.json.JSONUtil;
import com.bytter.basic.entity.BankTypeCustom;
import com.bytter.basic.vo.BankTypeCustomVO;
import com.bytter.basic.vo.CustomQueryVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 银行自定义自定义维护
 * @date 2023-12-22 18:07:34
 */
@Repository
public class BtBankTypeCustomDao extends BaseDao<BankTypeCustom, String> {

    /**
     * 根据银行类型删除自定义
     * @param bankType
     */
    public void deleteByBankType(String bankType, String customType) {
        String sql = " update  bt_bank_type_custom set is_deleted=1 where bank_type_id=:bankType and custom_type=:customType and  is_deleted<> '1' ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("bankType", bankType);
        params.put("customType", customType);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 根据银行类型获取银行自定义
     * @param bankType
     * @return
     */
    public List<BankTypeCustom> listByBankType(String bankType, String customType) {
        String hql = " from BankTypeCustom b where b.bankTypeId=:bankType and b.customType=:customType and b.isDeleted <> '1' ";
        StringBuilder sb=new StringBuilder(hql);
        Map<String, String> params = new HashMap<>();
        params.put("bankType", bankType);
        params.put("customType", customType);
        sb.append("order by b.customCode");
        return listByHQLAliasToBean(sb.toString(), params, BankTypeCustom.class);
    }

    /**
     * 根据银行类型和接口代码获取银行自定义
     * @param bankType
     * @param bifCode
     * @return
     */
    public List<BankTypeCustomVO> listByBankTypeAndBifCode(String bankType, String bifCode, String countryId,String customType) {
        StringBuilder sql = new StringBuilder(getBankTypeCustomSelectSql());
        sql.append(" where b.BANK_TYPE_ID=:bankType and b.BIF_CODE=:bifCode and b.CUSTOM_TYPE=:customType ");
        appendCountryCondition(sql, countryId, true);
        sql.append(" and b.is_deleted <> '1'");
        Map<String, String> params = new HashMap<>();
        params.put("bankType", bankType);
        params.put("bifCode", bifCode);
        params.put("customType", customType);
        putCountryParam(params, countryId);
        sql.append(" order by b.CUSTOM_CODE");
        return listBySQLAliasToBean(sql.toString(), params, BankTypeCustomVO.class);
    }

    /**
     * 根据银行类型和接口代码获取银行自定义
     * @param bankType
     * @param bifCode
     * @return
     */
    public List<BankTypeCustomVO> listByBankTypeAndBifCode(String bankType, String bifCode, String countryId, String bankPayType,String customType) {
        StringBuilder sql = new StringBuilder(getBankTypeCustomSelectSql());
        sql.append(" where b.BANK_TYPE_ID=:bankType and b.BIF_CODE=:bifCode and b.CUSTOM_TYPE=:customType ");
        appendCountryCondition(sql, countryId, true);
        sql.append(" and (b.BANK_PAY_TYPE like :bankPayType or b.BANK_PAY_TYPE is null or b.BANK_PAY_TYPE = '') and b.is_deleted <> '1' ");
        Map<String, String> params = new HashMap<>();
        params.put("bankType", bankType);
        params.put("bifCode", bifCode);
        params.put("customType", customType);
        putCountryParam(params, countryId);
        params.put("bankPayType", "%" + bankPayType + "%");
        sql.append(" order by b.CUSTOM_CODE");
        return listBySQLAliasToBean(sql.toString(), params, BankTypeCustomVO.class);
    }

    /**
     * 根据银行类型和接口代码获取银行自定义
     * @param bankType
     * @param bifCode
     * @return
     */
    public BankTypeCustomVO getByBankTypeCustom(String bankType, String bifCode, String customCode, String customType) {
        String hql = " from BankTypeCustom b where b.bankTypeId=:bankType and b.bifCode=:bifCode and b.customCode=:customCode and b.customType=:customType  and b.isDeleted <> '1' ";
        Map<String, String> params = new HashMap<>();
        params.put("bankType", bankType);
        params.put("bifCode", bifCode);
        params.put("customCode", customCode);
        params.put("customType", customType);
        List<BankTypeCustomVO> list = listByHQLAliasToBean(hql, params, BankTypeCustomVO.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }


    /**
     * 根据银行类型和接口代码和国家获取银行自定义
     *
     * @param bankTypeId
     * @param bifCode
     * @param countryId
     * @return
     */
    public List<BankTypeCustomVO> listByBankTypeIdAndBifCodeAndCountryId(String bankTypeId, String bifCode, String countryId) {
        String sql = "select b.ID id,b.BANK_TYPE_ID bankTypeId,b.BIF_CODE bifCode,b.COUNTRY_ID countryId,b.CUSTOM_CODE  customCode,b.CUSTOM_NAME customName," +
                "b.CUSTOM_TYPE customType,b.BANK_PAY_TYPE bankPayType from bt_bank_type_custom b  " +
                "  where   b.BANK_TYPE_ID=:bankTypeId  and b.BIF_CODE=:bifCode  and   b.CUSTOM_TYPE='bankPayType'  and b.is_deleted <> '1' ";
        StringBuilder sb=new StringBuilder(sql);
        Map<String, String> params = new HashMap<>();
        params.put("bankTypeId", bankTypeId);
        params.put("bifCode", bifCode);
        appendCountryCondition(sb, countryId, false);
        putCountryParam(params, countryId);
        sb.append(" order by b.CUSTOM_CODE");
        return listBySQLAliasToBean(sb.toString(), params, BankTypeCustomVO.class);
    }


    public List<BankTypeCustomVO> listByCondition(CustomQueryVo dto) {
        String sql = "select b.ID id,b.BANK_TYPE_ID bankTypeId,b.BIF_CODE bifCode,b.COUNTRY_ID countryId,b.CUSTOM_CODE  customCode,b.CUSTOM_NAME customName," +
                "b.CUSTOM_TYPE customType,b.BANK_PAY_TYPE bankPayType,bbt.type_name bankTypeName,bbi.BIF_CODE bifName " +
                " from bt_bank_type_custom b  " ;
        StringBuilder sb=new StringBuilder(sql);
        sb.append("left join bt_bank_type bbt on bbt.id=b.bank_type_id " );
        sb.append("left join bis_bif_init bbi on bbi.id=b.bif_code " );
        sb.append(" where b.is_deleted<>1 ");
        Map<String, String> params = new HashMap<>();

        if(EmptyUtils.isNotEmpty(dto.getPayTypeId())) {
            params.put("payTypeId", dto.getPayTypeId());
            sb.append(" and b.id=:payTypeId ");
        }
        if(EmptyUtils.isNotEmpty(dto.getBankTypeId())) {
            params.put("bankTypeId", dto.getBankTypeId());
            sb.append(" and b.BANK_TYPE_ID=:bankTypeId ");
        }
        if(EmptyUtils.isNotEmpty(dto.getBifCode())) {
            params.put("bifCode", dto.getBifCode());
            sb.append(" and b.BIF_CODE=:bifCode  ");
        }
        if(EmptyUtils.isNotEmpty(dto.getCountryId())) {
            appendCountryCondition(sb, dto.getCountryId(), false);
            putCountryParam(params, dto.getCountryId());
        }
        if(EmptyUtils.isNotEmpty(dto.getCustomCode())) {
            params.put("customCode", dto.getCustomCode());
            sb.append(" and b.custom_code=:customCode ");
        }
        if(EmptyUtils.isNotEmpty(dto.getCustomName())) {
            params.put("customName", dto.getCustomName());
            sb.append(" and b.custom_name=:customName ");
        }
        if(EmptyUtils.isNotEmpty(dto.getCustomType())) {
            params.put("customType", dto.getCustomType());
            sb.append(" and b.CUSTOM_TYPE=:customType ");
        }
        sb.append(" order by b.CUSTOM_CODE");
        logger.info("listByCondition params :", JSONUtil.toJsonStr(params));
        return listBySQLAliasToBean(sb.toString(), params, BankTypeCustomVO.class);
    }


    public List<BankTypeCustom> listByIds(List<String> ids, String customType) {
        String hql = " from BankTypeCustom b where   b.id=:id  and isDeleted<>1 ";
        StringBuilder stringBuilder=new StringBuilder(hql);
        Map<String, String> params = new HashMap<>();
        if (!EmptyUtils.isEmpty(ids)) {
            stringBuilder.append(" and (" + getSQLIn(ids, 900, "b.id") + ")");
        }
        params.put("customType", customType);
        stringBuilder.append(" order by by b.customCode");
        return listByHQLAliasToBean(stringBuilder.toString(), params, BankTypeCustom.class);
    }

    private String getBankTypeCustomSelectSql() {
        return "select b.ID id,b.BANK_TYPE_ID bankTypeId,b.BIF_CODE bifCode,b.COUNTRY_ID countryId,b.CUSTOM_CODE customCode,b.CUSTOM_NAME customName," +
                "b.CUSTOM_TYPE customType,b.BANK_PAY_TYPE bankPayType from bt_bank_type_custom b ";
    }

    /**
     * 追加国家过滤条件。
     * 兼容银行自定义国家字段直接存企业国家ID，以及存内管国家ID后需映射到企业国家ID的两种场景。
     *
     * @param sql                 SQL 构造器
     * @param countryId           企业端国家ID
     * @param includeEmptyCountry 是否保留空国家配置作为通用配置返回
     */
    private void appendCountryCondition(StringBuilder sql, String countryId, boolean includeEmptyCountry) {
        if (EmptyUtils.isNotEmpty(countryId)) {
            sql.append(" and (");
            if (includeEmptyCountry) {
                sql.append("b.COUNTRY_ID is null or b.COUNTRY_ID = '' or ");
            }
            sql.append("BT_CONCAT3(',', b.COUNTRY_ID, ',') like :countryId ");
            sql.append(" or exists (select 1 from sys_country_cust_manager cm ");
            sql.append(" inner join sys_country sc on sc.id=cm.sys_id ");
            sql.append(" inner join sys_country_cust scc on scc.sys_id=sc.id ");
            sql.append(" where scc.id=:countryIdValue ");
            sql.append(" and BT_CONCAT3(',', b.COUNTRY_ID, ',') like BT_CONCAT3('%,', cm.id, ',%')))");
        }
    }

    /**
     * 设置国家过滤参数。
     * countryId 用于兼容历史逗号分隔存储的直接匹配，countryIdValue 用于国家映射子查询。
     *
     * @param params    查询参数
     * @param countryId 企业端国家ID
     */
    private void putCountryParam(Map<String, String> params, String countryId) {
        if (EmptyUtils.isNotEmpty(countryId)) {
            params.put("countryId", "%," + countryId + ",%");
            params.put("countryIdValue", countryId);
        }
    }

}
