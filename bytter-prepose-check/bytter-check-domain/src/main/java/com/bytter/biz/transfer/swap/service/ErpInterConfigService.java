package com.bytter.biz.transfer.swap.service;
import com.bytter.biz.transfer.swap.dao.ErpInterConfigDao;
import com.bytter.biz.transfer.swap.entity.ErpInterConfig;
import com.bytter.biz.transfer.swap.vo.ErpInterConfigFillVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/28.
 */
@Service("erpInterConfigService")
@Transactional
public class ErpInterConfigService extends BaseService<ErpInterConfig,String> {
    @Autowired
    private ErpInterConfigDao erpInterConfigDao;
    @Autowired
    private ErpInterConfigFillService erpInterConfigFillService;
    @Override
    public BaseDao<ErpInterConfig, String> getEntityDao() {
        return erpInterConfigDao;
    }
    public Map<String, Object> selectByConfig(String type) {
        List<ErpInterConfig> configList = erpInterConfigDao.listBy("type", type);
        Map<String, Object> map = new HashMap<String, Object>();
        for (ErpInterConfig config : configList) {
            String value = config.getValue();
            map.put(config.getCode(), value);
        }
        return map;
    }
    public void saveConfigs(Map<String, String[]> parameterMap) {
        List<ErpInterConfig> configList = new ArrayList<ErpInterConfig>();
        for (Map.Entry<String, String[]> map : parameterMap.entrySet()){
            String paraKey = map.getKey();
            String[] value = map.getValue();
            String[] data = paraKey.split(CommonConstant.DOT_SPLIT);
            if (data.length != 2) {
                continue;
            }
            ErpInterConfig config = new ErpInterConfig(data[0], data[1], value[0]);
            configList.add(config);
        }
        for (ErpInterConfig config : configList) {
            ErpInterConfig configDB = erpInterConfigDao.getByTypeAndCode(config);
            if (configDB == null) {
                erpInterConfigDao.save(config);
            } else {
                BeanUtils.copyNotNullProperties(config, configDB);
                erpInterConfigDao.update(configDB);
            }
        }
    }
    /**
     * 配置明细信息
     *
     * @return list集合
     */
    @Transactional(readOnly = true)
    public List<ErpInterConfigFillVO> listErpInterConfigFillInfoByType(String type){
        return erpInterConfigFillService.listErpInterConfigFillInfoByType(type);
    }
    /**
     * 获取参数的值
     * @param type
     * @param code
     * @return
     */
    public Integer getValueByTypeAndCode(String type,String code){
        ErpInterConfig config = erpInterConfigDao.getByTypeAndCode(type,code);
        if(StringUtils.isNotEmpty(config)){
            if(StringUtils.isNotEmpty(config.getValue())){
                return Integer.valueOf(config.getValue());
            }
        }
        return Integer.valueOf(0);
    }
}
