package com.bytter.system.web.controller;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.dao.UserMessageDao;
import com.bytter.system.entity.UserMessage;
import com.bytter.system.service.UserMessageService;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 用户消息-站内信控制器
 *
 * @author zhoul
 * @since 2020-04-28
 */
@Controller
@RequestMapping("/sys/userMessage")
public class UserMessageController extends BaseController {
    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private UserMessageDao userMessageDao;
    @Autowired
    private UserServiceImpl userService;

    public static void main(String[] args) {
        System.out.println(StringUtils.getUUId());
    }
    /**
     * 列出当前登录用户的站内信
     */
    @GetMapping("/list")
    @ResponseBody
    public Object list(Integer page, Integer pageSize, String type, String status) {
        ICurrentUser currentUser = getCurrentUser();
        String userId = currentUser.getId();
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize == null ? 10 : pageSize);
        return userMessageService.list(pageRequest, userId, status, "send-message".equals(type));
    }
    @GetMapping("/view")
    public void view(String messageId, Model model) {
        ICurrentUser currentUser = getCurrentUser();
        String userId = currentUser.getId();
        ArrayList<String> strings = new ArrayList<>(1);
        strings.add(messageId);
        userMessageService.read(strings, userId);
        UserMessage userMessage = userMessageDao.get(messageId);
        if (StringUtils.isNotBlank(userMessage.getCreateBy())){
            UserDTO dto = userService.getByUserId(userMessage.getCreateBy());
            userMessage.setCreateUserName(dto.getUsername());
        }
        model.addAttribute("message", userMessage);
    }
    /**
     * 标记已读消息
     */
    @PostMapping("/read")
    @ResponseBody
    public Object read(@RequestParam("ids[]") List<String> list) {
        ICurrentUser currentUser = getCurrentUser();
        String userId = currentUser.getId();
        userMessageService.read(list, userId);
        return success();
    }
    /**
     * 删除站内信
     *
     * @param list
     */
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(@RequestParam("ids[]") List<String> list) {
        ICurrentUser currentUser = getCurrentUser();
        String userId = currentUser.getId();
        userMessageService.delete(list, userId);
        return success();
    }
    /**
     * 发送站内信
     *
     * @param title    标题
     * @param context  内容
     * @param userList 接收用户集合
     */
    @PostMapping("/send")
    @ResponseBody
    public Object send(String type, String title, String context, @RequestParam(value = "userList[]", required = false) List<String> userList) {
        ICurrentUser currentUser = getCurrentUser();
        String userId = currentUser.getId();
        userMessageService.save(type, title, context, userList, userId);
        return success();
    }
    @GetMapping("/sendView")
    public void sendView() {
    }
}
