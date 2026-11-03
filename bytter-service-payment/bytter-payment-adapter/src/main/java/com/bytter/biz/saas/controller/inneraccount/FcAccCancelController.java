package com.bytter.biz.saas.controller.inneraccount;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.finacial.api.service.IFcAccWriteoffService;
import com.bytter.biz.inneraccount.dto.v1.FcAccCommonDTO;
import com.bytter.biz.inneraccount.service.FcAccCancelService;
import com.bytter.biz.inneraccount.service.FcAccManageService;
import com.bytter.biz.inneraccount.vo.FcCancelAccountVO;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.inneraccount.vo.v1.FcAccCancelAccountVO;
import com.bytter.biz.inneraccount.vo.v1.FcAccCheckVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.vo.FcAccListVO;
import com.bytter.system.api.vo.FcAccVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**   
 * @Title: Controller  
 * @Description: 内部账户注销信息
 * @author onlineGenerator
 * @date 2017-05-25 19:36:12
 * @version 1.0
 *
 */
@Api(value = "内部账户注销信息", tags = "内部账户注销信息")
@RestController("v1FcAccCancelController")
@RequestMapping("/v1-payment/inneraccount/fcacccancel")
public class FcAccCancelController extends BaseController {
	@Autowired
	private FcAccManageService fcAccService;
	@Autowired
	private FcAccCancelService fcAccCancelService;
	@DubboReference(check = false)
	private IFcAccWriteoffService fcAccWriteoffService;
    @DubboReference(check = false)
	private IFcCorpPoolService fcCorpPoolService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IDictService dictService;

    /**
    * 内部注销待复核信息表 table列表
    *及
    * @return
    */
	@ApiOperation("内部注销待复核信息表 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAccListVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(fcAccCancelService.getDataTablesUnion(input, getCurrentUser()));
    }

	@ApiOperation("资金池内部注销待复核信息表 table列表")
	@PostMapping("/datatablePool")
	@ResponseBody
	public AjaxJson<DataTablesOutput<FcAccVO>> dataTablePool(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(fcAccCancelService.getDataTablesUnionForPool(input, getCurrentUser()));
	}

