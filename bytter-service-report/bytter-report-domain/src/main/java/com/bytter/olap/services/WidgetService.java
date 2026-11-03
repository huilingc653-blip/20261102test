package com.bytter.olap.services;

import com.bytter.core.utils.JsonUtils;
import com.bytter.olap.dao.WidgetDao;
import com.bytter.olap.dto.ViewDashboardWidget;
import com.bytter.olap.pojo.DashboardWidget;
import com.bytter.olap.util.SqlUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by yfyuan on 2016/8/22.
 */
@Repository
public class WidgetService {

    @Autowired
    private WidgetDao widgetDao;

    @Autowired
    private BoardService boardService;

    /**
     * 获取图表data字段数组（用于前端管理图表，判断模型元素是否能删除）
     *
     * @return 图表data字段数组
     */
    public List<ViewDashboardWidget> getAllWidgetDataFieldList() {
        List<ViewDashboardWidget> viewDashboardWidgetList = new ArrayList<>();
        List<DashboardWidget> list = widgetDao.getAllWidgetList();
        ViewDashboardWidget viewDashboardWidget;
        for (DashboardWidget widget : list) {
            viewDashboardWidget = new ViewDashboardWidget();
            viewDashboardWidget.setName(widget.getName());
            // viewDashboardWidget.setData(JSONObject.parseObject(widget.getData()));
            viewDashboardWidget.setData(JsonUtils.json2Map(widget.getData()));
            viewDashboardWidgetList.add(viewDashboardWidget);
        }
        return viewDashboardWidgetList;
    }


//    public ServiceStatus save(String json) {
//        JSONObject jsonObject = JSONObject.parseObject(json);
//        DashboardWidget widget = new DashboardWidget();
//        widget.setName(jsonObject.getString("name"));
//        widget.setData(jsonObject.getString("data"));
//        widget.setCategoryName(jsonObject.getString("categoryName"));
//        if (StringUtils.isEmpty(widget.getCategoryName())) {
//            widget.setCategoryName("默认分类");
//        }
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("widget_name", widget.getName());
//        paramMap.put("category_name", widget.getCategoryName());
//
//        if (widgetDao.countExistWidgetName(paramMap) <= 0) {
//            String widgetId = SqlUtils.getUuid();
//            widget.setId(widgetId);
//            widgetDao.save(widget);
//            return new ServiceStatus(ServiceStatus.Status.Success, "成功",widgetId);
//        } else {
//            return new ServiceStatus(ServiceStatus.Status.Fail, "对应名称的图表已经存在，请修改图表名称后再保存！");
//        }
//    }


