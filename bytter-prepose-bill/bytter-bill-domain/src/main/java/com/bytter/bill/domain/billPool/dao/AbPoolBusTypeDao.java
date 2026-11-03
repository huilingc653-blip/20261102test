package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPoolBusType;
import com.bytter.bill.domain.billPool.vo.AbPoolBusTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
* @Title:  AbPoolBusTypeDao.java   
* @Package com.bytter.biz.billPool.dao   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2020-1-02 下午19:39:11   
* @version V1.0
 */
@Repository
public class AbPoolBusTypeDao extends BaseDao<AbPoolBusType,String>{
	
	protected static StringBuilder publicSqlVoid(){
		return new StringBuilder().append("select ")
					.append(" ap.id as id,")
					.append(" ap.BUSTYPE_CODE as bustypeCode,")
					.append(" ap.BUSTYPE_NAME as bustypeName,")
					.append(" ap.VALID_SIGN as validSign,")
					.append(" ap.PARENT_ID as parentId,")
					.append(" ap.BUSTYPE_LEVEL as bustypeLevel,")
					.append(" (select ab.bustype_name from AB_POOL_BUSTYPE ab where ab.id=ap.parent_id) as parentBusName")
					.append(" from AB_POOL_BUSTYPE ap")
			        .append(" WHERE 1=1 ");
	}
	public List<AbPoolBusTypeVO> listBusType(AbPoolBusTypeVO queryVo) {
		StringBuilder hql = new StringBuilder(publicSqlVoid());
		if (!StringUtils.isEmpty(queryVo.getValidSign())) {
			if (queryVo.getValidSign().equals("1")) {
				hql.append(" and ap.VALID_SIGN =:validSign");
			} else {
				hql.append(" and ap.VALID_SIGN <>:validSign");
			}
		}
		if (!StringUtils.isEmpty(queryVo.getBustypeCode())) {
			hql.append(" and ap.BUSTYPE_CODE like :busTypeCode");
		}
		if (StringUtils.isNotBlank(queryVo.getBustypeName())) {
			hql.append(" and ap.BUSTYPE_NAME like :busTypeName");
		}
		hql.append(" order by ap.BUSTYPE_LEVEL,ap.create_time ");
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(queryVo.getBustypeName())) {
			params.put("busTypeName", "%" + queryVo.getBustypeName().trim() + "%");
		}
		if (!StringUtils.isEmpty(queryVo.getBustypeCode())) {
			params.put("busTypeCode", "%" + queryVo.getBustypeCode() + "%");
		}
		if (!StringUtils.isEmpty(queryVo.getValidSign())) {
			params.put("validSign", queryVo.getValidSign());
		}
		List<AbPoolBusTypeVO> list = listBySQLAliasToBean(hql.toString(),
				params, AbPoolBusTypeVO.class);
		return ListUtils.isNotNullOrEmpty(list)? list : null;
	}
	  
	public AbPoolBusTypeVO getAbPoolBustTypeVOByBpId(String bpId) {
		StringBuilder hql = new StringBuilder(publicSqlVoid());
		hql.append(" and ap.id=:id");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", bpId);
		List<AbPoolBusTypeVO> list = listBySQLAliasToBean(hql.toString(),
				params, AbPoolBusTypeVO.class);
		return ListUtils.isNotNullOrEmpty(list)? list.get(0) : null;
	}
	public int updateEntityByParams(Map<String, Object> params) {
		StringBuilder hql = new StringBuilder("update AbPoolBusType " + " set "
				+ "updateTime = :updateTime ");
		if (params.containsKey("updateId")) {
			hql.append(",updateId = :updateId ");
		}
		if (params.containsKey("validSign")) {
			hql.append(",validSign = :validSign ");
		}
		if (params.containsKey("bustypeName")) {
			hql.append(",bustypeName = :bustypeName ");
		}
		if (params.containsKey("bustypeCode")) {
			hql.append(",bustypeCode = :bustypeCode ");
		}
		if (params.containsKey("parentId")) {
			hql.append(",parentId = :parentId ");
		}
		if (params.containsKey("bustypeLevel")) {
			hql.append(",bustypeLevel = :bustypeLevel ");
		}
		hql.append(" where 1=1 ");
		if (params.containsKey("id")) {
			hql.append(" and id =:id ");
		}
		return this.batchExecute(hql.toString(), params);
	}
	public boolean checkRepeat(AbPoolBusTypeVO abPoolBusTypeVO) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" ap.ID");
		sql.append(" from AB_POOL_BUSTYPE ap");
		sql.append(" where 1=1");
		if (StringUtils.isNotBlank(abPoolBusTypeVO.getId())) {
			sql.append(" and ap.id <> :id");
		}
		if (StringUtils.isNotBlank(abPoolBusTypeVO.getBustypeCode())) {
			sql.append(" and (ap.BUSTYPE_CODE=:bustypeCode");
		}
		if (StringUtils.isNotBlank(abPoolBusTypeVO.getBustypeName())) {
			sql.append(" or ap.BUSTYPE_NAME=:bustypeName)");
		}
		SQLQuery query = createSQLQuery(sql.toString());
		if (StringUtils.isNotBlank(abPoolBusTypeVO.getId())) {
			query.setString("id", abPoolBusTypeVO.getId());
		}
		if (!StringUtils.isBlank(abPoolBusTypeVO.getBustypeCode())) {
			query.setString("bustypeCode", abPoolBusTypeVO.getBustypeCode());
		}
		if (!StringUtils.isBlank(abPoolBusTypeVO.getBustypeName())) {
			query.setString("bustypeName", abPoolBusTypeVO.getBustypeName());
		}
		if (!query.list().isEmpty()) {
			return true;
		}
		return false;
	}
	/**
	 * 获取所有父类Id
	 * 
	 * @return 单位id
	 * @author
	 */
	public List<String> getAllPoolParentIds() {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p.PARENT_ID as parentId from ab_pool_bustype p ");
		sql.append(" where p.VALID_SIGN <> '-2' ");
		Query query = createSQLQuery(sql.toString(), params);
		List<String> list = query.list();
		return list;
	}
	public List<AbPoolBusType> listEffectiveCorp() {
		// StringBuilder hql = new
		// StringBuilder("from AbPoolBusType m where m.validSign = 1");
		StringBuilder hql = new StringBuilder("from AbPoolBusType m ");
		hql.append(" order by m.bustypeCode");
		return createQuery(hql.toString()).list();
	}
	public boolean checkDelRepeat(Map<String, Object> params) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" ap.ID");
		sql.append(" from AB_POOL_BUSTYPE ap");
		sql.append(" where 1=1");
		sql.append(" and (id=:id or parent_Id =:sid) ");
		sql.append(" and valid_sign=1");
		List<AbPoolBusTypeVO> list = listBySQLAliasToBean(sql.toString(),
				params, AbPoolBusTypeVO.class);
		return null != list && list.size() > 0 ? true : false;
	}
	  
}
