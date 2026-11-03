package com.bytter.biz.mobile.service;

import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.mobile.dao.BankAccMobieDao;
import com.bytter.biz.mobile.vo.BtBankAccMobileBalVO;
import com.bytter.biz.mobile.vo.BtBankAccMobileDtlVO;
import com.bytter.biz.mobile.vo.BtBankAccMobileVO;
import com.bytter.cloud.common.core.constant.CommonConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.rest.RestInput;
import com.bytter.core.common.rest.RestResponse;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.system.api.IBtBankAccCommonService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号申请表
 * @date 2017-05-23 17:37:39
 */
@Service("bankAccMobieService")
@Transactional
public class BankAccMobieService extends BaseService<T, String> implements  java.io.Serializable{

	@Autowired
	private BankAccMobieDao bankAccMobieDao;


	@DubboReference(check = false)
	private IBtBankAccCommonService iBtBankAccCommonService;

	@DubboReference(check = false)
	private IBisAccDtlService iBisAccDtlService;


//    @DubboReference(check = false)
//	private BankAccMobieService bankAccMobieService;
//	@DubboReference(check = false)
//	private BtBankAccService btBankAccService;
//	@DubboReference(check = false)
//	private ICorpService corpServiceI;
//	@DubboReference(check = false)
//	 private IBtCurrencyService btCurrencyServiceI;
//
//	@DubboReference(check = false)
//	private BisAccAttributeService bisAccAttributeService;
//	@DubboReference(check = false)
//	private IDictService dictService;
//
//	@DubboReference(check = false)
//	private BisAccBalService bisAccBalService;
//
//	@DubboReference(check = false)
//	private IBisAccDtlService iBisAccDtlService;
//	@DubboReference(check = false)
//	private IBisAccBalService iBisAccBalService;
//	@DubboReference(check = false)
//	private IBankTypeService bankTypeService;
//	@DubboReference(check = false)
//	private IUserService userService;
//	@DubboReference(check = false)
//	private IAccountGrantService accountGrantService;

	public RestResponse<BisAccDtlVO> getPageListByBankAcc(String id, String curId, RestInput restInput, String remark, String beginDate, String endDate) {
		BtBankAccDTO btBankAccDTO = iBtBankAccCommonService.getBtBankAccVOById(id, curId);

		IPageResult<BisAccDtlVO> pageListByBankAcc = iBisAccDtlService.getPageListByBankAcc(restInput, btBankAccDTO.getBankAcc(), btBankAccDTO.getCurId(), remark, beginDate, endDate);
		RestResponse<BisAccDtlVO> restResponse = new RestResponse<BisAccDtlVO>();
		restResponse.setPageSize(pageListByBankAcc.getPagePara().getPageSize());
		restResponse.setPageNumber(pageListByBankAcc.getPageNumber());
		restResponse.setTotalPage(pageListByBankAcc.getTotalRow());
		restResponse.setData(pageListByBankAcc.getList());
		restResponse.setVersion(pageListByBankAcc.getVersion());

		//增加金额汇总统计
		List<BisAccDtlVO> list = iBisAccDtlService.getListByBankAcc(btBankAccDTO.getBankAcc(), btBankAccDTO.getCurId(),remark, beginDate, endDate);
		BigDecimal sumIncome = new BigDecimal(0);//收入总金额
		BigDecimal sumPay = new BigDecimal(0);//支出总金额
		for(BisAccDtlVO vo:list){
			if(CommonConstant.YES.equals(vo.getCdSign())){
				sumPay=sumPay.add(vo.getAmt());
			}else{
				sumIncome=sumIncome.add(vo.getAmt());
			}
		}
		restResponse.setSumIncome(sumIncome);
		restResponse.setSumPay(sumPay);
		return restResponse;
	}





