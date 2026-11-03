package com.bytter.biz.bdlink.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.dao.BisAccBalDao;
import com.bytter.biz.bdlink.entity.BisAccBal;
import com.bytter.biz.bdlink.entity.BtBankAccBal;
import com.bytter.biz.bdlink.entity.BtBankAccMessage;
import com.bytter.biz.bdlink.vo.BisAccBalVO;
import com.bytter.biz.bdlink.vo.BtBankAccBalVO;
import com.bytter.biz.payment.api.dto.BtBankAccBalDTO;
import com.bytter.biz.payment.api.service.IBtBankAccBalService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BisAccBalConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.NisWorkFlowConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccFreezeService;
import com.bytter.system.api.IBtBankAccMessageService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankAccMessageDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.enums.AccBalMessageEnum;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.collect.Lists;

import cn.hutool.core.util.ObjectUtil;

import javax.annotation.Resource;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BIS_ACC_BAL
 * @date 2017-06-23 15:02:30
 */
@WorkFlow(NisWorkFlowConstant.BT_BANKACC_BAL)
@Service("bisAccBalService")
@DubboService(interfaceClass = IWorkFlowListener.class, group = "netbankBankaccBal")
@Transactional
public class BisAccBalService extends BaseService<BisAccBal, String> implements IWorkFlowListener, IExcelVerifyHandler<BisAccBalVO>, IWorkFlowRevokeListener, ICorpDisableVerifyService , DictInjectProvider{

	@Autowired
    private BisAccBalDao bisAccBalDao;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IBtBankAccMessageService btBankAccMessageService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IBtBankAccBalService bankAccBalService;
    @DubboReference(check = false)
    private IBtBankAccFreezeService btBankAccFreezeService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private BisAccDtlService bisAccDtlService;

