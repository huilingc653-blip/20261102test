package com.bytter.credential.api.service;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.credential.api.dto.GuaContractInternationalDTO;
import com.bytter.credential.api.dto.LcBillArrivalDTO;
import java.util.List;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-07-24 11:53
 * @Version 1.0
 */
public interface ILcEnrolService {
    /**
     * 挂接担保
     *
     * @return
     * @Param busId
     */
    void updateLcGuaById(String busId);
    /**
     * 删除挂接担保
     *
     * @return
     * @Param busId
     */
    void deleteLcGuaById(String busId);
    /**
     * 银行流水认领挂接信用证，标记待到期收款数据
     *
     * @return
     * @Param id
     */
    void addBillArrivalMark(String id);
    /**
     * 银行流水认领挂接信用证，删除已标记待到期收款数据
     *
     * @return
     * @Param id
     */
    void deletBillArrivalMark(String id);
    /**
     * 通过交单登记id获取交单登记数据
     *
     * @param id
     * @return LcBillArrivalDTO
     */
    LcBillArrivalDTO getBillArrivalById(String id);
    /**
     * 获取挂接授信的信用证ID集合
     * @return
     */
    List<String> getLcEnrolIdByRat();
    /**
     * 获取所有挂接担保的信用证数据
     */
    List<GuaContractInternationalDTO> getGuaForInternational();
}
