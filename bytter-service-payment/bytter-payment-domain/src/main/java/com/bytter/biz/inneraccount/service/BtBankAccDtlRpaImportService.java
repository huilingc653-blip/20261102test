package com.bytter.biz.inneraccount.service;

import com.bytter.biz.bdlink.api.service.IBisAccBalAutoService;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.inneraccount.dao.BtBankAccDtlDao;
import com.bytter.biz.inneraccount.entity.BtBankAccDtl;
import com.bytter.biz.inneraccount.vo.BtBankAccDtlRpaVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.core.constant.BisAccBalConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.NisWorkFlowConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行账号明细录入表
 * @date 2017-06-29 13:51:01
 */
@WorkFlow(NisWorkFlowConstant.BT_BANKACC_DTL)
@Service("btBankAccDtlRpaImportService")
@Transactional
public class BtBankAccDtlRpaImportService extends BaseService<BtBankAccDtl, String> implements IUserExcelVerifyHandler<BtBankAccDtlRpaVO>/*, ICorpDisableVerifyService*/ {
	@Autowired
	private BtBankAccDtlDao btBankAccDtlDao;
	@DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;
	@DubboReference(check = false)
	private IBisBifInitService iBisBifInitService;
	@DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@Autowired
	private BtBankAccMessageService btBankAccMessageService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IBisAccBalAutoService bisAccBalAutoService;
	@Autowired(required = false)
	private IBtBankAccService btBankAccService;

	@Override
	public BaseDao<BtBankAccDtl, String> getEntityDao() {
		return btBankAccDtlDao;
	}

