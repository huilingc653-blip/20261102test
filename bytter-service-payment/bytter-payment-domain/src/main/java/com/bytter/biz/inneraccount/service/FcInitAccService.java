package com.bytter.biz.inneraccount.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.util.ObjectUtils;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcAccBalanceService;
import com.bytter.biz.finacial.api.service.IFcAccuBalanceService;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.finacial.api.vo.FcAccBalanceVO;
import com.bytter.biz.finacial.api.vo.FcOnlineLedgerVO;
import com.bytter.biz.inneraccount.dao.FcInitAccDao;
import com.bytter.biz.inneraccount.entity.FcInitAcc;
import com.bytter.biz.inneraccount.util.InnerAccountUtils;
import com.bytter.biz.inneraccount.vo.FcInitAccListVO;
import com.bytter.biz.inneraccount.vo.FcInitAccVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.dto.FcAccInitCheckBeanDTO;
import com.bytter.biz.payment.api.dto.FcDayCloseDTO;
import com.bytter.biz.payment.api.dto.FcInitAccDTO;
import com.bytter.biz.payment.api.dto.FcInitSubjectDTO;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccLogService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcAccLogDTO;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bytter.core.utils.DateUtils.getDate;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账号初始化
 * @date 2017-06-12 18:12:02
 */
