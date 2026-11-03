package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCorpPoolDao;
import com.bytter.biz.foreign.entity.FexCorpPool;
import com.bytter.biz.foreign.entity.FexCorpPoolBankacc;
import com.bytter.biz.foreign.entity.FexCorpPoolLimit;
import com.bytter.biz.foreign.vo.FexCorpPoolBankAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.system.api.ICorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 外汇资金池表
 * @date 2020-02-12 14:31:56
 */
@Service("fexCorpPoolService")
@Transactional
public class FexCorpPoolService extends BaseService<FexCorpPool, String> {

    @Autowired
    private FexCorpPoolDao fexCorpPoolDao;
    @Autowired
    private FexCorpPoolBankaccService fexCorpPoolBankaccService;
    @Autowired
    private FexCorpPoolLimitService fexCorpPoolLimitService;
    @Autowired(required = false)
    private ICorpService corpService;

    @Override
    public BaseDao<FexCorpPool, String> getEntityDao() {
        return fexCorpPoolDao;
    }

    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(FexCorpPool entity) {
        super.deleteEntity(entity);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }

    /**
     * 保存
     *
     * @param entity
     */
    public void save(FexCorpPool entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(FexCorpPool entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(FexCorpPool entity) {
        super.saveOrUpdate(entity);
    }

    /**
     * 根据资金池id,单位id,币别id查询外汇资金池列表数据
     * @param poolId
     * @param corpId
     * @param curId
     * @return
     */
    public List<FexCorpPoolBankAccVO> listAllPool(String poolId, String corpId, String curId) {
        List<FexCorpPoolBankAccVO> fexCorpPoolVOS = fexCorpPoolDao.listAllPool(poolId, corpId, curId);
        fexCorpPoolBankaccService.injectValue(fexCorpPoolVOS);
        return fexCorpPoolVOS;
    }

    /**
     * 根据资金池表id得到外汇资金池信息(不带账号信息)
     *
     * @param id
     * @return
     */
    public FexCorpPoolBankAccVO getPoolVOById(String id) {
        FexCorpPoolBankAccVO vo = fexCorpPoolDao.getFexCorpPoolBankAccVOById(id);
        //this.inject(vo);
        return vo;
    }

    /**
     * 根据资金池表id得到外汇资金池信息(带账号信息)
     *
     * @param id
     * @return
     */
    public FexCorpPoolBankAccVO getFexCorpPoolBankAccVOById(String id) {
        FexCorpPoolBankAccVO vo = fexCorpPoolDao.getFexCorpPoolBankAccVOById(id);
        if (!EmptyUtils.isEmpty(vo)) {
            List<FexCorpPoolBankAccVO> fexCorpPoolBankaccVOS = fexCorpPoolBankaccService.listBankAccByParentId(vo.getId(), "", "");
            if (!EmptyUtils.isEmptyList(fexCorpPoolBankaccVOS)) {
                vo.setAccList(fexCorpPoolBankaccVOS);
            }
            List<FexCorpPoolLimit> fexCorpPoolLimits = fexCorpPoolLimitService.listFexCorpPoolLimitByrelatedId(vo.getId());
            if (!EmptyUtils.isEmptyList(fexCorpPoolLimits)) {
                vo.setFexCorpPoolLimits(fexCorpPoolLimits);
            }
            String limit = "";
            for (int i = 0; i < fexCorpPoolLimits.size(); i++) {
                if (i == 0) {
                    limit += fexCorpPoolLimits.get(i).getLimitType();
                } else {
                    limit += "," + fexCorpPoolLimits.get(i).getLimitType();
                }
                if (ForeignConstant.FEX_LIMIT_TYPE_1.equals(fexCorpPoolLimits.get(i).getLimitType())) {
                    vo.setLimitMoney1(fexCorpPoolLimits.get(i).getLimitMoney());
                } else if (ForeignConstant.FEX_LIMIT_TYPE_2.equals(fexCorpPoolLimits.get(i).getLimitType())) {
                    vo.setLimitMoney2(fexCorpPoolLimits.get(i).getLimitMoney());
                } else {
                    vo.setLimitMoney3(fexCorpPoolLimits.get(i).getLimitMoney());
                }
            }
            vo.setLimitType(limit);
        }
        //this.inject(vo);
        return vo;
    }

    /**
     * 获取有效外汇资金池
     * @return
     */
    public List<FexCorpPoolBankAccVO> getFexCorpPools(ICurrentUser user, String resource){
        StringBuilder sql = new StringBuilder();
        if(!StringUtils.isBlank(resource)) {
            String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "c.corp_Id", true);
            sql.append(" and ").append(corpAuthSql);
        }
        List<FexCorpPoolBankAccVO> fexCorpPoolList = this.fexCorpPoolDao.getFexCorpPools(sql.toString());
        List<FexCorpPoolBankAccVO> fcCorpPoolDTOList = new ArrayList<>();
        for (FexCorpPoolBankAccVO vo : fexCorpPoolList) {
            FexCorpPoolBankAccVO showVO = new FexCorpPoolBankAccVO();
            showVO.setShowName("(" + vo.getPoolCode() + ")" + vo.getPoolName());
            BeanUtils.copyNotNullProperties(vo,showVO);
            fcCorpPoolDTOList.add(showVO);
        }
        return fcCorpPoolDTOList;
    }

    /**
     * 获取有效外汇资金池
     * @return
     */
    public List<FexCorpPoolBankAccVO> getPoolAccUniq(ICurrentUser user, String resource){
        StringBuilder sql = new StringBuilder();
        if(!StringUtils.isBlank(resource)) {
            String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "f.corp_Id", true);
            sql.append(" and ").append(corpAuthSql);
        }
        List<FexCorpPoolBankAccVO> fexCorpPoolList = this.fexCorpPoolDao.getPoolAccUniq(sql.toString());
        List<FexCorpPoolBankAccVO> fcCorpPoolDTOList = new ArrayList<>();
        for (FexCorpPoolBankAccVO vo : fexCorpPoolList) {
            FexCorpPoolBankAccVO showVO = new FexCorpPoolBankAccVO();
            showVO.setShowName("(" + vo.getPoolCode() + ")" + vo.getPoolName());
            BeanUtils.copyNotNullProperties(vo,showVO);
            fcCorpPoolDTOList.add(showVO);
        }
        return fcCorpPoolDTOList;
    }

