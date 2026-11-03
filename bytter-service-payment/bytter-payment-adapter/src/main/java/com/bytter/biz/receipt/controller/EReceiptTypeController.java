package com.bytter.biz.receipt.controller;
import com.bytter.biz.receipt.entity.EReceiptType;
import com.bytter.biz.receipt.service.EReceiptTypeService;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**   
 * @Title: Controller  
 * @Description: 电子回单类型维护
 * @author onlineGenerator
 * @date 2017-06-05 10:52:28
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/receipt/eReceiptType")
public class EReceiptTypeController extends BaseController {
	@Autowired
	private EReceiptTypeService eRceiptTypeService;
	/**
	 * 查询电子回单类型列表
	 * @return
	 */
	@GetMapping("/json")
	@ResponseBody
	public List<EReceiptType> listByValidSign() {
		List<EReceiptType> list=eRceiptTypeService.listReceiptType("1","");
		return list;
	}
}
