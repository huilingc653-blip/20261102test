package com.bytter.biz.allocation.service.impl;
import com.bytter.biz.allocation.entity.NisAllocationReq;
import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.payment.api.dto.NisAllocationReqListDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.payment.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.enums.AllocationMessageEnum;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * 资金调拨接口实现类
 *
 * @author zuoy 2017-12-04
 */
@Service("nisAllocationReqServiceImpl")
@Transactional
@DubboService
public class NisAllocationReqServiceImpl implements INisAllocationReqService {
	@Autowired
	private NisAllocationReqService nisAllocationReqService;
	@DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IConfigService configService;
	@Autowired
	private NisMessageService nisMessageService;
	/**
	 * 保存头寸调拨单据
	 *
	 * @param nisAllocationReqDTO
	 */
	@Override
	public void saveNisAllocationReq(NisAllocationReqDTO nisAllocationReqDTO, ICurrentUser user) {
		NisAllocationReqListDTO entity = new NisAllocationReqListDTO();
		BeanUtils.copyNotNullProperties(nisAllocationReqDTO, entity);
		nisAllocationReqService.saveNisAllocationReqForImpl(entity, user);
		//消息提醒
		notificationService.sendToUserByLoginName(entity.getId(), entity.getCreateBy(), NotificationConstant.NETBANK.ALLOCATION_POS, entity);
	}
	/**
	 * 修改头寸调拨单据
	 *
	 * @param nisAllocationReqDTO
	 * @param user
	 */
	@Override
	public void updateNisAllocationReq(NisAllocationReqDTO nisAllocationReqDTO, ICurrentUser user) {
		NisAllocationReqListDTO entity = new NisAllocationReqListDTO();
		BeanUtils.copyNotNullProperties(nisAllocationReqDTO, entity);
		nisAllocationReqService.updateNisAllocationReqForImpl(entity, user);
	}
	/**
	 * 更新头寸调拨单据（银行借款反写接口）
	 * 状态：-1 打回 -2 删除
	 *
	 * @param erpId  外部编码ID
	 * @param status 状态
	 * @param msg    信息
	 */
	@Override
	public void updateNisAllocationReq(String erpId, Integer status, String msg, ICurrentUser user) {
		NisAllocationReqListDTO vo = nisAllocationReqService.getNisAllocationReqByErpId(erpId);
		//防止状态不是暂存的，不能进行修改
		if (!AllocationConstant.STATUE_TEMP.equals(vo.getStatus())) {
			throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_STATUS_DELETE);
		}
		vo.setStatus(status);
		vo.setUpdateBy(user.getLoginName());
		vo.setUpdateDate(DateUtils.getDate());
		Integer[] oldStatus = { NetbankConstant.STATUE_TEMP };
		nisAllocationReqService.updateNisAllocationReqStatus(vo, oldStatus);
		//生成日志
		if (status.equals(AllocationConstant.STATUE_BACK)) {
			nisMessageService.saveMessage(vo.getId(), NisMessageConstant.MESSAGE_TYPE_BACK, msg, NisMessageConstant.BUS_TYPE_ALLOCATION, user);
		} else if (status.equals(AllocationConstant.STATUE_DEL)) {
			nisMessageService.saveMessage(vo.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, msg, NisMessageConstant.BUS_TYPE_ALLOCATION, user);
		} else {
			nisMessageService.saveMessage(vo.getId(), NisMessageConstant.MESSAGE_TYPE_SUCCEED, msg, NisMessageConstant.BUS_TYPE_ALLOCATION, user);
		}
	}
	/**
	 * 根据外部id获取资金调拨单据
	 *
	 * @param erpId
	 * @return
	 */
	@Override
	public NisAllocationReqDTO getNisAllocationReqByErpId(String erpId) {
		NisAllocationReqListDTO vo = nisAllocationReqService.getNisAllocationReqByErpId(erpId);
		if (EmptyUtils.isEmpty(vo)) {
			return null;
		}
		NisAllocationReqDTO dto = new NisAllocationReqDTO();
		BeanUtils.copyNotNullProperties(vo, dto);
		return dto;
	}
	/**
	 * 根据外部id删除头寸调拨单据
	 *
	 * @param erpId
	 */
	@Override
	public void deleteNisAllocationReq(String erpId) {
		NisAllocationReqListDTO vo = nisAllocationReqService.getNisAllocationReqByErpId(erpId);
		if (!EmptyUtils.isEmpty(vo) && !EmptyUtils.isEmpty(vo.getId())) {
			NisAllocationReq entity = nisAllocationReqService.get(vo.getId(), true);
			nisAllocationReqService.deleteEntity(entity);
		}
	}
	/**
	 * 手工记账查询
	 *
	 * @param dataTablesInput
	 * @param user
	 * @return
	 */
	@Override
	public DataTablesOutput getDataTables(DataTablesInput dataTablesInput, ICurrentUser user) {
		return this.nisAllocationReqService.getDataTablesAccount(dataTablesInput, user);
	}
	/**
	 * 根据单号查找资金调拨单据
	 *
	 * @param billNo
	 * @return
	 * @author zuoy 2018-03-08
	 */
	@Override
	public NisAllocationReqDTO getNisAllocationReqByBillNo(String billNo) {
		NisAllocationReqListDTO vo = nisAllocationReqService.getNisAllocationReqByBillNo(billNo);
		if (EmptyUtils.isEmpty(vo)) {
			return null;
		}
		NisAllocationReqDTO dto = new NisAllocationReqDTO();
		BeanUtils.copyNotNullProperties(vo, dto);
		return dto;
	}
	/**
	 * 判断是否存在在途数据
	 * @param corpId
	 * @return
	 */
	@Override
	public boolean isExistNotFinishedNisAllocationReq(String corpId){
		List<NisAllocationReqListDTO> list = this.nisAllocationReqService.listNotFinishedNisAllocationReq(corpId);
		if(!EmptyUtils.isEmptyList(list)){
			return true;
		}
		return false;
	}
	@Override
	public List<NisAllocationReqDTO> queryTallyNisAllReq(Integer menuType) {
		return nisAllocationReqService.queryTallyNisAllReq(menuType);
	}
	/**
	 * 查询还款状态
	 */
	public List<NisAllocationReqDTO> getRepayBisExc(NisAllocationReqDTO nisAllocationReqDTO) {
		List<NisAllocationReqDTO> list = nisAllocationReqService.getRepayBisExc(nisAllocationReqDTO);
		return list;
	}
}
