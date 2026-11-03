package com.bytter.biz.ttpay.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.bytter.biz.ttpay.service.TpPayBillDtlService;
import com.bytter.biz.ttpay.vo.TpPayBillViewDtlVO;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import oracle.sql.TRANSDUMP;
public class TtPayUtil {
	
	/**
	 * 缴款方式和表、字段对应信息缓存
	 */
	public static final Map<String,Map<String,List<TpPayBillViewDtlVO>>> PAYTYPETABLEINFO=new Hashtable<>();
	
	/**
	 * 缴款方式和sql语句信息缓存
	 */
	public static final Map<String,Map<String,String>> PAYTYPESQLINFO=new Hashtable<>();
	
	/**
	 * 缴款方式和voAttr缓存对应
	 */
	public static final Map<String,Map<String,TpPayBillViewDtlVO>> PAYTYPEATTRINFO=new Hashtable<>();
	
	/** 列表字段信息 **/
	public static final String TABLEFIELD_LIST="tableListField";
	
	/** 查看明细字段信息 **/
	public static final String TABLEFIELD_VIEW="tableViewField";
	
	/** 合计字段信息 **/
	public static final String TABLEFIELD_SUM="tableSumField";
	
	/** 公共列明对应VO字段信息 **/
	public static final String VOATTRFIELD="voAttrField";
	
	/** 对应交易日期 **/
	public static final String TRANDTIME="trandTime";
	
	/** 付款方式 **/
	public static final String PAYTYPE="payType";
	/** 应用id **/
	public static final String APPID="appId";
	/** 商户号 **/
	public static final String MCHID="mchId";
	
	/** 对应查询的sql语句  **/
	public static final String SQL="SQL";
	
	public static final String[] ALLVOATTR={"payType","appId","mchId","serialNo","trandNo","tradeType","payAcc","trandTime","trandMoney"};
	
	/**
	 * 通过全部表和字段对应信息初始化缓存
	 */
	public static void initPaytypeTableinfo(){
		List<TpPayBillViewDtlVO> list=((TpPayBillDtlService)ApplicationContextUtil.getContext().getBean("tpPayBillDtlService")).getTpMerchantVOList();
		if(list!=null&&list.size()>0){
		PAYTYPETABLEINFO.clear();
		Map<String,List<TpPayBillViewDtlVO>> payTypeMap=null;
		for(TpPayBillViewDtlVO info:list){
			//设置不同支付方式MAP
			payTypeMap=PAYTYPETABLEINFO.get(info.getPayType());
			if(payTypeMap==null){
				payTypeMap=new HashMap<>();
				PAYTYPETABLEINFO.put(info.getPayType(), payTypeMap);
			}
			
//			//设置此支付方式的表和字段信息
//			List<TpPayBillViewDtl> tableField=payTypeMap.get(TABLEFIELD);
//			if(tableField==null){
//				tableField=new ArrayList<>();
//				payTypeMap.put(TABLEFIELD, tableField);
//			}
//			tableField.add(info);
			String fieldUse=TtPayConstant.TTPAY_SEPARATORCHARS+info.getFieldUse()+TtPayConstant.TTPAY_SEPARATORCHARS;
			//列表字段信息
			if(fieldUse.indexOf((TtPayConstant.TTPAY_SEPARATORCHARS+TtPayConstant.TTPAY_BILLVIEWDTL_FIELDTYPE1+TtPayConstant.TTPAY_SEPARATORCHARS))!=-1){
				List<TpPayBillViewDtlVO> tablefield_list=payTypeMap.get(TABLEFIELD_LIST);
				if(tablefield_list==null){
					tablefield_list=new ArrayList<>();
					payTypeMap.put(TABLEFIELD_LIST, tablefield_list);
				}
				tablefield_list.add(info);
			}
			//查看明细字段信息 
			if(fieldUse.indexOf((TtPayConstant.TTPAY_SEPARATORCHARS+TtPayConstant.TTPAY_BILLVIEWDTL_FIELDTYPE2+TtPayConstant.TTPAY_SEPARATORCHARS))!=-1){
				List<TpPayBillViewDtlVO> tablefield_view=payTypeMap.get(TABLEFIELD_VIEW);
				if(tablefield_view==null){
					tablefield_view=new ArrayList<>();
					payTypeMap.put(TABLEFIELD_VIEW, tablefield_view);
				}
				tablefield_view.add(info);
			}
			//合计字段信息
			if(fieldUse.indexOf((TtPayConstant.TTPAY_SEPARATORCHARS+TtPayConstant.TTPAY_BILLVIEWDTL_FIELDTYPE3+TtPayConstant.TTPAY_SEPARATORCHARS))!=-1){
				List<TpPayBillViewDtlVO> tablefield_sum=payTypeMap.get(TABLEFIELD_SUM);
				if(tablefield_sum==null){
					tablefield_sum=new ArrayList<>();
					payTypeMap.put(TABLEFIELD_SUM, tablefield_sum);
				}
				tablefield_sum.add(info);
			}
			
			if(StringUtils.isNotEmpty(info.getVoAttr())){
				if(TRANDTIME.equals(info.getVoAttr())){
					payTypeMap.put(TRANDTIME, new ArrayList<TpPayBillViewDtlVO>());
					payTypeMap.get(TRANDTIME).add(info);
				}
				//设置此支付方式的公共VO和字段信息
				if(fieldUse.indexOf((TtPayConstant.TTPAY_SEPARATORCHARS+TtPayConstant.TTPAY_BILLVIEWDTL_FIELDTYPE0+TtPayConstant.TTPAY_SEPARATORCHARS))!=-1){
					Map<String,TpPayBillViewDtlVO> voAttrField=PAYTYPEATTRINFO.get(info.getPayType());
					if(voAttrField==null){
						voAttrField=new HashMap<>();
						PAYTYPEATTRINFO.put(info.getPayType(), voAttrField);
					}
					voAttrField.put(info.getVoAttr(), info);
				}
			}
		}
		}
	}
	
