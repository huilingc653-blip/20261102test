package com.bytter.basic.service;
import com.bytter.basic.dao.BtPurposeBankTypeDao;
import com.bytter.basic.entity.BtPurposeBankType;
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
@Service("btPurposeBankTypeServiceBase")
@Transactional
public class BtPurposeBankTypeService extends BaseService<BtPurposeBankType, String> {
    @Autowired
    private BtPurposeBankTypeDao btPurposeBankTypeDao;
    @Override
    public BaseDao<BtPurposeBankType, String> getEntityDao() {
        return btPurposeBankTypeDao;
    }
    /**
     * 根据用途id查找用途银行类别
     *
     * @param purposeId 用途ID
     * @return
     */
    public List<BtPurposeBankType> getByPurposeId(String purposeId) {
        return this.btPurposeBankTypeDao.getByPurposeId(purposeId);
    }
    /**
     * 删除数据
     *
     * @param purposeId
     */
    public void deleteBtPurposeBankType(String purposeId) {
        this.btPurposeBankTypeDao.deleteBtPurposeBankType(purposeId);
    }
}
