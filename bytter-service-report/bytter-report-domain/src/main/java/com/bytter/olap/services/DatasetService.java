package com.bytter.olap.services;

import com.bytter.core.utils.JsonUtils;
import com.bytter.olap.dao.DatasetDao;
import com.bytter.olap.dto.ViewDashboardDataset;
import com.bytter.olap.pojo.DashboardDataset;
import com.bytter.olap.util.AESUtils;
import com.bytter.olap.util.FileUtils;
import com.bytter.olap.util.SqlUtils;
import com.bytter.olap.util.StreamUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yfyuan on 2016/10/11.
 */
@Repository
public class DatasetService {

    /**
     * 导出数据集文件的后缀名
     * Bytter Custom Report Data Set, btcrds
     */
    private static final String EXPORT_DATASET_FILE_SUFFIX = "btcrds";

    /**
     * AES加密和解密的钥匙：Bytter
     * 用于加密和解密数据集文件的内容。（这个值不能修改！）
     */
    private static final String DATASET_FILE_AES_KEY = "Bytter";

    @Autowired
    private DatasetDao datasetDao;

    @Autowired
    private WidgetService widgetService;

    public ServiceStatus save(String json) {
        JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(json);
        DashboardDataset dataset = new DashboardDataset();

        JsonNode nameNode = jsonNode.get("name");
        JsonNode categoryNameNode = jsonNode.get("categoryName");
        JsonNode dataNode = jsonNode.get("data");

        dataset.setName(nameNode != null ? nameNode.textValue() : null);
        dataset.setCategoryName(categoryNameNode != null ? categoryNameNode.textValue() : null);
        dataset.setData(JsonUtils.toJSONString(dataNode));

        if (StringUtils.isEmpty(dataset.getCategoryName())) {
            dataset.setCategoryName("默认分类");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dataset_name", dataset.getName());
        paramMap.put("category_name", dataset.getCategoryName());
        if (datasetDao.countExistDatasetName(paramMap) <= 0) {
            String uuid = SqlUtils.getUuid();
            dataset.setId(uuid);
            datasetDao.save(dataset);
            return new ServiceStatus(ServiceStatus.Status.Success, "成功", uuid);
        } else {
            return new ServiceStatus(ServiceStatus.Status.Fail, "对应名称的数据集已经存在，请修改数据集名称后再保存！");
        }
    }

    public ServiceStatus update(String json) {
        JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(json);
        DashboardDataset dataset = new DashboardDataset();

        JsonNode idNode = jsonNode.get("id");
        JsonNode nameNode = jsonNode.get("name");
        JsonNode categoryNameNode = jsonNode.get("categoryName");
        JsonNode dataNode = jsonNode.get("data");

        dataset.setId(idNode != null ? idNode.textValue() : null);
        dataset.setName(nameNode != null ? nameNode.textValue() : null);
        dataset.setCategoryName(categoryNameNode != null ? categoryNameNode.textValue() : null);
        dataset.setData(JsonUtils.toJSONString(dataNode));

        dataset.setUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        if (StringUtils.isEmpty(dataset.getCategoryName())) {
            dataset.setCategoryName("默认分类");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("dataset_name", dataset.getName());
        paramMap.put("dataset_id", dataset.getId());
        paramMap.put("category_name", dataset.getCategoryName());
        if (datasetDao.countExistDatasetName(paramMap) <= 0) {
            datasetDao.update(dataset);
            return new ServiceStatus(ServiceStatus.Status.Success, "成功");
        } else {
            return new ServiceStatus(ServiceStatus.Status.Fail, "对应名称的数据集已经存在，请修改数据集名称后再保存！");
        }
    }


    public ServiceStatus delete(String id) {
        if (id != null) {
            List<String> dependentBoardNameList = widgetService.getWidgetNameListByDatasetId(id);
            if (dependentBoardNameList.size() > 0) {
                String msg = String.format("不允许删除数据集，因为该数据集在图表当中被依赖！ [ %s ]", StringUtils.join(dependentBoardNameList, " , "));
                return new ServiceStatus(ServiceStatus.Status.Fail, msg);
            }
            datasetDao.delete(id);
            return new ServiceStatus(ServiceStatus.Status.Success, "成功");
        }
        return new ServiceStatus(ServiceStatus.Status.Fail, "删除失败");
    }


    public ViewDashboardDataset getViewDashboardDataset(String datasetId) {
        if (datasetId == null) {
            return null;
        }
        DashboardDataset dashboardDataset = datasetDao.getDataset(datasetId);
        if (dashboardDataset == null) {
            return null;
        }
        return new ViewDashboardDataset(dashboardDataset);
    }


    /**
     * 导出数据集（将数据集的配置数据输出到文件并下载）
     *
     * @param json
     * @param response
     */
    public void exportDataSet(String json, HttpServletResponse response) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        String fileName = "dataset." + EXPORT_DATASET_FILE_SUFFIX;
        JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(json);
        if (jsonNode != null) {
            JsonNode dataSetNameNode = jsonNode.get("name");
            if (dataSetNameNode != null) {
                fileName = dataSetNameNode.textValue() + "." + EXPORT_DATASET_FILE_SUFFIX;
            }
        }

        try {

            // 转码，免得文件名中文乱码
            String filename = URLEncoder.encode(fileName, "UTF-8");
            // 设置文件下载头
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            // 把文件的后缀名添加到响应头里面去
            response.addHeader("x-file-suffix", URLEncoder.encode(EXPORT_DATASET_FILE_SUFFIX, "UTF-8"));
            // 设置文件ContentType类型
            response.setContentType("multipart/form-data");

            // 对数据集的Json串进行Base64转码
            // String base64Json = Base64Utils.encodeToString(json.getBytes("UTF-8"));

            // 使用AES加密
            String encryptJson = AESUtils.encrypt(json, DATASET_FILE_AES_KEY);

            // 将字符串转化成输入流
            inputStream = StreamUtils.getStringStream(encryptJson);

            // 获取相应输出流
            outputStream = response.getOutputStream();
            byte[] buff = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 解析DataSet文件
     *
     * @param datasetFile
     * @return ServiceStatus Map
     * （1）解析失败，则返回失败状态的ServiceStatus。{status:"2",msg:"失败提示消息"}
     * （2）解析成功，则返回成功状态的ServiceStatus。{status:"1",dataset:{}}
     */
    public Map resolveDataSetFile(MultipartFile datasetFile) {
        Map<String, Object> map = new HashMap<>();
        if (datasetFile != null) {

            String fileSuffix = FileUtils.getFileSuffix(datasetFile.getOriginalFilename());
            if (!EXPORT_DATASET_FILE_SUFFIX.equals(fileSuffix)) {
                map.put("status", ServiceStatus.Status.Fail);
                map.put("msg", "文件格式不正确，请检查文件的格式（文件后缀名为" + EXPORT_DATASET_FILE_SUFFIX + "）");
            } else {

                try {
                    String dataSetJson = StreamUtils.getStreamString(datasetFile.getInputStream());

                    // Base64解码
                    // dataSetJson = new String(Base64Utils.decodeFromString(dataSetJson));

                    // AES解密
                    dataSetJson = AESUtils.decrypt(dataSetJson, DATASET_FILE_AES_KEY);

                    JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(dataSetJson);
                    if (jsonNode != null) {

                        JsonNode idNode = jsonNode.get("id");
                        JsonNode nameNode = jsonNode.get("name");
                        JsonNode categoryNameNode = jsonNode.get("categoryName");
                        JsonNode dataNode = jsonNode.get("data");

                        // 导入数据集的必须字段：数据集名称，data串
                        String id = idNode != null ? idNode.textValue() : null;
                        String dataSetName = nameNode != null ? nameNode.textValue() : null;
                        String categoryName = categoryNameNode != null ? categoryNameNode.textValue() : null;
                        String dataJson = JsonUtils.toJSONString(dataNode);

                        if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(dataSetName) && StringUtils.isNotEmpty(dataJson)) {
                            // ID、数据集名称和数据集配置JSON串这些字段不能缺少
                            map.put("status", ServiceStatus.Status.Success.toString());
                            map.put("msg", "数据集文件已成功解析。");
                            DashboardDataset dataset = new DashboardDataset();
                            dataset.setId(id);
                            dataset.setName(dataSetName);
                            dataset.setCategoryName(categoryName);
                            dataset.setData(dataJson);
                            map.put("dataset", dataset);
                        } else {
                            map.put("status", ServiceStatus.Status.Fail.toString());
                            map.put("msg", "解析数据集文件数据失败。失败原因：文件内容可能被篡改。");
                        }

                    } else {
                        map.put("status", ServiceStatus.Status.Fail.toString());
                        map.put("msg", "解析数据集文件数据失败。失败原因：文件内容可能被篡改。");
                    }
                } catch (Exception ex) {
                    map.put("status", ServiceStatus.Status.Fail.toString());
                    map.put("msg", "解析数据集文件数据失败。失败原因：文件内容可能被篡改。");
                }

            }
        } else {
            map.put("status", ServiceStatus.Status.Fail.toString());
            map.put("msg", "解析文件失败。");
        }
        return map;
    }


    /**
     * 导入数据集文件（上传文件、解析并保存数据集的数据到数据库。这两个操作是一步完成。）
     *
     * @param datasetFile
     * @return
     */
    public Map importDataSet(MultipartFile datasetFile) {
        Map<String, Object> map = new HashMap<>();
        if (datasetFile != null) {

            String fileSuffix = FileUtils.getFileSuffix(datasetFile.getOriginalFilename());
            if (!EXPORT_DATASET_FILE_SUFFIX.equals(fileSuffix)) {
                map.put("status", ServiceStatus.Status.Fail);
                map.put("msg", "文件格式不正确，请检查文件的格式（文件后缀名为" + EXPORT_DATASET_FILE_SUFFIX + "）");
            } else {
                try {
                    String dataSetJson = StreamUtils.getStreamString(datasetFile.getInputStream());

                    // AES解密
                    dataSetJson = AESUtils.decrypt(dataSetJson, DATASET_FILE_AES_KEY);

                    JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(dataSetJson);
                    if (jsonNode != null) {
                        // 导入数据集的必须字段：数据集名称，data串

                        JsonNode dataSetNameNode = jsonNode.get("name");
                        JsonNode dataJsonNode = jsonNode.get("data");
                        if (StringUtils.isNotEmpty(dataSetNameNode.textValue()) && StringUtils.isNotEmpty(JsonUtils.toJSONString(dataJsonNode))) {

                            ServiceStatus saveStatus = save(dataSetJson);
                            if (saveStatus != null && ServiceStatus.Status.Success.toString().equals(saveStatus.getStatus())) {
                                map.put("status", ServiceStatus.Status.Success.toString());
                                map.put("msg", "导入数据集成功");
                            } else {
                                map.put("status", ServiceStatus.Status.Fail.toString());
                                map.put("msg", "导入数据集失败。失败原因：" + saveStatus.getMsg());
                            }

                        } else {
                            map.put("status", ServiceStatus.Status.Fail.toString());
                            map.put("msg", "解析数据集文件数据失败。失败原因：文件内容可能被篡改。");
                        }

                    } else {
                        map.put("status", ServiceStatus.Status.Fail.toString());
                        map.put("msg", "解析数据集文件数据失败。失败原因：文件内容可能被篡改。");
                    }
                } catch (Exception ex) {
                    map.put("status", ServiceStatus.Status.Fail.toString());
                    map.put("msg", "解析数据集文件数据失败。失败原因：文件内容可能被篡改。");
                }
            }
        } else {
            map.put("status", ServiceStatus.Status.Fail.toString());
            map.put("msg", "解析文件失败。");
        }
        return map;
    }

    /**
     * 保存导入的数据集
     *
     * @param json 数据集JSON
     * @return ServiceStatus
     */
    public ServiceStatus saveImportDataset(String json) {
        try {
            DashboardDataset dataset = JsonUtils.parseObject(json, DashboardDataset.class);
            if (dataset != null && StringUtils.isNotBlank(dataset.getId()) && datasetDao.getDataset(dataset.getId()) != null) {
                // 数据集ID已经存在，不允许再次导入
                return new ServiceStatus(ServiceStatus.Status.Fail, "该数据集已经存在记录当中，无须再次导入！");
            } else {
                if (StringUtils.isEmpty(dataset.getCategoryName())) {
                    dataset.setCategoryName("默认分类");
                }
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("dataset_name", dataset.getName());
                paramMap.put("category_name", dataset.getCategoryName());
                if (datasetDao.countExistDatasetName(paramMap) <= 0) {
                    datasetDao.save(dataset);
                    return new ServiceStatus(ServiceStatus.Status.Success, "成功", dataset.getId());
                } else {
                    return new ServiceStatus(ServiceStatus.Status.Fail, "对应名称的数据集已经存在，请修改数据集名称后再保存！");
                }
            }
        } catch (Exception e) {
            return new ServiceStatus(ServiceStatus.Status.Fail, "数据集保存失败！");
        }
    }


}
