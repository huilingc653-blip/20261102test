package com.bytter.increment.web.controller;
import com.bytter.increment.domain.dao.ChoProductRedeemDao;
import com.bytter.increment.domain.entity.ChoMessage;
import com.bytter.increment.domain.entity.ChoProductRedeem;
import com.bytter.increment.domain.service.ChoMessageService;
import com.bytter.increment.domain.service.ChoProductRedeemService;
import com.bytter.increment.domain.service.ChoProductReqService;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 理财日志表
 * @date 2017-09-23 15:20:33
 */
@Controller
@RequestMapping("/chowmatistic/chomessage")
public class ChoMessageController extends BaseController {
    @Autowired
    private ChoMessageService choMessageService;
    @Autowired
    private ChoProductReqService choProductReqService;
    @Autowired
    private ChoProductRedeemService choProductRedeemService;
    @Autowired
    private ChoProductRedeemDao choProductRedeemDao;
    /**
     * 日志查询
     *
     * @param billNo
     * @param busType
     * @param model
     * @return
     */
    @GetMapping("/list/{billNo}/{busType}")
    public String list(@PathVariable("billNo") String billNo, @PathVariable("busType") String busType, Model model) {
        List<ChoMessage> listNisMessage = choMessageService.listMessage(billNo, busType);
        List<ChoMessage> list = new ArrayList<>();
        if (busType.equals(ChoMessageConstant.BUS_TYPE_PRODUCT_REQ)) {
            ChoProductReqVo choProductReqVo = choProductReqService.getChoProductReqVoById(billNo);
            if (choProductReqVo != null) {
                list = choMessageService.listMessage(choProductReqVo.getBillNo(), busType);
            }
            List<ChoProductRedeem> productRedeems = choProductRedeemDao.listProductRedeemedByReqId(billNo);
            if (!EmptyUtils.isEmpty(productRedeems)) {
                for (int i = 0; i < productRedeems.size(); i++) {
                    List<ChoMessage> billNoList1 = choMessageService.listMessage(productRedeems.get(i).getBillNo(), ChoMessageConstant.BUS_TYPE_PRODUCT_REDEEM);
                    List<ChoMessage> billNoList2 = choMessageService.listMessage(productRedeems.get(i).getId(), ChoMessageConstant.BUS_TYPE_PRODUCT_REDEEM);
                    for (int j = 0; j < billNoList1.size(); j++) {
                        listNisMessage.add(billNoList1.get(j));
                    }
                    for (int k = 0; k < billNoList2.size(); k++) {
                        listNisMessage.add(billNoList2.get(k));
                    }
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            listNisMessage.add(list.get(i));
        }
        Collections.sort(listNisMessage, new Comparator<ChoMessage>() {
            @Override
            public int compare(ChoMessage msg1, ChoMessage msg2) {
                return msg2.getEnrolDate().compareTo(msg1.getEnrolDate());
            }
        });
        model.addAttribute("list", listNisMessage);
        return "chowmatistic/listChoMessage";
    }
}
