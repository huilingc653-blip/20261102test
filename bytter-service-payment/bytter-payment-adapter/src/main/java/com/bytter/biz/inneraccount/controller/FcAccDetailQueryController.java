package com.bytter.biz.inneraccount.controller;

import com.bytter.biz.finacial.api.dto.FcAccDetailDTO;
import com.bytter.biz.inneraccount.service.FcAccDetailQueryService;
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
import java.util.HashMap;
import java.util.List;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title: Controller
 * @Description: 内部账户信息表
 * @author onlineGenerator
 * @date 2017-05-25 19:36:12
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/inneraccount/fcaccdetailquery")
public class FcAccDetailQueryController extends BaseController {
	@Autowired
	private FcAccDetailQueryService fcAccDetailQueryService;
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCACCDETAIL_QUERY)
	@PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcAccDetailQueryService.getDataTablesUnion(dataTablesInput,getUserId());
    }
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCACCDETAIL_QUERY)
	@LogMessage(content = "查询内部账户信息表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "inneraccount/listFcAccDetailQuery";
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
				DataTablesOutput dataTablesOutput = fcAccDetailQueryService.getDataTablesUnion(dataTablesInput, user.getId());
				List<FcAccDetailDTO> list = dataTablesOutput.getData();
				HashMap<String, FcAccDetailDTO> curTotalMap = new HashMap<>();
				for(FcAccDetailDTO vo:list){
					FcAccDetailDTO fcAccDetail;
					if(curTotalMap.containsKey(vo.getCurName())){
						fcAccDetail = curTotalMap.get(vo.getCurName());
						fcAccDetail.setCreditMoney(fcAccDetail.getCreditMoney().add(vo.getCreditMoney()));
						fcAccDetail.setDebitMoney(fcAccDetail.getDebitMoney().add(vo.getDebitMoney()));
					} else {
						fcAccDetail = new FcAccDetailDTO();
						fcAccDetail.setCreditMoney(vo.getCreditMoney());
						fcAccDetail.setDebitMoney(vo.getDebitMoney());
					}
					fcAccDetail.setAccount(vo.getCurName() + "合计");
					curTotalMap.put(vo.getCurName(), fcAccDetail);
				}
				for(String key:curTotalMap.keySet()){
					list.add(curTotalMap.get(key));
				}
				return dataTablesOutput.getData();
			}
		}, totalParam, fileNameParam, "内部账户流水查询",5000, FcAccDetailDTO.class);
	}
}
