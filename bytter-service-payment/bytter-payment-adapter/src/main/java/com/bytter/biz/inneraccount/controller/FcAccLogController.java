package com.bytter.biz.inneraccount.controller;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IFcAccLogService;
import com.bytter.system.api.vo.FcAccLogVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 日志查询
 * @author onlineGenerator
 * @date 2017-07-18 10:48:05
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/inneraccount/fcAccLog")
public class FcAccLogController extends BaseController {
	@DubboReference(check = false)
	private IFcAccLogService fcAccLogService;
	/**
	 * 日志页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "页面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/viewFcAccLog")
	public String viewFcAccLog(HttpServletRequest request, Model model) {
		String bizId=request.getParameter("bizId"); //业务表单据Id
		String account = request.getParameter("account"); //内部账号
		String bizType=request.getParameter("bizType"); //业务类型
		String logType=request.getParameter("logType"); //类型
		if (StringUtils.isNotEmpty(bizId) || StringUtils.isNotEmpty(account) || StringUtils.isNotEmpty(bizType) ) {
			List<FcAccLogVO> listLog;
			if(InnerAccountConstant.LOG_TYPE_ALL.equals(logType)){
				listLog=fcAccLogService.listByParams(null,account,null);
			}else{
				listLog=fcAccLogService.listByParams(bizId,account,bizType);
			}

			//排序：先提交后复核，按操作时间升序
//			listLog.sort(Comparator.comparing(FcAccLogVO::getOpTime, Comparator.nullsLast(Comparator.naturalOrder()))
//					.thenComparing(FcAccLogVO::getOpType, Comparator.nullsLast(Comparator.naturalOrder()))
//
//			);

			model.addAttribute("listLogPage", listLog);
		}
		return "inneraccount/viewFcAccLog";
	}
}