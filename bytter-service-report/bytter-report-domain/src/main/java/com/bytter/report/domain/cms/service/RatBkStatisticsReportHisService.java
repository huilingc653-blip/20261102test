package com.bytter.report.domain.cms.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.CurrentUser;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.report.domain.cms.dao.RatBkStatisticsReportHisDao;
import com.bytter.report.domain.cms.entity.RatBkStatisticsReportHis;
import com.bytter.report.domain.cms.vo.RatBkStatisticSearchVO;
import com.bytter.report.domain.cms.vo.RatBkStatisticVO;
import com.bytter.report.domain.cms.vo.RatBkStatisticsReportHisVo;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.DictDTO;
import com.google.common.collect.Maps;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("ratBkStaticReportHisService")
@Transactional
public class RatBkStatisticsReportHisService extends BaseService implements ITaskService {

    @Autowired
    private RatBkStatisticsReportHisDao ratBkStatisticsReportHisDao;
    @Autowired
    private RatBkStatisticsService ratBkStatisticsService;
    @Autowired
    private ICorpService corpService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao getEntityDao() {
        return null;
    }

    @Override
    @XxlJob("RatBkStatisticsReportHisService")
    public void run(String arguments) {
        this.logger.info("---自动执行保存当日综合授信额度统计表查询，开始执行定时任务---");
        this.logger.info("参数：arguments=" + arguments);
        Date nowDate = new Date();
        nowDate = DateUtils.parse(DateUtils.format(nowDate, "yyyy-MM-dd"), "yyyy-MM-dd");
        nowDate = DateUtils.addDay(nowDate, -1);
        RatBkStatisticSearchVO ratBkStatisticSearchVO = new RatBkStatisticSearchVO();
        ratBkStatisticSearchVO.setMoneyType("1");
        List<RatBkStatisticVO> ratBkAmountStatisticsList = ratBkStatisticsService.getRatBkAmountStatisticsList(ratBkStatisticSearchVO, new CurrentUser());
        if (ratBkAmountStatisticsList != null && ratBkAmountStatisticsList.size() != 0) {
            List<RatBkStatisticsReportHisVo> list = BeanUtils.copyListProperties(ratBkAmountStatisticsList, RatBkStatisticsReportHisVo.class);
            List<RatBkStatisticsReportHis> entityList = (List<RatBkStatisticsReportHis>) BeanUtils.copyListProperties(list, RatBkStatisticsReportHis.class);
            for (RatBkStatisticsReportHis ratBkStatisticsReportHis : entityList) {
                ratBkStatisticsReportHis.setReportDate(nowDate);
            }
            //过滤掉合计元素
            List<RatBkStatisticsReportHis> list1 = entityList.stream().filter(p -> !p.getCorpName().contains("合计")).collect(Collectors.toList());
            //删除重复的数据，防止定时任务重复执行
            ratBkStatisticsReportHisDao.deleteHisRatBkStatisics(nowDate);
            ratBkStatisticsReportHisDao.batchSave(list1);
            this.logger.info("---保存当日委托综合授信额度统计表定时任务执行完毕！---");
        } else {
            this.logger.info("---当日综合授信额度统计表查询数据为空---");
        }
    }

