package com.bytter.system.api;
import com.bytter.system.api.dto.BtBankAccCurDTO;
import com.bytter.system.api.vo.BtBankAccCurVO;
import java.util.List;
/**
* @Title: Service
* @Description: BT_BANK_ACC_CUR
* @author onlineGenerator
* @date 2017-06-02 18:03:22
* @version 1.0
*
*/
public interface IBtBankAccCurService{

	public void deleteEntity(BtBankAccCurVO entity);
	/**
	* 根据id删除
	* @param id
	*/
	public void delete(String id);
	/**
	* 保存
	* @param entity
	*/
 	public void save(BtBankAccCurVO entity);
	/**
	* 更新
	* @param entity
	*/
	public void update(BtBankAccCurVO entity);
	/**
	* 保存or更新
	* @param entity
	*/
	public void saveOrUpdate(BtBankAccCurVO entity);

	public void batchSaveEntity(List<BtBankAccCurVO> list);
	/**
	 * 根据币种ID获取银行账号ID集合
	 * @param curId
	 * @return
	 */
	public List<String> getBankAccIdsByCurId(String curId);

	/**
	 * 根据账号Id获取 账号币别对应表
	 * @param BankAccId
	 * @return
	 */
	public BtBankAccCurDTO getBankAccCurByBankAccId(String BankAccId, String curId);

	public List<BtBankAccCurDTO> getBankAccCurByBankAccId(String BankAccId);


}
