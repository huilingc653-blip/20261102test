package com.bytter.credential.domain.international.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.enums.AccebillMessageEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.api.constant.*;
import com.bytter.credential.api.enums.LcMessageEnum;
import com.bytter.credential.domain.international.dao.ArrivalLetterDao;
import com.bytter.credential.domain.international.dao.MortgageLetterDao;
import com.bytter.credential.domain.international.dao.ReceiveLetterDao;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.vo.LcBillArrivalVO;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service("arrivalletterservice")
@Transactional
@WorkFlow(value = {LcWorkFlowConstant.LC_RECEIVE_ARRIVAL})
@DubboService(interfaceClass = IWorkFlowListener.class, group = LcWorkFlowConstant.LC_RECEIVE_ARRIVAL)
public class ArrivalLetterService extends BaseService<LcBillArrival, String> implements IWorkFlowListener, DictInjectProvider {
    @Autowired
    private ArrivalLetterDao arrivalLetterDao;
    @Autowired
    private LcMessageService lcMessage;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private MortgageLetterDao mortgageLetterDao;
    @Autowired
    private ReceiveLetterDao receiveLetterDao;

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @DubboReference(check = false)
    private ICommonUtilService ICommonUtilService;

    @DubboReference(check = false)
    private IDictService dictService;


    /**
     * 更新
     *
     * @param entity
     */
    public void update(LcBillArrival entity, ICurrentUser user) {
        entity.setUpdateDate(new Date());
        entity.setUpdateBy(user.getLoginName());
        super.updateEntity(entity);
    }