    /**
     * 保存资金账号信息
     * @param parentId
     * @param fexCorpPoolVO
     * @param user
     * @param parentSign
     */
    public void saveBankAcc(String parentId, FexCorpPoolBankAccVO fexCorpPoolVO, ICurrentUser user, String parentSign) {

        String[] ids = null;
        String[] accIds = null;
        String[] corpIds = null;
        String[] curIds = null;
        String[] accNatures = null;
        if (EmptyUtils.isNotEmpty(fexCorpPoolVO.getIds())) {
            ids = fexCorpPoolVO.getIds().split(",");
        }
        if (EmptyUtils.isNotEmpty(fexCorpPoolVO.getAccIds())) {
            accIds = fexCorpPoolVO.getAccIds().split(",");
        }
        if (EmptyUtils.isNotEmpty(fexCorpPoolVO.getCorpIds())) {
            corpIds = fexCorpPoolVO.getCorpIds().split(",");
        }
        if (EmptyUtils.isNotEmpty(fexCorpPoolVO.getCurIds())) {
            curIds = fexCorpPoolVO.getCurIds().split(",");
        }
        if (EmptyUtils.isNotEmpty(fexCorpPoolVO.getAccNatures())) {
            accNatures = fexCorpPoolVO.getAccNatures().split(",");
        }

        //编辑
        if (!EmptyUtils.isEmpty(ids)) {
            for (int i = 0; i < ids.length; i++) {
                FexCorpPoolBankacc poolAcc = null;
                if(StringUtils.isNotBlank(StringUtils.formatNull(ids[i]))){
                    poolAcc = fexCorpPoolBankaccService.get(ids[i]);
                }else{
                    poolAcc = new FexCorpPoolBankacc();
                }
                poolAcc.setAccId(accIds[i] == null ? "" : accIds[i]);
                poolAcc.setCurId(curIds[i] == null ? "" : curIds[i]);
                poolAcc.setCorpId(corpIds[i] == null ? "" : corpIds[i]);
                poolAcc.setAccNature(accNatures[i] == null ? "" : accNatures[i]);
                poolAcc.setParentId(parentId);
                poolAcc.setParentSign(parentSign);
                poolAcc.setCreateBy(user.getLoginName());
                poolAcc.setCreateDate(DateUtils.getDate());
                fexCorpPoolBankaccService.saveOrUpdate(poolAcc);
            }
        }else{
            //新增
            if (!EmptyUtils.isEmpty(accIds)) {
                for (int i = 0; i < accIds.length; i++) {
                    FexCorpPoolBankacc poolAcc = new FexCorpPoolBankacc();
                    poolAcc.setAccId(accIds[i] == null ? "" : accIds[i]);
                    poolAcc.setCurId(curIds[i] == null ? "" : curIds[i]);
                    poolAcc.setCorpId(corpIds[i] == null ? "" : corpIds[i]);
                    poolAcc.setAccNature(accNatures[i] == null ? "" : accNatures[i]);
                    poolAcc.setParentId(parentId);
                    poolAcc.setParentSign(parentSign);
                    poolAcc.setCreateBy(user.getLoginName());
                    poolAcc.setCreateDate(DateUtils.getDate());
                    fexCorpPoolBankaccService.saveOrUpdate(poolAcc);
                }
            }
        }
    }

