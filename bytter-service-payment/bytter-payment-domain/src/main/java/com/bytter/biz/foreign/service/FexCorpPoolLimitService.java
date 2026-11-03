package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCorpPoolLimitDao;
import com.bytter.biz.foreign.entity.FexCorpPoolLimit;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Service
 * @Description: 外汇资金池限额设置表
 * @date 2020-02-12 14:31:56
 */
@Service("fexCorpPoolLimitService")
@Transactional
public class FexCorpPoolLimitService extends BaseService<FexCorpPoolLimit, String> {

    @Autowired
    private FexCorpPoolLimitDao fexCorpPoolLimitDao;

    @Override
    public BaseDao<FexCorpPoolLimit, String> getEntityDao() {
        return fexCorpPoolLimitDao;
    }

    /**
     * 删除对象
     * @param entity
     */
    @Override
    public void deleteEntity(FexCorpPoolLimit entity){
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     * @param id
     */
    @Override
    public void delete(String id){
        super.delete(id);
    }
    /**
     * 保存
     * @param entity
     */
    public void save(FexCorpPoolLimit entity){
        super.saveEntity(entity);
    }
    /**
     * 更新
     * @param entity
     */
    public void update(FexCorpPoolLimit entity){
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * @param entity
     */
    @Override
    public void saveOrUpdate(FexCorpPoolLimit entity){
        super.saveOrUpdate(entity);
    }

    /**
     * 根据(或者资金池账号表id)查找外汇资金池限额设置
     *
     * @param relatedId
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<FexCorpPoolLimit> listFexCorpPoolLimitByrelatedId(String relatedId) {
        return fexCorpPoolLimitDao.listLimitByIdType(relatedId);
    }

}
