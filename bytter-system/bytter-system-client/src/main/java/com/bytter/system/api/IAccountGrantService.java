package com.bytter.system.api;

import com.bytter.system.api.dto.AccountDTO;
import com.bytter.system.api.dto.AccountGrantDTO;
import com.bytter.system.api.dto.AccountSearchDTO;

import java.util.List;
import java.util.Map;

/**
 * 账号授权对外接口
 *
 * @author huangx
 * @date 2017/5/25
 */
public interface IAccountGrantService {
    /**
     * @param corpId      单位id
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     */
    List listBy(String corpId, String accountType, String permission);
    /**
     * @param corpId      单位id
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     */
    List listNotUnionBy(String corpId, String accountType, String permission);
    /**
     * @param corpIds     单位ids
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     */
    List listByMultiVal(List<String> corpIds, String accountType, String permission);
    /**
     * param userId     用户id
     *
     * @param corpIds     单位ids
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     */
    List listByMultiVal(String userId, List<String> corpIds, String accountType, String permission);
    /**
     * param userId     用户id
     *
     * @param corpIds    单位ids
     * @param permission d 详情，b 余额查询，t 转账
     * @param isHavDel   是否包含
     * @return
     */
    List listInnerByMultiVal(String userId, List<String> corpIds, String permission, Boolean isHavDel);
    /**
     * 列出内部账号
     *
     * @param corpId     单位id
     * @param permission d 详情，b 余额查询，t 转账
     * @return
     */
    List listInnerAccount(String corpId, String permission);
    /**
     * 列出内部账号
     *
     * @param userId     用户id
     * @param corpId     单位id
     * @param permission d 详情，b 余额查询，t 转账
     * @return
     */
    List listInnerAccount(String userId, String corpId, String permission);
    /**
     * 列出在线的银行账号
     *
     * @param corpId     单位id
     * @param permission d 详情，b 余额查询，t 转账
     * @return
     */
    List listOnlineBankAccount(String corpId, String permission);
    /**
     * 列出离线的银行账号
     *
     * @param corpId     单位id
     * @param permission d 详情，b 余额查询，t 转账
     * @return List
     */
    List listOfflineBankAccount(String corpId, String permission);
    /**
     * 根据条件查询账户信息
     *
     * @param accountSearchDTO 查询条件对象
     * @return List<AccountDTO>
     * @author zuoy
     */
    List<AccountDTO> listBy(AccountSearchDTO accountSearchDTO);
    /**
     * 根据条件查询账户信息
     *
     * @param userId      用户ID
     * @param resource    资源权限
     * @param curIds      币别ID
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     * @author zuoy 2017-10-10
     */
    List<String> listByMultiVal(String userId, String resource, List<String> curIds, String accountType, String permission);
    /**
     * 根据条件查询账户对象
     *
     * @param userId      用户ID
     * @param resource    资源权限
     * @param curIds      币别ID
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     * @author zuoy 2017-10-10
     */
    List listDTOByMultiVal(String userId, String resource, List<String> curIds, String accountType, String permission);
    /**
     * 根据条件查询账户信息
     *
     * @param userId      用户ID
     * @param corpId      单位ID
     * @param resource    资源权限
     * @param curIds      币别ID
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     * @author zuoy 2018-04-09
     */
    List<String> listByMultiVal(String userId, String corpId, String resource, List<String> curIds, String accountType, String permission);
    /**
     * 根据条件查询账户对象
     *
     * @param userId      用户ID
     * @param corpId      单位ID
     * @param resource    资源权限
     * @param curIds      币别ID
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     * @author zuoy 2017-10-10
     */
    List listDTOByMultiVal(String userId, String corpId, String resource, List<String> curIds, String accountType, String permission);
    /**
     * 根据条件查询账户对象
     *
     * @param userId           用户ID
     * @param corpId           单位ID
     * @param resource         资源权限
     * @param curIds           币别ID
     * @param accountType      1内部账号，2银行账号，3离线账号
     * @param permission       d 详情，b 余额查询，t 转账
     * @param isUseAccountCorp 是否启用所用账号单位
     * @return
     * @author zhenghj 2022-05-13
     */
    List listDTOByMultiVal(String userId, String corpId, String resource, List<String> curIds, String accountType, String permission, boolean isUseAccountCorp);
    /**
     * 根据条件查询账户对象
     *
     * @param userId      用户ID
     * @param corpId      单位ID
     * @param resource    资源权限
     * @param curIds      币别ID
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     * @author zuoy 2017-10-10
     */
    List listDTONotUnionByMultiVal(String userId, String corpId, String resource, List<String> curIds, String accountType, String permission);
    List listDTONotUnionByMultiVal(String userId, String corpId, String resource,String accountType, String permission, Map params);
    /**
     * 检测用户是否有账号权限
     *
     * @param userId      用户ID
     * @param account     账号
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     * @author zuoy 2019-07-05
     */
    Boolean checkPermissionByUserAndAccount(String userId, String account, String accountType, String permission, String resource);
    /**
     * 更改账号的类型
     *
     * @param accountId   账号ID
     * @param accountType 更改类型 1内部账号，2银行账号，3离线账号
     */
    void updateAccountTypeByAccount(String accountId, String accountType);
    /**
     * 保存账号授权方法
     *
     * @param accountGrantDTO 账号授权对象
     */
    void saveAccountGrant(AccountGrantDTO accountGrantDTO);
    /**
     * 根据条件查询账户信息
     *
     * @param userId      用户ID
     * @param resource    资源权限
     * @param curIds      币别ID
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     * @author zuoy 2017-10-10
     */
    List<String> listByMultiValAccIds(String userId, String resource, List<String> curIds, String accountType, String permission);
    /**
     * 根据条件查询账户信息
     *
     * @param userId      用户ID
     * @param resource    资源权限
     * @param curIds      币别ID
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     * @author zuoy 2017-10-10
     */
    List<String> listByMultiValAccs(String userId, String resource, List<String> curIds, String accountType, String permission);

    List listDTOByMultiValAccId(String userId, String resource, List<String> curIds, String accountType, String permission,String accId);
}
