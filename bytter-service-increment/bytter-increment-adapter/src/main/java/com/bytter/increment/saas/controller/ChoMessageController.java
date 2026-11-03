package com.bytter.increment.saas.controller;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.domain.dao.ChoProductRedeemDao;
import com.bytter.increment.domain.entity.ChoMessage;
import com.bytter.increment.domain.entity.ChoProductRedeem;
import com.bytter.increment.domain.service.ChoMessageService;
import com.bytter.increment.domain.service.ChoProductRedeemService;
import com.bytter.increment.domain.service.ChoProductReqService;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.increment.domain.vo.v1.ChoMessageVO;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 理财日志表
 * @date 2017-09-23 15:20:33
 */
@Api(value = "/v1-increment/chowmatistic/chomessage",tags = "理财日志" )
@RestController("v1ChoMessageController")
@RequestMapping("/v1-increment/chowmatistic/chomessage")
public class ChoMessageController extends BaseController {
    @Autowired
    private ChoMessageService choMessageService;
    @Autowired
    private ChoProductReqService choProductReqService;
    @Autowired
    private ChoProductRedeemService choProductRedeemService;
    @Autowired
    private ChoProductRedeemDao choProductRedeemDao;
    @DubboReference(check = false)
    private IDictService dictService;
    /**
     * 日志查询
     *
     * @param billNo
     * @param busType
     * @param model
     * @return
     */
    @ApiOperation(value = "日志查询")
    @GetMapping("/list/{billNo}/{busType}")
    public AjaxJson<List<ChoMessageVO>> list(@PathVariable("billNo") String billNo, @PathVariable("busType") String busType, Model model) {
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
        Map<String, DictDTO> dictMap = dictService.listMap("choMessage.messageType");

        for (int i = 0; i < list.size(); i++) {
            listNisMessage.add(list.get(i));
        }
        Collections.sort(listNisMessage, new Comparator<ChoMessage>() {
            @Override
            public int compare(ChoMessage msg1, ChoMessage msg2) {
                return msg2.getEnrolDate().compareTo(msg1.getEnrolDate());
            }
        });
        List<ChoMessageVO> resultList = new ArrayList<>();
        listNisMessage.stream().forEach(message -> {
            ChoMessageVO choMessageVO = new ChoMessageVO();
            BeanUtils.copyProperties(message, choMessageVO);
            if (ObjectUtil.isNotEmpty(message.getMessageType())) {
                Optional.ofNullable(dictMap.get(message.getMessageType().toString()))
                        .ifPresent(dict -> choMessageVO.setMessageTypeName(dict.getName()));
            }
            resultList.add(choMessageVO);
        });
        return success(resultList);
    }
}
