package com.bytter.report.domain.stock.service;
import com.bytter.biz.finacial.api.constant.InnerAccountConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.report.domain.stock.dao.FcAccInterestDao;
import com.bytter.report.domain.stock.vo.FcAccInterestVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("fcAccInterestService")
@Transactional
public class FcAccInterestService extends BaseService<FcAccInterestVO, String>{
	@Autowired
	private FcAccInterestDao fcAccInterestDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@Override
	public BaseDao<FcAccInterestVO, String> getEntityDao() {
		return null;
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getFcAccInterest(DataTablesInput input,ICurrentUser user) {
		StringBuilder sql=new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_FCACCINTEREST_QUERY, user.getId()), "i.netId", true);
		sql.append(corpAuthSql);
		DataTablesOutput output = fcAccInterestDao.getInterest(input,sql.toString());
		List<FcAccInterestVO> interestVo =  output.getData();
		if(interestVo.size() < 1){
			return new DataTablesOutput();
		}
		//关联赋值
		this.associativeAssignment(interestVo);
		List<FcAccInterestVO> res = HandleRes(interestVo);
		output.setData(res);
		output.setRecordsTotal(res.size());
		output.setRecordsFiltered(res.size());
		return output;
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDetail(DataTablesInput input){
		DataTablesOutput output=fcAccInterestDao.getDtlView(input);
		//关联赋值
		this.associativeAssignment(output.getData());
		return output;
	}
	public List<FcAccInterestVO> HandleRes(List<FcAccInterestVO> list){
		List<FcAccInterestVO> resList = new ArrayList<>();
		FcAccInterestVO newInterestVo = null;
		String corp = "";
		int i = 0;
		for(FcAccInterestVO vo : list){
			if(i==0){
				corp = vo.getCorpId();
				newInterestVo = new FcAccInterestVO();
				newInterestVo.setNetId(vo.getNetId());
				newInterestVo.setCorpId(vo.getCorpId());
				newInterestVo.setCurId(vo.getCurId());
				newInterestVo.setNetName(vo.getNetName());
				newInterestVo.setCorpName(vo.getCorpName());
				newInterestVo.setCurName(vo.getCurName());
			}
			if(corp.equals(vo.getCorpId())){
				setInterest(newInterestVo,vo);
			}else {
				resList.add(newInterestVo);
				corp = vo.getCorpId();
				newInterestVo = new FcAccInterestVO();
				newInterestVo.setNetId(vo.getNetId());
				newInterestVo.setCorpId(vo.getCorpId());
				newInterestVo.setCurId(vo.getCurId());
				newInterestVo.setNetName(vo.getNetName());
				newInterestVo.setCorpName(vo.getCorpName());
				newInterestVo.setCurName(vo.getCurName());
				setInterest(newInterestVo,vo);
			}
			i++;
		}
		resList.add(newInterestVo);
		return resList;
	}
	public void setInterest(FcAccInterestVO newInterestVo,FcAccInterestVO vo){
		if(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(vo.getAccType())){
			newInterestVo.setdInterest_1(vo.getdInterest());
			newInterestVo.settInterest_1(vo.gettInterest());
		}
		if(InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(vo.getAccType())){
			newInterestVo.setdInterest_5(vo.getdInterest());
			newInterestVo.settInterest_5(vo.gettInterest());
		}
		if(InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(vo.getAccType())){
			newInterestVo.setdInterest_8(vo.getdInterest());
			newInterestVo.settInterest_8(vo.gettInterest());
		}
		if(InnerAccountConstant.FCACC_LOAN.equals(vo.getAccType())){
			newInterestVo.setdInterest_3(vo.getdInterest());
			newInterestVo.settInterest_3(vo.gettInterest());
		}
		if(InnerAccountConstant.FCACC_BANK_DEMAND.equals(vo.getAccType())){
			newInterestVo.setdInterest_2(vo.getdInterest());
			newInterestVo.settInterest_2(vo.gettInterest());
		}
		if(InnerAccountConstant.FCACC_BANK_BORROW.equals(vo.getAccType())){
			newInterestVo.setdInterest_4(vo.getdInterest());
			newInterestVo.settInterest_4(vo.gettInterest());
		}
	}

	/**
	 * 关联赋值
	 * @param list
	 */
	public void associativeAssignment(List<FcAccInterestVO> list){
		if(EmptyUtils.isNotEmpty(list)){
			//获取单位网点信息
			Map<String, CorpDTO> corpMap = corpService.getCorpMap();
			//获取币种信息
			Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrency();
			for(FcAccInterestVO vo : list){
				if(null != corpMap){
					CorpDTO netDTO = corpMap.get(vo.getNetId());
					CorpDTO corpDTO = corpMap.get(vo.getCorpId());
					if(null != netDTO){
						vo.setNetName(netDTO.getName());
					}
					if(null != corpDTO){
						vo.setCorpName(corpDTO.getName());
					}
				}
				if(null != currencyMap){
					BtCurrencyDTO btCurrencyDTO = currencyMap.get(vo.getCurId());
					if(null != btCurrencyDTO){
						vo.setCurName(btCurrencyDTO.getCurName());
					}
				}
			}
		}
	}
}
