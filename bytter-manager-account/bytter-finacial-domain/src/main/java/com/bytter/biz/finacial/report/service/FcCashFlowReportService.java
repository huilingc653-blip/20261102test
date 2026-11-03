package com.bytter.biz.finacial.report.service;

import cn.hutool.core.map.MapUtil;
import com.bytter.biz.finacial.closedeal.vo.FcCashFlowVO;
import com.bytter.biz.finacial.interest.vo.NoticeAccountVO;
import com.bytter.biz.finacial.report.dao.FcCashFlowReportDao;
import com.bytter.biz.finacial.report.vo.FcCashFlowReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.enums.ParamConfigMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("fcCashFlowReportService")
public class FcCashFlowReportService extends BaseService {
    @Autowired
    private FcCashFlowReportDao cashFlowReportDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    public DataTablesOutput datatable(DataTablesInput input, ICurrentUser user) {
        Map<String, Object> params = input.getFormdata();
        params.put("endDate", params.get("endDate").toString() + " 23:59:59");
        params.put("startDate", params.get("startDate").toString() + " 00:00:00");
        String isConvert = null;
        BtCurrencyDTO convertCur = null;
        String curId = null;
        boolean searchByCurId = false;
        String rateType;
        String netCode=null;
        Date convertDate;
        /**
         * 是否折算本位币
         */
        if (null != params.get("isConvert") && StringUtils.isNotEmpty(params.get("isConvert").toString())) {
            isConvert = params.get("isConvert").toString();
        }
        if(isConvert.equals("1")){
            String convertCurId = params.get("convertCurId").toString();
            if(convertCurId.isEmpty()){
                throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_CURNAME_NOTNULL);
            }
        }
        /**
         * 这算本位币种
         */
        if (null != params.get("convertCurId") && StringUtils.isNotEmpty(params.get("convertCurId").toString())) {
            String convertCurId = params.get("convertCurId").toString();
            convertCur = currencyService.getObjectById(convertCurId);
        }
        /**
         * 这算汇率类型
         */
        if (null != params.get("rateType") && StringUtils.isNotEmpty(params.get("rateType").toString())) {
            rateType = params.get("rateType").toString();
        } else {
            rateType = FcExcrateAdjustConstant.RATE_TYPE_CONVERT;
        }
        /**
         * 折算日期
         */
        if (null != params.get("convertDate") && StringUtils.isNotEmpty(params.get("convertDate").toString())) {
            convertDate = DateUtils.setSpecifiedDateOfTime(DateUtils.parse(params.get("convertDate").toString(), "yyyy-MM-dd"));
        } else {
            convertDate = new Date();
        }
        /**
         * 查询币种
         */
        if (null != params.get("curId") && StringUtils.isNotEmpty(params.get("curId").toString())) {
            curId = params.get("curId").toString();
            searchByCurId = true;
        }
        netCode = params.get("netCode").toString();
        List<String> curIds = fcAccService.getFcAccCurByNetId(netCode);
        Map<String, BtCurrencyDTO> currencyDTOMap=currencyService.getCurrencyByIds(curIds);
        List<BtCurrencyDTO> currencyDTOS=new ArrayList<>();
        for (String cur:curIds){
            currencyDTOS.add(currencyDTOMap.get(cur));
        }
        DataTablesOutput output = new DataTablesOutput();
        List<FcCashFlowVO> cashFlowVODatas;
        Integer pageNum = input.getPageNumber();
        if (CommonConstant.YES.equals(isConvert)) {
            if (searchByCurId) {
                //        放入查询币种
            	if (null != params.get("curId") && StringUtils.isNotEmpty(params.get("curId").toString())) {
            		params.put("curId", curId);
            	 }
                input.setStart(0);
                cashFlowVODatas = cashFlowReportDao.getCashFlowData(params);
                if (pageNum == 2) {
                    curId = null;
                }
            } else {
                if (pageNum - 1 < currencyDTOS.size()) {
                    curId = currencyDTOS.get(pageNum - 1).getId();
                    //        放入查询币种
                    params.put("curId", curId);
                    input.setStart(0);
                    cashFlowVODatas = cashFlowReportDao.getCashFlowData(params);
                } else {
//            跳转至统计页
                    input.setStart(0);
                    cashFlowVODatas = new ArrayList<>();
                    for (BtCurrencyDTO currencyDTO : currencyDTOS) {
                        params.put("curId", currencyDTO.getId());
                        cashFlowVODatas.addAll(cashFlowReportDao.getCashFlowData(params));
                    }
                }
            }
        }else {
            if (searchByCurId) {
                //        放入查询币种
                params.put("curId", curId);
                input.setStart(0);
                cashFlowVODatas = cashFlowReportDao.getCashFlowData(params);
            } else {
                curId = currencyDTOS.get(pageNum - 1).getId();
                //        放入查询币种
                params.put("curId", curId);
                input.setStart(0);
                cashFlowVODatas = cashFlowReportDao.getCashFlowData(params);
            }
        }
//        合并左右数据
        List<FcCashFlowReportVO> fcCashFlowReportVOS = getCashFlowReportVO(cashFlowVODatas, convertCur, convertDate, rateType, isConvert, curId);
//        手动设置分页
        if (StringUtils.isNotEmpty(isConvert) && CommonConstant.YES.equals(isConvert)) {
            //            是否按币种查询
            if (searchByCurId) {
                output.setRecordsTotal(input.getPageSize() * 2);
                output.setRecordsFiltered(input.getPageSize() * 2);
            } else {
                output.setRecordsTotal(input.getPageSize() * (currencyDTOS.size() + 1));
                output.setRecordsFiltered(input.getPageSize() * (currencyDTOS.size() + 1));
            }
        } else {
//            是否按币种查询
            if (searchByCurId) {
                output.setRecordsTotal(input.getPageSize());
                output.setRecordsFiltered(input.getPageSize());
            } else {
                output.setRecordsTotal(input.getPageSize() * (currencyDTOS.size()));
                output.setRecordsFiltered(input.getPageSize() * (currencyDTOS.size()));
            }
        }
        output.setData(fcCashFlowReportVOS);
        output.setDraw(input.getDraw());
        return output;
    }
    /**
     * 组装报表数据格式
     *
     * @param cashFlowVODatas
     * @param convertCur
     * @param convertDate
     * @param rateType
     * @param isConvert
     * @return
     */
    private List<FcCashFlowReportVO> getCashFlowReportVO(List<FcCashFlowVO> cashFlowVODatas,
                                                         BtCurrencyDTO convertCur, Date convertDate,
                                                         String rateType, String isConvert, String curId) {
        List<FcCashFlowVO> leftCashFlowVOData = new ArrayList<>();
        List<FcCashFlowVO> rightCashFlowVOData = new ArrayList<>();

        BtCurrencyDTO currencyDTO;
        if (StringUtils.isNotEmpty(curId)) {
            currencyDTO = currencyService.getObjectById(curId);
            currencyDTO.setNaturalSign(CommonConstant.NO);
        } else {
            currencyDTO = convertCur;
            currencyDTO.setNaturalSign(CommonConstant.YES);
        }
        /**
         * 找出左右数据
         */
        getLeftAndRightData(cashFlowVODatas, leftCashFlowVOData, rightCashFlowVOData, isConvert, convertCur, convertDate, rateType,currencyDTO);
        List<FcCashFlowReportVO> fcCashFlowReportVOS = new ArrayList<>();
//        统计相同项目金额
//        设置左边数据
        setLeftData(leftCashFlowVOData, cashFlowVODatas, fcCashFlowReportVOS, currencyDTO);
//        设置右边数据
        setRightData(rightCashFlowVOData, cashFlowVODatas, fcCashFlowReportVOS, currencyDTO);
        return fcCashFlowReportVOS;
    }
    /**
     * 找出左右数据
     * @param cashFlowVODatas
     * @param isConvert
     * @param convertCur
     * @param convertDate
     * @param rateType
     */
    public void getLeftAndRightData(List<FcCashFlowVO> cashFlowVODatas,
                                    List<FcCashFlowVO> leftCashFlowVOData,
                                    List<FcCashFlowVO> rightCashFlowVOData,
                                    String isConvert, BtCurrencyDTO convertCur,
                                    Date convertDate, String rateType,BtCurrencyDTO currencyDTO) {
        Map<String, BigDecimal> exchangeRateMap = new HashMap<>();
//        这算本位币
        for (FcCashFlowVO cashFlowVOData : cashFlowVODatas) {
            if (CommonConstant.YES.equals(isConvert)) {
                //            取汇率
                BigDecimal exchangeRate = BigDecimal.ONE;
                if (null != exchangeRateMap.get(cashFlowVOData.getCurCode() + "-" + convertCur.getId())) {
                    exchangeRate = exchangeRateMap.get(cashFlowVOData.getCurCode() + "-" + convertCur.getId());
                } else {
                    exchangeRate = fcExcrateAdjustService.getExchangeRateBy(cashFlowVOData.getCurCode(), convertCur.getId(), convertDate, rateType);
                    exchangeRateMap.put(cashFlowVOData.getCurCode() + "-" + convertCur.getId(), exchangeRate);
                }
                // if(currencyDTO.getNaturalSign().equals(CommonConstant.YES)){
                    if (StringUtils.isEmpty(cashFlowVOData.getFormula())) {
                        cashFlowVOData.setMoney(cashFlowVOData.getMoney().multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                // }
            }
            if ("1".equals(cashFlowVOData.getDirection())) {
                leftCashFlowVOData.add(cashFlowVOData);
            } else {
                rightCashFlowVOData.add(cashFlowVOData);
            }
        }
    }
    /**
     * 设置左边数据
     * @param leftCashFlowVOData
     * @param cashFlowVODatas
     * @param fcCashFlowReportVOS
     * @param currencyDTO
     */
    public void setLeftData(List<FcCashFlowVO> leftCashFlowVOData,
                            List<FcCashFlowVO> cashFlowVODatas,
                            List<FcCashFlowReportVO> fcCashFlowReportVOS,
                            BtCurrencyDTO currencyDTO) {
        Map<String, BigDecimal> itemMap = new HashMap<>();
//        设置左边数据
        for (FcCashFlowVO leftCashFlowVODatum : leftCashFlowVOData) {
            BigDecimal money = leftCashFlowVODatum.getMoney();
            if (null == itemMap.get(leftCashFlowVODatum.getCashItem())) {
                if (StringUtils.isNotEmpty(leftCashFlowVODatum.getFormula())) {
                    money = getFormulaVal(leftCashFlowVODatum.getFormula(), cashFlowVODatas, leftCashFlowVODatum.getCurCode());
                }
                itemMap.put(leftCashFlowVODatum.getCashItem(), money);
            } else {
                if (StringUtils.isNotEmpty(leftCashFlowVODatum.getFormula())) {
                    money = getFormulaVal(leftCashFlowVODatum.getFormula(), cashFlowVODatas, leftCashFlowVODatum.getCurCode());
                }
                money = money.add(itemMap.get(leftCashFlowVODatum.getCashItem()));
                itemMap.put(leftCashFlowVODatum.getCashItem(), money);
            }
        }
//        合并相同项目
        Map<String, FcCashFlowVO> leftCashFlowVOMap = new HashMap<>();
        for (FcCashFlowVO leftCashFlowVODatum : leftCashFlowVOData) {
            leftCashFlowVOMap.put(leftCashFlowVODatum.getCashItem(), leftCashFlowVODatum);
        }
//        根据order排序
        List<Map.Entry<String, FcCashFlowVO>> list1 = new ArrayList<>(leftCashFlowVOMap.entrySet());
        Collections.sort(list1, new Comparator<Map.Entry<String, FcCashFlowVO>>() {
            @Override
            public int compare(Map.Entry<String, FcCashFlowVO> o1, Map.Entry<String, FcCashFlowVO> o2) {
                return o1.getValue().getOrderNo().compareTo(o2.getValue().getOrderNo());
            }
        });
//        设置报表VO左边值
        for (Map.Entry<String, FcCashFlowVO> stringFcCashFlowVOEntry : list1) {
            if (stringFcCashFlowVOEntry.getValue().getLineNo() < 200) {
                FcCashFlowReportVO fcCashFlowReportVO = new FcCashFlowReportVO();
                fcCashFlowReportVO.setLeftItemId(stringFcCashFlowVOEntry.getValue().getCashItem());
                fcCashFlowReportVO.setLeftItemName(stringFcCashFlowVOEntry.getValue().getCashItemName());
                fcCashFlowReportVO.setLeftOrderNo(stringFcCashFlowVOEntry.getValue().getOrderNo().toString());
                if (CommonConstant.YES.equals(currencyDTO.getNaturalSign())) {
                    fcCashFlowReportVO.setCurName("折合本位币-" + currencyDTO.getCurName());
                } else {
                    fcCashFlowReportVO.setCurName(currencyDTO.getCurName());
                }
                fcCashFlowReportVO.setLeftMoney(itemMap.get(stringFcCashFlowVOEntry.getValue().getCashItem()));
                fcCashFlowReportVOS.add(fcCashFlowReportVO);
            }
        }
        itemMap.clear();
    }
    /**
     * 设置右边数据
     * @param rightCashFlowVOData
     * @param cashFlowVODatas
     * @param fcCashFlowReportVOS
     * @param currencyDTO
     */
    public void setRightData(List<FcCashFlowVO> rightCashFlowVOData,
                             List<FcCashFlowVO> cashFlowVODatas,
                             List<FcCashFlowReportVO> fcCashFlowReportVOS,
                             BtCurrencyDTO currencyDTO){
        Map<String, BigDecimal> itemMap = new HashMap<>();
//        设置右边数据
        for (int i = 0; i < rightCashFlowVOData.size(); i++) {
            BigDecimal money = rightCashFlowVOData.get(i).getMoney();
            if (null == itemMap.get(rightCashFlowVOData.get(i).getCashItem())) {
                if (StringUtils.isNotEmpty(rightCashFlowVOData.get(i).getFormula())) {
                    money = getFormulaVal(rightCashFlowVOData.get(i).getFormula(), cashFlowVODatas, rightCashFlowVOData.get(i).getCurCode());
                }
                itemMap.put(rightCashFlowVOData.get(i).getCashItem(), money);
            } else {
                if (StringUtils.isNotEmpty(rightCashFlowVOData.get(i).getFormula())) {
                    money = getFormulaVal(rightCashFlowVOData.get(i).getFormula(), cashFlowVODatas, rightCashFlowVOData.get(i).getCurCode());
                }
                money = money.add(itemMap.get(rightCashFlowVOData.get(i).getCashItem()));
                itemMap.put(rightCashFlowVOData.get(i).getCashItem(), money);
            }
        }
//        合并相同项目
        Map<String, FcCashFlowVO> rightCashFlowVOMap = new HashMap<>();
        for (FcCashFlowVO rightCashFlowVODatum : rightCashFlowVOData) {
            rightCashFlowVOMap.put(rightCashFlowVODatum.getCashItem(), rightCashFlowVODatum);
        }
        //        根据order排序
        List<Map.Entry<String, FcCashFlowVO>> list2 = new ArrayList<>(rightCashFlowVOMap.entrySet());
        Collections.sort(list2, new Comparator<Map.Entry<String, FcCashFlowVO>>() {
            @Override
            public int compare(Map.Entry<String, FcCashFlowVO> o1, Map.Entry<String, FcCashFlowVO> o2) {
                return o1.getValue().getOrderNo().compareTo(o2.getValue().getOrderNo());
            }
        });
        Integer i = 0;
        for (Map.Entry<String, FcCashFlowVO> stringFcCashFlowVOEntry : list2) {
            if (stringFcCashFlowVOEntry.getValue().getLineNo() < 200) {
                FcCashFlowReportVO fcCashFlowReportVO = new FcCashFlowReportVO();
                // if (i >= fcCashFlowReportVOS.size()) {
                //     fcCashFlowReportVO = new FcCashFlowReportVO();
                //     fcCashFlowReportVOS.add(fcCashFlowReportVO);
                // } else {
                //     fcCashFlowReportVO = fcCashFlowReportVOS.get(i);
                // }
                fcCashFlowReportVO.setRightItemId(stringFcCashFlowVOEntry.getValue().getCashItem());
                fcCashFlowReportVO.setRightItemName(stringFcCashFlowVOEntry.getValue().getCashItemName());
                fcCashFlowReportVO.setRightOrderNo(stringFcCashFlowVOEntry.getValue().getOrderNo().toString());
                if (CommonConstant.YES.equals(currencyDTO.getNaturalSign())) {
                    fcCashFlowReportVO.setCurName("折合本位币-" + currencyDTO.getCurName());
                } else {
                    fcCashFlowReportVO.setCurName(currencyDTO.getCurName());
                }
                fcCashFlowReportVO.setRightMoney(itemMap.get(stringFcCashFlowVOEntry.getValue().getCashItem()));
                fcCashFlowReportVOS.add(fcCashFlowReportVO);
                i++;
            }
        }
    }
    /**
     * 计算公式数据值
     *
     * @param formula
     * @param cashFlowVODatas
     * @param curId
     * @return
     */
    public BigDecimal getFormulaVal(String formula, List<FcCashFlowVO> cashFlowVODatas, String curId) {
        String[] lineNos = formula.split("[+]");
        BigDecimal result = BigDecimal.ZERO;
        for (String lineNo : lineNos) {
            for (FcCashFlowVO cashFlowVOData : cashFlowVODatas) {
                if (lineNo.equals(cashFlowVOData.getLineNo().toString()) && curId.equals(cashFlowVOData.getCurCode())) {
                    if (StringUtils.isNotEmpty(cashFlowVOData.getFormula())) {
                        BigDecimal tmpMoney = getFormulaVal(cashFlowVOData.getFormula(), cashFlowVODatas, curId);
//                        如果是公式，则将公式结果乘以正负方向
                        if (cashFlowVOData.getRect() == 1) {
                            tmpMoney = tmpMoney.multiply(BigDecimal.ONE);
                        } else {
                            tmpMoney = tmpMoney.multiply(new BigDecimal(-1));
                        }
                        result = null == tmpMoney ? result : result.add(tmpMoney);
                    } else {
                        result = result.add(cashFlowVOData.getMoney());
                    }
                }
            }
        }
        return result;
    }
    /**
     * excel导出
     *
     * @param params
     * @return
     */
    public HSSFWorkbook exportExcel(Map<String, Object> params) {
        List<List<FcCashFlowVO>> groupByCurData = new ArrayList<>();
        List<BtCurrencyDTO> currencyDTOS = currencyService.getBtCurrency();
//        按照币种分组数据
//        getGroupCurId(cashFlowVODatas, groupByCurData, formula);
        String isConvert = null;
        BtCurrencyDTO convertCur = null;
        String rateType;
        Date convertDate;
        String curId = null;
        /**
         * 是否这算本位币
         */
        if (null != params.get("isConvert") && StringUtils.isNotEmpty(params.get("isConvert").toString())) {
            isConvert = params.get("isConvert").toString();
        }
        /**
         * 查询币种
         */
        if (null != params.get("curId") && StringUtils.isNotEmpty(params.get("curId").toString())) {
            curId = params.get("curId").toString();
        }
        /**
         * 这算本位币种
         */
        if (null != params.get("convertCurId") && StringUtils.isNotEmpty(params.get("convertCurId").toString())) {
            String convertCurId = params.get("convertCurId").toString();
            convertCur = currencyService.getObjectById(convertCurId);
        }
        /**
         * 这算汇率类型
         */
        if (null != params.get("rateType") && StringUtils.isNotEmpty(params.get("rateType").toString())) {
            rateType = params.get("rateType").toString();
        } else {
            rateType = FcExcrateAdjustConstant.RATE_TYPE_CONVERT;
        }
        /**
         * 折算日期
         */
        if (null != params.get("convertDate") && StringUtils.isNotEmpty(params.get("convertDate").toString())) {
            convertDate = DateUtils.setSpecifiedDateOfTime(DateUtils.parse(params.get("convertDate").toString(), "yyyy-MM-dd"));
        } else {
            convertDate = new Date();
        }
        if (null != curId) {
            params.put("curId",curId);
            groupByCurData.add(cashFlowReportDao.getCashFlowData(params));
        } else {
            for (BtCurrencyDTO currencyDTO : currencyDTOS) {
                params.put("curId",currencyDTO.getId());
                groupByCurData.add(cashFlowReportDao.getCashFlowData(params));
            }
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("现金流量表");
        HSSFCellStyle headercCellStyle = initColumnHeadStyle(workbook);
        int index = 0;
        //创建 表头
        HSSFRow rowHead = sheet.createRow(index);
        rowHead.setHeightInPoints(20);
        HSSFCell cellHead = rowHead.createCell(0);
        cellHead.setCellValue("现金流量表");
        cellHead.setCellStyle(headercCellStyle);
        for (int i = 1; i < 7; i++) {
            HSSFCell cell = rowHead.createCell(i);
            cell.setCellValue(new HSSFRichTextString());
            cell.setCellStyle(headercCellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(index, index++, (short) 0, (short) 5));
//        表格行标题设置
        String startDate = DateUtils.format(DateUtils.parse(params.get("startDate").toString()),"yyyy-MM-dd");
        String endDate = DateUtils.format(DateUtils.parse(params.get("endDate").toString()),"yyyy-MM-dd");
        HSSFCellStyle contentCellStyle = initColumnCenterstyle(workbook);
        String corpName = corpService.get(params.get("netCode").toString()).getName();
        String curName = null;
//        设置表头
        setHead(sheet, contentCellStyle, index++);
        List<FcCashFlowReportVO> fcCashFlowReportVOS = null;
        Map<String, BigDecimal> leftTotalMap = new HashMap<>();
        Map<String, BigDecimal> rightTotalMap = new HashMap<>();
        List<FcCashFlowReportVO> fcCashFlowReportVOTotal = new ArrayList<>();
        for (List<FcCashFlowVO> groupByCurDatum : groupByCurData) {
            //            组装报表格式数据
            curId = groupByCurDatum.get(0).getCurCode();
            curName = currencyService.getObjectById(curId).getCurName();
            fcCashFlowReportVOS = getCashFlowReportVO(groupByCurDatum, convertCur, convertDate, rateType, isConvert, curId);
            setOtherInfo(sheet, index++, contentCellStyle, corpName, startDate, endDate, curName);
            index = setContent(fcCashFlowReportVOS, index, sheet, contentCellStyle);
            for (FcCashFlowReportVO fcCashFlowReportVO : fcCashFlowReportVOS) {
                if (null == leftTotalMap.get(fcCashFlowReportVO.getLeftItemId())) {
                    leftTotalMap.put(fcCashFlowReportVO.getLeftItemId(), fcCashFlowReportVO.getLeftMoney());
                } else {
                    leftTotalMap.put(fcCashFlowReportVO.getLeftItemId(), leftTotalMap.get(fcCashFlowReportVO.getLeftItemId()).add(fcCashFlowReportVO.getLeftMoney()));
                }
                if (null == rightTotalMap.get(fcCashFlowReportVO.getRightItemId())) {
                    rightTotalMap.put(fcCashFlowReportVO.getRightItemId(), fcCashFlowReportVO.getRightMoney());
                } else {
                    rightTotalMap.put(fcCashFlowReportVO.getRightItemId(), rightTotalMap.get(fcCashFlowReportVO.getRightItemId()).add(fcCashFlowReportVO.getRightMoney()));
                }
            }
            if (ListUtils.isNullOrEmpty(fcCashFlowReportVOTotal)) {
                fcCashFlowReportVOTotal.addAll(fcCashFlowReportVOS);
            }
        }
        if (CommonConstant.YES.equals(isConvert) && null != convertCur && null != convertDate) {
            //            组装报表格式数据
            for (FcCashFlowReportVO fcCashFlowReportVO : fcCashFlowReportVOTotal) {
                fcCashFlowReportVO.setLeftMoney(leftTotalMap.get(fcCashFlowReportVO.getLeftItemId()));
                fcCashFlowReportVO.setRightMoney(rightTotalMap.get(fcCashFlowReportVO.getRightItemId()));
            }
            setOtherInfo(sheet, index++, contentCellStyle, corpName, startDate, endDate, convertCur.getCurName());
            setContent(fcCashFlowReportVOTotal, index, sheet, contentCellStyle);
        }
        return workbook;
    }
    /**
     * 设置其他信息
     * @param sheet
     * @param index
     * @param contentCellStyle
     * @param corpName
     * @param startDate
     * @param endDate
     * @param curName
     */
    private void setOtherInfo(HSSFSheet sheet, int index, HSSFCellStyle contentCellStyle, String corpName, String startDate,String endDate,String curName) {
        HSSFRow row = sheet.createRow(index);
        HSSFCell cell = row.createCell(0);
        cell.setCellStyle(contentCellStyle);
        cell.setCellValue("编制单位:" + corpName);
        cell = row.createCell(2);
        cell.setCellStyle(contentCellStyle);
        cell.setCellValue("日期:" + startDate + "至" + endDate);
        cell = row.createCell(4);
        cell.setCellStyle(contentCellStyle);
        cell.setCellValue("币种:" + curName);
        sheet.addMergedRegion(new CellRangeAddress(index, index, (short) 0, (short) 1));
        sheet.addMergedRegion(new CellRangeAddress(index, index, (short) 2, (short) 3));
        sheet.addMergedRegion(new CellRangeAddress(index, index, (short) 4, (short) 5));
        index++;
    }
    /**
     * 设置表头格式
     * @param wb
     * @return
     */
    private static HSSFCellStyle initColumnHeadStyle(HSSFWorkbook wb) {
        HSSFCellStyle columnHeadStyle = wb.createCellStyle();
        HSSFFont columnHeadFont = wb.createFont();
        columnHeadFont.setFontName("宋体");
        columnHeadFont.setFontHeightInPoints((short) 10);
        columnHeadFont.setBold(true);
        columnHeadStyle.setFont(columnHeadFont);
        columnHeadStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        columnHeadStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        columnHeadStyle.setLocked(true);
        columnHeadStyle.setWrapText(true);
        columnHeadStyle.setLeftBorderColor(HSSFColor.BLACK.index);// 左边框的颜色
        columnHeadStyle.setBorderLeft(BorderStyle.THIN);// 边框的大小
        columnHeadStyle.setRightBorderColor(HSSFColor.BLACK.index);// 右边框的颜色
        columnHeadStyle.setBorderRight(BorderStyle.THIN);// 边框的大小
        columnHeadStyle.setBorderBottom(BorderStyle.THIN); // 设置单元格的边框为粗体
        columnHeadStyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色
        // 设置单元格的背景颜色（单元格的样式会覆盖列或行的样式）
        columnHeadStyle.setFillForegroundColor(HSSFColor.WHITE.index);
        return columnHeadStyle;
    }
    /**
     * 设置内容格式
     * @param wb
     * @return
     */
    public static HSSFCellStyle initColumnCenterstyle(HSSFWorkbook wb) {
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);
        HSSFCellStyle centerstyle = wb.createCellStyle();
        centerstyle.setFont(font);
        centerstyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        centerstyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        centerstyle.setWrapText(true);//自动换行
        centerstyle.setLeftBorderColor(HSSFColor.BLACK.index);
        centerstyle.setBorderLeft(BorderStyle.THIN);
        centerstyle.setRightBorderColor(HSSFColor.BLACK.index);
        centerstyle.setBorderRight(BorderStyle.THIN);
        centerstyle.setBorderBottom(BorderStyle.THIN); // 设置单元格的边框为粗体
        centerstyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
        centerstyle.setFillForegroundColor(HSSFColor.WHITE.index);// 设置单元格的背景颜色．
        return centerstyle;
    }
    /**
     *
     * @param sheet
     * @param cellStyle
     * @param index
     * @return
     */
    private int setHead(HSSFSheet sheet, HSSFCellStyle cellStyle, int index) {
        String[] heads = null;
        heads = new String[]{"项目", "行次", "金额", "项目", "行次", "金额",};
        HSSFRow rowHead = sheet.createRow(index++);
        rowHead.setHeightInPoints(20);
        for (int i = 0; i < heads.length; i++) {
            HSSFCell cell = rowHead.createCell(i);
            cell.setCellValue(heads[i]);
            cell.setCellStyle(cellStyle);
            sheet.setColumnWidth(i, 256 * 18);
        }
        return index;
    }
    /**
     * 设置内容
     */
    public int setContent(List<FcCashFlowReportVO> fcCashFlowReportVOS, int index, HSSFSheet sheet, HSSFCellStyle cellStyle) {
        for (FcCashFlowReportVO fcCashFlowReportVO : fcCashFlowReportVOS) {
            HSSFRow rowHead = sheet.createRow(index++);
            HSSFCell cell = rowHead.createCell(0);
            cell.setCellValue(fcCashFlowReportVO.getLeftItemName());
            cell.setCellStyle(cellStyle);
            cell = rowHead.createCell(1);
            cell.setCellValue(fcCashFlowReportVO.getLeftOrderNo());
            cell.setCellStyle(cellStyle);
            cell = rowHead.createCell(2);
            if (StringUtils.isNotEmpty(fcCashFlowReportVO.getLeftItemName())) {
                if (null == fcCashFlowReportVO.getLeftMoney()) {
                    cell.setCellValue("0.00");
                } else {
                    cell.setCellValue(fcCashFlowReportVO.getLeftMoney().toString());
                }
            } else {
                cell.setCellValue("");
            }
            cell.setCellStyle(cellStyle);
            cell = rowHead.createCell(3);
            cell.setCellValue(fcCashFlowReportVO.getRightItemName());
            cell.setCellStyle(cellStyle);
            cell = rowHead.createCell(4);
            cell.setCellValue(fcCashFlowReportVO.getRightOrderNo());
            cell.setCellStyle(cellStyle);
            cell = rowHead.createCell(5);
            if (StringUtils.isNotEmpty(fcCashFlowReportVO.getRightItemName())) {
                if (null == fcCashFlowReportVO.getRightMoney()) {
                    cell.setCellValue("0.00");
                } else {
                    cell.setCellValue(fcCashFlowReportVO.getRightMoney().toString());
                }
            } else {
                cell.setCellValue("");
            }
            cell.setCellStyle(cellStyle);
        }
        return index;
    }
    /**
     * 将数据按照币种分组，且找出公式数据
     * @param cashFlowVODatas
     * @param groupData
     * @param formula
     */
    public void getGroupCurId(List<FcCashFlowVO> cashFlowVODatas,
                              List<List<FcCashFlowVO>> groupData,
                              List<FcCashFlowVO> formula){
        String curId = null;
        List<FcCashFlowVO> fcCashFlowVOS = null;
        if (ListUtils.isNotNullOrEmpty(cashFlowVODatas)) {
            for (FcCashFlowVO cashFlowVOData : cashFlowVODatas) {
                if (cashFlowVOData.getLineNo() >= 200 && StringUtils.isNotEmpty(cashFlowVOData.getFormula())) {
                    formula.add(cashFlowVOData);
                } else {
                    if (StringUtils.isNotEmpty(cashFlowVOData.getCurCode())) {
                        if (StringUtils.isEmpty(curId) || !curId.equals(cashFlowVOData.getCurCode())) {
                            fcCashFlowVOS = new ArrayList<>();
                            groupData.add(fcCashFlowVOS);
                        }
                        fcCashFlowVOS.add(cashFlowVOData);
                        curId = cashFlowVOData.getCurCode();
                    }
                }
            }
        }
    }
    @Override
    public BaseDao getEntityDao() {
        return cashFlowReportDao;
    }
}
