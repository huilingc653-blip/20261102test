package com.bytter.system.api;
import com.bytter.system.api.dto.BlackWhiteDTO;
import com.bytter.core.base.entity.ICurrentUser;
import java.util.List;
/**
 * 黑白名单接口
 * Created by zhengg on 2020/07/28.
 */
public interface IBlackWhiteService {
    /**
     * 单个检查客商信息
     *
     * @param name      客商名称
     * @param isControl 是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递）
     * @param corpId    单位id
     * @return BlackWhiteDTO{reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，msg:失败信息reCode为2和3时必填！提醒内容}
     */
    BlackWhiteDTO externalCorpCheck(String name, String isControl, String corpId);
    /**
     * 单个检查承兑行信息
     *
     * @param name      客商名称
     * @param isControl 是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递）
     * @return BlackWhiteDTO{reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，msg:失败信息reCode为2和3时必填！提醒内容}
     */
    BlackWhiteDTO bankAcceptanceCheck(String name, String isControl, String corpId);
    /**
     * 单个检查客商信息
     *
     * @param name 客商名称
     * @return BlackWhiteDTO{reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，msg:失败信息reCode为2和3时必填！提醒内容}
     */
    BlackWhiteDTO externalCorpCheck(String name, String corpId);
    /**
     * 单个检查承兑行信息
     *
     * @param name 客商名称
     * @return BlackWhiteDTO{reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，msg:失败信息reCode为2和3时必填！提醒内容}
     */
    BlackWhiteDTO bankAcceptanceCheck(String name, String corpId);
    /**
     * 批量检查客商信息
     *
     * @param params    BlackWhiteDTO{ name:检查名称 appNo:申请单据号 }
     * @param isControl 是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递）
     * @return BlackWhiteDTO{reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，msg:失败信息reCode为2和3时必填！提醒内容}
     */
    BlackWhiteDTO externalCorpCheckBatch(List<BlackWhiteDTO> params, String isControl);
    /**
     * 批量检查承兑行信息
     *
     * @param params    BlackWhiteDTO{ name:检查名称 appNo:申请单据号 }
     * @param isControl 是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递）
     * @return BlackWhiteDTO{reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，msg:失败信息reCode为2和3时必填！提醒内容}
     */
    BlackWhiteDTO bankAcceptanceCheckBatch(List<BlackWhiteDTO> params, String isControl);
    /**
     * 批量检查客商信息
     *
     * @param params BlackWhiteDTO{ name:检查名称 appNo:申请单据号 }
     * @return BlackWhiteDTO{reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，msg:失败信息reCode为2和3时必填！提醒内容}
     */
    BlackWhiteDTO externalCorpCheckBatch(List<BlackWhiteDTO> params);
    /**
     * 批量检查承兑行信息
     *
     * @param params BlackWhiteDTO{ name:检查名称 appNo:申请单据号 }
     * @return BlackWhiteDTO{reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，msg:失败信息reCode为2和3时必填！提醒内容}
     */
    BlackWhiteDTO bankAcceptanceCheckBatch(List<BlackWhiteDTO> params);
    /**
     * 判断客商名称是否存在黑白名单中
     *
     * @param name 客商名称
     * @return
     */
    boolean checkExternalCorpByName(String name, String corpId);
    /**
     * 判断承兑行是否存在黑白名单中
     *
     * @param name         客商名称
     * @param noExternalId 需要排除的外部业务ID
     * @return
     */
    boolean checkBankAcceptanceByName(String name, String corpId, String noExternalId);
    /**
     * 判断承兑行是否存在黑白名单中
     *
     * @param name 客商名称
     * @return
     */
    boolean checkBankAcceptanceByName(String name, String corpId);
    /**
     * 判断客商名称是否存在黑白名单中
     *
     * @param name         客商名称
     * @param noExternalId 需要排除的外部业务ID
     * @return
     */
    boolean checkExternalCorpByName(String name, String corpId, String noExternalId);
    /**
     * 通过客商信息新增或修改黑白名单信息
     *
     * @param params 黑白名单信息
     * @return
     */
    void saveOrUpdateBlackWhite(BlackWhiteDTO params, ICurrentUser user);
    void deleteBlackWhiteByExternalId(String externalId, ICurrentUser user);
    /**
     * 获取黑名单收付限制配置信息。
     *
     * @return 配置信息。
     */
    public String getBlacklistCheck();
    /**
     * 白名批量验证服务类。
     *
     * @param params    验证信息集合
     * @param isControl 是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递）
     * @param listType  名单类别：01客商、02承兑行
     * @return 黑白名单信息。
     */
    public BlackWhiteDTO blackWhiteCheckBatch(List<BlackWhiteDTO> params, String isControl, String listType);
}
