package com.bytter.biz.pool.service;
import com.bytter.biz.pool.dao.FcCorpPoolDao;
import com.bytter.biz.pool.entity.FcCorpPool;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.InnerAccountConstant;
import com.bytter.core.constant.PoolContant;
import com.bytter.core.enums.NetbankMessageEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.FcCorpPoolVO;
import com.bytter.system.producer.InitNetDayCloseMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Service
 * @Description: 资金池单位数据类
 * @date 2019-04-30 16:44:38
 */
@Service("fcCorpPoolService")
@Transactional
public class FcCorpPoolService extends BaseService<FcCorpPool, String> implements ICorpDeleteVerifyService {
    @Autowired
    private FcCorpPoolDao fcCorpPoolDao;
    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private IFcAccService fcAccServiceI;
    @Autowired
    private IBtBankAccService btBankAccServiceI;
    @Autowired
    private IMultiLangService multiLangServiceI;
    @Autowired
    private InitNetDayCloseMQProducer initNetDayCloseMQProducer;
    @Override
    public BaseDao<FcCorpPool, String> getEntityDao() {
        return fcCorpPoolDao;
    }
    /**
     * 根据id查找资金池
     * @param id
     * @return
     */
    public FcCorpPool getByIdCache(String id) {
        return fcCorpPoolDao.getByIdCache(id);
    }
    
