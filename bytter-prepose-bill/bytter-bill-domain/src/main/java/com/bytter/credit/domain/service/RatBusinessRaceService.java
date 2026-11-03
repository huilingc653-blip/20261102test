package com.bytter.credit.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.credit.domain.dao.RatBusinessRaceDao;
import com.bytter.credit.domain.entity.RatBusinessRace;
import com.bytter.credit.domain.vo.RatBusinessRaceVO;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service("ratBusinessRaceService")
@Transactional
public class RatBusinessRaceService extends BaseService<RatBusinessRace, String> {
    @Autowired
    private RatBusinessRaceDao ratBusinessRaceDao;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    public DataTablesOutput<RatBusinessRaceVO> dataTable(DataTablesInput input) {
        return ratBusinessRaceDao.dataTable(input);
    }
    /**
     * 新增业务种类保存
     */
    public void save(RatBusinessRace ratBusinessRace, String userId) {
        // 检查是否重复
        checkRaceCode(ratBusinessRace);
        super.saveEntity(ratBusinessRace);
        this.getEntityDao().flush();
        // 新增当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(ratBusinessRace.getId());
        i18nDTO.setLocale(LocaleContextHolder.getLocale().toString());
        i18nDTO.setContext(ratBusinessRace.getRaceName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
    }
    /**
     * 修改业务种类提交
     */
    public void update(RatBusinessRace ratBusinessRace, String userId) {
        // 判断fcbusrace是否重复
        checkRaceCode(ratBusinessRace);
        String local = LocaleContextHolder.getLocale().toString();
        ii18nService.deleteBusinessBaseContext(local, ratBusinessRace.getId());// 先删除之前的多语言维护
        // 新增当前语种
        I18nDTO i18nDTO = new I18nDTO();
        i18nDTO.setKeyId(ratBusinessRace.getId());
        i18nDTO.setLocale(LocaleContextHolder.getLocale().toString());
        i18nDTO.setContext(ratBusinessRace.getRaceName());
        ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
        super.updateEntity(ratBusinessRace);
    }
    /**
     * 删除业务类型 判断是否引用
     */
    public void deleteRatBusinessRace(String id) {
        RatBusinessRace fcBusRace = this.get(id);
        // 判断fcBusRace是否被引用
        if (ratBusinessRaceDao.isUsed(fcBusRace.getCustomId(), fcBusRace.getId())) {
            throw new ParameterException(RatMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_ISUSED);
        }
        // 删除多语言
        ii18nService.deleteBusinessBaseContext(id);
        this.delete(id);
    }
    /** 判断fcBusRace是否被引用 */
    public boolean isUsed(String customId, String raceName) {
        return ratBusinessRaceDao.isUsed(customId, raceName);
    }
    /**
     * 判断是否重复------
     */
    public void checkRaceCode(RatBusinessRace ratBusinessRace) {
        String id = ratBusinessRace.getId();
        String raceName = ratBusinessRace.getRaceName();
        String customId = ratBusinessRace.getCustomId();
        if (isUsed(customId, id)) {// 是否被引用
            throw new ParameterException(RatMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_ISNOTEDIT);
        }
        List<RatBusinessRace> races = findAllBy(id, raceName);
        if (!races.isEmpty() && races.size() > 0) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_RACE_UNIQUE);
        }
    }
    /**
     * 根据业务名称查询 --------
     */
    @Transactional(readOnly = true)
    public List<RatBusinessRace> findAllBy(String id, String raceName) {
        return ratBusinessRaceDao.findAllBy(id, raceName);
    }
    /***
     * 根据授信品种id查询业务品种 --------
     */
    public List<RatBusinessRaceVO> findAllByCustomId(String customId) {
        return ratBusinessRaceDao.findAllByCustomId(customId);
    }
    @Override
    public BaseDao<RatBusinessRace, String> getEntityDao() {
        return ratBusinessRaceDao;
    }
}
