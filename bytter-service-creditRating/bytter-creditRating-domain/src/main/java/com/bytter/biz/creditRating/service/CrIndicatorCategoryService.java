package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.dao.CrIndicatorCategoryDao;
import com.bytter.biz.creditRating.entity.CrIndicatorCategory;
import com.bytter.biz.creditRating.vo.CrIndicatorCategoryVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 指标类别((已弃用))
 */
@Service("crIndicatorCategoryService")
@Transactional
public class CrIndicatorCategoryService extends BaseService<CrIndicatorCategory, String> {

    @Autowired
    private CrIndicatorCategoryDao crIndicatorCategoryDao;

    @Override
    public BaseDao<CrIndicatorCategory, String> getEntityDao() {
        return crIndicatorCategoryDao;
    }


    /**
     * 查询指标类别列表数据
     * @param input
     * @return
     */
    public DataTablesOutput getDataTable(DataTablesInput input){
        return crIndicatorCategoryDao.getDataTable(input);
    }

    /**
     * 根据指标类别id查询指标类别
     * @param id
     * @return
     */
    public CrIndicatorCategoryVo getById(String id){
        return crIndicatorCategoryDao.getById(id);
    }

    /**
     * 查询所有有校的指标类别
     * @return
     */
    public List<CrIndicatorCategoryVo> getEffectData(){
        return crIndicatorCategoryDao.getEffectData();
    }

    /**
     * 保存指标类别
     * @param crIndicatorCategoryVo
     */
    public void save(CrIndicatorCategoryVo crIndicatorCategoryVo, String userId){
        checkData(crIndicatorCategoryVo);
        CrIndicatorCategory crIndicatorCategory = new CrIndicatorCategory();
        BeanUtils.copyNotNullProperties(crIndicatorCategoryVo, crIndicatorCategory);
        crIndicatorCategory.setDeleteFlag(CreditRatingConstant.INIT_DELETE_FLAG);
        crIndicatorCategory.setCreateDate(DateUtils.getDate());
        crIndicatorCategory.setCreator(userId);
        crIndicatorCategoryDao.save(crIndicatorCategory);
    }

    /**
     * 校验指标类别的数据合规性
     * @param crIndicatorCategoryVo
     */
    private void checkData(CrIndicatorCategoryVo crIndicatorCategoryVo){

    }

    /**
     * 更新指标类别
     * @param crIndicatorCategoryVo
     */
    public void update(CrIndicatorCategoryVo crIndicatorCategoryVo){
        checkData(crIndicatorCategoryVo);
        CrIndicatorCategoryVo byId = crIndicatorCategoryDao.getById(crIndicatorCategoryVo.getId());
        BeanUtils.copyNotNullProperties(crIndicatorCategoryVo,byId);
        CrIndicatorCategory crIndicatorCategory = new CrIndicatorCategory();
        BeanUtils.copyNotNullProperties(byId, crIndicatorCategory);
        crIndicatorCategoryDao.update(crIndicatorCategory);
    }

    /**
     * 删除指标类别
     * @param id
     */
    public void delete(String id){
        CrIndicatorCategoryVo crIndicatorCategoryVo = crIndicatorCategoryDao.getById(id);
        CrIndicatorCategory crIndicatorCategory = new CrIndicatorCategory();
        BeanUtils.copyNotNullProperties(crIndicatorCategoryVo, crIndicatorCategory);
        crIndicatorCategory.setDeleteFlag(CreditRatingConstant.DELETE_STATUS);
        crIndicatorCategory.setStatus(CreditRatingConstant.DELETE_STATUS);
        crIndicatorCategoryDao.update(crIndicatorCategory);
    }
}
