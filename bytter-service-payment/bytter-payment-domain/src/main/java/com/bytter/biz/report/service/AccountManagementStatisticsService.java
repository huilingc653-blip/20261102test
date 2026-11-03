package com.bytter.biz.report.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.service.BtBankAccUseService;
import com.bytter.biz.netbank.service.FcBankAccGroupService;
import com.bytter.biz.netbank.service.NisBusTypeService;
import com.bytter.biz.netbank.vo.BtBankAccUseVO;
import com.bytter.biz.payment.api.constant.NisBusTypeConstant;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.NisBusTypeDTO;
import com.bytter.biz.report.dao.NisAllocationDailyReportDao;
import com.bytter.biz.report.vo.AccountManagementStatisticsVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.increment.api.service.IChoProductReqService;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jiangw
 * @version 1.0
 * @Title: Service
 * @Description: 账户管理统计表
 * @date 2024-08-14
 */
@Service("accountManagementStatisticsService")
@Transactional
public class AccountManagementStatisticsService extends BaseService<BisExcDTO, String> {
	@Autowired
	private NisAllocationDailyReportDao nisAllocationDailyReportDao;
	@Autowired
	private NisAllocationReqService nisAllocationReqService;
	@Autowired
	private NisBusTypeService nisBusTypeService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IEquityMaintenanceService equityMaintenanceService;
	@DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IChoProductReqService choProductReqService;
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
	@DubboReference(check = false)
	private IOperationAreaService operationAreaService;
	@DubboReference(check = false)
	private IBtBankAccQueryService btBankAccQueryService;
	@Autowired
	private FcBankAccGroupService fcBankAccGroupService;
	@Autowired
	private BtBankAccUseService btBankAccUseService;

	@Override
	public BaseDao<BisExcDTO, String> getEntityDao() {
		return nisAllocationDailyReportDao;
	}
	/**
	 * 月度资金划拨汇总表查询
	 *
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<AccountManagementStatisticsVO> getList(AccountManagementStatisticsVO vo, ICurrentUser user) {
		String queryCorpId = vo.getCorpId();
		List<String> queryCorpList = new ArrayList<String>();
		if(!EmptyUtils.isEmpty(queryCorpId)){
			String[] queryCorpIds = queryCorpId.split(",");
			queryCorpList = Arrays.asList(queryCorpIds);
		}else{
			queryCorpList = corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_STATISTICS_QUERY, user.getId());
		}
		Map<String, String> unitCmMap = new HashMap<>();
		if (ObjectUtil.isNotEmpty(queryCorpList)){
			List<CorpDTO> list = corpService.listCorpDTOByCorpIds(queryCorpList);
			for (CorpDTO corpDTO : list) {
				if (ObjectUtil.isNotEmpty(corpDTO.getUnitCmCode()) && "6000".equals(corpDTO.getUnitCmCode())){
					unitCmMap.put(corpDTO.getId(), corpDTO.getName());
				}
			}
		}

		List<AccountManagementStatisticsVO> accountManagementStatisticsVOS = new ArrayList<AccountManagementStatisticsVO>();
		List<BtBankAccVO> btBankAccVOS = btBankAccQueryService.getAllBankAccToStatistics(queryCorpList, vo.getBankTypeId(), vo.getBankAcc(), vo.getStatus()+"", vo.getBindingBankAcc());
		//查询所有账户组
		List<FcBankAccGroup> validPayType = fcBankAccGroupService.getValidPayType();
		HashMap<String, String> map = new HashMap<>();
		for (FcBankAccGroup fcBankAccGroup : validPayType) {
			map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
		}

		// 所有用途
		List<BtBankAccUseVO> voList = btBankAccUseService.getVOList();
		HashMap<String, String> accUseMap = new HashMap<>();
		if (!EmptyUtils.isEmptyList(voList)){
			for (BtBankAccUseVO accUseVO : voList) {
				accUseMap.put(accUseVO.getUseCode(), accUseVO.getUseName());
			}
		}
		for(BtBankAccVO btBankAccVO: btBankAccVOS){
			AccountManagementStatisticsVO accountManagementStatisticsVO = new AccountManagementStatisticsVO();
			BeanUtils.copyProperties(btBankAccVO, accountManagementStatisticsVO);
			// 从上级取大区
			if (ObjectUtil.isNotEmpty(btBankAccVO.getCorpParentIds())){
				for (String corpParentId : btBankAccVO.getCorpParentIds().split(",")){
					if (unitCmMap.containsKey(corpParentId)){
						accountManagementStatisticsVO.setAreaName(unitCmMap.get(corpParentId));
						break;
					}
				}
			}

			accountManagementStatisticsVO.setEquityMaintenanceName(btBankAccVO.getShareholding());

			if(EmptyUtils.isEmpty(accountManagementStatisticsVO.getRealTimeBal())){
				accountManagementStatisticsVO.setRealTimeBal(BigDecimal.ZERO);
			}

			//处理账户组
			/*String fcAccGroupCode = btBankAccVO.getFcAccGroupCode();
			if (EmptyUtils.isNotEmpty(fcAccGroupCode)){
				List<String> codes = Arrays.asList(fcAccGroupCode.split(","));
				StringBuilder accGroupName = new StringBuilder();
				for(int i = 0;i<=codes.size()-1;i++){
					if (i==codes.size()-1){
						if (map.containsKey(codes.get(i))){
							accGroupName.append(map.get(codes.get(i)));
						}
					}else{
						if (map.containsKey(codes.get(i))){
							accGroupName.append(map.get(codes.get(i))+",");
						}
					}
				}
				accountManagementStatisticsVO.setFcAccGroupName(accGroupName.toString());
			}*/
			// 处理第三方用户组
			String bindingAccGroupCode = btBankAccVO.getBindingAccGroupCode();
			if (ObjectUtil.isNotEmpty(bindingAccGroupCode)){
				List<String> codes = Arrays.asList(bindingAccGroupCode.split(","));
				StringBuilder accGroupName = new StringBuilder();
				for(int i = 0;i<=codes.size()-1;i++){
					if (i==codes.size()-1){
						if (map.containsKey(codes.get(i))){
							accGroupName.append(map.get(codes.get(i)));
						}
					}else{
						if (map.containsKey(codes.get(i))){
							accGroupName.append(map.get(codes.get(i))+",");
						}
					}
				}
				accountManagementStatisticsVO.setBindingAccGroupCode(accGroupName.toString());
			}

