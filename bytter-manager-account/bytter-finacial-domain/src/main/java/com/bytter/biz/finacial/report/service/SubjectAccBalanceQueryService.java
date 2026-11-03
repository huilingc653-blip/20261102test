package com.bytter.biz.finacial.report.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.finacial.report.dao.SubjectAccBalanceQueryDao;
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
import java.util.stream.Collectors;

@Service("subjectAccBalanceQueryService")
public class SubjectAccBalanceQueryService extends BaseService<BalanceQueryVo,String> {
    @Autowired
    private SubjectAccBalanceQueryDao subjectAccBalanceQueryDao;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyServiceImpl;
    @DubboReference(check = false)
	private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IDictService dictService;
    @DubboReference(check = false)
	private IFcSubjectService iFcSubjectService;

    public List<BalanceQueryVo> querySubjectAccResult(BalanceQueryVo vo, String resource, String userCode) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, userCode);
        if (EmptyUtils.isBlank(vo.getCurId())) {//获取币别01，人民币
            //默认为本位币
            BtCurrencyDTO currency = btCurrencyServiceImpl.getNatualCurrency();
            vo.setCurId(currency.getId());
        }
        if (EmptyUtils.isBlank(vo.getSumType())) {
            vo.setSumType("1");
        }
        List<BalanceQueryVo> list = new ArrayList<BalanceQueryVo>();
        if ("5".equals(vo.getSumType()) || "6".equals(vo.getSumType())) {//比较维度
            list = subjectAccBalanceQueryDao.queryBalanceBySubject(vo, userCode, corpAuth, "true");//查询结果
            if (!"6".equals(vo.getSumType())){
                list = groupSubject(list,vo.getSumType());
            }else{
                if(list !=null){
                    for (BalanceQueryVo res : list) {
                        BigDecimal balance = res.getDebitMoney().subtract(res.getCreditMoney()).multiply(new BigDecimal(res.getBalanceWay()));
                        res.setBalance(res.getYesterBalance().add(balance));
                    }
                }

            }
            // 字段赋值
            injectValues(list);
            return list;
        } else {
            list = subjectAccBalanceQueryDao.queryResult(vo, userCode, corpAuth, "true");//查询结果
        }
        // 字段赋值
        injectValues(list);
        // 根据维度将数据分组
        list = groupSubject(list,vo.getSumType());
        if (list == null || list.size() == 0) {
            return null;
        }
        return setSubjectBalanceBean(list, vo.getSumType(), vo);
    }

    private void injectValues(List<BalanceQueryVo> list) {
        if (ObjectUtil.isNotEmpty(list)) {
            // 借贷方向
            Map<String, DictDTO> dictMap = dictService.listMap("balance.query.report");
            // 币种
            List<String> curIdList = list.stream().map(BalanceQueryVo::getCurId).distinct().collect(Collectors.toList());
            Map<String, BtCurrencyDTO> currencyMap = btCurrencyServiceImpl.getCurrencyByIds(curIdList);
            // 网点
            List<String> netIdList = list.stream().map(BalanceQueryVo::getNetId).distinct().collect(Collectors.toList());
            Map<String, String> netNameMap = corpService.getNameByIds(netIdList);
            list.forEach(item -> {
                Optional.ofNullable(item.getBalanceWay()).flatMap(balanceWay -> Optional.ofNullable(dictMap.get(String.valueOf(balanceWay))))
                        .ifPresent(dict -> item.setBalanceName(dict.getName()));
                Optional.ofNullable(item.getCurId()).flatMap(curId -> Optional.ofNullable(currencyMap.get(curId)))
                        .ifPresent(currency -> item.setCurName(currency.getCurName()));
                Optional.ofNullable(item.getNetId()).flatMap(netId -> Optional.ofNullable(netNameMap.get(netId)))
                        .ifPresent(item::setNetName);
            });
        }
    }

    /**
     * <br>
     * <b>功能：</b>按网点设置账户科目实时余额<br>
     */
    private List<BalanceQueryVo> setSubjectBalanceBean(List<BalanceQueryVo> listFcAcc, String sumType, BalanceQueryVo bean) {
        //网点集合map
        Map<String, String> netMap = new HashMap<String, String>();
        //单位集合
        Map<String, BalanceQueryVo> subMap = new HashMap<String, BalanceQueryVo>();
        //科目余额实体
        Map<String, Map<String, BalanceQueryVo>> netAccSubMap = new HashMap<String, Map<String, BalanceQueryVo>>();
        //网点科目余额集合
        List<BalanceQueryVo> netBalanceBeans = new ArrayList<BalanceQueryVo>();
        String curKey = "";//币别key
        String type = "";
        Map<String, CorpDTO> corpMap = new HashMap<String, CorpDTO>();
        if (EmptyUtils.isBlank(sumType)) {
            sumType = "1";
        }
        //1：按单位+科目，2：按科目+单位，4：按科目+账号，5：按科目统计，6：按科目明细统计
        if("2".equals(sumType) || "4".equals(sumType)){
            return groupBySumType(listFcAcc);
        }
        for (BalanceQueryVo balanceQuery : listFcAcc) {
            String netCode = balanceQuery.getNetId(); //balanceQuery.getParentId()==null?balanceQuery.getNetId():balanceQuery.getParentId();//获取dao查询结果的网点、单位、科目、币别Id
            String corpCode = balanceQuery.getCorpId();
            String corpName = balanceQuery.getCorpName();
            String subjectCode = balanceQuery.getSubjectId();
            String subjectName = balanceQuery.getSubjectName();
            String curCode = balanceQuery.getCurId();
            String accName = balanceQuery.getAccName();//账号名称
            if ("1".equals(sumType)) {//判断维度
                curKey = netCode + "_" + corpCode + "_" + curCode;//币别key
                type = netCode + "_" + corpCode;//类型为网点+单位,大分组
            }
            Map<String, BalanceQueryVo> accSubMap = netAccSubMap.get(netCode);//实体中查找key为netCode的数据
            String netName = netMap.get(netCode);//设置网点下单位集合，netCode通过遍历获得的
            if (EmptyUtils.isBlank(netName)) {//netMap（网点集合）为空
                netMap.put(netCode, balanceQuery.getNetName());//在netMap中添加网点
                accSubMap = new HashMap<String, BalanceQueryVo>();
                netAccSubMap.put(netCode, accSubMap);//科目余额实体中添加网点
            }
            BalanceQueryVo accSubBalBean = accSubMap.get(type);//设置单位下科目集合 type：维度中的类型
            if (accSubBalBean == null) { //accSubBalBean第二层--单位层
                accSubBalBean = new BalanceQueryVo();
                accSubBalBean.setCorpId(corpCode); //设置vo中单位的值
                accSubBalBean.setCorpName(corpName);
                accSubBalBean.setAccName(accName);
                accSubBalBean.setSubBalBeans(new ArrayList<BalanceQueryVo>());
                accSubMap.put(type, accSubBalBean);//添加单位科目集合中的值
            }
            if ("1".equals(sumType)) {//判断维度
                setCorpBalRelation(netCode, corpCode, accSubMap, corpMap, accSubBalBean);
            }
            BalanceQueryVo subBean = subMap.get(curKey);//设置科目下账户集合  subMap保存分组键对应的整体对象（内置集合）
            if (subBean == null) {//subBean第三层
                subBean = new BalanceQueryVo();
                subBean.setSubBalBeans(new ArrayList<BalanceQueryVo>());//第三次 科目集合
                subBean.setSubjectName(subjectName);
                subMap.put(curKey, subBean);
                accSubBalBean.getSubBalBeans().add(subBean);
            }
            BalanceQueryVo balBean = setBalanceBean(balanceQuery, sumType);//参数balanceQuery是dao数据遍历结果
            subBean.getSubBalBeans().add(balBean);//单位+科目明细
            if (subBean.getSubBalBeans().size() >= 1) {
                //设置小计金额
                setSumBalance(subBean,balBean, sumType);
            }
            if ("1".equals(sumType)) {
                subBean.setSubjectId("测试");
            }
            setSuperCorpBalBean(netCode, corpCode, accSubMap, corpMap, balBean);//计算上级单位总金额
        }
        for (Entry<String, Map<String, BalanceQueryVo>> entry2 : netAccSubMap.entrySet()) {//遍历科目余额实体
            String netCode = entry2.getKey();
            Map<String, BalanceQueryVo> tempMap = entry2.getValue();
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
            CorpDTO corp= corpService.get(netCode);
            String parentId = null;
            if(StringUtils.isEmpty(corp.getParentId())){
                parentId = netCode;
            }else {
                parentId = corp.getParentId();
            }
            netBean.setParentId(parentId);
            netBean.setNetId(netCode);
            netBean.setNetName(netMap.get(netCode));
            setNetSumSubBalance(netBean, null, null);//设置合计金额
            netBalanceBeans.add(netBean);//网点科目余额集合
        }
        setSumVo(netBalanceBeans);
        return sumTopNet(netBalanceBeans);
    }
    /**
     * 按照维度统计
     * @param listFcAcc
     * @return
     */
    public List<BalanceQueryVo> groupBySumType(List<BalanceQueryVo> listFcAcc){
        //网点科目余额集合
        List<BalanceQueryVo> netBalanceBeans = new ArrayList<BalanceQueryVo>();
        String subjectName = null;
        BalanceQueryVo subBeans = null;
        List<BalanceQueryVo> beans = null;
        for (BalanceQueryVo balanceQuery : listFcAcc) {
            if(subjectName == null){
                subjectName = balanceQuery.getSubjectName();
                subBeans = new BalanceQueryVo();
                subBeans.setSubjectName(subjectName);
                beans = new ArrayList<>();
            }
            if(subjectName.equals(balanceQuery.getSubjectName())){
                beans.add(balanceQuery);
                subBeans.setYesterBalance(subBeans.getYesterBalance().add(balanceQuery.getYesterBalance()));
                subBeans.setDebitMoney(subBeans.getDebitMoney().add(balanceQuery.getDebitMoney()));
                subBeans.setCreditMoney(subBeans.getCreditMoney().add(balanceQuery.getCreditMoney()));
                subBeans.setBalance(subBeans.getBalance().add(balanceQuery.getBalance()));
            }else {
                subBeans.setSubBalBeans(beans);
                netBalanceBeans.add(subBeans);
                subjectName = balanceQuery.getSubjectName();
                subBeans = new BalanceQueryVo();
                subBeans.setSubjectName(subjectName);
                beans = new ArrayList<>();
                beans.add(balanceQuery);
                subBeans.setYesterBalance(subBeans.getYesterBalance().add(balanceQuery.getYesterBalance()));
                subBeans.setDebitMoney(subBeans.getDebitMoney().add(balanceQuery.getDebitMoney()));
                subBeans.setCreditMoney(subBeans.getCreditMoney().add(balanceQuery.getCreditMoney()));
                subBeans.setBalance(subBeans.getBalance().add(balanceQuery.getBalance()));
            }
        }
        subBeans.setSubBalBeans(beans);
        netBalanceBeans.add(subBeans);
        return netBalanceBeans;
    }
    /**
     * 按顶级网点合计
     * @param listFcAcc
     * @return
     */
    public List<BalanceQueryVo> sumTopNet(List<BalanceQueryVo> listFcAcc){
        if(!EmptyUtils.isEmptyList(listFcAcc)) {
            Collections.sort(listFcAcc, new Comparator<BalanceQueryVo>() {
                @Override
                public int compare(BalanceQueryVo o1, BalanceQueryVo o2) {
                    return o2.getParentId().compareTo(o1.getParentId());
                }
            });
            Map<String, List<BalanceQueryVo>> nextNetMap = new HashMap<>();
            List<BalanceQueryVo> nextList = null;
            String parentId = null;
            for (BalanceQueryVo res : listFcAcc) {
                parentId = res.getParentId();
                CorpDTO corp = corpService.get(parentId);
                if (StringUtils.isNotEmpty(corp.getParentId())) {
                    parentId = corp.getParentId();
                }else {
                    if(listFcAcc.size()==1 && !res.getNetId().equals(corp.getParentId())){
                        parentId = res.getNetId();//按照单位过滤,只有一条数据没有上级信息,按自己汇总
                    }
                }
                if (nextNetMap.get(parentId) == null) {
                    nextList = new ArrayList<>();
                    nextList.add(res);
                    nextNetMap.put(parentId, nextList);
                } else {
                    List<BalanceQueryVo> list = nextNetMap.get(parentId);
                    list.add(res);
                }
            }
            BalanceQueryVo topNetVo = null;
            List<BalanceQueryVo> topList = new ArrayList<>();
            for (BalanceQueryVo res : listFcAcc) {
                List<BalanceQueryVo> mlist = nextNetMap.get(res.getNetId());
                if (!EmptyUtils.isEmptyList(mlist) && mlist.size() > 0) {
                    topNetVo = new BalanceQueryVo();
                    for (BalanceQueryVo m : mlist) {
                        if (m.getNetId().equals(res.getNetId())) {
                            String corpName = corpService.get(res.getNetId()).getName();
                            topNetVo.setCorpName(corpName);
                            topNetVo.setYesterBalance(m.getYesterBalance());
                            topNetVo.setDebitMoney(m.getDebitMoney());
                            topNetVo.setCreditMoney(m.getCreditMoney());
                            topNetVo.setBalance(m.getBalance());
                            topNetVo.setSubBalBeans(mlist);
                            topList.add(topNetVo);
                        }
                    }
                }
            }
            return topList;
        }else {
            return new ArrayList<>();
        }
    }
    public void setSumVo(List<BalanceQueryVo> resList){
        if(!EmptyUtils.isEmptyList(resList)) {
            Collections.sort(resList, new Comparator<BalanceQueryVo>() {
                @Override
                public int compare(BalanceQueryVo o1, BalanceQueryVo o2) {
                    return o2.getParentId().compareTo(o1.getParentId());
                }
            });
            Map<String, List<BalanceQueryVo>> nextNetMap = new HashMap<>();
            List<BalanceQueryVo> nextList = null;
            String parentId = null;
            for (BalanceQueryVo res : resList) {
                parentId = res.getParentId();
                if (!res.getNetId().equals(res.getParentId())) {
                    if (nextNetMap.get(parentId) == null) {
                        nextList = new ArrayList<>();
                        nextList.add(res);
                        nextNetMap.put(parentId, nextList);
                    } else {
                        List<BalanceQueryVo> list = nextNetMap.get(parentId);
                        list.add(res);
                    }
                }
            }
            for (BalanceQueryVo res : resList) {
                List<BalanceQueryVo> mlist = nextNetMap.get(res.getNetId());
                if (!EmptyUtils.isEmptyList(mlist) && mlist.size() > 0) {
                    for (BalanceQueryVo m : mlist) {
                        res.setYesterBalance(res.getYesterBalance().add(m.getYesterBalance()));
                        res.setDebitMoney(res.getDebitMoney().add(m.getDebitMoney()));
                        res.setCreditMoney(res.getCreditMoney().add(m.getCreditMoney()));
                        res.setBalance(res.getBalance().add(m.getBalance()));
                    }
                }
            }
        }
    }
    public List<BalanceQueryVo> groupSubject(List<BalanceQueryVo> listFcAcc,String sumType){
        if(!EmptyUtils.isEmptyList(listFcAcc)) {
            if (!"4".equals(sumType)) {//科目+账号不需要按照FatherId排序
                Collections.sort(listFcAcc, new Comparator<BalanceQueryVo>() {
                    @Override
                    public int compare(BalanceQueryVo o1, BalanceQueryVo o2) {
                        return o2.getFatherId().compareTo(o1.getFatherId());
                    }
                });
            }
            Map<String, List<BalanceQueryVo>> nextNetMap = new HashMap<>();
            List<BalanceQueryVo> resList = null;
            String fatherSubject = "I,A,L,B,E,G";
            for (BalanceQueryVo res : listFcAcc) {
                // BigDecimal balance = res.getDebitMoney().subtract(res.getCreditMoney()).multiply(new BigDecimal(res.getBalanceWay()));
                // res.setBalance(res.getYesterBalance().add(balance));
                String father = res.getFatherId() + "_" + res.getCorpId();
                if (!fatherSubject.contains(res.getFatherId())) {
                    if (nextNetMap.get(father) == null) {
                        resList = new ArrayList<>();
                        resList.add(res);
                        nextNetMap.put(father, resList);
                    } else {
                        List<BalanceQueryVo> list = nextNetMap.get(father);
                        list.add(res);
                    }
                }
            }
            // List<FcSubjectDTO> fatherSub = iFcSubjectService.findSubjectByFatherId();
            Map<String, String> sign = new HashMap<>();
            List<BalanceQueryVo> res = new ArrayList<>();
            BalanceQueryVo sumVo = null;
            for (BalanceQueryVo vo : listFcAcc) {
                String key = vo.getFatherId() + "_" + vo.getCorpId();
                if (StringUtils.isNotEmpty(sign.get(key))) {
                    continue;
                }
                List<BalanceQueryVo> map = nextNetMap.get(key);
                if (!EmptyUtils.isEmptyList(map) && map.size() > 0) {
                    for (BalanceQueryVo m : map) {
                        if (sumVo == null) {
                            sumVo = new BalanceQueryVo();
                            BeanUtils.copyProperties(m, sumVo, "yesterBalance", "debitMoney", "creditMoney", "balance");
                        }
                        sumVo.setYesterBalance(sumVo.getYesterBalance().add(m.getYesterBalance()));
                        sumVo.setDebitMoney(sumVo.getDebitMoney().add(m.getDebitMoney()));
                        sumVo.setCreditMoney(sumVo.getCreditMoney().add(m.getCreditMoney()));
                        sumVo.setBalance(sumVo.getBalance().add(m.getBalance()));
                    }
                    res.add(sumVo);
                    sign.put(key, "1");
                    sumVo = null;
                } else {
                    res.add(vo);
                }
            }
            if ("4".equals(sumType)) {
                res = listFcAcc;
            }
            for (BalanceQueryVo vo : res) {
                if (!fatherSubject.contains(vo.getFatherId())) {
                    FcSubjectDTO subjectDTO = iFcSubjectService.findSubjectById(vo.getFatherId());
                    if (null != subjectDTO) {
                        vo.setSubjectName(subjectDTO.getSubjectName());
                        if ("5".equals(sumType)) {
                            vo.setSubjectCode(subjectDTO.getSubjectCode());
                        }
                    }
                }
            }
            return res;
        }else {
            return new ArrayList<>();
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
            //计算总计金额
            accSubBalBean2.setYesterBalance(accSubBalBean2.getYesterBalance().add(balBean.getYesterBalance()));
            accSubBalBean2.setDebitMoney(accSubBalBean2.getDebitMoney().add(balBean.getDebitMoney()));
            accSubBalBean2.setCreditMoney(accSubBalBean2.getCreditMoney().add(balBean.getCreditMoney()));
            accSubBalBean2.setBalance(accSubBalBean2.getBalance().add(balBean.getBalance()));
        }
        setSuperCorpBalBean(netCode, superCorpCode, accSubMap, corpMap, balBean);
    }
    /**
     * <br>
     * <b>功能：</b>设置每个账户余额信息,并小计、合计<br>
     */
    private BalanceQueryVo setBalanceBean(BalanceQueryVo accBankBean, String sumType) {
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
            superAccSubBalBean = new BalanceQueryVo();
            superAccSubBalBean.setSubBalBeans(new ArrayList<BalanceQueryVo>());
            superAccSubBalBean.setCorpId(superCorpCode);
            superAccSubBalBean.setIsTotal("1");
            accSubMap.put(superKey, superAccSubBalBean);
        }
        String type = netCode + "_" + corpCode;
        Map<String, BalanceQueryVo> map = new HashMap<String, BalanceQueryVo>();
        for(BalanceQueryVo vo:superAccSubBalBean.getSubBalBeans()){
            map.put(vo.getNetId()+"_"+vo.getCorpId(), vo);
        }
        if (map.get(type)!=null) {
            superAccSubBalBean.getSubBalBeans().add(accSubBalBean);
        }
        accSubBalBean.setHasFather("1");//表示有上级节点
        setCorpBalRelation(netCode, superCorpCode, accSubMap, corpMap, superAccSubBalBean);
    }
    /**
     * <br>
     * <b>功能：</b>设置小计金额<br>
     */
    private void setSumBalance(BalanceQueryVo subBean, BalanceQueryVo balBean, String sumType) {//科目合计和这个方法没关系
        subBean.setIsSum("1");//小计
        subBean.setYesterBalance(subBean.getYesterBalance().add(balBean.getYesterBalance()));
        subBean.setDebitMoney(subBean.getDebitMoney().add(balBean.getDebitMoney()));
        subBean.setCreditMoney(subBean.getCreditMoney().add(balBean.getCreditMoney()));
        subBean.setBalance(subBean.getBalance().add(balBean.getBalance()));
        List<BalanceQueryVo> balBeans = subBean.getSubBalBeans();
        if ("1".equals(sumType)) {
            subBean.setSubjectName(balBeans.get(0).getSubjectName());
        } else if ("2".equals(sumType)) {
            subBean.setSubjectName(balBeans.get(0).getSubjectName());
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
            @Override
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
                @Override
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
                    @Override
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
                @Override
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
    private void setNetSumSubBalance(BalanceQueryVo accSubBalBean, BalanceQueryVo netBean, String sumType) {
        List<BalanceQueryVo> subBeans = accSubBalBean.getSubBalBeans();
        if(subBeans == null){
            return;
        }
        for (BalanceQueryVo balanceBean : subBeans) {
            accSubBalBean.setYesterBalance(accSubBalBean.getYesterBalance().add(balanceBean.getYesterBalance()));
            accSubBalBean.setDebitMoney(accSubBalBean.getDebitMoney().add(balanceBean.getDebitMoney()));
            accSubBalBean.setCreditMoney(accSubBalBean.getCreditMoney().add(balanceBean.getCreditMoney()));
            accSubBalBean.setBalance(accSubBalBean.getBalance().add(balanceBean.getBalance()));
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
            balanceBean.setYesterBalance(balanceBean.getYesterBalance().add(balanceBean.getYesterBalance()));
            balanceBean.setDebitMoney(balanceBean.getDebitMoney().add(balanceBean.getDebitMoney()));
            balanceBean.setCreditMoney(balanceBean.getCreditMoney().add(balanceBean.getCreditMoney()));
            balanceBean.setBalance(balanceBean.getBalance().add(balanceBean.getBalance()));
        }
    }
    /***
     * 功能介绍 导出。<br>
     * <b>日期：</b>2018年1月29日<br>
     * @version 1.0
     * @author zhengxn
     */
    public List<Map<String,Object>> excel(BalanceQueryVo vo, String resource, String userId){
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
            list = querySubjectAccResult(vo,resource,userId);//查询结果
        }else{
            list = querySubjectAccResult(vo,resource,userId);//查询结果
        }
        if(!EmptyUtils.isEmptyList(list)) {
            if (!"2".equals(vo.getSumType()) && !"4".equals(vo.getSumType()) && !"5".equals(vo.getSumType()) && !"6".equals(vo.getSumType())){
                for(BalanceQueryVo nets : list) {
                    for (BalanceQueryVo netBeans : nets.getSubBalBeans()) {
                        for (BalanceQueryVo corpBeans : netBeans.getSubBalBeans()) {
                            for (BalanceQueryVo subjectBeans : corpBeans.getSubBalBeans()) {
                                for (BalanceQueryVo balBeans : subjectBeans.getSubBalBeans()) {//遍历实体
                                    accountVo = new BalanceQueryVo();
                                    if ("1".equals(vo.getSumType())) {
                                        accountVo.setCorpName(balBeans.getCorpName());
                                        accountVo.setSubjectName(balBeans.getSubjectName());
                                    }
                                    accountVo.setCurName(balBeans.getCurName());
                                    accountVo.setBalanceName(balBeans.getBalanceName());
                                    accountVo.setYesterBalance(balBeans.getYesterBalance());
                                    accountVo.setDebitMoney(balBeans.getDebitMoney());
                                    accountVo.setCreditMoney(balBeans.getCreditMoney());
                                    accountVo.setBalance(balBeans.getBalance());
                                    res.add(accountVo);
                                    map.add(BeanUtils.beanToMap(accountVo));
                                }
                                if (!"1".equals(vo.getSumType())) {
                                    accountVo = new BalanceQueryVo();
                                    accountVo.setCurName(subjectBeans.getSubjectName());
                                    accountVo.setBalanceName(dictService.get("balance.query.report", 0));//小计
                                    accountVo.setYesterBalance(subjectBeans.getYesterBalance());
                                    accountVo.setDebitMoney(subjectBeans.getDebitMoney());
                                    accountVo.setCreditMoney(subjectBeans.getCreditMoney());
                                    accountVo.setBalance(subjectBeans.getBalance());
                                    res.add(accountVo);
                                    map.add(BeanUtils.beanToMap(accountVo));
                                }
                            }
                            accountVo = new BalanceQueryVo();
                            accountVo.setCurName(corpBeans.getCorpName());
                            accountVo.setBalanceName(dictService.get("balance.query.report", 2));//合计
                            accountVo.setYesterBalance(corpBeans.getYesterBalance());
                            accountVo.setDebitMoney(corpBeans.getDebitMoney());
                            accountVo.setCreditMoney(corpBeans.getCreditMoney());
                            accountVo.setBalance(corpBeans.getBalance());
                            res.add(accountVo);
                            map.add(BeanUtils.beanToMap(accountVo));
                        }
                        accountVo = new BalanceQueryVo();
                        accountVo.setCurName(netBeans.getNetName());
                        accountVo.setBalanceName(dictService.get("balance.query.report", 2));//合计
                        accountVo.setYesterBalance(netBeans.getYesterBalance());
                        accountVo.setDebitMoney(netBeans.getDebitMoney());
                        accountVo.setCreditMoney(netBeans.getCreditMoney());
                        accountVo.setBalance(netBeans.getBalance());
                        res.add(accountVo);
                        map.add(BeanUtils.beanToMap(accountVo));
                    }
                    accountVo = new BalanceQueryVo();
                    accountVo.setCurName(nets.getCorpName());
                    accountVo.setBalanceName(dictService.get("balance.query.report", 3));//总计
                    accountVo.setYesterBalance(nets.getYesterBalance());
                    accountVo.setDebitMoney(nets.getDebitMoney());
                    accountVo.setCreditMoney(nets.getCreditMoney());
                    accountVo.setBalance(nets.getBalance());
                    res.add(accountVo);
                    map.add(BeanUtils.beanToMap(accountVo));
                }
            }
            if("2".equals(vo.getSumType()) || "4".equals(vo.getSumType())){
                for (BalanceQueryVo subjectBeans : list) {
                    for (BalanceQueryVo balBeans : subjectBeans.getSubBalBeans()) {//遍历实体
                        accountVo = new BalanceQueryVo();
                        accountVo.setSubjectName(balBeans.getSubjectName());
                        if("4".equals(vo.getSumType())){
                            accountVo.setBlnctrAcc(balBeans.getBlnctrAcc());
                            accountVo.setAccName(balBeans.getAccName());
                        }else {
                            accountVo.setCorpName(balBeans.getCorpName());
                        }
                        accountVo.setCurName(balBeans.getCurName());
                        accountVo.setBalanceName(balBeans.getBalanceName());
                        accountVo.setYesterBalance(balBeans.getYesterBalance());
                        accountVo.setDebitMoney(balBeans.getDebitMoney());
                        accountVo.setCreditMoney(balBeans.getCreditMoney());
                        accountVo.setBalance(balBeans.getBalance());
                        res.add(accountVo);
                        map.add(BeanUtils.beanToMap(accountVo));
                    }
                    accountVo = new BalanceQueryVo();
                    accountVo.setCurName(subjectBeans.getSubjectName());
                    accountVo.setBalanceName(dictService.get("balance.query.report", 0));//小计
                    accountVo.setYesterBalance(subjectBeans.getYesterBalance());
                    accountVo.setDebitMoney(subjectBeans.getDebitMoney());
                    accountVo.setCreditMoney(subjectBeans.getCreditMoney());
                    accountVo.setBalance(subjectBeans.getBalance());
                    res.add(accountVo);
                    map.add(BeanUtils.beanToMap(accountVo));
                }
            }
            if("5".equals(vo.getSumType()) || "6".equals(vo.getSumType())){
                for (BalanceQueryVo subjectBeans : list) {
                    accountVo = new BalanceQueryVo();
                    accountVo.setSubjectName(subjectBeans.getSubjectCode()+"-"+subjectBeans.getSubjectName());
                    accountVo.setCurName(subjectBeans.getCurName());
                    accountVo.setBalanceName(subjectBeans.getBalanceName());
                    accountVo.setYesterBalance(subjectBeans.getYesterBalance());
                    accountVo.setDebitMoney(subjectBeans.getDebitMoney());
                    accountVo.setCreditMoney(subjectBeans.getCreditMoney());
                    accountVo.setBalance(subjectBeans.getBalance());
                    res.add(accountVo);
                    map.add(BeanUtils.beanToMap(accountVo));
                }
            }
        }
        return map;
    }
    public List<FcAccDTO> queryListAccount(String netId, String corpId){
        return	innerAccountService.listAccountByParams(null, netId, corpId, null);
    }
    public String formatNull(String str) {
        if (str == null || "null".equals(str)) {
            return "";
        } else {
            return str;
        }
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
