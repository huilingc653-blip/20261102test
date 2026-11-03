package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbIntellectRuleDao;
import com.bytter.bill.domain.bill.entity.AbIntellectRule;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleVO;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * 智能选票规则处理service
 *
 * @author ouyj
 */
@Service("abIntellectRuleService")
@Transactional(rollbackFor = Exception.class)
public class AbIntellectRuleService extends BaseService<AbIntellectRule, String> {
    @Autowired
    private AbIntellectRuleDao abIntellectRuleDao;
    @Autowired
    private AbIntellectRuleDtlService abIntellectRuleDtlService;
    @Override
    public BaseDao<AbIntellectRule, String> getEntityDao() {
        return abIntellectRuleDao;
    }
    /**
     * <b>功能：</b>获取有效的选票规则<br>
     * <b>日期：</b> 2022-10-11 <br>
     */
    public List<AbIntellectRuleVO> listAbIntellectRule() {
        return abIntellectRuleDao.listAbIntellectRule();
    }
    /**
     * 查询选票规则列表数据
     *
     * @param input 前端传参
     * @return 选票规则列表数据
     */
    public DataTablesOutput<AbIntellectRuleVO> datatable(DataTablesInput input) {
        return abIntellectRuleDao.datatable(input);
    }
    /**
     * 根据id查询有效的选票规则
     *
     * @param id 主键id
     * @return 选票规则
     */
    public AbIntellectRuleVO getAbIntellectRuleVOById(String id) {
        return abIntellectRuleDao.getAbIntellectRuleVOById(id);
    }
    /**
     * 根据id查询选票规则(不排除无效数据)
     *
     * @param id 主键id
     * @return 选票规则
     */
    public AbIntellectRuleVO getAllAbIntellectRuleVOById(String id) {
        return abIntellectRuleDao.getAllAbIntellectRuleVOById(id);
    }
    /**
     * 保存选票规则
     *
     * @param abIntellectRuleVO 选票规则vo
     * @param user              当前用户
     */
    public void saveRule(AbIntellectRuleVO abIntellectRuleVO, ICurrentUser user) {
        // 校验名称、编码是否已存在
        verifyCodeAndName(abIntellectRuleVO);
        // 修改规则明细表的规则编码
        abIntellectRuleDtlService.updateRuleCodeByApplyNo(abIntellectRuleVO.getApplyNo(), abIntellectRuleVO.getRuleCode());
        AbIntellectRule abIntellectRule = BeanUtils.copyNewInstanceProperties(abIntellectRuleVO, AbIntellectRule.class);
        abIntellectRule.setCreateBy(user.getId());
        abIntellectRule.setCreateName(user.getUsername());
        abIntellectRule.setCreateDate(new Date());
        this.saveEntity(abIntellectRule);
    }
    /**
     * 编辑选票规则
     *
     * @param abIntellectRuleVO 选票规则vo
     * @param user              当前用户
     */
    public void updateRule(AbIntellectRuleVO abIntellectRuleVO, ICurrentUser user) {
        // 校验名称、编码是否已存在
        verifyCodeAndName(abIntellectRuleVO);
        // 修改规则明细表的规则编码
        abIntellectRuleDtlService.updateRuleCodeByApplyNo(abIntellectRuleVO.getApplyNo(), abIntellectRuleVO.getRuleCode());
        AbIntellectRule abIntellectRule = BeanUtils.copyNewInstanceProperties(abIntellectRuleVO, AbIntellectRule.class);
        abIntellectRule.setUpdateBy(user.getId());
        abIntellectRule.setUpdateName(user.getUsername());
        abIntellectRule.setUpdateDate(new Date());
        this.updateEntity(abIntellectRule);
    }
    /**
     * 根据id删除对应规则
     *
     * @param id 规则id
     */
    public void delRule(String id) {
        if (abIntellectRuleDao.isQuote(id)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_INTELLECT_RULE_IS_QUOTE);
        }
        String ruleCode = abIntellectRuleDao.get(id).getRuleCode();
        abIntellectRuleDao.delete(id);
        abIntellectRuleDtlService.delDtlByRuleCode(ruleCode);
    }
    /**
     * 校验规则编码、名称是否已存在
     *
     * @param abIntellectRuleVO 规则vo
     */
    public void verifyCodeAndName(AbIntellectRuleVO abIntellectRuleVO) {
        AbIntellectRuleVO ruleVO = abIntellectRuleDao.queryByRuleCode(abIntellectRuleVO.getId(),
                abIntellectRuleVO.getRuleCode());
        if (ruleVO != null) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_INTELLECT_RULE_CODE_IS_EXISTS);
        }
        ruleVO = abIntellectRuleDao.queryByRuleName(abIntellectRuleVO.getId(),
                abIntellectRuleVO.getRuleName());
        if (ruleVO != null) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_INTELLECT_RULE_NAME_IS_EXISTS);
        }
    }
}
