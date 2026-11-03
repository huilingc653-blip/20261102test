package com.bytter.system.saas.controller.web;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.basic.service.SysCountryCustService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.jqgrid.TreeGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.PoolContant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.utils.TreeNodeUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.dto.OperationAreaDTO;
import com.bytter.system.entity.Corp;
import com.bytter.system.entity.EquityMaintenance;
import com.bytter.system.entity.SystemProvince;
import com.bytter.system.entity.UnitStoreMaintenance;
import com.bytter.system.service.*;
import com.bytter.system.vo.CorpComponentVO;
import com.bytter.system.vo.CorpSimpleVO;
import com.bytter.system.vo.CorpVO;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhoul on 2017/4/11.
 */
@Api(value = "/v1-system/sys/corp", tags = "网点/单位相关")
@RestController("v1CorpController")
@RequestMapping("/v1-system/sys/corp")
public class CorpController extends BaseController {
    @Autowired
    private CorpService corpService;
    @Autowired
    private SysCountryCustService sysCountryCustService;
    @Autowired
    private IFcCorpPoolService fcCorpPoolServiceI;
    @Autowired
    private IMultiLangService multiLangServiceI;

    @Autowired
    private OperationAreaService operationAreaService;

    @Autowired
    private EquityMaintenanceService equityMaintenanceService;

    @Autowired
    private UnitStoreMaintenanceService unitStoreMaintenanceService;

    @Autowired
    private SystemProVinceService systemProVinceService;

