package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexExposureDetail;
import com.bytter.biz.foreign.vo.FexExposureDetailVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 单位敞口上报明细表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexExposureDetailDao extends BaseDao<FexExposureDetail, String> {

    /**
     * 根据敞口上报信息获取上报明细
     * @param exposureId
     * @return
     */
    public List<FexExposureDetailVO> listVOByExposureId(String exposureId, String itemCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.exposure_Id as exposureId,t.cur_Id as curId,t.item_Code as itemCode,i.super_Item_Code as superItemCode,");
        sql.append(" t.item_Name as itemName,t.sign as sign,t.is_Natural_Hedge as isNaturalHedge,t.fill_Money as fillMoney");
        sql.append(" from Fex_Exposure_Detail t left join fex_exposure_item i on i.item_Code=t.item_Code");
        sql.append(" where t.exposure_Id=:exposureId");
        if (StringUtils.isNotBlank(itemCode)) {
            String[] itemCodes = itemCode.split(CommonConstant.COMMA_SPLIT);
            sql.append(" and ").append(SqlUtil.getInSql(Arrays.asList(itemCodes), "t.item_Code", true));
        }
        sql.append(" order by t.item_Code");
        HashMap<String, String> params = new HashMap<>();
        params.put("exposureId", exposureId);
        return this.listBySQLAliasToBean(sql.toString(), params, FexExposureDetailVO.class);
    }

    /**
     * 删除上报明细
     * @param id
     */
    public void deleteByMainId(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from Fex_Exposure_Detail where exposure_Id in (select id from Fex_Exposure where main_id=:mainId)");
        Map<String,String> params = new HashMap<String,String>();
        params.put("mainId",id);
        this.createSQLQuery(sql.toString(),params).executeUpdate();
    }

    /**
     * 根据子单获取上报明细集合
     * @param childMainIds
     * @return
     */
    public List<FexExposureDetailVO> getDtlByChildCorp(List<String> childMainIds, String curId, Date beginDate, Date endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.item_code as itemCode,t.item_name as itemName,t.sign,t.is_Natural_hedge as isNaturalHedge,sum(t.fill_money) as fillMoney from Fex_Exposure_Detail t ");
        sql.append(" left join Fex_Exposure f on f.id = t.exposure_id");
        sql.append(" where ").append(SqlUtil.getInSql(childMainIds, "f.main_id", true));
        sql.append(" and f.cur_Id=:curId and t.cur_Id=:curId");
        sql.append(" and f.begin_date=:beginDate and f.end_date=:endDate");
        sql.append(" group by t.item_code,t.item_name,t.sign,t.is_Natural_hedge");
        sql.append(" order by t.item_code");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("curId",curId);
        params.put("beginDate", beginDate);
        params.put("endDate",endDate);
        return this.listBySQLAliasToBean(sql.toString(), params, FexExposureDetailVO.class);
    }
}
