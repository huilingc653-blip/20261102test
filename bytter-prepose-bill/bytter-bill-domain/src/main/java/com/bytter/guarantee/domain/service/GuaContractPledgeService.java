package com.bytter.guarantee.domain.service;
import java.util.List;
import com.bytter.guarantee.domain.vo.GuaContractPledgeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.guarantee.domain.dao.GuaContractPledgeDao;
import com.bytter.guarantee.domain.entity.GuaContractPledge;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaContractPledgeService
 * @Description 担保合同关联抵质押物service
 * @date 2018年12月11日
 */
@Service("guaContractPledgeService")
@Transactional
public class GuaContractPledgeService extends BaseService<GuaContractPledge, String> {
    @Autowired
    GuaContractPledgeDao guaContractPledgeDao;
    /**
     * 通过担保合同id查询担保合同关联抵质押物信息表
     * 
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<GuaContractPledge> getGuaContractPledge(String guaId) {
        return guaContractPledgeDao.getGuaContractPledge(guaId);
    }
    /**
     * 通过抵质押物id查询担保合同关联抵质押物的信息
     * 
     * @param pledgeId
     * @return
     */
    @Transactional(readOnly = true)
    public List<GuaContractPledge> getGuaContractPledgeByPledgeId(String pledgeId) {
        return guaContractPledgeDao.getGuaContractPledgeByPledgeId(pledgeId);
    }
    /**
     * 通过担保合同关联担保方式id删除相应的担保合同关联抵质押物信息表
     * 
     * @param id
     * @return
     */
    public int delGuaContractPledge(String id) {
        int n = guaContractPledgeDao.delGuaContractPledge(id);
        this.getSession().flush();
        return n;
    }
    /**
     * 删除对象
     * 
     * @param entity
     */
    public void deleteEntity(GuaContractPledge entity) {
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
    public void save(GuaContractPledge entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     * 
     * @param entity
     */
    public void update(GuaContractPledge entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * 
     * @param entity
     */
    public void saveOrUpdate(GuaContractPledge entity) {
        super.saveOrUpdate(entity);
        this.getSession().flush();
    }
    @Override
    public BaseDao<GuaContractPledge, String> getEntityDao() {
        return guaContractPledgeDao;
    }
    /**
     * 通过担保合同关联的担保方式id查询抵质押物信息表
     * 
     * @param guaWayId
     *            担保合同关联的担保方式id
     * @return
     */
    @Transactional(readOnly = true)
    public List<GuaContractPledge> queryGuaContractPledge(String guaWayId) {
        return guaContractPledgeDao.queryGuaContractPledge(guaWayId);
    }
    /**
     * 通过担保合同id查询担保合同关联抵质押物信息表
     * 
     * @return
     */
    @Transactional(readOnly = true)
    public List<GuaContractPledgeVO> getGuaContractPledgeList(String guaId) {
        return guaContractPledgeDao.getGuaContractPledgeList(guaId);
    }
}
