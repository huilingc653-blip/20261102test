package com.bytter.biz.allocation.controller;
import com.alibaba.fastjson.JSON;
import com.bytter.biz.inneraccount.entity.BisGatherPolicy;
import com.bytter.biz.inneraccount.service.BisAccGatherPolicyService;
import com.bytter.biz.inneraccount.service.BisGatherPolicyService;
import com.bytter.biz.payment.api.dto.TransactionMqMsg;
import com.bytter.biz.payment.api.enums.BusinessType;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.common.queue.CommonMqMsg;
import com.bytter.core.common.queue.TenantAwareStreamBridge;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;
//////import org.apache.shiro.authz.annotation.RequiresPermissios;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号关系策略维护表
 * @date 2017-06-20 16:44:38
 */
@Controller
@RequestMapping("/allocation/bisaccgatherpolicy")
public class BisAccGatherPolicyController extends BaseController {
	@Autowired
	private BisAccGatherPolicyService bisAccGatherPolicyService;
	@Autowired
	private BisGatherPolicyService bisGatherPolicyService;
//
//	@Autowired
//	private StreamBridge streamBridge;

	@Autowired
	private TenantAwareStreamBridge tenantAwareStreamBridge;

	@GetMapping("/test")
	@ResponseBody
	public String test(){
		// 创建正确类型的消息
		TransactionMqMsg transactionMsg = new TransactionMqMsg();
		transactionMsg.setBusinessId("TEST-" + System.currentTimeMillis());
		transactionMsg.setBusinessType(BusinessType.AUTO_PAYMENT_INSTRUCTIONS);
		transactionMsg.setMessage("{\n" +
				"\"userName\":\"张三\",\n" +
				"\"userAge\":\"18\"\n" +
				"}");

		CommonMqMsg<TransactionMqMsg> commonMsg = new CommonMqMsg<>();
		commonMsg.setMsgId(UUID.randomUUID().toString());
		commonMsg.setData(transactionMsg);

		// 构建消息
		Message<CommonMqMsg<TransactionMqMsg>> message = MessageBuilder
				.withPayload(commonMsg)
				.setHeader("bookkeeping", "bookkeeping")
				.setHeader("messageId", commonMsg.getMsgId())
				.build();
		/*MimeType mimeType = MimeType.valueOf("application/json");
		boolean send = streamBridge.send("record-booking-out-0", message, mimeType);*/

		HashMap<String, Object> headers = new HashMap<>();
		headers.put("bookkeeping", "bookkeeping"); // routingKey
		headers.put("messageId", commonMsg.getMsgId());
		boolean send = tenantAwareStreamBridge.send("record-booking-out-0", commonMsg, UserContextHelper.helper().getTenantId(), headers);

		return "test";
	}

