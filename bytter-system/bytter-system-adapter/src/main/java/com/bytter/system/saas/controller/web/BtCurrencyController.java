package com.bytter.system.saas.controller.web;

import com.bytter.basic.entity.BtCurrency;
import com.bytter.basic.entity.BtCurrencyInit;
import com.bytter.basic.service.BtCurrencyInitService;
import com.bytter.basic.service.BtCurrencyService;
import com.bytter.basic.vo.BtCurrencyVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 货币资料维护
 * @date 2017-05-23 11:14:22
 */
@Api(value = "/v1-system/basic/business/btcurrency", tags = "货币资料维护")
@RestController("v1BtCurrencyController")
@RequestMapping("/v1-system/basic/business/btcurrency")
public class BtCurrencyController extends BaseController {
    @Autowired
    private BtCurrencyService btCurrencyService;
    @Autowired
    private BtCurrencyInitService btCurrencyInitService;
    @Autowired
    private Ii18nService i18nServiceI;

    /**
     * 货币资料维护 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(btCurrencyService.getDataTablesUnion(input));
    }

    /**
     * 货币资料维护编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public AjaxJson<BtCurrencyVO> update(String id) {
        if (StringUtils.isNotEmpty(id)) {
            BtCurrencyVO vo = this.btCurrencyService.getByUnion(id);
            return AjaxJson.success(vo);
        }
        return AjaxJson.success();
    }

    /**
     * 货币资料维护详情页
     *
     * @return
     */
    @LogMessage(content = "查询货币资料维护详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<BtCurrencyVO> view(@PathVariable("id") String id) {
        BtCurrencyVO vo = btCurrencyService.getByUnion(id);
        return AjaxJson.success(vo);
    }

    /**
     * 添加货币资料维护
     *
     * @param btCurrency
     * @return
     */
    @LogMessage(content = "添加货币资料维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody BtCurrency btCurrency) {
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
    public AjaxJson modify(@RequestBody BtCurrency btCurrency) {
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
    public AjaxJson<List<BtCurrencyDTO>> json() {
        List<BtCurrencyVO> btCurrency = btCurrencyService.getBtCurrency();
        return AjaxJson.success(getI18nCurrency(btCurrency));
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
    public AjaxJson<List<BtCurrencyInit>> listSysCur() {
        return AjaxJson.success(btCurrencyInitService.listAll());
    }
}
