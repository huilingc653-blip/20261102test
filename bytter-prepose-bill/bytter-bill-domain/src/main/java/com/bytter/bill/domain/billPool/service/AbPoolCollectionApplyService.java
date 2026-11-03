package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.billPool.dao.AbPoolCollectionApplyDao;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolCollectionApplyVO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * @Title: AbPoolCollectionApplyService
 * @Description: 提示付款Service
 * @author wangzy
 * @date 2020/3/11 10:09
 *
 */
@WorkFlow(BillPoolWorkFlowConstant.AB_POOL_COLLECTION_APPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillPoolWorkFlowConstant.AB_POOL_COLLECTION_APPLY)
@Service("abPoolCollectionApplyService")
@Transactional
public class AbPoolCollectionApplyService  extends BaseService<AbPoolDtl, String> implements IWorkFlowListener {
    @Autowired
    private AbPoolCollectionApplyDao abPoolCollectionApplyDao;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbWorkFlowService abWorkFlowService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Override
    public BaseDao<AbPoolDtl, String> getEntityDao() {
        return abPoolCollectionApplyDao;
    }
    /**
     * 提示付款列表查询
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        String resourceOp = AbPoolConstant.AB_POLL_COLLECTION_APPLY_RESOURCE_OP;
        List<String> corpAuthList = abCorpService.getUserCorpIds(resourceOp,input.getCurrentUser().getId());
        return abPoolCollectionApplyDao.getDataTablesUnion(input, corpAuthList);
    }
    /**
     * 提示付款提交
     * @param abPoolCollectionApplyVO
     * @param user 当前用户
     */
    public AjaxJson abPoolCollectionApplySubmit(AbPoolCollectionApplyVO abPoolCollectionApplyVO, ICurrentUser user) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(false);
        String logType = AccebillConstant.ABLOG_LOGTYPE_SUBMIT;
        String msg = AccebillConstant.LANG_LOG_SUBMIT_SUCC;
        if (StringUtils.isNotEmpty(abPoolCollectionApplyVO.getDtlIds())) {
            String[] idArray = abPoolCollectionApplyVO.getDtlIds().split(",");
            for(String id : idArray){
                AbPoolDtl abPoolDtlOld = this.get(id);
                AbPoolDtl abPoolDtlNew = new AbPoolDtl();
                BeanUtils.copyNotNullProperties(abPoolDtlOld, abPoolDtlNew);
                abPoolDtlNew.setId(null);
                abPoolDtlNew.setLastid(abPoolDtlOld.getId());
                abPoolDtlNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
                abPoolDtlNew.setCreateTime(new Date());
                abPoolDtlNew.setCreateId(user.getId());
                abPoolDtlNew.setStatus(AbPoolConstant.AB_POOL_STATUS11);
                abPoolDtlNew.setBriefId(AbPoolConstant.AB_POOL_BRIEF_020);
                abPoolDtlNew.setOptStatus(null);
                // 保存新的明细表信息
                this.saveEntity(abPoolDtlNew);
                // 修改明细表为无效
                abPoolDtlOld.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
                abPoolDtlOld.setUpdateId(user.getId());
                abPoolDtlOld.setUpdateTime(new Date());
                this.updateEntity(abPoolDtlOld);
                AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abPoolDtlOld.getVvId());
                abVoucherinfoVice.setBackAcid(null);
                abVoucherinfoVice.setBackBriefId(null);
                abVoucherinfoViceService.updateEntity(abVoucherinfoVice);
                List<String> ids = abPoolCollectionApplyDao.listAbAccrecordIds(abPoolDtlOld.getId());
                if(ids == null || ids.size() < 1){
                    return ajaxJson;
                }
                for (String aid : ids){
                    AbAccrecords abAccrecordsOld = abAccrecordsService.get(aid);
                    abAccrecordsOld.setUpdateBy(user.getId());
                    abAccrecordsOld.setUpdateDate(new Date());
                    abAccrecordsOld.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
                    // 修改上一条流水为无效
                    abAccrecordsService.updateEntity(abAccrecordsOld);
                }
                AbAccrecords abAccrecordsOld = abAccrecordsService.get(ids.get(0));
                // 复制旧流水信息
                AbAccrecords abAccrecordsNew = this.getAccrecords(abPoolCollectionApplyVO, abAccrecordsOld, user);
                abAccrecordsNew.setPdId(abPoolDtlNew.getId());
                // 保存新流水
                abAccrecordsService.save(abAccrecordsNew);
                // 挂存入审批流
                 getStartHandler().handler(BillPoolWorkFlowConstant.AB_POOL_COLLECTION_APPLY, abAccrecordsNew.getId(), abVoucherinfoService.get(abVoucherinfoVice.getVid()), abVoucherinfoVice.getCorpId(), user.getId());
                // 生成日志
                abLogService.saveAbLog(abPoolDtlNew.getVvId(), abPoolDtlNew.getId(), AbPoolConstant.AB_POOL_BRIEF_020, logType, msg, user);
            }
            ajaxJson.setSuccess(true);
        }
        return ajaxJson;
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DataTablesOutput listApproveDatatable(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = abWorkFlowService.getWorkFlowBusiness(BillPoolWorkFlowConstant.AB_POOL_COLLECTION_APPLY, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput dp = abPoolCollectionApplyDao.listApproveDatatable(dataTablesInput, businessList);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 根据流水ID查询信息
     * @param dtlId 明细表Id
     * @return
     */
    @Transactional(readOnly = true)
    public AbPoolCollectionApplyVO getAbPoolCollectionApplyVOByDtlId(String dtlId) {
        if (StringUtils.isEmpty(dtlId)){
            return null;
        }
        AbPoolCollectionApplyVO abPoolCollectionApplyVO = abPoolCollectionApplyDao.getAbPoolCollectionApplyVOInfo(dtlId);
        return abPoolCollectionApplyVO;
    }
    /**
     * 继续重发
     * @param aid 流水ID
     * @param user 当前用户
     */
    public AjaxJson continueRemind(String aid, ICurrentUser user) {
        AjaxJson ajaxJson = new AjaxJson();
        if(StringUtils.isNotEmpty(aid)){
            // 改流水和明细为待发送
            AbAccrecords abAccrecords = abAccrecordsService.get(aid);
            if(StringUtils.isNotEmpty(abAccrecords.getPdId())){
                AbPoolDtl abPoolDtl = this.get(abAccrecords.getPdId());
                abPoolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
                abPoolDtl.setUpdateTime(new Date());
                abPoolDtl.setUpdateId(user.getId());
                this.updateEntity(abPoolDtl);
            }
            abAccrecords.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
            abAccrecords.setReturnCode(AccebillConstant.AB_BILL_RETURN_CODE_3);
            abAccrecords.setReturnMsg(null);
            abAccrecords.setUpdateDate(new Date());
            abAccrecords.setUpdateBy(user.getId());
            abAccrecordsService.updateEntity(abAccrecords);
            ajaxJson.setSuccess(true);
        }
        return ajaxJson;
    }
    /**
     * 中止
     * @param aid
     * @param user 当前用户
     */
    public AjaxJson abortDtl(String aid, ICurrentUser user) {
        AjaxJson ajaxJson = new AjaxJson();
        if (StringUtils.isNotEmpty(aid)) {
            // 业务回滚
            AbAccrecords abAccrecords = abAccrecordsService.get(aid);
            // 设置修改信息
            abAccrecords.setUpdateDate(new Date());
            abAccrecords.setUpdateName(user.getUsername());
            abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
            // 当前流水无效
            abAccrecordsService.updateEntity(abAccrecords);
            AbPoolDtl abPoolDtl = this.get(abAccrecords.getPdId());
            List<String> ids = abPoolCollectionApplyDao.listAbAccrecordIds(abPoolDtl.getLastid());
            for(String id : ids){
                AbAccrecords abAccrecordsOld = abAccrecordsService.get(id);
                // 上一笔流水改为有效
                abAccrecordsOld.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
                abAccrecordsOld.setUpdateDate(new Date());
                abAccrecordsOld.setUpdateName(user.getUsername());
                abAccrecordsService.updateEntity(abAccrecordsOld);
            }
            // 上一笔明细改为有效，当前明细无效
            abPoolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
            AbPoolDtl abPoolDtlOld = this.get(abPoolDtl.getLastid());
            abPoolDtlOld.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
            abPoolDtl.setUpdateTime(new Date());
            abPoolDtl.setUpdateId(user.getId());
            abPoolDtlOld.setUpdateId(user.getId());
            abPoolDtlOld.setUpdateTime(new Date());
            // 保存修改信息
            this.updateEntity(abPoolDtl);
            this.updateEntity(abPoolDtlOld);
            ajaxJson.setSuccess(true);
        }
        return ajaxJson;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        AbAccrecords abAccrecords = abAccrecordsService.get(workFlowResult.getBusinessKey());
        AbPoolDtl abPoolDtl = this.get(abAccrecords.getPdId());
        Integer status = workFlowResult.isPass() ? AbPoolConstant.AB_POOL_STATUS95 : AbPoolConstant.AB_POOL_STATUS_1;
        abPoolDtl.setStatus(status);
        String logType = "";
        String msg = "";
        if (AbPoolConstant.AB_POOL_STATUS95.equals(status)) {
            abAccrecords.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
            abPoolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
            abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
            abAccrecords.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
            abAccrecords.setApplyDate(new Date());
            logType = AccebillConstant.ABLOG_LOGTYPE_APP;
            msg = AccebillConstant.LANG_LOG_APP_SUCC;
        } else {
            logType = AccebillConstant.ABLOG_LOGTYPE_REPULSE;
            msg = AccebillConstant.LANG_LOG_APP_BACK;
            // 业务回滚
            AbAccrecords abAccrecordsOld = abAccrecordsService.get(abAccrecords.getLastid());
            // 保存从表审批打回信息
            AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
            abVoucherinfoVice.setBackBriefId(abAccrecords.getBriefId());
            abVoucherinfoVice.setBackAcid(abAccrecords.getId());
            abVoucherinfoVice.setUpdateName(user.getUsername());
            abVoucherinfoVice.setUpdateDate(new Date());
            abVoucherinfoViceService.updateEntity(abVoucherinfoVice);
            // 上一笔流水改为有效，当前流水无效
            abAccrecordsOld.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
            abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
            // 上一笔明细改为有效，当前明细无效
            abPoolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
            AbPoolDtl abPoolDtlOld = this.get(abAccrecordsOld.getPdId());
            abPoolDtlOld.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
            this.updateEntity(abPoolDtlOld);
            abAccrecordsService.updateEntity(abAccrecordsOld);
        }
        abPoolDtl.setUpdateId(user.getId());
        abPoolDtl.setUpdateTime(new Date());
        this.updateEntity(abPoolDtl);
        abAccrecords.setUpdateDate(new Date());
        abAccrecords.setUpdateBy(user.getId());
        abAccrecordsService.updateEntity(abAccrecords);
        //生成日志
        abLogService.saveAbLog(abPoolDtl.getVvId(), abPoolDtl.getId(), AbPoolConstant.AB_POOL_BRIEF_020, logType, msg, user);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 根据流水ID查询信息
     * @param abPoolCollectionApplyVO
     * @param abAccrecordsOld
     * @param userVo
     * @return
     */
    protected AbAccrecords getAccrecords(AbPoolCollectionApplyVO abPoolCollectionApplyVO, AbAccrecords abAccrecordsOld, ICurrentUser userVo) {
        AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abAccrecordsOld.getVid());
        AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecordsOld.getVvid());
        AbAccrecords entity = new AbAccrecords();
        entity.setVid(abAccrecordsOld.getVid());// 票据主表ID
        entity.setVvid(abAccrecordsOld.getVvid());// 票据从表ID
        entity.setLastid(abAccrecordsOld.getId());// 上条流水ID
        entity.setBriefId(AccebillConstant.BRIEF_STATUS_020);// 业务类型
        entity.setApplyDate(new Date());// 申请日期
        entity.setBusinessDate(abPoolCollectionApplyVO.getBusinessDate());// 业务归属日期
        entity.setBillFlag(abVoucherinfo.getBillFlag());
        entity.setSttlmmk(abPoolCollectionApplyVO.getSttlmmk());
        entity.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
        entity.setRsn(abPoolCollectionApplyVO.getRsn());
        entity.setReqRole(abAccrecordsService.getRole(abVoucherinfoVice.getCorpId(), abVoucherinfoVice.getBankAcc(),
                abVoucherinfoVice.getBankName()));// 业务申请人类别
        String corpName =  abCorpService.get(abVoucherinfoVice.getCorpId()).getName();
        entity.setReqNm(corpName);// 业务申请人
        entity.setReqAcctId(abVoucherinfoVice.getBankAcc());// 业务申请人账号
        entity.setReqAcctsvcr(abVoucherinfoVice.getBankCode());// 业务申请人开户行号
        entity.setRecRole(abAccrecordsService.getRole(abVoucherinfo.getAcceptoracc(), abVoucherinfo.getAcceptoracc(),
                null));// 业务接收人类别--(承兑人)
        entity.setRecNm(abVoucherinfo.getAcceptor());// 业务接收人名称
        entity.setRecAcctId(abVoucherinfo.getAcceptoracc());// 业务接收人账号
        entity.setRecAcctsvcr(abVoucherinfo.getAcceptorbankno());// 业务接收人开户行号
        if (userVo != null) {
            entity.setCreateName(userVo.getUsername());// 创建人名称
            entity.setCreateBy(userVo.getLoginName());// 创建人登录名称
        }
        entity.setCreateDate(new Date());// 创建日期
        return entity;
    }
    /**
     * 根据流水ID查询开票信息
     * @param aid 流水表Id
     * @return
     */
    @Transactional(readOnly = true)
    public AbPoolCollectionApplyVO getAbPoolCollectionApplyVOByAid(String aid) {
        if (StringUtils.isEmpty(aid)){
            return null;
        }
        AbAccrecords abAccrecords = abAccrecordsService.get(aid);
        AbPoolCollectionApplyVO abPoolCollectionApplyVO = this.getAbPoolCollectionApplyVOByDtlId(abAccrecords.getPdId());
        return abPoolCollectionApplyVO;
    }
}
