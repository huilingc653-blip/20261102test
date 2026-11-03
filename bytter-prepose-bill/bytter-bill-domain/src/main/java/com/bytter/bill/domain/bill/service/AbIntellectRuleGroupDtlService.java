package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbIntellectRuleGroupDtlDao;
import com.bytter.bill.domain.bill.entity.AbIntellectRuleGroupDtl;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleGroupDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 智能选票规则组明细处理service
 *
 * @author ouyj
 */
@Service("abIntellectRuleGroupDtlService")
@Transactional(rollbackFor = Exception.class)
public class AbIntellectRuleGroupDtlService extends BaseService<AbIntellectRuleGroupDtl, String> {
    @Autowired
    private AbIntellectRuleGroupDtlDao abIntellectRuleGroupDtlDao;
    @Override
    public BaseDao<AbIntellectRuleGroupDtl, String> getEntityDao() {
        return abIntellectRuleGroupDtlDao;
    }
    /**
     * <b>功能：</b>获取有效的选票规则组明细<br>
     * <b>日期：</b> 2022-10-11 <br>
     */
    public List<AbIntellectRuleGroupDtlVO> listAbIntellectRuleGroupDtl() {
        return abIntellectRuleGroupDtlDao.listAbIntellectRuleGroupDtl();
    }
    /**
     * 查询选票规则组明细列表数据
     *
     * @param input 前端传参
     * @return 选票规则组明细列表数据
     */
    public DataTablesOutput datatable(DataTablesInput input) {
        return abIntellectRuleGroupDtlDao.datatable(input);
    }
    /**
     * 根据id查询选票规则组明细
     *
     * @param id 主键id
     * @return 选票规则组明细
     */
    public AbIntellectRuleGroupDtlVO getAbIntellectRuleGroupDtlVOById(String id) {
        return abIntellectRuleGroupDtlDao.getAbIntellectRuleGroupDtlVOById(id);
    }
    /**
     * 保存选票规则组明细
     *
     * @param abIntellectRuleGroupDtlVO 选票规则组明细vo
     * @param user                      当前用户
     */
    public void saveRuleGroupDtl(AbIntellectRuleGroupDtlVO abIntellectRuleGroupDtlVO, ICurrentUser user) {
        // 批量新增的规则id
        String ruleIds = abIntellectRuleGroupDtlVO.getRuleIds();
        // 批量新增的规则name
        String ruleNames = abIntellectRuleGroupDtlVO.getRuleNames();
        // 申请单号
        String applyNo = abIntellectRuleGroupDtlVO.getApplyNo();
        if (StringUtils.isEmpty(ruleIds) || StringUtils.isEmpty(ruleNames) || StringUtils.isEmpty(applyNo)) {
            return;
        }
        // 计算当前规则组的顺序
        List<AbIntellectRuleGroupDtlVO> ruleDtlVOList = abIntellectRuleGroupDtlDao.listRuleGroupDtlByApplyNo(abIntellectRuleGroupDtlVO.getApplyNo());
        int serialNo = CollectionUtils.isEmpty(ruleDtlVOList) ? 1 : ruleDtlVOList.size() + 1;
        String[] ruleIdArr = ruleIds.split(CommonConstant.COMMA_SPLIT);
        String[] ruleNameArr = ruleNames.split(CommonConstant.COMMA_SPLIT);
        // 新增的规则组明细信息
        ArrayList<AbIntellectRuleGroupDtl> ruleGroupDtlList = new ArrayList<>();
        for (int i = 0; i < ruleIdArr.length; i++) {
            AbIntellectRuleGroupDtl ruleGroupDtl = new AbIntellectRuleGroupDtl();
            ruleGroupDtl.setApplyNo(applyNo);
            ruleGroupDtl.setRuleId(ruleIdArr[i]);
            ruleGroupDtl.setRuleName(ruleNameArr[i]);
            ruleGroupDtl.setCreateBy(user.getId());
            ruleGroupDtl.setCreateName(user.getUsername());
            ruleGroupDtl.setCreateDate(new Date());
            // 顺序号+1
            ruleGroupDtl.setSerialNo(serialNo++);
            ruleGroupDtlList.add(ruleGroupDtl);
        }
        this.batchSave(ruleGroupDtlList);
    }
    /**
     * 编辑选票规则组明细
     *
     * @param abIntellectRuleGroupDtlVO 选票规则组明细vo
     * @param user                      当前用户
     */
    public void updateRuleGroupDtl(AbIntellectRuleGroupDtlVO abIntellectRuleGroupDtlVO, ICurrentUser user) {
        AbIntellectRuleGroupDtl abIntellectRuleGroupDtl = BeanUtils.copyNewInstanceProperties(abIntellectRuleGroupDtlVO, AbIntellectRuleGroupDtl.class);
        abIntellectRuleGroupDtl.setUpdateBy(user.getId());
        abIntellectRuleGroupDtl.setUpdateName(user.getUsername());
        abIntellectRuleGroupDtl.setUpdateDate(new Date());
        this.updateEntity(abIntellectRuleGroupDtl);
    }
    /**
     * 根据选票规则编码查询对应的选票规则组明细列表
     *
     * @param ruleCode 规则编码
     * @return 选票规则组明细列表
     */
    public List<AbIntellectRuleGroupDtlVO> listRuleGroupDtlByCode(String ruleCode) {
        return abIntellectRuleGroupDtlDao.listRuleGroupDtlByApplyNo(ruleCode);
    }
    /**
     * 根据申请单号修改规则编码
     *
     * @param applyNo       申请单号
     * @param ruleGroupCode 规则编码
     */
    public void updateRuleGroupCodeByApplyNo(String applyNo, String ruleGroupCode) {
        abIntellectRuleGroupDtlDao.updateRuleGroupCodeByApplyNo(applyNo, ruleGroupCode);
    }
    /**
     * 根据申请号删除无用的规则组明细信息(删除规则编码为空的数据)
     *
     * @param applyNo 申请单号
     */
    public void delUselessByApplyNo(String applyNo) {
        abIntellectRuleGroupDtlDao.delUselessByApplyNo(applyNo);
    }
    /**
     * 上移下移规则组明细顺序
     *
     * @param abIntellectRuleGroupDtlVO 选票规则组明细VO
     */
    public void moveUpOrDown(AbIntellectRuleGroupDtlVO abIntellectRuleGroupDtlVO) {
        String updId = abIntellectRuleGroupDtlVO.getId();
        // 上移、下移标志
        Boolean moveFlag = abIntellectRuleGroupDtlVO.getMoveFlag();
        AbIntellectRuleGroupDtl abIntellectRuleGroupDtl = this.get(updId, true);
        // 顺序
        Integer serialNo = abIntellectRuleGroupDtl.getSerialNo();
        // 根据申请号查询对应的所有规则组明细数据
        String applyNo = abIntellectRuleGroupDtlVO.getApplyNo();
        List<AbIntellectRuleGroupDtlVO> abIntellectRuleGroupDtlVOList = abIntellectRuleGroupDtlDao.listRuleGroupDtlByApplyNo(applyNo);
        // 需要上移的数据顺序不为1才需要移动
        boolean moveUp = moveFlag && serialNo != 1;
        // 需要下移的数据顺序不为当前最后一位才需要移动
        boolean moveDown = !moveFlag && serialNo != abIntellectRuleGroupDtlVOList.size();
        ArrayList<AbIntellectRuleGroupDtl> updList = new ArrayList<>();
        if (moveUp) {
            // 设置选中数据上移一位
            abIntellectRuleGroupDtlVOList.get(serialNo - 1).setSerialNo(serialNo - 1);
            // 设置选中数据的前一个数据下移一位
            abIntellectRuleGroupDtlVOList.get(serialNo - 2).setSerialNo(serialNo);
            updList.add(BeanUtils.copyNewInstanceProperties(abIntellectRuleGroupDtlVOList.get(serialNo - 1), AbIntellectRuleGroupDtl.class));
            updList.add(BeanUtils.copyNewInstanceProperties(abIntellectRuleGroupDtlVOList.get(serialNo - 2), AbIntellectRuleGroupDtl.class));
        } else if (moveDown) {
            // 设置选中数据下移一位
            abIntellectRuleGroupDtlVOList.get(serialNo - 1).setSerialNo(serialNo + 1);
            // 设置选中数据的后一个数据上移一位
            abIntellectRuleGroupDtlVOList.get(serialNo).setSerialNo(serialNo);
            updList.add(BeanUtils.copyNewInstanceProperties(abIntellectRuleGroupDtlVOList.get(serialNo - 1), AbIntellectRuleGroupDtl.class));
            updList.add(BeanUtils.copyNewInstanceProperties(abIntellectRuleGroupDtlVOList.get(serialNo), AbIntellectRuleGroupDtl.class));
        }
        this.getSession().clear();
        if (!CollectionUtils.isEmpty(updList)) {
            abIntellectRuleGroupDtlDao.batchUpdate(updList);
        }
    }
    /**
     * 根据规则组明细id删除对应的规则组明细数据
     *
     * @param abIntellectRuleGroupDtlVO 规则组明细vo
     */
    public void delRuleGroupDtl(AbIntellectRuleGroupDtlVO abIntellectRuleGroupDtlVO) {
        // 需要删除的规则明细顺序
        Integer serialNo = abIntellectRuleGroupDtlVO.getSerialNo();
        // 当前申请号对应的所有规则明细数据
        List<AbIntellectRuleGroupDtlVO> ruleDtlList = abIntellectRuleGroupDtlDao.listRuleGroupDtlByApplyNo(abIntellectRuleGroupDtlVO.getApplyNo());
        ArrayList<AbIntellectRuleGroupDtl> updList = new ArrayList<>();
        for (Integer i = serialNo; i < ruleDtlList.size(); i++) {
            ruleDtlList.get(i).setSerialNo(i);
            updList.add(BeanUtils.copyNewInstanceProperties(ruleDtlList.get(i), AbIntellectRuleGroupDtl.class));
        }
        this.getSession().clear();
        this.delete(abIntellectRuleGroupDtlVO.getId());
        if (!CollectionUtils.isEmpty(updList)) {
            abIntellectRuleGroupDtlDao.batchUpdate(updList);
        }
    }
}
