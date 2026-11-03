package com.bytter.subject.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.subject.dao.FcSubjectDao;
import com.bytter.subject.dao.FcSubjectPoolDao;
import com.bytter.subject.entity.FcSubject;
import com.bytter.subject.entity.FcSubjectInit;
import com.bytter.subject.entity.FcSubjectPool;
import com.bytter.subject.vo.FcSubjectPoolVO;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.FcSubjectPoolDTO;
import com.bytter.system.api.enums.ParamConfigMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: Controller  
 * @Description: 资金池会计科目
 * @author zhengxn
 * @date 2019-4-30
 * @version V1.0   
 * TODO
 */
@Service("fcSubjectPoolService")
@Transactional
public class FcSubjectPoolService extends BaseService<FcSubjectPool,String>  {
	@Autowired
	private FcSubjectPoolDao fcSubjectPoolDao;
	@Autowired
	private IDictService dictServiceI;
	@Autowired
	private FcSubjectInitService fcSubjectInitService;
	@Autowired
	private FcSubjectDao fcSubjectDao;
	
	/**
	 * 功能介绍 : 获取树形结构的会计科目 ,会将 <b>科目类型<b> 加入树形<br>	------
	 */
	public List<FcSubjectPoolVO> listTreeData(FcSubjectPool sub){
		List<FcSubjectPoolVO> paran = new ArrayList<FcSubjectPoolVO>();
		List<FcSubjectPoolVO> list = fcSubjectPoolDao.listAllOrder(sub);
		String subSort = sub.getSubjectSort()!=null && sub.getSubjectSort().equals(" ")?"":sub.getSubjectSort();
		//空查询条件 增加科目类别在列表
		if(StringUtils.isEmpty(sub.getSubjectCode()) && StringUtils.isEmpty(sub.getSubjectName()) && StringUtils.isEmpty(subSort)){
			//获取字典表 科目类型
			for (DictDTO bean : dictServiceI.list("Admin.SubjectSort")) {
				FcSubjectPoolVO fs = new FcSubjectPoolVO();
				fs.setId(bean.getCode());
				fs.setSubjectName(bean.getName());
				fs.setFatherId("");
				fs.setSubjectSort(bean.getCode());
				if(!bean.getCode().equals("G")){
					paran.add(fs);
				}
			}
		}
		paran.addAll(list);
		return getTree(paran,null);
	}
	
	/**
	 * 功能介绍 : 获取树形数据，父ID未查询到默认首级<br>	------
	 */
	public List<FcSubjectPoolVO> getTree(List<FcSubjectPoolVO> list,FcSubjectPoolVO fs){
		List<FcSubjectPoolVO> treeList = new ArrayList<FcSubjectPoolVO>();
		for (FcSubjectPoolVO sub : list) {
			String parentId = sub.getParentId();
			List<FcSubjectPoolVO> childrens = new ArrayList<FcSubjectPoolVO>();
			if((EmptyUtils.isEmpty(fs)) || (!EmptyUtils.isEmpty(fs) && EmptyUtils.isNotEmpty(parentId) && fs.getId().equals(parentId))){
				//补充上有父ID去没有父节点的数据
				if(EmptyUtils.isEmpty(fs) && EmptyUtils.isNotEmpty(parentId)){
					if(checkParentNode(list,parentId)){
						continue;
					}
				}
				childrens = getTree(list,sub);
				
				treeList.add(sub);
				if(!EmptyUtils.isEmptyList(childrens)){
					treeList.addAll(childrens);
				}
			}
		}
		return treeList;
	}
	
