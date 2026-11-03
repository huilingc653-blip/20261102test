package com.bytter.finance.domain.equity.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.equity.dao.CmsEquityRepoDao;
import com.bytter.finance.domain.equity.dao.CmsEquityRepoPlanDao;
import com.bytter.finance.domain.equity.dao.CmsEquityRepoPlanDetailDao;
import com.bytter.finance.domain.equity.entity.CmsEquityRepo;
import com.bytter.finance.domain.equity.entity.CmsEquityRepoPlan;
import com.bytter.finance.domain.equity.entity.CmsEquityRepoPlanDetail;
import com.bytter.finance.domain.equity.vo.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 股权融资回购计划修改
 *
 * @author zhenghj
 */
@Service("cmsEquityRepoPlanUpdateService")
@WorkFlow(CmsWorkFlowConstant.CMS_EQUITY_REPOPLAN_UPDATE)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_EQUITY_REPOPLAN_UPDATE)
@Transactional
public class CmsEquityRepoPlanUpdateService extends BaseService<CmsEquityRepoPlan, String>
        implements IWorkFlowListener, IUserExcelVerifyHandler<CmsEquityRepoPlanDetailVO> {
    @Autowired
    private CmsEquityRepoPlanDao cmsEquityRepoPlanDao;
    @Autowired
    private CmsEquityRepoPlanDetailDao cmsEquityRepoPlanDetailDao;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;
    @Autowired
    private CmsEquityRepoPlanDetailService cmsEquityRepoPlanDetailService;
    @Autowired
    private CmsEquityRepoDao cmsEquityRepoDao;
    /**
     * 股权融资列表数据查询(回购计划修改查询)
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataEquityRepoPlanUpdateTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_EQUITY_REPOPLANUPDATE_QUERY,
                user.getId());
        return cmsEquityRepoPlanDao.getDataEquityRepoPlanUpdateTable(dataTablesInput, corpAuth);
    }
    /**
     * 计划修改审批查询列表 - 数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput datatableUpdateApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_EQUITY_REPOPLAN_UPDATE, dataTablesInput);
        DataTablesOutput dp = cmsEquityRepoPlanDao.getDatatableApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍 :保存<br>
     *
     * @param vo
     */
    public void repoPlanUpdate(CmsEquityRepoPlanVO vo, ICurrentUser user) {
        CmsEquityRepoPlan cmsEquityRepoPlan;
        // 根据融资编号查询股权融资信息
        CmsEquityInfoVO infoVO = cmsEquityInfoService.getCmsEquityInfoByEquityCode(vo.getEquityCode());
        // 判断计划主表id是否为空，空则新增，不为空则修改
        if (StringUtils.isNotEmpty(vo.getId())) {
            cmsEquityRepoPlan = get(vo.getId());
            if (user != null) {
                cmsEquityRepoPlan.setUpdateBy(user.getLoginName());
            }
            cmsEquityRepoPlan.setUpdateDate(new Date());
        } else {
            cmsEquityRepoPlan = new CmsEquityRepoPlan();
            if (user != null) {
                cmsEquityRepoPlan.setCreateBy(user.getLoginName());
            }
            cmsEquityRepoPlan.setCreateDate(new Date());
        }
        BeanUtils.copyNotNullProperties(vo, cmsEquityRepoPlan);
        if (infoVO != null) {
            cmsEquityRepoPlan.setCorpId(infoVO.getCorpId());
        }
        cmsEquityRepoPlan.setStatus(CmsConstant.LEASE_COMMITAPPROVE_11);
        // 如果计划主表不为空，则先删除原明细数据
        if (StringUtils.isNotEmpty(vo.getId())) {
            // 更新计划主表数据
            cmsEquityRepoPlanDao.update(cmsEquityRepoPlan);
        } else {
            // 保存计划主表数据
            cmsEquityRepoPlanDao.save(cmsEquityRepoPlan);
        }
        // 更新ids
        List<String> detailUpdateIds = new ArrayList<>();
        // 新增集合
        List<CmsEquityRepoPlanDetail> planAddDetails = new ArrayList<>();
        // 更新集合
        List<CmsEquityRepoPlanDetail> planUpdateDetails = new ArrayList<>();
        // 组装计划明细表数据
        if (vo.getDetailDtl().size() > 0) {
            CmsEquityRepoPlanDetail planDetail;
            for (CmsEquityRepoPlanDetailVO detailVO : vo.getDetailDtl()) {
                if (StringUtils.isNotEmpty(detailVO.getEquityCode())) {
                    // 更新
                    if (StringUtils.isNotEmpty(detailVO.getId())) {
                        detailUpdateIds.add(detailVO.getId());
                        planDetail = cmsEquityRepoPlanDetailService.get(detailVO.getId());
                        BeanUtils.copyNotNullProperties(detailVO, planDetail);
                        planDetail.setAddOrUpdate(CmsConstant.ADD_OR_UPDATE_1);
                        if (user != null) {
                            planDetail.setUpdateBy(user.getLoginName());
                        }
                        planDetail.setUpdateDate(new Date());
                        planUpdateDetails.add(planDetail);
                    } else {// 新增
                        planDetail = new CmsEquityRepoPlanDetail();
                        BeanUtils.copyNotNullProperties(detailVO, planDetail);
                        planDetail.setRepoPlanId(cmsEquityRepoPlan.getId());
                        planDetail.setValidSign(CmsConstant.VALIDSIGN_1);
                        planDetail.setAddOrUpdate(CmsConstant.ADD_OR_UPDATE_1);
                        if (user != null) {
                            planDetail.setCreateBy(user.getLoginName());
                        }
                        planDetail.setCreateDate(new Date());
                        planAddDetails.add(planDetail);
                    }
                }
            }
        }
        // 删除除更新id以外的明细
        if (StringUtils.isNotEmpty(vo.getId())) {
            cmsEquityRepoPlanDetailService.deleteRepoPlanDetailByPlanId(vo.getId(), detailUpdateIds);
        }
        // 新增
        if (!CollectionUtils.isEmpty(planAddDetails)) {
            cmsEquityRepoPlanDetailService.batchSave(planAddDetails);
        }
        // 修改
        if (!CollectionUtils.isEmpty(planUpdateDetails)) {
            cmsEquityRepoPlanDetailService.batchUpdate(planUpdateDetails);
        }
        // notificationService.complete(NotificationConstant.CMS.EQUITY_INFO, cmsEquityRepoPlan.getId(), user.getId());
        // 提交 启动审批流程
        // 更新融资明细状态为回购计划修改审批中
        vo.setId(cmsEquityRepoPlan.getId());
        cmsEquityInfoService.updateBizStatusByEquityCode(vo.getEquityCode(), CmsConstant.EQUITY_DETAIL_BIZSTATUS_45);
         getStartHandler().handler(CmsWorkFlowConstant.CMS_EQUITY_REPOPLAN_UPDATE, cmsEquityRepoPlan.getId(), vo,
                cmsEquityRepoPlan.getCorpId(), user.getId());
    }
    /**
     * 功能介绍 : 修改或者新增<br>
     */
    public AjaxJson saveDetail(CmsEquityRepoPlanDetail cmsEquityRepoPlanDetail) {
        AjaxJson ajaxJson = new AjaxJson();
        if (StringUtils.isEmpty(cmsEquityRepoPlanDetail.getId())) {
            cmsEquityRepoPlanDetailDao.save(cmsEquityRepoPlanDetail);
        } else {
            cmsEquityRepoPlanDetailDao.update(cmsEquityRepoPlanDetail);
        }
        ajaxJson.setObj(cmsEquityRepoPlanDetail.getId());
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }
    @Override
    public BaseDao<CmsEquityRepoPlan, String> getEntityDao() {
        return cmsEquityRepoPlanDao;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        CmsEquityRepoPlan info = this.get(businessKey,true);
        if (result) {// 审批通过
            info.setStatus(CmsConstant.APPROVED);
            // 更新融资明细状态为回购计划修改审批通过
            cmsEquityInfoService.updateBizStatusByEquityCode(info.getEquityCode(),
                    CmsConstant.EQUITY_DETAIL_BIZSTATUS_50);
            // 对已选择为是的还款进行缴付数据处理
            List<CmsEquityRepoPlanDetailVO> planDetailList = cmsEquityRepoPlanDetailService
                    .listRepoPlanDetailByEquityCodeOrPlanId(null, info.getEquityCode());
            for (CmsEquityRepoPlanDetailVO detailVO : planDetailList) {
                if (!EmptyUtils.isEmpty(detailVO.getIsPay())) {
                    if (detailVO.getIsPay().equals("1")) {
                        List<CmsEquityRepoVO> equityPayTheVOS = cmsEquityRepoDao.getByPlanDetail(detailVO.getId());// 根据明细查找回购
                        if (equityPayTheVOS.size() == 0) {
                            CmsEquityInfoDetailVO infodetailVO = cmsEquityInfoService.getDetail(info.getEquityCode());
                            CmsEquityRepo cmsEquityRepo = new CmsEquityRepo();
                            cmsEquityRepo.setEquityCode(infodetailVO.getEquityCode());
                            cmsEquityRepo.setFinancingWay(infodetailVO.getFinancingWay());
                            cmsEquityRepo.setInvestor(infodetailVO.getInvestor());
                            cmsEquityRepo.setInvWay(infodetailVO.getInvWay());
                            cmsEquityRepo.setInvMoney(infodetailVO.getInvMoney());
                            cmsEquityRepo.setRepoDate(detailVO.getRepoDate());
                            cmsEquityRepo.setThisRepoMoney(detailVO.getRepoMoney());
                            cmsEquityRepo.setStatus(CmsConstant.APPROVED);
                            cmsEquityRepo.setRepoPlanDetailId(detailVO.getId());
                            cmsEquityRepo.setRepoPlanId(detailVO.getRepoPlanId());
                            cmsEquityRepo.setOnlineSign("0");
                            cmsEquityRepo.setReRepoMoney(detailVO.getReRepoMoney());
                            cmsEquityRepoDao.save(cmsEquityRepo);
                            if (detailVO.getReRepoMoney().compareTo(BigDecimal.ZERO) == 0) {// 更新明细业务状态
                                cmsEquityInfoService.updateBizStatusByEquityCode(info.getEquityCode(),
                                        CmsConstant.EQUITY_DETAIL_BIZSTATUS_60);
                            } else {
                                cmsEquityInfoService.updateBizStatusByEquityCode(info.getEquityCode(),
                                        CmsConstant.EQUITY_DETAIL_BIZSTATUS_56);
                            }
                        }
                    }
                }
            }
        } else {// 打回
            info.setStatus(CmsConstant.BACK);
            cmsEquityInfoService.updateBizStatusByEquityCode(info.getEquityCode(),
                    CmsConstant.EQUITY_DETAIL_BIZSTATUS_41);
            String submitUser = StringUtils.isEmpty(info.getUpdateBy()) ? info.getCreateBy() : info.getUpdateBy();// 提交人
            // notificationService.sendToUser(info.getId(), submitUser, NotificationConstant.CMS.EQUITY_INFO,
            // info);//添加股权融资登记的消息提示
        }
        this.saveOrUpdate(info);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(CmsEquityRepoPlanDetailVO cmsEquityRepoPlanDetailVO,
            Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<CmsEquityRepoPlanDetailVO> list, Object... objects) {
        /*
         * if (!ListUtils.isNullOrEmpty(list)) { for (CmsEquityRepoPlanDetailVO detailVO : list) { List<String>
         * errorMsgList = new ArrayList<>(); if (!EmptyUtils.isEmpty(detailVO.getFinancingWayName())) { if
         * (detailVO.getFinancingWayName().equals("转让")) { detailVO.setFinancingWay("1"); } else if
         * (detailVO.getFinancingWayName().equals("增资")) { detailVO.setFinancingWay("2"); } else {
         * errorMsgList.add("请输入正确的融资方式"); } } else { errorMsgList.add("融资方式不能为空"); } if
         * (EmptyUtils.isEmpty(detailVO.getInvestor())) { errorMsgList.add("出资人不能为空"); } if
         * (!EmptyUtils.isEmpty(detailVO.getInvWayName())) { if (detailVO.getInvWayName().equals("现金")) {
         * detailVO.setInvWay("1"); } else if (detailVO.getInvWayName().equals("实物资产")) { detailVO.setInvWay("2"); if
         * (EmptyUtils.isEmpty(detailVO.getAssetName())) { errorMsgList.add("实物资产资产名称不能为空"); } } else {
         * errorMsgList.add("请输入正确的出资方式"); } } else { errorMsgList.add("出资方式不能为空"); } if
         * (EmptyUtils.isEmpty(detailVO.getInvMoney())) { errorMsgList.add("出资金额不能为空"); } if
         * (!EmptyUtils.isEmpty(detailVO.getEquityPro())) { BigDecimal s = new BigDecimal(100);
         * detailVO.setEquityPro(detailVO.getEquityPro().divide(s)); } detailVO.addErrorMsg(errorMsgList); } }
         */
    }
    public AjaxJson saveImport(List<CmsEquityRepoPlanDetailVO> list) {
        AjaxJson ajaxJson = new AjaxJson();
        String addIds = null;
        for (CmsEquityRepoPlanDetailVO detailVO : list) {
            CmsEquityRepoPlanDetail detail = new CmsEquityRepoPlanDetail();
            BeanUtils.copyNotNullProperties(detailVO, detail);
            // String equityCode = this.getEquityCode();
            // detail.setEquityCode(equityCode);
            this.saveDetail(detail);
            if (addIds == null) {
                addIds = detail.getId();
            } else {
                addIds = addIds + "," + detail.getId();
            }
        }
        ajaxJson.setObj(addIds);
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }
}
