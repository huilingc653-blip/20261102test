package com.bytter.biz.inneraccount.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.inneraccount.dao.BisCorpPoolBalDao;
import com.bytter.biz.inneraccount.entity.BisCorpPoolBal;
import com.bytter.biz.inneraccount.vo.BisCorpPoolBalVO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: 多级单位余额核算-单位归集余额表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Service("bisCorpPoolBalService")
@Transactional
public class BisCorpPoolBalService extends BaseService<BisCorpPoolBal, String> implements IExcelVerifyHandler<BisCorpPoolBalVO> {
    @Autowired
    private BisCorpPoolBalDao bisCorpPoolBalDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @Override
    public BaseDao<BisCorpPoolBal, String> getEntityDao() {
        return bisCorpPoolBalDao;
    }

    /**
     * 查询 单位归集余额表 列表数据
     * @param dataTablesInput
     * @param user
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder joinSql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.BANKACC_BISCORPPOOLBAL_QUERY, user.getId()), "b.corp_id",
                true);
        joinSql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = bisCorpPoolBalDao.getDataTablesUnion(dataTablesInput, joinSql.toString());
        List<BisCorpPoolBalVO> list = output.getData();
        for (BisCorpPoolBalVO vo:list) {
            vo.setOverdraftAmt(vo.getOverdraftAmt() == null? BigDecimal.ZERO:vo.getOverdraftAmt());
            vo.setAvailBal(vo.getBal().add(vo.getOverdraftAmt()));
            vo.setInitSignName(CommonConstant.YES.equals(vo.getInitSign())?"是":"否");
        }
        output.setData(list);
        return output;
    }

    /**
     * 根据ID获取单位归集余额表
     * @param id
     * @return
     */
    public BisCorpPoolBalVO getByIdUnion(String id) {
        return bisCorpPoolBalDao.getByIdUnion(id);
    }

    /**
     * 保存 单位归集余额数据
     * @param bisCorpPoolBal
     * @param user
     */
    public void saveBisCorpPoolBal(BisCorpPoolBalVO bisCorpPoolBal, ICurrentUser user) {
        BisCorpPoolBal entity = new BisCorpPoolBal();
        BeanUtils.copyNotNullProperties(bisCorpPoolBal, entity);
        entity.setGetDate(DateUtils.getDate());
        entity.setCreateBy(user.getLoginName());
        entity.setCreateTime(DateUtils.getDate());
        this.saveEntity(entity);
    }

    /**
     * 编辑保存 单位归集余额数据
     * @param bisCorpPoolBal
     * @param user
     */
    public void updateBisCorpPoolBal(BisCorpPoolBalVO bisCorpPoolBal, ICurrentUser user) {
        BisCorpPoolBal entity = new BisCorpPoolBal();
        BeanUtils.copyNotNullProperties(bisCorpPoolBal, entity);
        entity.setGetDate(DateUtils.getDate());
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateTime(DateUtils.getDate());
        this.updateEntity(entity);
    }

    /**
     * 导入保存 单位归集余额数据
     * @param list
     * @param user
     */
    public void saveImport(List<BisCorpPoolBalVO> list, ICurrentUser user) {
        List<BisCorpPoolBal> bals = new ArrayList<>();
        for (BisCorpPoolBalVO vo:list) {
            if (!this.validRepeat(vo)) {
                throw new ParameterException(NetbankMessageEnum.NETBANK_MESSAGE_BISCORPPOOLBAL_REPEAT);
            }
            BisCorpPoolBal entity = new BisCorpPoolBal();
            BeanUtils.copyNotNullProperties(vo, entity);
            entity.setInitSign(CommonConstant.YES);
            entity.setGetDate(DateUtils.getDate());
            entity.setCreateBy(user.getLoginName());
            entity.setCreateTime(DateUtils.getDate());
            bals.add(entity);
        }
        this.batchSave(bals);
    }

    /**
     * 删除 单位归集余额数据
     * @param ids
     */
    public void delBisCorpPoolBal(String ids) {
        bisCorpPoolBalDao.delBisCorpPoolBal(ids);
    }

