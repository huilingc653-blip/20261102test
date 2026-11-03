package com.bytter.biz.inneraccount.service;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.inneraccount.dao.BtBankAccPlanFreezeDao;
import com.bytter.biz.inneraccount.entity.BtBankAccPlanFreeze;
import com.bytter.biz.inneraccount.vo.BtBankAccFreezeVO;
import com.bytter.biz.inneraccount.vo.BtBankAccPlanFreezeVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccPlanFreezePageVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.NisWorkFlowConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.enums.NetbankMessageEnum;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 银行账户计划冻结申请业务层
 *
 * @author fangcf
 * @date 2025-02-11
 */
@WorkFlow(NisWorkFlowConstant.BT_BANKACC_FREEZE)
@Service("btBankAccPlanFreezeService")
@Transactional
public class BtBankAccPlanFreezeService extends BaseService<BtBankAccPlanFreeze, String> implements IUserExcelVerifyHandler<BtBankAccPlanFreezeVO>{

    @Autowired
    private BtBankAccPlanFreezeDao btBankAccPlanFreezeDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IBtBankAccFreezeService ibtBankAccFreezeService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IBtBankAccFreezeService btBankAccFreezeService;

    @Override
    public BaseDao<BtBankAccPlanFreeze, String> getEntityDao() {
        return btBankAccPlanFreezeDao;
    }

    /**
     * 银行账户计划冻结列表
     *
     * @return 银行账户计划冻结数据
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        //有资源权限的单位集合
        List<String> authCorpIdList = corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCPLANFREEZE_QUERY, user.getId());
        List<BtBankAccDTO> offLineBankAccList = accountGrantService.listByMultiVal(authCorpIdList, AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
        //银行账户计划冻结申请列表页查询数据取消是否直联限制，直联账户（在线银行账号）也可以正常查询出来
        List<BtBankAccDTO> onLineBankAccList = accountGrantService.listByMultiVal(authCorpIdList, AccountGrantConstant.ONLINE_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
        List<BtBankAccDTO> allBankAccList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(offLineBankAccList)){
            allBankAccList.addAll(offLineBankAccList);
        }
        if (!CollectionUtils.isEmpty(onLineBankAccList)){
            allBankAccList.addAll(onLineBankAccList);
        }
        if (CollectionUtils.isEmpty(allBankAccList)) {
            return new DataTablesOutput(dataTablesInput.getDraw(), Lists.newArrayList(), 0L, 0L);
        }
        // 只获取账号集合，并且去重
        List<String> bankAccList = allBankAccList.stream().map(BtBankAccDTO::getBankAcc).distinct().collect(Collectors.toList());
        String bankAccSql = SqlUtil.getInSql(bankAccList, "acc.bank_acc", true);
        return btBankAccPlanFreezeDao.getDataTablesUnion(dataTablesInput, " and " + bankAccSql);
    }

    public BtBankAccPlanFreezePageVO getAccInfoBeforePlanFreeze(BtBankAccPlanFreezeVO btBankAccPlanFreezeVO) {
        BtBankAccPlanFreezePageVO btBankAccPlanFreezePageVO = new BtBankAccPlanFreezePageVO();
        //设置账户冻结申请信息
        BtBankAccDTO accVO = this.setFreezeApplyInfo(btBankAccPlanFreezeVO);
        // 完成的冻结信息
        List<BtBankAccPlanFreezeVO> finishedList = btBankAccPlanFreezeDao.listBtBankAccPlanFreeze(btBankAccPlanFreezeVO.getBankAccId()
                                                                    , btBankAccPlanFreezeVO.getCurId(), BtBankAccConstant.STATUE_CHECK);
        //设置冻结信息
        this.setFreezeInfo(btBankAccPlanFreezeVO, accVO.getBankAcc(), finishedList);

        btBankAccPlanFreezePageVO.setAccVO(accVO);
        btBankAccPlanFreezePageVO.setFreezeVO(btBankAccPlanFreezeVO);
        return btBankAccPlanFreezePageVO;
    }

    /**
     * 计划冻结申请
     *
     * @param vo    vo对象
     * @param model model对象
     */
    public void addPlanFreeze(BtBankAccPlanFreezeVO vo, Model model) {
        //设置账户冻结申请信息
        BtBankAccDTO accVO = this.setFreezeApplyInfo(vo);
        // 完成的冻结信息
        List<BtBankAccPlanFreezeVO> finishedList = btBankAccPlanFreezeDao.listBtBankAccPlanFreeze(vo.getBankAccId(), vo.getCurId(), BtBankAccConstant.STATUE_CHECK);
        //设置冻结信息
        this.setFreezeInfo(vo, accVO.getBankAcc(), finishedList);

        model.addAttribute("accVO", accVO);
        model.addAttribute("freezeVO", vo);
    }
    /**
     * 计划冻结申请修改
     *
     * @param vo    vo对象
     * @param model model对象
     */
    public void updatePlanFreeze(BtBankAccPlanFreezeVO vo, Model model) {
        //设置账户冻结申请信息
        BtBankAccDTO accVO = this.setFreezeApplyInfo(vo);
        // 完成的冻结信息
        List<BtBankAccPlanFreezeVO> finishedList = btBankAccPlanFreezeDao.listBtBankAccPlanFreeze(vo.getBankAccId(), vo.getCurId(), BtBankAccConstant.STATUE_CHECK);
        //设置冻结信息
        this.setFreezeInfo(vo, accVO.getBankAcc(), finishedList);

        model.addAttribute("accVO", accVO);
        model.addAttribute("freezeVO", vo);
    }

