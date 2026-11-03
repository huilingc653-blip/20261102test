package com.bytter.report.domain.cms.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.*;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.UrationParamDTO;
import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.report.domain.cms.dao.RatBkStatisticsDao;
import com.bytter.report.domain.cms.vo.RatBkStatisticSearchVO;
import com.bytter.report.domain.cms.vo.RatBkStatisticVO;
import com.bytter.report.domain.cms.vo.RatUrationUseBeanVO;
import com.bytter.report.domain.cms.vo.RatUrationVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

@Service("ratBkStatisticsService")
@Transactional
public class RatBkStatisticsService extends BaseService<RatBkStatisticVO, String> {

    @Autowired
    private RatBkStatisticsDao ratBkStatisticsDao;
    @DubboReference
    private IMultiLangService multiLangServiceI;
    @DubboReference
    private IRatUrationService ratUrationService;

    @DubboReference
    private ICorpService corpService;
    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<RatBkStatisticVO, String> getEntityDao() {
        return ratBkStatisticsDao;
    }

    /**
     * 根据 单位,银行,授信结束日期 查询统计额度
     *
     * @param ratVO
     * @return
     */
    public List<RatBkStatisticVO> getRatBkAmountStatisticsList(RatBkStatisticSearchVO ratVO, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(RationConstant.CMS_REPORT_AMOUNT_QUERY, user.getId());

        if (EmptyUtils.isNotEmpty(ratVO.getRatCode())) {
            if (EmptyUtils.isEmpty(ratVO.getRatCode().trim())) {
                ratVO.setRatType(null);
            }
        }
        if (EmptyUtils.isNotEmpty(ratVO.getRatMode())) {
            if (EmptyUtils.isEmpty(ratVO.getRatMode().trim())) {
                ratVO.setRatMode(null);
            }
        }

        UrationParamDTO paramDTO = new UrationParamDTO();
        BeanUtils.copyNotNullProperties(ratVO, paramDTO);

        String corpId = ratVO.getCorpId();//保存每次查询的单位
        if (StringUtils.isEmpty(ratVO.getCorpId())) {//未选择查询的单位则默认查询当前用户下所有单位的
            String tempStr = StringUtils.listTtoString(corpIds);
            paramDTO.setCorpId(tempStr);
        }

        if (EmptyUtils.isNotEmpty(ratVO.getBankTypeId())) {
            paramDTO.setBankPrefix(ratVO.getBankTypeId());
            paramDTO.setBankCtlType(RationConstant.RAT_BANKTYPE_SAME);
        }
        if (!EmptyUtils.isEmpty(ratVO.getBeginDate())) {
            paramDTO.setgBeginDate(ratVO.getBeginDate());
        }
        if (!EmptyUtils.isEmpty(ratVO.getEndDate())) {
            paramDTO.setlBeginDate(ratVO.getEndDate());
        }
        if (!EmptyUtils.isEmpty(ratVO.getBeginDate2())) {
            paramDTO.setgEndDate(ratVO.getBeginDate2());
        }
        if (!EmptyUtils.isEmpty(ratVO.getEndDate2())) {
            paramDTO.setlEndDate(ratVO.getEndDate2());
        }

        paramDTO.setStatus(RationConstant.STATUS_CHECK + "," + RationConstant.STATUS_INVALID);//授信额度明细表可查已作废的数据
        paramDTO.setMoneyType(RationConstant.COMMON_MONEYUNIT_1);//元
        List<RatUrationUseVO> rations = ratUrationService.queryRationUseDtl(paramDTO);
        if (EmptyUtils.isEmptyList(rations)) {
            return new ArrayList<RatBkStatisticVO>();
        }

        Map<String, String> disRatMap = new HashMap<>();
        List<RatBkStatisticVO> ratBkList = new ArrayList<>();
        for (RatUrationUseVO urationUseVO : rations) {
            RatBkStatisticVO statisticVO = new RatBkStatisticVO();
            BeanUtils.copyNotNullProperties(urationUseVO, statisticVO);
            List<com.bytter.credit.vo.RatUrationUseDtlVO> rationDlts = urationUseVO.getRationDlts();//金额获取途径
            BigDecimal money = BigDecimal.ZERO;
            BigDecimal useMoney = BigDecimal.ZERO;
            BigDecimal approveMoney = BigDecimal.ZERO;
            BigDecimal virtualMaxMoney = BigDecimal.ZERO;
            //虚拟在途占用的最高额度
            BigDecimal virtualUrationMaxMoney = BigDecimal.ZERO;
            BigDecimal leftMoney = BigDecimal.ZERO;
            BigDecimal overPlusMoney = BigDecimal.ZERO;
            List<RatUrationUseDtlVO> listdtl = BeanUtils.copyListProperties(rationDlts, RatUrationUseDtlVO.class);
            for (RatUrationUseDtlVO urationUseDtlVO : listdtl) {
                money = money.add(urationUseDtlVO.getMoney());//总额
                useMoney = useMoney.add(urationUseDtlVO.getUseMoney());//占用
                approveMoney = approveMoney.add(urationUseDtlVO.getApproveMoney());//在途
                virtualMaxMoney = virtualMaxMoney.add(urationUseDtlVO.getVirtualMaxMoney());//虚拟占用
                virtualUrationMaxMoney = virtualUrationMaxMoney.add(urationUseDtlVO.getVirtualUrationMaxMoney());//虚拟在途
                leftMoney = leftMoney.add(urationUseDtlVO.getLeftMoney());//剩余
                overPlusMoney = overPlusMoney.add(urationUseDtlVO.getOverPlusMoney());//可用
            }
            statisticVO.setMoney(money);
            statisticVO.setMoneyStr(String.valueOf(money));
            statisticVO.setUseMoney(useMoney);
            statisticVO.setFreezeMoney(approveMoney);
            statisticVO.setVirtualUration(virtualMaxMoney);
            statisticVO.setReliefAndUrationMoney(virtualUrationMaxMoney);
            statisticVO.setLeftMoney(leftMoney);
            statisticVO.setOverPlusMoney(overPlusMoney);
            if (ObjectUtil.isNotEmpty(statisticVO.getBeginDate())) {
                statisticVO.setBeginDateStr(DateUtil.format(statisticVO.getBeginDate(), "yyyy-MM-dd"));
            }
            if (ObjectUtil.isNotEmpty(statisticVO.getEndDate())) {
                statisticVO.setEndDateStr(DateUtil.format(statisticVO.getEndDate(), "yyyy-MM-dd"));
            }
            ratBkList.add(statisticVO);
        }

        //ratBkList = ratBkStatisticsDao.getRatBkAmountStatisticsList(ratVO, corpIds);
        if (EmptyUtils.isEmptyList(ratBkList)) {
            ratBkList = new ArrayList<RatBkStatisticVO>();
            return ratBkList;
        }

        Map<String, DictDTO> disWayMap = dictService.listMap("RatBkrationDis.DistributeWay");
        Map<String, DictDTO> ratModeMap = dictService.listMap("RatBkration.RatMode");
        Map<String, DictDTO> rationBillStatusMap = dictService.listMap("ration.billStatus");
        Map<String, RatBkStatisticVO> ratMap = new HashMap<String, RatBkStatisticVO>();
        List<String> ratCodeList = new ArrayList<>();
        for (RatBkStatisticVO vo : ratBkList) {
            ratCodeList.add(vo.getRatCode());
        }
        //根据ratCode获取已结转占用数据
        Map<String, RatUrationVO> ratUrationMap = this.getListByRatCodes(ratCodeList);
        List<RatBkStatisticVO> returnList = new ArrayList<>();
        if (null != ratVO.getStatus() && !StringUtils.isBlank(ratVO.getStatus())) {
            for (RatBkStatisticVO bean : ratBkList) {
                checkBillStauts(bean, ratUrationMap, rationBillStatusMap);
                if (ratVO.getStatus().equals(bean.getStatus())) {
                    returnList.add(bean);
                }
            }
        } else {
            for (RatBkStatisticVO bean : ratBkList) {
                checkBillStauts(bean, ratUrationMap, rationBillStatusMap);
                returnList.add(bean);
            }
        }
        if (EmptyUtils.isEmpty(returnList)) {
            return new ArrayList<>();
        }
        for (RatBkStatisticVO vo : returnList) {
            String disWay = vo.getDisWay();
            if (EmptyUtils.isNotEmpty(disWay)) {
                DictDTO disDTO = disWayMap.get(disWay);
                if (!EmptyUtils.isEmpty(disDTO)) {
                    vo.setDisWayName(disDTO.getName());
                }
            }
            String ratMode = vo.getRatMode();
            if (EmptyUtils.isNotEmpty(ratMode)) {
                DictDTO ratDTO = ratModeMap.get(ratMode);
                if (!EmptyUtils.isEmpty(ratDTO)) {
                    vo.setRatModeName(ratDTO.getName());
                }
            }

            String ratCode = vo.getRatCode();
            //单位金额转换
            setBeanMoneyType(vo, Integer.valueOf(ratVO.getMoneyType()));
            // 在审占用额度+追减
            BigDecimal approveMoney = vo.getFreezeMoney();
            // 在审占用
            vo.setApproveMoney(approveMoney);
            String rationDateStr = DateUtils.date2Str(vo.getBeginDate(), DateUtils.date_sdf()) + multiLangServiceI.getLang("table.relationBank.dateRange") + DateUtils.date2Str(vo.getEndDate(), DateUtils.date_sdf());
            vo.setRationDateStr(rationDateStr);
            vo.setMoneyStr(NumberUtil.formatNumeric(vo.getMoney()));
            RatBkStatisticVO ratBean = ratMap.get(vo.getCurCode());
            String beginDateTemp = DateUtils.date2Str(vo.getBeginDate(), DateUtils.date_sdf());
            String endDateTemp = DateUtils.date2Str(vo.getEndDate(), DateUtils.date_sdf());
            if (ratBean == null) {
                ratBean = new RatBkStatisticVO();
                ratBean.setFlagSign("total");//合计
                ratBean.setCorpName(vo.getCurName() + "_" + multiLangServiceI.getLang("table.datatable.total") + ":");
                ratBean.setCurName(vo.getCurName());
                ratBean.setBankName(vo.getBankName());
                ratBean.setMoney(vo.getMoney());
                ratBean.setMoneyStr(String.valueOf(vo.getMoney()));
                ratBean.setApproveMoney(vo.getApproveMoney());
                ratBean.setOverPlusMoney(vo.getOverPlusMoney());
                ratBean.setLeftMoney(vo.getLeftMoney());
                ratBean.setUseMoney(vo.getUseMoney());
                ratBean.setCorpId(vo.getCorpId());
                ratBean.setBankCode(vo.getBankCode());
                ratBean.setEndDate(vo.getEndDate());
                ratBean.setBeginDate(vo.getBeginDate());
                ratBean.setBeginDateStr(beginDateTemp);
                ratBean.setEndDateStr(endDateTemp);
                ratBean.setRatCode(vo.getRatCode());
                ratBean.setAssurepercent(vo.getAssurepercent());
                ratBean.setProcedureFeeRate(vo.getProcedureFeeRate());
                ratMap.put(vo.getCurCode(), ratBean);
                if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disWay)) {
                    String ratCodeStr = disRatMap.get(ratCode);
                    if (EmptyUtils.isEmpty(ratCodeStr)) {
                        disRatMap.put(ratCode, ratCode);
                    }
                }
            } else {
                //判断是否共享的数据
                if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disWay)) {
                    String ratCodeStr = disRatMap.get(ratCode);
                    if (EmptyUtils.isEmpty(ratCodeStr)) {
                        ratBean.setMoney(ratBean.getMoney().add(vo.getMoney()));
                        ratBean.setLeftMoney(ratBean.getLeftMoney().add(vo.getLeftMoney()));
                        ratBean.setOverPlusMoney(ratBean.getOverPlusMoney().add(vo.getOverPlusMoney()));
                        disRatMap.put(ratCode, ratCode);
                    }
                } else {
                    ratBean.setMoney(ratBean.getMoney().add(vo.getMoney()));
                    ratBean.setLeftMoney(ratBean.getLeftMoney().add(vo.getLeftMoney()));
                    ratBean.setOverPlusMoney(ratBean.getOverPlusMoney().add(vo.getOverPlusMoney()));
                }

                ratBean.setApproveMoney(ratBean.getApproveMoney().add(vo.getApproveMoney()));
                ratBean.setUseMoney(ratBean.getUseMoney().add(vo.getUseMoney()));
                ratBean.setCorpId(ratBean.getCorpId() + "_" + vo.getCorpId());
                ratBean.setBankCode(ratBean.getBankCode() + "_" + vo.getBankCode());
                ratBean.setEndDate(vo.getEndDate());
                ratBean.setBeginDate(vo.getBeginDate());
                ratBean.setBeginDateStr(ratBean.getBeginDateStr() + "_" + beginDateTemp);
                ratBean.setEndDateStr(ratBean.getEndDateStr() + "_" + endDateTemp);
                ratBean.setRatCode(ratBean.getRatCode() + "_" + vo.getRatCode());
            }
            //设置code值
            ratBean.setCurCode(vo.getCurCode());
        }

        for (Entry<String, RatBkStatisticVO> entry : ratMap.entrySet()) {
            RatBkStatisticVO bean = entry.getValue();
            if (StringUtils.isNotBlank(bean.getMoneyStr())) {
                bean.setMoneyStr(String.valueOf(bean.getMoney()));
            }
            returnList.add(bean);
        }
        return returnList;
    }

    private void checkBillStauts(RatBkStatisticVO bean, Map<String, RatUrationVO> ratUrationMap, Map<String, DictDTO> rationBillStatusMap) {
        // 先判断授信额度录入状态是否为关闭和关闭审批中
        if (String.valueOf(RationConstant.STATUS_WAIT_CLOSE_CHECK).equals(bean.getStatus())) {
            bean.setStatus(RationConstant.RAT_BILL_STATUS_CLOSE_APPROVE);
            bean.setStatusStr(rationBillStatusMap.get(RationConstant.RAT_BILL_STATUS_CLOSE_APPROVE).getName());
        } else if (String.valueOf(RationConstant.STATUS_CLOSE).equals(bean.getStatus())) {
            bean.setStatus(RationConstant.RAT_BILL_STATUS_CLOSE);
            bean.setStatusStr(rationBillStatusMap.get(RationConstant.RAT_BILL_STATUS_CLOSE).getName());
        } else if (bean.getEndDate().before(new Date())) {
            // 再判断是否已经过期
            bean.setStatus(RationConstant.RAT_BILL_STATUS_END_DATE);
            bean.setStatusStr(rationBillStatusMap.get(RationConstant.RAT_BILL_STATUS_END_DATE).getName());
        } else if (ratUrationMap.containsKey(bean.getRatCode())) {
            //判断是否已经结转、部分结转
            RatUrationVO ratUration = ratUrationMap.get(bean.getRatCode());
            //结转额度小于授信额度说明是部分结转，等于则是全部结转
            if (bean.getMoney().compareTo(ratUration.getMoney()) == 1) {
                bean.setStatus(RationConstant.RAT_BILL_STATUS_PARTIAL_CARRY_OVER);
                bean.setStatusStr(rationBillStatusMap.get(RationConstant.RAT_BILL_STATUS_PARTIAL_CARRY_OVER).getName());
            } else if (bean.getMoney().compareTo(ratUration.getMoney()) == 0) {
                bean.setStatus(RationConstant.RAT_BILL_STATUS_ALL_CARRY_OVER);
                bean.setStatusStr(rationBillStatusMap.get(RationConstant.RAT_BILL_STATUS_ALL_CARRY_OVER).getName());
            }
        } else {
            //判断是否占用未使用、部分使用、全部使用
            if (bean.getUseMoney().compareTo(BigDecimal.ZERO) == 0) {
                bean.setStatus(RationConstant.RAT_BILL_STATUS_UNUSED);
                bean.setStatusStr(rationBillStatusMap.get(RationConstant.RAT_BILL_STATUS_UNUSED).getName());
            } else if (bean.getMoney().compareTo(bean.getUseMoney()) == 0) {
                bean.setStatus(RationConstant.RAT_BILL_STATUS_ALL);
                bean.setStatusStr(rationBillStatusMap.get(RationConstant.RAT_BILL_STATUS_ALL).getName());
            } else if (bean.getMoney().compareTo(bean.getUseMoney()) == 1) {
                bean.setStatus(RationConstant.RAT_BILL_STATUS_PARTIAL_USE);
                bean.setStatusStr(rationBillStatusMap.get(RationConstant.RAT_BILL_STATUS_PARTIAL_USE).getName());
            }
        }
    }

    public Map<String, RatUrationVO> getListByRatCodes(List<String> ratCodeList) {
        Map<String, RatUrationVO> ratUrationMap = new HashMap<>();
        if (CollectionUtils.isEmpty(ratCodeList)) {
            return ratUrationMap;
        }
        List<RatUrationVO> listByRatCodes = ratBkStatisticsDao.getListByRatCodes(ratCodeList);
        if (!CollectionUtils.isEmpty(listByRatCodes)) {
            for (RatUrationVO item : listByRatCodes) {
                ratUrationMap.put(item.getRatCode(), item);
            }
        }
        return ratUrationMap;
    }

    /**
     * 根据 单位,银行,授信结束日期 查询统计额度
     *
     * @param ratVO
     * @return
     */
    public List<RatBkStatisticVO> getMobileData(RatBkStatisticSearchVO ratVO, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(RationConstant.CMS_REPORT_AMOUNT_QUERY, user.getId());
        List<RatBkStatisticVO> ratBkList = ratBkStatisticsDao.getRatBkAmountStatisticsList(ratVO, corpIds);
        if (EmptyUtils.isEmptyList(ratBkList)) {
            ratBkList = new ArrayList<RatBkStatisticVO>();
            return ratBkList;
        }
        for (RatBkStatisticVO vo : ratBkList) {
            //单位金额转换
            setBeanMoneyType(vo, Integer.valueOf(ratVO.getMoneyType()));
            // 在审占用额度+追减
            BigDecimal approveMoney = vo.getFreezeMoney();
            // 在审占用
            vo.setApproveMoney(approveMoney);
            // 可用额度
            vo.setOverPlusMoney(vo.getMoney().subtract(vo.getUseMoney()).subtract(approveMoney)
                    .subtract(vo.getVirtualRelief()).subtract(vo.getVirtualUration())
                    .subtract(vo.getReliefAndUrationMoney()));
            //剩余额度 = 在审+可用
            vo.setLeftMoney(vo.getApproveMoney().add(vo.getOverPlusMoney()).add(vo.getVirtualRelief())
                    .add(vo.getVirtualUration()));
            String rationDateStr = DateUtils.date2Str(vo.getBeginDate(), DateUtils.date_sdf()) + multiLangServiceI.getLang("table.relationBank.dateRange") + DateUtils.date2Str(vo.getEndDate(), DateUtils.date_sdf());
            vo.setRationDateStr(rationDateStr);
            vo.setMoneyStr(NumberUtil.formatNumeric(vo.getMoney()));
        }

        return ratBkList;
    }

    /**
     * 金额单位转换
     *
     * @param ratVO
     * @param moneyType
     */
    private void setBeanMoneyType(RatBkStatisticVO ratVO, Integer moneyType) {
        if (RationConstant.COMMON_MONEYUNIT_1.equals(moneyType)) {
            return;
        }
        Class userCla = (Class) ratVO.getClass();
        /*
         * 得到类中的所有属性集合
         */
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); //设置些属性是可以访问的
                Object val = f.get(ratVO);// 得到此属性的值

                String type = f.getType().toString();// 得到此属性的类型
                if (type.endsWith("BigDecimal")) {
                    if (null == val) {
                        f.set(ratVO, BigDecimal.ZERO);
                    } else {
                        BigDecimal value = (BigDecimal) val;
                        f.set(ratVO, value.divide(BigDecimal.valueOf(RationConstant.COMMON_MONEYUNIT_10000), 2, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 功能介绍 : 查询授信占用情况。<br>
     * <b>日期：</b>2018年3月16日<br>
     *
     * @version 1.0
     * @author yangl
     */
    public List<RatUrationUseBeanVO> queryCorpRationUseDtl(RatBkStatisticSearchVO searchVO) {
        List<RatUrationUseBeanVO> result = new ArrayList<RatUrationUseBeanVO>();
        if ("total".equals(searchVO.getFlagSign())) {//查看合计明细
            String[] bankCode = searchVO.getBankCode().split("_");
            String[] corpId = searchVO.getCorpId().split("_");
            String[] beginDate = searchVO.getBeginDateStr().split("_");
            String[] endDate = searchVO.getEndDateStr().split("_");
            String[] ratCode = searchVO.getRatCode().split("_");
            for (int i = 0; i < bankCode.length; i++) {
                RatBkStatisticSearchVO bean = new RatBkStatisticSearchVO();
                bean.setCurCode(searchVO.getCurCode());
                bean.setCorpId(corpId[i]);
                bean.setBankCode(bankCode[i]);
                bean.setBeginDate(DateUtils.parse(beginDate[i], DateUtils.PATTERN_CLASSICAL_SIMPLE));
                bean.setEndDate(DateUtils.parse(endDate[i], DateUtils.PATTERN_CLASSICAL_SIMPLE));
                bean.setRatCode(ratCode[i]);
                result.addAll(ratBkStatisticsDao.queryCorpRationUseDtl(bean));
            }
        } else {
            result.addAll(ratBkStatisticsDao.queryCorpRationUseDtl(searchVO));
        }
        return result;
    }

}
