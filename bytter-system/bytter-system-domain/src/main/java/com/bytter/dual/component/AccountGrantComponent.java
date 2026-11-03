package com.bytter.dual.component;
import com.bytter.core.annotation.DualManager;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.entity.AccountGrant;
import com.bytter.system.service.AccountGrantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
/**
 * 双管理员 - 账号授权
 * Created by zhoul on 2018/11/
 */
@DualManager
public class AccountGrantComponent extends AbstractDualManagerCommand<Map<String, Object>> {
    @Autowired
    private AccountGrantService accountGrantService;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public String getApprovePageUrl() {
        return "/dual/manager/accountGrant";
    }
    @Override
    void executeData(Map<String, Object> variables) {
        String userId = (String) variables.get("userId");
        List<Map<String, Object>> accountGrantList = (List<Map<String, Object>>) variables.get("accountGrantList");
        List<AccountGrant> accountGrants = BeanUtils.copyMapListProperties(accountGrantList, AccountGrant.class);
        accountGrantService.save(accountGrants, userId);
    }
}
