package com.bytter.bill.domain.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bytter.bill.api.erp.service.IErpAbEmitBillPayService;
import com.bytter.bill.domain.bill.bean.DSLAbEmitBillPayBean;
import com.bytter.bill.domain.bill.bean.DSLAbEmitBillPayResponseBean;
import com.bytter.bill.domain.bill.entity.DSLInterfaceLogBill;
import com.bytter.bill.domain.bill.service.DSLInterfaceLogBillService;
import com.bytter.bill.domain.bill.service.ErpAbEmitBillPayService;
import com.bytter.bill.domain.bill.vo.AbEmitBillPayVo;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.StringUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lijinquan
 * @description:
 * @date 2024-09-03 17:58
 */
@Service("erpAbEmitBillPayServiceImpl")
@DubboService
public class ErpAbEmitBillPayServiceImpl implements IErpAbEmitBillPayService {

    @Autowired
    private ErpAbEmitBillPayService erpAbEmitBillPayService;
    @Autowired
    private DSLInterfaceLogBillService dslInterfaceLogBillService;
    

    @Override
    public String abEmitBillPayData(String jsonData) {
        String message = null;
        String status = "1"; // 2成功,1失败
        List<DSLAbEmitBillPayBean> list = new ArrayList<DSLAbEmitBillPayBean>();
        List<DSLAbEmitBillPayResponseBean> responseBeanList = new ArrayList<DSLAbEmitBillPayResponseBean>();
        System.out.println("应付票据兑付信息同步开始...");
        System.out.println(String.format("接收报文为:%s", jsonData));
        try {
            list = JSON.parseArray(jsonData, DSLAbEmitBillPayBean.class);
        } catch (Exception e) {
            System.out.println(String.format("数据解析失败:%s", e));
            return "解析报文失败";
        }
        if (list != null && list.size() > 0) {
            System.out.println("本次一共接收【" + list.size() + "】条兑付数据");

            List<String> origNos = new ArrayList<>();
            for (DSLAbEmitBillPayBean bean : list) {
                origNos.add(bean.getReceiptNum());
            }

            List<String> origNoList = null;
            Map<String, AbEmitBillPayVo> billPayMap = null;
            try {
                //获取已解付票据
                origNoList = erpAbEmitBillPayService.billPayDataTable(origNos);
                //获取未解付票据
                billPayMap = erpAbEmitBillPayService.getBillPayMap(origNos);
            } catch (Exception e) {
                e.printStackTrace();
                return "获取已解付和未解付票据发生异常，详情情况日志！";
            }

            for (DSLAbEmitBillPayBean dslAbEmitBillPayBean : list) {
                DSLAbEmitBillPayResponseBean responseBean = new DSLAbEmitBillPayResponseBean();
                Boolean isSuccess = true;
                DSLInterfaceLogBill dslLog = new DSLInterfaceLogBill(dslAbEmitBillPayBean.getReceiptNum(),"DSL_BILL_PAY_WEB","大参林应付票据兑付信息同步",new Date());
                dslLog.info("接收报文为：" + jsonData,true);
                dslLog.info("开始处理单据RECEIPT_NUM("+dslAbEmitBillPayBean.getReceiptNum()+")>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", true);

                try {
                    erpAbEmitBillPayService.handleErpBillPay(responseBean,dslAbEmitBillPayBean,origNoList,billPayMap,dslLog);
                } catch (BusinessException e) {
                    isSuccess = false;
                    message = e.getMessage();
                    responseBean.setStatus(status);
                    responseBean.setMessage(message);
                    dslLog.printStackTrace(message, e,true);
                }catch (Exception e) {
                    isSuccess = false;
                    message = e.getMessage();
                    responseBean.setStatus(status);
                    responseBean.setMessage(message);
                    dslLog.printStackTrace(message, e,true);
                }
                responseBeanList.add(responseBean);
                dslLog.info("单据RECEIPT_NUM("+dslAbEmitBillPayBean.getReceiptNum()+")的返回报文为：【"+
                        JSON.toJSONString(responseBean, SerializerFeature.WriteMapNullValue) +"】",true);
                try {
                    dslInterfaceLogBillService.saveLog(dslLog, isSuccess);
                } catch (Exception e) {
                    System.out.println("(DSL_INTERFACE_LOG)日志保存失败");
                }
            }
        } else {
            if (StringUtil.isBlank_new(message)) {
                message = "本次未获取到解析数据;";
                System.out.println(message);
            }
            DSLAbEmitBillPayResponseBean responseBean = new DSLAbEmitBillPayResponseBean(null, status, message);
            responseBeanList.add(responseBean);
        }
        String responseJson = JSON.toJSONString(responseBeanList, SerializerFeature.WriteMapNullValue);
        System.out.println("返回信息为:" + responseJson);
        return responseJson;
    }

}
