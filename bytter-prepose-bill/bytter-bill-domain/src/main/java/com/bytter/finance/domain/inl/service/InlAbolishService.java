package com.bytter.finance.domain.inl.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatCpUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.constant.InlWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.dao.InlAbolishDao;
import com.bytter.finance.domain.inl.entity.CmsInlAbolish;
import com.bytter.finance.domain.inl.entity.CmsInlInfo;
import com.bytter.finance.domain.inl.vo.CmsInlAbolishVO;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.InlMessageEnum;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
@WorkFlow(InlWorkFlowConstant.CMS_INL_ABOLISH)
@DubboService(interfaceClass = IWorkFlowListener.class,group = InlWorkFlowConstant.CMS_INL_ABOLISH)
@Service("inlAbolishService")
public class InlAbolishService extends BaseService<CmsInlAbolish, String> implements IWorkFlowListener {
    @Autowired
    private InlAbolishDao inlAbolishDao;
    @Autowired
    private InlInfoService inlInfoService;
    @Autowired
    private InlSendService inlSendService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsProcessMsgService msService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Autowired
    private IRatCpUrationService ratCpUrationService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    /**
     *
     * @param id
     * @return
     */
    public CmsInlAbolishVO getCmsInlAbolishById(String id) {
        CmsInlAbolish cmsInlAbolish = inlAbolishDao.get(id);
        CmsInlAbolishVO cmsInlAbolishVO = new CmsInlAbolishVO();
        BeanUtils.copyProperties(cmsInlAbolish, cmsInlAbolishVO);
        return cmsInlAbolishVO;
    }
    /**
     * 删除数据
     *
     * @param id
     */
    public void deleteById(String id) {
        CmsInlAbolish cmsInlAbolish = inlAbolishDao.get(id);
        cmsInlAbolish.setStatus(InlConstant.DELETE);
        inlAbolishDao.update(cmsInlAbolish);
    }
    /**
     * 查询可作废内部拆借申请数据
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput listAbolishInfoDataTable(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_INL_INFO_QUERY, user.getId());
        return inlAbolishDao.listAbolishInfoDataTable(input, corpIds);
    }
    /**
     * 查询待审批数据
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput approveDataTable(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(InlWorkFlowConstant.CMS_INL_ABOLISH,
                input);
        return workFlowBusiness.merge(inlAbolishDao.listInlAbolishApprove(input, workFlowBusiness.getBusinessIdList()));
    }
    /**
     * 保存数据
     *
     * @param cmsInlAbolishVO
     * @param user
     */
    public void save(CmsInlAbolishVO cmsInlAbolishVO, ICurrentUser user) {
        CmsInlAbolish cmsInlAbolish;
        if (StringUtils.isNotEmpty(cmsInlAbolishVO.getId())) {
            cmsInlAbolish = inlAbolishDao.get(cmsInlAbolishVO.getId());
            BeanUtils.copyNotNullProperties(cmsInlAbolishVO, cmsInlAbolish);
            cmsInlAbolish.setUpdateBy(user.getId());
            cmsInlAbolish.setUpdateDate(new Date());
        } else {
            cmsInlAbolish = new CmsInlAbolish();
            BeanUtils.copyNotNullProperties(cmsInlAbolishVO, cmsInlAbolish);
            cmsInlAbolish.setCreateBy(user.getId());
            cmsInlAbolish.setCreateDate(new Date());
            cmsInlAbolish.setBillNo(InlUtil.getInlAbolishNo());
        }
        inlAbolishDao.saveOrUpdate(cmsInlAbolish);
        if (InlConstant.COMMIT_APPROVE == cmsInlAbolish.getStatus()) {
            notificationService.complete(NotificationConstant.CMS.INL_ABOLISH, cmsInlAbolish.getId(), user.getId());
             getStartHandler().handler(InlWorkFlowConstant.CMS_INL_ABOLISH, cmsInlAbolish.getId(), cmsInlAbolish,
                    cmsInlAbolish.getCorpId(), user.getId());
        }
    }
    @Override
    public BaseDao<CmsInlAbolish, String> getEntityDao() {
        return inlAbolishDao;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /* 判断业务主键是否为空 */
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        String id = workFlowResult.getBusinessKey();
        boolean isPass = workFlowResult.isPass();
        CmsInlAbolish cmsInlAbolish = inlAbolishDao.get(id);
        ICurrentUser user = workFlowResult.getUser();
        if (isPass) {
            cmsInlAbolish.setStatus(InlConstant.APPROVED);
            msService.insertProcessMsg(cmsInlAbolish.getBillNo(), InlConstant.CMS_INL_ABOLIS_NO,
                    CmsProcessMsgConstant.MSGA_CHECKOK, "审批通过", user);
            checkSend(cmsInlAbolish);
            CmsInlInfo inlInfo = inlInfoService.get(cmsInlAbolish.getInlInfoId());
            inlInfo.setBizStatus(InlConstant.DEFERRED);
            inlInfo.setStatus(InlConstant.DEFERRED);
            inlInfoService.updateEntity(inlInfo);
            // 其他处理
            otherHandle(inlInfo, user);
        } else {
            cmsInlAbolish.setStatus(InlConstant.BACK);
            /**
             * 增加消息提醒
             */
            notificationService.send(cmsInlAbolish.getId(), cmsInlAbolish.getCorpId(),
                    NotificationConstant.CMS.INL_ABOLISH, cmsInlAbolish);
            msService.insertProcessMsg(cmsInlAbolish.getBillNo(), InlConstant.CMS_INL_ABOLIS_NO,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, "审批打回", user);
        }
        // 发放数据检查校验
        inlAbolishDao.update(cmsInlAbolish);
    }
    /**
     * 其他挂接处理
     *
     * @param cmsInlInfo
     * @param user
     */
    public void otherHandle(CmsInlInfo cmsInlInfo, ICurrentUser user) {
        // 释放删除被担保合同
        doGua(cmsInlInfo);
        // 释放授信
        doCpRate(cmsInlInfo, "1");
    }
    /**
     * 释放担保
     */
    private void doGua(CmsInlInfo cmsInlInfo) {
        if (CommonConstant.YES.equals(cmsInlInfo.getIsHangGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            createGua(cmsInlInfo, guaContractOccupDTO);
            guaContractOccupServie.backUseGuaContractOccup(guaContractOccupDTO);
            guaContractOccupServie.deleteGuaContractOccup(cmsInlInfo.getId(), ConfigConstant.INL);
        }
    }
    /**
     * 授信挂接处理
     *
     * @param cmsInlInfo
     * @param isAllRelease
     */
    private void doCpRate(CmsInlInfo cmsInlInfo, String isAllRelease) {
        if (CommonConstant.YES.equals(cmsInlInfo.getIsHangCredit())) {
            RatUrationDTO ratUrationDTO = new RatUrationDTO();
            createRat(ratUrationDTO, cmsInlInfo, isAllRelease);
            ratCpUrationService.releaseRation(ratUrationDTO);
            ratCpUrationService.deleteUseCpRation(ratUrationDTO);
        }
    }
    /**
     * 释放授信
     *
     * @param urationDTO
     * @param cmsInlInfo
     * @param isAllRelease
     */
    private void createRat(RatUrationDTO urationDTO, CmsInlInfo cmsInlInfo, String isAllRelease) {
        urationDTO.setRatCode(cmsInlInfo.getCpRatCode());
        urationDTO.setCurCode(cmsInlInfo.getCurId());// 币别ID
        urationDTO.setBillId(cmsInlInfo.getId());// 业务ID
        urationDTO.setBillNo(cmsInlInfo.getBillNo());// 业务编号
        urationDTO.setSysCode(ConfigConstant.INL);// 内部拆借
        urationDTO.setBusinessName(RationConstant.BUSINESS_NAME_10);// 内部拆借占用
        urationDTO.setBeginDate(cmsInlInfo.getStartDate());// 内部拆借日期
        urationDTO.setEndDate(cmsInlInfo.getEndDate());// 到期日期
        urationDTO.setMoney(cmsInlInfo.getMoney());// 释放/占用金额
        urationDTO.setBillMoney(cmsInlInfo.getMoney());// 业务金额
        urationDTO.setCorpId(cmsInlInfo.getBorrowInCorp());// 网点/单位
        urationDTO.setRatItemCode(cmsInlInfo.getCpRatItemCode());
        if (StringUtils.isNotEmpty(isAllRelease) && CommonConstant.YES.equals(isAllRelease)) {
            // 释放全部授信
            urationDTO.setAllRelease(true);
        }
    }
    /**
     * 设置担保挂接数据
     *
     * @param cmsInlInfo
     * @param guaContractOccupDTO
     */
    private static void createGua(CmsInlInfo cmsInlInfo, GuaContractOccupDTO guaContractOccupDTO) {
        guaContractOccupDTO.setBusinessId(cmsInlInfo.getId());
        if (StringUtils.isNotEmpty(cmsInlInfo.getGuaContractId())) {
            if (cmsInlInfo.getGuaContractId().contains(",")) {
                guaContractOccupDTO.setContractIds(Arrays.asList(cmsInlInfo.getGuaContractId().split(",")));
            } else {
                guaContractOccupDTO.setContractIds(Collections.singletonList(cmsInlInfo.getGuaContractId()));
            }
        }
        guaContractOccupDTO.setSysCode(ConfigConstant.INL);
        guaContractOccupDTO.setItemNo(cmsInlInfo.getBillNo());
        guaContractOccupDTO.setUseDate(new Date());
    }
    /**
     * 发放检查
     */
    public void checkSend(CmsInlAbolish cmsInlAbolish) {
        CmsInlInfoVO cmsInlInfoVO = inlSendService.getSendMoneyByInfoId(cmsInlAbolish.getInlInfoId());
        if (null != cmsInlInfoVO && cmsInlInfoVO.getSendMoney().compareTo(BigDecimal.ZERO) > 0) {
            throw new ParameterException(InlMessageEnum.INL_ABOLISH_ERROR1_MSG);
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
