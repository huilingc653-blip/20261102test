package com.bytter.biz.inneraccount.service;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.NisWorkFlowConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.BtBankAccMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.AttachmentDTO;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.enums.BankInfoMessageEnum;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
//import com.bytter.biz.inneraccount.entity.BtBankAccAppCur;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行账号变更
 * @date 2017-05-23 17:37:39
 */
@WorkFlow(NisWorkFlowConstant.BT_BANKACC_REV)
@DubboService(interfaceClass = IWorkFlowListener.class, group = NisWorkFlowConstant.BT_BANKACC_REV)
@Service("btBankAccRevService")
@Transactional
public class BtBankAccRevService implements IWorkFlowListener {
    @DubboReference(check = false)
    private IBtBankAccRevService btBankAccRevService;
    @DubboReference(check = false)
    private IBtBankAccAppService btBankAccAppService;
    @Autowired
	private IWorkFlowService workFlowService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBtBankService bankService;
    @DubboReference(check = false)
	private IBtBankAccMessageService btBankAccMessageService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
	private IBtBankAccCommonService btBankAccCommonService;
    @DubboReference(check = false)
	private IBtBankAccAppCurService btBankAccAppCurService;
    @DubboReference(check = false)
    private IAttachmentService attachmentService;
    @DubboReference(check = false)
    private IUserService userService;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(BtBankAccAppDTO entity, ICurrentUser user) {
        // 删除相关联的币别中间表
        /*List<BtBankAccAppCurVO> appCurList = this.btBankAccCommonService.listBtBankAccAppCurById(entity.getId());
        List<com.bytter.biz.inneraccount.entity.BtBankAccAppCur> list = BeanUtils.copyListProperties(appCurList, com.bytter.biz.inneraccount.entity.BtBankAccAppCur.class);
        for (int i = 0; i < list.size(); i++) {
            this.btBankAccAppCurService.deleteEntity(list.get(i));
        }
        super.deleteEntity(entity);*/
        this.btBankAccAppCurService.delByAppId(entity.getId());
        notificationService.complete(NotificationConstant.NETBANK.BANKACC_REV, entity.getId(), user.getId());
    }
    /**
     * 保存
     *
     * @param entity
     */
    public BtBankAccAppDTO save(BtBankAccAppDTO entity) {
        // 初始化联行号信息
        this.bankService.saveBtBank(entity.getBankCode(), entity.getBankTypeId());
        entity = this.btBankAccAppService.save(entity);
        return entity;
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BtBankAccAppDTO entity) {
        // 初始化联行号信息
        this.bankService.saveBtBank(entity.getBankCode(), entity.getBankTypeId());
        btBankAccAppService.saveOrUpdate(entity);
    }
    /**
     * 显示列表
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCREV_QUERY, user.getId()), "acc.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = this.btBankAccRevService.getDataTablesUnion(dataTablesInput, sql.toString());
        return output;
    }
    public void toModify(BtBankAccAppDTO btBankAccApp, ICurrentUser user) {
        String curIdStr = btBankAccApp.getCurId();
        if (EmptyUtils.isEmpty(curIdStr)) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,"","");
        }
        String[] curIds = curIdStr.split(",");
        if (curIds.length < 1) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,"","");
        }
        BtBankAccAppDTO t = null;
        if (BtBankAccConstant.FORM_APP.equals(btBankAccApp.getIsFrom())) {
            t = btBankAccCommonService.getBtBankAccApp(btBankAccApp.getId());
        } else {
            t = new BtBankAccAppDTO();
        }
        
        BeanUtils.copyNotNullProperties(btBankAccApp, t);
        if ("0".equals(btBankAccApp.getFlag())) {
            t.setStatus(BtBankAccConstant.STATUE_TEMP);
        } else {
            t.setStatus(BtBankAccConstant.STATUE_WAIT_CHECK);
        }
        //账号变更申请
        t.setBusType(BtBankAccConstant.BUSTYPE_2);
        t.setCurId("");
        if (BtBankAccConstant.FORM_APP.equals(btBankAccApp.getIsFrom())) {
            t.setUpdateBy(user.getLoginName());
            t.setUpdateDate(DateUtils.getDate());
            this.btBankAccAppService.saveOrUpdate(t);
        }else{
            t.setId("");
            t.setAccNo(this.btBankAccAppService.getSequence(SequenceEnum.ACC_NO));
            t.setCreateBy(user.getLoginName());
            t.setCreateDate(DateUtils.getDate());
            t = this.save(t);
        }
        /*for (int i = 0; i < curIds.length; i++) {
            BtBankAccAppCurVO accCur = new BtBankAccAppCurVO();
            if ("all".equals(curIds[i])) {
                continue;
            }
            BtBankAccAppCurVO cur = this.btBankAccCommonService.getBtBankAccAppCur(t.getId(), curIds[i]);
            if (EmptyUtils.isEmpty(cur)) {
                accCur.setAppId(t.getId());
                accCur.setCurId(curIds[i]);
                this.getEntityDao().save(accCur);
            }
        }*/
        btBankAccAppCurService.saveByAppId(t.getId(), curIds);
        if (BtBankAccConstant.STATUE_TEMP.equals(t.getStatus())) {
            //生成日志
            btBankAccMessageService.saveMessage(t.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_ADD, "", BtBankAccMessageConstant.BUS_TYPE_REV, user);
        } else {
            //生成日志
            btBankAccMessageService.saveMessage(t.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_REV, user);
             getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_REV, t.getId(), t, t.getCorpId(), user.getId());
        }
        notificationService.complete(NotificationConstant.NETBANK.BANKACC_REV, t.getId(), user.getId());
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        BtBankAccAppDTO bankAccApp = this.btBankAccAppService.getById(workFlowResult.getBusinessKey());
        Integer status = workFlowResult.isPass() ? BtBankAccConstant.STATUE_CHECK : BtBankAccConstant.STATUE_BACK;
        bankAccApp.setStatus(status);
        bankAccApp.setUpdateBy(user.getLoginName());
        bankAccApp.setUpdateDate(DateUtils.getDate());
        this.btBankAccAppService.saveOrUpdate(bankAccApp);
        Integer messageStatus = workFlowResult.isPass() ? BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_PASS : BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        //生成日志
        btBankAccMessageService.saveMessage(bankAccApp.getId(), messageStatus, "", BtBankAccMessageConstant.BUS_TYPE_REV, user);
        // 消息提醒
        notificationService.send(bankAccApp.getId(), bankAccApp.getCorpId(), NotificationConstant.NETBANK.BANKACC_REV_HANDLE, bankAccApp);
    }
    /**
     * 删除
     *
     * @param id
     * @param user
     */
    public void delete(String id, ICurrentUser user) {
        BtBankAccAppDTO btBankAccApp = this.btBankAccAppService.getById(id);
        this.btBankAccAppService.deleteEntity(btBankAccApp);
        // 消息提醒
        notificationService.complete(NotificationConstant.NETBANK.BANKACC_REV, id, user.getId());
    }
    /**
     * 银行账号变更查询列表
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput getChangeDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_ACCCHANGE_QUERY, user.getId()), "acc.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = btBankAccRevService.getChangeDataTablesUnion(dataTablesInput, sql.toString());
        this.setChangeQueryInfo(output);
        return output;
    }
    /**
     * 设置账号变更查询中查询信息
     * @param  output
     */
    public void setChangeQueryInfo(DataTablesOutput output){
        List<BtBankAccAppDTO> list = output.getData();
        if (!EmptyUtils.isEmpty(list)){
            for (int i = 0;i < list.size();i++) {
                //附件数
                List<AttachmentDTO> attachements =  attachmentService.findAttachmentByGroupId(list.get(i).getAttachmentId());
                if (!EmptyUtils.isEmpty(attachements)) {
                    list.get(i).setAttachmentNum(attachements.size());
                } else {
                    list.get(i).setAttachmentNum(0);
                }
                //变更人
                if (StringUtils.isNotEmpty(list.get(i).getUpdateBy())){
                    UserDTO userDTO = userService.getByLoginName(list.get(i).getUpdateBy());
                    if (!EmptyUtils.isEmpty(userDTO)){
                        list.get(i).setUpdateByName(userDTO.getUsername());
                    }
                }
                if (StringUtils.isNotEmpty(list.get(i).getCreateBy())){
                    UserDTO userDTO = userService.getByLoginName(list.get(i).getCreateBy());
                    if (!EmptyUtils.isEmpty(userDTO)){
                        list.get(i).setCreateByName(userDTO.getUsername());
                    }
                }
            }
            output.setData(list);
        }
    }
}
