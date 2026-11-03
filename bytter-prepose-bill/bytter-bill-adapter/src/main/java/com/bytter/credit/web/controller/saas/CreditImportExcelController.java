package com.bytter.credit.web.controller.saas;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.excel.dto.ExcelImportDTO;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.RAMPageUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.handler.inter.IExcelModel;
import com.bytter.poi.service.ExcelCacheService;
import com.bytter.poi.service.ExcelImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 导入excel生成列表
 * Created by zhouj on 2017/9/4.
 */
@RestController("v1CreditImportExcelController")
@RequestMapping("/v1-credit/credit/import/excel")
public class CreditImportExcelController extends BaseController {
    private int pageSize = 10;
    @Autowired
    private ExcelImportService excelImportService;
    @Autowired
    private ExcelCacheService excelCacheService;

    public String getKey(String code) {
        return code + this.getUserId();//UUIDGenerator.generate()
    }
    //上传模板
    //解析文档转list
    //根据表头、表内容生成列表
    //保存内容返回数据
    @PostMapping("/upload")
    @ResponseBody
    public Object upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String page = request.getParameter("page");
        if ("1".equals(page)) {//分页导入
            String code = request.getParameter("code");
            String key = getKey(code);
            String ekey = key + "_error";
            ExcelImportDTO excelImportDTO = (ExcelImportDTO) excelImportService.importExcel(request,this.getCurrentUser());
            List<Object> list = excelImportDTO.getList();
            List<Object> eList = new ArrayList<Object>();
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
            excelCacheService.putExcelDataCache(key, list);//正确数据
            excelCacheService.putExcelDataCache(ekey, eList);//错误数据
            ExcelImportDTO dto = new ExcelImportDTO(null, null, (LinkedHashMap<String, String>) excelImportDTO.getMap(), excelImportDTO.getHeader());
            dto.setKey(key);
            dto.setAllTotal(Long.parseLong(String.valueOf(list.size())));
            dto.setTotal(Long.parseLong(String.valueOf(eList.size())));
            return AjaxJson.success(dto);
        } else {
            long startTime = System.currentTimeMillis();
            String code = request.getParameter("code");
            ExcelImportDTO dto = (ExcelImportDTO) excelImportService.importExcel(request,this.getCurrentUser());
            dto.setAllTotal(null == dto.getList() ? 0 : Long.parseLong(String.valueOf(dto.getList().size())));
            setExcelCache(dto, code, false);
            logger.info("简单模板导入总耗时：" + (System.currentTimeMillis() - startTime));
            return AjaxJson.success(dto);
        }
    }
    //分页获取数据
    @GetMapping("/uploadPage")
    @ResponseBody
    public Object uploadPage(String key, int page, int len) throws Exception {
        List<Object> cacheList = excelCacheService.getListExcelDataCache(key);
        List<Object> list = new ArrayList<Object>();
        if (!EmptyUtils.isEmpty(cacheList)) {
            list = RAMPageUtil.page(cacheList, page, len);
        }
        String dtoKey = key.split("_")[0] + "_dto";
        ExcelImportDTO dto = excelCacheService.getExcelDataCache(dtoKey, ExcelImportDTO.class);
        dto.setList(list);
        return AjaxJson.success(dto);
    }
    @PostMapping("/uploads")
    @ResponseBody
    public Object uploads(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        long startTime = System.currentTimeMillis();
        ExcelImportDTO dto = (ExcelImportDTO) excelImportService.importExcels(request, this.getCurrentUser());
        setExcelCache(dto, code, false);
        logger.info("复杂模板导入总耗时：" + (System.currentTimeMillis() - startTime));
        return AjaxJson.success(dto);
    }
    @PostMapping("/uploadCustom")
    @ResponseBody
    public Object uploadCustom(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        long startTime = System.currentTimeMillis();
        ExcelImportDTO dto = (ExcelImportDTO) excelImportService.importExcelCustom(request, this.getCurrentUser());
        logger.info("自定义导入总耗时：" + (System.currentTimeMillis() - startTime));
        setExcelCache(dto, code, true);
        return AjaxJson.success(dto);
    }
    public void setExcelCache(ExcelImportDTO dto, String code, boolean isPage) {
        String key = getKey(code);
        String ekey = key + "_error";
        String dtoKey = key + "_dto";
        excelCacheService.clearExcelDataCache(key);
        excelCacheService.clearExcelDataCache(ekey);
        List successList = new ArrayList();
        if (null != dto.getList()) {
            successList.addAll(dto.getList());
        }
        List errorList = new ArrayList();
        if (null != dto.getErrorList()) {
            errorList.addAll(dto.getErrorList());
        }
        excelCacheService.putExcelDataCache(key, successList);
        excelCacheService.putExcelDataCache(ekey, errorList);
        excelCacheService.putExcelObjectCache(dtoKey, new ExcelImportDTO(null, null, null, dto.getAlignment(), dto.getSplit()));
        List list = new ArrayList();
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
