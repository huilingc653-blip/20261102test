package com.bytter.poi.service;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.excel.dto.ExcelImportDTO;
import com.bytter.core.excel.enums.ExcelMessageEnum;
import com.bytter.core.utils.*;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelCollection;
import com.bytter.poi.enums.IResourceTemplateEnum;
import com.bytter.poi.enums.ResourceTemplateEnum;
import com.bytter.poi.enums.SplitTypeEnum;
import com.bytter.poi.excel.ExcelImportUtil;
import com.bytter.poi.excel.entity.ImportParams;
import com.bytter.poi.excel.entity.params.ImportCustomRules;
import com.bytter.poi.excel.entity.result.ExcelImportResult;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.excel.entity.vo.TemplateExcelConstants;
import com.bytter.poi.exception.excel.ExcelImportException;
import com.bytter.poi.handler.ExcelModel;
import com.bytter.poi.handler.inter.ICustomVerifyHandler;
import com.bytter.poi.handler.inter.IExcelModel;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.poi.util.PoiValidationUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
/**
 * 导入excel生成模板实现 不做分布式
 * Created by zhouj on 2017/9/11.
 */
@Service()
public class ExcelImportService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 普通模板导入解析方法
     *
     * @param request
     * @param user    当前用户对象
     * @return
     */
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
    public Object importExcels(HttpServletRequest request, ICurrentUser user) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    String code = request.getParameter("code");
                    IResourceTemplateEnum resourceTemplateEnum = ResourceTemplateEnum.getResourceTemplate(code);
                    Class aClass = null;
                    try {
                        aClass = Class.forName(resourceTemplateEnum.getReflex());
                        Object serviceObject = null;
                        String beanName = (String) Reflections.invokeGetter(aClass.newInstance(), "beanName");
                        if (beanName != null) {
                            serviceObject = ApplicationContextUtil.getContext().getBean(beanName.substring(0, 1).toLowerCase() + beanName.substring(1));
                        }
                        String[] titleRowsList = request.getParameter("titleRows").split(",");
                        String[] headRowsList = request.getParameter("headRows").split(",");
                        String[] endRowsList = request.getParameter("endRows").split(",");
                        //主实体
                        ImportParams params = new ImportParams();
                        params.setTitleRows(Integer.parseInt(titleRowsList[0]));
                        params.setHeadRows(Integer.parseInt(headRowsList[0]));
                        params.setEndRows(Integer.parseInt(endRowsList[0]));
                        params.setNeedVerfiy(true);//需要检验
                        params.setReadTitleKey(true);//需要返回对应关系
                        params.setCurrentUser(user);
                        ExcelImportResult resultAll = ExcelImportUtil.importExcelVerify(file.getInputStream(), aClass, params);
                        return dealImportExcels(file, user, aClass, serviceObject, resultAll, params, titleRowsList, headRowsList, endRowsList);
                    } catch (Exception e) {
                        logger.error("导入复杂excel模板解析异常：{}", e);
                        throw new BusinessException(ExcelMessageEnum.EXCEL_MESSAGE_ERROR_CUSTOM_TEMPLATE_PARSING);
                    }
                }
            }
        }
        return null;
    }
    /**
     * 自定义规则导入
     *
     * @param request
     * @param user    当前用户对象
     * @return
     * @authro zuoy 2019-05-14
     */
    public Object importExcelCustom(HttpServletRequest request, ICurrentUser user) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            Map<String, Object> alignment = new HashMap<>();
            Map<String, Object> split = new HashMap<>();
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            String headRows = request.getParameter("headRows");
            String code = request.getParameter("code");
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    IResourceTemplateEnum resourceTemplateEnum = ResourceTemplateEnum.getResourceTemplate(code);
                    Class aClass = null;
                    try {
                        aClass = Class.forName(resourceTemplateEnum.getReflex());
                        Object serviceObject = null;
                        String beanName = (String) Reflections.invokeGetter(aClass.newInstance(), "beanName");
                        if (beanName != null) {
                            serviceObject = ApplicationContextUtil.getContext().getBean(beanName.substring(0, 1).toLowerCase() + beanName.substring(1));
                        }
                        dealAlignSplit(aClass, alignment, split);
                        Map<String, Object> mapReq = new HashMap<>();
                        mapReq.putAll(request.getParameterMap());
                        mapReq.remove("page");
                        mapReq.remove("code");
                        mapReq.remove("titleRows");
                        mapReq.remove("headRows");
                        mapReq.remove("endRows");
                        String suffix = FileUtils.getExtend(file.getOriginalFilename());
                        String encoding = ExcelImportUtil.getCharsetName(file.getInputStream());
                        mapReq.put("suffix", suffix);
                        mapReq.put("user", user);
                        ICustomVerifyHandler iCustomVerifyHandler = (ICustomVerifyHandler) serviceObject;
                        //获取自定义导入规则
                        ImportCustomRules importCustomRules = iCustomVerifyHandler.getImportCustomRules(mapReq);
                        //主实体
                        ImportParams params = new ImportParams();
                        if (importCustomRules.isHead()) {
                            params.setTitleRows(importCustomRules.getStartRow() - 2 < 0 ? 0 : importCustomRules.getStartRow() - 2);
                        } else {
                            params.setTitleRows(importCustomRules.getStartRow() - 1 < 0 ? 0 : importCustomRules.getStartRow() - 1);
                        }
                        params.setHeadRows(null == headRows || "".equalsIgnoreCase(headRows) ? 1 : Integer.parseInt(headRows));
                        params.setStartRows(importCustomRules.getStartRow());
                        params.setNeedVerfiy(true);//需要检验
                        params.setReadTitleKey(true);//需要返回对应关系
                        params.setCurrentUser(user);
                        params.setImportCustomRules(importCustomRules);
                        params.setSuffix(suffix);
                        params.setEncoding(encoding);
                        ExcelImportResult resultAll = null;
                        if ("txt".equalsIgnoreCase(suffix) && !importCustomRules.isSeparator()) {
                            InputStream inputStream = ExcelImportUtil.txtInputStreamConvertExcelInputStream(file.getInputStream(), importCustomRules, params);
                            params.setTitleRows(0);
                            params.setHeadRows(1);
                            params.setStartRows(1);
                            params.setImportCustomRules(importCustomRules);
                            params.setEncoding("GB2312");
                            resultAll = ExcelImportUtil.importExcelVerify(inputStream, aClass, params);
                        } else {
                            resultAll = ExcelImportUtil.importExcelVerify(file.getInputStream(), aClass, params);
                        }
                        return excelVerify(serviceObject, resultAll.getList(), params, alignment, split, null, "3");
                    } catch (ParameterException e) {
                        logger.error("导入文件解析异常：", e);
                        throw e;
                    } catch (ExcelImportException e) {
                        if (e.getMessage().indexOf("不超过") > -1) {
                            logger.error("导入文件解析异常：", e);
                            throw new BusinessException(ExcelMessageEnum.EXCEL_MESSAGE_ERROR_ROWS_GT_20000);
                        } else {
                            logger.error("导入文件解析异常：", e);
                            throw new BusinessException(ExcelMessageEnum.EXCEL_MESSAGE_ERROR_COMPLEX_TEMPLATE_PARSING);
                        }
                    } catch (Exception e) {
                        logger.error("导入文件解析异常：", e);
                        throw new BusinessException(ExcelMessageEnum.EXCEL_MESSAGE_ERROR_COMPLEX_TEMPLATE_PARSING);
                    }
                }
            }
        }
        return null;
    }
    /**
     * 处理复杂表格导入解析
     *
     * @param file
     * @param user
     * @param aClass
     * @param serviceObject
     * @param resultAll
     * @param params
     * @param titleRowsList
     * @param headRowsList
     * @param endRowsList
     * @return
     * @throws Exception
     */
    private ExcelImportDTO dealImportExcels(MultipartFile file, ICurrentUser user, Class aClass, Object serviceObject
            , ExcelImportResult resultAll, ImportParams params, String[] titleRowsList, String[] headRowsList,
                                            String[] endRowsList) throws Exception {
        List list = new ArrayList();
        Map<String, Object> alignment = new HashMap<String, Object>();
        Map<String, Object> split = new HashMap<String, Object>();
        LinkedHashMap<String, LinkedHashMap<String, String>> header = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        if (!EmptyUtils.isEmpty(resultAll)) {
            list = resultAll.getList();
            header.put("table_top", params.getTitleKey());
            if (titleRowsList.length > 1) {
                //获取父类 标识对齐方式+货币格式化
                dealAlignSplit(aClass, alignment, split);
                List<Field> fields = Reflections.fields(aClass, ExcelCollection.class);
                if (!EmptyUtils.isEmptyList(fields)) {
                    for (int i = 0; i < fields.size(); i++) {
                        Field field = fields.get(i);
                        Type genericType = field.getGenericType();
                        if (genericType == null) {
                            continue;
                        }
                        Class<?> genericClazz = null;
                        boolean isNull = true;
                        if (genericType instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) genericType;
                            genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                            params = new ImportParams();
                            params.setTitleRows(Integer.parseInt(titleRowsList[i + 1]));
                            params.setHeadRows(Integer.parseInt(headRowsList[i + 1]));
                            params.setEndRows(Integer.parseInt(endRowsList[i + 1]));
                            params.setNeedVerfiy(true);//需要检验
                            params.setReadTitleKey(true);//需要返回对应关系
                            ExcelImportResult excelImportResult = ExcelImportUtil.importExcelVerify(file.getInputStream(), genericClazz, params);
                            if (!EmptyUtils.isEmpty(excelImportResult)) {
                                for (Object o : list) {
                                    Reflections.setFieldValue(o, field.getName(), excelImportResult.getList());
                                }
                                header.put(field.getName(), params.getTitleKey());
                                isNull = false;
                            }
                        } else {
                            genericClazz = (Class<?>) genericType;
                            params = new ImportParams();
                            params.setTitleRows(Integer.parseInt(titleRowsList[i + 1]));
                            params.setHeadRows(Integer.parseInt(headRowsList[i + 1]));
                            params.setEndRows(Integer.parseInt(endRowsList[i + 1]));
                            params.setNeedVerfiy(true);//需要检验
                            params.setReadTitleKey(true);//需要返回对应关系
                            ExcelImportResult excelImportResult = ExcelImportUtil.importExcelVerify(file.getInputStream(), genericClazz, params);
                            if (!EmptyUtils.isEmpty(excelImportResult)) {
                                for (Object o : list) {
                                    Reflections.setFieldValue(o, field.getName(), excelImportResult.getList().get(0));
                                }
                                header.put(field.getName(), params.getTitleKey());
                                isNull = false;
                            }
                        }
                        if (!isNull) {
                            dealAlignSplit(genericClazz, alignment, split);
                        }
                    }
                }
            }
        }
        return excelVerify(serviceObject, list, params, alignment, split, header, "2");
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
                        IExcelModel model = (IExcelModel) o;
                        // 字段校验需保留批量业务校验的错误，重复提示只显示一次。
                        Set<String> errorMessages = new LinkedHashSet<>();
                        if (StringUtils.isNotEmpty(model.getErrorMsg())) {
                            errorMessages.addAll(Arrays.asList(model.getErrorMsg().split(",")));
                        }
                        errorMessages.addAll(Arrays.asList(errorMsg.split(",")));
                        model.setErrorMsg(StringUtils.join(errorMessages, ","));
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
                boolean b = false;
                for(String k : map.keySet()){
                    if(k.contains(TemplateExcelConstants.FORMATERRORSUFFIX) && map.get(k) != null){
                        map.put(k.replace(TemplateExcelConstants.FORMATERRORSUFFIX,""),map.get(k));
                        b = true;
                    }
                }
                if (null == msg || StringUtils.isBlank(msg.toString())) {
                    if(b){
                        successList.add(map);
                    }else {
                        successList.add(obj);
                    }
                } else {
                    if(b){
                        errorList.add(map);
                    }else {
                        errorList.add(obj);
                    }
                }
            }
            logger.info("循环组装耗时：" + (System.currentTimeMillis() - startTime));
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
