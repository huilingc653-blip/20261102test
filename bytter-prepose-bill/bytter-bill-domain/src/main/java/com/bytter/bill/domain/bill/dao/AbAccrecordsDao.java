package com.bytter.bill.domain.bill.dao;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.bill.api.bill.dto.AbAccrecordsDTO;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
* @Title: Dao
* @Description: 票据流水信息
* @author onlineGenerator
* @date 2017-06-21 15:36:09
* @version 1.0
*
*/
@Repository
public class AbAccrecordsDao extends BaseDao<AbAccrecords,String>{
	/**
	 * 根据map修改数据信息
	 * @param params
	 * @return
	 */
	public int batchUpdate(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update AbAccrecords "
				+ " set updateDate = :updateDate ");
		if(!params.containsKey("updateDate")){
			params.put("updateDate",new Date());
		}
		if(params.containsKey("insideEndorseAutoRegSign")){
			hql.append(",insideEndorseAutoRegSign = :insideEndorseAutoRegSign ");
		}
		if(params.containsKey("msgId")){
			hql.append(",msgId = :msgId ");
		}
		if(params.containsKey("createBy")){
			hql.append(",createBy = :createBy  ");
		}
		if(params.containsKey("createName")){
			hql.append(",createName = :createName ");
		}
		if(params.containsKey("updateBy")){
			hql.append(",updateBy = :updateBy,createBy = (case when createBy is null then :updateBy else createBy end) ");
		}
		if(params.containsKey("updateName")){
			hql.append(",updateName = :updateName,createName = (case when createName is null then :updateName else createName end) ");
		}
		if(params.containsKey("statusNew")){
			hql.append(",status = (case when briefId=:briefId001 and "+
					AccebillConstant.AB_BILL_BUS_STATUS_2 +"=:statusNew  then "+
					AccebillConstant.AB_BILL_BUS_STATUS_1 +" else :statusNew end) ");
			params.put("briefId001", AccebillConstant.BRIEF_STATUS_001);
			params.put("statusNew", (Integer)params.get("statusNew"));

//			hql.append(",status = (case when briefId=:briefId001 and "+
//					AccebillConstant.AB_BILL_BUS_STATUS_2 +"=:statusNew  then "+
//					AccebillConstant.AB_BILL_BUS_STATUS_1 +" else :statusNew end) ");
//			params.put("briefId001", AccebillConstant.BRIEF_STATUS_001);
//			params.put("status001", AccebillConstant.AB_BILL_BUS_STATUS_2);
//			params.put("status001Val", AccebillConstant.AB_BILL_BUS_STATUS_1);
		}
		if(params.containsKey("optStatusNew")){
			hql.append(",optStatus = :optStatusNew ");
		}
		if(params.containsKey("returnMsg")){
			hql.append(",returnMsg = :returnMsg ");
		}
		if(params.containsKey("returnCode")){
			hql.append(",returnCode = :returnCode ");
		}
		if(params.containsKey("validSign")){
			hql.append(",validSign = :validSign ");
		}
		if(params.containsKey("validSignNew")){
			hql.append(",validSign = :validSignNew ");
		}
		if(params.containsKey("sendSignNew")){
			hql.append(",sendSign = :sendSignNew ");
		}
		if(params.containsKey("cdtRatgs")){
			hql.append(",cdtRatgs = :cdtRatgs ");
		}
		if(params.containsKey("busGlobalId")){
			if("null".equals(params.get("busGlobalId"))){
				hql.append(",busGlobalId = null ");
			}else{
				hql.append(",busGlobalId = :busGlobalId ");
			}
		}
		if(params.containsKey("splitFlg")){
			hql.append(",splitFlg = :splitFlg ");
		}
		if(params.containsKey("copyFlag")){
			hql.append(",copyFlag = :copyFlag ");
		}
		if(params.containsKey("copyAccIdNew")){
			hql.append(",copyAccId = :copyAccIdNew ");
		}
		if(params.containsKey("copyAccIdIsNull")){
			hql.append(",copyAccId = null ");
		}
		if(params.containsKey("oldAccIdNew")){
			hql.append(",oldAccId = :oldAccIdNew ");
		}
		if(params.containsKey("oldAccIdIsNull")){
			hql.append(",oldAccId = null ");
		}
		if(params.containsKey("excId")){
			hql.append(",excId = :excId ");
		}
		if(params.containsKey("rmk")){
			hql.append(",rmk = :rmk ");
		}
		hql.append(" where ").append(SqlUtil.getInSqlByParams("ids","id",params));
		if(params.containsKey("statusOld")){
			hql.append(" and status=:statusOld ");
		}
		if(params.containsKey("statusNew")){
			hql.append(" and status != (case when briefId=:briefId001W and "
					+AccebillConstant.AB_BILL_BUS_STATUS_2+"=:statusNew  then "+
					AccebillConstant.AB_BILL_BUS_STATUS_1+" else :statusNew end) ");
			params.put("briefId001W", AccebillConstant.BRIEF_STATUS_001);
			params.put("statusNew", (Integer)params.get("statusNew"));
//			params.put("status001W", AccebillConstant.AB_BILL_BUS_STATUS_2);
//			params.put("status001ValW", AccebillConstant.AB_BILL_BUS_STATUS_1);
		}
		if(params.containsKey("validSign")){
			hql.append(" and (validSign!=:validSign or validSign is null) ");
		}
		if(params.containsKey("optStatusNew")){
			if(params.get("optStatusNew").toString().equals(" ")){
				hql.append(" and (optStatus!='null' or optStatus is null) ");
			}else {
				hql.append(" and (optStatus!=:optStatusNew or optStatus is null) ");
			}
		}
		if(params.containsKey("optStatusOld")){
			hql.append(" and optStatus=:optStatusOld ");
		}
		if(params.containsKey("copyAccIdWIsNull")){
			hql.append(" and copyAccId is null ");
		}
        if(params.containsKey("copyAccIdOld")){
            hql.append(" and copyAccId = :copyAccIdOld ");
        }
		if(params.containsKey("oldAccIdOld")){
			hql.append(" and oldAccId = :oldAccIdOld ");
		}
		if(params.containsKey("updateDateOld")){
			hql.append(" and updateDate = :updateDateOld ");
		}
//		if(params.containsKey("busGlobalId")){
//			if(!"null".equals(params.get("busGlobalId"))){
//				hql.append(" and busGlobalId is null ");
//			}
//		}
		int size=this.batchExecute(hql.toString(), params);
		this.flush();
		return size;
	}
	/**
	 * 开票登记删除
	 * @param params
	 * @return
	 */
	public int batchDeleteByBriefIdIs001(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update AbAccrecords "
				+ " set updateDate = :updateDate ");
		if(params.containsKey("updateBy")){
			hql.append(",updateBy = :updateBy ");
		}
		if(params.containsKey("updateName")){
			hql.append(",updateName = :updateName ");
		}
		hql.append(",status = :statusVal,excId=(case when excId = '3' then '2' end) ");
		hql.append(" where ").append(SqlUtil.getInSqlByParams("ids","id",params) );
		hql.append(" and status  <> :statusVal");
		params.put("statusVal", AccebillConstant.AB_BILL_BUS_STATUS_2);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 根据lastId修改状态
	 * @param params
	 * @return
	 */
	public int updateStatusByLastId(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update AbAccrecords "
				+ " set updateDate = :updateDate ");
		if(params.containsKey("updateBy")){
			hql.append(",updateBy = :updateBy ");
		}
		if(params.containsKey("updateName")){
			hql.append(",updateName = :updateName ");
		}
		hql.append(",status = :status ");
		hql.append(" where lastid in (:lastId) ");
		hql.append(" and status  <> :status");
		hql.append(" and briefId =:briefId");
		params.put("briefId",AccebillConstant.BRIEF_STATUS_6001);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 根据Id修改状态
	 * @param params
	 * @return
	 */
	public int updateStatusById(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update AbAccrecords "
				+ " set updateDate = :updateDate ");
		if(params.containsKey("updateBy")){
			hql.append(",updateBy = :updateBy ");
		}
		if(params.containsKey("updateName")){
			hql.append(",updateName = :updateName ");
		}
		hql.append(",status = :status ");
		hql.append(" where id = :id ");
		hql.append(" and status  <> :status");
		hql.append(" and briefId =:briefId");
		params.put("briefId",AccebillConstant.BRIEF_STATUS_6001);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 兑付跟踪，确认已付款
	 * @param params
	 * @return
	 */
	public int batchUpdateStatusByTrack(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update AbAccrecords "
				+ " set updateDate = :updateDate ");
		if(params.containsKey("updateBy")){
			hql.append(",updateBy = :updateBy ");
		}
		if(params.containsKey("updateName")){
			hql.append(",updateName = :updateName ");
		}
		hql.append(",status = :statusNew ");
		hql.append(" where ").append(SqlUtil.getInSqlByParams("ids","id",params));
		hql.append(" and status  = :statusOld ");
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 更新申请日期及业务办理日期
	 * @param params
	 * @return
	 */
	public int updateApplyDateById(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update AbAccrecords "
				+ " set updateDate = :updateDate ");
		if(params.containsKey("updateBy")){
			hql.append(",updateBy = :updateBy ");
		}
		if(params.containsKey("updateName")){
			hql.append(",updateName = :updateName ");
		}
		hql.append(",applyDate = :applyDate ");
		hql.append(",businessDate = :businessDate ");
		hql.append(" where id = :id ");
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 根据IDS获取流水表对象信息
	 * @param accrecordsVo
	 * @return
	 */
	public List<AbAccrecords> listAbAccrecordsByIds(AbAccrecordsVO accrecordsVo){
		if(accrecordsVo==null||accrecordsVo.getIdArray()==null||StringUtils.isEmpty(accrecordsVo.getIdArray())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		Map<String,Object> params=new HashMap<>();
		params.put("ids", accrecordsVo.getIdArray());
//		String hql="from AbAccrecords where id in(:ids)";
		String hql="from AbAccrecords where "+SqlUtil.getInSqlByParams("ids","id",params);
		if(accrecordsVo!=null){
			if(StringUtils.isNotEmpty(accrecordsVo.getValidSign())){
				hql+=" and validSign=:validSign ";
				params.put("validSign", accrecordsVo.getValidSign());
			}
		}
		return this.listByHQL(hql, params);
	}
	/**
	 * 根据lastIds设置有效标志为1(有效)的数据设置为2（结束）
	 * @return
	 */
	public int setAbAccrecordsValidSign2ByLastIdAndValidSign1(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update AbAccrecords "
				+ " set validSign = :validSignVal ");
		params.put("validSignVal", AccebillConstant.AB_BILL_VALID_SIGN_2);
		if(params.containsKey("updateBy")){
			hql.append(",updateBy = :updateBy ");
		}
		if(params.containsKey("updateName")){
			hql.append(",updateName = :updateName ");
		}
		hql.append(",updateDate = :updateDate "
				+ "where validSign = :validSignW "
				+ " and " + SqlUtil.getInSqlByParams("lastIds","lastid",params) +" and optStatus=:optStatus and briefId!=:recBillBriefId ");
		params.put("validSignW", AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
		params.put("recBillBriefId", AccebillConstant.BRIEF_STATUS_6001);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 根据签收表id获取流水信息
	 * @param recId
	 * @return
	 */
	public List<AbAccrecords> listAbAccrecordsByRecId(String recId){
		Map<String,Object> params=new HashMap<>();
//		String hql="from AbAccrecords where recId=:recId and status not in (:statusNotIn)";
		String hql="from AbAccrecords where recId=:recId order by createDate desc ";
//		params.put("statusNotIn",new Integer[]{AccebillConstant.AB_BILL_BUS_STATUS_1,AccebillConstant.AB_BILL_BUS_STATUS_2});
		params.put("recId",recId);
		return this.listByHQL(hql, params);
	}
	/**
	 * 根据lastid获取流水信息
	 * @param lastid
	 * @return List<AbAccrecords>
	 */
	public List<AbAccrecords> getAbAccrecordsbyLastid(String lastid){
		Map<String,Object> params=new HashMap<>();
		String hql="from AbAccrecords where lastid=:lastid order by createDate desc";
		params.put("lastid",lastid);
		return this.listByHQL(hql, params);
	}
	/**
	 * 根据id或者pdId获取流水信息,创建时间升序
	 * @param id
	 * @param pdId 票据池明细id
	 * @return List<AbAccrecords>
	 */
	public List<AbAccrecordsVO> getAbAccrecordsbyIdOrPdId(String id,String pdId){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder("select id,briefId,createDate,inoptStatus,returnMsg,returnCode from( ");
		StringBuilder select=new StringBuilder("select a.id,a.BRIEF_ID as briefId,a.CREATE_DATE as createDate,a.RETURN_CODE as returnCode,a.RETURN_MSG as returnMsg,a.OPT_STATUS as inoptStatus  from ab_accrecords a");
		if(StringUtils.isNotEmpty(id)){
			sql.append(select);
			sql.append(" where id = :id");
			params.put("id",id);
		}
		if(StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(pdId)){
			sql.append(" union ");
		}
		if(StringUtils.isNotEmpty(pdId)){
			sql.append(select);
			sql.append(" where pd_id = :pdId");
			params.put("pdId",pdId);
		}
		sql.append(" ) info order by CREATEDATE asc");
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}
	/**
	 * 根据lastid获取流水信息实体
	 * @param lastid
	 * @return AbAccrecords
	 */
	public AbAccrecords getAbAccrebyLastid(String lastid){
		Map<String,Object> params=new HashMap<>();
		String sql="select * from AB_ACCRECORDS where lastid=:lastid and status<>:status ";
		params.put("lastid",lastid);
		params.put("status",AccebillConstant.AB_BILL_BUS_STATUS_2);
		return getBySQLAliasToBean(sql, params, AbAccrecords.class);
	}
	/**
	 * 根据params获取流水信息
	 * @param params 查询参数
	 * @return List<AbAccrecords>
	 */
	public List<AbAccrecords> getAbAccrecordsbyParams(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("from AbAccrecords where 1=1 ");
		if(params.containsKey("ids")){
			hql.append("and ").append(SqlUtil.getInSqlByParams("ids","id",params));
		}
		if(params.containsKey("trid")){
			hql.append("and trid=:trid ");
		}
		return this.listByHQL(hql.toString(), params);
	}
	/**
	 * 获取需要设置余额的电票业务
	 * @return
	 */
	public List<AbAccrecordsVO> getElectBusBillInfoByBal(){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder();
		sql.append("select a.id as id ");
		sql.append("From ab_voucherinfo v ");
		sql.append("left join ab_accrecords a on v.id=a.vid ");
		sql.append("where a.status=:statusVal ");
		params.put("statusVal", AccebillConstant.AB_BILL_BUS_STATUS95);
		sql.append("and a.opt_status in (:opt_statusVal) ");
		params.put("opt_statusVal", new String[]{
				AccebillConstant.AB_BILL_OPT_STATUS19,
				AccebillConstant.AB_BILL_OPT_STATUS_3,
				AccebillConstant.AB_BILL_OPT_STATUS_4});
		sql.append("and v.bill_flag=:bill_flag ");
		params.put("bill_flag", AccebillConstant.AB_BILL_FLAG_ELECT);
		sql.append("and a.send_sign=:send_sign ");
		params.put("send_sign", AccebillConstant.AB_BILL_SEND_SIGN_Y);
		//B20210800694 针对票据池出池生成解除质押申请流水不需要做余额变化 zhengg 20210819
		sql.append("and (a.brief_id in (:brief_id1) or (a.brief_id in (:brief_id2) and a.pd_id is null) ) ");
		params.put("brief_id1", new String[]{
				AccebillConstant.BRIEF_STATUS_9003,
				AccebillConstant.BRIEF_STATUS_010,
				AccebillConstant.BRIEF_STATUS_9010,
				AccebillConstant.BRIEF_STATUS_011,
				AccebillConstant.BRIEF_STATUS_9012,
				AccebillConstant.BRIEF_STATUS_018,
				AccebillConstant.BRIEF_STATUS_9018,
				AccebillConstant.BRIEF_STATUS_9019,
				AccebillConstant.BRIEF_STATUS_020,
				AccebillConstant.BRIEF_STATUS_021,
				AccebillConstant.BRIEF_STATUS_9023});
		params.put("brief_id2", new String[]{AccebillConstant.BRIEF_STATUS_019});
//		sql.append(" ");
//		params.put("brief_id2", AccebillConstant.BRIEF_STATUS_018);
//		params.put("opt_status", new String[]{
//				AccebillConstant.AB_BILL_OPT_STATUS_3,
//				AccebillConstant.AB_BILL_OPT_STATUS_4});
//		sql.append(") ");
		sql.append("and a.cdt_ratgs is null ");
		sql.append("order by a.create_date ");
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}
	/**
	 * 设置质押未解除已到期截止票据业务
	 * @return 票据流水明细VO
	 */
	public List<AbAccrecordsVO> getPledgeBusBillInfoByBal(){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder();
		sql.append("select a.id as id ");
		sql.append("From ab_voucherinfo v ");
		sql.append("left join ab_accrecords a on v.id=a.vid ");
		sql.append("where a.status=:status ");
		params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
		sql.append("and (a.brief_id in (:brief_id1) or (a.brief_id in (:brief_id2) and a.bill_source in (:bill_source) )"
				+ ") ");
		params.put("brief_id1", new String[]{AccebillConstant.BRIEF_STATUS_018,AccebillConstant.BRIEF_STATUS_9018});
		params.put("brief_id2", new String[]{AccebillConstant.BRIEF_STATUS_6001});
		params.put("bill_source", new String[]{AccebillConstant.AB_BILL_SOURCE_PLEDGE});
		sql.append("and a.opt_status=:opt_status ");
		params.put("opt_status", AccebillConstant.AB_BILL_OPT_STATUS19);
		sql.append("and a.valid_sign=:valid_sign ");
		params.put("valid_sign", AccebillConstant.AB_BILL_VALID_SIGN_1);
		sql.append("and v.end_date<:todayDate ");
		sql.append("and a.cdt_ratgs is null ");
		sql.append("order by a.create_date ");
		Date todayDate=DateUtils.getDateYMD();
		params.put("todayDate", todayDate);
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}
	/**
	 * 根据传递流水ID获取本单位此流水前上一笔此票据流水Id信息
	 * @param recordId  流水ID
	 * @return 上一笔流水信息
	 */
	public String getUpBusinessByRecordId(String recordId){
		StringBuilder sql=new StringBuilder("select a.id from ab_accrecords a ");
		sql.append("inner join ab_accrecords a1 on a.vvid=a1.vvid ");
		sql.append("where ");
		sql.append("a.status=:status ");
		sql.append("and a.opt_status=:opt_status ");
		sql.append("and a.create_date<a1.create_date ");
		sql.append("and a1.id=:recordId ");
		sql.append("order by a.create_date desc ");
		Map<String,Object> params=new HashMap<>();
		params.put("recordId", recordId);
		params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("opt_status", AccebillConstant.AB_BILL_OPT_STATUS19);
		Query query=this.createSQLQuery(sql.toString(), params).setMaxResults(1);
		Object obj=query.uniqueResult();
		if(obj!=null){
			return (String)obj;
		}
		return "";
	}
	@Transactional(readOnly = true)
	public AbAccrecords getBillByVid(String vvid,String briefId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("vvid", vvid);
		StringBuilder sql=new StringBuilder(" FROM AbAccrecords");
		sql.append(" where vvid=:vvid");
		if(!StringUtils.isBlank(briefId)){
			sql.append(" and  briefId=:briefId");
			params.put("briefId", briefId);
		}
		@SuppressWarnings("unchecked")
		List<AbAccrecords> list=this.listByHQL(sql.toString(), params);
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	@Transactional(readOnly = true)
	public AbAccrecords getBillByVouchid(String vid,String briefId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("vid", vid);
		StringBuilder sql=new StringBuilder(" FROM AbAccrecords");
		sql.append(" where vid=:vid");
		if(!StringUtils.isBlank(briefId)){
			sql.append(" and  briefId=:briefId");
			params.put("briefId", briefId);
		}
		sql.append(" order by createDate desc");
		List<AbAccrecords> list=this.listByHQL(sql.toString(), params);
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 根据从表id和票据池交易明细id获取流水信息
	 * @param vvid 从表id
	 * @param pdId 票据池交易明细id
	 * @param dateSort 创建日期排序  true 降序 false 升序
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbAccrecords getBillByPdIdAndVvId(String vvid,String pdId,boolean dateSort) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuilder sql=new StringBuilder(" FROM AbAccrecords");
		sql.append(" where vvid=:vvid");
		params.put("vvid", vvid);
		if(!StringUtils.isBlank(pdId)){
			sql.append(" and  pdId=:pdId");
			params.put("pdId", pdId);
		}
		if(dateSort){
			sql.append(" order by createDate desc ");
		}else{
			sql.append(" order by createDate asc ");
		}
		List<AbAccrecords> list=this.listByHQL(sql.toString(), params);
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}else{
			//持有票据处理
			StringBuilder sql1=new StringBuilder(" FROM AbAccrecords");
			Map<String,Object> params1 = new HashMap<String,Object>();
			sql1.append(" where 1=1");
			sql1.append(" and validSign=1");
			if(!StringUtils.isBlank(pdId)){
				sql1.append(" and  pdId=:pdId");
				params1.put("pdId", pdId);
			}
			List<AbAccrecords> list1=this.listByHQL(sql1.toString(), params);
			if(list1!=null&&!list1.isEmpty()){
				return list1.get(0);
			}
		}
		return null;
	}
	public int updateRecordByParams(Map<String,Object> params,List<String> listIds){
        StringBuilder hql=new StringBuilder("update AbAccrecords "
                + " set "
                + "updateDate = :updateTime ");
        if(params.containsKey("status")){
            hql.append(",status = :status ");
        }
        if(params.containsKey("optStatus")){
            hql.append(",optStatus = :optStatus ");
        }
        if(params.containsKey("validSign")){
            hql.append(",validSign = :validSign");
        }
		if(params.containsKey("pdId")){
			hql.append(",pdId = :pdId");
		}
        if(params.containsKey("sendSign")){
        	hql.append(",sendSign = :sendSign");
        }
        if(params.containsKey("returnCode")){
       	   hql.append(",returnCode = :returnCode ");
         }
         if(params.containsKey("returnMsg")){
       	   hql.append(",returnMsg = :returnMsg");
         }
    	if(params.containsKey("sid")){
    		hql.append(" where id =:sid");
    	}
    	if(ListUtils.isNotNullOrEmpty(listIds)){
    		hql.append(" where id in (:listIds) ");
    		params.put("listIds", listIds);
    	}
        //防止条件异常修改状态
    	if(!ListUtils.isNotNullOrEmpty(listIds) && !params.containsKey("sid")){
    		return 0;
    	}
        return this.batchExecute(hql.toString(), params);
    }
    /**
	 * 根据票据池交易明细pdId修改流水状态
	 * @param params
	 * @return
	 */
	public int updateStatusByPdId(Map<String,Object> params) {
		StringBuilder hql = new StringBuilder("update AbAccrecords "
				+ " set updateDate = :updateDate ");
		if (params.containsKey("updateBy")) {
			hql.append(",updateBy = :updateBy ");
		}
		if (params.containsKey("updateName")) {
			hql.append(",updateName = :updateName ");
		}
		if(params.containsKey("status")){
			hql.append(",status = :status ");
		}
		if(params.containsKey("optStatus")){
			hql.append(",optStatus = :optStatus ");
		}
		if(params.containsKey("validSign")){
			hql.append(",validSign = :validSign");
		}
		hql.append(" where pdId = :pdId ");
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 通过从表Id获取对应业务类型最近一笔正常出去业务的流水
	 * @param vvid
	 * @param briefId
	 * @return
	 */
	public AbAccrecords getLastAbAccrecordsByParams(String vvid,String briefId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("vvid", vvid);
		StringBuilder sql=new StringBuilder(" FROM AbAccrecords");
		sql.append(" where vvid=:vvid and validSign=:validSign ");
		params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_2);
		if(!StringUtils.isBlank(briefId)){
			sql.append(" and  briefId=:briefId");
			params.put("briefId", briefId);
		}
		sql.append(" order by createDate desc ");
		@SuppressWarnings("unchecked")
		List<AbAccrecords> list=this.listByHQL(sql.toString(), 0 ,1 , params);
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 获取线上票据应付未结清、应收持有业务数据
	 * @return
	 */
	public List<AbAccrecordsVO> getAbAccrecordsVOByHold(boolean isExpire){
		Map<String,Object> params=new HashMap<>();
		StringBuffer sql=new StringBuffer("select v.orig_no as origNo,v.SBBLL_RNG_STRT_SN sbbllRngStrtSn, v.SBBLL_RNG_END_SN sbbllRngEndSn,v.new_Stm_Issuofppr_Ind newStmIssuofpprInd,"
				+ "v.bill_status as billStatus, v.EBILL_BL_CRCL_INDC ebillBlCrclIndc,v.end_date endDate,v.id vid, a.vvid vvid,a.REQ_NM reqName,a.Req_Acctsvcr reqAcctsvcr,"
				+ "a.id,a.req_acct_id as reqAcctId,vv.bank_acc as bankAcc,a.brief_Id briefId from ab_voucherinfo v ");
		sql.append(" left join ab_accrecords a on v.id=a.vid ");
		sql.append(" left join ab_voucherinfo_vice vv on vv.id=a.vvid ");
		//发起方账号关联电票账号方便排查离线登记的在线
//		sql.append(" left join bt_bank_acc ba on ba.electric_bill='1' and ba.circulation_mode='1' and ba.bank_acc=a.req_acct_id ");
		//sql.append(" where a.status=:status and a.valid_Sign=:validSign and a.opt_Status=:optStatus ");
		sql.append(" where a.status=:status and ( a.valid_Sign=:validSign or a.brief_id =:briefId003 ) and a.opt_Status=:optStatus ");
		sql.append(" and ((a.send_sign=:sendSign and a.brief_id in (:briefIds)) ");//线上电票
		sql.append(" or (a.send_sign=:sendSign and a.brief_id in (:briefId018) and v.end_date>=:lastDate10 and v.end_date<=:todayDate ) ");//线上票据质押数据已到期且未结清已经到期日期是近10天的票据也可以查询正面和背面
		sql.append(" or (a.brief_id in (:briefId6001) and a.send_sign=:sendSignN and v.bill_flag = :billFlag11 )) ");//线下登记线上账号电票
		if(isExpire){
			sql.append(" and v.end_date<:endDateNextDate");
			sql.append(" and vv.type='1' ");//应收票据
			params.put("endDateNextDate", DateUtils.addDay(DateUtils.getDateYMD(),1));
		}else {
			sql.append(" and ((v.bill_status not in (:overBillStatus) or v.bill_status is null) or (v.EBILL_BL_CRCL_INDC != 'TF0501' or v.EBILL_BL_CRCL_INDC is null)) ");
		}
		sql.append(" order by a.business_Date desc,a.create_Date desc ");
		params.put("sendSign", AccebillConstant.AB_BILL_SEND_SIGN_Y);
		params.put("sendSignN", AccebillConstant.AB_BILL_SEND_SIGN_N);
		params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
		params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
//	    params.put("briefId003", AccebillConstant.BRIEF_STATUS_003);
		params.put("overBillStatus", new String[]{AccebillConstant.AB_BILL_STATUS_000000, AccebillConstant.NEWBILL_STATUS_CS06});
		params.put("briefId003", AccebillConstant.BRIEF_STATUS_003);
		params.put("briefId018", AccebillConstant.BRIEF_STATUS_018);
		params.put("todayDate", DateUtils.getDateYMD());
		params.put("lastDate10", DateUtils.addDay(DateUtils.getDateYMD(),-10));
		params.put("briefIds", new String[]{
	    	  AccebillConstant.BRIEF_STATUS_003,
	          AccebillConstant.BRIEF_STATUS_9003,
	          AccebillConstant.BRIEF_STATUS_9010,
	          AccebillConstant.BRIEF_STATUS_9012,
	          AccebillConstant.BRIEF_STATUS_9018,
	          AccebillConstant.BRIEF_STATUS_9019});
		params.put("billFlag11",Integer.parseInt(AccebillConstant.AB_BILL_FLAG_ELECT));
		params.put("briefId6001", new String[]{AccebillConstant.BRIEF_STATUS_6001,AccebillConstant.BRIEF_STATUS_001});
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}
	/**
	 * 根据vvid删除票据流水
	 * @param vvid
	 * @return
	 */
	public int deleteByVvId(String vvid){
		Map<String,Object> params=new HashMap<>();
		params.put("vvid",vvid);
		String hql="DELETE AbAccrecords where vvid=:vvid";
		return this.batchExecute(hql, params);
	}
	/**
	 * 根据lastId删除流水
	 * 用于收款流水恢复数据时使用
	 * @param lastIds
	 * @return
	 */
	public int deleteByLastId(String[] lastIds){
		StringBuilder hql=new StringBuilder("delete from AbAccrecords ");
		hql.append(" where lastid in (:lastId) and validSign=:validSign ");
		Map<String,Object> params=new HashMap<>();
		params.put("lastId",lastIds);
		params.put("validSign",AccebillConstant.AB_BILL_VALID_SIGN_1);
		return this.batchExecute(hql.toString(), params);
	}
	public List<AbAccrecordsVO> abPoolRecordList(Map<String,Object> refParams){
		StringBuilder sql = new StringBuilder()
			.append(" select ")
			.append(" c.id as id,c.vvId as vvid,c.vId as vid,")
			.append(" v.orig_no as origNo,c.bankendrsmtmk as bankendrsmtmk,c.cn_accrecords as cnAccrecords,")
			.append(" v.bill_flag as billFlag,v.bill_type as billType,c.abs as abs,")
			.append(" c.rmk as rmk,c.inside_sing as insideSing,c.enter_stock_old as enterStockOld,")
			.append(" c.bif_code as bifCode,vv.corp_id as corpId,vv.VALID_SIGN as validSign,")
//			.append(" c.bill_id as billId, ba.circulation_Mode as circulationMode,vv.bank_Acc as bankAcc,vv.bank_Code as bankCode")
			.append(" c.bill_id as billId, vv.circulation_Mode as circulationMode,vv.bank_Acc as bankAcc,vv.bank_Code as bankCode")
			.append(" from ab_accrecords c")
			.append(" left join ab_voucherinfo v  on v.id=c.vid")
			.append(" left join ab_voucherinfo_vice vv on c.vvid = vv.id")
//			.append(" left join BT_BANK_ACC ba on ba.bank_acc = vv.bank_acc")
//			.append(" LEFT JOIN BIS_BIF_INIT bi on bi.id = ba.bif_code")
			.append(" where 1=1")
			.append(" and c.id in (:ids)");
		return this.listBySQLAliasToBean(sql.toString(),refParams,AbAccrecordsVO.class,null);
	}
	/**
	 * 根据签收表ID和流水表ID获取最近的票据流水表ID
	 * @param recid 签收表ID
	 * @param acid 需要去除的流水表ID
	 * @return
	 */
	public String getListidByRecordIdAndAcid(String recid,String acid){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql= new StringBuilder("select id from ab_accrecords where recid=:recid ");
		params.put("recid",recid);
		if(StringUtils.isNotEmpty(acid)){
			sql.append("and id!=:acid ");
			params.put("acid",acid);
		}
		sql.append(" order by create_date desc ");
		Query query=this.createSQLQuery(sql.toString(),params);
		query.setMaxResults(1);
		String listid=(String)query.uniqueResult();
		return listid;
	}
	/**
	 * 根据从表id获取所有流水id
	 * @param vvid 从表id
	 * @return 流水i
	 */
	public List<AbAccrecordsVO> listAbAccrecordsIdByVvId(String vvid){
		Map<String,Object> params=new HashMap<>();
		params.put("vvid",vvid);
		StringBuilder sql=new StringBuilder("select");
		sql.append(" a.id as id,a.brief_Id as briefId,a.vvid as vvid ");
		sql.append(" from ab_accrecords a ");
		sql.append(" where a.vvid=:vvid");
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}
	/**
	 * 根据从表id和流水表状态获取所有流水id
	 * @param vvid 从表id
	 * @param status 流水表状态
	 * @return 流水i
	 */
	public List<AbAccrecordsVO> listAbAccrecordsIdByVvIdAndStatus(String vvid,Integer status){
		Map<String,Object> params=new HashMap<>();
		params.put("vvid",vvid);
		StringBuilder sql=new StringBuilder("select");
		sql.append(" a.id as id,a.brief_Id as briefId,a.vvid as vvid ");
		sql.append(" from ab_accrecords a ");
		sql.append(" where a.vvid=:vvid");
		if(status != null){
			sql.append(" and a.status=:status");
			params.put("status",status);
		}
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}
	/**
	 * 根据票据号银行接口为工商银行、线上外部背书业务完成的数据
	 * @param origNo 票据号
	 * @return 流水信息
	 */
	public List<AbAccrecordsVO> getAbAccrecordsVOByOrigNo(String origNo,String sbbllRngStrtSn, String sbbllRngEndSn){
		Map<String,Object> params=new HashMap<>();
		params.put("origNo",origNo);
		StringBuilder sql=new StringBuilder("select");
		sql.append(" a.business_date as businessDate, a.req_nm as reqNm, a.rec_nm as recNm, a.bankendrsmtmk as bankendrsmtmk, ");
		sql.append(" v.orig_no as origNo, a.id as id, v.SBBLL_RNG_STRT_SN as sbbllRngStrtSn, v.SBBLL_RNG_END_SN as sbbllRngEndSn ");
		sql.append(" from  AB_ACCRECORDS a left join AB_VOUCHERINFO v   on v.id=a.vid");
		sql.append("  where a.bif_code='9040' and a.brief_id='010' and a.inside_sing=0 and a.opt_status=19 ");
		sql.append("  and  v.orig_no=:origNo ");
		if(sbbllRngStrtSn != null){
			sql.append("  and  v.sbbll_Rng_Strt_Sn=:sbbllRngStrtSn ");
			params.put("sbbllRngStrtSn",sbbllRngStrtSn);
		}
		if(sbbllRngEndSn != null){
			sql.append("  and  v.sbbll_Rng_End_Sn=:sbbllRngEndSn ");
			params.put("sbbllRngEndSn",sbbllRngEndSn);
		}
		sql.append(" order by a.apply_date");
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}
	/**
	 * 修改票据主表id为vid的应收票据流水6001的validSign为2
	 *
	 * @param vid 票据主表id
	 */
	public void updateWithEndorse(String vid) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("vid", vid);
		StringBuilder sql = new StringBuilder("update AB_ACCRECORDS l set valid_sign = 2 ");
		sql.append(" where l.vid = :vid and l.brief_id = '6001'");
		this.batchSQLExecute(sql.toString(), params);
	}
	/**
	 * 根据主表id和从表id获取状态为95(已完成)的流水信息记录
	 * @param vid 票据主表id
	 * @param vvid 从表id
	 * @param dateSort 创建日期排序  true 降序 false 升序
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbAccrecords> queryAbAccrecordsByVidAndVVid(String vid,String vvid,boolean dateSort) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuilder sql=new StringBuilder(" FROM AbAccrecords ab");
		sql.append(" where ab.status = '95'  and ab.vid=:vid");
		params.put("vid", vid);
		if(!StringUtils.isBlank(vvid)){
			sql.append(" and  ab.vvid=:vvid");
			params.put("vvid", vvid);
		}
		if(dateSort){
			sql.append(" order by ab.createDate desc ");
		}else{
			sql.append(" order by ab.createDate asc ");
		}
		List<AbAccrecords> list=this.listByHQL(sql.toString(), params);
		if(CollectionUtils.isEmpty(list)){
			return new ArrayList<AbAccrecords>();
		}
		return list;
	}
	/**
	 * 根据主表id和从表id获取状态为96(已拆票)的流水信息记录
	 * @param vid 票据主表id
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbAccrecords> queryAbAccrecordsByVid(String vid) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuilder sql=new StringBuilder(" FROM AbAccrecords ab");
		sql.append(" where ab.status = '96'  and ab.vid=:vid");
		params.put("vid", vid);
		List<AbAccrecords> list=this.listByHQL(sql.toString(), params);
		if(CollectionUtils.isEmpty(list)){
			return new ArrayList<AbAccrecords>();
		}
		return list;
	}

	/**
	 * 获取已拆分并且可操作的明细数据(收票登记、收票签收、背书签收、质押签收、质押解除签收、贴现赎回签收)
	 * @param oldAccId 拆分持有流水ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbAccrecords> listByAvaidSplit(String oldAccId) {
		return listByAvaidSplit(null, oldAccId, null);
	}
	/**
	 * 获取已拆分并且可操作的明细数据(收票登记、收票签收、背书签收、质押签收、质押解除签收、贴现赎回签收)
	 * @param vids 票据主表id list
	 * @return
	 */
	public List<AbAccrecords> listByAvaidSplit(List<String> vids,String corpId) {
		return listByAvaidSplit(vids, null, corpId);
	}

	/**
	 * 获取已拆分并且可操作的明细数据(收票登记、收票签收、背书签收、质押签收、质押解除签收、贴现赎回签收、贴现申请)
	 * @param vids 票据主表id list
	 * @param oldAccId 拆分持有流水ID
	 * @return
	 */
	public List<AbAccrecords> listByAvaidSplit(List<String> vids, String oldAccId, String corpId) {
		if(CollectionUtils.isEmpty(vids) && StrUtil.isEmpty(oldAccId)){
			return new ArrayList<AbAccrecords>();
		}
		Map<String,Object> params = new HashMap<>();
		StringBuilder sql=new StringBuilder(" FROM AbAccrecords a ");
		sql.append(" where a.status =:status and a.validSign =:validSign and a.briefId in (:briefIds) and a.optStatus=:optStatus  ");
		sql.append(" and EXISTS(select 1 from AbVoucherinfoVice vv where vv.id=a.vvid and vv.boxStatus=:boxStatus ");
		if(StrUtil.isNotEmpty(corpId)){
			sql.append(" and vv.corpId=:corpId  ");
			params.put("corpId", corpId);
		}
		sql.append(" )  ");
		if(!CollectionUtils.isEmpty(vids)){
			params.put("vids", vids);
			sql.append(" and a.vid in (:vids) ");
		}
		if(StrUtil.isNotEmpty(oldAccId)){
			params.put("oldAccId", oldAccId);
			sql.append(" and (a.oldAccId =:oldAccId or a.id=:oldAccId) ");
			sql.append(" and EXISTS(select 1 from AbVoucherinfo v where v.id=a.vid and v.sbbllRngStrtSn=' ')  ");
		}
		sql.append(" order by a.createDate ");
		params.put("boxStatus", AccebillConstant.AB_BOX_STATUS_Y);
		params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
		params.put("briefIds", new String[]{
				AccebillConstant.BRIEF_STATUS_6001
				,AccebillConstant.BRIEF_STATUS_9003
				,AccebillConstant.BRIEF_STATUS_9010
				,AccebillConstant.BRIEF_STATUS_6011
				,AccebillConstant.BRIEF_STATUS_9012
				,AccebillConstant.BRIEF_STATUS_9018
				,AccebillConstant.BRIEF_STATUS_9019});
		List<AbAccrecords> list=this.listByHQL(sql.toString(), params);
		if(CollectionUtils.isEmpty(list)){
			return new ArrayList<AbAccrecords>();
		}
		return list;
	}

	/**
	 * 获取拆分并且业务中数据(背书、质押、贴现、质押解除)
	 * @param oldAccId 拆分持有流水ID
	 * @return
	 */
	public List<AbAccrecords> listByUnderwaySplit(String oldAccId) {
		if(StrUtil.isEmpty(oldAccId)){
			return new ArrayList<AbAccrecords>();
		}
		Map<String,Object> params = new HashMap<>();
		StringBuilder sql=new StringBuilder(" FROM AbAccrecords a ");
		sql.append(" where ((a.briefId in (:briefIds) and (a.status in (:status) or a.optStatus in (:optStatus))) " +
				" or (a.briefId = '6011' and a.status=95 " +
					" and NOT EXISTS(select 1 from AbAccrecords b where b.briefId='011' and b.lastid=a.id and b.optStatus in ('18','19','-3','-4')) ))");
		if(StrUtil.isNotEmpty(oldAccId)){
			params.put("oldAccId", oldAccId);
			sql.append(" and (a.oldAccId =:oldAccId or a.id=:oldAccId) ");
//			sql.append(" and oldAccId =:oldAccId ");
		}
		sql.append(" order by a.createDate ");
		params.put("status", new Integer[]{AccebillConstant.AB_BILL_BUS_STATUS11
				,AccebillConstant.AB_BILL_BUS_STATUS94});
		params.put("optStatus", new String[]{
				AccebillConstant.AB_BILL_OPT_STATUS15
				,AccebillConstant.AB_BILL_OPT_STATUS16
				,AccebillConstant.AB_BILL_OPT_STATUS17
//				,AccebillConstant.AB_BILL_OPT_STATUS18
				,AccebillConstant.AB_BILL_OPT_STATUS_1});
		params.put("briefIds", new String[]{
				AccebillConstant.BRIEF_STATUS_010
				,AccebillConstant.BRIEF_STATUS_011
				,AccebillConstant.BRIEF_STATUS_6011
				,AccebillConstant.BRIEF_STATUS_018
				,AccebillConstant.BRIEF_STATUS_019
				,AccebillConstant.AB_POOL_BRIEF_8018});
		List<AbAccrecords> list=this.listByHQL(sql.toString(), params);
		if(CollectionUtils.isEmpty(list)){
			return new ArrayList<AbAccrecords>();
		}
		return list;
	}

	/**
	 * 获取拆分并且待发送的数据(背书、质押、贴现、质押解除)
	 * @param oldAccId 拆分持有流水ID
	 * @return
	 */
	public List<AbAccrecords> listByAwaitSendSplit(String oldAccId) {
		if(StrUtil.isEmpty(oldAccId)){
			return new ArrayList<AbAccrecords>();
		}
		Map<String,Object> params = new HashMap<>();
		StringBuilder sql=new StringBuilder(" FROM AbAccrecords a ");
		sql.append(" where a.briefId in (:briefIds) and a.status =:status and a.optStatus=:optStatus ");
		sql.append(" and EXISTS(select 1 from AbVoucherinfo v where v.id=a.vid and v.sbbllRngStrtSn=' ')  ");
		if(StrUtil.isNotEmpty(oldAccId)){
			params.put("oldAccId", oldAccId);
			sql.append(" and (a.oldAccId =:oldAccId or a.id=:oldAccId) ");
//			sql.append(" and a.oldAccId =:oldAccId ");
		}
		sql.append(" order by a.updateDate ");
		params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS16);
		params.put("briefIds", new String[]{
				AccebillConstant.BRIEF_STATUS_010
				,AccebillConstant.BRIEF_STATUS_011
				,AccebillConstant.BRIEF_STATUS_018
				,AccebillConstant.BRIEF_STATUS_019
				,AccebillConstant.AB_POOL_BRIEF_8018
				,AccebillConstant.AB_POOL_BRIEF_8019});
		List<AbAccrecords> list=this.listByHQL(sql.toString(), params);
		if(CollectionUtils.isEmpty(list)){
			return new ArrayList<AbAccrecords>();
		}
		return list;
	}


	/**
	 * 定时任务查询待生成需求单的承兑到期
	 * @return
	 */
	public List<AbAccrecordsDTO> listTaskBillForNisDemand(Date endDate) {
		StringBuilder sql = new StringBuilder();
		Map<String,Object> params = new HashMap<>();
		sql.append(" select c.corp_id corpId,c.bank_acc bankAcc,c.bank_name bankName,z.bill_money billMoney,z.orig_no origNo,  ");
		sql.append(" z.end_date endDate,z.id ");
		sql.append(" from AB_VOUCHERINFO_VICE c ");
		sql.append(" join AB_VOUCHERINFO z on z.id=c.vid ");
		sql.append(" join AB_ACCRECORDS l on c.id = l.vvid ");
		sql.append(" left join nis_demand d on z.id = d.pay_bill_id ");
		sql.append(" where c.valid_sign != 3 and l.status = 95 and l.opt_status = 19 and (z.old_draft_id is null or z.old_draft_id not like '%-del') and l.BRIEF_ID in ('001','003','004','6120','6101') ");
		sql.append(" and l.VALID_SIGN = '1' ");
		sql.append(" and (c.BAIL_PERCENT is null or c.BAIL_PERCENT < 1) ");
		sql.append(" and c.type = '0' and c.bail_percent <> 100 ");
		sql.append(" and d.pay_bill_id is null ");//没有生成需求单的
		// 修改为到期日等于指定日期的数据
		sql.append(" and z.end_date > :startDate  ");
		sql.append(" and z.end_date <= :endDate5  ");
		params.put("startDate", DateUtils.getIntegralStartTime(new Date()));
		params.put("endDate5", endDate);
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsDTO.class);
	}

	public void updateApprover(String id, ICurrentUser userVo) {
		Map<String,Object> params = new HashMap<>();
		String sql = "update AB_ACCRECORDS set final_approver = :finalApprover  where id = :id ";
		params.put("id",id);
		params.put("finalApprover",userVo.getUsername());
		this.createSQLQuery(sql, params).executeUpdate();
	}
}
