package com.bytter.biz.foreign.service;

import com.bytter.biz.bdlinks.service.BisForeignExcService;
import com.bytter.biz.foreign.dao.RefundCheckDao;
import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.vo.FexCashFlowVO;
import com.bytter.biz.foreign.vo.RefundCheckVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Service
 * @Description: 外汇退款勾对
 * @author huangx
 * @date 2024-03-8 09:20:33
 * @version 1.0
 *
 */
@Service("refundCheckService")
@Transactional
public class RefundCheckService extends BaseService<RefundCheckVO, String> implements ITaskService {

    @Autowired
    private RefundCheckDao refundCheckDao;
    @Autowired(required=false)
    private IAccountGrantService accountGrantService;
    @Autowired
    private FexCashFlowService fexCashFlowService;
    @Autowired
    private FexTranReqService fexTranReqService;
    @Autowired
    private BisForeignExcService bisForeignExcService;

    @Override
    public BaseDao<RefundCheckVO, String> getEntityDao() {
        return refundCheckDao;
    }

    /**
     * 查询银行账户流水
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        List<String> accList =  accountGrantService.listByMultiValAccIds(user.getId(), ResourceConstant.FOREIGN_REFUNDCHECK_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
        String corpAuthSql = SqlUtil.getInSql(accList, "acc.id", true);
        sql.append(" and ").append(corpAuthSql);
        return this.refundCheckDao.getDataTablesUnion(dataTablesInput, sql.toString());
    }

    /**
     * 根据流水号ID获取流水
     * @param id
     * @return
     */
    public RefundCheckVO getDtlById(String id) {
        return refundCheckDao.getDtlById(id);
    }

