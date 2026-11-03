package com.bytter.guarantee.bond.service;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtil;
import com.bytter.guarantee.bond.constant.CmsProcessMsgConstant;
import com.bytter.guarantee.bond.dao.FcBondAddDao;
import com.bytter.guarantee.bond.entity.FcBondAdd;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.vo.FcBondAccVO;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserDTO;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:FcBondAddService
 * @Description:追加保证金服务层
 * @date 2018年9月11日 上午10:20:03
 */
@Service("FcBondAddService")
@Transactional
public class FcBondAddService extends BaseService<FcBondAdd, String> {
	@Autowired
	private FcBondAddDao fcBondAddDao;

	@Autowired
	private FcBondLogService fcBondLogService;
	@Autowired
	private FcBondBalService fcBondBalService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@Autowired
	private FcBondDetailService fcBondDetailService;

	@DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;

	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	@DubboReference(check = false)
	private IUserService userService;

	@Override
	public BaseDao getEntityDao() {
		return fcBondAddDao;
	}

	public String getSequenceBillNO() {
		return SequenceSingleton.getSequence(SequenceEnum.NIS_FCBOND_ADD);
	}
	/**
	 * 查询追加保证金表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput<FcBondAddDTO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		sql.append(" and m.create_by = '").append(user.getLoginName()).append("' ");
        String resource = FcBondConstant.NETBANK_FCBONDADD_DEPOSIT_QUERY;//查询资源
        List<String> corpList = corpService.getUserCorpIds(resource, user.getId());
        if (EmptyUtils.isNotEmpty(corpList)) {
            String corpAuthSql = SqlUtil.getInSql(corpList, "b.corp_id", true);
            sql.append(" and ").append(corpAuthSql);
        } else {
            sql.append(" and 1 <> 1");
        }
		//账号条件
		String likes_bankName = (String) dataTablesInput.getFormdata().get("likes_bankName");
		String likes_bankAcc = (String) dataTablesInput.getFormdata().get("likes_bankAcc");
		String likes_accName = (String) dataTablesInput.getFormdata().get("likes_accName");
		if (!StringUtil.isBlank_new(likes_bankName) ||!StringUtil.isBlank_new(likes_bankAcc)||!StringUtil.isBlank_new(likes_accName)) {
			StringBuilder accSql = new StringBuilder();
			Map<String, Object> params = new HashMap();
			accSql.append(" SELECT a.id FROM bt_bank_acc a WHERE 1=1");
			if (!StringUtil.isBlank_new(likes_bankName)){
				accSql.append(" and a.bank_name like :bankName");
				String bankName = "%" + likes_bankName + "%";
				params.put("bankName", bankName);
			}
			if (!StringUtil.isBlank_new(likes_bankAcc)){
				accSql.append(" and a.bank_acc like :bankAcc");
				String bankAcc = "%" + likes_bankAcc + "%";
				params.put("bankAcc", bankAcc);
			}
			if (!StringUtil.isBlank_new(likes_accName)){
				accSql.append(" and a.acc_name like :accName");
				String accName = "%" + likes_accName + "%";
				params.put("accName", accName);
			}
			List<BtBankAccDTO> accDTOList = iBtBankAccService.listBankAccBySql(accSql.toString(), params);
			if (accDTOList.size() > 0) {
				List<String> accList = accDTOList.stream().map(BtBankAccDTO::getId).collect(Collectors.toList());
				if (accList != null && accList.size() > 0) {
					sql.append(" and ").append(SqlUtil.getInSql(accList, " B.BANK_ACC_ID", true));
				}
				dataTablesInput.getFormdata().remove("likes_bankName");
				dataTablesInput.getFormdata().remove("likes_bankAcc");
				dataTablesInput.getFormdata().remove("likes_accName");
			} else {
				return new DataTablesOutput<>();
			}
		}
		DataTablesOutput<FcBondAddDTO> output = fcBondAddDao.getBondAdditionDataTables(dataTablesInput, sql.toString());
		List<FcBondAddDTO> list = output.getData();
		if(list.size()>0) {
			//单位
			Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(corpList);
			Map<String, BtCurrencyDTO> btCurrencyMap = iBtCurrencyService.getCurrency();
			for (FcBondAddDTO vo : list) {
				vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
				vo.setCurName(btCurrencyMap.get(vo.getCurId()).getCurName());
				//账号
				setVoBankAcc(vo);
			}
		}
        return output;
	}
	/**
	 * 为vo增加账号字段
	 * @param vo
	 */
	private void setVoBankAcc(FcBondAddDTO vo) {
		if (!StringUtil.isBlank(vo.getBankAccId())) {
			//账号
			BtBankAccDTO bankAccDTO = iBtBankAccService.getBankAccById(vo.getBankAccId());
			if(null != bankAccDTO) {
				vo.setBankAcc(bankAccDTO.getBankAcc());
				vo.setAccName(bankAccDTO.getAccName());
				vo.setBankName(bankAccDTO.getBankName());
			}
		}
	}
	/**
	 * 查询存出保证金的账户列表
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput<FcBondAccVO> queryBondAccDataTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        String resource = FcBondConstant.NETBANK_FCBONDADD_DEPOSIT_APPLY;//应用资源
        List<String> corpList = corpService.getUserCorpIds(resource, user.getId());
		List<String> bankAccIds = Lists.newArrayList();
        if (EmptyUtils.isNotEmpty(corpList)) {
			bankAccIds = iBtBankAccService.getBankAccIdsByCorpIdList(corpList, "", new HashMap<>());
		}
		DataTablesOutput<FcBondAccVO> output = fcBondAddDao.queryFcBondVO(dataTablesInput, bankAccIds);
		List<FcBondAccVO> data = output.getData();
		if (CollectionUtils.isNotEmpty(data)){
			List<String> bankAccS = data.stream().map(FcBondAccVO::getBankAcc).collect(Collectors.toList());
			Map<String, BtBankAccDTO> accMap = iBtBankAccService.getBankAccMapByAccsAndCurId(bankAccS);
			for( FcBondAccVO a :data) {
				BtBankAccDTO btBankAccDTO = accMap.get(a.getBankAcc() + a.getCurId());
				a.setBankName(btBankAccDTO.getBankName());
				a.setAccName(btBankAccDTO.getAccName());
				a.setCorpId(btBankAccDTO.getCorpId());
				a.setBankAcc(btBankAccDTO.getBankAcc());
				a.setCorpName(btBankAccDTO.getCorpName());
				a.setCurName(btBankAccDTO.getCurName());
			}
		}
		return output;
	}
	/**
	 * 查询待审批追加保证金列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public DataTablesOutput getApproveDataTables(DataTablesInput dataTablesInput, String businessSql) {
		return fcBondAddDao.getAddApproveDataTables(dataTablesInput,businessSql);
	}
	/**
	 * 查询历史追加明细
	 */
	public List<FcBondAddDTO> queryAddHis(String bondId) {
		return fcBondAddDao.queryAddHis(bondId);
	}
	/**
	 * 根据id查询追加保证金的详细信息
	 */
	public FcBondAddDTO queryAddVOById(String id) {
		return fcBondAddDao.queryAddVOById(id);
	}
	/**
	 * 改变追加保证金的状态为删除
	 */
	public void deleteBondAdd(String id, ICurrentUser user) {
		FcBondAdd fcBondAdd = this.get(id);
		fcBondAdd.setStatus(FcBondConstant.STATUE_DEL);
		fcBondAdd.setUpdateBy(user.getLoginName());
		fcBondAdd.setUpdateDate(DateUtils.getDate());
		super.updateEntity(fcBondAdd);
	}
	public void updateEntity(FcBondAdd fcBondAdd){
		super.updateEntity(fcBondAdd);
	}
	public void saveEntity(FcBondAdd fcBondAdd){
		super.saveEntity(fcBondAdd);
	}

