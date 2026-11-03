package com.bytter.olap.services;

import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.olap.dao.BoardDao;
import com.bytter.olap.dao.WidgetDao;
import com.bytter.olap.dto.ViewDashboardBoard;
import com.bytter.olap.dto.ViewDashboardWidget;
import com.bytter.olap.pojo.DashboardBoard;
import com.bytter.olap.pojo.DashboardWidget;
import com.bytter.olap.services.persist.PersistContext;
import com.bytter.olap.services.persist.excel.XlsProcessService;
import com.bytter.olap.util.SqlUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;


/**
 * Created by yfyuan on 2016/8/23.
 */
@Repository
public class BoardService {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BoardDao boardDao;

    @Autowired
    private WidgetDao widgetDao;

    @Autowired
    private PersistService persistService;

    @Autowired
    private XlsProcessService xlsProcessService;

    @Autowired
    private DashboardUserPermissionService dashboardUserPermissionService;

    @Autowired
    private DashboardRolePermissionService dashboardRolePermissionService;

    public List<DashboardBoard> getAllBoardList() {
        return boardDao.getAllBoardList();
    }

    /**
     * 获取所有看板列表（携带用户权限数据）
     *
     * @return 看板列表
     */
    public List<ViewDashboardBoard> getPermissionAllBoardList() {
        List<DashboardBoard> dashboardBoardList = boardDao.getAllBoardList();
        List<ViewDashboardBoard> viewDashboardBoardList = new ArrayList<>();
        ViewDashboardBoard viewDashboardBoard;
        for (DashboardBoard dashboardBoard : dashboardBoardList) {
            String boardId = dashboardBoard.getId();
            viewDashboardBoard = new ViewDashboardBoard(dashboardBoard);
            // 查询有权限的用户ID列表
            viewDashboardBoard.setUserIdList(dashboardUserPermissionService.getUserIdListByBoardId(boardId));
            // 查询有权限的角色ID列表
            viewDashboardBoard.setRoleIdList(dashboardRolePermissionService.getRoleIdListByBoardId(boardId));
            viewDashboardBoardList.add(viewDashboardBoard);
        }
        return viewDashboardBoardList;
    }


    public List<ViewDashboardBoard> getBoardListByPermission(String currentUserId) {
        List<DashboardBoard> dashboardBoardList = boardDao.getBoardListByPermission(currentUserId);
        List<ViewDashboardBoard> viewDashboardBoardList = new ArrayList<>();
        ViewDashboardBoard viewDashboardBoard;
        for (DashboardBoard dashboardBoard : dashboardBoardList) {
            viewDashboardBoard = new ViewDashboardBoard(dashboardBoard);
            viewDashboardBoardList.add(viewDashboardBoard);
        }
        return viewDashboardBoardList;
    }


//    public ViewDashboardBoard getBoardData(String id) {
//        DashboardBoard board = boardDao.getBoard(id);
//        JSONObject layout = JSONObject.parseObject(board.getLayout());
//        JSONArray rows = layout.getJSONArray("rows");
//        for (Object row : rows) {
//            JSONObject o = (JSONObject) row;
//            if ("param".equals(o.getString("type"))) {
//                layout.put("containsParam", true);
//                continue;
//            }
//            JSONArray widgets = o.getJSONArray("widgets");
//            for (Object w : widgets) {
//                JSONObject ww = (JSONObject) w;
//                String widgetId = ww.getString("widgetId");
//                DashboardWidget widget = widgetDao.getWidget(widgetId);
//                JSONObject dataJson = JSONObject.parseObject(widget.getData());
//                // DataProviderResult data = dataProviderService.getData(dataJson.getLong("datasource"), Maps.transformValues(dataJson.getJSONObject("query"), Functions.toStringFunction()));
//                JSONObject widgetJson = (JSONObject) JSONObject.toJSON(new ViewDashboardWidget(widget));
//                // widgetJson.put("queryData", data.getData());
//                ww.put("widget", widgetJson);
//                ww.put("show", false);
//            }
//        }
//        ViewDashboardBoard view = new ViewDashboardBoard(board);
//        view.setLayout(layout);
//        return view;
//    }


