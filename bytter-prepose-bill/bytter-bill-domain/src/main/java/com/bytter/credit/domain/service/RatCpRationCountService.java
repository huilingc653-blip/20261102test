package com.bytter.credit.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatBkrationDao;
import com.bytter.credit.domain.dao.RatBkrationDetailDao;
import com.bytter.credit.domain.dao.RatCpRationCountDao;
import com.bytter.credit.domain.entity.RatUration;
import com.bytter.credit.domain.vo.RatUrationDisBean;
import com.bytter.credit.dto.UrationParamDTO;
import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
/**
 * 内部授信数据额度明细和统计
 * 
 * @author fusb
 * @version 1.0
 * @Title RatCpRationCountService
 * @Description
 * @date 2018年12月1日
 */
@Service("ratCpRationCountService")
@Transactional
public class RatCpRationCountService extends BaseService<RatUration, String> {
    @Autowired
    private RatCpRationCountDao ratCpRationCountDao;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService curService;
    @Autowired
    private RatBkrationDetailDao ratDetailDao;
    @Autowired
    private RatBkrationDao ratBkrationDao;
    public BaseDao<RatUration, String> getEntityDao() {
        return ratCpRationCountDao;
    }
    private void setBeanMoneyType(RatUrationDisBean disBean, Integer moneyType) {
        if (RationConstant.COMMON_MONEYUNIT_1.equals(moneyType)) {
            return;
        }
        Class userCla = (Class) disBean.getClass();
        /*
         * 得到类中的所有属性集合
         */
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = f.get(disBean);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                if (type.endsWith("BigDecimal")) {
                    if (null == val) {
                        f.set(disBean, BigDecimal.ZERO);
                    } else {
                        int commonMoneyUnit = 1;
                        if (RationConstant.COMMON_MONEYUNIT_10000.equals(moneyType)) {
                            commonMoneyUnit = RationConstant.COMMON_MONEYUNIT_10000;
                        } else if (RationConstant.COMMON_MONEYUNIT_1000000.equals(moneyType)) {
                            commonMoneyUnit = RationConstant.COMMON_MONEYUNIT_1000000;
                        } else if (RationConstant.COMMON_MONEYUNIT_100000000.equals(moneyType)) {
                            commonMoneyUnit = RationConstant.COMMON_MONEYUNIT_100000000;
                        }
                        BigDecimal value = (BigDecimal) val;
                        f.set(disBean, value.divide(BigDecimal.valueOf(commonMoneyUnit), 2, BigDecimal.ROUND_HALF_UP));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 查询内部授信额度使用明细
     * 
     * @param paramDTO
     * @return
     */
    public List<RatUrationUseVO> queryRationUseDtl(UrationParamDTO paramDTO) {
        Date beginDate = paramDTO.getBeginDate();
        if (null == beginDate) {
            beginDate = DateUtils.getDate();
            paramDTO.setBeginDate(beginDate);
        }
        List<RatUrationDisBean> disBeans = ratCpRationCountDao.queryCorpRatDisration(paramDTO);
        // 对授信明细进行分组
        List<RatUrationUseVO> disDTOs = new ArrayList<RatUrationUseVO>();
        Map<String, RatUrationUseVO> disMap = new HashMap<String, RatUrationUseVO>();
        String itemCode = paramDTO.getItemCode();// 页面选择的授信品种
        if (!EmptyUtils.isEmpty(itemCode)) {
            itemCode = itemCode.trim();// 非空限制
        }
        if (EmptyUtils.isEmptyList(disBeans)) {
            disBeans = new ArrayList<RatUrationDisBean>();
        }
        for (RatUrationDisBean disBean : disBeans) {
            setBeanMoneyType(disBean, paramDTO.getMoneyType());
            String disCode = disBean.getDisCode();
            RatUrationUseVO disDTO = disMap.get(disCode);
            if (null == disDTO) {
                disDTO = new RatUrationUseVO();
                BeanUtils.copyProperties(disBean, disDTO);
                disDTO.setRationDlts(new ArrayList<RatUrationUseDtlVO>());
                disMap.put(disCode, disDTO);
            }
            // 在审占用额度+追减
            BigDecimal approveMoney = disBean.getFreezeMoney();
            disBean.setApproveMoney(approveMoney);// 在审占用
            disBean.setOverPlusMoney(disBean.getMoney().subtract(disBean.getUseMoney()).subtract(approveMoney)
                    .subtract(disBean.getVirtualRelief()).subtract(disBean.getVirtualUration())
                    .subtract(disBean.getReliefAndUrationMoney()));// 可用额度
            // 剩余额度 = 在审+可用
            disBean.setLeftMoney(disBean.getApproveMoney().add(disBean.getOverPlusMoney())
                    .add(disBean.getVirtualRelief()).add(disBean.getVirtualUration()));
            RatUrationUseDtlVO disDtlDTO = new RatUrationUseDtlVO();
            BeanUtils.copyProperties(disBean, disDtlDTO);
            disDTO.addRationDtl(disDtlDTO);
        }
        // 判断是否存在指定品种
        for (Entry<String, RatUrationUseVO> entry : disMap.entrySet()) {
            RatUrationUseVO disDTO = entry.getValue();
            if (EmptyUtils.isEmpty(itemCode)) {// 未选择品种时
                // setUrationDisDTO(disDTO);
                disDTOs.add(disDTO);
            } else {
                // 检查是否包含授信品种
                RatUrationUseDtlVO disDtlDTO = checkRationUseItem(disDTO, itemCode);
                if (null != disDtlDTO) {
                    disDTOs.add(disDTO);
                }
            }
        }
        // 计算品种的条数
        for (RatUrationUseVO disDTO : disDTOs) {
            List<RatUrationUseDtlVO> rationDtls = disDTO.getRationDlts();
            disDTO.setConId(ratBkrationDao.get(disDTO.getRatCode()).getConId());
            if (!EmptyUtils.isEmptyList(rationDtls)) {
                for (RatUrationUseDtlVO disDtlDTO : rationDtls) {
                    if (RationConstant.RAT_COMFLAG_0.equals(disDtlDTO.getItemFlag())) {
                        disDtlDTO.setRationSize(disDtlDTO.getRationSize() + 1);
                        disDTO.setRationSize(disDTO.getRationSize() + 1);
                    } else {
                        disDtlDTO.setRationSize(disDtlDTO.getRationDlts().size());
                    }
                }
                Collections.sort(rationDtls, new Comparator<RatUrationUseDtlVO>() {
                    @Override
                    public int compare(RatUrationUseDtlVO o1, RatUrationUseDtlVO o2) {
                        if (o1.getItemFlag().equals(o2.getItemFlag())) {
                            return o1.getItemOrder().compareTo(o2.getItemOrder());
                        }
                        return o1.getItemFlag().compareTo(o2.getItemFlag());
                    }
                });
            }
        }
        return disDTOs;
    }
    /**
     * 功能介绍 : 检查是否包含选择的品种。<br>
     * <b>日期：</b>2017年9月2日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    private RatUrationUseDtlVO checkRationUseItem(RatUrationUseVO disDTO, String itemCode) {
        List<RatUrationUseDtlVO> newRationDtls = new ArrayList<RatUrationUseDtlVO>();
        List<RatUrationUseDtlVO> rationDlts = disDTO.getRationDlts();
        for (RatUrationUseDtlVO dtlDTO : rationDlts) {
            if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {// 单品种
                if (itemCode.equals(dtlDTO.getItemCode())) {
                    newRationDtls.add(dtlDTO);
                    disDTO.setRationDlts(newRationDtls);
                    return dtlDTO;
                }
            } else {
                List<RatUrationUseDtlVO> ratDtls = dtlDTO.getRationDlts();// 综合品种
                for (RatUrationUseDtlVO dto : ratDtls) {// 综合品种匹配到则显示所有的综合品种信息
                    if (itemCode.equals(dto.getItemCode())) {
                        newRationDtls.add(dtlDTO);
                        disDTO.setRationDlts(newRationDtls);
                        return dtlDTO;
                    }
                }
            }
        }
        return null;
    }
    /**
     * 内部授信额度统计
     * 
     * @param paramDTO
     * @return
     */
    public List<RatUrationDisBean> queryRationUseCountDtl(UrationParamDTO paramDTO) {
        List<RatUrationUseVO> rations = null;
        rations = queryRationUseDtl(paramDTO);
        String curName = curService.getObjectById(paramDTO.getCurCode()).getCurName();
        List<RatUrationDisBean> listBean = new ArrayList<RatUrationDisBean>();
        for (RatUrationUseVO vo : rations) {
            RatUrationDisBean disBean = new RatUrationDisBean();
            disBean.setCorpId(vo.getCorpId());
            disBean.setCorpName(vo.getCorpName());
            disBean.setCorpNo(vo.getCorpNo());
            disBean.setBeginDate(vo.getBeginDate());
            disBean.setEndDate(vo.getEndDate());
            disBean.setCurCode(vo.getCurCode());
            disBean.setCurName(curName);
            BigDecimal money = new BigDecimal(0);// 总额度
            BigDecimal useMoney = new BigDecimal(0);// 占用金额
            BigDecimal approveMoney = new BigDecimal(0);// 在审占用
            BigDecimal leftMoney = new BigDecimal(0);// 剩余额度
            BigDecimal overPlusMoney = new BigDecimal(0);// 可用额度
            for (RatUrationUseDtlVO dtl : vo.getRationDlts()) {
                // 累加
                money = money.add(dtl.getMoney());
                useMoney = useMoney.add(dtl.getUseMoney());
                approveMoney = approveMoney.add(dtl.getApproveMoney());
                leftMoney = leftMoney.add(dtl.getLeftMoney());
                overPlusMoney = overPlusMoney.add(dtl.getOverPlusMoney());
            }
            disBean.setMoney(money);
            disBean.setUseMoney(useMoney);
            disBean.setApproveMoney(approveMoney);
            disBean.setLeftMoney(leftMoney);
            disBean.setOverPlusMoney(overPlusMoney);
            listBean.add(disBean);
        }
        List<RatUrationDisBean> listBean1 = new ArrayList<RatUrationDisBean>();
        Map<String, RatUrationDisBean> map = new HashMap<String, RatUrationDisBean>();
        for (RatUrationDisBean rBean : listBean) {
            String corpId = rBean.getCorpId();
            if (map.get(corpId) == null) {
                map.put(rBean.getCorpId(), rBean);
            } else {
                RatUrationDisBean rb = map.get(rBean.getCorpId());
                BigDecimal money = rb.getMoney();
                BigDecimal useMoney = rb.getUseMoney();
                BigDecimal approveMoney = rb.getApproveMoney();
                BigDecimal leftMoney = rb.getLeftMoney();
                BigDecimal overPlusMoney = rb.getOverPlusMoney();// 可用额度
                // 累加
                rb.setMoney(money.add(rBean.getMoney()));
                rb.setUseMoney(useMoney.add(rBean.getUseMoney()));
                rb.setApproveMoney(approveMoney.add(rBean.getApproveMoney()));
                rb.setLeftMoney(leftMoney.add(rBean.getLeftMoney()));
                rb.setOverPlusMoney(overPlusMoney.add(rBean.getOverPlusMoney()));
                if (rBean.getBeginDate().before(rb.getBeginDate())) {// rBean.getBeginDate()小于rb.getBeginDate()
                    rb.setBeginDate(rBean.getBeginDate());
                }
                if (rBean.getEndDate().after(rb.getEndDate())) {
                    rb.setEndDate(rBean.getEndDate());
                }
                map.put(rBean.getCorpId(), rb);
            }
        }
        // 将map转换成list
        List<RatUrationDisBean> listCpRat = new ArrayList<RatUrationDisBean>();
        for (Entry<String, RatUrationDisBean> entry : map.entrySet()) {
            //setBeanMoneyType(entry.getValue(), paramDTO.getMoneyType());
            listCpRat.add(entry.getValue());
        }
        return listCpRat;
    }
    /**
     * 查询授信单位
     * 
     * @param corpSql
     * @return
     */
    @Transactional(readOnly = true)
    public List<CorpDTO> queryRationCorp(String corpSql) {
        return ratCpRationCountDao.queryRationCorp(corpSql);
    }
}