	/**
	* <br>
	* <b>功能：</b> 删除头寸调拨单打回保证金追加 <br>
	* <b>作者：</b>liyh<br>
	* <b>日期：</b> 2020/8/18 0018 <br>
	*/
	public String tempAddBond(String id, ICurrentUser user) {
		FcBondAdd fcBondAdd = this.get(id);
		fcBondAdd.setStatus(FcBondConstant.STATUE_BACK);//头寸删除后打回保证金追加状态
		fcBondAdd.setUpdateBy(user.getId());
		fcBondAdd.setUpdateDate(DateUtils.getDate());
		super.updateEntity(fcBondAdd);
        //生成日志记录
        Integer messageType = FcBondConstant.MESSAGE_TYPE_BACK;
        String msg = CmsProcessMsgConstant.MSGA_DELETE_BACK + "";//业务删除打回
        fcBondLogService.saveFcBondMessge(fcBondAdd.getId(), fcBondAdd.getAddMoney(), messageType, msg, user);
        return fcBondAdd.getBondId();
	}

	/**
	 * 查询历史追加明细
	 */
	public DataTablesOutput<FcBondAddDTO> addHisDatatable(DataTablesInput dataTablesInput, String joinSql) {
		DataTablesOutput<FcBondAddDTO> output = fcBondAddDao.addHisDatatable(dataTablesInput, joinSql);
		List<FcBondAddDTO> list	= output.getData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(FcBondAddDTO vo : list){
			UserDTO user=userService.getByLoginName(vo.getCreateBy());
			vo.setCreateByName(user.getUsername());
			if (vo.getAddDate() != null){
				vo.setAddDateStr(sdf.format(vo.getAddDate()));
			}
		}
		return output;
	}

}
