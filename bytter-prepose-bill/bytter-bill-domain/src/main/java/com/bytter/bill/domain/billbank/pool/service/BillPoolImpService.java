package com.bytter.bill.domain.billbank.pool.service;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.pool.dao.BillPoolImpDao;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b>票据池服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-18 <br>
 */
@Service("billPoolImpService")
@Transactional
public class BillPoolImpService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BillPoolImpDao billPoolImpDao;
	
	/**
	 * <b>功能：</b>获取对于银行类型需要的票据池<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据池信息
	 */
	public List<PoolBaseVO> queryPoolByBifCode(String bifCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode);
		}
		return billPoolImpDao.queryPoolByBifCode(bifCode);
	}
	
	/**
	 * <b>功能：</b>获取对于银行类型需要的票据池及票据池单位下的所有票据账号<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2022-03-17 <br>
	 * 
	 * @param bifCode 银行接口代码
	 * @return 票据池及账号信息
	 */
	public List<PoolBaseVO> queryPoolAndReqAccByBifCode(String bifCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode);
		}
		return billPoolImpDao.queryPoolAndReqAccByBifCode(bifCode);
	}
	
	/**
	 * <b>功能：</b>根据票据池代码获取票据池<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * 
	 * @param poolNo
	 *            票据池代码
	 * @param bifCode
	 *            银行接口代码
	 * @return 票据池信息
	 */
	public PoolBaseVO queryPoolByPoolNo(String poolNo,String bifCode){
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+",poolNo="+poolNo);
		}
		if(StrUtil.isNotEmpty(poolNo)||StrUtil.isNotEmpty(bifCode)){
			List<PoolBaseVO> list=billPoolImpDao.queryPoolByPoolNoOrId(poolNo, null, bifCode);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}
	
	/**
	 * <b>功能：</b>根据票据池代码获取票据池<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * 
	 * @param poolNo
	 *            票据池代码
	 * @param bifCode
	 *            银行接口代码
	 * @param poolMap
	 *            查询缓存信息
	 * @return 票据池信息
	 */
	public PoolBaseVO queryPoolByPoolNo(String poolNo,String bifCode,Map<String,PoolBaseVO> poolMap){
		if(StrUtil.isNotEmpty(poolNo)){
			String key=poolNo+"_"+bifCode;
			if(poolMap.containsKey(key)){
				return poolMap.get(key);
			}
			List<PoolBaseVO> list=billPoolImpDao.queryPoolByPoolNoOrId(poolNo, null, bifCode);
			if(list!=null&&list.size()>0){
				poolMap.put(key, list.get(0));
				return list.get(0);
			}
		}
		return null;
	}
	/**
	 * <b>功能：</b>根据票据池Id获取票据池<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * 
	 * @param poolNo
	 *            票据池代码
	 * @return 票据池信息
	 */
	public PoolBaseVO queryPoolById(String id){
		if(StrUtil.isNotEmpty(id)){
			List<PoolBaseVO> list=billPoolImpDao.queryPoolByPoolNoOrId(null,id, null);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}
	
	/**
	 * <b>功能：</b>根据单位名称或者企业机构号获取单位ID<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * 
	 * @param corpName
	 *            单位名称
	 * @param origCode
	 *            企业机构号
	 * @return 票据池信息
	 */
	public String getCorpIdByCorpNameOrOrigNo(String corpName,String origCode) {
		if(StrUtil.isNotEmpty(corpName)||StrUtil.isNotEmpty(origCode)){
			return billPoolImpDao.getCorpIdByCorpNameOrOrigNo(corpName, origCode);
		}
		return null;
	}
	/**
	 * <b>功能：</b>根据业务类型代码获取业务类型ID<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * 
	 * @param busTypeCode
	 *            上级节点业务类型代码
	 * @param busTypeCodeDtl
	 *            明细节点业务类型代码
	 * @return 票据池信息
	 */
	public String getBusTypeIdByBusTypeCode(String busTypeCode,String busTypeCodeDtl) {
		if(StrUtil.isNotEmpty(busTypeCodeDtl)){
			 return billPoolImpDao.getBusTypeIdByBusTypeCode(busTypeCode,busTypeCodeDtl);
		}
		return null;
	}
	
	
	/**
	 * 根据业务类型代码获取业务类型ID
	 * @param corpName 单位名称
	 * @param origCode  企业机构号
	 * @param corpMap 单位缓存map
	 * @return
	 */
	public String getCorpId(String corpName,String origCode,Map<String,String> corpMap){
		if(StrUtil.isNotEmpty(origCode)
				&&corpMap.containsKey(origCode)){
			return corpMap.get(origCode);
		}else if(StrUtil.isNotEmpty(corpName)
				&&corpMap.containsKey(corpName)){
			return corpMap.get(corpName);
		}
		if(StrUtil.isNotEmpty(origCode)){
			String corpId=getCorpIdByCorpNameOrOrigNo(null, origCode);
			if(StrUtil.isNotEmpty(corpId)){
				corpMap.put(origCode, corpId);
				return corpId;
			}
		}
		if(StrUtil.isNotEmpty(corpName)){
			String corpId=getCorpIdByCorpNameOrOrigNo(corpName, null);
			if(StrUtil.isNotEmpty(corpId)){
				corpMap.put(corpName, corpId);
				return corpId;
			}
		}
		return null;
	}
}
