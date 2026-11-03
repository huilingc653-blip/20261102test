package com.bytter.biz.fundwarn.service;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.biz.budget.spi.dto.FbsBudgetPeriodDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetPeriodService;
import com.bytter.biz.fundwarn.dao.FundWarnStrategyAnalysisDao;
import com.bytter.biz.fundwarn.entity.BtWarnCondition;
import com.bytter.biz.fundwarn.entity.BtWarnData;
import com.bytter.biz.fundwarn.entity.BtWarnDataDtl;
import com.bytter.biz.fundwarn.spi.constant.FundWarnConstant;
import com.bytter.biz.fundwarn.util.BtFundWarnUtils;
import com.bytter.biz.fundwarn.vo.BtWarnDataInfoVO;
import com.bytter.biz.fundwarn.vo.BtWarnStrategyVO;
import com.bytter.core.base.AbstractService;
import com.bytter.core.utils.*;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预警策略解析操作服务类
 *
 * @author zhengg
 *
 */
@Service("fundWarnStrategyAnalysisService")
@Transactional
public class FundWarnStrategyAnalysisService extends AbstractService {
    @Autowired
    private FundWarnStrategyAnalysisDao fundWarnStrategyAnalysisDao;
    @Autowired
    private FundWarnNotificationService fundWarnNotificationService;
    @Autowired
    private BtWarnStrategyService btWarnStrategyService;
    @Autowired
    private BtWarnDataService btWarnDataService;
    @Autowired
    private BtWarnDataDtlService btWarnDataDtlService;
    @Autowired
    private BtWarningLogService btWarningLogService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    IBankTypeService bankTypeService;
    @DubboReference(check = false)
    private ICorpService corpService;

    @DubboReference(check = false)
    private IFbsBudgetPeriodService budgetPeriodService;
    /**
     * 根据查询条件加载 符合条件的预警策略信息
     *
     * @param params 查询条件
     * @return 返回列表数据
     */
    public List<BtWarnStrategyVO> getTimelyWarnStrategyByParams(Map<String, Object> params) {
        //先通过bankAcc查询到BtBankAccDTO，然后进行下面的查询
        if (params.containsKey("bankAcc")) {
            BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccInfoByBankAcc(params.get("bankAcc").toString());
            if (!EmptyUtils.isEmpty(btBankAccDTO)) {
                params.put("attributeId", btBankAccDTO.getAttributeId());
                params.put("accType", btBankAccDTO.getAccType());
                params.put("isOnline", btBankAccDTO.getIsOnline());
            } else {
                params.remove("bankAcc");
            }
            CorpDTO net = corpService.getNetByCorpId(btBankAccDTO.getCorpId());
            if (ObjectUtils.isNotEmpty(net)) {
                params.put("netId", net.getId());
            }
        }
        List<BtWarnStrategyVO> list = fundWarnStrategyAnalysisDao.getTimelyWarnStrategyByParams(params);
        List<BtWarnStrategyVO> reList = new ArrayList<>();
        for (BtWarnStrategyVO entity : list) {
            if (FundWarnConstant.TYPECODE_DAYSUMPAY.equals(params.get("typeCode"))
                    || FundWarnConstant.TYPECODE_BUDGETPROGRESS.equals(params.get("typeCode"))) {
                //当日累计付款  预算执行进度 的预警条件处理需要自己统计业务数据判断
                reList.add(entity);
            } else if (params.containsKey("conditionMoney")) {
                BigDecimal conditionMoney = NumberUtil.objToBigDecimal(params.get("conditionMoney"));
                if (conditionMoney != null && BtFundWarnUtils.checkConditionMoney(entity, conditionMoney)) {
                    reList.add(entity);
                }
            }
        }
        return reList;
    }


    /**
     * 根据策略进行 提醒数据写入和消息发送
     *
     * @param entity 预警策略对象
     */
    public void executeStrategy(BtWarnStrategyVO entity) {
        executeStrategy(entity, null);
    }

    /**
     * 根据策略进行 提醒数据写入和消息发送
     *
     * @param entity 预警策略对象
     */
    public void executeStrategy(BtWarnStrategyVO entity, Map<String, Object> dataInfo) {
        if (FundWarnConstant.TYPECODE_EQCORPPAY.equals(entity.getTypeCode())) {
            executeEqcorppayStrategy(entity, dataInfo);
        } else if (FundWarnConstant.TYPECODE_DAYSUMPAY.equals(entity.getTypeCode())) {
            executeDaySumPayStrategy(entity, dataInfo);
        } else if (FundWarnConstant.TYPECODE_BUDGETPROGRESS.equals(entity.getTypeCode())) {
            if (dataInfo != null) {
                executeBudgetProgressStrategy(entity, dataInfo);
            } else {
                executeBudgetProgressCorpStrategy(entity);
            }
        } else if (FundWarnConstant.TYPECODE_BANKACCPAY.equals(entity.getTypeCode())) {
            executeBankaccpayStrategy(entity, dataInfo);
        } else if (FundWarnConstant.TYPECODE_CASHPAY.equals(entity.getTypeCode())) {
            executeCashpayStrategy(entity, dataInfo);
        } else if (FundWarnConstant.TYPECODE_BALOVER.equals(entity.getTypeCode())) {
            if (FundWarnConstant.DIMENSIONCODE_CORP.equals(entity.getDimensionCode())) {
                executeBaloverCorpStrategy(entity);
            } else if (FundWarnConstant.DIMENSIONCODE_BANKACC.equals(entity.getDimensionCode())) {
                executeBaloverBankAccStrategy(entity);
            }
        } else if (FundWarnConstant.TYPECODE_CASHOVER.equals(entity.getTypeCode())) {
            if (FundWarnConstant.DIMENSIONCODE_CORP.equals(entity.getDimensionCode())) {
                executeCashoverCorpStrategy(entity);
            } else if (FundWarnConstant.DIMENSIONCODE_FCACC.equals(entity.getDimensionCode())) {
                executeCashoverFcAccStrategy(entity);
            }
        } else if (FundWarnConstant.TYPECODE_INFLOWOVER.equals(entity.getTypeCode())) {
            if (FundWarnConstant.DIMENSIONCODE_CORP.equals(entity.getDimensionCode())) {
                executeInflowOverCorpStrategy(entity);
            } else if (FundWarnConstant.DIMENSIONCODE_BANKACC.equals(entity.getDimensionCode())) {
                executeInflowOverBankAccStrategy(entity);
            }
        } else if (FundWarnConstant.TYPECODE_BALDOWN.equals(entity.getTypeCode())) {
            if (FundWarnConstant.DIMENSIONCODE_CORP.equals(entity.getDimensionCode())) {
                executeBalDownCorpStrategy(entity);
            } else if (FundWarnConstant.DIMENSIONCODE_BANKACC.equals(entity.getDimensionCode())) {
                executeBalDownBankAccStrategy(entity);
            }
        } else if (FundWarnConstant.TYPECODE_OUTPAY.equals(entity.getTypeCode())) {
            if (FundWarnConstant.DIMENSIONCODE_CORP.equals(entity.getDimensionCode())) {
                executeOutPayCorpStrategy(entity);
            } else if (FundWarnConstant.DIMENSIONCODE_BANKACC.equals(entity.getDimensionCode())) {
                executeOutPayBankAccStrategy(entity);
            }
        } else if (FundWarnConstant.TYPECODE_EXPENDINFLOW.equals(entity.getTypeCode())) {
            if (FundWarnConstant.DIMENSIONCODE_CORP.equals(entity.getDimensionCode())) {
                executeExpendInflowCorpStrategy(entity);
            } else if (FundWarnConstant.DIMENSIONCODE_BANKACC.equals(entity.getDimensionCode())) {
                executeExpendInflowBankAccStrategy(entity);
            }
        } else if (FundWarnConstant.TYPECODE_DEPOSITLOANOVER.equals(entity.getTypeCode())) {
            if (FundWarnConstant.DIMENSIONCODE_CORP.equals(entity.getDimensionCode())) {
                executeDepositLoanOverCorpStrategy(entity);
            }
        } else if (FundWarnConstant.TYPECODE_OLDBANKACC.equals(entity.getTypeCode())) {
            if (FundWarnConstant.DIMENSIONCODE_BANKACC.equals(entity.getDimensionCode())) {
                executeOldBankAccByBankAccStrategy(entity);
            }
        }

    }


