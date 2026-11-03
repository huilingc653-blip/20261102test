package com.bytter.biz.inneraccount.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.BtBankAccMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.bytter.system.api.vo.BtBankAccVO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行账号变更
 * @date 2017-05-23 17:37:39
 */
@Service("btBankAccDelHandleService")
@Transactional
//TODO注释的需要处理
public class BtBankAccDelHandleService {
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private BtBankAccMessageService btBankAccMessageService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IBtBankAccAppService btBankAccAppService;
	@DubboReference(check = false)
	private IBtBankAccCommonService btBankAccCommonService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
	/**
	 * 显示列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(
				corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCDELHANDLE_QUERY, user.getId()), "acc.corp_id",
				true);
		sql.append(" and ").append(corpAuthSql);
		sql.append(" and (acc.status = " + BtBankAccConstant.STATUE_CHECK + ") ");
		DataTablesOutput output = btBankAccAppService.getDataTablesUnion(dataTablesInput, sql.toString(), BtBankAccConstant.BUSTYPE_3);
		//this.btBankAccCommonService.setCurName(output);
		return output;
	}
	public void toModify(BtBankAccVO btBankAccVO, ICurrentUser user) {
		BtBankAccAppDTO t = this.btBankAccCommonService.getBtBankAccApp(btBankAccVO.getId());
		//补充附件信息
		if (StringUtils.isNotBlank(btBankAccVO.getAttachmentId())) {
			t.setAttachmentId(btBankAccVO.getAttachmentId());
		}
		BtBankAccDTO acc = this.btBankAccCommonService.getBtBankAccByBankAcc(t.getBankAcc());
		List<BtCurrencyDTO> btCurrencyDTOList = this.btBankAccCommonService.listCurAccById(acc.getId());
		String[] curIds = null;
		if(!EmptyUtils.isEmptyList(btCurrencyDTOList)){
			curIds = new String[btCurrencyDTOList.size()];
			for(int i = 0 ; i < btCurrencyDTOList.size(); i++){
				curIds[i] = btCurrencyDTOList.get(i).getId();
			}
		}
		if(checkAccBalZero(acc.getBankAcc(), curIds)){
			throw new ParameterException(NetbankMessageEnum.NIS_BANKACC_MESSAGE_BAL_NOT_ZERO);
		}
		Date curDate = DateUtils.getDate();
		t.setStatus(100);
		t.setUpdateBy(user.getLoginName());
		t.setUpdateDate(curDate);
		this.btBankAccAppService.saveOrUpdate(t);
		if (!EmptyUtils.isEmpty(acc)) {
			acc.setStatus(-2);
			//注销日期
			acc.setCloseDate(btBankAccVO.getCloseDate());
			acc.setUpdateBy(user.getLoginName());
			acc.setUpdateDate(curDate);
			/*acc.setReceiptCode(btBankAccVO.getReceiptCode());
			acc.setFcAccGroupCode(btBankAccVO.getFcAccGroupCode());*/
			btBankAccService.saveOrUpdateObj(acc);
		}
		//生成日志
		btBankAccMessageService.saveMessage(t.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_UPDATE, "", BtBankAccMessageConstant.BUS_TYPE_DELHANDLE, user);
		// 消息提醒
		notificationService.complete(NotificationConstant.NETBANK.BANKACC_DEL_HANDLE, t.getId(), user.getId());
		notificationService.sendToUserByLoginName(t.getId(), t.getCreateBy(), NotificationConstant.NETBANK.BANKACC_DEL_HANDLE_SUCCESS, t);
	}
	/**
	 * 判断账户余额是否为零
	 *
	 * @param bankAcc 账户
	 * @return BisAccBalDTO
	 */
	private boolean checkAccBalZero(String bankAcc, String[] curIds){
		boolean result = false;
		BigDecimal bal = BigDecimal.ZERO;
		for(String curId : curIds){
			BigDecimal lastBal = this.bisAccBalService.getLastByBalDate(bankAcc, curId);
			if(!EmptyUtils.isEmpty(lastBal)){
				bal = bal.add(lastBal);
			}
		}
		if(bal.doubleValue() > BigDecimal.ZERO.doubleValue()){
			result = true;
		}
		return result;
	}
	/**
	 * 打回
	 *
	 * @param id
	 */
	public void back(String id, ICurrentUser user) {
		BtBankAccAppDTO btbankaccapp = this.btBankAccAppService.getById(id);
		btbankaccapp.setStatus(-1);
		btbankaccapp.setStatus(BtBankAccConstant.STATUE_BACK);
		this.btBankAccAppService.saveOrUpdate(btbankaccapp);
		if (ObjectUtil.isNotEmpty(btbankaccapp.getBankAcc())){
			btBankAccService.updateBankAccClose(btbankaccapp.getBankAcc(), CommonConstant.NO);
		}
		//生成日志
		btBankAccMessageService.saveMessage(btbankaccapp.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_BACK, "", BtBankAccMessageConstant.BUS_TYPE_DEL, user);
		// 消息提醒
		notificationService.complete(NotificationConstant.NETBANK.BANKACC_DEL_HANDLE, id, user.getId());
		notificationService.sendToUserByLoginName(btbankaccapp.getId(), btbankaccapp.getCreateBy(), NotificationConstant.NETBANK.BANKACC_DEL, btbankaccapp);
	}

	/**
	 * 银行账户到期自动销户
	 */
	@XxlJob("bankAccAutoCancelJob")
	public void bankAccAutoCancelJob() {
		try {
			Date closeDate = DateUtils.getDate();
			String curDate = DateUtils.formatDate(closeDate,DateUtils.PATTERN_CLASSICAL_SIMPLE);
			XxlJobHelper.log("---当前日期【{}】，账户到期自动销户，开始执行定时任务---",curDate);
			List<BtBankAccDTO> list = btBankAccService.listBankAccByDate(curDate);
			if (EmptyUtils.isEmptyList(list)) {
				XxlJobHelper.log("---当前日期【{}】，账户到期的数量为0----",curDate);
			} else {
				List<String> ids = this.checkBankAccAmount(list);
				XxlJobHelper.log("---满足注销条件账户【{}】个---",ids.size());
				if(!EmptyUtils.isEmptyList(ids)){
					int num = btBankAccService.updateBankAccByIds(ids, closeDate);
					XxlJobHelper.log("---成功注销【{}】个银行账户---",num);
				}
			}
			XxlJobHelper.log("---当前日期【{}】，账户到期自动销户，结束执行定时任务",curDate);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 校验银行账户是否存在余额
	 * @param list
	 * @return
	 */
	public List<String> checkBankAccAmount(List<BtBankAccDTO> list){
		List<String> ids = new ArrayList<>();
		for(BtBankAccDTO btBankAccDTO : list){
			BtBankAccDTO acc = this.btBankAccCommonService.getBtBankAccByBankAcc(btBankAccDTO.getBankAcc());
			List<BtCurrencyDTO> btCurrencyDTOList = this.btBankAccCommonService.listCurAccById(acc.getId());
			String[] curIds = null;
			if(!EmptyUtils.isEmptyList(btCurrencyDTOList)){
				curIds = new String[btCurrencyDTOList.size()];
				for(int i = 0 ; i < btCurrencyDTOList.size(); i++){
					curIds[i] = btCurrencyDTOList.get(i).getId();
				}
			}
			if(!checkAccBalZero(acc.getBankAcc(), curIds)){
				ids.add(btBankAccDTO.getId());
			}
		}
		return ids;
	}
}
