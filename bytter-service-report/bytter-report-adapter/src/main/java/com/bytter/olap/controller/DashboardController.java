package com.bytter.olap.controller;

import com.bytter.core.utils.JsonUtils;
import com.bytter.olap.dao.BoardDao;
import com.bytter.olap.dao.CategoryDao;
import com.bytter.olap.dao.DatasetDao;
import com.bytter.olap.dao.WidgetDao;
import com.bytter.olap.dataprovider.DataProviderViewManager;
import com.bytter.olap.dataprovider.config.AggConfig;
import com.bytter.olap.dataprovider.result.AggregateResult;
import com.bytter.olap.dto.*;
import com.bytter.olap.pojo.DashboardBoardParam;
import com.bytter.olap.pojo.DashboardCategory;
import com.bytter.olap.pojo.DashboardDataset;
import com.bytter.olap.pojo.DashboardWidget;
import com.bytter.olap.services.*;
import com.bytter.olap.services.persist.excel.XlsProcessService;
import com.bytter.olap.util.SqlUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by yfyuan on 2016/8/9.
 */
@RestController
@RequestMapping("/olap/dashboard")
public class DashboardController extends BaseOlapController {

    @Autowired(required=false)
    private BoardDao boardDao;

    @Autowired
    private DataProviderService dataProviderService;

    @Autowired
    private WidgetService widgetService;

    @Autowired
    private WidgetDao widgetDao;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DatasetDao datasetDao;

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private XlsProcessService xlsProcessService;


    /**
     * 在数据集页面，切换数据源的时候会调用此方法
     *
     * @param type
     * @param page
     * @return
     */
    @RequestMapping(value = "/getConfigParams")
    public List<Map<String, Object>> getConfigParams(@RequestParam(name = "type") String type, @RequestParam(name = "page") String page) {
        return DataProviderViewManager.getQueryParams(type, page);
    }

    @RequestMapping(value = "/getConfigView")
    public String getConfigView(@RequestParam(name = "type") String type, @RequestParam(name = "page") String page) {
        return DataProviderViewManager.getQueryView(type, page);
    }


    @RequestMapping(value = "/saveNewWidget")
    public ServiceStatus saveNewWidget(@RequestParam(name = "json") String json) {
        json = HtmlUtils.htmlUnescape(json);
        return widgetService.save(json);
    }

    @RequestMapping(value = "/getAllWidgetList")
    public List<ViewDashboardWidget> getAllWidgetList() {
        List<DashboardWidget> list = widgetDao.getAllWidgetList();
        return Lists.transform(list, ViewDashboardWidget.TO);
    }


    @RequestMapping(value = "/getAllWidgetDataFieldList")
    public List<ViewDashboardWidget> getAllWidgetDataFieldList() {
        return widgetService.getAllWidgetDataFieldList();
    }

    @RequestMapping(value = "/getWidgetList")
    public List<ViewDashboardWidget> getWidgetList() {
        List<DashboardWidget> list = widgetDao.getAllWidgetList();
        return Lists.transform(list, ViewDashboardWidget.TO);
    }

    @RequestMapping(value = "/updateWidget")
    public ServiceStatus updateWidget(@RequestParam(name = "json") String json) {
        json = HtmlUtils.htmlUnescape(json);
        return widgetService.update(json);
    }

    @RequestMapping(value = "/deleteWidget")
    public ServiceStatus deleteWidget(@RequestParam(name = "id") String id) {
        return widgetService.delete(id);
    }

    /**************************************  看板API  start ********************************************/

    /**
     * 获取所有看板列表（携带用户权限数据）
     *
     * @return 看板列表
     */
    @RequestMapping(value = "/getBoardList")
    public List<ViewDashboardBoard> getPermissionAllBoardList() {
        return boardService.getPermissionAllBoardList();
    }

    /**
     * 获取看板列表（根据当前用户的权限获取有权限的列表）
     *
     * @return 看板列表
     */
    @RequestMapping(value = "/getBoardListByPermission")
    public List<ViewDashboardBoard> getBoardListByPermission() {
        return boardService.getBoardListByPermission(getUserId());
    }


    @RequestMapping(value = "/saveNewBoard")
    public ServiceStatus saveNewBoard(@RequestParam(name = "json") String json) {
        json = HtmlUtils.htmlUnescape(json);
        return boardService.save(json,getUserId());
    }

    @RequestMapping(value = "/updateBoard")
    public ServiceStatus updateBoard(@RequestParam(name = "json") String json) {
        json = HtmlUtils.htmlUnescape(json);
        return boardService.update(json);
    }

