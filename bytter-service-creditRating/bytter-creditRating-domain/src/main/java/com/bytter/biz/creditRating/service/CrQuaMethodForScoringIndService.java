package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.dao.CrQuaMethodForScoringIndDao;
import com.bytter.biz.creditRating.entity.CrQuaMethodForScoringInd;
import com.bytter.biz.creditRating.vo.CrQuaMethodForScoringIndVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("crQuaMethodForScoringIndService")
@Transactional
public class CrQuaMethodForScoringIndService extends BaseService<CrQuaMethodForScoringInd, String> {

    @Autowired
    private CrQuaMethodForScoringIndDao crQuaMethodForScoringIndDao;

    @Override
    public BaseDao<CrQuaMethodForScoringInd, String> getEntityDao() {
        return crQuaMethodForScoringIndDao;
    }

    public List<CrQuaMethodForScoringIndVo> batchSelectBySids(List<String> sidList) {
        return crQuaMethodForScoringIndDao.batchSelectBySids(sidList);
    }

    /**
     * 通过评分指标id查询字段对应值
     * @param sid
     * @return
     */
    public List<CrQuaMethodForScoringIndVo> getBySid(String sid) {
        return crQuaMethodForScoringIndDao.getBySid(sid);
    }
}