	/**
	 * 银行账号关系策略维护表列表 页面跳转
	 *
	 * @return
	 * @throws Exception
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISACCGATHERPOLICY_QUERY)
	@LogMessage(content = "查询银行账号关系策略维护表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(BisGatherPolicy bisGatherPolicy, Model model) throws Exception {
		model.addAttribute("treeDate", bisAccGatherPolicyService.listTreeData(bisGatherPolicy));
		return "allocation/listBisAccGatherPolicy";
	}
	/**
	 * 银行账号关系策略维护表列表 查询
	 *
	 * @return
	 * @throws Exception
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISACCGATHERPOLICY_QUERY)
	@LogMessage(content = "查询银行账号关系策略维护表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/queryList")
	public String queryList(BisGatherPolicy bisGatherPolicy, Model model,@RequestParam(required = false) String parentAccName) throws Exception {
		model.addAttribute("treeDate", bisAccGatherPolicyService.listTreeData(bisGatherPolicy,parentAccName));
		model.addAttribute("bisGatherPolicyPage", bisGatherPolicy);
		model.addAttribute("parentAccName", parentAccName);
		return "allocation/listBisAccGatherPolicy";
	}
	/**
	 * 父账号查询
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISACCGATHERPOLICY_APPLY)
	@LogMessage(content = "查询银行账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/listParentBankAcc/{gatherPolicyId}/{curId}")
	public String listParentBankAcc(@PathVariable("gatherPolicyId") String gatherPolicyId, @PathVariable("curId") String curId, Model model) {
		model.addAttribute("gatherPolicyId", gatherPolicyId);
		model.addAttribute("parentAccId", "");
		model.addAttribute("parentSign", "1");
		model.addAttribute("curId", curId);
		//调拨类型：0-定时归集，1-实时归集，2-定时下拨
		model.addAttribute("gatherType", bisGatherPolicyService.get(gatherPolicyId, true).getGatherType());
		return "allocation/selectBtBankAcc";
	}
	/**
	 * 子账号查询
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISACCGATHERPOLICY_APPLY)
	@LogMessage(content = "查询银行账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/listBankAcc/{gatherPolicyId}/{prentAccId}/{curId}")
	public String listBankAcc(@PathVariable("gatherPolicyId") String gatherPolicyId, @PathVariable("prentAccId") String prentAccId, @PathVariable("curId") String curId, Model model) {
		model.addAttribute("gatherPolicyId", gatherPolicyId);
		model.addAttribute("prentAccId", prentAccId);
		model.addAttribute("parentSign", "");
		model.addAttribute("curId", curId);
		//调拨类型：0-定时归集，1-实时归集，2-定时下拨
		model.addAttribute("gatherType", bisGatherPolicyService.get(gatherPolicyId, true).getGatherType());
		if(EmptyUtils.isEmpty(prentAccId)) {
			return "allocation/selectBtBankAcc";
		}else{
			return "allocation/selectChildBtBankAcc";
		}
	}
	/**
	 * 银行账号查询列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISACCGATHERPOLICY_APPLY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return bisAccGatherPolicyService.getDataTablesUnion(dataTablesInput);
	}

	@PostMapping("/childDatatable")
	@ResponseBody
	public DataTablesOutput childDatatable(@RequestBody DataTablesInput dataTablesInput) {
		return bisAccGatherPolicyService.getDataTablesUnionChild(dataTablesInput);
	}
	/**
	 * 添加银行账号关系策略维护表
	 *
	 * @param ids
	 * @param gatherPolicyId
	 * @param prentAccId
	 * @param curId
	 * @return
	 * @throws Exception
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISACCGATHERPOLICY_APPLY)
	@LogMessage(content = "添加银行账号关系策略维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(@RequestParam("ids") String ids, @RequestParam("gatherPolicyId") String gatherPolicyId,
						 @RequestParam("prentAccId") String prentAccId, @RequestParam("curId") String curId) throws Exception {
		if (!StringUtils.isEmpty(prentAccId)) {
			//增加子账号
			bisAccGatherPolicyService.saveChildAcc(ids, gatherPolicyId, prentAccId, curId, this.getCurrentUser());
		} else {
			//增加父账号
			bisAccGatherPolicyService.savePrentAcc(ids, gatherPolicyId, curId, this.getCurrentUser());
		}
		return success();
	}

	@LogMessage(content = "添加银行账号关系策略维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/saveChild")
	@ResponseBody
	public AjaxJson saveChild(@RequestParam("ids") String ids, @RequestParam("gatherPolicyId") String gatherPolicyId,
	@RequestParam("curId") String curId,@RequestParam("childIds") String childIds) throws Exception {
			//增加父账号
		bisAccGatherPolicyService.savePrentAcc(ids, gatherPolicyId, curId,childIds, this.getCurrentUser());
		return success();
	}
	/**
	 * 删除银行账号关系策略维护表
	 *
	 * @param bankAccId
	 * @param prentAccId
	 * @param gatherPolicyId
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISACCGATHERPOLICY_APPLY)
	@LogMessage(content = "删除银行账号关系策略维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete")
	@ResponseBody
	public AjaxJson delete(@RequestParam("bankAccId") String bankAccId, @RequestParam("prentAccId") String prentAccId,
						   @RequestParam("gatherPolicyId") String gatherPolicyId) {
		bisAccGatherPolicyService.deleteBisGatherPolicy(bankAccId, prentAccId, gatherPolicyId);
		return success();
	}
}
