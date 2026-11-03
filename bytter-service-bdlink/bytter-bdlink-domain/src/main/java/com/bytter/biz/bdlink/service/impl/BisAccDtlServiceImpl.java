package com.bytter.biz.bdlink.service.impl;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.dto.*;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlHisService;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlService;
import com.bytter.biz.bdlink.dao.BisAccDtlBudgetRuleDao;
import com.bytter.biz.bdlink.dao.BisAccDtlDao;
import com.bytter.biz.bdlink.entity.BisAccDtl;
import com.bytter.biz.bdlink.service.BisAccDtlService;
import com.bytter.biz.bdlink.vo.*;
import com.bytter.biz.payment.api.dto.FcBankAccGroupDTO;
import com.bytter.biz.payment.api.service.IFcBankAccGroupService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.constant.BisAccBalConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 银行账户流水管理Service接口实现类
 *
 * @author wangzx
 */
@Slf4j
@Service("bisAccDtlServiceImpl")
@DubboService
@Transactional
public class BisAccDtlServiceImpl implements IBisAccDtlService {
    @DubboReference(check = false)
    IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IBisBifInitService bifInitService;
    @Autowired
    private BisAccDtlService bisAccDtlService;
    @Autowired
    BisAccDtlDao bisAccDtlDao;
    @Autowired
    private BisAccDtlBudgetRuleDao bisAccDtlBudgetRuleDao;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IFcBankAccGroupService fcBankAccGroupService;

    @DubboReference(check = false)
    private IBisBankReceiptDtlService bisBankReceiptDtlService;

    @DubboReference(check = false)
    private IBisBankReceiptDtlHisService bisBankReceiptDtlHisService;

    public List<BisAccDtlDTO> getByTransTime(String gedTransTime,String ledTransTime){
        List<BisAccDtlDTO> list = new ArrayList<>();
        List<BisAccDtlVO> accDtlVOList = bisAccDtlService.getByTransTime( gedTransTime, ledTransTime);
        if(!EmptyUtils.isEmpty(accDtlVOList)) {
            list = BeanUtils.copyListProperties(accDtlVOList, BisAccDtlDTO.class);
        }
        return list;
    }

    public DataTablesOutput setDtlReceipt(DataTablesOutput out) {
        if(!EmptyUtils.isEmpty(out.getData())) {
            for (int i = 0; i < out.getData().size(); i++) {
                BisAccDetailVO bisAccDetailVO = (BisAccDetailVO) out.getData().get(i);
                if (bisAccDetailVO != null) {
                    BisAccDtlRulesVO accDtlRulesVO = this.getRules(bisAccDetailVO.getId());
                    if (accDtlRulesVO != null) {
                        bisAccDetailVO.setReceiptNum(1);
                        BisBankReceiptDtlDTO bisBankReceiptDtlDTO = bisBankReceiptDtlService.getById(accDtlRulesVO.getReceiptId());
                        if (!EmptyUtils.isEmpty(bisBankReceiptDtlDTO)) {
                            bisAccDetailVO.setReceiptFlag(CommonConstant.NO); //电子回单
                            bisAccDetailVO.setReceiptId(bisBankReceiptDtlDTO.getId());
                        } else {
                            //电子回单id为空，则获取历史电子回单
                            BisBankReceiptDtlDTO bisBankReceiptDtlHis = new BisBankReceiptDtlDTO();
                            BisBankReceiptDtlHisVO byIdUnion = bisBankReceiptDtlHisService.getByIdUnion(bisBankReceiptDtlDTO.getId());
                            BeanUtils.copyProperties(byIdUnion,bisBankReceiptDtlHis);
                            if (!EmptyUtils.isEmpty(bisBankReceiptDtlHis.getId())) {
                                bisAccDetailVO.setReceiptFlag(CommonConstant.YES);
                                bisAccDetailVO.setReceiptId(bisBankReceiptDtlDTO.getId());
                            }
                        }
                    }
                }
            }
        }

        return out;
    }

    /*
    被关联的流水的勾兑规则
     */
    public com.bytter.biz.bdlink.vo.BisAccDtlRulesVO getRules(String id) {
        List<BisAccDtlRulesVO> list = this.bisAccDtlDao.getRules(id);
        if (list.size() == 1) {
            com.bytter.biz.bdlink.vo.BisAccDtlRulesVO vo = new BisAccDtlRulesVO();
            BeanUtils.copyProperties(list.get(0),vo);
            return vo;
        } else {
            return null;
        }
    }

