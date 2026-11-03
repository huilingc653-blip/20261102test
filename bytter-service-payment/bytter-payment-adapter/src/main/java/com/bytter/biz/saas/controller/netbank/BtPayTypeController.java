package com.bytter.biz.saas.controller.netbank;

import cn.hutool.core.bean.BeanUtil;
import com.bytter.biz.netbank.entity.BtPayType;
import com.bytter.biz.netbank.service.BtPayTypeService;
import com.bytter.biz.netbank.vo.BtPayTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Title: Controller
 * @Description: 系统结算方式表
 * @author onlineGenerator
 * @date 2017-05-12 13:44:54
 * @version V1.0
 *
 */
@Api(value = "/v1-payment/netbank/btpaytype",tags = "系统结算方式")
@RestController("v1BtPayTypeController")
@RequestMapping("/v1-payment/netbank/btpaytype")
public class BtPayTypeController extends BaseController {
	@Autowired
	private BtPayTypeService btPayTypeService;
	@DubboReference(check = false)
	private IDictService dictService;

    /**
    * 系统结算方式表 table列表
    *
    * @return
    */
	@ApiOperation("系统结算方式表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<BtPayTypeVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesOutput<BtPayTypeVO> result=new DataTablesOutput<>();
		DataTablesInput input = dataTablesInput.convertAndInitColumns( );
		Map<String, Object> vo = dataTablesInput.getParams();
		if (vo != null && !vo.isEmpty()) {
			Optional.ofNullable(vo.get("sysVoucherName"))
					.ifPresent(status -> input.addFormData(MatchTypeEnum.EQ, "sysVoucherName",
							status, String.class));
			Optional.ofNullable(vo.get("sysVoucherCode"))
					.ifPresent(status -> input.addFormData(MatchTypeEnum.EQ, "sysVoucherCode",
							status, String.class));
		}
		DataTablesOutput<BtPayType> btPayTypeDataTablesOutput=	btPayTypeService.getDataTables(input);
		BeanUtils.copyProperties(btPayTypeDataTablesOutput, result);
		if(EmptyUtils.isNotEmpty(btPayTypeDataTablesOutput.getData())){
			Map<String, DictDTO> dictMap = dictService.listMap("sf_yn");
		List<BtPayTypeVO> list=	BeanUtil.copyToList(btPayTypeDataTablesOutput.getData(), BtPayTypeVO.class);
			list.forEach(e->{
				e.setValidSignName(dictMap.get(e.getValidSign()).getName());
			});
			result.setData(list);
		}
		return AjaxJson.success(result);
    }


	/**
	* 系统结算方式表详情页
	*
	* @return
	*/
	@ApiOperation("系统结算方式表详情页,修改页面的数据也是这个接口获取")
	@GetMapping("/update/{id}")
	public AjaxJson<BtPayType> update(@PathVariable("id") String id) {
		return AjaxJson.success(btPayTypeService.get(id));
	}

	/**
	 * 获取有效的系统结算方式
	 * @return
	 */
	@ApiOperation("获取有效的系统结算方式")
	@GetMapping("/json")
	public AjaxJson<List<BtPayTypeVO>> json(){
		return AjaxJson.success(btPayTypeService.getValidPayType());
	}

	/**
	 * 添加系统结算方式表
	 * @param btPayType
	 * @return
	 */
	@ApiOperation("添加系统结算方式表")
	@LogMessage(content = "添加系统结算方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	public AjaxJson save(@RequestBody  BtPayType btPayType){
		btPayTypeService.save(btPayType);
		return success();
	}

	/**
	 * 修改系统结算方式表
	 * @param btPayType
	 * @return
	 */
	@ApiOperation("修改系统结算方式表")
	@LogMessage(content = "修改系统结算方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	public AjaxJson modify(@RequestBody  BtPayType btPayType){
		BtPayType t = btPayTypeService.get(btPayType.getId());
		BeanUtils.copyNotNullProperties(btPayType, t);
		btPayTypeService.saveOrUpdate(t);
		return  success();
	}

	/**
	 * 删除系统结算方式表
	 * @param id
	 * @return
	 */
	@ApiOperation("删除系统结算方式表")
	@LogMessage(content = "删除系统结算方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	public AjaxJson delete(@PathVariable("id") String id){
		BtPayType btPayType = btPayTypeService.get(id);
		btPayTypeService.deleteEntity(btPayType);
		return success();
	}

	/**
	 * 批量删除系统结算方式表
	 * @param ids
	 * @return
	 */
	@ApiOperation("批量删除系统结算方式表")
	@LogMessage(content = "批量删除系统结算方式表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BtPayType btPayType = btPayTypeService.get(id);
			btPayTypeService.deleteEntity(btPayType);
		}
		return success();
	}

}
