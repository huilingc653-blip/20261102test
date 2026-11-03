package com.bytter.biz.netbank.service.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.expense.service.NisExpenseDetailService;
import com.bytter.biz.expense.vo.NisExpenseDetailVO;
import com.bytter.biz.payment.api.dto.NisExpenseDetailDTO;
import com.bytter.biz.payment.api.service.INisExpenseDetailService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
/**
 * @author huangx
 * @Description 费用报销
 * @date 2017.9.14
 */
@Service("nisExpenseDetailServiceImpl")
@Transactional
@DubboService
public class NisExpenseDetailServiceImpl implements INisExpenseDetailService {
    @Autowired
    private NisExpenseDetailService nisExpenseDetailService;
    /**
     * 根据付款Id获取交易失败的费用报销明细列表
     *
     * @param billId
     * @return
     */
    @Override
    public List<NisExpenseDetailDTO> listByBillIdFailure(String billId) {
        List<NisExpenseDetailVO> listVo = nisExpenseDetailService.listByBillIdFailure(billId);
        List<NisExpenseDetailDTO> list = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(listVo)) {
            for (NisExpenseDetailVO vo : listVo) {
                NisExpenseDetailDTO dto = new NisExpenseDetailDTO();
                BeanUtils.copyNotNullProperties(vo, dto);
                list.add(dto);
            }
        }
        return list;
    }
    /**
     * 根据付款Id获取等待结果的费用报销明细列表
     *
     * @param billId
     * @return
     */
    @Override
    public List<NisExpenseDetailDTO> listByBillIdWaitResult(String billId) {
        List<NisExpenseDetailVO> listVo = nisExpenseDetailService.listByBillIdWaitResult(billId);
        List<NisExpenseDetailDTO> list = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(listVo)) {
            for (NisExpenseDetailVO vo : listVo) {
                NisExpenseDetailDTO dto = new NisExpenseDetailDTO();
                BeanUtils.copyNotNullProperties(vo, dto);
                list.add(dto);
            }
        }
        return list;
    }
    /**
     * 获取费用报销单总金额
     * @param billId
     * @return
     */
    @Override
    public BigDecimal getSumExpenseMoneyByBillId(String billId){
        return nisExpenseDetailService.getSumExpenseMoneyByBillId(billId);
    }
    /**
     * 获取费用报销单失败总金额
     *
     * @param billId
     * @return
     */
    @Override
    public BigDecimal getSumFailExpenseMoneyByBillId(String billId) {
        return nisExpenseDetailService.getSumFailExpenseMoneyByBillId(billId);
    }
    /**
     * 根据付款Id获取费用报销明细列表
     *
     * @param billId
     * @return
     */
    @Override
    public List<NisExpenseDetailDTO> listByBillId(String billId) {
        List<NisExpenseDetailVO> listVo = nisExpenseDetailService.listByBillId(billId);
        List<NisExpenseDetailDTO> list = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(listVo)) {
            for (NisExpenseDetailVO vo : listVo) {
                NisExpenseDetailDTO dto = new NisExpenseDetailDTO();
                BeanUtils.copyNotNullProperties(vo, dto);
                list.add(dto);
            }
        }
        return list;
    }
    @Override
    public List<NisExpenseDetailDTO> listErpTodoBill() {
        List<NisExpenseDetailVO> listErp = nisExpenseDetailService.listErpTodoBill();
        if (EmptyUtils.isEmptyList(listErp)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(listErp, NisExpenseDetailDTO.class);
    }
}
