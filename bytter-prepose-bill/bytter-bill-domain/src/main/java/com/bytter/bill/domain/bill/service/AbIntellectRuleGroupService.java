package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbIntellectRuleGroupDao;
import com.bytter.bill.domain.bill.entity.AbIntellectRuleGroup;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleGroupVO;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * 智能选票规则组处理service
 *
 * @author ouyj
 */
@Service("abIntellectRuleGroupService")
@Transactional(rollbackFor = Exception.class)
public class AbIntellectRuleGroupService extends BaseService<AbIntellectRuleGroup, String> {
    @Autowired
    private AbIntellectRuleGroupDao abIntellectRuleGroupDao;
    @Autowired
    private AbIntellectRuleGroupDtlService abIntellectRuleGroupDtlService;
    @Override
    public BaseDao<AbIntellectRuleGroup, String> getEntityDao() {
        return abIntellectRuleGroupDao;
    }
    /**
     * 查询选票规则列表数据
     *
     * @param input 前端传参
     * @return 选票规则列表数据
     */
    public DataTablesOutput<AbIntellectRuleGroupVO> datatable(DataTablesInput input) {
        return abIntellectRuleGroupDao.datatable(input);
    }
    /**
     * 根据id查询有效的选票规则
     *
     * @param id 主键id
     * @return 选票规则
     */
    public AbIntellectRuleGroupVO getAbIntellectRuleGroupVOById(String id) {
        return abIntellectRuleGroupDao.getAbIntellectRuleGroupVOById(id);
    }
    /**
     * 根据id查询选票规则
     *
     * @param id 主键id
     * @return 选票规则
     */
    public AbIntellectRuleGroupVO getAllAbIntellectRuleGroupVOById(String id) {
        return abIntellectRuleGroupDao.getAllAbIntellectRuleGroupVOById(id);
    }
    /**
     * 保存选票规则
     *
     * @param abIntellectRuleGroupVO 选票规则vo
     * @param user                   当前用户
     */
    public void saveRuleGroup(AbIntellectRuleGroupVO abIntellectRuleGroupVO, ICurrentUser user) {
        // 校验名称、编码是否已存在
        verifyCodeAndName(abIntellectRuleGroupVO);
        // 修改规则明细表的规则编码
        abIntellectRuleGroupDtlService.updateRuleGroupCodeByApplyNo(abIntellectRuleGroupVO.getApplyNo(), abIntellectRuleGroupVO.getRuleGroupCode());
        AbIntellectRuleGroup abIntellectRuleGroup = BeanUtils.copyNewInstanceProperties(abIntellectRuleGroupVO, AbIntellectRuleGroup.class);
        abIntellectRuleGroup.setCreateBy(user.getId());
        abIntellectRuleGroup.setCreateName(user.getUsername());
        abIntellectRuleGroup.setCreateDate(new Date());
        this.saveEntity(abIntellectRuleGroup);
    }
    /**
     * 编辑选票规则
     *
     * @param abIntellectRuleGroupVO 选票规则vo
     * @param user                   当前用户
     */
    public void updateRuleGroup(AbIntellectRuleGroupVO abIntellectRuleGroupVO, ICurrentUser user) {
        // 校验名称、编码是否已存在
        verifyCodeAndName(abIntellectRuleGroupVO);
        // 修改规则明细表的规则编码
        abIntellectRuleGroupDtlService.updateRuleGroupCodeByApplyNo(abIntellectRuleGroupVO.getApplyNo(), abIntellectRuleGroupVO.getRuleGroupCode());
        AbIntellectRuleGroup abIntellectRuleGroup = BeanUtils.copyNewInstanceProperties(abIntellectRuleGroupVO, AbIntellectRuleGroup.class);
        abIntellectRuleGroup.setUpdateBy(user.getId());
        abIntellectRuleGroup.setUpdateName(user.getUsername());
        abIntellectRuleGroup.setUpdateDate(new Date());
        this.updateEntity(abIntellectRuleGroup);
    }
    /**
     * 校验规则编码、名称是否已存在
     *
     * @param abIntellectRuleGroupVO 规则vo
     */
    public void verifyCodeAndName(AbIntellectRuleGroupVO abIntellectRuleGroupVO) {
        AbIntellectRuleGroupVO ruleVO = abIntellectRuleGroupDao.queryByRuleGroupCode(abIntellectRuleGroupVO.getId(),
                abIntellectRuleGroupVO.getRuleGroupCode());
        if (ruleVO != null) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_INTELLECT_RULE_GROUP_CODE_IS_EXISTS);
        }
        ruleVO = abIntellectRuleGroupDao.queryByRuleGroupName(abIntellectRuleGroupVO.getId(),
                abIntellectRuleGroupVO.getRuleGroupName());
        if (ruleVO != null) {
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_INTELLECT_RULE_GROUP_NAME_IS_EXISTS);
        }
    }
    /**
     * 根据规则组id删除规则组以及对应的规则组明细数据
     *
     * @param id 规则组id
     */
    public void delRuleGroup(String id) {
        String ruleGroupCode = abIntellectRuleGroupDao.get(id).getRuleGroupCode();
        abIntellectRuleGroupDao.delete(id);
        abIntellectRuleGroupDao.delDtlByRuleGroupCode(ruleGroupCode);
    }
    /**
     * <b>功能：</b>获取有效的选票规则组<br>
     * <b>日期：</b> 2022-10-11 <br>
     */
    public List<AbIntellectRuleGroupVO> listAbIntellectRuleGroup() {
        return abIntellectRuleGroupDao.listAbIntellectRuleGroup();
    }
}
