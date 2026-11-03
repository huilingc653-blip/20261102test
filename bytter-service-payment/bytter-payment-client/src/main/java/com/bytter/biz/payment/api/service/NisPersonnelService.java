package com.bytter.biz.payment.api.service;

/**
 * 职工信息维护
 */
public interface NisPersonnelService {
    boolean isRepeatWorkNo(String id, String workNo);

    boolean isRepeatIdcard(String id, String idcard);

    int getPersonnelCountByCorpId(String corpId);
}
