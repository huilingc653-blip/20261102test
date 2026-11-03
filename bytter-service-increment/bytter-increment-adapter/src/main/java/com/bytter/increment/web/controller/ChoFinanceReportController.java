package com.bytter.increment.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.dto.BiRemainTermDTO;
import com.bytter.increment.api.service.IBiRemainTermService;
import com.bytter.increment.domain.service.ChoFinanceReportService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: Controller
 * @Description: 即将到期存款分析
 * @author wangzx
 * @date 2020-01-14 14:03:30
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/report/chofinancereport")
public class ChoFinanceReportController extends BaseController {
    @Autowired
    private ChoFinanceReportService choFinanceReportService;

    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    
    @Autowired
    private IBiRemainTermService biRemainTermService;

    
    @GetMapping("/list")
    public String corplist(Model model){
        List<BiRemainTermDTO> biRemainTermDTOs = biRemainTermService.getBiRemainTermDTOs(null);
        model.addAttribute("terms", biRemainTermDTOs);
        //本位币转换
        BtCurrencyDTO natualCurrency = btCurrencyService.getNatualCurrency();
        if(EmptyUtils.isEmpty(natualCurrency)){
            model.addAttribute("curId","");
        }else{
            model.addAttribute("curId",natualCurrency.getId());
        }
        return "chowmatistic/listChoFinanceReport";
    }
    /**
     * 银行理财表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        DataTablesOutput out = choFinanceReportService.getDataTablesUnion(dataTablesInput, this.getCurrentUser(),null,null);
        return out;
    }
    /**
     * 即将到期存款分析图表
     * @param curId
     * @param corpId
     * @param opType
     * @param moneyUnit
     * @param isToCurr
     * @return
     */
    @PostMapping("/getData")
    @ResponseBody
    public Object getData(String curId, String corpId, String opType , String moneyUnit, String isToCurr,String reducedCurId){
        DataTablesInput input = new DataTablesInput();
        if(StringUtils.isNotEmpty(curId)){
            input.addFilter(MatchTypeEnum.EQ,"curId",curId);
        }
        if(StringUtils.isNotEmpty(corpId)){
            input.addFilter(MatchTypeEnum.IN,"corpId",corpId);
        }
        if(StringUtils.isNotEmpty(opType)){
            input.addFilter(MatchTypeEnum.EQ,"opType",opType);
        }
        if(StringUtils.isNotEmpty(reducedCurId)){
            input.getFormdata().put("reducedCurId",reducedCurId);
        }
        DataTablesOutput out = choFinanceReportService.getDataTablesUnion(input, this.getCurrentUser(),moneyUnit,isToCurr);
        BtCurrencyDTO curDto = btCurrencyService.getObjectById(curId);
        BtCurrencyDTO dto = btCurrencyService.getObjectById(reducedCurId);
        String curName = "";
        if(!EmptyUtils.isEmpty(curDto)){
            if(!EmptyUtils.isEmpty(curDto.getCurName())){
                curName = curDto.getCurName();
            }
        }
        if(!EmptyUtils.isEmpty(dto)){
            if(!EmptyUtils.isEmpty(dto.getCurName())){
                curName = dto.getCurName();
            }
        }
        return choFinanceReportService.getData(out.getData(),curName,moneyUnit);
    }
}
