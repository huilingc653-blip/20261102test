package com.bytter.biz.saas.controller.inneraccount;

import com.bytter.biz.inneraccount.dto.v1.FcAccLogPageDTO;
import com.bytter.biz.inneraccount.vo.v1.FcAccLogPageVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IFcAccLogService;
import com.bytter.system.api.vo.FcAccLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Title: Controller
 * @Description: 日志查询
 * @author onlineGenerator
 * @date 2017-07-18 10:48:05
 * @version 1.0
 *
 */
@Api(value = "账户操作日志查询",tags = "账户操作日志查询")
@RestController("v1FcAccLogController")
@RequestMapping("/v1-payment/inneraccount/fcAccLog")
public class FcAccLogController extends BaseController {
	@DubboReference(check = false)
	private IFcAccLogService fcAccLogService;


	@ApiOperation(value = "/viewFcAccLog 查询账户操作日志")
	@LogMessage(content = "查询账户操作日志",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/viewFcAccLog")
	public AjaxJson<DataTablesOutput<FcAccLogVO>> viewFcAccLog(@RequestBody FcAccLogPageDTO pageParams) {
		String bizId = pageParams.getBizId();       //业务表单据Id
		String account = pageParams.getAccount();	//内部账号
		String bizType = pageParams.getBizType();	//业务类型
		String logType = pageParams.getLogType();	//类型
		AjaxJson<DataTablesOutput<FcAccLogVO>> listLogJson = new AjaxJson<>();
		DataTablesOutput<FcAccLogVO> listLogData = new DataTablesOutput<>();
		if (StringUtils.isNotEmpty(bizId) || StringUtils.isNotEmpty(account)
				|| StringUtils.isNotEmpty(bizType) ) {
			List<FcAccLogVO> listLog;
			if(InnerAccountConstant.LOG_TYPE_ALL.equals(logType)){
				listLog=fcAccLogService.listByParams(null,account,null);
			}else{
				listLog=fcAccLogService.listByParams(bizId,account,bizType);
			}

			listLogData.setData(listLog);
			listLogData.setRecordsTotal(listLog.size());
		}
		listLogJson.setObj(listLogData);
		listLogJson.setSuccess(true);
		return listLogJson;
	}
}
