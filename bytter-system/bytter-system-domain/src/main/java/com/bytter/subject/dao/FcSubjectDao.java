package com.bytter.subject.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.*;
import com.bytter.subject.entity.FcSubject;
import com.bytter.subject.entity.FcSubjectInit;
import com.bytter.system.api.dto.FcInstrateDTO;
import com.bytter.subject.vo.FcSubjectInitVO;
import com.bytter.subject.vo.FcSubjectSortVo;
import com.bytter.subject.vo.FcSubjectVo;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Controller
 * @Description: 会计科目数据访问
 * @author 吕伟林
 * @date 2017-05-12 14:18:39
 * @version V1.0
 * TODO 迁移到基础模块
 */
@Repository
public class FcSubjectDao extends BaseDao<FcSubject,String>{
	/**
	 *
	 * 功能介绍 : 页面条件查询<br>
	 * @param sub 查询条件
	 * @return
	 * <b>日期：</b>2017-5-27<br>
	 * @version 1.0
	 * @author lvwl
	 */
	@SuppressWarnings("unchecked")
	public List<FcSubjectVo> listAllOrder(FcSubject sub){
		StringBuilder hql = new StringBuilder("select fs.*,fs.subject_name as subjectNameShow,ss.id as sortId from Fc_Subject fs left join Fc_Subject_Sort ss " +
				"on fs.sys_subject_code = ss.sys_subject_code where 1=1 ");
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
		return listBySQLAliasToBean(hql.toString(), val, FcSubjectVo.class);
	}
	/**
	 * 获取有效的会计科目
	 * @param fs 查询条件
	 * @return
	 * @author 吕伟林
	 * @date 2017-05-27
	 */
	@SuppressWarnings("unchecked")
	public List<FcSubjectVo> listValidSubject(FcSubject fs){
		StringBuilder hql = new StringBuilder();
		hql.append("select * from Fc_Subject where valid_Sign='1' ");
		Map<String, String> values = null;
		if(!EmptyUtils.isEmpty(fs)){
			values = new HashMap<String, String>();
			if(EmptyUtils.isNotEmpty(fs.getAccType())){
				hql.append(" and acc_Type =:accType");
				values.put("accType", fs.getAccType());
			}
		}
		hql.append(" order by subject_Code");
		return listBySQLAliasToBean(hql.toString(), values, FcSubjectVo.class);
	}
	/**
	 * 判断科目代码不存在<br>
	 * @param subjectCode 会计科目Code
	 * @return 不存在：true  <br> 存在：false <br>
	 * @author 吕伟林
	 * @date 2017-05-27
	 */
	public boolean checkSubjectCode(String subjectCode){
		String hql = "select 1 from fc_subject where subject_code = :subjectCode ";
		Map<String,String> val = new HashMap<String,String>();
		val.put("subjectCode", subjectCode);
		List list = createSQLQuery(hql, val).list();
		if(EmptyUtils.isEmptyList(list)){
			return true;
		}
		return false;
	}
	public boolean checkSubjectCode(String id,String subjectCode){
		String hql = "select 1 from fc_subject where id != :id and subject_code = :subjectCode ";
		Map<String,String> val = new HashMap<String,String>();
		val.put("subjectCode", subjectCode);
		val.put("id", id);
		List list = createSQLQuery(hql, val).list();
		if(EmptyUtils.isEmptyList(list)){
			return true;
		}
		return false;
	}
	public FcSubject getSubjectByCode(String subjectCode){
		String hql = "from FcSubject where subjectCode = :subjectCode ";
		Map<String,String> val = new HashMap<String,String>();
		val.put("subjectCode", subjectCode);
		List<FcSubject> list = createQuery(hql, val).list();
		if(EmptyUtils.isEmptyList(list)){
			return null;
		}
		return list.get(0);
	}
	/**
	 * 检查 不存在子科目<br>
	 * @param id  检查科目的ID
	 * @return 不存在：true <br> 存在：false <br>
	 * @author 吕伟林
	 * @date 2017-05-27
	 */
	public boolean checkChildNode(String id){
		String hql = "select 1 from FcSubject where fatherId = :fatherId ";
		Map<String,String> val = new HashMap<String,String>();
		val.put("fatherId", id);
		List list = createQuery(hql, val).list();
		if(EmptyUtils.isEmptyList(list)){
			return true;
		}
		return false;
	}
	/**
	 * 获取系统科目
	 * @return
	 * @author 吕伟林
	 * @date 2017-05-27
	 */
	@SuppressWarnings("unchecked")
	public List<FcSubjectSortVo> listSysSubject(){
		String sql = "select * from Fc_Subject_Sort";
		return listBySQLAliasToBean(sql.toString(), null, FcSubjectSortVo.class);
	}
	public List<FcSubject> listSubject(){
		String hql = "from FcSubject";
		return createQuery(hql).list();
	}
	/**
	 * 获取系统准备的<b>初始化系统科目<b>
	 * @return
	 * @author 吕伟林
	 * @date 2017-05-27
	 */
	@SuppressWarnings("unchecked")
	public List<FcSubjectInit> listFcSubjectInit(){
		String sql = "from FcSubjectInit";
		return createQuery(sql).list();
	}
	/**
	 * 获取系统准备的<b>初始化系统科目<b>包括外键对象
	 * @return
	 * @author 吕伟林
	 * @date 2017-05-27
	 */
	@SuppressWarnings("unchecked")
	public List<FcSubjectInit> listFcSubjectInitFetch(){
		String sql = "from FcSubjectInit init left join fetch init.sysSubject  order by init.subjectCode";
		return createQuery(sql).list();
	}
	/**
	 * 功能介绍 : 查看可初始化科目。<br>
	 * <b>日期：</b>2017年11月20日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<FcSubjectInitVO> listAllInitSubject(){
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.SUBJECT_CODE as subjectCode, a.SUBJECT_NAME as subjectName, ");
		sql.append(" a.SUBJECT_SORT as subjectSort,a.BALANCE_WAY as balanceWay,");
		sql.append(" a.SYS_SUBJECT_CODE as sysSubjectCode, b.SYS_SUBJECT_NAME as sysSubjectName, ");
		sql.append(" a.ACC_TYPE as accType,a.VALID_SIGN as validSign ");
		sql.append(" from FC_SUBJECT_INIT a ");
		sql.append(" left join  FC_SUBJECT_SORT b on a.SYS_SUBJECT_CODE = b.SYS_SUBJECT_CODE ");
		sql.append(" where not exists (select 1 from fc_subject c where c.subject_code = a.subject_code or c.id = a.id ) ");
		sql.append(" order by a.SUBJECT_CODE ");
		return listBySQLAliasToBean(sql.toString(), null, FcSubjectInitVO.class);
	}
	/**
	 * 导入系统提供的会计科目需要删除之前手动填写的数据
	 * @author 吕伟林
	 * @date 2017-05-27
	 */
	public void delAllSubject(){
		createSQLQuery("delete from FC_SUBJECT ").executeUpdate();
	}
	/**
	 *
	 * 功能介绍 : 根据id获取会计科目DTO对象<br>
	 * @param id
	 * @return
	 * <b>日期：</b>2017-5-27<br>
	 * @version 1.0
	 * @author lvwl
	 */
	public FcSubjectDTO findSubjectById(String id){
		if(EmptyUtils.isEmpty(id)){
			return null;
		}
		String sql = "select f.*,f.subject_name as subjectNameShow,s.id as sortId from Fc_Subject f left join fc_subject_sort s on f.sys_subject_code = s.sys_subject_code" +
				" where f.id = :id";
		Map<String,String> values = new HashMap<String,String>();
		values.put("id", id);
		FcSubjectDTO dto =null;
		List<FcSubjectDTO> fcSubject=listBySQLAliasToBean(sql, values, FcSubjectDTO.class);
		if(fcSubject.size() > 0){
			dto= fcSubject.get(0);
		}
		return dto;
	}
	/**
	 *
	 * 功能介绍 : 根据科目代码获取会计科目DTO对象<br>
	 * @param subjectCode 会计科目代码
	 * @return
	 * <b>日期：</b>2017-5-27<br>
	 * @version 1.0
	 * @author lvwl
	 */
	public FcSubjectDTO findSubjectByCode(String subjectCode){
		if(EmptyUtils.isEmpty(subjectCode)){
			return null;
		}
		String hql = "from FcSubject where subjectCode = :subjectCode";
		Map<String,String> values = new HashMap<String,String>();
		values.put("subjectCode", subjectCode);
		FcSubjectDTO dto = new FcSubjectDTO();
		BeanUtils.copyNotNullProperties(createQuery(hql.toString(), values).uniqueResult(), dto);
		return dto;
	}
	/**
	 *
	 * 功能介绍 : <br>
	 * @param sysSubjcetCode 系统科目代码
	 * @param valid 是否查询有效
	 * @return
	 * <b>日期：</b>2017-5-27<br>
	 * @version 1.0
	 * @author lvwl
	 */
	@SuppressWarnings("unchecked")
	public List<FcSubjectDTO> findSubjectBySysCode(String sysSubjcetCode,String valid){
		if(EmptyUtils.isEmpty(sysSubjcetCode)){
			return null;
		}
		StringBuilder hql = new StringBuilder("from FcSubject fs where fs.sysSubjectCode = :sysSubjcetCode");
		Map<String,String> values = new HashMap<String,String>();
		values.put("sysSubjcetCode", sysSubjcetCode);
		if(EmptyUtils.isNotEmpty(valid)){
			hql.append(" and fs.validSign=:validSign");
			values.put("validSign", valid);
		}
		hql.append(" order by subjectCode");
		return BeanUtils.copyListProperties(createQuery(hql.toString(), values).list(), FcSubjectDTO.class);
	}
	public List<FcSubjectDTO> queryAllSubject(String valid){
		StringBuilder hql = new StringBuilder("from FcSubject fs where 1=1 ");
		Map<String,String> values = new HashMap<String,String>();
		if(EmptyUtils.isNotEmpty(valid)){
			hql.append(" and fs.validSign=:validSign");
			values.put("validSign", valid);
		}
		hql.append(" order by subjectCode");
		return BeanUtils.copyListProperties(createQuery(hql.toString(), values).list(), FcSubjectDTO.class);
	}
	/**
	 *
	 * 功能介绍 : <br>
	 * @param accType 开户种类代码
	 * @param valid 是否查询有效
	 * @return
	 * <b>日期：</b>2017-5-27<br>
	 * @version 1.0
	 * @author lvwl
	 */
	@SuppressWarnings("unchecked")
	public List<FcSubjectDTO> findSubjectByAccType(String accType,String valid){
		if(EmptyUtils.isEmpty(accType)){
			return null;
		}
		StringBuilder hql = new StringBuilder("select * from Fc_Subject where BT_RTRIM(acc_Type) = :accType");
		Map<String,String> values = new HashMap<String,String>();
		values.put("accType", accType);
		if(EmptyUtils.isNotEmpty(valid)){
			hql.append(" and BT_RTRIM(valid_Sign)=:validSign");
			values.put("validSign", valid);
		}
		hql.append(" order by subject_Code");
		return listBySQLAliasToBean(hql.toString(),values,FcSubjectDTO.class);
	}
	/**
	 *
	 * 功能介绍 : <br>
	 * @param sort 科目类别代码
	 * @param valid 是否查询有效
	 * @return
	 * <b>日期：</b>2017-5-27<br>
	 * @version 1.0
	 * @author lvwl
	 */
	@SuppressWarnings("unchecked")
	public List<FcSubjectDTO> findSubjectBySort(String sort,String valid){
		if(EmptyUtils.isEmpty(sort)){
			return null;
		}
		StringBuilder hql = new StringBuilder("from FcSubject where subjectSort = :subjectSort");
		Map<String,String> values = new HashMap<String,String>();
		values.put("subjectSort", sort);
		if(EmptyUtils.isNotEmpty(valid)){
			hql.append(" and validSign=:validSign");
			values.put("validSign", valid);
		}
		hql.append(" order by subjectCode");
		return BeanUtils.copyListProperties(createQuery(hql.toString(), values).list(), FcSubjectDTO.class);
	}
	/**
	 *
	 * @param subjectCode
	 * @param exeDate
	 * @return
	 */
	public FcInstrateDTO getExistInstrate(String subjectCode, Date exeDate){
		HashMap<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from FC_INSTRATE fc where fc.subject_code = :subjectCode ");
		sql.append(" and fc.exe_date = (select max(exe_date) from  FC_INSTRATE t where t.subject_code = :subjectCode and t.exe_date <=:exeDate)");
		sql.append(" and fc.status=1 ");
		params.put("subjectCode", subjectCode);
		params.put("exeDate",exeDate );
		return getBySQLAliasToBean(sql.toString(),params, FcInstrateDTO.class);
	}
	/**
	 * <b>功能介绍：</b>根据系统科目和开户种类查询<br>
	 * <b>日期：</b>2017年7月14日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public FcSubjectDTO findSubjectByAccTypeSubCode(String sysSubjcetCode, String accType){
		if(EmptyUtils.isEmpty(sysSubjcetCode)||EmptyUtils.isEmpty(accType)){
			return null;
		}
		StringBuilder hql = new StringBuilder("from FcSubject where sysSubjectCode = :sysSubjcetCode and accType = :accType ");
		Map<String,String> params = new HashMap<String,String>();
		params.put("sysSubjcetCode", sysSubjcetCode);
		params.put("accType", accType);
		FcSubjectDTO dto = new FcSubjectDTO();
		BeanUtils.copyNotNullProperties(createQuery(hql.toString(), params).uniqueResult(), dto);
		return dto;
	}
	public List<FcSubject> listSubjectBySort(String sort){
		StringBuilder hql = new StringBuilder("from FcSubject where subjectSort = :sort and validSign=:validSign ");
		Map<String,String> params = new HashMap<String,String>();
		params.put("sort", sort);
		params.put("validSign", "1");
		return createQuery(hql.toString(), params).list();
	}
	/**
	 * 功能介绍 : 会计科目导入。<br>
	 * <b>日期：</b>2017年11月20日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void importInitSubject() {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into FC_SUBJECT (ID, SUBJECT_CODE, SUBJECT_NAME, BALANCE_WAY, ");
		sql.append(" INTEREST_SIGN, INTEREST_RATE, SHOW_LEVEL, LEVEL_NUM, ACC_TYPE, LEAF_SIGN, FATHER_ID, ");
		sql.append(" VALID_SIGN, SYS_SUBJECT_CODE, SUBJECT_SORT, RMK, MEASURE_UNIT, ");
		sql.append(" PREDRAW_SIGN, IS_CASH_ITEM, DEBIT_CASH_ITEM, CREDIT_CASH_ITEM) ") ;
		sql.append(" select a.ID,a.SUBJECT_CODE,a.SUBJECT_NAME,a.BALANCE_WAY, ");
		sql.append(" a.INTEREST_SIGN,a.INTEREST_RATE,a.SHOW_LEVEL,a.LEVEL_NUM,a.ACC_TYPE,a.LEAF_SIGN,a.FATHER_ID, ");
		sql.append(" a.VALID_SIGN,a.SYS_SUBJECT_CODE,a.SUBJECT_SORT,a.RMK,a.MEASURE_UNIT, ");
		sql.append(" a.PREDRAW_SIGN,a.IS_CASH_ITEM,a.DEBIT_CASH_ITEM,a.CREDIT_CASH_ITEM ");
		sql.append(" from FC_SUBJECT_INIT a");
		sql.append(" where not exists (select 1 from FC_SUBJECT b where a.SUBJECT_CODE = b.SUBJECT_CODE or a.id = b.id)");
		createSQLQuery(sql.toString()).executeUpdate();
	}
	public List<FcSubjectDTO> findSubjectByFatherId(){
		StringBuilder sql = new StringBuilder();
		sql.append("select * from fc_subject where 1=1 and valid_Sign = 1 ");
		sql.append(" and father_id in ('I','A','L','B','E','G')");
		sql.append(" order by subject_Code ");
		return listBySQLAliasToBean(sql.toString(),null,FcSubjectDTO.class);
	}
	public List<FcSubjectDTO> getSubjectNameAndIdByIds(List<String> ids){
		StringBuilder sql = new StringBuilder();
		sql.append("select s.id, s.subject_name as subjectName from fc_subject s where 1 = 1");
		if (ListUtils.isNotNullOrEmpty(ids)){
			sql.append(" and ").append(getSQLIn(ids, 900, "s.id"));
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("id", ids);
		List<FcSubjectDTO>  list = listBySQLAliasToBean(sql.toString(), param, FcSubjectDTO.class);
		if(list==null||list.size()==0){
			return null;
		}
		return list;
	}
	public List<FcSubjectDTO> getSubjectByIds(List<String> ids) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select s.* from fc_subject s where 1=1 ");
		sql.append(" and ").append(getSQLIn(ids,900,"s.id"));
		return listBySQLAliasToBean(sql.toString(),null,FcSubjectDTO.class);
	}
	/**
	 * 根据 subjectsCodes , 查询 fc_subject 表，返回 subjectName; k = subjectCode, v = subjectName
	 * @param subjectCodes
	 * @return
	 */
	public List<FcSubjectDTO> getSubjectNameBySubjectCode(List<String> subjectCodes) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select s.subject_code as subjectCode, s.subject_name as subjectName from fc_subject s where 1=1 ");
		sql.append(" and ").append(getSQLIn(subjectCodes,900,"s.subject_code"));
		return listBySQLAliasToBean(sql.toString(),null,FcSubjectDTO.class);
	}
	public String getSubjectNameById(String id) {
		String sql = "select a.subject_name from fc_subject a where a.id = :id";
		HashMap<String, String> params = Maps.newHashMap();
		params.put("id",id);
		Map<String,String> map = getBySQL(sql, params);
		return map.get("subject_name");
	}

	public List<FcSubjectDTO> getSubListByIds(List<String> subIds) {
		StringBuilder sql = new StringBuilder();
		sql.append("select s.* from fc_subject s where 1=1 and s.valid_Sign = 1  ");
		sql.append(" and ").append(SqlUtil.getInSql(subIds, "s.id", true));
		sql.append(" order by s.subject_Code ");
		return listBySQLAliasToBean(sql.toString(), null, FcSubjectDTO.class);
	}
}
