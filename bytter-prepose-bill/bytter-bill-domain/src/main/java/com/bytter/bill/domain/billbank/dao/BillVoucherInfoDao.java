package com.bytter.bill.domain.billbank.dao;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
import com.bytter.core.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings("serial")
@Repository
public class BillVoucherInfoDao extends BaseDao<BillAbVoucherinfo,String> {
	/**
	 * 根据票据号和子票区间号获取票据信息
	 * @param origNo 票号
	 * @param sbbllRngStrtSn 子票区间开始序号
	 * @param sbbllRngEndSn 子票区间结束序号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BillAbVoucherinfo getBillVoucherInfoByOrigNo(String origNo,String sbbllRngStrtSn, String sbbllRngEndSn){
		if(origNo==null){
			return null;
		}
		String hql="from BillAbVoucherinfo where origNo=:origNo";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("origNo", origNo);
		List<String> subRngNoList = NewDraftDealMethod.subTicketIntervalLenLack(sbbllRngStrtSn,sbbllRngEndSn);
		sbbllRngStrtSn = subRngNoList.get(0);
		sbbllRngEndSn = subRngNoList.get(1);
		if(StringUtils.isNotBlank(sbbllRngStrtSn) && !"".equals(sbbllRngStrtSn)){
			hql = hql + " and sbbllRngStrtSn=:sbbllRngStrtSn";
			params.put("sbbllRngStrtSn", sbbllRngStrtSn);
		}
		if(StringUtils.isNotBlank(sbbllRngEndSn) && !"".equals(sbbllRngEndSn)){
			hql = hql + " and sbbllRngEndSn=:sbbllRngEndSn";
			params.put("sbbllRngEndSn", sbbllRngEndSn);
		}
		hql = hql + " and (oldDraftId is null or oldDraftId not like '%-del') order by createDate desc";
		List<BillAbVoucherinfo> list=this.listByHQL(hql, params);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 根据票据主表ID删除票据主信息
	 * @param id
	 * @return
	 */
	public int deleteById(String id){
		if(id==null){
			return 0;
		}
		String hql="delete from BillAbVoucherinfo where id=:id ";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("id", id);
		return this.updateDelByHql(hql, params);
	}
	/**
	 * 根据ID获取票据主信息
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BillAbVoucherinfo getBillAbVoucherinfoById(String id){
		if(StringUtil.isEmpty(id)){
			return null;
		}
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("id", id);
		List<BillAbVoucherinfo> list=this.listByHQL("from BillAbVoucherinfo where id=:id", params);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据票据主信息ID删除增信信息
	 * @param vid
	 */
	public int delAddCreditInfoByVid(String vid){
		if(StringUtil.isBlank_new(vid)){
			return 0;
		}
		String hql="delete from BillAddCreditInfo where vid=:vid ";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("vid", vid);
		return this.updateDelByHql(hql, params);
	}

	/**
	 *
	 */
	public int updateVoucherinfoEndorsementCount(String origNo,String sbbllRngStrtSn, String sbbllRngEndSn,Integer endorsementCount){
		String hql="update BillAbVoucherinfo set endorsementCount=:endorsementCount " +
				"where origNo=:origNo and sbbllRngStrtSn=:sbbllRngStrtSn and sbbllRngEndSn=:sbbllRngEndSn  ";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("origNo", origNo);
		params.put("sbbllRngStrtSn", sbbllRngStrtSn);
		params.put("sbbllRngEndSn", sbbllRngEndSn);
		params.put("endorsementCount", endorsementCount);
		return this.updateDelByHql(hql, params);
	}
}
