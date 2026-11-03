package com.bytter.system.inneraccount.service.impl;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.hibernate.internal.SQLQueryImplAdapter;
import com.bytter.core.constant.*;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.constant.SysResourceConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.*;
import com.bytter.system.enums.InnerAccountMessageEnum;
import com.bytter.system.inneraccount.dao.FcAccDao;
import com.bytter.system.inneraccount.entity.FcAcc;
import com.bytter.system.inneraccount.service.FcAccService;
import com.bytter.system.inneraccount.util.InnerAccountUtils;
import com.bytter.system.service.CorpService;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboService;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 账户联机余额对外接口实现类
 *
 * @author liyh
 */
@Service("fcAccServiceImpl")
@DubboService
@Transactional
public class FcAccServiceImpl extends BaseDao<FcAcc, String> implements IFcAccService {
    @Autowired
    private FcAccDao fcAccDao;
    @Autowired
    private FcAccService fcAccService;
    @Autowired
    private CorpService corpService;
    @Autowired
    private IConfigService configServiceI;
    @Autowired
    private IFcCorpPoolService fcCorpPoolServiceI;
    @Autowired
    private IDictService dictServiceI;

    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private IBtCurrencyService btCurrencyServiceI;
    /**
     * 内部账号表All
     *
     * @return
     */
    public String getCommonFcAccUnion() {
        StringBuilder strB = new StringBuilder();
        strB.append( "select t.id as id, t.update_Date as updateDate,t.blnctr_Acc as blnctrAcc," );
        strB.append(        "t.acc_Name as accName,t.net_Id as netId,net.name as netName," );
        strB.append(        "t.corp_Id as corpId,t.cur_Id as curId,cur.cur_name AS curName," );
        strB.append(        "t.subject_Id as subjectId,f.sys_subject_code as sysSubjectCode,f.subject_name as subjectName,");
        strB.append(        "t.acc_Type as accType,t.attribute_Id as attributeId,t.nature_Code_Id as natureCodeId," );
        strB.append(        "a.ATTRIBUTE_NAME as attributeName,n.NATURE_NAME as natureName," );
        strB.append(        "t.bank_Code as bankCode,t.Delay_Rate as DelayRate," );
        strB.append(        "t.balance_Way as balanceWay,t.interest_Sign as interestSign," );
        strB.append(        "t.interest_Start_Date as interestStartDate,t.interest_Rate as interestRate," );
        strB.append(        "t.overdraft_Sign as overdraftSign," );
        strB.append(        "t.overdraft_Rate as overdraftRate,t.overdraft_Money as overdraftMoney,");
        strB.append(        "t.contract_No as contractNo,t.deadline_Date as deadlineDate," );
        strB.append(        "t.delay_Deadline_Date as delayDeadlineDate,t.overdue_Rate as overdueRate,");
        strB.append(        "t.acc_For_Deduct as accForDeduct,t.deduct_Mode as deductMode," );
        strB.append(        "t.notify_Type as notifyType,t.acc_State as accState," );
        strB.append(        "t.agreed_Deposit_Sign as agreedDepositSign,t.predraw_Sign as predrawSign," );
        strB.append(        "t.if_Compare as ifCompare,t.acc_Id as accId," );
        strB.append(        "t.acc_Start_Date as accStartDate,t.operator_Code as operatorCode," );
        strB.append(        "t.acc_Open_Time as accOpenTime,t.handle_Charge as handleCharge," );
        strB.append(        "t.shortcut as shortcut,t.union_Net as unionNet," );
        strB.append(        "t.union_Acc as unionAcc,t.precation_Acc as precationAcc," );
        strB.append(        "t.precation_Corp as precationCorp,t.poundage_Rate as poundageRate,");
        strB.append(        "t.charge_Acc as chargeAcc,t.transact_Way as transactWay," );
        strB.append(         "t.valid_Sign as validSign,t.check_Back_Reason as checkBackReason," );
        strB.append(        "t.end_Unit as endUnit,t.certificate_Way as certificateWay,t.check_time as checkTime," );
        strB.append(        "t.create_By as createBy,t.create_Date as createDate,t.update_By as updateBy,t.checker_code as checkerCode,");
        strB.append(        "t.acc_Cussent as accCussent,f.measure_Unit as measureUnit," );
        strB.append(        "t.pool_Sign as poolSign,t.RELATION_ACC as relationAcc,t.rmk as rmk, " );
        strB.append(        "t.interest_in_exp as interestInExp,t.interest_rec_pay as interestRecPay, " );
        strB.append(        "t.reference_Rate_Type as referenceRateType" );
        strB.append(        " from Fc_Acc t " );
        strB.append(        " left join sys_corp net ON t.net_id=net.id " );
        strB.append(        " left join bt_currency cur on t.cur_id=cur.id " );
        strB.append(        " left join BT_ACC_NATURE n on t.nature_Code_Id=n.id " );
        strB.append(        " left join BIS_ACC_ATTRIBUTE a on t.attribute_Id=a.id " );
//				" left join fc_online_ledger fl on t.blnctr_acc = fl.account " +
        strB.append(        " left join fc_subject f on t.subject_id = f.id ");
        return strB.toString();
    }
    /**
     * 资金池账号查询语句
     *
     * @return
     */
    public String getCommonFcAccUnionForPool() {//f.sys_subject_code as sysSubjectCode,f.measure_Unit as measureUnit,
        String sql = "select t.id as id," + "t.update_Date as updateDate,t.blnctr_Acc as blnctrAcc," +
                "t.acc_Name as accName,t.net_Id as netPoolId," +
                "t.corp_Id as corpId,t.cur_Id as curId," +
                "t.subject_Id as subjectPoolId,f.subject_name as subjectPoolName,f.subject_name as subjectName," +
                "t.acc_Type as accType,t.attribute_Id as attributeId,t.nature_Code_Id as natureCodeId," +
                "a.ATTRIBUTE_NAME as attributeName,n.NATURE_NAME as natureName," +
                "t.bank_Code as bankCode,t.Delay_Rate as DelayRate," +
                "t.balance_Way as balanceWay,t.interest_Sign as interestSign," +
                "t.interest_Start_Date as interestStartDate,t.interest_Rate as interestRate," +
                "t.overdraft_Sign as overdraftSign," +
                "t.overdraft_Rate as overdraftRate,t.overdraft_Money as overdraftMoney," +
                "t.contract_No as contractNo,t.deadline_Date as deadlineDate," +
                "t.delay_Deadline_Date as delayDeadlineDate,t.overdue_Rate as overdueRate," +
                "t.acc_For_Deduct as accForDeduct,t.deduct_Mode as deductMode," +
                "t.notify_Type as notifyType,t.acc_State as accState," +
                "t.agreed_Deposit_Sign as agreedDepositSign,t.predraw_Sign as predrawSign," +
                "t.if_Compare as ifCompare,t.acc_Id as accId," +
                "t.acc_Start_Date as accStartDate,t.operator_Code as operatorCode," +
                "t.acc_Open_Time as accOpenTime,t.handle_Charge as handleCharge," +
                "t.shortcut as shortcut,t.union_Net as unionNet," +
                "t.union_Acc as unionAcc,t.precation_Acc as precationAcc," +
                "t.precation_Corp as precationCorp,t.poundage_Rate as poundageRate," +
                "t.charge_Acc as chargeAcc,t.transact_Way as transactWay," +
                "t.valid_Sign as validSign,t.check_Back_Reason as checkBackReason," +
                "t.end_Unit as endUnit,t.certificate_Way as certificateWay,t.check_time as checkTime," +
                "t.create_By as createBy,t.create_Date as createDate,t.update_By as updateBy,t.checker_code as checkerCode," +
                "t.acc_Cussent as accCussent,fl.check_balance - fl.freeze_money as fcBalance, " +
                "t.pool_Sign as poolSign " +
                " from Fc_Acc t " +
                " left join BT_ACC_NATURE n on t.nature_Code_Id=n.id " +
                " left join BIS_ACC_ATTRIBUTE a on t.attribute_Id=a.id " +
//                " left join fc_online_ledger fl on t.blnctr_acc = fl.account " +
                " left join fc_subject_pool f on t.subject_id = f.id ";
        return sql;
    }
    /**
     * 处理oracle sql 语句in子句中超过1000项就会报错 <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param ids   in语句中的集合对象
     * @param count in语句中出现的条件个数
     * @param field in语句对应的数据库查询字段
     * @return field in (...) or field in (...) 字符串
     */
    @Override
    protected String getSQLIn(List<?> ids, int count, String field) {
        count = Math.min(count, 1000);
        int len = ids.size();
        int size = len % count;
        if (size == 0) {
            size = len / count;
        } else {
            size = (len / count) + 1;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int fromIndex = i * count;
            int toIndex = Math.min(fromIndex + count, len);
            String productId = StringUtils.defaultIfEmpty(StringUtils.join(ids.subList(fromIndex, toIndex), "','"), "");
            if (i != 0) {
                builder.append(" or ");
            }
            builder.append(field).append(" in ('").append(productId).append("')");
        }
        if (EmptyUtils.isNotEmpty(builder.toString())) {
            return "(" + builder + ")";
        }
        return field + " in ('')";
    }
    @Override
    public List<FcAccDTO> getFcAccBySubjectCode(String chargeAccount, String netId, String subjectCode) {
        return fcAccDao.getFcAccBySubjectCode(chargeAccount, netId, subjectCode);
    }
    /**
     * 功能介绍 : 判断账户是否同币别。<br>
     * <b>日期：</b>2017年7月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Override
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
    @Override
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
     * 功能介绍  ： 查询相关科目账号。<br>
     * <b>日期：</b>2017年7月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Override
    public List<FcAccDTO> getFcAccByCorpSubjectCode(String netId, String corpId, String subjectCode) {
        return fcAccDao.getFcAccByCorpSubjectCode(netId, corpId, subjectCode);
    }
    /**
     * 根据系统科目查询账户
     *
     * @param sysSubjectId
     * @param netIds
     * @param corpIds
     * @param curId
     */
    @Override
    public List<FcAccDTO> getFcAccBySysSubjectId(String sysSubjectId, String netIds, Set<String> corpIds,
                                                 String curId) {
        List<FcAccDTO> dtoList = fcAccDao.findBySysSubjectId(sysSubjectId, netIds, corpIds, curId);
        List<FcAccDTO> list = BeanUtils.copyListProperties(dtoList, FcAccDTO.class);
        return list;
    }

