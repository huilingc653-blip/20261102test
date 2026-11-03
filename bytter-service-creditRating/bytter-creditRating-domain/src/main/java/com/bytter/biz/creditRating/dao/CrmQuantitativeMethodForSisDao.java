package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrmQuantitativeMethodForSis;
import com.bytter.biz.creditRating.vo.CrmQuantitativeMethodForSisVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2024/8/16<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Repository
public class CrmQuantitativeMethodForSisDao extends BaseDao<CrmQuantitativeMethodForSis,String> {

    /**
     * 根据指标固化表id取对应的定量表数据
     * @param sidList
     * @return
     */
    public List<CrmQuantitativeMethodForSisVO> batchSelectBySids(List<String> sidList) {
        Map<String,Object> param = new HashMap<>();
        String sql = " select qmf.ID as id,qmf.SCORING_INDICATOR_SOL_ID as scoringIndicatorSolId,qmf.SCORING_INDICATOR_CODE as scoringIndicatorCode,qmf.SCORING_INDICATOR_NAME as scoringIndicatorName," +
                " qmf.SCORING_INDICATOR_DESCRIPTION as scoringIndicatorDescription,qmf.CATEGORY_ENCODING as categoryEnCoding,qmf.CATEGORY_NAME as categoryName,qmf.VALUE as value,qmf.QUANTITATIVE_METHOD_FOR_SI_ID as quantitativeMethodForSiId," +
                " qmf.OTHERVALUE as otherValue,qmf.VALUE_NAME as valueName,qmf.VALUE_NAME_CODE as valueNameCode,qmf.VALUE_DIRECTION AS valueDirection,qmf.OTHERVALUE_DIRECTION as otherValueDirection,qmf.GRADE as grade," +
                " qmf.VERSION_NUMBER as versionNumber,qmf.CREATOR as creator,qmf.CREATE_DATE as createDate from QUANTITATIVE_METHOD_FOR_SIS qmf  " +
                " where " + getSQLIn(sidList, 900, "qmf.SCORING_INDICATOR_SOL_ID");
        return listBySQLAliasToBean(sql,param, CrmQuantitativeMethodForSisVO.class);
    }

    /**
     * 根据计算模型获取所有对应的固化指标数据的定量表数据
     * @param modelId
     * @return
     */
    public List<CrmQuantitativeMethodForSisVO>  getCrmQuantitativeByModelId(String modelId){
        Map<String,Object> param = new HashMap<>();
        String sql = " select qmf.* " +
                " from QUANTITATIVE_METHOD_FOR_SIS qmf" +
                " left join SCORING_INDICATOR_SOLIDIFICATI sis on sis.id=qmf.scoring_Indicator_sol_Id" +
                " where sis.computational_model_id=:modelId";
        param.put("modelId",modelId);
        return listBySQLAliasToBean(sql,param, CrmQuantitativeMethodForSisVO.class);
    }

    /**
     * 根据固化指标id获取对应的定量数据
     */
    public List<CrmQuantitativeMethodForSisVO>  getCrmQuantitativeBySid(String sid){
        Map<String,Object> param = new HashMap<>();
        String sql = " select qmf.ID as id,qmf.SCORING_INDICATOR_SOL_ID as scoringIndicatorSolId,qmf.SCORING_INDICATOR_CODE as scoringIndicatorCode,qmf.SCORING_INDICATOR_NAME as scoringIndicatorName," +
                " qmf.SCORING_INDICATOR_DESCRIPTION as scoringIndicatorDescription,qmf.CATEGORY_ENCODING as categoryEnCoding,qmf.CATEGORY_NAME as categoryName,qmf.VALUE as value,qmf.QUANTITATIVE_METHOD_FOR_SI_ID as quantitativeMethodForSiId," +
                " qmf.OTHERVALUE as otherValue,qmf.VALUE_NAME as valueName,qmf.VALUE_NAME_CODE as valueNameCode,qmf.VALUE_DIRECTION AS valueDirection,qmf.OTHERVALUE_DIRECTION as otherValueDirection,qmf.GRADE as grade," +
                " qmf.VERSION_NUMBER as versionNumber,qmf.CREATOR as creator,qmf.CREATE_DATE as createDate from QUANTITATIVE_METHOD_FOR_SIS qmf  " +
                " where qmf.SCORING_INDICATOR_SOL_ID=:sid";
        param.put("sid",sid);
        return listBySQLAliasToBean(sql,param, CrmQuantitativeMethodForSisVO.class);
    }
}