    public ViewDashboardBoard getBoardData(String id) {
        ObjectMapper mapper = new ObjectMapper();
        DashboardBoard board = boardDao.getBoard(id);
        if(!EmptyUtils.isEmpty(board)) {
            JsonNode layout = JsonUtils.getJsonNodeWithUtf8(board.getLayout());
            Object rowsObj = layout.get("rows");
            if (rowsObj != null) {
                ArrayNode rows = (ArrayNode) rowsObj;
                for (JsonNode row : rows) {
                    ObjectNode layoutOperationObj = (ObjectNode) layout;
                    JsonNode typeNode = row.get("type");
                    if ("param".equals(typeNode.textValue())) {
                        layoutOperationObj.put("containsParam", true);
                        continue;
                    }
                    Object widgetsObj = row.get("widgets");
                    if (widgetsObj == null) {
                        continue;
                    }
                    ArrayNode widgets = (ArrayNode) widgetsObj;
                    for (Object w : widgets) {
                        ObjectNode ww = (ObjectNode) w;
                        JsonNode widgetIdNode = ww.get("widgetId");
                        DashboardWidget widget = widgetDao.getWidget(widgetIdNode.textValue());
                        JsonNode widgetJson = JsonUtils.getJsonNodeWithUtf8(JsonUtils.toJSONString(new ViewDashboardWidget(widget)));
                        ww.put("widget", widgetJson);
                        ww.put("show", false);
                    }
                }
            }
            ViewDashboardBoard view = new ViewDashboardBoard(board);
            view.setLayout(mapper.convertValue(layout, Map.class));
            return view;
        }else{
            return null;
        }
    }


//    public ServiceStatus save(String json, String currentUserId) {
//        JSONObject jsonObject = JSONObject.parseObject(json);
//        DashboardBoard board = new DashboardBoard();
//        board.setName(jsonObject.getString("name"));
//        board.setCategoryId(jsonObject.getString("categoryId"));
//        board.setLayout(jsonObject.getString("layout"));
//
//        Map<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("board_name", board.getName());
//        if (boardDao.countExistBoardName(paramMap) <= 0) {
//            String boardId = SqlUtils.getUuid();
//            board.setId(boardId);
//            board.setUserId(currentUserId);
//            int saveResult = boardDao.save(board);
//            if (saveResult > 0) {
//                updateBoardPermission(boardId, jsonObject);
//            }
//            return new ServiceStatus(ServiceStatus.Status.Success, "成功", board.getId());
//        } else {
//            return new ServiceStatus(ServiceStatus.Status.Fail, "对应名称的图表看板已经存在，请修改名称后再保存！");
//        }
//    }



    public ServiceStatus save(String json, String currentUserId) {
        DashboardBoard board = new DashboardBoard();
        JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(json);
        JsonNode nameNode = jsonNode.get("name");
        JsonNode categoryIdNode = jsonNode.get("categoryId");
        JsonNode layoutNode = jsonNode.get("layout");
        board.setName(nameNode != null ? nameNode.textValue() : null);
        board.setCategoryId(categoryIdNode != null ? categoryIdNode.textValue() : null);
        board.setLayout(JsonUtils.toJSONString(layoutNode));

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("board_name", board.getName());
        if (boardDao.countExistBoardName(paramMap) <= 0) {
            String boardId = SqlUtils.getUuid();
            board.setId(boardId);
            board.setUserId(currentUserId);
            int saveResult = boardDao.save(board);
            if (saveResult > 0) {
                updateBoardPermission(boardId, jsonNode);
            }
            return new ServiceStatus(ServiceStatus.Status.Success, "成功", board.getId());
        } else {
            return new ServiceStatus(ServiceStatus.Status.Fail, "对应名称的图表看板已经存在，请修改名称后再保存！");
        }
    }

