package com.bytter.biz.budget.service;
import com.bytter.biz.budget.dao.FbsMessageDao;
import com.bytter.biz.budget.entity.FbsMessage;
import com.bytter.biz.budget.spi.constant.FbsMessageConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.dto.TaskResponseDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FBS_MESSAGE
 * @date 2017-06-12 14:53:19
 */
@Service("fbsMessageService")
@Transactional
public class FbsMessageService extends BaseService<FbsMessage, String> {
    @Autowired
    private FbsMessageDao fbsMessageDao;
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
    public void deleteEntity(FbsMessage entity) {
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
    public void save(FbsMessage entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FbsMessage entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(FbsMessage entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<FbsMessage, String> getEntityDao() {
        return fbsMessageDao;
    }
    /**
     * 写入日志信息表
     *
     * @param msg
     * @return
     */
    public boolean saveMessage(FbsMessage msg) {
        if (msg == null || StringUtils.isAllBlank(msg.getBillId(), msg.getMessage())) {
            return false;
        }
        this.save(msg);
        return true;
    }
    /**
     * 写入日志信息表
     *
     * @param billId      外部引用ID
     * @param messageType 日志类型
     * @param message     日志信息
     * @param busName     业务类型
     * @return
     */
    public boolean saveMessage(String billId, Integer messageType, String message, String busName, ICurrentUser user) {
        return saveMessage(billId, messageType, message, busName, user.getLoginName(), user.getUsername());
    }
    /**
     * 写入日志信息表
     *
     * @param billId      外部引用ID
     * @param messageType 日志类型
     * @param message     日志信息
     * @param busName     业务类型
     * @param userCode    操作人代码
     * @param userName    操作人名称
     * @return
     */
    public boolean saveMessage(String billId, Integer messageType, String message, String busName, String userCode, String userName) {
        FbsMessage msg = new FbsMessage();
        msg.setBillId(billId);
        msg.setEnterDate(DateUtils.getDate());
        msg.setMessageType(messageType);
        msg.setMessage(message);
        msg.setMessage(message);
        msg.setBusName(busName);
        msg.setOp(userCode);
        msg.setOpName(userName);
        return saveMessage(msg);
    }
    /**
     * 日志列表
     *
     * @param billId  外部引用ID
     * @param busName 业务类型
     * @return
     */
    public List<FbsMessage> listMessage(String billId, String busName) {
        List<FbsMessage> list = fbsMessageDao.listMessage(billId, busName);
        if (EmptyUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        List<TaskResponseDTO> taskResponseDTOS = workFlowService.listAllCommentsByBusinessId(billId);
        if (!EmptyUtils.isEmptyList(taskResponseDTOS)) {
            for (int i = 0; i < taskResponseDTOS.size(); i++) {
                TaskResponseDTO responseDTO = taskResponseDTOS.get(i);
                FbsMessage message = new FbsMessage();
                Integer messageType = FbsMessageConstant.MESSAGE_TYPE_CHECK_PASS;
                if (!"1".equals(responseDTO.getTaskPassFlag()) && !"2".equals(responseDTO.getTaskPassFlag())
                        && !"3".equals(responseDTO.getTaskPassFlag()) && !"4".equals(responseDTO.getTaskPassFlag())) {
                    continue;
                }
                if ("3".equals(responseDTO.getTaskPassFlag()) || "4".equals(responseDTO.getTaskPassFlag())) {
                    messageType = FbsMessageConstant.MESSAGE_TYPE_CHECK_BACK;
                }
                message.setMessageType(messageType);
                message.setMessage(responseDTO.getComment());
                message.setEnterDate(responseDTO.getEndTime());
                UserDTO user = userService.getByUserId(responseDTO.getUserId());
                if (user != null) {
                    message.setOp(user.getLoginName());
                    message.setOpName(responseDTO.getUserName());
                }
                list.add(message);
            }
        }
        Collections.sort(list, new Comparator<FbsMessage>() {
            @Override
            public int compare(FbsMessage msg1, FbsMessage msg2) {
                return msg2.getEnterDate().compareTo(msg1.getEnterDate());
            }
        });
        return list;
    }
}
