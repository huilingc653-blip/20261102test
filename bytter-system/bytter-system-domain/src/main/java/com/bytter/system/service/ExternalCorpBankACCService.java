package com.bytter.system.service;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.core.annotation.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictUtil;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IRegionService;
import com.bytter.system.api.ISysCountryCustService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.RegionDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
import com.bytter.system.dao.ExternalCorpBankACCDao;
import com.bytter.system.entity.ExternalCorpBankAcc;
import com.bytter.system.vo.ExternalCorpBankAccVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 外部单位/客商 账号
 * Created by zhoul on 2017/6/26.
 */
@Service("externalCorpBankAccService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExternalCorpBankACCService extends BaseService<ExternalCorpBankAcc, String> {
    private static final Logger LOG = LoggerFactory.getLogger(ExternalCorpBankACCService.class);
    @Autowired
    private ExternalCorpBankACCDao externalCorpBankACCDao;
    @Autowired
    private ISysCountryCustService sysCountryCustService;
    @Autowired
    private IRegionService regionService;
    @Autowired
    private IDictService dictServiceI;

    @Autowired
    private IBtCurrencyService btCurrencyService;

    @Override
    public BaseDao<ExternalCorpBankAcc, String> getEntityDao() {
        return externalCorpBankACCDao;
    }
    public List<ExternalCorpBankAccVO> listByExternalCorpId(String externalCorpId) {
        List<ExternalCorpBankAccVO> list = this.externalCorpBankACCDao.listByExternalCorpId(externalCorpId);
        injectValue(list);
        return list;
    }
    public void deleteBankAccByExternalCorpId(String externalCorpId) {
        this.externalCorpBankACCDao.deleteBankAccByExternalCorpId(externalCorpId);
    }
    public List<ExternalCorpBankAcc> listBankAccByExternalCorpId(String externalCorpId) {
        return this.externalCorpBankACCDao.listBankAccByExternalCorpId(externalCorpId);
    }
    public void save(ExternalCorpBankAcc externalCorpBankAcc) {
        this.saveEntity(externalCorpBankAcc);
    }
    public void update(ExternalCorpBankAcc externalCorpBankAcc) {
        this.updateEntity(externalCorpBankAcc);
    }

    /**
     * 所有客商信息组件取数
     */
    public DataTablesOutput selectAllExternalDate(DataTablesInput input) {
        String isNative = (String) input.getFormdata().get("isNative");
        input.getFormdata().remove("isNative");
        DataTablesOutput output = new DataTablesOutput();
        if ("1".equals(isNative)) {
            SysCountryCustDTO custDTO = sysCountryCustService.getByNameZh("中国");
            if (custDTO != null) {
                output = this.externalCorpBankACCDao.selectInnerExternalBankAccDate(input, custDTO);
            }
        } else {
            output = this.externalCorpBankACCDao.selectOuterExternalBankAccDate(input);
        }
        injectOutputValue(output);
        return output;
    }

    public ExternalCorpBankAccVO queryByAccAndCurId(String externalAcc, String curId) {
        ExternalCorpBankAccVO vo = this.externalCorpBankACCDao.queryByAccAndCurId(externalAcc, curId);
        injectValue(vo);
        return vo;
    }

    public int isExternal(String payeeAccName, String accCorpId) {
        return this.externalCorpBankACCDao.isExternal(payeeAccName, accCorpId);
    }

    public void injectValue(ExternalCorpBankAccVO externalCorpBankAccVO) {
        if (EmptyUtils.isEmpty(externalCorpBankAccVO)) {
            return;
        }
        injectValue(Collections.singletonList(externalCorpBankAccVO));
    }

    public void injectValue(List<ExternalCorpBankAccVO> externalCorpBankAccVOList) {
        if (EmptyUtils.isEmptyList(externalCorpBankAccVOList)) {
            return;
        }
        List<ExternalCorpBankAccVO> list = new ArrayList<>();
        for (ExternalCorpBankAccVO externalCorpBankAccVO : externalCorpBankAccVOList) {
            if (!EmptyUtils.isEmpty(externalCorpBankAccVO)) {
                list.add(externalCorpBankAccVO);
            }
        }
        if (EmptyUtils.isEmptyList(list)) {
            return;
        }
        injectDictValue(list);
        injectRegionValue(list);
        injectCurrencyValue(list);
    }

    private void injectOutputValue(DataTablesOutput output) {
        if (output == null || EmptyUtils.isEmpty(output.getData())) {
            return;
        }
        injectValue((List<ExternalCorpBankAccVO>) output.getData());
    }

    private void injectDictValue(List<ExternalCorpBankAccVO> list) {
        List<String> dictGroups = DictUtil.getDictGroups(ExternalCorpBankAccVO.class);
        if (EmptyUtils.isEmptyList(dictGroups)) {
            return;
        }
        Map<String, Map<String, DictInDTO>> dictMap = dictServiceI.listDictMap(new ArrayList<>(new HashSet<>(dictGroups)));
        DictUtil.handleList(list, dictMap);
    }

    private void injectRegionValue(List<ExternalCorpBankAccVO> list) {
        List<Field> regionFields = getAnnotationFields(ExternalCorpBankAccVO.class, Region.class);
        if (EmptyUtils.isEmptyList(regionFields)) {
            return;
        }
        Set<String> regionIds = new HashSet<>();
        for (ExternalCorpBankAccVO externalCorpBankAccVO : list) {
            for (Field regionField : regionFields) {
                addIfNotEmpty(regionIds, getStringFieldValue(externalCorpBankAccVO, regionField));
            }
        }
        if (regionIds.isEmpty()) {
            return;
        }
        Map<String, RegionDTO> regionMap = regionService.findRegionMapByIds(new ArrayList<>(regionIds));
        if (EmptyUtils.isEmpty(regionMap)) {
            return;
        }
        for (ExternalCorpBankAccVO externalCorpBankAccVO : list) {
            for (Field regionField : regionFields) {
                Region region = regionField.getAnnotation(Region.class);
                RegionDTO regionDTO = regionMap.get(getStringFieldValue(externalCorpBankAccVO, regionField));
                if (regionDTO != null) {
                    setFieldValue(externalCorpBankAccVO, region.inject(), regionDTO.getName());
                }
            }
        }
    }

    private void injectCurrencyValue(List<ExternalCorpBankAccVO> list) {
        List<Field> currencyFields = getAnnotationFields(ExternalCorpBankAccVO.class, Currencys.class);
        if (EmptyUtils.isEmptyList(currencyFields)) {
            return;
        }
        Set<String> curIds = new HashSet<>();
        for (ExternalCorpBankAccVO externalCorpBankAccVO : list) {
            for (Field currencyField : currencyFields) {
                addIfNotEmpty(curIds, getStringFieldValue(externalCorpBankAccVO, currencyField));
            }
        }
        if (curIds.isEmpty()) {
            return;
        }
        Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyByIds(new ArrayList<>(curIds));
        if (EmptyUtils.isEmpty(currencyMap)) {
            return;
        }
        for (ExternalCorpBankAccVO externalCorpBankAccVO : list) {
            for (Field currencyField : currencyFields) {
                BtCurrencyDTO currencyDTO = currencyMap.get(getStringFieldValue(externalCorpBankAccVO, currencyField));
                if (currencyDTO == null) {
                    continue;
                }
                Currencys currencys = currencyField.getAnnotation(Currencys.class);
                for (Currency currency : currencys.value()) {
                    setFieldValue(externalCorpBankAccVO, currency.inject(), getCurrencyFieldValue(currencyDTO, currency.field()));
                }
            }
        }
    }

    private Object getCurrencyFieldValue(BtCurrencyDTO currencyDTO, String fieldName) {
        String currencyFieldName = StringUtils.isBlank(fieldName) ? "curName" : fieldName;
        Field currencyField = findField(BtCurrencyDTO.class, currencyFieldName);
        if (currencyField == null) {
            LOG.warn("币种注入字段不存在: {}", currencyFieldName);
            return null;
        }
        try {
            currencyField.setAccessible(true);
            return currencyField.get(currencyDTO);
        } catch (IllegalAccessException e) {
            LOG.warn("读取币种字段失败: {}", currencyFieldName, e);
            return null;
        }
    }

    private List<Field> getAnnotationFields(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        List<Field> annotationFields = new ArrayList<>();
        for (Field field : getAllFields(clazz)) {
            if (field.getAnnotation(annotationClass) != null) {
                field.setAccessible(true);
                annotationFields.add(field);
            }
        }
        return annotationFields;
    }

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }
        return fields;
    }

    private Field findField(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    private String getStringFieldValue(Object object, Field field) {
        try {
            Object fieldValue = field.get(object);
            return fieldValue == null ? null : fieldValue.toString();
        } catch (IllegalAccessException e) {
            LOG.warn("读取字段失败: 类={}, 字段={}", object.getClass().getSimpleName(), field.getName(), e);
            return null;
        }
    }

    private void setFieldValue(Object object, String fieldName, Object value) {
        if (StringUtils.isBlank(fieldName) || value == null) {
            return;
        }
        Field field = findField(object.getClass(), fieldName);
        if (field == null) {
            LOG.warn("注入目标字段不存在: 类={}, 字段={}", object.getClass().getSimpleName(), fieldName);
            return;
        }
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            LOG.warn("注入字段失败: 类={}, 字段={}", object.getClass().getSimpleName(), fieldName, e);
        }
    }

    private void addIfNotEmpty(Set<String> values, String value) {
        if (StringUtils.isNotBlank(value)) {
            values.add(value);
        }
    }
}