    @RequestMapping(value = "/deleteBoard")
    public ServiceStatus deleteBoard(@RequestParam(name = "id") String id) {
        return boardService.delete(id);
    }

    /**
     * 获取看板信息
     *
     * @param id
     * @returngetColumns
     */
    @RequestMapping(value = "/getBoardData")
    public ViewDashboardBoard getBoardData(@RequestParam(name = "id") String id) {
        return boardService.getBoardData(id);
    }

    /**************************************  看板API  end ********************************************/



    /**************************************  看板分类API  start ********************************************/


    @RequestMapping(value = "/saveNewCategory")
    public ServiceStatus saveNewCategory(@RequestParam(name = "json") String json) {
        json = HtmlUtils.htmlUnescape(json);
        return categoryService.save(json);
    }

    @RequestMapping(value = "/getCategoryList")
    public List<DashboardCategory> getCategoryList() {
        List<DashboardCategory> list = categoryDao.getCategoryList();
        return list;
    }

    @RequestMapping(value = "/updateCategory")
    public ServiceStatus updateCategory(@RequestParam(name = "json") String json) {
        json = HtmlUtils.htmlUnescape(json);
        return categoryService.update(json);
    }

    @RequestMapping(value = "/deleteCategory")
    public ServiceStatus deleteCategory(@RequestParam(name = "id") String id) {
        return categoryService.delete(id);
    }

    @RequestMapping(value = "/getWidgetCategoryList")
    public List<String> getWidgetCategoryList() {
        return widgetDao.getCategoryList();
    }

    /**************************************  看板分类API  end ********************************************/


    /**************************************  数据集API  start ********************************************/

    @RequestMapping(value = "/saveNewDataset")
    public ServiceStatus saveNewDataset(@RequestParam(name = "json") String json) {
        json = HtmlUtils.htmlUnescape(json);
        return datasetService.save(json);
    }

    @RequestMapping(value = "/saveImportDataset")
    public ServiceStatus saveImportDataset(@RequestParam(name = "json") String json) {
        json = HtmlUtils.htmlUnescape(json);
        return datasetService.saveImportDataset(json);
    }


    /**
     * 获取所有数据集列表
     *
     * @return
     */
    @RequestMapping(value = "/getAllDatasetList")
    public List<ViewDashboardDataset> getAllDatasetList() {
        List<DashboardDataset> list = datasetDao.getAllDatasetList();
        return Lists.transform(list, ViewDashboardDataset.TO);
    }

    /**
     * 获取数据集列表
     *
     * @return
     */
    @RequestMapping(value = "/getDatasetList")
    public List<ViewDashboardDataset> getDatasetList() {
        List<DashboardDataset> list = datasetDao.getAllDatasetList();
        return Lists.transform(list, ViewDashboardDataset.TO);
    }

    @RequestMapping(value = "/updateDataset")
    public ServiceStatus updateDataset(@RequestParam(name = "json") String json) {
        json = HtmlUtils.htmlUnescape(json);
        return datasetService.update(json);
    }

    @RequestMapping(value = "/deleteDataset")
    public ServiceStatus deleteDataset(@RequestParam(name = "id") String id) {
        return datasetService.delete(id);
    }

    @RequestMapping(value = "/getDatasetCategoryList")
    public List<String> getDatasetCategoryList() {
        return datasetDao.getCategoryList();
    }


    /**
     * 导出数据集（将数据集的配置数据输出到文件并下载）
     * created by chenlw on 2019/06/17
     *
     * @param json
     * @param response
     */
    @RequestMapping(value = "/exportDataSet")
    public void exportDataSet(@RequestParam(name = "json") String json, HttpServletResponse response) throws Exception {
        json = HtmlUtils.htmlUnescape(json);
        datasetService.exportDataSet(json, response);
    }

    /**
     * 解析数据集的数据文件
     *
     * @param datasetFile
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/resolveDataSetFile")
    public Object resolveDataSetFile(@RequestParam("datasetFile") MultipartFile datasetFile) {
        return datasetService.resolveDataSetFile(datasetFile);
    }

    /**
     * 一步导入数据集文件
     *
     * @param datasetFile
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/importDataSet")
    public Object importDataSet(@RequestParam("datasetFile") MultipartFile datasetFile) {
        return datasetService.importDataSet(datasetFile);
    }


    /**************************************  数据集API  end ********************************************/


