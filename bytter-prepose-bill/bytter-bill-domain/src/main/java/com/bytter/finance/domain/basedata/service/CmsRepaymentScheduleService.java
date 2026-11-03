package com.bytter.finance.domain.basedata.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.finance.domain.basedata.dao.CmsRepaymentScheduleDao;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
 * @Title: Service
 * @Description: 还款计划主表
 * @author songkk
 * @date 2017-08-01 09:58:43
 * @version 1.0
 *
 */
@Service("cmsRepaymentScheduleService")
@Transactional
public class CmsRepaymentScheduleService extends BaseService<CmsRepaymentSchedule, String> {
    @Autowired
    private CmsRepaymentScheduleDao cmsRepaymentScheduleDao;
    /**
     * 根据id删除
     * 
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存还款计划
     * 
     * @param rp
     */
    public void save(CmsRepaymentSchedule entity) {
        super.saveEntity(entity);
    }
    @Override
    public BaseDao<CmsRepaymentSchedule, String> getEntityDao() {
        // TODO Auto-generated method stub
        return cmsRepaymentScheduleDao;
    }
    @Transactional(readOnly = true)
    public CmsRepaymentSchedule findById(String id) {
        return cmsRepaymentScheduleDao.findById(id);
    }
}
