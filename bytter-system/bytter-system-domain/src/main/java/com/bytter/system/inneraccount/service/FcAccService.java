package com.bytter.system.inneraccount.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.*;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.SysResourceConstant;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.FcAccListVO;
import com.bytter.system.api.vo.FcAccPlusVO;
import com.bytter.system.api.vo.FcAccVO;
import com.bytter.system.entity.User;
import com.bytter.system.enums.InnerAccountMessageEnum;
import com.bytter.system.inneraccount.dao.FcAccDao;
import com.bytter.system.inneraccount.entity.FcAcc;
import com.bytter.system.inneraccount.entity.FcAccLog;
import com.bytter.system.inneraccount.util.InnerAccountUtils;
import com.bytter.system.service.DictService;
import com.bytter.system.service.UserService;
import com.bytter.system.vo.DictVO;
import com.google.common.collect.Maps;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户信息表
 * @date 2017-05-25 19:36:12
 */
@Service("fcAccService")
@Transactional
public class FcAccService extends BaseService<FcAcc, String> implements ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @Autowired
    private FcAccDao fcAccDao;
    @Autowired
    private IConfigService configServiceI;
    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private FcAccLogService fcAccLogService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private IFcCorpPoolService fcCorpPoolServiceI;
    @Autowired
    private UserService userService;
    @Autowired
    private IAccountGrantService iAccountGrantService;
    @Autowired
    private IMultiLangService multiLangServiceI;
    @Autowired
    private IBtCurrencyService btCurrencyServiceI;
    @Autowired
    private DictService dictService;
    
    public Map<String, Map<String, DictDTO>> listDictMap(String... groupCode) {
        List<DictVO> listDictData = dictService.getDictListByCode(groupCode);
        Map<String, Map<String, DictDTO>> listData = Maps.newHashMap();
        Optional.ofNullable(listDictData).ifPresent(listDict -> {
            // 先分组
            Map<String, List<DictVO>> collect = listDict.stream().collect(Collectors.groupingBy(DictVO::getGroupCode));
            // 再分Map
            collect.forEach((key, list) -> {
                List<DictDTO> dictDTOList = BeanUtils.copyListProperties(list, DictDTO.class);
                Map<String, DictDTO> dictMap = dictDTOList.stream().collect(Collectors.toMap(DictDTO::getCode, dict -> dict));
                listData.put(key, dictMap);
            });
        });
        return listData;
    }

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(FcAcc entity, ICurrentUser user) {
        super.deleteEntity(entity);
        //完成通知提醒
        if (AllocationConstant.POOL_SIGN_1.equals(entity.getPoolSign())) {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_BACK, entity.getId(), user.getId());
        } else {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_BACK, entity.getId(), user.getId());
        }
        //日志记录
        saveLog(entity, CommonConstant.SUBMIT_DELETE, InnerAccountConstant.BIZ_TYPE_DELETE, user.getLoginName());
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(FcAcc entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcAcc entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(FcAcc entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<FcAcc, String> getEntityDao() {
        return fcAccDao;
    }
    /**
     * 判断是否存在多条
     *
     * @param blnctrAcc
     * @param opType
     * @param id
     * @return
     */
    public boolean isExistMulByBlnctrAcc(String blnctrAcc, String opType, String id) {
        boolean flag = false;
        List<FcAcc> list = fcAccDao.listByBlnctrAcc(blnctrAcc);
        if (!EmptyUtils.isEmptyList(list)) {
            if (CommonConstant.SUBMIT_ADD.equals(opType) && !list.isEmpty()) {
                flag = true;
            } else if (CommonConstant.SUBMIT_MODIFY.equals(opType)) {
                if (!EmptyUtils.isEmptyList(list)) {
                    for (FcAcc acc : list) {
                        if (!id.equals(acc.getId())) {
                            flag = true;
                            break;
                        }
                    }
                }
            }
        }
        return flag;
    }
    /**
     * 启用日期必须大于当前网点已日结日期
     *
     * @param date1
     * @param date2
     */
    protected void dateCompare(Date date1, Date date2) {
        if (DateUtils.dayDiffs(date1, date2) <= 0) {
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_OPENTIME_TALLYTIME_COMPARE,
                    DateUtils.formatDate(date1), DateUtils.formatDate(date2));
        }
    }
    /**
     * 启用日期必须大于当前资金池已日结日期
     *
     * @param date1
     * @param date2
     */
    protected void dateCompareForPool(Date date1, Date date2) {
        if (DateUtils.dayDiffs(date1, date2) <= 0) {
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_OPENTIME_TALLYTIME_POOL_COMPARE,
                    DateUtils.formatDate(date1), DateUtils.formatDate(date2));
        }
    }
    /**
     * 根据资金池标志生成内部账号生成法则
     *
     * @param poolSign
     * @return
     */
    public String getRuleVaql(String poolSign) {
        String reVal = "0000";
        int accAccNoRule = 2;
        //资金池
        if (AllocationConstant.POOL_SIGN_1.equals(poolSign)) {
            /**
             * 资金池内部账号生成法则
             * 顺序号 ==资金池+币别+科目+ 科目类别（acc_type）后从0开始的数据。
             * <option value="1" #if($config.fcs.accAccnoRule == 1)selected#end>资金池+币别+科目+3位顺序号</option>
             * <option value="2" #if($config.fcs.accAccnoRule == 2)selected#end>资金池+币别+科目+4位顺序号</option>
             * <option value="3" #if($config.fcs.accAccnoRule == 3)selected#end>资金池+币别+科目+6位顺序号</option>
             */
            accAccNoRule = configServiceI.getConfig("pool", "poolAccnoRule").toInteger(1).intValue();
        } else {
            /**
             * 内部账号生成法则
             * 顺序号 ==网点+币别+科目+ 科目类别（acc_type）后从0开始的数据。
             * <option value="1" #if($config.fcs.accAccnoRule == 1)selected#end>网点+币别+科目+3位顺序号</option>
             * <option value="2" #if($config.fcs.accAccnoRule == 2)selected#end>网点+币别+科目+4位顺序号</option>
             * <option value="3" #if($config.fcs.accAccnoRule == 3)selected#end>网点+币别+科目+6位顺序号</option>
             * <option value="4" #if($config.fcs.accAccnoRule == 4)selected#end>网点+币别+科目(科目不足6位补齐)+4位顺序号</option>
             */
            accAccNoRule = configServiceI.getConfig("fcs", "accAccnoRule").toInteger(1).intValue();
        }
        switch (accAccNoRule) {
            case 1:
                reVal = "000";
                break;
            case 2:
                reVal = "0000";
                break;
            case 3:
                reVal = "000000";
                break;
            case 4:
                reVal = "0000";
            default:
                reVal = "0000";
        }
        return reVal;
    }
    /**
     * 自动生成顺序号
     *
     * @param netId
     * @param curId
     * @param accType
     * @return
     */
    public String createSequence(String netId, String curId, String accType) {
        /**
         * 内部账号生成法则
         * 顺序号 ==网点+币别+科目+ 科目类别（acc_type）后从0开始的数据。
         * <option value="1" #if($config.fcs.accAccnoRule == 1)selected#end>网点+币别+科目+3位顺序号</option>
         * <option value="2" #if($config.fcs.accAccnoRule == 2)selected#end>网点+币别+科目+4位顺序号</option>
         * <option value="3" #if($config.fcs.accAccnoRule == 3)selected#end>网点+币别+科目+6位顺序号</option>
         * <option value="4" #if($config.fcs.accAccnoRule == 4)selected#end>网点+币别+科目(科目不足6位补齐)+4位顺序号</option>
         */
        String reVal = getRuleVaql(AllocationConstant.POOL_SIGN_0);
        int relen = reVal.length();
        /**
         * 过滤掉单位，有可能在没有单位作为账号的生成法则内，
         * 相同网点下的不同单位生成的账号一样，因此查出此网点下的账号
         * 进行比较
         */
        List<FcAcc> list = fcAccDao.queryMultiAccAll(netId, curId, accType);
        if (list != null && list.size() > 0) {
            Integer[] val = new Integer[list.size()];
            for (int i = 0; i < list.size(); i++) {
                String blnctrAcc = list.get(i).getBlnctrAcc();
                if (blnctrAcc.trim().length() < relen) {
                    val[i] = Integer.MIN_VALUE;
                    continue;
                }
                String temp = blnctrAcc.substring(blnctrAcc.trim().length() - relen);
                val[i] = Integer.parseInt(temp);
            }
            Integer maxVal = InnerAccountUtils.getMax(val);
            int charLen = String.valueOf(maxVal + 1).length();// 现有的max长度
            String prefix = "";
            /**
             * 根据长度去补齐长度
             */
            while (charLen < relen) {
                prefix = "0" + prefix;
                charLen++;
            }
            reVal = prefix + (maxVal + 1);
        }
        return reVal;
    }
    /**
     * 手动生成顺序号
     *
     * @param netId
     * @param curId
     * @param accType
     * @return
     */
    public String createManualSequence(String netId, String curId, String accType, String seqNum) {
        /**
         * 内部账号生成法则
         * 顺序号 ==网点+币别+科目+ 科目类别（acc_type）后从0开始的数据。
         * <option value="1" #if($config.fcs.accAccnoRule == 1)selected#end>网点+币别+科目+3位顺序号</option>
         * <option value="2" #if($config.fcs.accAccnoRule == 2)selected#end>网点+币别+科目+4位顺序号</option>
         * <option value="3" #if($config.fcs.accAccnoRule == 3)selected#end>网点+币别+科目+6位顺序号</option>
         * <option value="4" #if($config.fcs.accAccnoRule == 4)selected#end>网点+币别+科目(科目不足6位补齐)+4位顺序号</option>
         */
        String reVal = getRuleVaql(AllocationConstant.POOL_SIGN_0);
        int relen = reVal.length();
        String prefix = "";
        //手动生成
        if (StringUtils.isNotBlank(seqNum)) {
            int seqLength = reVal.length();
            if (StringUtils.isNotBlank(seqNum)) {
                seqLength = seqNum.length();//手动输入顺序号的长度
            }
            /**
             * 根据长度去补齐长度
             */
            while (seqLength < relen) {
                prefix = "0" + prefix;
                seqLength++;
            }
            reVal = prefix + seqNum;
        } else {
            /**
             * 过滤掉单位，有可能在没有单位作为账号的生成法则内，
             * 相同网点下的不同单位生成的账号一样，因此查出此网点下的账号
             * 进行比较
             */
            List<FcAcc> list = fcAccDao.queryMultiAccAll(netId, curId, accType);
            if (list != null && list.size() > 0) {
                Integer[] val = new Integer[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    String blnctrAcc = list.get(i).getBlnctrAcc();
                    String temp = blnctrAcc.substring(blnctrAcc.trim().length() - relen);
                    val[i] = Integer.parseInt(temp);
                }
                Integer maxVal = InnerAccountUtils.getMax(val);
                int charLen = String.valueOf(maxVal + 1).length();// 现有的max长度
                /**
                 * 根据长度去补齐长度
                 */
                while (charLen < relen) {
                    prefix = "0" + prefix;
                    charLen++;
                }
                reVal = prefix + (maxVal + 1);
            }
        }
        return reVal;
    }
    /**
     * 自动生成顺序号
     *
     * @param netId
     * @param curId
     * @param accType
     * @return
     */
    public String createSequenceForPool(String netId, String curId, String accType) {
        /**
         * 资金池内部账号生成法则
         * 顺序号 ==资金池+币别+科目+ 科目类别（acc_type）后从0开始的数据。
         * <option value="1" #if($config.fcs.accAccnoRule == 1)selected#end>资金池+币别+科目+3位顺序号</option>
         * <option value="2" #if($config.fcs.accAccnoRule == 2)selected#end>资金池+币别+科目+4位顺序号</option>
         * <option value="3" #if($config.fcs.accAccnoRule == 3)selected#end>资金池+币别+科目+6位顺序号</option>
         */
        String reVal = getRuleVaql(AllocationConstant.POOL_SIGN_1);
        int relen = reVal.length();
        /**
         * 过滤掉单位，有可能在没有单位作为账号的生成法则内，
         * 相同网点下的不同单位生成的账号一样，因此查出此网点下的账号
         * 进行比较
         */
        List<FcAcc> list = fcAccDao.queryMultiAccAll(netId, curId, accType);
        if (list != null && list.size() > 0) {
            Integer[] val = new Integer[list.size()];
            for (int i = 0; i < list.size(); i++) {
                String blnctrAcc = list.get(i).getBlnctrAcc();
                String temp = blnctrAcc.substring(blnctrAcc.trim().length() - relen);
                val[i] = Integer.parseInt(temp);
            }
            Integer maxVal = InnerAccountUtils.getMax(val);
            int charLen = String.valueOf(maxVal + 1).length();// 现有的max长度
            String prefix = "";
            /**
             * 根据长度去补齐长度
             */
            while (charLen < relen) {
                prefix = "0" + prefix;
                charLen++;
            }
            reVal = prefix + (maxVal + 1);
        }
        return reVal;
    }
    /**
     * 更改帐户利率
     *
     * @param blnctrAcc
     * @param colName
     * @param instrate
     */
    public void updateRate(String blnctrAcc, String colName, BigDecimal instrate) {
        fcAccDao.updateRate(blnctrAcc, colName, instrate);
    }
    /**
     * 根据账号查对象
     *
     * @param blnctrAcc
     * @return
     */
    public FcAcc findFcAccByBlnctrAcc(String blnctrAcc) {
        FcAcc fcAcc = fcAccDao.findFcAccByBlnctrAcc(blnctrAcc);
        return fcAcc;
    }
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccVO findFcAccById(String id) {
        FcAccVO fcAcc = fcAccDao.findFcAccById(id);
        return fcAcc;
    }
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccVO findFcAccByIdForPool(String id) {
        return fcAccDao.findFcAccByIdForPool(id);
    }
    public List<FcAccVO> findFcAcc(FcAcc fcAcc, String sql) {
        return fcAccDao.findFcAcc(fcAcc, sql);
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser userVO) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_QUERY, userVO.getId()), "t.corp_Id", params);
        String loginName = userVO.getLoginName();
        //是否开户复核
        String isCheck = (String) dataTablesInput.getFormdata().get("isCheck");
        params.put("loginName", loginName);
        if ("1".equals(isCheck)) {
            sql.append(" where t.acc_state =:accState and t.Operator_Code <> :loginName");
            params.put("accState", InnerAccountConstant.ACC_STATE_NEW);
        } else {
            sql.append(" where t.acc_state in(:accState) and t.Operator_Code =:loginName");
            params.put("accState", new String[]{InnerAccountConstant.ACC_STATE_TEMP, InnerAccountConstant.ACC_STATE_BACK});
        }
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = fcAccDao.getDataTablesUnion(dataTablesInput, sql.toString(), params);
        return output;
    }
    /**
     * 资金池查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, ICurrentUser userVO) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap();
        String loginName = userVO.getLoginName();
        //是否开户复核
        String isCheck = (String) dataTablesInput.getFormdata().get("isCheck");
        params.put("loginName", loginName);
        if ("1".equals(isCheck)) {
            sql.append(" where t.acc_state =:accState and t.Operator_Code <> :loginName");
            params.put("accState", InnerAccountConstant.ACC_STATE_NEW);
        } else {
            sql.append(" where t.acc_state in(:accState) and t.Operator_Code =:loginName");
            params.put("accState", new String[]{InnerAccountConstant.ACC_STATE_TEMP, InnerAccountConstant.ACC_STATE_BACK});
        }
        List<String> corpList = new ArrayList<>();
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_QUERY;
        List<FcCorpPoolDTO> pools = new ArrayList<>();
        //资金池不为空
        String poolId = (String) dataTablesInput.getFormdata().get("netId");
        if (!StringUtils.isBlank(poolId)) {
            pools.add(fcCorpPoolServiceI.getById(poolId));
        } else {
            pools = fcCorpPoolServiceI.getUserCorpPools(userVO, resource);
        }
        for (FcCorpPoolDTO pool : pools) {
            List<FcCorpPoolDTO> poolChilds = fcCorpPoolServiceI.getAllUserCorpPoolChilds(userVO, resource, pool.getId());
            if (!EmptyUtils.isEmptyList(poolChilds)) {
                for (FcCorpPoolDTO dto : poolChilds) {
                    corpList.add(dto.getCorpId());
                }
            }
        }
        String corpSql = SqlUtil.getInSql(corpList, "t.corp_id", true);
        sql.append(" and ").append(corpSql);
        DataTablesOutput output = fcAccDao.getDataTablesUnionForPool(dataTablesInput, sql.toString(), params);
        return output;
    }
    /**
     * 自动开户复核通过
     *
     * @param fcAcc
     * @param type
     */
    public void checkAuto(FcAcc fcAcc, String type, String loginName) {
        FcAcc acc = fcAccDao.get(fcAcc.getId(), true);
        acc.setCheckerCode(loginName);
        acc.setCheckTime(DateUtils.getDate());
        acc.setCheckBackReason(fcAcc.getCheckBackReason());
        check(acc, InnerAccountConstant.ACC_STATE_NORMAL, acc.getAccState());
        //插入联机余额
//        fcOnlineLedgerService.insertFclOnlineLedger(acc.getBlnctrAcc(), acc.getCurId(), loginName);
        //初始化账户余额
        Date balanceDate = InnerAccountUtils.getBalanceDate(DateUtils.getDateYMD(acc
                .getAccStartDate()));
        if (acc.getAccType().equals(InnerAccountConstant.FCACC_OFFBALANCE)) {// 表外
//            fcOutAccBalanceService.initAccountBalance(balanceDate, acc.getBlnctrAcc(), acc.getAccName(), acc.getCurId(), loginName);
        } else {
//            fcAccBalanceService.initAccountBalance(balanceDate, acc.getBlnctrAcc(), acc.getAccName(), acc.getCurId(), loginName);
        }
        //初始化账户积数余额
        initAccuBalance(balanceDate, acc.getBlnctrAcc(), acc.getCurId(), acc.getInterestSign(), loginName);
        //新增账户自动授权
        AccountGrantDTO grantDTO = new AccountGrantDTO();
        grantDTO.setAccountId(fcAcc.getId());
        //权限给申请用户
        User createBy = userService.getByLoginName(fcAcc.getCreateBy());
        grantDTO.setUserId(createBy.getId());
        grantDTO.setCorpId(fcAcc.getCorpId());
        if (AllocationConstant.POOL_SIGN_1.equals(fcAcc.getPoolSign())) {
            grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.POOLINNER_BANK_TYPE));
        } else {
            grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.INNER_BANK_TYPE));
        }
        iAccountGrantService.saveAccountGrant(grantDTO);
        //刷新缓存
        fcAccDao.flush();
        //日志记录
        saveLog(acc, type, InnerAccountConstant.BIZ_TYPE_CHECK_AUTO_SUCCESS, loginName);
    }
    /**
     * 复核通过或者失败
     *
     * @param fcAcc
     * @param accState    复核通过  ,复核打回
     * @param oldAccState 复核前的状态
     * @return
     */
    public int check(FcAcc fcAcc, String accState, String oldAccState) {
        return fcAccDao.check(fcAcc, accState, oldAccState);
    }
    /**
     * 初始化账户积数余额
     * 1、开户的时候判断 计息标志，计息积数初始化为 0。
     * 2、改户 是判断 不计息改成计息，取日结日期那天每日的余额（fc_acc_balance）是否存在，【不存在，插入积数表 基数为balance （ select balance from fc_acc_balance）】。
     * 3、查询fc_accu_balance 积数表是否存在 条件：balance_date = fc_acc（accopentime 一天之前的日期）.基数不存在，插入积数表 基数为0 ，其他值同fcAcc同步。
     *
     * @param balanceDate
     * @param account
     * @param curId
     * @param interestSign
     */
    private void initAccuBalance(Date balanceDate, String account, String curId, String interestSign, String loginName) {
        if (InnerAccountConstant.INTERESTSIGN_YES.equals(interestSign)) {
//            fcAccuBalanceService.initAccuBalance(balanceDate, account, curId, BigDecimal.ZERO, loginName);
        }
    }
    /**
     * 保存开户日志
     *
     * @param acc
     * @param opType 　操作类型
     * @param bizMsg 　业务信息
     */
    private void saveLog(FcAcc acc, String opType, String bizMsg, String loginName) {
        FcAccLog fcAccLog = new FcAccLog();
        fcAccLog.setBizId(acc.getId());
        fcAccLog.setOpType(opType);
        fcAccLog.setAccType(acc.getAccType());
        fcAccLog.setBizMsg(bizMsg);
        fcAccLog.setAccount(acc.getBlnctrAcc());
        fcAccLog.setBizType(InnerAccountConstant.BIZ_TYPE_OPEN);
        fcAccLogService.saveLog(fcAccLog, loginName);
    }
    /**
     * 判断账户是否被其它账户引用
     *
     * @param blnctrAcc
     * @return
     */
    public boolean isOtherAccountQuote(String blnctrAcc) {
        return fcAccDao.isOtherAccountQuote(blnctrAcc);
    }
    public boolean isNotReceivedInterest(String blnctrAcc) {
        return fcAccDao.isExistInterestPayable(blnctrAcc);
    }
    /**
     * 开户复核通过或者打回
     *
     * @param fcAcc
     * @param user
     */
    public void check(FcAcc fcAcc, ICurrentUser user) {
        FcAcc fcacc = fcAccDao.get(fcAcc.getId(), true);
        if (!InnerAccountConstant.ACC_STATE_NEW.equals(fcacc.getAccState())) {
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS, fcacc.getBlnctrAcc());
        }
        FcAcc acc = new FcAcc();
        BeanUtils.copyNotNullProperties(fcacc, acc);
        acc.setCheckBackReason(fcAcc.getCheckBackReason());
        //完成通知提醒
        if (AllocationConstant.POOL_SIGN_1.equals(acc.getPoolSign())) {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CHECK, acc.getId(), user.getId());
        } else {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CHECK, acc.getId(), user.getId());
        }
        if (InnerAccountConstant.ACC_STATE_NORMAL.equals(fcAcc.getAccState())) {//开户复核通过
            acc.setCheckerCode(user.getLoginName());
            acc.setCheckTime(DateUtils.getDate());
            check(acc, InnerAccountConstant.ACC_STATE_NORMAL, acc.getAccState());
            //插入联机余额
//            fcOnlineLedgerService.insertFclOnlineLedger(acc.getBlnctrAcc(), acc.getCurId(), user.getLoginName());
            //初始化账户余额
            Date balanceDate = InnerAccountUtils.getBalanceDate(DateUtils.getDateYMD(acc.getAccStartDate()));
            if (acc.getAccType().equals(InnerAccountConstant.FCACC_OFFBALANCE)) {// 表外
//                fcOutAccBalanceService.initAccountBalance(balanceDate, acc.getBlnctrAcc(), acc.getAccName(), acc.getCurId(), user.getLoginName());
            } else {
//                fcAccBalanceService.initAccountBalance(balanceDate, acc.getBlnctrAcc(), acc.getAccName(), acc.getCurId(), user.getLoginName());
            }
            //初始化账户积数余额
            initAccuBalance(balanceDate, acc.getBlnctrAcc(), acc.getCurId(), acc.getInterestSign(), user.getLoginName());
            //新增账户自动授权
            AccountGrantDTO grantDTO = new AccountGrantDTO();
            grantDTO.setAccountId(acc.getId());
            //权限给申请用户
            User createBy = userService.getByLoginName(acc.getCreateBy());
            grantDTO.setUserId(createBy.getId());
            grantDTO.setCorpId(acc.getCorpId());
            if (AllocationConstant.POOL_SIGN_1.equals(fcAcc.getPoolSign())) {
                grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.POOLINNER_BANK_TYPE));
            } else {
                grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.INNER_BANK_TYPE));
            }
            iAccountGrantService.saveAccountGrant(grantDTO);
            //日志记录
            saveLog(acc, CommonConstant.SUBMIT_CHECK, InnerAccountConstant.BIZ_TYPE_CHECK_SUCCESS, user.getLoginName());
        } else {//打回
            acc.setCheckerCode(null);
            acc.setCheckTime(null);
            check(acc, InnerAccountConstant.ACC_STATE_BACK, acc.getAccState());
            if (AllocationConstant.POOL_SIGN_1.equals(acc.getPoolSign())) {
                notificationService.sendToUserByLoginName(acc.getId(), acc.getCreateBy(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_BACK, acc);
            } else {
                notificationService.sendToUserByLoginName(acc.getId(), acc.getCreateBy(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_BACK, acc);
            }
            //日志记录
            saveLog(acc, CommonConstant.SUBMIT_CHECK, InnerAccountConstant.BIZ_TYPE_CHECK_BACK, user.getLoginName());
        }
        //刷新缓存
        fcAccDao.flush();
    }
    /**
     * 功能介绍 : 获取有效的内部账户。
     *
     * @param netId
     * @param curId
     * @return
     */
    public List<FcAccPlusVO> getValidAccount(String netId, String curId, String corpId) {
        return listAccountByParams(curId, netId, corpId, null);
    }
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param subjectId
     * @return
     */
    public List<FcAccPlusVO> listAccountByParams(String curId, String netId, String corpId, String subjectId) {
        return fcAccDao.getAccountByParams(curId, netId, corpId, subjectId, null, null, null);
    }
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @return
     */
    public List<FcAccListVO> listAccountByParams(String[] accTypes, String curId, String netId, String corpId) {
        return fcAccDao.getAccountByParams(accTypes, curId, netId, corpId);
    }
    /**
     * 根据参数筛选有转账权限的 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param subjectId
     * @return
     */
    public List<FcAccPlusVO> listAccountByParamsAndGrantForPool(String curId, String netId, String corpId, String subjectId, String sysSubjectCode, List<String> accs, String bankCode) {
        return fcAccDao.getAccountByParamsAndGrantForPool(curId, netId, corpId, subjectId, sysSubjectCode, accs, bankCode);
    }
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param sysSubjectCode
     * @return
     */
    public List<FcAccPlusVO> listAccountByMoreParams(String curId, String netId, String corpId, String subjectId, String sysSubjectCode) {
        return fcAccDao.getAccountByParams(curId, netId, corpId, subjectId, null, sysSubjectCode, null);
    }
    /**
     * 根据银行行号查询内部帐号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param subjectId
     * @param bankCode
     * @return
     */
    public List<FcAccPlusVO> listAccountByBankParams(String curId, String netId,
                                                     String corpId, String subjectId, String bankCode) {
        return fcAccDao.getAccountByParams(curId, netId, corpId, subjectId, null, null, bankCode);
    }
    /**
     * 功能介绍 : 获取有效的内部账户。
     * 以后扩展为有权限的内部账户<br>
     * <b>日期：</b>2017年6月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccPlusVO> getValidAccount() {
        return getValidAccount(null, null, null);
    }
    /**
     * 功能介绍 : 获取有效的内部账户(包含已注销的)。
     */
    public List<FcAccPlusVO> getValidAccountIsHavDel() {
        return fcAccDao.getAccountByParams(null, null, null, null, null, null, null, null, true);
    }
    /**
     * 更改账号状态
     *
     * @param blnctrAcc
     * @param state
     */
    public void changeState(String blnctrAcc, String state) {
        FcAcc fcAcc = this.findFcAccByBlnctrAcc(blnctrAcc);
        fcAcc.setAccState(state);
        fcAccDao.update(fcAcc);
    }
    /**
     * 内部账号查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTableAll(DataTablesInput dataTablesInput, ICurrentUser user) {
        Map<String, Object> params = new HashMap();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_ALLQUERY, user.getId()), "t.corp_Id", params);
        DataTablesOutput output = fcAccDao.getDataTableAll(dataTablesInput, corpAuthSql, params);
        return output;
    }
    /**
     * 内部账号查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTableAllForPool(DataTablesInput dataTablesInput, ICurrentUser user) {
        Map<String, Object> params = new HashMap();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_ALLQUERY, user.getId()), "t.corp_Id", params);
        DataTablesOutput output = fcAccDao.getDataTableAllForPool(dataTablesInput, corpAuthSql, params);
        return output;
    }
    /**
     * 内部账号查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTableAllNormal(DataTablesInput dataTablesInput, ICurrentUser user) {
        Map<String, Object> params = new HashMap();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_QUERY, user.getId()), "t.corp_Id", params);
        DataTablesOutput output = fcAccDao.getDataTableAllNormal(dataTablesInput, corpAuthSql, params);
        return output;
    }
    /**
     * 内部账号查询
     * Created by chenlj 2018/4/19 基础平台权限问题
     *
     * @param dataTablesInput
     * @return
     */
    //TODO SYS服务提供
    public DataTablesOutput<FcAccListVO> getDataTableAllNormalSYS(DataTablesInput dataTablesInput, ICurrentUser user) {
        Map<String, Object> params = new HashMap();
        StringBuilder corpAuthSql = new StringBuilder();
        corpAuthSql.append(SqlUtil.getInSqlByParams(corpServiceI.getUserCorpIds(SysResourceConstant.SYS_ACCOUNTGRANT_INNERACCOUNT, user.getId()),
                "t.corp_Id", params));
        String belongingCorpIdsInSql = getBelongingCorpIdsInSql(dataTablesInput);
        if (!EmptyUtils.isBlank(belongingCorpIdsInSql)) {
            corpAuthSql.append(" and ").append(belongingCorpIdsInSql);
        }
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("eqs_accountGrantSearch"))) {
            String accountGrantSearch = dataTablesInput.getFormdata().get("eqs_accountGrantSearch").toString();
            String accountGrantFunction = dataTablesInput.getFormdata().get("eqs_accountGrantFunction").toString();
            //查询未授权的账号
            if ("0".equals(accountGrantSearch)) {
                corpAuthSql.append(" and t.id not in (select distinct account_id from sys_account_grant sag ) ");
            } else if ("1".equals(accountGrantSearch)) {
                if (EmptyUtils.isNotEmpty(accountGrantFunction)) {
                    String[] accountGrantFunctionStr = accountGrantFunction.split(",");
                    corpAuthSql.append(" and t.id  in (select distinct account_id from sys_account_grant sag where 1=1  ");
                    for (int i = 0; i < accountGrantFunctionStr.length; i++) {
                        String entity = accountGrantFunctionStr[i];
                        if ("1".equals(entity)) {
                            corpAuthSql.append(" and sag.can_query_detail = '1' ");
                        }
                        if ("2".equals(entity)) {
                            corpAuthSql.append(" and sag.can_query_balance = '1' ");
                        }
                        if ("3".equals(entity)) {
                            corpAuthSql.append(" and sag.can_transfer_accounts = '1' ");
                        }
                    }
                    corpAuthSql.append(" )");
                }
            }
        }
        Object poolSignObj = dataTablesInput.getFormdata().get("eqs_poolSign");
        // 判断是否为资金池查询
        boolean poolSignFlag = false;
        if (!EmptyUtils.isEmpty(poolSignObj)) {
            String poolSign = String.valueOf(poolSignObj);
            if ("1".equalsIgnoreCase(poolSign)) {
                poolSignFlag = true;
            }
        }
        DataTablesOutput<FcAccListVO> dataTablesOutput;
        if (poolSignFlag) {
            String poolId = (String) dataTablesInput.getFormdata().get("eqs_corpId");
            String poolType = (String) dataTablesInput.getFormdata().get("poolType");
            if (!EmptyUtils.isBlank(poolId) && CorpConstant.CORP_TYPE_CODE_NET.equals(Integer.parseInt(poolType))) {
                dataTablesInput.getFormdata().put("eqs_netId", poolId);
                dataTablesInput.getFormdata().remove("eqs_corpId");
            }
            dataTablesInput.getFormdata().remove("poolType");
            dataTablesOutput = fcAccDao.getPoolDataTableAllNormal(dataTablesInput, corpAuthSql.toString(), params);
        } else {
            dataTablesOutput = fcAccDao.getDataTableAllNormal(dataTablesInput, corpAuthSql.toString(), params);
        }
        List<FcAccListVO> fcAccListVOList = dataTablesOutput.getData();
        Map<String, Map<String, DictDTO>> maps = listDictMap("InnerAccount.AccType");
        for(FcAccListVO fcAccListVO:fcAccListVOList){
            if (poolSignFlag) {
                FcCorpPoolDTO fcCorpPoolDTO = fcCorpPoolServiceI.getById(fcAccListVO.getNetPoolId());
                if(!EmptyUtils.isEmpty(fcCorpPoolDTO)){
                    fcAccListVO.setNetPoolName(fcCorpPoolDTO.getPoolName());
                }
            }else{
                CorpDTO corpDTO = corpServiceI.get(fcAccListVO.getNetId());
                if(!EmptyUtils.isEmpty(corpDTO)){
                    fcAccListVO.setNetName(corpDTO.getName());
                }
            }
            CorpDTO corpDTO = corpServiceI.get(fcAccListVO.getCorpId());
            if(!EmptyUtils.isEmpty(corpDTO)){
                fcAccListVO.setCorpName(corpDTO.getName());
            }
            BtCurrencyDTO btCurrencyDTO = btCurrencyServiceI.getObjectById(fcAccListVO.getCurId());
            if(!EmptyUtils.isEmpty(btCurrencyDTO)){
                fcAccListVO.setCurName(btCurrencyDTO.getCurName());
            }
            DictDTO dto = (DictDTO)maps.get("InnerAccount.AccType").get(fcAccListVO.getAccType());
            fcAccListVO.setAccTypeName(dto.getName());
        }
        return dataTablesOutput;
    }
    /**
     * 组装开户单位的条件SQL子句
     *
     * @param dataTablesInput 表单数据
     * @return 条件SQL子句
     */
    private String getBelongingCorpIdsInSql(DataTablesInput dataTablesInput) {
        String belongingCorpIdsInSql = null;
        String belongingCorpIds = (String) dataTablesInput.getFormdata().get("belongingCorpIds");
        if (!EmptyUtils.isBlank(belongingCorpIds)) {
            String[] corpIds = belongingCorpIds.split(",");
            belongingCorpIdsInSql = SqlUtil.getInSql(corpIds, "t.corp_id", true);
            dataTablesInput.getFormdata().remove("belongingCorpIds");
        }
        return belongingCorpIdsInSql;
    }
    /**
     * 注销转出账号,单位企业活期账号，不包含自己的活期账号
     *
     * @param blnctrAcc
     * @param netId
     * @return
     */
    public List<FcAccVO> transferOutAcc(String blnctrAcc, String netId) {
        return fcAccDao.transferOutAcc(blnctrAcc, netId);
    }
    /**
     * 获取FcAccVO
     *
     * @param account
     * @return
     */
    public FcAccVO getFcAccVOByBlnctrAcc(String account) {
        return fcAccDao.getFcAccVOByBlnctrAcc(account);
    }
    /**
     * 获取FcAccVO 不需要全新过滤
     *
     * @param netId
     * @param corpId
     * @param subjectId
     * @param curId
     * @return
     */
    public FcAccPlusVO getFcAccVONotAuthByParams(String netId, String corpId, String subjectId, String curId) {
        return fcAccDao.getFcAccVOByParams(netId, corpId, subjectId, curId, null);
    }
    /**
     * 根据账号ID列表查询账号列表
     *
     * @param ids
     * @return
     */
    public List<FcAccDTO> listFcAccInfo(List<String> ids) {
        return listFcAccInfo(ids, null);
    }
    public List<FcAccDTO> listFcAccInfo(List<String> ids, List<String> accState) {
        if (EmptyUtils.isEmptyList(ids)) {
            return null;
        }
        List<FcAccDTO> list = fcAccDao.listFcAccInfo(ids, accState);
        return list;
    }
    /**
     * 功能介绍  ： 查询账号币别的相关科目账号。<br>
     * <b>日期：</b>2017年7月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> getFcAccBySubjectCode(String chargeAccount, String netId, String subjectCode) {
        return fcAccDao.getFcAccBySubjectCode(chargeAccount, netId, subjectCode);
    }
    /**
     * 功能介绍  ： 查询相关科目账号。<br>
     * <b>日期：</b>2017年7月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> getFcAccByCorpSubjectCode(String netId, String corpId, String subjectCode) {
        return fcAccDao.getFcAccByCorpSubjectCode(netId, corpId, subjectCode);
    }
    /**
     * 功能介绍 : 判断账户是否同币别。<br>
     * <b>日期：</b>2017年7月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public boolean getFcAccCurCode(String blnctrAcc, String chargeAccount) {
        return fcAccDao.getFcAccCurCode(blnctrAcc, chargeAccount);
    }
    /**
     * 功能介绍  : 查询账户相关信息。<br>
     * <b>日期：</b>2017年7月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public FcAccDTO getAccDTOByAccount(String account) {
        FcAccVO fcAcc = fcAccDao.getFcAccVOByBlnctrAcc(account);
        if (EmptyUtils.isEmpty(fcAcc)) {
            return null;
        }
        FcAccDTO fcAccDTO = new FcAccDTO();
        BeanUtils.copyNotNullProperties(fcAcc, fcAccDTO);
        return fcAccDTO;
    }
    /**
     * 根据单位ID列表查询账号列表
     *
     * @param corpIds
     * @param accState
     * @return
     */
    public List<FcAccDTO> listFcAccInFoByCorpIds(List<String> corpIds, String accState) {
        if (corpIds == null) {
            return null;
        }
        return fcAccDao.listFcAccInFoByCorpIds(corpIds, accState);
    }
    /**
     * 查找放款账号对应的账号
     *
     * @param accForDeduct
     * @return
     */
    public List<FcAcc> getFcAccByAccForDeduct(String accForDeduct) {
        return this.getEntityDao().listBy("accForDeduct", accForDeduct);
    }
    /**
     * <b>功能介绍：</b>根据系统科目查询账户<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<FcAccDTO> findBySysSubjectId(String sysSubjectId, String netIds, Set<String> corpIds,
                                             String curId) {
        return fcAccDao.findBySysSubjectId(sysSubjectId, netIds, corpIds, curId);
    }
    /**
     * <b>功能介绍：</b>根据科目id,系统科目code查询账户(过滤掉关联了银行代码)<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<FcAccDTO> findBySysSubjectIdFilterBankCode(String subjectId, String sysSubjectCode, String netIds, Set<String> corpIds,
                                                           String curId) {
        return fcAccDao.findBySysSubjectIdFilterBankCode(subjectId, sysSubjectCode, netIds, corpIds, curId);
    }
    /**
     * 功能介绍 : 开户未复核通过的账户。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> findNoCheckVFcAcc(FcDayCloseDTO dayClose) {
        return fcAccDao.findNoCheckVFcAcc(dayClose);
    }
    /**
     * 功能介绍 : 查询账户集合。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> getAccSet(List<String> accList) {
        return fcAccDao.getAccSet(accList);
    }
    /**
     * 功能介绍 : 查询表外账户集合。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> getOutAccTypeAccs() {
        return fcAccDao.getOutAccTypeAccs();
    }
    /**
     * 功能介绍 : 获取可记账内部账号(非表外)。<br>
     * <b>日期：</b>2017年11月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccPlusDTO> getTallyAccTypeAccs(List<String> ids) {
        List<FcAccVO> accVOs = fcAccDao.getTallyAccTypeAccs(ids);
        return BeanUtils.copyListProperties(accVOs, FcAccPlusDTO.class);
    }
    /**
     * 根据科目 判断是否存在使用的账号
     *
     * @param subujectId
     * @return
     */
    public boolean isExsits(String subujectId) {
        return fcAccDao.isExsits(subujectId);
    }
    /**
     * 根据内部账号搜索是否有关联的银行账号存在
     *
     * @param blnctrAcc
     * @return
     */
    public boolean isExsitsForValidBankAccByInnerAcc(String blnctrAcc) {
        return fcAccDao.isExsitsForValidBankAccByInnerAcc(blnctrAcc);
    }
    /**
     * 根据内部账号搜索是否有关联的计息账号存在
     *
     * @param blnctrAcc
     * @return
     */
    public boolean isExsitsByExpPay(String blnctrAcc) {
        FcAccVO vo = new FcAccVO();
        vo.setBlnctrAcc(blnctrAcc);
        List<FcAccVO> accListByExpPay = fcAccDao.getAccListByExpPay(vo);
        FcAcc acc = fcAccDao.findFcAccByBlnctrAcc(blnctrAcc);
        boolean flag = !EmptyUtils.isEmptyList(accListByExpPay) ||
                (!EmptyUtils.isEmpty(blnctrAcc) && (!EmptyUtils.isEmpty(acc.getInterestInExp()) || !EmptyUtils.isEmpty(acc.getInterestRecPay())));
        return flag;
    }
    /**
     * 通过父银行账号 找子账号对应的内部账号【N,P,S】
     *
     * @param parentBankAccId
     * @param curId
     * @return
     */
    public List<FcAccPlusVO> getSubAccFcAccByParentBankAcc(String parentBankAccId, String curId, List<String> ids) {
        return fcAccDao.getSubAccFcAccByParentBankAcc(parentBankAccId, curId, ids);
    }
    /**
     * 判断两个账号是否所属同一网点下
     *
     * @param accInfo1 账号
     * @param accInfo2 账号
     * @return
     */
    public boolean isTheSameNetByFcAcc(String accInfo1, String accInfo2) {
        if (EmptyUtils.isBlank(accInfo1) || EmptyUtils.isBlank(accInfo2)) {
            return false;
        }
        FcAccVO fcAcc1 = fcAccDao.getFcAccVOByBlnctrAcc(accInfo1);
        FcAccVO fcAcc2 = fcAccDao.getFcAccVOByBlnctrAcc(accInfo2);
        if (EmptyUtils.isEmpty(fcAcc1) || EmptyUtils.isEmpty(fcAcc2)) {
            return false;
        }
        return fcAcc1.getNetId().equalsIgnoreCase(fcAcc1.getNetId());
    }
    public DataTablesOutput getFcAcc(DataTablesInput input) {
        return fcAccDao.getDataTableAllNormal(input, null, null);
    }
    /**
     * <b>功能介绍：</b>获取网点下计息的资金池账号<br>
     * <b>日期：</b>2019年5月21日<br>
     * <b>作者：</b>liyh<br>
     *
     * @version 1.0
     */
    public List<FcAccDTO> getNetAccByPoolSign(String netId, String accType, String interestSign, String poolSign) {
        return fcAccDao.getNetAccByPoolSign(netId, accType, interestSign, poolSign);
    }
    /**
     * <b>功能介绍：</b>查询资金池科目的资金池账号<br>
     * <b>日期：</b>2019年5月22日<br>
     * <b>作者：</b>liyh<br>
     *
     * @version 1.0
     */
    public List<FcAccDTO> getNetPoolAccBySubject(String netId, String curId, String subjectId, String poolSign) {
        return fcAccDao.getNetPoolAccBySubject(netId, curId, subjectId, poolSign);
    }
    /**
     * 根据单位币别筛选有效的企业活期账号
     *
     * @param corpId
     * @param curId
     * @return
     */
    public List<FcAccVO> listFcAccByCorpId(String corpId, String curId) {
        return fcAccDao.listFcAccByCorpId(corpId, curId);
    }
    /**
     * 根据银行号获取银行类别信息(因构建报循环调用问题，先放到此处，后面再行优化)
     *
     * @param bankCode [银行号]
     * @return 银行类别信息
     * @author wangzx
     * @date 2020-05-07
     */
    public BankTypeDTO getInfoByBankCode(String bankCode) {
        return this.fcAccDao.getInfoByBankCode(bankCode);
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位已经开设内部账户，不允许删除！
            int num = this.fcAccDao.getFcAccCountByCorpId(corpId, "del");
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_INNERACC,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DEL_INNERACC_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位存在有效的内部账户，不允许禁用！
            int num = this.fcAccDao.getFcAccCountByCorpId(corpId, "disable");
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_INNERACC,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_INNERACC_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    /**
     * 根据网点单位科目代码币种银行类型
     *
     * @param netId
     * @param corpId
     * @param sysSubjectCode
     * @param curId
     * @param bankTypeId
     * @return
     */
    public List<FcAccVO> getAccListByInfo(String netId, String corpId, String sysSubjectCode, String curId, String bankTypeId) {
        return this.fcAccDao.getAccListByInfo(netId, corpId, sysSubjectCode, curId, bankTypeId);
    }
    /**
     * 查看计息账号数据
     *
     * @param dataTablesInput 查询
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listFcAccInfoDataTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        String sysSubjectCode = (String) dataTablesInput.getFormdata().get("sysSubjectCode");
        if (StringUtils.isNotBlank(sysSubjectCode)) {
            //系统科目：应付利息-企业活期存款（2011）,利息支出-企业活期存款（5212） 对应获取账户种类是企业活期存款
            if (SubjectConstant.SysSubject.INTEREST_PAYABLE_CURRENT.get().equals(sysSubjectCode) || SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT.get().equals(sysSubjectCode)) {
                dataTablesInput.getFormdata().put("accType", InnerAccountConstant.FCACC_BUSINESS_ACCOUNT);
            } else if (SubjectConstant.SysSubject.DEPOSIT_INTEREST_INCOME_REGULAR.get().equals(sysSubjectCode) || SubjectConstant.SysSubject.DEPOSIT_INTEREST_EXPENDITURE_REGULAR.get().equals(sysSubjectCode)) {
                //系统科目：应付利息-企业定期存款（2012）,利息支出—企业定期存款（5211） 对应获取账户种类是企业定期
                dataTablesInput.getFormdata().put("accType", InnerAccountConstant.FCACC_BUSINESS_BEGULAR);
            } else if (SubjectConstant.SysSubject.INTEREST_PAYABLE_NOTICE_DEPOSIT.get().equals(sysSubjectCode) || SubjectConstant.SysSubject.INTEREST_EXPENSES_NOTICE_DEPOSIT.get().equals(sysSubjectCode)) {
                //系统科目：应付利息-通知存款（2013）,利息支出-通知存款（5213） 对应获取账户种类是通知存款
                dataTablesInput.getFormdata().put("accType", InnerAccountConstant.FCACC_BANK_DEPOSIT);
            } else if (SubjectConstant.SysSubject.INTEREST_PAYABLE_COMP_AGREED.get().equals(sysSubjectCode) || SubjectConstant.SysSubject.DISCOUNT_INTEREST_COMP_AGREED.get().equals(sysSubjectCode)) {
                //系统科目：应付利息-企业协定（2016）,利息支出-企业协定（5216） 对应获取账户种类是协定存款
                dataTablesInput.getFormdata().put("accType", InnerAccountConstant.FCACC_AGREEMENT);
            } else if (SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_CURRENT.get().equals(sysSubjectCode) || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_CURRENT.get().equals(sysSubjectCode)) {
                //系统科目：应收利息-银行活期存款（1032）,利息收入-银行活期存款（5012） 对应获取账户种类是银行活期存款
                dataTablesInput.getFormdata().put("accType", InnerAccountConstant.FCACC_BANK_DEMAND);
            } else if (SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_LOAN.get().equals(sysSubjectCode) || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_LOAN.get().equals(sysSubjectCode)) {
                //系统科目：应付利息-银行借款（2014）,利息支出-银行借款（5214） 对应获取账户种类是银行借款
                dataTablesInput.getFormdata().put("accType", InnerAccountConstant.FCACC_BANK_BORROW);
            } else if (SubjectConstant.SysSubject.INTEREST_PAYABLE_LOAN.get().equals(sysSubjectCode) || SubjectConstant.SysSubject.INTEREST_EXPENSES_LOAN.get().equals(sysSubjectCode)) {
                //系统科目：应收利息-企业放款（1031）,利息收入-企业放款（5011） 对应获取账户种类是放款（内部贷款）
                dataTablesInput.getFormdata().put("accType", InnerAccountConstant.FCACC_LOAN);
            }
            //应收应付科目查询应收应付利息户INTEREST_REC_PAY为空的数据,利息收入科目查询利息收入支出户INTEREST_IN_EXP为空的数据．
            if (SubjectConstant.SysSubject.INTEREST_PAYABLE_CURRENT.get().equals(sysSubjectCode) || SubjectConstant.SysSubject.DEPOSIT_INTEREST_INCOME_REGULAR.get().equals(sysSubjectCode)
                    || SubjectConstant.SysSubject.INTEREST_PAYABLE_NOTICE_DEPOSIT.get().equals(sysSubjectCode) || SubjectConstant.SysSubject.INTEREST_PAYABLE_COMP_AGREED.get().equals(sysSubjectCode)
                    || SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_CURRENT.get().equals(sysSubjectCode) || SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_LOAN.get().equals(sysSubjectCode)
                    || SubjectConstant.SysSubject.INTEREST_PAYABLE_LOAN.get().equals(sysSubjectCode)) {
                dataTablesInput.getFormdata().put("interestType", "pay");
            } else if (SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT.get().equals(sysSubjectCode) || SubjectConstant.SysSubject.DEPOSIT_INTEREST_EXPENDITURE_REGULAR.get().equals(sysSubjectCode)
                    || SubjectConstant.SysSubject.INTEREST_EXPENSES_NOTICE_DEPOSIT.get().equals(sysSubjectCode) || SubjectConstant.SysSubject.DISCOUNT_INTEREST_COMP_AGREED.get().equals(sysSubjectCode)
                    || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_CURRENT.get().equals(sysSubjectCode) || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_LOAN.get().equals(sysSubjectCode)
                    || SubjectConstant.SysSubject.INTEREST_EXPENSES_LOAN.get().equals(sysSubjectCode)) {
                dataTablesInput.getFormdata().put("interestType", "exp");
            }
        }
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_QUERY, user.getId()), "t.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        String corpId = (String) dataTablesInput.getFormdata().get("corpId");
        Map<String, Object> params = new HashMap<>();
        int entrySign = configServiceI.getConfig("ist", "entrySign").toInteger().intValue();
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        if (entrySign == 0) {//结算中心
            formdata.put("corpId", "");
        } else {//成员单位或银行
            formdata.put("corpId", corpId);
        }
        return this.fcAccDao.listFcAccInfoDataTable(dataTablesInput, sql.toString());
    }
    /**
     * 根据账号信息查找计息账号列表
     *
     * @param vo
     * @return
     */
    public List<FcAccVO> getAccListByExpPay(FcAccVO vo) {
        return this.fcAccDao.getAccListByExpPay(vo);
    }
    /**
     * 根据账号删除关联计息账号
     *
     * @param entity
     */
    public void delInterestAcc(FcAcc entity) {
        if (!EmptyUtils.isEmpty(entity) && !EmptyUtils.isEmpty(entity.getBlnctrAcc())) {
            FcAccVO vo = new FcAccVO();
            vo.setBlnctrAcc(entity.getBlnctrAcc());
            List<FcAccVO> accListByExpPay = this.getAccListByExpPay(vo);
            if (!EmptyUtils.isEmptyList(accListByExpPay)) {
                for (FcAccVO accVO : accListByExpPay) {
                    FcAcc fcAcc = this.get(accVO.getId());
                    fcAcc.setInterestInExp("");
                    fcAcc.setInterestRecPay("");
                    this.saveOrUpdate(fcAcc);
                }
            }
        }
    }
    /**
     * 保存计息账号信息
     *
     * @param fcAcc
     */
    public void saveInterestAcc(FcAcc fcAcc) {
        //如果计息账号不为空,遍历账号根据科目设置到对应的字段中,
        //应收应付利息户:INTEREST_REC_PAY,利息收入支出户:INTEREST_IN_EXP
        if (!StringUtils.isBlank(fcAcc.getAccType()) && InnerAccountConstant.FCACC_INNER_ACCOUNT.equals(fcAcc.getAccType())) {
//            FcSubjectDTO subjectDto = fcSubjectService.findSubjectById(fcAcc.getSubjectId());
//            if(!EmptyUtils.isEmpty(subjectDto) && !EmptyUtils.isEmpty(subjectDto.getSysSubjectCode())){
//                if(!StringUtils.isBlank(fcAcc.getInterestAcc())){
//                    String[] accArr = fcAcc.getInterestAcc().split(CommonConstant.SEMI_SPLIT);
//                    for(int i = 0; i < accArr.length; i++){
//                        FcAccVO accVo = this.getFcAccVOByBlnctrAcc(accArr[i]);
//                        if(!EmptyUtils.isEmpty(accVo)){
//                            FcAcc accEntity = this.get(accVo.getId());
//                            //系统科目：应付利息-企业活期存款（2011）,应付利息-企业定期存款（2012）,应付利息-通知存款（2013）,应付利息-企业协定（2016）,
//                            // 应收利息-银行活期存款（1032）,应付利息-银行借款（2014）,应收利息-企业放款（1031）,这几个科目放入INTEREST_REC_PAY字段中
//                            if(SubjectConstant.SysSubject.INTEREST_PAYABLE_CURRENT.get().equals(subjectDto.getSysSubjectCode())
//                                    || SubjectConstant.SysSubject.DEPOSIT_INTEREST_INCOME_REGULAR.get().equals(subjectDto.getSysSubjectCode())
//                                    || SubjectConstant.SysSubject.INTEREST_PAYABLE_NOTICE_DEPOSIT.get().equals(subjectDto.getSysSubjectCode())
//                                    || SubjectConstant.SysSubject.INTEREST_PAYABLE_COMP_AGREED.get().equals(subjectDto.getSysSubjectCode())
//                                    || SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_CURRENT.get().equals(subjectDto.getSysSubjectCode())
//                                    || SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_LOAN.get().equals(subjectDto.getSysSubjectCode())
//                                    || SubjectConstant.SysSubject.INTEREST_PAYABLE_LOAN.get().equals(subjectDto.getSysSubjectCode())){
//                                accEntity.setInterestRecPay(fcAcc.getBlnctrAcc());
//                            }
//                            //系统科目：利息支出-企业活期存款（5212）,利息支出—企业定期存款（5211）,利息支出-通知存款（5213）,利息支出-企业协定（5216）,
//                            //利息收入-银行活期存款（5012）,利息支出-银行借款（5214）,利息收入-企业放款（5011）
//                            if(SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT.get().equals(subjectDto.getSysSubjectCode())
//                                    || SubjectConstant.SysSubject.DEPOSIT_INTEREST_EXPENDITURE_REGULAR.get().equals(subjectDto.getSysSubjectCode())
//                                    || SubjectConstant.SysSubject.INTEREST_EXPENSES_NOTICE_DEPOSIT.get().equals(subjectDto.getSysSubjectCode())
//                                    || SubjectConstant.SysSubject.DISCOUNT_INTEREST_COMP_AGREED.get().equals(subjectDto.getSysSubjectCode())
//                                    || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_CURRENT.get().equals(subjectDto.getSysSubjectCode())
//                                    || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_LOAN.get().equals(subjectDto.getSysSubjectCode())
//                                    || SubjectConstant.SysSubject.INTEREST_EXPENSES_LOAN.get().equals(subjectDto.getSysSubjectCode())){
//                                accEntity.setInterestInExp(fcAcc.getBlnctrAcc());
//                            }
//                            this.saveOrUpdate(accEntity);
//                        }
//                    }
//                }
//            }
        }
    }
    /**
     * 根据单位集合 得到内部账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    public List<FcAccDTO> getFcAccByCorpIdList(List<String> corpIdList, List<String> accState, String joinSql) {
        if (EmptyUtils.isEmptyList(corpIdList)) {
            return null;
        }
        List<FcAccDTO> list = fcAccDao.getFcAccByCorpIdList(corpIdList, accState, joinSql);
        return list;
    }
    /**
     * 根据单位集合 得到内部账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    public List<String> getFcAccsByCorpIdList(List<String> corpIdList, List<String> accState, String joinSql) {
        if (EmptyUtils.isEmptyList(corpIdList)) {
            return null;
        }
        return fcAccDao.getFcAccsByCorpIdList(corpIdList, accState, joinSql);
    }
    public List<FcAccCorpCurrencyDTO> getAccCorpCurDataByAccounts(List<String> accounts) {
        return fcAccDao.getAccCorpCurDataByAccounts(accounts);
    }
    public List<BalanceQueryDTO> queryAccBalanceAndBank(BalanceQueryDTO dto, String userCode, List<String> corpAuth, String... aTrue) {
        return fcAccDao.queryAccBalanceAndBank(dto, userCode, corpAuth, aTrue);
    }
    /**
     * 根据 netId , 查询 fc_acc 表，返回 blnctr_acc 结算中心账号
     * 除了 netId，没有其它条件
     *
     * @param netId
     * @return
     */
    public List<String> getBlnctrAccByNetId2(String netId) {
        return fcAccDao.getBlnctrAccByNetId2(netId);
    }
    public List<FcAccSubjectDTO> getAccAndSubjectData(List<String> corpAuth, boolean isPool) {
        return fcAccDao.getAccAndSubjectData(corpAuth, isPool);
    }
    public List<FcAccSubjectDTO> getAccNameAndSubjectName(String netId, String accType, List<String> corpAuth) {
        return fcAccDao.getAccNameAndSubjectName(netId, accType, corpAuth);
    }
    public List<FcAccSubjectDTO> getAccAndSubjectAndCurData(Map<String, Object> params, List<String> blnctrAccLists) {
        return fcAccDao.getAccAndSubjectAndCurData(params, blnctrAccLists);
    }
    public List<FcAccSubjectDTO> getAccNameAndMeasureUnitByCorpId(List<String> corpIds) {
        return fcAccDao.getAccNameAndMeasureUnitByCorpId(corpIds);
    }
    public List<FcAccSubjectDTO> getAccSubjectDateByCorpIds(List<String> corpIds) {
        return fcAccDao.getAccSubjectDateByCorpIds(corpIds);
    }
    public FcAccSubjectDTO getAccSubjectCurDateByAccount(String account) {
        return fcAccDao.getAccSubjectCurDateByAccount(account);
    }
    public List<FcAccSubjectDTO> getAccSubjectCurDateByAccounts(List<String> accounts) {
        return fcAccDao.getAccSubjectCurDateByAccounts(accounts);
    }
    public List<FcAccCorpCurrencyDTO> getAccCorpPoolCurDataByAccounts(List<String> accounts) {
        return fcAccDao.getAccCorpPoolCurDataByAccounts(accounts);
    }
    public List<FcAccSubjectDTO> getAccCurDateByCorpIdsAndPool(List<String> corpIds, String isPool) {
        return fcAccDao.getAccCurDateByCorpIdsAndPool(corpIds, isPool);
    }
    public List<FcAccSubjectDTO> getAccSysCorpDateByCorpIdsAndPool(List<String> corpIds, String isPool) {
        return fcAccDao.getAccSysCorpDateByCorpIdsAndPool(corpIds, isPool);
    }
    public List<FcAccSubjectDTO> getAccCurDateByAccountAndPool(String account, String isPool) {
        return fcAccDao.getAccCurDateByAccountAndPool(account, isPool);
    }
    public List<FcAccSubjectDTO> getAccCurDataByCorpIds(List<String> corpIds) {
        return fcAccDao.getAccCurDataByCorpIds(corpIds);
    }
    public List<FcAccSubjectDTO> getAccCurDataByNetIds(List<String> netIds) {
        return fcAccDao.getAccCurDataByNetIds(netIds);
    }
    public List<FcAccDTO> getNoticeAccount(String corpId, String curId) {
        return fcAccDao.getNoticeAccount(corpId, curId);
    }
    public Map<String, String> getAccNameByNetIdAndBlnctrAccs(String netId, List<String> blnctrAccs) {
        List<FcAccDTO> lists = fcAccDao.getAccNameByNetIdAndBlnctrAccs(netId, blnctrAccs);
        return lists.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, FcAccDTO::getAccName));
    }
    public List<String> getBlnctrAccByNetIdAndAccType(String netId, String accType) {
        return fcAccDao.getBlnctrAccByNetIdAndAccType(netId, accType);
    }
    public List<FcAccDTO> getFcAccByNetIdAndCheckerCodeIsNotNull(String netId) {
        return fcAccDao.getFcAccByNetIdAndCheckerCodeIsNotNull(netId);
    }
    public List<FcAccDTO> getFcAccByNetIdAndAccTypeCheckerCodeIsNotNull(String netId, String[] accType) {
        return fcAccDao.getFcAccByNetIdAndAccTypeCheckerCodeIsNotNull(netId, accType);
    }

    public List<FcAccDTO> getFcAccByAccountAndNetIdAndAccTypeCheckerCodeIsNotNull(String account, String netId, String accType){
        return fcAccDao.getFcAccByAccountAndNetIdAndAccTypeCheckerCodeIsNotNull(account, netId, accType);
    }

    public List<FcAccDTO> getFcAccByIdAndNotAccType(String netId, String[] accType) {
        return fcAccDao.getFcAccByIdAndNotAccType(netId, accType);
    }

    public List<FcAccDTO> getFcAccByNetId(String netId) {
        return fcAccDao.getFcAccByNetId(netId);
    }
    public List<String> getFcAccCurByNetId(String netId) {
        return fcAccDao.getFcAccCurByNetId(netId);
    }
    public List<Map<String,String>> getFcAccCurByNetIds(String netId) {
        return fcAccDao.getFcAccCurByNetIds(netId);
    }

    public List<String> getBlnctrAccByNetId(String netId) {
        return fcAccDao.getBlnctrAccByNetId(netId);
    }
    public List<FcAccDTO> getFcAccByNetIdAndOther(String netId) {
        return fcAccDao.getFcAccByNetIdAndOther(netId);
    }
    public Map<String, String> getBlnctrAccAndAccNameByNetId(String netId) {
        List<FcAccDTO> lists = fcAccDao.getBlnctrAccAndAccNameByNetId(netId);
        return lists.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, FcAccDTO::getAccName));
    }
    public Map<String, Integer> getBlnctrAccAndBalanceWayByNetIdAndNotAccType(String netId, String accType) {
        List<FcAccDTO> lists = fcAccDao.getBlnctrAccAndBalanceWayByNetIdAndNotAccType(netId, accType);
        return lists.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, FcAccDTO::getBalanceWay));
    }
    public List<String> getBlnctrAccByNetIdAndIfCompare(String netId, String compare) {
        return fcAccDao.getBlnctrAccByNetIdAndIfCompare(netId, compare);
    }
    /**
     * 根据 accountList and acc_state <> 'D' and interest_sign = '1' , 查询 fc_acc
     * return key=账号  BLNCTR_ACC
     * value=起利日期  INTEREST_START_DATE
     *
     * @param accountList
     * @return
     */
    public Map<String, Date> getInterestStartDate(List accountList) {
        List<FcAccDTO> lists = fcAccDao.getInterestStartDate(accountList);
        return lists.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, FcAccDTO::getInterestStartDate));
    }
    public Map<String, Date> getInterestStartDate(String netId, String[] accTypeCode, String... sysSubjectFilters) {
        List<FcAccDTO> lists = fcAccDao.getInterestStartDate(netId, accTypeCode, sysSubjectFilters);
        return lists.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, FcAccDTO::getInterestStartDate));
    }
    public FcAccDTO getFcAccByCorpIdAndBlnctrAcc(String corpId, String blnctrAcc) {
        return fcAccDao.getFcAccByCorpIdAndBlnctrAcc(corpId, blnctrAcc);
    }
    public List<FcAccDTO> getFcAccByIds(List<String> ids) {
        return fcAccDao.getFcAccByIds(ids);
    }
    public List<FcAccDTO> getFcAccByBlnctrAccs(List<String> blnctrAccs) {
        return fcAccDao.getFcAccByBlnctrAccs(blnctrAccs);
    }
    //getFcAccByBlnctrAccsAndAcctype
    public List<FcAccDTO> getFcAccByBlnctrAccsAndAcctype(List<String> blnctrAccs, Map<String, String> queryParams) {
        return fcAccDao.getFcAccByBlnctrAccsAndAcctype(blnctrAccs, queryParams);
    }
    public List<Map<String, Object>> getLoanAccByCorpCode(String corpId) {
        return fcAccDao.getLoanAccByCorpCode(corpId);
    }
    public List<Map<String, Object>> getCorpCurAccInfo(String loanAcc) {
        return fcAccDao.getCorpCurAccInfo(loanAcc);
    }
    public List<FcAccDTO> findNeedInterestAcc(String corpId, String accType, String subjectId) {
        return fcAccDao.findNeedInterestAcc(corpId, accType, subjectId);
    }
    public List<String> getBlnctrAccByNetCodeAndAccType(String netCode, String accType) {
        return fcAccDao.getBlnctrAccByNetCodeAndAccType(netCode, accType);
    }
    public List<FcAccPlusDTO> getTallyAccTypeAccList(String netId, String corpId, String curId, List<String> accList) {
        return fcAccDao.getTallyAccTypeAccList(netId, corpId, curId, accList);
    }
    public List<FcAccDTO> queryBySomeCondPgd(String condition) {
        return fcAccDao.queryBySomeCondPgd(condition);
    }
}
