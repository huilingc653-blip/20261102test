package com.bytter.biz.inneraccount.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.inneraccount.entity.BisVirtualAccExc;
import com.bytter.biz.inneraccount.entity.BtBankAccMessage;
import com.bytter.biz.inneraccount.entity.BtBankAccVirtual;
import com.bytter.core.utils.BytterAESUtils;
import com.bytter.biz.netbank.entity.BtBankAccUse;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.entity.FcReceiptType;
import com.bytter.biz.netbank.service.BtBankAccUseService;
import com.bytter.biz.netbank.service.FcBankAccGroupService;
import com.bytter.biz.netbank.service.FcReceiptTypeService;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.*;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.enums.BankInfoMessageEnum;
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.bytter.system.api.vo.BtBankAccCurVO;
import com.bytter.system.api.vo.BtBankAccHandleVO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@WorkFlow(NisWorkFlowConstant.BT_BANKACC_ACC_HANDLE)
@Service("btBankAccHandleService")
@DubboService(interfaceClass = IWorkFlowListener.class, group = "btbankacchandle")
@Transactional
public class BtBankAccHandleService extends AbstractService implements IExcelVerifyHandler<BtBankAccVO>, ITaskService, IWorkFlowListener {
    @DubboReference(check = false)
    private IBtBankAccHandleService btBankAccHandleService;//转换成system的接口
    @Autowired(required=false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;
    @DubboReference(check = false)
    private IInputBankInfoService inputBankInfoService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Autowired
    private BtBankAccMessageService btBankAccMessageService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private ILicenseService licenseService;
    @DubboReference(check = false)
    private IBtBankAccCommonService btBankAccCommonService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IBtBankService bankService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @DubboReference(check = false)
    private IAccountGrantService iAccountGrantService;
    @DubboReference(check = false)
    private IBtSwiftInfoService btSwiftInfoService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IBtBankAccCurService btBankAccCurService;
    @Autowired
    private BtBankAccVirtualService btBankAccVirtualService;
    @Autowired
    private BisVirtualAccExcService bisVirtualAccExcService;
    @DubboReference(check = false)
    private IBisBifInitService iBisBifInitService;
    @DubboReference(check = false)
    private IBtBankAccAppService btBankAccAppService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IBtAccNatureService iBtAccNatureService;
    @DubboReference(check = false)
    private IBisAccAttributeService iBisAccAttributeService;
    @Autowired
    private FcBankAccGroupService fcBankAccGroupService;
    @Autowired
    private FcReceiptTypeService fcReceiptTypeService;
    @Autowired
    private BtBankAccUseService btBankAccUseService;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(BtBankAccDTO entity) {
        this.btBankAccService.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        this.btBankAccService.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(BtBankAccDTO entity) {
        // 初始化联行号信息
        this.bankService.saveBtBank(entity.getBankCode(), entity.getBankTypeId());
        this.btBankAccService.save(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BtBankAccDTO entity) {
        // 初始化联行号信息
        this.bankService.saveBtBank(entity.getBankCode(), entity.getBankTypeId());
        this.btBankAccService.update(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(BtBankAccDTO entity) {
        // 初始化联行号信息
        this.bankService.saveBtBank(entity.getBankCode(), entity.getBankTypeId());
        this.btBankAccService.update(entity);
    }
    public BtBankAccDTO getBtBankAccVOById(String id){
        BtBankAccDTO accDTO= btBankAccCommonService.getBtBankAccVOById(id);
        // CorpDTO corp=this.corpService.getTopCorpByCorpId(accDTO.getCorpId());
        // accDTO.setCorpName(corp.getName());
        BisBifInitDTO bisBifInitDTO= iBisBifInitService.getObjectById(accDTO.getBifCode());
        if(null !=bisBifInitDTO){
            accDTO.setBifName(bisBifInitDTO.getName());
        }
        accDTO.setElectricBillName(dictService.get("sf_yn",accDTO.getElectricBill()));
        return accDTO;
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder accSql = new StringBuilder();
        String accCorpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACC_QUERY, user.getId()), "acc.corp_id",
                true);
        accSql.append(" and ").append(accCorpAuthSql);
        StringBuilder appSql = new StringBuilder();
        String appCorpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY, user.getId()), "acc.corp_id",
                true);
        appSql.append(" and ").append(appCorpAuthSql);
        DataTablesOutput out = btBankAccHandleService.getDataTablesUnion(dataTablesInput, appSql.toString(), accSql.toString(), BtBankAccConstant.BUSTYPE_1);
        //获取所有的单据类型
        List<FcReceiptType> fcReceiptList = fcReceiptTypeService.getValidFcReceiptType();
        Map<String,String> fcReceiptMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(fcReceiptList)){
            fcReceiptMap = fcReceiptList.stream().collect(Collectors.toMap(FcReceiptType::getReceiptCode, FcReceiptType::getReceiptName));
        }
        // 所有得账号用途
        List<BtBankAccUse> btBankAccUses = btBankAccUseService.listAll();
        Map<String,String> useMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(btBankAccUses)){
            useMap = btBankAccUses.stream().collect(Collectors.toMap(BtBankAccUse::getUseCode, BtBankAccUse::getUseName));
        }
        List<BtBankAccHandleVO> list = out.getData();
        for(BtBankAccHandleVO entity:list){
            String name = "";
            //设置单据类型
            if(EmptyUtils.isNotEmpty(entity.getReceiptCode())){
                for(String code : entity.getReceiptCode().split( ",")){
                    if(fcReceiptMap.containsKey(code)){
                        if(ObjectUtil.isEmpty(name)) {
                            name = fcReceiptMap.get(code);
                        }else{
                            name += ","+fcReceiptMap.get(code);
                        }
                    }
                }
            }
            entity.setReceiptName(name);

            // 设置账号用途
            String accUseName = "";
            if (ObjectUtil.isNotEmpty(entity.getAccUseCode())){
                for (String code :entity.getAccUseCode().split( ",")){
                    if(useMap.containsKey(code)){
                        if(ObjectUtil.isEmpty(accUseName)) {
                            accUseName = useMap.get(code);
                        }else{
                            accUseName += ","+useMap.get(code);
                        }
                    }
                }
            }
            entity.setAccUseName(accUseName);
        }
        return out;
    }
    public List<BtBankAccVO> getBankaccList() {
        return btBankAccHandleService.getBankaccList();
    }
    /**
     * 银行账号新增
     *
     * @param btBankAccVO
     * @throws Exception
     */
    public void toSave(BtBankAccVO btBankAccVO, ICurrentUser user, String status) {
        BtBankAccDTO btBankAcc = new BtBankAccDTO();
        org.springframework.beans.BeanUtils.copyProperties(btBankAccVO, btBankAcc);
        // 对登录密码和支付密码加密保存
        try {
            if (ObjectUtil.isNotEmpty(btBankAcc.getLoginPassword())){
                btBankAcc.setLoginPassword(BytterAESUtils.encrypt(btBankAcc.getLoginPassword()));
            }
            if (ObjectUtil.isNotEmpty(btBankAcc.getPayPassword())){
                btBankAcc.setPayPassword(BytterAESUtils.encrypt(btBankAcc.getPayPassword()));
            }
        }catch (Exception e){
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BTBANKACC_ENCRYPTION_FAILURE);
        }

        if (!this.btBankAccCommonService.isBankAcc(btBankAcc.getBankAcc())) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKACC_UNIQUE,BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKACC_UNIQUE.getCode(),btBankAcc.getBankAcc());
        }
        if (!btBankAccService.isBtBankAccCountLicense()) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BTBANKACC_ACCOUNT_LICENSE, licenseService.getBankAccountNumber().toString());
        }
        String curIdStr = btBankAcc.getCurId();
        //{0}币别不能为空{1} 需要用空字符串替换
        if (EmptyUtils.isBlank(curIdStr)) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,"","");
        }
        String[] curIds = curIdStr.split(",");
        if (EmptyUtils.isEmptyArray(curIds)) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,"","");
        }
        if (curIds.length < 1 || "".equals(curIdStr.trim())) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,"","");
        }
        if (!EmptyUtils.isBlank(btBankAcc.getBifCode())) {
            BisBifInitDTO bisBifInitDTO = this.bisBifInitService.getObjectById(btBankAcc.getBifCode());
            if (!EmptyUtils.isEmpty(bisBifInitDTO) && !bisBifInitDTO.getBankTypeId().equalsIgnoreCase(btBankAcc.getBankTypeId())) {
                throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BIFINIT_EQ_BANKTYPEID);
            }
        }
        btBankAcc.setCreateBy(user.getLoginName());
        btBankAcc.setCreateDate(DateUtils.getDate());
        btBankAcc.setStatus(BtBankAccConstant.STATUE_WAIT_CHECK);
        String bindingBankAcc = btBankAcc.getBindingBankAcc();
        btBankAcc.setBindingBankAcc("");
        String btBankAccId = this.btBankAccService.save(btBankAcc);
        logger.info("新增银行账号 {}", JsonUtils.toJSONString(btBankAcc));
        if (StringUtils.isNotBlank(btBankAccId)){
            btBankAcc.setId(btBankAccId);
        }
        this.saveAccCur(btBankAcc, curIds, user);
        //如果绑定账号不为空则保存绑定账号
        if(EmptyUtils.isNotEmpty(bindingBankAcc)){
            //先删除中间表，再插入
            btBankAccService.deleteBybankId(btBankAccId);
            String[] bindingBankAccArr = bindingBankAcc.split(CommonConstant.COMMA_SPLIT);
            if(!EmptyUtils.isEmpty(bindingBankAccArr)){
                for(int i=0;i<bindingBankAccArr.length;i++){
                    btBankAccService.saveNisBindingBankac(btBankAccId,bindingBankAccArr[i]);
                }
            }
        }
        //新增账户自动授权
        AccountGrantDTO grantDTO = new AccountGrantDTO();
        grantDTO.setAccountId(btBankAcc.getId());
        grantDTO.setUserId(user.getId());
        grantDTO.setCorpId(btBankAcc.getCorpId());
        if(!EmptyUtils.isEmpty(btBankAcc.getIsOnline()) && BtBankAccConstant.ISONLINE_0.equals(btBankAcc.getIsOnline())){
            grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.OFFLINE_BANK_TYPE));
        }else{
            grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.ONLINE_BANK_TYPE));
        }
        iAccountGrantService.saveAccountGrant(grantDTO);
        //招行 虚拟户 线上办理 保存虚拟信息
       /* if (BtBankAccConstant.ACCTYPE_09.equals(btBankAccVO.getAccType()) && CommonConstant.YES.equals(btBankAccVO.getIsOnlineHandle())
                && BtBankAccConstant.BIS_BIF_CODE_CMB.equals(btBankAccVO.getBifCode())) {
            if (!EmptyUtils.isEmptyList(btBankAccVO.getBankAccVirtual())) {
                List<BtBankAccVirtual> listDtl = new ArrayList<>();
                for (com.bytter.system.api.vo.BtBankAccVirtual bankAccVirtual : btBankAccVO.getBankAccVirtual()) {
                    if (StringUtils.isNotBlank(bankAccVirtual.getVirtualNo())) {
                        bankAccVirtual.setBankAccId(btBankAcc.getId());
                        listDtl.add(bankAccVirtual);
                    }
                }
                btBankAccVirtualService.batchSave(listDtl);
            }
        }*/
        //开启审批
         getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_ACC_HANDLE, btBankAcc.getId(), btBankAcc, btBankAcc.getCorpId(), user.getId());
        //生成日志
        if (BtBankAccConstant.STATUE_TEMP.equals(status)) {
            btBankAccMessageService.saveMessage(btBankAcc.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_ADD, "", BtBankAccMessageConstant.BUS_TYPE_APPHANDLE, user);
        } else {
            btBankAccMessageService.saveMessage(btBankAcc.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_APPHANDLE, user);
        }
    }
    /**
     * 银行账号开户
     *
     * @param btBankAccVO
     * @param user
     */
    @Deprecated
    public void toModify(BtBankAccVO btBankAccVO, ICurrentUser user) {
        if ("0".equals(btBankAccVO.getIsFrom())) {
            BtBankAccDTO btBankAcc = new BtBankAccDTO();
            org.springframework.beans.BeanUtils.copyProperties(btBankAccVO, btBankAcc);
            BtBankAccAppDTO btBankAccApp = btBankAccAppService.getById(btBankAccVO.getId());
	        if (!btBankAccCommonService.isBankAcc(btBankAcc.getBankAcc())) {
	            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKACC_UNIQUE);
	        }
	        if (!btBankAccService.isBtBankAccCountLicense()) {
	            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BTBANKACC_ACCOUNT_LICENSE, licenseService.getBankAccountNumber().toString());
	        }
	        String curIdStr = btBankAcc.getCurId();
	        if (EmptyUtils.isBlank(curIdStr)) {
	            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,"","");
	        }
	        String[] curIds = curIdStr.split(",");
	        if (EmptyUtils.isEmptyArray(curIds)) {
	            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,"","");
	        }
	        if (!EmptyUtils.isBlank(btBankAcc.getBifCode())) {
	            BisBifInitDTO bisBifInitDTO = this.bisBifInitService.getObjectById(btBankAcc.getBifCode());
	            if (!EmptyUtils.isEmpty(bisBifInitDTO) && !btBankAcc.getBankTypeId().equalsIgnoreCase(bisBifInitDTO.getBankTypeId())) {
	                throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BIFINIT_EQ_BANKTYPEID);
	            }
	        }
	        //办理完成状态
	        btBankAccApp.setStatus(BtBankAccConstant.STATUE_CANCELLATION);
	        btBankAccApp.setBankAcc(btBankAcc.getBankAcc());
	        btBankAccApp.setUpdateBy(user.getLoginName());
	        btBankAccApp.setUpdateDate(DateUtils.getDate());
	        btBankAcc.setStatus(BtBankAccConstant.STATUE_WAIT_CHECK);
	        btBankAcc.setCreateBy(user.getLoginName());
	        btBankAcc.setCreateDate(DateUtils.getDate());
            btBankAccAppService.saveOrUpdate(btBankAccApp);
	        btBankAcc.setId("");
            String bindingBankAcc = btBankAcc.getBindingBankAcc();
            btBankAcc.setBindingBankAcc("");
            String btBankAccId = this.btBankAccService.save(btBankAcc);
            if (StringUtils.isNotBlank(btBankAccId)){
                btBankAcc.setId(btBankAccId);
            }
	        //新增账户自动授权
	        AccountGrantDTO grantDTO = new AccountGrantDTO();
	        grantDTO.setAccountId(btBankAcc.getId());
	        //授权给申请人
	        UserDTO createBy = userService.getByLoginName(btBankAccApp.getCreateBy());
	        grantDTO.setUserId(createBy.getId());
	        grantDTO.setCorpId(btBankAcc.getCorpId());
	        if (!EmptyUtils.isEmpty(btBankAcc.getIsOnline()) && BtBankAccConstant.ISONLINE_0.equals(btBankAcc.getIsOnline())) {
	            grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.OFFLINE_BANK_TYPE));
	        } else {
	            grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.ONLINE_BANK_TYPE));
	        }
            iAccountGrantService.saveAccountGrant(grantDTO);
            this.updateAccCur(btBankAcc, curIds, user);
