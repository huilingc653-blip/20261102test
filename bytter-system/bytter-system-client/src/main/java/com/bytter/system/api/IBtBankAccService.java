package com.bytter.system.api;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.common.rest.RestResponse;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.BillBankBankAccVO;
import com.bytter.system.api.vo.BtBankAccBalVO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.api.vo.VAccListVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 银行账号接口类
 *
 * @author huangx
 * @since 2019-05-28 09:49:44
 */
public interface IBtBankAccService {
    String save(BtBankAccDTO entity);
    void update(BtBankAccDTO entity);
    BtBankAccVO getByUnion(String id, String curId);
    /**
     * 根据银行账号各个条件查询银行账号列表
     *
     * @param dto 银行账号查询条件
     * @return 银行账号集合
     */
    List<BtBankAccDTO> getBankAccList(BtBankAccDTO dto);
    List<BtBankAccDTO> getBankAccList(AccountSearchDTO dto);

    List<BtBankAccDTO> getBankAccList();

    /**
     * 银行帐号去重 bankcode
     */
    List<BtBankAccDTO> getDistinctBankAccList();
    /**
     * 根据条件查询分页列表
     *
     * @param dataTablesInput 表格封装信息
     * @return 表格封装信息
     */
    DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput);
    /**
     * 根据条件查询分页列表 离线账号，在线账号都可以显示
     *
     * @param dataTablesInput 表格封装信息
     * @return 表格封装信息
     */
    DataTablesOutput getDataTablesUnionAll(DataTablesInput dataTablesInput);
    /**
     * 根据id得到实体
     *
     * @param id 账号ID
     * @return 银行账号
     */
    BtBankAccDTO getBankAccById(String id);
    /**
     * 根据id得到实体
     *
     * @param id 账号ID
     * @return 银行账号
     */
    List<BtBankAccDTO> getBankAccDTOById(String id);
    /**
     * 根据银行账号各个条件查询银行账号列表(因为一个银行账号可以多币别，会有多条记录)
     *
     * @param dto 银行账号查询条件
     * @return 银行账号集合
     */
    List<BtBankAccDTO> getBankAccListAll(BtBankAccDTO dto);
    /**
     * 根据银行账户查找账户信息
     *
     * @param bankAcc 账号
     * @return 银行账号
     */
    BtBankAccDTO getBankAccInfo(String bankAcc);
    /**
     * 根据idlist 得到银行账户列表
     *
     * @param idList 银行账号ID集合
     * @return 银行账号集合
     */
    List<BtBankAccDTO> getBankAccByIdList(List<String> idList);
    /**
     * 根据idlist 得到银行账户列表(不关联余额)
     *
     * @param idList 银行账号ID集合
     * @return 银行账号集合
     */
    List<BtBankAccDTO> getBankAccNotUnionByIdList(List<String> idList);
    /**
     * 根据内部账号和币别，获取银行账号集合
     *
     * @param countAcc 内部账号
     * @param curId    币别
     * @return 银行账号集合
     */
    List<BtBankAccDTO> getBankAccByCountAcc(String countAcc, String curId);
    /**
     * 根据联行号，获取银行账号集合
     *
     * @param bankCode 内部账号
     * @return 银行账号集合
     */
    List<BtBankAccDTO> getBankAccByBankCode(String bankCode);

    /**
     * 根据银行账号id和币别，获取对应的父账号信息
     *
     * @param bankAccId 银行账号ID
     * @param curId     币别
     * @return 银行账号
     */
    BtBankAccDTO getParentAccByAccId(String bankAccId, String curId);
    /**
     * 根据条件得到银行账户列表
     *
     * @param accountSearchDTO 账号查询条件
     * @return 银行账号集合
     */
    List<BtBankAccDTO> getBankAccByAccountDTO(AccountSearchDTO accountSearchDTO);
    /**
     * 根据银行账号获取内部账户
     *
     * @param bankAcc 银行账户
     * @param curId   币别ID
     * @return 银行账号
     */
    String getBlnctrAcc(String bankAcc, String curId);
    /**
     * 根据账号属性查询账号是否有使用过这个属性
     *
     * @param propertyName 属性名称
     * @param value        属性值
     * @return true/false
     */
    boolean isUsedBaseData(String propertyName, String value);
    /**
     * 根据多个单位id得到银行账号列表
     *
     * @param corpIds 单位ID集合
     * @return 银行账号集合
     */
    List<BtBankAccDTO> getListByCorpIds(List<String> corpIds);

    /**
     * 根据多个bank_type_id 得到银行账号列表
     *
     * @param bankTypeIds
     * @return 银行账号集合
     */
    List<BtBankAccDTO> queryListByBankTypeIds(List<String> bankTypeIds);
    /**
     * 判断两个账号(或者户名)是否所属同一网点下
     *
     * @param accInfo1  账号或者户名
     * @param accInfo2  账号或者户名
     * @param isBankAcc 是否是账号 true-比较账户 false-比较户名
     * @return true/false
     */
    boolean isTheSameNetByAccInfo(String accInfo1, String accInfo2, boolean isBankAcc);
    /**
     * 修改银行流水截至时间
     *
     * @param bankAccId  账号ID
     * @param curId      币别ID
     * @param endDtlDate 截止日期
     */
    void updateEndDtlDate(String bankAccId, String curId, Date endDtlDate);
    /**
     * 获取最近一次银行流水截至时间
     *
     * @param bankAccId 账号ID
     * @param curId     币别ID
     */
    Date getLastEndDtlDate(String bankAccId, String curId);
    /**
     * Created by chenlj 2018/4/19 基础平台权限问题
     *
     * @param dataTablesInput
     * @param user
     * @return 权限信息
     */
    DataTablesOutput<BtBankAccVO> getDataTablesUnionSYS(DataTablesInput dataTablesInput, ICurrentUser user);
    /**
     * 根据条件查找账户信息
     *
     * @param id    账号ID
     * @param curId 币别ID
     * @return 银行账号
     */
    BtBankAccDTO getBankAccByIdAndCurId(String id, String curId);
    /**
     * 根据条件查找账户信息
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @return 银行账号
     */
    BtBankAccDTO getBankAccByBankAccAndCurId(String bankAcc, String curId);
    /**
     * 根据父账号和子账号对应的内部账号取具有支出属性的子账号
     *
     * @param prentAccId 父账号ID
     * @param curId      币别ID
     * @param counterAcc 内部账号
     * @return 银行账号集合
     */
    List<BtBankAccDTO> getOutPayByParentAcc(String prentAccId, String curId, String counterAcc);
    /**
     * 获取下属账户单位集合
     *
     * @param prentAccId 父类账户ID
     * @return 账号ID集合
     */
    List<String> listChildCorpIdsByParent(String prentAccId);
    /**
     * 查询单位/网点可用余额（企e金服接口）
     *
     * @param corpflag 单位标志
     * @param corpCode 单位编码
     * @return 账户余额
     */
    BigDecimal queryBankAccAvailableBal(String corpflag, String corpCode);
    /**
     * 同步接口-批量保存
     *
     * @param list 账号集合
     */
    void syncBatchSave(List<BtBankAccDTO> list);
    /**
     * 同步接口-批量保存
     *
     * @param list 账号集合
     */
    void syncBatchUpdate(List<BtBankAccDTO> list);
    /**
     * 同步接口-批量删除
     *
     * @param list 账号集合
     */
    void syncBatchDelete(List<BtBankAccDTO> list);
    /**
     * 查询正常状态在线和离线账号列表
     *
     * @param isOnline 1-在线账号，0-离线账号
     * @return 账号集合
     */
    List<BtBankAccDTO> getBankAccListByIsOnline(String isOnline);
    /**
     * 查询正常状态离线账号列表（带分页自动生成余额专用）
     *
     * @param parameterInput
     * @return
     */
    RestResponse<BtBankAccDTO> bankAccResult(IParameterInput parameterInput);
    /**
     * 银行账号限额校验
     *
     * @param id      单条数据id
     * @param bankAcc 账号
     * @param curId   币别
     * @param balance 单笔支付金额
     * @param temp    是否暂存数据，1新增数据校验，0，修改数据校验，空，批量提交校验
     * @return 0, 未进行限额; 1,单笔支付限额; 2,日累计限额; 3,月累计限额
     */
