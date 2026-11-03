package com.bytter.biz.fundwarn.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import org.apache.dubbo.config.annotation.DubboReference;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.fundwarn.entity.BtWarnCondition;
import com.bytter.biz.fundwarn.service.BtWarnStrategyService;
import com.bytter.biz.fundwarn.service.BtWarningLogService;
import com.bytter.biz.fundwarn.spi.constant.FundWarnConstant;
import com.bytter.biz.fundwarn.util.BtFundWarnUtils;
import com.bytter.biz.fundwarn.vo.BtWarnConditionVO;
import com.bytter.biz.fundwarn.vo.BtWarnStrategyVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
/**
 * @Title: Controller
 * @Description:预警策略控制器类
 * @author zhenghj
 * @date 2018-09-27
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/fundwarn/btwarnstrategy")
public class BtWarnStrategyController extends BaseController {
	@Autowired
	private BtWarnStrategyService btWarnStrategyService;
	@Autowired
	private BtWarningLogService btWarningLogService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	/**
	 * 预警策略管理页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(FundWarnConstant.RESOURCE_WARNSTRATEGY_QUERY)
	@LogMessage(content = "预警策略管理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model, String overdueWarn) {
		if(StringUtils.isNotEmpty(overdueWarn)){
			model.addAttribute("overdueWarn",overdueWarn);
		}
		return "fundwarn/listBtWarnStrategy";
	}

	/**
	 * 预警策略管理table列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	//@RequiresPermissions(FundWarnConstant.RESOURCE_WARNSTRATEGY_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return btWarnStrategyService.listWarnStrategy(dataTablesInput, getCurrentUser());
	}

	/**
	 * 预警策略新增页面跳转
	 *
	 * @return
	 */
	@GetMapping("/add")
	public String add(Model model, String overdueWarn) {
		model.addAttribute("typecodeDimensionMap", BtFundWarnUtils.getAllTypecodeDimension());
		if(StringUtils.isNotEmpty(overdueWarn)){
			model.addAttribute("overdueWarn",overdueWarn);
		}
		String code = SequenceSingleton.getSequence(SequenceEnum.YJ_BTWARNSTRATEGY_CODE);
		model.addAttribute("code",code);
		return "fundwarn/addBtWarnStrategy";
	}



    /**
     * 根据单位id获取数据  数据展示用
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/showCorpByCorpIds")
    @ResponseBody
    public DataTablesOutput showCorpByCorpIds(@RequestBody DataTablesInput dataTablesInput) {
        String corpAccSelectQuary = String.valueOf(dataTablesInput.getFormdata().get("corpAccSelectQuary"));
        if ("1".equals(corpAccSelectQuary)){
            return new DataTablesOutput();
        }
        List<String> corps = getCorpIdOrAccByInput(dataTablesInput);
        return btWarnStrategyService.getDataTablesCorpByCorpIds(dataTablesInput,corps);
    }

	/**
	 * 根据银行账号,币别获取数据  数据展示用
	 */
	@PostMapping("/getNotDeleteAndValidAndBankAcc")
	@ResponseBody
	public DataTablesOutput getNotDeleteAndValidAndBankAcc(@RequestBody DataTablesInput input){
		List<String> banks = getCorpIdOrAccByInput(input);
		return btWarnStrategyService.getNotDeleteAndValidAndBankAcc(input,banks);
	}

	/**
	 * 根据内部账号,币别获取数据  数据展示用
	 */
	@PostMapping("/getblnctrAccByAccAndcurId")
	@ResponseBody
	public DataTablesOutput getblnctrAccByAccAndcurId(@RequestBody DataTablesInput input){
		List<String> blnctrAccs = getCorpIdOrAccByInput(input);
		return btWarnStrategyService.getblnctrAccByAccAndcurId(input,blnctrAccs);
	}

	/**
	 * 获取前台传的单位id、银行账号、内部账号，处理成集合
	 * @param dataTablesInput
	 * @return
	 */
	public List<String> getCorpIdOrAccByInput(DataTablesInput dataTablesInput){
		String[] accs = new String[] {};
		if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("corpIdAndAccStr"))){
			//显示全部
			if("all".equals(dataTablesInput.getFormdata().get("corpIdAndAccStr"))){
				accs=new String[]{};
			}else if("select".equals(dataTablesInput.getFormdata().get("corpIdAndAccStr"))){
				//不显示
				accs=new String[]{" "};
			}else{
				//显示选择的数据
				accs=dataTablesInput.getFormdata().get("corpIdAndAccStr").toString().split(",");
			}
		}
		List<String> accList = new ArrayList<String>();
        Collections.addAll(accList, accs);
		return accList;
	}

	/**
	 * 判断策略代码是否重复
	 */
	@PostMapping("/checkWarnCode")
	@ResponseBody
	public AjaxJson checkWarnCode(String warnCode) {
		boolean flag = btWarnStrategyService.checkWarnCode(warnCode,getCurrentUser().getTenantId());
		AjaxJson json = success();
		json.setSuccess(flag);
		return json;
	}

	/**
	 * 新增预警策略
	 *
	 * @param strategyVO  预警策略主信息VO
	 * @param conditionVO 预警策略条件VO
	 * @return
	 */
	//@RequiresPermissions(FundWarnConstant.RESOURCE_WARNSTRATEGY_OP)
	@LogMessage(content = "新增预警策略", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BtWarnStrategyVO strategyVO, BtWarnConditionVO conditionVO) {
		btWarnStrategyService.save(strategyVO, conditionVO, this.getCurrentUser());
		return success();
	}
	/**
	 * 编辑或复制页面跳转
	 *
	 * @param warnId 预警Id
	 * @param opType 操作类型 update 编辑更新，copy 复制
	 * @param model
	 * @return
	 */
	//@RequiresPermissions(FundWarnConstant.RESOURCE_WARNSTRATEGY_OP)
	@GetMapping("/updateOrCopy")
	public String updateOrCopy(String warnId,String opType, String overdueWarn, Model model) {
		if(StringUtils.isNotEmpty(overdueWarn)){
			model.addAttribute("overdueWarn",overdueWarn);
		}

		BtWarnStrategyVO btWarnStrategy = null;
		BtWarnConditionVO btWarnCondition = null;
		String corpIdAndAccStr = "";//单位,银行账号,内部账号
		if (StringUtils.isNotEmpty(warnId)) {
			btWarnStrategy = btWarnStrategyService.getBtWarnStrategyInfo(warnId);
			List<BtWarnCondition> warnConditions = btWarnStrategyService.getWarnConditionByWarnId(warnId);
			btWarnCondition = new BtWarnConditionVO();
			//循环判断预警策略条件表数据，拼接单位id、银行账号或内部账号。拼接银行账号属性、银行账号类别、
			// 银行账号性质 、内部账户性质、会计科目
			for (BtWarnCondition condetion : warnConditions) {
				switch (condetion.getDataType()) {
					case FundWarnConstant.CONDITION_TYPE_CORP:
						corpIdAndAccStr = getCondetionValueStr(corpIdAndAccStr, condetion.getDataValue());
						break;
					case FundWarnConstant.CONDITION_TYPE_BANKACC:
						corpIdAndAccStr = getCondetionValueStr(corpIdAndAccStr, condetion.getDataValue());
						break;
					case FundWarnConstant.CONDITION_TYPE_FCACC:
						corpIdAndAccStr = getCondetionValueStr(corpIdAndAccStr, condetion.getDataValue());
						break;
					case FundWarnConstant.CONDITION_TYPE_BANKACCATTRIBUTE:
						btWarnCondition.setBankAttStr(getCondetionValueStr(btWarnCondition.getBankAttStr(), condetion.getDataValue()));
						break;
					case FundWarnConstant.CONDITION_TYPE_BANKACCTYPE:
						btWarnCondition.setBankAccTypeStr(getCondetionValueStr(btWarnCondition.getBankAccTypeStr(), condetion.getDataValue()));
						break;
					case FundWarnConstant.CONDITION_TYPE_BANKACCNATURE:
						btWarnCondition.setBankAccOnlineStr(getCondetionValueStr(btWarnCondition.getBankAccOnlineStr(), condetion.getDataValue()));
						break;
					case FundWarnConstant.CONDITION_TYPE_FCACCNATURE:
						btWarnCondition.setBankAccNatureStr(getCondetionValueStr(btWarnCondition.getBankAccNatureStr(), condetion.getDataValue()));
						break;
					case FundWarnConstant.CONDITION_TYPE_ACCSUBJECT:
						btWarnCondition.setSubjectNameStr(getCondetionValueStr(btWarnCondition.getSubjectNameStr(), condetion.getDataValue()));
						break;
					case FundWarnConstant.CONDITION_TYPE_WARNCONDITIONRATE:
						btWarnCondition.setWarnConditionRate(getCondetionValueStr(btWarnCondition.getWarnConditionRate(), condetion.getDataValue()));
						break;
					case FundWarnConstant.CONDITION_TYPE_RATINGSELECTION:
						btWarnCondition.setRatingSelection(getCondetionValueStr(btWarnCondition.getRatingSelection(), condetion.getDataValue()));
						break;
					case FundWarnConstant.CONDITION_TYPE_DATENUM:
						btWarnCondition.setDateNum(getCondetionValueStr(btWarnCondition.getDateNum(), condetion.getDataValue()));
						break;
					case FundWarnConstant.CONDITION_TYPE_TIME_TYPE:
						btWarnCondition.setTimeType(getCondetionValueStr(btWarnCondition.getDateNum(), condetion.getDataValue()));
						break;
				}
			}
		}
		model.addAttribute("btWarnStrategy", btWarnStrategy);
		model.addAttribute("btWarnCondition", btWarnCondition);
		if (!EmptyUtils.isEmpty(btWarnCondition.getDateNum())) {
			String[] ss = btWarnCondition.getDateNum().split(",");
			//从小到大排序
			Arrays.sort(ss,(s1, s2) -> Integer.compare(Integer.parseInt(s1), Integer.parseInt(s2)));
			model.addAttribute("dateNum", ss);
		}
		if("all".equals(corpIdAndAccStr)){
			model.addAttribute("corpAccSelect",0);
		}else{
			model.addAttribute("corpAccSelect",1);
		}
		if("all".equals(btWarnCondition.getSubjectNameStr())){
			model.addAttribute("subjectRadio","0");
		}else{
			model.addAttribute("subjectRadio","1");
		}
		model.addAttribute("corpIdAndAccStr", corpIdAndAccStr);
//		String corpArr = iCorpService.listAllTreeW(null, null, this.getCurrentUser());//vm页面单位组件不带资源
		String corpArr = iCorpService.getFilterCorpW(FundWarnConstant.RESOURCE_WARNSTRATEGY_QUERY, this.getCurrentUser());//vm页面单位组件带资源
		model.addAttribute("corpArr", corpArr);
		model.addAttribute("typecodeDimensionMap", BtFundWarnUtils.getAllTypecodeDimension());
		String path = null;
		if(FundWarnConstant.WARNSTRATEGY_OPTYPE_UPDATE.equals(opType)){
			path = "fundwarn/updateBtWarnStrategy";
		}else if(FundWarnConstant.WARNSTRATEGY_OPTYPE_COPY.equals(opType)){
			String code = SequenceSingleton.getSequence(SequenceEnum.YJ_BTWARNSTRATEGY_CODE);
			model.addAttribute("code",code);
			path = "fundwarn/copyBtWarnStrategy";
		}
		return path;

	}

	/**
	 * 修改预警策略
	 *
	 * @param strategyVO
	 * @param conditionVO
	 * @return
	 */
	@LogMessage(content = "修改预警策略", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson modify(BtWarnStrategyVO strategyVO,BtWarnConditionVO conditionVO) {
		AjaxJson j = success();
		boolean flag = btWarnStrategyService.modifyWarnStrategyCondition(strategyVO,conditionVO, this.getCurrentUser());
		j.setSuccess(flag);
		return j;
	}

	/**
	 * 复制预警策略
	 *
	 * @param strategyVO  预警策略主信息VO
	 * @param conditionVO 预警策略条件VO
	 * @return
	 */
	//@RequiresPermissions(FundWarnConstant.RESOURCE_WARNSTRATEGY_OP)
	@LogMessage(content = "复制预警策略", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/copy")
	@ResponseBody
	public AjaxJson copy(BtWarnStrategyVO strategyVO, BtWarnConditionVO conditionVO) {
		btWarnStrategyService.copy(strategyVO, conditionVO, this.getCurrentUser());
		return success();
	}



	/**
	 * 预警策略扫描参数维护页面跳转
	 *
	 * @return
	 */
	@GetMapping("/scanParameter")
	public String scanParameter(String warnId, Model model) {
		BtWarnStrategyVO strategyVO = btWarnStrategyService.getBtWarnStrategyInfo(warnId);
		model.addAttribute("btWarnStrategy", strategyVO);
		return "fundwarn/scanParameter";
	}

	/**
	 * 预警策略扫描参数维护
	 *
	 * @return
	 */
	@LogMessage(content = "添加或者修改预警策略扫描参数", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/saveScanParameter")
	@ResponseBody
	public AjaxJson saveScanParameter(BtWarnStrategyVO strategyVO) {
		AjaxJson j = success();
		boolean flag = btWarnStrategyService.saveScanParameter(strategyVO, this.getCurrentUser());
		j.setSuccess(flag);
		return j;
	}

	/**
	 * 预警策略扫描参数手动执行
	 *
	 * @return
	 */
	@PostMapping("/executeScan")
	@ResponseBody
	public AjaxJson executeScan(BtWarnStrategyVO strategyVO) {
		AjaxJson j = success();
		strategyVO.setCurrentUser(this.getCurrentUser());
		boolean flag = btWarnStrategyService.executeScan(strategyVO);
		j.setSuccess(flag);
		return j;
	}

	/**
	 * 拼接值
	 * @param typeStr
	 * @param value
	 * @return
	 */
	public String getCondetionValueStr(String typeStr,String value){
		if(StringUtils.isNotEmpty(typeStr)){
			typeStr+=","+value;
		}else{
			if(StringUtils.isNotEmpty(value)){
				typeStr=value;
			}else{
				typeStr="all";
			}
		}
		return typeStr;
	}


	/**
	 * 逾期账款超标一键初始化
	 */
	@LogMessage(content = "逾期账款超标一键初始化", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/initExReceivaOver")
	@ResponseBody
	public AjaxJson initExReceivaOver(){
		btWarnStrategyService.initExReceivaOver(getCurrentUser());
		return success();
	}

	/**
	 * 逾期时间预警一键初始化
	 */
	@LogMessage(content = "逾期时间预警一键初始化", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/initTimeWarnOver")
	@ResponseBody
	public AjaxJson initTimeWarnOver(){
		btWarnStrategyService.initTimeWarnOver(getCurrentUser());
		return success();
	}

	/**
	 * 单条预警策略（YJ001~YJ021）恢复初始化操作
	 */
	@LogMessage(content = "单条预警策略（YJ001~YJ021）恢复初始化操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/goInit")
	@ResponseBody
	public AjaxJson goInit(String code){
		btWarnStrategyService.goInit(code,getCurrentUser());
		return success();
	}
}
