package com.bytter.biz.netbank.service;

import com.bytter.biz.netbank.dao.NisScfInfoDao;
import com.bytter.biz.netbank.entity.NisScfInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: Service
 * @Description: 付款单-供应链金融债权凭证关联表（中间表 nis_scf_info）
 * @author zhukr
 * @since 2026/8/18
 */
@Service("nisScfInfoService")
@Transactional
public class NisScfInfoService extends BaseService<NisScfInfo, String> {

    @Autowired
    private NisScfInfoDao nisScfInfoDao;

    @Override
    public BaseDao<NisScfInfo, String> getEntityDao() {
        return nisScfInfoDao;
    }

    /**
     * 新增或更新（先删后插）：保存付款单关联的债权凭证ID
     *
     * @param payId       付款单ID
     * @param payNo       付款单号
     * @param voucherType 凭证类型（1=应付，2=应收）
     * @param voucherIds  凭证ID集合（逗号分隔）
     */
    public void saveOrUpdateSync(String payId, String payNo, String voucherType, String voucherIds) {
        this.saveOrUpdateSync(payId, payNo, voucherType, voucherIds, null, null, null);
    }

    /**
     * 新增或更新（先删后插）：保存付款单关联的债权凭证ID与供应链金融平台/融资产品上下文
     *
     * @param payId       付款单ID
     * @param payNo       付款单号
     * @param voucherType 凭证类型（1=应付，2=应收）
     * @param voucherIds  凭证ID集合（逗号分隔，可为空）
     * @param platformId  供应链金融平台ID
     * @param productId   融资产品ID
     */
    public void saveOrUpdateSync(String payId, String payNo, String voucherType, String voucherIds,
                                 String platformId, String productId) {
        this.saveOrUpdateSync(payId, payNo, voucherType, voucherIds, null, platformId, productId);
    }

    /**
     * 新增或更新（先删后插）：保存付款单关联的债权凭证ID与转让草稿ID、供应链金融平台/融资产品上下文
     * <p>voucherIds 与 transferIds 均按逗号分隔按序写入（52-应收：transfer_id 存转让草稿ID，
     * voucher_id 存派生凭证ID）；两者均为空时仅保存平台/产品上下文（51-未开立，暂存凭证审批通过后生成再补凭证ID），
     * 使 51-未开立 的 平台/产品 能穿过异步审批流。
     *
     * @param payId       付款单ID
     * @param payNo       付款单号
     * @param voucherType 凭证类型（1=应付，2=应收）
     * @param voucherIds  凭证ID集合（逗号分隔，可为空）
     * @param transferIds 转让草稿ID集合（逗号分隔，可为空）
     * @param platformId  供应链金融平台ID
     * @param productId   融资产品ID
     */
    public void saveOrUpdateSync(String payId, String payNo, String voucherType, String voucherIds,
                                 String transferIds, String platformId, String productId) {
        nisScfInfoDao.delByPayIdAndType(payId, voucherType);
        if (StringUtils.isBlank(voucherIds)) {
            //51-未开立：仅保存平台/产品上下文
            NisScfInfo ctx = new NisScfInfo();
            ctx.setPayId(payId);
            ctx.setPayNo(payNo);
            ctx.setVoucherType(voucherType);
            ctx.setPlatformId(platformId);
            ctx.setProductId(productId);
            nisScfInfoDao.saveOrUpdate(ctx);
            return;
        }
        String[] voucherIdAr = StringUtils.split(voucherIds, CommonConstant.COMMA_SPLIT);
        String[] transferIdAr = StringUtils.split(transferIds, CommonConstant.COMMA_SPLIT);
        if (voucherIdAr == null) {
            voucherIdAr = new String[0];
        }
        if (transferIdAr == null) {
            transferIdAr = new String[0];
        }
        int size = Math.max(voucherIdAr.length, transferIdAr.length);
        for (int i = 0; i < size; i++) {
            NisScfInfo nisScfInfo = new NisScfInfo();
            nisScfInfo.setPayId(payId);
            nisScfInfo.setPayNo(payNo);
            nisScfInfo.setVoucherType(voucherType);
            if (i < voucherIdAr.length) {
                nisScfInfo.setVoucherId(voucherIdAr[i]);
            }
            if (i < transferIdAr.length) {
                nisScfInfo.setTransferId(transferIdAr[i]);
            }
            nisScfInfo.setPlatformId(platformId);
            nisScfInfo.setProductId(productId);
            nisScfInfoDao.saveOrUpdate(nisScfInfo);
        }
    }

