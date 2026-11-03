package com.bytter.system.web.controller;
import com.bytter.basic.entity.BtCurrency;
import com.bytter.basic.entity.BtCurrencyInit;
import com.bytter.basic.service.BtCurrencyInitService;
import com.bytter.basic.service.BtCurrencyService;
import com.bytter.basic.vo.BtCurrencyVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 货币资料维护
 * @date 2017-05-23 11:14:22
 */
@Controller
@RequestMapping("/basic/business/btcurrency")
public class BtCurrencyController extends BaseController {
    @Autowired
    private BtCurrencyService btCurrencyService;
    @Autowired
    private BtCurrencyInitService btCurrencyInitService;
    @Autowired
    private Ii18nService i18nServiceI;
    /**
     * 货币资料维护列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询货币资料维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "basic/business/listBtCurrency";
    }
    /**
     * 货币资料维护 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btCurrencyService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 货币资料维护新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(BtCurrency btCurrency, Model model) {
        if (StringUtils.isNotEmpty(btCurrency.getId())) {
            btCurrency = btCurrencyService.get(btCurrency.getId());
            model.addAttribute("btCurrencyPage", btCurrency);
        }
        return "basic/business/addBtCurrency";
    }
    /**
     * 货币资料维护编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(BtCurrency btCurrency, Model model) {
        if (StringUtils.isNotEmpty(btCurrency.getId())) {
            BtCurrencyVO vo = this.btCurrencyService.getByUnion(btCurrency.getId());
            model.addAttribute("btCurrencyPage", vo);
        }
        return "basic/business/updateBtCurrency";
    }
    /**
     * 货币资料维护详情页
     *
     * @return
     */
    @LogMessage(content = "查询货币资料维护详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BtCurrencyVO vo = btCurrencyService.getByUnion(id);
        model.addAttribute("btCurrencyPage", vo);
        return "basic/business/viewBtCurrency";
    }
    /**
     * 添加货币资料维护
     *
     * @param btCurrency
     * @return
     */
    @LogMessage(content = "添加货币资料维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BtCurrency btCurrency) {
        btCurrencyService.saveBtCurrency(btCurrency, getUserLoginName());
        return success();
    }
    /**
     * 修改货币资料维护
     *
     * @param btCurrency
     * @return
     */
    @LogMessage(content = "修改货币资料维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BtCurrency btCurrency) {
        //判断判断币别编码是否重复
        boolean result = btCurrencyService.isRepeatCurCode(btCurrency.getId(), btCurrency.getCurCode());
        if (result) {
            throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_MESSAGE_CURCODE_UNIQUE);
        }
        BtCurrency t = btCurrencyService.get(btCurrency.getId());
        //判断关联业务，如已经关联外汇的相关表，无法修改币种
        if (btCurrency.getEnglishCode().equals(t.getEnglishCode()) && btCurrency.getPbScaleCode().equals(t.getPbScaleCode())) {
        } else {
            if (btCurrencyService.checkAssociated(btCurrency.getId())) {
                throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_ASSOCIATED_FOREIGN);
            }
        }
        BeanUtils.copyNotNullProperties(btCurrency, t);
        btCurrencyService.updateBtCurrency(t, getUserLoginName());
        return success();
    }
    /**
     * 删除货币资料维护
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除货币资料维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BtCurrency btCurrency = btCurrencyService.get(id);
        btCurrencyService.deleteEntity(btCurrency);
        return success();
    }
    /**
     * 获取所有有效货币信息
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<BtCurrencyDTO> json() {
        List<BtCurrencyVO> btCurrency = btCurrencyService.getBtCurrency();
        return getI18nCurrency(btCurrency);
    }
    private List<BtCurrencyDTO> getI18nCurrency(List<BtCurrencyVO> btCurrencies) {
        List<BtCurrencyDTO> list = BeanUtils.copyListProperties(btCurrencies, BtCurrencyDTO.class);
        for (BtCurrencyDTO btCurrencyDTO : list) {
            String businessBaseContext = i18nServiceI.getBusinessBaseContext(btCurrencyDTO.getId());
            if (StringUtils.isNotBlank(businessBaseContext)) {
                btCurrencyDTO.setCurName(businessBaseContext);
            }
        }
        return list;
    }
    /**
     * <p>
     * （数据）获取国家集合的json数据
     * </p>
     *
     * @return List
     */
    @PostMapping("/listSysCur")
    @ResponseBody
    public List<BtCurrencyInit> listSysCur() {
        return btCurrencyInitService.listAll();
    }


    /**
     * 获取有效业务币种集合
     *
     * @return List
     */
    @PostMapping("/listCur")
    @ResponseBody
    public List<BtCurrencyVO> listCur() {
        List<BtCurrencyVO> btCurrencys = btCurrencyService.getBtCurrency();
        return btCurrencys;
    }
}
