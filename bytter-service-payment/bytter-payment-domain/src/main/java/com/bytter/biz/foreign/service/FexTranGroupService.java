package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexTranGroupDao;
import com.bytter.biz.foreign.entity.FexTranGroup;
import com.bytter.biz.foreign.vo.FexTranGroupVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: Service
 * @Description: 交易组
 * @author huangx
 * @date 2020-02-27 15:20:33
 * @version 1.0
 *
 */
@Service("fexTranGroupService")
@Transactional
public class FexTranGroupService extends BaseService<FexTranGroup, String> {
    @Autowired
    private FexTranGroupDao fexTranGroupDao;

    @Override
    public BaseDao<FexTranGroup, String> getEntityDao() {
        return fexTranGroupDao;
    }

    /**
     * 查询交易组
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        return fexTranGroupDao.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 根据ID获取交易组信息
     *
     * @param id
     * @return
     */
    public FexTranGroupVO getByIdUnion(String id) {
        return fexTranGroupDao.getByIdUnion(id);
    }

    /**
     * 保存交易组信息
     *
     * @param fexTranGroup
     */
    public void saveFexTranGroup(FexTranGroupVO fexTranGroup, ICurrentUser user) {
        FexTranGroup entity = new FexTranGroup();
        BeanUtils.copyNotNullProperties(fexTranGroup, entity);
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_GROUP));
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        this.saveEntity(entity);
    }

    /**
     * 修改交易组信息
     *
     * @param fexTranGroup
     */
    public void updateFexTranGroup(FexTranGroupVO fexTranGroup, ICurrentUser user) {
        FexTranGroup entity = this.get(fexTranGroup.getId());
        BeanUtils.copyNotNullProperties(fexTranGroup, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
    }

    /**
     * 删除交易组信息
     *
     * @param id
     */
    public void delFexTranGroup(String id, ICurrentUser user) {
        this.delete(id);
    }

    /**
     * 判断是否存在产品名称
     * @param groupName
     * @param id
     * @return
     */
    public boolean isExistGroupName(String groupName,String id){
        List<FexTranGroupVO> list = fexTranGroupDao.listByGroupName(groupName, id);
        if(EmptyUtils.isEmptyList(list)){
            return false;
        }
        return true;
    }

    /**
     * 获取有效的交易组
     * @return
     */
    public List<FexTranGroupVO> listTranGroup(String tranType, String proId){
        return fexTranGroupDao.listTranGroup(tranType, proId);
    }
}
