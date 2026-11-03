package com.bytter.biz.saas.controller.bankacc;

import com.bytter.biz.inneraccount.entity.BtBankAccMessage;
import com.bytter.biz.inneraccount.service.BtBankAccMessageService;
import com.bytter.biz.netbank.vo.BtBankAccMessageVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 结算日志表
 * @date 2017-05-23 15:20:33
 */
@Api(value = "结算日志表", tags = "结算日志表")
@RestController("v1BtBankAccMessageController")
@RequestMapping("/v1-payment/bankacc/message")
public class BtBankAccMessageController extends BaseController {

	@Autowired
	private BtBankAccMessageService btBankAccMessageService;

	@DubboReference(check = false)
	private IDictService dictService;
	/**
	 * 日志查询
	 * @param billNo
	 * @param busType
	 * @return
	 */
	@ApiOperation("结算日志查询")
	@GetMapping("/list/{billNo}/{busType}")
	@ResponseBody
	public AjaxJson<List<BtBankAccMessageVO>> list(@PathVariable("billNo") String billNo, @PathVariable("busType") String busType) {
		List<BtBankAccMessage> list = btBankAccMessageService.listMessage(billNo, busType);
		Map<String, DictDTO> stringDictDTOMap = dictService.listMap("btBankAccMessage.messageType");
		Map<String,String> typeNameMap = stringDictDTOMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, item -> item.getValue().getName()));
		List<BtBankAccMessageVO> resultList = list.stream().map(item -> {
			BtBankAccMessageVO messageVO = new BtBankAccMessageVO();
			BeanUtils.copyProperties(item, messageVO);
			messageVO.setMessageTypeName(typeNameMap.get(String.valueOf(messageVO.getMessageType())));
			return messageVO;
		}).collect(Collectors.toList());
		return success(resultList);
	}

}
