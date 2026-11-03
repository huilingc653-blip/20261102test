package com.bytter.biz.allocation.service;
import com.bytter.biz.allocation.dao.ExcRulesManageDao;
import com.bytter.biz.allocation.entity.ExcRulesManage;
import com.bytter.biz.allocation.vo.ExcRulesManageAdCorpVO;
import com.bytter.biz.allocation.vo.ExcRulesManagePayerAccNoVO;
import com.bytter.biz.allocation.vo.ExcRulesManageVO;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.enums.AllocationMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author zhoup
 * @Description 例外规则表
 * @date 2018/2/6
 */
@Service("excRulesManageService")
@Transactional
public class ExcRulesManageService extends BaseService<ExcRulesManage, String> {
	@Autowired
	private ExcRulesManageDao excRulesManageDao;
	@Autowired
	private ExcRulesManageService excRulesManageService;
	@DubboReference(check = false)
	private ICorpService corpService;
	
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(ExcRulesManage entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	@Override
	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param excRulesManageVO
	*/
 	public void save(ExcRulesManageVO excRulesManageVO, ICurrentUser user){
 		List<ExcRulesManagePayerAccNoVO> notNullPayerAccNos=new ArrayList<ExcRulesManagePayerAccNoVO>();
 		List<ExcRulesManagePayerAccNoVO> payerAccNos=excRulesManageVO.getPayerAccNos();
 		for(ExcRulesManagePayerAccNoVO excPayerNoVo:payerAccNos){
 			//剔除付款账号为空的项
 			if(!StringUtils.isBlank(excPayerNoVo.getPayerAccNo())){
 				notNullPayerAccNos.add(excPayerNoVo);
 			}
 		}
 		for(int i=0;i<notNullPayerAccNos.size()-1;i++){
 			for(int j=i+1;j<notNullPayerAccNos.size();j++){
 				//判断同时新增的付款账号是否相同
 				if((notNullPayerAccNos.get(i).getPayerAccNo()).equals(notNullPayerAccNos.get(j).getPayerAccNo())){
 					throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_EXCRULESMANAGE_PAYERNOREAPEAT, notNullPayerAccNos.get(i).getPayerAccNo());
 				}
 			}
 		}
 		for(int i=0;i<notNullPayerAccNos.size();i++){
 			if((notNullPayerAccNos.get(i).getPayerAccNo()).equals(excRulesManageVO.getPayeeAccNo())){
 				//判断收付款账号是否相同
 				throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_EXCRULESMANAGE_PAYERNOEQUALPAYEENO, notNullPayerAccNos.get(i).getPayerAccNo());
 			}
 			List<ExcRulesManage> list=excRulesManageDao.getByPayeeNoAndPayerNo(excRulesManageVO.getPayeeAccNo(), notNullPayerAccNos.get(i).getPayerAccNo());
 			if(!list.isEmpty()){
 				//判断此条数据是否已存在
 	 			throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_EXCRULESMANAGE_PAYERNOEXIST,notNullPayerAccNos.get(i).getPayerAccNo(),excRulesManageVO.getPayeeAccNo());
 	 		} 	
 		}
 		//	保存数据
 		if(!notNullPayerAccNos.isEmpty()){
 			List<ExcRulesManage> list=BeanUtils.copyListProperties(notNullPayerAccNos, ExcRulesManage.class);
 			for (ExcRulesManage excRulesManage: list) {
 				BeanUtils.copyProperties(excRulesManageVO, excRulesManage);
 				excRulesManage.setCreateBy(user.getLoginName());
 				excRulesManage.setCreateDate(DateUtils.getDate());				
			}
 		    excRulesManageService.batchSave(list);
 		}
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(ExcRulesManage entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(ExcRulesManage entity, ICurrentUser user){
 		entity.setUpdateBy(user.getLoginName());
 		entity.setUpdateDate(DateUtils.getDate());
 		super.saveOrUpdate(entity);
 	}
     
	@Override
	public BaseDao<ExcRulesManage, String> getEntityDao() {
		return excRulesManageDao;
	}
	
	/**
	 * 根据规则编码获取策略信息
	 * @param filterCode
	 * @return
	 */
	public ExcRulesManage findByCode(String filterCode){
		return excRulesManageDao.getUniqueBy("gatherPolicyCode", filterCode);
	}
	
	/**
	 * 根据收款账号ID获取收款账号
	 * @param payerAccId
	 * @return
	 */
	public List<ExcRulesManage> findBypayerAccId(String payerAccId){
		return  excRulesManageDao.getExcRulesManageByPayerAccId(payerAccId);
	}
	
	/**
	 * 根据ID获取获取详细信息
	 * @param id
	 * @return
	 */
	public ExcRulesManage findById(String id){
		return  super.get(id, true);
	}
	
	/**
	 * 根据DataTableInput获取获取详细信息
	 * @param dataTableInput,user
	 * @return
	 */
	public DataTablesOutput<ExcRulesManageAdCorpVO> getDataTablesUnion(DataTablesInput dataTableInput,ICurrentUser user){
		StringBuilder sql = new StringBuilder();
		String resource = ResourceConstant.NETBANK_ALLOCATION_EXCRULESMANAGE_QUERY;
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "b.corp_Id", true);
		sql.append(" and ").append(corpAuthSql);
		DataTablesOutput<ExcRulesManageAdCorpVO> output = excRulesManageDao.getDataTablesUnion(dataTableInput,sql.toString());
		List<ExcRulesManageAdCorpVO> list = output.getData();
		if(list.size()>0) {
			List<CorpDTO> corpList = corpService.listUserCorp(resource, user.getId());
			Map<String, CorpDTO> corpMap = corpList.stream().collect(Collectors.toMap(CorpDTO::getId, Function.identity()));
			for (ExcRulesManageAdCorpVO vo : list) {
				CorpDTO corp = corpMap.get(vo.getCorpId());
				vo.setCode(corp.getCode());
				vo.setCorpName(corp.getName());
			}
		}
		return output;
	}
	
	/**
	 * 批量删除对象
	 * @param ids
	 */
	public void batchDelete(String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			ExcRulesManage  excRulesManage= this.get(id);
			this.deleteEntity(excRulesManage);
		}
	}
	/**
	 * 根据ID获取获取详细信息及网点单位，网点代码
	 * @param id
	 * @return
	 */
	public ExcRulesManageAdCorpVO getByUnion(String id){
		ExcRulesManageAdCorpVO excRulesManageAdCorpVO = excRulesManageDao .getByUnion(id);
		CorpDTO corpDTO = corpService.get(excRulesManageAdCorpVO.getCorpId());
		if(!EmptyUtils.isEmpty(corpDTO)){
			excRulesManageAdCorpVO.setCode(corpDTO.getCode());
			excRulesManageAdCorpVO.setCorpName(corpDTO.getName());
		}
		return excRulesManageAdCorpVO;
	}
	
	/**
	 * 根据收付款账号进行校验
	 * @param payeeNo,payerNo
	 * @return
	 */
	public List<ExcRulesManage> getByPayeeNoAndPayerNo(String payeeNo,String payerNo){
		return excRulesManageDao .getByPayeeNoAndPayerNo(payeeNo, payerNo);
	}
	
	
}