//    int bankAccLimitCheck(String id, String bankAcc, String curId, BigDecimal balance, String temp, String wishPayDate);
    /**
     * 单位限额校验d
     *
     * @param id      单条数据id
     * @param corpId  单位
     * @param curId   币别
     * @param balance 单笔支付金额
     * @param temp    是否暂存数据，1新增数据校验，0，修改数据校验，空，批量提交校验
     * @return 0, 未进行限额; 1,单笔支付限额; 2,日累计限额; 3,月累计限额
     */
//    int corpLimitCheck(String id, String corpId, String curId, BigDecimal balance, String temp, String wishPayDate);
    /**
     * 根据用户权限获取有效的银行账号
     *
     * @param userId
     * @param isDomesticBank 是否境内银行 1-境内，0-境外
     * @return
     */
    List<BtBankAccDTO> getBankAccByUserId(String userId, String isDomesticBank);
    /**
     * 根据用户权限获取有效的银行账号
     *
     * @param userId  用户id
     * @param corpId  单位id
     * @param curId   币别
     * @param accType 账户类别 可多选用,隔开
     * @return
     */
    List<BtBankAccDTO> getBankAccByUserIdCurId(String userId, String corpId, String curId, String accType);
    /**
     * 根据单位集合 得到银行账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 银行账号集合
     */
    List<BtBankAccDTO> getBankAccByCorpIdList(List<String> corpIdList, String joinSql, Map<String, Object> params);
    /**
     * 根据单位集合 得到银行账户列表（不包含账户余额）
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 银行账号集合
     */
    List<BtBankAccDTO> getBankAccNotUnionByCorpIdList(List<String> corpIdList, String joinSql, Map<String,Object> params);
    /**
     * 获取所有父账号集合
     *
     * @return
     */
    List<BtBankAccDTO> getAllParentBankAcc();
    /**
     * 根据单位集合 得到银行账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 银行账号集合
     */
    List<String> getBankAccIdsByCorpIdList(List<String> corpIdList, String joinSql, Map<String,Object> params);
    /**
     * 根据单位集合 得到银行账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 银行账号集合
     */
    List<String> getBankAccsByCorpIdList(List<String> corpIdList, String joinSql, Map<String,Object> params);
    /**
     * 根据银行账户查找账户信息
     *
     * @param bankAcc 账号
     * @return 银行账号
     */
    BtBankAccDTO getBankAccInfoByBankAcc(String bankAcc);
    List<BtBankAccDTO> getBankAccByBankAccIds(List<String> bankAccIds);
    /**
     * 根据账号集合查询账户信息，包括省市信息
     *
     * @param bankAccs 账号集合
     * @return 账户信息
     */
    Map<String, BtBankAccDTO> getBankAccByInfoAccs(List<String> bankAccs);

    /**
     * 根据账号集合查询账户信息，包括省市信息
     *
     * @param bankAccs 账号集合
     * @return 账户信息
     */
     Map<String, BtBankAccDTO> getBankAccMapByAccsAndCurId(List<String> bankAccs,DataTablesInput input);

    Map<String, BtBankAccDTO> getBankAccMapByAccsAndCurId(List<String> bankAccs);
    /**
     * 根据账号id集合查询账户信息，包括省市信息
     *
     * @param ids 账号id集合
     * @return 账户信息
     */
    Map<String, BtBankAccDTO> getBankAccUnByIds(List<String> ids);
    List<BtBankAccDTO> getBankAccByInfoAccsList(List<String> bankAccs);
    /**
     * 根据账户ID集合获取账号信息
     *
     * @param idList
     * @return
     */
    List<String> getBankAccByIds(List<String> idList);
    BtBankAccDTO getBtBankAcc(String id);
    boolean isBtBankAccCountLicense();
