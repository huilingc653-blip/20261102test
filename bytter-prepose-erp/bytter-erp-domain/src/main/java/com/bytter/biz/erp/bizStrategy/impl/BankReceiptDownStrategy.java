package com.bytter.biz.erp.bizStrategy.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.biz.erp.bizStrategy.BizStrategy;
import com.bytter.biz.erp.entity.ErpBankReceiptImgView;
import com.bytter.biz.erp.service.ErpBankReceiptViewService;
import com.bytter.biz.erp.utils.FileDownloadUtil;
import com.bytter.biz.erp.vo.ErpBankReceiptVo;
import com.bytter.biz.erp.vo.request.BankReceiptDownReqVo;
import com.bytter.erp.common.constant.ErpConstant;
import com.bytter.erp.common.constant.ServiceCodeEnum;
import com.bytter.system.api.IBillFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电子回单文件下载策略实现
 *
 * @author fangcf
 * @date 2026-05-12
 */
@Slf4j
@Component
public class BankReceiptDownStrategy implements BizStrategy {

    @Autowired
    private ErpBankReceiptViewService erpBankReceiptViewService;

    @DubboReference(check = false)
    private IBillFileService billFileService;

    @Override
    public String getServiceCode() {
        return ServiceCodeEnum.BIS_BANK_RECEIPT_DOWN.getServiceCode(); // 服务代码
    }

    @Override
    public Map<String, String> doBiz(Map<String, String> params) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_SUCCESS_STATUS);

        // 请求原文
        String data = params.get(ErpConstant.DO_BIZ_DATA);
        String requestNo = params.get(ErpConstant.DO_BIZ_SERIAL_NO);
        String tenantId = params.get(ErpConstant.DO_BIZ_TENANT_ID);

        BankReceiptDownReqVo reqVo = safeParseJson(data, BankReceiptDownReqVo.class);
        String checkResult = checkData(reqVo);
        if (ObjectUtil.isNotEmpty(checkResult)) {
            map.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_FAIL_STATUS);
            map.put(ErpConstant.DO_BIZ_MSG, checkResult);
            return map;
        }

        // 查询明细
        List<ErpBankReceiptImgView> list = erpBankReceiptViewService.listBankReceiptImgView(tenantId, reqVo.getSerialId());
        if (ObjectUtil.isNotEmpty(list)) {
            List<ErpBankReceiptVo> resultList = new ArrayList<>();

            for (ErpBankReceiptImgView op : list) {
                ErpBankReceiptVo vo = new ErpBankReceiptVo();
                vo.setSerialId(op.getSerialId());
                vo.setFileType(op.getFileType());
                byte[] fileBytes;
                try {
                    fileBytes = FileDownloadUtil.downloadFile(op, billFileService);
                    vo.setEbillimg(fileBytes);
                    resultList.add(vo);
                } catch (Exception e) {
                    log.error("流水号：{} 下载电子回单：{} 异常：", requestNo, reqVo.getSerialId(), e);
                }
            }

            map.put(ErpConstant.DO_BIZ_DATA, JSONUtil.toJsonStr(resultList));
            map.put(ErpConstant.DO_BIZ_MSG, "查询成功");
        } else {
            map.put(ErpConstant.DO_BIZ_MSG, "查无数据");
        }

        return map;
    }

    // 校验数据
    private String checkData(BankReceiptDownReqVo reqVo) {
        StringBuilder result = new StringBuilder(128);
        // 先判断报文是否正确
        if (ObjectUtil.isEmpty(reqVo)) {
            appendMsg(result, "请求报文格式不正确，解析失败！");
            return result.toString();
        }
        // 银行账号
        if (ObjectUtil.isEmpty(reqVo.getSerialId()) || ObjectUtil.isEmpty(reqVo.getSerialId().trim())) {
            appendMsg(result, "流水号不能为空");
        }
        return result.toString();
    }

}
