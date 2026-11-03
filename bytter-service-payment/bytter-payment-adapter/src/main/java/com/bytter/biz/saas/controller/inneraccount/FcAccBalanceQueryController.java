package com.bytter.biz.saas.controller.inneraccount;

import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.inneraccount.service.FcAccBalanceQueryService;
import com.bytter.biz.inneraccount.vo.FcAccBalanceQueryVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.poi.util.ExportUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: Controller
 * @Description: 内部账户余额信息表
 * @author onlineGenerator
 * @date 2017-05-25 19:36:12
 * @version 1.0
 *
 */
@Api(value = "内部账户余额信息表", tags = "内部账户余额信息表")
@RestController("v1FcAccBalanceQueryController")
@RequestMapping("/v1-payment/inneraccount/fcaccbalancequery")
public class FcAccBalanceQueryController extends BaseController {
	@Autowired
	private FcAccBalanceQueryService fcAccBalanceQueryService;

	@ApiOperation("/datatable 内部账户余额信息查询列表 包含内部账户 和 资金池账户通过 poolSign判别")
	@PostMapping("/datatable")
	@ResponseBody
	 public AjaxJson<DataTablesOutput<FcAccBalanceQueryVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		if(AllocationConstant.POOL_SIGN_1.equals(input.getFormdata().get("poolSign"))){
			return success(fcAccBalanceQueryService.getDataTablesUnionForPool(input, getUserId()));
		}else {
			return success(fcAccBalanceQueryService.getDataTablesUnion(input,getUserId(),"1"));
		}
    }

	/**
	 * 活期账号下的定期或通知账号的数据详情
	 * @param dataTablesInput 数据源
	 * @param account 活期账号
	 * @return
	 */
	@ApiOperation("/details/{account} 活期账号下的定期或通知账号的数据详情")
	@PostMapping("/details/{account}")
	@ResponseBody
	public AjaxJson<DataTablesOutput<FcAccBalanceQueryVO>> getAccountDtl(@RequestBody V1DataTablesInput dataTablesInput, @PathVariable String account){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		DataTablesOutput output = fcAccBalanceQueryService.getAccountDtl(input, account, getUserId());
		List<FcAccBalanceQueryVO> data = output.getData();
		List<FcAccBalanceQueryVO> newData = new ArrayList<>();
		for(FcAccBalanceQueryVO vo:data){
			if(!InnerAccountConstant.FCACC_AGREEMENT.equals(vo.getAccType())){
				newData.add(vo);
			}
		}
		output.setData(newData);
		return success(output);
	}
	/**
	 * 导出全部
	 * @return
	 */
	@ApiOperation("内部账户余额导出全部")
	@PostMapping("/exportAll")
	public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
						  String totalParam, String fileNameParam) throws Exception {
		ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
			@Override
			public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
				DataTablesOutput<?> dataTablesOutput = fcAccBalanceQueryService.getDataTablesUnion(dataTablesInput, user.getId(),"1");
				return dataTablesOutput.getData();
			}
		}, totalParam, fileNameParam, "内部账户余额查询",5000, FcAccBalanceQueryVO.class);
	}
}
