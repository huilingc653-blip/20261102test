package com.bytter.subject.dao;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.subject.entity.FcSubject;
import com.bytter.subject.entity.FcSubjectPool;
import com.bytter.subject.vo.FcSubjectPoolVO;
import com.bytter.system.api.dto.FcSubjectPoolDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**   
 * @Title: Controller  
 * @Description: 资金池会计科目
 * @author zhengxn
 * @date 2019-4-30
 * @version V1.0   
 * TODO 迁移到基础模块
 */
@Repository
public class FcSubjectPoolDao extends BaseDao<FcSubjectPool,String>{
	
	/**
	 * 
	 * 功能介绍 : 资金池会计科目列表<br>	------
	 * @param sub 查询条件
	 * @author zhengxn
	 */
	@SuppressWarnings("unchecked")
	public List<FcSubjectPoolVO> listAllOrder(FcSubjectPool sub){
		StringBuilder hql = new StringBuilder("select fs.* ,fs.subject_name as subjectNameShow from Fc_Subject_Pool fs  where 1=1");
		Map<String,String> val = new HashMap<String,String>();
		
		if(StringUtils.isNotEmpty(sub.getSubjectCode())){
			hql.append(" and fs.subject_Code like :subjectCode");
			val.put("subjectCode", "%"+sub.getSubjectCode().trim()+"%");
		}
		
		if(StringUtils.isNotEmpty(sub.getSubjectName())){
			hql.append(" and fs.subject_Name like :subjectName");
			val.put("subjectName", "%"+sub.getSubjectName().trim()+"%");
		}
		
		if(StringUtils.isNotEmpty(sub.getSubjectSort()) && !"undefined".equals(sub.getSubjectSort())){
			if(!"".equals(sub.getSubjectSort().trim())){
				hql.append(" and fs.subject_Sort = :subjectSort ");
				val.put("subjectSort", sub.getSubjectSort().trim());
			}
		}
		
		hql.append(" order by fs.subject_Sort,fs.subject_Code");
		return listBySQLAliasToBean(hql.toString(), val, FcSubjectPoolVO.class);
	}
	
	
	/**
	 * 判断科目代码不存在<br>-----
	 */
	public boolean checkSubjectCode(String subjectCode){
		String hql = "select 1 from fc_subject_pool where subject_code = :subjectCode ";
		Map<String,String> val = new HashMap<String,String>();
		val.put("subjectCode", subjectCode);
		List list = createSQLQuery(hql, val).list();
		if(EmptyUtils.isEmptyList(list)){
			return true;
		}
		return false;
	}
	/**保存选择的初始化数据---------
	 * */
	public void saveSelectData(List<String> ids){
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into fc_subject_pool (id, subject_code, subject_name,balance_way,interest_sign,interest_rate,");
		sql.append(" show_level, level_num,leaf_sign,acc_type,father_id,valid_sign, subject_sort,rmk, predraw_sign ) ");
		sql.append(" select a.pool_id,a.subject_code, a.subject_name, a.balance_way,a.interest_sign,a.interest_rate,");
		sql.append(" a.show_level, a.level_num,a.leaf_sign, a.acc_type, a.pool_father_id, a.valid_sign, a.subject_sort,");
		sql.append(" a.rmk, a.predraw_sign from fc_subject_init a ");
		sql.append("  where not exists (select 1 from fc_subject_pool b where a.subject_code =  b.subject_code ) ");
		sql.append(" and ").append(getSQLIn(ids, 900, "a.id"));
		createSQLQuery(sql.toString()).executeUpdate();
	}
	/**
	 * 功能介绍 : 选择科目列表。<br>-------
	 */
	public DataTablesOutput<FcSubject> listSelectSubject(DataTablesInput input){
		StringBuilder sql = new StringBuilder();
		sql.append(" select s.id,s.subject_code as subjectCode,s.subject_name as subjectName,s.subject_sort as subjectSort, ");
		sql.append(" s.balance_way as balanceWay,s.acc_type as accType ");
		sql.append(" from fc_subject_init s ");
		sql.append(" where not exists (select 1 from fc_subject_pool p where p.subject_code = s.subject_code  ) ");
		sql.append(" order by s.subject_code ");
		return getDynamicBySQL(input,sql.toString(), FcSubject.class, "",false);
	}
	/**
	 * 功能介绍 : 根据开户种类查询科目<br>------
	 */
	public List<FcSubjectPoolDTO> findSubjectPoolByAccType(String accType, String valid){
		if(EmptyUtils.isEmpty(accType)){
			return null;
		}
		StringBuilder hql = new StringBuilder("from FcSubjectPool where accType = :accType");
		Map<String,String> values = new HashMap<String,String>();
		values.put("accType", accType);
		if(EmptyUtils.isNotEmpty(valid)){
			hql.append(" and validSign=:validSign");
			values.put("validSign", valid);
		}
		hql.append(" order by subjectCode");
		return BeanUtils.copyListProperties(createQuery(hql.toString(), values).list(), FcSubjectPoolDTO.class);
	}
	/**
	 * 功能介绍 : 根据计息标志查询科目<br>------
	 */
	public List<FcSubjectPoolDTO> findSubjectPoolByInterestSign(String interestSign){
		if(EmptyUtils.isEmpty(interestSign)){
			return null;
		}
		StringBuilder sql = new StringBuilder("select * from fc_subject_pool where interest_sign = :interestSign and valid_sign = 1 ");
		Map<String,String> values = new HashMap<String,String>();
		values.put("interestSign", interestSign);
		sql.append(" order by subject_code");
//		return BeanUtils.copyListProperties(createQuery(hql.toString(), values).list(), FcSubjectPoolDTO.class);
		return listBySQLAliasToBean(sql.toString(),values,FcSubjectPoolDTO.class);
	}
	
	/**
	 * 新增修改操作验证科目标号是否重复
	 * @param id
	 * @param subjectCode
	 * @return
	 */
	public boolean checkSubjectCode(String id,String subjectCode){
		String hql = "select 1 from fc_subject_pool where id != :id and subject_code = :subjectCode ";
		Map<String,String> val = new HashMap<String,String>();
		val.put("subjectCode", subjectCode);
		val.put("id", id);
		List list = createSQLQuery(hql, val).list();
		if(EmptyUtils.isEmptyList(list)){
			return true;
		}
		return false;
	}
	public List<FcSubjectPoolDTO> findSubjectPool(){
		String sql = "select * from fc_subject_pool where valid_sign = 1 ";
		return listBySQLAliasToBean(sql,null,FcSubjectPoolDTO.class);
	}
	/**
	 * 获取有效的资金池科目
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FcSubjectPool> listValidSubject(){
		return this.listBy("validSign", "1");
	}
	public List<FcSubjectPoolDTO> getFcSubjectPoolByIds(List<String> ids) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from fc_subject_pool a where 1=1 ");
		if(CollectionUtils.isNotEmpty(ids)){
			sql.append(" and ").append(getSQLIn(ids,900,"a.id"));
		}
		return listBySQLAliasToBean(sql.toString(),null,FcSubjectPoolDTO.class);
	}
}
