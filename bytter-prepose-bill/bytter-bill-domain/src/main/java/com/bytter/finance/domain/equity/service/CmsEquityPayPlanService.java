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
import com.bytter.finance.domain.equity.dao.CmsEquityPayPlanDao;
import com.bytter.finance.domain.equity.dao.CmsEquityPayPlanDetailDao;
import com.bytter.finance.domain.equity.dao.CmsEquityPayTheDao;
import com.bytter.finance.domain.equity.entity.CmsEquityPayPlan;
import com.bytter.finance.domain.equity.entity.CmsEquityPayPlanDetail;
import com.bytter.finance.domain.equity.entity.CmsEquityPayThe;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanVO;
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
 * 缴付/过户计划登记
 *
 * @author zhenghj
 */
@Service("cmsEquityPayPlanService")
@WorkFlow(CmsWorkFlowConstant.CMS_EQUITY_PAYPLAN)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_EQUITY_PAYPLAN)
@Transactional
public class CmsEquityPayPlanService extends BaseService<CmsEquityPayPlan, String>
        implements IWorkFlowListener, IUserExcelVerifyHandler<CmsEquityPayPlanDetailVO> {
    @Autowired
    private CmsEquityPayPlanDao cmsEquityPayPlanDao;
    @Autowired
    private CmsEquityPayPlanDetailDao cmsEquityPayPlanDetailDao;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;
    @Autowired
    private CmsEquityPayPlanDetailService cmsEquityPayPlanDetailService;
    @Autowired
    private CmsEquityPayTheDao cmsEquityPayTheDao;
    /**
     * 根据计划主表id或融资编号获取信息
     */
    public CmsEquityPayPlanVO getPayPlanByEquityCodeOrId(String id, String equityCode) {
        return cmsEquityPayPlanDao.getPayPlanByEquityCodeOrId(id, equityCode);
    }
    /**
     * 股权融资列表数据查询（计划登记查询）
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataEquityPayPlanTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_EQUITY_PAYPLAN_QUERY, user.getId());
        return cmsEquityPayPlanDao.getDataEquityPayPlanTable(dataTablesInput, corpAuth);
    }
    /**
     * 计划登记审批查询列表 - 数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDatatableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_EQUITY_PAYPLAN,
                dataTablesInput);
        DataTablesOutput dp = cmsEquityPayPlanDao.getDatatableApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍 :保存<br>
     *
     * @param vo
     */
    public void save(CmsEquityPayPlanVO vo, ICurrentUser user) {
        CmsEquityPayPlan cmsEquityPayPlan;
        // 根据融资编号查询股权融资信息
        CmsEquityInfoVO infoVO = cmsEquityInfoService.getCmsEquityInfoByEquityCode(vo.getEquityCode());
        // 判断计划主表id是否为空，空则新增，不为空则修改
        if (StringUtils.isNotEmpty(vo.getId())) {
            cmsEquityPayPlan = get(vo.getId());
            if (user != null) {
                cmsEquityPayPlan.setUpdateBy(user.getLoginName());
            }
            cmsEquityPayPlan.setUpdateDate(new Date());
        } else {
            cmsEquityPayPlan = new CmsEquityPayPlan();
            cmsEquityPayPlan.setPayMoney(BigDecimal.ZERO);
            if (user != null) {
                cmsEquityPayPlan.setCreateBy(user.getLoginName());
            }
            cmsEquityPayPlan.setCreateDate(new Date());
        }
        BeanUtils.copyNotNullProperties(vo, cmsEquityPayPlan);
        if (infoVO != null) {
            cmsEquityPayPlan.setCorpId(infoVO.getCorpId());
        }
        cmsEquityPayPlan.setStatus(CmsConstant.LEASE_COMMITAPPROVE_11);
        // 如果计划主表不为空，则先删除原明细数据
        if (StringUtils.isNotEmpty(vo.getId())) {
            // 更新计划主表数据
            cmsEquityPayPlanDao.update(cmsEquityPayPlan);
        } else {
            // 保存计划主表数据
            cmsEquityPayPlanDao.save(cmsEquityPayPlan);
        }
        // 更新ids
        List<String> detailUpdateIds = new ArrayList<>();
        // 新增集合
        List<CmsEquityPayPlanDetail> planAddDetails = new ArrayList<>();
        // 更新集合
        List<CmsEquityPayPlanDetail> planUpdateDetails = new ArrayList<>();
        // 组装计划明细表数据
        if (vo.getDetailDtl().size() > 0) {
            CmsEquityPayPlanDetail planDetail;
            for (CmsEquityPayPlanDetailVO detailVO : vo.getDetailDtl()) {
                if (StringUtils.isNotEmpty(detailVO.getEquityCode())) {
                    // 更新
                    if (StringUtils.isNotEmpty(detailVO.getId())) {
                        detailUpdateIds.add(detailVO.getId());
                        planDetail = cmsEquityPayPlanDetailService.get(detailVO.getId());
                        BeanUtils.copyNotNullProperties(detailVO, planDetail);
                        if (user != null) {
                            planDetail.setUpdateBy(user.getLoginName());
                        }
                        planDetail.setUpdateDate(new Date());
                        planUpdateDetails.add(planDetail);
                    } else {// 新增
                        planDetail = new CmsEquityPayPlanDetail();
                        BeanUtils.copyNotNullProperties(detailVO, planDetail);
                        planDetail.setPlanId(cmsEquityPayPlan.getId());
                        planDetail.setValidSign(CmsConstant.VALIDSIGN_1);
                        planDetail.setAddOrUpdate(CmsConstant.ADD_OR_UPDATE_0);
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
            cmsEquityPayPlanDetailService.deletePayPlanDetailByPlanId(vo.getId(), detailUpdateIds);
        }
        // 新增
        if (!CollectionUtils.isEmpty(planAddDetails)) {
            cmsEquityPayPlanDetailService.batchSave(planAddDetails);
        }
        // 修改
        if (!CollectionUtils.isEmpty(planUpdateDetails)) {
            cmsEquityPayPlanDetailService.batchUpdate(planUpdateDetails);
        }
        // notificationService.complete(NotificationConstant.CMS.EQUITY_INFO, cmsEquityPayPlan.getId(), user.getId());
        // 提交 启动审批流程
        // 更新融资明细状态为计划登记审批中
        vo.setId(cmsEquityPayPlan.getId());
        cmsEquityInfoService.updateBizStatusByEquityCode(vo.getEquityCode(), CmsConstant.EQUITY_DETAIL_BIZSTATUS_5);
         getStartHandler().handler(CmsWorkFlowConstant.CMS_EQUITY_PAYPLAN, cmsEquityPayPlan.getId(), vo,
                cmsEquityPayPlan.getCorpId(), user.getId());
    }
    /**
     * 功能介绍 : 修改或者新增<br>
     */
    public AjaxJson saveDetail(CmsEquityPayPlanDetail cmsEquityPayPlanDetail) {
        AjaxJson ajaxJson = new AjaxJson();
        if (StringUtils.isEmpty(cmsEquityPayPlanDetail.getId())) {
            cmsEquityPayPlanDetailDao.save(cmsEquityPayPlanDetail);
        } else {
            cmsEquityPayPlanDetailDao.update(cmsEquityPayPlanDetail);
        }
        ajaxJson.setObj(cmsEquityPayPlanDetail.getId());
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }
    @Override
    public BaseDao<CmsEquityPayPlan, String> getEntityDao() {
        return cmsEquityPayPlanDao;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        CmsEquityPayPlan info = this.get(businessKey,true);
        if (result) {// 审批通过
            info.setStatus(CmsConstant.APPROVED);
            // 更新融资明细状态为计划登记审批通过
            cmsEquityInfoService.updateBizStatusByEquityCode(info.getEquityCode(),
                    CmsConstant.EQUITY_DETAIL_BIZSTATUS_10);
            // 对已选择为是的还款进行缴付数据处理
            List<CmsEquityPayPlanDetailVO> planDetailList = cmsEquityPayPlanDetailService
                    .listPayPlanDetailByEquityCodeOrPlanId(null, info.getEquityCode());
            for (CmsEquityPayPlanDetailVO detailVO : planDetailList) {
                if (!EmptyUtils.isEmpty(detailVO.getIsPay())) {
                    if (detailVO.getIsPay().equals("1")) {
                        CmsEquityInfoDetailVO infodetailVO = cmsEquityInfoService.getDetail(info.getEquityCode());
                        CmsEquityPayThe cmsEquityPayThe = new CmsEquityPayThe();
                        cmsEquityPayThe.setEquityCode(infodetailVO.getEquityCode());
                        cmsEquityPayThe.setFinancingWay(infodetailVO.getFinancingWay());
                        cmsEquityPayThe.setInvestor(infodetailVO.getInvestor());
                        cmsEquityPayThe.setInvWay(infodetailVO.getInvWay());
                        cmsEquityPayThe.setInvMoney(infodetailVO.getInvMoney());
                        cmsEquityPayThe.setAssName(infodetailVO.getAssetName());
                        cmsEquityPayThe.setPayDate(detailVO.getPayDate());
                        cmsEquityPayThe.setPayMoney(detailVO.getPayMoney());
                        cmsEquityPayThe.setRmbMoney(detailVO.getPayMoney());
                        cmsEquityPayThe.setRePayMoney(detailVO.getRePayMoney());
                        cmsEquityPayThe.setPlanId(detailVO.getPlanId());
                        cmsEquityPayThe.setPlanDetailId(detailVO.getId());
                        cmsEquityPayThe.setStatus(CmsConstant.APPROVED);
                        cmsEquityPayTheDao.save(cmsEquityPayThe);// 保存新增的缴付数据
                        if (detailVO.getRePayMoney().compareTo(BigDecimal.ZERO) == 0) {
                            cmsEquityInfoService.updateBizStatusByEquityCode(info.getEquityCode(),
                                    CmsConstant.EQUITY_DETAIL_BIZSTATUS_30);
                        } else {
                            cmsEquityInfoService.updateBizStatusByEquityCode(info.getEquityCode(),
                                    CmsConstant.EQUITY_DETAIL_BIZSTATUS_26);
                        }
                    }
                }
            }
        } else {// 打回
            info.setStatus(CmsConstant.BACK);
            cmsEquityInfoService.updateBizStatusByEquityCode(info.getEquityCode(),
                    CmsConstant.EQUITY_DETAIL_BIZSTATUS_1);
            String submitUser = StringUtils.isEmpty(info.getUpdateBy()) ? info.getCreateBy() : info.getUpdateBy();// 提交人
            // notificationService.sendToUser(info.getId(), submitUser, NotificationConstant.CMS.EQUITY_INFO,
            // info);//添加股权融资登记的消息提示
        }
        this.saveOrUpdate(info);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(CmsEquityPayPlan entity, ICurrentUser user) {
        entity.setCreateDate(new Date());
        entity.setCreateBy(user.getId());
        super.saveEntity(entity);
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(CmsEquityPayPlanDetailVO cmsEquityPayPlanDetailVO,
            Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<CmsEquityPayPlanDetailVO> list, Object... objects) {
        /*
         * if (!ListUtils.isNullOrEmpty(list)) { for (CmsEquityPayPlanDetailVO detailVO : list) { List<String>
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
    public AjaxJson saveImport(List<CmsEquityPayPlanDetailVO> list) {
        AjaxJson ajaxJson = new AjaxJson();
        String addIds = null;
        for (CmsEquityPayPlanDetailVO detailVO : list) {
            CmsEquityPayPlanDetail detail = new CmsEquityPayPlanDetail();
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
