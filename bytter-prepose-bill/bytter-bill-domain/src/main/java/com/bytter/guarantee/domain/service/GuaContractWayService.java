package com.bytter.guarantee.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.guarantee.domain.dao.GuaContractWayDao;
import com.bytter.guarantee.domain.entity.GuaContractWay;
import com.bytter.guarantee.domain.vo.GuaContractWayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaContractWayService
 * @Description 担保合同关联担保方式service
 * @date 2018年12月11日
 */
@Service("guaContractWayService")
@Transactional
public class GuaContractWayService extends BaseService<GuaContractWay, String> {
    @Autowired
    GuaContractWayDao guaContractWayDao;
    /**
     * 同担保合同id查询担保合同关联担保方式信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<GuaContractWayVO> getGuaContractWay(String guaId) {
        return guaContractWayDao.getGuaContractWay(guaId);
    }
    /**
     * 根据id得到担保合同关联担保方式信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public GuaContractWay getGuaContractWayById(String id) {
        return guaContractWayDao.getGuaContractWayById(id);
    }
    /**
     * 通过担保合同id删除相应的担保合同关联担保方式信息
     *
     * @param id
     * @return
     */
    public int delGuaContractWay(String guaId) {
        return guaContractWayDao.delGuaContractWay(guaId);
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(GuaContractWay entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(GuaContractWay entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(GuaContractWay entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(GuaContractWay entity) {
        super.saveOrUpdate(entity);
        this.getSession().flush();
    }
    @Override
    public BaseDao<GuaContractWay, String> getEntityDao() {
        return guaContractWayDao;
    }
    /**
     * 同担保合同id查询担保合同关联担保方式信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<GuaContractWayVO> getGuaContractWayVO(String guaId) {
        return guaContractWayDao.getGuaContractWayVO(guaId);
    }
    /**
     * 根据info表id获取担保方式
     *
     * @param ids 担保合同主表id集合
     * @return 担保方式集合
     */
    public List<GuaContractWayVO> getListByInfoIds(List<String> ids) {
        return guaContractWayDao.getListByInfoIds(ids);
    }
}