	/**
	 * 根据单位和时间查询余额
	 * @param corpId
	 * @param userId
	 * @param beginDate
	 * @param endDate
	 * @param bankAccId
	 * @return
	 */
	public String getBal(String corpId,String userId, String beginDate,String endDate,String bankAccId,String curId){
		String html = "";
		List<BtBankAccMobileBalVO> list = bankAccMobieDao.getBal(corpId,userId, beginDate,endDate,bankAccId,curId);
		BigDecimal totalBal = new BigDecimal(0);
		for (int i = 0; i < list.size(); i++) {
			html +="<li class='mui-table-view-cell'><span style='font-size:11px;'>";
			html +=list.get(i).getBankName()+""+list.get(i).getBankAcc().toString().substring(list.get(i).getBankAcc().toString().length()-4);
			html +="</br>"+ DateUtils.formatDate(list.get(i).getBalDate().toString())+"&nbsp;"+list.get(i).getCurName()+"</span>";
			html +="<span class='mui-badge mui-badge-inverted'>"+ NumberUtil.formatNumeric(((BigDecimal)list.get(i).getBal()))+"</span>";
			html +="</li>";
			totalBal = totalBal.add((BigDecimal)list.get(i).getBal());
		}
		html += "<input type=\"hidden\" id=\"totalBal\" value=\""+NumberUtil.formatNumeric(totalBal)+"\">";
		return html;
	}

	/**
	 * 查询明细
	 * @param corpId
	 * @param userId
	 * @param beginDate
	 * @param endDate
	 * @param beginMoney
	 * @param endMoney
	 * @param oppBankAcc
	 * @return
	 */
	public String getDtl(String corpId,String userId, String beginDate,String endDate,String bankAcc,String beginMoney,String endMoney,String oppBankAcc,String curId){
		String html = "";
		//支出
		BigDecimal expenditure = new BigDecimal(0);
		//收入
		BigDecimal income = new BigDecimal(0);
		List<BtBankAccMobileDtlVO> list = bankAccMobieDao.getDtl(corpId,userId, beginDate,endDate,bankAcc,beginMoney,endMoney,oppBankAcc,curId);
		for (int i = 0; i < list.size(); i++) {
			String a = "";
			if("1".equals(list.get(i).getCdSign().toString())){
				a = "-";
				expenditure = expenditure.add((BigDecimal)list.get(i).getAmt());
			}else{
				income = income.add((BigDecimal)list.get(i).getAmt());
			}
			html +="<li class='mui-table-view-cell'><span style='font-size:15px;'>";
			html +="对方账号名称："+(list.get(i).getOppAccName()==null?"":list.get(i).getOppAccName())+"</br>交易时间："+DateUtils.format((Timestamp)list.get(i).getTransTime(),"yyyy-MM-dd HH:mm:ss")+"</br>余额：";
			html +=NumberUtil.formatNumeric((BigDecimal)list.get(i).getBal());
			html +="</br>交易金额："+a+NumberUtil.formatNumeric(((BigDecimal)list.get(i).getAmt()));
			html +="</br>对方账号："+(list.get(i).getOppAccNo()==null?"":list.get(i).getOppAccNo())+"</br>摘要："+(list.get(i).getRemark() == null?"":list.get(i).getRemark());
			html +="</li>";
//			html +="对方账号名称："+"</br>交易时间："+"</br>余额：";
		}
		html += "<input type=\"hidden\" id=\"expenditure\" value=\""+NumberUtil.formatNumeric(expenditure)+"\">";
		html += "<input type=\"hidden\" id=\"income\" value=\""+NumberUtil.formatNumeric(income)+"\">";
		return html;
	}


	/**
	 * 获取有权限的银行账号
	 * @param userId
	 * @param corpId
	 * @param grantType
	 * @return
	 */
	public List<BtBankAccMobileVO> getGrantBankAcc(String userId, String corpId, String grantType){
		return bankAccMobieDao.getGrantBankAcc(userId, corpId, grantType);
	}

	@Override
	public BaseDao<T, String> getEntityDao() {
		return null;
	}


}
