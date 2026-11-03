package com.bytter.credit.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatBkUseStatisticsDao;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.UrationParamDTO;
import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.CorpDTO;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service("ratBkUseStatisticsSercive")
@Transactional
public class RatBkUseStatisticsSercive extends BaseService<RatBkStatisticVO, String> {
    @Autowired
    private RatBkUseStatisticsDao ratBkUseStatisticsDao;
    @DubboReference
    private ICorpService corpService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference
    private IDictService dictService;
    @Autowired
    private RatBkrationDetailService ratBkrationDetailService;
    @Autowired
    private RatBkrationDisService ratBkrationDisService;
    @DubboReference
    private IBtCurrencyService currencyService;
    @DubboReference
    private IFcExcrateAdjustService rateAdjustService;
    @Autowired(required = false)
    private RatUrationService ratUrationService;
    @Autowired(required = false)
    private RatBkrationReliefDetailService reliefDetailService;
    @Autowired(required = false)
    private RatBkrationAddDetailService addDetailService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookServiceImp;
    @Override
    public BaseDao<RatBkStatisticVO, String> getEntityDao() {
        return ratBkUseStatisticsDao;
    }
    public Map<String, RatUrationUseDtlVO> trun2BkUseVo(List<RatUrationUseVO> rations,
                                                        Map<String, RatUrationUseDtlVO> disRatMap, Map<String, RatUrationUseDtlVO> disCorpMap,
                                                        String queryItemCode) {
        Map<String, RatUrationUseDtlVO> ratuMap = new HashMap<>();
        List<RatBkUseVO> ratLists = new ArrayList<>();
        if (EmptyUtils.isEmptyList(rations)) {
            return ratuMap;
        }
        for (RatUrationUseVO urationUseVO : rations) {
            List<RatUrationUseDtlVO> rationDlts = urationUseVO.getRationDlts();
            for (RatUrationUseDtlVO urationUseDtlVO : rationDlts) {
                String disCode = urationUseDtlVO.getDisCode();
                String itemFlag = urationUseDtlVO.getItemFlag();
                RatUrationUseDtlVO ratUseDtlVO = disRatMap.get(disCode);// 分配总使用额度
                if (EmptyUtils.isEmpty(ratUseDtlVO)) {
                    ratUseDtlVO = new RatUrationUseDtlVO();
                    disRatMap.put(disCode, ratUseDtlVO);
                }
                BeanUtils.copyProperties(urationUseDtlVO, ratUseDtlVO, "money", "useMoney", "leftMoney", "approveMoney",
                        "overPlusMoney");
                if (EmptyUtils.isEmpty(queryItemCode)) {
                    ratUseDtlVO.setMoney(ratUseDtlVO.getMoney().add(urationUseDtlVO.getMoney()));
                    ratUseDtlVO.setUseMoney(ratUseDtlVO.getUseMoney().add(urationUseDtlVO.getUseMoney()));
                    ratUseDtlVO.setApproveMoney(ratUseDtlVO.getApproveMoney().add(urationUseDtlVO.getApproveMoney()));
                    ratUseDtlVO.setLeftMoney(ratUseDtlVO.getLeftMoney().add(urationUseDtlVO.getLeftMoney()));
                    ratUseDtlVO
                            .setOverPlusMoney(ratUseDtlVO.getOverPlusMoney().add(urationUseDtlVO.getOverPlusMoney()));
                    ratUseDtlVO.setRatCode(urationUseDtlVO.getRatCode());
                }
                RatUrationUseDtlVO ratCorpVO = disCorpMap.get(disCode);// 单位分配总使用额度
                if (EmptyUtils.isEmpty(ratCorpVO)) {
                    ratCorpVO = new RatUrationUseDtlVO();
                    disCorpMap.put(disCode, ratCorpVO);
                }
                ratCorpVO.setMoney(ratCorpVO.getMoney().add(urationUseDtlVO.getMoney()));
                ratCorpVO.setUseMoney(ratCorpVO.getUseMoney().add(urationUseDtlVO.getUseMoney()));
                ratCorpVO.setApproveMoney(ratCorpVO.getApproveMoney().add(urationUseDtlVO.getApproveMoney()));
                ratCorpVO.setLeftMoney(ratCorpVO.getLeftMoney().add(urationUseDtlVO.getLeftMoney()));
                ratCorpVO.setOverPlusMoney(ratCorpVO.getOverPlusMoney().add(urationUseDtlVO.getOverPlusMoney()));
                ratCorpVO.setRatCode(ratCorpVO.getRatCode());
                if (RationConstant.RAT_COMFLAG_1.equals(itemFlag)) {
                    List<RatUrationUseDtlVO> compRationDlts = urationUseDtlVO.getRationDlts();
                    for (RatUrationUseDtlVO useDtlVO : compRationDlts) {
                        String itemCode = useDtlVO.getItemCode();
                        RatBkUseVO bkUseVO = new RatBkUseVO();
                        BeanUtils.copyNotNullProperties(useDtlVO, bkUseVO);
                        ratLists.add(bkUseVO);
                        if (EmptyUtils.isNotEmpty(queryItemCode)) {
                            if (itemCode.equals(queryItemCode)) {
                                ratUseDtlVO.setMoney(ratUseDtlVO.getMoney().add(useDtlVO.getMoney()));
                                ratUseDtlVO.setUseMoney(ratUseDtlVO.getUseMoney().add(useDtlVO.getUseMoney()));
                                ratUseDtlVO.setApproveMoney(
                                        ratUseDtlVO.getApproveMoney().add(urationUseDtlVO.getApproveMoney()));
                                ratUseDtlVO.setLeftMoney(ratUseDtlVO.getLeftMoney().add(useDtlVO.getLeftMoney()));
                                ratUseDtlVO.setOverPlusMoney(
                                        ratUseDtlVO.getOverPlusMoney().add(useDtlVO.getOverPlusMoney()));
                                ratUseDtlVO.setRatCode(ratUseDtlVO.getRatCode());
                            }
                        }
                        ratuMap.put(disCode + "_" + itemCode, useDtlVO);
                    }
                } else {
                    String itemCode = urationUseDtlVO.getItemCode();
                    RatBkUseVO bkUseVO = new RatBkUseVO();
                    BeanUtils.copyNotNullProperties(urationUseDtlVO, bkUseVO);
                    ratLists.add(bkUseVO);
                    if (EmptyUtils.isNotEmpty(queryItemCode)) {
                        if (itemCode.equals(queryItemCode)) {
                            ratUseDtlVO.setMoney(ratUseDtlVO.getMoney().add(urationUseDtlVO.getMoney()));
                            ratUseDtlVO.setUseMoney(ratUseDtlVO.getUseMoney().add(urationUseDtlVO.getUseMoney()));
                            ratUseDtlVO.setApproveMoney(
                                    ratUseDtlVO.getApproveMoney().add(urationUseDtlVO.getApproveMoney()));
                            ratUseDtlVO.setLeftMoney(ratUseDtlVO.getLeftMoney().add(urationUseDtlVO.getLeftMoney()));
                            ratUseDtlVO.setOverPlusMoney(
                                    ratUseDtlVO.getOverPlusMoney().add(urationUseDtlVO.getOverPlusMoney()));
                            ratUseDtlVO.setRatCode(urationUseDtlVO.getRatCode());
                        }
                    }
                    ratuMap.put(disCode + "_" + itemCode, urationUseDtlVO);
                }
            }
        }
        return ratuMap;
    }
    /**
     * 授信使用统计
     *
     * @param ratVO
     * @return
     */
    public List<RatBkUseVO> getRatBkUseStatisticsList(RatBkStatisticSearchVO ratVO, String resource,
            ICurrentUser user) {
        // 当前用户查询单位权限
        List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
        // 查询统计数据
        //TODO 补充保证金项目id
        List<String> itemIds = fcBondHookServiceImp.getItemIds();
        List<RatBkUseVO> ratLists = ratBkUseStatisticsDao.queryCorpRatDisration(ratVO, corpAuth, itemIds);
        if (EmptyUtils.isEmptyList(ratLists)) {
            ratLists = new ArrayList<RatBkUseVO>();
        }
        if (EmptyUtils.isNotEmpty(ratVO.getCurCode())) {
            if (EmptyUtils.isEmpty(ratVO.getCurCode().trim())) {
                ratVO.setCurCode(null);
            }
        }
        UrationParamDTO paramDTO = new UrationParamDTO();
        String corpId = ratVO.getCorpId();// 保存每次查询的单位
        if (StringUtils.isEmpty(ratVO.getCorpId())) {// 未选择查询的单位则默认查询当前用户下所有单位的
            String tempStr = StringUtils.listTtoString(corpAuth);
            paramDTO.setCorpId(tempStr);
        }
        BeanUtils.copyNotNullProperties(ratVO, paramDTO);
        if (EmptyUtils.isNotEmpty(ratVO.getRatCode())) {
            if (EmptyUtils.isEmpty(ratVO.getRatCode().trim())) {
                // paramDTO.setItemCode(null);
            } else {
                // paramDTO.setItemCode(ratVO.getRatCode());
            }
            paramDTO.setRatCode(null);
        }
        paramDTO.setStatus(RationConstant.STATUS_CHECK + "," + RationConstant.STATUS_INVALID);// 授信额度明细表可查已作废的数据
        paramDTO.setMoneyType(RationConstant.COMMON_MONEYUNIT_1);// 元
        // 查询各授信的占用、在审额度数据
        List<RatUrationUseVO> rations = ratUrationService.queryRationUseDtl(paramDTO);
        Map<String, RatUrationUseDtlVO> disRatMap = new HashMap<>();// 保存每个授信分配合同下授信品种的数据（可能只包含部分品种）
        Map<String, RatUrationUseDtlVO> disCorpMap = new HashMap<>();// 保存每个单位授信分配合同下授信品种的数据
        Map<String, RatUrationUseDtlVO> ratuMap = trun2BkUseVo(rations, disRatMap, disCorpMap,
                ratVO.getRatCode().trim());
        Map<String, RatBkUseVO> useVOMap = new HashMap<>();// 保存原综合授信的授信额度
        for (RatBkUseVO vo : ratLists) {
            String disCode = vo.getDisCode();
            String itemCode = vo.getItemCode();
            RatBkUseVO useVO = new RatBkUseVO();
            BeanUtils.copyNotNullProperties(vo, useVO);
            RatBkUseVO ratBkUseVO = useVOMap.get(disCode);
            if (EmptyUtils.isEmpty(ratBkUseVO)) {
                useVOMap.put(disCode, useVO);
            }
            RatUrationUseDtlVO useDtlVO = ratuMap.get(disCode + "_" + itemCode);
            if (!EmptyUtils.isEmpty(useDtlVO)) {
                vo.setMoney(useDtlVO.getMoney());
                vo.setApproveMoney(useDtlVO.getApproveMoney());
                vo.setUseMoney(useDtlVO.getUseMoney());
                vo.setOverPlusMoney(useDtlVO.getOverPlusMoney());
                vo.setLeftMoney(useDtlVO.getLeftMoney());
            }
        }
        List<RatBkUseVO> ratList = new ArrayList<RatBkUseVO>();
        if (!EmptyUtils.isEmpty(ratVO.getRatCode()) && !EmptyUtils.isEmpty(ratVO.getRatCode().trim())) {
            for (RatBkUseVO vo : ratLists) {
                if (ratVO.getRatCode().equalsIgnoreCase(vo.getItemCode())) {
                    ratList.add(vo);
                }
            }
        } else {
            ratList = ratLists;
        }
        String moneyType = ratVO.getMoneyType();
        Map<String, RatBkUseCorpVO> totalMap = new HashMap<>();
        List<RatBkUseVO> beanList = new ArrayList<>();
        Map<String, RatBkUseCorpVO> subTotal = new HashMap<>();// 币别小计
        if (ratVO.getTypesOfStatistics().equals(RationConstant.SORT_TYPE_0)) {// 按单位统计
            beanList = dealDatas(ratList, moneyType, RationConstant.SORT_TYPE_0, disCorpMap, useVOMap); // 币别小计
            subTotal = calBankOrCorpSubTotal(beanList, RationConstant.SORT_TYPE_0, disRatMap, totalMap, moneyType);
        } else {// 按银行统计
            beanList = dealDatas(ratList, moneyType, RationConstant.SORT_TYPE_1, disCorpMap, useVOMap);
            subTotal = calBankOrCorpSubTotal(beanList, RationConstant.SORT_TYPE_1, disRatMap, totalMap, moneyType);// 币别小计
        }
        // Map<String,RatBkUseCorpVO> bankSubTotal=calBankSubTotal(beanList);
        // 设置银行、单位数
        setTotalSize(beanList);
        // 币别合计
        calCurTotal(subTotal, beanList, totalMap);
        return beanList;
    }
    /**
     * 将数据分组
     *
     * @param ratList
     * @param moneyType
     * @param sortType
     *            统计维度：0-单位 1-银行
     * @return
     */
    public List<RatBkUseVO> dealDatas(List<RatBkUseVO> ratList, String moneyType, String sortType,
            Map<String, RatUrationUseDtlVO> disRatMap, Map<String, RatBkUseVO> useVOMap) {
        List<CorpDTO> corpDTOList = corpService.listEffectiveCorp();
        Map<String, CorpDTO> corpDTOMap = Maps.uniqueIndex(corpDTOList, new Function<CorpDTO, String>() {
            @Override
            public String apply(CorpDTO corpDTO) {
                return corpDTO.getId();
            }
        });
        List<RatItemDisVO> ratItemDisVOS = ratBkrationDetailService.getAllItemAssure();
        Map<String, RatItemDisVO> ratItemDisVOMap = Maps.uniqueIndex(ratItemDisVOS,
                new Function<RatItemDisVO, String>() {
                    @Override
                    public String apply(RatItemDisVO ratItemDisVO) {
                        return ratItemDisVO.getRatCode() + "-" + ratItemDisVO.getItemCode();
                    }
                });
        String dateRange = multiLangServiceI.getLang("table.relationBank.dateRange");
        List<RatBkUseVO> beanList = new ArrayList<RatBkUseVO>();
        // 一级分组Map
        Map<String, RatBkUseVO> firstMap = new HashMap<String, RatBkUseVO>();
        // 二级分组Map
        Map<String, RatBkUseCorpVO> secondMap = new HashMap<String, RatBkUseCorpVO>();
        // 累计使用额度
        Map<String, BigDecimal> sumUseMoney = new HashMap<>();
        // 综合授信额度
        Map<String, BigDecimal> itemMoneyMap = new HashMap<>();
        for (RatBkUseVO bean : ratList) {
            // 金额单位转换
            // setBeanMoneyType(bean, Integer.valueOf(moneyType));
            String bankCode = bean.getBankCode();// 银行id
            String corpNo = bean.getCorpNo();// 单位id
            String curCode = bean.getCurCode();// 币别id
            String beginDate = DateUtils.date2Str(bean.getBeginDate(), DateUtils.date_sdf());// 开始时间
            String endDate = DateUtils.date2Str(bean.getEndDate(), DateUtils.date_sdf());// 结束时间
            String corpKey = bankCode + "_" + corpNo + "_" + curCode + "_" + beginDate + "_" + endDate;
            String firstMapKey = "";
            if (sortType.equals(RationConstant.SORT_TYPE_0)) {// 按单位统计
                firstMapKey = corpNo;
            } else {// 按银行统计
                firstMapKey = bankCode;
            }
            RatBkUseVO firstVO = firstMap.get(firstMapKey);
            if (firstVO == null) {
                firstVO = new RatBkUseVO();
                BeanUtils.copyProperties(bean, firstVO);
                firstMap.put(firstMapKey, firstVO);
                beanList.add(firstVO);
            }
            RatBkUseCorpVO secondVO = secondMap.get(corpKey);
            if (secondVO == null) {
                secondVO = new RatBkUseCorpVO();
                BeanUtils.copyProperties(bean, secondVO);
                secondVO.setId(bean.getDisCode());
                secondVO.setRatDateStr(DateUtils.date2Str(secondVO.getBeginDate(), DateUtils.date_sdf) + dateRange
                        + DateUtils.date2Str(secondVO.getEndDate(), DateUtils.date_sdf));
                secondVO.setRationDlts(new ArrayList<RatUrationUseDtlVO>());
                // 添加银行或单位明细
                secondMap.put(corpKey, secondVO);
                for (RatBkUseVO rat : ratList) {
                    RatItemDisVO ratItemDisVO;
                    if (null == ratItemDisVOMap.get(rat.getRatCode() + "-" + rat.getItemCode())) {
                        ratItemDisVO = new RatItemDisVO();
                        ratItemDisVO.setAssurepercent(BigDecimal.ZERO);
                        ratItemDisVO.setInterestrate(BigDecimal.ZERO);
                        ratItemDisVO.setProcedureFeeRate(BigDecimal.ZERO);
                    } else {
                        ratItemDisVO = ratItemDisVOMap.get(rat.getRatCode() + "-" + rat.getItemCode());
                    }
                    // RatItemDisVO ratItemDisVO =
                    // ratBkrationDetailService.getItemAssure(rat.getRatCode(),rat.getItemCode());
                    // 银行、单位、币别,起始结束时间一致添加明细
                    if (rat.getBankCode().equals(bankCode) && rat.getCorpNo().equals(corpNo)
                            && rat.getCurCode().equals(curCode) && secondVO.getBeginDate().equals(rat.getBeginDate())
                            && secondVO.getEndDate().equals(rat.getEndDate())) {
                        // 在审占用额度+追减
                        // BigDecimal approveMoney = rat.getFreezeMoney();
                        // rat.setApproveMoney(approveMoney);// 在审占用
                        // rat.setOverPlusMoney(rat.getMoney().subtract(rat.getUseMoney()).subtract(approveMoney)
                        // .subtract(rat.getVirtualRelief()).subtract(rat.getVirtualUration())
                        // .subtract(rat.getReliefAndUrationMoney()));// 可用额度
                        // //剩余额度 = 在审+可用(包含虚拟在途占用和虚拟在途调剂，这些不能在真实在途占用中)
                        // rat.setLeftMoney(rat.getApproveMoney().add(rat.getOverPlusMoney()).add(rat.getVirtualRelief())
                        // .add(rat.getVirtualUration()));
                        // 授信品种
                        if (RationConstant.RAT_COMFLAG_1.equals(rat.getItemFlag())) {// 综合授信
                            RatUrationUseDtlVO comRatDtl = new RatUrationUseDtlVO();
                            BeanUtils.copyProperties(rat, comRatDtl);
                            comRatDtl.setRatTotalMoney(rat.getMoney());// 授信总额
                            // comRatDtl.setMoney(rat.getMoney());//授信额度
                            // comRatDtl.setUseMoney(rat.getUseMoney());//占用额度
                            // comRatDtl.setApproveMoney(rat.getFreezeMoney());//在审占用
                            // comRatDtl.setOverPlusMoney(rat.getMoney().subtract(rat.getUseMoney()).subtract(rat.getFreezeMoney()));//
                            // 可用额度=分配额度-占用额度-冻结额度+追加额度
                            // comRatDtl.setLeftMoney(comRatDtl.getApproveMoney().add(comRatDtl.getOverPlusMoney()));//剩余额度
                            // = 在审+可用(剩余额度与可用额度相差在审占用额度)
                            if (sumUseMoney.containsKey(rat.getRatCode())) {
                                sumUseMoney.put(rat.getRatCode(),
                                        sumUseMoney.get(rat.getRatCode()).subtract(rat.getUseMoney()));
                            } else {
                                sumUseMoney.put(rat.getRatCode(), rat.getUseMoney());
                            }
                            // setBeanMoneyType(comRatDtl, Integer.valueOf(moneyType));
                            // 保证金比例和费率不需要格式化单位（万元）
                            comRatDtl.setAssurepercent(ratItemDisVO.getAssurepercent().multiply(new BigDecimal(100)));
                            comRatDtl.setProcedureFeeRate(
                                    ratItemDisVO.getProcedureFeeRate().multiply(new BigDecimal(100)));
                            secondVO.addRationDtl(comRatDtl);
                        } else {// 单品种
                            RatUrationUseDtlVO disDtlDTO = new RatUrationUseDtlVO();
                            BeanUtils.copyProperties(rat, disDtlDTO);
                            disDtlDTO.setRatTotalMoney(rat.getMoney());
                            disDtlDTO.setMaxMoney(rat.getMoney());
                            // disDtlDTO.setLeftMoney(rat.getMoney().subtract(rat.getUseMoney()));//剩余额度 = 授信额度-已使用额度
                            disDtlDTO.setRatCode(rat.getRatCode());
                            CorpDTO corpDTO = corpDTOMap.get(rat.getCorpId());
                            if (null != rat.getBondCurIdApply() && !rat.getCurCode().equals(rat.getBondCurIdApply())) {// 授信币别与保证金占用币别不一致
                                if (rat.getCurCode().equals(corpDTO.getCurId())) {// 授信币别为本位币,保证金额度乘汇率
                                    disDtlDTO.setBondApply(rat.getBondApply().multiply(rat.getExchangeRateApply())
                                            .setScale(2, BigDecimal.ROUND_HALF_UP));// 保证金（已执行）
                                } else {// 保证金币别为本位币，保证金额度除以汇率
                                    disDtlDTO.setBondApply(rat.getBondApply().divide(rat.getExchangeRateApply(), 2,
                                            BigDecimal.ROUND_HALF_UP));// 保证金（已执行）
                                }
                            } else {
                                disDtlDTO.setBondApply(rat.getBondApply());
                            }
                            if (null != rat.getBondCurIdAway() && !rat.getCurCode().equals(rat.getBondCurIdAway())) {// 授信币别与保证金在途币别不一致
                                if (rat.getCurCode().equals(corpDTO.getCurId())) {// 授信币别为本位币,保证金额度乘汇率
                                    disDtlDTO.setBondAway(rat.getBondAway().multiply(rat.getExchangeRateAway())
                                            .setScale(2, BigDecimal.ROUND_HALF_UP));// 保证金（在途）
                                } else {// 保证金币别为本位币，保证金额度除以汇率
                                    disDtlDTO.setBondAway(rat.getBondAway().divide(rat.getExchangeRateAway(), 2,
                                            BigDecimal.ROUND_HALF_UP));// 保证金（在途）
                                }
                            } else {
                                disDtlDTO.setBondAway(rat.getBondAway());
                            }
                            // setBeanMoneyType(disDtlDTO, Integer.valueOf(moneyType));
                            // 保证金比例和费率不需要格式化单位（万元）
                            disDtlDTO.setAssurepercent(ratItemDisVO.getAssurepercent().multiply(new BigDecimal(100)));
                            disDtlDTO.setProcedureFeeRate(
                                    ratItemDisVO.getProcedureFeeRate().multiply(new BigDecimal(100)));
                            secondVO.addRationDtl(disDtlDTO);
                        }
                    }
                }
                // setBeanMoneyType(secondVO, Integer.valueOf(moneyType));
                firstVO.addCorpDtl(secondVO);
            }
        }
        // 金额合计
        calCorpMoney(beanList, sumUseMoney, disRatMap, moneyType, useVOMap);
        return beanList;
    }
    /**
     * 计算单位金额
     *
     * @param beanList
     */
    public void calCorpMoney(List<RatBkUseVO> beanList, Map<String, BigDecimal> sumUseMoney,
            Map<String, RatUrationUseDtlVO> disRatMap, String moneyType, Map<String, RatBkUseVO> useVOMap) {
        // 单位金额
        for (RatBkUseVO vo : beanList) {
            List<RatBkUseCorpVO> corpList = vo.getCorpDtls();
            Map<String, RatBkUseCorpVO> corpMap = new HashMap<>();
            for (RatBkUseCorpVO corp : corpList) {
                String disWay = corp.getDisWay();// 分配方式
                /*
                 * if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disWay)) { String disCode = corp.getDisCode();//授信ID
                 * RatUrationUseDtlVO corpVO = disRatMap.get(disCode); if (!EmptyUtils.isEmpty(corpVO)) {
                 * corp.setMoney(corpVO.getMoney());//剩余额度合计=授信额度合计-使用额度
                 * corp.setLeftMoney(corpVO.getLeftMoney());//剩余额度合计=授信额度合计-使用额度
                 * corp.setUseMoney(corpVO.getUseMoney());//授信使用额度 continue; } }
                 */
                // 授信总额
                BigDecimal subTotalMoney = new BigDecimal(0.0);
                // 在审合计
                BigDecimal subTotalApproveMoney = new BigDecimal(0.0);
                // 可用额度合计
                BigDecimal subTotalOverPlusMoney = new BigDecimal(0.0);
                // 剩余额度合计
                BigDecimal subTotalLeftMoney = new BigDecimal(0.0);
                // 已使用合度合计
                BigDecimal subTotalUseMoney = new BigDecimal(0.0);
                // 保证金占用合计
                BigDecimal subTotalBondApplyMoney = new BigDecimal(0.0);
                // 保证金已使用合计
                BigDecimal subTotalBondAwayMoney = new BigDecimal(0.0);
                // 综合授信剩余额度合计
                BigDecimal comSubTotalLeftMoney = new BigDecimal(0.0);
                // 综合授信可用额度合计
                BigDecimal comSubTotalOverPlusMoney = new BigDecimal(0.0);
                List<RatUrationUseDtlVO> dtl = corp.getRationDlts();
                for (RatUrationUseDtlVO dtlVO : dtl) {
                    if (RationConstant.RAT_COMFLAG_1.equals(dtlVO.getItemFlag())) {// 综合品种只计算一次授信总额
                        if (dtl.size() == 1) {
                            subTotalMoney = subTotalMoney.add(dtlVO.getRatTotalMoney());
                            subTotalLeftMoney = subTotalLeftMoney.add(dtlVO.getLeftMoney());// 剩余额度
                            subTotalOverPlusMoney = subTotalOverPlusMoney.add(dtlVO.getOverPlusMoney());
                            comSubTotalLeftMoney = dtlVO.getRatTotalMoney();
                        } else {
                            if (null != dtlVO.getCompSign()
                                    && dtlVO.getCompSign().equals(RationConstant.RAT_COMP_SIGN_1)) {
                                // subTotalMoney = subTotalMoney.add(dtlVO.getMoney());
                                String disCode = corp.getDisCode();// 授信ID
                                RatBkUseVO ratBkUseVO = useVOMap.get(disCode);
                                subTotalMoney = subTotalMoney.add(ratBkUseVO.getMoney());
                                subTotalLeftMoney = subTotalLeftMoney.add(dtlVO.getLeftMoney());// 剩余额度
                                subTotalOverPlusMoney = subTotalOverPlusMoney.add(dtlVO.getOverPlusMoney());
                                comSubTotalLeftMoney = dtlVO.getRatTotalMoney();
                            }
                            // comSubTotalLeftMoney = comSubTotalLeftMoney.subtract(dtlVO.getUseMoney());
                            // comSubTotalOverPlusMoney = comSubTotalLeftMoney.subtract(dtlVO.getApproveMoney());
                        }
                    } else {
                        subTotalMoney = subTotalMoney.add(dtlVO.getRatTotalMoney());
                        subTotalLeftMoney = subTotalLeftMoney.add(dtlVO.getLeftMoney());// 剩余额度
                        subTotalOverPlusMoney = subTotalOverPlusMoney.add(dtlVO.getOverPlusMoney());
                    }
                    subTotalApproveMoney = subTotalApproveMoney.add(dtlVO.getApproveMoney());
                    subTotalUseMoney = subTotalUseMoney.add(dtlVO.getUseMoney());
                    subTotalBondApplyMoney = subTotalBondApplyMoney.add(dtlVO.getBondApply());
                    subTotalBondAwayMoney = subTotalBondAwayMoney.add(dtlVO.getBondAway());
                }
                // for (RatUrationUseDtlVO ratUrationUseDtlVO : dtl) {
                // if (RationConstant.RAT_COMFLAG_1.equals(ratUrationUseDtlVO.getItemFlag())) {
                // ratUrationUseDtlVO.setLeftMoney(ratUrationUseDtlVO.getLeftMoney().min(comSubTotalLeftMoney));
                // ratUrationUseDtlVO.setOverPlusMoney(ratUrationUseDtlVO.getOverPlusMoney().min(comSubTotalOverPlusMoney));
                // }
                // }
                // corp.setMoney(subTotalMoney);
                // corp.setApproveMoney(subTotalApproveMoney);
                // corp.setOverPlusMoney(subTotalOverPlusMoney);//可使用额度合计=授信额度合计-在审合计-使用合计
                // corp.setOverPlusMoney(subTotalMoney.subtract(subTotalApproveMoney).subtract(subTotalUseMoney));//可使用额度合计=授信额度合计-在审合计-使用合计
                // corp.setLeftMoney(subTotalLeftMoney);//剩余额度合计=授信额度合计-使用额度
                // corp.setLeftMoney(subTotalMoney.subtract(subTotalUseMoney));//剩余额度合计=授信额度合计-使用额度
                // corp.setUseMoney(subTotalUseMoney);//授信使用额度
                corp.setBondApply(subTotalBondApplyMoney);// 保证金（占用）
                corp.setBondAway(subTotalBondAwayMoney);// 保证金（在途）
                if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disWay)) {
                    String disCode = corp.getDisCode();// 授信ID
                    RatUrationUseDtlVO corpVO = disRatMap.get(disCode);
                    if (!EmptyUtils.isEmpty(corpVO)) {
                        corp.setMoney(corpVO.getMoney());// 剩余额度合计=授信额度合计-使用额度
                        corp.setLeftMoney(corpVO.getLeftMoney());// 剩余额度合计=授信额度合计-使用额度
                        corp.setUseMoney(corpVO.getUseMoney());// 授信使用额度
                        corp.setOverPlusMoney(corpVO.getOverPlusMoney());// 可使用额度合计
                    }
                } else {
                    corp.setMoney(subTotalMoney);
                    corp.setApproveMoney(subTotalApproveMoney);
                    corp.setOverPlusMoney(subTotalOverPlusMoney);// 可使用额度合计=授信额度合计-在审合计-使用合计
                    corp.setOverPlusMoney(subTotalMoney.subtract(subTotalApproveMoney).subtract(subTotalUseMoney));// 可使用额度合计=授信额度合计-在审合计-使用合计
                    corp.setLeftMoney(subTotalLeftMoney);// 剩余额度合计=授信额度合计-使用额度
                    corp.setLeftMoney(subTotalMoney.subtract(subTotalUseMoney));// 剩余额度合计=授信额度合计-使用额度
                    corp.setUseMoney(subTotalUseMoney);// 授信使用额度
                }
            }
        }
    }
    /**
     * 设置银行、单位总数
     *
     * @param beanList
     */
    public void setTotalSize(List<RatBkUseVO> beanList) {
        for (RatBkUseVO rat : beanList) {
            int totalNum = 0;
            List<RatBkUseCorpVO> corpList = rat.getCorpDtls();
            for (RatBkUseCorpVO vo : corpList) {
                if (vo.getCorpId().equals("subTotal")) {// 币别小计行数
                    totalNum += 1;
                } else {
                    int dtlSize = vo.getRationDlts().size();
                    vo.setRationSize(dtlSize);
                    totalNum += dtlSize;
                }
            }
            rat.setTotalSize(totalNum);
        }
    }
    /**
     * 币别小计
     *
     * @param beanList
     * @return
     */
    public Map<String, RatBkUseCorpVO> calBankOrCorpSubTotal(List<RatBkUseVO> beanList, String sortType,
            Map<String, RatUrationUseDtlVO> disRatMap, Map<String, RatBkUseCorpVO> totalMap, String moneyType) {
        Map<String, RatBkUseCorpVO> subTotal = new HashMap<String, RatBkUseCorpVO>();
        Map<String, String> disMap = new HashMap<>();
        String curName = multiLangServiceI.getLang("table.datatable.subtotal");
        for (RatBkUseVO rat : beanList) {
            String sortCode = "";
            List<RatBkUseCorpVO> secondList = rat.getCorpDtls();// 二级分组集合
            RatBkUseCorpVO curVO = null;
            for (RatBkUseCorpVO vo : secondList) {
                String disWay = vo.getDisWay();// 分配方式,同一授信数据只能小计一次
                RatUrationUseDtlVO corpVO = null;
                String disCode = vo.getDisCode();// 分配ID
                String ratCode = vo.getRatCode();// 授信ID
                corpVO = disRatMap.get(disCode);
                String curCode = vo.getCurCode();
                if (sortType.equals(RationConstant.SORT_TYPE_0)) {
                    sortCode = vo.getCorpNo();
                } else {
                    sortCode = vo.getBankCode();
                }
                RatBkUseCorpVO totalVO = totalMap.get(curCode);
                if (EmptyUtils.isEmpty(totalVO)) {
                    totalVO = new RatBkUseCorpVO();
                    // BeanUtils.copyProperties(vo, totalVO);
                    totalMap.put(curCode, totalVO);
                }
                curVO = subTotal.get(sortCode + "_" + curCode);
                if (curVO == null) {
                    curVO = new RatBkUseCorpVO();
                    BeanUtils.copyProperties(vo, curVO);
                    curVO.setCorpId("subTotal");
                    curVO.setCorpName(vo.getCurName() + "_" + curName);
                    curVO.setMoney(corpVO.getMoney());
                    curVO.setLeftMoney(corpVO.getLeftMoney());
                    curVO.setOverPlusMoney(corpVO.getOverPlusMoney());
                    curVO.setUseMoney(corpVO.getUseMoney());
                    curVO.setApproveMoney(corpVO.getApproveMoney());
                    curVO.setBondApply(vo.getBondApply());// 保证金（已执行）
                    curVO.setBondAway(vo.getBondAway());// 保证金（在途）
                    subTotal.put(sortCode + "_" + curCode, curVO);
                    String ratCodeStr = disMap.get(ratCode);
                    if (EmptyUtils.isEmpty(ratCodeStr)) {
                        disMap.put(ratCode, ratCode);
                        setTotalMoney(totalVO, corpVO.getMoney(), corpVO.getLeftMoney(), corpVO.getOverPlusMoney());
                    } else {
                        if (!RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disWay)) {
                            setTotalMoney(totalVO, corpVO.getMoney(), corpVO.getLeftMoney(), corpVO.getOverPlusMoney());
                        }
                    }
                    totalVO.setUseMoney(totalVO.getUseMoney().add(corpVO.getUseMoney()));
                    totalVO.setApproveMoney(totalVO.getApproveMoney().add(corpVO.getApproveMoney()));
                    totalVO.setBondApply(totalVO.getBondApply().add(vo.getBondApply()));// 保证金（已执行）总计
                    totalVO.setBondAway(totalVO.getBondAway().add(vo.getBondAway()));// 保证金（在途）总计
                    disMap.put(ratCode, ratCode);
                } else {
                    // 判断是否共享的数据
                    String ratCodeStr = disMap.get(ratCode);
                    if (EmptyUtils.isEmpty(ratCodeStr)) {
                        disMap.put(ratCode, ratCode);
                        setTotalMoney(curVO, corpVO.getMoney(), corpVO.getLeftMoney(), corpVO.getOverPlusMoney());
                        setTotalMoney(totalVO, corpVO.getMoney(), corpVO.getLeftMoney(), corpVO.getOverPlusMoney());
                    } else {
                        if (!RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disWay)) {
                            setTotalMoney(curVO, corpVO.getMoney(), corpVO.getLeftMoney(), corpVO.getOverPlusMoney());
                            setTotalMoney(totalVO, corpVO.getMoney(), corpVO.getLeftMoney(), corpVO.getOverPlusMoney());
                        }
                    }
                    curVO.setUseMoney(curVO.getUseMoney().add(corpVO.getUseMoney()));
                    curVO.setApproveMoney(curVO.getApproveMoney().add(corpVO.getApproveMoney()));
                    curVO.setBondApply(curVO.getBondApply().add(vo.getBondApply()));// 保证金（已执行）小计
                    curVO.setBondAway(curVO.getBondAway().add(vo.getBondAway()));// 保证金（在途）小计
                    totalVO.setUseMoney(totalVO.getUseMoney().add(corpVO.getUseMoney()));
                    totalVO.setApproveMoney(totalVO.getApproveMoney().add(corpVO.getApproveMoney()));
                    totalVO.setBondApply(totalVO.getBondApply().add(vo.getBondApply()));// 保证金（已执行）小计
                    totalVO.setBondAway(totalVO.getBondAway().add(vo.getBondAway()));// 保证金（在途）小计
                }
            }
        }
        // 添加小计明细
        for (RatBkUseVO rat : beanList) {
            String sortCode = "";
            if (sortType.equals(RationConstant.SORT_TYPE_0)) {
                sortCode = rat.getCorpNo();
            } else {
                sortCode = rat.getBankCode();
            }
            List<RatBkUseCorpVO> corpList = rat.getCorpDtls();
            for (RatBkUseCorpVO vo : corpList) {
                List<RatUrationUseDtlVO> rationDlts = vo.getRationDlts();
                for (RatUrationUseDtlVO useDtlVO : rationDlts) {
                    setBeanMoneyType(useDtlVO, Integer.valueOf(moneyType));
                }
                setBeanMoneyType(vo, Integer.valueOf(moneyType));
            }
            for (Entry<String, RatBkUseCorpVO> entry : subTotal.entrySet()) {
                String code = entry.getKey();
                String[] codeStr = code.split("_");
                RatBkUseCorpVO corpVO = entry.getValue();
                if (codeStr[0].equals(sortCode)) {
                    setBeanMoneyType(corpVO, Integer.valueOf(moneyType));
                    corpList.add(corpVO);
                }
            }
        }
        for (Entry<String, RatBkUseCorpVO> entry : totalMap.entrySet()) {
            RatBkUseCorpVO totalVo = entry.getValue();
            setBeanMoneyType(totalVo, Integer.valueOf(moneyType));
        }
        return subTotal;
    }
    public void setTotalMoney(RatBkUseCorpVO corpVO, BigDecimal money, BigDecimal leftMoney, BigDecimal overPlusMoney) {
        corpVO.setMoney(corpVO.getMoney().add(money));
        corpVO.setLeftMoney(corpVO.getLeftMoney().add(leftMoney));
        corpVO.setOverPlusMoney(corpVO.getOverPlusMoney().add(overPlusMoney));
    }
    public void setTotalOtherMoney(RatBkUseCorpVO corpVO, BigDecimal useMoney, BigDecimal approveMoney,
            BigDecimal bondApply, BigDecimal bondAway) {
        corpVO.setUseMoney(corpVO.getUseMoney().add(useMoney));
        corpVO.setApproveMoney(corpVO.getApproveMoney().add(corpVO.getApproveMoney()));
        corpVO.setBondApply(corpVO.getBondApply().add(bondApply));// 保证金（已执行）小计
        corpVO.setBondAway(corpVO.getBondAway().add(bondAway));// 保证金（在途）小计
    }
    /**
     * 币别合计
     *
     * @param bankSubTotal
     * @param beanList
     * @return
     */
    public void calCurTotal(Map<String, RatBkUseCorpVO> bankSubTotal, List<RatBkUseVO> beanList,
            Map<String, RatBkUseCorpVO> totalMap) {
        // 币别合计
        Map<String, RatBkUseVO> curTotal = new HashMap<String, RatBkUseVO>();
        String corpName = multiLangServiceI.getLang("table.datatable.total");
        for (Entry<String, RatBkUseCorpVO> entry : bankSubTotal.entrySet()) {
            String code = entry.getKey();
            String[] codeStr = code.split("_");
            RatBkUseCorpVO corpUseVO = entry.getValue();
            RatBkUseVO bankVO = curTotal.get(codeStr[1]);
            RatBkUseCorpVO corpVO = totalMap.get(codeStr[1]);
            if (EmptyUtils.isEmpty(corpVO)) {
                corpVO = entry.getValue();
            }
            if (bankVO == null) {
                bankVO = new RatBkUseVO();
                bankVO.setCorpId("total");
                bankVO.setCorpName(corpUseVO.getCurName() + "_" + corpName);
                bankVO.setMoney(corpVO.getMoney());
                bankVO.setApproveMoney(corpVO.getApproveMoney());
                bankVO.setOverPlusMoney(corpVO.getOverPlusMoney());
                bankVO.setLeftMoney(corpVO.getLeftMoney());
                bankVO.setUseMoney(corpVO.getUseMoney());
                bankVO.setBondApply(corpVO.getBondApply());// 保证金（已执行）
                bankVO.setBondAway(corpVO.getBondAway());// 保证金（在途）
                curTotal.put(corpVO.getCurCode(), bankVO);
            } else {
                bankVO.setMoney(bankVO.getMoney().add(corpVO.getMoney()));
                bankVO.setApproveMoney(bankVO.getApproveMoney().add(corpVO.getApproveMoney()));
                bankVO.setOverPlusMoney(bankVO.getOverPlusMoney().add(corpVO.getOverPlusMoney()));
                bankVO.setLeftMoney(bankVO.getLeftMoney().add(corpVO.getLeftMoney()));
                bankVO.setUseMoney(bankVO.getUseMoney().add(corpVO.getUseMoney()));
                bankVO.setBondApply(bankVO.getBondApply().add(corpVO.getBondApply()));// 保证金（已执行）合计
                bankVO.setBondAway(bankVO.getBondAway().add(corpVO.getBondAway()));// 保证金（在途）合计
            }
        }
        // 遍历添加合计
        for (Entry<String, RatBkUseVO> entry : curTotal.entrySet()) {
            beanList.add(entry.getValue());
        }
    }
    /**
     * 金额单位转换
     *
     * @param disBeans
     * @param moneyType
     */
    private void setBeanMoneyType(Object disBeans, Integer moneyType) {
        if (RationConstant.COMMON_MONEYUNIT_1.equals(moneyType)) {
            return;
        }
        Class userCla = null;
        Object disBean = null;
        if (disBeans instanceof RatBkUseCorpVO) {
            disBean = (RatBkUseCorpVO) disBeans;
            userCla = (Class) disBean.getClass();
        }
        if (disBeans instanceof RatBkUseVO) {
            disBean = (RatBkUseVO) disBeans;
            userCla = (Class) disBean.getClass();
        }
        if (disBeans instanceof RatUrationUseDtlVO) {
            disBean = (RatUrationUseDtlVO) disBeans;
            userCla = (Class) disBean.getClass();
        }
        /*
         * 得到类中的所有属性集合
         */
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = f.get(disBean);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                if (type.endsWith("BigDecimal")) {
                    if (null == val) {
                        f.set(disBean, BigDecimal.ZERO);
                    } else {
                        BigDecimal value = (BigDecimal) val;
                        f.set(disBean, value.divide(BigDecimal.valueOf(RationConstant.COMMON_MONEYUNIT_10000), 2,
                                BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取国际化
     */
    public String getLangStr(String str) {
        return multiLangServiceI.getLang(str);
    }
    /**
     * 获取数据字典
     */
    public String getDict(String groupCode, String code) {
        return dictService.get(groupCode, code);
    }
    /**
     * <br>
     * <b>功能：</b> 按照授信分配明细计算占用、在途、追加明细数据 大概过程：1、根据授信占用表查询授信的占用情况，包括在审占用和正式占用 2、根据授信追减表查询授信的追减情况，这部分数据为在审占用
     * 3、根据授信调剂表查询授信的调剂情况，这部分数据为在审占用 4、最后汇总计算授信的可用额度=分配额度-占用-在途占用-虚拟占用-虚拟在途 剩余额度=分配额度-占用-虚拟占用=可用+在途占用+虚拟在途 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/18 0018 <br>
     */
    public void groupbyRatUserVo(List<RatBkUseVO> ratLists) {
        List<RatUrationCurVO> rationCurVOS = queryItemApplyMoney(ratLists);
        Map<String, BigDecimal> rateMap = new HashMap<String, BigDecimal>();
        List<String> ratCodes = new ArrayList<>();
        Map<String, List<RatBkUseVO>> ratMap = new HashMap<>();
        Map<String, Set<String>> shareMap = new HashMap<>();
        for (RatBkUseVO useVO : ratLists) {
            String ratCode = useVO.getRatCode();
            String disCode = useVO.getDisCode();
            String disWay = useVO.getDisWay();
            List<RatBkUseVO> bkUseVOS = ratMap.get(disCode);// 按照授信分配合同分组
            if (null == bkUseVOS) {
                bkUseVOS = new ArrayList<>();
                ratMap.put(disCode, bkUseVOS);
            }
            bkUseVOS.add(useVO);
            if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disWay)) {// 总额共享
                Set<String> ratSet = shareMap.get(ratCode);// 授信合同
                if (null == ratSet) {
                    ratSet = new HashSet<>();
                    shareMap.put(ratCode, ratSet);
                }
                ratSet.add(disCode);
            }
            if (!ratCodes.contains(ratCode)) {
                ratCodes.add(ratCode);
            }
        }
        for (RatUrationCurVO rationCurVO : rationCurVOS) {
            rationCurVO.setUpdateAll(false);
            calcRatMoney(rationCurVO, ratMap, shareMap, rateMap);
        }
        // 计算调剂在途和追减在途
        List<RatUrationCurVO> reliefList = reliefDetailService.queryRationReliefFreeze(ratCodes);// 调剂在途
        for (RatUrationCurVO rationCurVO : reliefList) {
            rationCurVO.setInitCurCode(rationCurVO.getCurCode());// 默认即授信币别
            rationCurVO.setStatus(RationConstant.RAT_USE_STATUS_FREEZE);// 在途占用
            rationCurVO.setIsOpenRat(CommonConstant.NO);
            rationCurVO.setUpdateAll(true);
            calcRatMoney(rationCurVO, ratMap, shareMap, rateMap);
        }
        List<RatUrationCurVO> addList = addDetailService.queryRationSubAddFreeze(ratCodes);// 追减在途
        for (RatUrationCurVO rationCurVO : addList) {
            rationCurVO.setInitCurCode(rationCurVO.getCurCode());// 默认即授信币别
            rationCurVO.setStatus(RationConstant.RAT_USE_STATUS_FREEZE);// 在途占用
            rationCurVO.setIsOpenRat(CommonConstant.NO);
            rationCurVO.setUpdateAll(false);
            calcRatMoney(rationCurVO, ratMap, shareMap, rateMap);
        }
    }
    public BigDecimal calBillMoney(RatUrationCurVO rationCurVO, Map<String, BigDecimal> rateMap) {
        BigDecimal ratMoney = rationCurVO.getBillMoney();// 授信金额
        String curCode = rationCurVO.getCurCode();// 授信币别
        String initCurCode = rationCurVO.getInitCurCode();// 业务币别
        /*
         * if(curCode.equals(initCurCode)){//业务币别与授信币别相等 ratMoney = rationCurVO.getBillMoney();//业务金额 } else
         * {//不等，需要按照最新汇率折算为授信币别 ratMoney = rationCurVO.getBillMoney();//业务金额 String ratKey = curCode + "_" +
         * initCurCode; BigDecimal curRate = rateMap.get(ratKey);//币别折本位币汇率 if (null == curRate) { //获取最新的币别折算汇率 curRate
         * = rateAdjustService.getExchangeRateBy(initCurCode, curCode, DateUtils.getDate(),
         * FcExcrateAdjustConstant.RATE_TYPE_CONVERT); rateMap.put(ratKey, curRate); }
         * rationCurVO.setExchangeRate(curRate); ratMoney = ratMoney.multiply(curRate).setScale(2,
         * BigDecimal.ROUND_HALF_UP); }
         */
        String isOpenRat = rationCurVO.getIsOpenRat();// 敞口授信
        if (CommonConstant.YES.equals(isOpenRat)) {// 启用 授信= 业务金额-保证金金额
            BigDecimal bondMoney = rationCurVO.getBondMoney();// 保证金金额
            /*
             * String bondCurId = rationCurVO.getBondCurId();//保证金币别 if(curCode.equals(bondCurId)){//保证金币别与授信币别相等
             * bondMoney = rationCurVO.getBondMoney();//保证金金额 } else {//不等，需要按照最新汇率折算为授信币别 bondMoney =
             * rationCurVO.getBondMoney();//保证金金额 String ratKey = curCode + "_" + bondCurId; BigDecimal curRate =
             * rateMap.get(ratKey);//币别折本位币汇率 if (null == curRate) { //获取最新的币别折算汇率 curRate =
             * rateAdjustService.getExchangeRateBy(bondCurId, curCode, DateUtils.getDate(),
             * FcExcrateAdjustConstant.RATE_TYPE_CONVERT); rateMap.put(ratKey, curRate); } bondMoney =
             * bondMoney.multiply(curRate).setScale(2, BigDecimal.ROUND_HALF_UP); }
             */
            ratMoney = ratMoney.subtract(bondMoney);// 授信占用额度
        }
        return ratMoney;
        // return rationCurVO.getMoney();
    }
    public void calcRatMoney(RatUrationCurVO rationCurVO, Map<String, List<RatBkUseVO>> ratMap,
            Map<String, Set<String>> shareMap, Map<String, BigDecimal> rateMap) {
        String ratCode = rationCurVO.getRatCode();// 授信ID
        String disCode = rationCurVO.getDisCode();// 分配ID
        BigDecimal ratMoney = calBillMoney(rationCurVO, rateMap);// 业务金额
        List<RatBkUseVO> useVOS = ratMap.get(disCode);// 授信分配
        calcUseRatMoney(useVOS, rationCurVO, ratMoney);// 计算授信占用和在途占用的额度
        // 共享模式下占用
        Set<String> shareDisCodes = shareMap.get(ratCode);
        if (EmptyUtils.isEmpty(shareDisCodes)) {
            return;
        }
        for (String shareDisCode : shareDisCodes) {
            if (shareDisCode.equals(disCode)) {// 计算其他单位分配的额度
                continue;
            }
            List<RatBkUseVO> shareUseVOS = ratMap.get(shareDisCode);// 共享单位的授信分配额度
            calcShareUseRatMoney(shareUseVOS, rationCurVO, ratMoney);// 计算共享单位下授信占用和在途占用的额度
        }
    }
    public void calcUseRatMoney(List<RatBkUseVO> useVOS, RatUrationCurVO rationCurVO, BigDecimal ratMoney) {
        if (EmptyUtils.isEmpty(useVOS)) {
            return;
        }
        String itemCode = rationCurVO.getItemCode();// 授信品种
        String itemFlag = rationCurVO.getItemFlag();// 授信品种类型
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
            for (RatBkUseVO useVO : useVOS) {
                if (useVO.getItemCode().equals(itemCode)) {
                    if (RationConstant.RAT_USE_STATUS_FREEZE.equals(rationCurVO.getStatus())) {
                        useVO.setFreezeMoney(useVO.getFreezeMoney().add(ratMoney));
                    } else {
                        useVO.setUseMoney(useVO.getUseMoney().add(ratMoney));
                    }
                    break;
                }
            }
        } else {
            // 综合品种计算占用额度，需减掉其他综合品种占用的额度
            for (RatBkUseVO useVO : useVOS) {
                if (RationConstant.RAT_COMFLAG_0.equals(useVO.getItemFlag())) {
                    continue;
                }
                if (useVO.getItemCode().equals(itemCode)) {
                    if (RationConstant.RAT_USE_STATUS_FREEZE.equals(rationCurVO.getStatus())) {
                        useVO.setFreezeMaxMoney(useVO.getFreezeMaxMoney().add(ratMoney));
                    } else {
                        useVO.setUseMaxMoney(useVO.getUseMaxMoney().add(ratMoney));
                    }
                } else {
                    if (rationCurVO.isUpdateAll()) {
                        useVO.setVirtualUrationMaxMoney(useVO.getVirtualUrationMaxMoney().add(ratMoney));
                    }
                }
            }
        }
    }
    public void calcShareUseRatMoney(List<RatBkUseVO> useVOS, RatUrationCurVO rationCurVO, BigDecimal ratMoney) {
        if (EmptyUtils.isEmpty(useVOS)) {
            return;
        }
        String itemCode = rationCurVO.getItemCode();// 授信品种
        String itemFlag = rationCurVO.getItemFlag();// 授信品种类型
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
            for (RatBkUseVO useVO : useVOS) {
                if (useVO.getItemCode().equals(itemCode)) {
                    if (RationConstant.RAT_USE_STATUS_FREEZE.equals(rationCurVO.getStatus())) {
                        useVO.setVirtualUration(useVO.getVirtualUration().add(ratMoney));// 虚拟在途占用
                    } else {
                        useVO.setReliefAndUrationMoney(useVO.getReliefAndUrationMoney().add(ratMoney));// 虚拟占用
                    }
                    break;
                }
            }
        } else {
            // 综合品种计算占用额度，需减掉其他综合品种占用的额度
            for (RatBkUseVO useVO : useVOS) {
                if (RationConstant.RAT_COMFLAG_0.equals(useVO.getItemFlag())) {
                    continue;
                }
                if (rationCurVO.isUpdateAll()) {
                    if (RationConstant.RAT_USE_STATUS_FREEZE.equals(rationCurVO.getStatus())) {
                        useVO.setVirtualUrationMaxMoney(useVO.getVirtualUrationMaxMoney().add(ratMoney));// 共享单位的虚拟在途占用
                    } else {
                        useVO.setVirtualMaxMoney(useVO.getVirtualMaxMoney().add(ratMoney));// 共享单位的虚拟占用
                    }
                } else {
                    if (useVO.getItemCode().equals(itemCode)) {
                        if (RationConstant.RAT_USE_STATUS_FREEZE.equals(rationCurVO.getStatus())) {
                            useVO.setVirtualUrationMaxMoney(useVO.getVirtualUrationMaxMoney().add(ratMoney));// 共享单位的虚拟在途占用
                        } else {
                            useVO.setVirtualMaxMoney(useVO.getVirtualMaxMoney().add(ratMoney));// 共享单位的虚拟占用
                        }
                    }
                }
            }
        }
    }
    /**
     * <br>
     * <b>功能：</b> 查询所有占用及在审占用的授信数据 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/17 0017 <br>
     */
    public List<RatUrationCurVO> queryItemApplyMoney(List<RatBkUseVO> ratLists) {
        List<String> ratCodes = ratLists.stream().map(RatBkUseVO::getRatCode).distinct().collect(Collectors.toList());
        List<RatUrationCurVO> rationCurVOS = queryRatApplyMoney(ratCodes);// 授信占用数据
        return rationCurVOS;
    }
    /**
     * <br>
     * <b>功能：</b> 查询所有占用及在审占用的授信数据 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/17 0017 <br>
     */
    public List<RatUrationCurVO> queryRatApplyMoney(List<String> ratCodes) {
        List<RatUrationCurVO> urationCurVOS = ratBkUseStatisticsDao.queryItemApplyMoney(ratCodes);
        return calcLeftUseRationMoney(urationCurVOS);
    }
    /**
     * <br>
     * <b>功能：</b> 计算授信合同额度 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/19 0019 <br>
     */
    public List<RatUrationCurVO> calcLeftUseRationMoney(List<RatUrationCurVO> urationCurVOS) {
        // 计算占用减去还款
        List<RatUrationCurVO> result = new ArrayList<>();
        if (EmptyUtils.isEmptyList(urationCurVOS) || urationCurVOS.size() == 0) {
            return result;
        }
        Map<String, List<RatUrationCurVO>> ratMap = new HashMap<>();
        List<RatUrationCurVO> useList = new ArrayList<>();// 使用集合
        for (RatUrationCurVO curVO : urationCurVOS) {
            Integer status = curVO.getStatus();
            if (RationConstant.RAT_USE_STATUS_FREEZE.equals(status)) {// 在途
                result.add(curVO);
            } else if (RationConstant.RAT_USE_STATUS_USE.equals(status)) {// 占用
                curVO.setStatus(RationConstant.RAT_USE_STATUS_USE);
                useList.add(curVO);
            } else if (RationConstant.RAT_USE_STATUS_RELEASE.equals(status)) {// 释放
                String billId = curVO.getBillId();
                List<RatUrationCurVO> curVOS = ratMap.get(billId);
                if (null == curVOS) {
                    curVOS = new ArrayList<>();
                    ratMap.put(billId, curVOS);
                }
                curVOS.add(curVO);
            }
        }
        // 针对部分还款
        for (RatUrationCurVO curVO : useList) {
            String billId = curVO.getBillId();// 占用的业务ID
            List<RatUrationCurVO> releaseList = ratMap.get(billId);// 释放集合
            if (null != releaseList) {
                BigDecimal billMoney = curVO.getBillMoney();
                for (RatUrationCurVO releaseVO : releaseList) {
                    BigDecimal releaseMoney = releaseVO.getMoney();// 已释放的额度
                    billMoney = billMoney.subtract(releaseMoney);
                }
                curVO.setBillMoney(billMoney);// 剩余占用的额度
            }
            result.add(curVO);
        }
        return result;
    }
}
