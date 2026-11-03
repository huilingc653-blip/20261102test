package com.bytter.basic.service.impl;
import com.bytter.basic.entity.SysCountryCust;
import com.bytter.basic.service.SysCountryCustService;
import com.bytter.basic.vo.SysCountryCustVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ISysCountryCustService;
import com.bytter.system.api.dto.SysCountryCustDTO;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service("sysCountryCustServiceI")
@DubboService
@Transactional
public class SysCountryCustServiceImpl implements ISysCountryCustService {
    @Autowired
    private SysCountryCustService sysCountryCustService;
    /**
     * 根据中文名称获取地点信息
     *
     * @param nameZh
     * @return
     */
    @Override
    public SysCountryCustDTO getByNameZh(String nameZh) {
        SysCountryCustVO sysCountryCustVO = sysCountryCustService.getByNameZh(nameZh);
        if(sysCountryCustVO!=null){
            SysCountryCustDTO sysCountryCustDTO = new SysCountryCustDTO();
            BeanUtils.copyNotNullProperties(sysCountryCustVO, sysCountryCustDTO);
            return sysCountryCustDTO;
        }
        return null;
    }
    /**
     * 根据国家和中文名称获取地区信息
     *
     * @param nameZh 中文名称
     * @param sysId  国家ID
     * @return Region
     */
    @Override
    public SysCountryCustDTO getByNameZhAndSysId(String nameZh, String sysId) {
        SysCountryCustDTO sysCountryCustDTO = new SysCountryCustDTO();
        SysCountryCustVO sysCountryCustVO = sysCountryCustService.getByNameZhAndSysId(nameZh, sysId);
        BeanUtils.copyNotNullProperties(sysCountryCustVO, sysCountryCustDTO);
        return sysCountryCustDTO;
    }
    @Override
    public List<SysCountryCustDTO> listAll() {
        List<SysCountryCust> countryCustList = sysCountryCustService.listAll();
        List<SysCountryCustDTO> sysCountryCustDTOS = new ArrayList<>();
        for (SysCountryCust sysCountryCust : countryCustList) {
            SysCountryCustDTO sysCountryCustDTO = new SysCountryCustDTO();
            BeanUtils.copyNotNullProperties(sysCountryCust, sysCountryCustDTO);
            sysCountryCustDTOS.add(sysCountryCustDTO);
        }
        return sysCountryCustDTOS;
    }
    /**
     * 根据ID查询国家信息
     *
     * @param id
     * @return Region
     */
    @Override
    public SysCountryCustDTO getById(String id) {
        if(EmptyUtils.isEmpty(id)){
            return null;
        }
        SysCountryCustDTO sysCountryCustDTO = new SysCountryCustDTO();
        SysCountryCust sysCountryCust = sysCountryCustService.get(id, true);
        if (sysCountryCust != null) {
            BeanUtils.copyNotNullProperties(sysCountryCust, sysCountryCustDTO);
        }
        return sysCountryCustDTO;
    }
    /**
     * 根据中文名称获取地点信息
     *
     * @param nameZh
     * @return
     */
    @Override
    public SysCountryCustDTO getByNameZh(String nameZh, String countryLevel) {
        SysCountryCustDTO sysCountryCustDTO = new SysCountryCustDTO();
        SysCountryCustVO sysCountryCustVO = sysCountryCustService.getByNameZh(nameZh, countryLevel);
        BeanUtils.copyNotNullProperties(sysCountryCustVO, sysCountryCustDTO);
        return sysCountryCustDTO;
    }

    @Override
    public List<SysCountryCustDTO> listDtoById(String sysId) {
        List<SysCountryCustDTO> result = Lists.newArrayList();
        List<SysCountryCustVO> sysCountryCustVOS = sysCountryCustService.listVOById(sysId);
        if (null != sysCountryCustVOS && sysCountryCustVOS.size() >0 ){
            result = BeanUtils.copyListProperties(sysCountryCustVOS, SysCountryCustDTO.class);
        }

        return result;
    }


    /**
     * 根据简称查询国家信息
     *
     * @param enCode
     * @return
     */
    @Override
    public SysCountryCustDTO getByEnCode(String enCode) {
        SysCountryCustDTO sysCountryCustDTO = new SysCountryCustDTO();
        SysCountryCustVO sysCountryCustVO = sysCountryCustService.getByEnCode(enCode);
        BeanUtils.copyNotNullProperties(sysCountryCustVO, sysCountryCustDTO);
        return sysCountryCustDTO;
    }

    public SysCountryCustDTO getByNameZhLevelTop(String nameZh) {
        SysCountryCustDTO sysCountryCustDTO = new SysCountryCustDTO();
        SysCountryCustVO sysCountryCustVO = sysCountryCustService.getByNameZhLevelTop(nameZh);
        BeanUtils.copyNotNullProperties(sysCountryCustVO, sysCountryCustDTO);
        return sysCountryCustDTO;
    }
}
