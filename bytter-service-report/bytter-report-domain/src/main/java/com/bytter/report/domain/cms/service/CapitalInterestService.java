package com.bytter.report.domain.cms.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.report.api.constant.ResourceConstant;
import com.bytter.report.domain.cms.dao.CapitalInterestDao;
import com.bytter.report.domain.cms.vo.CapitalInterestVO;
import com.bytter.report.domain.cms.vo.HomeCapitalInterestVO;
import com.bytter.report.domain.cms.vo.InitCapitalVo;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.BtCurrencyConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("capitalInterestService")
public class CapitalInterestService extends BaseService {
    @Autowired
    private CapitalInterestDao capitalInterestDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService rateAdjustService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao getEntityDao() {
        return capitalInterestDao;
    }

    @Transactional(readOnly = true)
    public DataTablesOutput listOutputData(DataTablesInput input, String userId, CapitalInterestVO capitalInterestVO){
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.BI_CMS_CAPITALINTEREST_QUERY, userId);
        DataTablesOutput output = capitalInterestDao.listOutputData(input, corpIds, capitalInterestVO);
        List<CapitalInterestVO> data = output.getData();
        if (ObjectUtil.isNotEmpty(data)){
            // 补全单位信息
            Map<String, String> corps = corpService.getNameByIds(corpIds);
            // 币别
            Map<String, String> currencyMap = currencyService.getCurrencyNameMap() ;
            // 融资类型
            Map<String, DictDTO> finTypeMap = dictService.listMap("Loan.finType");

            for (CapitalInterestVO vo : data){
                if (ObjectUtil.isNotEmpty(vo.getCorpId()) && ObjectUtil.isNotEmpty(corps)){
                    vo.setCorpName(corps.get(vo.getCorpId()));
                }
                if (ObjectUtil.isNotEmpty(vo.getCurId()) && ObjectUtil.isNotEmpty(currencyMap)){
                    vo.setCurName(currencyMap.get(vo.getCurId()));
                }
                if (ObjectUtil.isNotEmpty(vo.getFinType()) && ObjectUtil.isNotEmpty(finTypeMap) && ObjectUtil.isNotEmpty(finTypeMap.get(vo.getFinType()))){
                    vo.setFinTypeName(finTypeMap.get(vo.getFinType()).getName());
                }
            }
        }
        return output;
    }

    public void interestMake(Date beginDate,Date endDate,List<String> months,DataTablesOutput output){
       int i=0;
       Map<String, CapitalInterestVO> map=new HashMap<>();
       List<String> times=new ArrayList<>();
       while (DateUtils.compareDate(DateUtils.addMonth(beginDate,i),endDate)<=0){
           Date newDate=DateUtils.addMonth(beginDate,i);
           int year=DateUtils.getYear(newDate);
           int month=DateUtils.getMonth(newDate);
           months.add(year+"年"+month+"月");
          if(month<10) {
              times.add(year + "-0" + month);
          }else {
              times.add(year + "-" + month);
          }
           i++;
       }
    List<CapitalInterestVO> infoList=output.getData();
       for(CapitalInterestVO vo:infoList){
               List<CapitalInterestVO> repay = capitalInterestDao.listRepay(vo, beginDate, endDate);
               for(CapitalInterestVO reVo:repay){
                   map.put(reVo.getReTime(),reVo);
               }
               List<BigDecimal> unMoney = new ArrayList<>();
              for(String time:times){
                  if(StringUtils.isNotEmpty(map.get(time))){
                      CapitalInterestVO rv= map.get(time);
                      unMoney.add(rv.getReMoney());
                      unMoney.add(rv.getReInterest());
                  }else {
                      unMoney.add(BigDecimal.ZERO);
                      unMoney.add(BigDecimal.ZERO);
                  }
              }
               vo.setUnMoney(unMoney);
              map.clear();
       }
    }

    /**
     * */
    public Map<String, Object> initCapitalInterest(HomeCapitalInterestVO capitalInterestVO, ICurrentUser user){
        Map<String, Object> map=new HashMap<>();
        List<String> corpAuth = null;
        if(capitalInterestVO.getFinType().equals("01")|| EmptyUtils.isEmpty(capitalInterestVO.getFinType())) {//内部贷款
            // 当前用户查询单位权限
            corpAuth = corpService.getUserCorpIds(com.bytter.finance.constant.ResourceConstant.CMS_LOAN_INFO_REPORTQUERY, user.getId());
        }else if(capitalInterestVO.getFinType().equals("02")) {//银行借款
            // 当前用户查询单位权限
            corpAuth = corpService.getUserCorpIds(com.bytter.finance.constant.ResourceConstant.CMS_PROVIDE_INFO_QUERY, user.getId());
        }else if(capitalInterestVO.getFinType().equals("03")) {//委托贷款
            // 当前用户查询单位权限
            corpAuth = corpService.getUserCorpIds(com.bytter.finance.constant.ResourceConstant.CMS_PRECATIVE_INFO_QUERY, user.getId());
        }else if(capitalInterestVO.getFinType().equals("04")) {//内部拆解
            // 当前用户查询单位权限
            corpAuth = corpService.getUserCorpIds(com.bytter.finance.constant.ResourceConstant.CMS_INL_INFO_QUERY, user.getId());
        }else if(capitalInterestVO.getFinType().equals("05")) {//直融
            // 当前用户查询单位权限
            corpAuth = corpService.getUserCorpIds(com.bytter.finance.constant.ResourceConstant.CMS_DFDIRECTFINANCEINFO_APPLY, user.getId());
        }else if(capitalInterestVO.getFinType().equals("06")) {//融资租赁
            // 当前用户查询单位权限
            corpAuth = corpService.getUserCorpIds(com.bytter.finance.constant.ResourceConstant.CMS_LEASE_FINANCING_QUERY, user.getId());
        }else if(capitalInterestVO.getFinType().equals("07")) {//债券
            // 当前用户查询单位权限
            corpAuth = corpService.getUserCorpIds(com.bytter.finance.constant.ResourceConstant.CMS_BOND_REGISTER_QUERY, user.getId());
        }else if(capitalInterestVO.getFinType().equals("08")) {//资产证券化
            // 当前用户查询单位权限
            corpAuth = corpService.getUserCorpIds(com.bytter.finance.constant.ResourceConstant.CMS_SECURITY_TYPE_QUERY, user.getId());
        }
        Date startDate = null;
        Date endDate = null;
        if (StringUtils.isEmpty(capitalInterestVO.getStartDate())){
            startDate = DateUtils.getFirstMonthDate(DateUtils.getDate());
        }else{
            startDate = DateUtils.parse(capitalInterestVO.getStartDate()+"-01","yyyy-MM-dd");
        }
        if (StringUtils.isEmpty(capitalInterestVO.getEndDate())){
            endDate = DateUtils.addMonth(DateUtils.getFirstMonthDate(DateUtils.getDate()),12);
        }else{
            endDate = DateUtils.getLastMonthDate(DateUtils.parse(capitalInterestVO.getEndDate()+"-01","yyyy-MM-dd"));
        }
        //获取开始时间到结束时间中间隔了多少个月份
        int monthDiff = DateUtils.getMonthDiff(startDate, endDate);
        ArrayList<String> dates = new ArrayList<>();
        //将X轴固定
        //如果没有选择日期默认为12个月，从本月开始
        if (StringUtils.isEmpty(capitalInterestVO.getEndDate())){
            for (int i = 0; i < monthDiff; i++) {
                if (i==0){
                    dates.add(DateUtils.format(startDate,"yyyy-MM"));
                }else{
                    dates.add(DateUtils.format(DateUtils.addMonth(DateUtils.getFirstMonthDate(startDate),i),"yyyy-MM"));
                }
            }
        }else{
            for (int i = 0; i <= monthDiff; i++) {
                if (i==0){
                    dates.add(DateUtils.format(startDate,"yyyy-MM"));
                }else{
                    dates.add(DateUtils.format(DateUtils.addMonth(DateUtils.getFirstMonthDate(startDate),i),"yyyy-MM"));
                }
            }
        }
        //如果是折合人民币，置空
        if (BtCurrencyConstant.ZH_RMB.equals(capitalInterestVO.getCurId())) {
            capitalInterestVO.setCurId(null);
        }
        List<HomeCapitalInterestVO> homeCapitalInterestVOS= capitalInterestDao.initCapitalInterest(capitalInterestVO,corpAuth);
        //如果是折合人民币，将数据分组计算
        if (capitalInterestVO.getCurId()==null && !homeCapitalInterestVOS.isEmpty()) {
            List<HomeCapitalInterestVO> interestVOList=new ArrayList<>();
            // 获取需要折算币种的基数人民币
            BtCurrencyDTO btCurrency = currencyService.getRMBCurrency();
            //month  先根据月份时间类别分组
            Map<String,List<HomeCapitalInterestVO>> interestVOMap=homeCapitalInterestVOS.stream().collect(Collectors.groupingBy(HomeCapitalInterestVO::getMonth));
            for (String month:interestVOMap.keySet()){
                List<HomeCapitalInterestVO> voList=interestVOMap.get(month);
                HomeCapitalInterestVO interestVO=new HomeCapitalInterestVO();
                interestVO.setCorpId(voList.get(0).getCorpId());
                interestVO.setCurId(BtCurrencyConstant.ZH_RMB);
                interestVO.setEndDate(voList.get(0).getEndDate());
                interestVO.setFinType(voList.get(0).getFinType());
                interestVO.setMoneyType(voList.get(0).getMoneyType());
                interestVO.setMonth(month);
                interestVO.setReInterest(BigDecimal.ZERO);
                interestVO.setReMoney(BigDecimal.ZERO);
                interestVO.setStartDate(voList.get(0).getStartDate());

                for (HomeCapitalInterestVO vo:voList){
                    // 查询货币汇率
                    BigDecimal exchangeRate = rateAdjustService.getExchangeRateByNoCheck(vo.getCurId(), btCurrency.getId(), DateUtils.getDate(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);

                    if (EmptyUtils.isEmpty(exchangeRate)) {
                        exchangeRate = BigDecimal.ONE;
                    }
                    if (EmptyUtils.isEmpty(vo.getReInterest())) {
                        vo.setReInterest(BigDecimal.ZERO);
                    }
                    if (EmptyUtils.isEmpty(vo.getReMoney())) {
                        vo.setReMoney(BigDecimal.ZERO);
                    }
                    vo.setReInterest(vo.getReInterest().multiply(exchangeRate));
                    interestVO.setReInterest(interestVO.getReInterest().add(vo.getReInterest()));
                    vo.setReMoney(vo.getReMoney().multiply(exchangeRate));
                    interestVO.setReMoney(interestVO.getReMoney().add(vo.getReMoney()));
                }
                interestVOList.add(interestVO);
            }
            //替换掉折算好的数据
            homeCapitalInterestVOS=interestVOList;
        }


        InitCapitalVo initCapitalVo = new InitCapitalVo();
        if (ListUtils.isNotNullOrEmpty(homeCapitalInterestVOS)){
            Collections.sort(homeCapitalInterestVOS, Comparator.comparing(HomeCapitalInterestVO::getMonth));
            //准备三个集合对应x轴,y,y1轴
            ArrayList<String> xData = new ArrayList<>();
            ArrayList<BigDecimal> yData = new ArrayList<>();
            ArrayList<BigDecimal> y1Data = new ArrayList<>();
            //判断当月是否有数据的标志
            boolean flag = true;
            for (String date : dates) {
                for (HomeCapitalInterestVO homeCapitalInterestVO : homeCapitalInterestVOS) {
                    if (date.equals(homeCapitalInterestVO.getMonth())){
                        String dateStr = homeCapitalInterestVO.getMonth().substring(0,4)+"年"+homeCapitalInterestVO.getMonth().substring(5,homeCapitalInterestVO.getMonth().length())+"月";
                        xData.add(dateStr);
                        yData.add(homeCapitalInterestVO.getReInterest());
                        y1Data.add(homeCapitalInterestVO.getReMoney());
                        flag = false;
                    }
                }
                if (flag){
                    String dateStr = date.substring(0,4)+"年"+date.substring(5,date.length())+"月";
                    xData.add(dateStr);
                    yData.add(new BigDecimal(BigInteger.ZERO));
                    y1Data.add(new BigDecimal(BigInteger.ZERO));
                }
               flag = true;
            }
            initCapitalVo.setxData(xData);
            initCapitalVo.setY1Data(y1Data);//应付本金
            initCapitalVo.setyData(yData);//应付利息
        }
        BigDecimal sumReMoney=homeCapitalInterestVOS.stream().map(HomeCapitalInterestVO::getReMoney).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal sumReInterest=homeCapitalInterestVOS.stream().map(HomeCapitalInterestVO::getReInterest).reduce(BigDecimal.ZERO,BigDecimal::add);
        initCapitalVo.setSumReInterest(sumReInterest.setScale(2, RoundingMode.HALF_UP));
        initCapitalVo.setSumReMoney(sumReMoney.setScale(2, RoundingMode.HALF_UP));
        map.put("initCapitalVo",initCapitalVo);
        return map;
    }



    /**
     * 信贷本息统计表导出
     *
     * @param months 月份
     * @param output 数据
     * @return 表格
     */
    public HSSFWorkbook export(List<String> months, DataTablesOutput output) {
        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        String content = "信贷本息统计表";
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet(content);
        // 设置列宽
        this.setColumnWidth(sheet, months);
        this.mergedRegion(sheet, months);
        // 获取居中样式
        HSSFCellStyle headStyle = ExportUtils.getCenterCellStyle(workbook, true);
        HSSFCellStyle centerContentStyle = ExportUtils.getCenterCellStyle(workbook, true);
        HSSFCellStyle rightContentStyle = ExportUtils.getRightCellStyle(workbook, true);
        // 设置字体
        headStyle.setFont(ExportUtils.getHeadFont(workbook));
        // 创建第一行
        HSSFRow row0 = sheet.createRow(0);
        // 创建行高
        row0.setHeight((short)600);
        // 创建第一列
        HSSFCell cell0 = row0.createCell(0);
        cell0.setCellValue(new HSSFRichTextString("信贷本息统计表"));
        this.setCellStyle(row0, headStyle);
        // 创建第二行
        HSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("申请单位");
        row1.createCell(1).setCellValue("借款单位");
        row1.createCell(2).setCellValue("融资类型");
        row1.createCell(3).setCellValue("申请单号");
        row1.createCell(4).setCellValue("币别");
        row1.createCell(5).setCellValue("借款/注册金额");
        row1.createCell(6).setCellValue("发放/发行单号");
        row1.createCell(7).setCellValue("发放/发行金额");
        row1.createCell(8).setCellValue("发放/发行日期");
        row1.createCell(9).setCellValue("到期日期");
        int startCol = 10;
        for (int i = 0; i < months.size(); i++) {
            row1.createCell(startCol + i).setCellValue(months.get(i));
            row1.createCell(startCol + i + 1);
            startCol++;
        }
        this.setCellStyle(row1, centerContentStyle);
        // 创建第三行
        HSSFRow row2 = sheet.createRow(2);
        startCol = 0;
        for (int i = 0; i < 10 + months.size(); i++) {
            if (i < 10) {
                row2.createCell(i);
            } else {
                row2.createCell(startCol + i).setCellValue("本金");
                row2.createCell(startCol + 1 + i).setCellValue("利息");
                startCol++;
            }
        }
        this.setCellStyle(row2, centerContentStyle);
        // 创建数据行
        List<CapitalInterestVO> infoList = output.getData();
        int startRow = 3;
        for (CapitalInterestVO interestVO : infoList) {
            HSSFRow row = sheet.createRow(startRow);
            row.createCell(0).setCellValue(interestVO.getCorpName());
            row.createCell(1).setCellValue(interestVO.getBorrow());
            row.createCell(2).setCellValue(interestVO.getFinTypeName());
            row.createCell(3).setCellValue(interestVO.getBillCode());
            row.createCell(4).setCellValue(interestVO.getCurName());
            row.createCell(5).setCellValue(null != interestVO.getBorrowMoney() ? transferMoney(interestVO.getBorrowMoney()) : "0.00");
            row.createCell(6).setCellValue(interestVO.getSendCode());
            row.createCell(7).setCellValue(null != interestVO.getSendMoney() ? transferMoney(interestVO.getSendMoney()) : "0.00");
            row.createCell(8).setCellValue(DateUtils.format(interestVO.getSendDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            row.createCell(9).setCellValue(DateUtils.format(interestVO.getEndDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            List<BigDecimal> unMoneyList = interestVO.getUnMoney();
            startCol = 10;
            for (BigDecimal umMoney : unMoneyList) {
                row.createCell(startCol).setCellValue(null != umMoney ? transferMoney(umMoney) : "0.00");
                startCol++;
            }
            this.setCellStyle(row, centerContentStyle, rightContentStyle);
            startRow++;
        }
        return workbook;
    }

    /**
     * 设置列宽
     *
     * @param sheet  sheet页
     * @param months 月数
     */
    private void setColumnWidth(HSSFSheet sheet, List<String> months) {
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 3000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 6000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 4000);
        sheet.setColumnWidth(9, 4000);
        for (int i = 10; i < 10 + months.size() * 2; i++) {
            sheet.setColumnWidth(i, 3000);
        }
    }

    /**
     * 表头合并
     *
     * @param sheet  sheet页
     * @param months 月数
     */
    private void mergedRegion(HSSFSheet sheet, List<String> months) {
        int totalCol = 10 + months.size() * 2 - 1;
        CellRangeAddress range = new CellRangeAddress(0, 0, 0, totalCol);
        sheet.addMergedRegion(range);
        int col = 0;
        for (int i = 0; i <= totalCol; i++) {
            if (i < 10) {
                range = new CellRangeAddress(1, 2, i, i);
                sheet.addMergedRegion(range);
            } else {
                range = new CellRangeAddress(1, 1, i + col, i + col + 1);
                sheet.addMergedRegion(range);
                col++;
            }
        }
    }

    /**
     * 设置表头单元格样式
     *
     * @param row  某行
     * @param cellStyle 单元格样式
     */
    private void setCellStyle(HSSFRow row, HSSFCellStyle cellStyle) {
        Iterator<Cell> iterator = row.cellIterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            cell.setCellStyle(cellStyle);
        }
    }

    /**
     * 设置数据行单元格样式
     *
     * @param row  某行
     * @param centerCellStyle 居中单元格样式
     * @param rightCellStyle 靠右单元格样式
     */
    private void setCellStyle(HSSFRow row, HSSFCellStyle centerCellStyle, HSSFCellStyle rightCellStyle) {
        int col = 0;
        Iterator<Cell> iterator = row.cellIterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            if (col == 5 || col == 7 || col >= 10) {
                cell.setCellStyle(rightCellStyle);
            } else {
                cell.setCellStyle(centerCellStyle);
            }
            col++;
        }
    }

    /**
     * 转换金额格式
     */
    private String transferMoney(BigDecimal money) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(money) + "";
    }
}
