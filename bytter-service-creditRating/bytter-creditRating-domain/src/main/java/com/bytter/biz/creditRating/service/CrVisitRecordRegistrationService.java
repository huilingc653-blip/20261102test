package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.dao.CrVisitRecordRegistrationDao;
import com.bytter.biz.creditRating.entity.CrVisitRecordRegistration;
import com.bytter.biz.creditRating.vo.CrVisitRecordRegistrationVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.*;

import com.bytter.system.api.IExcelExportHandler;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.ITaskService;
import org.apache.commons.compress.utils.Lists;
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

@Service(value = "crVisitRecordRegistrationService")
@Transactional
public class CrVisitRecordRegistrationService extends BaseService<CrVisitRecordRegistration, String> implements IWorkFlowListener
        , ITaskService, IExcelExportHandler<CrVisitRecordRegistrationVo> {

    @Autowired
    private CrVisitRecordRegistrationDao crVisitRecordRegistrationDao;

    @Resource
    private IMultiLangService multiLangServiceI;

    public List<CrVisitRecordRegistrationVo> queryByInstitutionalRawDataId(String insId){
        return  crVisitRecordRegistrationDao.queryByInstitutionalRawDataId(insId);
    }

    public List<CrVisitRecordRegistrationVo> findByIds(List<String> visitIds){
        List<CrVisitRecordRegistrationVo> result = Lists.newArrayList();
        if (null != visitIds && visitIds.size() > 0){
           result = crVisitRecordRegistrationDao.findByIds(visitIds);
            for (int i = 0; i < result.size(); i++) {
                result.get(i).setRowNum(i + 1 );
                if (result.get(i).getVisitDate()!=null){
                    result.get(i).setVisitDateStr(DateUtils.getDateStr(result.get(i).getVisitDate(),"yyyy-MM-dd"));
                }
            }
        }
        return result;
    }

    public AjaxJson saveDetail(CrVisitRecordRegistrationVo vo, ICurrentUser user){
        AjaxJson ajaxJson = new AjaxJson();
        CrVisitRecordRegistration visit = new CrVisitRecordRegistration();
        visit.setCreator(user.getLoginName());
        visit.setCreateDate(new Date());
        if(StringUtils.isNotBlank(vo.getId())){
            BeanUtils.copyNotNullProperties(vo, visit, "creator","createDate");
            crVisitRecordRegistrationDao.update(visit);
        }
        if(StringUtils.isBlank(vo.getId())){
            BeanUtils.copyNotNullProperties(vo, visit,"creator","createDate");
            crVisitRecordRegistrationDao.save(visit);
            vo.setId(visit.getId());
        }
        ajaxJson.setObj(visit.getId());
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }


    public void updateInstitutionalRawDataIdByIds(List<String> ids, String institutionalRawDataId){
        crVisitRecordRegistrationDao.updateInstitutionalRawDataIdByIds(ids, institutionalRawDataId);
    }


    public AjaxJson saveImport(List<CrVisitRecordRegistrationVo> list, ICurrentUser user) {
        AjaxJson ajaxJson = new AjaxJson();
        String addIds = null;
        for (CrVisitRecordRegistrationVo detailVO : list) {
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

    public Map<String,String> getLangMap() {
        List<String> langList = new ArrayList<>();
        langList.add("table.visit.fileExport");//拜访记录登记导出
        langList.add("table.common.orderNum");//序号
        langList.add("table.institution.visitingMatters");//拜访事项
        langList.add("table.institution.visitingPersonnel");//拜访人员
        langList.add("table.institution.visitingObject");//拜访对象
        langList.add("table.institution.visitingObject.job");//拜访对象职务
        langList.add("table.institution.visitingDate");//拜访日期
        langList.add("common.message.remark");//备注
        return multiLangServiceI.getLangList(LocaleContextHolder.getLocale(), langList);
    }

    public void exportAll(HttpServletRequest request, HttpServletResponse response, CrVisitRecordRegistrationVo vo) {
        Map<String, String> langMap = getLangMap();
        String fileName = langMap.containsKey("table.visit.fileExport") ? langMap.get("table.visit.fileExport") : "拜访记录登记导出";
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
            String visitingMattersCellName = langMap.containsKey("table.institution.visitingMatters") ? langMap.get("table.institution.visitingMatters") : "拜访事项";
            String visitingPersonnelCellName = langMap.containsKey("table.institution.visitingPersonnel") ? langMap.get("table.institution.visitingPersonnel") : "拜访人员";
            String visitingObjectCellName = langMap.containsKey("table.institution.visitingObject") ? langMap.get("table.institution.visitingObject") : "拜访对象";
            String visitingObjectJobCellName = langMap.containsKey("table.institution.visitingObject.job") ? langMap.get("table.institution.visitingObject.job") : "拜访对象职务";
            String visitingDateCellName = langMap.containsKey("table.institution.visitingDate") ? langMap.get("table.institution.visitingDate") : "拜访日期";
            String rmkNumCellName = langMap.containsKey("common.message.remark") ? langMap.get("common.message.remark") : "备注";

            String[] head = new String[]{orderNumCellName, visitingMattersCellName, visitingPersonnelCellName, visitingObjectCellName, visitingObjectJobCellName, visitingDateCellName,  rmkNumCellName};
            this.setHeadCell(workbook, sheet, head, 0);
            HSSFCellStyle rightCellStyle = initStyle(workbook, HorizontalAlignment.RIGHT, true);
            HSSFCellStyle leftUnCellStyle = initStyle(workbook, HorizontalAlignment.LEFT, true);
            HSSFCellStyle centerUnCellStyle = initStyle(workbook, HorizontalAlignment.CENTER, true);
            // 组装数据
            List<CrVisitRecordRegistrationVo> dtlList = new ArrayList<>();
            if (EmptyUtils.isEmpty(vo.getId())) {
//                dtlList = this.listLeaseCapitalPlan(leaseCapitalPlanVO.getId());
            } else {
                dtlList = this.findByIds(Arrays.asList(vo.getId().split(",")));
            }
            if (!EmptyUtils.isEmptyList(dtlList)) {
                int index = 1;
                int num = 1;
                for (CrVisitRecordRegistrationVo dbVo : dtlList) {
                    HSSFRow row = sheet.createRow(index);
                    // 序号
                    HSSFCell cell = row.createCell(0);
                    cell.setCellStyle(centerUnCellStyle);
                    cell.setCellValue(num);
                    // 拜访事项
                    HSSFCell cell1 = row.createCell(1);
                    cell1.setCellStyle(centerUnCellStyle);
                    cell1.setCellValue(dbVo.getVisitingMatters());
                    // 拜访人员
                    HSSFCell cell2 = row.createCell(2);
                    cell2.setCellStyle(centerUnCellStyle);
                    cell2.setCellValue(dbVo.getVisitingPersonnel());
                    // 拜访对象
                    HSSFCell cell3 = row.createCell(3);
                    cell3.setCellStyle(centerUnCellStyle);
                    cell3.setCellValue(dbVo.getVisitingObj());
                    // 拜访对象职务
                    HSSFCell cell4 = row.createCell(4);
                    cell4.setCellStyle(centerUnCellStyle);
                    cell4.setCellValue(dbVo.getVisitingObjJob());
                    // 拜访日期
                    HSSFCell cell5 = row.createCell(5);
                    cell5.setCellStyle(centerUnCellStyle);
                    cell5.setCellValue(DateUtils.formatDate(dbVo.getVisitDate()));
                    // 备注
                    HSSFCell cell10 = row.createCell(6);
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
    public BaseDao<CrVisitRecordRegistration, String> getEntityDao() {
        return crVisitRecordRegistrationDao;
    }

    @Override
    public List getExportData(DataTablesInput dataTablesInput, int total) {
        return null;
    }

    @Override
    public String getExportQuerySql(DataTablesInput dataTablesInput) {
        return null;
    }
}
