package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.dao.CrdScoringIndicatorValueDao;
import com.bytter.biz.creditRating.entity.CrdScoringIndicatorValue;
import com.bytter.biz.creditRating.enums.CreditRatingEnum;
import com.bytter.biz.creditRating.vo.CrdRawDataVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.exception.BusinessException;

import com.bytter.system.api.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("scoringIndicatorValueService")
public class CrdScoringIndicatorValueService extends BaseService<CrdScoringIndicatorValue, String> implements IWorkFlowListener, ITaskService {

    @Autowired
    private CrScoringIndicatorService scoringIndicatorService;

    @Autowired
    private CrdRawDataService rawDataService;

    public void save(List<String> scoringIds, List<String> indicatorIds, List<String> rawDataIds, ICurrentUser user){
        List<CrdRawDataVo> crdRawDataVos = rawDataService.queryByIds(rawDataIds);
        if (crdRawDataVos == null || crdRawDataVos.isEmpty()){
            throw new BusinessException(CreditRatingEnum.CREDIT_RATING_RAWDATA_NULL);
        }
        crdRawDataVos.stream().forEach(vo ->{
            CrdScoringIndicatorValue value = new CrdScoringIndicatorValue();
            value.setInstitutionalRawDataId(vo.getId());

        });
    }
    @Autowired
    private CrdScoringIndicatorValueDao scoringIndicatorValueDao;

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void run(String arguments) {

    }

    @Override
    public BaseDao<CrdScoringIndicatorValue, String> getEntityDao() {
        return scoringIndicatorValueDao;
    }
}
