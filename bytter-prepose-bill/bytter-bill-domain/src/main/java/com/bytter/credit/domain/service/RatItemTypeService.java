package com.bytter.credit.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.domain.dao.RatItemTypeDao;
import com.bytter.credit.domain.entity.RatItemCustom;
import com.bytter.credit.domain.entity.RatItemType;
import com.bytter.credit.dto.RatItemTypeDTO;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @Title: Service
 * @Description: 授信品种
 * @author onlineGenerator
 * @date 2017-07-20 14:52:52
 * @version 1.0
 *
 */
@Service("ratItemTypeService")
@Transactional
public class RatItemTypeService extends BaseService<RatItemType, String> {
    @Autowired
    private RatItemTypeDao ratItemTypeDao;
    @Autowired
    private RatItemCustomService itemCustomService;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    /**
     * 删除对象
     * 
     * @param entity
     */
    public void deleteEntity(RatItemType entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     * 
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     * 
     * @param entity
     */
    public void save(RatItemType entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     * 
     * @param entity
     */
    public void update(RatItemType entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * 
     * @param entity
     */
    public void saveOrUpdate(RatItemType entity) {
        super.saveOrUpdate(entity);
    }
    /**
     * 功能介绍 : 删除授信品种类型前判断。<br>
     * <b>日期：</b>2017年9月18日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void deleteItemType(String id) {
        RatItemType entity = get(id, true);
        // 是否被授信品种引用
        List<RatItemCustom> itemCustoms = itemCustomService.getRatItemByCustomType(entity.getId());
        if (!EmptyUtils.isEmptyList(itemCustoms)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMTYPE_USE);
        }
        entity.setStatus(CommonConstant.STATUE_DELETE);
        update(entity);
        // 删除多语言
        ii18nService.deleteBusinessBaseContext(id);
    }
    /**
     * 功能介绍 : 保存或更新授信品种类型。<br>
     * <b>日期：</b>2017年7月20日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void saveOrUpdateItemType(RatItemType entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            // 查询编号是否存在相应的授信品种类型
            String ritCode = entity.getRitCode();
            if (StringUtils.isEmpty(ritCode)) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_CODE_NULL);
            }
            List<RatItemType> itemTypes = ratItemTypeDao.getItemTypeByCode(ritCode);
            if (EmptyUtils.isNotEmpty(itemTypes)) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_CODE_EXIST);
            }
            this.save(entity);
            this.getEntityDao().flush();
        } else {
            String local = LocaleContextHolder.getLocale().toString();
            ii18nService.deleteBusinessBaseContext(local, entity.getId());// 先删除之前的多语言维护
            this.update(entity);
        }
        // 新增当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(entity.getId());
        i18nDTO.setLocale(LocaleContextHolder.getLocale().toString());
        i18nDTO.setContext(entity.getRitName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
    }
    /**
     * 功能介绍 : 获取有效的授信品种类型。<br>
     * <b>日期：</b>2017年7月20日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatItemTypeDTO> getAllRatItemType() {
        return ratItemTypeDao.getAllRatItemType();
    }
    /**
     * 查询列表数据
     * 
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput dataTables(DataTablesInput input) {
        return ratItemTypeDao.dataTables(input);
    }
    public BaseDao<RatItemType, String> getEntityDao() {
        return ratItemTypeDao;
    }
}
