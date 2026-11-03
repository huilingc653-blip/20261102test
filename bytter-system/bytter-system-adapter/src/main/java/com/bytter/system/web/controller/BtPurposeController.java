package com.bytter.system.web.controller;
import com.bytter.basic.entity.BtPurpose;
import com.bytter.basic.service.BtPurposeService;
import com.bytter.basic.vo.BtPurposeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 接口代码维护
 * @date 2017-05-19 17:07:12
 */
@Controller
@RequestMapping("/basic/business/btpurpose")
public class BtPurposeController extends BaseController {
    @Autowired
    private BtPurposeService btPurposeService;
    /**
     * 接口代码维护列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询接口代码维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "basic/business/listBtPurpose";
    }
    /**
     * 用途维护表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btPurposeService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(BtPurpose btPurpose, Model model) {
        if (StringUtils.isNotEmpty(btPurpose.getId())) {
            btPurpose = this.btPurposeService.get(btPurpose.getId());
            model.addAttribute("btPurposePage", btPurpose);
        }
        return "basic/business/addBtPurpose";
    }
    /**
     * 编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(BtPurpose btPurpose, Model model) {
        if (StringUtils.isNotEmpty(btPurpose.getId())) {
            BtPurposeVO vo = this.btPurposeService.getByUnion(btPurpose.getId());
            model.addAttribute("btPurposePage", vo);
        }
        return "basic/business/updateBtPurpose";
    }
    /**
     * 查看详情页
     *
     * @return
     */
    @LogMessage(content = "查询接口代码维护详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BtPurposeVO btPurpose = this.btPurposeService.getByUnion(id);
        model.addAttribute("btPurposePage", btPurpose);
        return "basic/business/viewBtPurpose";
    }
    /**
     * 添加用途维护表
     *
     * @param vo
     * @return
     */
    @LogMessage(content = "添加用途维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BtPurposeVO vo) {
        btPurposeService.saveBtPurpose(vo, getUserLoginName());
        return success();
    }
    /**
     * 修改用途维护表
     *
     * @param vo
     * @return
     */
    @LogMessage(content = "修改用途维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BtPurposeVO vo) {
        btPurposeService.updateBtPurpose(vo, getUserLoginName());
        return success();
    }
    /**
     * 删除用途维护表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除用途维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BtPurpose btPurpose = btPurposeService.get(id);
        btPurposeService.deleteEntity(btPurpose);
        return success();
    }
    /**
     * 批量删除用途维护表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除用途维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BtPurpose btPurpose = btPurposeService.get(id);
            btPurposeService.deleteEntity(btPurpose);
        }
        return success();
    }
    /**
     * 获取单位用途
     *
     * @param corpId
     * @param sysBusType
     * @return
     */
    @RequestMapping(params = "getPurpose")
    @ResponseBody
    public AjaxJson getPurpose(String corpId, String sysBusType) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(btPurposeService.listBy(corpId, sysBusType));
        return j;
    }
    /**
     * 获取单位用途
     *
     * @param corpId
     * @param sysBusType
     * @return
     */
    @RequestMapping(params = "getByList")
    @ResponseBody
    public List<BtPurposeVO> getByList(String corpId, String sysBusType) {
        return btPurposeService.listBy(corpId, sysBusType);
    }
    /**
     * 获取单位用途
     *
     * @param corpId
     * @param sysBusType
     * @return
     */
    @RequestMapping(params = "getByListSort")
    @ResponseBody
    public List<BtPurposeVO> getByListSort(String corpId, String sysBusType, String busDtl) {
        //return btPurposeService.listBySort(corpId, sysBusType, busDtl);
        return btPurposeService.listBy(corpId, sysBusType);
    }
    @GetMapping("/corpId.json/{id}")
    @ResponseBody
    public Object getCorpIdByPurposeId(@PathVariable("id") String id) {
        return btPurposeService.getCorpIdByPurposeId(id);
    }
    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<BtPurposeVO> list) {
        this.btPurposeService.saveImport(list, this.getCurrentUser());
        return success();
    }
    /**
     * 获取单位用途(外汇)
     *
     * @param corpId
     * @param sysBusType
     * @return
     */
    @RequestMapping("/getByListForFex")
    @ResponseBody
    public List<BtPurposeVO> getByListForFex(String corpId, String sysBusType, String curId, String countryId, String bankTypeId) {
        return btPurposeService.listBy(corpId, sysBusType, curId, countryId, bankTypeId);
    }
}
