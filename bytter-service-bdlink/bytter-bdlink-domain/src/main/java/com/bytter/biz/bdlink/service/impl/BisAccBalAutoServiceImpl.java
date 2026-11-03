package com.bytter.biz.bdlink.service.impl;

import com.bytter.biz.bdlink.api.dto.BisAccBalAutoDTO;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalAutoService;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.dao.BisAccBalAutoDao;
import com.bytter.biz.bdlink.dao.BisAccBalDao;
import com.bytter.biz.bdlink.entity.BisAccBal;
import com.bytter.biz.bdlink.entity.BisAccBalAuto;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.constant.BisAccBalConstant;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.tenant.SmartThreadPoolManager;
import com.bytter.core.tenant.TenantShardingExecutor;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccFreezeService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Service
 * @Description: BIS_ACC_BAL_AUTO
 * @date 2019-08-08 15:02:30
 */
@Service("bisAccBalAutoServiceImpl")
@DubboService
@Transactional
@Component
public class BisAccBalAutoServiceImpl extends BaseService<BisAccBalAuto, String> implements IBisAccBalAutoService,ITaskService {
    @Autowired
    private BisAccBalAutoDao bisAccBalAutoDao;
    @Autowired
    private BisAccBalDao bisAccBalDao;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccServiceImpl;
    @Autowired
    private BisAccDtlServiceImpl bisAccDtlServiceImpl;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @DubboReference(check = false)
    private IBtBankAccFreezeService btBankAccFreezeService;
    public static final int CPU_NUM = Runtime.getRuntime().availableProcessors();
    /**
     * 账号计算余额
     */
    private static ExecutorService BISACCBALAUTO_START = SmartThreadPoolManager.wrap(Executors.newFixedThreadPool(CPU_NUM*2));

    public void destroy() throws Exception {
        BISACCBALAUTO_START.shutdown();
    }
    @Autowired(required = false)
    private BisAccBalAutoServiceImpl bisAccBalAutoService;
    @Override
    public BaseDao<BisAccBalAuto, String> getEntityDao() {
        return bisAccBalAutoDao;
    }
    /**
     * 自动自动更新离线账户历史余额任务触发执行
     */
    @XxlJob("BisAccBalAutoService")
    @Override
    public void run(String arguments) {
//        RestInput restInput = null;
//        RestResponse<BtBankAccDTO> btBankAccDTORestResponse = null;
//        int pageNumber = 1;
//        do {
//            if(restInput==null){
//                restInput=new RestInput();
//                restInput.put("pageNumber",pageNumber);
//                restInput.put("pageSize",5);
//            }else{
//                pageNumber = (Integer)restInput.get("pageNumber");
//                restInput.put("pageNumber",pageNumber+1);
//            }
//            btBankAccDTORestResponse = btBankAccServiceImpl.bankAccResult(restInput);
//            //处理生成余额
//            Map<String,List<BisAccBal>> mapList = this.doBal(btBankAccDTORestResponse.getData());
//            bisAccBalDao.batchSave(mapList.get("saveBal"));
//            bisAccBalDao.batchUpdate(mapList.get("upadteBal"));
//        }while (restInput.getPageNumber() < btBankAccDTORestResponse.getTotalPage());
//        //由于余额计算不正确，先刷新余额，再进行查询
////        bisAccDtlServiceImpl.flushBalance();
        this.logger.info("---自动更新离线账户历史余额，开始执行定时任务---");
        List<BtBankAccDTO> list = btBankAccServiceImpl.getBankAccListByIsOnline(BtBankAccConstant.ISONLINE_0);
        //线程执行结果集合
        List<Future<String>> futures = new ArrayList<>();
        final CountDownLatch countDownLatch = new CountDownLatch(list.size());
        for(BtBankAccDTO btBankAccDTO : list){
            XxlJobHelper.log("账号："+btBankAccDTO.getBankAcc());
            final BtBankAccDTO btBankAccDTOTemp = btBankAccDTO;
            //使用线程池处理流程启动，并返回结果
            Future<String> submit = BISACCBALAUTO_START.submit(new Callable<String>() {
                @Override
                public String call() {
                    try {
                        Map<String, List<BisAccBal>> stringListMap = bisAccBalAutoService.autoSaveBal(btBankAccDTOTemp);
                        bisAccBalDao.batchSave(stringListMap.get("saveBal"));
                        bisAccBalDao.batchUpdate(stringListMap.get("upadteBal"));
                    } catch (Exception e) {
                        throw e;
                    } finally {
                        //线程监听器统计已经完成处理
                        countDownLatch.countDown();
                    }
                    return "success";
                }
            });
            futures.add(submit);
        }
        this.logger.info("---自动更新离线账户历史余额，结束执行定时任务---");
    }

