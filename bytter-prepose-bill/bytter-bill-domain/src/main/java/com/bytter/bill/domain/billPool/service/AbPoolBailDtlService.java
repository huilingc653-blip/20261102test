package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbWorkFlowService;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.dao.AbPoolBailDtlDao;
import com.bytter.bill.domain.billPool.entity.AbPool;
import com.bytter.bill.domain.billPool.entity.AbPoolBailDtl;
import com.bytter.bill.domain.billPool.entity.AbPoolif;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolBailDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Title: AbPoolBailDtlService
 * @Description: 保证金管理Service
 * @author wangzy
 * @date 2020/1/10 18:09
 *
 */
@WorkFlow({
        BillPoolWorkFlowConstant.AB_POOL_BAIL_DTL_IN_APPLY,
        BillPoolWorkFlowConstant.AB_POOL_BAIL_DTL_OUT_APPLY})
@Service("abPoolBailDtlService")
@Transactional
public class AbPoolBailDtlService  extends BaseService<AbPoolBailDtl, String> implements IWorkFlowListener {
    @Autowired
    private AbPoolBailDtlDao abPoolBailDtlDao;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbWorkFlowService workFlowService;
    @Autowired
    private AbCorpService corpService;
    @Autowired
    private AbPoolService abPoolService;
    @Autowired
    private AbPoolifService abPoolifService;
    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<AbPoolBailDtl, String> getEntityDao() {
        return abPoolBailDtlDao;
    }
    /**
     * 保证金存入列表查询
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(String type, DataTablesInput input) {
        String bailResource = AbPoolConstant.AB_POLL_BAIL_OUT_RESOURCE_OP;
        // 资源权限
        if(AbPoolConstant.AB_POOL_BAIL_DTL_TYPE_IN.equals(type)){
            bailResource = AbPoolConstant.AB_POLL_BAIL_IN_RESOURCE_OP;
        }
        List<String> corpAuthList = corpService.getUserCorpIds(bailResource,input.getCurrentUser().getId());
        DataTablesOutput<AbPoolBailDtlVO> dataTablesUnion = abPoolBailDtlDao.getDataTablesUnion(type, input, corpAuthList);
        Optional.ofNullable(dataTablesUnion.getData()).ifPresent(listData->{
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_POOL_BAIL_TRANS_TYPE","AB_POOL_STATUS","AB_POOL_OPT_STATUS");
            listData.forEach(item -> redundantValues(listDictMap, item));
        });
        return dataTablesUnion;
    }

    private static void redundantValues(Map<String, Map<String, DictDTO>> listDictMap, AbPoolBailDtlVO item) {
        Optional.ofNullable(listDictMap).ifPresent(listMap -> {
            Optional.ofNullable(item.getType()).flatMap(limtType -> Optional.ofNullable(listMap.get("AB_POOL_LIMT_TYPE"))
                            .flatMap(dictMap -> Optional.ofNullable(dictMap.get(limtType))))
                    .ifPresent(dict -> item.setTypeName(dict.getName()));
            Optional.ofNullable(item.getType()).flatMap(optStatus -> Optional.ofNullable(listMap.get("AB_POOL_OPT_STATUS"))
                            .flatMap(dictMap -> Optional.ofNullable(dictMap.get(optStatus))))
                    .ifPresent(dict -> item.setOptStatusName(dict.getName()));
            Optional.ofNullable(item.getOptStatus()).flatMap(optStatus -> Optional.ofNullable(listMap.get("AB_POOL_OPT_STATUS"))
                            .flatMap(dictMap -> Optional.ofNullable(dictMap.get(optStatus))))
                    .ifPresent(dict -> item.setOptStatusName(dict.getName()));
            Optional.ofNullable(item.getStatus()).flatMap(status -> Optional.ofNullable(listMap.get("AB_POOL_STATUS"))
                            .flatMap(dictMap -> Optional.ofNullable(dictMap.get(String.valueOf(status)))))
                    .ifPresent(dict -> item.setStatusName(dict.getName()));
        });
    }

    /**
     * 保证金账户信息查询
     * @param type 存取类型
     * @param corpId 单位ID
     * @param bpId 票据池ID
     * @return 保证金信息
     */
    @Transactional(readOnly = true)
    public AbPoolBailDtlVO getBailInfo(String type, String corpId, String bpId){
        if (StringUtils.isEmpty(corpId) || StringUtils.isEmpty(bpId)){
            return null;
        }
        if(AbPoolConstant.AB_POOL_BAIL_DTL_TYPE_IN.equals(type)){
            // 兴业银行不支持在线存入
            AbPool abPool = abPoolService.get(bpId);
            AbPoolif abPoolif = abPoolifService.get(abPool.getPifId());
            if (AbPoolConstant.AB_POOL_INTO_APPLE_9015.equals(abPoolif.getBifCode())){
                throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_INBAIL_ERROR);
            }
        }
        AbPoolBailDtlVO bailInfo = abPoolBailDtlDao.getBailInfo(corpId, bpId);
        Optional.ofNullable(bailInfo).ifPresent(bail -> {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_POOL_BAIL_TRANS_TYPE", "AB_POOL_STATUS", "AB_POOL_OPT_STATUS");
            redundantValues(listDictMap, bailInfo);
        });
        return bailInfo;
    }
    /**
     * 保证金存取提交
     * @param abPoolBailDtlVO 流水信息
     * @param user 当前用户
     */
    public AjaxJson bailInfoSubmit(AbPoolBailDtlVO abPoolBailDtlVO, String type, ICurrentUser user) {
        AjaxJson ajaxJson = new AjaxJson();
        if (abPoolBailDtlVO != null) {
            AbPoolBailDtl abPoolBailDtl = new AbPoolBailDtl();
            BeanUtils.copyNotNullProperties(abPoolBailDtlVO, abPoolBailDtl);
            abPoolBailDtl.setType(type);
            abPoolBailDtl.setStatus(AbPoolConstant.AB_POOL_STATUS11);
            abPoolBailDtl.setAppDate(new Date());
            abPoolBailDtl.setCreateId(user.getId());
            abPoolBailDtl.setCreateTime(new Date());
            String workFlowType = null;
            // 挂审批流
            if(AbPoolConstant.AB_POOL_BAIL_DTL_TYPE_IN.equals(type)){
                // 挂存入审批流
                workFlowType = BillPoolWorkFlowConstant.AB_POOL_BAIL_DTL_IN_APPLY;
            }else {
                // 挂提取审批流
                workFlowType = BillPoolWorkFlowConstant.AB_POOL_BAIL_DTL_OUT_APPLY;
            }
            this.saveEntity(abPoolBailDtl);
            ajaxJson.setSuccess(true);
             getStartHandler().handler(workFlowType, abPoolBailDtl.getId(), abPoolBailDtl, abPoolBailDtlVO.getCorpId(), user.getId());
            // 生成日志
            abLogService.saveAbLog(abPoolBailDtl.getBailId(), abPoolBailDtl.getId(), type, AccebillConstant.ABLOG_LOGTYPE_SUBMIT,
                    AccebillConstant.LANG_LOG_SUBMIT_SUCC, user);
        }
        return ajaxJson;
    }
    /**
     * 保证金存取信息查询
     * @param id 流水ID
     * @return 信息
     */
    @Transactional(readOnly = true)
    public AbPoolBailDtlVO getAbPoolBailDtlInfo(String id){
        if (StringUtils.isEmpty(id)){
            return null;
        }
        AbPoolBailDtlVO bailDtlInfo = abPoolBailDtlDao.getAbPoolBailDtlInfo(id);
        Optional.ofNullable(bailDtlInfo).ifPresent(bail -> {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_POOL_BAIL_TRANS_TYPE", "AB_POOL_STATUS", "AB_POOL_OPT_STATUS");
            redundantValues(listDictMap, bailDtlInfo);
        });
        return bailDtlInfo;
    }
    /**
     * 日志信息查询
     * @param id 流水ID
     * @return 信息
     */
    @Transactional(readOnly = true)
    public List<AbLog> listAbLogInfo(String id){
        if (StringUtils.isEmpty(id)){
            return null;
        }
        return abLogService.listAbLog(null,id);
    }
    /**
     * 加载存入审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DataTablesOutput listApproveDatatable(DataTablesInput dataTablesInput, String type) {
        String workFlowType = null;
        if(AbPoolConstant.AB_POOL_BAIL_DTL_TYPE_IN.equals(type)){
            // 保证金存入审批KEY
            workFlowType = BillPoolWorkFlowConstant.AB_POOL_BAIL_DTL_IN_APPLY;
        }else {
            // 保证金提取审批KEY
            workFlowType = BillPoolWorkFlowConstant.AB_POOL_BAIL_DTL_OUT_APPLY;
        }
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(workFlowType, dataTablesInput);
        List<String> businessList=workFlowBusiness.getBusinessIdList();
        DataTablesOutput dp = abPoolBailDtlDao.listApproveDatatable(dataTablesInput, businessList, type);
        return workFlowBusiness.merge(dp);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        AbPoolBailDtl entity = this.get(workFlowResult.getBusinessKey());
        Integer status = workFlowResult.isPass() ? AbPoolConstant.AB_POOL_STATUS95 : AbPoolConstant.AB_POOL_STATUS_1;
        entity.setStatus(status);
        String logType = "";
        String msg = "";
        if (AbPoolConstant.AB_POOL_STATUS95.equals(status)) {
            // 审批通过
            // 指令状态为待发送
            entity.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
            logType = AccebillConstant.ABLOG_LOGTYPE_APP;
            msg = AccebillConstant.LANG_LOG_APP_SUCC;
        } else {
            // 审批打回
            logType = AccebillConstant.ABLOG_LOGTYPE_REPULSE;
            msg = AccebillConstant.LANG_LOG_APP_BACK;
        }
        entity.setUpdateId(user.getId());
        entity.setUpdateTime(new Date());
        this.updateEntity(entity);
        // 生成日志
        abLogService.saveAbLog(entity.getBailId(), entity.getId(), entity.getType(), logType, msg, user);
    }
}
