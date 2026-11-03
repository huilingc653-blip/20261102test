package com.bytter.guarantee.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.guarantee.domain.dao.GuaWayDao;
import com.bytter.guarantee.domain.entity.GuaWay;
import com.bytter.guarantee.domain.vo.GuaWayVO;
import com.bytter.guarantee.enums.GuaranteeMessageEnum;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
@Service("guaWayService")
@Transactional
public class GuaWayService extends BaseService<GuaWay, String> {
    @Autowired
    private GuaWayDao guaWayDao;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    /**
     * 担保方式维护列表查询
     *
     * @param input 查询参数
     * @return 列表数据
     */
    public DataTablesOutput<GuaWayVO> dataTable(DataTablesInput input) {
        return guaWayDao.dataTable(input);
    }
    /**
     * 添加担保方式维护
     *
     * @param guaWay 担保方式信息
     */
    public void save(GuaWay guaWay, String userId) {
        // 检查是否重复
        checkCode(guaWay);
        super.saveEntity(guaWay);
        this.getEntityDao().flush();
        // 新增当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(guaWay.getId());
        i18nDTO.setLocale(LocaleContextHolder.getLocale().toString());
        i18nDTO.setContext(guaWay.getName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
    }
    /**
     * 修改担保方式提交
     */
    public void update(GuaWay guaWay, String userId) {
        // 判断code是否重复
        checkCode(guaWay);
        super.updateEntity(guaWay);
        // 修改多语言
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(guaWay.getId());
        i18nDTO.setLocale(LocaleContextHolder.getLocale().toString());
        i18nDTO.setContext(guaWay.getName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
    }
    /**
     * 删除业务类型 判断是否引用
     */
    public void deleteGuaWay(String id) {
        if (guaWayDao.isRegister(id)) {
            throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUAWAY_ISREGISTER);
        }
        if (isUsed(id)) {// 是否被引用
            throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUAWAY_ISUSED_NOT_DEL);
        }
        // 删除多语言
        ii18nService.deleteBusinessBaseContext(id);
        this.delete(id);
    }
    /**
     * 判断担保方式是否被引用
     */
    public boolean isUsed(String wayId) {
        return guaWayDao.isUsed(wayId);
    }
    /**
     * 判断是否重复或被引用------
     */
    public void checkCode(GuaWay guaWay) {
        String id = guaWay.getId();
        String code = guaWay.getCode();
        String name = guaWay.getName();
        if (isUsed(id)) {// 是否被引用
            throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUAWAY_ISUSED);
        }
        List<GuaWay> wayCodes = findAllByCode(code, id);
        List<GuaWay> wayNames = findAllByName(name, id);
        if (!wayCodes.isEmpty() && wayCodes.size() > 0) {
            throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUAWAY_CODE_NOT_UNIQUE);
        }
        if (!wayNames.isEmpty() && wayNames.size() > 0) {
            throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUAWAY_NAME_NOT_UNIQUE);
        }
    }
    /**
     * 根据担保方式code查询 --------
     */
    @Transactional(readOnly = true)
    public List<GuaWay> findAllByCode(String code, String id) {
        return guaWayDao.findAllByCode(code, id);
    }
    /**
     * 根据担保方式name查询 --------
     */
    @Transactional(readOnly = true)
    public List<GuaWay> findAllByName(String name, String id) {
        return guaWayDao.findAllByName(name, id);
    }
    @Override
    public BaseDao<GuaWay, String> getEntityDao() {
        return guaWayDao;
    }
    public List<GuaWay> queryGuaWayByCodes(List<String> codeList) {
        return guaWayDao.queryAllByCodes(codeList);
    }
    /**
     * 根据担保合同id获取所有担保合同挂接得担保方式
     *
     * @param params
     * @return
     */
    public List<GuaWayVO> queryGuaWayByConstractIds(Map<String, Object> params) {
        return guaWayDao.getGuaWayByConstractIds(params);
    }
}
