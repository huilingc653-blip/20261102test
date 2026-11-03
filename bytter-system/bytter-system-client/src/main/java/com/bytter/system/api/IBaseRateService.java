package com.bytter.system.api;
import com.bytter.system.api.dto.TbBaseRateDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
public interface IBaseRateService {
    /** 查询本位币利率类型 */
    List<TbBaseRateDTO> getBaseRateByType(String type);
    /** 通过id查询基础利率列表 */
    List<TbBaseRateDTO> getBaseRateListById(String id);
    /** 通过id查询基础利率信息 */
    TbBaseRateDTO getBaseRateById(String id);
}
