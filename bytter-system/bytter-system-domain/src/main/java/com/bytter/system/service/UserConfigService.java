package com.bytter.system.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.dao.UserConfigDao;
import com.bytter.system.entity.UserConfig;
@Service("userConfigService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserConfigService extends BaseService<UserConfig, String> {
    @Autowired
    private UserConfigDao userConfigDao;
    @Override
    public BaseDao<UserConfig, String> getEntityDao() {
        return userConfigDao;
    }
    public UserConfig getUserIndexSkin(String userId) {
        return userConfigDao.getUserIndexSkin(userId);
    }
    public void saveOrUpdateIndexSkin(String userId, String content) {
        String configType = "indexSkin";
        UserConfig userConfigDb = getUserIndexSkin(userId);
        if (userConfigDb == null) {
            UserConfig userConfig = new UserConfig();
            userConfig.setType(configType);
            userConfig.setUserId(userId);
            userConfig.setContent(content);
            userConfigDao.save(userConfig);
        } else {
            userConfigDb.setContent(content);
            userConfigDao.update(userConfigDb);
        }
    }
}
