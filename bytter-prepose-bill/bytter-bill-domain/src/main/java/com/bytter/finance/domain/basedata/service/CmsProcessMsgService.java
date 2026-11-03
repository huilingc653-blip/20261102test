package com.bytter.finance.domain.basedata.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.MapUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.domain.basedata.dao.CmsProcessMsgDao;
import com.bytter.finance.domain.basedata.entity.CmsProcessMsg;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.dto.TaskResponseDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
@Service("cmsProcessMsgService")
@Transactional
public class CmsProcessMsgService extends BaseService<CmsProcessMsg, String> {
    @Autowired
    private CmsProcessMsgDao cmsProcessMsgDao;
    @DubboReference(check = false)
    private IDictService iDictService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IUserService userService;
    /**
     * <b>功能介绍：</b>插入操作信息<br>
     * <b>日期：</b>2017年9月14日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     * @param billCode
     *            单号
     * @param operationType
     *            业务类型
     * @param messageType
     *            消息类型
     * @param msg
     *            消息
     */
    public void insertProcessMsg(String billCode, String operationType, Integer messageType, String msg,
            ICurrentUser user) {
        CmsProcessMsg cmsProcessMsg = new CmsProcessMsg();
        cmsProcessMsg.setBillCode(billCode);
        cmsProcessMsg.setOperationType(operationType);
        cmsProcessMsg.setMessageType(messageType);
        cmsProcessMsg.setMessage(msg);
        if (!EmptyUtils.isEmpty(user)) {
            cmsProcessMsg.setCreateBy(user.getLoginName());
            cmsProcessMsg.setCreateName(user.getUsername());
        }
        cmsProcessMsg.setCreateDate(DateUtils.getDate());
        this.save(cmsProcessMsg);
    }
    /**
     * <b>功能介绍：</b>查询所有日志<br>
     * <b>日期：</b>2017年9月14日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     * @param billCode
     *            多个用逗号隔开
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listMsgData(String billCode, DataTablesInput input) {
        List<CmsProcessMsg> result = Lists.newArrayList();
        int start = input.getStart();
        int draw = input.getDraw();
        input.setStart(0);
        input.setDraw(1);
        Map<String, DictDTO> dictMap = new HashMap<String, DictDTO>();
        String sql = "select d.id,d.type_code as code,d.type_name as name from sys_dict_group g,sys_dict d where g.id=d.type_group_id and g.type_group_code='cms.messageType' ";
        List<Map<String, Object>> list = iDictService.getExtendTableData(sql);
        for (Map<String, Object> map : list) {
            DictDTO dto = new DictDTO();
            // Oracle 返回大写 key，MySQL 保持原样；用大小写不敏感取值避免 NPE
            Object idVal = MapUtils.getMapValueIgnoreCase(map, "ID");
            Object codeVal = MapUtils.getMapValueIgnoreCase(map, "code");
            Object nameVal = MapUtils.getMapValueIgnoreCase(map, "name");
            if (idVal == null || codeVal == null || nameVal == null) {
                continue;
            }
            dto.setId(idVal.toString());
            dto.setCode(codeVal.toString());
            dto.setName(nameVal.toString());
            dictMap.put(nameVal.toString(), dto);
        }
        // DataTablesOutput dataTablesOutput=cmsProcessMsgDao.listMsgData(billCode,input);
        DataTablesOutput dataTablesOutput = cmsProcessMsgDao.listMsgDataWithAsc(billCode, input);
        List<CmsProcessMsg> cmsProcessMsgs = dataTablesOutput.getData();
        if (EmptyUtils.isNotEmpty(cmsProcessMsgs)) {
            for (CmsProcessMsg cmsProcessMsg : cmsProcessMsgs) {
                DictDTO dictDTO = dictMap.get(cmsProcessMsg.getMessage());
                if (dictDTO != null) {
                    cmsProcessMsg.setMessage(dictDTO.getCode());
                }
            }
            List<Integer> workflowOperations = Lists.newArrayList(new Integer(CmsProcessMsgConstant.MSGA_CHECKOK),
                    new Integer(CmsProcessMsgConstant.MSGA_ROLLBACK),
                    new Integer(CmsProcessMsgConstant.MSGA_CHECKBACK));
            for (CmsProcessMsg processMsg : cmsProcessMsgs) {
                if (!workflowOperations.contains(processMsg.getMessageType())) {
                    result.add(processMsg);
                }
            }
        }
        // 添加审批流程日志
        Map<String, String> typeMap = new HashMap<String, String>();
        typeMap.put("cms_loan_send", "58");
        typeMap.put("cms_loan_sendo", "59");
        typeMap.put("cms_loan_info", "76");
        typeMap.put("cms_loan_infoo", "77");
        typeMap.put("cms_loan_repay", "64");
        typeMap.put("cms_loan_repayo", "65");
        typeMap.put("cms_loan_deferred", "96");
        typeMap.put("cms_loan_deferredo", "97");
        typeMap.put("cms_provide_loan_info", "24");
        typeMap.put("cms_provide_loan_infoo", "25");
        typeMap.put("cms_provide_loan_send", "58");
        typeMap.put("cms_provide_loan_sendo", "59");
        typeMap.put("cms_provide_loan_repay", "64");
        typeMap.put("cms_provide_loan_repayo", "65");
        typeMap.put("cms_provide_loan_deferred", "125");
        typeMap.put("cms_provide_loan_deferredo", "126");
        typeMap.put("cms_provide_loan_abolish", "138");
        typeMap.put("cms_provide_loan_abolisho", "138");
        typeMap.put("cms_precative_loan_info", "76");
        typeMap.put("cms_precative_loan_infoo", "77");
        typeMap.put("cms_precative_loan_send", "58");
        typeMap.put("cms_precative_loan_sendo", "59");
        typeMap.put("cms_precative_loan_repay", "64");
        typeMap.put("cms_precative_loan_repayo", "65");
        typeMap.put("cms_precative_loan_deferred", "96");
        typeMap.put("cms_precative_loan_deferredo", "97");
        String[] billCodes = billCode.split(",");
        for (int j = 0; j < billCodes.length; j++) {
            List<Map<Object, Object>> id = cmsProcessMsgDao.getIdByBillCode(billCodes[j]);
            if (EmptyUtils.isEmpty(id)) {
                continue;
            }
            Object sid = id.get(0).get("ID");
            if (!StringUtils.isNotEmpty(sid)) {
                sid = id.get(0).get("id");
            }
            if (!StringUtils.isNotEmpty(sid)) {
                continue;
            }
            // List<TaskResponseDTO> taskResponseDTOS =
            // workFlowService.listAllCommentsByBusinessId(String.valueOf(sid));
            List<TaskResponseDTO> allTaskResponseDTOSForOneBillCode = workFlowService
                    .listAllOperatedCommentsByBusinessId(String.valueOf(sid));
            // 不管一个业务单 经过几次业务处理（非流程操作），经过几个工作流（是几个工作流不是一个工作流中的多个节点）都是 将CmsProcessMsg中工作流的操作剔除，然后加入所有的工作流操作信息，最后通过时间排序
            if (EmptyUtils.isNotEmpty(allTaskResponseDTOSForOneBillCode)) {
                Map<String, String> userId2UserLogin = getTaskUserInfo(allTaskResponseDTOSForOneBillCode);
                List<CmsProcessMsg> workflowProccessMsgsForCurrentOperation = convertToCmsProcessMsg(
                        allTaskResponseDTOSForOneBillCode, typeMap, id, userId2UserLogin);
                result.addAll(workflowProccessMsgsForCurrentOperation);
            }
        }
        /*
         * List<CmsProcessMsg> allList = new ArrayList<>(); if(!EmptyUtils.isEmpty(cmsProcessMsgs)){
         * allList.add(cmsProcessMsgs.get(0)); out: for (int i = 0; i <cmsProcessMsgs.size() ; i++) { CmsProcessMsg p =
         * cmsProcessMsgs.get(i); for (CmsProcessMsg a:allList ) { if(StringUtils.isNotEmpty(p.getCreateBy()) &&
         * p.getCreateBy().equals(a.getCreateBy()) && p.getMessageType().equals(a.getMessageType())){ continue out; } }
         * allList.add(p); } } Collections.sort(allList, new Comparator<CmsProcessMsg>() {
         * 
         * @Override public int compare(CmsProcessMsg msg1, CmsProcessMsg msg2) { return
         * msg2.getCreateDate().compareTo(msg1.getCreateDate()); } });
         */
        Collections.sort(result, new Comparator<CmsProcessMsg>() {
            @Override
            public int compare(CmsProcessMsg msg1, CmsProcessMsg msg2) {
                return msg2.getCreateDate().compareTo(msg1.getCreateDate());
            }
        });
        dataTablesOutput.setRecordsTotal(result.size());
        dataTablesOutput.setRecordsFiltered(result.size());
        dataTablesOutput.setDraw(draw);
        int end = start + input.getLength();
        end = end > result.size() ? start + (result.size() % input.getLength()) : end;
        if (end > start) {
            result = result.subList(start, end);
        }
        dataTablesOutput.setData(result);
        return dataTablesOutput;
    }
    private Map<String, String> getTaskUserInfo(List<TaskResponseDTO> allTaskResponseDTOS) {
        List<String> userIds = Lists.newArrayList();
        for (TaskResponseDTO taskResponseDTO : allTaskResponseDTOS) {
            userIds.add(taskResponseDTO.getUserId());
        }
        List<UserDTO> users = userService.listByUserIds(userIds);
        Map<String, String> userId2UserLogin = Maps.newHashMap();
        if (EmptyUtils.isNotEmpty(users)) {
            for (UserDTO user : users) {
                userId2UserLogin.put(user.getId(), user.getLoginName());
            }
        }
        return userId2UserLogin;
    }
    private List<CmsProcessMsg> convertToCmsProcessMsg(List<TaskResponseDTO> taskResponseDTOS,
            Map<String, String> typeMap, List<Map<Object, Object>> id, Map<String, String> userId2UserLogin) {
        List<CmsProcessMsg> cmsProcessMsgs = Lists.newArrayList();
        if (!EmptyUtils.isEmptyList(taskResponseDTOS)) {
            for (int i = 0; i < taskResponseDTOS.size(); i++) {
                TaskResponseDTO responseDTO = taskResponseDTOS.get(i);
                CmsProcessMsg nisMessage = new CmsProcessMsg();
                Integer messageType = 24;
                String sta = "TA";
                if (EmptyUtils.isEmpty(id.get(0).get("TA"))) {
                    sta = "ta";
                }
                String message = typeMap.get(id.get(0).get(sta));
                if (!"1".equals(responseDTO.getTaskPassFlag()) && !"2".equals(responseDTO.getTaskPassFlag())
                        && !"3".equals(responseDTO.getTaskPassFlag()) && !"4".equals(responseDTO.getTaskPassFlag())) {
                    continue;
                }
                if ("3".equals(responseDTO.getTaskPassFlag()) || "4".equals(responseDTO.getTaskPassFlag())) {
                    messageType = 25;
                    message = typeMap.get(id.get(0).get(sta) + "o");
                } else if ("2".equals(responseDTO.getTaskPassFlag())) {
                    message = "撤回";
                    messageType = 26;
                }
                nisMessage.setMessageType(messageType);
                nisMessage.setMessage(message);
                nisMessage.setCreateDate(responseDTO.getEndTime());
                /*
                 * UserDTO user = userService.getByUserId(responseDTO.getUserId()); if (user != null) {
                 */
                // nisMessage.setCreateBy(user.getLoginName());
                nisMessage.setCreateBy(userId2UserLogin.get(responseDTO.getUserId()));
                nisMessage.setCreateName(responseDTO.getUserName());
                // }
                cmsProcessMsgs.add(nisMessage);
            }
        }
        return cmsProcessMsgs;
    }
    /**
     * 删除对象
     * 
     * @param entity
     */
    public void deleteEntity(CmsProcessMsg entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     * 
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     * 
     * @param entity
     */
    public void save(CmsProcessMsg entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     * 
     * @param entity
     */
    public void update(CmsProcessMsg entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * 
     * @param entity
     */
    public void saveOrUpdate(CmsProcessMsg entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<CmsProcessMsg, String> getEntityDao() {
        return cmsProcessMsgDao;
    }
}