    private Map<String, List<BisAccBal>> autoSaveBal(BtBankAccDTO btBankAccDTO){
        Map<String, List<BisAccBal>> doBalMap = this.doBal(btBankAccDTO);
        return doBalMap;
    }
    /**
     * 根据账号id币别id查询离线账户余额自动计算信息表
     *
     * @param bankAccId 账户ID
     * @param curId   币别ID
     * @return BisAccBalAuto
     */
    public BisAccBalAutoDTO getBisAccBalAutoByAccCurId(String bankAccId, String curId) {
        BisAccBalAuto bisAccBalAuto = bisAccBalAutoDao.getBisAccBalAutoByAccCurId(bankAccId, curId);
        if(!EmptyUtils.isEmpty(bisAccBalAuto)){
            BisAccBalAutoDTO bisAccBalAutoDTO = new BisAccBalAutoDTO();
            BeanUtils.copyNotNullProperties(bisAccBalAuto,bisAccBalAutoDTO);
            return bisAccBalAutoDTO;
        }
        return null;
    }

    @Override
    public void updateEntity(BisAccBalAutoDTO bisAccBalAutoDTO) {
        if(!EmptyUtils.isEmpty(bisAccBalAutoDTO)){
            BisAccBalAuto entity = new BisAccBalAuto();
            BeanUtils.copyNotNullProperties(bisAccBalAutoDTO,entity);
            this.updateEntity(entity);
        }

    }

