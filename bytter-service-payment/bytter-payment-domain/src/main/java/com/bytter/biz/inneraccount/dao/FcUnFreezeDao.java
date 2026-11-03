package com.bytter.biz.inneraccount.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.inneraccount.entity.FcFreeze;
import com.bytter.biz.inneraccount.vo.FcFreezeListVO;
import com.bytter.biz.inneraccount.vo.FcFreezeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 内部账号解冻
 * @date 2017-06-16 17:31:26
 */
@Repository
public class FcUnFreezeDao extends BaseDao<FcFreeze, String> {


	/**
	 * 解冻查询列表
	 *
	 * @param dataTablesInput
	 * @param joinSql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput findCanModifyUnFreezes(DataTablesInput dataTablesInput, String joinSql,Map<String,Object> params) {
		StringBuilder sb = new StringBuilder(" AND FREEZE.FREEZE_SIGN=:freezeSign AND FREEZE.STATUS NOT IN(:status) ");
		sb.append(joinSql);
		params.put("freezeSign",InnerAccountConstant.BUS_TYPE_UNFREEZE);
		params.put("status",new Integer[]{CommonConstant.STATUE_CHECK,CommonConstant.STATUE_WAIT_CHECK});
		String sql = unFreezeQuery(sb.toString());

		return getDynamicBySQL(dataTablesInput, sql, FcFreezeVO.class, "", false,params);
	}
	private String unFreezeQuery(String unFreezeCondition) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT  ID as id,UN_FREEZE_ID as unFreezeId, BLNCTR_ACC as blnctrAcc,");
		sb.append("FREEZE_MONEY as freezeMoney,FREEZE_TYPE as freezeType,BEGIN_DATE as beginDate, END_DATE as endDate, ACCU_FREEZE_MONEY as accuFreezeMoney,");
		sb.append(" FREEZE_NO as freezeNo,STATUS as status,UN_FREEZE_MONEY as unFreezeMoney, ");
		sb.append(" EXEC_OFFICIAL_NO as execOfficialNo,EXEC_CARD_NO as execCardNo,FREEZE_REASON as freezeReason,FREEZE_ORG as freezeOrg");
		sb.append(" FROM (select FREEZE.ID,FREEZE.UN_FREEZE_ID,FREEZE.BLNCTR_ACC, ");
		sb.append(" FREEZE.FREEZE_MONEY,FREEZE.exec_Official_No, FREEZE.exec_Card_No, FREEZE.freeze_Reason, FREEZE.freeze_Org,");
		sb.append("(FREEZE.FREEZE_MONEY - (SELECT BT_NVL_NUM(SUM(F.FREEZE_MONEY),0) FROM FC_FREEZE F WHERE F.FREEZE_SIGN = 'T' AND F.UN_FREEZE_ID = FREEZE.Id)) AS UN_FREEZE_MONEY, ");
		sb.append("FREEZE.FREEZE_TYPE,FREEZE.BEGIN_DATE,FREEZE.END_DATE,FREEZE.ACCU_FREEZE_MONEY,FREEZE.FREEZE_NO,FREEZE.STATUS FROM FC_FREEZE FREEZE ");
		sb.append("WHERE 1=1  ");
		sb.append(unFreezeCondition);
		sb.append(") fcAcc WHERE 1=1 ");
		return sb.toString();
	}
	private String canUnFreezeQuery(String unFreezeCondition) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT  ID as id,UN_FREEZE_ID as unFreezeId, BLNCTR_ACC as blnctrAcc,");
		sb.append("FREEZE_MONEY as freezeMoney,FREEZE_TYPE as freezeType,BEGIN_DATE as beginDate, END_DATE as endDate, ACCU_FREEZE_MONEY as accuFreezeMoney,");
		sb.append(" FREEZE_NO as freezeNo,STATUS as status,UN_FREEZE_MONEY as unFreezeMoney, ");
		sb.append(" EXEC_OFFICIAL_NO as execOfficialNo,EXEC_CARD_NO as execCardNo,FREEZE_REASON as freezeReason,FREEZE_ORG as freezeOrg");
		sb.append(" FROM (select FREEZE.ID,FREEZE.UN_FREEZE_ID,FREEZE.BLNCTR_ACC, ");
		sb.append(" FREEZE.FREEZE_MONEY,FREEZE.exec_Official_No, FREEZE.exec_Card_No, FREEZE.freeze_Reason, FREEZE.freeze_Org,");
		sb.append("(FREEZE.FREEZE_MONEY - (SELECT BT_NVL_NUM(SUM(F.FREEZE_MONEY),0) FROM FC_FREEZE F WHERE F.FREEZE_SIGN = 'T' AND F.UN_FREEZE_ID = FREEZE.Id)) AS UN_FREEZE_MONEY, ");
		sb.append("FREEZE.FREEZE_TYPE,FREEZE.BEGIN_DATE,FREEZE.END_DATE,FREEZE.ACCU_FREEZE_MONEY,FREEZE.FREEZE_NO,FREEZE.STATUS FROM FC_FREEZE FREEZE ");
		sb.append("WHERE 1=1  ");
		sb.append(unFreezeCondition);
		sb.append(") fcAcc WHERE 1=1 and UN_FREEZE_MONEY>0");
		return sb.toString();
	}

	/**
	 * 查询可解冻记录
	 *
	 * @param accList
	 * @return
	 */
	public List<FcFreezeListVO> findCanUnFreezes(List<String> accList) {
		Map<String,Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder("AND FREEZE.FREEZE_SIGN=:freezeSign AND FREEZE.STATUS=:status ");
		if (CollectionUtil.isNotEmpty(accList)) {
			sb.append(" and ").append(SqlUtil.getInSql(accList, "FREEZE.BLNCTR_ACC", true));
		}
		sb.append(" AND (FREEZE.DEDUCT_SIGN !=:deductSign or FREEZE.DEDUCT_SIGN is null)");
		sb.append(" AND NOT EXISTS(SELECT * FROM FC_FREEZE SUB WHERE SUB.UN_FREEZE_ID = FREEZE.ID AND SUB.STATUS!=:status)");
		sb.append(" AND NOT EXISTS(SELECT * FROM FC_FREEZE SUB WHERE SUB.blnctr_acc = FREEZE.blnctr_acc AND SUB.STATUS!=:status");
		sb.append(" and sub.freeze_sign=:freezeSignUn and SUB.freeze_type in(:freezeType) )");

		params.put("freezeSign",InnerAccountConstant.BUS_TYPE_FREEZE);
		params.put("status",CommonConstant.STATUE_CHECK);
		params.put("freezeEndSign",InnerAccountConstant.BOOLEAN_TYPE_YES);
		params.put("deductSign",InnerAccountConstant.BOOLEAN_TYPE_YES);
		params.put("freezeSignUn",InnerAccountConstant.BUS_TYPE_UNFREEZE);
		params.put("freezeType",new String[]{InnerAccountConstant.FREEZE_TYPE_ALL,InnerAccountConstant.FREEZE_TYPE_S});
		String sql = canUnFreezeQuery(sb.toString());
		return listBySQLAliasToBean(sql, params, FcFreezeListVO.class);
	}