    /**
     * 更新看板的用户和角色权限
     *
     * @param boardId         看板ID
     * @param boardJsonObject 看板JSON对象
     */
    private void updateBoardPermission(String boardId, JsonNode boardJsonObject) {
        // 更新用户对看板的权限
        List<String> userIdList = getUserIdList(boardJsonObject);
        dashboardUserPermissionService.updateBoardUserPermission(boardId, userIdList);
        // 更新角色对看板的权限
        List<String> roleIdList = getRoleIdList(boardJsonObject);
        dashboardRolePermissionService.updateBoardRolePermission(boardId, roleIdList);
    }

    /**
     * 获取用户ID列表
     *
     * @param boardJsonObject 看板JSON对象
     * @return 用户ID列表
     */
    private List<String> getUserIdList(JsonNode boardJsonObject) {
        List<String> userIdList = new ArrayList<>();
        ArrayNode userIdListObj = (ArrayNode) boardJsonObject.get("userIdList");
        if (userIdListObj != null) {
            for (JsonNode userIdNode : userIdListObj) {
                userIdList.add(userIdNode.textValue());
            }
        }
        return userIdList;
    }

    /**
     * 获取角色ID列表
     *
     * @param boardJsonObject 看板JSON对象
     * @return 角色ID列表
     */
    private List<String> getRoleIdList(JsonNode boardJsonObject) {
        List<String> roleIdList = new ArrayList<>();
        ArrayNode roleIdListObj = (ArrayNode) boardJsonObject.get("roleIdList");
        if (roleIdListObj != null) {
            for (JsonNode roleIdNode : roleIdListObj) {
                roleIdList.add(roleIdNode.textValue());
            }
        }
        return roleIdList;
    }


//    /**
//     * 更新看板的用户和角色权限
//     *
//     * @param boardId         看板ID
//     * @param boardJsonObject 看板JSON对象
//     */
//    private void updateBoardPermission(String boardId, JSONObject boardJsonObject) {
//        // 更新用户对看板的权限
//        List<String> userIdList = getUserIdList(boardJsonObject);
//        dashboardUserPermissionService.updateBoardUserPermission(boardId, userIdList);
//        // 更新角色对看板的权限
//        List<String> roleIdList = getRoleIdList(boardJsonObject);
//        dashboardRolePermissionService.updateBoardRolePermission(boardId, roleIdList);
//    }
//
//
//    /**
//     * 获取用户ID列表
//     *
//     * @param boardJsonObject 看板JSON对象
//     * @return 用户ID列表
//     */
//    private List<String> getUserIdList(JSONObject boardJsonObject) {
//        List<String> userIdList = new ArrayList<>();
//        JSONArray userIdListObj = boardJsonObject.getJSONArray("userIdList");
//        if (userIdListObj != null) {
//            for (Object userId : userIdListObj) {
//                userIdList.add(userId.toString());
//            }
//        }
//        return userIdList;
//    }

//    /**
//     * 获取角色ID列表
//     *
//     * @param boardJsonObject 看板JSON对象
//     * @return 角色ID列表
//     */
//    private List<String> getRoleIdList(JSONObject boardJsonObject) {
//        List<String> roleIdList = new ArrayList<>();
//        JSONArray roleIdListObj = boardJsonObject.getJSONArray("roleIdList");
//        if (roleIdListObj != null) {
//            for (Object roleId : roleIdListObj) {
//                roleIdList.add(roleId.toString());
//            }
//        }
//        return roleIdList;
//    }

