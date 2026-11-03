package com.bytter.biz.inneraccount.service;

import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.service.IFcAccBalanceService;
import com.bytter.biz.finacial.api.service.IFcAccuBalanceService;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.api.vo.FcAccBalanceVO;
import com.bytter.biz.finacial.api.vo.FcAccuBalanceVO;
import com.bytter.biz.inneraccount.util.InnerAccountUtils;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.FcAccLogDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.bytter.system.api.vo.FcAccHistoryVO;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户信息表
 * @date 2017-05-25 19:36:12
 */
@Service("fcAccChangeService")
@Transactional
public class FcAccChangeService extends AbstractService {
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
    private IFcAccBalanceService fcAccBalanceService;
    @DubboReference(check = false)
	private IFcDayCloseService fcDayCloseService;
    @DubboReference(check = false)
    private IFcAccuBalanceService fcAccuBalanceService;
    @Autowired
    private FcAccHistoryService fcAccHistoryService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IFcAccLogService fcAccLogService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
	private IFcSubjectService fcSubjectService;

    /**
     * 可改户申请的数据列表
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,ICurrentUser userVO) {
        return fcAccHistoryService.getDataTablesUnion(dataTablesInput,userVO);
    }
    /**
     * 可改户申请的数据列表
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput,ICurrentUser userVO) {
        return fcAccHistoryService.getDataTablesUnionForPool(dataTablesInput,userVO);
    }
    /**
     * 复核数据列表
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getCheckDataTable(DataTablesInput dataTablesInput,ICurrentUser user) {
        return fcAccHistoryService.getCheckDataTable(dataTablesInput,user);
    }
    /**
     * 复核数据列表
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getCheckDataTableForPool(DataTablesInput dataTablesInput,ICurrentUser user) {
        return fcAccHistoryService.getCheckDataTableForPool(dataTablesInput,user);
    }
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccHistoryVO findFcAccHistoryVOById(String id) {
        return fcAccHistoryService.findFcAccHistoryVOById(id);
    }
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccHistoryVO findFcAccHistoryVOByIdForPool(String id) {
        return fcAccHistoryService.findFcAccHistoryVOByIdForPool(id);
    }
    /**
     * 改户复核通过或者失败
     *
     * @param fcAccHistoryVO
     */
    public void check(FcAccHistoryVO fcAccHistoryVO,ICurrentUser user,boolean flagAuto) {
        FcAccHistoryVO fcAccHistory = fcAccHistoryService.getFcAccHistoryById(fcAccHistoryVO.getId());
        fcAccHistory.setCheckerCode(user.getLoginName());
        fcAccHistory.setCheckTime(DateUtils.getDate());
        fcAccHistory.setCheckBackReason(fcAccHistoryVO.getCheckBackReason());
        if(!InnerAccountConstant.ACC_STATE_EDIT.equals(fcAccHistory.getAccState())){
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS,fcAccHistory.getBlnctrAcc());
        }
        FcAccVO fcAcc = new FcAccVO();
        //完成通知提醒
        if(AllocationConstant.POOL_SIGN_1.equals(fcAccHistory.getPoolSign())){
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CHANGECHECK, fcAccHistoryVO.getId(), user.getId());
        }else {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CHANGECHECK, fcAccHistoryVO.getId(), user.getId());
        }
        if (InnerAccountConstant.ACC_STATE_NORMAL.equals(fcAccHistoryVO.getAccState()) || flagAuto ) {//改户复核通过
            BeanUtils.copyNotNullProperties(fcAccHistory,fcAcc);
            fcAcc.setAccState(InnerAccountConstant.ACC_STATE_NORMAL);
            fcAccHistory.setAccState(InnerAccountConstant.ACC_STATE_NORMAL);
            //账户积数处理
            opAccuBalance(fcAccHistoryVO,user.getLoginName());
            //日志记录
            saveLog(fcAccHistory, CommonConstant.SUBMIT_CHECK,InnerAccountConstant.BIZ_TYPE_CHECK_SUCCESS,user.getLoginName());
        } else {//改户打回
            FcAccVO acc = fcAccService.getFcAcc(fcAccHistory.getFcaccId());
            BeanUtils.copyNotNullProperties(acc,fcAcc);
            fcAcc.setAccState(InnerAccountConstant.ACC_STATE_F);
            fcAccHistory.setAccState(InnerAccountConstant.ACC_STATE_F);
            if(AllocationConstant.POOL_SIGN_1.equals(fcAccHistoryVO.getPoolSign())) {
                notificationService.sendToUserByLoginName(fcAccHistoryVO.getId(), fcAcc.getCreateBy(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CHANGEBACK, fcAcc);
            }else{
                notificationService.sendToUserByLoginName(fcAccHistoryVO.getId(), fcAcc.getCreateBy(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CHANGEBACK, fcAcc);
            }
            //日志记录
            saveLog(fcAccHistory, CommonConstant.SUBMIT_CHECK,InnerAccountConstant.BIZ_TYPE_CHECK_BACK,user.getLoginName());
        }
        fcAcc.setId(fcAccHistory.getFcaccId());
        fcAcc.setCheckerCode(user.getLoginName());
        fcAcc.setCheckTime(DateUtils.getDate());
        fcAcc.setCheckBackReason(fcAccHistoryVO.getCheckBackReason());
        if(fcAcc.getPoolSign() != null){
            fcAcc.setPoolSign(fcAcc.getPoolSign().trim());
        }
        if(fcAcc.getReferenceRateType() != null){
            fcAcc.setReferenceRateType(fcAcc.getReferenceRateType().trim());
        }
        
        fcAccService.merge(fcAcc);
        fcAccHistoryService.saveOrUpdate(fcAccHistory);
        //改户通过计息账户处理
        saveFcAccInterestAcc(fcAccHistory);
    }
    /**
     * 账户积数处理
     * 1、开户的时候判断 计息标志，计息积数初始化为 0。
     * 2、改户 是判断 不计息改成计息，取日结日期积数表是否存在,【不存在，插入积数表 基数为balance （ select balance from fc_acc_balance）】。
     * 3、查询fc_accu_balance 积数表是否存在 条件：balance_date = fc_acc（AccStartDate 一天之前的日期）.基数不存在，插入积数表 基数为0 .
     * @param fcAccHistoryVO
     */
    private void opAccuBalance(FcAccHistoryVO fcAccHistoryVO,String loginName){
        FcAccHistoryVO history = fcAccHistoryService.get(fcAccHistoryVO.getId());
        FcAccVO acc = fcAccService.getFcAcc(history.getFcaccId());
        //改户 是判断 不计息改成计息
        if(InnerAccountConstant.INTERESTSIGN_NO.equals(acc.getInterestSign())
                && InnerAccountConstant.INTERESTSIGN_YES.equals(history.getInterestSign())){
            Date tallyDate = null;
            if(AllocationConstant.POOL_SIGN_1.equals(history.getPoolSign())) {
                tallyDate = fcDayCloseService.getPoolDayCloseDate(acc.getNetId());
                //启用日期必须大于当前网点已日结日期
                fcAccService.dateCompareForPool(history.getAccStartDate(), tallyDate);
            }else{
                tallyDate = fcDayCloseService.getDayCloseFinalDate(acc.getNetId());
                //启用日期必须大于当前网点已日结日期
                //修改:经沟通要求改户不做日期判断控制
//                fcAccService.dateCompare(history.getAccStartDate(), tallyDate);
            }
            //2.取日结日期积数表是否存在, 不存在，插入积数表 基数为balance
            FcAccuBalanceVO fcAccuBalance = fcAccuBalanceService.getEntityByAccountAndDate(tallyDate,acc.getBlnctrAcc());
            if(fcAccuBalance == null){
                FcAccBalanceVO fcAccBalance = fcAccBalanceService.getEntityByAccountAndDate(tallyDate,acc.getBlnctrAcc());
                if(fcAccBalance == null) {
                    fcAccuBalanceService.initAccuBalance(tallyDate,acc.getBlnctrAcc(),acc.getCurId(), BigDecimal.ZERO,loginName);
                }else {
                    fcAccuBalanceService.initAccuBalance(tallyDate, acc.getBlnctrAcc(), acc.getCurId(),fcAccBalance.getBalance(),loginName);
                }
            }
            //3.查询fc_accu_balance 积数表是否存在 条件：balance_date = fc_acc（AccStartDate 一天之前的日期）.基数不存在，插入积数表 基数为0
            Date balanceDate = InnerAccountUtils.getBalanceDate(new java.util.Date(history
                    .getAccStartDate().getTime()));
            fcAccuBalance = fcAccuBalanceService.getEntityByAccountAndDate(balanceDate,acc.getBlnctrAcc());
            if(fcAccuBalance == null) {
                fcAccuBalanceService.initAccuBalance(balanceDate, acc.getBlnctrAcc(), acc.getCurId(),BigDecimal.ZERO,loginName);
            }
        }
    }
    /**
     * 改户(暂存 or 提交）
     * @param fcAccHistoryVO
     */
    public void change(FcAccHistoryVO fcAccHistoryVO,ICurrentUser user){
        //改户的数据存储对象
        FcAccHistoryVO fcAccHistory= new FcAccHistoryVO();
        if(StringUtils.isEmpty(fcAccHistoryVO.getOverdraftSign())){
            fcAccHistoryVO.setOverdraftSign(InnerAccountConstant.OVERDRAFT_SIGN_N);
        }
        Date now = DateUtils.getDate();
        String accId;
        if(StringUtils.isNotEmpty(fcAccHistoryVO.getFcaccId())){
            accId =fcAccHistoryVO.getFcaccId();
            FcAccHistoryVO history = fcAccHistoryService.getFcAccHistoryById(fcAccHistoryVO.getId());
            BeanUtils.copyNotNullProperties(history,fcAccHistory);
            BeanUtils.copyProperties(fcAccHistoryVO,fcAccHistory);
            fcAccHistory.setCreateBy(user.getLoginName());
            fcAccHistory.setCreateDate(now);
            fcAccHistory.setUpdateBy(user.getLoginName());
            fcAccHistory.setUpdateDate(now);
            fcAccHistory.setCheckBackReason(null);
            fcAccHistoryService.saveOrUpdate(fcAccHistory); // 改户时将改户后的数据插入历史表
        }else{
            accId = fcAccHistoryVO.getId();
            FcAccVO acc = fcAccService.getFcAcc(accId);
            BeanUtils.copyNotNullProperties(acc,fcAccHistory);
            BeanUtils.copyProperties(fcAccHistoryVO,fcAccHistory);
            fcAccHistory.setFcaccId(accId);
            fcAccHistory.setId(null);
            fcAccHistory.setCreateBy(user.getLoginName());
            fcAccHistory.setCreateDate(now);
            fcAccHistory.setCheckBackReason(null);
            fcAccHistoryService.save(fcAccHistory); // 改户时将改户后的数据插入历史表
//            fcAccHistoryService.getEntityDao().flush();
        }
        //保存前先清除计息关系
        fcAccHistoryService.delInterestAcc(fcAccHistory,false);
        //改户历史表计息账户处理
        saveInterestAcc(fcAccHistory,user);
        //没有id再读取一次
        fcAccHistory=fcAccHistoryService.getFcAccHistoryVO(fcAccHistory);
        // 主表数据只改变数据状态
        FcAccVO acc = fcAccService.getFcAcc(accId);
        acc.setAccState(fcAccHistoryVO.getAccState());
        fcAccService.merge(acc);
        //完成通知提醒
        if(AllocationConstant.POOL_SIGN_1.equals(fcAccHistory.getPoolSign())) {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CHANGEBACK, fcAccHistory.getId(), user.getId());
        }else {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CHANGEBACK, fcAccHistory.getId(), user.getId());
        }
        if(InnerAccountConstant.ACC_STATE_G.equals(fcAccHistoryVO.getAccState())) {
            //日志记录
            saveLog(fcAccHistory, CommonConstant.SUBMIT_MODIFY, InnerAccountConstant.BIZ_TYPE_TEMP,user.getLoginName());
        }else{
            //日志记录
            saveLog(fcAccHistory, CommonConstant.SUBMIT_MODIFY, InnerAccountConstant.BIZ_TYPE_SUBMIT,user.getLoginName());
            //fcAccService.getEntityDao().flush();
            //获取是否需要自动复核
            int accAutoCheck = 0;
            if(AllocationConstant.POOL_SIGN_1.equals(fcAccHistoryVO.getPoolSign())){
                accAutoCheck = configService.getConfig("pool", "changeAutoCheck").toInteger(0).intValue();
            }else{
                accAutoCheck = configService.getConfig("fcs", "changeAutoCheck").toInteger(0).intValue();
            }
            if (accAutoCheck == 1) {
                FcAccHistoryVO vo = new FcAccHistoryVO();
                vo.setId(fcAccHistory.getId());
                vo.setAccState(InnerAccountConstant.ACC_STATE_NORMAL);
                check(vo,user,true);
            }else{
                if(AllocationConstant.POOL_SIGN_1.equals(fcAccHistory.getPoolSign())){
                    notificationService.sendIgnoreMySelf(fcAccHistory.getId(), fcAccHistory.getCorpId(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CHANGECHECK, fcAccHistory, user.getId());
                }else {
                    notificationService.sendIgnoreMySelf(fcAccHistory.getId(), fcAccHistory.getCorpId(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CHANGECHECK, fcAccHistory, user.getId());
                }
            }
        }
    }
    /**
     * 取消--还原改户申请前的数据
     * @param id
     */
    public void canelEntity(String id,ICurrentUser user) {
        FcAccHistoryVO fcAccHistory = this.fcAccHistoryService.getFcAccHistoryById(id);
        FcAccVO acc = fcAccService.getFcAcc(fcAccHistory.getFcaccId());
        acc.setAccState(InnerAccountConstant.ACC_STATE_NORMAL);// 将账户状态改成正常状态
        fcAccService.update(acc);// 账户信息表更新过来
        //完成通知提醒
        if(AllocationConstant.POOL_SIGN_1.equals(acc.getPoolSign())) {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CHANGEBACK, id, user.getId());
        }else {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CHANGEBACK, id, user.getId());
        }
        this.fcAccHistoryService.delete(id);
        //根据账号删除关联计息账号
        this.fcAccHistoryService.delInterestAcc(fcAccHistory,true);
        //日志记录
        saveLog(fcAccHistory,CommonConstant.SUBMIT_CANCEL,InnerAccountConstant.BIZ_TYPE_CANCEL_INFO,user.getLoginName());
    }
    /**
     * 保存改户日志
     * @param fcAccHistory
     * @param opType　操作类型
     * @param bizMsg　业务信息
     */
    private void saveLog(FcAccHistoryVO fcAccHistory,String opType,String bizMsg,String loginName){
        FcAccLogDTO fcAccLog = new FcAccLogDTO();
        fcAccLog.setBizId(fcAccHistory.getId());
        fcAccLog.setOpType(opType);
        fcAccLog.setAccType(fcAccHistory.getAccType());
        fcAccLog.setBizMsg(bizMsg);
        fcAccLog.setAccount(fcAccHistory.getBlnctrAcc());
        fcAccLog.setBizType(InnerAccountConstant.BIZ_TYPE_CHANGE);
        fcAccLogService.saveLog(fcAccLog,loginName);
    }
    /**
     * 改户历史表计息账户处理
     * @param fcAccHistory
     * @param user
     */
    public void saveInterestAcc(FcAccHistoryVO fcAccHistory,ICurrentUser user) {
        //如果计息账号不为空,遍历账号根据科目设置到对应的字段中,
        //应收应付利息户:INTEREST_REC_PAY,利息收入支出户:INTEREST_IN_EXP
        if(!StringUtils.isBlank(fcAccHistory.getAccType()) && InnerAccountConstant.FCACC_INNER_ACCOUNT.equals(fcAccHistory.getAccType())){
            FcSubjectDTO subjectDto = fcSubjectService.findSubjectById(fcAccHistory.getSubjectId());
            if(!EmptyUtils.isEmpty(subjectDto) && !EmptyUtils.isEmpty(subjectDto.getSysSubjectCode())){
                if(!StringUtils.isBlank(fcAccHistory.getInterestAcc())){
                    String[] accArr = fcAccHistory.getInterestAcc().split(CommonConstant.SEMI_SPLIT);
                    for(int i = 0; i < accArr.length; i++){
                        FcAccHistoryVO fcAccHistoryVO = fcAccHistoryService.findFcAccHistoryVOByBlnctrAcc(accArr[i]);
                        //为空说明历史数据中没有,新增一条插入
                        if(EmptyUtils.isEmpty(fcAccHistoryVO)){
                        	com.bytter.system.api.vo.FcAccVO accVo = fcAccService.getFcAccVOByBlnctrAcc(accArr[i]);
                            if(!EmptyUtils.isEmpty(accVo)){
                                //改户的数据存储对象
                                FcAccHistoryVO entity= new FcAccHistoryVO();
                                FcAccVO acc = fcAccService.getFcAcc(accVo.getId());
                                BeanUtils.copyNotNullProperties(acc,entity);
                                entity.setFcaccId(accVo.getId());
                                entity.setId(null);
                                entity.setCreateBy(user.getLoginName());
                                entity.setCreateDate(DateUtils.getDate());
                                entity.setCheckBackReason(null);
                                //系统科目：应付利息-企业活期存款（2011）,应付利息-企业定期存款（2012）,应付利息-通知存款（2013）,应付利息-企业协定（2016）,
                                // 应收利息-银行活期存款（1032）,应付利息-银行借款（2014）,应收利息-企业放款（1031）,这几个科目放入INTEREST_REC_PAY字段中
                                if(SubjectConstant.SysSubject.INTEREST_PAYABLE_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.DEPOSIT_INTEREST_INCOME_REGULAR.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_NOTICE_DEPOSIT.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_COMP_AGREED.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_LOAN.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_LOAN.get().equals(subjectDto.getSysSubjectCode())){
                                    entity.setInterestRecPay(fcAccHistory.getBlnctrAcc());
                                }
                                //系统科目：利息支出-企业活期存款（5212）,利息支出—企业定期存款（5211）,利息支出-通知存款（5213）,利息支出-企业协定（5216）,
                                //利息收入-银行活期存款（5012）,利息支出-银行借款（5214）,利息收入-企业放款（5011）
                                if(SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.DEPOSIT_INTEREST_EXPENDITURE_REGULAR.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_EXPENSES_NOTICE_DEPOSIT.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.DISCOUNT_INTEREST_COMP_AGREED.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_LOAN.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_EXPENSES_LOAN.get().equals(subjectDto.getSysSubjectCode())){
                                    entity.setInterestInExp(fcAccHistory.getBlnctrAcc());
                                }
                                fcAccHistoryService.save(entity); // 改户时如果计息账号没有,将账户数据插入历史表
                            }
                        }else{
                            FcAccHistoryVO history = fcAccHistoryService.getFcAccHistoryById(fcAccHistoryVO.getId());
                            if(!EmptyUtils.isEmpty(history)){
                                //系统科目：应付利息-企业活期存款（2011）,应付利息-企业定期存款（2012）,应付利息-通知存款（2013）,应付利息-企业协定（2016）,
                                // 应收利息-银行活期存款（1032）,应付利息-银行借款（2014）,应收利息-企业放款（1031）,这几个科目放入INTEREST_REC_PAY字段中
                                if(SubjectConstant.SysSubject.INTEREST_PAYABLE_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.DEPOSIT_INTEREST_INCOME_REGULAR.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_NOTICE_DEPOSIT.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_COMP_AGREED.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_LOAN.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_LOAN.get().equals(subjectDto.getSysSubjectCode())){
                                    history.setInterestRecPay(fcAccHistory.getBlnctrAcc());
                                }
                                //系统科目：利息支出-企业活期存款（5212）,利息支出—企业定期存款（5211）,利息支出-通知存款（5213）,利息支出-企业协定（5216）,
                                //利息收入-银行活期存款（5012）,利息支出-银行借款（5214）,利息收入-企业放款（5011）
                                if(SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.DEPOSIT_INTEREST_EXPENDITURE_REGULAR.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_EXPENSES_NOTICE_DEPOSIT.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.DISCOUNT_INTEREST_COMP_AGREED.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_LOAN.get().equals(subjectDto.getSysSubjectCode())
                                        || SubjectConstant.SysSubject.INTEREST_EXPENSES_LOAN.get().equals(subjectDto.getSysSubjectCode())){
                                    history.setInterestInExp(fcAccHistory.getBlnctrAcc());
                                }
                            }
                            fcAccHistoryService.save(history);
                        }
                    }
                }
            }
        }
    }
    /**
     * 改户通过计息账户处理
     * @param fcAccHistory
     */
    public void saveFcAccInterestAcc(FcAccHistoryVO fcAccHistory) {
    	FcAccVO accVO = new FcAccVO();
        accVO.setBlnctrAcc(fcAccHistory.getBlnctrAcc());
        List<FcAccVO> accList = fcAccService.getAccListByExpPay(accVO);
        boolean isPay = false;
        boolean isExp = false;
        //如果计息账号不为空,遍历账号根据科目设置到对应的字段中,
        //应收应付利息户:INTEREST_REC_PAY,利息收入支出户:INTEREST_IN_EXP
        if(!StringUtils.isBlank(fcAccHistory.getAccType()) && InnerAccountConstant.FCACC_INNER_ACCOUNT.equals(fcAccHistory.getAccType())) {
            FcSubjectDTO subjectDto = fcSubjectService.findSubjectById(fcAccHistory.getSubjectId());
            if (!EmptyUtils.isEmpty(subjectDto) && !EmptyUtils.isEmpty(subjectDto.getSysSubjectCode())) {
                //系统科目：应付利息-企业活期存款（2011）,应付利息-企业定期存款（2012）,应付利息-通知存款（2013）,应付利息-企业协定（2016）,
                // 应收利息-银行活期存款（1032）,应付利息-银行借款（2014）,应收利息-企业放款（1031）,这几个科目放入INTEREST_REC_PAY字段中
                if(SubjectConstant.SysSubject.INTEREST_PAYABLE_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                        || SubjectConstant.SysSubject.DEPOSIT_INTEREST_INCOME_REGULAR.get().equals(subjectDto.getSysSubjectCode())
                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_NOTICE_DEPOSIT.get().equals(subjectDto.getSysSubjectCode())
                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_COMP_AGREED.get().equals(subjectDto.getSysSubjectCode())
                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_LOAN.get().equals(subjectDto.getSysSubjectCode())
                        || SubjectConstant.SysSubject.INTEREST_PAYABLE_LOAN.get().equals(subjectDto.getSysSubjectCode())){
                    isPay = true;
                }
                //系统科目：利息支出-企业活期存款（5212）,利息支出—企业定期存款（5211）,利息支出-通知存款（5213）,利息支出-企业协定（5216）,
                //利息收入-银行活期存款（5012）,利息支出-银行借款（5214）,利息收入-企业放款（5011）
                if(SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                        || SubjectConstant.SysSubject.DEPOSIT_INTEREST_EXPENDITURE_REGULAR.get().equals(subjectDto.getSysSubjectCode())
                        || SubjectConstant.SysSubject.INTEREST_EXPENSES_NOTICE_DEPOSIT.get().equals(subjectDto.getSysSubjectCode())
                        || SubjectConstant.SysSubject.DISCOUNT_INTEREST_COMP_AGREED.get().equals(subjectDto.getSysSubjectCode())
                        || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                        || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_LOAN.get().equals(subjectDto.getSysSubjectCode())
                        || SubjectConstant.SysSubject.INTEREST_EXPENSES_LOAN.get().equals(subjectDto.getSysSubjectCode())){
                    isExp = true;
                }
            }
        }
        for (com.bytter.system.api.vo.FcAccVO acc : accList){
            FcAccVO entity = fcAccService.getFcAcc(acc.getId());
            if(isPay){
                entity.setInterestRecPay("");
            }
            if(isExp){
                entity.setInterestInExp("");
            }
            fcAccService.merge(entity);
        }
        FcAccHistoryVO fcAccHistoryVO = new FcAccHistoryVO();
        fcAccHistoryVO.setBlnctrAcc(fcAccHistory.getBlnctrAcc());
        List<FcAccHistoryVO> accListByExpPay = fcAccHistoryService.getAccListByExpPay(fcAccHistoryVO);
        for (FcAccHistoryVO hisVo : accListByExpPay){
        	com.bytter.system.api.vo.FcAccVO vo = fcAccService.findFcAccById(hisVo.getFcaccId());
            if(!EmptyUtils.isEmpty(vo)){
                FcAccVO  entity = fcAccService.getFcAcc(hisVo.getFcaccId());
                entity.setInterestRecPay(hisVo.getInterestRecPay());
                entity.setInterestInExp(hisVo.getInterestInExp());
                fcAccService.merge(entity);
            }
        }
    }
    /**
     * 根据账号信息查找计息账号列表
     * @param vo
     * @return
     */
    public List<FcAccHistoryVO> getAccListByExpPay(FcAccHistoryVO vo) {
        List<FcAccHistoryVO> accList = fcAccHistoryService.getAccListByExpPay(vo);
        return accList;
    }

}