	/**
	 * 功能介绍 : 判断集合list元素id<br>-------
	 */
	public boolean checkParentNode(List<FcSubjectPoolVO> list,String id){
		for (FcSubjectPoolVO fcSubject : list) {
			if(fcSubject.getId().equals(id)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 功能介绍 : 可选择科目列表。<br>-------
	 */
	public DataTablesOutput<FcSubject> listSelectSubject(DataTablesInput input){
		return fcSubjectPoolDao.listSelectSubject(input);
	}
	/**
	 * 功能介绍 : 保存选择的会计科目------
	 */
	public void saveSelectSubject(List<String> ids){
		for (String id:ids) {
			FcSubjectInit init = fcSubjectInitService.get(id,true);
			if(!("A".equals(init.getPoolFatherId()) || "E".equals(init.getPoolFatherId())
					|| "G".equals(init.getPoolFatherId()) || "I".equals(init.getPoolFatherId())
					|| "L".equals(init.getPoolFatherId()))){
				FcSubjectPool pool = fcSubjectPoolDao.get(init.getPoolFatherId(),true);
				if(pool == null){
					throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_NOT_FATHER,(init.getSubjectCode()+"-"+init.getSubjectName()));
				}
			}
		}
		fcSubjectPoolDao.saveSelectData(ids);
	}
	
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcSubjectPool entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(FcSubjectPool entity, String userId){
 		entity.setCreateBy(userId);
 		entity.setValidSign("1");
 		entity.setCreateTime(new Date());
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcSubjectPool entity, String userId){
		entity.setUpdateBy(userId);
		entity.setUpdateTime(new Date());
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcSubjectPool entity, String userId){
 		if(EmptyUtils.isEmpty(entity.getId())){
 			entity.setCreateBy(userId);
 			entity.setCreateTime(new Date());
 		}else{
 			entity.setUpdateBy(userId);
 			entity.setUpdateTime(new Date());
 		}
 		
 		super.saveOrUpdate(entity);
 		this.fcSubjectPoolDao.flush();
 	}
	public List<FcSubjectPoolDTO> findSubjectPoolByInterestSign(){
 		String interestSign = "1";
		return fcSubjectPoolDao.findSubjectPoolByInterestSign(interestSign);
	}
	
	
 	/******************    验证 begin  ******************/
 	/**
 	 
 	 * 功能介绍 : 添加或修改科目验证<br>
	 *     */
 	public void checkAddSubject(FcSubjectPool entity){
 		
		if(EmptyUtils.isEmpty(entity.getId())){
			if(EmptyUtils.isEmpty(entity.getSubjectCode())){
	 			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_NULL);
	 		}else if(!fcSubjectPoolDao.checkSubjectCode(entity.getSubjectCode())){
	 			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_REPEAT);
 			}
		}else{//修改
			if(!fcSubjectPoolDao.checkSubjectCode(entity.getId(),entity.getSubjectCode())){
	 			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_REPEAT);
 			}
		}
 		
 		if(EmptyUtils.isEmpty(entity.getSubjectName())){
 			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_SUBJECTNAME_NULL);
 		}
 		
 		if(EmptyUtils.isEmpty(entity.getBalanceWay())){
 			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_BALANCEWAY_NULL);
 		}
 		
 		if(EmptyUtils.isEmpty(entity.getInterestSign())){
 			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_INTERESTSIGN_NULL);
 		}
 		
 		if(EmptyUtils.isEmpty(entity.getAccType())){
 			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_ACCTYPE_NULL);
 		}
 	}
	
 	
 	/******************    验证 end  ******************/
 	
    @Override
	public BaseDao<FcSubjectPool, String> getEntityDao() {
		return fcSubjectPoolDao;
	}
	/**
	 * 根据账户类别过滤有效的资金池科目
	 * @param accType
	 * @param valid
	 * @return
	 */
	public List<FcSubjectPoolDTO> findSubjectPoolByAccType(String accType, String valid) {
		return fcSubjectPoolDao.findSubjectPoolByAccType(accType, valid).stream().map(fcSubjectPool -> {
			fcSubjectPool.setShowName("(" +fcSubjectPool.getSubjectCode()+ ") " +  fcSubjectPool.getSubjectName() );
			return fcSubjectPool;
		}).collect(Collectors.toList());
	}
	/**
	 * 获取有效的资金池科目<br>
	 * @return
	 */
	public List<FcSubjectPool> listValidSubject(){
		return fcSubjectPoolDao.listValidSubject();
	}
}
