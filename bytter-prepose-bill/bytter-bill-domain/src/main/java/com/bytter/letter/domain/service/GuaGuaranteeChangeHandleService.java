package com.bytter.letter.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.letter.domain.constant.LetterConstant;
import com.bytter.letter.domain.dao.GuaGuaranteeChangeHandleDao;
import com.bytter.letter.domain.entity.GuaGuaranteeChangeInfo;
import com.bytter.letter.domain.entity.GuaGuaranteeInfo;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author luxy
 * @version 1.0
 * @Title:GuaGuaranteeChangeHandleService
 * @Description:
 * @date 2019年1月14日 上午11:51:00
 */
@Service("GuaGuaranteeChangeHandleService")
@Transactional
public class GuaGuaranteeChangeHandleService extends BaseService<GuaGuaranteeChangeInfo, String> {
    @Autowired
    private GuaGuaranteeChangeHandleDao lgChangeHandleDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private GuaGuaranteeInfoService lgInfoService;
    @Autowired
    @Qualifier("ratUrationServiceImpl")
    private IRatUrationService urationService;
    /**
     * 查询保函申请更改办理数据列表
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput datatableList(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(LetterConstant.GUA_GUARANTEE_CHANGEHANDLE_QUERY,
                user.getId());
        return lgChangeHandleDao.datatableList(input, corpAuth);
    }
    /**
     * 完成更改，将保函申请更改状态改为完成更改状态，并更新保函申请表
     */
    public void submit(String id, ICurrentUser user) {
        GuaGuaranteeChangeInfo lgChangeInfo = this.get(id);
        lgChangeInfo.setStatus(LetterConstant.STATUS_CHANGE_CHECK);
        lgChangeInfo.setUpdateBy(user.getId());
        lgChangeInfo.setUpdateDate(DateUtils.getDate());
        changeLgInfo(lgChangeInfo, user);// 更新保函申请表中的数据
        super.updateEntity(lgChangeInfo);
    }
    /**
     * 拒绝更改，将状态改为更改拒绝状态
     */
    public void refuse(String id, ICurrentUser user) {
        GuaGuaranteeChangeInfo lgChangeInfo = this.get(id);
        lgChangeInfo.setStatus(LetterConstant.STATUS_CHANGE_REFUSE);
        lgChangeInfo.setUpdateBy(user.getId());
        lgChangeInfo.setUpdateDate(DateUtils.getDate());
        GuaGuaranteeInfo lgInfo = lgInfoService.get(lgChangeInfo.getGuaraId());
        lgInfo.setStatus(LetterConstant.STATUS_CHANGEING);// 将保函申请状态改为更改中
        lgInfoService.updateEntity(lgInfo);
        super.updateEntity(lgChangeInfo);
    }
    /**
     * 更新保函申请的数据
     *
     * @return
     */
    public void changeLgInfo(GuaGuaranteeChangeInfo lgChangeInfo, ICurrentUser user) {
        GuaGuaranteeInfo lgInfo = lgInfoService.get(lgChangeInfo.getGuaraId());
        // 如果挂接了授信,保函金额发生变化，或者启用敞口授信时，保证金额度发生变化，确认更改后更新授信信息
        if (lgChangeInfo.getGuaranteeMoney().compareTo(lgInfo.getGuaranteeMoney()) != 0
                || (lgInfo.getIsOpenRat().equals(LetterConstant.GUA_COMMON_Y)
                        && lgChangeInfo.getBondMoney().compareTo(lgInfo.getBondMoney()) != 0)) {
            if (!EmptyUtils.isEmpty(lgInfo.getRatmodel()) && CommonConstant.TRUE.equals(lgInfo.getRatmodel())) {
                RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.GUA, lgInfo.getId());
                urationService.passUseRation(urationDTO);
            }
        }
        BigDecimal guaranteeMoney = lgChangeInfo.getGuaranteeMoney();
        if (guaranteeMoney != null) {
            lgInfo.setGuaranteeMoney(lgChangeInfo.getGuaranteeMoney());
        }
        BigDecimal bondMoney = lgChangeInfo.getBondMoney();
        if (bondMoney != null) {
            lgInfo.setBondMoney(lgChangeInfo.getBondMoney());
        }
        BigDecimal handleCharge = lgChangeInfo.getHandleCharge();
        if (handleCharge != null) {
            lgInfo.setHandleCharge(lgChangeInfo.getHandleCharge());
        }
        Date endDate = lgChangeInfo.getEndDate();
        if (endDate != null) {
            lgInfo.setEndDate(lgChangeInfo.getEndDate());
        }
        Date exceptEndDate = lgChangeInfo.getExpectEndDate();
        if (exceptEndDate != null) {
            lgInfo.setExpectEndDate(lgChangeInfo.getExpectEndDate());
        }
        lgInfo.setStatus(LetterConstant.GUARANTEE_RECEIVED);// 将保函申请状态改为领取完成
        lgInfo.setUpdateBy(user.getId());
        lgInfo.setUpdateDate(DateUtils.getDate());
        lgInfoService.updateEntity(lgInfo);
        lgInfoService.getEntityDao().flush();
    }
    @Override
    public BaseDao<GuaGuaranteeChangeInfo, String> getEntityDao() {
        // TODO Auto-generated method stub
        return lgChangeHandleDao;
    }
}
