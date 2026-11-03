package com.bytter.biz.transfer.swap.controller;
import com.bytter.biz.transfer.swap.service.ErpRPABankService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
/**
 * 网银RPA接口
 */
@Controller
@RequestMapping("/erp/swap/rpa")
public class ErpRPABankController extends BaseController {
    @Autowired
    private ErpRPABankService erpRPABankService;
    /**
     * 获取查询明细的查询条件
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/fetchDetailInfo")
    @ResponseBody
    public Object fetchDetailInfo() throws Exception {
        Map<String, Object> map = erpRPABankService.fetchDetailInfo();
        return map;
    }
    /**
     * 根据银行明细插入历史明细表
     *
     * @param request
     * @throws Exception
     */
    @PostMapping("/receiveDetailData")
    @ResponseBody
    public void receiveDetailData(HttpServletRequest request) throws Exception {
        String jsonParams = "";
        int contentLength = request.getContentLength();
        if (contentLength > 0) {
            byte buffer[] = new byte[contentLength];
            for (int i = 0; i < contentLength; ) {
                int len = request.getInputStream().read(buffer, i, contentLength - i);
                if (len == -1) {
                    break;
                }
                i += len;
            }
            jsonParams = new String(buffer, "utf-8");
        }
        List<Map<String, Object>> mapListJson = JsonUtils.parseObject(jsonParams, List.class);
        erpRPABankService.toSaveBankDtl(mapListJson);
    }
    /**
     * RPA 银行账号余额条件接口
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/fetchBalanceInfo")
    @ResponseBody
    public Object fetchBalanceInfo() throws Exception {
        Map<String, Object> map = erpRPABankService.fetchBalanceInfo();
        return map;
    }
    /**
     * 根据银行余额插入历史余额表
     *
     * @param request
     * @throws Exception
     */
    @PostMapping("/receiveBalanceData")
    @ResponseBody
    public void receiveBalanceData(HttpServletRequest request) throws Exception {
        String jsonParams = "";
        int contentLength = request.getContentLength();
        if (contentLength > 0) {
            byte buffer[] = new byte[contentLength];
            for (int i = 0; i < contentLength; ) {
                int len = request.getInputStream().read(buffer, i, contentLength - i);
                if (len == -1) {
                    break;
                }
                i += len;
            }
            jsonParams = new String(buffer, "utf-8");
        }
        List<Map<String, Object>> mapListJson = JsonUtils.parseObject(jsonParams, List.class);
        erpRPABankService.toSaveBankBal(mapListJson);
    }
    @GetMapping("/fetchBillInfo")
    @ResponseBody
    public Object fetchBillInfo() throws Exception {
        Map<String, Object> map = erpRPABankService.fetchBillInfo();
        return map;
    }
    /**
     * 插入银行电子回单
     *
     * @param request
     * @throws Exception
     */
    @PostMapping("/receiveBillData")
    @ResponseBody
    public void receiveBillData(HttpServletRequest request) throws Exception {
        String jsonParams = "";
        int contentLength = request.getContentLength();
        if (contentLength > 0) {
            byte buffer[] = new byte[contentLength];
            for (int i = 0; i < contentLength; ) {
                int len = request.getInputStream().read(buffer, i, contentLength - i);
                if (len == -1) {
                    break;
                }
                i += len;
            }
            jsonParams = new String(buffer, "utf-8");
        }
        List<Map<String, Object>> mapListJson = JsonUtils.parseObject(jsonParams, List.class);
        erpRPABankService.toSaveBankBill(mapListJson);
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_RPA_VIEW)
    @GetMapping("/list")
    public String list(Model model) {
        erpRPABankService.setModel(model);
        return "dataexchange/netbank/listRPABank";
    }
    @PostMapping("/toSave")
    @ResponseBody
    public AjaxJson toSave(HttpServletRequest request) throws Exception {
        String type = request.getParameter("type");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String bankType = request.getParameter("bankType");
        erpRPABankService.toSearchBank(type, bankType, startDate, endDate);
        return success();
    }
}