    /**
     * 余额生成处理
     * @param bankAccList
     * @return
     */
    public Map<String,List<BisAccBal>> doBal(List<BtBankAccDTO> bankAccList){
        //当前日期前一天
        Date startDate = DateUtils.getDate();
        Date endDate = DateUtils.getDate();
        Map<String,Date> dateMap = new HashMap<String,Date>();
        List<BisAccBal> saveBal = new ArrayList<BisAccBal>();
        List<BisAccBal> updateBal = new ArrayList<BisAccBal>();
        List<BisAccBalAuto> autoBal = new ArrayList<BisAccBalAuto>();
        Map<String,List<BisAccBal>> mapObj = new HashMap<String,List<BisAccBal>>();
        for (BtBankAccDTO dto : bankAccList) {
            //先根据账号id和币别id取余额自动计算信息表信息
            BisAccBalAutoDTO auto = this.getBisAccBalAutoByAccCurId(dto.getId(), dto.getCurId());
            //根据账号和币别id取第一笔流水的交易日期
            Date dtlDate = bisAccDtlServiceImpl.getMinTransTime(dto.getBankAcc(), dto.getCurId());
            //获取账户的初始化余额
            BisAccBalDTO initBal = bisAccBalService.getInitBisAccBal(dto.getBankAcc(), dto.getCurId());
            if (!EmptyUtils.isEmpty(auto) && !EmptyUtils.isEmpty(auto.getEndDate())) {
                //开始日期=截止日期
                startDate = auto.getEndDate();
            } else if (!EmptyUtils.isEmpty(dto.getInitBalanceDate())) {
                //开始日期=余额初始化日期
                startDate = dto.getInitBalanceDate();
            } else if (!EmptyUtils.isEmpty(dto.getRegDate())) {
                //开始日期=开户日期
                startDate = dto.getRegDate();
            } else if (!EmptyUtils.isEmpty(dtlDate)) {
                //开始日期=流水第一笔时间
                startDate = dtlDate;
            } else {
                //开始日期=当前日期前一天
                startDate = endDate;
            }
            //判断前一天的余额是否有余额
            BisAccBalDTO bisAccBalPre = bisAccBalService.getBisAccBalBy(dto.getBankAcc(), dto.getCurId(), DateUtils.addDay(startDate,-1), false);
            BigDecimal balTemp = BigDecimal.ZERO;
            if (bisAccBalPre != null){
                balTemp = bisAccBalPre.getBal();
            }
            Date endDateTemp = endDate;
            while (startDate.compareTo(endDate) < 1) {
                //判断当日是否有余额
                BisAccBalDTO bisAccBalDTO = bisAccBalService.getBisAccBalBy(dto.getBankAcc(), dto.getCurId(), startDate, false);
                BisAccBal bisAccBal = null;
                if(!EmptyUtils.isEmpty(bisAccBalDTO)){
                    bisAccBal = new BisAccBal();
                    BeanUtils.copyNotNullProperties(bisAccBalDTO,bisAccBal);
                }
                //当前日期对应的最后一笔银行流水
                //BisAccDtlVO dtlVO = bisAccDtlService.getBisAccDtlVOByAccCurTime(dto.getBankAcc(), dto.getCurId(), startDate);
                //取当天流水结余（不能取最后一笔流水的余额，考虑有交易日期一模一样的流水，无法区分先后顺序）
                BigDecimal lastBal = bisAccDtlServiceImpl.getLastBal(dto.getBankAcc(), dto.getCurId(), startDate);
                //获取当天的冻结金额
                BigDecimal freezeMoney = btBankAccFreezeService.getFreezeMoneyByBankAcc(dto.getBankAcc(), dto.getCurId(), startDate);
                //如果都为空，从开始日期到当前日期生成为0的数据
                if (EmptyUtils.isEmpty(bisAccBal)) {
                    BisAccBal bal = new BisAccBal();
                    bal.setBankAcc(dto.getBankAcc());
                    bal.setCurId(dto.getCurId());
                    bal.setCorpId(dto.getCorpId());
                    //前一天的余额+当天流水结余
                    bal.setBal(balTemp.add(lastBal));
                    //可用金额=余额-冻结金额
                    bal.setAvailBal(bal.getBal().subtract(freezeMoney));
                    // 设置单号
                    String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
                    bal.setAccNo(accNo);
                    bal.setGetDate(startDate);
                    bal.setBalDate(startDate);
                    bal.setBifCode(dto.getBifCode());
                    bal.setCreateDate(DateUtils.getDate());
                    bal.setFrzBal(freezeMoney);
                    bal.setStatus(BisAccBalConstant.STATUE_CHECK);
                    balTemp = bal.getBal();
                    endDateTemp = bal.getBalDate();
                    dateMap.put(dto.getId() + ";" + dto.getCurId(),endDateTemp);
                    saveBal.add(bal);
                } else {
                    //存在审批过的余额，更新
                    if (EmptyUtils.isEmpty(bisAccBal.getStatus()) || BisAccBalConstant.STATUE_CHECK.equals(bisAccBal.getStatus())) {
                        //校验余额日期是否跟初始化余额日期相同，初始化余额当天余额不做变更
                        if(initBal != null && startDate.equals(initBal.getBalDate())){
                            balTemp = bisAccBal.getBal();
                            endDateTemp = bisAccBal.getBalDate();
                            startDate = DateUtils.addDay(startDate, 1);
                            continue;
                        }
                        //避免银行接口返回余额为空的数据
                        if (balTemp == null) {
                            balTemp = BigDecimal.ZERO;
                        }
                        //前一天的余额+当天流水结余
                        bisAccBal.setBal(balTemp.add(lastBal));
                        //可用金额=余额-冻结金额
                        bisAccBal.setAvailBal(bisAccBal.getBal().subtract(freezeMoney));
                        bisAccBal.setFrzBal(freezeMoney);
                        bisAccBal.setStatus(BisAccBalConstant.STATUE_CHECK);
                        //更新余额获取时间
                        bisAccBal.setGetDate(DateUtils.getDate());
                        bisAccBal.setUpdateDate(DateUtils.getDate());
                        balTemp = bisAccBal.getBal();
                        endDateTemp = bisAccBal.getBalDate();
                        dateMap.put(dto.getId() + ";" + dto.getCurId(),endDateTemp);
                        updateBal.add(bisAccBal);
                    } else {
                        //存在余额但没有审批通过，不更新余额，截止中断
                        endDateTemp = startDate;
                        dateMap.put(dto.getId() + ";" + dto.getCurId(),endDateTemp);
                        break;
                    }
                }
                startDate = DateUtils.addDay(startDate, 1);
            }
        }
        if(!EmptyUtils.isEmpty(dateMap)){
            for(String key : dateMap.keySet()){
                String[] keyTemp = key.split(";");
                Date endTemp = dateMap.get(key);
                BisAccBalAuto balAuto = this.bisAccBalAutoDao.getBisAccBalAutoByAccCurId(keyTemp[0],keyTemp[1]);
                if(EmptyUtils.isEmpty(balAuto)) {
                    balAuto = new BisAccBalAuto();
                    balAuto.setBankAccId(keyTemp[0]);
                    balAuto.setCurId(keyTemp[1]);
                }
                balAuto.setEndDate(endTemp);
                autoBal.add(balAuto);
            }
            this.batchSaveOrUpdate(autoBal);
        }
        mapObj.put("saveBal",saveBal);
        mapObj.put("upadteBal",updateBal);
        return mapObj;
    }

