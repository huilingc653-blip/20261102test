package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCotpBankAccCurDao;
import com.bytter.biz.foreign.entity.FexCotpBankAccCur;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: Service
 * @Description: BT_BANK_ACC_CUR
 * @author huangx
 * @date 2024-02-24 18:03:22
 * @version 1.0
 *
 */
@Service("fexCotpBankAccCurService")
@Transactional
public class FexCotpBankAccCurService extends BaseService<FexCotpBankAccCur,String> {
    @Autowired
    private FexCotpBankAccCurDao fexCotpBankAccCurDao;

    @Override
    public BaseDao<FexCotpBankAccCur, String> getEntityDao() {
        return fexCotpBankAccCurDao;
    }

    /**
     * 根据账号ID删除币种信息
     * @param bankAccId
     */
    public int deleteByBankAccId(String bankAccId){
        return fexCotpBankAccCurDao.deleteByBankAccId(bankAccId);
    }

    /**
     * 根据账号ID获取币种集合
     * @param bankAccId
     * @return
     */
    public List<FexCotpBankAccCur> listByBankAccId(String bankAccId) {
        return fexCotpBankAccCurDao.listBy("bankAccId", bankAccId);
    }
}