    /**
     * 保存外汇资金池限额设置表
     * @param fexCorpPoolVO
     * @param relatedId
     * @param poolSign
     */
    public void saveLimitTypes(FexCorpPoolBankAccVO fexCorpPoolVO, String relatedId, String poolSign) {
        String[] limitTypes = null;
        if(EmptyUtils.isNotEmpty(fexCorpPoolVO.getLimitTypes())){
            limitTypes = fexCorpPoolVO.getLimitTypes().split(",");
        }
        if (!EmptyUtils.isEmpty(limitTypes)) {
            for (int i = 0; i < limitTypes.length; i++) {
                FexCorpPoolLimit limit = new FexCorpPoolLimit();
                limit.setRelatedId(relatedId);
                limit.setLimitType(limitTypes[i]);
                if(ForeignConstant.FEX_LIMIT_TYPE_1.equals(limitTypes[i])){
                    limit.setLimitMoney(fexCorpPoolVO.getLimitMoney1());
                }else if (ForeignConstant.FEX_LIMIT_TYPE_2.equals(limitTypes[i])) {
                    limit.setLimitMoney(fexCorpPoolVO.getLimitMoney2());
                } else {
                    limit.setLimitMoney(fexCorpPoolVO.getLimitMoney3());
                }
                limit.setPoolSign(poolSign);
                fexCorpPoolLimitService.saveEntity(limit);
            }
        }
    }

    /**
     * 保存资金池
     *
     * @param fexCorpPoolVO
     * @param user
     */
    public void saveFexCorpPool(FexCorpPoolBankAccVO fexCorpPoolVO, ICurrentUser user) {
        FexCorpPool entity = new FexCorpPool();
        BeanUtils.copyProperties(fexCorpPoolVO, entity);
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        this.saveEntity(entity);
        //保存账号表
        this.saveBankAcc(entity.getId(),fexCorpPoolVO,user, ForeignConstant.FEX_PARENT_SIGN_1);
        //保存外汇资金池限额设置表
        this.saveLimitTypes(fexCorpPoolVO,entity.getId(),ForeignConstant.FEX_POOL_SIGN_1);
    }

