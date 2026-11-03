package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCorpPoolTranDao;
import com.bytter.biz.foreign.entity.FexCorpPoolTran;
import com.bytter.biz.foreign.vo.FexCorpPoolTranVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 外汇资金池交易表
 * @date 2020-02-21 14:31:56
 */
@Service("fexCorpPoolTranService")
@Transactional
public class FexCorpPoolTranService extends BaseService<FexCorpPoolTran, String> {

    @Autowired
    private FexCorpPoolTranDao fexCorpPoolTranDao;


    @Override
    public BaseDao<FexCorpPoolTran, String> getEntityDao() {
        return fexCorpPoolTranDao;
    }

    /**
     * 外汇资金池交易列表查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        return fexCorpPoolTranDao.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 根据资金池交易表id得到资金池交易相关信息
     *
     * @param id
     * @return
     */
    public FexCorpPoolTranVO getFexCorpPoolTranVOById(String id) {
        return fexCorpPoolTranDao.getFexCorpPoolTranVOById(id);
    }

    /**
     * 根据外汇资金池id得到资金池交易相关信息
     *
     * @param poolId 外汇资金池id
     * @param bankAccId 外汇资金池账户表id
     * @return
     */
    public FexCorpPoolTranVO getFexCorpPoolTranVOByPoolId(String poolId, String bankAccId) {
        List<FexCorpPoolTranVO> voList = fexCorpPoolTranDao.getFexCorpPoolTranVOByPoolId(poolId,bankAccId);
        FexCorpPoolTranVO tranVo = setTranInfo(voList);
        return tranVo;
    }

    /**
     * 根据资金池id和账号id和币别id得到资金池交易相关信息
     *
     * @return
     */
    public List<FexCorpPoolTranVO> getTranListByPoolIdAndBankAccId(String poolId, String bankAccId,String curId) {
        List<FexCorpPoolTranVO> tranList = fexCorpPoolTranDao.getTranListByPoolIdAndBankAccId(poolId, bankAccId,curId);

        FexCorpPoolTranVO tranVo = new FexCorpPoolTranVO();
        for (FexCorpPoolTranVO vo : tranList) {
            BeanUtils.copyProperties(vo, tranVo);
            List<FexCorpPoolTranVO> jvo = new ArrayList<>();
            List<FexCorpPoolTranVO> wvo = new ArrayList<>();
        }
        return tranList;
    }

    /**
     * 根据资金池id和账号id得到资金池交易相关信息
     *
     * @return
     */
    public FexCorpPoolTranVO getTranInfoByPoolIdAndBankAccId(String poolId, String bankAccId, String curId) {
        List<FexCorpPoolTranVO> voList = this.getTranListByPoolIdAndBankAccId(poolId, bankAccId, curId);
        FexCorpPoolTranVO tranVo = setTranInfo(voList);
        return tranVo;
    }

    /**
     * 设置借入，对外交易信息
     * @param voList
     * @return
     */
    public FexCorpPoolTranVO setTranInfo(List<FexCorpPoolTranVO> voList){
        FexCorpPoolTranVO tranVo = new FexCorpPoolTranVO();
        if (!EmptyUtils.isEmptyList(voList)) {
            List<FexCorpPoolTranVO> jvo = new ArrayList<>();
            List<FexCorpPoolTranVO> wvo = new ArrayList<>();
            for (FexCorpPoolTranVO vo : voList) {
                BeanUtils.copyProperties(vo, tranVo);
                if (ForeignConstant.FEX_TRAN_SIGN_0.equals(vo.getTranSign())) {
                    jvo.add(vo);
                } else {
                    wvo.add(vo);
                }
            }
            tranVo.setTranTypeJDtl(jvo);
            tranVo.setTranTypeWDtl(wvo);
        }
        return tranVo;
    }
    /**
     * 保存资金池交易表
     *
     * @param fexCorpPoolTranVO
     * @param user
     */
    public void save(FexCorpPoolTranVO fexCorpPoolTranVO, ICurrentUser user) {
        List<FexCorpPoolTranVO> list = this.setTranVal(fexCorpPoolTranVO);
        if(!EmptyUtils.isEmptyList(list)){
            for(FexCorpPoolTranVO vo : list){
                this.saveTran(vo,user);
            }
        }
    }

    /**
     * 保存资金池交易表
     *
     * @param fexCorpPoolTranVO
     * @param user
     */
    public void saveTran(FexCorpPoolTranVO fexCorpPoolTranVO, ICurrentUser user) {
        FexCorpPoolTran entity = new FexCorpPoolTran();
        BeanUtils.copyProperties(fexCorpPoolTranVO, entity);
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        this.saveEntity(entity);
    }

    /**
     * 修改资金池交易表
     *
     * @param fexCorpPoolTranVO
     * @param user
     */
    public void modify(FexCorpPoolTranVO fexCorpPoolTranVO, ICurrentUser user) {
        List<FexCorpPoolTranVO> voList = this.getTranListByPoolIdAndBankAccId(fexCorpPoolTranVO.getPoolId(), fexCorpPoolTranVO.getBankaccId(), fexCorpPoolTranVO.getCurId());
        //先删除再新增
        if(!EmptyUtils.isEmptyList(voList)){
            for(FexCorpPoolTranVO tranVO : voList){
                this.delete(tranVO.getId());
            }
        }
        List<FexCorpPoolTranVO> list = this.setTranVal(fexCorpPoolTranVO);
        if(!EmptyUtils.isEmptyList(list)){
            //说明是编辑
            for(FexCorpPoolTranVO vo : list){
               this.saveTran(vo,user);
            }
        }
    }

    /**
     * 数据拼装处理
     * @param fexCorpPoolTranVO
     * @return
     */
    public List<FexCorpPoolTranVO> setTranVal(FexCorpPoolTranVO fexCorpPoolTranVO) {
        FexCorpPoolTranVO tranVO = null;
        List<FexCorpPoolTranVO> dataVO = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(fexCorpPoolTranVO.getTranTypeJDtl())) {
            for (FexCorpPoolTranVO vo : fexCorpPoolTranVO.getTranTypeJDtl()) {
                if(StringUtils.isNotBlank(vo.getProId())&&StringUtils.isNotBlank(vo.getTranType())){
                    tranVO = new FexCorpPoolTranVO();
                    BeanUtils.copyProperties(fexCorpPoolTranVO,tranVO);
                    tranVO.setId(vo.getId());
                    tranVO.setTranType(vo.getTranType());
                    tranVO.setProId(vo.getProId());
                    tranVO.setTranSign(ForeignConstant.FEX_TRAN_SIGN_0);
                    dataVO.add(tranVO);
                }
            }
        }
        if (!EmptyUtils.isEmptyList(fexCorpPoolTranVO.getTranTypeWDtl())) {
            for (FexCorpPoolTranVO vo : fexCorpPoolTranVO.getTranTypeWDtl()) {
                if(StringUtils.isNotBlank(vo.getProId())&&StringUtils.isNotBlank(vo.getTranType())){
                    tranVO = new FexCorpPoolTranVO();
                    BeanUtils.copyProperties(fexCorpPoolTranVO,tranVO);
                    tranVO.setId(vo.getId());
                    tranVO.setTranType(vo.getTranType());
                    tranVO.setProId(vo.getProId());
                    tranVO.setTranSign(ForeignConstant.FEX_TRAN_SIGN_1);
                    dataVO.add(tranVO);
                }
            }
        }
        return dataVO;
    }
}