	/**
	 * 通过支付方式获取对应的VO字段对照信息
	 * @param payType
	 * @return
	 */
	public static List<TpPayBillViewDtlVO> getVoAttrField(String payType){
		if(TtPayUtil.PAYTYPETABLEINFO.isEmpty()){
			initPaytypeTableinfo();
		}
		Map<String,List<TpPayBillViewDtlVO>> payTypeMap=TtPayUtil.PAYTYPETABLEINFO.get(payType);
		if(payTypeMap!=null){
			return payTypeMap.get(VOATTRFIELD);
		}
		return null;
	}
	
	/**
	 * 通过支付方式获取列表字段信息
	 * @param payType
	 * @return
	 */
	public static List<TpPayBillViewDtlVO> getTableListField(String payType){
		if(TtPayUtil.PAYTYPETABLEINFO.isEmpty()){
			initPaytypeTableinfo();
		}
		Map<String,List<TpPayBillViewDtlVO>> payTypeMap=TtPayUtil.PAYTYPETABLEINFO.get(payType);
		if(payTypeMap!=null){
			return payTypeMap.get(TABLEFIELD_LIST);
		}
		return null;
	}
	
	/**
	 * 通过支付方式获取查看明细字段信息
	 * @param payType
	 * @return
	 */
	public static List<TpPayBillViewDtlVO> getTableViewField(String payType){
		if(TtPayUtil.PAYTYPETABLEINFO.isEmpty()){
			initPaytypeTableinfo();
		}
		Map<String,List<TpPayBillViewDtlVO>> payTypeMap=TtPayUtil.PAYTYPETABLEINFO.get(payType);
		if(payTypeMap!=null){
			return payTypeMap.get(TABLEFIELD_VIEW);
		}
		return null;
	}
	
	/**
	 * 通过支付方式获取合计字段信息
	 * @param payType
	 * @return
	 */
	public static List<TpPayBillViewDtlVO> getTableSumField(String payType){
		if(TtPayUtil.PAYTYPETABLEINFO.isEmpty()){
			initPaytypeTableinfo();
		}
		Map<String,List<TpPayBillViewDtlVO>> payTypeMap=TtPayUtil.PAYTYPETABLEINFO.get(payType);
		if(payTypeMap!=null){
			return payTypeMap.get(TABLEFIELD_SUM);
		}
		return null;
	}
	
	
	/**
	 * 通过支付方式获取列表字段信息sql
	 * @param payType
	 * @return
	 */
	public static String getTableListFieldSql(String payType){
		List<TpPayBillViewDtlVO> fieldList=getTableListField(payType);
		if(fieldList==null||fieldList.size()<1){
			return null;
		}
		if(!PAYTYPESQLINFO.containsKey(payType)
				||!PAYTYPESQLINFO.get(payType).containsKey(TABLEFIELD_LIST+SQL)){
			StringBuffer sql=new StringBuffer("select ");
			for(int i=0;i<fieldList.size();i++){
				if(i>0){
					sql.append(",");
				}
				TpPayBillViewDtlVO info=fieldList.get(i);
				sql.append(info.getFieldCode());
			}
			sql.append(" from ").append(fieldList.get(0).getTableCode());
			sql.append(" where 1=1 ");
			if(TtPayUtil.PAYTYPETABLEINFO.get(payType).containsKey(TRANDTIME)){
				String trandTime=TtPayUtil.PAYTYPETABLEINFO.get(payType).get(TRANDTIME).get(0).getFieldCode();
				sql.append(" and ").append(trandTime).append(">=:beginTransTime ");
				sql.append(" and ").append(trandTime).append("<:endTransTime ");
			}
			PAYTYPESQLINFO.put(payType, new HashMap<String,String>());
			PAYTYPESQLINFO.get(payType).put(TABLEFIELD_LIST+SQL, sql.toString());
		}
		return PAYTYPESQLINFO.get(payType).get(TABLEFIELD_LIST+SQL);
	}
	