    /**
     * /**
     * 根据id得到实体
     *
     * @param id
     * @return BtBankAccDTO
     */
    @Override
    public BisAccDtlDTO getById(String id) {
        BisAccDtlVO dtlVO = bisAccDtlDao.getByUnion(id);
        if (EmptyUtils.isEmpty(dtlVO)) {
            return null;
        }
        BisBifInitDTO bifInit = bifInitService.getObjectById(dtlVO.getBifCode());
        BisAccDtlDTO dto = new BisAccDtlDTO();
        BeanUtils.copyNotNullProperties(dtlVO, dto);
        if (null != bifInit) {
            dto.setBifName(bifInit.getName());
        }
        return dto;
    }
    /**
     * 更新流水计划匹配状态
     *
     * @param id
     * @param status
     */
    @Override
    public void updateDtlFbsStatus(String id, String status) {
        BisAccDtl bisAccDtl = bisAccDtlDao.get(id);
        bisAccDtl.setFbsStatus(status);
//		bisAccDtl.setBlendSign(BtBankAccConstant.BLENDSIGN_0);
        bisAccDtl.setSettlType("");
        bisAccDtl.setBusType("");
        bisAccDtl.setRmk("");
        bisAccDtlDao.update(bisAccDtl);
    }
    /**
     * 更新流水计划匹配
     *
     * @param id
     * @param status
     */
    @Override
    public void updateDtlFbsStatus(String id, String status, String settlType) {
        BisAccDtl bisAccDtl = bisAccDtlDao.get(id);
        bisAccDtl.setFbsStatus(status);
        if (!EmptyUtils.isBlank(settlType)) {
            bisAccDtl.setSettlType(settlType);
        }
        bisAccDtlDao.update(bisAccDtl);
    }
    /**
     * 银行流水号为空。更新为id
     *
     * @param id
     */
    @Override
    public void updateDtlBankSerialNo(String id) {
        BisAccDtl bisAccDtl = bisAccDtlDao.get(id);
        if (EmptyUtils.isEmpty(bisAccDtl.getBankSerialNo())) {
            bisAccDtl.setBankSerialNo(id);
            bisAccDtlDao.update(bisAccDtl);
        }
    }
    /**
     * 更新流水
     *
     * @param dto
     */
    @Override
    public void updateDtlByDto(BisAccDtlDTO dto) {
        BisAccDtl bisAccDtl = bisAccDtlDao.get(dto.getId(), true);
        if (!EmptyUtils.isEmpty(bisAccDtl)) {
            BeanUtils.copyNotNullProperties(dto, bisAccDtl);
            //这里是为了保证撤销拆分后 设置状态为未确认状态
            if (dto.getStatus() == null){
                bisAccDtl.setStatus(null);
            }
            bisAccDtlDao.update(bisAccDtl);
        }
    }
    @Override
    public void updateClaimIdByIds(String[] ids, String claimId) {
        bisAccDtlDao.updateClaimIdByIds(ids, claimId);
    }
    /**
     * 外接口保存流水
     *
     * @param dto
     */
    @Override
    public void saveDtlByDto(BisAccDtlDTO dto) {
        BisAccDtl dtl = new BisAccDtl();
        BeanUtils.copyNotNullProperties(dto, dtl);
        bisAccDtlDao.save(dtl);
    }
    /**
     * 判断是否是合并单据
     *
     * @param id
     * @return
     */
    @Override
    public Boolean isMergeBill(String id) {
        List<BisAccDtl> list = this.bisAccDtlDao.listBy("mergeId", id);
        return !EmptyUtils.isEmptyList(list);
    }
    @Override
    public BisAccDtlDTO getBisAccDtlByAccCurTime(String bankAcc, String curId, Date transTime) {
        BisAccDtlVO bisAccDtlVO = bisAccDtlDao.getBisAccDtlVOByAccCurTime(bankAcc, curId, transTime);
        if (EmptyUtils.isEmpty(bisAccDtlVO)) {
            return null;
        }
        BisAccDtlDTO dtlDTO = new BisAccDtlDTO();
        BeanUtils.copyNotNullProperties(bisAccDtlVO, dtlDTO);
        return dtlDTO;
    }
    @Override
    public BigDecimal getDuringMoney(String curId, String bankAcc, Date beginDate, Date endDate) {
        return bisAccDtlDao.getDuringMoney(curId, bankAcc, beginDate, endDate);
    }
    /**
     * 电子回单勾兑根据条件获取银行流水
     *
     * @param dataTablesInput
     * @return
     */
    @Override
    public DataTablesOutput<BisAccDtlVO> getDataTablesUnionForReceiptBlending(DataTablesInput dataTablesInput) {
        return bisAccDtlDao.getDataTablesUnionForReceiptBlending(dataTablesInput);
    }
    @Override
    public void updateByClaimArr(String[] claimIdArr) {
        bisAccDtlDao.updateByClaimArr(claimIdArr);
    }
    /**
     * 根据流水id集合查询流水
     *
     * @param ids 流水ID 集合
     * @return
     */
    @Override
    public List<BisAccDtlDTO> getByIds(List<String> ids) {
        List<BisAccDtlDTO> list = new ArrayList<>();
        List<BisAccDtlVO> tempList = bisAccDtlDao.getByUnionIds(ids);
        if (!EmptyUtils.isEmpty(tempList)) {
            list = BeanUtils.copyListProperties(tempList, BisAccDtlDTO.class);
        }
        return list;
    }
    @Override
    public List<BisAccDtlVO> getListForFlush() {
        List<BisAccDtl> list = this.bisAccDtlDao.getListForFlush();
        List<BisAccDtlVO> listVO = BeanUtils.copyListProperties(list, BisAccDtlVO.class);
        return listVO;
    }
    /**
     * 根据流水余额
     *
     * @param dtl 流水
     */
    @Override
    public void updateBal(BtBankAccDtl dtl) {
        com.bytter.biz.bdlink.entity.BtBankAccDtl entity = new com.bytter.biz.bdlink.entity.BtBankAccDtl();
        BeanUtils.copyProperties(dtl, entity);
        bisAccDtlDao.updateBal(entity);
    }
    @Override
    public void save(BisAccDtlVO dtl) {
        com.bytter.biz.bdlink.entity.BisAccDtl entity = new com.bytter.biz.bdlink.entity.BisAccDtl();
        BeanUtils.copyProperties(dtl, entity);
        bisAccDtlDao.save(entity);
    }
    @Override
    public List<BisAccDtlVO> listAfterBtBankAccDtl(String bankAcc, String curId, Date transTime, String id, Integer orderNo) {
        List<BisAccDtl> list = bisAccDtlDao.listAfterBtBankAccDtl(bankAcc, curId, transTime, id, orderNo);
        List<BisAccDtlVO> listVO = BeanUtils.copyListProperties(list, BisAccDtlVO.class);
        return listVO;
    }
    @Override
    public void saveOrUpdate(BisAccDtlVO dtl) {
        BisAccDtl entity = new BisAccDtl();
        BeanUtils.copyProperties(dtl, entity);
        bisAccDtlDao.saveOrUpdate(entity);
    }
    /**
     * 余额刷新
     */
    @Override
    public void flushBalance() {
        List<BisAccDtlVO> list = this.getListForFlush();
        for (BisAccDtlVO data : list) {
            com.bytter.biz.bdlink.entity.BisAccDtl dtl = new com.bytter.biz.bdlink.entity.BisAccDtl();
            BeanUtils.copyProperties(data, dtl);
            this.updateAfterBtBankAccDtl(dtl);
        }
    }
    /**
     * 重新计算余额
     *
     * @param entity
     */
    private void updateAfterBtBankAccDtl(BisAccDtl entity) {
        List<BisAccDtl> afterList = this.bisAccDtlDao.listAfterBtBankAccDtl(entity.getBankAcc(), entity.getCurId());
        // 更新余额
        if (!EmptyUtils.isEmptyList(afterList)) {
            com.bytter.system.api.dto.BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(entity.getBankAcc(), entity.getCurId());
            BisAccDtl bisAccDtl = bisAccDtlDao.get(afterList.get(0).getId());
            BigDecimal lastBal = bisAccDtl.getBal();
            //第一次取初始化余额
            if (btBankAccDTO != null && btBankAccDTO.getInitBalance() != null && DateUtils.dayDiffs(bisAccDtl.getTransTime(), btBankAccDTO.getInitBalanceDate()) > 0) {
                lastBal = btBankAccDTO.getInitBalance();
            } else {
                lastBal = BigDecimal.ZERO;
            }
            List<BisAccDtl> newList = new ArrayList<>();
            for (BisAccDtl dtl : afterList) {
                boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(dtl.getRbSign());
                boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(dtl.getRbSign());
                if (outBlue || inRed) {
                    //支出(蓝) 收入(红)
                    dtl.setBal(lastBal.subtract(dtl.getAmt()));
                } else {
                    //收入(蓝) 支出(红)
                    dtl.setBal(lastBal.add(dtl.getAmt()));
                }
                lastBal = dtl.getBal();
                newList.add(dtl);
            }
//			this.batchSaveOrUpdate(newList);
            //只更新余额
            for (BisAccDtl dtl : newList) {
                bisAccDtlDao.updateBalById(dtl.getId(), dtl.getBal());
            }
        }
    }
    /**
     * 根据账号id和币别id查找账号明细最小时间
     *
     * @param bankAcc
     * @param curId
     * @return
     */
    @Override
    public Date getMinTransTime(String bankAcc, String curId) {
        return this.bisAccDtlDao.getMinTransTime(bankAcc, curId);
    }
    /**
     * 获取当天流水的结余
     *
     * @param bankAcc
     * @param curId
     * @param transTime
     * @return
     */
    @Override
    public BigDecimal getLastBal(String bankAcc, String curId, Date transTime) {
        BigDecimal lastBal = BigDecimal.ZERO;
        List<BisAccDtlVO> list = bisAccDtlDao.listBisAccDtlVOByAccCurTime(bankAcc, curId, transTime);
        for (BisAccDtlVO dtl : list) {
            boolean outBlue = BisAccBalConstant.CDSIGN_OUT.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_BLUE.equalsIgnoreCase(dtl.getRbSign());
            boolean inRed = BisAccBalConstant.CDSIGN_IN.equalsIgnoreCase(dtl.getCdSign()) && BisAccBalConstant.RBSIGN_RED.equalsIgnoreCase(dtl.getRbSign());
            if (outBlue || inRed) {
                //支出(蓝) 收入(红)
                lastBal = lastBal.subtract(dtl.getAmt());
            } else {
                //收入(蓝) 支出(红)
                lastBal = lastBal.add(dtl.getAmt());
            }
        }
        return lastBal;
    }
    @Override
    public BisAccDtlVO getBisAccDtlByAccNo(String accNo) {
        List<BisAccDtl> list = this.bisAccDtlDao.listBy("accNo", accNo);
        BisAccDtlVO dtl = null;
        BisAccDtl entity = null;
        if (!EmptyUtils.isEmptyList(list)) {
            entity = list.get(0);
            dtl = new BisAccDtlVO();
            BeanUtils.copyProperties(entity, dtl);
            return dtl;
        }
        return dtl;
    }
    @Override
    public List<BisAccDtlDTO> getClaimBillAccInfo(Map<String, Object> params, String corpAuthSql) {
        return bisAccDtlDao.getClaimBillAccInfo(params, corpAuthSql);
    }
    @Override
    public DataTablesOutput<BisAccDtlDTO> queryFcBankAccDtl(DataTablesInput input, String menuType, List<String> dtlIds) {
        return bisAccDtlDao.queryFcBankAccDtl(input, menuType, dtlIds);
    }
    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        return bisAccDtlService.getDataTablesUnion(dataTablesInput, user);
    }
    @Override
    public BisAccDtlDTO getByUnion(String id) {
        BisAccDtlVO bisAccDtlVO = this.bisAccDtlService.getByUnion(id);
        BisAccDtlDTO bisAccDtlDTO = new BisAccDtlDTO();
        if (bisAccDtlVO != null) {
            BeanUtils.copyNotNullProperties(bisAccDtlVO, bisAccDtlDTO);
        }
        BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(bisAccDtlDTO.getCurId());
        bisAccDtlDTO.setCurName(btCurrencyDTO.getCurName());
//        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(bisAccDtlDTO.getBankAcc());
        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfoByBankAcc(bisAccDtlDTO.getBankAcc());
        if(!EmptyUtils.isEmpty(bankAccInfo)){
            Map<String, String> nameMap = corpService.getNameByIds(Lists.newArrayList(bankAccInfo.getCorpId()));
            bisAccDtlDTO.setCorpName(nameMap.get(bankAccInfo.getCorpId()));
        }
        return bisAccDtlDTO;
    }
    @Override
    public DataTablesOutput getDataTablesMakeup(DataTablesInput dataTablesInput, ICurrentUser user) {
        return bisAccDtlService.getDataTablesMakeup(dataTablesInput, user);
    }
    @Override
    public void saveOrUpdate(BisAccDtlDTO bisAccDtl) {
        BisAccDtl t = bisAccDtlService.get(bisAccDtl.getId());
        BeanUtils.copyNotNullProperties(bisAccDtl, t);
        t.setUpdateDate(new Date());
//        bisAccDtlService.saveOrUpdate(t);
        bisAccDtlService.update(t);
    }
    @Override
    public List<BisAccDtlExportDTO> listExport(DataTablesInput dataTablesInput, ICurrentUser user) {

        //导出全部沿用列表查询方法，PageSize(1000000)添加不分页处理即可
        // if(true){
            // dataTablesInput.setPageSize(1000000);
            DataTablesOutput<BisAccDetailVO> data = getDataTablesUnion(dataTablesInput,user);
            List<BisAccDetailVO> bisAccDetailVO = data.getData();
            List<BisAccDtlExportDTO> dtlList = BeanUtils.copyListProperties(bisAccDetailVO,BisAccDtlExportDTO.class);
            return dtlList;
        // }
        // StopWatch stopWatch = new StopWatch("银行流水导出效率");
        // stopWatch.start("getCorpIdsByUserResource");
        // StringBuilder sql = new StringBuilder();
        // List<String> corpList = corpService.getCorpIdsByUserResource(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, user.getId());
        // stopWatch.stop();
        // stopWatch.start("listByMultiValAccs");
        // List<String> accList = accountGrantService.listByMultiValAccs(user.getId(), ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
        // stopWatch.stop();
        // //根据查询条件及账户数据进行一次过滤
        // stopWatch.start("getAcclistByCondition");
        // List<String> accList2 = bisAccDtlDao.getAcclistByCondition(dataTablesInput, corpList, accList);
        // String accAuthSql = SqlUtil.getInSql(accList2, "t.bank_acc", true);
        // sql.append(" and ").append(accAuthSql);
        // stopWatch.stop();
        // stopWatch.start("listExport");
        // List<BisAccDtlExportVO> dtlList = this.bisAccDtlDao.listExport(dataTablesInput, sql.toString());
        // stopWatch.stop();
        // stopWatch.start("listDictMap");
        // Map<String, Map<String, DictDTO>> dictMap = dictService.listDictMap("btBankAccDtl.cdSign", "btBankAcc.accType","sf_yn");
        // stopWatch.stop();
        // stopWatch.start("getNameByIds");
        // Map<String, String> corpNameMap = corpService.getNameByIds(dtlList.stream().map(BisAccDtlExportVO::getCorpId).collect(Collectors.toList()));
        // stopWatch.stop();
        // stopWatch.start("getCurrencyNameByIds");
        // Map<String, String> currencyNameMap = currencyService.getCurrencyNameByIds(dtlList.stream().map(BisAccDtlExportVO::getCurId).collect(Collectors.toList()));
        // stopWatch.stop();
        // stopWatch.start("组装数据");
        // //查询所有账户组
        // List<FcBankAccGroupDTO> validPayType = fcBankAccGroupService.getValidPayType();
        // HashMap<String, String> map = new HashMap<>();
        // for (FcBankAccGroupDTO fcBankAccGroup : validPayType) {
        //     map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
        // }
        // for(BisAccDtlExportVO dtlVo : dtlList) {
        //     dtlVo.setCorpName(corpNameMap.get(dtlVo.getCorpId()));
        //     dtlVo.setCurName(currencyNameMap.get(dtlVo.getCurId()));
        //     if(StringUtils.isNotEmpty(dtlVo.getCdSign())){
        //         dtlVo.setCdSignName(dictMap.get("btBankAccDtl.cdSign").getOrDefault(dtlVo.getCdSign(),DictDTO.EMPTY).getName());
        //     }
        //     if(StringUtils.isNotEmpty(dtlVo.getAccType())){
        //         dtlVo.setAccTypeName(dictMap.get("btBankAcc.accType").getOrDefault(dtlVo.getAccType(),DictDTO.EMPTY).getName());
        //     }
        //     if(StringUtils.isNotEmpty(dtlVo.getIsOnline())){
        //         dtlVo.setIsOnlineStr(dictMap.get("sf_yn").getOrDefault(dtlVo.getIsOnline(),DictDTO.EMPTY).getName());
        //     }
        //     //查询账户组名称
        //     String fcAccGroupCode = dtlVo.getFcAccGroupCode();
        //     if (EmptyUtils.isNotEmpty(fcAccGroupCode)){
        //         List<String> codes = Arrays.asList(fcAccGroupCode.split(","));
        //         StringBuilder accGroupName = new StringBuilder();
        //         for(int i = 0;i<=codes.size()-1;i++){
        //             if (i==codes.size()-1){
        //                 if (map.containsKey(codes.get(i))){
        //                     accGroupName.append(map.get(codes.get(i)));
        //                 }
        //             }else{
        //                 if (map.containsKey(codes.get(i))){
        //                     accGroupName.append(map.get(codes.get(i))+",");
        //                 }
        //             }
        //         }
        //         dtlVo.setFcAccGroupName(accGroupName.toString());
        //     }
        //
        //     // 推送状态
        //     String tempStatus = "否";
        //     if (!EmptyUtils.isEmpty(dtlVo.getSyncStatus()) && "S".equals(dtlVo.getSyncStatus())){
        //         tempStatus = "是";
        //     }
        //     dtlVo.setSyncStatus(tempStatus);
        // }
        // stopWatch.stop();
        // log.error(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        // return BeanUtils.copyListProperties(dtlList, BisAccDtlExportDTO.class);
    }
    /**
     * 通过sql语句查询明细列表
     *
     * @param params
     * @param sql
     * @return
     */
    @Override
    public List<BisAccDtlVO> getBisAccDtlListBySql(Map<String, Object> params, String sql) {
        return bisAccDtlDao.getBisAccDtlListBySql(params, sql);
    }
    @Override
    public DataTablesOutput getInterfaceFcBankBill(DataTablesInput dataTablesInput, String jionSql) {
        DataTablesOutput output = bisAccDtlDao.getInterfaceFcBankBill(dataTablesInput, jionSql);
        List<FcBankBillVo> voList = output.getData();
        if (voList.size() > 0) {
            Map<String, BtCurrencyDTO> curMap = currencyService.getCurrency();
            for (FcBankBillVo vo : voList) {
                vo.setCurName(curMap.get(vo.getCurId()).getCurName());
            }
        }
        return output;
    }

    /**
     * 查询页面数据-记账
     */
    @Override
    public List<BisAccDtlDTO> queryBisAccDtlDTO(DataTablesInput dataTablesInput, String menuType, List<String> accList) {
        return bisAccDtlDao.queryBisAccDtlDTO(dataTablesInput, menuType, accList);
    }
    /**
     * 通过bankids的还原银行明细导出标志
     */
    @Override
    public void recoverBankBillImportSign(String bankIds) {
        bisAccDtlDao.recoverBankBillImportSign(bankIds);
    }

    /**
     * 通过bankids的修改银行明细导出标志
     */
    @Override
    public int updateBankBillImportSign(String bankIds) {
      return   bisAccDtlDao.updateBankBillImportSign(bankIds);
    }

    /**
     * 获取银行明细列表
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    @Override
    public DataTablesOutput getDataTablesUnionPaymentBlending(DataTablesInput dataTablesInput, String joinSql, String allocationAllowBlending) {
        return bisAccDtlDao.getDataTablesUnionPaymentBlending( dataTablesInput,  joinSql, allocationAllowBlending);
    }

    /**
     * 获取银行明细列表
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    @Override
    public DataTablesOutput getDataTablesUnionRefundBlending(DataTablesInput dataTablesInput, String joinSql) {
        return bisAccDtlDao.getDataTablesUnionRefundBlending( dataTablesInput,  joinSql);
    }

    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        return bisAccDtlBudgetRuleDao.getDataTablesUnion(dataTablesInput,joinSql);
    }

    @Override
    public BisAccDtlBudgetRuleDTO getBisAccDtlByIdUnion(String id) {
        return bisAccDtlBudgetRuleDao.getBisAccDtlByIdUnion(id);
    }

    /**
     * 根据合并id查到流水明细
     * @param mergeId
     * @return
     */
    @Override
    public List<BisAccDtlBudgetRuleDTO> getBisAccDtlByMergeId(String mergeId) {
        return bisAccDtlBudgetRuleDao.getBisAccDtlByMergeId(mergeId);
    }

    /**
     * 获取流水实体转的dto
     * @param id
     * @return
     */
    @Override
    public BisAccDtlDTO getDTOById(String id) {
        BisAccDtl bisAccDtl = bisAccDtlDao.get(id,true);
        if(EmptyUtils.isEmpty(bisAccDtl)){
            return null;
        }else{
            BisAccDtlDTO bisAccDtlDTO = new BisAccDtlDTO();
            BeanUtils.copyNotNullProperties(bisAccDtl,bisAccDtlDTO);
            return bisAccDtlDTO;
        }
    }

    @Override
    public BisAccDtlDTO getUniqueBy(String propertyName, Object value) {
        BisAccDtl bisAccDtl = bisAccDtlDao.getUniqueBy(propertyName, value);
        if(EmptyUtils.isEmpty(bisAccDtl)){
            return null;
        }else{
            BisAccDtlDTO bisAccDtlDTO = new BisAccDtlDTO();
            BeanUtils.copyNotNullProperties(bisAccDtl,bisAccDtlDTO);
            return bisAccDtlDTO;
        }
    }

    @Override
    public List<BisAccDtlDTO> listBy(String propertyName, Object value) {
        List<BisAccDtl> list = bisAccDtlDao.listBy(propertyName, value);
        if(EmptyUtils.isEmpty(list)){
            return null;
        }else{
            List<BisAccDtlDTO> bisAccDtlDTOList = BeanUtils.copyListProperties(list,BisAccDtlDTO.class);
            return bisAccDtlDTOList;
        }
    }

    @Override
    public int interestDtlAutoClaim(String bankTypeId, String remark, Date transTime, String busType) {
        log.info("开始执行银行【{}】的利息流水自动认领业务。", bankTypeId);
        int count = bisAccDtlDao.countDtlByInterestRule(bankTypeId, remark, transTime, busType);
        log.info("银行【{}】交易日期【{}】符合用途摘要信息【{}】的流水有【{}】条。", bankTypeId, transTime, remark, count);
        if (count > 0) {
            // 根据记录数量决定分页大小，避免流水数据量过大，造成内存溢出
            int pageSize = count > 100000 ? 10000 : 1000;
            for (int i = 0; i < count; i += pageSize) {
                // 分页处理，因事务级别为【2-读已提交】，每次查询结果会实时变化，每次从0开始获取
                processPage(bankTypeId, remark, transTime, busType, 0, Math.min(pageSize, count - i));
            }
        }
        return count;
    }

    /**
     * 辅助方法：分页认领银行利息流水。
     *
     * @param bankTypeId 银行类型编号。
     * @param remark     用途摘要。
     * @param transTime  交易时间
     * @param busType    认领业务类型。
     * @param startIndex 开始处理行数。
     * @param maxIndex   结束处理行数。
     */
    private void processPage(String bankTypeId, String remark, Date transTime, String busType, Integer startIndex, Integer maxIndex) {
        log.info("开始处理银行【{}】从【{} - {}】的流水认领数据。", bankTypeId, startIndex, maxIndex);
        List<BisAccDtlVO> bisAccDtlList = bisAccDtlDao.listDtlByInterestRule(bankTypeId, remark, transTime, busType, startIndex, maxIndex);
        if (ObjectUtil.isNotEmpty(bisAccDtlList)) {
            List<String> dtlIdList = bisAccDtlList.stream().map(BisAccDtlVO::getId).collect(Collectors.toList());
            int count = bisAccDtlDao.updateBusTypeByIdList(dtlIdList, busType);
            log.info("银行【{}】利息流水自动认领【{}】条。", bankTypeId, count);
        }
    }


    /**
     * 更新流水勾兑状态
     *
     * @param id
     * @param blendSign
     * @return
     */
    @Override
    public int toUpdateBlendSign(String id, String blendSign, String userName){
       return bisAccDtlDao.toUpdateBlendSign(id,blendSign, userName);
    }
    /**
     * 更新流水银行流水号
     *
     * @param id
     * @param bankSerialNo
     * @return
     */
    @Override
    public int toUpdateBankSerialNo(String id, String bankSerialNo) {
        return bisAccDtlDao.toUpdateBankSerialNo(id,bankSerialNo);
    }

    @Override
    public IPageResult<BisAccDtlVO> getPageListByBankAcc(IParameterInput parameterInput, String bankAcc, String curId, String remark, String beginDate, String endDate){
        IPageResult<BisAccDtlVO> pageResult = bisAccDtlDao.getPageListByBankAcc(parameterInput,bankAcc,curId,remark,beginDate,endDate);
        return pageResult;
    }

    @Override
    public BigDecimal getListByBankAccAmt(String bankAcc, String curId, String remark,String cdSign) {
        BisAccDtlVO VO = this.bisAccDtlDao.getListByBankAccAmt(bankAcc, curId, remark,cdSign);
        return VO != null ? VO.getAmt() : BigDecimal.ZERO;
    }


    @Override
    public BigDecimal getListWithTransTime(String bankAcc, String curId, String remark, String cdSign, String beginDate, String endDate) {
        BisAccDtlVO VO = this.bisAccDtlDao.getListWithTransTime(bankAcc, curId, remark,cdSign,beginDate,endDate);
        return VO != null ? VO.getAmt() : BigDecimal.ZERO;
    }

    /**
     * 根据账号币别获取流水
     * @param bankAcc
     * @param curId
     * @return
     */
    @Override
    public List<BisAccDtlVO> getListByBankAcc(String bankAcc, String curId, String remark, String beginDate, String endDate) {
        return this.bisAccDtlDao.getListByBankAcc(bankAcc, curId, remark, beginDate, endDate);
    }

    @Override
    public int fundTansferFlowAutoClaim(String busType, List<NisFlowRulePurposeDTO> list, String transferMode) {
        log.info("开始执业务类型【{}】的资金调拨认领业务。", busType);
        int count = bisAccDtlDao.countDtlByFlowRule(list, transferMode);
        log.info("符合资金调拨规则的流水有【{}】条。", count);
        if (count > 0) {
            // 根据记录数量决定分页大小，避免流水数据量过大，造成内存溢出
            int pageSize = count > 100000 ? 10000 : 1000;
            for (int i = 0; i < count; i += pageSize) {
                // 分页处理，因事务级别为【2-读已提交】，每次查询结果会实时变化，每次从0开始获取
                autoClaim(busType, list,transferMode,0, Math.min(pageSize, count - i));
            }
        }
        return count;
    }

    @Override
    public List<BisAccDtlDTO> listBisAccDtlByBusType(Date queryDate, List<String> busTypes) {
        return bisAccDtlDao.listBisAccDtlByBusType(queryDate, busTypes);
    }

    @Override
    public List<BisAccDtlDTO> listBisAccDtlByBankAccQueryDateBusTypes(List<String> bankAccs,Date queryDate, List<String> busTypes) {
        return bisAccDtlDao.listBisAccDtlByBankAccQueryDateBusTypes(bankAccs,queryDate, busTypes);
    }

    @Override
    public List<BisAccDtlDTO> listBisAccDtlByCorpIdsQueryDateBusTypes(List<String> corpIds,Date queryDate, List<String> busTypes) {
        return bisAccDtlDao.listBisAccDtlByCorpIdsQueryDateBusTypes(corpIds,queryDate, busTypes);
    }

    @Override
    public List<BisAccDtlDTO> listBisAccDtlByCorpIdsQueryDateBusTypes(List<String> corpIds,List<String> busTypes,Date queryDateBegin,Date queryDateEnd,String accountGroup) {
        return bisAccDtlDao.listBisAccDtlByCorpIdsQueryDateBusTypes(corpIds,busTypes,queryDateBegin,queryDateEnd,accountGroup);
    }

    /**
     * 分页认领银行利息流水。
     * @param busType    认领业务类型。
     * @param startIndex 开始处理行数。
     * @param maxIndex   结束处理行数。
     */
    private void autoClaim(String busType, List<NisFlowRulePurposeDTO> list,String transferMode, Integer startIndex,Integer maxIndex) {
        List<BisAccDtlVO> bisAccDtlList = bisAccDtlDao.listDtlByFlowRule(startIndex, maxIndex, list, transferMode);
        if (ObjectUtil.isNotEmpty(bisAccDtlList)) {
            List<String> dtlIdList = bisAccDtlList.stream().map(BisAccDtlVO::getId).collect(Collectors.toList());
            int count = bisAccDtlDao.updateBusTypeByIdList(dtlIdList, busType);
        }
    }

    @Override
    public List<String> getAcclistByCondition(DataTablesInput dataTablesInput, List<String> corpList, List<String> accList) {
        return bisAccDtlDao.getAcclistByCondition(dataTablesInput, corpList, accList);
    }

	@Override
	public void updateBisAccDtlDTOByAutoAndAgainSign(List<BisAccDtlDTO> bisAccDtlDTOs) {
		if(CollectionUtils.isNotEmpty(bisAccDtlDTOs)) {
			log.info("bisAccDtlDTOs size ==================="+bisAccDtlDTOs.size());
			Map<String,String> mapAuthSign = bisAccDtlDTOs.stream().collect(Collectors.toMap(BisAccDtlDTO::getId, BisAccDtlDTO::getAutoSign));
			Map<String,String> mapAgainSign = bisAccDtlDTOs.stream().collect(Collectors.toMap(BisAccDtlDTO::getId, BisAccDtlDTO::getAgainSign));
			List<String> ids = bisAccDtlDTOs.stream().map(BisAccDtlDTO::getId).collect(Collectors.toList());
			List<BisAccDtl> bisAccDtlList = bisAccDtlService.getByIds(ids);
			//替换bisAccDtl的autoSign againSign值
			for(BisAccDtl bisAccDtl : bisAccDtlList)
			{
				bisAccDtl.setAutoSign(mapAuthSign.get(bisAccDtl.getId()));
				bisAccDtl.setAgainSign(mapAgainSign.get(bisAccDtl.getId()));
			}
			bisAccDtlService.batchUpdate(bisAccDtlList, 500);
			log.info("bisAccDtlDTOs size finish==================="+bisAccDtlDTOs.size());
			}
	}

    @Override
    public BigDecimal getBnakAccDtlForReportDate(List<String> corpIds, Date startDate, Date endDate, String bankTypeId, String bankCode) {
        return bisAccDtlDao.getBnakAccDtlForReportDate(corpIds, startDate, endDate, bankTypeId, bankCode);
    }

    @Override
    public BigDecimal getBalFromDetail(String bankAcc, String curId, Date transTime) {
        return bisAccDtlService.getBalFromDetail(bankAcc, curId, transTime);
    }
}