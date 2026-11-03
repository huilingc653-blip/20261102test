package com.bytter.biz.finacial.paramconfig.service;

import com.bytter.biz.finacial.paramconfig.dao.FcNisBusTypeRuleDao;
import com.bytter.biz.finacial.paramconfig.entity.FcNisBusTypeRule;
import com.bytter.biz.finacial.paramconfig.vo.FcNisBusTypeRuleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lijinquan
 * @description:
 * @date 2024-10-29 16:52
 */
@Service("fcNisBusTypeRuleService")
@Transactional
public class FcNisBusTypeRuleService extends BaseService<FcNisBusTypeRule, String> {

    @Autowired
    private FcNisBusTypeRuleDao fcNisBusTypeRuleDao;

    @Override
    public BaseDao<FcNisBusTypeRule, String> getEntityDao() {
        return fcNisBusTypeRuleDao;
    }

    /**
     * 根据业务id获取所有业务规则
     * @param busId
     * @return
     */
    public List<FcNisBusTypeRuleVO> getByByBusId(String busId){
        return fcNisBusTypeRuleDao.getByByBusId(busId);
    }

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteNisBusType(String busId) {
        //删除匹配规则
        fcNisBusTypeRuleDao.deleteByBusId(busId);
    }

}
