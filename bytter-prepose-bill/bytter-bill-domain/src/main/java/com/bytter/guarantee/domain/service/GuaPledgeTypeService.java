package com.bytter.guarantee.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.dao.GuaPledgeTypeDao;
import com.bytter.guarantee.domain.entity.GuaPledgeType;
import com.bytter.guarantee.domain.vo.GuaPledgeTypeVO;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaPledgeTypeService
 * @Description 抵质押物类型维护Service
 * @date 2018年12月11日
 */
@Service("guaPledgeTypeService")
@Transactional
public class GuaPledgeTypeService extends BaseService<GuaPledgeType, String> {
    @Autowired
    GuaPledgeTypeDao guaPledgeTypeDao;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    /**
     * 查询抵质押物类型
     * 
     * @param input
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryGuaPledgeType(DataTablesInput input) {
        return guaPledgeTypeDao.queryGuaPledgeType(input);
    }
    /**
     * 保存或更新抵质押物类型
     * 
     * @param entity
     */
    public void saveOrUpdateGuaPledgeType(GuaPledgeType entity, ICurrentUser user) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setValidSign("1");
            entity.setCreateDate(DateUtils.getDate());
            entity.setCreateBy(user.getId());
            this.save(entity);
            this.getEntityDao().flush();
        } else {
            entity.setUpdateBy(user.getId());
            entity.setUpdateDate(DateUtils.getDate());
            this.update(entity);
        }
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(entity.getId());
        i18nDTO.setLocale(LocaleContextHolder.getLocale().toString());
        i18nDTO.setContext(entity.getTypeName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
    }
    /**
     * 校验抵质押物类型维护表中的相应数据能否被修改和删除
     * 
     * @param id
     * @return
     */
    public boolean dateCheck(String id) {
        return guaPledgeTypeDao.dateCheck(id);
    }
    /**
     * 通过id得到抵质押物类型信息
     * 
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public GuaPledgeType getGuaPledgeTypeById(String id) {
        return guaPledgeTypeDao.getGuaPledgeTypeById(id);
    }
    @Transactional(readOnly = true)
    public List<GuaPledgeTypeVO> getListByValidSign() {
        return guaPledgeTypeDao.getListByValidSign();
    }
    @Transactional(readOnly = true)
    public List<GuaPledgeTypeVO> getListByValidSign1(String typeAttr) {
        return guaPledgeTypeDao.getListByValidSign1(typeAttr);
    }
    /**
     * 修改抵质押有效validSig为0
     * 
     * @param id
     */
    public void deleteGuaPledgeType(String id, ICurrentUser user) {
        GuaPledgeType guaPledgeType = guaPledgeTypeDao.getGuaPledgeTypeById(id);
        guaPledgeType.setValidSign("0");
        guaPledgeType.setUpdateBy(user.getId());
        guaPledgeType.setUpdateDate(DateUtils.getDate());
        this.update(guaPledgeType);
        // 删除多语言
        ii18nService.deleteBusinessBaseContext(id);
    }
    /**
     * 根据抵质押物编号查询列表
     */
    public List<GuaPledgeType> queryTypeByCode(String typeCode) {
        return guaPledgeTypeDao.getListByTypeCode(typeCode);
    }
    /**
     * 根据类型名称和类型查询列表
     */
    public GuaPledgeType getListByTypeCode(String typeName, String typeAttr) {
        return guaPledgeTypeDao.getListByTypeCode(typeName, typeAttr);
    }
    /**
     * 删除对象
     * 
     * @param entity
     */
    public void deleteEntity(GuaPledgeType entity) {
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
    public void save(GuaPledgeType entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     * 
     * @param entity
     */
    public void update(GuaPledgeType entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * 
     * @param entity
     */
    public void saveOrUpdate(GuaPledgeType entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<GuaPledgeType, String> getEntityDao() {
        return guaPledgeTypeDao;
    }
}
