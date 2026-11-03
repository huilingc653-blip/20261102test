package com.bytter.system.service.impl;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccUnFreezeService;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;
import com.bytter.system.dao.BtBankAccUnFreezeDao;
import com.bytter.system.entity.BtBankAccFreeze;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service("BtBankAccUnFreezeServiceI")
@DubboService
@Transactional
public class BtBankAccUnFreezeServiceImpl extends BaseService<BtBankAccFreeze,String> implements IBtBankAccUnFreezeService {
    @Autowired
    private BtBankAccUnFreezeDao btBankAccUnFreezeDao;

    @Override
    public BaseDao<BtBankAccFreeze, String> getEntityDao() {
        return btBankAccUnFreezeDao;
    }

    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        return btBankAccUnFreezeDao.getDataTablesUnion(dataTablesInput, joinSql);
    }

    @Override
    public List<BtBankAccFreezeDTO> getBtBankAccFreeze(String accId, String curId) {
        return btBankAccUnFreezeDao.getBtBankAccFreeze(accId, curId);
    }

    @Override
    public List<BtBankAccFreezeDTO> getBtBankAccFreezeTemp(String accId, String curId) {
        return btBankAccUnFreezeDao.getBtBankAccFreezeTemp(accId, curId);
    }

    @Override
    public List<BtBankAccFreezeDTO> getBtBankAccUnFreeze(String accId, String curId) {
        return btBankAccUnFreezeDao.getBtBankAccUnFreeze(accId, curId);
    }

    @Override
    public BigDecimal getFreezeMoney(String accId, String curId) {
        return btBankAccUnFreezeDao.getFreezeMoney(accId, curId);
    }

    @Override
    public BigDecimal getUfFreezeMoney(String accId, String curId) {
        return btBankAccUnFreezeDao.getUfFreezeMoney(accId, curId);
    }

    @Override
    public BigDecimal getUfFreezeMoney(String accId, String curId, String uufid) {
        return btBankAccUnFreezeDao.getUfFreezeMoney(accId, curId, uufid);
    }

    @Override
    public BigDecimal getUfWaitFreezeMoney(String accId, String curId, String uufid) {
        return btBankAccUnFreezeDao.getUfWaitFreezeMoney(accId, curId, uufid);
    }

    @Override
    public BigDecimal getFreezeMoney(String id) {
        return btBankAccUnFreezeDao.getFreezeMoney(id);
    }

    @Override
    public BigDecimal getUfFreezeMoneyForTemp(String accId, String curId, String uufid) {
        return btBankAccUnFreezeDao.getUfFreezeMoneyForTemp(accId, curId, uufid);
    }

    @Override
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        return btBankAccUnFreezeDao.dataTableApprove(dataTablesInput, businessSql);
    }

    @Override
    public BtBankAccFreezeDTO getObject(String id) {
        BtBankAccFreeze btBankAccFreeze = this.get(id ,true);
        if(EmptyUtils.isEmpty(btBankAccFreeze)){
            return null;
        }
        BtBankAccFreezeDTO btBankAccFreezeDTO = new BtBankAccFreezeDTO();
        BeanUtils.copyNotNullProperties(btBankAccFreeze, btBankAccFreezeDTO);
        return btBankAccFreezeDTO;
    }

    @Override
    public void saveOrUpdate(BtBankAccFreezeDTO btBankAccFreezeDTO) {
        if(!EmptyUtils.isEmpty(btBankAccFreezeDTO)){
            BtBankAccFreeze btBankAccFreeze = new BtBankAccFreeze();
            BeanUtils.copyNotNullProperties(btBankAccFreezeDTO, btBankAccFreeze);
            super.saveOrUpdate(btBankAccFreeze);
        }
    }

    @Override
    public void save(BtBankAccFreezeDTO btBankAccFreezeDTO) {
        if(!EmptyUtils.isEmpty(btBankAccFreezeDTO)){
            BtBankAccFreeze btBankAccFreeze = new BtBankAccFreeze();
            BeanUtils.copyNotNullProperties(btBankAccFreezeDTO, btBankAccFreeze);
            super.saveEntity(btBankAccFreeze);
        }
    }

    @Override
    public void deleteEntity(BtBankAccFreezeDTO btBankAccFreezeDTO) {
        if(!EmptyUtils.isEmpty(btBankAccFreezeDTO)){
            BtBankAccFreeze btBankAccFreeze = new BtBankAccFreeze();
            BeanUtils.copyNotNullProperties(btBankAccFreezeDTO, btBankAccFreeze);
            super.deleteEntity(btBankAccFreeze);
        }
    }
}
