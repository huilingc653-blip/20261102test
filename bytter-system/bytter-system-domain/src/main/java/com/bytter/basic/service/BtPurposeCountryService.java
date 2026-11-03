package com.bytter.basic.service;
import com.bytter.basic.dao.BtPurposeCountryDao;
import com.bytter.basic.entity.BtPurposeCountry;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 用途维护表
 * @date 2022-11-23 09:57:15
 */
@Service("btPurposeCountryServiceBase")
@Transactional
public class BtPurposeCountryService extends BaseService<BtPurposeCountry, String> {
    @Autowired
    private BtPurposeCountryDao btPurposeCountryDao;
    @Override
    public BaseDao<BtPurposeCountry, String> getEntityDao() {
        return btPurposeCountryDao;
    }
    /**
     * 根据用途id查找用途币种
     *
     * @param purposeId 用途ID
     * @return
     */
    public List<BtPurposeCountry> getByPurposeId(String purposeId) {
        return this.btPurposeCountryDao.getByPurposeId(purposeId);
    }
    /**
     * 删除数据
     *
     * @param purposeId
     */
    public void deleteBtPurposeCountry(String purposeId) {
        this.btPurposeCountryDao.deleteBtPurposeCountry(purposeId);
    }
}
