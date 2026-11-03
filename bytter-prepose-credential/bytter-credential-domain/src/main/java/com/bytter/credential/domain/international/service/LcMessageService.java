package com.bytter.credential.domain.international.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.domain.international.dao.LcMsgDao;
import com.bytter.credential.domain.international.entity.LcMessage;
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
* @Title: Service
* @Description: CMS_PROCESS_MSG
* @author onlineGenerator
* @date 2017-09-14 14:32:39
* @version 1.0
*
*/
@Service("lcMessageService")
@Transactional
public class LcMessageService extends BaseService<LcMessage,String> {
	@Autowired
	private LcMsgDao lcMsgDao;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private IUserService userService;
	
	/**
	 * <b>功能介绍：</b>插入操作信息<br>
	 * <b>日期：</b>2017年9月14日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param billCode 单号
	 * @param operationType 业务类型
	 * @param messageType 消息类型
	 * @param msg 消息
	 */
	public void insertProcessMsg(String lcId,String billCode,String operationType,Integer messageType,String msg, ICurrentUser user){
		LcMessage lcmsg = new LcMessage();
		lcmsg.setLcId(lcId);
		lcmsg.setBillNo(billCode);
		lcmsg.setMessageType(messageType);
		lcmsg.setMessage(msg);
		if(!EmptyUtils.isEmpty(user)){
			lcmsg.setCreateBy(user.getLoginName());
		}
		lcmsg.setCreateDate(DateUtils.getDate());
		this.save(lcmsg);
	}
	
	/**
	 * <b>功能介绍：</b>查询所有日志<br>
	 * @param billCode 多个用逗号隔开
	 * @return
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput listMsgData(String billCode,DataTablesInput input){
        DataTablesOutput dataTablesOutput=lcMsgDao.listMsgData(billCode,input);
        //添加审批流程日志
        Map<String,String> typeMap=new HashMap<String,String>();
        typeMap.put("LC_ENROL","收到信用证入库审批通过");
        typeMap.put("LC_ENROLO","收到信用证入库审批打回");
        typeMap.put("LC_BILL_ARRIVAL","交单审批通过");
        typeMap.put("LC_BILL_ARRIVALO","交单审批打回");
        typeMap.put("LC_MORTGAGE","申请抵押审批通过");
        typeMap.put("LC_MORTGAGEO","申请抵押审批打回");
        List<LcMessage> list =   dataTablesOutput.getData();
        String[] billCodes = billCode.split(",");
        for (int j = 0; j <billCodes.length ; j++) {
            List<Map<Object, Object>> id = lcMsgDao.getIdByBillCode(billCodes[j]);
            if(!EmptyUtils.isEmpty(id) || StringUtils.isNotEmpty(id.get(0).get("ID")) ){
                List<TaskResponseDTO> taskResponseDTOS = workFlowService.listAllCommentsByBusinessId(String.valueOf(id.get(0).get("ID")));
                if(!EmptyUtils.isEmptyList(taskResponseDTOS)){
                    for(int i = 0; i < taskResponseDTOS.size(); i++) {
                        TaskResponseDTO responseDTO = taskResponseDTOS.get(i);
                        LcMessage lcMessage = new LcMessage();
                        Integer messageType = 24;
                        String message = typeMap.get(id.get(0).get("TA"));
                        if(!"1".equals(responseDTO.getTaskPassFlag()) && !"2".equals(responseDTO.getTaskPassFlag())
                                && !"3".equals(responseDTO.getTaskPassFlag()) && !"4".equals(responseDTO.getTaskPassFlag())){
                            continue;
                        }
                        if("3".equals(responseDTO.getTaskPassFlag()) || "4".equals(responseDTO.getTaskPassFlag())){
                            messageType = 25;
                            message = typeMap.get(id.get(0).get("TA")+"O");
                        }else if("2".equals(responseDTO.getTaskPassFlag())){
                            message = "撤回";
                            messageType = 26;
                        }
                        lcMessage.setMessageType(messageType);
                        lcMessage.setMessage(message);
                        lcMessage.setCreateDate(responseDTO.getEndTime());
                        UserDTO user = userService.getByUserId(responseDTO.getUserId());
                        if (user != null) {
                            lcMessage.setCreateBy(user.getLoginName());
                        }
                        list.add(lcMessage);
                    }
                }
            }
        }
        List<LcMessage> allList = new ArrayList<>();
        if(!EmptyUtils.isEmpty(list)){
            allList.add(list.get(0));
            out:
            for (int i = 0; i <list.size() ; i++) {
                LcMessage p = list.get(i);
                for (LcMessage a:allList ) {
                    if(StringUtils.isNotEmpty(p.getCreateBy()) && p.getCreateBy().equals(a.getCreateBy()) && p.getMessageType().equals(a.getMessageType())){
                        continue out;
                    }
                }
                allList.add(p);
            }
        }
        Collections.sort(allList, new Comparator<LcMessage>() {
            @Override
            public int compare(LcMessage msg1, LcMessage msg2) {
                return msg2.getCreateDate().compareTo(msg1.getCreateDate());
            }
        });
        dataTablesOutput.setData(allList);
        dataTablesOutput.setRecordsTotal(allList.size());
        dataTablesOutput.setRecordsFiltered(allList.size());
		return dataTablesOutput;
	}
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(LcMessage entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(LcMessage entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(LcMessage entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(LcMessage entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<LcMessage, String> getEntityDao() {
		return lcMsgDao;
	}
}
