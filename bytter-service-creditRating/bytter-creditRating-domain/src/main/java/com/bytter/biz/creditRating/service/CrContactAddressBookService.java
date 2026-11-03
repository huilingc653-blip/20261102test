package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.dao.CrContactAddressBookDao;
import com.bytter.biz.creditRating.entity.CrContactAddressBook;
import com.bytter.biz.creditRating.enums.CreditRatingEnum;
import com.bytter.biz.creditRating.util.EmailUtils;
import com.bytter.biz.creditRating.vo.CrContactAddressBookVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.UserConstant;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

@Service(value = "crContactAddressBookService")
@Transactional
public class CrContactAddressBookService extends BaseService<CrContactAddressBook, String> implements IWorkFlowListener
        , ITaskService, IUserExcelVerifyHandler<CrContactAddressBookVo> {

    @Autowired
    private CrContactAddressBookDao crContactAddressBookDao;

    @Resource
    private IMultiLangService multiLangServiceI;

    public List<CrContactAddressBookVo> queryByInstitutionalRawDataId(String insId) {
        return crContactAddressBookDao.queryByInstitutionalRawDataId(insId);
    }

    @Transactional(readOnly = true)
    public List<CrContactAddressBookVo> findByIds(List<String> ids) {
        List<CrContactAddressBookVo> voList = crContactAddressBookDao.findByIds(ids);
        if (null != voList && voList.size() > 0) {
            for (int i = 0; i < voList.size(); i++) {
                voList.get(i).setRowNum(i + 1);
            }
        }
        return voList;
    }


    public AjaxJson saveDetail(CrContactAddressBookVo vo, ICurrentUser user) {
        AjaxJson ajaxJson = new AjaxJson();
        CrContactAddressBook book = new CrContactAddressBook();
        book.setCreator(user.getLoginName());
        checkEmail(vo.getEmail());
        book.setCreateDate(new Date());
        if (StringUtils.isNotBlank(vo.getId())) {
            BeanUtils.copyNotNullProperties(vo, book, "creator", "createDate");
            crContactAddressBookDao.update(book);
        }
        if (StringUtils.isBlank(vo.getId())) {
            BeanUtils.copyNotNullProperties(vo, book, "creator", "createDate");
            crContactAddressBookDao.save(book);
            vo.setId(book.getId());
        }
        ajaxJson.setObj(book.getId());
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }

    private void checkEmail(String email) {
        if (StringUtils.isNotBlank(email)) {
            if (!EmailUtils.isEmail(email)) {
                throw new BusinessException(CreditRatingEnum.CREDIT_FORM_VERIFY_EMAIL_ERROR);
            }
        }

    }

    public CrContactAddressBookVo getById(String id) {
        CrContactAddressBookVo vo = new CrContactAddressBookVo();
        CrContactAddressBook bookDb = crContactAddressBookDao.get(id, true);
        BeanUtils.copyNotNullProperties(bookDb, vo);
        return vo;
    }

    public void updateInstitutionalRawDataIdByIds(List<String> ids, String institutionalRawDataId) {
        crContactAddressBookDao.updateInstitutionalRawDataIdByIds(ids, institutionalRawDataId);
    }

    public AjaxJson saveImport(List<CrContactAddressBookVo> list, ICurrentUser user) {
        AjaxJson ajaxJson = new AjaxJson();
        String addIds = null;
        for (CrContactAddressBookVo detailVO : list) {
            this.saveDetail(detailVO, user);
            if (addIds == null) {
                addIds = detailVO.getId();
            } else {
                addIds = addIds + "," + detailVO.getId();
            }
        }
        ajaxJson.setObj(addIds);
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }

    public Map<String, String> getLangMap() {
        List<String> langList = new ArrayList<>();
        langList.add("table.institution.fileExport");//联系人通讯录导出
        langList.add("table.common.orderNum");//序号
        langList.add("table.institution.userName");//姓名
        langList.add("table.common.gender");//性别
        langList.add("table.institution.job");//职务
        langList.add("table.institution.telephoneNum");//电话号码
        langList.add("table.common.email");//邮箱
        langList.add("table.common.weChatNum");//微信号
        langList.add("table.common.QQNum");//QQ号
        langList.add("common.message.remark");//备注
        return multiLangServiceI.getLangList(LocaleContextHolder.getLocale(), langList);
    }

    public void exportAll(HttpServletRequest request, HttpServletResponse response, CrContactAddressBookVo vo) {
        Map<String, String> langMap = getLangMap();
        String fileName = langMap.containsKey("table.institution.fileExport") ? langMap.get("table.institution.fileExport") : "联系人通讯录导出";
        String codedFileName = fileName;
        try {
            if (ServletUtils.isIE(request)) {
                codedFileName = URLEncoder.encode(codedFileName, "UTF8");
            } else {
                codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download;");
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet(fileName);
            // 锁定表格
            //sheet.protectSheet("edit");
            // 设置表格默认列宽度为20个字节
            sheet.setDefaultColumnWidth(20);
            // 组装标题头
            String orderNumCellName = langMap.containsKey("table.common.orderNum") ? langMap.get("table.common.orderNum") : "序号";
            String userNameCellName = langMap.containsKey("table.institution.userName") ? langMap.get("table.institution.userName") : "姓名";
            String genderCellName = langMap.containsKey("table.common.gender") ? langMap.get("table.common.gender") : "性别";
            String jobCellName = langMap.containsKey("table.institution.job") ? langMap.get("table.institution.job") : "职务";
            String telephoneNumCellName = langMap.containsKey("table.institution.telephoneNum") ? langMap.get("table.institution.telephoneNum") : "电话号码";
            String emailCellName = langMap.containsKey("table.common.email") ? langMap.get("table.common.email") : "邮箱";
            String weChatNumCellName = langMap.containsKey("table.common.weChatNum") ? langMap.get("table.common.weChatNum") : "微信号";
            String QQNumCellName = langMap.containsKey("table.common.QQNum") ? langMap.get("table.common.QQNum") : "QQ号";
            String rmkCellName = langMap.containsKey("common.message.remark") ? langMap.get("common.message.remark") : "备注";
            String[] head = new String[]{orderNumCellName, userNameCellName, genderCellName, jobCellName, telephoneNumCellName, emailCellName, weChatNumCellName, QQNumCellName, rmkCellName};
            this.setHeadCell(workbook, sheet, head, 0);
            HSSFCellStyle rightCellStyle = initStyle(workbook, HorizontalAlignment.RIGHT, true);
            HSSFCellStyle leftUnCellStyle = initStyle(workbook, HorizontalAlignment.LEFT, true);
            HSSFCellStyle centerUnCellStyle = initStyle(workbook, HorizontalAlignment.CENTER, true);
            // 组装数据
            List<CrContactAddressBookVo> dtlList = new ArrayList<>();
            if (EmptyUtils.isEmpty(vo.getId())) {
//                dtlList = this.listLeaseCapitalPlan(leaseCapitalPlanVO.getId());
            } else {
                dtlList = this.findByIds(Arrays.asList(vo.getId().split(",")));
            }
            if (!EmptyUtils.isEmptyList(dtlList)) {
                int index = 1;
                int num = 1;
                for (CrContactAddressBookVo dbVo : dtlList) {
                    HSSFRow row = sheet.createRow(index);
                    // 序号
                    HSSFCell cell = row.createCell(0);
                    cell.setCellStyle(centerUnCellStyle);
                    cell.setCellValue(num);
                    // 姓名
                    HSSFCell cell1 = row.createCell(1);
                    cell1.setCellStyle(centerUnCellStyle);
                    cell1.setCellValue(dbVo.getName());
                    // 性别
                    HSSFCell cell2 = row.createCell(2);
                    cell2.setCellStyle(centerUnCellStyle);
                    cell2.setCellValue(dbVo.getSex());
                    // 职务
                    HSSFCell cell3 = row.createCell(3);
                    cell3.setCellStyle(centerUnCellStyle);
                    cell3.setCellValue(dbVo.getJob());
                    // 电话号码
                    HSSFCell cell4 = row.createCell(4);
                    cell4.setCellStyle(centerUnCellStyle);
                    cell4.setCellValue(dbVo.getTelephoneNum());
                    // 邮箱
                    HSSFCell cell5 = row.createCell(5);
                    cell5.setCellStyle(centerUnCellStyle);
                    cell5.setCellValue(dbVo.getEmail());
                    // 微信号
                    HSSFCell cell6 = row.createCell(6);
                    cell6.setCellStyle(centerUnCellStyle);
                    cell6.setCellValue(dbVo.getWeChatNum());
                    // qq号
                    HSSFCell cell8 = row.createCell(7);
                    cell8.setCellStyle(centerUnCellStyle);
                    cell8.setCellValue(dbVo.getQqNum());
                    // 备注
                    HSSFCell cell10 = row.createCell(8);
                    cell10.setCellStyle(centerUnCellStyle);
                    cell10.setCellValue(dbVo.getRmk());
                    index++;
                    num++;
                }
            }
            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 写入标题头
     *
     * @param sheet
     * @param head
     * @throws Exception
     */
    public void setHeadCell(HSSFWorkbook workbook, HSSFSheet sheet, String[] head, int rowTitle) throws Exception {
        HSSFRow row = sheet.createRow(rowTitle);
        for (int i = 0; i < head.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(getCenterCellStyle(workbook, true));
            cell.setCellValue(head[i]);
        }
    }

    /**
     * 获取居中样式
     *
     * @throws Exception
     */
    public HSSFCellStyle getCenterCellStyle(HSSFWorkbook workbook, Boolean locked) {
        return initStyle(workbook, HorizontalAlignment.CENTER, locked);
    }

    /**
     * 获取一个初始的样式
     */
    public HSSFCellStyle initStyle(HSSFWorkbook workbook, HorizontalAlignment align, Boolean locked) {
        HSSFCellStyle style = workbook.createCellStyle();
        // 边框设置
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 字体居中
        style.setAlignment(align);
        style.setLocked(EmptyUtils.isEmpty(locked) || locked ? true : locked);
        return style;
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void run(String arguments) {

    }

    @Override
    public BaseDao<CrContactAddressBook, String> getEntityDao() {
        return crContactAddressBookDao;
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(CrContactAddressBookVo crContactAddressBookVo, Object... objects) {
        return null;
    }

    @Override
    public void verifyHandler(List<CrContactAddressBookVo> list, Object... objects) {
        if (!EmptyUtils.isEmptyList(list)) {
            List<String> msgList = null;

            for (CrContactAddressBookVo vo : list) {
                msgList = new ArrayList<>();
                //姓名
                if (EmptyUtils.isEmpty(vo.getName())) {
                    msgList.add("sys.form.verify.nameError");
                }

                // 联系电话
                if (!EmptyUtils.isEmpty(vo.getTelephoneNum())) {
                    String phoneRegex = "^1\\d{10}$";
                    Pattern phonePattern = Pattern.compile(phoneRegex);
                    if (!phonePattern.matcher(vo.getTelephoneNum()).matches()) {
                        msgList.add("sys.form.verify.phoneError");
                    }
                }
                // EMAIL
                if (!EmptyUtils.isEmpty(vo.getEmail())) {
                    if (vo.getEmail().length() > 50) {
                        msgList.add("邮件最大长度不能超过50");
                    } else {
                        String emailRegex = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
                        Pattern emailPattern = Pattern.compile(emailRegex);
                        if (!emailPattern.matcher(vo.getEmail()).matches()) {
                            msgList.add("sys.form.verify.emailError");
                        }
                    }
                }
                vo.addErrorMsg(msgList);
            }
        }
    }
}
