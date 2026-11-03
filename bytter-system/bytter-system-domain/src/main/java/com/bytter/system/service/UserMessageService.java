package com.bytter.system.service;
import com.bytter.system.dao.UserDao;
import com.bytter.system.dao.UserMessageDao;
import com.bytter.system.entity.User;
import com.bytter.system.entity.UserMessage;
import com.bytter.system.vo.UserVO;
import com.bytter.core.base.AbstractService;
import com.bytter.core.common.exception.ParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户消息类
 *
 * @author zhoul
 * @since 2020-05-14
 */
@Service
public class UserMessageService extends AbstractService {
    public static final String SENT_TYPE_ALL_USER = "0";
    public static final String SENT_TYPE_ORDER_USER = "1";
    @Autowired
    private UserMessageDao userMessageDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;

    /**
     * 保存发送的消息
     *
     * @param type     0发送所有用户，1指定用户
     * @param title    消息标题
     * @param content  消息内容
     * @param userList 指定用户集合
     * @param createBy 创建者
     */
    public void save(String type, String title, String content, List<String> userList, String createBy) {
        if (SENT_TYPE_ORDER_USER.equals(type) && CollectionUtils.isEmpty(userList)) {
            throw new ParameterException("指定用户不能为空");
        }
        if (SENT_TYPE_ALL_USER.equals(type)) {
            List<UserVO> userVOList = userDao.listAllUserByUnDelete();
            userList = new ArrayList<>();
            for (UserVO userVO : userVOList) {
                userList.add(userVO.getId());
            }
        }
        UserMessage userMessage = new UserMessage();
        userMessage.setTitle(title);
        userMessage.setContext(content);
        userMessage.setCreateBy(createBy);
        userMessageDao.insert(userMessage, userList);
    }
    @Transactional(rollbackFor = Exception.class)
    public void read(List<String> ids, String updateBy) {
        userMessageDao.tagRead(ids, updateBy);
    }
    /**
     * 删除消息
     *
     * @param ids      消息集合
     * @param updateBy 操作人
     */
    public void delete(List<String> ids, String updateBy) {
        userMessageDao.delete(ids, updateBy);
    }

    public Object list(PageRequest pageRequest, String userId, String status, boolean flag) {
        Page<UserMessage> page = userMessageDao.list(pageRequest, userId, status, flag);
        if (page.isEmpty()) {
            return page;
        }
        List<String> uids = page.getContent().stream().map(UserMessage::getCreateBy).collect(Collectors.toList());
        Map<String, User> userMap = userService.listUserByIdsGpId(uids);
        page.get().forEach(item ->
                Optional.ofNullable(userMap.get(item.getCreateBy()))
                        .ifPresent(user -> item.setCreateUserName(user.getUsername())));
        return page;
    }
}