	@Override
	public ExcelVerifyHanlderResult verifyHandler(BtBankAccDtlRpaVO vo,Object... user) {
		return null;
	}
	@Override
	public void verifyHandler(List<BtBankAccDtlRpaVO> list,Object... user) {
		if (!EmptyUtils.isEmptyList(list)) {
			//银行账号集合
			HashMap<String, BtBankAccDTO> bankAccMap = new HashMap<>();
			//币种集合
			HashMap<String, String> curMap = new HashMap<>();
			//用户
			ICurrentUser userInfo=(ICurrentUser)user[0];
			BtBankAccDTO bankAcc = null;
			Map<String,BtBankAccDtlRpaVO> map = new HashMap<>();


			String rmbId = btCurrencyService.getCurrencyByName("人民币");
			for(BtBankAccDtlRpaVO vo:list){
				List<String> msgList = new ArrayList<>();
				if(EmptyUtils.isEmpty(vo.getCurName())) {
					// 币别默认为人民币
					vo.setCurId(rmbId);
				}else{
					String curId = btCurrencyService.getCurrencyByName(vo.getCurName());
					if(EmptyUtils.isNotEmpty(curId)){
						vo.setCurId(curId);
					}else{
						vo.setCurId(rmbId);
					}
				}

				// 银行账号不为空
				if (EmptyUtils.isBlank(vo.getBankAcc())) {
					msgList.add("nis.netbank.message.dtl.bankacc.null");
				} else {
					//币别不为空
					if(StringUtils.isNotBlank(vo.getCurId())) {
						if (bankAccMap.containsKey(vo.getBankAcc()+CommonConstant.COMMA_SPLIT+vo.getCurId())) {
							bankAcc = bankAccMap.get(vo.getBankAcc()+CommonConstant.COMMA_SPLIT+vo.getCurId());
							vo.setCorpId(bankAcc.getCorpId());
							vo.setBifCode(bankAcc.getBifCode());
							vo.setBankName(bankAcc.getBankName());
							vo.setAccName(bankAcc.getAccName());
						} else {
							// 判断银行账号是否在系统中存在
							bankAcc = this.iBtBankAccService.getBankAccByBankAccAndCurId(vo.getBankAcc(), vo.getCurId());
							if (EmptyUtils.isEmpty(bankAcc)) {
								msgList.add("nis.netbank.message.dtl.bankaccinfo.null");
							} else if (BtBankAccConstant.ISONLINE_1.equalsIgnoreCase(bankAcc.getIsOnline())) {
								// 只能导入离线账号
								msgList.add("nis.netbank.message.dtl.online.no");
							} else {
								Boolean flag = accountGrantService.checkPermissionByUserAndAccount(userInfo.getId(), vo.getBankAcc(), AccountGrantConstant.OFFLINE_BANK_TYPE,
										AccountGrantConstant.QUERY_DETAIL, ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY);
								//为空说明无权限
								if (flag) {
									msgList.add("nis.netbank.message.bankacc.detail");
								} else {
									vo.setCorpId(bankAcc.getCorpId());
									vo.setBifCode(bankAcc.getBifCode());
									vo.setBankName(bankAcc.getBankName());
									vo.setAccName(bankAcc.getAccName());
									bankAccMap.put(vo.getBankAcc()+CommonConstant.COMMA_SPLIT+vo.getCurId(), bankAcc);
								}
							}
						}
					}
				}

				// 收支标志
				if (EmptyUtils.isEmpty(vo.getCdSign())) {
					msgList.add("nis.netbank.message.cdsign.null");
				} else if (!BisAccBalConstant.CDSIGN_OUT.equals(vo.getCdSign())
						&& !BisAccBalConstant.CDSIGN_IN.equals(vo.getCdSign())) {
					msgList.add("nis.netbank.message.cdsign.format");
				}

				// 交易时间 为“交易日期”+“交易时间”
				if (EmptyUtils.isEmpty(vo.getTransDate())) {
					msgList.add("nis.netbank.message.transdate.null");
				}
				if (EmptyUtils.isEmpty(vo.getTransDtlTime())) {
					msgList.add("nis.netbank.message.transdtltime.null");
				}
				if(!EmptyUtils.isEmpty(vo.getTransDate()) && !EmptyUtils.isEmpty(vo.getTransDtlTime())){
					Calendar cal = Calendar.getInstance();
					cal.setTime(vo.getTransDate());
					cal.add(Calendar.HOUR, vo.getTransDtlTime().getHours());
					cal.add(Calendar.MINUTE, vo.getTransDtlTime().getMinutes());
					cal.add(Calendar.SECOND, vo.getTransDtlTime().getSeconds());
					vo.setTransTime(cal.getTime());
				}

				if (EmptyUtils.isEmpty(vo.getTransTime())) {
					msgList.add("nis.netbank.message.transtime.null");
				} else {
					Calendar c = Calendar.getInstance();
					c.setTime(vo.getTransTime());
					vo.setMonths(c.get(Calendar.MONTH) + 1);
					//校验交易日期不能大于系统当前日期
					if(vo.getTransTime().after(DateUtils.getDate())){
						msgList.add("bt.bankaccdtl.message.transtime.lt.sysdate");
					}
				}

				//明细截止时间校验
				if (bankAcc != null && StringUtils.isNotBlank(bankAcc.getId()) && vo.getTransTime() != null) {
					Date endDtlDate = this.iBtBankAccService.getLastEndDtlDate(bankAcc.getId(), vo.getCurId());
					if (!EmptyUtils.isEmpty(endDtlDate)) {
						if (!endDtlDate.before(vo.getTransTime())) {
							msgList.add("bt.bankaccdtl.import.message.baldate.lt.completedate");
						}
					}
				}
				// 金额不能为空
				if (EmptyUtils.isEmpty(vo.getAmt())) {
					msgList.add("nis.netbank.message.amt.null");
				}

				//对方账户(必填)
				if(EmptyUtils.isEmpty(vo.getOppAccNo())){
					msgList.add("对方账户必填");
				}
				//对方户名(必填)
				if(EmptyUtils.isEmpty(vo.getOppAccName())){
					msgList.add("对方户名必填");
				}
				//摘要不能为空
				if(EmptyUtils.isEmpty(vo.getRemark())){
					msgList.add("nis.netbank.message.remark.null");
				}
				//判断导入集合是否重复
				String key = vo.getBankAcc()+vo.getOppAccNo()+vo.getCdSign()+DateUtils.formatDate(vo.getTransTime(),"yyyy-MM-dd HH:mm:ss")
						+vo.getRemark()+vo.getAmt()+vo.getBal();
				if(map.containsKey(key)){
					msgList.add("nis.netbank.message.isRepeat.null");
				}else{
					map.put(key,vo);
				}

				//本方账号、对方账号、借贷标志、交易时间、金额、摘要 不能重复 增加余额
				boolean isRepeat = btBankAccDtlDao.isRepeat(vo.getBankAcc(),vo.getOppAccNo(),vo.getCdSign(), vo.getTransTime(), vo.getAmt(), vo.getRemark(),vo.getBal());
				if(isRepeat){
					msgList.add("nis.netbank.message.isRepeat.null");
				}

				vo.addErrorMsg(msgList);
			}

			//总数
			int count = 0;
			//付款金额
			BigDecimal payment = BigDecimal.ZERO;
			//收款金额
			BigDecimal collection = BigDecimal.ZERO;
			for(BtBankAccDtlRpaVO vo:list){
				if(EmptyUtils.isEmpty(vo.getErrorMsg())) {
					count++;
					if (BisAccBalConstant.CDSIGN_IN.equals(vo.getCdSign())) {
						collection = collection.add(vo.getAmt());
					}
					if (BisAccBalConstant.CDSIGN_OUT.equals(vo.getCdSign())) {
						payment = payment.add(vo.getAmt());
					}
				}
			}
			BtBankAccDtlRpaVO totle = new BtBankAccDtlRpaVO();
			totle.setBankAcc("总计笔数");
			totle.setAmt(new BigDecimal(count));
			totle.setOppAccNo("付款金额小计");
			totle.setOppAccName(NumberUtil.formatDouble(payment));
			totle.setOppAccBank("收款金额小计");
			totle.setRemark(NumberUtil.formatDouble(collection));
			list.add(totle);
		}
	}

}
