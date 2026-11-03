package com.bytter.ras.controller;

import com.bytter.ras.entity.RasLog;
import com.bytter.ras.service.RasLogService;
import com.bytter.ras.service.RasShopService;
import com.bytter.ras.vo.RasShopVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.ras.api.enums.RasMessageEnum;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核门店维护Controller
 * @date 2019/5/5 18:02
 */
@Controller
@RequestMapping("/ras/shop")
public class RasShopController extends BaseController {

    @Autowired
    public RasShopService rasShopService;
    @Autowired
    private RasLogService rasLogService;

    /**
     * 跳转门店维护页面
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "ras/listRasShop";
    }

    /**
     * 门店列表加载
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "查询营收稽核门店列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return rasShopService.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 门店授权门店列表加载
     * @param dataTablesInput
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_SHOPAUTH_OP)
    @LogMessage(content = "查询营收稽核门店授权门店列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/shopGrantShopDatatable")
    @ResponseBody
    public DataTablesOutput shopGrantShopDatatable(@RequestBody DataTablesInput dataTablesInput){
        return rasShopService.getDataTablesUnionShopGrant(dataTablesInput);
    }
    /**
     * 新增、编辑门户页面跳转
     */
    @GetMapping("/add")
    public String add(Model model) {
//        model.addAttribute("asDate", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        return "ras/addRasShop";
    }
    /**
     * 保存门户数据
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_SHOP_OP)
    @LogMessage(content = "添加营收稽核门店数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RasShopVO shopVO){
        //根据门户编码是否存在
        String id = rasShopService.checkShopByCorpAndCode(shopVO.getCode());
        if(StringUtils.isNotEmpty(id)){
            throw new BusinessException(RasMessageEnum.RAS_SHOP_MESSAGE_REPEAT);
        }
        //校验是否单位级别门店（一个单位只能存在一个单位级别门店）
        if("1".equals(shopVO.getIsCorp())){
            String flag = rasShopService.checkShopByIsCorp(shopVO);
            if(StringUtils.isNotEmpty(flag)) throw new BusinessException(RasMessageEnum.RAS_SHOP_ISCORP_REPEAT);
        }
        rasShopService.save(shopVO,getCurrentUser());
        return success();
    }
    /**
     * 门店编辑页面跳转
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id")String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            RasShopVO shop = rasShopService.getByIdUnion(id);
            model.addAttribute("shopPage", shop);
        }
        return "ras/updateRasShop";
    }
    /**
     * 门店编辑数据保存
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_SHOP_OP)
    @LogMessage(content = "修改营收稽核门店信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(RasShopVO shopVO, Model model) {
        if (StringUtils.isNotEmpty(shopVO.getId())) {
            //校验是否单位级别门店（一个单位只能存在一个单位级别门店）
            if("1".equals(shopVO.getIsCorp())){
                //如果flag不为空，且跟shopVO中的id不一致，该数据不允许修改
                String flag = rasShopService.checkShopByIsCorp(shopVO);
                if(StringUtils.isNotEmpty(flag)&&!flag.equals(shopVO.getId()))
                    throw new BusinessException(RasMessageEnum.RAS_SHOP_ISCORP_REPEAT);
            }
            //根据门户编码是否存在
            String id = rasShopService.checkShopByCorpAndCode(shopVO.getCode());
            //如果id跟shopVO中的id一致、或者id为空，该数据才能允许修改
            if(id.equals(shopVO.getId())||StringUtils.isEmpty(id)){
                rasShopService.update(shopVO,this.getCurrentUser());
            }else{
                throw new BusinessException(RasMessageEnum.RAS_SHOP_MESSAGE_REPEAT);
            }
        }
        return success();
    }
    /**
     * 查看门店详细信息
     */
    @LogMessage(content = "查询营收稽核门店详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id")String id, Model model) {
        RasShopVO shopVO = rasShopService.getByIdUnion(id);
        model.addAttribute("shopPage", shopVO);
        //查找该门店的启用、禁用记录
        List<RasLog> rasLogs = rasLogService.listRasLog(id);
        model.addAttribute("shopLogList", rasLogs);
        return "ras/viewRasShop";
    }
    /**
     * 批量更新状态（启用、禁用按钮）
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_SHOP_OP)
    @PostMapping("/updateStatus/{ids}/{opType}")
    @ResponseBody
    public AjaxJson updateStatus(@PathVariable("ids")String ids,@PathVariable("opType")String opType){
        rasShopService.updateStatus(ids,opType,this.getCurrentUser());
        return success();
    }
    /**
     * 导入保存数据
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_SHOP_OP)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<RasShopVO> list) {
        rasShopService.saveImport(list, this.getCurrentUser());
        return success();
    }

    /**
     * 校验该门店是否存在待稽核的数据
     */
    @PostMapping("/checkShopNotAudit/{ids}")
    @ResponseBody
    public AjaxJson checkShopNotAudit(@PathVariable("ids")String ids){
        boolean flag = rasShopService.checkShopNotAudit(ids);
        AjaxJson ajax = new AjaxJson();
        if(flag) ajax.setObj(true);
        else ajax.setObj(false);
        return ajax;
    }
}
