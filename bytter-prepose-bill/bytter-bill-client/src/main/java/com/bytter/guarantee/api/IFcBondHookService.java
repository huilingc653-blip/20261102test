package com.bytter.guarantee.api;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.FcBondDTO;
import com.bytter.guarantee.vo.FcBondDetailVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 挂接保证金的接口
 *
 * @author liyh
 */
public interface IFcBondHookService {
    /**
     * 根据项目ID查询所有项目保证金信息
     *
     * @param itemId 项目id
     * @return 保证金占用DTO
     */
    List<FcBondApplyDTO> getFcBondApplyListByItemId(String itemId);

    /**
     * 新增保证金占用的相关信息到FC_BOND_APPLY表中 要确保FcBondApplyDTO对象中status属性的正确性, 接口实现类中会根据status进行相应的计算和数据更新 status:95占用,2已释放
     *
     * @param dto 保证金占用DTO
     */
    public void addFcBondApply(FcBondApplyDTO dto);

    /**
     * 更新保证金占用的相关信息 要确保FcBondApplyDTO对象中status属性的正确性, 接口实现类中会根据status进行相应的计算和数据更新 status:95占用,2已释放
     *
     * @param dto 保证金占用DTO
     */
    void updateFcBondApply(FcBondApplyDTO dto);

    /**
     * 招行关联保证金列表开票的保证金存入和账户删除
     *
     * @param dto 保证金占用DTO
     */
    void deleteFcBondByCmbBailFlag(FcBondApplyDTO dto, ICurrentUser user);

    /**
     * 更新项目占用保证金的日期<br>
     * 该方法用于授信延期,借款展期对项目占用保证金日期的修改
     *
     * @param dto 保证金占用DTO
     */
    void updateFcBondApplyDate(FcBondApplyDTO dto);

    /**
     * 根据项目id和项目单号查询项目占用的保证金信息
     *
     * @param itemId 项目id
     * @param itemNo 项目编码
     * @return 保证金占用DTO
     */
    FcBondApplyDTO getFcBondApplyByItemIds(String itemId, String itemNo);

    /**
     * 授信追加时更新项目金额
     *
     * @param dto 保证金占用DTO
     */
    void updateFcBondItemMoney(FcBondApplyDTO dto);

    /**
     * 根据项目id和保证金id查询项目占用的保证金信息
     *
     * @param itemId 项目id
     * @param itemNo 项目编码
     * @param bondId 保证金id
     * @return 保证金占用DTO
     */
    FcBondApplyDTO getFcBondApply(String itemId, String itemNo, String bondId);

    /**
     * 更新保证金占用的相关信息<br>
     * 要确保FcBondApplyDTO对象中status属性的正确性, 接口实现类中会根据status进行相应的计算和数据更新 status:95占用,2已释放<br>
     *
     * @param dto 保证金占用DTO
     */
    void updateFcBondApplyData(FcBondApplyDTO dto);

    /**
     * 根据保证金id查询账户信息
     *
     * @param id 保证金id
     */
    FcBondDTO getFcBondMsgById(String id);

    /**
     * 根据保证金账号查询保证金信息
     *
     * @param bondBankAcc 保证金账号
     */
    FcBondDTO getFcBondMsgByBondBankAcc(String bondBankAcc);

    FcBondDetailVO getFcBondDetailById(String id);

    /**
     * 根据参数查询项目释放的保证金信息
     *
     * @param fcBondApplyDTO 保证金查询参数
     * @return 保证金占用DTO
     */
    FcBondApplyDTO getFcBondByParams(FcBondApplyDTO fcBondApplyDTO);

    /**
     * @param billFrom  业务类型
     * @param bondId    业务Id
     * @param bondMoney 金额
     */
    void backBondMoney(String billFrom, String bondId, BigDecimal bondMoney, ICurrentUser user);


    List<String> getItemIds();

    /**
     * 根据项目ID查询所有项目保证金信息
     * @param itemIds 项目id
     * @return 保证金占用DTO
     */
    List<FcBondApplyDTO>  getFcBondApplyByItemIdsType(List<String> itemIds,String itemType);

}
