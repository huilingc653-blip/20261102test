package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexExposureDao;
import com.bytter.biz.foreign.entity.FexExposure;
import com.bytter.biz.foreign.entity.FexExposureDetail;
import com.bytter.biz.foreign.entity.FexExposureMain;
import com.bytter.biz.foreign.vo.FexCorpExposureTemplateVO;
import com.bytter.biz.foreign.vo.FexExposureDetailVO;
import com.bytter.biz.foreign.vo.FexExposureMainVO;
import com.bytter.biz.foreign.vo.FexExposureSearchVO;
import com.bytter.biz.foreign.vo.FexExposureTemplateItemVO;
import com.bytter.biz.foreign.vo.FexExposureTemplateVO;
import com.bytter.biz.foreign.vo.FexExposureVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.FexWorkFlowConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.constant.ForeignExposureConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IDictService;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Service
 * @Description: 单位敞口上报表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Service("fexExposureService")
@Transactional
public class FexExposureService extends BaseService<FexExposure, String> implements IWorkFlowListener, IWorkFlowRevokeListener {
    @Autowired
    private FexExposureDao fexExposureDao;

    @Autowired
    private FexExposureMainService fexExposureMainService;

    @Autowired
    private FexExposureDetailService fexExposureDetailService;

    @Autowired(required = false)
    private IBtCurrencyService currencyService;

    @Autowired
    private FexCorpExposureTemplateService fexCorpExposureTemplateService;

    @Autowired
    private FexExposureTemplateService fexExposureTemplateService;

    @Autowired
    private FexMessageService fexMessageService;

    @Autowired(required=false)
    private IWorkFlowService workFlowService;

    @Autowired(required=false)
    private FexExposureDownloadAndImportService fexExposureDownloadAndImportService;

    @Autowired(required=false)
    private IDictService dictService;

    @Override
    public BaseDao<FexExposure, String> getEntityDao() {
        return fexExposureDao;
    }

    /**
     * 初始化上报明细
     * @param corpId 单位Id
     * @param templateId 单位模板Id
     * @param curId 上报币种
     * @param model
     */
    public void initExposure(String corpId, String templateId, String curId, Model model) {
        //获取模板详情
        FexExposureTemplateVO fexExposureTemplate = fexExposureTemplateService.getByIdUnion(templateId);
        //获取单位模板币种信息
        FexCorpExposureTemplateVO fexCorpExposureTemplateVO = fexCorpExposureTemplateService.getByCorpIdAndTemplateId(corpId, templateId);

        if (CommonConstant.NO.equals(curId)) {
            curId = fexCorpExposureTemplateVO.getCurId();
        }
        String[] curIds = curId.split(CommonConstant.COMMA_SPLIT);
        List<FexExposureTemplateItemVO> listItem = fexExposureTemplate.getListItem();
        HashMap<BtCurrencyDTO, List<FexExposureTemplateItemVO>> dataMap = new HashMap<>();
        for (String cur:curIds) {
            BtCurrencyDTO currencyDTO = currencyService.getObjectById(cur);
            dataMap.put(currencyDTO, listItem);
        }
        model.addAttribute("dataMap", dataMap);
        model.addAttribute("inputCurIds", curId);
        model.addAttribute("periodMap", this.getExposurePeriod(fexExposureTemplate));
    }

