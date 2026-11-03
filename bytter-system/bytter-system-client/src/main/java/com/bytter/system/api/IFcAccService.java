package com.bytter.system.api;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.FcAccBalanceQueryVO;
import com.bytter.system.api.vo.FcAccListVO;
import com.bytter.system.api.vo.FcAccPlusVO;
import com.bytter.system.api.vo.FcAccVO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 内部账号查询相关接口
 *
 * @author liyh
 */
public interface IFcAccService {
    List<FcAccVO> listBy(String propertyName, String value);
    FcAccDTO getFcAccDTO(String id);
    FcAccVO getFcAcc(String id);
    void delInterestAcc(FcAccVO entity);
    void delete(String id);
    void deleteEntity(FcAccVO entity, ICurrentUser user);
    DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, ICurrentUser userVO);
    List<FcAccPlusVO> listAccountByMoreParamsForPool(String curId, String netId, String corpId, String subjectId, String sysSubjectCode);
    void dateCompareForPool(Date date1, Date date2);
    void save(FcAccVO entity);
    void update(FcAccVO entity);
    String saveOrUpdate(FcAccVO entity);
    boolean isExistMulByBlnctrAcc(String blnctrAcc, String opType, String id);
    void updateRate(String blnctrAcc, String colName, BigDecimal instrate);
    FcAccVO findFcAccByBlnctrAcc(String blnctrAcc);
    List<FcAccVO> getFcAcc(FcAccVO fcAcc, String sql, Map<String, Object> params);
    List<FcAccVO> findFcAcc(FcAccVO fcAcc, String sql);
    int check(FcAccVO fcAcc, String accState, String oldAccState);
    boolean isOtherAccountQuote(String blnctrAcc);
    boolean isNotReceivedInterest(String blnctrAcc);
    void changeState(String blnctrAcc, String state);
    DataTablesOutput getDataTableAll(DataTablesInput dataTablesInput, ICurrentUser user);
    DataTablesOutput getDataTableAllForPool(DataTablesInput dataTablesInput, ICurrentUser user);
    String getBelongingCorpIdsInSql(DataTablesInput dataTablesInput);
    List<FcAccVO> getFcAccByAccForDeduct(String accForDeduct);
    boolean isExsits(String subujectId);
    boolean isExsitsForValidBankAccByInnerAcc(String blnctrAcc);
    /**
     * TODO huangyunlong
     *
     * @param userId
     * @param poolId
     * @param corpId
     * @param accountType
     * @param permission
     * @return
     */
    List<FcAccDTO> getBlnctrAcc(String userId, String poolId, String corpId, String accountType, String permission);
    /**
     * TODO huangyunlong
     *
     * @param userId
     * @param corpId
     * @param accountType
     * @param permission
     * @return
     */
    List<FcAccDTO> getBlnctrAccByCorpId(String userId, String corpId, String accountType, String permission);
    DataTablesOutput getDataTableAllNormal(DataTablesInput dataTablesInput, ICurrentUser user);
    /**
     * 查询账号币别的相关科目账号
     *
     * @param chargeAccount 变化的账号
     * @param netId         网点id
     * @param subjectCode   科目代码
     * @return 内部账号集合
     */
    List<FcAccDTO> getFcAccBySubjectCode(String chargeAccount, String netId, String subjectCode);
    /**
     * 判断账户是否同币别
     *
     * @param blnctrAcc     内部账号
     * @param chargeAccount 相比较的账号
     * @return 是否
     */
    boolean getFcAccCurCode(String blnctrAcc, String chargeAccount);
    /**
     * 查询账户相关信息
     *
     * @param account 内部账号
     * @return 一个内部账号实体
     */
    FcAccDTO getAccDTOByAccount(String account);
    /**
     * 查询相关科目账号
     *
     * @param netId       网点id
     * @param corpId      单位id
     * @param subjectCode 科目代码
     * @return 内部账号集合
     */
    List<FcAccDTO> getFcAccByCorpSubjectCode(String netId, String corpId, String subjectCode);
    /**
     * 查询相关科目账号
     *
     * @param netId      网点id
     * @param corpId     单位id
     * @param subjectId  科目代码
     * @param curId      币种id
     * @param instrtType 利率类型
     * @param account    账号
     * @return 内部账号集合
     */
    List<FcAccDTO> getFcAccByParams(String netId, String corpId, String subjectId, String curId, String instrtType, String account);
    /**
     * 根据系统科目查询账户
     *
     * @param sysSubjectCode 系统科目代码
     * @param netIds         网点id
     * @param corpIds        单位id
     * @param curId          币别id
     * @return 内部账号集合
     */
    List<FcAccDTO> getFcAccBySysSubjectId(String sysSubjectCode, String netIds, Set<String> corpIds, String curId);
    List<FcAccDTO> getFcAccBySysSubject(String sysSubjectCode, String netIds, List<String> corpIds, List<String> curIds);
    /**
     * 根据科目id、科目系统code查询账户(过滤掉关联了银行代码)
     *
     * @param subjectId      科目Id
     * @param sysSubjectCode 系统科目代码
     * @param netIds         网点id
     * @param corpIds        单位id
     * @param curId          币别id
     * @return 内部账号集合
     */
    List<FcAccDTO> getFcAccBySysSubjectIdFilterBankCode(String subjectId, String sysSubjectCode, String netIds, Set<String> corpIds, String curId);
    /**
     * 根据系统科目（开户种类）查询账户
     *
     * @param sysSubjectCode 系统科目代码
     * @param accType        账号类型
     * @param netId          网点id
     * @param corpCods       单位代码
     * @param curId          币别id
     * @return 内部账号集合
     */
    List<FcAccDTO> findBySysSubjectCode(String sysSubjectCode, String accType, String netId, Set<String> corpCods, String curId);
    /**
     * 开户未复核通过的账户
     *
     * @param dayClose 网点日结日期传输显示对象
     * @return 内部账号集合
     */
    List<FcAccDTO> findNoCheckVFcAcc(FcDayCloseDTO dayClose);
    /**
     * 查询账户集合
     *
     * @param accList 账号集合
     * @return 内部账号集合
     */
    List<FcAccDTO> getAccSet(List<String> accList);
    /**
     * 查询表外账户集合
     *
     * @return 内部账号集合
     */
    List<FcAccDTO> getOutAccTypeAccs();
    /**
     * 获取可记账内部账号
     *
     * @param accs 账号集合
     * @return 内部账号集合
     */
    List<FcAccPlusVO> getTallyAccTypeAccs(List<String> accs);
    List<FcAccPlusDTO> getTallyAccTypeAccsDto(List<String> accs);
    /**
     * 通过父银行账号 找子账号对应的内部账号【N,P,S】
     *
     * @param parentBankAccId 父银行账号id
     * @param curId           币别id
     * @param ids             可空 权限 sql
     * @return 内部账号集合
     */
    List<FcAccPlusVO> getSubAccFcAccByParentBankAcc(String parentBankAccId, String curId, List<String> ids);
    /**
     * 判断两个账号是否所属同一网点下
     *
     * @param accInfo1 账号
     * @param accInfo2 账号
     * @return 是否
     */
    boolean isTheSameNetByFcAcc(String accInfo1, String accInfo2);
    /**
     * 内部账号查询 基础平台权限问题
     *
     * @param dataTablesInput 查询条件集合
     * @return 内部账号集合
     */
    DataTablesOutput getDataTableAllNormalSYS(DataTablesInput dataTablesInput, ICurrentUser user);
    /**
     * 根据查询条件查询内部账号
     *
     * @param input 查询条件集合
     * @return 内部账号集合
     */
    DataTablesOutput<FcAccListVO> getFcAcc(DataTablesInput input);
    /**
     * 查询资金池账户相关信息
     *
     * @param account 内部账号
     * @return 内部账号集合
     */
    FcAccDTO getPoolAccDTOByAccount(String account);
    /**
     * 获取网点下计息的资金池账号
     *
     * @param netId        网点id
     * @param accType      账号类别
     * @param interestSign 计息标志
     * @param poolSign     资金池标识 1为资金池
     * @return 内部账号集合
     */
    List<FcAccDTO> getNetAccByPoolSign(String netId, String accType, String interestSign, String poolSign);
    /**
     * 查询资金池科目的资金池账号
     *
     * @param netId     网点id
     * @param curId     币别id
     * @param subjectId 科目id
     * @param poolSign  资金池标识 1为资金池
     * @return 内部账号集合
     */
    List<FcAccDTO> getNetPoolAccBySubject(String netId, String curId, String subjectId, String poolSign);
    /**
     * 根据资金池单位查询账号(开户待复核、开户复核打回、改户待复核、改户复核打回、销户待复核、销户复核打回)
     *
     * @param netId    资金池主体id
     * @param corpId   单位id
     * @param isFilter 是否过滤状态 true过滤，flase 不过滤
     * @return 内部账号集合
     */
    List<FcAccDTO> listFcAccPoolByCorpId(String netId, String corpId, boolean isFilter);
    /**
     * 根据资金池标志生成内部账号生成法则
     *
     * @param poolSign
     * @return
     */
    String getRuleVaql(String poolSign);
    /**
     * 根据单位集合 得到内部账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    List<FcAccDTO> getFcAccByCorpIdList(List<String> corpIdList, List<String> accState, String joinSql);
    /**
     * 根据资金池或开户单位查找内部账号。
     *
     * @param userId
     * @param poolId
     * @param corpId
     * @param busType
     * @param listAccId
     * @return
     */
    List<FcAccDTO> getBlnctrAcc(String userId, String poolId, String corpId, String busType, List<String> listAccId,String accAuthSql);
    /**
     * 根据开户单位,单据类型查找正常状态非资金池内部账号。
     *
     * @param userId
     * @param corpId
     * @param busType
     * @param listAccId
     * @return
     */
    List<FcAccDTO> getBlnctrAccByCorpId(String userId, String corpId, String busType, List<String> listAccId);
    /**
     * 根据 netId、blnctrAccs 返回 k=blnctrAcc, v=accName
     *
     * @param netId
     * @param blnctrAccs
     * @return
     */
    Map<String, String> getAccNameByNetIdAndBlnctrAccs(String netId, List<String> blnctrAccs);
    List<FcAccVO> transferOutAcc(String blnctrAcc, String netId);
    List<FcAccVO> getAccListByExpPay(FcAccVO vo);
    List<FcAccDTO> listFcAccInfoForPool(List<String> ids, List<String> accState);
    List<FcAccDTO> listFcAccInFoByCorpIds(List<String> corpIds, String accState);
    List<FcAccDTO> listFcAccPoolByCorpIds(List<String> corpIds, String accState, String poolSign);
    BankTypeDTO getInfoByBankCode(String bankCode);
    boolean isExsitsByExpPay(String blnctrAcc);
    Date getValidDate(String netId);
    FcAccPlusVO getFcAccVONotAuthByParams(String netId, String corpId, String subjectId, String curId);
    List<FcAccPlusVO> listAccountByParams(String curId, String netId, String corpId, String subjectId);
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param subjectId
     * @return
     */
    List<FcAccPlusVO> listAccountByParams(String curId, String netId, String corpId, String subjectId, String endUnit);
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @return
     */
    List<FcAccListVO> listAccountByParams(String[] accTypes, String curId, String netId, String corpId);
    String createSequence(String netId, String curId, String accType);
    List<FcAccPlusVO> listAccountByMoreParams(String curId, String netId, String corpId, String subjectId, String sysSubjectCode);
    List<FcAccPlusVO> listAccountByParamsAndGrant(String curId, String netId, String corpId, String subjectId, String sysSubjectCode, List<String> accs, String bankTypeId);
    List<FcAccPlusVO> listAccountByParamsAndGrantForPool(String curId, String netId, String corpId, String subjectId, String sysSubjectCode, List<String> accs, String bankCode);
    FcAccVO getUniqueById(String id);
    FcAccVO findFcAccByIdForPool(String id);
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser userVO);
    //========================================================================
    /**
     * 根据单位集合 得到内部账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    List<String> getFcAccsByCorpIdList(List<String> corpIdList, List<String> accState, String joinSql);
    DataTablesOutput listFcAccInfoDataTable(DataTablesInput dataTablesInput, String joinSql);
    List<FcAccVO> getAccListByInfo(String netId, String corpId, String sysSubjectCode, String curId, String bankTypeId);
    int getFcAccCountByCorpId(String corpId, String busType);
    List<FcAccVO> listFcAccByCorpId(String corpId, String curId);
    FcAccVO getPoolFcAccVOByBlnctrAcc(String blnctrAcc);
    List<FcAccDTO> findBySysSubjectId(String sysSubjectId, String netIds, Set<String> corpIds, String curId);
    /**
     * <b>功能介绍：</b>根据科目id,系统科目code查询账户(过滤掉关联了银行代码)<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    List<FcAccDTO> findBySysSubjectIdFilterBankCode(String subjectId, String sysSubjectCode, String netIds, Set<String> corpIds, String curId);
    List<FcAccDTO> listFcAccInfo(List<String> ids, List<String> accState);
    FcAccPlusVO getFcAccVOByParams(String netId, String corpId, String subjectId, String curId, String sqlUnion);
    FcAccVO getFcAccVOByBlnctrAcc(String blnctrAcc);
    DataTablesOutput getDataTableAll(DataTablesInput dataTablesInput, String corpAuthSql, Map<String, Object> params);
    /**
     * 资金池内部账号查询
     *
     * @param dataTablesInput
     * @return
     */
    DataTablesOutput getDataTableAllForPool(DataTablesInput dataTablesInput, String corpAuthSql, Map<String, Object> params);
    /**
     * 内部账号查询状态正常
     *
     * @param dataTablesInput
     * @return
     */
    DataTablesOutput getDataTableAllNormal(DataTablesInput dataTablesInput, String corpAuthSql, Map<String, Object> params);
    List<FcAccPlusVO> getAccountByParams(String curId, String netId, String corpId, String subjectId, String joinSql, String sysSubjectCode, String bankCode);
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
    List<FcAccPlusVO> getAccountByParamsForPool(String curId, String netId, String corpId, String subjectId, String joinSql, String sysSubjectCode, String bankCode);
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
    List<FcAccPlusVO> getAccountByParams(String curId, String netId, String corpId, String subjectId, String endUnit, String joinSql, String sysSubjectCode, String bankCode, Boolean isHavDel);
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
    List<FcAccPlusVO> getAccountByParamsForPool(String curId, String netId, String corpId, String subjectId, String endUnit, String joinSql, String sysSubjectCode, String bankCode);
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
    List<FcAccPlusVO> getAccountByParamsAndGrant(String curId, String netId, String corpId, String subjectId, String sysSubjectCode, List<String> accList, String bankTypeId);
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @param sysSubjectCode
     * @return
     */
    List<FcAccPlusVO> getAccountByParamsAndGrantForPool(String curId, String netId, String corpId, String subjectId, String sysSubjectCode, List<String> accList, String bankCode);
    /**
     * 根据( 账号种类/状态为正常/部分冻结/只收不支)参数筛选 内部账号
     *
     * @param curId
     * @param netId
     * @param corpId
     * @return
     */
    List<FcAccListVO> getAccountByParams(String[] accTypes, String curId, String netId, String corpId);
    boolean isExistInterestPayable(String blnctrAcc);
    DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, String sqlWhere, Map<String, Object> params);
    DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String sqlWhere, Map<String, Object> params);
    FcAccVO findFcAccById(String id);
    List<FcAccVO> listByBlnctrAcc(String blnctrAcc);
    List<FcAccVO> queryMultiAccAll(String netId, String curId, String accType);
    DataTablesOutput getPoolDataTableAllNormal(DataTablesInput dataTablesInput, String corpAuthSql, Map<String, Object> params);
    void merge(FcAccVO acc);
    FcAccBalanceQueryVO getFcAccBalByAccount(String account);
    /**
     * 根据 netId accType 查询 fc_acc, 返回 blnctrAcc 集合
     *
     * @param netId
     * @param accType
     * @return
     */
    List<String> getBlnctrAccByNetIdAndAccType(String netId, String accType);
    /**
     * 根据netId 和 checker_code is not null，查询 fc_acc fa ,
     *
     * @param netId
     * @return
     */
    List<FcAccDTO> getFcAccByNetIdAndCheckerCodeIsNotNull(String netId);
    /**
     * 根据netId 和 accType checker_code is not null，查询 fc_acc a
     *
     * @param netId
     * @param accType
     * @return
     */
    List<FcAccDTO> getFcAccByNetIdAndAccTypeCheckerCodeIsNotNull(String netId, String accType);

    List<FcAccDTO> getFcAccByAccountAndNetIdAndAccTypeCheckerCodeIsNotNull(String account, String netId, String accType);
    /**
     * @param netId
     * @param accType
     * @return
     */
    List<FcAccDTO> getFcAccByNetIdAndAccTypeCheckerCodeIsNotNull(String netId, String[] accType);

    List<FcAccDTO> getFcAccByIdAndNotAccType(String netId, String[] accType);

    /**
     * 根据 netId 查询 fc_acc
     *
     * @param netId
     * @return
     */
    List<FcAccDTO> getFcAccByNetId(String netId);
    /**
     * 根据 netId 查询 fc_acc币种集合
     *
     * @param netId
     * @return
     */
    List<String> getFcAccCurByNetId(String netId);

    List<Map<String,String>> getFcAccCurByNetIds(String netId);

    /**
     * 根据 netId and acc_state <> 'D' and interest_sign = '1' , 查询 fc_acc , 返回 blnctrAcc 集合
     *
     * @param netId
     * @return
     */
    List<String> getBlnctrAccByNetId(String netId);
    /**
     * 根据 netId , 查询 fc_acc 表，返回 blnctr_acc 结算中心账号
     *
     * @param netId
     * @return
     */
    List<String> getBlnctrAccByNetId2(String netId);
    /**
     * 根据 netId and a.Interest_Sign = '1' and a.Acc_Type Not In ('05', '06', '08' ,'15','16'), 查询 fc_acc a,
     * 返回 a.blnctr_acc as blnctrAcc, a.Acc_Start_Date as accStartDate, a.cur_id as curId, a.acc_type as accType
     *
     * @param netId
     * @return
     */
    List<FcAccDTO> getFcAccByNetIdAndOther(String netId);
    /**
     * 根据 netId , 查询 fc_acc ，返回 k=blnctrAcc , v=accName 集合
     *
     * @param netId
     * @return
     */
    Map<String, String> getBlnctrAccAndAccNameByNetId(String netId);
    /**
     * 根据条件 c.acc_type <> :accType and c.NET_ID = :netId,查询 fc_acc c ，返回 c.BLNCTR_ACC c.BALANCE_WAY
     */
    Map<String, Integer> getBlnctrAccAndBalanceWayByNetIdAndNotAccType(String netId, String accType);
    /**
     * 根据 netId、compare, 查询返回 blnctrAcc 集合
     */
    List<String> getBlnctrAccByNetIdAndIfCompare(String netId, String compare);
    /**
     * 根据条件 and acc.acc_state not in ('D')  and acc.interest_sign = '1'
     * if (StringUtils.isNotBlank(netCode)) {
     * sql.append(" and acc.net_id = :netCode ");
     * params.put("netCode", netCode);
     * }
     * if (accTypeCode != null && accTypeCode.length > 0) {
     * sql.append(" and acc.acc_type in (:accTypeCode) ");
     * params.put("accTypeCode", accTypeCode);
     * }
     * if (sysSubjectFilters != null && sysSubjectFilters.length > 0) {
     * sql.append(" and acc.subject_id in (:subjectCodes) ");
     * params.put("subjectCodes", sysSubjectFilters);
     * }
     * return key=账号  BLNCTR_ACC
     * value=起利日期  INTEREST_START_DATE
     */
    Map<String, Date> getInterestStartDate(String netId, String[] accTypeCode, String... sysSubjectFilters);
    /**
     * StringBuilder sql = new StringBuilder();
     * Map<String, Object> params = new HashMap<String, Object>();
     * sql.append("select acc.* from fc_acc acc where acc.subject_id = :subjectId and acc.acc_type = :accType and acc.corp_id = :corpId and acc.acc_state = :accState ");
     * params.put("subjectId", subjectId);
     * params.put("accType", accType);
     * params.put("corpId", corpId);
     * params.put("accState", InnerAccountConstant.ACC_STATE_NORMAL);
     * List<FcAccDTO> acc = listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
     *
     * @param corpId
     * @param accType
     * @param subjectId
     * @return
     */
    List<FcAccDTO> findNeedInterestAcc(String corpId, String accType, String subjectId);
    /**
     * 根据 accountList and acc_state <> 'D' and interest_sign = '1' , 查询 fc_acc
     * *   return key=账号  BLNCTR_ACC
     * *   value=起利日期  INTEREST_START_DATE
     *
     * @param accountList
     * @return
     */
    Map<String, Date> getInterestStartDate(List accountList);
    /**
     * select a.cur_id as curId, a.net_id as netId, a.pool_sign as poolSign from fc_acc a where corpId = :corpId and a.blnctrAcc = :blnctrAcc ;
     * Map<String, Object> map = new HashMap<String, Object>();
     * map.put("corpId", corpId);
     * map.put("blnctrAcc", blnctrAcc);
     *
     * @param corpId
     * @param blnctrAcc
     * @return
     */
    FcAccDTO getFcAccByCorpIdAndBlnctrAcc(String corpId, String blnctrAcc);
    /**
     * 根据 ids 查询 fc_acc
     * select * from fc_acc where id in (:ids);
     *
     * @param ids
     * @return
     */
    List<FcAccDTO> getFcAccByIds(List<String> ids);
    /**
     * 根据 blnctrAccs 查询 fc_acc
     * select * from fc_acc where blnctr_acc in (:blnctrAccs) ;
     *
     * @param blnctrAccs
     * @return
     */
    List<FcAccDTO> getFcAccByBlnctrAccs(List<String> blnctrAccs);

    /**
     * 根据 blnctrAccs 和 accType 查询 fc_acc
     * select * from fc_acc where blnctr_acc in (:blnctrAccs) and acc_type in (:accTypeCode ) ;
     *
     * @param blnctrAccs
     * @return
     */
    List<FcAccDTO> getFcAccByBlnctrAccsAndAcctype(List<String> blnctrAccs, Map<String, String> queryParams);
    /**
     * 参考SQL
     * StringBuilder sql = new StringBuilder();
     * Map<String,Object> params = new HashMap<String,Object>();
     * if(StringUtils.isEmpty(corpId)){
     * return null;
     * }
     * sql.append("select acc.blnctr_acc as acc from fc_acc acc where acc.acc_type=:accType ");
     * sql.append("and acc.corp_id = :corpId ");
     * sql.append("and acc.acc_state= :accState ");
     * params.put("accType", InnerAccountConstant.FCACC_LOAN);
     * params.put("corpId", corpId);
     * params.put("accState", InnerAccountConstant.ACC_STATE_NORMAL);
     * return createSQLQuery(sql.toString(), params).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
     *
     * @param corpId
     * @return
     */
    List<Map<String, Object>> getLoanAccByCorpCode(String corpId);
    /**
     * 参考SQL
     * if(StringUtils.isEmpty(loanAcc)){
     * return null;
     * }
     * StringBuilder sql = new StringBuilder();
     * Map<String,Object> params = new HashMap<String,Object>();
     * sql.append("select a.blnctr_acc as account from fc_acc a, fc_acc b where a.corp_id = b.corp_id ");
     * sql.append("and a.cur_id = b.cur_id ");
     * sql.append("and a.acc_State in ('"+InnerAccountConstant.ACC_STATE_NORMAL+"', '"+InnerAccountConstant.ACC_STATE_PART_FREEZE+"') ");
     * sql.append("and a.acc_type = :accType ");
     * sql.append("and b.blnctr_acc = :loanAcc ");
     * params.put("accType", InnerAccountConstant.FCACC_BUSINESS_ACCOUNT);
     * params.put("loanAcc", loanAcc);
     * return createSQLQuery(sql.toString(), params).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
     */
    List<Map<String, Object>> getCorpCurAccInfo(String loanAcc);
    /**
     * 根据查询条件获取内部账号
     *
     * @param fcAccDto 查询参数实体类
     * @return 账号集合
     */
    List<FcAccDTO> listByCondition(FcAccDTO fcAccDto);
    /**
     * StringBuilder sql = new StringBuilder();
     * sql.append(" select a.blnctr_acc as account, a.cur_id as curCode,a.interest_sign as interestSign,");
     * sql.append(" a.predraw_sign as preSign,a.interest_start_date as interestTime ");
     * <p>
     * sql.append(" FROM fc_acc a ");
     * sql.append(" left join sys_corp s on a.net_id = s.id ");
     * sql.append(" left join bt_currency cur on a.cur_id = cur.id ");
     * sql.append(" left join bt_bank bank  on bank.id = a.bank_code ");
     * <p>
     * sql.append(" where  a.acc_State  IN  ('N','I')  and  a.interest_Sign  =  '1'  and  a.acc_Start_Date  <  :endInstDate  ");
     * Map<String, Object> params = new HashMap<String, Object>();
     * params.put("endInstDate", endInstDate);
     * if (StringUtils.isNotBlank(netCode)) {
     * sql.append("  and  a.net_id  =  :netCode  ");
     * params.put("netCode", netCode);
     * }
     * if (StringUtils.isNotBlank(account)) {
     * sql.append("  and  a.blnctr_acc  =  :account  ");
     * params.put("account", account);
     * }
     * if (accTypeCode != null && accTypeCode.length > 0) {
     * sql.append("  and  a.acc_Type  in  (:accTypeCode)  ");
     * params.put("accTypeCode", accTypeCode);
     * }
     *
     * @param netCode
     * @param accTypeCode
     * @param endInstDate
     * @return
     */
    List<Map<String, Object>> queryAccuBalanceForNone(String account, String netCode, String[] accTypeCode, Date endInstDate);
    /**
     * 根据查询条件获取内部账号信息
     *
     * @param fcAccDto 查询条件
     * @return 内部账号集合
     */
    List<FcAccDTO> getFcCarryList(FcAccDTO fcAccDto);
    List<FcAccSubjectDTO> getAccAndSubjectData(List<String> corpAuth, boolean isPool);
    List<FcAccSubjectDTO> getAccAndSubject(List<String> corpAuth, FcAccSubjectDTO dto, boolean isPool) ;
    /**
     * 查询 blnctr_acc, acc_name, subject_name
     *
     * @param netId
     * @param accType
     * @param corpAuth
     * @return
     */
    List<FcAccSubjectDTO> getAccNameAndSubjectName(String netId, String accType, List<String> corpAuth);
    /**
     * 查询 fc_acc、fc_subject、bt_currency
     * 有很多限制查询条件，具体看 SQL
     */
    List<FcAccSubjectDTO> getAccAndSubjectAndCurData(Map<String, Object> params, List<String> blnctrAccLists);
    /**
     * 根据 corpIds 查询 fc_acc a join fc_subject b，返回 a.blnctr_acc、a.acc_name、b.measure_unit
     *
     * @param corpIds
     * @return
     */
    List<FcAccSubjectDTO> getAccNameAndMeasureUnitByCorpId(List<String> corpIds);
    List<FcAccSubjectDTO> getAccSubjectDateByCorpIds(List<String> corpIds);
    FcAccSubjectDTO getAccSubjectCurDateByAccount(String account);
    /**
     * 根据 account ，返回 measure_unit acc_name cur_id
     *
     * @param accounts
     * @return
     */
    List<FcAccSubjectDTO> getAccSubjectCurDateByAccounts(List<String> accounts);
    List<FcAccCorpCurrencyDTO> getAccCorpPoolCurDataByAccounts(List<String> accounts);
    /**
     * 根据 corpIds, isPool 查询返回 a.blnctr a.acc_name b.cur_name
     */
    List<FcAccSubjectDTO> getAccCurDateByCorpIdsAndPool(List<String> corpIds, String isPool);
    /**
     * 根据 corpIds, isPool 查询返回 a.blnctr_acc as blnctrAcc, a.acc_name as accName, a.corp_id as corpId, a.acc_type as accType, b.name as corpName
     *
     * @param corpIds
     * @param isPool
     * @return
     */
    List<FcAccSubjectDTO> getAccSysCorpDateByCorpIdsAndPool(List<String> corpIds, String isPool);
    /**
     * 根据 account, isPool 查询返回 a.blnctr a.acc_name b.cur_name
     *
     * @param account
     * @param isPool
     * @return
     */
    List<FcAccSubjectDTO> getAccCurDateByAccountAndPool(String account, String isPool);
    /**
     * 根据 corpIds 查询返回 a.blnctr a.acc_name b.cur_name
     *
     * @param corpIds
     * @return
     */
    List<FcAccSubjectDTO> getAccCurDataByCorpIds(List<String> corpIds);
    List<FcAccSubjectDTO> getAccCurDataByNetIds(List<String> netIds);
    List<FcAccDTO> getNoticeAccount(String corpId, String curId);
    List<String> getBlnctrAccByNetCodeAndAccType(String netCode, String accType);
    /**
     * 获取可记账内部账号
     *
     * @param netId   网点id
     * @param corpId  单位id
     * @param curId   币别id
     * @param accList
     * @return 账号集合
     */
    List<FcAccPlusDTO> getTallyAccTypeAccList(String netId, String corpId, String curId, List<String> accList);
    /**
     * 查询账号信息
     *
     * @param condition 查询条件
     * @return 账号集合
     */
    List<FcAccDTO> queryBySomeCondPgd(String condition);

    /**
     * 查询网点帐号信息
     * @param netId 网点
     * @return
     */
    Map<String,FcAccDTO> listFcAccByNetId(String netId);

    /**
     * 获取所有帐号信息
     * @return
     */
    Map<String,FcAccDTO> listAllFcAcc();

    /**
     * 获取可记账内部账号
     *
     * @param idList   网点id
     * @param accList  结算中心账号
     * @return 账号集合
     */
    Map<String,FcAccDTO> queryFcAccByIdsOrAccs(List<String> idList, List<String> accList);

    List<FcAccDTO> getBlnctrAccByUserId(String userId);

    List<String> getFcAccByLikeBlnctrAccAndNetIdAndCorpIdAndAccType(String blnctrAccParam, String netId, String[] corpIds, String accType);

    List<FcAccDTO> getFcAccByInterest();
}
