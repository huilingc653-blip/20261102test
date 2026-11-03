package com.bytter.biz.finacial.report.service;

import com.alibaba.excel.util.ObjectUtils;
import com.bytter.biz.finacial.report.dao.BalanceQueryDao;
import com.bytter.biz.finacial.report.vo.BalanceQueryVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service("balanceQueryService")
public class BalanceQueryService extends BaseService<BalanceQueryVo,String>{
    @Autowired
    private BalanceQueryDao balanceQueryDao;
    @DubboReference(check = false)
    private IFcSubjectService iFcSubjectService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyServiceImpl;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private ISystemForeignService iSystemForeignService;
    public List<BalanceQueryVo> queryOnlineLedgerResult(BalanceQueryVo vo,String resource, String userCode) {
        List<String> corpAuth = corpService.getUserCorpIds(resource ,userCode);
        if (EmptyUtils.isBlank(vo.getCurId())) {//获取币别01，人民币
          //默认为本位币
            BtCurrencyDTO currency = btCurrencyServiceImpl.getNatualCurrency();
            vo.setCurId(currency.getId());
        }
        if (EmptyUtils.isBlank(vo.getSumType())) {//1：按单位+科目，2：按科目+单位，3：按科目+银行
            vo.setSumType("1");
        }
        List<BalanceQueryVo> list = new ArrayList<BalanceQueryVo>();
        if ("3".equals(vo.getSumType())) {//比较维度
//			list = balanceQueryDao.queryAccBalanceAndBank(vo, userCode,corpAuth, "true");//查询结果
            BalanceQueryDTO balanceQueryDTO = new BalanceQueryDTO();
            BeanUtils.copyProperties(vo,balanceQueryDTO);
            List<BalanceQueryDTO> balanceQueryDTOS = iSystemForeignService.queryAccBalanceAndBank(balanceQueryDTO, userCode, corpAuth, "true");
            if (balanceQueryDTOS == null) {
                return null;
            }
            List<String> subjectIdList = new ArrayList<>();
            List<String> onlineAccountList = new ArrayList<>();
            balanceQueryDTOS.forEach(a -> {
                subjectIdList.add(a.getSubjectId());
                onlineAccountList.add(a.getBlnctrAcc());
            });
            List<FcSubjectDTO> fcSubjectDTOS = iFcSubjectService.getSubjectByIds(subjectIdList);
            List<BalanceQueryVo> balanceQueryVos = balanceQueryDao.queryAccBalanceAndBank_2(onlineAccountList);
            Map<String, String> subjectIdAndNameMap = fcSubjectDTOS.stream().collect(Collectors.toMap(FcSubjectDTO::getId, FcSubjectDTO::getSubjectName));
            Map<String, BalanceQueryVo> accountMap = balanceQueryVos.stream().collect(Collectors.toMap(BalanceQueryVo::getAccount, Function.identity()));
            for(BalanceQueryDTO dto : balanceQueryDTOS){
                BalanceQueryVo balanceQueryVo = accountMap.get(dto.getBlnctrAcc());
                if(null != balanceQueryVo){
                    dto.setFreezeMoney(balanceQueryVo.getFreezeMoney());
                    dto.setOnlineBalance(balanceQueryVo.getOnlineBalance());
                    dto.setCheckBalance(balanceQueryVo.getCheckBalance());
                    dto.setTallyCnt(balanceQueryVo.getTallyCnt());
                    dto.setCheckCnt(balanceQueryVo.getCheckCnt());
                }
                //BeanUtils.copyProperties(balanceQueryVo,dto);
                //dto.setSubjectName(subjectIdAndNameMap.get(dto.getSubjectId()));
            }

            list = BeanUtils.copyListProperties(balanceQueryDTOS, BalanceQueryVo.class);
            //list = list.stream().sorted(Comparator.comparing(BalanceQueryVo::getSubjectCode).thenComparing(Comparator.nullsFirst(Comparator.comparing(BalanceQueryVo::getBankType))).thenComparing(BalanceQueryVo::getBlnctrAcc)).collect(Collectors.toList());
        } else {
            list = balanceQueryDao.queryResult(vo, userCode,corpAuth,"true");//查询结果
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        // 添加冗余值
        Map<String, DictDTO> dictMap = dictService.listMap("balance.query.report");
        list.forEach(item -> Optional.ofNullable(item.getBalanceWay()).flatMap(balanceWay -> Optional.ofNullable(dictMap.get(String.valueOf(balanceWay))))
                .ifPresent(dict -> item.setBalanceName(dict.getName())));
        List<BalanceQueryVo> resultList;
        if ("4".equals(vo.getSumType())) {//科目加账号，add by liusy
            resultList = setSubjectBalanceBean(list, vo);
        } else {
            resultList = setSubjectBalanceBean(list, vo.getSumType(), vo);
        }
        return resultList;
  }
    /**
     * <br>
     * <b>功能：</b>按网点设置账户科目实时余额<br>
     */
    private List<BalanceQueryVo> setSubjectBalanceBean(List<BalanceQueryVo> listFcAcc, String sumType, BalanceQueryVo bean) {
        //网点集合map
        LinkedHashMap<String, String> netMap = new LinkedHashMap<String, String>();
        //单位集合
        LinkedHashMap<String, BalanceQueryVo> subMap = new LinkedHashMap<String, BalanceQueryVo>();
        //科目余额实体
        LinkedHashMap<String, LinkedHashMap<String, BalanceQueryVo>> netAccSubMap = new LinkedHashMap<>();
        //网点科目余额集合
        List<BalanceQueryVo> netBalanceBeans = new ArrayList<BalanceQueryVo>();
        String curKey = "";//币别key
        String type = "";
        Boolean corpRelation = false;//是否具有上下级单位关系
        Map<String, Boolean> corpRMap = new HashMap<String, Boolean>();
        Map<String, CorpDTO> corpMap = new HashMap<String, CorpDTO>();
        
        if (EmptyUtils.isBlank(sumType)) {
            sumType = "1";
        }
        if ("1".equals(sumType)) {
            //getCorpRelation(bean.getCorpId(), corpMap, corpRMap);//判断单位间的层级关系
        }
        
        for (BalanceQueryVo balanceQuery : listFcAcc) {
            balanceQuery.setNoCheckBalance(balanceQuery.getOnlineBalance().subtract(balanceQuery.getCheckBalance()));//未复核余额=记账余额-复核余额
            String netCode = balanceQuery.getNetId();//获取dao查询结果的网点、单位、科目、币别Id
            String corpCode = balanceQuery.getCorpId();
            String corpName = balanceQuery.getCorpName();
            String subjectCode = balanceQuery.getSubjectId();
            String subjectName = balanceQuery.getSubjectName();
            String balanceName=balanceQuery.getBalanceName();
            String curCode = balanceQuery.getCurId();
            String bankName = balanceQuery.getTypeName();//银行名称
            
            corpRelation = corpRMap.get(corpCode);//判断单位是否有上下级关系
            if (null == corpRelation) {
                corpRelation = false;//没有
            }
            if ("1".equals(sumType)) {//判断维度1：按单位+科目，
                curKey = netCode + "_" + corpCode + "_" + subjectCode + "_" + curCode;//币别key
                type = netCode + "_" + corpCode;//类型为网点+单位,大分组
            } else if ("2".equals(sumType)) {//2：按科目+单位
                curKey = netCode + "_" + subjectCode + "_" + corpCode + "_" + curCode;
                type = netCode + "_" + subjectCode;
            } else if ("3".equals(sumType)) {//3：按科目+银行
                String bankType = balanceQuery.getBankType();
                if (EmptyUtils.isBlank(bankType)) {
                    bankType = "99999";//用账号代替银行类别
                }
                curKey = netCode + "_" + subjectCode + "_" + bankType + "_" + curCode;
                type = netCode + "_" + subjectCode;
            }
            LinkedHashMap<String, BalanceQueryVo> accSubMap = netAccSubMap.get(netCode);//科目余额实体中查找key为netCode的数据
            String netName = netMap.get(netCode);//设置网点下单位集合，netCode通过遍历获得的
            if (EmptyUtils.isBlank(netName)) {//netMap（网点集合）为空
            	if(!ObjectUtils.isEmpty(netCode)) {
            		netName = corpService.get(netCode)!=null ? corpService.get(netCode).getName() : "";
                    balanceQuery.setNetName(netName);
            		netMap.put(netCode, balanceQuery.getNetName());//在netMap中添加网点
                    accSubMap = new LinkedHashMap<>();
                    netAccSubMap.put(netCode, accSubMap);//科目余额实体中添加网点
            	}
                
            }
            BalanceQueryVo accSubBalBean = accSubMap.get(type);//设置单位下科目集合 type：维度中的类型
            if (accSubBalBean == null) { //accSubBalBean第二层
                accSubBalBean = new BalanceQueryVo();
                if ("2".equals(sumType)) {//判断维度2：按科目+单位，
                    accSubBalBean.setCorpId(netCode); //设置vo中结算中心的值
                    accSubBalBean.setCorpName(netName);
                } else if ("1".equals(sumType)) {
                    accSubBalBean.setCorpId(corpCode); //设置vo中单位的值
                    accSubBalBean.setCorpName(corpName);
                    accSubBalBean.setTypeName(bankName);
                } else if ("3".equals(sumType)) {//3：按科目+银行
                    accSubBalBean.setCorpId(corpCode); //设置vo中单位的值
                    accSubBalBean.setCorpName(corpName);
                }
                accSubBalBean.setSubBalBeans(new ArrayList<BalanceQueryVo>());
                accSubMap.put(type, accSubBalBean);//添加单位科目集合中的值
            }
            
            if ("1".equals(sumType)) {//判断维度
                setCorpBalRelation(netCode, corpCode, accSubMap, corpMap, accSubBalBean);
            }
            BalanceQueryVo subBean = subMap.get(curKey);//设置科目下账户集合  subMap保存分组键对应的整体对象（内置集合）
            if (subBean == null) {//subBean第三层
                subBean = new BalanceQueryVo ();
                subBean.setSubBalBeans(new ArrayList<BalanceQueryVo>());//第三次 科目集合？
                subBean.setSubjectName(subjectName);
                subBean.setBalanceName(balanceName);
                subMap.put(curKey, subBean);
                accSubBalBean.getSubBalBeans().add(subBean);
            }
            BalanceQueryVo balBean = setBalanceBean(balanceQuery, sumType);//参数balanceQuery是dao数据遍历结果
            subBean.getSubBalBeans().add(balBean);
            if (subBean.getSubBalBeans().size() >= 1) {
                //设置小计金额
                setSumBalance(subBean,balBean, sumType);
            }
            if ("1".equals(sumType)) {
                subBean.setSubjectId(subjectCode);
            } else if ("2".equals(sumType)) {
                subBean.setSubjectId(corpCode);
            } else if ("3".equals(sumType)) {
                subBean.setSubjectId(EmptyUtils.isBlank(balanceQuery.getBankType()) ? "99999" : balanceQuery.getBankType());
            }
            setSuperCorpBalBean(netCode, corpCode, accSubMap, corpMap, balBean);//计算上级单位总金额
        }
        for (Entry<String, LinkedHashMap<String, BalanceQueryVo>> entry2 : netAccSubMap.entrySet()) {//遍历科目余额实体
            String netCode = entry2.getKey();
            LinkedHashMap<String, BalanceQueryVo> tempMap = entry2.getValue();
            List<BalanceQueryVo> accSubBalBeans = new ArrayList<BalanceQueryVo>();
            BalanceQueryVo netBean = new BalanceQueryVo(); //netBean第一层
            for (Entry<String, BalanceQueryVo> entry : tempMap.entrySet()) {//遍历value
                BalanceQueryVo accSubBalBean = entry.getValue();
                if ("1".equals(accSubBalBean.getHasFather())) {
                    continue;
                }
                setNetSumSubBalance(accSubBalBean, netBean, sumType);//设置合计金额
                
                accSubBalBeans.add(accSubBalBean);//将科目余额实体存进集合
            }
            netBean.setSubBalBeans(accSubBalBeans);
            netBean.setNetId(netCode);
            netBean.setNetName(netMap.get(netCode));
            netBalanceBeans.add(netBean);//网点科目余额集合
            setNetSumSubBalance(netBean, null, null);
        }
        //sortNetBalanceBeans(netBalanceBeans, sumType);
        return netBalanceBeans;
    }
    /**
     * 按照科目+账号维度统计数据
     *
     */
    private List<BalanceQueryVo> setSubjectBalanceBean(List<BalanceQueryVo> listFcAcc,  BalanceQueryVo bean) {
        //科目账号余额集合
        List<BalanceQueryVo> accBalanceBeans = new ArrayList<BalanceQueryVo>();
//        科目分类
        LinkedHashMap<String, BalanceQueryVo> subjectMap = new LinkedHashMap<>();
        for (BalanceQueryVo balanceQuery : listFcAcc) {
            balanceQuery.setNoCheckBalance(balanceQuery.getOnlineBalance().subtract(balanceQuery.getCheckBalance()));//未复核余额=记账余额-复核余额
            BalanceQueryVo subBean = subjectMap.get(balanceQuery.getSubjectId());//设置科目下账户集合  subMap保存分组键对应的整体对象（内置集合）
            if (subBean == null) {
                subBean = new BalanceQueryVo();
                subBean.setSubBalBeans(new ArrayList<BalanceQueryVo>());
                subBean.setSubjectName(balanceQuery.getSubjectName());
                subjectMap.put(balanceQuery.getSubjectId(), subBean);
            }
            subBean.getSubBalBeans().add(balanceQuery);
            //            计算小计
            setSumBalance(subBean,balanceQuery, "4");
        }
        for (Entry<String, BalanceQueryVo> balanceQueryVoEntry : subjectMap.entrySet()) {
            accBalanceBeans.add(balanceQueryVoEntry.getValue());
        }
        return accBalanceBeans;
    }
    /**
     * <br>
     * <b>功能：</b>获取网点间关系<br>
     */
    private void getCorpRelation(String corpCode, Map<String, CorpDTO> corpMap, Map<String, Boolean> corpRMap) {
        CorpDTO corp = corpService.get(corpCode);
        corpMap.put(corpCode, corp);
        for (Entry<String, CorpDTO> entry : corpMap.entrySet()) {
            CorpDTO btCorp = entry.getValue();
            String superCorp = btCorp.getParentId();//上级单位
            if (null != corpMap.get(superCorp)) {
                corpRMap.put(superCorp, true);
            }
        }
    }
    /**
     * <br>
     * <b>功能：</b>递归计算上级单位总计金额<br>
     */
    private void setSuperCorpBalBean(String netCode, String corpCode, Map<String, BalanceQueryVo> accSubMap,
            Map<String, CorpDTO> corpMap, BalanceQueryVo balBean) {
        CorpDTO btCorp = corpMap.get(corpCode);
        if (null == btCorp) {
            return;
        }
        String superCorpCode = btCorp.getParentId();//上级单位?是上级单位还是上级网点-网点btCorp.getNetId()
        if (EmptyUtils.isBlank(superCorpCode)) {
            return;
        }
        
        CorpDTO superCorp = corpMap.get(superCorpCode);
        if (null == superCorp) {
            return;
        }
        
        String superType = netCode + "_" + superCorpCode;
        BalanceQueryVo accSubBalBean2 = accSubMap.get(superType);
        if (accSubBalBean2 == null) {
            accSubBalBean2 = new BalanceQueryVo();
            //AccBalanceBean sumBean = new AccBalanceBean();
            /*if (null != superCorp) {
                sumBean.setCorpCode(superCorpCode);
                sumBean.setCorpName(superCorp.getCorp_name());//上级单位名称
                accSubBalBean2.setCorpName(superCorp.getCorp_name());
            }*/
            
            accSubBalBean2.setCorpId(superCorpCode);
            accSubBalBean2.setIsTotal("1");
            
            //accSubBalBean2.setTotalBean(sumBean);
            accSubBalBean2.setSubBalBeans(new ArrayList<BalanceQueryVo>());
            accSubMap.put(superType, accSubBalBean2);
        } else {
            accSubBalBean2.setIsTotal("1");
            accSubBalBean2.setCorpId(superCorpCode);
            /*if (null == sumBean) {
                sumBean = new AccBalanceBean();
                if (null != superCorp) {
                    accSubBalBean2.setCorpName(superCorp.getCorp_name());//上级单位名称
                    sumBean.setCorpCode(superCorpCode);
                    sumBean.setCorpName(superCorp.getCorp_name());//上级单位名称
                }
                accSubBalBean2.setTotalBean(sumBean);
            }*/
            //计算总计金额
            accSubBalBean2.setFreezeMoney(accSubBalBean2.getFreezeMoney().add(balBean.getFreezeMoney()));
            accSubBalBean2.setOnlineBalance(accSubBalBean2.getOnlineBalance().add(balBean.getOnlineBalance()));
            accSubBalBean2.setCheckBalance(accSubBalBean2.getCheckBalance().add(balBean.getCheckBalance()));
            accSubBalBean2.setNoCheckBalance(accSubBalBean2.getNoCheckBalance().add(balBean.getNoCheckBalance()));
            accSubBalBean2.setTallyCnt(accSubBalBean2.getTallyCnt() + balBean.getTallyCnt());
            accSubBalBean2.setCheckCnt(accSubBalBean2.getCheckCnt() + balBean.getCheckCnt());
        }
        setSuperCorpBalBean(netCode, superCorpCode, accSubMap, corpMap, balBean);
    }
    /**
     * <br>
     * <b>功能：</b>设置每个账户实时余额信息,并小计、合计<br>
     */
    private BalanceQueryVo setBalanceBean(BalanceQueryVo accBankBean, String sumType) {
        //VFcAcc vFcAcc = accBankBean.getFcAcc();
        //AccBalanceBean balBean = new AccBalanceBean();
        if ("3".equals(sumType)) {//科目+银行维度显示银行类型
            accBankBean.setCorpName(formatNull(accBankBean.getTypeName()));
        }
        return accBankBean;
    }
    /**
     * <br>
     * <b>功能：</b>设置上级单位间科目关系<br>
     */
    private void setCorpBalRelation(String netCode, String corpCode, Map<String, BalanceQueryVo> accSubMap,
            Map<String, CorpDTO> corpMap, BalanceQueryVo accSubBalBean) {
        CorpDTO btCorp = corpMap.get(corpCode);
        if (null == btCorp) {
            return;
        }
        String superCorpCode = btCorp.getParentId();//上级单位
        if (EmptyUtils.isBlank(superCorpCode)) {
            return;
        }
        String superKey = netCode + "_" + superCorpCode;
        BalanceQueryVo superAccSubBalBean = accSubMap.get(superKey);//设置上级单位下科目集合
        if (superAccSubBalBean == null) {
            superAccSubBalBean = new BalanceQueryVo ();
            superAccSubBalBean.setSubBalBeans(new ArrayList<BalanceQueryVo>());
            superAccSubBalBean.setCorpId(superCorpCode);
            superAccSubBalBean.setIsTotal("1");
            accSubMap.put(superKey, superAccSubBalBean);
        }
        String type = netCode + "_" + corpCode;
        Map<String,BalanceQueryVo> map = new HashMap<String,BalanceQueryVo>();
        for(BalanceQueryVo vo:superAccSubBalBean.getSubBalBeans()){
            map.put(vo.getNetId()+"_"+vo.getCorpId(), vo);
        }
        if (map.get(type)!=null) {
            superAccSubBalBean.getSubBalBeans().add(accSubBalBean);
            //superAccSubBalBean.getChildSet().add(type);
        }
        accSubBalBean.setHasFather("1");//表示有上级节点
        setCorpBalRelation(netCode, superCorpCode, accSubMap, corpMap, superAccSubBalBean);
    }
    /**
     * <br>
     * <b>功能：</b>设置小计金额<br>
     */
    private void setSumBalance(BalanceQueryVo subBean, BalanceQueryVo balBean, String sumType) {
        subBean.setIsSum("1");//小计
        subBean.setFreezeMoney((subBean.getFreezeMoney()==null?BigDecimal.ZERO:subBean.getFreezeMoney()).add(balBean.getFreezeMoney()==null?BigDecimal.ZERO:balBean.getFreezeMoney()));
        subBean.setOnlineBalance((subBean.getOnlineBalance()==null?BigDecimal.ZERO:subBean.getOnlineBalance()).add(balBean.getOnlineBalance()==null?BigDecimal.ZERO:balBean.getOnlineBalance()));
        subBean.setCheckBalance((subBean.getCheckBalance()==null?BigDecimal.ZERO:subBean.getCheckBalance()).add(balBean.getCheckBalance()==null?BigDecimal.ZERO:balBean.getCheckBalance()));
        subBean.setNoCheckBalance((subBean.getNoCheckBalance()==null?BigDecimal.ZERO:subBean.getNoCheckBalance()).add(balBean.getNoCheckBalance()==null?BigDecimal.ZERO:balBean.getNoCheckBalance()));
        subBean.setTallyCnt((subBean.getTallyCnt()==null?0:subBean.getTallyCnt()) + (balBean.getTallyCnt()==null?0:balBean.getTallyCnt()));
        subBean.setCheckCnt((subBean.getCheckCnt()==null?0:subBean.getCheckCnt()) + (balBean.getCheckCnt()==null?0:balBean.getCheckCnt()));
        List<BalanceQueryVo> balBeans = subBean.getSubBalBeans();
        if ("1".equals(sumType)) {
            subBean.setSubjectName(balBeans.get(0).getSubjectName());
        } else if ("2".equals(sumType)) {
            subBean.setSubjectName(balBeans.get(0).getCorpName());
        } else if ("3".equals(sumType)) {
            subBean.setSubjectName(balBeans.get(0).getSubjectName());//存储字段为银行类型
        }
    }
    /**
     * <br>
     * <b>功能：</b>按照查询条件排序<br>
     */
    private void sortNetBalanceBeans(List<BalanceQueryVo> netBalanceBeans, final String sumType) {
        for (BalanceQueryVo netAccBalanceBean : netBalanceBeans) {
            List<BalanceQueryVo> accSubBalBeans = netAccBalanceBean.getSubBalBeans();
            sortCorpBalBean(accSubBalBeans);
        }
        Collections.sort(netBalanceBeans, new Comparator<BalanceQueryVo>() {
            public int compare(BalanceQueryVo o1, BalanceQueryVo o2) {
                return o1.getNetId().compareTo(o2.getNetId());
            }
        });
    }
    private void sortCorpBalBean(List<BalanceQueryVo> accSubBalBeans){
        for (BalanceQueryVo accSubBalBean : accSubBalBeans) {
            if (!EmptyUtils.isEmptyList(accSubBalBean.getSubBalBeans())) {
                sortCorpBalBean(accSubBalBean.getSubBalBeans());
            }
            sortSubjectBalBean(accSubBalBean);
        }
        /**
         * 单位
         */
        if (accSubBalBeans != null && accSubBalBeans.size() > 1) {
            Collections.sort(accSubBalBeans, new Comparator<BalanceQueryVo>() {
                public int compare(BalanceQueryVo o1, BalanceQueryVo o2) {
                    return o1.getCorpId().compareTo(o2.getCorpId());
                }
            });
        }
    }
    private void sortSubjectBalBean(BalanceQueryVo accSubBalBean) {
        List<BalanceQueryVo> subBalBeans = accSubBalBean.getSubBalBeans();
        for (BalanceQueryVo subjectBalanceBean : subBalBeans) {
            List<BalanceQueryVo> balBeans = subjectBalanceBean.getSubBalBeans();
            if (balBeans != null && balBeans.size() > 1) {
                Collections.sort(balBeans, new Comparator<BalanceQueryVo>() {
                    public int compare(BalanceQueryVo o1, BalanceQueryVo o2) {
                        return o1.getBlnctrAcc().compareTo(o2.getBlnctrAcc());
                    }
                });
            }
        }
        /**
         * 科目
         */
        if (subBalBeans != null && subBalBeans.size() > 1) {
            Collections.sort(subBalBeans, new Comparator<BalanceQueryVo>() {
                public int compare(BalanceQueryVo o1, BalanceQueryVo o2) {
                    return o1.getSubjectId().compareTo(o2.getSubjectId());
                }
            });
        }
    }
    
    /**
     * <br>
     * <b>功能：</b>设置合计金额<br>
     */
    private void setNetSumSubBalance(BalanceQueryVo accSubBalBean,BalanceQueryVo netBean, String sumType) {
        List<BalanceQueryVo> subBeans = accSubBalBean.getSubBalBeans();
        if(subBeans == null){
            return;
        }
        for (BalanceQueryVo balanceBean : subBeans) {
            accSubBalBean.setFreezeMoney(accSubBalBean.getFreezeMoney()==null?BigDecimal.ZERO:accSubBalBean.getFreezeMoney().add(balanceBean.getFreezeMoney()==null?BigDecimal.ZERO:balanceBean.getFreezeMoney()));
            accSubBalBean.setOnlineBalance(accSubBalBean.getOnlineBalance()==null?BigDecimal.ZERO:accSubBalBean.getOnlineBalance().add(balanceBean.getOnlineBalance()==null?BigDecimal.ZERO:balanceBean.getOnlineBalance()));
            accSubBalBean.setCheckBalance(accSubBalBean.getCheckBalance()==null?BigDecimal.ZERO:accSubBalBean.getCheckBalance().add(balanceBean.getCheckBalance()==null?BigDecimal.ZERO:balanceBean.getCheckBalance()));
            accSubBalBean.setNoCheckBalance(accSubBalBean.getNoCheckBalance()==null?BigDecimal.ZERO:accSubBalBean.getNoCheckBalance().add(balanceBean.getNoCheckBalance()));
            accSubBalBean.setTallyCnt(accSubBalBean.getTallyCnt() + balanceBean.getTallyCnt());
            accSubBalBean.setCheckCnt(accSubBalBean.getCheckCnt() + balanceBean.getCheckCnt());
        }
    }
    
    /**
     * <br>
     * <b>功能：</b>设置合计金额<br>
     */
    private void setSumSub(BalanceQueryVo accSubBalBean) {
        List<BalanceQueryVo> subBeans = accSubBalBean.getSubBalBeans();
        if(subBeans == null){
            return;
        }
        for (BalanceQueryVo balanceBean : subBeans) {
            accSubBalBean.setFreezeMoney(accSubBalBean.getFreezeMoney()==null?BigDecimal.ZERO:accSubBalBean.getFreezeMoney().add(balanceBean.getFreezeMoney()==null?BigDecimal.ZERO:balanceBean.getFreezeMoney()));
            accSubBalBean.setOnlineBalance(accSubBalBean.getOnlineBalance()==null?BigDecimal.ZERO:accSubBalBean.getOnlineBalance().add(balanceBean.getOnlineBalance()==null?BigDecimal.ZERO:balanceBean.getOnlineBalance()));
            accSubBalBean.setCheckBalance(accSubBalBean.getCheckBalance()==null?BigDecimal.ZERO:accSubBalBean.getCheckBalance().add(balanceBean.getCheckBalance()==null?BigDecimal.ZERO:balanceBean.getCheckBalance()));
            accSubBalBean.setNoCheckBalance(accSubBalBean.getNoCheckBalance()==null?BigDecimal.ZERO:accSubBalBean.getNoCheckBalance().add(balanceBean.getNoCheckBalance()));
            accSubBalBean.setTallyCnt(accSubBalBean.getTallyCnt() + balanceBean.getTallyCnt());
            accSubBalBean.setCheckCnt(accSubBalBean.getCheckCnt() + balanceBean.getCheckCnt());
        }
    }
    /***
     * 功能介绍 导出。<br>
     * <b>日期：</b>2018年1月29日<br>
     * @version 1.0
     * @author zhengxn 
     */
    public List<Map<String,Object>> excel(BalanceQueryVo vo,String resource,String userId){
        List<String> corpAuth = corpService.getUserCorpIds(resource ,userId);
        List<BalanceQueryVo> res = new ArrayList<BalanceQueryVo>();
        List<Map<String,Object>> map = new ArrayList<Map<String,Object>>();
        List<BalanceQueryVo> list = null;
        BalanceQueryVo accountVo = null;
        if(StringUtils.isNoneBlank(vo.getNetId()) || StringUtils.isNoneBlank(vo.getCorpId())
                || StringUtils.isNoneBlank(vo.getBlnctrAcc())
                || StringUtils.isNoneBlank(vo.getAccState())
                || StringUtils.isNoneBlank(vo.getAccType())
                || StringUtils.isNoneBlank(vo.getSubjectId())
                || StringUtils.isNoneBlank(vo.getSumType())
                || StringUtils.isNoneBlank(vo.getCurId())){
            list = queryOnlineLedgerResult(vo,resource,userId);//查询结果
        }else{
            list = queryOnlineLedgerResult(vo,resource,userId);//查询结果
        }
        if(!EmptyUtils.isEmptyList(list)) {
            if ("4".equals(vo.getSumType())) {
                for (BalanceQueryVo subjectBeans : list) {
                    for (BalanceQueryVo balBeans : subjectBeans.getSubBalBeans()) {
                        accountVo = new BalanceQueryVo();
                        if (StringUtils.isNotEmpty(balBeans.getSubjectName())) {
                            accountVo.setSubjectName(balBeans.getSubjectName());
                        }
                        accountVo.setBlnctrAcc(balBeans.getBlnctrAcc());
                        accountVo.setAccName(balBeans.getAccName());
                        accountVo.setBalanceName(dictService.get("balance.query.report", balBeans.getBalanceWay()));
                        accountVo.setFreezeMoney(balBeans.getFreezeMoney());
                        accountVo.setOnlineBalance(balBeans.getOnlineBalance());
                        accountVo.setCheckBalance(balBeans.getCheckBalance());
                        accountVo.setNoCheckBalance(balBeans.getNoCheckBalance());
                        accountVo.setTallyCnt(balBeans.getTallyCnt());
                        accountVo.setCheckCnt(balBeans.getCheckCnt());
                        res.add(accountVo);
                        map.add(BeanUtils.beanToMap(accountVo));
                    }
                    accountVo = new BalanceQueryVo();
                    accountVo.setAccName(subjectBeans.getSubjectName());
                    accountVo.setBalanceName(dictService.get("balance.query.report", 0));//小计
                    accountVo.setFreezeMoney(subjectBeans.getFreezeMoney());
                    accountVo.setOnlineBalance(subjectBeans.getOnlineBalance());
                    accountVo.setCheckBalance(subjectBeans.getCheckBalance());
                    accountVo.setNoCheckBalance(subjectBeans.getNoCheckBalance());
                    accountVo.setTallyCnt(subjectBeans.getTallyCnt());
                    accountVo.setCheckCnt(subjectBeans.getCheckCnt());
                    res.add(accountVo);
                    map.add(BeanUtils.beanToMap(accountVo));
                }
            } else {
                for (BalanceQueryVo netBeans : list) {
                    for (BalanceQueryVo corpBeans : netBeans.getSubBalBeans()) {
                        for (BalanceQueryVo subjectBeans : corpBeans.getSubBalBeans()) {
                            for (BalanceQueryVo balBeans : subjectBeans.getSubBalBeans()) {//遍历实体
                                accountVo = new BalanceQueryVo();
                                if ("1".equals(vo.getSumType())) {
                                    accountVo.setCorpName(balBeans.getCorpName());
                                    accountVo.setSubjectName(balBeans.getSubjectName());
                                } else if ("2".equals(vo.getSumType())) {
                                    accountVo.setCorpName(balBeans.getSubjectName());
                                    accountVo.setSubjectName(balBeans.getCorpName());
                                } else if ("3".equals(vo.getSumType())) {
                                    accountVo.setCorpName(balBeans.getSubjectName());
                                    accountVo.setSubjectName(balBeans.getCorpName());
                                }
                                accountVo.setBlnctrAcc(balBeans.getBlnctrAcc());
                                accountVo.setAccName(balBeans.getAccName());
                                accountVo.setBalanceName(dictService.get("balance.query.report", balBeans.getBalanceWay()));
                                accountVo.setFreezeMoney(balBeans.getFreezeMoney());
                                accountVo.setOnlineBalance(balBeans.getOnlineBalance());
                                accountVo.setCheckBalance(balBeans.getCheckBalance());
                                accountVo.setNoCheckBalance(balBeans.getNoCheckBalance());
                                accountVo.setTallyCnt(balBeans.getTallyCnt());
                                accountVo.setCheckCnt(balBeans.getCheckCnt());
                                res.add(accountVo);
                                map.add(BeanUtils.beanToMap(accountVo));
                            }
                            accountVo = new BalanceQueryVo();
                            accountVo.setAccName(subjectBeans.getSubjectName());
                            accountVo.setBalanceName(dictService.get("balance.query.report", 0));//小计
                            accountVo.setFreezeMoney(subjectBeans.getFreezeMoney());
                            accountVo.setOnlineBalance(subjectBeans.getOnlineBalance());
                            accountVo.setCheckBalance(subjectBeans.getCheckBalance());
                            accountVo.setNoCheckBalance(subjectBeans.getNoCheckBalance());
                            accountVo.setTallyCnt(subjectBeans.getTallyCnt());
                            accountVo.setCheckCnt(subjectBeans.getCheckCnt());
                            res.add(accountVo);
                            map.add(BeanUtils.beanToMap(accountVo));
                        }
                        accountVo = new BalanceQueryVo();
                        if ("3".equals(vo.getSumType())) {
                            accountVo.setAccName(corpBeans.getTypeName());
                            accountVo.setBalanceName(dictService.get("balance.query.report", 0));//银行小计
                        } else {
                            accountVo.setAccName(corpBeans.getCorpName());
                            accountVo.setBalanceName(dictService.get("balance.query.report", 2));//合计
                        }
                        accountVo.setFreezeMoney(corpBeans.getFreezeMoney());
                        accountVo.setOnlineBalance(corpBeans.getOnlineBalance());
                        accountVo.setCheckBalance(corpBeans.getCheckBalance());
                        accountVo.setNoCheckBalance(corpBeans.getNoCheckBalance());
                        accountVo.setTallyCnt(corpBeans.getTallyCnt());
                        accountVo.setCheckCnt(corpBeans.getCheckCnt());
                        res.add(accountVo);
                        map.add(BeanUtils.beanToMap(accountVo));
                    }
                    accountVo = new BalanceQueryVo();
                    if (!"3".equals(vo.getSumType())) {
                        accountVo.setAccName(netBeans.getNetName());
                        accountVo.setBalanceName(dictService.get("balance.query.report", 3));//总计
                        accountVo.setFreezeMoney(netBeans.getFreezeMoney());
                        accountVo.setOnlineBalance(netBeans.getOnlineBalance());
                        accountVo.setCheckBalance(netBeans.getCheckBalance());
                        accountVo.setNoCheckBalance(netBeans.getNoCheckBalance());
                        accountVo.setTallyCnt(netBeans.getTallyCnt());
                        accountVo.setCheckCnt(netBeans.getCheckCnt());
                        res.add(accountVo);
                        map.add(BeanUtils.beanToMap(accountVo));
                    }
                }
            }
        }
        return map;
    }
    public List<FcAccDTO> queryListAccount(String netId, String corpId){
        return innerAccountService.listAccountByParams(null, netId, corpId, null);
    }
    public String formatNull(String str) {
        if (str == null || "null".equals(str))
            return "";
        else
            return str;
    }
    
    /**
     * 
     * 功能介绍 显示页面表头。<br>
     * <b>日期：</b>2018年1月16日<br>
     * @version 1.0
     * @author zhengxn 
     */
    public CorpDTO getNetId(String netId){
        return corpService.get(netId);
    }
    @Override
    public BaseDao<BalanceQueryVo, String> getEntityDao() {
        // TODO Auto-generated method stub
        return null;
    }
}
