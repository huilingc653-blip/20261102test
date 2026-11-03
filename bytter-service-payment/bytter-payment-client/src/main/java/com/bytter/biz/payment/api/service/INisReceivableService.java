package com.bytter.biz.payment.api.service;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.biz.payment.api.dto.NisReceivableDTO;
import java.util.List;
/**
 * 收款接口类
 * @author wangzx
 * @since 2019-07-05 09:49:44
 */
public interface INisReceivableService {
    /**
     * 收款单生成（供流水认领/ERP接口写）
     *
     * @param entity 收款表
     * @param user 操作用户
     */
    void saveNisReceivable(NisReceivableDTO entity, ICurrentUser user);
    /**
     * 更新收款单（供流水认领）
     *
     * @param billNo       收款单号
     * @param bankSerialNo 银行流水号
     * @param status       状态
     * @param user         用户
     * @param msg          信息
     */
    void updateNisReceivable(String billNo, String bankSerialNo, Integer status, String msg, String busType,ICurrentUser user,String sysBusType,String billId,String busBillNo);
    /**
     * 根据银行流水号查询收款单信息
     *
     * @param bankSerialNo 银行流水号DTO
     * @return 收款单据DTO
     */
    List<NisReceivableDTO> getNisReceivableByBankSerialNo(String bankSerialNo);
    /**
     * 根据id删除收款单
     * @param billId
     */
    void deleteNisReceivable(String billId,ICurrentUser user);
    /**
     * 根据id删除收款单
     *
     * @param billId 业务单据id
     * @param user 当前用户
     * @param triggerBillheadRollback 是否触发反向回删付款单
     */
    void deleteNisReceivable(String billId, ICurrentUser user, boolean triggerBillheadRollback);
    NisReceivableDTO getNisReceivableByBillId(String billId);
}
