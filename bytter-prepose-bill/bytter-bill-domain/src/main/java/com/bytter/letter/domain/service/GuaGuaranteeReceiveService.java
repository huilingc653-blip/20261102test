package com.bytter.letter.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.credit.dto.RatUrationDtlDTO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.FcBondDTO;
import com.bytter.guarantee.enums.FcBondMessageEnum;
import com.bytter.letter.domain.constant.LetterConstant;
import com.bytter.letter.domain.dao.GuaGuaranteeReceiveDao;
import com.bytter.letter.domain.entity.GuaGuaranteeInfo;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaGuaranteeReceiveService
 * @Description 保函领取service
 * @date 2019年1月7日
 */
@Service("guaGuaranteeReceiveService")
@Transactional
public class GuaGuaranteeReceiveService extends BaseService<GuaGuaranteeInfo, String> {
    @Autowired
    GuaGuaranteeReceiveDao guaGuaranteeReceiveDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private GuaGuaranteeInfoService guaGuaranteeInfoService;
    @Autowired
    @Qualifier("fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Autowired
    @Qualifier("ratUrationServiceImpl")
    private IRatUrationService urationService;
    /**
     * 保函领取列表
     *
     * @param dataTablesInput
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(LetterConstant.GUA_GUARANTEE_INFO_RECEIVE, user.getId());
        return guaGuaranteeReceiveDao.getDataTablesUnion(dataTablesInput, corpAuth);
    }
    /**
     * 通过保函id查询相应的保函信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public GuaGuaranteeInfoVO getGuaGuaranteeInfoById(String id) {
        return guaGuaranteeReceiveDao.getGuaGuaranteeInfoById(id);
    }
    /**
     * 保存保函领取后的相关信息
     *
     * @param vo
     * @param user
     */
    public void saveGuaGuaranteeReceive(GuaGuaranteeInfoVO vo, ICurrentUser user) {
        ConfigDTO bailConfig = guaGuaranteeInfoService.getIsHangBail();// 获取参数设置是否挂接保证金
        if (vo != null) {
            GuaGuaranteeInfoVO infoVo = this.getGuaGuaranteeInfoById(vo.getId());
            infoVo.setHandleRate(vo.getHandleRate());
            infoVo.setHandleCharge(vo.getHandleCharge());
            infoVo.setGuaranteeMoney(vo.getGuaranteeMoney());
            infoVo.setStartDate(vo.getStartDate());
            infoVo.setEndDate(vo.getEndDate());
            infoVo.setAttachmentId(vo.getAttachmentId());
            infoVo.setReceiver(vo.getReceiver());
            infoVo.setBankName(vo.getBankName());
            infoVo.setReceiveDate(vo.getReceiveDate());
            infoVo.setGuaranteeedCorp(vo.getGuaranteeedCorp());
            if (bailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {// 保函参数设置挂接保证金
                infoVo.setBondMoney(vo.getBondMoney() == null ? BigDecimal.ZERO : vo.getBondMoney());// 保证金金额为空时设置为0
                infoVo.setBondPercent(vo.getBondPercent() == null ? BigDecimal.ZERO : vo.getBondPercent());
                infoVo.setBondContract(vo.getBondContract());
                infoVo.setBondId(vo.getBondId());
            } else {
                infoVo.setBondMoney(BigDecimal.ZERO);
                infoVo.setBondPercent(BigDecimal.ZERO);
            }
            GuaGuaranteeInfo guaGuaranteeInfo = new GuaGuaranteeInfo();
            BeanUtils.copyNotNullProperties(infoVo, guaGuaranteeInfo);
            guaGuaranteeInfo.setStatus(LetterConstant.GUARANTEE_RECEIVED);// 修改为领取完成状态
            guaGuaranteeInfo.setUpdateBy(user.getId());
            guaGuaranteeInfo.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(guaGuaranteeInfo);
            this.getEntityDao().flush();
            if (bailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {// 参数设置挂接保证金
                // 判断是否挂接了保证金
                if ((infoVo.getIsHookBond() != null) && (infoVo.getIsHookBond().equals(FcBondConstant.IS_HOOK_Y))) {// 保函挂接保证金
                    FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(infoVo.getId(),
                            infoVo.getGuaranteeNo());
                    if (dto.getApplyMoney().compareTo(infoVo.getBondMoney()) != 0
                            || infoVo.getGuaranteeMoney().compareTo(dto.getItemMoney()) != 0) {// 修改保证金额度或者保函申请额度，需要更新保证金占用额度
                        // 判断占用金额是否大于可用余额
                        FcBondDTO fcBondDTO = fcBondHookService.getFcBondMsgById(infoVo.getBondId());
                        BigDecimal applyMoney = infoVo.getBondMoney();
                        BigDecimal availMoney = fcBondDTO.getAvailMoney();
                        if (dto != null) {
                            availMoney = availMoney.add(dto.getApplyMoney());// 编辑状态可用金额要加上修改之前的额度
                        }
                        BigDecimal dif = availMoney.subtract(applyMoney);
                        if ((dif.compareTo(BigDecimal.ZERO)) == -1) {// 占用金额大于可用余额
                            throw new ParameterException(FcBondMessageEnum.FCBOND_APPLYMONEY_CHECKCANUSE,
                                    availMoney.toString());
                        }
                        infoVo.setId(infoVo.getId());
                        guaGuaranteeInfoService.updateFcBondApply(infoVo, dto, user);
                    }
                }
            }
            // 挂接授信
            if (!EmptyUtils.isEmpty(infoVo.getRatmodel()) && CommonConstant.TRUE.equals(infoVo.getRatmodel())) {// 挂接授信
                RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, infoVo.getId());
                if (urationDTO != null) {
                    if (urationDTO.getBillMoney().compareTo(vo.getGuaranteeMoney()) != 0
                            || urationDTO.getBondMoney().compareTo(infoVo.getBondMoney()) != 0) {
                        urationDTO.setMoney(urationDTO.getBillMoney());
                        urationDTO.setReleaseNonRecycleRat(true);// 当为不循环授信时，需要释放授信
                        urationService.releaseRation(urationDTO);// 金额发生变化，释放之前占用的授信
                        RatUrationDTO newRatUDTO = new RatUrationDTO();
                        BeanUtils.copyNotNullProperties(urationDTO, newRatUDTO, "urationId", "applyDtls");
                        newRatUDTO.setMoney(vo.getGuaranteeMoney());
                        newRatUDTO.setBillMoney(vo.getGuaranteeMoney());
                        newRatUDTO.setBondMoney(infoVo.getBondMoney());
                        newRatUDTO.setSysCode(ConfigConstant.GUA);
                        newRatUDTO.setBailRate(infoVo.getBondPercent());// 保证金比例
                        if (urationDTO.getItemFlag().equals(RationConstant.RAT_COMFLAG_1)) {
                            List<RatUrationDtlDTO> newUrationDtl = new ArrayList<>();
                            RatUrationDtlDTO urationDtlDTO = new RatUrationDtlDTO();
                            urationDtlDTO.setItemCode(urationDTO.getRatItemCode());
                            urationDtlDTO.setItemMoney(newRatUDTO.getMoney());
                            urationDtlDTO.setUseMaxMoney(newRatUDTO.getMoney());
                            newUrationDtl.add(urationDtlDTO);
                            newRatUDTO.setApplyDtls(newUrationDtl);
                        }
                        urationService.addUseRation(newRatUDTO);
                        urationService.passUseRation(newRatUDTO);
                        // urationService.directUseRation(newRatUDTO);//重新保存，直接占用
                    }
                }
            }
            // 挂接授信完
        }
    }
    /**
     * 根据id修改领取失败信息
     */
    public void getfail(GuaGuaranteeInfoVO vo, ICurrentUser user) {
        GuaGuaranteeInfo LgInfo = this.get(vo.getId());
        LgInfo.setStatus(LetterConstant.STATUS_FAIL);
        LgInfo.setFailReason(vo.getFailReason());
        LgInfo.setUpdateBy(user.getId());
        LgInfo.setUpdateDate(DateUtils.getDate());
        this.updateEntity(LgInfo);
        // 释放挂接的授信
        if (!EmptyUtils.isEmpty(LgInfo.getRatmodel()) && CommonConstant.TRUE.equals(LgInfo.getRatmodel())) {
            RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.GUA, vo.getId());
            urationDTO.setMoney(LgInfo.getGuaranteeMoney());// 所有金额
            urationDTO.setAllRelease(true);// 全部释放
            urationService.releaseRation(urationDTO);
        }
        // 删除保证金挂接信息，释放占用额度
        if (!EmptyUtils.isEmpty(LgInfo.getIsHookBond())
                && LetterConstant.GUA_COMMON_Y.equals(LgInfo.getIsHookBond())) {// 挂接保证金
            FcBondApplyDTO bondApplyDTO = fcBondHookService.getFcBondApplyByItemIds(LgInfo.getId(),
                    LgInfo.getGuaranteeNo());
            bondApplyDTO.setStatus(FcBondConstant.STATUE_RELEASE);
            fcBondHookService.updateFcBondApplyData(bondApplyDTO);
        }
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(GuaGuaranteeInfo entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(GuaGuaranteeInfo entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(GuaGuaranteeInfo entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(GuaGuaranteeInfo entity) {
        super.saveOrUpdate(entity);
        this.getSession().flush();
    }
    @Override
    public BaseDao<GuaGuaranteeInfo, String> getEntityDao() {
        return guaGuaranteeReceiveDao;
    }
}
