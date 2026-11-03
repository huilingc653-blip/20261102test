package com.bytter.finance.saas.controller.excel;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.excel.dto.ExcelImportDTO;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.RAMPageUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.enums.IResourceTemplateEnum;
import com.bytter.poi.enums.ResourceTemplateEnum;
import com.bytter.poi.handler.inter.IExcelModel;
import com.bytter.poi.service.ExcelCacheService;
import com.bytter.poi.service.ExcelImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * 导入excel生成列表
 * Created by zhouj on 2017/9/4.
 */
@Api(value = "/v1-preposebill/finance/import/excel", tags = "Excel导入")
@RestController("v1FinanceImportExcelController")
@RequestMapping({"/v1-preposebill/finance/import/excel", "/v1-finance/finance/import/excel"})
public class FinanceImportExcelController extends BaseController {

    private static final String SUFFIX_URL = "/import/excel/upload";
    private static final String UPLOAD = "upload";
    private int pageSize = 10;

    @Autowired
    private ExcelImportService excelImportService;

    @Autowired
    private ExcelCacheService excelCacheService;

    public String getKey(String code) {
        return code + this.getUserId();
    }

    /**
     * 上传模板 - 导入Excel
     */
    @LogMessage(content = "导入Excel", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/upload")
    @ApiOperation("导入Excel")
    public AjaxJson<ExcelImportDTO> upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String page = request.getParameter("page");
        String code = request.getParameter("code");

        if ("1".equals(page)) {
            // 分页导入
            String key = getKey(code);
            String ekey = key + "_error";
            ExcelImportDTO excelImportDTO = (ExcelImportDTO) excelImportService.importExcel(request, this.getCurrentUser());
            List<Object> list = excelImportDTO.getList();
            List<Object> eList = new ArrayList<>();
            if (!EmptyUtils.isEmpty(list)) {
                for (Object o : list) {
                    IExcelModel model = (IExcelModel) o;
                    if (StringUtils.isNotBlank(model.getErrorMsg())) {
                        eList.add(o);
                    }
                }
            }
            excelCacheService.clearExcelDataCache(key);
            excelCacheService.clearExcelDataCache(ekey);
            excelCacheService.putExcelDataCache(key, list);
            excelCacheService.putExcelDataCache(ekey, eList);

            ExcelImportDTO dto = new ExcelImportDTO(null, null, (LinkedHashMap<String, String>) excelImportDTO.getMap(), excelImportDTO.getHeader());
            dto.setKey(key);
            dto.setAllTotal(Long.parseLong(String.valueOf(list.size())));
            dto.setTotal(Long.parseLong(String.valueOf(eList.size())));
            return AjaxJson.success(dto);
        } else {
            long startTime = System.currentTimeMillis();
            ExcelImportDTO dto = (ExcelImportDTO) excelImportService.importExcel(request, this.getCurrentUser());
            dto.setAllTotal(null == dto.getList() ? 0 : Long.parseLong(String.valueOf(dto.getList().size())));
            setExcelCache(dto, code, false);
            logger.info("简单模板导入总耗时：" + (System.currentTimeMillis() - startTime));
            return AjaxJson.success(dto);
        }
    }

    /**
     * 分页获取导入数据
     */
    @LogMessage(content = "分页获取导入数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/uploadPage")
    @ApiOperation("分页获取导入数据")
    public AjaxJson<ExcelImportDTO> uploadPage(@RequestParam String key, @RequestParam int page, @RequestParam int len) throws Exception {
        List<Object> cacheList = excelCacheService.getListExcelDataCache(key);
        List<Object> list = new ArrayList<>();
        if (!EmptyUtils.isEmpty(cacheList)) {
            list = RAMPageUtil.page(cacheList, page, len);
        }
        String dtoKey = key.split("_")[0] + "_dto";
        ExcelImportDTO dto = excelCacheService.getExcelDataCache(dtoKey, ExcelImportDTO.class);
        dto.setList(list);
        return AjaxJson.success(dto);
    }

    /**
     * 复杂模板导入
     */
    @LogMessage(content = "复杂模板导入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/uploads")
    @ApiOperation("复杂模板导入")
    public AjaxJson<ExcelImportDTO> uploads(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        long startTime = System.currentTimeMillis();
        ExcelImportDTO dto = (ExcelImportDTO) excelImportService.importExcels(request, this.getCurrentUser());
        setExcelCache(dto, code, false);
        logger.info("复杂模板导入总耗时：" + (System.currentTimeMillis() - startTime));
        return AjaxJson.success(dto);
    }

    /**
     * 自定义模板导入
     */
    @LogMessage(content = "自定义模板导入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/uploadCustom")
    @ApiOperation("自定义模板导入")
    public AjaxJson<ExcelImportDTO> uploadCustom(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        long startTime = System.currentTimeMillis();
        ExcelImportDTO dto = (ExcelImportDTO) excelImportService.importExcelCustom(request, this.getCurrentUser());
        logger.info("自定义导入总耗时：" + (System.currentTimeMillis() - startTime));
        setExcelCache(dto, code, true);
        return AjaxJson.success(dto);
    }

    /**
     * 获取上传URL
     */
    @LogMessage(content = "获取上传URL", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/getUrlByCode")
    @ApiOperation("获取上传URL")
    public AjaxJson<String> getUrlByCode(@RequestParam String code) {
        String url = SUFFIX_URL;
        IResourceTemplateEnum resourceTemplate = ResourceTemplateEnum.getResourceTemplate(code);
        if (Objects.nonNull(resourceTemplate) && !StringUtils.isBlank(resourceTemplate.getUrl())) {
            String templateUrl = resourceTemplate.getUrl();
            if (templateUrl.contains(UPLOAD)) {
                url = templateUrl;
            } else {
                url = templateUrl + SUFFIX_URL;
            }
        }
        return AjaxJson.success(url);
    }

    /**
     * 设置Excel缓存
     */
    private void setExcelCache(ExcelImportDTO dto, String code, boolean isPage) {
        String key = getKey(code);
        String ekey = key + "_error";
        String dtoKey = key + "_dto";

        excelCacheService.clearExcelDataCache(key);
        excelCacheService.clearExcelDataCache(ekey);

        List<Object> successList = new ArrayList<>();
        if (null != dto.getList()) {
            successList.addAll(dto.getList());
        }

        List<Object> errorList = new ArrayList<>();
        if (null != dto.getErrorList()) {
            errorList.addAll(dto.getErrorList());
        }

        excelCacheService.putExcelDataCache(key, successList);
        excelCacheService.putExcelDataCache(ekey, errorList);
        excelCacheService.putExcelObjectCache(dtoKey, new ExcelImportDTO(null, null, null, dto.getAlignment(), dto.getSplit()));

        List<Object> list = new ArrayList<>();
        list.addAll(successList);
        list.addAll(errorList);
        dto.setList(list);
        dto.setCode(key);

        if (isPage) {
            dto.setKey(key);
            dto.setAllTotal(Long.parseLong(String.valueOf(successList.size())));
            dto.setTotal(Long.parseLong(String.valueOf(errorList.size())));
        }
    }

}