    /**
     * 获取维度的列表值
     *
     * @param datasourceId 数据源ID
     * @param query        查询map
     * @param datasetId    数据集ID
     * @param colmunName   列名
     * @param cfg          图表聚合配置
     * @param reload       是否重新加载
     * @return 维度的列表值
     */
    @RequestMapping(value = "/getDimensionValues")
    public String[] getDimensionValues(@RequestParam(name = "datasourceId", required = false) String datasourceId,
                                       @RequestParam(name = "query", required = false) String query,
                                       @RequestParam(name = "datasetId", required = false) String datasetId,
                                       @RequestParam(name = "colmunName", required = true) String colmunName,
                                       @RequestParam(name = "cfg", required = false) String cfg,
                                       @RequestParam(name = "reload", required = false, defaultValue = "false") Boolean reload) {
        Map<String, String> strParams = null;
        if (query != null) {
            query = HtmlUtils.htmlUnescape(query);
            // JSONObject queryO = JSONObject.parseObject(query);
            // strParams = Maps.transformValues(queryO, Functions.toStringFunction());
            JsonNode queryO = JsonUtils.getJsonNodeWithUtf8(query);
            strParams = JsonUtils.json2Map(JsonUtils.toJSONString(queryO));
        }
        AggConfig config = null;
        if (cfg != null) {
            cfg = HtmlUtils.htmlUnescape(cfg);
            // config = ViewAggConfig.getAggConfig(JSONObject.parseObject(cfg, ViewAggConfig.class));
            config = ViewAggConfig.getAggConfig(JsonUtils.parseObject(cfg, ViewAggConfig.class));
        }
        return dataProviderService.getDimensionValues(datasourceId, strParams, datasetId, colmunName, config, reload, sqlParams);
    }

    /**
     * 获取字段列数组（SQL语句中select的字段）
     * 在数据集页面读取数据时，会调用此方法
     *
     * @param datasourceId 数据源ID
     * @param query        查询SQL
     * @param datasetId    数据集ID
     * @param reload       是否从缓存里加载数据（对应图表设计页面选项：是否从缓存加载数据。当数据源未启用数据源聚合选项时，该参数生效）
     * @return 字段列数组
     */
    @RequestMapping(value = "/getColumns")
    public DataProviderResult getColumns(@RequestParam(name = "datasourceId", required = false) String datasourceId,
                                         @RequestParam(name = "query", required = false) String query,
                                         @RequestParam(name = "datasetId", required = false) String datasetId,
                                         @RequestParam(name = "reload", required = false, defaultValue = "false") Boolean reload) {
        Map<String, String> strParams = null;
        if (query != null) {
            // 将转义字符转为字符串
            query = HtmlUtils.htmlUnescape(query);
            // JSONObject queryO = JSONObject.parseObject(query);
            // strParams = Maps.transformValues(queryO, Functions.toStringFunction());
            JsonNode queryO = JsonUtils.getJsonNodeWithUtf8(query);
            strParams = JsonUtils.json2Map(JsonUtils.toJSONString(queryO));
        }
        //扩展Freemarker语法的SQL语句    Created by chenlw on 2019/05/24.
        return dataProviderService.getColumns(datasourceId, strParams, datasetId, reload, sqlParams);
    }

    /**
     * 获取图表数据
     *
     * @param datasourceId 数据源ID
     * @param query        查询SQL
     * @param datasetId    数据集ID
     * @param cfg          图表聚合配置对象
     * @param reload       是否从缓存里加载数据（对应图表设计页面选项：是否从缓存加载数据。当数据源未启用数据源聚合选项时，该参数生效）
     * @return 图表数据
     */
    @RequestMapping(value = "/getAggregateData")
    public AggregateResult getAggregateData(@RequestParam(name = "datasourceId", required = false) String datasourceId,
                                            @RequestParam(name = "query", required = false) String query,
                                            @RequestParam(name = "datasetId", required = false) String datasetId,
                                            @RequestParam(name = "cfg") String cfg,
                                            @RequestParam(name = "reload", required = false, defaultValue = "false") Boolean reload) {
        Map<String, String> strParams = null;
        if (query != null && !"".equals(query)) {
            query = HtmlUtils.htmlUnescape(query);
            JsonNode queryO = JsonUtils.getJsonNodeWithUtf8(query);
            strParams = JsonUtils.json2Map(JsonUtils.toJSONString(queryO));
        }
        AggregateResult aggResult = null;
        // data source aggreagtor instance need not lock
        // boolean isDataSourceAggInstance = dataProviderService.isDataSourceAggInstance(datasourceId, strParams, datasetId);
        boolean isDataSourceAggInstance = dataProviderService.isDataSourceAggInstance(datasourceId, strParams, datasetId, sqlParams);

        String randomFlag = isDataSourceAggInstance ? UUID.randomUUID().toString() : "1";
        String lockString = Hashing.md5().newHasher()
                .putString(datasourceId + query + datasetId + randomFlag, Charsets.UTF_8)
                .hash().toString();
        synchronized (lockString.intern()) {
            cfg = HtmlUtils.htmlUnescape(cfg);
            // AggConfig config = ViewAggConfig.getAggConfig(JSONObject.parseObject(cfg, ViewAggConfig.class));
            AggConfig config = ViewAggConfig.getAggConfig(JsonUtils.parseObject(cfg, ViewAggConfig.class));
            // 扩展Freemarker语法的SQL语句    Created by chenlw on 2019/05/24.
            aggResult = dataProviderService.queryAggData(datasourceId, strParams, datasetId, config, reload, sqlParams);
        }
        return aggResult;
    }

