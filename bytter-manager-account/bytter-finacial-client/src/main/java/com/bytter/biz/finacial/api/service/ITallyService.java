package com.bytter.biz.finacial.api.service;

import com.bytter.biz.finacial.api.dto.*;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;

import java.util.List;
import java.util.Map;

/**
 * 记账管理调用核算接口类
 */
public interface ITallyService {
    /**
     * 银企接口收款记账列表查询
     *
     * @param dataTablesInput 查询参数
     * @param type            查询类型
     * @param userId          用户id
     * @param corpAuth        有权限的单位id集合
     * @return 列表数据
     */
    DataTablesOutput<BtNetChalkDTO> queryBtNetChalk(DataTablesInput dataTablesInput, String type, String userId, List<String> corpAuth);

    /**
     * 根据id查询银企收款数据
     *
     * @param id 主键
     * @return 银企收款数据
     */
    BtNetChalkDTO queryBtNetChalkDTO(String id);

    /**
     * 改账自动复核
     *
     * @param entity 修改参数
     * @param idList 状态
     * @return 修改笔数
     */
    int updateEditAutoCheckSign(BtNetChalkDTO entity, List<String> idList);

    /**
     * 改账提交
     *
     * @param id       数据主键
     * @param newState 单据新状态
     * @param eState   单据状态
     * @return 修改笔数
     */
    int updateEditPassSign(List<String> idList, String newState);

    /**
     * 更新付款单复核信息
     *
     * @param entity 修改参数
     * @return 修改笔数
     */
    int updateAutoCheckSign(BtNetChalkDTO entity, List<String> idList);

    /**
     * 记账提交
     *
     * @param entity 修改参数
     * @param eState 单据新状态
     * @return 修改笔数
     */
    int updateTallySign(BtNetChalkDTO entity, List<String> idList, String eState);

    /**
     * 改账打回
     *
     * @param id     单据id
     * @param eState 状态
     * @return 修改笔数
     */
    int updateEditBackSign(String id, String eState);

    /**
     * 复核打回
     *
     * @param id 单据主键
     * @return 修改笔数
     */
    int updateCheckBackSign(String id);

    /**
     * 改账打回
     *
     * @param entity 修改参数
     * @return 修改笔数
     */
    int updateCheckSign(BtNetChalkDTO entity, List<String> idList);

    /**
     * 查询待记账数据
     *
     * @param booleanFalse 是否标志
     * @return 查询数据集合
     */
    List<BtNetChalkDTO> queryTallyData(String booleanFalse,String num);

    /**
     * 查询待记账数据
     *
     * @param booleanFalse 是否标志
     * @return 查询数据集合
     */
    List<BtNetChalkDTO> queryTallyData(String booleanFalse);

    /**
     * 根据id查询实体类
     *
     * @param id 主键
     * @return 收款信息
     */
    BtNetChalkDTO queryBtNetChalk(String id);

    /**
     * 更新实体类数据
     *
     * @param btNetChalk 更新实体
     */
    void updateBtNetChalkEntity(BtNetChalkDTO btNetChalk);

    /**
     * 根据id获取银企收款款数据
     *
     * @param id 数据id
     * @return 数据信息
     */
    BtNetChalkDTO getNeChalkDtoById(String id);

    /**
     * 修改自动记账标识
     *
     * @param id        主键
     * @param tallySign 是否有效标识
     */
    void updateAutoTallySign(String id, String tallySign);

    /**
     * 单位校验使用
     *
     * @param corpId 单位id
     * @return 是否
     */
    Integer verifyCorpDisableByCorpId(String corpId);

    /**
     * 通过流水账主档表id查询明细信息
     *
     * @param bookId 流水主键id
     * @return 记账流水
     */
    List<FcAccbookDtlDTO> getBookDtlVo(String bookId);

    /**
     * 新增网点往来账务信息表
     *
     * @param unionAccBookDto 新增数据
     */
    void saveUnionAccBook(FcUnionAccbookDTO unionAccBookDto);

    /**
     * 更新网点往来账务信息表
     *
     * @param unionAccBookDto 更新参数
     */
    void updateUnionAccEntity(FcUnionAccbookDTO unionAccBookDto);

    /**
     * 根据id查询网点往来账户信息数据集合
     *
     * @param id 凭证id
     * @return 数据集合
     */
    List<FcUnionAccbookDTO> queryFcUnionAccBook(String id);

    /**
     * 查询网点往来账户分页数据
     *
     * @param dataTablesInput 查询参数
     * @param type            查询类型
     * @param userId          用户id
     * @return 列表数据
     */
    DataTablesOutput<FcUnionAccbookDTO> queryFcUnionAccBook(DataTablesInput dataTablesInput, String type, String userId);

    /**
     * 根据发送凭证号获取往来信息
     *
     * @param sendVoucherNo 发送凭证号
     * @return 数据集合
     */
    List<FcUnionAccbookDTO> getUnionAccBookBySendNo(String sendVoucherNo);

    /**
     * 修改单据状态
     *
     * @param id           单据主键
     * @param billState    修改状态
     * @param oldBillState 旧状态
     */
    void updateUnionAccBookBillState(String id, String billState, String oldBillState);

    /**
     * 获取所有网点往来账信息
     *
     * @return 往来账数据集合
     */
    List<FcUnionAccbookDTO> queryTallyFcUnionAccBook();

    /**
     * 根据id获取流水数据信息
     *
     * @param key 流水id
     * @return 流水数据
     */
    FcBankAccDtlDTO getFcBankAccDtl(String key);

