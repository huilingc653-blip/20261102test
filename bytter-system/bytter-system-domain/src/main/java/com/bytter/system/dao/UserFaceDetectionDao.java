package com.bytter.system.dao;
import com.bytter.system.entity.UserFaceDetection;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by zhoul on 2018/07/30.
 */
@Repository
public class UserFaceDetectionDao extends BaseDao<UserFaceDetection, String> {
    /**
     * 根据账号查询人脸图像数据
     *
     * @param account
     * @return
     */
    public UserFaceDetection getUserDetectionByAccount(String account) {
        String hql = "from UserFaceDetection where account = :account";
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        UserFaceDetection userFaceDetection = getUnique(hql, params);
        return userFaceDetection;
    }
}
