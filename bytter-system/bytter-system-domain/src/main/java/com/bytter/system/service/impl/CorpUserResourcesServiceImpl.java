package com.bytter.system.service.impl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpUserResourcesService;
import com.bytter.system.dao.CorpUserResourceDao;
import com.bytter.system.entity.CorpUserResource;
import com.bytter.system.vo.ResourcesSimpleVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * @author zuoy
 */
@Service("CorpUserResourcesServiceI")
@DubboService
public class CorpUserResourcesServiceImpl extends BaseService<CorpUserResource, String> implements  ICorpUserResourcesService{
    @Autowired
    private CorpUserResourceDao corpUserResourceDao;
    public static final String CORP_USER_RESOURCE_CACHE_NAME = "corpUserResourceCache";
    @Override
    public BaseDao<CorpUserResource, String> getEntityDao() {
        return corpUserResourceDao;
    }
    /**
     * 获取当前用户拥有对应单位的资源信息
     *
     * @return
     */
//    @Cacheable(value = CORP_USER_RESOURCE_CACHE_NAME, key = "#userId")
    @Override
    public List<com.bytter.system.api.vo.ResourcesSimpleVO> listCorpResourcesByActiveUser(String userId) {
    	List<ResourcesSimpleVO> list = corpUserResourceDao.listCorpResourcesByActiveUser(userId);
    	List<com.bytter.system.api.vo.ResourcesSimpleVO> listVo = BeanUtils.copyListProperties(list, com.bytter.system.api.vo.ResourcesSimpleVO.class);
        return listVo;
    }
    /**
     * 获取当前用户拥有对应单位的资源信息
     *
     * @param userId
     * @param menuId
     * @return
     */
    @Override
    public List<com.bytter.system.api.vo.ResourcesSimpleVO> listCorpResourcesByActiveUser(String userId, String menuId) {
    	List<ResourcesSimpleVO> list = corpUserResourceDao.listCorpResourcesByActiveUser(userId, menuId);
      	List<com.bytter.system.api.vo.ResourcesSimpleVO> listVo = BeanUtils.copyListProperties(list, com.bytter.system.api.vo.ResourcesSimpleVO.class);
        return listVo;
    }
    /**
     * 根据用户ID和资源权限值获取用户拥有对应单位的资源信息
     * Created by chenlw on 2019/04/29.
     *
     * @param userId
     * @param permission
     * @return
     */
//    @Cacheable(value = CORP_USER_RESOURCE_CACHE_NAME, key = "#userId.concat(#permission)")
    @Override
    public List<com.bytter.system.api.vo.ResourcesSimpleVO> listCorpResourcesByUserIdAndPermission(String userId, String permission) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(permission)) {
            return null;
        }
        permission = permission.substring(0, permission.indexOf(CommonConstant.COLON_SPLIT, permission.indexOf(CommonConstant.COLON_SPLIT) + 1));
        
        List<ResourcesSimpleVO> list = corpUserResourceDao.listCorpResourcesByUserIdAndPermission(userId, permission);
    	List<com.bytter.system.api.vo.ResourcesSimpleVO> listVo = BeanUtils.copyListProperties(list, com.bytter.system.api.vo.ResourcesSimpleVO.class);
        return listVo;
    }
    /**
     * 根据用户Id获取分配单位ID集合
     *
     * @param userId
     * @return
     */
    public List<String> listCorpIdByUserId(String userId) {
        return corpUserResourceDao.listCorpIdByUserId(userId);
    }
    /**
     * 获取所有有序的单位id
     *
     * @return
     */
    public List<String> listAllCorps() {
        return this.corpUserResourceDao.listAllCorps();
    }
    public void batchSaveData(List<CorpUserResource> list) {
        corpUserResourceDao.batchSaveData(list);
    }
}