//    void toModify(BtBankAcc btBankAcc, ICurrentUser user);
    /**
     * 根据Id获取账号
     *
     * @param btBankAcc 账号VO
     * @return
     */
    List<BtBankAccVO> selectBankAccView(BtBankAccVO btBankAcc);
    List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId);
    /**
     * 打回
     *
     * @param id
     */
    void back(String id, ICurrentUser user);
    void deleteEntity(BtBankAccDTO entity);
    /**
     * 根据单位id得到银行账号列表
     *
     * @param corpId
     * @return
     */
    List<BtBankAccDTO> getListByCorpId(String corpId);
    /**
     * 获取实体账号
     *
     * @param btBankAccApp
     * @return
     */
    List<BtBankAccDTO> getEntityAcc(BtBankAccAppDTO btBankAccApp);
    DataTablesOutput dataTableOldBankAcc(DataTablesInput dataTablesInput, ICurrentUser user);
    /**
     * 根据单位、账号类型、权限，查询帐号列表
     * zuoy 2017-07-13
     *
     * @param accountSearchDTO
     * @return
     */
    List<AccountDTO> listBy(AccountSearchDTO accountSearchDTO);
    /**
     * @param @param  accountSearchDTO
     * @param @return 参数
     * @return List<AccountDTO>    返回类型
     * @throws
     * @Title: getRasBankListBy
     * @Description: TODO(增加稽查银行账户信息查询)
     * @author Jinyong.cao
     */
    List<AccountDTO> getRasBankListBy(AccountSearchDTO accountSearchDTO, String local);
    /**
     * 获取资源下的单位ID
     *
     * @param resource 资源
     * @return
     * @author zuoy 2017-10-10
     */
    List<String> getUserCorpIds(String resource, String userId);
    List<BtBankAccVO> getBankAccVOByCorpIdList(List<String> corpIdList, String joinSql, Map<String, Object> params);
    List<BtBankAccVO> getBankAccVOByuserIdCurIdWithSql(String corpId, String curId, String accType, String sql);
    List<BtBankAccVO> getBankAccVOByuserId(String userId, String isDomesticBank);
    int getBankAccCountByCorpId(String corpId, String busType);
    List<AccountDTO> copyBtBankAccDTOList(List<BtBankAccDTO> listBankAcc, AccountSearchDTO accountSearchDTO);
    List<BtBankAccVO> listByIds(String[] ids, String curId);
    IPageResult<BtBankAccDTO> getBankAccListByIsOnlineResult(IParameterInput parameterInput);
    List<String[]> getBankRasAccByAccountDTO(AccountSearchDTO accountSearchDTO);
    BtBankAccDTO getByBankAcc(String bankAcc);
    List<BtBankAccVO> listBankAccByEndDate(String searchDate);

    /**
     * 根据到期日期获取有效的银行账户
     * @param endDate
     * @return
     */
    List<BtBankAccDTO> listBankAccByDate(String endDate);
    DataTablesOutput getDataTableOldBankAcc(DataTablesInput dataTablesInput, String joinSql);
    List<BtBankAccDTO> listBtBankAccBriefInfoBy(String bankAcc, String accName);
    BtBankAccAppDTO getBtbankAccApp(String id);
    List<BtBankAccDTO> getBankAccNotUnionByIdList(BtBankAccDTO dto);
    List<BtBankAccDTO> getBankAccDTOList(BtBankAccDTO dto);
    List<BtBankAccVO> getBankAccVOByIdList(List<String> idList);
    DataTablesOutput getDataTablesUnion1(DataTablesInput dataTablesInput);
    List<BtBankAccVO> getListByVO(BtBankAccVO vo, StringBuilder corpSql);
    DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql);
    void delete(String id);
    void saveOrUpdateObj(BtBankAccDTO entity);
    List<BtBankAccBalVO> queryBankAccBalanceById(List<String> bankAccs, List<String> curIds);
    IPageResult<BtBankAccVO> getPageListByVO(IParameterInput parameterInput, com.bytter.system.api.vo.BtBankAccVO vo, ICurrentUser user);
    /**
     * 根据付款单号的账号id获取账号id
     *
     * @param accIds 账号id
     * @return 账号id集合
     */
    List<String> getBankAccByAccId(List<String> accIds);
    /**
     * 根据银行账号列表查询线上票据专户。
     *
     * @param bankAccList 银行账号。
     * @return 票据专户和流通方式。
     */
    List<Map<String, Object>> getBankModelByBankAcc(List<String> bankAccList);
    /**
     * 根据银行账号列表查询票据专户。
     *
     * @param bankAccList 银行账号。
     * @return 票据专户和流通方式。
     */
    Map<String, BtBankAccDTO> getBillBankAccByBankAccList(List<String> bankAccList);
    /**
     * <b>功能：</b>根据银行账号获取单位ID与组织机构代码<br>
     *
     * @param bankAcc 银行账号
     */
    List<Map<String, Object>> queryVAccListVo(String bankAcc);
    /**
     * <b>功能：</b>查询银行账号信息视图<br>
     *
     * @param bifCode 银行接口代码
     * @return 票据信息
     */
    List<VAccListVo> queryVAccListVoNew(String bifCode);
    /**
     * <b>功能：</b>查询银行账号,单位视图<br>
     *
     * @param bifCode 银行接口代码
     * @return 票据信息
     */
    List<VAccListVo> queryVAccListVoNewByCorp(String bifCode, List<String> poolCorpId);
    /**
     * <b>功能：</b>查询流水申请人与接收人账号是否是同一单位<br>
     *
     * @param id
     */
    boolean getReqAndRecCorpIdById(String id);
    BillBankBankAccVO getBankAccByAcctId(String acctId);
    DataTablesOutput getDataTablesUnion2(DataTablesInput dataTablesInput, String joinSql);
    /**
     * 获取跟银行账户关联的内部账号
     *
     * @param bankAcc 银行账号
     * @return 内部账号。
     */
    List<BtBankAccCurDTO> getCountertAcc(String bankAcc);
    /**
     * 根据账号id获取账号集合
     *
     * @param bankAccIds   账号id集合
     * @param oppAccNoList 账号集合
     * @return 账号信息集合
     */
    List<BtBankAccDTO> listByIds(List<String> bankAccIds, List<String> oppAccNoList);
    /**
     * 根据客商账号获取账号银行性质类型。
     *
     * @param accList 客商账号。
     * @return 账号银行性质类型。
     */
    List<String> getBankClassByExtAcc(List<String> accList);
    /**
     * 根据单位账号获取账号银行性质类型。
     *
     * @param accList 单位账号。
     * @return 账号银行性质类型。
     */
    List<String> getBankClassByBankAcc(List<String> accList);
    List<BtBankAccDTO> listBankAccByEnd(String corpId, String bankAcc, String bifCode, String curId,String joinSql);
    /**
     * 根据有权限的id集合获取账号数据
     *
     * @param corpAuth 有权限的单位id集合
     * @return 账号数据集合
     */
    List<BtBankAccDTO> listByFcBankAccDtl(List<String> corpAuth);
    /**
     * 通过sql语句查询账号，可以是关联查询语句
     * @param sql
     * @param params
     * @return
     */
    List<BtBankAccDTO> listBankAccBySql(String sql,Map<String, Object> params );

    /**
     * <b>功能介绍：</b>获取银行账号<br>
     * <b>日期：</b>2017年9月26日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param bankAcc 账号
     * @param netCode 网点
     * @param natureCode 账户性质
     * @return
     */
    List<Map<String,Object>> getBankAccByOpType(String bankAcc,String netCode,String natureCode);

    /**
     * 查询ERP在离线账号
     * @param dataTablesInput
     * @return
     */
    //TODO SYS服务提供
    DataTablesOutput getBankAccsErpData(DataTablesInput dataTablesInput,String userId,List<String> bankList);

    List<BtBankAccDTO> listBankAccByNillHead(List<String> downAccIds,List<String> curIds);

    List<BtBankAccDTO> batchSaveEntity(List<BtBankAccDTO> btBankAccDTOList);

    Map<String, BtBankAccDTO> getBankAccDTOByIds(List<String> ids);

    /**
     * 根据银行帐号获取帐户信息（仅银行信息，需要可添加相关字段）。
     *
     * @param bankAccList 银行帐号集合。
     * @return 帐户信息。
     */
    Map<String, BtBankAccDTO> getBankAccInfoByBankAccList(List<String> bankAccList);

    /**
     * 根据银行账号获取银行接口Code。
     *
     * @param bankAcc 银行账号。
     * @return 银行接口Code。
     */
    String getBifCodeByBankAcc(String bankAcc);

    /**
     * 根据银行账户组获取
     * @param accGroupCode
     * @return
     */
    List<BtBankAccDTO> getByGroupCode(String accGroupCode);

    /**
     * 根据账号id变更账号状态
     * @param ids
     * @return
     */
    int updateBankAccByIds(List<String> ids,Date cancelDate);

    void setBtCurrency(BtBankAccVO vo);
    int setCheckBankAccSave(String ids, String checkSign);

    void saveNisBindingBankac(String bankId,String bindingBankId);
    void deleteBybankId(String bankId);

    void deleteZhBailAccount(String bankAcc, ICurrentUser user);

    /**
     * 获取过期的保证金账户
     * @param curDate
     * @return
     */
    List<BtBankAccDTO> getExpireBailBankAcc(String curDate);

    /***
     * 获取账号用途为1004的占用数量
     * @param id
     * @param corpId
     * @return TODO
     */
    Integer getBankAccUse1004(String id, String corpId);

    /**
     * 根据单位获取有效的营收账号
     * @param corpIds
     * @return
     */
    List<BtBankAccDTO> listBankAcc1004(List<String> corpIds);

    void updateBankAccClose(String bankAcc, String yes);
}
