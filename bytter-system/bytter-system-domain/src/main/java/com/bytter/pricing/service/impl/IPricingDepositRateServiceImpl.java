package com.bytter.pricing.service.impl;
import com.bytter.pricing.service.DepositRateService;
import com.bytter.pricing.vo.DepositRateVO;
import com.bytter.system.pricing.dto.DepositRateDTO;
import com.bytter.system.pricing.service.IPricingDepositRateService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author luxy
 * @Title: IPricingDepositRateServiceImpl
 * @ProjectName T2_IDEA
 * @Description: 存款利率维护接口实现类
 * @date 2019/5/27 10:32
 */
@Service("pricingDepositRateServiceI")
@Transactional
@DubboService
public class IPricingDepositRateServiceImpl implements IPricingDepositRateService {
    @Autowired
    private DepositRateService depositRateService;
    /**
     * 根据存款账户类别获取存款利率信息
     * @param accType 存款账户类别
     * @param time 开户日期
     * @return 存款利率DTO
     */
    @Override
    public DepositRateDTO getDepositRateByAccType(String accType, Date time,String curId) {
        List<DepositRateDTO> depositRateDTOList=new ArrayList<DepositRateDTO>();
        String depositClass="";
        if(accType.equals("01")){//数据库存储与定义类别不同，需进行类别对应转换
            depositClass="04";//活期
        }else if(accType.equals("08")){
            depositClass="06";//通知
        }else if(accType.equals("05")){
            depositClass="05";//定期
        }
        if(StringUtils.isNotEmpty(depositClass)){
            depositRateDTOList=depositRateService.getDepositRateByClass(depositClass,time,curId);
        }
        if(!EmptyUtils.isEmptyList(depositRateDTOList)){
            return depositRateDTOList.get(0);
        }else{
            return null;
        }
    }
    /**
     * 根据id 获取存款利率
     * @param id
     * @return 存款利率DTO
     */
    @Override
    public DepositRateDTO getDepositRateById(String id) {
        DepositRateDTO depositRateDTO=new DepositRateDTO();
        DepositRateVO depositRateVO=depositRateService.getDepositRateById(id);
        BeanUtils.copyNotNullProperties(depositRateVO,depositRateDTO);
        return depositRateDTO;
    }
    /**
     * 根据条件查询存款利率
     * @param depositRateDTO
     * @return
     */
    @Override
    public DepositRateDTO getDepositRateByParams(DepositRateDTO depositRateDTO) {
        return depositRateService.getDepositRateByParams(depositRateDTO);
    }
    @Override
    public BigDecimal getDepositRateByPeriod(String depositClass,String depositPeriod,String curId) {
        return depositRateService.getRateByPeriod(depositClass,depositPeriod,curId);
    }
}
