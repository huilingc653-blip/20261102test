package com.bytter.bill.domain.bill.service;

import com.bytter.bill.domain.bill.dao.AbBillRuleBillDtlRmkDtlDao;
import com.bytter.bill.domain.bill.entity.AbBillRuleBillDtlRmk;
import com.bytter.bill.domain.bill.entity.AbBillRuleBillDtlRmkDtl;
import com.bytter.bill.domain.bill.vo.AbBillRuleBillDtlRmkDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhous
 * @Title: Service
 * @Description: 票据流水备注规则明细
 * @date 2025-02-24
 */
@Service("AbBillRuleBillDtlRmkDtlService")
@Transactional
public class AbBillRuleBillDtlRmkDtlService extends BaseService<AbBillRuleBillDtlRmkDtl, String> {

    @Autowired
    private AbBillRuleBillDtlRmkDtlDao abBillRuleBillDtlRmkDtlDao;

    @Override
    public BaseDao<AbBillRuleBillDtlRmkDtl, String> getEntityDao() {
        return abBillRuleBillDtlRmkDtlDao;
    }

    /**
    * @Description 根据业务id删除对应明细
    * @author zhous
    * @date 2025/2/25 8:48
    */
    public void deleteByBusId(String id) {
        abBillRuleBillDtlRmkDtlDao.deleteByBusId(id);
    }

    /**
    * @Description 根据业务id获取对应明细
    * @author zhous
    * @date 2025/2/25 8:49
    */
    public List<AbBillRuleBillDtlRmkDtlVO> getByByBusId(String id) {
        return abBillRuleBillDtlRmkDtlDao.getByByBusId(id);
    }
}