    @GetMapping("/index")
    public AjaxJson<List<CorpVO>> index() {
      return   AjaxJson.success(corpService.listAllTreeV2());
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public AjaxJson<Corp> getById(@PathVariable("id") String id) {
        return AjaxJson.success(corpService.get(id, true));
    }

    @PostMapping("/appTree")
    @ResponseBody
    public AjaxJson<List<CorpVO>> appTree() {
        return AjaxJson.success(corpService.listAllTree());
    }


    @GetMapping("/tree.json")
    @ResponseBody
    public AjaxJson<Object> treeJson(CorpVO search) {
        List<CorpVO> list = corpService.getTreeTableV2(search);
        return AjaxJson.success(new TreeGridResponse<>(TreeNodeUtils.transformToList(list))) ;
    }

    @PostMapping("/updateSort")
    @ResponseBody
    public AjaxJson updateSort(String id, Integer sort) {
        if (sort == null) {
            return success();
        }
        corpService.updateSort(id, sort);
        return success();
    }

    @GetMapping("/initCorpRouter")
    @ResponseBody
    public void initCorpRouter() {
        corpService.initFullRouter();
    }

    @GetMapping("/tree")
    @ResponseBody
    public Object tree(String type, String resource) {
        return corpService.listAllTree(type, resource, this.getCurrentUser());
    }

    @GetMapping("/treeAll")
    @ResponseBody
    public AjaxJson< List<CorpVO> > treeAll() {
        return AjaxJson.success(corpService.listAllTree());
    }

    @GetMapping("/treeOfOwner")
    @ResponseBody
    public AjaxJson< List<CorpVO> >  treeOfOwner(String type, String resource) {
        return AjaxJson.success(corpService.listOwnerTree(type, resource, this.getCurrentUser()));
    }

    @GetMapping("/treeBankAccCorp")
    @ResponseBody
    public AjaxJson< List<CorpVO> > tree(String corpId) {
        return AjaxJson.success(corpService.listUserAccountTreeByCorpId(corpId, null));
    }

    /**
     * 根据用户获取用户拥有的单位
     *
     * @return
     */
    @GetMapping("/getCorpTreeByUserId")
    @ResponseBody
    public  AjaxJson< List<CorpVO> > getCorpTreeByUserId() {
        return AjaxJson.success(corpService.listAllTree());
    }

    @GetMapping("/filter")
    @ResponseBody
    public  AjaxJson< List<CorpVO> > filter(CorpComponentVO corpComponentVO) {
        return AjaxJson.success(corpService.getFilterCorp(corpComponentVO, getCurrentUser()));
    }

    @GetMapping("/filter1")
    @ResponseBody
    public  AjaxJson< List<CorpVO> > filter1(CorpComponentVO corpComponentVO) {
        return AjaxJson.success(corpService.getFilterCorp1(corpComponentVO, getCurrentUser()));
    }

    /**
     * 传入单位网点类型获取数据
     *
     * @param type
     * @return
     */
    @GetMapping("/{type}.json")
    @ResponseBody
    public AjaxJson<List<CorpSimpleVO>> json(@PathVariable("type") String type) {
        List<Corp> corpList = corpService.listByType(type);
        return AjaxJson.success(BeanUtils.copyListProperties(corpList, CorpSimpleVO.class));
    }

    @GetMapping("/id.json")
    @ResponseBody
    public AjaxJson< List<CorpSimpleVO>> parentIdJson(String id) {
        List<CorpSimpleVO> list = corpService.listById(id);
        return AjaxJson.success(list);
    }

    @PostMapping("/selectCorp.json")
    @ResponseBody
    public AjaxJson<DataTablesOutput<CorpVO>> selectCorp(@RequestBody V1DataTablesInput input, String corpBank) {
        DataTablesInput dataTablesInput=input.convertAndInitColumns();
        //type 数据查询类型 corp 只取全部单位信息
        String type = dataTablesInput.getParameter().get("type");
        if ("corp".equals(type)) {
            return AjaxJson.success(corpService.getDataTablesUnion(dataTablesInput));
        } else {
            Map<String, Object> formdata = dataTablesInput.getFormdata();
            if (StringUtils.isNotEmpty(corpBank)) {
                formdata.put("corpBank", "corpBank");
            }
            dataTablesInput.setFormdata(formdata);
            return AjaxJson.success(corpService.listCorpAndBtBankAccVO(dataTablesInput));
        }
    }

    @GetMapping("/view")
    public AjaxJson< Map<String,Object>> view(String id, String parentId) {
        Map<String,Object> model=new HashMap<>();
        // 编辑单位
        if (StringUtils.isNotEmpty(id)) {
            model.put("newPage", 1);
            Corp corp = corpService.get(id);
            if (corp != null && StringUtils.isNotBlank(corp.getCountryId())) {
                model.put("country", sysCountryCustService.get(corp.getCountryId(), true));
            }
            CorpVO corpVO = new CorpVO();
            BeanUtils.copyNotNullProperties(corp, corpVO);
            String areaId = corpVO.getOperationAreaId();
            List<OperationAreaDTO> operationAreaDTOS = Lists.newArrayList();
            String provinceCodes = "";
            String provinceNames = "";
            String provinceIds = "";
            if (StringUtils.isNotBlank(areaId)) {
                List<String> collect = Arrays.stream(areaId.split(",")).collect(Collectors.toList());
                operationAreaDTOS = operationAreaService.queryByCods(collect);
            }
            if (CollectionUtil.isNotEmpty(operationAreaDTOS)) {

                String areaName = new String();
                for (OperationAreaDTO vo : operationAreaDTOS) {
                    areaName += vo.getOperationAreaName();
                    areaName += ",";
                    if (StringUtils.isNotBlank(vo.getProvinceId())) {
                        for (String pid : vo.getProvinceId().split(",")) {
                            if (StringUtils.isBlank(pid) || "null".equals(pid)) {
                                continue;
                            }
                            SystemProvince systemProvince = systemProVinceService.get(pid, true);
                            if ("".equals(provinceCodes)) {
                                provinceCodes = systemProvince.getProvinceCode();
                                provinceNames = systemProvince.getProvinceName();
                                provinceIds = systemProvince.getId();
                            } else {
                                provinceCodes = provinceCodes + "," + systemProvince.getProvinceCode();
                                provinceNames = provinceNames + "," + systemProvince.getProvinceName();
                                provinceIds = provinceIds + "," + systemProvince.getId();
                            }
                        }
                    }
                }
                if (StringUtils.isNotBlank(areaName)) {

                    corpVO.setOperationAreaName(areaName.substring(0, areaName.length() - 1));
                }

            }

            if (StringUtils.isNotBlank(corpVO.getProvinceId())) {
                provinceCodes = "";
                provinceNames = "";
                provinceIds = "";
                for (String pid : corpVO.getProvinceId().split(",")) {
                    if (StringUtils.isBlank(pid) || "null".equals(pid)) {
                        continue;
                    }
                    SystemProvince systemProvince = systemProVinceService.get(pid, true);
                    if ("".equals(provinceCodes)) {
                        provinceCodes = systemProvince.getProvinceCode();
                        provinceNames = systemProvince.getProvinceName();
                        provinceIds = systemProvince.getId();
                    } else {
                        provinceCodes = provinceCodes + "," + systemProvince.getProvinceCode();
                        provinceNames = provinceNames + "," + systemProvince.getProvinceName();
                        provinceIds = provinceIds + "," + systemProvince.getId();
                    }
                }
            }

            corpVO.setProvinceCode(provinceCodes);
            corpVO.setProvinceName(provinceNames);
            corpVO.setProvinceId(provinceIds);

            String equityCode = corpVO.getEquityMaintenanceId();
            List<EquityMaintenance> equityMaintenances = Lists.newArrayList();
            if (StringUtils.isNotBlank(equityCode)) {
                List<String> collect = Arrays.stream(equityCode.split(",")).collect(Collectors.toList());
                equityMaintenances = equityMaintenanceService.queryByCodes(collect);
            }
            if (CollectionUtil.isNotEmpty(equityMaintenances)) {

                String areaName = new String();
                for (EquityMaintenance vo : equityMaintenances) {
                    areaName += vo.getEquityName();
                    areaName += ",";
                }
                if (StringUtils.isNotBlank(areaName)) {

                    corpVO.setEquityMaintenanceName(areaName.substring(0, areaName.length() - 1));
                }
            }

            String unistStoreCode = corpVO.getUnitStoreMaintenanceId();
            List<UnitStoreMaintenance> unitStoreMaintenances = Lists.newArrayList();
            if (StringUtils.isNotBlank(unistStoreCode)) {
                List<String> collect = Arrays.stream(unistStoreCode.split(",")).collect(Collectors.toList());
                unitStoreMaintenances = unitStoreMaintenanceService.queryByCodes(collect);
            }
            if (CollectionUtil.isNotEmpty(unitStoreMaintenances)) {

                String areaName = new String();
                for (UnitStoreMaintenance vo : unitStoreMaintenances) {
                    areaName += vo.getStoreName();
                    areaName += ",";
                }
                if (StringUtils.isNotBlank(areaName)) {

                    corpVO.setUnitStoreMaintenanceName(areaName.substring(0, areaName.length() - 1));
                }
            }

            if (ObjectUtil.isEmpty(corpVO.getReportDisplay())) {
                corpVO.setReportDisplay(CommonConstant.YES);
            }
            model.put("corp", corpVO);
            if (StringUtils.isNotEmpty(corp.getParentId())) {
                Corp parentCorp = corpService.get(corp.getParentId());
                if (StringUtils.isEmpty(corp.getUnitCm())) {
                    corp.setUnitCm(parentCorp.getUnitCm());
                }
                model.put("parentCorp", "(" + parentCorp.getCode() + ")" + parentCorp.getName());
            }
        } else {
            // 新增单位
            Corp corp = new Corp();
            if (StringUtils.isNotEmpty(parentId)) {
                corp.setParentId(parentId);
                Corp parentCorp = corpService.get(corp.getParentId());
                corp.setUnitCm(parentCorp.getUnitCm());
            }
            corp.setReportDisplay(CommonConstant.YES);
            model.put("corp", corp);
        }
         return AjaxJson.success(model);
    }

    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(@RequestBody  Corp corp ) {
        return corpService.update(corp, getUserId());
    }

    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody  Corp corp ) {
        corpService.save(corp, getUserId());
        return success();
    }

