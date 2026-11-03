package com.bytter.finance.domain.lease.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.lease.dao.LeaseCapitalPlanDao;
import com.bytter.finance.domain.lease.dao.LeaseCapitalPlanDelDao;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.entity.LeaseCapitalPlan;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanDelVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanVO;
import com.bytter.finance.enums.CmsLeaseEnum;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.workflow.bpm.IWorkFlowListener;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
/**
 * @author mof
 * @version 1.0
 * @Title: Service
 * @Description: 租赁计划主表
 * @date 2022-04-02 11:09:43
 */
@WorkFlow(CmsWorkFlowConstant.CMS_LEASECAPITALPLAN_INFO)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_LEASECAPITALPLAN_INFO)
@Service("leaseCapitalPlanService")
@Transactional
public class LeaseCapitalPlanService extends BaseService<LeaseCapitalPlan, String> implements IWorkFlowListener {
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private LeaseCapitalPlanDao leaseCapitalPlanDao;
    @Autowired
    private LeaseCapitalPlanDelDao leaseCapitalPlanDelDao;
    @Autowired
    private LeaseCapitalPlanDelService leaseCapitalPlanDelService;
    @Autowired
    private CmsFinancingLeaseInfoService financingLeaseInfoService;
    @Override
    public BaseDao<LeaseCapitalPlan, String> getEntityDao() {
        return leaseCapitalPlanDao;
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
     * 保存租金计划
     *
     * @param entity
     */
    public void save(LeaseCapitalPlan entity) {
        super.saveEntity(entity);
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String isSaveSign) {
        // 获取该用户有权限的单位id集
        String resource = "";
        if (CmsConstant.IS_TRUE.equals(isSaveSign)) {
            resource = ResourceConstant.CMS_LEASE_CMSLEASECAPITALPLAN_QUERY;
        }
        if (CmsConstant.IS_FALSE.equals(isSaveSign)) {
            resource = ResourceConstant.CMS_LEASE_UPDATELEASECAPITALPLAN_QUERY;
        }
        List<String> corpAuthList = corpService.getUserCorpIds(resource, dataTablesInput.getCurrentUserId());
        return leaseCapitalPlanDao.getDataTablesUnion(dataTablesInput, isSaveSign, corpAuthList);
    }
    /**
     * 功能介绍 : 租赁数据查询根据Id集合
     *
     * @param dataTablesInput
     * @param ids
     * @return
     */
    public DataTablesOutput getInfoByIds(DataTablesInput dataTablesInput, List<String> ids) {
        return leaseCapitalPlanDao.getInfoByIds(dataTablesInput, ids);
    }
    /**
     * 租金计划数据查询根据Id集合
     *
     * @return 租金计划数据查询根据Id集合
     */
    public List<String> getAllDataByPlan(List<String> corpAuthList) {
        List<String> leaseList = new ArrayList<>();
        List<LeaseCapitalPlanVO> list = leaseCapitalPlanDao.getAllDataByPlan(corpAuthList);
        if (!EmptyUtils.isEmptyList(list)) {
            for (LeaseCapitalPlanVO vo : list) {
                leaseList.add(vo.getLeaseId());
            }
        }
        return leaseList;
    }
    /**
     * 通过申请id查询相关信息
     *
     * @param id
     * @return
     */
    public LeaseCapitalPlanVO getInfoById(String id) {
        List<LeaseCapitalPlanVO> cmsLeaseCapitalPlanVos = leaseCapitalPlanDao.getInfoById(id);
        if (ListUtils.isNullOrEmpty(cmsLeaseCapitalPlanVos)) {
            return null;
        }
        return cmsLeaseCapitalPlanVos.get(0);
    }
    /**
     * 保存租金计划
     *
     * @param vo
     */
    public String saveOrUpdate(String operator, LeaseCapitalPlanVO vo, ICurrentUser user) {
        String id = "";
        if (!StringUtils.isNotEmpty(vo.getLeaseId())) {
            return id;
        }
        // 获取租赁表信息
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = financingLeaseInfoService.get(vo.getLeaseId());
        // 校验租金计划的明细
        validata(vo, cmsFinancingLeaseInfo);
        // 新增或修改
        if (AccebillConstant.BILL_OPERATE_ADD.equals(operator)) {
            LeaseCapitalPlan plan = new LeaseCapitalPlan();
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_51);
            if (StringUtils.isNotEmpty(vo.getId())) {
                // 根据单号查询租金计划主表id
                plan = this.get(vo.getId());
                if (plan != null) {
                    // 更新租金计划主表信息
                    plan.setStatus(CmsConstant.COMMITAPPROVE);
                    plan.setRepayType(vo.getRepayType()); // 租金方式
                    if (!EmptyUtils.isEmpty(vo.getBeginDate())) {
                        plan.setBeginDate(vo.getBeginDate());// 租金开始日期
                    }
                    plan.setUpdateBy(user.getLoginName());
                    plan.setUpdateDate(DateUtils.getDate());
                    this.saveOrUpdate(plan);
                    // 保存租金计划子表
                    id = plan.getId();
                }
            } else {
                // 新增租金计划主表
                plan.setIsSaveSign(CmsConstant.IS_TRUE);
                plan.setStatus(CmsConstant.COMMITAPPROVE);
                plan.setCorpId(vo.getCorpId());
                plan.setLeaseId(vo.getLeaseId()); // 租赁表id
                plan.setLeaseCode(vo.getLeaseCode()); // 融资租赁合同编号
                plan.setRate(cmsFinancingLeaseInfo.getRate());
                plan.setRepayCycle(cmsFinancingLeaseInfo.getRepayCycle());// 利率期限
                plan.setRepayType(vo.getRepayType()); // 租金方式
                plan.setPayMoney(cmsFinancingLeaseInfo.getOriMoney());
                plan.setRepayBeginDate(cmsFinancingLeaseInfo.getBeginPaymoneyDate()); // 还款开始日
                plan.setBeginDate(cmsFinancingLeaseInfo.getBeginDate());// 租金开始日期
                plan.setEndDate(cmsFinancingLeaseInfo.getEndDate());// 租金到期日期
                plan.setCreateBy(user.getLoginName());
                plan.setCreateDate(DateUtils.getDate());
                this.save(plan);
                // 新增租金计划子表
                id = plan.getId();
            }
        } else {
            // 修改数据
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_61);
            // 根据单号查询租金计划主表id
            LeaseCapitalPlanVO leaseCapitalPlanVO = this.getInfoById(vo.getId());
            if (leaseCapitalPlanVO != null) {
                // 更新租金计划主表信息
                LeaseCapitalPlan cms = new LeaseCapitalPlan();
                BeanUtils.copyNotNullProperties(leaseCapitalPlanVO, cms);
                cms.setRepayType(vo.getRepayType()); // 租金方式
                cms.setStatus(CmsConstant.COMMITAPPROVE);
                cms.setIsSaveSign(CmsConstant.IS_FALSE);
                cms.setUpdateBy(user.getLoginName());
                cms.setUpdateDate(DateUtils.getDate());
                if (!EmptyUtils.isEmpty(vo.getBeginDate())) {
                    cms.setBeginDate(vo.getBeginDate());// 租金开始日期
                }
                this.saveOrUpdate(cms);
                // 保存租金计划子表
                id = cms.getId();
            }
        }
        vo.setId(id);
        // 修改租赁表总状态
        financingLeaseInfoService.saveOrUpdate(cmsFinancingLeaseInfo);
        leaseCapitalPlanDelService.subSave(id, vo, user);
        if (AccebillConstant.BILL_OPERATE_ADD.equals(operator)) {
            msService.insertProcessMsg(vo.getId(), LeaseConstant.LEASECAPITALPLAN_LEASE_LEASE_NO,
                    CmsProcessMsgConstant.MSGA_SUBMIT, "新增提交", user);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_LEASECAPITALPLAN_INFO, id, vo, vo.getCorpId(), user.getId());
        }
        if (AccebillConstant.BILL_OPERATE_UPDATE.equals(operator)) {
            msService.insertProcessMsg(vo.getId(), LeaseConstant.LEASECAPITALPLAN_LEASE_LEASE_NO,
                    CmsProcessMsgConstant.MSGA_SUBMIT, "修改提交", user);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_LEASECAPITALPLAN_CHANGE, id, vo, vo.getCorpId(),
                    user.getId());
        }
        return id;
    }
    /**
     * 校验租金计划的明细
     *
     * @param vo
     */
    private void validata(LeaseCapitalPlanVO vo, CmsFinancingLeaseInfo cmsFinancingLeaseInfo) {
        if (EmptyUtils.isEmptyList(vo.getLeaseCapitalPlanDels())) {
            throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASECAPITALPLAN_EMPTY);
        }
        for (int i = 0; i < vo.getLeaseCapitalPlanDels().size(); i++) {
            LeaseCapitalPlanDelVO delVo = vo.getLeaseCapitalPlanDels().get(i);
            // 以是否已还/抵扣值判断是否有效
            if (EmptyUtils.isEmpty(delVo.getIsRepay())) {
                continue;
            }
            // 付租日期校验
            if (EmptyUtils.isEmpty(delVo.getPayDate())) {
                throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_PAYDATE_EMPTY);
            } else {
                if (delVo.getPayDate().compareTo(cmsFinancingLeaseInfo.getBeginDate()) < 0) {
                    throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_PAYDATE_beginDate);
                }
                if (delVo.getPayDate().compareTo(cmsFinancingLeaseInfo.getEndDate()) > 0) {
                    throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_PAYDATE_endDate);
                }
            }
            // 计划付租日校验
            if (EmptyUtils.isEmpty(delVo.getPayPlanDate())) {
                throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_PAYPLANDATE_EMPTY);
            } else {
                if (delVo.getPayPlanDate().compareTo(cmsFinancingLeaseInfo.getBeginDate()) < 0) {
                    throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_PAYPLANDATE_beginDate);
                }
                if (delVo.getPayPlanDate().compareTo(cmsFinancingLeaseInfo.getEndDate()) > 0) {
                    throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_PAYPLANDATE_endDate);
                }
            }
        }
    }
    public LeaseCapitalPlanVO getByLeaseId(String leaseId) {
        if (EmptyUtils.isEmpty(leaseId)) {
            return new LeaseCapitalPlanVO();
        }
        List<LeaseCapitalPlanVO> list = leaseCapitalPlanDao.getByLeaseId(leaseId);
        if (ListUtils.isNullOrEmpty(list)) {
            return new LeaseCapitalPlanVO();
        }
        return list.get(0);
    }
    /**
     * 导出租金计划模板
     *
     * @param request
     * @param response
     */
    public void exportBudget(HttpServletRequest request, HttpServletResponse response,
                             LeaseCapitalPlanVO leaseCapitalPlanVO) {
        String fileName = "租金计划导出";
        String codedFileName = fileName;
        try {
            if (ServletUtils.isIE(request)) {
                codedFileName = URLEncoder.encode(codedFileName, "UTF8");
            } else {
                codedFileName = new String(codedFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
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
            String[] head = new String[]{"序号", "付租日期", "计划付租日", "租赁本金", "租赁利息", "本期租金", "本期费用", "剩余本金", "保证金",
                    "是否已还/抵扣", "备注"};
            this.setHeadCell(workbook, sheet, head, 0);
            HSSFCellStyle rightCellStyle = initStyle(workbook, HorizontalAlignment.RIGHT, true);
            HSSFCellStyle leftUnCellStyle = initStyle(workbook, HorizontalAlignment.LEFT, true);
            HSSFCellStyle centerUnCellStyle = initStyle(workbook, HorizontalAlignment.CENTER, true);
            // 组装数据
            List<LeaseCapitalPlanDelVO> dtlList = new ArrayList<LeaseCapitalPlanDelVO>();
            if (!EmptyUtils.isEmpty(leaseCapitalPlanVO.getId())) {
                dtlList = this.listLeaseCapitalPlan(leaseCapitalPlanVO.getId());
            } else {
                dtlList = this.listLeaseCapitalPlanDel(leaseCapitalPlanVO);
            }
            if (!EmptyUtils.isEmptyList(dtlList)) {
                int index = 1;
                int num = 1;
                for (LeaseCapitalPlanDelVO vo : dtlList) {
                    HSSFRow row = sheet.createRow(index);
                    // 序号
                    HSSFCell cell = row.createCell(0);
                    cell.setCellStyle(centerUnCellStyle);
                    cell.setCellValue(num);
                    // 付租日期
                    HSSFCell cell1 = row.createCell(1);
                    cell1.setCellStyle(centerUnCellStyle);
                    cell1.setCellValue(DateUtils.formatDate(vo.getPayDate()));
                    // 计划付租日
                    HSSFCell cell2 = row.createCell(2);
                    cell2.setCellStyle(centerUnCellStyle);
                    cell2.setCellValue(DateUtils.formatDate(vo.getPayPlanDate()));
                    // 租赁本金
                    HSSFCell cell3 = row.createCell(3);
                    cell3.setCellStyle(rightCellStyle);
                    cell3.setCellValue(NumberUtil.formatNumeric(vo.getLeaseMonry()));
                    // 租赁利息
                    HSSFCell cell4 = row.createCell(4);
                    cell4.setCellStyle(rightCellStyle);
                    BigDecimal leaseInterest = EmptyUtils.isEmpty(vo.getLeaseInterest()) ? BigDecimal.ZERO
                            : vo.getLeaseInterest();
                    cell4.setCellValue(NumberUtil.formatNumeric(leaseInterest));
                    // 本期租金
                    HSSFCell cell5 = row.createCell(5);
                    cell5.setCellStyle(rightCellStyle);
                    BigDecimal thisMoney = EmptyUtils.isEmpty(vo.getThisMoney()) ? BigDecimal.ZERO : vo.getThisMoney();
                    cell5.setCellValue(NumberUtil.formatNumeric(thisMoney));
                    // 本期费用
                    HSSFCell cell6 = row.createCell(6);
                    cell6.setCellStyle(rightCellStyle);
                    BigDecimal thisCost = EmptyUtils.isEmpty(vo.getThisCost()) ? BigDecimal.ZERO : vo.getThisCost();
                    cell6.setCellValue(NumberUtil.formatNumeric(thisCost));
                    // 剩余本金
                    HSSFCell cell7 = row.createCell(7);
                    cell7.setCellStyle(rightCellStyle);
                    BigDecimal surplusMoney = EmptyUtils.isEmpty(vo.getSurplusMoney()) ? BigDecimal.ZERO
                            : vo.getSurplusMoney();
                    cell7.setCellValue(NumberUtil.formatNumeric(surplusMoney));
                    // 保证金
                    HSSFCell cell8 = row.createCell(8);
                    cell8.setCellStyle(rightCellStyle);
                    BigDecimal bailMoney = EmptyUtils.isEmpty(vo.getBailMoney()) ? BigDecimal.ZERO : vo.getBailMoney();
                    cell8.setCellValue(NumberUtil.formatNumeric(bailMoney));
                    // 是否已还
                    HSSFCell cell9 = row.createCell(9);
                    cell9.setCellStyle(centerUnCellStyle);
                    if (!EmptyUtils.isEmpty(leaseCapitalPlanVO.getId())) {
                        cell9.setCellValue(vo.getIsRepay().equals("0") ? "否" : "是");
                    } else {
                        cell9.setCellValue("否");
                    }
                    // 备注
                    HSSFCell cell10 = row.createCell(10);
                    cell10.setCellStyle(centerUnCellStyle);
                    cell10.setCellValue(vo.getRmk());
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
     * 功能介绍：根据id号获取租金计划<br>
     *
     * @param
     * @return
     */
    @Transactional(readOnly = true)
    public List<LeaseCapitalPlanDelVO> listLeaseCapitalPlan(String id) {
        List<LeaseCapitalPlanDelVO> rsList = leaseCapitalPlanDelService.getByLeasePlanId(id);
        return rsList;
    }
    /**
     * 查询租金计划数据
     *
     * @param leaseCapitalPlanVO
     * @return
     */
    @Transactional(readOnly = true)
    public List<LeaseCapitalPlanDelVO> listLeaseCapitalPlanDel(LeaseCapitalPlanVO leaseCapitalPlanVO) {
        // planId非空：则回显租金计划数据
        if (StringUtils.isEmpty(leaseCapitalPlanVO.getId())) {
            return new ArrayList<>();
        }
        List<LeaseCapitalPlanDelVO> rsList = new ArrayList<>();
        List<LeaseCapitalPlanDelVO> list = new ArrayList<LeaseCapitalPlanDelVO>();
        BigDecimal sumRepaymentIst = BigDecimal.ZERO;
        for (LeaseCapitalPlanDelVO vo : rsList) {
            if (StringUtils.isNotEmpty(vo.getLeaseInterest())) {
                sumRepaymentIst = sumRepaymentIst.add(vo.getLeaseInterest());
            }
        }
        /*
         * for (int i = 0, length = rsList.size(); i < length; i++) { LeaseCapitalPlanDelVO vo = rsList.get(i);
         * vo.setSumRepaymentIst(sumRepaymentIst); if (i == rsList.size() - 1) { vo.setLastSign(true); } list.add(vo); }
         */
        return list;
    }
    /**
     * 功能介绍 : 申请审批列表<br>
     *
     * @param dataTablesInput <b>日期：</b>2017-8-1<br>
     *                        <br>
     * @author lvwl
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_LEASECAPITALPLAN_INFO, dataTablesInput);
        DataTablesOutput dp = leaseCapitalPlanDao.getDataInfoApproveTables(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /***************** 审批BEGIN ********************/
    /**
     * 审批启动完成 执行
     */
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 审批流程结束 执行
     */
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        String processDefinitionKey = workFlowResult.getProcessDefinitionKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        LeaseCapitalPlan leaseCapitalPlan = this.get(businessKey,true);
        // 修改租赁表信息
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = financingLeaseInfoService.get(leaseCapitalPlan.getLeaseId());
        if (result) {// 审批通过
            leaseCapitalPlan.setIsSaveSign(CmsConstant.IS_FALSE);
            leaseCapitalPlan.setStatus(CmsConstant.APPROVED);
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_100);
            msService.insertProcessMsg(leaseCapitalPlan.getId(), LeaseConstant.LEASECAPITALPLAN_LEASE_LEASE_NO,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_CHECKOK + "", user);
            // notificationService.send(leaseCapitalPlan.getId(), leaseCapitalPlan.getCorpId(),
            // NotificationConstant.CMS.PROVIDE_REPAIR, leaseCapitalPlan);//添加补录的消息提示
        } else {// 打回
            leaseCapitalPlan.setStatus(CmsConstant.BACK);
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS__7);
            msService.insertProcessMsg(leaseCapitalPlan.getId(), LeaseConstant.LEASECAPITALPLAN_LEASE_LEASE_NO,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_ROLLBACK + "", user);
            // notificationService.send(leaseCapitalPlan.getId(), leaseCapitalPlan.getCorpId(),
            // NotificationConstant.CMS.PROVIDE_ADD, leaseCapitalPlan);//添加录入申请的消息提示
        }
        financingLeaseInfoService.saveOrUpdate(cmsFinancingLeaseInfo);
        this.saveOrUpdate(leaseCapitalPlan);
    }
    /***************** 审批END ********************/
}
