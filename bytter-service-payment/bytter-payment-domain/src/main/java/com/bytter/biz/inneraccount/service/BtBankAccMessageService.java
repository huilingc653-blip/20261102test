package com.bytter.biz.inneraccount.service;

import com.bytter.biz.inneraccount.entity.BtBankAccMessage;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.NisMessageConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccMessageService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.BtBankAccMessageDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.dto.TaskResponseDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
* @Title: Service
* @Description: btBankAccMessageService
* @author onlineGenerator
* @date 2017-06-12 14:53:19
* @version 1.0
*
*/
@Service("btBankAccMessageService")
@Transactional
public class BtBankAccMessageService{
	@DubboReference(check = false)
	IBtBankAccMessageService iBtBankAccMessageService;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private IUserService userService;
	/**
	* 删除对象
	* @param dto
	*/
	public void deleteEntity(BtBankAccMessageDTO dto){
		iBtBankAccMessageService.deleteEntity(dto);
	}
	/**
	* 根据id删除
	* @param id
	*/
	/*public void delete(String id){

		iBtBankAccMessageService.delete(id);
 	}*/
	/**
	* 保存
	* @param dto
	*/
 	public void save(BtBankAccMessageDTO dto){
		iBtBankAccMessageService.save(dto);
 	}
	/**
	* 更新
	* @param dto
	*/
	public void update(BtBankAccMessageDTO dto){
		iBtBankAccMessageService.update(dto);
	}
	/**
	* 保存or更新
	* @param dto
	*/
	public void saveOrUpdate(BtBankAccMessageDTO dto){
		iBtBankAccMessageService.saveOrUpdate(dto);
 	}
	/**
	 * 写入日志信息表
	 * @param dto
	 * @return
	 */
	public boolean saveMessage(BtBankAccMessageDTO dto){
		return iBtBankAccMessageService.saveMessage(dto);
	}
	
	/**
	 * 写入日志信息表
	 * @param billNo 外部引用ID
	 * @param messageType 日志类型
	 * @param message 日志信息
	 * @param busType 业务类型
	 * @return
	 */
	public boolean saveMessage(String billNo, Integer messageType, String message, String busType, ICurrentUser user){
		return saveMessage(billNo, messageType, message, busType, user.getLoginName(), user.getUsername());
	}
	
	/**
	 * 写入日志信息表
	 * @param billNo 外部引用ID
	 * @param messageType 日志类型
	 * @param message 日志信息
	 * @param busType 业务类型
	 * @param userCode 操作人代码
	 * @param userName 操作人名称
	 * @return
	 */
	public boolean saveMessage(String billNo, Integer messageType, String message, String busType, String userCode, String userName){
		BtBankAccMessageDTO dto = new BtBankAccMessageDTO();
		dto.setBillNo(billNo);
		dto.setEnrolDate(new Date());
		dto.setMessageType(messageType);
		dto.setMessage(message);
		dto.setMessage(message);
		dto.setBusType(busType);
		dto.setOp(userCode);
		if(StringUtils.isEmpty(userName)){
			userName = userCode;
		}
		dto.setOpName(userName);
		return saveMessage(dto);
	}
	/**
	 * 写入日志信息表
	 * @param billNo 外部引用ID
	 * @param messageType 日志类型
	 * @param message 日志信息
	 * @param busType 业务类型
	 * @return
	 */
	public BtBankAccMessage buildMessage(String billNo, Integer messageType, String message, String busType, ICurrentUser user){
		return buildMessage(billNo, messageType, message, busType, user.getLoginName(), user.getUsername());
	}
	/**
	 * 写入日志信息表
	 * @param billNo 外部引用ID
	 * @param messageType 日志类型
	 * @param message 日志信息
	 * @param busType 业务类型
	 * @param userCode 操作人代码
	 * @param userName 操作人名称
	 * @return
	 */
	public BtBankAccMessage buildMessage(String billNo, Integer messageType, String message, String busType, String userCode, String userName){
		BtBankAccMessage msg = new BtBankAccMessage();
		msg.setBillNo(billNo);
		msg.setEnrolDate(new Date());
		msg.setMessageType(messageType);
		msg.setMessage(message);
		msg.setMessage(message);
		msg.setBusType(busType);
		msg.setOp(userCode);
		msg.setOpName(userName);
		if(StringUtils.isAllBlank(msg.getBillNo(),msg.getMessage())){
			return null;
		}
		return msg;
	}
	/**
	 * 日志列表
	 * @param billNo 外部引用ID
	 * @param busType 业务类型
	 * @return
	 */
	public List<BtBankAccMessage> listMessage(String billNo, String busType){
		List<BtBankAccMessageDTO> list = iBtBankAccMessageService.listMessage(billNo, busType);
		if(EmptyUtils.isEmpty(list)){
			list = new ArrayList<>();
		}
		List<TaskResponseDTO> taskResponseDTOS = workFlowService.listAllCommentsByBusinessId(billNo);
		if(!EmptyUtils.isEmptyList(taskResponseDTOS)){
			for(int i = 0; i < taskResponseDTOS.size(); i++) {
				TaskResponseDTO responseDTO = taskResponseDTOS.get(i);
				BtBankAccMessageDTO dto = new BtBankAccMessageDTO();
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
				dto.setMessageType(messageType);
				dto.setMessage(responseDTO.getComment());
				dto.setEnrolDate(responseDTO.getEndTime());
				UserDTO user = userService.getByUserId(responseDTO.getUserId());
				if (user != null) {
					dto.setOp(user.getLoginName());
					dto.setOpName(responseDTO.getUserName());
				}
				list.add(dto);
			}
		}
		Collections.sort(list, new Comparator<BtBankAccMessageDTO>() {
			@Override
			public int compare(BtBankAccMessageDTO dto1, BtBankAccMessageDTO dto2) {
				return dto2.getEnrolDate().compareTo(dto1.getEnrolDate());
			}
		});
		return BeanUtils.copyListProperties(list,BtBankAccMessage.class);
	}
}
