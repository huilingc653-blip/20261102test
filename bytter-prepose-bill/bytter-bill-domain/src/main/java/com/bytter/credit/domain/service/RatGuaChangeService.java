package com.bytter.credit.domain.service;
import com.bytter.credit.domain.entity.RatBkration;
import com.bytter.credit.domain.dao.RatGuaChangeDao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author luxy
 * @Title: RatGuaChangeService
 * @ProjectName T2_IDEA
 * @Description: 银行授信担保变更业务逻辑层
 * @date 2019/5/5 16:21
 */
@Service("RatGuaChangeService")
public class RatGuaChangeService extends BaseService<RatBkration, String> {
    @Autowired
    private RatGuaChangeDao ratGuaChangeDao;
    @Override
    public BaseDao<RatBkration, String> getEntityDao() {
        return ratGuaChangeDao;
    }
}
