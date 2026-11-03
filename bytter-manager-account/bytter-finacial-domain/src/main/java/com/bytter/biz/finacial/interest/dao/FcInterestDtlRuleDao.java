package com.bytter.biz.finacial.interest.dao;

import com.bytter.biz.finacial.interest.entity.FcInterestDtlRule;
import com.bytter.biz.finacial.interest.vo.FcInterestDtlRuleVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 利息流水规则Dao。
 *
 * @author zhaohc
 * @date 2024/4/29 17:37
 */
@Repository
public class FcInterestDtlRuleDao extends BaseDao<FcInterestDtlRule, String> {

    /**
     * 查询分页列表数据。
     *
     * @param input 查询条件。
     * @return 分页数据。
     */
    public DataTablesOutput<FcInterestDtlRuleVo> listInterestRule(DataTablesInput input) {
        String sql = "select fidr.id,fidr.bank_type_id as bankTypeId,fidr.bank_name as bankName,fidr.remark as remark,fidr.trans_time as transTime" +
                " ,fidr.valid_sign as validSign  from fc_interest_dtl_rule fidr " +
                " where 1 = 1 ";
        return getDynamicBySQL(input, sql, FcInterestDtlRuleVo.class, "fidr", false);
    }
}
