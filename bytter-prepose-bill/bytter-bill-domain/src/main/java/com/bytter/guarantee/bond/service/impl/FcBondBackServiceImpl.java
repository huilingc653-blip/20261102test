package com.bytter.guarantee.bond.service.impl;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.api.IFcBondBackService;
import com.bytter.guarantee.bond.service.FcBondBackService;
import com.bytter.guarantee.vo.FcBondApplyVO;
import com.bytter.guarantee.vo.FcBondBackVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("fcBondBackServiceImpl")
@DubboService
public class FcBondBackServiceImpl implements IFcBondBackService {
    @Autowired
    FcBondBackService fcBondBackService;
    /**
     * 查询保证金转回列表
     *
     * @param dataTablesInput
     * @return
     */
    @Override
    public DataTablesOutput<FcBondBackVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        return fcBondBackService.getDataTablesUnion( dataTablesInput,  user);
    }
    /**
     * 根据保证金id，得到保证金账户信息
     *
     * @param id
     * @return
     */
    @Override
    public FcBondBackVO getByIdFcBondBackVO(String id) {
        return fcBondBackService.getByIdFcBondBackVO( id);
    }
    /**
     * 转回保证金
     *
     * @param fcBondBackVO
     * @param user
     */
    @Override
    public void backFcBondMoney(FcBondBackVO fcBondBackVO, ICurrentUser user) {
        fcBondBackService.backFcBondMoney( fcBondBackVO,  user);
    }

    /**
     * 根据保证金id，得到保证金账户信息
     *
     * @param id
     * @return
     */
    @Override
    public List<FcBondApplyVO> getByIdFcBondApply(String id) {
        return fcBondBackService.getByIdFcBondApply(id);
    }


    @Override
    public DataTablesOutput<FcBondApplyVO> fcBondApplyDatatable(DataTablesInput dataTablesInput, ICurrentUser user) {
        String joinSql ="";
        return fcBondBackService.fcBondApplyDatatable( dataTablesInput,  joinSql);
    }
    /**
     * 根据保证金id，得到保证金转回历史记录
     *
     * @param id
     * @return
     */
    @Override
    public List<FcBondBackVO> getByIdFcBondBack(String id) {
        return fcBondBackService.getByIdFcBondBack( id);
    }
}
