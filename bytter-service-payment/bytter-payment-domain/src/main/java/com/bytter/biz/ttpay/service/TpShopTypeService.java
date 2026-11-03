package com.bytter.biz.ttpay.service;
import com.bytter.biz.payment.api.enums.TtPayMessageEnum;
import com.bytter.biz.ttpay.dao.TpShopTpyeDao;
import com.bytter.biz.ttpay.entity.TpShopType;
import com.bytter.biz.ttpay.vo.TpShopTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service("tpShopTypeService")
@Transactional
public class TpShopTypeService extends BaseService<TpShopType, String> {
    @Autowired
    public TpShopTpyeDao tpShopTpyeDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getQueryDataTablesUnion(DataTablesInput input) {
        DataTablesOutput data=tpShopTpyeDao.getQueryDataTablesUnion(input);
        return data;
    }
    @SuppressWarnings("rawtypes")
    public void saveTpShopType(TpShopType tpShopType, ICurrentUser user) {
        tpShopType.setStatus(CommonConstant.STATUE_CHECK);
        tpShopType.setCreateBy(user.getLoginName());
        tpShopType.setCreateDate(DateUtils.getDate());
        if(!tpShopTpyeDao.checkTpShopType(tpShopType,"code")){
            throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_SHOPTYPE_CODE);
        }
        if (!tpShopTpyeDao.checkTpShopType(tpShopType,"type")){
            throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_SHOPTYPE_TYPE);
        }
        tpShopTpyeDao.save(tpShopType);
    }
    //获取类型名称
    public List<TpShopTypeVO> getTypeName(){
        return tpShopTpyeDao.getTypeName();
    }
    /**
     * 根据商户类型id、code、名称查找商户类别（查询支付宝、微信对象）
     * @param payTypeId 商户类型id
     * @param payTypeCode 商户类型Code
     * @param payTypeName 商户类型名称：支付宝、微信
     * @return 商户对象
     */
    public TpShopType getShopType(String payTypeId,String payTypeCode,String payTypeName){
        return tpShopTpyeDao.getShopType(payTypeId,payTypeCode,payTypeName);
    }
    @SuppressWarnings("rawtypes")
    public void updateTpShopType(TpShopType tpShopType, ICurrentUser user) {
        tpShopType.setStatus(CommonConstant.STATUE_CHECK);
        tpShopType.setUpdateBy(user.getLoginName());
        tpShopType.setUpdateDate(DateUtils.getDate());
        if(!tpShopTpyeDao.checkTpShopType(tpShopType,"code")){
            throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_SHOPTYPE_CODE);
        }
        if (!tpShopTpyeDao.checkTpShopType(tpShopType,"type")){
            throw new BusinessException(TtPayMessageEnum.TTPAY_MESSAGE_ERROR_SHOPTYPE_TYPE);
        }
        tpShopTpyeDao.update(tpShopType);
    }
    @Override
    public BaseDao<TpShopType, String> getEntityDao() {
        return tpShopTpyeDao;
    }
}
