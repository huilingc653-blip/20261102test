package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexSettleFillRuleAddDao;
import com.bytter.biz.foreign.entity.FexSettleFillRuleAdd;
import com.bytter.biz.foreign.vo.FexSettleFillRuleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luxy
 * @Title: FexSettleFillRuleAddService
 * @ProjectName T3(3.2)
 * @Description: 结算自动填充规则补充服务层
 * @date 2020/2/19 11:28
 */
@Service("FexSettleFillRuleAddService")
public class FexSettleFillRuleAddService extends BaseService<FexSettleFillRuleAdd,String> {
    @Autowired
    private FexSettleFillRuleAddDao fexSettleFillRuleAddDao;
    @Override
    public BaseDao<FexSettleFillRuleAdd, String> getEntityDao() {
        return fexSettleFillRuleAddDao;
    }

    /**
     * 根据规则id查询规则补充数据
     * @param ruleId
     * @return
     */
    public List<FexSettleFillRuleAdd> getRuleAddByRuleId(String ruleId){
        return fexSettleFillRuleAddDao.getRuleAddByRuleId(ruleId);
    }

    /**
     * 保存规则补充数据
     * @param addList
     * @param ruleId
     */
    public void saveRuleAdd(List<FexSettleFillRuleAdd> addList,String ruleId){
        if(null!=addList&&addList.size()>0){
            for (FexSettleFillRuleAdd ruleAdd:addList){
                if(null==ruleAdd.getFieldName()|| StringUtils.isBlank(ruleAdd.getFieldName())){
                    continue;
                }
                ruleAdd.setRuleId(ruleId);
                this.saveEntity(ruleAdd);
            }
        }
    }

    /**
     * 根据规则id删除规则补充数据
     * @param ruleId
     */
    public void deleteRuleAdd(String ruleId){
        fexSettleFillRuleAddDao.deleteRuleAddByRuleId(ruleId);
    }


    /**
     * 根据规则条件查询规则补充数据
     * @param searchVo
     * @return
     */
    public List<FexSettleFillRuleAdd> listRuleAddByRule(FexSettleFillRuleVO searchVo){
        return fexSettleFillRuleAddDao.listRuleAddByRule(searchVo);
    }

}
