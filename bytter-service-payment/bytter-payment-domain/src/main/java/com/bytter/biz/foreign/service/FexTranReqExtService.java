package com.bytter.biz.foreign.service;


import com.bytter.biz.foreign.dao.FexTranReqExtDao;
import com.bytter.biz.foreign.entity.FexTranReqExt;
import com.bytter.biz.foreign.vo.FexTranReqExtVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service("fexTranReqExtService")
@Transactional
public class FexTranReqExtService extends BaseService<FexTranReqExt, String> {

    @Autowired
    private FexTranReqExtDao fexTranReqExtDao;

    @Override
    public BaseDao<FexTranReqExt, String> getEntityDao() {
        return fexTranReqExtDao;
    }


    /**
     *
     * 根据交易ID删除扩展信息
     *
     * @param reqId
     */
    public void delByReqId(String reqId){
        fexTranReqExtDao.delByReqId(reqId);
    }

    /**
     *
     * 根据交易ID查询扩展信息
     *
     * @param reqId
     * @return
     */
    public FexTranReqExtVo getByReqId(String reqId){
        return fexTranReqExtDao.getByReqId(reqId);
    }
    /**
     *
     * 根据交易ID查询扩展信息
     *
     * @param reqIdList
     * @return
     */
    public List<FexTranReqExtVo> listByReqIds(List<String> reqIdList){
        return fexTranReqExtDao.listByReqIds(reqIdList);
    }

    /**
     * 折本位币汇率变更
     * @param id 交易id
     * @param convertFloatMoney 折本位币汇率
     */
    public void modifyConvertRate(String id, BigDecimal convertRate){
        fexTranReqExtDao.modifyConvertRate(id, convertRate);
    }

    /**
     * 损益币种变更
     * @param id 交易id
     * @param profitLossCur 损益币种
     */
    public void modifyProfitLossCur(String id, String profitLossCur){
        fexTranReqExtDao.modifyProfitLossCur(id, profitLossCur);
    }
}
