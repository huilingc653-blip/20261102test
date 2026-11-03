package com.bytter.guarantee.domain.service;
import com.bytter.guarantee.domain.dao.ConWarnRulesDao;
import com.bytter.guarantee.domain.entity.ConWarnRules;
import com.bytter.guarantee.domain.vo.ConWarnRulesVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @Title: ConWarnRulesService
 * @Description: 合同预警规则Service
 * @author wangzy
 * @date 2019/11/4 15:21
 *
 */
@Service("conWarnRulesService")
@Transactional
public class ConWarnRulesService extends BaseService<ConWarnRules, String> {
    @Autowired
    private ConWarnRulesDao conWarnRulesDao;
    @Override
    public BaseDao<ConWarnRules, String> getEntityDao() {
        return conWarnRulesDao;
    }
    /**
     * 查询合同退款预警规则
     * 
     * @return 合同退款预警规则list
     *
     */
    @Transactional(readOnly = true)
    public List<ConWarnRulesVO> listConWarnRulesVO() {
        return conWarnRulesDao.listConWarnRulesVO();
    }
    /**
     * @Title: 合同预警规则保存 @param infoVO 合同预警规则数据信息 @param user 用户 @return 处理结果 @throws
     */
    public boolean saveConWarnRules(ConWarnRulesVO infoVO, ICurrentUser user) {
        // 在保存合同预警信息之前将原有的全部删除
        conWarnRulesDao.deleteConWarnRules();
        // 合同预警规则进行保存
        if (infoVO != null && StringUtils.isNotEmpty(infoVO.getConTypeIds())) {
            ConWarnRules conWarnRules = null;
            for (int i = 0; i < infoVO.getConTypeIds().length; i++) {
                ConWarnRulesVO conWarnRulesVO = new ConWarnRulesVO(infoVO.getConTypeIds()[i], infoVO.getBeforDays()[i],
                        infoVO.getIntervalDays()[i], infoVO.getValidSigns()[i]);
                conWarnRules = new ConWarnRules();
                conWarnRules.setOpDate(DateUtils.getDate());
                conWarnRules.setOpId(user.getId());
                BeanUtils.copyNotNullProperties(conWarnRulesVO, conWarnRules);
                this.saveEntity(conWarnRules);
            }
        }
        return true;
    }
    /**
     * 查询需要合同预警提醒的合同信息
     * 
     * @return 所有合同预警消息
     *
     */
    @Transactional(readOnly = true)
    public List<ConWarnRulesVO> listConWarnInfoVO() {
        return conWarnRulesDao.listConWarnInfoVO();
    }
}
