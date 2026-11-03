package com.bytter.credit.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatBkStatisticsDao;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.UrationParamDTO;
import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
@Service("ratBkStatisticsService")
@Transactional
public class RatBkStatisticsService extends BaseService<RatBkStatisticVO, String> {
    @Autowired
    private RatBkStatisticsDao ratBkStatisticsDao;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired(required = false)
    private RatUrationService ratUrationService;
    @DubboReference
    private ICorpService corpService;
    @DubboReference
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
        List<String> corpIds = corpService.getUserCorpIds(RationConstant.CMS_RATION_RATIONQUERY_QUERY, user.getId());
        // List<RatBkStatisticVO> ratBkList = ratBkStatisticsDao.getRatBkAmountStatisticsList(ratVO, corpIds);
        UrationParamDTO paramDTO = new UrationParamDTO();
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
        String corpId = ratVO.getCorpId();// 保存每次查询的单位
        if (StringUtils.isEmpty(ratVO.getCorpId())) {// 未选择查询的单位则默认查询当前用户下所有单位的
            String tempStr = StringUtils.listTtoString(corpIds);
            paramDTO.setCorpId(tempStr);
        }
        BeanUtils.copyNotNullProperties(ratVO, paramDTO);
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
        paramDTO.setStatus(RationConstant.STATUS_CHECK + "," + RationConstant.STATUS_INVALID);// 授信额度明细表可查已作废的数据
        paramDTO.setMoneyType(RationConstant.COMMON_MONEYUNIT_1);// 元
        List<RatUrationUseVO> rations = ratUrationService.queryRationUseDtl(paramDTO);
        if (EmptyUtils.isEmptyList(rations)) {
            return new ArrayList<RatBkStatisticVO>();
        }
        Map<String, String> disRatMap = new HashMap<>();
        List<RatBkStatisticVO> ratBkList = new ArrayList<>();
        for (RatUrationUseVO urationUseVO : rations) {
            RatBkStatisticVO statisticVO = new RatBkStatisticVO();
            BeanUtils.copyNotNullProperties(urationUseVO, statisticVO);
            List<RatUrationUseDtlVO> rationDlts = urationUseVO.getRationDlts();
            BigDecimal money = BigDecimal.ZERO;
            BigDecimal useMoney = BigDecimal.ZERO;
            BigDecimal approveMoney = BigDecimal.ZERO;
            BigDecimal virtualMaxMoney = BigDecimal.ZERO;
            // 虚拟在途占用的最高额度
            BigDecimal virtualUrationMaxMoney = BigDecimal.ZERO;
            BigDecimal leftMoney = BigDecimal.ZERO;
            BigDecimal overPlusMoney = BigDecimal.ZERO;
            for (RatUrationUseDtlVO urationUseDtlVO : rationDlts) {
                money = money.add(urationUseDtlVO.getMoney());// 总额
                useMoney = useMoney.add(urationUseDtlVO.getUseMoney());// 占用
                approveMoney = approveMoney.add(urationUseDtlVO.getApproveMoney());// 在途
                virtualMaxMoney = virtualMaxMoney.add(urationUseDtlVO.getVirtualMaxMoney());// 虚拟占用
                virtualUrationMaxMoney = virtualUrationMaxMoney.add(urationUseDtlVO.getVirtualUrationMaxMoney());// 虚拟在途
                leftMoney = leftMoney.add(urationUseDtlVO.getLeftMoney());// 剩余
                overPlusMoney = overPlusMoney.add(urationUseDtlVO.getOverPlusMoney());// 可用
            }
            statisticVO.setMoney(money);
            statisticVO.setUseMoney(useMoney);
            statisticVO.setFreezeMoney(approveMoney);
            statisticVO.setVirtualUration(virtualMaxMoney);
            statisticVO.setReliefAndUrationMoney(virtualUrationMaxMoney);
            statisticVO.setLeftMoney(leftMoney);
            statisticVO.setOverPlusMoney(overPlusMoney);
            ratBkList.add(statisticVO);
        }
        // ratBkList = ratBkStatisticsDao.getRatBkAmountStatisticsList(ratVO, corpIds);
        if (EmptyUtils.isEmptyList(ratBkList)) {
            ratBkList = new ArrayList<RatBkStatisticVO>();
            return ratBkList;
        }
        Map<String, DictDTO> disWayMap = dictService.listMap("RatBkrationDis.DistributeWay");
        Map<String, DictDTO> ratModeMap = dictService.listMap("RatBkration.RatMode");
        Map<String, RatBkStatisticVO> ratMap = new HashMap<String, RatBkStatisticVO>();
        for (RatBkStatisticVO vo : ratBkList) {
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
            // 单位金额转换
            setBeanMoneyType(vo, Integer.valueOf(ratVO.getMoneyType()));
            // 在审占用额度+追减
            BigDecimal approveMoney = vo.getFreezeMoney();
            // 在审占用
            vo.setApproveMoney(approveMoney);
            // 可用额度
            /*
             * vo.setOverPlusMoney(vo.getMoney().subtract(vo.getUseMoney()).subtract(approveMoney)
             * .subtract(vo.getVirtualRelief()).subtract(vo.getVirtualUration())
             * .subtract(vo.getReliefAndUrationMoney())); //剩余额度 = 在审+可用
             * vo.setLeftMoney(vo.getApproveMoney().add(vo.getOverPlusMoney()).add(vo.getVirtualRelief())
             * .add(vo.getVirtualUration()));
             */
            String rationDateStr = DateUtils.date2Str(vo.getBeginDate(), DateUtils.date_sdf())
                    + multiLangServiceI.getLang("table.relationBank.dateRange")
                    + DateUtils.date2Str(vo.getEndDate(), DateUtils.date_sdf());
            vo.setRationDateStr(rationDateStr);
            vo.setMoneyStr(NumberUtil.formatNumeric(vo.getMoney()));
            RatBkStatisticVO ratBean = ratMap.get(vo.getCurCode());
            String beginDateTemp = DateUtils.date2Str(vo.getBeginDate(), DateUtils.date_sdf());
            String endDateTemp = DateUtils.date2Str(vo.getEndDate(), DateUtils.date_sdf());
            if (ratBean == null) {
                ratBean = new RatBkStatisticVO();
                ratBean.setFlagSign("total");// 合计
                ratBean.setCorpName(vo.getCurName() + "_" + multiLangServiceI.getLang("table.datatable.total") + ":");
                ratBean.setCurName(vo.getCurName());
                ratBean.setBankName(vo.getBankName());
                ratBean.setMoney(vo.getMoney());
                ratBean.setApproveMoney(vo.getApproveMoney());
                ratBean.setOverPlusMoney(vo.getOverPlusMoney());
                ratBean.setLeftMoney(vo.getLeftMoney());
                ratBean.setUseMoney(vo.getUseMoney());
                ratBean.setCorpId(vo.getCorpId());
                ratBean.setBankCode(vo.getBankCode());
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
                // 判断是否共享的数据
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
                ratBean.setBeginDateStr(ratBean.getBeginDateStr() + "_" + beginDateTemp);
                ratBean.setEndDateStr(ratBean.getEndDateStr() + "_" + endDateTemp);
                ratBean.setRatCode(ratBean.getRatCode() + "_" + vo.getRatCode());
            }
            // 设置code值
            ratBean.setCurCode(vo.getCurCode());
        }
        for (Entry<String, RatBkStatisticVO> entry : ratMap.entrySet()) {
            RatBkStatisticVO bean = entry.getValue();
            ratBkList.add(bean);
        }
        return ratBkList;
    }
    /**
     * 根据 单位,银行,授信结束日期 查询统计额度
     *
     * @param ratVO
     * @return
     */
    public List<RatBkStatisticVO> getMobileData(RatBkStatisticSearchVO ratVO, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(RationConstant.CMS_RATION_RATIONQUERY_QUERY, user.getId());
        List<RatBkStatisticVO> ratBkList = ratBkStatisticsDao.getRatBkAmountStatisticsList(ratVO, corpIds);
        if (EmptyUtils.isEmptyList(ratBkList)) {
            ratBkList = new ArrayList<RatBkStatisticVO>();
            return ratBkList;
        }
        for (RatBkStatisticVO vo : ratBkList) {
            // 单位金额转换
            setBeanMoneyType(vo, Integer.valueOf(ratVO.getMoneyType()));
            // 在审占用额度+追减
            BigDecimal approveMoney = vo.getFreezeMoney();
            // 在审占用
            vo.setApproveMoney(approveMoney);
            // 可用额度
            vo.setOverPlusMoney(
                    vo.getMoney().subtract(vo.getUseMoney()).subtract(approveMoney).subtract(vo.getVirtualRelief())
                            .subtract(vo.getVirtualUration()).subtract(vo.getReliefAndUrationMoney()));
            // 剩余额度 = 在审+可用
            vo.setLeftMoney(vo.getApproveMoney().add(vo.getOverPlusMoney()).add(vo.getVirtualRelief())
                    .add(vo.getVirtualUration()));
            String rationDateStr = DateUtils.date2Str(vo.getBeginDate(), DateUtils.date_sdf())
                    + multiLangServiceI.getLang("table.relationBank.dateRange")
                    + DateUtils.date2Str(vo.getEndDate(), DateUtils.date_sdf());
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
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = f.get(ratVO);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                if (type.endsWith("BigDecimal")) {
                    if (null == val) {
                        f.set(ratVO, BigDecimal.ZERO);
                    } else {
                        BigDecimal value = (BigDecimal) val;
                        f.set(ratVO, value.divide(BigDecimal.valueOf(RationConstant.COMMON_MONEYUNIT_10000), 2,
                                BigDecimal.ROUND_HALF_UP));
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
        if ("total".equals(searchVO.getFlagSign())) {// 查看合计明细
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
