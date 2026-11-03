package com.bytter.bill.domain.bill.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import com.bytter.system.api.IConfigService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *
 * @Title:  挂接担保服务类
 * @Package com.bytter.biz.bill.accebill.service
 * @Description:    TODO
 * @author: Jinyong.cao
 * @date:   2019-7-20 下午19:55:53
 * @version V1.0
 */
@Service("abGuaranteeService")
@Transactional
public class AbGuaranteeService extends BaseService<String, AbAccrecords> {
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;

	@Resource(name = "guaContractOccupServieImpl")
	private IGuaContractOccupServie guaContractOccupServie;
	@Autowired
	private IGuaWayServie guaWayServie;
	@DubboReference(check = false)
	private IConfigService configService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 是否挂担保合同
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isBear(){
		return configService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISBEAR).toBoolean(true);
	}
	/**
	 * 是否挂担保显示
	 * @return
	 */
	@Transactional(readOnly = true)
	public Integer isHangGuarantee(){
		return configService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISBEAR).toInteger(0);
	}
	/**
	 *
	 * @Title: bearContractLink
	 * @Description: TODO(承兑汇票挂接担保的方法)
	 * @param @param optType
	 * @param @param userVo
	 * @param @param abEmitBillVo    参数
	 * @return void    返回类型
	 * @throws
	 */
	public void  bearContractLink(String optType,AbAccrecordsVO abEmitBillVo,ICurrentUser userVo){
		GuaContractOccupDTO bearVo = convertGuaContractOccupDTO(abEmitBillVo);
		if(bearVo==null || !isBear() || StringUtils.isBlank(abEmitBillVo.getIsHangGuarantee())){
			return;
		}
		//如果设置单笔业务不挂
		if(!AccebillConstant.AB_BILL_RATMODEL_Y.equals(abEmitBillVo.getIsHangGuarantee())){
			if(AccebillConstant.BILL_OPERATE_ADD.equals(optType)
					||AccebillConstant.BILL_OPERATE_EDIT.equals(optType)
					||AccebillConstant.BILL_OPERATE_SUBMIT.equals(optType)){
				//对已挂暂存的数据进行删除操作
				guaContractOccupServie.releaseUseGuaContractOccup(bearVo);
			}else if(AccebillConstant.BILL_OPERATE_SUPPLY.equals(optType)){
				//补录时进行退回再删除
				guaContractOccupServie.backUseGuaContractOccup(bearVo);
				guaContractOccupServie.deleteGuaContractOccup(abEmitBillVo.getVvid(),AccebillConstant.AB_CONFIG_TYPE);
			}
			return;
		}
		if(optType.equals(AccebillConstant.BILL_OPERATE_ADD)
				|| AccebillConstant.BILL_OPERATE_EDIT.equals(optType)){
			guaContractOccupServie.tempUseGuaContractOccup(bearVo);
		}else if(AccebillConstant.BILL_OPERATE_SUBMIT.equals(optType)){
			guaContractOccupServie.submitUseGuaContractOccup(bearVo);
		}else if(AccebillConstant.BILL_OPERATE_BACK.equals(optType)){
			guaContractOccupServie.backUseGuaContractOccup(bearVo);
//			this.editBillViceBearModel(abEmitBillVo.getVvid(), "0");
		}else if(AccebillConstant.BILL_OPERATE_APPOK.equals(optType)){
			guaContractOccupServie.saveUseGuaContractOccup(bearVo);
		}else if(AccebillConstant.BILL_OPERATE_REPULSE.equals(optType)){
			guaContractOccupServie.releaseUseGuaContractOccup(bearVo);
		}else if(AccebillConstant.BILL_OPERATE_DELETE.equals(optType)){
			guaContractOccupServie.deleteGuaContractOccup(abEmitBillVo.getVvid(),AccebillConstant.AB_CONFIG_TYPE);
		}else if(AccebillConstant.BILL_OPERATE_SUPPLY.equals(optType)){
			guaContractOccupServie.submitUseGuaContractOccup(bearVo);
		}
	}
	/**
	 *
	 * @Title: convertGuaContractOccupDTO
	 * @Description: TODO(转换担保VO)
	 */
	protected GuaContractOccupDTO convertGuaContractOccupDTO(AbAccrecordsVO abEmitBillVo){
		GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
		if(null!=abEmitBillVo&&!StringUtils.isBlank(abEmitBillVo.getIsHangGuarantee())){
			guaContractOccupDTO.setBusinessId(abEmitBillVo.getVvid()); //票据从表ID
			guaContractOccupDTO.setContractIds(abEmitBillVo.getContractIds()!=null?abEmitBillVo.getContractIds():null);
			guaContractOccupDTO.setSysCode(AccebillConstant.AB_CONFIG_TYPE);
			guaContractOccupDTO.setContractCode(abEmitBillVo.getGuaContract());
			guaContractOccupDTO.setItemNo(abEmitBillVo.getAppNo());
			guaContractOccupDTO.setUseDate(DateUtils.getDate());
			guaContractOccupDTO.setGuaWay(abEmitBillVo.getGuaWay());
			guaContractOccupDTO.setGuaWay(abEmitBillVo.getGuaWayNames());
			guaContractOccupDTO.setGuaRmk(abEmitBillVo.getGuaranteeRmk());
		}
		return guaContractOccupDTO;
	}
	@Transactional(readOnly = true)
	public void editBillViceBearModel(String vvid,String status){
		AbVoucherinfoVice vo = abVoucherinfoViceService.get(vvid,true);
		vo.setBearModel(status);
		abVoucherinfoViceService.update(vo);
	}
	@Transactional(readOnly = true)
	public GuaContractOccupDTO getGuaContractOccupDTOByBillId(String vvid){
		if(guaContractOccupServie==null){
			return null;
		}
		GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
		guaContractOccupDTO.setSysCode(AccebillConstant.AB_CONFIG_TYPE);
		guaContractOccupDTO.setBusinessId(vvid);
		return guaContractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
	}
	@Transactional(readOnly = true)
	public String getGuaWayNameByCodes(String code){
		return guaWayServie.getGuaWayNameByCodes(code);
	}


	public GuaContractUseDTO getGuaContractOccupyDtl(String businessId, String itemNo) {
		return guaContractOccupServie.getGuaContractOccupyDtl(businessId, itemNo);
	}
}
