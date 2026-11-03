package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbBillDeleteDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbBillDeleteVO;
import com.bytter.bill.domain.bill.vo.AbEmitBillVo;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Service
 * @Description: 票据删除
 * @date 2020-06-01
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLDELETE)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ABBILLDELETE)
@Service("abBillDeleteService")
@Transactional
public class AbBillDeleteService extends BaseService<AbVoucherinfo, String>
        implements IAccebillBaseService, IWorkFlowListener {
    @Autowired
    private AbBillDeleteDao abBillDeleteDao;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbEmitBillService abEmitBillService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbWorkFlowService abWorkFlowService;
    @Autowired
    private AbBillNotificationService abBillNotificationService;
    @Autowired
    private AbReceiveBillService abReceiveBillService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @Override
    public BaseDao<AbVoucherinfo, String> getEntityDao() {
        return abBillDeleteDao;
    }
    /**
     * 加载票据删除列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbBillDelete(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_DELETE_QUERY, dataTablesInput.getCurrentUserId());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput,AccebillConstant.AB_BILL_RESOURCE_DELETE_QUERY,corpIds);
        return abBillDeleteDao.listAbBillDelete(dataTablesInput, corpAuthList);
    }
    /**
     * 票据删除提交
     *
     * @param deleteVO
     */
    public void deleteConfirm(AbBillDeleteVO deleteVO, ICurrentUser userVo) {
        // 获取需要删除的条数
        int len = deleteVO.getIds().split(",").length;
        // 批量修改主表box_status处理标记为处理中
        int num = abVoucherinfoViceService.batchSubmit(deleteVO.getVvids().split(","), userVo);
//        if (num != len) {
//            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
//        }
        // 批量插入流水表数据
        List<AbAccrecords> abList = getAbAccrecords(deleteVO);
        num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
        if (num != len) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        String[] aids = deleteVO.getIds().split(",");
        for (String id : aids) {
            //修改上一条流水valid_sign=2
            abAccrecordsService.updateStatusByLastId(id, userVo, AccebillConstant.AB_BILL_BUS_STATUS_2);
        }
        abLogService.batchSaveAbLog(deleteVO.getVvids().split(","), deleteVO.getIds().split(","),
                AccebillConstant.BRIEF_STATUS_9004, AccebillConstant.ABLOG_LOGTYPE_SUBMIT,
                AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
        for (AbAccrecords abs : abList) {
            abBillNotificationService.completeMsg(abs, userVo);
            abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());
            AbBillDeleteVO variables = new AbBillDeleteVO();
            BeanUtils.copyNotNullProperties(abs.getVouInfo(), variables);
            BeanUtils.copyNotNullProperties(abs, variables);
             getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLDELETE, abs.getId(), variables,
                    abs.getVouViceInfo().getApplyCorpId(), userVo.getId());
        }
    }
    /**
     * 审批操作
     *
     * @param buId     主表id数组
     * @param isPassOK 通过标识
     * @param appMsg   审批信息
     */
    public void approvalPass(String buId, boolean isPassOK, String appMsg, ICurrentUser userVo) {
        if (StringUtils.isEmpty(buId)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbAccrecords info = abAccrecordsService.get(buId, true);
        if (info == null) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        // 批量修改票据表信息从(修改处理标记为处理完成)
        int num = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y, userVo);
        if (num != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 修改流水表数据（修改status状态为95：审批通过、修改VALID_SIGN为1：可用）
        Integer status = isPassOK ? AccebillConstant.AB_BILL_BUS_STATUS95 : AccebillConstant.AB_BILL_BUS_STATUS_2;
        int appNum = abAccrecordsService.approve(info.getId(), status, userVo);
        if (appNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 通过则上一流水valid_sign=2 失败修改BackBriefId
        if (isPassOK) {
            String[] lastids = new String[]{info.getLastid()};
            abAccrecordsService.batchAppPass(lastids, isPassOK, userVo);
            //迁移流水表数据到流水历史表中
            abBillDeleteDao.moveAccrecordsSql(info.getVvid());
            //迁移从表数据到从表历史表中
            abBillDeleteDao.moveVoucherinfoViceSql(info.getVvid());
            List<AbVoucherinfoVice> vvice = abVoucherinfoViceService.getVoucherinfoViceByVidAndVvid(info.getVid(), info.getVvid());
            if (vvice == null) {
                abBillDeleteDao.moveVoucherinfoSql(info.getVid());
                abVoucherinfoService.delete(info.getVid());
            }
            //根据vvid物理删除从表数据
            abVoucherinfoViceService.deleteByVvId(info.getVvid());
            //根据vvid物理删除流水表数据
            abAccrecordsService.deleteByVvId(info.getVvid());
        } else {
            int reNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
            if (reNum != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
        }
        String logType = isPassOK ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
        abLogService.saveAbLog(info.getVvid(), info.getId(), AccebillConstant.BRIEF_STATUS_9004, logType, appMsg,
                userVo);
    }
    /**
     * 获取流水信息
     *
     * @param deleteVO
     * @return
     */
    protected List<AbAccrecords> getAbAccrecords(AbBillDeleteVO deleteVO) {
        List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
        // 流水表id数组
        String[] aids = deleteVO.getIds().split(",");
        for (String id : aids) {
            // 获取流水表信息
            AbAccrecords abAccrecords = abAccrecordsService.get(id, true);
            // 获取当前票据主，从表信息
            AbVoucherinfo ab = abVoucherinfoService.get(abAccrecords.getVid(), true);
            AbVoucherinfoVice abVice = abVoucherinfoViceService.get(abAccrecords.getVvid(), true);
            // 新的流水数据
            AbAccrecords entity = new AbAccrecords();
            // 票据主表ID
            entity.setVid(ab.getId());
            // 票据从表ID
            entity.setVvid(abVice.getId());
            entity.setVouInfo(ab);
            entity.setVouViceInfo(abVice);
            // 业务处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
            entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
            entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
            // 业务类型
            entity.setBriefId(AccebillConstant.BRIEF_STATUS_9004);
            entity.setLastid(id);
            entity.setLastbillcode(ab.getBillStatus());
            // 申请日期
            entity.setBusinessDate(new Date());// 业务归属日期
            entity.setApplyDate(new Date());// 申请日期
            if (ab.getBillFlag() == 11) {
                entity.setReqAcctId(abVice.getBankAcc());
            }
            entity.setBankendrsmtmk(abAccrecords.getBankendrsmtmk());
            entity.setBillFlag(ab.getBillFlag());
            abList.add(entity);
        }
        return abList;
    }
    /**
     * 根据流水查询票据信息
     *
     * @param aid 流水ID
     * @return
     */
    @Transactional(readOnly = true)
    public AbReceiveBillVO getAbReceiveBillVoByAid(String aid) {
        return abReceiveBillService.getAbReceiveBillVOById(aid);
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(readOnly = true)
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = abWorkFlowService
                .getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ABBILLDELETE, dataTablesInput);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput dp = abBillDeleteDao.dataTableApprove(dataTablesInput, businessList);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 工作流启动回调方法(无需关注)
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // Auto-generated method stub
    }
    /**
     * 审批回调方法
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
        approvalPass(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
    }
    @Override
    @Transactional(readOnly = true)
    public AbVoucherinfoVO getAbBillInfoById(String aid) {
        return this.getAbReceiveBillVoByAid(aid);
    }
    /**
     * 根据流水查询开票信息
     *
     * @param aid 流水ID
     * @return
     */
    @Transactional(readOnly = true)
    public AbEmitBillVo getAbEmitBillVoByAid(String aid) {
        return abEmitBillService.getAbEmitBillVoByAid(aid, false);
    }
}
