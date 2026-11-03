package com.bytter.guarantee.api;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.vo.*;

import java.util.List;

public interface ISearchFcBondService {
    /**
     * 保证金项目查询
     *
     * @param dataTablesInput
     * @return
     */
    DataTablesOutput<FcBondApplyVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user);

    /**
     * 保证金账户查询
     *
     * @param dataTablesInput
     * @return
     */
    DataTablesOutput<FcBondBackSearchVO> getDataTablesUnion1(DataTablesInput dataTablesInput, ICurrentUser user);

    /**
     * 根据保证金id，得到保证金账户信息
     *
     * @param id
     * @return
     */
    FcBondBackVO getByIdFcBondBackVO(String id);

    /**
     * 根据保证金id，得到保证金账户信息
     *
     * @param id
     * @return
     */
    List<FcBondApplyVO> getByIdFcBondApply(String id);
    List<FcBondApplyVO> getByIdFcBondApply(String id,String param);

    /**
     * 根据保证金id，得到保证金转回历史记录
     *
     * @param id
     * @return
     */
    public List<FcBondBackVO> getByIdFcBondBack(String id);

    /**
     * 根据占用表FC_BOND_APPLY的ID，得到FcBondApplyVO
     *
     * @param id
     * @return
     */
    public FcBondApplyVO getFcBondApplyVO(String id);

    /**
     * 根据项目id和项目单号查询项目占用的保证金信息
     *
     * @param itemId
     * @param itemNo
     * @return
     */
    public FcBondApplyInfoVO getFcBondApplyInfo(String itemId, String itemNo);

    /**
     * 根据占用表FC_BOND_ADD中的bond_id，得到保证金追历史明细
     *
     * @return
     */
    public List<FcBondAddDTO> getFcBondAddsByBondId(String bondId);

    /**
     * 查询保证金占用相关银行
     *
     * @return
     */
    public List<FcBondVO> queryBondBank();

    /**
     * 查询保证金占用相关单位
     *
     * @return
     */
    public List<FcBondApplyDTO> queryBondCorp(String resource, ICurrentUser user);

    public List<FcBondUseVO> getBondUseList(FcBondApplyVO applyVO, String resource, ICurrentUser user);

    public List<FcBondUseDtlVO> getBondTransDtl(String id);

    /**
     * 根据单位，银行，币别查询保证金占用明细
     *
     * @return
     */
    public List<FcBondUseDtlVO> queryBondUseDtl(FcBondApplyVO applyVO);
}