    @PostMapping("/transfer")
    @ResponseBody
    public AjaxJson transfer(@RequestParam("corpIds[]") List<String> corpIds, String parentId) {
        corpService.transferCorp(corpIds, parentId, getUserId());
        return success();
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        return corpService.deleteByid(id);
    }

    @GetMapping("/judgeCorp")
    @ResponseBody
    public AjaxJson<   HashMap<String, Object>> judgeCorp(String id) {
        HashMap<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(id)) {
            map.put("hasUser", false);
            map.put("hasChildCorp", false);
            return AjaxJson.success(map);
        }
//用户判断统一校验
//        if (!EmptyUtils.isEmptyList(userService.listByCorpId(id))) {
//            map.put("hasUser", true);
//        }
        if (!EmptyUtils.isEmptyList(corpService.listCorpByParentId(id))) {
            map.put("hasChildCorp", true);
        }
        return AjaxJson.success(map);
    }

    /**
     * 获取单位信息
     *
     * @param id
     * @return
     */
    @PostMapping("/getSysCorp")
    @ResponseBody
    public AjaxJson getSysCorp(String id) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        Corp corp = corpService.get(id, true);
        if (StringUtils.isEmpty(corp.getUserAccountCode())) {
            corp.setUserAccountCode(corp.getId());
        }
        ajaxJson.setObj(corp);
        return ajaxJson;
    }

    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(corpService.getDataTablesUnion(input));
    }

    /**
     * 根据corpIds组装单位树
     *
     * @param corpIds
     * @return
     * @author zuoy 2018-06-11
     */
    @GetMapping("/treeByCorpIds")
    @ResponseBody
    public AjaxJson<List<CorpVO> > treeByCorpIds(String corpIds) {
        return AjaxJson.success(corpService.treeByCorpIds(corpIds));
    }

    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<CorpVO> list) {
        this.corpService.saveImport(list, getCurrentUser());
        return success();
    }

    /**
     * 资金池单位树
     *
     * @return
     */
    @GetMapping("/poolTree")
    @ResponseBody
    public AjaxJson<List<Map<String, Object>>> poolTree() {
        List<FcCorpPoolDTO> list = fcCorpPoolServiceI.getAllCorpPool(getCurrentUser(), null);
        List<Map<String, Object>> tempList = new ArrayList<>();
        for (FcCorpPoolDTO corpPool : list) {
            Map<String, Object> map = Maps.newHashMap();
            boolean poolSign = PoolContant.POOL_SIGN_1.equals(corpPool.getPoolSign());
            map.put("id", poolSign ? corpPool.getId() : corpPool.getCorpId());
            map.put("pId", corpPool.getPoolId());
            map.put("code", poolSign ? corpPool.getPoolCode() : corpPool.getCorpCode());
            map.put("name", poolSign ? corpPool.getPoolName() : corpPool.getCorpName());
            map.put("type", poolSign ? CorpConstant.CORP_TYPE_CODE_NET : CorpConstant.CORP_TYPE_CODE_CORP);
            map.put("netId", poolSign ? corpPool.getId() : corpPool.getPoolId());
            tempList.add(map);
        }
        return AjaxJson.success(tempList);
    }

    /**
     * 导出单位信息
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/downloadCorp")
    public void downloadCorp(HttpServletRequest request, HttpServletResponse response, CorpVO search) throws Exception {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download;");
        response.setHeader("content-disposition", "attachment;filename=" + ExportUtils.getFileName(request, "网点/单位管理") + ".xls");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet("sheet");
            // 锁定表格
            //sheet.protectSheet("edit");
            // 设置表格默认列宽度为20个字节
            sheet.setDefaultColumnWidth(20);
            // 组装标题头
            String[] head = new String[]{
                    multiLangServiceI.getLang("table.sysCorp.code")
                    , multiLangServiceI.getLang("table.sysCorp.name")
                    , multiLangServiceI.getLang("table.sysCorp.type")
                    , multiLangServiceI.getLang("table.sysCorp.netName")
                    , multiLangServiceI.getLang("sys.sysCorp.unitAttribute")
                    , multiLangServiceI.getLang("sys.sysCorp.unitCm")
                    , multiLangServiceI.getLang("table.sysCorp.curId")
                    , multiLangServiceI.getLang("table.sysCorp.status")
                    , multiLangServiceI.getLang("common.message.sort")};
//            String[] head = new String[]{"机构代码", "组织名称", "组织类型", "所属网点","单位属性","管理单元","本位币", "状态", "排序"};
            HSSFRow row0 = sheet.createRow(0);
            for (int i = 0; i < head.length; i++) {
                HSSFCell cell = row0.createCell(i);
                cell.setCellStyle(ExportUtils.getHeaderCellStyle(workbook, false));
                cell.setCellValue(head[i]);
            }
//          List<CorpVO> list = corpService.listAllTree();
            List<CorpVO> list = corpService.getTreeTableV2(search);
            list = TreeNodeUtils.transformToList(list);
            if (!EmptyUtils.isEmptyList(list)) {
                HSSFCellStyle centerCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
                HSSFCellStyle leftCellStyle = ExportUtils.getLeftCellStyle(workbook, false);
                int index = 1;
                for (CorpVO vo : list) {
                    HSSFRow row = sheet.createRow(index);
                    // 机构代码
                    HSSFCell cell2 = row.createCell(0);
                    cell2.setCellStyle(leftCellStyle);
                    cell2.setCellValue(vo.getCode());
                    // 组织名称
                    HSSFCell cell1 = row.createCell(1);
                    cell1.setCellStyle(leftCellStyle);
                    cell1.setCellValue(vo.getName());
                    // 组织类型
                    HSSFCell cell3 = row.createCell(2);
                    cell3.setCellStyle(centerCellStyle);
                    cell3.setCellValue(CorpConstant.CORP_TYPE_CODE_NET.equals(vo.getType()) ? multiLangServiceI.getLang("common.button.center") : multiLangServiceI.getLang("common.button.resetAcceptor"));
                    // 所属网点
                    HSSFCell cell4 = row.createCell(3);
                    cell4.setCellStyle(leftCellStyle);
                    cell4.setCellValue(vo.getNetName());
                    // 单位属性
                    HSSFCell cell5 = row.createCell(4);
                    cell5.setCellStyle(leftCellStyle);
                    cell5.setCellValue(vo.getUnitAttributeName());
                    // 管理单元
                    HSSFCell cell6 = row.createCell(5);
                    cell6.setCellStyle(leftCellStyle);
                    cell6.setCellValue(vo.getUnitCmName());
                    // 本位币
                    HSSFCell cell7 = row.createCell(6);
                    cell7.setCellStyle(leftCellStyle);
                    cell7.setCellValue(vo.getCurName());
                    // 状态
                    HSSFCell cell8 = row.createCell(7);
                    cell8.setCellStyle(centerCellStyle);
                    cell8.setCellValue(CorpConstant.CORP_STATUS_ENABLE.equals(vo.getStatus()) ? multiLangServiceI.getLang("common.button.enable") : multiLangServiceI.getLang("common.button.taboo"));
                    // 排序
                    HSSFCell cell9 = row.createCell(8);
                    cell9.setCellStyle(centerCellStyle);
                    cell9.setCellValue(Convert.toStr(vo.getSort(), ""));
                    index++;
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



    @PostMapping("/getCorpByOrgId")
    @ResponseBody
    public AjaxJson<List<CorpVO> > getCorpByOrgId(Model model, @RequestParam("moduleId") String moduleId) {
        List<CorpVO> corpVOList = corpService.listCorpByModuleId(moduleId);
        return AjaxJson.success(corpVOList);
    }
}
