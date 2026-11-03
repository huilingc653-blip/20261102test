package com.bytter.biz.inneraccount.service;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.api.service.*;
import com.bytter.biz.inneraccount.util.InnerAccountUtils;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.inneraccount.vo.v1.FcAccCommonVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.FcAccListVO;
import com.bytter.system.api.vo.FcAccPlusVO;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户信息表
 * @date 2017-05-25 19:36:12
 */
@Service("fcAccManageService")
@Transactional
public class FcAccManageService extends AbstractService implements ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @DubboReference(check = false)
	private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IFcAccuBalanceService fcAccuBalanceService;
    @DubboReference(check = false)
    private IFcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
    private IFcOutAccBalanceService fcOutAccBalanceService;
    @DubboReference(check = false)
    private IFcAccBalanceService fcAccBalanceService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcDayCloseService fcDayCloseService;
    @DubboReference(check = false)
    private IFcAccLogService fcAccLogService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IAccountGrantService iAccountGrantService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IFcSubjectService fcSubjectService;
    @DubboReference(check = false)
    private IFcOnlineLedgerService ledgerService;
    @DubboReference(check = false)
    private IFcCorpPoolService fcCorpPoolService;
    /**
     * 查看复核公用
     * @param id
     * @param model
     */
    public void search(String id, Model model){
        FcAccVO fcAcc = fcAccService.findFcAccById(id);
        int accAccNoRule = configService.getConfig("fcs","accAccnoRule").toInteger(1).intValue();
        if(AllocationConstant.POOL_SIGN_1.equals(fcAcc.getPoolSign())){
            fcAcc = fcAccService.findFcAccByIdForPool(id);
            fcAcc.setNetId(fcAcc.getNetPoolId());
            fcAcc.setSubjectId(fcAcc.getSubjectPoolId());
            accAccNoRule = configService.getConfig("pool", "poolAccnoRule").toInteger(1).intValue();
            FcCorpPoolDTO fcCorpPoolDTO = fcCorpPoolService.getById(fcAcc.getNetPoolId());
            model.addAttribute("netCode", fcCorpPoolDTO.getPoolCode());
            model.addAttribute("corpCode", (StringUtils.isEmpty(fcAcc.getCorpId()) || corpService.get(fcAcc.getCorpId())==null)?"":corpService.get(fcAcc.getCorpId()).getCode());
        }else{
            model.addAttribute("netCode", (StringUtils.isEmpty(fcAcc.getNetId()) || corpService.get(fcAcc.getNetId())==null)?"": corpService.get(fcAcc.getNetId()).getCode());
            model.addAttribute("corpCode", (StringUtils.isEmpty(fcAcc.getCorpId()) || corpService.get(fcAcc.getCorpId())==null)?"":corpService.get(fcAcc.getCorpId()).getCode());
            model.addAttribute("unionNetCode", (StringUtils.isEmpty(fcAcc.getUnionNet()) || corpService.get(fcAcc.getUnionNet())==null)?"":corpService.get(fcAcc.getUnionNet()).getCode());
        }
        fcAcc.setAccAccNoRule(accAccNoRule);
        List<FcAccVO> accListByExpPay = fcAccService.getAccListByExpPay(fcAcc);
        String interestAccTemp = "";
        if(!EmptyUtils.isEmptyList(accListByExpPay)){
            for(int i = 0; i < accListByExpPay.size(); i++){
                if(i == 0){
                    interestAccTemp = accListByExpPay.get(i).getBlnctrAcc();
                }else{
                    interestAccTemp += ";" + accListByExpPay.get(i).getBlnctrAcc();
                }
            }
        }
        fcAcc.setInterestAcc(interestAccTemp);
        model.addAttribute("fcAccPage", fcAcc);
        //顺序号生成模式
        int seqGeneratingMode = configService.getConfig("fcs","seqGeneratingMode").toInteger(1).intValue();
        model.addAttribute("seqNumPage", seqGeneratingMode);
        //根据资金池标志生成内部账号生成法则
        String reVal = fcAccService.getRuleVaql(AllocationConstant.POOL_SIGN_0);
        int relen = reVal.length();
        model.addAttribute("relen", relen);
    }
    public void search(String id, FcAccCommonVO fcAccCommonVo){
        FcAccVO fcAcc = fcAccService.findFcAccById(id);
        int accAccNoRule = configService.getConfig("fcs","accAccnoRule").toInteger(1).intValue();
        if(AllocationConstant.POOL_SIGN_1.equals(fcAcc.getPoolSign())){
            fcAcc = fcAccService.findFcAccByIdForPool(id);
            fcAcc.setNetId(fcAcc.getNetPoolId());
            fcAcc.setSubjectId(fcAcc.getSubjectPoolId());
            accAccNoRule = configService.getConfig("pool", "poolAccnoRule").toInteger(1).intValue();
            FcCorpPoolDTO fcCorpPoolDTO = fcCorpPoolService.getById(fcAcc.getNetPoolId());
            fcAccCommonVo.setNetCode(fcCorpPoolDTO.getPoolCode());
            fcAccCommonVo.setCorpCode((StringUtils.isEmpty(fcAcc.getCorpId()) || corpService.get(fcAcc.getCorpId())==null)?"":corpService.get(fcAcc.getCorpId()).getCode());

        }else{
            fcAccCommonVo.setNetCode((StringUtils.isEmpty(fcAcc.getNetId()) || corpService.get(fcAcc.getNetId())==null)?"": corpService.get(fcAcc.getNetId()).getCode());
            fcAccCommonVo.setCorpCode((StringUtils.isEmpty(fcAcc.getCorpId()) || corpService.get(fcAcc.getCorpId())==null)?"":corpService.get(fcAcc.getCorpId()).getCode());
            fcAccCommonVo.setUnionNetCode((StringUtils.isEmpty(fcAcc.getUnionNet()) || corpService.get(fcAcc.getUnionNet())==null)?"":corpService.get(fcAcc.getUnionNet()).getCode());
            if(StringUtils.isNotEmpty(fcAcc.getUnionNet())){
                fcAccCommonVo.setUnionNetName(corpService.get(fcAcc.getUnionNet()).getName());
            }
        }
        fcAcc.setAccAccNoRule(accAccNoRule);
        List<FcAccVO> accListByExpPay = fcAccService.getAccListByExpPay(fcAcc);
        String interestAccTemp = "";
        if(!EmptyUtils.isEmptyList(accListByExpPay)){
            for(int i = 0; i < accListByExpPay.size(); i++){
                if(i == 0){
                    interestAccTemp = accListByExpPay.get(i).getBlnctrAcc();
                }else{
                    interestAccTemp += ";" + accListByExpPay.get(i).getBlnctrAcc();
                }
            }
        }
        fcAcc.setInterestAcc(interestAccTemp);
        if(StringUtils.isNotEmpty(fcAcc.getReferenceRateType())){
            fcAcc.setReferenceRateTypeName(dictService.get("reference.RateType", fcAcc.getReferenceRateType()));
        }
        setDictName(fcAcc);
        fcAccCommonVo.setFcAccPage(fcAcc);
        //顺序号生成模式
        int seqGeneratingMode = configService.getConfig("fcs","seqGeneratingMode").toInteger(1).intValue();
        fcAccCommonVo.setSeqNumModel(seqGeneratingMode);
        //根据资金池标志生成内部账号生成法则
        String reVal = fcAccService.getRuleVaql(AllocationConstant.POOL_SIGN_0);
        int relen = reVal.length();
        fcAccCommonVo.setRenlen(relen);
        //前后端分离后端生成顺序号  之前逻辑根据长度 以及 生成模式  取截取原内部账号的位数
        String seqNumFromBlnctrAcc = extractSeqNumFromBlnctrAcc(accAccNoRule, fcAcc.getBlnctrAcc());
        fcAccCommonVo.getFcAccPage().setSequenceNumber(seqNumFromBlnctrAcc);
    }

    private void setDictName(FcAccVO fcAcc) {
        if(StringUtils.isNotEmpty(fcAcc.getReferenceRateType())){
            fcAcc.setReferenceRateTypeName(dictService.get("reference.RateType", fcAcc.getReferenceRateType()));
        }
        if (StringUtils.isNotEmpty(fcAcc.getInterestSign())){
            fcAcc.setInterestSignName(dictService.get("fcAcc.interestSign", fcAcc.getInterestSign()));
        }
        if (StringUtils.isNotEmpty(fcAcc.getPredrawSign())){
            fcAcc.setPredrawSignName(dictService.get("Admin.PredrawSign", fcAcc.getPredrawSign()));
        }
        if (StringUtils.isNotEmpty(fcAcc.getIfCompare())){
            fcAcc.setIfCompareName(dictService.get("sf_yn", fcAcc.getIfCompare()));
        }
        if (StringUtils.isNotEmpty(fcAcc.getOverdraftSign())){
            fcAcc.setOverdraftSignName(dictService.get("fcAcc.overdraftSign", fcAcc.getOverdraftSign()));
        }
        if (StringUtils.isNotEmpty(fcAcc.getNotifyType())){
            fcAcc.setNotifyTypeName(dictService.get("choBankFinance.noticeDayLimit", fcAcc.getNotifyType()));
        }
        if (StringUtils.isNotEmpty(fcAcc.getEndUnit())){
            fcAcc.setEndUnitName(dictService.get("fcAcc.endUnit", fcAcc.getEndUnit()));
        }
    }

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(FcAccVO entity, ICurrentUser user) {
        fcAccService.delete(entity.getId());
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
    public void delete(String id) {
        fcAccService.delete(id);
    }
    public FcAccVO getFcAcc(String id) {
        return fcAccService.getFcAcc(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(FcAccVO entity) {
        fcAccService.save(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcAccVO entity) {
        fcAccService.update(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public String saveOrUpdate(FcAccVO entity) {
        return fcAccService.saveOrUpdate(entity);
    }
    /**
     * 启用日期大于已日结日期
     *
     * @param netId
     * @return
     */
    public Date getValidDate(String netId) {
        Date tallyDate = fcDayCloseService.getDayCloseFinalDate(netId);
        if (tallyDate != null) {
            Date accStartDate = DateUtils.addDay(tallyDate, 1);
            return accStartDate;
        }
        return DateUtils.getDateYMD();
    }
    /**
     * 启用日期大于已日结日期
     *
     * @param netId
     * @return
     */
    public Date getValidDateForPool(String netId) {
        Date tallyDate = fcDayCloseService.getPoolDayCloseDate(netId);
        if (tallyDate != null) {
            Date accStartDate = DateUtils.addDay(tallyDate, 1);
            return accStartDate;
        }
        return DateUtils.getDateYMD();
    }
    /**
     * 手动开户更新
     *
     * @param entity
     */
    public void modify(FcAccVO entity, ICurrentUser user) {
        Date tallyDate = null;
        int accAutoCheck = 0;
        if (AllocationConstant.POOL_SIGN_1.equals(entity.getPoolSign())) {
            tallyDate = fcDayCloseService.getPoolDayCloseDate(entity.getNetId());
            accAutoCheck = configService.getConfig("pool", "accAutoCheck").toInteger(0).intValue();
            //启用日期必须大于当前网点已日结日期
            dateCompareForPool(entity.getAccStartDate(), tallyDate);
        } else {
            tallyDate = fcDayCloseService.getDayCloseFinalDate(entity.getNetId());
            accAutoCheck = configService.getConfig("fcs", "accAutoCheck").toInteger(0).intValue();
            //启用日期必须大于当前网点已日结日期
            dateCompare(entity.getAccStartDate(), tallyDate);
        }
        entity.setUpdateDate(DateUtils.getDate());
        entity.setUpdateBy(user.getLoginName());
        //完成通知提醒
        if (AllocationConstant.POOL_SIGN_1.equals(entity.getPoolSign())) {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_BACK, entity.getId(), user.getId());
        } else {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_BACK, entity.getId(), user.getId());
        }
        if (StringUtils.isEmpty(entity.getOverdraftSign())) {
            entity.setOverdraftSign(InnerAccountConstant.OVERDRAFT_SIGN_N);
        }
        //先删除计息账号
        this.delInterestAcc(entity);
        if (InnerAccountConstant.ACC_STATE_TEMP.equals(entity.getAccState())) {
            //日志记录
            saveLog(entity, CommonConstant.SUBMIT_MODIFY, InnerAccountConstant.BIZ_TYPE_TEMP, user.getLoginName());
            this.saveOrUpdate(entity);
            //保存计息账号
            this.saveInterestAcc(entity);
        } else {
            fcAccService.saveOrUpdate(entity);
            //保存计息账号
            this.saveInterestAcc(entity);
            //日志记录
            saveLog(entity, CommonConstant.SUBMIT_MODIFY, InnerAccountConstant.BIZ_TYPE_SUBMIT, user.getLoginName());
            //刷新缓存
            //fcAccService.flush();
            if (accAutoCheck == 1) {
                checkAuto(entity, CommonConstant.SUBMIT_MODIFY, user.getLoginName());
            } else {
                //发送消息提醒，忽略自身
                if (AllocationConstant.POOL_SIGN_1.equals(entity.getPoolSign())) {
                    notificationService.sendIgnoreMySelf(entity.getId(), entity.getCorpId(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CHECK, entity, user.getId());
                } else {
                    notificationService.sendIgnoreMySelf(entity.getId(), entity.getCorpId(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CHECK, entity, user.getId());
                }
            }
        }
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
        List<FcAccVO> list = fcAccService.listByBlnctrAcc(blnctrAcc);
        if (!EmptyUtils.isEmptyList(list)) {
            if (CommonConstant.SUBMIT_ADD.equals(opType) && !list.isEmpty()) {
                flag = true;
            } else if (CommonConstant.SUBMIT_MODIFY.equals(opType)) {
                if (!EmptyUtils.isEmptyList(list)) {
                    for (FcAccVO acc : list) {
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
     * 手动开户新增
     *
     * @param fcAcc
     */
    public void add(FcAccVO fcAcc, ICurrentUser user) {
        this.saveOrUpdate(fcAcc, user, false);
    }
    /**
     * 自动开户
     *
     * @param fcAcc
     * @param autoCheck 为true 系统自动复核通过，false 根据[是否自动复核参数]控制 是否自动复核通过
     */
    public void saveOrUpdate(FcAccVO fcAcc, ICurrentUser user, boolean autoCheck) {
        Date tallyDate = null;
        int accAutoCheck = 0;
//        if (StringUtils.isNotBlank(fcAcc.getBankCode())){
//            fcAcc.setBankName(fcAcc.getBankAccName());
//        }
        if (AllocationConstant.POOL_SIGN_1.equals(fcAcc.getPoolSign())) {
            tallyDate = fcDayCloseService.getPoolDayCloseDate(fcAcc.getNetId());
            accAutoCheck = configService.getConfig("pool", "accAutoCheck").toInteger(0).intValue();
            //启用日期必须大于当前资金池已日结日期
            dateCompareForPool(DateUtils.getDateYMD(fcAcc.getAccStartDate()), tallyDate);
        } else {
            tallyDate = fcDayCloseService.getDayCloseFinalDate(fcAcc.getNetId());
            accAutoCheck = configService.getConfig("fcs", "accAutoCheck").toInteger(0).intValue();
            //启用日期必须大于当前网点已日结日期
            dateCompare(DateUtils.getDateYMD(fcAcc.getAccStartDate()), tallyDate);
        }
        if (StringUtils.isEmpty(fcAcc.getId())) {
            fcAcc.setId(null);
        }
        if (StringUtils.isEmpty(fcAcc.getOverdraftSign())) {
            fcAcc.setOverdraftSign(InnerAccountConstant.OVERDRAFT_SIGN_N);
        }
        fcAcc.setOperatorCode(user.getLoginName());
        fcAcc.setCreateBy(user.getId());
//        fcAcc.setCreateBy(user.getLoginName());
        fcAcc.setCreateDate(DateUtils.getDate());
        if (autoCheck) {//自动开户
            fcAcc.setAccState(InnerAccountConstant.ACC_STATE_NEW);
        }
        if (InnerAccountConstant.ACC_STATE_TEMP.equals(fcAcc.getAccState())) {
            this.saveOrUpdate(fcAcc);
            //保存计息账号
            this.saveInterestAcc(fcAcc);
            //日志记录
            saveLog(fcAcc, CommonConstant.SUBMIT_ADD, InnerAccountConstant.BIZ_TYPE_TEMP, user.getLoginName());
        } else {
            String fcAccId = this.saveOrUpdate(fcAcc);
            fcAcc.setId(fcAccId);
            //保存计息账号
            this.saveInterestAcc(fcAcc);
            //刷新缓存
            //fcAccService.flush();
            if (autoCheck) {//自动开户
                //日志记录
                saveLog(fcAcc, CommonConstant.SUBMIT_ADD, InnerAccountConstant.BIZ_TYPE_AUTO_SUBMIT, user.getLoginName());
                checkAuto(fcAcc, CommonConstant.SUBMIT_ADD, user.getLoginName());
            } else {
                //日志记录
                saveLog(fcAcc, CommonConstant.SUBMIT_ADD, InnerAccountConstant.BIZ_TYPE_SUBMIT, user.getLoginName());
                if (accAutoCheck == 1) {
                    checkAuto(fcAcc, CommonConstant.SUBMIT_ADD, user.getLoginName());
                } else {
                    if (AllocationConstant.POOL_SIGN_1.equals(fcAcc.getPoolSign())) {
                        notificationService.sendIgnoreMySelf(fcAcc.getId(), fcAcc.getCorpId(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CHECK, fcAcc, user.getId());
                    } else {
                        notificationService.sendIgnoreMySelf(fcAcc.getId(), fcAcc.getCorpId(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CHECK, fcAcc, user.getId());
                    }
                }
            }
        }
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
            accAccNoRule = configService.getConfig("pool", "poolAccnoRule").toInteger(1).intValue();
        } else {
            /**
             * 内部账号生成法则
             * 顺序号 ==网点+币别+科目+ 科目类别（acc_type）后从0开始的数据。
             * <option value="1" #if($config.fcs.accAccnoRule == 1)selected#end>网点+币别+科目+3位顺序号</option>
             * <option value="2" #if($config.fcs.accAccnoRule == 2)selected#end>网点+币别+科目+4位顺序号</option>
             * <option value="3" #if($config.fcs.accAccnoRule == 3)selected#end>网点+币别+科目+6位顺序号</option>
             * <option value="4" #if($config.fcs.accAccnoRule == 4)selected#end>网点+币别+科目(科目不足6位补齐)+4位顺序号</option>
             */
            accAccNoRule = configService.getConfig("fcs", "accAccnoRule").toInteger(1).intValue();
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
        List<FcAccVO> list = fcAccService.queryMultiAccAll(netId, curId, accType);
        if (list != null && list.size() > 0) {
            Integer[] val = new Integer[list.size()];
            for (int i = 0; i < list.size(); i++) {
                String blnctrAcc = list.get(i).getBlnctrAcc();
                if (blnctrAcc.trim().length() < relen) {
                    val[i] = Integer.MIN_VALUE;
                    continue;
                }
                String temp = blnctrAcc.substring(blnctrAcc.trim().length() - relen, blnctrAcc.length());
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
            List<FcAccVO> list = fcAccService.queryMultiAccAll(netId, curId, accType);
            if (list != null && list.size() > 0) {
                Integer[] val = new Integer[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    String blnctrAcc = list.get(i).getBlnctrAcc();
                    String temp = blnctrAcc.substring(blnctrAcc.trim().length() - relen, blnctrAcc.length());
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
        List<FcAccVO> list = fcAccService.queryMultiAccAll(netId, curId, accType);
        if (list != null && list.size() > 0) {
            Integer[] val = new Integer[list.size()];
            for (int i = 0; i < list.size(); i++) {
                String blnctrAcc = list.get(i).getBlnctrAcc();
                String temp = blnctrAcc.substring(blnctrAcc.trim().length() - relen, blnctrAcc.length());
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
        fcAccService.updateRate(blnctrAcc, colName, instrate);
    }
    /**
     * 根据账号查对象
     *
     * @param blnctrAcc
     * @return
     */
    public FcAccVO findFcAccByBlnctrAcc(String blnctrAcc) {
        FcAccVO fcAcc = fcAccService.findFcAccByBlnctrAcc(blnctrAcc);
        return fcAcc;
    }
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccVO findFcAccById(String id) {
        FcAccVO fcAcc = fcAccService.findFcAccById(id);
        return fcAcc;
    }
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccVO findFcAccByIdForPool(String id) {
        FcAccVO fcAccDTO = fcAccService.findFcAccByIdForPool(id);
        //获取联余额
        List<String> accList = new ArrayList<>();
        accList.add(fcAccDTO.getBlnctrAcc());
        List<FcOnlineLedgerDTO> ledgerList = ledgerService.getBalanceByAcc(accList);
        if(ledgerList!=null&&ledgerList.size()>1){
            Map<String, FcOnlineLedgerDTO> ledgerMap = ledgerList.stream().collect(Collectors.toMap(FcOnlineLedgerDTO::getAccount, Function.identity()));
            fcAccDTO.setFcBalance(ledgerMap.get(fcAccDTO.getBlnctrAcc()).getCheckBalance());
        }
        return fcAccDTO;
    }
    public List<FcAccVO> findFcAcc(FcAccVO fcAcc, String sql) {
        return fcAccService.findFcAcc(fcAcc, sql);
    }
    public List<FcAccVO> getFcAccList(FcAccVO fcAcc, String sql,Map<String,Object> params) {
        return fcAccService.getFcAcc(fcAcc, sql, params);
    }
    public FcAccVO getFcAcc(FcAccVO fcAcc, String sql,Map<String,Object> params) {
        List<FcAccVO> list= fcAccService.getFcAcc(fcAcc, sql, params);
        if(list.size()==1){
            return list.get(0);
        }
        return null;
    }
    /**
     * 自动开户复核通过
     *
     * @param fcAcc
     * @param type
     */
    public void checkAuto(FcAccVO fcAcc, String type, String loginName) {
        FcAccVO acc = fcAccService.findFcAccByBlnctrAcc(fcAcc.getBlnctrAcc());
        acc.setCheckerCode(loginName);
        acc.setCheckTime(DateUtils.getDate());
        acc.setCheckBackReason(fcAcc.getCheckBackReason());
        check(acc, InnerAccountConstant.ACC_STATE_NORMAL, acc.getAccState());
        //插入联机余额
        com.bytter.biz.finacial.api.vo.FcAccVO vo = new com.bytter.biz.finacial.api.vo.FcAccVO();
        BeanUtils.copyProperties(acc,vo);
        fcOnlineLedgerService.insertFclOnlineLedger(vo, loginName);
        //初始化账户余额
        Date balanceDate = InnerAccountUtils.getBalanceDate(DateUtils.getDateYMD(acc
                .getAccStartDate()));
        if (acc.getAccType().equals(InnerAccountConstant.FCACC_OFFBALANCE)) {// 表外
            fcOutAccBalanceService.initAccountBalance(balanceDate, acc.getBlnctrAcc(), acc.getAccName(), acc.getCurId(), loginName);
        } else {
            fcAccBalanceService.initAccountBalance(balanceDate, acc.getBlnctrAcc(), acc.getAccName(), acc.getCurId(), loginName);
        }
        //初始化账户积数余额
        initAccuBalance(balanceDate, acc.getBlnctrAcc(), acc.getCurId(), acc.getInterestSign(), loginName);
        //新增账户自动授权
        AccountGrantDTO grantDTO = new AccountGrantDTO();
        grantDTO.setAccountId(acc.getId());
        //权限给申请用户
        grantDTO.setUserId(fcAcc.getCreateBy());
        grantDTO.setCorpId(fcAcc.getCorpId());
        if (AllocationConstant.POOL_SIGN_1.equals(fcAcc.getPoolSign())) {
            grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.POOLINNER_BANK_TYPE));
        } else {
            grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.INNER_BANK_TYPE));
        }
        iAccountGrantService.saveAccountGrant(grantDTO);
        //刷新缓存
        //fcAccService.flush();
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
    public int check(FcAccVO fcAcc, String accState, String oldAccState) {
        return fcAccService.check(fcAcc, accState, oldAccState);
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
            fcAccuBalanceService.initAccuBalance(balanceDate, account, curId, BigDecimal.ZERO, loginName);
        }
    }
    /**
     * 保存开户日志
     *
     * @param acc
     * @param opType 　操作类型
     * @param bizMsg 　业务信息
     */
    private void saveLog(FcAccVO acc, String opType, String bizMsg, String loginName) {
        FcAccLogDTO fcAccLog = new FcAccLogDTO();
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
        return fcAccService.isOtherAccountQuote(blnctrAcc);
    }
    public boolean isNotReceivedInterest(String blnctrAcc) {
        return fcAccService.isExistInterestPayable(blnctrAcc);
    }
    /**
     * 开户复核通过或者打回
     *
     * @param fcAcc
     * @param user
     */
    public void check(FcAccVO fcAcc, ICurrentUser user) {
        FcAccVO acc = fcAccService.getFcAcc(fcAcc.getId());
        if (!InnerAccountConstant.ACC_STATE_NEW.equals(acc.getAccState())) {
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS, fcAcc.getBlnctrAcc());
        }
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
            com.bytter.biz.finacial.api.vo.FcAccVO vo = new com.bytter.biz.finacial.api.vo.FcAccVO();
            BeanUtils.copyProperties(acc,vo);
            fcOnlineLedgerService.insertFclOnlineLedger(vo, user.getLoginName());
            //初始化账户余额
            Date balanceDate = InnerAccountUtils.getBalanceDate(DateUtils.getDateYMD(acc.getAccStartDate()));
            if (acc.getAccType().equals(InnerAccountConstant.FCACC_OFFBALANCE)) {// 表外
                fcOutAccBalanceService.initAccountBalance(balanceDate, acc.getBlnctrAcc(), acc.getAccName(), acc.getCurId(), user.getLoginName());
            } else {
                fcAccBalanceService.initAccountBalance(balanceDate, acc.getBlnctrAcc(), acc.getAccName(), acc.getCurId(), user.getLoginName());
            }
            //初始化账户积数余额
            initAccuBalance(balanceDate, acc.getBlnctrAcc(), acc.getCurId(), acc.getInterestSign(), user.getLoginName());
            //新增账户自动授权
            AccountGrantDTO grantDTO = new AccountGrantDTO();
            grantDTO.setAccountId(acc.getId());
            //权限给申请用户
            UserDTO createBy = userService.getByUserId(acc.getCreateBy());
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
        //fcAccService.flush();
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
        return fcAccService.getAccountByParams(curId, netId, corpId, subjectId, null, null, null);
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
        return fcAccService.getAccountByParams(accTypes, curId, netId, corpId);
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
    public List<FcAccPlusVO> listAccountByParams(String curId, String netId, String corpId, String subjectId, String endUnit) {
        return fcAccService.getAccountByParams(curId, netId, corpId, subjectId, endUnit, null, null, null, false);
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
    public List<FcAccPlusVO> listAccountByParamsAndGrant(String curId, String netId, String corpId, String subjectId, String sysSubjectCode, List<String> accs, String bankTypeId) {
        return fcAccService.getAccountByParamsAndGrant(curId, netId, corpId, subjectId, sysSubjectCode, accs, bankTypeId);
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
        return fcAccService.getAccountByParamsAndGrantForPool(curId, netId, corpId, subjectId, sysSubjectCode, accs, bankCode);
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
        return fcAccService.getAccountByParams(curId, netId, corpId, subjectId, null, sysSubjectCode, null);
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
    public List<FcAccPlusVO> listAccountByMoreParamsForPool(String curId, String netId, String corpId, String subjectId, String sysSubjectCode) {
        return fcAccService.getAccountByParamsForPool(curId, netId, corpId, subjectId, null, sysSubjectCode, null);
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
        return fcAccService.getAccountByParams(curId, netId, corpId, subjectId, null, null, bankCode);
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
        List<FcAccPlusVO> fcAccPlusVOS = fcAccService.getAccountByParams(null, null, null, null, null, null, null, null, true);
        List<String> accounts = fcAccPlusVOS.stream().map(FcAccPlusVO::getBlnctrAcc).collect(Collectors.toList());
        List<FcOnlineLedgerDTO> fcOnlineLedgerDTOS = fcOnlineLedgerService.getBalanceByAcc(accounts);
        if (CollectionUtils.isNotEmpty(fcOnlineLedgerDTOS)){
            Map<String, BigDecimal> map = fcOnlineLedgerDTOS.stream().collect(Collectors.toMap(FcOnlineLedgerDTO::getAccount, FcOnlineLedgerDTO::getCheckBalance,(a,b) -> a));
            fcAccPlusVOS.forEach(a -> {
                a.setFcBalance(map.get(a.getBlnctrAcc()));
            });
        }
        return fcAccPlusVOS;
    }
    /**
     * 更改账号状态
     *
     * @param blnctrAcc
     * @param state
     */
    public void changeState(String blnctrAcc, String state) {
        FcAccVO fcAcc = this.findFcAccByBlnctrAcc(blnctrAcc);
        fcAcc.setAccState(state);
        fcAccService.update(fcAcc);
    }
    /**
     * 内部账号查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTableAll(DataTablesInput dataTablesInput, ICurrentUser user) {
        Map<String, Object> params = new HashMap();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_ALLQUERY, user.getId()), "t.corp_Id", params);
        DataTablesOutput output = fcAccService.getDataTableAll(dataTablesInput, corpAuthSql, params);
        setVoDictData(output);
        return output;
    }
    private void setVoDictData(DataTablesOutput output) {
        List<FcAccListVO> list = output.getData();
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("InnerAccount.AccType", "inneraccount.fcStatus", "Admin.BalanceWay");
        for (FcAccListVO vo : list) {
            Optional.ofNullable(vo.getAccType()).ifPresent(accType -> {
                Map<String, DictDTO> dictDtoMap = listDictMap.get("InnerAccount.AccType");
                Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(accType)))
                        .ifPresent(dict -> vo.setAccTypeName(dict.getName()));
            });
            Optional.ofNullable(vo.getAccState()).ifPresent(accState -> {
                Map<String, DictDTO> dictDtoMap = listDictMap.get("inneraccount.fcStatus");
                Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(accState)))
                        .ifPresent(dict -> vo.setAccStateName(dict.getName()));
            });
            Optional.ofNullable(vo.getBalanceWay()).ifPresent(balanceWay -> {
                Map<String, DictDTO> dictDtoMap = listDictMap.get("Admin.BalanceWay");
                Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(balanceWay.toString())))
                        .ifPresent(dict -> vo.setBalanceWayName(dict.getName()));
            });
            // vo.setAccTypeName(dictService.get("InnerAccount.AccType", vo.getAccType()));
            // vo.setAccStateName(dictService.get("inneraccount.fcStatus", vo.getAccState()));
            // vo.setBalanceWayName(dictService.get("Admin.BalanceWay", vo.getBalanceWay()));
        }
    }
    /**
     * 内部账号查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTableAllForPool(DataTablesInput dataTablesInput, ICurrentUser user) {
        Map<String, Object> params = new HashMap();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_ALLQUERY, user.getId()), "t.corp_Id", params);
        DataTablesOutput output = fcAccService.getDataTableAllForPool(dataTablesInput, corpAuthSql, params);
        setVoDictData(output);
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
        String corpAuthSql = SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_QUERY, user.getId()), "t.corp_Id", params);
        DataTablesOutput output = fcAccService.getDataTableAllNormal(dataTablesInput, corpAuthSql, params);
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
    public DataTablesOutput getDataTableAllNormalSYS(DataTablesInput dataTablesInput, ICurrentUser user) {
        Map<String, Object> params = new HashMap();
        StringBuilder corpAuthSql = new StringBuilder();
        corpAuthSql.append(SqlUtil.getInSqlByParams(corpService.getUserCorpIds(SysResourceConstant.SYS_ACCOUNTGRANT_INNERACCOUNT, user.getId()),
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
        if (poolSignFlag) {
            String poolId = (String) dataTablesInput.getFormdata().get("eqs_corpId");
            String poolType = (String) dataTablesInput.getFormdata().get("poolType");
            if (!EmptyUtils.isBlank(poolId) && CorpConstant.CORP_TYPE_CODE_NET.equals(Integer.parseInt(poolType))) {
                dataTablesInput.getFormdata().put("eqs_netId", poolId);
                dataTablesInput.getFormdata().remove("eqs_corpId");
            }
            dataTablesInput.getFormdata().remove("poolType");
            return fcAccService.getPoolDataTableAllNormal(dataTablesInput, corpAuthSql.toString(), params);
        } else {
            return fcAccService.getDataTableAllNormal(dataTablesInput, corpAuthSql.toString(), params);
        }
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
        return fcAccService.transferOutAcc(blnctrAcc, netId);
    }
    /**
     * 获取FcAccVO
     *
     * @param account
     * @return
     */
    public FcAccVO getFcAccVOByBlnctrAcc(String account) {
        return fcAccService.getFcAccVOByBlnctrAcc(account);
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
        return fcAccService.getFcAccVOByParams(netId, corpId, subjectId, curId, null);
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
        List<FcAccDTO> list = fcAccService.listFcAccInfo(ids, accState);
        return list;
    }
    public List<FcAccDTO> listFcAccInfoForPool(List<String> ids, List<String> accState) {
        if (EmptyUtils.isEmptyList(ids)) {
            return null;
        }
        List<FcAccDTO> list = fcAccService.listFcAccInfoForPool(ids, accState);
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
        return fcAccService.getFcAccBySubjectCode(chargeAccount, netId, subjectCode);
    }
    /**
     * 功能介绍  ： 查询相关科目账号。<br>
     * <b>日期：</b>2017年7月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> getFcAccByCorpSubjectCode(String netId, String corpId, String subjectCode) {
        return fcAccService.getFcAccByCorpSubjectCode(netId, corpId, subjectCode);
    }
    /**
     * 功能介绍 : 判断账户是否同币别。<br>
     * <b>日期：</b>2017年7月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public boolean getFcAccCurCode(String blnctrAcc, String chargeAccount) {
        return fcAccService.getFcAccCurCode(blnctrAcc, chargeAccount);
    }
    /**
     * 功能介绍  : 查询账户相关信息。<br>
     * <b>日期：</b>2017年7月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public FcAccDTO getAccDTOByAccount(String account) {
        FcAccVO fcAcc = fcAccService.getFcAccVOByBlnctrAcc(account);
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
        return fcAccService.listFcAccInFoByCorpIds(corpIds, accState);
    }
    /**
     * 根据单位ID列表查询资金池账号列表
     *
     * @param corpIds
     * @param accState
     * @return
     */
    public List<FcAccDTO> listFcAccPoolByCorpIds(List<String> corpIds, String accState, String poolSign) {
        if (corpIds == null) {
            return null;
        }
        return fcAccService.listFcAccPoolByCorpIds(corpIds, accState, poolSign);
    }
    /**
     * 查找放款账号对应的账号
     *
     * @param accForDeduct
     * @return
     */
    public List<FcAccVO> getFcAccByAccForDeduct(String accForDeduct) {
        return fcAccService.listBy("accForDeduct", accForDeduct);
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
        return fcAccService.findBySysSubjectId(sysSubjectId, netIds, corpIds, curId);
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
        return fcAccService.findBySysSubjectIdFilterBankCode(subjectId, sysSubjectCode, netIds, corpIds, curId);
    }
    /**
     * 功能介绍 : 开户未复核通过的账户。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> findNoCheckVFcAcc(FcDayCloseDTO dayClose) {
        return fcAccService.findNoCheckVFcAcc(dayClose);
    }
    /**
     * 功能介绍 : 查询账户集合。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> getAccSet(List<String> accList) {
        return fcAccService.getAccSet(accList);
    }
    /**
     * 功能介绍 : 查询表外账户集合。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccDTO> getOutAccTypeAccs() {
        return fcAccService.getOutAccTypeAccs();
    }
    /**
     * 功能介绍 : 获取可记账内部账号(非表外)。<br>
     * <b>日期：</b>2017年11月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccPlusVO> getTallyAccTypeAccs(List<String> ids) {
        return fcAccService.getTallyAccTypeAccs(ids);
    }
    /**
     * 根据科目 判断是否存在使用的账号
     *
     * @param subujectId
     * @return
     */
    public boolean isExsits(String subujectId) {
        return fcAccService.isExsits(subujectId);
    }
    /**
     * 根据内部账号搜索是否有关联的银行账号存在
     *
     * @param blnctrAcc
     * @return
     */
    public boolean isExsitsForValidBankAccByInnerAcc(String blnctrAcc) {
        return fcAccService.isExsitsForValidBankAccByInnerAcc(blnctrAcc);
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
        List<FcAccVO> accListByExpPay = fcAccService.getAccListByExpPay(vo);
        FcAccVO acc = fcAccService.findFcAccByBlnctrAcc(blnctrAcc);
        boolean flag = false;
        if (!EmptyUtils.isEmptyList(accListByExpPay) ||
                (!EmptyUtils.isEmpty(blnctrAcc) && (!EmptyUtils.isEmpty(acc.getInterestInExp()) || !EmptyUtils.isEmpty(acc.getInterestRecPay())))) {
            flag = true;
        }
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
        return fcAccService.getSubAccFcAccByParentBankAcc(parentBankAccId, curId, ids);
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
        FcAccVO fcAcc1 = fcAccService.getFcAccVOByBlnctrAcc(accInfo1);
        FcAccVO fcAcc2 = fcAccService.getFcAccVOByBlnctrAcc(accInfo2);
        if (EmptyUtils.isEmpty(fcAcc1) || EmptyUtils.isEmpty(fcAcc2)) {
            return false;
        }
        return fcAcc1.getNetId().equalsIgnoreCase(fcAcc1.getNetId());
    }
    public DataTablesOutput getFcAcc(DataTablesInput input) {
        return fcAccService.getDataTableAllNormal(input, null, null);
    }
    /**
     * 功能介绍  : 查询资金池账户相关信息。<br>
     * <b>日期：</b>2019年5月9日<br>
     *
     * @version 1.0
     * @author wangzx
     */
    public FcAccDTO getPoolAccDTOByAccount(String account) {
        FcAccVO fcAcc = fcAccService.getPoolFcAccVOByBlnctrAcc(account);
        if (EmptyUtils.isEmpty(fcAcc)) {
            return null;
        }
        FcAccDTO fcAccDTO = new FcAccDTO();
        BeanUtils.copyNotNullProperties(fcAcc, fcAccDTO);
        return fcAccDTO;
    }
    /**
     * <b>功能介绍：</b>获取网点下计息的资金池账号<br>
     * <b>日期：</b>2019年5月21日<br>
     * <b>作者：</b>liyh<br>
     *
     * @version 1.0
     */
    public List<FcAccDTO> getNetAccByPoolSign(String netId, String accType, String interestSign, String poolSign) {
        return fcAccService.getNetAccByPoolSign(netId, accType, interestSign, poolSign);
    }
    /**
     * <b>功能介绍：</b>查询资金池科目的资金池账号<br>
     * <b>日期：</b>2019年5月22日<br>
     * <b>作者：</b>liyh<br>
     *
     * @version 1.0
     */
    public List<FcAccDTO> getNetPoolAccBySubject(String netId, String curId, String subjectId, String poolSign) {
        return fcAccService.getNetPoolAccBySubject(netId, curId, subjectId, poolSign);
    }
    /**
     * 根据资金池单位查询账号(开户待复核、开户复核打回、改户待复核、改户复核打回、销户待复核、销户复核打回)
     *
     * @param netId    单位id
     * @param corpId   单位id
     * @param isFilter 是否过滤状态 true过滤，flase 不过滤
     * @return
     */
    public List<FcAccDTO> listFcAccPoolByCorpId(String netId, String corpId, boolean isFilter) {
        List<FcAccDTO> dto = fcAccService.listFcAccPoolByCorpId(netId, corpId, isFilter);
        if (EmptyUtils.isEmptyList(dto)) {
            return null;
        }
        return dto;
    }
    /**
     * 根据单位币别筛选有效的企业活期账号
     *
     * @param corpId
     * @param curId
     * @return
     */
    public List<FcAccVO> listFcAccByCorpId(String corpId, String curId) {
        return fcAccService.listFcAccByCorpId(corpId, curId);
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
        return this.fcAccService.getInfoByBankCode(bankCode);
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位已经开设内部账户，不允许删除！
            int num = this.fcAccService.getFcAccCountByCorpId(corpId, "del");
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
            int num = this.fcAccService.getFcAccCountByCorpId(corpId, "disable");
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
        return this.fcAccService.getAccListByInfo(netId, corpId, sysSubjectCode, curId, bankTypeId);
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
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_QUERY, user.getId()), "t.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        String corpId = (String) dataTablesInput.getFormdata().get("corpId");
        Map<String, Object> params = new HashMap<>();
        int entrySign = configService.getConfig("ist", "entrySign").toInteger().intValue();
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        if (entrySign == 0) {//结算中心
            formdata.put("corpId", "");
        } else {//成员单位或银行
            formdata.put("corpId", corpId);
        }
        DataTablesOutput dataTablesOutput = this.fcAccService.listFcAccInfoDataTable(dataTablesInput, sql.toString());
        return dataTablesOutput;
    }
    /**
     * 根据账号信息查找计息账号列表
     *
     * @param vo
     * @return
     */
    public List<FcAccVO> getAccListByExpPay(FcAccVO vo) {
        return this.fcAccService.getAccListByExpPay(vo);
    }
    /**
     * 根据账号删除关联计息账号
     *
     * @param entity
     */
    public void delInterestAcc(FcAccVO entity) {
        if (!EmptyUtils.isEmpty(entity) && !EmptyUtils.isEmpty(entity.getBlnctrAcc())) {
            FcAccVO vo = new FcAccVO();
            vo.setBlnctrAcc(entity.getBlnctrAcc());
            List<FcAccVO> accListByExpPay = this.getAccListByExpPay(vo);
            if (!EmptyUtils.isEmptyList(accListByExpPay)) {
                for (FcAccVO accVO : accListByExpPay) {
                    FcAccVO fcAcc = fcAccService.getFcAcc(accVO.getId());
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
    public void saveInterestAcc(FcAccVO fcAcc) {
        //如果计息账号不为空,遍历账号根据科目设置到对应的字段中,
        //应收应付利息户:INTEREST_REC_PAY,利息收入支出户:INTEREST_IN_EXP
        if (!StringUtils.isBlank(fcAcc.getAccType()) && InnerAccountConstant.FCACC_INNER_ACCOUNT.equals(fcAcc.getAccType())) {
            FcSubjectDTO subjectDto = fcSubjectService.findSubjectById(fcAcc.getSubjectId());
            if (!EmptyUtils.isEmpty(subjectDto) && !EmptyUtils.isEmpty(subjectDto.getSysSubjectCode())) {
                if (!StringUtils.isBlank(fcAcc.getInterestAcc())) {
                    String[] accArr = fcAcc.getInterestAcc().split(CommonConstant.SEMI_SPLIT);
                    for (int i = 0; i < accArr.length; i++) {
                        FcAccVO accVo = this.getFcAccVOByBlnctrAcc(accArr[i]);
                        if (!EmptyUtils.isEmpty(accVo)) {
                            FcAccVO accEntity = fcAccService.getFcAcc(accVo.getId());
                            //系统科目：应付利息-企业活期存款（2011）,应付利息-企业定期存款（2012）,应付利息-通知存款（2013）,应付利息-企业协定（2016）,
                            // 应收利息-银行活期存款（1032）,应付利息-银行借款（2014）,应收利息-企业放款（1031）,这几个科目放入INTEREST_REC_PAY字段中
                            if (SubjectConstant.SysSubject.INTEREST_PAYABLE_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                                    || SubjectConstant.SysSubject.DEPOSIT_INTEREST_INCOME_REGULAR.get().equals(subjectDto.getSysSubjectCode())
                                    || SubjectConstant.SysSubject.INTEREST_PAYABLE_NOTICE_DEPOSIT.get().equals(subjectDto.getSysSubjectCode())
                                    || SubjectConstant.SysSubject.INTEREST_PAYABLE_COMP_AGREED.get().equals(subjectDto.getSysSubjectCode())
                                    || SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                                    || SubjectConstant.SysSubject.INTEREST_PAYABLE_BANK_LOAN.get().equals(subjectDto.getSysSubjectCode())
                                    || SubjectConstant.SysSubject.INTEREST_PAYABLE_LOAN.get().equals(subjectDto.getSysSubjectCode())) {
                                accEntity.setInterestRecPay(fcAcc.getBlnctrAcc());
                            }
                            //系统科目：利息支出-企业活期存款（5212）,利息支出—企业定期存款（5211）,利息支出-通知存款（5213）,利息支出-企业协定（5216）,
                            //利息收入-银行活期存款（5012）,利息支出-银行借款（5214）,利息收入-企业放款（5011）
                            if (SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                                    || SubjectConstant.SysSubject.DEPOSIT_INTEREST_EXPENDITURE_REGULAR.get().equals(subjectDto.getSysSubjectCode())
                                    || SubjectConstant.SysSubject.INTEREST_EXPENSES_NOTICE_DEPOSIT.get().equals(subjectDto.getSysSubjectCode())
                                    || SubjectConstant.SysSubject.DISCOUNT_INTEREST_COMP_AGREED.get().equals(subjectDto.getSysSubjectCode())
                                    || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_CURRENT.get().equals(subjectDto.getSysSubjectCode())
                                    || SubjectConstant.SysSubject.INTEREST_EXPENSES_BANK_LOAN.get().equals(subjectDto.getSysSubjectCode())
                                    || SubjectConstant.SysSubject.INTEREST_EXPENSES_LOAN.get().equals(subjectDto.getSysSubjectCode())) {
                                accEntity.setInterestInExp(fcAcc.getBlnctrAcc());
                            }
                            this.saveOrUpdate(accEntity);
                        }
                    }
                }
            }
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

        List<FcAccDTO> list = fcAccService.getFcAccByCorpIdList(corpIdList, accState, joinSql);
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
        return fcAccService.getFcAccsByCorpIdList(corpIdList, accState, joinSql);
    }

    public String extractSeqNumFromBlnctrAcc(int accAccNoRule, String blnctrAcc) {
        if (StringUtils.isEmpty(blnctrAcc)) {
            return "";
        }

        int tempLength;
        switch (accAccNoRule) {
            case 1:
                tempLength = 3;
                break;
            case 2:
                tempLength = 4;
                break;
            case 3:
                tempLength = 6;
                break;
            case 4:
                tempLength = 4;
                break;
            default:
                tempLength = 4;
        }

        if (blnctrAcc.length() < tempLength) {
            return blnctrAcc;
        }

        return blnctrAcc.substring(blnctrAcc.length() - tempLength);
    }
}
