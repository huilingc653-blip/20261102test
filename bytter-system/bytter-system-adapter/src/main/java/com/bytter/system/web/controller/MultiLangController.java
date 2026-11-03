package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.MultiLang;
import com.bytter.system.service.MultiLangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * Created by zhoul on 2017/5/2.
 */
@Controller
@RequestMapping("/sys/lang")
public class MultiLangController extends BaseController {
    @Autowired
    private MultiLangService multiLangService;
    
    @Autowired
    RedisTemplate redisTemplate;
    /**
     * 多语言列表 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public void list() {
    }
    @PostMapping("/tableGrid.json")
    @ResponseBody
    public TableGridResponse<MultiLang> tableGrid(@RequestBody TableGridInput tableGridInput) {
        IPageResult<MultiLang> pageResult = multiLangService.getTableGrid(tableGridInput);
        return pageResult.convert(TableGridResponse.class);
    }
    @GetMapping("/view")
    public void view(String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            MultiLang multiLang = multiLangService.get(id);
            model.addAttribute("multiLang", multiLang);
        }
    }
    /**
     * 添加多语言
     *
     * @param multiLang
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(MultiLang multiLang) {
        multiLangService.save(multiLang, getUserId());
        return success();
    }
    /**
     * 多语言列表页面跳转
     *
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(MultiLang multiLang) {
        multiLangService.update(multiLang, getUserId());
        return success();
    }
    /**
     * 删除多语言
     *
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(@RequestParam("langIds[]") List<String> langIds) {
        multiLangService.delete(langIds);
        return success();
    }
    
    @GetMapping("/initAllEnum")
    public AjaxJson initAllEnum() {
    	List<MultiLang> list = multiLangService.findList("");
    	int i = 0;
    	for(MultiLang lang:list) {
    		if(StringUtils.isNotEmpty(lang.getLangKey()) || StringUtils.isNotEmpty(lang.getLangContext()))
    		if(redisTemplate.opsForValue().get("enum:"+lang.getLangKey())!=null) {
    			redisTemplate.delete("enum:"+lang.getLangKey());
    		}
    		redisTemplate.opsForValue().set("enum:"+lang.getLangKey(), lang.getLangContext());
    		i++;
    		System.out.println("i=========="+i);
    	}
    	System.out.println("result of i =========="+i);
        return success();
    }
}
	
