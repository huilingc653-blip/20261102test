package com.bytter.supply.domain.payable.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.payable.dao.ScfPayConfigDao;
import com.bytter.supply.domain.payable.dao.ScfPayConfigVersionDao;
import com.bytter.supply.domain.payable.dao.ScfPayTemplateConfigDao;
import com.bytter.supply.domain.payable.entity.ScfPayConfigVersion;
import com.bytter.supply.domain.payable.vo.ScfPayConfigVersionVO;
import com.bytter.supply.domain.payable.vo.ScfPayTemplateProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("scfPayConfigVersionService")
@Transactional(rollbackFor = Exception.class)
public class ScfPayConfigVersionService extends BaseService<ScfPayConfigVersion, String> {

    @Autowired
    private ScfPayConfigVersionDao scfPayConfigVersionDao;

    @Autowired
    private ScfPayConfigDao scfPayConfigDao;

    @Autowired
    private ScfPayTemplateConfigDao scfPayTemplateConfigDao;

    @Override
    public BaseDao<ScfPayConfigVersion, String> getEntityDao() {
        return scfPayConfigVersionDao;
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        //先查询模版  再去关联查询相应产品
        DataTablesOutput dataTablesUnion = scfPayConfigVersionDao.getDataTablesUnion(dataTablesInput);
        List<ScfPayConfigVersionVO> data = dataTablesUnion.getData();
        String versionId = data.stream()
                .map(vo -> "'" + vo.getId() + "'")
                .collect(Collectors.joining(","));
        //拿到了versionId 去找对应的平台和产品
        List<ScfPayTemplateProductVO> resultList = new ArrayList<>();
        if(StringUtils.isNotBlank(versionId)){
            String platFormSql = "select ptc.version_id as id,ptc.platform_id as platformId,spl.platform_name as platformName,ptc.product_id as productId,spr.product_name as productName" +
                    " from scf_pay_template_config ptc " +
                    " left join scf_platform spl on ptc.platform_id = spl.id " +
                    " left join scf_product spr on ptc.product_id = spr.id where version_id in ("+ versionId +")";
            List<ScfPayTemplateProductVO> productVOS = scfPayConfigVersionDao.listBySQLAliasToBean(platFormSql, null, ScfPayTemplateProductVO.class);

            // 将主表数据转为Map（id -> ScfPayConfigVersionVO），用于关联填充
            Map<String, ScfPayConfigVersionVO> versionMap = data.stream()
                    .collect(Collectors.toMap(ScfPayConfigVersionVO::getId, v -> v));

            // 按id（version_id）分组，将同一版本的productName拼接在一起，合并为一个实体
            resultList = productVOS.stream()
                    .collect(Collectors.groupingBy(ScfPayTemplateProductVO::getId))
                    .entrySet().stream()
                    .map(entry -> {
                        List<ScfPayTemplateProductVO> items = entry.getValue();
                        ScfPayTemplateProductVO merged = items.get(0); // 取第一条作为基础
                        // 合并productName
                        String combinedProductName = items.stream()
                                .map(ScfPayTemplateProductVO::getProductName)
                                .filter(Objects::nonNull)
                                .distinct()
                                .collect(Collectors.joining(", "));
                        merged.setProductName(combinedProductName);
                        // 填充主表模板信息
                        ScfPayConfigVersionVO version = versionMap.get(merged.getId());
                        if (version != null) {
                            merged.setTemplateCode(version.getTemplateCode());
                            merged.setTemplateName(version.getTemplateName());
                            merged.setValidSign(version.getValidSign());
                        }
                        return merged;
                    })
                    .collect(Collectors.toList());
            dataTablesUnion.setData(resultList);
            return dataTablesUnion;
        }
        return new DataTablesOutput();
    }

    public void saveScfPayConfigVersion(ScfPayConfigVersionVO vo) {
        ScfPayConfigVersion entity = new ScfPayConfigVersion();
        BeanUtils.copyNotNullProperties(vo, entity);
        scfPayConfigVersionDao.save(entity);
    }

    public void modify(ScfPayConfigVersionVO vo) {
        ScfPayConfigVersion entity = new ScfPayConfigVersion();
        BeanUtils.copyNotNullProperties(vo, entity);
        scfPayConfigVersionDao.update(entity);
    }

    public void deleteColumn(String id) {
        scfPayConfigDao.deleteByTemplateId(id);
        scfPayTemplateConfigDao.deleteConfigByVersionId(id);
        ScfPayConfigVersion entity = this.get(id);
        this.deleteEntity(entity);
    }
}
