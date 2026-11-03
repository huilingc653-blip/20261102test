package com.bytter.system.service.impl;

import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.entity.AccountGrant;
import com.bytter.system.service.AccountGrantService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Created by huangx on 2017/5/25.
 */
@Service("accountGrantServiceI")
@DubboService
public class AccountGrantServiceImpl implements IAccountGrantService {
    @Autowired
    private AccountGrantService accountGrantService;
    @Autowired
    private IBtCurrencyService btCurrencyServiceI;
    @Override
    public List listBy(String corpId, String accountType, String permission) {
        return accountGrantService.listBy(corpId, accountType, permission);
    }
    @Override
    public List listNotUnionBy(String corpId, String accountType, String permission) {
        return accountGrantService.listNotUnionBy(corpId, accountType, permission);
    }
    public List listBy(String userId, String corpId, String accountType, String permission) {
        return accountGrantService.listBy(userId, corpId, accountType, permission);
    }
    @Override
    public List listByMultiVal(List<String> corpIds, String accountType, String permission) {
        return accountGrantService.listBy(corpIds, accountType, permission);
    }
    @Override
    public List listByMultiVal(String userId, List<String> corpIds, String accountType, String permission) {
        return accountGrantService.listBy(userId, corpIds, accountType, permission, false);
    }
    @Override
    public List listInnerByMultiVal(String userId, List<String> corpIds, String permission, Boolean isHavDel) {
        return accountGrantService.listBy(userId, corpIds, AccountGrantConstant.INNER_BANK_TYPE, permission, isHavDel);
    }
    @Override
    public List listInnerAccount(String corpId, String permission) {
        return listBy(corpId, AccountGrantConstant.INNER_BANK_TYPE, permission);
    }
    @Override
    public List listInnerAccount(String userId, String corpId, String permission) {
        return listBy(userId, corpId, AccountGrantConstant.INNER_BANK_TYPE, permission);
    }
    @Override
    public List listOnlineBankAccount(String corpId, String permission) {
        return listNotUnionBy(corpId, AccountGrantConstant.ONLINE_BANK_TYPE, permission);
    }
    @Override
    public List listOfflineBankAccount(String corpId, String permission) {
        return listNotUnionBy(corpId, AccountGrantConstant.OFFLINE_BANK_TYPE, permission);
    }
    @Override
    public List<AccountDTO> listBy(AccountSearchDTO accountSearchDTO) {
        return accountGrantService.listBy(accountSearchDTO);
    }
    @Override
    public List<String> listByMultiVal(String userId, String resource, List<String> curIds, String accountType, String permission) {
        List<String> accIds = new ArrayList<>();
        List temp = this.accountGrantService.listDTOByMultiVal(userId, resource, curIds, accountType, permission);
        if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                || AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
            List<FcAccDTO> list = temp;
            if (!EmptyUtils.isEmpty(list)) {
                for (FcAccDTO dto : list) {
                    accIds.add(dto.getId());
                }
            }
        } else {
            List<BtBankAccDTO> list = temp;
            if (!EmptyUtils.isEmpty(list)) {
                for (BtBankAccDTO dto : list) {
                    accIds.add(dto.getId());
                }
            }
        }
        return accIds;
    }
    @Override
    public List listDTOByMultiVal(String userId, String resource, List<String> curIds, String accountType, String permission) {
        return this.accountGrantService.listDTOByMultiVal(userId, resource, curIds, accountType, permission);
    }
    @Override
    public List<String> listByMultiVal(String userId, String corpId, String resource, List<String> curIds, String accountType, String permission) {
        return this.accountGrantService.listByMultiVal(userId, corpId, resource, curIds, accountType, permission);
    }
    @Override
    public List listDTOByMultiVal(String userId, String corpId, String resource, List<String> curIds, String accountType, String permission) {
        List<BtBankAccDTO> list = accountGrantService.listDTOByMultiVal(userId, corpId, resource, curIds, accountType, permission);
        //获取币种信息
        Map<String, BtCurrencyDTO> currencyMap = btCurrencyServiceI.getCurrency();
        for(BtBankAccDTO btBankAccDTO : list){
            if(!EmptyUtils.isEmpty(currencyMap)){
                if(currencyMap.containsKey(btBankAccDTO.getCurId())){
                    BtCurrencyDTO btCurrencyDTO = currencyMap.get(btBankAccDTO.getCurId());
                    if(!EmptyUtils.isEmpty(btCurrencyDTO)){
                        //设置币别code
                        btBankAccDTO.setCurEnglishCode(btCurrencyDTO.getEnglishCode());
                    }
                }
            }
        }
        return list;
    }
    @Override
    public List listDTOByMultiVal(String userId, String corpId, String resource, List<String> curIds, String accountType, String permission, boolean isUseAccountCorp) {
        return this.accountGrantService.listDTOByMultiVal(userId, corpId, resource, curIds, accountType, permission, isUseAccountCorp);
    }
    @Override
    public List listDTONotUnionByMultiVal(String userId, String corpId, String resource, List<String> curIds, String accountType, String permission) {
        return this.accountGrantService.listDTONotUnionByMultiVal(userId, corpId, resource, curIds, accountType, permission);
    }

    @Override
    public List listDTONotUnionByMultiVal(String userId, String corpId, String resource, String accountType, String permission, Map params) {
        return this.accountGrantService.listDTONotUnionByMultiVal(userId, corpId, resource, accountType, permission,params);
    }

    @Override
    public Boolean checkPermissionByUserAndAccount(String userId, String account, String accountType, String permission, String resource) {
        return this.accountGrantService.checkPermissionByUserAndAccount(userId, account, accountType, permission, resource);
    }
    @Override
    public void updateAccountTypeByAccount(String accountId, String accountType) {
        this.accountGrantService.updateAccountTypeByAccount(accountId, accountType);
    }
    @Override
    public void saveAccountGrant(AccountGrantDTO accountGrantDTO) {
        if (null == accountGrantDTO) {
            throw new RuntimeException("save accountGrant. object is null");
        }
        if (!StringUtils.hasText(accountGrantDTO.getAccountId())) {
            throw new RuntimeException("save accountGrant. accountId is null");
        }
        if (!StringUtils.hasText(accountGrantDTO.getCorpId())) {
            throw new RuntimeException("save accountGrant. corpId is null");
        }
        if (!StringUtils.hasText(accountGrantDTO.getUserId())) {
            throw new RuntimeException("save accountGrant. userId is null");
        }
        if (accountGrantDTO.getAccountType() == null) {
            throw new RuntimeException("save accountGrant. accountType is null");
        }
        AccountGrant accountGrant = new AccountGrant();
        accountGrant.setId(com.bytter.core.utils.StringUtils.getUUId());
        accountGrant.setAccountId(accountGrantDTO.getAccountId());
        accountGrant.setUserId(accountGrantDTO.getUserId());
        accountGrant.setCorpId(accountGrantDTO.getCorpId());
        accountGrant.setAccountType(accountGrantDTO.getAccountType());
        accountGrant.setCanQueryDetail(1);
        accountGrant.setCanQueryBalance(1);
        accountGrant.setCanTransferAccounts(1);
        accountGrantService.saveAccountGrant(accountGrant);
    }
    @Override
    public List<String> listByMultiValAccIds(String userId, String resource, List<String> curIds, String accountType, String permission) {
        return this.accountGrantService.listByMultiValAccIds(userId, resource, curIds, accountType, permission);
    }
    @Override
    public List<String> listByMultiValAccs(String userId, String resource, List<String> curIds, String accountType, String permission) {
        return this.accountGrantService.listByMultiValAccs(userId, resource, curIds, accountType, permission);
    }

    @Override
    public List listDTOByMultiValAccId(String userId, String resource, List<String> curIds, String accountType, String permission,String accId) {
        return this.accountGrantService.listDTOByMultiVal(userId, resource, curIds, accountType, permission,accId);
    }
}
