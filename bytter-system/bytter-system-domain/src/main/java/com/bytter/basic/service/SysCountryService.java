package com.bytter.basic.service;
import com.bytter.basic.dao.SysCountryDao;
import com.bytter.basic.entity.SysCountry;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * 国家信息业务层
 *
 * @author qinl
 * @date 2019-07-25
 */
@Service("sysCountryServiceBase")
@Transactional
public class SysCountryService extends BaseService<SysCountry, String> {
    /**
     * 国家信息数据层
     */
    @Autowired
    private SysCountryDao sysCountryDao;
    @Override
    public BaseDao<SysCountry, String> getEntityDao() {
        return sysCountryDao;
    }
    /**
     * <p>
     * 获取列表数据
     * </p>
     *
     * @return DataTablesOutput（Entity）
     */
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput) {
        return sysCountryDao.selectDataTable(dataTablesInput);
    }
    /**
     * <p>
     * 获取国家信息集合
     * </p>
     *
     * @return 集合(Entity)
     */
    public List<SysCountry> getList() {
        return sysCountryDao.selectList();
    }
    /**
     * <p>
     * 获取一条国家信息
     * </p>
     *
     * @param sysCountry [条件集合]
     * @return (Entity)
     */
    public SysCountry getOne(SysCountry sysCountry) {
        if (sysCountry == null) {
            return null;
        }
        return sysCountryDao.selectOne(sysCountry);
    }
    /**
     * <p>
     * 获取一条国家信息
     * </p>
     *
     * @param code [编号]
     * @return (Entity)
     */
    public SysCountry getByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        return sysCountryDao.selectByCode(code);
    }
    public List<SysCountry> listByContinent(String continent) {
        return sysCountryDao.listByContinent(continent);
    }
}
