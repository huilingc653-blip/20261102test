package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.common.rest.RestInput;
import com.bytter.core.common.rest.RestResponse;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.HomeWidget;
import com.bytter.system.service.DictService;
import com.bytter.system.service.HomeWidgetService;
import com.bytter.system.vo.DictVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/10/12.
 */
@Controller
@RequestMapping("/sys/homeWidget")
public class HomeWidgetController extends BaseController {
    private static final String WIDGET_TYPE_CHART = "chart";
    private static final String WIDGET_TYPE_WIDGET_CONTENT = "widgetContent";
    private static final String WIDGET_TYPE_USER_INFO = "userInfo";
    private static final String WIDGET_TYPE_TODO_INFO = "todoInfo";
    @Resource
    private HomeWidgetService homeWidgetService;
    @Resource
    private DictService dictService;
    @GetMapping("/condition")
    @ResponseBody
    public Object condition() {
        List<DictVO> type = dictService.getDictGroupByCode("sys.home.widget.type");
        List<DictVO> width = dictService.getDictGroupByCode("sys.home.widget.width");
        List<DictVO> height = dictService.getDictGroupByCode("sys.home.widget.height");
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("width", width);
        map.put("height", height);
        return map;
    }
    @GetMapping("/index")
    public void index() {
    }
    @PostMapping("/tableGrid.json")
    @ResponseBody
    public TableGridResponse todoListGrid(@RequestBody TableGridInput tableGridInput) {
        return homeWidgetService.getTableGrid(tableGridInput);
    }
    @PostMapping("/list")
    @ResponseBody
    public Object list(@RequestBody RestInput input) {
        IParameter parameter = input.getParameter();
        String type = parameter.getPara("type");
        Integer width = parameter.getParaToInt("width");
        Integer height = parameter.getParaToInt("height");
        IPageResult pageResult = homeWidgetService.getPageResult(input, type, width, height, getUserId());
        if (pageResult != null) {
            return pageResult.convert(RestResponse.class);
        }
        return null;
    }
    @PostMapping("/save")
    @ResponseBody
    public Object save(HomeWidget homeWidget) {
        homeWidgetService.save(homeWidget);
        return success();
    }
    @PostMapping("/update")
    @ResponseBody
    public Object update(HomeWidget homeWidget) {
        homeWidgetService.update(homeWidget);
        return success();
    }
    @GetMapping("/view")
    public void view(String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            HomeWidget homeWidget = homeWidgetService.get(id);
            model.addAttribute("homeWidget", homeWidget);
        }
    }
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(@RequestParam("ids[]") List<String> ids) {
        homeWidgetService.delete(ids);
        return success();
    }
}
