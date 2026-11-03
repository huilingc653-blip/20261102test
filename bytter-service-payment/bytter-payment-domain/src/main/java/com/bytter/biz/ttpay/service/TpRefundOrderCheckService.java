package com.bytter.biz.ttpay.service;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.ttpay.dao.TpRefundOrderCheckDao;
import com.bytter.biz.ttpay.entity.TpRefundOrder;
import com.bytter.biz.ttpay.vo.TpPayBillDtlVO;
import com.bytter.biz.ttpay.vo.TpRefundOrderVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * @Title: TpRefundOrderAuditService
 * @Description: 退款确认
 * @author wangzy
 * @date 2019/10/11 9:02
 *
 */
@Service("tpRefundOrderCheckService")
@Transactional
public class TpRefundOrderCheckService extends BaseService<TpRefundOrder, String> {
    @Autowired
    private TpRefundOrderCheckDao tpRefundOrderCheckDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private TpLogService tpLogService;
    @Autowired
    private TpPayBillDtlService tpPayBillDtlService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<TpRefundOrder, String> getEntityDao() {
        return tpRefundOrderCheckDao;
    }
    /**
     * 退款确认列表查询
     * @param input
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getCheckDataTablesUnion(DataTablesInput input) {
        List<String> corpAuthList = corpService.getUserCorpIds(TtPayConstant.TTPAY_RESOURCE_REFUNDCHECK_OP,input.getCurrentUser().getId());
        DataTablesOutput data=tpRefundOrderCheckDao.getCheckDataTablesUnion(input,corpAuthList);
        return this.setJoinField(data);
    }
    /**
     * 设置付款办理的关联字段
     * @param output
     * @return
     */
    public DataTablesOutput setJoinField(DataTablesOutput output) {
        if(!EmptyUtils.isEmpty(output.getData())) {
            List<TpRefundOrderVO> list = output.getData();
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(TtPayConstant.TTPAY_PAYTYPE_DICT,
                    TtPayConstant.TTP_REFUND_APPROVE_STATUS_DICT,TtPayConstant.TTPAY_REFUND_CHECKSTATUS_DICT);
            for (TpRefundOrderVO vo:list) {
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
    private void injectValues(TpRefundOrderVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
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
        // 退款审批状态
        Optional.ofNullable(vo.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(TtPayConstant.TTPAY_REFUND_CHECKSTATUS_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status)))
                    .ifPresent(dict -> vo.setStatusName(dict.getName()));
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
        Map<String,Object> data = tpPayBillDtlService.getPayBillDtlInfoByIdOrPayType(id,payType);
        return data;
    }
    /**
     * 查询交易所有退款
     * @param id
     * @return 明细数据
     * @throws
     */
    @Transactional(readOnly = true)
    public List<TpRefundOrderVO> listRefundInfo(String id) {
        if(StringUtils.isEmpty(id)){
            return null;
        }
        List<TpRefundOrderVO> list=tpRefundOrderCheckDao.listRefundInfo(id);
        return list;
    }
    /**
     * 退款确认通过或打回
     * @param ids
     * @param opType 确认操作（通过或打回）
     * @param user
     * @return boolean
     */
    public boolean batchCheckOrBack(String ids, String opType, String refundMsg, ICurrentUser user) {
        if(StringUtils.isEmpty(ids)){
            return false;
        }
        List<String> listId = StringUtils.splitToList(",",ids);
        String appStatus = "";
        String status = "";
        String voucherStat = "";
        String msg = "";
        String logType = TtPayConstant.TTPAY_LOGTYPE_CHECK;
        if (StringUtils.equals(opType,TtPayConstant.TTPAY_REFUND_OPTYPE_CHECK)) {
            // 审批状态为通过 95
            appStatus = TtPayConstant.TTPAY_REFUND_APPLY_STATUS95;
            // 将退款确认状态改为通过 95
            status = TtPayConstant.TTPAY_REFUND_CHECK_STATUS95;
            // 查询状态为待发送 3
            voucherStat = TtPayConstant.TTPAY_REFUND_VOUCHER_STATUS3;
            msg = TtPayConstant.COMMON_MESSAGE_CHECKPASS;
        } else {
            // 审批状态为通过 95
            appStatus = TtPayConstant.TTPAY_REFUND_APPLY_STATUS95;
            // 将退款确认状态改为打回 -1
            status = TtPayConstant.TTPAY_REFUND_CHECK_STATUS_1;
            // 查询状态为确认打回 -2
            voucherStat = TtPayConstant.TTPAY_REFUND_VOUCHER_STATUS_2;
            msg = TtPayConstant.COMMON_MESSAGE_CHECKBACK;
        }
        for(String id:listId){
            TpRefundOrder entity = tpRefundOrderCheckDao.get(id,true);
            entity.setAppStatus(appStatus);
            entity.setStatus(status);
            entity.setVoucherStat(voucherStat);
            // 确认人信息
            entity.setCheckDate(DateUtils.getDate());
            entity.setCheckBy(user.getLoginName());
            entity.setCheckName(user.getUsername());
            entity.setUpdateBy(user.getLoginName());
            entity.setUpdateName(user.getUsername());
            entity.setUpdateDate(DateUtils.getDate());
            entity.setMsg(refundMsg);
            this.updateEntity(entity);
            // 保存操作日志
            tpLogService.saveTpLog(id, logType, msg, user);
        }
        return true;
    }
}
