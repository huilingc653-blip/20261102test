package com.bytter.biz.expense.controller;
import com.bytter.biz.expense.entity.NisBusinessType;
import com.bytter.biz.expense.entity.NisOperationType;
import com.bytter.biz.expense.vo.NisBusinessTypeVO;
import com.bytter.biz.expense.vo.NisOperationTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.expense.service.NisBusinessTypeService;
import com.bytter.biz.expense.service.NisOperationTypeService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 业务类型表
 * @date 2017-06-01 14:42:30
 */
@Controller
@RequestMapping("/expense/nisbusinesstype")
public class NisBusinessTypeController extends BaseController {
	@Autowired
	private NisBusinessTypeService nisBusinessTypeService;
	@Autowired
	private NisOperationTypeService nisOperationTypeService;
	/**
	 * 查询所有的业务种类
	 */
	@GetMapping("/json")
	@ResponseBody
	public List<NisBusinessTypeVO> json() {
		return nisBusinessTypeService.listAllBusType();
	}
	/**
	 * 查询操作类型
	 *
	 * @return
	 */
	@GetMapping("/getOpType")
	@ResponseBody
	public List<NisOperationTypeVO> getOpType(String busType) {
		return nisOperationTypeService.listByBusType(busType);
	}
}