	@ApiOperation("根据id查询账户信息")
	@ResponseBody
	@GetMapping("/getById")
	public AjaxJson<FcAccVO> getById(String id){
		FcAccVO fcAcc = fcAccService.getFcAcc(id);
		return success(fcAcc);
	}
	/**
	 * 注销保存
	 * @param fcCancelAccountVO
	 * @return
	 */
	@ApiOperation("账号注销保存")
	@LogMessage(content = "注销保存",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save/{opType}")
	@ResponseBody
	public AjaxJson save(@RequestBody FcCancelAccountVO fcCancelAccountVO, @PathVariable("opType") String opType){
		fcAccCancelService.saveCancel(fcCancelAccountVO,opType,this.getCurrentUser());
		return success();
	}
	/**
	 * 注销编辑
	 * @param fcCancelAccountVO
	 * @return
	 */
	@ApiOperation("账号注销编辑")
	@LogMessage(content = "注销编辑",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/edit/{opType}")
	@ResponseBody
	public AjaxJson edit(@RequestBody FcCancelAccountVO fcCancelAccountVO, @PathVariable("opType") String opType){
		fcAccCancelService.editCancel(fcCancelAccountVO,opType,this.getCurrentUser());
		return success();
	}
	/**
	* 取消--还原注销申请前的数据
	* @param id
	* @return
	*/
	@ApiOperation("取消--还原注销申请前的数据")
	@LogMessage(content = "还原改户申请前的数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/cancel/{id}")
	@ResponseBody
	public AjaxJson cancel(@PathVariable("id") String id){
		fcAccCancelService.canelEntity(id,getCurrentUser());
		return success();
	}

	@ApiOperation("根据id获取复核信息，用于复核check编辑")
	@LogMessage(content = "复核编辑页面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@GetMapping("/check")
	public AjaxJson<FcAccCheckVO> check(String id) {
		AjaxJson<FcAccCheckVO> ajaxJson = new AjaxJson<>();
		if (StringUtils.isNotEmpty(id)) {
			FcAccCheckVO fcAccCheckVo = new FcAccCheckVO();
			search(id,fcAccCheckVo);
			ajaxJson.setObj(fcAccCheckVo);
			ajaxJson.setSuccess(true);
		}
		return ajaxJson;
	}

	/**
	 * 查看复核公用
	 * @param id
	 * @param fcAccCheckVo
	 */
	private void search(String id, FcAccCheckVO fcAccCheckVo){
		FcAccVO fcAcc = fcAccService.findFcAccById(id);
		int accAccNoRule = configService.getConfig("fcs","accAccnoRule").toInteger(1).intValue();
		if(AllocationConstant.POOL_SIGN_1.equals(fcAcc.getPoolSign())){
			fcAcc = fcAccService.findFcAccByIdForPool(id);
			fcAcc.setNetId(fcAcc.getNetPoolId());
			fcAcc.setSubjectId(fcAcc.getSubjectPoolId());
			accAccNoRule = configService.getConfig("pool", "poolAccnoRule").toInteger(1).intValue();
			FcCorpPoolDTO fcCorpPoolDTO = fcCorpPoolService.getById(fcAcc.getNetPoolId());
			fcAccCheckVo.setNetCode(fcCorpPoolDTO.getPoolCode());
			fcAccCheckVo.setCorpCode((StringUtils.isEmpty(fcAcc.getCorpId()) || corpService.get(fcAcc.getCorpId())==null)?"":corpService.get(fcAcc.getCorpId()).getCode());
		}else{
			fcAccCheckVo.setNetCode((StringUtils.isEmpty(fcAcc.getNetId()) || corpService.get(fcAcc.getNetId())==null)?"": corpService.get(fcAcc.getNetId()).getCode());
			fcAccCheckVo.setCorpCode((StringUtils.isEmpty(fcAcc.getCorpId()) || corpService.get(fcAcc.getCorpId())==null)?"":corpService.get(fcAcc.getCorpId()).getCode());
			fcAccCheckVo.setUnionNetCode((StringUtils.isEmpty(fcAcc.getUnionNet()) || corpService.get(fcAcc.getUnionNet())==null)?"":corpService.get(fcAcc.getUnionNet()).getCode());
		}
		if (ObjectUtil.isNotEmpty(fcAcc.getAccType())){
			fcAcc.setAccTypeName(dictService.get("InnerAccount.AccType", fcAcc.getAccType()));
		}
		fcAcc.setAccAccNoRule(accAccNoRule);
		List<FcAccVO> accListByExpPay = fcAccService.getAccListByExpPay(fcAcc);
		String interestAccTemp = "";
		if(!EmptyUtils.isEmptyList(accListByExpPay)){
			for(int i = 0; i < accListByExpPay.size(); i++){
				if(i == 0){
					interestAccTemp = accListByExpPay.get(i).getBlnctrAcc();
				}else{
					interestAccTemp += ";" + accListByExpPay.get(i).getBlnctrAcc();
				}
			}
		}
		fcAcc.setInterestAcc(interestAccTemp);
		fcAccCheckVo.setFcAccPage(fcAcc);
		//顺序号生成模式
		int seqGeneratingMode = configService.getConfig("fcs","seqGeneratingMode").toInteger(1).intValue();
		fcAccCheckVo.setSeqNumModel(seqGeneratingMode);
		//根据资金池标志生成内部账号生成法则
		String reVal = fcAccService.getRuleVaql(AllocationConstant.POOL_SIGN_0);
		int relen = reVal.length();
		fcAccCheckVo.setRenlen(relen);
		//前后端分离后端生成顺序号  之前逻辑根据长度 以及 生成模式  取截取原内部账号的位数
		String seqNumFromBlnctrAcc = extractSeqNumFromBlnctrAcc(accAccNoRule, fcAcc.getBlnctrAcc());
		fcAccCheckVo.getFcAccPage().setSequenceNumber(seqNumFromBlnctrAcc);
	}

	@ApiOperation("注销复核通过或者打回")
	@LogMessage(content = "注销复核通过或者打回",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/checkPassOrBack")
	@ResponseBody
	public AjaxJson checkPassOrBack(@RequestBody FcAccVO fcAcc){
		fcAccCancelService.check(fcAcc,this.getCurrentUser());
		return  success();
	}
	/**
	 * 验证公共入口
	 * @return
	 */
	@ApiOperation("验证是否可以注销")
	@LogMessage(content = "验证是否可以注销",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/validFcAcc")
	@ResponseBody
	public AjaxJson validFcAcc(String id) {
		AjaxJson json=new AjaxJson();
		if (StringUtils.isNotEmpty(id)) {
			FcAccVO fcAcc = fcAccService.getFcAcc(id);
			fcAccCancelService.checkCommonEnter(fcAcc,getCurrentUser());
			if (fcAcc != null) {
				//非资金池的企业活期 并且 联机余额或者复核余额不为0 才需要调出记账分录　验证是否有效
				if(fcAccCancelService.isCurrType(fcAcc.getAccType()) && fcAccCancelService.validAmt(fcAcc.getBlnctrAcc())
						&& !AllocationConstant.POOL_SIGN_1.equals(fcAcc.getPoolSign())){
//					fcAccCancelService.getCancelPreData(fcAcc.getBlnctrAcc(),getCurrentUser());
					json.setObj(true);
				}
			}
		}
		json.setSuccess(true);
		return json;
	}
	/**
	 *
	 * 注销申请页面数据查询
	 * @return
	 */
	@ApiOperation("注销申请页面数据查询 传入account")
	@LogMessage(content = "注销申请页面数据查询",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/viewFcCancelAccount")
	public AjaxJson<FcAccCancelAccountVO> viewFcCancelAccount(@RequestBody FcAccCommonDTO fcAccCommonDTO) {
		AjaxJson<FcAccCancelAccountVO> ajaxJson = success();
		FcAccCancelAccountVO fcAccCancelAccountVo = new FcAccCancelAccountVO();
		String account = fcAccCommonDTO.getAccount(); //内部账号
		if (StringUtils.isNotEmpty(account)) {
			FcCancelAccountVO fcCancelAccountVO=fcAccCancelService.getCancelPreData(account,getCurrentUser());
			if("0".equals(fcCancelAccountVO.getIsNeedBusRule())){//不需要定义会计分录
				return ajaxJson;
			}
			fcAccCancelAccountVo.setListCancelVOPage(fcCancelAccountVO);
			Integer size = 0;
			if(!EmptyUtils.isEmpty(fcCancelAccountVO.getFcAccbookVoucher())
					&& !EmptyUtils.isEmpty(fcCancelAccountVO.getFcAccbookVoucher().getBookDtls())
					&& fcCancelAccountVO.getFcAccbookVoucher().getBookDtls().size() >0){
				size = fcCancelAccountVO.getFcAccbookVoucher().getBookDtls().size();
			}
			fcAccCancelAccountVo.setSize(size);
			ajaxJson.setObj(fcAccCancelAccountVo);
		}
		return ajaxJson;
	}
	/**
	 *
	 * 获取编辑页面的账号撤销相关数据信息
	 * @return
	 */
	@ApiOperation("获取编辑页面的账号撤销相关数据信息")
	@LogMessage(content = "获取编辑页面的账号撤销相关数据信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/getFcCancelAccountById")
	public AjaxJson<FcAccCancelAccountVO> getFcCancelAccountById(String account) {
		AjaxJson<FcAccCancelAccountVO> ajaxJson = new AjaxJson<>();
		FcAccCancelAccountVO fcAccCancelAccountVo = cancelEditPreData(account);
		ajaxJson.setObj(fcAccCancelAccountVo);
		return ajaxJson;
	}
	/**
	 * 获取记账分录页面
	 * @param account
	 * @return
	 */
	private FcAccCancelAccountVO cancelEditPreData(String account) {
		FcAccCancelAccountVO fcAccCancelAccountVo = new FcAccCancelAccountVO();
		if (StringUtils.isNotEmpty(account)) {
			FcCancelAccountVO fcCancelAccountVO = fcAccCancelService.getCancelEditPreData(account,getCurrentUser());
			if("0".equals(fcCancelAccountVO.getIsNeedBusRule())){//不需要定义会计分录
				return null;
			}
			fcAccCancelAccountVo.setListCancelVOPage(fcCancelAccountVO);
			Integer size = 0;
			if(!EmptyUtils.isEmpty(fcCancelAccountVO.getFcAccbookVoucher())
					&& !EmptyUtils.isEmpty(fcCancelAccountVO.getFcAccbookVoucher().getBookDtls())
					&& fcCancelAccountVO.getFcAccbookVoucher().getBookDtls().size() >0){
				size = fcCancelAccountVO.getFcAccbookVoucher().getBookDtls().size();
			}
			fcAccCancelAccountVo.setSize(size);
		}
		return fcAccCancelAccountVo;
	}
    /**
     * 获取资金池
     * @return
     */
    @ApiOperation("获取用户单位下的资金池")
	@GetMapping("/getUserCorpPools")
    @ResponseBody
    public AjaxJson<List<FcCorpPoolDTO>> getUserCorpPools() {
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELQUERY;
        return success(fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource));
    }
    /**
     * 获取资金池
     * @param poolId
     * @return
     */
    @GetMapping("/getUserCorpPoolChilds")
    @ResponseBody
    public AjaxJson<List<FcCorpPoolDTO>> getUserCorpPoolChilds(String poolId) {
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELQUERY;
        return success(fcCorpPoolService.getAllUserCorpPoolChilds(this.getCurrentUser(), resource, poolId));
    }


	public String extractSeqNumFromBlnctrAcc(int accAccNoRule, String blnctrAcc) {
		if (StringUtils.isEmpty(blnctrAcc)) {
			return "";
		}

		int tempLength;
		switch (accAccNoRule) {
			case 1:
				tempLength = 3;
				break;
			case 2:
				tempLength = 4;
				break;
			case 3:
				tempLength = 6;
				break;
			case 4:
				tempLength = 4;
				break;
			default:
				tempLength = 4;
		}

		if (blnctrAcc.length() < tempLength) {
			return blnctrAcc;
		}

		return blnctrAcc.substring(blnctrAcc.length() - tempLength);
	}
}
