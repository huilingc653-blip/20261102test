package com.bytter.system.api;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.BtBankAccAppCurVO;
import com.bytter.system.api.vo.BtBankAccVO;
import java.util.List;
import java.util.Set;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
public interface IBtBankAccCommonService {
	
	void saveOrUpdateNew(BtBankAccDTO btBankAcc);
	/**
	 * 获取有效的账户信息
	 *
	 * @return
	 */
	List<BtBankAccDTO> listEffectiveBtBankAcc();
	/**
	 * 获取银行账户表的币别信息
	 *
	 * @param bankAccId 账户ID
	 * @return
	 */
	List<BtBankAccCurDTO> listBtBankAccCurById(String bankAccId);
	/**
	 * 获取银行账户申请表的币别信息
	 *
	 * @param appId 账户ID
	 * @return
	 */
	List<BtBankAccAppCurVO> listBtBankAccAppCurById(String appId);
	/**
	 * 根据id得到币别
	 *
	 * @param id
	 * @return
	 * @author zuoy 2018-02-28
	 */
	List<BtCurrencyDTO> listCurAccById(String id);
	/**
	 * 根据id得到币别
	 *
	 * @param id
	 * @return
	 * @author zuoy 2018-02-28
	 */
	List<BtCurrencyDTO> listCurAccAppById(String id);
	/**
	 * 根据id得到entity
	 *
	 * @param id
	 * @return
	 */
	BtBankAccDTO getBtBankAcc(String id);
	/**
	 * 根据id得到entity
	 *
	 * @param id
	 * @return
	 */
	BtBankAccAppDTO getBtBankAccApp(String id);
	/**
	 * 根据账号得到银行账号
	 *
	 * @param bankAcc
	 * @return
	 */
	BtBankAccAppDTO getBtBankAccAppByBankAcc(String bankAcc);
	/**
	 * 根据账号得到银行账号信息
	 *
	 * @param bankAcc
	 * @return
	 */
	BtBankAccDTO getBtBankAccByBankAcc(String bankAcc);
	/**
	 * 显示账号表详情
	 *
	 * @param id
	 * @return
	 */
	BtBankAccDTO getBtBankAccVOById(String id);
	/**
	 * 显示账号申请表详情
	 *
	 * @param id
	 * @return
	 */
	BtBankAccAppDTO getBtBankAccAppVOById(String id);
	/**
	 * 设置币别
	 *
	 * @param id
	 * @return
	 */
	String setCurId(String id, String type);
	/**
	 * 设置币别
	 *
	 * @param output
	 */
	DataTablesOutput setCurName(DataTablesOutput output);
	/**
	 * 判断是否有相同的银行账号
	 *
	 * @param bankAcc
	 * @return
	 */
	boolean isBankAcc(String bankAcc);
	/**
	 * 获取银行账号申请表币别中间表
	 *
	 * @param appId
	 * @param curId
	 * @return
	 */
	BtBankAccAppCurDTO getBtBankAccAppCur(String appId, String curId);
	/**
	 * 获取银行账号表币别中间表
	 *
	 * @param bankAccId
	 * @param curId
	 * @return
	 */
	BtBankAccCurDTO getBtBankAccCur(String bankAccId, String curId);
	/**
	 * 获取有效的父帐号银行信息
	 *
	 * @return
	 */
	List<BtBankAccVO> listPrentBankAcc();
	/**
	 * 判断是否有子账号
	 *
	 * @param prentAccId
	 * @return
	 */
	boolean isChildBankAccByPrentAccId(String prentAccId);
	/**
	 * 显示账号表详情
	 *
	 * @param id
	 * @return
	 */
	BtBankAccDTO getBtBankAccVOById(String id, String curId);
	/**
	 * 显示账号申请表详情
	 *
	 * @param id
	 * @return
	 */
	BtBankAccAppDTO getBtBankAccAppVOById(String id, String curId);
	/**
	 * 获取账户性质
	 *
	 * @param natureName
	 * @return
	 */
	String getNatureId(String natureName);
	/**
	 * 获取账户属性
	 *
	 * @param attributeName
	 * @return
	 */
	String getAttributeId(String attributeName);
	/**
	 * 获取银行账号
	 *
	 * @return
	 */
	List<String> getByBankAccs(Set<String> bankAccs);
	/**
	 * 获取所有的AccNature
	 *
	 * @return
	 */
	List<BtAccNatureDTO> getAllBtAccNature();
	/**
	 * 获取所有账户属性
	 *
	 * @return
	 */
	List<BisAccAttributeDTO> getAllBisAccAttributes();
	Long countEffectiveBtBankAcc();
	/**
	 * 通过单据类型id查询银行账户信息
	 * @param receiptCode
	 * @return
	 */
	List<BtBankAccVO> getBtBankAccVOByReceiptType(String receiptCode);
	/**
	 * 通过账户code查询银行账户信息
	 * @param accGroupCode
	 * @return
	 */
	List<BtBankAccVO> getBtBankAccVOByAccGroupCode(String accGroupCode);
}
