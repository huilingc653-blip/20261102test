package com.bytter.system.web.controller;
import com.bytter.basic.entity.BisBifInit;
import com.bytter.basic.service.BisBifInitService;
import com.bytter.basic.vo.BisBifInitVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.enums.BankInfoMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 接口代码维护
 * @date 2017-05-19 17:07:12
 */
@Controller
@RequestMapping("/basic/business/bisbifinit")
public class BisBifInitController extends BaseController {
    @Autowired
    private BisBifInitService bisBifInitService;
    @Autowired
    private Ii18nService i18nServiceI;
    /**
     * 接口代码维护列表 页面跳转
     *
     * @param model
     * @return
     */
    @LogMessage(content = "查询接口代码维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "basic/business/listBisBifInit";
    }
    /**
     * 接口代码维护 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput = bisBifInitService.getDataTablesUnion(dataTablesInput);
        return dataTablesOutput;
    }
    /**
     * 接口代码维护新增页面跳转
     *
     * @param bisBifInit
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String add(BisBifInit bisBifInit, Model model) {
        if (StringUtils.isNotEmpty(bisBifInit.getId())) {
            bisBifInit = bisBifInitService.get(bisBifInit.getId());
            model.addAttribute("bisBifInitPage", bisBifInit);
        }
        return "basic/business/addBisBifInit";
    }
    /**
     * 接口代码维护编辑页面跳转
     *
     * @param bisBifInit
     * @param model
     * @return
     */
    @GetMapping("/update")
    public String update(BisBifInit bisBifInit, Model model) {
        if (StringUtils.isNotEmpty(bisBifInit.getId())) {
            bisBifInit = bisBifInitService.get(bisBifInit.getId());
            model.addAttribute("bisBifInitPage", bisBifInit);
        }
        return "basic/business/updateBisBifInit";
    }
    /**
     * 接口代码维护详情页
     *
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "查询接口代码维护详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BisBifInit bisBifInit = bisBifInitService.get(id);
        model.addAttribute("bisBifInitPage", bisBifInit);
        return "basic/business/viewBisBifInit";
    }
    /**
     * 添加接口代码维护
     *
     * @param bisBifInit
     * @return
     */
    @LogMessage(content = "添加接口代码维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BisBifInit bisBifInit) {
        if (bisBifInitService.isExistMulByBifCode(bisBifInit.getBifCode(), CommonConstant.SUBMIT_ADD, bisBifInit.getId())) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BIFCODE_REQUIRED_UNIQUE);
        }
        // 创建人信息
        if (EmptyUtils.isBlank(bisBifInit.getCreateBy())) {
            bisBifInit.setCreateBy(this.getCurrentUser().getLoginName());
            bisBifInit.setCreateDate(DateUtils.getDate());
        }
        bisBifInitService.save(bisBifInit);
        return success();
    }
    /**
     * 修改接口代码维护
     *
     * @param bisBifInit
     * @return
     */
    @LogMessage(content = "修改接口代码维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BisBifInit bisBifInit) {
        if (bisBifInitService.isExistMulByBifCode(bisBifInit.getBifCode(), CommonConstant.SUBMIT_MODIFY, bisBifInit.getId())) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BIFCODE_REQUIRED_UNIQUE);
        }
        BisBifInit t = bisBifInitService.get(bisBifInit.getId());
        BeanUtils.copyNotNullProperties(bisBifInit, t);
        // 修改人信息
        t.setUpdateBy(this.getCurrentUser().getLoginName());
        t.setUpdateDate(DateUtils.getDate());
        bisBifInitService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除接口代码维护
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除接口代码维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BisBifInit bisBifInit = bisBifInitService.get(id);
        bisBifInitService.deleteEntity(bisBifInit);
        return success();
    }
    /**
     * 批量删除接口代码维护
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除接口代码维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisBifInit bisBifInit = bisBifInitService.get(id);
            bisBifInitService.deleteEntity(bisBifInit);
        }
        return success();
    }
    /**
     * 银行类型维护列表
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<BisBifInit> getListByValidSign() {
        List<BisBifInit> bifInitList = bisBifInitService.getListByValidSign("1");
        // 根据 bifCode 去重
        bifInitList = bifInitList.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(BisBifInit::getBifCode))),
                        ArrayList::new
                ));
        for (BisBifInit bifInit : bifInitList) {
            String baseContext = i18nServiceI.getBusinessBaseContext(bifInit.getId());
            if (StringUtils.isNotBlank(baseContext)) {
                bifInit.setName(baseContext);
            }
        }
        return bifInitList;
    }
    /**
     * 银行类型维护列表
     *
     * @return
     */
    @GetMapping("/jsonFex")
    @ResponseBody
    public List<BisBifInit> jsonForFex() {
        List<BisBifInit> bifInitList = bisBifInitService.getListByValidSign("1");
        for (BisBifInit bifInit : bifInitList) {
            String baseContext = i18nServiceI.getBusinessBaseContext(bifInit.getId());
            if (StringUtils.isNotBlank(baseContext)) {
                bifInit.setName(baseContext);
            }
        }
        List<BisBifInit> list = new ArrayList<>();
        for (BisBifInit data : bifInitList) {
            if (CommonConstant.YES.equals(data.getIsSupportBankForeign())) {
                list.add(data);
            }
        }
        return list;
    }
    /**
     * 银行类型维护列表
     *
     * @return
     */
    @GetMapping("/json/{isDomesticBank}")
    @ResponseBody
    public List<BisBifInit> getListByValidSign(@PathVariable String isDomesticBank) {
        List<BisBifInit> bifInitList = bisBifInitService.getListByValidSign("1");
        for (BisBifInit bifInit : bifInitList) {
            String baseContext = i18nServiceI.getBusinessBaseContext(bifInit.getId());
            if (StringUtils.isNotBlank(baseContext)) {
                bifInit.setName(baseContext);
            }
        }
        //过滤境内外
        List<BisBifInit> bifInitListFilter = new ArrayList<>();
        for (BisBifInit bisBifInit : bifInitList) {
            if (isDomesticBank.equals(bisBifInit.getIsDomesticBank())) {
                bifInitListFilter.add(bisBifInit);
            }
        }
        return bifInitListFilter;
    }
    /**
     * 银行类型维护列表
     *
     * @return
     */
    @GetMapping("/getByBankType/{bankType}")
    @ResponseBody
    public List<BisBifInitVO> getByBankType(@PathVariable String bankType) {
        List<BisBifInit> bifInitList = bisBifInitService.getListByValidSign("1");
        //过滤境内外
        List<BisBifInitVO> bifInitListFilter = new ArrayList<>();
        for (BisBifInit bifInit : bifInitList) {
            if (bankType.equals(bifInit.getBankTypeId())) {
                String baseContext = i18nServiceI.getBusinessBaseContext(bifInit.getId());
                if (StringUtils.isNotBlank(baseContext)) {
                    bifInit.setName(baseContext);
                }
                BisBifInitVO vo = new BisBifInitVO();
                BeanUtils.copyNotNullProperties(bifInit, vo);
                bifInitListFilter.add(vo);
            }
        }
        return bifInitListFilter;
    }
    /**
     * 银行类型维护列表
     *
     * @return
     */
    @GetMapping("/listFilter")
    @ResponseBody
    public List<BisBifInit> listFilter() {
        List<BisBifInit> bifInitList = bisBifInitService.listBisBifInitFilterByBankAcc();
        for (BisBifInit bifInit : bifInitList) {
            String baseContext = i18nServiceI.getBusinessBaseContext(bifInit.getId());
            if (StringUtils.isNotBlank(baseContext)) {
                bifInit.setName(baseContext);
            }
        }
        return bifInitList;
    }
    /**
     * 银行类型维护列表
     *
     * @return
     */
    @GetMapping("/getDisConnectionAlert")
    @ResponseBody
    public List<BisBifInit> getDisConnectionAlert() {
        List<BisBifInit> bifInitList = bisBifInitService.getListByValidSign("1");
        List<BisBifInit> result = new ArrayList<>();
        for (BisBifInit bifInit : bifInitList) {
            if (CommonConstant.YES.equals(bifInit.getDisConnectionAlert())) {
                String baseContext = i18nServiceI.getBusinessBaseContext(bifInit.getId());
                if (StringUtils.isNotBlank(baseContext)) {
                    bifInit.setName(baseContext);
                }
                result.add(bifInit);
            }
        }
        return result;
    }
}
