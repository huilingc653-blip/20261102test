package com.bytter.biz.foreign.service;


import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.foreign.dao.FexRateInfoDao;
import com.bytter.biz.foreign.entity.FexRateExc;
import com.bytter.biz.foreign.entity.FexRateInfo;
import com.bytter.biz.foreign.entity.FexRateType;
import com.bytter.biz.foreign.vo.FexRateInfoVO;
import com.bytter.biz.foreign.vo.FexRateTypeVO;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: 外汇牌价表
 * @author huangx
 * @date 2020-07-16
 *
 */
@Service("fexRateInfoService")
@Transactional
public class FexRateInfoService extends BaseService<FexRateInfo, String> implements IExcelVerifyHandler<FexRateInfoVO> {
    @Autowired
    private FexRateInfoDao fexRateInfoDao;
    @Autowired
    private FexRateTypeService fexRateTypeService;
    @Autowired
    private FexRateExcService fexRateExcService;
    @Autowired(required = false)
    private IBtCurrencyService btCurrencyService;

    @Override
    public BaseDao<FexRateInfo, String> getEntityDao() {
        return fexRateInfoDao;
    }

    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String curName = (String) dataTablesInput.getFormdata().get("curName");
        String referCurName = (String) dataTablesInput.getFormdata().get("referCurName");
        if(StringUtils.isEmpty(curName)){
            dataTablesInput.getFormdata().remove("eqs_cur");
        }
        if(StringUtils.isEmpty(referCurName)){
            dataTablesInput.getFormdata().remove("eqs_referCur");
        }
        return fexRateInfoDao.getDataTablesUnion(dataTablesInput, sql.toString());
    }

    /**
     * 根据ID获取外汇牌价信息
     *
     * @param id
     * @return
     */
    public FexRateInfoVO getByIdUnion(String id) {
        return fexRateInfoDao.getByIdUnion(id);
    }

    /**
     * 外汇牌价查询
     * @param fexRateInfo
     */
    public void query(FexRateInfoVO fexRateInfo, ICurrentUser user){
        //获取外汇牌价类型
        FexRateType fexRateType = fexRateTypeService.get(fexRateInfo.getId(), true);
        FexRateExc fexRateExc = new FexRateExc();
        fexRateExc.setRateTypeId(fexRateInfo.getRateTypeId());
        fexRateExc.setBifCode(fexRateType.getBifCode());
        fexRateExc.setCur(fexRateInfo.getCur());
        fexRateExc.setVoucherStat(BdlinkConstant.PRE_SEND);
        fexRateExc.setCreateBy(user.getLoginName());
        fexRateExc.setCreateDate(DateUtils.getDate());
        fexRateExcService.saveEntity(fexRateExc);
    }

    /**
     * 导入
     *
     * @param list
     */
    public void saveImport(List<FexRateInfoVO> list, ICurrentUser user) {
        List<FexRateInfo> newFexRateInfoList=new ArrayList<>();
        Map<String,FexRateInfoVO> fexRateInfoMap=new HashMap<>();//用于判断去重数据
        List<String> ids=new ArrayList<>();//需要被覆盖删除的数据
        for (FexRateInfoVO rateInfoVO : list) {
            FexRateExc fexRateExc = new FexRateExc();
            fexRateExc.setRateTypeId(rateInfoVO.getRateTypeId());
            fexRateExc.setBifCode(rateInfoVO.getBifCode());
            fexRateExc.setCur(rateInfoVO.getCur());
            fexRateExc.setVoucherStat(BdlinkConstant.SUCCESS);
            fexRateExc.setReturnTime(rateInfoVO.getReturnTime());
            fexRateExc.setCreateBy(user.getLoginName());
            fexRateExc.setCreateDate(DateUtils.getDate());
            fexRateExcService.saveEntity(fexRateExc);

            if(EmptyUtils.isEmpty(rateInfoVO.getMiddleRate())){
                BigDecimal middleRate=(rateInfoVO.getPurchaseRate().add(rateInfoVO.getSellRate())).divide(new BigDecimal(2));
                rateInfoVO.setMiddleRate(middleRate);
            }
            if(EmptyUtils.isEmpty(rateInfoVO.getExchangeRateDate())){
                rateInfoVO.setExchangeRateDate(rateInfoVO.getReturnTime());
            }
            if(EmptyUtils.isEmpty(rateInfoVO.getExchangeRateDate())){
                rateInfoVO.setExchangeRateDate(rateInfoVO.getEffectDate());
            }

            FexRateInfo rateInfo = new FexRateInfo();
            BeanUtils.copyNotNullProperties(rateInfoVO, rateInfo);
            rateInfo.setRateExcId(fexRateExc.getId());
            rateInfo.setCreateBy(user.getLoginName());
            rateInfo.setCreateDate(DateUtils.getDate());

            newFexRateInfoList.add(rateInfo);
            String key=rateInfoVO.getRateTypeId()+rateInfoVO.getCur()+rateInfoVO.getReferCur()+DateUtils.format(rateInfoVO.getExchangeRateDate(),"yyyy-MM-dd")
                    +DateUtils.format(rateInfoVO.getReturnTime(),DateUtils.PATTERN_CLASSICAL);
            if(!fexRateInfoMap.containsKey(key)){
                fexRateInfoMap.put(key,rateInfoVO);
            }
        }
        //导入时，如果已存在同外汇牌价类型、同标准币种、同报价币种、同发布时间、和同汇率日期的数据时，做覆盖处理。
        // 获取极值
        Date maxDate = Collections.max(list,
                Comparator.comparing(FexRateInfoVO::getExchangeRateDate))
                .getExchangeRateDate();

        Date minDate = Collections.min(list,
                Comparator.comparing(FexRateInfoVO::getExchangeRateDate))
                .getExchangeRateDate();
        //获取所有
        List<FexRateInfoVO> infoVOList=fexRateInfoDao.getByExchangeRateDate(minDate,maxDate);
        if(!EmptyUtils.isEmpty(infoVOList) && infoVOList.size()>0){
            for(FexRateInfoVO infoVO:infoVOList){
                String key=infoVO.getRateTypeId()+infoVO.getCur()+infoVO.getReferCur()+DateUtils.format(infoVO.getExchangeRateDate(),"yyyy-MM-dd")
                        +DateUtils.format(infoVO.getReturnTime(),DateUtils.PATTERN_CLASSICAL);
                if(fexRateInfoMap.containsKey(key)){
                    ids.add(infoVO.getId());
                }
            }
        }
        if(ids.size()>0){
            fexRateInfoDao.deleteByIds(ids);
            fexRateInfoDao.deleteExcByIds(ids);
        }
        this.batchSave(newFexRateInfoList);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(FexRateInfoVO vo) {
        return null;
    }

    private HashMap<String, BtCurrencyDTO> getCurMap(){
        //币种集合
        HashMap<String, BtCurrencyDTO> curMap = new HashMap<>();
        List<BtCurrencyDTO> list = btCurrencyService.listAllBtCurrency();
        for(BtCurrencyDTO cur:list){
            curMap.put(cur.getCurName(), cur);
        }
        return curMap;
    }

    @Override
    public void verifyHandler(List<FexRateInfoVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            //外汇牌价类型集合
            HashMap<String, FexRateTypeVO> rateTypeMap = new HashMap<>();
            //币种集合
            HashMap<String, BtCurrencyDTO> curMap = this.getCurMap();
            //外汇牌价类型查询条件
            FexRateTypeVO searchVo = new FexRateTypeVO();
            searchVo.setValidSign(CommonConstant.YES);
            // 创建复合键(外汇牌价类型、同标准币种、同报价币种、同发布时间、和同汇率日期)
            // 记录已出现的复合键
            Set<String> duplicateKeys = new HashSet<>();
            for (FexRateInfoVO vo : list) {
                searchVo.setTypeName(vo.getRateTypeName());
                List<String> msgList = new ArrayList<>();
                // 校验外汇牌价类型
                FexRateTypeVO rateType = rateTypeMap.get(vo.getRateTypeName());
                if (rateType == null) {
                    List<FexRateTypeVO> fexRateTypeList = this.fexRateTypeService.getFexRateTypeList(searchVo);
                    if (EmptyUtils.isNotEmpty(fexRateTypeList)) {
                        rateType = fexRateTypeList.get(0);
                    }
                    if(rateType != null) {
                        rateTypeMap.put(rateType.getTypeName(), rateType);
                    }
                }
                String key = buildCompositeKey(vo);
                if (duplicateKeys.contains(key)) {
                    msgList.add("table.externalCorpQuickPay.data.repeat");
                } else {
                    duplicateKeys.add(key);
                }
                if (rateType == null) {
                    msgList.add("fex.rateInfo.message.rateType.not.exist");
                } else {
                    vo.setRateTypeId(rateType.getId());
                    vo.setBifCode(rateType.getBifCode());
                }
                // 校验标准币种
                if (curMap.containsKey(vo.getCurName())) {
                    vo.setCur(curMap.get(vo.getCurName()).getId());
                } else {
                    msgList.add("fex.rateInfo.message.cur.not.exist");
                }
                // 校验报价币种
                if (curMap.containsKey(vo.getReferCurName())) {
                    vo.setReferCur(curMap.get(vo.getReferCurName()).getId());
                } else {
                    msgList.add("fex.rateInfo.message.referCur.not.exist");
                }
                if (EmptyUtils.isEmpty(vo.getEffectDate())) {
                    msgList.add("fex.rateInfo.message.returnTime.null");
                } else {
                    try{
                        DateUtils.format(vo.getEffectDate(), "yyyy-MM-dd HH:mm:ss");
                    } catch (Exception e) {
                        msgList.add("fex.rateInfo.message.returnTime.null");
                    }
                }
                if(EmptyUtils.isEmpty(vo.getExchangeRateDate())){
                    vo.setExchangeRateDate(DateUtils.parseDate(vo.getReturnTime()));
                }
                vo.addErrorMsg(msgList);
            }
        }
    }

    /**
     * 根据外汇牌价类型获取外汇牌价信息
     * @param rateTypeId
     * @return
     */
    public List<FexRateInfo> listByRateTypeId(String rateTypeId){
        return fexRateInfoDao.listBy("rateTypeId", rateTypeId);
    }


    /**
     * 获取即期报价方式
     * @param rateTypeId
     * @param curId
     * @param referCurId
     * @return
     */
    public FexRateInfoVO getQuoMethodForSP(String rateTypeId,String curId,String referCurId){
        FexRateInfoVO fexRateInfoVO=null;
        FexRateTypeVO fexRateTypeVO = fexRateTypeService.getById(rateTypeId);
        //Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
        //List<FexRateInfoVO> fexRateInfoVOList= fexRateInfoDao.getFexRateInfoVO(rateTypeId,currencyMap.get(curId).getSysCurId(),currencyMap.get(referCurId).getSysCurId(),"1");
        List<FexRateInfoVO> fexRateInfoVOList= fexRateInfoDao.getFexRateInfoVO(rateTypeId,curId,referCurId,"1");
        if(CollectionUtil.isEmpty(fexRateInfoVOList)){
            throw new ParameterException(ForeignMessageEnum.FEX_RATE_INFO_IS_NULL, fexRateTypeVO.getTypeName());
        }
        if(!EmptyUtils.isEmpty(fexRateInfoVOList) && fexRateInfoVOList.size()>0){
            fexRateInfoVO=fexRateInfoVOList.get(0);
        }
        return fexRateInfoVO;
    }

    /**
     * 获取即期对应汇率
     * @param rateTypeId
     * @param curId
     * @param referCurId
     * @param type  1买入价、2卖出价、3中间价
     * @return
     */
    public BigDecimal getRateForSP(String rateTypeId,String curId,String referCurId,String type){
        BigDecimal returnBIg=null;
        //Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
        //List<FexRateInfoVO> fexRateInfoVOList= fexRateInfoDao.getFexRateInfoVO(rateTypeId, currencyMap.get(curId).getSysCurId(), currencyMap.get(referCurId).getSysCurId(),"1");
        List<FexRateInfoVO> fexRateInfoVOList= fexRateInfoDao.getFexRateInfoVO(rateTypeId, curId, referCurId,"1");
        if(!EmptyUtils.isEmpty(fexRateInfoVOList) && fexRateInfoVOList.size()>0){
            FexRateInfoVO fexRateInfoVO=fexRateInfoVOList.get(0);
            if("1".equals(type)){
                returnBIg=fexRateInfoVO.getPurchaseRate();
            }else if("2".equals(type)){
                returnBIg=fexRateInfoVO.getSellRate();
            }else if("3".equals(type)){
                if(EmptyUtils.isEmpty(fexRateInfoVO.getMiddleRate())){
                    fexRateInfoVO.setMiddleRate((fexRateInfoVO.getPurchaseRate().add(fexRateInfoVO.getSellRate())).divide(new BigDecimal(2)));
                }
                returnBIg=fexRateInfoVO.getMiddleRate();
            }

        }
        return returnBIg.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取最新即期汇率
     * @param rateTypeId
     * @param curId
     * @param referCurId
     * @return
     */
    public FexRateInfoVO  getLastFexRateForSP(String rateTypeId, String curId,String referCurId){
        //Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
        //List<FexRateInfoVO> fexRateInfoVOList= fexRateInfoDao.getFexRateInfoVO(rateTypeId,currencyMap.get(curId).getSysCurId(),
        //       currencyMap.get(referCurId).getSysCurId(),"1");
        List<FexRateInfoVO> fexRateInfoVOList= fexRateInfoDao.getFexRateInfoVO(rateTypeId,curId,referCurId,"1");
        FexRateInfoVO fexRateInfoVO = null;
        if (!EmptyUtils.isEmpty(fexRateInfoVOList)) {
            fexRateInfoVO = fexRateInfoVOList.get(0);
        }
        return fexRateInfoVO;
    }

    /**
     * 获取远期的报价方式
     * @param rateTypeId
     * @param curId
     * @param referCurId
     * @return
     */
    public FexRateInfoVO getQuoMethodForEnd(String rateTypeId,String curId,String referCurId){
        //Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
        FexRateInfoVO fexRateInfoVO = null;
        //List<FexRateInfoVO> fexRateInfoVOList= fexRateInfoDao.getFexRateInfoVO(rateTypeId,currencyMap.get(curId).getSysCurId(), currencyMap.get(referCurId).getSysCurId(),"0");
        List<FexRateInfoVO> fexRateInfoVOList= fexRateInfoDao.getFexRateInfoVO(rateTypeId,curId, referCurId,"0");
        if(!EmptyUtils.isEmpty(fexRateInfoVOList) && fexRateInfoVOList.size()>0){
            fexRateInfoVO=fexRateInfoVOList.get(0);
        }
        return fexRateInfoVO;
    }

    /**
     *  获取远期的汇率
     * @param rateTypeId
     * @param curId
     * @param referCurId
     * @param rateDate  汇率日期
     * @param type  1买入价、2卖出价、3中间价
     * @return
     */
    public FexRateInfoVO getRateForEnd(String rateTypeId,String curId,String referCurId,Date rateDate,String type){
        //Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
        BigDecimal returnBIg = null;
        FexRateInfoVO fexRateInfoVO = null;
        FexRateInfoVO latestFexRateInfoVO = null;
        //List<FexRateInfoVO> fexRateInfoVOList= fexRateInfoDao.getFexRateInfoVO(rateTypeId,currencyMap.get(curId).getSysCurId(), currencyMap.get(referCurId).getSysCurId(),"0");
        List<FexRateInfoVO> fexRateInfoVOList= fexRateInfoDao.getFexRateInfoVO(rateTypeId,curId, referCurId,"0");
        if(!EmptyUtils.isEmpty(fexRateInfoVOList) && fexRateInfoVOList.size()>0) {
            latestFexRateInfoVO = fexRateInfoVOList.get(0);
            Date maxEffectDate=fexRateInfoVOList.get(0).getEffectDate();
            FexRateInfoVO firstVO=null;//前面一个日期小值
            FexRateInfoVO lastVO=null;//后面一个日期大值
            //得到最新发布的数据
            List<FexRateInfoVO> rateInfoVOS=fexRateInfoVOList.stream()
                    .filter(item -> item.getEffectDate() != null && item.getEffectDate().equals(maxEffectDate))
                    .collect(Collectors.toList());
            //统一发布日期按照汇率日期降序排列数据
            if(rateInfoVOS.size()>=2) {
                Date minDate= rateInfoVOS.get(rateInfoVOS.size() - 1).getEffectDate();
                Date maxDate= rateInfoVOS.get(0).getEffectDate();
                if (EmptyUtils.isEmpty(rateDate) || rateDate.compareTo(minDate)<0) {
                    firstVO = rateInfoVOS.get(rateInfoVOS.size() - 1);
                    lastVO = rateInfoVOS.get(rateInfoVOS.size() - 2);
                }else if(rateDate.compareTo(maxDate)>0){
                    firstVO = rateInfoVOS.get(1);
                    lastVO = rateInfoVOS.get(0);
                }else {
                    for(int i=0;i<fexRateInfoVOList.size();i++){
                        maxDate=fexRateInfoVOList.get(i).getEffectDate();
                        if(maxDate.compareTo(rateDate)==0){
                            firstVO = fexRateInfoVOList.get(i);
                            lastVO = fexRateInfoVOList.get(i);
                            break;
                        }else {
                            minDate = fexRateInfoVOList.get(i + 1).getEffectDate();
                            if (rateDate.compareTo(maxDate) < 0 && rateDate.compareTo(minDate) > 0) {
                                firstVO = fexRateInfoVOList.get(i + 1);
                                lastVO = fexRateInfoVOList.get(i);
                                break;
                            }
                        }
                    }
                }

            }else if(rateInfoVOS.size()==1){
                firstVO=  rateInfoVOS.get(0);
                lastVO= rateInfoVOS.get(0);
            } else {
                return fexRateInfoVO;
            }
            //俩计算日期
            Date date1=firstVO.getEffectDate();
            Date date2=lastVO.getEffectDate();
            //俩计算利率
            BigDecimal rate1=BigDecimal.ZERO;
            BigDecimal rate2=BigDecimal.ZERO;
            if("1".equals(type)){
                //买入价
                rate1=firstVO.getPurchaseRate();
                rate2=lastVO.getPurchaseRate();
            }else if("2".equals(type)){
                //卖出价
                rate1=firstVO.getSellRate();
                rate2=lastVO.getSellRate();
            }else if("3".equals(type)){
                //防止有的数据没处理好存入时
                if(EmptyUtils.isEmpty(firstVO.getMiddleRate())){
                    firstVO.setMiddleRate((firstVO.getPurchaseRate().add(firstVO.getSellRate())).divide(new BigDecimal(2)));
                }
                if(EmptyUtils.isEmpty(lastVO.getMiddleRate())){
                    lastVO.setMiddleRate((lastVO.getPurchaseRate().add(lastVO.getSellRate())).divide(new BigDecimal(2)));
                }
                //中间价
                rate1=firstVO.getMiddleRate();
                rate2=lastVO.getMiddleRate();
            }

            if(date1.compareTo(date2)==0){
                //俩数据的汇率日期相同
                returnBIg=rate1;
            }else  {
                //俩汇率的日期差
                int initDays=DateUtils.dayDiffs(date2,date1);
                //传入的时间与小值的日期差
                int days=DateUtils.dayDiffs(rateDate,date1);
                //相差的汇率
                BigDecimal diRate=((rate2.subtract(rate1)).divide(new BigDecimal(initDays), 8, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(days));
                returnBIg=rate1.add(diRate);
            }
        }
        if(null != latestFexRateInfoVO){
            fexRateInfoVO = new FexRateInfoVO();
            fexRateInfoVO.setCurEnCode(latestFexRateInfoVO.getCurEnCode());//标准币种
            fexRateInfoVO.setReferCurEnCode(latestFexRateInfoVO.getReferCurEnCode());//报价币种
            fexRateInfoVO.setEffectDate(latestFexRateInfoVO.getEffectDate());//发布时间
            // 损益等业务取报价时间用 returnTime；无返回时间时回退 effectDate
            Date quotationTime = latestFexRateInfoVO.getReturnTime() != null
                    ? latestFexRateInfoVO.getReturnTime() : latestFexRateInfoVO.getEffectDate();
            fexRateInfoVO.setReturnTime(quotationTime);
            fexRateInfoVO.setCur(latestFexRateInfoVO.getCur());
            fexRateInfoVO.setReferCur(latestFexRateInfoVO.getReferCur());
            fexRateInfoVO.setReferenceRate(returnBIg.setScale(6, BigDecimal.ROUND_HALF_UP));
        }
        return fexRateInfoVO;
    }

    /**
     *
     * 获取报价方式
     *
     * @param curId
     * @param referCurId
     * @param rateTypeId
     * @param exchangeDate
     * @param term
     * @return
     */
    public FexRateInfoVO getRateInfoByRateType(String curId,String referCurId,String rateTypeId,String exchangeDate,String term){
        //Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
        if (StringUtils.isBlank(rateTypeId)) {
            throw new ParameterException(ForeignMessageEnum.FEX_RATE_TYPE_IS_NULL);
        }
        //查看外汇牌价类型
        //todo
        FexRateTypeVO fexRateTypeVO = fexRateTypeService.getById(rateTypeId);
        FexRateInfoVO rateInfoByRateType = null;
        if(StringUtils.isNotBlank(curId) && StringUtils.isNotBlank(referCurId) && StringUtils.isNotBlank(rateTypeId)){
            //rateInfoByRateType = fexRateInfoDao.getRateInfoByRateType(currencyMap.get(curId).getSysCurId(), currencyMap.get(referCurId).getSysCurId(), rateTypeId, exchangeDate, term, null);
            rateInfoByRateType = fexRateInfoDao.getRateInfoByRateType(curId, referCurId, rateTypeId, exchangeDate, term, null);
        }
        if (EmptyUtils.isEmpty(rateInfoByRateType)) {
            //这里给出提示“交易货币对在外汇牌价查询的外汇牌价类型***中无报价，请先在外汇牌价查询中录入”
            throw new ParameterException(ForeignMessageEnum.FEX_RATE_INFO_IS_NULL, fexRateTypeVO.getTypeName());
        }
        return rateInfoByRateType;
    }



    /**
     *
     * 获取外汇牌价
     *
     * @param curId
     * @param referCurId
     * @param rateTypeId
     * @param exchangeDate
     * @param term
     * @return
     */
    public FexRateInfoVO getRateInfo(String curId,String referCurId,String rateTypeId,String exchangeDate,String term){
        //Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
        //查看外汇牌价类型
        //todo
        FexRateTypeVO fexRateTypeVO = fexRateTypeService.getById(rateTypeId);
        //FexRateInfoVO rateInfoByRateType = fexRateInfoDao.getRateInfoByRateType(currencyMap.get(curId).getSysCurId(),
        //        currencyMap.get(referCurId).getSysCurId(), rateTypeId, exchangeDate, term, null);
        FexRateInfoVO rateInfoByRateType = fexRateInfoDao.getRateInfoByRateType(curId,referCurId, rateTypeId, exchangeDate, term, null);
        if (EmptyUtils.isEmpty(rateInfoByRateType)) {
            //这里给出提示“交易货币对在外汇牌价查询的外汇牌价类型***中无报价，请先在外汇牌价查询中录入”
            throw new ParameterException(ForeignMessageEnum.FEX_RATE_INFO_IS_NULL, fexRateTypeVO.getTypeName());
        }
        return rateInfoByRateType;
    }

    /**
     *
     * 唯一key
     * (外汇牌价类型、同标准币种、同报价币种、同发布时间、和同汇率日期)
     *
     * @param vo
     * @return
     */
    private static String buildCompositeKey(FexRateInfoVO vo) {
        return String.join("|",
                vo.getRateTypeName(),
                vo.getCurName(),
                vo.getReferCurName(),
                DateUtils.format(vo.getEffectDate(), "yyyy-MM-dd HH:mm:ss"),
                DateUtils.format(vo.getExchangeRateDate(), "yyyy-MM-dd"),
                vo.getTerm()
        );
    }


    /**
     *
     * 校验日期格式
     *
     * @param dateStr
     * @param formatterStr
     */
    private static Boolean isValidDateFormat(String dateStr, String formatterStr ,FexRateInfoVO vo) {
        // 定义原始字符串的格式（注意Locale.US，避免月份缩写解析错误）
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(
                "EEE MMM dd HH:mm:ss zzz yyyy",
                Locale.US
        );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterStr);


        // 解析为ZonedDateTime（包含时区信息），再转换为LocalDateTime，最后格式化
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr, inputFormatter);
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        String result = localDateTime.format(formatter);
        try {
            LocalDateTime.parse(result, formatter);
            if (!EmptyUtils.isEmpty(vo)) {
                vo.setPublicationDate(result);
            }
            return true;
        } catch (DateTimeParseException e) {
            // 解析失败，格式错误
            return false;
        }
    }
}
