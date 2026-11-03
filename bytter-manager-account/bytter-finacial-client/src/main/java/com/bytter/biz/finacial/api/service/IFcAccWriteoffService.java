package com.bytter.biz.finacial.api.service;
import java.util.Date;
import com.bytter.biz.finacial.api.vo.FcAccWriteoffVO;
/**
* @Title: Service
* @Description: FC_ACC_WRITEOFF
* @author onlineGenerator
* @date 2017-06-12 10:08:33
* @version 1.0
*
*/
public interface IFcAccWriteoffService {
	/**
	 * 添加账户注销记录
	 * @param blnctrAcc
	 */
	public String saveObject(String blnctrAcc,String transferAcc,Date dayOver,String loginName);
	/**
	 * 修改全局id
	 * @param blnctrAcc
	 */
	public FcAccWriteoffVO getFcAccWriteoff(String blnctrAcc);
	/**
	 * 修改全局id
	 * @param blnctrAcc
	 */
	public void updateBusGlobalId(String blnctrAcc,String busGlobalId);
	/**
	 * 修改账户注销记录
	 * @param blnctrAcc
	 */
	public void updateObject(String blnctrAcc,String loginName);
	/**
	 * 通过内部账号删除账户销户明细
	 * @param blnctrAcc
	 * @return
	 */
	public int delWriteOffByBlnctrAcc(String blnctrAcc);
}
