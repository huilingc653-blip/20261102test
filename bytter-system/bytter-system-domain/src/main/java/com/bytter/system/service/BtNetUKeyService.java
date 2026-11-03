package com.bytter.system.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.enums.BankInfoMessageEnum;
import com.bytter.system.api.enums.RatMessageEnum;
import com.bytter.system.constants.BtBankAccConstant;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.dao.BtNetUKeyBankAccDao;
import com.bytter.system.dao.BtNetUKeyDao;
import com.bytter.system.entity.BtNetUKey;
import com.bytter.system.entity.BtNetUKeyBankAcc;
import com.bytter.system.entity.User;
import com.bytter.system.vo.BtNetUKeyVO;
import com.bytter.system.vo.DictVO;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @author qinl
 * @version 1.0
 * @Description: BtNetUKeyService
 * @date 2020-04-16
 */
@Service("btNetUKeyService")
@Transactional
public class BtNetUKeyService extends BaseService<BtNetUKey, String> implements IExcelVerifyHandler<BtNetUKeyVO>{
    @Autowired
    private BtNetUKeyDao btNetUKeyDao;
    @Autowired
    private BtNetUKeyBankAccDao btNetUKeyBankAccDao;
    @Autowired
    private BtNetUKeyBankAccService btNetUKeyBankAccService;
    @Autowired(required=false)
    private ICorpService corpServiceI;
//    @Autowired
//    private BtBankAccService btBankAccService;//业务处理放在结算
    @Autowired
    private IBtBankAccService btBankAccServiceI;
    @Autowired(required=false)
    private IBtCurrencyService btCurrencyServiceI;
    @Autowired(required = false)
    private UserService userService;
    @Autowired(required = false)
    private IUserService userServiceI;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired(required=false)
    private IConfigService configServiceI;
    @Autowired(required=false)
    private IBankTypeService bankTypeService;
    @Autowired
    private DictService dictService;
    @Override
    public BaseDao<BtNetUKey, String> getEntityDao() {
        return btNetUKeyDao;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,ICurrentUser currentUser) {
        String accCorpAuthSql = SqlUtil.getInSql(
                corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTNETUKEY_QUERY, currentUser.getId()), "b.corp_id",
                true);
        DataTablesOutput dataTablesOutput = btNetUKeyDao.getDataTablesUnion(dataTablesInput,accCorpAuthSql);
        //填充属性值
        this.setAttributes(dataTablesOutput);
        return dataTablesOutput;
    }
    /**
     * 保存
     * @param vo
     */
    public void save(BtNetUKeyVO vo){
        if(checkUKey(vo.getUkeyNo())){
            throw new ParameterException(RatMessageEnum.CMS_RAT_CODE_EXIST);
        }
        if(StringUtils.isBlank(vo.getBankAccIds())){
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKACC_NULL,"","");
        }
        //保存ukey
        BtNetUKey btNetUKey = BeanUtils.copyNewInstanceProperties(vo, BtNetUKey.class);
        this.saveEntity(btNetUKey);
        //保存ukey的账号
        String[] bankAccIds = vo.getBankAccIds().split(CommonConstant.COMMA_SPLIT);
        for(String bankAccId:bankAccIds){
            BtNetUKeyBankAcc entity = new BtNetUKeyBankAcc();
            entity.setBankAccId(bankAccId);
            entity.setuKeyId(btNetUKey.getId());
            btNetUKeyBankAccService.saveEntity(entity);
        }
    }
    /**
     * 修改数据
     * @param vo
     * @param uKeyNoCheck
     */
    public void modify(BtNetUKeyVO vo,String uKeyNoCheck){
        if(vo.getUkeyNo().equals(uKeyNoCheck)){//没有更改ukey
            if(StringUtils.isBlank(vo.getBankAccIds())){
                throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKACC_NULL,"","");
            }
            BtNetUKey byUKeyNo = btNetUKeyDao.getByUKeyNo(vo.getUkeyNo());
            List<BtNetUKeyBankAcc> listByUKeyId = btNetUKeyBankAccService.getListByUKeyId(byUKeyNo.getId());
            //删除所有的账号
            for (BtNetUKeyBankAcc bankAcc : listByUKeyId) {
                deleteBankAcc(bankAcc.getId());
            }
            //更新ukey
            BeanUtils.copyNotNullProperties(vo,byUKeyNo);
            this.updateEntity(byUKeyNo);
            //保存ukey的账号
            String[] bankAccIds = vo.getBankAccIds().split(CommonConstant.COMMA_SPLIT);
            for(String bankAccId:bankAccIds){
                BtNetUKeyBankAcc entity = new BtNetUKeyBankAcc();
                entity.setBankAccId(bankAccId);
                entity.setuKeyId(byUKeyNo.getId());
                btNetUKeyBankAccService.saveEntity(entity);
            }
        }else{
            if(checkUKey(vo.getUkeyNo())){
                throw new ParameterException(RatMessageEnum.CMS_RAT_CODE_EXIST);
            }
            if(StringUtils.isBlank(vo.getBankAccIds())){
                throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKACC_NULL,"","");
            }
            BtNetUKey byUKeyNo = btNetUKeyDao.getByUKeyNo(uKeyNoCheck);
            List<BtNetUKeyBankAcc> listByUKeyId = btNetUKeyBankAccService.getListByUKeyId(byUKeyNo.getId());
            //删除所有的账号包括ukey
            for (BtNetUKeyBankAcc bankAcc : listByUKeyId) {
                delete(bankAcc.getId());
            }
            //更新ukey
            BtNetUKey btNetUKey = BeanUtils.copyNewInstanceProperties(vo, BtNetUKey.class);
            this.saveEntity(btNetUKey);
            //保存ukey的账号
            String[] bankAccIds = vo.getBankAccIds().split(CommonConstant.COMMA_SPLIT);
            for(String bankAccId:bankAccIds){
                BtNetUKeyBankAcc entity = new BtNetUKeyBankAcc();
                entity.setBankAccId(bankAccId);
                entity.setuKeyId(btNetUKey.getId());
                btNetUKeyBankAccService.saveEntity(entity);
            }
        }
    }
    /**
     * 根据id获取网银信息的vo
     * @param id
     * @return
     */
    public BtNetUKeyVO getVOById(String id){
        BtNetUKeyVO btNetUKeyVO = btNetUKeyDao.selectById(id);
        //单位信息
        CorpDTO corpDto = corpServiceI.get(btNetUKeyVO.getCorpId());
        //银行类型
        BankTypeDTO bankTypeDto =bankTypeService.getObjectById(btNetUKeyVO.getBankTypeId());
//        //ukey用户
//        UserDTO ukeyUser = userServiceI.getByUserId(btNetUKeyVO.getUkeyUserId());
        //保管人
        UserDTO custodianUser = userServiceI.getByUserId(btNetUKeyVO.getCustodianId());
        if(!EmptyUtils.isEmpty(corpDto)){
            btNetUKeyVO.setCorpName(corpDto.getName());
        }
        if(!EmptyUtils.isEmpty(bankTypeDto)){
            btNetUKeyVO.setBankTypeName(bankTypeDto.getTypeName());
        }
//        if(!EmptyUtils.isEmpty(ukeyUser)){
//            btNetUKeyVO.setUkeyUserName(ukeyUser.getUsername());
//        }
        if(!EmptyUtils.isEmpty(custodianUser)){
            btNetUKeyVO.setCustodianName(custodianUser.getUsername());
        }
        if(!EmptyUtils.isEmpty(btNetUKeyVO.getSupervisorId())){
            //监交人
            UserDTO supervisorUser = userServiceI.getByUserId(btNetUKeyVO.getSupervisorId());
            if(!EmptyUtils.isEmpty(supervisorUser)){
                btNetUKeyVO.setSupervisorName(supervisorUser.getUsername());
            }
        }

        return btNetUKeyVO;
    }
    public void delete(String id,ICurrentUser user){
        BtNetUKeyBankAcc btNetUKeyBankAcc = btNetUKeyBankAccService.get(id);
        String keyId = btNetUKeyBankAcc.getuKeyId();
        BtNetUKeyVO btNetUKey = this.getVOById(keyId);
        if(!user.getUsername().equals(btNetUKey.getCustodianName())){
            throw new ParameterException(BankInfoMessageEnum.TABLE_BANKACC_BTNETUKEY_DELETE_NOTCORRECT,btNetUKey.getUkeyNo());
        }
        btNetUKeyBankAccService.delete(btNetUKeyBankAcc.getId());
        getSession().flush();
        long num = btNetUKeyBankAccService.associateBankAccNum(keyId);
        if(num==0){
            btNetUKeyDao.delete(keyId);
        }
    }
    /**
     * 删除绑定的银行账号
     * @param id
     */
    public void deleteBankAcc(String id){
        btNetUKeyBankAccService.delete(id);
    }
    /**
     * 检查UKey是否存在
     * @returnf
     */
    public boolean checkUKey(String uKeyNo){
        BtNetUKey btNetUKey = btNetUKeyDao.getByUKeyNo(uKeyNo);
        return btNetUKey != null;
    }
    @XxlJob("BtNetUKeyService")
    public void run(String arguments) {
        Date curDate = DateUtils.getDate();
        this.logger.info(curDate + "---网银到期提醒，开始执行定时任务---");
        try {
            //获取参数配置对象
            ConfigDTO configDTO = configServiceI.getConfig(ConfigConstant.BANKACC, BtBankAccConstant.NET_UKEY_DATE_ALERT_DAYS);
            //网银到期提醒时间
            Integer checkExpireDateAlertDays = EmptyUtils.isBlank(configDTO.getValue()) ? 30 : Integer.parseInt(configDTO.getValue());
            String searchDate = DateUtils.formatDate(DateUtils.addDay(DateUtils.getDate(), checkExpireDateAlertDays),DateUtils.PATTERN_CLASSICAL_SIMPLE);
            //到期账户集合
            List<BtNetUKeyVO> expireDataList = btNetUKeyDao.getExpireDataEqual(searchDate);
            String searchDate1 = DateUtils.formatDate(DateUtils.addDay(DateUtils.getDate(), 10),DateUtils.PATTERN_CLASSICAL_SIMPLE);
            //到期账户集合
            List<BtNetUKeyVO> expireDataList1 = btNetUKeyDao.getExpireData(searchDate1);
            expireDataList.addAll(expireDataList1);
            int expireData = expireDataList.size();
            //消息发送
            if (expireData == 0) {
                this.logger.info("---数量为0---");
                notificationService.completeListByTag(NotificationConstant.NETBANK.BANKACC_CHECK_NETUKEY_DATE);
            } else {
                //清空“网银到期到期提醒”消息
                notificationService.completeListByTag(NotificationConstant.NETBANK.BANKACC_CHECK_NETUKEY_DATE);
                for(BtNetUKeyVO vo : expireDataList){
                    Map<String, Object> noticeMap = new HashMap<>();
                    noticeMap.put("expireDate", DateUtils.format(vo.getExpireDate(),"yyyy-MM-dd"));
                    noticeMap.put("accName", vo.getAccName());
                    noticeMap.put("bankAcc", vo.getBankAcc());
                    noticeMap.put("bankName", vo.getBankName());
                    String personalLimit = Objects.isNull(dictService.getDictByDictGroupAndCode("personal.permissions", vo.getPersonalLimit())) ? "" : dictService.getDictByDictGroupAndCode("personal.permissions", vo.getPersonalLimit()).getName();
                    noticeMap.put("personalLimit", personalLimit);
                    noticeMap.put("ukeyNo", vo.getUkeyNo());
                    //重新发送消息
                    notificationService.sendToUserByResPermission(vo.getId(),ResourceConstant.NETBANK_BANKACC_BTNETUKEY_QUERY, NotificationConstant.NETBANK.BANKACC_CHECK_NETUKEY_DATE, noticeMap,vo.getCorpId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.logger.info("---网银到期提醒，结束执行定时任务---");
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(BtNetUKeyVO btNetUKeyVO) {
        return null;
    }
    @Override
    public void verifyHandler(List<BtNetUKeyVO> list) {
        List<String> filterAccount= new ArrayList<>();//账号重复校验
        Map<String,String> filterCorp= new HashMap<>();//单位重复校验
        for(BtNetUKeyVO vo:list){
            List<String> msgList = new ArrayList<>();
            BtNetUKey byUKeyNo = btNetUKeyDao.getByUKeyNo(vo.getUkeyNo());

            //权限校验的 字典映射  如果正确则进行映射
            boolean flag = true;
            //权限校验
            if("1".equals(vo.getBankLimitName())){
                if(!"1".equals(vo.getPersonalLimitName()) || StringUtils.isBlank(vo.getPersonalLimitName())){
                    msgList.add("table.bankacc.btnetukey.personalLimit.notCorrect");// 个人权限不正确
                    flag = false;
                }
            }else if("2".equals(vo.getBankLimitName())){
                if(!("2".equals(vo.getPersonalLimitName())
                        || "3".equals(vo.getPersonalLimitName())
                        || "4".equals(vo.getPersonalLimitName()))
                        || StringUtils.isBlank(vo.getPersonalLimitName())){
                    msgList.add("table.bankacc.btnetukey.personalLimit.notCorrect");// 个人权限不正确
                    flag = false;
                }
            }else{
                msgList.add("table.bankacc.btnetukey.bankLimit.notCorrect");//银行权限不正确
                flag = false;
            }
            //权限正确
            if(flag){
                vo.setBankLimit(vo.getBankLimitName());
                vo.setPersonalLimit(vo.getPersonalLimitName());
                DictVO bankPer = dictService.getDictByDictGroupAndCode("bank.permissions", vo.getBankLimit());
                if(bankPer != null){
                    vo.setBankLimitName(bankPer.getName());
                }
                DictVO personalPer = dictService.getDictByDictGroupAndCode("personal.permissions", vo.getPersonalLimit());
                if(personalPer != null){
                    vo.setPersonalLimitName(personalPer.getName());
                }
            }

            if(StringUtils.isBlank(vo.getIsHeadCustodianName())){
                msgList.add("是否总部保管不能为空");
            }else{
                if("是".equals(vo.getIsHeadCustodianName())){
                    vo.setIsHeadCustodian("1");
                }else if("否".equals(vo.getIsHeadCustodianName())){
                    vo.setIsHeadCustodian("0");
                }else{
                    msgList.add("是否总部保管不正确");
                }
            }
            //获取银行账号
            BtBankAccDTO bank = btBankAccServiceI.getBankAccInfo(vo.getBankAcc());
            if(bank==null){
                msgList.add("nis.netbank.message.bankaccinfo.null");//银行账号不存在！
                vo.addErrorMsg(msgList);
                continue;
            }
            if(bank!=null && !bank.getBankName().equals(vo.getBankName())){
                msgList.add("sys.externalCorp.import.bank.match");//开户行不匹配
            }
            vo.setBankTypeId(bank.getBankTypeId());
            vo.setBankAccIds(bank.getId());
            if(byUKeyNo!=null){//如果存在ukey
                BeanUtils.copyNotNullProperties(byUKeyNo,vo);
                if(!byUKeyNo.getBankTypeId().equals(bank.getBankTypeId())){
//                    msgList.add("nis.netbank.message.btbanktype.null");//银行类型不存在！
                    msgList.add("当前账号银行类型与U-key银行类型不一致");//银行类型不存在！
                }
                //账号校验
                List<BtNetUKeyBankAcc> listUkey = btNetUKeyBankAccService.getListByUKeyId(byUKeyNo.getId());
                for(BtNetUKeyBankAcc acc:listUkey){
                    filterAccount.add(byUKeyNo.getUkeyNo()+acc.getBankAccId());
                }
                if(!filterAccount.contains(byUKeyNo.getUkeyNo()+bank.getId())){
                    filterAccount.add(byUKeyNo.getUkeyNo()+bank.getId());
                }else{
                    msgList.add("inner.account.message.union");//   重复提交，此账号已存在!
                }
                //用户校验
                if(StringUtils.isBlank(vo.getCustodianName())){
                    msgList.add("table.bankacc.btnetukey.custodian.notCorrect");//保管人不存在
                }else{
                    User user1 = userService.getByLoginName(vo.getCustodianName());
                    if(user1==null){
                        user1 = userService.getByUserName(vo.getCustodianName());
                        if (user1==null){
                            msgList.add("table.bankacc.btnetukey.custodian.notCorrect");//保管人不存在
                        }
                    }
                    if (user1!=null){
                        if(!byUKeyNo.getCustodianId().equals(user1.getId())){
                            msgList.add("保管人无权限操作");//当前保管人无权限
                        }
                    }
                }
            }else{//如果不存在ukey
                if(StringUtils.isBlank(vo.getUkeyNo())){
                    msgList.add("table.bankacc.btnetukey.uKey.notCorrect");//ukey不能为空
                    vo.addErrorMsg(msgList);
                    continue;
                }
                if(StringUtils.isBlank(vo.getCorpName())){
                    msgList.add("ttpay.message.corpid.isnotexist");//所属单位不存在!
                }else{
                    CorpDTO corpIdByName = corpServiceI.getCorpIdByName(vo.getCorpName());
                    if(corpIdByName==null){
                        msgList.add("ttpay.message.corpid.isnotexist");//所属单位不存在!
                    }else{
                        vo.setCorpId(corpIdByName.getId());
                    }
                }
                //用户校验
                if(StringUtils.isBlank(vo.getCustodianName())){
                    msgList.add("table.bankacc.btnetukey.custodian.notCorrect");//保管人不存在
                }else{
                    User user1 = userService.getByLoginName(vo.getCustodianName());
                    if(user1==null){
                        user1 = userService.getByUserName(vo.getCustodianName());
                        if (user1 == null){
                            msgList.add("table.bankacc.btnetukey.custodian.notCorrect");//保管人不存在
                        }
                    }
                    if(user1!=null){
                        ICurrentUser user = UserContextHelper.helper().getCurrentUser();
                        if(user.getLoginName().equals(vo.getCustodianName())
                                ||user.getUsername().equals(vo.getCustodianName())){
                            vo.setCustodianId(user1.getId());
                        }else{
                            msgList.add("保管人应为当前登录用户");//当前保管人无权限
                        }
                    }
                }
//                if(StringUtils.isBlank(vo.getUkeyUserName())){
//                    msgList.add("table.bankacc.btnetukey.uKeyUser.notCorrect");//UKey用户不存在
//                }else{
//                    User user2 = userService.getByLoginName(vo.getUkeyUserName());
//                    if(user2==null){
//                        msgList.add("table.bankacc.btnetukey.uKeyUser.notCorrect");//UKey用户不存在
//                    }else{
//                        vo.setUkeyUserId(user2.getId());
//                    }
//                }
                vo.setUkeyUserId(vo.getUkeyUserName());
                if(StringUtils.isBlank(vo.getSupervisorName())){
                    msgList.add("table.bankacc.btnetukey.supervisor.notCorrect");//监交人不存在
                }else{
                    User user3 = userService.getByLoginName(vo.getSupervisorName());
                    if(user3==null){
                        user3 = userService.getByUserName(vo.getSupervisorName());
                        if(user3==null){
                            msgList.add("table.bankacc.btnetukey.supervisor.notCorrect");//监交人不存在
                        }
                    }
                    if(user3!=null){
                        vo.setSupervisorId(user3.getId());
                    }
                }
                //日期校验
                if(vo.getExpireDate()==null || vo.getOpenDate() ==null){
                    msgList.add("bis.accbal.message.baldate.null");//	到期日期不能为空
                }else{
                    if(vo.getOpenDate()!=null){
                        long millis1 = DateUtils.getMillis(vo.getOpenDate());
                        long millis2 = DateUtils.getMillis(vo.getExpireDate());
                        if((millis1-millis2)>0){
                            msgList.add("nis.fcAcc.message.earlier.expiration.activation");//   到期日期不能小于启用日期
                        }
                    }
                }
                //账号校验
                List<BtNetUKeyBankAcc> listUkey = btNetUKeyBankAccService.getListByUKeyCode(vo.getUkeyNo());
                for(BtNetUKeyBankAcc acc:listUkey){
                    filterAccount.add(vo.getUkeyNo()+acc.getBankAccId());
                }
                if(!filterAccount.contains(vo.getUkeyNo()+bank.getId())){
                    filterAccount.add(vo.getUkeyNo()+bank.getId());
                }else{
                    msgList.add("inner.account.message.union");//   重复提交，此账号已存在!
                }
                //单位校验
                if(filterCorp.containsKey(vo.getUkeyNo())){
                    if(!vo.getCorpName().equals(filterCorp.get(vo.getUkeyNo()))){
                        msgList.add("当前U-key的单位应该为"+filterCorp.get(vo.getUkeyNo()));//   重复提交，此账号已存在!
                    }
                }else{
                    filterCorp.put(vo.getUkeyNo(),vo.getCorpName());
                }
            }
            //处理
            vo.addErrorMsg(msgList);
        }
    }
    /**
     * <p>
     * 校验成功后，保存操作
     * </p>
     *
     * @param list        [待保存的excel]
     * @param currentUser [操作用户]
     */
    public void saveImport(List<BtNetUKeyVO> list, ICurrentUser currentUser) {
        List<BtNetUKeyVO> addList = new ArrayList<>();
        Map<String,BtNetUKeyVO> mapFilter = new HashMap<>();
        //过滤重复的ukey
        for(BtNetUKeyVO vo:list){
            if(!mapFilter.containsKey(vo.getBankAccIds()+vo.getUkeyNo())){
                mapFilter.put(vo.getBankAccIds()+vo.getUkeyNo(),vo);
            }
        }
        for(String key:mapFilter.keySet()){
            addList.add(mapFilter.get(key));
        }
        for(BtNetUKeyVO vo:addList){
            if(StringUtils.isNotBlank(vo.getId())){
                BtNetUKeyBankAcc bankAcc=new BtNetUKeyBankAcc();
                bankAcc.setuKeyId(vo.getId());
                bankAcc.setBankAccId(vo.getBankAccIds());
                btNetUKeyBankAccService.saveEntity(bankAcc);
            }else{
                BtNetUKey byUKeyNo = this.btNetUKeyDao.getByUKeyNo(vo.getUkeyNo());
                if(byUKeyNo==null){//不存在ukey
                    BtNetUKey btNetUKey = BeanUtils.copyNewInstanceProperties(vo, BtNetUKey.class);
                    this.saveEntity(btNetUKey);
                    BtNetUKeyBankAcc bankAcc=new BtNetUKeyBankAcc();
                    bankAcc.setuKeyId(btNetUKey.getId());
                    bankAcc.setBankAccId(vo.getBankAccIds());
                    btNetUKeyBankAccService.saveEntity(bankAcc);
                }else{//存在ukey
                    BtNetUKeyBankAcc bankAcc=new BtNetUKeyBankAcc();
                    bankAcc.setuKeyId(byUKeyNo.getId());
                    bankAcc.setBankAccId(vo.getBankAccIds());
                    btNetUKeyBankAccService.saveEntity(bankAcc);
                }
            }
            getSession().flush();
        }
    }

    /**
     * 填充属性
     * @param dataTablesOutput
     */
    public void setAttributes(DataTablesOutput dataTablesOutput){
        List<BtNetUKeyVO> list = dataTablesOutput.getData();
        if(list.size() > 0){
            //获取单位信息
            Map<String, CorpDTO> corpMap = corpServiceI.getCorpMap();
            //获取用户信息
            Map<String, UserDTO> userMap = userServiceI.listUser();
            for(BtNetUKeyVO vo : list){
                //单位名称
                if(corpMap != null){
                    if(corpMap.containsKey(vo.getCorpId())){
                        String corpName = corpMap.get(vo.getCorpId()) != null ? corpMap.get(vo.getCorpId()).getName(): "";
                        vo.setCorpName(corpName);
                    }
                }
                if(!EmptyUtils.isEmpty(userMap)){
//                    //ukey用户
//                    if(userMap.containsKey(vo.getUkeyUserId())){
//                        String ukeyUserName = userMap.get(vo.getUkeyUserId()) != null ? userMap.get(vo.getUkeyUserId()).getUsername(): "";
//                        vo.setUkeyUserName(ukeyUserName);
//                    }
                    //保管人
                    if(userMap.containsKey(vo.getCustodianId())){
                        String custodianName = userMap.get(vo.getCustodianId()) != null ? userMap.get(vo.getCustodianId()).getUsername(): "";
                        vo.setCustodianName(custodianName);
                    }
                    //监交人
                    if(!EmptyUtils.isEmpty(vo.getSupervisorId())){
                        if(userMap.containsKey(vo.getSupervisorId())){
                            String supervisorName = userMap.get(vo.getSupervisorId()) != null ? userMap.get(vo.getSupervisorId()).getUsername(): "";
                            vo.setSupervisorName(supervisorName);
                        }
                    }
                }
            }
        }
    }
}
