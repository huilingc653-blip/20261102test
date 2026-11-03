package com.bytter.basic.service;
import com.bytter.basic.dao.SysCountryCustDao;
import com.bytter.basic.entity.SysCountryCust;
import com.bytter.basic.vo.SysCountryCustVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 国家地区表
 * @date 2020-1-19 11:14:22
 */
@Service("sysCountryCustServiceBase")
@Transactional
public class SysCountryCustService extends BaseService<SysCountryCust, String> {
    @Autowired
    private SysCountryCustDao sysCountryCustDao;
    @Override
    public BaseDao<SysCountryCust, String> getEntityDao() {
        return sysCountryCustDao;
    }
    /**
     * 查询国家地区表列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        return this.sysCountryCustDao.getDataTablesUnion(input);
    }
    /**
     * 根据ID查找国家地区表
     *
     * @param id
     * @return
     */
    public SysCountryCustVO getByUnion(String id) {
        return this.sysCountryCustDao.getByUnion(id);
    }
    /**
     * 根据中文名称获取地点信息
     *
     * @param nameZh
     * @return
     */
    public SysCountryCustVO getByNameZh(String nameZh) {
        List<SysCountryCust> list = this.sysCountryCustDao.listBy("nameZh", nameZh);
        if (!EmptyUtils.isEmpty(list)) {
            SysCountryCustVO sysCountryCustVO = new SysCountryCustVO();
            BeanUtils.copyNotNullProperties(list.get(0), sysCountryCustVO);
            return sysCountryCustVO;
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
    public SysCountryCustVO getByNameZhAndSysId(String nameZh, String sysId) {
        List<SysCountryCustVO> list = this.sysCountryCustDao.listByNameZhAndSysId(nameZh, sysId);
        if (!EmptyUtils.isEmpty(list)) {
            return list.get(0);
        }
        return new SysCountryCustVO();
    }
    /**
     * 保存国家地区表
     *
     * @param sysCountryCustVO
     */
    public void saveSysCountryCust(SysCountryCustVO sysCountryCustVO, List<String> nameZhs, List<String> nameEns,
                                   List<String> codes, List<String> timeZones, String userLoginName) {
        SysCountryCust entity = new SysCountryCust();
        BeanUtils.copyNotNullProperties(sysCountryCustVO, entity);
        entity.setCreateBy(userLoginName);
        entity.setCreateDate(DateUtils.getDate());
        this.saveEntity(entity);
        if (!EmptyUtils.isEmpty(nameZhs)) {
            for (int i = 0; i < nameZhs.size(); i++) {
                if (StringUtils.isNotBlank(nameZhs.get(i))) {
                    SysCountryCust country = new SysCountryCust();
                    country.setSysId(entity.getId());
                    country.setCountryLevel(CommonConstant.YES);
                    country.setNameZh(nameZhs.get(i));
                    country.setNameEn(nameEns.get(i));
                    country.setCode(codes.get(i));
                    country.setTimeZoneId(timeZones.get(i));
                    country.setValidSign(CommonConstant.YES);
                    country.setCreateBy(userLoginName);
                    country.setCreateDate(DateUtils.getDate());
                    this.saveEntity(country);
                }
            }
        }
    }
    /**
     * 修改国家地区表
     *
     * @param sysCountryCustVO
     */
    public void updateSysCountryCust(SysCountryCustVO sysCountryCustVO, List<String> ids, List<String> nameZhs, List<String> nameEns,
                                     List<String> codes, List<String> timeZones, String userLoginName) {
        SysCountryCust entity = this.get(sysCountryCustVO.getId());
        BeanUtils.copyNotNullProperties(sysCountryCustVO, entity);
        entity.setUpdateBy(userLoginName);
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        if (!EmptyUtils.isEmpty(nameZhs)) {
            for (int i = 0; i < nameZhs.size(); i++) {
                if (StringUtils.isNotBlank(nameZhs.get(i))) {
                    SysCountryCust country = new SysCountryCust();
                    if (!EmptyUtils.isEmpty(ids) && StringUtils.isNotBlank(ids.get(i))) {
                        country.setId(ids.get(i));
                    }
                    country.setSysId(entity.getId());
                    country.setCountryLevel(CommonConstant.YES);
                    country.setNameZh(nameZhs.get(i));
                    country.setNameEn(nameEns.get(i));
                    country.setCode(codes.get(i));
                    country.setTimeZoneId(timeZones.get(i));
                    country.setValidSign(CommonConstant.YES);
                    country.setCreateBy(userLoginName);
                    country.setCreateDate(DateUtils.getDate());
                    this.saveOrUpdate(country);
                }
            }
        }
    }
    /**
     * 根据SYS_ID查找国家地区表
     *
     * @param sysId
     * @return
     */
    public List<SysCountryCustVO> listVOBySysId(String sysId) {
        return sysCountryCustDao.listBySysId(sysId);
    }
    /**
     * 根据COUNTRY_LEVEL查找国家地区表
     *
     * @param countryLevel
     * @return
     */
    public List<SysCountryCustVO> listByCountryLevel(String countryLevel) {
        return sysCountryCustDao.listByCountryLevel(countryLevel);
    }
    /**
     * 根据SYS_ID查找国家地区表
     *
     * @param sysId
     * @return
     */
    public List<SysCountryCust> listBySysId(String sysId) {
        return sysCountryCustDao.listBy("sysId", sysId);
    }
    /**
     * 根据中文名称获取地点信息
     *
     * @param nameZh
     * @return
     */
    public SysCountryCustVO getByNameZh(String nameZh, String countryLevel) {
        List<SysCountryCustVO> list = this.sysCountryCustDao.getByNameZh(nameZh, countryLevel);
        SysCountryCustVO sysCountryCustVO = new SysCountryCustVO();
        if (!EmptyUtils.isEmpty(list)) {
            sysCountryCustVO = list.get(0);
        }
        return sysCountryCustVO;
    }

    /**
     * 根据ID查找国家地区表
     *
     * @param id
     * @return
     */
    public List<SysCountryCustVO> listVOById(String id) {
        return sysCountryCustDao.listById(id);
    }


    /**
     * 根据简称查询国家信息
     *
     * @param enCode
     * @return
     */
    public SysCountryCustVO getByEnCode(String enCode) {
        List<SysCountryCust> list = this.sysCountryCustDao.listBy("enCode", enCode);
        SysCountryCustVO sysCountryCustVO = new SysCountryCustVO();
        if(!EmptyUtils.isEmpty(list)){
            BeanUtils.copyNotNullProperties(list.get(0), sysCountryCustVO);
        }
        return sysCountryCustVO;
    }

    public SysCountryCustVO getByNameZhLevelTop(String nameZh){
        List<SysCountryCust> list = this.sysCountryCustDao.listBy("nameZh", nameZh).stream().filter(e-> "0".equals(e.getCountryLevel())).collect(Collectors.toList());
        SysCountryCustVO sysCountryCustVO = new SysCountryCustVO();
        if(!EmptyUtils.isEmpty(list)){
            BeanUtils.copyNotNullProperties(list.get(0) , sysCountryCustVO);
        }
        return sysCountryCustVO;
    }
}