    /**
     * 获取付款单指定凭证类型的关联上下文（51-未开立时读取平台/产品）
     *
     * @param payId       付款单ID
     * @param voucherType 凭证类型（1=应付，2=应收）
     * @return 关联信息，无则返回 null
     */
    public NisScfInfo getContext(String payId, String voucherType) {
        List<NisScfInfo> list = nisScfInfoDao.listByPayIdAndType(payId, voucherType);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 根据付款单ID和凭证类型获取凭证ID集合（逗号分隔）
     *
     * @param payId       付款单ID
     * @param voucherType 凭证类型（1=应付，2=应收）
     * @return 凭证ID集合
     */
    public String getVoucherIds(String payId, String voucherType) {
        List<NisScfInfo> list = nisScfInfoDao.listByPayIdAndType(payId, voucherType);
        StringBuilder ids = new StringBuilder();
        if (!EmptyUtils.isEmptyList(list)) {
            for (NisScfInfo nisScfInfo : list) {
                if (StringUtils.isEmpty(nisScfInfo.getVoucherId())) {
                    continue;
                }
                if (EmptyUtils.isBlank(ids.toString())) {
                    ids.append(nisScfInfo.getVoucherId());
                } else {
                    ids.append(CommonConstant.COMMA_SPLIT).append(nisScfInfo.getVoucherId());
                }
            }
        }
        return ids.toString();
    }

    /**
     * 根据付款单ID和凭证类型获取转让草稿ID集合（逗号分隔）
     *
     * @param payId       付款单ID
     * @param voucherType 凭证类型（1=应付，2=应收）
     * @return 转让草稿ID集合
     */
    public String getTransferIds(String payId, String voucherType) {
        List<NisScfInfo> list = nisScfInfoDao.listByPayIdAndType(payId, voucherType);
        StringBuilder ids = new StringBuilder();
        if (!EmptyUtils.isEmptyList(list)) {
            for (NisScfInfo nisScfInfo : list) {
                if (StringUtils.isEmpty(nisScfInfo.getTransferId())) {
                    continue;
                }
                if (EmptyUtils.isBlank(ids.toString())) {
                    ids.append(nisScfInfo.getTransferId());
                } else {
                    ids.append(CommonConstant.COMMA_SPLIT).append(nisScfInfo.getTransferId());
                }
            }
        }
        return ids.toString();
    }

    /**
     * 根据付款单号和凭证类型获取凭证ID集合（逗号分隔）
     *
     * @param payNo       付款单号
     * @param voucherType 凭证类型（1=应付，2=应收）
     * @return 凭证ID集合
     */
    public String getVoucherIdsByPayNo(String payNo, String voucherType) {
        List<NisScfInfo> list = nisScfInfoDao.listByPayNoAndType(payNo, voucherType);
        StringBuilder ids = new StringBuilder();
        if (!EmptyUtils.isEmptyList(list)) {
            for (NisScfInfo nisScfInfo : list) {
                if (StringUtils.isEmpty(nisScfInfo.getVoucherId())) {
                    continue;
                }
                if (EmptyUtils.isBlank(ids.toString())) {
                    ids.append(nisScfInfo.getVoucherId());
                } else {
                    ids.append(CommonConstant.COMMA_SPLIT).append(nisScfInfo.getVoucherId());
                }
            }
        }
        return ids.toString();
    }

    /**
     * 根据付款单号和凭证类型获取关联上下文（平台/产品）
     *
     * @param payNo       付款单号
     * @param voucherType 凭证类型（1=应付，2=应收）
     * @return 关联信息，无则返回 null
     */
    public NisScfInfo getContextByPayNo(String payNo, String voucherType) {
        List<NisScfInfo> list = nisScfInfoDao.listByPayNoAndType(payNo, voucherType);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 根据付款单ID获取全部关联信息
     *
     * @param payId 付款单ID
     * @return 关联信息
     */
    public List<NisScfInfo> listByPayId(String payId) {
        return nisScfInfoDao.listByPayId(payId);
    }

    /**
     * 根据债权凭证ID获取付款单与凭证的关联信息
     * <p>凭证开立审批通过后，按凭证ID反查绑定的付款单，回写平台/产品上下文。
     *
     * @param voucherId 债权凭证ID
     * @return 关联信息
     */
    public List<NisScfInfo> listByVoucherId(String voucherId) {
        return nisScfInfoDao.listByVoucherId(voucherId);
    }

    /**
     * 根据付款单ID删除关联信息
     *
     * @param payId 付款单ID
     */
    public void delByPayId(String payId) {
        List<NisScfInfo> list = listByPayId(payId);
        if (!EmptyUtils.isEmptyList(list)) {
            for (NisScfInfo nisScfInfo : list) {
                this.deleteEntity(nisScfInfo);
            }
        }
    }
}
