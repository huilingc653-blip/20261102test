package com.bytter.system.api;
import com.bytter.system.api.vo.ResourcesSimpleVO;
import java.util.List;
/**
 * @author zuoy
 */
public interface ICorpUserResourcesService{
   
    public List<ResourcesSimpleVO> listCorpResourcesByActiveUser(String userId) ;
    /**
     * 获取当前用户拥有对应单位的资源信息
     *
     * @param userId
     * @param menuId
     * @return
     */
    public List<ResourcesSimpleVO> listCorpResourcesByActiveUser(String userId, String menuId);
    /**
     * 根据用户ID和资源权限值获取用户拥有对应单位的资源信息
     * Created by chenlw on 2019/04/29.
     *
     * @param userId
     * @param permission
     * @return
     */
//    @Cacheable(value = CORP_USER_RESOURCE_CACHE_NAME, key = "#userId.concat(#permission)")
    public List<ResourcesSimpleVO> listCorpResourcesByUserIdAndPermission(String userId, String permission);
    /**
     * 根据用户Id获取分配单位ID集合
     *
     * @param userId
     * @return
     */
    public List<String> listCorpIdByUserId(String userId) ;
    /**
     * 获取所有有序的单位id
     *
     * @return
     */
    public List<String> listAllCorps();
}
