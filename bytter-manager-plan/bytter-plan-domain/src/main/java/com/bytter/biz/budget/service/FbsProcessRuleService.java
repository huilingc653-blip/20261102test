package com.bytter.biz.budget.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.budget.dao.FbsProcessRuleDao;
import com.bytter.biz.budget.entity.FbsProcessRule;
import com.bytter.biz.budget.vo.FbsProcessRuleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 资金计划执行规则表
 * @date 2020-08-31 14:51:48
 */
@Service("fbsProcessRuleService")
@Transactional
public class FbsProcessRuleService extends BaseService<FbsProcessRule, String> {
    @Autowired
    private FbsProcessRuleDao fbsProcessRuleDao;
    @Override
    public BaseDao<FbsProcessRule, String> getEntityDao() {
        return fbsProcessRuleDao;
    }
    /**
     * 批量更新有效标志
     * @param ids
     * @param validSign
     */
    public void batchUpdateValidSign(String ids, String validSign){
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FbsProcessRule entity = this.get(id);
            entity.setValidSign(validSign);
            this.saveOrUpdate(entity);
        }
    }
    /**
     * 判断是否存在执行规则
     *
     * @param processRule
     * @param id
     * @return
     */
    public FbsProcessRuleVO isExistProcessRule(String processRule, String id) {
        List<FbsProcessRuleVO> list = this.fbsProcessRuleDao.isExistProcessRule(processRule, id);
        if (EmptyUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 判断是否存在执行规则
     *
     * @param sort
     * @param id
     * @return
     */
    public FbsProcessRuleVO isExistSort(Integer sort, String id) {
        List<FbsProcessRuleVO> list = this.fbsProcessRuleDao.isExistSort(sort, id);
        if (EmptyUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 获取有效的执行规则
     *
     * @return
     */
    public List<FbsProcessRuleVO> listValidProcessRule() {
        return this.fbsProcessRuleDao.listValidProcessRule();
    }
}
