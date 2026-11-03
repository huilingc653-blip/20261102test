package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.dao.CrOwnershipStructureDataDao;
import com.bytter.biz.creditRating.entity.CrOwnershipStructureData;
import com.bytter.biz.creditRating.vo.CrOwnershipStructureDataVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.*;

import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.DictDTO;
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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

@Service("crOwnershipStructureDataService")
@Transactional
public class CrOwnershipStructureDataService extends BaseService<CrOwnershipStructureData, String> implements IWorkFlowListener
        , ITaskService, IExcelVerifyHandler<CrOwnershipStructureDataVo> {

    @Autowired
    private CrOwnershipStructureDataDao ownershipStructureDataDao;

    @DubboReference(check = false)
    private IDictService dictService;

    @Resource
    private IMultiLangService multiLangServiceI;

    @Transactional(readOnly = true)
    public List<CrOwnershipStructureDataVo> queryByInstitutionalRawDataId(String institutionalRawDataId) {
        return ownershipStructureDataDao.queryByInstitutionalRawDataId(institutionalRawDataId);
    }

    @Transactional(readOnly = true)
    public CrOwnershipStructureDataVo getById(String id) {
        CrOwnershipStructureDataVo vo = new CrOwnershipStructureDataVo();
        CrOwnershipStructureData crOwnershipStructureData = ownershipStructureDataDao.get(id, true);
        BeanUtils.copyProperties(crOwnershipStructureData, vo);
        vo.setFinancingWayName(dictService.get("cms.equity.financingWay", vo.getFinancingWay()));
        vo.setInvestmentMethodName(dictService.get("cms.equity.invWay", vo.getInvestmentMethod()));
        return vo;
    }

    @Transactional(readOnly = true)
    public List<CrOwnershipStructureDataVo> findByIds(List<String> ids) {
        List<CrOwnershipStructureDataVo> voList = ownershipStructureDataDao.findByIds(ids);
        if (null != voList && voList.size() > 0) {
            for (int i = 0; i < voList.size(); i++) {
                voList.get(i).setRowNum(i + 1);
            }
        }
        voList.stream().forEach(vo -> {
            if (null != vo.getShareholdingRatio()) {
                vo.setShareholdingRatioStr(String.valueOf(vo.getShareholdingRatio().multiply(new BigDecimal(100)).setScale( 2, BigDecimal.ROUND_HALF_UP)));
            }
        });
        return voList;
    }

    public AjaxJson saveDetail(CrOwnershipStructureDataVo vo, ICurrentUser user) {
        AjaxJson ajaxJson = new AjaxJson();
        CrOwnershipStructureData crOwnershipStructureData = new CrOwnershipStructureData();
        crOwnershipStructureData.setCreator(user.getLoginName());
        crOwnershipStructureData.setCreateDate(new Date());
        if (StringUtils.isNotBlank(vo.getId())) {
            BeanUtils.copyNotNullProperties(vo, crOwnershipStructureData, "creator", "createDate");
            ownershipStructureDataDao.update(crOwnershipStructureData);
        }
        if (StringUtils.isBlank(vo.getId())) {
            BeanUtils.copyNotNullProperties(vo, crOwnershipStructureData, "creator", "createDate");
            if (crOwnershipStructureData.getShareholdingRatio() != null ){
                crOwnershipStructureData.setShareholdingRatio(crOwnershipStructureData.getShareholdingRatio().divide(BigDecimal.valueOf(100)));
            }

            ownershipStructureDataDao.save(crOwnershipStructureData);
            vo.setId(crOwnershipStructureData.getId());
        }
        ajaxJson.setObj(crOwnershipStructureData.getId());
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }

    public void updateInstitutionalRawDataIdByIds(List<String> ids, String institutionalRawDataId) {
        ownershipStructureDataDao.updateInstitutionalRawDataIdByIds(ids, institutionalRawDataId);
    }


    public AjaxJson saveImport(List<CrOwnershipStructureDataVo> list, ICurrentUser user) {
        AjaxJson ajaxJson = new AjaxJson();
        String addIds = null;
        Map<String, DictDTO> financingWayDictDTOMap = dictService.listMap("cms.equity.financingWay");
        Map<String, DictDTO> investmentMethodDictDTOMap = dictService.listMap("cms.equity.invWay");
        for (CrOwnershipStructureDataVo detailVO : list) {
            if (StringUtils.isNotBlank(detailVO.getFinancingWay()) && !("1".equals(detailVO.getFinancingWay()) || "2".equals(detailVO.getFinancingWay()))){
                detailVO.setFinancingWay(financingWayDictDTOMap.get(detailVO.getFinancingWay()).getCode());
            }
            if (StringUtils.isNotBlank(detailVO.getInvestmentMethod()) && !("1".equals(detailVO.getInvestmentMethod()) || "2".equals(detailVO.getInvestmentMethod()))){
                detailVO.setInvestmentMethod(investmentMethodDictDTOMap.get(detailVO.getInvestmentMethod()).getCode());
            }
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
        langList.add("table.cmsEquity.fileExport");//股权结构导出
        langList.add("table.common.orderNum");//序号
        langList.add("table.cmsEquity.investor");//出资人
        langList.add("table.cmsEquity.financingWay");//融资方式
        langList.add("table.cmsEquity.invWay");//出资方式
        langList.add("table.cmsEquity.invMoney");//出资金额
        langList.add("table.cmsEquity.equityPro");//持股比例
        langList.add("table.cmsEquity.assetName");//资产名称
        langList.add("cms.equity.rmk");//备注
        return multiLangServiceI.getLangList(LocaleContextHolder.getLocale(), langList);
    }

    public void exportAll(HttpServletRequest request, HttpServletResponse response, CrOwnershipStructureDataVo vo) {
        Map<String, String> langMap = getLangMap();
        String fileName = langMap.containsKey("table.cmsEquity.fileExport") ? langMap.get("table.cmsEquity.fileExport") : "股权结构导出";
        String codedFileName = fileName;
        Map<String, DictDTO> financingWayDictDTOMap = dictService.listMap("cms.equity.financingWay");
        Map<String, DictDTO> investmentMethodDictDTOMap = dictService.listMap("cms.equity.invWay");
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
            String investorCellName = langMap.containsKey("table.cmsEquity.investor") ? langMap.get("table.cmsEquity.investor") : "出资人";
            String financingWayCellName = langMap.containsKey("table.cmsEquity.financingWay") ? langMap.get("table.cmsEquity.financingWay") : "融资方式";
            String invWayCellName = langMap.containsKey("table.cmsEquity.invWay") ? langMap.get("table.cmsEquity.invWay") : "出资方式";
            String invMoneyCellName = langMap.containsKey("table.cmsEquity.invMoney") ? langMap.get("table.cmsEquity.invMoney") : "出资金额";
            String equityProCellName = langMap.containsKey("table.cmsEquity.equityPro") ? langMap.get("table.cmsEquity.equityPro") : "持股比例";
            String assetNameCellName = langMap.containsKey("table.cmsEquity.assetName") ? langMap.get("table.cmsEquity.assetName") : "资产名称";
            String rmkCellName = langMap.containsKey("cms.equity.rmk") ? langMap.get("cms.equity.rmk") : "备注";

            String[] head = new String[]{orderNumCellName, investorCellName, financingWayCellName, invWayCellName, invMoneyCellName, equityProCellName, assetNameCellName, rmkCellName};
            this.setHeadCell(workbook, sheet, head, 0);
            HSSFCellStyle rightCellStyle = initStyle(workbook, HorizontalAlignment.RIGHT, true);
            HSSFCellStyle leftUnCellStyle = initStyle(workbook, HorizontalAlignment.LEFT, true);
            HSSFCellStyle centerUnCellStyle = initStyle(workbook, HorizontalAlignment.CENTER, true);
            // 组装数据
            List<CrOwnershipStructureDataVo> dtlList = new ArrayList<>();
            if (EmptyUtils.isEmpty(vo.getId())) {
//                dtlList = this.listLeaseCapitalPlan(leaseCapitalPlanVO.getId());
            } else {
                dtlList = this.findByIds(Arrays.asList(vo.getId().split(",")));
            }
            if (!EmptyUtils.isEmptyList(dtlList)) {
                int index = 1;
                int num = 1;
                for (CrOwnershipStructureDataVo dbVo : dtlList) {
                    HSSFRow row = sheet.createRow(index);
                    // 序号
                    HSSFCell cell = row.createCell(0);
                    cell.setCellStyle(centerUnCellStyle);
                    cell.setCellValue(num);
                    // 出资人
                    HSSFCell cell1 = row.createCell(1);
                    cell1.setCellStyle(centerUnCellStyle);
                    cell1.setCellValue(dbVo.getInvestor());
                    // 融资方式
                    HSSFCell cell2 = row.createCell(2);
                    cell2.setCellStyle(centerUnCellStyle);
//                    cell2.setCellValue(DateUtils.formatDate(dbVo.getFinancingWay()));
                    if (StringUtils.isNotBlank(dbVo.getFinancingWay())){
                        cell2.setCellValue(financingWayDictDTOMap.get(dbVo.getFinancingWay()).getName());
                    }

                    // 出资方式
                    HSSFCell cell3 = row.createCell(3);
                    cell3.setCellStyle(rightCellStyle);
                    if (StringUtils.isNotBlank(dbVo.getInvestmentMethod())){
                        cell3.setCellValue(investmentMethodDictDTOMap.get(dbVo.getInvestmentMethod()).getName());
                    }
                    // 出资金额
                    HSSFCell cell4 = row.createCell(4);
                    cell4.setCellStyle(rightCellStyle);
                    BigDecimal contributionAmount = EmptyUtils.isEmpty(dbVo.getContributionAmount()) ? BigDecimal.ZERO : dbVo.getContributionAmount();
                    cell4.setCellValue(NumberUtil.formatNumeric(contributionAmount));
                    // 持股比例
                    HSSFCell cell5 = row.createCell(5);
                    cell5.setCellStyle(rightCellStyle);
//                    BigDecimal shareholdingRatio = EmptyUtils.isEmpty(dbVo.getShareholdingRatio()) ? BigDecimal.ZERO : dbVo.getShareholdingRatio();
                    cell5.setCellValue(dbVo.getShareholdingRatioStr());
                    // 资产名称
                    HSSFCell cell6 = row.createCell(6);
                    cell6.setCellStyle(rightCellStyle);
                    cell6.setCellValue(dbVo.getAssetName());
                    // 备注
                    HSSFCell cell8 = row.createCell(7);
                    cell8.setCellStyle(rightCellStyle);
                    cell8.setCellValue(dbVo.getRmk());
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
            if ("持股比例".equals(head[i])){
                cell.setCellValue(head[i]+"%");
            }else{
                cell.setCellValue(head[i]);
            }
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
    public BaseDao<CrOwnershipStructureData, String> getEntityDao() {
        return ownershipStructureDataDao;
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
    public ExcelVerifyHanlderResult verifyHandler(CrOwnershipStructureDataVo vo) {
        return null;
    }

    @Override
    public void verifyHandler(List<CrOwnershipStructureDataVo> list) {
        if (null != list && list.size() > 0) {
            Map<String, DictDTO> financingWayDictDTOMap = dictService.listMap("cms.equity.financingWay");
            Map<String, DictDTO> investmentMethodDictDTOMap = dictService.listMap("cms.equity.invWay");
            for (CrOwnershipStructureDataVo crOwnershipStructureDataVo : list) {
                List<String> msgList = new ArrayList<>();
                String financingWay = crOwnershipStructureDataVo.getFinancingWay();
                if (!financingWayDictDTOMap.containsKey(financingWay)){
                    msgList.add("融资方式不合法");
                }

                String investmentMethod = crOwnershipStructureDataVo.getInvestmentMethod();
                if (!investmentMethodDictDTOMap.containsKey(investmentMethod)){
                    msgList.add("投资方式不合法");
                }
                if(null != msgList && msgList.size() > 0){
                    crOwnershipStructureDataVo.addErrorMsg(msgList);
                }
            }
        }
    }
}
