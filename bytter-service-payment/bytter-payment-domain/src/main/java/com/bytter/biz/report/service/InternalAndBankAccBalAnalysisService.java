package com.bytter.biz.report.service;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.report.dao.InternalAndBankAccBalAnalysisDao;
import com.bytter.biz.report.vo.InternalAndBankAccBalVO;
import com.bytter.biz.report.vo.ParamBalVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.IMultiLangService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author zhoup
 * @Description 内部银行账户余额分析情况表
 * @date 2018.6.8
 */
@Transactional
@Service("internalAndBankAccBalAnalysisService")
public class InternalAndBankAccBalAnalysisService extends BaseService<InternalAndBankAccBalVO, String> {
    @Autowired
	private InternalAndBankAccBalAnalysisDao internalAndBankAccBalAnalysisDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Override
    public BaseDao<InternalAndBankAccBalVO, String> getEntityDao() {
        return internalAndBankAccBalAnalysisDao;
    }
    public DataTablesOutput getInternalAndBankAccBal(DataTablesInput input, String resource, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
        DataTablesOutput output = internalAndBankAccBalAnalysisDao.getInternalAndBankAccBal(input, corpAuth);
        List<InternalAndBankAccBalVO> outputList = output.getData();
        List<String> accList = new ArrayList<>();
        List<String> curList = new ArrayList<>();
        for(InternalAndBankAccBalVO balVO : outputList) {
            String bankAcc = balVO.getBankAcc();
            String curId = balVO.getCurId();
            if(!accList.contains(bankAcc)){
                accList.add(bankAcc);
            }
            if(!curList.contains(curId)){
                curList.add(curId);
            }
        }
        List<InternalAndBankAccBalVO> balVOS = internalAndBankAccBalAnalysisDao.queryBankAccBal(accList, curList, input);//查询银行账户余额
        setBankAccBal(output, balVOS);
        return outputPageData(input,output);
    }
    public void setBankAccBal(DataTablesOutput output, List<InternalAndBankAccBalVO> balVOS) {
        List<InternalAndBankAccBalVO> outputList = output.getData();
        Map<String, InternalAndBankAccBalVO> balMap = new HashMap<>();
        for(InternalAndBankAccBalVO balVO : balVOS) {
            String bankAcc = balVO.getBankAcc();
            String curId = balVO.getCurId();
            String key = bankAcc + "_" + curId;
            balMap.put(key, balVO);
        }
        for(InternalAndBankAccBalVO balVO : outputList) {
            String bankAcc = balVO.getBankAcc();
            String curId = balVO.getCurId();
            String key = bankAcc + "_" + curId;
            InternalAndBankAccBalVO accBalVO = balMap.get(key);
            if(null == accBalVO){
                balVO.setBal(BigDecimal.ZERO);
                balVO.setFrzBal(BigDecimal.ZERO);
                balVO.setUseBal(BigDecimal.ZERO);
            } else {
                balVO.setBal(accBalVO.getBal());
                balVO.setFrzBal(accBalVO.getFrzBal());
                balVO.setUseBal(accBalVO.getUseBal());
            }
        }
    }
    /**
     * 将多个银行账号对应一个内部账号进行合并
     *
     * @param output
     * @return
     */
    public DataTablesOutput outputPageData(DataTablesInput input, DataTablesOutput output) {
        DataTablesOutput out = new DataTablesOutput();
        List<InternalAndBankAccBalVO> outputList = output.getData();
        List<ParamBalVO> list = new ArrayList<ParamBalVO>();
        List<ParamBalVO> setList = new ArrayList<ParamBalVO>();
        List<InternalAndBankAccBalVO> listAcc = new ArrayList<InternalAndBankAccBalVO>();
        ParamBalVO paramBal = new ParamBalVO();
        if (null == outputList) {
            return output;
        } else {
            for (InternalAndBankAccBalVO vo : outputList) {
                BeanUtils.copyProperties(vo, paramBal);
                if (StringUtils.isNotEmpty(vo.getAccountAcc()) && "1".equals(vo.getIsOnline())) {
                    setList.add(paramBal);
                } else {//先将内部账号为空的先加入集合
                    listAcc.add(vo);
                    paramBal.setNum(1);
                    if (StringUtils.isNotEmpty(vo.getUseBal())) {
                        paramBal.setSumMoney(vo.getUseBal());
                    } else {
                        paramBal.setSumMoney(vo.getUseMoney());
                    }
                    paramBal.setInternal(listAcc);
                    list.add(paramBal);
                    listAcc = new ArrayList<InternalAndBankAccBalVO>();
                }
                paramBal = new ParamBalVO();
            }
            if (null != setList) {
                Set<ParamBalVO> listAccount = new HashSet<ParamBalVO>(); //去重
                listAccount.addAll(setList);
                for (ParamBalVO bal : listAccount) {
                    BigDecimal sumMoney = BigDecimal.ZERO;
                    BigDecimal sumBal = BigDecimal.ZERO;
                    BigDecimal sumFrzBal = BigDecimal.ZERO;
                    BigDecimal sumUseBal = BigDecimal.ZERO;
                    for (InternalAndBankAccBalVO voBal : outputList) {
                        if (voBal.getCorpId().equals(bal.getCorpId()) && voBal.getCurId().equals(bal.getCurId()) && StringUtils.isNotEmpty(voBal.getAccountAcc()) && voBal.getAccountAcc().equals(bal.getAccountAcc())) {
                            if (StringUtils.isNotEmpty(voBal.getUseBal())) {
                                sumMoney = sumMoney.add(voBal.getUseBal());
                            }
                            if (StringUtils.isNotEmpty(voBal.getBal())) {
                                sumBal = sumBal.add(voBal.getBal());
                            }
                            if (StringUtils.isNotEmpty(voBal.getFrzBal())) {
                                sumFrzBal = sumFrzBal.add(voBal.getFrzBal());
                            }
                            if (StringUtils.isNotEmpty(voBal.getUseBal())) {
                                sumUseBal = sumUseBal.add(voBal.getUseBal());
                            }
                            listAcc.add(voBal);    //合并内部账号相同的银行账号
                        }
                    }
                    if (StringUtils.isNotEmpty(bal.getUseMoney())) {
                        sumMoney = sumMoney.add(bal.getUseMoney());
                    }
                    bal.setInternal(listAcc);
                    bal.setNum(listAcc.size());
                    bal.setSumMoney(sumMoney);
                    bal.setBal(sumBal);
                    bal.setFrzBal(sumFrzBal);
                    bal.setUseBal(sumUseBal);
                    list.add(bal);
                    listAcc = new ArrayList<InternalAndBankAccBalVO>();
                }
            }
            Collections.sort(list); //排序
            out.setDraw(output.getDraw());
            out.setRecordsFiltered(output.getRecordsFiltered());
            out.setRecordsTotal(output.getRecordsTotal());
            out.setData(list);
            String changeCur=(String)input.getFormdata().get("EQs_changeCur");
            String reducedCurId=(String)input.getFormdata().get("EQs_reducedCurId");
            String rateType=(String)input.getFormdata().get("EQs_rateType");
            Date rateDate= DateUtils.getDateTime();
            if(StringUtils.isNotEmpty(input.getFormdata().get("LEd_rateDate"))){
                rateDate=DateUtils.getIntegralEndTime(DateUtils.parse((String)input.getFormdata().get("LEd_rateDate"), "yyyy-MM-dd"));
            }
            //获取汇率
            if (out != null && out.getData() != null) {
                List<ParamBalVO> dataList = out.getData();
                HashMap<String, ParamBalVO> dataMap = new HashMap<>();
                for(ParamBalVO vo:dataList){
                    if (dataMap.containsKey(vo.getCurId())) {
                        ParamBalVO paramBalVO = dataMap.get(vo.getCurId());
                        //内部账户
                        paramBalVO.setBalance(vo.getBalance()==null?paramBalVO.getBalance():paramBalVO.getBalance().add(vo.getBalance()));
                        paramBalVO.setFreezeMoney(vo.getFreezeMoney()==null?paramBalVO.getFreezeMoney():paramBalVO.getFreezeMoney().add(vo.getFreezeMoney()));
                        paramBalVO.setUseMoney(vo.getUseMoney()==null?paramBalVO.getUseMoney():paramBalVO.getUseMoney().add(vo.getUseMoney()));
                        //银行账户
                        paramBalVO.setBal(vo.getBal()==null?paramBalVO.getBal():paramBalVO.getBal().add(vo.getBal()));
                        paramBalVO.setFrzBal(vo.getFrzBal()==null?paramBalVO.getFrzBal():paramBalVO.getFrzBal().add(vo.getFrzBal()));
                        paramBalVO.setUseBal(vo.getUseBal()==null?paramBalVO.getUseBal():paramBalVO.getUseBal().add(vo.getUseBal()));
                        //合计
                        paramBalVO.setSumMoney(vo.getSumMoney()==null?paramBalVO.getSumMoney():paramBalVO.getSumMoney().add(vo.getSumMoney()));
                    } else {
                        ParamBalVO paramBalVO = new ParamBalVO();
                        BeanUtils.copyNotNullProperties(vo, paramBalVO);
                        //内部账户
                        paramBalVO.setBalance(vo.getBalance()==null?BigDecimal.ZERO:vo.getBalance());
                        paramBalVO.setFreezeMoney(vo.getFreezeMoney()==null?BigDecimal.ZERO:vo.getFreezeMoney());
                        paramBalVO.setUseMoney(vo.getUseMoney()==null?BigDecimal.ZERO:vo.getUseMoney());
                        //银行账户
                        paramBalVO.setBal(vo.getBal()==null?BigDecimal.ZERO:vo.getBal());
                        paramBalVO.setFrzBal(vo.getFrzBal()==null?BigDecimal.ZERO:vo.getFrzBal());
                        paramBalVO.setUseBal(vo.getUseBal()==null?BigDecimal.ZERO:vo.getUseBal());
                        //合计
                        paramBalVO.setSumMoney(vo.getSumMoney()==null?BigDecimal.ZERO:vo.getSumMoney());
                        dataMap.put(vo.getCurId(), paramBalVO);
                    }
                }
                //折合币种合计
                ParamBalVO totalVO = new ParamBalVO();
                totalVO.setCorpName("");
                totalVO.setCurName("");
                for (String key : dataMap.keySet()) {
                    ParamBalVO vo = dataMap.get(key);
                    vo.setCorpName("");
                    vo.setNetName("");
                    dataList.add(vo);
                    if(CommonConstant.YES.equals(changeCur)) {
                        BigDecimal curExrate = fcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), reducedCurId, DateUtils.setSpecifiedDateOfTime(rateDate), rateType);
                        if(curExrate!=null) {
                            //内部账户
                            totalVO.setBalance(totalVO.getBalance() == null ? vo.getBalance().multiply(curExrate) : totalVO.getBalance().add((vo.getBalance().multiply(curExrate))));
                            totalVO.setFreezeMoney(totalVO.getFreezeMoney() == null ? vo.getFreezeMoney().multiply(curExrate) : totalVO.getFreezeMoney().add((vo.getFreezeMoney().multiply(curExrate))));
                            totalVO.setUseMoney(totalVO.getUseMoney() == null ? vo.getUseMoney().multiply(curExrate) : totalVO.getUseMoney().add((vo.getUseMoney().multiply(curExrate))));
                            //银行账户
                            totalVO.setBal(totalVO.getBal() == null ? vo.getBal().multiply(curExrate) : totalVO.getBal().add((vo.getBal().multiply(curExrate))));
                            totalVO.setFrzBal(totalVO.getFrzBal() == null ? vo.getFrzBal().multiply(curExrate) : totalVO.getFrzBal().add((vo.getFrzBal().multiply(curExrate))));
                            totalVO.setUseBal(totalVO.getUseBal() == null ? vo.getUseBal().multiply(curExrate) : totalVO.getUseBal().add((vo.getUseBal().multiply(curExrate))));
                            //合计
                            totalVO.setSumMoney(totalVO.getSumMoney()==null ? vo.getSumMoney().multiply(curExrate) : totalVO.getSumMoney().add((vo.getSumMoney().multiply(curExrate))));
                        }
                    }
                }
                if (CommonConstant.YES.equals(changeCur)) {
                    dataList.add(totalVO);
                }
            }
            return out;
        }
    }
    /*
     * ----------------------------------------------导出功能 begin-------------------------------------------------
     */
    /**
     * 生成excel数据
     *
     * @param input 查询数据
     * @return 文档对象
     * @author qinl
     * @since 2019/08/29
     */
    public HSSFWorkbook doExportInterestBillXls(DataTablesInput input, ICurrentUser user) {
        //创建文档对象HSSFWorkbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建sheet
        HSSFSheet sheet = workbook.createSheet("账户余额统计信息");
        //待插入的数据
        String resource = ResourceConstant.NETBANK_REPORT_NISALLOCATIONREQ_QUERY;
//        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_NISALLOCATIONREQ_QUERY, user.getId());
        DataTablesOutput internal = getInternalAndBankAccBal(input, resource, user);
        long recordsTotal = internal.getRecordsTotal();
        input.setLength(Long.valueOf(recordsTotal).intValue());
        DataTablesOutput internalAndBankAccBal = getInternalAndBankAccBal(input, resource, user);
        List<ParamBalVO> listVO = internalAndBankAccBal.getData();
        //for循环方式进行构建"行和列"
        //给sheet构建2行
        for (int i = 0; i < 2 + listVO.size(); i++) {
            sheet.createRow(i);  //sheet的行
        }
        //设置表头
        this.setExcelHeader(workbook, sheet);
        //设置表体
        this.setExcelBody(workbook, sheet, listVO);
        //设置合计
        //this.setExcelBodyMerge(workbook, sheet, listVO);
        return workbook;
    }
    /**
     * 设置表头
     *
     * @param sheet 表对象
     * @author qinl
     * @since 2019/08/29
     */
    private void setExcelHeader(HSSFWorkbook workbook, HSSFSheet sheet) {
        //第一行
        HSSFRow row1 = sheet.getRow(0);
        row1.createCell(0).setCellValue(multiLangServiceI.getLang("table.fcAcc.netId"));
        row1.createCell(1).setCellValue(multiLangServiceI.getLang("table.fcAcc.corpId"));
        row1.createCell(2).setCellValue(multiLangServiceI.getLang("table.ratBkrationRelief.curCode"));
        row1.createCell(3).setCellValue(multiLangServiceI.getLang("table.btBankAccApp.bankAcc"));
        row1.createCell(4);
        row1.createCell(5);
        row1.createCell(6);
        row1.createCell(7);
        row1.createCell(8).setCellValue(multiLangServiceI.getLang("table.interet.sum"));
        //第二行
        HSSFRow row2 = sheet.getRow(1);
        row2.createCell(3).setCellValue(multiLangServiceI.getLang("table.fcPredrawDtlTemp.account"));
        row2.createCell(4).setCellValue(multiLangServiceI.getLang("table.fcCheckInit.bankAccName"));
        row2.createCell(5).setCellValue(multiLangServiceI.getLang("downAllocationAnalysis.BisAccBal"));
        row2.createCell(6).setCellValue(multiLangServiceI.getLang("table.bisAccBal.availBal"));
        row2.createCell(7).setCellValue(multiLangServiceI.getLang("table.fcFreeze.freezeMoney"));
        //上下单元格合并操作
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));
        //左右单元格合并
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 7));
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 12));
        //表头样式
        for (int i = 0; i < 9; i++) {
            sheet.setColumnWidth(i, 25 * 256);
            HSSFCell cell = row1.getCell(i);
            HSSFCell cell1 = row2.getCell(i);
            if (cell != null) {
                cell.setCellStyle(this.setExcelHeaderStyle(workbook));
            }
            if (cell1 != null) {
                cell1.setCellStyle(this.setExcelHeaderStyle(workbook));
            }
        }
    }
    /**
     * 设置表头样式
     *
     * @param workbook 文档对象
     * @return 表头样式
     * @author qinl
     * @since 2019/08/29
     */
    private HSSFCellStyle setExcelHeaderStyle(HSSFWorkbook workbook) {
        //构建列的样式
        HSSFCellStyle cs = workbook.createCellStyle();
        //创建字体样式
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体"); //字体
        font.setBold(true);                     //加粗
//        font.setColor((short) 50);               //设置字体颜色
        font.setFontHeightInPoints((short) 16); //字体大小
        cs.setFont(font);      //列的字体
        cs.setVerticalAlignment(VerticalAlignment.CENTER);  //垂直居中对齐
        cs.setAlignment(HorizontalAlignment.CENTER);        //水平居中对齐
//        cs.setLocked(true);
//        cs.setWrapText(true);
//        cs.setLeftBorderColor(HSSFColor.BLACK.index);// 左边框的颜色
//        cs.setBorderLeft(BorderStyle.THIN);// 边框的大小
//        cs.setRightBorderColor(HSSFColor.BLACK.index);// 右边框的颜色
//        cs.setBorderRight(BorderStyle.THIN);// 边框的大小
//        cs.setBottomBorderColor(HSSFColor.GREEN.index);// 下边框的颜色
//        cs.setBorderBottom(BorderStyle.THIN); // 下边框的大小
//        cs.setTopBorderColor(HSSFColor.BLACK.index);// 上边框的颜色
//        cs.setBorderTop(BorderStyle.THIN);// 上边框的颜色
//        cs.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色
        cs.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index); // 设置单元格的背景颜色（单元格的样式会覆盖列或行的样式）
        return cs;
    }
    /**
     * 设置表体样式
     *
     * @param workbook 文档对象
     * @return 表体样式
     * @author qinl
     * @since 2019/08/29
     */
    private HSSFCellStyle setExcelBodyStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cs = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体"); //字体
        font.setFontHeightInPoints((short) 12);
        cs.setFont(font);      //列的字体
        cs.setVerticalAlignment(VerticalAlignment.CENTER);  //垂直居中对齐
        cs.setAlignment(HorizontalAlignment.CENTER);        //水平居中对齐
        return cs;
    }
    /**
     * 插入表体数据
     *
     * @param sheet  表对象
     * @param listVO 数据源
     * @author qinl
     * @since 2019/08/29
     */
    private void setExcelBody(HSSFWorkbook workbook, HSSFSheet sheet, List<ParamBalVO> listVO) {
        //获取样式
        HSSFCellStyle cs = setExcelBodyStyle(workbook);
        HSSFCellStyle rightCs = setExcelBodyStyle(workbook);
        rightCs.setAlignment(HorizontalAlignment.RIGHT);
        HSSFDataFormat mformat = workbook.createDataFormat();
        rightCs.setDataFormat(mformat.getFormat("#,##0.00"));
        //行指针
        int index = 2;
        //待合并的行
        for (ParamBalVO vo : listVO) {
            if(EmptyUtils.isEmpty(vo.getCorpName())){
                continue;
            }
            List<InternalAndBankAccBalVO> internal = vo.getInternal();
            for(InternalAndBankAccBalVO balVO : internal) {
                HSSFRow row = sheet.getRow(index);
                row.createCell(0).setCellValue(vo.getNetName());
                row.createCell(1).setCellValue(vo.getCorpName());
                row.createCell(2).setCellValue(vo.getCurName());
                row.createCell(3).setCellValue(balVO.getBankAcc());
                row.createCell(4).setCellValue(balVO.getBankName());
                row.createCell(5).setCellValue(balVO.getBal().doubleValue());
                row.createCell(6).setCellValue(balVO.getUseBal().doubleValue());
                row.createCell(7).setCellValue(balVO.getFrzBal().doubleValue());
                row.createCell(8).setCellValue(vo.getSumMoney().doubleValue());
                index++;
                //表体样式
                for (int i = 0; i < 5; i++) {
                    HSSFCell cell = row.getCell(i);
                    if (cell != null) {
                        //设置单元格格式
                        HSSFDataFormat format = workbook.createDataFormat();
                        cs.setDataFormat(format.getFormat("@"));
                        cell.setCellStyle(cs);
                    }
                }
                for (int i = 5; i < 9; i++) {
                    HSSFCell cell = row.getCell(i);
                    if (cell != null) {
                        //设置金额单元格格式
                        cell.setCellStyle(rightCs);
                    }
                }
            }
        }
        String sumLang = multiLangServiceI.getLang("table.datatable.total");
        String convertlang = multiLangServiceI.getLang("table.fexForeign.report.convert.currency.total");
        for (ParamBalVO vo : listVO) {
            if (!EmptyUtils.isEmpty(vo.getCorpName())) {
                continue;
            }
            HSSFRow row = sheet.getRow(index);
            row.createCell(0);
            row.createCell(1);
            if(EmptyUtils.isEmpty(vo.getCurName())){
                row.createCell(2).setCellValue(convertlang + sumLang);
            } else {
                row.createCell(2).setCellValue(vo.getCurName() + sumLang);
            }
            row.createCell(3);
            row.createCell(4);
            row.createCell(5).setCellValue(vo.getBal().doubleValue());
            row.createCell(6).setCellValue(vo.getUseBal().doubleValue());
            row.createCell(7).setCellValue(vo.getFrzBal().doubleValue());
            row.createCell(8).setCellValue(vo.getSumMoney().doubleValue());
            index++;
            //表体样式
            for (int i = 0; i < 5; i++) {
                HSSFCell cell = row.getCell(i);
                if (cell != null) {
                    //设置单元格格式
                    HSSFDataFormat format = workbook.createDataFormat();
                    cs.setDataFormat(format.getFormat("@"));
                    cell.setCellStyle(cs);
                }
            }
            for (int i = 5; i < 9; i++) {
                HSSFCell cell = row.getCell(i);
                if (cell != null) {
                    //设置金额单元格格式
                    cell.setCellStyle(rightCs);
                }
            }
        }
    }
    /**
     * 插入合计
     *
     * @param workbook 文档对象
     * @param sheet    表对象
     * @param listVO   数据源
     * @author qinl
     * @since 2019/08/29
     */
    private void setExcelBodyMerge(HSSFWorkbook workbook, HSSFSheet sheet, List<ParamBalVO> listVO) {
        //获取样式
        HSSFCellStyle cs = setExcelBodyStyle(workbook);
        //行指针
        int index = 2;
        //待合并的行
        String accountAcc = null;
        String netName = null;
        String curName = null;
        String corpName = null;
        int merge = 0;
        //内部账号金额
        BigDecimal sum = new BigDecimal("0");
        BigDecimal sumMerge = new BigDecimal("0");
        for (ParamBalVO vo : listVO) {
            HSSFRow row = sheet.getRow(index);
            if (vo.getUseMoney() != null) {
                sum = sum.add(vo.getUseMoney());
            }
            if (vo.getUseBal() != null) {
                sum = sum.add(vo.getUseBal());
            }
            row.createCell(8).setCellValue(NumberUtil.formatNumeric(sum));
            sum = new BigDecimal("0");
            accountAcc = vo.getAccountAcc();
            curName = vo.getCurName();
            corpName = vo.getCorpName();
            netName = vo.getNetName();
            index++;
            //表体样式
            for (int i = 0; i < 9; i++) {
                HSSFCell cell = row.getCell(i);
                if (cell != null) {
                    //设置单元格格式
                    HSSFDataFormat format = workbook.createDataFormat();
                    cs.setDataFormat(format.getFormat("@"));
                    cell.setCellStyle(cs);
                }
            }
        }
        //最后一行合并
        if (merge > 0) {
            sheet.addMergedRegion(new CellRangeAddress(index - 1 - merge, index - 1, 8, 8));
        }
    }
    /*
     * ----------------------------------------------导出功能 end-------------------------------------------------
     */
}
