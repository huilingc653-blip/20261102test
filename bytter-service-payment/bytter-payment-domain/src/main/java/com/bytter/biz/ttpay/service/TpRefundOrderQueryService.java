package com.bytter.biz.ttpay.service;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.ttpay.dao.TpRefundOrderQueryDao;
import com.bytter.biz.ttpay.entity.TpRefundOrder;
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
 * @Title: TpRefundOrderQueryService
 * @Description: 退款查询
 * @author wangzy
 * @date 2019/10/11 9:01
 *
 */
@Service("tpRefundOrderQueryService")
@Transactional
public class TpRefundOrderQueryService extends BaseService<TpRefundOrder, String> {
    @Autowired
    private TpRefundOrderQueryDao tpRefundOrderQueryDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private TpLogService tpLogService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<TpRefundOrder, String> getEntityDao() {
        return tpRefundOrderQueryDao;
    }
    /**
     * 退款申请列表查询
     * @param input
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getQueryDataTablesUnion(DataTablesInput input) {
        List<String> corpAuthList = corpService.getUserCorpIds(TtPayConstant.TTPAY_RESOURCE_REFUNDQUERY_OP,input.getCurrentUser().getId());
        DataTablesOutput data=tpRefundOrderQueryDao.getQueryDataTablesUnion(input,corpAuthList);
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
     * 退款打回
     * @param tpRefundOrderVO 退款信息
     * @param user 当前用户
     */
    public void refundQueryUpdate(TpRefundOrderVO tpRefundOrderVO, ICurrentUser user) {
        if(tpRefundOrderVO != null && StringUtils.isNotEmpty(tpRefundOrderVO.getId())){
            // 打回修改状态
            TpRefundOrder tpRefundOrder = super.get(tpRefundOrderVO.getId(),true);
            tpRefundOrder.setUpdateDate(DateUtils.getDate());
            tpRefundOrder.setUpdateName(user.getUsername());
            tpRefundOrder.setUpdateBy(user.getLoginName());
            // 审批状态为审批通过 95
            tpRefundOrder.setAppStatus(TtPayConstant.TTPAY_REFUND_APPLY_STATUS95);
            // 确认状态为打回 -1
            tpRefundOrder.setStatus(TtPayConstant.TTPAY_REFUND_CHECK_STATUS_1);
            // 查询状态为失败打回 -1
            tpRefundOrder.setVoucherStat(TtPayConstant.TTPAY_REFUND_VOUCHER_STATUS_1);
            // 退款打回原因
            tpRefundOrder.setMsg(tpRefundOrderVO.getMsg());
            super.updateEntity(tpRefundOrder);
            // 生成日志
            tpLogService.saveTpLog(tpRefundOrder.getId(), TtPayConstant.TTPAY_LOGTYPE_RETURN, TtPayConstant.COMMON_MESSAGE_FAILBACK, user);
        }
    }
    /**
     * 退款重发
     * @param id
     * @param user
     * @return boolean
     */
    public boolean reLaunch(String id, ICurrentUser user) {
        if(StringUtils.isEmpty(id)){
            return false;
        }
        TpRefundOrder entity = tpRefundOrderQueryDao.get(id,true);
        // 审批状态为通过 95
        entity.setAppStatus(TtPayConstant.TTPAY_REFUND_APPLY_STATUS95);
        // 将退款确认状态改为通过 95
        entity.setStatus(TtPayConstant.TTPAY_REFUND_CHECK_STATUS95);
        // 查询状态为待发送 3
        entity.setVoucherStat(TtPayConstant.TTPAY_REFUND_VOUCHER_STATUS3);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateName(user.getUsername());
        entity.setUpdateDate(DateUtils.getDate());
        entity.setMsg("");
        this.updateEntity(entity);
        // 保存操作日志
        tpLogService.saveTpLog(id, TtPayConstant.TTPAY_LOGTYPE_UPDATE, TtPayConstant.COMMON_MESSAGE_RELAUNCH, user);
        return true;
    }
}
