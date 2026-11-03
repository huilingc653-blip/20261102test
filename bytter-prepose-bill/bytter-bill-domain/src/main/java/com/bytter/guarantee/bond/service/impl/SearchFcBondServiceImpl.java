package com.bytter.guarantee.bond.service.impl;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StreamUtils;
import com.bytter.guarantee.api.ISearchFcBondService;
import com.bytter.guarantee.bond.service.SearchFcBondService;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.vo.*;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service("searchFcBondServiceImpl")
@DubboService
public class SearchFcBondServiceImpl implements ISearchFcBondService {
    @Autowired
    private SearchFcBondService searchFcBondService;
    /**
     * 保证金项目查询
     *
     * @param dataTablesInput
     * @return
     */
    @Override
    public DataTablesOutput<FcBondApplyVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        return searchFcBondService.getDataTablesUnion( dataTablesInput,  user);
    }
    /**
     * 保证金账户查询
     *
     * @param dataTablesInput
     * @return
     */
    @Override
    public DataTablesOutput<FcBondBackSearchVO> getDataTablesUnion1(DataTablesInput dataTablesInput, ICurrentUser user) {

        return searchFcBondService.getDataTablesUnion1( dataTablesInput,  user);
    }
    /**
     * 根据保证金id，得到保证金账户信息
     *
     * @param id
     * @return
     */
    @Override
    public FcBondBackVO getByIdFcBondBackVO(String id) {
        return searchFcBondService.getByIdFcBondBackVO( id);
    }
    /**
     * 根据保证金id，得到保证金账户信息
     *
     * @param id
     * @return
     */
    @Override
    public List<FcBondApplyVO> getByIdFcBondApply(String id) {

        return searchFcBondService.getByIdFcBondApply(id);
    }

    @Override
    public List<FcBondApplyVO> getByIdFcBondApply(String id,String param) {
        List<FcBondApplyVO> list = searchFcBondService.getByIdFcBondApply(id,param);
        if(EmptyUtils.isNotEmpty(list)){
            BigDecimal total = BigDecimal.ZERO;
            for(FcBondApplyVO vo : list){
                total = total.add(vo.getApplyMoney());
            }
            FcBondApplyVO vo = new FcBondApplyVO();
            vo.setApplyMoney(total);
            vo.setOrigNo("汇总");
            list.add(vo);
        }
        return list;
    }
    /**
     * 根据保证金id，得到保证金转回历史记录
     *
     * @param id
     * @return
     */
    @Override
    public List<FcBondBackVO> getByIdFcBondBack(String id) {
        return searchFcBondService.getByIdFcBondBack(id);
    }
    /**
     * 根据占用表FC_BOND_APPLY的ID，得到FcBondApplyVO
     *
     * @param id
     * @return
     */
    @Override
    public FcBondApplyVO getFcBondApplyVO(String id) {
        return searchFcBondService.getFcBondApplyVO(id);
    }
    /**
     * 根据项目id和项目单号查询项目占用的保证金信息
     *
     * @param itemId
     * @param itemNo
     * @return
     */
    @Override
    public FcBondApplyInfoVO getFcBondApplyInfo(String itemId, String itemNo) {
        return searchFcBondService.getFcBondApplyInfo(itemId, itemNo);
    }
    /**
     * 根据占用表FC_BOND_ADD中的bond_id，得到保证金追历史明细
     * @return
     */
    @Override
    public List<FcBondAddDTO> getFcBondAddsByBondId(String bondId) {
        return searchFcBondService.getFcBondAddsByBondId(bondId);
    }
    /**
     * 查询保证金占用相关银行
     * @return
     */
    @Override
    public List<FcBondVO> queryBondBank() {
        //增加去重处理
        return searchFcBondService.queryBondBank().stream()
                .filter(StreamUtils.distinctKey(FcBondVO::getBankCode)).collect(Collectors.toList());
    }
    /**
     * 查询保证金占用相关单位
     *
     * @return
     */
    @Override
    public List<FcBondApplyDTO> queryBondCorp(String resource, ICurrentUser user) {
        return searchFcBondService.queryBondCorp( resource,  user) ;
    }
    @Override
    public List<FcBondUseVO> getBondUseList(FcBondApplyVO applyVO, String resource, ICurrentUser user) {

        return searchFcBondService.getBondUseList( applyVO,  resource,  user);
    }
    @Override
    public List<FcBondUseDtlVO> getBondTransDtl(String id){
        return searchFcBondService.getBondTransDtl(id);
    }
    /**
     * 根据单位，银行，币别查询保证金占用明细
     * @return
     */
    @Override
    public List<FcBondUseDtlVO> queryBondUseDtl(FcBondApplyVO applyVO){
        return searchFcBondService.queryBondUseDtl(applyVO);
    }

}