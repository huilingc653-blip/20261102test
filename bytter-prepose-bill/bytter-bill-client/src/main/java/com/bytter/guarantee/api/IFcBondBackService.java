package com.bytter.guarantee.api;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.vo.*;

import java.util.List;

public interface IFcBondBackService {

    /**
     * 查询保证金转回列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<FcBondBackVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) ;
    /**
     * 根据保证金id，得到保证金账户信息
     *
     * @param id
     * @return
     */
    public FcBondBackVO getByIdFcBondBackVO(String id) ;
    /**
     * 转回保证金
     *
     * @param fcBondBackVO
     * @param user
     */
    public void backFcBondMoney(FcBondBackVO fcBondBackVO, ICurrentUser user) ;

    /**
     * 根据保证金id，得到保证金账户信息
     *
     * @param id
     * @return
     */
    public List<FcBondApplyVO> getByIdFcBondApply(String id);

    DataTablesOutput<FcBondApplyVO> fcBondApplyDatatable(DataTablesInput dataTablesInput, ICurrentUser user) ;

    /**
     * 根据保证金id，得到保证金转回历史记录
     *
     * @param id
     * @return
     */
    public List<FcBondBackVO> getByIdFcBondBack(String id);

}