    /**
     * 净流入透支-单位维度策略处理
     *
     * @param entity
     */
    public synchronized void executeInflowOverCorpStrategy(BtWarnStrategyVO entity) {
        //获取余额超标-单位维度满足条件的数据
        Map<String, Object> paramsData = new HashMap<>();
        String sqlW = BtFundWarnUtils.getConditionSql(entity, "aInfo.triggerNum", paramsData);
        paramsData.put("warnId", entity.getId());
        Date endDate = DateUtils.getMonthStartTime();
        Date warnDate = DateUtils.addDay(endDate, -1);
        Date beginDate = DateUtils.addMonth(endDate, -1);
        if (entity.getRunDate() != null) {
            beginDate = DateUtils.getMonthFirstDate(entity.getRunDate());
            endDate = DateUtils.addMonth(beginDate, 1);
            warnDate = DateUtils.addDay(endDate, -1);
        }
        paramsData.put("beginDate", beginDate);
        paramsData.put("endDate", endDate);
        List<BtWarnData> warnDatas = fundWarnStrategyAnalysisDao.getInflowOverCorpDataByParams(paramsData, sqlW);
        if (!ListUtils.isNullOrEmpty(warnDatas) && warnDatas.size() > 0) {
            for (BtWarnData data : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("corpId", data.getCorpId());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                BeanUtils.copyNotNullProperties(data, warnData);
                warnData.setCorpId(data.getCorpId());
                warnData.setOneNum(data.getOneNum());
                warnData.setTwoNum(data.getTwoNum());
                warnData.setTriggerNum(data.getTriggerNum());
                warnData.setWarnDate(warnDate);
                btWarnDataService.saveOrUpdate(warnData);

                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);

                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }

                fundWarnNotificationService.sendMsg(warnDataVO);

                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 净流入透支-银行账户维度策略处理
     *
     * @param entity
     */
    public synchronized void executeInflowOverBankAccStrategy(BtWarnStrategyVO entity) {
        //获取余额超标-单位维度满足条件的数据
        Map<String, Object> paramsData = new HashMap<>();
        String sqlW = BtFundWarnUtils.getConditionSql(entity, "aInfo.triggerNum", paramsData);
        paramsData.put("warnId", entity.getId());
        Date endDate = DateUtils.getMonthStartTime();
        Date warnDate = DateUtils.addDay(endDate, -1);
        Date beginDate = DateUtils.addMonth(endDate, -1);
        if (entity.getRunDate() != null) {
            beginDate = DateUtils.getMonthFirstDate(entity.getRunDate());
            endDate = DateUtils.addMonth(beginDate, 1);
            warnDate = DateUtils.addDay(endDate, -1);
        }
        paramsData.put("beginDate", beginDate);
        paramsData.put("endDate", endDate);
        List<BtWarnData> warnDatas = fundWarnStrategyAnalysisDao.getInflowOverBankAccDataByParams(paramsData, sqlW);
        if (!ListUtils.isNullOrEmpty(warnDatas) && warnDatas.size() > 0) {
            for (BtWarnData data : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("bankAcc", data.getBankAcc());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                warnData.setCorpId(data.getCorpId());
                warnData.setBankAcc(data.getBankAcc());
                warnData.setOneNum(data.getOneNum());
                warnData.setTwoNum(data.getTwoNum());
                warnData.setTriggerNum(data.getTriggerNum());
                warnData.setWarnDate(warnDate);
                btWarnDataService.saveOrUpdate(warnData);

                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);
                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }
                fundWarnNotificationService.sendMsg(warnDataVO);

                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 余额超标-单位维度策略处理
     *
     * @param entity
     */
    public synchronized void executeBaloverCorpStrategy(BtWarnStrategyVO entity) {
        //获取余额超标-单位维度满足条件的数据
        Map<String, Object> paramsData = new HashMap<>();
        String sqlW = BtFundWarnUtils.getConditionSql(entity, "info.bal", paramsData);
        paramsData.put("warnId", entity.getId());
        Date warnDate = entity.getRunDate();
        if (warnDate == null) {
            warnDate = DateUtils.getDateYMD();
        }
        paramsData.put("balDate", warnDate);
        List<BtWarnData> warnDatas = fundWarnStrategyAnalysisDao.getBaloverCorpDataByParams(paramsData, sqlW);
        if (!ListUtils.isNullOrEmpty(warnDatas) && warnDatas.size() > 0) {
            for (BtWarnData balData : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("corpId", balData.getCorpId());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                warnData.setCorpId(balData.getCorpId());
                warnData.setTriggerNum(balData.getTriggerNum());
                warnData.setWarnDate(warnDate);
                btWarnDataService.saveOrUpdate(warnData);

                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);
                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }
                fundWarnNotificationService.sendMsg(warnDataVO);

                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 余额超标-银行账户维度策略处理
     *
     * @param entity synchronized
     */
    public synchronized void executeBaloverBankAccStrategy(BtWarnStrategyVO entity) {
        //获取余额超标-单位维度满足条件的数据
        Map<String, Object> paramsData = new HashMap<>();
        String sqlW = BtFundWarnUtils.getConditionSql(entity, "ab.bal", paramsData);
        paramsData.put("warnId", entity.getId());
        Date warnDate = entity.getRunDate();
        if (warnDate == null) {
            warnDate = DateUtils.getDateYMD();
        }
        paramsData.put("balDate", warnDate);
        paramsData.put("balDateStr", DateUtils.formatDate(warnDate, "yyyy-MM-dd"));
        List<BtWarnData> warnDatas = fundWarnStrategyAnalysisDao.getBaloverBankAccDataByParams(paramsData, sqlW);
        Map<String, BankTypeDTO> mapType = bankTypeService.getBankTypeMap();

        if (!ListUtils.isNullOrEmpty(warnDatas) && warnDatas.size() > 0) {
            for (BtWarnData balData : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("bankAcc", balData.getBankAcc());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                warnData.setCorpId(balData.getCorpId());
                warnData.setBankAcc(balData.getBankAcc());
                BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccInfoByBankAcc(balData.getBankAcc());
                if (!ObjectUtils.isEmpty(btBankAccDTO)) {
                    warnData.setBankTypeName(!ObjectUtils.isEmpty(mapType.get(btBankAccDTO.getBankTypeId())) ? mapType.get(btBankAccDTO.getBankTypeId()).getTypeName() : "");
                }
                warnData.setTriggerNum(balData.getTriggerNum());
                warnData.setWarnDate(warnDate);
                warnData.setCreateDate(new Date());
                btWarnDataService.saveOrUpdateEntity(warnData);
                logger.info("生成预警数据:{}", warnData);
                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);
                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }
                fundWarnNotificationService.sendMsg(warnDataVO);
                logger.info("进行消息提醒:{}", warnDataVO);

                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
                logger.info("记录新日志:{}", btWarningLogService);
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }
//
//	/**
//	 * 支出户流入-单位维度策略处理
//	 * @param entity
//	 */
//	public synchronized void executeExpendInflowCorpStrategy(BtWarnStrategyVO entity){
//		//获取余额超标-单位维度满足条件的数据
//		Map<String, Object> paramsData=new HashMap<>();
//		paramsData.put("warnId", entity.getId());
//		Date warnDate=entity.getRunDate();
//		if(warnDate==null){
//			warnDate=DateUtils.getDateYMD();
//		}
//		paramsData.put("beginDate", warnDate);
//		paramsData.put("endDate", DateUtils.addDay(warnDate, 1));
//		String sqlW=BtFundWarnUtils.getConditionSql(entity, "dtl.amt" ,paramsData);
//		List<BtWarnDataDtl> warnDataDtls=fundWarnStrategyAnalysisDao.getExpendInflowCorpDataByParams(paramsData, sqlW);
//		List<BtWarnData> warnDatas=getBtWarnDataListByBtWarnDataDtlList(warnDataDtls, entity);
//		if(!ListUtils.isNullOrEmpty(warnDatas)&&warnDatas.size()>0){
//			for(BtWarnData data:warnDatas){
//				//消息提醒传递VO
//				BtWarnDataInfoVO warnDataVO=new BtWarnDataInfoVO();
//				Map<String, Object> params=new HashMap<>();
//				params.put("wsId", entity.getId());
//				params.put("corpId", data.getCorpId());
//				params.put("warnDate", warnDate);
//				BtWarnData warnData=btWarnDataService.getBtWarnDataByParams(params);
//				String logType=null;
//				String msg=BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode())+"-"
//						+BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
//				if(warnData==null){
//					warnData=new BtWarnData();
//					warnDataVO.setSendMsgSign("1");
//					logType=FundWarnConstant.LOGTYPE_ADD;
//					msg+="，新增预测触发数据信息！";
//				}else{
//					logType=FundWarnConstant.LOGTYPE_UPDATE;
//					msg+="，修改预测触发数据信息！";
//				}
//				setBtWarnData(entity, warnData);
//				warnData.setCorpId(data.getCorpId());
//				warnData.setTriggerNum(data.getTriggerNum());
//				warnData.setWarnDate(warnDate);
//				if(data.getDataDtlList()!=null){
//					warnData.setSizeNum(data.getDataDtlList().size());
//					warnData.setTriggerNum(data.getTriggerNum());
//				}
//				btWarnDataService.saveOrUpdate(warnData);
//
//				//写入明细数据
//				btWarnDataDtlService.batchStrategyDataDtl(data.getDataDtlList(),warnData);
//
//				//进行消息提醒
//				BeanUtils.copyProperties(warnData, warnDataVO);
//				this.inject(warnDataVO);
//				fundWarnNotificationService.sendMsg(warnDataVO);
//
//				//记录新日志
//				btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
//			}
//		}
//		//记录策略执行日志
//		btWarningLogService.saveBtWarningLog(entity.getId(),
//				FundWarnConstant.LOGTYPE_EXECUTE,
//				BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode())+"-"
//						+BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode())+",定时策略被执行！",
//						entity.getCurrentUser());
//	}
//
//	/**
//	 * 支出户流入-银行账户维度策略处理
//	 * @param entity
//	 */
//	public synchronized void executeExpendInflowBankAccStrategy(BtWarnStrategyVO entity){
//		//获取余额超标-单位维度满足条件的数据
//		Map<String, Object> paramsData=new HashMap<>();
//		paramsData.put("warnId", entity.getId());
//		Date warnDate=entity.getRunDate();
//		if(warnDate==null){
//			warnDate=DateUtils.getDateYMD();
//		}
//		paramsData.put("beginDate", warnDate);
//		paramsData.put("endDate", DateUtils.addDay(warnDate, 1));
//		String sqlW=BtFundWarnUtils.getConditionSql(entity, "dtl.amt" ,paramsData);
//		List<BtWarnDataDtl> warnDataDtls=fundWarnStrategyAnalysisDao.getExpendInflowBankAccDataByParams(paramsData, sqlW);
//		List<BtWarnData> warnDatas=getBtWarnDataListByBtWarnDataDtlList(warnDataDtls, entity);
//		if(!ListUtils.isNullOrEmpty(warnDatas)&&warnDatas.size()>0){
//			for(BtWarnData data:warnDatas){
//				//消息提醒传递VO
//				BtWarnDataInfoVO warnDataVO=new BtWarnDataInfoVO();
//				Map<String, Object> params=new HashMap<>();
//				params.put("wsId", entity.getId());
//				params.put("bankAcc", data.getBankAcc());
//				params.put("warnDate", warnDate);
//				BtWarnData warnData=btWarnDataService.getBtWarnDataByParams(params);
//				String logType=null;
//				String msg=BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode())+"-"
//						+BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
//				if(warnData==null){
//					warnData=new BtWarnData();
//					warnDataVO.setSendMsgSign("1");
//					logType=FundWarnConstant.LOGTYPE_ADD;
//					msg+="，新增预测触发数据信息！";
//				}else{
//					logType=FundWarnConstant.LOGTYPE_UPDATE;
//					msg+="，修改预测触发数据信息！";
//				}
//				setBtWarnData(entity, warnData);
//				warnData.setCorpId(data.getCorpId());
//				warnData.setBankAcc(data.getBankAcc());
//				warnData.setTriggerNum(data.getTriggerNum());
//				warnData.setWarnDate(warnDate);
//				if(data.getDataDtlList()!=null){
//					warnData.setSizeNum(data.getDataDtlList().size());
//					warnData.setTriggerNum(data.getTriggerNum());
//				}
//				btWarnDataService.saveOrUpdate(warnData);
//
//				//写入明细数据
//				btWarnDataDtlService.batchStrategyDataDtl(data.getDataDtlList(),warnData);
//
//				//进行消息提醒
//				BeanUtils.copyProperties(warnData, warnDataVO);
//				this.inject(warnDataVO);
//				fundWarnNotificationService.sendMsg(warnDataVO);
//
//				//记录新日志
//				btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
//			}
//		}
//		//记录策略执行日志
//		btWarningLogService.saveBtWarningLog(entity.getId(),
//				FundWarnConstant.LOGTYPE_EXECUTE,
//				BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode())+"-"
//						+BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode())+",定时策略被执行！",
//						entity.getCurrentUser());
//	}
//

    /**
     * 支出户流入-单位维度策略处理
     *
     * @param entity
     */
    public synchronized void executeExpendInflowCorpStrategy(BtWarnStrategyVO entity) {
        //获取余额超标-单位维度满足条件的数据
        Map<String, Object> paramsData = new HashMap<>();
        paramsData.put("warnId", entity.getId());
        Date warnDate = entity.getRunDate();
        if (warnDate == null) {
            warnDate = DateUtils.getDateYMD();
        }
        paramsData.put("beginDate", warnDate);
        paramsData.put("endDate", DateUtils.addDay(warnDate, 1));
        String sqlW = BtFundWarnUtils.getConditionSql(entity, "info.triggerNum", paramsData);
        List<BtWarnData> warnDatas = fundWarnStrategyAnalysisDao.getExpendInflowCorpDataByParams(paramsData, sqlW);
        if (!ListUtils.isNullOrEmpty(warnDatas) && !warnDatas.isEmpty()) {
            for (BtWarnData data : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("corpId", data.getCorpId());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                warnData.setSizeNum(data.getSizeNum());
                warnData.setCorpId(data.getCorpId());
                warnData.setTriggerNum(data.getTriggerNum());
                warnData.setWarnDate(warnDate);
                if (data.getDataDtlList() != null) {
                    warnData.setSizeNum(data.getDataDtlList().size());
                    warnData.setTriggerNum(data.getTriggerNum());
                }
                btWarnDataService.saveOrUpdate(warnData);

                paramsData.put("corpId", warnData.getCorpId());
                List<BtWarnDataDtl> warnDataDtls = fundWarnStrategyAnalysisDao.getExpendInflowCorpDataDtlByParams(paramsData);
                if (!ListUtils.isNullOrEmpty(warnDataDtls)) {
                    //写入明细数据
                    btWarnDataDtlService.batchStrategyDataDtl(warnDataDtls, warnData);
                }

                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);
                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }
                fundWarnNotificationService.sendMsg(warnDataVO);

                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 支出户流入-银行账户维度策略处理
     *
     * @param entity
     */
    public synchronized void executeExpendInflowBankAccStrategy(BtWarnStrategyVO entity) {
        //获取余额超标-单位维度满足条件的数据
        Map<String, Object> paramsData = new HashMap<>();
        paramsData.put("warnId", entity.getId());
        Date warnDate = entity.getRunDate();
        if (warnDate == null) {
            warnDate = DateUtils.getDateYMD();
        }
        paramsData.put("beginDate", warnDate);
        paramsData.put("endDate", DateUtils.addDay(warnDate, 1));
        String sqlW = BtFundWarnUtils.getConditionSql(entity, "info.triggerNum", paramsData);
        List<BtWarnData> warnDatas = fundWarnStrategyAnalysisDao.getExpendInflowBankAccDataByParams(paramsData, sqlW);
        if (!ListUtils.isNullOrEmpty(warnDatas) && !warnDatas.isEmpty()) {
            for (BtWarnData data : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("bankAcc", data.getBankAcc());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                warnData.setCorpId(data.getCorpId());
                warnData.setBankAcc(data.getBankAcc());
                warnData.setTriggerNum(data.getTriggerNum());
                warnData.setWarnDate(warnDate);
                warnData.setSizeNum(data.getSizeNum());
                if (data.getDataDtlList() != null) {
                    warnData.setSizeNum(data.getDataDtlList().size());
                    warnData.setTriggerNum(data.getTriggerNum());
                }
                btWarnDataService.saveOrUpdate(warnData);
                paramsData.put("bankAcc", warnData.getBankAcc());
                List<BtWarnDataDtl> warnDataDtls = fundWarnStrategyAnalysisDao.getExpendInflowBankAccDataDtlByParams(paramsData);
                if (!ListUtils.isNullOrEmpty(warnDataDtls)) {
                    //写入明细数据
                    btWarnDataDtlService.batchStrategyDataDtl(warnDataDtls, warnData);
                }

                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);
                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }
                fundWarnNotificationService.sendMsg(warnDataVO);

                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 系统外付款-单位维度策略处理
     *
     * @param entity
     */
    public synchronized void executeOutPayCorpStrategy(BtWarnStrategyVO entity) {
        //获取余额超标-单位维度满足条件的数据
        Map<String, Object> paramsData = new HashMap<>();
        paramsData.put("warnId", entity.getId());
        Date warnDate = entity.getRunDate();
        if (warnDate == null) {
            warnDate = DateUtils.getDateYMD();
        }
        paramsData.put("beginDate", warnDate);
        paramsData.put("endDate", DateUtils.addDay(warnDate, 1));
        List<BtWarnDataDtl> warnDataDtls = fundWarnStrategyAnalysisDao.getOutPayCorpDataByParams(paramsData);
        List<BtWarnData> warnDatas = getBtWarnDataListByBtWarnDataDtlList(warnDataDtls, entity);
        if (!ListUtils.isNullOrEmpty(warnDatas) && warnDatas.size() > 0) {
            for (BtWarnData data : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("corpId", data.getCorpId());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                warnData.setCorpId(data.getCorpId());
                warnData.setTriggerNum(data.getTriggerNum());
                warnData.setWarnDate(warnDate);
                if (data.getDataDtlList() != null) {
                    warnData.setSizeNum(data.getDataDtlList().size());
                }
                btWarnDataService.saveOrUpdate(warnData);

                //写入明细数据
                btWarnDataDtlService.batchStrategyDataDtl(data.getDataDtlList(), warnData);

                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);
                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }
                fundWarnNotificationService.sendMsg(warnDataVO);

                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 系统外付款-银行账户维度策略处理
     *
     * @param entity
     */
    public synchronized void executeOutPayBankAccStrategy(BtWarnStrategyVO entity) {
        //获取余额超标-单位维度满足条件的数据
        Map<String, Object> paramsData = new HashMap<>();
        paramsData.put("warnId", entity.getId());
        Date warnDate = entity.getRunDate();
        if (warnDate == null) {
            warnDate = DateUtils.getDateYMD();
        }
        paramsData.put("beginDate", warnDate);
        paramsData.put("endDate", DateUtils.addDay(warnDate, 1));
        List<BtWarnDataDtl> warnDataDtls = fundWarnStrategyAnalysisDao.getOutPayBankAccDataByParams(paramsData);
        List<BtWarnData> warnDatas = getBtWarnDataListByBtWarnDataDtlList(warnDataDtls, entity);
        if (!ListUtils.isNullOrEmpty(warnDatas) && warnDatas.size() > 0) {
            for (BtWarnData data : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("bankAcc", data.getBankAcc());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                warnData.setCorpId(data.getCorpId());
                warnData.setBankAcc(data.getBankAcc());
                warnData.setTriggerNum(data.getTriggerNum());
                warnData.setWarnDate(warnDate);
                if (data.getDataDtlList() != null) {
                    warnData.setSizeNum(data.getDataDtlList().size());
                }
                btWarnDataService.saveOrUpdate(warnData);

                //写入明细数据
                btWarnDataDtlService.batchStrategyDataDtl(data.getDataDtlList(), warnData);

                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);
                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }
                fundWarnNotificationService.sendMsg(warnDataVO);

                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 账户余额持续下降-单位维度策略处理
     *
     * @param entity
     */
    public synchronized void executeBalDownCorpStrategy(BtWarnStrategyVO entity) {
        Date warnDate = DateUtils.addDay(DateUtils.getMonthStartTime(), -1);
        if (entity.getRunDate() != null) {
            warnDate = DateUtils.addDay(DateUtils.getNextMonthFirstDate(entity.getRunDate()), -1);
        }
        List<BtWarnData> warnDatas = getBalDownDataInfo(entity, warnDate);
        if (!ListUtils.isNullOrEmpty(warnDatas) && warnDatas.size() > 0) {
            for (BtWarnData data : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("corpId", data.getCorpId());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                warnData.setCorpId(data.getCorpId());
                warnData.setTriggerNum(data.getTriggerNum());
                warnData.setWarnDate(warnDate);
                btWarnDataService.saveOrUpdate(warnData);

                if (!ListUtils.isNullOrEmpty(data.getDataDtlList())) {
                    btWarnDataDtlService.batchStrategyDataDtl(data.getDataDtlList(), warnData);
                }

                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);
                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }
                fundWarnNotificationService.sendMsg(warnDataVO);

                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 账户余额持续下降-银行账户维度策略处理
     *
     * @param entity
     */
    public synchronized void executeBalDownBankAccStrategy(BtWarnStrategyVO entity) {
        Date warnDate = DateUtils.addDay(DateUtils.getMonthStartTime(), -1);
        if (entity.getRunDate() != null) {
            //warnDate=DateUtils.addDay(DateUtils.getNextMonthFirstDate(entity.getRunDate()),-1);
            warnDate = DateUtils.addDay(DateUtils.getMonthFirstDate(entity.getRunDate()), -1);
        }
        List<BtWarnData> warnDatas = getBalDownDataInfo(entity, warnDate);
        if (!ListUtils.isNullOrEmpty(warnDatas) && warnDatas.size() > 0) {
            for (BtWarnData data : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("bankAcc", data.getBankAcc());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                warnData.setCorpId(data.getCorpId());
                warnData.setBankAcc(data.getBankAcc());
                warnData.setTriggerNum(data.getTriggerNum());
                warnData.setWarnDate(warnDate);
                btWarnDataService.saveOrUpdate(warnData);
                if (!ListUtils.isNullOrEmpty(data.getDataDtlList())) {
                    btWarnDataDtlService.batchStrategyDataDtl(data.getDataDtlList(), warnData);
                }

                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);
                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }
                fundWarnNotificationService.sendMsg(warnDataVO);

                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 库存现金超标-单位维度策略处理
     *
     * @param entity
     */
    public synchronized void executeCashoverCorpStrategy(BtWarnStrategyVO entity) {
        //获取余额超标-单位维度满足条件的数据
        Map<String, Object> paramsData = new HashMap<>();
        String sqlW = BtFundWarnUtils.getConditionSql(entity, "info.bal", paramsData);
        paramsData.put("warnId", entity.getId());
        Date warnDate = DateUtils.getDateYMD();
//		paramsData.put("balDate", warnDate);
        List<BtWarnData> warnDatas = fundWarnStrategyAnalysisDao.getCashoverCorpDataByParams(paramsData, sqlW);
        if (!ListUtils.isNullOrEmpty(warnDatas) && warnDatas.size() > 0) {
            for (BtWarnData balData : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("corpId", balData.getCorpId());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                warnData.setCorpId(balData.getCorpId());
                warnData.setTriggerNum(balData.getTriggerNum());
                warnData.setWarnDate(warnDate);
                btWarnDataService.saveOrUpdate(warnData);

                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);
                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }
                fundWarnNotificationService.sendMsg(warnDataVO);
                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 存贷比超标-单位维度策略处理
     *
     * @param entity
     */
    public synchronized void executeDepositLoanOverCorpStrategy(BtWarnStrategyVO entity) {
        //获取存贷比超标-单位维度满足条件的数据
        Map<String, Object> paramsData = new HashMap<>();
        String sqlW = BtFundWarnUtils.getConditionSql(entity, "info.triggerNum", paramsData);
        paramsData.put("warnId", entity.getId());
        Date warnDate = DateUtils.getDateYMD();
        List<BtWarnData> warnDatas = fundWarnStrategyAnalysisDao.getDepositLoanOverCorpDataByParams(paramsData, sqlW);
        if (!ListUtils.isNullOrEmpty(warnDatas) && !warnDatas.isEmpty()) {
            for (BtWarnData data : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("corpId", data.getCorpId());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                warnData.setCorpId(data.getCorpId());
                warnData.setTriggerNum(data.getTriggerNum());
                warnData.setWarnDate(warnDate);
                warnData.setOneNum(data.getOneNum());
                warnData.setTwoNum(data.getTwoNum());
                btWarnDataService.saveOrUpdate(warnData);

                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);
                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }
                fundWarnNotificationService.sendMsg(warnDataVO);
                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 库存现金超标-内部账户维度策略处理
     *
     * @param entity
     */
    public synchronized void executeCashoverFcAccStrategy(BtWarnStrategyVO entity) {
        //获取余额超标-单位维度满足条件的数据
        Map<String, Object> paramsData = new HashMap<>();
        String sqlW = BtFundWarnUtils.getConditionSql(entity, "ab.check_balance", paramsData);
        paramsData.put("warnId", entity.getId());
        Date warnDate = DateUtils.getDateYMD();
//		paramsData.put("balDate", warnDate);
        List<BtWarnData> warnDatas = fundWarnStrategyAnalysisDao.getCashoverFcAccDataByParams(paramsData, sqlW);
        if (!ListUtils.isNullOrEmpty(warnDatas) && warnDatas.size() > 0) {
            for (BtWarnData balData : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("fcAcc", balData.getFcAcc());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                warnData.setCorpId(balData.getCorpId());
                warnData.setFcAcc(balData.getFcAcc());
                warnData.setTriggerNum(balData.getTriggerNum());
                warnData.setWarnDate(warnDate);
                btWarnDataService.saveOrUpdate(warnData);

                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);
                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }
                fundWarnNotificationService.sendMsg(warnDataVO);
                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 相同公司付款策略处理
     *
     * @param entity
     */
    public synchronized void executeEqcorppayStrategy(BtWarnStrategyVO entity, Map<String, Object> dataInfo) {

//		判断此数据是否有相同公司付款的的信息
        //查询对应条件的付款明细数据
        Map<String, Object> paramsDataDtl = new HashMap<>();
        paramsDataDtl.put("cur_id", entity.getCurId());
        paramsDataDtl.put("warnId", entity.getId());
        paramsDataDtl.put("pay_money", NumberUtil.objToBigDecimal(dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_PAYMONEY)));
        paramsDataDtl.put("wish_payday", (Date) dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_PAYDATE));
        paramsDataDtl.put("payee_acc_name", String.valueOf(dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_PAYEENAME)));
        paramsDataDtl.put("corp_id", String.valueOf(dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_CORPID)));
        List<BtWarnDataDtl> warnDataDtllist = getEqcorppayDtlDataByParams(paramsDataDtl);
        if (!ListUtils.isNullOrEmpty(warnDataDtllist) && warnDataDtllist.size() > 1) {

            //消息提醒传递VO
            BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
            Map<String, Object> params = new HashMap<>();
            params.put("wsId", entity.getId());
            params.put("corpId", String.valueOf(dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_CORPID)));
            params.put("payeeName", String.valueOf(dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_PAYEENAME)));
            params.put("warnDate", (Date) dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_PAYDATE));
            params.put("triggerNum", NumberUtil.objToBigDecimal(dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_PAYMONEY)));
            BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
            String logType = null;
            String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                    + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
            if (warnData == null) {
                warnData = new BtWarnData();
                warnDataVO.setSendMsgSign("1");
                logType = FundWarnConstant.LOGTYPE_ADD;
                msg += "，新增预测触发数据信息！";
            } else {
                logType = FundWarnConstant.LOGTYPE_UPDATE;
                msg += "，修改预测触发数据信息！";
            }
            setBtWarnData(entity, warnData);
            warnData.setCorpId(String.valueOf(dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_CORPID)));
            warnData.setPayeeName(String.valueOf(dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_PAYEENAME)));
//			warnData.setBankAcc(String.valueOf(dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_BANKACC)));
            warnData.setTriggerNum(NumberUtil.objToBigDecimal(dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_PAYMONEY)));
            warnData.setWarnDate((Date) dataInfo.get(FundWarnConstant.EQCORPPAY.FIELD_PAYDATE));
            warnData.setSizeNum(warnDataDtllist.size());
            btWarnDataService.saveOrUpdate(warnData);

            //写入明细数据
            btWarnDataDtlService.batchStrategyDataDtl(warnDataDtllist, warnData);

            //进行消息提醒
            BeanUtils.copyProperties(warnData, warnDataVO);
            if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
            }
            fundWarnNotificationService.sendMsg(warnDataVO);

            //记录新日志
            btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",事中策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 当日累计支付策略处理
     *
     * @param entity
     */
    public synchronized void executeDaySumPayStrategy(BtWarnStrategyVO entity, Map<String, Object> dataInfo) {
        //查询对应条件的付款明细数据
        Map<String, Object> paramsData = new HashMap<>();
        paramsData.put("cur_id", entity.getCurId());
        paramsData.put("warnId", entity.getId());
        paramsData.put("wish_payday", (Date) dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_PAYDATE));
        paramsData.put("billNo", String.valueOf(dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_NO)));
        paramsData.put("corp_id", String.valueOf(dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_CORPID)));
        String wMoney = BtFundWarnUtils.getConditionSql(entity, "info.triggerNum", paramsData);
        BtWarnData data = getDaySumPayDataByParams(paramsData, wMoney);
        if (data != null) {
            //消息提醒传递VO
            BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
            Map<String, Object> params = new HashMap<>();
            params.put("wsId", entity.getId());
            params.put("corpId", String.valueOf(dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_CORPID)));
            params.put("warnDate", (Date) dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_PAYDATE));
            BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
            String logType = null;
            String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                    + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
            if (warnData == null) {
                warnData = new BtWarnData();
                warnDataVO.setSendMsgSign("1");
                logType = FundWarnConstant.LOGTYPE_ADD;
                msg += "，新增预测触发数据信息！";
            } else {
                logType = FundWarnConstant.LOGTYPE_UPDATE;
                msg += "，修改预测触发数据信息！";
            }
            setBtWarnData(entity, warnData);
            warnData.setCorpId(String.valueOf(dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_CORPID)));
            warnData.setTriggerNum(data.getTriggerNum());
            warnData.setWarnDate((Date) dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_PAYDATE));
            btWarnDataService.saveOrUpdate(warnData);

            //进行消息提醒
            BeanUtils.copyProperties(warnData, warnDataVO);
            if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
            }
            fundWarnNotificationService.sendMsg(warnDataVO);

            //记录新日志
            btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",事中策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 大额账户支出策略处理
     *
     * @param entity
     */
    public synchronized void executeBankaccpayStrategy(BtWarnStrategyVO entity, Map<String, Object> dataInfo) {
        Map<String, Object> params = new HashMap<>();
        //消息提醒传递VO
        BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
        params.put("wsId", entity.getId());
        params.put("no", String.valueOf(dataInfo.get(FundWarnConstant.BANKACCPAY.FIELD_NO)));
        BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
        String logType = null;
        String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
        if (warnData == null) {
            warnData = new BtWarnData();
            warnDataVO.setSendMsgSign("1");
            logType = FundWarnConstant.LOGTYPE_ADD;
            msg += "，新增预测触发数据信息！";
        } else {
            logType = FundWarnConstant.LOGTYPE_UPDATE;
            msg += "，修改预测触发数据信息！";
        }
        setBtWarnData(entity, warnData);
        warnData.setNo(String.valueOf(dataInfo.get(FundWarnConstant.BANKACCPAY.FIELD_NO)));
        warnData.setCorpId(String.valueOf(dataInfo.get(FundWarnConstant.BANKACCPAY.FIELD_CORPID)));
        warnData.setBankAcc(String.valueOf(dataInfo.get(FundWarnConstant.BANKACCPAY.FIELD_BANKACC)));
        warnData.setTriggerNum(NumberUtil.objToBigDecimal(dataInfo.get(FundWarnConstant.BANKACCPAY.FIELD_PAYMONEY)));
        warnData.setWarnDate((Date) dataInfo.get(FundWarnConstant.BANKACCPAY.FIELD_PAYDATE));
        //写入策略提醒数据
        btWarnDataService.saveOrUpdate(warnData);

        //进行消息提醒
        BeanUtils.copyProperties(warnData, warnDataVO);
        if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
            warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
        }
        fundWarnNotificationService.sendMsg(warnDataVO);

        //记录新日志
        btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());

        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",事中策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 大额现金支出策略处理
     *
     * @param entity
     */
    public synchronized void executeCashpayStrategy(BtWarnStrategyVO entity, Map<String, Object> dataInfo) {
        Map<String, Object> params = new HashMap<>();
        //消息提醒传递VO
        BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
        params.put("wsId", entity.getId());
        params.put("no", String.valueOf(dataInfo.get(FundWarnConstant.CASHPAY.FIELD_NO)));
        BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
        String logType = null;
        String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
        if (warnData == null) {
            warnData = new BtWarnData();
            warnDataVO.setSendMsgSign("1");
            logType = FundWarnConstant.LOGTYPE_ADD;
            msg += "，新增预测触发数据信息！";
        } else {
            logType = FundWarnConstant.LOGTYPE_UPDATE;
            msg += "，修改预测触发数据信息！";
        }
        setBtWarnData(entity, warnData);
        warnData.setNo(String.valueOf(dataInfo.get(FundWarnConstant.CASHPAY.FIELD_NO)));
        warnData.setCorpId(String.valueOf(dataInfo.get(FundWarnConstant.CASHPAY.FIELD_CORPID)));
        warnData.setFcAcc(String.valueOf(dataInfo.get(FundWarnConstant.CASHPAY.FIELD_FCACC)));
        warnData.setTriggerNum(NumberUtil.objToBigDecimal(dataInfo.get(FundWarnConstant.CASHPAY.FIELD_PAYMONEY)));
        warnData.setWarnDate(DateUtils.getDateYMD());
        //写入策略提醒数据
        btWarnDataService.saveOrUpdate(warnData);
        //进行消息提醒
        BeanUtils.copyProperties(warnData, warnDataVO);
        if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
            warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
        }
        fundWarnNotificationService.sendMsg(warnDataVO);

        //记录新日志
        btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());

        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",事中策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 根据策略信息设置策略提醒数据信息
     *
     * @param entity
     * @param warnData
     */
    public void setBtWarnData(BtWarnStrategyVO entity, BtWarnData warnData) {
        warnData.setWsId(entity.getId());
        warnData.setCode(entity.getCode());
        warnData.setName(entity.getName());
        warnData.setCurId(entity.getCurId());
        warnData.setTypeCode(entity.getTypeCode());
        warnData.setDimensionCode(entity.getDimensionCode());
        warnData.setCondition(BtFundWarnUtils.getConditionValue(entity, "触发值"));
    }

    /**
     * 根据params获取付款表nis_billhead相同公司付款明细数据
     *
     * @param params 入参
     * @return
     */
    @Transactional(readOnly = true)
    public List<BtWarnDataDtl> getEqcorppayDtlDataByParams(Map<String, Object> params) {
        StringBuilder sqlW = new StringBuilder();
        String wFields = "nb.corp_id,nb.payee_acc_name,nb.cur_id,nb.pay_money,nb.wish_payday";
        sqlW.append(SqlUtil.getWhereByParamsAndField(wFields, params));
        List<BtWarnDataDtl> list = fundWarnStrategyAnalysisDao.getEqcorppayDtlDataByParams(params, sqlW.toString());
        return list;
    }

    /**
     * 根据params获取付款表nis_billhead 累计支付超额数据
     *
     * @param params 入参
     * @return
     */
    @Transactional(readOnly = true)
    public BtWarnData getDaySumPayDataByParams(Map<String, Object> params, String wMoney) {
        StringBuilder sqlW = new StringBuilder();
        String wFields = "nb.corp_id,nb.cur_id,nb.wish_payday";
        sqlW.append(SqlUtil.getWhereByParamsAndField(wFields, params));
        List<BtWarnData> list = fundWarnStrategyAnalysisDao.getDaySumPayDataByParams(params, wMoney, sqlW.toString());
        if (!ListUtils.isNullOrEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据明细数据获取主数据信息
     *
     * @param dataDtlList 明细数据信息
     * @param entity      对应模式数据  FundWarnConstant.DIMENSIONCODE_*对应信息
     * @return 主数据信息
     */
    public List<BtWarnData> getBtWarnDataListByBtWarnDataDtlList(List<BtWarnDataDtl> dataDtlList, BtWarnStrategyVO entity) {
        List<BtWarnData> dataList = new ArrayList<>();
        if (!ListUtils.isNullOrEmpty(dataDtlList)) {
            String key = null;
            BigDecimal sumMoney = null;
            BtWarnData warnData = null;
            for (BtWarnDataDtl dataDtl : dataDtlList) {
                String keyNew = getKey(dataDtl, entity);
                if (!String.valueOf(key).equals(keyNew)) {
                    key = keyNew;
                    warnData = new BtWarnData();
                    dataList.add(warnData);
                    sumMoney = new BigDecimal(0);
                    warnData.setCorpId(dataDtl.getCorpId());
                    warnData.setBankAcc(dataDtl.getPayerAcc());
                    warnData.setDataDtlList(new ArrayList<BtWarnDataDtl>());
                }
                warnData.getDataDtlList().add(dataDtl);
                sumMoney = sumMoney.add(getMoney(dataDtl, entity));
                warnData.setTriggerNum(sumMoney);
            }
        }
        return dataList;
    }

    private String getKey(BtWarnDataDtl dataDtl, BtWarnStrategyVO entity) {
        if (dataDtl != null) {
            if (FundWarnConstant.DIMENSIONCODE_CORP.equals(entity.getDimensionCode())) {
                return dataDtl.getCorpId();
            } else if (FundWarnConstant.DIMENSIONCODE_BANKACC.equals(entity.getDimensionCode())
                    || FundWarnConstant.DIMENSIONCODE_FCACC.equals(entity.getDimensionCode())) {
                return dataDtl.getPayerAcc();
            }
        }
        return null;
    }

    private BigDecimal getMoney(BtWarnDataDtl dataDtl, BtWarnStrategyVO entity) {
        if (dataDtl != null) {
            if (FundWarnConstant.TYPECODE_EXPENDINFLOW.equals(entity.getTypeCode())) {
                return dataDtl.getPayMoney();
            }
        }
        return new BigDecimal(0);
    }

    /**
     * 获取余额下降的主数据信息
     *
     * @param entity
     * @param warnDate
     * @return
     */
    public List<BtWarnData> getBalDownDataInfo(BtWarnStrategyVO entity, Date warnDate) {
        List<BtWarnData> warnDataList = new ArrayList<>();
        Map<String, BtWarnData> warnDataMap = new HashMap<>();
        List<String> wValues = new ArrayList<>();
        while (true) {
            List<BtWarnDataDtl> warnDataDtlList = getBalDownDataDtlAllInfo(entity, warnDate, wValues);
            if (ListUtils.isNullOrEmpty(warnDataDtlList)) {
                break;
            } else {
                BtWarnData dataInfo = null;
                for (BtWarnDataDtl dataDtl : warnDataDtlList) {
                    String key = getKey(dataDtl, entity);
                    if (warnDataMap.containsKey(key)) {
                        dataInfo = warnDataMap.get(key);
                        BtWarnDataDtl lastDataDtl = dataInfo.getDataDtlList().get(dataInfo.getDataDtlList().size() - 1);
                        if (dataDtl.getExcessMoney().compareTo(lastDataDtl.getExcessMoney()) > 0) {
                            dataInfo.setTriggerNum(dataInfo.getTriggerNum().add(new BigDecimal(1)));
                            dataInfo.getDataDtlList().add(dataDtl);
                        } else {
                            wValues.remove(key);
                        }
                    } else {
                        dataInfo = new BtWarnData();
                        dataInfo.setCorpId(dataDtl.getCorpId());
                        dataInfo.setBankAcc(dataDtl.getPayerAcc());
                        dataInfo.setTriggerNum(new BigDecimal(0));
                        dataInfo.setDataDtlList(new ArrayList<BtWarnDataDtl>());
                        dataInfo.getDataDtlList().add(dataDtl);

                        warnDataMap.put(key, dataInfo);
                        warnDataList.add(dataInfo);
                    }
                }
            }
            warnDate = DateUtils.addDay(DateUtils.getMonthFirstDate(warnDate), -1);
        }
        List<BtWarnData> reWarnDataList = new ArrayList<>();
        for (BtWarnData data : warnDataList) {
            if (BtFundWarnUtils.checkConditionMoney(entity, data.getTriggerNum())) {
                reWarnDataList.add(data);
            }
        }
        return reWarnDataList;
    }

    /**
     * 获取余额下降的明细数据-月末余额以及
     *
     * @param entity
     * @param wValues
     * @return
     */
    public List<BtWarnDataDtl> getBalDownDataDtlAllInfo(BtWarnStrategyVO entity, Date warnDate, List<String> wValues) {
        Map<String, BtWarnDataDtl> dataDtlMap = new HashMap<>();
        Date beginDate = warnDate;
        Date endDate = DateUtils.addDay(warnDate, 1);
        int days = DateUtils.getDayOfMonth(warnDate);
        //获取月末余额
        List<BtWarnDataDtl> warnDataDtlList = getBalDownDataDtlInfo(entity, beginDate, endDate, wValues);
        if (!ListUtils.isNullOrEmpty(warnDataDtlList)) {
            beginDate = DateUtils.getMonthFirstDate(warnDate);
            wValues.clear();
            for (BtWarnDataDtl dataDtl : warnDataDtlList) {
                String key = getKey(dataDtl, entity);
                wValues.add(key);
                dataDtlMap.put(key, dataDtl);
            }
            //获取月总计余额
            List<BtWarnDataDtl> warnDataSumDtlList = getBalDownDataDtlInfo(entity, beginDate, endDate, wValues);
            if (!ListUtils.isNullOrEmpty(warnDataSumDtlList)) {
                for (BtWarnDataDtl dataDtl : warnDataSumDtlList) {
                    String key = getKey(dataDtl, entity);
                    if (dataDtlMap.containsKey(key)) {
                        dataDtlMap.get(key).setMonthNum(DateUtils.getMonth(warnDate));
                        dataDtlMap.get(key).setPayMoney(dataDtl.getExcessMoney().divide(new BigDecimal(days), 2, RoundingMode.HALF_UP));
                    }
                }
            }
        }
        return warnDataDtlList;
    }


    /**
     * 获取余额下降的明细数据
     *
     * @param entity    预警格式信息
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @param wValues   需要另外过滤的参数
     * @return
     */
    public List<BtWarnDataDtl> getBalDownDataDtlInfo(BtWarnStrategyVO entity, Date beginDate, Date endDate, List<String> wValues) {
        Map<String, Object> params = new HashMap<>();
        params.put("warnId", entity.getId());
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        String sqlW = null;
        if (!ListUtils.isNullOrEmpty(wValues)) {
            String fieldName = "";
            if (FundWarnConstant.DIMENSIONCODE_CORP.equals(entity.getDimensionCode())) {
                fieldName = "ba.corp_id";
            } else {
                fieldName = "ab.bank_acc";
            }
            sqlW = SqlUtil.getInSqlByParams(wValues, fieldName, params);
        }
        List<BtWarnDataDtl> warnDataDtlList = null;
        if (FundWarnConstant.DIMENSIONCODE_CORP.equals(entity.getDimensionCode())) {
            warnDataDtlList = fundWarnStrategyAnalysisDao.getBalDownCorpDataDtlByParams(params, sqlW);
        } else {
            boolean isSum = false;
            if (DateUtils.dayDiffs(endDate, beginDate) > 1) {
                isSum = true;
            }
            warnDataDtlList = fundWarnStrategyAnalysisDao.getBalDownBankAccDataDtlByParams(params, sqlW, isSum);
        }
        return warnDataDtlList;
    }

    /**
     * 久悬户预警-银行账户维度策略处理
     *
     * @param entity
     */
    public synchronized void executeOldBankAccByBankAccStrategy(BtWarnStrategyVO entity) {
        Map<String, Object> paramsData = new HashMap<>();
        paramsData.put("warnId", entity.getId());
        Date warnDate = entity.getRunDate();
        if (warnDate == null) {
            warnDate = DateUtils.getDateYMD();
        }
        Date beginDate = warnDate;
        if (entity.getForwardTime() != null) {
            beginDate = DateUtils.addDay(warnDate, -1 * entity.getForwardTime());
        } else {
            entity.setForwardTime(0);
        }
        //把时间转换成字符串，保证oracle和mysql都能兼容，另外数据库有的字段时timestamp有的是date
        paramsData.put("beginDate", DateUtils.format(beginDate, "yyyy-MM-dd HH:mm:ss"));
        Date endDate = DateUtils.addDay(warnDate, 1);
        paramsData.put("endDate", DateUtils.format(endDate, "yyyy-MM-dd") + " 23:59:59");
        paramsData.put("balBeginDate", DateUtils.format(warnDate, "yyyy-MM-dd HH:mm:ss"));
        Date balEndDate = DateUtils.addDay(warnDate, 1);
        paramsData.put("balEndDate", DateUtils.format(balEndDate, "yyyy-MM-dd") + " 23:59:59");

        String sqlW = BtFundWarnUtils.getConditionSql(entity, "info.triggerNum", paramsData);
        List<BtWarnData> warnDatas = fundWarnStrategyAnalysisDao.getExpendOldBankAccByBankAccDataByParams(paramsData, sqlW);
        if (!ListUtils.isNullOrEmpty(warnDatas) && warnDatas.size() > 0) {
            for (BtWarnData data : warnDatas) {
                //消息提醒传递VO
                BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
                Map<String, Object> params = new HashMap<>();
                params.put("wsId", entity.getId());
                params.put("bankAcc", data.getBankAcc());
                params.put("warnDate", warnDate);
                BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
                String logType = null;
                String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                if (warnData == null) {
                    warnData = new BtWarnData();
                    warnDataVO.setSendMsgSign("1");
                    logType = FundWarnConstant.LOGTYPE_ADD;
                    msg += "，新增预测触发数据信息！";
                } else {
                    logType = FundWarnConstant.LOGTYPE_UPDATE;
                    msg += "，修改预测触发数据信息！";
                }
                setBtWarnData(entity, warnData);
                warnData.setCorpId(data.getCorpId());
                warnData.setBankAcc(data.getBankAcc());
                warnData.setTriggerNum(data.getTriggerNum());
                warnData.setWarnDate(warnDate);
                warnData.setReserve1(entity.getForwardTime() + "天");
                if (data.getOneNum() == null) {
                    warnData.setOneNum(new BigDecimal(0));
                } else {
                    warnData.setOneNum(data.getOneNum());
                }
                if (data.getDataDtlList() != null) {
                    warnData.setSizeNum(data.getDataDtlList().size());
                    warnData.setTriggerNum(data.getTriggerNum());
                }
                btWarnDataService.saveOrUpdate(warnData);

                //进行消息提醒
                BeanUtils.copyProperties(warnData, warnDataVO);
                if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                    warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                }
                fundWarnNotificationService.sendMsg(warnDataVO);

                //记录新日志
                btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
            }
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",定时策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 预算执行进度策略处理
     *
     * @param entity
     */
    public synchronized void executeBudgetProgressStrategy(BtWarnStrategyVO entity, Map<String, Object> dataInfo) {
        //查询对应条件的付款明细数据
        Map<String, Object> paramsData = new HashMap<>();
        paramsData.put("cur_id", entity.getCurId());
        paramsData.put("corp_id", String.valueOf(dataInfo.get(FundWarnConstant.BUDGETPROGRESS.FIELD_CORPID)));
        String wMoney = BtFundWarnUtils.getConditionSql(entity, "info.triggerNum", paramsData);
        BtWarnData data = fundWarnStrategyAnalysisDao.getBudgetProgressDataByParams(paramsData, wMoney);
        if (data != null) {
            //消息提醒传递VO
            BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();
            Map<String, Object> params = new HashMap<>();
            params.put("wsId", entity.getId());
            params.put("corpId", String.valueOf(dataInfo.get(FundWarnConstant.BUDGETPROGRESS.FIELD_CORPID)));
            params.put("warnDate", (Date) dataInfo.get(DateUtils.getDateYMD()));
            BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);
            String logType = null;
            String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                    + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
            if (warnData == null) {
                warnData = new BtWarnData();
                warnDataVO.setSendMsgSign("1");
                logType = FundWarnConstant.LOGTYPE_ADD;
                msg += "，新增预测触发数据信息！";
            } else {
                logType = FundWarnConstant.LOGTYPE_UPDATE;
                msg += "，修改预测触发数据信息！";
            }
            setBtWarnData(entity, warnData);
            warnData.setCorpId(String.valueOf(dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_CORPID)));
            warnData.setTriggerNum(data.getTriggerNum());
            warnData.setWarnDate((Date) dataInfo.get(FundWarnConstant.DAYSUMPAY.FIELD_PAYDATE));
            btWarnDataService.saveOrUpdate(warnData);

            //进行消息提醒
            BeanUtils.copyProperties(warnData, warnDataVO);
            if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
            }
            fundWarnNotificationService.sendMsg(warnDataVO);

            //记录新日志
            btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
        }
        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",事中策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 预算执行进度-单位维度策略处理
     *
     * @param entity
     */
    public synchronized void executeBudgetProgressCorpStrategy(BtWarnStrategyVO entity) {

        List<BtWarnCondition> warnConditions = btWarnStrategyService.getWarnConditionByWarnId(entity.getId());
        String timeType = warnConditions.stream()
                .filter(cond -> FundWarnConstant.CONDITION_TYPE_TIME_TYPE.equals(cond.getDataType()))
                .map(BtWarnCondition::getDataValue)
                .findFirst()
                .orElse(null);
        if (StringUtils.isBlank(timeType)){
            return;
        }
        //获取当前执行的计划周期
        FbsBudgetPeriodDTO periodDto = budgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), timeType);
        String tableName = this.getFbsTableName(timeType);
        Map<String, Object> paramsData = new HashMap<>();
        paramsData.put("cur_id", entity.getCurId());
        paramsData.put("beginDate", periodDto.getBeginDate());
        paramsData.put("endDate", periodDto.getEndDate());
        //获取预算数据
        List<BtWarnData> list = fundWarnStrategyAnalysisDao.getBudgetProgressListDataByTableName(paramsData, tableName);
        //获取执行数据
        List<BtWarnData> process = fundWarnStrategyAnalysisDao.getProcessDataByTableName(paramsData);
        // 预算map key:corpId  value:triggerNum
        Map<String, BigDecimal> budgetMap = list.stream()
                .collect(Collectors.toMap(BtWarnData::getCorpId, BtWarnData::getTriggerNum));

        // 执行map key:corpId  value:triggerNum
        Map<String, BigDecimal> processMap = process.stream()
                .collect(Collectors.toMap(BtWarnData::getCorpId, BtWarnData::getTriggerNum));

        Date warnDate = DateUtils.getDate();
        //遍历预算map
        for (Map.Entry<String, BigDecimal> item : budgetMap.entrySet()) {
            String corpId = item.getKey();
            BigDecimal budgetAmount = item.getValue();
            if (processMap.containsKey(corpId)) {
                BigDecimal processAmount = processMap.get(corpId);
                // ===== 调用规则判断，替换硬编码=====
                boolean needWarn = matchNumCondition(entity, processAmount, budgetAmount, true);
                if (needWarn) {
                    // 原有预警逻辑：查询warnData、saveOrUpdate、发消息、写日志
                    Map<String, Object> params = new HashMap<>();
                    params.put("wsId", entity.getId());
                    params.put("corpId", corpId);
                    params.put("warnDate", warnDate);
                    BtWarnData warnData = btWarnDataService.getBtWarnDataByParams(params);

                    String logType;
                    String msg = BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                            + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode());
                    BtWarnDataInfoVO warnDataVO = new BtWarnDataInfoVO();

                    if (warnData == null) {
                        warnData = new BtWarnData();
                        warnDataVO.setSendMsgSign("1");
                        logType = FundWarnConstant.LOGTYPE_ADD;
                        msg += "，新增预测触发数据信息！";
                    } else {
                        logType = FundWarnConstant.LOGTYPE_UPDATE;
                        msg += "，修改预测触发数据信息！";
                    }

                    setBtWarnData(entity, warnData);
                    warnData.setCorpId(corpId);
                    warnData.setOneNum(processAmount);//执行数
                    warnData.setTwoNum(budgetAmount);//预算追加 上报数
                    warnData.setReserve1(periodDto.getBeginDateStr() + " - " + periodDto.getEndDateStr()); //日期
                    warnData.setUpdateDate(warnDate);//修改日期
                    warnData.setTriggerNum(processAmount.divide(budgetAmount, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100.00")) );
                    warnData.setWarnDate(warnDate);
                    btWarnDataService.saveOrUpdate(warnData);

                    BeanUtils.copyProperties(warnData, warnDataVO);
                    if (StringUtils.isNotBlank(warnDataVO.getCorpId())) {
                        warnDataVO.setCorpName(corpService.getCorpName(warnDataVO.getCorpId()));
                    }
                    fundWarnNotificationService.sendMsg(warnDataVO);
                    btWarningLogService.saveBtWarningLog(warnData.getId(), logType, msg, entity.getCurrentUser());
                }
            }
        }

        //记录策略执行日志
        btWarningLogService.saveBtWarningLog(entity.getId(),
                FundWarnConstant.LOGTYPE_EXECUTE,
                BtFundWarnUtils.getTypeNameByCode(entity.getTypeCode()) + "-"
                        + BtFundWarnUtils.getDimensionNameByCode(entity.getDimensionCode()) + ",事中策略被执行！",
                entity.getCurrentUser());
    }

    /**
     * 数值区间规则判断，和getConditionSql逻辑完全一致，JDK8
     * @param entity 预警策略，包含 beginNum beginOperator、endNum endOperator、numOperator(AND/OR)
     * @param checkVal 待校验金额（执行金额，BigDecimal）
     * @param oldVal  参考金额（计划金额，BigDecimal）
     * @param isRatio 是否开始 结束是比例
     * @return true满足预警条件
     */
    public static boolean matchNumCondition(BtWarnStrategyVO entity, BigDecimal checkVal, BigDecimal oldVal, boolean isRatio) {
        // entity为空，返回true，和原sql (1=1) 保持一致
        if (entity == null) {
            return true;
        }
        // null兜底为0
        BigDecimal val = Optional.ofNullable(checkVal).orElse(BigDecimal.ZERO);
        boolean hasBegin = entity.getBeginNum() != null;
        boolean hasEnd = entity.getEndNum() != null;
        // 没有配置任何数值条件，默认不触发
        if (!hasBegin && !hasEnd) {
            return false;
        }

        boolean beginMatch = false;
        if (hasBegin) {
            BigDecimal beginNum = entity.getBeginNum();
            //是否比例计算 设置参考值
            if (isRatio){
                beginNum = beginNum.multiply(oldVal);
            }
            String beginOp = entity.getBeginOperator();
            beginMatch = compareBigDecimal(val, beginOp, beginNum);
        }

        boolean endMatch = false;
        if (hasEnd) {
            BigDecimal endNum = entity.getEndNum();
            //是否比例计算 设置参考值
            if (isRatio){
                endNum = endNum.multiply(oldVal);
            }
            String endOp = entity.getEndOperator();
            endMatch = compareBigDecimal(val, endOp, endNum);
        }

        // 只配置了开始条件
        if (hasBegin && !hasEnd) {
            return beginMatch;
        }
        // 只配置了结束条件
        if (!hasBegin && hasEnd) {
            return endMatch;
        }

        // 同时配置begin+end，用numOperator AND/OR拼接
        String relation = entity.getNumOperator();
        if ("OR".equals(relation)) {
            return beginMatch || endMatch;
        } else {
            // 默认AND，和getConditionSql保持一致
            return beginMatch && endMatch;
        }
    }

    /**
     * 基础比较工具，GT GE LT LE EQ
     * @param source 待比较值
     * @param op 运算符编码 GT GE LT LE EQ
     * @param target 目标数值
     * @return 比较结果
     */
    private static boolean compareBigDecimal(BigDecimal source, String op, BigDecimal target) {
        if (op == null) {
            return false;
        }
        int cmp = source.compareTo(target);
        switch (op) {
            case "10":
                return cmp > 0;
            case "11":
                return cmp >= 0;
            case "20":
                return cmp < 0;
            case "21":
                return cmp <= 0;
            case "EQ":
                return cmp == 0;
        }
        return false;
    }



    /**
     * 获取计划周期参数表
     *
     */
    private String getFbsTableName(String timeType){
        Map<String, String> tableNameMap = new HashMap<>();
        tableNameMap.put("1", "FBS_BUDGET_YEAR");
        tableNameMap.put("2", "FBS_BUDGET_QUARTER");
        tableNameMap.put("3", "FBS_BUDGET_MONTH");
        tableNameMap.put("4", "FBS_BUDGET_WEEK");
        tableNameMap.put("5", "FBS_BUDGET_DAY");

        return tableNameMap.get(timeType);
    }
}
