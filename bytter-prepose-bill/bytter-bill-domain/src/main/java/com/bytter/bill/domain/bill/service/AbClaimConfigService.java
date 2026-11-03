package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 票据配置服务类
 *
 * @author mof
 */
@Service("abillClaimConfigService")
@Transactional
public class AbClaimConfigService extends BaseService<String, AbAccrecords> {
    @Override
    public BaseDao<String, AbAccrecords> getEntityDao() {
        return null;
    }
}
