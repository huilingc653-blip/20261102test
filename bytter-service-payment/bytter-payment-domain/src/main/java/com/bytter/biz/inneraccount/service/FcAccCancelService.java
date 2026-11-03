package com.bytter.biz.inneraccount.service;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.api.service.*;
import com.bytter.biz.finacial.api.vo.FcAccWriteoffVO;
import com.bytter.biz.finacial.api.vo.FcOnlineLedgerVO;
import com.bytter.biz.inneraccount.util.InnerAccountUtils;
import com.bytter.biz.inneraccount.vo.FcCancelAccountVO;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.vo.FcAccListVO;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户信息表
 * @date 2017-05-25 19:36:12
 */
@Service("fcAccCancelService")
@Transactional
public class FcAccCancelService {
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
    private IFcAccWriteoffService fcAccWriteoffService;
    @DubboReference(check = false)
    private IFcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IFcAccLogService fcAccLogService;
    @DubboReference(check = false)
	private IFcDayCloseService fcDayCloseService;
    @Autowired
    private FcInitAccService fcInitAccService;
    @DubboReference(check = false)
	private IFcInterestService fcInterestService;
    @DubboReference(check = false)
	private IFcAccbookService fcAccbookService;
    @DubboReference(check = false)
	private IFcSubjectService fcSubjectService;
    @DubboReference(check = false)
	private INisBillheadService nisBillheadservice;
    @DubboReference(check = false)
	private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
	private IFcFinacialParamConfigService fcFinacialParamConfigService;
    @DubboReference(check = false)
	private IFcBusRaceService fcBusRaceService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
	private IFcCorpPoolService fcCorpPoolService;
    @Autowired
    private IBtCurrencyService btCurrencyServiceI;
    /**
     * 查询数据集
     *
     * @param id
     * @return
     */
    public FcAccVO getFcAccById(String id) {
    	return fcAccService.getFcAcc(id);
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,ICurrentUser userVO) {
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params=new HashMap();
        String corpAuthSql=SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CANCELQUERY,userVO.getId()),"t.corp_Id",params);
        params.put("loginName",userVO.getLoginName());
        //是否注销复核
        String isCheck = (String) dataTablesInput.getFormdata().get("isCheck");
        if ("1".equals(isCheck)) {
            sql.append(" where t.acc_state=:accState and t.Operator_Code <> :loginName");
            params.put("accState",InnerAccountConstant.ACC_STATE_REQUISITION);
        } else {//查找可以注销的账户，正常状态账户和注销暂存，注销打回
            sql.append(" where (t.acc_state=:accStateN or (t.acc_state=:accStateZ and t.Operator_Code =:loginName )");
            sql.append(" or (t.acc_state=:accStateU and t.Operator_Code =:loginName))");
            params.put("accStateN",InnerAccountConstant.ACC_STATE_NORMAL);
            params.put("accStateZ",InnerAccountConstant.ACC_STATE_Z);
            params.put("accStateU",InnerAccountConstant.ACC_STATE_U);
        }
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = fcAccService.getDataTablesUnion(dataTablesInput, sql.toString(),params);
        List<FcAccListVO> list = output.getData();
        if(list.size()>0) {
            Map<String, CorpDTO> corpMap = corpService.getCorpNetMap();
            Map<String, BtCurrencyDTO> curMap = btCurrencyServiceI.getCurrency();
            for(FcAccListVO vo:list){
                vo.setAccTypeName(dictService.get("InnerAccount.AccType", vo.getAccType()));
                vo.setAccStateName(dictService.get("inneraccount.fcStatus", vo.getAccState()));
                vo.setBalanceWayName(dictService.get("Admin.BalanceWay", vo.getBalanceWay()));

                CorpDTO corpDTO= corpMap.get(vo.getCorpId());
                vo.setCorpName(corpDTO.getName());
                vo.setNetName(corpDTO.getNetName());
                vo.setCurName(curMap.get(vo.getCurId()).getCurName());
            }
        }
        return output;
    }

    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput,ICurrentUser userVO) {
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params=new HashMap();
        params.put("loginName",userVO.getLoginName());
        //是否注销复核
        String isCheck = (String) dataTablesInput.getFormdata().get("isCheck");
        if ("1".equals(isCheck)) {
            sql.append(" where t.acc_state=:accState and t.Operator_Code <> :loginName");
            params.put("accState",InnerAccountConstant.ACC_STATE_REQUISITION);
        } else {//查找可以注销的账户，正常状态账户和注销暂存，注销打回
            sql.append(" where (t.acc_state=:accStateN or (t.acc_state=:accStateZ and t.Operator_Code =:loginName )");
            sql.append(" or (t.acc_state=:accStateU and t.Operator_Code =:loginName))");
            params.put("accStateN",InnerAccountConstant.ACC_STATE_NORMAL);
            params.put("accStateZ",InnerAccountConstant.ACC_STATE_Z);
            params.put("accStateU",InnerAccountConstant.ACC_STATE_U);
        }
        List<String> corpList = new ArrayList<>();
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELQUERY;
        List<FcCorpPoolDTO> pools = new ArrayList<>();
        //资金池不为空
        String poolId = (String) dataTablesInput.getFormdata().get("eqs_netId");
        if(!StringUtils.isBlank(poolId)){
            pools.add(fcCorpPoolService.getById(poolId));
        }else{
            pools = fcCorpPoolService.getUserCorpPools(userVO, resource);
        }
        //TODO 如果pools为空，应该抛异常还是继续返回空列表？在结算-基础数据维护-单位资金池维护 没设置时，pools为空
        for(FcCorpPoolDTO pool:pools) {
            List<FcCorpPoolDTO> poolChilds = fcCorpPoolService.getAllUserCorpPoolChilds(userVO, resource, pool.getId());
            if (!EmptyUtils.isEmptyList(poolChilds)) {
                for (FcCorpPoolDTO dto : poolChilds) {
                    corpList.add(dto.getCorpId());
                }
            }
        }
        String corpSql = SqlUtil.getInSql(corpList, "t.corp_id", true);
        sql.append(" and ").append(corpSql);
        DataTablesOutput output = fcAccService.getDataTablesUnionForPool(dataTablesInput, sql.toString(),params);
        List<FcAccVO> list = output.getData();
        if(list.size()>0) {
            Map<String, CorpDTO> corpMap = corpService.getCorpNetMap();
            Map<String, BtCurrencyDTO> curMap = btCurrencyServiceI.getCurrency();
            for (FcAccVO vo : list) {
                vo.setAccTypeName(dictService.get("InnerAccount.AccType", vo.getAccType()));
                vo.setAccStateName(dictService.get("inneraccount.fcStatus", vo.getAccState()));
                vo.setBalanceWayName(dictService.get("Admin.BalanceWay", vo.getBalanceWay()));

                CorpDTO corpDTO= corpMap.get(vo.getCorpId());
                vo.setCorpName(corpDTO.getName());
                vo.setNetName(corpDTO.getNetName());
                vo.setNetPoolName(corpDTO.getNetName());
                vo.setCurName(curMap.get(vo.getCurId()).getCurName());
            }
        }
        return output;
    }
    /**
     * 注销复核通过或者打回
     *
     * @param fcAcc
     */
    public void check(FcAccVO fcAcc,ICurrentUser userVo) {
        FcAccVO acc = fcAccService.getFcAcc(fcAcc.getId());
        if(!InnerAccountConstant.ACC_STATE_REQUISITION.equals(acc.getAccState())){
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS,acc.getBlnctrAcc());
        }
        //完成通知提醒
        if(AllocationConstant.POOL_SIGN_1.equals(acc.getPoolSign())){
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CANCELCHECK, acc.getId(), userVo.getId());
        }else {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CANCELCHECK, acc.getId(), userVo.getId());
        }
        if (InnerAccountConstant.ACC_STATE_DESTORY.equals(fcAcc.getAccState())) {//改户复核通过
            checkCommonEnter(acc,userVo);
            saveLog(acc, CommonConstant.SUBMIT_CHECK, InnerAccountConstant.BIZ_TYPE_CHECK_SUCCESS,userVo.getLoginName());
        } else {//注销打回
            //完成通知提醒
            if(AllocationConstant.POOL_SIGN_1.equals(acc.getPoolSign())) {
                notificationService.sendToUserByLoginName(acc.getId(), acc.getUpdateBy(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CANCELBACK, acc);
            }else {
                notificationService.sendToUserByLoginName(acc.getId(), acc.getUpdateBy(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CANCELBACK, acc);
            }
            saveLog(acc, CommonConstant.SUBMIT_CHECK, InnerAccountConstant.BIZ_TYPE_CHECK_BACK,userVo.getLoginName());
        }
        FcAccWriteoffVO writeoff = fcAccWriteoffService.getFcAccWriteoff(acc.getBlnctrAcc());
        if(writeoff != null){
            processCheckCancelAccount(fcAcc,writeoff.getBusGlobalId(),userVo);
        }else{
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS, acc.getBlnctrAcc());
        }
    }
    /**
     * 注销保存
     * @param fcCancelAccountVO
     * @param opType
     */
    public void saveCancel(FcCancelAccountVO fcCancelAccountVO,String opType,ICurrentUser userVo){
        FcAccVO acc = fcAccService.getFcAcc(fcCancelAccountVO.getId());
        //完成通知提醒
        if(AllocationConstant.POOL_SIGN_1.equals(acc.getPoolSign())) {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CANCELBACK, acc.getId(), userVo.getId());
        }else {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CANCELBACK, acc.getId(), userVo.getId());
        }
        commonCancel(fcCancelAccountVO,opType,userVo);
        //日志记录
        if(InnerAccountConstant.ACC_STATE_REQUISITION.equals(opType)) {
            saveLog(acc, CommonConstant.SUBMIT_ADD, InnerAccountConstant.BIZ_TYPE_SUBMIT,userVo.getLoginName());
        }else {
            saveLog(acc, CommonConstant.SUBMIT_ADD, InnerAccountConstant.BIZ_TYPE_TEMP,userVo.getLoginName());
        }
    }
    /**
     * 功能：注销修改,1、先删除先前的信息；2、再恢复账户信息;3、再放一条新的记录
     * 注销编辑
     * @param fcCancelAccountVO
     * @param opType
     */
    public void editCancel(FcCancelAccountVO fcCancelAccountVO,String opType,ICurrentUser userVo){
        FcAccVO acc = fcAccService.getFcAcc(fcCancelAccountVO.getId());
        if (isCurrType(acc.getAccType())) {
            deleteCommonCancelAccount(acc,userVo.getLoginName());
        }
        //完成通知提醒
        if(AllocationConstant.POOL_SIGN_1.equals(acc.getPoolSign())) {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CANCELBACK, acc.getId(), userVo.getId());
        }else {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CANCELBACK, acc.getId(), userVo.getId());
        }
        commonCancel(fcCancelAccountVO,opType,userVo);
        //日志记录
        if(InnerAccountConstant.ACC_STATE_REQUISITION.equals(opType)) {
            saveLog(acc, CommonConstant.SUBMIT_MODIFY, InnerAccountConstant.BIZ_TYPE_SUBMIT,userVo.getLoginName());
        }else {
            saveLog(acc, CommonConstant.SUBMIT_MODIFY, InnerAccountConstant.BIZ_TYPE_TEMP,userVo.getLoginName());
        }
    }
    private void commonCancel(FcCancelAccountVO fcCancelAccountVO,String opType,ICurrentUser userVo){
    	FcAccVO acc = fcAccService.getFcAcc(fcCancelAccountVO.getId());
        checkCommonEnter(acc,userVo);
        //添加账户注销记录
        if(fcCancelAccountVO.getDayOver() == null){
            if(AllocationConstant.POOL_SIGN_1.equals(acc.getPoolSign())){
                fcCancelAccountVO.setDayOver(fcDayCloseService.getPoolDayCloseDate(acc.getNetId()));
            }else {
                fcCancelAccountVO.setDayOver(fcDayCloseService.getDayCloseFinalDate(acc.getNetId()));
            }
        }
        String writeOffId = fcAccWriteoffService.saveObject(fcCancelAccountVO.getBlnctrAcc(),fcCancelAccountVO.getTransferAcc(),fcCancelAccountVO.getDayOver(),userVo.getLoginName());
        acc.setAccState(opType);
        acc.setOperatorCode(userVo.getLoginName());
        acc.setUpdateBy(userVo.getLoginName());
        acc.setUpdateDate(DateUtils.getDate());
        fcAccService.update(acc);
        //fcAccService.flush();
        //记账
        String busGlobalId = "";
        if("1".equals(fcCancelAccountVO.getIsNeedBusRule())) {
            busGlobalId = saveVoucher(fcCancelAccountVO, acc, opType, writeOffId,userVo);
            //针对暂存的注销帐户，需要排除当前暂存的数据重新提交注销操作验证
            if (!fcAccbookService.existsUnCheckAccbook(acc.getBlnctrAcc(),busGlobalId)) {
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_EXISTS_UNCHECK_BOOK, acc.getBlnctrAcc());//"账号{0}存在未复核的账,不能销户";
            }
            //记录全局标识id
            fcAccWriteoffService.updateBusGlobalId(fcCancelAccountVO.getBlnctrAcc(), busGlobalId);
        }
        //账户开户注销复核
        if(InnerAccountConstant.ACC_STATE_REQUISITION.equals(opType)) {
            processAutoCheck(acc, busGlobalId,userVo);
        }
    }
    /**
     * 记账
     * @param fcCancelAccountVO
     * @param opType
     */
    private String saveVoucher(FcCancelAccountVO fcCancelAccountVO,FcAccVO acc,String opType,String writeOffId,ICurrentUser userVo){
        // 如果账号余额和利息都为0，则不需要定义会计分录
        isNeedBusRule(fcCancelAccountVO ,fcCancelAccountVO.getBlnctrAcc(),userVo);
        if("1".equals(fcCancelAccountVO.getIsNeedBusRule())) {
            FcAccbookDTO accBook = new FcAccbookDTO();
            String sysRaceCode =  FinaBookConstant.SysRaceCode.CANCEL_ACCOUNT.get();
            String busRaceId = fcBusRaceService.getNetBusRace(sysRaceCode, acc.getNetId(),userVo.getId());
            ConstantBean<Integer> busOriType = FinaBookConstant.BusOriType.CANCEL_ACCOUNT;
            String voucherType = fcCancelAccountVO.getVoucherType();
            accBook.setNetId(acc.getNetId());
            accBook.setBusRaceId(busRaceId);
            accBook.setBusOriType(busOriType.get());
            accBook.setVoucherType(voucherType);
            accBook.setTallyDate(fcCancelAccountVO.getDayOver());
            accBook.setInterestStartDate(fcCancelAccountVO.getStartDate());
            accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_F);
            List<FcAccbookDtlDTO> accbookDtls=new ArrayList<>();
            FcAccbookDtlDTO fcAccbookDtl;
            List<FcVoucherEntryDefineDTO> voucherDtls = fcCancelAccountVO.getListFcVoucher();
            String[] accounts = fcCancelAccountVO.getAccount();
            for (int i = 0; i < accounts.length; i++) {
                fcAccbookDtl = new FcAccbookDtlDTO();
                FcAccVO fcAcc = fcAccService.findFcAccByBlnctrAcc(accounts[i]);
                if(fcAcc !=null){
                    fcAccbookDtl.setAccName(fcAcc.getAccName());
                    fcAccbookDtl.setAccount(fcAcc.getBlnctrAcc());
                }else{
                    throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS, accounts[i]);
                }
                fcAccbookDtl.setLineNo(i+1);
                fcAccbookDtl.setCurCode(fcAcc.getCurId());
                fcAccbookDtl.setSummaryContent(busOriType.getName());
                fcAccbookDtl.setDebitMoney(fcCancelAccountVO.getDebitMoney()[i]);
                fcAccbookDtl.setCreditMoney(fcCancelAccountVO.getCreditMoney()[i]);
                BtSummaryDTO btSummaryDTO =fcFinacialParamConfigService.getBtSummaryDTOById(fcCancelAccountVO.getSummaryContent()[i]);
                if(btSummaryDTO != null){
                    fcAccbookDtl.setSummaryContent(btSummaryDTO.getSummaryContent());
                }
                accbookDtls.add(fcAccbookDtl);
            }
            FcAccbookVoucherDTO voucherInfo=new FcAccbookVoucherDTO();
            voucherInfo.setAccbook(accBook);
            voucherInfo.setBookDtls(accbookDtls);
            VoucherWrapperDTO voucher=new VoucherWrapperDTO();
            voucher.addTarget(voucherInfo);
            voucher.setCharge(false);
            voucher.setProcessType(ProcessType.PROCESS_COMMON);
            voucher.setUserCode(userVo.getId());//设置用户
            voucher.setAutoCheck(false);
            voucher.setBusGlobalId(fcCancelAccountVO.getBusGlobalId());
            voucher.setAutoAccounter(false);//业务上处理记账
            if(InnerAccountConstant.ACC_STATE_REQUISITION.equals(opType)){
                fcAccbookService.submitVoucher(voucher);
            }else{//暂存
                fcAccbookService.saveVoucher(voucher);
            }
            String orderNo = voucher.getTarget().get(0).getAccbook().getOrderNo();
            fcInterestService.processTellerMoneyBalance(acc.getBlnctrAcc(),null,orderNo,userVo,writeOffId,null);
            return voucher.getBusGlobalId();
        }
        return "";
    }
    /**
     * 检查能否销户
     * @param acc
     */
    public void checkCommonEnter(FcAccVO acc,ICurrentUser user) {
        validFcAcc(acc);
        //检查本账号对应的业务是否已完成
        this.commonValid(acc.getBlnctrAcc(),user);
        //企业活期账号[放款账号] 注销时效验
        if(isCurrType(acc.getAccType())){
            checkLoanAcc(acc.getBlnctrAcc(),user);
        }
        //校验账户类型为内部账号的账户销户是否有关联关系
        isExsitsByExpPay(acc);
    }
    /**
     * 校验账户类型为内部账号的账户销户是否有关联关系
     * @param fcAcc
     */
    public void isExsitsByExpPay(FcAccVO fcAcc){
        if(!EmptyUtils.isEmpty(fcAcc) && !EmptyUtils.isEmpty(fcAcc.getAccType())){
            if(InnerAccountConstant.FCACC_INNER_ACCOUNT.equals(fcAcc.getAccType())){
                /** 账号{0}有关联的计息账号存在，不能销户！*/
                if(fcAccService.isExsitsByExpPay(fcAcc.getBlnctrAcc())){
                    throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_EXIST_EXPPAY, fcAcc.getBlnctrAcc());
                }
            }
        }
    }
    /**
     * 第一步
     * acc_type 01 05 08 活期 定期  通知
     * 账户余额fc_online_ledger.check_balance online_balance可以不为零
     * 其他的账户余额需为零才能注销
     * @return
     */
    public void validFcAcc(FcAccVO fcAcc){
        if(InnerAccountUtils.validAccType(fcAcc.getAccType())){//过滤账户种类是否需要验证
            if(validAmt(fcAcc.getBlnctrAcc())){
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_NOT_ZERO, fcAcc.getBlnctrAcc());
            }
        }
    }
    /**
     * 取消--还原销户申请前的数据
     * @param id
     */
    public void canelEntity(String id,ICurrentUser user) {
        FcAccVO acc = fcAccService.getFcAcc(id);
        //完成通知提醒
        if(AllocationConstant.POOL_SIGN_1.equals(acc.getPoolSign())) {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CANCELBACK, acc.getId(), user.getId());
        }else {
            notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CANCELBACK, id, user.getId());
        }
        deleteCommonCancelAccount(acc,user.getLoginName());
    }
    /**
     * 取消销户公共方法
     * @param fcAcc
     */
    public void deleteCommonCancelAccount(FcAccVO fcAcc,String loginName) {
        fcAcc.setUpdateBy(loginName);
        fcAcc.setUpdateDate(DateUtils.getDate());
        fcAcc.setAccState(InnerAccountConstant.ACC_STATE_NORMAL);
        fcAccService.update(fcAcc);
        if (isCurrType(fcAcc.getAccType())) {
            FcAccWriteoffVO writeoff = fcAccWriteoffService.getFcAccWriteoff(fcAcc.getBlnctrAcc());
            if(writeoff != null) {
                String busGlobalId = writeoff.getBusGlobalId();
                if (StringUtils.isNotEmpty(busGlobalId)) {
                    VoucherWrapperDTO voucher = fcAccbookService.getVoucher(FinaBookConstant.BusOriType.CANCEL_ACCOUNT.get(),writeoff.getBusGlobalId());
                    voucher.setAutoAccounter(false);
                    fcAccbookService.deleteVoucher(voucher);
                    //删除账户产生的利息，更新积数
                    fcInterestService.deleteAccInterest(fcAcc.getBlnctrAcc(),voucher.getTarget().get(0).getAccbook().getOrderNo(),writeoff.getId());
                }
            }
        }
        //销户信息删除
        fcAccWriteoffService.delWriteOffByBlnctrAcc(fcAcc.getBlnctrAcc());
    }
    /**
     * 账户注销复核
     * @param fcAcc
     */
    private void processAutoCheck(FcAccVO fcAcc,String busGlobalId,ICurrentUser userVo){
        // 提交账户时获取账户自动复核参数,判断是否自动复核
        int thawAutoCheck = 0;
        if(AllocationConstant.POOL_SIGN_1.equals(fcAcc.getPoolSign())){
            thawAutoCheck = configService.getConfig("pool", "thawAutoCheck").toInteger(0).intValue();
        }else{
            thawAutoCheck = configService.getConfig("fcs", "thawAutoCheck").toInteger(0).intValue();
        }
        if(thawAutoCheck==1){
            //日志记录
            saveLog(fcAcc, CommonConstant.SUBMIT_ADD, InnerAccountConstant.BIZ_TYPE_CHECK_AUTO_SUCCESS,userVo.getLoginName());
            fcAcc.setAccState(InnerAccountConstant.ACC_STATE_DESTORY);
            processCheckCancelAccount(fcAcc,busGlobalId,userVo);
        }else{
            if(AllocationConstant.POOL_SIGN_1.equals(fcAcc.getPoolSign())){
                notificationService.sendIgnoreMySelf(fcAcc.getId(), fcAcc.getCorpId(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_POOL_CANCELCHECK, fcAcc, userVo.getId());
            }else {
                notificationService.sendIgnoreMySelf(fcAcc.getId(), fcAcc.getCorpId(), NotificationConstant.NETBANK.INNERACCOUNT_FCACC_CANCELCHECK, fcAcc, userVo.getId());
            }
        }
    }
    /**
     *
     * 抽出复核处理方法
     * @param fcAcc
     */
    private void processCheckCancelAccount(FcAccVO fcAcc,String busGlobalId,ICurrentUser userVo){
        FcAccVO acc = fcAccService.findFcAccByBlnctrAcc(fcAcc.getBlnctrAcc());
        acc.setAccState(fcAcc.getAccState());
        acc.setCheckerCode(userVo.getLoginName());
        acc.setCheckTime(DateUtils.getDate());
        acc.setCheckBackReason(fcAcc.getCheckBackReason());
        fcAccService.update(acc);
        if (isCurrType(acc.getAccType())) {
            //复核调用会计分录接口
            if(StringUtils.isNotEmpty(busGlobalId)){
                VoucherWrapperDTO voucher = new VoucherWrapperDTO();
                voucher.setOriType(FinaBookConstant.BusOriType.CANCEL_ACCOUNT.get());
                voucher.setBusGlobalId(busGlobalId);
                voucher.setUserCode(userVo.getLoginName());
                voucher.setAutoAccounter(false);
                //复核通过
                if (InnerAccountConstant.ACC_STATE_DESTORY.equals(fcAcc.getAccState())) {
                    fcAccbookService.checkVoucher(voucher);
                }else{
                    fcAccbookService.backVoucher(voucher);
                }
            }
        }
        //注销通过  ---记录账户注销复核信息
        if (InnerAccountConstant.ACC_STATE_DESTORY.equals(fcAcc.getAccState())) {
            fcAccWriteoffService.updateObject(fcAcc.getBlnctrAcc(),userVo.getLoginName());
        }
    }
    /**
     * 是否活期
     * @param accTypeCode
     * @return
     */
    public boolean isCurrType(String accTypeCode){
        return accTypeCode.equals(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT);
    }
    /**
     * 效验金额
     * @return true 不通过，false 通过
     */
    public boolean validAmt(String blnctrAcc){
        boolean flag = false;
        FcOnlineLedgerVO onlineLedger=fcOnlineLedgerService.getEntityByAccount(blnctrAcc);
        if (onlineLedger == null) {
            /** 账号{0} 没有实时金额，存在异常账号数据，请联系管理员！*/
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_NOT_EXIST_ONLINE, blnctrAcc);
        }
        BigDecimal bal = onlineLedger.getCheckBalance();//复核余额
        BigDecimal onlBal = onlineLedger.getOnlineBalance();//联机余额
        if (bal.compareTo(BigDecimal.ZERO)!=0 || onlBal.compareTo(BigDecimal.ZERO)!=0) {
            flag = true;
        }
        return flag;
    }
    /**
     * 公共验证方法
     * @param blnctrAcc
     * @return
     */
    public void commonValid(String blnctrAcc,ICurrentUser user) {
        boolean flag= fcInitAccService.accountInitCheck(blnctrAcc);
        if(flag) { //"账户 {0} 存在未复核的初始化数据,不能销户";
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_ACCOUNT_INIT_CHECK_EXISTS, blnctrAcc);
        }
        FcAccVO fcAcc = fcAccService.findFcAccByBlnctrAcc(blnctrAcc);
        if (!isCurrType(fcAcc.getAccType())) {
            //"账号 {0} 存在未结息的利息";
            BigDecimal interest=calAccInterest(fcAcc.getBlnctrAcc(),user);
            if(interest != null && interest.compareTo(BigDecimal.ZERO)>0){
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_CAL_TELLER_MONEY_INTEREST, blnctrAcc);
            }
        }
        if(fcInterestService.existUnOverInterest(blnctrAcc)){
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_EXIST_UNOVER_INTEREST, blnctrAcc); //" 账号{0}账号存在在途利息";
        }
        //if (!isCurrType(fcAcc.getAccType())) {
            if (existUnRush(blnctrAcc)) {
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_EXIST_UNRUSH, blnctrAcc); //" 账号{0}此账号存在未冲预提,不能销户";
            }
        //}
        if (nisBillheadservice.isExistBlnctrAcc(blnctrAcc)) {
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_PAY_ORDER_CONFIRM, blnctrAcc); //"此账号在业务付款申请单有未确认的申请,不能销户";
        }
        if (fcAccService.isOtherAccountQuote(blnctrAcc)){
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_OTHER_ACCOUNT_QUOTE, blnctrAcc); //" 账号{0}是被其他的利息上账（扣息）、手续费引用,不能销户";
        }
        if (isNotReceivedFee(blnctrAcc)) {
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_NOT_RECEIVED, blnctrAcc); //" 账号{0}此账号有未收的手续费,不能销户";
        }
        //"账号{0}有未收的应收利息,不能销户";
        if (!fcAccService.isNotReceivedInterest(blnctrAcc)){
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_NOT_RECEIVED_INTEREST, blnctrAcc);
        }
        //"账号{0}有关联的银行账号存在,不能销户";
        if (fcAccService.isExsitsForValidBankAccByInnerAcc(blnctrAcc)){
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_EXIST_BANKACC, blnctrAcc);
        }
    }
    /**
     * 计算冲预提金额利息
     * @param account
     * @return
     */
    private BigDecimal calAccInterest(String account,ICurrentUser user){
        return fcInterestService.calTellerMoneyInterest(account, BigDecimal.ZERO,null,null,user);
    }
    /**
     * 计算冲预提金额
     * @param account
     * @return
     */
    public BigDecimal calPredrawMoney(String account){
        return fcInterestService.getRushPreDrawMoney(account, null, null);
    }
    /**
     * 是否存在未冲预提
     * @param blnctrAcc
     * @return
     */
    public boolean existUnRush(String blnctrAcc) {
        BigDecimal drawMoney = this.calPredrawMoney(blnctrAcc);
        if (BigDecimal.ZERO.compareTo(drawMoney) != 0) {
            return true;
        }
        return false;
    }
    /**
     * 判断该账号是否有未收的手续费
     * @param blnctrAcc
     * @return
     */
    public boolean isNotReceivedFee(String blnctrAcc) {
        Map<String, BigDecimal> res = fcAccbookService.getNotOverChargeByAccount(blnctrAcc);
        if(res==null || res.isEmpty()){
            return false;
        }
        return true;
    }
    /**
     * 企业活期账号[放款账号] 注销时效验
     *
     * @param blnctrAcc
     * @return
     */
    public void checkLoanAcc(String blnctrAcc,ICurrentUser user){
        List<FcAccVO> accList  = fcAccService.getFcAccByAccForDeduct(blnctrAcc);
        for (FcAccVO fcAcc : accList) {
            //与之关联的放款账号是否存在余额
            boolean flag = validAmt(fcAcc.getBlnctrAcc());
            if(flag) {
                //"账号对应的放款账号{0}余额不为0";
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_NOT_ZERO_FOR_DEDUCT, blnctrAcc);
            }
            BigDecimal money=calAccInterest(fcAcc.getBlnctrAcc(),user);
            if(money != null && money.compareTo(BigDecimal.ZERO)>0){
                // "账号对应的放款账号{0}存在未结息的利息";
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_NOT_TELLER_MONEY_INTEREST, blnctrAcc);
            }
            //判断 在途利息
            if(fcInterestService.existUnOverInterest(fcAcc.getBlnctrAcc())){
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_EXIST_UNOVER_INTEREST_FOR_DEDUCT, blnctrAcc);//"账号对应的放款账号{0}存在在途利息";
            }
            //与之关联的放款账号是否存在未结清的手续费
            if(isNotReceivedFee(blnctrAcc)){
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_NOT_RECEIVED_FOR_DEDUCT, blnctrAcc);//"账号对应的放款账号{0}存在未结清的手续费";
            }
            //与之关联的放款账号是否存在未复核的账
            if(!fcAccbookService.existsUnCheckAccbook(fcAcc.getBlnctrAcc(),"")){
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_EXISTS_UNCHECK_BOOK_FOR_DEDUCT, blnctrAcc);//"账号对应的放款账号{0}存在未复合的账";
            }
            //与之关联的放款账号是否存在应收利息
            if(!fcAccService.isNotReceivedInterest(fcAcc.getBlnctrAcc())){
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_NOT_RECEIVED_INTEREST_FOR_DEDUCT, blnctrAcc); // "账号对应的放款账号{0}应收预期利息余额不为0";
            }
            //与之关联的放款账号是否存在未冲预提
            if(existUnRush(fcAcc.getBlnctrAcc())){
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_EXIST_UNRUSH_FOR_DEDUCT, blnctrAcc);//"账号对应的放款账号{0}存在未冲预提";
            }
        }
    }
    /**
     * 得到添加前置页面需要的数据
     * @param blnctrAcc
     * @return
     */
    public FcCancelAccountVO getCancelPreData(String blnctrAcc,ICurrentUser user){
        FcCancelAccountVO fcCancelAccountVO = new FcCancelAccountVO();
        //获取当前账号相关信息
        com.bytter.system.api.vo.FcAccVO fcAccVO = fcAccService.getFcAccVOByBlnctrAcc(blnctrAcc);
        //注销转出账号,单位企业活期账号，不包含自己的活期账号
        List<com.bytter.system.api.vo.FcAccVO> transferOutAcc = fcAccService.transferOutAcc(blnctrAcc,fcAccVO.getNetId());
        String sysRaceCode =  FinaBookConstant.SysRaceCode.CANCEL_ACCOUNT.get();
        FcBusinessRuleDefineDTO accbookRule=this.getAccBookRule(fcAccVO.getNetId(),sysRaceCode);
        //获取记账分录模板
        List<FcVoucherEntryDefineDTO> listVoucher=accbookRule.getVouchers();
        /**
         * 如果是休眠账户
         */
        if (fcAccVO.getAccType().equals(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT)
                && fcAccVO.getAccState().equals(InnerAccountConstant.ACC_STATE_K)) {
        }
        // 如果账号余额和利息都为0，则不需要定义会计分录
        isNeedBusRule(fcCancelAccountVO ,blnctrAcc,user);
//        fcCancelAccountVO.setFcAcc(fcAccVO);
//        fcCancelAccountVO.setListFcVoucher(listVoucher);
//        fcCancelAccountVO.setTransferOutAcc(transferOutAcc);
        fcCancelAccountVO.setListBtSummary(fcFinacialParamConfigService.getListByValidSign());
        Date time = fcAccService.getValidDate(fcAccVO.getNetId());
        fcCancelAccountVO.setDayOver(time);
        setFcCancelAccountVO(fcCancelAccountVO);
        return fcCancelAccountVO;
    }
    /**
     * 得到编辑前置页面需要的数据
     * @param blnctrAcc
     * @return
     */
    public FcCancelAccountVO getCancelEditPreData(String blnctrAcc,ICurrentUser user){
        FcCancelAccountVO fcCancelAccountVO = new FcCancelAccountVO();
        //获取当前账号相关信息
        com.bytter.system.api.vo.FcAccVO fcAccVO = fcAccService.getFcAccVOByBlnctrAcc(blnctrAcc);
        //注销转出账号,单位企业活期账号，不包含自己的活期账号
        List<com.bytter.system.api.vo.FcAccVO> transferOutAcc = fcAccService.transferOutAcc(blnctrAcc,fcAccVO.getNetId());
        FcAccWriteoffVO writeoff = fcAccWriteoffService.getFcAccWriteoff(blnctrAcc);
        if(writeoff != null && StringUtils.isNotEmpty(writeoff.getBusGlobalId())){
            VoucherWrapperDTO voucherWrapper = fcAccbookService.getVoucher(FinaBookConstant.BusOriType.CANCEL_ACCOUNT.get(),writeoff.getBusGlobalId());
            FcAccbookVoucherDTO fcAccbookVoucher = voucherWrapper.getBookVoucher();
            fcCancelAccountVO.setFcAccbookVoucher(fcAccbookVoucher);
            fcCancelAccountVO.setTransferAcc(writeoff.getTransferAcc());
            fcCancelAccountVO.setBusGlobalId(writeoff.getBusGlobalId());
        }
//        else{
//            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS, blnctrAcc);
//        }
        /**
         * 如果是休眠账户
         */
        if (fcAccVO.getAccType().equals(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT)
                && fcAccVO.getAccState().equals(InnerAccountConstant.ACC_STATE_K)) {
        }
        // 如果账号余额和利息都为0，则不需要定义会计分录
        isNeedBusRule(fcCancelAccountVO ,blnctrAcc,user);
//        fcCancelAccountVO.setFcAcc(fcAccVO);
//        fcCancelAccountVO.setTransferOutAcc(transferOutAcc);
        fcCancelAccountVO.setListBtSummary(fcFinacialParamConfigService.getListByValidSign());
        fcCancelAccountVO.setDayOver(fcDayCloseService.getDayCloseFinalDate(fcAccVO.getNetId()));
        setFcCancelAccountVO(fcCancelAccountVO);
        return fcCancelAccountVO;
    }
    /**
     *  如果账号余额和利息都为0，则不需要定义会计分录
     * @param fcCancelAccountVO
     * @param blnctrAcc
     */
    private void isNeedBusRule(FcCancelAccountVO fcCancelAccountVO ,String blnctrAcc,ICurrentUser user){
        FcOnlineLedgerVO fcOnlineLedger = fcOnlineLedgerService.getEntityByAccount(blnctrAcc);
        if(fcOnlineLedger == null){
            /** 账号{0} 没有实时金额，存在异常账号数据，请联系管理员！*/
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_NOT_EXIST_ONLINE, blnctrAcc);
        }
        BigDecimal bal = fcOnlineLedger.getCheckBalance();
        BigDecimal interest = calAccInterest(blnctrAcc,user);
        BigDecimal rushMoney = calPredrawMoney(blnctrAcc);//冲预提金额
        if(BigDecimal.ZERO.compareTo(bal)==0 && BigDecimal.ZERO.compareTo(interest)==0){
            fcCancelAccountVO.setIsNeedBusRule("0");
        }else{
            fcCancelAccountVO.setIsNeedBusRule("1");
        }
        fcCancelAccountVO.setFcOnlineLedger(fcOnlineLedger);
        fcCancelAccountVO.setInterest(interest);
        fcCancelAccountVO.setRushMoney(rushMoney);
    }
    /**
     * 设置凭证分录信息
     * @param fcCancelAccountVO
     */
    private void setFcCancelAccountVO(FcCancelAccountVO fcCancelAccountVO){
        List<FcVoucherEntryDefineDTO> listFcVoucher = fcCancelAccountVO.getListFcVoucher();
        List<FcVoucherEntryDefineDTO> resListFcVoucher = new ArrayList<FcVoucherEntryDefineDTO>();
        FcAccVO fcAccVO = null;//TODO 整改 fcCancelAccountVO.getFcAcc();
        boolean flag = false;
        if (listFcVoucher != null && !listFcVoucher.isEmpty()) {
            for (FcVoucherEntryDefineDTO fcVoucherEntryDefine : listFcVoucher) {
                String subjectCode = fcVoucherEntryDefine.getSubjectCode();
                FcSubjectDTO fcSubjectDTO = fcSubjectService.findSubjectById(subjectCode);
                if(fcSubjectDTO == null){
                    continue;
                }
                if(FinacialConstant.ENTRY_TYPE_30.equals(fcVoucherEntryDefine.getEntryType())) {//冲预提
                    if (BigDecimal.ZERO.compareTo(fcCancelAccountVO.getRushMoney()) == 0) {
                        continue;
                    }
                    flag = setFcCancelAccountVO(fcVoucherEntryDefine, fcAccVO, subjectCode, false);
                }else if(FinacialConstant.ENTRY_TYPE_40.equals(fcVoucherEntryDefine.getEntryType())
                        && fcVoucherEntryDefine.getBalanceWay().toString().equals(FinacialConstant.BALANCE_WAY_DEBIT)) {
                    if(BigDecimal.ZERO.compareTo(fcCancelAccountVO.getInterest())==0){
                        continue;
                    }
                    flag = setFcCancelAccountVO(fcVoucherEntryDefine, fcAccVO, subjectCode, true);
                }
                resListFcVoucher.add(fcVoucherEntryDefine);
            }
            //1标识单位下没有利息支出企业活期账号 ，暂存是或提交时给予提示。
            fcCancelAccountVO.setIsInterestAcc(flag?1:0);
            fcCancelAccountVO.setListFcVoucher(resListFcVoucher);
        }
    }
    /**
     * 冲预提/结息 设置账号
     * @param fcVoucherEntryDefine
     * @param fcAccVO
     * @param subjectCode
     * @return true标识单位下没有利息支出企业活期账号
     */
    private boolean setFcCancelAccountVO(FcVoucherEntryDefineDTO fcVoucherEntryDefine,FcAccVO fcAccVO,String subjectCode, boolean interestSign){
        boolean flag = false;
        com.bytter.system.api.vo.FcAccPlusVO fcAccListVO = null;
        String instAcc = null;
        //获取分录的账号
        Integer balanceWay = fcVoucherEntryDefine.getBalanceWay();
        if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).equals(balanceWay)) {//借方
            instAcc = getDebitAccout(fcAccVO, interestSign);//获取借方计息账户的应收应付利息户
        } else {//贷方
            instAcc = getCreditAccout(fcAccVO, interestSign);//获取贷方计息账户的应收应付利息户
        }
        if (EmptyUtils.isNotEmpty(instAcc)) {
            fcVoucherEntryDefine.setAccount(instAcc);//获取账户信息
            return false;
        }
        int entrySign = configService.getConfig("ist", "entrySign").toInteger().intValue();
        if (StringUtils.isEmpty(fcVoucherEntryDefine.getAccount())) {
            if(entrySign == 0){
                fcAccListVO = fcAccService.getFcAccVONotAuthByParams(fcAccVO.getNetId(), fcAccVO.getNetId(),subjectCode,fcAccVO.getCurId());
            }else {
                fcAccListVO = fcAccService.getFcAccVONotAuthByParams(fcAccVO.getNetId(), fcAccVO.getCorpId(),subjectCode,fcAccVO.getCurId());
            }
            if(fcAccListVO != null){
                fcVoucherEntryDefine.setAccount(fcAccListVO.getBlnctrAcc());
            }else{
                flag = true;
                fcVoucherEntryDefine.setAccount(null);
                CorpDTO corpDTO = null;
                if(entrySign == 0){
                    corpDTO = corpService.get(fcAccVO.getNetId());
                }else{
                    corpDTO = corpService.get(fcAccVO.getCorpId());
                }
                if(!EmptyUtils.isEmpty(corpDTO)){
                    /** 单位：{0} ，没有可用的利息支出企业活期账号，无法转出余额到其他账号，因而无法注销！*/
                    //修改：如果计息方式是按结算中心，单位名称为网点，如果是银行／成员单位，单位名称名称为成员单位名称
                    throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_NOT_EXSITS_LIXIACCOUNT, corpDTO.getName());
                }
            }
        }
        return flag;
    }
    /**
     * <b>功能介绍：</b>获取借方记账账号<br>
     * <b>日期：</b>2020年6月16日<br>
     * <b>作者：</b>liyh<br>
     * @version 1.0
     */
    public String getDebitAccout(FcAccVO fcAccDTO, boolean interestSign) {
        if (interestSign) {//计息
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAccDTO.getAccType())) {//企业活期
                return fcAccDTO.getInterestInExp();//借利息支出
            } else if (InnerAccountConstant.FCACC_LOAN.equals(fcAccDTO.getAccType())) {//放款
                return fcAccDTO.getAccForDeduct();//借企业活期
            }
        } else {
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BANK_BORROW.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(fcAccDTO.getAccType())) {//企业活期、银行借款、企业定期、企业通知
                return fcAccDTO.getInterestInExp();//借利息支出
            } else if (InnerAccountConstant.FCACC_BANK_DEMAND.equals(fcAccDTO.getAccType()) ||
                    InnerAccountConstant.FCACC_LOAN.equals(fcAccDTO.getAccType()) ||
                    InnerAccountConstant.FCACC_BANK_BEGULAR.equals(fcAccDTO.getAccType())) {//银行活期、企业放款、银行定期
                return fcAccDTO.getInterestRecPay();//借应收利息户
            }
        }
        return null;
    }
    /**
     * <b>功能介绍：</b>获取贷方记账账号<br>
     * <b>日期：</b>2020年6月16日<br>
     * <b>作者：</b>liyh<br>
     * @version 1.0
     */
    public String getCreditAccout(FcAccVO fcAccDTO, boolean interestSign) {
        if (interestSign) {//计息
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAccDTO.getAccType())) {//企业活期
                return fcAccDTO.getBlnctrAcc();//贷企业活期
            } else if (InnerAccountConstant.FCACC_LOAN.equals(fcAccDTO.getAccType())) {//放款
                return fcAccDTO.getInterestInExp();//贷利息收入
            }
        } else {
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BANK_BORROW.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(fcAccDTO.getAccType())) {//企业活期、银行借款、企业定期、企业通知
                return fcAccDTO.getInterestRecPay();//贷应付利息
            } else if (InnerAccountConstant.FCACC_BANK_DEMAND.equals(fcAccDTO.getAccType()) ||
                    InnerAccountConstant.FCACC_LOAN.equals(fcAccDTO.getAccType()) ||
                    InnerAccountConstant.FCACC_BANK_BEGULAR.equals(fcAccDTO.getAccType())) {//银行活期、企业放款、银行定期
                return fcAccDTO.getInterestInExp();//贷利息收入
            }
        }
        return null;
    }
    /**
     * 自动销户
     *
     *1、复核余额不为零，为零提示
     *2、写销户记录，写记账日期
     *3、账户状态为销户D
     * @param account
     * @param tallyDate
     */
    public void cancelFcAcc(String account, Date tallyDate,ICurrentUser user){
        /**1、复核余额不为零，为零提示 **/
        FcOnlineLedgerVO onlineLedger=fcOnlineLedgerService.getEntityByAccount(account);
        if (onlineLedger == null) {
            /** 账号{0} 没有实时金额，存在异常账号数据，请联系管理员！*/
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_NOT_EXIST_ONLINE, account);
        }
        BigDecimal bal = onlineLedger.getCheckBalance();//复核余额
        if (bal.compareTo(BigDecimal.ZERO)!=0 ) {//复核余额不为零，为零提示
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_NOT_ZERO, account);
        }
        /**写销户记录，写记账日期**/
        fcAccWriteoffService.saveObject(account,null,tallyDate,user.getLoginName());
        /**3、账户状态为销户D**/
        FcAccVO acc = fcAccService.findFcAccByBlnctrAcc(account);
        if(acc!=null) {
            /**内部账户销户的记账日期不能小于账户的启用日期 */
            if(tallyDate.compareTo(acc.getAccStartDate())<0){
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_ACCSTARTDATE_TALLYTIME_COMPARE,
                        account, DateUtils.formatDate(tallyDate), DateUtils.formatDate(acc.getAccStartDate()));
            }
            acc.setAccState(InnerAccountConstant.ACC_STATE_DESTORY);
            fcAccService.update(acc);
        }
    }
    /**
     * 根据规则代码获取记账规则
     * @param netId 网点ID
     * @param sysRaceCode 记账规则代码
     * @return 返回记账规则信息
     */
    private FcBusinessRuleDefineDTO getAccBookRule(String netId, String sysRaceCode){
        Map<String, Object> map = new HashMap<>();
        map.put("netId", netId);
        map.put("sysRaceCode", sysRaceCode);
        return fcBusinessRuleDefineService.getBusinessRuleExp(map);
    }
    /**
     * 保存销户日志
     * @param acc
     * @param opType　操作类型
     * @param bizMsg　业务信息
     */
    private void saveLog(FcAccVO acc,String opType,String bizMsg,String loginName){
        FcAccLogDTO fcAccLog = new FcAccLogDTO();
        fcAccLog.setBizId(acc.getId());
        fcAccLog.setOpType(opType);
        fcAccLog.setAccType(acc.getAccType());
        fcAccLog.setBizMsg(bizMsg);
        fcAccLog.setAccount(acc.getBlnctrAcc());
        fcAccLog.setBizType(InnerAccountConstant.BIZ_TYPE_CANCEL);
        fcAccLogService.saveLog(fcAccLog,loginName);
    }
}
