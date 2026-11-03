package com.bytter.system.api;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import com.bytter.system.api.dto.FbsBudgetDetailDTO;
import com.bytter.system.api.vo.ExternalCorpBankAccDetailDto;

import java.util.List;
import java.util.Map;
/**
 * 客商资料接口
 *
 * @author zhoul on 2017/7/6.
 */
public interface IExternalCorpService {
    /**
     * 根据单位ID，查找供应商集合
     *
     * @param corpId
     * @return
     */
    List<ExternalCorpDTO> listByCorpId(String corpId);
    /**
     * 根据客商code和客商名称判断是否需要新增客商信息
     *
     * @param code
     */
    ExternalCorpDTO getByCorpIdAndCode(String code);

    /**
     * 根据客商ID和客商名称判断是否需要新增客商信息
     *
     * @param externalCorpDTO
     */
    void saveIfNotExist(ExternalCorpDTO externalCorpDTO);
    /**
     * 根据单位和客商名称获取客商信息
     *
     * @param corpId
     * @param name
     * @return
     */
    ExternalCorpDTO findByCorpIdAndName(String corpId, String name);
    /**
     * 根据客商表Id和客商银行账号查找客商账号信息
     *
     * @param externalCorpId
     * @param bankAcc
     * @return
     */
    ExternalCorpBankAccDTO getExternalCorpBankAccByIdAndAcc(String externalCorpId, String bankAcc);
    /**
     * 根据客商银行账号集合查询客商账号信息，包括省市信息
     *
     * @param bankAccs 客商银行账号集合
     * @return 客商账号信息
     */
    Map<String, ExternalCorpBankAccDTO> getExternalCorpBankAccByAcc(List<String> bankAccs);
    /**
     * 根据id集合获取客商
     *
     * @param externalCorpIds
     * @return
     */
    public List<ExternalCorpDTO> getExternalCorpVoListByIds(List<String> externalCorpIds);
    /**
     * 根据id集合获取客商Map
     *
     * @param externalCorpIds
     * @return
     */
    public Map<String,ExternalCorpDTO> getExternalCorpVoMapByIds(List<String> externalCorpIds) ;
    /**
     * 根据id获取客商
     *
     * @param externalCorpId
     * @return
     */
    public ExternalCorpDTO getExternalCorpVoById(String externalCorpId) ;
    IPageResult<ExternalCorpBankAccDetailDto> listByExternalDetail(DataTablesInput dataTablesInput);
    ExternalCorpBankAccDetailDto listByExternalDetailForBill(DataTablesInput dataTablesInput);
    List<ExternalCorpDTO> findExternalCorpLikeName(String name);

    public boolean queryExternalCorpAndCorpId(String externalCorpId,String corpId);

    List<ExternalCorpDTO> queryAll();


    /**
     * 根据银行账号查询客商信息
     * @param budgetDetail
     * @return
     */
    ExternalCorpBankAccDTO queryExternalCorpAndAccInfoDTO(FbsBudgetDetailDTO budgetDetail);


    List<ExternalCorpDTO> queryInId(List<String> ids);

    Map<String, ExternalCorpDTO> queryFexExternalCorpMapByIds(List<String> ids);

    /**
     * 根据外汇交易对手数据源ID批量查询展示信息。
     * 数据源包括客商、单位、境内银行和境外银行。
     *
     * @param ids 交易对手数据源ID集合
     * @return 以数据源ID为键的交易对手展示信息
     */
    Map<String, ExternalCorpDTO> queryFexCounterpartyMapByIds(List<String> ids);

    /**
     * 根据名称模糊查询外汇统一交易对手。
     * 数据源包括客商、单位、境内银行和境外银行。
     *
     * @param name 交易对手名称
     * @return 交易对手信息
     */
    List<ExternalCorpDTO> findFexCounterpartyLikeName(String name);

    Map<String, ExternalCorpBankAccDTO> queryFexExternalCorpBankAccMapByIds(List<String> ids);

    List<ExternalCorpBankAccDTO> queryFexExternalCorpBankAccListByExternalCorpId(String externalCorpId);

    List<ExternalCorpDTO> queryInExternalType(List<String> externalType);



    /**
     * 更新客商信息（外汇）
     *
     * @param externalCorpDTO
     */
    void updateExternalCorpInfo(ExternalCorpDTO externalCorpDTO, ExternalCorpBankAccDTO bankAccDTO, ICurrentUser user);

    /**
     *
     * 根据客商账号查询账号信息
     *
     * @param externalAcc
     * @return
     */
    ExternalCorpBankAccDTO queryByBankAcc(String externalAcc);

    ExternalCorpDTO getById(String oppId);

    List<String> getCorpIdsByExternalCorpId(String externalId);
}
