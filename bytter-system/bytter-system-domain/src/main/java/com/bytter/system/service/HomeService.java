package com.bytter.system.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.system.dao.HomeDao;
import com.bytter.system.entity.Home;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Created by zhoul on 2017/4/12.
 */
@Service("homeService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HomeService extends BaseService<Home, String> {
    @Autowired
    private HomeDao homeDao;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public BaseDao<Home, String> getEntityDao() {
        return homeDao;
    }
    public void save(List<Map<String, Object>> content, String userId) {
        Home home = new Home();
        home.setUserId(userId);
        home.setCreateTime(new Date());
        try {
            home.setContent(objectMapper.writeValueAsString(content));
        } catch (JsonProcessingException e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
        }
        homeDao.save(home);
    }
    @Transactional
    public void resetDefault(String userId) {
        homeDao.resetDefault(userId);
    }
    public String newest(String userId) {
        Home home = homeDao.getUserNewest(userId);
        if (home != null) {
            return home.getContent();
        }
        return "";
    }
    public Boolean judgeRepeat(List<Map<String, Object>> content) {
        List<String> allIdList = new ArrayList<>();
        for (Map<String, Object> rows : content) {
            List<LinkedHashMap<String, Object>> columns = (List<LinkedHashMap<String, Object>>) rows.get("columns");
            for (LinkedHashMap<String, Object> object : columns) {
                if (allIdList.contains((String) object.get("name"))) {
                    return true;
                }
                allIdList.add((String) object.get("name"));
            }
        }
        return false;
    }
}
