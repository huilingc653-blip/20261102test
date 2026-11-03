package com.bytter.system.mobile.controller;
import com.bytter.system.entity.UserFaceDetection;
import com.bytter.system.service.UserFaceDetectionService;
import com.bytter.core.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.HashMap;
@Controller
@RequestMapping("/api/face")
public class FaceController extends BaseController {
    @Resource
    private UserFaceDetectionService userFaceDetectionService;
    @PostMapping("/collection")
    @ResponseBody
    public Object collection(String account, String face) {
        UserFaceDetection userFaceDetection = new UserFaceDetection();
        userFaceDetection.setAccount(account);
        userFaceDetection.setFace(face);
        try {
            //userFaceDetectionService.saveOrUpdate(userFaceDetection);
            userFaceDetectionService.saveOrUpdateUserFace(userFaceDetection);
        } catch (IllegalArgumentException e) {
            HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("errorCode", e.getMessage());
            return objectObjectHashMap;
        }
        return success();
    }
    @PostMapping("/addOrUpdate")
    @ResponseBody
    public Object addOrUpdate(String account, String face) {
        UserFaceDetection userFaceDetection = new UserFaceDetection();
        userFaceDetection.setAccount(account);
        userFaceDetection.setFace(face);
        try {
            userFaceDetectionService.saveOrUpdateUserFaceNew(userFaceDetection);
        } catch (IllegalArgumentException e) {
            HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("errorCode", e.getMessage());
            return objectObjectHashMap;
        }
        return success();
    }
    @PostMapping("/getFace")
    @ResponseBody
    public Object getFace(String account) {
        UserFaceDetection userFaceDetection = userFaceDetectionService.getUserFaceDetection(account);
        return userFaceDetection;
    }
}