    @Override
    public List<FcAccDTO> getFcAccBySysSubject(String sysSubject, String netIds, List<String> corpIds,
                                                 List<String> curIds) {
        List<FcAccDTO> dtoList = fcAccDao.findBySysSubject(sysSubject, netIds, corpIds, curIds);
        List<FcAccDTO> list = BeanUtils.copyListProperties(dtoList, FcAccDTO.class);
        return list;
    }
    /**
     * 根据科目id,系统科目code查询账户(过滤掉关联了银行代码)
     *
     * @param subjectId
     * @param netIds
     * @param corpIds
     * @param curId
     */
    @Override
    public List<FcAccDTO> getFcAccBySysSubjectIdFilterBankCode(String subjectId, String sysSubjectCode, String netIds, Set<String> corpIds,
                                                               String curId) {
        List<FcAccDTO> dtoList = fcAccDao.findBySysSubjectIdFilterBankCode(subjectId, sysSubjectCode, netIds, corpIds, curId);
        List<FcAccDTO> list = BeanUtils.copyListProperties(dtoList, FcAccDTO.class);
        return list;
    }
    /**
     * 查询相关科目账号
     *
     * @param netId      　--网点
     * @param corpId     　--单位
     * @param subjectId  　--科目
     * @param curId      　--币种
     * @param instrtType 　--利率类型
     * @param account    　--账号
     * @return
     */
    @Override
    public List<FcAccDTO> getFcAccByParams(String netId, String corpId, String subjectId,
                                           String curId, String instrtType, String account) {
        FcAcc fcAcc = new FcAcc();
        fcAcc.setNetId(netId);
        fcAcc.setCorpId(corpId);
        fcAcc.setSubjectId(subjectId);
        fcAcc.setCurId(curId);
        fcAcc.setBlnctrAcc(account);
        List<FcAccVO> fcAccVO = fcAccDao.findFcAcc(fcAcc, getSqlByInstrtType(instrtType));
        List<FcAccDTO> list = BeanUtils.copyListProperties(fcAccVO, FcAccDTO.class);
        return list;
    }
    /**
     * 如果账户是计息的，才需要显示账号
     * 再根据开户种类：
     * 01 02：正常1、透支
     * 05、 06 08 正常1 超期  提前
     * 03 04 09 10  正常1 展期 逾期
     *
     * @return
     */
    private String getSqlByInstrtType(String instrtType) {
        StringBuilder sb = new StringBuilder();
        /**
         * 不同的利率类型对应不同类型的账号 帐户开户种类（01企业活期存款 \02 银行活期存款\03 放款\04 银行借款\05 企业定期\06 银行定期\07 内部帐户\08 通知存款\15定期保证金）
         */
        if ("2".equals(instrtType) || "7".equals(instrtType)) {
            sb.append(" and a.acc_Type in ('03','04','09','11','12','13') ");
        } else if ("3".equalsIgnoreCase(instrtType)) {
            sb.append(" and a.acc_Type in ('05','06','15')");
        } else if ("4".equals(instrtType)) {
            sb.append(" and a.overdraft_Sign <> 'N'");
        } else if ("5".equals(instrtType)) {
            sb.append(" and a.agreed_Deposit_Sign='1'");
        } else if ("6".equals(instrtType)) {
            sb.append(" and a.acc_Type in ('05','06','08','15')");
        }
        return sb.toString();
    }
    /**
     * 功能介绍 : 开户未复核通过的账户。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Override
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
    @Override
    public List<FcAccDTO> getAccSet(List<String> accList) {
        return fcAccDao.getAccSet(accList);
    }
    /**
     * 功能介绍 : 根据系统科目（开户种类）查询账户<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author caozx
     */
    @Override
    public List<FcAccDTO> findBySysSubjectCode(String sysSubjcetCode, String accType, String netId, Set<String> corpCods, String curId) {
        List<FcAccDTO> dtoList = fcAccDao.findBySysSubjectId("", netId, corpCods, curId);
        return dtoList;
    }
    /**
     * 功能介绍 : 查询表外账户集合。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Override
    public List<FcAccDTO> getOutAccTypeAccs() {
        return fcAccDao.getOutAccTypeAccs();
    }
    @Override
    public List<FcAccPlusVO> getTallyAccTypeAccs(List<String> accs) {
        List<FcAccVO> accVOs = fcAccDao.getTallyAccTypeAccs(accs);
        return BeanUtils.copyListProperties(accVOs, FcAccPlusVO.class);
    }
    @Override
    public List<FcAccPlusDTO> getTallyAccTypeAccsDto(List<String> accs) {
        List<FcAccVO> accVOs = fcAccDao.getTallyAccTypeAccs(accs);
        return BeanUtils.copyListProperties(accVOs, FcAccPlusDTO.class);
    }
    /**
     * 通过父银行账号 找子账号对应的内部账号【N,P,S】
     *
     * @param parentBankAccId
     * @param curId
     * @return
     */
    @Override
    public List<FcAccPlusVO> getSubAccFcAccByParentBankAcc(String parentBankAccId, String curId, List<String> ids) {
        List<FcAccPlusVO> voList = fcAccDao.getSubAccFcAccByParentBankAcc(parentBankAccId, curId, ids);
        return voList;
    }
    /**
     * 判断两个账号是否所属同一网点下
     *
     * @param accInfo1 账号
     * @param accInfo2 账号
     * @return
     */
    @Override
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
    @Override
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
            return fcAccDao.getPoolDataTableAllNormal(dataTablesInput, corpAuthSql.toString(), params);
        } else {
            return fcAccDao.getDataTableAllNormal(dataTablesInput, corpAuthSql.toString(), params);
        }
    }
    @Override
    public FcAccVO getFcAcc(String id) {
        return fcAccDao.findFcAccById(id);
    }
    @Override
    public FcAccDTO getFcAccDTO(String id) {
        FcAccVO vo = fcAccDao.findFcAccById(id);
        if (EmptyUtils.isEmpty(vo)) {
            return null;
        }
        FcAccDTO fcAccDTO = new FcAccDTO();
        BeanUtils.copyNotNullProperties(vo, fcAccDTO);
        return fcAccDTO;
    }
    @Override
    public DataTablesOutput<FcAccListVO> getFcAcc(DataTablesInput input) {
        return fcAccDao.getDataTableAllNormal(input, null, null);
    }
    /**
     * 功能介绍  : 查询资金池账户相关信息。<br>
     * <b>日期：</b>2019年5月9日<br>
     *
     * @version 1.0
     * @author wangzx
     */
    @Override
    public FcAccDTO getPoolAccDTOByAccount(String account) {
        FcAccVO fcAcc = fcAccDao.getPoolFcAccVOByBlnctrAcc(account);
        if (EmptyUtils.isEmpty(fcAcc)) {
            return null;
        }
        FcAccDTO fcAccDTO = new FcAccDTO();
        BeanUtils.copyNotNullProperties(fcAcc, fcAccDTO);
        // todo 获取联余额
      /*  List<String> accList = new ArrayList<>();
        accList.add(fcAccDTO.getBlnctrAcc());
        List<FcOnlineLedgerDTO> ledgerList = ledgerService.getBalanceByAcc(accList);
        Map<String, FcOnlineLedgerDTO> ledgerMap = ledgerList.stream().collect(Collectors.toMap(FcOnlineLedgerDTO::getAccount, Function.identity()));
        fcAccDTO.setFcBalance(ledgerMap.get(fcAccDTO.getBlnctrAcc()).getCheckBalance());*/
        return fcAccDTO;
    }
    @Override
    public List<FcAccDTO> getNetAccByPoolSign(String netId, String accType, String interestSign, String poolSign) {
        return fcAccDao.getNetAccByPoolSign(netId, accType, interestSign, poolSign);
    }
    @Override
    public List<FcAccDTO> getNetPoolAccBySubject(String netId, String curId, String subjectId, String poolSign) {
        return fcAccDao.getNetPoolAccBySubject(netId, curId, subjectId, poolSign);
    }
    /**
     * 功能介绍  : 根据资金池单位查询账号(开户待复核、开户复核打回、改户待复核、改户复核打回、销户待复核、销户复核打回)。<br>
     * <b>日期：</b>2019年5月22日<br>
     *
     * @param netId    单位id
     * @param corpId   单位id
     * @param isFilter 是否过滤状态 true过滤，flase 不过滤
     * @version 1.0
     * @author wangzx
     */
    @Override
    public List<FcAccDTO> listFcAccPoolByCorpId(String netId, String corpId, boolean isFilter) {
        return fcAccDao.listFcAccPoolByCorpId(netId, corpId, isFilter);
    }
    /**
     * 根据资金池标志生成内部账号生成法则
     *
     * @param poolSign
     * @return
     */
    @Override
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
     * 根据单位集合 得到内部账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    @Override
    public List<FcAccDTO> getFcAccByCorpIdList(List<String> corpIdList, List<String> accState, String joinSql) {
        return fcAccDao.getFcAccByCorpIdList(corpIdList, accState, joinSql);
    }
    //==========================================================================================================
    /**
     * 根据单位集合 得到内部账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    @Override
    public List<String> getFcAccsByCorpIdList(List<String> corpIdList, List<String> accState, String joinSql) {
        StringBuilder sql = new StringBuilder("select t.blnctr_Acc as blnctrAcc");
        sql.append(" from Fc_Acc t ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(corpIdList)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpIdList, "t.corp_id", true));
        }
        if (!EmptyUtils.isEmpty(accState)) {
            sql.append(" and ").append(SqlUtil.getInSql(accState, "t.acc_State", true));
        }
        sql.append(joinSql);
        return fcAccDao.createSQLQuery(sql.toString()).list();
    }
    @Override
    public DataTablesOutput listFcAccInfoDataTable(DataTablesInput dataTablesInput, String joinSql) {
        String netId = (String) dataTablesInput.getFormdata().get("netId");
        String corpId = (String) dataTablesInput.getFormdata().get("corpId");
        String curId = (String) dataTablesInput.getFormdata().get("curId");
//		String sysSubjectCode = (String)dataTablesInput.getFormdata().get("sysSubjectCode");
        String bankTypeId = (String) dataTablesInput.getFormdata().get("bankTypeId");
        String accType = (String) dataTablesInput.getFormdata().get("accType");
        String bankAcc = (String) dataTablesInput.getFormdata().get("bankAcc");
        String accName = (String) dataTablesInput.getFormdata().get("accName");
        String interestAcc = (String) dataTablesInput.getFormdata().get("interestAcc");
        String interestType = (String) dataTablesInput.getFormdata().get("interestType");
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,net.name as netName,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("corp.name as corpName,cur.cur_name AS curName,");
        sql.append("t.acc_Type as accType,");
        sql.append("t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime,t.pool_sign as poolSign");
        sql.append(" from Fc_Acc t ");
        sql.append(" left join fc_subject f on t.subject_id = f.id ");
        sql.append(" left join bt_input_bank_info bi on t.bank_code = bi.sys_bank_code ");
        sql.append(" left join bt_bank_type bbt on (bi.bank_prefix = bbt.bank_prefix ");
        sql.append(" and (bbt.bank_name_prefix is null or bi.bank_name like bt_concat2(bbt.bank_name_prefix, '%')))");
        sql.append(" left join bt_currency cur on t.cur_id = cur.id ");
        sql.append(" left join sys_corp net on t.net_id=net.id ");
        sql.append(" left join sys_corp corp on t.corp_id=corp.id ");
        sql.append(" where t.acc_state = 'N' ");
        //过滤掉资金池内部账号
        sql.append(" and (t.pool_sign <> '1' or t.pool_sign is null) ");
        if (StringUtils.isNotEmpty(interestType)) {
            if ("pay".equals(interestType)) {
                //新增过滤掉已经绑定过计息账户的账户
                if (StringUtils.isNotEmpty(interestAcc)) {
                    String accSql = SqlUtil.getInSql(interestAcc.split(CommonConstant.SEMI_SPLIT), "fa.blnctr_acc", true);
                    sql.append(" and exists (select 1 from fc_acc fa where t.id = fa.id ");
                    sql.append(" and ( ").append(accSql).append(" or fa.interest_rec_pay is null))");
                } else {
                    sql.append(" and exists (select 1 from fc_acc fa where t.id = fa.id and fa.interest_rec_pay is null) ");
                }
            } else if ("exp".equals(interestType)) {
                //新增过滤掉已经绑定过计息账户的账户
                if (StringUtils.isNotEmpty(interestAcc)) {
                    String accSql = SqlUtil.getInSql(interestAcc.split(CommonConstant.SEMI_SPLIT), "fa.blnctr_acc", true);
                    sql.append(" and exists (select 1 from fc_acc fa where t.id = fa.id ");
                    sql.append(" and ( ").append(accSql).append(" or fa.interest_in_exp is null))");
                } else {
                    sql.append(" and exists (select 1 from fc_acc fa where t.id = fa.id and fa.interest_in_exp is null) ");
                }
            }
        } else {
            sql.append(" and exists (select 1 from fc_acc fa where t.id = fa.id and fa.interest_in_exp is null and fa.interest_rec_pay is null) ");
        }
        if (StringUtils.isNotBlank(joinSql)) {
            sql.append(joinSql);
        }
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id =:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_id =:corpId");
            params.put("corpId", corpId);
        }
		/*if (StringUtils.isNotEmpty(sysSubjectCode)) {
			sql.append(" and f.sys_subject_code =:sysSubjectCode");
			params.put("sysSubjectCode", sysSubjectCode);
		}*/
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id =:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(bankTypeId)) {
            sql.append(" and bbt.id = :bankTypeId");
            params.put("bankTypeId", bankTypeId);
        }
        if (StringUtils.isNotEmpty(accType)) {
            sql.append(" and t.acc_Type = :accType");
            params.put("accType", accType);
        }
        if (StringUtils.isNotEmpty(bankAcc)) {
            sql.append(" and t.blnctr_acc like :bankAcc");
            params.put("bankAcc", "%" + bankAcc + "%");
        }
        if (StringUtils.isNotEmpty(accName)) {
            sql.append(" and t.acc_name like :accName");
            params.put("accName", "%" + accName + "%");
        }
        return fcAccDao.getDynamicBySQL(dataTablesInput, sql.toString(), FcAccListVO.class, "t", false, params);
    }
    @Override
    public List<FcAccVO> getAccListByInfo(String netId, String corpId, String sysSubjectCode, String curId, String bankTypeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        sql.append(" left join bt_input_bank_info bi on t.bank_code = bi.sys_bank_code ");
        sql.append(" left join bt_bank_type bbt on (bi.bank_prefix = bbt.bank_prefix ");
        sql.append(" and (bbt.bank_name_prefix is null or bi.bank_name like bt_concat2(bbt.bank_name_prefix, '%')))");
        sql.append(" where t.acc_state = 'N' ");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id =:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_id =:corpId");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(sysSubjectCode)) {
            sql.append(" and f.sys_subject_code in(:sysSubjectCode)");
            params.put("sysSubjectCode", sysSubjectCode.split(","));
        }
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id =:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(bankTypeId)) {
            sql.append(" and bbt.id = :bankTypeId");
            params.put("bankTypeId", bankTypeId);
        }
        return fcAccDao.listBySQLAliasToBean(sql.toString(), params, FcAccVO.class);
    }
    @Override
    public int getFcAccCountByCorpId(String corpId, String busType) {
        String sql = "select count(*) from fc_acc where corp_id=:corpId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        if ("disable".equals(busType)) {
            //N-正常状态
            sql += " and acc_State='" + InnerAccountConstant.ACC_STATE_NORMAL + "'";
        }
        return ((Number) fcAccDao.createSQLQuery(sql, params).uniqueResult()).intValue();
    }
    @Override
    public List<FcAccVO> listFcAccByCorpId(String corpId, String curId) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        sql.append(" where 1=1 ");
        Map map = new HashMap();
        sql.append(" and t.acc_state in('N','P')  ");
        //企业活期账号
        sql.append(" and f.acc_type = '01' and f.sys_subject_code = '2120'  ");
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_id =:corpId");
            map.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id =:curId");
            map.put("curId", curId);
        }
        return fcAccDao.listBySQLAliasToBean(sql.toString(), map, FcAccVO.class);
    }
    @Override
    public FcAccVO getPoolFcAccVOByBlnctrAcc(String blnctrAcc) {
        String sql = getCommonFcAccUnionForPool() +
                " where t.blnctr_Acc=:blnctrAcc ";
        Map map = new HashMap();
        map.put("blnctrAcc", blnctrAcc);
        return fcAccDao.getBySQLAliasToBean(sql, map, FcAccVO.class);
    }
    @Override
    public List<FcAccDTO> findBySysSubjectId(String sysSubjectId, String netIds, Set<String> corpIds,
                                             String curId) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        sql.append(" where 1=1 and t.acc_state = '" + InnerAccountConstant.ACC_STATE_NORMAL + "' ");
        if (StringUtils.isNotEmpty(sysSubjectId)) {
            sql.append("and  f.sys_subject_code = '");
            sql.append(sysSubjectId);
            sql.append("' ");
        }
        if (StringUtils.isNotEmpty(netIds)) {
            sql.append("and  t.net_Id = '");
            sql.append(netIds);
            sql.append("' ");
        }
        if (StringUtils.isNotEmpty(curId)) {
            sql.append("and  t.cur_Id = '");
            sql.append(curId);
            sql.append("' ");
        }
        List<String> corpIdss = new ArrayList<String>(corpIds);
        String where = getSQLIn(corpIdss, 900, "t.corp_Id");
        return fcAccDao.listBySQLAliasToBean(sql.append("and " + where).toString(), null, FcAccDTO.class);
    }
    /**
     * <b>功能介绍：</b>根据科目id,系统科目code查询账户(过滤掉关联了银行代码)<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Override
    public List<FcAccDTO> findBySysSubjectIdFilterBankCode(String subjectId, String sysSubjectCode, String netIds, Set<String> corpIds,
                                                           String curId) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        sql.append(" where 1=1 and t.acc_state = '" + InnerAccountConstant.ACC_STATE_NORMAL + "' ");
        //sql.append(" and t.bank_code is null ");
        if (StringUtils.isNotEmpty(subjectId)) {
            sql.append(" and f.id = '");
            sql.append(subjectId);
            sql.append("' ");
        }
        if (StringUtils.isNotEmpty(sysSubjectCode)) {
            sql.append(" and f.sys_subject_code = '");
            sql.append(sysSubjectCode);
            sql.append("' ");
        }
        if (StringUtils.isNotEmpty(netIds)) {
            sql.append(" and t.net_Id = '");
            sql.append(netIds);
            sql.append("' ");
        }
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_Id = '");
            sql.append(curId);
            sql.append("' ");
        }
        List<String> corpIdss = new ArrayList<String>(corpIds);
        String where = getSQLIn(corpIdss, 900, "t.corp_Id");
        return fcAccDao.listBySQLAliasToBean(sql.append("and " + where).toString(), null, FcAccDTO.class);
    }
    @Override
    public List<FcAccDTO> listFcAccInfo(List<String> ids, List<String> accState) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCommonFcAccUnion());
        sql.append(" where 1=1 ");
        if (ids != null && !ids.isEmpty()) {
            sql.append(" and ");
            sql.append(getSQLIn(ids, 900, "t.id"));
        }
        if (accState != null && !accState.isEmpty()) {
            sql.append(" and ");
            sql.append(getSQLIn(accState, 900, "t.acc_State"));
        }
        return fcAccDao.listBySQLAliasToBean(sql.toString(), null, FcAccDTO.class);
    }
    @Override
    public FcAccPlusVO getFcAccVOByParams(String netId, String corpId, String subjectId, String curId, String sqlUnion) {
        List<FcAccPlusVO> list = this.getAccountByParams(curId, netId, corpId, subjectId, sqlUnion, null, null);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    @Override
    public FcAccVO getFcAccVOByBlnctrAcc(String blnctrAcc) {
        String sql = getCommonFcAccUnion() +
                " where t.blnctr_Acc=:blnctrAcc ";
        Map map = new HashMap();
        map.put("blnctrAcc", blnctrAcc);
        return fcAccDao.getBySQLAliasToBean(sql, map, FcAccVO.class);
    }
    @Override
    public DataTablesOutput getDataTableAll(DataTablesInput dataTablesInput, String corpAuthSql, Map<String, Object> params) {
        String sql = "select t.id as id,t.blnctr_Acc as blnctrAcc,t.acc_Name as accName,t.net_Id as netId,net.name AS netName," +
                "t.corp_Id as corpId,corp.name AS corpName,t.cur_Id as curId,cur.cur_name AS curName,t.acc_Type as accType," +
                "t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime,t.pool_sign as poolSign" +
                " from Fc_Acc t  LEFT JOIN sys_corp net ON t.net_id=net.id " +
                " LEFT JOIN sys_corp corp ON t.corp_id=corp.id" +
                " LEFT JOIN bt_currency cur ON t.cur_id=cur.id " +
                //过滤掉资金池内部账号
                " where 1=1 and (t.pool_sign <> '1' or t.pool_sign is null) " +

                " and " + corpAuthSql;
        return fcAccDao.getDynamicBySQL(dataTablesInput, sql, FcAccListVO.class, "t", false, params);
    }
    /**
     * 资金池内部账号查询
     *
     * @param dataTablesInput
     * @return
     */
    @Override
    public DataTablesOutput getDataTableAllForPool(DataTablesInput dataTablesInput, String corpAuthSql, Map<String, Object> params) {
        String sql = "select t.id as id,t.blnctr_Acc as blnctrAcc,t.acc_Name as accName,t.net_Id as netPoolId,net.POOL_NAME AS netPoolName," +
                "t.corp_Id as corpId,corp.name AS corpName,t.cur_Id as curId,cur.cur_name AS curName,t.acc_Type as accType," +
                "t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime,t.pool_sign as poolSign" +
                " from Fc_Acc t  LEFT JOIN fc_corp_pool net ON t.net_id=net.id " +
                " LEFT JOIN sys_corp corp ON t.corp_id=corp.id" +
                " LEFT JOIN bt_currency cur ON t.cur_id=cur.id " +
                //查询资金池内部账号
                " where 1=1 and t.pool_sign='1'" +
                " and " + corpAuthSql;
        return fcAccDao.getDynamicBySQL(dataTablesInput, sql, FcAccListVO.class, "t", false, params);
    }
    /**
     * 内部账号查询状态正常
     *
     * @param dataTablesInput
     * @retu rn
     */
    @Override
    public DataTablesOutput getDataTableAllNormal(DataTablesInput dataTablesInput, String corpAuthSql, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.acc_Type as accType,");
        sql.append("t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime");
        sql.append(" from Fc_Acc t  where 1=1 ");
        //过滤掉资金池内部账号
        sql.append(" and (t.pool_sign <> '1' or t.pool_sign is null) ");
        if (StringUtils.isNotEmpty(corpAuthSql)) {
            sql.append(" and ").append(corpAuthSql);
        }
        //已注销的内部账号不能授权可做账户授权-云南交投
        sql.append(" and ( t.acc_State = 'N' or t.acc_State = 'P' or t.acc_State = 'S' or t.acc_State = 'D' ) ");
        return fcAccDao.getDynamicBySQL(dataTablesInput, sql.toString(), FcAccListVO.class, "t", false, params);
    }
    @Override
    public List<FcAccPlusVO> getAccountByParams(String curId, String netId, String corpId, String subjectId, String joinSql,
                                                String sysSubjectCode, String bankCode) {
        return getAccountByParams(curId, netId, corpId, subjectId, null, joinSql, sysSubjectCode, bankCode, false);
    }
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param subjectId
     * @param joinSql        权限sql
     * @param sysSubjectCode
     * @return
     */
    @Override
    public List<FcAccPlusVO> getAccountByParamsForPool(String curId, String netId, String corpId, String subjectId, String joinSql,
                                                       String sysSubjectCode, String bankCode) {
        return getAccountByParamsForPool(curId, netId, corpId, subjectId, null, joinSql, sysSubjectCode, bankCode);
    }
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param sysSubjectCode
     * @param joinSql        权限sql
     * @return
     */
    @Override
    public List<FcAccPlusVO> getAccountByParams(String curId, String netId, String corpId, String subjectId, String endUnit,
                                                String joinSql, String sysSubjectCode, String bankCode, Boolean isHavDel) {
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sql.append("t.balance_Way as balanceWay,corp.name as corpName,t.acc_open_time as accOpenTime,t.acc_start_date as accStartDate, ");
        sql.append("t.interest_start_date as interestStartDate,t.deadline_date as deadlineDate,net.name as netName,t.acc_State as accState,");
//        sql.append("f.subject_name as subjectName,cur.cur_name as curName,fl.check_balance - fl.freeze_money as fcBalance  ");
        sql.append("f.subject_name as subjectName,cur.cur_name as curName ");
        sql.append(" from Fc_Acc t left join fc_Subject f on t.subject_id=f.id ");
        sql.append(" left join sys_corp net on t.net_id=net.id ");
        sql.append(" left join sys_corp corp on t.corp_id=corp.id ");
//        sql.append(" left join fc_online_ledger fl on t.blnctr_acc = fl.account ");
        sql.append(" left join bt_currency cur on t.cur_id=cur.id where 1=1 ");
        if (isHavDel) {
            sql.append(" and t.acc_state in('N','P','D')");
        } else {
            sql.append(" and t.acc_state in('N','P')");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id=:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id=:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(endUnit)) {
            sql.append(" and t.end_unit=:endUnit");
            params.put("endUnit", endUnit);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_Id=:corpId");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(subjectId)) {
            sql.append(" and t.Subject_id=:subjectId");
            params.put("subjectId", subjectId);
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if (StringUtils.isNotEmpty(sysSubjectCode)) {
            sql.append(" and f.sys_Subject_Code = :sysSubjectCode");
            params.put("sysSubjectCode", sysSubjectCode);
        }
        if (StringUtils.isNotEmpty(bankCode)) {
            sql.append(" and t.bank_code = :bankCode");
            params.put("bankCode", bankCode);
        }
        sql.append(" order by t.blnctr_Acc asc ,t.ACC_OPEN_TIME desc,t.CREATE_DATE ");
        return fcAccDao.listBySQLAliasToBean(sql.toString(), params, FcAccPlusVO.class);
    }
    /**
     * 根据参数筛选 内部账号（资金池）
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param sysSubjectCode
     * @param joinSql        权限sql
     * @return
     */
    @Override
    public List<FcAccPlusVO> getAccountByParamsForPool(String curId, String netId, String corpId, String subjectId, String endUnit,
                                                       String joinSql, String sysSubjectCode, String bankCode) {
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sql.append("t.balance_Way as balanceWay,corp.name as corpName,t.acc_open_time as accOpenTime,t.acc_start_date as accStartDate, ");
        sql.append("t.interest_start_date as interestStartDate,t.deadline_date as deadlineDate,net.name as netName,t.acc_State as accState,");
//        sql.append("f.subject_name as subjectName,cur.cur_name as curName,fl.check_balance - fl.freeze_money as fcBalance  ");
        sql.append("f.subject_name as subjectName,cur.cur_name as curName  ");
        sql.append(" from Fc_Acc t left join fc_Subject_pool f on t.subject_id=f.id ");
        sql.append(" left join sys_corp net on t.net_id=net.id ");
        sql.append(" left join sys_corp corp on t.corp_id=corp.id ");
//        sql.append(" left join fc_online_ledger fl on t.blnctr_acc = fl.account ");
        sql.append(" left join bt_currency cur on t.cur_id=cur.id where 1=1 ");
        sql.append(" and t.acc_state in('N','P')");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id=:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id=:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(endUnit)) {
            sql.append(" and t.end_unit=:endUnit");
            params.put("endUnit", endUnit);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_Id=:corpId");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(subjectId)) {
            sql.append(" and t.Subject_id=:subjectId");
            params.put("subjectId", subjectId);
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if (StringUtils.isNotEmpty(sysSubjectCode)) {
            sql.append(" and f.subject_code = :sysSubjectCode");
            params.put("sysSubjectCode", sysSubjectCode);
        }
        if (StringUtils.isNotEmpty(bankCode)) {
            sql.append(" and t.bank_code = :bankCode");
            params.put("bankCode", bankCode);
        }
        sql.append(" order by t.blnctr_Acc asc ,t.ACC_OPEN_TIME desc,t.CREATE_DATE ");
        return fcAccDao.listBySQLAliasToBean(sql.toString(), params, FcAccPlusVO.class);
    }
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param sysSubjectCode
     * @param accList
     * @param bankTypeId
     * @return
     */
    @Override
    public List<FcAccPlusVO> getAccountByParamsAndGrant(String curId, String netId, String corpId, String subjectId, String sysSubjectCode, List<String> accList, String bankTypeId) {
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sql.append("t.balance_Way as balanceWay,corp.name as corpName,t.acc_open_time as accOpenTime, ");
        sql.append("net.name as netName,t.acc_State as accState,");
//        sql.append("f.subject_name as subjectName,cur.cur_name as curName,fl.check_balance - fl.freeze_money as fcBalance  ");
        sql.append("f.subject_name as subjectName,cur.cur_name as curName  ");
        sql.append(" from Fc_Acc t left join fc_Subject f on t.subject_id=f.id ");
        sql.append(" left join sys_corp net on t.net_id=net.id ");
        sql.append(" left join sys_corp corp on t.corp_id=corp.id ");
//        sql.append(" left join fc_online_ledger fl on t.blnctr_acc = fl.account ");
        sql.append(" left join bt_currency cur on t.cur_id=cur.id ");
        sql.append(" left join bt_input_bank_info bi on t.bank_code = bi.sys_bank_code ");
        sql.append(" left join bt_bank_type bbt on (bi.bank_prefix = bbt.bank_prefix and (bbt.bank_name_prefix is null or bi.bank_name like bt_concat2(bbt.bank_name_prefix,'%'))) ");
        sql.append(" where 1=1 and t.acc_state in('N','P')");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id=:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id=:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_Id=:corpId");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(subjectId)) {
            sql.append(" and t.Subject_id=:subjectId");
            params.put("subjectId", subjectId);
        }
        if (StringUtils.isNotEmpty(sysSubjectCode)) {
            sql.append(" and f.sys_Subject_Code = :sysSubjectCode");
            params.put("sysSubjectCode", sysSubjectCode);
        }
        if (StringUtils.isNotEmpty(bankTypeId)) {
            sql.append(" and bbt.id = :bankTypeId");
            params.put("bankTypeId", bankTypeId);
        }
        if (!EmptyUtils.isEmptyList(accList)) {
            sql.append(" and t.id in (:accList)");
            params.put("accList", accList);
        }
        sql.append(" order by t.blnctr_Acc asc ,t.ACC_OPEN_TIME desc,t.CREATE_DATE ");
        return fcAccDao.listBySQLAliasToBean(sql.toString(), params, FcAccPlusVO.class);
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
    @Override
    public List<FcAccPlusVO> getAccountByParamsAndGrantForPool(String curId, String netId, String corpId, String subjectId, String sysSubjectCode, List<String> accList, String bankCode) {
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sql.append("t.balance_Way as balanceWay,corp.name as corpName,t.acc_open_time as accOpenTime, ");
        sql.append("net.pool_Name as netName,t.acc_State as accState,");
//        sql.append("f.subject_name as subjectName,cur.cur_name as curName,fl.check_balance - fl.freeze_money as fcBalance  ");
        sql.append("f.subject_name as subjectName,cur.cur_name as curName  ");
        sql.append(" from Fc_Acc t left join fc_Subject_pool f on t.subject_id=f.id ");
        sql.append(" left join fc_corp_pool net on t.net_id=net.id ");
        sql.append(" left join sys_corp corp on t.corp_id=corp.id ");
//        sql.append(" left join fc_online_ledger fl on t.blnctr_acc = fl.account ");
        sql.append(" left join bt_currency cur on t.cur_id=cur.id where 1=1 ");
        sql.append(" and t.acc_state in('N','P')");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id=:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id=:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_Id=:corpId");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(subjectId)) {
            sql.append(" and t.Subject_id=:subjectId");
            params.put("subjectId", subjectId);
        }
        if (StringUtils.isNotEmpty(sysSubjectCode)) {
            sql.append(" and f.sys_Subject_Code = :sysSubjectCode");
            params.put("sysSubjectCode", sysSubjectCode);
        }
        if (StringUtils.isNotEmpty(bankCode)) {
            sql.append(" and t.bank_code = :bankCode");
            params.put("bankCode", bankCode);
        }
        if (!EmptyUtils.isEmptyList(accList)) {
            sql.append(" and t.id in (:accList)");
            params.put("accList", accList);
        }
        sql.append(" order by t.blnctr_Acc asc ,t.ACC_OPEN_TIME desc,t.CREATE_DATE ");
        return fcAccDao.listBySQLAliasToBean(sql.toString(), params, FcAccPlusVO.class);
    }
    /**
     * 根据( 账号种类/状态为正常/部分冻结/只收不支)参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @return
     */
    @Override
    public List<FcAccListVO> getAccountByParams(String[] accTypes, String curId, String netId, String corpId) {
        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.subject_Id as subjectId,t.acc_Type as accType,");
        sql.append("t.balance_Way as balanceWay,corp.name as corpName,");
        sql.append("net.name as netName,t.acc_State as accState,");
        sql.append("f.subject_name as subjectName,cur.cur_name as curName ");
        sql.append(" from Fc_Acc t left join fc_Subject f on t.subject_id=f.id ");
        sql.append(" left join sys_corp net on t.net_id=net.id ");
        sql.append(" left join sys_corp corp on t.corp_id=corp.id ");
        sql.append(" left join bt_currency cur on t.cur_id=cur.id where 1=1 ");
        sql.append(" and t.acc_state in('N','P','S')");
        Map<String, Object> params = new HashMap<String, Object>();
        //去掉按账户类型过滤，除表外账户，其他所有账户类型都可以查询
		if (accTypes != null && accTypes.length > 0) {
            sql.append("  and t.ACC_TYPE in(:accTypes) ");
			params.put("accTypes", accTypes);
		}else{
            sql.append("  and t.ACC_TYPE <> :accType ");
            params.put("accType", InnerAccountConstant.FCACC_OFFBALANCE);
        }
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and t.cur_id=:curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotEmpty(netId)) {
            sql.append(" and t.net_id=:netId");
            params.put("netId", netId);
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_Id=:corpId");
            params.put("corpId", corpId);
        }
        sql.append(" order by t.ACC_OPEN_TIME desc");
        return fcAccDao.listBySQLAliasToBean(sql.toString(), params, FcAccListVO.class);
    }
    @Override
    public boolean isExistInterestPayable(String blnctrAcc) {
        String sql = "select count(1) as CN from Fc_Acc t, fc_Online_Ledger x,fc_subject f where t.acc_State = 'N' and t.acc_Type = :accType" +
                " and f.sys_Subject_Code = :sysSubjectCode and x.check_Balance <> 0 and t.acc_For_Deduct = :blnctrAcc";
        Map<String, Object> params = new HashMap<>();
        params.put("blnctrAcc", blnctrAcc);
        params.put("sysSubjectCode", SubjectConstant.SysSubject.INTEREST_PAYABLE_OWE_INNERACC.get());
        params.put("accType", InnerAccountConstant.FCACC_INNER_ACCOUNT);
        Map rs = getBySQL(sql, params);
        Integer count = new Integer(rs.get("CN").toString());
        return count == null || count <= 0;
    }
    /**
     * 根据SQL查询出object对象
     *
     * @param sql    SQL
     * @param params Map条件参数
     * @return Map结果集
     */
    @Override
    public Map getBySQL(String sql, final Map<String, ?> params) {
        return getBySQL(sql, params, Boolean.FALSE);
    }
    @Override
    public Map getBySQL(String sql, final Map<String, ?> params, Boolean cache) {
        Query sqlQuery = createSQLQuery(sql, params);
        sqlQuery.setCacheable(cache);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (Map) sqlQuery.uniqueResult();
    }
    /**
     * 根据查询SQL与参数列表创建Query对象.
     *
     * @param queryString SQL
     * @param values      命名参数,按名称绑定.
     * @return SQLQuery
     */
    @Override
    public SQLQuery createSQLQuery(String queryString, final Map<String, ?> values) {
        SQLQuery sqlQuery = createSQLQuery(queryString);
        if (values != null) {
            sqlQuery.setProperties(values);
        }
        return sqlQuery;
    }
    /**
     * @param queryString 执行的SQL语句
     * @return SQLQueryImpl适配器
     */
    @Override
    protected SQLQuery createSQLQuery(String queryString) {
        SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
        return new SQLQueryImplAdapter(sqlQuery);
    }
    @Override
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, String sqlWhere, Map<String, Object> params) {
        String sql = "select t.id as id," + "t.blnctr_Acc as blnctrAcc," +
                "t.acc_Name as accName,t.net_Id as netPoolId," +
                "t.corp_Id as corpId,t.cur_Id as curId," +
                "t.subject_Id as subjectPoolId,t.acc_Type as accType," +
                "t.balance_Way as balanceWay,t.acc_State as accState," +
                "f.subject_name as subjectName,t.pool_sign as poolSign" +
                " from Fc_Acc t left join fc_Subject_pool f on t.subject_id=f.id " +
                sqlWhere +
                " and t.pool_sign='1'";
        return fcAccDao.getDynamicBySQL(dataTablesInput, sql, FcAccVO.class, "", false, params);
    }
    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String sqlWhere, Map<String, Object> params) {
        String sql = "SELECT t.id AS id,t.blnctr_Acc AS blnctrAcc," + "t.acc_Name AS accName,t.net_Id AS netId," +
                "t.corp_Id AS corpId,t.cur_Id AS curId," +
                "t.subject_Id AS subjectId,t.acc_Type AS accType," +
                "t.balance_Way AS balanceWay,corp.name AS corpName,t.acc_open_time AS accOpenTime,t.acc_start_date AS accStartDate," +
                "t.interest_start_date AS interestStartDate,t.deadline_date AS deadlineDate,net.name AS netName,t.acc_State AS accState," +
                "t.pool_sign as poolSign,f.subject_name AS subjectName,cur.cur_name AS curName" +
                " from Fc_Acc t " +
                " LEFT JOIN fc_Subject f ON t.subject_id=f.id" +
                " LEFT JOIN sys_corp net ON t.net_id=net.id " +
                " LEFT JOIN sys_corp corp ON t.corp_id=corp.id " +
                " LEFT JOIN bt_currency cur ON t.cur_id=cur.id " +
                sqlWhere +
                " and (t.pool_sign <> '1' or t.pool_sign is null) ";
        return fcAccDao.getDynamicBySQL(dataTablesInput, sql, FcAccListVO.class, "t", false, params);
    }
    @Override
    public FcAccVO findFcAccById(String id) {
        return fcAccService.findFcAccById(id);
    }
    @Override
    public List<FcAccVO> listByBlnctrAcc(String blnctrAcc) {
        List<FcAccVO> list = listBy("blnctrAcc", blnctrAcc);
        return list;
    }
    @Override
    public List<FcAccVO> queryMultiAccAll(String netId, String curId, String accType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accType", accType);
        params.put("netId", netId);
        params.put("curId", curId);
        List<FcAcc> list = fcAccDao.listByHQLAliasToBean(" select t.blnctrAcc as blnctrAcc from FcAcc t" + " where t.accType=:accType and t.netId=:netId and t.curId=:curId", params, FcAcc.class);
        List<FcAccVO> listvo = BeanUtils.copyListProperties(list, FcAccVO.class);
        return listvo;
    }
    @Override
    public void delInterestAcc(FcAccVO entity) {
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
    @Override
    public void deleteEntity(FcAccVO entity, ICurrentUser user) {
        FcAcc acc = new FcAcc();
        BeanUtils.copyProperties(entity, acc);
        fcAccDao.delete(acc);
    }
    @Override
    public void delete(String id) {
        fcAccDao.delete(id);
    }
    /**
     * 资金池查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @Override
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
        List<FcAccVO> list = output.getData();
        if(list.size()>0) {
            Map<String, CorpDTO>  corpMap = corpServiceI.getCorpNetMap();
            Map<String, BtCurrencyDTO>  curMap = btCurrencyServiceI.getCurrency();
            for(FcAccVO vo:list){
                vo.setAccTypeName(dictServiceI.get("InnerAccount.AccType", vo.getAccType()));
                vo.setAccStateName(dictServiceI.get("inneraccount.fcStatus", vo.getAccState()));
                vo.setBalanceWayName(dictServiceI.get("Admin.BalanceWay", vo.getBalanceWay()));

                CorpDTO corpDTO= corpMap.get(vo.getCorpId());
                vo.setCorpName(corpDTO.getName());
                vo.setNetPoolName(corpDTO.getNetName());
                vo.setNetName(corpDTO.getNetName());
                vo.setCurName(curMap.get(vo.getCurId()).getCurName());
            }
        }
        return output;
    }
    @Override
    public List<FcAccPlusVO> listAccountByMoreParamsForPool(String curId, String netId, String corpId, String subjectId,
                                                            String sysSubjectCode) {
        return fcAccDao.getAccountByParamsForPool(curId, netId, corpId, subjectId, null, sysSubjectCode, null);
    }
    @Override
    public void dateCompareForPool(Date date1, Date date2) {
        if (DateUtils.dayDiffs(date1, date2) <= 0) {
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_OPENTIME_TALLYTIME_POOL_COMPARE,
                    DateUtils.formatDate(date1), DateUtils.formatDate(date2));
        }
    }
    @Override
    public void save(FcAccVO entity) {
        FcAcc acc = new FcAcc();
        BeanUtils.copyProperties(entity, acc);
        fcAccService.save(acc);
    }
    @Override
    public void update(FcAccVO entity) {
        FcAcc acc = new FcAcc();
        BeanUtils.copyProperties(entity, acc);
        fcAccService.update(acc);
    }
    @Override
    public String saveOrUpdate(FcAccVO entity) {
        FcAcc acc = new FcAcc();
        BeanUtils.copyProperties(entity, acc);
        fcAccService.saveOrUpdate(acc);
        return acc.getId();
    }
    @Override
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
    @Override
    public void updateRate(String blnctrAcc, String colName, BigDecimal instrate) {
        fcAccDao.updateRate(blnctrAcc, colName, instrate);
    }
    @Override
    public FcAccVO findFcAccByBlnctrAcc(String blnctrAcc) {
        FcAcc fcAcc = fcAccDao.findFcAccByBlnctrAcc(blnctrAcc);
        FcAccVO vo = new FcAccVO();
        BeanUtils.copyProperties(fcAcc, vo);
        return vo;
    }
    @Override
    public List<FcAccVO> getFcAcc(FcAccVO fcAcc, String sql, Map<String, Object> params) {
        return fcAccDao.findFcAcc(fcAcc, sql, params);
    }
    @Override
    public List<FcAccVO> findFcAcc(FcAccVO fcAcc, String sql) {
        FcAcc entity = new FcAcc();
        BeanUtils.copyProperties(fcAcc, entity);
        return fcAccDao.findFcAcc(entity, sql);
    }
    @Override
    public int check(FcAccVO fcAcc, String accState, String oldAccState) {
        FcAcc entity = new FcAcc();
        BeanUtils.copyProperties(fcAcc, entity);
        return fcAccDao.check(entity, accState, oldAccState);
    }
    @Override
    public boolean isOtherAccountQuote(String blnctrAcc) {
        return fcAccDao.isOtherAccountQuote(blnctrAcc);
    }
    @Override
    public boolean isNotReceivedInterest(String blnctrAcc) {
        return fcAccDao.isExistInterestPayable(blnctrAcc);
    }
    @Override
    public void changeState(String blnctrAcc, String state) {
        FcAcc fcAcc = fcAccDao.findFcAccByBlnctrAcc(blnctrAcc);
        fcAcc.setAccState(state);
        fcAccDao.update(fcAcc);
    }
    @Override
    public DataTablesOutput getDataTableAll(DataTablesInput dataTablesInput, ICurrentUser user) {
        Map<String, Object> params = new HashMap();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_ALLQUERY, user.getId()), "t.corp_Id", params);
        DataTablesOutput output = fcAccDao.getDataTableAll(dataTablesInput, corpAuthSql, params);
        return output;
    }
    @Override
    public DataTablesOutput getDataTableAllForPool(DataTablesInput dataTablesInput, ICurrentUser user) {
        Map<String, Object> params = new HashMap();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_ALLQUERY, user.getId()), "t.corp_Id", params);
        DataTablesOutput output = fcAccDao.getDataTableAllForPool(dataTablesInput, corpAuthSql, params);
        return output;
    }
    @Override
    public String getBelongingCorpIdsInSql(DataTablesInput dataTablesInput) {
        String belongingCorpIdsInSql = null;
        String belongingCorpIds = (String) dataTablesInput.getFormdata().get("belongingCorpIds");
        if (!EmptyUtils.isBlank(belongingCorpIds)) {
            String[] corpIds = belongingCorpIds.split(",");
            belongingCorpIdsInSql = SqlUtil.getInSql(corpIds, "t.corp_id", true);
            dataTablesInput.getFormdata().remove("belongingCorpIds");
        }
        return belongingCorpIdsInSql;
    }
    @Override
    public List<FcAccVO> getFcAccByAccForDeduct(String accForDeduct) {
        List<FcAccVO> list = listBy("accForDeduct", accForDeduct);
        return list;
    }
    @Override
    public boolean isExsits(String subujectId) {
        return fcAccDao.isExsits(subujectId);
    }
    @Override
    public boolean isExsitsForValidBankAccByInnerAcc(String blnctrAcc) {
        return fcAccDao.isExsitsForValidBankAccByInnerAcc(blnctrAcc);
    }
    @Override
    public List<FcAccDTO> getBlnctrAcc(String userId, String poolId, String corpId, String accountType,
                                       String permission) {
        return null;
    }
    @Override
    public DataTablesOutput getDataTableAllNormal(DataTablesInput dataTablesInput, ICurrentUser user) {
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
            return fcAccDao.getPoolDataTableAllNormal(dataTablesInput, corpAuthSql.toString(), params);
        } else {
            return fcAccDao.getDataTableAllNormal(dataTablesInput, corpAuthSql.toString(), params);
        }
    }
    @Override
    public List<FcAccDTO> getBlnctrAcc(String userId, String poolId, String corpId, String busType,
                                       List<String> listAccId,String accAuthSql) {
        return fcAccDao.getBlnctrAcc(userId, poolId, corpId, busType, listAccId, accAuthSql);
    }

    @Override
    public List<FcAccDTO> getBlnctrAccByCorpId(String userName, String corpId, String busType, List<String> listAccId) {
        return fcAccDao.getBlnctrAccByCorpId(userName, corpId, busType, listAccId);
    }

    @Override
    public Map<String, String> getAccNameByNetIdAndBlnctrAccs(String netId, List<String> blnctrAccs) {
        return fcAccService.getAccNameByNetIdAndBlnctrAccs(netId, blnctrAccs);
    }

    @Override
    public List<FcAccDTO> getBlnctrAccByCorpId(String userId, String corpId, String accountType, String permission) {
        return fcAccDao.getBlnctrAccByCorpId(userId, corpId, accountType, permission);
    }
    @Override
    public List<FcAccVO> transferOutAcc(String blnctrAcc, String netId) {
        return fcAccDao.transferOutAcc(blnctrAcc, netId);
    }
    /**
     * 根据账号信息查找计息账号列表
     *
     * @param vo
     * @return
     */
    @Override
    public List<FcAccVO> getAccListByExpPay(FcAccVO vo) {
        return this.fcAccDao.getAccListByExpPay(vo);
    }
    @Override
    public List<FcAccDTO> listFcAccInfoForPool(List<String> ids, List<String> accState) {
        return fcAccDao.listFcAccInfoForPool(ids, accState);
    }
    @Override
    public List<FcAccDTO> listFcAccInFoByCorpIds(List<String> corpIds, String accState) {
        return fcAccDao.listFcAccInFoByCorpIds(corpIds, accState);
    }
    @Override
    public List<FcAccDTO> listFcAccPoolByCorpIds(List<String> corpIds, String accState, String poolSign) {
        return fcAccDao.listFcAccPoolByCorpIds(corpIds, accState, poolSign);
    }
    @Override
    public BankTypeDTO getInfoByBankCode(String bankCode) {
        return fcAccDao.getInfoByBankCode(bankCode);
    }
    @Override
    public boolean isExsitsByExpPay(String blnctrAcc) {
        FcAccVO vo = new FcAccVO();
        vo.setBlnctrAcc(blnctrAcc);
        List<FcAccVO> accListByExpPay = fcAccDao.getAccListByExpPay(vo);
        FcAcc acc = fcAccDao.findFcAccByBlnctrAcc(blnctrAcc);
        boolean flag = !EmptyUtils.isEmptyList(accListByExpPay) ||
                (!EmptyUtils.isEmpty(blnctrAcc) && (!EmptyUtils.isEmpty(acc.getInterestInExp()) || !EmptyUtils.isEmpty(acc.getInterestRecPay())));
        return flag;
    }
    @Override
    public Date getValidDate(String netId) {
//		Date tallyDate = fcDayCloseService.getDayCloseFinalDate(netId);
//        if (tallyDate != null) {
//            Date accStartDate = DateUtils.addDay(tallyDate, 1);
//            return accStartDate;
//        }
        return DateUtils.getDateYMD();
    }
    @Override
    public FcAccPlusVO getFcAccVONotAuthByParams(String netId, String corpId, String subjectId, String curId) {
        return fcAccDao.getFcAccVOByParams(netId, corpId, subjectId, curId, null);
    }
    @Override
    public List<FcAccPlusVO> listAccountByParams(String curId, String netId, String corpId, String subjectId) {
        return fcAccDao.getAccountByParams(curId, netId, corpId, subjectId, null, null, null);
    }
    @Override
    public List<FcAccPlusVO> listAccountByParams(String curId, String netId, String corpId, String subjectId,
                                                 String endUnit) {
        return fcAccDao.getAccountByParams(curId, netId, corpId, subjectId, endUnit, null, null, null, false);
    }
    @Override
    public List<FcAccListVO> listAccountByParams(String[] accTypes, String curId, String netId, String corpId) {
        return fcAccDao.getAccountByParams(accTypes, curId, netId, corpId);
    }
    @Override
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
    @Override
    public List<FcAccPlusVO> listAccountByMoreParams(String curId, String netId, String corpId, String subjectId,
                                                     String sysSubjectCode) {
        return fcAccDao.getAccountByParams(curId, netId, corpId, subjectId, null, sysSubjectCode, null);
    }
    @Override
    public List<FcAccPlusVO> listAccountByParamsAndGrant(String curId, String netId, String corpId, String subjectId,
                                                         String sysSubjectCode, List<String> accs, String bankTypeId) {
        return fcAccDao.getAccountByParamsAndGrant(curId, netId, corpId, subjectId, sysSubjectCode, accs, bankTypeId);
    }
    @Override
    public List<FcAccPlusVO> listAccountByParamsAndGrantForPool(String curId, String netId, String corpId,
                                                                String subjectId, String sysSubjectCode, List<String> accs, String bankCode) {
        return fcAccDao.getAccountByParamsAndGrantForPool(curId, netId, corpId, subjectId, sysSubjectCode, accs, bankCode);
    }
    @Override
    public FcAccVO getUniqueById(String id) {
        return this.getFcAcc(id);
    }
    @Override
    public FcAccVO findFcAccByIdForPool(String id) {
        return fcAccService.findFcAccByIdForPool(id);
    }
    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser userVO) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_QUERY, userVO.getId()), "t.corp_Id", params);
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
        List<FcAccListVO> list = output.getData();
        if(list.size()>0) {
            Map<String, CorpDTO>  corpMap = corpServiceI.getCorpNetMap();
            Map<String, BtCurrencyDTO>  curMap = btCurrencyServiceI.getCurrency();
            for(FcAccListVO vo:list){
                vo.setAccTypeName(dictServiceI.get("InnerAccount.AccType", vo.getAccType()));
                vo.setAccStateName(dictServiceI.get("inneraccount.fcStatus", vo.getAccState()));
                vo.setBalanceWayName(dictServiceI.get("Admin.BalanceWay", vo.getBalanceWay()));

                CorpDTO corpDTO= corpMap.get(vo.getCorpId());
                vo.setCorpName(corpDTO.getName());
                vo.setNetName(corpDTO.getNetName());
                vo.setCurName(curMap.get(vo.getCurId()).getCurName());
            }
        }
        return output;
    }

    /**
     * 获取单个账号的余额信息
     *
     * @param account
     * @return
     */
    @Override
    public FcAccBalanceQueryVO getFcAccBalByAccount(String account) {
        String sql = "select fa.blnctr_acc as account,fa.cur_id as curId, fa.acc_name as accName, fa.corp_id as corpId, fa.subject_id as subjectId," +
                " (case fa.pool_sign when '1' then fcp.subject_name else fc.subject_name end) as subjectName,"+
                " fa.overdraft_money as overdraftMoney,fa.acc_type as accType, fa.acc_open_time as accOpenTime, fa.deadline_date as deadlineDate,fa.relation_acc as relationAcc," +
                " fa.interest_rate as interestRate,fa.pool_sign as poolSign " +
                " from fc_acc fa " +
                " left join fc_subject fc on fa.subject_id=fc.id "+
                " left join fc_subject_pool fcp on fa.subject_id=fcp.id "+
                " where fa.blnctr_acc=:account";
        HashMap<String, String> params = new HashMap<>();
        params.put("account", account);
        List<FcAccBalanceQueryVO> list = this.listBySQLAliasToBean(sql, params, FcAccBalanceQueryVO.class);
        if (!EmptyUtils.isEmptyList(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据 netId accType 查询 fc_acc, 返回 blnctrAcc 集合
     *
     * @param netId
     * @param accType
     * @return
     */
    @Override
    public List<String> getBlnctrAccByNetIdAndAccType(String netId, String accType) {
        return fcAccService.getBlnctrAccByNetIdAndAccType(netId, accType);
    }
    @Override
    public List<FcAccDTO> getFcAccByNetIdAndCheckerCodeIsNotNull(String netId) {
        return fcAccService.getFcAccByNetIdAndCheckerCodeIsNotNull(netId);
    }
    @Override
    public List<FcAccDTO> getFcAccByNetIdAndAccTypeCheckerCodeIsNotNull(String netId, String accType) {
        return fcAccService.getFcAccByNetIdAndAccTypeCheckerCodeIsNotNull(netId, new String[]{accType});
    }

    @Override
    public List<FcAccDTO> getFcAccByAccountAndNetIdAndAccTypeCheckerCodeIsNotNull(String account, String netId, String accType){
        return fcAccService.getFcAccByAccountAndNetIdAndAccTypeCheckerCodeIsNotNull(account,netId,accType);
    }

    @Override
    public List<FcAccDTO> getFcAccByNetIdAndAccTypeCheckerCodeIsNotNull(String netId, String[] accType) {
        return fcAccService.getFcAccByNetIdAndAccTypeCheckerCodeIsNotNull(netId, accType);
    }

    @Override
    public List<FcAccDTO> getFcAccByIdAndNotAccType(String netId, String[] accType){
        return fcAccService.getFcAccByIdAndNotAccType(netId, accType);
    }

    @Override
    public List<FcAccDTO> getFcAccByNetId(String netId) {
        return fcAccService.getFcAccByNetId(netId);
    }
    @Override
    public List<String> getFcAccCurByNetId(String netId) {
        return fcAccService.getFcAccCurByNetId(netId);
    }
    @Override
    public List<Map<String,String>> getFcAccCurByNetIds(String netId) {
        return fcAccService.getFcAccCurByNetIds(netId);
    }

    @Override
    public List<FcAccDTO> getFcAccByNetIdAndOther(String netId) {
        return fcAccService.getFcAccByNetIdAndOther(netId);
    }
    @Override
    public List<String> getBlnctrAccByNetId(String netId) {
        return fcAccService.getBlnctrAccByNetId(netId);
    }
    /**
     * 根据 netId , 查询 fc_acc 表，返回 blnctr_acc 结算中心账号
     * 除了 netId，没有其它条件
     *
     * @param netId
     * @return
     */
    @Override
    public List<String> getBlnctrAccByNetId2(String netId) {
        return fcAccService.getBlnctrAccByNetId2(netId);
    }
    @Override
    public Map<String, String> getBlnctrAccAndAccNameByNetId(String netId) {
        return fcAccService.getBlnctrAccAndAccNameByNetId(netId);
    }
    @Override
    public Map<String, Integer> getBlnctrAccAndBalanceWayByNetIdAndNotAccType(String netId, String accType) {
        return fcAccService.getBlnctrAccAndBalanceWayByNetIdAndNotAccType(netId, accType);
    }
    @Override
    public List<String> getBlnctrAccByNetIdAndIfCompare(String netId, String compare) {
        return fcAccService.getBlnctrAccByNetIdAndIfCompare(netId, compare);
    }
    @Override
    public Map<String, Date> getInterestStartDate(String netId, String[] accTypeCode, String... sysSubjectFilters) {
        return fcAccService.getInterestStartDate(netId, accTypeCode, sysSubjectFilters);
    }
    @Override
    public List<FcAccDTO> findNeedInterestAcc(String corpId, String accType, String subjectId) {
        return fcAccService.findNeedInterestAcc(corpId, accType, subjectId);
    }
    @Override
    public Map<String, Date> getInterestStartDate(List accountList) {
        return fcAccService.getInterestStartDate(accountList);
    }
    @Override
    public FcAccDTO getFcAccByCorpIdAndBlnctrAcc(String corpId, String blnctrAcc) {
        return fcAccService.getFcAccByCorpIdAndBlnctrAcc(corpId, blnctrAcc);
    }
    @Override
    public List<FcAccDTO> getFcAccByIds(List<String> ids) {
        return fcAccService.getFcAccByIds(ids);
    }
    @Override
    public List<FcAccDTO> getFcAccByBlnctrAccs(List<String> blnctrAccs) {
        return fcAccService.getFcAccByBlnctrAccs(blnctrAccs);
    }

    @Override
    public List<FcAccDTO> getFcAccByBlnctrAccsAndAcctype(List<String> blnctrAccs, Map<String, String> queryParams) {

        return fcAccService.getFcAccByBlnctrAccsAndAcctype(blnctrAccs, queryParams);
    }

    @Override
    public List<Map<String, Object>> getLoanAccByCorpCode(String corpId) {
        return fcAccService.getLoanAccByCorpCode(corpId);
    }
    @Override
    public List<Map<String, Object>> getCorpCurAccInfo(String loanAcc) {
        return fcAccService.getCorpCurAccInfo(loanAcc);
    }
    @Override
    public List<FcAccDTO> listByCondition(FcAccDTO fcAccDto) {
        return fcAccDao.listByCondition(fcAccDto);
    }
    @Override
    public List<Map<String, Object>> queryAccuBalanceForNone(String account, String netCode, String[] accTypeCode, Date endInstDate) {
        return fcAccDao.queryAccuBalanceForNone(account, netCode, accTypeCode, endInstDate);
    }
    @Override
    public List<FcAccDTO> getFcCarryList(FcAccDTO fcAccDto) {
        return fcAccDao.getFcCarryList(fcAccDto);
    }
    @Override
    public DataTablesOutput getPoolDataTableAllNormal(DataTablesInput dataTablesInput, String corpAuthSql, Map<String, Object> params) {
        return fcAccDao.getPoolDataTableAllNormal(dataTablesInput, corpAuthSql, params);
    }
    @Override
    public void merge(FcAccVO entity) {
        com.bytter.system.inneraccount.entity.FcAcc acc = new com.bytter.system.inneraccount.entity.FcAcc();
        BeanUtils.copyProperties(entity, acc);
        this.getSession().merge(acc);
    }
    @Override
    public List<FcAccVO> listBy(String propertyName, String value) {
        List<FcAcc> fcAcc = fcAccDao.listBy(propertyName, value);
        List<FcAccVO> list = BeanUtils.copyListProperties(fcAcc, FcAccVO.class);
        return list;
    }
    @Override
    public List<FcAccSubjectDTO> getAccAndSubjectData(List<String> corpAuth, boolean isPool) {
        return fcAccService.getAccAndSubjectData(corpAuth, isPool);
    }
    @Override
    public List<FcAccSubjectDTO> getAccAndSubject(List<String> corpAuth, FcAccSubjectDTO dto, boolean isPool) {
        return fcAccDao.getAccAndSubject(corpAuth, dto, isPool);
    }
    @Override
    public List<FcAccSubjectDTO> getAccNameAndSubjectName(String netId, String accType, List<String> corpAuth) {
        return fcAccService.getAccNameAndSubjectName(netId, accType, corpAuth);
    }
    @Override
    public List<FcAccSubjectDTO> getAccAndSubjectAndCurData(Map<String, Object> params, List<String> blnctrAccLists) {
        return fcAccService.getAccAndSubjectAndCurData(params, blnctrAccLists);
    }
    @Override
    public List<FcAccSubjectDTO> getAccNameAndMeasureUnitByCorpId(List<String> corpIds) {
        return fcAccService.getAccNameAndMeasureUnitByCorpId(corpIds);
    }
    @Override
    public List<FcAccSubjectDTO> getAccSubjectDateByCorpIds(List<String> corpIds) {
        return fcAccService.getAccSubjectDateByCorpIds(corpIds);
    }
    @Override
    public FcAccSubjectDTO getAccSubjectCurDateByAccount(String account) {
        return fcAccService.getAccSubjectCurDateByAccount(account);
    }
    @Override
    public List<FcAccSubjectDTO> getAccSubjectCurDateByAccounts(List<String> accounts) {
        return fcAccService.getAccSubjectCurDateByAccounts(accounts);
    }
    @Override
    public List<FcAccCorpCurrencyDTO> getAccCorpPoolCurDataByAccounts(List<String> accounts) {
        return fcAccService.getAccCorpPoolCurDataByAccounts(accounts);
    }
    @Override
    public List<FcAccSubjectDTO> getAccCurDateByCorpIdsAndPool(List<String> corpIds, String isPool) {
        return fcAccService.getAccCurDateByCorpIdsAndPool(corpIds, isPool);
    }
    @Override
    public List<FcAccSubjectDTO> getAccSysCorpDateByCorpIdsAndPool(List<String> corpIds, String isPool) {
        return fcAccService.getAccSysCorpDateByCorpIdsAndPool(corpIds, isPool);
    }
    @Override
    public List<FcAccSubjectDTO> getAccCurDateByAccountAndPool(String account, String isPool) {
        return fcAccService.getAccCurDateByAccountAndPool(account, isPool);
    }
    @Override
    public List<FcAccSubjectDTO> getAccCurDataByCorpIds(List<String> corpIds) {
        return fcAccService.getAccCurDataByCorpIds(corpIds);
    }
    public List<FcAccSubjectDTO> getAccCurDataByNetIds(List<String> netIds) {
        return fcAccService.getAccCurDataByNetIds(netIds);
    }
    @Override
    public List<FcAccDTO> getNoticeAccount(String corpId, String curId) {
        return fcAccService.getNoticeAccount(corpId, curId);
    }
    @Override
    public List<String> getBlnctrAccByNetCodeAndAccType(String netCode, String accType) {
        return fcAccService.getBlnctrAccByNetCodeAndAccType(netCode, accType);
    }
    @Override
    public List<FcAccPlusDTO> getTallyAccTypeAccList(String netId, String corpId, String curId, List<String> accList) {
        return fcAccService.getTallyAccTypeAccList(netId, corpId, curId, accList);
    }
    @Override
    public List<FcAccDTO> queryBySomeCondPgd(String condition) {
        return fcAccService.queryBySomeCondPgd(condition);
    }

    @Override
    public Map<String,FcAccDTO> listFcAccByNetId(String netId) {
        List<FcAccDTO> fcAccDTOS = fcAccDao.listFcAccByNetId(netId);
        return fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, fcAccDTO -> fcAccDTO,
                (pre, next) -> next));
    }

    @Override
    public Map<String, FcAccDTO> listAllFcAcc() {
        List<FcAccDTO> fcAccDTOS = fcAccDao.listAllFcAcc();
        return fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, fcAccDTO -> fcAccDTO,
                (pre, next) -> next));
    }

    @Override
    public Map<String, FcAccDTO> queryFcAccByIdsOrAccs(List<String> ids, List<String> accs) {
        Map<String, FcAccDTO> result = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(ids)){
            List<FcAccDTO> fcAccDTOS = fcAccDao.getFcAccByIds(ids);
            result = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getId, fcAccDTO -> fcAccDTO,
                    (pre, next) -> next));
        }
        if (CollectionUtil.isNotEmpty(accs)){
            List<FcAccDTO> fcAccDTOS = fcAccDao.getFcAccByBlnctrAccs(accs);
            result = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, fcAccDTO -> fcAccDTO,
                    (pre, next) -> next));
        }
        return result;
    }

    @Override
    public List<FcAccDTO> getBlnctrAccByUserId(String userId) {
        return fcAccDao.getBlnctrAccByUserId(userId);
    }

    @Override
    public List<String> getFcAccByLikeBlnctrAccAndNetIdAndCorpIdAndAccType(String blnctrAccParam, String netId, String[] corpIds, String accType) {
        return fcAccDao.getFcAccByLikeBlnctrAccAndNetIdAndCorpIdAndAccType( blnctrAccParam,  netId, corpIds, accType);
    }

    @Override
    public List<FcAccDTO> getFcAccByInterest() {
        return fcAccDao.getFcAccByInterest();
    }
}