    @DubboReference(check = false)
    private IBtCurrencyService currencyService;

    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(BisAccBal entity) {
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
    public void save(BisAccBal entity) {
        super.saveEntity(entity);
    }

    /**
     * 保存前检查
     *
     * @param entity
     * @return
     */
    public boolean isSave(BisAccBal entity) {
        String bankAcc = entity.getBankAcc();
        String curId = entity.getCurId();
        Date balDate = entity.getBalDate();
        this.checkRegDate(bankAcc, balDate);
        BisAccBal bal = this.getBisAccBalBy(bankAcc, curId, balDate, false);
        // 当前日期已存在余额
        if (!EmptyUtils.isEmpty(bal) && !bal.getId().equalsIgnoreCase(entity.getId())) {
            throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_BALDATE_CHECK_BAL, bankAcc, DateUtils.formatDate(balDate));
        }
        // 余额与流水余额是否一致
        BigDecimal dtlAmt = this.bisAccDtlService.getBalFromDetail(bankAcc, curId, balDate);
        if (!EmptyUtils.isEmpty(dtlAmt) && dtlAmt.compareTo(BigDecimal.ZERO) != 0 && dtlAmt.compareTo(entity.getBal()) != 0) {
            throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_BAL_NOTEQ_BISACCDTLBAL, bankAcc);
        }
        return true;
    }

    /**
     * 校验日期是否在注册日期之前
     *
     * @param bankAcc
     * @param balDate
     */
    public void checkRegDate(String bankAcc, Date balDate) {
        BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccInfo(bankAcc);
        // 账户注册日期为空
        if (EmptyUtils.isEmpty(btBankAccDTO) || EmptyUtils.isEmpty(btBankAccDTO.getRegDate())) {
            throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_BANKACC_REGDATE_NULL);
        }
        // 当前余额日期在注册日期之前
        if (balDate.before(btBankAccDTO.getRegDate())) {
            throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_BALDATE_LT_ACCREDDATE, bankAcc, DateUtils.formatDate(balDate));
        }
    }

    /**
     * 保存银行账户余额
     *
     * @param bisAccBalVO
     */
    public void saveBisAccBal(BisAccBalVO bisAccBalVO, ICurrentUser user) {
        List<Map<String, Object>> btBankAccList = bisAccBalVO.getBtBankAccList();
        if (!ListUtils.isNullOrEmpty(btBankAccList)) {
            List<BisAccBal> entityList = new ArrayList();
            Date curDate = DateUtils.getDate();
            String loginName = user.getLoginName();
            Date getDate = DateUtils.getDate();
            for (Map<String, Object> map : btBankAccList) {
                String balStr = String.valueOf(map.get("accBal"));
                String availBalStr = String.valueOf(map.get("accAvailBal"));
                String balDateStr = String.valueOf(map.get("balDate"));
                // 判断余额
                if (EmptyUtils.isEmpty(balStr)) {
                    throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_BAL_NULL);
                }
                // 判断余额日期
                if (EmptyUtils.isEmpty(balDateStr)) {
                    throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_BALDATE_NULL);
                }
                String curId = String.valueOf(map.get("accCurId"));
                String bankAcc = String.valueOf(map.get("bankAcc"));
                String bifCode = EmptyUtils.isEmpty(map.get("accBifCode")) ? "" : String.valueOf(map.get("accBifCode"));
                String corpId = String.valueOf(map.get("accCorpId"));
                Date balDate = DateUtils.str2Date(balDateStr, DateUtils.date_sdf());
//                //获取当天的冻结金额
                BigDecimal freezeMoney = btBankAccFreezeService.getFreezeMoneyByBankAcc(bankAcc, curId, balDate);
                BisAccBal entity = new BisAccBal();
                BeanUtils.copyNotNullProperties(bisAccBalVO, entity);
                entity.setBifCode(bifCode);
                entity.setCorpId(corpId);
                entity.setCurId(curId);
                entity.setBankAcc(bankAcc);
                entity.setBal(new BigDecimal(balStr));
                entity.setFrzBal(freezeMoney);
                if (EmptyUtils.isBlank(availBalStr)) {
                    entity.setAvailBal(entity.getBal().subtract(freezeMoney));
                } else {
                    entity.setAvailBal(new BigDecimal(availBalStr));
                }
                entity.setBalDate(balDate);
                entity.setGetDate(getDate);
                // 设置单号
                String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
                entity.setAccNo(accNo);
                // 当前操作人信息
                entity.setCreateBy(loginName);
                entity.setCreateDate(curDate);
                entity.setUpdateBy(loginName);
                entity.setUpdateDate(curDate);
                if (isSave(entity)) {
                    entityList.add(entity);
                }
            }
            this.batchSave(entityList);
            Integer messageType = BisAccBalConstant.STATUE_WAIT_CHECK.equals(bisAccBalVO.getStatus()) ? BtBankAccMessageConstant.MESSAGE_TYPE_UPDATE : BtBankAccMessageConstant.MESSAGE_TYPE_ADD;
            for (BisAccBal entity : entityList) {
                saveAfterBisAccBal(entity.getBankAcc(), entity.getCurId(), entity.getBalDate(), user);
                //生成日志
                btBankAccMessageService.saveMessage(entity.getId(), messageType, "", BtBankAccMessageConstant.BUS_TYPE_ACCBAL, user);
                // 挂接流程
                if (entity.getStatus().intValue() >= BisAccBalConstant.STATUE_WAIT_CHECK.intValue()
                        && entity.getStatus().intValue() < BisAccBalConstant.STATUE_CHECK.intValue()) {
                    getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_BAL, entity.getId(), entity, entity.getCorpId(), user.getId());
                }
            }
        }
    }

    /**
     * 保存银行账户余额
     *
     * @param bisAccBalVO
     */
    public void initBalSave(BisAccBalVO bisAccBalVO, ICurrentUser user) {
        List<Map<String, Object>> btBankAccList = bisAccBalVO.getBtBankAccList();
        if (!ListUtils.isNullOrEmpty(btBankAccList)) {
            List<BisAccBal> addList = new ArrayList();
            List<BisAccBal> updateList = new ArrayList();
            Date curDate = DateUtils.getDate();
            String loginName = user.getLoginName();
            Date getDate = DateUtils.getDate();
            for (Map<String, Object> map : btBankAccList) {
                String balStr = String.valueOf(map.get("accBal"));
                String availBalStr = String.valueOf(map.get("accAvailBal"));
                String balDateStr = String.valueOf(map.get("balDate"));
                // 判断余额
                if (EmptyUtils.isEmpty(balStr)) {
                    throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_INITBAL_NULL);
                }
                // 判断余额日期
                if (EmptyUtils.isEmpty(balDateStr)) {
                    throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_INITBALDATE_NULL);
                }
                String curId = String.valueOf(map.get("accCurId"));
                String bankAcc = String.valueOf(map.get("bankAcc"));
                String bifCode = EmptyUtils.isEmpty(map.get("accBifCode")) ? "" : String.valueOf(map.get("accBifCode"));
                String corpId = String.valueOf(map.get("accCorpId"));
                Date balDate = DateUtils.str2Date(balDateStr, DateUtils.date_sdf());
                //获取账号本身的初始化余额
                BisAccBal initBal = this.getInitBisAccBal(bankAcc, curId);
                BisAccBal entity = this.getBisAccBalBy(bankAcc, curId, balDate, false);
                //获取当天的冻结金额
                BigDecimal freezeMoney = btBankAccFreezeService.getFreezeMoneyByBankAcc(bankAcc, curId, balDate);
                if (entity != null) {
                    if (initBal != null && !initBal.getId().equals(entity.getId())) {
                        //取消初始化余额标志
                        initBal.setInitSign(CommonConstant.NO);
                        updateList.add(initBal);
                    }
                    entity.setInitSign(CommonConstant.YES);
                    entity.setBal(new BigDecimal(balStr));
                    entity.setFrzBal(freezeMoney);
                    if (EmptyUtils.isBlank(availBalStr)) {
                        entity.setAvailBal(entity.getBal().subtract(freezeMoney));
                    } else {
                        entity.setAvailBal(new BigDecimal(availBalStr));
                    }
                    entity.setBalDate(balDate);
                    entity.setGetDate(getDate);
                    entity.setStatus(BisAccBalConstant.STATUE_CHECK.intValue());
                    entity.setUpdateBy(loginName);
                    entity.setUpdateDate(curDate);
                    updateList.add(entity);
                } else {
                    entity = new BisAccBal();
                    BeanUtils.copyNotNullProperties(bisAccBalVO, entity);
                    entity.setId("");
                    entity.setBifCode(bifCode);
                    entity.setCorpId(corpId);
                    entity.setCurId(curId);
                    entity.setBankAcc(bankAcc);
                    entity.setBal(new BigDecimal(balStr));
                    entity.setFrzBal(freezeMoney);
                    entity.setAvailBal(entity.getBal().subtract(entity.getFrzBal()));
                    entity.setBalDate(balDate);
                    entity.setGetDate(getDate);
                    // 设置单号
                    String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
                    entity.setAccNo(accNo);
                    // 当前操作人信息
                    entity.setCreateBy(loginName);
                    entity.setCreateDate(curDate);
                    entity.setInitSign(CommonConstant.YES);
                    entity.setStatus(BisAccBalConstant.STATUE_CHECK.intValue());
                    addList.add(entity);
                }
            }
            if (addList.size() > 0) {
                this.batchSave(addList);
            }
            if (updateList.size() > 0) {
                this.batchSaveOrUpdate(updateList);
            }
        }
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(BisAccBal entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(BisAccBal entity) {
        super.saveOrUpdate(entity);
    }

    /**
     * 修改银行账户余额
     *
     * @param entity
     */
    public void updateBisAccBal(BisAccBal entity, ICurrentUser user) {
        if (isSave(entity)) {
            if (EmptyUtils.isEmpty(entity.getAvailBal())) {
                entity.setAvailBal(entity.getBal());
            }
            this.saveOrUpdate(entity);
            //生成日志
            btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_UPDATE, "", BtBankAccMessageConstant.BUS_TYPE_ACCBAL, user);
            if (entity.getStatus().intValue() >= BisAccBalConstant.STATUE_WAIT_CHECK.intValue()
                    && entity.getStatus().intValue() < BisAccBalConstant.STATUE_CHECK.intValue()) {
                getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_BAL, entity.getId(), entity, entity.getCorpId(), user.getId());
            }
        }
    }

    @Override
    public BaseDao<BisAccBal, String> getEntityDao() {
        return bisAccBalDao;
    }

    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        DataTablesOutput output = bisAccBalDao.getDataTablesUnion(dataTablesInput, getAuthSql(user) + " and b.create_by ='" + user.getLoginName() + "'");
        List<BisAccBalVO> voList = output.getData();
        List<String> bankAccS = new ArrayList<>();
        for (BisAccBalVO vo : voList) {
            bankAccS.add(vo.getBankAcc());
        }
        if (bankAccS.size() > 0) {
            Map<String, BtBankAccDTO> accMap = btBankAccService.getBankAccMapByAccsAndCurId(bankAccS);
            if(ObjectUtil.isNotEmpty(accMap)){
                for (BisAccBalVO vo : voList) {
                    BtBankAccDTO accDTO = accMap.get(vo.getBankAcc() + vo.getCurId());
                    //判空，防止空指针
                    if (ObjectUtil.isNotEmpty(accDTO)) {
                        vo.setAccName(accDTO.getAccName());
                        vo.setBankName(accDTO.getBankName());
                        vo.setCorpName(accDTO.getCorpName());
                        vo.setCurName(accDTO.getCurName());
                    }
                }
            }
        }
        return output;
    }

    /**
     * 根据id查找银行余额信息
     * zuoy 2017-06-23
     *
     * @param id
     * @return BisAccBalVO
     */
    public BisAccBalVO getByUnion(String id) {
        return bisAccBalDao.getByUnion(id);
    }

    /**
     * 获取权限SQL
     *
     * @return
     */
    private String getAuthSql(ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BISACCBAL_QUERY, user.getId()), "b.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        return sql.toString();
    }

    /**
     * 银行账户余额查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, ICurrentUser user) {
        String authSql = getAuthSql(user);
        // 权限控制类型  1-单位级 2-账号级
        String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (!CommonConstant.YES.equals(controlType)) {
            List<String> accList = accountGrantService.listByMultiValAccs(user.getId(), ResourceConstant.NETBANK_BANKACC_BISACCBAL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
            authSql += "and " + SqlUtil.getInSql(accList, "b.bank_acc", true);
        }
        DataTablesOutput output = bisAccBalDao.getBankAccMapByAccsAndCurId(dataTablesInput, authSql);
        List<BisAccBalVO> voList = output.getData();
        List<BisAccBalVO> bisAccBalVOList = new ArrayList<>();

        if (voList.size() > 0) {
            //获取币种信息
            Map<String, BtCurrencyDTO> btCurrencyMap = btCurrencyService.getCurrency();
            //获取单位信息
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();

            Map<String, BtBankAccDTO> accMap = new HashMap<>();

            // 字典表数据
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("btBankAcc.accType", "cgsBill.status", "sf_yn");
            if (accMap != null) {
                for (BisAccBalVO vo : voList) {
                    // 账户类别
                    Optional.ofNullable(vo.getAccType()).ifPresent(accType -> {
                        Map<String, DictDTO> dictDtoMap = listDictMap.get("btBankAcc.accType");
                        Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(accType)))
                                .ifPresent(dict -> vo.setAccTypeName(dict.getName()));
                    });
                    // 状态
                    Optional.ofNullable(vo.getStatus()).ifPresent(status -> {
                        Map<String, DictDTO> dictDtoMap = listDictMap.get("cgsBill.status");
                        Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status.toString())))
                                .ifPresent(dict -> vo.setStatusLang(dict.getName()));
                    });
                    // 是否上线
                    Optional.ofNullable(vo.getIsOnline()).ifPresent(isOnline -> {
                        Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
                        Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isOnline)))
                                .ifPresent(dict -> vo.setIsOnlineStr(dict.getName()));
                    });

                    bisAccBalVOList.add(vo);
//                    if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("accStatus")) && StringUtils.isNotBlank(dataTablesInput.getFormdata().get("accStatus").toString())) {
//                        String accStatus = dataTablesInput.getFormdata().get("accStatus").toString();
//                        if (accStatus.equals(com.bytter.core.constant.BtBankAccConstant.ACCSTATUS_1) && com.bytter.core.constant.BtBankAccConstant.STATUE_DEL != vo.getStatus()) {
//                            bisAccBalVOList.add(vo);
//                        } else if (accStatus.equals(com.bytter.core.constant.BtBankAccConstant.ACCSTATUS_2) && com.bytter.core.constant.BtBankAccConstant.STATUE_DEL == vo.getStatus()) {
//                            bisAccBalVOList.add(vo);
//                        }
//                    } else {
//                        bisAccBalVOList.add(vo);
//                    }
                    if (btCurrencyMap != null) {
                        if (btCurrencyMap.containsKey(vo.getCurId())) {
                            String curName = btCurrencyMap.get(vo.getCurId()).getCurName();
                            vo.setCurName(curName);
                        }
                    }
                    if (corpMap != null) {
                        if (corpMap.containsKey(vo.getCorpId())) {
                            String corpName = corpMap.get(vo.getCorpId()).getName();
                            vo.setCorpName(corpName);
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(bisAccBalVOList)) {
            return new DataTablesOutput();
        }
        //设置期初余额(获取余额日期前一天的余额)
        List<String> listBankAcc = null;
        List<String> listCurId = null;
        List<Date> listBalDate = null;
        List<Date> listBalDateTransfer = new ArrayList<Date>();
        for (BisAccBalVO bisAccBalVO : bisAccBalVOList) {
            listBankAcc = bisAccBalVOList.stream().map(BisAccBalVO::getBankAcc).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }
        for (BisAccBalVO bisAccBalVO : bisAccBalVOList) {
            listCurId = bisAccBalVOList.stream().map(BisAccBalVO::getCurId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }
        for (BisAccBalVO bisAccBalVO : bisAccBalVOList) {
            listBalDate = bisAccBalVOList.stream().map(BisAccBalVO::getBalDate).filter((Objects::nonNull)).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(listBalDate)) {
            for (Date bal : listBalDate) {
                listBalDateTransfer.add(DateUtils.addDay(bal, -1));
            }
        }
        Map<String, BigDecimal> mapAvailable = (Map<String, BigDecimal>) bisAccBalDao.BalByBalDateForBatch(listBankAcc, listCurId, listBalDateTransfer, null, true);

        if (MapUtils.isNotEmpty(mapAvailable)) {
            for (BisAccBalVO bisAccBalVO : bisAccBalVOList) {
                if (mapAvailable.containsKey(bisAccBalVO.getBankAcc() + bisAccBalVO.getCurId() +  DateUtils.formatDate(DateUtils.addDay(bisAccBalVO.getBalDate(), -1)))) {
                    bisAccBalVO.setBeginBal((BigDecimal) mapAvailable.get(bisAccBalVO.getBankAcc() + bisAccBalVO.getCurId() + DateUtils.formatDate(DateUtils.addDay(bisAccBalVO.getBalDate(), -1))));
                }
            }
        }
        for (BisAccBalVO bisAccBalVO : bisAccBalVOList) {
            //如果是初始化余额的话 需要当天余额 - 流水总额
            if(CommonConstant.YES.equals(bisAccBalVO.getInitSign())){

                BigDecimal balFromDetail = bisAccDtlService.getBalFromDetail(bisAccBalVO.getBankAcc(), bisAccBalVO.getCurId(), bisAccBalVO.getBalDate());

                bisAccBalVO.setBeginBal(bisAccBalVO.getBal().subtract(balFromDetail));
                continue;
            }
            if( (bisAccBalVO.getBeginBal() == null || bisAccBalVO.getBeginBal().compareTo(BigDecimal.ZERO) == 0) && bisAccBalVO.getBal() != null){
                bisAccBalVO.setBeginBal(bisAccBalVO.getBal());
            }
        }
        return output;
    }

    /**
     * 余额导出全部
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    public List listExport(DataTablesInput dataTablesInput, ICurrentUser user) {
        String authSql = getAuthSql(user);
        //权限控制类型  1-单位级 2-账号级
        String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (!CommonConstant.YES.equals(controlType)) {
            List<String> accList = accountGrantService.listByMultiValAccIds(user.getId(), ResourceConstant.NETBANK_BANKACC_BISACCBAL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
            authSql += "and " + SqlUtil.getInSql(accList, "a.id", true);
        }
        return bisAccBalDao.listExport(dataTablesInput, authSql);
    }

    /**
     * 根据条件查询银行账户余额
     *
     * @param banAcc 账户
     * @param curId  币别ID，可为null
     * @return BtBankAccDTO
     */
    public List<BisAccBalVO> listBisAccBalBy(String banAcc, String curId) {
        return bisAccBalDao.listBisAccBalBy(banAcc, curId);
    }

    /**
     * 根据条件获取余额信息
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @param balDate 日期,为null则查询最后一次余额
     * @param status  状态，true - 查询已完成 false - 查询未删除
     * @return BisAccBal
     */
    public BisAccBal getBisAccBalBy(String bankAcc, String curId, Date balDate, boolean status) {
        return bisAccBalDao.getBisAccBalBy(bankAcc, curId, balDate, status);
    }
    
    public List<BisAccBal> getBisAccBalByList(List<String> bankAccs, List<String> curIds, Date balDate, boolean status) {
        return bisAccBalDao.getBisAccBalByList(bankAccs, curIds, balDate, status);
    }

    /**
     * 根据条件获取余额日期
     *
     * @param bankAcc      账户
     * @param curId        币别ID
     * @param balBeginDate 开始日期 不包含
     * @param balEndDate   结束日期 不包含
     * @param status       状态，true - 查询已完成 false - 查询未删除
     * @return BisAccBal
     */
    public List<Date> getBisAccBalDateBy(String bankAcc, String curId, Date balBeginDate, Date balEndDate, boolean status) {
        return bisAccBalDao.getBisAccBalBy(bankAcc, curId, balBeginDate, balEndDate, status);
    }

    /**
     * 获取最近一次的冻结金额
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @return BigDecimal
     */
    public BigDecimal getLastFrzBal(String bankAcc, String curId) {
        BisAccBal bal = this.getBisAccBalBy(bankAcc, curId, null, true);
        return EmptyUtils.isEmpty(bal) || EmptyUtils.isEmpty(bal.getFrzBal()) ? BigDecimal.ZERO : bal.getFrzBal();
    }

    /**
     * 获取余额表的执行日期
     *
     * @param bankAcc  账户
     * @param curId    币别ID
     * @param balDate  比较日期，为null则查询最后一次执行日期
     * @param operator 跟balDate组合使用，日期比较运算符，参照MatchTypeEnum
     * @param status   状态，true - 查询已完成 false - 查询未删除
     * @return Date
     */
    public Date getLastBalDate(String bankAcc, String curId, Date balDate, String operator, boolean status) {
        if (EmptyUtils.isNotEmpty(operator) && EmptyUtils.isEmpty(balDate)) {
            return null;
        }
        return this.bisAccBalDao.getLastBalDate(bankAcc, curId, balDate, operator, status);
    }

    /**
     * 获取最近一次的余额
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @param status  状态，true - 查询已完成 false - 查询未删除
     * @return BigDecimal
     */
    public BigDecimal getLastByBalDate(String bankAcc, String curId, boolean status) {
        return this.getBalByBalDate(bankAcc, curId, null, null, status);
    }

    /**
     * 获取余额表最后一次的执行日期
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @return Date
     */
    public Date getLastBalDate(String bankAcc, String curId, boolean status) {
        return this.getLastBalDate(bankAcc, curId, null, null, status);
    }

    /**
     * 获取指定日期的余额
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @param balDate 日期
     * @param status  状态，true - 查询已完成 false - 查询未删除
     * @return BigDecimal
     */
    public BigDecimal getBalByBalDate(String bankAcc, String curId, Date balDate, boolean status) {
        if (EmptyUtils.isEmpty(balDate)) {
            return null;
        }
        return this.getBalByBalDate(bankAcc, curId, balDate, MatchTypeEnum.EQ.getValue(), status);
    }

    /**
     * 获取指定日期的余额，若指定日期不存在余额，则查询最近一次
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @param balDate 日期
     * @param status  状态，true - 查询已完成 false - 查询未删除
     * @return BigDecimal
     */
    public BigDecimal getCurBalOrLastBal(String bankAcc, String curId, Date balDate, boolean status) {
        BigDecimal curBal = this.getBalByBalDate(bankAcc, curId, balDate, status);
        if (!EmptyUtils.isEmpty(curBal)) {
            return curBal;
        }
        return this.getBalByBalDate(bankAcc, curId, balDate, MatchTypeEnum.LE.getValue(), status);
    }

    /**
     * 根据条件组合查询余额
     *
     * @param bankAcc  账户
     * @param curId    币别ID
     * @param balDate  比较日期，为null则查询最近一次余额
     * @param operator 跟balDate组合使用，日期比较运算符，参照MatchTypeEnum
     * @param status   状态，true - 查询已完成 false - 查询未删除
     * @return BigDecimal
     */
    public BigDecimal getBalByBalDate(String bankAcc, String curId, Date balDate, String operator, boolean status) {
        if (EmptyUtils.isNotEmpty(operator) && EmptyUtils.isEmpty(balDate)) {
            return null;
        }
        return this.bisAccBalDao.getBalByBalDate(bankAcc, curId, balDate, operator, status);
    }

    /**
     * 获取指定日期的余额，若指定日期不存在余额，则查询最近一次
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @param balDate 日期
     * @param status  状态，true - 查询已完成 false - 查询未删除
     * @return BigDecimal
     */
    public BigDecimal getCurRealBalOrLastBal(String bankAcc, String curId, Date balDate, boolean status) {
        if (EmptyUtils.isEmpty(balDate)) {
            return null;
        }
        BigDecimal curBal = this.getRealBalByBalDate(bankAcc, curId, balDate, MatchTypeEnum.EQ.getValue(), status);
        if (!EmptyUtils.isEmpty(curBal)) {
            return curBal;
        }
        return this.getRealBalByBalDate(bankAcc, curId, balDate, MatchTypeEnum.LE.getValue(), status);
    }

    /**
     * 根据条件组合查询账户余额
     *
     * @param bankAcc  账户
     * @param curId    币别ID
     * @param balDate  比较日期，为null则查询最近一次余额
     * @param operator 跟balDate组合使用，日期比较运算符，参照MatchTypeEnum
     * @param status   状态，true - 查询已完成 false - 查询未删除
     * @return BigDecimal
     */
    public BigDecimal getRealBalByBalDate(String bankAcc, String curId, Date balDate, String operator, boolean status) {
        if (EmptyUtils.isNotEmpty(operator) && EmptyUtils.isEmpty(balDate)) {
            return null;
        }
        return this.bisAccBalDao.getBankBalByBalDate(bankAcc, curId, balDate, operator, status);
    }

    /**
     * 计算余额
     *
     * @param bankAcc   账户
     * @param curId     币别ID
     * @param transTime 交易日期
     * @return
     */
    public BigDecimal getBalFromDetail(String bankAcc, String curId, Date transTime) {
        //判断余额当天是否有初始化余额，有初始化余额的话当天以初始化余额为准
        //BtBankAccDTO bankAccDTO = btBankAccService.getBankAccInfo(bankAcc);
        //不直接取流水明细里缓存的运行余额(bis_acc_dtl.bal)：在中间补录流水后该缓存值会过期，
        //导致重新生成余额时后续日期未回算。改为按“前一日期余额 + 当天明细”重新累加。

//        BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getLastDtlBal(bankAcc, curId, transTime);
//        if(bisAccDtlDTO != null && bisAccDtlDTO.getBal() != null){
//            return bisAccDtlDTO.getBal();
//        }


        BisAccBal initBal = this.getInitBisAccBal(bankAcc, curId);

        if (initBal != null && DateUtils.dayDiffs(transTime, initBal.getBalDate()) == 0 && initBal.getBal() != null) {
            return initBal.getBal();
        } else {
            // 查询昨天的余额
            BigDecimal bal = bisAccBalDao.getBalByBalDateNew(bankAcc, curId, transTime, MatchTypeEnum.LT.getValue(), true);
            if (EmptyUtils.isEmpty(bal)) {
                bal = BigDecimal.ZERO;
            }
            // 指定日期的明细
            BigDecimal dtlAmt = bisAccDtlService.getBalFromDetail(bankAcc, curId, transTime);
            return bal.add(dtlAmt);
        }
    }

    /**
     * 更新银行账户冻结余额与可用余额
     *
     * @param bankAcc    账户
     * @param curId      币别ID
     * @param updateDate 冻结日期
     * @param frzBal     冻结金额
     * @param isFrozen   true-->冻结， false-->解冻
     */
    public void updateFrzBalAndAvailBal(String bankAcc, String curId, Date updateDate, BigDecimal frzBal, boolean isFrozen, ICurrentUser user) {
        // 查询冻结日期当天的余额
        BisAccBal bisAccBal = this.getBisAccBalBy(bankAcc, curId, updateDate, false);
        boolean flag = false;
        if (EmptyUtils.isEmpty(bisAccBal)) {
            bisAccBal = new BisAccBal();
            BisAccBal oldBisAccBal = this.getBisAccBalBy(bankAcc, curId, null, true);
            BeanUtils.copyNotNullProperties(oldBisAccBal, bisAccBal);
            bisAccBal.setId("");
            bisAccBal.setInitSign(CommonConstant.YES);
            flag = true;
        }
        bisAccBal.setBalDate(DateUtils.parseDate(updateDate));
//	 	冻结	余额=余额	可用余额=可用余额-现冻结金额	冻结金额=原冻结金额+现冻结金额
//		解冻	余  额=余额  	可用余额=可用余额+现解冻金额  	冻结金额=原冻结金额-现解冻金额
        if (isFrozen) {
            bisAccBal.setFrzBal(bisAccBal.getFrzBal().add(frzBal));
            bisAccBal.setAvailBal(bisAccBal.getAvailBal().subtract(frzBal));
        } else {
            bisAccBal.setFrzBal(bisAccBal.getFrzBal().subtract(frzBal));
            bisAccBal.setAvailBal(bisAccBal.getAvailBal().add(frzBal));
        }
        // 设置修改人信息
        bisAccBal.setUpdateBy(user.getLoginName());
        bisAccBal.setUpdateDate(DateUtils.getDate());
        if (flag) {
            this.save(bisAccBal);
        } else {
            this.getSession().merge(bisAccBal);
            //this.saveOrUpdate(bisAccBal);
        }
        saveAfterBisAccBal(bisAccBal.getBankAcc(), bisAccBal.getCurId(), bisAccBal.getBalDate(), user);
    }


    public void updateFrzBalAndAvailBal(String bankAcc, String curId, Date updateDate,String planFlag, BigDecimal frzBal, boolean isFrozen, ICurrentUser user) {
        // 查询冻结日期当天的余额
        BisAccBal bisAccBal = this.getBisAccBalBy(bankAcc, curId, updateDate, false);
        boolean flag = false;
        if (EmptyUtils.isEmpty(bisAccBal)) {
            bisAccBal = new BisAccBal();
            BisAccBal oldBisAccBal = this.getBisAccBalBy(bankAcc, curId, null, true);
            BeanUtils.copyNotNullProperties(oldBisAccBal, bisAccBal);
            bisAccBal.setId("");
            bisAccBal.setInitSign(CommonConstant.YES);
            flag = true;
        }
        bisAccBal.setBalDate(DateUtils.parseDate(updateDate));
//	 	冻结	余额=余额	可用余额=可用余额-现冻结金额	冻结金额=原冻结金额+现冻结金额
//		解冻	余  额=余额  	可用余额=可用余额+现解冻金额  	冻结金额=原冻结金额-现解冻金额
        if (isFrozen) {
            if(CommonConstant.YES.equals(planFlag)){
                bisAccBal.setSysFrzBal(bisAccBal.getSysFrzBal().add(frzBal));
                bisAccBal.setAvailBal(bisAccBal.getAvailBal().subtract(frzBal));
            }else{
                bisAccBal.setFrzBal(bisAccBal.getFrzBal().add(frzBal));
                bisAccBal.setAvailBal(bisAccBal.getAvailBal().subtract(frzBal));
            }

        } else {
            if(CommonConstant.YES.equals(planFlag)){
                bisAccBal.setSysFrzBal(bisAccBal.getSysFrzBal().subtract(frzBal));
                bisAccBal.setAvailBal(bisAccBal.getAvailBal().add(frzBal));
            }else{
                bisAccBal.setFrzBal(bisAccBal.getFrzBal().subtract(frzBal));
                bisAccBal.setAvailBal(bisAccBal.getAvailBal().add(frzBal));
            }
        }
        // 设置修改人信息
        bisAccBal.setUpdateBy(user.getLoginName());
        bisAccBal.setUpdateDate(DateUtils.getDate());
        if (flag) {
            this.save(bisAccBal);
        } else {
            this.getSession().merge(bisAccBal);
            //this.saveOrUpdate(bisAccBal);
        }
        saveAfterBisAccBal(bisAccBal.getBankAcc(), bisAccBal.getCurId(), bisAccBal.getBalDate(), user);
    }

    /**
     * 由银行账户流水明细生成余额
     *
     * @param bisAccBalVO
     */
    public void saveAccBalByDtl(BisAccBalDTO bisAccBalVO, ICurrentUser user) {
        List<Map<String, Object>> btBankAccList = bisAccBalVO.getBtBankAccList();
        if (!ListUtils.isNullOrEmpty(btBankAccList)) {
            for (Map<String, Object> map : btBankAccList) {
                saveAccBalByDtlTransaction(map, bisAccBalVO, user);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAccBalByDtlTransaction(Map<String, Object> map, BisAccBalDTO bisAccBalVO, ICurrentUser user) {
        // 当前操作人信息
        String loginName = user.getLoginName();
        Date curDate = DateUtils.getDate();
        Date getDate = DateUtils.getDate();
        // 由明细生成余额时间
        Calendar beginDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Date startDate = DateUtils.getIntegralStartTime(bisAccBalVO.getBeginDate());
        Date endTime = DateUtils.getIntegralEndTime(bisAccBalVO.getEndDate());
        endDate.setTime(endTime);
        beginDate.setTime(startDate);
        String curId = String.valueOf(map.get("accCurId"));
        String bankAcc = String.valueOf(map.get("bankAcc"));
        String corpId = String.valueOf(map.get("accCorpId"));
        String bifCode = null == map.get("bifCode") ? "" : String.valueOf(map.get("bifCode"));
        while (!beginDate.after(endDate)) {
            // 账户余额
            BigDecimal money = this.getBalFromDetail(bankAcc, curId, beginDate.getTime());
            //获取当天的冻结金额
            BigDecimal freezeMoney = btBankAccFreezeService.getFreezeMoneyByBankAcc(bankAcc, curId, beginDate.getTime());
            this.checkRegDate(bankAcc, beginDate.getTime());
            // 查询当天账户的余额信息
            BisAccBal bisAccBal = this.getBisAccBalBy(bankAcc, curId, beginDate.getTime(), false);
            if (!EmptyUtils.isEmpty(bisAccBal)) {
                if (bisAccBal.getStatus() == null || BisAccBalConstant.STATUE_CHECK.intValue() == bisAccBal.getStatus().intValue()) {
                    // 如果需要重新生成余额，则更新
                    if (!EmptyUtils.isEmpty(bisAccBalVO.getGenerateBalance()) && 1 == bisAccBalVO.getGenerateBalance()) {
                        bisAccBal.setBalDate(beginDate.getTime());
                        bisAccBal.setBal(money);
                        if (EmptyUtils.isBlank(bisAccBal.getAccNo())) {
                            // 设置单号
                            String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
                            bisAccBal.setAccNo(accNo);
                        }
                        bisAccBal.setFrzBal(freezeMoney);
                        bisAccBal.setAvailBal(bisAccBal.getBal().subtract(bisAccBal.getFrzBal()));
                        //更新余额获取时间
                        bisAccBal.setGetDate(curDate);
                        bisAccBal.setUpdateBy(loginName);
                        bisAccBal.setUpdateDate(curDate);
                        this.saveOrUpdate(bisAccBal);
                        getSession().flush();
                        //生成日志
                        btBankAccMessageService.saveMessage(bisAccBal.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_DTLINIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCBAL, user);
                    }
                }
            } else {
                bisAccBal = new BisAccBal();
                // 设置单号
                String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
                bisAccBal.setAccNo(accNo);
                bisAccBal.setBankAcc(bankAcc);
                bisAccBal.setCurId(curId);
                bisAccBal.setCorpId(corpId);
                bisAccBal.setBifCode(bifCode);
                bisAccBal.setBalDate(beginDate.getTime());
                bisAccBal.setGetDate(getDate);
                bisAccBal.setBal(money);
                bisAccBal.setFrzBal(freezeMoney);
                bisAccBal.setAvailBal(bisAccBal.getBal().subtract(bisAccBal.getFrzBal()));
                bisAccBal.setCreateBy(loginName);
                bisAccBal.setCreateDate(curDate);
                bisAccBal.setUpdateBy(loginName);
                bisAccBal.setUpdateDate(curDate);
                bisAccBal.setStatus(BisAccBalConstant.STATUE_CHECK);
                this.save(bisAccBal);
                getSession().flush();
                //生成日志
                btBankAccMessageService.saveMessage(bisAccBal.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_DTLINIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCBAL, user);
            }
            beginDate.add(Calendar.DAY_OF_YEAR, 1);
        }
        saveAfterBisAccBal(bankAcc, curId, bisAccBalVO.getBeginDate(), user);
    }

    public void saveAccBalByDtlTransactionForDtl(Map<String, Object> map, BisAccBalDTO bisAccBalVO, ICurrentUser user) {
        // 当前操作人信息
        String loginName = user.getLoginName();
        Date curDate = DateUtils.getDate();
        Date getDate = DateUtils.getDate();
        // 由明细生成余额时间
        Calendar beginDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(bisAccBalVO.getEndDate());
        beginDate.setTime(bisAccBalVO.getBeginDate());
        String curId = String.valueOf(map.get("accCurId"));
        String bankAcc = String.valueOf(map.get("bankAcc"));
        String corpId = String.valueOf(map.get("accCorpId"));
        String bifCode = null == map.get("bifCode") ? "" : String.valueOf(map.get("bifCode"));
        while (!beginDate.after(endDate)) {
            // 账户余额
            BigDecimal money = this.getBalFromDetail(bankAcc, curId, beginDate.getTime());
            //获取当天的冻结金额
            BigDecimal freezeMoney = btBankAccFreezeService.getFreezeMoneyByBankAcc(bankAcc, curId, beginDate.getTime());
            this.checkRegDate(bankAcc, beginDate.getTime());
            // 查询当天账户的余额信息
            BisAccBal bisAccBal = this.getBisAccBalBy(bankAcc, curId, beginDate.getTime(), false);
            if (!EmptyUtils.isEmpty(bisAccBal)) {
                if (bisAccBal.getStatus() == null || BisAccBalConstant.STATUE_CHECK.intValue() == bisAccBal.getStatus().intValue()) {
                    // 如果需要重新生成余额，则更新
                    if (!EmptyUtils.isEmpty(bisAccBalVO.getGenerateBalance()) && 1 == bisAccBalVO.getGenerateBalance()) {
                        bisAccBal.setBalDate(beginDate.getTime());
                        bisAccBal.setBal(money);
                        if (EmptyUtils.isBlank(bisAccBal.getAccNo())) {
                            // 设置单号
                            String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
                            bisAccBal.setAccNo(accNo);
                        }
                        bisAccBal.setFrzBal(freezeMoney);
                        bisAccBal.setAvailBal(bisAccBal.getBal().subtract(bisAccBal.getFrzBal()));
                        //更新余额获取时间
                        bisAccBal.setGetDate(curDate);
                        bisAccBal.setUpdateBy(loginName);
                        bisAccBal.setUpdateDate(curDate);
                        if(StringUtils.isNotBlank(user.getTenantId())){
                            bisAccBal.setTenantId(user.getTenantId());
                        }
                        this.saveOrUpdate(bisAccBal);
                        getSession().flush();
                        //生成日志
                        btBankAccMessageService.saveMessage(bisAccBal.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_DTLINIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCBAL, user);
                    }
                }
            } else {
                bisAccBal = new BisAccBal();
                // 设置单号
                String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
                bisAccBal.setAccNo(accNo);
                bisAccBal.setBankAcc(bankAcc);
                bisAccBal.setCurId(curId);
                bisAccBal.setCorpId(corpId);
                bisAccBal.setBifCode(bifCode);
                bisAccBal.setBalDate(beginDate.getTime());
                bisAccBal.setGetDate(getDate);
                bisAccBal.setBal(money);
                bisAccBal.setFrzBal(freezeMoney);
                bisAccBal.setAvailBal(bisAccBal.getBal().subtract(bisAccBal.getFrzBal()));
                bisAccBal.setCreateBy(loginName);
                bisAccBal.setCreateDate(curDate);
                bisAccBal.setUpdateBy(loginName);
                bisAccBal.setUpdateDate(curDate);
                bisAccBal.setStatus(BisAccBalConstant.STATUE_CHECK);
                bisAccBal.setTenantId(user.getTenantId());
                //工作流那里貌似审批后拿不到租户ID 这里保底策略  如果为空 则去根据用户ID查询租户ID
                if(StringUtils.isEmpty(bisAccBal.getTenantId())){
                    String sql = "select tenant_id as tenantId from sys_user where id = :id ";
                    Map<String,Object> params = new HashMap<>();
                    params.put("id",user.getId());
                    String tenantId = (String) bisAccBalDao.createSQLQuery(sql.toString(), params).uniqueResult();
                    bisAccBal.setTenantId(tenantId);
                }
                this.save(bisAccBal);
                getSession().flush();
                //生成日志
                btBankAccMessageService.saveMessage(bisAccBal.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_DTLINIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCBAL, user);
            }
            beginDate.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    /**
     * 银行账户创建时初始化余额信息
     *
     * @param bankAcc 银行账户实体对象
     * @return boolean
     */
    public boolean initBisAccBal(BtBankAccDTO bankAcc, ICurrentUser user) {
        if (EmptyUtils.isNotEmpty(bankAcc.getCurList())) {
            try {
                List<BisAccBal> entityList = new ArrayList();
                Date curDate = DateUtils.getDate();
                String loginName = user.getLoginName();
                Date getDate = DateUtils.getDate();
                List<BtBankAccBal> bankAccBals = new ArrayList();
                for (String curId : bankAcc.getCurList()) {
                    Long countBisAccBal = this.countBisAccBalBy(bankAcc.getBankAcc(), curId, null, true);
//                    BisAccBal oldBisAccBal = this.getBisAccBalBy(bankAcc.getBankAcc(), curId, null, true);
//                    // 存在余额信息则不保存
//                    if (!EmptyUtils.isEmpty(oldBisAccBal)) {
//                        continue;
//                    }
                    if (!EmptyUtils.isEmpty(countBisAccBal) && countBisAccBal > 0L) {
                        continue;
                    }
                    BisAccBal bisAccBal = new BisAccBal();
                    // 设置账户信息
                    bisAccBal.setBankAcc(bankAcc.getBankAcc());
                    bisAccBal.setCorpId(bankAcc.getCorpId());
                    bisAccBal.setBifCode(bankAcc.getBifCode());
                    bisAccBal.setCurId(curId);
                    // 设置余额信息
                    BigDecimal bal = EmptyUtils.isEmpty(bankAcc.getInitBalance()) ? BigDecimal.ZERO : bankAcc.getInitBalance();
                    Date balDate = EmptyUtils.isEmpty(bankAcc.getInitBalanceDate()) ? DateUtils.getDateYMD() : bankAcc.getInitBalanceDate();
                    bisAccBal.setGetDate(getDate);
                    bisAccBal.setBalDate(balDate);
                    bisAccBal.setBal(bal);
                    bisAccBal.setAvailBal(bal);
                    bisAccBal.setFrzBal(BigDecimal.ZERO);
                    bisAccBal.setStatus(BisAccBalConstant.STATUE_CHECK);
                    // 设置单号
                    String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
                    bisAccBal.setAccNo(accNo);
                    // 设置创建人信息
                    bisAccBal.setCreateBy(loginName);
                    bisAccBal.setCreateDate(curDate);
                    bisAccBal.setUpdateBy(loginName);
                    bisAccBal.setUpdateDate(curDate);
                    entityList.add(bisAccBal);
                    //初始化余额到银行账户实时余额表
                    BtBankAccBal accBal = new BtBankAccBal();
                    accBal.setBankAcc(bankAcc.getBankAcc());
                    accBal.setCurId(curId);
                    accBal.setGetDate(getDate);
                    accBal.setBalDate(balDate);
                    accBal.setBal(bal);
                    accBal.setAvailBal(bal);
                    accBal.setFrzBal(BigDecimal.ZERO);
                    bankAccBals.add(accBal);
                    //生成日志
                    btBankAccMessageService.saveMessage(bisAccBal.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_INIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCBAL, user);
                }
                List<BtBankAccBalDTO> listdto = BeanUtils.copyListProperties(bankAccBals, BtBankAccBalDTO.class);
                bankAccBalService.batchSaveByDTO(listdto);
                this.batchSave(entityList);
                return true;
            } catch (Exception e) {
                logger.error(e.toString());
                ExceptionStackUtils.getStack(e);
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 银行账户创建时初始化余额信息
     *
     * @param bankAccList 银行账户实体对象
     * @return boolean
     */
    public boolean initBisAccBal(List<BtBankAccDTO> bankAccList, ICurrentUser user) {
        List<BisAccBal> entityList = new ArrayList(bankAccList.size());
        List<BtBankAccMessage> msgList = new ArrayList(bankAccList.size());
        try {
            for (BtBankAccDTO bankAcc : bankAccList) {
                if (EmptyUtils.isNotEmpty(bankAcc.getCurList())) {
                    Date curDate = DateUtils.getDate();
                    String loginName = user.getLoginName();
                    Date getDate = DateUtils.getDate();
                    for (String curId : bankAcc.getCurList()) {
                        Long countBisAccBal = this.countBisAccBalBy(bankAcc.getBankAcc(), curId, null, true);
                        // 存在余额信息则不保存
                        if (!EmptyUtils.isEmpty(countBisAccBal) && countBisAccBal > 0L) {
                            continue;
                        }
                        BisAccBal bisAccBal = new BisAccBal();
                        // 设置账户信息
                        bisAccBal.setBankAcc(bankAcc.getBankAcc());
                        bisAccBal.setCorpId(bankAcc.getCorpId());
                        bisAccBal.setBifCode(bankAcc.getBifCode());
                        bisAccBal.setCurId(curId);
                        // 设置余额信息
                        BigDecimal bal = EmptyUtils.isEmpty(bankAcc.getInitBalance()) ? BigDecimal.ZERO : bankAcc.getInitBalance();
                        Date balDate = EmptyUtils.isEmpty(bankAcc.getInitBalanceDate()) ? DateUtils.getDateYMD() : bankAcc.getInitBalanceDate();
                        bisAccBal.setGetDate(getDate);
                        bisAccBal.setBalDate(balDate);
                        bisAccBal.setBal(bal);
                        bisAccBal.setAvailBal(bal);
                        bisAccBal.setFrzBal(BigDecimal.ZERO);
                        bisAccBal.setStatus(BisAccBalConstant.STATUE_CHECK);
                        // 设置单号
                        String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
                        bisAccBal.setAccNo(accNo);
                        // 设置创建人信息
                        bisAccBal.setCreateBy(loginName);
                        bisAccBal.setCreateDate(curDate);
                        bisAccBal.setUpdateBy(loginName);
                        bisAccBal.setUpdateDate(curDate);
                        entityList.add(bisAccBal);
                        //生成日志
                        //BtBankAccMessage msg = btBankAccMessageService.buildMessage(bisAccBal.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_INIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCBAL, user);
                        //if (!EmptyUtils.isEmpty(msg)) {
                        //    msgList.add(msg);
                        //}
                    }
                }
            }
            if (EmptyUtils.isNotEmpty(entityList)) {
                this.batchSave(entityList);
                for (BisAccBal bisAccBal : entityList) {
                    BtBankAccMessageDTO msgDTO = btBankAccMessageService.buildMessage(bisAccBal.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_INIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCBAL, user);
                    BtBankAccMessage msg = new BtBankAccMessage();
                    BeanUtils.copyProperties(msgDTO, msg);
                    if (!EmptyUtils.isEmpty(msg)) {
                        msgList.add(msg);
                    }
                }
                if (EmptyUtils.isNotEmpty(msgList)) {
                    List<BtBankAccMessageDTO> listdto = BeanUtils.copyListProperties(msgList, BtBankAccMessageDTO.class);
                    btBankAccMessageService.batchSaveEntity(listdto);
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 账户流水截止时间功能
     *
     * @param bisAccBalVO
     * @param user
     */
    public void saveComplete(BisAccBalVO bisAccBalVO, ICurrentUser user) {
        try {
            List<Map<String, Object>> btBankAccList = bisAccBalVO.getBtBankAccList();
            if (!EmptyUtils.isEmptyList(btBankAccList)) {
                for (Map<String, Object> map : btBankAccList) {
                    String bankAccId = String.valueOf(map.get("bankAccId"));
                    if (EmptyUtils.isEmpty(bankAccId)) {
                        continue;
                    }
                    String curId = String.valueOf(map.get("curId"));
                    Date endDate = DateUtils.str2Date(String.valueOf(map.get("endDate")), DateUtils.datetimeFormat());
                    this.btBankAccService.updateEndDtlDate(bankAccId, curId, endDate);
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
        }
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        BisAccBal entity = this.get(workFlowResult.getBusinessKey());
        Integer status = workFlowResult.isPass() ? BisAccBalConstant.STATUE_CHECK : BisAccBalConstant.STATUE_BACK;
        entity.setStatus(status);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        Integer messageType = workFlowResult.isPass() ? BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_PASS : BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        //生成日志
        btBankAccMessageService.saveMessage(entity.getId(), messageType, "", BtBankAccMessageConstant.BUS_TYPE_ACCBAL, user);
    }

    /**
     * 获取审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = this.workFlowService.getWorkFlowBusiness(NisWorkFlowConstant.BT_BANKACC_BAL, dataTablesInput);
        String idsSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "b.id", true);
        DataTablesOutput dp = this.bisAccBalDao.getDataTablesApprove(dataTablesInput, idsSql);
        return workFlowBusiness.merge(dp);
    }

    /**
     * 若跨时间创建余额，则补全之前的余额信息
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @param balData 余额日期
     */
    public void saveAfterBisAccBal(String bankAcc, String curId, Date balData, ICurrentUser user) {
        // 之前最近的余额日期
        Date beginDate = this.getLastBalDate(bankAcc, curId, balData, MatchTypeEnum.LT.getValue(), true);
        if (!EmptyUtils.isEmpty(beginDate)) {
            // 之前最近的余额
            BisAccBal bisAccBal = this.getBisAccBalBy(bankAcc, curId, beginDate, true);
            List<BisAccBal> entityList = new ArrayList<>();
            Date curDate = DateUtils.getDate();
            String loginName = user.getLoginName();
            Date getDate = DateUtils.getDate();
            Date endDate = DateUtils.parseDate(balData);
            beginDate = DateUtils.parseDate(beginDate);
            if (endDate.compareTo(DateUtils.addDay(beginDate, 1)) > 0) {
                List<Date> balDateList = this.getBisAccBalDateBy(bisAccBal.getBankAcc(), bisAccBal.getCurId(), beginDate, endDate, false);
                while (balData.compareTo(beginDate) > 0) {
                    beginDate = DateUtils.addDay(beginDate, 1);
                    if (balData.compareTo(beginDate) == 0) {
                        break;
                    }
                    if (balDateList.contains(beginDate)) {
                        continue;
                    }
                    // 当天存在余额则跳过
                    BisAccBal newBisAccBal = new BisAccBal();
                    BeanUtils.copyNotNullProperties(bisAccBal, newBisAccBal);
                    newBisAccBal.setId("");
                    newBisAccBal.setInitSign(CommonConstant.NO);
                    // 设置单号
                    String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
                    newBisAccBal.setAccNo(accNo);
                    newBisAccBal.setBalDate(beginDate);
                    newBisAccBal.setGetDate(getDate);
                    newBisAccBal.setStatus(BisAccBalConstant.STATUE_CHECK);
                    newBisAccBal.setCreateBy(loginName);
                    newBisAccBal.setCreateDate(curDate);
                    newBisAccBal.setUpdateBy(loginName);
                    newBisAccBal.setUpdateDate(curDate);
                    entityList.add(newBisAccBal);
//                    BisAccBal curBal = this.getBisAccBalBy(bisAccBal.getBankAcc(), bisAccBal.getCurId(), beginDate, false);
//                    if (EmptyUtils.isEmpty(curBal)) {
//                        BisAccBal newBisAccBal = new BisAccBal();
//                        BeanUtils.copyNotNullProperties(bisAccBal, newBisAccBal);
//                        // 设置单号
//                        String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
//                        newBisAccBal.setAccNo(accNo);
//                        newBisAccBal.setBalDate(beginDate);
//                        newBisAccBal.setGetDate(getDate);
//                        newBisAccBal.setStatus(BisAccBalConstant.STATUE_CHECK);
//                        newBisAccBal.setCreateBy(loginName);
//                        newBisAccBal.setCreateDate(curDate);
//                        newBisAccBal.setUpdateBy(loginName);
//                        newBisAccBal.setUpdateDate(curDate);
//                        entityList.add(newBisAccBal);
//                    }
                }
            }
            if (EmptyUtils.isNotEmpty(entityList)) {
                this.batchSave(entityList);
            }
            List<BtBankAccMessageDTO> btBankAccMessageList = Lists.newArrayList();
            for (BisAccBal entity : entityList) {
                if (StringUtils.isNotEmpty(entity.getId())) {
                    //生成日志
                    BtBankAccMessageDTO msg = btBankAccMessageService.buildMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_ACCBAL, user);
                    if (!EmptyUtils.isEmpty(msg)) {
                        btBankAccMessageList.add(msg);
                    }
                }
            }
            if (EmptyUtils.isNotEmpty(btBankAccMessageList)) {
                List<BtBankAccMessageDTO> listdto = BeanUtils.copyListProperties(btBankAccMessageList, BtBankAccMessageDTO.class);
                btBankAccMessageService.batchSaveEntity(listdto);
            }
        }
    }

    /**
     * 导入功能
     *
     * @param list
     */
    public void saveImport(List<BisAccBalVO> list, ICurrentUser user) {
        List<BisAccBal> entityList = new ArrayList<>();
        List<BisAccBal> updateList = new ArrayList<>();
        String loginName = user.getLoginName();
        Date curDate = DateUtils.getDate();
        List<String> curNames = list.stream().map(BisAccBalVO::getCurName).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        Map<String, String> nameToIds = currencyService.getCurrencyIdsByNames(curNames);
        List<String> bankAccList = list.stream().map(BisAccBalVO::getBankAcc).collect(Collectors.toList());
        Map<String, BtBankAccDTO> bankAccInfoMap = btBankAccService.getBankAccByInfoAccs(bankAccList);
        for (BisAccBalVO vo : list) {
            BisAccBal bisAccBal = new BisAccBal();
            BeanUtils.copyNotNullProperties(vo, bisAccBal);
            bisAccBal.setStatus(NetbankConstant.STATUE_TEMP);
            bisAccBal.setCreateBy(loginName);
            bisAccBal.setCreateDate(curDate);
            bisAccBal.setUpdateBy(loginName);
            bisAccBal.setUpdateDate(curDate);
            if (EmptyUtils.isEmpty(bisAccBal.getFrzBal())) {
                bisAccBal.setFrzBal(BigDecimal.ZERO);
            }
            if (EmptyUtils.isEmpty(bisAccBal.getAvailBal())) {
                bisAccBal.setAvailBal(bisAccBal.getBal());
            }
            BtBankAccDTO btBankAccDTO = bankAccInfoMap.get(bisAccBal.getBankAcc());
            if (ObjectUtil.isNotEmpty(btBankAccDTO)) {
                bisAccBal.setCorpId(btBankAccDTO.getCorpId());
                bisAccBal.setBifCode(btBankAccDTO.getBifCode());
            }
            if (StringUtils.isNotBlank(nameToIds.get(vo.getCurName())) && StringUtils.isBlank(bisAccBal.getCurId())) {
                bisAccBal.setCurId(nameToIds.get(vo.getCurName().trim()));
            }
            if (StringUtils.isBlank(bisAccBal.getAccNo())) {
                // 设置单号
                String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
                bisAccBal.setAccNo(accNo);
            }
            bisAccBal.setGetDate(curDate);
            BisAccBal bal = this.getBisAccBalBy(bisAccBal.getBankAcc(), bisAccBal.getCurId(), bisAccBal.getBalDate(), false);
            if (ObjectUtil.isEmpty(bal)) {
                entityList.add(bisAccBal);
            }else{
                bisAccBal.setId(bal.getId());
                updateList.add(bisAccBal);
            }
        }
        this.batchSave(entityList);
        this.batchSaveOrUpdate(updateList);
        List<BtBankAccMessageDTO> btBankAccMessageList = Lists.newArrayList();
        for (BisAccBal entity : entityList) {
            saveAfterBisAccBal(entity.getBankAcc(), entity.getCurId(), entity.getBalDate(), user);
            if (StringUtils.isNotEmpty(entity.getId())) {
                //生成日志
                BtBankAccMessageDTO dto = btBankAccMessageService.buildMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_IMPORT, "", BtBankAccMessageConstant.BUS_TYPE_ACCBAL, user);
                if (!EmptyUtils.isEmpty(dto)) {
                    btBankAccMessageList.add(dto);
                }
            }
        }
        List<BtBankAccMessageDTO> listdto = BeanUtils.copyListProperties(btBankAccMessageList, BtBankAccMessageDTO.class);
        btBankAccMessageService.batchSaveEntity(listdto);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(BisAccBalVO vo) {
        return null;
    }

    @Override
    public void verifyHandler(List<BisAccBalVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            //用来存储导入模版里面账号，日期，币别来判断是否有重复
            List<String> balcurdate = new ArrayList<>();
            for (BisAccBalVO vo : list) {
                List<String> msgList = new ArrayList<>();
                BtBankAccDTO bankAcc = null;
                String curId = "";
                // 银行账号不为空
                if (EmptyUtils.isBlank(vo.getBankAcc())) {
                    msgList.add("nis.netbank.message.bankacc.null");
                } else {
                    // 判断银行账号是否在系统中存在
//                    BtBankAccDTO bankAcc = this.btBankAccService.getBankAccInfo(vo.getBankAcc());
                    bankAcc = this.btBankAccService.getBankAccInfoByBankAcc(vo.getBankAcc());
                    if (EmptyUtils.isEmpty(bankAcc)) {
                        msgList.add("nis.netbank.message.bankaccinfo.null");
                    } else {
                        vo.setCorpId(bankAcc.getCorpId());
                        vo.setBifCode(bankAcc.getBifCode());
                        vo.setBankName(bankAcc.getBankName());
                        vo.setAccName(bankAcc.getAccName());
                        if (BtBankAccConstant.ISONLINE_1.equalsIgnoreCase(bankAcc.getIsOnline())) {
                            // 只能导入离线账号
                            msgList.add("nis.netbank.message.online.no");
                        }
                    }
                }
                // 币别不为空
                if (EmptyUtils.isBlank(vo.getCurName())) {
                    msgList.add("nis.netbank.message.curname.null");
                } else {
                    // 判断币别是否在系统中存在
                    curId = this.btCurrencyService.getCurrencyByName(vo.getCurName());
                    if (EmptyUtils.isBlank(curId)) {
                        msgList.add("nis.netbank.message.cur.null");
                    } else {
                        //查询 银行账号币别是否存在
                        BtBankAccDTO accAndCurId = this.btBankAccService.getBankAccByBankAccAndCurId(vo.getBankAcc(), curId);
                        if( accAndCurId == null){
                            //该银行账户未存在该币种
                            msgList.add("nis.netbank.message.bankacc.cur.null");
                        }else {
                            vo.setCurId(curId);
                        }
                    }
                }
                // 余额不能为空
                if (EmptyUtils.isEmpty(vo.getBal())) {
                    msgList.add("nis.netbank.message.bal.null");
                }
                // 余额日期不能为空
                if (EmptyUtils.isEmpty(vo.getBalDate())) {
                    msgList.add("nis.netbank.message.baldate.null");
                } else {
                    if (bankAcc != null && !EmptyUtils.isEmpty(curId)) {
                        BigDecimal bal = this.getBalByBalDate(vo.getBankAcc(), curId, vo.getBalDate(), false);
                        // 当前日期已存在余额
                        if (!EmptyUtils.isEmpty(bal)) {
                            msgList.add("nis.netbank.message.balbybaldate.notnull");
                        }
//                        BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(vo.getBankAcc(), vo.getCurId());
                        // 账户注册日期为空
//                        if (EmptyUtils.isEmpty(btBankAccDTO) || EmptyUtils.isEmpty(btBankAccDTO.getRegDate())) {
                        if (EmptyUtils.isEmpty(bankAcc) || EmptyUtils.isEmpty(bankAcc.getRegDate())) {
                            msgList.add("nis.netbank.message.regdate.null");
                        }
                        // 当前余额日期在注册日期之前
//                        if (!EmptyUtils.isEmpty(btBankAccDTO) && vo.getBalDate().before(btBankAccDTO.getRegDate())) {
                        if (!EmptyUtils.isEmpty(bankAcc) && vo.getBalDate().before(bankAcc.getRegDate())) {
                            msgList.add("bt.bankaccdtl.message.transtime.lt.accreddate");
                        }
                        //校验交易日期不能大于系统当前日期
                        if (vo.getBalDate().after(DateUtils.getDateYMD())) {
                            msgList.add("bt.bankaccdtl.message.baldate.lt.sysdate");
                        }
                    }
                }
                //根据账号日期币别id判断余额是否有重复
                if (balcurdate.contains(vo.getBankAcc() + DateUtils.format(vo.getBalDate(), "yyyyMMdd") + vo.getCurId())) {
                    msgList.add("nis.netbank.message.balcurdate.repetition");
                }
                balcurdate.add(vo.getBankAcc() + DateUtils.format(vo.getBalDate(), "yyyyMMdd") + vo.getCurId());
                vo.addErrorMsg(msgList);
            }
        }
    }

    /**
     * 撤回
     *
     * @param workFlowResult
     */
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        BisAccBal entity = this.get(workFlowResult.getBusinessKey());
        entity.setStatus(CommonConstant.STATUE_TEMP);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        //生成日志
        btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, "", BtBankAccMessageConstant.BUS_TYPE_ACCBAL, user);
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在办的银行账户流水信息，不允许禁用！
            int num = this.bisAccBalDao.getBankAccBalCountByCorpId(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_BANKACC_BAL,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_BANKACC_BAL_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }

    /**
     * 获取账户余额（活期存款）
     *
     * @param user     用户
     * @param resource 资源
     * @return
     */
    public List<BisAccBalVO> listBalSumByCurId(ICurrentUser user, String resource, Date searchDate) {
        List<String> corpIds = corpService.getUserCorpIds(resource, user.getId());
        return this.bisAccBalDao.listBalSumByCurId(searchDate, corpIds);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    public void batchDelete(String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.delete(id);
        }
    }

    /**
     * 批量提交
     *
     * @param ids
     * @param user
     */
    public void batchSubmit(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisAccBal bisAccBal = this.get(id);
            bisAccBal.setStatus(BisAccBalConstant.STATUE_WAIT_CHECK);
            this.updateBisAccBal(bisAccBal, user);
        }
    }

    /**
     * 根据条件统计余额信息
     *
     * @param bankAcc 账户
     * @param curId   币别ID
     * @param balDate 日期,为null则查询最后一次余额
     * @param status  状态，true - 查询已完成 false - 查询未删除
     * @return BisAccBal
     */
    public Long countBisAccBalBy(String bankAcc, String curId, Date balDate, boolean status) {
        return bisAccBalDao.countBisAccBalBy(bankAcc, curId, balDate, status);
    }

    /**
     * 获取账号初始化余额信息
     *
     * @param bankAcc
     * @param curId
     * @return
     */
    public BisAccBal getInitBisAccBal(String bankAcc, String curId) {
        return bisAccBalDao.getInitBisAccBal(bankAcc, curId);
    }

    /**
     * 外汇资金池账户余额查询
     *
     * @param balDate
     * @param bankAcc
     * @param curId
     * @return
     */
    public BtBankAccBalVO getFexCorpPoolBankAccBalance(Date balDate, String bankAcc, String curId) {
        return bisAccBalDao.getFexCorpPoolBankAccBalance(balDate, bankAcc, curId);
    }

    public List<BisAccBalDTO> getBankBal(String bankAcc, String balanceDate) {
        return bisAccBalDao.getBankBal(bankAcc, balanceDate);
    }
    
    @Override
   	public void submitVerify(Map<String, List<String>> params, String workflowPass, boolean isNeedVerify)
   			throws WorkFlowVerifyException {
   	}

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
