package com.bytter.biz.inneraccount.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.inneraccount.dao.BisCorpPoolDtlDao;
import com.bytter.biz.inneraccount.entity.BisCorpPoolBal;
import com.bytter.biz.inneraccount.vo.BisCorpPoolBalVO;
import com.bytter.biz.inneraccount.vo.BisCorpPoolDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.DictDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: 多级单位余额核算-单位归集流水维护表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Slf4j
@Service("bisCorpPoolDtlService")
@Transactional
public class BisCorpPoolDtlService extends BaseService<BisCorpPoolDtlVO, String> implements ITaskService {

    @Autowired
    private BisCorpPoolDtlDao bisCorpPoolDtlDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Autowired
    private BisCorpPoolBalService bisCorpPoolBalService;
    @DubboReference(check = false)
    private IUserService userService;

    @Override
    public BaseDao<BisCorpPoolDtlVO, String> getEntityDao() {
        return bisCorpPoolDtlDao;
    }

    /**
     * 查询 单位归集流水维护表 列表数据
     * @param dataTablesInput
     * @param user
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder joinSql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.BANKACC_BISCORPPOOLDTL_QUERY, user.getId()), "acc.corp_id",
                true);
        joinSql.append(" and ").append(corpAuthSql);

        String poolTypeSearch = dataTablesInput.getFormdata().get("poolType").toString();
        if (CommonConstant.NO.equals(poolTypeSearch)) {
            joinSql.append(" and (t.pool_Type = '' or t.pool_Type is null)");
        } else {
            dataTablesInput.getFormdata().put("eqs_poolType", poolTypeSearch);
        }

        DataTablesOutput output = bisCorpPoolDtlDao.getDataTablesUnion(dataTablesInput, joinSql.toString());
        List<BisCorpPoolDtlVO> list = output.getData();
        Map<String, DictDTO> poolTypeDict = dictService.listMap("bisCorpPoolDtl.poolType");
        for (BisCorpPoolDtlVO vo:list) {
            if (CommonConstant.YES.equals(vo.getCdSign())) {
                vo.setAmtOut(vo.getAmt());
            } else if (CommonConstant.NO.equals(vo.getCdSign())) {
                vo.setAmtIn(vo.getAmt());
            }
            if (StringUtil.isBlank(vo.getPoolType())) {
                vo.setPoolType(CommonConstant.NO);
            }
            // 类型
            Optional.ofNullable(vo.getPoolType())
                    .flatMap(poolType -> Optional.ofNullable(poolTypeDict)
                            .flatMap(dictMap -> Optional.ofNullable(dictMap.get(poolType))))
                    .ifPresent(dict -> vo.setPoolTypeName(dict.getName()));
        }
        output.setData(list);
        return output;
    }

    /**
     * 批量认领 保存类型
     * @param vo
     * @param user
     */
    public void batchClaimSave(BisCorpPoolDtlVO vo, ICurrentUser user) {
        vo.setPoolBlendSign(CommonConstant.YES);
        vo.setUpdateBy(user.getLoginName());
        this.bisCorpPoolDtlDao.updatePoolType(vo);
    }

    /**
     * 单位归集流水自动认领
     *
     * @param typeName
     * @param ruleSql
     * @return
     */
    public int autoBatchClaimSave(String typeName, String ruleSql) {
        log.info("开始执行单位归集类型为" + typeName + " 的流水自动认领业务,规则对应的SQL【{}】", ruleSql);
        int count = bisCorpPoolDtlDao.countDtlByConfigRule(ruleSql);
        log.info("单位归集类型为:" + typeName + "的待认领流水有【{}】条。", count);
        if (count > 0) {
            // 根据记录数量决定分页大小，避免流水数据量过大，造成内存溢出
            int pageSize = count > 100000 ? 10000 : 1000;
            for (int i = 0; i < count; i += pageSize) {
                processPage(typeName, ruleSql, 0, Math.min(pageSize, count - i));
            }
        }
        return count;
    }

    /**
     * 单位归集流水认领
     *
     * @param ruleSql
     * @param startIndex
     * @param maxIndex
     */
    private void processPage(String typeName, String ruleSql, Integer startIndex, Integer maxIndex) {
        log.info("开始单位归集流水,类型为" + typeName + ",从【{} - {}】的流水认领数据。", startIndex, maxIndex);
        List<BisCorpPoolDtlVO> bisAccDtlList = bisCorpPoolDtlDao.listDtlByConfigRule(ruleSql, startIndex, maxIndex);
        if (!EmptyUtils.isEmptyList(bisAccDtlList)) {
            List<String> dtlIdList = bisAccDtlList.stream().map(BisCorpPoolDtlVO::getId).collect(Collectors.toList());
            String poolType = StringUtils.equals(typeName, "归集") ? "1" : "2";
            int count = bisCorpPoolDtlDao.updateBusTypeByIdList(dtlIdList, poolType);
            log.info("单位归集流水类型为" + typeName + "的流水,自动认领【{}】条。", count);
        }
    }

