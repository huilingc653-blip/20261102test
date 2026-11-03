package com.bytter.finance.domain.equity.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.domain.equity.dao.CmsEquityInfoDetailDao;
import com.bytter.finance.domain.equity.dao.CmsEquityPayTheDao;
import com.bytter.finance.domain.equity.entity.CmsEquityInfoDetail;
import com.bytter.finance.domain.equity.entity.CmsEquityPayPlan;
import com.bytter.finance.domain.equity.entity.CmsEquityPayPlanDetail;
import com.bytter.finance.domain.equity.entity.CmsEquityPayThe;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayTheVO;
import com.bytter.finance.enums.EquityMessageEnum;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
/**
 * 股权融资缴付/过户
 *
 * @author tongqr
 */
@Service("cmsEquityPayTheService")
@WorkFlow(CmsWorkFlowConstant.CMS_EQUITY_PAYTHE)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_EQUITY_PAYTHE)
@Transactional
public class CmsEquityPayTheService extends BaseService<CmsEquityPayThe, String> implements IWorkFlowListener {
    @Autowired
    private CmsEquityPayTheDao cmsEquityPayTheDao;
    @Autowired
    private CmsEquityInfoDetailDao cmsEquityInfoDetailDao;
    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService rateAdjustService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private CmsEquityPayPlanService cmsEquityPayPlanService;
    @Autowired
    private CmsEquityPayPlanDetailService cmsEquityPayPlanDetailService;
    @Override
    public BaseDao<CmsEquityPayThe, String> getEntityDao() {
        return cmsEquityPayTheDao;
    }
    /**
     * 功能介绍 : 查找的缴付，pass 通过 temp 暂存或者打回,comm审批中
     */
    public List<CmsEquityPayTheVO> getByCode(String equityCode, String flag) {
        return cmsEquityPayTheDao.getByCode(equityCode, flag);
    }
    /**
     * 融资申请查询列表 - 数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDatatableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_EQUITY_PAYTHE,
                dataTablesInput);
        DataTablesOutput dp = cmsEquityPayTheDao.getDatatableApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍 : 新增缴付的时候对数据进行赋值
     */
    public void createPayThe(CmsEquityPayTheVO payTheVO, CmsEquityInfoDetailVO detailVO) {
        payTheVO.setEquityCode(detailVO.getEquityCode());
        payTheVO.setFinancingWay(detailVO.getFinancingWay());
        payTheVO.setInvestor(detailVO.getInvestor());
        payTheVO.setInvWay(detailVO.getInvWay());
        payTheVO.setInvMoney(detailVO.getInvMoney());
        payTheVO.setAssName(detailVO.getAssetName());
        List<CmsEquityPayTheVO> payTheVOList = cmsEquityPayTheDao.getByCode(detailVO.getEquityCode(), "pass");
        BigDecimal allMoney = new BigDecimal(0);
        for (CmsEquityPayTheVO payTheVO1 : payTheVOList) {
            allMoney = allMoney.add(payTheVO1.getRmbMoney());
        }
        payTheVO.setRePayMoney(detailVO.getInvMoney().subtract(allMoney));
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
    public void savePay(CmsEquityPayTheVO vo, String saveType, ICurrentUser user) {
        CmsEquityPayThe cmsEquityPayThe = new CmsEquityPayThe();
        //TODO 币种名称补充
        Optional.ofNullable(vo).ifPresent(cmsEquityPayTheDO -> {
            if (StringUtils.isNotEmpty(cmsEquityPayTheDO.getPayCurId())){
                BtCurrencyDTO currencyDO = btCurrencyService.getObjectById(cmsEquityPayTheDO.getPayCurId());
                Optional.ofNullable(currencyDO).ifPresent(currency -> vo.setPayCurName(currency.getCurName()));
            }
        });
        BeanUtils.copyNotNullProperties(vo, cmsEquityPayThe);
        cmsEquityPayThe.setPayOrCh(cmsEquityPayThe.getInvWay());
        if (cmsEquityPayThe.getInvWay().equals("1")) {
            if (vo.getRmbMoney().compareTo(vo.getRePayMoney()) == 1) {
                throw new ParameterException(EquityMessageEnum.CMS_EQUITY_REPAY_MONEY_CHECK);
            }
        }
        // 对应的明细
        CmsEquityInfoDetailVO cmsEquityInfoDetailVO = cmsEquityInfoService.getDetail(cmsEquityPayThe.getEquityCode());
        CmsEquityInfoDetail detail = new CmsEquityInfoDetail();
        BeanUtils.copyNotNullProperties(cmsEquityInfoDetailVO, detail);
        if ("1".equals(saveType)) {// 提交
            cmsEquityPayThe.setStatus(CmsConstant.COMMITAPPROVE);
            detail.setBizStatus(CmsConstant.EQUITY_DETAIL_BIZSTATUS_25);
        } else {// 暂存
            cmsEquityPayThe.setStatus(CmsConstant.TEMP);
            detail.setBizStatus(CmsConstant.EQUITY_DETAIL_BIZSTATUS_22);
        }
        CmsEquityPayThe payThe = null;
        if (StringUtils.isEmpty(cmsEquityPayThe.getId())) {
            payThe = cmsEquityPayThe;
            this.save(payThe, user);
        } else {
            payThe = this.get(cmsEquityPayThe.getId());
            BeanUtils.copyNotNullProperties(cmsEquityPayThe, payThe);
            this.update(payThe, user);
        }
        cmsEquityInfoDetailDao.merge(detail);
        // //关闭消息
        // notificationService.complete(NotificationConstant.CMS.EQUITY_PAYTHE, payThe.getId(), user.getId());
        if ("1".equals(saveType)) {// 提交 启动审批流程
             getStartHandler().handler(CmsWorkFlowConstant.CMS_EQUITY_PAYTHE, payThe.getId(), payThe,
                    payThe.getCorpId(), user.getId());
        } else {
        }
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(CmsEquityPayThe entity, ICurrentUser user) {
        entity.setCreateDate(new Date());
        entity.setCreateBy(user.getId());
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsEquityPayThe entity, ICurrentUser user) {
        entity.setUpdateDate(new Date());
        entity.setUpdateBy(user.getId());
        super.updateEntity(entity);
    }
    /**
     * @param curId
     * @param curPreTime
     * @return java.math.BigDecimal
     * @description 获取汇率
     **/
    public BigDecimal getConverRate(String curId, String payCurId, Date curPreTime) {
        BtCurrencyDTO natualCurrency = btCurrencyService.getNatualCurrency();
        return rateAdjustService.getExchangeRateByNoCheck(payCurId, curId, curPreTime,
                FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        CmsEquityPayThe payThe = this.get(businessKey,true);
        // 对应的明细
        CmsEquityInfoDetailVO cmsEquityInfoDetailVO = cmsEquityInfoService.getDetail(payThe.getEquityCode());
        CmsEquityInfoDetail detail = new CmsEquityInfoDetail();
        BeanUtils.copyNotNullProperties(cmsEquityInfoDetailVO, detail);
        if (result) {// 审批通过
            payThe.setStatus(CmsConstant.APPROVED);
            // 如果剩余缴付小于等于0则完成，负责未完成。还在进行中
            if (payThe.getInvWay().equals("1")) {
                if (payThe.getRePayMoney().subtract(payThe.getRmbMoney()).compareTo(BigDecimal.ZERO) == 0) {
                    detail.setBizStatus(CmsConstant.EQUITY_DETAIL_BIZSTATUS_30);
                } else {
                    detail.setBizStatus(CmsConstant.EQUITY_DETAIL_BIZSTATUS_26);
                }
                if (!EmptyUtils.isEmpty(payThe.getPlanId())) {
                    // 修改已缴付金额
                    CmsEquityPayPlan cmsEquityPayPlan = cmsEquityPayPlanService.get(payThe.getPlanId());
                    BigDecimal payMoney = EmptyUtils.isEmpty(cmsEquityPayPlan.getPayMoney()) ? new BigDecimal(0)
                            : cmsEquityPayPlan.getPayMoney();
                    cmsEquityPayPlan.setPayMoney(payMoney.add(payThe.getRmbMoney()));
                    cmsEquityPayPlanService.updateEntity(cmsEquityPayPlan);
                }
                if (!EmptyUtils.isEmpty(payThe.getPlanDetailId())) {
                    // 修改计划标识
                    CmsEquityPayPlanDetail cmsEquityPayPlanDetail = cmsEquityPayPlanDetailService
                            .get(payThe.getPlanDetailId());
                    cmsEquityPayPlanDetail.setIsPay("1");
                    cmsEquityPayPlanDetailService.updateEntity(cmsEquityPayPlanDetail);
                }
            } else {
                detail.setBizStatus(CmsConstant.EQUITY_DETAIL_BIZSTATUS_30);
            }
        } else {// 打回
            payThe.setStatus(CmsConstant.BACK);
            detail.setBizStatus(CmsConstant.EQUITY_DETAIL_BIZSTATUS_21);
            // String submitUser = StringUtils.isEmpty(payThe.getUpdateBy()) ? payThe.getCreateBy() :
            // payThe.getUpdateBy();//提交人
            // notificationService.sendToUser(payThe.getId(), submitUser, NotificationConstant.CMS.EQUITY_PAYTHE,
            // payThe);//添加股权融资缴付/过户的消息提示
        }
        cmsEquityInfoDetailDao.merge(detail);
        this.saveOrUpdate(payThe);
        this.getSession().flush();
        this.getSession().clear();
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
