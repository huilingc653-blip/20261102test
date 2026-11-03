package com.bytter.biz.netbank.service.impl;

import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.netbank.service.BtAssignPayWayService;
import com.bytter.biz.netbank.service.FcVoucherTypeService;
import com.bytter.biz.netbank.vo.BtAssignPayWayVO;
import com.bytter.biz.netbank.vo.FcVoucherTypeVO;
import com.bytter.biz.payment.api.dto.BtAssignPayWayDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author huangx
 * @Description 付款方式接口实现类
 * @date 2017.9.14
 */
@Service("fcVoucherTypeServiceImpl")
@Transactional
@DubboService
@Primary
public class FcVoucherTypeServiceImpl implements IFcVoucherTypeService {
    @Autowired
    private FcVoucherTypeService fcVoucherTypeService;
	@Autowired
	private BtAssignPayWayService btAssignPayWayService;
	/**
	 * 根据系统结算方式获取系统凭证种类(付款方式)
	 * @param voucherCode
	 * @return
	 */
	@Override
	public FcVoucherTypeDTO getFcVoucherTypeByCode(String voucherCode) {
		 return fcVoucherTypeService.getFcVoucherTypeByCode(voucherCode);
	}
	/**
	 * 获取系统凭证种类(付款方式)
	 * @param sysVoucherCode 为空默认取内转 不为空默认取第一个参数对应的值
	 * @return
	 */
	@Override
	public FcVoucherTypeDTO getSysFcVoucherType(String... sysVoucherCode) {
		FcVoucherTypeVO voucherType = null;
		FcVoucherTypeDTO typeDTO = new FcVoucherTypeDTO();
		if(sysVoucherCode.length == 0){
			voucherType = fcVoucherTypeService.getSystemBySysVoucherCode(NetbankConstant.PAY_TYPE_05);
		}else{
			voucherType = fcVoucherTypeService.getSystemBySysVoucherCode(sysVoucherCode[0]);
		}
		if (voucherType != null) {
			BeanUtils.copyProperties(voucherType, typeDTO);
		}
		return typeDTO;
	}
	@Override
	public FcVoucherTypeDTO getFcVoucherTypeById(String voucherId) {
		FcVoucherTypeVO typeVO = fcVoucherTypeService.getByIdUnionById(voucherId);
		if (null != typeVO) {
			FcVoucherTypeDTO typeDTO = new FcVoucherTypeDTO();
			BeanUtils.copyProperties(typeVO, typeDTO);
			return typeDTO;
		}
		return null;
	}
	@Override
	public List<FcVoucherTypeDTO> getFcVoucherTypeByIds(List<String> voucherIds){
		List<FcVoucherTypeDTO> dtoList = fcVoucherTypeService.getFcVoucherTypeByIds(voucherIds);
		return dtoList;
	}
	/**
	 * <b>功能：</b>查询所有有效、记账的付款方式<br>
	 * <b>日期：</b> 2017-10-25 <br>
	 */
	@Override
	public List<FcVoucherTypeDTO> getFcVoucherType() {
		return fcVoucherTypeService.getFcVoucherType();
	}
	@Override
	public List<FcVoucherTypeDTO> getFcVoucherTypeAll() {
		return fcVoucherTypeService.getFcVoucherTypeAll();
	}
	
	/**
	 * <b>功能：</b>查询所有有效的付款方式<br>
	 * <b>日期：</b> 2017-10-25 <br>
	 */
	@Override
	public List<FcVoucherTypeDTO> getValidFcVoucherType() {
		return fcVoucherTypeService.getValidFcVoucherType();
	}
	@Override
	public FcVoucherTypeDTO getDtoByVoucherName(String voucherName){
		return fcVoucherTypeService.getDtoByVoucherName(voucherName);
	}
	/**
	 * 根据单位ID获取分配的付款方式信息
	 * @param corpId
	 * @return
	 */
	@Override
	public List<BtAssignPayWayDTO> getBtAssignPayWays(String corpId){
		List<BtAssignPayWayVO> btAssignPayWayVOS = btAssignPayWayService.getByCorpId(corpId);
		List<BtAssignPayWayDTO> btAssignPayWayDTOS = BeanUtils.copyListProperties(btAssignPayWayVOS,BtAssignPayWayDTO.class);
		return btAssignPayWayDTOS;
	}
	/**
	 * 查询所有有效的结算业务相关的付款方式
	 * @return
	 */
	@Override
	public List<FcVoucherTypeDTO> getFcVoucherTypeForSettlement() {
		return fcVoucherTypeService.getFcVoucherTypeForSettlement();
	}
	@Override
	public Map<String, FcVoucherTypeDTO> getFcVoucherTypeMap() {
		List<FcVoucherType> list = this.fcVoucherTypeService.listAll();
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		List<FcVoucherTypeDTO> resultList = BeanUtils.copyListProperties(list,FcVoucherTypeDTO.class);
		return resultList.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, Function.identity()));
	}
	@Override
	public Map<String, FcVoucherTypeDTO> getFcVoucherTypeTypeMap() {
		List<FcVoucherType> list = this.fcVoucherTypeService.listAll();
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		List<FcVoucherTypeDTO> resultList = BeanUtils.copyListProperties(list,FcVoucherTypeDTO.class);
		return resultList.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getVoucherType, Function.identity()));
	}

	@Override
	public Map<String, FcVoucherTypeDTO> getFcVoucherTypeByIdsToMap(List<String> voucherIds) {
		List<FcVoucherTypeDTO> dtoList = fcVoucherTypeService.getFcVoucherTypeByIds(voucherIds);
		if (EmptyUtils.isEmptyList(dtoList)) {
			return null;
		}
		List<FcVoucherTypeDTO> resultList = BeanUtils.copyListProperties(dtoList,FcVoucherTypeDTO.class);

		return resultList.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, Function.identity()));
	}

	@Override
	public List<FcVoucherTypeDTO> getFcVoucherTypeLikeName(String voucherName) {
		return fcVoucherTypeService.getFcVoucherTypeLikeName(voucherName);
	}
}
