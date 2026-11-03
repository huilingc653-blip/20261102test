package com.bytter.credit.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.credit.domain.entity.RatBkrationAdjustDetail;
import com.bytter.credit.domain.vo.RatBkrationAdjustDtlVO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 授信调整明细数据访问层
 *
 * @author luxy
 */
@Repository
public class RatBkrationAdjustDtlDao extends BaseDao<RatBkrationAdjustDetail, String> {
    /**
     * 根据adjustCode删除授信调整明细
     *
     * @param adjustCode 调整主表id
     */
    public void deleteBkAdjustDtl(String adjustCode) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from RAT_BKRATION_ADJUST_DETAIL where adjust_Code = :adjustCode ");
        params.put("adjustCode", adjustCode);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 根据授信调整主表id查询你授信调整明细
     *
     * @param adjustCode 调整主表id
     * @return 授信调整明细集合
     */
    public List<RatBkrationAdjustDtlVO> getRatBkAdjustDtl(String adjustCode) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append("select ra.id as id,ra.ADJUST_CODE as adjustCode ,ra.ITEM_FLAG as itemFlag,ra.ITEM_CODE as itemCode,");
        sql.append(" ra.ADJUST_MONEY as adjustMoney, ra.money as money,rc.name as itemName,  ");
        sql.append(" case when ra.credit_type is null then '1' else ra.credit_type end as creditType ");
        sql.append(" from RAT_BKRATION_ADJUST_DETAIL ra ,rat_item_custom rc ");
        sql.append(" where ra.item_Code=rc.id and  ra.ADJUST_CODE=:adjustCode ");
        params.put("adjustCode", adjustCode);
        return listBySQLAliasToBean(sql.toString(), params, RatBkrationAdjustDtlVO.class);
    }

    /**
     * 根据adjustCode查询授信录入最高额度调整明细
     *
     * @param adjustCode 授信调整主表id
     * @return 授信录入最高额度调整明细集合
     */
    public List<RatBkrationAdjustDtlVO> queryInputAdjustDtl(String adjustCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rbad.id as id, rbad.ADJUST_CODE as adjustCode, rbad.ITEM_FLAG as itemFlag, rbad.ITEM_CODE as itemCode, rc.name as itemName, rbad.money as money, ");
        sql.append(" case when rbad.credit_type is null then '1' else rbad.credit_type end as creditType, ");
        sql.append(" rbd.comp_sign as compSign ");
        sql.append(" from rat_bkration_adjust_detail rbad ");
        sql.append(" inner join rat_bkration_adjust rba on rbad.adjust_code = rba.id ");
        sql.append(" inner join rat_bkration_detail rbd on rba.business_id = rbd.rat_code and rbad.item_code = rbd.item_code ");
        sql.append(" inner join rat_item_custom rc on rbd.ITEM_CODE = rc.id and rc.status = 0 ");
        sql.append(" where rbad.adjust_code = :adjustCode ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("adjustCode", adjustCode);
        return listBySQLAliasToBean(sql.toString(), params, RatBkrationAdjustDtlVO.class);
    }

    /**
     * 根据adjustCode查询授信分配最高额度调整明细
     *
     * @param adjustCode 授信调整主表id
     * @return 授信录入最高额度调整明细集合
     */
    public List<RatBkrationAdjustDtlVO> queryDisAdjustDtl(String adjustCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rbad.id as id, rbad.ADJUST_CODE as adjustCode, rbad.ITEM_FLAG as itemFlag, rbad.ITEM_CODE as itemCode, rc.name as itemName, rbad.money as money, ");
        sql.append(" case when rbad.credit_type is null then '1' else rbad.credit_type end as creditType, ");
        sql.append(" rbdd.comp_sign as compSign ");
        sql.append(" from rat_bkration_adjust_detail rbad ");
        sql.append(" inner join rat_bkration_adjust rba on rbad.adjust_code = rba.id ");
        sql.append(" inner join rat_bkration_dis_detail rbdd on rba.business_id = rbdd.dis_code and rbad.item_code = rbdd.item_code ");
        sql.append(" inner join rat_bkration_dis dis on dis.id = rbdd.dis_code ");
        sql.append(" inner join rat_item_custom rc on rbdd.ITEM_CODE = rc.id and rc.status = 0 ");
        sql.append(" where rbad.adjust_code = :adjustCode ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("adjustCode", adjustCode);
        return listBySQLAliasToBean(sql.toString(), params, RatBkrationAdjustDtlVO.class);
    }
}
