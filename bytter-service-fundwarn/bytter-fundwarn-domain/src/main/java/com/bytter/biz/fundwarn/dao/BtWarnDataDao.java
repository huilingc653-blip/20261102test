package com.bytter.biz.fundwarn.dao;
import com.bytter.biz.fundwarn.entity.BtWarnData;
import com.bytter.biz.fundwarn.vo.BtWarnDataInfoVO;
import com.bytter.biz.fundwarn.vo.HomeWarnDataSearchVO;
import com.bytter.biz.fundwarn.vo.HomeWarnDataVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 预警策略数据信息 Dao
 * @author zhengg
 * @date 2018-10-12
 *
 */
@Repository
public class BtWarnDataDao extends BaseDao<BtWarnData,String> {
	/**
	 * 预警策略数据列表数据
	 * @param corpAuthList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,List<String> corpAuthList) {
		Map<String, Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder();
		sql.append("select a.id,a.warn_date as warndate,a.create_date as createdate,a.update_date as updatedate,a.WS_ID as wsId,");
		sql.append("a.CORP_NAME as corpName,a.CORP_ID as corpId,a.bank_acc as bankAcc,a.fc_acc as fcAcc,a.code,a.name,a.reserve1, ");
		sql.append("a.type_code as typeCode,a.dimension_code as dimensioncode,a.cur_id as curId,a.trigger_num as triggerNum, ");
		sql.append("a.bank_type_id as banktypeid,a.size_num as sizeNum,a.payee_name as payeename,a.one_num as onenum,a.two_num as twonum, ");
		sql.append("a.conditions as conditions,a.BANK_TYPE_NAME bankTypeName,a.no,ws.warn_Level as warnLevel  ");
		sql.append("from bt_warn_data a ");
		sql.append("left join bt_warn_strategy ws on ws.ID = a.WS_ID ");
		sql.append(" where 1=1 ");
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "a.CORP_ID", params);
		//用户满足对应查询权限的单位数据
		sql.append(" and ").append(corpAuthSql).append(" ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtWarnDataInfoVO.class, null, false, params);
	}
	
	
    /**
     * 根据params获取查询数据
     * @param params 入参
     * @param hqlW 过滤条件
     * @return
     */
    public List<BtWarnData> getBtWarnDataByParams(Map<String,Object> params,String hqlW){
    	StringBuilder hql=new StringBuilder("from BtWarnData ");
    	hql.append(" where 1=1 ").append(hqlW);
    	hql.append(" order by createDate desc,updateDate desc ");
    	@SuppressWarnings("unchecked")
		List<BtWarnData> list=this.listByHQLAliasToBean(hql.toString(), params,BtWarnData.class);
    	return list;
    }
	/**
	 * 统计预警次数
	 * @param searchVO 查询条件
	 * @return
	 */
	public List<HomeWarnDataVO> getWarnDataTimes(HomeWarnDataSearchVO searchVO, String joinSql){
		StringBuilder sql = new StringBuilder();
		sql.append("select ws.warn_Level as warnLevel, a.warn_date as warnDate, count(*) as warnTimes");
		sql.append(" from bt_warn_data a left join BT_WARN_STRATEGY ws on ws.code = a.code");
		sql.append(" where 1 = 1");
		sql.append(joinSql);
		sql.append(" and a.warn_date >=:beginDate and a.warn_date<=:endDate");
		HashMap<String, Object> params = new HashMap<>();
		params.put("beginDate", DateUtils.parse(DateUtils.formatDate(searchVO.getBeginDate(), DateUtils.PATTERN_CLASSICAL), DateUtils.PATTERN_CLASSICAL));
		params.put("endDate", DateUtils.parse(DateUtils.formatDate(searchVO.getEndDate(), DateUtils.PATTERN_CLASSICAL), DateUtils.PATTERN_CLASSICAL));
		//预警类别
		if(StringUtils.isNotBlank(searchVO.getTypeCode())){
			sql.append(" and a.type_Code = :typeCode");
			params.put("typeCode", searchVO.getTypeCode());
		}
		//预警维度
		if(StringUtils.isNotBlank(searchVO.getDimensionCode())){
			sql.append(" and a.dimension_Code = :dimensionCode");
			params.put("dimensionCode", searchVO.getDimensionCode());
		}
		sql.append(" group by ws.warn_Level, a.warn_date order by a.warn_date");
		return this.listBySQLAliasToBean(sql.toString(), params, HomeWarnDataVO.class);
	}
	/**
	 * 获取预警统计数量
	 *
	 * @param joinSql
	 * @return
	 */
	public Integer getWarnDataCountByUserId(String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(a.id) from bt_warn_data a where 1=1");
		sql.append(" and ").append(joinSql);
		SQLQuery sqlQuery = createSQLQuery(sql.toString());
		BigDecimal count = (BigDecimal) sqlQuery.uniqueResult();
		return count.intValue();
	}
}
