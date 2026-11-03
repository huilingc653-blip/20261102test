package com.bytter.biz.finacial.interest.controller;
import com.bytter.biz.finacial.interest.service.FcInterestSingleNodBookingService;
import com.bytter.biz.finacial.interest.service.FcInterestSingleNodService;
import com.bytter.biz.finacial.interest.vo.FcInterestSingleNodBookVO;
import com.bytter.biz.finacial.interest.vo.NoticeAccountVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 企业通知存款个别计息
 *
 * @author zuoy 2020-12-21
 */
@Controller
@RequestMapping("/finacial/interest/single/nod")
public class FcInterestSingleNodController extends BaseController {
    @Autowired
    private FcInterestSingleNodBookingService fcInterestSingleNodBookingService;
    @Autowired
    private FcInterestSingleNodService fcInterestSingleNodService;
    /**
     * 跳转企业通知存款个别计息-通知预约页面
     *
     * @param model
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_BOOKING_APPLY)
    @GetMapping("/booking")
    public String booking(Model model) {
        return "finacial/interest/single/nod/listInterestSingleNodBooking";
    }
    /**
     * 加载企业通知存款个别计息-通知预约列表数据
     *
     * @param dataTablesInput
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_BOOKING_QUERY)
    @PostMapping("/dataTableBooking")
    @ResponseBody
    public DataTablesOutput dataTableBooking(@RequestBody DataTablesInput dataTablesInput) {
        return fcInterestSingleNodBookingService.dataTableBooking(dataTablesInput, getCurrentUser());
    }
    /**
     * 跳转通知预约新增页面
     *
     * @param model
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_BOOKING_APPLY)
    @GetMapping("/addBooking")
    public String addBooking(Model model) {
        return "finacial/interest/single/nod/addInterestSingleNodBooking";
    }
    /**
     * 跳转通知预约编辑页面
     *
     * @param vo
     * @param model
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_BOOKING_APPLY)
    @GetMapping("/updateBooking")
    public String updateBooking(FcInterestSingleNodBookVO vo, Model model) {
        model.addAttribute("interestSingleNodBookVO", fcInterestSingleNodBookingService.getByUnion(vo.getId()));
        return "finacial/interest/single/nod/updateInterestSingleNodBooking";
    }
    /**
     * 跳转通知预约查看页面
     *
     * @param id
     * @param model
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_BOOKING_QUERY)
    @GetMapping("/viewBooking/{id}")
    public String viewBooking(@PathVariable("id") String id, Model model) {
        model.addAttribute("interestSingleNodBookVO", fcInterestSingleNodBookingService.getByUnion(id));
        return "finacial/interest/single/nod/viewInterestSingleNodBooking";
    }
    /**
     * 通知预约新增保存
     *
     * @param vo
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_BOOKING_APPLY)
    @PostMapping("/saveBooking")
    @ResponseBody
    public AjaxJson saveBooking(FcInterestSingleNodBookVO vo) {
        fcInterestSingleNodBookingService.saveInterestSingleNodBook(vo, this.getCurrentUser());
        return success();
    }
    /**
     * 通知预约编辑保存
     *
     * @param vo
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_BOOKING_APPLY)
    @PostMapping("/modifyBooking")
    @ResponseBody
    public AjaxJson modifyBooking(FcInterestSingleNodBookVO vo) {
        fcInterestSingleNodBookingService.modifyInterestSingleNodBook(vo, this.getCurrentUser());
        return success();
    }
    /**
     * 删除通知预约
     *
     * @param id
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_BOOKING_APPLY)
    @PostMapping("/deleteBooking/{id}")
    @ResponseBody
    public AjaxJson deleteBooking(@PathVariable("id") String id) {
        fcInterestSingleNodBookingService.deleteBooking(id);
        return success();
    }
    /**
     * 取消通知预约
     *
     * @param id
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_INTEREST_SINGLE_NOD_BOOKING_UN_BOOKING)
    @PostMapping("/unBooking/{id}")
    @ResponseBody
    public AjaxJson unBooking(@PathVariable("id") String id) {
        fcInterestSingleNodBookingService.unBooking(id);
        return success();
    }
    /**
     * 获取通知存款账号
     *
     * @param corpId
     * @param curId
     * @return
     */
    @PostMapping("/getNoticeAccount")
    @ResponseBody
    public List<NoticeAccountVO> getNoticeAccount(String corpId, String curId) {
        return fcInterestSingleNodBookingService.getNoticeAccount(corpId, curId);
    }
    /**
     *
     * @param account   内部记账
     * @param id   编辑存在id
     * @return
     */
    @GetMapping("checkAccount")
    @ResponseBody
    public AjaxJson checkAccount(String account,String id) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(fcInterestSingleNodBookingService.checkAccount(account,id));
        return j;
    }
}
