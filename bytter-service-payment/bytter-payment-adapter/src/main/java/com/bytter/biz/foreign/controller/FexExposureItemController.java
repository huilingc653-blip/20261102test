package com.bytter.biz.foreign.controller;

import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.foreign.entity.FexExposureItem;
import com.bytter.biz.foreign.service.FexExposureItemService;
import com.bytter.biz.foreign.vo.FexExposureItemVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 单位敞口设置表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fexExposureItem")
public class FexExposureItemController extends BaseController {
    @Autowired
    private FexExposureItemService fexExposureItemService;

    /**
     * 敞口科目列表 页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREITEM_QUERY)
    @LogMessage(content = "查询敞口科目列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(FexExposureItemVO fexExposureItemVO, Model model) {
        fexExposureItemVO.setShowItem(0);
        model.addAttribute("treeData",fexExposureItemService.listTreeData(fexExposureItemVO,""));
        this.initMaxLevelShow(model);
        model.addAttribute("fexExposureItemPage", fexExposureItemVO);
        return "foreign/listFexExposureItem";
    }

    private void initMaxLevelShow(Model model){
        //获取科目最大级别
        Integer maxLevel = this.fexExposureItemService.getItemMaxLevel();
        String maxLevelShow="0";
        for(int i=1; i<=maxLevel;i++){
            maxLevelShow += CommonConstant.COMMA_SPLIT+i;
        }
        maxLevelShow += CommonConstant.COMMA_SPLIT+"-1";
        model.addAttribute("maxLevelShow", maxLevelShow);
    }

    /**
     * 敞口科目 table列表
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREITEM_APPLY)
    @LogMessage(content = "查询银行账号关系策略维护表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/queryList")
    public String queryList(FexExposureItemVO fexExposureItem, Model model) throws Exception{
        model.addAttribute("treeData",fexExposureItemService.listTreeData(fexExposureItem,""));
        model.addAttribute("fexExposureItemPage",fexExposureItem);
        this.initMaxLevelShow(model);
        return "foreign/listFexExposureItem";
    }

    /**
     * 敞口科目新增页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREITEM_APPLY)
    @GetMapping("/add")
    public String add(FexExposureItem fexExposureItem, Model model) {
        if (StringUtils.isNotEmpty(fexExposureItem.getId())) {
            fexExposureItem = fexExposureItemService.get(fexExposureItem.getId());
            model.addAttribute("fexExposureItemPage", fexExposureItem);
        }
        return "foreign/addFexExposureItem";
    }

    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREITEM_APPLY)
    @GetMapping("/update")
    public String update(String id, String parentId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FexExposureItemVO fexExposureItem = fexExposureItemService.getFexExposureItemVO(id,null);
            if(StringUtils.isNotBlank(fexExposureItem.getSuperItemCode())){
                fexExposureItem.setSuperItemName(fexExposureItemService.getFexExposureItemVO(null,fexExposureItem.getSuperItemCode()).getItemName());
            }else{
                fexExposureItem.setSuperItemName("");
            }
            model.addAttribute("fexExposureItemPage", fexExposureItem);
        }
        if (StringUtils.isNotEmpty(parentId)) {
            FexExposureItemVO fexExposureItem = fexExposureItemService.getFexExposureItemVO(parentId,null);
            FexExposureItemVO newItem =new FexExposureItemVO();
            newItem.setSuperItemCode(fexExposureItem.getItemCode());
            newItem.setSuperItemName(fexExposureItem.getItemName());
            newItem.setSign(fexExposureItem.getSign());
            newItem.setIsNaturalHedge(fexExposureItem.getIsNaturalHedge());
            model.addAttribute("fexExposureItemPage", newItem);
            model.addAttribute("isSubItem", "1");
            return "foreign/addFexExposureItem";
        }
        return "foreign/updateFexExposureItem";
    }

    @GetMapping("/tree")
    @ResponseBody
    public Object tree(String type) {
        FexExposureItemVO fexExposureItem = new FexExposureItemVO();
        List<FexExposureItemVO> itemList = fexExposureItemService.listTreeData(fexExposureItem,"");
        List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
        for (FexExposureItemVO item : itemList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", item.getItemCode());
            map.put("pId", item.getSuperItemCode());
            map.put("name", "("+item.getItemCode()+")"+item.getItemName());
            map.put("code", item.getItemCode());
            tempList.add(map);
        }
        return tempList;
    }
    
    /**
     * 添加敞口科目
     * @param fexExposureItem
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREITEM_APPLY)
    @LogMessage(content = "添加敞口科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FexExposureItem fexExposureItem){
        fexExposureItemService.saveItem(fexExposureItem, this.getCurrentUser(), LocaleContextHolder.getLocale().toString());
        return success();
    }
    /**
     * 修改敞口科目
     * @param fexExposureItem
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREITEM_APPLY)
    @LogMessage(content = "修改敞口科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FexExposureItem fexExposureItem){
        FexExposureItem t = fexExposureItemService.get(fexExposureItem.getId());
        if(CommonConstant.VALID_SIGN_NO.equals(fexExposureItem.getValidSign()) && fexExposureItemService.isEdit(t.getItemCode())){
            throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_SUB_VALIDSIGN,"("+t.getItemCode()+")"+t.getItemName());
        }
        BeanUtils.copyNotNullProperties(fexExposureItem, t);
        fexExposureItemService.updateItem(t, this.getCurrentUser(),LocaleContextHolder.getLocale().toString());
        return  success();
    }
    /**
     * 删除敞口科目
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREITEM_APPLY)
    @LogMessage(content = "删除敞口科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        FexExposureItem fexExposureItem = fexExposureItemService.get(id);
        fexExposureItemService.deleteItem(fexExposureItem);
        return success();
    }

    /**
     * 校验科目是否是已使用
     * @param itemCode
     * @return
     */
    @RequestMapping("/validItemUsed")
    @ResponseBody
    public AjaxJson validItemUsed(String itemCode) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(fexExposureItemService.validItemUsed(itemCode));
        return j;
    }
}
