package com.bytter.system.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.system.api.IBaseRateService;
import com.bytter.system.api.dto.TbBaseRateDTO;
import com.bytter.system.api.enums.BaseDataMessageEnum;
import com.bytter.system.api.enums.RatMessageEnum;
import com.bytter.system.constants.BaseDataConstant;
import com.bytter.system.dao.TbBaseRateDao;
import com.bytter.system.entity.TbBaseRate;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
@Service("baseRateService")
@Transactional
@DubboService
public class BaseRateService  extends BaseService<TbBaseRate, String> implements IBaseRateService {
    @Autowired
    private TbBaseRateDao tbBaseRateDao;
    @Autowired
    private RateMaintenceService rateMaintenceService;
    @Override
    public BaseDao<TbBaseRate, String> getEntityDao() {
        return tbBaseRateDao;
    }
    /** 查询本位币利率类型 */
    public List<TbBaseRateDTO> getBaseRateByType(String type) {
        List<TbBaseRate> baseRateList = tbBaseRateDao.getBaseRateByType(type);
        if (EmptyUtils.isEmpty(baseRateList)){
            return Collections.emptyList();
        }
        return BeanUtils.copyListProperties(baseRateList,TbBaseRateDTO.class);
    }
    /** 通过id查询基础利率列表 */
    public List<TbBaseRateDTO> getBaseRateListById(String id) {
        List<TbBaseRate> tbBaseRateList = tbBaseRateDao.getTbBaseRateList(id);
        if (EmptyUtils.isEmpty(tbBaseRateList)){
            return Collections.emptyList();
        }
        return BeanUtils.copyListProperties(tbBaseRateList,TbBaseRateDTO.class);
    }
    /** 通过id查询基础利率信息 */
    public TbBaseRateDTO getBaseRateById(String id){
        TbBaseRate tbBaseRate = tbBaseRateDao.get(id);
        if (EmptyUtils.isEmpty(tbBaseRate)) {
            return null;
        }
        TbBaseRateDTO dto = new TbBaseRateDTO();
        BeanUtils.copyNotNullProperties(tbBaseRate, dto);
        return dto;
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(TbBaseRate entity) {
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
    public void save(TbBaseRate entity) {
        checkCodeOrName(entity, null, true);
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(TbBaseRate entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(TbBaseRate entity) {
        super.saveOrUpdate(entity);
    }
    /**
     * 查询央行基准利率是否存在 1 是 0 否
     */
    public boolean isBankBaseRateExist() {
        return !ListUtils.isNullOrEmpty(tbBaseRateDao.isBankBaseRateExist("1"));
    }
    /**
     * 根据类型查询基准利率基准
     */
    public List<TbBaseRate> queryBankBaseRateByType(String type) {
        return tbBaseRateDao.isBankBaseRateExist(type);
    }
    /**
     * 删除基准利率
     */
    public void doDelete(String id) {
        List<TbBaseRate> tbBaseRate = tbBaseRateDao.getTbBaseRateList(id);
        TbBaseRate baseRate = tbBaseRate.get(0);
//        // 系统预设，不能删除
//        if (BaseDataConstant.IS_BANKBASERATE.equals(baseRate.getIsBankBaseRate())) {
//            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_SYSTEM_SET);
//        }
        boolean check = rateMaintenceService.checkBaseRateById(id);
        if (check) {
            // 利率类型已被利率引用，不能操作
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_BASERATE_IS_USED);
        }
        deleteEntity(baseRate);
    }
    /**
     * 修改基准利率
     */
    public void doUpdate(TbBaseRate tbBaseRate, String id) {
        checkCodeOrName(tbBaseRate, id, false);
//        // 系统预设，不能修改
//        List<TbBaseRate> t = tbBaseRateDao.getTbBaseRateList(tbBaseRate.getId());
//        TbBaseRate tt = t.get(0);
//        if (BaseDataConstant.IS_BANKBASERATE.equals(tt.getIsBankBaseRate())) {
//            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_SYSTEM_SET);
//        }
        saveOrUpdate(tbBaseRate);
    }
    /**
     * 判断是否重复------
     *
     * @param tbBaseRate
     * @return
     */
    public void checkCodeOrName(TbBaseRate tbBaseRate, String id, boolean isAdd) {
        String code = tbBaseRate.getCode();
        String name = tbBaseRate.getBaseTypeName();
        int res = tbBaseRateDao.findAllBy(code, name, id, isAdd);
        if (res == 1) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_CODE_EXIST);
        }
        if (res == 2) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_BASETYPENAME_EXISTENCE);
        }
    }
    public List<TbBaseRate> getListTbBaseRate() {
        return tbBaseRateDao.getListTbBaseRate();
    }
}
