package com.bytter.system.service.impl;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.IBtBankAccFreezeService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;
import com.bytter.system.dao.BtBankAccFreezeDao;
import com.bytter.system.entity.BtBankAccFreeze;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("BtBankAccFreezeServiceI")
@DubboService
@Transactional
public class BtBankAccFreezeServiceImpl extends BaseService<BtBankAccFreeze,String> implements IBtBankAccFreezeService {
    @Autowired
    private BtBankAccFreezeDao btBankAccFreezeDao;

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        return btBankAccFreezeDao.getDataTablesUnion(dataTablesInput,joinSql);
    }

    @Override
    public BigDecimal getFreezeMoneyByBankAcc(String bankAcc, String curId, Date balDate) {
        //冻结金额
        BigDecimal freezeMoney = btBankAccFreezeDao.getFreezeMoneyByBankAcc(bankAcc, curId, BtBankAccConstant.UFFLAG_0.toString(), balDate);
        //解冻金额
        BigDecimal unFreezeMoney = btBankAccFreezeDao.getFreezeMoneyByBankAcc(bankAcc, curId, BtBankAccConstant.UFFLAG_1.toString(), balDate);
        return freezeMoney.subtract(unFreezeMoney);
    }

    @Override
    public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, String joinSql) {
        return btBankAccFreezeDao.getDataTablesQuery(dataTablesInput,joinSql);
    }

    @Override
    public List<BtBankAccFreezeDTO> getBtBankAccFreeze(String accId, String curId) {
        return btBankAccFreezeDao.getBtBankAccFreeze(accId,curId);
    }

    @Override
    public List<BtBankAccFreezeDTO> getBtBankAccFreezeTemp(String accId, String curId) {
        return btBankAccFreezeDao.getBtBankAccFreezeTemp(accId,curId);
    }

    @Override
    public BigDecimal getFreezeMoney(String accId, String curId) {
        return btBankAccFreezeDao.getFreezeMoney(accId,curId);
    }

    @Override
    public BigDecimal getUfFreezeMoney(String accId, String curId) {
        return btBankAccFreezeDao.getUfFreezeMoney(accId,curId);
    }

    @Override
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        return btBankAccFreezeDao.dataTableApprove(dataTablesInput, businessSql);
    }

    @Override
    public List<BtBankAccFreezeDTO> getBtBankAccFreezeWait(String accId, String curId) {
        return btBankAccFreezeDao.getBtBankAccFreezeWait(accId,curId);
    }

    @Override
    public int getBankAccFreezeCountByCorpId(String corpId) {
        return btBankAccFreezeDao.getBankAccFreezeCountByCorpId(corpId);
    }

    @Override
    public BigDecimal getFreezeMoneyByBankAcc(String bankAcc, String curId, String ufFlag, Date balDate) {
        return btBankAccFreezeDao.getFreezeMoneyByBankAcc(bankAcc,curId,ufFlag,balDate);
    }

    @Override
    public void deleteEntity(BtBankAccFreezeDTO dto) {
        BtBankAccFreeze entity = new BtBankAccFreeze();
        BeanUtils.copyNotNullProperties(dto, entity);
        super.deleteEntity(entity);
    }

    @Override
    public void save(BtBankAccFreezeDTO dto) {
        BtBankAccFreeze entity = new BtBankAccFreeze();
        BeanUtils.copyNotNullProperties(dto, entity);
        super.saveEntity(entity);
    }

    @Override
    public void update(BtBankAccFreezeDTO dto) {
        BtBankAccFreeze entity = new BtBankAccFreeze();
        BeanUtils.copyNotNullProperties(dto, entity);
        super.updateEntity(entity);
    }

    @Override
    public void saveOrUpdate(BtBankAccFreezeDTO dto) {
        BtBankAccFreeze entity = new BtBankAccFreeze();
        BeanUtils.copyNotNullProperties(dto, entity);
        super.saveOrUpdate(entity);
    }

    /**
     * 根据id和币别得到银行账号
     *
     * @param id
     * @param curId
     * @return
     */
    public BtBankAccDTO getAccVOById(String id, String curId) {
		return btBankAccFreezeDao.getAccVOById(id,curId);
    }

    @Override
    public BaseDao<BtBankAccFreeze, String> getEntityDao() {
        return btBankAccFreezeDao;
    }

    @Override
    public BtBankAccFreezeDTO getBtBankAccFreezeDTO(String id) {
        BtBankAccFreeze entity = super.get(id);
        BtBankAccFreezeDTO dto = new BtBankAccFreezeDTO();
        BeanUtils.copyNotNullProperties(entity, dto);
        return dto;
    }

    @Override
    public BtBankAccFreezeDTO getBtBankAccFreezeDTO(String id, boolean eager) {
        BtBankAccFreeze entity = super.get(id,eager);
        BtBankAccFreezeDTO dto = new BtBankAccFreezeDTO();
        BeanUtils.copyNotNullProperties(entity, dto);
        return dto;
    }

    @Override
    public BtBankAccFreezeDTO getBtBankAccFreezeByFreezeNo(String freezeNo) {
        BtBankAccFreeze entity = btBankAccFreezeDao.getBtBankAccFreezeByFreezeNo(freezeNo);
        BtBankAccFreezeDTO dto = new BtBankAccFreezeDTO();
        BeanUtils.copyNotNullProperties(entity, dto);
        return dto;
    }

    @Override
    public BigDecimal getUnFreezedMoneyById(String freezeId) {
        //根据id查询解冻的记录
        BigDecimal unFreezedMoney= btBankAccFreezeDao.getUnFreezedMoneyById(freezeId);
        return unFreezedMoney;
    }
}
