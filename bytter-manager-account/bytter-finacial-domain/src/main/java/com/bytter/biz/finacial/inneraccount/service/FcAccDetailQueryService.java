package com.bytter.biz.finacial.inneraccount.service;

import com.bytter.biz.finacial.api.dto.FcAccDetailDTO;
import com.bytter.biz.finacial.api.dto.FcAccbookDtlDTO;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.finacial.api.dto.VoucherWrapperDTO;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.inneraccount.dao.FcAccDetailQueryDao;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户流水
 * @date 2017-06-23 19:36:12
 */
@Service("fcAccDetailQueryService")
@Transactional
public class FcAccDetailQueryService  {
	@Autowired
    private FcAccDetailQueryDao fcAccDetailQueryDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IFcAccbookService fcAccbookService;
	/**
	 * 查询内部账号流水列表
	 * @param
	 * @return
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,String joinSql,Map<String,Object> params) {
		DataTablesOutput dataTablesOutput = fcAccDetailQueryDao.getDataTablesUnion(dataTablesInput,joinSql,params);
		return setInnerExtInfo(dataTablesOutput);
	}
	private DataTablesOutput setInnerExtInfo(DataTablesOutput dataTablesOutput) {
		List<FcAccDetailDTO> list = dataTablesOutput.getData();
		HashMap<String, VoucherWrapperDTO> voucherMap = new HashMap<>();
		for(FcAccDetailDTO vo:list){
			if(NetbankConstant.PAY_TYPE_05.equals(vo.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_37.equals(vo.getSysVoucherCode())){
				VoucherWrapperDTO voucherWrapperDTO = voucherMap.get(vo.getVoucherNo());
				if (voucherWrapperDTO == null) {
					voucherWrapperDTO = this.fcAccbookService.getVoucherByVoucherNo(vo.getVoucherNo());
				}
				if (voucherWrapperDTO != null) {
					List<FcAccbookVoucherDTO> accbookDtls = voucherWrapperDTO.getTarget();
					List<FcAccbookDtlDTO> bookDtls = accbookDtls.get(0).getBookDtls();
					//借贷明细超过2条则不显示外单位
					if(!EmptyUtils.isEmpty(bookDtls) && bookDtls.size() == 2) {
						for (FcAccbookDtlDTO dtl : bookDtls) {
							if (vo.getAccount().equals(dtl.getAccount())) {
								continue;
							}
							vo.setExternalAcc(dtl.getAccount());
							vo.setExternalName(dtl.getAccName());
						}
					}
				}
			}
		}
		return dataTablesOutput;
	}
}
