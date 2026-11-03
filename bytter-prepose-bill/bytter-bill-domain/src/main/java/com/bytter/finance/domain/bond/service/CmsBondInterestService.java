package com.bytter.finance.domain.bond.service;
import com.bytter.finance.domain.bond.dao.CmsBondInterestDao;
import com.bytter.finance.domain.bond.entity.CmsBondInterest;
import com.bytter.finance.domain.bond.vo.CmsBondInterestVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: Service
 * @Description: 债券利息预计明细表
 * @date 2022-06-08 10:07:46
 */
@Service("cmsBondInterestService")
@Transactional
public class CmsBondInterestService extends BaseService<CmsBondInterest, String> {
    @Autowired
    private CmsBondInterestDao cmsBondInterestDao;
    @Override
    public BaseDao<CmsBondInterest, String> getEntityDao() {
        return cmsBondInterestDao;
    }
    public List<CmsBondInterestVo> getByRegisterId(String registerId) {
        List<CmsBondInterestVo> list = cmsBondInterestDao.getByRegisterId(registerId);
        return list;
    }
}
