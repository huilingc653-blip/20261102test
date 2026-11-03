package com.bytter.guarantee.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.guarantee.domain.entity.GuaPledgeOccupy;
import com.bytter.guarantee.domain.vo.GuaPledgeOccupyVO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luxy
 * @Title: GuaPledgeOccupyDao
 * @ProjectName T3(3.2)
 * @Description: 抵质押占用数据访问层
 * @date 2020/7/7 9:46
 */
@Repository
public class GuaPledgeOccupyDao extends BaseDao<GuaPledgeOccupy, String> {
    public List<GuaPledgeOccupyDTO> getOccupyList(String businessId, String itemNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id,a.sysCode,a.business_id as businessId,  ")
                .append(" a.item_no  as itemNo,a.pledge_id   as pledgeId,")
                .append(" a.start_date  as startDate,a.end_date    as endDate, b.pledge_no   as pledgeNo,a.status, ")
                .append(" b.pledge_attr as pledgeAttr, ").append(" b.pledge_type as pledgeType, ")
                .append(" b.pledge_name as pledgeName, ").append(" a.cur_id as curId, ")
                //TODO 补充币种名称
                .append(" b.CUR_NAME as curName, t.type_name as typeName, ")
                .append(" b.pledge_money as pledgeMoney, ")
                .append(" (BT_NVL_NUM(B.PLEDGE_MONEY, 0) - BT_NVL_NUM(B.FREEZE_MONEY, 0) - BT_NVL_NUM(B.OCCUPY_MONEY, 0)) AS availMoney, ")
                .append(" (CASE WHEN a.occup_money IS NULL THEN 0 ELSE a.occup_money END) AS occupyMoney ")
                .append(" from GUA_PLEDGE_OCCUP a left join gua_pledge_info b ").append(" on a.pledge_id = b.id ")
                .append("join GUA_PLEDGE_TYPE t on t.id = b.pledge_type ")
                .append(" where 1=1 and a.status in (1,2) ");
        if (EmptyUtils.isNotEmpty(businessId)) {
            sql.append(" and a.BUSINESS_ID = :businessId  ");
            params.put("businessId", businessId);
        }
        if (EmptyUtils.isNotEmpty(itemNo)) {
            sql.append(" and a.ITEM_NO=:itemNo ");
            params.put("itemNo", itemNo);
        }
        return listBySQLAliasToBean(sql.toString(), params, GuaPledgeOccupyDTO.class);
    }

    public List<GuaPledgeOccupyVO> getPledgeOccupyList(String pledgeId, String businessId, String status) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id,a.sysCode,a.business_id as businessId,  ")
                .append(" a.item_no  as itemNo,a.pledge_id   as pledgeId,")
                .append(" a.start_date  as startDate,a.end_date as endDate, b.pledge_no as pledgeNo,a.status, ")
                .append(" b.pledge_attr as pledgeAttr, ").append(" b.pledge_type as pledgeType, ")
                //TODO 补充币种名称
                .append(" b.pledge_name as pledgeName, ").append(" a.cur_id as curId, b.cur_name as curName,")
                .append(" a.occup_money as occupyMoney ")
                .append(" from GUA_PLEDGE_OCCUP a left join gua_pledge_info b ").append(" on a.pledge_id = b.id ")
                .append(" where 1=1  ");
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and a.status in (:status) ");
            params.put("status", status.split(","));
        }
        if (EmptyUtils.isNotEmpty(pledgeId)) {
            sql.append(" and a.pledge_id=:pledgeId ");
            params.put("pledgeId", pledgeId);
        }
        if (EmptyUtils.isNotEmpty(businessId)) {
            sql.append(" and a.business_id=:businessId ");
            params.put("businessId", businessId);
        }
        return listBySQLAliasToBean(sql.toString(), params, GuaPledgeOccupyVO.class);
    }
}