    /**
     * 由明细生成余额
     * @param vo
     * @param user
     */
    public void genBalSave(BisCorpPoolBalVO vo, ICurrentUser user) {
        this.logger.info("---由明细生成余额begin---");
        // 根据单位和余额日期，获取基准余额
        if (vo.getBalDate() == null) {
            vo.setBalDate(vo.getBalDateBegin());
        }
        Map<String, BisCorpPoolBalVO> balMap = bisCorpPoolBalService.getBalMap(vo);
        for (String corpId:vo.getCorpId().split(CommonConstant.COMMA_SPLIT)) {
            for (String curId:vo.getCurId().split(CommonConstant.COMMA_SPLIT)) {
                BisCorpPoolBalVO initBal = balMap.get(corpId + CommonConstant.COMMA_SPLIT + curId);
                if (initBal == null) {
                    initBal = bisCorpPoolBalService.getInitBalByCorpId(corpId, curId);
                    if (initBal == null) {
                        // 未做初始化余额的单位跳过计算
                        this.logger.info("单位[" + corpId + "]&币种[" + curId + "]未做初始化余额，不生成归集余额");
                        continue;
                    }
                    // 获取单位真实的初始化余额日期，开始日期从初始化余额日期后一天开始计算
                    vo.setBalDateBegin(DateUtils.addDay(initBal.getBalDate(), 1));
                }
                this.genBal(initBal, vo.getBalDateBegin(), vo.getBalDateEnd(), user);
            }
        }
        this.logger.info("---由明细生成余额end---");
    }

    /**
     * 按单位&币种，生成归集余额
     * @param initBal
     * @param balDateEnd
     * @param user
     */
    public void genBal(BisCorpPoolBalVO initBal, Date balDateBegin, Date balDateEnd, ICurrentUser user) {
        Date startDate = balDateBegin == null ? DateUtils.addDay(initBal.getBalDate(), 1):balDateBegin;
        Date endDate = balDateEnd;
        BigDecimal balTemp = initBal.getBal();
        List<BisCorpPoolBal> listAdd = new ArrayList<>();
        List<BisCorpPoolBal> listUpdate = new ArrayList<>();
        while (startDate.compareTo(endDate) < 1) {
            //判断当日是否有余额
            BisCorpPoolBalVO balVO = bisCorpPoolBalService.getByBalDate(initBal.getCorpId(), initBal.getCurId(), startDate);
            BigDecimal lastBal = this.getLastBal(initBal.getCorpId(), initBal.getCurId(), startDate);

            BisCorpPoolBal bal = new BisCorpPoolBal();
            bal.setInitSign(CommonConstant.NO);
            bal.setBalDate(startDate);
            bal.setGetDate(DateUtils.getDate());
            if(!EmptyUtils.isEmpty(balVO)){
                // 原数据覆盖更新
                BeanUtils.copyNotNullProperties(balVO, bal);
                bal.setBal(balTemp.add(lastBal));
                bal.setUpdateBy(user == null ? "":user.getLoginName());
                bal.setUpdateTime(DateUtils.getDate());
                listUpdate.add(bal);
            } else {
                // 新增数据
                bal.setCorpId(initBal.getCorpId());
                bal.setCurId(initBal.getCurId());
                bal.setBal(balTemp.add(lastBal));
                bal.setCreateBy(user == null ? "":user.getLoginName());
                bal.setCreateTime(DateUtils.getDate());
                listAdd.add(bal);
            }
            balTemp = bal.getBal();
            startDate = DateUtils.addDay(startDate, 1);
        }
        bisCorpPoolBalService.batchSave(listAdd);
        bisCorpPoolBalService.batchUpdate(listUpdate);
    }

    /**
     * 获取当天流水的结余
     * @param corpId
     * @param curId
     * @param transTime
     * @return
     */
    public BigDecimal getLastBal(String corpId, String curId, Date transTime) {
        BigDecimal lastBal = BigDecimal.ZERO;
        List<BisCorpPoolDtlVO> list = bisCorpPoolDtlDao.listByCorpIdAndTransTime(corpId, curId, transTime);
        for (BisCorpPoolDtlVO dtl : list) {
            if ("1".equals(dtl.getPoolType())) {
                // 归集（收入）
                lastBal = lastBal.add(dtl.getAmt());
            } else {
                // 下拨（支出）
                lastBal = lastBal.subtract(dtl.getAmt());
            }
        }
        return lastBal;
    }

    @Override
    @XxlJob("BisCorpPoolDtlService")
    public void run(String arguments) {
        this.logger.info("---单位归集余额自动计算begin---");
        BisCorpPoolBalVO initBal = new BisCorpPoolBalVO();
        // 默认计算到当日
        initBal.setBalDate(DateUtils.getDateYMD());
        initBal.setBalDateEnd(DateUtils.getDateYMD());
        // 获取已初始化的单位
        List<BisCorpPoolBal> initBals = bisCorpPoolBalService.listByInitSign();
        initBal.setCorpId(initBals.stream().map(BisCorpPoolBal::getCorpId).collect(Collectors.joining(CommonConstant.COMMA_SPLIT)));
        initBal.setCurId(initBals.stream().map(BisCorpPoolBal::getCurId).collect(Collectors.joining(CommonConstant.COMMA_SPLIT)));
        this.logger.info("执行日期："+initBal.getBalDate());
        this.logger.info("执行单位："+initBal.getCorpId());
        this.logger.info("执行币种："+initBal.getCurId());
        this.genBalSave(initBal, userService.getScheduler());
        this.logger.info("---单位归集余额自动计算end---");
    }
}
