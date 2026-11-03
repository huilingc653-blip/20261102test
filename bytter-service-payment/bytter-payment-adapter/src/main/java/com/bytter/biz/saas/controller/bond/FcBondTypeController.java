package com.bytter.biz.saas.controller.bond;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTableOutPutOrderUtils;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IFcBondTypeService;
import com.bytter.guarantee.vo.FcBondTypeVO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Title: Controller
 * @Description: 保证金类型维护列表
 */
@Api(value = "/v1-payment/bond/fcbondtype", tags = "保证金类型维护")
@RestController("v1FcBondTypeController")
@RequestMapping("/v1-payment/bond/fcbondtype")
public class FcBondTypeController extends BaseController {
    @DubboReference(check = false)
    private IFcBondTypeService fcBondTypeService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private IFcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;


    /**
     * 保证金类型维护列表 table列表
     */
    @ApiOperation("保证金类型维护列表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<FcBondTypeVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<FcBondTypeVO> fcBondTypeVODataTablesOutput = fcBondTypeService.bondTypeList(input);
        return AjaxJson.success(fcBondTypeVODataTablesOutput);
    }


    /**
     * 保证金类型维护 编辑页面跳转
     */
    @ApiOperation("保证金类型维护 编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<FcBondTypeVO> update(@PathVariable String id) {
        return AjaxJson.success(fcBondTypeService.getBondType(id));
    }

    /**
     * 保证金类型维护详情页
     */
    @ApiOperation("保证金类型维护详情页")
    @GetMapping("/view/{id}")
    public AjaxJson<FcBondTypeVO> view(@PathVariable("id") String id, Model model) {
        return AjaxJson.success(fcBondTypeService.getBondType(id));
    }

    /**
     * 保存保证金类型
     */
    @ApiOperation("保存保证金类型")
    @LogMessage(content = "保存保证金类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody FcBondTypeVO fcBondType) {
        fcBondTypeService.save(fcBondType, getUserId());
        return success();
    }

    /**
     * 修改保证金类型
     */
    @ApiOperation("修改保证金类型")
    @LogMessage(content = "修改保证金类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody FcBondTypeVO fcBondType) {
        fcBondTypeService.saveOrUpdate(fcBondType, getUserId());
        return success();
    }

    /**
     * 删除保证金类型
     */
    @ApiOperation("保证金类型维护列表 table列表")
    @LogMessage(content = "删除保证金类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        FcBondTypeVO fcBondTypeVO = new FcBondTypeVO();
        fcBondTypeVO.setId(id);
        fcBondTypeService.deleteEntity(fcBondTypeVO);
        return success();
    }

    /***
     * 获取有效保证金类型
     * @return
     */
    @ApiOperation("获取有效保证金类型")
    @GetMapping("/getBondType")
    public AjaxJson<List<FcBondTypeVO>> json() {
        return AjaxJson.success(fcBondTypeService.getAllBondType());
    }

    /**
     * 获取转出账号：活期、票据活期两类
     */
    @ApiOperation("获取转出账号：活期、票据活期两类")
    @PostMapping("/getOutBankAcc")
    public AjaxJson<List<BtBankAccDTO>> getOutBankAcc(String corpId, String curId) {
        List<BtBankAccDTO> bankAccs = new ArrayList<BtBankAccDTO>();
        if (StringUtils.isNotEmpty(corpId)) {
            bankAccs = iBtBankAccService.getBankAccByUserIdCurId(getUserId(), corpId, curId, "01,10");
            List<String> countAccS = new ArrayList<>();
            for (BtBankAccDTO btBankAccDTO : bankAccs) {
                countAccS.add(btBankAccDTO.getCounterAccId());

            }
            List<FcOnlineLedgerDTO> fcOnlineLedgerDTOList = fcOnlineLedgerService.getFcOnlineLedgerByAccList(countAccS);
            Map<String, FcOnlineLedgerDTO> fcOnlineLedgerDTOMap = new HashMap<>();
            for (FcOnlineLedgerDTO fcOnlineLedgerDTO : fcOnlineLedgerDTOList) {
                fcOnlineLedgerDTOMap.put(fcOnlineLedgerDTO.getAccount(), fcOnlineLedgerDTO);
            }

            for (BtBankAccDTO btBankAccDTO : bankAccs) {//fcBalance fc.check_Balance - fc.freeze_Money
                FcOnlineLedgerDTO fcOnlineLedgerDTO = fcOnlineLedgerDTOMap.get(btBankAccDTO.getCounterAccId());
                if (null != fcOnlineLedgerDTO) {
                    btBankAccDTO.setFcBalance(fcOnlineLedgerDTO.getCheckBalance().subtract(fcOnlineLedgerDTO.getFreezeMoney()));
                } else {
                    btBankAccDTO.setFcBalance(BigDecimal.ZERO);
                }
                BisAccBalDTO bisAccBalDTO = bisAccBalService.getLastBisAccBalBy(btBankAccDTO.getBankAcc(), btBankAccDTO.getCurId());
                if (null != bisAccBalDTO) {
                    btBankAccDTO.setBalance(bisAccBalDTO.getAvailBal());
                } else {
                    btBankAccDTO.setBalance(BigDecimal.ZERO);
                }

            }

            setDictName(bankAccs);

        }
        return AjaxJson.success(bankAccs);
    }

    private void setDictName(List<BtBankAccDTO> bankAccs) {
        for (BtBankAccDTO btBankAccDTO : bankAccs) {
            if(StringUtils.isNotEmpty(btBankAccDTO.getIsOnline())){
                String isOnline = btBankAccDTO.getIsOnline();
                btBankAccDTO.setIsOnlineName("1".equals(isOnline) ? "是" : "0".equals(isOnline) ? "否" : null);
            }
        }
    }
}
