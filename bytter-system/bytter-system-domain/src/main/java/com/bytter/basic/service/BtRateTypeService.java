package com.bytter.basic.service;
import com.bytter.basic.dao.BtRateTypeDao;
import com.bytter.basic.entity.BtRateType;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.enums.SystemMessageEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 汇率类型
 * @date 2020-1-19 11:14:22
 */
@Service("btRateTypeServiceBase")
@Transactional
public class BtRateTypeService extends BaseService<BtRateType, String> {
    @Autowired
    private BtRateTypeDao btRateTypeDao;
    @Autowired
    private Ii18nService i18nServiceI;
    @Override
    public BaseDao<BtRateType, String> getEntityDao() {
        return btRateTypeDao;
    }
    /**
     * 保存汇率类型
     *
     * @param entity
     */
    public void saveBtRateType(BtRateType entity, String userLoginName, String locale) {
        entity.setCreateBy(userLoginName);
        entity.setCreateDate(DateUtils.getDate());
        super.saveEntity(entity);
        //新增当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(entity.getId());
        i18nDTO.setLocale(locale);
        i18nDTO.setContext(entity.getTypeName());
        i18nServiceI.saveOrUpdateBusinessBaseContext(i18nDTO);
    }
    /**
     * 修改汇率类型
     *
     * @param entity
     */
    public void updateBtRateType(BtRateType entity, String userLoginName, String locale) {
        entity.setUpdateBy(userLoginName);
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        //更新当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(entity.getId());
        i18nDTO.setLocale(locale);
        i18nDTO.setContext(entity.getTypeName());
        i18nServiceI.saveOrUpdateBusinessBaseContext(i18nDTO);
    }
    /**
     * 获取有效的汇率类型
     *
     * @return
     */
    public List<BtRateType> listByValidSign() {
        return this.btRateTypeDao.listBy("validSign", CommonConstant.YES);
    }
    /**
     * 判断是否已存在
     *
     * @param typeName
     * @return
     */
    public boolean isExistRateType(String typeName, String id) {
        boolean flag = false;
        List<BtRateType> list = this.btRateTypeDao.listBy("typeName", typeName);
        if (list != null) {
            if (StringUtils.isBlank(id) && !EmptyUtils.isEmptyList(list)) {
                flag = true;
            } else if (StringUtils.isNotBlank(id)) {
                if (list.size() > 0) {
                    if (!id.equals(list.get(0).getId())) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }
    /**
     * 校验汇率类型跟其他业务的关联
     * todo 汇率业务异常为啥用信贷的异常抛出
     *
     * @param id
     */
    public void checkAssociated(String id) {
        if (btRateTypeDao.checkAssociated(id) > 0)
            throw new ParameterException(SystemMessageEnum.DATA_REFERENCED_EXIST);
    }
}
