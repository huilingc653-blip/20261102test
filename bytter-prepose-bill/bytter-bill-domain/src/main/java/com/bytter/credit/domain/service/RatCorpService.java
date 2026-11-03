package com.bytter.credit.domain.service;
import java.util.List;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.credit.domain.dao.RatCorpDao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.dto.CorpDTO;
/**
 * 操作单位集团统一授信主体标识Service
 *
 * @author liyh
 *
 */
@Service("ratCorpService")
@Transactional
public class RatCorpService extends BaseService<CorpDTO, String> {
    @Autowired
    private RatCorpDao corpDao;
    @DubboReference
    private ICorpService corpService;
    @Override
    public BaseDao<CorpDTO, String> getEntityDao() {
        return corpDao;
    }
    /**
     * 功能介绍 : 更新单位集团统一授信主体标识。<br>
     * <b>日期：</b>2017年7月27日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRatGroup(List<String> corpIds) {
        corpService.resetRatGroup();
        if (null != corpIds && !corpIds.isEmpty()) {
            corpService.updateRatGroup(corpIds);
        }
    }
}