    /**
     * 根据模板获取周期设置
     * @param fexExposureTemplate
     * @return
     */
    public LinkedMap<String, FexExposureVO> getExposurePeriod(FexExposureTemplateVO fexExposureTemplate) {
        //填报结束日期
        Date endDate = DateUtils.getDateYMD();
        if (ForeignExposureConstant.FILL_UNIT_YEAR.equals(fexExposureTemplate.getTotalFillUnit())) {
            endDate = DateUtils.addYear(endDate, fexExposureTemplate.getTotalFillPeriod());
        } else if (ForeignExposureConstant.FILL_UNIT_QUARTER.equals(fexExposureTemplate.getTotalFillUnit())) {
            endDate = DateUtils.addQuarter(endDate, fexExposureTemplate.getTotalFillPeriod());
        } else if (ForeignExposureConstant.FILL_UNIT_MONTH.equals(fexExposureTemplate.getTotalFillUnit())) {
            endDate = DateUtils.addMonth(endDate, fexExposureTemplate.getTotalFillPeriod());
        }
        //滚动结束日期
        Date endScrollDate = DateUtils.getDateYMD();
        if (ForeignExposureConstant.FILL_UNIT_YEAR.equals(fexExposureTemplate.getScrollFillUnit())) {
            endScrollDate = DateUtils.addYear(endScrollDate, fexExposureTemplate.getScrollFillPeriod());
        } else if (ForeignExposureConstant.FILL_UNIT_QUARTER.equals(fexExposureTemplate.getScrollFillUnit())) {
            endScrollDate = DateUtils.addQuarter(endScrollDate, fexExposureTemplate.getScrollFillPeriod());
        } else if (ForeignExposureConstant.FILL_UNIT_MONTH.equals(fexExposureTemplate.getScrollFillUnit())) {
            endScrollDate = DateUtils.addMonth(endScrollDate, fexExposureTemplate.getScrollFillPeriod());
        }

        LinkedMap<String, FexExposureVO> periodMap = new LinkedMap<>();
        Date beginResolveDate = DateUtils.getDateYMD(); //分解子周期开始日期
        Date endResolveDate = DateUtils.getDateYMD(); //分解子周期结束日期
        while (endResolveDate.before(endScrollDate)) {
            String exposurePeriod = "";
            if (ForeignExposureConstant.FILL_UNIT_YEAR.equals(fexExposureTemplate.getScrollResolveUnit())) {
                endResolveDate = DateUtils.addYear(endResolveDate, fexExposureTemplate.getScrollResolvePeriod());
                if (endResolveDate.after(endScrollDate)) {
                    endResolveDate = endScrollDate;
                }
                if (DateUtils.format(beginResolveDate, DateUtils.PATTERN_CH_YEAR).equals(DateUtils.format(endResolveDate, DateUtils.PATTERN_CH_YEAR))) {
                    exposurePeriod = DateUtils.format(beginResolveDate, DateUtils.PATTERN_CH_YEAR);
                } else {
                    exposurePeriod = DateUtils.format(beginResolveDate, DateUtils.PATTERN_CH_YEAR) + " - " + DateUtils.format(endResolveDate, DateUtils.PATTERN_CH_YEAR);
                }
            } else if (ForeignExposureConstant.FILL_UNIT_QUARTER.equals(fexExposureTemplate.getScrollResolveUnit())) {
                endResolveDate = DateUtils.addMonth(endResolveDate, 2*fexExposureTemplate.getScrollResolvePeriod());
                if (endResolveDate.after(endScrollDate)) {
                    endResolveDate = endScrollDate;
                }
                exposurePeriod = DateUtils.format(beginResolveDate, DateUtils.PATTERN_CH_YYYYMM)+" - "+DateUtils.format(endResolveDate, DateUtils.PATTERN_CH_YYYYMM);
            } else if (ForeignExposureConstant.FILL_UNIT_MONTH.equals(fexExposureTemplate.getScrollResolveUnit())) {
                endResolveDate = DateUtils.addMonth(endResolveDate, fexExposureTemplate.getScrollResolvePeriod() - 1);
                if (endResolveDate.after(endScrollDate)) {
                    endResolveDate = endScrollDate;
                }
                if (DateUtils.format(beginResolveDate, DateUtils.PATTERN_CH_YYYYMM).equals(DateUtils.format(endResolveDate, DateUtils.PATTERN_CH_YYYYMM))) {
                    exposurePeriod = DateUtils.format(beginResolveDate, DateUtils.PATTERN_CH_YYYYMM);
                } else {
                    exposurePeriod = DateUtils.format(beginResolveDate, DateUtils.PATTERN_CH_YYYYMM) + " - " + DateUtils.format(endResolveDate, DateUtils.PATTERN_CH_YYYYMM);
                }
            }
            FexExposureVO fexExposure = new FexExposureVO();
            fexExposure.setFillUnit(fexExposureTemplate.getScrollResolveUnit());
            fexExposure.setBeginDate(DateUtils.getFirstMonthDate(beginResolveDate));
            fexExposure.setEndDate(DateUtils.getLastMonthDate(endResolveDate));
            periodMap.put(exposurePeriod, fexExposure);
            if(ForeignExposureConstant.FILL_UNIT_QUARTER.equals(fexExposureTemplate.getScrollResolveUnit())
            || ForeignExposureConstant.FILL_UNIT_MONTH.equals(fexExposureTemplate.getScrollResolveUnit())){
                endResolveDate = DateUtils.addMonth(endResolveDate, 1);
            }
            beginResolveDate = endResolveDate;
        }
        //前面按周期填报，后续是否按年度填报
        if (CommonConstant.YES.equals(fexExposureTemplate.getIsYearFill())) {
            Date endScrollDateTemp = endScrollDate;
            if(endScrollDateTemp.before(endDate)
                && !DateUtils.format(endScrollDateTemp, DateUtils.PATTERN_CH_YEAR).equals(DateUtils.format(endDate, DateUtils.PATTERN_CH_YEAR))){
                while (endScrollDateTemp.before(endDate)) {
                    String exposurePeriod = DateUtils.format(endScrollDateTemp, DateUtils.PATTERN_CH_YEAR);
                    FexExposureVO fexExposure = new FexExposureVO();
                    fexExposure.setFillUnit(ForeignExposureConstant.FILL_UNIT_YEAR);
                    fexExposure.setBeginDate(DateUtils.getFirstYearDate(endScrollDateTemp));
                    fexExposure.setEndDate(DateUtils.getLastYearDate(endScrollDateTemp));
                    periodMap.put(exposurePeriod, fexExposure);
                    endScrollDateTemp = DateUtils.addYear(endScrollDateTemp, 1);
                }
            }
        }
        //前面按周期填报，后续是否按总额填报
        if (CommonConstant.YES.equals(fexExposureTemplate.getIsTotalFill())) {
            if(endScrollDate.before(endDate) &&
                    !DateUtils.format(endScrollDate, DateUtils.PATTERN_CH_YEAR).equals(DateUtils.format(endDate, DateUtils.PATTERN_CH_YEAR))){
                String exposurePeriod = DateUtils.format(endScrollDate, DateUtils.PATTERN_CH_YEAR)+" - "+DateUtils.format(endDate, DateUtils.PATTERN_CH_YEAR)+"周期总额";
                FexExposureVO fexExposure = new FexExposureVO();
                fexExposure.setFillUnit(ForeignExposureConstant.FILL_UNIT_TOTAL);
                fexExposure.setBeginDate(DateUtils.getFirstYearDate(endScrollDate));
                fexExposure.setEndDate(DateUtils.getLastYearDate(endDate));
                periodMap.put(exposurePeriod, fexExposure);
            }
        }
        return periodMap;
    }

