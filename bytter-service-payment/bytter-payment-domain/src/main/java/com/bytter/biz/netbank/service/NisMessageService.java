package com.bytter.biz.netbank.service;
import com.bytter.biz.netbank.dao.NisMessageDao;
import com.bytter.biz.netbank.entity.NisMessage;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.dto.TaskResponseDTO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: NIS_MESSAGE
 * @date 2017-06-12 14:53:19
 */
@Service("nisMessageService")
@Transactional
public class NisMessageService extends BaseService<NisMessage, String> {
	@Autowired
	private NisMessageDao nisMessageDao;
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
	public void deleteEntity(NisMessage entity) {
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
	public void save(NisMessage entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(NisMessage entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(NisMessage entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<NisMessage, String> getEntityDao() {
		return nisMessageDao;
	}
	/**
	 * 写入日志信息表
	 *
	 * @param msg
	 * @return
	 */
	public boolean saveMessage(NisMessage msg) {
		if (msg == null || StringUtils.isAllBlank(msg.getBillNo(), msg.getMessage())) {
			return false;
		}
		this.save(msg);
		return true;
	}
	/**
	 * 批量写入日志信息表
	 *
	 * @param msg
	 * @return
	 */
	public boolean saveBatchMessage(List<NisMessage> msglist) {
		if(CollectionUtils.isEmpty(msglist)) {
			return false;
		}
		List<NisMessage> msglistUsefull = new ArrayList<NisMessage>();
		for(NisMessage msg : msglist) {
			if (msg == null || StringUtils.isAllBlank(msg.getBillNo(), msg.getMessage())) {
				continue;
			}else {
				msglistUsefull.add(msg);
			}
		}
		this.batchSave(msglistUsefull, 1000);
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
		NisMessage msg = new NisMessage();
		msg.setBillNo(billNo);
		msg.setEnrolDate(new Date());
		msg.setMessageType(messageType);
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
	public List<NisMessage> listMessage(String billNo, String busType) {
		List<NisMessage> list = nisMessageDao.listMessage(billNo, busType);
		if(EmptyUtils.isEmpty(list)){
			list = new ArrayList<>();
		}
		List<TaskResponseDTO> taskResponseDTOS = workFlowService.listAllCommentsByBusinessId(billNo);
		if(!EmptyUtils.isEmptyList(taskResponseDTOS)){
			for(int i = 0; i < taskResponseDTOS.size(); i++) {
				TaskResponseDTO responseDTO = taskResponseDTOS.get(i);
				NisMessage nisMessage = new NisMessage();
				Integer messageType = NisMessageConstant.MESSAGE_TYPE_CHECK_PASS;
				if(!"1".equals(responseDTO.getTaskPassFlag()) && !"2".equals(responseDTO.getTaskPassFlag())
						&& !"3".equals(responseDTO.getTaskPassFlag()) && !"4".equals(responseDTO.getTaskPassFlag())){
					continue;
				}
				if("3".equals(responseDTO.getTaskPassFlag()) || "4".equals(responseDTO.getTaskPassFlag())){
					messageType = NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
				}else if("2".equals(responseDTO.getTaskPassFlag())){
					messageType = NisMessageConstant.MESSAGE_TYPE_CHECK_REVOKE;
				}
				nisMessage.setMessageType(messageType);
				nisMessage.setMessage(responseDTO.getComment());
				nisMessage.setEnrolDate(responseDTO.getEndTime());
				UserDTO user = userService.getByUserId(responseDTO.getUserId());
				if (user != null) {
					nisMessage.setOp(user.getLoginName());
					nisMessage.setOpName(responseDTO.getUserName());
				}
				list.add(nisMessage);
			}
		}
		Collections.sort(list, new Comparator<NisMessage>() {
			@Override
			public int compare(NisMessage msg1, NisMessage msg2) {
				return msg2.getEnrolDate().compareTo(msg1.getEnrolDate());
			}
		});
		return list;
	}
}
