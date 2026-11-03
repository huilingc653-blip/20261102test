package com.bytter.biz.ttpay.service;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.ttpay.dao.TpRefundOrderApplyDao;
import com.bytter.biz.ttpay.entity.TpRefundOrder;
import com.bytter.biz.ttpay.vo.TpPayBillDtlVO;
import com.bytter.biz.ttpay.vo.TpRefundOrderVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
/**
 * @Title: TpRefundOrderApplyService
 * @Description: 退款申请
 * @author wangzy
 * @date 2019/10/10 10:33
 *
 */
@WorkFlow(TtPayConstant.TP_REFUND_ORDER_APPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = TtPayConstant.TP_REFUND_ORDER_APPLY)
@Service("tpRefundOrderApplyService")
@Transactional
public class TpRefundOrderApplyService extends BaseService<TpRefundOrder, String> implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private TpRefundOrderApplyDao tpRefundOrderApplyDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private TpLogService tpLogService;
    @Autowired
	private IWorkFlowService workFlowService;
    @Autowired
    private TpPayBillDtlService tpPayBillDtlService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<TpRefundOrder, String> getEntityDao() {
        return tpRefundOrderApplyDao;
    }
    /**
     * 退款申请列表查询
     * @param input
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getApplyDataTablesUnion(DataTablesInput input) {
        List<String> corpAuthList = corpService.getUserCorpIds(TtPayConstant.TTPAY_RESOURCE_REFUNDAPPLY_OP,input.getCurrentUser().getId());
        DataTablesOutput data=tpRefundOrderApplyDao.getApplyDataTablesUnion(input,corpAuthList);
        return this.setJoinField(data);
    }
    /**
     * 设置付款办理的关联字段
     * @param output
     * @return
     */
    public DataTablesOutput setJoinField(DataTablesOutput output) {
        if(!EmptyUtils.isEmpty(output.getData())) {
            List<TpPayBillDtlVO> list = output.getData();
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(TtPayConstant.TTPAY_PAYTYPE_DICT,TtPayConstant.TTP_REFUND_APPROVE_STATUS_DICT);
            for (TpPayBillDtlVO vo:list) {
                if (corpMap.containsKey(vo.getCorpId())) {
                    vo.setCorpCode(corpMap.get(vo.getCorpId()).getCode());
                    vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
                }
                //字典注入
                this.injectValues(vo, listDictMap);
            }
        }
        return output;
    }
    /**
     * 字典注入
     *
     * @param vo
     * @param listDictMap
     */
    private void injectValues(TpPayBillDtlVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
        // 支付方式
        Optional.ofNullable(vo.getPayType()).ifPresent(payType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(TtPayConstant.TTPAY_PAYTYPE_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(payType)))
                    .ifPresent(dict -> vo.setPayTypeName(dict.getName()));
        });
        // 退款审批状态
        Optional.ofNullable(vo.getAppStatus()).ifPresent(appStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(TtPayConstant.TTP_REFUND_APPROVE_STATUS_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(appStatus)))
                    .ifPresent(dict -> vo.setAppStatusName(dict.getName()));
        });
    }
    /**
     * 交易流水详情
     * @param id
     * @param payType
     * @return 明细数据
     * @throws
     */
    @Transactional(readOnly = true)
    public Map<String,Object> getPayBillDtlInfoByIdOrPayType(String id, String payType) {
        Map<String,Object> data=tpPayBillDtlService.getPayBillDtlInfoByIdOrPayType(id,payType);
        return data;
    }
    /**
     * 查询交易所有退款
     * @param dtlId
     * @param isApprove 是否只查审批中数据
     * @return 明细数据
     * @throws
     */
    @Transactional(readOnly = true)
    public List<TpRefundOrderVO> listRefundInfo(String dtlId, boolean isApprove) {
        if(EmptyUtils.isNotEmpty(dtlId)){
            return tpRefundOrderApplyDao.listRefundInfo(dtlId, isApprove);
        }
        return null;
    }
    /**
     * 提交退款申请
     * @param tpPayBillDtlVO 流水信息
     * @param user 当前用户
     */
    public void refundApplySubmit(TpPayBillDtlVO tpPayBillDtlVO, ICurrentUser user) {
        Map<String,Object> map = tpPayBillDtlService.getPayBillDtlInfoByIdOrPayType(tpPayBillDtlVO.getId(),tpPayBillDtlVO.getPayType());
        if (tpPayBillDtlVO != null) {
            // 每次提交退款申请即生成一笔退款单数据
            TpRefundOrder tpRefundOrder = new TpRefundOrder();
            // 交易流水号
            if(map.containsKey("transaction_id")){
                tpRefundOrder.setTransactionId(String.valueOf(map.get("transaction_id")));
            }
            // 订单号
            if(map.containsKey("trade_no")){
                tpRefundOrder.setOrderNo(String.valueOf(map.get("trade_no")));
            }
            // 商户号
            if(map.containsKey("mch_id")){
                tpRefundOrder.setMerchantNo(String.valueOf(map.get("mch_id")));
            }else if (map.containsKey("app_id")){
                tpRefundOrder.setMerchantNo(String.valueOf(map.get("app_id")));
            }
            // 交易日期
            if(map.containsKey("trade_time")){
                tpRefundOrder.setOrderDate(DateUtils.parseDate(String.valueOf(map.get("trade_time"))));
            }
            // 对方账户
            if(map.containsKey("pay_acc")){
                tpRefundOrder.setCustAcc(String.valueOf(map.get("pay_acc")));
            }
            // 交易流水Id
            tpRefundOrder.setDtlId(tpPayBillDtlVO.getId());
            // 单位Id
            tpRefundOrder.setCorpId(tpPayBillDtlVO.getCorpId());
            // 退款单号
            String backNo = SequenceSingleton.getSequence(SequenceEnum.TTPAY_REFUND_NO);
            tpRefundOrder.setBackNo(backNo);
            // 退款来源
            tpRefundOrder.setRetSource(TtPayConstant.TTPAY_REFUND_APPLY_BFS);
            // 订单金额
            tpRefundOrder.setOrderMoney(tpPayBillDtlVO.getTrandMoney());
            // 退款方式
            tpRefundOrder.setPayType(tpPayBillDtlVO.getPayType());
            // 退款申请时间
            tpRefundOrder.setBackDate(new Date());
            // 可退款金额
            tpRefundOrder.setRefundableAmount(tpPayBillDtlVO.getTrandMoney().subtract(tpPayBillDtlVO.getRefundedMoney()));
            // 退款申请金额
            tpRefundOrder.setBackMoney(tpPayBillDtlVO.getBackMoney());
            // 申请时间
            tpRefundOrder.setCreateDate(new Date());
            // 申请人名称
            tpRefundOrder.setCreateName(user.getUsername());
            tpRefundOrder.setCreateBy(user.getLoginName());
            // 审批状态为审批中
            tpRefundOrder.setAppStatus(TtPayConstant.TTPAY_REFUND_APPLY_STATUS11);
            // 查询状态为审批中
            tpRefundOrder.setVoucherStat(TtPayConstant.TTPAY_REFUND_APPLY_STATUS11);
            // 退款申请备注
            tpRefundOrder.setRmk(tpPayBillDtlVO.getRmk());
            saveEntity(tpRefundOrder);
            // 挂审批流
             getStartHandler().handler(TtPayConstant.TP_REFUND_ORDER_APPLY, tpRefundOrder.getId(), tpRefundOrder, tpRefundOrder.getCorpId(), user.getId());
            // 生成日志
            tpLogService.saveTpLog(tpRefundOrder.getId(), TtPayConstant.TTPAY_LOGTYPE_SUBMIT, TtPayConstant.COMMON_MESSAGE_SUBMITSUCCESS, user);
        }
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(TtPayConstant.TP_REFUND_ORDER_APPLY, dataTablesInput);
        List<String> businessList=workFlowBusiness.getBusinessIdList();
        DataTablesOutput dp = tpRefundOrderApplyDao.dataTableApprove(dataTablesInput, businessList);
        return workFlowBusiness.merge(dp);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        TpRefundOrder entity = this.get(workFlowResult.getBusinessKey(), true);
        String status = workFlowResult.isPass() ? TtPayConstant.TTPAY_REFUND_APPLY_STATUS95 : TtPayConstant.TTPAY_REFUND_APPLY_STATUS_1;
        entity.setAppStatus(status);
        String logType = "";
        String msg = "";
        if (TtPayConstant.TTPAY_REFUND_APPLY_STATUS95.equals(status)) {
            // 审批通过时，复核状态变为待处理 00，查询装填变为12 待复核
            entity.setStatus(TtPayConstant.TTPAY_REFUND_CHECK_STATUS00);
            entity.setVoucherStat(TtPayConstant.TTPAY_REFUND_VOUCHER_STATUS12);
            logType = TtPayConstant.TTPAY_LOGTYPE_APPROVE;
            msg = TtPayConstant.COMMON_MESSAGE_APPROVESUCCESS;
        } else {
            // 审批打回时，查询装填变为R 审批打回
            entity.setVoucherStat(TtPayConstant.TTPAY_REFUND_VOUCHER_STATUS_R);
            logType = TtPayConstant.TTPAY_LOGTYPE_RETURN;
            msg = TtPayConstant.COMMON_MESSAGE_APPROVEBACK;
        }
        entity.setUpdateDate(DateUtils.getDate());
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateName(user.getUsername());
        this.updateEntity(entity);
        //生成日志
        tpLogService.saveTpLog(entity.getId(), logType, msg, user);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 手机审批查询退款信息
     * @param id 退款ID
     * @return 明细数据
     * @throws
     */
    @Transactional(readOnly = true)
    public TpRefundOrderVO getRefundInfo(String id) {
        if(EmptyUtils.isNotEmpty(id)){
            return tpRefundOrderApplyDao.getRefundInfo(id);
        }
        return null;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<>();
            //获取有效的退款数据
            int num = tpRefundOrderApplyDao.getTpRefundByCorpId(corpId);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.TTPAY,
                        SysConfigConstant.BusType.TTPAY_REFUND,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.TTPAY_VALID_DISABLE_REFUND_ERROR)));
            }
            if(verifyErrorDTOS.size()>0){
                return verifyErrorDTOS;
            }
        }
        return null;
    }
}