    /**
     * 存储校验
     * @param vo
     * @return
     */
    public boolean validRepeat(BisCorpPoolBalVO vo) {
        List<BisCorpPoolBalVO> list = bisCorpPoolBalDao.listValidByCorpIdAndBalDate(vo);
        if (EmptyUtils.isNotEmpty(list)) {
            return false;
        }
        return true;
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(BisCorpPoolBalVO vo) {
        return null;
    }

    @Override
    public void verifyHandler(List<BisCorpPoolBalVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            //单位集合
            HashMap<String, CorpDTO> corpMap = new HashMap<>();
            //币种集合
            HashMap<String, String> curMap = new HashMap<>();
            //重复数据集合
            HashMap<String, String> repeatDataMap = new HashMap<>();
            for (BisCorpPoolBalVO vo : list) {
                List<String> msgList = new ArrayList<>();
                // 判断单位在系统中是否存在
                CorpDTO corp = corpMap.get(vo.getCorpName());
                if (corp == null) {
                    corp = this.corpService.getCorpIdByName(vo.getCorpName());
                    corpMap.put(vo.getCorpName(), corp);
                }
                if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
                    msgList.add("nis.netbank.message.corp.null");
                } else {
                    // 校验单位是否有权限
                    ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
                    List<String> userCorpIds = corpService.getUserCorpIds(ResourceConstant.BANKACC_BISCORPPOOLBAL_QUERY, currentUser.getId());
                    if (!userCorpIds.contains(corp.getId())){
                        msgList.add(vo.getCorpName()+"没有操作权限");
                    }else{
                        vo.setCorpId(corp.getId());
                    }
                }

                //校验币别
                String curId = curMap.get(vo.getCurName());
                if (StringUtils.isBlank(curId)) {
                    curId = btCurrencyService.getCurrencyByName(vo.getCurName());
                    curMap.put(vo.getCurName(), curId);
                }
                if (EmptyUtils.isEmpty(curId)) {
                    msgList.add("cms.guacontract.cur.error");
                } else {
                    vo.setCurId(curId);
                }

                //余额不能为空且必须必须要大于等于0
                if (EmptyUtils.isEmpty(vo.getBal())) {
                    msgList.add("bt.bankaccdtl.message.amt.notzero");
                }

                // 重复校验
                String key = vo.getCorpId() + vo.getCurId() + vo.getBalDate();
                if (repeatDataMap.containsKey(key)) {
                    msgList.add("netbank.message.biscorppoolbal.repeat");
                } else {
                    if (!validRepeat(vo)) {
                        msgList.add("netbank.message.biscorppoolbal.repeat");
                    }
                }
                repeatDataMap.put(key, key);
                vo.addErrorMsg(msgList);
            }
        }
    }

    /**
     * 根据单位和余额日期，获取余额
     * @param vo
     * @return
     */
    public Map<String, BisCorpPoolBalVO> getBalMap(BisCorpPoolBalVO vo) {
        List<BisCorpPoolBalVO> list = bisCorpPoolBalDao.listByCorpIdAndBalDate(vo);
        // 根据单位Id 和 币别Id 分组
        Map<String, BisCorpPoolBalVO> map = list.stream()
                .collect(Collectors.toMap(
                        balVO -> balVO.getCorpId() + CommonConstant.COMMA_SPLIT + balVO.getCurId(),
                        balVO -> balVO,
                        (oldVal, newVal) -> newVal
                ));
        if (CollectionUtil.isEmpty(map)) {
            map = new HashMap<>();
        }
        return map;
    }

    /**
     * 获取余额信息
     * @param corpId
     * @param curId
     * @param balDate
     * @return
     */
    public BisCorpPoolBalVO getByBalDate(String corpId, String curId, Date balDate) {
        BisCorpPoolBalVO vo = new BisCorpPoolBalVO();
        vo.setCorpId(corpId);
        vo.setCurId(curId);
        vo.setBalDate(balDate);
        List<BisCorpPoolBalVO> list = bisCorpPoolBalDao.listValidByCorpIdAndBalDate(vo);
        if (EmptyUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取所有的初始化余额
     * @return
     */
    public List<BisCorpPoolBal> listByInitSign() {
        return bisCorpPoolBalDao.listBy("initSign", CommonConstant.YES);
    }

    /**
     * 根据单位和币种，获取初始化余额
     * @param corpId
     * @param curId
     * @return
     */
    public BisCorpPoolBalVO getInitBalByCorpId(String corpId, String curId) {
        return bisCorpPoolBalDao.getInitBalByCorpId(corpId, curId);
    }

    public List<BisCorpPoolBalVO> getLevelCorpBalances(int corpLevel) {
        return bisCorpPoolBalDao.getLevelCorpBalances(corpLevel);
    }
}
