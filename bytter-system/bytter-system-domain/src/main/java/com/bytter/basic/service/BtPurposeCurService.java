package com.bytter.basic.service;
import com.bytter.basic.dao.BtPurposeCurDao;
import com.bytter.basic.entity.BtPurposeCur;
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
@Service("btPurposeCurServiceBase")
@Transactional
public class BtPurposeCurService extends BaseService<BtPurposeCur, String> {
    @Autowired
    private BtPurposeCurDao btPurposeCurDao;
    @Override
    public BaseDao<BtPurposeCur, String> getEntityDao() {
        return btPurposeCurDao;
    }
    /**
     * 根据用途id查找用途币种
     *
     * @param purposeId 用途ID
     * @return
     */
    public List<BtPurposeCur> getByPurposeId(String purposeId) {
        return this.btPurposeCurDao.getByPurposeId(purposeId);
    }
    /**
     * 删除数据
     *
     * @param purposeId
     */
    public void deleteBtPurposeCur(String purposeId) {
        this.btPurposeCurDao.deleteBtPurposeCur(purposeId);
    }
}