    /**
     * 计划冻结申请暂存
     *
     * @param vo   计划申请表实体
     * @param user 用户对象
     */
    public void planFreezeTemp(BtBankAccPlanFreezeVO vo, ICurrentUser user) {
//        // 暂存前需判断：是否已存在冻结时间范围内的冻结记录，冻结时间范围不能交叉
//        List<BtBankAccPlanFreezeVO> allFreezeList = btBankAccPlanFreezeDao.listBtBankAccPlanFreeze(vo.getBankAccId(), vo.getCurId(), null);
//        // 冻结时间范围
//        Date freezeStartDate = vo.getFreezeStartDate();
//        Date freezeEndDate = vo.getFreezeEndDate();
//        if (!CollectionUtils.isEmpty(allFreezeList)) {
//            allFreezeList.forEach(item -> {
//                if (!freezeStartDate.after(item.getFreezeEndDate()) && !freezeEndDate.before(item.getFreezeStartDate())) {
//                    // 时间段有交叉
//                    throw new ParameterException(NetbankMessageEnum.NIS_BANKACC_MESSAGE_FREEZE_RECORD_EXIST);
//                }
//            });
//        }

        BtBankAccPlanFreeze entity = new BtBankAccPlanFreeze();
        BeanUtils.copyNotNullProperties(vo, entity);
        //判断冻结日期 不能小于开户日期
        BtBankAccDTO bankAccById = btBankAccService.getBankAccById(vo.getBankAccId());
        if (vo.getFreezeStartDate().before(bankAccById.getRegDate())) {
            throw new ParameterException(NetbankMessageEnum.BTBANKACC_FREEZETIME_ACCOPENTIME);
        }
        //状态为暂存
        entity.setStatus(BtBankAccConstant.STATUE_TEMP);
        //计划冻结单号
        entity.setPlanFreezeNo(SequenceSingleton.getSequence(SequenceEnum.PLAN_FREEZE_NO));
        //保存时，实际已冻结金额为0，冻结状态为“未冻结”，是否冻结完成提醒为0否，是否解冻为0否。
        entity.setRealFreezeMoney(BigDecimal.ZERO);
        entity.setFreezeStatus(BtBankAccConstant.FREEZE_STATUS_0);
        entity.setIsFreezeComplete(CommonConstant.NO);
        entity.setIsUnfreeze(CommonConstant.NO);
        entity.setCreateBy(user.getId());
        entity.setCreateDate(DateUtils.getDate());

        this.saveEntity(entity);
    }

    /**
     * 计划冻结申请修改
     *
     * @param vo   计划申请表实体
     * @param user 用户对象
     */
    public void planFreezeModify(BtBankAccPlanFreezeVO vo, ICurrentUser user) {
//        // 修改前需判断：是否已存在冻结时间范围内的冻结记录，冻结时间范围不能交叉
//        List<BtBankAccPlanFreezeVO> allFreezeList = btBankAccPlanFreezeDao.listBtBankAccPlanFreeze(vo.getBankAccId(), vo.getCurId(), null);
//        // 冻结时间范围
//        Date freezeStartDate = vo.getFreezeStartDate();
//        Date freezeEndDate = vo.getFreezeEndDate();
//        if (!CollectionUtils.isEmpty(allFreezeList)) {
//            // 此处需要过滤掉其本身记录
//            List<BtBankAccPlanFreezeVO> filterList = allFreezeList.stream().filter(item -> !item.getId().equals(vo.getId())).collect(Collectors.toList());
//            filterList.forEach(item -> {
//                if (!freezeStartDate.after(item.getFreezeEndDate()) && !freezeEndDate.before(item.getFreezeStartDate())) {
//                    // 时间段有交叉
//                    throw new ParameterException(NetbankMessageEnum.NIS_BANKACC_MESSAGE_FREEZE_RECORD_EXIST);
//                }
//            });
//        }

        // 通过主键id得到实体
        BtBankAccPlanFreeze entity = this.get(vo.getId(), true);
        entity.setApplyFreezeMoney(vo.getApplyFreezeMoney());
        entity.setFreezeRession(vo.getFreezeRession());
        entity.setFreezeStartDate(vo.getFreezeStartDate());
        entity.setFreezeEndDate(vo.getFreezeEndDate());
        entity.setUpdateBy(user.getId());
        entity.setUpdateDate(DateUtils.getDate());

        this.saveOrUpdate(entity);
    }