    /**
     * 根据id获取数据
     *
     * @param id id
     * @return 流水数据
     */
    FcBankAccDtlDTO findByDtlId(String id);

    /**
     * 保存或新增流水信息
     *
     * @param entity 参数
     */
    void fcBankAccDtlSaveOrUpdate(FcBankAccDtlDTO entity);

    /**
     * 根据流水号查询明细
     *
     * @param orderNo 流水号
     * @return 明细数据
     */
    FcBankAccDtlDTO findByOrderNo(String orderNo);

    /**
     * 更新明细数据
     *
     * @param entity 更新参数
     */
    void updateFcBankAccDtlEntity(FcBankAccDtlDTO entity);

    /**
     * 获取凭证导入列表数据
     *
     * @param dataTablesInput 查询参数
     * @param userId          用户id
     * @return 列表数据
     */
    DataTablesOutput<FcAccbookImportDTO> voucherImpDatatable(DataTablesInput dataTablesInput, String userId);

    /**
     * 删除凭证信息
     *
     * @param ids 凭证id集合
     */
    void deleteImportVoucher(List<String> ids);

    /**
     * 保存导入凭证信息
     *
     * @param importlist 导入数据集合
     */
    void batchSaveFcAccBookImport(List<FcAccbookImportDTO> importlist);

    /**
     * 获取凭证数据
     *
     * @param ids 凭证id集合
     * @return 凭证信息
     */
    List<FcAccbookImportDTO> getImprotVoucher(List<String> ids);

    /**
     * 查询明细列表数据
     *
     * @param input    查询参数
     * @param type     查询类型
     * @param menuType 菜单类型
     * @param id       主键
     * @param corpAuth 有权限的单位数据
     * @return 列表数据
     */
    DataTablesOutput<BisAccDtlCopyDTO> queryFcBankAccDtl(DataTablesInput input, String type, String menuType, String id, List<String> corpAuth);

    /**
     * 查询明细列表数据
     *
     * @param input    查询你参数
     * @param menuType 查询类型
     * @param corpAuth 有权限的单位
     * @return 列表数据
     */
    DataTablesOutput<BisAccDtlCopyDTO> queryBisAccDtl(DataTablesInput input, String menuType, List<String> corpAuth);

    /**
     * 获取数据集合
     *
     * @param menuType 查询类型
     * @return 集合明细
     */
    List<BisAccDtlCopyDTO> queryTallyBisAccDtl(String menuType);

    /**
     * 批量新增明细
     *
     * @param remindDtls 明细集合
     */
    void batchSaveRemindDtl(List<FcTallyRemindDtlDTO> remindDtls);

    /**
     * 删除明细
     *
     * @param busId
     */
    void deleteRemindDtlByBusId(String busId);

    /**
     * 数据回写
     *
     * @param toString sql
     * @param params   参数
     */
    void createSQLQueryFinacialColumn(String toString, Map<String, Object> params);

    /**
     * 获取手工账类型数据
     *
     * @param sysRaceCode 系统编码
     * @param flowType    流程类型
     * @return 手工账类型
     */
    FcManualFinacialTypeDTO getTypeBysysCodeAndFlow(String sysRaceCode, String flowType);

    /**
     * 根据手工帐ID查询 回写列值对应 数据
     *
     * @param id 手工账id
     * @return 对应的数据
     */
    List<FcManualFinacialColumnDTO> listColumnVoBySysRaceCode(String id);

    /**
     * 查询全部的 记账类型  分页
     *
     * @param dataTablesInput 查询参数
     * @param user            当前用户信息
     * @return 列表数据信息
     */
    DataTablesOutput<FcManualFinacialTypeDTO> getDataTablesAllType(DataTablesInput dataTablesInput, ICurrentUser user);

    /**
     * 获取记账类型
     *
     * @param id 类型id
     * @return 类型
     */
    FcManualFinacialTypeDTO getType(String id);

    /**
     * 更新或新增手工账类型表
     *
     * @param type 更新数据
     */
    void saveOrUpdateManualFinacial(FcManualFinacialTypeDTO type);

    /**
     * 删除手工账类型
     *
     * @param id 主键
     */
    void deleteColumnByTypeId(String id);

    /**
     * 批量新增手工账回写列值对应表
     *
     * @param list 新增数据
     */
    void batchSaveManualFinacialColumn(List<FcManualFinacialColumnDTO> list);

    /**
     * 删除手工账类型数据
     *
     * @param id 主键
     */
    void deleteTypeById(String id);

    List<FcBankAccDtlDTO> listFcBankAccDtlDTOByDtlIds(List<String> dtlIds, String type, String userId);

    /**
     * 根据ids获取数据
     *
     * @param ids ids
     * @return 流水数据
     */
    List<FcBankAccDtlDTO> getFcBankAccDtlsByIds(List<String> ids);

    /**
     * 根据id集合获取数据
     *
     * @param idList id集合
     * @return 数据集合
     */
    List<BtNetChalkDTO> listBtNetChalkVo(List<String> idList);

    List<String> listByBookId(String bookId);

    BtNetChalkDTO queryBtNetChalkVo(String id);

    boolean existCashSubject(List<String> accList);

    int mergeUpdateEditBackSign(List<String> idList);

    int mergeUpdateCheckBackSign(List<String> idList);

    /**
     * 更新银行流水记账恢复状态
     *
     * @param id
     * @return
     */
    void updateBisAccDtlRecovery(String id);
}
