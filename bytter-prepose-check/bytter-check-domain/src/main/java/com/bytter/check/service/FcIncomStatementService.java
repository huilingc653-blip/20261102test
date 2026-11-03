package com.bytter.check.service;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.check.dao.FcIncomStatementDao;
import com.bytter.check.vo.FcBanlanceSheetVO;
import com.bytter.check.vo.FcIncomStatementVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
@Service("fcIncomStatementService")
@Transactional
public class FcIncomStatementService extends BaseService<FcIncomStatementVO, String> {
    @Autowired
    private FcIncomStatementDao fcIncomStatementDao;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
	private IFcExcrateAdjustService excrateAdjustService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @DubboReference
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @Override
    public BaseDao<FcIncomStatementVO, String> getEntityDao() {
        return fcIncomStatementDao;
    }
    public DataTablesOutput getFcIncomeStatement(DataTablesInput input) {
        Map<String, Object> params = input.getFormdata();
        String isConvert = null;
        Date preDate = new Date();
        String convertCurId = null;
        String rateType = null;
        if (null != params.get("isConvert") && StringUtils.isNotEmpty(params.get("isConvert").toString())) {
            isConvert = params.get("isConvert").toString();
        }
        if (null != params.get("preDate") && StringUtils.isNotEmpty(params.get("preDate").toString())) {
            preDate = DateUtils.parse(params.get("preDate").toString(), "yyyy-MM-dd");
        }
        if (CommonConstant.YES.equals(isConvert)) {
            if (null != params.get("convertCurId") && StringUtils.isNotEmpty(params.get("convertCurId").toString())) {
                convertCurId = params.get("convertCurId").toString();
            }
            if (null != params.get("rateType") && StringUtils.isNotEmpty(params.get("rateType").toString())) {
                rateType = params.get("rateType").toString();
            }
        }
        DataTablesOutput output;
        List<Map<String, String>> curs = null;
        if (null == params.get("eqs_curCode") || StringUtils.isEmpty(params.get("eqs_curCode").toString())) {
            curs = fcIncomStatementDao.getFcIncomeStatementCurId(params.get("eqs_netCode").toString(), params.get("checkDate").toString());
        }
        if (curs != null && curs.size() > 0 && (input.getStart() / input.getLength()) < curs.size()) {
            //兼容orcl和mysql
            String curCode = curs.get((input.getStart() / input.getLength())).get("CUR_CODE");
            if (StringUtils.isBlank(curCode)) {
                curCode = curs.get((input.getStart() / input.getLength())).get("cur_code");
            }
            params.put("eqs_curCode", curCode);
            // 一直从第一条数据查
            input.setStart(0);
        } else {
            if (input.getStart() >= 10) {
                // 一直从第一条数据查
                input.setStart(0);
                if (ListUtils.isNullOrEmpty(curs) && StringUtils.isNotBlank(params.get("eqs_curCode").toString())) {
                    curs = new ArrayList<>();
                    Map<String, String> map = new HashMap<>();
                    map.put("CUR_CODE",params.get("eqs_curCode").toString());
                    curs.add(map);
                }
                return getIncomeStatementTotaolMoney(input, preDate, ListUtils.isNullOrEmpty(curs) ? 1 : curs.size());
            }
        }
        input.getFormdata().remove("isConvert");
        input.getFormdata().remove("preDate");
        input.getFormdata().remove("convertCurId");
        input.getFormdata().remove("rateType");
        if (CommonConstant.YES.equals(isConvert)) {//折算本位币
            output = fcIncomStatementDao.getFcIncomeStatement(input);
            BigDecimal exchangeRate = BigDecimal.ONE;
            List<FcIncomStatementVO> fcIncomStatementVOS = new ArrayList<>();
            for (Object data : output.getData()) {
                FcIncomStatementVO fcIncomStatementVO = (FcIncomStatementVO) data;
//				根据币种获取汇率
                if (StringUtils.isNotEmpty(fcIncomStatementVO.getCurCode()) && StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(rateType)) {
                    preDate = DateUtils.setSpecifiedDateOfTime(preDate);
                    exchangeRate = excrateAdjustService.getExchangeRateBy(fcIncomStatementVO.getCurCode(), convertCurId, preDate, rateType);
                }
//                exchangeRate = new BigDecimal(3.8);
//				进行折算
                fcIncomStatementVO.setBalance(fcIncomStatementVO.getBalance().multiply(exchangeRate));
                fcIncomStatementVO.setYearTotal(fcIncomStatementVO.getYearTotal().multiply(exchangeRate));
                fcIncomStatementVOS.add(fcIncomStatementVO);
            }
            output.setData(fcIncomStatementVOS);
        } else {
            output = fcIncomStatementDao.getFcIncomeStatement(input);
        }
        this.fillDataFcIncom(output);
        if (curs != null && curs.size() > 0) {
//			手动设置分页
            if (null != output.getData() && output.getData().size() > 0) {
                if (CommonConstant.YES.equals(isConvert)) {//折合本位币，手动增加一页显示合计
                    output.setRecordsFiltered(input.getPageSize() * (curs.size() + 1));
                    output.setRecordsTotal(input.getPageSize() * (curs.size() + 1));
                } else {
                    output.setRecordsFiltered(input.getPageSize() * curs.size());
                    output.setRecordsTotal(input.getPageSize() * curs.size());
                }
            } else {
                output.setRecordsFiltered(0);
                output.setRecordsTotal(0);
            }
        } else {
            if (null != output.getData() && output.getData().size() > 0) {
                if (CommonConstant.YES.equals(isConvert)) {//折合本位币，手动增加一页显示合计
                    output.setRecordsFiltered(input.getPageSize() * 2);
                    output.setRecordsTotal(input.getPageSize() * 2);
                }
            } else {
            	output = new DataTablesOutput();
            }
        }
        return output;
    }
    /**
     * 计算合计,并分类
     *
     * @param input
     * @param preDate
     * @return
     */
    public DataTablesOutput getIncomeStatementTotaolMoney(DataTablesInput input, Date preDate, Integer curSize) {
        Map<String, Object> params = input.getFormdata();
        String convertCurId = null;
        String rateType = null;
        String curName = null;
        if (null != params.get("convertCurId") && StringUtils.isNotEmpty(params.get("convertCurId").toString())) {
            convertCurId = params.get("convertCurId").toString();
        }
        if (null != params.get("rateType") && StringUtils.isNotEmpty(params.get("rateType").toString())) {
            rateType = params.get("rateType").toString();
        }
        Integer size = input.getPageSize() * (curSize + 1);
        input.setPageSize(size);
        input.getFormdata().remove("isConvert");
        input.getFormdata().remove("preDate");
        input.getFormdata().remove("convertCurId");
        input.getFormdata().remove("rateType");
        DataTablesOutput output = fcIncomStatementDao.getFcIncomeStatement(input);
        List<FcIncomStatementVO> fcIncomStatementVOS = new ArrayList<>();
//		存放分类数据
        Map<String, FcIncomStatementVO> totalCategory = new HashMap<>();
        BigDecimal exchangeRate = BigDecimal.ONE;
/*        FcIncomStatementVO totalData = new FcIncomStatementVO();
        totalData.setYearTotal(BigDecimal.ZERO);
        totalData.setBalance(BigDecimal.ZERO);
        totalData.setItem("合计");*/
        if (StringUtils.isNotEmpty(convertCurId)) {
            BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(convertCurId);
            curName = "折合本位币-" + currencyDTO.getCurName();
        }
        for (Object data : output.getData()) {
            FcIncomStatementVO fcIncomStatementVO = (FcIncomStatementVO) data;
//				根据币种获取汇率
            if (StringUtils.isNotEmpty(fcIncomStatementVO.getCurCode()) && StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(rateType)) {
                preDate = DateUtils.setSpecifiedDateOfTime(preDate);
                exchangeRate = excrateAdjustService.getExchangeRateBy(fcIncomStatementVO.getCurCode(), convertCurId, preDate, rateType);
            }
//            exchangeRate = new BigDecimal(3.8);
            fcIncomStatementVO.setBalance(fcIncomStatementVO.getBalance().multiply(exchangeRate));
            fcIncomStatementVO.setYearTotal(fcIncomStatementVO.getYearTotal().multiply(exchangeRate));
//			合计
//            totalData.setBalance(totalData.getBalance().add(fcIncomStatementVO.getBalance()));
//            totalData.setYearTotal(totalData.getYearTotal().add(fcIncomStatementVO.getYearTotal()));
//			分类汇总
            if (null != totalCategory.get(fcIncomStatementVO.getItem())) {
                FcIncomStatementVO fcIncomStatementVOTmp = totalCategory.get(fcIncomStatementVO.getItem());
                fcIncomStatementVOTmp.setBalance(fcIncomStatementVOTmp.getBalance().add(fcIncomStatementVO.getBalance()));
                fcIncomStatementVOTmp.setYearTotal(fcIncomStatementVOTmp.getYearTotal().add(fcIncomStatementVO.getYearTotal()));
                fcIncomStatementVOTmp.setCurName(curName);//合计设置币种为空
                totalCategory.put(fcIncomStatementVO.getItem(), fcIncomStatementVOTmp);
            } else {
                fcIncomStatementVO.setCurName(curName);
                fcIncomStatementVO.setTotalFlag(true);
                totalCategory.put(fcIncomStatementVO.getItem(), fcIncomStatementVO);
            }
        }
        for (Map.Entry<String, FcIncomStatementVO> fcIncomStatementVOEntry : totalCategory.entrySet()) {
            fcIncomStatementVOS.add(fcIncomStatementVOEntry.getValue());
        }
        this.fillData2StatementVO(fcIncomStatementVOS);
        //调整顺序
        sortIncomeStatementByOrderNum(fcIncomStatementVOS);
//        fcIncomStatementVOS.add(totalData);
        output.setRecordsFiltered(size);
        output.setRecordsTotal(size);
        output.setData(fcIncomStatementVOS);
        return output;
    }
    /**
     * 拆SQL，改为接口调用
     * @param fcIncomStatementVOS
     */
    private void fillData2StatementVO(List<FcIncomStatementVO> fcIncomStatementVOS){
        //查询curName
        List<String> curIds = fcIncomStatementVOS.stream().map(FcIncomStatementVO::getCurCode).collect(Collectors.toList());
        Map<String, BtCurrencyDTO> curIdAndNameMap = btCurrencyService.getCurrencyByIds(curIds);
        //查询name
        List<String> corpIds = fcIncomStatementVOS.stream().map(FcIncomStatementVO::getNetCode).collect(Collectors.toList());
        Map<String, CorpDTO> corpDTOMap = iCorpService.listCorpDtoByCorpIds(corpIds);
        fcIncomStatementVOS.forEach(a -> {
            //此处不要怀疑，据说整个票据 code --> id
            String curId = a.getCurCode();
            String corpId = a.getNetCode();
            if (ObjectUtil.isEmpty(a.getCurName())) {
                a.setCurName(curIdAndNameMap.get(curId).getCurName());
            }
            a.setNetName(corpDTOMap.get(corpId).getName()); //corpName = netName
        });
    }
    private void fillData2SheetVO(List<FcBanlanceSheetVO> fcBanlanceSheetVOS){
        //查询curName
        List<String> curIds = fcBanlanceSheetVOS.stream().map(FcBanlanceSheetVO::getCurCode).collect(Collectors.toList());
        Map<String, BtCurrencyDTO> curIdAndNameMap = btCurrencyService.getCurrencyByIds(curIds);
        //查询name
        List<String> corpIds = fcBanlanceSheetVOS.stream().map(FcBanlanceSheetVO::getNetCode).collect(Collectors.toList());
        Map<String, CorpDTO> corpDTOMap = iCorpService.listCorpDtoByCorpIds(corpIds);
        fcBanlanceSheetVOS.forEach(a -> {
            //此处不要怀疑，据说整个票据 code --> id
            String curId = a.getCurCode();
            String corpId = a.getNetCode();
            if (ObjectUtil.isEmpty(a.getCurName())) {
                a.setCurName(curIdAndNameMap.get(curId).getCurName());
            }
            a.setNetName(corpDTOMap.get(corpId).getName()); //corpName = netName
        });
    }
    /**
     * 按照行次排序
     *
     * @param fcIncomStatementVOS
     */
    public void sortIncomeStatementByOrderNum(List<FcIncomStatementVO> fcIncomStatementVOS) {
        FcIncomStatementVO Tmp = null;
        for (int i = 0; i < fcIncomStatementVOS.size(); i++) {
            for (int j = 0; j < fcIncomStatementVOS.size() - i - 1; j++) {
                if (fcIncomStatementVOS.get(j).getOrderNo() > fcIncomStatementVOS.get(j + 1).getOrderNo()) {
                    Tmp = fcIncomStatementVOS.get(j);
                    fcIncomStatementVOS.set(j, fcIncomStatementVOS.get(j + 1));
                    fcIncomStatementVOS.set(j + 1, Tmp);
                }
            }
        }
    }
    public DataTablesOutput getFcBanlanceSheet(DataTablesInput input) {
        Map<String, Object> params = input.getFormdata();
        String isConvert = null;
        String convertCurId = null;
        String rateType = null;
        String netCode=null;
        Date preDate = new Date();
        DataTablesOutput output;
        List<Map<String, String>> curs = null;
        if (null != params.get("isConvert") && StringUtils.isNotEmpty(params.get("isConvert").toString())) {
            isConvert = params.get("isConvert").toString();
        }
        if (null != params.get("preDate") && StringUtils.isNotEmpty(params.get("preDate").toString())) {
            preDate = DateUtils.parse(params.get("preDate").toString(), "yyyy-MM-dd");
        }
        if (CommonConstant.YES.equals(isConvert)) {
            if (null != params.get("convertCurId") && StringUtils.isNotEmpty(params.get("convertCurId").toString())) {
                convertCurId = params.get("convertCurId").toString();
            }
            if (null != params.get("rateType") && StringUtils.isNotEmpty(params.get("rateType").toString())) {
                rateType = params.get("rateType").toString();
            }
        }
        if (null == params.get("eqs_curCode") || StringUtils.isEmpty(params.get("eqs_curCode").toString())) {
//            curs = fcIncomStatementDao.getFcBanlanceSheetCurId(params.get("eqs_netCode").toString(), params.get("checkDate").toString());
            netCode = params.get("eqs_netCode").toString();
            curs = fcAccService.getFcAccCurByNetIds(netCode);
        }
        if (curs != null && curs.size() > 0 && (input.getStart() / input.getLength()) < curs.size()) {
            //兼容orcl和mysql
            String curCode = curs.get((input.getStart() / input.getLength())).get("CUR_CODE");
            if (StringUtils.isBlank(curCode)) {
                curCode = curs.get((input.getStart() / input.getLength())).get("cur_code");
            }
            params.put("eqs_curCode", curCode);
            input.getFormdata().put("eqs_curCode", params.get("eqs_curCode"));
            //			一直从第一条数据查
            input.setStart(0);
        } else {
            if (input.getStart() >= 10) {
                //			一直从第一条数据查
                input.setStart(0);
                return getFcBanlanceSheetTotaolMoney(input, preDate, ListUtils.isNullOrEmpty(curs) ? 1 : curs.size());
            }
        }
        input.getFormdata().remove("isConvert");
        input.getFormdata().remove("preDate");
        input.getFormdata().remove("convertCurId");
        input.getFormdata().remove("rateType");
        if (CommonConstant.YES.equals(isConvert)) {//折算本位币
            output = fcIncomStatementDao.getFcBanlanceSheet(input);
            BigDecimal exchangeRate = BigDecimal.ONE;
            BigDecimal lastChange = BigDecimal.ZERO;
            List<FcBanlanceSheetVO> fcBanlanceSheetVOS = new ArrayList<>();
            for (Object data : output.getData()) {
                FcBanlanceSheetVO fcBanlanceSheetVO = (FcBanlanceSheetVO) data;
//				根据币种获取汇率
                if (StringUtils.isNotEmpty(fcBanlanceSheetVO.getCurCode()) && StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(rateType)) {
                    preDate = DateUtils.setSpecifiedDateOfTime(preDate);
                    exchangeRate = excrateAdjustService.getExchangeRateBy(fcBanlanceSheetVO.getCurCode(), convertCurId, preDate, rateType);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(preDate);
                    int year = calendar.get(Calendar.YEAR);
                    lastChange = excrateAdjustService.getExchangeRateBy(fcBanlanceSheetVO.getCurCode(), convertCurId, DateUtils.parse((year-1)+"-12-31 23:59:59"), rateType);
                }
//                exchangeRate = new BigDecimal(3.8);
//				进行折算
                fcBanlanceSheetVO.setaYearBalance(fcBanlanceSheetVO.getaYearBalance().multiply(lastChange));
                fcBanlanceSheetVO.setaMonthBalance(fcBanlanceSheetVO.getaMonthBalance().multiply(exchangeRate));
                fcBanlanceSheetVO.setlYearBalance(fcBanlanceSheetVO.getlYearBalance().multiply(lastChange));
                fcBanlanceSheetVO.setlMonthBalance(fcBanlanceSheetVO.getlMonthBalance().multiply(exchangeRate));
                fcBanlanceSheetVOS.add(fcBanlanceSheetVO);
            }
            output.setData(fcBanlanceSheetVOS);
        } else {
            output = fcIncomStatementDao.getFcBanlanceSheet(input);
        }
        this.fillDatafcBalance(output);
        //			手动设置分页
        if (curs != null && curs.size() > 0) {
            if (null != output.getData() && output.getData().size() > 0) {
                if (CommonConstant.YES.equals(isConvert)) {//折合本位币，手动增加一页显示合计
                    output.setRecordsFiltered(input.getPageSize() * (curs.size() + 1));
                    output.setRecordsTotal(input.getPageSize() * (curs.size() + 1));
                } else {
                    output.setRecordsFiltered(input.getPageSize() * curs.size());
                    output.setRecordsTotal(input.getPageSize() * curs.size());
                }
            } else {
                output.setRecordsFiltered(0);
                output.setRecordsTotal(0);
            }
        } else {
            if (null != output.getData() && output.getData().size() > 0) {
                if (CommonConstant.YES.equals(isConvert)) {//折合本位币，手动增加一页显示合计
                    output.setRecordsFiltered(input.getPageSize() * 2);
                    output.setRecordsTotal(input.getPageSize() * 2);
                }
            } else {
                output.setRecordsFiltered(0);
                output.setRecordsTotal(0);
            }
        }
        return output;
    }
    /**
     * 计算合计
     *
     * @param input
     * @param preDate
     * @return
     */
    public DataTablesOutput getFcBanlanceSheetTotaolMoney(DataTablesInput input, Date preDate, Integer curSize) {
        Map<String, Object> params = input.getFormdata();
        String convertCurId = null;
        String rateType = null;
        String curName = null;
        if (null != params.get("convertCurId") && StringUtils.isNotEmpty(params.get("convertCurId").toString())) {
            convertCurId = params.get("convertCurId").toString();
        }
        if (null != params.get("rateType") && StringUtils.isNotEmpty(params.get("rateType").toString())) {
            rateType = params.get("rateType").toString();
        }
        Integer size = input.getPageSize() * (curSize + 1);
        input.setPageSize(size);
        input.getFormdata().remove("isConvert");
        input.getFormdata().remove("preDate");
        input.getFormdata().remove("convertCurId");
        input.getFormdata().remove("rateType");
        DataTablesOutput output = fcIncomStatementDao.getFcBanlanceSheet(input);
        List<FcBanlanceSheetVO> fcBanlanceSheetVOS = new ArrayList<>();
        //		存放分类数据
        Map<String, FcBanlanceSheetVO> totalCategory = new HashMap<>();
        BigDecimal exchangeRate = BigDecimal.ONE;
        BigDecimal lastChange = BigDecimal.ONE;
        if (StringUtils.isNotEmpty(convertCurId)) {
            BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(convertCurId);
            curName = "折合本位币-" + currencyDTO.getCurName();
        }
        for (Object data : output.getData()) {
            FcBanlanceSheetVO fcBanlanceSheetVO = (FcBanlanceSheetVO) data;
//				根据币种获取汇率
            if (StringUtils.isNotEmpty(fcBanlanceSheetVO.getCurCode()) && StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(rateType)) {
                preDate = DateUtils.setSpecifiedDateOfTime(preDate);
                exchangeRate = excrateAdjustService.getExchangeRateBy(fcBanlanceSheetVO.getCurCode(), convertCurId, preDate, rateType);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(preDate);
                int year = calendar.get(Calendar.YEAR);
                lastChange = excrateAdjustService.getExchangeRateBy(fcBanlanceSheetVO.getCurCode(), convertCurId, DateUtils.parse((year-1)+"-12-31 23:59:59"), rateType);
            }
            //				进行折算，并计算合计金额
            fcBanlanceSheetVO.setaYearBalance(fcBanlanceSheetVO.getaYearBalance().multiply(lastChange));
            fcBanlanceSheetVO.setaMonthBalance(fcBanlanceSheetVO.getaMonthBalance().multiply(exchangeRate));
            fcBanlanceSheetVO.setlYearBalance(fcBanlanceSheetVO.getlYearBalance().multiply(lastChange));
            fcBanlanceSheetVO.setlMonthBalance(fcBanlanceSheetVO.getlMonthBalance().multiply(exchangeRate));
            //			分类汇总
            String item = Convert.toStr(fcBanlanceSheetVO.getaItem(),fcBanlanceSheetVO.getlItem());
            if (null != totalCategory.get(item)) {
                FcBanlanceSheetVO fcBanlanceSheetVOTmp = totalCategory.get(fcBanlanceSheetVO.getaItem());
                fcBanlanceSheetVOTmp.setaYearBalance(fcBanlanceSheetVOTmp.getaYearBalance().add(fcBanlanceSheetVO.getaYearBalance()));
                fcBanlanceSheetVOTmp.setaMonthBalance(fcBanlanceSheetVOTmp.getaMonthBalance().add(fcBanlanceSheetVO.getaMonthBalance()));
                fcBanlanceSheetVOTmp.setlYearBalance(fcBanlanceSheetVOTmp.getlYearBalance().add(fcBanlanceSheetVO.getlYearBalance()));
                fcBanlanceSheetVOTmp.setlMonthBalance(fcBanlanceSheetVOTmp.getlMonthBalance().add(fcBanlanceSheetVO.getlMonthBalance()));
                fcBanlanceSheetVOTmp.setCurName(curName);//合计设置币种为空
                totalCategory.put(item, fcBanlanceSheetVOTmp);
            } else {
                fcBanlanceSheetVO.setCurName(curName);
                fcBanlanceSheetVO.setTotalFlag(true);
                totalCategory.put(item, fcBanlanceSheetVO);
            }
        }
        for (Map.Entry<String, FcBanlanceSheetVO> fcBanlanceSheetVOEntry : totalCategory.entrySet()) {
            fcBanlanceSheetVOS.add(fcBanlanceSheetVOEntry.getValue());
        }
//        fcBanlanceSheetVOS.add(totalData);
        output.setRecordsFiltered(size);
        output.setRecordsTotal(size);
        sortFcBanlanceSheetByOrderNum(fcBanlanceSheetVOS);
        this.fillData2SheetVO(fcBanlanceSheetVOS);
        output.setData(fcBanlanceSheetVOS);
        return output;
    }
    /**
     * 按照行次排序
     *
     * @param fcBanlanceSheetVOS
     */
    public void sortFcBanlanceSheetByOrderNum(List<FcBanlanceSheetVO> fcBanlanceSheetVOS) {
        FcBanlanceSheetVO Tmp = null;
        for (int i = 0; i < fcBanlanceSheetVOS.size(); i++) {
            for (int j = 0; j < fcBanlanceSheetVOS.size() - i - 1; j++) {
                // 行次号，非左即右
                FcBanlanceSheetVO currentBalance = fcBanlanceSheetVOS.get(j);
                FcBanlanceSheetVO nextBalance = fcBanlanceSheetVOS.get(j+1);
                Integer currentOrderNo = Convert.toInt(currentBalance.getaOrderNo(), currentBalance.getlOrderNo());
                Integer nextOrderNo = Convert.toInt(nextBalance.getaOrderNo(), nextBalance.getlOrderNo());
                if (currentOrderNo > nextOrderNo) {
                    Tmp = fcBanlanceSheetVOS.get(j);
                    fcBanlanceSheetVOS.set(j, fcBanlanceSheetVOS.get(j + 1));
                    fcBanlanceSheetVOS.set(j + 1, Tmp);
                }
            }
        }
    }
    /**
     * <b>功能介绍：</b>获取国际化<br>
     * <b>日期：</b>2018年7月9日<br>
     * <b>作者：</b>zhoup<br>
     *
     * @param str
     * @return
     * @version 1.0
     */
    public String getLangStr(String str) {
        return multiLangServiceI.getLang(str);
    }
    /**
     * 生成excel数据
     *
     * @param input
     * @param impType //1、损益 2、负债
     * @return
     * @throws Exception
     */
    public HSSFWorkbook doExportInterestBillXls(DataTablesInput input, String impType
    ) throws Exception {
//		List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        List<Map<String, String>> curs = null;
        Map<String, Object> params = input.getFormdata();
        String isConvert = null;
        String convertCurId = null;
        String rateType = null;
        Date preDate = new Date();
        BigDecimal exchangeRate = BigDecimal.ONE;
        BigDecimal lastChange = BigDecimal.ONE;
        input.setLength(-1);
        input.setStart(0);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = null;
        if (impType.equals("1")) {
            sheet = workbook.createSheet("损益表");
        } else {
            sheet = workbook.createSheet("资产负债表");
        }
        HSSFCellStyle cellStyle = this.initColumnHeadStyle(workbook);
        int index = 0;
        //创建 表头
        HSSFRow rowHead = sheet.createRow(index);
        rowHead.setHeightInPoints(20);
        HSSFCell cellHead = rowHead.createCell(0);
        //标题
        if (impType.equals("1")) {
            cellHead.setCellValue("损益表");
            cellHead.setCellStyle(cellStyle);
            for (int i = 1; i < 4; i++) {
                HSSFCell cell = rowHead.createCell(i);
                cell.setCellValue(new HSSFRichTextString());
                cell.setCellStyle(cellStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(index, index, (short) 0, (short) 3));
        } else {
            cellHead.setCellValue("资产负债表");
            cellHead.setCellStyle(cellStyle);
            for (int i = 1; i < 8; i++) {
                HSSFCell cell = rowHead.createCell(i);
                cell.setCellValue(new HSSFRichTextString());
                cell.setCellStyle(cellStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(index, index, (short) 0, (short) 7));
        }
        index++;
        HSSFCellStyle dataCellStyle = this.initColumnCenterstyle(workbook);
        DataTablesOutput output = null;
        List<FcIncomStatementVO> outList = null;
        List<FcBanlanceSheetVO> outListSheet = null;
        String checkDate = params.get("checkDate").toString();
        if (StringUtils.isNotEmpty(checkDate)) {
            checkDate = checkDate.replace("-", "年") + ("月");
        } else {
            checkDate = "";
        }
        if (null != params.get("isConvert") && StringUtils.isNotEmpty(params.get("isConvert").toString())) {
            isConvert = params.get("isConvert").toString();
        }
        if (null != params.get("preDate") && StringUtils.isNotEmpty(params.get("preDate").toString())) {
            preDate = DateUtils.parse(params.get("preDate").toString(), "yyyy-MM-dd");
        }
        if ("1".equals(isConvert)) {
            if (null != params.get("convertCurId") && StringUtils.isNotEmpty(params.get("convertCurId").toString())) {
                convertCurId = params.get("convertCurId").toString();
            }
            if (null != params.get("rateType") && StringUtils.isNotEmpty(params.get("rateType").toString())) {
                rateType = params.get("rateType").toString();
            }
        }
        if (impType.equals("1")) {
            outList = new ArrayList<>();
            if (null != params.get("EQs_curCode") && StringUtils.isNotEmpty(params.get("EQs_curCode").toString())) {
                output = fcIncomStatementDao.getFcIncomeStatement(input);
                this.fillDataFcIncom(output);
                outList.addAll(output.getData());
            } else {
                curs = fcIncomStatementDao.getFcIncomeStatementCurId(params.get("EQs_netCode").toString(), params.get("checkDate").toString());
                int i = 0;
                for (Map<String, String> cur : curs) {
                    String curCode = Convert.toStr(cur.get("CUR_CODE"),cur.get("cur_code"));
                    input.getFormdata().put("eqs_curCode", curCode);
                    output = fcIncomStatementDao.getFcIncomeStatement(input);
                    this.fillDataFcIncom(output);
                    outList.addAll(output.getData());
                }
                input.getFormdata().remove("eqs_curCode");
            }
            if (CommonConstant.YES.equals(isConvert)) {
                for (FcIncomStatementVO fcIncomStatementVO : outList) {
//				根据币种获取汇率
                    if (StringUtils.isNotEmpty(fcIncomStatementVO.getCurCode()) && StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(rateType)) {
                        preDate = DateUtils.setSpecifiedDateOfTime(preDate);
                        exchangeRate = excrateAdjustService.getExchangeRateBy(fcIncomStatementVO.getCurCode(), convertCurId, preDate, rateType);
                    }
                    fcIncomStatementVO.setBalance(fcIncomStatementVO.getBalance().multiply(exchangeRate));
                    fcIncomStatementVO.setYearTotal(fcIncomStatementVO.getYearTotal().multiply(exchangeRate));
                }
                if (null != params.get("EQs_curCode") && StringUtils.isNotEmpty(params.get("EQs_curCode").toString())) {
                    outList.addAll(getIncomeStatementTotaolMoney(input, preDate, 1).getData());
                } else {
                    outList.addAll(getIncomeStatementTotaolMoney(input, preDate, curs.size()).getData());
                }
            }
//			 outList=output.getData();
        } else {
            outListSheet = new ArrayList<>();
            if (null != params.get("EQs_curCode") && StringUtils.isNotEmpty(params.get("EQs_curCode").toString())) {
                output = fcIncomStatementDao.getFcBanlanceSheet(input);
                this.fillDatafcBalance(output);
                outListSheet.addAll(output.getData());
            } else {
                curs = fcIncomStatementDao.getFcBanlanceSheetCurId(params.get("EQs_netCode").toString(), params.get("checkDate").toString());
                for (Map<String, String> cur : curs) {
                    String curCode = Convert.toStr(cur.get("CUR_CODE"),cur.get("cur_code"));
                    input.getFormdata().put("eqs_curCode", curCode);
                    output = fcIncomStatementDao.getFcBanlanceSheet(input);
                    this.fillDatafcBalance(output);
                    outListSheet.addAll(output.getData());
                }
                input.getFormdata().remove("eqs_curCode");
            }
            if (CommonConstant.YES.equals(isConvert)) {
                for (FcBanlanceSheetVO fcBanlanceSheetVO : outListSheet) {
//				根据币种获取汇率
                    if (StringUtils.isNotEmpty(fcBanlanceSheetVO.getCurCode()) && StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(rateType)) {
                        preDate = DateUtils.setSpecifiedDateOfTime(preDate);
                        exchangeRate = excrateAdjustService.getExchangeRateBy(fcBanlanceSheetVO.getCurCode(), convertCurId, preDate, rateType);
//                        计算上年期末汇率
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(preDate);
                        int year = calendar.get(Calendar.YEAR);
                        lastChange = excrateAdjustService.getExchangeRateBy(fcBanlanceSheetVO.getCurCode(), convertCurId, DateUtils.parse((year-1)+"-12-31 23:59:59"), rateType);
                    }
//                    exchangeRate = new BigDecimal(3.8);
                    fcBanlanceSheetVO.setaYearBalance(fcBanlanceSheetVO.getaYearBalance().multiply(lastChange));
                    fcBanlanceSheetVO.setaMonthBalance(fcBanlanceSheetVO.getaMonthBalance().multiply(exchangeRate));
                    fcBanlanceSheetVO.setlYearBalance(fcBanlanceSheetVO.getlYearBalance().multiply(lastChange));
                    fcBanlanceSheetVO.setlMonthBalance(fcBanlanceSheetVO.getlMonthBalance().multiply(exchangeRate));
                }
                if (null != params.get("EQs_curCode") && StringUtils.isNotEmpty(params.get("EQs_curCode").toString())) {
                    outListSheet.addAll(getFcBanlanceSheetTotaolMoney(input, preDate, 1).getData());
                } else {
                    outListSheet.addAll(getFcBanlanceSheetTotaolMoney(input, preDate, curs.size()).getData());
                }
            }
//			 outListSheet=output.getData();
        }
        Map<String, Object> titleMap = new LinkedHashMap<String, Object>();
        if (impType.equals("1")) {
            titleMap.put("编制单位", outList.get(0).getNetName());
            titleMap.put("日期", checkDate);
            titleMap.put("币别", outList.get(0).getCurName());
        } else {
            titleMap.put("编制单位", outListSheet.get(0).getNetName());
            titleMap.put("日期", checkDate);
            titleMap.put("币别", outListSheet.get(0).getCurName());
        }
        index = this.writeTotalAndBisAccBal(sheet, dataCellStyle, index, impType, titleMap);
        if (impType.equals("1")) {
            index = this.writeAllocationDetailsData(workbook, sheet, cellStyle, index, impType, outList, checkDate);
        } else {
            index = this.writeAllocationDetailsDataOfSheet(workbook, sheet, cellStyle, index, impType, outListSheet, checkDate);//资产负债表数据
        }
//		this.writeTotal(sheet, dataCellStyle, index,impType ,totalMap);
        return workbook;
    }
    /**
     * 拆SQL，改为接口调用
     * @param output
     */
    private void fillDataFcIncom(DataTablesOutput output){
        //填充数据
        List<FcIncomStatementVO> fcIncomStatementVOS = new ArrayList<>();
        for (Object data : output.getData()) {
            FcIncomStatementVO fcIncomStatementVO = (FcIncomStatementVO) data;
            fcIncomStatementVOS.add(fcIncomStatementVO);
        }
        this.fillData2StatementVO(fcIncomStatementVOS);
        output.setData(fcIncomStatementVOS);
    }
    private void fillDatafcBalance(DataTablesOutput output){
        List<FcBanlanceSheetVO> fcBalanceSheetVOS = new ArrayList<>();
        for (Object data : output.getData()) {
            FcBanlanceSheetVO fcBanlanceSheetVO = (FcBanlanceSheetVO) data;
            fcBalanceSheetVOS.add(fcBanlanceSheetVO);
        }
        this.fillData2SheetVO(fcBalanceSheetVOS);
        // 汇总数据
        List<FcBanlanceSheetVO> listData = fillTotalStatisticBalance(fcBalanceSheetVOS);
        sortBanlanceSheetByOrderNum(listData);
        output.setData(listData);
    }

    public void sortBanlanceSheetByOrderNum(List<FcBanlanceSheetVO> fcIncomStatementVOS) {
        FcBanlanceSheetVO Tmp = null;
        for (int i = 0; i < fcIncomStatementVOS.size(); i++) {
            for (int j = 0; j < fcIncomStatementVOS.size() - i - 1; j++) {
                Integer preOrderNo = Convert.toInt(fcIncomStatementVOS.get(j).getaOrderNo(),fcIncomStatementVOS.get(j).getlOrderNo());
                Integer nextOrderNo = Convert.toInt(fcIncomStatementVOS.get(j + 1).getaOrderNo(),fcIncomStatementVOS.get(j + 1).getlOrderNo());
                if (preOrderNo > nextOrderNo) {
                    Tmp = fcIncomStatementVOS.get(j);
                    fcIncomStatementVOS.set(j, fcIncomStatementVOS.get(j + 1));
                    fcIncomStatementVOS.set(j + 1, Tmp);
                }
            }
        }
    }

    private List<FcBanlanceSheetVO> fillTotalStatisticBalance(List<FcBanlanceSheetVO> balanceSheetList) {
        List<FcBanlanceSheetVO> listData = new ArrayList<>();
        // 获取右侧数据
        Map<String, List<FcBanlanceSheetVO>> rightListMap = balanceSheetList.stream().filter(item -> ObjectUtil.isNotEmpty(item.getaItem()))
                .collect(Collectors.groupingBy(FcBanlanceSheetVO::getaItem));
        // 获取左侧数据
        Map<String, List<FcBanlanceSheetVO>> leftListMap = balanceSheetList.stream().filter(item -> ObjectUtil.isNotEmpty(item.getlItem()))
                .collect(Collectors.groupingBy(FcBanlanceSheetVO::getlItem));
        fillTotalData(listData, rightListMap);
        fillTotalData(listData, leftListMap);
        return listData;
    }

    private static void fillTotalData(List<FcBanlanceSheetVO> listData, Map<String, List<FcBanlanceSheetVO>> listMap) {
        listMap.forEach((key, list) -> {
            FcBanlanceSheetVO data = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                FcBanlanceSheetVO balanceSheet = list.get(i);
                data.setaYearBalance(data.getaYearBalance().add(balanceSheet.getaYearBalance()));
                data.setaMonthBalance(data.getaMonthBalance().add(balanceSheet.getaMonthBalance()));
                data.setlYearBalance(data.getlYearBalance().add(balanceSheet.getlYearBalance()));
                data.setlMonthBalance(data.getlMonthBalance().add(balanceSheet.getlMonthBalance()));
            }
            listData.add(data);
        });
    }

    /**
     * <br>
     * <b>功能：</b>列头的样式<br>
     * <b>作者：</b>zhoup<br>
     * <b>日期：</b> 2018-7-9 <br>
     *
     * @param wb
     * @return
     */
    public static HSSFCellStyle initColumnHeadStyle(HSSFWorkbook wb) {
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
     * <br>
     * <b>功能：</b>单元格的左边对齐样式<br>
     * <b>作者：</b>zhoup<br>
     * <b>日期：</b> 2018-7-9 <br>
     *
     * @param wb
     * @return
     */
    public static HSSFCellStyle initColumnLeftAlignStyle(HSSFWorkbook wb) {
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);
        HSSFCellStyle centerstyle = wb.createCellStyle();
        centerstyle.setFont(font);
        centerstyle.setAlignment(HorizontalAlignment.LEFT);// 左右居中
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
     * <br>
     * <b>功能：</b>单元格的右边对齐样式<br>
     * <b>作者：</b>zhoup<br>
     * <b>日期：</b> 2018-7-9 <br>
     *
     * @param wb
     * @return
     */
    public static HSSFCellStyle initColumnRightAlignStyle(HSSFWorkbook wb) {
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);
        HSSFCellStyle centerstyle = wb.createCellStyle();
        centerstyle.setFont(font);
        centerstyle.setAlignment(HorizontalAlignment.RIGHT);// 右边对齐
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
     * <br>
     * <b>功能：</b>单元格的默认样式<br>
     * <b>作者：</b>zhoup<br>
     * <b>日期：</b> 2018-7-9 <br>
     *
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
     * <br>
     * <b>功能：</b>第一列表头<br>
     * <b>作者：</b>zhoup<br>
     * <b>日期：</b> 2018-7-9 <br>
     *
     * @param sheet
     * @param cellStyle
     * @param index
     * @param
     */
    private int writeTotalAndBisAccBal(HSSFSheet sheet, HSSFCellStyle cellStyle, int index, String impType,
                                       Map<String, Object> titleMap) {
        Iterator<Map.Entry<String, Object>> iter = titleMap.entrySet().iterator();
        HSSFRow rowBal = null;
        while (iter.hasNext()) {
            rowBal = sheet.createRow(index);
            rowBal.setHeightInPoints(30);
            Map.Entry<String, Object> et0 = iter.next();
            HSSFCell cell0 = rowBal.createCell(0);
            cell0.setCellValue(et0.getKey() + ": " + et0.getValue());
            cell0.setCellStyle(cellStyle);
            HSSFCell cell1 = rowBal.createCell(1);
            cell1.setCellStyle(cellStyle);
            if (impType.equals("1")) {
                if (iter.hasNext()) {
                    Map.Entry<String, Object> et1 = iter.next();
                    HSSFCell cell2 = rowBal.createCell(2);
                    cell2.setCellValue(et1.getKey() + ": " + et1.getValue());
                    cell2.setCellStyle(cellStyle);
                }
                if (iter.hasNext()) {
                    Map.Entry<String, Object> et2 = iter.next();
                    HSSFCell cell3 = rowBal.createCell(3);
                    cell3.setCellValue(et2.getKey() + ": " + et2.getValue());
                    cell3.setCellStyle(cellStyle);
                }
            } else {
                HSSFCell cell2 = rowBal.createCell(2);
                cell2.setCellStyle(cellStyle);
//					sheet.addMergedRegion(new CellRangeAddress(index, index, (short)0, 2));合并单元格
                if (iter.hasNext()) {
                    Map.Entry<String, Object> et1 = iter.next();
                    HSSFCell cell3 = rowBal.createCell(3);
                    cell3.setCellValue(et1.getKey() + ": " + et1.getValue());
                    cell3.setCellStyle(cellStyle);
                    HSSFCell cell4 = rowBal.createCell(4);
                    cell4.setCellStyle(cellStyle);
                    HSSFCell cell5 = rowBal.createCell(5);
                    cell5.setCellStyle(cellStyle);
                    HSSFCell cell6 = rowBal.createCell(6);
                    cell6.setCellStyle(cellStyle);
                }
                if (iter.hasNext()) {
                    Map.Entry<String, Object> et2 = iter.next();
                    HSSFCell cell7 = rowBal.createCell(7);
                    cell7.setCellValue(et2.getKey() + ": " + et2.getValue());
                    cell7.setCellStyle(cellStyle);
                }
            }
        }
        return index + 1;
    }
    /**
     * <br>
     * <b>功能：</b>合计<br>
     * <b>作者：</b>zhoup<br>
     * <b>日期：</b> 2018-7-9 <br>
     * @param sheet
     * @param cellStyle
     * @param index
     * @param bals
     */
//	private int writeTotal(HSSFSheet sheet,HSSFCellStyle cellStyle,int index,String impType,
//									   Map<String, Object> totalMap){
//		Iterator<Map.Entry<String,Object>> iter = totalMap.entrySet().iterator();
//		HSSFRow rowBal = null;
//		while(iter.hasNext()){
//			rowBal = sheet.createRow(index);
//			rowBal.setHeightInPoints(20);
////			Map.Entry<String,Object> et0 = iter.next();
//				HSSFCell cell0 = rowBal.createCell(0);
//				cell0.setCellValue("合计");
//				cell0.setCellStyle(cellStyle);
//				HSSFCell cell1 = rowBal.createCell(1);
//				cell1.setCellStyle(cellStyle);
//			
//			sheet.addMergedRegion(new CellRangeAddress(index, index, (short)0, 1));
//			
//			if(iter.hasNext()){
//				Map.Entry<String,Object> et0 = iter.next();
//				HSSFCell cell2 = rowBal.createCell(2);
//				cell2.setCellValue(NumberUtil.formatNumeric(((BigDecimal) et0.getValue()).doubleValue()));
//				cell2.setCellStyle(cellStyle);
//				
//			}
//			if(iter.hasNext()){
//				Map.Entry<String,Object> et1 = iter.next();
//				HSSFCell cell3 = rowBal.createCell(3);
//				cell3.setCellValue(NumberUtil.formatNumeric(((BigDecimal) et1.getValue()).doubleValue()));
//				cell3.setCellStyle(cellStyle);
//			}
//			if(iter.hasNext()){
//				HSSFCell cell4= rowBal.createCell(4);
//				cell4.setCellValue("");
//				cell4.setCellStyle(cellStyle);
//				HSSFCell cell5= rowBal.createCell(5);
//				cell5.setCellValue("");
//				cell5.setCellStyle(cellStyle);
//				Map.Entry<String,Object> et2 = iter.next();
//				HSSFCell cell6 = rowBal.createCell(6);
//				cell6.setCellValue(NumberUtil.formatNumeric(((BigDecimal) et2.getValue()).doubleValue()));
//				cell6.setCellStyle(cellStyle);
//			}
//			if(iter.hasNext()){
//				Map.Entry<String,Object> et3 = iter.next();
//				HSSFCell cell7 = rowBal.createCell(7);
//				cell7.setCellValue(NumberUtil.formatNumeric(((BigDecimal) et3.getValue()).doubleValue()));
//				cell7.setCellStyle(cellStyle);
//			}
//		}
//		
//		return  index;
//	}
    /**
     * <br>
     * <b>功能：</b>损益表数据<br>
     * <b>作者：</b>zhoup<br>
     * <b>日期：</b> 2018-7-9 <br>
     *
     * @param workbook
     * @param sheet
     * @param cellStyle
     * @param index
     * @param
     */
    private int writeAllocationDetailsData(HSSFWorkbook workbook, HSSFSheet sheet, HSSFCellStyle cellStyle, int index, String impType,
                                           List<FcIncomStatementVO> outList, String checkDate) {
        HSSFCellStyle centerCellStyle = this.initColumnCenterstyle(workbook);
        // created by chenlw on 2020-11-23
        HSSFCellStyle leftCellStyle = initColumnLeftAlignStyle(workbook);
        HSSFCellStyle rightCellStyle = initColumnRightAlignStyle(workbook);
        index = this.writeAllocationDetailsHead(sheet, centerCellStyle, index++, impType);
        FcIncomStatementVO tmpBean = new FcIncomStatementVO();
        boolean flag = true;
        for (FcIncomStatementVO bean : outList) {
            HSSFRow row = sheet.createRow(index++);
            if ((StringUtils.isNotEmpty(tmpBean.getCurName()) && !tmpBean.getCurName().equals(bean.getCurName())) ||
                    (bean.isTotalFlag() && flag)) {
                row.setHeightInPoints(20);
                sheet.setColumnWidth(0, 256 * 35);
                HSSFCell cell = row.createCell(0);
                cell.setCellStyle(centerCellStyle);
                cell.setCellValue("编制网点:" + bean.getNetName());
                cell = row.createCell(1);
                cell.setCellStyle(centerCellStyle);
                cell.setCellValue("");
                cell = row.createCell(2);
                cell.setCellStyle(centerCellStyle);
                cell.setCellValue("日期:" + checkDate);
                cell = row.createCell(3);
                cell.setCellStyle(centerCellStyle);
                if (StringUtils.isNotEmpty(bean.getCurName())) {
                    cell.setCellValue("币别:" + bean.getCurName());
                } else {
                    cell.setCellValue("币别:");
                }
                row = sheet.createRow(index++);
                if (bean.isTotalFlag()) {
                    flag = false;
                }
                tmpBean = bean;
            }
            row.setHeightInPoints(20);
            for (int i = 0; i < 4; i++) {
                HSSFCell cell = row.createCell(i);
                switch (i) {
                    case 0:
                        sheet.setColumnWidth(i, 256 * 35);
                        cell.setCellValue(bean.getItem());
                        cell.setCellStyle(leftCellStyle);
                        break;
                    case 1:
                        if (StringUtils.isNotEmpty(bean.getOrderNo())) {
                            cell.setCellValue(bean.getOrderNo());
                        } else {
                            cell.setCellValue("");
                        }
                        cell.setCellStyle(centerCellStyle);
                        break;
                    case 2:
                        if (StringUtils.isNotEmpty(bean.getBalance())) {
                            cell.setCellValue(NumberUtil.formatNumeric(bean.getBalance().doubleValue()));
                        } else {
                            cell.setCellValue(NumberUtil.formatNumeric(BigDecimal.ZERO.doubleValue()));
                        }
                        // 金额右边对齐
                        cell.setCellStyle(rightCellStyle);
                        break;
                    case 3:
                        if (StringUtils.isNotEmpty(bean.getBalance())) {
                            cell.setCellValue(NumberUtil.formatNumeric(bean.getYearTotal().doubleValue()));
                        } else {
                            cell.setCellValue(NumberUtil.formatNumeric(BigDecimal.ZERO.doubleValue()));
                        }
                        // 金额右边对齐
                        cell.setCellStyle(rightCellStyle);
                        break;
                    default:
                        break;
                }
            }
            tmpBean = bean;
        }
        HSSFRow row = sheet.createRow(index++);
        row.setHeightInPoints(20);
        HSSFCell cell = row.createCell(0);
        cell.setCellStyle(leftCellStyle);
        cell.setCellValue("会计主管:");
        cell = row.createCell(1);
        cell.setCellStyle(leftCellStyle);
        cell.setCellValue("复核员:");
        cell = row.createCell(2);
        cell.setCellStyle(leftCellStyle);
        cell.setCellValue("制表人:");
        for (int i = 3; i < 4; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(leftCellStyle);
            cell.setCellValue("");
        }
//			HSSFRow rowBlank = sheet.createRow(index);
//			rowBlank.setHeightInPoints(20);
//			for(int i = 0; i< 4; i ++){
//				HSSFCell cell = rowBlank.createCell(i);
//				cell.setCellValue(new HSSFRichTextString());
//				cell.setCellStyle(cellStyle);
//			}
//		
        return index;
    }
    /**
     * <br>
     * <b>功能：</b>资产负债表数据<br>
     * <b>作者：</b>zhoup<br>
     * <b>日期：</b> 2018-7-9 <br>
     *
     * @param workbook
     * @param sheet
     * @param cellStyle
     * @param index
     * @param
     */
    private int writeAllocationDetailsDataOfSheet(HSSFWorkbook workbook, HSSFSheet sheet, HSSFCellStyle cellStyle, int index, String impType,
                                                  List<FcBanlanceSheetVO> outLisSheet, String checkDate) {
        HSSFCellStyle centerCellStyle = this.initColumnCenterstyle(workbook);
        // created by chenlw on 2020-11-23
        HSSFCellStyle leftCellStyle = initColumnLeftAlignStyle(workbook);
        HSSFCellStyle rightCellStyle = initColumnRightAlignStyle(workbook);
        index = this.writeAllocationDetailsHead(sheet, centerCellStyle, index++, impType);
        FcBanlanceSheetVO tmpBean = new FcBanlanceSheetVO();
        boolean flag = true;
        for (FcBanlanceSheetVO bean : outLisSheet) {
            HSSFRow row = sheet.createRow(index++);
            if ((StringUtils.isNotEmpty(tmpBean.getCurName()) && !tmpBean.getCurName().equals(bean.getCurName())) ||
                    (bean.isTotalFlag() && flag)) {
                row.setHeightInPoints(20);
                sheet.setColumnWidth(0, 256 * 35);
                HSSFCell cell = row.createCell(0);
                cell.setCellStyle(centerCellStyle);
                cell.setCellValue("编制网点:" + bean.getNetName());
                cell = row.createCell(1);
                cell.setCellStyle(centerCellStyle);
                cell.setCellValue("");
                cell = row.createCell(2);
                cell.setCellStyle(centerCellStyle);
                cell.setCellValue("");
                cell = row.createCell(3);
                cell.setCellStyle(centerCellStyle);
                cell.setCellValue("日期:" + checkDate);
                cell = row.createCell(4);
                cell.setCellStyle(centerCellStyle);
                cell.setCellValue("");
                cell = row.createCell(5);
                cell.setCellStyle(centerCellStyle);
                cell.setCellValue("");
                cell = row.createCell(6);
                cell.setCellStyle(centerCellStyle);
                cell.setCellValue("");
                cell = row.createCell(7);
                cell.setCellStyle(centerCellStyle);
                if (StringUtils.isNotEmpty(bean.getCurName())) {
                    cell.setCellValue("币别:" + bean.getCurName());
                } else {
                    cell.setCellValue("币别:");
                }
                row = sheet.createRow(index++);
                if (bean.isTotalFlag()) {
                    flag = false;
                }
                tmpBean = bean;
            }
            row.setHeightInPoints(20);
            for (int i = 0; i < 8; i++) {
                HSSFCell cell = row.createCell(i);
                switch (i) {
                    case 0:
                        sheet.setColumnWidth(i, 256 * 35);
                        cell.setCellValue(bean.getaItem());
                        // 资产  第一列  左对齐，使其可以层次显示
                        cell.setCellStyle(leftCellStyle);
                        break;
                    case 1:
                        if (StringUtils.isNotEmpty(bean.getaOrderNo())) {
                            cell.setCellValue(bean.getaOrderNo());
                        } else {
                            cell.setCellValue("");
                        }
                        // 居中对齐
                        cell.setCellStyle(centerCellStyle);
                        break;
                    case 2:
                        if (StringUtils.isNotEmpty(bean.getaYearBalance())) {
                            cell.setCellValue(NumberUtil.formatNumeric(bean.getaYearBalance().doubleValue()));
                        } else {
                            cell.setCellValue(NumberUtil.formatNumeric(BigDecimal.ZERO.doubleValue()));
                        }
                        // 金额右边对齐
                        cell.setCellStyle(rightCellStyle);
                        break;
                    case 3:
                        if (StringUtils.isNotEmpty(bean.getaMonthBalance())) {
                            cell.setCellValue(NumberUtil.formatNumeric(bean.getaMonthBalance().doubleValue()));
                        } else {
                            cell.setCellValue(NumberUtil.formatNumeric(BigDecimal.ZERO.doubleValue()));
                        }
                        // 金额右边对齐
                        cell.setCellStyle(rightCellStyle);
                        break;
                    case 4:
                        // 负债及所有者权益
                        sheet.setColumnWidth(i, 256 * 35);
                        cell.setCellValue(bean.getlItem());
                        // 左边对齐  层次显示
                        cell.setCellStyle(leftCellStyle);
                        break;
                    case 5:
                        if (bean.getlOrderNo() != null) {
                            cell.setCellValue(bean.getlOrderNo());
                        }
                        // 居中对齐
                        cell.setCellStyle(centerCellStyle);
                        break;
                    case 6:
                        if (StringUtils.isNotEmpty(bean.getlYearBalance())) {
                            cell.setCellValue(NumberUtil.formatNumeric(bean.getlYearBalance().doubleValue()));
                        } else {
                            cell.setCellValue(NumberUtil.formatNumeric(BigDecimal.ZERO.doubleValue()));
                        }
                        // 金额右边对齐
                        cell.setCellStyle(rightCellStyle);
                        break;
                    case 7:
                        if (StringUtils.isNotEmpty(bean.getlMonthBalance())) {
                            cell.setCellValue(NumberUtil.formatNumeric(bean.getlMonthBalance().doubleValue()));
                        } else {
                            cell.setCellValue(NumberUtil.formatNumeric(BigDecimal.ZERO.doubleValue()));
                        }
                        // 金额右边对齐
                        cell.setCellStyle(rightCellStyle);
                        break;
                    default:
                        break;
                }
            }
            tmpBean = bean;
        }
        HSSFRow row = sheet.createRow(index++);
        row.setHeightInPoints(20);
        HSSFCell cell = row.createCell(0);
        cell.setCellStyle(leftCellStyle);
        cell.setCellValue("会计主管:");
        cell = row.createCell(1);
        cell.setCellStyle(leftCellStyle);
        cell.setCellValue("复核员:");
        cell = row.createCell(2);
        cell.setCellStyle(leftCellStyle);
        cell.setCellValue("制表人:");
        for (int i = 3; i < 8; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(leftCellStyle);
            cell.setCellValue("");
        }
//			HSSFRow rowBlank = sheet.createRow(index);//创建多少行
//			rowBlank.setHeightInPoints(20);
//			for(int i = 0; i< 8; i ++){
//				HSSFCell cell = rowBlank.createCell(i);
//				cell.setCellValue(new HSSFRichTextString());
//				cell.setCellStyle(cellStyle);
//			}
        return index;
    }
    /**
     * <br>
     * <b>功能：</b>表头<br>
     * <b>作者：</b>zhoup<br>
     * <b>日期：</b> 2018-7-9 <br>
     *
     * @param sheet
     * @param cellStyle
     * @param index
     * @param
     */
    private int writeAllocationDetailsHead(HSSFSheet sheet, HSSFCellStyle cellStyle, int index, String impType) {
        String[] heads = null;
        if (impType.equals("1")) {
            /*String[] heads = new String[]{"项目", "行次" , "本期数", "本年累计数"};*/
            heads = new String[]{getLangStr("table.fcReportFormat.item"),
                    getLangStr("table.fcReportFormat.orderNo"),
                    getLangStr("table.fcIncomStatement.balance"),
                    getLangStr("table.fcIncomStatement.yearTotal")};
        } else {
            heads = new String[]{getLangStr("table.fcBalanceSheet.aItem"),
                    getLangStr("table.fcReportFormat.orderNo"),
                    getLangStr("table.fcBalanceSheet.yearBalance"),
                    getLangStr("table.fcBalanceSheet.monthBalance"),
                    getLangStr("table.fcBalanceSheet.lItem"),
                    getLangStr("table.fcReportFormat.orderNo"),
                    getLangStr("table.fcBalanceSheet.yearBalance"),
                    getLangStr("table.fcBalanceSheet.monthBalance")
            };
        }
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
     * 资产负债/损益币种汇率折算检查
     *
     * @param orig
     * @param convertCurId
     * @param preDate
     * @param rateType
     */
    public void checkConvertCurId(String orig, String convertCurId, Date preDate, String rateType, String banlanceSheet) {
        preDate = DateUtils.setSpecifiedDateOfTime(preDate);
//        上年期末折算日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(preDate);
        Date lastPreDate = DateUtils.parse((calendar.get(Calendar.YEAR) - 1) + "-12-31 23:59:59");
        if (StringUtils.isNotEmpty(orig)) {
            /**
             * 资产负债表年初数折算时校验
             */
            if (CommonConstant.YES.equals(banlanceSheet)) {
                excrateAdjustService.getExchangeRateBy(orig, convertCurId, lastPreDate, rateType);
            }
            excrateAdjustService.getExchangeRateBy(orig, convertCurId, preDate, rateType);
        } else {
            List<BtCurrencyDTO> btCurrencyDTOS = btCurrencyService.getBtCurrency();
            for (BtCurrencyDTO cur : btCurrencyDTOS) {
                excrateAdjustService.getExchangeRateBy(cur.getId(), convertCurId, preDate, rateType);
            }
        }
    }
}
