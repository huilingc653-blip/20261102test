package com.bytter.biz.foreign.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.vo.BtBankAccBalVO;
import com.bytter.biz.foreign.dao.FexCorpPoolBankaccBalDao;
import com.bytter.biz.foreign.vo.FexCorpPoolBankAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Service
 * @Description: 资金池账户余额查询
 * @date 2020-02-25 09:31:56
 */
@Service("fexCorpPoolBankaccBalService")
@Transactional
public class FexCorpPoolBankaccBalService extends BaseService<FexCorpPoolBankAccVO, String> {

    @Autowired
    private FexCorpPoolBankaccBalDao fexCorpPoolBankaccBalDao;
    @Autowired(required = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired(required = false)
    private ICorpService corpService;
    @Autowired(required = false)
    private IDictService dictService;
    @Autowired(required = false)
    private IFcExcrateAdjustService fcExcrateAdjustService;
    @DubboReference(check = false)
    private IFcCorpPoolService fcCorpPoolService;
    @DubboReference(check = false)
    private IBisAccBalService iBisAccBalService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;

    @Override
    public BaseDao<FexCorpPoolBankAccVO, String> getEntityDao() {
        return fexCorpPoolBankaccBalDao;
    }

    /**
     * 根据条件查询资金池账户信息
     *
     * @param fexCorpPoolBankAccVO
     * @return
     */
    public List<FexCorpPoolBankAccVO> getBalList(FexCorpPoolBankAccVO fexCorpPoolBankAccVO) {
        return fexCorpPoolBankaccBalDao.getBalList(fexCorpPoolBankAccVO);
    }

    /**
     * 组装树结构 资金池
     *
     * @param fexCorpPoolBankAccVO
     */
    public List<FexCorpPoolBankAccVO> setTreeForPool(FexCorpPoolBankAccVO fexCorpPoolBankAccVO) {
        FexCorpPoolBankAccVO searchVO = new FexCorpPoolBankAccVO();
        searchVO.setPoolId(fexCorpPoolBankAccVO.getPoolId());
        searchVO.setCurId(fexCorpPoolBankAccVO.getCurId());
        searchVO.setBalDate(fexCorpPoolBankAccVO.getBalDate());
        searchVO.setParentSign(ForeignConstant.FEX_PARENT_SIGN_1);
        //根据条件查询出主账号信息
        List<FexCorpPoolBankAccVO> voList = this.getBalList(searchVO);
        //小计和
        Map<String, BigDecimal> subtotalMap = new HashMap<>();
        //小计
        BigDecimal subtotal = BigDecimal.ZERO;
        List<Map<String, BigDecimal>> subCurList = new ArrayList<>();
        for (FexCorpPoolBankAccVO parentVO : voList) {
            //同资金池同币别相加
            if (subtotalMap.containsKey(parentVO.getId() + "," + parentVO.getCurId())) {
                //相同就相加，相加完重新放进Map中
                subtotal = subtotal.add(subtotalMap.get(parentVO.getId() + "," + parentVO.getCurId()));
                subtotalMap.put(parentVO.getId() + "," + parentVO.getCurId(), subtotal);
            } else {
                //不存在放入
                subtotalMap.put(parentVO.getId() + "," + parentVO.getCurId(), parentVO.getBal());
            }
            searchVO.setPoolId("");
            searchVO.setParentId(parentVO.getId());
            searchVO.setParentSign(ForeignConstant.FEX_PARENT_SIGN_0);
            //根据资金池主体id(或者主账号id)查询主体账号信息
            List<FexCorpPoolBankAccVO> accList = fexCorpPoolBankaccBalDao.getBalList(searchVO);
            if (!EmptyUtils.isEmptyList(accList)) {
                //主体账号
                parentVO.setAccList(accList);
                for (FexCorpPoolBankAccVO childVO : accList) {
                    //遍历主体账号，父id和币别id，如果存在，说明和主账号同资金池同币别
                    if (subtotalMap.containsKey(childVO.getParentId() + "," + childVO.getCurId())) {
                        //相同就相加，相加完重新放进Map中
                        subtotal = childVO.getBal().add(subtotalMap.get(childVO.getParentId() + "," + childVO.getCurId()));
                        subtotalMap.put(childVO.getParentId() + "," + childVO.getCurId(), subtotal);
                    } else {
                        subtotalMap.put(childVO.getParentId() + "," + childVO.getCurId(), childVO.getBal());
                    }
                }
            }
        }
        subCurList.add(subtotalMap);
        for (FexCorpPoolBankAccVO showVO : voList) {
            showVO.setSubCurList(subCurList);
        }
        return voList;
    }

    /**
     * 返回折合未维护折币种信息
     */
    public Map<String, String> mergeCurInfo(List<Map<String, BigDecimal>> subList,String mergeCurId, String typeId, Date balDate) {
        //如果传合并计算币种，则根据汇率货币对公式得到汇率
        Map<String,String> curInfo = null;
        if (StringUtils.isNotBlank(mergeCurId) && balDate !=null && StringUtils.isNotBlank(typeId)) {
            curInfo = new HashMap<>();
            for (Map<String, BigDecimal> subtotalMap : subList) {
                for (Map.Entry<String, BigDecimal> entry : subtotalMap.entrySet()) {
                    String[] curArr = entry.getKey().split(",");
                    if(!EmptyUtils.isEmpty(curArr)){
                        String curId = curArr[1];
                        BigDecimal exchangeRate = fcExcrateAdjustService.getExchangeRateByNoCheck(curId, mergeCurId, balDate,typeId);
                        if(EmptyUtils.isEmpty(exchangeRate)){
                            String englishCur = btCurrencyService.getObjectById(curId).getEnglishCode();
                            String mergeEnglishCur = btCurrencyService.getObjectById(mergeCurId).getEnglishCode();
                            if(!curInfo.containsKey(englishCur)){
                                curInfo.put(englishCur,mergeEnglishCur);
                            }
                        }
                    }

                }
            }
        }
        return curInfo;
    }


    /**
     * 合计折合本位币换算
     */
    public Map<String, BigDecimal> toCurrMoneyTotal(List<Map<String, BigDecimal>> subList,String mergeCurId, String typeId, Date balDate) {
        BigDecimal total = BigDecimal.ZERO;
        //如果传合并计算币种，则根据汇率货币对公式得到汇率
        if (StringUtils.isNotBlank(mergeCurId) && balDate !=null && StringUtils.isNotBlank(typeId)) {
            Map<String, BigDecimal> totalMap = new HashMap<>();
            BigDecimal exchangeRate = null;
            for (Map<String, BigDecimal> subtotalMap : subList) {
                for (Map.Entry<String, BigDecimal> entry : subtotalMap.entrySet()) {
                    String[] curArr = entry.getKey().split(",");
                    if(!EmptyUtils.isEmpty(curArr)){
                        String curId = curArr[1];
                        exchangeRate = fcExcrateAdjustService.getExchangeRateByNoCheck(curId, mergeCurId, balDate,typeId);
                        if(!EmptyUtils.isEmpty(exchangeRate)){
                            total = total.add(entry.getValue().multiply(exchangeRate));
                        }else{
                            return null;
                        }
                    }

                }
            }
            totalMap.put(btCurrencyService.getObjectById(mergeCurId).getEnglishCode(), total);
            return totalMap;
        }
        return null;
    }

    /**
     * 小计折合本位币换算
     */
    public List<Map<String, BigDecimal>> toCurrMoneySub(List<Map<String, BigDecimal>> subList, String curIdTemp) {
        //默认折合本位币换算
        BigDecimal sub = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        List<Map<String, BigDecimal>> showList = new ArrayList<>();
        BigDecimal curExrate = new BigDecimal(1);
        Map<String, BigDecimal> curSubtotalMap = new HashMap<>();
        BtCurrencyDTO natualCurrency = btCurrencyService.getNatualCurrency();//获取本位币

        for (Map<String, BigDecimal> subtotalMap : subList) {
            for (Map.Entry<String, BigDecimal> entry : subtotalMap.entrySet()) {
                String[] keyId = entry.getKey().split(",");
                if (!natualCurrency.getId().equals(keyId[1])) {
                    BtCurrencyDTO objectById = btCurrencyService.getObjectById(keyId[1]);
                    curExrate = objectById.getCurExrate();
                }
                curSubtotalMap.put(keyId[0] + "," + curIdTemp, entry.getValue().multiply(curExrate));
                showList.add(curSubtotalMap);
            }
        }
        return showList;
    }

    /**
     * 资金池单位转换
     *
     * @param fexCorpPoolBankAccVOS
     * @return
     */
    private List<FexCorpPoolBankAccVO> convertAccDTO(List<FexCorpPoolBankAccVO> fexCorpPoolBankAccVOS) {
        List<FexCorpPoolBankAccVO> list = new ArrayList<>();
        for (FexCorpPoolBankAccVO fexCorpPoolBankAccVO : fexCorpPoolBankAccVOS) {
            FexCorpPoolBankAccVO tree = new FexCorpPoolBankAccVO();
            BeanUtils.copyNotNullProperties(fexCorpPoolBankAccVO, tree);
            list.add(tree);
        }
        return list;
    }

    /**
     * 创建表头样式
     *
     * @param workbook
     * @return
     */
    private HSSFCellStyle createExcelHeaderStyle(HSSFWorkbook workbook) {
        HSSFCellStyle columnHeadStyle = workbook.createCellStyle();
        HSSFFont columnHeadFont = workbook.createFont();
        columnHeadFont.setFontName("宋体");
        columnHeadFont.setFontHeightInPoints((short) 10);
        columnHeadFont.setBold(true);
        columnHeadFont.setFontHeightInPoints((short) 14); //字体大小
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
     * 创建Excel内容样式
     *
     * @param workbook
     * @return
     */
    private HSSFCellStyle createExcelBodyStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cs = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体"); //字体
        font.setFontHeightInPoints((short) 12);
        cs.setFont(font);      //列的字体
        cs.setVerticalAlignment(VerticalAlignment.CENTER);  //垂直居中对齐
        cs.setAlignment(HorizontalAlignment.CENTER);        //水平居中对齐
        cs.setWrapText(true);
        cs.setLeftBorderColor(HSSFColor.BLACK.index);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setRightBorderColor(HSSFColor.BLACK.index);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderBottom(BorderStyle.THIN); // 设置单元格的边框为粗体
        cs.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
        cs.setFillForegroundColor(HSSFColor.WHITE.index);// 设置单元格的背景颜色．
        return cs;
    }

    /**
     * 生成Excel表的内容
     *
     * @param fexCorpPoolBankAccVO
     * @param resource
     * @param userId
     * @return
     */
    public HSSFWorkbook createExcelContent(FexCorpPoolBankAccVO fexCorpPoolBankAccVO, String resource, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        List<FexCorpPoolBankAccVO> vos = this.setTreeForPool(fexCorpPoolBankAccVO);
        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<Map<String, Object>> mapList = null;
        String content = "资金池账户余额查询";

        // 创建sheet
        HSSFSheet sheet = workbook.createSheet(content);
        sheet.createRow(0);
        // 创建Excel表头
        this.createExcelHeader(workbook, sheet);
        // 创建Excel表中的内容
        this.createExcelBody(workbook, sheet, vos, fexCorpPoolBankAccVO);
        return workbook;
    }

    /**
     * 创建表头
     *
     * @param workbook
     * @param sheet
     */
    public void createExcelHeader(HSSFWorkbook workbook, HSSFSheet sheet) {
        // 第一行
        HSSFRow row1 = sheet.getRow(0);
        // 第一行
        row1.createCell(0).setCellValue("资金池");
        row1.createCell(1).setCellValue("单位");
        row1.createCell(2).setCellValue("账户");
        row1.createCell(3).setCellValue("币种");
        row1.createCell(4).setCellValue("资金池性质");
        row1.createCell(5).setCellValue("账户资金性质");
        row1.createCell(6).setCellValue("日期");
        row1.createCell(7).setCellValue("余额");
        // 设置Excel表头样式
        for (int i = 0; i < 8; i++) {
            sheet.setColumnWidth(i, 25 * 256);
            HSSFCell row1cell = row1.getCell(i);
            if (row1cell != null) {
                row1cell.setCellStyle(this.createExcelHeaderStyle(workbook));
            }
        }
    }

    /**
     * 创建Excel表内容
     *
     * @param workbook
     * @param sheet
     * @param voList
     */
    private void createExcelBody(HSSFWorkbook workbook, HSSFSheet sheet, List<FexCorpPoolBankAccVO> voList, FexCorpPoolBankAccVO fexCorpPoolBankAccVO) {
        // 指向所在行
        int index = 1;
        //for(FexCorpPoolBankAccVO vo : voList){
        for (int i = 0; i < voList.size(); i++) {
            // 创建行
            HSSFRow row = sheet.createRow(index);
            //HSSFRow row = sheet.getRow(index);
            //设置主账号信息
            row = setRowData(row, voList.get(i), null, true);
            // 设置Excel表头样式
            setRowCellStyle(workbook, row);

            if (!EmptyUtils.isEmptyList(voList.get(i).getAccList())) {
                // 创建行
                index = index + voList.get(i).getAccList().size();
                List<FexCorpPoolBankAccVO> childList = voList.get(i).getAccList();
                for (int j = 0; j < childList.size(); j++) {
                    // 创建行
                    row = sheet.createRow(index);
                    //设置成员账号信息
                    row = setRowData(row, voList.get(i), childList.get(j), false);
                    // 设置Excel表头样式
                    setRowCellStyle(workbook, row);

                    //主体账号存在成员账号
                    if (j == childList.size() - 1) {
                        for (Map<String, BigDecimal> subMap : voList.get(i).getSubCurList()) {
                            for (Map.Entry<String, BigDecimal> entry : subMap.entrySet()) {
                                //资金池id和币别id组成的
                                String mapKey = entry.getKey();
                                //资金池id
                                String[] keyId = mapKey.split(",");
                                //如果相等说明同资金池同币别,新增行
                                if (mapKey.equals(childList.get(j).getParentId() + "," + childList.get(j).getCurId())) {
                                    // 创建行
                                    row = sheet.createRow(++index);
                                    //设置小计合计
                                    row = this.setRowSubTal(row, childList.get(j).getEnglishCode(), entry.getValue(), true);
                                } else if (keyId[0].equals(voList.get(i).getId())) {
                                    //说明同资金池不同币别新加一行小计
                                    // 创建行
                                    row = sheet.createRow(++index);
                                    //设置小计合计
                                    row = this.setRowSubTal(row, voList.get(i).getEnglishCode(), entry.getValue(), true);
                                }

                                // 设置Excel表头样式
                                setRowCellStyle(workbook, row);
                            }
                        }

                    }
                }
            } else {
                //处理没有子集(主体账号)。subCurList存放的是资金池id与币别id组成的钱
                for (Map<String, BigDecimal> subMap : voList.get(i).getSubCurList()) {
                    for (Map.Entry<String, BigDecimal> entry : subMap.entrySet()) {
                        //资金池id和币别id组成的
                        String mapKey = entry.getKey();
                        //资金池id
                        String[] keyId = mapKey.split(",");
                        if (keyId[0].equals(voList.get(i).getId())) {
                            //说明同资金池
                            // 创建行
                            row = sheet.createRow(++index);
                            //设置小计合计
                            row = this.setRowSubTal(row, voList.get(i).getEnglishCode(), entry.getValue(), true);
                            // 设置Excel表头样式
                            setRowCellStyle(workbook, row);
                        }
                    }
                }
            }
            if (i == voList.size() - 1) {
                //合计
                FexCorpPoolBankAccVO vo = voList.get(0);
                Map<String, BigDecimal> totalMap = this.toCurrMoneyTotal(vo.getSubCurList(),fexCorpPoolBankAccVO.getCurId(), fexCorpPoolBankAccVO.getRateType(), fexCorpPoolBankAccVO.getBalDate());
                if (ObjectUtil.isNotNull(totalMap)) {
                    for (Map.Entry<String, BigDecimal> totalTemp : totalMap.entrySet()) {
                        //说明同资金池
                        // 创建行
                        row = sheet.createRow(++index);
                        //设置小计合计
                        this.setRowSubTal(row, totalTemp.getKey(), totalTemp.getValue(), false);
                        // 设置Excel表头样式
                        setRowCellStyle(workbook, row);
                    }
                }
            }
            index++;
        }
    }

    /**
     * 设置行的样式
     *
     * @param workbook
     * @param row
     */
    public void setRowCellStyle(HSSFWorkbook workbook, HSSFRow row) {
        // Excel表格内容样式
        HSSFCellStyle cs = this.createExcelBodyStyle(workbook);
        // 设置Excel表头样式
        for (int num = 0; num < 8; num++) {
            HSSFCell row1cell = row.getCell(num);
            if (row1cell != null) {
                row1cell.setCellStyle(cs);
            }
        }
    }

    /**
     * 设置小计合计
     *
     * @param row
     * @param englishCode 币别
     * @param bal         合计
     * @param isSub       是否小计
     * @return
     */
    public HSSFRow setRowSubTal(HSSFRow row, String englishCode, BigDecimal bal, boolean isSub) {

        if (isSub) {
            row.createCell(0).setCellValue("小计(" + englishCode + ")");
        } else {
            row.createCell(0).setCellValue("合计(" + englishCode + ")");
        }
        row.createCell(1).setCellValue("");
        row.createCell(2).setCellValue("");
        row.createCell(3).setCellValue("");
        row.createCell(4).setCellValue("");
        row.createCell(5).setCellValue("");
        row.createCell(6).setCellValue("");
        row.createCell(7).setCellValue(NumberUtil.formatNumeric(bal));
        return row;
    }

    /**
     * 设置小计
     *
     * @param row
     * @param parentVO 主账号VO
     * @param childVO  成员账号VO
     * @param isParent 是否主账号
     * @return
     */
    public HSSFRow setRowData(HSSFRow row, FexCorpPoolBankAccVO parentVO, FexCorpPoolBankAccVO childVO, boolean isParent) {

        String poolCodeName = "";
        String corpName = "";
        String bankAcc = "";
        String englishCode = "";
        String poolNature = "";
        String accNature = "";
        String BalDate = "";
        String bal = "";
        if (isParent && !EmptyUtils.isEmpty(parentVO)) {
            poolCodeName = "(" + parentVO.getPoolCode() + ")" + parentVO.getPoolName();
            corpName = parentVO.getCorpName();
            bankAcc = parentVO.getBankAcc();
            englishCode = parentVO.getEnglishCode();
            poolNature = dictService.get("fexCorpPool.poolNature", parentVO.getPoolNature());
            accNature = dictService.get("fexCorpPoolBank.accNature", parentVO.getAccNature());
            BalDate = DateUtils.formatDate(parentVO.getBalDate());
            bal = NumberUtil.formatNumeric(parentVO.getBal());
            row.createCell(0).setCellValue(poolCodeName);
        } else {
            corpName = childVO.getCorpName();
            bankAcc = childVO.getBankAcc();
            englishCode = childVO.getEnglishCode();
            poolNature = dictService.get("fexCorpPool.poolNature", parentVO.getPoolNature());
            accNature = dictService.get("fexCorpPoolBank.accNature", childVO.getAccNature());
            BalDate = DateUtils.formatDate(childVO.getBalDate());
            bal = NumberUtil.formatNumeric(childVO.getBal());
            row.createCell(0).setCellValue("");
        }
        row.createCell(1).setCellValue(corpName);
        row.createCell(2).setCellValue(bankAcc);
        row.createCell(3).setCellValue(englishCode);
        row.createCell(4).setCellValue(poolNature);
        row.createCell(5).setCellValue(accNature);
        row.createCell(6).setCellValue(BalDate);
        row.createCell(7).setCellValue(bal);
        return row;
    }
}
