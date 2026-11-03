package com.bytter.biz.netbank.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.netbank.service.ChaeOpenCorpService;
import com.bytter.biz.netbank.vo.ChaeOpenCorpVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Title: Controller
 * @Description: 差额下拨启用单位
 * @author onlineGenerator
 * @date 2017-05-17 19:23:52
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/netbank/chaeOpenCorp")
public class ChaeOpenCorpController extends BaseController {
	@Autowired
	private IConfigService iConfigService;
	@Autowired
	private ICorpService iCorpService;
	@Autowired
	private ChaeOpenCorpService chaeOpenCorpService;

	@GetMapping("/list")
	public String list(Model model) {
        return "netbank/listChaeOpenCorp";
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
			newMap.put("isChaeOpenCorp",map2.get(key).toString());
			newMap.put("isChaeOpenCorpName","1".equals(map2.get(key).toString()) ? "是" : "否");
			CorpDTO corpDTO = iCorpService.get(key);
			newMap.put("corpCode",corpDTO.getCode());
			newMap.put("corpName",corpDTO.getName());
			returnList.add(newMap);
		}
		return returnList;
	}

    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return chaeOpenCorpService.dataTable(dataTablesInput);
    }

	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(String corpId){
		if(StringUtils.isBlank(corpId)){
			return AjaxJson.failed("请先选择单位数据");
		}
		chaeOpenCorpService.save(corpId);
		return success();
	}

	@PostMapping("/batchUpdate")
	@ResponseBody
	public AjaxJson batchUpdate(String corpIds,String values){
		if(StringUtils.isBlank(corpIds)){
			return AjaxJson.failed("请先选择数据");
		}
		List<Map<String, String>> list = ChaeOpenCorpList(corpIds);
		if(CollectionUtil.isEmpty(list) || list.size() != corpIds.split(",").length){
			return AjaxJson.failed("数据错误");
		}
		String[] split = values.split(",");
		String[] split1 = corpIds.split(",");
		for(int i = 0; i < split1.length; i++){
			String cid = split1[i];
			Map<String, Object> configMap = iConfigService.getConfigMap("nis-isChaeOpenCorp", cid);
			Map<String,String> updMap = new HashMap<>();
			updMap.put("value",split[i]);
			updMap.put("id",configMap.get("id").toString());
			iConfigService.saveOrUpdate2(updMap);
		}
		return  success();
	}

	@PostMapping("/delete")
	@ResponseBody
	public AjaxJson delete(String id){
		if(StringUtils.isBlank(id)){
			AjaxJson.failed("请先选择数据");
		}
		iConfigService.deleteById(id);
		return success();
	}

	/**
	 * 保存导入数据
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
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

}
