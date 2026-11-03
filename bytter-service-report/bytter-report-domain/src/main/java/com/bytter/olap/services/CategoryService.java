package com.bytter.olap.services;

import com.bytter.core.utils.JsonUtils;
import com.bytter.olap.dao.CategoryDao;
import com.bytter.olap.pojo.DashboardCategory;
import com.bytter.olap.util.SqlUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yfyuan on 2016/8/26.
 */
@Repository
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private BoardService boardService;

    /**
     * 添加看板分类
     * Created by chenlw on 2019/05/07.
     *
     * @param json
     * @return
     */
    public ServiceStatus save(String json) {
        JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(json);
        DashboardCategory category = new DashboardCategory();

        JsonNode nameNode = jsonNode.get("name");
        category.setName(nameNode != null ? nameNode.textValue() : null);

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("category_name", category.getName());
        if (categoryDao.countExistCategoryName(paramMap) <= 0) {
            category.setId(SqlUtils.getUuid());
            categoryDao.save(category);
            return new ServiceStatus(ServiceStatus.Status.Success, "成功");
        } else {
            return new ServiceStatus(ServiceStatus.Status.Fail, "对应分类名称已经存在，请修改名称后再保存！");
        }
    }

    public ServiceStatus update(String json) {
        JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(json);
        DashboardCategory category = new DashboardCategory();

        JsonNode idNode = jsonNode.get("id");
        JsonNode nameNode = jsonNode.get("name");

        category.setId(idNode != null ? idNode.textValue() : null);
        category.setName(nameNode != null ? nameNode.textValue() : null);

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("category_id", category.getId());
        paramMap.put("category_name", category.getName());
        if (categoryDao.countExistCategoryName(paramMap) <= 0) {
            categoryDao.update(category);
            return new ServiceStatus(ServiceStatus.Status.Success, "成功");
        } else {
            return new ServiceStatus(ServiceStatus.Status.Fail, "对应分类名称已经存在，请修改名称后再保存！");
        }
    }


    public ServiceStatus delete(String id) {
        if (StringUtils.isNotBlank(id)) {
            List<String> boardNameList = boardService.getCategoryNameListByCategoryId(id);
            if (boardNameList.size() > 0) {
                String msg = String.format("不允许删除该分类，因为该分类在图表看板当中被依赖！ [ %s ]", StringUtils.join(boardNameList, " , "));
                return new ServiceStatus(ServiceStatus.Status.Fail, msg);
            }
            categoryDao.delete(id);
            return new ServiceStatus(ServiceStatus.Status.Success, "成功");
        }
        return new ServiceStatus(ServiceStatus.Status.Fail, "删除失败");
    }
}