    /**
     * 外汇敞口上报保存
     * @param exposureMainVO
     * @param request
     * @param user
     */
    public void saveExposure(FexExposureMainVO exposureMainVO, HttpServletRequest request, ICurrentUser user) {
        //获取单位模板币种信息
        FexCorpExposureTemplateVO fexCorpExposureTemplateVO = fexCorpExposureTemplateService.getByCorpIdAndTemplateId(exposureMainVO.getCorpId(), exposureMainVO.getTemplateId());
        //获取模板详情
        FexExposureTemplateVO fexExposureTemplate = fexExposureTemplateService.getByIdUnion(fexCorpExposureTemplateVO.getTemplateId());
        //获取周期设置
        LinkedMap<String, FexExposureVO> periodMap = this.getExposurePeriod(fexExposureTemplate);
        if (StringUtils.isBlank(exposureMainVO.getCurId())){
            exposureMainVO.setCurId(fexCorpExposureTemplateVO.getCurId());
        }
        //生成敞口上报主表信息
        FexExposureMain fexExposureMain = new FexExposureMain();
        BeanUtils.copyNotNullProperties(fexExposureTemplate, fexExposureMain);
        BeanUtils.copyNotNullProperties(exposureMainVO, fexExposureMain);
        fexExposureMain.setCurId(exposureMainVO.getCurId());
        fexExposureMain.setTemplateId(fexExposureTemplate.getId());
        fexExposureMain.setEnrolDate(DateUtils.getDate());
        fexExposureMain.setCreateBy(user.getLoginName());
        fexExposureMain.setCreateDate(DateUtils.getDate());
        fexExposureMain.setUpdateBy(null);
        fexExposureMain.setUpdateDate(null);
        fexExposureMain.setId("");
        fexExposureMainService.saveEntity(fexExposureMain);
        String[] curIds = exposureMainVO.getCurId().split(CommonConstant.COMMA_SPLIT);
        for (String curId:curIds) {
            int i = 1;
            for (String key : periodMap.keySet()) {
                FexExposure entity = new FexExposure();
                BeanUtils.copyNotNullProperties(periodMap.get(key), entity);
                entity.setCurId(curId);
                entity.setMainId(fexExposureMain.getId());
                entity.setSort(i);
                this.saveEntity(entity);

                String[] itemCode = request.getParameterValues("itemCode_"+curId);//科目代码
                String[] itemName = request.getParameterValues("itemName_"+curId);//科目名称
                String[] sign = request.getParameterValues("sign_"+curId);//资金流向 1,资金流入 -1,资金流出
                String[] isNaturalHedge = request.getParameterValues("isNaturalHedge_"+curId);//科目内是否自然对冲  1-是 0-否
                String[] fillMoney = request.getParameterValues("fillMoney"+i+"_"+curId);//填报金额
                List<FexExposureDetail> dtlList = new ArrayList<>();
                for(int j=0;j<fillMoney.length;j++) {
                    FexExposureDetail dtl = new FexExposureDetail();
                    dtl.setExposureId(entity.getId());
                    dtl.setCurId(curId);
                    dtl.setItemCode(itemCode[j]);
                    dtl.setItemName(itemName[j]);
                    dtl.setSign(Integer.parseInt(sign[j]));
                    dtl.setIsNaturalHedge(isNaturalHedge[j]);
                    dtl.setFillMoney(new BigDecimal(fillMoney[j]));
                    dtlList.add(dtl);
                }
                this.fexExposureDetailService.batchSave(dtlList);
                i++;
            }
        }
        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_ADD_TEMPSAVE_SUCC;
        //启动审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(fexExposureMain.getStatus())) {
            messageType = FexMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_ADD_SUBMIT_SUCC;
            //调用审批后方法
            getStartHandler().handler(FexWorkFlowConstant.FEX_EXPOSURE_MAIN, fexExposureMain.getId(), fexExposureMain, fexExposureMain.getCorpId(), user.getId());
        }
        if (user !=null) {
            //生成日志
            fexMessageService.saveMessage(fexExposureMain.getId(), messageType, message, FexMessageConstant.BUS_TYPE_EXPOSURE, user);
        }
    }

    /**
     * 删除上报明细
     * @param id
     * @param user
     */
    public void deleteExposure(String id, ICurrentUser user) {
        this.fexExposureDetailService.deleteByMainId(id);
        this.fexExposureDao.deleteByMainId(id);
        fexExposureMainService.delete(id);
        //生成日志
        fexMessageService.saveMessage(id, FexMessageConstant.MESSAGE_TYPE_DELETE, FexMessageConstant.FEX_LOG_DELETE_SUCC, FexMessageConstant.BUS_TYPE_EXPOSURE, user);
    }

    /**
     * 查看上报明细
     * @param id
     * @param model
     */
    public void viewExposure(String id, Model model) {
        //获取上报主信息
        FexExposureMainVO main = fexExposureMainService.getByIdUnion(id);
        //根据上报主信息获取上报明细
        String[] curIds = main.getCurId().split(CommonConstant.COMMA_SPLIT);
        LinkedMap<BtCurrencyDTO, List<FexExposureVO>> dataMap = new LinkedMap<>();
        List<FexExposureDetailVO> listDtl = null;
        for (String curId:curIds) {
            List<FexExposureVO> listExposure = this.listVOByMainId(main.getId(), curId);
            if (listExposure != null && listExposure.size() >0) {
                BtCurrencyDTO currencyDTO = currencyService.getObjectById(curId);
                dataMap.put(currencyDTO, listExposure);
                if (listExposure != null && listExposure.size() > 0) {
                    listDtl = listExposure.get(0).getListDtl();
                }
            }
        }
        if (dataMap != null && dataMap.size() > 0) {
            model.addAttribute("periodSize", dataMap.getValue(0).size());
        }
        model.addAttribute("dataMap", dataMap);
        model.addAttribute("listDtl", listDtl);
        model.addAttribute("fexExposureMainPage", main);
        List<BtCurrencyDTO> listCur = currencyService.listAllBtCurrency();
        String filterCur = "";
        for(BtCurrencyDTO cur:listCur){
            if (main.getCurId().indexOf(cur.getId()) == -1) {
                filterCur += cur.getId() + CommonConstant.COMMA_SPLIT;
            }
        }
        model.addAttribute("filterCur", filterCur);
    }

    /**
     * 根据敞口上报主信息获取上报明细
     * @param mainId
     * @param curId
     * @return
     */
    public List<FexExposureVO> listVOByMainId(String mainId, String curId) {
        List<FexExposureVO> list = fexExposureDao.listVOByMainId(mainId, curId);
        for (FexExposureVO exposureVO:list) {
            exposureVO.setListDtl(fexExposureDetailService.listVOByExposureId(exposureVO.getId()));
            String exposurePeriod = "";
            if (ForeignExposureConstant.FILL_UNIT_YEAR.equals(exposureVO.getFillUnit())) {
                if (DateUtils.format(exposureVO.getBeginDate(), DateUtils.PATTERN_CH_YEAR).equals(DateUtils.format(exposureVO.getEndDate(), DateUtils.PATTERN_CH_YEAR))) {
                    exposurePeriod = DateUtils.format(exposureVO.getBeginDate(), DateUtils.PATTERN_CH_YEAR);
                } else {
                    exposurePeriod = DateUtils.format(exposureVO.getBeginDate(), DateUtils.PATTERN_CH_YEAR) + " - " + DateUtils.format(exposureVO.getEndDate(), DateUtils.PATTERN_CH_YEAR);
                }
            } else if (ForeignExposureConstant.FILL_UNIT_QUARTER.equals(exposureVO.getFillUnit())) {
                exposurePeriod = DateUtils.format(exposureVO.getBeginDate(), DateUtils.PATTERN_CH_YYYYMM)+" - "+DateUtils.format(exposureVO.getEndDate(), DateUtils.PATTERN_CH_YYYYMM);
            } else if (ForeignExposureConstant.FILL_UNIT_MONTH.equals(exposureVO.getFillUnit())) {
                if (DateUtils.format(exposureVO.getBeginDate(), DateUtils.PATTERN_CH_YYYYMM).equals(DateUtils.format(exposureVO.getEndDate(), DateUtils.PATTERN_CH_YYYYMM))) {
                    exposurePeriod = DateUtils.format(exposureVO.getBeginDate(), DateUtils.PATTERN_CH_YYYYMM);
                } else {
                    exposurePeriod = DateUtils.format(exposureVO.getBeginDate(), DateUtils.PATTERN_CH_YYYYMM) + " - " + DateUtils.format(exposureVO.getEndDate(), DateUtils.PATTERN_CH_YYYYMM);
                }
            } else if (ForeignExposureConstant.FILL_UNIT_TOTAL.equals(exposureVO.getFillUnit())) {
                exposurePeriod = DateUtils.format(exposureVO.getBeginDate(), DateUtils.PATTERN_CH_YEAR)+" - "+DateUtils.format(exposureVO.getEndDate(), DateUtils.PATTERN_CH_YEAR)+"周期总额";
            }
            exposureVO.setExposurePeriod(exposurePeriod);
        }
        return list;
    }

    /**
     * 根据敞口上报主信息获取上报明细
     * @param searchVO
     * @return
     */
    public List<FexExposureVO> listVOBySearch(FexExposureSearchVO searchVO) {
        List<FexExposureVO> list = fexExposureDao.listVOBySearch(searchVO);
        for (FexExposureVO exposureVO:list) {
            exposureVO.setListDtl(fexExposureDetailService.listVOByExposureId(exposureVO.getId(), searchVO.getItemCode()));
            String exposurePeriod = "";
            if (ForeignExposureConstant.FILL_UNIT_YEAR.equals(exposureVO.getFillUnit())) {
                exposurePeriod = DateUtils.format(exposureVO.getBeginDate(), DateUtils.PATTERN_CH_YEAR);
            } else if (ForeignExposureConstant.FILL_UNIT_QUARTER.equals(exposureVO.getFillUnit())) {
                exposurePeriod = DateUtils.format(exposureVO.getBeginDate(), DateUtils.PATTERN_CH_YYYYMM)+" - "+DateUtils.format(exposureVO.getEndDate(), DateUtils.PATTERN_CH_YYYYMM);
            } else if (ForeignExposureConstant.FILL_UNIT_MONTH.equals(exposureVO.getFillUnit())) {
                exposurePeriod = DateUtils.format(exposureVO.getBeginDate(), DateUtils.PATTERN_CH_YYYYMM)+" - "+DateUtils.format(exposureVO.getEndDate(), DateUtils.PATTERN_CH_YYYYMM);
            } else if (ForeignExposureConstant.FILL_UNIT_TOTAL.equals(exposureVO.getFillUnit())) {
                exposurePeriod = DateUtils.format(exposureVO.getBeginDate(), DateUtils.PATTERN_CH_YEAR)+" - "+DateUtils.format(exposureVO.getEndDate(), DateUtils.PATTERN_CH_YEAR)+"周期总额";
            }
            exposureVO.setExposurePeriod(exposurePeriod);
        }
        return list;
    }

    /**
     * 查看上报明细
     * @param id
     * @param model
     */
    public void viewUpadateExposure(String id, String curId, Model model) {
        //获取上报主信息
        FexExposureMainVO main = fexExposureMainService.getByIdUnion(id);
        if(StringUtils.isBlank(curId)){
            curId = main.getCurId();
        }
        //根据上报主信息获取上报明细
        String[] curIds = curId.split(CommonConstant.COMMA_SPLIT);
        LinkedMap<BtCurrencyDTO, List<FexExposureVO>> dataMap = new LinkedMap<>();
        //获取所有的上报信息
        LinkedMap<String, List<FexExposureVO>> exposureMap = this.getExposureMap(main.getId());
        for (String cur:curIds) {
            List<FexExposureVO> initExposureList = exposureMap.get(cur);
            if(EmptyUtils.isEmptyList(initExposureList) && exposureMap != null && exposureMap.size() > 0){
                initExposureList = exposureMap.getValue(0);
                for (FexExposureVO exposureVO:initExposureList) {
                    for(FexExposureDetailVO dtl:exposureVO.getListDtl()){
                        dtl.setFillMoney(BigDecimal.ZERO);
                    }
                }
            }
            BtCurrencyDTO currencyDTO = currencyService.getObjectById(cur);
            dataMap.put(currencyDTO, initExposureList);
        }
        model.addAttribute("dataMap", dataMap);
        if (dataMap != null && dataMap.size() > 0) {
            model.addAttribute("listDtl", dataMap.getValue(0).get(0).getListDtl());
            model.addAttribute("periodSize", dataMap.getValue(0).size());
        }
        model.addAttribute("inputCurIds", curId);
        model.addAttribute("fexExposureMainPage", main);
    }

    /**
     * 获取敞口上报集合（按币种区分）
     * @param id
     * @return
     */
    public LinkedMap<String, List<FexExposureVO>> getExposureMap(String id){
        LinkedMap<String, List<FexExposureVO>> exposureMap = new LinkedMap<>();
        //获取所有的上报信息
        List<FexExposureVO> listExposure = this.listVOByMainId(id, null);
        for (FexExposureVO exposureVO:listExposure) {
            List<FexExposureVO> tempList = null;
            if(exposureMap.containsKey(exposureVO.getCurId())){
                tempList = exposureMap.get(exposureVO.getCurId());
            } else {
                tempList = new ArrayList<>();
            }
            tempList.add(exposureVO);
            exposureMap.put(exposureVO.getCurId(), tempList);
        }
        return exposureMap;
    }

    /**
     * 外汇敞口上报调整
     * @param exposureMainVO
     * @param request
     * @param user
     */
    public void updateExposure(FexExposureMainVO exposureMainVO, HttpServletRequest request, ICurrentUser user) {
        //生成敞口上报主表信息
        FexExposureMain fexExposureMain = fexExposureMainService.get(exposureMainVO.getId(), true);
        fexExposureMain.setCurId(exposureMainVO.getCurId());
        fexExposureMain.setStatus(exposureMainVO.getStatus());
        fexExposureMain.setUpdateBy(null);
        fexExposureMain.setUpdateDate(null);
        fexExposureMainService.updateEntity(fexExposureMain);
        //获取所有的上报信息
        LinkedMap<String, List<FexExposureVO>> exposureMap = this.getExposureMap(fexExposureMain.getId());
        //获取周期设置
        LinkedMap<String, FexExposureVO> periodMap = new LinkedMap<>();
        for(FexExposureVO exposureVO:exposureMap.getValue(0)){
            exposureVO.setId("");
            periodMap.put(exposureVO.getExposurePeriod(), exposureVO);
        }
        //删除原明细
        this.fexExposureDetailService.deleteByMainId(exposureMainVO.getId());
        this.fexExposureDao.deleteByMainId(exposureMainVO.getId());
        String[] curIds = exposureMainVO.getCurId().split(CommonConstant.COMMA_SPLIT);
        for (String curId:curIds) {
            int i = 1;
            for (String key : periodMap.keySet()) {
                FexExposure entity = new FexExposure();
                BeanUtils.copyNotNullProperties(periodMap.get(key), entity);
                entity.setCurId(curId);
                entity.setMainId(fexExposureMain.getId());
                entity.setSort(i);
                this.saveEntity(entity);

                String[] itemCode = request.getParameterValues("itemCode_"+curId);//科目代码
                String[] itemName = request.getParameterValues("itemName_"+curId);//科目名称
                String[] sign = request.getParameterValues("sign_"+curId);//资金流向 1,资金流入 -1,资金流出
                String[] isNaturalHedge = request.getParameterValues("isNaturalHedge_"+curId);//科目内是否自然对冲  1-是 0-否
                String[] fillMoney = request.getParameterValues("fillMoney"+i+"_"+curId);//填报金额
                List<FexExposureDetail> dtlList = new ArrayList<>();
                for(int j=0;j<fillMoney.length;j++) {
                    FexExposureDetail dtl = new FexExposureDetail();
                    dtl.setExposureId(entity.getId());
                    dtl.setCurId(curId);
                    dtl.setItemCode(itemCode[j]);
                    dtl.setItemName(itemName[j]);
                    dtl.setSign(Integer.parseInt(sign[j]));
                    dtl.setIsNaturalHedge(isNaturalHedge[j]);
                    dtl.setFillMoney(new BigDecimal(fillMoney[j]));
                    dtlList.add(dtl);
                }
                this.fexExposureDetailService.batchSave(dtlList);
                i++;
            }
        }
        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_ADD_TEMPSAVE_SUCC;
        //启动审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(fexExposureMain.getStatus())) {
            messageType = FexMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_ADD_SUBMIT_SUCC;
            //调用审批后方法
            getStartHandler().handler(FexWorkFlowConstant.FEX_EXPOSURE_MAIN, fexExposureMain.getId(), fexExposureMain, fexExposureMain.getCorpId(), user.getId());
        }
        if (user !=null) {
            //生成日志
            fexMessageService.saveMessage(fexExposureMain.getId(), messageType, message, FexMessageConstant.BUS_TYPE_EXPOSURE, user);
        }
    }

    /**
     * 统计报表查询明细
     * @param searchVO
     * @param model
     */
    public void viewExposureQuery(FexExposureSearchVO searchVO, Model model) {
        //获取上报主信息
        FexExposureMainVO main = fexExposureMainService.getByIdUnion(searchVO.getId());
        //根据上报主信息获取上报明细
        String[] curIds = main.getCurId().split(CommonConstant.COMMA_SPLIT);
        if (StringUtils.isNotBlank(searchVO.getCurId())) {
            curIds = searchVO.getCurId().split(CommonConstant.COMMA_SPLIT);
        }
        LinkedMap<BtCurrencyDTO, List<FexExposureVO>> dataMap = new LinkedMap<>();
        List<FexExposureDetailVO> listDtl = null;
        for (String cur:curIds) {
            searchVO.setCurId(cur);
            List<FexExposureVO> listExposure = this.listVOBySearch(searchVO);
            if (!EmptyUtils.isEmptyList(listExposure)) {
                BtCurrencyDTO currencyDTO = currencyService.getObjectById(cur);
                dataMap.put(currencyDTO, listExposure);
                listDtl = listExposure.get(0).getListDtl();
            }
        }
        if (dataMap != null && dataMap.size() > 0) {
            model.addAttribute("periodSize", dataMap.getValue(0).size());
        } else {
            model.addAttribute("periodSize", 0);
        }
        model.addAttribute("inputCurIds", searchVO.getCurId());
        model.addAttribute("dataMap", dataMap);
        model.addAttribute("listDtl", listDtl);
        model.addAttribute("fexExposureMainPage", main);
    }

    /**
     * 导出报表
     * @param request
     * @param model
     * @param filePath
     */
    public void exporFexExposure(HttpServletRequest request, Model model, String filePath, String fileName) {
        FileOutputStream fout = null;
        try {
            File file = this.fexExposureDownloadAndImportService.getFile(filePath);

            // 声明一个工作薄
            XSSFWorkbook workbook = new XSSFWorkbook();
            // 生成一个表格
            XSSFSheet sheet = workbook.createSheet(fileName);
            // 锁定表格
            //sheet.protectSheet("edit");
            // 设置表格默认列宽度为20个字节
            sheet.setDefaultColumnWidth(20);
            // 样式
            XSSFCellStyle centerLockCellStyle = this.fexExposureDownloadAndImportService.getCenterCellStyle(workbook);
            XSSFCellStyle leftLockCellStyle = this.fexExposureDownloadAndImportService.getLeftCellStyle(workbook);
            XSSFCellStyle rightLockCellStyle = this.fexExposureDownloadAndImportService.getRightCellStyle(workbook);

            Map<String, Object> map = model.asMap();
            FexExposureMainVO main = (FexExposureMainVO)map.get("fexExposureMainPage");
            // 组装敞口主信息
            this.setExposureMain(workbook, sheet, main);

            LinkedMap<BtCurrencyDTO, List<FexExposureVO>> dataMap = (LinkedMap)map.get("dataMap");
            List<FexExposureDetailVO> listDtl = (List<FexExposureDetailVO>)map.get("listDtl");
            int index = 5;
            for (BtCurrencyDTO curData:dataMap.keySet()) {
                // 币别
                XSSFRow rowCur = sheet.createRow(index);
                XSSFCell cellCur = rowCur.createCell(0);
                cellCur.setCellValue("币别："+curData.getEnglishCode());
                cellCur.setCellStyle(leftLockCellStyle);
                index = index + 1;

                // 组装表格标题头
                List<String> headList = new ArrayList<>();
                headList.add("序号");
                headList.add("科目编号");
                headList.add("科目名称");
                for (FexExposureVO exposure:dataMap.get(curData)) {
                    headList.add(exposure.getExposurePeriod());
                }
                // 标题
                XSSFRow rowHead = sheet.createRow(index);
                for (int i = 0; i < headList.size(); i++) {
                    XSSFCell cellHead = rowHead.createCell(i);
                    cellHead.setCellValue(headList.get(i));
                    cellHead.setCellStyle(centerLockCellStyle);
                }
                index = index + 1;
                int no = 1;
                for (FexExposureDetailVO data:listDtl) {
                    int cellNo = 0;
                    XSSFRow row = sheet.createRow(index);
                    // 序号
                    XSSFCell cell = row.createCell(cellNo++);
                    cell.setCellStyle(centerLockCellStyle);
                    cell.setCellValue(no++);
                    // 科目编号
                    XSSFCell cell1 = row.createCell(cellNo++);
                    cell1.setCellStyle(leftLockCellStyle);
                    cell1.setCellValue(data.getItemCode());
                    // 科目名称
                    XSSFCell cell2 = row.createCell(cellNo++);
                    cell2.setCellStyle(leftLockCellStyle);
                    cell2.setCellValue(data.getItemName());
                    for (FexExposureVO exposure:dataMap.get(curData)) {
                        BigDecimal collectMoney = BigDecimal.ZERO;
                        for (FexExposureDetailVO dtl:exposure.getListDtl()) {
                            if (dtl.getItemCode().equals(data.getItemCode())) {
                                XSSFCell cellPeriod = row.createCell(cellNo++);
                                cellPeriod.setCellStyle(rightLockCellStyle);
                                cellPeriod.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(dtl.getFillMoney()) ? BigDecimal.ZERO : dtl.getFillMoney()));
                                if (StringUtils.isBlank(dtl.getSuperItemCode())) {
                                    collectMoney = collectMoney.add(dtl.getFillMoney().multiply(new BigDecimal(dtl.getSign())));
                                }
                            }
                        }
                    }
                    index++;
                }
                //汇总计算
                XSSFRow collectRow = sheet.createRow(index++);
                XSSFCell cellCollect0 = collectRow.createCell(0);
                cellCollect0.setCellStyle(centerLockCellStyle);
                XSSFCell cellCollect1 = collectRow.createCell(1);
                cellCollect1.setCellStyle(centerLockCellStyle);
                XSSFCell cellCollect2 = collectRow.createCell(2);
                cellCollect2.setCellStyle(centerLockCellStyle);
                cellCollect2.setCellValue("汇总");
                int cellNo = 2;
                for (FexExposureVO exposure:dataMap.get(curData)) {
                    cellNo++;
                    XSSFCell cellCollectMoney = collectRow.createCell(cellNo);
                    cellCollectMoney.setCellValue(headList.get(cellNo));
                    cellCollectMoney.setCellStyle(rightLockCellStyle);
                    BigDecimal collectMoney = BigDecimal.ZERO;
                    for (FexExposureDetailVO dtl:exposure.getListDtl()) {
                        if (StringUtils.isBlank(dtl.getSuperItemCode())) {
                            collectMoney = collectMoney.add(dtl.getFillMoney().multiply(new BigDecimal(dtl.getSign())));
                        }
                    }
                    cellCollectMoney.setCellValue(NumberUtil.formatNumeric(collectMoney));
                }
            }
            fout = new FileOutputStream(file);
            workbook.write(fout);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 组装敞口主信息
     *
     * @param workbook
     * @param sheet
     * @param main
     * @throws Exception
     */
    public void setExposureMain(XSSFWorkbook workbook, XSSFSheet sheet, FexExposureMainVO main) throws Exception {
        XSSFCellStyle leftLockCellStyle = this.fexExposureDownloadAndImportService.getLeftCellStyle(workbook, true);
        XSSFCellStyle rightLockCellStyle = this.fexExposureDownloadAndImportService.getRightCellStyle(workbook, true);
        XSSFRow row0 = sheet.createRow(0);
        // 单位
        XSSFCell cellCorp = row0.createCell(0);
        cellCorp.setCellStyle(rightLockCellStyle);
        cellCorp.setCellValue("单位");
        XSSFCell cellCorpName = row0.createCell(1);
        cellCorpName.setCellStyle(leftLockCellStyle);
        cellCorpName.setCellValue(main.getCorpName());
        // 模板名称
        XSSFCell cellTemplate = row0.createCell(2);
        cellTemplate.setCellStyle(rightLockCellStyle);
        cellTemplate.setCellValue("模板名称");
        XSSFCell cellTemplateName = row0.createCell(3);
        cellTemplateName.setCellStyle(leftLockCellStyle);
        cellTemplateName.setCellValue(main.getTemplateName());

        XSSFRow row1 = sheet.createRow(1);
        // 总填报周期
        XSSFCell cellTotalFill = row1.createCell(0);
        cellTotalFill.setCellStyle(rightLockCellStyle);
        cellTotalFill.setCellValue("总填报周期");
        XSSFCell cellTotalFillPeriod = row1.createCell(1);
        // 文本格式写入「周期+单位」，避免 Excel 把「12月」识别成日期后丢掉单位
        setPeriodWithUnitCell(workbook, cellTotalFillPeriod, leftLockCellStyle, main.getTotalFillPeriod(), main.getTotalFillUnit());
        // 前面按周期填报，后续是否按年度填报 0-否 1-是
        XSSFCell cellIsYearFill = row1.createCell(2);
        cellIsYearFill.setCellStyle(rightLockCellStyle);
        cellIsYearFill.setCellValue("前面按周期填报，后续是否按年度填报");
        XSSFCell cellYearFillName = row1.createCell(3);
        cellYearFillName.setCellStyle(leftLockCellStyle);
        cellYearFillName.setCellValue(dictService.get("sf_yn",main.getIsYearFill()));

        XSSFRow row2 = sheet.createRow(2);
        // 滚动总周期
        XSSFCell cellScrollFill = row2.createCell(0);
        cellScrollFill.setCellStyle(rightLockCellStyle);
        cellScrollFill.setCellValue("滚动总周期");
        XSSFCell cellScrollFillPeriod = row2.createCell(1);
        setPeriodWithUnitCell(workbook, cellScrollFillPeriod, leftLockCellStyle, main.getScrollFillPeriod(), main.getScrollFillUnit());
        // 前面按周期填报，后续是否按总额填报
        XSSFCell cellIsTotalFill = row2.createCell(2);
        cellIsTotalFill.setCellStyle(rightLockCellStyle);
        cellIsTotalFill.setCellValue("前面按周期填报，后续是否按总额填报");
        XSSFCell cellTotalFillName = row2.createCell(3);
        cellTotalFillName.setCellStyle(leftLockCellStyle);
        cellTotalFillName.setCellValue(dictService.get("sf_yn",main.getIsTotalFill()));

        XSSFRow row3 = sheet.createRow(3);
        // 滚动分解子周期
        XSSFCell cellScrollResolve = row3.createCell(0);
        cellScrollResolve.setCellStyle(rightLockCellStyle);
        cellScrollResolve.setCellValue("滚动分解子周期");
        XSSFCell cellScrollResolvePeriod = row3.createCell(1);
        setPeriodWithUnitCell(workbook, cellScrollResolvePeriod, leftLockCellStyle, main.getScrollResolvePeriod(), main.getScrollResolveUnit());
        // 币别
        XSSFCell cellCur = row3.createCell(2);
        cellCur.setCellStyle(rightLockCellStyle);
        cellCur.setCellValue("币别");
        XSSFCell cellCurName = row3.createCell(3);
        cellCurName.setCellStyle(leftLockCellStyle);
        cellCurName.setCellValue(main.getEnglishCode());
    }

    /**
     * 将填报周期和单位写入单元格，强制为文本，避免 Excel 把「12月」当成日期。
     */
    private void setPeriodWithUnitCell(XSSFWorkbook workbook, XSSFCell cell, XSSFCellStyle baseStyle,
                                       Integer period, String unitCode) {
        XSSFCellStyle textStyle = workbook.createCellStyle();
        textStyle.cloneStyleFrom(baseStyle);
        textStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));
        cell.setCellStyle(textStyle);
        String unitName = "";
        if (dictService != null && StringUtils.isNotBlank(unitCode)) {
            unitName = dictService.get("fexExposureTemplate.timeType", unitCode);
            if (StringUtils.isBlank(unitName)) {
                unitName = unitCode;
            }
        }
        String value = (period == null ? "" : String.valueOf(period)) + unitName;
        cell.setCellValue(value);
    }

    /**
     * 根据子单获取上报的周期集合
     * @param childMainIds
     * @return
     */
    public List<FexExposureVO> getPeriodByChildCorp(List<String> childMainIds) {
        return fexExposureDao.getPeriodByChildCorp(childMainIds);
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {

    }
}