@Service("fcInitAccService")
@Transactional
public class FcInitAccService extends BaseService<FcInitAcc, String> implements IUserExcelVerifyHandler<FcInitAccVO> {
    @Autowired
    private FcInitAccDao fcInitAccDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcAccuBalanceService fcAccuBalanceService;
    @DubboReference(check = false)
    private IFcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
    private IFcAccBalanceService fcAccBalanceService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IFcAccLogService fcAccLogService;
    @DubboReference(check = false)
    private INotificationService notificationService;

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(FcInitAcc entity, ICurrentUser user) {
        fcInitAccDao.updateStatus(entity.getId(), CommonConstant.STATUE_DELETE);
        //日志记录
        saveLog(entity, CommonConstant.SUBMIT_DELETE, InnerAccountConstant.BIZ_TYPE_DELETE, user.getLoginName());
        //完成通知提醒
        notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCINITACC_BACK, entity.getId(), user.getId());
    }

    public FcInitAcc getById(String id) {
        return fcInitAccDao.getById(id);
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
    public void save(FcInitAcc entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcInitAcc entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(FcInitAcc entity) {
        super.saveOrUpdate(entity);
    }

    @Override
    public BaseDao<FcInitAcc, String> getEntityDao() {
        return fcInitAccDao;
    }

    /**
     * 根据参数查询可以初始化的内部账号
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput tableInit(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder joinSql=new StringBuilder();
        Map<String,Object> params=new HashMap();
        String corpAuthSql=SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCINITACC_QUERY,user.getId()),"a.corp_Id",params);
        joinSql.append(" and ").append(corpAuthSql);
        return fcInitAccDao.tableInit(dataTablesInput,joinSql.toString(),user.getLoginName(),params);
    }

    private DataTablesOutput getDataTablesOutput(DataTablesOutput output, List<FcAccVO> fcAccVOList) {
        List<FcInitAccVO> fcAccList = new ArrayList();
        for (FcAccVO vo : fcAccVOList) {
            boolean isAdd = true;//关联关系是否查询到内部账号
            // 查询FC_ONLINE_LEDGER
            FcOnlineLedgerVO fcOnlineLedgerDTO = fcOnlineLedgerService.getEntityByAccount(vo.getBlnctrAcc());
            if (null != fcOnlineLedgerDTO) {
                //   AND b.tally_cnt = 0 AND b.check_cnt = 0 AND b.online_balance = 0 AND b.check_balance = 0
                if (null == fcOnlineLedgerDTO.getTallyCnt())
                    fcOnlineLedgerDTO.setTallyCnt(0);
                if (null == fcOnlineLedgerDTO.getCheckCnt())
                    fcOnlineLedgerDTO.setCheckCnt(0);
                if (0 == fcOnlineLedgerDTO.getTallyCnt() && 0 == fcOnlineLedgerDTO.getCheckCnt()
                        && 0 == BigDecimal.ZERO.compareTo(fcOnlineLedgerDTO.getOnlineBalance())
                        && 0 == BigDecimal.ZERO.compareTo(fcOnlineLedgerDTO.getCheckBalance())) {
                } else {
                    isAdd = false;
                    continue;
                }
                FcInitAcc fcInitAcc = fcInitAccDao.listByAccount(vo.getBlnctrAcc());
                if (null != fcInitAcc) {
                    if (fcInitAcc.getStatus().compareTo(-2) != 0) {
                        if (StringUtils.isNotEmpty(fcInitAcc.getId())) {
                            isAdd = false;
                            continue;
                        }
                    }
                }
                FcAccBalanceVO fcAccBalanceVO = fcAccBalanceService.getGroupByAccount(vo.getBlnctrAcc());
                if (null != fcAccBalanceVO) {
                    if (fcAccBalanceVO.getCnt() > 1) {
                        isAdd = false;
                        continue;
                    }
                }
                if (isAdd) {
                    FcInitAccVO fcInitAccVO = new FcInitAccVO();
                    fcInitAccVO.setId("0");
                    fcInitAccVO.setAccount(vo.getBlnctrAcc());
                    fcInitAccVO.setAccName(vo.getAccName());
                    fcInitAccVO.setBalanceWay(vo.getBalanceWay());
                    fcInitAccVO.setCurId(vo.getCurId());
                    fcInitAccVO.setCumulation(new BigDecimal(0));
                    fcInitAccVO.setBalance(new BigDecimal(0));
                    fcInitAccVO.setStatus(-10);
                    fcInitAccVO.setAccStartDate(vo.getAccStartDate());
                    fcInitAccVO.setYearBalance(new BigDecimal(0));
                    fcInitAccVO.setYearTotal(new BigDecimal(0));
                    fcInitAccVO.setNetId(vo.getNetId());
                    fcInitAccVO.setCorpId(vo.getCorpId());
                    fcAccList.add(fcInitAccVO);
                }
            }
        }
        if (null == output.getData()) {
            output.setData(fcAccList);
        } else {
            output.getData().addAll(fcAccList);
        }
        return output;
    }

    /**
     * 保存
     *
     * @param fcInitAccList
     */
    public void saveFcAccbook(FcInitAccListVO fcInitAccList, String opType, ICurrentUser user) {
        List<FcInitAccVO> fcInitAccs = fcInitAccList.getFcInitAccs();
        List<FcInitAcc> list = new ArrayList<FcInitAcc>();
        FcInitAcc fcInitAcc;
        java.util.Date initTime = getDate();
        for (FcInitAccVO vo : fcInitAccs) {
            fcInitAcc = new FcInitAcc();
            BeanUtils.copyNotNullProperties(vo, fcInitAcc);
            if (vo.getBalanceWay() != null && vo.getBalanceWay() == 1) {
                fcInitAcc.setBalance(vo.getDebitMoney());
            } else {
                fcInitAcc.setBalance(vo.getCreditMoney());
            }
            fcInitAcc.setId("0".equals(vo.getId()) ? null : vo.getId());
            fcInitAcc.setInitTime(initTime);
            fcInitAcc.setCreateBy(user.getLoginName());
            fcInitAcc.setUpdateBy(user.getLoginName());
            fcInitAcc.setCreateDate(initTime);
            fcInitAcc.setUpdateDate(initTime);
            fcInitAcc.setStatus(fcInitAccList.getStatus());
            if (StringUtils.isEmpty(vo.getId())) {  //批量导入
                FcInitAcc initAcc = fcInitAccDao.listByAccount(fcInitAcc.getAccount());
                if (initAcc != null && StringUtils.isNotEmpty(initAcc.getId())) {
                    fcInitAcc.setId(initAcc.getId());
                }
            }
            list.add(fcInitAcc);
        }
        //判断是否能进行初始化
        isInitable(list);
        for (int i = 0; i < list.size(); i++) {
            FcInitAcc acc = list.get(i);
            if (StringUtils.isNotEmpty(acc.getId())) {
                fcInitAccDao.merge(acc);
            } else {
                fcInitAccDao.save(acc);
            }
            fcInitAccDao.flush();
            //完成通知提醒
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCINITACC_BACK, acc.getId(), user.getId());
            //日志记录
            if (CommonConstant.STATUE_TEMP.equals(acc.getStatus())) {
                saveLog(acc, opType, InnerAccountConstant.BIZ_TYPE_TEMP, user.getLoginName());
            } else {
                FcAccVO fcAcc = fcAccService.findFcAccByBlnctrAcc(acc.getAccount());
                if (fcAcc != null && StringUtils.isNotEmpty(fcAcc.getCorpId())) {
                    notificationService.sendIgnoreMySelf(acc.getId(), fcAcc.getCorpId(), NotificationConstant.NETBANK.INNERACCOUNT_FCINITACC_CHECK, acc, user.getId());
                }
                saveLog(acc, opType, InnerAccountConstant.BIZ_TYPE_SUBMIT, user.getLoginName());
            }
        }
    }

    /**
     * 查询复核数据集
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser userVO) {

        DataTablesOutput output = new DataTablesOutput<>();
        List<FcAccVO> fcAccVOList = queryFcAcc(userVO, dataTablesInput);
        if (fcAccVOList.size() > 0) {
            List<String> accountS = new ArrayList<>();
            Map<String, FcAccVO> accMap = new HashMap<>();
            for (FcAccVO acc : fcAccVOList) {
                accountS.add(acc.getBlnctrAcc());
                accMap.put(acc.getBlnctrAcc(), acc);
            }

            StringBuilder joinSql = new StringBuilder();
            joinSql.append(" and c.create_by !='").append(userVO.getLoginName()).append("'");
            joinSql.append(" and ").append(SqlUtil.getInSql(accountS, "c.account", true));


            output = fcInitAccDao.getDataTablesUnion(dataTablesInput, joinSql.toString());
            List<FcInitAccVO> list = output.getData();
            for (FcInitAccVO vo : list) {
                String id = vo.getId();
                BeanUtils.copyNotNullProperties(accMap.get(vo.getAccount()), vo);
                vo.setId(id);
            }
        }
        return output;
    }

    private List<FcAccVO> queryFcAcc(ICurrentUser userVO, DataTablesInput dataTablesInput) {

        String netId = (String) dataTablesInput.getFormdata().get("eqs_netId");
        String ins_corpId = (String) dataTablesInput.getFormdata().get("ins_corpId");
        String likes_account = (String) dataTablesInput.getFormdata().get("likes_account");
        String eqs_curId = (String) dataTablesInput.getFormdata().get("eqs_curId");
        dataTablesInput.getFormdata().remove("eqs_netId");
        dataTablesInput.getFormdata().remove("ins_corpId");
        dataTablesInput.getFormdata().remove("likes_account");
        dataTablesInput.getFormdata().remove("eqs_curId");
        FcAccVO fcAccVO = new FcAccVO();
        fcAccVO.setNetId(netId);
        fcAccVO.setCurId(eqs_curId);
        fcAccVO.setPoolSign("0");
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCINITACC_QUERY, userVO.getId()), "t.corp_Id",
                true);
        StringBuilder joinSqlAcc = new StringBuilder();
        Map<String, Object> params = new HashMap();
        joinSqlAcc.append(" and ").append(corpAuthSql);
        if (StringUtils.isNotEmpty(likes_account)) {
            joinSqlAcc.append(" and t.blnctr_acc like:blnctr_acc");
            params.put("blnctr_acc", "%" + likes_account + "%");
        }
        if (StringUtils.isNotEmpty(ins_corpId)) {
            joinSqlAcc.append(" and ").append(SqlUtil.getInSql(ins_corpId.split(","), "t.corp_id", true));
        }
        joinSqlAcc.append(" and t.acc_type!='" + InnerAccountConstant.FCACC_OFFBALANCE + "' ");
        joinSqlAcc.append(" and t.pool_sign<>'1' ");//资金池标识：0-非资金池 1-资金池
        if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("status"))) {
            String status = dataTablesInput.getFormdata().get("status").toString();
            if ("-10".equals(status)) {//新建状态
                joinSqlAcc.append(" and t.ACC_STATE = 'N' ");
            }
            //-20 全部
            if ("-20".equals(status)) {
                joinSqlAcc.append(" and t.ACC_STATE  != 'D' ");
            }

        }

        return fcAccService.getFcAcc(fcAccVO, joinSqlAcc.toString(), params);
    }

    private void leftJoinFcAcc(ICurrentUser userVO, FcInitAccVO vo) {
        FcAccVO fcAccVO = new FcAccVO();
        fcAccVO.setBlnctrAcc(vo.getAccount());
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCINITACC_QUERY, userVO.getId()), "t.corp_Id",
                true);
        StringBuilder joinSqlAcc = new StringBuilder();
        joinSqlAcc.append(" and ").append(corpAuthSql);
        Map<String, Object> params = new HashMap();
        List<FcAccVO> fcAccVOList = fcAccService.getFcAcc(fcAccVO, joinSqlAcc.toString(), params);
        String idtemp = vo.getId();
        for (FcAccVO fcAcc : fcAccVOList) {
            BeanUtils.copyNotNullProperties(fcAcc, vo);
            vo.setId(idtemp);
        }
    }

    /**
     * 复核通过 or 打回
     *
     * @param ids
     */
    public void check(String ids, String status, ICurrentUser user) {
        //查询待复核初始化账户
        List<FcInitAcc> initAccs = fcInitAccDao.getListByIds(ids);
        List<String> accountList = initAccs.stream().map(FcInitAcc::getAccount).collect(Collectors.toList());
        List<FcAccDTO> accList = fcAccService.getAccSet(accountList);
        Map<String, FcAccDTO> accountMap = new HashMap<>();
        if (accList != null && accList.size() > 0) {
            accountMap = accList.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
        }
        Integer sta = Integer.valueOf(status);
        if (CommonConstant.STATUE_BACK.intValue() == sta.intValue()) {
            //更新复核基本信息
            fcInitAccDao.updateStatus(ids, sta);
            //日志记录
            for (FcInitAcc initAcc : initAccs) {
                if (!CommonConstant.STATUE_WAIT_CHECK.equals(initAcc.getStatus())) {
                    throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS, initAcc.getAccount());
                }
                //完成通知提醒
                notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCINITACC_CHECK, initAcc.getId(), user.getId());
                FcAccVO fcAcc = fcAccService.findFcAccByBlnctrAcc(initAcc.getAccount());
                if (fcAcc != null && StringUtils.isNotEmpty(fcAcc.getCorpId())) {
                    notificationService.sendToUserByLoginName(initAcc.getId(), initAcc.getCreateBy(), NotificationConstant.NETBANK.INNERACCOUNT_FCINITACC_BACK, initAcc);
                }
                saveLog(initAcc, CommonConstant.SUBMIT_CHECK, InnerAccountConstant.BIZ_TYPE_CHECK_BACK, user.getLoginName());
            }
        } else {
            //判断是否能进行初始化
            isInitable(initAccs);
            //检查平衡
            checkBanlace(initAccs, accountMap);
            //更新复核基本信息
            fcInitAccDao.updateStatusCheck(ids, sta, user.getLoginName());
            for (FcInitAcc initAcc : initAccs) {
                if (!CommonConstant.STATUE_WAIT_CHECK.equals(initAcc.getStatus())) {
                    throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS, initAcc.getAccount());
                }

                String account = initAcc.getAccount();
                BigDecimal balance = initAcc.getBalance() == null ? BigDecimal.ZERO : initAcc.getBalance();
                FcAccVO fcAcc = fcAccService.findFcAccByBlnctrAcc(initAcc.getAccount());
                //更新每日余额
                Date balanceDate = new Date(InnerAccountUtils.getBalanceDate(
                        new Date(fcAcc.getAccStartDate().getTime())).getTime());
                fcAccBalanceService.updateAccBalanceByInitAcc(balanceDate, fcAcc.getBlnctrAcc(), balance);
                //更新联机余额表
                fcOnlineLedgerService.updateOnlineBalanceByInitAcc(account, balance);
                //更新积数
                fcAccuBalanceService.updateAccountAccuBalanceByBalanceDate(account, balanceDate, initAcc.getCumulation());
                //完成通知提醒
                notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCINITACC_CHECK, initAcc.getId(), user.getId());
                //日志记录
                saveLog(initAcc, CommonConstant.SUBMIT_CHECK, InnerAccountConstant.BIZ_TYPE_CHECK_SUCCESS, user.getLoginName());
            }
        }
    }

    /**
     * 是否能初始化，条件:1、联机余额表记账金额为0；2、每日余额表只有一条记录
     *
     * @param initAccs
     */
    private void isInitable(List<FcInitAcc> initAccs) {
        List<String> initAccList = initAccs.stream().map(FcInitAcc::getAccount).collect(Collectors.toList());
        List<String> allCanInitAccounts = getCanInitAccounts(initAccList);
        initAccList.removeAll(allCanInitAccounts);
        if (!initAccList.isEmpty()) {
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_BLAND_CHANGE, StringUtils.join(initAccList.toArray()));
        }
    }


    public List<String> getCanInitAccounts(List<String> accList) {
        List<String> canInitAccounts = new ArrayList<>();
        Map<String, FcAccDTO> fcAccMap = fcAccService.getFcAccByBlnctrAccs(accList).stream()
                .collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
        Map<String, FcOnlineLedgerDTO> balanceByAccMap = fcOnlineLedgerService.getBalanceByAccMap(accList);
        Map<String, FcAccBalanceVO> balanceVOMap = fcAccBalanceService.getGroupByAccount(accList);
        for (String accNo : accList) {
            FcAccDTO fcAccDTO = fcAccMap.get(accNo);
            if (!ObjectUtils.isEmpty(fcAccDTO)) {
                if (!InnerAccountConstant.ACC_STATE_NORMAL.equals(fcAccDTO.getAccState()) ||
                        InnerAccountConstant.FCACC_OFFBALANCE.equals(fcAccDTO.getAccType())) {
                    continue;
                }
            }

            FcOnlineLedgerDTO fcOnlineLedger = balanceByAccMap.get(accNo);
            if (null == fcOnlineLedger) {
                continue;
            }
            if (!Integer.valueOf(0).equals(fcOnlineLedger.getTallyCnt())) {
                continue;
            }
            if (!Integer.valueOf(0).equals(fcOnlineLedger.getCheckCnt())) {
                continue;
            }
            if (BigDecimal.ZERO.compareTo(fcOnlineLedger.getOnlineBalance()) != 0) {
                continue;
            }
            if (BigDecimal.ZERO.compareTo(fcOnlineLedger.getCheckBalance()) != 0) {
                continue;
            }
            FcAccBalanceVO fcAccBalanceVO = balanceVOMap.get(accNo);
            if (null != fcAccBalanceVO && fcAccBalanceVO.getCnt() > 1) {
                continue;
            }
            canInitAccounts.add(accNo);
        }
        return canInitAccounts;
    }

    /**
     * 根据某天,检查平衡
     *
     * @param initAccs
     */
    private void checkBanlace(List<FcInitAcc> initAccs, Map<String, FcAccDTO> accountMap) {
        //当前待复核的借贷金额
        Map<Date, BigDecimal[]> dateInitAccInfoMap = packInitAccountBalance(initAccs, accountMap);
        if (dateInitAccInfoMap.isEmpty()) {
            return;
        }
        //每日余额表的借贷金额
        Map<Date, BigDecimal[]> dateAccInfoMap = findAccBalanceInfoByDate(dateInitAccInfoMap.keySet()
                .toArray(new Date[dateInitAccInfoMap.size()]));
        for (Map.Entry<Date, BigDecimal[]> entry : dateInitAccInfoMap.entrySet()) {
            BigDecimal[] totalBalanceMoney = entry.getValue();
            //日期的借贷总金额
            BigDecimal[] dateBalanceMoney = dateAccInfoMap.get(new Date(entry.getKey().getTime()));
            if (dateBalanceMoney != null) {
                //最终借方金额
                totalBalanceMoney[0] = totalBalanceMoney[0].add(dateBalanceMoney[0]);
                //最终贷方方金额
                totalBalanceMoney[1] = totalBalanceMoney[1].add(dateBalanceMoney[1]);
            }
            //检查借贷方是否平衡
            if (totalBalanceMoney[0].doubleValue() != totalBalanceMoney[1].doubleValue()) {
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_DATE_IMBALANCE, DateUtils.getDateStr(entry.getKey(), "yyyy-MM-dd"));
            }
        }
    }

    public Map<Date, BigDecimal[]> findAccBalanceInfoByDate(Date[] dates) {
        if (dates == null || dates.length == 0) {
            throw new IllegalArgumentException("Dates array must not be null or empty");
        }
        Map<Date, BigDecimal[]> accBalanceInfo = new HashMap<>(dates.length);
        List<FcAccBalanceVO> fcAccBalanceVOList = fcAccBalanceService.getGroupByDates(dates);
        for (FcAccBalanceVO fcAccBalanceVO : fcAccBalanceVOList) {
            FcAccVO fcAccVO = new FcAccVO();
            fcAccVO.setBlnctrAcc(fcAccBalanceVO.getAccount());
            Map<String, Object> param = new HashMap<>();
            List<FcAccVO> fcAccVOList = fcAccService.getFcAcc(fcAccVO, null, param);
            for (FcAccVO vo : fcAccVOList) {
                if ("1".equals(vo.getBalanceWay())) {
                    accBalanceInfo.put(fcAccBalanceVO.getBalanceDate(), new BigDecimal[]{fcAccBalanceVO.getCreditMoney(), BigDecimal.ZERO});
                } else if ("-1".equals(vo.getBalanceWay())) {
                    accBalanceInfo.put(fcAccBalanceVO.getBalanceDate(), new BigDecimal[]{BigDecimal.ZERO, fcAccBalanceVO.getDebitMoney()});
                }
            }
        }
        return Collections.unmodifiableMap(accBalanceInfo);
    }

    /**
     * 组装当前待复核的借贷金额
     *
     * @param initAccs
     * @return
     */
    private Map<Date, BigDecimal[]> packInitAccountBalance(List<FcInitAcc> initAccs, Map<String, FcAccDTO> accountMap) {
        int debitWay = Integer.parseInt(FinacialConstant.BALANCE_WAY_DEBIT);
        Map<Date, BigDecimal[]> dateInitAccInfoMap = new HashMap<Date, BigDecimal[]>();
        for (FcInitAcc initAcc : initAccs) {
            FcAccVO fcAcc = fcAccService.findFcAccByBlnctrAcc(initAcc.getAccount());
            //2017/11/20 李又虎 提议 检查的时间点是：启用日期 减一天
            Date accStartDate = InnerAccountUtils.getBalanceDate(fcAcc.getAccStartDate());
            BigDecimal[] dateMoney = dateInitAccInfoMap.get(accStartDate);
            if (dateMoney == null) {
                dateMoney = new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO};
                dateInitAccInfoMap.put(accStartDate, dateMoney);
            }
            BigDecimal bal = initAcc.getBalance() == null ? BigDecimal.ZERO : initAcc.getBalance();
            if (accountMap.containsKey(initAcc.getAccount())) {
                FcAccDTO fcAccVO = accountMap.get(initAcc.getAccount());
                if (fcAccVO.getBalanceWay() == debitWay) {
                    dateMoney[0] = dateMoney[0].add(bal);
                } else {
                    dateMoney[1] = dateMoney[1].add(bal);
                }
            }else {
                throw new ParameterException("内部账户不存在！");
            }
        }
        return dateInitAccInfoMap;
    }

    /**
     * 导入功能
     *
     * @param list
     */
    public void saveImport(List<FcInitAccVO> list, ICurrentUser user) {
        if (list != null && !list.isEmpty()) {
            FcInitAccListVO vo = new FcInitAccListVO();
            vo.setFcInitAccs(list);
            vo.setStatus(CommonConstant.STATUE_TEMP);
            this.saveFcAccbook(vo, CommonConstant.SUBMIT_IMPORT, user);
        }
    }

    /**
     * 初始化未复核
     *
     * @param account
     * @return
     */
    public boolean accountInitCheck(String account) {
        return fcInitAccDao.accountInitCheck(account);
    }

    /**
     * 导入校验方法
     *
     * @param obj 当前对象
     * @return
     */
    @Override
    public ExcelVerifyHanlderResult verifyHandler(FcInitAccVO obj, Object... user) {
        return null;
    }

    /**
     * 导入校验方法
     *
     * @param list 当前集合对象
     *             最后将有错误的信息调用T里的addErrorMsg方法
     */
    @Override
    public void verifyHandler(List<FcInitAccVO> list, Object... user) {
        List<String> errorMsgList; //错误存储列表声明
        if (list != null && !list.isEmpty()) {
            int debitWay = Integer.parseInt(FinacialConstant.BALANCE_WAY_DEBIT);
            int creditWay = Integer.parseInt(FinacialConstant.BALANCE_WAY_CREDIT);
            //获取可以操作的账号
            DataTablesInput input = new DataTablesInput();
            input.setLength(100000);
            ShiroUser shiroUser = (ShiroUser) UserContextHelper.helper().getCurrentUser();
            List<FcInitAccVO> controlAccount = this.tableInit(input, shiroUser).getData();
            Map<String, FcInitAccVO> accountMap = controlAccount.stream().collect(Collectors.toMap(FcInitAccVO::getAccount, Function.identity()));
            for (FcInitAccVO fcInitAccVO : list) {
                errorMsgList = new ArrayList<>();
                boolean flag = false; //controlAccount对象列表中找到账号 flag =true
                Integer balanceWay = 0;//余额方向
                if (CollectionUtils.isEmpty(controlAccount)) {
                    break;
                }
                if (accountMap.containsKey(fcInitAccVO.getAccount())) {
                    FcInitAccVO vo = accountMap.get(fcInitAccVO.getAccount());
                    balanceWay = vo.getBalanceWay();
                    //账户名称为空
                    if (StringUtils.isEmpty(fcInitAccVO.getAccName())) {
                        fcInitAccVO.setAccName(vo.getAccName());
                    }
                    flag = true;
                } else {
                    errorMsgList.add(InnerAccountConstant.NETBANK_INNER_EXCEL_ERRORMSG_ACCOUNT_ABSENT);
                }
                if (flag) {//找到账号
                    if (balanceWay == debitWay) { //借方余额必须要有值
                        fcInitAccVO.setBalanceWay(debitWay);
                        if (fcInitAccVO.getDebitMoney() == null || fcInitAccVO.getDebitMoney().compareTo(BigDecimal.ZERO) < 0) {
                            errorMsgList.add(InnerAccountConstant.NETBANK_INNER_EXCEL_ERRORMSG_DEBITWAY_NULL);
                        }
                    } else {
                        fcInitAccVO.setBalanceWay(creditWay);
                        if (fcInitAccVO.getCreditMoney() == null || fcInitAccVO.getCreditMoney().compareTo(BigDecimal.ZERO) < 0) {
                            errorMsgList.add(InnerAccountConstant.NETBANK_INNER_EXCEL_ERRORMSG_CREDITWAY_NULL);
                        }
                    }
                } else {
                    errorMsgList.add(InnerAccountConstant.NETBANK_INNER_EXCEL_ERRORMSG_ACCOUNT_INVALID);
                }
                fcInitAccVO.addErrorMsg(errorMsgList);
            }
        }
    }

    /**
     * 保存初始化日志
     *
     * @param acc
     * @param opType 　操作类型
     * @param bizMsg 　业务信息
     */
    private void saveLog(FcInitAcc acc, String opType, String bizMsg, String loginName) {
        FcAccLogDTO fcAccLog = new FcAccLogDTO();
        fcAccLog.setBizId(acc.getId());
        fcAccLog.setOpType(opType);
        fcAccLog.setAccType("");
        fcAccLog.setBizMsg(bizMsg);
        fcAccLog.setAccount(acc.getAccount());
        fcAccLog.setBizType(InnerAccountConstant.BIZ_TYPE_INIT);
        fcAccLogService.saveLog(fcAccLog, loginName);
    }

    public List<FcAccInitCheckBeanDTO> findNoCheckAccInit(FcDayCloseDTO dayClose) {
//		return fcInitAccDao.findNoCheckAccInit(dayClose);
        return null;
    }

    public List<FcInitSubjectDTO> getInitSubjectDTO(String netId) {
        List<FcAccVO> fcAccVOList = fcAccService.listBy("netId", netId);
        if (CollectionUtil.isEmpty(fcAccVOList)) {
            return new ArrayList<>();
        }
        Map<String, FcAccVO> fccVoMap = fcAccVOList.stream().collect(Collectors.toMap(FcAccVO::getBlnctrAcc, item -> item, (pre, next) -> next));
        List<String> accounts = new ArrayList<>(fccVoMap.keySet());
        List<FcInitAcc> fcInitAccs = fcInitAccDao.finInitAccByAccAndStatus(accounts, CommonConstant.STATUE_CHECK);
        //按币别与科目分组求和返回
        return fcInitAccs.stream().map(item -> {
            FcInitSubjectDTO fcInitSubjectDTO = new FcInitSubjectDTO();
            FcAccVO fcAccVO = fccVoMap.get(item.getAccount());
            fcInitSubjectDTO.setSubjectId(fcAccVO.getSubjectId());
            fcInitSubjectDTO.setCurId(fcAccVO.getCurId());
            fcInitSubjectDTO.setYearBalance(item.getYearBalance());
            fcInitSubjectDTO.setYearTotal(item.getYearTotal());
            return fcInitSubjectDTO;
        }).collect(Collectors.groupingBy(item -> item.getCurId() + item.getSubjectId(), Collectors.reducing(FcInitSubjectDTO::merge)))
                .values().stream().map(Optional::get).collect(Collectors.toList());
    }

    /**
     * 功能介绍 : 查询网点初始化记录。<br>
     * <b>日期：</b>2017年7月18日<br>
     *
     * @author liyh
     */
    public List<FcInitAccDTO> queryInitAccByNetId(String netId) {
        Map<String, FcAccDTO> fcAccMap = fcAccService.listFcAccByNetId(netId);
        if (CollectionUtil.isEmpty(fcAccMap)) {
            return new ArrayList<>();
        }
        List<String> accounts = new ArrayList<>(fcAccMap.keySet());
        List<FcInitAcc> fcInitAccList = fcInitAccDao.finInitAccByAcc(accounts);
        return fcInitAccList.stream().map(fcInitAcc -> {
            FcInitAccDTO fcInitAccDTO = new FcInitAccDTO();
            BeanUtils.copyProperties(fcInitAcc, fcInitAccDTO);
            FcAccDTO fcAccDTO = fcAccMap.get(fcInitAcc.getAccount());
            if (Objects.nonNull(fcAccDTO)) {
                fcInitAccDTO.setAccName(fcAccDTO.getAccName());
                fcInitAccDTO.setBalanceWay(fcAccDTO.getBalanceWay());
                fcInitAccDTO.setCurId(fcAccDTO.getCurId());
                fcInitAccDTO.setNetId(fcAccDTO.getNetId());
                fcInitAccDTO.setSubjectId(fcAccDTO.getSubjectId());
            }
            return fcInitAccDTO;
        }).collect(Collectors.toList());
    }
}
