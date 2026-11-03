package com.bytter.finance.domain.bond.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.bond.dao.CmsBondRepayPlanDao;
import com.bytter.finance.domain.bond.entity.CmsBondRepayPlan;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanDtlVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanVo;
import com.bytter.finance.enums.CmsBondMessageEnum;
import com.bytter.system.api.ICorpService;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
/**
 * @author mof
 * @version 1.0
 * @Title: Service
 * @Description: 债券还款计划登记
 * @date 2022-06-06 10:07:46
 */
@WorkFlow(CmsWorkFlowConstant.CMSBONDREPAY_PLAN)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMSBONDREPAY_PLAN)
@Service("cmsBondRepayPlanService")
@Transactional
public class CmsBondRepayPlanService extends BaseService<CmsBondRepayPlan, String> implements IWorkFlowListener {
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsBondRepayPlanDao cmsBondRepayPlanDao;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Autowired
    private CmsBondRepayPlanDtlService cmsBondRepayPlanDtlService;
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;
    @Override
    public BaseDao<CmsBondRepayPlan, String> getEntityDao() {
        return cmsBondRepayPlanDao;
    }
    /**
     * 保存债券还款计划
     *
     * @param entity
     */
    public void save(CmsBondRepayPlan entity) {
        super.saveEntity(entity);
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String isSaveSign) {
        String resource = "";
        if (CmsConstant.IS_TRUE.equals(isSaveSign)) {
            resource = ResourceConstant.CMS_BOND_CMSBONDREPAYPLAN_QUERY;
        }
        if (CmsConstant.IS_FALSE.equals(isSaveSign)) {
            resource = ResourceConstant.CMS_BOND_UPDATECMSBONDREPAYPLAN_QUERY;
        }
        // 获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(resource, dataTablesInput.getCurrentUserId());
        // 筛选债券信息
        List<String> registerIdList = this.getByRepayPlan(corpAuthList, isSaveSign);
        DataTablesOutput output = cmsBondRegisterInfoService.getDataTablesByIds(dataTablesInput, registerIdList);
        return output;
    }
    /**
     * 筛选债券信息
     *
     * @param corpAuthList
     *            单位权限
     * @return 发行信息集合
     */
    public List<String> getByRepayPlan(List<String> corpAuthList, String isSave) {
        Set<String> registerIdSet = new HashSet<String>();
        List<CmsBondRepayPlanVo> list = cmsBondRepayPlanDao.getByRepayPlan(corpAuthList, isSave);
        if (!EmptyUtils.isEmptyList(list)) {
            for (CmsBondRepayPlanVo vo : list) {
                registerIdSet.add(vo.getRegisterId());
            }
        }
        List<String> registerIdList = new ArrayList<>();
        for (String elem : registerIdSet) {
            registerIdList.add(elem);
        }
        return registerIdList;
    }
    public CmsBondRepayPlanVo getByIssueId(String issueId) {
        return cmsBondRepayPlanDao.getByIssueId(issueId);
    }
    public CmsBondRepayPlanVo getByRegisterId(String registerId) {
        if (EmptyUtils.isEmpty(registerId)) {
            return new CmsBondRepayPlanVo();
        }
        List<CmsBondRepayPlanVo> list = cmsBondRepayPlanDao.getRegisterById(registerId);
        if (ListUtils.isNullOrEmpty(list)) {
            return new CmsBondRepayPlanVo();
        }
        return list.get(0);
    }
    public List<CmsBondRepayPlanVo> getAllByRegisterId(String registerId) {
        List<CmsBondRepayPlanVo> list = cmsBondRepayPlanDao.getRegisterById(registerId);
        return list;
    }
    /**
     * 筛选已经添加了还款计划的债券信息
     *
     * @return 筛选添加还款计划的数据
     */
    public List<String> getAllDataByPlan(List<String> corpAuthList) {
        Set<String> registerIdSet = new HashSet<String>();
        List<CmsBondRepayPlanVo> list = cmsBondRepayPlanDao.getAllDataByPlan(corpAuthList);
        if (!EmptyUtils.isEmptyList(list)) {
            for (CmsBondRepayPlanVo vo : list) {
                registerIdSet.add(vo.getRegisterId());
            }
        }
        List<String> registerIdList = new ArrayList<>();
        for (String elem : registerIdSet) {
            registerIdList.add(elem);
        }
        return registerIdList;
    }
    /**
     * 通过申请id查询相关信息
     *
     * @param id
     * @return
     */
    public CmsBondRepayPlanVo getInfoById(String id) {
        List<CmsBondRepayPlanVo> cmsBondRepayPlanVos = cmsBondRepayPlanDao.getInfoById(id);
        if (ListUtils.isNullOrEmpty(cmsBondRepayPlanVos)) {
            return null;
        }
        return cmsBondRepayPlanVos.get(0);
    }
    /**
     * 新增/修改-保存债券还款计划修改数据
     *
     * @param vo
     */
    public String saveOrUpdate(String operator, CmsBondRepayPlanVo vo, ICurrentUser user) {
        String id = "";
        if (!StringUtils.isNotEmpty(vo.getRegisterId())) {
            return id;
        }
        id = vo.getRegisterId();
        // 获取债券发行信息
        CmsBondIssueInfoVo cmsBondIssueInfoVo = cmsBondIssueInfoService.getByIssueId(vo.getBondIssueNum());
        // 获取债券计划主表信息
        CmsBondRepayPlanVo cmsBondRepayPlanVo = cmsBondRepayPlanDao.getByIssueId(vo.getBondIssueNum());
        if (AccebillConstant.BILL_OPERATE_ADD.equals(operator)) {
            if (CmsConstant.IS_FALSE.equals(cmsBondRepayPlanVo.getIsSave())) {
                throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDCAPITALPLAN_PLANISSAVEISTRUE);
            }
        } else {
            if (CmsConstant.IS_TRUE.equals(cmsBondRepayPlanVo.getIsSave())) {
                throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDCAPITALPLAN_PLANISSAVEISFALSE);
            }
        }
        // 校验还款计划的明细
        Map<String, List<CmsBondRepayPlanDtlVo>> planDtlVoMap = new HashMap<>();
        validata(vo, cmsBondIssueInfoVo, planDtlVoMap);
        Map<String, CmsBondRepayPlan> cmsBondRepayPlanMap = new HashMap<>();
        List<String> planIds = new ArrayList<>();
        List<String> issueIds = new ArrayList<>();
        String bizStatus;
        // 新增或修改
        if (AccebillConstant.BILL_OPERATE_ADD.equals(operator)) {
            bizStatus = CmsConstant.BOND_BIZ_STATUS_99;
        } else {
            bizStatus = CmsConstant.BOND_BIZ_STATUS_100;
        }
        for (String key : planDtlVoMap.keySet()) {
            List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtlVoList = planDtlVoMap.get(key);
            if (EmptyUtils.isEmptyList(cmsBondRepayPlanDtlVoList)) {
                continue;
            }
            String issueId = cmsBondRepayPlanDtlVoList.get(0).getIssueId();
            issueIds.add(issueId);
            // 查询还款计划id
            CmsBondRepayPlan plan = this.get(cmsBondRepayPlanVo.getId());
            // 新增租金计划主表
            plan.setRegisterId(vo.getRegisterId());
            plan.setIssueId(issueId);
            plan.setIsSave(CmsConstant.IS_FALSE);
            plan.setStatus(CmsConstant.APPROVED);
            plan.setCorpId(vo.getCorpId());
            plan.setRegisterId(vo.getRegisterId()); // 债券表id
            plan.setRepayType(vo.getRepayType()); // 还款方式
            if (!EmptyUtils.isEmpty(vo.getRepayStartDate())) {
                plan.setRepayStartDate(vo.getRepayStartDate());// 还款开始日
            }
            plan.setCreateBy(user.getLoginName());
            plan.setCreateDate(DateUtils.getDate());
            this.saveOrUpdate(plan);
            // 保存还款计划子表
            planIds.add(plan.getId());
            cmsBondRepayPlanMap.put(key, plan);
        }
        // 修改债券注册表总状态
        cmsBondIssueInfoService.updateStatus(issueIds, bizStatus);
        cmsBondRepayPlanDtlService.subSave(vo, planDtlVoMap, cmsBondRepayPlanMap, planIds, user);
        // 审批功能取消
        // for (String key : cmsBondRepayPlanMap.keySet()) {
        // CmsBondRepayPlan plan = cmsBondRepayPlanMap.get(key);
        // if (AccebillConstant.BILL_OPERATE_ADD.equals(operator)) {
        // msService.insertProcessMsg(plan.getId(), BondConstant.BONDREPAYPLAN_BOND_NO,
        // CmsProcessMsgConstant.MSGA_SUBMIT, "新增提交", user);
        //  getStartHandler().handler(CmsWorkFlowConstant.CMSBONDREPAY_PLAN, plan.getId(), plan, plan.getCorpId(),
        // user.getId());
        // }
        // if (AccebillConstant.BILL_OPERATE_UPDATE.equals(operator)) {
        // msService.insertProcessMsg(plan.getId(), BondConstant.BONDREPAYPLAN_BOND_NO,
        // CmsProcessMsgConstant.MSGA_SUBMIT, "修改提交", user);
        //  getStartHandler().handler(CmsWorkFlowConstant.UPDATE_CMSBONDREPAY_PLAN, plan.getId(), plan, plan.getCorpId(),
        // user.getId());
        // }
        // }
        return id;
    }
    /**
     * 新增/修改-保存债券还款计划修改数据（一次性保存多次发行，暂不使用）
     *
     * @param vo
     */
    /*
     * public String saveOrUpdate(String operator, CmsBondRepayPlanVo vo, ICurrentUser user) { String id = ""; if
     * (!StringUtils.isNotEmpty(vo.getRegisterId())) { return id; } id = vo.getRegisterId(); // 获取债券发行信息
     * List<CmsBondIssueInfoVo> cmsBondIssueInfoVoList =
     * cmsBondIssueInfoService.getRegisterByIdAndOperator(vo.getRegisterId(), operator); Map<String, CmsBondIssueInfoVo>
     * cmsBondIssueInfoVoMap = new HashMap<>(); for (CmsBondIssueInfoVo cmsBondIssueInfoVo : cmsBondIssueInfoVoList) {
     * cmsBondIssueInfoVoMap.put(cmsBondIssueInfoVo.getBondIssueNum(), cmsBondIssueInfoVo); } // 校验还款计划的明细 Map<String,
     * List<CmsBondRepayPlanDtlVo>> planDtlVoMap = new HashMap<>(); validata(vo, cmsBondIssueInfoVoMap, planDtlVoMap);
     * Map<String, CmsBondRepayPlan> cmsBondRepayPlanMap = new HashMap<>(); List<String> planIds = new ArrayList<>();
     * List<String> issueIds = new ArrayList<>(); String bizStatus = ""; // 新增或修改 if
     * (AccebillConstant.BILL_OPERATE_ADD.equals(operator)) { for (String key : planDtlVoMap.keySet()) {
     * List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtlVoList = planDtlVoMap.get(key); if
     * (EmptyUtils.isEmptyList(cmsBondRepayPlanDtlVoList)) { continue; } String issueId =
     * cmsBondRepayPlanDtlVoList.get(0).getIssueId(); // 根据债券发行编号查询债券还款计划主表id CmsBondRepayPlanVo cmsBondRepayPlanVo =
     * cmsBondRepayPlanDao.getByIssueId(issueId); issueIds.add(issueId); // 查询还款计划id bizStatus =
     * CmsConstant.BOND_BIZ_STATUS_51; CmsBondRepayPlan plan = new CmsBondRepayPlan(); if (cmsBondRepayPlanVo != null) {
     * plan = this.get(cmsBondRepayPlanVo.getId()); // 更新债券还款计划主表信息 plan.setStatus(CmsConstant.COMMITAPPROVE);
     * plan.setRepayType(vo.getRepayType()); // 还款方式 plan.setUpdateBy(user.getLoginName());
     * plan.setUpdateDate(DateUtils.getDate()); this.saveOrUpdate(plan); // 保存还款计划子表 planIds.add(plan.getId());
     * cmsBondRepayPlanMap.put(key, plan); } else { // 新增租金计划主表 plan.setRegisterId(vo.getRegisterId());
     * plan.setIssueId(issueId); plan.setIsSave(CmsConstant.IS_TRUE); plan.setStatus(CmsConstant.COMMITAPPROVE);
     * plan.setCorpId(vo.getCorpId()); plan.setRegisterId(vo.getRegisterId()); //债券表id
     * plan.setRepayType(vo.getRepayType()); // 还款方式 if (!EmptyUtils.isEmpty(vo.getRepayStartDate())) {
     * plan.setRepayStartDate(vo.getRepayStartDate());// 还款开始日 } plan.setCreateBy(user.getLoginName());
     * plan.setCreateDate(DateUtils.getDate()); this.save(plan); // 保存还款计划子表 planIds.add(plan.getId());
     * cmsBondRepayPlanMap.put(key, plan); } } } else { for (String key : planDtlVoMap.keySet()) {
     * List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtlVoList = planDtlVoMap.get(key); if
     * (EmptyUtils.isEmptyList(cmsBondRepayPlanDtlVoList)) { continue; } bizStatus = CmsConstant.BOND_BIZ_STATUS_61; //
     * 根据单号查询债券还款计划主表id String issueId = cmsBondRepayPlanDtlVoList.get(0).getIssueId(); CmsBondRepayPlanVo
     * cmsBondRepayPlanVo = cmsBondRepayPlanDao.getByIssueId(issueId); issueIds.add(issueId); if (cmsBondRepayPlanVo !=
     * null) { // 更新租金计划主表信息 CmsBondRepayPlan cms = this.get(cmsBondRepayPlanVo.getId());
     * cms.setRepayType(vo.getRepayType()); // 还款方式 cms.setStatus(CmsConstant.COMMITAPPROVE);
     * cms.setIsSave(CmsConstant.IS_FALSE); cms.setUpdateBy(user.getLoginName());
     * cms.setUpdateDate(DateUtils.getDate()); this.saveOrUpdate(cms); // 保存还款计划子表 planIds.add(cms.getId());
     * cmsBondRepayPlanMap.put(key, cms); } } } // 修改债券注册表总状态 cmsBondIssueInfoService.updateStatus(issueIds, bizStatus);
     * cmsBondRepayPlanDtlService.subSave(vo, planDtlVoMap, cmsBondRepayPlanMap, planIds, user);
     * 
     * for (String key : cmsBondRepayPlanMap.keySet()) { CmsBondRepayPlan plan = cmsBondRepayPlanMap.get(key); if
     * (AccebillConstant.BILL_OPERATE_ADD.equals(operator)) { msService.insertProcessMsg(plan.getId(),
     * BondConstant.BONDREPAYPLAN_BOND_NO, CmsProcessMsgConstant.MSGA_SUBMIT, "新增提交", user);
     *  getStartHandler().handler(CmsWorkFlowConstant.CMSBONDREPAY_PLAN, plan.getId(), plan, plan.getCorpId(), user.getId());
     * } if (AccebillConstant.BILL_OPERATE_UPDATE.equals(operator)) { msService.insertProcessMsg(plan.getId(),
     * BondConstant.BONDREPAYPLAN_BOND_NO, CmsProcessMsgConstant.MSGA_SUBMIT, "修改提交", user);
     *  getStartHandler().handler(CmsWorkFlowConstant.UPDATE_CMSBONDREPAY_PLAN, plan.getId(), plan, plan.getCorpId(),
     * user.getId()); } } return id; }
     */
    /**
     * 新增/修改-债券发行保存债券还款计划修改数据
     *
     * @param operator
     *            操作类型
     * @param vo
     *            还款计划vo
     * @param user
     *            当前用户
     * @return 还款计划主表id
     */
    public String issueSaveOrUpdate(String operator, CmsBondRepayPlanVo vo, ICurrentUser user) {
        String id = vo.getId();
        // 新增或修改
        if (AccebillConstant.BILL_OPERATE_ADD.equals(operator) && StringUtils.isEmpty(id)) {
            CmsBondRepayPlan plan = new CmsBondRepayPlan();
            // 新增债券还款计划主表
            plan.setIsSave(CmsConstant.IS_TRUE);
            plan.setStatus(CmsConstant.COMMITAPPROVE);
            plan.setCorpId(vo.getCorpId());
            plan.setRegisterId(vo.getRegisterId());
            plan.setRepayType(vo.getRepayType());
            plan.setRepayStartDate(vo.getRepayStartDate());
            plan.setCreateBy(user.getLoginName());
            plan.setCreateDate(DateUtils.getDate());
            this.save(plan);
            id = plan.getId();
            vo.setId(id);
        }
        cmsBondRepayPlanDtlService.issueSave(vo, user);
        return id;
    }
    /**
     * 校验债券还款计划的明细
     *
     * @param vo
     */
    private void validata(CmsBondRepayPlanVo vo, CmsBondIssueInfoVo cmsBondIssueInfoVo,
            Map<String, List<CmsBondRepayPlanDtlVo>> planDtlVoMap) {
        List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtlVos = vo.getCmsBondRepayPlanDtls();
        if (EmptyUtils.isEmptyList(cmsBondRepayPlanDtlVos)) {
            throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDCAPITALPLAN_EMPTY);
        }
        String bondIssueNum = cmsBondIssueInfoVo.getBondIssueNum();
        List<CmsBondRepayPlanDtlVo> tmpList = new ArrayList<>();
        // 根据发行编号对计划进行分组
        for (CmsBondRepayPlanDtlVo cmsBondRepayPlanDtlVo : cmsBondRepayPlanDtlVos) {
            if (bondIssueNum.equals(cmsBondRepayPlanDtlVo.getBondIssueNum())) {
                cmsBondRepayPlanDtlVo.setIssueId(cmsBondIssueInfoVo.getId());
                tmpList.add(cmsBondRepayPlanDtlVo);
            }
        }
        planDtlVoMap.put(cmsBondIssueInfoVo.getBondIssueNum(), tmpList);
        // 校验
        List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtlVoList = planDtlVoMap.get(cmsBondIssueInfoVo.getBondIssueNum());
        for (int i = 0; i < cmsBondRepayPlanDtlVoList.size(); i++) {
            CmsBondRepayPlanDtlVo delVo = cmsBondRepayPlanDtlVoList.get(i);
            // 以是否已还/抵扣值判断是否有效
            if (EmptyUtils.isEmpty(delVo.getIsRepay())) {
                continue;
            }
            // 还款日期校验
            if (EmptyUtils.isEmpty(delVo.getRepayDate())) {
                throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_PAYDATE_EMPTY);
            } else {
                if (delVo.getRepayDate().compareTo(cmsBondIssueInfoVo.getIssueStartDate()) < 0) {
                    throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_PAYDATE_BEGINDATE);
                }
            }
            // 计划还款日校验
            if (EmptyUtils.isEmpty(delVo.getPlanRepayDate())) {
                throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_PAYPLANDATE_EMPTY);
            }
        }
    }
    /**
     * 校验债券还款计划的明细
     *
     * @param vo
     */
    /*
     * private void validata(CmsBondRepayPlanVo vo, Map<String, CmsBondIssueInfoVo> cmsBondIssueInfoVoMap, Map<String,
     * List<CmsBondRepayPlanDtlVo>> planDtlVoMap) { List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtlVos =
     * vo.getCmsBondRepayPlanDtls(); if (EmptyUtils.isEmptyList(cmsBondRepayPlanDtlVos)) { throw new
     * ParameterException(CmsBondMessageEnum.CMS_BOND_BONDCAPITALPLAN_EMPTY); }
     * 
     * // 根据发行编号对计划进行分组 for (CmsBondRepayPlanDtlVo cmsBondRepayPlanDtlVo : cmsBondRepayPlanDtlVos) { // 判断编号是否存在 String
     * bondIssueNum = cmsBondRepayPlanDtlVo.getBondIssueNum(); if (cmsBondIssueInfoVoMap.containsKey(bondIssueNum)) {
     * List<CmsBondRepayPlanDtlVo> tmpList = planDtlVoMap.get(cmsBondRepayPlanDtlVo.getBondIssueNum()); if (tmpList ==
     * null) { tmpList = new ArrayList<>(); CmsBondIssueInfoVo cmsBondIssueInfoVo =
     * cmsBondIssueInfoVoMap.get(bondIssueNum); // 添加债券发行id
     * cmsBondRepayPlanDtlVo.setIssueId(cmsBondIssueInfoVo.getId()); tmpList.add(cmsBondRepayPlanDtlVo);
     * planDtlVoMap.put(cmsBondRepayPlanDtlVo.getBondIssueNum(), tmpList); } else { tmpList.add(cmsBondRepayPlanDtlVo);
     * } } }
     * 
     * // 校验 for (String key : planDtlVoMap.keySet()) { List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtlVoList =
     * planDtlVoMap.get(key); for (int i = 0; i < cmsBondRepayPlanDtlVoList.size(); i++) { CmsBondRepayPlanDtlVo delVo =
     * cmsBondRepayPlanDtlVoList.get(i); // 以是否已还/抵扣值判断是否有效 if (EmptyUtils.isEmpty(delVo.getIsRepay())) { continue; }
     * //还款日期校验 if (EmptyUtils.isEmpty(delVo.getRepayDate())) { throw new
     * ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_PAYDATE_EMPTY); } else { String bondIssueNum =
     * delVo.getBondIssueNum(); if
     * (delVo.getRepayDate().compareTo(cmsBondIssueInfoVoMap.get(bondIssueNum).getIssueStartDate()) < 0) { throw new
     * ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_PAYDATE_BEGINDATE); } } //计划还款日校验 if
     * (EmptyUtils.isEmpty(delVo.getPlanRepayDate())) { throw new
     * ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_PAYPLANDATE_EMPTY); } } } }
     */
    /**
     * 导出租金计划模板
     *
     * @param request
     * @param response
     */
    public void exportBudget(HttpServletRequest request, HttpServletResponse response,
            CmsBondRepayPlanVo cmsBondRepayPlanVO) {
        String fileName = "还款计划导出";
        String codedFileName = fileName;
        try {
            if (ServletUtils.isIE(request)) {
                codedFileName = URLEncoder.encode(codedFileName, "UTF8");
            } else {
                codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download;");
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet(fileName);
            // 锁定表格
            // sheet.protectSheet("edit");
            // 设置表格默认列宽度为20个字节
            sheet.setDefaultColumnWidth(20);
            // 组装标题头
            String[] head = new String[] { "序号", "债券发行编号", "还款日期", "计划还款日", "还款本金值", "剩余本金值", "本次还款金额", "本次偿还占发行总额比例%",
                    "是否已还/抵扣", "备注" };
            this.setHeadCell(workbook, sheet, head, 0);
            HSSFCellStyle rightCellStyle = initStyle(workbook, HorizontalAlignment.RIGHT, true);
            HSSFCellStyle leftUnCellStyle = initStyle(workbook, HorizontalAlignment.LEFT, true);
            HSSFCellStyle centerUnCellStyle = initStyle(workbook, HorizontalAlignment.CENTER, true);
            // 组装数据
            List<CmsBondRepayPlanDtlVo> dtlList = new ArrayList<CmsBondRepayPlanDtlVo>();
            dtlList = cmsBondRepayPlanDtlService.getByRegisterId(cmsBondRepayPlanVO.getRegisterId());
            if (!EmptyUtils.isEmptyList(dtlList)) {
                int index = 1;
                int num = 1;
                for (CmsBondRepayPlanDtlVo vo : dtlList) {
                    HSSFRow row = sheet.createRow(index);
                    // 序号
                    HSSFCell cell = row.createCell(0);
                    cell.setCellStyle(centerUnCellStyle);
                    cell.setCellValue(num);
                    // 债券发行编号
                    HSSFCell cell1 = row.createCell(1);
                    cell1.setCellStyle(centerUnCellStyle);
                    cell1.setCellValue(cmsBondRepayPlanVO.getId());
                    // 还款日期
                    HSSFCell cell2 = row.createCell(2);
                    cell2.setCellStyle(centerUnCellStyle);
                    cell2.setCellValue(DateUtils.formatDate(vo.getRepayDate()));
                    // 计划还款日
                    HSSFCell cell3 = row.createCell(2);
                    cell3.setCellStyle(centerUnCellStyle);
                    cell3.setCellValue(DateUtils.formatDate(vo.getPlanRepayDate()));
                    // 还款本金值
                    HSSFCell cell4 = row.createCell(3);
                    cell4.setCellStyle(rightCellStyle);
                    cell4.setCellValue(NumberUtil.formatNumeric(vo.getRepayMoney()));
                    // 剩余本金值
                    HSSFCell cell5 = row.createCell(3);
                    cell5.setCellStyle(rightCellStyle);
                    cell5.setCellValue(NumberUtil.formatNumeric(vo.getRemainMoney()));
                    // 本次还款金额
                    HSSFCell cell6 = row.createCell(5);
                    cell6.setCellStyle(rightCellStyle);
                    BigDecimal repayAmount = EmptyUtils.isEmpty(vo.getRepayAmount()) ? BigDecimal.ZERO
                            : vo.getRepayAmount();
                    cell6.setCellValue(NumberUtil.formatNumeric(repayAmount));
                    // 本次偿还占发行总额比例%
                    HSSFCell cell7 = row.createCell(6);
                    cell7.setCellStyle(rightCellStyle);
                    cell7.setCellValue(NumberUtil.formatNumeric(vo.getRepayRate()));
                    // 是否已还
                    HSSFCell cell8 = row.createCell(9);
                    cell8.setCellStyle(centerUnCellStyle);
                    if (!EmptyUtils.isEmpty(cmsBondRepayPlanVO.getId())) {
                        cell8.setCellValue(vo.getIsRepay().equals("0") ? "否" : "是");
                    } else {
                        cell8.setCellValue("否");
                    }
                    // 备注
                    HSSFCell cell9 = row.createCell(10);
                    cell9.setCellStyle(centerUnCellStyle);
                    cell9.setCellValue(vo.getRmk());
                    index++;
                    num++;
                }
            }
            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 写入标题头
     *
     * @param sheet
     * @param head
     * @throws Exception
     */
    public void setHeadCell(HSSFWorkbook workbook, HSSFSheet sheet, String[] head, int rowTitle) throws Exception {
        HSSFRow row = sheet.createRow(rowTitle);
        for (int i = 0; i < head.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(getCenterCellStyle(workbook, true));
            cell.setCellValue(head[i]);
        }
    }
    /**
     * 获取居中样式
     *
     * @throws Exception
     */
    public HSSFCellStyle getCenterCellStyle(HSSFWorkbook workbook, Boolean locked) {
        return initStyle(workbook, HorizontalAlignment.CENTER, locked);
    }
    /**
     * 获取一个初始的样式
     */
    public HSSFCellStyle initStyle(HSSFWorkbook workbook, HorizontalAlignment align, Boolean locked) {
        HSSFCellStyle style = workbook.createCellStyle();
        // 边框设置
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 字体居中
        style.setAlignment(align);
        style.setLocked(EmptyUtils.isEmpty(locked) || locked ? true : locked);
        return style;
    }
    /**
     * 功能介绍 : 申请审批列表<br>
     *
     * @param dataTablesInput
     *            <b>日期：</b>2017-8-1<br>
     *            <br>
     * @author lvwl
     */
    @Transactional(readOnly = true)
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMSBONDREPAY_PLAN,
                dataTablesInput);
        DataTablesOutput dp = cmsBondRepayPlanDao.getDataInfoApproveTables(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    // 审批功能取消
    // /*****************审批BEGIN********************/
    //
    // /**
    // * 审批启动完成 执行
    // */
    // public void flowStarted(IWorkFlowResult workFlowResult) {
    //
    // }
    //
    // /**
    // * 审批流程结束 执行
    // */
    // public void flowCompleted(IWorkFlowResult workFlowResult) {
    // String businessKey = workFlowResult.getBusinessKey();
    // String processDefinitionKey = workFlowResult.getProcessDefinitionKey();
    // boolean result = workFlowResult.isPass();
    // ICurrentUser user = workFlowResult.getUser();
    //
    // CmsBondRepayPlan plan = this.get(businessKey);
    // // 修改债券发行表信息
    // CmsBondIssueInfo cmsBondIssueInfo = cmsBondIssueInfoService.get(plan.getIssueId());
    //
    // if (result) {//审批通过
    // plan.setIsSave(CmsConstant.IS_FALSE);
    // plan.setStatus(CmsConstant.APPROVED);
    // cmsBondIssueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS_99);
    // msService.insertProcessMsg(plan.getId(), BondConstant.BONDREPAYPLAN_BOND_NO, CmsProcessMsgConstant.MSGA_CHECKOK,
    // CmsProcessMsgConstant.MSGA_CHECKOK + "", user);
    // //notificationService.send(plan.getId(), plan.getCorpId(), NotificationConstant.CMS.PROVIDE_REPAIR,
    // plan);//添加补录的消息提示
    // } else {//打回
    // plan.setStatus(CmsConstant.BACK);
    // cmsBondIssueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS__5);
    // msService.insertProcessMsg(plan.getId(), BondConstant.BONDREPAYPLAN_BOND_NO, CmsProcessMsgConstant.MSGA_ROLLBACK,
    // CmsProcessMsgConstant.MSGA_ROLLBACK + "", user);
    // //notificationService.send(plan.getId(), plan.getCorpId(), NotificationConstant.CMS.PROVIDE_ADD,
    // plan);//添加录入申请的消息提示
    //
    // }
    // cmsBondIssueInfoService.saveOrUpdate(cmsBondIssueInfo);
    // this.saveOrUpdate(plan);
    // }
    // /*****************审批END********************/
}
