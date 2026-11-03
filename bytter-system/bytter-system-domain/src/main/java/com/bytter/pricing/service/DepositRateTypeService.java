package com.bytter.pricing.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.StringUtils;
import com.bytter.pricing.dao.DepositRateTypeDao;
import com.bytter.pricing.entity.DepositRateType;
import com.bytter.pricing.vo.DepositRateTypeVO;
import com.bytter.system.pricing.constant.PricingConstant;
import com.bytter.system.pricing.enums.PricingMessageEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * @author luxy
 * @Title: DepositRateTypeService
 * @ProjectName T2_IDEA
 * @Description: 存款利率类型服务层
 * @date 2019/5/22 14:23
 */
@Service("DepositIRateTypeService")
public class DepositRateTypeService extends BaseService<DepositRateType,String> {
    @Autowired
    private DepositRateTypeDao depositIRateTypeDao;
    @Override
    public BaseDao<DepositRateType, String> getEntityDao() {
        return depositIRateTypeDao;
    }
    public void save(DepositRateTypeVO depositRateTypeVO){
        DepositRateType depositRateType=new DepositRateType();
        if(StringUtils.isNotEmpty(depositRateTypeVO.getId())){//修改数据
            depositRateType=this.get(depositRateTypeVO.getId());
        }
        BeanUtils.copyProperties(depositRateTypeVO,depositRateType);
        if(depositRateTypeVO.getDepositClass().equals(PricingConstant.DEPOSIT_CLASS_TZ)){
            depositRateType.setPeriod(depositRateTypeVO.getNoticeType());
        }
        if(StringUtils.isNotEmpty(depositRateTypeVO.getId())){//修改数据
            super.saveOrUpdate(depositRateType);
        }else{
            super.saveEntity(depositRateType);
        }
    }
    public void isAllowOp(DepositRateType entity){
        if(depositIRateTypeDao.isReferedRate(entity)){//被引用
            throw new ParameterException(PricingMessageEnum.PRICING_DEPOSITRATETYPE_ATUSE);
        }
    }
    /**
     * 根据编号获取存款利率类型
     * @param code
     * @return
     */
    public DepositRateType findPeriodByCode(String code){
        return depositIRateTypeDao.findPeriodByCode(code);
    }
    /**
     * 获取所有存款利率类型
     */
    public List<DepositRateType> getTypeList(){
        return depositIRateTypeDao.getAllRateType();
    }
    /**
     * 根据存款类别获取存款利率类型
     */
    public List<DepositRateType> getTypeListByClass(String depositClass){
        return depositIRateTypeDao.getRateTypeByDepositClass(depositClass);
    }
    /**
     * 根据存款类别和存款账户标识获取存款利率类型
     * @param depositClass 存款类型标识
     * @param depositAccSign 存款账户标识
     * @return 存款利率类型集合
     */
    public List<DepositRateType> getTypeListByParams(String depositClass,String depositAccSign){
        return depositIRateTypeDao.getRateTypeByParams(depositClass,depositAccSign);
    }


    /**
     * 根据存款类别和存款账户标识获取存款利率类型
     * @param depositClass 存款类型标识
     * @param depositAccSign 存款账户标识
     * @return 存款利率类型集合
     */
    public List<DepositRateType> getRateTypeByParamsNotJoinCur(String depositClass,String depositAccSign){
        return depositIRateTypeDao.getRateTypeByParamsNotJoinCur(depositClass,depositAccSign);
    }
}
