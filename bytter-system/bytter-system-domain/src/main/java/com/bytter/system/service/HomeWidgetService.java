package com.bytter.system.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.page.IPagePara;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.dao.HomeWidgetDao;
import com.bytter.system.entity.HomeWidget;
import com.bytter.system.vo.DictVO;
import com.bytter.system.vo.HomeWidgetVO;
import com.bytter.system.vo.ResourcesSimpleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhoul on 2017/4/12.
 */
@Service("homeWidgetService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HomeWidgetService extends BaseService<HomeWidget, String> {
    @Autowired
    private HomeWidgetDao homeWidgetDao;
    @Autowired
    //@Qualifier("resourcesService")
    private ResourcesService resourcesService;
    @Autowired
    private DictService dictService;

    @Override
    public BaseDao<HomeWidget, String> getEntityDao() {
        return homeWidgetDao;
    }
    public IPageResult<HomeWidget> getPageResult(IPagePara input, String type, Integer width, Integer height, String userId) {
        IPageResult<HomeWidget> homeWidgets = homeWidgetDao.listWidget(input, type, width, height);
        List<ResourcesSimpleVO> allUserResources = resourcesService.listResourceByUserId(userId);
        if (EmptyUtils.isEmpty(allUserResources)) {
            return homeWidgets;
        }
        List<String> allUserPermission = new ArrayList<>();
        for (ResourcesSimpleVO resources : allUserResources) {
            allUserPermission.add(resources.getPermission());
        }
        List<HomeWidget> homeWidgetList = homeWidgets.getList();
        Iterator<HomeWidget> it = homeWidgetList.iterator();
        while (it.hasNext()) {
            HomeWidget homeWidget = it.next();
            if (StringUtils.isNotBlank(homeWidget.getResourceContent()) && !allUserPermission.contains(homeWidget.getResourceContent())) {
                it.remove();
            }
        }
        return homeWidgets;
    }
    public TableGridResponse<HomeWidgetVO> getTableGrid(TableGridInput input) {
        IPageResult<HomeWidgetVO> pageResult = homeWidgetDao.listAllWidget(input);
        List<DictVO> dictVOList = dictService.getDictListByCode("sys.home.widget.type", "sys.home.widget.category");
        Map<String, String> dictVOMap = dictVOList.stream().collect(Collectors.toMap(item -> item.getGroupCode() + item.getCode(),
                DictVO::getName,(pre,next)->next));
        List<HomeWidgetVO> list = pageResult.getList();
        for (HomeWidgetVO homeWidget : list) {
            homeWidget.setCategoryLang(dictVOMap.get("sys.home.widget.category"+homeWidget.getCategoryId()));
            homeWidget.setTypeLang(dictVOMap.get("sys.home.widget.type"+homeWidget.getType()));
        }
        return pageResult.convert(TableGridResponse.class);
    }
    public void update(HomeWidget homeWidget) {
        homeWidgetDao.update(homeWidget);
    }
    public void save(HomeWidget homeWidget) {
        if (homeWidget.getSort() == null) {
            homeWidget.setSort(500);
        }
        homeWidget.setCreateTime(new Date());
        homeWidgetDao.save(homeWidget);
    }
    @Override
    public void delete(List<String> ids) {
        for (String id : ids) {
            homeWidgetDao.delete(id);
        }
    }
    /**
     * 根据用户判断是否含有某个组件的资源权限
     *
     * @param content     转向内容
     * @param currentUser Created by chenlj on 2018/1/30.
     */
    public boolean judgeWidgetPermission(String content, ICurrentUser currentUser) {
        ShiroUser userShiroVO = (ShiroUser) currentUser;
        List<HomeWidget> homeWidgets = homeWidgetDao.listBy("content", content);
        if (EmptyUtils.isEmpty(homeWidgets)) {
            return true;
        }
        Set<String> allUserPermission = userShiroVO.getPermission();
        if (EmptyUtils.isEmpty(allUserPermission)) {
            return false;
        }
        for (HomeWidget homeWidget : homeWidgets) {
            if (allUserPermission.contains(homeWidget.getResourceContent())) {
                return true;
            }
        }
        return false;
    }
}
