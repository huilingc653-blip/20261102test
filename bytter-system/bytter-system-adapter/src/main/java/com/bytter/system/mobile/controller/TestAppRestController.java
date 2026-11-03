package com.bytter.system.mobile.controller;
import com.bytter.system.service.PushService;
import com.bytter.system.service.UserFaceDetectionService;
import com.bytter.core.base.BaseController;
import com.bytter.core.enums.AppPageEnum;
import com.bytter.core.utils.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by chenlw on 2018/12/28.
 */
@RestController
@RequestMapping("/app/test/api")
public class TestAppRestController extends BaseController {
    @Resource
    private PushService pushService;
    @Resource
    private UserFaceDetectionService userFaceDetectionService;
    /**
     * 测试APP消息推送
     *
     * @param account 要推送的账户
     * @param body    推送的消息内容
     * @return
     */
    @PostMapping("/testNotificationPushing")
    public Map testNotificationPushing(@RequestParam("account") String account,
                                       @RequestParam("body") String body) {
        Map resultMap = new HashMap();
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(body)) {
            resultMap.put("state", "401");
            resultMap.put("message", "推送账号或消息为空");
            return resultMap;
        }
        ArrayList<String> strings = new ArrayList<>();
        strings.add(account);
        Map<String, Object> map = new HashMap<>();
//        map.put("id", "a66fc45c-49b9-11e8-8352-66dd0a55a045");
//        map.put("businessId", "zlzlzlzlzl");
//        map.put("categoryId", "testflow");
//        pushService.pushNotice(strings, "测试审批详细", "测试审批详细", AppPageEnum.APPROVE_DETAIL, map);
        map.put("id", "acf5d2246e2c4800b24c77144751b80a");
        map.put("categoryName", "银行借款申请补录");
        map.put("createDate", 1522288713000L);
        map.put("name", "你有一笔金额为200的借款补录待处理！");
        try {
            pushService.pushNotice(strings, "消息测试", body, AppPageEnum.NOTIFICATION, map, true);
        } catch (Exception e) {
            resultMap.put("state", "501");
            resultMap.put("message", "推送消息失败，具体错误内容为" + e.getMessage());
            return resultMap;
        }
        resultMap.put("state", "200");
        resultMap.put("message", "消息已推送，请查看手机以确认是否收到消息");
        return resultMap;
    }
    /**
     * 测试APP人脸识别
     *
     * @param faceImage1 图片文件的base64编码字符串
     * @param faceImage2 图片文件的base64编码字符串
     * @return
     */
    @PostMapping("/testFaceDetection")
    public Map testFaceDetection(@RequestParam("faceImage1") String faceImage1,
                                 @RequestParam("faceImage2") String faceImage2) {
        return userFaceDetectionService.testMatch(faceImage1, faceImage2);
    }
}
