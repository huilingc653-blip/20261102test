//package com.bytter.biz.netbank.service.impl;
//
//import cn.hutool.core.util.ObjectUtil;
//import com.alibaba.fastjson.JSONArray;
//import com.bytter.biz.netbank.dao.NisBillheadDao;
//import com.bytter.biz.netbank.entity.NisBillhead;
//import com.bytter.biz.netbank.service.INisBillheadTCCService;
//import com.bytter.biz.netbank.service.NisBillheadService;
//import com.bytter.biz.netbank.vo.NisBillheadVO;
//import com.bytter.biz.payment.api.constant.NetbankConstant;
//import com.bytter.core.base.entity.ICurrentUser;
//import io.seata.rm.tcc.api.BusinessActionContext;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//
///**
// * create by LuoYJ on 2026/2/5.
// */
//@Slf4j
//@Service
//public class INisBillheadTCCServiceImpl implements INisBillheadTCCService {
//
//    @Autowired
//    private NisBillheadService nisBillheadService;
//    @Autowired
//    private NisBillheadDao nisBillheadDao;
//
//    /**
//     * TCC：自动审批
//     */
//    @Override
//    public String prepareAuto(String id, String sysVoucherCode, NisBillheadVO nisBillheadVO, ICurrentUser user) {
//        log.info("INisBillheadTCCServiceImpl-prepare-save");
//        return nisBillheadService.saveNisBillhead2(id, nisBillheadVO, user);
//    }
//
//    @Override
//    public boolean commitAuto(BusinessActionContext actionContext) {
//        log.info("INisBillheadTCCServiceImpl-commitAuto");
//        //确认付款制单状态--审批通过
////        NisBillheadVO vo = actionContext.getActionContext("vo", NisBillheadVO.class);
////        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK, NetbankConstant.STATUE_WAIT_CHECK, NetbankConstant.STATUE_TALLY_CHECKPASS};
////        nisBillheadDao.updateNisBillheadStatus(vo,oldStatus );
//        return true;
//    }
//
//    @Override
//    public boolean rollbackAuto(BusinessActionContext actionContext) {
//        log.info("INisBillheadTCCServiceImpl-rollbackAuto");
//        // 删除制单
//        String id = actionContext.getActionContext("id", String.class);
//        nisBillheadDao.delete(id);
//        return true;
//    }
//
//}