//            this.saveAccCur(btBankAcc, curIds, user);
            //如果绑定账号不为空则保存绑定账号
            if(EmptyUtils.isNotEmpty(bindingBankAcc)){
                //先删除中间表，再插入
                btBankAccService.deleteBybankId(btBankAccVO.getId());
                String[] bindingBankAccArr = bindingBankAcc.split(CommonConstant.COMMA_SPLIT);
                if(!EmptyUtils.isEmpty(bindingBankAccArr)){
                    for(int i=0;i<bindingBankAccArr.length;i++){
                        btBankAccService.saveNisBindingBankac(btBankAccId,bindingBankAccArr[i]);
                    }
                }
            }
	        //生成日志
	        btBankAccMessageService.saveMessage(btBankAcc.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_APPHANDLE, user);
	        // 消息提醒
	        notificationService.complete(NotificationConstant.NETBANK.BANKACC_REQ_HANDLE, btBankAccApp.getId(), user.getId());
	        //开启审批
	         getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_ACC_HANDLE, btBankAcc.getId(), btBankAcc, btBankAcc.getCorpId(), user.getId());
        } else {
            BtBankAccDTO btBankAcc = new BtBankAccDTO();
            org.springframework.beans.BeanUtils.copyProperties(btBankAccVO, btBankAcc);
            try {
                if (ObjectUtil.isNotEmpty(btBankAcc.getLoginPassword())){
                    btBankAcc.setLoginPassword(BytterAESUtils.encrypt(btBankAcc.getLoginPassword()));
                }
                if (ObjectUtil.isNotEmpty(btBankAcc.getPayPassword())){
                    btBankAcc.setPayPassword(BytterAESUtils.encrypt(btBankAcc.getPayPassword()));
                }
            }catch (Exception e){
                throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BTBANKACC_ENCRYPTION_FAILURE);
            }
            BtBankAccDTO t = btBankAccService.getBtBankAcc(btBankAcc.getId());
            if ((!t.getBankAcc().equals(btBankAcc.getBankAcc())) && !btBankAccCommonService.isBankAcc(btBankAcc.getBankAcc())) {
                throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKACC_UNIQUE);
            }
            // 对币别字符串进行判断,当curIdStr为空时,curIdStr.split方法抛异常
            String curIdStr = btBankAcc.getCurId();
            if (EmptyUtils.isBlank(curIdStr)) {
                throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,"","");
            }
            String[] curIds = curIdStr.split(",");
            if (EmptyUtils.isEmptyArray(curIds)) {
                throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,"","");
            }
            if (!EmptyUtils.isBlank(btBankAcc.getBifCode())) {
                BisBifInitDTO bisBifInitDTO = this.bisBifInitService.getObjectById(btBankAcc.getBifCode());
                if (!EmptyUtils.isEmpty(bisBifInitDTO) && !bisBifInitDTO.getBankTypeId().equalsIgnoreCase(btBankAcc.getBankTypeId())) {
                    throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BIFINIT_EQ_BANKTYPEID);
                }
            }
            List<String> curList = Arrays.asList(curIds);
            // 生成修改日志
            String msg = buildUpdateMsg(btBankAcc,t);
            t.setCurList(curList);
            //默认父账号标识等信息不修改
            BeanUtils.copyNotNullProperties(btBankAcc, t, new String[]{"parentSign"});
            t.setUpdateBy(user.getLoginName());
            t.setUpdateDate(DateUtils.getDate());
            t.setStatus(BtBankAccConstant.STATUE_WAIT_CHECK);
            //控制按钮，清空年检时间
            if (EmptyUtils.isEmpty(btBankAccVO.getCheckExpireDate())) {
                t.setCheckExpireDate(null);
            }
            String bindingBankAcc = btBankAcc.getBindingBankAcc();
            t.setBindingBankAcc("");
            this.saveOrUpdate(t);
            this.updateAccCur(btBankAcc, curIds, user);