    /**
     * 功能介绍 : 入库或登记<br>
     * 0登记，11入库
     */
    public void tempOrSubmit(LcBillArrival entity, ICurrentUser user) {
        if (entity.getStatus() == 11) {//提交审批-入库
            entity.setStatus(InternationalConstant.COMMITAPPROVE);
        } else if (entity.getStatus() == 0) {//登记
            entity.setStatus(InternationalConstant.TEMP);
        }
        entity.setArrivalType(1);
        entity.setArrivalCode(getArrivalCode());
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(new Date());
        super.saveEntity(entity);
//            消息提醒处理
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_RECEIVE_ARRIVAL, entity.getLcId(), user.getId());
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_ARRIVAL_UPDATE, entity.getId(), user.getId());
        //审批
        if (entity.getStatus() == 11) {// 提交审批
            lcMessage.insertProcessMsg(entity.getId(), entity.getArrivalCode(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_ARRIVAL, LcMsgConstant.MSGA_SUBMIT, "交单入库提交", user);
            //默认审批通过
            this.getEntityDao().flush();
            getStartHandler().handler(LcWorkFlowConstant.LC_RECEIVE_ARRIVAL, entity.getId(), entity, entity.getCorpId(), user.getId());
        } else {
            lcMessage.insertProcessMsg(entity.getId(), entity.getArrivalCode(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_ARRIVAL, LcMsgConstant.MSGA_INPUT, "交单登记提交", user);
        }

    }

    /**
     * 交单列表
     **/
    @Transactional(readOnly = true)
    public DataTablesOutput queryAll(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_ARRIVAL_QUERY, user.getId());
        return arrivalLetterDao.queryAll(input, corpAuth);

    }

    /**
     * 交单修改列表
     **/
    @Transactional(readOnly = true)
    public DataTablesOutput queryUpdate(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_ARRIVAL_QUERY, user.getId());
        DataTablesOutput dataTablesOutput = arrivalLetterDao.queryUpdate(input, corpAuth, null);
        List<LcBillArrivalVO> data = dataTablesOutput.getData();
        if (!(null == data || data.isEmpty())) {
            data.stream().forEach(item -> {
                CorpDTO corpDTO = corpService.get(item.getCorpId());
                if (ObjectUtils.isNotEmpty(corpDTO)) {
                    item.setCorpName(corpDTO.getName());
                }

                BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(item.getCurId());
                if (ObjectUtils.isNotEmpty(btCurrencyDTO)) {
                    item.setCurName(btCurrencyDTO.getCurName());
                }

            });
        }
        dataTablesOutput.setData(data);
        return dataTablesOutput;
    }

    /**
     * 报表查询
     **/
    @Transactional(readOnly = true)
    public DataTablesOutput reportDatatable(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_REPORT_ARRIVAL_QUERY, user.getId());
        DataTablesOutput output = arrivalLetterDao.queryUpdate(input, corpAuth, "R");
        List<LcBillArrivalVO> voList = output.getData();
        if (CollectionUtils.isEmpty(voList)) {
            return output;
        }
        Map<String, String> mapCorp = ICommonUtilService.mapListCorp();
        Map<String, String> mapCur = ICommonUtilService.mapListCurrency();
        for (LcBillArrivalVO vo : voList) {
            if (mapCorp != null && mapCorp.size() > 0) {
                vo.setCorpName(mapCorp.get(vo.getCorpId()));
            }
            if (mapCur != null && mapCur.size() > 0) {
                vo.setCurName(mapCur.get(vo.getCurId()));
            }
        }
        output.setData(voList);
        return output;
    }

    /**
     * 删除收到信用证登记信息
     **/
    public void delLcEnrol(String id, ICurrentUser user) {
        LcBillArrival lcBillArrival = this.get(id);
        lcBillArrival.setStatus(InternationalConstant.DELETED);
        // lcEnrol.setBizStatus(InternationalConstant.BUSINESSSTATE_2_);
        /*消息处理*/
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_RECEIVE_ARRIVAL, lcBillArrival.getLcId(), user.getId());
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_ARRIVAL_UPDATE, lcBillArrival.getId(), user.getId());
        this.update(lcBillArrival, user);
    }

    /**
     * 通过信用证单号查询实体
     **/
    public LcEnrol getInfoByLcNo(String lcNo) {
        return arrivalLetterDao.getInfoByLcNo(lcNo);
    }


    /**
     * <b>功能介绍：</b>生成交单单号<br>
     */
    public String getArrivalCode() {
        return SequenceSingleton.getSequence(SequenceEnum.LC_BILL_ARRIVAL_BILL_NO, LcEnrolConstant.LC_RECEIVE_BILL_CODE_ARRIVAL);
    }

    /**
     * 修改操作
     *
     * @version 1.0
     * @author zhengxn
     */
    public void saveOrUpdate(LcBillArrival entity, ICurrentUser user) {
        LcBillArrival lcBillArrival = this.get(entity.getId());
        if (entity.getStatus() == 11) {//提交审批-入库
            entity.setStatus(InternationalConstant.COMMITAPPROVE);
            //entity.setBizStatus(CmsConstant.BUSINESSSTATE_61);//收到信用证入库提交待审批
        } else if (entity.getStatus() == 0) {//登记
            entity.setStatus(InternationalConstant.TEMP);
            //entity.setBizStatus(CmsConstant.BUSINESSSTATE_60);//登记
        }
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(new Date());
        BeanUtils.copyNotNullProperties(entity, lcBillArrival);
        super.saveOrUpdate(lcBillArrival);
        /*消息处理*/
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_RECEIVE_ARRIVAL, entity.getLcId(), user.getId());
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_ARRIVAL_UPDATE, entity.getId(), user.getId());
        //审批
        if (lcBillArrival.getStatus() == 11) {// 提交审批
            lcMessage.insertProcessMsg(lcBillArrival.getId(), lcBillArrival.getArrivalCode(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_ARRIVAL, LcMsgConstant.MSGA_SUBMIT, "交单入库提交", user);
            //默认审批通过
            this.getEntityDao().flush();
            getStartHandler().handler(LcWorkFlowConstant.LC_RECEIVE_ARRIVAL, lcBillArrival.getId(), lcBillArrival, lcBillArrival.getCorpId(), user.getId());
        } else {
            lcMessage.insertProcessMsg(lcBillArrival.getId(), lcBillArrival.getArrivalCode(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_ARRIVAL, LcMsgConstant.MSGA_INPUT, "交单登记提交", user);
        }
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        LcBillArrival lcBillArrival = this.get(workFlowResult.getBusinessKey(), true);
        Integer status = null;
        String bizStatus = null;
        if (workFlowResult.getProcessDefinitionKey().equals(LcWorkFlowConstant.LC_RECEIVE_ARRIVAL)) {//提交审批
            status = workFlowResult.isPass() ? InternationalConstant.APPROVED : InternationalConstant.BACK;
            //bizStatus = workFlowResult.isPass()?CmsConstant.BUSINESSSTATE_72:CmsConstant.BUSINESSSTATE_67;//收到信用证待交单or审批打回
            if (workFlowResult.isPass()) {
                lcMessage.insertProcessMsg(lcBillArrival.getId(), lcBillArrival.getArrivalCode(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_ARRIVAL, LcMsgConstant.MSGA_CHECKOK, "交单审批通过", workFlowResult.getUser());
                notificationService.send(lcBillArrival.getId(), lcBillArrival.getCorpId(), NotificationConstant.INTERNATIONAL.INTERNATIONAL_ARRIVAL_ARRIVAL, lcBillArrival);
            } else {
                lcMessage.insertProcessMsg(lcBillArrival.getId(), lcBillArrival.getArrivalCode(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_ARRIVAL, LcMsgConstant.MSGA_ROLLBACK, "交单审批打回", workFlowResult.getUser());
                notificationService.send(lcBillArrival.getId(), lcBillArrival.getCorpId(), NotificationConstant.INTERNATIONAL.INTERNATIONAL_ARRIVAL_UPDATE, lcBillArrival);
            }
        }
        lcBillArrival.setStatus(status);
        lcBillArrival.setBankFlowSign("0");//设置未挂接流水
        //lcBillArrival.setBizStatus(bizStatus);
        arrivalLetterDao.saveOrUpdate(lcBillArrival);
    }

    public String getNextArrivalNo(String lcCode) {
        return arrivalLetterDao.getNextArrivalNo(lcCode);
    }

    /**
     * 清零
     **/
    public void reset(String id) {
        LcEnrol lcEnrol = receiveLetterDao.get(id);
        boolean isOk = mortgageLetterDao.getInfoByLcId(lcEnrol.getId());
        if (!isOk) {
            throw new ParameterException(LcMessageEnum.LC_RECEIVE_ARRIVAL_NO_RESET);
        }
        lcEnrol.setStatus(InternationalConstant.RESET);
        receiveLetterDao.saveOrUpdate(lcEnrol);
    }

    /**
     * 功能介绍 : 申请审批列表<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(LcWorkFlowConstant.LC_RECEIVE_ARRIVAL, dataTablesInput);
        DataTablesOutput dp = null;
        try {
            dp = arrivalLetterDao.getDataInfoApproveTables(dataTablesInput, workFlowBusiness.getBusinessIdList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workFlowBusiness.merge(dp);
    }

    /**
     * 查询合计交单金额
     *
     * @return
     * @Param id
     */
    public BigDecimal getTotalMoney(String id) {
        return BigDecimal.valueOf(arrivalLetterDao.getTotalMoney(id));
    }

    @Override
    public BaseDao<LcBillArrival, String> getEntityDao() {
        return arrivalLetterDao;
    }

    /**
     * 导出月度贷款明细表
     *
     * @param resultList 导出结果
     * @return 导出对象
     */
    public HSSFWorkbook exportArrival(List<LcBillArrivalVO> resultList) {
        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet("交单查询");
        // 设置列宽
        this.setColumnWidth(sheet);
        // 创建表格样式
        this.createExcel(workbook, sheet);
        // 填充数据
        this.createData(workbook, sheet, resultList);
        return workbook;
    }

    /**
     * 设置列宽
     *
     * @param sheet sheet页
     */
    private void setColumnWidth(HSSFSheet sheet) {
        // 设置单元格默认的列宽
        sheet.setDefaultColumnWidth((short) 20);
        // 自定义列宽
        sheet.setColumnWidth(0, 50 * 256);//设置第一列列宽
        sheet.setColumnWidth(1, 20 * 256);//设置第二列列宽
        sheet.setColumnWidth(2, 20 * 256);//设置第三列列宽
        sheet.setColumnWidth(3, 50 * 256);//设置第四列列宽
        sheet.setColumnWidth(4, 20 * 256);//设置第五列列宽
        sheet.setColumnWidth(5, 20 * 256);//设置第六列列宽
        sheet.setColumnWidth(6, 20 * 256);//设置第七列列宽
        sheet.setColumnWidth(7, 25 * 256);//设置第八列列宽
        sheet.setColumnWidth(8, 20 * 256);//设置第九列列宽
    }

    /**
     * 创建表格样式
     *
     * @param wb    workbook对象
     * @param sheet sheet页
     */
    public void createExcel(HSSFWorkbook wb, HSSFSheet sheet) {
        // 获取居中样式
        HSSFCellStyle headStyle = ExportUtils.getCenterCellStyle(wb, true);
        // 设置一级表头字体
        headStyle.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 14));
        // 一级表头
        HSSFCellStyle secondColorStyle = ExportUtils.getCenterCellStyle(wb, true);
        // 设置二级表头字体
        secondColorStyle.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12, IndexedColors.RED.getIndex()));
        // 二级表头
        HSSFCellStyle secondStyle = ExportUtils.getCenterCellStyle(wb, true);
        // 设置二级表头字体
        secondStyle.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));
        // 创建第一行
        HSSFRow row0 = sheet.createRow(0);
        // 创建行高
        row0.setHeight((short) 600);
        // 创建第一行第一列
        HSSFCell cell00 = row0.createCell(0);
        cell00.setCellValue(new HSSFRichTextString("交单查询"));
        this.setCellStyle(row0, headStyle);
        // 第一行合并
        CellRangeAddress range0 = new CellRangeAddress(0, 0, 0, 8);
        sheet.addMergedRegion(range0);
        // 创建第二行
        HSSFRow row1 = sheet.createRow(1);
        String[] oneHeads = new String[]{"单位名称", "(L/C NO)", "信用证类型", "(Presenting Bank)", "(Ben)", "交单金额", "(Currency)", "(Presenting Date)", "状态"};
        row1.setHeightInPoints(20);
        for (int i = 0; i < oneHeads.length; i++) {
            HSSFCell cell = row1.createCell((short) i);
            cell.setCellValue(oneHeads[i]);
            cell.setCellStyle(secondColorStyle);
        }
        // 创建第二行
        HSSFRow row2 = sheet.createRow(2);
        String[] twoHeads = new String[]{"", "信用证号", "", "交单银行", "受益人", "", "币别", "交单日期", ""};
        row2.setHeightInPoints(20);
        for (int i = 0; i < twoHeads.length; i++) {
            HSSFCell cell = row2.createCell((short) i);
            cell.setCellValue(twoHeads[i]);
            cell.setCellStyle(secondStyle);
        }
        CellRangeAddress range2311 = new CellRangeAddress(1, 2, 0, 0);
        sheet.addMergedRegion(range2311);
        CellRangeAddress range2333 = new CellRangeAddress(1, 2, 2, 2);
        sheet.addMergedRegion(range2333);
        CellRangeAddress range2366 = new CellRangeAddress(1, 2, 5, 5);
        sheet.addMergedRegion(range2366);
        CellRangeAddress range2399 = new CellRangeAddress(1, 2, 8, 8);
        sheet.addMergedRegion(range2399);
    }

    /**
     * 填充数据
     *
     * @param wb         结果集
     * @param sheet      sheet页
     * @param resultList workbook对象
     */
    private void createData(HSSFWorkbook wb, HSSFSheet sheet, List<LcBillArrivalVO> resultList) {
        // 文字居中
        HSSFCellStyle centerCellStyleTotal = ExportUtils.getCenterCellStyle(wb, false);
        // 设置字体
        centerCellStyleTotal.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));
        HSSFCellStyle centerCellStyle = ExportUtils.getCenterCellStyle(wb, false);
        // 金额靠右（合计行）
        HSSFCellStyle rightCellStyleTotal = ExportUtils.getRightCellStyle(wb, false);
        DataFormat currencyDataFormat = wb.createDataFormat();
        // 设置字体
        rightCellStyleTotal.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));
        rightCellStyleTotal.setDataFormat(currencyDataFormat.getFormat("#,##0.00"));
        // 金额靠右
        HSSFCellStyle rightCellStyle = ExportUtils.getRightCellStyle(wb, false);
        rightCellStyle.setDataFormat(currencyDataFormat.getFormat("#,##0.00"));

        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("letter_of_credit_inter", "international.lcMortgage.status", "international.lcBillArrival.status", "international.status");

        // 数据开始行
        AtomicInteger rowIndex = new AtomicInteger(3);
        resultList.forEach(item -> {
            // 创建数据行
            HSSFRow row = sheet.createRow(rowIndex.get());
            // 创建第1列：单位名称
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellStyle(centerCellStyle);
            cell1.setCellValue(new HSSFRichTextString(item.getCorpName()));
            // 创建第2列：信用证号
            HSSFCell cell2 = row.createCell(1);
            cell2.setCellStyle(centerCellStyle);
            cell2.setCellValue(new HSSFRichTextString(item.getCreditNo()));
            // 创建第3列：信用证类型
            HSSFCell cell3 = row.createCell(2);
            cell3.setCellStyle(centerCellStyle);
            String lcInterName = "";
            Map<String, DictDTO> interDictMap = listDictMap.get("letter_of_credit_inter");
            if (null != interDictMap && interDictMap.containsKey(item.getLcInter())) {
                DictDTO dict = interDictMap.get(item.getLcInter());
                lcInterName = dict.getName();
            }
            cell3.setCellValue(new HSSFRichTextString(lcInterName));
            // 创建第4列：交单银行
            HSSFCell cell4 = row.createCell(3);
            cell4.setCellStyle(centerCellStyle);
            cell4.setCellValue(new HSSFRichTextString(item.getHandDocBank()));
            // 创建第5列：受益人
            HSSFCell cell5 = row.createCell(4);
            cell5.setCellStyle(centerCellStyle);
            cell5.setCellValue(new HSSFRichTextString(item.getBeneficiary()));
            // 创建第6列交单金额
            HSSFCell cell6 = row.createCell(5);
            cell6.setCellStyle(rightCellStyle);
            cell6.setCellValue(item.getBillMoney().doubleValue());
            // 创建第7列币别
            HSSFCell cell7 = row.createCell(6);
            cell7.setCellStyle(centerCellStyle);
            cell7.setCellValue(new HSSFRichTextString(item.getCurName()));
            // 创建第8列交单日期
            HSSFCell cell8 = row.createCell(7);
            cell8.setCellStyle(centerCellStyle);
            cell8.setCellValue(DateUtils.formatDate(item.getPaymentDate()));
            // 创建第9列状态
            HSSFCell cell9 = row.createCell(8);
            cell9.setCellStyle(centerCellStyle);
            String statusName = "";
            if (CommonConstant.STATUE_WAIT_CHECK.equals(item.getStatus())) {
                if (null != item.getStatus2()) {
                    Map<String, DictDTO> statusDictMap = listDictMap.get("international.lcMortgage.status");
                    if (null != statusDictMap && statusDictMap.containsKey(item.getStatus2())) {
                        DictDTO dict = statusDictMap.get(item.getStatus2());
                        statusName = dict.getName();
                    }
                } else {
                    Map<String, DictDTO> statusDictMap = listDictMap.get("international.status");
                    if (null != statusDictMap && statusDictMap.containsKey(item.getStatus())) {
                        DictDTO dict = statusDictMap.get(item.getStatus());
                        statusName = dict.getName();
                    }
                }
            } else {
                if ("99".equals(item.getStatus())) {
                    Map<String, DictDTO> statusDictMap = listDictMap.get("international.lcBillArrival.status");
                    if (null != statusDictMap && statusDictMap.containsKey(item.getStatus())) {
                        DictDTO dict = statusDictMap.get(item.getStatus());
                        statusName = dict.getName();
                    }
                } else {
                    Map<String, DictDTO> statusDictMap = listDictMap.get("international.status");
                    if (null != statusDictMap && statusDictMap.containsKey(item.getStatus())) {
                        DictDTO dict = statusDictMap.get(item.getStatus());
                        statusName = dict.getName();
                    }
                }
            }
            cell9.setCellValue(new HSSFRichTextString(statusName));
            rowIndex.getAndIncrement();
        });
        // 汇总列
        // 创建数据行
        HSSFRow row = sheet.createRow(rowIndex.get());
        HSSFCell cell1 = row.createCell(0);
        cell1.setCellStyle(centerCellStyleTotal);
        cell1.setCellValue(new HSSFRichTextString("汇总"));
        for (int a = 1; a < 5; a++) {
            HSSFCell cell = row.createCell(a);
            cell.setCellStyle(centerCellStyleTotal);
            cell.setCellValue(new HSSFRichTextString(""));
        }
        // 交单金额汇总
        // 循环resultList，汇总集合中LcBillArrivalVO对象的交单金额，过滤交单金额为空的数据，返回BigDecimal
        BigDecimal totalMoney = Optional.ofNullable(resultList).orElse(Collections.emptyList()).stream().map(LcBillArrivalVO::getBillMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        HSSFCell cell6 = row.createCell(5);
        cell6.setCellStyle(rightCellStyleTotal);
        cell6.setCellValue(totalMoney.doubleValue());
        for (int a = 6; a < 9; a++) {
            HSSFCell cell = row.createCell(a);
            cell.setCellStyle(centerCellStyleTotal);
            cell.setCellValue(new HSSFRichTextString(""));
        }
    }

    /**
     * 设置表头单元格样式
     *
     * @param row       某行
     * @param cellStyle 单元格样式
     */
    private void setCellStyle(HSSFRow row, HSSFCellStyle cellStyle) {
        Iterator<Cell> iterator = row.cellIterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            cell.setCellStyle(cellStyle);
        }
    }

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
