package com.bytter.biz.inneraccount.service.impl;

import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.finacial.api.vo.FcOnlineLedgerVO;
import com.bytter.biz.inneraccount.service.FcAccCancelService;
import com.bytter.biz.inneraccount.service.FcAccManageService;
import com.bytter.biz.inneraccount.service.FcFreezeService;
import com.bytter.biz.inneraccount.vo.FcFreezeSearchListVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcAccPoolDTO;
import com.bytter.system.api.dto.FcFreezeDTO;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by zhangxb on 2017/5/22 0022.
 */
@Service("innerAccountServiceImpl")
@DubboService
@Transactional
public class InnerAccountServiceImpl implements IInnerAccountService {
    @DubboReference(check = false)
	private IFcAccService fcAccService;
	@Autowired
	private FcAccManageService fcAccSer;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFcSubjectService fcSubjectService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @Autowired
    private FcAccCancelService fcAccCancelService;
    @Autowired
    private FcFreezeService fcFreezeService;
    @DubboReference(check = false)
	private IAccountGrantService accGrantService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
    IFcOnlineLedgerService fcOnlineLedgerService;
    /**
     * 根据账号ID列表查询账号列表
     * @param ids
     * @return
     */
    @Override
    public List<FcAccDTO> listFcAccInfo(List<String> ids){
        List<String> accStates =new ArrayList<>();
        accStates.add(InnerAccountConstant.ACC_STATE_NORMAL);
        accStates.add(InnerAccountConstant.ACC_STATE_PART_FREEZE);
        return this.listFcAccInfo(ids,accStates);
    }
    /**
     * 根据账号ID列表查询账号列表
     * @param ids
     * @return
     */
    @Override
    public List<FcAccDTO> listFcAccInfo(List<String> ids, boolean containDelBankAcc){
        List<String> accStates =new ArrayList<>();
        accStates.add(InnerAccountConstant.ACC_STATE_NORMAL);
        accStates.add(InnerAccountConstant.ACC_STATE_PART_FREEZE);
        if(containDelBankAcc){
            accStates.add(InnerAccountConstant.ACC_STATE_DESTORY);
        }
        return this.listFcAccInfo(ids,accStates);
    }
    @Override
    public List<FcAccDTO> listFcAccForReceiptsInfo(List<String> ids){
        List<String> accStates =new ArrayList<>();
        accStates.add(InnerAccountConstant.ACC_STATE_NORMAL);
        accStates.add(InnerAccountConstant.ACC_STATE_PART_FREEZE);
        accStates.add(InnerAccountConstant.ACC_STATE_S);
        return this.listFcAccInfo(ids,accStates);
    }
    @Override
    public List<FcAccDTO> listFcAccForHavDel(List<String> ids){
        List<String> accStates =new ArrayList<>();
        accStates.add(InnerAccountConstant.ACC_STATE_NORMAL);
        accStates.add(InnerAccountConstant.ACC_STATE_PART_FREEZE);
        accStates.add(InnerAccountConstant.ACC_STATE_DESTORY);
        return this.listFcAccInfo(ids,accStates);
    }
    /**
     * 根据账号ID列表,查询账号列表
     * @param ids
     * @param accState
     * @return
     */
    @Override
    public List<FcAccDTO> listFcAccInfo(List<String> ids, List<String> accState){
        List<FcAccDTO> list=fcAccService.listFcAccInfo(ids,accState);
        for(FcAccDTO fcAcc:list){
            if(StringUtils.isNotEmpty(fcAcc.getBlnctrAcc())) {
                FcOnlineLedgerVO fcOnlineLedgerVO = fcOnlineLedgerService.getEntityByAccount(fcAcc.getBlnctrAcc());
                if(null != fcOnlineLedgerVO) {
                    if(!EmptyUtils.isEmpty(fcOnlineLedgerVO.getCheckBalance()) &&
                    !EmptyUtils.isEmpty(fcOnlineLedgerVO.getFreezeMoney()))
                    fcAcc.setFcBalance(fcOnlineLedgerVO.getCheckBalance().subtract(fcOnlineLedgerVO.getFreezeMoney()));
                }
            }
        }
        return list;
    }
    /**
     * 根据账号ID列表,查询账号列表（资金池）
     * @param ids
     * @param accState
     * @return
     */
    @Override
    public List<FcAccDTO> listFcAccInfoForPool(List<String> ids, List<String> accState){
        List<FcAccDTO> dtoList=fcAccService.listFcAccInfoForPool(ids,accState);
        List<FcAccDTO> list = BeanUtils.copyListProperties(dtoList,FcAccDTO.class);
        return list;
    }
    /**
     * 根据单位ID列表查询账号列表
     * @param corpIds
     * @param accState
     * @return
     */
    @Override
    public List<FcAccDTO> listFcAccInFoByCorpIds(List<String> corpIds, String accState){
        List<FcAccDTO> dtoList=fcAccService.listFcAccInFoByCorpIds(corpIds,accState);
        List<FcAccDTO> list = BeanUtils.copyListProperties(dtoList,FcAccDTO.class);
        return list;
    }
    /**
     * 根据单位ID列表查询资金池账号列表
     * @param corpIds
     * @param accState　　InnerAccountConstant.ACC_STATE_*
     * @return
     */
    @Override
    public List<FcAccDTO> listFcAccPoolByCorpIds(List<String> corpIds, String accState, String poolSign) {
        List<FcAccDTO> dtoList=fcAccService.listFcAccPoolByCorpIds(corpIds,accState,poolSign);
        List<FcAccDTO> list = BeanUtils.copyListProperties(dtoList,FcAccDTO.class);
        return list;
    }
    /**
     *  根据参数筛选 内部账号
     * @param curId
     * @param netId
     * @param corpId
     * @param subjectId
     * @return
     */
    @Override
    public List<FcAccDTO> listAccountByParams(String curId, String netId, String corpId, String subjectId){
        return listAccountByParams( curId, netId, corpId, subjectId, null);
    }
    /**
     *  根据参数筛选 内部账号
     * @param curId
     * @param netId
     * @param corpId
     * @param subjectId
     * @return
     */
    @Override

