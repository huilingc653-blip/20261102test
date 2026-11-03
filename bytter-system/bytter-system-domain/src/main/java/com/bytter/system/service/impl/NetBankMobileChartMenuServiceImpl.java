package com.bytter.system.service.impl;
import com.bytter.system.api.IMobileChartMenuService;
import com.bytter.system.api.dto.MobileChartMenuDTO;
import com.bytter.system.enums.MobileChartTopMenuEnum;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * @author chenlw
 * @since 2020-06-22
 */
@Service("netBankMobileChartMenuServiceI")
@DubboService(group = "netBankMobileChartMenuServiceImpl")
public class NetBankMobileChartMenuServiceImpl implements IMobileChartMenuService {
    /**
     * 银行账户查询菜单ID
     */
    private static String bankAccountQueryMenuId = "3c842a31c8f84366af36eb17851b4dfa";
    @Override
    public String getMobileChartModuleKey() {
        return MobileChartTopMenuEnum.netbank.name();
    }
    @Override
    public String getMobileChartModuleName() {
        return MobileChartTopMenuEnum.netbank.getName();
    }
    @Override
    public List<MobileChartMenuDTO> getMobileChartMenuList(Set<String> userHavePermissionMenuIds) {
        List<MobileChartMenuDTO> menuVoList = new ArrayList<>();
        if (userHavePermissionMenuIds.contains(bankAccountQueryMenuId)) {
            MobileChartMenuDTO bankAccountQuery = new MobileChartMenuDTO();
            bankAccountQuery.setMenuId(bankAccountQueryMenuId);
            bankAccountQuery.setName("银行账户查询");
            bankAccountQuery.setDescription("银行账户查询表");
            bankAccountQuery.setUrl("/mobile/api/bankacc/mobileBankAcc");
            bankAccountQuery.setIconUrl("/static/mobile/icon/chartPage/netbank/icon_netbank_bank_acc_query@2x.png");
            menuVoList.add(bankAccountQuery);
        }
        return menuVoList;
    }
    @Override
    public List<MobileChartMenuDTO> getMobileChartTabMenuList(String menuId, Set<String> userHavePermissionMenuIds) {
        return null;
    }
    @Override
    public int getUserHavePermissionMenuSize(Set<String> userHavePermissionMenuIds) {
        int result = 0;
        if (userHavePermissionMenuIds.contains(bankAccountQueryMenuId)) {
            result++;
        }
        return result;
    }
}
