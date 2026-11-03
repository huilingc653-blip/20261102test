package com.bytter.system.api;
import com.bytter.system.api.dto.SysCountryDTO;
import java.util.List;
/**
 * 国家表对外接口
 *
 * @author zuoy
 * @date 2020/01/16
 */
public interface ICountryService {
    /**
     * 根据条件查询
     *
     * @param code 国家编码
     * @return Region
     */
    SysCountryDTO getByCode(String code);
    /**
     * 根据条件查询
     *
     * @param continent 国家所属大区
     * @return
     */
    List<SysCountryDTO> listByContinent(String continent);
}