    /**
     * 根据 单位,银行,授信结束日期 查询统计额度
     *
     * @param ratVO
     * @return
     */
    public List<RatBkStatisticsReportHisVo> getRatBkAmountStatisticsListHis(RatBkStatisticSearchVO ratVO, ICurrentUser user) {
        // 当前用户查询单位权限
        List<String> corpAuth = corpService.getUserCorpIds(RationConstant.CMS_REPORT_AMOUNT_QUERY, user.getId());
        List<RatBkStatisticsReportHisVo> ratBkStatisticVOList = ratBkStatisticsReportHisDao.getRatBkAmountStatisticsListHis(ratVO, corpAuth);

        Map<String, DictDTO> disWayMap = dictService.listMap("RatBkrationDis.DistributeWay");
        Map<String, DictDTO> ratModeMap = dictService.listMap("RatBkration.RatMode");

        Map<String, String> disRatMap = Maps.newHashMap();
        Map<String, RatBkStatisticsReportHisVo> ratHisMap = Maps.newHashMap();
        for (RatBkStatisticsReportHisVo hisVo : ratBkStatisticVOList) {
            String disWay = hisVo.getDisWay(); // 分配方式
            if (EmptyUtils.isNotEmpty(disWay)) {
                DictDTO disDTO = disWayMap.get(disWay);
                if (!EmptyUtils.isEmpty(disDTO)) {
                    hisVo.setDisWayName(disDTO.getName());
                }
            }
            String ratMode = hisVo.getRatMode(); // 授信主键id
            if (EmptyUtils.isNotEmpty(ratMode)) {
                DictDTO ratDTO = ratModeMap.get(ratMode);
                if (!EmptyUtils.isEmpty(ratDTO)) {
                    hisVo.setRatModeName(ratDTO.getName());
                }
            }

            String ratCode = hisVo.getRatCode();// 授信主键id
            // 单位金额转换
            setBeanMoneyType(hisVo, Integer.valueOf(ratVO.getMoneyType()));
            RatBkStatisticsReportHisVo ratHisBean = ratHisMap.get(hisVo.getCurCode());
            if (null == ratHisBean) {
                ratHisBean = new RatBkStatisticsReportHisVo();
                ratHisBean.setFlagSign("total");//合计列
                ratHisBean.setCorpName(hisVo.getCurName() + "_" + multiLangServiceI.getLang("table.datatable.total") + ":");
                ratHisBean.setMoney(hisVo.getMoney());
                ratHisBean.setUseMoney(hisVo.getUseMoney());
                ratHisBean.setApproveMoney(hisVo.getApproveMoney());
                ratHisBean.setOverPlusMoney(hisVo.getOverPlusMoney());
                ratHisBean.setLeftMoney(hisVo.getLeftMoney());
                ratHisMap.put(hisVo.getCurCode(), ratHisBean);
                if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disWay)) {
                    if (StringUtils.isNotEmpty(ratCode)) {
                        String ratCodeStr = disRatMap.get(ratCode);
                        if (EmptyUtils.isEmpty(ratCodeStr)) {
                            disRatMap.put(ratCode, ratCode);
                        }
                    }
                }
            } else {
                // 判断是否共享的数据
                if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disWay)) {
                    if (StringUtils.isNotEmpty(ratCode)) {
                        String ratCodeStr = disRatMap.get(ratCode);
                        if (EmptyUtils.isEmpty(ratCodeStr)) {
                            ratHisBean.setMoney(ratHisBean.getMoney().add(hisVo.getMoney()));
                            ratHisBean.setLeftMoney(ratHisBean.getLeftMoney().add(hisVo.getLeftMoney()));
                            ratHisBean.setOverPlusMoney(ratHisBean.getOverPlusMoney().add(hisVo.getOverPlusMoney()));
                            disRatMap.put(ratCode, ratCode);
                        }
                    }
                } else {
                    ratHisBean.setMoney(ratHisBean.getMoney().add(hisVo.getMoney()));
                    ratHisBean.setLeftMoney(ratHisBean.getLeftMoney().add(hisVo.getLeftMoney()));
                    ratHisBean.setOverPlusMoney(ratHisBean.getOverPlusMoney().add(hisVo.getOverPlusMoney()));
                }
                ratHisBean.setApproveMoney(ratHisBean.getApproveMoney().add(hisVo.getApproveMoney()));
                ratHisBean.setUseMoney(ratHisBean.getUseMoney().add(hisVo.getUseMoney()));
            }
        }
        ratHisMap.forEach((k, v) -> ratBkStatisticVOList.add(v));
        return ratBkStatisticVOList;
    }

    /**
     * 金额单位转换
     *
     * @param ratVO
     * @param moneyType
     */
    private void setBeanMoneyType(RatBkStatisticsReportHisVo ratVO, Integer moneyType) {
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
}
