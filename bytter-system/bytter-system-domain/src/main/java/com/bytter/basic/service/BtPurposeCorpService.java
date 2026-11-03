package com.bytter.basic.service;
import com.bytter.basic.dao.BtPurposeCorpDao;
import com.bytter.basic.entity.BtPurposeCorp;
import com.bytter.basic.vo.BtPurposeCorpVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 用途维护表
 * @date 2017-05-24 09:57:15
 */
@Service("btPurposeCorpServiceBase")
@Transactional
public class BtPurposeCorpService extends BaseService<BtPurposeCorp, String> {
    @Autowired
    private BtPurposeCorpDao btPurposeCorpDao;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(BtPurposeCorp entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(BtPurposeCorp entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BtPurposeCorp entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(BtPurposeCorp entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<BtPurposeCorp, String> getEntityDao() {
        return btPurposeCorpDao;
    }
    /**
     * 根据用途id查找用途单位
     *
     * @param purposeId 用途ID
     * @return
     */
    public List<BtPurposeCorp> getByPurposeId(String purposeId) {
        return this.btPurposeCorpDao.getByPurposeId(purposeId);
    }
    /**
     * 删除数据
     *
     * @param purposeId
     */
    public void deleteBtPurposeCorp(String purposeId) {
        this.btPurposeCorpDao.deleteBtPurposeCorp(purposeId);
    }
    public List<BtPurposeCorpVO> getCorpByPurposeId(String purposeId) {
        return this.btPurposeCorpDao.getCorpByPurposeId(purposeId);
    }
}
