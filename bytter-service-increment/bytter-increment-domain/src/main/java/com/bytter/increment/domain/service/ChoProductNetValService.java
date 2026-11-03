package com.bytter.increment.domain.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.*;
import com.bytter.increment.api.constant.ChoProductReqConstant;
import com.bytter.increment.api.constant.ResourceConstant;
import com.bytter.increment.domain.dao.ChoProductNetValDao;
import com.bytter.increment.domain.entity.ChoProductNetVal;
import com.bytter.increment.domain.entity.ChoProductRedeem;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
/**
 * 理财产品净值维护service
 *
 * @author ouyj
 */
@Service("choProductNetValService")
@Transactional
public class ChoProductNetValService extends BaseService<ChoProductNetVal, String> {
    @Autowired
    private ChoProductNetValDao choProductNetValDao;
    @Autowired
    private ChoProductRedeemService choProductRedeemService;
    @Autowired
    private ChoProductReqService choProductReqService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<ChoProductNetVal, String> getEntityDao() {
        return choProductNetValDao;
    }
    /**
     * 保存对象
     *
     * @param entity
     */
    public void save(ChoProductNetVal entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新对象
     *
     * @param entity
     */
    public void update(ChoProductNetVal entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存或更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(ChoProductNetVal entity) {
        super.saveOrUpdate(entity);
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(ChoProductNetVal entity) {
        delete(entity.getId());
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        String moneyUint = (String) input.getFormdata().get("moneyUnit");
        if (EmptyUtils.isEmpty(moneyUint)) {
            moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
        }
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.CHO_PRODUCTNETVAL_CHOPRODUCTNETVAL_QUERY,
                        user.getId()), "pr.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = choProductNetValDao.getDataTablesUnion(input, sql.toString());
        // 注入远程信息
        Map<String, DictDTO> listStatusMap = dictService.listMap("choBankFinance.status");
        Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
        Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
        List<ChoProductReqVo> list = output.getData();
        for (ChoProductReqVo product : list) {
            setBeanMoneyType(product, moneyUint);
            if (ObjectUtil.isAllNotEmpty(product.getStatus(), listStatusMap)) {
                Optional.ofNullable(listStatusMap.get(product.getStatus() + "")).ifPresent(dict -> product.setStatusName(dict.getName()));
            }
            if (ObjectUtil.isAllNotEmpty(product.getFinancialType(), listTypeMap)) {
                Optional.ofNullable(listTypeMap.get(product.getFinancialType())).ifPresent(dict -> product.setFinancialTypeName(dict.getName()));
            }
            if (ObjectUtil.isAllNotEmpty(product.getPayWay(), listPayWayMap)) {
                Optional.ofNullable(listPayWayMap.get(product.getPayWay())).ifPresent(dict -> product.setPayWayName(dict.getName()));
            }
        }
        output.setData(list);
        return output;
    }
    /**
     * 金额单位转换
     *
     * @param ratVO
     * @param moneyType
     */
    private void setBeanMoneyType(ChoProductReqVo ratVO, String moneyType) {
        if (ChoProductReqConstant.COMMON_MONEYUNIT_1.equals(moneyType)) {
            return;
        }
        Class userCla = (Class) ratVO.getClass();
        /*
         * 得到类中的所有属性集合
         */
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); //设置些属性是可以访问的
                Object val = f.get(ratVO);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                String name = f.getName();//得到属性的名称
                if (type.endsWith("BigDecimal") && name.endsWith("Money")) {
                    if (null == val) {
                        f.set(ratVO, BigDecimal.ZERO);
                    } else {
                        BigDecimal value = (BigDecimal) val;
                        f.set(ratVO, value.divide(BigDecimal.valueOf(Integer.parseInt(moneyType)), 6, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 查询净值维护详情列表
     *
     * @param dataTablesInput 前端参数
     * @return 净值列表
     */
    public DataTablesOutput getEditDataTable(DataTablesInput dataTablesInput) {
        return choProductNetValDao.getEditDataTable(dataTablesInput);
    }
    /**
     * 根据登记id查询对应净值维护数据
     *
     * @param reqId 登记id
     * @return 净值维护集合
     */
    public List<ChoProductNetVal> listByReqId(String reqId) {
        return choProductNetValDao.listByReqId(reqId, null, null);
    }
    /**
     * 根据登记id查询netValDate日期钱对应净值维护数据
     *
     * @param reqId      登记id
     * @param netValDate 净值维护日期
     * @param comparator 比较符
     * @return 净值维护集合
     */
    public List<ChoProductNetVal> listByReqId(String reqId, Date netValDate, String comparator) {
        return choProductNetValDao.listByReqId(reqId, netValDate, comparator);
    }
    /**
     * 新增净值维护数据
     *
     * @param choProductNetVal 净值维护数据
     * @param model            model
     */
    public void addNetVal(ChoProductNetVal choProductNetVal, Model model) {
        String reqId = choProductNetVal.getReqId();
        if (StringUtils.isNotEmpty(reqId)) {
            // 查询该理财产品对应的所有净值维护数据
            List<ChoProductNetVal> choProductNetValList = listByReqId(reqId);
            if (!CollectionUtils.isEmpty(choProductNetValList)) {
                // 理财登记数据
                ChoProductReq choProductReq = choProductReqService.get(reqId, true);
                ChoProductNetVal nextChoProductNetVal = new ChoProductNetVal();
                // 最近一条净值维护数据
                ChoProductNetVal entity = choProductNetValList.get(0);
                model.addAttribute("choProductNetVal", entity);
                // 最近一条净值维护数据的下一天
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(entity.getNetValDate());
                // 把日期往后增加一天,整数往后推,负数往前移动
                calendar.add(Calendar.DATE, 1);
                Date nextDate = calendar.getTime();
                // 这个时间就是日期往后推一天的结果
                model.addAttribute("nextDate", DateUtils.formatDate(nextDate));
                // 查询该理财产品对应nextDate所有赎回审批通过的数据
                List<ChoProductRedeem> choProductRedeemList = choProductRedeemService.listProductRedeemApproved(reqId, nextDate);
                // 赎回总金额
                BigDecimal redeemMoneySum = new BigDecimal(0);
                // 赎回总份额
                BigDecimal redeemPortionSum = new BigDecimal(0);
                if (!CollectionUtils.isEmpty(choProductRedeemList)) {
                    for (ChoProductRedeem choProductRedeem : choProductRedeemList) {
                        redeemMoneySum = redeemMoneySum.add(choProductRedeem.getRedeemMoney());
                        redeemPortionSum = redeemPortionSum.add(choProductRedeem.getRedeemPortion());
                    }
                    // 标记有赎回审批通过数据
                    model.addAttribute("isRedeem", true);
                    // 赎回份额
                    model.addAttribute("redeemPortion", redeemPortionSum);
                }
                nextChoProductNetVal.setBuyMoney(entity.getBuyMoney().subtract(redeemMoneySum));
                nextChoProductNetVal.setBuyPortion(entity.getBuyPortion().subtract(redeemPortionSum));
                model.addAttribute("nextChoProductNetVal", nextChoProductNetVal);
                model.addAttribute("choProductReq", choProductReq);
            }
        }
    }
    /**
     * 跳转到净值维护列表页
     *
     * @param choProductNetVal 净值维护实体
     * @param model            model
     * @return 前端页路径
     */
    public String toUpdate(ChoProductNetVal choProductNetVal, Model model) {
        String reqId = choProductNetVal.getReqId();
        if (StringUtils.isNotEmpty(reqId)) {
            ChoProductReqVo choProductReqVo = choProductReqService.getChoProductReqVoById(reqId);
            List<ChoProductNetVal> choProductNetValList = listByReqId(reqId);
            if (!CollectionUtils.isEmpty(choProductNetValList)) {
                // 最新一条净值维护数据
                model.addAttribute("lastId", choProductNetValList.get(0).getId());
                model.addAttribute("lastMoney", choProductNetValList.get(0).getBuyMoney());
                // 最老一条净值维护数据
                model.addAttribute("firstId", choProductNetValList.get(choProductNetValList.size() - 1).getId());
            }
            model.addAttribute("choProductReqPage", choProductReqVo);
        }
        if (choProductNetVal.getIsView() != null) {
            model.addAttribute("isView", true);
        }
        return "chowmatistic/updateChoProductNetVal";
    }
    /**
     * 跳转到净值维护编辑页
     *
     * @param choProductNetVal 净值维护实体
     * @param model            model
     * @return 前端页面
     */
    public String updateNetVal(ChoProductNetVal choProductNetVal, Model model) {
        ChoProductNetVal entity = get(choProductNetVal.getId());
        if (entity != null) {
            // 查询该理财产品小于目前修改日期对应的净值维护数据
            List<ChoProductNetVal> choProductNetValList = listByReqId(entity.getReqId(), entity.getNetValDate(), "<");
            if (!CollectionUtils.isEmpty(choProductNetValList)) {
                // 理财登记数据
                ChoProductReq choProductReq = choProductReqService.get(entity.getReqId(), true);
                ChoProductNetVal nextChoProductNetVal = new ChoProductNetVal();
                // 最近一条净值维护数据
                ChoProductNetVal lastChoProductNetVal = choProductNetValList.get(0);
                model.addAttribute("lastChoProductNetVal", lastChoProductNetVal);
                // 查询该理财产品对应nextDate日期所有赎回审批通过的数据
                List<ChoProductRedeem> choProductRedeemList = choProductRedeemService.listProductRedeemApproved(entity.getReqId(), entity.getNetValDate());
                // 赎回总金额
                BigDecimal redeemMoneySum = new BigDecimal(0);
                // 赎回总份额
                BigDecimal redeemPortionSum = new BigDecimal(0);
                if (!CollectionUtils.isEmpty(choProductRedeemList)) {
                    for (ChoProductRedeem choProductRedeem : choProductRedeemList) {
                        redeemMoneySum = redeemMoneySum.add(choProductRedeem.getRedeemMoney());
                        redeemPortionSum = redeemPortionSum.add(choProductRedeem.getRedeemPortion());
                    }
                    // 标记有赎回审批通过数据
                    model.addAttribute("isRedeem", true);
                    // 赎回份额
                    model.addAttribute("redeemPortion", redeemPortionSum);
                }
                nextChoProductNetVal.setBuyMoney(entity.getBuyMoney().subtract(redeemMoneySum));
                nextChoProductNetVal.setBuyPortion(entity.getBuyPortion().subtract(redeemPortionSum));
                model.addAttribute("nextChoProductNetVal", nextChoProductNetVal);
                model.addAttribute("choProductReq", choProductReq);
            }
        }
        model.addAttribute("choProductNetVal", entity);
        return "chowmatistic/updateChoProductNetValDetail";
    }
    /**
     * 修改净值维护数据
     *
     * @param choProductNetVal 净值维护实体
     * @param user             当前用户
     */
    public void modify(ChoProductNetVal choProductNetVal, ICurrentUser user) {
        ChoProductNetVal t = get(choProductNetVal.getId(), true);
        choProductNetVal.setUpdateBy(user.getLoginName());
        choProductNetVal.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyNotNullProperties(choProductNetVal, t);
        // 获取当前修改日期的前一天
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(t.getNetValDate());
        calendar.add(Calendar.DATE, -1);
        Date preDate = calendar.getTime();
        // 查询该理财产品在netValDate前一天后的所有净值维护数据
        List<ChoProductNetVal> choProductNetValList = listByReqId(t.getReqId(), preDate, ">=");
        //当新增日期小于等与已有日期存在问题
        if (CollectionUtils.isEmpty(choProductNetValList)) {
         return;
        }
        // 理财登记实体
        ChoProductReq choProductReq = choProductReqService.get(t.getReqId(), true);
        // 存储更新后的净值维护实体集合
        ArrayList<ChoProductNetVal> updateNetValList = new ArrayList<>();
        // 获取当前修改日期的前一天净值维护数据
        ChoProductNetVal lastChoProductNetVal = choProductNetValList.get(choProductNetValList.size() - 1);
        for (int i = choProductNetValList.size() - 2; i >= 0; i--) {
            // 若为当前修改日期，则使用修改后的净值维护数据
            ChoProductNetVal temp = choProductNetValList.get(i).getId().equals(t.getId()) ? t : choProductNetValList.get(i);
            // 查询该理财产品对应nextDate日期所有赎回审批通过的数据
            List<ChoProductRedeem> choProductRedeemList = choProductRedeemService.listProductRedeemApproved(temp.getReqId(), temp.getNetValDate());
            // 赎回总金额
            BigDecimal redeemMoneySum = new BigDecimal(0);
            // 赎回总份额
            BigDecimal redeemPortionSum = new BigDecimal(0);
            // 本日日净值
            BigDecimal toDayNetVal = temp.getDailyNetVal();
            // 上日日净值
            BigDecimal lastDayNetVal = lastChoProductNetVal.getDailyNetVal();
            // 当日每万份收益=（本期日净值-上期日净值）*10000
            temp.setTenThousandIncome(toDayNetVal.subtract(lastDayNetVal).multiply(new BigDecimal(10000)));
            temp.setBuyMoney(lastChoProductNetVal.getBuyMoney());
            temp.setBuyPortion(lastChoProductNetVal.getBuyPortion());
            if (!CollectionUtils.isEmpty(choProductRedeemList)) {
                for (ChoProductRedeem choProductRedeem : choProductRedeemList) {
                    redeemMoneySum = redeemMoneySum.add(choProductRedeem.getRedeemMoney());
                    redeemPortionSum = redeemPortionSum.add(choProductRedeem.getRedeemPortion());
                }
                // 购买金额 = 上日购买金额 - 本日赎回金额
                temp.setBuyMoney(lastChoProductNetVal.getBuyMoney().subtract(redeemMoneySum));
                // 购买份额 = 上日购买份额 - 本日赎回份额
                temp.setBuyPortion(lastChoProductNetVal.getBuyPortion().subtract(redeemPortionSum));
                // 当日收益=(本日净值-上日净值)*（购买份额-赎回份额）已在购买金额中减去了赎回金额
                temp.setDayIncome(toDayNetVal.subtract(lastDayNetVal).multiply(temp.getBuyPortion()));
                // 赎回实际收益 = （上日净值-购买日净值）*赎回份额
                BigDecimal redeemIncome = lastChoProductNetVal.getDailyNetVal().subtract(choProductReq.getDailyNetVal()).multiply(redeemPortionSum);
                // 实际收益=上日实际收益-（上日净值-购买日净值）*赎回份额+当日收益
                temp.setActualIncome(lastChoProductNetVal.getActualIncome().subtract(redeemIncome).add(temp.getDayIncome()));
                // 赎回余额 = （赎回份额*上日净值）
                BigDecimal redeemBal = redeemPortionSum.multiply(lastDayNetVal);
                // 余额=上日余额-（赎回份额*上日净值）+当日收益
                temp.setBal(lastChoProductNetVal.getBal().subtract(redeemBal).add(temp.getDayIncome()));
            } else {
                // 当日收益=（本期日净值-上期日净值）*购买份额
                temp.setDayIncome(toDayNetVal.subtract(lastDayNetVal).multiply(temp.getBuyPortion()));
                // 实际收益=上日实际收益+本日当日收益
                temp.setActualIncome(lastChoProductNetVal.getActualIncome().add(temp.getDayIncome()));
                // 余额=购买金额+实际收益
                temp.setBal(temp.getBuyMoney().add(temp.getActualIncome()));
            }
            lastChoProductNetVal = temp;
            updateNetValList.add(temp);
        }
        batchUpdate(updateNetValList);
    }
    /**
     * 查询可赎回金额（即为赎回当天的净值维护余额）
     *
     * @param choProductNetVal 净值维护实体
     * @return 返回结果
     */
    public AjaxJson queryRedeemBal(ChoProductNetVal choProductNetVal) {
        AjaxJson json = new AjaxJson();
        json.setSuccess(true);
        String reqId = choProductNetVal.getReqId();
        if (StringUtils.isNotEmpty(reqId)) {
            Date netValDate = choProductNetVal.getNetValDate();
            List<ChoProductNetVal> choProductNetValList = netValDate == null ?
                    listByReqId(reqId) : listByReqId(reqId, netValDate, "=");
            Map<String, Object> params = new HashMap<>(2);
            if (!CollectionUtils.isEmpty(choProductNetValList)) {
                params.put("leftMoney", choProductNetValList.get(0).getBal());
                params.put("redeemableMoney", choProductNetValList.get(0).getBuyMoney());
                params.put("netVal", choProductNetValList.get(0).getDailyNetVal());
                params.put("dayIncome", choProductNetValList.get(0).getActualIncome());
                params.put("maxPortion", choProductNetValList.get(0).getBuyPortion());
                params.put("maxRedeemDate", choProductNetValList.get(0).getNetValDate());
            }
            json.setAttributes(params);
        }
        return json;
    }
}
