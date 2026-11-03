package com.bytter.report.domain.cms.service;


import com.bytter.report.domain.cms.dao.CmsLoanRepayDao;
import com.bytter.report.domain.cms.dao.CmsPrecativeLoanRepayDao;
import com.bytter.report.domain.cms.dao.CmsLoanInterestPaymentsDao;
import com.bytter.report.domain.cms.vo.CmsInterestPayVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.IDictService;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@SuppressWarnings("rawtypes")
@Service("CmsLoanInterestPaymentsService")
@Transactional
public class CmsLoanInterestPaymentsService extends BaseService {
    @Autowired CmsLoanInterestPaymentsDao paymentsDao;
    @Autowired
    private CmsLoanRepayDao cmsLoanRepayDao;
    @Autowired
    private CmsPrecativeLoanRepayDao cmsPrecativeLoanRepayDao;

    @DubboReference
    private IDictService dictService;

    public List<CmsInterestPayVO> getList(CmsInterestPayVO vo){
        Date startDate = DateUtils.parseDate(vo.getSearchStartDate());
        Date endDate = DateUtils.parseDate(vo.getSearchEndDate());

        List<CmsInterestPayVO> list = paymentsDao.getList(vo);//总的发放信息
        List<String> key = getKey(list);
        Map<String, List<CmsInterestPayVO>> res = getPlanList(list);//key-value
        
        List<CmsInterestPayVO> result = new ArrayList<CmsInterestPayVO>();//最后返回页面的集合

        Date endRepayDate = null;//分段结束日期前的还款日期

        //应付
        BigDecimal yfInterest = BigDecimal.ZERO;
        //实付
        BigDecimal sfInterest = BigDecimal.ZERO;
        //临时
        BigDecimal sfInter = BigDecimal.ZERO;
        BigDecimal yfInter = BigDecimal.ZERO;

        //起息日期
        Date repayDate = null;
        Date sendDate = null;
        Date upRepayDate = startDate;//上一笔预计还款日默认设置设置为查询开始日期

        BigDecimal yaer = new BigDecimal("360");
        BigDecimal day = null;

        for (String s : key) {
            List<CmsInterestPayVO> lis = res.get(s);
            if (lis == null) {
                break;//如果没有还款计划，在这里通过billcode把还款信息查出来
                //没有还款信息就是没还，有了就看还了没有，还了设置实付，没还设置应付
            }
            CmsInterestPayVO pvo = lis.get(lis.size() - 1);//还款计划集合的最后一笔
            endRepayDate = getRepayDate(lis, endDate);
            if (endRepayDate == null) {
                //没有维护还款计划的情况下
                if (lis.get(0).getPrediceRepayDate() == null) {
                    String sendBillCode = lis.get(0).getSendBillCode();
                    String cmsType = sendBillCode.substring(0, 2);
                    if (startDate.compareTo(lis.get(0).getEndDate()) == 1) {//查询开始日期比最后还款日大
                        continue;
                    }
                    int cont5 = startDate.compareTo(lis.get(0).getSendDate());//开始日期~发放日期
                    int cont6 = endDate.compareTo(lis.get(0).getEndDate());//结束日期~到期日期
                    int cont7 = new Date().compareTo(lis.get(0).getEndDate());//当前时间和到期日比
                    BigDecimal repayMoney = BigDecimal.ZERO;
                    if ("A2".equals(cmsType)) {
                        repayMoney = cmsLoanRepayDao.getSendRepayedMoney(sendBillCode);
                    } else if ("C2".equals(cmsType)) {
                        repayMoney = cmsPrecativeLoanRepayDao.getInfoRepayMoneySum(sendBillCode);
                    }

                    if (repayMoney.compareTo(BigDecimal.ZERO) == 0) {//没还
                        int days = 0;
                        if (cont5 == -1 && cont6 == -1) {
                            days = DateUtils.getOffsetDays(lis.get(0).getSendDate(), endDate); //发放日期-查询截止日期
                        } else if (cont5 == -1 && cont6 == 1) {
                            days = DateUtils.getOffsetDays(lis.get(0).getSendDate(), lis.get(0).getEndDate()); //发放日期-到期日期
                            if (lis.get(0).getDeferredDate() != null) {//展期
                                int dayss = DateUtils.getOffsetDays(lis.get(0).getEndDate(), lis.get(0).getDeferredDate());
                                day = new BigDecimal(dayss);
                                yfInter = day.multiply(lis.get(0).getDeferredRate()).multiply(lis.get(0).getSendMoney()).divide(yaer, 2, RoundingMode.HALF_UP);
                                yfInterest = yfInterest.add(yfInter);
                            }
                        } else if ((cont5 == 0 || cont5 == 1) && cont6 == -1) {
                            days = DateUtils.getOffsetDays(startDate, endDate); //查询开始日期-查询截止日期
                        } else if ((cont5 == 0 || cont5 == 1) && (cont6 == 0 || cont6 == 1)) {
                            days = DateUtils.getOffsetDays(startDate, lis.get(0).getEndDate()); //查询开始日期-到期日期
                            if (lis.get(0).getDeferredDate() != null) {//展期
                                int dayss = DateUtils.getOffsetDays(lis.get(0).getEndDate(), lis.get(0).getDeferredDate());
                                day = new BigDecimal(dayss);
                                yfInter = day.multiply(lis.get(0).getDeferredRate()).multiply(lis.get(0).getSendMoney()).divide(yaer, 2, RoundingMode.HALF_UP);
                                yfInterest = yfInterest.add(yfInter);
                            }
                        }

                        if (cont7 == 1) {
                            int dayss = DateUtils.getOffsetDays(lis.get(0).getEndDate(), new Date());
                            day = new BigDecimal(dayss);
                            yfInter = day.multiply(lis.get(0).getOverdueRate()).multiply(lis.get(0).getSendMoney()).divide(yaer, 2, RoundingMode.HALF_UP);
                            yfInterest = yfInterest.add(yfInter);
                        }
                        if (days < 0) {
                            continue;
                        }
                        day = new BigDecimal(days);
                        yfInter = day.multiply(lis.get(0).getLoanRate()).multiply(lis.get(0).getSendMoney()).divide(yaer, 2, RoundingMode.HALF_UP);
                        yfInterest = yfInterest.add(yfInter);
                        lis.get(0).setYfInterest(yfInterest);//应付
                        lis.get(0).setSfInterest(BigDecimal.ZERO);//实付
                        lis.get(0).setCmsLoanType(cmsType);
                        result.add(lis.get(0));
                        yfInterest = BigDecimal.ZERO;
                        continue;
                    } else {
                        int days = DateUtils.getOffsetDays(lis.get(0).getSendDate(), lis.get(0).getEndDate()); //发放日期-预计还款日期
                        day = new BigDecimal(days);
                        if (lis.get(0).getRepaymentIst() == null) {
                            continue;
                        } else {
                            sfInter = day.divide(day, 2, BigDecimal.ROUND_HALF_UP).multiply(lis.get(0).getRepaymentIst());
                        }
                        lis.get(0).setYfInterest(yfInterest);//应付
                        lis.get(0).setSfInterest(sfInter);//实付
                        lis.get(0).setCmsLoanType(cmsType);
                        result.add(lis.get(0));
                        continue;
                    }
                }
            }

            for (int j = 0; j < lis.size(); j++) {
                CmsInterestPayVO item = lis.get(j);//通过单号获取还款明细

                if (j == 0) {
                    sendDate = item.getSendDate();//第一笔起息日期取发放日期
                } else {
                    repayDate = item.getPrediceRepayDate();//第二笔开始取预计还款日期
                }

                int cont = startDate.compareTo(sendDate);//查询开始日期~发放日期 -1小于，1大于，0等于
                int cont1 = endDate.compareTo(item.getPrediceRepayDate());//查询结束日期~第一笔预计还款日期
                int cont2 = endDate.compareTo(sendDate);//查询截止日期~发放日期
                int cont3 = endDate.compareTo(pvo.getPrediceRepayDate());//查询结束日期~还款计划最后一笔的预计还款日期
                int cont4 = startDate.compareTo(item.getPrediceRepayDate());//查询开始日期~预计还款日期
                int cont8 = new Date().compareTo(item.getPrediceRepayDate());//当前时间和预计还款日比较
                int cont9 = startDate.compareTo(pvo.getPrediceRepayDate());//查询开始日期~还款计划最后一笔的预计还款日期

                //1 止期小于发放日期
                if (endDate.compareTo(sendDate) < 0) {
                    break;
                }
                //2 查询起始日期小于发放日期,查询结束日期在发放日期和还款计划最后一笔数据之间
                // 查询的开始时间<起息日期<=查询结束时间<止息日期
                if ((cont == -1 || cont == 0) && cont2 == 1 && cont3 == -1) {
//                    预计还款日期大于查询开始日期
                    if (item.getPrediceRepayDate().compareTo(startDate) >= 0) {
                        int interestDate = 0;//计息天数
                        int totalInterestDate = 0;//计息总天数
                        if (endDate.compareTo(item.getPrediceRepayDate()) < 0 && endRepayDate.compareTo(item.getPrediceRepayDate()) == 0) {
                            interestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, endDate); //开始时间-预计还款日期
                            totalInterestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //发放日期预计还款日期
                        } else {
                            interestDate = DateUtils.getOffsetDays(j == 0 ? sendDate : upRepayDate, item.getPrediceRepayDate()); //开始时间-预计还款日期
                            totalInterestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //发放日期预计还款日期
                        }
                        BigDecimal iDate = new BigDecimal(interestDate);//计息天数
                        BigDecimal tDate = new BigDecimal(totalInterestDate);//计息总天数
                        if (BigDecimal.ZERO.compareTo(tDate) == 0) {
                            yfInter = BigDecimal.ZERO;
                        } else {
                            yfInter = iDate.divide(tDate, BigDecimal.ROUND_HALF_UP, 4).multiply(item.getRepaymentIst());
                        }
                        if (CommonConstant.YES.equals(item.getIsRepay()) && BigDecimal.ZERO.compareTo(tDate) < 0) {
                            if (null == item.getRepaymentRelIst()) {
                                item.setRepaymentRelIst(item.getRepaymentIst());
                            }
                            sfInter = iDate.divide(tDate, BigDecimal.ROUND_HALF_UP, 4).multiply(item.getRepaymentRelIst());
                            ;
                        } else {
                            sfInter = BigDecimal.ZERO;
                        }
                        yfInterest = yfInterest.add(yfInter);
                        sfInterest = sfInterest.add(sfInter);
                    }
                }

                //3.查询的起始日期和结束日期都在还款计划之间  算开始时间到结束时间的的利息
                //起息日期<=查询的开始时间<查询结束日期<=止息日期
                if ((cont == 1 || cont == 0) && (cont3 == -1 || cont3 == 0)) {
//                    预计还款日期大于查询开始日期
                    if (item.getPrediceRepayDate().compareTo(startDate) >= 0) {
                        int interestDate = 0;//计息天数
                        int totalInterestDate = 0;//计息总天数
                        if (endDate.compareTo(item.getPrediceRepayDate()) < 0 && endRepayDate.compareTo(item.getPrediceRepayDate()) == 0) {
                            interestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, endDate); //开始时间-预计还款日期
                            totalInterestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //发放日期预计还款日期
                        } else {
                            //                            上次还款日期<查询日期<本次还款日期
                            if (startDate.compareTo(upRepayDate) >= 0 && startDate.compareTo(item.getPrediceRepayDate()) < 0) {
                                interestDate = DateUtils.getOffsetDays(startDate, item.getPrediceRepayDate()); //开始时间-预计还款日期
                            } else {
                                interestDate = DateUtils.getOffsetDays(upRepayDate, item.getPrediceRepayDate()); //开始时间-预计还款日期
                            }
                            totalInterestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //发放日期预计还款日期
                        }
                        BigDecimal iDate = new BigDecimal(interestDate);//计息天数
                        BigDecimal tDate = new BigDecimal(totalInterestDate);//计息总天数
                        if (BigDecimal.ZERO.compareTo(tDate) == 0) {
                            yfInter = BigDecimal.ZERO;
                        } else {
                            yfInter = iDate.divide(tDate, BigDecimal.ROUND_HALF_UP, 4).multiply(item.getRepaymentIst());
                        }
                        if (CommonConstant.YES.equals(item.getIsRepay()) && BigDecimal.ZERO.compareTo(tDate) < 0) {
                            if (null == item.getRepaymentRelIst()) {
                                item.setRepaymentRelIst(item.getRepaymentIst());
                            }
                            sfInter = iDate.divide(tDate, BigDecimal.ROUND_HALF_UP, 4).multiply(item.getRepaymentRelIst());
                            ;
                        } else {
                            sfInter = BigDecimal.ZERO;
                        }
                        yfInterest = yfInterest.add(yfInter);
                        sfInterest = sfInterest.add(sfInter);
                    }
                }
                //4。查询的起始日期在范围中，结束日期不在
                //起息日期<查询的开始时间<止息日期<查询结束时间
                if ((cont == 1 && cont9 == -1 || cont4 == 0) && cont3 == 1) {
//                    预计还款日期大于查询开始日期
                    if (item.getPrediceRepayDate().compareTo(startDate) >= 0) {
                        int interestDate = 0;//计息天数
                        int totalInterestDate = 0;//计息总天数
                        if (item.getPrediceRepayDate().compareTo(endRepayDate) == 0) {
                            interestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //开始时间-预计还款日期
                            totalInterestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //发放日期预计还款日期

                            if (item.getDeferredDate() != null) {//展期
                                int dayss = DateUtils.getOffsetDays(pvo.getPrediceRepayDate(), item.getDeferredDate());
                                day = new BigDecimal(dayss);
                                yfInter = day.multiply(item.getDeferredRate()).multiply(item.getSurplusMoney().add(item.getRepayMoney())).divide(yaer, 2, RoundingMode.HALF_UP);
                                yfInterest = yfInterest.add(yfInter);
                            }
                        } else {
                            //                            上次还款日期<查询日期<本次还款日期
                            if (startDate.compareTo(upRepayDate) >= 0 && startDate.compareTo(item.getPrediceRepayDate()) < 0) {
                                interestDate = DateUtils.getOffsetDays(startDate, item.getPrediceRepayDate()); //开始时间-预计还款日期
                            } else {
                                interestDate = DateUtils.getOffsetDays(upRepayDate, item.getPrediceRepayDate()); //上次预计还款日-预计还款日期
                            }
                            totalInterestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //发放日期预计还款日期
                        }
                        BigDecimal iDate = new BigDecimal(interestDate);//计息天数
                        BigDecimal tDate = new BigDecimal(totalInterestDate);//计息总天数
                        //                            是否已还，未还则逾期
                        if (!CommonConstant.YES.equals(item.getIsRepay())) {
                            if (cont8 == 1) {//逾期
                                int dayss = DateUtils.getOffsetDays(item.getPrediceRepayDate(), new Date());
                                day = new BigDecimal(dayss);
                                yfInter = day.multiply(item.getOverdueRate()).multiply(item.getSurplusMoney().add(item.getRepayMoney())).divide(yaer, 2, RoundingMode.HALF_UP);
                                yfInterest = yfInterest.add(yfInter);
                            }
                        }
                        if (BigDecimal.ZERO.compareTo(tDate) == 0) {
                            yfInter = BigDecimal.ZERO;
                        } else {
                            yfInter = iDate.divide(tDate, BigDecimal.ROUND_HALF_UP, 4).multiply(item.getRepaymentIst());
                        }
                        if (CommonConstant.YES.equals(item.getIsRepay()) && BigDecimal.ZERO.compareTo(tDate) < 0) {
                            if (null == item.getRepaymentRelIst()) {
                                item.setRepaymentRelIst(item.getRepaymentIst());
                            }
                            sfInter = iDate.divide(tDate, BigDecimal.ROUND_HALF_UP, 4).multiply(item.getRepaymentRelIst());
                            ;
                        } else {
                            sfInter = BigDecimal.ZERO;
                        }
                        yfInterest = yfInterest.add(yfInter);
                        sfInterest = sfInterest.add(sfInter);
                    }
                }

                //5.开始日期小于发放日期，结束日期大于最后一笔还款计划
                //查询的开始时间<起息日期<止息日期<查询结束时间
                if (cont == -1 && (cont3 == 1 || cont3 == 0)) {
//                    预计还款日期大于查询开始日期
                    yfInter = item.getRepaymentIst();
                    if (CommonConstant.YES.equals(item.getIsRepay())) {
                        if (null == item.getRepaymentRelIst()) {
                            item.setRepaymentRelIst(item.getRepaymentIst());
                        }
                        sfInter = item.getRepaymentRelIst();
                    } else {
                        sfInter = BigDecimal.ZERO;
                    }
                    yfInterest = yfInterest.add(yfInter);
                    sfInterest = sfInterest.add(sfInter);
                    if (item.getPrediceRepayDate().compareTo(endRepayDate) == 0) {
                        if (item.getDeferredDate() != null) {//展期
                            int dayss = DateUtils.getOffsetDays(pvo.getPrediceRepayDate(), item.getDeferredDate());
                            day = new BigDecimal(dayss);
                            yfInter = day.multiply(item.getDeferredRate()).multiply(item.getSurplusMoney().add(item.getRepayMoney())).divide(yaer, 2, RoundingMode.HALF_UP);
                            yfInterest = yfInterest.add(yfInter);
                        }
                    }
                    if (!CommonConstant.YES.equals(item.getIsRepay())) {
                        if (cont8 == 1) {//逾期
                            int dayss = DateUtils.getOffsetDays(item.getPrediceRepayDate(), new Date());
                            day = new BigDecimal(dayss);
                            if(item.getSurplusMoney() != null && item.getRepayMoney() != null && item.getOverdueRate() != null){
                                yfInter = day.multiply(item.getOverdueRate()).multiply(item.getSurplusMoney().add(item.getRepayMoney())).divide(yaer, 2, RoundingMode.HALF_UP);
                            }
                            yfInterest = yfInterest.add(yfInter);
                        }
                    }
                }

                //6.起始日期在结束日期后
                if (startDate.compareTo(pvo.getPrediceRepayDate()) == 1) {
                    break;
                }
                if (endRepayDate.compareTo(item.getPrediceRepayDate()) == 0) {
                    item.setYfInterest(yfInterest);
                    item.setSfInterest(sfInterest);
                    result.add(item);
                    break;
                }
                upRepayDate = item.getPrediceRepayDate();
                yfInter = BigDecimal.ZERO;
                sfInter = BigDecimal.ZERO;
            }
            yfInterest = BigDecimal.ZERO;
            sfInterest = BigDecimal.ZERO;
            upRepayDate = startDate;
        }
    
        return result;
    }

    //用结束日期和全部的计划对比出，最后一个日期是多少
    public  Date getRepayDate(List<CmsInterestPayVO> list,Date endDate){
        Date repayDate = null;
        for (CmsInterestPayVO cmsInterestPayVO : list) {
            if (cmsInterestPayVO.getPrediceRepayDate() == null) {
                continue;
            } else {
                int flag = cmsInterestPayVO.getPrediceRepayDate().compareTo(endDate);
                //-1小于，1大于，0等于
                if (flag == 1) {
                    repayDate = cmsInterestPayVO.getPrediceRepayDate();
                    break;
                } else if (flag == 0) {
                    repayDate = cmsInterestPayVO.getPrediceRepayDate();
                    break;
                }
            }

        }
        
        if(repayDate == null){
            if(list.size() == 1){
                repayDate =  (list.get(0).getPrediceRepayDate() == null) ? null : list.get(0).getPrediceRepayDate();
            }else{
                repayDate =  (list.get(list.size()-1).getPrediceRepayDate() == null) ? null : list.get(list.size()-1).getPrediceRepayDate();
            }
        }
        return repayDate;
    }
        
    public Map<String,List<CmsInterestPayVO>> getPlanList(List<CmsInterestPayVO> list){
        String sendBillCode = "";
        List<CmsInterestPayVO> res = null;
        Map<String,List<CmsInterestPayVO>> map = new HashMap<String, List<CmsInterestPayVO>>();
        for (CmsInterestPayVO vo : list) {
            sendBillCode = vo.getSendBillCode();
            if (map.get(sendBillCode) != null) {
                map.get(sendBillCode).add(vo);
            } else {
                res = new ArrayList<CmsInterestPayVO>();
                res.add(vo);
                map.put(sendBillCode, res);
            }

        }

        return map;

    }

    public List<String> getKey(List<CmsInterestPayVO> list){
        String sendBillCode = "";
        List<String> res  = new ArrayList<String>();;

        for (CmsInterestPayVO vo : list) {
            if (!vo.getSendBillCode().equals(sendBillCode)) {//发放单号相同
                sendBillCode = vo.getSendBillCode();
                if (EmptyUtils.isNotEmpty(sendBillCode)) {
                    res.add(sendBillCode);
                }
            }
        }
        return res;
    }

    
    //实付~应付 明细数据
    public List<CmsInterestPayVO> getDetails(String sendBillCode,String isRepay,String cmsType,String startDate,String endDate){
        Date searchStartDate = DateUtils.parseDate(startDate);//查询开始日期
        Date searchEndDate = DateUtils.parseDate(endDate);//查询结束日期
        List<CmsInterestPayVO> yfList = new ArrayList<CmsInterestPayVO>();
        List<CmsInterestPayVO> sfList = new ArrayList<CmsInterestPayVO>();
        BigDecimal repayMoney = BigDecimal.ZERO;
       
        Date sendDate = null;
        Date repayDate = null;
        Date beginInterestDate = null;
        Date endInterestDate = null;
        //临时
        BigDecimal sfInter = BigDecimal.ZERO;
        BigDecimal yfInter = BigDecimal.ZERO;

        Date upRepayDate =searchStartDate;
        BigDecimal day = BigDecimal.ZERO;
        BigDecimal yaer = new BigDecimal("360");
        BigDecimal sfInterest = BigDecimal.ZERO;
        BigDecimal yfInterest = BigDecimal.ZERO;
        List<CmsInterestPayVO> list =  paymentsDao.getDetails(sendBillCode,isRepay,cmsType);//数据集合
        
        if(EmptyUtils.isEmpty(list.get(0).getPrediceRepayDate())){//没有还款计划
            String cmsType1 = sendBillCode.substring(0,2);
            if ("A2".equals(cmsType1)) {
                repayMoney = cmsLoanRepayDao.getSendRepayedMoney(sendBillCode);
            } else if ("C2".equals(cmsType1)) {
                repayMoney = cmsPrecativeLoanRepayDao.getInfoRepayMoneySum(sendBillCode);
            }
            int cont5 = searchStartDate.compareTo(list.get(0).getSendDate());//开始日期~发放日期
            int cont6 = searchEndDate.compareTo(list.get(0).getEndDate());//结束日期~到期日期
            
            if(searchStartDate.compareTo(list.get(0).getEndDate()) == 1){//查询开始日期比最后还款日大
                return new ArrayList<CmsInterestPayVO>();
            }
            
            if (repayMoney.compareTo(BigDecimal.ZERO) == 0) {//没还
                int days = 0;
                if (cont5 == -1 && cont6 == -1) {
                    days = DateUtils.getOffsetDays(list.get(0).getSendDate(), searchEndDate); //发放日期-查询截止日期
                    list.get(0).setInterestStartDate(list.get(0).getSendDate());//起息日期
                    list.get(0).setBlnInstDate(searchEndDate);//止息日期
                }else if(cont5 == -1 && cont6 == 1){
                    days = DateUtils.getOffsetDays(list.get(0).getSendDate(), list.get(0).getEndDate()); //发放日期-到期日期
                    list.get(0).setInterestStartDate(list.get(0).getSendDate());//起息日期
                    list.get(0).setBlnInstDate(list.get(0).getEndDate());//止息日期
                }else if ((cont5 == 0 || cont5 == 1) && cont6 == -1) {
                    days = DateUtils.getOffsetDays(searchStartDate, searchEndDate); //查询开始日期-查询截止日期
                    list.get(0).setInterestStartDate(searchStartDate);//起息日期
                    list.get(0).setBlnInstDate(searchEndDate);//止息日期
                } else if ((cont5 == 0 || cont5 == 1) && (cont6 == 0 || cont6 == 1)) {
                    days = DateUtils.getOffsetDays(searchStartDate, list.get(0).getEndDate()); //查询开始日期-到期日期
                    list.get(0).setInterestStartDate(searchStartDate);//起息日期
                    list.get(0).setBlnInstDate(list.get(0).getEndDate());//止息日期
                }
                day = new BigDecimal(days);
                yfInterest = day.multiply(list.get(0).getLoanRate()).multiply(list.get(0).getSendMoney()).divide(yaer, 2, RoundingMode.HALF_UP);
                list.get(0).setYfInterest(yfInterest);//应付
                list.get(0).setBearingMoney(list.get(0).getSendMoney());//计息金额
                yfList.add(list.get(0));
               
                if ((cont5 == 0 || cont5 == 1 || cont5 == -1) && cont6 == 1){//展期
                    if (list.get(0).getDeferredDate() != null) {//展期
                        int dayss = DateUtils.getOffsetDays(list.get(0).getEndDate(), list.get(0).getDeferredDate());
                        day = new BigDecimal(dayss);
                        yfInterest = day.multiply(list.get(0).getDeferredRate()).multiply(list.get(0).getSendMoney()).divide(yaer, 2, RoundingMode.HALF_UP);
                        CmsInterestPayVO vo = new CmsInterestPayVO();
                        BeanUtils.copyNotNullProperties(list.get(0), vo);
                        vo.setYfInterest(yfInterest);//应付
                        vo.setBearingMoney(list.get(0).getSendMoney());//计息金额
                        vo.setLoanRate(list.get(0).getDeferredRate());//利率
                        vo.setInterestStartDate(list.get(0).getEndDate());//起息日期
                        vo.setBlnInstDate(list.get(0).getDeferredDate());//止息日期
                        yfList.add(vo);
                    }
                }
                return yfList;
            }else{
                int days = DateUtils.getOffsetDays(list.get(0).getSendDate(), list.get(0).getEndDate()); //发放日期-到期日期
                day = new BigDecimal(days);
                if(list.get(0).getRepaymentIst() == null){
                    return new ArrayList<CmsInterestPayVO>();
                }else{
                    sfInterest = day.multiply(list.get(0).getRepaymentIst()).divide(day,2,BigDecimal.ROUND_HALF_UP);
                }
                
                list.get(0).setBearingMoney(list.get(0).getSendMoney());//计息金额
                list.get(0).setInterestStartDate(list.get(0).getSendDate());//起息日期
                list.get(0).setBlnInstDate(list.get(0).getEndDate());//止息日期
                list.get(0).setSfInterest(sfInterest);//实付
                sfList.add(list.get(0));
                return sfList;
               
            }
        }

        Date lastDate =  getRepayDate(list,searchEndDate);//最后一笔还款日期

        for(int i=0;i<list.size();i++) {
            CmsInterestPayVO  item = list.get(i);

                if (i == 0) {
                    sendDate = item.getSendDate();//第一笔起息日期取发放日期
                } else {
                    repayDate = item.getPrediceRepayDate();//第二笔开始取预计还款日期
                }
                int cont = searchStartDate.compareTo(sendDate);//查询开始日期~发放日期 -1小于，1大于，0等于
                int cont1 = searchEndDate.compareTo(item.getPrediceRepayDate());//查询结束日期~第一笔预计还款日期
                int cont2 = searchEndDate.compareTo(sendDate);//查询截止日期~发放日期
                int cont3 = searchEndDate.compareTo(list.get(list.size() - 1).getPrediceRepayDate());//查询结束日期~还款计划最后一笔的预计还款日期
                int cont4 = searchStartDate.compareTo(item.getPrediceRepayDate());//查询开始日期~预计还款日期
                int cont8 = new Date().compareTo(item.getPrediceRepayDate());//当前时间和预计还款日比较
                int cont9 = searchStartDate.compareTo(list.get(list.size() - 1).getPrediceRepayDate());//查询开始日期~还款计划最后一笔的预计还款日期

                //查询起始日期小于发放日期,查询结束日期在发放日期和还款计划最后一笔数据之间
            // 查询的开始时间<起息日期，起息日期<=查询结束时间<止息日期
                if ((cont == -1 || cont == 0) && cont2 == 1 && cont3 ==-1)  {
//                    预计还款日期大于查询开始日期
                    if (item.getPrediceRepayDate().compareTo(searchStartDate) >= 0) {
                        int interestDate = 0;//计息天数
                        int totalInterestDate = 0;//计息总天数
                        if (searchEndDate.compareTo(item.getPrediceRepayDate()) < 0 && lastDate.compareTo(item.getPrediceRepayDate()) == 0) {
                            beginInterestDate = null == upRepayDate ? sendDate : upRepayDate;
                            endInterestDate = searchEndDate;
                            interestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate,searchEndDate); //开始时间-预计还款日期
                            totalInterestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //发放日期预计还款日期
                        } else {
                            beginInterestDate = i == 0 ? sendDate : upRepayDate;
                            endInterestDate = item.getPrediceRepayDate();
                            interestDate = DateUtils.getOffsetDays(beginInterestDate, item.getPrediceRepayDate()); //开始时间-预计还款日期
                            totalInterestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //发放日期预计还款日期
                        }
                        BigDecimal iDate = new BigDecimal(interestDate);//计息天数
                        BigDecimal tDate = new BigDecimal(totalInterestDate);//计息总天数
                        if (tDate.compareTo(BigDecimal.ZERO)==0){
                            yfInter = BigDecimal.ZERO;
                        }else {
                            yfInter = iDate.divide(tDate,BigDecimal.ROUND_HALF_UP,4).multiply(item.getRepaymentIst()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        }
                        sfInter = BigDecimal.ZERO;
                        if (CommonConstant.YES.equals(item.getIsRepay()) && BigDecimal.ZERO.compareTo(tDate) < 0) {
                            if (null == item.getRepaymentRelIst()) {
                                item.setRepaymentRelIst(item.getRepaymentIst());
                            }
                            if (tDate.compareTo(BigDecimal.ZERO)==0){
                                sfInter = BigDecimal.ZERO;
                            }else {
                                sfInter = iDate.divide(tDate, BigDecimal.ROUND_HALF_UP, 4).multiply(item.getRepaymentRelIst());
                            }
                            sfList.add(item);
                        } else {
                            sfInter = BigDecimal.ZERO;
                        }
                        yfList.add(item);
                        item.setSfInterest(sfInter);//实付利息
                        item.setYfInterest(yfInter);//应付利息
                        item.setInterestStartDate(beginInterestDate);//计息开始时间
                        item.setBlnInstDate(endInterestDate);//计息结束时间
                        item.setBearingMoney(item.getSurplusMoney().add(item.getRepayMoney()));//计息金额
                    }

                }
            

                //3.查询起始日期和结束日期都在还款计划之间
                if ((cont == 1 || cont ==0) && (cont3 == -1 || cont3 == 0)){//查询结束日期在还款计划最后一笔之前
//                    预计还款日期大于查询开始日期
                    if (item.getPrediceRepayDate().compareTo(searchStartDate) >= 0) {
                        int interestDate = 0;//计息天数
                        int totalInterestDate = 0;//计息总天数
                        if (searchEndDate.compareTo(item.getPrediceRepayDate()) < 0 && lastDate.compareTo(item.getPrediceRepayDate()) == 0) {
                            beginInterestDate = null == upRepayDate ? sendDate : upRepayDate;
                            endInterestDate = searchEndDate;
                            interestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, searchEndDate); //开始时间-预计还款日期
                            totalInterestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //发放日期预计还款日期
                        } else {
                            //                            上次还款日期<查询日期<本次还款日期
                            if (searchStartDate.compareTo(upRepayDate) >= 0 && searchStartDate.compareTo(item.getPrediceRepayDate()) < 0) {
                                beginInterestDate = searchStartDate;
                            } else {
                                beginInterestDate = upRepayDate;
                            }
                            endInterestDate = item.getPrediceRepayDate();
                            interestDate = DateUtils.getOffsetDays(beginInterestDate, item.getPrediceRepayDate()); //开始时间-预计还款日期
                            totalInterestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //发放日期预计还款日期
                        }
                        BigDecimal iDate = new BigDecimal(interestDate);//计息天数
                        BigDecimal tDate = new BigDecimal(totalInterestDate);//计息总天数
                        if (BigDecimal.ZERO.compareTo(tDate) == 0) {
                            yfInter = BigDecimal.ZERO;
                        }else {
                            yfInter = iDate.divide(tDate, BigDecimal.ROUND_HALF_UP, 4).multiply(item.getRepaymentIst());
                        }
                        if (CommonConstant.YES.equals(item.getIsRepay()) && BigDecimal.ZERO.compareTo(tDate) < 0) {
                            if (null == item.getRepaymentRelIst()) {
                                item.setRepaymentRelIst(item.getRepaymentIst());
                            }
                            sfInter = iDate.divide(tDate, BigDecimal.ROUND_HALF_UP, 4).multiply(item.getRepaymentRelIst());;
                            sfList.add(item);
                        } else {
                            sfInter = BigDecimal.ZERO;
                        }
                        yfList.add(item);
                        item.setSfInterest(sfInter);//实付利息
                        item.setYfInterest(yfInter);//应付利息
                        item.setInterestStartDate(beginInterestDate);//计息开始时间
                        item.setBlnInstDate(endInterestDate);//计息结束时间
                        item.setBearingMoney(item.getSurplusMoney().add(item.getRepayMoney()));//计息金额
                    }
                }
                //4。查询的起始日期在范围中，结束日期不在
            //起息日期<查询的开始时间<止息日期<查询结束时间

                if ((cont == 1 && cont9 == -1 || cont4 == 0) && cont3 == 1) {
//                    预计还款日期大于查询开始日期
                    if (item.getPrediceRepayDate().compareTo(searchStartDate) >= 0) {
                        int interestDate = 0;//计息天数
                        int totalInterestDate = 0;//计息总天数
                        if (item.getPrediceRepayDate().compareTo(lastDate) == 0) {
                            beginInterestDate = null == upRepayDate ? sendDate : upRepayDate;
                            endInterestDate = item.getPrediceRepayDate();
                            interestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //开始时间-预计还款日期
                            totalInterestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //发放日期预计还款日期
                            if(item.getDeferredDate() != null){//展期
                                int dayss = DateUtils.getOffsetDays(item.getPrediceRepayDate(),item.getDeferredDate());
                                day = new BigDecimal(dayss);
                                yfInter = day.multiply(item.getDeferredRate()).multiply(item.getSurplusMoney().add(item.getRepayMoney())).divide(yaer, 2, RoundingMode.HALF_UP);
                            }
                        } else {
//                            上次还款日期<查询日期<本次还款日期
                            if (searchStartDate.compareTo(upRepayDate) >= 0 && searchStartDate.compareTo(item.getPrediceRepayDate()) < 0) {
                                beginInterestDate = searchStartDate;
                            } else {
                                beginInterestDate = upRepayDate;
                            }
                            endInterestDate = item.getPrediceRepayDate();
                            interestDate = DateUtils.getOffsetDays(beginInterestDate, item.getPrediceRepayDate()); //开始时间-预计还款日期
                            totalInterestDate = DateUtils.getOffsetDays(null == upRepayDate ? sendDate : upRepayDate, item.getPrediceRepayDate()); //发放日期预计还款日期
                        }
                        if (!CommonConstant.YES.equals(item.getIsRepay())) {
                            if(cont8 == 1){//逾期
                                int dayss = DateUtils.getOffsetDays(item.getPrediceRepayDate(), new Date());
                                day = new BigDecimal(dayss);
                                yfInter = yfInter.add(day.multiply(item.getOverdueRate()).multiply(item.getSurplusMoney().add(item.getRepayMoney())).divide(yaer, 2, RoundingMode.HALF_UP));
                            }
                        }
                        BigDecimal iDate = new BigDecimal(interestDate);//计息天数
                        BigDecimal tDate = new BigDecimal(totalInterestDate);//计息总天数
                        if (BigDecimal.ZERO.compareTo(tDate) == 0) {
                            yfInter = BigDecimal.ZERO;
                        }else {
                            yfInter = yfInter.add(iDate.divide(tDate, BigDecimal.ROUND_HALF_UP, 4).multiply(item.getRepaymentIst()));
                        }
                        if (CommonConstant.YES.equals(item.getIsRepay()) && BigDecimal.ZERO.compareTo(tDate) < 0) {
                            if (null == item.getRepaymentRelIst()) {
                                item.setRepaymentRelIst(item.getRepaymentIst());
                            }
                            sfInter = iDate.divide(tDate, BigDecimal.ROUND_HALF_UP, 4).multiply(item.getRepaymentRelIst());;
                            sfList.add(item);
                        } else {
                            sfInter = BigDecimal.ZERO;
                        }
                        yfList.add(item);
                        item.setSfInterest(sfInter);//实付利息
                        item.setYfInterest(yfInter);//应付利息
                        item.setInterestStartDate(beginInterestDate);//计息开始时间
                        item.setBlnInstDate(endInterestDate);//计息结束时间
                        item.setBearingMoney(item.getSurplusMoney().add(item.getRepayMoney()));//计息金额
                    }
                }

                //5.开始日期小于发放日期，结束日期大于最后一笔还款计划
                if(cont == -1 && (cont3 == 1 || cont3==0)){
//                    预计还款日期大于查询开始日期
                    yfInter = item.getRepaymentIst();
                    beginInterestDate = upRepayDate;
                    endInterestDate = item.getPrediceRepayDate();
                    if (CommonConstant.YES.equals(item.getIsRepay())) {
                        if (null == item.getRepaymentRelIst()) {
                            item.setRepaymentRelIst(item.getRepaymentIst());
                        }
                        sfInter = item.getRepaymentRelIst();
                        sfList.add(item);
                    } else {
                        sfInter = BigDecimal.ZERO;
                    }
                    if (item.getPrediceRepayDate().compareTo(lastDate) == 0) {
                        if (item.getPrediceRepayDate().compareTo(lastDate) == 0) {
                            if(item.getDeferredDate() != null){//展期
                                int dayss = DateUtils.getOffsetDays(item.getPrediceRepayDate(),item.getDeferredDate());
                                day = new BigDecimal(dayss);
                                yfInter = yfInter.add(day.multiply(item.getDeferredRate()).multiply(item.getSurplusMoney().add(item.getRepayMoney())).divide(yaer, 2, RoundingMode.HALF_UP));
                            }
                        }
                    }

                    if (!CommonConstant.YES.equals(item.getIsRepay())) {
                        if(cont8 == 1){//逾期
                            int dayss = DateUtils.getOffsetDays(item.getPrediceRepayDate(), new Date());
                            day = new BigDecimal(dayss);
                            yfInter = yfInter.add(day.multiply(item.getOverdueRate()).multiply(item.getRepayMoney().add(item.getSurplusMoney())).divide(yaer, 2, RoundingMode.HALF_UP));
                        }
                    }
                    yfList.add(item);
                    item.setSfInterest(sfInter);//实付利息
                    item.setYfInterest(yfInter);//应付利息
                    item.setInterestStartDate(beginInterestDate);//计息开始时间
                    item.setBlnInstDate(endInterestDate);//计息结束时间
                    item.setBearingMoney(item.getSurplusMoney().add(item.getRepayMoney()));//计息金额
                }
                if (lastDate.compareTo(item.getPrediceRepayDate()) == 0) {
                    break;
                }
                upRepayDate = item.getPrediceRepayDate();
                sfInter = BigDecimal.ZERO;
                yfInter = BigDecimal.ZERO;
            }
        if("0".equals(isRepay)){
            return yfList;
        }else{
            return sfList;
        }
       
    }


    /**
     * excel导出
     * @param vo
     * @return
     */
    public HSSFWorkbook exportExcel(CmsInterestPayVO vo){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        sheet.setColumnWidth(0,9000);
        sheet.setColumnWidth(1,3000);
        sheet.setColumnWidth(2,9000);
        sheet.setColumnWidth(3,5000);
        sheet.setColumnWidth(4,3000);
        sheet.setColumnWidth(5,5000);
        sheet.setColumnWidth(6,5000);
        sheet.setColumnWidth(7,3000);
        sheet.setColumnWidth(8,3000);
        sheet.setColumnWidth(9,5000);
        sheet.setColumnWidth(10,5000);
        createHeader(sheet, workbook);
        List<CmsInterestPayVO> cmsInterestPayVOS = getList(vo);
        Map<String, String> dictMap = new HashMap<>();
        BigDecimal yfTotal = BigDecimal.ZERO;
        BigDecimal sfTotal = BigDecimal.ZERO;
        createHeader(sheet, workbook);
        HSSFCellStyle contentStyle = getContentStyle(workbook);
        int rowIndex = 1;
        for (CmsInterestPayVO cmsInterestPayVO : cmsInterestPayVOS) {
            yfTotal = yfTotal.add(cmsInterestPayVO.getYfInterest());
            sfTotal = sfTotal.add(cmsInterestPayVO.getSfInterest());
            rowIndex = createContent(cmsInterestPayVO, sheet, rowIndex, contentStyle, dictMap);
        }
        HSSFRow totalRow = sheet.createRow(rowIndex);

        HSSFCell cell0 = totalRow.createCell(0);
        cell0.setCellValue("");

        HSSFCell cell1 = totalRow.createCell(1);
        cell1.setCellValue("");

        HSSFCell cell2 = totalRow.createCell(2);
        cell2.setCellValue("");

        HSSFCell cell3 = totalRow.createCell(3);
        cell3.setCellValue("");

        HSSFCell cell4 = totalRow.createCell(4);
        cell4.setCellValue("合计");

        HSSFCell cell5 = totalRow.createCell(5);
        cell5.setCellValue("");

        HSSFCell cell6 = totalRow.createCell(6);
        cell6.setCellValue("");

        HSSFCell cell7 = totalRow.createCell(7);
        cell7.setCellValue("");

        HSSFCell cell8 = totalRow.createCell(8);
        cell8.setCellValue("");

        HSSFCell cell9 = totalRow.createCell(9);
        cell9.setCellValue(yfTotal.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());

        HSSFCell cell10 = totalRow.createCell(10);
        cell10.setCellValue(sfTotal.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());

        Iterator<Cell> cellIterator = totalRow.iterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            cell.setCellStyle(getHeaderStyle(workbook));
        }
        return workbook;
    }

    private void createHeader(HSSFSheet sheet,HSSFWorkbook workbook){
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell0 = row.createCell(0);
        cell0.setCellValue("申请单位");

        HSSFCell cell1 = row.createCell(1);
        cell1.setCellValue("融资类型");

        HSSFCell cell2 = row.createCell(2);
        cell2.setCellValue("借款银行");

        HSSFCell cell3 = row.createCell(3);
        cell3.setCellValue("申请单号");

        HSSFCell cell4 = row.createCell(4);
        cell4.setCellValue("币别");

        HSSFCell cell5 = row.createCell(5);
        cell5.setCellValue("发放单号");

        HSSFCell cell6 = row.createCell(6);
        cell6.setCellValue("发放金额");

        HSSFCell cell7 = row.createCell(7);
        cell7.setCellValue("发放日期");

        HSSFCell cell8 = row.createCell(8);
        cell8.setCellValue("到期日期");

        HSSFCell cell9 = row.createCell(9);
        cell9.setCellValue("应付利息");

        HSSFCell cell10 = row.createCell(10);
        cell10.setCellValue("实付利息");

        Iterator<Cell> cellIterator = row.iterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            cell.setCellStyle(getHeaderStyle(workbook));
        }
    }

    private Integer createContent(CmsInterestPayVO cmsInterestPayVO, HSSFSheet sheet, int rowNum, HSSFCellStyle style, Map<String, String> dictMap) {

        HSSFRow row = sheet.createRow(rowNum++);
        HSSFCell cell0 = row.createCell(0);
        cell0.setCellValue(cmsInterestPayVO.getCorpName());//单位
        String cmsType = null;
        HSSFCell cell1 = row.createCell(1);
        if ("A2".equals(cmsInterestPayVO.getCmsType()) || "A2".equals(cmsInterestPayVO.getCmsLoanType())) {
            cmsType = "01";
        } else if ("B2".equals(cmsInterestPayVO.getCmsType()) || "B2".equals(cmsInterestPayVO.getCmsLoanType())) {
            cmsType = "02";
        } else if ("C2".equals(cmsInterestPayVO.getCmsType()) || "C2".equals(cmsInterestPayVO.getCmsLoanType())) {
            cmsType = "03";
        }

        if (StringUtils.isNotEmpty(dictMap.get("Loan.Sign_" + cmsType))) {
            cell1.setCellValue(dictMap.get("Loan.Sign_" + cmsType));//融资类型
        } else {
            cmsType = dictService.get("Loan.Sign", cmsType);
            dictMap.put("Loan.Sign_" + cmsType, cmsType);
            cell1.setCellValue(cmsType);
        }

        HSSFCell cell2 = row.createCell(2);
        cell2.setCellValue(cmsInterestPayVO.getBankName());//借款银行

        HSSFCell cell3 = row.createCell(3);
        cell3.setCellValue(cmsInterestPayVO.getBillCode());//申请单号

        HSSFCell cell4 = row.createCell(4);
        cell4.setCellValue(cmsInterestPayVO.getCurName());//币种

        HSSFCell cell5 = row.createCell(5);
        cell5.setCellValue(cmsInterestPayVO.getSendBillCode());//发放单号

        HSSFCell cell6 = row.createCell(6);
        cell6.setCellValue(cmsInterestPayVO.getSendMoney().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());//发放金额

        HSSFCell cell7 = row.createCell(7);
        cell7.setCellValue(DateUtils.formatDate(cmsInterestPayVO.getSendDate(),"yyyy-MM-dd"));//发放日期

        HSSFCell cell8 = row.createCell(8);
        cell8.setCellValue(DateUtils.formatDate(cmsInterestPayVO.getEndDate(),"yyyy-MM-dd"));//发放到期日期

        HSSFCell cell9 = row.createCell(9);
        cell9.setCellValue(cmsInterestPayVO.getYfInterest().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());//应付利息

        HSSFCell cell10 = row.createCell(10);
        cell10.setCellValue(cmsInterestPayVO.getSfInterest().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());//实付利息

        Iterator<Cell> cellIterator = row.iterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            cell.setCellStyle(style);
        }
        return rowNum;
    }


    /**
     * 设置表头样式
     * @param workbook
     * @return
     */
    public HSSFCellStyle getHeaderStyle(HSSFWorkbook workbook){
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setFont(font);
        return headerStyle;
    }


    /**
     * 设置内容样式
     * @param workbook
     * @return
     */
    public HSSFCellStyle getContentStyle(HSSFWorkbook workbook){
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setFont(font);
        return headerStyle;
    }

    @Override
    public BaseDao getEntityDao() {
        return paymentsDao;
    }
    
}



