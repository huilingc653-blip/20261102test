package com.bytter.report.web.synthesize.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.report.domain.synthesize.constants.MoneyMapConstant;
import com.bytter.report.domain.synthesize.service.MoneyMapService;
import com.bytter.report.domain.synthesize.vo.MoneyMapQueryVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author zuoy
 * @since 2020-01-16
 */
@Controller
@RequestMapping("/bi/synthesize/moneyMap")
public class MoneyMapController extends BaseController {
    @Autowired
    private MoneyMapService moneyMapService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    /**
     * 展示资金地图报表
     *
     * @param model
     * @return
     */
        @GetMapping("/list")
    public String list(Model model) {
        return "synthesize/listMoneyMap";
    }
    /**
     * 查询
     *
     * @param moneyMapQueryVO 查询条件
     * @param model
     * @return
     */
    @PostMapping("/query")
    public String query(MoneyMapQueryVO moneyMapQueryVO, Model model) {
        model.addAttribute("moneyMapQueryVO", moneyMapQueryVO);
        if (StringUtils.isNotBlank(moneyMapQueryVO.getReducedCurId())) {
            BtCurrencyDTO objectById = currencyService.getObjectById(moneyMapQueryVO.getReducedCurId());
            model.addAttribute("reduceCurName", objectById.getCurName());
        }
        if (MoneyMapConstant.SHOW_TYPE_MAP.equalsIgnoreCase(moneyMapQueryVO.getShowType())) {
            // 按地图展示
            return "synthesize/listMoneyMapEchartMap";
        } else if (MoneyMapConstant.SHOW_TYPE_TABLE.equalsIgnoreCase(moneyMapQueryVO.getShowType())) {
            // 按表格展示
            if (MoneyMapConstant.STATISTICAL_DIMENSION_CORP.equalsIgnoreCase(moneyMapQueryVO.getStatisticalDimension())) {
                // 按单位统计
                return "synthesize/listMoneyMapCorp";
            } else if (MoneyMapConstant.STATISTICAL_DIMENSION_BANK.equalsIgnoreCase(moneyMapQueryVO.getStatisticalDimension())) {
                // 按银行统计
                return "synthesize/listMoneyMapBank";
            }
        }
        return "";
    }
    /**
     * 查询
     *
     * @param moneyMapQueryVO 查询条件
     * @param model
     * @return
     */
    @GetMapping("/queryByMap")
    public String queryByMap(MoneyMapQueryVO moneyMapQueryVO, Model model) {
        return "synthesize/listMoneyMapEchartMap";
    }
    @SuppressWarnings("rawtypes")
        @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return this.moneyMapService.getDataTablesUnion(dataTablesInput, getCurrentUser());
    }
    /**
     * 首页加载世界地图
     *
     * @param model
     * @return
     */
    @PostMapping("/getWorldMapData")
    @ResponseBody
    public Object getWorldMapData(MoneyMapQueryVO moneyMapQueryVO, Model model) {
        return moneyMapService.getWorldMapData(moneyMapQueryVO, getCurrentUser());
    }
    /**
     * 首页加载大区国家地图
     *
     * @param model
     * @return
     */
    @PostMapping("/getContinentCountryMapData")
    @ResponseBody
    public Object getContinentCountryMapData(MoneyMapQueryVO moneyMapQueryVO, Model model) {
        return moneyMapService.getContinentCountryMapData(moneyMapQueryVO, getCurrentUser());
    }
    /**
     * 首页加载世界地图
     *
     * @param model
     * @return
     */
    @PostMapping("/getChinaMapData")
    @ResponseBody
    public Object getChinaMapData(MoneyMapQueryVO moneyMapQueryVO, Model model) {
        return moneyMapService.getChinaMapData(moneyMapQueryVO, getCurrentUser());
    }
    /**
     * 首页加载世界地图
     *
     * @param model
     * @return
     */
    @PostMapping("/getProvinceMapData")
    @ResponseBody
    public Object getProvinceMapData(MoneyMapQueryVO moneyMapQueryVO, Model model) {
        return moneyMapService.getProvinceMapData(moneyMapQueryVO, getCurrentUser());
    }
}
