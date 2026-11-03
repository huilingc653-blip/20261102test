package com.bytter.bill.domain.billbank.pool.dao;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import com.bytter.bill.domain.billbank.dao.BaseDao;
import com.bytter.core.utils.BeanUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b>池信息DAO<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-19 <br>
 */
@SuppressWarnings("rawtypes")
@Repository("billPoolImpDao")
public class BillPoolImpDao extends BaseDao {
	private static final long serialVersionUID = 1L;
	
	/**
	 * <b>功能：</b>获取对于银行类型需要的票据池<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * 
	 * @param bifCode
	 *            银行接口代码
	 * @return 票据池信息
	 */
	@SuppressWarnings("unchecked")
	public List<PoolBaseVO> queryPoolByBifCode(String bifCode) {
		List<PoolBaseVO> list = new ArrayList<PoolBaseVO>();
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append(" select p.id as busId,p.bp_code as poolNo,p.bp_name as poolName,i.bif_code as bifCode,p.bp_mode as bpMode from ab_pool p inner join ab_poolif i on p.pif_id=i.id ");
		sqlselect.append(" where p.valid_sign='1' and i.valid_sign='1' and i.bif_code=:bifCode ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bifCode", bifCode);
		List<Map<String, Object>> mapList = this.listBySQL(sqlselect.toString(), params);
		list = BeanUtils.copyMapListProperties(mapList, PoolBaseVO.class);
		return list;
	}
	
