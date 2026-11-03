package com.bytter.report.domain.cms.service;

import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.bytter.report.domain.cms.dao.RatBkAmountCalculateDao;
import com.bytter.report.domain.cms.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.*;
import com.bytter.core.constant.RationConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.*;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luxy
 * @Title: RatBkAmountCalculateService
 * @ProjectName T3.4
 * @Description: 授信额度测算服务层
 * @date 2020/7/28 10:32
 */
@Service("ratBkAmountCalculateService")
public class RatBkAmountCalculateService extends BaseService {
    @Autowired
    private RatBkAmountCalculateDao calculateDao;
    @DubboReference
    private IMultiLangService multiLangServiceI;
    @DubboReference
    private ICorpService corpService;
    @DubboReference
    private IFcExcrateAdjustService rateAdjustService;
    @DubboReference
    private IBtCurrencyService currencyService;
    @DubboReference
    private IDictService iDictService;
    
    @Override
    public BaseDao getEntityDao() {
        return null;
    }

    /**
     * 获取授信额度测算数据
     * @param searchVO
     * @param user
     * @return
     */
    public List<RatBkCalculateVO> getRatCalList(RatBkCalculateSearchVO searchVO, ICurrentUser user){
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_RATION_AMOUNTCALCULATE_QUERY, user.getId());
        //查询统计数据
        List<RatBkCalculateVO> ratLists=calculateDao.queryRatBkDisList(searchVO,corpIds);
        if (EmptyUtils.isEmptyList(ratLists)) {
            ratLists=new ArrayList<RatBkCalculateVO>();
            return ratLists;
        }
        List<RatBkCalculateVO> beanList=dealDatas(ratLists,searchVO);
        Map<String, RatBkUseCorpVO> subTotal=calBankOrCorpSubTotal(beanList);//币别小计
        //设置银行、单位数、综合品种数
        setTotalSize(beanList);
        //币别合计
        calCurTotal(subTotal,beanList);
        return beanList;
    }


    /**
     * 处理数据：查询可释放额度，折合币种
     * @param ratList
     * @param searchVO
     * @return
     */
    public List<RatBkCalculateVO> dealDatas(List<RatBkCalculateVO> ratList,RatBkCalculateSearchVO searchVO) {
        Date calEndDate=DateUtils.getDate();
        if(EmptyUtils.isNotEmpty(searchVO.getCalEndDate())){
            calEndDate=DateUtils.parse(searchVO.getCalEndDate(),DateUtils.PATTERN_CLASSICAL_SIMPLE);
        }
        //设置授信预测释放额度
        for(RatBkCalculateVO bean:ratList) {
            BigDecimal useMoney=BigDecimal.ZERO;
            if(null!=bean.getSysCode()){
                useMoney=calculateDao.queryRatUse(bean.getSysCode(), bean.getBillId(), bean.getBillNo(), calEndDate);
                if(StringUtils.isNotEmpty(bean.getBillCurCode()) && !bean.getBillCurCode().equals(bean.getCurCode())){//业务币别与授信币别不一致需要进行金额折算
                    useMoney=useMoney.multiply(bean.getExchangeRate());
                }
            }
            bean.setCalReleaseMoney(useMoney);
        }

        //折合币种
        if(EmptyUtils.isNotEmpty(searchVO.getIsConvertCur())&&searchVO.getIsConvertCur().equals(RationConstant.RAT_COMMON_Y)){
            convertCur(ratList,searchVO.getConvertCurId(),searchVO.getExRateType());
        }

        List<RatUrationUseDtlVO> dtlVOS=new ArrayList<>();
        List<RatBkCalculateVO> beanList=new ArrayList<RatBkCalculateVO>();
        //一级分组Map
        Map<String,RatBkCalculateVO> firstMap=new HashMap<String,RatBkCalculateVO>();
        //二级分组Map
        Map<String,RatBkUseCorpVO> secondMap=new HashMap<String,RatBkUseCorpVO>();
        //三级分组Map
        Map<String,RatUrationUseDtlVO> thirdMap=new LinkedHashMap<>();
        //四级分组Map
        Map<String,RatUrationUseDtlVO> fourthMap=new LinkedHashMap<>();
        for(RatBkCalculateVO bean:ratList){
            String bankCode=bean.getBankCode();//银行编号
            String corpId=bean.getCorpId();//单位id
            String curCode=bean.getCurCode();//币别id
            String itemCode=bean.getItemCode();
            String beginDate=DateUtils.date2Str(bean.getBeginDate(),DateUtils.date_sdf());//开始时间
            String endDate=DateUtils.date2Str(bean.getEndDate(),DateUtils.date_sdf());//结束时间
            String corpKey=bankCode+"_"+corpId+"_"+curCode+"_"+beginDate+"_"+endDate;
            String itemKey=corpKey+"&"+itemCode;
            String firstMapKey=bankCode;//设置一级分组数据
            RatBkCalculateVO firstVO=firstMap.get(bankCode);
            if(firstVO==null){
                firstVO=new RatBkCalculateVO();
                BeanUtils.copyProperties(bean, firstVO);
                firstMap.put(firstMapKey, firstVO);
                beanList.add(firstVO);
            }

            //设置四级分组数据
            RatUrationUseDtlVO fourthVO=fourthMap.get(itemKey);
            if(fourthVO==null){
                fourthVO=new RatUrationUseDtlVO();
                BeanUtils.copyProperties(bean, fourthVO);
                fourthMap.put(itemKey, fourthVO);
            }else{
                fourthVO.setCalReleaseMoney(fourthVO.getCalReleaseMoney().add(bean.getCalReleaseMoney()));
            }
            if (null != bean.getItemFlag() && bean.getItemFlag().equals(RationConstant.RAT_COMFLAG_1)) {//综合品种
                fourthVO.setCalLeftMoney(fourthVO.getMaxMoney().subtract(fourthVO.getApproveMoney()).subtract(fourthVO.getUseMoney()).add(fourthVO.getCalReleaseMoney()));
            } else {
                fourthVO.setCalLeftMoney(fourthVO.getMoney().subtract(fourthVO.getApproveMoney()).subtract(fourthVO.getUseMoney()).add(fourthVO.getCalReleaseMoney()));
            }

        }

        //整理三级分组数据
        for (String key : fourthMap.keySet()) {
            RatUrationUseDtlVO useDtlVO=fourthMap.get(key);
            String[] keyArr=key.split("&");
            String secondKey=keyArr[0];
            if(useDtlVO.getItemFlag().equals(RationConstant.RAT_COMFLAG_1)) {//综合品种需要进行额度汇总
                String itemFlagKey=secondKey+"&comp";
                RatUrationUseDtlVO thirdVO=thirdMap.get(itemFlagKey);
                if (null == thirdVO) {
                    thirdVO = new RatUrationUseDtlVO();
                    BeanUtils.copyProperties(useDtlVO, thirdVO);
                    thirdMap.put(itemFlagKey, thirdVO);
                } else {
                    thirdVO.setUseMoney(thirdVO.getUseMoney().add(useDtlVO.getUseMoney()));
                    thirdVO.setApproveMoney(thirdVO.getApproveMoney().add(useDtlVO.getApproveMoney()));
                    thirdVO.setCalReleaseMoney(thirdVO.getCalReleaseMoney().add(useDtlVO.getCalReleaseMoney()));
                    //预测剩余额度=授信额度-占用-在审占用+预测释放
                    thirdVO.setCalLeftMoney(thirdVO.getMoney().subtract(thirdVO.getUseMoney()).subtract(thirdVO.getApproveMoney()).add(thirdVO.getCalReleaseMoney()));
                }

                thirdVO.addRationDtl(useDtlVO);
            }else{
                thirdMap.put(key, useDtlVO);
            }
        }


        //将数据整理到二级和一级分组
        for(RatBkCalculateVO bean:ratList){
            String bankCode=bean.getBankCode();//银行id
            String corpId=bean.getCorpId();//单位id
            String curCode=bean.getCurCode();//币别id
            String beginDate=DateUtils.date2Str(bean.getBeginDate(),DateUtils.date_sdf());//开始时间
            String endDate=DateUtils.date2Str(bean.getEndDate(),DateUtils.date_sdf());//结束时间
            String corpKey=bankCode+"_"+corpId+"_"+curCode+"_"+beginDate+"_"+endDate;
            RatBkCalculateVO firstVO=firstMap.get(bankCode);
            RatBkUseCorpVO secondVO=secondMap.get(corpKey);
            if(secondVO==null){
                secondVO=new RatBkUseCorpVO();
                BeanUtils.copyProperties(bean, secondVO);
                secondMap.put(corpKey, secondVO);
                for (String key : thirdMap.keySet()) {
                    String[] keyArr=key.split("&");
                    String secondKey=keyArr[0];
                    if(secondKey.equals(corpKey)){
                        RatUrationUseDtlVO disDtlDTO =thirdMap.get(key) ;
                        secondVO.addRationDtl(disDtlDTO);
                    }
                }
                firstVO.addCorpDtl(secondVO);
            }
        }

        //小计
        calCorpMoney(beanList);
        return beanList;
    }


    /**
     * 币别转换
     * @param ratList
     * @param convertCurId
     */
    public void convertCur(List<RatBkCalculateVO> ratList,String convertCurId,String exRateType){
        BtCurrencyDTO currencyDTO=currencyService.getObjectById(convertCurId);
        String convertCurName=currencyDTO.getCurName();
        for(RatBkCalculateVO calculateVO:ratList){
            if(calculateVO.getCurCode().equals(convertCurId)){
                continue;
            }else{
                BigDecimal exrate=rateAdjustService.getExchangeRateBy(calculateVO.getCurCode(), convertCurId,DateUtils.getDate(),exRateType);
                calculateVO.setMoney(calculateVO.getMoney()==null?BigDecimal.ZERO:calculateVO.getMoney().multiply(exrate).setScale(2, BigDecimal.ROUND_HALF_UP));
                calculateVO.setMaxMoney(calculateVO.getMaxMoney()==null?BigDecimal.ZERO:calculateVO.getMaxMoney().multiply(exrate).setScale(2, BigDecimal.ROUND_HALF_UP));
                calculateVO.setUseMoney(calculateVO.getUseMoney()==null?BigDecimal.ZERO:calculateVO.getUseMoney().multiply(exrate).setScale(2, BigDecimal.ROUND_HALF_UP));
                calculateVO.setApproveMoney(calculateVO.getApproveMoney()==null?BigDecimal.ZERO:calculateVO.getApproveMoney().multiply(exrate).setScale(2, BigDecimal.ROUND_HALF_UP));
                calculateVO.setCalReleaseMoney(calculateVO.getCalReleaseMoney()==null?BigDecimal.ZERO:calculateVO.getCalReleaseMoney().multiply(exrate).setScale(2, BigDecimal.ROUND_HALF_UP));
                calculateVO.setCurCode(convertCurId);
                calculateVO.setCurName(convertCurName);
            }
        }

    }

    /**
     * 数据小计
     * @param beanList
     */

    public void calCorpMoney(List<RatBkCalculateVO> beanList){
        //单位金额
        for(RatBkCalculateVO vo:beanList){
            List<RatBkUseCorpVO> corpList=vo.getCorpDtls();
            for(RatBkUseCorpVO corp:corpList){
                BigDecimal subTotalMoney =new BigDecimal(0.0);
                BigDecimal subTotalApproveMoney =new BigDecimal(0.0);
                BigDecimal subTotalUseMoney =new BigDecimal(0.0);
                BigDecimal subTotalReleaseMoney =new BigDecimal(0.0);
                BigDecimal subTotalCalLeftMoney =new BigDecimal(0.0);
                List<RatUrationUseDtlVO> dtl=corp.getRationDlts();
                for(RatUrationUseDtlVO dtlVO:dtl){
                    if(dtlVO.getItemFlag().equals(RationConstant.RAT_COMFLAG_0)){
                        subTotalMoney = subTotalMoney.add(dtlVO.getMoney());
                    }else{//综合品种取最高额度
                        subTotalMoney = subTotalMoney.add(dtlVO.getMaxMoney());
                    }
                    subTotalApproveMoney=subTotalApproveMoney.add(dtlVO.getApproveMoney());
                    subTotalUseMoney=subTotalUseMoney.add(dtlVO.getUseMoney());
                    subTotalReleaseMoney=subTotalReleaseMoney.add(dtlVO.getCalReleaseMoney());
                    subTotalCalLeftMoney=subTotalCalLeftMoney.add(dtlVO.getCalLeftMoney());
                }

                corp.setMoney(subTotalMoney);
                corp.setApproveMoney(subTotalApproveMoney);
                corp.setUseMoney(subTotalUseMoney);
                corp.setCalReleaseMoney(subTotalReleaseMoney);
                corp.setCalLeftMoney(subTotalCalLeftMoney);
            }
        }
    }


    /**
     * 分币别小计（二级分组小计）
     * @param beanList
     * @return
     */
    public Map<String,RatBkUseCorpVO> calBankOrCorpSubTotal(List<RatBkCalculateVO> beanList){
        Map<String,RatBkUseCorpVO> subTotal=new HashMap<String,RatBkUseCorpVO>();
        for(RatBkCalculateVO rat:beanList){
            String sortCode="";
            List<RatBkUseCorpVO> secondList=rat.getCorpDtls();//二级分组集合
            RatBkUseCorpVO curVO=null;
            for(RatBkUseCorpVO vo:secondList){
                String curCode=vo.getCurCode();
                sortCode=vo.getBankCode();
                curVO=subTotal.get(sortCode+"_"+curCode);
                if(curVO==null){
                    curVO=new RatBkUseCorpVO();
                    BeanUtils.copyProperties(vo, curVO);
                    curVO.setCorpId("subTotal");
                    curVO.setCorpName(vo.getCurName()+"_"+multiLangServiceI.getLang("table.datatable.subtotal"));
                    curVO.setMoney(vo.getMoney());
                    curVO.setApproveMoney(vo.getApproveMoney());
                    curVO.setUseMoney(vo.getUseMoney());
                    curVO.setCalReleaseMoney(vo.getCalReleaseMoney());
                    curVO.setCalLeftMoney(vo.getCalLeftMoney());
                    subTotal.put(sortCode+"_"+curCode, curVO);
                }else{
                    curVO.setMoney(curVO.getMoney().add(vo.getMoney()));
                    curVO.setApproveMoney(curVO.getApproveMoney().add(vo.getApproveMoney()));
                    curVO.setUseMoney(curVO.getUseMoney().add(vo.getUseMoney()));
                    curVO.setCalReleaseMoney(curVO.getCalReleaseMoney().add(vo.getCalReleaseMoney()));
                    curVO.setCalLeftMoney(curVO.getCalLeftMoney().add(vo.getCalLeftMoney()));
                }
            }
        }
        //添加小计明细
        for(RatBkCalculateVO rat:beanList){
            String sortCode=rat.getBankCode();
            List<RatBkUseCorpVO> corpList=rat.getCorpDtls();
            for(Map.Entry<String,RatBkUseCorpVO> entry:subTotal.entrySet()){
                String code=entry.getKey();
                String[] codeStr=code.split("_");
                RatBkUseCorpVO corpVO=entry.getValue();
                if(codeStr[0].equals(sortCode)){
                    corpList.add(corpVO);
                }
            }
        }
        return subTotal;
    }

    /**
     * 分币别合计
     * @param bankSubTotal
     * @param beanList
     */
    public void calCurTotal(Map<String,RatBkUseCorpVO> bankSubTotal,List<RatBkCalculateVO> beanList){
        //币别合计
        Map<String,RatBkCalculateVO> curTotal=new HashMap<String,RatBkCalculateVO>();
        for(Map.Entry<String,RatBkUseCorpVO> entry:bankSubTotal.entrySet()){
            String code=entry.getKey();
            String[] codeStr=code.split("_");
            RatBkUseCorpVO corpVO=entry.getValue();
            RatBkCalculateVO bankVO=curTotal.get(codeStr[1]);
            if(bankVO==null){
                bankVO=new RatBkCalculateVO();
                bankVO.setCorpId("total");
                bankVO.setCorpName(corpVO.getCurName()+"_"+multiLangServiceI.getLang("table.datatable.total"));
                bankVO.setMoney(corpVO.getMoney());
                bankVO.setApproveMoney(corpVO.getApproveMoney());
                bankVO.setUseMoney(corpVO.getUseMoney());
                bankVO.setCalReleaseMoney(corpVO.getCalReleaseMoney());
                bankVO.setCalLeftMoney(corpVO.getCalLeftMoney());
                curTotal.put(corpVO.getCurCode(), bankVO);
            }else{
                bankVO.setMoney(bankVO.getMoney().add(corpVO.getMoney()));
                bankVO.setApproveMoney(bankVO.getApproveMoney().add(corpVO.getApproveMoney()));
                bankVO.setUseMoney(bankVO.getUseMoney().add(corpVO.getUseMoney()));
                bankVO.setCalReleaseMoney(bankVO.getCalReleaseMoney().add(corpVO.getCalReleaseMoney()));
                bankVO.setCalLeftMoney(bankVO.getCalLeftMoney().add(corpVO.getCalLeftMoney()));
            }
        }
        //遍历添加合计
        for(Map.Entry<String,RatBkCalculateVO> entry:curTotal.entrySet()){
            beanList.add(entry.getValue());
        }
    }

    /**
     * 设置各级总数，用于跨行显示
     * @param beanList
     */
    public void setTotalSize(List<RatBkCalculateVO> beanList){
        for(RatBkCalculateVO rat:beanList){
            int totalNum=0;

            List<RatBkUseCorpVO> corpList=rat.getCorpDtls();
            for(RatBkUseCorpVO vo:corpList){
                if(vo.getCorpId().equals("subTotal")){//币别小计行数
                    totalNum+=1;
                }else{
                    int dtlSize=vo.getRationDlts().size();
                    vo.setRationSize(dtlSize);
                    totalNum+=dtlSize;
                    List<RatUrationUseDtlVO> dtlVOList = vo.getRationDlts();
                    for (RatUrationUseDtlVO dtlVO : dtlVOList) {
                        if(null!=dtlVO.getRationDlts()){//设置综合品种条数
                            int comSize=dtlVO.getRationDlts().size();
                            dtlVO.setRationSize(comSize);
                        }
                    }
                }
            }
            rat.setTotalSize(totalNum);
        }
    }

    /**
     * 创建表格样式
     */
    public void creatExcel(HSSFWorkbook wb, HSSFSheet sheet) {
        //给单子名称一个长度
        sheet.setDefaultColumnWidth((short) 12);
        // 生成一个样式
        HSSFCellStyle style = wb.createCellStyle();
        //创建第一行（也可以称为表头）
        HSSFRow row = sheet.createRow(0);
        //设置表头字体
        HSSFFont headfont = wb.createFont();
        headfont.setFontName("宋体");
        headfont.setFontHeightInPoints((short) 14);// 字体大小
        headfont.setBold(true);//粗体显示
        style.setFont(headfont);
        style.setAlignment(HorizontalAlignment.CENTER); // 居中
        this.setBorder(style);
        //给表头第一行一次创建单元格
        String[] heads = new String[]{
                multiLangServiceI.getLang("common.button.bank"),//银行
                multiLangServiceI.getLang( "table.fcloanrepay.corpName"),//单位
                multiLangServiceI.getLang("table.fcBusinessRuleDefine.curCode"),//币别
                multiLangServiceI.getLang("table.ratBkration.begindate"),//授信开始时间
                multiLangServiceI.getLang("table.ratBkration.enddate"),//授信结束时间
                multiLangServiceI.getLang("table.ratBkrationDetail.itemCode"),//授信品种
                "",
                multiLangServiceI.getLang("table.ratBkration.money"),//授信额度
                multiLangServiceI.getLang("table.ratBkrationDisDetail.superProcessMoney"),//占用额度
                multiLangServiceI.getLang("table.ratBkrationDisDetail.checkProcessMoney"),//在审占用额度
                multiLangServiceI.getLang("table.ratBkration.releaseMoney"),//预测释放额度
                multiLangServiceI.getLang("table.ratBkration.calLeftMoney")};//预测剩余额度
        row.setHeightInPoints(20);
        for (int i = 0; i < heads.length; i++) {
            HSSFCell cell = row.createCell((short) i);
            cell.setCellValue(heads[i]);
            cell.setCellStyle(style);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));//合并表头6-7列
    }
    /**
     * 填充数据
     */
    public HSSFWorkbook addDataToSheet(List<RatBkCalculateVO> list, HSSFSheet sheet, HSSFWorkbook wb) {
        String compName=iDictService.get("RatModel.ItemFlag",1);
        //获取第2-6列的数据，存入map中
        int corpSize = 1;
        Map<String, RatBkUseCorpVO> map = new HashMap<String, RatBkUseCorpVO>();
        for (int i = 0; i < list.size(); i++) {
            List<RatBkUseCorpVO> corpVOList = list.get(i).getCorpDtls();
            if (null != corpVOList) {
                for (int j = 0; j < corpVOList.size(); j++) {
                    map.put(corpSize + "", corpVOList.get(j));
                    corpSize++;
                }
            }
        }

        //获取明细数据，放入detaillMap中
        int detailIndex=1;
        Map<String, Object> detaillMap = new LinkedHashMap<String, Object>();
        for (int j = 0; j < map.size(); j++) {
            RatBkUseCorpVO corpVO = map.get(j + 1 + "");
            List<RatUrationUseDtlVO> detailList = new ArrayList<RatUrationUseDtlVO>();
            if (!corpVO.getCorpId().equals("subTotal")) {//合计
                detailList = corpVO.getRationDlts();
                for (int z = 0; z < detailList.size(); z++) {
                    detaillMap.put(detailIndex + "", detailList.get(z));
                    detailIndex++;
                }
            } else {
                detaillMap.put(detailIndex + "", corpVO);
                detailIndex++;
            }
        }
        detaillMap.put(detailIndex + "", list.get(list.size() - 1));

        //填充明细数据
        int rowIndex = 1;
        HSSFRow row = null;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        HSSFCellStyle centerCellStyle = getCenterCellStyle(wb);
        HSSFCellStyle rightCellStyle = getRightCellStyle(wb);


        int dtlSpanSize=0;
        int dtlRow=1;
        for (int i = 1; i < detaillMap.size(); i++) {
            String mapObject = detaillMap.get(i + "").getClass().getSimpleName();
            row = sheet.createRow(rowIndex);
            for (int k = 0; k <= 6; k++) {
                row.createCell(k);
                row.getCell((short) k).setCellStyle(centerCellStyle);
            }
            for (int k = 7; k <= 11; k++) {
                row.createCell(k);
                row.getCell((short) k).setCellStyle(rightCellStyle);
            }
            if (mapObject.equals("RatUrationUseDtlVO")) {
                RatUrationUseDtlVO dtlVO = (RatUrationUseDtlVO) detaillMap.get(i + "");
                if(dtlVO.getItemFlag().equals(RationConstant.RAT_COMFLAG_0)){
                    row.getCell(5).setCellValue(dtlVO.getItemName());//授信品种
                    row.getCell(7).setCellValue(df.format(dtlVO.getMoney()) + "");//授信额度
                    row.getCell(8).setCellValue(df.format(dtlVO.getUseMoney()) + "");//占用额度
                    row.getCell(9).setCellValue(df.format(dtlVO.getApproveMoney()) + "");//在审占用额度
                    row.getCell(10).setCellValue(df.format(dtlVO.getCalReleaseMoney()) + "");//预测释放额度
                    row.getCell(11).setCellValue(df.format(dtlVO.getCalLeftMoney()) + "");//预测剩余额度
                    sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,5,6));//合并5-6列单品种的授信品种名
                    rowIndex++;
                }else{//综合品种
                    row.getCell(5).setCellValue(compName);
                    row.getCell(7).setCellValue(df.format(dtlVO.getMaxMoney()) + "");//授信额度
                    row.getCell(8).setCellValue(df.format(dtlVO.getUseMoney()) + "");//占用额度
                    row.getCell(9).setCellValue(df.format(dtlVO.getApproveMoney()) + "");//在审占用额度
                    row.getCell(10).setCellValue(df.format(dtlVO.getCalReleaseMoney()) + "");//预测释放额度
                    row.getCell(11).setCellValue(df.format(dtlVO.getCalLeftMoney()) + "");//预测剩余额度
                    List<RatUrationUseDtlVO> compDtls=dtlVO.getRationDlts();
                    if(compDtls.size()>1){//存在多条明细
                        row.getCell(6).setCellValue(compDtls.get(0).getItemName());//设置综合授信第一条授信品种名称
                        rowIndex++;
                        for(int m=1;m<compDtls.size();m++){
                            row = sheet.createRow(rowIndex);
                            for (int k = 0; k <= 11; k++) {
                                row.createCell(k);
                                row.getCell((short) k).setCellStyle(centerCellStyle);
                            }
                            row.getCell(6).setCellValue(compDtls.get(m).getItemName());//授信品种
                            rowIndex++;
                        }
                    }else{//只有一条明细
                        row.getCell(6).setCellValue(compDtls.get(0).getItemName());//授信品种
                        rowIndex++;
                    }
                }

                for (int k = 1; k <= 6; k++) {
                    row.getCell((short) k).setCellStyle(centerCellStyle);
                }
                dtlSpanSize = getRatDtlListSize(dtlVO);//获取需要跨的行数
                if (dtlSpanSize > 1) {
                    int lastRow = dtlRow + dtlSpanSize - 1;
                    sheet.addMergedRegion(new CellRangeAddress(dtlRow,lastRow,5,5));
                    sheet.addMergedRegion(new CellRangeAddress(dtlRow,lastRow,7,7));
                    sheet.addMergedRegion(new CellRangeAddress(dtlRow,lastRow,8,8));
                    sheet.addMergedRegion(new CellRangeAddress(dtlRow,lastRow,9,9));
                    sheet.addMergedRegion(new CellRangeAddress(dtlRow,lastRow,10,10));
                    sheet.addMergedRegion(new CellRangeAddress(dtlRow,lastRow,11,11));
                }
                dtlRow += dtlSpanSize;
            } else if (mapObject.equals("RatBkUseCorpVO")) {
                RatBkUseCorpVO corpVO = (RatBkUseCorpVO) detaillMap.get(i + "");
                row.getCell(7).setCellValue(df.format(corpVO.getMoney()) + "");
                row.getCell(8).setCellValue(df.format(corpVO.getUseMoney()) + "");
                row.getCell(9).setCellValue(df.format(corpVO.getApproveMoney()) + "");
                row.getCell(10).setCellValue(df.format(corpVO.getCalReleaseMoney()) + "");
                row.getCell(11).setCellValue(df.format(corpVO.getCalLeftMoney()) + "");
                CellRangeAddress region5 = new CellRangeAddress(rowIndex, rowIndex, (short) 1, (short) 6);//合并2-7列
                sheet.addMergedRegion(region5);
                row.createCell(1).setCellValue(corpVO.getCorpName());
                HSSFCell theCell = row.getCell((short) 1);//设置合并列格式
                HSSFCellStyle style = wb.createCellStyle();
                style.setAlignment(HorizontalAlignment.RIGHT);//居右
                this.setBorder(style);//设置所有边框
                theCell.setCellStyle(style);
                for (int m = 1; m < 6; m++) {
                    row.getCell((short) m).setCellStyle(style);
                }
                rowIndex++;
                dtlRow++;
            }

        }


        //填充数据（2-5列）
        int corpSpanSize = 0;
        int firstRow = 1;
        for (int i = 1; i < map.size(); i++) {
            RatBkUseCorpVO corpVO = map.get(i + "");
            if (!corpVO.getCorpId().equals("subTotal")) {
                row = sheet.getRow(firstRow);
                //统计维度为单位时第二列显示银行，统计维度为银行时第二列显示单位
                row.getCell(1).setCellValue(corpVO.getCorpName());
                row.getCell(2).setCellValue(corpVO.getCurName());
                row.getCell(3).setCellValue(DateUtils.formatDate(corpVO.getBeginDate(),"yyyy-MM-dd"));
                row.getCell(4).setCellValue(DateUtils.formatDate(corpVO.getEndDate(),"yyyy-MM-dd"));
            }

            if (corpVO.getCorpId().equals("subTotal")) {//总计的数据单独占一行
                corpSpanSize = 1;
            } else {
                corpSpanSize = getOneCorpListSize(corpVO);//获取需要跨的行数
            }
            if (corpSpanSize > 1) {
                int lastRow = firstRow + corpSpanSize - 1;
                CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, (short) 1, (short) 1);
                sheet.addMergedRegion(region);
                CellRangeAddress region2 = new CellRangeAddress(firstRow, lastRow, (short) 2, (short) 2);
                sheet.addMergedRegion(region2);
                CellRangeAddress region3 = new CellRangeAddress(firstRow, lastRow, (short) 3, (short) 3);
                sheet.addMergedRegion(region3);
                CellRangeAddress region4 = new CellRangeAddress(firstRow, lastRow, (short) 4, (short) 4);
                sheet.addMergedRegion(region4);
            }

            firstRow += corpSpanSize;
        }


        //设置第一列的数据
        int index = 1;
        int rowspanSize = 0;

        for (int i = 0; i < list.size(); i++) {
            HSSFCell cell = null;
            if (StringUtils.isEmpty(list.get(i).getBankName())) {//币别合计显示
                row = sheet.createRow(index);
                row.createCell(0).setCellValue(list.get(i).getCorpName());
                cell = row.getCell((short) 0);
                row.createCell(1);
                row.createCell(2);
                row.createCell(3);
                row.createCell(4);
                row.createCell(5);
                row.createCell(6);
                row.createCell(7).setCellValue(df.format(list.get(i).getMoney()) + "");
                row.createCell(8).setCellValue(df.format(list.get(i).getUseMoney()) + "");
                row.createCell(9).setCellValue(df.format(list.get(i).getApproveMoney()) + "");
                row.createCell(10).setCellValue(df.format(list.get(i).getCalReleaseMoney()) + "");
                row.createCell(11).setCellValue(df.format(list.get(i).getCalLeftMoney()) + "");
                CellRangeAddress region = new CellRangeAddress(index, index, (short) 0, (short) 6);//合并2-7列
                sheet.addMergedRegion(region);
                HSSFCell sumCell = row.getCell((short) 1);//设置合并列格式
                HSSFCellStyle sumStyle = wb.createCellStyle();
                sumStyle.setAlignment(HorizontalAlignment.RIGHT);//居右
                this.setBorder(sumStyle);//设置所有边框
                sumCell.setCellStyle(sumStyle);
                for (int n = 1; n < 12; n++) {
                    row.getCell((short) n).setCellStyle(sumStyle);
                }
            } else {
                row = sheet.getRow(index);
                row.createCell(0).setCellValue(list.get(i).getBankName());
                cell = row.getCell((short) 0);
            }
            HSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.RIGHT);//水平居右
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
            cellStyle.setWrapText(true);//设置自动换行
            this.setBorder(cellStyle);//设置所有边框
            cell.setCellStyle(cellStyle);
            rowspanSize = getOneBkUseSize(list.get(i));//获取需要跨的行数
            if (rowspanSize > 1) {
                CellRangeAddress region = new CellRangeAddress(index, index + rowspanSize - 1, (short) 0, (short) 0);//参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
                sheet.addMergedRegion(region);
            }
            index += rowspanSize;
        }

        return wb;
    }

    /**
     * 获取单个对象的高度（需要跨的行数）,主要是处理一对多的情况
     */
    public int getOneCorpListSize(RatBkUseCorpVO ratBkUseCorpVO) {
        int maxHeight=0;
        List<RatUrationUseDtlVO> listDtl = ratBkUseCorpVO.getRationDlts();
        if(null!=listDtl){
            for(RatUrationUseDtlVO dtlVO:listDtl){
                if(dtlVO.getItemFlag().equals(RationConstant.RAT_COMFLAG_1)){
                    maxHeight+=dtlVO.getRationDlts().size();
                }else {
                    maxHeight++;
                }
            }
        }else{
            maxHeight=1;
        }

        return maxHeight;
    }

    /**
     * 获取银行或单位名称的高度（需要跨的行数）
     */
    public int getOneBkUseSize(RatBkCalculateVO calculateVO) {
        int maxHeight = 1;
        List<RatBkUseCorpVO> banklist = calculateVO.getCorpDtls();
        if (null != banklist) {
            for (int k = 0, paramSize = banklist.size(); k < paramSize - 1; k++) {
                if (banklist.get(k).getCorpId().equals("subTotal")) {//合计时加1
                    maxHeight += 1;
                } else {
                    List<RatUrationUseDtlVO> listDtl = banklist.get(k).getRationDlts();
                    if (listDtl != null || listDtl.size() > 0) {
                        for(RatUrationUseDtlVO dtlVO:listDtl){
                            if(dtlVO.getItemFlag().equals(RationConstant.RAT_COMFLAG_1)){
                                maxHeight+=dtlVO.getRationDlts().size();
                            }else {
                                maxHeight++;
                            }
                        }
                    }
                }
            }
        }
        return maxHeight;
    }

    /**
     * 获取授信品种需要跨的行数
     */
    public int getRatDtlListSize(RatUrationUseDtlVO dtlVO) {
        int maxHeight = 1;
        if(null!=dtlVO.getRationDlts()){
            List<RatUrationUseDtlVO> listDtl = dtlVO.getRationDlts();
            maxHeight = listDtl.size();
        }
        return maxHeight;
    }

    /**
     * 设置表格所有边框
     */
    public void setBorder(HSSFCellStyle style) {
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
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
     * 校验汇率是否存在
     */
    public Object checkExchangeRate(String curIds,String convertCurId,String exRateType){
        Map<String, Object> returnData=new HashMap<>();
        BigDecimal curExrate = BigDecimal.ZERO;
        if(EmptyUtils.isNotEmpty(curIds)){
            String[] curArr=curIds.split(",");
            //汇率时间取当前时间
            Date date=DateUtils.getDate();
            for(String origCurId:curArr){
                if (StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(origCurId)) {
                    curExrate = rateAdjustService.getExchangeRateBy(origCurId, convertCurId, date, exRateType);//距当前日期最近的汇率
                }
            }

        }
        return returnData;
    }
}
