package com.bytter.report.domain.cms.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.dto.GuaContractQueryDTO;
import com.bytter.report.api.constant.ResourceConstant;
import com.bytter.report.domain.cms.dao.CmsProvideQueryDao;
import com.bytter.report.domain.cms.vo.ProvideCapitalizeVO;
import com.bytter.report.domain.cms.vo.ProvideCorpTreeVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
/**
 * <br>
 * <b>功能：</b> 借款查询报表服务 <br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2020/10/20 0020 <br>
 */
@Service("cmsProvideQueryService")
@Transactional
public class CmsProvideQueryService extends BaseService {
    @Autowired
    private CmsProvideQueryDao queryDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IGuaContractOccupServie guaContractOccupServie;
    @DubboReference
	private IMultiLangService multiLangServiceI;

    @Override
    public BaseDao getEntityDao() {
        return queryDao;
    }
    /**
     * <br>
     * <b>功能：</b> set转String <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/10/26 0026 <br>
     */
    private String setToString(String joinFlag, Set<String> set) {
        StringBuffer result = new StringBuffer("");
        if (null == set || set.size() == 0) {
            return result.toString();
        }
        for (String value : set) {
            result.append(value).append(joinFlag);
        }
        if (result.lastIndexOf(joinFlag) != -1) {
            return result.substring(0, result.lastIndexOf(joinFlag));
        }
        return result.toString();
    }
    /**
     * <br>
     * <b>功能：</b> 查询银行借款融资情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/6 0006 <br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryLoanCapitalize(String curId, ICurrentUser user, Date calcDate, List<String> corpIds, Date startRepayDate, Date endRepayDate, Date startBorrowDate, Date endReBorrowDate, String bankTypeId, String plClass, List<String> plType ) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_CAPITALIZE_QUERY, user.getId());//单位权限
        List<ProvideCapitalizeVO> capitalizeVOs = queryDao.queryLoanCapitalize(corpAuth, curId, calcDate, corpIds, startRepayDate, endRepayDate, startBorrowDate, endReBorrowDate, bankTypeId, plClass, plType);//查询借款融资情况数据
        DataTablesOutput output = new DataTablesOutput();
        output.setData(queryLoanGuaInfo(capitalizeVOs, calcDate));
        return output;
    }
    /**
     * <br>
     * <b>功能：</b> 查询担保、还款等信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/9 0009 <br>
     */
    public List<ProvideCapitalizeVO> queryLoanGuaInfo(List<ProvideCapitalizeVO> capitalizeVOs, Date calcDate) {
        //查询截止日期的借款还款情况
        if (EmptyUtils.isEmptyList(capitalizeVOs)) {
            capitalizeVOs = new ArrayList<>();
        }
        List<String> infoIds = new ArrayList<>();
        for (ProvideCapitalizeVO capitalizeVO : capitalizeVOs) {
            if (!infoIds.contains(capitalizeVO.getInfoId())) {
                infoIds.add(capitalizeVO.getInfoId());
            }
            BigDecimal bailRate = capitalizeVO.getBailRate();//保证金比例
            if(null == bailRate){
                bailRate = BigDecimal.ZERO;
            }
            capitalizeVO.setBailMoney(capitalizeVO.getSendMoney().multiply(bailRate));//保证金金额
            capitalizeVO.setConverMoney(capitalizeVO.getLeftMoney().subtract(capitalizeVO.getBailMoney()));//贷款余额=借款余额-保证金金额
        }
        queryGuaWayInfo(capitalizeVOs, infoIds);
        //汇总合计
        return sumCapitalizeInfo(capitalizeVOs);
    }
    public void moneyConversion(List<ProvideCapitalizeVO> capitalizeVOs) {
        for (ProvideCapitalizeVO capitalizeVO : capitalizeVOs) {
            capitalizeVO.setLeftMoney(toTenThousandYuan(capitalizeVO.getLeftMoney()));
            capitalizeVO.setBailMoney(toTenThousandYuan(capitalizeVO.getBailMoney()));
            capitalizeVO.setConverMoney(toTenThousandYuan(capitalizeVO.getConverMoney()));
            capitalizeVO.setGuaMoney(toTenThousandYuan(capitalizeVO.getGuaMoney()));
        }
    }
    /**
     * <br>
     * <b>功能：</b> 按照单位级别汇总数据 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/14 0014 <br>
     */
    public List<ProvideCapitalizeVO> sumCapitalizeInfo(List<ProvideCapitalizeVO> capitalizeVOs) {
        String subtotal = multiLangServiceI.getLang("table.datatable.subtotal");//小计
        String total = multiLangServiceI.getLang("table.datatable.total");//合计
        //先显示本单位的合计，再显示上级单位的合计，最后显示一级网点的合计
        Map<String, List<ProvideCapitalizeVO>> corpVoMap = new HashMap<>();//单位借款的明细
        Map<String, String> infoMap = new HashMap<>();//借款申请合同的集合
        for (ProvideCapitalizeVO capitalizeVO : capitalizeVOs) {
            String infoId = capitalizeVO.getInfoId();//借款申请合同
            String corpId = capitalizeVO.getCorpId();//单位
            List<ProvideCapitalizeVO> corpVOs = corpVoMap.get(corpId);
            if (null == corpVOs) {
                corpVOs = new ArrayList<>();
                corpVoMap.put(corpId, corpVOs);
            }
            corpVOs.add(capitalizeVO);
            //infoMap.put(infoId, infoId);
        }
        List<ProvideCapitalizeVO> resultList = new ArrayList<>();//结果集
        List<CorpDTO> corpDTOS = corpService.listEffectiveCorp();//有效单位
        List<CorpDTO> corpTree = corpService.listCorpTree(corpDTOS);//单位树
        List<ProvideCorpTreeVO> corpTreeVOS = turn2CorpTree(corpTree);
        for (ProvideCorpTreeVO corpTreeVO : corpTreeVOS) {
            showCorpCapitalize(resultList, corpTreeVO, corpVoMap, infoMap, subtotal, total);//循环显示单位的明细
        }
        moneyConversion(resultList);//金额折算
        return resultList;
    }
    /**
    * <br>
    * <b>功能：</b> 显示单位下明细数据及合计 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/12/17 0017 <br>
    */
    public ProvideCapitalizeVO showCorpCapitalize(List<ProvideCapitalizeVO> resultList, ProvideCorpTreeVO corpTreeVO,
                                                  Map<String, List<ProvideCapitalizeVO>> corpVoMap,
                                                  Map<String, String> infoMap, String subtotal, String total) {
        List<ProvideCorpTreeVO> childs = corpTreeVO.getChilds();//子单位
        List<ProvideCapitalizeVO> corpSumList = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(childs) && childs.size() > 1) {
            for (ProvideCorpTreeVO childCorp : childs) {
                ProvideCapitalizeVO childSum = showCorpCapitalize(resultList, childCorp, corpVoMap, infoMap, subtotal, total);//递归显示子单位的明细
                corpSumList.add(childSum);
            }
        }
        ProvideCapitalizeVO corpSum = showCorpListAndSum(resultList, corpTreeVO, corpVoMap, infoMap, subtotal);//单位小计
        //显示本单位及下级单位的合计
        if (EmptyUtils.isNotEmpty(childs) && childs.size() > 1) {
            ProvideCapitalizeVO parentSum = new ProvideCapitalizeVO();
            parentSum.setCorpName(corpTreeVO.getCorpName() + total + "：");
            for (ProvideCapitalizeVO childSum : corpSumList) {
                calcSumCapitalize(parentSum, childSum);
            }
            calcSumCapitalize(parentSum, corpSum);//下级单位合计加上本单位的小计
            if(parentSum.getLeftMoney().compareTo(BigDecimal.ZERO) > 0) {
                resultList.add(parentSum);
            }
            return parentSum;
        }
        return corpSum;
    }
    /**
    * <br>
    * <b>功能：</b> 统计单位小计数 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/12/17 0017 <br>
    */
    public ProvideCapitalizeVO showCorpListAndSum(List<ProvideCapitalizeVO> resultList, ProvideCorpTreeVO corpTreeVO, Map<String,
            List<ProvideCapitalizeVO>> corpVoMap, Map<String, String> infoMap, String subtotal) {
        List<ProvideCapitalizeVO> corpVOs = corpVoMap.get(corpTreeVO.getCorpId());//本单位明细数据
        if(!EmptyUtils.isNotEmpty(corpVOs)){
            corpVOs = new ArrayList<>();
        }
        resultList.addAll(corpVOs);
        ProvideCapitalizeVO corpSum = new ProvideCapitalizeVO();
        corpSum.setCorpName(corpTreeVO.getCorpName() + subtotal + "：");
        for (ProvideCapitalizeVO capitalizeVO : corpVOs) {
            String infoId = capitalizeVO.getInfoId();//借款申请合同
            corpSum.setLeftMoney(corpSum.getLeftMoney().add(capitalizeVO.getLeftMoney()));
            corpSum.setBailMoney(corpSum.getBailMoney().add(capitalizeVO.getBailMoney()));
            corpSum.setConverMoney(corpSum.getConverMoney().add(capitalizeVO.getConverMoney()));
            //同一笔借款申请合同只统计一个担保额度
            if (!infoMap.containsKey(infoId)) {
                corpSum.setGuaMoney(corpSum.getGuaMoney().add(capitalizeVO.getGuaMoney()));
            }
            infoMap.put(infoId, infoId);
        }
        if(corpSum.getLeftMoney().compareTo(BigDecimal.ZERO) > 0) {
            resultList.add(corpSum);
        }
        return corpSum;
    }
    /**
    * <br>
    * <b>功能：</b> 计算汇总金额 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/12/17 0017 <br>
    */
    public void calcSumCapitalize(ProvideCapitalizeVO parentSum, ProvideCapitalizeVO childSum) {
        parentSum.setLeftMoney(parentSum.getLeftMoney().add(childSum.getLeftMoney()));
        parentSum.setBailMoney(parentSum.getBailMoney().add(childSum.getBailMoney()));
        parentSum.setConverMoney(parentSum.getConverMoney().add(childSum.getConverMoney()));
        parentSum.setGuaMoney(parentSum.getGuaMoney().add(childSum.getGuaMoney()));
    }
    /**
    * <br>
    * <b>功能：</b> 按单位树汇总 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/12/17 0017 <br>
    */
    public List<ProvideCorpTreeVO> turn2CorpTree(List<CorpDTO> corpDTOS) {
        List<ProvideCorpTreeVO> list = new ArrayList<>();
        Map<String, ProvideCorpTreeVO> treeVOMap = new HashMap();
        for (CorpDTO corp : corpDTOS) {
            String corpId = corp.getId();//单位ID
            String parentId = corp.getParentId();//父ID
            ProvideCorpTreeVO treeVO = new ProvideCorpTreeVO();
            treeVO.setCorpId(corpId);
            treeVO.setCorpName(corp.getName());
            if (!EmptyUtils.isEmpty(treeVOMap.get(parentId))) {
                ProvideCorpTreeVO parentVO = treeVOMap.get(parentId);
                parentVO.getChilds().add(treeVO);
                treeVO.setParentVO(parentVO);
            } else {
                list.add(treeVO);
            }
            treeVOMap.put(corp.getId(), treeVO);
        }
        return list;
    }
    /**
     * <br>
     * <b>功能：</b> 查询担保信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/14 0014 <br>
     */
    public void queryGuaWayInfo(List<ProvideCapitalizeVO> capitalizeVOs, List<String> infoIds) {
        //查询借款直接挂接的担保方式
        Map<String, GuaContractQueryDTO> loanGuaWayNameMap = getLoanGuaWayName(infoIds);
        //获取所有授信挂接被担保合同担保方式
        Map<String, GuaContractQueryDTO> RatGuaWayNameMap = getRatGuaWayName(infoIds);
        if (null != loanGuaWayNameMap) {
            for (ProvideCapitalizeVO capitalizeVO : capitalizeVOs) {
                //设置担保方式
                GuaContractQueryDTO guaDTO = null;
                if (CommonConstant.YES.equals(capitalizeVO.getHangGuarantee())) {
                    guaDTO = loanGuaWayNameMap.get(capitalizeVO.getInfoId());
                } else {
                    guaDTO = RatGuaWayNameMap.get(capitalizeVO.getInfoId());
                }
                if (null != guaDTO) {
                    capitalizeVO.setContractCode(guaDTO.getContractCode());//担保合同
                    capitalizeVO.setGuaMoney(guaDTO.getGuaMoney());//担保额度
                    //capitalizeVO.setRmk(guaDTO.getGuaRmk());//担保备注
                    capitalizeVO.setStartDate(guaDTO.getStartDate());//担保起始日
                    capitalizeVO.setEndDate(guaDTO.getEndDate());//担保截止日
                    capitalizeVO.setGuaWay(guaDTO.getGuaWayName());
                }
            }
        }
    }
    /**
     * 获取借款业务数据对应的担保方式
     *
     * @param infoIds
     * @return
     */
    public Map<String, GuaContractQueryDTO> getLoanGuaWayName(List<String> infoIds) {
        Map<String, GuaContractQueryDTO> LcGuaWayMap = new HashMap<>();
        if (ListUtils.isNotNullOrEmpty(infoIds)) {
            List<GuaContractQueryDTO> guaContractQueryDTOS = guaContractOccupServie.listGuaWayName(infoIds, "provide");
            return turnToGuaInfoMap(guaContractQueryDTOS);
        }
        return LcGuaWayMap;
    }
    /**
    * <br>
    * <b>功能：</b> 担保信息汇总分组 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2020/12/31 0031 <br>
    */
    public Map<String, GuaContractQueryDTO> turnToGuaInfoMap(List<GuaContractQueryDTO> guaContractQueryDTOS) {
        Map<String, GuaContractQueryDTO> guaWayMap = new HashMap<>();
        if(null == guaContractQueryDTOS || EmptyUtils.isEmptyList(guaContractQueryDTOS)) {
            return guaWayMap;
        }
        for (GuaContractQueryDTO guaContractQueryDTO : guaContractQueryDTOS) {
            String businessId = guaContractQueryDTO.getBusinessId();
            BigDecimal guaMoney = guaContractQueryDTO.getGuaMoney();
            String contractCode = guaContractQueryDTO.getContractCode();
            String guaRmk = guaContractQueryDTO.getGuaRmk();
            if (EmptyUtils.isEmpty(guaRmk)) {
                guaRmk = "";
                guaContractQueryDTO.setGuaRmk(guaRmk);
            }
            Date startDate = guaContractQueryDTO.getStartDate();
            Date endDate = guaContractQueryDTO.getEndDate();
            String guaWay = guaContractQueryDTO.getGuaWayName();
            GuaContractQueryDTO queryDTO = guaWayMap.get(businessId);
            if (null == queryDTO) {
                guaWayMap.put(businessId, guaContractQueryDTO);
            } else {
				queryDTO.setGuaMoney(queryDTO.getGuaMoney().max(guaMoney));//担保最大金额
                //queryDTO.setGuaMoney(queryDTO.getGuaMoney().add(guaMoney));//担保金额合计
                if (!EmptyUtils.isEmpty(guaRmk)) {
                    if (EmptyUtils.isNotEmpty(queryDTO.getGuaRmk()) && -1 != queryDTO.getGuaRmk().indexOf(guaRmk)) {
                    } else {
                        queryDTO.setGuaRmk(queryDTO.getGuaRmk() + "," + guaRmk);//担保备注
                    }
                }
                if (EmptyUtils.isNotEmpty(queryDTO.getContractCode()) && -1 != queryDTO.getContractCode().indexOf(contractCode)) {
                } else {
                    queryDTO.setContractCode(queryDTO.getContractCode() + "," + contractCode);//担保合同
                }
                if (EmptyUtils.isNotEmpty(queryDTO.getGuaWayName()) && -1 != queryDTO.getGuaWayName().indexOf(guaWay)) {
                } else {
                    queryDTO.setGuaWayName(queryDTO.getGuaWayName() + "," + guaWay);//担保方式
                }
                if (!EmptyUtils.isEmpty(queryDTO.getStartDate())) {
                    if (DateUtils.dayDiffs(queryDTO.getStartDate(), startDate) >= 0) {
                        queryDTO.setStartDate(startDate);
                    }
                } else {
                    queryDTO.setStartDate(startDate);
                }
                if (!EmptyUtils.isEmpty(queryDTO.getEndDate())) {
                    if (DateUtils.dayDiffs(queryDTO.getEndDate(), endDate) < 0) {
                        queryDTO.setEndDate(endDate);
                    }
                } else {
                    queryDTO.setEndDate(endDate);
                }
            }
        }
        return guaWayMap;
    }
    /**
     * 根据借款合同ID获取授信业务数据对应的担保方式
     *
     * @param infoIds
     * @return
     */
    public Map<String, GuaContractQueryDTO> getRatGuaWayName(List<String> infoIds) {
        Map<String, GuaContractQueryDTO> ratGuaWayMap = new HashMap<>();
        if (ListUtils.isNotNullOrEmpty(infoIds)) {
            List<GuaContractQueryDTO> guaContractQueryDTOS = guaContractOccupServie.listGuaWayName(infoIds, "cms");
            return turnToGuaInfoMap(guaContractQueryDTOS);
        }
        return ratGuaWayMap;
    }
    /**
     * 转为万元
     *
     * @param money 金额
     * @return 万元 金额
     */
    public BigDecimal toTenThousandYuan(BigDecimal money) {
        if (money != null) {
            return money.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
