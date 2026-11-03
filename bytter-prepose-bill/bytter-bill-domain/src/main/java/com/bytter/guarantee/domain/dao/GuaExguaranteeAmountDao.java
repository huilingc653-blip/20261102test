package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.entity.GuaExguaranteeAmount;
import com.bytter.guarantee.domain.vo.GuaExguaranteeAmountVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
@Repository
public class GuaExguaranteeAmountDao extends BaseDao<GuaExguaranteeAmount, String> {
    /**
     * 查询信息列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct g.* ");
        sql.append(" from GUA_EXGUARANTEE_AMOUNT g  ");
        if (!EmptyUtils.isEmpty(input.getFormdata().get("likes_corpId"))) {
            String corpId = input.getFormdata().get("likes_corpId").toString();
            if (!EmptyUtils.isBlank(corpId)) {
                sql.append(" left join GUA_EXGUARANTEE_CORP gc on g.id = gc.EX_AMOUNT_ID ");
            }
        }
        return getDynamicBySQL(input, sql.toString(), GuaExguaranteeAmountVO.class, "", true);
    }
}