    /**
     * 余额生成处理
     * @param dto
     * @return
     */
    public Map<String,List<BisAccBal>> doBal(BtBankAccDTO dto){
        //当前日期前一天
        Date startDate = DateUtils.getDateYMD();
        Date endDate = DateUtils.getDateYMD();
        Map<String,Date> dateMap = new HashMap<>();
        List<BisAccBal> saveBal = new ArrayList<>();
        List<BisAccBal> updateBal = new ArrayList<>();
        List<BisAccBalAuto> autoBal = new ArrayList<>();
        Map<String,List<BisAccBal>> mapObj = new HashMap<>();
        if(EmptyUtils.isEmpty(dto.getCurId())){
            dto.setCurId("4028802e5c346968015c346c18850008");
        }
        //先根据账号id和币别id取余额自动计算信息表信息
        BisAccBalAutoDTO auto = this.getBisAccBalAutoByAccCurId(dto.getId(), dto.getCurId());
        //根据账号和币别id取第一笔流水的交易日期
        Date dtlDate = bisAccDtlServiceImpl.getMinTransTime(dto.getBankAcc(), dto.getCurId());
        //获取账户的初始化余额
        BisAccBalDTO initBal = bisAccBalService.getInitBisAccBal(dto.getBankAcc(), dto.getCurId());
        if (!EmptyUtils.isEmpty(auto) && !EmptyUtils.isEmpty(auto.getEndDate())) {
            //开始日期=截止日期
            startDate = auto.getEndDate();
        } else if (!EmptyUtils.isEmpty(dto.getInitBalanceDate())) {
            //开始日期=余额初始化日期
            startDate = dto.getInitBalanceDate();
        } else if (!EmptyUtils.isEmpty(dto.getRegDate())) {
            //开始日期=开户日期
            startDate = dto.getRegDate();
        } else if (!EmptyUtils.isEmpty(dtlDate)) {
            //开始日期=流水第一笔时间
            startDate = dtlDate;
        } else {
            //开始日期=当前日期前一天
            startDate = endDate;
        }
        //判断前一天的余额是否有余额
        BisAccBalDTO bisAccBalPre = bisAccBalService.getBisAccBalBy(dto.getBankAcc(), dto.getCurId(), DateUtils.addDay(startDate,-1), false);
        BigDecimal balTemp = BigDecimal.ZERO;
        BigDecimal sysFrzBalTemp = BigDecimal.ZERO;// 系统冻结金额
        if (bisAccBalPre != null){
            balTemp = bisAccBalPre.getBal();
            sysFrzBalTemp = bisAccBalPre.getSysFrzBal();
        }
        Date endDateTemp = endDate;
        while (startDate.compareTo(endDate) < 1) {
            //判断当日是否有余额
            BisAccBalDTO bisAccBalDTO = bisAccBalService.getBisAccBalBy(dto.getBankAcc(), dto.getCurId(), startDate, false);
            BisAccBal bisAccBal = null;
            if(!EmptyUtils.isEmpty(bisAccBalDTO)){
                bisAccBal = new BisAccBal();
                BeanUtils.copyNotNullProperties(bisAccBalDTO,bisAccBal);
            }
            //当前日期对应的最后一笔银行流水
            //BisAccDtlVO dtlVO = bisAccDtlService.getBisAccDtlVOByAccCurTime(dto.getBankAcc(), dto.getCurId(), startDate);
            //取当天流水结余（不能取最后一笔流水的余额，考虑有交易日期一模一样的流水，无法区分先后顺序）
            BigDecimal lastBal = bisAccDtlServiceImpl.getLastBal(dto.getBankAcc(), dto.getCurId(), startDate);
            //获取当天的冻结金额
            BigDecimal freezeMoney = btBankAccFreezeService.getFreezeMoneyByBankAcc(dto.getBankAcc(), dto.getCurId(), startDate);
            //如果都为空，从开始日期到当前日期生成为0的数据
            if (EmptyUtils.isEmpty(bisAccBal)) {
                BisAccBal bal = new BisAccBal();
                bal.setBankAcc(dto.getBankAcc());
                bal.setCurId(dto.getCurId());
                bal.setCorpId(dto.getCorpId());
                //前一天的余额+当天流水结余
                bal.setBal(balTemp.add(lastBal));
                //可用金额=余额-冻结金额
                bal.setAvailBal(bal.getBal().subtract(freezeMoney));
                // 系统冻结金额=前一天的系统冻结金额
                bal.setSysFrzBal(sysFrzBalTemp);
                // 设置单号
                String accNo = SequenceSingleton.getSequence(SequenceEnum.BIS_ACC_BAL);
                bal.setAccNo(accNo);
                bal.setGetDate(DateUtils.getDate());
                bal.setBalDate(startDate);
                bal.setBifCode(dto.getBifCode());
                bal.setCreateDate(DateUtils.getDate());
                bal.setFrzBal(freezeMoney);
                bal.setStatus(BisAccBalConstant.STATUE_CHECK);
                balTemp = bal.getBal();
                endDateTemp = bal.getBalDate();
                dateMap.put(dto.getId() + ";" + dto.getCurId(),endDateTemp);
                saveBal.add(bal);
            } else {
                //存在审批过的余额，更新
                if (EmptyUtils.isEmpty(bisAccBal.getStatus()) || BisAccBalConstant.STATUE_CHECK.equals(bisAccBal.getStatus())) {
                    //校验余额日期是否跟初始化余额日期相同，初始化余额当天余额不做变更
                    if(initBal != null && startDate.equals(initBal.getBalDate())){
                        balTemp = bisAccBal.getBal();
                        sysFrzBalTemp = bisAccBal.getSysFrzBal();
                        endDateTemp = bisAccBal.getBalDate();
                        startDate = DateUtils.addDay(startDate, 1);
                        continue;
                    }
                    //避免银行接口返回余额为空的数据
                    if (balTemp == null) {
                        balTemp = BigDecimal.ZERO;
                    }
                    //前一天的余额+当天流水结余
                    bisAccBal.setBal(balTemp.add(lastBal));
                    //可用金额=余额-冻结金额
                    bisAccBal.setAvailBal(bisAccBal.getBal().subtract(freezeMoney));
                    bisAccBal.setFrzBal(freezeMoney);
                    // 系统冻结金额=前一天的系统冻结金额
                    bisAccBal.setSysFrzBal(sysFrzBalTemp);
                    bisAccBal.setStatus(BisAccBalConstant.STATUE_CHECK);
                    //更新余额获取时间
                    bisAccBal.setGetDate(DateUtils.getDate());
                    bisAccBal.setUpdateDate(DateUtils.getDate());
                    balTemp = bisAccBal.getBal();
                    endDateTemp = bisAccBal.getBalDate();
                    dateMap.put(dto.getId() + ";" + dto.getCurId(),endDateTemp);
                    updateBal.add(bisAccBal);
                } else {
                    //存在余额但没有审批通过，不更新余额，截止中断
                    endDateTemp = startDate;
                    dateMap.put(dto.getId() + ";" + dto.getCurId(),endDateTemp);
                    break;
                }
            }
            startDate = DateUtils.addDay(startDate, 1);
        }
        if(!EmptyUtils.isEmpty(dateMap)){
            for(String key : dateMap.keySet()){
                String[] keyTemp = key.split(";");
                Date endTemp = dateMap.get(key);
                BisAccBalAuto balAuto = this.bisAccBalAutoDao.getBisAccBalAutoByAccCurId(keyTemp[0],keyTemp[1]);
                if(EmptyUtils.isEmpty(balAuto)) {
                    balAuto = new BisAccBalAuto();
                    balAuto.setBankAccId(keyTemp[0]);
                    balAuto.setCurId(keyTemp[1]);
                }
                balAuto.setEndDate(endTemp);
                autoBal.add(balAuto);
            }
            this.batchSaveOrUpdate(autoBal);
        }
        mapObj.put("saveBal",saveBal);
        mapObj.put("upadteBal",updateBal);
        bisAccBalDao.batchSave(saveBal);
        bisAccBalDao.batchUpdate(updateBal);
        return mapObj;
    }
}