    public ServiceStatus update(String json) {

        DashboardBoard board = new DashboardBoard();
        JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(json);
        JsonNode nameNode = jsonNode.get("name");
        JsonNode categoryIdNode = jsonNode.get("categoryId");
        JsonNode layoutNode = jsonNode.get("layout");
        JsonNode idNode = jsonNode.get("id");
        board.setName(nameNode != null ? nameNode.textValue() : null);
        board.setCategoryId(categoryIdNode != null ? categoryIdNode.textValue() : null);
        board.setLayout(JsonUtils.toJSONString(layoutNode));
        board.setId(idNode != null ? idNode.textValue() : null);
        board.setUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));


        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("board_id", board.getId());
        paramMap.put("board_name", board.getName());
        if (boardDao.countExistBoardName(paramMap) <= 0) {
            int updateResult = boardDao.update(board);
            if (updateResult > 0) {
                updateBoardPermission(board.getId(), jsonNode);
            }
            return new ServiceStatus(ServiceStatus.Status.Success, "成功");
        } else {
            return new ServiceStatus(ServiceStatus.Status.Fail, "对应名称的图表看板已经存在，请修改名称后再保存！");
        }
    }

//    public ServiceStatus update(String json) {
//        JSONObject jsonObject = JSONObject.parseObject(json);
//        DashboardBoard board = new DashboardBoard();
//        board.setName(jsonObject.getString("name"));
//        board.setCategoryId(jsonObject.getString("categoryId"));
//        board.setLayout(jsonObject.getString("layout"));
//        board.setId(jsonObject.getString("id"));
//        board.setUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
//
//        Map<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("board_id", board.getId());
//        paramMap.put("board_name", board.getName());
//        if (boardDao.countExistBoardName(paramMap) <= 0) {
//            int updateResult = boardDao.update(board);
//            if (updateResult > 0) {
//                updateBoardPermission(board.getId(), jsonObject);
//            }
//            return new ServiceStatus(ServiceStatus.Status.Success, "成功");
//        } else {
//            return new ServiceStatus(ServiceStatus.Status.Fail, "对应名称的图表看板已经存在，请修改名称后再保存！");
//        }
//    }


    public ServiceStatus delete(String id) {
        try {
            int deleteResult = boardDao.delete(id);
            if (deleteResult > 0) {
                // 删除看板-用户权限
                dashboardUserPermissionService.deleteAllUserPermissionByBoardId(id);
                // 删除看板-角色权限
                dashboardRolePermissionService.deleteAllRolePermissionByBoardId(id);
            }
            return new ServiceStatus(ServiceStatus.Status.Success, "成功");
        } catch (Exception e) {
            LOG.error("", e);
            return new ServiceStatus(ServiceStatus.Status.Fail, e.getMessage());
        }
    }

    /**
     * 导出看板数据
     * @param id
     * @param request
     * @return
     */
    public byte[] exportBoard(String id, HttpServletRequest request) {
        PersistContext persistContext = persistService.persist(id, request);
        List<PersistContext> workbookList = new ArrayList<>();
        workbookList.add(persistContext);
        HSSFWorkbook workbook = xlsProcessService.dashboardToXls(workbookList);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            LOG.error("", e);
        }
        return null;
    }


