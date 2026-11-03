package com.bytter.system.api;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcAccPoolDTO;
import com.bytter.system.api.dto.FcFreezeDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 内部账号服务
 *
 * @author wangzx
 * @since 2019-08-26 18:49:44
 */
public interface IInnerAccountService {
    /**
     * 根据账号ID列表查询账号列表
     * 查询可N正常，P部分冻结 账号（支付账号）
     *
     * @param ids 账户id集合
     * @return 账户集合
     */
    List<FcAccDTO> listFcAccInfo(List<String> ids);
    /**
     * 根据账号ID列表查询账号列表
     * 查询可N正常，P部分冻结 账号（支付账号）
     *
     * @param ids 账户id集合
     * @return 账户集合
     */
    List<FcAccDTO> listFcAccInfo(List<String> ids, boolean containDelBankAcc);
    /**
     * 根据账号ID列表查询账号列表
     * 查询可N正常，P部分冻结，S只收不支 账号（收方账号）
     *
     * @param ids 账户id集合
     * @return 账户集合
     */
    List<FcAccDTO> listFcAccForReceiptsInfo(List<String> ids);
    /**
     * 根据账号ID列表查询账号列表
     * 查询可N正常，P部分冻结，D已注销的账号
     *
     * @param ids 账户id集合
     * @return 账户集合
     */
    List<FcAccDTO> listFcAccForHavDel(List<String> ids);
    /**
     * 根据账号ID列表,状态查询账号列表
     *
     * @param ids      账户id集合
     * @param accState 账户状态
     * @return 账户集合
     */
    List<FcAccDTO> listFcAccInfo(List<String> ids, List<String> accState);
    /**
     * 根据账号ID列表,状态查询账号列表(资金池)
     *
     * @param ids      账户id集合
     * @param accState 账户状态
     * @return 账户集合
     */
    List<FcAccDTO> listFcAccInfoForPool(List<String> ids, List<String> accState);
    /**
     * 根据单位ID列表查询账号列表
     *
     * @param corpIds  单位id集合
     * @param accState 账户状态 InnerAccountConstant.ACC_STATE_*
     * @return 账户集合
     */
    List<FcAccDTO> listFcAccInFoByCorpIds(List<String> corpIds, String accState);
    /**
     * 根据单位ID列表查询资金池账号列表
     *
     * @param corpIds  单位id集合
     * @param accState 账户状态InnerAccountConstant.ACC_STATE_*
     * @param poolSign 资金池标志
     * @return 账户集合
     */
    List<FcAccDTO> listFcAccPoolByCorpIds(List<String> corpIds, String accState, String poolSign);
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId     币别id
     * @param netId     网点id
     * @param corpId    单位id
     * @param subjectId 科目id
     * @return 账户集合
     */
    List<FcAccDTO> listAccountByParams(String curId, String netId, String corpId, String subjectId);
    /**
     * 根据参数筛选 有转账权限的内部账号
     *
     * @param curId          币别id
     * @param netId          网点id
     * @param corpId         单位id
     * @param subjectId      科目id
     * @param sysSubjectCode 系统科目编码
     * @param userId         用户id
     * @return 账户集合
     */
    List<FcAccDTO> listAccountByParamsAndGrant(String curId, String netId, String corpId, String subjectId, String sysSubjectCode, String userId);
    /**
     * 根据参数筛选内部账号(账号种类:企业活期存款／银行活期存款／内部账户,状态:为正常/部分冻结/只收不支）
     *
     * @param curId  币别id
     * @param netId  网点id
     * @param corpId 单位id
     * @return 账户集合
     */
    List<FcAccDTO> listAccountByParams(String curId, String netId, String corpId);
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId          币别id
     * @param netId          网点id
     * @param corpId         单位id
     * @param subjectId      科目id
     * @param sysSubjectCode 系统科目
     * @return 账户集合
     */
    List<FcAccDTO> listAccountByMoreParams(String curId, String netId, String corpId, String subjectId, String sysSubjectCode);
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId     币别id
     * @param netId     网点id
     * @param corpId    单位id
     * @param subjectId 科目id
     * @param bankCode  银行行号
     * @return 账户集合
     */
    List<FcAccDTO> listAccountByBankParams(String curId, String netId, String corpId, String subjectId, String bankCode, String userId);
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId     币别id
     * @param netId     网点id
     * @param corpId    单位id
     * @param subjectId 科目id
     * @param bankCode  银行行号
     * @param userId    用户id
     * @return 账户集合
     */
    List<FcAccPoolDTO> listAccountByBankParamsForPool(String curId, String netId, String corpId, String subjectId, String bankCode, String userId);
    /**
     * 根据参数筛选 内部账号
     *
     * @param curId     币别id
     * @param netId     网点id
     * @param corpId    单位id
     * @param subjectId 科目
     * @param endUnit   到期单位
     * @return 账户集合
     */
    List<FcAccDTO> listAccountByParams(String curId, String netId, String corpId, String subjectId, String endUnit);
    /**
     * 根据账号和调整类型 改变利率
     *
     * @param account    内部账户
     * @param instrate   利率
     * @param instrtType 调整类型
     */
    void updateRate(String account, BigDecimal instrate, String instrtType);
    /**
     * 自动开户内部账号[新增内部账号]
     *
     * @param fcAccDTO 账户DTO
     * @param user     操作用户
     * @return 内部账号
     */
    String saveOrUpdate(FcAccDTO fcAccDTO, ICurrentUser user);
    /**
     * 修改开户内部账号
     *
     * @param fcAccDTO 账户DTO
     */
    void updateFcAcc(FcAccDTO fcAccDTO);
    /**
     * 根据账号获取内部账号信息
     *
     * @param account 内部账户
     * @return 账户DTO
     */
    FcAccDTO getByBlnctrAcc(String account);
    /**
     * 自动销户
     * <p>
     * 1、复核余额不为零，为零提示
     * 2、写销户记录，写记账日期
     * 3、账户状态为销户D
     *
     * @param account   内部账户
     * @param tallyDate 记账日期
     */
    void cancelFcAcc(String account, Date tallyDate, ICurrentUser user);
    /**
     * 查询网点冻结 到期账户
     *
     * @param netId   网点id
     * @param endDate 到期日期
     */
    List<FcFreezeDTO> getList(String netId, Date endDate);
    /**
     * 根据科目 判断是否存在使用的账号
     *
     * @param subujectId 科目id
     * @return true 存在
     */
    boolean isExsits(String subujectId);
    /**
     * 根据单位集合 得到内部账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    List<FcAccDTO> getFcAccByCorpIdList(List<String> corpIdList, String joinSql);
    /**
     * 根据单位集合 得到内部账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    List<FcAccDTO> getFcAccByCorpIdList(List<String> corpIdList, String joinSql, boolean containDelBankAcc);
    /**
     * 根据单位集合 得到内部账户列表包含注销账号
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    List<FcAccDTO> getFcAccByCorpIdListContainDel(List<String> corpIdList, String joinSql);
    /**
     * 根据单位集合 得到内部账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    List<String> getFcAccIdsByCorpIdList(List<String> corpIdList, String joinSql);
    /**
     * 根据单位集合 得到内部账户列表
     *
     * @param corpIdList 单位ID集合
     * @param joinSql    拼接其他查询条件
     * @return 内部账号集合
     */
    List<String> getFcAccsByCorpIdList(List<String> corpIdList, String joinSql);
}
