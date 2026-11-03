package com.bytter.basic.service;

import com.bytter.basic.dao.FcInstrateAdjustDao;
import com.bytter.basic.entity.FcInstrateAdjust;
import com.bytter.basic.vo.FcInstrateAdjustSimpleVO;
import com.bytter.basic.vo.FcInstrateAdjustVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.PoolContant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.subject.dao.FcSubjectDao;
import com.bytter.subject.dao.FcSubjectDayDao;
import com.bytter.system.api.*;
import com.bytter.system.api.bean.AccInstrateBean;
import com.bytter.system.api.bean.InstrateAdjustBean;
import com.bytter.system.api.constant.SysResourceConstant;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.inneraccount.service.InnerAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部利率
 * @date 2017-07-11 10:54:47
 */
@Service("fcInstrateAdjustServiceBase")
@Transactional
public class FcInstrateAdjustService extends BaseService<FcInstrateAdjust, String> {
    @Autowired
    private FcInstrateAdjustDao fcInstrateAdjustDao;
    @Autowired
    private IFcAccService fcAccServiceI;
    @Autowired
    private IDictService dictServiceI;
    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private IFcCorpPoolService fcCorpPoolServiceI;
    @Autowired
    private IConfigService configServiceI;
    @Autowired
    private InnerAccountService innerAccountService;