//    /**
//     * 判断图表是否存在看板当中，如果存在则返回看板列表名称
//     *
//     * @param findWidgetId 图表ID
//     * @return 如果存在则返回看板列表名称
//     */
//    public List<String> getBoardNameListByWidgetId(String findWidgetId) {
//        List<String> boardNameList = new ArrayList<>();
//        if (StringUtils.isEmpty(findWidgetId)) {
//            return boardNameList;
//        }
//        List<DashboardBoard> boardList = boardDao.getAllBoardList();
//        String data;
//        for (int i = 0, size = boardList.size(); i < size; i++) {
//            data = boardList.get(i).getLayout();
//            if (StringUtils.isEmpty(data)) {
//                continue;
//            }
//
//            JSONObject layoutJsonObj = JSONObject.parseObject(data);
//            if (layoutJsonObj == null) {
//                continue;
//            }
//            JSONArray rows = layoutJsonObj.getJSONArray("rows");
//            if (rows == null) {
//                continue;
//            }
//            for (int j = 0, rowSize = rows.size(); j < rowSize; j++) {
//                JSONObject row = rows.getJSONObject(j);
//                if (row == null) {
//                    continue;
//                }
//                String type = row.getString("type");
//                if (!"widget".equals(type)) {
//                    continue;
//                }
//                JSONArray widgets = row.getJSONArray("widgets");
//                if (widgets == null || widgets.size() == 0) {
//                    continue;
//                }
//                for (int k = 0, widgetsSize = widgets.size(); k < widgetsSize; k++) {
//                    JSONObject widget = widgets.getJSONObject(k);
//                    if (widget == null) {
//                        continue;
//                    }
//                    String widgetId = widget.getString("widgetId");
//                    if (findWidgetId.equals(widgetId)) {
//                        // 获取图表所依赖的看板名称
//                        boardNameList.add(boardList.get(i).getName());
//                    }
//                }
//            }
//        }
//        return boardNameList;
//    }


    /**
     * 判断图表是否存在看板当中，如果存在则返回看板列表名称
     *
     * @param findWidgetId 图表ID
     * @return 如果存在则返回看板列表名称
     */
    public List<String> getBoardNameListByWidgetId(String findWidgetId) {
        List<String> boardNameList = new ArrayList<>();
        if (StringUtils.isEmpty(findWidgetId)) {
            return boardNameList;
        }
        List<DashboardBoard> boardList = boardDao.getAllBoardList();
        String data;
        for (int i = 0, size = boardList.size(); i < size; i++) {
            data = boardList.get(i).getLayout();
            if (StringUtils.isEmpty(data)) {
                continue;
            }

            JsonNode layoutJsonObj = JsonUtils.getJsonNodeWithUtf8(data);
            if (layoutJsonObj == null) {
                continue;
            }
            Object rowsObj = layoutJsonObj.get("rows");
            if (rowsObj == null) {
                continue;
            }
            ArrayNode rows = (ArrayNode) rowsObj;
            for (int j = 0, rowSize = rows.size(); j < rowSize; j++) {
                JsonNode row = rows.get(j);
                if (row == null) {
                    continue;
                }
                JsonNode typeNode = row.get("type");
                if (typeNode != null && !"widget".equals(typeNode.textValue())) {
                    continue;
                }
                Object widgetsObj = row.get("widgets");
                if (widgetsObj == null) {
                    continue;
                }
                ArrayNode widgets = (ArrayNode) widgetsObj;
                if (widgets.size() == 0) {
                    continue;
                }
                for (int k = 0, widgetsSize = widgets.size(); k < widgetsSize; k++) {
                    JsonNode widget = widgets.get(k);
                    if (widget == null) {
                        continue;
                    }
                    JsonNode widgetIdNode = widget.get("widgetId");
                    if (widgetIdNode != null && findWidgetId.equals(widgetIdNode.textValue())) {
                        // 获取图表所依赖的看板名称
                        boardNameList.add(boardList.get(i).getName());
                    }
                }
            }
        }
        return boardNameList;
    }


    /**
     * 通过看板ID获取使用了该分类名称的看板名称列表
     *
     * @param categoryId 看板ID
     * @return 看板名称列表
     */
    public List<String> getCategoryNameListByCategoryId(String categoryId) {
        List<String> categoryNameList = new ArrayList<>();
        if (StringUtils.isEmpty(categoryId)) {
            return categoryNameList;
        }
        List<DashboardBoard> boardList = boardDao.getAllBoardList();
        for (int i = 0, boardsSize = boardList.size(); i < boardsSize; i++) {
            DashboardBoard board = boardList.get(i);
            if (categoryId.equals(board.getCategoryId())) {
                categoryNameList.add(board.getName());
            }
        }
        return categoryNameList;
    }



}