    /**
     * 计划冻结申请提交
     *
     * @param ids  id集合
     * @param user 用户对象
     */
    public void planFreezeSubmit(String ids, ICurrentUser user) {
        List<String> idList = Arrays.asList(ids.split(CommonConstant.COMMA_SPLIT));
        List<BtBankAccPlanFreeze> entityList = Lists.newArrayList();
        idList.forEach(id -> {
            BtBankAccPlanFreeze entity = this.get(id, true);
            entity.setStatus(BtBankAccConstant.STATUE_CHECK);
            entity.setUpdateBy(user.getId());
            entity.setUpdateDate(DateUtils.getDate());
            entityList.add(entity);
        });
        this.batchSaveOrUpdate(entityList);
    }

    /**
     * 设置账户冻结申请信息
     *
     * @param vo vo对象
     * @return 账户冻结申请信息
     */
    private BtBankAccDTO setFreezeApplyInfo(BtBankAccPlanFreezeVO vo) {
        BtBankAccDTO accVO = ibtBankAccFreezeService.getAccVOById(vo.getBankAccId(), vo.getCurId());
        //单位名称
        String corpName = corpService.getCorpName(accVO.getCorpId());
        if (StringUtils.isNotBlank(corpName)) {
            accVO.setCorpName(corpName);
        }
        //币别
        BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(accVO.getCurId());
        if (null != btCurrencyDTO) {
            accVO.setCurName(btCurrencyDTO.getCurName());
        }
        //账户类别
        String accTypeName = dictService.getByName("btBankAcc.accType", accVO.getAccType());
        if (StringUtils.isNotBlank(accTypeName)) {
            accVO.setAccTypeName(accTypeName);
        }
        return accVO;
    }

