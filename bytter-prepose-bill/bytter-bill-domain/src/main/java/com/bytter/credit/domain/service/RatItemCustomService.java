package com.bytter.credit.domain.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.persistence.PropertyFilter;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatItemCustomDao;
import com.bytter.credit.domain.dao.RatItemTypeDao;
import com.bytter.credit.domain.entity.RatItemCustom;
import com.bytter.credit.domain.entity.RatItemType;
import com.bytter.credit.domain.vo.RatItemCustomVO;
import com.bytter.credit.dto.RatSysItemDTO;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
/**
 * @Title: Service
 * @Description: 授信品种
 * @author onlineGenerator
 * @date 2017-07-20 15:46:08
 * @version 1.0
 *
 */
@Service("ratItemCustomService")
@Transactional
public class RatItemCustomService extends BaseService<RatItemCustom, String> {
    @Autowired
    private RatItemCustomDao ratItemCustomDao;
    @Autowired
    private RatBkrationDetailService detailService;
    @Autowired
    private RatItemTypeDao ratItemTypeDao;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    /**
     * 删除对象
     * 
     * @param entity
     */
    public void deleteEntity(RatItemCustom entity) {
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
    public void save(RatItemCustom entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     * 
     * @param entity
     */
    public void update(RatItemCustom entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * 
     * @param entity
     */
    public void saveOrUpdate(RatItemCustom entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<RatItemCustom, String> getEntityDao() {
        return ratItemCustomDao;
    }
    /**
     * 功能介绍 : 根据授信业务主体类型获取对应的系统授信品种。<br>
     * <b>日期：</b>2017年7月20日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatSysItemDTO> getSysRatItemByType(String type) {
        return ratItemCustomDao.getSysRatItemByType(type, null);
    }
    /**
     * 功能介绍 : 根据授信品种类型获取维护的自定义品种。<br>
     * <b>日期：</b>2017年7月20日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatItemCustom> getRatItemByCustomType(String ritCode) {
        return ratItemCustomDao.getRatItemByCustomType(ritCode);
    }
    /**
     * 功能介绍 : 获取维护的授信品种。<br>
     * <b>日期：</b>2017年7月21日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<com.bytter.credit.dto.RatItemCustomDTO> getAllItemCustom(String type) {
        return getAllItemCustom(type, null);
    }
    public List<com.bytter.credit.dto.RatItemCustomDTO> getAllItemCustom(String type,String ritCode) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("eqi_type", type));
        filters.add(new PropertyFilter("nei_status", CommonConstant.STATUE_DELETE.toString()));
        if(StrUtil.isNotEmpty(ritCode)){
            List<RatItemType> list = ratItemTypeDao.getItemTypeByCode(ritCode);
            if(CollectionUtil.isNotEmpty(list)){
                ritCode = list.get(0).getId();
            }
            filters.add(new PropertyFilter("eqs_ritCode", ritCode));
        }
        List<RatItemCustom> ratItemCustoms = ratItemCustomDao.list(filters);
        for (RatItemCustom ratItemCustom : ratItemCustoms) {
            String mulLan = ii18nService.getBusinessBaseContext(ratItemCustom.getId());// 多语言
            if (StringUtils.isNotEmpty(mulLan)) {
                ratItemCustom.setName(mulLan);
            }
        }
        return BeanUtils.copyListProperties(ratItemCustoms, com.bytter.credit.dto.RatItemCustomDTO.class);
    }
    /**
     * 功能介绍 : 设置品种综合位标识。<br>
     * <b>日期：</b>2017年7月21日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    // @Transactional(readOnly = true)
    private void setSysItemCompFlag(RatItemCustom itemCustom) {
        List<RatSysItemDTO> itemDTOs = ratItemCustomDao.getSysRatItemByType(itemCustom.getType().toString(),
                itemCustom.getSysItemCode());
        RatSysItemDTO sysItemDTO = itemDTOs.get(0);
        itemCustom.setFlag(sysItemDTO.getFlag());
    }
    /**
     * 功能介绍 : 根据系统授信品种和授信业务主体获取维护的自定义品种。<br>
     * <b>日期：</b>2017年7月20日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatItemCustom> getRatItemBySysItemAndType(String sysItemCode, Integer type) {
        return ratItemCustomDao.getRatItemBySysItemAndType(sysItemCode, type);
    }
    /**
     * 功能介绍 : 保存自定义授信品种。<br>
     * <b>日期：</b>2017年7月20日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void saveRatItemCustom(RatItemCustom itemCustom, ICurrentUser user) {
        // 定义授信品种名称是否已存在
        List<RatItemCustom> customs2 = ratItemCustomDao.getRatItemByItemNameAndType(itemCustom.getName(),
                itemCustom.getType());
        if (null != customs2 && !customs2.isEmpty()) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMNAME_UNIQUE);
        }
        if (itemCustom.getType().toString().equals(RationConstant.RAT_TYPE_1)) {// 银行授信时一个系统品种可对应多个授信品种名称
            /**
             * 自定义授信品种与对应的系统授信品种只能维护一对一的关系 是否存在系统品种已维护品种
             */
            List<RatItemCustom> customs1 = ratItemCustomDao.getRatItemBySysItemAndType(itemCustom.getSysItemCode(),
                    itemCustom.getType());
            if (null != customs1 && !customs1.isEmpty()) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMCODE_UNIQUE);
            }
        }
        setSysItemCompFlag(itemCustom);// 设置综合位标识
        itemCustom.setStatus(CommonConstant.STATUE_TEMP);
        itemCustom.setCreateBy(user.getId());
        itemCustom.setCreateName(user.getUsername());
        itemCustom.setCreateDate(DateUtils.getDate());
        this.saveEntity(itemCustom);
    }
    /**
     * 功能介绍 : 更新前检查授信品种是否已存在。<br>
     * <b>日期：</b>2017年7月21日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void checkUpdRatItemCustom(RatItemCustom itemCustom) {
        RatItemCustom oriItemCustom = get(itemCustom.getId(), true);
        if (!oriItemCustom.getName().equals(itemCustom.getName())) {// 品种名称变化
            // 定义授信品种名称是否已存在
            List<RatItemCustom> customs2 = ratItemCustomDao.getRatItemByItemNameAndType(itemCustom.getName(),
                    itemCustom.getType());
            if (null != customs2 && !customs2.isEmpty()) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMNAME_UNIQUE);
            }
        }
        if (!oriItemCustom.getSysItemCode().equals(itemCustom.getSysItemCode())) {
            if (oriItemCustom.getType().toString().equals(RationConstant.RAT_TYPE_1)) {// 集团授信时做此项校验
                /**
                 * 自定义授信品种与对应的系统授信品种只能维护一对一的关系 是否存在修改的系统品种已维护品种
                 */
                List<RatItemCustom> customs1 = ratItemCustomDao.getRatItemBySysItemAndType(itemCustom.getSysItemCode(),
                        itemCustom.getType());
                if (null != customs1 && !customs1.isEmpty()) {
                    throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMCODE_UNIQUE);
                }
            }
            // 设置系统品种的综合标识位
            setSysItemCompFlag(itemCustom);// 设置综合位标识
        }
    }
    /**
     * 功能介绍 : 修改授信品种。<br>
     * <b>日期：</b>2017年7月21日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void updateRatItemCustom(RatItemCustom itemCustom, ICurrentUser user) {
        checkUpdRatItemCustom(itemCustom);
        itemCustom.setStatus(CommonConstant.STATUE_TEMP);
        itemCustom.setUpdateBy(user.getId());
        itemCustom.setUpdateName(user.getUsername());
        itemCustom.setUpdateDate(DateUtils.getDate());
        // 修改多语言
        String local = LocaleContextHolder.getLocale().toString();
        ii18nService.deleteBusinessBaseContext(local, itemCustom.getId());// 先删除之前的
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(itemCustom.getId());
        i18nDTO.setLocale(local);
        i18nDTO.setContext(itemCustom.getName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
        ratItemCustomDao.merge(itemCustom);
    }
    /**
     * 功能介绍 : 删除授信品种。<br>
     * <b>日期：</b>2017年7月21日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void deleteRatItemCustom(RatItemCustom itemCustom) {
        // 检查授信品种是否被引用
        if (detailService.checkItemCodeUse(itemCustom.getId())) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMCODE_USE);
        }
        itemCustom.setStatus(CommonConstant.STATUE_DELETE);
        // 未被引用删除
        merge(itemCustom);
        // 删除多语言
        ii18nService.deleteBusinessBaseContext(itemCustom.getId());
    }
    /**
     * 功能介绍 : 查询授信品种维护列表数据。<br>
     * <b>日期：</b>2017年7月21日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput<RatItemCustomVO> listOutputData(DataTablesInput input) {
        return ratItemCustomDao.listOutputData(input);
    }
    /**
     * 功能介绍 : 根据系统授信品种编号获取系统授信品种
     */
    @Transactional(readOnly = true)
    public List<RatSysItemDTO> getRatSysItemByCode(String code) {
        return ratItemCustomDao.getRatSysItemByCode(code);
    }
    /**
     * 功能介绍 : 获取银行授信品种。<br>
     */
    public List<RatItemCustomVO> getRatCustomByBank(String customId) {
        return ratItemCustomDao.getRatCustomByBank(customId);
    }

    /**
     * 通过授信品种名称获取授信品种code
     * @param itemCodeName
     * @return
     */
    public String getItemCodeByName(String itemCodeName,String type) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("eqi_type", type));
        filters.add(new PropertyFilter("nei_status", CommonConstant.STATUE_DELETE.toString()));
        filters.add(new PropertyFilter("eqs_name",itemCodeName));
        List<RatItemCustom> ratSysItemVO = ratItemCustomDao.list(filters);
        if (EmptyUtils.isEmpty(ratSysItemVO)){
            return null;
        }

        return ratSysItemVO.get(0).getId();
    }
}
