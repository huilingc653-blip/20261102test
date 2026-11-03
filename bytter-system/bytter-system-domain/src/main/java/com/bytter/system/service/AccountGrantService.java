package com.bytter.system.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.AccountDTO;
import com.bytter.system.api.dto.AccountSearchDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.enums.AccountGrantMessageEnum;
import com.bytter.system.dao.AccountGrantDao;
import com.bytter.system.dao.CorpDao;
import com.bytter.system.dao.SysAccountGrantHisDao;
import com.bytter.system.entity.AccountGrant;
import com.bytter.system.entity.Corp;
import com.bytter.system.entity.SysAccountGrantHis;
import com.bytter.system.inneraccount.service.InnerAccountService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhoul on 2017/4/12.
 */
@Transactional
@Service("accountGrantService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountGrantService extends BaseService<AccountGrant, String> {
    @Autowired
    private CorpDao corpDao;
    @Autowired
    private AccountGrantDao dao;
    @Autowired
    private IBtBankAccService btBankAccServiceI;
    @Autowired(required = false)
    private InnerAccountService innerAccountService;
    @Autowired
    private SysAccountGrantHisDao sysAccountGrantHisDao;
    @Autowired
    private IConfigService configServiceI;

    @Override
    public BaseDao<AccountGrant, String> getEntityDao() {
        return dao;
    }

    public void save(List<AccountGrant> accountGrantList, String userId) {
        for (AccountGrant accountGrant : accountGrantList) {
//            saveSysAccountGrantHis(accountGrant, userId);
            dao.deleteByAccountIdAndUserId(accountGrant);
            //update by zhoul 2018-12-05
            //有账号权限的将数据写入表中、减少账号授权表中的冗余数据
            if (accountGrant.getCanQueryBalance() == 1 || accountGrant.getCanQueryDetail() == 1 || accountGrant.getCanTransferAccounts() == 1) {
                saveEntity(accountGrant);
            }
        }
    }

    public List<AccountGrant> listAssigned(String accountId, String accountType, String userId) {
        return dao.listAssigned(accountId, accountType, userId);
    }

    public AccountGrant getAccountGrant(String accountId, Integer accountType, String userId, String corpId) {
        return dao.getAccountGrant(accountId, accountType, userId, corpId);
    }

    public void delete(AccountGrant accountGrant) {
        dao.delete(accountGrant);
    }

    public Integer transferBoolean(Object data) {
        if (StringUtils.isNotEmpty(data)) {
            return (Integer) data;
        } else {
            return 3;
        }
    }

    /**
     * 保存账号权限历史变更明细
     *
     * @param accountGrant
     * @param userId
     */
    public void saveSysAccountGrantHis(AccountGrant accountGrant, String userId) {
        //查询变更前的账号权限
        AccountGrant acc = getAccountGrant(accountGrant.getAccountId(), accountGrant.getAccountType(), accountGrant.getUserId(), accountGrant.getCorpId());
        Integer canQueryDetail = null;
        Integer canQueryBalance = null;
        Integer canTransferAccounts = null;
        if (StringUtils.isNotEmpty(acc)) {
            canQueryDetail = acc.getCanQueryDetail();
            canQueryBalance = acc.getCanQueryBalance();
            canTransferAccounts = acc.getCanTransferAccounts();
        } else {
            acc = new AccountGrant();
            acc.setAccountId(accountGrant.getAccountId());
            acc.setAccountType(accountGrant.getAccountType());
            acc.setUserId(accountGrant.getUserId());
            acc.setCorpId(accountGrant.getCorpId());
            acc.setCanQueryDetail(null);
            acc.setCanQueryBalance(null);
            acc.setCanTransferAccounts(null);
        }
        //如果变更前后权限一样则不记录，否则就记录，并且记录的是变更前的权限
        if (transferBoolean(canQueryDetail).equals(transferBoolean(accountGrant.getCanQueryDetail()))
                && transferBoolean(canQueryBalance).equals(transferBoolean(accountGrant.getCanQueryBalance()))
                && transferBoolean(canTransferAccounts).equals(transferBoolean(accountGrant.getCanTransferAccounts()))) {
        } else {
            SysAccountGrantHis sysHis = new SysAccountGrantHis(acc.getAccountId(), acc.getAccountType(), acc.getUserId(), acc.getCorpId(),
                    acc.getCanQueryDetail(), acc.getCanQueryBalance(), acc.getCanTransferAccounts(), new Date(), userId);
            sysAccountGrantHisDao.save(sysHis);
        }
    }

    /**
     * 根据单位、账号类型、权限，查询帐号列表
     *
     * @param corpId
     * @param accountType
     * @param permission
     * @return
     */
    public List listBy(String corpId, String accountType, String permission) {
        return getListData(corpId, null, accountType, permission);
    }

    /**
     * 根据单位、账号类型、权限，查询帐号列表（不包含账号余额）
     *
     * @param corpId
     * @param accountType
     * @param permission
     * @return
     */
    public List listNotUnionBy(String corpId, String accountType, String permission) {
        return getListDataNotUnion(corpId, null, accountType, permission);
    }

    /**
     * 根据用户，单位、账号类型、权限，查询帐号列表
     *
     * @param userId
     * @param corpId
     * @param accountType
     * @param permission
     * @return
     */
    public List listBy(String userId, String corpId, String accountType, String permission) {
        return getListData(userId, corpId, null, accountType, permission, false);
    }

    /**
     * 根据多单位、账号类型、权限，查询帐号列表
     *
     * @param corpIds
     * @param accountType
     * @param permission
     * @return
     */
    public List listBy(List<String> corpIds, String accountType, String permission) {
        return getListData(null, corpIds, accountType, permission);
    }

    /**
     * 根据用户，多单位、账号类型、权限，查询帐号列表
     *
     * @param userId
     * @param corpIds
     * @param accountType
     * @param permission
     * @return
     */
    public List listBy(String userId, List<String> corpIds, String accountType, String permission, Boolean isHavDel) {
        return getListData(userId, null, corpIds, accountType, permission, isHavDel);
    }

    public List getListData(String corpId, List<String> corpIds, String accountType, String permission) {
        //校验参数是否正确
        if (StringUtils.isBlank(accountType) ||
                !(AccountGrantConstant.INNER_BANK_TYPE.equals(accountType) ||
                        AccountGrantConstant.ONLINE_BANK_TYPE.equals(accountType) ||
                        AccountGrantConstant.OFFLINE_BANK_TYPE.equals(accountType) ||
                        AccountGrantConstant.POOLINNER_BANK_TYPE.equals(accountType))) {
            throw new ParameterException(AccountGrantMessageEnum.SYS_ACCOUNT_TYPE_NOT_CORRECT);
        }
        List list = null;
        //权限控制类型 1-单位级 2-账号级
        String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (CommonConstant.YES.equals(controlType)) {
            List<String> corpIdList = null;
            if (StringUtils.isNotBlank(corpId)) {
                String[] corpArr = corpId.split(CommonConstant.COMMA_SPLIT);
                corpIdList = new ArrayList<>(Arrays.asList(corpArr));
            }
            if (EmptyUtils.isNotEmpty(corpIds)) {
                corpIdList = corpIds;
            }
            String joinSql = "";
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    // 仅查询内部账户
                    joinSql = " and t.pool_sign = '0' ";
                } else {
                    // 仅查询内部资金池账户
                    joinSql = " and t.pool_sign = '1' ";
                }
                list = innerAccountService.getFcAccByCorpIdList(corpIdList, joinSql);
            } else {
                if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.ONLINE_BANK_TYPE)) {
                    // 仅查询在线账户
                    joinSql = " and acc.is_online = 1 ";
                } else if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                    // 仅查询离线账户
                    joinSql = " and acc.is_online = 0 ";
                }
                list = btBankAccServiceI.getBankAccByCorpIdList(corpIdList, joinSql, new HashMap<>());
            }
        } else {
            List<String> accountGrantList = null;
            if (StringUtils.isNotBlank(corpId)) {
                accountGrantList = dao.listBy(corpId, accountType, permission);
            }
            if (EmptyUtils.isNotEmpty(corpIds)) {
                accountGrantList = dao.listBy(corpIds, accountType, permission);
            }
            if (EmptyUtils.isEmptyList(accountGrantList)) {
                return list;
            }
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                list = innerAccountService.listFcAccInfo(accountGrantList);
            } else if (AccountGrantConstant.ONLINE_BANK_TYPE.equals(accountType) ||
                    AccountGrantConstant.OFFLINE_BANK_TYPE.equals(accountType)) {
                list = btBankAccServiceI.getBankAccByIdList(accountGrantList);
            }
        }
        return list;
    }

    public List getListDataNotUnion(String corpId, List<String> corpIds, String accountType, String permission) {
        //校验参数是否正确
        if (StringUtils.isBlank(accountType) ||
                !(AccountGrantConstant.INNER_BANK_TYPE.equals(accountType) ||
                        AccountGrantConstant.ONLINE_BANK_TYPE.equals(accountType) ||
                        AccountGrantConstant.OFFLINE_BANK_TYPE.equals(accountType) ||
                        AccountGrantConstant.POOLINNER_BANK_TYPE.equals(accountType))) {
            throw new ParameterException(AccountGrantMessageEnum.SYS_ACCOUNT_TYPE_NOT_CORRECT);
        }
        List list = null;
        //权限控制类型 1-单位级 2-账号级
        String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (CommonConstant.YES.equals(controlType)) {
            List<String> corpIdList = null;
            if (StringUtils.isNotBlank(corpId)) {
                String[] corpArr = corpId.split(CommonConstant.COMMA_SPLIT);
                corpIdList = new ArrayList<>(Arrays.asList(corpArr));
            }
            if (EmptyUtils.isNotEmpty(corpIds)) {
                corpIdList = corpIds;
            }
            String joinSql = "";
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    // 仅查询内部账户
                    joinSql = " and t.pool_sign = '0' ";
                } else {
                    // 仅查询内部资金池账户
                    joinSql = " and t.pool_sign = '1' ";
                }
                list = innerAccountService.getFcAccByCorpIdList(corpIdList, joinSql);
            } else {
                if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.ONLINE_BANK_TYPE)) {
                    // 仅查询在线账户
                    joinSql = " and acc.is_online = 1 ";
                } else if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                    // 仅查询离线账户
                    joinSql = " and acc.is_online = 0 ";
                }
                list = btBankAccServiceI.getBankAccNotUnionByCorpIdList(corpIdList, joinSql, new HashMap<>());
            }
        } else {
            List<String> accountGrantList = null;
            if (StringUtils.isNotBlank(corpId)) {
                accountGrantList = dao.listBy(corpId, accountType, permission);
            }
            if (EmptyUtils.isNotEmpty(corpIds)) {
                accountGrantList = dao.listBy(corpIds, accountType, permission);
            }
            if (EmptyUtils.isEmptyList(accountGrantList)) {
                return list;
            }
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                list = innerAccountService.listFcAccInfo(accountGrantList);
            } else if (AccountGrantConstant.ONLINE_BANK_TYPE.equals(accountType) ||
                    AccountGrantConstant.OFFLINE_BANK_TYPE.equals(accountType)) {
                list = btBankAccServiceI.getBankAccNotUnionByIdList(accountGrantList);
            }
        }
        return list;
    }

    public List getListData(String userId, String corpId, List<String> corpIds, String accountType, String permission, Boolean isHavDel) {
        //校验参数是否正确
        if (StringUtils.isBlank(accountType) ||
                !(AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType) ||
                        accountType.contains(AccountGrantConstant.ONLINE_BANK_TYPE) ||
                        accountType.contains(AccountGrantConstant.OFFLINE_BANK_TYPE) ||
                        AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType))) {
            throw new ParameterException(AccountGrantMessageEnum.SYS_ACCOUNT_TYPE_NOT_CORRECT);
        }
        List list = null;
        //权限控制类型 1-单位级 2-账号级
        String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (CommonConstant.YES.equals(controlType)) {
            List<String> corpIdList = null;
            if (StringUtils.isNotBlank(corpId)) {
                String[] corpArr = corpId.split(CommonConstant.COMMA_SPLIT);
                corpIdList = new ArrayList<>(Arrays.asList(corpArr));
            }
            if (EmptyUtils.isNotEmpty(corpIds)) {
                corpIdList = corpIds;
            }
            String joinSql = "";
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    // 仅查询内部账户
                    joinSql = " and t.pool_sign = '0' ";
                } else {
                    // 仅查询内部资金池账户
                    joinSql = " and t.pool_sign = '1' ";
                }
                if (isHavDel) {
                    list = innerAccountService.getFcAccByCorpIdListContainDel(corpIdList, joinSql);
                } else {
                    list = innerAccountService.getFcAccByCorpIdList(corpIdList, joinSql);
                }
            } else {
                if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.ONLINE_BANK_TYPE)) {
                    // 仅查询在线账户
                    joinSql = " and acc.is_online = 1 ";
                } else if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                    // 仅查询离线账户
                    joinSql = " and acc.is_online = 0 ";
                }
                list = btBankAccServiceI.getBankAccByCorpIdList(corpIdList, joinSql, new HashMap<>());
            }
        } else {
            List<String> accountGrantList = null;
            if (StringUtils.isNotBlank(corpId)) {
                accountGrantList = dao.listBy(userId, corpId, accountType, permission);
            }
            if (EmptyUtils.isNotEmpty(corpIds)) {
                accountGrantList = dao.listBy(userId, corpIds, accountType, permission);
            }
            if (EmptyUtils.isEmptyList(accountGrantList)) {
                return list;
            }
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                if (isHavDel) {
                    list = innerAccountService.listFcAccForHavDel(accountGrantList);
                } else {
                    list = innerAccountService.listFcAccInfo(accountGrantList);
                }
            } else if (accountType.contains(AccountGrantConstant.ONLINE_BANK_TYPE) ||
                    accountType.contains(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                list = btBankAccServiceI.getBankAccByIdList(accountGrantList);
            }
        }
        return list;
    }

    /**
     * 根据单位、账号类型、权限，查询帐号列表
     * zuoy 2017-07-13
     *
     * @param accountSearchDTO
     * @return
     */
    public List<AccountDTO> listBy(AccountSearchDTO accountSearchDTO) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //logger.info("开始执行AccountGrantService.getListBy()方法。所有请求参数：{}", gson.toJson(accountSearchDTO));
        String accountType = accountSearchDTO.getAccountType();
        //校验参数是否正确
        if (EmptyUtils.isBlank(accountType)) {
            throw new ParameterException(AccountGrantMessageEnum.SYS_ACCOUNT_TYPE_NOT_CORRECT);
        }
        String corpId = accountSearchDTO.getCorpId();
        List<AccountDTO> list = new ArrayList<AccountDTO>();
        //单位需要必填且单位为空的情况下，直接不查询账号
        if (CommonConstant.YES.equals(accountSearchDTO.getIsMustCorp()) && StringUtils.isBlank(accountSearchDTO.getCorpId())) {
            return list;
        }
        try {
            // 获取资源下的单位ID
            List<String> userCorpIds = new ArrayList<String>();
            if (!EmptyUtils.isBlank(corpId)) {
                userCorpIds.addAll(Arrays.asList(corpId.split(CommonConstant.COMMA_SPLIT)));
            } else {
                if (!EmptyUtils.isBlank(accountSearchDTO.getResource())) {
                    userCorpIds = this.getUserCorpIds(accountSearchDTO.getResource(), accountSearchDTO.getUserId());
                }
            }
            //logger.info("AccountGrantService.getListBy()：有权限资源的用户单位集合：{}", gson.toJson(userCorpIds));
            //权限控制类型  1-单位级 2-账号级
            String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
            //logger.info("AccountGrantService.getListBy()：权限控制类型：{}（1-单位级，2-账号级）。", controlType);
            if (CommonConstant.YES.equals(controlType)) {
                String joinSql = "";
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                        || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                        // 仅查询内部账户
                        joinSql = " and t.pool_sign = '0' ";
                    } else {
                        // 仅查询内部资金池账户
                        joinSql = " and t.pool_sign = '1' ";
                    }
                    //logger.info("AccountGrantService.getListBy()：执行方法名：getFcAccByCorpIdList()，行数标记：447");
                    List<FcAccDTO> listFcAcc = innerAccountService.getFcAccByCorpIdList(userCorpIds, joinSql, accountSearchDTO.getContainDelBankAcc());
                    if (!EmptyUtils.isEmptyList(listFcAcc)) {
                        list = copyFcAccDTOList(listFcAcc);
                    }
                } else {
                    accountSearchDTO.setCorpGrantList(userCorpIds);
                    //logger.info("AccountGrantService.getListBy()：执行方法名：getBankAccByAccountDTO()，行数标记：454");
                    List<BtBankAccDTO> listBankAcc = btBankAccServiceI.getBankAccByAccountDTO(accountSearchDTO);
                    if (!EmptyUtils.isEmptyList(listBankAcc)) {
                        list = copyBtBankAccDTOList(listBankAcc, accountSearchDTO);
                    }
                }
            } else {
                // 查询单位权限下的账户
                List<String> accountGrantList = null;
                // 账号为空时，是否忽略授权
                boolean ignorePermission = EmptyUtils.isBlank(accountSearchDTO.getPermission()) && accountSearchDTO.getIgnorePermission();
                if (!ignorePermission) {
                    accountGrantList = this.dao.listBy(accountSearchDTO.getUserId(), userCorpIds, accountType, accountSearchDTO.getPermission());
                }
                if (EmptyUtils.isEmptyList(accountGrantList) && !ignorePermission) {
                    return list;
                }
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                        || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    //logger.info("AccountGrantService.getListBy()：执行方法名：listFcAccInfo()，行数标记：473");
                    List<FcAccDTO> listFcAcc = innerAccountService.listFcAccInfo(accountGrantList, accountSearchDTO.getContainDelBankAcc());
                    if (!EmptyUtils.isEmptyList(listFcAcc)) {
                        list = copyFcAccDTOList(listFcAcc);
                    }
                } else {
                    accountSearchDTO.setAccountGrantList(accountGrantList);
                    if (accountSearchDTO.getContainDelBankAcc() == null) {
                        accountSearchDTO.setContainDelBankAcc(true);
                    }
                    //logger.info("AccountGrantService.getListBy()：执行方法名：getBankAccByAccountDTO()，行数标记：483");
                    List<BtBankAccDTO> listBankAcc = btBankAccServiceI.getBankAccByAccountDTO(accountSearchDTO);
                    if (!EmptyUtils.isEmptyList(listBankAcc)) {
                        list = copyBtBankAccDTOList(listBankAcc, accountSearchDTO);
                    }
//                    list = getBankAccByAccountDTO(accountSearchDTO);
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
            throw e;
        }
        //logger.info("结束执行AccountGrantService.getListBy()方法，返回账号集合：{}", gson.toJson(list));
        return list;
    }

    public List<AccountDTO> getBankAccByAccountDTO(AccountSearchDTO accountSearchDTO) {
        JSONArray accountJson = JSONUtil.readJSONArray(new File("D:\\bytter\\file\\accountList.json"), StandardCharsets.UTF_8);
        List<AccountDTO> accountList = accountJson.toList(AccountDTO.class);
        return accountList.stream().filter(acc -> StrUtil.equals(acc.getCorpId(), accountSearchDTO.getCorpId())).collect(Collectors.toList());
    }

    /**
     * 将BtBankAccDTO集合cope到AccountDTO集合
     * zuoy 2017-07-13
     *
     * @param listBankAcc      银行账户集合
     * @param accountSearchDTO
     * @return
     */
    public List<AccountDTO> copyBtBankAccDTOList(List<BtBankAccDTO> listBankAcc, AccountSearchDTO accountSearchDTO) {
        List<AccountDTO> list = new ArrayList<>();
        Map<String, BtBankAccDTO> parentBankAccMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(accountSearchDTO.getContainParent()) && accountSearchDTO.getContainParent()) {
            //获取所有的父账号集合
            List<BtBankAccDTO> parentBankAccList = btBankAccServiceI.getAllParentBankAcc();
            for (BtBankAccDTO dto : parentBankAccList) {
                parentBankAccMap.put(dto.getId(), dto);
            }
        }
        for (BtBankAccDTO bankAcc : listBankAcc) {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setBankAccId(bankAcc.getId());
            accountDTO.setCorpId(bankAcc.getCorpId());
            accountDTO.setCorpName(bankAcc.getCorpName());
            accountDTO.setBankAcc(bankAcc.getBankAcc());
            accountDTO.setAccName(bankAcc.getAccName());
            accountDTO.setBankName(bankAcc.getBankName());
            accountDTO.setBankCode(bankAcc.getBankCode());
            accountDTO.setCurId(bankAcc.getCurId());
            accountDTO.setCurName(bankAcc.getCurName());
            accountDTO.setBifCode(bankAcc.getBifCode());
            accountDTO.setProv(bankAcc.getProv());
            accountDTO.setProvName(bankAcc.getProvName());
            accountDTO.setCity(bankAcc.getCity());
            accountDTO.setCityName(bankAcc.getCityName());
            accountDTO.setCirculationMode(bankAcc.getCirculationMode());
            accountDTO.setBalance(bankAcc.getBalance());
            accountDTO.setFcBalance(bankAcc.getFcBalance());
            accountDTO.setCurEnglishCode(bankAcc.getCurEnglishCode());
            accountDTO.setBankTypeId(bankAcc.getBankTypeId());
            accountDTO.setBankTypeName(bankAcc.getBankTypeName());
            // containParent参数为true时，加载父账户信息
            if (!EmptyUtils.isEmpty(accountSearchDTO.getContainParent()) && accountSearchDTO.getContainParent()) {
//                BtBankAccDTO parentBankAcc = btBankAccService.getParentAccByAccId(bankAcc.getId(), bankAcc.getCurId());
//                if (EmptyUtils.isEmpty(parentBankAcc)) {
//                	continue;
//				}
                BtBankAccDTO parentBankAcc = parentBankAccMap.get(bankAcc.getPrentAccId());
                if (!EmptyUtils.isEmpty(parentBankAcc)) {
                    AccountDTO parentAccountDTO = new AccountDTO();
                    parentAccountDTO.setBankAccId(parentBankAcc.getId());
                    parentAccountDTO.setCorpId(parentBankAcc.getCorpId());
                    parentAccountDTO.setCorpName(parentBankAcc.getCorpName());
                    parentAccountDTO.setBankAcc(parentBankAcc.getBankAcc());
                    parentAccountDTO.setAccName(parentBankAcc.getAccName());
                    parentAccountDTO.setBankName(parentBankAcc.getBankName());
                    parentAccountDTO.setBankCode(parentBankAcc.getBankCode());
                    parentAccountDTO.setCurId(parentBankAcc.getCurId());
                    parentAccountDTO.setCurName(parentBankAcc.getCurName());
                    parentAccountDTO.setBifCode(parentBankAcc.getBifCode());
                    parentAccountDTO.setProv(parentBankAcc.getProv());
                    parentAccountDTO.setProvName(parentBankAcc.getProvName());
                    parentAccountDTO.setCity(parentBankAcc.getCity());
                    parentAccountDTO.setCityName(parentBankAcc.getCityName());
                    parentAccountDTO.setCurEnglishCode(parentBankAcc.getCurEnglishCode());
                    parentAccountDTO.setBankTypeId(parentBankAcc.getBankTypeId());
                    parentAccountDTO.setBankTypeName(parentBankAcc.getBankTypeName());
//                    BigDecimal parentBalance = this.bisaccBalService.getLastByBalDate(parentBankAcc.getBankAcc(), parentBankAcc.getCurId());
                    parentAccountDTO.setBalance(EmptyUtils.isEmpty(parentBankAcc.getBalance()) ? BigDecimal.ZERO : parentBankAcc.getBalance());
                    accountDTO.setParentAccount(parentAccountDTO);
                }
            }
            list.add(accountDTO);
        }
        return list;
    }

    /**
     * 将FcAccDTO集合cope到AccountDTO集合
     * zuoy 2017-07-13
     *
     * @param listFcAcc 内部账户集合
     * @return
     */
    public List<AccountDTO> copyFcAccDTOList(List<FcAccDTO> listFcAcc) {
        List<AccountDTO> list = new ArrayList<AccountDTO>();
        for (FcAccDTO fcAcc : listFcAcc) {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setBankAccId(fcAcc.getId());
            accountDTO.setCorpId(fcAcc.getCorpId());
            accountDTO.setCorpName(fcAcc.getCorpName());
            accountDTO.setBankAcc(fcAcc.getBlnctrAcc());
            accountDTO.setAccName(fcAcc.getAccName());
            accountDTO.setBankName(fcAcc.getCorpName());
            accountDTO.setBankCode(fcAcc.getBankCode());
            accountDTO.setCurId(fcAcc.getCurId());
            accountDTO.setCurName(fcAcc.getCurName());
            accountDTO.setFcBalance(fcAcc.getFcBalance());
            list.add(accountDTO);
        }
        return list;
    }

    /**
     * 获取资源下的单位ID
     *
     * @param resource 资源
     * @return
     * @author zuoy 2017-10-10
     */
    private List<String> getUserCorpIdsV2(String resource, String userId) {
        // 获取资源下的单位ID
        List<String> userCorpIds = null;
        if (!EmptyUtils.isBlank(resource)) {
            userCorpIds = corpDao.getCorpIdsByUserResource(resource, userId);
        }
        return userCorpIds;
    }

    /**
     * 根据条件查询的账户信息
     *
     * @param userId      用户ID
     * @param resource    资源权限
     * @param curIds      币别ID
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     * @author zuoy 2017-10-10
     */
    public List<String> listByMultiVal(String userId, String resource, List<String> curIds, String accountType, String permission) {
        return this.dao.listBy(userId, getUserCorpIds(resource, userId), curIds, accountType, permission);
    }

    /**
     * 根据条件查询的账户信息
     *
     * @param userId      用户ID
     * @param resource    资源权限
     * @param curIds      币别ID
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     * @author zuoy 2017-10-10
     */
    public List<String> listByMultiValV2(String userId, String resource, List<String> curIds, String accountType, String permission) {
        return this.dao.listByV2(userId, getUserCorpIdsV2(resource, userId), curIds, accountType, permission);
    }

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
    public List listDTOByMultiVal(String userId, String resource, List<String> curIds, String accountType, String permission) {
        List list = null;
        //权限控制类型  1-单位级 2-账号级
        String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (CommonConstant.YES.equals(controlType)) {
            List<String> corpIdList = null;
            if (EmptyUtils.isNotEmpty(resource)) {
                corpIdList = getUserCorpIds(resource, userId);
            }
            String joinSql = "";
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    // 仅查询内部账户
                    joinSql = " and t.pool_sign = '0' ";
                } else {
                    // 仅查询内部资金池账户
                    joinSql = " and t.pool_sign = '1' ";
                }
                if (!EmptyUtils.isEmpty(curIds)) {
                    joinSql += " and " + SqlUtil.getInSql(curIds, "t.cur_id", true);
                }
                list = innerAccountService.getFcAccByCorpIdList(corpIdList, joinSql);
            } else {
                if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.ONLINE_BANK_TYPE)) {
                    // 仅查询在线账户
                    joinSql = " and acc.is_online = 1 ";
                } else if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                    // 仅查询离线账户
                    joinSql = " and acc.is_online = 0 ";
                }
                if (!EmptyUtils.isEmpty(curIds)) {
                    joinSql += " and " + SqlUtil.getInSql(curIds, "acccur.cur_id", true);
                }
                list = btBankAccServiceI.getBankAccByCorpIdList(corpIdList, joinSql, new HashMap<>());
            }
        } else {
            List<String> accountGrantList = this.listByMultiVal(userId, resource, curIds, accountType, permission);
            if (EmptyUtils.isEmptyList(accountGrantList)) {
                return list;
            }
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                list = innerAccountService.listFcAccInfo(accountGrantList);
            } else {
                list = btBankAccServiceI.getBankAccByIdList(accountGrantList);
            }
        }
        return list;
    }

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
    public List<String> listByMultiVal(String userId, String corpId, String resource, List<String> curIds, String accountType, String permission) {
        //返回 fc_acc 表 ID
        return this.dao.listBy(userId, corpId, getUserCorpIds(resource, userId), curIds, accountType, permission);
    }

    /**
     * 根据条件查询账户信息 （添加所用账号单位）
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
    public List<String> listByMultiValAndUseAccount(String userId, String corpId, String resource, List<String> curIds, String accountType, String permission) {
        return this.dao.listBy(userId, corpId, this.getUserCorpIdsByUseAccount(resource, userId), curIds, accountType, permission);
    }

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
     * @author zuoy 2018-04-09
     */
    public List listDTOByMultiVal(String userId, String corpId, String resource, List<String> curIds, String accountType, String permission) {
        return this.listDTOByMultiVal(userId, corpId, resource, curIds, accountType, permission, false);
    }

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
    public List listDTOByMultiVal(String userId, String resource, List<String> curIds, String accountType, String permission, String accId) {
        List list = null;
        //权限控制类型  1-单位级 2-账号级
        String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (CommonConstant.YES.equals(controlType)) {
            List<String> corpIdList = null;
            if (EmptyUtils.isNotEmpty(resource)) {
                corpIdList = getUserCorpIds(resource, userId);
            }
            String joinSql = "";
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    // 仅查询内部账户
                    joinSql = " and t.pool_sign = '0' ";
                } else {
                    // 仅查询内部资金池账户
                    joinSql = " and t.pool_sign = '1' ";
                }
                if (!EmptyUtils.isEmpty(curIds)) {
                    joinSql += " and " + SqlUtil.getInSql(curIds, "t.cur_id", true);
                }
                list = innerAccountService.getFcAccByCorpIdList(corpIdList, joinSql);
            } else {
                if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.ONLINE_BANK_TYPE)) {
                    // 仅查询在线账户
                    joinSql = " and acc.is_online = 1 ";
                } else if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                    // 仅查询离线账户
                    joinSql = " and acc.is_online = 0 ";
                }
                if (!EmptyUtils.isEmpty(curIds)) {
                    joinSql += " and " + SqlUtil.getInSql(curIds, "acccur.cur_id", true);
                }
                list = btBankAccServiceI.getBankAccByCorpIdList(corpIdList, joinSql, new HashMap<>());
            }
        } else {
            List<String> accountGrantList = this.listByMultiVal(userId, resource, curIds, accountType, permission);
            if (EmptyUtils.isEmptyList(accountGrantList)) {
                return list;
            }

            if (StringUtils.isNotEmpty(accId)) {
                if (accountGrantList.contains(accId)) {
                    accountGrantList.clear();
                    accountGrantList.add(accId);
                } else {
                    return list;
                }
            }

            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                list = innerAccountService.listFcAccInfo(accountGrantList);
            } else {
                list = btBankAccServiceI.getBankAccByIdList(accountGrantList);
            }
        }
        return list;
    }


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
     * @author zuoy 2018-04-09
     */
    public List listDTOByMultiVal(String userId, String corpId, String resource, List<String> curIds, String accountType, String permission, boolean isUseAccountCorp) {
        //权限控制类型  1-单位级 2-账号级
        String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (CommonConstant.YES.equals(controlType)) {
            List<String> corpIdList = null;
            if (StringUtils.isNotBlank(corpId)) {
                String[] corpArr = corpId.split(CommonConstant.COMMA_SPLIT);
                corpIdList = new ArrayList<>(Arrays.asList(corpArr));
            } else {
                if (EmptyUtils.isNotEmpty(resource)) {
                    corpIdList = getUserCorpIds(resource, userId);
                }
            }
            String joinSql = "";
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    // 仅查询内部账户
                    joinSql = " and t.pool_sign = '0' ";
                } else {
                    // 仅查询内部资金池账户
                    joinSql = " and t.pool_sign = '1' ";
                }
                if (!EmptyUtils.isEmpty(curIds)) {
                    joinSql += " and " + SqlUtil.getInSql(curIds, "t.cur_id", true);
                }
                return innerAccountService.getFcAccByCorpIdList(corpIdList, joinSql);
            } else {
                if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.ONLINE_BANK_TYPE)) {
                    // 仅查询在线账户
                    joinSql = " and acc.is_online = 1 ";
                } else if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                    // 仅查询离线账户
                    joinSql = " and acc.is_online = 0 ";
                }
                if (!EmptyUtils.isEmpty(curIds)) {
                    joinSql += " and " + SqlUtil.getInSql(curIds, "acccur.cur_id", true);
                }
                return btBankAccServiceI.getBankAccByCorpIdList(corpIdList, joinSql, new HashMap<>());
            }
        } else {
            List<String> accountGrantList = null;
            //当为账号级且查询所用账号单位时，则查询单位的所用账号单位的所属账号
            if (isUseAccountCorp) {
                accountGrantList = this.listByMultiValAndUseAccount(userId, corpId, resource, curIds, accountType, permission);
            } else {
                accountGrantList = this.listByMultiVal(userId, corpId, resource, curIds, accountType, permission);
            }
            if (EmptyUtils.isEmptyList(accountGrantList)) {
                return null;
            }
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                return innerAccountService.listFcAccInfo(accountGrantList);
            } else {
                return btBankAccServiceI.getBankAccByIdList(accountGrantList);
            }
        }
    }

    /***
     * 根据条件查询账户对象
     * @param userId 用户ID
     * @param corpId 单位ID
     * @param resource 资源权限
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission d 详情，b 余额查询，t 转账
     * @param params 其他参数
     * @return TODO
     */
    public List listDTONotUnionByMultiVal(String userId, String corpId, String resource, String accountType, String permission, Map params) {
        //权限控制类型  1-单位级 2-账号级
        String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (CommonConstant.YES.equals(controlType)) {
            List<String> corpIdList = null;
            if (StringUtils.isNotBlank(corpId)) {
                String[] corpArr = corpId.split(CommonConstant.COMMA_SPLIT);
                corpIdList = new ArrayList<>(Arrays.asList(corpArr));
            } else {
                if (EmptyUtils.isNotEmpty(resource)) {
                    corpIdList = getUserCorpIds(resource, userId);
                }
            }
            String joinSql = "";
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    // 仅查询内部账户
                    joinSql = " and t.pool_sign = '0' ";
                } else {
                    // 仅查询内部资金池账户
                    joinSql = " and t.pool_sign = '1' ";
                }
                return innerAccountService.getFcAccByCorpIdList(corpIdList, joinSql);
            } else {
                if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.ONLINE_BANK_TYPE)) {
                    // 仅查询在线账户
                    joinSql = " and acc.is_online = 1 ";
                } else if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                    // 仅查询离线账户
                    joinSql = " and acc.is_online = 0 ";
                }
                return btBankAccServiceI.getBankAccNotUnionByCorpIdList(corpIdList, joinSql, params);
            }
        } else {
            List<String> accountGrantList = this.listByMultiVal(userId, corpId, resource, null, accountType, permission);
            if (EmptyUtils.isEmptyList(accountGrantList)) {
                List<FcAccDTO> list = new ArrayList<>();
                return list;//返回空页面会显示加载中
            }
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                return innerAccountService.listFcAccInfo(accountGrantList);
            } else {
                return btBankAccServiceI.getBankAccNotUnionByIdList(accountGrantList);
            }
        }
    }

    public List listDTONotUnionByMultiVal(String userId, String corpId, String resource, List<String> curIds, String accountType, String permission) {
        //权限控制类型  1-单位级 2-账号级
        String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (CommonConstant.YES.equals(controlType)) {
            List<String> corpIdList = null;
            if (StringUtils.isNotBlank(corpId)) {
                String[] corpArr = corpId.split(CommonConstant.COMMA_SPLIT);
                corpIdList = new ArrayList<>(Arrays.asList(corpArr));
            } else {
                if (EmptyUtils.isNotEmpty(resource)) {
                    corpIdList = getUserCorpIds(resource, userId);
                }
            }
            String joinSql = "";
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    // 仅查询内部账户
                    joinSql = " and t.pool_sign = '0' ";
                } else {
                    // 仅查询内部资金池账户
                    joinSql = " and t.pool_sign = '1' ";
                }
                if (!EmptyUtils.isEmpty(curIds)) {
                    joinSql += " and " + SqlUtil.getInSql(curIds, "t.cur_id", true);
                }
                return innerAccountService.getFcAccByCorpIdList(corpIdList, joinSql);
            } else {
                if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.ONLINE_BANK_TYPE)) {
                    // 仅查询在线账户
                    joinSql = " and acc.is_online = 1 ";
                } else if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                    // 仅查询离线账户
                    joinSql = " and acc.is_online = 0 ";
                }
                if (!EmptyUtils.isEmpty(curIds)) {
                    joinSql += " and " + SqlUtil.getInSql(curIds, "acccur.cur_id", true);
                }
                return btBankAccServiceI.getBankAccNotUnionByCorpIdList(corpIdList, joinSql, new HashMap<>());
            }
        } else {
            List<String> accountGrantList = this.listByMultiVal(userId, corpId, resource, curIds, accountType, permission);
            if (EmptyUtils.isEmptyList(accountGrantList)) {
                List<FcAccDTO> list = new ArrayList<>();
                return list;//返回空页面会显示加载中
            }
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                return innerAccountService.listFcAccInfo(accountGrantList);
            } else {
                return btBankAccServiceI.getBankAccNotUnionByIdList(accountGrantList);
            }
        }
    }

    /**
     * 根据账号授权ID查找账号信息
     *
     * @param accountIds
     * @param userIds
     * @return
     * @Auhor zhoul
     */
    public List<AccountGrant> listByAccountIdAndUserIds(List<String> accountIds, List<String> userIds) {
        return dao.listByAccountIdsAndUserIds(accountIds, userIds);
    }

    /**
     * 验证当前授权账号是否删除
     *
     * @param accountGrant 授权账号
     * @return true页面做删除
     */
    public boolean isDeleteAction(AccountGrant accountGrant) {
        return accountGrant.getCanQueryBalance() == null && accountGrant.getCanQueryDetail() == null && accountGrant.getCanTransferAccounts() == null;
    }

    public Boolean checkPermissionByUserAndAccount(String userId, String account, String accountType, String permission, String resource) {
        //权限控制类型  1-单位级 2-账号级
        String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (CommonConstant.YES.equals(controlType)) {
            List<String> corpIdList = null;
            if (EmptyUtils.isNotEmpty(resource)) {
                corpIdList = getUserCorpIds(resource, userId);
            }
            StringBuilder joinSql = new StringBuilder();
            if (!EmptyUtils.isEmpty(corpIdList)) {
                joinSql.append(" and ").append(SqlUtil.getInSql(corpIdList, "acc.corp_id", true));
            }
            return this.dao.checkPermission(userId, account, accountType, permission, resource, joinSql.toString());
        } else {
            return this.dao.checkPermission(userId, account, accountType, permission, null, null);
        }
    }

    public void updateAccountTypeByAccount(String accountId, String accountType) {
        this.dao.updateAccountTypeByAccount(accountId, accountType);
    }

    public void saveAccountGrant(AccountGrant accountGrant) {
        dao.deleteByAccountIdAndUserId(accountGrant);
        dao.saveAccountGrant(accountGrant);
    }

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
    public List<String> listByMultiValAccIds(String userId, String resource, List<String> curIds, String accountType, String permission) {
        List<String> list = null;
        //权限控制类型  1-单位级 2-账号级
        String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (CommonConstant.YES.equals(controlType)) {
            List<String> corpIdList = null;
            if (EmptyUtils.isNotEmpty(resource)) {
                corpIdList = getUserCorpIds(resource, userId);
            }
            String joinSql = "";
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    // 仅查询内部账户
                    joinSql = " and t.pool_sign = '0' ";
                } else {
                    // 仅查询内部资金池账户
                    joinSql = " and t.pool_sign = '1' ";
                }
                if (!EmptyUtils.isEmpty(curIds)) {
                    joinSql += " and " + SqlUtil.getInSql(curIds, "t.cur_id", true);
                }
                list = innerAccountService.getFcAccIdsByCorpIdList(corpIdList, joinSql);
            } else {
                if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.ONLINE_BANK_TYPE)) {
                    // 仅查询在线账户
                    joinSql = " and acc.is_online = 1 ";
                } else if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                    // 仅查询离线账户
                    joinSql = " and acc.is_online = 0 ";
                }
                if (!EmptyUtils.isEmpty(curIds)) {
                    joinSql += " and " + SqlUtil.getInSql(curIds, "acccur.cur_id", true);
                }
                list = btBankAccServiceI.getBankAccIdsByCorpIdList(corpIdList, joinSql, new HashMap<>());
            }
        } else {
            list = this.listByMultiVal(userId, resource, curIds, accountType, permission);
        }
        return list;
    }

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
    public List<String> listByMultiValAccs(String userId, String resource, List<String> curIds, String accountType, String permission) {
        List<String> list = null;
        //权限控制类型  1-单位级 2-账号级
        String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (CommonConstant.YES.equals(controlType)) {
            List<String> corpIdList = null;
            if (EmptyUtils.isNotEmpty(resource)) {
                corpIdList = getUserCorpIds(resource, userId);
            }
            String joinSql = "";
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    // 仅查询内部账户
                    joinSql = " and t.pool_sign = '0' ";
                } else {
                    // 仅查询内部资金池账户
                    joinSql = " and t.pool_sign = '1' ";
                }
                if (!EmptyUtils.isEmpty(curIds)) {
                    joinSql += " and " + SqlUtil.getInSql(curIds, "t.cur_id", true);
                }
                list = innerAccountService.getFcAccsByCorpIdList(corpIdList, joinSql);
            } else {
                if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.ONLINE_BANK_TYPE)) {
                    // 仅查询在线账户
                    joinSql = " and acc.is_online = 1 ";
                } else if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                    // 仅查询离线账户
                    joinSql = " and acc.is_online = 0 ";
                }
                if (!EmptyUtils.isEmpty(curIds)) {
                    joinSql += " and " + SqlUtil.getInSql(curIds, "acccur.cur_id", true);
                }
                list = btBankAccServiceI.getBankAccsByCorpIdList(corpIdList, joinSql, new HashMap<>());
            }
        } else {
            list = this.listByMultiValV2(userId, resource, curIds, accountType, permission);
        }
        return list;
    }


    public List<String> getCorpIdsByUserResource(String resource, String userId) {
        return this.corpDao.getCorpIdsByUserResource(resource, userId);
    }

    public List<String> getUserCorpIds(String resource, String userId) {
        List<String> list = this.corpDao.getUserCorpIds(resource, userId);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return distinctList(list);
    }

    public List<String> distinctList(List<String> list) {
        HashSet<String> set = new HashSet<>(list);
        return new ArrayList<>(set);
    }

    public List<String> getUserCorpIdsByUseAccount(String resource, String userId) {
        // 获取资源下的单位ID
        List<String> userCorpIds = new ArrayList<>();
        if (!EmptyUtils.isBlank(resource)) {
            userCorpIds = this.getUserCorpIds(resource, userId);
            if (!EmptyUtils.isEmptyList(userCorpIds)) {
                // 获取单位下的所用账号单位
                List<String> userAccountCorpList = this.listUserAccountCorpByCorpId(userCorpIds);
                if (!EmptyUtils.isEmptyList(userAccountCorpList)) {
                    userCorpIds.addAll(userAccountCorpList);
                    //去重
                    HashSet h = new HashSet(userCorpIds);
                    userCorpIds.clear();
                    userCorpIds.addAll(h);
                }
            }
        }
        return userCorpIds;
    }

    public List<String> listUserAccountCorpByCorpId(List<String> corpIds) {
        if (EmptyUtils.isEmptyList(corpIds)) {
            return null;
        }
        List<String> corpAuth = new ArrayList<>();
        List<Corp> list = corpDao.listEffectiveCorp(corpIds);
        for (Corp corp : list) {
            if (StringUtils.isNotEmpty(corp.getUserAccountCode())) {
                corpAuth.addAll(Arrays.asList(corp.getUserAccountCode().split(CommonConstant.COMMA_SPLIT)));
            }
        }
        return corpAuth;
    }
}
