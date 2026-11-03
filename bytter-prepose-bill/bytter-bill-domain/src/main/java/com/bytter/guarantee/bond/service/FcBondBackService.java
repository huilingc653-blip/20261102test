package com.bytter.guarantee.bond.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BaseDataConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.guarantee.bond.dao.FcBondBackDao;
import com.bytter.guarantee.bond.entity.FcBondBack;
import com.bytter.guarantee.bond.entity.FcBondBal;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.vo.FcBondApplyVO;
import com.bytter.guarantee.vo.FcBondBackVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fusb
 * @version 1.0
 * @Title FcBondBackService
 * @Description 保证金转回service
 * @date 2018年9月17日
 */
@Service("fcBondBackService")
@Transactional
public class FcBondBackService extends BaseService<FcBondBack, String> {
	@Autowired
	FcBondBackDao fcBondBackDao;
	@Autowired
	FcBondBalService fcBondBalService;
    @DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;
	@DubboReference(check = false)
	private IUserService iUserService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	@DubboReference(check = false)
	private IConfigService configService;

	/**
	 * 查询保证金转回列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput<FcBondBackVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
        String resource = FcBondConstant.NETBANK_FCBOND_DEPOSITBACK_QUERY;//查询资源
        List<String> corpList = corpService.getUserCorpIds(resource, user.getId());
        if (EmptyUtils.isNotEmpty(corpList)) {
            String corpAuthSql = SqlUtil.getInSql(corpList, "b.corp_id", true);
            sql.append(" and ").append(corpAuthSql);
        } else {
            sql.append(" and 1 <> 1");
        }
		DataTablesOutput<FcBondBackVO> dataTablesUnion = fcBondBackDao.getDataTablesUnion(dataTablesInput, sql.toString());
		dataTablesUnion.getData().forEach(vo -> {
			if(vo.getBackInterest() == null){
				vo.setBackInterest(BigDecimal.ZERO);
			}
		});
		return dataTablesUnion;
	}
	/**
	 * 根据保证金id，得到保证金账户信息
	 *
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public FcBondBackVO getByIdFcBondBackVO(String id) {
		FcBondBackVO vo = fcBondBackDao.getByIdFcBondBackVO(id);
		BtBankAccDTO accDTO= iBtBankAccService.getBankAccById(vo.getBankAccId());
		if(null != accDTO){
			vo.setBankAcc(accDTO.getBankAcc());
			vo.setAccName(accDTO.getAccName());
			vo.setBankName(accDTO.getBankName());
			vo.setCorpId(accDTO.getCorpId());
			vo.setCorpName(corpService.getCorpName(accDTO.getCorpId()));
			vo.setCurId(accDTO.getCurId());
			vo.setCurName(accDTO.getCurName());
		}
		return vo;
	}
	/**
	 * 转回保证金
	 *
	 * @param fcBondBackVO
	 * @param user
	 */
	public void backFcBondMoney(FcBondBackVO fcBondBackVO, ICurrentUser user) {
		FcBondBack entity = new FcBondBack();
		// 复制属性给entity
		BeanUtils.copyNotNullProperties(fcBondBackVO, entity);
		// 生成单号
		entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_FCBOND_BACK));
		entity.setStatus(95);
		entity.setBackDate(new Date());
		entity.setCreateDate(new Date());
		entity.setCreateBy(user.getLoginName());
		super.saveEntity(entity);
		// 通过保证金ID得到相应的余额表中保证金信息
		FcBondBal fcBondBal = fcBondBackDao.getFcBondBalById(entity.getBondId());
		// 修改保证金余额表中的信息，余额表FC_BOND_BAL中的转回金额BACK_MON累加
		BigDecimal backMoney1 = entity.getBackMoney();
		BigDecimal backMoney2 = fcBondBal.getBackMoney();// 跟新前的余额表中
		BigDecimal money = backMoney2.add(backMoney1);
		fcBondBal.setBackMoney(money);
		// 更新保证金余额表Fc_Bond_Bal中相应的记录信息
		updateFcBondBal(fcBondBal);
	}
	/**
	 * 更新保证金余额表中的记录信息
	 *
	 * @param fcBondBal
	 */
	public void updateFcBondBal(FcBondBal fcBondBal) {
		fcBondBalService.saveOrUpdate(fcBondBal);
	}
	/**
	 * 根据保证金id，得到保证金账户信息
	 *
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<FcBondApplyVO> getByIdFcBondApply(String id) {
		return fcBondBackDao.getByIdFcBondApplys(id);
	}
	@Transactional(readOnly = true)
	public DataTablesOutput<FcBondApplyVO> fcBondApplyDatatable(DataTablesInput dataTablesInput, String joinSql) {
		return fcBondBackDao.fcBondApplyDatatable(dataTablesInput, joinSql);
	}
	/**
	 * 根据保证金id，得到保证金转回历史记录
	 *
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<FcBondBackVO> getByIdFcBondBack(String id) {
		List<FcBondBackVO> backVoList = fcBondBackDao.getByIdFcBondBacks(id);
		List<String> loginNames=new ArrayList<>();
		for(FcBondBackVO backVO:backVoList){
			loginNames.add(backVO.getCreateBy());
		}
		Map<String, String> userMap= iUserService.getUserNameByLoginNames(loginNames);
		for(FcBondBackVO backVO:backVoList){
			backVO.setCreateByName(userMap.get(backVO.getCreateBy()));
		}
		return backVoList;
	}
	@Override
	public BaseDao<FcBondBack, String> getEntityDao() {
		return fcBondBackDao;
	}
}
