package com.bytter.increment.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.PoolContant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.increment.api.constant.AgreedConstant;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.domain.dao.FcAgreedDepositDao;
import com.bytter.increment.domain.dao.FcAgreedDetailDao;
import com.bytter.increment.domain.entity.FcAgreedDeposit;
import com.bytter.increment.domain.entity.FcAgreedDetail;
import com.bytter.increment.domain.vo.FcAccVO;
import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.FcCorpPoolVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Service
 * @Description: 协定存款登记表数据类
 * @date 2019-05-10 16:44:38
 */
@Service("fcAgreedDepositService")
@Transactional
public class FcAgreedDepositService extends BaseService<FcAgreedDeposit, String> implements ICorpDisableVerifyService {
    @Autowired
    private FcAgreedDepositDao fcAgreedDepositDao;
    @Autowired
    private FcAgreedDetailDao fcAgreedDetailDao;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IInnerAccountService innerAccountService;
    @Autowired
    private FcAgreedDetailService fcAgreedDetailService;
    @DubboReference(check = false)
	private IFcCorpPoolService fcCorpPoolService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference
    IUserService userService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    /**
     * 保存
     *
     * @param entity
     */
    public void save(FcAgreedDeposit entity) {
        super.saveEntity(entity);
    }
    @Override
    public BaseDao<FcAgreedDeposit, String> getEntityDao() {
        return fcAgreedDepositDao;
    }
    /**
     * 查询业务类型列表列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput findListBysql(DataTablesInput input,ICurrentUser user) {
        String opType = (String) input.getFormdata().get("opType");
        // 获取对应资源
        String resource = "";
        StringBuilder joinSql = new StringBuilder();
        if(AgreedConstant.BUS_TYPE_1.equals(opType)){
            resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_QUERY;
        }else if(AgreedConstant.BUS_TYPE_2.equals(opType)){
            resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_QUERY;
        }else{
            resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_QUERY;
        }
        List<String> corpList = new ArrayList<>();
        List<FcCorpPoolDTO> pools = new ArrayList<>();
        //资金池不为空
        String poolId = (String) input.getFormdata().get("eqs_netId");
        if (!StringUtils.isBlank(poolId)) {
            pools.add(fcCorpPoolService.getById(poolId));
        } else {
            pools = fcCorpPoolService.getUserCorpPools(user, resource);
        }
        for (FcCorpPoolDTO pool : pools) {
            List<FcCorpPoolDTO> poolChilds = fcCorpPoolService.getAllUserCorpPoolChilds(user, resource, pool.getId());
            if (!EmptyUtils.isEmptyList(poolChilds)) {
                for (FcCorpPoolDTO dto : poolChilds) {
                    corpList.add(dto.getCorpId());
                }
            }
        }
        String corpSql = SqlUtil.getInSql(corpList, "d.corp_id", true);
        joinSql.append(" and ").append(corpSql);
        DataTablesOutput dataTablesOutput= fcAgreedDepositDao.findListBysql(input,user,joinSql.toString());
        List<FcAgreedDepositVO> fcAgreedDepositVOList=dataTablesOutput.getData();
        if(fcAgreedDepositVOList.size()>0) {
            List<String> loginNameS = new ArrayList<>();
            String fcAccIdS = "";
            for (FcAgreedDepositVO vo : fcAgreedDepositVOList) {
                loginNameS.add(vo.getCreateBy());
                fcAccIdS+="'"+vo.getAccId()+"',";
            }
            //查询用户
            Map<String, String> userMap = userService.getUserNameByLoginNames(loginNameS);
            //查询内部账号信息
            String condition=" and t.id in("+fcAccIdS.substring(0,fcAccIdS.length()-1)+")";
            List<FcAccDTO> fcAccDTOList = fcAccService.queryBySomeCondPgd(condition);
            Map<String, FcAccDTO> fcAccMap = fcAccDTOList.stream()
                    .collect(Collectors.toMap(FcAccDTO::getId, fcAccDTO -> fcAccDTO));
            for (FcAgreedDepositVO vo : fcAgreedDepositVOList) {
                //查询fc_corp_pool
                FcCorpPoolVO fcCorpPoolVO = fcCorpPoolService.getFcCorpPoolById(vo.getNetId());
//                vo.setNetName(fcCorpPoolVO.getPoolName());
                vo.setUserName(userMap.get(vo.getCreateBy()));
//                if (!StringUtil.isBlank_new(vo.getAccId())) {
//                    FcAccDTO fcAccDTO = fcAccMap.get(vo.getAccId());
//                    vo.setAccName(fcAccDTO.getAccName());
//                    vo.setCurId(fcAccDTO.getCurId());
//                    vo.setCurName(fcAccDTO.getCurName());
//                    vo.setCorpName(fcAccDTO.getCorpName());
//                }
            }
        }
            return dataTablesOutput;
    }
    /**
     * 查询协定存款登记表复核维护列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput checkListBysql(DataTablesInput input, ICurrentUser user) {
        // 两个页面调用
        String opType = (String) input.getFormdata().get("opType");
        String resource = "";
        StringBuilder joinSql = new StringBuilder();
        if(AgreedConstant.BUS_TYPE_1.equals(opType)){
            resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POSCHECK_QUERY;
        }else if(AgreedConstant.BUS_TYPE_2.equals(opType)){
            resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERMCHECK_QUERY;
        }else{
            resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REGCHECK_QUERY;
        }
        List<String> corpList = new ArrayList<>();
        List<FcCorpPoolDTO> pools = new ArrayList<>();
        //资金池不为空
        String poolId = (String) input.getFormdata().get("eqs_netId");
        if (!StringUtils.isBlank(poolId)) {
            pools.add(fcCorpPoolService.getById(poolId));
        } else {
            pools = fcCorpPoolService.getUserCorpPools(user, resource);
        }
        for (FcCorpPoolDTO pool : pools) {
            List<FcCorpPoolDTO> poolChilds = fcCorpPoolService.getAllUserCorpPoolChilds(user, resource, pool.getId());
            if (!EmptyUtils.isEmptyList(poolChilds)) {
                for (FcCorpPoolDTO dto : poolChilds) {
                    corpList.add(dto.getCorpId());
                }
            }
        }
        String corpSql = SqlUtil.getInSql(corpList, "d.corp_id", true);
        joinSql.append(" and ").append(corpSql);
        DataTablesOutput dataTablesOutput = fcAgreedDepositDao.checkListBysql(input,user,joinSql.toString());
        List<FcAgreedDepositVO> dataList = dataTablesOutput.getData();
        //获取内部账户信息
        //获取资金池信息
        //fcCorpPoolService
        //获取有效的网点、单位
        List<CorpDTO> list = corpService.listEffectiveCorp();
        //获取所有的货币资料信息
        //获取所有用户信息
        List<BtCurrencyDTO> btCurrencyDTOS = btCurrencyService.listAllBtCurrency();
        for(FcAgreedDepositVO fcAgreedDepositVO : dataList){
        }
        dataTablesOutput.setData(dataList);
        return dataTablesOutput;
    }
    /**
     * 根据资金池或开户单位查找内部账号
     * @param poolId
     * @param corpId
     * @return
     */
    public List<FcAccVO> getBlnctrAcc(ICurrentUser user, String poolId, String corpId, String busType){
        String accAuthSql = SqlUtil.getInSql(accountGrantService.listByMultiVal(user.getId(), corpId, null, null, "4", AccountGrantConstant.TRANSFER_ACCOUNT), "a.id", true);
        List<String> listAccId = fcAgreedDepositDao.getAccIdForDeposit(busType);
        List<FcAccDTO> blnctrAcc = fcAccService.getBlnctrAcc(user.getId(), poolId, corpId, busType, listAccId,accAuthSql);
        return BeanUtils.copyListProperties(blnctrAcc, FcAccVO.class);
    }
    /**
     * 根据ID查询协定存款登记信息
     *
     * @param id
     * @return
     */
    public FcAgreedDepositVO getByUnion(String id) {
        FcAgreedDepositVO vo= fcAgreedDepositDao.getByUnion(id);
        if(!StringUtil.isBlank_new(vo.getAccId())) {
            //查询内部账号信息
            String condition = " and t.id ='" + vo.getAccId() + "'";
            List<FcAccDTO> fcAccDTOList = fcAccService.queryBySomeCondPgd(condition);
            for (FcAccDTO fcAccDTO : fcAccDTOList) {
                vo.setAccName(fcAccDTO.getAccName());
                vo.setCurId(fcAccDTO.getCurId());
                vo.setCurName(fcAccDTO.getCurName());
                vo.setCorpName(fcAccDTO.getCorpName());
            }
        }
        if(!StringUtil.isBlank_new(vo.getNetId())) {
            //查询fc_corp_pool
            FcCorpPoolVO fcCorpPoolVO = fcCorpPoolService.getFcCorpPoolById(vo.getNetId());
            vo.setNetName(fcCorpPoolVO.getPoolName());
        }
        return vo;
    }
    /**
     * 根据账户id查找协定信息
     * @param accId
     * @return
     */
    public FcAgreedDepositVO getAgreeInfo(String accId,String busType){
        List<FcAgreedDepositVO> list = fcAgreedDepositDao.getAgreeInfo(accId,busType);
        FcAgreedDepositVO vo = null;
        if(!EmptyUtils.isEmptyList(list)){
            vo = list.get(0);
            List<FcAgreedDetail> details = fcAgreedDetailDao.getDetailByAgreedIdLineNo(vo.getId(),null);
            vo.setDetailList(details);
        }
        return vo;
    }
    /**
     * 保存协定信息
     * @param fcAgreedDepositVO
     * @param user
     */
    public void saveFcAgreedDeposit(FcAgreedDepositVO fcAgreedDepositVO, ICurrentUser user) {
        FcAgreedDeposit fcAgreedDeposit = new FcAgreedDeposit();
        BeanUtils.copyNotNullProperties(fcAgreedDepositVO, fcAgreedDeposit);
        fcAgreedDeposit.setCreateBy(user.getLoginName());
        fcAgreedDeposit.setCreateDate(DateUtils.getDate());
        fcAgreedDeposit.setPoolSign(PoolContant.POOL_SIGN_1);
        fcAgreedDeposit.setBillStatus(AgreedConstant.BILL_STATUS_10);
        FcCorpPoolDTO corpPoolDTO= fcCorpPoolService.getById(fcAgreedDeposit.getNetId());
        fcAgreedDeposit.setNetName(corpPoolDTO.getPoolName());
        String corpName=corpService.getCorpName(fcAgreedDeposit.getCorpId());
        fcAgreedDeposit.setCorpName(corpName);
        FcAccDTO fcAccDTO = fcAccService.getPoolAccDTOByAccount(fcAgreedDeposit.getBlnctrAcc());//查询账户
        fcAgreedDeposit.setAccName(fcAccDTO.getAccName());
        BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(fcAgreedDeposit.getCurId());
        fcAgreedDeposit.setCurName(btCurrencyDTO.getCurName());
        if (AgreedConstant.BUS_TYPE_1.equals(fcAgreedDepositVO.getOpType())) {
            fcAgreedDeposit.setBusType(AgreedConstant.BUS_TYPE_1);
        } else if (AgreedConstant.BUS_TYPE_2.equals(fcAgreedDepositVO.getOpType())) {
            fcAgreedDeposit.setBusType(AgreedConstant.BUS_TYPE_2);
        } else {
            fcAgreedDeposit.setBusType(AgreedConstant.BUS_TYPE_0);
        }
        if(AgreedConstant.STATUE_TEMP.equals(fcAgreedDeposit.getStatus())){
            fcAgreedDeposit.setStatus(AgreedConstant.STATUE_TEMP);
        }else {
            //延期
            if(AgreedConstant.BUS_TYPE_1.equals(fcAgreedDepositVO.getOpType())){
                int agreeRegAutoCheck = configService.getConfig("pool", "agreeRegAutoCheck").toInteger(0).intValue();
                if(agreeRegAutoCheck == 1){
                    fcAgreedDeposit.setStatus(AgreedConstant.STATUE_CHECK);
                    //复核通过，更新原协定登记结束日期为延期开始日期
                    FcAgreedDeposit entity = this.get(fcAgreedDeposit.getAgreedDepositOriId());
                    entity.setEndDate(fcAgreedDeposit.getBeginDate());
                    this.updateEntity(entity);
                    fcAgreedDeposit.setBeginDate(fcAgreedDepositVO.getOldBeginDate());
                }else{
                    fcAgreedDeposit.setStatus(AgreedConstant.STATUE_WAIT_CHECK);
                }
                //终止
            }else if(AgreedConstant.BUS_TYPE_2.equals(fcAgreedDepositVO.getOpType())){
                int agreeTermAutoCheck = configService.getConfig("pool", "agreeTermAutoCheck").toInteger(0).intValue();
                if(agreeTermAutoCheck == 1){
                    fcAgreedDeposit.setStatus(AgreedConstant.STATUE_CHECK);
                    //复核通过，更新内部账号的协定存款标识为非协定
                    FcAccDTO dto = innerAccountService.getByBlnctrAcc(fcAgreedDeposit.getBlnctrAcc());
                    dto.setAgreedDepositSign(PoolContant.POOL_SIGN_0);
                    innerAccountService.updateFcAcc(dto);
                    /*复核通过，如果是终止修改单据状态为11终止，业务结束*/
                    fcAgreedDeposit.setBillStatus(AgreedConstant.BILL_STATUS_11);
                }else{
                    fcAgreedDeposit.setStatus(AgreedConstant.STATUE_WAIT_CHECK);
                }
                //登记
            }else{
                int agreeAutoCheck = configService.getConfig("pool", "agreeAutoCheck").toInteger(0).intValue();
                //自动复核状态设置为复核通过
                if(agreeAutoCheck == 1){
                    fcAgreedDeposit.setStatus(AgreedConstant.STATUE_CHECK);
                    //复核通过，更新内部账号的协定存款标识为协定
                    FcAccDTO dto = innerAccountService.getByBlnctrAcc(fcAgreedDeposit.getBlnctrAcc());
                    dto.setAgreedDepositSign(PoolContant.POOL_SIGN_1);
                    innerAccountService.updateFcAcc(dto);
                }else{
                    fcAgreedDeposit.setStatus(AgreedConstant.STATUE_WAIT_CHECK);
                }
            }
        }
        this.saveEntity(fcAgreedDeposit);
        //保存明细
        this.saveDetail(fcAgreedDeposit,fcAgreedDepositVO);
        //如果是延期/终止更新登记信息为延期
        if(AgreedConstant.BUS_TYPE_1.equals(fcAgreedDepositVO.getOpType()) || AgreedConstant.BUS_TYPE_2.equals(fcAgreedDepositVO.getOpType())){
            FcAgreedDeposit entity = this.get(fcAgreedDeposit.getAgreedDepositOriId());
            entity.setBillStatus(AgreedConstant.BILL_STATUS_11);
            this.updateEntity(entity);
        }
    }
    /**
     * 修改协定信息
     * @param fcAgreedDepositVO
     * @param user
     */
    public void updateFcAgreedDeposit(FcAgreedDepositVO fcAgreedDepositVO, ICurrentUser user) {
        FcAgreedDeposit fcAgreedDeposit= this.get(fcAgreedDepositVO.getId());
        BeanUtils.copyNotNullProperties(fcAgreedDepositVO, fcAgreedDeposit);
        fcAgreedDeposit.setUpdateBy(user.getLoginName());
        fcAgreedDeposit.setUpdateDate(DateUtils.getDate());
        fcAgreedDeposit.setBillStatus(AgreedConstant.BILL_STATUS_10);
        if(AgreedConstant.BUS_TYPE_1.equals(fcAgreedDepositVO.getOpType())){
            fcAgreedDeposit.setBusType(AgreedConstant.BUS_TYPE_1);
        }else if(AgreedConstant.BUS_TYPE_2.equals(fcAgreedDepositVO.getOpType())){
            fcAgreedDeposit.setBusType(AgreedConstant.BUS_TYPE_2);
        }else{
            fcAgreedDeposit.setBusType(AgreedConstant.BUS_TYPE_0);
        }
        if(AgreedConstant.STATUE_TEMP.equals(fcAgreedDeposit.getStatus())){
            fcAgreedDeposit.setStatus(AgreedConstant.STATUE_TEMP);
        }else {
            //延期
            if(AgreedConstant.BUS_TYPE_1.equals(fcAgreedDepositVO.getOpType())){
                int agreeRegAutoCheck = configService.getConfig("pool", "agreeRegAutoCheck").toInteger(0).intValue();
                if(agreeRegAutoCheck == 1){
                    fcAgreedDeposit.setStatus(AgreedConstant.STATUE_CHECK);
                    //复核通过，更新原协定登记结束日期为延期开始日期
                    FcAgreedDeposit entity = this.get(fcAgreedDeposit.getAgreedDepositOriId());
                    entity.setEndDate(fcAgreedDeposit.getBeginDate());
                    this.updateEntity(entity);
                    fcAgreedDeposit.setBeginDate(fcAgreedDepositVO.getOldBeginDate());
                }else{
                    fcAgreedDeposit.setStatus(AgreedConstant.STATUE_WAIT_CHECK);
                }
                //终止
            }else if(AgreedConstant.BUS_TYPE_2.equals(fcAgreedDepositVO.getOpType())){
                int agreeTermAutoCheck = configService.getConfig("pool", "agreeTermAutoCheck").toInteger(0).intValue();
                if(agreeTermAutoCheck == 1){
                    fcAgreedDeposit.setStatus(AgreedConstant.STATUE_CHECK);
                    //复核通过，更新内部账号的协定存款标识为非协定
                    FcAccDTO dto = innerAccountService.getByBlnctrAcc(fcAgreedDeposit.getBlnctrAcc());
                    dto.setAgreedDepositSign(PoolContant.POOL_SIGN_0);
                    innerAccountService.updateFcAcc(dto);
                    /*复核通过，如果是终止修改单据状态为11终止，业务结束*/
                    fcAgreedDeposit.setBillStatus(AgreedConstant.BILL_STATUS_11);
                }else{
                    fcAgreedDeposit.setStatus(AgreedConstant.STATUE_WAIT_CHECK);
                }
                //登记
            }else{
                int agreeAutoCheck = configService.getConfig("pool", "agreeAutoCheck").toInteger(0).intValue();
                //自动复核状态设置为复核通过
                if(agreeAutoCheck == 1){
                    fcAgreedDeposit.setStatus(AgreedConstant.STATUE_CHECK);
                    //复核通过，更新内部账号的协定存款标识为协定
                    FcAccDTO dto = innerAccountService.getByBlnctrAcc(fcAgreedDeposit.getBlnctrAcc());
                    dto.setAgreedDepositSign(PoolContant.POOL_SIGN_1);
                    innerAccountService.updateFcAcc(dto);
                }else{
                    fcAgreedDeposit.setStatus(AgreedConstant.STATUE_WAIT_CHECK);
                }
            }
        }
        this.saveOrUpdate(fcAgreedDeposit);
        //先删除后增加
        List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(fcAgreedDeposit.getId());
        if(!EmptyUtils.isEmptyList(details)){
            for(FcAgreedDetail detail : details){
                fcAgreedDetailService.delete(detail.getId());
            }
        }
        //保存明细
        this.saveDetail(fcAgreedDeposit,fcAgreedDepositVO);
        /*如果是到期处理/终止 更新原单据为延期/终止*/
        if (AgreedConstant.BUS_TYPE_1.equals(fcAgreedDeposit.getBusType()) || AgreedConstant.BUS_TYPE_2.equals(fcAgreedDeposit.getBusType())) {
            FcAgreedDeposit entity = this.get(fcAgreedDeposit.getAgreedDepositOriId());
            entity.setBillStatus(AgreedConstant.BILL_STATUS_11);
            this.updateEntity(entity);
        }
    }
    /**
     * 删除协定信息
     * @param id
     */
    public void delFcAgreedDeposit(String id) {
        FcAgreedDeposit fcAgreedDeposit = this.get(id);
        fcAgreedDeposit.setStatus(AgreedConstant.STATUE_DEL);
        this.updateEntity(fcAgreedDeposit);
        /*如果是到期处理/终止 更新原单据为登记*/
        if (AgreedConstant.BUS_TYPE_1.equals(fcAgreedDeposit.getBusType()) || AgreedConstant.BUS_TYPE_2.equals(fcAgreedDeposit.getBusType())) {
            FcAgreedDeposit deposit = fcAgreedDepositDao.get(fcAgreedDeposit.getAgreedDepositOriId());
            if (!EmptyUtils.isEmpty(deposit)) {
                deposit.setBillStatus(AgreedConstant.BILL_STATUS_10);
                this.updateEntity(fcAgreedDeposit);
            }
        }else{
            /*如果登记，把账号协定标识修改为非资金池*/
            FcAccDTO dto = innerAccountService.getByBlnctrAcc(fcAgreedDeposit.getBlnctrAcc());
            dto.setAgreedDepositSign(PoolContant.POOL_SIGN_0);
            innerAccountService.updateFcAcc(dto);
        }
    }
    /**
     * 保存明细
     * @param fcAgreedDeposit
     */
    public void saveDetail(FcAgreedDeposit fcAgreedDeposit, FcAgreedDepositVO vo){
        int line = 0;
        if(!EmptyUtils.isEmptyList(vo.getDetailList())){
            for(FcAgreedDetail detail : vo.getDetailList()){
                line++;
                FcAgreedDetail entity = new FcAgreedDetail();
                entity.setAgreedDepositId(fcAgreedDeposit.getId());
                entity.setMinMoney(detail.getMinMoney());
                entity.setMaxMoney(detail.getMaxMoney());
                entity.setAgreeRate(detail.getAgreeRate());
                entity.setLineNo(line);
                fcAgreedDetailService.saveEntity(entity);
            }
        }
    }
    /**
     * 操作(通过,打回）
     * @return
     */
    public void opCheck(FcAgreedDepositVO fcAgreedDepositVO,ICurrentUser user){
        FcAgreedDeposit fcAgreedDeposit = this.get(fcAgreedDepositVO.getId());
        FcAgreedDepositVO vo = fcAgreedDepositDao.getByUnion(fcAgreedDepositVO.getId());
        BeanUtils.copyNotNullProperties(fcAgreedDepositVO,vo);
        fcAgreedDeposit.setCheckerBy(user.getLoginName());
        fcAgreedDeposit.setCheckDate(DateUtils.getDate());
        if(AgreedConstant.STATUE_CHECK.intValue() == vo.getStatus()){
            fcAgreedDeposit.setStatus(AgreedConstant.STATUE_CHECK);
            FcAccDTO dto = innerAccountService.getByBlnctrAcc(fcAgreedDeposit.getBlnctrAcc());
            if(AgreedConstant.BUS_TYPE_0.equals(vo.getBusType())){
                //复核通过，更新内部账号的协定存款标识为协定
                dto.setAgreedDepositSign(PoolContant.POOL_SIGN_1);
                innerAccountService.updateFcAcc(dto);
            }
            if(AgreedConstant.BUS_TYPE_1.equals(vo.getBusType())){
                //复核通过，更新原协定登记结束日期为延期开始日期
                FcAgreedDeposit f = this.get(fcAgreedDeposit.getAgreedDepositOriId());
                f.setEndDate(fcAgreedDeposit.getBeginDate());
                this.updateEntity(f);
                fcAgreedDeposit.setBeginDate(vo.getOldBeginDate());
            }
            if(AgreedConstant.BUS_TYPE_2.equals(vo.getBusType())){
                fcAgreedDeposit.setStatus(AgreedConstant.STATUE_CHECK);
                //复核通过，更新内部账号的协定存款标识为非协定
                dto.setAgreedDepositSign(PoolContant.POOL_SIGN_0);
                innerAccountService.updateFcAcc(dto);
                /*如果是终止修改单据状态为11终止，业务结束*/
                fcAgreedDeposit.setBillStatus(InnerFinanceConstant.BILL_STATUS_11);
            }
        }else{
            fcAgreedDeposit.setStatus(AgreedConstant.STATUE_BACK);
            fcAgreedDeposit.setCheckBackReason(vo.getCheckBackReason());
        }
        this.updateEntity(fcAgreedDeposit);
    }
    /**
     * 编辑查询vo公用方法
     * @param fcAgreedDeposit
     * @return
     */
    public FcAgreedDepositVO getAgreeVo(FcAgreedDepositVO fcAgreedDeposit){
        FcAgreedDepositVO fcAgreedDepositVO = null;
        if (StringUtils.isNotEmpty(fcAgreedDeposit.getId())) {
            fcAgreedDepositVO = this.getByUnion(fcAgreedDeposit.getId());
            List<FcAgreedDetail> details = null;
            if(AgreedConstant.BUS_TYPE_1.equals(fcAgreedDeposit.getOpType())){
                details = fcAgreedDetailService.getDetailList(fcAgreedDepositVO.getAgreedDepositOriId());
            }else{
                details = fcAgreedDetailService.getDetailList(fcAgreedDeposit.getId());
            }
            fcAgreedDepositVO.setDetailList(details);
        }
        return fcAgreedDepositVO;
    }
    /**
     * 根据合同号查找协定信息
     *
     * @param contractNo
     * @return
     */
    public List<FcAgreedDepositVO> getInfoByContractNo(String contractNo) {
        return fcAgreedDepositDao.getInfoByContractNo(contractNo);
    }
    /**
     * 根据账户id,单据类型,状态查找协定信息
     *
     * @param accId
     * @param busType
     * @param status
     * @return
     */
    public List<FcAgreedDepositVO> getAgreedInfo(String accId, String busType, List<Integer> status) {
        return fcAgreedDepositDao.getAgreedInfo(accId, busType, status);
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的内部存款（协定存款）单据，不允许禁用！
            int num = this.fcAgreedDepositDao.getAgreedDepositCountByCorpId(corpId);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.CHOWMATISTIC,
                        SysConfigConstant.BusType.CHO_AGREED_DEPOSIT,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_AGREEDDEPOSIT_ERROR)));
            }
            //单位有未终止的协定存款单据，不允许禁用！
            num = this.fcAgreedDepositDao.getAgreedDepositNotEndCountByCorpId(corpId);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.CHOWMATISTIC,
                        SysConfigConstant.BusType.CHO_AGREED_DEPOSIT,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_AGREEDDEPOSIT_NOTEND_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
}
