package com.bytter.biz.importExcel.controller;
import com.bytter.biz.inneraccount.vo.BtBankAccDtlRpaVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.excel.dto.ExcelImportDTO;
import com.bytter.core.excel.enums.ExcelMessageEnum;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.RAMPageUtil;
import com.bytter.core.utils.Reflections;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.enums.IResourceTemplateEnum;
import com.bytter.poi.enums.ResourceTemplateEnum;
import com.bytter.poi.enums.SplitTypeEnum;
import com.bytter.poi.excel.ExcelImportUtil;
import com.bytter.poi.excel.entity.ImportParams;
import com.bytter.poi.excel.entity.result.ExcelImportResult;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.exception.excel.ExcelImportException;
import com.bytter.poi.handler.ExcelModel;
import com.bytter.poi.handler.inter.ICustomVerifyHandler;
import com.bytter.poi.handler.inter.IExcelModel;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.poi.service.ExcelCacheService;
import com.bytter.poi.service.ExcelImportService;
import com.bytter.poi.util.PoiValidationUtil;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * 导入excel生成列表
 * Created by zhouj on 2017/9/4.
 */
@Controller
@RequestMapping("/payment/import/excel")
public class ImportExcelController extends BaseController {
    private int pageSize = 10;
    @Autowired
    private ExcelImportService excelImportService;
    @Autowired
    private ExcelCacheService excelCacheService;
    @GetMapping("/test")
    public String test() {
        return "attachment/import";
    }
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
            ExcelImportDTO excelImportDTO = (ExcelImportDTO) importExcel(request,this.getCurrentUser());
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
            return JsonUtils.toJSONString(dto);
        } else {
            long startTime = System.currentTimeMillis();
            String code = request.getParameter("code");
            ExcelImportDTO dto = (ExcelImportDTO) importExcel(request,this.getCurrentUser());
            dto.setAllTotal(null == dto.getList() ? 0 : Long.parseLong(String.valueOf(dto.getList().size())));
            setExcelCache(dto, code, false);
            logger.info("简单模板导入总耗时：" + (System.currentTimeMillis() - startTime));
            return JsonUtils.toJSONString(dto);
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
        return JsonUtils.toJSONString(dto);
    }
    @PostMapping("/uploads")
    @ResponseBody
    public Object uploads(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        long startTime = System.currentTimeMillis();
        ExcelImportDTO dto = (ExcelImportDTO) excelImportService.importExcels(request, this.getCurrentUser());
        setExcelCache(dto, code, false);
        logger.info("复杂模板导入总耗时：" + (System.currentTimeMillis() - startTime));
        return JsonUtils.toJSONString(dto);
    }
    @PostMapping("/uploadRpa")
    @ResponseBody
    public Object uploadRpa(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        long startTime = System.currentTimeMillis();
        ExcelImportDTO dto = (ExcelImportDTO) excelImportService.importExcels(request, this.getCurrentUser());
        setExcelCache(dto, code, false);
        logger.info("复杂模板导入总耗时：" + (System.currentTimeMillis() - startTime));
        return JsonUtils.toJSONString(dto);
//        long startTime = System.currentTimeMillis();
//        String code = request.getParameter("code");
//        ExcelImportDTO dto = (ExcelImportDTO) importExcel(request,this.getCurrentUser());
//        dto.setAllTotal(null == dto.getList() ? 0 : Long.parseLong(String.valueOf(dto.getList().size())));
//        setExcelCache(dto, code, false);
//        logger.info("简单模板导入总耗时：" + (System.currentTimeMillis() - startTime));
//        return JsonUtils.toJSONString(dto);
    }
    @PostMapping("/uploadCustom")
    @ResponseBody
    public Object uploadCustom(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        long startTime = System.currentTimeMillis();
        ExcelImportDTO dto = (ExcelImportDTO) excelImportService.importExcelCustom(request, this.getCurrentUser());
        logger.info("自定义导入总耗时：" + (System.currentTimeMillis() - startTime));
        setExcelCache(dto, code, true);
        return JsonUtils.toJSONString(dto);
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
        dto.setSuccessList(successList);
        dto.setList(list);
        dto.setCode(key);
        if (isPage) {
            dto.setKey(key);
            dto.setAllTotal(Long.parseLong(String.valueOf(successList.size())));
            dto.setTotal(Long.parseLong(String.valueOf(errorList.size())));
        }
    }
    
    public Object importExcel(HttpServletRequest request, ICurrentUser user) {
        Object result = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        Map<String, String[]> mapReq = new HashMap<String, String[]>();
        mapReq.putAll(request.getParameterMap());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    String code = request.getParameter("code");
                    String titleRows = request.getParameter("titleRows");
                    String headRows = request.getParameter("headRows");
                    String endRows = request.getParameter("endRows");
                    mapReq.remove("page");
                    mapReq.remove("code");
                    mapReq.remove("titleRows");
                    mapReq.remove("headRows");
                    mapReq.remove("endRows");
                    ExcelImportDTO excelImportDTO = dealExcel(mapReq, user, file, titleRows, headRows, endRows, code);
                    if (EmptyUtils.isEmpty(excelImportDTO)) {
                        excelImportDTO = new ExcelImportDTO();
                    }
                    result = excelImportDTO;
                }
            }
        }
        return result;
    }
    
    /**
     * 普通导入解析excel处理
     *
     * @param mapReq
     * @param user
     * @param file
     * @param titleRows
     * @param headRows
     * @param endRows
     * @param code
     * @return
     */
    private ExcelImportDTO dealExcel(Map<String, String[]> mapReq, ICurrentUser user, MultipartFile file, String titleRows, String headRows, String endRows, String code) {
        Map<String, Object> alignment = new HashMap<String, Object>();
        Map<String, Object> split = new HashMap<String, Object>();
        Object serviceObject = null;
        String[] titleRowsList = titleRows.split(",");
        String[] headRowsList = headRows.split(",");
        String[] endRowsList = endRows.split(",");
        ImportParams params = new ImportParams();
        params.setTitleRows(Integer.parseInt(titleRowsList[0]));
        params.setHeadRows(Integer.parseInt(headRowsList[0]));
        params.setEndRows(Integer.parseInt(endRowsList[0]));
        params.setNeedVerfiy(true);//需要检验
        params.setReadTitleKey(true);//需要返回对应关系
        params.setCurrentUser(user);
        params.setParams(mapReq);
        try {
            IResourceTemplateEnum resourceTemplateEnum = ResourceTemplateEnum.getResourceTemplate(code);
            Class aClass = null;
            try {
                aClass = Class.forName(resourceTemplateEnum.getReflex());
                try {
                    //获取验证类
                    String beanName = (String) Reflections.invokeGetter(aClass.newInstance(), "beanName");
                    if (beanName != null) {
                        serviceObject = ApplicationContextUtil.getContext().getBean(beanName.substring(0, 1).toLowerCase() + beanName.substring(1));
                        if (serviceObject instanceof IExcelVerifyHandler) {
                            params.setVerifyHanlder((IExcelVerifyHandler) serviceObject);
                        }
                        if (serviceObject instanceof IUserExcelVerifyHandler) {
                            //IUserExcelVerifyHandler s = (IUserExcelVerifyHandler)serviceObject;
                            params.setUserVerifyHandler((IUserExcelVerifyHandler) serviceObject);
                        }
                    }
                    dealAlignSplit(aClass, alignment, split);
                } catch (Exception e) {
                    logger.error("导入excel模板解析异常：{}", e);
                    throw new BusinessException(ExcelMessageEnum.EXCEL_MESSAGE_ERROR_TEMPLATE_PARSING);
                }
            } catch (Exception e) {
                logger.error("导入excel模板实例化类异常：{}", e);
                //aClass = Map.class;
                throw new BusinessException(ExcelMessageEnum.EXCEL_MESSAGE_ERROR_TEMPLATE_INSTANCE);
            }
            ExcelImportResult result = ExcelImportUtil.importExcelVerify(file.getInputStream(), aClass, params);
            return excelVerify(serviceObject, result.getList(), params, alignment, split, null, "1");
        } catch (Exception e) {
            logger.error("上传异常：", e);
            if(e instanceof ExcelImportException){
                if(((ExcelImportException) e).getType()!=null){
                    throw new ExcelImportException(((ExcelImportException) e).getType().getMsg());
                }else {
                    throw new ExcelImportException(e.getMessage());
                }
            }else {
                throw new BusinessException(ExcelMessageEnum.EXCEL_MESSAGE_ERROR_TEMPLATE_UPLOAD);
            }
        }
    }
    
    /**
     * 校验方法
     *
     * @param serviceObject
     * @param list
     * @param params
     * @param alignment
     * @param split
     * @param header
     * @param type          类型 1-简单模板  2-复杂模板 3-自定义模板
     * @return
     * @throws Exception
     */
    private ExcelImportDTO excelVerify(Object serviceObject, List list, ImportParams params
            , Map<String, Object> alignment, Map<String, Object> split, LinkedHashMap<String, LinkedHashMap<String, String>> header, String type) throws Exception {
        if (EmptyUtils.isEmptyList(list)) {
            return new ExcelImportDTO();
        }
        List successList = new ArrayList();
        List errorList = new ArrayList();
        //校验
        if (!EmptyUtils.isEmpty(serviceObject) && !EmptyUtils.isEmptyList(list)) {
            long startBtachVerifyTime = System.currentTimeMillis();
            if (serviceObject instanceof IExcelVerifyHandler) {
                IExcelVerifyHandler iExcelVerifyHandler = (IExcelVerifyHandler) serviceObject;
                //运行批量校验
                iExcelVerifyHandler.verifyHandler(list);
            }
            if (serviceObject instanceof IUserExcelVerifyHandler) {
                IUserExcelVerifyHandler iExcelVerifyHandler = (IUserExcelVerifyHandler) serviceObject;
                //运行批量校验
                iExcelVerifyHandler.verifyHandler(list, params.getUserId(), params.getParams());
            }
            if (serviceObject instanceof ICustomVerifyHandler) {
                ICustomVerifyHandler iCustomVerifyHandler = (ICustomVerifyHandler) serviceObject;
                //运行批量校验
                iCustomVerifyHandler.verifyHandler(list, params.getUserId(), params.getImportCustomRules());
            }
            logger.info("批量校验耗时：" + (System.currentTimeMillis() - startBtachVerifyTime));
            long startVerifyTime = System.currentTimeMillis();
            ExcelVerifyHanlderResult result = new ExcelVerifyHanlderResult();
            for (Object o : list) {
                if (o instanceof ExcelModel) {
                    String errorMsg = PoiValidationUtil.validation(o);
                    if (StringUtils.isNotEmpty(errorMsg)) {
                        ((IExcelModel) o).setErrorMsg(errorMsg);
                    }
                    if (null != result) {
                        if (serviceObject instanceof IExcelVerifyHandler) {
                            result = ((IExcelVerifyHandler) serviceObject).verifyHandler(o);
                        }
                        if (serviceObject instanceof IUserExcelVerifyHandler) {
                            result = ((IUserExcelVerifyHandler) serviceObject).verifyHandler(o, params.getUserId(), params.getParams());
                        }
                        if (serviceObject instanceof ICustomVerifyHandler) {
                            result = ((ICustomVerifyHandler) serviceObject).verifyHandler(o, params.getUserId(), params.getImportCustomRules());
                        }
                        if (result != null && !result.isSuccess()) {
                            ((ExcelModel) o).addErrorMsg(result.getMsg());
                        }
                    }
                }
            }
            logger.info("单笔循环校验耗时：" + (System.currentTimeMillis() - startVerifyTime));
            long startTime = System.currentTimeMillis();
            for (Object obj : list) {
                Map<String, Object> map = BeanUtils.beanToMap(obj);
                Object msg = map.get("errorMsg");
                if (EmptyUtils.isEmpty(msg)||EmptyUtils.isEmpty(msg.toString())) {
                    successList.add(obj);
                } else {
                    errorList.add(obj);
                }
            }
            logger.info("循环组装耗时：" + (System.currentTimeMillis() - startTime));
        }
        if ("2".equalsIgnoreCase(type)) {
            return new ExcelImportDTO(successList, errorList, null, alignment, split, header);
        } else if ("3".equalsIgnoreCase(type)) {
            return new ExcelImportDTO(successList, errorList, params.getTitleKey(), alignment, split);
        } else {
            return new ExcelImportDTO(successList, errorList, params.getTitleKey(), alignment, split);
        }
    }
    
    /**
     * 封装 处理对齐和货币格式化方法
     *
     * @param aClss
     * @param alignment
     * @param split
     */
    private void dealAlignSplit(Class<?> aClss, Map<String, Object> alignment, Map<String, Object> split) {
        try {
            List<Field> annoFiles = Reflections.fields(aClss, Excel.class);
            if (!EmptyUtils.isEmptyList(annoFiles)) {
                for (Field field : annoFiles) {
                    Excel excel = field.getAnnotation(Excel.class);
                    HorizontalAlignment horizontalAlignment = excel.align();
                    if (!HorizontalAlignment.GENERAL.equals(horizontalAlignment)) {
                        alignment.put(field.getName(), horizontalAlignment);
                    }
                    if (!SplitTypeEnum.GENERAL.equals(excel.splitType())) {
                        split.put(field.getName(), excel.splitType().toString().toLowerCase());
                    } else {
                        if (!EmptyUtils.isBlank(excel.numFormat())) {
                            if (excel.numFormat().contains("#0.0000")) {
                                split.put(field.getName(), SplitTypeEnum.THOUSAND.toString().toLowerCase());
                            } else if (excel.numFormat().contains("#0.00")) {
                                split.put(field.getName(), SplitTypeEnum.PERCENT.toString().toLowerCase());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("excel模板解析属性格式化异常：{}", e);
        }
    }
}
