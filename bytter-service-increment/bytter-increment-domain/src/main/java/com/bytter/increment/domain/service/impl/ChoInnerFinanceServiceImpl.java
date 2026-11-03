package com.bytter.increment.domain.service.impl;
import com.bytter.increment.domain.entity.ChoInnerFinance;
import com.bytter.increment.domain.service.ChoInnerFinanceService;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.api.dto.ChoInnerFinanceDTO;
import com.bytter.increment.api.service.IChoInnerFinanceService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICreditPeriodService;
import com.bytter.system.api.dto.TbCreditPeriodDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @Title: Service
 * @Description: 内部理财信息表
 * @author huangx
 * @date 2019-11-1 14:03:30
 * @version 1.0
 *
 */
@Service("choInnerFinanceServiceImpl")
@Transactional
@DubboService
public class ChoInnerFinanceServiceImpl implements IChoInnerFinanceService {
    @Autowired
    private ChoInnerFinanceService choInnerFinanceService;
    @DubboReference(check = false)
    private ICreditPeriodService iCreditPeriodService;
    /**
     * 根据业务类型和状态获取内部理财信息集合
     * @param busType 业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知预约
     * @param status 状态
     * @return
     */
    @Override
    public DataTablesOutput listChoInnerFinanceByBusType(DataTablesInput input, Integer busType, String status, String opType, String userId, List<String> corpAuth) {
        return choInnerFinanceService.listByBusType(input,busType, status,opType,userId,corpAuth);
    }
    /**
     * 根据ID获取理财信息
     * @param id
     * @return
     */
    @Override
    public ChoInnerFinanceDTO getInnerDepositById(String id){
        return choInnerFinanceService.getInnerDepositById(id);
    }
    /**
     * 根据billNo获取理财信息
     * @param billNo
     * @return
     */
    @Override
    public ChoInnerFinanceDTO getInnerDepositByBillNo(String billNo){
        return choInnerFinanceService.getInnerDepositByBillNo(billNo);
    }
    @Override
    public void updateInnerFinance(ChoInnerFinanceDTO dto, ICurrentUser user) {
        ChoInnerFinance entity = new ChoInnerFinance();
        BeanUtils.copyNotNullProperties(dto, entity);
        entity.setUpdateBy(user.getLoginName());
        TbCreditPeriodDTO creditPeriodInfo = iCreditPeriodService.getById(entity.getDepositRateType());
        entity.setPeriodName(creditPeriodInfo.getPeriodName());
        entity.setUpdateDate(DateUtils.getDate());
        //判断是否是通知转活期，如果是通知转活期，则将该申请最近的一笔通知预约更新为已取款
        choInnerFinanceService.update(entity);
    }
    @Override
    public void tallyBackMessage(String id,ICurrentUser user) {
        choInnerFinanceService.tallyBackMessage(id,user);
    }
    @Override
    public List<ChoInnerFinanceDTO> queryTallyByBusType(Integer busType) {
        return choInnerFinanceService.queryTallyByBusType(busType);
    }
    @Override
    public void updateLastN2BData(ChoInnerFinanceDTO entity) {
        //查询最近的通知预约记录
        ChoInnerFinanceDTO dto = choInnerFinanceService.queryLastN2BData(entity);
        if (!EmptyUtils.isEmpty(dto) && EmptyUtils.isNotEmpty(dto.getId())) {
            //只有95状态的通知预约记录更新状态为取款完成
            if (InnerFinanceConstant.STATUE_CHECK.equals(dto.getStatus())
                && InnerFinanceConstant.BUS_TYPE_N2B.equals(dto.getBusType())) {
                choInnerFinanceService.updateLastN2BData(dto);
            }
        }
    }
    @Override
    public List<ChoInnerFinanceDTO> getChoInnerFinance(Integer[] status, Integer billType){
        return choInnerFinanceService.getChoInnerFinance(status, billType);
    }

    public List<String> filterBankAcc(List<String> blnctrAccs){
        return choInnerFinanceService.filterBankAcc(blnctrAccs);
    }
}