    public List<FcAccDTO> listAccountByParamsAndGrant(String curId, String netId, String corpId, String subjectId,String sysSubjectCode,String userId){
        List<String> accs = accGrantService.listByMultiVal(userId, corpId,null, null,
                AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
        List<com.bytter.system.api.vo.FcAccPlusVO> listVO = fcAccService.listAccountByParamsAndGrant( curId, netId, corpId, subjectId,sysSubjectCode, accs,null);
        List<FcAccDTO> list = BeanUtils.copyListProperties(listVO,FcAccDTO.class);
        return list;
    }
    /**
     *  根据参数筛选 内部账号
     * @param curId
     * @param netId
     * @param corpId
     * @return
     */
    @Override
    public List<FcAccDTO> listAccountByParams(String curId, String netId, String corpId){
        String [] accTypes =new String[5];
        accTypes[0] = InnerAccountConstant.FCACC_BUSINESS_ACCOUNT;
        accTypes[1] = InnerAccountConstant.FCACC_BANK_DEMAND;
        accTypes[2] = InnerAccountConstant.FCACC_INNER_ACCOUNT;
        accTypes[3] = InnerAccountConstant.FCACC_BUSINESS_BEGULAR;
        accTypes[4] = InnerAccountConstant.FCACC_BANK_BEGULAR;
        List<com.bytter.system.api.vo.FcAccListVO> listVO=fcAccService.listAccountByParams(accTypes,curId, netId, corpId);
        List<FcAccDTO> list = BeanUtils.copyListProperties(listVO,FcAccDTO.class);
        return list;
    }
    /**
     *  根据参数筛选 内部账号
     * @param curId
     * @param netId
     * @param corpId
     * @param subjectId
     * @return
     */
    @Override
    public List<FcAccDTO> listAccountByParams(String curId, String netId, String corpId, String subjectId, String endUnit){
        List<com.bytter.system.api.vo.FcAccPlusVO> listVO=fcAccService.listAccountByParams( curId, netId, corpId, subjectId,endUnit);
        List<FcAccDTO> list = BeanUtils.copyListProperties(listVO,FcAccDTO.class);
        return list;
    }
    /**
     * 根据账号和调整类型 改变利率
     * @param account
     * @param instrate
     * @param instrtType
     */
    @Override
    public void updateRate(String account, BigDecimal instrate, String instrtType) {
        FcAccVO fcAcc = fcAccService.findFcAccByBlnctrAcc(account);
        String accType = fcAcc.getAccType();
        String colName = "";
        switch (Integer.parseInt(instrtType)) {
            case 1:
                colName="interestRate";//~{U}3#~}
                break;
            case 2:
                colName="overdueRate";//~{SbFZ~}
                break;
            case 3:
                colName="overdueRate";//~{3,FZ~}=~{SbFZ~}
                break;
            case 4:
                colName="overdraftRate";//~{M8V'~}=~{LaG0~}
                break;
            case 5:
                colName="overdueRate";//~{P-6(~}=~{SbFZ92SCR;8vWV6N~}
                break;
            case 6:
                colName="overdraftRate";//~{LaG0~}=~{M8V'~}
                //modify by liyh 2013.7.16 ~{6(FZM(V*6TS&5DLaG0@{BJWV6NN*~}delayRate
                if(InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(accType) || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(accType)){
                    colName="delayRate";//~{LaG0~}
                }
                break;
            case 7:
                colName="delayRate";//~{U9FZ~}
                break;
            default:
                break;
        }
        fcAccService.updateRate(account, colName,instrate);
    }
    /**
     * 自动开户内部账号
     * @param fcAccDTO
     */
    @Override
    public String saveOrUpdate(FcAccDTO fcAccDTO, ICurrentUser user){
    	FcAccVO fcAcc = new FcAccVO();
        BeanUtils.copyNotNullProperties(fcAccDTO,fcAcc);
        String netId = fcAcc.getNetId();
        String curId = fcAcc.getCurId();
        String accType = fcAcc.getAccType();
        String subjectId = fcAcc.getSubjectId();
        //是否资金池默认为否
        if(StringUtils.isBlank(fcAcc.getPoolSign())){
            fcAcc.setPoolSign(AllocationConstant.POOL_SIGN_0);
        }
        //自动生成序列号
        String seqNum = fcAccService.createSequence(netId,curId,accType);
        //根据资金池标志生成内部账号生成法则
        int accAccNoRule = configService.getConfig("fcs","accAccnoRule").toInteger(1).intValue();
        //顺序号生成模式 1自动 0手动
        int seqGeneratingMode = configService.getConfig("fcs","seqGeneratingMode").toInteger(1).intValue();
        if(0 == seqGeneratingMode){
            if(StringUtils.isNotBlank(fcAcc.getBlnctrAcc())){
                int tempLength=4;
                if(1 == accAccNoRule){
                    tempLength = 3;
                }else if(2 == accAccNoRule){
                    tempLength = 4;
                }else if(3 == accAccNoRule){
                    tempLength = 6;
                }else{
                    tempLength = 4;
                }
                seqNum = fcAcc.getBlnctrAcc().substring(fcAcc.getBlnctrAcc().length()-tempLength,fcAcc.getBlnctrAcc().length());
            }
        }
        String netCode = corpService.get(netId).getCode();
        String subjectCode = fcSubjectService.findSubjectById(subjectId).getSubjectCode();
        String curCode = btCurrencyService.getObjectById(curId).getCurCode();
        if(StringUtils.isEmpty(netCode) || StringUtils.isEmpty(subjectCode)
                || StringUtils.isEmpty(curCode) || StringUtils.isEmpty(seqNum)  ){
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_AUTO_PROCESS);
        }
        StringBuilder account = new StringBuilder();
        //如果是生成规则：网点+币别+科目(科目不足6位补齐)+4位顺序号
        if(4 == accAccNoRule){
            if(StringUtils.isNotBlank(subjectCode)){
                subjectCode = subjectCode.trim() + "000000";
                subjectCode = subjectCode.substring(0,6);
            }
        }
        account.append(netCode).append(curCode).append(subjectCode).append(seqNum);
        //自动组合账号
        fcAcc.setBlnctrAcc(account.toString());
        //根据 autoCheck 自动开户 是否自动复核通过 true 是，false 否
        fcAccSer.saveOrUpdate(fcAcc,user,fcAccDTO.getAutoCheck());
        return account.toString();
    }
    @Override
    public void updateFcAcc(FcAccDTO fcAccDTO){
        FcAccVO fcAcc= fcAccService.getFcAcc(fcAccDTO.getId());
        BeanUtils.copyNotNullProperties(fcAccDTO,fcAcc);
        fcAccService.update(fcAcc);
    }
    /**
     * 根据账号获取内部账号信息
     * @param blnctrAcc
     * @return
     */
    @Override
    public FcAccDTO getByBlnctrAcc(String blnctrAcc) {
        return fcAccService.getAccDTOByAccount(blnctrAcc);
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
    @Override
    public void cancelFcAcc(String account, Date tallyDate, ICurrentUser user){
        fcAccCancelService.cancelFcAcc(account,tallyDate,user);
    }
    @Override
    public List<FcAccDTO> listAccountByMoreParams(String curId, String netId, String corpId, String subjectId,
                                                  String sysSubjectCode) {
        List<com.bytter.system.api.vo.FcAccPlusVO> listVO = fcAccService.listAccountByMoreParams(curId,netId,corpId,subjectId,sysSubjectCode);
        List<FcAccDTO> list = BeanUtils.copyListProperties(listVO,FcAccDTO.class);
        return list;
    }
    /**
     *
     * 查询网点冻结 到期账户
     * @param netId
     * @param endDate
     */
    @Override
    public List<FcFreezeDTO> getList(String netId, Date endDate){
        List<FcFreezeSearchListVO> listVO=fcFreezeService.getList( netId, endDate);
        List<FcFreezeDTO> list = BeanUtils.copyListProperties(listVO,FcFreezeDTO.class);
        return list;
    }
    @Override
    public List<FcAccDTO> listAccountByBankParams(String curId, String netId,
                                                  String corpId, String subjectId, String bankCode,String userId) {
        //根据联行号获取的银行类型信息
        BankTypeDTO dto = fcAccService.getInfoByBankCode(bankCode);
        if(EmptyUtils.isEmpty(dto)){
            throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_BTBANKTYPE_NULL, bankCode);
        }
        List<String> accs = accGrantService.listByMultiVal(userId , corpId,null,null,
                AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
        if(!EmptyUtils.isEmptyList(accs) && !EmptyUtils.isEmpty(dto)){
            List<com.bytter.system.api.vo.FcAccPlusVO> listVO = fcAccService.listAccountByParamsAndGrant( curId, netId, corpId, subjectId,null, accs,dto.getId());
            List<FcAccDTO> list = BeanUtils.copyListProperties(listVO,FcAccDTO.class);
            return list;
        }else{
            return new ArrayList<FcAccDTO>();
        }
    }
    @Override
    public List<FcAccPoolDTO> listAccountByBankParamsForPool(String curId, String netId,
                                                             String corpId, String subjectId, String bankCode, String userId) {
        List<String> accs = accGrantService.listByMultiVal(userId , corpId,null,null,
                AccountGrantConstant.POOLINNER_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
        if(!EmptyUtils.isEmptyList(accs)){
            List<com.bytter.system.api.vo.FcAccPlusVO> listVO = fcAccService.listAccountByParamsAndGrantForPool( curId, netId, corpId, subjectId,null, accs,bankCode);
            List<FcAccPoolDTO> list = BeanUtils.copyListProperties(listVO,FcAccPoolDTO.class);
            return list;
        }else{
            return new ArrayList<FcAccPoolDTO>();
        }
    }
    /**
     * 根据科目 判断是否存在使用的账号
     * @param subujectId
     * @return
     */
    @Override
    public boolean isExsits(String subujectId){
        return fcAccService.isExsits(subujectId);
    }
    /**
     * 根据单位集合 得到内部账户列表
     * @param corpIdList 单位ID集合
     * @param joinSql 拼接其他查询条件
     * @return 内部账号集合
     */
    @Override
    public List<FcAccDTO> getFcAccByCorpIdList(List<String> corpIdList, String joinSql){
        List<String> accStates =new ArrayList<>();
        accStates.add(InnerAccountConstant.ACC_STATE_NORMAL);
        accStates.add(InnerAccountConstant.ACC_STATE_PART_FREEZE);
        List<FcAccDTO> dtoList=fcAccService.getFcAccByCorpIdList(corpIdList, accStates, joinSql);
        List<FcAccDTO> list = BeanUtils.copyListProperties(dtoList,FcAccDTO.class);
        return list;
    }
    /**
     * 根据单位集合 得到内部账户列表
     * @param corpIdList 单位ID集合
     * @param joinSql 拼接其他查询条件
     * @param containDelBankAcc 是否包含已注销的账户
     * @return 内部账号集合
     */
    @Override
    public List<FcAccDTO> getFcAccByCorpIdList(List<String> corpIdList, String joinSql, boolean containDelBankAcc){
        List<String> accStates =new ArrayList<>();
        accStates.add(InnerAccountConstant.ACC_STATE_NORMAL);
        accStates.add(InnerAccountConstant.ACC_STATE_PART_FREEZE);
        if(containDelBankAcc){
            accStates.add(InnerAccountConstant.ACC_STATE_DESTORY);
        }
        List<FcAccDTO> dtoList=fcAccService.getFcAccByCorpIdList(corpIdList, accStates, joinSql);
        List<FcAccDTO> list = BeanUtils.copyListProperties(dtoList,FcAccDTO.class);
        return list;
    }
    /**
     * 根据单位集合 得到内部账户列表包含注销账号
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    @Override
    public List<FcAccDTO> getFcAccByCorpIdListContainDel(List<String> corpIdList, String joinSql) {
        List<String> accStates = new ArrayList<>();
        accStates.add(InnerAccountConstant.ACC_STATE_NORMAL);
        accStates.add(InnerAccountConstant.ACC_STATE_PART_FREEZE);
        //增加注销账号也可以查询
        accStates.add(InnerAccountConstant.ACC_STATE_DESTORY);
        List<FcAccDTO> dtoList = fcAccService.getFcAccByCorpIdList(corpIdList, accStates, joinSql);
        List<FcAccDTO> list = BeanUtils.copyListProperties(dtoList, FcAccDTO.class);
        return list;
    }
    /**
     * 根据单位集合 得到内部账户列表
     * @param corpIdList 单位ID集合
     * @param joinSql 拼接其他查询条件
     * @return 内部账号集合
     */
    @Override
    public List<String> getFcAccIdsByCorpIdList(List<String> corpIdList, String joinSql){
        List<String> accStates =new ArrayList<>();
        accStates.add(InnerAccountConstant.ACC_STATE_NORMAL);
        accStates.add(InnerAccountConstant.ACC_STATE_PART_FREEZE);
        List<FcAccDTO> dtoList=fcAccService.getFcAccByCorpIdList(corpIdList, accStates, joinSql);
        List<String> list = new ArrayList<>();
        for (FcAccDTO dto:dtoList) {
            list.add(dto.getId());
        }
        return list;
    }
    /**
     * 根据单位集合 得到内部账户列表
     * @param corpIdList 单位ID集合
     * @param joinSql 拼接其他查询条件
     * @return 内部账号集合
     */
    @Override
    public List<String> getFcAccsByCorpIdList(List<String> corpIdList, String joinSql){
        List<String> accStates =new ArrayList<>();
        accStates.add(InnerAccountConstant.ACC_STATE_NORMAL);
        accStates.add(InnerAccountConstant.ACC_STATE_PART_FREEZE);
        return fcAccService.getFcAccsByCorpIdList(corpIdList, accStates, joinSql);
    }
}
