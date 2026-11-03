package com.bytter.biz.payment.api.service;

/**
 * 付款单-供应链金融债权凭证关联表（nis_scf_info）接口
 * <p>
 * 供 bytter-prepose-bill 等模块通过 dubbo 调用，按付款单ID+凭证类型查询关联的债权凭证ID集合。
 *
 * @author ztd
 * @since 2026/8/24
 */
public interface INisScfInfoService {

    /**
     * 根据付款单ID和凭证类型获取关联的债权凭证ID集合（逗号分隔）
     *
     * @param payId       付款单ID
     * @param voucherType 凭证类型（1=应付债权凭证，2=应收债权凭证）
     * @return 凭证ID集合，无关联返回空串
     */
    String getVoucherIds(String payId, String voucherType);

    /**
     * 根据付款单ID和凭证类型获取关联的转让草稿ID集合（逗号分隔）
     *
     * @param payId       付款单ID
     * @param voucherType 凭证类型（1=应付债权凭证，2=应收债权凭证）
     * @return 转让草稿ID集合，无关联返回空串
     */
    String getTransferIds(String payId, String voucherType);
}