    /**
     * 自动勾对
     * @param ids
     * @param refundCheckRule
     * @param refundCheckHandle
     * @param user
     * @return
     */
    public String autoCheck(String ids, String refundCheckRule, String refundCheckHandle, ICurrentUser user){
        String msg = "";
        List<RefundCheckVO> dtlList = refundCheckDao.listDtlByIds(ids.split(CommonConstant.COMMA_SPLIT));
        DataTablesInput input = new DataTablesInput();
        Map<String, RefundCheckVO> tempMap = new HashMap<>();
        Map<String, RefundCheckVO> repeatMap = new HashMap<>();
        Boolean eqg_payMoney = false;
        Boolean eqs_corpBankAcc = false;
        Boolean eqs_cotpBankAcc = false;
        Boolean eqs_transTime = false;
        if(refundCheckRule.contains(ForeignConstant.REFUND_CHECK_RULE_1)){
            eqg_payMoney = true;
        }
        if(refundCheckRule.contains(ForeignConstant.REFUND_CHECK_RULE_2)){
            eqs_corpBankAcc = true;
        }
        if(refundCheckRule.contains(ForeignConstant.REFUND_CHECK_RULE_3)){
            eqs_cotpBankAcc = true;
        }
        if(refundCheckRule.contains(ForeignConstant.REFUND_CHECK_RULE_4)){
            eqs_transTime = true;
        }
        for(int i=0;i<dtlList.size();i++){
            input.getFormdata().put("blendSign", "0");
            input.getFormdata().put("type", "auto");
            if(eqg_payMoney){
                //交易金额的绝对值
                input.getFormdata().put("eqg_payMoney", dtlList.get(i).getAmt().abs());
            }
            if(eqs_corpBankAcc){
                //本方账号=主体账号
                input.getFormdata().put("eqs_corpBankAcc", dtlList.get(i).getBankAcc());
            }
            if(eqs_cotpBankAcc){
                //对方账号=交易对手账号
                input.getFormdata().put("eqs_cotpBankAcc", dtlList.get(i).getOppAccNo());
            }
            if(eqs_transTime){
                //交易日期=结算时间
                input.getFormdata().put("settleDate",DateUtils.formatDate(dtlList.get(i).getTransTime(),"yyyy-MM-dd"));
            }
            //得到付款单
            DataTablesOutput output = this.fexCashFlowService.getDataTablesUnionForRefundCheck(input);
            if(output.getData().size() == 1){
                List<FexCashFlowVO> list = output.getData();
                //银行流水编号为空的才勾兑
                if(StringUtils.isEmpty(list.get(0).getBankSerialNo())) {
                    if(!tempMap.containsKey(list.get(0).getId())) {
                        tempMap.put(list.get(0).getId(), dtlList.get(i));
                    }else{
                        repeatMap.put(list.get(0).getId(), dtlList.get(i));
                    }
                }
            }
        }
        // 获取有转账权限的账户
        Map<String, BtBankAccDTO> bankAccMap = this.getBankAccMap(user.getId());
        Boolean isHasData = false;
        for(String key:tempMap.keySet()){
            RefundCheckVO dtl = tempMap.get(key);
            if(repeatMap.containsKey(key)){
                continue;
            }else{
                // 生成新单，校验用户权限，如果没有转账权限，则跳过
                if(ForeignConstant.REFUND_CHECK_HANDLE_2.equals(refundCheckHandle)
                        || ForeignConstant.REFUND_CHECK_HANDLE_3.equals(refundCheckHandle)){
                    if (!bankAccMap.containsKey(dtl.getBankAcc())) {
                        continue;
                    }
                }
                isHasData = true;
                String bankSerialNo = dtl.getBankSerialNo();
                if (StringUtils.isBlank(dtl.getBankSerialNo())) {
                    // 银行流水号未返回，则写入流水ID
                    bankSerialNo = dtl.getId();
                }
                // 更新流水勾对状态为3-勾对待确认，如果银行流水号为空则同步更新流水号为流水id
                refundCheckDao.updateBlendSign(dtl.getId(), BtBankAccConstant.BLENDSIGN_3, bankSerialNo, "", user);
                // 回写业务流水号（包含现金流表和指令表）
                fexCashFlowService.updateBankSerialNo(key, bankSerialNo, "");
                bisForeignExcService.updateBankSerialNoByCbsBillId(key, bankSerialNo, "");
            }
        }
        if(!isHasData){
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_MERGE_NOT_MERGE);
        }
        return msg;
    }

    /**
     * 获取用户具有转账权限的账户
     * @param userId
     * @return
     */
    public Map<String, BtBankAccDTO> getBankAccMap(String userId) {
        //获取转账权限的银行账号
        List<BtBankAccDTO> listBankAcc = accountGrantService.listDTONotUnionByMultiVal(userId, null, null, null, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);
        Map<String, BtBankAccDTO> bankAccMap = new HashMap<>();
        if (!EmptyUtils.isEmptyList(listBankAcc)) {
            for (BtBankAccDTO bankAcc:listBankAcc) {
                bankAccMap.put(bankAcc.getBankAcc(), bankAcc);
            }
        }
        return bankAccMap;
    }

    /**
     * 构建现金流
     * @param fexCashFlow
     * @param user
     * @return
     */
    public FexCashFlow buildFexCashFlow(FexCashFlow fexCashFlow, ICurrentUser user) {
        FexCashFlow entity = new FexCashFlow();
        BeanUtils.copyNotNullProperties(fexCashFlow, entity);
        entity.setId(null);
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_CASH_FLOW));
        entity.setBillFrom(ForeignConstant.BILL_FROM_4);
        entity.setRelBillNo(fexCashFlow.getBillNo());
        entity.setStatus(CommonConstant.STATUE_TEMP);
        entity.setPayStatus(CommonConstant.NO);
        // 清空流水号
        entity.setBankSerialNo(null);
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        entity.setUpdateBy(null);
        entity.setUpdateDate(null);
        return  entity;
    }

    /**
     * 构建外汇交易
     * @param fexTranReq
     * @param user
     * @return
     */
    public FexTranReq buildFexTranReq(FexTranReq fexTranReq, ICurrentUser user) {
        FexTranReq entity = new FexTranReq();
        BeanUtils.copyNotNullProperties(fexTranReq, entity);
        entity.setId(null);
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_REQ));
        entity.setBillFrom(ForeignConstant.BILL_FROM_4);
        entity.setRelBillNo(fexTranReq.getBillNo());
        entity.setStatus(CommonConstant.STATUE_TEMP);
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        entity.setUpdateBy(null);
        entity.setUpdateDate(null);
        return entity;
    }

    /**
     * 自动确认
     * @param ids
     * @param refundCheckHandle
     * @param user
     * @return
     */
    public void autoConfirm(String ids, String refundCheckHandle, ICurrentUser user){
        // 获取流水信息
        List<RefundCheckVO> dtlList = refundCheckDao.listDtlByIds(ids.split(CommonConstant.COMMA_SPLIT));
        for (RefundCheckVO dtl:dtlList) {
            String bankSerialNo = dtl.getBankSerialNo();
            if (StringUtils.isBlank(dtl.getBankSerialNo())) {
                // 银行流水号未返回，则写入流水ID
                bankSerialNo = dtl.getId();
            }
            //获取现金流
            FexCashFlow fexCashFlow = fexCashFlowService.getByBankSerialNo(dtl.getBankSerialNo());
            if (fexCashFlow != null) {
                if (ForeignConstant.REFUND_CHECK_HANDLE_2.equals(refundCheckHandle)) {
                    // 2-生成新结算执行单
                    fexCashFlowService.saveEntity(this.buildFexCashFlow(fexCashFlow, user));
                } else if (ForeignConstant.REFUND_CHECK_HANDLE_3.equals(refundCheckHandle)) {
                    // 3-生成新交易单
                    FexTranReq fexTranReq = fexTranReqService.get(fexCashFlow.getTranBillId(), true);
                    fexTranReqService.saveEntity(this.buildFexTranReq(fexTranReq, user));
                }
                // 回写业务流水号，更新现金流的支付状态为已退款（包含现金流表和指令表）
                fexCashFlowService.updateBankSerialNo(fexCashFlow.getId(), bankSerialNo, ForeignConstant.PAY_STATUS_REFUNDED);
                bisForeignExcService.updateBankSerialNoByCbsBillId(fexCashFlow.getId(), bankSerialNo, CommonConstant.YES);

                // 同行资金调拨交易需同步修改收款现金流状态
                if (ForeignConstant.REFUND_CHECK_HANDLE_2.equals(refundCheckHandle)
                        || ForeignConstant.REFUND_CHECK_HANDLE_3.equals(refundCheckHandle)) {
                    if (ForeignConstant.TRAN_TYPE_3.equals(fexCashFlow.getTranType())) {
                        FexCashFlow receiptCashFlow = fexCashFlowService.getReceiptCashFlowByTranBillId(fexCashFlow.getTranBillId());
                        // 资金调拨交易同步更新
                        if (receiptCashFlow != null) {
                            fexCashFlowService.updateBankSerialNo(receiptCashFlow.getId(), "", ForeignConstant.PAY_STATUS_REFUNDED);
                        }
                    }
                }
            }
            //更新流水表勾兑标志为1-已勾兑，同步写入处理方式
            refundCheckDao.updateBlendSign(dtl.getId(), BtBankAccConstant.BLENDSIGN_1, bankSerialNo, refundCheckHandle, user);
        }
    }

    /**
     * 取消勾对
     * @param dtl
     * @param user
     * @return
     */
    public void cancel(RefundCheckVO dtl, ICurrentUser user){
        //获取现金流
        FexCashFlow fexCashFlow = fexCashFlowService.getByBankSerialNo(dtl.getBankSerialNo());
        if (fexCashFlow != null) {
            if (ForeignConstant.REFUND_CHECK_HANDLE_2.equals(dtl.getRefundBlendType())) {
                // 2-删除新结算执行单
                FexCashFlow entity = fexCashFlowService.getByRelBillNo(fexCashFlow.getBillNo());
                if (entity != null) {
                    if (entity != null && !CommonConstant.STATUE_TEMP.equals(entity.getStatus()) && !CommonConstant.STATUE_BACK.equals(entity.getStatus())) {
                        throw new ParameterException(ForeignMessageEnum.FEX_REFUNDCHECK_NOTCANCEL_CASHFLOW, entity.getBillNo());
                    }
                    entity.setRelBillNo(null);
                    entity.setStatus(CommonConstant.STATUE_DELETE);
                    entity.setUpdateBy(user.getLoginName());
                    entity.setUpdateDate(DateUtils.getDate());
                    fexCashFlowService.updateEntity(entity);
                }
            } else if (ForeignConstant.REFUND_CHECK_HANDLE_3.equals(dtl.getRefundBlendType())) {
                // 3-删除新交易单
                FexTranReq fexTranReq = fexTranReqService.get(fexCashFlow.getTranBillId(), true);
                FexTranReq entity = fexTranReqService.getByRelBillNo(fexTranReq.getBillNo());
                if (entity != null) {
                    if (entity != null && !CommonConstant.STATUE_TEMP.equals(entity.getStatus()) && !CommonConstant.STATUE_BACK.equals(entity.getStatus())) {
                        throw new ParameterException(ForeignMessageEnum.FEX_REFUNDCHECK_NOTCANCEL_TRAN, entity.getBillNo());
                    }
                    entity.setRelBillNo(null);
                    entity.setStatus(CommonConstant.STATUE_DELETE);
                    entity.setUpdateBy(user.getLoginName());
                    entity.setUpdateDate(DateUtils.getDate());
                    fexTranReqService.updateEntity(entity);
                }
            }
            // 回写业务流水号（包含现金流表和指令表）
            fexCashFlowService.updateBankSerialNo(fexCashFlow.getId(), "", ForeignConstant.PAY_STATUS_1);
            bisForeignExcService.updateBankSerialNoByCbsBillId(fexCashFlow.getId(), "", CommonConstant.NO);

            // 同行资金调拨交易需同步修改收款现金流状态
            if (ForeignConstant.REFUND_CHECK_HANDLE_2.equals(dtl.getRefundBlendType())
                    || ForeignConstant.REFUND_CHECK_HANDLE_3.equals(dtl.getRefundBlendType())) {
                if (ForeignConstant.TRAN_TYPE_3.equals(fexCashFlow.getTranType())) {
                    FexCashFlow receiptCashFlow = fexCashFlowService.getReceiptCashFlowByTranBillId(fexCashFlow.getTranBillId());
                    if (receiptCashFlow != null) {
                        fexCashFlowService.updateBankSerialNo(receiptCashFlow.getId(), "", ForeignConstant.PAY_STATUS_1);
                    }
                }
            }
        }
        //更新流水表勾兑标志为0-未勾兑，同步写入处理方式
        refundCheckDao.updateBlendSign(dtl.getId(), BtBankAccConstant.BLENDSIGN_0, "", "", user);
    }

    /**
     * 批量取消勾对
     * @param ids
     * @param user
     * @return
     */
    public void batchCancel(String ids, ICurrentUser user){
        // 获取流水信息
        List<RefundCheckVO> dtlList = refundCheckDao.listDtlByIds(ids.split(CommonConstant.COMMA_SPLIT));
        for (RefundCheckVO dtl:dtlList) {
            this.cancel(dtl, user);
        }
    }

    /**
     * 作废
     * @param id
     */
    public void defeat(String id, ICurrentUser user) {
        //更新流水表勾兑标志为2-已作废
        refundCheckDao.updateBlendSign(id, BtBankAccConstant.BLENDSIGN_2, "", "", user);
    }

    /**
     * 批量作废
     * @param ids
     */
    public void batchDefeat(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //更新流水表勾兑标志为2-已作废
            refundCheckDao.updateBlendSign(id, BtBankAccConstant.BLENDSIGN_2, "", "", user);
        }
    }

    /**
     * 取消作废
     * @param id
     */
    public void cancelDefeat(String id, ICurrentUser user) {
        //更新流水表勾兑标志为0-未勾对
        refundCheckDao.updateBlendSign(id, BtBankAccConstant.BLENDSIGN_0, "", "", user);
    }

    /**
     * 批量取消作废
     * @param ids
     */
    public void batchCancelDefeat(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.cancelDefeat(id, user);
        }
    }

    /**
     * 手工勾对
     * @param dtlId
     * @param billId
     * @param refundCheckHandle
     * @param opFlag
     * @param user
     */
    public void manualSave(String dtlId, String billId, String refundCheckHandle, String opFlag, ICurrentUser user) {
        RefundCheckVO dtl = this.getDtlById(dtlId);
        if (!CommonConstant.NO.equals(dtl.getBlendSign()) && !"3".equals(dtl.getBlendSign())) {
            return;
        }
        String bankSerialNo = dtl.getBankSerialNo();
        if (StringUtils.isBlank(dtl.getBankSerialNo())) {
            // 银行流水号未返回，则写入流水ID
            bankSerialNo = dtl.getId();
        }
        if (CommonConstant.STATUE_TEMP.toString().equals(opFlag)) {
            //更新流水表勾兑标志，同步写入处理方式
            refundCheckDao.updateBlendSign(dtlId, BtBankAccConstant.BLENDSIGN_3, bankSerialNo, refundCheckHandle, user);
            // 回写业务流水号（包含现金流表和指令表）
            fexCashFlowService.updateBankSerialNo(billId, bankSerialNo, "");
            bisForeignExcService.updateBankSerialNoByCbsBillId(billId, bankSerialNo, "");
        } else {
            //获取现金流
            FexCashFlow fexCashFlow = fexCashFlowService.get(billId);
            if (ForeignConstant.REFUND_CHECK_HANDLE_2.equals(refundCheckHandle)) {
                // 2-生成新结算执行单
                fexCashFlowService.saveEntity(this.buildFexCashFlow(fexCashFlow, user));
            } else if (ForeignConstant.REFUND_CHECK_HANDLE_3.equals(refundCheckHandle)) {
                // 3-生成新交易单
                FexTranReq fexTranReq = fexTranReqService.get(fexCashFlow.getTranBillId(), true);
                fexTranReqService.saveEntity(this.buildFexTranReq(fexTranReq, user));
            }
            // 回写业务流水号,更新支付状态为已退款  （包含现金流表和指令表）
            fexCashFlowService.updateBankSerialNo(fexCashFlow.getId(), bankSerialNo, ForeignConstant.PAY_STATUS_REFUNDED);
            bisForeignExcService.updateBankSerialNoByCbsBillId(fexCashFlow.getId(), bankSerialNo, CommonConstant.YES);
            //更新流水表勾兑标志为1-已勾兑，同步写入处理方式
            refundCheckDao.updateBlendSign(dtl.getId(), BtBankAccConstant.BLENDSIGN_1, bankSerialNo, refundCheckHandle, user);

            // 同行资金调拨交易需同步修改收款现金流状态
            if (ForeignConstant.REFUND_CHECK_HANDLE_2.equals(refundCheckHandle)
                    || ForeignConstant.REFUND_CHECK_HANDLE_3.equals(refundCheckHandle)) {
                if (ForeignConstant.TRAN_TYPE_3.equals(fexCashFlow.getTranType())) {
                    FexCashFlow receiptCashFlow = fexCashFlowService.getReceiptCashFlowByTranBillId(fexCashFlow.getTranBillId());
                    // 资金调拨交易同步更新
                    if (receiptCashFlow != null) {
                        fexCashFlowService.updateBankSerialNo(receiptCashFlow.getId(), "", ForeignConstant.PAY_STATUS_REFUNDED);
                    }
                }
            }
        }
    }

    @Override
    public void run(String arguments) {

    }
}
