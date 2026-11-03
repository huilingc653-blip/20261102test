package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbIntellectRuleDtlDao;
import com.bytter.bill.domain.bill.entity.AbIntellectRuleDtl;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 智能选票规则明细处理service
 *
 * @author ouyj
 */
@Service("abIntellectRuleDtlService")
@Transactional(rollbackFor = Exception.class)
public class AbIntellectRuleDtlService extends BaseService<AbIntellectRuleDtl, String> {
    @Autowired
    private AbIntellectRuleDtlDao abIntellectRuleDtlDao;
    @Override
    public BaseDao<AbIntellectRuleDtl, String> getEntityDao() {
        return abIntellectRuleDtlDao;
    }
    /**
     * <b>功能：</b>获取有效的选票规则明细<br>
     * <b>日期：</b> 2022-10-11 <br>
     */
    public List<AbIntellectRuleDtlVO> listAbIntellectRuleDtl() {
        return abIntellectRuleDtlDao.listAbIntellectRuleDtl();
    }
    /**
     * 查询选票规则明细列表数据
     *
     * @param input 前端传参
     * @return 选票规则明细列表数据
     */
    public DataTablesOutput datatable(DataTablesInput input) {
        return abIntellectRuleDtlDao.datatable(input);
    }
    /**
     * 根据id查询选票规则明细
     *
     * @param id 主键id
     * @return 选票规则明细
     */
    public AbIntellectRuleDtlVO getAbIntellectRuleDtlVOById(String id) {
        return abIntellectRuleDtlDao.getAbIntellectRuleDtlVOById(id);
    }
    /**
     * 保存选票规则明细
     *
     * @param abIntellectRuleDtlVO 选票规则明细vo
     * @param user                 当前用户
     */
    public void saveRuleDtl(AbIntellectRuleDtlVO abIntellectRuleDtlVO, ICurrentUser user) {
        List<AbIntellectRuleDtlVO> ruleDtlVOList = abIntellectRuleDtlDao.listRuleDtlByApplyNo(abIntellectRuleDtlVO.getApplyNo());
        Integer serialNo = CollectionUtils.isEmpty(ruleDtlVOList) ? 1 : ruleDtlVOList.size() + 1;
        AbIntellectRuleDtl abIntellectRuleDtl = BeanUtils.copyNewInstanceProperties(abIntellectRuleDtlVO, AbIntellectRuleDtl.class);
        abIntellectRuleDtl.setCreateBy(user.getId());
        abIntellectRuleDtl.setCreateName(user.getUsername());
        abIntellectRuleDtl.setCreateDate(new Date());
        abIntellectRuleDtl.setSerialNo(serialNo);
        this.saveEntity(abIntellectRuleDtl);
    }
    /**
     * 编辑选票规则明细
     *
     * @param abIntellectRuleDtlVO 选票规则明细vo
     * @param user                 当前用户
     */
    public void updateRuleDtl(AbIntellectRuleDtlVO abIntellectRuleDtlVO, ICurrentUser user) {
        AbIntellectRuleDtl abIntellectRuleDtl = BeanUtils.copyNewInstanceProperties(abIntellectRuleDtlVO, AbIntellectRuleDtl.class);
        abIntellectRuleDtl.setUpdateBy(user.getId());
        abIntellectRuleDtl.setUpdateName(user.getUsername());
        abIntellectRuleDtl.setUpdateDate(new Date());
        this.updateEntity(abIntellectRuleDtl);
    }
    /**
     * 根据选票规则编码查询对应的选票规则明细列表
     *
     * @param ruleCode 规则编码
     * @return 选票规则明细列表
     */
    public List<AbIntellectRuleDtlVO> listRuleDtlByCode(String ruleCode) {
        return abIntellectRuleDtlDao.listRuleDtlByApplyNo(ruleCode);
    }
    /**
     * 根据申请单号修改规则编码
     *
     * @param applyNo  申请单号
     * @param ruleCode 规则编码
     */
    public void updateRuleCodeByApplyNo(String applyNo, String ruleCode) {
        abIntellectRuleDtlDao.updateRuleCodeByApplyNo(applyNo, ruleCode);
    }
    /**
     * 根据申请号删除无用的规则明细信息(删除规则编码为空的数据)
     *
     * @param applyNo 申请单号
     */
    public void delUselessByApplyNo(String applyNo) {
        abIntellectRuleDtlDao.delUselessByApplyNo(applyNo);
    }
    /**
     * 上移下移规则明细顺序
     *
     * @param abIntellectRuleDtlVO 选票规则明细VO
     */
    public void moveUpOrDown(AbIntellectRuleDtlVO abIntellectRuleDtlVO) {
        String updId = abIntellectRuleDtlVO.getId();
        // 上移、下移标志
        Boolean moveFlag = abIntellectRuleDtlVO.getMoveFlag();
        AbIntellectRuleDtl abIntellectRuleDtl = this.get(updId, true);
        // 顺序
        Integer serialNo = abIntellectRuleDtl.getSerialNo();
        // 根据申请号查询对应的所有规则明细数据
        String applyNo = abIntellectRuleDtlVO.getApplyNo();
        List<AbIntellectRuleDtlVO> abIntellectRuleDtlVOList = abIntellectRuleDtlDao.listRuleDtlByApplyNo(applyNo);
        // 需要上移的数据顺序不为1才需要移动
        boolean moveUp = moveFlag && serialNo != 1;
        // 需要下移的数据顺序不为当前最后一位才需要移动
        boolean moveDown = !moveFlag && serialNo != abIntellectRuleDtlVOList.size();
        ArrayList<AbIntellectRuleDtl> updList = new ArrayList<>();
        if (moveUp) {
            // 设置选中数据上移一位
            abIntellectRuleDtlVOList.get(serialNo - 1).setSerialNo(serialNo - 1);
            // 设置选中数据的前一个数据下移一位
            abIntellectRuleDtlVOList.get(serialNo - 2).setSerialNo(serialNo);
            updList.add(BeanUtils.copyNewInstanceProperties(abIntellectRuleDtlVOList.get(serialNo - 1), AbIntellectRuleDtl.class));
            updList.add(BeanUtils.copyNewInstanceProperties(abIntellectRuleDtlVOList.get(serialNo - 2), AbIntellectRuleDtl.class));
        } else if (moveDown) {
            // 设置选中数据下移一位
            abIntellectRuleDtlVOList.get(serialNo - 1).setSerialNo(serialNo + 1);
            // 设置选中数据的后一个数据上移一位
            abIntellectRuleDtlVOList.get(serialNo).setSerialNo(serialNo);
            updList.add(BeanUtils.copyNewInstanceProperties(abIntellectRuleDtlVOList.get(serialNo - 1), AbIntellectRuleDtl.class));
            updList.add(BeanUtils.copyNewInstanceProperties(abIntellectRuleDtlVOList.get(serialNo), AbIntellectRuleDtl.class));
        }
        this.getSession().clear();
        if (!CollectionUtils.isEmpty(updList)) {
            abIntellectRuleDtlDao.batchUpdate(updList);
        }
    }
    /**
     * 根据规则编码删除规则明细数据
     *
     * @param ruleCode 规则编码
     */
    public void delDtlByRuleCode(String ruleCode) {
        abIntellectRuleDtlDao.delDtlByRuleCode(ruleCode);
    }
    /**
     * 根据规则明细id删除对应的规则明细数据
     *
     * @param abIntellectRuleDtlVO 规则明细id
     */
    public void delRuleDtl(AbIntellectRuleDtlVO abIntellectRuleDtlVO) {
        // 需要删除的规则明细顺序
        Integer serialNo = abIntellectRuleDtlVO.getSerialNo();
        // 当前申请号对应的所有规则明细数据
        List<AbIntellectRuleDtlVO> ruleDtlList = abIntellectRuleDtlDao.listRuleDtlByApplyNo(abIntellectRuleDtlVO.getApplyNo());
        ArrayList<AbIntellectRuleDtl> updList = new ArrayList<>();
        for (Integer i = serialNo; i < ruleDtlList.size(); i++) {
            ruleDtlList.get(i).setSerialNo(i);
            updList.add(BeanUtils.copyNewInstanceProperties(ruleDtlList.get(i), AbIntellectRuleDtl.class));
        }
        this.getSession().clear();
        this.delete(abIntellectRuleDtlVO.getId());
        if (!CollectionUtils.isEmpty(updList)) {
            abIntellectRuleDtlDao.batchUpdate(updList);
        }
    }
}
