package com.bytter.system.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.DigestUtils;
import com.bytter.system.dao.UserHistoryPasswordDao;
import com.bytter.system.entity.UserHistoryPassword;
/**
 * @author zuoy 2017/11/15
 */
@Service("userHistoryPasswordService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserHistoryPasswordService extends BaseService<UserHistoryPassword, String> {
    @Autowired
    private UserHistoryPasswordDao userHistoryPasswordDao;
    @Override
    public BaseDao<UserHistoryPassword, String> getEntityDao() {
        return userHistoryPasswordDao;
    }
    /**
     * 根据ID查询最近三次的旧密码
     *
     * @param userId
     * @return
     */
    public List<UserHistoryPassword> listOldThreePasswordByUserId(String userId, Integer maxResult) {
        return this.userHistoryPasswordDao.listOldThreePasswordByUserId(userId, maxResult);
    }
    /**
     * 查询最后一次密码
     *
     * @param userId 用户ID
     * @return
     */
    public UserHistoryPassword getLastPasswordBy(String userId) {
        return this.userHistoryPasswordDao.getLastPasswordBy(userId);
    }
    /**
     * 将密码保存到历史表
     *
     * @param userId
     * @param password
     */
    public void saveUserHistoryPassword(String userId, String password) {
        UserHistoryPassword userHistoryPassword = new UserHistoryPassword();
        userHistoryPassword.setUserId(userId);
        userHistoryPassword.setCreateTime(DateUtils.getDate());
        userHistoryPassword.setPassword(DigestUtils.md5Hex(password));
        this.saveEntity(userHistoryPassword);
    }
}
