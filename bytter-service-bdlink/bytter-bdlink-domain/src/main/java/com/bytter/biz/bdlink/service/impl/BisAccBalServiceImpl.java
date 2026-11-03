package com.bytter.biz.bdlink.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;

import com.alibaba.excel.util.ObjectUtils;
import com.bytter.biz.bdlink.api.constant.BisAccBalConstant;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.dto.BtBankAccDTO;
import com.bytter.biz.bdlink.api.dto.DepositReportCommonDTO;
import com.bytter.biz.bdlink.api.enums.AccBalMessageEnum;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.dao.BisAccBalDao;
import com.bytter.biz.bdlink.entity.BisAccBal;
import com.bytter.biz.bdlink.service.BisAccBalService;
import com.bytter.biz.bdlink.vo.BisAccBalVO;
import com.bytter.biz.bdlink.vo.BisAccDtlBillVO;
import com.bytter.biz.bdlink.vo.BtBankAccBalVO;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.NisWorkFlowConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.collect.Maps;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 银行账户余额管理Service接口实现类
 *
 * @author zuoy
 */
@Service("bisAccBalServiceImpl")
@DubboService
@Transactional
public class BisAccBalServiceImpl implements IBisAccBalService {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BisAccBalService bisAccBalService;
    @Autowired
    private BisAccBalDao bisAccBalDao;
    @Autowired(required = false)
    private ICorpService corpService;
    @Autowired(required = false)
    private IFcExcrateAdjustService fcExcrateAdjustService;
    @Autowired(required = false)
    private IDictService dictService;
    @DubboReference(check = false)
    ICorpService abCorpService;
    @DubboReference(check = false)
    private IBtBankAccFreezeService btBankAccFreezeService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IBisExcService iBisExcService;

    @Autowired
    private IWorkFlowService iWorkFlowService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    
    /**
     * 根据id得到实体
     *
     * @param id
     * @return BtBankAccDTO
     */
    @Override
    public BisAccBalDTO getById(String id) {
        BisAccBalVO balVO = bisAccBalService.getByUnion(id);
        if (EmptyUtils.isEmpty(balVO)) {
            return null;
        }
        BisAccBalDTO dto = new BisAccBalDTO();
        BeanUtils.copyNotNullProperties(balVO, dto);
        return dto;
    }
    /**
     * 根据条件查询银行账户余额
     *
     * @param banAcc 账户
     * @return BtBankAccDTO
     */
    @Override
    public List<BisAccBalDTO> listBisAccBalBy(String banAcc) {
        List<BisAccBalVO> balList = bisAccBalService.listBisAccBalBy(banAcc, null);
        if (EmptyUtils.isEmptyList(balList)) {
            return null;
        }
        List<BisAccBalDTO> dtoList = BeanUtils.copyListProperties(balList, BisAccBalDTO.class);
        return dtoList;
    }
    /**
     * 根据条件查询银行账户最近一次的余额信息
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @return BtBankAccDTO
     */
    @Override
    public BisAccBalDTO getLastBisAccBalBy(String bankAcc, String curId) {
        BisAccBal bal = bisAccBalService.getBisAccBalBy(bankAcc, curId, null, true);
        if (EmptyUtils.isEmpty(bal)) {
            return null;
        }
        BisAccBalDTO dto = new BisAccBalDTO();
        BeanUtils.copyNotNullProperties(bal, dto);
        return dto;
    }
    
//    @Override
//    public List<BisAccBalDTO> getLastBisAccBalByList(List<String> bankAccs, List<String> curIds) {
//        List<BisAccBal> balList = bisAccBalService.getBisAccBalByList(bankAccs, curIds, null, true);
//        List<BisAccBalDTO> list = new ArrayList<BisAccBalDTO>();
//        if (CollectionUtils.isEmpty(balList)) {
//            return list;
//        }
//        list = BeanUtils.copyListProperties(balList, BisAccBalDTO.class);
//        return list;
//    }
    /**
     * 更新银行账户冻结余额与可用余额
     *
     * @param bankAcc    账户
     * @param curId      币别ID
     * @param updateDate 更新日期
     * @param frzBal     冻结余额
     * @param isFrozen   true-->冻结， false-->解冻
     */
    @Override
    public void updateFrzBalAndAvailBal(String bankAcc, String curId, Date updateDate, BigDecimal frzBal, boolean isFrozen, ICurrentUser user) {
        bisAccBalService.updateFrzBalAndAvailBal(bankAcc, curId, updateDate, frzBal, isFrozen, user);
    }

