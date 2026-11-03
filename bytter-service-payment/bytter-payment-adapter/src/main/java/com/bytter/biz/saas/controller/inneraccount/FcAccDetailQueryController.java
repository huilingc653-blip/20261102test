package com.bytter.biz.saas.controller.inneraccount;

import com.bytter.biz.finacial.api.dto.FcAccDetailDTO;
import com.bytter.biz.inneraccount.service.FcAccDetailQueryService;
import com.bytter.biz.inneraccount.vo.v1.FcAccDetailVO;
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
import java.util.HashMap;
import java.util.List;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @Title: Controller
 * @Description: 内部账户流水查询
 * @author onlineGenerator
 * @date 2017-05-25 19:36:12
 * @version 1.0
 *
 */
@Api(value = "内部账户流水查询",tags = "内部账户流水查询")
@RestController("v1FcAccDetailQueryController")
@RequestMapping("/v1-payment/inneraccount/fcaccdetailquery")
public class FcAccDetailQueryController extends BaseController {
	@Autowired
	private FcAccDetailQueryService fcAccDetailQueryService;
	@PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAccDetailVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(fcAccDetailQueryService.getDataTablesUnion(input,getUserId()));
    }
	/**
	 * 导出全部
	 *
	 * @return
	 */
	@ApiOperation("内部账户流水导出全部")
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
