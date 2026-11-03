package com.bytter.olap.services;

import com.bytter.core.utils.JsonUtils;
import com.bytter.olap.dao.DatasourceDao;
import com.bytter.olap.dataprovider.DataProviderManager;
import com.bytter.olap.dataprovider.annotation.DatasourceParameter;
import com.bytter.olap.dto.ViewDashboardDatasource;
import com.bytter.olap.pojo.DashboardDatasource;
import com.bytter.olap.util.SqlUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by yfyuan on 2016/8/19.
 */
@Repository
public class DatasourceService {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DatasourceDao datasourceDao;

    /**
     * 将DashboardDatasource实体类转化成ViewDashboardDatasource（去除JAVA8特性）
     * Created by chenlw on 2019/5/10.
     *
     * @param list
     * @return
     */
    public List<ViewDashboardDatasource> getViewDatasourceList(List<DashboardDatasource> list) {
        if (list == null) {
            return null;
        }
        List<ViewDashboardDatasource> vlist = new ArrayList<>();
        for (DashboardDatasource datasource : list) {
            vlist.add((ViewDashboardDatasource) ViewDashboardDatasource.TO.apply(datasource));
        }
        for (ViewDashboardDatasource viewDashboardDatasource : vlist) {
            try {
                List<String> fields = DataProviderManager.getProviderFieldByType(viewDashboardDatasource.getType(), DatasourceParameter.Type.Password);
                for (String f : fields) {
                    if (viewDashboardDatasource.getConfig().containsKey(f)) {
                        viewDashboardDatasource.getConfig().put(f, "");
                    }
                }
            } catch (Exception e) {
                LOG.error("DatasourceService.getViewDatasourceList:", e);
            }

        }

        return vlist;
    }

    public ServiceStatus save(String json) {
        JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(json);
        DashboardDatasource datasource = new DashboardDatasource();

        JsonNode nameNode = jsonNode.get("name");
        JsonNode typeNode = jsonNode.get("type");
        JsonNode configNode = jsonNode.get("config");

        datasource.setName(nameNode != null ? nameNode.textValue() : null);
        datasource.setType(typeNode != null ? typeNode.textValue() : null);
        datasource.setConfig(JsonUtils.toJSONString(configNode));

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("source_name", datasource.getName());
        if (datasourceDao.countExistDatasourceName(paramMap) <= 0) {
            datasource.setId(SqlUtils.getUuid());
            datasourceDao.save(datasource);
            return new ServiceStatus(ServiceStatus.Status.Success, "success");
        } else {
            return new ServiceStatus(ServiceStatus.Status.Fail, "Duplicated Name!");
        }
    }

    public ServiceStatus update(String json) {
        JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(json);
        DashboardDatasource datasource = new DashboardDatasource();

        JsonNode nameNode = jsonNode.get("name");
        JsonNode typeNode = jsonNode.get("type");
        JsonNode configNode = jsonNode.get("config");
        JsonNode idNode = jsonNode.get("id");

        datasource.setName(nameNode != null ? nameNode.textValue() : null);
        datasource.setType(typeNode != null ? typeNode.textValue() : null);
        datasource.setConfig(JsonUtils.toJSONString(configNode));
        datasource.setId(idNode != null ? idNode.textValue() : null);
        datasource.setUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("datasource_id", datasource.getId());
        paramMap.put("source_name", datasource.getName());
        if (datasourceDao.countExistDatasourceName(paramMap) <= 0) {
            datasourceDao.update(datasource);
            return new ServiceStatus(ServiceStatus.Status.Success, "success");
        } else {
            return new ServiceStatus(ServiceStatus.Status.Fail, "Duplicated Name!");
        }
    }

    public ServiceStatus delete(String id) {
        datasourceDao.delete(id);
        return new ServiceStatus(ServiceStatus.Status.Success, "success");
    }

}