//            this.saveAccCur(btBankAcc, curIds, user);
            //如果绑定账号不为空则保存绑定账号
            if(EmptyUtils.isNotEmpty(bindingBankAcc)){
                //先删除中间表，再插入
                btBankAccService.deleteBybankId(btBankAccVO.getId());
                String[] bindingBankAccArr = bindingBankAcc.split(CommonConstant.COMMA_SPLIT);
                if(!EmptyUtils.isEmpty(bindingBankAccArr)){
                    for(int i=0;i<bindingBankAccArr.length;i++){
                        btBankAccService.saveNisBindingBankac(t.getId(),bindingBankAccArr[i]);
                    }
                }
            }
            //根据在线标志更新账号授权类型
            String accountType = AccountGrantConstant.ONLINE_BANK_TYPE;
            if(CommonConstant.NO.equals(btBankAcc.getIsOnline())){
                accountType = AccountGrantConstant.OFFLINE_BANK_TYPE;
            }
            iAccountGrantService.updateAccountTypeByAccount(btBankAcc.getId(), accountType);
            //招行 虚拟户 线上办理 保存虚拟信息
            btBankAccVirtualService.deleteByBankAccId(t.getId());
            if (BtBankAccConstant.ACCTYPE_09.equals(btBankAccVO.getAccType()) && CommonConstant.YES.equals(btBankAccVO.getIsOnlineHandle())
                    && BtBankAccConstant.BIS_BIF_CODE_CMB.equals(btBankAccVO.getBifCode())) {
                if (!EmptyUtils.isEmptyList(btBankAccVO.getBankAccVirtual())) {
                    List<BtBankAccVirtual> listDtl = new ArrayList<>();
                    /*for (BtBankAccVirtual bankAccVirtual : btBankAccVO.getBankAccVirtual()) {
                        if (StringUtils.isNotBlank(bankAccVirtual.getVirtualNo())) {
                            bankAccVirtual.setBankAccId(btBankAcc.getId());
                            listDtl.add(bankAccVirtual);
                        }
                    }*/
                    btBankAccVirtualService.batchSave(listDtl);
                }
            } else {
            }
            //开启审批
             getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_ACC_HANDLE, t.getId(), t, t.getCorpId(), user.getId());
            //生成日志
            btBankAccMessageService.saveMessage(btBankAcc.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, msg, BtBankAccMessageConstant.BUS_TYPE_APPHANDLE, user);
        }
    }

    private String buildUpdateMsg(BtBankAccDTO newObj, BtBankAccDTO oldObj) {
        String msg = "";
        String temp = "%s：%s更新为%s；";
        // 账户名称
        if (ObjectUtil.isNotEmpty(oldObj.getAccName()) && ObjectUtil.isNotEmpty(newObj.getAccName())){
            if (!oldObj.getAccName().equals(newObj.getAccName())){
                String oldName = oldObj.getAccName();
                String newName = newObj.getAccName();
                msg += String.format(temp,"账户名称",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getAccName()) && ObjectUtil.isEmpty(newObj.getAccName())){
                String oldName = oldObj.getAccName();
                msg += String.format(temp,"账户名称",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getAccName()) && ObjectUtil.isNotEmpty(newObj.getAccName())){
                String newName = newObj.getAccName();
                msg += String.format(temp,"账户名称","空",newName);
            }
        }
        // 币别
        if(StringUtils.isNotBlank(newObj.getCurId())){
            List<String> curList = Arrays.asList(newObj.getCurId().split(","));
            List<BtBankAccCurDTO> accCurList = btBankAccCurService.getBankAccCurByBankAccId(oldObj.getId());
            if (!ObjectUtil.isEmpty(accCurList) && !ObjectUtil.isEmpty(curList)){
                List<String> oldCurList = accCurList.stream().map(BtBankAccCurDTO::getCurId)
                        .collect(Collectors.toList());
                boolean areElementsEqual = new TreeSet<String>(curList).equals(new TreeSet<String>(oldCurList));
                if (!areElementsEqual){
                    Map<String, String> curMap = btCurrencyService.getCurrencyNameByIds(curList);
                    Map<String, String> oldCurMap = btCurrencyService.getCurrencyNameByIds(oldCurList);
                    String curValues = curMap.values().stream()
                            .collect(Collectors.joining(","));
                    String oldValues = oldCurMap.values().stream()
                            .collect(Collectors.joining(","));
                    msg += String.format(temp,"币别",oldValues,curValues);
                }
            }
        }
        // 境内/境外账号 isDomesticBank
        if (ObjectUtil.isNotEmpty(oldObj.getIsDomesticBank()) && ObjectUtil.isNotEmpty(newObj.getIsDomesticBank())){
            if (!oldObj.getIsDomesticBank().equals(newObj.getIsDomesticBank())){
                String oldName = CommonConstant.YES.equals(oldObj.getIsDomesticBank())?"境内":"境外";
                String newName = CommonConstant.YES.equals(newObj.getIsDomesticBank())?"境内":"境外";
                msg += String.format(temp,"境内/境外账号",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getIsDomesticBank()) && ObjectUtil.isEmpty(newObj.getIsDomesticBank())){
                String oldName = CommonConstant.YES.equals(oldObj.getIsDomesticBank())?"境内":"境外";
                msg += String.format(temp,"境内/境外账号",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getIsDomesticBank()) && ObjectUtil.isNotEmpty(newObj.getIsDomesticBank())){
                String newName = CommonConstant.YES.equals(newObj.getIsDomesticBank())?"境内":"境外";
                msg += String.format(temp,"境内/境外账号","空",newName);
            }
        }
        // 是否离岸账号 isOffshoreAccount
        if (ObjectUtil.isNotEmpty(oldObj.getIsOffshoreAccount()) && ObjectUtil.isNotEmpty(newObj.getIsOffshoreAccount())){
            if (!oldObj.getIsOffshoreAccount().equals(newObj.getIsOffshoreAccount())){
                String oldName = CommonConstant.YES.equals(oldObj.getIsOffshoreAccount())?"是":"否";
                String newName = CommonConstant.YES.equals(newObj.getIsOffshoreAccount())?"是":"否";
                msg += String.format(temp,"是否离岸账号",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getIsOffshoreAccount()) && ObjectUtil.isEmpty(newObj.getIsOffshoreAccount())){
                String oldName = CommonConstant.YES.equals(oldObj.getIsOffshoreAccount())?"是":"否";
                msg += String.format(temp,"是否离岸账号",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getIsOffshoreAccount()) && ObjectUtil.isNotEmpty(newObj.getIsOffshoreAccount())){
                String newName = CommonConstant.YES.equals(newObj.getIsOffshoreAccount())?"是":"否";
                msg += String.format(temp,"是否离岸账号","空",newName);
            }
        }
        // 开户行
        if (ObjectUtil.isNotEmpty(oldObj.getBankName()) && ObjectUtil.isNotEmpty(newObj.getBankName())){
            if (!oldObj.getBankName().equals(newObj.getBankName())){
                String oldName = oldObj.getBankName();
                String newName = newObj.getBankName();
                msg += String.format(temp,"开户行",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getBankName()) && ObjectUtil.isEmpty(newObj.getBankName())){
                String oldName = oldObj.getBankName();
                msg += String.format(temp,"开户行",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getBankName()) && ObjectUtil.isNotEmpty(newObj.getBankName())){
                String newName = newObj.getBankName();
                msg += String.format(temp,"开户行","空",newName);
            }
        }
        // 账户性质
        if (ObjectUtil.isNotEmpty(oldObj.getNatureId()) && ObjectUtil.isNotEmpty(newObj.getNatureId())){
            if (!oldObj.getNatureId().equals(newObj.getNatureId())){
                Map<String,String> idNameMap = loadNatureMap();
                String oldName = idNameMap.get(oldObj.getNatureId());
                String newName = idNameMap.get(newObj.getNatureId());
                msg += String.format(temp,"账户性质",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getNatureId()) && ObjectUtil.isEmpty(newObj.getNatureId())){
                Map<String,String> idNameMap = loadNatureMap();
                String oldName = idNameMap.get(oldObj.getNatureId());
                msg += String.format(temp,"账户性质",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getNatureId()) && ObjectUtil.isNotEmpty(newObj.getNatureId())){
                Map<String,String> idNameMap = loadNatureMap();
                String newName = idNameMap.get(newObj.getNatureId());
                msg += String.format(temp,"账户性质","空",newName);
            }
        }
        // 账户属性 attributeId
        if (ObjectUtil.isNotEmpty(oldObj.getAttributeId()) && ObjectUtil.isNotEmpty(newObj.getAttributeId())){
            if (!oldObj.getAttributeId().equals(newObj.getAttributeId())){
                Map<String,String> idNameMap = loadAttributeMap();
                String oldName = idNameMap.get(oldObj.getAttributeId());
                String newName = idNameMap.get(newObj.getAttributeId());
                msg += String.format(temp,"账户属性",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getAttributeId()) && ObjectUtil.isEmpty(newObj.getAttributeId())){
                Map<String,String> idNameMap = loadAttributeMap();
                String oldName = idNameMap.get(oldObj.getAttributeId());
                msg += String.format(temp,"账户属性",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getAttributeId()) && ObjectUtil.isNotEmpty(newObj.getAttributeId())){
                Map<String,String> idNameMap = loadAttributeMap();
                String newName = idNameMap.get(newObj.getAttributeId());
                msg += String.format(temp,"账户属性","空",newName);
            }
        }
        // 账户类别 accType
        if (ObjectUtil.isNotEmpty(oldObj.getAccType()) && ObjectUtil.isNotEmpty(newObj.getAccType())){
            if (!oldObj.getAccType().equals(newObj.getAccType())){
                String oldName = dictService.getByName("btBankAcc.accType", oldObj.getAccType());
                String newName = dictService.getByName("btBankAcc.accType", newObj.getAccType());
                msg += String.format(temp,"账户类别",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getAccType()) && ObjectUtil.isEmpty(newObj.getAccType())){
                String oldName = dictService.getByName("btBankAcc.accType", oldObj.getAccType());
                msg += String.format(temp,"账户类别",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getAccType()) && ObjectUtil.isNotEmpty(newObj.getAccType())){
                String newName = dictService.getByName("btBankAcc.accType", newObj.getAccType());
                msg += String.format(temp,"账户类别","空",newName);
            }
        }
        // 接口代码
        if (ObjectUtil.isNotEmpty(oldObj.getBifCode()) && ObjectUtil.isNotEmpty(newObj.getBifCode())){
            if (!oldObj.getBifCode().equals(newObj.getBifCode())){
                Map<String,String> idNameMap = loadBifMap();
                String oldName = idNameMap.get(oldObj.getBifCode());
                String newName = idNameMap.get(newObj.getBifCode());
                msg += String.format(temp,"接口代码",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getBifCode()) && ObjectUtil.isEmpty(newObj.getBifCode())){
                Map<String,String> idNameMap = loadBifMap();
                String oldName = idNameMap.get(oldObj.getBifCode());
                msg += String.format(temp,"接口代码",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getBifCode()) && ObjectUtil.isNotEmpty(newObj.getBifCode())){
                Map<String,String> idNameMap = loadBifMap();
                String newName = idNameMap.get(newObj.getBifCode());
                msg += String.format(temp,"接口代码","空",newName);
            }
        }
        // 是否资金池账号 isCapitalPool
        if (ObjectUtil.isNotEmpty(oldObj.getIsCapitalPool()) && ObjectUtil.isNotEmpty(newObj.getIsCapitalPool())){
            if (!oldObj.getIsCapitalPool().equals(newObj.getIsCapitalPool())){
                String oldName = CommonConstant.YES.equals(oldObj.getIsCapitalPool())?"是":"否";
                String newName = CommonConstant.YES.equals(newObj.getIsCapitalPool())?"是":"否";
                msg += String.format(temp,"是否资金池账号",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getIsCapitalPool()) && ObjectUtil.isEmpty(newObj.getIsCapitalPool())){
                String oldName = CommonConstant.YES.equals(oldObj.getIsCapitalPool())?"是":"否";
                msg += String.format(temp,"是否资金池账号",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getIsCapitalPool()) && ObjectUtil.isNotEmpty(newObj.getIsCapitalPool())){
                String newName = CommonConstant.YES.equals(newObj.getIsCapitalPool())?"是":"否";
                msg += String.format(temp,"是否资金池账号","空",newName);
            }
        }
        // 开户日期 regDate
        if (ObjectUtil.isNotEmpty(oldObj.getRegDate()) && ObjectUtil.isNotEmpty(newObj.getRegDate())){
            if (!DateUtils.formatDate(oldObj.getRegDate()).equals(DateUtils.formatDate(newObj.getRegDate()))){
                msg += String.format(temp,"开户日期",DateUtils.formatDate(oldObj.getRegDate()),DateUtils.formatDate(newObj.getRegDate()));
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getRegDate()) && ObjectUtil.isEmpty(newObj.getRegDate())){
                msg += String.format(temp,"开户日期",DateUtils.formatDate(oldObj.getRegDate()),"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getRegDate()) && ObjectUtil.isNotEmpty(newObj.getRegDate())){
                msg += String.format(temp,"开户日期","空",DateUtils.formatDate(newObj.getRegDate()));
            }
        }
        // 是否上线 isOnline
        if (ObjectUtil.isNotEmpty(oldObj.getIsOnline()) && ObjectUtil.isNotEmpty(newObj.getIsOnline())){
            if (!oldObj.getIsOnline().equals(newObj.getIsOnline())){
                String oldName = CommonConstant.YES.equals(oldObj.getIsOnline())?"是":"否";
                String newName = CommonConstant.YES.equals(newObj.getIsOnline())?"是":"否";
                msg += String.format(temp,"是否上线",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getIsOnline()) && ObjectUtil.isEmpty(newObj.getIsOnline())){
                String oldName = CommonConstant.YES.equals(oldObj.getIsOnline())?"是":"否";
                msg += String.format(temp,"是否上线",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getIsOnline()) && ObjectUtil.isNotEmpty(newObj.getIsOnline())){
                String newName = CommonConstant.YES.equals(newObj.getIsOnline())?"是":"否";
                msg += String.format(temp,"是否上线","空",newName);
            }
        }
        // 有效标志 validSign
        if (ObjectUtil.isNotEmpty(oldObj.getValidSign()) && ObjectUtil.isNotEmpty(newObj.getValidSign())){
            if (!oldObj.getValidSign().equals(newObj.getValidSign())){
                String oldName = CommonConstant.YES.equals(oldObj.getValidSign())?"是":"否";
                String newName = CommonConstant.YES.equals(newObj.getValidSign())?"是":"否";
                msg += String.format(temp,"有效标志",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getValidSign()) && ObjectUtil.isEmpty(newObj.getValidSign())){
                String oldName = CommonConstant.YES.equals(oldObj.getValidSign())?"是":"否";
                msg += String.format(temp,"有效标志",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getValidSign()) && ObjectUtil.isNotEmpty(newObj.getValidSign())){
                String newName = CommonConstant.YES.equals(newObj.getValidSign())?"是":"否";
                msg += String.format(temp,"有效标志","空",newName);
            }
        }
        // 账户年检到期日 checkExpireDate
        if (ObjectUtil.isNotEmpty(oldObj.getCheckExpireDate()) && ObjectUtil.isNotEmpty(newObj.getCheckExpireDate())){
            if (!DateUtils.formatDate(oldObj.getCheckExpireDate()).equals(DateUtils.formatDate(newObj.getCheckExpireDate()))){
                msg += String.format(temp,"账户年检到期日",DateUtils.formatDate(oldObj.getCheckExpireDate()),DateUtils.formatDate(newObj.getCheckExpireDate()));
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getCheckExpireDate()) && ObjectUtil.isEmpty(newObj.getCheckExpireDate())){
                msg += String.format(temp,"账户年检到期日",DateUtils.formatDate(oldObj.getCheckExpireDate()),"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getCheckExpireDate()) && ObjectUtil.isNotEmpty(newObj.getCheckExpireDate())){
                msg += String.format(temp,"账户年检到期日","空",DateUtils.formatDate(newObj.getCheckExpireDate()));
            }
        }

        // 备注
        if (ObjectUtil.isNotEmpty(oldObj.getRmk()) && ObjectUtil.isNotEmpty(newObj.getRmk())){
            if (!oldObj.getRmk().equals(newObj.getRmk())){
                String oldName = oldObj.getRmk();
                String newName = newObj.getRmk();
                msg += String.format(temp,"备注",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getRmk()) && ObjectUtil.isEmpty(newObj.getRmk())){
                String oldName = oldObj.getRmk();
                msg += String.format(temp,"备注",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getRmk()) && ObjectUtil.isNotEmpty(newObj.getRmk())){
                String newName = newObj.getRmk();
                msg += String.format(temp,"备注","空",newName);
            }
        }
        // 到期日期 endDate
        if (ObjectUtil.isNotEmpty(oldObj.getEndDate()) && ObjectUtil.isNotEmpty(newObj.getEndDate())){
            if (!DateUtils.formatDate(oldObj.getEndDate()).equals(DateUtils.formatDate(newObj.getEndDate()))){
                msg += String.format(temp,"到期日期",DateUtils.formatDate(oldObj.getEndDate()),DateUtils.formatDate(newObj.getEndDate()));
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getEndDate()) && ObjectUtil.isEmpty(newObj.getEndDate())){
                msg += String.format(temp,"到期日期",DateUtils.formatDate(oldObj.getEndDate()),"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getEndDate()) && ObjectUtil.isNotEmpty(newObj.getEndDate())){
                msg += String.format(temp,"到期日期","空",DateUtils.formatDate(newObj.getEndDate()));
            }
        }

        // 是否线上办理 isOnlineHandle
        if (ObjectUtil.isNotEmpty(oldObj.getIsOnlineHandle()) && ObjectUtil.isNotEmpty(newObj.getIsOnlineHandle())){
            if (!oldObj.getIsOnlineHandle().equals(newObj.getIsOnlineHandle())){
                String oldName = CommonConstant.YES.equals(oldObj.getIsOnlineHandle())?"是":"否";
                String newName = CommonConstant.YES.equals(newObj.getIsOnlineHandle())?"是":"否";
                msg += String.format(temp,"是否线上办理",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getIsOnlineHandle()) && ObjectUtil.isEmpty(newObj.getIsOnlineHandle())){
                String oldName = CommonConstant.YES.equals(oldObj.getIsOnlineHandle())?"是":"否";
                msg += String.format(temp,"是否线上办理",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getIsOnlineHandle()) && ObjectUtil.isNotEmpty(newObj.getIsOnlineHandle())){
                String newName = CommonConstant.YES.equals(newObj.getIsOnlineHandle())?"是":"否";
                msg += String.format(temp,"是否线上办理","空",newName);
            }
        }
        // 账户组 fcAccGroupCode
        if(ObjectUtil.isNotEmpty(newObj.getFcAccGroupCode()) && ObjectUtil.isNotEmpty(oldObj.getFcAccGroupCode())){
            List<String> groupList = Arrays.asList(newObj.getFcAccGroupCode().split(","));
            List<String> oldGroupList = Arrays.asList(oldObj.getFcAccGroupCode().split(","));
            boolean areElementsEqual = new TreeSet<String>(groupList).equals(new TreeSet<String>(oldGroupList));
            if (!areElementsEqual){
                Map<String, String> groupMap = fcBankAccGroupService.getAccGroupMapByIds(groupList);
                Map<String, String> oldGroupMap = fcBankAccGroupService.getAccGroupMapByIds(oldGroupList);
                String curValues = "";
                if (!ObjectUtil.isEmpty(groupMap)){
                    curValues = groupMap.values().stream()
                            .collect(Collectors.joining(","));
                }
                String oldValues = "";
                if (!ObjectUtil.isEmpty(oldGroupMap)){
                    oldValues = oldGroupMap.values().stream()
                            .collect(Collectors.joining(","));
                }
                msg += String.format(temp,"账户组",oldValues,curValues);
            }
        }
        // 单据类型 receiptCode;fcReceiptTypeService
        if(ObjectUtil.isNotEmpty(newObj.getReceiptCode()) && ObjectUtil.isNotEmpty(oldObj.getReceiptCode())){
            List<String> receiptList = Arrays.asList(newObj.getReceiptCode().split(","));
            List<String> oldReceiptList = Arrays.asList(oldObj.getReceiptCode().split(","));
            boolean areElementsEqual = new TreeSet<String>(receiptList).equals(new TreeSet<String>(oldReceiptList));
            if (!areElementsEqual){
                Map<String,String> map = fcReceiptTypeService.getFcReceiptTypeNameMap();
                String newName = "";
                for (String code : receiptList){
                    newName += map.get(code)+",";
                }
                if (newName.length()>0){
                    newName = newName.substring(0,newName.length()-1);
                }

                String oldName = "";
                for (String code : oldReceiptList){
                    oldName += map.get(code)+",";
                }
                if (oldName.length()>0){
                    oldName = oldName.substring(0,oldName.length()-1);
                }
                msg += String.format(temp,"单据类型",oldName,newName);
            }
        }
        // 是否参与资金需求计算 isDemand
        if (ObjectUtil.isNotEmpty(oldObj.getIsDemand()) && ObjectUtil.isNotEmpty(newObj.getIsDemand())){
            if (!oldObj.getIsDemand().equals(newObj.getIsDemand())){
                String oldName = CommonConstant.YES.equals(oldObj.getIsDemand())?"是":"否";
                String newName = CommonConstant.YES.equals(newObj.getIsDemand())?"是":"否";
                msg += String.format(temp,"是否参与资金需求计算",oldName,newName);
            }
        }else{
            if (ObjectUtil.isNotEmpty(oldObj.getIsDemand()) && ObjectUtil.isEmpty(newObj.getIsDemand())){
                String oldName = CommonConstant.YES.equals(oldObj.getIsDemand())?"是":"否";
                msg += String.format(temp,"是否参与资金需求计算",oldName,"空");
            }
            if (ObjectUtil.isEmpty(oldObj.getIsDemand()) && ObjectUtil.isNotEmpty(newObj.getIsDemand())){
                String newName = CommonConstant.YES.equals(newObj.getIsDemand())?"是":"否";
                msg += String.format(temp,"是否参与资金需求计算","空",newName);
            }
        }

        return msg;
    }

    private Map<String,String> loadNatureMap(){
        Map<String,String> map = new HashMap<>();
        List<BtAccNatureDTO> btAccNatures=btBankAccCommonService.getAllBtAccNature();
        if(EmptyUtils.isNotEmpty(btAccNatures)){
            for(BtAccNatureDTO btAccNature:btAccNatures){
                map.put(btAccNature.getId(),btAccNature.getNatureName());
            }
        }
        return map;
    }

    private Map<String,String> loadAttributeMap(){
        Map<String,String> map = new HashMap<>();
        List<BisAccAttributeDTO> bisAccAttributes=btBankAccCommonService.getAllBisAccAttributes();
        if(EmptyUtils.isNotEmpty(bisAccAttributes)){
            for(BisAccAttributeDTO bisAccAttribute:bisAccAttributes){
                map.put(bisAccAttribute.getId(),bisAccAttribute.getAttributeName());
            }
        }
        return map;
    }

    private Map<String,String> loadBifMap(){
        Map<String,String> map = new HashMap<>();
        String validSign="1";
        List<BisBifInitDTO> bifInitDTOS=bisBifInitService.listByValidSign(validSign);
        if(EmptyUtils.isNotEmpty(bifInitDTOS)){
            for(BisBifInitDTO bifInitDTO:bifInitDTOS){
                map.put(bifInitDTO.getId(),bifInitDTO.getName());
            }
        }
        return map;
    }

    @Deprecated
    public void saveAccCur(BtBankAccDTO btBankAcc, String[] curIds, ICurrentUser user) {
        List<String> curList = new ArrayList<String>();
        for (int i = 0; i < curIds.length; i++) {
            BtBankAccCurVO accCur = new BtBankAccCurVO();
            if ("all".equals(curIds[i])) {
                continue;
            }
            BtBankAccCurDTO cur = this.btBankAccCommonService.getBtBankAccCur(btBankAcc.getId(), curIds[i]);
            if (EmptyUtils.isEmpty(cur)) {
                accCur.setBankAccId(btBankAcc.getId());
                accCur.setCurId(curIds[i]);
                btBankAccCurService.save(accCur);
                curList.add(curIds[i]);
            }
        }
        if (BtBankAccConstant.ISONLINE_0.equalsIgnoreCase(btBankAcc.getIsOnline()) && btBankAcc.getInitBalanceDate()!=null) {
            // 初始化余额信息 zuoy 2017-08-26
        	com.bytter.biz.bdlink.api.dto.BtBankAccDTO dto = new com.bytter.biz.bdlink.api.dto.BtBankAccDTO();
            BeanUtils.copyNotNullProperties(btBankAcc, dto);
            dto.setCurList(curList);
            this.bisAccBalService.initBisAccBal(dto, user);
        }
        // 初始化联行号信息
        this.bankService.saveBtBank(btBankAcc.getBankCode(), btBankAcc.getBankTypeId());
    }

    public void updateAccCur(BtBankAccDTO btBankAcc, String[] curIds, ICurrentUser user) {
        List<String> curList = new ArrayList<String>();

        List<BtBankAccCurDTO> existingCurList = this.btBankAccCurService.getBankAccCurByBankAccId(btBankAcc.getId());
        Set<String> existingCurIds = existingCurList.stream()
                .map(BtBankAccCurDTO::getCurId)
                .collect(Collectors.toSet());

        Set<String> newCurIds = new HashSet<String>();
        for (int i = 0; i < curIds.length; i++) {
            BtBankAccCurVO accCur = new BtBankAccCurVO();
            if ("all".equals(curIds[i])) {
                continue;
            }
            newCurIds.add(curIds[i]);
        }

        Set<String> toDelete = new HashSet<String>(existingCurIds);
        toDelete.removeAll(newCurIds);

        Set<String> toAdd = new HashSet<String>(newCurIds);
        toAdd.removeAll(existingCurIds);


        if (!toDelete.isEmpty()) {
            existingCurList.stream().filter(cur -> toDelete.contains(cur.getCurId()))
                    .forEach(cur -> btBankAccCurService.delete(cur.getId()));
        }
        for (String curId : toAdd) {
            BtBankAccCurVO accCur = new BtBankAccCurVO();
            accCur.setBankAccId(btBankAcc.getId());
            accCur.setCurId(curId);
            btBankAccCurService.save(accCur);
            curList.add(curId);
        }

        if (BtBankAccConstant.ISONLINE_0.equalsIgnoreCase(btBankAcc.getIsOnline()) && btBankAcc.getInitBalanceDate()!=null) {
            // 初始化余额信息 zuoy 2017-08-26
            com.bytter.biz.bdlink.api.dto.BtBankAccDTO dto = new com.bytter.biz.bdlink.api.dto.BtBankAccDTO();
            BeanUtils.copyNotNullProperties(btBankAcc, dto);
            dto.setCurList(curList);
            this.bisAccBalService.initBisAccBal(dto, user);
        }
        // 初始化联行号信息
        this.bankService.saveBtBank(btBankAcc.getBankCode(), btBankAcc.getBankTypeId());
    }
    private void loadDictGroupCodeNameCode(Map<String,String> dictGroupCodeNameCodeCache){
        String sql="select d.type_name as \"typeName\",d.type_code as \"typeCode\" , dg.TYPE_GROUP_CODE as \"typeGroupCode\" from sys_dict d left join sys_dict_group dg on d.TYPE_GROUP_ID = dg.id" +
                " where dg.TYPE_GROUP_CODE in ('btBankAcc.accType','btBankAcc.Term','bankAcc.noticeType','btBankAcc.interestDate')";
        List<Map<String,Object>> dictGroupCodeNameCodeMapList = dictService.getExtendTableData(sql);
        if (EmptyUtils.isNotEmpty(dictGroupCodeNameCodeMapList)) {
            for (Map<String, Object> dictGroupCodeNameCodeMap : dictGroupCodeNameCodeMapList) {
                Object typeGroupCode = MapUtils.getMapValueIgnoreCase(dictGroupCodeNameCodeMap, "typeGroupCode");
                Object typeName = MapUtils.getMapValueIgnoreCase(dictGroupCodeNameCodeMap, "typeName");
                Object typeCode = MapUtils.getMapValueIgnoreCase(dictGroupCodeNameCodeMap, "typeCode");
                if (typeGroupCode == null || typeName == null || typeCode == null) {
                    continue;
                }
                dictGroupCodeNameCodeCache.put(typeGroupCode + BtBankAccConstant.DELIMITER_D_COLON + typeName, typeCode.toString());
            }
        }
    }
    /**
     * 导入功能
     *
     * @param list
     */
    public void saveImport(List<BtBankAccVO> list, ICurrentUser user) {
        Map<String,Object> bankAccCache=Maps.newHashMap();
        Set<String> bankAccs= Sets.newHashSet();
        for(BtBankAccVO bankAccVO : list){
            if(EmptyUtils.isNotEmpty(bankAccVO.getBankAcc())) {
                bankAccs.add(bankAccVO.getBankAcc());
            }
        }
        loadBankAcc(bankAccCache,bankAccs);
        Map<String,String> dictGroupCodeNameCodeCache = Maps.newHashMap();
        List<BtBankAccDTO> entityList = new ArrayList<>();
        String loginName = user.getLoginName();
        Date curDate = DateUtils.getDate();
        for (BtBankAccVO vo : list) {
            BtBankAccDTO btBankAcc = new BtBankAccDTO();
            BeanUtils.copyNotNullProperties(vo, btBankAcc);
            btBankAcc.setStatus(NetbankConstant.STATUE_CHECK);
            btBankAcc.setCreateBy(loginName);
            btBankAcc.setCreateDate(curDate);
            // 判断银行账号是否在系统中存在
            if(bankAccCache.containsKey(vo.getBankAcc())){
//            if (!this.btBankAccCommonService.isBankAcc(vo.getBankAcc())) {
                continue;
            }
            /*if (EmptyUtils.isEmpty(vo.getInitBalanceDate())) {
                btBankAcc.setInitBalanceDate(vo.getRegDate());
            }*/
            //设置利率是否浮动(是否转换为1-0)
            if (BtBankAccConstant.YES_ZN.equals(vo.getRatesFloatName())) {
                btBankAcc.setRatesFloat(BtBankAccConstant.YES);
            } else {
                btBankAcc.setRatesFloat(BtBankAccConstant.NO);
            }
            //设置是否在线(是否转换为1-0)
            if (BtBankAccConstant.YES_ZN.equals(vo.getIsOnline())) {
                btBankAcc.setIsOnline(BtBankAccConstant.ISONLINE_1);
            } else {
                btBankAcc.setIsOnline(BtBankAccConstant.ISONLINE_0);
            }
            //设置是否有效(是-否转换为1-0)
            if (BtBankAccConstant.YES_ZN.equals(vo.getValidSign())) {
                btBankAcc.setValidSign(BtBankAccConstant.VALIDSIGN_1);
            } else {
                btBankAcc.setValidSign(BtBankAccConstant.VALIDSIGN_0);
            }
            // 账户类别为定期时，定期期限对应数据字典
            if (!EmptyUtils.isBlank(vo.getRegulars())) {
                if(dictGroupCodeNameCodeCache.isEmpty()){
                    loadDictGroupCodeNameCode(dictGroupCodeNameCodeCache);
                }
                String regulars = dictGroupCodeNameCodeCache.get("btBankAcc.Term"+BtBankAccConstant.DELIMITER_D_COLON+vo.getRegulars());
//                String regulars = dictService.getCodeByName("btBankAcc.Term", vo.getRegulars());
                btBankAcc.setRegulars(regulars);
            }
            //通知类型对应数据字典
            if (!EmptyUtils.isBlank(vo.getNoticeType())) {
                if(dictGroupCodeNameCodeCache.isEmpty()){
                    loadDictGroupCodeNameCode(dictGroupCodeNameCodeCache);
                }
                String noticeType = dictGroupCodeNameCodeCache.get("bankAcc.noticeType"+BtBankAccConstant.DELIMITER_D_COLON+vo.getNoticeType());
//                String noticeType = dictService.getCodeByName("bankAcc.noticeType", vo.getNoticeType());
                btBankAcc.setNoticeType(noticeType);
            }
            //计息日对应数据字典
            if (!EmptyUtils.isBlank(vo.getInterestDate())) {
                if(dictGroupCodeNameCodeCache.isEmpty()){
                    loadDictGroupCodeNameCode(dictGroupCodeNameCodeCache);
                }
                String interestDate = dictGroupCodeNameCodeCache.get("btBankAcc.interestDate"+BtBankAccConstant.DELIMITER_D_COLON+vo.getInterestDate());
//                String interestDate = dictService.getCodeByName("btBankAcc.interestDate", vo.getInterestDate());
                btBankAcc.setInterestDate(interestDate);
            }
            entityList.add(btBankAcc);
        }
        bankAccCache=null;
        dictGroupCodeNameCodeCache=null;
        entityList.stream().filter(entity -> !EmptyUtils.isBlank(entity.getBankAcc()) && !EmptyUtils.isBlank(entity.getBankTypeId()));
        List<BtBankAccDTO> resultList = btBankAccService.batchSaveEntity(entityList);
//        for (BtBankAcc entity : entityList) {
//            String curIdStr = entity.getCurId();
//            String[] curIds = curIdStr.split(",");
//            this.saveAccCur(entity, curIds, user);
//        }
        batchSaveAccCur(resultList,user);
//        batchSaveBtBank(entityList);
    }
    private void batchSaveAccCur(List<BtBankAccDTO> entityList,ICurrentUser user) {
        List<BtBankAccCurVO> btBankAccCurList=new ArrayList<>();
        List<BtBankAccDTO> btBankAccDTOList=new ArrayList<>();
        for (BtBankAccDTO btBankAcc : entityList) {
            String curIdStr = btBankAcc.getCurId();
            String[] curIds = curIdStr.split(",");
            List<String> curList = new ArrayList<String>(curIds.length);
            for (int i = 0; i < curIds.length; i++) {
                BtBankAccCurVO accCur = new BtBankAccCurVO();
                if ("all".equals(curIds[i])) {
                    continue;
                }
                BtBankAccCurDTO btBankAccCur = this.btBankAccCommonService.getBtBankAccCur(btBankAcc.getId(), curIds[i]);
                BtBankAccCurDTO cur = btBankAccCur;
                if (EmptyUtils.isEmpty(cur)) {
                    accCur.setBankAccId(btBankAcc.getId());
                    accCur.setCurId(curIds[i]);
                    btBankAccCurList.add(accCur);
                    curList.add(curIds[i]);
                }
            }
            if (BtBankAccConstant.ISONLINE_0.equalsIgnoreCase(btBankAcc.getIsOnline()) && btBankAcc.getInitBalanceDate() != null) {
                // 初始化余额信息 zuoy 2017-08-26
                BtBankAccDTO dto = new BtBankAccDTO();
                BeanUtils.copyNotNullProperties(btBankAcc, dto);
                dto.setCurList(curList);
                btBankAccDTOList.add(dto);
            }
        }
        this.btBankAccCurService.batchSaveEntity(btBankAccCurList);
        List<com.bytter.biz.bdlink.api.dto.BtBankAccDTO> accDTOList = BeanUtils.copyListProperties(btBankAccDTOList,com.bytter.biz.bdlink.api.dto.BtBankAccDTO.class);
        this.bisAccBalService.initBisAccBal(accDTOList, user);
        this.bankService.saveBtBank(btBankAccDTOList);
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(BtBankAccVO vo) {
        return null;
    }
    private void loadBankAcc(Map<String,Object> bankAccCache,Set<String> bankAccs){
        if(EmptyUtils.isEmpty(bankAccs)){
            return;
        }
        List<String> existsBankAccs=this.btBankAccCommonService.getByBankAccs(bankAccs);
        if(EmptyUtils.isNotEmpty(existsBankAccs)){
            for(String bankAcc:existsBankAccs){
                bankAccCache.put(bankAcc,null);
            }
        }
    }
    private void loadBankTypeNameCache(Map<String,BankTypeDTO> bankTypeNameCache,Set<String> bankTypeNames){
        if(EmptyUtils.isEmpty(bankTypeNames)){
            return;
        }
        List<BankTypeDTO> existsBankTypeDTOs=bankTypeService.getByBankTypeNames(bankTypeNames);
        if(EmptyUtils.isNotEmpty(existsBankTypeDTOs)){
            for(BankTypeDTO bankTypeDTO:existsBankTypeDTOs){
                bankTypeNameCache.put(bankTypeDTO.getTypeName(),bankTypeDTO);
            }
        }
    }
    private void loadBifNameId(Map<String,String> bifNameIdCache){
        String validSign="1";
        List<BisBifInitDTO> bifInitDTOS=bisBifInitService.listByValidSign(validSign);
        if(EmptyUtils.isNotEmpty(bifInitDTOS)){
            for(BisBifInitDTO bifInitDTO:bifInitDTOS){
                bifNameIdCache.put(bifInitDTO.getName(),bifInitDTO.getId());
            }
        }
    }
    private void loadNatureNameIdCache(Map<String,String> natureNameIdCache){
        List<BtAccNatureDTO> btAccNatures=btBankAccCommonService.getAllBtAccNature();
        if(EmptyUtils.isNotEmpty(btAccNatures)){
            for(BtAccNatureDTO btAccNature:btAccNatures){
                natureNameIdCache.put(btAccNature.getNatureName(),btAccNature.getId());
            }
        }
    }
    private void loadAttributeNameId(Map<String,String> attributeNameIdCache){
        List<BisAccAttributeDTO> bisAccAttributes=btBankAccCommonService.getAllBisAccAttributes();
        if(EmptyUtils.isNotEmpty(bisAccAttributes)){
            for(BisAccAttributeDTO bisAccAttribute:bisAccAttributes){
                attributeNameIdCache.put(bisAccAttribute.getAttributeName(),bisAccAttribute.getId());
            }
        }
    }
    private void loadBtCurrencyNameIdCache(Map<String,String> btCurrencyNameIdCache){
        List<BtCurrencyDTO> currencyDTOs = this.btCurrencyService.listAllBtCurrency();
        if(EmptyUtils.isNotEmpty(currencyDTOs)){
            for(BtCurrencyDTO currencyDTO:currencyDTOs){
                btCurrencyNameIdCache.put(currencyDTO.getCurName(),currencyDTO.getId());
            }
        }
    }
    @Override
    public void verifyHandler(List<BtBankAccVO> list) {
        List<String> bankAccList = new ArrayList<>();
        Map<String,Object> bankAccCache= Maps.newHashMap();
        Map<String,BankTypeDTO> bankTypeNameCache=Maps.newHashMap();
        Map<String,InputBankInfoDTO> bankCodeBankInfoCache= Maps.newHashMap();
        Map<String,BtSwiftInfoDTO> bankCodeSwiftInfoCache=Maps.newHashMap();
        Map<String,String> bifNameIdCache=Maps.newHashMap();
        Map<String,String> natureNameIdCache=Maps.newHashMap();
        Map<String,String> attributeNameIdCache=Maps.newHashMap();
        Map<String,String> dictGroupCodeNameCodeCache = Maps.newHashMap();
        Map<String,String> btCurrencyNameIdCache=Maps.newHashMap();
        Set<String> bankAccs = Sets.newHashSet();
        Set<String> bankTypeNames = Sets.newHashSet();
        for(BtBankAccVO bankAccVO : list){
            if(EmptyUtils.isNotEmpty(bankAccVO.getBankAcc())) {
                bankAccs.add(bankAccVO.getBankAcc());
            }
            if(EmptyUtils.isNotEmpty(bankAccVO.getBankTypeName())){
                bankTypeNames.add(bankAccVO.getBankTypeName());
            }
        }
        loadBankAcc(bankAccCache,bankAccs);
        loadBankTypeNameCache(bankTypeNameCache,bankTypeNames);
        loadBtCurrencyNameIdCache(btCurrencyNameIdCache);
        loadDictGroupCodeNameCode(dictGroupCodeNameCodeCache);
        bankAccs=null;
        bankTypeNames=null;
        //获取所有的单据类型
        List<FcReceiptType> validFcReceiptType = fcReceiptTypeService.getValidFcReceiptType();
        //查询所有账户组
        List<FcBankAccGroup> validPayType = fcBankAccGroupService.getValidPayType();
        // 所有账户用途
        List<BtBankAccUse> accUses = btBankAccUseService.listAll();
        // 用途为1004的配置数量
        ConfigDTO config = configService.getConfig("allot", "revenueAccountsNumber");
        Integer configSize = config.toInteger(0);
        // 已使用数量
        Map<String, Integer> corpUseNums = new HashMap<>();
        if (!EmptyUtils.isEmptyList(list)) {
            //单位赋值
            for (BtBankAccVO vo : list) {
                if (StringUtils.isNotBlank(vo.getCorpName())) {
                    CorpDTO corp = corpService.getCorpIdByName(vo.getCorpName());
                    if (EmptyUtils.isEmpty(corp) || StringUtils.isBlank(corp.getId())) {
                        continue;
                    }
                    vo.setCorpId(corp.getId());
                }
            }
            List<String> corpIds = list.stream().filter(vo -> StringUtils.isNotBlank(vo.getCorpId())).map(BtBankAccVO::getCorpId).distinct().collect(Collectors.toList());
            for (String corpId : corpIds) {
                Integer bankAccUse1004 = btBankAccService.getBankAccUse1004("",corpId);
                // 剩余量
                Integer accUse1004 = configSize - bankAccUse1004;
                if (new BigDecimal(accUse1004).compareTo(BigDecimal.ZERO) < 0) {
                    accUse1004 = 0;
                }
                corpUseNums.put(corpId, accUse1004);
            }
        }

//        Integer currentUse = 0;
        //当前选择数量
        Map<String, Integer> newCorpUseNum = new HashMap<>();
        for (BtBankAccVO vo : list) {
            List<String> msgList = new ArrayList<>();
            // 银行账号不为空
            if (EmptyUtils.isBlank(vo.getBankAcc())) {
                msgList.add("nis.netbank.message.bankacc.null");
            } else {
                // 判断银行账号是否在系统中存在
//                if (this.btBankAccCommonService.isBankAcc(vo.getBankAcc())) {
                if(!bankAccCache.containsKey(vo.getBankAcc())){
                    // 导入文件中存在多个相同账户
                    if (bankAccList.contains(vo.getBankAcc())) {
                        msgList.add("nis.netbank.message.bankacc.multiple");
                    } else {
                        bankAccList.add(vo.getBankAcc());
                    }
                } else {
                    msgList.add("nis.netbank.message.bankaccinfo.notnull");
                }
            }
            // 判断单位在系统中是否存在
            CorpDTO corp = this.corpService.getCorpIdByName(vo.getCorpName());
            if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
                corp = new CorpDTO();
                msgList.add("nis.netbank.message.corp.null");
            } else {
                vo.setCorpId(corp.getId());
            }
            //利率校验
            if (null != vo.getAgrInterest()) {
                BigDecimal rate = new BigDecimal("1");
                // 利率判断值是否大于1
                if (vo.getAgrInterest().compareTo(rate) > 0) {
                    //协定利率不允许超过1
                    msgList.add("nis.netbank.message.agrInterest.valueError");
                }
            }
            // 账户名不为空
            if (EmptyUtils.isBlank(vo.getAccName())) {
                msgList.add("nis.netbank.message.accname.null");
            }
            // 单位不为空
            if (EmptyUtils.isEmpty(vo.getCorpName())) {
                msgList.add("nis.netbank.message.corpname.null");
            }

//            else {
//                // 判断单位在系统中是否存在
//                CorpDTO corp = this.corpService.getCorpIdByName(vo.getCorpName());
//                if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
//                    msgList.add("nis.netbank.message.corp.null");
//                } else {
//                    vo.setCorpId(corp.getId());
//                }
//            }
            //境内/境外账号 1-境内，0-境外
            //境内/境外账号 1-境内，0-境外
            //境内/境外账号 1-境内，0-境外
            if (StringUtils.isBlank(vo.getIsDomesticBankName())) {
                msgList.add("nis.netbank.message.isdomesticbank.format");
            } else if (!"境内".equals(vo.getIsDomesticBankName())
                    && !"境外".equals(vo.getIsDomesticBankName())) {
                msgList.add("nis.netbank.message.isdomesticbank.format");
            } else {
                if ("境内".equals(vo.getIsDomesticBankName())) {
                    vo.setIsDomesticBank(CommonConstant.YES);
                }
                if ("境外".equals(vo.getIsDomesticBankName())) {
                    vo.setIsDomesticBank(CommonConstant.NO);
                }
            }
            // 银行类别不能为空
            if (EmptyUtils.isBlank(vo.getBankTypeName())) {
                msgList.add("nis.netbank.message.banktypename.null");
            } else {
                // 判断银行类别在系统中是否存在
                // BankTypeDTO banktype = bankTypeService.getByTypeName(vo.getBankTypeName());
                BankTypeDTO banktype = bankTypeNameCache.get(vo.getBankTypeName());
                if (EmptyUtils.isEmpty(banktype)) {
                    msgList.add("nis.netbank.message.btbanktype.null");
                } else {
                    vo.setBankTypeId(banktype.getId());
                    // 开户行不为空
                    if (EmptyUtils.isBlank(vo.getBankCode())) {
                        msgList.add("nis.netbank.message.bankname.null");
                    } else {
                        if(CommonConstant.NO.equals(banktype.getIsDomesticBank())){
                            //境外银行
                            BtSwiftInfoDTO swiftInfoDTO = bankCodeSwiftInfoCache.get(vo.getBankCode());
                            if(swiftInfoDTO == null) {
                                swiftInfoDTO = this.btSwiftInfoService.getByCode(vo.getBankCode());
                                bankCodeSwiftInfoCache.put(vo.getBankCode(),swiftInfoDTO);
                            }
                            if (EmptyUtils.isEmpty(swiftInfoDTO) || StringUtils.isBlank(swiftInfoDTO.getSwiftCode())) {
                                msgList.add("nis.netbank.message.bank.null");
                            } else {
                                vo.setBankCode(swiftInfoDTO.getSwiftCode());
                                vo.setBankName(swiftInfoDTO.getBankName());
                                vo.setProv(swiftInfoDTO.getNameZh());
                                vo.setCity(swiftInfoDTO.getCityName());
                            }
                        }else {
                            // 判断开户行在系统中是否存在
                            InputBankInfoDTO btBankDTO = bankCodeBankInfoCache.get(vo.getBankCode());
                            if(btBankDTO == null) {
                                btBankDTO = this.inputBankInfoService.getByBankCodeAndImport(vo.getBankCode());
                                bankCodeBankInfoCache.put(vo.getBankCode(),btBankDTO);
                            }
                            if (EmptyUtils.isEmpty(btBankDTO)) {
                                msgList.add("nis.netbank.message.bank.null");
                            } else {
                                vo.setBankCode(btBankDTO.getBankCode());
                                vo.setBankName(btBankDTO.getBankName());
                                vo.setProv(btBankDTO.getProv());
                                vo.setCity(btBankDTO.getCity());
                            }
                        }
                    }
                }
            }
            if (EmptyUtils.isNotEmpty(vo.getBifName())) {
                // 判断接口代码在系统中是否存在
                if(bifNameIdCache.isEmpty()){
                    loadBifNameId(bifNameIdCache);
                }
//                BisBifInitDTO bisBifInitDTO = bisBifInitService.getByName(vo.getBifName());
                String bisBifInitId= bifNameIdCache.get(vo.getBifName());
                if(StringUtils.isEmpty(bisBifInitId)){
//                if (EmptyUtils.isEmpty(bisBifInitDTO)) {
                    msgList.add("nis.netbank.message.bisbifinit.null");
                } else {
//                    vo.setBifCode(bisBifInitDTO.getId());
                    vo.setBifCode(bisBifInitId);
                }
            } else {
                msgList.add("nis.netbank.message.bifcode.null");
            }
            // 账户性质不能为空
            if (EmptyUtils.isBlank(vo.getNatureName())) {
                msgList.add("nis.netbank.message.naturename.null");
            } else {
                // 判断账户性质在系统中是否存在
                if(natureNameIdCache.isEmpty()){
                    loadNatureNameIdCache(natureNameIdCache);
                }
                String natureId = natureNameIdCache.get(vo.getNatureName());
//                String natureId = btBankAccCommonService.getNatureId(vo.getNatureName());
                if (EmptyUtils.isEmpty(natureId)) {
                    msgList.add("nis.netbank.message.btaccnature.null");
                } else {
                    vo.setNatureId(natureId);
                }
            }
            // 账户属性不能为空
            if (EmptyUtils.isBlank(vo.getAttributeName())) {
                msgList.add("nis.netbank.message.attributeName.null");
            } else {
                // 判断账户属性在系统中是否存在
                if(attributeNameIdCache.isEmpty()){
                    loadAttributeNameId(attributeNameIdCache);
                }
                String attributeId = attributeNameIdCache.get(vo.getAttributeName());
//                String attributeId = btBankAccCommonService.getAttributeId(vo.getAttributeName());
                if (EmptyUtils.isEmpty(attributeId)) {
                    msgList.add("nis.netbank.message.bisaccattribute.null");
                } else {
                    vo.setAttributeId(attributeId);
                }
            }
            // 默认不是票据专户，如果填了账户类别accType=10;下面会改为是
            vo.setElectricBill(CommonConstant.NO);
            // 账户类别不能为空
            if (EmptyUtils.isBlank(vo.getAccTypeName())) {
                msgList.add("nis.netbank.message.acctypename.null");
            } else {
                // 判断账户类别在系统中是否存在
                // String accType = dictService.getCodeByName("btBankAcc.accType", vo.getAccTypeName());
                String accType = dictGroupCodeNameCodeCache.get("btBankAcc.accType"+BtBankAccConstant.DELIMITER_D_COLON+vo.getAccTypeName());
                if (EmptyUtils.isEmpty(accType)) {
                    msgList.add("nis.netbank.message.acctype.null");
                } else {
                    vo.setAccType(accType);
                    // 账户类别为定期时，定期期限不能为空
                    if (BtBankAccConstant.ACCTYPE_02.equalsIgnoreCase(accType)) {
                        if (EmptyUtils.isEmpty(vo.getRegulars())) {
                            msgList.add("nis.netbank.message.regulars.null");
                        } else {
                            // String regulars = dictService.getCodeByName("btBankAcc.Term", vo.getRegulars());
                            String regulars = dictGroupCodeNameCodeCache.get("btBankAcc.Term"+BtBankAccConstant.DELIMITER_D_COLON+vo.getRegulars());
                            if (EmptyUtils.isBlank(regulars)) {
                                msgList.add("nis.netbank.message.regularsinfo.null");
                            }
                        }
                    }
                    // 账户类别为通知时，通知类型不能为空
                    if (BtBankAccConstant.ACCTYPE_06.equalsIgnoreCase(accType)) {
                        if (EmptyUtils.isEmpty(vo.getNoticeType())) {
                            msgList.add("nis.netbank.message.noticetype.null");
                        } else {
                            // String noticeType = dictService.getCodeByName("bankAcc.noticeType", vo.getNoticeType());
                            String noticeType = dictGroupCodeNameCodeCache.get("bankAcc.noticeType"+BtBankAccConstant.DELIMITER_D_COLON+vo.getNoticeType());
                            if (EmptyUtils.isBlank(noticeType)) {
                                msgList.add("nis.netbank.message.noticetypeinfo.null");
                            }
                        }
                    }
                    // 账户类别为协定时，协定金额，协定利率，协定合同，协定开始日期，结束日期不能为空
                    if (BtBankAccConstant.ACCTYPE_07.equalsIgnoreCase(accType)) {
                        if (EmptyUtils.isEmpty(vo.getAgrMoney())) {
                            msgList.add("nis.netbank.message.agrmoney.null");
                        }
                        if (EmptyUtils.isEmpty(vo.getAgrInterest())) {
                            msgList.add("nis.netbank.message.agrinterest.null");
                        }
                        if (EmptyUtils.isEmpty(vo.getAgrContract())) {
                            msgList.add("nis.netbank.message.agrcontract.null");
                        }
                        if (EmptyUtils.isEmpty(vo.getAgrBeginDate())) {
                            msgList.add("nis.netbank.message.agrbegindate.null");
                        }
                        if (EmptyUtils.isEmpty(vo.getAgrEndDate())) {
                            msgList.add("nis.netbank.message.agrenddate.null");
                        }
                    }
                    // 票据专户时候，是否票据账户设置值，流通方式设置
                    if (BtBankAccConstant.ACCTYPE_10.equalsIgnoreCase(accType)){
                        vo.setElectricBill(CommonConstant.YES);
                        //流通方式 0-线下 1-线上 circulationModeName
                        if (EmptyUtils.isEmpty(vo.getCirculationModeName())) {
                            msgList.add("nis.netbank.message.circulationMode.null");
                        }else{
                            if ("线上".equals(vo.getCirculationModeName())){
                                vo.setCirculationMode(CommonConstant.YES);
                            }
                            if ("线下".equals(vo.getCirculationModeName())){
                                vo.setCirculationMode(CommonConstant.NO);
                            }
                        }
                    }
                }
            }
            // 币别不为空
            if (EmptyUtils.isBlank(vo.getCurName())) {
                msgList.add("nis.netbank.message.curname.null");
            } else {
                StringBuilder curId = new StringBuilder("");
                // 判断币别是否在系统中存在
                for (String curName : vo.getCurName().split(CommonConstant.COMMA_SPLIT)) {
                    // String tempCurId = this.btCurrencyService.getCurrencyByName(curName);
                    String tempCurId = btCurrencyNameIdCache.get(curName);
                    if (EmptyUtils.isBlank(tempCurId)) {
                        break;
                    }
                    if (EmptyUtils.isBlank(curId.toString())) {
                        curId.append(tempCurId);
                    } else {
                        curId.append("," + tempCurId);
                    }
                }
                if (EmptyUtils.isEmpty(curId.toString())) {
                    msgList.add("nis.netbank.message.cur.null");
                } else {
                    vo.setCurId(curId.toString());
                }
            }
            BigDecimal ratesFormat = new BigDecimal(1);
            // 利率判断值是否大于1
            if (!EmptyUtils.isEmpty(vo.getRates())) {
                if (vo.getRates().compareTo(ratesFormat) == 1) {
                    msgList.add("nis.netbank.message.rates.format");
                }
            }
            // 利率是否浮动不能为空
            ratesFormat = new BigDecimal(100);// 浮动百分比
            if (EmptyUtils.isEmpty(vo.getRatesFloatName())) {
                msgList.add("nis.netbank.message.ratesfloatname.null");
            } else {
                // 判断利率是否浮动格式
                if (!BtBankAccConstant.YES_ZN.equalsIgnoreCase(vo.getRatesFloatName())
                        && !BtBankAccConstant.NO_ZN.equalsIgnoreCase(vo.getRatesFloatName())) {
                    msgList.add("nis.netbank.message.ratesfloatname.format");
                }
                // 利率是否浮动为“是”，则“浮动百分比”不能为空
                if (BtBankAccConstant.YES_ZN.equalsIgnoreCase(vo.getRatesFloatName())) {
                    if (EmptyUtils.isEmpty(vo.getRatesFloatPer())) {
                        msgList.add("nis.netbank.message.ratesfloatper.null");
                    } else {
                        if (vo.getRatesFloatPer().compareTo(ratesFormat) == 1) {
                            msgList.add("nis.netbank.message.ratesfloatper.format");
                        }else{
                            // 百分比除以100保存
                            BigDecimal ratesFloatPer = vo.getRatesFloatPer();
                            BigDecimal result = ratesFloatPer.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
                            vo.setRatesFloatPer(ratesFloatPer);
                        }
                    }
                }
            }
            // 计息周期不能为空
            if (EmptyUtils.isBlank(vo.getInterestCycleStr())) {
                vo.setInterestCycle(BtBankAccConstant.INTERESTCYCLE_NONE);
            } else {
                if (BtBankAccConstant.INTERESTCYCLE_MONTH_ZH.equalsIgnoreCase(vo.getInterestCycleStr())) {
                    vo.setInterestCycle(BtBankAccConstant.INTERESTCYCLE_MONTH);
                    // 计息日不能为空
                    if (EmptyUtils.isEmpty(vo.getInterestDate())) {
                        msgList.add("nis.netbank.message.interestdate.null");
                    } else {
                        // String interestDate = dictService.getCodeByName("btBankAcc.interestDate", vo.getInterestDate());
                        String interestDate = dictGroupCodeNameCodeCache.get("btBankAcc.interestDate"+BtBankAccConstant.DELIMITER_D_COLON+vo.getInterestDate());
                        if (EmptyUtils.isBlank(interestDate)) {
                            msgList.add("nis.netbank.message.noticetypeinfo.null");
                        }
                    }
                } else if (BtBankAccConstant.INTERESTCYCLE_SEASON_ZH.equals(vo.getInterestCycleStr())) {
                    vo.setInterestCycle(BtBankAccConstant.INTERESTCYCLE_SEASON);
                } else {
                    vo.setInterestCycle(BtBankAccConstant.INTERESTCYCLE_NONE);
                }
            }
            // 开户日期不能为空
            if (EmptyUtils.isEmpty(vo.getRegDate())) {
                msgList.add("nis.netbank.message.regdate.null");
            } else {
                // 初始余额日期不能小于账户开户日期
                if (!EmptyUtils.isEmpty(vo.getInitBalanceDate()) && vo.getInitBalanceDate().compareTo(vo.getRegDate()) == -1) {
                    msgList.add("nis.netbank.message.regdate.le.initBalanceDate");
                }
            }
            // 是否上线不为空
            if (EmptyUtils.isEmpty(vo.getIsOnline())) {
                msgList.add("nis.netbank.message.isonline.null");
            } else if (!BtBankAccConstant.NO_ZN.equals(vo.getIsOnline())
                    && !BtBankAccConstant.YES_ZN.equals(vo.getIsOnline())) {
                msgList.add("nis.netbank.message.isonline.format");
            }
            // 有效标志不为空
            if (EmptyUtils.isEmpty(vo.getValidSign())) {
                msgList.add("nis.netbank.message.validsign.null");
            } else if (!BtBankAccConstant.NO_ZN.equals(vo.getValidSign())
                    && !BtBankAccConstant.YES_ZN.equals(vo.getValidSign())) {
                msgList.add("nis.netbank.message.validsign.format");
            }
            /*//单据类型不能为空
            if(EmptyUtils.isEmpty(vo.getReceiptName())){
                msgList.add("单据类型不能为空!");
            } else{
                String receiptName = vo.getReceiptName();
                String receiptCode = "";
                String[] receiptNameStr = receiptName.split(",");
                for(int i=0;i<receiptNameStr.length;i++){
                    String name = receiptNameStr[i];
                    String code = "";
                    for(FcReceiptType fcReceiptType : validFcReceiptType){
                        if(EmptyUtils.isNotEmpty(name) && !EmptyUtils.isEmpty(fcReceiptType)){
                            if(name.equals(fcReceiptType.getReceiptName())){
                                code =  fcReceiptType.getReceiptCode();
                                if(EmptyUtils.isEmpty(receiptCode)){
                                    receiptCode = fcReceiptType.getReceiptCode();
                                }else{
                                    receiptCode += ","+fcReceiptType.getReceiptCode();
                                }
                            }
                        }
                    }
                    if(EmptyUtils.isEmpty(code)){
                        msgList.add("单据类型:"+name+"在系统中不存在");
                    }
                }
                vo.setReceiptCode(receiptCode);
            }
            //账户组不能为空
            if(EmptyUtils.isEmpty(vo.getFcAccGroupName())){
                msgList.add("账户组不能为空!");
            }else{
                String fcAccGroupName = vo.getFcAccGroupName();
                String fcAccGroupCode = "";
                String[] fcAccGroupNameStr = fcAccGroupName.split(",");
                for(int i=0;i<fcAccGroupNameStr.length;i++){
                    String name = fcAccGroupNameStr[i];
                    String code = "";
                    for(FcBankAccGroup fcBankAccGroup : validPayType){
                        if(EmptyUtils.isNotEmpty(name) && !EmptyUtils.isEmpty(fcBankAccGroup)){
                            if(name.equals(fcBankAccGroup.getAccGroupName())){
                                code = fcBankAccGroup.getAccGroupCode();
                                if(EmptyUtils.isEmpty(fcAccGroupCode)){
                                    fcAccGroupCode = fcBankAccGroup.getAccGroupCode();
                                }else {
                                    fcAccGroupCode += ","+fcBankAccGroup.getAccGroupCode();
                                }
                            }
                        }
                    }
                    if(EmptyUtils.isEmpty(code)){
                        msgList.add("账户组:"+name+"在系统中不存在");
                    }
                }
                vo.setFcAccGroupCode(fcAccGroupCode);
            }*/
            // 账号用途
            if (EmptyUtils.isEmpty(vo.getAccUseCode())){
//                msgList.add("账号用途不能为空!");
            }else{
                String[] strings = vo.getAccUseCode().split(",");
                String result = "";
                for (String code : strings){
                    String useCode = "";
                    for (BtBankAccUse use : accUses){
                        if (code.equals(use.getUseCode())){
                            useCode = use.getUseCode();
                            break;
                        }
                    }
                    if (ObjectUtil.isEmpty(useCode)){
                        msgList.add("账号用途编码:"+code+"在系统中不存在");
                    }else{
                        // 如果useCode = 1004判断数量是否超过配置数量
                        if ("1004".equals(useCode)){
                            Integer totalUseNum = 0;
                            if (newCorpUseNum.containsKey(vo.getCorpId())) {
                                totalUseNum = newCorpUseNum.get(vo.getCorpId()) + 1;
                                newCorpUseNum.put(vo.getCorpId(), totalUseNum);
                            } else {
                                totalUseNum = 1;
                                newCorpUseNum.put(vo.getCorpId(), totalUseNum);
                            }

                            if (corpUseNums.containsKey(vo.getCorpId())) {
                                if (totalUseNum > corpUseNums.get(vo.getCorpId())) {
                                    msgList.add("账号用途编码:" + code + "使用超过系统配置数量");
                                }
                            }
                        }
                        if (ObjectUtil.isEmpty(result)){
                            result = useCode;
                        }else{
                            result += "," + useCode;
                        }
                    }
                }
                vo.setAccUseCode(result);
            }

            vo.addErrorMsg(msgList);
        }
    }
    /**
     * 根据银行接口代码得到银行账号，用与银企直联
     * BT_BANK_ACC table列表
     *
     * @return
     */
    public DataTablesOutput getDataTableByBifCode(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.NETBANK_BDLINK_BISQUERYCMD_SENDQUERY, user.getId()), "acc.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        return btBankAccHandleService.getDataTableByBifCode(dataTablesInput, sql.toString());
    }
    /**
     * 删除功能
     *
     * @param id
     */
    public void deleteBtBankAcc(String id, ICurrentUser user) {
        BtBankAccDTO btBankAcc = btBankAccService.getBtBankAcc(id);
        // 校验账号是否被引用
        boolean checkQuotePrentBankAcc = this.btBankAccHandleService.checkQuotePrentBankAcc(btBankAcc.getId());
        if (!checkQuotePrentBankAcc) {
            throw new ParameterException(NetbankMessageEnum.NIS_BANKACC_MESSAGE_QUOTE_PRENTBANKACC);
        }
        boolean checkQuoteFcAcc = this.btBankAccHandleService.checkQuoteFcAcc(btBankAcc.getId());
        if (!checkQuoteFcAcc) {
            throw new ParameterException(NetbankMessageEnum.NIS_BANKACC_MESSAGE_QUOTE_FCACC);
        }
        boolean checkQuoteBusiness = this.btBankAccHandleService.checkQuoteBusiness(btBankAcc.getBankAcc());
        if (!checkQuoteBusiness) {
            throw new ParameterException(NetbankMessageEnum.NIS_BANKACC_MESSAGE_QUOTE_BUSINESS);
        }
        boolean checkQuoteEntityBankAcc = this.btBankAccHandleService.checkQuoteEntityBankAcc(btBankAcc.getId());
        if (!checkQuoteEntityBankAcc) {
            throw new ParameterException(NetbankMessageEnum.NIS_BANKACC_MESSAGE_QUOTE_ENTITYBANKACC);
        }
       /* List<BtBankAccCur> curList = btBankAccCommonService.listBtBankAccCurById(btBankAcc.getId());
        if (!EmptyUtils.isEmptyList(curList)) {
            for (BtBankAccCur cur : curList) {
                this.getEntityDao().delete(this.getEntityDao().merge(cur));
            }
        }*/
//        //福耀-修改物理删除
////        this.deleteEntity(btBankAcc);
        //增加审批流，审批通过后删除
        //开启审批
        btBankAcc.setStatus(BtBankAccConstant.STATUE_DEL);
        btBankAccService.update(btBankAcc);
         getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_ACC_HANDLE, btBankAcc.getId(), btBankAcc, btBankAcc.getCorpId(), user.getId());
    }
    public Boolean isHasPrantAndChild(String id) {
        Boolean result = false;
        if (btBankAccHandleService.isHasChildAccById(id) || btBankAccHandleService.isHasParentAccById(id)) {
            result = true;
        }
        return result;
    }
    /**
     * 定时任务
     * 账户年检到期提醒
     *
     * @author qinl
     * @date 2019-07-04
     */
    @Override
    @XxlJob("BtBankAccHandleService")
    public void run(String arguments) {
        Date curDate = DateUtils.getDate();
        this.logger.info(curDate + "---账户年检到期提醒，开始执行定时任务---");
        try {
            //获取参数配置对象
            ConfigDTO configDTO = configService.getConfig(ConfigConstant.BANKACC, BtBankAccConstant.FCS_CHECK_EXPIRE_DATE_ALERT_DAYS);
            //年检到期提醒时间
            Integer checkExpireDateAlertDays = EmptyUtils.isBlank(configDTO.getValue()) ? 0 : Integer.parseInt(configDTO.getValue());
            String searchDate = DateUtils.formatDate(DateUtils.addDay(DateUtils.getDate(), checkExpireDateAlertDays),DateUtils.PATTERN_CLASSICAL_SIMPLE);
            //到期账户集合
            List<BtBankAccDTO> btBankAccList = btBankAccHandleService.listByExpireDate(searchDate);
            //消息发送
            if (EmptyUtils.isEmptyList(btBankAccList)) {
                this.logger.info("---账户年检到期的数量为0---");
                notificationService.completeListByTag(NotificationConstant.NETBANK.BANKACC_CHECK_EXPIRE_DATE);
            } else {
                Map<String, Integer> noticeMap = new HashMap<>();
                noticeMap.put("count", btBankAccList.size());
                //清空“账户年检到期提醒”消息
                notificationService.completeListByTag(NotificationConstant.NETBANK.BANKACC_CHECK_EXPIRE_DATE);
                //重新发送消息
                notificationService.sendToUserByResPermission(ResourceConstant.NETBANK_BANKACC_BTBANKACCQUERY_QUERY, NotificationConstant.NETBANK.BANKACC_CHECK_EXPIRE_DATE, noticeMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.logger.info("---账户年检到期提醒，结束执行定时任务---");
    }

    @XxlJob("autoAccCancellTaskService")
    public void autoAccCaloell(String arguments){
        XxlJobHelper.log("开始执行：保证金账户自动销户");
        String searchDate = DateUtils.formatDate(DateUtils.getIntegralEndTime(DateUtils.getDate()),DateUtils.PATTERN_CLASSICAL);
        List<BtBankAccDTO> btBankAccList = btBankAccHandleService.listByEndDate("04",searchDate);
        ICurrentUser userVo = userService.getScheduler();
        if (ObjectUtil.isNotEmpty(btBankAccList)){
            for (BtBankAccDTO acc : btBankAccList){
                acc.setStatus(-2);
                acc.setCloseDate(new Date());
                acc.setUpdateBy(userVo.getLoginName());
                acc.setUpdateDate(new Date());
            }
            XxlJobHelper.log("本次注销保证金账户数量："+btBankAccList.size());
            btBankAccService.syncBatchUpdate(btBankAccList);
        }

        XxlJobHelper.log("执行完成：保证金账户自动销户");
    }
    /**
     * 用于银行预留印鉴的id拼接
     * @param vo 页面数据
     * @return 待存储VO
     */
    public BtBankAccVO insertField(BtBankAccVO vo){
        StringBuilder sb = new StringBuilder();
        for(String s:vo.getSealDirectoryList()){
            if(StringUtils.isNotBlank(s)){
                sb.append(s).append(CommonConstant.COMMA_SPLIT);
            }
        }
        vo.setSealDirectory(sb.toString());
        return vo;
    }
    /**
     * 票据账户设置
     *
     * @param btBankAccVO
     * @param user
     */
    public void batchSetBillInfo(BtBankAccVO btBankAccVO, ICurrentUser user) {
        if (StringUtils.isNotEmpty(btBankAccVO.getId())) {
            BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
            String[] ids = btBankAccVO.getId().split(CommonConstant.COMMA_SPLIT);
            for (String id : ids) {
                boolean flag = false;
                BtBankAccDTO entity = btBankAccService.getBtBankAcc(id);
                if (BtBankAccConstant.ELECTRICBILL_YES.equals(btBankAccVO.getElectricBill())) {
                    if (!EmptyUtils.isEmptyList(entity.getCurList())) {
                        for (String curId : entity.getCurList()) {
                            if (curId.equals(rmbCurrency.getId())) {
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (!flag) {
                        throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BTBANKACC_ELECTRIC_RMB);
                    }
                }else{
                    //如果不是票据专户,流通方式为空
                    btBankAccVO.setCirculationMode("");
                }
                entity.setElectricBill(btBankAccVO.getElectricBill());
                entity.setCirculationMode(btBankAccVO.getCirculationMode());
//                btBankAccService.save(entity);
                btBankAccService.saveOrUpdateObj(entity);
            }
        }
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        BtBankAccDTO bankAcc = btBankAccService.getBtBankAcc(workFlowResult.getBusinessKey());
        Integer status = workFlowResult.isPass() ? BtBankAccConstant.STATUE_CHECK : BtBankAccConstant.STATUE_BACK;
        //福耀-修改物理删除
        if(bankAcc.getStatus().equals(BtBankAccConstant.STATUE_DEL)&& status.equals(BtBankAccConstant.STATUE_CHECK)){
        	btBankAccService.deleteEntity(bankAcc);
        }else {
            bankAcc.setStatus(status);
            bankAcc.setUpdateBy(user.getLoginName());
            bankAcc.setUpdateDate(DateUtils.getDate());
            btBankAccService.update(bankAcc);
        }
        // 消息提醒
        //判断是否已经发送过消息
        boolean result = true;
        //result = notificationService.listByBusinessIdAndTag(bankAcc.getId(), NotificationConstant.NETBANK.BANKACC_REQ_HANDLE_SUCCESS);
        // 改为有操作日志判断，如果日志存在1条则发送，如果多条则不发送
        List<BtBankAccMessage> btBankAccMessages = btBankAccMessageService.listMessage(bankAcc.getId(), BtBankAccMessageConstant.BUS_TYPE_APPHANDLE);
        if (ObjectUtil.isNotEmpty(btBankAccMessages) && btBankAccMessages.size() > 1){
            logger.info("存在日志记录，跳过通知发送 {}", JsonUtils.toJSONString(bankAcc));
            result = false;
        }
        // 如果银行类别为bankType：8500的时候不发送消息
        BankTypeDTO bankType = bankTypeService.getObjectById(bankAcc.getBankTypeId());
        if (ObjectUtil.isNotEmpty(bankType) && "8500".equals(bankType.getBankType())){
            logger.info("银行类型为 8500，跳过通知发送 {}", JsonUtils.toJSONString(bankAcc));
            result = false;
        }
        if(workFlowResult.isPass() && result){
            logger.info("【银行账户开户办理成功】发送消息 {}", JsonUtils.toJSONString(bankAcc));
            notificationService.send(bankAcc.getId(), bankAcc.getCorpId(), NotificationConstant.NETBANK.BANKACC_REQ_HANDLE_SUCCESS, bankAcc);
        }
        Integer messageStatus = workFlowResult.isPass() ? BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_PASS : BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        //生成日志
        btBankAccMessageService.saveMessage(bankAcc.getId(), messageStatus, "", BtBankAccMessageConstant.BUS_TYPE_APPHANDLE, user);
        //开户办理完成，通知到个人
//        notificationService.sendToUserByLoginName(bankAcc.getId(), bankAcc.getCreateBy(), NotificationConstant.NETBANK.BANKACC_REQ_HANDLE_SUCCESS, bankAcc);
        //招行 虚拟户 线上办理
        if (BtBankAccConstant.ACCTYPE_09.equals(bankAcc.getAccType()) && CommonConstant.YES.equals(bankAcc.getIsOnlineHandle())
                && BtBankAccConstant.BIS_BIF_CODE_CMB.equals(bankAcc.getBifCode())) {
            List<BtBankAccVirtual> listVirtual = btBankAccVirtualService.getListByBankAccId(bankAcc.getId());
            if (!EmptyUtils.isEmptyList(listVirtual)) {
                HashMap<String, BisVirtualAccExc> excMap = bisVirtualAccExcService.getBisVirtualAccExcMap(bankAcc.getId());
                List<BisVirtualAccExc> listExc = new ArrayList<>();
                for (BtBankAccVirtual bankAccVirtual:listVirtual) {
                    BisVirtualAccExc exc = new BisVirtualAccExc();
                    BeanUtils.copyNotNullProperties(bankAccVirtual, exc);
                    exc.setId("");
                    exc.setBankAcc(bankAcc.getId());
                    exc.setReqDate(DateUtils.getDate());
                    exc.setCreateBy(user.getLoginName());
                    exc.setCreateDate(DateUtils.getDate());
                    exc.setVoucherStat(BdlinkConstant.PRE_SEND);
                    exc.setBifCode(bankAcc.getBifCode());
                    if (excMap.containsKey(bankAccVirtual.getBankAccId()+CommonConstant.COMMA_SPLIT+bankAccVirtual.getVirtualNo())) {
                        exc.setBusType("2"); //编辑
                    } else {
                        exc.setBusType("1"); //新增
                    }
                    listExc.add(exc);
                }
                bisVirtualAccExcService.batchSave(listExc);
            }
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(NisWorkFlowConstant.BT_BANKACC_ACC_HANDLE, dataTablesInput);
        List<String> businessIdList = workFlowBusiness.getBusinessIdList();
        String businessSql = "";
        if(!EmptyUtils.isEmpty(businessIdList)){
            businessSql = SqlUtil.getInSql(businessIdList, "acc.id", true);
        }else {
            // 无待审批业务数据时直接返回空结果，避免businessSql为null时DAO查询返回所有非审批通过状态的数据
            return new DataTablesOutput(dataTablesInput.getDraw(), new ArrayList<>(), 0, 0);
        }
        DataTablesOutput output = this.btBankAccHandleService.dataTableApprove(dataTablesInput, businessSql);
        output = this.btBankAccCommonService.setCurName(output);
        List<BtBankAccVO> data = output.getData();
        List<BtBankAccUse> btBankAccUses = btBankAccUseService.listAll();
        for (BtBankAccVO entity : data){
            // 设置账号用途
            String accUseName = "";
            if (ObjectUtil.isNotEmpty(entity.getAccUseCode())){
                List<String> useCodeList = Arrays.asList(entity.getAccUseCode().split(",")).stream().filter(s -> s != null && !s.trim().isEmpty())
                        .map(String::trim).collect(Collectors.toList());
                List<String> matchedUseNames = btBankAccUses.stream()
                        .filter(accUse -> useCodeList.contains(accUse.getUseCode().trim()))
                        .map(BtBankAccUse::getUseName)
                        .collect(Collectors.toList());
                accUseName = String.join(",", matchedUseNames);
            }
            entity.setAccUseName(accUseName);
        }

        return workFlowBusiness.merge(output);
    }

    /**
     * 通过单据类型id查询银行账户信息
     * @param receiptCode
     * @return
     */
    public List<BtBankAccVO> getBtBankAccVOByReceiptType(String receiptCode) {
        List<BtBankAccVO> accDTO= btBankAccCommonService.getBtBankAccVOByReceiptType(receiptCode);
        return accDTO;
    }

    /**
     * 通过账户code查询银行账户信息
     * @param accGroupCode
     * @return
     */
    public List<BtBankAccVO> getBtBankAccVOByAccGroupCode(String accGroupCode) {
        List<BtBankAccVO> accDTO= btBankAccCommonService.getBtBankAccVOByAccGroupCode(accGroupCode);
        return accDTO;
    }

    /**
     * 银行账号新增，系统自动新增保证金账户
     *
     * @param btBankAccDTO
     * @throws Exception
     */
    public com.bytter.biz.payment.api.dto.BtBankAccDTO saveBtBankAcc(com.bytter.biz.payment.api.dto.BtBankAccDTO btBankAccDTO, ICurrentUser user) {
        BtBankAccDTO btBankAcc = new BtBankAccDTO();
        org.springframework.beans.BeanUtils.copyProperties(btBankAccDTO, btBankAcc);

        btBankAcc.setElectricBill("0");
        btBankAcc.setCurId(btCurrencyService.getRMBCurrency().getId());
        btBankAcc.setIsDomesticBank("1");
        btBankAcc.setIsOffshoreAccount("0");
        BtAccNatureDTO btAccNatureDTO = iBtAccNatureService.findByCode("0004");
        if(!EmptyUtils.isEmpty(btAccNatureDTO)){
            btBankAcc.setNatureId(btAccNatureDTO.getId());
        }
        BisAccAttributeDTO bisAccAttributeDTO = iBisAccAttributeService.findByCode("02");
        if(!EmptyUtils.isEmpty(bisAccAttributeDTO)){
            btBankAcc.setAttributeId(bisAccAttributeDTO.getId());
        }
        btBankAcc.setAccType("04");
        btBankAcc.setIsCapitalPool("0");
        btBankAcc.setIsOnline("0");
        btBankAcc.setValidSign("1");
        btBankAcc.setIsOnlineHandle("0");
        List<FcBankAccGroup> fcBankAccGroups = fcBankAccGroupService.getValidPayType();
        if(!EmptyUtils.isEmpty(fcBankAccGroups)){
            btBankAcc.setFcAccGroupCode(fcBankAccGroups.get(0).getAccGroupCode());
        }
        List<FcReceiptType> fcReceiptTypes = fcReceiptTypeService.getValidFcReceiptType();
        if(!EmptyUtils.isEmpty(fcReceiptTypes)){
            btBankAcc.setReceiptCode(fcReceiptTypes.get(0).getReceiptCode());
        }
//        btBankAcc.setRates(new BigDecimal("0.0035"));
        btBankAcc.setRatesFloat("0");
        btBankAcc.setInterestCycle(-1);
        btBankAcc.setIsLimitQuota("0");

        if (!this.btBankAccCommonService.isBankAcc(btBankAcc.getBankAcc())) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKACC_UNIQUE,BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKACC_UNIQUE.getCode(),btBankAcc.getBankAcc());
        }
        if (!btBankAccService.isBtBankAccCountLicense()) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BTBANKACC_ACCOUNT_LICENSE, licenseService.getBankAccountNumber().toString());
        }
        String curIdStr = btBankAcc.getCurId();
        if (EmptyUtils.isBlank(curIdStr)) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL.getCode());
        }
        String[] curIds = curIdStr.split(",");
        if (EmptyUtils.isEmptyArray(curIds)) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL.getCode());
        }
        if (curIds.length < 1 || "".equals(curIdStr.trim())) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL.getCode());
        }
        if (!EmptyUtils.isBlank(btBankAcc.getBifCode())) {
            BisBifInitDTO bisBifInitDTO = this.bisBifInitService.getObjectById(btBankAcc.getBifCode());
            if (!EmptyUtils.isEmpty(bisBifInitDTO) && !bisBifInitDTO.getBankTypeId().equalsIgnoreCase(btBankAcc.getBankTypeId())) {
                throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BIFINIT_EQ_BANKTYPEID);
            }
        }
        btBankAcc.setCreateBy(user.getLoginName());
        btBankAcc.setCreateDate(DateUtils.getDate());
        btBankAcc.setUpdateBy(user.getLoginName());
        btBankAcc.setUpdateDate(DateUtils.getDate());
        btBankAcc.setStatus(BtBankAccConstant.STATUE_CHECK);
        String btBankAccId = this.btBankAccService.save(btBankAcc);
        if (StringUtils.isNotBlank(btBankAccId)){
            btBankAcc.setId(btBankAccId);
        }
        this.saveAccCur(btBankAcc, curIds, user);
        //新增账户自动授权
        AccountGrantDTO grantDTO = new AccountGrantDTO();
        grantDTO.setAccountId(btBankAcc.getId());
        grantDTO.setUserId(user.getId());
        grantDTO.setCorpId(btBankAcc.getCorpId());
        if(!EmptyUtils.isEmpty(btBankAcc.getIsOnline()) && BtBankAccConstant.ISONLINE_0.equals(btBankAcc.getIsOnline())){
            grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.OFFLINE_BANK_TYPE));
        }else{
            grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.ONLINE_BANK_TYPE));
        }
        iAccountGrantService.saveAccountGrant(grantDTO);

        btBankAccDTO.setCurId(btBankAcc.getCurId());
        btBankAccDTO.setId(btBankAccId);
        return btBankAccDTO;
    }
	@Override
	public void submitVerify(Map<String, List<String>> params, String workflowPass, boolean isNeedVerify)
			throws WorkFlowVerifyException {
	}

    /***
     * 批量设置非直联
     * @param ids
     * @param currentUser
     * @return TODO
     */
    public void batchUnOnline(String ids, ICurrentUser currentUser) {
        for (String id : ids.split(",")){
            BtBankAccDTO btBankAccDTO = btBankAccService.getBtBankAcc(id);
            if(btBankAccDTO != null){
                btBankAccDTO.setIsOnline(CommonConstant.NO);
                btBankAccDTO.setUpdateBy(currentUser.getLoginName());
                btBankAccDTO.setUpdateDate(DateUtils.getDate());
                btBankAccService.update(btBankAccDTO);
            }
        }
    }
}
