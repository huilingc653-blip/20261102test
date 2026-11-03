package com.bytter.credit.domain.service;
import java.util.List;
import com.bytter.credit.domain.dao.RatUrationApplyDtlDao;
import com.bytter.credit.domain.entity.RatUrationApplyDtl;
import com.bytter.credit.dto.RatUrationApplyDtlDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
 * @Title: Service
 * @Description: 授信占用明细表
 * @author onlineGenerator
 * @date 2017-08-22 17:45:20
 * @version 1.0
 *
 */
@Service("ratUrationApplyDtlService")
@Transactional
public class RatUrationApplyDtlService extends BaseService<RatUrationApplyDtl, String> {
    @Autowired
    private RatUrationApplyDtlDao ratUrationApplyDtlDao;
    /**
     * 删除对象
     * 
     * @param entity
     */
    public void deleteEntity(RatUrationApplyDtl entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     * 
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     * 
     * @param entity
     */
    public void save(RatUrationApplyDtl entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     * 
     * @param entity
     */
    public void update(RatUrationApplyDtl entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * 
     * @param entity
     */
    public void saveOrUpdate(RatUrationApplyDtl entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<RatUrationApplyDtl, String> getEntityDao() {
        return ratUrationApplyDtlDao;
    }
    /**
     * 功能介绍 : 根据占用Id查询综合占用详情。<br>
     * <b>日期：</b>2017年8月23日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatUrationApplyDtlDTO> queryRatUrationById(String urationId) {
        return ratUrationApplyDtlDao.queryRatUrationById(urationId);
    }
    /**
     * 功能介绍 : 根据占用申请Id查询综合占用详情。<br>
     * <b>日期：</b>2017年8月23日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatUrationApplyDtlDTO> queryRatUrationByApplyId(String urationId) {
        return ratUrationApplyDtlDao.queryRatUrationByApplyId(urationId);
    }
    /**
     * 功能介绍 : 根据占用Id查询综合占用实体。<br>
     * <b>日期：</b>2017年8月23日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatUrationApplyDtl> queryRatUrationDtlById(String urationId) {
        return ratUrationApplyDtlDao.queryRatUrationDtlById(urationId);
    }
    /**
     * 功能介绍 : 删除原授信综合占用明细。<br>
     * <b>日期：</b>2017年8月24日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void deleteRatUrationDtlByCode(String urationId) {
        ratUrationApplyDtlDao.deleteRatUrationDtlByCode(urationId);
    }
}