	/**
	 * 通过支付方式获取查看明细字段信息sql
	 * @param payType
	 * @return
	 */
	public static String getTableViewFieldSql(String payType){
		List<TpPayBillViewDtlVO> fieldList=getTableViewField(payType);
		if(fieldList==null||fieldList.size()<1){
			return null;
		}
		if(!PAYTYPESQLINFO.containsKey(payType)
				||!PAYTYPESQLINFO.get(payType).containsKey(TABLEFIELD_VIEW+SQL)){
			StringBuffer sql=new StringBuffer("select ");
			for(int i=0;i<fieldList.size();i++){
				if(i>0){
					sql.append(",");
				}
				TpPayBillViewDtlVO info=fieldList.get(i);
				sql.append(info.getFieldCode());
			}
			sql.append(" from ").append(fieldList.get(0).getTableCode());
			sql.append(" where 1=1 ");
			PAYTYPESQLINFO.put(payType, new HashMap<String,String>());
			PAYTYPESQLINFO.get(payType).put(TABLEFIELD_VIEW+SQL, sql.toString());
		}
		return PAYTYPESQLINFO.get(payType).get(TABLEFIELD_VIEW+SQL);
	}
	
	/**
	 * 通过支付方式获取合计字段信息sql
	 * @param payType
	 * @return
	 */
	public static String getTableSumFieldSql(String payType){
		List<TpPayBillViewDtlVO> fieldList=getTableSumField(payType);
		if(fieldList==null||fieldList.size()<1){
			return null;
		}
		if(!PAYTYPESQLINFO.containsKey(payType)
				||!PAYTYPESQLINFO.get(payType).containsKey(TABLEFIELD_SUM+SQL)){
			StringBuffer sql=new StringBuffer("select*from(select ");
			for(int i=0;i<fieldList.size();i++){
				if(i>0){
					sql.append(",");
				}
				TpPayBillViewDtlVO info=fieldList.get(i);
				sql.append(info.getFieldCode());
				if(StringUtils.isNotEmpty(info.getVoAttr())){
					sql.append(" as ").append(info.getVoAttr());
				}
			}
			sql.append(" from ").append(fieldList.get(0).getTableCode());
			sql.append(" where 1=1 ");
			if(TtPayUtil.PAYTYPETABLEINFO.get(payType).containsKey(TRANDTIME)){
				String trandTime=TtPayUtil.PAYTYPETABLEINFO.get(payType).get(TRANDTIME).get(0).getFieldCode();
				sql.append(" and ").append(trandTime).append(">=:beginTransTime ");
				sql.append(" and ").append(trandTime).append("<:endTransTime ");
			}
			sql.append(") ");
			PAYTYPESQLINFO.put(payType, new HashMap<String,String>());
			PAYTYPESQLINFO.get(payType).put(TABLEFIELD_SUM+SQL, sql.toString());
		}
		return PAYTYPESQLINFO.get(payType).get(TABLEFIELD_SUM+SQL);
	}
	
	/**
	 * 获取所有第三方支付的公共列表sql
	 * @return
	 */
	public static String getAllFieldSql(){
		String payType="all";
		if(TtPayUtil.PAYTYPETABLEINFO.isEmpty()){
			initPaytypeTableinfo();
		}
		if(TtPayUtil.PAYTYPETABLEINFO.isEmpty()){
			return null;
		}
		if(!PAYTYPESQLINFO.containsKey(payType)
				||!PAYTYPESQLINFO.get(payType).containsKey(VOATTRFIELD+SQL)){
			StringBuffer sql=new StringBuffer();
			for(Entry<String, Map<String, TpPayBillViewDtlVO>> entry:TtPayUtil.PAYTYPEATTRINFO.entrySet()){
				Map<String, TpPayBillViewDtlVO> value=entry.getValue();
				if(sql.length()>0){
					sql.append(" union all ");
				}
				StringBuffer sqlSon=new StringBuffer("select id");
				String tableCode="";
				for(String voAttr:ALLVOATTR){
					sqlSon.append(",");
					if(PAYTYPE.equals(voAttr)){
						sqlSon.append(" '"+entry.getKey()+"' ").append(" as ").append(voAttr);
					}else if(value.containsKey(voAttr)){
						sqlSon.append(" ").append(value.get(voAttr).getFieldCode()).append(" ").append(" as ").append(voAttr);
						if(StringUtils.isEmpty(tableCode)){
							tableCode=value.get(voAttr).getTableCode();
						}
					}else{
						if ("trandMoney".equals(voAttr)) {
							// 支付宝明细字段（微信明细字段还未更改）
							sqlSon.append(" CASE WHEN PAYEE_MONEY = 0 THEN PAYER_MONEY ELSE PAYEE_MONEY END AS trandMoney ");
						} else {
							sqlSon.append(" null ").append(" as ").append(voAttr);
						}
					}
				}
				if(StringUtils.isNotEmpty(tableCode)){
					sqlSon.append(" from ").append(tableCode);
					if(sql.length()==0){
						sql.append("( ");
					}
					sql.append(sqlSon);
				}
			}
			if(sql.length()>0){
				sql.append(")");
			}
			PAYTYPESQLINFO.put(payType, new HashMap<String,String>());
			PAYTYPESQLINFO.get(payType).put(VOATTRFIELD+SQL, sql.toString());
		}
		return PAYTYPESQLINFO.get(payType).get(VOATTRFIELD+SQL);
	}
}
