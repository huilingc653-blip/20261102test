package com.bytter.biz.netbank.service;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.biz.netbank.dao.NisCorpBusDao;
import com.bytter.biz.netbank.entity.NisBusType;
import com.bytter.biz.netbank.entity.NisCorpBus;
import com.bytter.biz.netbank.vo.NisBusTypeVO;
import com.bytter.biz.netbank.vo.NisCorpBusVO;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpDeleteVerifyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: 单位业务类别对照表
* @author onlineGenerator
* @date 2017-05-18 16:36:11
* @version 1.0
*
*/
@Service("nisCorpBusService")
@DubboService
@Transactional
public class NisCorpBusService extends BaseService<NisCorpBus,String> implements ICorpDeleteVerifyService {
	@Autowired
	private NisCorpBusDao nisCorpBusDao;
	@Autowired
	private NisBusTypeService nisBusTypeService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IFbsItemService fbsItemService;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(NisCorpBus entity){
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
 	public void save(NisCorpBus entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(NisCorpBus entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(NisCorpBus entity){
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<NisCorpBus, String> getEntityDao() {
		return nisCorpBusDao;
	}

	/**
	 * 业务种类设置
	 *
	 * @param entity
	 * @param busIds
	 */
	public void assignBusCorp(NisCorpBus entity,List<String> busIds){
		String[] corpIds = entity.getCorpId().split(CommonConstant.COMMA_SPLIT);
		List<String> deleteBusTypes = new ArrayList<>();
		NisCorpBus corpBus = null;
		for(String corpId : corpIds){
    		List<NisCorpBus> list = nisCorpBusDao.listBy("corpId", corpId);
    		for (NisCorpBus bus : list){
    			if (!busIds.contains(bus.getBusId())){
    				//bus的BusID不在busIds中,表示之前存在,此次提交不存在,则是删除该业务类型,存入List
					if (nisCorpBusDao.getBusTypeUsedCount(bus.getBusId()) > 0){
						NisBusTypeVO nisBusTypeVO = nisBusTypeService.getByUnion(bus.getBusId());
						if (null != nisBusTypeVO) {
							deleteBusTypes.add("(" + nisBusTypeVO.getBusType() + ")" + nisBusTypeVO.getBusTypeName());
						}
					}
				}
			}
			if (deleteBusTypes.size() > 0) {
    			//拼接已经被使用的业务类型
				StringBuilder sb = new StringBuilder();
				for (String error : deleteBusTypes){
					sb.append(error).append(",");
				}
				throw new ParameterException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_NOTREMOVE,sb.toString().substring(0,sb.toString().length()-1));
			}
			//先删除单位已分配的业务种类
			for(NisCorpBus bus : list){
				this.deleteEntity(bus);
			}
			for(String busId : busIds){
				corpBus = new NisCorpBus();
				corpBus.setCorpId(corpId);
				corpBus.setBusId(busId);
				super.saveEntity(corpBus);
			}
		}
	}
	/**
	 * 根据单位ID获取分配的业务种类
	 *
	 * @param corpId
	 * @return
	 */
	public List<NisCorpBusVO> getByBusType(String corpId) {
		return nisCorpBusDao.getByBusType(corpId);
	}

	/**
	 * 根据单位ID,收付款标志获取分配的业务种类
	 * @param corpId
	 * @return
	 */
	public List<NisCorpBusVO> getByBusType(String corpId, String cdSign){
		return nisCorpBusDao.getByBusType(corpId, cdSign);
	}
	/**
	 * 根据单位ID,收付款标志获取分配的业务种类
	 * @param corpId
	 * @return
	 */
	public List<String> getBusIdsByCorpId(String corpId){
		return nisCorpBusDao.getBusIdsByCorpId(corpId);
	}
	/**
	 * 根据收付款标志获取分配的业务种类
	 * @param cdSign
	 * @return
	 */
	public List<NisCorpBusVO> getNisCorpBusByCdSign(String cdSign){
		return nisCorpBusDao.getByBusTypeByCdSign(cdSign);
	}

	/**
	 * 获取已分配的单位
	 * @return
	 */
	public HashMap<String,String> getBusTypeCorps(){
		HashMap<String,String> map = new HashMap<String,String>();
		List<NisCorpBus> list = nisCorpBusDao.listAll();
		for(NisCorpBus n : list){
			if(!map.containsKey(n.getCorpId())){
				map.put(n.getCorpId(), n.getCorpId());
			}
		}
		return map;
	}
	/**
	 * 获取已分配的单位
	 * @return
	 */
	public List<String> getCorpIdList(){
		return nisCorpBusDao.getCorpIdList();
	}

	/**
	 * 根据业务种类id查出单位
	 * @param busId
	 * @return
	 */
	public List<NisCorpBus> getByBusCorp(String busId){
		return nisCorpBusDao.getByBusCorp(busId);
	}
	/**
	 * 根据业务类型是否已分配
	 * @param busId		业务类型ID
	 * @return
	 */
	public boolean isUsedBusType(String busId) {
		List<NisCorpBus> list = this.getByBusCorp(busId);
		return EmptyUtils.isEmptyList(list) ? false: true;
	}
	/**
	 * 判断单位是否有分配付款方式权限
	 * @param corpId
	 * @param cdSign
	 * @param busTypeName
	 * @return
	 */
	public NisCorpBusVO getBusTypeByName(String corpId, String cdSign, String busTypeName) {
		List<NisCorpBusVO> list = nisCorpBusDao.getByBusType(corpId,cdSign);
		if (!EmptyUtils.isEmptyList(list)) {
			for (NisCorpBusVO vo : list) {
				if (busTypeName.equals(vo.getBusTypeName())) {
					return vo;
				}
			}
		}
		return null;
	}
	/**
	 * 查询单位业务种类设置信息
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput<NisCorpBusVO> getDataTablesUnion(DataTablesInput dataTablesInput, String userId) {
		List<String> authCorpIdList = corpService.getUserCorpIds(ResourceConstant.NETBANK_BASICDATA_NISCORPBUS_APPLY, userId);
		if (CollectionUtils.isEmpty(authCorpIdList)) {
			return new DataTablesOutput<>();
		}
		DataTablesOutput<NisCorpBusVO> output =  this.nisCorpBusDao.getDataTablesUnion(dataTablesInput, authCorpIdList);
		this.setBusTypeName(output);
		return output;
	}
	/**
	 * 设置业务种类名称
	 * @param output
	 */
	public void setBusTypeName(DataTablesOutput output){
		if(null != output.getData()){
			List<NisBusType> listBusType = nisBusTypeService.getValidBusType();
			HashMap<String, NisBusType> busTypeMap = new HashMap<>();
			for (NisBusType nisBusType:listBusType) {
				busTypeMap.put(nisBusType.getId(), nisBusType);
			}
			for(int i = 0;i<output.getData().size();i++){
				NisCorpBusVO vo = (NisCorpBusVO)output.getData().get(i);
				CorpDTO corpDTO = corpService.get(vo.getCorpId());
				if(null != corpDTO){
					vo.setCorpCode(corpDTO.getCode());
					vo.setCorpName(corpDTO.getName());
					vo.setCorpSort(corpDTO.getSort());
				}
				List<NisCorpBus> list = this.nisCorpBusDao.listBy("corpId", vo.getCorpId());
 				StringBuilder busTypeName = new StringBuilder();
				StringBuilder busType = new StringBuilder();
				StringBuilder busIds = new StringBuilder();
				for(NisCorpBus corpBus:list){
					NisBusType nisBusType = busTypeMap.get(corpBus.getBusId());
					if (nisBusType != null) {
						if (StringUtils.isBlank(busTypeName)) {
							busTypeName.append(nisBusType.getBusTypeName());
							busType.append(nisBusType.getBusType());
							busIds.append(corpBus.getBusId());
						} else {
							busTypeName.append(CommonConstant.COMMA_SPLIT).append(nisBusType.getBusTypeName());
							busType.append(CommonConstant.COMMA_SPLIT).append(nisBusType.getBusType());
							busIds.append(CommonConstant.COMMA_SPLIT).append(corpBus.getBusId());
						}
						//数据超长时直接跳出
						if (busTypeName.length() > 100) {
							busTypeName.append("...");
							break;
						}
					}
				}
				vo.setBusTypeName(busTypeName.toString());
				vo.setBusType(busType.toString());
				vo.setBusId(busIds.toString());
			}
		}
	}
	/**
	 * 获取业务种类
	 * @param corpId
	 */
	public String findBusTypeName(String corpId){
		List<String> list = this.nisCorpBusDao.getByBusTypeList(corpId);
		StringBuilder busType = new StringBuilder();
		for(int j = 0;j<list.size();j++){
			if(j == 0){
				busType.append(list.get(j));
			}else{
				busType.append(CommonConstant.COMMA_SPLIT).append(list.get(j));
			}
		}
		return busType.toString();
	}
	/**
	 * 根据单位ID,收付款标志,系统标志获取业务类别
	 * @param corpId
	 * @return
	 */
	public List<NisCorpBusVO> getBySysBusType(String corpId, String cdSign,String sysBusType) {
		List<NisCorpBusVO> list = this.nisCorpBusDao.getBySysBusType(corpId,cdSign,sysBusType);
		if(CollectionUtils.isEmpty(list)) {
    		return list;
    	}
		List<String> codes = list.stream().map(NisCorpBusVO::getItemCode).collect(Collectors.toList());
		List<String> stringList=codes.stream().filter(e->e!=null).collect(Collectors.toList());
		HashSet<String> set=new HashSet<>(stringList);
		List<com.bytter.biz.budget.spi.vo.FbsItemVO> listvo = fbsItemService.getFbsItemVOS(set);
		if(codes.size()==listvo.size()) {
			return list;
		}else {
			Map<String, String> map = listvo.stream().collect(Collectors.toMap(com.bytter.biz.budget.spi.vo.FbsItemVO::getItemCode,com.bytter.biz.budget.spi.vo.FbsItemVO::getItemCode));
			Iterator<NisCorpBusVO> it = list.iterator();
			String currentItemCode = null;
			while (it.hasNext()) {
				String itemCode = it.next().getItemCode();
				if (currentItemCode == null) {
					currentItemCode = itemCode;
				} else {
					String itemNextCode = itemCode;
					if (null == currentItemCode || null == map.get(currentItemCode) || !map.get(currentItemCode).equals(itemNextCode)) {
						it.remove();
					}
					currentItemCode = null;
				}
			}
		}
		return list;
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有设置业务种类，不允许删除！
			int num = this.nisCorpBusDao.getBusTypeCountByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_BUSTYPE,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DEL_BUSTYPE_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
	/**
	 * 根据单位保存单位业务种类
	 * @param corpId
	 * @param busId
	 */
	public void saveByCorpId(String corpId, String busId, ICurrentUser user) {
		//先删除业务种类已分配的单位
		this.nisCorpBusDao.deleteByBusId(busId);

		if (StringUtils.isNotBlank(corpId)) {
			String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
			List<NisCorpBus> list = new ArrayList<>();
			for (String id:corpIds) {
				NisCorpBus entity = new NisCorpBus();
				entity.setCorpId(id);
				entity.setBusId(busId);
				entity.setCreateBy(user.getLoginName());
				entity.setCreateDate(DateUtils.getDate());
				list.add(entity);
			}
			this.batchSave(list);
		}
	}
	/**
	 * 查询单位业务种类设置信息
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput<NisCorpBusVO> getDataTablesViewUnion(DataTablesInput dataTablesInput) {
		DataTablesOutput<NisCorpBusVO> output = this.nisCorpBusDao.getDataTablesViewUnion(dataTablesInput);
		List<NisCorpBusVO> list = output.getData();
		for (NisCorpBusVO vo : list) {
			vo.setCdSignStr(dictService.get("nisBusType.cdSign", vo.getCdSign()));
		}
		return output;
	}


	public List<NisCorpBusVO> getByBusTypeForAll(){
		return nisCorpBusDao.getByBusTypeForAll();
	}
}