    /**
     * 设置冻结信息
     *
     * @param vo      vo对象
     * @param bankAcc 银行账号
     */
    private void setFreezeInfo(BtBankAccPlanFreezeVO vo, String bankAcc, List<BtBankAccPlanFreezeVO> finishedList) {
        // 已冻结记录
        List<BtBankAccPlanFreezeVO> freezeList = finishedList
                .stream()
                // 冻结状态为部分冻结或全部冻结
                .filter(item -> (item.getFreezeStatus() == 1 || item.getFreezeStatus() == 2))
                .collect(Collectors.toList());
        // 已解冻记录
        List<BtBankAccPlanFreezeVO> unFreezeList = finishedList
                .stream()
                // 是否解冻为是
                .filter(item -> CommonConstant.YES.equals(item.getIsUnfreeze()))
                .collect(Collectors.toList());

        vo.setFreezeCount(freezeList.size());
        vo.setUnfreezeCount(unFreezeList.size());
        vo.setFreezeMoneySum(freezeList.stream().map(BtBankAccPlanFreezeVO::getRealFreezeMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
        vo.setUnFreezeMoneySum(unFreezeList.stream().map(BtBankAccPlanFreezeVO::getRealFreezeMoney).reduce(BigDecimal.ZERO, BigDecimal::add));

        // 余额
        getBisAccBal(vo);
    }

    private void getBisAccBal(BtBankAccPlanFreezeVO vo) {
        String bankAccId = vo.getBankAccId();
        String curId = vo.getCurId();
        BtBankAccDTO accVO = setVoParams(bankAccId,curId);
        List<BtBankAccFreezeDTO> list = ibtBankAccFreezeService.getBtBankAccFreeze(bankAccId,curId);
        List<BtBankAccFreezeDTO> tempList = ibtBankAccFreezeService.getBtBankAccFreezeTemp(bankAccId,curId);
        List<BtBankAccFreezeDTO> waitList = ibtBankAccFreezeService.getBtBankAccFreezeWait(bankAccId,curId);
        List<BtBankAccFreezeDTO> list1 = new ArrayList<>();
        list1.addAll(tempList);
        list1.addAll(waitList);
        /*** 得到账户冻结金额*/
        BigDecimal freezeNum = ibtBankAccFreezeService.getFreezeMoney(bankAccId,curId);
        /*** 得到账户解冻金额*/
        BigDecimal ufFreezeNum = ibtBankAccFreezeService.getUfFreezeMoney(bankAccId,curId);
        if(!EmptyUtils.isEmptyList(tempList)){
            for (int i = 0; i < tempList.size(); i++) {
                tempList.get(i).setUfFreezeMoney(ufFreezeNum);
                tempList.get(i).setUfavalBal(freezeNum.subtract(ufFreezeNum));
            }
        }
        BtBankAccFreezeVO btBankAccFreezeVO = new BtBankAccFreezeVO();
        btBankAccFreezeVO.setCurId(curId);
        btBankAccFreezeVO.setBankAccId(bankAccId);
        toFreeze(btBankAccFreezeVO,list,accVO.getBankAcc(),list1);
        vo.setBal(btBankAccFreezeVO.getBal());
        vo.setAvailBal(btBankAccFreezeVO.getAvalBal());
    }



    private BtBankAccDTO setVoParams(String bankAccId,String curId) {
        BtBankAccDTO accVO = ibtBankAccFreezeService.getAccVOById(bankAccId,curId);
        String corpName  = corpService.getCorpName(accVO.getCorpId());
        if(StringUtils.isNotBlank(corpName)){
            accVO.setCorpName(corpName);
        }
        BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(accVO.getCurId());
        if(null != btCurrencyDTO){
            accVO.setCurName(btCurrencyDTO.getCurName());
        }
        String accTypeName = dictService.getByName("btBankAcc.accType",accVO.getAccType());
        if(StringUtils.isNotBlank(accTypeName)){
            accVO.setAccTypeName(accTypeName);
        }

        //开户日期处理
        if (accVO.getRegDate() != null){
            Date regDate = accVO.getRegDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(regDate);
            accVO.setRegDateStr(format);
        }
        return accVO;
    }


    public void toFreeze(BtBankAccFreezeVO vo,List<BtBankAccFreezeDTO> list,String bankAcc,List<BtBankAccFreezeDTO> tempList){
        /** 冻结次数*/
        int freezeCount = 0;
        /** 解冻次数*/
        int unfreezeCount = 0;
        /**  冻结金额合计*/
        BigDecimal freezeMoneySum=new BigDecimal(0);
        /** 解冻金额合计*/
        BigDecimal unFreezeMoneySum=new BigDecimal(0);
        /** 计算冻结金额和解冻金额*/
        for (BtBankAccFreezeDTO btBankAccFreezeDTO : list) {
            if ("0".endsWith(btBankAccFreezeDTO.getUfFlag())) {
                freezeCount++;
                freezeMoneySum = freezeMoneySum.add(btBankAccFreezeDTO.getFreezeMoney());
            } else if ("1".endsWith(btBankAccFreezeDTO.getUfFlag())) {
                unfreezeCount++;
                unFreezeMoneySum = unFreezeMoneySum.add(btBankAccFreezeDTO.getFreezeMoney());
            }
        }
        /** 冻结金额合计*/
        vo.setFreezeMoneySum(freezeMoneySum);
        /** 解冻金额合计*/
        vo.setUnFreezeMoneySum(unFreezeMoneySum);
        /** 冻结次数*/
        vo.setFreezeCount(freezeCount);
        /** 解冻次数*/
        vo.setUnfreezeCount(unfreezeCount);
        /**余额*/
        BisAccBalDTO balDto = bisAccBalService.getLastBisAccBalBy(bankAcc, vo.getCurId());
        //这里的余额计算 有问题 因为账户可用余额已经减去了冻结金额 所以暂时加上冻结金额 和 系统
//		balDto.setBal(balDto.getBal().add(balDto.getFrzBal()).add(balDto.getSysFrzBal()));
        if(!EmptyUtils.isEmpty(balDto)){
            vo.setBal(balDto.getBal());
            BigDecimal availBal = balDto.getAvailBal();
//			vo.setAvalBal(availBal);
            // 先还原余额表已扣除的冻结金额，再按冻结、解冻流水统一计算。
            availBal = availBal.add(balDto.getFrzBal() == null ? BigDecimal.ZERO : balDto.getFrzBal())
                    .add(balDto.getSysFrzBal() == null ? BigDecimal.ZERO : balDto.getSysFrzBal());
            vo.setAvalBal(availBal.add(unFreezeMoneySum).subtract(freezeMoneySum));
//			for(int i = 0;i< tempList.size();i++){
//				vo.setAvalBal(vo.getAvalBal().subtract(tempList.get(i).getFreezeMoney()));
//			}
        }

    }
    /**
     * 查询暂存的计划冻结申请记录
     *
     * @param bankAccId 银行账号id
     * @param curId     币别id
     * @return 暂存的计划冻结申请记录集合
     */
    public List<BtBankAccPlanFreezeVO> listTempPlanFreeze(String bankAccId, String curId) {
        // 暂存的冻结信息
        List<BtBankAccPlanFreezeVO> tempList = btBankAccPlanFreezeDao.listBtBankAccPlanFreeze(bankAccId, curId, BtBankAccConstant.STATUE_TEMP);
        tempList.forEach(item -> {
            item.setStatusName(dictService.get("bisExc.status", item.getStatus()));
            item.setFreezeStatusName(dictService.get("btBankAcc.planFreezeStatus", item.getFreezeStatus()));
            item.setIsFreezeCompleteName(dictService.get("sf_yn", item.getIsFreezeComplete()));
            item.setIsUnfreezeName(dictService.get("sf_yn", item.getIsUnfreeze()));
        });
        return tempList;
    }

    /**
     * 查询符合自动冻结条件的记录
     *
     * @param now 当前时间
     * @return 计划冻结申请集合
     */
    public List<BtBankAccPlanFreezeVO> listAutoFreezeRecord(Date now) {
        return btBankAccPlanFreezeDao.listAutoFreezeRecord(now);
    }

    /**
     * 查询符合自动解冻条件的记录
     *
     * @param now 当前时间
     * @return 计划解冻申请集合
     */
    public List<BtBankAccPlanFreezeVO> listAutoUnfreezeRecord(Date now) {
        return btBankAccPlanFreezeDao.listAutoUnfreezeRecord(now);
    }

    /**
     * 查询符合全部冻结成功的记录
     *
     * @return 全部冻结成功记录集合
     */
    public List<BtBankAccPlanFreezeVO> listAllFreezeSuccessRecord() {
        return btBankAccPlanFreezeDao.listAllFreezeSuccessRecord();
    }

    /**
     * 查询符合部分冻结成功的记录
     *
     * @return 部分冻结成功记录集合
     */
    public List<BtBankAccPlanFreezeVO> listPartFreezeSuccessRecord() {
        return btBankAccPlanFreezeDao.listPartFreezeSuccessRecord();
    }

    /**
     * 查询符合无冻结金额成功的记录
     *
     * @param now
     * @return
     */
    public List<BtBankAccPlanFreezeVO> listNoFreezeSuccessRecord(Date now) {
        return btBankAccPlanFreezeDao.listNoFreezeSuccessRecord(now);
    }

    /**
     * 银行账户计划冻结申请导入
     *
     * @param list 导入数据集合
     * @param user 用户对象
     */
    public void saveImport(List<BtBankAccPlanFreezeVO> list, ICurrentUser user) {
        List<BtBankAccPlanFreeze> entityList = Lists.newArrayList();
        list.forEach(vo -> {
            BtBankAccPlanFreeze entity = new BtBankAccPlanFreeze();
            BeanUtils.copyNotNullProperties(vo, entity);
            //状态为审批通过：T25030035146 银行账户计划冻结申请导入后的单据状态直接设置为“完成”
            entity.setStatus(BtBankAccConstant.STATUE_CHECK);
            //计划冻结单号
            entity.setPlanFreezeNo(SequenceSingleton.getSequence(SequenceEnum.PLAN_FREEZE_NO));
            //保存时，实际已冻结金额为0，冻结状态为“未冻结”，是否冻结完成提醒为0否，是否解冻为0否。
            entity.setRealFreezeMoney(BigDecimal.ZERO);
            entity.setFreezeStatus(BtBankAccConstant.FREEZE_STATUS_0);
            entity.setIsFreezeComplete(CommonConstant.NO);
            entity.setIsUnfreeze(CommonConstant.NO);
            entity.setCreateBy(user.getId());
            entity.setCreateDate(DateUtils.getDate());
            //添加进集合
            entityList.add(entity);
        });
        this.batchSave(entityList);
    }

    /**
     * 银行账户计划冻结申请查询列表
     *
     * @return 银行账户计划冻结数据
     */
    public DataTablesOutput getQueryDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        //有资源权限的单位集合
        List<String> authCorpIdList = corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCPLANFREEZEQUERY_QUERY, user.getId());
        if (CollectionUtils.isEmpty(authCorpIdList)) {
            return new DataTablesOutput(dataTablesInput.getDraw(), Lists.newArrayList(), 0L, 0L);
        }
        String authCorpSql = SqlUtil.getInSql(authCorpIdList, "bf.CORP_ID", true);
        DataTablesOutput output = btBankAccPlanFreezeDao.getQueryDataTablesUnion(dataTablesInput, authCorpSql);
        List<BtBankAccPlanFreezeVO> list = output.getData();
        list.forEach(item -> {
            item.setStatusName(dictService.get("bisExc.status", item.getStatus()));
            item.setFreezeStatusName(dictService.get("btBankAcc.planFreezeStatus", item.getFreezeStatus()));
            // 当“是否解冻”为“否”并且冻结状态为部分冻结和全部冻结，且同时解冻的操作人必须和申请冻结单的创建人一致
            if (this.checkIfUnfreeze(item.getStatus(), item.getIsUnfreeze(), item.getFreezeStatus(), item.getCreateBy(), user.getId())
                    || canDelete(item.getStatus(), item.getFreezeStatus(), item.getCreateBy(), user.getId())) {
                item.setCheckboxHide(CommonConstant.NO);
            } else {
                item.setCheckboxHide(CommonConstant.YES);
            }
        });
        return output;
    }

