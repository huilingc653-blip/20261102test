package com.bytter.biz.inneraccount.dao;
import cn.hutool.core.date.StopWatch;
import com.bytter.biz.bdlink.vo.BisAccDetailVO;
import com.bytter.biz.inneraccount.entity.BtBankAccDtl;
import com.bytter.biz.inneraccount.vo.BtBankAccDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.BisAccBalConstant;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.FlowConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 银行账号明细录入表
 * @date 2017-06-29 13:51:01
 */
@Repository
public class BtBankAccDtlDao extends BaseDao<BtBankAccDtl, String> {
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from (");
		sql.append(" select bbad.id,bbad.acc_no,bbad.corp_id,bbad.bank_acc,bbad.acc_name,bbad.bank_name,bbad.bif_code,bbad.opp_acc_no,bbad.opp_acc_name,bbad.opp_acc_bank,bbad.cd_sign,bbad.rb_sign,");
		sql.append(" case bbad.CD_SIGN when '0' then bbad.amt when '1' then 0.00 end as amt_in, case bbad.CD_SIGN when '1' then bbad.amt when '0' then 0.00 end as amt_out, ");
		sql.append(" bbad.amt,bbad.bal,bbad.cur_id,bbad.uses,bbad.postscript,bbad.remark,bbad.trans_time,bbad.order_no,bbad.months,bbad.status,bbad.create_by,");
		sql.append(" bbad.create_date,bbad.update_by,bbad.update_date,bbad.project_remark,bbad.bank_serial_no  from bt_bank_acc_dtl bbad) t where 1=1 ");
//		sql.append(" left join bt_Bank_acc acc on acc.bank_acc=t.bank_acc ");
//		sql.append(" where acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK);
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		//交易时间
		String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
		String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
		if (StringUtils.isNotEmpty(gedTransTime)) {
			gedTransTime = gedTransTime + " 00:00:00";
			dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
		}
		if (StringUtils.isNotEmpty(ledTransTime)) {
			ledTransTime = ledTransTime + " 23:59:59";
			dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
		}
		dataTablesInput.setOrderString(new OrderString("t.order_no", "desc"));
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccDtlVO.class, "t", false);
	}
	/**
	 * 根据id查找银行账户流水信息
	 * zuoy 2017-07-03
	 *
	 * @param id
	 * @return BtBankAccDtlVO
	 */
	public BtBankAccDtlVO getByUnion(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.id,t.acc_no,t.corp_id,t.bank_acc,t.acc_name,t.bank_name,t.bif_code,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
		sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,t.order_no,t.months,t.status,t.create_by,");
		sql.append(" t.create_date,t.update_by,t.update_date,t.project_remark  from bt_bank_acc_dtl t ");
//		sql.append(" left join bis_bif_init bis on bis.id = t.bif_code ");
		sql.append(" where t.id = :id ");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, BtBankAccDtlVO.class);
	}
	/**
	 * 获取交易日期
	 *
	 * @param bankAcc   账户
	 * @param curId     币别ID
	 * @param transTime 比较日期，为null则查询最后一次
	 * @param operator  跟transTime组合使用，日期比较运算符，参照MatchTypeEnum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Date getLastTransTime(String bankAcc, String curId, Date transTime, String operator) {
		StringBuilder hql = new StringBuilder();
		HashMap<String, Object> params = new HashMap<String, Object>();
		hql.append(" from BtBankAccDtl where bankAcc = :bankAcc and curId = :curId and status <> :status ");
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		params.put("status", BisAccBalConstant.STATUE_DEL);
		if (!EmptyUtils.isEmpty(transTime)) {
			hql.append("and bt_to_char(transTime, 'yyyy-MM-dd') " + MatchTypeEnum.getTypeName(operator).getValue().toLowerCase() + " :transTime ");
			params.put("transTime", DateUtils.formatDate(transTime));
		}
		hql.append(" order by transTime desc, orderNo desc ");
		List<BtBankAccDtl> list = this.listByHQL(hql.toString(), params);
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		return list.get(0).getTransTime();
	}
	/**
	 * 查询流水余额
	 *
	 * @param bankAcc   账户
	 * @param curId     币别ID
	 * @param transTime 交易日期,为null则查询最后一次
	 * @param operator  跟balDate组合使用，日期比较运算符，参照MatchTypeEnum
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getAmtByTransTime(String bankAcc, String curId, Date transTime, String operator) {
		StringBuilder hql = new StringBuilder();
		HashMap<String, Object> params = new HashMap<String, Object>();
		hql.append(" from BtBankAccDtl where bankAcc = :bankAcc and curId = :curId and status <> :status ");
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		params.put("status", BisAccBalConstant.STATUE_DEL);
		if (!EmptyUtils.isEmpty(transTime)) {
			hql.append("and transTime " + MatchTypeEnum.getTypeName(operator).getValue().toLowerCase() + " :transTime ");
			params.put("transTime", transTime);
			if (operator.equalsIgnoreCase(MatchTypeEnum.EQ.getValue())) {
				// 若找不到指定日期最大顺序号，返回null
				Integer maxOrderNo = this.getOrderNo(transTime, bankAcc, curId);
				if (EmptyUtils.isEmpty(maxOrderNo)) {
					return null;
				}
				hql.append(" and orderNo = :orderNo ");
				params.put("orderNo", maxOrderNo);
			}
		}
		hql.append(" order by transTime desc, orderNo desc ");
		List<BtBankAccDtl> list = this.listByHQL(hql.toString(), params);
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		return list.get(0).getBal();
	}
	/**
	 * 获取指定日期最大顺序号
	 *
	 * @param transTime 日期
	 * @param bankAcc   账户
	 * @param curId     币别ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer getOrderNo(Date transTime, String bankAcc, String curId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select max(order_No) maxOrderNo from bt_bank_acc_dtl");
		sql.append(" where bank_acc = :bankAcc and cur_id = :curId and trans_time = :transTime and status <> :status ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		params.put("transTime", transTime);
		params.put("status", BisAccBalConstant.STATUE_DEL);
		List<String> maxOrderNo = createSQLQuery(sql.toString(), params).addScalar("maxOrderNo", StandardBasicTypes.STRING).list();
		if (EmptyUtils.isBlank(maxOrderNo.get(0))) {
			return 99;
		}
		return Integer.parseInt(maxOrderNo.get(0));
	}
	/**
	 * 查询账户指定月份未提交的流水记录
	 *
	 * @param bankAcc 账户
	 * @param curId   币别ID
	 * @param month   月份
	 * @return
	 */
	public List<BtBankAccDtlVO> getTempDtlByMonth(String bankAcc, String curId, Integer month) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.id,t.acc_no,t.corp_id,t.bank_acc,t.acc_name,t.bank_name,t.bif_code,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
		sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,t.order_no,t.months,t.status,t.create_by,");
		sql.append(" t.create_date,t.update_by,t.update_date,t.project_remark,t.BANK_SERIAL_NO  from bt_bank_acc_dtl t ");
		sql.append(" where status in (0, -1) and bank_acc = :bankAcc and cur_id = :curId and months = :month ");
		sql.append(" order by t.trans_time desc, order_no desc ");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		params.put("month", month);
		return listBySQLAliasToBean(sql.toString(), params, BtBankAccDtlVO.class);
	}
	/**
	 * 获取调整列表的数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesAdjust(DataTablesInput dataTablesInput) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.id,t.acc_no,t.corp_id,t.bank_acc,t.acc_name,t.bank_name,t.bif_code,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
		sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,t.order_no,t.months,t.status,t.create_by,");
		sql.append(" t.create_date,t.update_by,t.update_date,t.project_remark  from bt_bank_acc_dtl t where 1 = 1 ");
		dataTablesInput.setOrderString(new OrderString("t.order_no", "asc"));
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccDtlVO.class, "t", false);
	}
	/**
	 * 获取审批列表数据
	 *
	 * @param dataTablesInput
	 * @param businessSql
	 * @return
	 */
	public DataTablesOutput getDataTablesApprove(DataTablesInput dataTablesInput, String businessSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.id,t.acc_no,t.corp_id,t.bank_acc,t.acc_name,t.bank_name,t.bif_code,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
		sql.append(" t.amt,t.bal,t.cur_id,cur.cur_name as curName,t.uses,t.postscript,t.remark,t.trans_time,t.order_no,t.months,t.status,t.create_by,");
		sql.append(" t.create_date,t.update_by,t.update_date,t.project_remark ");
		sql.append(" from bt_bank_acc_dtl t ");
		sql.append(" LEFT JOIN bt_currency cur ON t.cur_id = cur.id");
		sql.append(" where 1 = 1 and ").append(businessSql);
		String flowStatus = dataTablesInput.getParameter().get("flowStatus");
		//已审批不过滤状态
		if(!FlowConstant.PASS.equals(flowStatus)) {
			//过滤已审数据
			sql.append(" and t.status<> ").append(CommonConstant.STATUE_CHECK);
		}
		//交易时间
		String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
		String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
		if (StringUtils.isNotEmpty(gedTransTime)) {
			gedTransTime = gedTransTime + " 00:00:00";
			dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
		}
		if (StringUtils.isNotEmpty(ledTransTime)) {
			ledTransTime = ledTransTime + " 23:59:59";
			dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccDtlVO.class, "t", false);
	}
	/**
	 * 根据条件获取暂存的流水信息
	 *
	 * @param bankAcc   账户
	 * @param curId     币别ID
	 * @param transTime 交易日期
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BtBankAccDtl> listByTempBtBankAccDtl(String bankAcc, String curId, Date transTime) {
		StringBuilder hql = new StringBuilder();
		HashMap<String, Object> params = new HashMap<String, Object>();
		hql.append(" from BtBankAccDtl where bankAcc = :bankAcc and curId = :curId and status in (0,-1) and trans_time = :transTime ");
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		params.put("transTime", transTime);
		hql.append(" order by order_no ");
		return this.listByHQL(hql.toString(), params);
	}
	/**
	 * 获取指定交易时间之后的数据流水记录
	 *
	 * @param bankAcc   账户
	 * @param curId     币别ID
	 * @param transTime 交易时间
	 * @return
	 */
	public List<BtBankAccDtl> listAfterBtBankAccDtl(String bankAcc, String curId, Date transTime, Integer orderNo, Date endDate) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.id,t.acc_no,t.corp_id,t.bank_acc,t.acc_name,t.bank_name,t.bif_code,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
		sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,t.order_no,t.months,t.status,t.create_by,");
		sql.append(" t.create_date,t.update_by,t.update_date,t.project_remark,t.BANK_SERIAL_NO  from bt_bank_acc_dtl t ");
		sql.append(" where status <> :status and bank_acc = :bankAcc and cur_id = :curId  ");
		sql.append(" and ((t.trans_time = :transTime and t.order_no > :orderNo ) or t.trans_time > :transTime) ");
		HashMap<String, Object> params = new HashMap<String, Object>();
		//截止日期
		if(!EmptyUtils.isEmpty(endDate)){
			sql.append(" and t.trans_time <=:endDate");
			endDate = DateUtils.parse(DateUtils.format(endDate, DateUtils.PATTERN_CLASSICAL_SIMPLE) + " 23:59:59", DateUtils.PATTERN_CLASSICAL);
			params.put("endDate", endDate);
		}
		sql.append(" order by t.trans_time, order_no ");
		params.put("status", BisAccBalConstant.STATUE_DEL);
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		params.put("transTime", transTime);
		params.put("orderNo", orderNo);
		return listBySQLAliasToBean(sql.toString(), params, BtBankAccDtl.class);
	}
	/**
	 * 查询流水余额
	 *
	 * @param bankAcc   账户
	 * @param curId     币别ID
	 * @param transTime 交易日期,为null则查询最后一次
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal lastByTransTime(String bankAcc, String curId, Date transTime, Integer orderNo) {
		StringBuilder hql = new StringBuilder();
		HashMap<String, Object> params = new HashMap<String, Object>();
		hql.append(" from BtBankAccDtl where bankAcc = :bankAcc and curId = :curId and status <> :status ");
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		params.put("status", BisAccBalConstant.STATUE_DEL);
		hql.append(" and ((transTime = :transTime and orderNo < :orderNo ) or transTime < :transTime) ");
		params.put("transTime", transTime);
		params.put("orderNo", orderNo);
		hql.append(" order by transTime desc, orderNo desc ");
		List<BtBankAccDtl> list = this.listByHQL(hql.toString(), params);
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		return list.get(0).getBal();
	}
	/**
	 * 获取当前账号最早的一条数据
	 * @return
	 */
	public List<BtBankAccDtl> getEarliestDtl(String bankAcc,String curId){
		StringBuilder hql = new StringBuilder();
		hql.append(" from BtBankAccDtl where bankAcc = :bankAcc and curId = :curId and status != -2");
		hql.append(" and transTime = ( SELECT min( transTime ) FROM BtBankAccDtl where bankAcc = :bankAcc and curId = :curId ) ");
		HashMap<String, Object> params = new HashMap<>();
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		return  listByHQLAliasToBean(hql.toString(), params, BtBankAccDtl.class);
	}
	/**
	 * 获取当前账号指定日期最后一条数据
	 * @return
	 */
	public List<BtBankAccDtl> getEarliestDtl(String bankAcc,String curId, Date searchDate){
		StringBuilder hql = new StringBuilder();
		hql.append(" from BtBankAccDtl where bankAcc = :bankAcc and curId = :curId and status != -2");
		HashMap<String, Object> params = new HashMap<>();
		if(EmptyUtils.isEmpty(searchDate)){
			hql.append(" and transTime = ( SELECT min( transTime ) FROM BtBankAccDtl where bankAcc = :bankAcc and curId = :curId ) ");
		} else {
			hql.append(" and transTime = ( SELECT max( transTime ) FROM BtBankAccDtl where bankAcc = :bankAcc and curId = :curId and transTime<=:searchDate ) ");
			params.put("searchDate", searchDate);
		}
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		hql.append(" order by transTime desc, orderNo desc");
		return  listByHQLAliasToBean(hql.toString(), params, BtBankAccDtl.class);
	}
	/**
	 * 根据单位统计银行账户流水
	 * @param corpId
	 * @return
	 */
	public int getBankAccDtlCountByCorpId(String corpId){
		String sql="select count(*) from bt_bank_acc_dtl where corp_id=:corpId";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		//0-暂存、11-待审批、-1-审批打回
		sql+=" and status in ('0','11','-1')";
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}
	/**
	 * 获取流水存在的离线银行账号
	 * @return
	 */
	public List<BtBankAccDtl> getListForFlush() {
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT distinct t.bank_acc AS bankAcc,t.cur_id AS curId ");
		sql.append(" FROM bt_bank_acc_dtl t ");
		sql.append(" LEFT JOIN bt_bank_acc acc ON t.bank_acc = acc.bank_acc ");
		sql.append(" WHERE 1 = 1  ");
		sql.append(" and t.BANK_ACC is not null and acc.IS_ONLINE = '0' and t.status != -2");
		return this.listBySQLAliasToBean(sql.toString(), null, BtBankAccDtl.class);
	}
    public List<BtBankAccDtl> listByIds(Set<String> btBankAccDtlIds) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from BtBankAccDtl where id in :btBankAccDtlIds");
		HashMap<String, Object> params = new HashMap<>();
		params.put("btBankAccDtlIds", btBankAccDtlIds);
		return  listByHQLAliasToBean(hql.toString(), params, BtBankAccDtl.class);
    }

	//本方账号、对方账号、借贷标志、交易时间、金额、摘要 不能重复
	public boolean isRepeat(String bankAcc, String oppAccNo, String cdSign, Date transTime, BigDecimal amt, String remark,BigDecimal bal) {
		String sql = "select * from bt_bank_acc_dtl t where t.status<>-2 and t.bank_acc=:bankAcc and t.opp_acc_no=:oppAccNo " +
				" and t.cd_sign=:cdSign and t.TRANS_TIME=:transTime and t.amt=:amt and t.remark=:remark and t.bal=:bal ";
		HashMap<String, Object> params = new HashMap<>();
		params.put("bankAcc",bankAcc);
		params.put("oppAccNo",oppAccNo);
		params.put("cdSign",cdSign);
		params.put("transTime",transTime);
		params.put("amt",amt);
		params.put("remark",remark);
		params.put("bal",bal);
		List<BtBankAccDtl> btBankAccDtls = listBySQLAliasToBean(sql, params, BtBankAccDtl.class);
		if(CollectionUtils.isEmpty(btBankAccDtls) || btBankAccDtls.size()< 1){
			return false;
		}
		return true;
	}

	public List<String> getAcclistByCondition(DataTablesInput dataTablesInput, List<String> corpList, List<String> accList) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.bank_acc from bt_bank_acc acc ");
		sql.append(" where 1 = 1 ");
		//根据查询条件及账户数据进行一次过滤
		String corpId = (String) dataTablesInput.getFormdata().get("ins_corpId");
		if (StringUtils.isNotBlank(corpId)) {
			String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
			corpList = Arrays.asList(corpIds);
		}
		if (EmptyUtils.isNotEmpty(corpList)) {
			if (corpList.size() >= 1000) {
				String corpAuthSql = SqlUtil.getInSql(corpList, "acc.corp_id", true);
				sql.append(" and ").append(corpAuthSql);
			} else {
				sql.append(" and acc.corp_id in (:corpList) ");
				params.put("corpList", corpList);
			}
		}
		if (EmptyUtils.isNotEmpty(accList)) {
			if (accList.size() >= 1000) {
				String accSql = SqlUtil.getInSql(accList, "acc.bank_acc", true);
				sql.append(" and ").append(accSql);
			} else {
				sql.append(" and acc.bank_acc in (:accLists) ");
				params.put("accLists", accList);
			}
		}
		if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("ins_attributeId"))) {//账户属性
			String attributeId = dataTablesInput.getFormdata().get("ins_attributeId").toString();
			if (StringUtils.isNotBlank(attributeId)) {
				sql.append(" and acc.attribute_Id in (:attributeId) ");
				params.put("attributeId", attributeId.split(CommonConstant.COMMA_SPLIT));
			}
		}
		if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("ins_natureId"))) {//账户性质
			String natureId = dataTablesInput.getFormdata().get("ins_natureId").toString();
			if (StringUtils.isNotBlank(natureId)) {
				sql.append(" and acc.nature_Id in (:natureId) ");
				params.put("natureId", natureId.split(CommonConstant.COMMA_SPLIT));
			}
		}
		if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("ins_accType"))) {//账户属性
			String accType = dataTablesInput.getFormdata().get("ins_accType").toString();
			if (StringUtils.isNotBlank(accType)) {
				sql.append(" and acc.acc_Type in (:accType) ");
				params.put("accType", accType.split(CommonConstant.COMMA_SPLIT));
			}
		}
		if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("accStatus"))) {//账户状态
			String accStatus = dataTablesInput.getFormdata().get("accStatus").toString();
			if (StringUtils.isNotBlank(accStatus)) {
				if (accStatus.equals(BtBankAccConstant.ACCSTATUS_1)) {
					sql.append(" and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
				} else if (accStatus.equals(BtBankAccConstant.ACCSTATUS_2)) {
					sql.append(" and acc.status = " + BtBankAccConstant.STATUE_DEL + " ");
				}
			}
		}
		if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("ins_bifCode"))) {//接口代码
			String bifCode = dataTablesInput.getFormdata().get("ins_bifCode").toString();
			if (StringUtils.isNotBlank(bifCode)) {
				sql.append(" and acc.bif_code in (:bifCode) ");
				params.put("bifCode", bifCode.split(CommonConstant.COMMA_SPLIT));
			}
		}
		if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("likes_bankAcc"))) {//账号
			String bankAcc = dataTablesInput.getFormdata().get("likes_bankAcc").toString();
			if (StringUtils.isNotBlank(bankAcc)) {
				sql.append(" and acc.bank_Acc = :bankAcc ");
				params.put("bankAcc", bankAcc);
			}
		}
		return this.createSQLQuery(sql.toString(), params).list();
	}

	public DataTablesOutput<BisAccDetailVO> getDataTablesUnionV2(DataTablesInput dataTablesInput, String joinSql) {
		StopWatch stopWatch = new StopWatch("银行流水查询效率");
		stopWatch.start("银行流水查询");
		//交易时间
		String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
		String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
		if (StringUtils.isNotEmpty(gedTransTime) && !gedTransTime.contains("00:00:00")) {
			gedTransTime = gedTransTime + " 00:00:00";
			dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
		}
		if (StringUtils.isNotEmpty(ledTransTime) && !ledTransTime.contains("23:59:59")) {
			ledTransTime = ledTransTime + " 23:59:59";
			dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
		}

		StringBuilder sqlForOtherFileds = new StringBuilder();
		sqlForOtherFileds.append(" select a.* from ( ");
		sqlForOtherFileds.append(" select t.id as id,t.id as dtlId,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
		sqlForOtherFileds.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
		sqlForOtherFileds.append(" case t.CD_SIGN when '0' then t.amt when '1' then 0.00 end as amt_in, case t.CD_SIGN when '1' then t.amt when '0' then 0.00 end as amt_out, ");
		sqlForOtherFileds.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
		sqlForOtherFileds.append(" acc.attribute_Id as attributeId, att.attribute_name as attributeName, t.project_remark as projectRemark,t.bank_serial_no as bank_serial_no, ");
		sqlForOtherFileds.append(" acc.nature_id as natureId, n.nature_name  as natureName, acc.acc_type as accType,acc.fc_acc_group_code as fcAccGroupCode, ");
		sqlForOtherFileds.append(" t.DTL_SYNC_STATUS syncStatus,t.bus_type as busType,acc.is_online as isOnline ");
		sqlForOtherFileds.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
		sqlForOtherFileds.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
		sqlForOtherFileds.append(" left join bt_acc_nature n on acc.nature_id = n.id");
		sqlForOtherFileds.append(" where  1 = 1 and (t.status <> -2 or t.status is null) ");
		if (StringUtils.isNotEmpty(joinSql)) {
			sqlForOtherFileds.append(joinSql);
		}
		if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("accStatus"))) {
			String accStatus = dataTablesInput.getFormdata().get("accStatus").toString();
			if (StringUtils.isNotBlank(accStatus)) {
				if (accStatus.equals(BtBankAccConstant.ACCSTATUS_1)) {
					sqlForOtherFileds.append(" and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
				} else if (accStatus.equals(BtBankAccConstant.ACCSTATUS_2)) {
					sqlForOtherFileds.append(" and acc.status = " + BtBankAccConstant.STATUE_DEL + " ");
				}
			}
		}
		String syncStatus = (String) dataTablesInput.getFormdata().get("eqs_syncStatus");
		if (!EmptyUtils.isEmpty(syncStatus)){
			if ("1".equals(syncStatus)){
				sqlForOtherFileds.append(" and t.DTL_SYNC_STATUS = 'S' ");
			}else{
				sqlForOtherFileds.append(" and ( t.DTL_SYNC_STATUS is null or t.DTL_SYNC_STATUS = 'F' ) ");
			}
			dataTablesInput.getFormdata().remove("eqs_syncStatus");
		}
		// if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("dtlId"))) {
		//     String id = dataTablesInput.getFormdata().get("dtlId").toString();
		//     sqlForOtherFileds.append(" and acc.id = " + id + " ");
		//     params.put("id", id);
		// }
		sqlForOtherFileds.append(" ) a where 1=1 ");
		DataTablesOutput<BisAccDetailVO> outputForBisAccDtls = getDynamicBySQL(dataTablesInput, sqlForOtherFileds.toString(), BisAccDetailVO.class, "a", false);
		stopWatch.stop();
		logger.error("stopWatch == " + stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
		return outputForBisAccDtls;
	}

	public Integer selectDtlCount(String bankAcc, String accCurId) {
		String countSql = "select count(*) from bis_acc_dtl where bank_acc = :bankAcc and cur_id = :accCurId";
		Map<String, Object> params = new HashMap<>();
		params.put("bankAcc", bankAcc);
		params.put("accCurId", accCurId);
		Object result = createSQLQuery(countSql, params).uniqueResult();
		if (result == null) {
			return 0;
		}
		return ((Number) result).intValue();
	}
}
