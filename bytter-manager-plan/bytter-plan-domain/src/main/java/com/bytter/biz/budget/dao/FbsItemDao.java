package com.bytter.biz.budget.dao;
import com.bytter.biz.budget.entity.FbsItem;
import com.bytter.biz.budget.spi.vo.SimpleFbsItemVo;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Lists;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 预算科目
 * @date 2017-07-06 16:40:26
 */
@Repository
public class FbsItemDao extends BaseDao<FbsItem, String> {
	/**
	 * 根据id或者 code 查找 对象
	 *
	 * @param id
	 * @param itemCode
	 * @return
	 */
	public FbsItemVO getFbsItemVO(String id, String itemCode) {
		StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName,c.super_Item_Code as superItemCode,c.super_Item_Codes as superItemCodes,");
		sql.append(" c.sign as sign,c.is_Internal as isInternal,c.is_Control as isControl,c.valid_Sign as validSign,c.is_System as isSystem,c.is_Write as isWrite,c.is_Write_Detail as isWriteDetail,");
		sql.append(" c.is_Rigid as isRigid,c.rmk as rmk,c.is_Begin_Period as isBeginPeriod,c.ITEM_RELATION as itemRelation,c.is_End_Period as isEndPeriod,c.over_Per_Spend as overPerSpend,");
		sql.append(" c.cash_Item_Code as cashItemCode,c.cash_Item_Name as cashItemName,c.allot_Item_Code as allotItemCode,(bt_length(item_code)/3) as itemLevel");
		sql.append(" from Fbs_Item c where 1=1 ");
		if (StringUtils.isNotEmpty(id)) {
			sql.append(" and c.id='");
			sql.append(id);
			sql.append("'");
		}
		if (StringUtils.isNotEmpty(itemCode)) {
			sql.append(" and c.item_Code ='");
			sql.append(itemCode);
			sql.append("'");
		}
		return getBySQLAliasToBean(sql.toString(), null, FbsItemVO.class);
	}
	/**
	 * 根据id或者 code 查找 对象
	 *
	 * @param itemCodes
	 * @return
	 */
	public List<FbsItemVO> getFbsItemVOS(Set<String> itemCodes) {
		if(EmptyUtils.isEmpty(itemCodes)){
			return null;
		}
		StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName,c.super_Item_Code as superItemCode,c.super_Item_Codes as superItemCodes,");
		sql.append(" c.sign as sign,c.is_Internal as isInternal,c.is_Control as isControl,c.valid_Sign as validSign,c.is_System as isSystem,c.is_Write as isWrite,c.is_Write_Detail as isWriteDetail,");
		sql.append(" c.is_Rigid as isRigid,c.rmk as rmk,c.is_Begin_Period as isBeginPeriod,c.ITEM_RELATION as itemRelation,c.is_End_Period as isEndPeriod,c.over_Per_Spend as overPerSpend,");
		sql.append(" c.cash_Item_Code as cashItemCode,c.cash_Item_Name as cashItemName,c.allot_Item_Code as allotItemCode,(bt_length(item_code)/3) as itemLevel");
		sql.append(" from Fbs_Item c where c.item_Code in (:itemCodes) ");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("itemCodes", itemCodes);
		return listBySQLAliasToBean(sql.toString(), params, FbsItemVO.class);
	}
	/**
	 * 根据code获取实体
	 * @param itemCode
	 * @return
	 */
	public FbsItem getFbsItem(String itemCode) {
		String hql = " from FbsItem f where f.itemCode = :itemCode";
		Map<String,Object> params = new HashMap<>();
		params.put("itemCode",itemCode);
		return (FbsItem)this.getSession().createQuery(hql.toString()).setProperties(params).uniqueResult();
	}
	/**
	 * 根据name搜索查询
	 *
	 * @param fbsItem
	 * @param validSign
	 * @return
	 */
	public List<FbsItemVO> listFbsItem(FbsItemVO fbsItem, String validSign) {
		StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName,c.super_Item_Code as superItemCode,c.super_Item_Codes as superItemCodes,");
		sql.append(" c.sign as sign,c.is_Internal as isInternal,c.is_Control as isControl,c.valid_Sign as validSign,c.is_System as isSystem,c.is_Write as isWrite,c.is_Write_Detail as isWriteDetail,");
		sql.append(" c.is_Rigid as isRigid,c.rmk as rmk,c.is_Begin_Period as isBeginPeriod,c.ITEM_RELATION as itemRelation,c.is_End_Period as isEndPeriod,c.over_Per_Spend as overPerSpend,");
		sql.append(" c.cash_Item_Code as cashItemCode,c.cash_Item_Name as cashItemName,c.allot_Item_Code as allotItemCode,(bt_length(item_code)/3) as itemLevel ");
		sql.append(" from Fbs_Item c where 1=1 ");
		if (StringUtils.isNotEmpty(validSign)) {
			sql.append(" and c.valid_Sign='");
			sql.append(validSign);
			sql.append("'");
		}
		if (!EmptyUtils.isEmpty(fbsItem)) {
			if (StringUtils.isNotEmpty(fbsItem.getItemName())) {
				sql.append(" and c.item_Name like '%");
				sql.append(fbsItem.getItemName());
				sql.append("%'");
			}
			if (StringUtils.isNotEmpty(fbsItem.getItemCode())) {
				sql.append(" and c.item_Code like '%");
				sql.append(fbsItem.getItemCode());
				sql.append("%'");
			}
			if (fbsItem.getSign() != null && 0 != fbsItem.getSign()) {
				sql.append(" and c.sign =");
				sql.append(fbsItem.getSign());
			}
			if (!EmptyUtils.isEmpty(fbsItem.getShowItem())) {
				if(fbsItem.getShowItem() == -1){
					//查询末级科目，即不存在子科目的科目
					sql.append(" and c.item_code not in (select super_item_code from fbs_item where super_item_code is not null)");
				}else if(fbsItem.getShowItem()>0){
					sql.append(" and (bt_length(item_code)/3)="+fbsItem.getShowItem());
				}
			}
		}
		return listBySQLAliasToBean(sql.toString(), null, FbsItemVO.class);
	}
	/**
	 * 根据name搜索查询
	 *
	 * @param fbsItem
	 * @param validSign
	 * @return
	 */
	public List<FbsItemVO> listFbsItemV2(FbsItemVO fbsItem, String validSign) {
		StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName,c.super_Item_Code as superItemCode,c.super_Item_Codes as superItemCodes,");
		sql.append(" c.sign as sign,c.is_Internal as isInternal,c.is_Control as isControl,c.valid_Sign as validSign,c.is_System as isSystem,c.is_Write as isWrite,c.is_Write_Detail as isWriteDetail,");
		sql.append(" c.is_Rigid as isRigid,c.rmk as rmk,c.is_Begin_Period as isBeginPeriod,c.ITEM_RELATION as itemRelation,c.is_End_Period as isEndPeriod,c.over_Per_Spend as overPerSpend,");
		sql.append(" c.cash_Item_Code as cashItemCode,c.cash_Item_Name as cashItemName,c.allot_Item_Code as allotItemCode");
		sql.append(" from Fbs_Item c where 1=1 ");
		if (StringUtils.isNotEmpty(validSign)) {
			sql.append(" and c.valid_Sign='");
			sql.append(validSign);
			sql.append("'");
		}
		if (!EmptyUtils.isEmpty(fbsItem)) {
			if (StringUtils.isNotEmpty(fbsItem.getItemName())) {
				sql.append(" and c.item_Name like '%");
				sql.append(fbsItem.getItemName());
				sql.append("%'");
			}
			if (StringUtils.isNotEmpty(fbsItem.getItemCode())) {
				sql.append(" and c.item_Code like '%");
				sql.append(fbsItem.getItemCode());
				sql.append("%'");
			}
			if (fbsItem.getSign() != null && 0 != fbsItem.getSign()) {
				sql.append(" and c.sign =");
				sql.append(fbsItem.getSign());
			}
			if (!EmptyUtils.isEmpty(fbsItem.getShowItem())) {
				if(fbsItem.getShowItem() == -1){
					//查询末级科目，即不存在子科目的科目
					sql.append(" and c.item_code not in (select super_item_code from fbs_item where super_item_code is not null)");
				}else if(fbsItem.getShowItem()>0){
					sql.append(" and (bt_length(item_code)/3)="+fbsItem.getShowItem());
				}
			}
		}
		List<Map> list = listBySQL(sql.toString(), null);
		if(EmptyUtils.isEmpty(list)){
			return Lists.newArrayListWithCapacity(0);
		}
		return BeanUtils.copyMapListProperties(list,  FbsItemVO.class);
	}
	/**
	 * 根据code搜索查询
	 *
	 * @param codes
	 * @return
	 */
	public List<FbsItemVO> listFbsItemByCodes(List<String> codes, FbsItemVO fbsItem) {
		StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName,c.super_Item_Code as superItemCode,c.super_Item_Codes as superItemCodes,");
		sql.append(" c.sign as sign,c.is_Internal as isInternal,c.is_Control as isControl,c.valid_Sign as validSign,c.is_System as isSystem,c.is_Write as isWrite,c.is_Write_Detail as isWriteDetail,");
		sql.append(" c.is_Rigid as isRigid,c.rmk as rmk,c.is_Begin_Period as isBeginPeriod,c.ITEM_RELATION as itemRelation,c.is_End_Period as isEndPeriod,c.over_Per_Spend as overPerSpend,");
		sql.append(" c.cash_Item_Code as cashItemCode,c.cash_Item_Name as cashItemName,c.allot_Item_Code as allotItemCode");
		sql.append(" from Fbs_Item c where 1=1 ");
		String where = getSQLIn(codes, 900, "c.item_Code");
		if (!EmptyUtils.isEmpty(fbsItem)) {
			if (!EmptyUtils.isEmpty(fbsItem.getShowItem())) {
				if (fbsItem.getShowItem() == -1) {
					//查询末级科目，即不存在子科目的科目
					sql.append(" and c.item_code not in (select super_item_code from fbs_item where super_item_code is not null)");
				} else if (fbsItem.getShowItem() > 0) {
					sql.append(" and (bt_length(item_code)/3)=" + fbsItem.getShowItem());
				}
			}
		}
		sql.append(" and ");
		return listBySQLAliasToBean(sql.append(where).toString(), null, FbsItemVO.class);
	}
	/**
	 * 根据super_item_code搜索查询
	 *
	 * @param codes
	 * @return
	 */
	public List<FbsItemVO> listFbsItemBySuperItemCodes(List<String> codes) {
		StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName,c.super_Item_Code as superItemCode,c.super_Item_Codes as superItemCodes,");
		sql.append(" c.sign as sign,c.is_Internal as isInternal,c.is_Control as isControl,c.valid_Sign as validSign,c.is_System as isSystem,c.is_Write as isWrite,c.is_Write_Detail as isWriteDetail,");
		sql.append(" c.is_Rigid as isRigid,c.rmk as rmk,c.is_Begin_Period as isBeginPeriod,c.ITEM_RELATION as itemRelation,c.is_End_Period as isEndPeriod,c.over_Per_Spend as overPerSpend,");
		sql.append(" c.cash_Item_Code as cashItemCode,c.cash_Item_Name as cashItemName,c.allot_Item_Code as allotItemCode");
		sql.append(" from Fbs_Item c where 1=1 and ");
		String where = getSQLIn(codes, 900, "c.super_item_code");
		return listBySQLAliasToBean(sql.append(where).toString(), null, FbsItemVO.class);
	}
	/**
	 * 根据代码 删除 记录
	 *
	 * @param codes
	 */
	public void delFbsItem(List<String> codes) {
		StringBuilder sql = new StringBuilder(" delete from Fbs_Item   where  1=1 ");
		String where = getSQLIn(codes, 900, "item_code");
		sql.append(" and ").append(where);
		this.createSQLQuery(sql.toString()).executeUpdate();
	}
	/**
	 * 根据科目代码查找科目信息
	 *
	 * @param itemCode
	 * @return
	 */
	public FbsItem getFbsItemByCode(String itemCode) {
		String hql = "from FbsItem where itemCode=?0";
		Query query = createQuery(hql, itemCode);
		return (FbsItem) query.uniqueResult();
	}
	public BigDecimal isEdit(String itemCode) {
		String sql = "select count(1) as cnt from fbs_item f where f.super_item_code=:itemCode and f.valid_sign ='1'";
		Map<String, String> params = new HashMap<>();
		params.put("itemCode", itemCode);
		BigDecimal count = new BigDecimal(createSQLQuery(sql, params).uniqueResult().toString());
		return count;
	}
	/**
	 * 获取最大流水号
	 *
	 * @param prefix 业务前缀
	 * @return
	 */
	public String getSequence(String prefix, int sequenceLength) {
		StringBuilder sql = new StringBuilder();
		sql.append("select max(item_code) as currentSequence from fbs_item ");
		Map<String, String> sqlMap = new HashMap<>();
		sql.append(" where seq_code = :prefix");
		sqlMap.put("prefix", prefix);
		Object sqlValue = createSQLQuery(sql.toString(), sqlMap).uniqueResult();
		if (!EmptyUtils.isEmpty(sqlValue)) {
			String seq = sqlValue.toString().replace(prefix, "");
			Integer maxSeq = Integer.parseInt(seq) + 1;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < (sequenceLength - String.valueOf(maxSeq).length()); i++) {
				sb.append(0);
			}
			sb.append(maxSeq);
			return prefix + sb.toString();
		}
		return "";
	}
	/**
	 * 获取所有科目
	 * @return
	 */
	public List<FbsItem> getAllFbsItem() {
		String hql = " from FbsItem f ";
		Map<String,Object> params = new HashMap<>();
		return this.getSession().createQuery(hql.toString()).setProperties(params).list();
	}
	/**
	 * 获取科目最高层级
	 * @return
	 */
	public Integer getItemMaxLevel(){
		String sql="select max(bt_length(item_code))/3 as maxLevel  from fbs_item";
		Object result = this.getSession().createSQLQuery(sql).uniqueResult();
		if(EmptyUtils.isEmpty(result)){
			return 0;
		}else {
			if (result instanceof BigDecimal) {
				return ((BigDecimal) result).intValue();
			}
			if (result instanceof BigInteger) {
				return ((BigInteger) result).intValue();
			}
			return 0;
		}
	}
	/**
	 * 获取计划执行科目
	 * @return
	 */
	public List<FbsItemVO> listFbsItemForProcess(Integer sign) {
		StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName,c.super_Item_Code as superItemCode,c.super_Item_Codes as superItemCodes,");
		sql.append(" c.sign as sign,c.is_Internal as isInternal,c.is_Control as isControl,c.valid_Sign as validSign,c.is_System as isSystem,c.is_Write as isWrite,c.is_Write_Detail as isWriteDetail,");
		sql.append(" c.is_Rigid as isRigid,c.rmk as rmk,c.is_Begin_Period as isBeginPeriod,c.item_relation as itemRelation,c.is_End_Period as isEndPeriod,c.over_Per_Spend as overPerSpend,");
		sql.append(" c.cash_Item_Code as cashItemCode,c.cash_Item_Name as cashItemName,c.allot_Item_Code as allotItemCode");
		//获取允许计划执行的科目：1.有效；2.顶级科目；3.非计算科目；4.非期初/期末科目
		sql.append(" from Fbs_Item c where 1=1 and c.valid_Sign='1' and (c.super_Item_Code is null or c.super_Item_Code ='' ) and (c.item_relation is null or c.item_relation ='' ) and c.is_Begin_Period <> '1' and c.is_End_Period<>'1'");
		if(sign!=null){
			sql.append(" and c.sign=" +sign);
		}
		return listBySQLAliasToBean(sql.toString(), null, FbsItemVO.class);
	}

    public List<SimpleFbsItemVo> getSimpleFbsItemVo(List<String> itemCodes) {
        StringBuilder sql = new StringBuilder("select c.id as id, c.item_Code as itemCode, c.item_Name as itemName");
        sql.append(" from Fbs_Item c where c.item_Code in (:itemCodes) ");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("itemCodes", itemCodes);
        return createSQLQuery(sql.toString(), params)
                .addScalar("id", StandardBasicTypes.STRING)
                .addScalar("itemCode", StandardBasicTypes.STRING)
                .addScalar("itemName", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(SimpleFbsItemVo.class)).list();
    }
}
