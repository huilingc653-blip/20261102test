package com.bytter.biz.saas.controller.netbank;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.netbank.dto.NisBatchUpdateCorpDTO;
import com.bytter.biz.netbank.dto.NisCorpIdDTO;
import com.bytter.biz.netbank.service.ChaeOpenCorpService;
import com.bytter.biz.netbank.vo.ChaeOpenCorpVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: Controller
 * @Description: 差额下拨启用单位
 * @author onlineGenerator
 * @date 2017-05-17 19:23:52
 * @version 1.0
 *
 */
@Api(value = "/v1-payment/netbank/chaeOpenCorp",tags = "差额下拨启用单位")
@RestController("v1ChaeOpenCorpController")
@RequestMapping("/v1-payment/netbank/chaeOpenCorp")
public class ChaeOpenCorpController extends BaseController {
	@Autowired
	private IConfigService iConfigService;
	@Autowired
	private ICorpService iCorpService;
	@Autowired
	private ChaeOpenCorpService chaeOpenCorpService;


    @ApiOperation("差额下拨启用单位列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<ChaeOpenCorpVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return AjaxJson.success(chaeOpenCorpService.dataTable(input));
    }



	@ApiOperation("添加差额下拨启用单位")
	@PostMapping("/save")
	public AjaxJson save(@RequestBody NisCorpIdDTO copIdDTO){
		String corpId = copIdDTO.getCorpId();
		if(StringUtils.isBlank(corpId)){
			return AjaxJson.failed("请先选择单位数据");
		}
		chaeOpenCorpService.save(corpId);
		return success();
	}

	@ApiOperation("批量修改差额下拨启用单位")
	@PostMapping("/batchUpdate")
	public AjaxJson batchUpdate(@RequestBody List<NisBatchUpdateCorpDTO> corpDTOList){
		if(corpDTOList == null || corpDTOList.size() == 0){
			return AjaxJson.failed("请先选择数据");
		}
		String corpIds = corpDTOList.stream()
				.map(NisBatchUpdateCorpDTO::getCorpId)
				.filter(StringUtils::isNotBlank)
				.collect(Collectors.joining(","));
		List<Map<String, String>> list = ChaeOpenCorpList(corpIds);
		if(CollectionUtil.isEmpty(list) || list.size() != corpIds.split(",").length){
			return AjaxJson.failed("数据错误");
		}
		for (NisBatchUpdateCorpDTO corpDTO : corpDTOList) {
			String corpId = corpDTO.getCorpId();
			Map<String, Object> configMap = iConfigService.getConfigMap("nis-isChaeOpenCorp", corpId);
			Map<String,String> updMap = new HashMap<>();
			updMap.put("value",corpDTO.getIsChaeOpenCorp());
			updMap.put("id",configMap.get("id").toString());
			iConfigService.saveOrUpdate2(updMap);
		}
		return  success();
	}

	@ApiOperation("删除修改差额下拨启用单位")
	@PostMapping("/delete/{id}")
	public AjaxJson delete(@PathVariable String id){
		if(StringUtils.isBlank(id)){
		return 	AjaxJson.failed("请先选择数据");
		}
		iConfigService.deleteById(id);
		return success();
	}

	/**
	 * 保存导入数据
	 * @param list
	 * @return
	 */
	@ApiOperation("保存导入数据")
	@PostMapping("/saveImport")
	public AjaxJson saveImport(@RequestBody List<ChaeOpenCorpVO> list) {
		for(ChaeOpenCorpVO vo : list){
			CorpDTO corpByCode = iCorpService.getCorpByCode(vo.getCorpCode());
			List<Map<String, String>> dataList = ChaeOpenCorpList(corpByCode.getId());
			if(!CollectionUtil.isEmpty(dataList)){
				return AjaxJson.failed(corpByCode.getCode() + corpByCode.getName() + "数据已存在");
			}
			Map<String,String> saveMap = new HashMap<>();
			saveMap.put("type","nis-isChaeOpenCorp");
			saveMap.put("name",corpByCode.getId());
			if("是".equals(vo.getIsEnableCorpChaeXB())){
				vo.setIsEnableCorpChaeXB("1");
			}else {
				vo.setIsEnableCorpChaeXB("0");
			}
			saveMap.put("value",vo.getIsEnableCorpChaeXB());
			iConfigService.saveOrUpdate2(saveMap);
		}
		return success();
	}


	private List<Map<String,String>> ChaeOpenCorpList(String corpId){
		Map<String, Object> map = iConfigService.listByType("nis-isChaeOpenCorp");
		Object o = map.get("nis-isChaeOpenCorp");
		if(o == null || ((Map)o).size() == 0){
			return null;
		}
		Map map1 = (Map)o;
		Map map2 = new TreeMap(map1);
		List<Map<String,String>> returnList = new ArrayList<>();
		for(Object oj : map2.keySet()){
			String key = oj.toString();
			if(StringUtils.isNotBlank(corpId)){
				String[] split = corpId.split(",");
				boolean b = false;
				for(String ss : split){
					if(ss.equals(key)){
						b = true;
						break;
					}
				}
				if(!b){
					continue;
				}
			}
			Map<String,String> newMap = new HashMap();
			newMap.put("corpId",key);
			if(map2!=null && map2.get(key)!=null){
				newMap.put("isChaeOpenCorp",map2.get(key).toString());
				newMap.put("isChaeOpenCorpName","1".equals(map2.get(key).toString()) ? "是" : "否");
			}
			CorpDTO corpDTO = iCorpService.get(key);
			newMap.put("corpCode",corpDTO.getCode());
			newMap.put("corpName",corpDTO.getName());
			returnList.add(newMap);
		}
		return returnList;
	}

}
