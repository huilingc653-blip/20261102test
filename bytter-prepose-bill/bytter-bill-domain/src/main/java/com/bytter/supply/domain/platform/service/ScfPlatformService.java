package com.bytter.supply.domain.platform.service;



import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.supply.domain.platform.dao.ScfPlatformDao;
import com.bytter.supply.domain.platform.dao.ScfProductDao;
import com.bytter.supply.domain.platform.entity.ScfPlatform;
import com.bytter.supply.domain.platform.entity.ScfProduct;
import com.bytter.supply.domain.platform.vo.ScfPlatformVo;
import com.bytter.supply.domain.platform.vo.ScfProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ScfPlatformManagerService")
public class ScfPlatformService extends BaseService<ScfPlatform, String> {

    @Autowired
    private ScfPlatformDao scfPlatformDao;

    @Autowired
    private ScfProductDao scfProductDao;


    @Override
    public BaseDao<ScfPlatform, String> getEntityDao() {
        return scfPlatformDao;
    }


    public List<ScfPlatformVo> getPlatformJson() {
        return scfPlatformDao.getPlatformJson();
    }

    public List<ScfProductVo> getProductJson(String platformId) {
        return scfProductDao.getListByPlatformId(platformId);
    }

    public List<ScfProductVo> getAllProductJson() {
        return scfProductDao.getAllProductJson();
    }

    public ScfProductVo getProductById(String id) {
        ScfProduct product = scfProductDao.get(id);
        if (ObjectUtil.isNotEmpty(product)){
            ScfProductVo scfProductVo = BeanUtil.copyProperties(product, ScfProductVo.class);
            return scfProductVo;
        }
        return null;
    }
}
