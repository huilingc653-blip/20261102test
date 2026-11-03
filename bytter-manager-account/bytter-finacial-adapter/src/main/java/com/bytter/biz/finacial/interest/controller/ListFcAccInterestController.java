package com.bytter.biz.finacial.interest.controller;
import com.bytter.biz.finacial.interest.service.ListFcAccInterestService;
import com.bytter.biz.finacial.interest.vo.FcInterestPrintVO;
import com.bytter.biz.finacial.interest.vo.ListFcAccInterestVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcSubjectService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
@Controller
@RequestMapping("/finacial/interest/listFcAccInterest")
public class ListFcAccInterestController extends BaseController {
    @Autowired
    ListFcAccInterestService listFcAccInterestService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFcSubjectService fcSubjectService;
    /**
     * 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list() {
        return "finacial/interest/listFcAccFor";
    }
    /**
     * 打印页面
     *
     * @param id
     * @param model
     * @return
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_FCINTERESTDTL_PRINT)
    @LogMessage(content = "打印页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/print")
    public String print(String id, Model model) {
        Map<String, FcInterestPrintVO> map = new LinkedHashMap<>();
        List<ListFcAccInterestVO> fcAccVO = listFcAccInterestService.getFcInterestDtl(id);
        setDate(fcAccVO,map);
        model.addAttribute("FcAccs", fcAccVO);
        model.addAttribute("map", map);
//        if (ListUtils.isNotNullOrEmpty(fcAccVO)) {
//            model.addAttribute("net", corpService.getNetByCorpId(fcAccVO.get(0).getNetId()));
//            model.addAttribute("subject", fcSubjectService.findSubjectById(fcAccVO.get(0).getSubjectId()));
//            model.addAttribute("acc",fcAccVO.get(0));
//        }
        model.addAttribute("Date", DateUtils.formatDate(new Date()));
        model.addAttribute("createTableUserName", getCurrentUser().getUsername());
        model.addAttribute("size",map.size());
        return "finacial/interest/listFcAccPrint";
    }
    /**
     * 批量打印页面
     * @param request
     * @param model
     * @return
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_FCINTERESTDTL_PRINT)
    @LogMessage(content = "批量打印页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/batchPrint")
    public String batchprint(HttpServletRequest request, Model model) {
        Map<String, FcInterestPrintVO> map = new LinkedHashMap<>();
        //获得对象集合
        List<ListFcAccInterestVO> fcAccVO = listFcAccInterestService.getFcAccVO(queryParams(request));
        setDate(fcAccVO,map);
        model.addAttribute("FcAccs", fcAccVO);
        model.addAttribute("map", map);
//        if (ListUtils.isNotNullOrEmpty(fcAccVO)) {
//            model.addAttribute("net", corpService.getNetByCorpId(fcAccVO.get(0).getNetId()));
//            model.addAttribute("subject", fcSubjectService.findSubjectById(fcAccVO.get(0).getSubjectId()));
//            model.addAttribute("acc",fcAccVO.get(0));
//        }
        model.addAttribute("createTableUserName", getCurrentUser().getUsername());
        model.addAttribute("Date", DateUtils.formatDate(new Date()));
        model.addAttribute("size",map.size());
        return "finacial/interest/listFcAccPrint";
    }
    /**
    * <br>
    * <b>功能：</b> 组装查询条件 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/3/13 0013 <br>
    */
    public Map<String, Object> queryParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        String likes_blnctrAcc = request.getParameter("likes_blnctrAcc");
        String eqs_netId = request.getParameter("eqs_netId");
        String ins_accType = request.getParameter("ins_accType");
        String ged_interestTimeStr = request.getParameter("ged_interestTime");
        if(EmptyUtils.isNotEmpty(ged_interestTimeStr)){
            Date ged_interestTime = DateUtils.parse(ged_interestTimeStr, DateUtils.PATTERN_CLASSICAL_SIMPLE);
            params.put("ged_interestTime", ged_interestTime);
        }
        String led_interestTimeStr = request.getParameter("led_interestTime");
        if(EmptyUtils.isNotEmpty(led_interestTimeStr)){
            Date led_interestTime = DateUtils.parse(led_interestTimeStr, DateUtils.PATTERN_CLASSICAL_SIMPLE);
            params.put("led_interestTime", led_interestTime);
        }
        String ged_interestStartDateStr = request.getParameter("ged_interestStartDate");
        if(EmptyUtils.isNotEmpty(ged_interestStartDateStr)){
            Date ged_interestStartDate = DateUtils.parse(ged_interestStartDateStr, DateUtils.PATTERN_CLASSICAL_SIMPLE);
            params.put("ged_interestStartDate", ged_interestStartDate);
        }
        String led_interestStartDateStr = request.getParameter("led_interestStartDate");
        if(EmptyUtils.isNotEmpty(led_interestStartDateStr)){
            Date led_interestStartDate = DateUtils.parse(led_interestStartDateStr, DateUtils.PATTERN_CLASSICAL_SIMPLE);
            params.put("led_interestStartDate", led_interestStartDate);
        }
        params.put("likes_blnctrAcc", likes_blnctrAcc);
        params.put("eqs_netId", eqs_netId);
        params.put("ins_accType", ins_accType);
        return params;
    }
    /**
     * 列表数据查询
     * @param dataTablesInput
     * @return
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_FCINTERESTDTL_PRINT)
    @LogMessage(content = "列表数据查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatableFor")
    @ResponseBody
    public DataTablesOutput findlistBySql(@RequestBody DataTablesInput dataTablesInput) {
        return listFcAccInterestService.findlistBySql(dataTablesInput);
    }
    /**
     * 设置显示值
     * @param fcAccVO
     * @param map
     */
    public void setDate(List<ListFcAccInterestVO> fcAccVO,Map<String, FcInterestPrintVO> map){
        for(ListFcAccInterestVO vo : fcAccVO){
            if(map.containsKey(vo.getOrderNo()+vo.getBlnctrAcc())){
                FcInterestPrintVO fcInterestPrintVO = map.get(vo.getOrderNo()+vo.getBlnctrAcc());
                List<ListFcAccInterestVO> list = fcInterestPrintVO.getListFcAccInterestVOList();
                list.add(vo);
                fcInterestPrintVO.setInterestSum(fcInterestPrintVO.getInterestSum().add(vo.getInterest()));
                map.put(vo.getOrderNo()+vo.getBlnctrAcc(),fcInterestPrintVO);
            }else{
                FcInterestPrintVO fcInterestPrintVO = new FcInterestPrintVO();
                List<ListFcAccInterestVO> list = new ArrayList<>();
                list.add(vo);
                fcInterestPrintVO.setListFcAccInterestVOList(list);
                fcInterestPrintVO.setNetCorp(corpService.getNetByCorpId(fcAccVO.get(0).getNetId()));
                fcInterestPrintVO.setFcSubjectDTO(fcSubjectService.findSubjectById(vo.getSubjectId()));
                fcInterestPrintVO.setListFcAccInterestVO(vo);
                fcInterestPrintVO.setInterestSum(vo.getInterest());
                map.put(vo.getOrderNo()+vo.getBlnctrAcc(),fcInterestPrintVO);
            }
        }
    }
}
