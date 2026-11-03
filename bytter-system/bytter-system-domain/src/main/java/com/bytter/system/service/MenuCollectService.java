package com.bytter.system.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.dao.MenuCollectDao;
import com.bytter.system.entity.Menu;
import com.bytter.system.entity.MenuCollect;
/**
 * 基础收藏菜单service
 * Created by zhoul on 2017/10/23.
 */
@Service("menuCollectService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MenuCollectService extends BaseService<MenuCollect, String> {
    @Autowired
    private MenuCollectDao menuCollectDao;
    @Autowired
    //@Qualifier("menuService")
    private MenuService menuService;
    @Override
    public BaseDao<MenuCollect, String> getEntityDao() {
        return menuCollectDao;
    }
    @Transactional
    public void collect(String menuId, String userId) {
        MenuCollect collect = menuCollectDao.collect(menuId, userId);
        if (collect == null) {
            Menu menu = menuService.get(menuId);
            String parentIds = menuService.getParentIds(menuId);
            MenuCollect menuCollect = new MenuCollect();
            menuCollect.setMenuId(menuId);
            menuCollect.setUserId(userId);
            menuCollect.setUrl(menu.getUrl());
            menuCollect.setParentIds(parentIds);
            menuCollect.setCreateTime(new Date());
            menuCollect.setSort(500);
            menuCollectDao.save(menuCollect);
        } else {
            menuCollectDao.deleteByMenuId(menuId, userId);
        }
    }
    @CachePut(value = "MenuCollectCache", key = "#userId")
    public List<MenuCollect> listByUserId(String userId) {
        List<MenuCollect> menuCollectList = menuCollectDao.listByUserId(userId);
        //去除重复的收藏菜单
        Map<String, MenuCollect> menuCollectMap = new HashMap<>();
        List<MenuCollect> menuCollects = new ArrayList<>();
        for (MenuCollect menuCollect : menuCollectList) {
            if (!menuCollectMap.containsKey(menuCollect.getMenuId() + menuCollect.getUserId())) {
                menuCollectMap.put(menuCollect.getMenuId() + menuCollect.getUserId(), menuCollect);
                menuCollects.add(menuCollect);
            }
        }
        return menuCollects;
    }
    /**
     * 进行收藏菜单置顶操作
     *
     * @param id     收藏菜单ID
     * @param userId 操作用户
     */
    @Transactional
    public void menuTop(String id, String userId) {
        //获取最大排序值
        Integer sort = menuCollectDao.getMaxSortByUserId(userId);
        //对此置顶收藏菜单进行进行最大排序设置
        MenuCollect mc = this.get(id, true);
        mc.setSort(sort + 1);
        this.updateEntity(mc);
    }
    public List<String> listMenuIdByUserId(String userId) {
        List<MenuCollect> menuCollects = menuCollectDao.listByUserId(userId);
        List<String> menudIds = new ArrayList<>();
        for (MenuCollect menuCollect : menuCollects) {
            menudIds.add(menuCollect.getMenuId());
        }
        return menudIds;
    }
    /**
     * 常用菜单拖拽保存
     *
     * @return
     */
    @CacheEvict(value = "MenuCollectCache", key = "#userId")
    public void saveDragWidget(List<MenuCollect> menuCollectList, String userId) {
        Set<String> set = new HashSet<>();
        for (MenuCollect menuCollect : menuCollectList) {
            if (StringUtils.isBlank(menuCollect.getId())) {
                continue;
            }
            if (set.contains(menuCollect.getId())) {
                continue;
            }
            set.add(menuCollect.getId());
            MenuCollect entity = menuCollectDao.get(menuCollect.getId());
            entity.setSort(menuCollect.getSort());
            entity.setCreateTime(DateUtils.getDateTime());
            menuCollectDao.update(entity);
        }
    }
}