    /**
     * 银行账户计划冻结申请批量解冻
     *
     * @param ids  多条id记录
     * @param user 用户对象
     */
    public void batchManualPlanUnFreeze(String ids, ICurrentUser user) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] idList = ids.split(CommonConstant.COMMA_SPLIT);
            for (String id : idList) {
                BtBankAccPlanFreeze entity = this.get(id, true);
                if (!this.checkIfUnfreeze(entity.getStatus(), entity.getIsUnfreeze(), entity.getFreezeStatus(), entity.getCreateBy(), user.getId())) {
                    throw new ParameterException(NetbankMessageEnum.NIS_BANKACC_MESSAGE_NOT_UNFREEZE, entity.getPlanFreezeNo());
                }
                //已解冻
                entity.setIsUnfreeze(CommonConstant.YES);
                entity.setUpdateDate(DateUtils.getDate());
                // 更新计划申请表
                this.saveOrUpdate(entity);
                // 更新余额表中“系统冻结金额”
                // 获取账号可用余额
                BisAccBalDTO balDto = bisAccBalService.getLastBisAccBalBy(entity.getBankAcc(), entity.getCurId());
                if (EmptyUtils.isEmpty(balDto)) {
                    continue;
                }
                bisAccBalService.updateSysFrzBal(balDto.getId(), entity.getRealFreezeMoney(), BtBankAccConstant.PLAN_UNFREEZE);
                // 生成解冻记录
                this.saveFreezeOrUnfreezeRecord(entity, entity.getRealFreezeMoney(), BtBankAccConstant.PLAN_UNFREEZE);

            }
        }
    }


    /**
     * 银行账户计划冻结申请批量删除
     * @author huao
     * @param ids id字符串拼接
     * @param currentUser 当前用户
     */
    public void batchManualPlanDelete(String ids, ICurrentUser currentUser) {
        if (CharSequenceUtil.isEmpty(ids)) return;
        String[] idList = ids.split(CommonConstant.COMMA_SPLIT);
        for (String id : idList) {
            BtBankAccPlanFreeze entity = this.get(id, true);
            // 只有当前单据的创建人才能删除
            if (!currentUser.getId().equals(entity.getCreateBy())) continue;
            // 单据状态为“审批通过” 且 “冻结状态”为“未冻结” 时，才可以删除
            if (!Objects.equals(entity.getStatus(), CommonConstant.STATUE_CHECK)
                    || !Objects.equals(entity.getFreezeStatus(), BtBankAccConstant.FREEZE_STATUS_0)) continue;
            // 删除后，将单据状态变成“删除”
            entity.setStatus(CommonConstant.STATUE_DELETE);
            entity.setUpdateDate(DateUtils.getDate());
            // 更新计划申请表
            this.saveOrUpdate(entity);
        }
    }

    /**
     * 校验判断是否可解冻
     *
     * @param status       审批状态（审批通过）
     * @param isUnfreeze   是否解冻（否）
     * @param freezeStatus 冻结状态（1-部分冻结或2-全部冻结）
     * @param createBy     创建人（解冻的操作人必须和申请冻结单的创建人一致）
     * @param userId       操作人（解冻的操作人必须和申请冻结单的创建人一致）
     * @return 是否
     */
    private boolean checkIfUnfreeze(Integer status, String isUnfreeze, Integer freezeStatus, String createBy, String userId) {
        return BtBankAccConstant.STATUE_CHECK.equals(status)
                && CommonConstant.NO.equals(isUnfreeze)
                && (BtBankAccConstant.FREEZE_STATUS_1.equals(freezeStatus) || BtBankAccConstant.FREEZE_STATUS_2.equals(freezeStatus))
                && userId.equals(createBy);
    }

    /**
     * 是否可以删除判断
     * @author loveCamille
     * @param status 审批状态
     * @param freezeStatus 冻结状态
     * @param createBy 创建人id
     * @param userId 当前用户id
     * @return 是否可以删除
    */
    private boolean canDelete(Integer status, Integer freezeStatus, String createBy, String userId) {
        return BtBankAccConstant.STATUE_CHECK.equals(status)
                && BtBankAccConstant.FREEZE_STATUS_0.equals(freezeStatus)
                && userId.equals(createBy);
    }

    /**
     * 保存冻结/解冻记录
     *
     * @param entity     计划冻结申请实体
     * @param money      冻结/解冻金额
     * @param freezeType 冻结类新
     */
    public String saveFreezeOrUnfreezeRecord(BtBankAccPlanFreeze entity, BigDecimal money, String freezeType) {
        BtBankAccFreezeDTO dto = new BtBankAccFreezeDTO();
        dto.setFreezeNo(SequenceSingleton.getSequence(SequenceEnum.FREEZE_NO));
        // 冻结类型
        if (BtBankAccConstant.PLAN_FREEZE.equals(freezeType)) {
            dto.setUfFlag(BtBankAccConstant.UFFLAG_0.toString());
        } else if (BtBankAccConstant.PLAN_UNFREEZE.equals(freezeType)) {
            dto.setUfFlag(BtBankAccConstant.UFFLAG_1.toString());
        }
        dto.setCorpId(entity.getCorpId());
        dto.setBankAccId(entity.getBankAccId());
        dto.setBankAcc(entity.getBankAcc());
        dto.setCurId(entity.getCurId());
        dto.setFreezeMoney(money);
        dto.setPlanFlag(CommonConstant.YES);
        dto.setFreezeDate(DateUtils.getDate());
        dto.setFreezeRession(entity.getFreezeRession());
        dto.setStatus(CommonConstant.STATUE_CHECK);
        dto.setCreateDate(DateUtils.getDate());
        dto.setCreateBy(entity.getCreateBy());
        btBankAccFreezeService.save(dto);
        if(freezeType.equals(BtBankAccConstant.PLAN_FREEZE)){
            return dto.getFreezeNo();
        }
        return null;
    }

    public void saveUnfreezeRecord(BtBankAccPlanFreeze entity,String freezeNo, BigDecimal money) {
        BtBankAccFreezeDTO dto = new BtBankAccFreezeDTO();
        if(StringUtils.isEmpty(freezeNo)){
            return;
        }
        dto = btBankAccFreezeService.getBtBankAccFreezeByFreezeNo(freezeNo);
        //这里还有一个问题 就是 计划冻结 实际上是产生了一条冻结记录  而解冻也是可以进行解冻的
        //所以在计划解冻的时候 要将已解冻的金额 减去
        BigDecimal unfreezedMoney = btBankAccFreezeService.getUnFreezedMoneyById(dto.getId());
        dto.setCorpId(entity.getCorpId());
        dto.setBankAccId(entity.getBankAccId());
        dto.setBankAcc(entity.getBankAcc());
        dto.setCurId(entity.getCurId());
        dto.setFreezeMoney(money.subtract(unfreezedMoney));
        dto.setFreezeNo(SequenceSingleton.getSequence(SequenceEnum.FREEZE_NO));
        dto.setUfFlag(BtBankAccConstant.UFFLAG_1.toString());
        dto.setFreezeDate(DateUtils.getDate());
        dto.setFreezeRession("系统定时任务解冻");
        dto.setStatus(CommonConstant.STATUE_CHECK);
        dto.setCreateDate(DateUtils.getDate());
        dto.setCreateBy(entity.getCreateBy());
        dto.setFUuid(dto.getId());
        dto.setId(UUID.randomUUID().toString().replace("-", ""));
        btBankAccFreezeService.save(dto);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(BtBankAccPlanFreezeVO obj, Object... user) {
        return null;
    }

    @Override
    public void verifyHandler(List<BtBankAccPlanFreezeVO> list, Object... user) {
        if (!CollectionUtils.isEmpty(list)) {
            String userId = "";
            if (user[0] != null) {
                ICurrentUser userInfo = (ICurrentUser) user[0];
                userId = userInfo.getId();
            }
            // 获取资源权限单位
            List<String> authCorpIdList = corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCPLANFREEZE_QUERY, userId);
            List<BtBankAccDTO> offLineBankAccList = accountGrantService.listByMultiVal(authCorpIdList, AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
            //银行账户计划冻结申请列表页查询数据取消是否直联限制，直联账户（在线银行账号）也可以正常查询出来
            List<BtBankAccDTO> onLineBankAccList = accountGrantService.listByMultiVal(authCorpIdList, AccountGrantConstant.ONLINE_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
            List<BtBankAccDTO> allBankAccList = Lists.newArrayList();
            allBankAccList.addAll(offLineBankAccList);
            allBankAccList.addAll(onLineBankAccList);
            if (CollectionUtils.isEmpty(allBankAccList)) {
                list.forEach(item -> item.setErrorMsg("未查询到任何可冻结的且有可转账权限的账号"));
            }
            // 只获取账号集合，并且去重
            List<String> bankAccList = allBankAccList.stream().map(BtBankAccDTO::getBankAcc).distinct().collect(Collectors.toList());
            for (BtBankAccPlanFreezeVO vo : list) {
                List<String> errorMsgList = new ArrayList<>();
                // 冻结账号
                if (StringUtils.isEmpty(vo.getBankAcc())) {
                    errorMsgList.add("冻结账号不能为空");
                } else {
                    if (!bankAccList.contains(vo.getBankAcc())) {
                        errorMsgList.add("冻结账号不存在或没有可转账权限");
                    }
                }
                // 币别
                if (StringUtils.isEmpty(vo.getCurName())) {
                    errorMsgList.add("币别不能为空");
                } else {
                    String curId = btCurrencyService.getCurrencyByName(vo.getCurName());
                    if (StringUtils.isEmpty(curId)) {
                        errorMsgList.add("币别不存在");
                    } else {
                        vo.setCurId(curId);
                    }
                }
                // 设置银行账号主键和单位id值
                if (StringUtils.isNotEmpty(vo.getBankAcc()) && StringUtils.isNotEmpty(vo.getCurId())) {
                    BtBankAccDTO accDto = btBankAccService.getBankAccByBankAccAndCurId(vo.getBankAcc(), vo.getCurId());
                    if (EmptyUtils.isEmpty(accDto)) {
                        errorMsgList.add("冻结账号未维护【" + vo.getCurName() + "】币别");
                    } else {
                        vo.setBankAccId(accDto.getId());
                        vo.setCorpId(accDto.getCorpId());
                    }
                }
                // 冻结开始时间
                if (null == vo.getFreezeStartDate()) {
                    errorMsgList.add("冻结开始时间不能为空");
                }
                // 冻结结束时间
                if (null == vo.getFreezeEndDate()) {
                    errorMsgList.add("冻结结束时间不能为空");
                }
                if (null != vo.getFreezeStartDate() && null != vo.getFreezeEndDate()) {
                    if (vo.getFreezeStartDate().after(vo.getFreezeEndDate())) {
                        errorMsgList.add("冻结开始时间不能大于冻结结束时间");
                    }
                }
                // 申请冻结金额
                if (null == vo.getApplyFreezeMoney()) {
                    errorMsgList.add("申请冻结金额不能为空");
                } else {
                    if (vo.getApplyFreezeMoney().compareTo(BigDecimal.ZERO) <= 0) {
                        errorMsgList.add("申请冻结金额必须大于0");
                    }
                }
                // 冻结原因
                if (StringUtils.isEmpty(vo.getFreezeRession())) {
                    errorMsgList.add("冻结原因不能为空");
                }
                vo.addErrorMsg(errorMsgList);
            }

//            // 过滤出合格数据
//            List<BtBankAccPlanFreezeVO> successList = list.stream().filter(item -> StringUtils.isEmpty(item.getErrorMsg())).collect(Collectors.toList());
//            // 同一个冻结账号与币别的冻结日期区间不能交叉
//            Map<String, List<BtBankAccPlanFreezeVO>> freezeAccGroup = successList.stream().collect(Collectors.groupingBy(item -> item.getBankAcc() + CommonConstant.COMMA_SPLIT + item.getCurId()));
//
//            freezeAccGroup.forEach((k, v) -> {
//                if (v.size() > 1) {
//                    // 根据冻结时间区间进行分组
//                    Map<String, List<BtBankAccPlanFreezeVO>> freezeDateGroup = v.stream().collect(Collectors.groupingBy(
//                            item -> DateUtils.format(item.getFreezeStartDate(), DateUtils.PATTERN_CLASSICAL) + CommonConstant.COMMA_SPLIT + DateUtils.format(item.getFreezeEndDate(), DateUtils.PATTERN_CLASSICAL)));
//
//                    // 同一个冻结账号与币别且同一个区间有两条数据，则必交叉
//                    freezeDateGroup.forEach((key, value) -> {
//                        if (value.size() > 1) {
//                            // 同一个冻结账号与币别且同一个区间有两条数据，则必交叉
//                            value.forEach(item -> item.setErrorMsg("同一个冻结账号与币别的冻结日期区间不能交叉"));
//                        }
//                    });
//
//                    // 双层循环判断该map中的key值是否存在交叉
//                    freezeDateGroup.forEach((currKey, currValue) -> {
//                        String[] currKeyStr = currKey.split(CommonConstant.COMMA_SPLIT);
//                        Date currStartDate = DateUtils.parse(currKeyStr[0], DateUtils.PATTERN_CLASSICAL);
//                        Date currEndDate = DateUtils.parse(currKeyStr[1], DateUtils.PATTERN_CLASSICAL);
//                        // 防止空指针异常
//                        if (null != currStartDate && null != currEndDate) {
//                            freezeDateGroup.forEach((nextKey, nextValue) -> {
//                                // 过滤掉同一个key的判断
//                                if (!currKey.equals(nextKey)) {
//                                    String[] nextKeyStr = nextKey.split(CommonConstant.COMMA_SPLIT);
//                                    Date nextStartDate = DateUtils.parse(nextKeyStr[0], DateUtils.PATTERN_CLASSICAL);
//                                    Date nextEndDate = DateUtils.parse(nextKeyStr[1], DateUtils.PATTERN_CLASSICAL);
//                                    if (!currStartDate.after(nextEndDate) && !currEndDate.before(nextStartDate)) {
//                                        nextValue.forEach(item -> item.setErrorMsg("同一个冻结账号与币别的冻结日期区间不能交叉"));
//                                    }
//                                }
//                            });
//                        }
//                    });
//                }
//            });
        }
    }

}
