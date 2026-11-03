package com.bytter.biz.netbank.controller;
import com.bytter.biz.netbank.entity.BtAssignPayWay;
import com.bytter.biz.netbank.service.BtAssignPayWayService;
import com.bytter.biz.netbank.service.FcVoucherTypeService;
import com.bytter.biz.netbank.vo.BtAssignPayWayUserVO;
import com.bytter.biz.netbank.vo.BtAssignPayWayVO;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title: Controller
 * @Description: 付款方式分配表
 * @author onlineGenerator
 * @date 2017-05-17 10:08:00
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/netbank/btassignpayway")
public class BtAssignPayWayController extends BaseController {
	@Autowired
	private BtAssignPayWayService btAssignPayWayService;
	@Autowired
	private FcVoucherTypeService fcVoucherTypeService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IUserService userService;
    /**
    * 付款方式分配表 table列表
    *
    * @return
    */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTASSIGNPAYWAY_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return btAssignPayWayService.getDataTablesUnion(dataTablesInput);
    }
	/**
	 * 用户付款方式分配表 table列表
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTASSIGNPAYWAY_APPLY)
	@SuppressWarnings("rawtypes")
	@PostMapping("/userDatatable")
	@ResponseBody
	public DataTablesOutput userDatatable(@RequestBody  DataTablesInput dataTablesInput){
		return btAssignPayWayService.getUserDataTablesUnion(dataTablesInput);
	}
	/**
	* 付款方式分配表新增页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTASSIGNPAYWAY_APPLY)
	@GetMapping("/add")
	public String add(BtAssignPayWay btAssignPayWay, Model model) {
		if (StringUtils.isNotEmpty(btAssignPayWay.getId())) {
			btAssignPayWay = btAssignPayWayService.get(btAssignPayWay.getId());
			model.addAttribute("btAssignPayWayPage", btAssignPayWay);
		}
		//获取有效的付款方式
		model.addAttribute("voucherTypes", fcVoucherTypeService.getFcVoucherTypeForAssign());
		//获取分配的单位
		model.addAttribute("corps",btAssignPayWayService.getAssignCorps());
		String isEnableUserAssign = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISENABLEUSERASSIGN).getValue();
		if(NetbankConstant.ISENABLEUSERASSIGN_0.equals(isEnableUserAssign)) {
			return "netbank/addBtAssignPayWay";
		}else{
			return "netbank/addBtAssignPayWayUser";
		}
	}
	/**
	 * 添加付款方式分配表
	 * @param btAssignPayWay
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTASSIGNPAYWAY_APPLY)
	@LogMessage(content = "添加付款方式分配表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BtAssignPayWayVO btAssignPayWay){
		if (StringUtils.isEmpty(btAssignPayWay.getCorpId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_CORPID_NULL);
        }
		String[] voucherCodes = null;
		if(StringUtils.isNotBlank(btAssignPayWay.getVoucherCodesStr())){
			voucherCodes = btAssignPayWay.getVoucherCodesStr().split(CommonConstant.COMMA_SPLIT);
		}
		btAssignPayWayService.assignPayWay(btAssignPayWay, voucherCodes, this.getCurrentUser());
		return success();
	}
	/**
	 * 添加用户付款方式分配表
	 * @param btAssignPayWayUser
	 * @return
	 */
	@LogMessage(content = "添加用户付款方式分配表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/saveUser")
	@ResponseBody
	public AjaxJson saveUser(BtAssignPayWayUserVO btAssignPayWayUser){
		if (StringUtils.isEmpty(btAssignPayWayUser.getUserId())) {
			throw new ParameterException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_USER_NULL);
		}
		String[] voucherCodes = null;
		if(StringUtils.isNotBlank(btAssignPayWayUser.getVoucherCodesStr())){
			voucherCodes = btAssignPayWayUser.getVoucherCodesStr().split(CommonConstant.COMMA_SPLIT);
		}
		btAssignPayWayService.assignPayWayUser(btAssignPayWayUser, voucherCodes, this.getCurrentUser());
		return success();
	}
	/**
	 * 费用报销查询有效的付款方式-过滤系统结算方式为转付、代付、外付网银、代发工资
	 * @param corpId
	 * @return
	 */
	@RequestMapping(params = "getAssignPayWayFilter")
	@ResponseBody
	public AjaxJson getAssignPayWayFilter(String corpId){
		List<BtAssignPayWayVO> listSpecialVoucherType = new ArrayList<>();
		List<BtAssignPayWayVO> listAll = btAssignPayWayService.getByCorpId(corpId);
		for(BtAssignPayWayVO type : listAll){
			if(NetbankConstant.PAY_TYPE_02.equals(type.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_33.equals(type.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_35.equals(type.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_34.equals(type.getSysVoucherCode())){
				listSpecialVoucherType.add(type);
			}
		}
		AjaxJson j=new AjaxJson();
		j.setSuccess(true);
		j.setObj(listSpecialVoucherType);
		return j;
	}
	/**
	 * 查询已分配的有效的付款方式
	 * @param corpId
	 * @return
	 */
	@RequestMapping(params = "getAssignPayWay")
	@ResponseBody
	public AjaxJson getAssignPayWay(String corpId){
		AjaxJson j=new AjaxJson();
		j.setSuccess(true);
		j.setObj(btAssignPayWayService.getByCorpId(corpId));
		return j;
	}
	/**
	 * 查询已分配的有效的付款方式
	 * @param corpId
	 * @return
	 */
	@RequestMapping(params = "getAssignPayWayAll")
	@ResponseBody
	public AjaxJson getAssignPayWayAll(String corpId){
		AjaxJson j=new AjaxJson();
		j.setSuccess(true);
		j.setObj(btAssignPayWayService.getAll(corpId,this.getUserId()));
		return j;
	}
	/**
	 * 查询已分配的有效的付款方式
	 * @param userId
	 * @return
	 */
	@RequestMapping(params = "getAssignPayWayByUserId")
	@ResponseBody
	public AjaxJson getAssignPayWayByUserId(String userId){
		AjaxJson j=new AjaxJson();
		j.setSuccess(true);
		j.setObj(btAssignPayWayService.getByUserId(userId));
		return j;
	}
	/**
	 * 查询所有有效供付款方式分配的付款方式
	 *
	 * @return
	 */
	@GetMapping("/json")
	@ResponseBody
	public List<FcVoucherTypeDTO> json() {
		return fcVoucherTypeService.getFcVoucherTypeForAssign();
	}
	/**
	 * 外汇管理-获取已分配的系统结算方式为34-外付网银的付款方式
	 * @param corpId
	 * @return
	 */
	@RequestMapping(params = "getAssignPayWayForForeign")
	@ResponseBody
	public List<BtAssignPayWayVO> getAssignPayWayForForeign(String corpId){
		List<BtAssignPayWayVO> listSpecialVoucherType = new ArrayList<>();
		List<BtAssignPayWayVO> listAll = btAssignPayWayService.getByCorpId(corpId);
		for(BtAssignPayWayVO type : listAll){
			if(NetbankConstant.PAY_TYPE_34.equals(type.getSysVoucherCode())){
				listSpecialVoucherType.add(type);
			}
		}
		return listSpecialVoucherType;
	}
	/**
	 * 获取有效的用户
	 * @return
	 */
	@PostMapping("/getUserDTO")
	@ResponseBody
	public List<UserDTO> getUserDTO(){
		List<UserDTO>  userDTOList = userService.listAllUser();
		if(EmptyUtils.isNotEmpty(userDTOList)){
			for(UserDTO userDTO : userDTOList){
				String name = userDTO.getLoginName()+"("+userDTO.getUsername()+")";
				userDTO.setLoginName(name);
			}
		}
		return userDTOList;
	}
}
