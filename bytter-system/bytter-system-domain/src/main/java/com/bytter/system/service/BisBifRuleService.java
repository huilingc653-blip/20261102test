package com.bytter.system.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.dao.BisBifRuleDao;
import com.bytter.system.entity.BisBifRule;
import com.bytter.system.vo.BisBifRuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 接口代码业务规则表
 * @date 2017-05-17 10:16:20
 */
@Service("bisBifRuleService")
@Transactional
public class BisBifRuleService extends BaseService<BisBifRule, String> {
    @Autowired
    private BisBifRuleDao bisBifRuleDao;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(BisBifRule entity) {
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
    public void save(BisBifRule entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BisBifRule entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(BisBifRule entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<BisBifRule, String> getEntityDao() {
        return bisBifRuleDao;
    }
    /**
     * 根据编码获取接口代码业务规则
     *
     * @param bifCode
     * @return
     */
    public BisBifRule findByBifCode(String bifCode) {
        return bisBifRuleDao.findByBifCode(bifCode);
    }
    /**
     * 根据接口代码ID获取接口代码业务规则
     *
     * @param bifId
     * @return
     */
    public BisBifRuleVO findByBifId(String bifId) {
        return bisBifRuleDao.findByBifId(bifId);
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return bisBifRuleDao.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 根据ID获取接口规则
     *
     * @param id
     * @return
     */
    public BisBifRuleVO getByIdUnion(String id) {
        return bisBifRuleDao.getByIdUnion(id);
    }
    /**
     * 获取所有接口代码业务规则信息
     *
     * @return
     */
    public List<BisBifRuleVO> listAllUnion() {
        return bisBifRuleDao.listAllUnion();
    }
	/**
	 * 根据ID获取接口规则
	 *
	 * @param id
	 * @return
	 */
	public BisBifRuleVO getById(String id) {
		return bisBifRuleDao.getById(id);
	}
}