    @RequestMapping(value = "/viewAggDataQuery")
    public String[] viewAggDataQuery(@RequestParam(name = "datasourceId", required = false) String datasourceId,
                                     @RequestParam(name = "query", required = false) String query,
                                     @RequestParam(name = "datasetId", required = false) String datasetId,
                                     @RequestParam(name = "cfg") String cfg) {
        Map<String, String> strParams = null;
        if (query != null) {
            query = HtmlUtils.htmlUnescape(query);
            JsonNode queryO = JsonUtils.getJsonNodeWithUtf8(query);
            strParams = JsonUtils.json2Map(JsonUtils.toJSONString(queryO));
        }
        // AggConfig config = ViewAggConfig.getAggConfig(JSONObject.parseObject(cfg, ViewAggConfig.class));
        AggConfig config = ViewAggConfig.getAggConfig(JsonUtils.parseObject(cfg, ViewAggConfig.class));
        return new String[]{dataProviderService.viewAggDataQuery(datasourceId, strParams, datasetId, config)};
    }

    @RequestMapping(value = "/dashboardWidget")
    public ViewDashboardWidget dashboardWidget(@RequestParam(name = "id") String id) {
        DashboardWidget widget = widgetDao.getWidget(id);
        return new ViewDashboardWidget(widget);
    }


    /**
     * 导出看板数据
     * 点击看板的导出按钮会调用此接口
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/exportBoard", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    public ResponseEntity<byte[]> exportBoard(@RequestParam(name = "id") String id, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "report.xls");
        return new ResponseEntity<>(boardService.exportBoard(id, request), headers, HttpStatus.CREATED);
    }

    /**
     * 表格转化成excel
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/tableToxls", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    public ResponseEntity<byte[]> tableToxls(@RequestParam(name = "data") String data) {
        data = HtmlUtils.htmlUnescape(data);
        JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(data);
        HSSFWorkbook wb = xlsProcessService.tableToxls(jsonNode);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            wb.write(out);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String exportFileName = "table.xls";
            JsonNode chartNameNode = jsonNode.get("chartName");
            if (chartNameNode != null) {
                exportFileName = chartNameNode.textValue().replaceAll("/", "-") + ".xls";
            }
            headers.setContentDispositionFormData("attachment", exportFileName);
            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
        } catch (IOException e) {
            LOG.error("", e);
        }
        return null;
    }

    /**
     * 获取看板参数
     *
     * @param boardId 看板ID
     * @return 看板参数
     */
    @RequestMapping(value = "/getBoardParam")
    public DashboardBoardParam getBoardParam(@RequestParam(name = "boardId") String boardId) {
        return boardDao.getBoardParam(boardId);
    }

    /**
     * 保存看板参数
     *
     * @param boardId 看板ID
     * @param config  聚合配置
     * @return 结果
     */
    @RequestMapping(value = "/saveBoardParam")
    @Transactional
    public String saveBoardParam(@RequestParam(name = "boardId") String boardId, @RequestParam(name = "config") String config) {
        if (boardId == null) {
            return "";
        }
        config = HtmlUtils.htmlUnescape(config);
        DashboardBoardParam boardParam = new DashboardBoardParam();
        boardParam.setBoardId(boardId);
        boardParam.setConfig(config);
        boardDao.deleteBoardParam(boardId);
        boardParam.setId(SqlUtils.getUuid());
        boardDao.saveBoardParam(boardParam);
        return "1";
    }

    @ExceptionHandler
    public ServiceStatus exp(HttpServletResponse response, Exception ex) {
        ex.printStackTrace();
        response.setStatus(500);
        LOG.error("Gloal exception Handler", ex);
        return new ServiceStatus(ServiceStatus.Status.Fail, ex.getMessage());
    }
}
