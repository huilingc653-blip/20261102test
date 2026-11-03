package com.bytter.system.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.SocketMsg;
import com.bytter.system.config.WebSocketServer;
import com.bytter.system.dao.ConfigDao;
import com.bytter.system.dao.SysConfigLogDao;
import com.bytter.system.entity.Config;
import com.bytter.system.entity.SysConfigLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * Created by zhoul on 2017/4/12.
 */
@Service("configService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigService extends BaseService<Config, String> {
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private SysConfigLogDao sysConfigLogDao;
    @Autowired
    private WebSocketServer webSocketServer;
    @Override
    public BaseDao<Config, String> getEntityDao() {
        return configDao;
    }
    public Map<String, Object> listByType(String type) {
        List<Config> configList = configDao.listBy("type", type);
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> maps = new HashMap<String, Object>();
        for (Config config : configList) {
            String value = config.getValue();
            //处理Checkbox
            if (StringUtils.isNotBlank(value) && value.startsWith("[") && value.endsWith("]")) {
                value = value.replace("[", "").replace("]", "");
                if (value.contains(CommonConstant.COMMA_SPLIT)) {
                    maps.put(config.getName(), value.split(CommonConstant.COMMA_SPLIT));
                } else {
                    String[] str = {value};
                    maps.put(config.getName(), str);
                }
            } else {
                maps.put(config.getName(), value);
            }
        }
        map.put(type, maps);
        return map;
    }

    public void saveOrUpdate(Map<String,String> configDTO) {
        if(StringUtils.isNotBlank(configDTO.get("id"))){
            Config config = configDao.get(configDTO.get("id"), true);
            config.setValue(configDTO.get("value"));
            configDao.saveOrUpdate(config);
        }else {
            Config config = BeanUtils.copyMapProperties(configDTO,Config.class);
            configDao.saveOrUpdate(config);
        }
    }

    public Map<String,Object> getConfig(String type,String name){
        Config byTypeAndName = configDao.getByTypeAndName(type, name);
        if(byTypeAndName == null || StringUtils.isBlank(byTypeAndName.getId())){
            return null;
        }
        return BeanUtils.beanToMap(byTypeAndName);
    }

    /**
     * 保存或更新配置
     *
     * @param parasMap
     */
    public void saveOrUpdate(Map<String, String[]> parasMap,String uid) {
        List<Config> configList = new ArrayList<Config>();
        String userName = null;
        if (StringUtils.isNotEmpty(parasMap.get("UName"))) {
            userName = parasMap.get("UName")[0];
            parasMap.remove("UName");
        }
        for (Map.Entry<String, String[]> e : parasMap.entrySet()) {
            String paraKey = e.getKey();
            String[] data = paraKey.split(CommonConstant.DOT_SPLIT);
            if (data.length != 2) {
                continue;
            }
            String values = null;
            if (paraKey.endsWith("[]")) {
                String[] value = e.getValue();
                values = Arrays.toString(value);
                values = values.replace(", ", ",");
            } else {
                values = e.getValue()[0];
            }
            Config config = new Config(data[0], data[1].replace("[]", ""), values);
            configList.add(config);
        }
        for (Config config : configList) {
            Config configDB = configDao.getByTypeAndName(config);
            SysConfigLog sysConfigLog = new SysConfigLog();
            if (configDB == null) {
                configDao.saveEntity(config);
            } else {
                if (!config.getValue().equals(configDB.getValue())) {
                    sysConfigLog = new SysConfigLog(configDB.getType(), configDB.getName(), configDB.getValue(), config.getValue(), new Date(), userName);
                    sysConfigLogDao.saveEntity(sysConfigLog);
                    //如果UI变更 发送消息通知用户刷新浏览器
                    if("uiStyle".equalsIgnoreCase(config.getName())){
                        webSocketServer.broadcastMessage(SocketMsg.build(SocketMsg.Type.UI_UPDATE), uid);
                    }
                }
                BeanUtils.copyNotNullProperties(config, configDB);
                configDao.updateEntity(configDB);
            }
        }
    }
    @Transactional(readOnly = true)
    /**
     * 根据业务类型，和参数名获取值
     *
     * @param type
     * @param name
     * @return
     */
    public Config getByTypeAndName(String type, String name) {
        return configDao.getByTypeAndName(type, name);
    }
    /**
     * 获取全局配置（未登录/空租户场景专用），返回实体对象。
     *
     * @param type 配置类型
     * @param name 配置名
     * @return 全局配置对象
     */
    public Config getGlobalConfig(String type, String name) {
        return configDao.getGlobalConfig(type, name);
    }
    /**
     * 获取全局配置（未登录/空租户场景专用），返回 Map。
     *
     * @param type 配置类型
     * @param name 配置名
     * @return 全局配置 Map
     */
    public Map<String, Object> getGlobalConfigMap(String type, String name) {
        Config config = configDao.getGlobalConfig(type, name);
        if (config == null || StringUtils.isBlank(config.getId())) {
            return null;
        }
        return BeanUtils.beanToMap(config);
    }
    public ConfigDTO getDTOByTypeAndName(String type, String name) {
        Config config = getByTypeAndName(type, name);
        return Optional.ofNullable(config).map(item -> new ConfigDTO(item.getValue())).orElse(new ConfigDTO(""));
    }
    /**
     * 获取系统统一默认登入方式
     *
     * @return 0用户密码，1银行密码+证书，2证书
     */
    public String getDefaultLoginType() {
        String defaultLoginType = "0";
        Config config = getGlobalConfig(ConfigConstant.SYS, SysConfigConstant.ENCRYPT);
        // 全局配置缺失（未登录/空租户下无 encrypt 行）时按 "0 不使用证书" 处理，默认用户密码登录
        String encrypt = config == null ? "0" : new ConfigDTO(config.getValue()).toString();
        //如果证书类型选择  0不使用证书则默认用户密码登入其他则根据登录方式设置
        if (!"0".equals(encrypt)) {
            //获取系统默认设置登录方式
            Config defaultConfig = getGlobalConfig(ConfigConstant.SYS, SysConfigConstant.LOGINTYPE);
            defaultLoginType = defaultConfig == null ? "0" : new ConfigDTO(defaultConfig.getValue()).toString();
        }
        return defaultLoginType;
    }

    public List<Config> listConfig(String type, String value, List<String> names) {
        return configDao.listConfig(type, value,names);
    }
}