	/**
	 * 根据id查当前实体对象
	 *
	 * @param id
	 * @return
	 */
	public FcFreezeListVO getEntityById(String id) {
		StringBuilder sb = new StringBuilder(" AND FREEZE.FREEZE_SIGN='");
		sb.append(InnerAccountConstant.BUS_TYPE_UNFREEZE);
		sb.append("'");
		sb.append(" and FREEZE.id=:id");
		String sql = unFreezeEditQuery(sb.toString());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, FcFreezeListVO.class);
	}

	/**
	 * 解冻复核列表查询
	 *
	 * @param dataTablesInput
	 * @param joinSql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput findCanCheckUnFreezes(DataTablesInput dataTablesInput, String joinSql,Map<String,Object> params) {
		StringBuilder sb = new StringBuilder("AND FREEZE.FREEZE_SIGN =:freezeSign AND FREEZE.STATUS =:status ");
		sb.append(joinSql);
		String sql = unFreezeQueryByNet(sb.toString());
		params.put("freezeSign",InnerAccountConstant.BUS_TYPE_UNFREEZE);
		params.put("status",CommonConstant.STATUE_WAIT_CHECK);
		return getDynamicBySQL(dataTablesInput, sql, FcFreezeVO.class, "", false,params);
	}

	/**
	 * 查询带网点
	 *
	 * @param unFreezeCondition
	 * @return
	 */
	private String unFreezeQueryByNet(String unFreezeCondition) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT  ID as id,UN_FREEZE_ID as unFreezeId, BLNCTR_ACC as blnctrAcc, ACC_NAME as accName, NET_ID as netId, CUR_ID as curId, CORP_ID as corpId,");
		sb.append("FREEZE_MONEY as freezeMoney,FREEZE_TYPE as freezeType,BEGIN_DATE as beginDate, END_DATE as endDate, ACCU_FREEZE_MONEY as accuFreezeMoney,");
		sb.append(" FREEZE_NO as freezeNo,STATUS as status,UN_FREEZE_MONEY as unFreezeMoney,REFUSE_WHY as refuseWhy, ");
		sb.append(" EXEC_OFFICIAL_NO as execOfficialNo,EXEC_CARD_NO as execCardNo,FREEZE_REASON as freezeReason,FREEZE_ORG as freezeOrg");
		sb.append(" FROM (select FREEZE.ID,FREEZE.UN_FREEZE_ID,fcAcc.BLNCTR_ACC ,fcAcc.ACC_NAME,fcAcc.NET_Id ,fcAcc.CUR_Id , fcAcc.CORP_ID ,");
		sb.append(" FREEZE.FREEZE_MONEY,FREEZE.exec_Official_No, FREEZE.exec_Card_No, FREEZE.freeze_Reason, FREEZE.freeze_Org,");
		sb.append(" FREEZE.UN_FREEZE_MONEY,FREEZE.REFUSE_WHY, ");
		sb.append("FREEZE.FREEZE_TYPE,FREEZE.BEGIN_DATE,FREEZE.END_DATE,FREEZE.ACCU_FREEZE_MONEY,FREEZE.FREEZE_NO,FREEZE.STATUS FROM FC_FREEZE FREEZE, FC_ACC fcAcc ");
		sb.append("WHERE FREEZE.BLNCTR_ACC = fcAcc.BLNCTR_ACC ");
		sb.append(unFreezeCondition);
		sb.append(") fcAcc WHERE 1=1 ");
		return sb.toString();
	}


	/**
	 * 根据相关条件查当前实体对象
	 *
	 * @param freeze
	 * @return
	 */
	public List<FcFreeze> findAllBy(FcFreeze freeze) {
		StringBuilder sb = new StringBuilder("select id as id,blnctrAcc as blnctrAcc,freezeType as freezeType from FcFreeze where blnctrAcc=:blnctrAcc and freezeSign=:freezeSign ");
		sb.append(" and freezeEndSign=:freezeEndSign and status=:status and id!=:id");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("blnctrAcc", freeze.getBlnctrAcc());
		params.put("freezeSign", InnerAccountConstant.BUS_TYPE_FREEZE);
		params.put("freezeEndSign", InnerAccountConstant.BOOLEAN_TYPE_NO);
		params.put("status", CommonConstant.STATUE_CHECK);
		params.put("id", freeze.getUnFreezeId());
		return listByHQLAliasToBean(sb.toString(), params, FcFreeze.class);
	}

	/**
	 * 编辑界面查询
	 *
	 * @param unFreezeCondition
	 * @return
	 */
	private String unFreezeEditQuery(String unFreezeCondition) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT  ID as id,UN_FREEZE_ID as unFreezeId, BLNCTR_ACC as blnctrAcc, ACC_NAME as accName, NET_ID as netId, CUR_ID as curId, CORP_ID as corpId,");
		sb.append("FREEZE_MONEY as freezeMoney,FREEZE_TYPE as freezeType,BEGIN_DATE as beginDate, END_DATE as endDate, ACCU_FREEZE_MONEY as accuFreezeMoney,");
		sb.append(" FREEZE_NO as freezeNo,STATUS as status,UN_FREEZE_MONEY as unFreezeMoney,REFUSE_WHY as refuseWhy, ");
		sb.append(" EXEC_OFFICIAL_NO as execOfficialNo,EXEC_CARD_NO as execCardNo,FREEZE_REASON as freezeReason,FREEZE_ORG as freezeOrg");
		sb.append(" FROM (select FREEZE.ID,FREEZE.UN_FREEZE_ID,fcAcc.BLNCTR_ACC ,fcAcc.ACC_NAME,fcAcc.NET_Id ,fcAcc.CUR_Id , fcAcc.CORP_ID ,");
		sb.append(" FREEZE.FREEZE_MONEY,FREEZE.exec_Official_No, FREEZE.exec_Card_No, FREEZE.freeze_Reason, FREEZE.freeze_Org,");
		sb.append(" FREEZE.UN_FREEZE_MONEY,FREEZE.REFUSE_WHY, ");
		sb.append("FREEZE.FREEZE_TYPE,FREEZE.BEGIN_DATE,FREEZE.END_DATE,FREEZE.ACCU_FREEZE_MONEY,FREEZE.FREEZE_NO,FREEZE.STATUS FROM FC_FREEZE FREEZE, FC_ACC fcAcc ");
		sb.append("WHERE FREEZE.BLNCTR_ACC = fcAcc.BLNCTR_ACC ");
		sb.append(unFreezeCondition);
		sb.append(") fcAcc WHERE 1=1 ");
		sb.append(" order by BLNCTRACC");
		return sb.toString();
	}
}
