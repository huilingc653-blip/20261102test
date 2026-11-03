package com.bytter.system.inneraccount.util;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.excel.dto.ExcelImportDTO;
import com.bytter.core.excel.enums.ExcelMessageEnum;
import com.bytter.core.utils.*;
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
import com.bytter.poi.util.PoiValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;
@Slf4j
public class ExcelImportSysUtils {
    /**
     * 普通模板导入解析方法
     *
     * @param request
     * @param user    当前用户对象
     * @return
     */
    public static ExcelImportDTO importExcel(HttpServletRequest request, ICurrentUser user) {
    	ExcelImportDTO result = null;
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
    private static ExcelImportDTO dealExcel(Map<String, String[]> mapReq, ICurrentUser user, MultipartFile file, String titleRows, String headRows, String endRows, String code) {
        Map<String, Object> alignment = new HashMap<String, Object>();
        Map<String, Object> split = new HashMap<String, Object>();
        Object serviceObject = null;
        ImportParams params = new ImportParams();
        params.setTitleRows(Integer.parseInt(titleRows));
        params.setHeadRows(Integer.parseInt(headRows));
        params.setEndRows(Integer.parseInt(endRows));
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
                        if("btInputBankInfoService".equals(beanName)){
                            beanName+="Base";
                        }
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
                    throw new BusinessException(ExcelMessageEnum.EXCEL_MESSAGE_ERROR_TEMPLATE_PARSING);
                }
            } catch (Exception e) {
                //aClass = Map.class;
                throw new BusinessException(ExcelMessageEnum.EXCEL_MESSAGE_ERROR_TEMPLATE_INSTANCE);
            }
            ExcelImportResult result = ExcelImportUtil.importExcelVerify(file.getInputStream(), aClass, params);
            return excelVerify(serviceObject, result.getList(), params, alignment, split, null, "1");
        } catch (Exception e) {
            log.error("Excel导入异常 code={}, fileName={}", code, file == null ? null : file.getOriginalFilename(), e);
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
     * 封装 处理对齐和货币格式化方法
     *
     * @param aClss
     * @param alignment
     * @param split
     */
    private static void dealAlignSplit(Class<?> aClss, Map<String, Object> alignment, Map<String, Object> split) {
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
    private static ExcelImportDTO excelVerify(Object serviceObject, List list, ImportParams params
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
            long startTime = System.currentTimeMillis();
            for (Object obj : list) {
                Map<String, Object> map = BeanUtils.beanToMap(obj);
                Object msg = map.get("errorMsg");
                if (null == msg || StringUtils.isBlank(msg.toString())) {
                    successList.add(obj);
                } else {
                    errorList.add(obj);
                }
            }
        }
        if ("2".equalsIgnoreCase(type)) {
            return new ExcelImportDTO(successList,successList, errorList, null, alignment, split, header);
        } else if ("3".equalsIgnoreCase(type)) {
            return new ExcelImportDTO(successList,successList, errorList, params.getTitleKey(), alignment, split);
        } else {
            return new ExcelImportDTO(successList,successList, errorList, params.getTitleKey(), alignment, split);
        }
    }
}
