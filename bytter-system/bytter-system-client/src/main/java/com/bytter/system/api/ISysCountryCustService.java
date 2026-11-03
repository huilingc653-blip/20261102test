package com.bytter.system.api;
import com.bytter.system.api.dto.SysCountryCustDTO;
import java.util.List;
/**
 * 国家表对外接口
 *
 * @author huangx
 * @date 2020/01/16
 */
public interface ISysCountryCustService {
    /**
     * 根据中文名称查询国家信息
     *
     * @param nameZh 中文名称
     * @return Region
     */
    SysCountryCustDTO getByNameZh(String nameZh);
    /**
     * 根据国家和中文名称获取地区信息
     *
     * @param nameZh 中文名称
     * @param sysId  国家ID
     * @return Region
     */
    SysCountryCustDTO getByNameZhAndSysId(String nameZh, String sysId);
    /**
     * 查询出全部国家信息
     *
     * @return Region
     */
    List<SysCountryCustDTO> listAll();
    /**
     * 根据ID查询国家信息
     *
     * @param id
     * @return Region
     */
    SysCountryCustDTO getById(String id);
    /**
     * 根据中文名称查询国家信息
     *
     * @param nameZh 中文名称
     * @return Region
     */
    SysCountryCustDTO getByNameZh(String nameZh, String countryLevel);

    /**
     * 根据ID查找国家地区表
     *
     * @param sysId
     * @return
     */
    List<SysCountryCustDTO> listDtoById(String sysId);


    /**
     * 根据简称查询国家信息
     *
     * @param enCode 简称
     * @return Region
     */
    SysCountryCustDTO getByEnCode(String enCode);

    SysCountryCustDTO getByNameZhLevelTop(String nameZh);
}
