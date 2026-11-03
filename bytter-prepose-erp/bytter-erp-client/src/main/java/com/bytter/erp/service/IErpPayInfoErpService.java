package com.bytter.erp.service;
import com.bytter.erp.dto.ErpPayInfoDTO;
/**
 * ERP转移接口
 * @author qijh
 * @since 2019.6.5
 */
public interface IErpPayInfoErpService {
    /**
     * 更新ERP中间表（付款删除退单反写接口）
     * @param id 外部单号
     * @param busType   业务类型
     */
    void updateErpPayInfo(String id, String busType);
    /**
     * erp标准接口保存
     * @param info 外币单信息
     * @return 单号
     */
    String saveErpPayInfo(ErpPayInfoDTO info);
    
    ErpPayInfoDTO getById(String id);
    
    void updateEntity(ErpPayInfoDTO dto);
    
    String saveEntity(ErpPayInfoDTO dto);
}
