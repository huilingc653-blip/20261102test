package com.bytter.biz.receipt.service;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlAutoVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.biz.receipt.dao.BisBankReceiptBlendingDao;
import com.bytter.biz.receipt.entity.BisBankReceiptDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import com.bytter.core.tenant.SmartThreadPoolManager;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 银行电子回单勾兑
 */
@Service("bisBankReceiptBlendingServer")
@Transactional
public class BisBankReceiptBlendingServer extends BaseService<BisBankReceiptDtl, String> implements ITaskService {
    @Autowired
    private BisBankReceiptBlendingDao bisBankReceiptBlendingDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBisAccDtlService accDtlService;
    @DubboReference(check = false)
    private IDictService iDictService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @Override
    public BaseDao<BisBankReceiptDtl, String> getEntityDao() {
        return bisBankReceiptBlendingDao;
    }
    public DataTablesOutput<BisBankReceiptDtlVO> getDataTablesUnion(DataTablesInput input, ICurrentUser user, Set<String> accNos) {
        DataTablesOutput<BisBankReceiptDtlVO> output = this.bisBankReceiptBlendingDao.getDataTablesUnion(input, accNos);
        String eqs_blendSign = (String) input.getFormdata().get("eqs_blendSign");
        //如果查询的已勾兑数据则查询关联的明细id
        if (eqs_blendSign.equals(BtBankAccConstant.BLENDSIGN_1) || "".equals(eqs_blendSign)) {
            List<BisBankReceiptDtlVO> list = output.getData();
            for (BisBankReceiptDtlVO vo : list) {
                List<String> dtlIdList = this.bisBankReceiptBlendingDao.getDtlId(vo.getId());
                String dtlId = "";
                for (int i = 0; i < dtlIdList.size(); i++) {
                    if (i == 0) {
                        dtlId += dtlIdList.get(i);
                    } else {
                        dtlId += "," + dtlIdList.get(i);
                    }
                }
                vo.setDtlId(dtlId);
            }
            output.setData(list);
        }
        return output;
    }
    /**
     * 自动勾兑
     *
     * @param ids
     * @param bankAccTypeStr
     */
    public String aotoBlending(String ids, String bankAccTypeStr) {
        String fuzzyMatching = configService.getConfig(ConfigConstant.ELE, ReceiptConstant.ELE_CONFIG_FUZZY_MATCHING).getValue();
        String msg = "";
        String rules = "";
        List<BisBankReceiptDtlVO> dtlList = bisBankReceiptBlendingDao.listReceiptByids(ids);
        Map<String, BisBankReceiptDtlVO> tempMap = new HashMap<>();
        Map<String, BisBankReceiptDtlVO> repeatMap = new HashMap<>();
        Boolean eqg_payMoney = false;
        Boolean eqs_payerAcc = false;
        Boolean eqs_payeeAcc = false;
        Boolean wishPayday = false;
        Boolean eqs_curId = false;
        Boolean isHasData = false;
        if (bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_1)) {
            eqg_payMoney = true;
            rules = rules + iDictService.get("paymentblending_matching_rule", "1");
        }
        if (bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_2)) {
            eqs_payerAcc = true;
            if (rules.equals("")) {
                rules = rules + iDictService.get("paymentblending_matching_rule", "2");
            } else {
                rules = rules + "+" + iDictService.get("paymentblending_matching_rule", "2");
            }
        }
        if (bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_3)) {
            eqs_payeeAcc = true;
            if (rules.equals("")) {
                rules = rules + iDictService.get("paymentblending_matching_rule", "3");
            } else {
                rules = rules + "+" + iDictService.get("paymentblending_matching_rule", "3");
            }
        }
        if (bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_4)) {
            wishPayday = true;
            if (rules.equals("")) {
                rules = rules + iDictService.get("paymentblending_matching_rule", "4");
            } else {
                rules = rules + "+" + iDictService.get("paymentblending_matching_rule", "4");
            }
        }
        if (bankAccTypeStr.contains(BtBankAccConstant.PAYMENTBLENDING_5)) {
            eqs_curId = true;
            if (rules.equals("")) {
                rules = rules + iDictService.get("paymentblending_matching_rule", "5");
            } else {
                rules = rules + "+" + iDictService.get("paymentblending_matching_rule", "5");
            }
        }
        for (int i = 0; i < dtlList.size(); i++) {
            DataTablesInput input = new DataTablesInput();
            String cdSign = dtlList.get(i).getCdSign();
            input.getFormdata().put("eqs_cdSign", cdSign);
            if (eqg_payMoney) {
                if (EmptyUtils.isEmpty(dtlList.get(i).getTransAmt())) {
                    continue;
                }
                input.getFormdata().put("eqg_amt", dtlList.get(i).getTransAmt());
            }
            if (eqs_payerAcc) {
                //考虑流水账号为空的情况
                if (EmptyUtils.isEmpty(dtlList.get(i).getPayAccNo())) {
                    dtlList.get(i).setPayAccNo("null");
                }
                //如果是收款，付款账号为对方账号。收款账号为本方账号
                if (cdSign.equals("0")) {
                    input.getFormdata().put("eqs_oppAccNo", dtlList.get(i).getPayAccNo());
                } else {
                    input.getFormdata().put("eqs_bankAcc", dtlList.get(i).getPayAccNo());
                }
            }
            if (eqs_payeeAcc) {
                //考虑流水账号为空的情况
                if (EmptyUtils.isEmpty(dtlList.get(i).getPayeeAccNo())) {
                    dtlList.get(i).setPayeeAccNo("null");
                }
                //如果是收款，付款账号为对方账号。收款账号为本方账号
                if (cdSign.equals("0")) {
                    input.getFormdata().put("eqs_bankAcc", dtlList.get(i).getPayeeAccNo());
                } else {
                    input.getFormdata().put("eqs_oppAccNo", dtlList.get(i).getPayeeAccNo());
                }
            }
            if (wishPayday) {
                if (EmptyUtils.isEmpty(dtlList.get(i).getCharageDate())) {
                    continue;
                }
                Date date = dtlList.get(i).getCharageDate();
                String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
                String beginDate = dateStr;
                String endDate = dateStr;
                input.getFormdata().put("ged_transTime", beginDate);
                input.getFormdata().put("led_transTime", endDate);
            }
            if (eqs_curId) {
                if (EmptyUtils.isEmpty(dtlList.get(i).getCurId())) {
                    continue;
                }
                input.getFormdata().put("eqs_curId", dtlList.get(i).getCurId());
            }
            DataTablesOutput output = this.accDtlService.getDataTablesUnionForReceiptBlending(input);
            if ("0".equals(fuzzyMatching)) {
                if (output.getData().size() == 1) {
                    List<BisAccDtlDTO> list = output.getData();
                    if (!tempMap.containsKey(list.get(0).getId())) {
                        tempMap.put(list.get(0).getId(), dtlList.get(i));
                    } else {
                        repeatMap.put(list.get(0).getId(), dtlList.get(i));
                    }
                }
            } else {
                if (output.getData().size() >= 1) {
                    List<BisAccDtlDTO> list = output.getData();
                    if (!tempMap.containsKey(list.get(0).getId())) {
                        tempMap.put(list.get(0).getId(), dtlList.get(i));
                    }
                }
            }
        }
        for (String key : tempMap.keySet()) {
            BisBankReceiptDtlVO dtl = tempMap.get(key);
            if (repeatMap.containsKey(key)) {
                continue;
            } else {
                isHasData = true;
                this.toUpdateBlendSign(dtl.getId(), BtBankAccConstant.BLENDSIGN_1);
                this.saveBisReceiptDtl(key, dtl.getId(), rules);
            }
        }
        if (!isHasData) {
            msg = "bankacc.message.notDate";
        }
        return msg;
    }
    /**
     * 电子回单勾对批量处理线程池
     */
    private static ExecutorService BISBANKRECEIPTBLENDING_START_EXECUTOR_SERVICE = SmartThreadPoolManager.wrap(Executors.newFixedThreadPool(20));

    //记录自动银行电子回单勾兑任务操作执行过程中，使用了的流水
    private static ConcurrentHashMap<String,String> DTL_ID_MAP = new ConcurrentHashMap<>();


    public void destroy() throws Exception {
        BISBANKRECEIPTBLENDING_START_EXECUTOR_SERVICE.shutdown();
    }
    @Autowired(required = false)
    private BisBankReceiptBlendingServer bisBankReceiptBlendingServer;
    /**
     * 自动银行电子回单勾兑
     */
    @Override
    @XxlJob("BisBankReceiptBlendingServer")
    public void run(String arguments) {
        try {
            Date curDate = DateUtils.getDate();
            this.logger.info(curDate + "---启用自动银行电子回单勾兑，开始执行定时任务---");
            String fuzzyMatching = configService.getConfig(ConfigConstant.ELE, ReceiptConstant.ELE_CONFIG_FUZZY_MATCHING).getValue();
            DataTablesInput input1 = new DataTablesInput();
            //一次默认600条
            int size = 600;
            //后台任务每批次执行笔数
            String autoCount = configService.getConfig(ConfigConstant.ELE, ReceiptConstant.ELE_CONFIG_AUTO_COUNT).getValue();
            if(EmptyUtils.isNotEmpty(autoCount)){
                size = Integer.parseInt(autoCount);
            }
            input1.setLength(size);
            String rollMateStr = configService.getConfig(ConfigConstant.ELE, ReceiptConstant.ELE_CONFIG_ROLL_MATE).getValue();
            //默认滚动3天
            int rollMate = 3;
            if(EmptyUtils.isNotEmpty(rollMateStr)){
                rollMate = Integer.parseInt(rollMateStr);
            }
            DataTablesOutput out = bisBankReceiptBlendingDao.getDtlRules(input1);
            List<BisBankReceiptDtlAutoVO> list = out.getData();
            //多条策略一条电子回单放到同一个线程里匹配
            Map<String,List<BisBankReceiptDtlAutoVO>> map = new HashMap<>();
            for (BisBankReceiptDtlAutoVO bisPaymentBlendingVO : list) {
                if(map.containsKey(bisPaymentBlendingVO.getReceiptId())){
                    List<BisBankReceiptDtlAutoVO> tempList = map.get(bisPaymentBlendingVO.getReceiptId()) ;
                    tempList.add(bisPaymentBlendingVO);
                    map.put(bisPaymentBlendingVO.getReceiptId(),tempList);
                }else{
                    List<BisBankReceiptDtlAutoVO> tempList = new ArrayList<>();
                    tempList.add(bisPaymentBlendingVO);
                    map.put(bisPaymentBlendingVO.getReceiptId(),tempList);
                }
            }
            final CountDownLatch countDownLatch = new CountDownLatch(list.size());
            //线程执行结果集合
            List<Future<String>> futures = new ArrayList<>();
            for (String id : map.keySet()) {
                List<BisBankReceiptDtlAutoVO> bisBankReceiptDtlAutoVOList = map.get(id);
                final List<BisBankReceiptDtlAutoVO> tempList = bisBankReceiptDtlAutoVOList;
                final String fuzzyMatchingTemp  = fuzzyMatching;
                final int finalRollmate = rollMate;
                //使用线程池处理流程启动，并返回结果
                Future<String> submit = BISBANKRECEIPTBLENDING_START_EXECUTOR_SERVICE.submit(new Callable<String>() {
                    @Override
                    public String call() {
                        try {
                            bisBankReceiptBlendingServer.bisBankReceiptBlending(tempList,fuzzyMatchingTemp,finalRollmate);
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
            this.logger.info("---启用自动银行电子回单勾兑，结束执行定时任务---");
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            //用完了清流水缓存
            DTL_ID_MAP.clear();
        }
    }

    public void bisBankReceiptBlending(List<BisBankReceiptDtlAutoVO> tempList,String fuzzyMatching,int rollMate){
        for(BisBankReceiptDtlAutoVO bisBankReceiptDtlAutoVO : tempList) {
            if (bisBankReceiptDtlAutoVO.getRules() != null) {
                if (bisBankReceiptDtlAutoVO.getIsExistId().equals("0")) {
                    String[] bankAccTypeStrs = bisBankReceiptDtlAutoVO.getRules().split(",");
                    String bankAccTypeStr = "";
                    HashMap<String, String> map = new HashMap<>();
                    for (String s : bankAccTypeStrs) {
                        bankAccTypeStr = bankAccTypeStr + "," + s;
                    }
                    DataTablesInput input = new DataTablesInput();
                    if (bankAccTypeStr.contains("corpId")) {
                        input.getFormdata().put("eqg_corpId", bisBankReceiptDtlAutoVO.getCorpId());
                        if (bisBankReceiptDtlAutoVO.getCdSign().equals("0")) {
                            map.put("payeeAccNo", bisBankReceiptDtlAutoVO.getPayeeAccNo());
                        } else {
                            map.put("payAccNo", bisBankReceiptDtlAutoVO.getPayAccNo());
                        }
                    }
                    if (bankAccTypeStr.contains("amt")) {
                        input.getFormdata().put("eqg_amt", bisBankReceiptDtlAutoVO.getAmt());
                        map.put("amt", bisBankReceiptDtlAutoVO.getAmt());
                    }
                    if (bankAccTypeStr.contains("oppAccNo")) {
                        //如果是收款，付款账号为对方账号。收款账号为本方账号
                        if (bisBankReceiptDtlAutoVO.getCdSign().equals("0")) {
                            String payAccNo = bisBankReceiptDtlAutoVO.getPayAccNo();
                            //考虑流水账号为空的情况
                            if (EmptyUtils.isEmpty(payAccNo)) {
                                payAccNo = "null";
                            }
                            input.getFormdata().put("eqs_oppAccNo", payAccNo);
                            map.put("payAccNo", payAccNo);
                        } else {
                            String payeeAccNo = bisBankReceiptDtlAutoVO.getPayeeAccNo();
                            //考虑流水账号为空的情况
                            if (EmptyUtils.isEmpty(payeeAccNo)) {
                                payeeAccNo = "null";
                            }
                            input.getFormdata().put("eqs_oppAccNo", payeeAccNo);
                            map.put("payeeAccNo", payeeAccNo);
                        }
                    }
                    if (bankAccTypeStr.contains("oppAccName")) {
                        //如果是收款，付款账号为对方账号。收款账号为本方账号
                        if (bisBankReceiptDtlAutoVO.getCdSign().equals("0")) {
                            input.getFormdata().put("eqs_oppAccName", bisBankReceiptDtlAutoVO.getPayAccName());
                            map.put("payAccName", bisBankReceiptDtlAutoVO.getPayAccName());
                        } else {
                            input.getFormdata().put("eqs_oppAccName", bisBankReceiptDtlAutoVO.getPayeeAccName());
                            map.put("payeeAccName", bisBankReceiptDtlAutoVO.getPayeeAccName());
                        }
                    }
                    if (bankAccTypeStr.contains("bankAcc")) {
                        //如果是收款，付款账号为对方账号。收款账号为本方账号
                        if (bisBankReceiptDtlAutoVO.getCdSign().equals("0")) {
                            String payeeAccNo = bisBankReceiptDtlAutoVO.getPayeeAccNo();
                            //考虑流水账号为空的情况
                            if (EmptyUtils.isEmpty(payeeAccNo)) {
                                payeeAccNo = "null";
                            }
                            input.getFormdata().put("eqs_bankAcc", payeeAccNo);
                            map.put("payeeAccNo", payeeAccNo);
                        } else {
                            String payAccNo = bisBankReceiptDtlAutoVO.getPayAccNo();
                            //考虑流水账号为空的情况
                            if (EmptyUtils.isEmpty(payAccNo)) {
                                payAccNo = "null";
                            }
                            input.getFormdata().put("eqs_bankAcc", payAccNo);
                            map.put("payAccNo", payAccNo);
                        }
                    }
                    if (bankAccTypeStr.contains("accName")) {
                        //如果是收款，付款账号为对方账号。收款账号为本方账号
                        if (bisBankReceiptDtlAutoVO.getCdSign().equals("0")) {
                            input.getFormdata().put("eqs_accName", bisBankReceiptDtlAutoVO.getPayeeAccName());
                            map.put("payeeAccName", bisBankReceiptDtlAutoVO.getPayeeAccName());
                        } else {
                            input.getFormdata().put("eqs_accName", bisBankReceiptDtlAutoVO.getPayAccName());
                            map.put("payAccName", bisBankReceiptDtlAutoVO.getPayAccName());
                        }
                    }
                    if (bankAccTypeStr.contains("transTime")) {
                        Date date = bisBankReceiptDtlAutoVO.getCharageDate();
                        String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
                        String beginDate = dateStr;
                        //相差天数
                        Integer transDays = bisBankReceiptDtlAutoVO.getTransDays();
                        if (transDays != null) {
                            beginDate = DateUtils.formatDate(DateUtils.addDay(date, -transDays), "yyyy-MM-dd");;
                        }
                        String endDate = dateStr;
                        input.getFormdata().put("ged_transTime", beginDate);
                        input.getFormdata().put("led_transTime", endDate);
                        map.put("charageDate", dateStr);
                    }
                    input.getFormdata().put("eqs_cdSign", bisBankReceiptDtlAutoVO.getCdSign());
                    map.put("cdSign", bisBankReceiptDtlAutoVO.getCdSign());
                    if (bankAccTypeStr.contains("uses")) {
                        input.getFormdata().put("eqs_uses", bisBankReceiptDtlAutoVO.getUsefor());
                        map.put("usefor", bisBankReceiptDtlAutoVO.getUsefor());
                    }
                    if (bankAccTypeStr.contains("curId")) {
                        input.getFormdata().put("eqs_curId", bisBankReceiptDtlAutoVO.getCurId());
                        map.put("curId", bisBankReceiptDtlAutoVO.getCurId());
                    }
                    List<BisBankReceiptDtlAutoVO> relist = bisBankReceiptBlendingDao.getForReceipt(bankAccTypeStr, map, bisBankReceiptDtlAutoVO.getCdSign());
                    DataTablesOutput output = this.bisAccDtlService.getDataTablesUnionForReceiptBlending(input);
                    if ("0".equals(fuzzyMatching)) {
                        if (output.getData().size() == 1 && relist.size() == 1) {
                            //如果有多条，使用筛选方法获取一笔流水
                            String dtlId = bisAccDtlDTOListTOOne(output.getData());
                            if (EmptyUtils.isNotEmpty(dtlId)) {
                                //更新勾兑标志为已勾兑
//                                int result = this.bisBankReceiptBlendingDao.toUpdateBlendSignForDtl(dtlId, BtBankAccConstant.BLENDSIGN_1);
//                                if (result > 0) {
                                    this.toUpdateBlendSign(bisBankReceiptDtlAutoVO.getReceiptId(), BtBankAccConstant.BLENDSIGN_1, BtBankAccConstant.BLENDSIGN_0);
                                    this.saveBisReceiptDtl(dtlId, bisBankReceiptDtlAutoVO.getReceiptId(), bisBankReceiptDtlAutoVO.getRulesName());
                                    //记录到缓存中
                                    DTL_ID_MAP.put(dtlId, "1");
                                }
//                            }
                        }
                    } else {
                        if (output.getData().size() >= 1 && relist.size() >= 1) {
                            //如果有多条，使用筛选方法获取一笔流水
                            String dtlId = bisAccDtlDTOListTOOne(output.getData());
                            if (EmptyUtils.isNotEmpty(dtlId)) {
                                //更新勾兑标志为已勾兑
                                int result = this.bisBankReceiptBlendingDao.toUpdateBlendSignForDtl(dtlId, BtBankAccConstant.BLENDSIGN_1);
                                if (result > 0) {
                                    this.toUpdateBlendSign(bisBankReceiptDtlAutoVO.getReceiptId(), BtBankAccConstant.BLENDSIGN_1, BtBankAccConstant.BLENDSIGN_0);
                                    this.saveBisReceiptDtl(dtlId, bisBankReceiptDtlAutoVO.getReceiptId(), bisBankReceiptDtlAutoVO.getRulesName());
                                    //记录到缓存中
                                    DTL_ID_MAP.put(dtlId, "1");
                                }
                            }
                        }
                    }
                }
            }
            if (bisBankReceiptDtlAutoVO.getIsExistId().equals("1")) {
                this.JustOne(bisBankReceiptDtlAutoVO);
            }
        }
        if(EmptyUtils.isNotEmpty(tempList) && tempList.size() >0) {
            //滚动当满足条件
            Date transTime = tempList.get(0).getCharageDate();
            transTime = DateUtils.addDay(transTime,rollMate);
            Date nowDate = DateUtils.getDate();
            if(transTime.compareTo(nowDate) < 0) {
                bisBankReceiptBlendingDao.toUpdateAutoSign(tempList.get(0).getReceiptId(), BtBankAccConstant.BLENDSIGN_1);
            }
        }
    }

    /**
     * 从集合列表中取一个还没有使用的流水
     * @param dtoList
     * @return
     */
    private String bisAccDtlDTOListTOOne(List<BisAccDtlDTO> dtoList){
        if(CollectionUtils.isEmpty(dtoList)){
            return null;
        }
        List<String> dtlIdList= bisBankReceiptBlendingDao.getDtlId(dtoList.stream().map(BisAccDtlDTO::getId).collect(Collectors.toList()));
        Set <String> dtlIdListSet =new HashSet<String>(dtlIdList);
        for (BisAccDtlDTO dto:dtoList){
            if (dtlIdListSet.contains(dto.getId())){
                continue;
            }
            if (DTL_ID_MAP.containsKey(dto.getId())){
                continue;
            }
            return dto.getId();
        }
        return null;
    }

    /*
    唯一标识勾兑
     */
    public void JustOne(BisBankReceiptDtlAutoVO bisBankReceiptDtlAutoVO) {
        String dtlONlyId = bisBankReceiptDtlAutoVO.getDtlOnlyId();
        String receiptONlyId = bisBankReceiptDtlAutoVO.getReceiptOnlyId();
        String checkString = "";
        if (receiptONlyId.equals("BIS_EBILL_NO")) {//业务单号
            if (EmptyUtils.isEmpty(bisBankReceiptDtlAutoVO.getBisEbillNo())) {
                return;
            }else{
                checkString = bisBankReceiptDtlAutoVO.getBisEbillNo();
            }
        }else if(receiptONlyId.equals("TRANS_SERIAL_NO")){
            if (EmptyUtils.isEmpty(bisBankReceiptDtlAutoVO.getTransSerialNo())) {
                return;
            }else {
                checkString = bisBankReceiptDtlAutoVO.getTransSerialNo();
            }
        }
        List<BisBankReceiptDtlAutoVO> relist = bisBankReceiptBlendingDao.getForReceipt(receiptONlyId, null, checkString);
        if (EmptyUtils.isNotEmpty(checkString)) {
            List<BisAccDtlDTO> li = new ArrayList<>();
            //BIS_EBILL_NO,BANK_SERIAL_NO,ACC_NO
            if (dtlONlyId.equals("BIS_EBILL_NO") || dtlONlyId.equals("BANK_SERIAL_NO") || dtlONlyId.equals("ACC_NO")) {//业务单号
                li = bisBankReceiptBlendingDao.getJust(dtlONlyId,checkString);
            }

            if (li.size() == 1 && relist.size() == 1) {
                bisBankReceiptDtlAutoVO.setRulesName(multiLangServiceI.getLang("table.bisBankExecution.unique"));
                this.toUpdateBlendSign(bisBankReceiptDtlAutoVO.getReceiptId(), BtBankAccConstant.BLENDSIGN_1);
                //更新勾兑标志为已勾兑
                this.bisBankReceiptBlendingDao.toUpdateBlendSignForDtl(li.get(0).getId(), BtBankAccConstant.BLENDSIGN_1);
                this.saveBisReceiptDtl(li.get(0).getId(), bisBankReceiptDtlAutoVO.getReceiptId(), bisBankReceiptDtlAutoVO.getRulesName());
            }
        }

    }
    /**
     * 更新电子回单勾兑标志
     *
     * @param id
     * @param blendSign
     */
    public int toUpdateBlendSign(String id, String blendSign) {
        return this.bisBankReceiptBlendingDao.toUpdateBlendSign(id, blendSign);
    }
    /**
     * 更新电子回单勾兑标志
     *
     * @param id
     * @param blendSign
     */
    public int toUpdateBlendSign(String id, String blendSign, String oldBlendSign) {
        return this.bisBankReceiptBlendingDao.toUpdateBlendSign(id, blendSign, oldBlendSign);
    }
    /**
     * 插入银行电子回单和银行流水的关系
     *
     * @param dtlId
     * @param receiptId
     */
    public int saveBisReceiptDtl(String dtlId, String receiptId, String rulesName) {
        return this.bisBankReceiptBlendingDao.saveBisReceiptDtl(dtlId, receiptId, rulesName);
    }
    /**
     * 手工勾兑保存
     *
     * @param dtlId
     * @param receiptId
     */
    public void manualBlending(String dtlId, String receiptId) {
        String[] dtlIds = dtlId.split(",");
        String[] receiptIds = receiptId.split(",");
        for (String receiptIdStr : receiptIds) {
            for (String dtlIdStr : dtlIds) {
                //插入关系关联表
                this.bisBankReceiptBlendingDao.saveBisReceiptDtl(dtlIdStr, receiptIdStr, null);
            }
            //更新勾兑标志为已勾兑
            this.bisBankReceiptBlendingDao.toUpdateBlendSign(receiptIdStr, BtBankAccConstant.BLENDSIGN_1);
        }
    }
    /**
     * 取消勾兑
     *
     * @param receiptId
     */
    public void cancelBlending(String receiptId) {
        String[] receiptIds = receiptId.split(",");
        //所有的流水id
        List<String> dtlIdList = new ArrayList<>();
        //所有的业务id
        List<String> receiptIdList = new ArrayList<>();
        for (String receiptIdStr : receiptIds) {
            receiptIdList.add(receiptIdStr);
            List<String> tempList = bisBankReceiptBlendingDao.getDtlId(receiptIdStr);
            dtlIdList.addAll(tempList);
        }
        for (String dtlId : dtlIdList) {
            List<String> tempList = bisBankReceiptBlendingDao.getReceiptId(dtlId);
            receiptIdList.addAll(tempList);
            this.bisBankReceiptBlendingDao.deleteDtlId(dtlId);
        }
        for (String receiptIdStr : receiptIdList) {
            //删除中间关联表
            this.bisBankReceiptBlendingDao.deleteBisReceiptDtl(receiptIdStr);
            //更新勾兑标志为已勾兑
            this.bisBankReceiptBlendingDao.toUpdateBlendSign(receiptIdStr, BtBankAccConstant.BLENDSIGN_0);
        }
    }
}
