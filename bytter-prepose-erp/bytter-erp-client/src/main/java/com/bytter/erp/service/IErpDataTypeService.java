package com.bytter.erp.service;
import com.bytter.erp.dto.ErpDataTypeDTO;
import java.util.List;
/**
 * Created by zhouj on 2018/5/21.
 */
public interface IErpDataTypeService {
    /**
     * 获取是否有效的数据类型
     * @param validSign
     * @return
     */
    List<ErpDataTypeDTO> listErpDataTypeByValidSign(String validSign);
    /**
     * 根据code获取数据
     * @param code
     * @return
     */
    ErpDataTypeDTO getErpDataTypeByCode(String code);
}
