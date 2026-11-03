package com.bytter.biz.finacial.interest.dao;

import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.interest.dto.FcPredrawRushDtlDTO;
import com.bytter.biz.finacial.interest.entity.FcPredrawRushDtl;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Title: 冲预提信息表DAO
* @Description: FC_PREDRAW_DTL
* @author zouxs
* @date 2023-11-06 19:25:58
* @version 1.0
*
*/
@Repository
public class FcPredrawRushDtlDao extends BaseDao<FcPredrawRushDtl,String>{

    public List<FcPredrawRushDtlDTO> queryByPredrawDtlIdAndStrikeInterestAndOrderNo(List<String> predrawDtlId, List<String> strikeInterest, List<String> orderNo){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dtl.* from fc_predraw_rush_dtl dtl ");
        sql.append(" where dtl.predraw_dtl_id in (:PredrawDtlId)");
        sql.append(" and  dtl.strike_interest in (:strikeInterest)");
        sql.append(" and  dtl.order_no in (:orderNo) and dtl.status in (:status) ");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("PredrawDtlId", predrawDtlId);
        params.put("strikeInterest", strikeInterest);
        params.put("orderNo", orderNo);
        params.put("status", new Integer[]{InterestConstant.INTEREST_WAIT_CHECK, InterestConstant.INTEREST_STATUE_TEMP, InterestConstant.INTEREST_BACK});

        return listBySQLAliasToBean(sql.toString(), params, FcPredrawRushDtlDTO.class);

    }

}
