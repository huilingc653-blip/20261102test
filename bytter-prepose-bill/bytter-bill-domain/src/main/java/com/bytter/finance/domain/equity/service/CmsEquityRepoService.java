package com.bytter.finance.domain.equity.service;
import com.bytter.biz.payment.api.constant.ExpenseConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.domain.equity.dao.CmsEquityInfoDetailDao;
import com.bytter.finance.domain.equity.dao.CmsEquityRepoDao;
import com.bytter.finance.domain.equity.entity.CmsEquityInfoDetail;
import com.bytter.finance.domain.equity.entity.CmsEquityRepo;
import com.bytter.finance.domain.equity.entity.CmsEquityRepoPlanDetail;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoVO;
import com.bytter.finance.enums.EquityMessageEnum;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
/**
 * 股权融资回购
 *
 * @author tongqr
 */
@Service("cmsEquityRepoService")
@WorkFlow(CmsWorkFlowConstant.CMS_EQUITY_REPO)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_EQUITY_REPO)
@Transactional
public class CmsEquityRepoService extends BaseService<CmsEquityRepo, String> implements IWorkFlowListener {
    @Autowired
    private CmsEquityRepoDao cmsEquityRepoDao;
    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;
    @Autowired
    private CmsEquityInfoDetailDao cmsEquityInfoDetailDao;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private ICorpService corpServiceImpl;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
    private INisBillheadService nisBillheadService;
    @Autowired
    private CmsEquityRepoPlanDetailService cmsEquityRepoPlanDetailService;
    @Override
    public BaseDao<CmsEquityRepo, String> getEntityDao() {
        return cmsEquityRepoDao;
    }
    /**
     * 融资申请查询列表 - 数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDatatableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_EQUITY_REPO,
                dataTablesInput);
        DataTablesOutput dp = cmsEquityRepoDao.getDatatableApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍 : 查找的回购，pass 通过 temp 暂存或者打回,comm审批中
     */
    public List<CmsEquityRepoVO> getByCode(String equityCode, String flag) {
        return cmsEquityRepoDao.getByCode(equityCode, flag);
    }
    /**
     * 功能介绍 : 删除暂存的数据
     */
    public void deleteRepo(String equityCode, ICurrentUser user) {
        CmsEquityRepoVO repoVO = this.getByCode(equityCode, "temp").get(0);
        // //关闭消息
        // notificationService.complete(NotificationConstant.CMS.EQUITY_REPO, repoVO.getId(), user.getId());
        // 对应的明细
        CmsEquityInfoDetailVO detailvo = cmsEquityInfoService.getDetail(equityCode);
        CmsEquityInfoDetail detail = new CmsEquityInfoDetail();
        BeanUtils.copyNotNullProperties(detailvo, detail);
        cmsEquityRepoDao.deleteRepo(equityCode);
        detail.setBizStatus(CmsConstant.EQUITY_DETAIL_BIZSTATUS_56);
        cmsEquityInfoDetailDao.merge(detail);
    }
    /**
     * 功能介绍 : 提交或暂存<br>
     *
     * @param vo
     * @param saveType
     *            1提交，0暂存 <b>日期：</b>2000-06-10<br>
     *            <br>
     * @author tongqr
     */
    public void save(CmsEquityRepoVO vo, String saveType, ICurrentUser user) {
        CmsEquityRepo cmsEquityRepo = new CmsEquityRepo();
        BeanUtils.copyNotNullProperties(vo, cmsEquityRepo);
        if (vo.getThisRepoMoney().compareTo(vo.getReRepoMoney()) == 1) {
            throw new ParameterException(EquityMessageEnum.CMS_EQUITY_REPO_MONEY_CHECK);
        }
        // 对应的明细
        CmsEquityInfoDetailVO cmsEquityInfoDetailVO = cmsEquityInfoService.getDetail(cmsEquityRepo.getEquityCode());
        CmsEquityInfoDetail detail = new CmsEquityInfoDetail();
        BeanUtils.copyNotNullProperties(cmsEquityInfoDetailVO, detail);
        if ("1".equals(saveType)) {// 提交
            cmsEquityRepo.setStatus(CmsConstant.COMMITAPPROVE);
            detail.setBizStatus(CmsConstant.EQUITY_DETAIL_BIZSTATUS_55);
        } else {// 暂存
            cmsEquityRepo.setStatus(CmsConstant.TEMP);
            detail.setBizStatus(CmsConstant.EQUITY_DETAIL_BIZSTATUS_52);
        }
        CmsEquityRepo repo = null;
        if (StringUtils.isEmpty(cmsEquityRepo.getId())) {
            repo = cmsEquityRepo;
            this.save(repo, user);
        } else {
            repo = this.get(cmsEquityRepo.getId());
            BeanUtils.copyNotNullProperties(cmsEquityRepo, repo);
            this.update(repo, user);
        }
        cmsEquityInfoDetailDao.merge(detail);
        // //关闭消息
        // notificationService.complete(NotificationConstant.CMS.EQUITY_REPO, repo.getId(), user.getId());
        if ("1".equals(saveType)) {// 提交 启动审批流程
             getStartHandler().handler(CmsWorkFlowConstant.CMS_EQUITY_REPO, repo.getId(), repo, repo.getCorpId(),
                    user.getId());
        } else {
        }
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(CmsEquityRepo entity, ICurrentUser user) {
        entity.setCreateDate(new Date());
        entity.setCreateBy(user.getId());
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsEquityRepo entity, ICurrentUser user) {
        entity.setUpdateDate(new Date());
        entity.setUpdateBy(user.getId());
        super.updateEntity(entity);
    }
    /**
     * 功能介绍 : 新增缴付的时候对数据进行赋值
     */
    public void createRepo(CmsEquityRepoVO repoVO, CmsEquityInfoDetailVO detailVO, String type) {
        repoVO.setEquityCode(detailVO.getEquityCode());
        repoVO.setFinancingWay(detailVO.getFinancingWay());
        repoVO.setInvestor(detailVO.getInvestor());
        repoVO.setInvWay(detailVO.getInvWay());
        repoVO.setInvMoney(detailVO.getInvMoney());
        if (type.equals("1")) {
            repoVO.setOnlineSign("1");
        } else {
            repoVO.setOnlineSign("0");
        }
        List<CmsEquityRepoVO> repoVOList = cmsEquityRepoDao.getByCode(detailVO.getEquityCode(), "pass");
        BigDecimal allMoney = new BigDecimal(0);
        for (CmsEquityRepoVO repoVO1 : repoVOList) {
            allMoney = allMoney.add(repoVO1.getThisRepoMoney());
        }
        repoVO.setReRepoMoney(repoVO.getRepoMoney().subtract(allMoney));
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        CmsEquityRepo repo = this.get(businessKey,true);
        // 对应的明细
        CmsEquityInfoDetailVO cmsEquityInfoDetailVO = cmsEquityInfoService.getDetail(repo.getEquityCode());
        CmsEquityInfoDetail detail = new CmsEquityInfoDetail();
        BeanUtils.copyNotNullProperties(cmsEquityInfoDetailVO, detail);
        if (result) {// 审批通过
            repo.setStatus(CmsConstant.APPROVED);
            if (!EmptyUtils.isEmpty(repo.getRepoPlanDetailId())) {
                // 修改计划标识
                CmsEquityRepoPlanDetail cmsEquityRepoPlanDetail = cmsEquityRepoPlanDetailService
                        .get(repo.getRepoPlanDetailId());
                cmsEquityRepoPlanDetail.setIsPay("1");
                cmsEquityRepoPlanDetailService.updateEntity(cmsEquityRepoPlanDetail);
            }
            if (repo.getOnlineSign().equals("1")) {
                // 生成付款单
                // 走付款办理流程
                NisBillheadDTO nisBillhead = new NisBillheadDTO();
                String id = UUID.randomUUID().toString().replace("-", "");
                nisBillhead.setId(id);
                nisBillhead.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
                CorpDTO corp = corpServiceImpl.get(repo.getCorpId());
                nisBillhead.setCorpId(repo.getCorpId());// 单位
                nisBillhead.setCorpName(corp.getName());// 单位
                BtBankAccDTO bank = iBtBankAccService.getBankAccInfo(repo.getOnlinePaymentAcc());
                if (!StringUtils.isNotEmpty(bank)) {
                    List<String> acclist = new ArrayList<>();
                    acclist.add(repo.getOnlinePaymentAcc());
                    // 根据账号查询客商账户信息
                    ExternalCorpDTO externalCorpDTO = externalCorpService.findByCorpIdAndName(null,
                            repo.getOnlineAccName());
                    if (!EmptyUtils.isEmpty(externalCorpDTO)) {
                        ExternalCorpBankAccDTO externalCorpBankAcc = externalCorpService
                                .getExternalCorpBankAccByIdAndAcc(externalCorpDTO.getId(), repo.getOnlinePaymentAcc());
                        if (externalCorpBankAcc != null) {
                            nisBillhead.setAccId(externalCorpBankAcc.getExternalAcc());// 账号
                            nisBillhead.setPayeeBankCode(externalCorpBankAcc.getBankSourceCode());// 收款方开户行行号
                            nisBillhead.setProv(externalCorpBankAcc.getAccProvince());// 所在省
                            nisBillhead.setCity(externalCorpBankAcc.getAccCity());// 所在市
                        }
                    }
                } else {
                    nisBillhead.setAccId(bank.getId());// 账号
                    nisBillhead.setPayeeBankCode(bank.getBankCode());// 收款方开户行行号
                    nisBillhead.setCurId(bank.getCurId());// 币种
                    nisBillhead.setProv(bank.getProv());// 所在省
                    nisBillhead.setCity(bank.getCity());// 所在市
                }
                nisBillhead.setPayeeAcc(repo.getOnlinePaymentAcc());// 账户
                nisBillhead.setPayeeAccName(repo.getOnlineAccName());// 账户名称
                nisBillhead.setPayeeBank(repo.getOnlineBankName());// 开户行名称
                nisBillhead.setPayMoney(repo.getThisRepoMoney());// 金额
                nisBillhead.setWishPayday(repo.getRepoDate());// 支付日期
                // 根据调拨类型设置结算方式
                FcVoucherTypeDTO voucherType = null;
                voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_34);
                if (voucherType != null) {
                    nisBillhead.setVoucherTypeId(voucherType.getId());// 付款方式默认外付网银
                }
                nisBillhead.setPurpose(repo.getPurpose());// 用途
                nisBillhead.setStatus(ExpenseConstant.STATUE_TEMP);// 状态
                nisBillhead.setBillFrom(NetbankConstant.BILLFROM_10);// 单据来源:10-股权融资回购
                nisBillhead.setIsPayed(NetbankConstant.ISPAYED_0); // 是否已付款标志 默认否
                nisBillhead.setIsQuick(NetbankConstant.ISQUICK_0);// 加急标记默认为不加急
                nisBillhead.setIsForindividual(NetbankConstant.FORINDIVIDUAL_0);// 是否对私付款 默认为对公
                nisBillheadService.saveNisBillhead(nisBillhead, user);
            }
            // 如果剩余缴付小于等于0则完成，负责未完成。还在进行中
            if (repo.getReRepoMoney().subtract(repo.getThisRepoMoney()).compareTo(BigDecimal.ZERO) == 0) {
                detail.setBizStatus(CmsConstant.EQUITY_DETAIL_BIZSTATUS_60);
            } else {
                detail.setBizStatus(CmsConstant.EQUITY_DETAIL_BIZSTATUS_56);
            }
        } else {// 打回
            repo.setStatus(CmsConstant.BACK);
            detail.setBizStatus(CmsConstant.EQUITY_DETAIL_BIZSTATUS_51);
            // String submitUser = StringUtils.isEmpty(repo.getUpdateBy()) ? repo.getCreateBy() :
            // repo.getUpdateBy();//提交人
            // notificationService.sendToUser(repo.getId(), submitUser, NotificationConstant.CMS.EQUITY_REPO,
            // repo);//添加股权融资缴付/过户的消息提示
        }
        cmsEquityInfoDetailDao.merge(detail);
        this.saveOrUpdate(repo);
        this.getSession().flush();
        this.getSession().clear();
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
