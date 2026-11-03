package com.bytter.biz.receipt.service;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.enums.EVisaTypeMessageEnum;
import com.bytter.biz.receipt.dao.EVisaTypeDao;
import com.bytter.biz.receipt.entity.EVisaType;
import com.bytter.biz.receipt.vo.EVisaTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
* @Title: Service
* @Description: 签章类型维护
* @author onlineGenerator
* @date 2017-06-05 10:52:28
* @version 1.0
*
*/
@Service("eVisaTypeService")
@Transactional
public class EVisaTypeService extends BaseService<EVisaType,String> {
	@Autowired
	private EVisaTypeDao eVisaTypeDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(EVisaType entity){
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
	* @param entity
	*/
 	public void save(EVisaType entity, ICurrentUser user){
 		if(StringUtils.isEmpty(entity.getNetId().trim())){
 			throw new ParameterException(EVisaTypeMessageEnum.NIS_EVISATYPE_MESSAGE_NETID_NULL);
 		}
 		if(StringUtils.isEmpty(entity.getBillType().trim())){
 			throw new ParameterException(EVisaTypeMessageEnum.NIS_EVISATYPE_MESSAGE_BILLTYPE_NULL);
 		}
 		List<String> billType = eVisaTypeDao.findBillType(entity.getNetId());
 		if(!billType.isEmpty()){
 			for(String bt : billType){
 	 			if(entity.getBillType().equals(bt)){
 	 				throw new ParameterException(EVisaTypeMessageEnum.NIS_EVISATYPE_MESSAGE_BILLTYPE_UNIQUE);
 	 			}
 	 		}
 		}
		entity.setCreateBy(user.getLoginName());
		entity.setCreateDate(DateUtils.getDate());
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(EVisaType entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(EVisaType entity, ICurrentUser user){
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<EVisaType, String> getEntityDao() {
		return eVisaTypeDao;
	}
	/**
	 * 根据付款ID获取电子回单信息
	 * @param id
	 * @return
	 */
	public EVisaType getById(String id){
		EVisaType eVisaType= eVisaTypeDao.getById(id);
//		CorpDTO corp=corpService.get(vo.getNetId());
//		eVisaType.setNetName(corp.getName());
		return eVisaType;
	}
	/**
	 * 根据id,状态判断是作废还是恢复单据
	 * @param id
	 * @param opType 0 作废 1 恢复
	 */
	public void updateOperate(String id,String opType){
		eVisaTypeDao.updateOperate(id,opType);
	}
	
	/**
	 * 查询电子回单类型列表
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput findListBysql(DataTablesInput input,ICurrentUser user){
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(
				corpService.getUserNetIds(ResourceConstant.NETBANK_RECEIPT_EVISATYPE_OP, CorpConstant.CORP_TYPE_CODE_NET,user.getId()), "e.net_id",
				true);
		sql.append(" and ").append(corpAuthSql);
		DataTablesOutput output = eVisaTypeDao.findListBysql(input,sql.toString());
		List<EVisaTypeVO> list =output.getData();
		for(EVisaTypeVO vo:list) {
			CorpDTO corp=corpService.get(vo.getNetId());
			vo.setNetName(corp.getName());
		}
		return output;
	}
	
	/**
	 * 根据付款ID获取电子回单信息
	 * @param id
	 * @return
	 */
	public EVisaTypeVO getByIdUnion(String id){
		EVisaTypeVO vo = eVisaTypeDao.getByIdUnion(id);
		CorpDTO corp=corpService.get(vo.getNetId());
		vo.setNetName(corp.getName());
		String billTypeName =dictService.get("eVisaType.billType",vo.getBillType());
		vo.setBillTypeName(billTypeName);
		String validSignName =dictService.get("eVisaType.validSign",vo.getValidSign());
		vo.setValidSignName(validSignName);
		return vo;
	}
	public EVisaTypeVO getVOByParams(String billType,String netId,String validSign){
		return eVisaTypeDao.getVOByParams(billType,netId,validSign);
	}
	public EVisaTypeVO getVOByParamsAndValid(String billType,String netId){
		return this.getVOByParams(billType,netId,"1");
	}
}
