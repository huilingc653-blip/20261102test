package com.bytter.increment.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.domain.dao.ChoMessageDao;
import com.bytter.increment.domain.entity.ChoMessage;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.dto.TaskResponseDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: CHO_MESSAGE
 * @date 2017-09-23 14:53:20
 */
@Service("choMessageService")
@Transactional
public class ChoMessageService extends BaseService<ChoMessage, String> {
    @Autowired
    private ChoMessageDao choMessageDao;
    @Autowired
	private IWorkFlowService workFlowService;
    @DubboReference(check = false)
	private IUserService userService;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(ChoMessage entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(ChoMessage entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(ChoMessage entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(ChoMessage entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<ChoMessage, String> getEntityDao() {
        return choMessageDao;
    }
    /**
     * 写入日志信息表
     *
     * @param msg
     * @return
     */
    public boolean saveMessage(ChoMessage msg) {
        if (msg == null || StringUtils.isAllBlank(msg.getBillNo(), msg.getMessage())) {
            return false;
        }
        this.save(msg);
        return true;
    }
    /**
     * 写入日志信息表
     *
     * @param billNo      外部引用ID
     * @param messageType 日志类型
     * @param message     日志信息
     * @param busType     业务类型
     * @return
     */
    public boolean saveMessage(String billNo, Integer messageType, String message, String busType, ICurrentUser user) {
        return saveMessage(billNo, messageType, message, busType, user.getLoginName(), user.getUsername());
    }
    /**
     * 写入日志信息表
     *
     * @param billNo      外部引用ID
     * @param messageType 日志类型
     * @param message     日志信息
     * @param busType     业务类型
     * @param userCode    操作人代码
     * @param userName    操作人名称
     * @return
     */
    public boolean saveMessage(String billNo, Integer messageType, String message, String busType, String userCode, String userName) {
        ChoMessage msg = new ChoMessage();
        msg.setBillNo(billNo);
        msg.setEnrolDate(new Date());
        msg.setMessageType(messageType);
        msg.setMessage(message);
        msg.setMessage(message);
        msg.setBusType(busType);
        msg.setOp(userCode);
        msg.setOpName(userName);
        return saveMessage(msg);
    }
    /**
     * 日志列表
     *
     * @param billNo  外部引用ID
     * @param busType 业务类型
     * @return
     */
    public List<ChoMessage> listMessage(String billNo, String busType) {
        List<ChoMessage> list = choMessageDao.listMessage(billNo, busType);
        if (EmptyUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        List<TaskResponseDTO> taskResponseDTOS = workFlowService.listAllCommentsByBusinessId(billNo);
        if (!EmptyUtils.isEmptyList(taskResponseDTOS)) {
            for (int i = 0; i < taskResponseDTOS.size(); i++) {
                TaskResponseDTO responseDTO = taskResponseDTOS.get(i);
                ChoMessage message = new ChoMessage();
                Integer messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_PASS;
                if (!"1".equals(responseDTO.getTaskPassFlag()) && !"2".equals(responseDTO.getTaskPassFlag())
                        && !"3".equals(responseDTO.getTaskPassFlag()) && !"4".equals(responseDTO.getTaskPassFlag())) {
                    continue;
                }
                if ("3".equals(responseDTO.getTaskPassFlag()) || "4".equals(responseDTO.getTaskPassFlag())) {
                    messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_BACK;
                } else if ("2".equals(responseDTO.getTaskPassFlag())) {
                    messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_REVOKE;
                }
                message.setMessageType(messageType);
                message.setMessage(responseDTO.getComment());
                message.setEnrolDate(responseDTO.getEndTime());
                UserDTO user = userService.getByUserId(responseDTO.getUserId());
                if (user != null) {
                    message.setOp(user.getLoginName());
                    message.setOpName(responseDTO.getUserName());
                }
                list.add(message);
            }
        }
        Collections.sort(list, new Comparator<ChoMessage>() {
            @Override
            public int compare(ChoMessage msg1, ChoMessage msg2) {
                return msg2.getEnrolDate().compareTo(msg1.getEnrolDate());
            }
        });
        return list;
    }
}
