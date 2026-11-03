package com.bytter.biz.ttpay.service;
import com.bytter.biz.ttpay.dao.TpPayTypeDao;
import com.bytter.biz.ttpay.entity.TpPayType;
import com.bytter.biz.ttpay.vo.TpPayTypeVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.biz.payment.api.enums.TtPayMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service("tpShopTypeNoService")
@Transactional
public class TpPayTypeService extends BaseService<TpPayType, String> {
    @Autowired
    private TpPayTypeDao tpPayTypeDao;
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getQueryDataTablesUnion(DataTablesInput input) {
        DataTablesOutput data=tpPayTypeDao.getQueryDataTablesUnion(input);
        return data;
    }
    @SuppressWarnings("rawtypes")
    public void saveTpShopType(TpPayType tpShopType, ICurrentUser user) {
        tpShopType.setStatus(CommonConstant.STATUE_CHECK);
        tpShopType.setCreateBy(user.getLoginName());
        tpShopType.setCreateDate(DateUtils.getDate());
        if(!tpPayTypeDao.checkTpShopType(tpShopType,"code")){
            throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_SHOPTYPENO_CODE);
        }
        if (!tpPayTypeDao.checkTpShopType(tpShopType,"type")){
            throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_SHOPTYPENO_TYPE);
        }
        tpPayTypeDao.save(tpShopType);
    }
    //获取类型名称
    public List<TpPayTypeVo> getTypeName(){
        return tpPayTypeDao.getTypeName();
    }
    @SuppressWarnings("rawtypes")
    public void updateTpShopType(TpPayType tpShopType, ICurrentUser user) {
        tpShopType.setStatus(CommonConstant.STATUE_CHECK);
        tpShopType.setUpdateBy(user.getLoginName());
        tpShopType.setUpdateDate(DateUtils.getDate());
        if(!tpPayTypeDao.checkTpShopType(tpShopType,"code")){
            throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_SHOPTYPENO_CODE);
        }
        if (!tpPayTypeDao.checkTpShopType(tpShopType,"type")){
            throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_SHOPTYPENO_TYPE);
        }
        tpPayTypeDao.update(tpShopType);
    }
    @Override
    public BaseDao<TpPayType, String> getEntityDao() {
        return tpPayTypeDao;
    }
}
