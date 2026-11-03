package com.bytter.system.service;
import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.MatchRequest;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.dao.UserFaceDetectionDao;
import com.bytter.system.entity.UserFaceDetection;
import com.bytter.system.enums.UserFaceVerifyStatusEnum;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2018/07/30.
 */
@Service("userFaceDetectionService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserFaceDetectionService extends BaseService<UserFaceDetection, String> {
    //设置APPID/AK/SK
    public static final String APP_ID = "24713112";
    public static final String API_KEY = "GDiMG8l3sXEPxrGxLUFGv5on";
    public static final String SECRET_KEY = "N3MoUGAcakoYS2wji0bdH9YxBaGha1S6";
    @Value("${proxy.switch}")
    private boolean proxySwitch;
    @Value("${proxy.host}")
    private String proxyHost;
    @Value("${proxy.port}")
    private Integer proxyPort;
    public AipFace client;
    @Autowired
    private UserFaceDetectionDao userFaceDetectionDao;
    @Autowired
    private IConfigService configServiceI;
    @Override
    public BaseDao<UserFaceDetection, String> getEntityDao() {
        return userFaceDetectionDao;
    }
    private AipFace getFaceClient() {
        if (client != null) {
            return client;
        }
        // 初始化一个AipFace
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        if (proxySwitch) {
            // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
            client.setHttpProxy(proxyHost, proxyPort);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
        }
        this.client = client;
        return client;
    }
    private List<MatchRequest> getMatcheRequests(String faceA, String faceB) {
        MatchRequest req1 = new MatchRequest(faceA, "BASE64", "LIVE", "NONE", "NORMAL");
        MatchRequest req2 = new MatchRequest(faceB, "BASE64", "LIVE", "NONE", "NORMAL");
        ArrayList<MatchRequest> requests = new ArrayList<>();
        requests.add(req1);
        requests.add(req2);
        return requests;
    }
    @Override
    public void saveOrUpdate(UserFaceDetection entity) {
        if (detect(entity.getFace())) {
            super.saveOrUpdate(entity);
        }
    }
    /**
     * 2019-02-25
     * chenlw
     * 保存用户上传的人脸图像（super.saveOrUpdate(entity);实体属性值是什么都会存到数据库）
     * <p>
     * 系统启用人脸复核后需要考虑的情况
     * （1）用户未曾上传人脸图像：数据库表没有记录，修改实体状态为待复核，新增记录。
     * （2）用户上传了人脸图像，处于待审核状态：face字段无值，verifyFace字段有值。覆盖verifyFace字段值。
     * （3）用户上传了人脸图像，通过复核了：face字段有值，verifyFace字段无值。更新verifyFace字段值，修改状态为待复核。注意这种情况，用户仍然可以使用人脸登录.
     * （4）用户已经上传过人脸图像，处于待复核状态，再次上传人脸图像：face字段有值，verifyFace字段有值。更新verifyFace字段值，保持通过待复核状态。
     * （注意这种情况，用户仍然可以使用人脸登录）
     * <p>
     * <p>
     * 系统未开启人脸复核需要考虑的情况
     * （1）用户未曾上传人脸图像：数据库表没有记录，修改实体状态为通过复核，新增记录。
     * （2）用户上传了人脸图像，处于待复核状态：face字段无值，verifyFace字段有值。更新face字段，保持待复核状态。
     * （3）用户上传了人脸图像，并通过了复核：face字段有值，verifyFace字段无值。更新face字段，保持通过复核状态。
     * （4）用户已经上传过人脸图像，处于待复核，再次上传人脸图像：face字段有值，verifyFace字段有值。更新face字段，保持通过待复核状态。
     *
     * @param entity
     */
    public void saveOrUpdateUserFace(UserFaceDetection entity) {
        if (detect(entity.getFace())) {
            if (isOpenFaceVerify()) {   //系统开启了人脸复核
                UserFaceDetection userFaceDetection = userFaceDetectionDao.getUserDetectionByAccount(entity.getAccount());
                if (userFaceDetection == null) {
                    //1、用户未曾上传人脸图像：数据库表没有记录。
                    //修改状态为待复核，新增记录。
                    entity.setVerifyStatus(UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue());
                    entity.setVerifyFace(entity.getFace());
                    entity.setFace("");
                    super.saveOrUpdate(entity);
                } else if (StringUtils.isEmpty(userFaceDetection.getFace())
                        && !StringUtils.isEmpty(userFaceDetection.getVerifyFace())
                        && UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())) {
                    //2、用户上传了人脸图像，处于待审核状态：face字段无值，verifyFace字段有值。覆盖verifyFace字段值。
                    userFaceDetection.setVerifyFace(entity.getFace());
                    super.saveOrUpdate(userFaceDetection);
                } else if (!StringUtils.isEmpty(userFaceDetection.getFace())
                        && StringUtils.isEmpty(userFaceDetection.getVerifyFace())
                        && UserFaceVerifyStatusEnum.VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())) {
                    //3、用户第一次上传人脸图像，通过复核了：face字段有值，verifyFace字段无值。
                    // 更新verifyFace字段值，修改状态为待复核。
                    //注意这种情况，用户仍然可以使用人脸登录
                    userFaceDetection.setVerifyFace(entity.getFace());
                    userFaceDetection.setVerifyStatus(UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue());
                    super.saveOrUpdate(userFaceDetection);
                } else if (!StringUtils.isEmpty(userFaceDetection.getFace())
                        && !StringUtils.isEmpty(userFaceDetection.getVerifyFace())
                        && UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())) {
                    //4、用户已经上传过人脸图像，处于待复核状态，再次上传人脸图像：face字段有值，verifyFace字段有值。
                    // 更新verifyFace字段值，保持通过待复核状态。
                    //注意这种情况，用户仍然可以使用人脸登录
                    userFaceDetection.setVerifyFace(entity.getFace());
                    super.saveOrUpdate(userFaceDetection);
                }
            } else { //系统未开启人脸复核（要处理反复关闭和开启人脸复核功能的情况）
                UserFaceDetection userFaceDetection = userFaceDetectionDao.getUserDetectionByAccount(entity.getAccount());
                if (userFaceDetection == null) {
                    //1、用户未曾上传人脸图像
                    //数据库表没有记录，修改实体状态为通过复核，新增记录。
                    entity.setVerifyStatus(UserFaceVerifyStatusEnum.VERIFIED.getValue());
                    super.saveOrUpdate(entity);
                } else if (UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())
                        && StringUtils.isEmpty(userFaceDetection.getFace())
                        && !StringUtils.isEmpty(userFaceDetection.getVerifyFace())) {
                    //2、用户上传了人脸图像，处于待复核状态：face字段无值，verifyFace字段有值。
                    // 更新face字段，保持待复核状态。
                    userFaceDetection.setFace(entity.getFace());
                    super.saveOrUpdate(userFaceDetection);
                } else if (UserFaceVerifyStatusEnum.VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())
                        && !StringUtils.isEmpty(userFaceDetection.getFace())
                        && StringUtils.isEmpty(userFaceDetection.getVerifyFace())) {
                    //3、用户上传了人脸图像，并通过了复核：face字段有值，verifyFace字段无值。
                    // 更新face字段，保持通过复核状态。
                    userFaceDetection.setFace(entity.getFace());
                    super.saveOrUpdate(userFaceDetection);
                } else if (UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())
                        && !StringUtils.isEmpty(userFaceDetection.getFace())
                        && !StringUtils.isEmpty(userFaceDetection.getVerifyFace())) {
                    //4、用户已经上传过人脸图像，处于待复核，再次上传人脸图像：face字段有值，verifyFace字段有值。
                    // 更新face字段，保持待复核状态。
                    userFaceDetection.setFace(entity.getFace());
                    super.saveOrUpdate(userFaceDetection);
                }
            }
        }
    }
    public void saveOrUpdateUserFaceNew(UserFaceDetection entity) {
        if (isOpenFaceVerify()) {   //系统开启了人脸复核
            UserFaceDetection userFaceDetection = userFaceDetectionDao.getUserDetectionByAccount(entity.getAccount());
            if (userFaceDetection == null) {
                //1、用户未曾上传人脸图像：数据库表没有记录。
                //修改状态为待复核，新增记录。
                entity.setVerifyStatus(UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue());
                entity.setVerifyFace(entity.getFace());
                entity.setFace("");
                super.saveOrUpdate(entity);
            } else if (StringUtils.isEmpty(userFaceDetection.getFace())
                    && !StringUtils.isEmpty(userFaceDetection.getVerifyFace())
                    && UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())) {
                //2、用户上传了人脸图像，处于待审核状态：face字段无值，verifyFace字段有值。覆盖verifyFace字段值。
                userFaceDetection.setVerifyFace(entity.getFace());
                super.saveOrUpdate(userFaceDetection);
            } else if (!StringUtils.isEmpty(userFaceDetection.getFace())
                    && StringUtils.isEmpty(userFaceDetection.getVerifyFace())
                    && UserFaceVerifyStatusEnum.VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())) {
                //3、用户第一次上传人脸图像，通过复核了：face字段有值，verifyFace字段无值。
                // 更新verifyFace字段值，修改状态为待复核。
                //注意这种情况，用户仍然可以使用人脸登录
                userFaceDetection.setVerifyFace(entity.getFace());
                userFaceDetection.setVerifyStatus(UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue());
                super.saveOrUpdate(userFaceDetection);
            } else if (!StringUtils.isEmpty(userFaceDetection.getFace())
                    && !StringUtils.isEmpty(userFaceDetection.getVerifyFace())
                    && UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())) {
                //4、用户已经上传过人脸图像，处于待复核状态，再次上传人脸图像：face字段有值，verifyFace字段有值。
                // 更新verifyFace字段值，保持通过待复核状态。
                //注意这种情况，用户仍然可以使用人脸登录
                userFaceDetection.setVerifyFace(entity.getFace());
                super.saveOrUpdate(userFaceDetection);
            }
        } else { //系统未开启人脸复核（要处理反复关闭和开启人脸复核功能的情况）
            UserFaceDetection userFaceDetection = userFaceDetectionDao.getUserDetectionByAccount(entity.getAccount());
            if (userFaceDetection == null) {
                //1、用户未曾上传人脸图像
                //数据库表没有记录，修改实体状态为通过复核，新增记录。
                entity.setVerifyStatus(UserFaceVerifyStatusEnum.VERIFIED.getValue());
                super.saveOrUpdate(entity);
            } else if (UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())
                    && StringUtils.isEmpty(userFaceDetection.getFace())
                    && !StringUtils.isEmpty(userFaceDetection.getVerifyFace())) {
                //2、用户上传了人脸图像，处于待复核状态：face字段无值，verifyFace字段有值。
                // 更新face字段，保持待复核状态。
                userFaceDetection.setFace(entity.getFace());
                super.saveOrUpdate(userFaceDetection);
            } else if (UserFaceVerifyStatusEnum.VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())
                    && !StringUtils.isEmpty(userFaceDetection.getFace())
                    && StringUtils.isEmpty(userFaceDetection.getVerifyFace())) {
                //3、用户上传了人脸图像，并通过了复核：face字段有值，verifyFace字段无值。
                // 更新face字段，保持通过复核状态。
                userFaceDetection.setFace(entity.getFace());
                super.saveOrUpdate(userFaceDetection);
            } else if (UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())
                    && !StringUtils.isEmpty(userFaceDetection.getFace())
                    && !StringUtils.isEmpty(userFaceDetection.getVerifyFace())) {
                //4、用户已经上传过人脸图像，处于待复核，再次上传人脸图像：face字段有值，verifyFace字段有值。
                // 更新face字段，保持待复核状态。
                userFaceDetection.setFace(entity.getFace());
                super.saveOrUpdate(userFaceDetection);
            }
        }
    }
    /**
     * 识别传的图片是否为人脸
     *
     * @param face 人脸图片BASE64
     * @return 返回识别结果
     */
    private boolean detect(String face) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("max_face_num", "1");
        options.put("face_type", "LIVE");
        AipFace faceClient = getFaceClient();
        JSONObject res = faceClient.detect(face, "BASE64", options);
        int errorCode = res.getInt("error_code");
        if (errorCode != 0) {
            logger.info(res.toString());
            throw new IllegalArgumentException(String.valueOf(errorCode));
        }
        JSONObject result = res.getJSONObject("result");
        JSONArray faceList = result.getJSONArray("face_list");
        JSONObject jsonObject = faceList.getJSONObject(0);
        double faceProbability = jsonObject.getDouble("face_probability");
        return faceProbability > 0.90D;
    }
    public boolean match(UserFaceDetection userFaceDetection) {
        return match(userFaceDetection.getAccount(), userFaceDetection.getFace());
    }
    /**
     * 根据账号匹配传入的图片是否为同一个人
     * <p>
     * 2019-02-27
     * chenlw
     * <p>
     * 系统开启人脸复核后，需要考虑的情况
     * （1）用户上传了人脸图像，并通过了复核，face字段有值，并且verifyFace字段为空，即用户没有再次上传人脸图像。允许使用人脸登录。
     * （2）用户已经上传过人脸图像，并通过了审核，再次上传人脸图像，此时这条记录的状态为待复核。face字段有值，verifyFace字段有值。这种情况，用户仍然可以使用人脸登录。
     * （3）用户已经上传过人脸图像，处于待复核状态：face字段无值，verifyFace字段有值。不允许使用人脸登录。
     * <p>
     * <p>
     *
     * @param account             账号
     * @param needToDetectionFace 需要识别的图片BASE64编码
     * @return 是否同一个人
     */
    public boolean match(String account, String needToDetectionFace) {
        //UserFaceDetection userFaceDetection = userFaceDetectionDao.get(account);
        UserFaceDetection userFaceDetection = userFaceDetectionDao.getUserDetectionByAccount(account);
        if (userFaceDetection == null) {
            //throw new IllegalArgumentException("BT0009");
            throw new IllegalArgumentException("user_not_upload_face");
        }
        AipFace faceClient = getFaceClient();
        JSONObject res = null;
        if (isOpenFaceVerify()) {
            //开启了人脸复核，特别处理
            if (UserFaceVerifyStatusEnum.VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())
                    && StringUtils.isEmpty(userFaceDetection.getVerifyFace())) {
                //1、用户上传了人脸图像，并通过了复核，face字段有值，并且verifyFace字段为空，即用户没有再次上传人脸图像。
                //允许使用人脸登录。
                res = faceClient.match(getMatcheRequests(userFaceDetection.getFace(), needToDetectionFace));
            } else if (UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())
                    && !StringUtils.isEmpty(userFaceDetection.getFace())
                    && !StringUtils.isEmpty(userFaceDetection.getVerifyFace())) {
                //2、用户已经上传过人脸图像，并通过了审核，再次上传人脸图像，此时这条记录的状态为待复核。face字段有值，verifyFace字段有值。
                // 这种情况，用户仍然可以使用人脸登录。
                res = faceClient.match(getMatcheRequests(userFaceDetection.getFace(), needToDetectionFace));
            } else if (UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())
                    && StringUtils.isEmpty(userFaceDetection.getFace())
                    && !StringUtils.isEmpty(userFaceDetection.getVerifyFace())) {
                //3、用户已经上传过人脸图像，处于待复核状态：face字段无值，verifyFace字段有值。
                // 不允许使用人脸登录。
                //throw new IllegalArgumentException("该用户的人脸图像暂未通过管理员的复核，不能登录。");
                throw new IllegalArgumentException("user_face_not_verified");
            }
        } else {
            res = faceClient.match(getMatcheRequests(userFaceDetection.getFace(), needToDetectionFace));
        }
        int errorCode = res.getInt("error_code");
        if (errorCode != 0) {
            logger.info(account + ":" + res.toString());
            throw new IllegalArgumentException(String.valueOf(errorCode));
        }
        JSONObject result = res.getJSONObject("result");
        double score = result.getDouble("score");
        logger.error(" score: " + score);
        return score > 70D;
    }
    /**
     * 2019-02-25
     * chenlw
     * work：获取系统配置，判断是否启用了人脸复核（ 人脸复核： 1启用；0停用）
     *
     * @return 返回true表示系统启用了人脸复核
     */
    public boolean isOpenFaceVerify() {
        boolean result = false;
        ConfigDTO configDTO = configServiceI.getConfig(ConfigConstant.SYS, "faceVerify");
        if (configDTO != null && !StringUtils.isEmpty(configDTO.getValue())
                && UserFaceVerifyStatusEnum.VERIFIED.getValue().toString().equals(configDTO.getValue())) {
            result = true;
        }
        return result;
    }
    /**
     * 2019-02-25
     * chenlw
     * 获取用户的人脸图像。
     *
     * @param account
     * @return
     */
    public UserFaceDetection getUserFaceDetection(String account) {
        UserFaceDetection userFaceDetection = userFaceDetectionDao.getUserDetectionByAccount(account);
        return userFaceDetection;
    }
    /**
     * 2019-02-27
     * chenlw
     * 更新人脸复核状态
     * 如果当前没有开启人脸复核，则无需更新
     *
     * @param account
     * @param currentVerifyStatus
     */
    public void updateUserFaceVerifyStatus(String account, Integer currentVerifyStatus) {
        if (UserFaceVerifyStatusEnum.VERIFIED.getValue().equals(currentVerifyStatus)) {
            passUserFaceVerify(account);
        } else if (UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue().equals(currentVerifyStatus)) {
            unPassUserFaceVerify(account);
        }
    }
    /**
     * 2019-02-26
     * chenlw
     * 通过人脸复核
     * <p>
     * 数据库表记录可能的情况
     * <p>
     * 系统启用人脸复核后需要考虑的情况
     * （1）用户上传了人脸图像，处于待审核状态：face字段无值，verifyFace字段有值。修改状态为通过复核，将verifyFace字段的值赋值给face字段，并将verifyFace的值清空。
     * （2）用户上传了人脸图像，通过复核了：无需处理。
     * （3）用户已经上传过人脸图像，并通过了审核，再次上传人脸图像：face字段有值，verifyFace字段有值。修改状态为通过复核，将verifyFace字段的值赋值给face字段，并将verifyFace的值清空。
     *
     * @param account
     */
    private void passUserFaceVerify(String account) {
        UserFaceDetection userFaceDetection = userFaceDetectionDao.getUserDetectionByAccount(account);
        if (userFaceDetection != null) {
            if (UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())) {
                //1、用户上传了人脸图像，处于待审核状态：face字段无值，verifyFace字段有值。
                //3、用户已经上传过人脸图像，并通过了审核，再次上传人脸图像：face字段有值，verifyFace字段有值。
                // 修改状态为通过复核，将verifyFace字段的值赋值给face字段，并将verifyFace的值清空。
                userFaceDetection.setVerifyStatus(UserFaceVerifyStatusEnum.VERIFIED.getValue());
                userFaceDetection.setFace(userFaceDetection.getVerifyFace());
                userFaceDetection.setVerifyFace("");
                super.saveOrUpdate(userFaceDetection);
            }
        }
    }
    /**
     * 2019-02-26
     * chenlw
     * 人脸复核不通过
     * <p>
     * 数据库表记录可能的情况
     * <p>
     * 系统启用人脸复核后需要考虑的情况
     * （1）用户上传了人脸图像，处于待审核状态：face字段无值，verifyFace字段有值。无需处理。
     * （2）用户上传人脸图像，通过复核了：face字段有值，verifyFace字段无值。修改状态为待复核，将face字段的值赋值给verifyFace字段，并清空face字段值。
     * （3）用户已经上传过人脸图像，并通过了审核，再次上传人脸图像：face字段有值，verifyFace字段有值。无需处理。
     *
     * @param account
     */
    private void unPassUserFaceVerify(String account) {
        UserFaceDetection userFaceDetection = userFaceDetectionDao.getUserDetectionByAccount(account);
        if (userFaceDetection != null) {
            if (UserFaceVerifyStatusEnum.VERIFIED.getValue().equals(userFaceDetection.getVerifyStatus())
                    && !StringUtils.isEmpty(userFaceDetection.getFace())
                    && StringUtils.isEmpty(userFaceDetection.getVerifyFace())) {
                //2、用户上传人脸图像，通过复核了：face字段有值，verifyFace字段无值。
                //修改状态为待复核，将face字段的值赋值给verifyFace字段，并清空face字段值。
                userFaceDetection.setVerifyStatus(UserFaceVerifyStatusEnum.NOT_VERIFIED.getValue());
                userFaceDetection.setVerifyFace(userFaceDetection.getFace());
                userFaceDetection.setFace("");
                super.saveOrUpdate(userFaceDetection);
            }
        }
    }
    /*********************************************  测试人脸识别 ********************************************/
    /**
     * 2018-12-28
     * chenlw
     * 测试人脸识别
     *
     * @param faceImage1
     * @param faceImage2
     * @return
     */
    public Map testMatch(String faceImage1, String faceImage2) {
        Map<String, String> resultMap = new HashMap<>();
        AipFace faceClient = getFaceClient();
        JSONObject res = faceClient.match(getMatcheRequests(faceImage1, faceImage2));
        int errorCode = res.getInt("error_code");
        if (errorCode != 0) {
            resultMap.put("state", "501");
            resultMap.put("message", "人脸识别失败，错误代码为" + errorCode);
            return resultMap;
        }
        JSONObject result = res.getJSONObject("result");
        double score = result.getDouble("score");
        String message;
        if (score > 90D) {
            message = "人脸匹配成功";
        } else {
            message = "人脸匹配失败";
        }
        resultMap.put("state", "200");
        resultMap.put("message", message);
        return resultMap;
    }
}
