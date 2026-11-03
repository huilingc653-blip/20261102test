package com.bytter.bill.web.saas.controller.bill;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillCollectionAccountService;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillCollectionAccountVo;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO;
import com.bytter.bill.domain.bill.vo.v1.AbBillCollectionAccountListPageVO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: 应收-票据托收-托收到账 Controller
 * @Description: AB_VOUCHERINFO
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 *
 */
@Api(value = "应收-票据托收-托收到账",tags = "应收-票据托收-托收到账")
@RestController("v1AbBillCollectionAccountController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillcollectionaccount")
public class AbBillCollectionAccountController extends BaseController {
	@Autowired
	private AbBillCollectionAccountService abBillCollectionAccountService;
	@Autowired
	private AbCorpService abCorpService;
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	/**
	 * 应收-票据托收-托收到账列表 页面跳转
	 * 用于收款管理
	 *
	 * @return
	 */
	@ApiOperation("应收-票据托收-托收到账列表 页面跳转")
	@GetMapping("/listCollectionAccount")
	public AjaxJson<AbBillCollectionAccountListPageVO> listCollectionAccount(AbVoucherinfoViceVO reVo) {
		AbBillCollectionAccountListPageVO listPageVO = new AbBillCollectionAccountListPageVO();
		if(StringUtils.isNotEmpty(reVo.getCorpId())){
			reVo.setCorpName(abCorpService.get(reVo.getCorpId()).getName());
		}
		listPageVO.setBillInfo(reVo);
		BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(reVo.getId());
		if(StringUtils.isNotEmpty(bisAccDtlDTO)){
			// 获取交易日期
			listPageVO.setBusinessDate(DateUtils.date2Str(bisAccDtlDTO.getTransTime() ,DateUtils.date_sdf()));
			// 获取交易金额
			listPageVO.setBillMoney(bisAccDtlDTO.getAmt());
		}
		return success(listPageVO);
	}
	/**
	 * 应收-票据托收-托收到账 table列表
	 * @param isApply 是否审批中数据
	 * @return
	 */
	@ApiOperation("查询可托收到账的票据")
	@LogMessage(content = "查询可托收到账的票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@PostMapping("/datatable")
	@ResponseBody
	public AjaxJson<DataTablesOutput<AbBillCollectionAccountVo>> dataTable(@RequestBody V1DataTablesInput input, String isApply) {
		DataTablesInput dataTablesInput = input.convertAndInitColumns();
		return AjaxJson.success(abBillCollectionAccountService.getDataTablesUnion(dataTablesInput, isApply));
	}
}
