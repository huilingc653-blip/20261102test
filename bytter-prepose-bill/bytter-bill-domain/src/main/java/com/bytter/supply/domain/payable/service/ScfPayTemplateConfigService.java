package com.bytter.supply.domain.payable.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.payable.dao.ScfPayConfigDao;
import com.bytter.supply.domain.payable.dao.ScfPayConfigVersionDao;
import com.bytter.supply.domain.payable.dao.ScfPayTemplateConfigDao;
import com.bytter.supply.domain.payable.dto.ScfPayProductDTO;
import com.bytter.supply.domain.payable.dto.ScfPayTemplateBaseInfoDTO;
import com.bytter.supply.domain.payable.entity.ScfPayConfigVersion;
import com.bytter.supply.domain.payable.entity.ScfPayTemplateConfig;
import com.bytter.supply.domain.payable.vo.ScfPayTemplateConfigVO;
import com.bytter.supply.domain.payable.vo.ScfPayTemplateViewConfigVO;
import com.bytter.supply.domain.platform.dao.ScfPlatformDao;
import com.bytter.supply.domain.platform.dao.ScfProductDao;
import com.bytter.supply.domain.platform.vo.ScfPlatformVo;
import com.bytter.supply.domain.platform.vo.ScfProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("scfPayTemplateConfigService")
@Transactional(rollbackFor = Exception.class)
public class ScfPayTemplateConfigService extends BaseService<ScfPayTemplateConfig, String> {

    @Autowired
    private ScfPayConfigVersionDao scfPayConfigVersionDao;

    @Autowired
    private ScfPayTemplateConfigDao scfPayTemplateConfigDao;

    @Autowired
    private ScfPayConfigDao scfPayConfigDao;

    @Autowired
    private ScfProductDao scfProductDao;

    @Autowired
    private ScfPlatformDao scfPlatformDao;

    @Override
    public BaseDao<ScfPayTemplateConfig, String> getEntityDao() {
        return scfPayTemplateConfigDao;
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return null;
    }

    /**
     * 融资产品是否有模板在引用
     * @param productIdList
     * @return
     */
    public Boolean isUsingProductTemplate(List<String> productIdList){
        return scfPayTemplateConfigDao.isUsingProductTemplate(productIdList);
    }

    public Boolean isUsingCustFieldTemplate(List<String> custFieldIdList){
        return scfPayTemplateConfigDao.isUsingCustFieldTemplate(custFieldIdList);
    }
    public void saveScfPayTemplateConfig(ScfPayTemplateConfigVO vo) {
        ScfPayTemplateConfig entity = new ScfPayTemplateConfig();
        BeanUtils.copyNotNullProperties(vo, entity);
        scfPayTemplateConfigDao.save(entity);
    }

    public void modify(ScfPayTemplateConfigVO vo) {
        ScfPayTemplateConfig entity = new ScfPayTemplateConfig();
        BeanUtils.copyNotNullProperties(vo, entity);
        scfPayTemplateConfigDao.update(entity);
    }

    public void deleteColumn(String id) {
        ScfPayConfigVersion scfPayConfigVersion = scfPayConfigVersionDao.get(id);
        if (scfPayConfigVersion != null) {
            scfPayConfigDao.deleteByTemplateId(id);
            scfPayConfigVersionDao.delete(scfPayConfigVersion);
            scfPayTemplateConfigDao.deleteConfigByVersionId(id);
        }
    }

    public List<ScfPlatformVo> getPlatformJson() {
        return scfPlatformDao.getPlatformJson();
    }

    public List<ScfProductVo> getProductJson(String platformId) {
        return scfProductDao.getListByPlatformId(platformId);
    }

    /**
     * 查询版本基础信息及产品配置列表（用于更新/查看回显）
     */
    public ScfPayTemplateViewConfigVO getScfPayTemplateBaseInfoConfig(String versionId) {
        ScfPayConfigVersion version = scfPayTemplateConfigDao.getVersionById(versionId);
//        if (version == null) {
//            throw new BusinessException(ScfMessageEnum.SCF_CONFIG_TEMPLATE_NO_EXISTED);
//        }
        List<ScfPayTemplateConfig> configList = scfPayTemplateConfigDao.listBy("versionId", versionId);

        ScfPayTemplateViewConfigVO result = new ScfPayTemplateViewConfigVO();
        result.setId(version.getId());
        result.setTemplateCode(version.getTemplateCode());
        result.setTemplateName(version.getTemplateName());
        result.setTemplateDesc(version.getTemplateDesc());
        result.setRemark(version.getRemark());
        result.setValidSign(version.getValidSign());

        if (EmptyUtils.isNotEmpty(configList)) {
            List<ScfPayProductDTO> productList = configList.stream().map(config -> {
                ScfPayProductDTO product = new ScfPayProductDTO();
                product.setPlatformId(config.getPlatformId());
                product.setProductId(config.getProductId());
                product.setSort(config.getSort() != null ? config.getSort() : 0);
                return product;
            }).collect(Collectors.toList());
            result.setProductList(productList);
        }
        return result;
    }



}
