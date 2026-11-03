package com.bytter.biz.receivable.service.impl;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.NisReceivableDTO;
import com.bytter.biz.payment.api.service.INisMessageService;
import com.bytter.biz.payment.api.service.INisReceivableService;
import com.bytter.biz.receivable.entity.NisReceivable;
import com.bytter.biz.receivable.service.NisReceivableService;
import com.bytter.biz.receivable.vo.NisReceivableVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author wangzx
 * @Description 收款单接口实现类
 * @date 2019/7/5
 */
@Service("nisReceivableServiceImpl")
@Transactional
@DubboService
public class NisReceivableServiceImpl implements INisReceivableService {
    @Autowired
    private NisReceivableService nisReceivableService;
    @DubboReference(check = false)
	private INisMessageService nisMessageService;
    /**
     * 收款单生成（供流水认领/ERP接口写）
     *
     * @param nisReceivableDTO
     */
    @Override
    public void saveNisReceivable(NisReceivableDTO nisReceivableDTO, ICurrentUser user) {
        NisReceivableVO entity = new NisReceivableVO();
        BeanUtils.copyNotNullProperties(nisReceivableDTO, entity);
        nisReceivableService.saveNisReceivable(entity, user);
    }
    /**
     * 更新收款单（供流水认领）
     *
     * @param billNo       收款单号
     * @param bankSerialNo 银行流水号
     * @param status       状态
     * @param user         用户
     * @param msg          信息
     */
    @Override
    public void updateNisReceivable(String billNo, String bankSerialNo, Integer status, String msg,String busType, ICurrentUser user,String sysBusType,String billId,String busBillNo) {
        NisReceivableVO nisReceivableVO = nisReceivableService.getByBillNoUnion(billNo);
        NisReceivable entity = new NisReceivable();
        BeanUtils.copyNotNullProperties(nisReceivableVO, entity);
        entity.setStatus(status);
        if(status.equals(NetbankConstant.STATUE_CHECK)){
            entity.setBankSerialNo("");
        }else {
            entity.setBankSerialNo(bankSerialNo);
        }
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        entity.setBusType(busType);
        entity.setSysBusType(sysBusType);
        entity.setBillId(billId);
        entity.setBusBillNo(busBillNo);
        nisReceivableService.saveOrUpdate(entity);
        //生成日志
        nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_RECEIVABLE_SUCCEED, msg, NisMessageConstant.BUS_TYPE_RECEIVABLE, user.getLoginName(), user.getUsername());
    }
    /**
     * 根据银行流水号查询收款单信息
     *
     * @param bankSerialNo 银行流水号
     */
    @Override
    public List<NisReceivableDTO> getNisReceivableByBankSerialNo(String bankSerialNo) {
        List<NisReceivableVO> listVo = nisReceivableService.getNisReceivableByBankSerialNo(bankSerialNo);
        if(!EmptyUtils.isEmptyList(listVo)){
            List<NisReceivableDTO> listDTO = BeanUtils.copyListProperties(listVo, NisReceivableDTO.class);
            return listDTO;
        }
        return null;
    }
    @Override
    public void deleteNisReceivable(String billId,ICurrentUser user) {
        deleteNisReceivable(billId, user, true);
    }

    @Override
    public void deleteNisReceivable(String billId, ICurrentUser user, boolean triggerBillheadRollback) {
        NisReceivableVO vo = nisReceivableService.getByBillId(billId);
        nisReceivableService.deleteNisReceivable(vo.getId(), user, false, triggerBillheadRollback);
    }
    @Override
    public NisReceivableDTO getNisReceivableByBillId(String billId) {
        NisReceivableVO vo = nisReceivableService.getByBillId(billId);
        NisReceivableDTO dto = new NisReceivableDTO();
        if(!EmptyUtils.isEmpty(vo)){
            BeanUtils.copyNotNullProperties(vo,dto);
        }
        return dto;
    }
}
