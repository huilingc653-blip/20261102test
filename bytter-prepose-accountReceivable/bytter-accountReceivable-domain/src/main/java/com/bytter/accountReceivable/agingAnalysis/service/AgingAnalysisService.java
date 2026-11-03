package com.bytter.accountReceivable.agingAnalysis.service;

import com.bytter.accountReceivable.constant.ResourceConstant;
import com.bytter.accountReceivable.agingAnalysis.dao.AgingAnalysisDao;
import com.bytter.accountReceivable.agingAnalysis.vo.AgingVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 应收款－应收账龄分析查询
 */
@Service("agingAnalysisService")
public class AgingAnalysisService extends BaseService<AgingVo, String> {

    @Autowired
    private AgingAnalysisDao agingAnalysisDao;

    @Override
    public BaseDao<AgingVo, String> getEntityDao() {
        return agingAnalysisDao;
    }

    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 按客商统计应收账龄
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryAging(DataTablesInput input, ICurrentUser currentUser) {
        String corpInSql = getCorpIdInSql(currentUser);
        String dimension = String.valueOf(input.getFormdata().get("dimension"));

        if(dimension.equals("byCus")){
            return agingAnalysisDao.queryAgingByCus(input, corpInSql);
        }
        if(dimension.equals("byBill")){
            return agingAnalysisDao.queryAgingByBill(input, corpInSql);
        }
        if(dimension.equals("byContract")){
            return agingAnalysisDao.queryAgingByContract(input, corpInSql);
        }
        return null;
    }

    private String getCorpIdInSql(ICurrentUser user){
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.ACCOUNT_RECEIVABLE_QUERYDOCMANAGE_QUERY, user.getId());
        return SqlUtil.getInSql(corpIds, "dm.corp_id", true);
    }
}