			// 账号用途 accUseCode
			String accUseCode = btBankAccVO.getAccUseCode();
			String accUseName = "";
			if (ObjectUtil.isNotEmpty(accUseCode)){
				for (String code : accUseCode.split(",")){
					if (ObjectUtil.isNotEmpty(code)){
						if (ObjectUtil.isNotEmpty(accUseName)){
							accUseName += ",";
						}
						accUseName += accUseMap.get(code);
					}
				}

			}
			accountManagementStatisticsVO.setAccUseName(accUseName);
			accountManagementStatisticsVO.setAccountPurpose(accUseName);

			accountManagementStatisticsVOS.add(accountManagementStatisticsVO);
		}

		// 根据查询条件过滤是否第三方账户
		String isBindThreeAcc = vo.getIsBindThreeAcc();
		if (ObjectUtil.isNotEmpty(isBindThreeAcc) && ObjectUtil.isNotEmpty(accountManagementStatisticsVOS)) {
			if (CommonConstant.YES.equals(isBindThreeAcc)){
				accountManagementStatisticsVOS = accountManagementStatisticsVOS.stream().filter(accountManagementStatisticsVO -> {
					if (ObjectUtil.isNotEmpty(accountManagementStatisticsVO.getBindingBankAcc())) {
						return true;
					}
					return false;
				}).collect(Collectors.toList());
			}
			if (CommonConstant.NO.equals(isBindThreeAcc)){
				accountManagementStatisticsVOS = accountManagementStatisticsVOS.stream().filter(accountManagementStatisticsVO -> {
					if (ObjectUtil.isNotEmpty(accountManagementStatisticsVO.getBindingBankAcc())) {
						return false;
					}
					return true;
				}).collect(Collectors.toList());
			}


		}
		return accountManagementStatisticsVOS;
	}

}