    public ServiceStatus save(String json) {
        DashboardWidget widget = new DashboardWidget();
        JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(json);

        JsonNode nameNode = jsonNode.get("name");
        JsonNode dataNode = jsonNode.get("data");
        JsonNode categoryNameNode = jsonNode.get("categoryName");

        widget.setName(nameNode != null ? nameNode.textValue() : null);
        widget.setData(dataNode != null ? dataNode.toString() : null);
        widget.setCategoryName(categoryNameNode != null ? categoryNameNode.textValue() : null);
        if (StringUtils.isEmpty(widget.getCategoryName())) {
            widget.setCategoryName("默认分类");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("widget_name", widget.getName());
        paramMap.put("category_name", widget.getCategoryName());

        if (widgetDao.countExistWidgetName(paramMap) <= 0) {
            String widgetId = SqlUtils.getUuid();
            widget.setId(widgetId);
            widgetDao.save(widget);
            return new ServiceStatus(ServiceStatus.Status.Success, "成功",widgetId);
        } else {
            return new ServiceStatus(ServiceStatus.Status.Fail, "对应名称的图表已经存在，请修改图表名称后再保存！");
        }
    }


//    public ServiceStatus update(String json) {
//        JSONObject jsonObject = JSONObject.parseObject(json);
//        DashboardWidget widget = new DashboardWidget();
//        widget.setId(jsonObject.getString("id"));
//        widget.setName(jsonObject.getString("name"));
//        widget.setCategoryName(jsonObject.getString("categoryName"));
//        widget.setData(jsonObject.getString("data"));
//        widget.setUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
//        if (StringUtils.isEmpty(widget.getCategoryName())) {
//            widget.setCategoryName("默认分类");
//        }
//        Map<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("widget_name", widget.getName());
//        paramMap.put("widget_id", widget.getId());
//        paramMap.put("category_name", widget.getCategoryName());
//        if (widgetDao.countExistWidgetName(paramMap) <= 0) {
//            widgetDao.update(widget);
//            return new ServiceStatus(ServiceStatus.Status.Success, "成功");
//        } else {
//            return new ServiceStatus(ServiceStatus.Status.Fail, "对应名称的图表已经存在，请修改图表名称后再保存！");
//        }
//    }


    public ServiceStatus update(String json) {
        DashboardWidget widget = new DashboardWidget();
        JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(json);

        JsonNode idNode = jsonNode.get("id");
        JsonNode nameNode = jsonNode.get("name");
        JsonNode categoryNameNode = jsonNode.get("categoryName");
        JsonNode dataNode = jsonNode.get("data");

        widget.setId(idNode != null ? idNode.textValue() : null);
        widget.setName(nameNode != null ? nameNode.textValue() : null);
        widget.setCategoryName(categoryNameNode != null ? categoryNameNode.textValue() : null);
        widget.setData(dataNode != null ? dataNode.toString() : null);
        widget.setUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        if (StringUtils.isEmpty(widget.getCategoryName())) {
            widget.setCategoryName("默认分类");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("widget_name", widget.getName());
        paramMap.put("widget_id", widget.getId());
        paramMap.put("category_name", widget.getCategoryName());
        if (widgetDao.countExistWidgetName(paramMap) <= 0) {
            widgetDao.update(widget);
            return new ServiceStatus(ServiceStatus.Status.Success, "成功");
        } else {
            return new ServiceStatus(ServiceStatus.Status.Fail, "对应名称的图表已经存在，请修改图表名称后再保存！");
        }
    }


    public ServiceStatus delete(String id) {
        if (StringUtils.isNotBlank(id)) {
            List<String> boardNameList = boardService.getBoardNameListByWidgetId(id);
            if (boardNameList.size() > 0) {
                String msg = String.format("不允许删除图表，因为该图表在图表看板当中被依赖！ [ %s ]", StringUtils.join(boardNameList, " , "));
                return new ServiceStatus(ServiceStatus.Status.Fail, msg);
            }
            widgetDao.delete(id);
            return new ServiceStatus(ServiceStatus.Status.Success, "成功");
        }
        return new ServiceStatus(ServiceStatus.Status.Fail, "删除失败");
    }


    /**
     * 通过数据集ID查询图表名称（即数据集被依赖的图表）
     * @param findDatasetId 数据集ID
     * @return 图表名称列表
     */
    public List<String> getWidgetNameListByDatasetId(String findDatasetId){
        List<String> widgetNameList = new ArrayList<>();
        if (StringUtils.isEmpty(findDatasetId)) {
            return widgetNameList;
        }
        List<DashboardWidget> widgetList = widgetDao.getAllWidgetList();
        for (int i = 0, widgetsSize = widgetList.size(); i < widgetsSize; i++) {
            String dataJson = widgetList.get(i).getData();
            if (StringUtils.isEmpty(dataJson)) {
                continue;
            }
            JsonNode widget = JsonUtils.getJsonNodeWithUtf8(dataJson);
            if (widget == null) {
                continue;
            }
            JsonNode datasetIdNode = widget.get("datasetId");
            if (datasetIdNode != null && findDatasetId.equals(datasetIdNode.textValue())) {
                widgetNameList.add(widgetList.get(i).getName());
            }
        }
        return widgetNameList;
    }

}
