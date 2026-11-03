package com.bytter.system.mobile.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.entity.Corp;
import com.bytter.system.entity.User;
import com.bytter.system.service.CorpService;
import com.bytter.system.service.UserService;
import com.bytter.system.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
/**
 * 用户管理控制器
 * Created by zhoul on 2017/4/11.
 */
@Controller
@RequestMapping("/api/user")
public class UserApiController extends BaseController {
    @Resource
    private UserService userService;
    @Resource
    private CorpService corpService;

    @GetMapping("/userInfo")
    @ResponseBody
    public Object userInfo() {
        ICurrentUser currentUser = getCurrentUser();
        User user = userService.get(currentUser.getId());
        UserVO userVO = new UserVO();
        BeanUtils.copyNotNullProperties(user, userVO);
        userVO.setSearchBox("hide"); //大森林特殊需求,用于app判断
        userVO.setCorpId(currentUser.getCorpId());
        Corp corp = corpService.get(currentUser.getCorpId());
        userVO.setCorpName(corp.getName());
        return userVO;
    }
    @PostMapping("/checkPassword")
    @ResponseBody
    public Object checkPassword(String account, String password) {
        User user = userService.getByLoginName(account);
        userService.checkPassword(user.getId(), password);
        return success();
    }
}