    public FcCorpPool findById(String id) {
        return fcCorpPoolDao.get(FcCorpPool.class, id);
    }
    /**
     * 根据资金池代码查询资金池信息
     * @param poolCode
     * @return
     */
    public List<FcCorpPool> getFcCorpPoolByPoolCode(String poolCode){
        return fcCorpPoolDao.getFcCorpPoolByPoolCode(poolCode);
    }
    /**
     * 根据资金池名称查询资金池信息
     * @param poolName
     * @return
     */
    public List<FcCorpPool> getFcCorpPoolByPoolName(String poolName){
        return fcCorpPoolDao.getFcCorpPoolByPoolName(poolName);
    }
    /**
     * 根据id查询资金池信息
     * @param id
     * @return
     */
    public FcCorpPoolVO getFcCorpPoolById(String id){
        return fcCorpPoolDao.getFcCorpPoolById(id);
    }
    /**
     * 根据单位id查询资金池单位
     * @param corpId
     * @return
     */
    public List<FcCorpPool> getFcCorpPoolByCorpId(String corpId,String poolSign){
        return fcCorpPoolDao.getFcCorpPoolByCorpId(corpId,poolSign);
    }
    /**
     * 保存资金池信息
     * @param fcCorpPool
     * @param user
     */
    public void saveCorpPool(FcCorpPool fcCorpPool,ICurrentUser user){
        //说明为子单位
        if(StringUtils.isNotBlank(fcCorpPool.getPoolId())){
            fcCorpPool.setPoolSign(PoolContant.POOL_SIGN_0);
            //检验资金池不允许增加同一个子单位
            List<FcCorpPool> corpPool = fcCorpPoolDao.listByPoolId(fcCorpPool.getPoolId());
            for(FcCorpPool pool : corpPool){
                if(pool.getCorpId().equals(fcCorpPool.getCorpId())){
                    throw new ParameterException(NetbankMessageEnum.POOL_POOLCODE_MESSAGE_CHILDCORP_SAME);
                }
            }
            //检验同单位资金池不允许增加相同子单位
            List<FcCorpPool> fp = fcCorpPoolDao.corpIdByPoolId(fcCorpPool.getPoolId());
            for(FcCorpPool fc : fp){
                if(fcCorpPool.getCorpId().equals(fc.getCorpId())){
                    throw new ParameterException(NetbankMessageEnum.POOL_POOLCODE_MESSAGE_CORP_SAME);
                }
            }
        }else{
            List<FcCorpPool> poolCodeList = this.getFcCorpPoolByPoolCode(fcCorpPool.getPoolCode());
            List<FcCorpPool> poolNameList = this.getFcCorpPoolByPoolName(fcCorpPool.getPoolName());
            if(!EmptyUtils.isEmptyList(poolCodeList) && poolCodeList.size() > 0){
                throw new ParameterException(NetbankMessageEnum.POOL_POOLCODE_MESSAGE_POOLCODE_UNIQUE);
            }
            if(!EmptyUtils.isEmptyList(poolNameList) && poolNameList.size() > 0){
                throw new ParameterException(NetbankMessageEnum.POOL_POOLNAME_MESSAGE_POOLNAME_UNIQUE);
            }
            CorpDTO corpDTO = corpServiceI.getCorpByCode(fcCorpPool.getPoolCode());
            if(!EmptyUtils.isEmpty(corpDTO)){
                if(corpDTO.getType().intValue() == 0){
                    throw new ParameterException(NetbankMessageEnum.POOL_POOLCODE_MESSAGE_NET_SAME);
                }
            }
            fcCorpPool.setPoolSign(PoolContant.POOL_SIGN_1);
        }
        this.saveEntity(fcCorpPool);
        //只有主体单位才初始化日结日期
        if(StringUtils.isBlank(fcCorpPool.getPoolId())){
            //初始化资金池主体日结日期
//            fcDayCloseService.initPoolNetCloseDate(fcCorpPool.getId(),user.getId()); //不能依赖其它业务服务
            InitNetDayCloseMessageDTO messageDto = InitNetDayCloseMessageDTO.builder().netId(fcCorpPool.getId()).userId(user.getId()).build();
            messageDto.setMsgType(InitNetDayCloseMessageDTO.Msg.P);
            initNetDayCloseMQProducer.sendMessage(messageDto);
        }
    }
    /**
     * 修改资金池
     * @param fcCorpPool
     * @param user
     */
    public void modifyCorpPool(FcCorpPool fcCorpPool,ICurrentUser user){
        if(PoolContant.POOL_SIGN_1.equals(fcCorpPool.getPoolSign())){
            List<FcCorpPool> pool = this.getFcCorpPoolByPoolName(fcCorpPool.getPoolName());
            if(EmptyUtils.isEmptyList(pool) && pool.size() > 0){
                throw new ParameterException(NetbankMessageEnum.POOL_POOLNAME_MESSAGE_POOLNAME_UNIQUE);
            }
        }
        FcCorpPool fcp = this.get(fcCorpPool.getId());
        BeanUtils.copyNotNullProperties(fcCorpPool, fcp);
        this.saveOrUpdate(fcp);
    }
    /**
     * 获取所有的菜单，排序为树形顺序机构
     *
     * @return
     */
    public List<FcCorpPoolVO> getTreeTable(String poolCode,String poolName, String status) {
        List<FcCorpPool> list = fcCorpPoolDao.listAllPool(poolCode,poolName,status);
        List<FcCorpPool> corpList = null;
        if(StringUtils.isNotBlank(status)){
            List<FcCorpPool> showList = new ArrayList<>();
            for(FcCorpPool listTemp : list){
                //如果是子单位,查询资金池是否也存在，如果不存在，把poolId设置为空，自己当成一级单位
                if(PoolContant.POOL_SIGN_0.equals(listTemp.getPoolSign())){
                    FcCorpPool parentCorp = fcCorpPoolDao.get(listTemp.getPoolId());
                    if(!listTemp.getStatus().equals(parentCorp.getStatus())){
                        listTemp.setPoolId("");
                    }
                }
                showList.add(listTemp);
            }
            corpList = TreeUtil.coverTreeTable(showList);
        }else{
            corpList = TreeUtil.coverTreeTable(list);
        }
        List<FcCorpPoolVO> fcCorpPoolVOS = BeanUtils.copyListProperties(corpList, FcCorpPoolVO.class);
        for(FcCorpPoolVO vo : fcCorpPoolVOS){
            CorpDTO dto = corpServiceI.get(vo.getCorpId());
            vo.setCorpCode(dto.getCode());
            vo.setCorpName(dto.getName());
        }
        // //inject(fcCorpPoolVOS);
        return fcCorpPoolVOS;
    }
    /**
     * 根据id查询资金池下面的子单位个数
     * @param poolId
     * @return
     */
    public List<FcCorpPool> listByPoolId(String poolId){
        return fcCorpPoolDao.listByPoolId(poolId);
    }
    /**
     * 根据主体单位查询资金池单位下面的子单位
     * @param poolId
     * @return
     */
    public List<FcCorpPool> corpIdByPoolId(String poolId){
        return fcCorpPoolDao.corpIdByPoolId(poolId);
    }
    /**
     * 获取资源权限的资金池
     * @param resource
     * @return
     */
    public List<FcCorpPoolVO> getUserCorpPools(ICurrentUser user, String resource){
        StringBuilder sql = new StringBuilder();
        if(!StringUtils.isBlank(resource)) {
            String corpAuthSql = SqlUtil.getInSql(corpServiceI.getUserCorpIds(resource, user.getId()), "f.corp_Id", true);
            sql.append(" and ").append(corpAuthSql);
        }
        return this.fcCorpPoolDao.getUserCorpPools(sql.toString());
    }
    /**
     * 获取资源权限的资金池
     * @param user
     * @param resource
     * @return
     */
    public List<FcCorpPoolVO> getUserCorpPoolChilds(ICurrentUser user, String resource, String poolId){
        StringBuilder sql = new StringBuilder();
        if(!StringUtils.isBlank(resource)) {
            String corpAuthSql = SqlUtil.getInSql(corpServiceI.getUserCorpIds(resource, user.getId()), "f.corp_Id", true);
            sql.append(" and ").append(corpAuthSql);
        }
        return this.fcCorpPoolDao.getUserCorpPoolChilds(sql.toString(), poolId);
    }
    /**
     * 根据资金池ID和子单位Id获取子单位信息
     * @param poolId
     * @param corpId
     * @return
     */
    public FcCorpPoolVO getChildCorpByPoolId(String poolId, String corpId){
        return this.fcCorpPoolDao.getChildCorpByPoolId(poolId, corpId);
    }
    /**
     * 根据父单位id和单位id查找资金池信息
     * @param parentId
     * @param corpId
     * @return
     */
    public FcCorpPool getPoolInfo(String parentId,String corpId){
        FcCorpPool parent = null;
        List<FcCorpPool> childList = fcCorpPoolDao.getPoolInfo(PoolContant.POOL_SIGN_0,corpId);//子单位
        List<FcCorpPool> parentList = fcCorpPoolDao.getPoolInfo(PoolContant.POOL_SIGN_1,parentId);//主体单位
        for(FcCorpPool childPool : childList){
            for(FcCorpPool parentPool : parentList){
                if(childPool.getPoolId().equals(parentPool.getId())){
                    parent = parentPool;
                    break;
                }
            }
        }
        return parent;
    }
    /**
     * 查询所有资金池信息
     * @return
     */
    public List<FcCorpPoolVO> getAllCorpPool(){
        return fcCorpPoolDao.getAllCorpPool();
    }
    /**
     * 判断资金池内部账户余额必须为0，且不能存在未结清的利息
     * @param corpId
     * @param user
     * @return
     */
    public boolean vaildAcc(String corpId,ICurrentUser user) {
        boolean flag = false;
        BigDecimal tellerMoney = null;
        List<String> corpIds = new ArrayList<>();
        corpIds.add(corpId);
        List<FcAccDTO> fcAccDTO = fcAccServiceI.listFcAccPoolByCorpIds(corpIds, InnerAccountConstant.ACC_STATE_NORMAL,InnerAccountConstant.POOL_SIGN_1);
        for(FcAccDTO dto : fcAccDTO){//TODO 在结算业务方法里处理
//            flag = fcOnlineLedgerService.validAmt(dto.getBlnctrAcc());
//            tellerMoney = fcInterestService.calTellerMoneyInterest(dto.getBlnctrAcc(), BigDecimal.ZERO,null,null,user);
//            if(flag && (tellerMoney != null && tellerMoney.compareTo(BigDecimal.ZERO) > 0)){
//                flag = true;
//                break;
//            }
        }
        return flag;
    }
    /**
     * 根据单位查询状态为(开户待复核、开户复核打回、改户待复核、改户复核打回、销户待复核、销户复核打回)账号
     * @param poolId
     * @param corpId
     * @param isFilter 是否过滤状态 true过滤，flase 不过滤
     * @return
     */
    public boolean fcAccUse(String poolId, String corpId, boolean isFilter){
        boolean flag = false;
        List<FcAccDTO> fcAccDTOList = fcAccServiceI.listFcAccPoolByCorpId(poolId,corpId,isFilter);
        if(!EmptyUtils.isEmptyList(fcAccDTOList)){
            flag = true;
        }
        return flag;
    }
    /**
     * 根据单位查询账号是否有关联关系
     * @param corpId
     * @param corpId
     * @return
     */
    public boolean relevance(String poolId, String corpId){
        boolean flag = false;
        List<String> corpList = new ArrayList<>();
        corpList.add(corpId);
        List<BtBankAccDTO> list =  btBankAccServiceI.getListByCorpIds(corpList);
        List<BtBankAccDTO> bankAccDTOS = BeanUtils.copyListProperties(list, BtBankAccDTO.class);
        if(!EmptyUtils.isEmptyList(bankAccDTOS)){
            for(BtBankAccDTO dto : bankAccDTOS){
                BtBankAccDTO btBankAccDTO = btBankAccServiceI.getBankAccById(dto.getId());
                if(StringUtils.isNotBlank(btBankAccDTO.getCounterAccId())){
                    FcAccDTO fcAccDTO = fcAccServiceI.getAccDTOByAccount(btBankAccDTO.getCounterAccId());
                    //关联关系存在并且内部账户是该资金池的账号说明该资金池维护过关系，如果不等，说明不是该资金池关系
                    if(fcAccDTO != null && poolId.equals(fcAccDTO.getNetId())){
                        flag = true;
                        break;
                    }
                }
                if(StringUtils.isNotBlank(btBankAccDTO.getPrentAccId())){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位是资金池主体单位或子单位，不允许删除！
            int num = this.fcCorpPoolDao.getPoolCountByCorpId(corpId);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_POOL,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DEL_POOL_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    public List<FcCorpPoolDTO> getFcCorpPoolByIds(List<String> ids) {
        return fcCorpPoolDao.getFcCorpPoolByIds(ids);
    }
    
    public List<FcCorpPoolDTO> getFcCorpPoolByPoolIds(List<String> ids) {
        return fcCorpPoolDao.getFcCorpPoolByPoolIds(ids);
    }
    
    
    public Map<String, String> getPoolNameByIds(List<String> ids) {
        List<FcCorpPoolDTO> lists = fcCorpPoolDao.getPoolNameByIds(ids);
        return lists.stream().collect(Collectors.toMap(FcCorpPoolDTO::getId,FcCorpPoolDTO::getPoolName));
    }
}
