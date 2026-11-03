package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexProduct;
import com.bytter.biz.foreign.vo.FexProductVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 外汇产品表Dao
 * @date 2020-02-16 15:20:33
 */
@Repository
public class FexProductDao extends BaseDao<FexProduct, String> {
    /**
     * 外汇产品表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select p.id as id,p.pro_Level as proLevel,p.tran_Type as tranType,p.pro_Name as proName,p.pro_Desc as proDesc,p.pro_Id as proId,");
        sql.append(" f.pro_Name as parentProName,p.fex_Bus_Type as fexBusType,p.fex_Tran_Type as fexTranType,p.delivery_Type as deliveryType,p.delivery_Cur_Id as deliveryCurId,");
        sql.append(" p.is_Allowed_Delivery as isAllowedDelivery,p.is_Allowed_Settle as isAllowedSettle,p.buy_Cur_Id as buyCurId,p.sell_Cur_Id as sellCurId,");
        sql.append(" p.non_Workday_Rule as nonWorkdayRule,p.trangroup_Rule_Id as trangroupRuleId,r.rule_Name as trangroupRuleName,p.is_Allowed_Change as isAllowedChange,p.valid_Sign as validSign,p.rmk as rmk,");
        sql.append(" p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate,p.fex_Option1 as fexOption1,p.fex_Option2 as fexOption2,p.fex_Option3 as fexOption3, ");
        sql.append(" p.fex_Option_Type as fexOptionType,p.fex_Option_Rate as fexOptionRate,p.refund_Time as refundTime,p.days_Rule as daysRule,p.valid_Standard as validStandard,p.tran_Settle_Method as tranSettleMethod,");
        sql.append(" p.is_Show_Agent as isShowAgent,p.is_Show_Clear_House as isShowClearHouse,p.is_allowed_del as isAllowedDel ");
        //新增即期是否自动交割字段
        sql.append(" ,p.is_auto_delivery as isAutoDelivery ");
        sql.append(" from fex_product p left join fex_product f on p.pro_id = f.id ");
        sql.append(" left join fex_tran_group_rule r on p.trangroup_Rule_Id = r.id");
        sql.append(" where 1=1");
        return sql.toString();
    }

    /**
     * 外汇产品列表查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        dataTablesInput.setOrderString(new OrderString("p.create_date", "desc"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexProductVO.class, "p", false);
    }

    /**
     * 根据ID获取外汇产品信息
     *
     * @param id
     * @return
     */
    public FexProductVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and p.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FexProductVO.class);
    }

    /**
     * 根据产品层级和交易类型获取产品信息
     *
     * @param proLevel
     * @param tranType
     * @return
     */
    public List<FexProductVO> listByProLevelAndTranType(String proLevel, String tranType) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(proLevel)) {
            sql.append(" and p.pro_Level=:proLevel");
            params.put("proLevel", proLevel);
        }
        if (StringUtils.isNotBlank(tranType) && !"-1".equals(tranType)) {
            if(ForeignConstant.TRAN_TYPE_1.equals(tranType) || ForeignConstant.TRAN_TYPE_7.equals(tranType)
                ||ForeignConstant.TRAN_TYPE_8.equals(tranType) || ForeignConstant.TRAN_TYPE_9.equals(tranType)){
                List<String> list = new ArrayList<>();
                list.add(tranType);
                // 产品组(pro_level=0)在外汇买卖/即期/远期/掉期之间共用，编辑即期/远期/掉期产品时
                // 也要能带出外汇买卖维护的产品组，否则所属产品组下拉为空
                if (ForeignConstant.PRO_LEVEL_0.equals(proLevel)) {
                    list = Arrays.asList(ForeignConstant.TRAN_TYPE_1, ForeignConstant.TRAN_TYPE_7,
                            ForeignConstant.TRAN_TYPE_8, ForeignConstant.TRAN_TYPE_9);
                }
                sql.append(" and p.tran_Type in:tranType");
                params.put("tranType", list);
            }else{
                // 含资金收付(4)：按交易类型查有效产品/产品组；勿用 is_allowed_del<>1（会漏掉可删产品及 NULL）
                sql.append(" and p.tran_Type=:tranType");
                params.put("tranType", tranType);
            }
        }
        sql.append(" and p.valid_Sign=:validSign");
        params.put("validSign", CommonConstant.YES);
        return listBySQLAliasToBean(sql.toString(), params, FexProductVO.class);
    }

    /**
     * 根据产品层级和外汇交易类型获取产品信息
     *
     * @param proLevel
     * @param fexTranType
     * @return
     */
    public List<FexProductVO> listByProLevelAndFexTranType(String proLevel, String fexTranType) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        HashMap<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(proLevel)) {
            sql.append(" and p.pro_Level=:proLevel");
            params.put("proLevel", proLevel);
        }
        if (StringUtils.isNotBlank(fexTranType)) {
            sql.append(" and p.fex_tran_Type=:fexTranType");
            params.put("fexTranType", fexTranType);
        }
        sql.append(" and p.valid_Sign=:validSign");
        params.put("validSign", CommonConstant.YES);
        return listBySQLAliasToBean(sql.toString(), params, FexProductVO.class);
    }

    /**
     * 根据产品名称获取产品信息
     *
     * @param proName
     * @param proLevel
     * @param id
     * @return
     */
    public List<FexProductVO> listByProName(String proName, String proLevel, String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and p.pro_Name=:proName and p.pro_Level=:proLevel");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("proName", proName);
        params.put("proLevel", proLevel);
        if (StringUtils.isNotBlank(id)) {
            sql.append("  and p.id<>:id");
            params.put("id", id);
        }
        return listBySQLAliasToBean(sql.toString(), params, FexProductVO.class);
    }

    /**
     * 根据ID获取外汇产品信息
     *
     * @param ids
     * @return
     */
    public List<FexProductVO> listByIds(String[] ids) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and p.id in (:ids)");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids);
        return listBySQLAliasToBean(sql.toString(), params, FexProductVO.class);
    }

    /**
     * <p>
     * 查询产品是否引用了规则组，列出所有引用规则组的产品
     * </p>
     *
     * @param tranGroupRuleId 规则组ID
     * @return number
     */
    public List<FexProductVO> countByTranGroupRuleId(String tranGroupRuleId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select trangroup_Rule_Id from fex_Product where");
        sql.append(" trangroup_Rule_Id = '").append(tranGroupRuleId).append("'");
        return listBySQLAliasToBean(sql.toString(),null,FexProductVO.class);
    }


    /**
     * <p>
     * 根据产品组id获取
     * </p>
     *
     * @param id 产品组id
     * @return number
     */
    public List<FexProductVO> listByGroupId(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT pro_name FROM fex_product WHERE");
        sql.append(" pro_level = '").append(ForeignConstant.PRO_LEVEL_1).append("'");
        sql.append(" AND pro_id = '").append(id).append("'");
        sql.append(" AND valid_sign = '").append(CommonConstant.VALID_SIGN_YES).append("'");
        return listBySQLAliasToBean(sql.toString(),null,FexProductVO.class);
    }


    /**
     * 获取外汇产品跟其他业务的关联数量
     * @param proId 外汇产品
     * @return
     */
    public long checkAssociated(String proId){
        long check = 0;
        Map<String,String> map = new HashMap<>();
        map.put("pro_id",proId);
        check = check +addSum("fex_tran_req",map);
        check = check +addSum("fex_tran_app_dtl",map);

        return check;
    }

    /**
     * 获取关联数量
     * @param table 表名
     * @param map 列名/值
     * @return
     */
    private long addSum(String table, Map<String,String> map){
        StringBuilder sql = new StringBuilder();
        sql.append("select count(id) from ");
        sql.append(table);
        sql.append(" WHERE ");
        for(String key:map.keySet()){
            sql.append(key).append("=").append("'").append( map.get(key)).append("'").append(" OR ");
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
     * 资金收付交易初始化产品
     *
     * @return
     */
    public List<FexProductVO> getInitProductDate() {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append(" and p.tran_type=:tranType");
        sql.append(" and (p.id like :fpsProductId or p.id like :payPalProductId)");
        sql.append(" and p.is_allowed_del<>1");
        params.put("tranType", ForeignConstant.TRAN_TYPE_4);
        params.put("fpsProductId", ForeignConstant.FEX_PRODUCT_FPS + "%");
        params.put("payPalProductId", ForeignConstant.FEX_PRODUCT_PAYPAL + "%");
        return listBySQLAliasToBean(sql.toString(), params, FexProductVO.class);
    }


    /**
     * 查询产品列表
     * @param proLevel
     * @param tranType
     * @param fexTranType
     * @return
     */
    public List<FexProductVO> listByProAndFexTranType(String proLevel, String tranType, String fexTranType) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        HashMap<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(proLevel)) {
            sql.append(" and p.pro_Level=:proLevel");
            params.put("proLevel", proLevel);
        }
        if (StringUtils.isNotBlank(tranType) && !"-1".equals(tranType)) {
            sql.append(" and p.tran_Type=:tranType");
            params.put("tranType", tranType);
        }
        if (StringUtils.isNotBlank(fexTranType) && !"-1".equals(fexTranType)) {
            sql.append(" and p.fex_tran_Type=:fexTranType");
            params.put("fexTranType", fexTranType);
        }
        sql.append(" and p.valid_Sign=:validSign");
        params.put("validSign", CommonConstant.YES);
        return listBySQLAliasToBean(sql.toString(), params, FexProductVO.class);
    }
}
