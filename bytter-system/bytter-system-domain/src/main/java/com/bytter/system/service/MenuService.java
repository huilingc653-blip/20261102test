package com.bytter.system.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.jqgrid.TreeGridResponse;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.RedisUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.utils.TreeUtil;
import com.bytter.system.api.ILicenseService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.constant.MenuConstant;
import com.bytter.system.api.enums.MenuMessageEnum;
import com.bytter.system.dao.CorpUserResourceDao;
import com.bytter.system.dao.MenuDao;
import com.bytter.system.dao.ResourcesDao;
import com.bytter.system.dao.RoleMenuDao;
import com.bytter.system.entity.CorpUserResource;
import com.bytter.system.entity.Menu;
import com.bytter.system.entity.Resources;
import com.bytter.system.vo.MenuIndexVO;
import com.bytter.system.vo.MenuSimpleVO;
import com.bytter.system.vo.ResourcesSimpleVO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.bytter.core.cache.redis.core.RedisCacheKey.URL_PERMISSION_MAPPING_ALL;

/**
 * Created by zhoul on 2017/4/12.
 */

@Transactional
@Service("menuService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MenuService extends BaseService<Menu, String> {


    @Autowired
    private MenuDao dao;

    @Autowired
    private ResourcesDao resourcesDao;

    @Autowired
    private CorpUserResourceDao corpUserResourceDao;

    @Autowired
    private RoleMenuDao roleMenuDao;

    @Autowired
    private ILicenseService licenseServiceImpl;

    @Autowired
    private Ii18nService i18nServiceI;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public BaseDao<Menu, String> getEntityDao() {
        return dao;
    }

    public void save(Menu menu, String userId,String tenantId) {
        validateParent(menu);
        menu.setCreateBy(userId);
        menu.setCreateTime(new Date());
        if (menu.getSort() == null) {
            menu.setSort(500);
        }
        saveEntity(menu);
        // 清除缓存
        clearMenuCache(tenantId);
    }

    /**
     * 校验上级菜单
     *
     * @param menu
     */
    private void validateParent(Menu menu) {
        if (StringUtils.isNotBlank(menu.getParentId())) {
            //当保存的菜单为标准菜单时，上一级不能是二开菜单
            if (!MenuConstant.IS_EXTEND_MENU.equals(menu.getIsExtend())) {
                Menu parentMenu = getEntityDao().get(menu.getParentId());
                if (MenuConstant.IS_EXTEND_MENU.equals(parentMenu.getIsExtend())) {
                    throw new BusinessException(MenuMessageEnum.SYS_MENU_PARENT_CAN_NOT_BE_EXTEND);
                }
            }
        }
    }

    /**
     * 校验菜单是否为license中的标准菜单，如果为标准菜单，则不允许设置成二开菜单
     *
     * @param menu
     */
    private void validateMenu(Menu menu) {
        if (licenseServiceImpl.getMenus().contains(menu.getId()) && MenuConstant.IS_EXTEND_MENU.equals(menu.getIsExtend())) {
            throw new BusinessException(MenuMessageEnum.SYS_MENU_CAN_NOT_BE_EXTEND);
        }
    }

    public void clearMenuCache(String tenantId){
        String key = RedisCacheKey.makeRedisKey("", URL_PERMISSION_MAPPING_ALL,tenantId);
        redisUtils.remove(key);
    }

    public void update(Menu menu, String userId,String tenantId) {
        validateParent(menu);
        validateMenu(menu);
        menu.setUpdateBy(userId);
        menu.setUpdateTime(new Date());
        if (menu.getSort() == null) {
            menu.setSort(500);
        }
        merge(menu);
        // 清除缓存
        clearMenuCache(tenantId);
    }


    public void delete(String id,String tenantId) {
        //获取持久化状态菜单
        Menu menu = get(id);
        //删除角色和菜单的关联关系
        dao.deleteRoleMenu(id);
        //以下循环是删除资源与角色的关系
        for (Resources resources : menu.getResources()) {
            resourcesDao.delete(resources.getId());
        }
        //删除资源与菜单的关系
        menu.getResources().clear();
        //删除菜单，转换为游离态
        dao.delete(menu);
        // 清除缓存
        clearMenuCache(tenantId);
    }

    public List<Menu> getTree() {
        return listToTree(dao.listAll());
    }

    /**
     * 获取所有的菜单，排序为树形顺序机构
     *
     * @return
     */
    public List<Menu> getTreeTable() {
        List<Menu> menus = dao.listAll();
        /*for (Menu menu : menus) {
            String baseContext = i18nServiceI.getBaseContext(menu.getId());
            evict(menu);
            if (StringUtils.isNotBlank(baseContext)) {
                menu.setName(baseContext);
            }
        }*/

        if (menus != null && !menus.isEmpty()) {
            // 提取所有菜单ID用于批量国际化处理
            List<String> menuIds = new ArrayList<>();
            for (Menu menu : menus) {
                menuIds.add(menu.getId());
            }

            // 一次性批量获取所有国际化内容
            Map<String, String> baseContextMap = i18nServiceI.getBaseContextBatch(menuIds);

            // 应用国际化内容到菜单
            for (Menu menu : menus) {
                String baseContext = baseContextMap.get(menu.getId());
                if (StringUtils.isNotBlank(baseContext)) {
                    menu.setName(baseContext);
                }
                evict(menu); // 确保在主线程中调用evict以保持线程安全
            }
        }
//        return listToTreeTable(menus);
        List<Menu> list2 = convertMenusToTreeTable(menus);
        return list2;
    }

    /**
     * 优化版本的树形表格转换，使用Map预处理提升性能
     * 时间复杂度从O(n²)降低到O(n)
     *
     * @param menus 菜单列表
     * @return 转换为树形表格结构的菜单列表
     */
    public List<Menu> convertMenusToTreeTable(List<Menu> menus) {
        if (menus == null || menus.isEmpty()) {
            return new ArrayList<>();
        }

        // 构建父子关系映射，时间复杂度O(n)
        Map<String, List<Menu>> parentChildrenMap = new HashMap<>();
        List<Menu> rootMenus = new ArrayList<>();

        // 首先构建父子关系映射
        for (Menu menu : menus) {
            String parentId = menu.getParentId();
            if (StringUtils.isBlank(parentId) || "0".equals(parentId.trim())) {
                // 根节点
                rootMenus.add(menu);
            } else {
                // 非根节点，添加到对应父节点的子节点列表中
                parentChildrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(menu);
            }
            // 初始化叶子节点标记
            menu.setIsLeaf(true);
        }

        // 使用深度优先遍历构建树形表格结构
        List<Menu> result = new ArrayList<>();
        buildTreeTableWithMap(rootMenus, parentChildrenMap, result, 0);

        return result;
    }

    /**
     * 递归构建树形表格结构
     *
     * @param menus             当前层级的菜单列表
     * @param parentChildrenMap 父子关系映射
     * @param result            结果列表
     * @param level             当前层级
     */
    private void buildTreeTableWithMap(List<Menu> menus, Map<String, List<Menu>> parentChildrenMap, List<Menu> result, int level) {
        if (menus == null || menus.isEmpty()) {
            return;
        }

        for (Menu menu : menus) {
            // 设置层级信息
            menu.setLevel(level);

            // 查找当前节点的子节点
            List<Menu> children = parentChildrenMap.get(menu.getId());
            if (children != null && !children.isEmpty()) {
                // 不是叶子节点
                menu.setIsLeaf(false);
                // 添加当前节点到结果中
                result.add(menu);
                // 递归处理子节点
                buildTreeTableWithMap(children, parentChildrenMap, result, level + 1);
            } else {
                // 是叶子节点
                menu.setIsLeaf(true);
                // 添加当前节点到结果中
                result.add(menu);
            }
        }
    }

    /**
     * 获取所有的菜单，排序为树形顺序机构
     *
     * @return
     */
    public TreeGridResponse getTreeTable1() {
        List<Menu> menus = dao.listAll();
        /*for (Menu menu : menus) {
            String baseContext = i18nServiceI.getBaseContext(menu.getId());
            evict(menu);
            if (StringUtils.isNotBlank(baseContext)) {
                menu.setName(baseContext);
            }
        }*/

        if (menus != null && !menus.isEmpty()) {
            // 提取所有菜单ID用于批量国际化处理
            List<String> menuIds = new ArrayList<>();
            for (Menu menu : menus) {
                menuIds.add(menu.getId());
            }

            // 一次性批量获取所有国际化内容
            Map<String, String> baseContextMap = i18nServiceI.getBaseContextBatch(menuIds);

            // 应用国际化内容到菜单
            for (Menu menu : menus) {
                String baseContext = baseContextMap.get(menu.getId());
                if (StringUtils.isNotBlank(baseContext)) {
                    menu.setName(baseContext);
                }
                evict(menu); // 确保在主线程中调用evict以保持线程安全
            }
        }
//        List<Menu> list = listToTreeTable(menus);
        List<Menu> list = convertMenusToTreeTable(menus);
        TreeGridResponse treeGridResponse = new TreeGridResponse<>(list);
        return treeGridResponse;
    }

    /**
     * 获取“启用”状态的菜单
     *
     * @return
     */
    public List<Menu> getEnableTreeTable() {
        return listToTreeTable(dao.listBy("status", MenuConstant.STATUS_ENABLE));
    }

    /**
     * 根据ID查找子节点<br>
     * 周亮<br>
     * 2017年6月8日<br>
     *
     * @param parentId
     * @return
     */
    public List<MenuSimpleVO> listByParentId(String parentId) {
        return dao.listByParentId(parentId);
    }

    /**
     * 根据父节点ID查找节点，受license控制
     *
     * @param parentId
     * @return
     */
    public List<MenuSimpleVO> listLicenseControlByParentId(String parentId) {
        List<MenuSimpleVO> voList = dao.listByParentId(parentId);
        List<MenuSimpleVO> tempList = new ArrayList<>();
        if (!ILicenseService.LICENSE_TYPE_DEVELOP.equalsIgnoreCase(licenseServiceImpl.getLicenseType())) {
            List<String> licenseMenus = licenseServiceImpl.getMenus();
            for (MenuSimpleVO menuSimpleVO : voList) {
                if (licenseMenus.contains(menuSimpleVO.getId()) || MenuConstant.IS_EXTEND_MENU.equals(menuSimpleVO.getIsExtend())) {
                    tempList.add(menuSimpleVO);
                }
            }
        } else {
            tempList = voList;
        }
        return tempList;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTreeAndResources(String id, String type) {
        List<String> licenseMenus = licenseServiceImpl.getMenus();
        Menu topMenu = get(id);
        List<Menu> menus = findChildList(id);
        menus.add(topMenu);
        List<Map<String, Object>> list = new ArrayList<>();
        boolean onlyMenu = StringUtils.isNotBlank(type) && "menu".equals(type);
        for (Menu menu : menus) {
            if (!ILicenseService.LICENSE_TYPE_DEVELOP.equalsIgnoreCase(licenseServiceImpl.getLicenseType()) &&
                    !onlyMenu && !licenseMenus.contains(menu.getId()) && menu.getIsExtend()!=null && MenuConstant.IS_EXTEND_MENU != menu.getIsExtend()) {
                continue;
            }
            String menuName = i18nServiceI.getBaseContext(menu.getId());
            Map<String, Object> map = new HashMap<>();
            map.put("id", menu.getId());
            map.put("parentId", menu.getParentId());
            map.put("name", StringUtils.isBlank(menuName) ? menu.getName() : menuName);
            map.put("sort", menu.getSort());
            map.put("isMenu", true);
            list.add(map);
            if (!onlyMenu) {
                //查询菜单下面的资源 以前是通过hibernate关联处理 todo 批量处理
                List<Resources> resourcesList = resourcesDao.listByMenuId(menu.getId());
                if (resourcesList.isEmpty()) {
                    continue;
                }
                for (Resources r : resourcesList) {
                    if (MenuConstant.STATUS_UNAGBLE.equals(r.getStatus())) {
                        continue;
                    }
                    String resourceName = i18nServiceI.getBaseContext(r.getId());
                    Map<String, Object> mapResources = new HashMap<>();
                    mapResources.put("id", r.getId());
                    mapResources.put("parentId", menu.getId());
                    mapResources.put("name", StringUtils.isBlank(resourceName) ? r.getName() : resourceName);
                    mapResources.put("isMenu", false);
                    mapResources.put("sort", 0);//设置资源优先于下级菜单先展示 zhengg 20200827
                    list.add(mapResources);
                }
            }
        }
        return list;
    }

    public List<MenuSimpleVO> listByMenuList(List<Menu> menuList) {
        return dao.listByMenuList(menuList);
    }

    public List<MenuSimpleVO> listByMenuId(List<String> menuList) {
        return dao.listByMenuId(menuList);
    }

    /**
     * 获取菜单的层级关系
     *
     * @param id
     * @return
     */
    public String getParentIds(String id) {
        StringBuilder sb = new StringBuilder();
        getParentIds(id, sb);
        return sb.toString();
    }

    /**
     * 获取菜单的层级关系
     *
     * @param id
     * @return
     */
    private void getParentIds(String id, StringBuilder sb) {
        Menu menu = get(id, true);
        if (menu == null) {
            return;
        }
        if (menu.getParentId() != null && !"".equals(menu.getParentId())) {
            sb.insert(0, menu.getId());
            sb.insert(0, ",");
            getParentIds(menu.getParentId(), sb);
        } else {
            sb.insert(0, menu.getId());
        }
    }

    public List<Menu> findChildList(String id) {
        List<Menu> list = new ArrayList<>();
        findChildList(id, list);
        return list;
    }

    private void findChildList(String id, List<Menu> menuList) {
        if (menuList == null) {
            menuList = new ArrayList<>();
        }
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("parentId", id));
        criteria.add(Restrictions.eq("status", 1));
        criteria.setCacheable(true);
        List<Menu> childList = criteria.list();
        if (childList != null && !childList.isEmpty()) {
            for (Menu menuc : childList) {
                findChildList(menuc.getId(), menuList);
            }
            menuList.addAll(childList);
        }
    }

    public List<Menu> listByMenuIds(String locale, List<String> ids) {
        if (EmptyUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return dao.listByMenuIds(locale, ids);
    }

    public Menu getMenuById(String menuId) {
        return dao.get(menuId, true);
    }

    public List<MenuSimpleVO> listMenusByUserId(String userId) {
        return dao.listMenusByUserId(userId);
    }

    public List<String> listFirstLayerMenuIdsByUserId(String userId) {
        return dao.listFirstLayerMenuIdsByUserId(userId);
    }

    public List<String> listMenuIdsByUserIdAndParentIds(String userId, Set<String> parentIds) {
        return dao.listMenuIdsByUserIdAndParentIds(userId, parentIds);
    }

    /**
     * 根据用户ID，获取该用户所拥有的角色菜单和资源
     *
     * @param userId 用户ID
     * @return
     */
    public List<Map<String, Object>> findUserMenuAndResourceByUserId(String userId) {
        List<Map<String, Object>> menuAndResource = new ArrayList<>();
        List<MenuSimpleVO> menuList = dao.listMenusByUserId(userId);
        if (menuList == null || menuList.isEmpty()) {
            return menuAndResource;
        }
        List<MenuSimpleVO> treeNodeList = TreeUtil.getTreeNodeList(menuList);
        List<ResourcesSimpleVO> resourcesList = resourcesDao.listResourceByUserId2(userId);

        Map<String, String> childrenMap = new HashMap<>();
        for (MenuSimpleVO menuSimpleVO : treeNodeList) {
            childrenMap.put(menuSimpleVO.getId(), menuSimpleVO.getParentId());
        }

        List<String> resourceMenus = new ArrayList<>();
        for (MenuSimpleVO menuSimpleVO : treeNodeList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", menuSimpleVO.getId());
            map.put("parentId", menuSimpleVO.getParentId());
            map.put("name", menuSimpleVO.getName());
            map.put("isMenu", true);
            menuAndResource.add(map);
            if (resourcesList == null) {
                continue;
            }
            for (ResourcesSimpleVO resourcesSimpleVO : resourcesList) {
                if (menuSimpleVO.getId().equals(resourcesSimpleVO.getMenuId())) {
                    Map<String, Object> mapResources = new HashMap<>();
                    mapResources.put("id", resourcesSimpleVO.getId());
                    mapResources.put("parentId", resourcesSimpleVO.getMenuId());
                    mapResources.put("name", resourcesSimpleVO.getName());
                    mapResources.put("isMenu", false);
                    menuAndResource.add(mapResources);
                    //调用递归方法，获取资源所在目录的全路径
                    String parentMenu = findParentMenu(childrenMap, resourcesSimpleVO.getId(), resourcesSimpleVO.getMenuId());
                    resourceMenus.add(parentMenu);
                }
            }
        }
        List<Map<String, Object>> finalList = new ArrayList<>();
        for (Map<String, Object> menu : menuAndResource) {
            String id = (String) menu.get("id");
            for (String m : resourceMenus) {
                if (m.contains(id)) {
                    finalList.add(menu);
                    break;
                }
            }
        }
        return finalList;
    }

    private String findParentMenu(Map<String, String> childrenMap, String parentIds, String menuId) {
        if (childrenMap.containsKey(menuId)) {
            String parentMenuId = childrenMap.get(menuId);
            parentIds += "," + menuId;
            return findParentMenu(childrenMap, parentIds, parentMenuId);
        } else {
            return parentIds;
        }
    }

    public void buildMenuAndResource(List<Map<String, Object>> menuAndResourceList, List<String> menus, List<String> resource) {
        for (Map<String, Object> map : menuAndResourceList) {
            String id = (String) map.get("id");
            boolean isMenu = (boolean) map.get("isMenu");
            if (isMenu) {
                if (menus != null) {
                    menus.add(id);
                }
            } else {
                if (resource != null) {
                    resource.add(id);
                }
            }
        }
    }


    public List<Map<String, Object>> menuAndResource(String userId, String corpId) {
        List<CorpUserResource> corpUserResourceList = corpUserResourceDao.listByUserIdAndCorpId(userId, corpId);
        //构建资源的ID集合
        List<String> resourceList = new ArrayList<>();
        for (CorpUserResource corpUserResource : corpUserResourceList) {
            if (corpUserResource != null && StringUtils.isNotBlank(corpUserResource.getResourceId())) {
                resourceList.add(corpUserResource.getResourceId());
            }
        }

        //获取已勾选的资源
        List<Map<String, Object>> userMenuAndResource = findUserMenuAndResourceByUserId(userId);
        List<Map<String, Object>> userMenuAndResourceChecked = new ArrayList<>();
        for (Map<String, Object> map : userMenuAndResource) {
            String id = (String) map.get("id");
            map.put("isChecked", resourceList.contains(id));
            userMenuAndResourceChecked.add(map);
        }
        return userMenuAndResourceChecked;
    }


    public List<String> getCorpIdsByUserId(String userId) {
        return corpUserResourceDao.listCorpIdByUserId(userId);
    }

    public List<String> listMenuIdsAssgnInModule() {
        return dao.listMenuIdsAssgnInModule();
    }

    /**
     * 获取最顶级菜单
     *
     * @param locale
     * @return
     */
    public List<Menu> listTopMenu(String locale) {
        return dao.listTopMenu(locale);
    }

    /**
     * 查询用户可以查看（有权限）的且启用的菜单id列表
     *
     * @param userId 用户ID
     * @return 菜单id列表
     */
    public List<Menu> listEnableMenuByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            return dao.listEnableMenuByUserId(userId);
        }
        return null;
    }

    /**
     * 查询用户可以查看（有权限）的且启用的菜单id列表
     *
     * @param userId 用户ID
     * @return 菜单id列表
     */
    public List<String> listEnableMenuIdByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            return dao.listEnableMenuIdByUserId(userId);
        }
        return new ArrayList<>();
    }

    public Set<String> getEnableMenuIdSetByUserId(String userId) {
        return new HashSet<>(listEnableMenuIdByUserId(userId));
    }

    public List<MenuIndexVO> listByIdsName(List<String> menuIds, String menuName) {
        return dao.listByIdsName(menuIds, menuName);
    }

    public List<MenuIndexVO> listMenuIdsByUserId(String userId) {
        return dao.listMenuIdsByUserId(userId);
    }

    public List<Map<String, String>> listEnableMenuIdsAndUrl() {
        return dao.listEnableMenusUrl();
    }

    /**
     * 获取 租户下所有菜单ID集合。
     *
     * @param tenantId 租户ID。
     * @return 菜单ID集合。
     */
    public Set<String> getTenantMenuIds(String tenantId) {
        List<MenuIndexVO> menuList = dao.getTenantMenuIds(tenantId);
        if (EmptyUtils.isEmpty(menuList)){
            return Collections.emptySet();
        }
        return menuList.stream().map(MenuIndexVO::getId).collect(Collectors.toSet());
    }
}
