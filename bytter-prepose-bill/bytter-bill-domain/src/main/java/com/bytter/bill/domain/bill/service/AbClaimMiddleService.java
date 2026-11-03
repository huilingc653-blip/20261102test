package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbClaimMiddleDao;
import com.bytter.bill.domain.bill.entity.AbClaimMiddle;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 票据认领中间表service
 *
 * @author ouyj
 */
@Service("AbClaimMiddleService")
@Transactional(rollbackFor = Exception.class)
public class AbClaimMiddleService extends BaseService<AbClaimMiddle, String> {
    @Autowired
    private AbClaimMiddleDao abClaimMiddleDao;
    @Override
    public BaseDao<AbClaimMiddle, String> getEntityDao() {
        return abClaimMiddleDao;
    }
    /**
     * 根据id数组修改认领信息
     *
     * @param ids           id数组
     * @param abClaimMiddle 认领对象
     * @return 更新条数
     */
    public int updateByIds(String[] ids, AbClaimMiddle abClaimMiddle) {
        return abClaimMiddleDao.updateByIds(ids, abClaimMiddle);
    }
}
