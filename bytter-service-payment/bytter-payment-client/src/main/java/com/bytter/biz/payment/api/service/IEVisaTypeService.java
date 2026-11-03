package com.bytter.biz.payment.api.service;
/**
 * qijh
 */
public interface IEVisaTypeService {
    /**
     * 获取签章图片附件id
     * @param billType 内部电子回单类型
     * @param netId 网点id
     * @return
     */
    String getAttachmentId(String billType, String netId);
}