    @Override
    public void updateFrzBalAndAvailBal(String bankAcc, String curId, Date updateDate,String planFlag, BigDecimal frzBal, boolean isFrozen, ICurrentUser user) {
        bisAccBalService.updateFrzBalAndAvailBal(bankAcc, curId, updateDate, planFlag,frzBal, isFrozen, user);
    }
    @Override
    public BigDecimal getLastByBalDate(String bankAcc, String curId) {
        return this.bisAccBalService.getLastByBalDate(bankAcc, curId, true);
    }
    @Override
    public BigDecimal getBalByBalDate(String bankAcc, String curId, Date balDate) {
        return this.bisAccBalService.getBalByBalDate(bankAcc, curId, balDate, true);
    }
    @Override
    public BigDecimal getCurBalOrLastBal(String bankAcc, String curId, Date balDate) {
        return this.bisAccBalService.getCurBalOrLastBal(bankAcc, curId, balDate, true);
    }
    @Override
    public BigDecimal getCurBalOrLastRealBal(String bankAcc, String curId, Date balDate) {
        return this.bisAccBalService.getCurRealBalOrLastBal(bankAcc, curId, balDate, true);
    }
    @Override
    public BigDecimal getBalByBalDate(String bankAcc, String curId, Date balDate, String operator) {
        return this.bisAccBalService.getBalByBalDate(bankAcc, curId, balDate, operator, true);
    }
    /**
     * 外接口保存余额
     *
     * @param dto
     */
    @Override
    public void saveBalByDto(BisAccBalDTO dto) {
        BisAccBal bal = new BisAccBal();
        BeanUtils.copyNotNullProperties(dto, bal);
        bisAccBalService.save(bal);
    }
    /**
     * 外接口更新余额
     *
     * @param dto
     */
    @Override
    public void updateBalByDto(BisAccBalDTO dto) {
        BisAccBal bal = bisAccBalService.get(dto.getId(), true);
        BeanUtils.copyNotNullProperties(dto, bal);
        bisAccBalService.update(bal);
    }
    @Override
    public BisAccBalDTO getBisAccBalBy(String bankAcc, String curId, Date balDate) {
        BisAccBal bal = bisAccBalService.getBisAccBalBy(bankAcc, curId, balDate, true);
        if (EmptyUtils.isEmpty(bal)) {
            return null;
        }
        BisAccBalDTO dto = new BisAccBalDTO();
        BeanUtils.copyNotNullProperties(bal, dto);
        return dto;
    }
    /**
     * 获取账户余额（活期存款）
     *
     * @param user     用户
     * @param resource 资源
     * @return
     */
    @Override
    public List<DepositReportCommonDTO> getBankAccBalByUser(ICurrentUser user, String resource, String corpId) {
        List<DepositReportCommonDTO> list = new ArrayList<>();
        //当前余额
        //获取账户汇总余额数据
        Date curDate = DateUtils.getDateTime();

        List<BisAccBalVO> curBalanceList = this.bisAccBalService.listBalSumByCurId(user, resource, curDate);
        Map<String, DepositReportCommonDTO> curDeposit = calculateBal(curBalanceList, curDate, corpId);
        //上月末余额
        Date preMonthDate = DateUtils.getLastMonthDate(DateUtils.addMonth(DateUtils.getDateTime(), -1));
        List<BisAccBalVO> preMonthBalanceList = this.bisAccBalService.listBalSumByCurId(user, resource, preMonthDate);
        Map<String, DepositReportCommonDTO> preMonthDeposit = calculateBal(preMonthBalanceList, preMonthDate, corpId);
        // 设置同比
        for (Map.Entry<String, DepositReportCommonDTO> curEntry : curDeposit.entrySet()) {
            for (Map.Entry<String, DepositReportCommonDTO> preMonthEntry : preMonthDeposit.entrySet()) {
                if (curEntry.getKey().equalsIgnoreCase(preMonthEntry.getKey())) {
                    DepositReportCommonDTO value = curEntry.getValue();
                    BigDecimal curBalance = value.getBalance();
                    BigDecimal preMonthBalance = preMonthEntry.getValue().getBalance();
                    // 计算同比
                    BigDecimal monthOnMonth = NumberUtil.calculateMonthOnMonth(curBalance, preMonthBalance);
                    value.setMonthOnMonth(monthOnMonth);
                    list.add(value);
                }
            }
        }
        return list;
    }
    /**
     * 计算余额
     *
     * @param listBal
     * @param searchDate
     * @param corpId
     * @return
     */
    private Map<String, DepositReportCommonDTO> calculateBal(List<BisAccBalVO> listBal, Date searchDate, String corpId) {
        //获取当前用户所属单位
        com.bytter.system.api.dto.CorpDTO corp = this.corpService.get(corpId);
        // 初始化所有存款类型
        List<DictDTO> dictList = dictService.list("btBankAcc.accType");
        List<DictDTO> dictDtoList = BeanUtils.copyListProperties(dictList, DictDTO.class);
        Map<String, DepositReportCommonDTO> map = Maps.newHashMap();
        for (DictDTO dict : dictDtoList) {
            DepositReportCommonDTO depositReportCommonDTO = new DepositReportCommonDTO();
            depositReportCommonDTO.setBusType(dict.getCode());
            depositReportCommonDTO.setBusTypeName(dict.getName());
            depositReportCommonDTO.setBalance(BigDecimal.ZERO);
            depositReportCommonDTO.setProportion(BigDecimal.ZERO);
            depositReportCommonDTO.setMonthOnMonth(BigDecimal.ZERO);
            map.put(dict.getCode(), depositReportCommonDTO);
        }
        //余额
        DepositReportCommonDTO deposit = null;
        for (BisAccBalVO vo : listBal) {
            BigDecimal balance = BigDecimal.ZERO;
            if (!map.containsKey(vo.getAccType())) {
                continue;
            }
            deposit = map.get(vo.getAccType());
            //判断存款币种与用户所属单位的本位币是否一致，不同则需要折算成单位本位币
            if (corp.getCurId().equals(vo.getCurId())) {
                balance = balance.add(vo.getBal());
            } else {
                BigDecimal exChangeRate = this.fcExcrateAdjustService.getExchangeRateByNoCheck(vo.getCurId(), corp.getCurId(), searchDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                if (null == exChangeRate) {
                    exChangeRate = BigDecimal.ONE;
                }
                balance = balance.add(vo.getBal().multiply(exChangeRate));
            }
            deposit.setBalance(balance);
        }
        return map;
    }
    @Override
    public BisAccBalDTO getInitBisAccBal(String bankAcc, String curId) {
        BisAccBal entity = bisAccBalService.getInitBisAccBal(bankAcc, curId);
        BisAccBalDTO bisAccBalDTO = new BisAccBalDTO();
        if (entity != null) {
            BeanUtils.copyNotNullProperties(entity, bisAccBalDTO);
        }
        return bisAccBalDTO;
    }
    @Override
    public void saveAccBalByDtl(BisAccBalDTO bisAccBalDTO, ICurrentUser user) {
        bisAccBalService.saveAccBalByDtl(bisAccBalDTO, user);
    }
    @Override
    public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, ICurrentUser user) {
        return bisAccBalService.getDataTablesQuery(dataTablesInput, user);
    }
    @Override
    public DataTablesOutput getDataTablesApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = iWorkFlowService.getWorkFlowBusiness(NisWorkFlowConstant.BT_BANKACC_BAL, dataTablesInput);
        String idsSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "b.id", true);
        DataTablesOutput dp = this.bisAccBalDao.getDataTablesApprove(dataTablesInput, idsSql);
        if(ObjectUtils.isEmpty(dp) || CollectionUtils.isEmpty(dp.getData())) {
        	dp = new DataTablesOutput();
        	return dp;
        }else {
        	List<BisAccBalVO> list = dp.getData();
        	List<String> curIds = list.stream().map(BisAccBalVO::getCurId).collect(Collectors.toList());
        	if(!CollectionUtils.isEmpty(curIds)) {
        		Map<String, String> mapCur = btCurrencyService.getCurrencyNameByIds(curIds);
        		for(BisAccBalVO vo : list) {
        			vo.setCurName(mapCur.get(vo.getCurId()));
        		}
        		dp.setData(list);
        	}
        	
        }
        return workFlowBusiness.merge(dp);
    }
    @Override
    public BisAccBalDTO getBisAccBalBy(String bankAcc, String curId, Date balDate, boolean status) {
        BisAccBal entity = bisAccBalService.getBisAccBalBy(bankAcc, curId, balDate, status);
        BisAccBalDTO bisAccBalDTO = null;
        if (!EmptyUtils.isEmpty(entity)) {
            bisAccBalDTO = new BisAccBalDTO();
            BeanUtils.copyNotNullProperties(entity, bisAccBalDTO);
        }
        return bisAccBalDTO;
    }
    @Override
    public BigDecimal getLastByBalDate(String bankAcc, String curId, boolean status) {
        return this.getBalByBalDate(bankAcc, curId, null, null, status);
    }
    /**
     * 根据条件组合查询余额
     *
     * @param bankAcc  账户
     * @param curId    币别ID
     * @param balDate  比较日期，为null则查询最近一次余额
     * @param operator 跟balDate组合使用，日期比较运算符，参照MatchTypeEnum
     * @param status   状态，true - 查询已完成 false - 查询未删除
     * @return BigDecimal
     */
    public BigDecimal getBalByBalDate(String bankAcc, String curId, Date balDate, String operator, boolean status) {
        if (EmptyUtils.isNotEmpty(operator) && EmptyUtils.isEmpty(balDate)) {
            return null;
        }
        return this.bisAccBalService.getBalByBalDate(bankAcc, curId, balDate, operator, status);
    }
    public List<BtBankAccBalVO> queryBankAccBalanceById(List<String> bankAccs, List<String> curIds) {
        return this.bisAccBalDao.queryBankAccBalanceById(bankAccs, curIds);
    }
    @Override
    public BtBankAccBalVO getFexCorpPoolBankAccBalance(Date balDate, String bankAcc, String curId) {
        return this.bisAccBalService.getFexCorpPoolBankAccBalance(balDate, bankAcc, curId);
    }
    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        return bisAccBalService.getDataTablesUnion(dataTablesInput, user);
    }
    @Override
    public void initBalSave(BisAccBalDTO bisAccBalVO, ICurrentUser user) {
        List<Map<String, Object>> btBankAccList = bisAccBalVO.getBtBankAccList();
        if (!ListUtils.isNullOrEmpty(btBankAccList)) {
            List<BisAccBal> addList = new ArrayList();
            List<BisAccBal> updateList = new ArrayList();
            Date curDate = DateUtils.getDate();
            String loginName = user.getLoginName();
            Date getDate = DateUtils.getDate();
            for (Map<String, Object> map : btBankAccList) {
                String balStr = String.valueOf(map.get("accBal"));
                String availBalStr = String.valueOf(map.get("accAvailBal"));
                String balDateStr = String.valueOf(map.get("balDate"));
                // 判断余额
                if (EmptyUtils.isEmpty(balStr) || balStr.equals("null")) {
                    throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_INITBAL_NULL);
                }
                // 判断余额日期
                if (EmptyUtils.isEmpty(balDateStr) || balStr.equals("null")) {
                    throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_INITBALDATE_NULL);
                }
                String curId = String.valueOf(map.get("accCurId"));
                String bankAcc = String.valueOf(map.get("bankAcc"));
                String bifCode = EmptyUtils.isEmpty(map.get("accBifCode")) ? "" : String.valueOf(map.get("accBifCode"));
                String corpId = String.valueOf(map.get("accCorpId"));
                Date balDate = DateUtils.str2Date(balDateStr, DateUtils.date_sdf());
                //获取账号本身的初始化余额
                BisAccBal initBal = bisAccBalService.getInitBisAccBal(bankAcc, curId);
                BisAccBal entity = bisAccBalService.getBisAccBalBy(bankAcc, curId, balDate, false);
                //获取当天的冻结金额
                BigDecimal freezeMoney = btBankAccFreezeService.getFreezeMoneyByBankAcc(bankAcc, curId, balDate);
                if (entity != null) {
                    if (initBal != null && !initBal.getId().equals(entity.getId())) {
                        //取消初始化余额标志
                        initBal.setInitSign(CommonConstant.NO);
                        updateList.add(initBal);
                    }
                    entity.setInitSign(CommonConstant.YES);
                    entity.setBal(new BigDecimal(balStr));
                    entity.setFrzBal(freezeMoney);
                    if (EmptyUtils.isBlank(availBalStr)) {
                        entity.setAvailBal(entity.getBal().subtract(freezeMoney));
                    } else {
                        entity.setAvailBal(new BigDecimal(availBalStr));
                    }
                    entity.setBalDate(balDate);
                    entity.setGetDate(getDate);
                    entity.setStatus(BisAccBalConstant.STATUE_CHECK.intValue());
                    entity.setUpdateBy(loginName);
                    entity.setUpdateDate(curDate);
                    updateList.add(entity);
                } else {
                    entity = new BisAccBal();
                    BeanUtils.copyNotNullProperties(bisAccBalVO, entity);
                    //entity.setId("");
                    entity.setBifCode(bifCode);
                    entity.setCorpId(corpId);
                    entity.setCurId(curId);
                    entity.setBankAcc(bankAcc);
                    entity.setBal(new BigDecimal(balStr));
                    entity.setFrzBal(freezeMoney);
                    if (EmptyUtils.isBlank(availBalStr)) {
                        entity.setAvailBal(entity.getBal().subtract(freezeMoney));
                    } else {
                        entity.setAvailBal(new BigDecimal(availBalStr));
                    }
                    entity.setBalDate(balDate);
                    entity.setGetDate(getDate);
                    // 设置单号
                    String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
                    entity.setAccNo(accNo);
                    // 当前操作人信息
                    entity.setCreateBy(loginName);
                    entity.setCreateDate(curDate);
                    entity.setInitSign(CommonConstant.YES);
                    entity.setStatus(BisAccBalConstant.STATUE_CHECK.intValue());
                    addList.add(entity);
                }
            }
            if (addList.size() > 0) {
                bisAccBalService.batchSave(addList);
            }
            if (updateList.size() > 0) {
                bisAccBalService.batchSaveOrUpdate(updateList);
            }
        }
    }
    @Override
    public void batchDelete(String ids) {
        bisAccBalService.batchDelete(ids);
    }
    @Override
    public void batchSubmit(String ids, ICurrentUser user) {
        bisAccBalService.batchSubmit(ids, user);
    }
    @Override
    public void delete(String id) {
        bisAccBalService.delete(id);
    }
    @Override
    public BisAccBalDTO getByUnion(String id) {
        BisAccBalVO bisAccBalVO = this.bisAccBalService.getByUnion(id);
        com.bytter.system.api.dto.BtBankAccDTO btBankAccDTO= btBankAccService.getBankAccInfo(bisAccBalVO.getBankAcc());
        BisAccBalDTO bisAccBalDTO = new BisAccBalDTO();
        if (bisAccBalVO != null) {
            BeanUtils.copyNotNullProperties(bisAccBalVO, bisAccBalDTO);
        }
        if(!EmptyUtils.isEmpty(btBankAccDTO)){
            bisAccBalDTO.setBankName(btBankAccDTO.getBankName());
            bisAccBalDTO.setAccName(btBankAccDTO.getAccName());
        }
        return bisAccBalDTO;
    }
    @Override
    public void saveImport(List<BisAccBalDTO> listDTO, ICurrentUser user) {
        if (!CollectionUtil.isEmpty(listDTO)) {
            List<BisAccBalVO> list = BeanUtils.copyListProperties(listDTO, BisAccBalVO.class);
            bisAccBalService.saveImport(list, user);
        }
    }
    @Override
    public List listExport(DataTablesInput dataTablesInput, ICurrentUser user) {
        return bisAccBalService.listExport(dataTablesInput, user);
    }
    @Override
    public void saveComplete(BisAccBalDTO bisAccBalDTO, ICurrentUser user) {
        BisAccBalVO vo = new BisAccBalVO();
        BeanUtils.copyNotNullProperties(bisAccBalDTO, vo);
        bisAccBalService.saveComplete(vo, user);
    }
    @Override
    public void saveBisAccBal(BisAccBalDTO bisAccBalDTO, ICurrentUser user) {
        BisAccBalVO vo = new BisAccBalVO();
        BeanUtils.copyNotNullProperties(bisAccBalDTO, vo);
        bisAccBalService.saveBisAccBal(vo, user);
    }
    @Override
    public void updateBisAccBal(BisAccBalDTO bisAccBalDTO, ICurrentUser user) {
        BisAccBal bisAccBal = new BisAccBal();
        BisAccBalVO dbInfo = bisAccBalService.getByUnion(bisAccBalDTO.getId());
        if (ObjectUtil.isEmpty(dbInfo)){
            throw new ParameterException(com.bytter.system.api.enums.AccBalMessageEnum.BIS_ACCBAL_MESSAGE_BALDATE_CHECK_BAL, bisAccBalDTO.getBankName(), DateUtils.formatDate(bisAccBalDTO.getBalDate()));
        }
        BeanUtils.copyNotNullProperties(dbInfo, bisAccBal);
        BeanUtils.copyNotNullProperties(bisAccBalDTO, bisAccBal);
        if (StringUtils.isNotBlank(bisAccBalDTO.getGetDateStr())){
            bisAccBal.setGetDate(DateUtil.formatDateYYYY_MM_DD(bisAccBalDTO.getGetDateStr()));
        }
        bisAccBal.setUpdateBy(user.getUsername());
        bisAccBal.setUpdateDate(new Date());
        bisAccBalService.updateBisAccBal(bisAccBal, user);
    }
    public List<BisAccBalDTO> getBankBal(String bankAcc, String balanceDate) {
        return bisAccBalService.getBankBal(bankAcc, balanceDate);
    }
    @Override
    public DataTablesOutput<BisAccDtlBillVO> listAbClaimConfirmedInfoAcc(DataTablesInput dataTablesInput, List<String> claimIdList) {
        String corpAuthSql = SqlUtil.getInSql(
                abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_CLAIM_QUERY,
                        dataTablesInput.getCurrentUserId()), "ACC.corp_id", true);
        String corpId=String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        List<String> corpAuthList=new ArrayList<>();
        if(StringUtil.isNotEmpty(corpId)){
            corpAuthList= Arrays.asList(corpId.split(","));
        }else {
            corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_CLAIM_QUERY,dataTablesInput.getCurrentUserId());
        }
        List<String> accStrList = btBankAccService.getBankAccsByCorpIdList(corpAuthList,"", new HashMap<>());
        String accStrSql=SqlUtil.getInSql(accStrList,"acc.bank_acc",true);
        List<BisExcDTO> allBisExcBankSerialNo = iBisExcService.getAllBisExcBankSerialNo();
        DataTablesOutput<BisAccDtlBillVO> bisAccDtlBillVODataTablesOutput = bisAccBalDao.queryBisAccDtl(dataTablesInput, corpAuthSql, claimIdList, accStrSql, allBisExcBankSerialNo);
        List<BisAccDtlBillVO> data = bisAccDtlBillVODataTablesOutput.getData();
        if(CollectionUtil.isEmpty(data)){
            return bisAccDtlBillVODataTablesOutput;
        }
        List<String> corpIds = data.stream().map(BisAccDtlBillVO::getCorpId).collect(Collectors.toList());
        Map<String, String> nameByIdsMap = abCorpService.getNameByIds(corpIds);
        for(BisAccDtlBillVO vo : data){
            vo.setCorpName(nameByIdsMap.get(vo.getCorpId()));
        }
        return bisAccDtlBillVODataTablesOutput;
    }
    /**
     * 通过sql语句查询账号，可以是关联查询语句
     * @param sql
     * @param params
     * @return
     */
    public List<BisAccBalDTO> listBisAccBalBySql(String sql, Map<String, Object> params ){
        return bisAccBalDao.listBisAccBalBySql(sql, params);
    }

    @Override
    public List<BisAccBalDTO> selectBalance(String balDate, String curId) {
        return bisAccBalDao.selectBalance(balDate,curId);
    }

    /**
     * 银行账户创建时初始化余额信息
     *
     * @param bankAcc 银行账户实体对象
     * @return boolean
     */
    @Override
    public boolean initBisAccBal(BtBankAccDTO bankAcc, ICurrentUser user) {
        com.bytter.system.api.dto.BtBankAccDTO dto = new com.bytter.system.api.dto.BtBankAccDTO();
        BeanUtils.copyProperties(bankAcc, dto);
        return this.bisAccBalService.initBisAccBal(dto, user);
    }
    @Override
    public boolean initBisAccBal(List<BtBankAccDTO> bankAcc, ICurrentUser user) {
        List<com.bytter.system.api.dto.BtBankAccDTO> listDto = BeanUtils.copyListProperties(bankAcc, com.bytter.system.api.dto.BtBankAccDTO.class);
        return this.bisAccBalService.initBisAccBal(listDto, user);
    }

    /**
     * 根据流水生成余额
     * @param bankAcc
     * @param beginDate
     * @param endDate
     * @param user
     */
    @Override
    public void saveBisAccBalByDtl(BtBankAccDTO bankAcc,Date beginDate,Date endDate, ICurrentUser user) {
        Map<String, Object> map = new HashMap<>();
        BisAccBalDTO bisAccBalVO = new BisAccBalDTO();
        map.put("accCurId",bankAcc.getCurId());
        map.put("bankAcc",bankAcc.getBankAcc());
        map.put("accCorpId",bankAcc.getCorpId());
        map.put("bifCode",bankAcc.getBifCode());
        bisAccBalVO.setBeginDate(beginDate);
        bisAccBalVO.setEndDate(endDate);
        bisAccBalVO.setGenerateBalance(1);
        bisAccBalService.saveAccBalByDtlTransactionForDtl(map,bisAccBalVO,user);
    }

    /**
     * 更新银行账户系统冻结余额
     *
     * @param accBalId    余额表id
     * @param sysFrzBal   系统冻结金额
     * @param freezeType  冻结状态：freeze-冻结，unfreeze-解冻
     */
    @Override
    public void updateSysFrzBal(String accBalId, BigDecimal sysFrzBal, String freezeType) {
        BisAccBal bisAccBal = bisAccBalService.get(accBalId, true);
        bisAccBal.setSysFrzBal(null == bisAccBal.getSysFrzBal() ? BigDecimal.ZERO : bisAccBal.getSysFrzBal());
        if (BtBankAccConstant.PLAN_FREEZE.equals(freezeType)) {
            // 冻结
            bisAccBal.setSysFrzBal(bisAccBal.getSysFrzBal().add(sysFrzBal));
            // 按本次冻结金额扣减，保留已有冻结对可用余额的影响。
            bisAccBal.setAvailBal(bisAccBal.getAvailBal().subtract(sysFrzBal));
        } else if (BtBankAccConstant.PLAN_UNFREEZE.equals(freezeType)) {
            // 解冻
            bisAccBal.setSysFrzBal(bisAccBal.getSysFrzBal().subtract(sysFrzBal));
            // 仅恢复本次解冻金额。
            bisAccBal.setAvailBal(bisAccBal.getAvailBal().add(sysFrzBal));
        } else {
            // 直联账户系统冻结金额同步
            bisAccBal.setSysFrzBal(sysFrzBal);
        }
        bisAccBalService.saveOrUpdate(bisAccBal);
    }

    @Override
    public BigDecimal getBankAccBalForReportDate(List<String> corpIds, Date balDate, String bankTypeId, String bankCode) {
        return bisAccBalDao.getBankAccBalForReportDate(corpIds, balDate, bankTypeId, bankCode);
    }

}
