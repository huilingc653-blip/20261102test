package com.bytter.system.api;
import com.bytter.system.api.dto.RegionDTO;
import java.util.List;
import java.util.Map;
/**
 * 省市管理对外接口
 *
 * @author zhoul
 * @date 2017/7/31
 */
public interface IRegionService {
    /**
     * 根据条件查询
     *
     * @param parentId 父类ID
     * @param name     当前省市名称
     * @return Region
     */
    RegionDTO getCityBy(String parentId, String name);
    /**
     * 根据名称查询
     *
     * @param name 当前查询省市名称
     * @param prov null - 全部 turn - 省  false - 市
     * @return
     * @author zuoy 2018-05-23
     */
    RegionDTO getByName(String name, Boolean prov);
    /**
     * 模糊查找省份信息
     *
     * @param provName
     * @return
     */
    RegionDTO getRegionByLikeProvName(String provName);
    /**
     * 获取所有省份
     *
     * @return
     */
    List<RegionDTO> listProvAll();
    /**
     * 获取省份下面的城市
     *
     * @return
     */
    List<RegionDTO> listCityByParentId(String parentId);
    RegionDTO getById(String id);
    /**
     * 获取省市信息
     *
     * @return
     */
    Map<String, RegionDTO> getRegionMap();
    /**
     * 根据id集合获取省市信息
     *
     * @param regionIds 省市id集合
     * @return 省市信息 key = id ，value = object
     */
    Map<String, RegionDTO> listByIds(List<String> regionIds);

    /**
     * @Desc：根据id查询记录
     * @param ids
     * @return
     */
    List<RegionDTO> findByIds(List<String> ids);

    Map<String, RegionDTO> findRegionMapByIds(List<String> ids);
}
