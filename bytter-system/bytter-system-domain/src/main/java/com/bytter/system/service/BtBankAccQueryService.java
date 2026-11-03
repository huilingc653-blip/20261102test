package com.bytter.system.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BytterAESUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.SysResourceConstant;
import com.bytter.system.api.dto.BisAccBalDTO;
import com.bytter.system.api.dto.CellManageDto;
import com.bytter.system.api.dto.OperationAreaDTO;
import com.bytter.system.api.dto.RegionDTO;
import com.bytter.system.api.enums.BankInfoMessageEnum;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.dao.BtBankAccCommonDao;
import com.bytter.system.dao.BtBankAccQueryDao;
import com.bytter.system.entity.BtBankAcc;
import com.bytter.system.entity.BtBankAccApp;
import com.bytter.system.entity.OperationArea;
import com.bytter.system.entity.SystemProvince;
import com.bytter.system.vo.BtBankAccGroupVO;
import com.bytter.system.vo.BtBankAccQueryVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@Service("btBankAccQueryService")
@Transactional
public class BtBankAccQueryService extends BaseService<BtBankAcc, String> {
	@Autowired
	private BtBankAccQueryDao btBankAccQueryDao;
	@Autowired(required=false)
	private ICorpService corpServiceI;
	@Autowired
	private IRegionService regionServiceI;
	@Autowired
	private IOperationAreaService operationAreaService;
	@Autowired
	private BtBankAccCommonDao btBankAccCommonDao;
	@DubboReference(check = false)
	private ICellManageService cellManageService;
	@Autowired
	private SystemProVinceService systemProVinceService;
	@Autowired
	private IBtBankAccCommonService btBankAccCommonServiceI;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(BtBankAcc entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(BtBankAcc entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(BtBankAcc entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(BtBankAcc entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<BtBankAcc, String> getEntityDao() {
		return btBankAccQueryDao;
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BtBankAccVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StopWatch stopWatch = new StopWatch("银行账户查询");
		stopWatch.start("getCellManageByCodeOrName1");
		CellManageDto region1 = cellManageService.getCellManageByCodeOrName1("6000", "大区");
		stopWatch.stop();
		stopWatch.start("corpAuthSql");
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(
				corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCQUERY_QUERY, user.getId()), "acc.corp_id",
				true);
		sql.append(" and ").append(corpAuthSql);
		stopWatch.stop();
		stopWatch.start("getDataTablesUnion");
		String curIds = String.valueOf(dataTablesInput.getFormdata().get("ins_curId"));
		DataTablesOutput<BtBankAccVO> output = null;
		//多选币别
		if (!"null".equals(curIds)&&StringUtils.isNotEmpty(curIds)) {
			//处理币别的过滤 筛选出 有对应币别的银行账号
			curIds = Arrays.stream(curIds.split(","))
					.map(String::trim)
					.filter(StringUtils::isNotBlank)
					.map(id -> "\"" + id + "\"")
					.collect(Collectors.joining(","));
			sql.append(" and ")
					.append("exists (select 1 from bt_bank_acc_cur acccur where acccur.BANK_ACC_ID = acc.id " +
							"and acccur.CUR_ID  in (" + curIds + "))");
		}
		dataTablesInput.getFormdata().remove("ins_curId");
		output = btBankAccQueryDao.getDataTablesUnion(dataTablesInput, sql.toString(),region1.getId());

		btBankAccCommonServiceI.setCurName(output);
		stopWatch.stop();
		//查询营运区维护数据
		stopWatch.start("getOperationArea");
		List<OperationAreaDTO> operationArea = operationAreaService.getOperationArea();
		HashMap<String, String> map = new HashMap<>();
		for (OperationAreaDTO operationAreaDTO : operationArea) {
			map.put(operationAreaDTO.getOperationAreaCode(),operationAreaDTO.getOperationAreaName());
		}
		stopWatch.stop();
		stopWatch.start("getRegionMap");
		Map<String, RegionDTO> regionMap = regionServiceI.getRegionMap();
		stopWatch.stop();
		stopWatch.start("getByBankAcc");
		List<BtBankAccVO> voList = output.getData();

		List<BtBankAccGroupVO> accGroupList = btBankAccQueryDao.getBankAccGroupData();
		Map<String, String> accGroupMap = new HashMap<>();
		for (BtBankAccGroupVO fcBankAccGroup : accGroupList) {
			accGroupMap.put(fcBankAccGroup.getAccGroupCode(), fcBankAccGroup.getAccGroupName());
		}

		List<String> bankAccNoList = voList.stream().map(BtBankAccVO::getBankAcc).collect(Collectors.toList());
		List<BisAccBalDTO> bankAccList = btBankAccQueryDao.getByBankAcc(bankAccNoList);
		Map<String, List<BisAccBalDTO>> bankAccMap = bankAccList.stream().collect(Collectors.groupingBy(BisAccBalDTO::getBankAcc));
		stopWatch.stop();
		stopWatch.start("数据组装");
		for(BtBankAccVO dto:voList) {
			RegionDTO region = regionMap.get(dto.getCity());
			if (region != null) {
				dto.setProvName(region.getProvName());
				dto.setCityName(region.getName());
			}
//			handAccGroupData(dto, accGroupMap);
			//查询营运区code对应的name
			String parentArea = dto.getOperationAreaID();
			if (EmptyUtils.isNotEmpty(parentArea)){
				String[] areaList = parentArea.split(",");
				StringBuilder operationAreaName = new StringBuilder();
				for (String area : areaList) {
					if (map.containsKey(area)){
						operationAreaName.append(map.get(area)).append(",");
					}
				}
				dto.setOperationAreaName(operationAreaName.toString());
			}
			List<BisAccBalDTO> bankAccBalList = bankAccMap.get(dto.getBankAcc());
			Optional.ofNullable(bankAccBalList).ifPresent(listData -> {
				Optional<BisAccBalDTO> first = listData.stream().filter(item -> ObjectUtil.equal(item.getCurId(), dto.getCurId())).distinct().findFirst();
				first.ifPresent(bisAccBalDTO -> dto.setRealTimeBal(bisAccBalDTO.getBal()));
			});
			// BisAccBalDTO bisAccBalDTO = btBankAccQueryDao.getByBankAcc(dto.getBankAcc(),dto.getCurId());
			// if(!EmptyUtils.isEmpty(bisAccBalDTO)){
			// 	dto.setRealTimeBal(bisAccBalDTO.getBal());
			// }
		}
		stopWatch.stop();
		logger.error(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
		return output;
	}


	public DataTablesOutput<BtBankAccVO> getDataTablesUnionWithAllCurId(DataTablesInput dataTablesInput, ICurrentUser user) {
		StopWatch stopWatch = new StopWatch("银行账户查询");
		stopWatch.start("getCellManageByCodeOrName1");
		CellManageDto region1 = cellManageService.getCellManageByCodeOrName1("6000", "大区");
		stopWatch.stop();
		stopWatch.start("corpAuthSql");
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(
				corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCQUERY_QUERY, user.getId()), "acc.corp_id",
				true);
		sql.append(" and ").append(corpAuthSql);
		stopWatch.stop();
		stopWatch.start("getDataTablesUnion");
		String curIds = String.valueOf(dataTablesInput.getFormdata().get("ins_curId"));
		DataTablesOutput<BtBankAccVO> output = null;
		//多选币别
		if (!"null".equals(curIds)&&StringUtils.isNotEmpty(curIds)) {
			//处理币别的过滤 筛选出 有对应币别的银行账号
			curIds = Arrays.stream(curIds.split(","))
					.map(String::trim)
					.filter(StringUtils::isNotBlank)
					.map(id -> "\"" + id + "\"")
					.collect(Collectors.joining(","));
			sql.append(" and ")
					.append("exists (select 1 from bt_bank_acc_cur acccur where acccur.BANK_ACC_ID = acc.id " +
							"and acccur.CUR_ID  in (" + curIds + "))");
		}
		dataTablesInput.getFormdata().remove("ins_curId");
		output = btBankAccQueryDao.getDataTablesUnionWithAllCurId(dataTablesInput, sql.toString(),region1.getId());

		btBankAccCommonServiceI.setCurName(output);
		stopWatch.stop();
		//查询营运区维护数据
		stopWatch.start("getOperationArea");
		List<OperationAreaDTO> operationArea = operationAreaService.getOperationArea();
		HashMap<String, String> map = new HashMap<>();
		for (OperationAreaDTO operationAreaDTO : operationArea) {
			map.put(operationAreaDTO.getOperationAreaCode(),operationAreaDTO.getOperationAreaName());
		}
		stopWatch.stop();
		stopWatch.start("getRegionMap");
		Map<String, RegionDTO> regionMap = regionServiceI.getRegionMap();
		stopWatch.stop();
		stopWatch.start("getByBankAcc");
		List<BtBankAccVO> voList = output.getData();

		List<BtBankAccGroupVO> accGroupList = btBankAccQueryDao.getBankAccGroupData();
		Map<String, String> accGroupMap = new HashMap<>();
		for (BtBankAccGroupVO fcBankAccGroup : accGroupList) {
			accGroupMap.put(fcBankAccGroup.getAccGroupCode(), fcBankAccGroup.getAccGroupName());
		}

		List<String> bankAccNoList = voList.stream().map(BtBankAccVO::getBankAcc).collect(Collectors.toList());
		List<BisAccBalDTO> bankAccList = btBankAccQueryDao.getByBankAcc(bankAccNoList);
		Map<String, List<BisAccBalDTO>> bankAccMap = bankAccList.stream().collect(Collectors.groupingBy(BisAccBalDTO::getBankAcc));
		stopWatch.stop();
		stopWatch.start("数据组装");
		for(BtBankAccVO dto:voList) {
			RegionDTO region = regionMap.get(dto.getCity());
			if (region != null) {
				dto.setProvName(region.getProvName());
				dto.setCityName(region.getName());
			}
//			handAccGroupData(dto, accGroupMap);
			//查询营运区code对应的name
			String parentArea = dto.getOperationAreaID();
			if (EmptyUtils.isNotEmpty(parentArea)){
				String[] areaList = parentArea.split(",");
				StringBuilder operationAreaName = new StringBuilder();
				for (String area : areaList) {
					if (map.containsKey(area)){
						operationAreaName.append(map.get(area)).append(",");
					}
				}
				dto.setOperationAreaName(operationAreaName.toString());
			}
			List<BisAccBalDTO> bankAccBalList = bankAccMap.get(dto.getBankAcc());
			Optional.ofNullable(bankAccBalList).ifPresent(listData -> {
				Optional<BisAccBalDTO> first = listData.stream().filter(item -> ObjectUtil.equal(item.getCurId(), dto.getCurId())).distinct().findFirst();
				first.ifPresent(bisAccBalDTO -> dto.setRealTimeBal(bisAccBalDTO.getBal()));
			});
			// BisAccBalDTO bisAccBalDTO = btBankAccQueryDao.getByBankAcc(dto.getBankAcc(),dto.getCurId());
			// if(!EmptyUtils.isEmpty(bisAccBalDTO)){
			// 	dto.setRealTimeBal(bisAccBalDTO.getBal());
			// }
		}
		stopWatch.stop();
		logger.error(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
		return output;
	}
	/**
	 * 账户组数据处理
	 *
	 * @param dto
	 * @param accGroupMap
	 */
	/*public void handAccGroupData(BtBankAccVO dto, Map<String, String> accGroupMap) {
		if (StringUtils.isBlank(dto.getFcAccGroupCode())) {
			return;
		}

		List<String> codes = Arrays.asList(dto.getFcAccGroupCode().split(","));
		StringBuilder accGroupName = new StringBuilder();
		for (int i = 0; i <= codes.size() - 1; i++) {
			if (i == codes.size() - 1) {
				if (accGroupMap.containsKey(codes.get(i))) {
					accGroupName.append(accGroupMap.get(codes.get(i)));
				}
			} else {
				if (accGroupMap.containsKey(codes.get(i))) {
					accGroupName.append(accGroupMap.get(codes.get(i)) + ",");
				}
			}
		}
		dto.setFcAccGroupName(accGroupName.toString());
	}*/

	/**
	 * 组装开户单位的条件SQL子句
	 *
	 * @param dataTablesInput 表单数据
	 * @return 条件SQL子句
	 */
	private String getBelongingCorpIdsInSql(DataTablesInput dataTablesInput) {
		String belongingCorpIdsInSql = null;
		String belongingCorpIds = (String) dataTablesInput.getFormdata().get("belongingCorpIds");
		if (!EmptyUtils.isBlank(belongingCorpIds)) {
			String[] corpIds = belongingCorpIds.split(",");
			belongingCorpIdsInSql = SqlUtil.getInSql(corpIds, "acc.corp_id", true);
			dataTablesInput.getFormdata().remove("belongingCorpIds");
		}
		return belongingCorpIdsInSql;
	}
	/**
	 * Created by chenlj 2018/4/19 基础平台权限问题
	 *
	 * @param dataTablesInput
	 * @param user
	 * @return
	 */
	public DataTablesOutput<BtBankAccVO> getDataTablesUnionSYS(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(
				corpServiceI.getUserCorpIds(SysResourceConstant.SYS_ACCOUNTGRANT_ACCOUNT, user.getId()), "acc.corp_id",
				true);
		sql.append(" and ").append(corpAuthSql);

		String belongingCorpIdsInSql = getBelongingCorpIdsInSql(dataTablesInput);
		if (!EmptyUtils.isBlank(belongingCorpIdsInSql)) {
			sql.append(" and ").append(belongingCorpIdsInSql);
		}
		if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("eqs_accountGrantSearch"))) {
			String accountGrantSearch = dataTablesInput.getFormdata().get("eqs_accountGrantSearch").toString();
			String accountGrantFunction = null;
			if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("eqs_accountGrantFunction"))){
				accountGrantFunction = dataTablesInput.getFormdata().get("eqs_accountGrantFunction").toString();
			}
			String userId = dataTablesInput.getFormdata().get("userId").toString();
			//查询未授权的账号
			if("0".equals(accountGrantSearch)){
				sql.append(" and acc.id not in (select distinct account_id from sys_account_grant sag  where sag.user_id = '"+userId+"' ) ");
			}else if("1".equals(accountGrantSearch)){
				if(EmptyUtils.isNotEmpty(accountGrantFunction)){
					accountGrantFunction = accountGrantFunction.replace("[","");
					accountGrantFunction = accountGrantFunction.replace("]","");
					accountGrantFunction = accountGrantFunction.replace(" ","");
					String[] accountGrantFunctionStr = accountGrantFunction.split(",");
					sql.append(" and acc.id  in (select distinct account_id from sys_account_grant sag where  sag.user_id = '"+userId+"' ");
					for(int i=0;i<accountGrantFunctionStr.length;i++){
						String entity = accountGrantFunctionStr[i];
						if("1".equals(entity)){
							sql.append(" and sag.can_query_detail = '1' ");
						}
						if("2".equals(entity)){
							sql.append(" and sag.can_query_balance = '1' ");
						}
						if("3".equals(entity)){
							sql.append(" and sag.can_transfer_accounts = '1' ");
						}
					}
					sql.append(" )");
				}
			}
		}
		CellManageDto region1 = cellManageService.getCellManageByCodeOrName1("6000", "大区");
		DataTablesOutput<BtBankAccVO> output = btBankAccQueryDao.getDataTablesUnion(dataTablesInput, sql.toString(),region1.getId());
		return setRefresh(output);
	}
	public DataTablesOutput getNotDeleteAndValid(DataTablesInput input, ICurrentUser user) {
		return btBankAccQueryDao.getNotDeleteAndValid(input, null);
	}
	/**
	 * 重构列表展示，一个账号成一行
	 * 多币别合并成一个字段展示
	 *
	 * @param output
	 * @return
	 */
	private DataTablesOutput<BtBankAccVO> setRefresh(DataTablesOutput<BtBankAccVO> output) {
		//获取银行账号主信息列表
		List<BtBankAccVO> data = output.getData();
		List<String> list = new ArrayList<>();
		for (BtBankAccVO vo : data) {
			list.add(vo.getId());
		}
		//组装银行列表id脚本
		Map<String, Object> params = new HashMap();
		String joinSql = SqlUtil.getInSqlByParams(list, "acccur.bank_acc_id", params);
		//查询各银行账号对应的币别列表
		List<BtBankAccVO> removeList = new ArrayList<>();
		List<BtBankAccVO> curList = btBankAccQueryDao.queryMultiCurByAccId(joinSql, params);
		StringBuilder curName = new StringBuilder();
		for (BtBankAccVO vo : data) {
			curName.setLength(0);
			removeList.clear();
			for (BtBankAccVO svo : curList) {
				if (vo.getId().equals(svo.getId())) {
					removeList.add(svo);
					curName.append(svo.getCurName());
					curName.append(",");
				}
			}
			curList.removeAll(removeList);
			if (StringUtils.isNotEmpty(curName)) {
				vo.setCurName(curName.substring(0, curName.length() - 1));
			}
		}
		output.setData(data);
		return output;
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	public void saveOrUpdate(BtBankAccApp entity) {
		btBankAccQueryDao.saveOrUpdate(entity);
	}
	/**
	 * 根据账号查询账号对应的流水信息（开户、变更、注销、冻结、解冻）
	 *
	 * @param bankAcc
	 * @return
	 */
	public List<BtBankAccQueryVO> listReqByBankAcc(String bankAcc) {
		return btBankAccQueryDao.listReqByBankAcc(bankAcc);
	}

	/**
	 * 添加附件
	 * @param id 银行账号id
	 * @param attachmentId 附件id
	 */
	public void addAttachment(String id, String attachmentId){
		btBankAccQueryDao.addAttachment(id, attachmentId);
	}

	public List<BtBankAccVO> getAllBankAccToStatistics(List<String> corpIds, String bankTypeId, String bankAcc, String status, String bindingBankAcc){
		List<BtBankAccVO> voList = btBankAccQueryDao.getAllBankAccToStatistics(corpIds, bankTypeId, bankAcc, status, bindingBankAcc);
		List<String> bankAccNoList = voList.stream().map(BtBankAccVO::getBindingBankAcc).collect(Collectors.toList());
		List<BisAccBalDTO> bankAccList = btBankAccQueryDao.getByBankAcc(bankAccNoList);
		Map<String, List<BisAccBalDTO>> bankAccMap = bankAccList.stream().collect(Collectors.groupingBy(BisAccBalDTO::getBankAcc));
		// 省区
		Set<String> sysProvinceIdList = new HashSet<>();
		for(BtBankAccVO dto:voList) {
			List<BisAccBalDTO> bankAccBalList = bankAccMap.get(dto.getBindingBankAcc());
			Optional.ofNullable(bankAccBalList).ifPresent(listData -> {
				Optional<BisAccBalDTO> first = listData.stream().filter(item -> ObjectUtil.equal(item.getCurId(), dto.getCurId())).distinct().findFirst();
				first.ifPresent(bisAccBalDTO -> dto.setRealTimeBal(bisAccBalDTO.getBal()));
			});
			// 第三方账户登录密码和支付密码解码
			try {
				dto.setLoginPassword(BytterAESUtils.decrypt(dto.getLoginPassword()));
				dto.setPayPassword(BytterAESUtils.decrypt(dto.getPayPassword()));
			}catch (Exception e){
				e.printStackTrace();
			}
			if (ObjectUtil.isNotEmpty(dto.getSysProvinceId())){
				sysProvinceIdList.addAll(Arrays.asList(dto.getSysProvinceId().split(",")));
			}
		}

		if (CollectionUtil.isNotEmpty(sysProvinceIdList)){
			List<String> ids = new ArrayList<>(sysProvinceIdList);
			List<SystemProvince> operationAreaList = systemProVinceService.queryByIds(ids);
			Map<String, String> sysProvinceIdMap = operationAreaList.stream().collect(Collectors.toMap(SystemProvince::getId, SystemProvince::getProvinceName));
			voList.forEach(item -> {
				if (ObjectUtil.isNotEmpty(item.getSysProvinceId())){
					String[] sysProvinceIdArr = item.getSysProvinceId().split(",");
					String sysProvinceName = "";
					for (String sysProvinceId : sysProvinceIdArr) {
						if (ObjectUtil.isNotEmpty(sysProvinceIdMap.get(sysProvinceId))){
							if (ObjectUtil.isEmpty(sysProvinceName)){
								sysProvinceName = sysProvinceIdMap.get(sysProvinceId);
							}else{
								sysProvinceName += "," + sysProvinceIdMap.get(sysProvinceId);
							}
						}
					}
					item.setSysProvinceName(sysProvinceName);
				}
			});
		}

		return voList;
	}
}