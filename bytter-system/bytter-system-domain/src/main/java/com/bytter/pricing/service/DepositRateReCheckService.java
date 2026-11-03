package com.bytter.pricing.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.pricing.constant.PricingConstant;
import com.bytter.pricing.dao.DepositRateDao;
import com.bytter.pricing.dao.DepositRateReCheckDao;
import com.bytter.pricing.entity.DepositRate;
import com.bytter.pricing.vo.DepositRateVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author luxy
 * @Title: DepositRateReCheckService
 * @ProjectName T2_IDEA
 * @Description: TODO
 * @date 2019/5/23 21:04
 */
@Service("DepositRateReCheckService")
public class DepositRateReCheckService extends BaseService<DepositRate,String> {
    @Autowired
    private DepositRateReCheckDao depositRateReCheckDao;
    @Autowired
    private DepositRateDao depositRateDao;

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @Override
    public BaseDao<DepositRate, String> getEntityDao() {
        return depositRateReCheckDao;
    }
    /**
     * 获取待复核列表数据
     */
    public DataTablesOutput datatable(DataTablesInput input, ICurrentUser user){
        StringBuilder joinSql=new StringBuilder();
        joinSql.append(" and m.create_by not in('").append(user.getLoginName()).append("')");
        DataTablesOutput dataTablesOutput=  depositRateDao.getDataTables(input,joinSql.toString());

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
     * 根据id获取存款利率维护数据
     */
    public DepositRateVO getDepositRateById(String id){
        return depositRateDao.getDepositRateById(id);
    }
    /**
     * 批量操作
     * @param ids 存款利率的id
     * @param handleType 操作类型：2-通过，-1 打回
     * @param user 当前用户
     */
    public void reCheckBatch(String ids, String handleType, String returndeScribe,ICurrentUser user){
        List<String> rateIds=new ArrayList<String>();
        String[] arr=ids.split(",");
        for (String id:arr) {
            rateIds.add(id);
        }
        for (String id:rateIds) {
            DepositRate depositRate=this.get(id);
            if(handleType.equals(PricingConstant.STATUS_PASS.toString())){
                depositRate.setStatus(PricingConstant.STATUS_PASS);
            }else if(handleType.equals(PricingConstant.STATUS_BACK.toString())){
                depositRate.setStatus(PricingConstant.STATUS_BACK);
                depositRate.setReturndeScribe(returndeScribe);
            }
            depositRate.setCheckOp(user.getLoginName());
            this.getSession().flush();
            this.saveOrUpdate(depositRate);
        }
    }
}
