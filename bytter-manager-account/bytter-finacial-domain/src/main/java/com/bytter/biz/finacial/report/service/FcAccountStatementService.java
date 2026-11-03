package com.bytter.biz.finacial.report.service;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.report.dao.FcAccountStatementDao;
import com.bytter.biz.finacial.report.vo.FcAccountDtlVO;
import com.bytter.biz.finacial.report.vo.FcAccountStatementVO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luxy
 * @Title: FcAccountStatementService
 * @ProjectName T3.3
 * @Description: TODO
 * @date 2020/6/30 14:55
 */
@Service("fcAccountStatementService")
public class FcAccountStatementService extends BaseService {
    @Autowired
    private FcAccountStatementDao statementDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IFcVoucherTypeService iFcVoucherTypeService;
    @Override
    public BaseDao getEntityDao() {
        return null;
    }
    /**
     * 获取账户信息
     * @return
     */
    public FcAccountStatementVO getAccountMsg(FcAccountStatementVO statementVO){
        return statementDao.getAccountMsg(statementVO);
    }
    /**
     * 获取所有账户信息
     * @param statementVO
     * @return
     */
    public List<FcAccountStatementVO> getAccountMsgAll(FcAccountStatementVO statementVO,List<String> accounts){
        return statementDao.getAccountMsgAll(statementVO,accounts);
    }
    /**
     * 查询账户明细
     * @param statementVO
     * @param user
     * @return
     */
    public List<FcAccountDtlVO> getAccountDtlList(FcAccountStatementVO statementVO, ICurrentUser user){
        return statementDao.getDtlData(statementVO);
    }
    /**
     * 查询显示的所有账户
     */
    public List<FcAccDTO> getAccAll(FcAccountStatementVO statementVO, ICurrentUser user){
        List<String> corps=corpService.getUserCorpIds(ResourceConstant.FINACIAL_ACCOUNT_STATEMENT_PRINT,user.getId());
        return statementDao.getAccAll(statementVO,corps);
    }
    /**
     * 创建表头样式
     */
    public void creatExcel(HSSFWorkbook wb, HSSFSheet sheet,Integer index) {
        //给单子名称一个长度
        sheet.setDefaultColumnWidth((short) 9);
        for (int i = 0; i < 9; i++) {
            sheet.setColumnWidth(i, 20 * 256);//设置第1-9列列宽
        }
        // 生成一个样式
        HSSFCellStyle style = wb.createCellStyle();
        //创建表头
        HSSFRow row = sheet.createRow(index);
        //设置表头字体
        HSSFFont headfont = wb.createFont();
        headfont.setFontName("宋体");
        style.setFont(headfont);
        //给表头第一行一次创建单元格
        String[] heads = new String[]{
                this.getLangStr("fcAccDetail.tallyDate"),//记账日期
                this.getLangStr("table.fcCompareAcc.interestStartDate"),//起息日期
                this.getLangStr("table.cmsLoanSend.voucherNo"),//凭证号
                this.getLangStr("table.fcAccbook.voucherType"),//凭证种类
                this.getLangStr("table.fbsBudgetProcess.billNo"),//单据号
                this.getLangStr("table.fcAccbookDtl.summaryContent"),//摘要
                this.getLangStr("table.fcAccbookDtlHis.debitMoney"),//借方发生额
                this.getLangStr("table.fcAccbookDtlHis.creditMoney"),//贷方发生额
                this.getLangStr("table.fcAccbookDtl.balance")};//余额
        row.setHeightInPoints(20);
        for (int i = 0; i < heads.length; i++) {
            HSSFCell cell = row.createCell((short) i);
            cell.setCellValue(heads[i]);
            cell.setCellStyle(getCenterCellStyle(wb));
        }
    }
    /**
     * 填充数据
     */
    public HSSFWorkbook addDataToSheet(FcAccountStatementVO searchVO,HSSFSheet sheet, HSSFWorkbook wb,ICurrentUser user) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        HSSFCellStyle centerCellStyle = getCenterCellStyle(wb);//带边框居中
        HSSFCellStyle rightCellStyle = getRightCellStyle(wb);//带边框居右
        HSSFCellStyle centerStyle=wb.createCellStyle();
        HSSFCellStyle rightStyle=wb.createCellStyle();
        HSSFCellStyle leftStyle=wb.createCellStyle();
        rightStyle.setAlignment(HorizontalAlignment.RIGHT);//无边框居右
        centerStyle.setAlignment(HorizontalAlignment.CENTER); // 无边框居中
        HSSFFont headfont = wb.createFont();
        headfont.setFontHeightInPoints((short) 14);// 字体大小
        headfont.setBold(true);
        centerStyle.setFont(headfont);
        leftStyle.setAlignment(HorizontalAlignment.LEFT); // 无边框居左
        List<FcAccDTO> fcAccDTOS=this.getAccAll(searchVO,user);
        List<String> accounts=new ArrayList<>();
        for(FcAccDTO accDTO:fcAccDTOS){
            accounts.add(accDTO.getBlnctrAcc());
        }
        List<FcAccountStatementVO> accountList=this.getAccountMsgAll(searchVO,accounts);
        for(FcAccountStatementVO accountVO:accountList){
            searchVO.setAccount(accountVO.getAccount());
            List<FcAccountDtlVO> list = this.getAccountDtlList(searchVO, user);
            if (!list.isEmpty()){
                List<String> voucherTypes = list.stream().map(FcAccountDtlVO::getVoucherType).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
                Map<String, FcVoucherTypeDTO> fcVoucherTypeByIdsToMap = iFcVoucherTypeService.getFcVoucherTypeByIdsToMap(voucherTypes);
                list.stream().forEach(vo -> {
                    if (fcVoucherTypeByIdsToMap.containsKey(vo.getVoucherType())){
                        FcVoucherTypeDTO fcVoucherTypeDTO = fcVoucherTypeByIdsToMap.get(vo.getVoucherType());
                        if (StringUtils.isNotBlank(fcVoucherTypeDTO.getVoucherName())){
                            vo.setVoucherName(fcVoucherTypeDTO.getVoucherName());
                        }
                    }
                });
            }
            accountVO.setAccountDtlVOList(list);
            String netName=corpService.getCorpName(accountVO.getNetId());
            accountVO.setNetName(netName);
            BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(accountVO.getCurId());
            accountVO.setCurName(btCurrencyDTO.getCurName());
        }
        String account = "";
        int index = 0;// 指向所在行
        for (FcAccountStatementVO accountVO : accountList){
            if(StringUtils.isEmpty(account)){
                account = accountVO.getAccount();
            }
            /*********************创建表头**********************/
            if(index == 0 || !account.equals(accountVO.getAccount())) {
                account = accountVO.getAccount();
                if(index != 0){
                    sheet.createRow(index);//多个表添加空行,分隔
                    index = index+1;
                }
                // 第一行：标题
                sheet.createRow(index);
                HSSFRow headRow = sheet.getRow(index);//标题
                headRow.createCell(0).setCellValue(accountVO.getNetName()+this.getLangStr("table.accountStatement.tableTitle"));//显示查询账户的上级结算中心:**对账单
                headRow.getCell((short) 0).setCellStyle(centerStyle);
                CellRangeAddress region1= new CellRangeAddress(index, index, (short) 0, (short) 8);//合并1-9列
                sheet.addMergedRegion(region1);
                index = index+1;
                //第二行：账户信息
                sheet.createRow(index);
                HSSFRow row2 = sheet.getRow(index);
                row2.createCell(0).setCellValue(getLangStr("table.accountStatement.account")+":"+accountVO.getAccount());//账号
                row2.getCell((short) 0).setCellStyle(leftStyle);
                CellRangeAddress region0 = new CellRangeAddress(index, index, (short) 0, (short) 1);//合并1-2列
                sheet.addMergedRegion(region0);
                row2.createCell(2).setCellValue(getLangStr("table.accountStatement.accName")+":"+accountVO.getAccName());//户名
                row2.getCell((short) 2).setCellStyle(leftStyle);
                CellRangeAddress region2 = new CellRangeAddress(index, index, (short) 2, (short) 3);//合并3-4列
                sheet.addMergedRegion(region2);
                row2.createCell(4).setCellValue(getLangStr("table.accountStatement.curId")+":"+accountVO.getCurName());
                row2.getCell((short) 4).setCellStyle(leftStyle);
                row2.createCell(5).setCellValue(getLangStr("table.accountStatement.beginningBalance")+":"+df.format(accountVO.getYesterBalance()));
                row2.getCell((short) 5).setCellStyle(leftStyle);
                CellRangeAddress region5 = new CellRangeAddress(index, index, (short) 5, (short) 6);//合并6-7列
                sheet.addMergedRegion(region5);
                row2.createCell(7).setCellValue(getLangStr("table.accountStatement.tallyDate")+":"+DateUtils.formatDate(searchVO.getStartDate(),"yyyy/MM/dd")+"-"+ DateUtils.formatDate(searchVO.getEndDate(),"yyyy/MM/dd"));
                row2.getCell((short) 7).setCellStyle(leftStyle);
                CellRangeAddress region7 = new CellRangeAddress(index, index, (short) 7, (short) 8);//合并7-8列
                sheet.addMergedRegion(region7);
                index = index+1;
                //第三行:表头
                creatExcel(wb,sheet,index);
                index = index+1;
            }
            /*********************表头结束**********************/
            /*********************账户明细**********************/
            List<FcAccountDtlVO> accountDtlVOS=accountVO.getAccountDtlVOList();
            BigDecimal debitMoneySum=BigDecimal.ZERO;
            BigDecimal creditMoneySum=BigDecimal.ZERO;
            for(FcAccountDtlVO dtlVO:accountDtlVOS){
                debitMoneySum=debitMoneySum.add(dtlVO.getDebitMoney());
                creditMoneySum=creditMoneySum.add(dtlVO.getCreditMoney());
                HSSFRow row = sheet.createRow(index);
                for (int k = 0; k <= 5; k++) {
                    row.createCell(k);
                    row.getCell((short) k).setCellStyle(centerCellStyle);
                }
                for (int k = 6; k <= 8; k++) {//金额栏靠右
                    row.createCell(k);
                    row.getCell((short) k).setCellStyle(rightCellStyle);
                }
                row.getCell(0).setCellValue(DateUtils.formatDate(dtlVO.getTallyDate(),DateUtils.PATTERN_CLASSICAL_SIMPLE));
                row.getCell(1).setCellValue(DateUtils.formatDate(dtlVO.getInterestStartDate(),DateUtils.PATTERN_CLASSICAL_SIMPLE));
                row.getCell(2).setCellValue(dtlVO.getVoucherNo());
                row.getCell(3).setCellValue(dtlVO.getVoucherName());
                row.getCell(4).setCellValue(dtlVO.getBillNo());
                row.getCell(5).setCellValue(dtlVO.getSummaryContent());
                row.getCell(6).setCellValue(df.format(dtlVO.getDebitMoney()));
                row.getCell(7).setCellValue(df.format(dtlVO.getCreditMoney()));
                row.getCell(8).setCellValue(df.format(dtlVO.getBalance()));
                index++;
            }
            //最后一行汇总
            HSSFRow lastRow = sheet.createRow(index);
            lastRow.createCell(0).setCellValue(getLangStr("common.button.collect"));//汇总
            lastRow.getCell((short) 0).setCellStyle(centerCellStyle);
            lastRow.createCell(6).setCellValue(df.format(debitMoneySum));
            lastRow.createCell(7).setCellValue(df.format(creditMoneySum));
            lastRow.createCell(8);
            for (int k = 1; k <=5 ; k++) {
                lastRow.createCell(k);
                lastRow.getCell((short) k).setCellStyle(centerCellStyle);
            }
            for (int k = 6; k <=8 ; k++) {//金额栏居右
                lastRow.getCell((short) k).setCellStyle(rightCellStyle);
            }
            index++;
        }
        return wb;
    }
    /**
     * 获取国际化
     */
    public String getLangStr(String str){
        return multiLangServiceI.getLang(str);
    }
    /**
     * 获取居中样式
     *
     * @throws Exception
     */
    public HSSFCellStyle getCenterCellStyle(HSSFWorkbook workbook) {
        return initStyle(workbook, HorizontalAlignment.CENTER);
    }
    /**
     * 获取靠右样式
     *
     * @throws Exception
     */
    public HSSFCellStyle getRightCellStyle(HSSFWorkbook workbook) {
        return initStyle(workbook, HorizontalAlignment.RIGHT);
    }
    /**
     * 获取一个初始的样式
     */
    public HSSFCellStyle initStyle(HSSFWorkbook workbook, HorizontalAlignment align) {
        HSSFCellStyle style = workbook.createCellStyle();
        // 边框设置
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 字体居中
        style.setAlignment(align);
        return style;
    }
    /**
     * 批量打印数据
     * @param searchVO
     * @param user
     * @return
     */
    public List<FcAccountStatementVO>  batchPrint(FcAccountStatementVO searchVO,ICurrentUser user){
        List<FcAccDTO> fcAccDTOS=this.getAccAll(searchVO,user);
        List<String> accounts=new ArrayList<>();
        for(FcAccDTO accDTO:fcAccDTOS){
            accounts.add(accDTO.getBlnctrAcc());
        }
        List<FcAccountStatementVO> accountList=this.getAccountMsgAll(searchVO,accounts);
        List<String> curIds = accountList.stream().map(FcAccountStatementVO::getCurId).collect(Collectors.toList());
        Map<String, String> currencyNameMap = currencyService.getCurrencyNameByIds(curIds);
        for(FcAccountStatementVO accountVO:accountList){
            searchVO.setAccount(accountVO.getAccount());
            List<FcAccountDtlVO> list = this.getAccountDtlList(searchVO, user);
            if (!list.isEmpty()){
                List<String> voucherTypes = list.stream().map(FcAccountDtlVO::getVoucherType).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
                Map<String, FcVoucherTypeDTO> fcVoucherTypeByIdsToMap = iFcVoucherTypeService.getFcVoucherTypeByIdsToMap(voucherTypes);
                list.stream().forEach(vo -> {
                    if (fcVoucherTypeByIdsToMap.containsKey(vo.getVoucherType())){
                        FcVoucherTypeDTO fcVoucherTypeDTO = fcVoucherTypeByIdsToMap.get(vo.getVoucherType());
                        if (StringUtils.isNotBlank(fcVoucherTypeDTO.getVoucherName())){
                            vo.setVoucherName(fcVoucherTypeDTO.getVoucherName());
                        }
                    }
                });
            }
//            List<FcAccountDtlVO> accountDtlVOS=statementDao.getDtlData(searchVO);
            BigDecimal debitMoneySum=BigDecimal.ZERO;
            BigDecimal creditMoneySum=BigDecimal.ZERO;
            List<FcAccountDtlVO> tempList = new ArrayList<>();
            for(int i =0;i<list.size();i++){
                FcAccountDtlVO fcAccountDtlVO = list.get(i);
                debitMoneySum=debitMoneySum.add(fcAccountDtlVO.getDebitMoney());
                creditMoneySum=creditMoneySum.add(fcAccountDtlVO.getCreditMoney());
                tempList.add(fcAccountDtlVO);
                if(i%23==0 && i>0){
                    FcAccountDtlVO upDtlVO = new FcAccountDtlVO();
                    upDtlVO.setBalance(fcAccountDtlVO.getBalance());
                    tempList.add(upDtlVO);
                }
            }
            accountVO.setCreditMoneySum(creditMoneySum);
            accountVO.setDebitMoneySum(debitMoneySum);
            accountVO.setAccountDtlVOList(tempList);
            accountVO.setCurName(currencyNameMap.get(accountVO.getCurId()));
        }
        return accountList;
    }
}
