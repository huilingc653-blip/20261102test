package com.bytter.biz.payment.api.service;
import com.bytter.biz.payment.api.dto.ErpPayInfoDTO;
/**
 * ERP转移接口
 * @author qijh
 * @since 2019.6.5
 */
public interface IErpPayInfoService {
    /**
     * 更新ERP中间表（付款删除退单反写接口）
     * @param id 外部单号
     * @param busType   业务类型
     */
    void updateErpPayInfo(String id,String busType);
    /**
     * erp标准接口保存
     * @param info 外币单信息
     * @return 单号
     */
    String saveErpPayInfo(ErpPayInfoDTO info);
}
