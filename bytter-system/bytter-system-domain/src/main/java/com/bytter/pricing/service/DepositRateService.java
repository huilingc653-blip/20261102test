package com.bytter.pricing.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.pricing.dao.DepositRateDao;
import com.bytter.pricing.entity.DepositRate;
import com.bytter.pricing.vo.DepositRateVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.pricing.constant.PricingConstant;
import com.bytter.system.pricing.dto.DepositRateDTO;
import com.bytter.system.pricing.enums.PricingMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author luxy
 * @Title: DepositRateService
 * @ProjectName T2_IDEA
 * @Description: 存款利率维护服务层
 * @date 2019/5/23 9:43
 */
@Service("DepositRateService")
public class DepositRateService extends BaseService<DepositRate,String> {
    @Autowired
    private DepositRateDao depositRateDao;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Override
    public BaseDao<DepositRate, String> getEntityDao() {
        return depositRateDao;
    }
    /**
     * 查询存款利率列表数据
     */
    public DataTablesOutput datatable(DataTablesInput input, ICurrentUser user){
        StringBuilder joinSql=new StringBuilder();
        joinSql.append(" and m.create_by ='").append(user.getLoginName()).append("'");
        DataTablesOutput dataTablesOutput = depositRateDao.getDataTables(input,joinSql.toString());
        List<DepositRateVO> list = dataTablesOutput.getData();
        if(!CollectionUtils.isEmpty(list)){
            Map<String, BtCurrencyDTO> btCurrencyMap = btCurrencyService.getCurrency();
            for(DepositRateVO vo : list){
                if(btCurrencyMap != null){
                    if(btCurrencyMap.containsKey(vo.getMoneyType())){
                        BtCurrencyDTO dto = btCurrencyMap.get(vo.getMoneyType());
                        if(dto != null){
                            vo.setCurName(dto.getCurName());
                        }
                    }
                }
            }
        }
        return dataTablesOutput;
    }
    /**
     * 保存存款利率维护数据
     */
    public void save(DepositRateVO depositRateVO,ICurrentUser user){
        if(depositRateVO.getDepositClass().equals(PricingConstant.DEPOSIT_CLASS_HQ)){//活期的时候分基础/基准利率
            if(depositRateVO.getReferenceRateType().equals("0")){//基准利率
                depositRateVO.setRate(depositRateVO.getReferenceRate());
            }else if(depositRateVO.getReferenceRateType().equals("1")){//基础利率
                depositRateVO.setRate(depositRateVO.getBaseRate());
            }
        }else{
            depositRateVO.setRate(depositRateVO.getRate());
        }
        DepositRate depositRate=new DepositRate();
        List<DepositRateVO> depositRateVOS=depositRateDao.getDepositRates(depositRateVO);//查询同编号，币别的存款利率
        if(depositRateVOS.size()>0){
            DepositRateVO lastDepositRate=depositRateVOS.get(depositRateVOS.size()-1);//获取上一次维护维护的同编号，币别的存款利率
            if(depositRateVO.getTimeOfTakeEffect().getTime()<lastDepositRate.getTimeOfTakeEffect().getTime()){//同类型编号、同币别的科目利率维护，如果非第一次维护，则控制生效日期大于上一次维护的生效日期。
                throw new ParameterException(PricingMessageEnum.PRICING_DEPOSITRATE_EFFECTTIMELELAST,depositRateVO.getRateTypeName(),DateUtils.formatDate(lastDepositRate.getTimeOfTakeEffect()));
            }
        }
        if(StringUtils.isNotEmpty(depositRateVO.getId())){//编辑操作
            depositRate=this.get(depositRateVO.getId());
            List<DepositRateVO> searchResult=this.getDepositRateByParams(depositRateVO);
            if(searchResult.size()>0){
                throw new ParameterException(PricingMessageEnum.PRICING_DEPOSITRATE_DATAREPEAT,depositRateVO.getRateTypeName(),DateUtils.formatDate(depositRateVO.getTimeOfTakeEffect()));
            }
            BeanUtils.copyProperties(depositRateVO,depositRate);
            depositRate.setTimeOfLastModifier(DateUtils.getDate());
            this.getSession().flush();
            this.saveOrUpdate(depositRate);
        }else{//新增数据
            List<DepositRateVO> searchResult=this.getDepositRateByParams(depositRateVO);
            if(searchResult.size()>0){
                throw new ParameterException(PricingMessageEnum.PRICING_DEPOSITRATE_DATAREPEAT,depositRateVO.getRateTypeName(),DateUtils.formatDate(depositRateVO.getTimeOfTakeEffect()));
            }
            BeanUtils.copyProperties(depositRateVO,depositRate);
            depositRate.setCreateBy(user.getLoginName());
            this.saveEntity(depositRate);
        }
    }
    /**
     *  根据id获取存款利率维护数据
     */
    public DepositRateVO getDepositRateById(String id){
        DepositRateVO depositRateVO = depositRateDao.getDepositRateById(id);
        //获取币种信息
        BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(depositRateVO.getMoneyType());
        if(btCurrencyDTO != null){
            depositRateVO.setCurName(btCurrencyDTO.getCurName());
        }
        return depositRateVO;
    }
    /**
     * 查询同编号同币别同日期的存款利率是否处于业务流中
     */
    public List<DepositRateVO> getDepositRateByParams(DepositRateVO depositRateVO){
        List<DepositRateVO> depositRates=depositRateDao.getDepositRateByParams(depositRateVO);
        return depositRates;
    }
    /**
     * 根据存款类型编号及开户时间获取复核通过的存款利率维护数据
     */
    public List<DepositRateDTO> getDepositRateByClass(String depositClass, Date time,String curId){
        return depositRateDao.getDepositRateByDepositClass(depositClass,time,curId);
    }
    /**
     * 根据条件查询存款利率
     */
    public DepositRateDTO getDepositRateByParams(DepositRateDTO depositRateDTO){
        List<DepositRateDTO> depositRateDTOS=depositRateDao.getDepositRateByParams(depositRateDTO);
        DepositRateDTO rateDTO=null;
        if(depositRateDTOS!=null&&depositRateDTOS.size()>0){//数据有两条以上时取时间最近的一条
            rateDTO=depositRateDTOS.get(0);
        }
        return rateDTO;
    }
    public BigDecimal getRateByPeriod(String depositClass, String depositPeriod,String curId){
        BigDecimal rate=BigDecimal.ZERO;
        Integer period=null;
        Integer periodCycle=null;
        if(EmptyUtils.isNotEmpty(depositPeriod)){
            char[] peArr=depositPeriod.toCharArray();
            period=Integer.parseInt(peArr[0]+"");
            if(peArr[1]=='M'){
                periodCycle=1;
            }else if(peArr[1]=='Y'){
                periodCycle=2;
            }
        }
        DepositRateDTO depositRateDTO=new DepositRateDTO();
        depositRateDTO.setMoneyType(curId);
        depositRateDTO.setDepositClass(depositClass);
        depositRateDTO.setPeriod(period);
        depositRateDTO.setPeriodCycle(periodCycle);
        List<DepositRateDTO> depositRateDTOS=depositRateDao.getDepositRateByParams(depositRateDTO);
        if(depositRateDTOS!=null&&depositRateDTOS.size()>0){//数据有两条以上时取时间最近的一条
            DepositRateDTO rateDTO=depositRateDTOS.get(0);
            rate=rateDTO.getRate();
        }
        return rate;
    }
}
