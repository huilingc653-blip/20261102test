package com.bytter.basic.service.impl;
import com.bytter.basic.entity.SysCountry;
import com.bytter.basic.service.SysCountryService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.ICountryService;
import com.bytter.system.api.dto.SysCountryDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.List;
/**
 * 国家信息接口实现类
 *
 * @author qinl
 * @since 2019-07-17
 */
@Transactional
@Service("sysCountryServiceI")
@DubboService
public class SysCountryServiceImpl implements ICountryService {
    /**
     * 国家信息业务层
     */
    @Autowired
    private SysCountryService sysCountryService;
    @Override
    public SysCountryDTO getByCode(String code) {
        SysCountry sysCountry = sysCountryService.getByCode(code);
        if (sysCountry == null) {
            return null;
        }
        SysCountryDTO dto = new SysCountryDTO();
        BeanUtils.copyNotNullProperties(sysCountry, dto);
        return dto;
    }
    @Override
    public List<SysCountryDTO> listByContinent(String continent) {
        List<SysCountry> list = sysCountryService.listByContinent(continent);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return BeanUtils.copyListProperties(list, SysCountryDTO.class);
    }
}
