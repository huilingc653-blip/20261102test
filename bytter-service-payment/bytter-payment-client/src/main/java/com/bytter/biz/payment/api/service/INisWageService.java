package com.bytter.biz.payment.api.service;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.biz.payment.api.dto.NisWageDTO;
import com.bytter.biz.payment.api.dto.NisWageDetailDTO;
import java.util.List;
/**
 * 代发工资接口类
 * @author qijh
 * @since 2017-09-02 09:49:44
 */
public interface INisWageService {
    /**
     * 代发工资生成（外模块生成付款单）
     * @param entity 代发工资单
     * @param user 用户
     */
    void saveNisWage(NisWageDTO entity, ICurrentUser user);
    /**
     * 判断工资册是否重复
     * @param corpId 单位ID
     * @param wageTitle 工资标题
     * @return true/false
     */
    boolean isCheckWageTitle(String corpId, String wageTitle);
    /**
     * 获取ERP待处理单据
     * @return
     */
    List<NisWageDTO> listErpTodoBill();
    /**
     * 根据指令编号获取代发工资明细
     * @param voucherNo 指令编号
     * @param wageId 代发工资ID
     * @return
     */
    List<NisWageDetailDTO> listByVoucherNo(String voucherNo, String wageId);
    /**
     * 根据指令编号更新代发工资明细
     * @param voucherNo 指令编号
     * @param wageId 代发工资ID
     * @return
     */
    int updateVoucherStatByVoucherNo(String voucherNo, String wageId, String newVoucherNo);
    /**
     * 根据指令编号和代发工资ID获取等待结果的费用报销明细列表
     * @param voucherNo 付款指令编号
     * @param wageId 代发工资ID
     * @return 代发工资明细集合
     */
    List<NisWageDetailDTO> listByVoucherNoWaitResult(String voucherNo, String wageId);
}
