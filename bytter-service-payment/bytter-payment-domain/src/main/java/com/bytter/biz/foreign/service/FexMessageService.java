package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexMessageDao;
import com.bytter.biz.foreign.entity.FexMessage;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.dto.TaskResponseDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 外汇管理日志表
 * @date 2019-07-15 15:20:33
 */
@Service("fexMessageService")
@Transactional
public class FexMessageService extends BaseService<FexMessage, String> {
    @Autowired
    private FexMessageDao fexMessageDao;

    @Autowired(required = false)
    private IWorkFlowService workFlowService;

    @Autowired(required = false)
    private IUserService userService;

    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(FexMessage entity) {
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
    public void save(FexMessage entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(FexMessage entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(FexMessage entity) {
        super.saveOrUpdate(entity);
    }


    @Override
    public BaseDao<FexMessage, String> getEntityDao() {
        return fexMessageDao;
    }

    /**
     * 写入日志信息表
     *
     * @param msg
     * @return
     */
    public boolean saveMessage(FexMessage msg) {
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
        FexMessage msg = new FexMessage();
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
    public List<FexMessage> listMessage(String billNo, String busType) {
        List<FexMessage> list = fexMessageDao.listMessage(billNo, busType);
        if (EmptyUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        List<TaskResponseDTO> taskResponseDTOS = workFlowService.listAllCommentsByBusinessId(billNo);
        if (!EmptyUtils.isEmptyList(taskResponseDTOS)) {
            for (int i = 0; i < taskResponseDTOS.size(); i++) {
                TaskResponseDTO responseDTO = taskResponseDTOS.get(i);
                FexMessage message = new FexMessage();
                Integer messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_PASS;
                if (!"1".equals(responseDTO.getTaskPassFlag()) && !"2".equals(responseDTO.getTaskPassFlag())
                        && !"3".equals(responseDTO.getTaskPassFlag()) && !"4".equals(responseDTO.getTaskPassFlag())) {
                    continue;
                }
                if ("3".equals(responseDTO.getTaskPassFlag()) || "4".equals(responseDTO.getTaskPassFlag())) {
                    messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_BACK;
                } else if ("2".equals(responseDTO.getTaskPassFlag())) {
                    messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_REVOKE;
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
        Collections.sort(list, new Comparator<FexMessage>() {
            @Override
            public int compare(FexMessage msg1, FexMessage msg2) {
                return msg2.getEnrolDate().compareTo(msg1.getEnrolDate());
            }
        });
        return list;
    }


    /**
     * 获取审批通过的日志列表（升序）
     *
     * @param billNo  外部引用ID
     * @param busType 业务类型
     * @return
     */
    public List<FexMessage> listMessageApprove(String billNo, String busType) {
        List<FexMessage> list = this.listMessage(billNo, busType);
        List<FexMessage> listMsg = new ArrayList<>();
        for (FexMessage msg:list) {
            // 只取审批通过的用户
            if (msg.getMessageType() == FexMessageConstant.MESSAGE_TYPE_CHECK_PASS) {
                listMsg.add(msg);
            }
        }
        Collections.sort(listMsg, new Comparator<FexMessage>() {
            @Override
            public int compare(FexMessage msg1, FexMessage msg2) {
                return msg1.getEnrolDate().compareTo(msg2.getEnrolDate());
            }
        });
        return listMsg;
    }
}
