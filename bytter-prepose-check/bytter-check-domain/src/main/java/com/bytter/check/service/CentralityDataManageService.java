package com.bytter.check.service;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.dto.FcCompareAccDTO;
import com.bytter.biz.finacial.api.service.IFcAccbookBlnedService;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.check.api.ICentralityDataManageService;
import com.bytter.check.dao.CentralityDataManageDao;
import com.bytter.check.dto.FcCompareAccBeanDTO;
import com.bytter.check.entity.FcBankCheckDtl;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.enums.CheckMessageEnum;
import com.bytter.check.vo.CentralityDataVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtil;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
/**
* @Title: Service
* @Description: FC_COMPARE_ACC
* @author onlineGenerator
* @date 2017-05-31 15:28:08
* @version 1.0
*
*/
@Service("centralityDataManageService")
@DubboService
@Transactional
public class CentralityDataManageService extends BaseService<FcCompareAcc,String> implements ICentralityDataManageService {
	@Autowired
	private CentralityDataManageDao centrailDao;
    @Autowired
    private FcBankCheckDtlService fcBankCheckDtlService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@DubboReference(check = false)
	private IFcAccbookBlnedService iFcAccbookBlnedService;
	/**中心对账单录入*/
	public DataTablesOutput dataTable(DataTablesInput dataTablesInput,String userId){
		List<String> corpAuth = corpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_QUERY,userId);
		List<String> accList = iFcAccService.getFcAccsByCorpIdList(corpAuth,null,"");
		return centrailDao.dataTable(dataTablesInput,accList);
	}
	
	public void saveList(CentralityDataVO vo){
		for (int i = 0; i < vo.getAccList().size(); i++) {
			FcCompareAcc acc = vo.getAccList().get(i);
			//验证
			saveCheck(acc,i);
			if(acc.getCreditMoney() == null){
				acc.setCreditMoney(BigDecimal.ZERO);
			}
			if(acc.getDebitMoney() == null){
				acc.setDebitMoney(BigDecimal.ZERO);
			}
			acc.setImportType(FinacialConstant.IMPORT_TYPE_FILE);
			acc.setCheckType(FinacialConstant.CHECK_TYPE_CEN);
			//未接收页面的对账标识,添加默认为 未对账
			acc.setCompareSign(FinacialConstant.COMPARE_SIGN_NO);
			//TODO 起息日期
			acc.setInterestStartDate(new Date());
			acc.setLineNo(new BigDecimal(i+1));
			/*//币别
			acc.setCurCode();*/
			save(acc);
		}
	}
	
	/**
	 * 
	 * 功能介绍 : 查询已结流水账信息,用于生产中心对账数据<br>	
	 * @param dataTablesInput
	 * @return
	 * <b>日期：</b>2017-6-6<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput listGenAcc(DataTablesInput dataTablesInput,String userId) {
		Object ob = dataTablesInput.getFormdata().get("corpId");
		if (null == ob) {
			return new DataTablesOutput();
		}
		dataTablesInput.getFormdata().remove("corpId");
		String joinsql = " and IF_COMPARE = '1' and net_id = '"+ob.toString()+"'";
		List<String> accList = iFcAccService.getFcAccsByCorpIdList(null,null,joinsql);
		DataTablesOutput output = iFcAccbookBlnedService.listGenAcc(dataTablesInput,accList);
		return output;
	}
	
	/**
	 * 
	 * 功能介绍 : 中心数据重新生成<br>	
	 * <b>日期：</b>2017-6-6<br> <br>
	 * @version 1.0
	 * @author liusy
	 */
	public void doAccImportAgain(Map<String,Object> params){
		if(null == params){
			return ;
		}
		List<String> accList = null;
		if (StringUtils.isNotEmpty(params.get("corpId"))){
			String joinsql = " and IF_COMPARE = '1' and net_id = '"+params.get("corpId")+"'";
			accList = iFcAccService.getFcAccsByCorpIdList(null,null,joinsql);
		}
		List<String> orderNos = centrailDao.getFcCompareAcc("");
		if(!EmptyUtils.isEmpty(params.get("queryBeginDate"))){
			String queryBeginDateStr = params.get("queryBeginDate").toString();
			Date queryBeginDate = DateUtil.parseDate(queryBeginDateStr,"yyyy-MM-dd");
			params.put("queryBeginDate",queryBeginDate);
		}
		if(!EmptyUtils.isEmpty(params.get("queryEndDate"))){
			String queryEndDateStr = params.get("queryEndDate").toString();
			Date queryEndDate = DateUtil.parseDate(queryEndDateStr,"yyyy-MM-dd");
			params.put("queryEndDate",queryEndDate);
		}
		//生成中心对账数据
		List<FcCompareAccDTO> List = iFcAccbookBlnedService.queryCompareAcc(params,accList,orderNos);
		for (FcCompareAccDTO accDTO : List) {
			FcCompareAcc acc = new FcCompareAcc();
			BeanUtils.copyNotNullProperties(accDTO,acc);
			acc.setImportType(FinacialConstant.IMPORT_TYPE_INTERFACE);
			acc.setCheckType(FinacialConstant.CHECK_TYPE_CEN);
			acc.setCompareSign(FinacialConstant.COMPARE_SIGN_NO);
			save(acc);
		}
	}
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(FcCompareAcc entity){
		if(!entity.getCompareSign().equals(FinacialConstant.COMPARE_SIGN_NO)){
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_COMPARE_YES_DEL);
		}
        centrailDao.deleteCompareAcc(entity);
        //已对账或未对账的数据才能删除
        FcBankCheckDtl bankCheckDtl = new FcBankCheckDtl();
        bankCheckDtl.setErpId(entity.getId());
        bankCheckDtl.setBankAcc(entity.getCompAcc());
        bankCheckDtl.setCurCode(entity.getCurId());
        bankCheckDtl.setStatus(CommonConstant.STATUE_DELETE);
        fcBankCheckDtlService.save(bankCheckDtl);
	}
	
	/**
	 * 批量删除中心对账数据
	 */
	public void batchDelete(List<String> ids){
		for(String id : ids){
			FcCompareAcc fcCompareAcc = this.get(id);
			this.deleteEntity(fcCompareAcc);
		}
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
 	public void save(FcCompareAcc entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcCompareAcc entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(FcCompareAcc entity){
 		super.saveOrUpdate(entity);
 	}
 	/****************       验证begin         *****************/
 	
 	
 	/**
 	 * 功能介绍 : 保存验证<br>	
 	 * <b>日期：</b>2017-6-5<br> <br>
 	 * @version 1.0
 	 * @author lvwl
 	 */
 	public void saveCheck(FcCompareAcc acc,int index){
 		if(EmptyUtils.isEmpty(acc.getVoucherNo())){
 			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_VOUCHERNO_EMPTY);
 		}
 		/*if(EmptyUtils.isEmpty(acc.getOrderNo())){
 			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_ORDERNO_EMPTY);
 		}*/
 		if(EmptyUtils.isEmpty(acc.getTallyDate())){
 			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_TALLYDATE_EMPTY);
 		}
 		if(EmptyUtils.isEmpty(acc.getCompAcc())){
 			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_COMPACC_EMPTY);
 		}
 		if( !bigDecimalIsZero(acc.getCreditMoney()) && !bigDecimalIsZero(acc.getDebitMoney()) ){
 			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_DE_CR_NOTZERO);
 		}
		if( bigDecimalIsZero(acc.getCreditMoney()) && bigDecimalIsZero(acc.getDebitMoney()) ){
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_DE_CR_ZERO);
 		}	
 	}
 	
 	/**
 	 * 功能介绍 : 生成中心对账单。<br>
 	 * <b>日期：</b>2017年7月12日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
	 @Override
 	public void saveCompareAccBean(FcCompareAccBeanDTO fcCompareAccBeanDTO) {
 		FcCompareAcc compareAcc = new FcCompareAcc();
 		BeanUtils.copyProperties(fcCompareAccBeanDTO, compareAcc);
 		save(compareAcc);
 	}
 	
 	/**
 	 * 
 	 * 功能介绍 : 判断BigDecimal是否为null或者为0<br>	
 	 * @return 为null或者0 ：true
 	 * <b>日期：</b>2017-6-9<br> <br>
 	 * @version 1.0
 	 * @author lvwl
 	 */
 	public boolean bigDecimalIsZero(BigDecimal data){
 		if(data == null || data.compareTo(BigDecimal.ZERO) == 0){
 			return true;
 		}
 		return false;
 	}
 	
 	/**
 	 * 
 	 * 功能介绍。获取余额初始化后的账号<br>
 	 * <b>日期：</b>2017年10月13日<br>
 	 * @version 1.0
 	 * @author zhengxn 
 	 */
 	@Transactional(readOnly = true)
 	public List<CentralityDataVO> getValidAccount(String userId){
 		List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCINITACC_QUERY,userId);
		List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByCorpIdList(corpAuth,null,"");
		List<String> accs = new ArrayList<>();
		Map<String,FcAccDTO> fcAccDTOMap = new HashMap<>();
		if(EmptyUtils.isNotEmpty(fcAccDTOS)){
			for(FcAccDTO dto : fcAccDTOS){
				accs.add(dto.getBlnctrAcc());
				fcAccDTOMap.put(dto.getBlnctrAcc(),dto);
			}
		}
		List<CentralityDataVO> list = centrailDao.getValidAccount(accs);
		if(EmptyUtils.isNotEmpty(list)){
			for(CentralityDataVO vo : list){
				String acc = vo.getAccount();
				FcAccDTO fcAccDTO = fcAccDTOMap.get(acc);
				if(!EmptyUtils.isEmpty(fcAccDTO)){
					vo.setAccName(fcAccDTO.getAccName());
					vo.setSubjectName(fcAccDTO.getSubjectName());
				}
			}
		}
 		return list;
 	}
 	
 	/****************       验证end         *****************/
	@Override
	public BaseDao<FcCompareAcc, String> getEntityDao() {
		return centrailDao;
	}
}
