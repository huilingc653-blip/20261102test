package com.bytter.system.dao;
import com.bytter.system.entity.UserMobileChartModule;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author chenlw
 * @since 2020-05-12
 */
@Repository
public class UserMobileChartModuleDao extends BaseDao<UserMobileChartModule, String> {
    public List<UserMobileChartModule> getUserMobileChartModuleList(String userId) {
        String sql = "select ID, USER_ID AS userId, CHART_MODULE_KEY AS chartModuleKey, STATUS as status FROM USER_MOBILE_CHART_MODULE WHERE USER_ID = :userId";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return listBySQLAliasToBean(sql, params, UserMobileChartModule.class);
    }
    public void saveUserMobileChartModuleResource(List<UserMobileChartModule> userMobileChartModuleList) {
        for (UserMobileChartModule userMobileChartModule : userMobileChartModuleList) {
            if (StringUtils.isNotBlank(userMobileChartModule.getChartModuleKey())) {
                saveOrUpdate(userMobileChartModule);
            }
        }
    }
}
