package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrmModelFormula;
import com.bytter.biz.creditRating.vo.CrmModelFormulaVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2024/8/14<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Repository
public class CrmModelFormulaDao extends BaseDao<CrmModelFormula, String> {
    /**
     * 功能介绍 :查询所有CrmModelFormulaVO对象<br>
     */
    public List<CrmModelFormulaVO> getCrmModelFormulaVOs() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select f.*");
        sql.append(" from MODEL_FORMULA f ");
        return this.listBySQLAliasToBean(sql.toString(), null, CrmModelFormulaVO.class);
    }
}