    @Autowired
    private   FcSubjectDayDao fcSubjectDayDao;
    @Autowired
    private FcSubjectDao fcSubjectDao;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(FcInstrateAdjust entity) {
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
    public void save(FcInstrateAdjust entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcInstrateAdjust entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(FcInstrateAdjust entity, String loginName) {
        entity.setCreateBy(loginName);
        entity.setCreateTime(DateUtils.getDate());
        entity.setOperatorCode(loginName);
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<FcInstrateAdjust, String> getEntityDao() {
        return fcInstrateAdjustDao;
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<FcInstrateAdjustVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(corpServiceI.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_RATE_ADJUST, user.getId()), "f.net_id", true);
        sql.append(corpAuthSql);
        return fcInstrateAdjustDao.getDataTablesUnion(dataTablesInput, sql.toString());
    }
    /**
     * 获取资金池数据集
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput<FcInstrateAdjustVO> getDataTablesUnionForPool(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String resource = SysResourceConstant.NETBANK_BASICDATA_FCINSTRATEADJUST_APPLY;
        List<FcCorpPoolDTO> fcCorpPoolDTOList = fcCorpPoolServiceI.getUserCorpPools(user, resource);
        List<String> netList = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(fcCorpPoolDTOList)) {
            for (FcCorpPoolDTO dto : fcCorpPoolDTOList) {
                netList.add(dto.getId());
            }
        }
        String corpSql = SqlUtil.getInSql(netList, "f.net_id", true);
        sql.append(" and ").append(corpSql);
        DataTablesOutput<FcInstrateAdjustVO> dataTablesUnionForPool = fcInstrateAdjustDao.getDataTablesUnionForPool(dataTablesInput, sql.toString());
        return dataTablesUnionForPool;
    }
    /**
     * 查询所有的待复核的调整利率
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<FcInstrateAdjustVO> getWaitCheckDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        return fcInstrateAdjustDao.getWaitCheckDataTablesUnion(dataTablesInput, user.getLoginName());
    }
    /**
     * 查询资金池所有的待复核的调整利率
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<FcInstrateAdjustVO> getWaitCheckDataTablesPoolUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        return fcInstrateAdjustDao.getWaitCheckDataTablesPoolUnion(dataTablesInput, user.getLoginName());
    }
    /**
     * 根据id 找到 当前的调整记录对象信息
     *
     * @param id
     * @return
     */
    public FcInstrateAdjustSimpleVO getFcInstrateAdjust(String id, String poolSign) {
        FcInstrateAdjust adjust = fcInstrateAdjustDao.get(id, false);
        FcAccDTO fcAccDTO = null;
        if ("1".equals(poolSign)) {
            fcAccDTO = fcAccServiceI.getPoolAccDTOByAccount(adjust.getAccount());
        } else {
            fcAccDTO = fcAccServiceI.getAccDTOByAccount(adjust.getAccount());
        }
        FcInstrateAdjustSimpleVO vo = new FcInstrateAdjustSimpleVO();
        BeanUtils.copyNotNullProperties(adjust, vo);
        vo.setInterestName(dictServiceI.get("Common.InstrtType", vo.getInstrtType()));
        vo.setAccName(fcAccDTO.getAccName());
        vo.setCorpId(fcAccDTO.getCorpId());
        vo.setCorpName(fcAccDTO.getCorpName());
        if ("1".equals(poolSign)) {
            vo.setNetId(fcAccDTO.getNetPoolId());
            vo.setNetName(fcAccDTO.getNetPoolName());
            vo.setSubjectId(fcAccDTO.getSubjectPoolId());
            vo.setSubjectName(fcAccDTO.getSubjectPoolName());
        } else {
            vo.setNetId(fcAccDTO.getNetId());
            vo.setNetName(fcAccDTO.getNetName());
            vo.setSubjectId(fcAccDTO.getSubjectId());
            vo.setSubjectName(fcAccDTO.getSubjectName());
        }
        vo.setCurName(fcAccDTO.getCurName());
        List<FcInstrateAdjust> fcAdjusts = fcInstrateAdjustDao.listFcInsrateAdjust(new String[]{adjust.getAccount()}, adjust.getInstrtType());
        if (fcAdjusts == null) {
            //如果没调整过利率，就取表中取最新利率，利率时间等信息
            vo.setLastAdjustTime(null);
            String blnctrAccInstrtType = adjust.getAccount() + "," + adjust.getInstrtType();
            vo.setOldInstrate(getLastFcInstrateAdjustValues(blnctrAccInstrtType, fcAccDTO));
        } else {
            FcInstrateAdjust fc = fcAdjusts.get(0);
            vo.setLastAdjustTime(fc.getUpdateTime() == null ? fc.getCreateTime() : fc.getUpdateTime());
        }
        return vo;
    }
    /**
     * 保存
     *
     * @param adjusts
     */
    public void save(List<FcInstrateAdjust> adjusts, ICurrentUser user, String poolSign) {
        //如果是资金池判断是否自动复核
        if (StringUtils.isNotBlank(poolSign) && PoolContant.POOL_SIGN_1.equals(poolSign)) {
            int poolAccRateAdjust = configServiceI.getConfig("pool", "poolAccRateAdjust").toInteger(0).intValue();
            for (FcInstrateAdjust adjust : adjusts) {
                adjust.setCreateBy(user.getLoginName());
                adjust.setCreateTime(DateUtils.getDate());
                adjust.setOperatorCode(user.getLoginName());
                if (poolAccRateAdjust == 1) {
                    adjust.setStatus(CommonConstant.STATUE_CHECK);
                } else {
                    adjust.setStatus(CommonConstant.STATUE_WAIT_CHECK);
                }
            }
        } else {
            for (FcInstrateAdjust adjust : adjusts) {
                adjust.setCreateBy(user.getLoginName());
                adjust.setCreateTime(DateUtils.getDate());
                adjust.setOperatorCode(user.getLoginName());
                adjust.setStatus(CommonConstant.STATUE_WAIT_CHECK);
            }
        }
        adjusts.stream().forEach(vo ->{
            vo.setUpdateTime(new Date());
            vo.setUpdateBy(user.getUsername());
        });
        super.batchSave(adjusts);
    }
    /**
     * 编辑
     *
     * @param adjust
     */
    public void modify(FcInstrateAdjust adjust, ICurrentUser user, String poolSign) {
        FcInstrateAdjust fc = fcInstrateAdjustDao.get(adjust.getId());
        BeanUtils.copyNotNullProperties(adjust, fc);
        fc.setUpdateBy(user.getLoginName());
        fc.setUpdateTime(DateUtils.getDate());
        fc.setOperatorCode(user.getLoginName());
        //如果是资金池判断是否自动复核
        if (StringUtils.isNotBlank(poolSign) && PoolContant.POOL_SIGN_1.equals(poolSign)) {
            int poolAccRateAdjust = configServiceI.getConfig("pool", "poolAccRateAdjust").toInteger(0).intValue();
            if (poolAccRateAdjust == 1) {
                fc.setStatus(CommonConstant.STATUE_CHECK);
            } else {
                fc.setStatus(CommonConstant.STATUE_WAIT_CHECK);
            }
        } else {
            fc.setStatus(CommonConstant.STATUE_WAIT_CHECK);
        }
        fc.setNewInstrate(adjust.getNewInstrate());
        fc.setAdjustTime(adjust.getAdjustTime());
        //innerAccountService.updateRate(adjust.getAccount(),adjust.getNewInstrate(),adjust.getInstrtType());
        this.update(fc);
    }
    /**
     * 复核操作
     *
     * @param adjust
     */
    public void checkFcInAdjusts(FcInstrateAdjust adjust, ICurrentUser user) {
        String[] ids = adjust.getId().split(",");
        Date date = DateUtils.getDate();
        //修改调整表状态 通过or 打回
        fcInstrateAdjustDao.changeStatus(ids, user.getLoginName(), adjust.getStatus(), date);
        //复合通过修改 内部账号对应字段利率
        if (CommonConstant.STATUE_CHECK.equals(adjust.getStatus())) {
            for (int i = 0; i < ids.length; i++) {
                FcInstrateAdjust fcInstrateAdjust = fcInstrateAdjustDao.get(ids[i], false);
                innerAccountService.updateRate(fcInstrateAdjust.getAccount(), fcInstrateAdjust.getNewInstrate(), fcInstrateAdjust.getInstrtType());
            }
        }
    }
    /**
     * 根据参数查询可以初始化的内部账号
     *
     * @param account
     * @param netId
     * @param corpId
     * @return
     */
    public List<FcInstrateAdjustSimpleVO> getFcInitAccsByParams(String netId, String corpId, String subjectId,
                                                                String curId, String instrtType, String account) {
        List<FcInstrateAdjustSimpleVO> fcAccs = new ArrayList<FcInstrateAdjustSimpleVO>();
        String[] instrtTypes = instrtType.split(",");
        FcInstrateAdjustSimpleVO fcAcc_ = null;
        //利率类型
        List<String> accountList = fcInstrateAdjustDao.getFilterAccount(netId, corpId, curId, subjectId);
        for (int iType = 0; iType < instrtTypes.length; iType++) {
            List<FcAccDTO> list = fcAccServiceI.getFcAccByParams(netId, corpId, subjectId, curId, instrtTypes[iType], account);
            if (list == null || list.size() == 0) {
                continue;
            }
            List<String> blnctrAccList = new ArrayList<>();
            List<FcAccDTO> listAll = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                FcAccDTO fcAcc = list.get(i);
                if (!accountList.contains(fcAcc.getBlnctrAcc())) {
                    blnctrAccList.add(fcAcc.getBlnctrAcc());
                    listAll.add(fcAcc);
                }
            }
            if (blnctrAccList.size() == 0) {
                continue;
            }
            //获取过滤后的可调整账号列表
            String[] blnctrAccs = new String[blnctrAccList.size()];
            blnctrAccList.toArray(blnctrAccs);
            //通过账号列表和 利率类型 获取最新一次变更记录
            Hashtable<String, FcInstrateAdjust> hashtableInstrate = this.fcInstrateAdjustDao.getLastFcInsrateAdjust(
                    blnctrAccs, instrtTypes[iType]);
            for (int i = 0; i < listAll.size(); i++) {
                FcAccDTO fcAcc = listAll.get(i);
                fcAcc_ = new FcInstrateAdjustSimpleVO();
                String blnctrAccInstrtType = fcAcc.getBlnctrAcc() + "," + instrtTypes[iType];
                FcInstrateAdjust fcInstrateAdjust = hashtableInstrate.get(fcAcc.getBlnctrAcc());
                BeanUtils.copyNotNullProperties(fcAcc, fcAcc_);
                if (fcInstrateAdjust != null) {//在利率调整表中取上一次调整日期和根据利率类型来取利率的值
                    //如果该账号在利率调整表中有记录，就取利率调整表中的信息
                    fcAcc_.setOldInstrate(fcInstrateAdjust.getNewInstrate());
                    fcAcc_.setAdjustTime(fcInstrateAdjust.getAdjustTime());
                    fcAcc_.setLastAdjustTime(fcInstrateAdjust.getUpdateTime() == null ? fcInstrateAdjust.getCreateTime() : fcInstrateAdjust.getUpdateTime());
                } else {
                    //如果没调整过利率，就取表中取最新利率，利率时间等信息
                    fcAcc_.setOldInstrate(getLastFcInstrateAdjustValues(blnctrAccInstrtType, fcAcc));
                    fcAcc_.setAdjustTime(null);
                    fcAcc_.setLastAdjustTime(null);
                }
                fcAcc_.setAccount(fcAcc.getBlnctrAcc());
                fcAcc_.setInstrtType(instrtTypes[iType]);
                fcAcc_.setInterestName(dictServiceI.get("Common.InstrtType", instrtTypes[iType]));
                fcAccs.add(fcAcc_);
            }
        }
        //return fcInstrateAdjustDao.getFcInitAccsByParams(joinSql.toString(),status);
        SortList<FcInstrateAdjustSimpleVO> sortList = new SortList<FcInstrateAdjustSimpleVO>();
        sortList.Sort(fcAccs, "getAccount", "getInstrtType", null);
        return fcAccs;
    }
    /**
     * 获取某账号最近的一次利率调整值
     *
     * @param blnctrAccInstrtType
     * @param fcAcc
     * @return
     */
    public BigDecimal getLastFcInstrateAdjustValues(String blnctrAccInstrtType, FcAccDTO fcAcc) {
        String[] temp = blnctrAccInstrtType.split(",");
        BigDecimal maxInstrate = BigDecimal.ZERO;
        if ("1".equals(temp[1])) {
            maxInstrate = fcAcc.getInterestRate();
        } else if ("2".equals(temp[1]) || "3".equals(temp[1])) {
            maxInstrate = fcAcc.getOverdueRate();
        } else if ("4".equals(temp[1]) || ("6".equals(temp[1]))) {
            maxInstrate = fcAcc.getOverdraftRate();
        } else if ("7".equals(temp[1])) {
            maxInstrate = fcAcc.getDelayRate();
        } else if ("5".equals(temp[1])) {
            //TODO 预留
            String xdSql = "select t.agreed_rate from fc_agreed_deposit t where t.terminate_time is null and t.blnctr_acc =?";
        }
        return maxInstrate;
    }
    public List<FcInstrateAdjustVO> getVOByInstrateId(String instrateId) {
        return fcInstrateAdjustDao.getVOByInstrateId(instrateId);
    }
    public Map<String, InstrateAdjustBean> queryInterestAdjustsByAcc(String blnctrAcc) {
        return fcInstrateAdjustDao.queryInterestAdjustsByAcc(blnctrAcc);
    }
    public List<AccInstrateBean> queryInstRateAdjustInfos(String netCode, String accTypeCode, Date endInstDate, Integer[] corpIds, String account) {
        return fcInstrateAdjustDao.queryInstRateAdjustInfos(netCode, accTypeCode, endInstDate, corpIds, account);
    }
}
