package com.bytter.bill.domain.billPool.service.impl;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.dto.AbPoolDtlDTO;
import com.bytter.bill.api.billPool.dto.AbPoolEmitFinaApplyDTO;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.api.billPool.service.IAccebillPoolService;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.billPool.entity.AbPool;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.entity.AbPoolif;
import com.bytter.bill.domain.billPool.service.AbPoolEmitFinaApplyService;
import com.bytter.bill.domain.billPool.service.AbPoolEndorseService;
import com.bytter.bill.domain.billPool.service.AbPoolService;
import com.bytter.bill.domain.billPool.service.AbPoolifService;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitFinaApplyVO;
import com.bytter.bill.domain.billPool.vo.AbPoolEndorseVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
/**
 * 票据池公用接口实现
 * @author zhenghj
 * 2022-03-22
 */
@Service("accebillPoolServiceImpl")
@Transactional
@DubboService
public class AccebillPoolServiceImpl implements IAccebillPoolService {
	@Autowired
	private AbPoolEndorseService abPoolEndorseService;
	@Autowired
	private AbPoolEmitFinaApplyService abPoolEmitFinaApplyService;
	@Autowired
	private AbPoolService abPoolService;
	@Autowired
	private AbPoolifService abPoolifService;
	@DubboReference(check = false)
	private IBisBifInitService iBisBifInitService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Override
	public AbPoolDtlDTO getAbPoolDtlDtoByParams(String id) {
		AbPoolDtl abPoolDtl = abPoolEndorseService.getAbPoolDtlByPdId(id);
		if(abPoolDtl!=null){
			AbPoolDtlDTO abPoolDtlDTO = new AbPoolDtlDTO();
			BeanUtils.copyNotNullProperties(abPoolDtl, abPoolDtlDTO);
			return abPoolDtlDTO;
		}
		return null;
	}
	/**
	 * 票据池背书转让-付款接口调用
	 *
	 * @param poolDtlIds 票据池明细流水id
	 * @param recName 被背书人名称
	 * @param recAcc 被背书人账号
	 * @param recBankCode 被背书人行号
	 * @param businessDate 办理日期
	 * @param userVo          操作用户
	 * @return 保存的明细流水id
	 */
	@Override
	public String saveAbPoolEndorseInfoByNisPay(String poolDtlIds, String recName, String recAcc, String recBankCode, Date businessDate, ICurrentUser userVo) {
		if(StringUtils.isEmpty(poolDtlIds) || StringUtils.isEmpty(recName)||StringUtils.isEmpty(recAcc)
				||StringUtils.isEmpty(recBankCode)){
			//传递的参数值不能为空
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		AbPoolEndorseVO endorseVO = getAbBillEndorseVo(poolDtlIds,recName,recAcc,recBankCode);
		abPoolEndorseService.saveAbPoolEndorseInfo(endorseVO,userVo);
		return endorseVO.getBillIds();
	}
	/**
	 * 票据池出票融资申请-付款接口调用
	 *
	 * @param emitFinaApplyDTO 出票融资信息
	 * @param user          操作用户
	 */
	@Override
	public void saveEmitFinaApplyByNisPay(AbPoolEmitFinaApplyDTO emitFinaApplyDTO, ICurrentUser user) {
		AbPoolEmitFinaApplyVO abPoolEmitFinaApplyVO = new AbPoolEmitFinaApplyVO();
		BeanUtils.copyNotNullProperties(emitFinaApplyDTO, abPoolEmitFinaApplyVO);
		// 融资申请只支持兴业银行、中信银行
		if (StringUtils.isEmpty(abPoolEmitFinaApplyVO.getPayeracc())
				|| StringUtils.isEmpty(abPoolEmitFinaApplyVO.getBpId())) {
			throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
		}
		AbPool abPool = abPoolService.get(abPoolEmitFinaApplyVO.getBpId());
		BtBankAccDTO bankAccInfo = abAccrecordsService.getBtBankAccInfo(abPoolEmitFinaApplyVO.getPayeracc());
		if (abPool == null || bankAccInfo == null || StringUtils.isEmpty(abPool.getPifId())
				|| StringUtils.isEmpty(bankAccInfo.getBifCode())) {
			throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
		}
		AbPoolif abPoolif = abPoolifService.get(abPool.getPifId());
		BisBifInitDTO bisBifInitInfo = iBisBifInitService.getObjectById(bankAccInfo.getBifCode());
		if (bisBifInitInfo == null || abPoolif == null
				|| !((AbPoolConstant.AB_POOL_INTO_APPLE_9015.equals(bisBifInitInfo.getBifCode())
				&& AbPoolConstant.AB_POOL_INTO_APPLE_9015.equals(abPoolif.getBifCode()))
				|| (AbPoolConstant.BIF_CODE_CITIC_9001.equals(bisBifInitInfo.getBifCode())
				&& AbPoolConstant.BIF_CODE_CITIC_9001.equals(abPoolif.getBifCode())))) {
			throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_EMITFINA_ERROR);
		}
		abPoolEmitFinaApplyVO.setPayerbankno(bankAccInfo.getBankCode());
		//承兑人设置为出票人开户行
		abPoolEmitFinaApplyVO.setAcceptor(bankAccInfo.getBankName());
		abPoolEmitFinaApplyVO.setAcceptoracc("0");
		abPoolEmitFinaApplyVO.setAcceptorbankno(bankAccInfo.getBankCode());
		abPoolEmitFinaApplyVO.setAcceptorBankName(bankAccInfo.getBankName());
		//开票数据来源--应付付款
		abPoolEmitFinaApplyVO.setSource(AccebillConstant.AB_ACCEBILL_SOURCE10);
		// 保存信息
		String logType = AccebillConstant.ABLOG_LOGTYPE_ADD;
		String msg = AccebillConstant.LANG_LOG_SAVE_SUCC;
		if (abPoolEmitFinaApplyVO != null) {
			// 保存信息
			abPoolEmitFinaApplyService.saveEmitFinaInfo(AccebillConstant.BILL_OPERATE_SAVE, abPoolEmitFinaApplyVO, user, logType, msg);
			emitFinaApplyDTO.setId(abPoolEmitFinaApplyVO.getId());
		}
	}
	/**
	 * 根据参数获取背书参数对象
	 * @return
	 */
	@Transactional(readOnly = true)
	protected AbPoolEndorseVO getAbBillEndorseVo(String poolDtlIds, String recName, String recAcc, String recBankCode){
		AbPoolEndorseVO abBillEndorseVo = new AbPoolEndorseVO();
		abBillEndorseVo.setId(poolDtlIds);
		//被背书人名称
		abBillEndorseVo.setRecNm(recName);
		// 被背书人账号
		abBillEndorseVo.setRecAcctId(recAcc);
		// 被背书人行号
		abBillEndorseVo.setRecAcctsvcr(recBankCode);
		//是否可转让标记
		abBillEndorseVo.setBanendrsmtmk(AccebillConstant.AB_BANEM_Y);
		return abBillEndorseVo;
	}
}
