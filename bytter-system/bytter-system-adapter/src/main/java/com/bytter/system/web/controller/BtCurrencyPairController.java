package com.bytter.system.web.controller;
import com.bytter.basic.entity.BtCurrencyPair;
import com.bytter.basic.service.BtCurrencyPairService;
import com.bytter.basic.vo.BtCurrencyPairVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @version 1.0
 * @Title: Controller
 * @Description: 货币对管理
 * @date 2020-1-19 11:14:22
 */
@Controller
@RequestMapping("/basic/business/btcurrencypair")
public class BtCurrencyPairController extends BaseController {
    @Autowired
    private BtCurrencyPairService btCurrencyPairService;
    /**
     * 货币对管理列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询货币对管理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "basic/business/listBtCurrencyPair";
    }
    /**
     * 货币对管理 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btCurrencyPairService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 货币对管理新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(BtCurrencyPair btCurrencyPair, Model model) {
        if (StringUtils.isNotEmpty(btCurrencyPair.getId())) {
            btCurrencyPair = btCurrencyPairService.get(btCurrencyPair.getId());
            model.addAttribute("btCurrencyPairPage", btCurrencyPair);
        }
        return "basic/business/addBtCurrencyPair";
    }
    /**
     * 货币对管理编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(BtCurrencyPair btCurrencyPair, Model model) {
        if (StringUtils.isNotEmpty(btCurrencyPair.getId())) {
            BtCurrencyPairVO vo = this.btCurrencyPairService.getByUnion(btCurrencyPair.getId());
            model.addAttribute("btCurrencyPairPage", vo);
        }
        return "basic/business/updateBtCurrencyPair";
    }
    /**
     * 货币对管理详情页
     *
     * @return
     */
    @LogMessage(content = "查询货币对管理详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BtCurrencyPairVO vo = btCurrencyPairService.getByUnion(id);
        model.addAttribute("btCurrencyPairPage", vo);
        return "basic/business/viewBtCurrencyPair";
    }
    /**
     * 添加货币对管理
     *
     * @param btCurrencyPair
     * @return
     */
    @LogMessage(content = "添加货币对管理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BtCurrencyPair btCurrencyPair) {
        btCurrencyPairService.checkData(btCurrencyPair);
        btCurrencyPairService.saveBtCurrencyPair(btCurrencyPair, getUserLoginName());
        return success();
    }
    /**
     * 修改货币对管理
     *
     * @param btCurrencyPair
     * @return
     */
    @LogMessage(content = "修改货币对管理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BtCurrencyPair btCurrencyPair) {
        BtCurrencyPair t = btCurrencyPairService.get(btCurrencyPair.getId());
        //数据校验
        btCurrencyPairService.checkEqualData(t, btCurrencyPair);
        BeanUtils.copyNotNullProperties(btCurrencyPair, t);
        btCurrencyPairService.updateBtCurrencyPair(t, getUserLoginName());
        return success();
    }
    /**
     * 删除货币对管理
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除货币对管理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BtCurrencyPair btCurrencyPair = btCurrencyPairService.get(id);
        btCurrencyPairService.deleteEntity(btCurrencyPair);
        return success();
    }
    /**
     * 货币对配对
     *
     * @param curId1
     * @param curId2
     * @return
     */
    @PostMapping("/getByCurId")
    @ResponseBody
    public AjaxJson getByCurId(String curId1, String curId2) {
        Map<String, Object> params = new HashMap<>();
        params.put("curId1", curId1);
        params.put("curId2", curId2);
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setObj(btCurrencyPairService.getByCurId(params));
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }


    /**
     * 获取有效的货币对
     * @return
     */
    @GetMapping("/getValidCurrencyPair")
    @ResponseBody
    public List<BtCurrencyPairVO> getValidCurrencyPair() {
        return btCurrencyPairService.getValidCurrencyPair();
    }


    /**
     *
     * 查询所有有效货币对
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<BtCurrencyPairVO> getCurrencyPairjson() {
        return btCurrencyPairService.getCurrencyPairjson();
    }

    /**
     * 通过买入币种和卖出币种查询货币对
     * @param curId1
     * @param curId2
     * @return
     */
    @PostMapping("/getBtCurrencyPairByCurId/{curId1}/{curId2}")
    @ResponseBody
    public AjaxJson getBtCurrencyPairByCurId(@PathVariable("curId1") String curId1, @PathVariable("curId2") String curId2) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setObj(btCurrencyPairService.getBtCurrencyPairByCurId(curId1,curId2));
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }
}
