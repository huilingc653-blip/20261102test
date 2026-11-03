package com.bytter.erp.service;
import com.bytter.erp.dto.BtCurrencyDTO;
import java.util.List;
/**
 * 货币维护接口
 * @author zuoy
 * @date 2017/6/26.
 */
public interface IBtCurrencyService {
	/**
	 * 根据id查找
	 *
	 * @param id
	 * @return
	 */
	BtCurrencyDTO getObjectById(String id);
	
	/**
	 * 查询本位币
	 *
	 * @return
	 */
	BtCurrencyDTO getNatualCurrency();
	/**
	 * 查询人民币
	 *
	 * @return
	 */
	BtCurrencyDTO getRMBCurrency();
	/**
	 * 查询非本位币的货币信息
	 *
	 * @return
	 */
	List<BtCurrencyDTO> listByNotNatualCurrency();
	/**
	 * 根据币别名称获取ID
	 *
	 * @param curName 币别名称
	 * @return id
	 */
	String getCurrencyByName(String curName);
	/**
	 * 根据币别编码获取币别传输对象
	 *
	 * @param curCode 币别编码
	 * @return id
	 */
	BtCurrencyDTO getCurrencyByCode(String curCode);
	/**
	 * 获取有效的货币资料信息
	 *
	 * @return
	 */
	List<BtCurrencyDTO> getBtCurrency();
	/**
	 * 根据币别名称或者英文简码获取ID
	 *
	 * @param eqMatchName 匹配字
	 * @return id
	 */
	String getIdByNameOrEnglishCode(String eqMatchName);
	/**
	 * 根据ID获取币别符号
	 *
	 * @param id
	 * @return
	 */
	String getCurSign(String id);
	/**
	 * 获取所有的货币资料信息
	 *
	 * @return
	 */
	List<BtCurrencyDTO> listAllBtCurrency();
	/**
	 * 获取所有的货币资料信息
	 *
	 * @return
	 */
	List<BtCurrencyDTO> listAllBtCurrencyInit();
	/**
	 * 根据币别名称或英文见吗获取币种
	 *
	 * @param search 查询条件
	 * @return id
	 */
	BtCurrencyDTO getByCurNameOrEnglishCode(String search);
}
