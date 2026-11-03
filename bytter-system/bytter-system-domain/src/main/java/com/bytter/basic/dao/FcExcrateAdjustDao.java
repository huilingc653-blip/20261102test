package com.bytter.basic.dao;
import com.bytter.basic.entity.FcExcrateAdjust;
import com.bytter.basic.vo.FcExcrateAdjustVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: FC_EXCRATE_ADJUST
 * @date 2017-09-06 10:01:05
 */
@Repository
public class FcExcrateAdjustDao extends BaseDao<FcExcrateAdjust, String> {
    /**
     * 查询列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id,t.cur_id,c.cur_name,t.refer_cur_id,cc.cur_name referCurName,t.pre_time,t.old_exchange_rate,t.new_exchange_rate,t.rmk,t.status,t.type_id,r.type_name");
        sql.append(" ,t.create_by,t.create_date,t.update_by,t.update_date, t.foreign_exchange_purchase, t.foreign_exchange_sell, t.foreign_exchange_middle,r.DECIMAL_PLACES as decimalPlaces  ");
        sql.append(" from fc_excrate_adjust t left join bt_rate_type r on t.type_id = r.id LEFT JOIN bt_currency c ON c.id=t.cur_id LEFT JOIN bt_currency cc ON cc.id=t.refer_cur_id where 1 = 1  ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcExcrateAdjustVO.class, "t", false);
    }
    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    public FcExcrateAdjustVO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id,t.cur_id,t.refer_cur_id,t.pre_time,t.old_exchange_rate,t.new_exchange_rate,t.rmk,t.status,t.type_id,r.type_name");
        sql.append(" ,t.create_by,t.create_date,t.update_by,t.update_date, t.foreign_exchange_purchase, t.foreign_exchange_sell, t.foreign_exchange_middle,r.DECIMAL_PLACES as decimalPlaces ");
        sql.append(" from fc_excrate_adjust t left join bt_rate_type r on t.type_id = r.id  where 1 = 1");
        sql.append(" and t.id = :id ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FcExcrateAdjustVO.class);
    }
    /**
     * 根据条件查询货币汇率
     *
     * @param curId   货币ID
     * @param preTime 日期
     * @param typeId  汇率类型id
     * @return
     */
    public BigDecimal getExchangeRateBy(String curId, Date preTime, String typeId) {
        StringBuilder hql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<>();
        hql.append(" from FcExcrateAdjust t where t.status = :status and t.curId =  :curId ");
        params.put("status", CommonConstant.STATUE_CHECK);
        params.put("curId", curId);
        if (!EmptyUtils.isEmpty(preTime)) {
            hql.append(" and bt_to_char(t.preTime,'yyyy-MM-dd') <= :preTime ");
            params.put("preTime", DateUtils.date_sdf().format(preTime));
        }
        if (StringUtils.hasText(typeId)) {
            hql.append(" and typeId = :typeId ");
            params.put("typeId", typeId);
        }
        hql.append(" order by t.preTime desc ");
        List<FcExcrateAdjust> list = this.listByHQL(hql.toString(), params, false);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0).getNewExchangeRate();
    }
    public DataTablesOutput getDataTablesCheck(DataTablesInput dataTablesInput, String loginName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.cur_id,c.cur_name, t.refer_cur_id,cc.cur_name referCurName, t.pre_time, t.old_exchange_rate, t.new_exchange_rate, t.rmk, t.status,t.type_id,r.type_name ");
        sql.append(" ,t.create_by,t.create_date,t.update_by,t.update_date, t.foreign_exchange_purchase, t.foreign_exchange_sell, t.foreign_exchange_middle,r.DECIMAL_PLACES as decimalPlaces ");
        sql.append(" from fc_excrate_adjust t left join bt_rate_type r on t.type_id = r.id LEFT JOIN bt_currency c ON c.id=t.cur_id LEFT JOIN bt_currency cc ON cc.id=t.refer_cur_id where 1 = 1 ");
        sql.append(" and t.create_by <> '" + loginName + "' ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcExcrateAdjustVO.class, "t", false);
    }
    /**
     * 根据条件查询货币汇率
     *
     * @param curId 货币ID
     * @return
     */
    public FcExcrateAdjust getPreTimeBy(String curId, String id) {
        StringBuilder hql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<>();
        hql.append(" from FcExcrateAdjust t where t.curId = :curId ");
        params.put("curId", curId);
        if (!EmptyUtils.isBlank(id)) {
            hql.append(" and id <> :id ");
            params.put("id", id);
        }
        hql.append(" order by t.preTime desc ");
        List<FcExcrateAdjust> list = this.listByHQL(hql.toString(), params, false);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }
    /**
     * 根据条件查询货币汇率
     *
     * @param curId1  币别1
     * @param curId2  币别2
     * @param preTime 执行日期
     * @param typeId  汇率类型id
     * @return
     */
    public FcExcrateAdjust getFcExcrateAdjustBy(String curId1, String curId2, Date preTime, String typeId) {
        StringBuilder hql = new StringBuilder();
        HashMap<String, Object> params = Maps.newHashMap();
        hql.append(" from FcExcrateAdjust t where t.status = :status ");
        hql.append(" and ((t.curId = :curId and referCurId = :referCurId ) or ( t.curId = :referCurId and referCurId = :curId))");
        params.put("status", CommonConstant.STATUE_CHECK);
        params.put("curId", curId1);
        params.put("referCurId", curId2);
        if (!EmptyUtils.isEmpty(preTime)) {
            hql.append(" and (t.preTime <= :preTime or t.preTime is null) ");
            params.put("preTime", preTime);
        }
        if (StringUtils.hasText(typeId)) {
            hql.append(" and typeId = :typeId ");
            params.put("typeId", typeId);
        }
        hql.append(" order by t.preTime desc ");
        List<FcExcrateAdjust> list = this.listByHQL(hql.toString(), params, false);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }
    /**
     * 根据币别字段查找
     *
     * @param curId1  币别1
     * @param curId2  币别2
     * @param preTime 执行日期
     * @param typeId  汇率类型id
     * @return
     */
    public String getTransferCurId(String curId1, String curId2, Date preTime, String typeId) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> params = Maps.newHashMap();
        sql.append(" select case when t.refer_cur_id = t2.refer_cur_id then t.refer_cur_id when t.cur_id = t2.cur_id then t.cur_id ");
        sql.append(" when t.cur_id = t2.refer_cur_id then t.cur_id when t.refer_cur_id = t2.cur_id then t.refer_cur_id  end as TRANSFER_CUR_ID ");
        sql.append(" from fc_excrate_adjust t, fc_excrate_adjust t2 where t.status = :status and t2.status = :status ");
        sql.append("  and t.type_Id = :typeId and t2.type_Id = :typeId");
        params.put("status", CommonConstant.STATUE_CHECK);
        params.put("typeId", typeId);
        sql.append(" and (");
        sql.append(" (t.refer_cur_id = t2.refer_cur_id and t.cur_id = :curId1 and t2.cur_id = :curId2 )");
        sql.append(" or (t.cur_id = t2.cur_id and t.refer_cur_id = :curId1 and t2.refer_cur_id = :curId2) ");
        sql.append(" or (t.cur_id = t2.refer_cur_id and t.refer_cur_id = :curId1  and t2.cur_id = :curId2)");
        sql.append(" or (t.refer_cur_id = t2.cur_id and t.cur_id = :curId1 and t2.refer_cur_id = :curId2)");
        sql.append(" or (t.cur_id = t2.cur_id and t.refer_cur_id = t2.refer_cur_id and t.cur_id = :curId1 and t2.refer_cur_id = :curId2)");
        sql.append(" ) ");
        params.put("curId1", curId1);
        params.put("curId2", curId2);
        if (!EmptyUtils.isEmpty(preTime)) {
            sql.append(" and (t.pre_time <= :preTime or t.pre_time is null ) and (t2.pre_time <= :preTime or t2.pre_time is null ) ");
            params.put("preTime", preTime);
        }
        sql.append(" order by t.pre_time desc, t2.pre_time desc");
        List<Map<String, String>> list = listBySQL(sql.toString(), params);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Map<String, String> map = list.get(0);
        String transferCurId = map.get("TRANSFER_CUR_ID");
        return transferCurId;
    }
}
