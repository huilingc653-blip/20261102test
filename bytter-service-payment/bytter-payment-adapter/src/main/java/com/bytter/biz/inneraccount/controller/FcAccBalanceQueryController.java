package com.bytter.biz.inneraccount.controller;
import com.bytter.biz.inneraccount.service.FcAccBalanceQueryService;
import com.bytter.biz.inneraccount.vo.FcAccBalanceQueryVO;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.poi.util.ExportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
//import org.apache.shiro.authz.annotation.Logical;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title: Controller
 * @Description: 内部账户余额信息表
 * @author onlineGenerator
 * @date 2017-05-25 19:36:12
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/inneraccount/fcaccbalancequery")
public class FcAccBalanceQueryController extends BaseController {
	@Autowired
	private FcAccBalanceQueryService fcAccBalanceQueryService;
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACCBALANCE_QUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACCBALANCE_POOL_QUERY},
//			logical = Logical.OR)
	@PostMapping("/datatable")
	@ResponseBody
	 public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		String poolSign = (String) dataTablesInput.getFormdata().get("poolSign");
		if(AllocationConstant.POOL_SIGN_1.equals(poolSign)){
			return fcAccBalanceQueryService.getDataTablesUnionForPool(dataTablesInput, getUserId());
		}else {
			return fcAccBalanceQueryService.getDataTablesUnion(dataTablesInput,getUserId(),"0");
		}
    }
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACCBALANCE_QUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACCBALANCE_POOL_QUERY},
//			logical = Logical.OR)
	@LogMessage(content = "查询内部账户余额信息列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list/{poolSign}")
	public String list(@PathVariable("poolSign") String poolSign, Model model) {
		model.addAttribute("poolSign", poolSign);
        return "inneraccount/listFcAccBalanceQuery";
	}
	/**
	 * 已关联的活期账号，显示该活期账号下的定期或通知账号
	 * @param account 活期账号
	 * @param model 视图
	 * @return 定期或通知账号
	 */
	@GetMapping("/viewdtl/{account}")
	public String viewDtl(@PathVariable String account,Model model) {
		model.addAttribute("account",account);
		return "inneraccount/listFcAccBalanceDetailQuery";
	}
	/**
	 * 活期账号下的定期或通知账号的数据详情
	 * @param dataTablesInput 数据源
	 * @param account 活期账号
	 * @return
	 */
	@PostMapping("/details/{account}")
	@ResponseBody
	public DataTablesOutput getAccountDtl(@RequestBody DataTablesInput dataTablesInput,@PathVariable String account){
		DataTablesOutput output = fcAccBalanceQueryService.getAccountDtl(dataTablesInput, account, getUserId());
		List<FcAccBalanceQueryVO> data = output.getData();
		List<FcAccBalanceQueryVO> newData = new ArrayList<>();
		for(FcAccBalanceQueryVO vo:data){
			if(!InnerAccountConstant.FCACC_AGREEMENT.equals(vo.getAccType())){
				newData.add(vo);
			}
		}
		output.setData(newData);
		return output;
	}
	/**
	 * 导出全部
	 *
	 * @return
	 */
	@RequestMapping("/exportAll")
	public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
						  String totalParam, String fileNameParam) throws Exception {
		ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
			@Override
			public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
				DataTablesOutput<?> dataTablesOutput = fcAccBalanceQueryService.getDataTablesUnion(dataTablesInput, user.getId(),"0");
				return dataTablesOutput.getData();
			}
		}, totalParam, fileNameParam, "内部账户余额查询",5000, FcAccBalanceQueryVO.class);
	}
}
