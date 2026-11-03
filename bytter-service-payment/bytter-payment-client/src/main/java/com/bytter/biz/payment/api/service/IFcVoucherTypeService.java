package com.bytter.biz.payment.api.service;
import com.bytter.biz.payment.api.dto.BtAssignPayWayDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import java.util.List;
import java.util.Map;
/**
 * 付款方式接口类
 * @author huangx
 * @since 2017-09-02 09:49:44
 */
public interface IFcVoucherTypeService {
    /**
     * 根据系统结算方式获取系统凭证种类(付款方式)
     * @param voucherType 付款方式编码
     * @return 付款方式
     */
    FcVoucherTypeDTO getFcVoucherTypeByCode(String voucherType);
    /**
     * 获取系统凭证种类(付款方式)
     * @param sysVoucherCode 为空默认取内转 不为空默认取第一个参数对应的值
     * @return 付款方式
     */
    FcVoucherTypeDTO getSysFcVoucherType(String... sysVoucherCode);
    
    /**
     * 根据付款方式ID获取凭证种类详细(付款方式ID)
     * @param voucherId 付款方式ID
     * @return 付款方式
     */
    FcVoucherTypeDTO getFcVoucherTypeById(String voucherId);
    List<FcVoucherTypeDTO> getFcVoucherTypeByIds(List<String> voucherIds);
	/**
	 * 查询所有有效、记账的付款方式
	 * @return 付款方式集合
	 */
	List<FcVoucherTypeDTO> getFcVoucherType();
	/**
	 * 根据付款方式名称获取付款方式
	 * @param voucherName 付款方式名称
	 * @return 付款方式
	 */
	FcVoucherTypeDTO getDtoByVoucherName(String voucherName);
	/**
	 * 查询所有付款方式
	 * @return 付款方式集合
	 */
	List<FcVoucherTypeDTO> getFcVoucherTypeAll();
	
	/**
	 * 查询所有有效的付款方式
	 * @return 付款方式集合
	 */
	List<FcVoucherTypeDTO> getValidFcVoucherType();
	/**
	 * 根据单位ID获取分配的付款方式信息
	 * @param corpId
	 * @return
	 */
	List<BtAssignPayWayDTO> getBtAssignPayWays(String corpId);
	/**
	 * 查询所有有效的结算业务相关的付款方式
	 * @return
	 */
	List<FcVoucherTypeDTO> getFcVoucherTypeForSettlement();
	Map<String, FcVoucherTypeDTO> getFcVoucherTypeMap();
	public Map<String, FcVoucherTypeDTO> getFcVoucherTypeTypeMap();

	Map<String, FcVoucherTypeDTO> getFcVoucherTypeByIdsToMap(List<String> voucherIds);

    List<FcVoucherTypeDTO> getFcVoucherTypeLikeName(String voucherName);
}
