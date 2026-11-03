package com.bytter.system.saas.controller.bankacc;

import com.bytter.basic.entity.BtInputBankInfo;
import com.bytter.basic.service.BtInputBankInfoService;
import com.bytter.basic.vo.BtInputBankInfoPageVO;
import com.bytter.basic.vo.BtInputBankInfoVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.service.ExcelCacheService;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.enums.BankInfoMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行行号维护
 * @date 2017-05-23 19:06:25
 */
@Api(value = "银行行号维护",tags = "银行行号维护")
@RestController("v1BtInputBankInfoController")
@RequestMapping("/v1-system/basic/business/btinputbankinfo")
public class BtInputBankInfoController extends BaseController {
    @Autowired
    private BtInputBankInfoService btInputBankInfoService;
    @Autowired
    private ExcelCacheService excelCacheService;
    /**
     * 银行行号维护 table列表
     *
     * @return
     */
    @ApiOperation("银行行号维护 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btInputBankInfoService.getDataTables(input));
    }
    /**
     * 银行行号维护 table列表(需要有前缀的前提)
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("银行行号维护 table列表(需要有前缀的前提)")
    @PostMapping("/datatablePrefix")
    @ResponseBody
    public AjaxJson<DataTablesOutput> datatablePrefix(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btInputBankInfoService.getDataTablesPrefix(input));
    }

    @ApiOperation("修改 查看 根据ID获取数据")
    @GetMapping("/getById")
    @ResponseBody
    public AjaxJson<BtInputBankInfoVO> getById(String id){
        BtInputBankInfoPageVO pageVO = new BtInputBankInfoPageVO();
        pageVO.setBtInputBankInfoPage(btInputBankInfoService.getByUnion(id));
        return success(pageVO);
    }

    /**
     * 添加银行行号维护
     *
     * @param btInputBankInfo
     * @return
     */
    @ApiOperation("添加银行行号维护")
    @LogMessage(content = "添加银行行号维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody BtInputBankInfo btInputBankInfo) {
        if (!StringUtils.isNumeric(btInputBankInfo.getBankCode()) || btInputBankInfo.getBankCode().length() < 12) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKCODE_WRONG,btInputBankInfo.getBankCode(),"");
        }
        btInputBankInfoService.prefixCityExist(btInputBankInfo.getBankCode());
        String bankPrefix = StringUtils.substring(StringUtils.trim(btInputBankInfo.getBankCode()), 0, 3);
        String areaCode = StringUtils.substring(StringUtils.trim(btInputBankInfo.getBankCode()), 3, 7);
        btInputBankInfo.setBankPrefix(bankPrefix);
        btInputBankInfo.setAreaCode(areaCode);
        btInputBankInfo.setCreateBy(this.getCurrentUser().getLoginName());
        btInputBankInfo.setCreateDate(DateUtils.getDate());
        btInputBankInfoService.chechSave(btInputBankInfo);
        return success();
    }
    /**
     * 修改银行行号维护
     *
     * @param btInputBankInfo
     * @return
     */
    @ApiOperation("修改银行行号维护")
    @LogMessage(content = "修改银行行号维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody BtInputBankInfo btInputBankInfo) {
        if (btInputBankInfoService.getByBankInfo(btInputBankInfo.getBankCode()) != null &&
                !(btInputBankInfoService.getSysBankInfoId(btInputBankInfo.getBankCode()).equals(btInputBankInfo.getId()))) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKCODE_UNIQUE);
        }
        if (!StringUtils.isNumeric(btInputBankInfo.getBankCode()) || btInputBankInfo.getBankCode().length() < 12) {
            throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKCODE_WRONG);
        }
        BtInputBankInfo t = btInputBankInfoService.get(btInputBankInfo.getId());
        String bankPrefix = StringUtils.substring(StringUtils.trim(btInputBankInfo.getBankCode()), 0, 3);
        String areaCode = StringUtils.substring(StringUtils.trim(btInputBankInfo.getBankCode()), 3, 7);
        btInputBankInfo.setBankPrefix(bankPrefix);
        btInputBankInfo.setAreaCode(areaCode);
        btInputBankInfo.setUpdateBy(this.getCurrentUser().getLoginName());
        btInputBankInfo.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyNotNullProperties(btInputBankInfo, t);
        btInputBankInfoService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除银行行号维护
     *
     * @param id
     * @return
     */
    @ApiOperation("删除银行行号维护")
    @LogMessage(content = "删除银行行号维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        BtInputBankInfo btInputBankInfo = btInputBankInfoService.get(id);
        btInputBankInfoService.deleteEntity(btInputBankInfo);
        return success();
    }
    /**
     * 批量删除银行行号维护
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除银行行号维护")
    @LogMessage(content = "批量删除银行行号维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BtInputBankInfo btInputBankInfo = btInputBankInfoService.get(id);
            btInputBankInfoService.deleteEntity(btInputBankInfo);
        }
        return success();
    }
    /**
     * 前台调用获取银行行号信息
     *
     * @param bankCode
     * @return
     */
    @ApiOperation("前台调用获取银行行号信息")
    @GetMapping(path = "/getBankInfoJson")
    @ResponseBody
    public AjaxJson getBankInfoJson(String bankCode) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        if (StringUtils.isEmpty(bankCode)) {
            j.setObj(btInputBankInfoService.listAll());
        } else {
            j.setObj(btInputBankInfoService.getBtInputBankInfo(bankCode));
        }
        return j;
    }
    @GetMapping("/json")
    @ResponseBody
    public List<BtInputBankInfo> json() {
        return btInputBankInfoService.listAll();
    }
    @GetMapping("/bankInfoJson")
    @ResponseBody
    public List<BtInputBankInfo> getBtInputBankInfo(String bankCode) {
        return btInputBankInfoService.getBtInputBankInfo(bankCode);
    }
    /**
     * 保存导入数据
     *
     * @param
     * @return
     */
    @ApiOperation("保存导入数据")
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestParam("cacheCode") String cacheCode) {
        List<BtInputBankInfoVO> list = excelCacheService.getListExcelDataCache(cacheCode, BtInputBankInfoVO.class);
        this.btInputBankInfoService.saveImport(list, this.getCurrentUser());
        return success();
    }
    /**
     * 导出全部
     *
     * @return
     */
    @ApiOperation("导出全部")
    @PostMapping("/exportAll")
    public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                DataTablesOutput<?> dataTablesOutput = btInputBankInfoService.getDataTablesPrefix(dataTablesInput);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "银行行号信息维护",5000, BtInputBankInfoVO.class);
    }

    /**
     * 银行行号维护 table列表(需要有前缀的前提)(选择组件，无需权限控制)
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("银行行号维护 table列表(需要有前缀的前提)(选择组件，无需权限控制)")
    @PostMapping("/datatablePrefixSelect")
    @ResponseBody
    public AjaxJson<DataTablesOutput> datatablePrefixSelect(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(btInputBankInfoService.getDataTablesPrefix(input));
    }
}