	/**
	 * <b>功能：</b>获取对于银行类型需要的票据池及票据池单位下的所有票据账号<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2022-03-17 <br>
	 * 
	 * @param bifCode 银行接口代码
	 * @return 票据池及账号信息
	 */
	@SuppressWarnings("unchecked")
	public List<PoolBaseVO> queryPoolAndReqAccByBifCode(String bifCode) {
		List<PoolBaseVO> list = new ArrayList<PoolBaseVO>();
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append(" select p.id as busId,p.bp_code as poolNo,p.bp_name as poolName,i.bif_code as bifCode,p.bp_mode as bpMode, bba.bank_acc as reqAcc");
		sqlselect.append(" from ab_pool p  ");
		sqlselect.append(" inner join ab_poolif i on p.pif_id=i.id  ");
		sqlselect.append(" left join BIS_BIF_INIT bbi on i.bif_code = bbi.bif_code  ");
		sqlselect.append(" left join bt_bank_acc bba on bbi.id = bba.bif_code and bba.electric_bill = '1' and bba.circulation_mode = '1' and bba.corp_id in (select pc.corp_id from AB_POOL_CORP pc where pc.valid_sign=1)  ");
		sqlselect.append(" where p.valid_sign='1' and i.valid_sign='1' and i.bif_code=:bifCode ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bifCode", bifCode);
		List<Map<String, Object>> mapList = this.listBySQL(sqlselect.toString(), params);
		list = BeanUtils.copyMapListProperties(mapList, PoolBaseVO.class);
		return list;
	}
	/**
	 * <b>功能：</b>根据票据池代码获取票据池<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * 
	 * @param poolNo
	 *            票据池代码
	 * @param id
	 *            票据ID
	 * @param bifCode
	 *            票据池接口代码
	 * @return 票据池信息
	 */
	@SuppressWarnings("unchecked")
	public List<PoolBaseVO> queryPoolByPoolNoOrId(String poolNo,String id,String bifCode) {
		if(StrUtil.isEmpty(poolNo)&&StrUtil.isEmpty(id)){
			return null;
		}
		List<PoolBaseVO> list = new ArrayList<PoolBaseVO>();
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append(" select p.id as busId,p.bp_code as poolNo,p.bp_name as poolName,i.bif_code as bifCode, p.bp_mode as bpMode,");
		sqlselect.append("pp.bank_acc as pledgeeAcc, ");
		sqlselect.append("pp.acc_name as pledgeeName, ");
		sqlselect.append("pp.bank_no as pledgeeBankNo, ");
		sqlselect.append("pp.bank_name as pledgeeBankName ");
		sqlselect.append(" from ab_pool p ");
		sqlselect.append(" inner join ab_poolif i on p.pif_id=i.id ");
		sqlselect.append(" left join AB_POOL_PLEDGEE pp ");
		sqlselect.append("  on pp.bp_id=p.id ");
		sqlselect.append(" where 1=1 and p.VALID_SIGN=1 and pp.valid_sign=1 ");
		Map<String, Object> params = new HashMap<String, Object>();
		if(StrUtil.isNotEmpty(id)){
			params.put("id", id);
			sqlselect.append(" and p.id=:id ");
		}
		if(StrUtil.isNotEmpty(poolNo)){
			params.put("poolNo", poolNo);
			sqlselect.append(" and p.bp_code=:poolNo ");
		}
		if(StrUtil.isNotEmpty(bifCode)){
			params.put("bifCode", bifCode);
			sqlselect.append(" and i.bif_code=:bifCode ");
		}
		List<Map<String, Object>> mapList = this.listBySQL(sqlselect.toString(), params);
		list = BeanUtils.copyMapListProperties(mapList, PoolBaseVO.class);
		return list;
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
	@SuppressWarnings("unchecked")
	public String getCorpIdByCorpNameOrOrigNo(String corpName,String origCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<PoolBaseVO> list = new ArrayList<PoolBaseVO>();
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append(" select pc.corp_id as corpId,pc.corp_name as corpName,ps.orig_code as bankOrgCode,ps.orig_name as bankOrgName from ab_pool_corp pc  ");
		sqlselect.append("left join ab_pool_sign_info ps on ps.corp_id=pc.corp_id  ");
		sqlselect.append("where 1=1 ");
		if(StrUtil.isNotEmpty(corpName)&&StrUtil.isNotEmpty(origCode)){
			params.put("corpName", corpName);
			params.put("origCode", origCode);
			sqlselect.append("and (ps.orig_name=:corpName or pc.corp_name=:corpName or ps.orig_code=:origCode) ");
		}else{
			if(StrUtil.isNotEmpty(corpName)){
				params.put("corpName", corpName);
				sqlselect.append("and (ps.orig_name=:corpName or pc.corp_name=:corpName) ");
			}
			if(StrUtil.isNotEmpty(origCode)){
				params.put("origCode", origCode);
				sqlselect.append("and ps.orig_code=:origCode ");
			}
		}
		// sqlselect.append("order by bt_nvl(pc.update_time,pc.create_time) desc ");
		sqlselect.append("order by pc.update_time desc,pc.create_time desc ");
		List<Map<String, Object>> mapList = this.listBySQL(sqlselect.toString(), params);
		list = BeanUtils.copyMapListProperties(mapList, PoolBaseVO.class);
		if(list!=null&&list.size()>0){
			return list.get(0).getCorpId();
		}
		return null;
	}
	/**
	 * <b>功能：</b>根据业务类型代码获取业务类型ID<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * 
	 * @param busTypeCode
	 *            业务类型代码
	 * @param busTypeCodeDtl
	 *            企业机构号
	 * @return 票据池信息
	 */
	@SuppressWarnings("unchecked")
	public String getBusTypeIdByBusTypeCode(String busTypeCode,String busTypeCodeDtl) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<PoolBaseVO> list = new ArrayList<PoolBaseVO>();
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append(" select b.id as busId,b.bustype_code as bankOrgCode from ab_pool_bustype b ");
		sqlselect.append("left join ab_pool_bustype fb on b.parent_id=fb.id ");
		sqlselect.append("left join ab_pool_bustype sb on sb.parent_id=b.id and sb.valid_sign = b.valid_sign ");
		sqlselect.append("where sb.id is null and b.valid_sign='1' ");
		if(StrUtil.isNotEmpty(busTypeCode)&&StrUtil.isNotEmpty(busTypeCodeDtl)){
			params.put("busTypeCode", busTypeCode);
			params.put("busTypeCodeDtl", busTypeCodeDtl);
			sqlselect.append("and b.bustype_code=:busTypeCodeDtl and fb.bustype_code=:busTypeCode and fb.valid_sign='1' ");
		}
		if(StrUtil.isEmpty(busTypeCode)&&StrUtil.isNotEmpty(busTypeCodeDtl)){
			params.put("busTypeCodeDtl", busTypeCodeDtl);
			sqlselect.append("and b.bustype_code=:busTypeCodeDtl and b.parent_id is null ");
		}
//		sqlselect.append("order by bt_nvl(pc.update_time,pc.create_time) desc ");
		List<Map<String, Object>> mapList = this.listBySQL(sqlselect.toString(), params);
		list = BeanUtils.copyMapListProperties(mapList, PoolBaseVO.class);
		if(list!=null&&list.size()>0){
			return list.get(0).getBusId();
		}
		return null;
	}
	
}