    /**
     * 修改资金池
     *
     * @param fexCorpPoolVO
     * @param user
     */
    public void modifyFexCorpPool(FexCorpPoolBankAccVO fexCorpPoolVO,ICurrentUser user) {
        FexCorpPool entity = this.get(fexCorpPoolVO.getId());
        BeanUtils.copyProperties(fexCorpPoolVO, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        //修改时，先删除之前账号和限制信息，再新增
        /*List<FexCorpPoolBankAccVO> fexCorpPoolBankaccVOS = fexCorpPoolBankaccService.listBankAccByParentId(fexCorpPoolVO.getId(), "", "");
        for (FexCorpPoolBankAccVO accVO : fexCorpPoolBankaccVOS) {
            fexCorpPoolBankaccService.delete(accVO.getId());
        }*/
        List<FexCorpPoolLimit> fexCorpPoolLimits = fexCorpPoolLimitService.listFexCorpPoolLimitByrelatedId(fexCorpPoolVO.getId());
        for (FexCorpPoolLimit lim : fexCorpPoolLimits) {
            fexCorpPoolLimitService.delete(lim.getId());
        }

        //保存账号表
        this.saveBankAcc(entity.getId(),fexCorpPoolVO,user, ForeignConstant.FEX_PARENT_SIGN_1);
        //保存外汇资金池限额设置表
        this.saveLimitTypes(fexCorpPoolVO,entity.getId(),ForeignConstant.FEX_POOL_SIGN_1);
    }

    /**
     * 保存主账号
     *
     * @param fexCorpPoolVO
     * @param user
     */
    public void saveFexCorpPoolBankAcc(FexCorpPoolBankAccVO fexCorpPoolVO, ICurrentUser user) {

        FexCorpPoolBankacc entity = new FexCorpPoolBankacc();
        BeanUtils.copyProperties(fexCorpPoolVO, entity);
        entity.setParentSign(ForeignConstant.FEX_PARENT_SIGN_1);
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        fexCorpPoolBankaccService.saveEntity(entity);
        //保存外汇资金池限额设置表
        this.saveLimitTypes(fexCorpPoolVO,entity.getId(),ForeignConstant.FEX_POOL_SIGN_0);
    }

    /**
     * 修改主账号
     *
     * @param fexCorpPoolVO
     * @param user
     */
    public void modifyFexCorpPoolBankAcc(FexCorpPoolBankAccVO fexCorpPoolVO, ICurrentUser user) {
        FexCorpPoolBankacc entity = fexCorpPoolBankaccService.get(fexCorpPoolVO.getId());
        BeanUtils.copyProperties(fexCorpPoolVO, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        fexCorpPoolBankaccService.saveOrUpdate(entity);
        List<FexCorpPoolLimit> fexCorpPoolLimits = fexCorpPoolLimitService.listFexCorpPoolLimitByrelatedId(fexCorpPoolVO.getId());
        for (FexCorpPoolLimit lim : fexCorpPoolLimits) {
            fexCorpPoolLimitService.delete(lim.getId());
        }
        //保存外汇资金池限额设置表
        this.saveLimitTypes(fexCorpPoolVO,entity.getId(),ForeignConstant.FEX_POOL_SIGN_0);
    }

    /**
     * 保存成员账号
     *
     * @param fexCorpPoolBankaccVO
     * @param user
     */
    public void saveFexCorpPoolChild(FexCorpPoolBankAccVO fexCorpPoolBankaccVO, ICurrentUser user) {
        //保存账号表
        this.saveBankAcc(fexCorpPoolBankaccVO.getParentId(),fexCorpPoolBankaccVO,user, ForeignConstant.FEX_PARENT_SIGN_0);
    }

    /**
     * 根据资金池id查找外汇资金池维护是否使用
     * @param poolId
     * @return
     */
    public boolean isUsePool(String poolId) {
        List<FexCorpPoolBankAccVO> fexCorpPoolVOS = fexCorpPoolDao.listAllPool(poolId, "", "");
        if(EmptyUtils.isEmptyList(fexCorpPoolVOS)){
            return false;
        }
        return true;
    }
}
